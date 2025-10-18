create table public.nu_goals
(
	id_             uuid    DEFAULT uuid_generate_v7() NOT NULL primary key,
	removed_        boolean DEFAULT false              NOT NULL,
	create_date_    timestamp with time zone,
	modify_date_    timestamp with time zone,

	user_id_        uuid                               not null,
	name_           varchar(255)                       not null,
	duration_month_ integer                            not null, -- 3, 6, 12, 24, 60
	monthly_invest_ numeric(16, 5)                     not null,
	target_         numeric(16, 5)                     not null,
	estimated_date_ timestamp                          not null,
	balance_        numeric(16, 5)                     not null,
	completed_      boolean                            not null default false
);

drop trigger if exists nu_goals__date on public.nu_goals;
create trigger nu_goals__date
	before insert or
		update on public.nu_goals for each row
execute function public.create_modify_date();
