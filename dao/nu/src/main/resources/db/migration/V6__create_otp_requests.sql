CREATE TABLE IF NOT EXISTS public.nu_otp_requests
(
	id_             uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_        boolean DEFAULT false              NOT NULL,
	create_date_    timestamp with time zone,
	modify_date_    timestamp with time zone,
	modify_user_id_ uuid,
	code_           character varying(50),
	otp_id_         uuid
);

drop trigger if exists nu_otp_requests__date on public.nu_otp_requests;
create trigger nu_otp_requests__date
	before insert or
		update on public.nu_otp_requests for each row
execute function public.create_modify_date();

drop trigger if exists nu_otp_requests__modify_user on public.nu_otp_requests;
create trigger nu_otp_requests__modify_user
	before insert or
		update on public.nu_otp_requests for each row
execute function public.modify_user();
