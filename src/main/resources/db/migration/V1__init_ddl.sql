alter table if exists users
    drop constraint if exists fk_user_profile_id;
drop table if exists user_profiles cascade;
drop table if exists users cascade;
drop sequence if exists user_profile_seq;
drop sequence if exists user_seq;
create sequence user_profile_seq start with 1 increment by 50;
create sequence user_seq start with 1 increment by 50;
create table user_profiles
(
    id           integer                     default nextval('user_profile_seq') not null,
    created_date timestamp(6) with time zone default NOW()                       not null,
    full_name    varchar(255)                                                    not null,
    primary key (id)
);
create table users
(
    id              integer                     default nextval('user_seq') not null,
    user_profile_id integer unique,
    created_date    timestamp(6) with time zone default NOW()               not null,
    username        varchar(255)                                            not null unique,
    primary key (id)
);
alter table if exists users
    add constraint fk_user_profile_id foreign key (user_profile_id) references user_profiles;