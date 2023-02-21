
-- ----------------------------
-- 是否强制更新（0:否 1:是）
-- ----------------------------
insert into sys_dict_type values(null, '是否强制更新', 'force_update',   '0', 'admin', sysdate(), '', null, '是否强制更新（0:否 1:是）');
insert into sys_dict_data values(null, 1,  '是',     '1',      'force_update',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:是');
insert into sys_dict_data values(null, 2,  '否',     '0',      'force_update',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '0:否');

