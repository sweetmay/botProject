create table public.users
(
    id int,
    first_name varchar(120),
    last_name varchar(120)
);

create unique index users_id_uindex
    on users (id);

alter table users
    add constraint users_pk
        primary key (id);
