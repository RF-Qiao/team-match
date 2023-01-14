-- auto-generated definition
create table user
(
    id           bigint                             not null
        primary key,
    userName     varchar(256)                       not null comment '用户昵称',
    userPassword varchar(20)                        not null comment '用户密码',
    gender       int                                null comment '性别',
    email        varchar(211)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '用户状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     int      default 0                 not null comment '是否删除'
);

