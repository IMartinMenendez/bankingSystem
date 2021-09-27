create table account_holder
(
    id              bigint       not null
        primary key,
    name            varchar(255) null,
    address         varchar(255) null,
    date_of_birth   varchar(255) null,
    mailing_address varchar(255) null,
    password        varchar(255) null
);

create table account_holder_transaction
(
    id         bigint         not null
        primary key,
    amount     decimal(19, 2) null,
    currency   varchar(255)   null,
    created_at datetime(6)    null,
    from_id    bigint         null,
    to_id      bigint         null
);

create table admin
(
    id       bigint       not null
        primary key,
    name     varchar(255) null,
    password varchar(255) null
);

create table checking_account
(
    id                               bigint         not null
        primary key,
    amount                           decimal(19, 2) null,
    currency                         varchar(255)   null,
    created_at                       datetime(6)    null,
    penalty_fee_amount               decimal(19, 2) null,
    penalty_fee_currency             varchar(255)   null,
    refreshed_at                     datetime(6)    null,
    status                           varchar(255)   null,
    primary_owner_id                 bigint         not null,
    secondary_owner_id               bigint         null,
    minimum_balance_amount           decimal(19, 2) null,
    minimum_balance_currency         varchar(255)   null,
    monthly_maintenance_fee_amount   decimal(19, 2) null,
    monthly_maintenance_fee_currency varchar(255)   null,
    secret_key                       varchar(255)   null
);

create table credit_card_account
(
    id                    bigint         not null
        primary key,
    amount                decimal(19, 2) null,
    currency              varchar(255)   null,
    created_at            datetime(6)    null,
    penalty_fee_amount    decimal(19, 2) null,
    penalty_fee_currency  varchar(255)   null,
    refreshed_at          datetime(6)    null,
    status                varchar(255)   null,
    primary_owner_id      bigint         not null,
    secondary_owner_id    bigint         null,
    credit_limit_amount   decimal(19, 2) null,
    credit_limit_currency varchar(255)   null,
    interest_rate         decimal(19, 2) null
);

create table hibernate_sequence
(
    next_val bigint null
);

create table interest_transaction
(
    id         bigint         not null
        primary key,
    amount     decimal(19, 2) null,
    currency   varchar(255)   null,
    created_at datetime(6)    null,
    from_id    bigint         null,
    to_id      bigint         null
);

create table role
(
    id   bigint       not null
        primary key,
    name varchar(255) null
);

create table savings_account
(
    id                       bigint         not null
        primary key,
    amount                   decimal(19, 2) null,
    currency                 varchar(255)   null,
    created_at               datetime(6)    null,
    penalty_fee_amount       decimal(19, 2) null,
    penalty_fee_currency     varchar(255)   null,
    refreshed_at             datetime(6)    null,
    status                   varchar(255)   null,
    primary_owner_id         bigint         not null,
    secondary_owner_id       bigint         null,
    interest_rate            decimal(19, 2) null,
    minimum_balance_amount   decimal(19, 2) null,
    minimum_balance_currency varchar(255)   null,
    secret_key               varchar(255)   null
);

create table student_checking_account
(
    id                   bigint         not null
        primary key,
    amount               decimal(19, 2) null,
    currency             varchar(255)   null,
    created_at           datetime(6)    null,
    penalty_fee_amount   decimal(19, 2) null,
    penalty_fee_currency varchar(255)   null,
    refreshed_at         datetime(6)    null,
    status               varchar(255)   null,
    primary_owner_id     bigint         not null,
    secondary_owner_id   bigint         null,
    secret_key           varchar(255)   null
);

create table third_party_user
(
    id         bigint       not null
        primary key,
    name       varchar(255) null,
    hashed_key varchar(255) null
);

create table user_roles
(
    user_id  bigint not null,
    roles_id bigint not null,
    primary key (user_id, roles_id),
        foreign key (roles_id) references role (id)
);