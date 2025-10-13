CREATE TABLE IF NOT EXISTS public.nu_users
(
	id_             uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_        boolean DEFAULT false              NOT NULL,
	create_date_    timestamp with time zone,
	modify_date_    timestamp with time zone,
	modify_user_id_ uuid,
	enabled_        boolean DEFAULT true               NOT NULL,
	phone_          character varying(15) UNIQUE       NOT NULL,
	firstname_      character varying(255),
	lastname_       character varying(255),
	patronymic_     character varying(255)
);

CREATE TABLE IF NOT EXISTS public.tg_users
(
	id_                     uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_                boolean DEFAULT false              NOT NULL,
	create_date_            timestamp with time zone,
	modify_date_            timestamp with time zone,
	phone_                  character varying(30) UNIQUE       NOT NULL,
	telegram_user_id_       bigint UNIQUE                      NOT NULL,
	telegram_chat_id_       bigint                             NOT NULL,
	telegram_username_      character varying(100),
	telegram_language_code_ character varying(20),
	lastname_               character varying(100),
	firstname_              character varying(100)
);

drop trigger if exists nu_users__date on public.nu_users;
create trigger nu_users__date
	before insert or
		update on public.nu_users for each row
execute function public.create_modify_date();

drop trigger if exists nu_users__modify_user on public.nu_users;
create trigger nu_users__modify_user
	before insert or
		update on public.nu_users for each row
execute function public.modify_user();

drop trigger if exists tg_users__date on public.tg_users;
create trigger tg_users__date
	before insert or
		update on public.tg_users for each row
execute function public.create_modify_date();
