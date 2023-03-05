/*
    DML  
*/

INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '24时无人检测', 'DEFAULT', 'vayyarDevice24HoursExistTask.check', '0 0/1 * * * ?', '3', '1', '0', 'admin', '2023-03-02 13:46:03', 'admin', '2023-03-02 13:59:30', '');
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, '进出监控任务', 'DEFAULT', 'vayyarDeviceAccessTask.check', '0 0/1 * * * ?', '3', '1', '0', 'admin', '2023-03-03 11:21:16', '', '2023-03-03 11:21:26', '');


-- ----------------------------
-- 设备安装位置：0 壁挂 1 顶挂
-- ----------------------------
insert into sys_dict_type values(null, '设备安装位置', 'install_position',   '0', 'admin', sysdate(), '', null, '设备安装位置(0 壁挂 1 顶挂)');
insert into sys_dict_data values(null, 1,  '壁挂',     '0',      'install_position',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '0:壁挂');
insert into sys_dict_data values(null, 2,  '顶挂',     '1',      'install_position',   '',   'info',  'N', '0', 'admin', sysdate(), '', null, '1:顶挂');
