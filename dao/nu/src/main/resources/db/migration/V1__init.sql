SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

CREATE SCHEMA if not exists public;

ALTER SCHEMA public OWNER TO pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

SET search_path TO public;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE OR REPLACE FUNCTION
	uuid_generate_v7()
	RETURNS
		uuid
	LANGUAGE plpgsql
	PARALLEL SAFE
AS
$$
DECLARE
	-- The current UNIX timestamp in milliseconds
	unix_time_ms CONSTANT bytea NOT NULL DEFAULT substring(int8send((extract(epoch FROM clock_timestamp()) * 1000)::bigint) from 3);

	-- The buffer used to create the UUID, starting with the UNIX timestamp and followed by random bytes
	buffer                bytea NOT NULL DEFAULT unix_time_ms || gen_random_bytes(10);
BEGIN
	-- Set most significant 4 bits of 7th byte to 7 (for UUID v7), keeping the last 4 bits unchanged
	buffer = set_byte(buffer, 6, (b'0111' || get_byte(buffer, 6)::bit(4))::bit(8)::int);

	-- Set most significant 2 bits of 9th byte to 2 (the UUID variant specified in RFC 4122), keeping the last 6 bits unchanged
	buffer = set_byte(buffer, 8, (b'10' || get_byte(buffer, 8)::bit(6))::bit(8)::int);

	RETURN encode(buffer, 'hex');
END
$$
;


CREATE FUNCTION public.create_modify_date() RETURNS trigger
	LANGUAGE plpgsql
	SECURITY DEFINER
AS
$$
BEGIN
	IF
		TG_OP != 'UPDATE'
	THEN
		NEW.create_date_ := NOW();
	END IF;
	NEW.modify_date_
		:= NOW();
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.create_modify_date() OWNER TO postgres;

CREATE FUNCTION public.create_user() RETURNS trigger
	LANGUAGE plpgsql
	SECURITY DEFINER
AS
$$
BEGIN
	IF
		nullif(current_setting('hx.user', true), '') IS NOT NULL
	THEN
		NEW.create_user_id_ := current_setting('hx.user')::uuid;
	END IF;
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.create_user() OWNER TO postgres;

CREATE FUNCTION public.modify_user() RETURNS trigger
	LANGUAGE plpgsql
	SECURITY DEFINER
AS
$$
BEGIN
	IF
		TG_OP != 'DELETE' AND nullif(current_setting('hx.user', true), '') IS NOT NULL
	THEN
		NEW.modify_user_id_ := current_setting('hx.user')::uuid;
	END IF;
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.modify_user() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;
