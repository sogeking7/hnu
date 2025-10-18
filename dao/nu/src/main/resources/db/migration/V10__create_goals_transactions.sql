create table public.nu_goal_transactions
(
	id_          uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_     boolean DEFAULT false              NOT NULL,
	create_date_ timestamp with time zone,
	modify_date_ timestamp with time zone,

	goal_id_     uuid                               not null,
	amount_      numeric(16, 5)                     not null,
	date_        timestamp                          not null
);

drop trigger if exists nu_goal_transactions__date on public.nu_goal_transactions;
create trigger nu_goal_transactions__date
	before insert or
		update on public.nu_goal_transactions for each row
execute function public.create_modify_date();
