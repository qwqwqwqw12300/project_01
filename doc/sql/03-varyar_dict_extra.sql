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
