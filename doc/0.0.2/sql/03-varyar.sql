
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
