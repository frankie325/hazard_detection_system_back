-- 创建数据库
CREATE DATABASE IF NOT EXISTS expressway_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE expressway_system;

-- 部门表
CREATE TABLE `sys_dept` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
                            `parent_id` BIGINT DEFAULT 0 COMMENT '上级部门ID',
                            `dept_name` VARCHAR(32) NOT NULL COMMENT '部门名称',
                            `dept_code` VARCHAR(64) DEFAULT '' COMMENT '部门编码',
                            `description` VARCHAR(512) DEFAULT '' COMMENT '描述',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门管理';

-- 角色表
CREATE TABLE `sys_role` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `role_name` VARCHAR(32) NOT NULL COMMENT '角色名称',
                            `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
                            `remark` VARCHAR(512) DEFAULT '' COMMENT '备注',
                            `permissions` JSON DEFAULT NULL COMMENT '权限(JSON格式)',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色管理';

-- 用户表
CREATE TABLE `sys_user` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `name` VARCHAR(16) NOT NULL COMMENT '姓名',
                            `gender` CHAR(1) NOT NULL COMMENT '性别(M-男, F-女)',
                            `username` VARCHAR(32) NOT NULL COMMENT '用户名',
                            `password` VARCHAR(128) NOT NULL COMMENT '密码(加密存储)',
                            `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
                            `id_card` VARCHAR(18) NOT NULL COMMENT '身份证号',
                            `role_id` BIGINT NOT NULL COMMENT '所属角色ID',
                            `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
                            `remark` VARCHAR(512) DEFAULT '' COMMENT '备注',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_username` (`username`),
                            KEY `idx_role_id` (`role_id`),
                            KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户管理';

-- 区域表
CREATE TABLE `sys_area` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '区域ID',
                            `area_name` VARCHAR(32) NOT NULL COMMENT '区域名称',
                            `dept_id` BIGINT NOT NULL COMMENT '负责部门ID',
                            `length` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '长度(公里)',
                            `lane_count` INT DEFAULT 0 COMMENT '车道数',
                            `coordinates` TEXT COMMENT '坐标信息(JSON格式)',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域管理';

