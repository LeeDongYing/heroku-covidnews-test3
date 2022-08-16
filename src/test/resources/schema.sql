create table news
(
    newsId        int auto_increment        primary key,
    title         varchar(1024) not null,
    content       longtext                   not null,
    connectUrl    varchar(1000)              not null,
    created_date  timestamp                  not null,
    modified_date timestamp                  not null,
    status        varchar(20)                not null
);
create table data
(
    explanation   varchar(200)   null,
    name          varchar(200)   null,
    connection    varchar(1024)  null,
    connectionUrl varchar(500)               not null
);