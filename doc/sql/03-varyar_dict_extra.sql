insert into sys_dict_type
values (null, '机构类型', 'org_type', '0', 'admin', sysdate(), '', null, '机构类型列表');
insert into sys_dict_data
values (null, 1, '个人机构', '1', 'org_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '机构类型-个人机构');
insert into sys_dict_data
values (null, 2, '企业机构', '0', 'org_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '机构类型-企业机构');
-- ----------------------------
-- 协议类型字典
-- ----------------------------
insert into sys_dict_type values(13, '协议类型', 'sys_agreement_type',   '0', 'admin', sysdate(), '', null, '协议类型列表');
insert into sys_dict_data values(34, 1,  '隐私协议',     '0',      'sys_agreement_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '协议类型（0:隐私协议）');
insert into sys_dict_data values(35, 2,  'app协议',     '1',       'sys_agreement_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '协议类型（1:app协议）');
-- ----------------------------
-- 已读未读标志
-- ----------------------------
insert into sys_dict_type values(14, '已读未读标志', 'sys_read_flag',   '0', 'admin', sysdate(), '', null, '已读未读标志列表');
insert into sys_dict_data values(36, 1,  '未读',     '0',      'sys_read_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:未读');
insert into sys_dict_data values(37, 2,  '已读',     '1',       'sys_read_flag',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:已读');

-- ----------------------------
-- 操作标志（0未处理 1已处理）
-- ----------------------------
insert into sys_dict_type values(15, '操作标志', 'sys_operate_flag',   '0', 'admin', sysdate(), '', null, '操作标志列表');
insert into sys_dict_data values(38, 1,  '未处理',     '0',      'sys_operate_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:未处理');
insert into sys_dict_data values(39, 2,  '已处理',     '1',       'sys_operate_flag',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:已处理');
