
drop table if exists t_version;
create table t_version (
  version_id        bigint(20)      not null auto_increment    comment '版本id',
  content              varchar(50)  default ''              comment '版本号',
  type              char(1)         default null               comment '类型（0android 1ios）',
  force_update      char(1)         default '0'                   comment '是否强制更新（0否 1是）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_time       datetime                                   comment '创建时间',
  update_time       datetime                                   comment '更新时间',
  primary key (version_id)
) engine=innodb auto_increment=100 comment = '版本表';



drop table if exists t_event;
create table t_event (
  event_id        bigint(20)      not null auto_increment    comment '事件id',
  no                varchar(50)     default ''                comment '事件编号',
  level              varchar(50)     default ''               comment '级别（urgent:紧急事件  normal：普通事件）',
  event_type        char(2)         default ''                comment '事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)',
  content           varchar(50)     default ''                comment '内容',
  device_id         bigint(20)                                comment '设备id',
  devicegroup_id    bigint(20)                                comment '设备组id',
  family_id         bigint(20)                                comment '家庭id',
  device_no         varchar(50)                               comment '设备编号',
  org_id            bigint(20)                                comment '机构id',
  org_name          varchar(50)                               comment '机构名称',
  operate_type          char(1)                               comment '操作类型（0会员操作 1后台人工）',
  member_id         bigint(20)                                comment '会员id',
  member_phone       varchar(11)                              comment '会员手机号',
  member_name       varchar(50)                               comment '会员姓名',
  user_id           bigint(20)                                comment '运营者id',
  user_name         varchar(50)                               comment '运营者姓名',
  operate_time       datetime                                 comment '操作时间',
  operator_id       bigint(20)        default null            comment '操作人id',
  operator_type       char(1)         default null            comment '操作人类型（0运营 1会员）',
  operate_flag        char(1)                                 comment '操作标志（0未处理 1已处理）',
  create_time       datetime                                  comment '创建时间',
  update_time       datetime                                  comment '更新时间',
  primary key (event_id)
) engine=innodb auto_increment=100 comment = '事件表';


drop table if exists t_msg;
create table t_msg (
  msg_id        bigint(20)      not null auto_increment    comment '消息id',
  msg_type                char(1)     default ''             comment '消息类型:1短信 2APP消息',
  device_type              char(1)         default '0'       comment '类型（0雷达波 1监控设备）',
  event_level              varchar(50)     default ''        comment '级别（urgent:紧急事件  normal：普通事件）',
  event_type        char(2)         default ''                comment '事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)',
  no                varchar(50)     default ''             comment '消息编号',
  content           varchar(50)     default ''             comment '内容',
  event_id       bigint(20)                                comment '事件id',
  device_id         bigint(20)                                comment '设备id',
  family_id         bigint(20)                                comment '家庭id',
  member_id       bigint(20)                                comment '处理人会员id',
  org_id            bigint(20)                                comment '机构id',
  operator        varchar (150)                                comment '操作人员',
  send_status     char (1)                                     comment '发送状态',
  reason          varchar (300)                                comment '失败原因',
  operate_flag        char(1)         default null            comment '已读未读标志（0未处理 1已处理）',
  send_time         datetime                                comment '发送时间',
  create_time       datetime                               comment '创建时间',
  update_time       datetime                               comment '更新时间',
  primary key (msg_id)
) engine=innodb auto_increment=100 comment = '消息表';