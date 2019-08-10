-- 用户表表的定义看 JdbcUserDetailsManager.java 或者 JdbcDaoImpl.java


-- 用户表3个字段
create table users
(
    username varchar(50) not null
        primary key,
    password varchar(50) not null,
    enabled  bit         not null
);


INSERT INTO oauth2db.users (username, password, enabled) VALUES ('aa', 'aa', true);


-- 定义用户权限表, 2个字段
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint ix_auth_username
        unique (username, authority),
    constraint fk_authorities_users
        foreign key (username) references users (username)
);

INSERT INTO oauth2db.authorities (username, authority) VALUES ('aa', 'oauth_client');


-- 组权限表
create table group_authorities
(
    group_id  bigint      not null,
    authority varchar(50) null,
    constraint fk_group_authorities_group
        foreign key (group_id) references `groups` (id)
);

-- 组用户表
create table group_members
(
    id       bigint auto_increment
        primary key,
    username varchar(50) not null,
    group_id bigint      not null,
    constraint fk_group_members_group
        foreign key (group_id) references `groups` (id)
);

-- 组表
create table `groups`
(
    id         bigint auto_increment
        primary key,
    group_name varchar(50) null
);




-- 详细结构看 JdbcClientDetailsService.java

-- token表(认证成功后, 将token存入)
-- authentication_id 认证后的认证ID
-- token_id tokenid
-- token token的序列化,其中包括了 refreshToken
-- user_name 用户名
-- client_id 客户端的id 代表是这个客户端访问授权
-- refresh_token 如果客户端允许刷新那么就会生成
-- 认证信息的序列化, 包括 用户的信息 和 客户端的信息

create table oauth_access_token
(

    authentication_id varchar(256)                        not null
        primary key,
    token_id          varchar(256)                        null,
    token             longblob                            null,
    user_name         varchar(256)                        null,
    client_id         varchar(256)                        null,
    authentication    longblob                            null,
    refresh_token     varchar(256)                        null,
    create_time       timestamp default CURRENT_TIMESTAMP null
);

-- 客户端的信息表
-- client_id 客户端id
-- client_secret 客户端密码 就像用户密码
-- resource_ids 资源id, 在这里没啥意义
-- authorized_grant_types 授权类型 ，4个类型
-- authorities 客户端认证以后拥有的权限

-- autoapprove 是否自动提交授权表单

create table oauth_client_details
(
    client_id               varchar(256)      not null
        primary key,
    client_secret           varchar(256)      not null,
    resource_ids            varchar(256)      null,
    scope                   varchar(256)      not null,
    authorized_grant_types  varchar(50)       null,
    web_server_redirect_uri varchar(256)      null,
    authorities             varchar(256)      null,
    access_token_validity   int               null,
    refresh_token_validity  int               null,
    additional_information  varchar(1024)     null,
    autoapprove             tinyint default 0 null,
    archived                tinyint default 1 null,
    trusted                 tinyint default 1 null
);

-- code 表 , code用来获取token
create table oauth_code
(
    create_time    timestamp default CURRENT_TIMESTAMP null,
    code           varchar(255)                        null,
    authentication blob                                null
);

create index code_index
    on oauth_code (code);

-- refreshToken表 如果上面的token失效了 就会把这里token对应的删了
create table oauth_refresh_token
(
    create_time    timestamp default CURRENT_TIMESTAMP null,
    token_id       varchar(256)                        null,
    token          blob                                null,
    authentication blob                                null
);

