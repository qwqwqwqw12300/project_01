
-- ----------------------------
-- 是否强制更新（0:否 1:是）
-- ----------------------------
insert into sys_dict_type values(null, '是否强制更新', 'force_update',   '0', 'admin', sysdate(), '', null, '是否强制更新（0:否 1:是）');
insert into sys_dict_data values(null, 1,  '是',     '1',      'force_update',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:是');
insert into sys_dict_data values(null, 2,  '否',     '0',      'force_update',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '0:否');



-- ----------------------------
-- 事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)
-- ----------------------------
insert into sys_dict_type values(null, '事件消息类型', 'event_type',   '0', 'admin', sysdate(), '', null, '级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)');
insert into sys_dict_data values(null, 1,  '断网',     '0',      'event_type',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '0:断网');
insert into sys_dict_data values(null, 2,  '人员跌倒',     '1',      'event_type',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '1:人员跌倒');
insert into sys_dict_data values(null, 3,  '人员移动',     '2',      'event_type',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '2:人员移动');
