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

-- ----------------------------
-- 消息类型:1短信 2APP消息 3事件消息
-- ----------------------------
insert into sys_dict_type values(16, '消息类型', 'sys_msg_type',   '0', 'admin', sysdate(), '', null, '消息类型');
insert into sys_dict_data values(40, 1,  '短信',     '1',      'sys_msg_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:短信');
insert into sys_dict_data values(41, 2,  'APP消息',     '2',       'sys_msg_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '2:APP消息');

-- ----------------------------
-- 发送状态:0未发送 1已发送
-- ----------------------------
insert into sys_dict_type values(17, '消息类型', 'sys_send_status',   '0', 'admin', sysdate(), '', null, '消息类型');
insert into sys_dict_data values(43, 1,  '未发送',     '0',      'sys_send_status',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:未发送');
insert into sys_dict_data values(44, 2,  '已发送',     '1',       'sys_send_status',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:已发送');
insert into sys_dict_data values(null, 2,  '不予发送',     '2',       'sys_send_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '2:不予发送');
insert into sys_dict_data values(null, 3,  '发送失败',     '3',       'sys_send_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '3:发送失败');


-- ----------------------------
-- 分配标志（0未分配 1已分配）
-- ----------------------------
insert into sys_dict_type values(18, '分配标志', 'sys_distribute_flag',   '0', 'admin', sysdate(), '', null, '分配标志（0未分配 1已分配)');
insert into sys_dict_data values(45, 1,  '未分配',     '0',      'sys_distribute_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:未分配');
insert into sys_dict_data values(46, 2,  '已分配',     '1',       'sys_distribute_flag',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:已分配');

-- ----------------------------
-- 设备类型:
-- ----------------------------
insert into sys_dict_type values(null, '设备类型', 'device_type',   '0', 'admin', sysdate(), '', null, '设备类型:0雷达波 1监控设备');
insert into sys_dict_data values(null, 1,  'vayyar',     '0',      'device_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:vayyar');
insert into sys_dict_data values(null, 2,  '电子牵挂卡',     '1',      'device_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:电子牵挂卡');

-- ----------------------------
-- 设备状态（0未激活 1激活 2下线）
-- ----------------------------
insert into sys_dict_type values(null, '设备状态', 'sys_device_status',   '0', 'admin', sysdate(), '', null, '设备状态（0未激活 1激活 2下线）');
insert into sys_dict_data values(null, 1,  '未激活',     '0',      'sys_device_status',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:未激活');
insert into sys_dict_data values(null, 2,  '激活',     '1',      'sys_device_status',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:激活');
insert into sys_dict_data values(null, 3,  '下线',     '2',       'sys_device_status',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '2:下线');

-- ----------------------------
-- 共享标志（0代表是 2代表非）
-- ----------------------------
insert into sys_dict_type values(19, '共享标志', 'sys_share_flag',   '0', 'admin', sysdate(), '', null, '共享标志（0代表是 2代表非）');
insert into sys_dict_data values(47, 1,  '共享',     '0',      'sys_share_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:共享');
insert 	into sys_dict_data values(48, 2,  '非共享',     '2',       'sys_share_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '2:非共享');

-- ----------------------------
-- 操作类型（0会员操作 1后台人工）
-- ----------------------------
insert into sys_dict_type values(null, '操作类型', 'sys_operate_type',   '0', 'admin', sysdate(), '', null, '操作类型（0会员操作 1后台人工）');
insert into sys_dict_data values(null, 1,  '会员操作',     '0',      'sys_operate_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:会员操作');
insert into sys_dict_data values(null, 2,  '后台人工',     '1',      'sys_operate_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:后台人工');


-- ----------------------------
-- 服务处理方式（0电话通知 ）
-- ----------------------------
insert into sys_dict_type values(null, '服务处理方式', 'sys_served_type',   '0', 'admin', sysdate(), '', null, '服务处理方式（0电话通知 ）');
insert into sys_dict_data values(null, 1,  '电话通知',     '0',      'sys_served_type',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:电话通知');

-- ----------------------------
-- 设备在线状态（0离线 1在线 ）
-- ----------------------------
insert into sys_dict_type values(null, '设备在线状态', 'sys_online_flag',   '0', 'admin', sysdate(), '', null, '设备在线状态（0离线 1在线 ）');
insert into sys_dict_data values(null, 1,  '离线',     '0',      'sys_online_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:离线');
insert into sys_dict_data values(null, 2,  '在线',     '1',      'sys_online_flag',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:在线');

-- ----------------------------
-- 事件等级（normal普通 urgent紧急 ）
-- ----------------------------
insert into sys_dict_type values(null, '事件等级', 'event_level',   '0', 'admin', sysdate(), '', null, '事件等级（normal普通 urgent紧急 ）');
insert into sys_dict_data values(null, 1,  '普通',     'normal',      'event_level',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, 'normal:普通');
insert into sys_dict_data values(null, 2,  '紧急',     'urgent',      'event_level',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, 'urgent:紧急');
