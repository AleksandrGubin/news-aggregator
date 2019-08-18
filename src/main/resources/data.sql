create table if not exists News
(
    id          int auto_increment primary key,
    guid        varchar2(255)   not null,
    title       nvarchar2(255)  not null,
    link        varchar2(255)   not null,
    description nvarchar2(1000) null,
    pubDate     datetime        not null,
    source      int             not null,
    constraint FK_News2Site foreign key (source) references Site (id)
);

create table if not exists Site
(
    id    int auto_increment primary key,
    title nvarchar2(100) not null,
    link  varchar2(255)  not null,
    constraint UQ_SiteTitle unique (title),
    constraint  UQ_SiteLink unique (link)
);

/*
insert into Site (title, link)
values ('CNN', 'http://rss.cnn.com/rss/edition.rss');

insert into Site (title, link)
values ('Лента', 'https://lenta.ru/rss');*/
