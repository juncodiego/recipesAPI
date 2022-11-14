create table if not exists RECIPE
(
    id                  bigserial primary key not null,
    name                varchar(256)          not null,
    number_of_servings  integer               not null,
    category            varchar(256)          not null,
    ingredients         jsonb                 not null,
    instructions        text                  not null
);