# 事件表内容类型改为 text
ALTER TABLE t_event MODIFY `content` text COLLATE utf8mb4_bin  COMMENT '内容';
ALTER TABLE t_msg MODIFY `content` text COLLATE utf8mb4_bin  COMMENT '内容';
