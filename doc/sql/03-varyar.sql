drop table if exists t_org;
create table t_org (
  org_id           bigint(20)      not null auto_increment    comment '机构id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  org_name          varchar(50)      default ''                comment '机构名称',
  org_no            varchar(50)     default ''                 comment '机构编号',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  leaderphone       varchar(11)     default null               comment '联系电话',
  type              char(1)         default '0'                comment '机构类型（0企业机构 1个人机构）',
  adress            varchar(200)     default null              comment '机构地址',
  phone1            varchar(11)     default null               comment '第一服务电话',
  phone2            varchar(11)     default null               comment '第二服务电话',
  phone3            varchar(11)     default null               comment '第三服务电话',
  status            char(1)         default '0'                comment '机构状态（0开通 1关闭）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (org_id)
) engine=innodb auto_increment=100 comment = '机构表';

INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (100, 0, '0', '若依科技', '', 0, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (101, 100, '0,100', '深圳总公司', '', 1, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (102, 100, '0,100', '长沙分公司', '', 2, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (103, 101, '0,100,101', '研发部门', '', 1, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (104, 101, '0,100,101', '市场部门', '', 2, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (105, 101, '0,100,101', '测试部门', '', 3, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (106, 101, '0,100,101', '财务部门', '', 4, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (107, 101, '0,100,101', '运维部门', '', 5, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (108, 102, '0,100,102', '市场部门', '', 1, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);
INSERT INTO `t_org` (`org_id`, `parent_id`, `ancestors`, `org_name`, `org_no`, `order_num`, `leader`, `leaderphone`, `type`, `adress`, `phone1`, `phone2`, `phone3`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (109, 102, '0,100,102', '财务部门', '', 2, '若依', '15888888888', '0', NULL, NULL, NULL, NULL, '0', '0', 'admin', '2022-12-19 02:23:41', '', NULL);

drop table if exists t_devicegroup;
create table t_devicegroup (
  devicegroup_id           bigint(20)      not null auto_increment    comment '设备组id',
  org_id            bigint(20)      not null                   comment '机构id',
  name              varchar(50)      default ''                comment '设备组名称',
  no                varchar(50)     default ''                 comment '设备组编号',
  user_id           bigint(20)      default null               comment '运营者id',
  user_name         varchar(50)     default null               comment '运营者姓名',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (devicegroup_id)
) engine=innodb auto_increment=100 comment = '设备组表';

drop table if exists t_device;
create table t_device (
  device_id           bigint(20)      not null auto_increment    comment '设备id',
  devicegroup_id   bigint(20)      default null     comment '设备组id',
  name              varchar(50)      default ''                comment '设备名称',
  no                varchar(50)     default ''                 comment '设备编号',
  status            char(1)         default '0'                comment '状态（0未激活 1激活 2下线）',
  type              char(1)         default '0'                comment '类型（0雷达波 1监控设备）',
  register_time     datetime                                   comment '激活时间',
  location          varchar(100)     default ''                comment '设备位置',  
  family_id        bigint(20)                                  comment '家庭id',
  room_id          bigint(20)                                  comment '房间id',
  distribute_flag   char(1)         default '0'                comment '分配标志（0未分配 1已分配）',
  org_id            bigint(20)      default null               comment '机构id',
  org_name          varchar(50)      default ''                comment '机构名称',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (device_id)
) engine=innodb auto_increment=100 comment = '设备表';

drop table if exists t_member;
create table t_member (
  member_id         bigint(20)      not null auto_increment    comment '会员id',
  no                varchar(50)     default ''                 comment '会员编号',
  phone             varchar(11)      default ''                comment '会员手机号',
  password          varchar(100)    default ''                 comment '密码',
  distribute_flag   char(1)         default '0'                comment '分配标志（0未分配 1已分配）',
  user_id           bigint(20)      default null               comment '运营者id',
  user_name         varchar(50)     default null               comment '运营者姓名',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (member_id)
) engine=innodb auto_increment=100 comment = '会员表';

drop table if exists t_membercontacts;
create table t_membercontacts (
  membercontacts_id         bigint(20)      not null auto_increment    comment '会员联络人id',
  member_id         bigint(20)      not null                   comment '会员id',
  name              varchar(50)     default ''                 comment '联络人姓名',
  phone             varchar(11)      default ''                comment '联络人手机号',
  order_num         int(4)          default 0                  comment '显示顺序',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (membercontacts_id)
) engine=innodb auto_increment=100 comment = '会员联络人表';

drop table if exists t_family;
create table t_family (
  family_id        bigint(20)      not null auto_increment    comment '家庭id',
  no                varchar(50)     default ''                 comment '家庭编号',
  name              varchar(50)     default ''                 comment '家庭名称',
  adress            varchar(100)    default ''                 comment '家庭地址',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (family_id)
) engine=innodb auto_increment=100 comment = '家庭表';

drop table if exists t_member_family;
create table t_member_family (
  member_family_id         bigint(20)      not null auto_increment    comment '会员和家庭关联id',
  member_id         bigint(20)      not null                   comment '会员id',
  family_id         bigint(20)      not null                   comment '家庭id',
  create_member_id  bigint(20)      not null                   comment '创建会员id',
  share_flag          char(1)                                  comment '共享标志（0代表是 2代表非）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (member_family_id)
) engine=innodb auto_increment=100 comment = '会员和家庭关联表';

drop table if exists t_room;
create table t_room (
  room_id        bigint(20)      not null auto_increment    comment '房间id',
  name              varchar(50)     default ''                 comment '房间名称',
  family_id        bigint(20)      not null                    comment '家庭id',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (room_id)
) engine=innodb auto_increment=100 comment = '房间表';

drop table if exists t_agreement;
create table t_agreement (
  agreement_id        bigint(20)      not null auto_increment    comment '协议id',
  content              varchar(500)     default ''                 comment '协议内容',
  ver               varchar(50)     default ''                 comment '协议版本',
  type              char(1)         default '0'                comment '协议类型（0隐私协议 1app协议）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (agreement_id)
) engine=innodb auto_increment=100 comment = '协议表';

drop table if exists t_advise;
create table t_advise (
  advise_id        bigint(20)      not null auto_increment    comment '建议id',
  content              varchar(500)     default ''             comment '建议内容',
  member_id         bigint(20)      not null                   comment '会员id',
  read_flag              char(1)         default '0'           comment '已读标志（0未读 1已读）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (advise_id)
) engine=innodb auto_increment=100 comment = '建议表';

drop table if exists t_version;
create table t_version (
  version_id        bigint(20)      not null auto_increment    comment '版本id',
  content              varchar(50)     default ''              comment '版本号',
  type              char(1)         default null               comment '类型（0android 1ios）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (version_id)
) engine=innodb auto_increment=100 comment = '版本表';

drop table if exists t_event;
create table t_event (
  event_id        bigint(20)      not null auto_increment    comment '事件id',
  no                varchar(50)     default ''                comment '事件编号',
  level              varchar(50)     default ''               comment '级别',
  content           varchar(50)     default ''                comment '内容',
  operate_type          char(1)         default null          comment '操作类型（0会员操作 1后台人工）',
  member_id         bigint(20)      default null              comment '会员id',
  member_phone       varchar(11)      default null            comment '会员手机号',
  member_name       varchar(50)      default null             comment '会员姓名',
  user_id           bigint(20)      default null              comment '运营者id',
  user_name         varchar(50)     default null              comment '运营者姓名',
  operate_time       datetime                                 comment '操作时间',
  operate_flag        char(1)         default null            comment '操作标志（0未处理 1已处理）',
  create_time       datetime                                  comment '创建时间',
  update_time       datetime                                  comment '更新时间',
  primary key (event_id)
) engine=innodb auto_increment=100 comment = '事件表';

drop table if exists t_msg;
create table t_msg (
  msg_id        bigint(20)      not null auto_increment    comment '消息id',
  no                varchar(50)     default ''             comment '消息编号',
  content           varchar(50)     default ''             comment '内容',
  event_id       bigint(20)                                comment '事件id',
  operate_flag        char(1)         default null            comment '已读未读标志（0未处理 1已处理）',
  create_time       datetime                               comment '创建时间',
  update_time       datetime                               comment '更新时间',
  primary key (msg_id)
) engine=innodb auto_increment=100 comment = '消息表';