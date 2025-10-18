--
-- PostgreSQL database dump
--

\restrict OeEdcVJzQf13iP5ysx4gO1rNmwwXINNbMlYdIrooEHewGBwKxkMrvNd1CTMKJyH

-- Dumped from database version 17.6 (Postgres.app)
-- Dumped by pg_dump version 17.6 (Postgres.app)

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

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA if not exists public;
CREATE EXTENSION pgcrypto SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: create_modify_date(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_modify_date() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
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

--
-- Name: create_user(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_user() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
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

--
-- Name: modify_user(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.modify_user() RETURNS trigger
    LANGUAGE plpgsql SECURITY DEFINER
    AS $$
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

--
-- Name: uuid_generate_v7(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.uuid_generate_v7() RETURNS uuid
    LANGUAGE plpgsql PARALLEL SAFE
    AS $$
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
$$;


ALTER FUNCTION public.uuid_generate_v7() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: nu_goal_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_goal_transactions (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    goal_id_ uuid NOT NULL,
    amount_ numeric(16,5) NOT NULL,
    date_ timestamp without time zone NOT NULL
);


ALTER TABLE public.nu_goal_transactions OWNER TO postgres;

--
-- Name: nu_goals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_goals (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    user_id_ uuid NOT NULL,
    name_ character varying(255) NOT NULL,
    duration_month_ integer NOT NULL,
    monthly_invest_ numeric(16,5) NOT NULL,
    target_ numeric(16,5) NOT NULL,
    estimated_date_ timestamp without time zone NOT NULL,
    balance_ numeric(16,5) NOT NULL,
    completed_ boolean DEFAULT false NOT NULL
);


ALTER TABLE public.nu_goals OWNER TO postgres;

--
-- Name: nu_otp_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_otp_requests (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    modify_user_id_ uuid,
    code_ character varying(50),
    otp_id_ uuid
);


ALTER TABLE public.nu_otp_requests OWNER TO postgres;

--
-- Name: nu_otps; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_otps (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    modify_user_id_ uuid,
    user_id_ uuid,
    code_ character varying(50),
    success_date_ timestamp with time zone,
    expire_date_ timestamp with time zone,
    phone_ character varying(50),
    session_id_ character varying(100),
    authenticator_ character varying(30)
);


ALTER TABLE public.nu_otps OWNER TO postgres;

--
-- Name: nu_sessions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_sessions (
    session_id_ character varying(100) NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    user_id_ uuid,
    phone_ character varying(50),
    data_ jsonb,
    expire_date_ timestamp with time zone,
    device_id_ uuid
);


ALTER TABLE public.nu_sessions OWNER TO postgres;

--
-- Name: nu_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_transactions (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    user_id uuid NOT NULL,
    amount_ numeric(16,5) NOT NULL,
    category_type_ character varying(255) NOT NULL,
    date_ timestamp without time zone NOT NULL
);


ALTER TABLE public.nu_transactions OWNER TO postgres;

--
-- Name: nu_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.nu_users (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    modify_user_id_ uuid,
    enabled_ boolean DEFAULT true NOT NULL,
    phone_ character varying(15) NOT NULL,
    firstname_ character varying(255),
    lastname_ character varying(255),
    patronymic_ character varying(255),
    iin_ character varying(255),
    birth_date_ timestamp with time zone
);


ALTER TABLE public.nu_users OWNER TO postgres;

--
-- Name: tg_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tg_users (
    id_ uuid DEFAULT public.uuid_generate_v7() NOT NULL,
    removed_ boolean DEFAULT false NOT NULL,
    create_date_ timestamp with time zone,
    modify_date_ timestamp with time zone,
    phone_ character varying(30) NOT NULL,
    telegram_user_id_ bigint NOT NULL,
    telegram_chat_id_ bigint NOT NULL,
    telegram_username_ character varying(100),
    telegram_language_code_ character varying(20),
    lastname_ character varying(100),
    firstname_ character varying(100),
    bot_ character varying(50) NOT NULL,
    enabled_ boolean DEFAULT true NOT NULL
);


ALTER TABLE public.tg_users OWNER TO postgres;

--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: nu_goal_transactions nu_goal_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_goal_transactions
    ADD CONSTRAINT nu_goal_transactions_pkey PRIMARY KEY (id_);


--
-- Name: nu_goals nu_goals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_goals
    ADD CONSTRAINT nu_goals_pkey PRIMARY KEY (id_);


--
-- Name: nu_otp_requests nu_otp_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_otp_requests
    ADD CONSTRAINT nu_otp_requests_pkey PRIMARY KEY (id_);


--
-- Name: nu_otps nu_otps_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_otps
    ADD CONSTRAINT nu_otps_pkey PRIMARY KEY (id_);


--
-- Name: nu_transactions nu_transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_transactions
    ADD CONSTRAINT nu_transactions_pkey PRIMARY KEY (id_);


--
-- Name: nu_users nu_users_phone__key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_users
    ADD CONSTRAINT nu_users_phone__key UNIQUE (phone_);


--
-- Name: nu_users nu_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.nu_users
    ADD CONSTRAINT nu_users_pkey PRIMARY KEY (id_);


--
-- Name: tg_users tg_users_phone__key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tg_users
    ADD CONSTRAINT tg_users_phone__key UNIQUE (phone_);


--
-- Name: tg_users tg_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tg_users
    ADD CONSTRAINT tg_users_pkey PRIMARY KEY (id_);


--
-- Name: tg_users tg_users_telegram_user_id__key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tg_users
    ADD CONSTRAINT tg_users_telegram_user_id__key UNIQUE (telegram_user_id_);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: nu_otps__phone__idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX nu_otps__phone__idx ON public.nu_otps USING btree (phone_) WHERE (removed_ = false);


--
-- Name: tg_users__chat_user_bot__uidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX tg_users__chat_user_bot__uidx ON public.tg_users USING btree (telegram_chat_id_, telegram_user_id_, bot_) WHERE (removed_ = false);


--
-- Name: nu_goal_transactions nu_goal_transactions__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_goal_transactions__date BEFORE INSERT OR UPDATE ON public.nu_goal_transactions FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_goals nu_goals__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_goals__date BEFORE INSERT OR UPDATE ON public.nu_goals FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_otp_requests nu_otp_requests__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_otp_requests__date BEFORE INSERT OR UPDATE ON public.nu_otp_requests FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_otp_requests nu_otp_requests__modify_user; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_otp_requests__modify_user BEFORE INSERT OR UPDATE ON public.nu_otp_requests FOR EACH ROW EXECUTE FUNCTION public.modify_user();


--
-- Name: nu_otps nu_otps__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_otps__date BEFORE INSERT OR UPDATE ON public.nu_otps FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_otps nu_otps__modify_user; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_otps__modify_user BEFORE INSERT OR UPDATE ON public.nu_otps FOR EACH ROW EXECUTE FUNCTION public.modify_user();


--
-- Name: nu_sessions nu_sessions__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_sessions__date BEFORE INSERT OR UPDATE ON public.nu_sessions FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_transactions nu_transactions__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_transactions__date BEFORE INSERT OR UPDATE ON public.nu_transactions FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_users nu_users__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_users__date BEFORE INSERT OR UPDATE ON public.nu_users FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- Name: nu_users nu_users__modify_user; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER nu_users__modify_user BEFORE INSERT OR UPDATE ON public.nu_users FOR EACH ROW EXECUTE FUNCTION public.modify_user();


--
-- Name: tg_users tg_users__date; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER tg_users__date BEFORE INSERT OR UPDATE ON public.tg_users FOR EACH ROW EXECUTE FUNCTION public.create_modify_date();


--
-- PostgreSQL database dump complete
--

\unrestrict OeEdcVJzQf13iP5ysx4gO1rNmwwXINNbMlYdIrooEHewGBwKxkMrvNd1CTMKJyH

