alter table tg_users add column bot_ varchar(50);
update tg_users set bot_ = 'devNuBot' where bot_ is null;

alter table tg_users
	alter column bot_ set not null,
	add column if not exists enabled_ boolean not null default true;

alter table tg_users
drop constraint if exists tg_users__phone__uc;

alter table tg_users
drop constraint if exists tg_users__user_id__uc;

create unique index if not exists tg_users__chat_user_bot__uidx
	on tg_users (telegram_chat_id_, telegram_user_id_, bot_)
	where removed_ = false;
