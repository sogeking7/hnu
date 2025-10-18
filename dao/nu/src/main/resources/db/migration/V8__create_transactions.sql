create table public.nu_transactions
(
	id_            uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_       boolean DEFAULT false              NOT NULL,
	create_date_   timestamp with time zone,
	modify_date_   timestamp with time zone,

	user_id        uuid                               not null,
	amount_        numeric(16, 5)                     not null,
	category_type_ varchar(255)                       not null,
	date_          timestamp                          not null -- payment date
);

drop trigger if exists nu_transactions__date on public.nu_transactions;
create trigger nu_transactions__date
	before insert or
		update on public.nu_transactions for each row
execute function public.create_modify_date();