-- 设备表
CREATE TABLE `sys_device` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备ID',
                              `device_name` VARCHAR(32) NOT NULL COMMENT '设备名称',
                              `device_code` VARCHAR(64) NOT NULL COMMENT '设备编码',
                              `device_type` CHAR(1) NOT NULL COMMENT '设备类型(C-摄像头, S-传感器)',
                              `model` VARCHAR(64) DEFAULT '' COMMENT '设备型号',
                              `install_location` VARCHAR(128) DEFAULT '' COMMENT '安装位置',
                              `ip_address` VARCHAR(32) DEFAULT '' COMMENT 'IP地址',
                              `status` CHAR(1) NOT NULL DEFAULT 'O' COMMENT '状态(O-在线, F-离线, M-维护)',
                              `area_id` BIGINT NOT NULL COMMENT '所属区域ID',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_device_code` (`device_code`),
                              KEY `idx_area_id` (`area_id`),
                              KEY `idx_device_type` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备管理';

-- 资源表
CREATE TABLE `sys_resource` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
                                `resource_name` VARCHAR(64) NOT NULL COMMENT '资源名称',
                                `resource_code` VARCHAR(64) NOT NULL COMMENT '资源编号',
                                `resource_type` VARCHAR(32) NOT NULL COMMENT '资源类型(如: 拖车, 救护车, 灭火器等)',
                                `quantity` INT NOT NULL DEFAULT 0 COMMENT '资源数量',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_resource_code` (`resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源管理';

-- 文件存储表
CREATE TABLE `sys_file` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
                            `file_name` VARCHAR(128) NOT NULL COMMENT '文件名称',
                            `file_path` VARCHAR(255) NOT NULL COMMENT '文件存储路径',
                            `file_size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小(字节)',
                            `file_type` VARCHAR(32) DEFAULT '' COMMENT '文件类型',
                            `upload_by` BIGINT NOT NULL COMMENT '上传人ID',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_upload_by` (`upload_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件存储';

-- 事件流表
CREATE TABLE `biz_event_stream` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '事件ID',
                                    `event_name` VARCHAR(128) NOT NULL COMMENT '事件名称',
                                    `event_type` VARCHAR(32) NOT NULL COMMENT '事件类型(抛洒物, 塌方, 火灾, 交通事故)',
                                    `area_id` BIGINT NOT NULL COMMENT '所属区域ID',
                                    `device_id` BIGINT NOT NULL COMMENT '所属设备ID',
                                    `location` VARCHAR(128) DEFAULT '' COMMENT '地点',
                                    `confidence` DECIMAL(5, 4) NOT NULL COMMENT '置信度',
                                    `event_time` DATETIME NOT NULL COMMENT '事件发生时间',
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_device_id` (`device_id`),
                                    KEY `idx_event_time` (`event_time`),
                                    KEY `idx_event_type` (`event_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件流';

-- 告警规则表
CREATE TABLE `biz_alarm_rule` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则ID',
                                  `rule_name` VARCHAR(32) NOT NULL COMMENT '规则名称',
                                  `hazard_type` VARCHAR(32) NOT NULL COMMENT '危害类型(抛洒物, 塌方, 火灾, 交通事故)',
                                  `associate_type` CHAR(1) NOT NULL COMMENT '关联类型(D-设备, A-区域)',
                                  `associate_ids` VARCHAR(255) NOT NULL COMMENT '关联对象ID列表(逗号分隔)',
                                  `match_condition` JSON NOT NULL COMMENT '匹配条件(JSON格式)',
                                  `alarm_level` CHAR(1) NOT NULL COMMENT '告警等级(L-低级, M-中级, H-高级, E-紧急)',
                                  `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(1-是, 0-否)',
                                  `remark` VARCHAR(512) DEFAULT '' COMMENT '备注',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_hazard_type` (`hazard_type`),
                                  KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警规则配置';

-- 告警消息表
CREATE TABLE `biz_alarm_message` (
                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '告警ID',
                                     `alarm_name` VARCHAR(128) NOT NULL COMMENT '告警名称',
                                     `alarm_level` CHAR(1) NOT NULL COMMENT '告警等级(L-低级, M-中级, H-高级, E-紧急)',
                                     `device_id` BIGINT NOT NULL COMMENT '发生设备ID',
                                     `location` VARCHAR(128) DEFAULT '' COMMENT '地点',
                                     `alarm_type` VARCHAR(32) NOT NULL COMMENT '告警分类(抛洒物, 塌方, 火灾, 交通事故)',
                                     `rule_id` BIGINT NOT NULL COMMENT '规则来源ID',
                                     `alarm_status` CHAR(1) NOT NULL DEFAULT 'O' COMMENT '告警状态(O-开启, P-处理中, C-已关闭)',
                                     `occur_time` DATETIME NOT NULL COMMENT '发生时间',
                                     `close_time` DATETIME DEFAULT NULL COMMENT '关闭时间',
                                     `close_reason` VARCHAR(32) DEFAULT '' COMMENT '关闭原因(误报, 已修复, 不需处理, 其他)',
                                     `processing_result` TEXT COMMENT '处理结果',
                                     `confirmed_by` BIGINT DEFAULT NULL COMMENT '确认人ID',
                                     `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_device_id` (`device_id`),
                                     KEY `idx_alarm_status` (`alarm_status`),
                                     KEY `idx_occur_time` (`occur_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警消息';

-- 告警附件关联表
CREATE TABLE `biz_alarm_attachment` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                        `alarm_id` BIGINT NOT NULL COMMENT '告警ID',
                                        `file_id` BIGINT NOT NULL COMMENT '文件ID',
                                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_alarm_id` (`alarm_id`),
                                        KEY `idx_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警附件关联';

-- 应急事件表
CREATE TABLE `biz_emergency_event` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '事件ID',
                                       `event_name` VARCHAR(128) NOT NULL COMMENT '事件名称',
                                       `event_level` CHAR(1) NOT NULL COMMENT '事件等级(L-低级, M-中级, H-高级, E-紧急)',
                                       `event_type` VARCHAR(32) NOT NULL COMMENT '事件类型(抛洒物, 塌方, 火灾, 交通事故)',
                                       `location` VARCHAR(128) DEFAULT '' COMMENT '地点',
                                       `status` VARCHAR(32) NOT NULL COMMENT '状态(启动, 已确认, 调度中, 处理中, 已关闭)',
                                       `receiver_id` BIGINT NOT NULL COMMENT '接收人员ID',
                                       `alarm_id` BIGINT NOT NULL COMMENT '关联告警ID',
                                       `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`),
                                       KEY `idx_alarm_id` (`alarm_id`),
                                       KEY `idx_status` (`status`),
                                       KEY `idx_receiver_id` (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急事件';

-- 事件时间线表
CREATE TABLE `biz_event_timeline` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '时间线ID',
                                      `emergency_id` BIGINT NOT NULL COMMENT '应急事件ID',
                                      `operate_time` DATETIME NOT NULL COMMENT '发生时间',
                                      `operator_id` BIGINT NOT NULL COMMENT '执行者ID',
                                      `action_type` VARCHAR(32) NOT NULL COMMENT '操作类型(确认, 资源绑定, 现场签到, 行动备注, 上传资料, 关闭事件)',
                                      `action_text` VARCHAR(512) NOT NULL COMMENT '文本说明',
                                      `resource_ids` VARCHAR(255) DEFAULT '' COMMENT '涉及资源ID列表(逗号分隔)',
                                      `file_ids` VARCHAR(255) DEFAULT '' COMMENT '涉及文件ID列表(逗号分隔)',
                                      `departure` VARCHAR(128) DEFAULT '' COMMENT '出发地',
                                      `destination` VARCHAR(128) DEFAULT '' COMMENT '目的地',
                                      `remark` VARCHAR(512) DEFAULT '' COMMENT '备注',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_emergency_id` (`emergency_id`),
                                      KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件时间线';

-- 应急事件资源关联表
CREATE TABLE `biz_emergency_resource` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                          `emergency_id` BIGINT NOT NULL COMMENT '应急事件ID',
                                          `resource_id` BIGINT NOT NULL COMMENT '资源ID',
                                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `uk_emergency_resource` (`emergency_id`, `resource_id`),
                                          KEY `idx_resource_id` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急事件资源关联';

-- 插入部门数据（按层级顺序插入，确保父部门先存在）
INSERT INTO sys_dept (parent_id, dept_name, dept_code, description, create_time, update_time) VALUES
-- 根部门：高速公路管理局（无上级部门，parent_id=0）
(0, '高速公路管理局', 'DEPT-001', '高速公路管理总局', '2023-01-15 09:00:00', '2023-01-15 09:00:00'),

-- 二级部门：监控指挥中心（上级为高速公路管理局，ID=1）
(1, '监控指挥中心', 'DEPT-001-002', '负责全路网监控调度', '2023-02-10 10:30:00', '2023-02-10 10:30:00'),

-- 三级部门：视频监控组（上级为监控指挥中心，ID=2）
(2, '视频监控组', 'DEPT-001-002-005', '视频监控设备维护', '2023-03-05 14:20:00', '2023-03-05 14:20:00'),

-- 三级部门：数据分析组（上级为监控指挥中心，ID=2）
(2, '数据分析组', 'DEPT-001-002-006', '交通数据分析处理', '2023-03-05 14:25:00', '2023-03-05 14:25:00'),

-- 二级部门：应急救援中心（上级为高速公路管理局，ID=1）
(1, '应急救援中心', 'DEPT-001-003', '负责应急事件处置', '2023-02-15 11:00:00', '2023-02-15 11:00:00'),

-- 二级部门：路政管理处（上级为高速公路管理局，ID=1）
(1, '路政管理处', 'DEPT-001-004', '负责路政执法管理', '2023-02-20 09:15:00', '2023-02-20 09:15:00'),

-- 二级部门：养护工程处（上级为高速公路管理局，ID=1）
(1, '养护工程处', 'DEPT-001-007', '负责道路养护工程', '2023-03-01 08:45:00', '2023-03-01 08:45:00'),

-- 二级部门：收费管理处（上级为高速公路管理局，ID=1）
(1, '收费管理处', 'DEPT-001-008', '负责收费站运营管理', '2023-03-10 16:30:00', '2023-03-10 16:30:00');


-- 新增区域表数据（7条），关联部门表的dept_id
INSERT INTO sys_area (
    area_name,        -- 区域名称
    dept_id,          -- 关联的部门ID（对应部门表主键）
    length,           -- 路段长度（公里）
    lane_count,       -- 车道数量
    coordinates,      -- 地理坐标（示例）
    create_time,      -- 创建时间
    update_time       -- 更新时间
)
VALUES
-- 监控指挥中心（dept_id=2）负责的区域
('京港澳高速K0-K10段', 2, 10.00, 4, 'LINESTRING(116.397222 39.908889,116.497222 39.908889)', NOW(), NOW()),
('京港澳高速K10-K20段', 2, 10.00, 4, 'LINESTRING(116.497222 39.908889,116.597222 39.908889)', NOW(), NOW()),
('京藏高速K0-K15段', 2, 15.00, 5, 'LINESTRING(116.397222 39.908889,116.547222 39.908889)', NOW(), NOW()),
-- 应急救援中心（dept_id=5）负责的区域
('大广高速K50-K60段', 5, 10.00, 4, 'LINESTRING(116.397222 39.908889,116.497222 39.908889)', NOW(), NOW()),
('大广高速K60-K70段', 5, 10.00, 4, 'LINESTRING(116.497222 39.908889,116.597222 39.908889)', NOW(), NOW()),
-- 路政管理处（dept_id=6）负责的区域
('京沪高速K20-K30段', 6, 10.00, 5, 'LINESTRING(116.397222 39.908889,116.497222 39.908889)', NOW(), NOW()),
-- 养护工程处（dept_id=7）负责的区域
('京哈高速K0-K12段', 7, 12.00, 4, 'LINESTRING(116.397222 39.908889,116.517222 39.908889)', NOW(), NOW());