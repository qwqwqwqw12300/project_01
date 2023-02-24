/*
    DML  
*/

--下载地址确认后修改
insert into sys_config values(null, 'app下载地址(ios版本)', 'app.ios.download.address',      'www.apple.com',         'Y', 'admin', sysdate(), '', null, 'app下载地址(ios版本)');
insert into sys_config values(null, 'app下载地址(android版本)', 'app.android.download.address',      'www.mi.com',         'Y', 'admin', sysdate(), '', null, 'app下载地址(android版本)');



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



-- ----------------------------
-- 房间类型：0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅
-- ----------------------------
insert into sys_dict_type values(null, '事件消息类型', 'room_type',   '0', 'admin', sysdate(), '', null, '房间类型：0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅');
insert into sys_dict_data values(null, 1,  '其他',     '0',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '0:其他');
insert into sys_dict_data values(null, 2,  '书房',     '1',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '1:书房');
insert into sys_dict_data values(null, 3,  '客厅',     '2',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '2:客厅');
insert into sys_dict_data values(null, 4,  '卧室',     '3',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '3:卧室');
insert into sys_dict_data values(null, 5,  '浴室',     '4',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '4:浴室');
insert into sys_dict_data values(null, 6,  '厨房',     '5',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '5:厨房');
insert into sys_dict_data values(null, 7,  '餐厅',     '6',      'room_type',   '',   'success',  'N', '0', 'admin', sysdate(), '', null, '6:餐厅');
