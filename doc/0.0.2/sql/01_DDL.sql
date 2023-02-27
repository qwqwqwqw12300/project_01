
/*
   修改表结构 
*/
--版本表新增字段force_update是否强制更新
ALTER TABLE t_version ADD force_update CHAR(1)  COMMENT '是否强制更新（0否 1是）' DEFAULT '0' AFTER type   ;


--事件表修改level字段注释
ALTER TABLE t_event MODIFY level varchar(50) default '' COMMENT '级别（urgent:紧急事件  normal：普通事件）';
--事件表新增字段event_type 事件消息类型
ALTER TABLE t_event ADD event_type char(2)  COMMENT '事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)' DEFAULT '' AFTER level   ;


--消息表修改event_level字段注释
ALTER TABLE t_msg MODIFY event_level varchar(50) default '' COMMENT '级别（urgent:紧急事件  normal：普通事件）';
--消息表新增字段event_type 事件消息类型
ALTER TABLE t_msg ADD event_type char(2)  COMMENT '事件消息类型：级别为紧急时细分(0:断网,1:人员跌倒,2:人员移动);普通事件(暂无)' DEFAULT '' AFTER event_level   ;


--房间表新增字段room_type房间类型
ALTER TABLE t_room ADD room_type CHAR(2)  COMMENT '房间类型:0:其他、1:书房、2:客厅、3:卧室、4:浴室、5:厨房、6:餐厅' DEFAULT '' AFTER NAME   ;


--修改协议表字段类型为text
ALTER TABLE t_agreement MODIFY content TEXT;



-- at 2023/02/27修复 调整会员表t_member  头像地址avatar长度
ALTER TABLE t_member MODIFY avatar varchar(1024) default '' COMMENT '头像地址';


