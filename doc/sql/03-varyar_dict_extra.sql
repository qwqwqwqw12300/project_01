insert into sys_dict_type
values (null, '机构类型', 'org_type', '0', 'admin', sysdate(), '', null, '机构类型列表');
insert into sys_dict_data
values (null, 1, '个人机构', '1', 'org_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '机构类型-个人机构');
insert into sys_dict_data
values (null, 2, '企业机构', '0', 'org_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '机构类型-企业机构');
