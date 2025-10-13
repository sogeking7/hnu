CREATE TABLE IF NOT EXISTS public.nu_sessions
(
	session_id_  character varying(100) NOT NULL,
	removed_     boolean DEFAULT false  NOT NULL,
	create_date_ timestamp with time zone,
	modify_date_ timestamp with time zone,
	user_id_     uuid,
	phone_       character varying(50),
	data_        jsonb,
	expire_date_ timestamp with time zone,
	device_id_   uuid
);

drop trigger if exists nu_sessions__date on public.nu_sessions;
create trigger nu_sessions__date
	before insert or
		update on public.nu_sessions for each row
execute function public.create_modify_date();
