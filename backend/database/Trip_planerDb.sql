create extension if not exists "uuid-ossp";

-- database generated with pgmodeler (postgresql database modeler).
-- pgmodeler  version: 0.9.1-beta
-- postgresql version: 10.0
-- project site: pgmodeler.com.br
-- model author: ---


-- database creation must be done outside an multicommand file.
-- these commands were put in this file only for convenience.
-- -- object: "trip_planerdb" | type: database --
-- -- drop database if exists "trip_planerdb";
-- create database "trip_planer"
-- ;
-- -- ddl-end --
-- 

-- object: public.expense | type: table --
-- drop table if exists public.expense cascade;
create table public.expense(
	id uuid not null default uuid_generate_v4(),
	name varchar(50) not null,
	cost money not null,
	"actualcost" money,
	id_budget uuid not null,
	constraint "expenses_pk" primary key (id,id_budget)

);
-- ddl-end --

-- object: public.budget | type: table --
-- drop table if exists public.budget cascade;
create table public.budget(
	id uuid not null default uuid_generate_v4(),
	founds money not null,
	id_trip uuid not null,
	constraint "budgets_pk" primary key (id)

);
-- ddl-end --

-- object: budget_fk | type: constraint --
-- alter table public.expense drop constraint if exists budget_fk cascade;
alter table public.expense add constraint budget_fk foreign key (id_budget)
references public.budget (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: public.controlpoint | type: table --
-- drop table if exists public.controlpoint cascade;
create table public.controlpoint(
	id uuid not null default uuid_generate_v4(),
	name varchar(50) not null,
	latitude float not null,
	northing bool not null,
	longitude float not null,
	easting bool not null,
	"order" smallint not null,
	id_trip uuid not null,
	constraint "controlpoints_pk" primary key (id)

);
-- ddl-end --

-- object: public.tasks | type: table --
-- drop table if exists public.tasks cascade;
create table public.tasks(
	id uuid not null default uuid_generate_v4(),
	finish bool not null,
	deadline date,
	"finishdate" date,
	id_trip uuid not null,
	id_user uuid,
	constraint "tasks_pk" primary key (id)

);
-- ddl-end --

-- object: public.comment | type: table --
-- drop table if exists public.comment cascade;
create table public.comment(
	id uuid not null default uuid_generate_v4(),
	date date not null,
	text varchar(500) not null,
	id_user uuid not null,
	id_blogentry uuid not null,
	constraint "comment_pk" primary key (id,id_blogentry)

);
-- ddl-end --

-- object: public.blogentry | type: table --
-- drop table if exists public.blogentry cascade;
create table public.blogentry(
	id uuid not null default uuid_generate_v4(),
	name varchar(100) not null,
	text varchar(500) not null,
	date date not null,
	id_blog uuid not null,
	constraint "blogentry_pk" primary key (id)

);
-- ddl-end --

-- object: public.picture | type: table --
-- drop table if exists public.picture cascade;
create table public.picture(
	id uuid not null default uuid_generate_v4(),
	location varchar(5000) not null,
	id_blogentry uuid not null,
	id_blog_blogentry uuid not null,
	constraint "picture_pk" primary key (id)

);
-- ddl-end --

-- object: blogentry_fk | type: constraint --
-- alter table public.comment drop constraint if exists blogentry_fk cascade;
alter table public.comment add constraint blogentry_fk foreign key (id_blogentry)
references public.blogentry (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: public."visibility" | type: type --
-- drop type if exists public."visibility" cascade;
create type public."visibility" as
 enum ('guests','users','participants');
-- ddl-end --

-- object: public.blog | type: table --
-- drop table if exists public.blog cascade;
create table public.blog(
	id uuid not null default uuid_generate_v4(),
	name varchar(50) not null,
	descryption varchar(5000) not null,
	visibility public."visibility" not null,
	id_trip uuid not null,
	constraint "blog_pk" primary key (id)

);
-- ddl-end --

-- object: blog_fk | type: constraint --
-- alter table public.blogentry drop constraint if exists blog_fk cascade;
alter table public.blogentry add constraint blog_fk foreign key (id_blog)
references public.blog (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: public.trip | type: table --
-- drop table if exists public.trip cascade;
create table public.trip(
	id uuid not null default uuid_generate_v4(),
	name varchar(100) not null,
	descryption varchar(5000),
	"startdate" date not null,
	"enddate" date not null,
	organizer uuid not null,
	constraint "trip_pk" primary key (id)

);
-- ddl-end --

-- object: trip_fk | type: constraint --
-- alter table public.budget drop constraint if exists trip_fk cascade;
alter table public.budget add constraint trip_fk foreign key (id_trip)
references public.trip (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: budget_uq | type: constraint --
-- alter table public.budget drop constraint if exists budget_uq cascade;
alter table public.budget add constraint budget_uq unique (id_trip);
-- ddl-end --

-- object: trip_fk | type: constraint --
-- alter table public.blog drop constraint if exists trip_fk cascade;
alter table public.blog add constraint trip_fk foreign key (id_trip)
references public.trip (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: blog_uq | type: constraint --
-- alter table public.blog drop constraint if exists blog_uq cascade;
alter table public.blog add constraint blog_uq unique (id_trip);
-- ddl-end --

-- object: trip_fk | type: constraint --
-- alter table public.controlpoint drop constraint if exists trip_fk cascade;
alter table public.controlpoint add constraint trip_fk foreign key (id_trip)
references public.trip (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: trip_fk | type: constraint --
-- alter table public.tasks drop constraint if exists trip_fk cascade;
alter table public.tasks add constraint trip_fk foreign key (id_trip)
references public.trip (id) match full
on delete cascade on update cascade;
-- ddl-end --

-- object: public."user" | type: table --
-- drop table if exists public."user" cascade;
create table public."user"(
	id uuid not null default uuid_generate_v4(),
	username varchar(50) not null,
	"passwordhash" varchar(50) not null,
	name varchar(50),
	surname varchar(50),
	constraint "user_pk" primary key (id),
	constraint username_uq unique (username)

);
-- ddl-end --

-- object: public.participants | type: table --
-- drop table if exists public.participants cascade;
create table public.participants(
	"user" uuid not null,
	trip uuid not null
);
-- ddl-end --

-- object: user_fk | type: constraint --
-- alter table public.tasks drop constraint if exists user_fk cascade;
alter table public.tasks add constraint user_fk foreign key (id_user)
references public."user" (id) match full
on delete set null on update cascade;
-- ddl-end --

-- object: user_fk | type: constraint --
-- alter table public.comment drop constraint if exists user_fk cascade;
alter table public.comment add constraint user_fk foreign key (id_user)
references public."user" (id) match full
on delete set null on update cascade;
-- ddl-end --

-- object: organizer | type: constraint --
-- alter table public.trip drop constraint if exists organizer cascade;
alter table public.trip add constraint organizer foreign key (organizer)
references public."user" (id) match full
on delete cascade on update no action;
-- ddl-end --

-- object: "user" | type: constraint --
-- alter table public.participants drop constraint if exists "user" cascade;
alter table public.participants add constraint "user" foreign key ("user")
references public."user" (id) match full
on delete no action on update no action;
-- ddl-end --

-- object: trip | type: constraint --
-- alter table public.participants drop constraint if exists trip cascade;
alter table public.participants add constraint trip foreign key (trip)
references public.trip (id) match full
on delete no action on update no action;
-- ddl-end --


