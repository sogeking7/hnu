CREATE TABLE IF NOT EXISTS public.nu_otps
(
	id_             uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_        boolean DEFAULT false              NOT NULL,
	create_date_    timestamp with time zone,
	modify_date_    timestamp with time zone,
	modify_user_id_ uuid,
	user_id_        uuid,
	code_           character varying(50),
	success_date_   timestamp with time zone,
	expire_date_    timestamp with time zone,
	phone_          character varying(50),
	session_id_     character varying(100),
	authenticator_  character varying(30)
);

CREATE INDEX nu_otps__phone__idx
	ON public.nu_otps USING btree (phone_)
	WHERE (removed_ = false);

drop trigger if exists nu_otps__date on public.nu_otps;
create trigger nu_otps__date
	before insert or
		update on public.nu_otps for each row
execute function public.create_modify_date();

drop trigger if exists nu_otps__modify_user on public.nu_otps;
create trigger nu_otps__modify_user
	before insert or
		update on public.nu_otps for each row
execute function public.modify_user();
