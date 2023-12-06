--liquibase formatted sql

--changeset initial-schema:1
create extension if not exists "uuid-ossp";

--changeset initial-schema:2
create table "profissionais" (
    "id"                 uuid           not null default uuid_generate_v4(),
    "created_date"       TIMESTAMP      not null default now(),
    "nome"               varchar(600)   not null,
    "cargo"              varchar(50)    not null,
    "nascimento"         date           not null,
    "is_excluido"        boolean        not null default false,
    constraint "profissional_pk" primary key ("id")
);
--rollback drop table "profissionais";

--changeset initial-schema:3
create table "contatos" (
    "id"                uuid            not null default uuid_generate_v4(),
    "created_date"      TIMESTAMP      not null default now(),
    "nome"              varchar(600)    not null,
    "contato"           varchar(1000)   not null,
    "profissional_id"   uuid            not null,
    constraint "contato_pk" primary key ("id"),
    constraint "contato_profissional_fk" foreign key ("profissional_id") references profissionais("id")
);
--rollback drop table "contatos";