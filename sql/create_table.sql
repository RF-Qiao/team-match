-- auto-generated definition
create table user
(
    id           int auto_increment
        primary key,
    userName     varchar(256)                       not null comment '用户昵称',
    userPassword varchar(256)                       not null comment '用户密码',
    gender       int                                null comment '性别',
    email        varchar(211)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '用户状态 0-正常',
    phone        varchar(128)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     int      default 0                 not null comment '是否删除 逻辑删除',
    tags         varchar(1024)                      null comment '''标签 json 列表'''
);

-- 队伍表
create table team
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)       not null comment '队伍名称',
    description varchar(1024) null comment '描述',
    maxNum      int      default 1 not null comment '最大人数',
    expireTime  datetime null comment '过期时间',
    userId      bigint comment '用户id（队长 id）',
    status      int      default 0 not null comment '0 - 公开，1 - 私有，2 - 加密',
    password    varchar(512) null comment '密码',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete    tinyint  default 0 not null comment '是否删除'
) comment '队伍';

-- 用户队伍关系
create table user_team
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint comment '用户id',
    teamId     bigint comment '队伍id',
    joinTime   datetime null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0 not null comment '是否删除'
) comment '用户队伍关系';