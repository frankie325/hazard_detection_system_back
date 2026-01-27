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
                            `parent_id` BIGINT DEFAULT 0 COMMENT '上级区域ID（0表示顶级区域）',
                            `area_name` VARCHAR(32) NOT NULL COMMENT '区域名称',
                            `dept_id` BIGINT NOT NULL COMMENT '负责部门ID',
                            `length` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '长度(公里)',
                            `lane_count` INT DEFAULT 0 COMMENT '车道数',
                            `coordinates` TEXT COMMENT '坐标信息(JSON格式)',
                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_parent_id` (`parent_id`),
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


-- 新增区域表数据（三级树形结构），关联部门表的dept_id
INSERT INTO sys_area (
    parent_id,         -- 上级区域ID（0表示顶级）
    area_name,         -- 区域名称
    dept_id,           -- 关联的部门ID（对应部门表主键）
    length,            -- 路段长度（公里）
    lane_count,        -- 车道数量
    coordinates,       -- 地理坐标（示例）
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
-- 第一级：高速公路（parent_id=0）
-- G1 京哈高速（监控指挥中心负责）
(0, 'G1 京哈高速', 2, 0.00, 0, NULL, '2023-03-10 09:00:00', '2023-03-10 09:00:00'),
-- G4 京港澳高速（监控指挥中心负责）
(0, 'G4 京港澳高速', 2, 0.00, 0, NULL, '2023-03-10 09:05:00', '2023-03-10 09:05:00'),
-- G6 京藏高速（监控指挥中心负责）
(0, 'G6 京藏高速', 2, 0.00, 0, NULL, '2023-03-10 09:10:00', '2023-03-10 09:10:00'),
-- G45 大广高速（应急救援中心负责）
(0, 'G45 大广高速', 5, 0.00, 0, NULL, '2023-03-10 09:15:00', '2023-03-10 09:15:00'),
-- G2 京沪高速（路政管理处负责）
(0, 'G2 京沪高速', 6, 0.00, 0, NULL, '2023-03-10 09:20:00', '2023-03-10 09:20:00'),

-- 第二级：隧道/路段（parent_id=第一级ID）
-- G1 京哈高速的隧道和路段
(1, 'G1 隧道A', 2, 2.50, 4, 'LINESTRING(116.397222 39.908889,116.447222 39.908889)', '2023-03-10 10:00:00', '2023-03-10 10:00:00'),
(1, 'G1 路段B', 2, 7.50, 4, 'LINESTRING(116.447222 39.908889,116.597222 39.908889)', '2023-03-10 10:05:00', '2023-03-10 10:05:00'),
-- G4 京港澳高速的隧道和路段
(2, 'G4 隧道C', 2, 3.00, 4, 'LINESTRING(116.597222 39.908889,116.697222 39.908889)', '2023-03-10 10:10:00', '2023-03-10 10:10:00'),
(2, 'G4 路段D', 2, 7.00, 4, 'LINESTRING(116.697222 39.908889,116.847222 39.908889)', '2023-03-10 10:15:00', '2023-03-10 10:15:00'),
-- G6 京藏高速的桥梁和路段
(3, 'G6 桥梁E', 2, 1.80, 5, 'LINESTRING(116.847222 39.908889,116.927222 39.908889)', '2023-03-10 10:20:00', '2023-03-10 10:20:00'),
(3, 'G6 路段F', 2, 13.20, 5, 'LINESTRING(116.927222 39.908889,117.397222 39.908889)', '2023-03-10 10:25:00', '2023-03-10 10:25:00'),
-- G45 大广高速的路段
(4, 'G45 路段G', 5, 20.00, 4, 'LINESTRING(117.397222 39.908889,117.897222 39.908889)', '2023-03-10 10:30:00', '2023-03-10 10:30:00'),
-- G2 京沪高速的路段
(5, 'G2 路段H', 6, 10.00, 5, 'LINESTRING(117.897222 39.908889,118.397222 39.908889)', '2023-03-10 10:35:00', '2023-03-10 10:35:00'),

-- 第三级：断面（parent_id=第二级ID）
-- G1 隧道A的断面
(6, 'A-1断面', 2, 0.00, 4, 'POINT(116.422222 39.908889)', '2023-03-10 11:00:00', '2023-03-10 11:00:00'),
(6, 'A-2断面', 2, 0.00, 4, 'POINT(116.432222 39.908889)', '2023-03-10 11:05:00', '2023-03-10 11:05:00'),
-- G1 路段B的断面
(7, 'B-1断面', 2, 0.00, 4, 'POINT(116.522222 39.908889)', '2023-03-10 11:10:00', '2023-03-10 11:10:00'),
-- G4 隧道C的断面
(8, 'C-1断面', 2, 0.00, 4, 'POINT(116.647222 39.908889)', '2023-03-10 11:15:00', '2023-03-10 11:15:00'),
-- G4 路段D的断面
(9, 'D-1断面', 2, 0.00, 4, 'POINT(116.772222 39.908889)', '2023-03-10 11:20:00', '2023-03-10 11:20:00'),
-- G6 桥梁E的断面
(10, 'E-1断面', 2, 0.00, 5, 'POINT(116.887222 39.908889)', '2023-03-10 11:25:00', '2023-03-10 11:25:00'),
-- G6 路段F的断面
(11, 'F-1断面', 2, 0.00, 5, 'POINT(117.162222 39.908889)', '2023-03-10 11:30:00', '2023-03-10 11:30:00'),
-- G45 路段G的断面
(12, 'G-1断面', 5, 0.00, 4, 'POINT(117.647222 39.908889)', '2023-03-10 11:35:00', '2023-03-10 11:35:00'),
-- G2 路段H的断面
(13, 'H-1断面', 6, 0.00, 5, 'POINT(118.147222 39.908889)', '2023-03-10 11:40:00', '2023-03-10 11:40:00');


-- 新增设备表数据（15条），关联区域表的area_id（关联到第三级断面）
INSERT INTO sys_device (
    device_name,        -- 设备名称
    device_code,        -- 设备编码
    device_type,        -- 设备类型(C-摄像头, S-传感器)
    model,              -- 设备型号
    install_location,   -- 安装位置
    ip_address,         -- IP地址
    status,             -- 状态(O-在线, F-离线, M-维护)
    area_id,            -- 所属区域ID（关联到第三级断面）
    create_time,        -- 创建时间
    update_time         -- 更新时间
)
VALUES
-- G1 隧道A的断面设备（area_id=14,15）
('隧道摄像头A-01', 'CAM-A-01', 'C', 'Hikvision DS-2CD3T45', '隧道入口左侧护栏', '192.168.1.10', 'O', 14, '2023-03-15 09:00:00', '2023-03-15 09:00:00'),
('隧道摄像头A-02', 'CAM-A-02', 'C', 'Hikvision DS-2CD3T45', 'A-1断面左侧护栏', '192.168.1.11', 'O', 14, '2023-03-15 09:05:00', '2023-03-15 09:05:00'),
('隧道气象站A-03', 'MET-A-03', 'S', 'Vaisala WXT536', '隧道外右侧紧急车道', '192.168.1.12', 'O', 15, '2023-03-15 09:10:00', '2023-03-15 09:10:00'),
-- G1 路段B的断面设备（area_id=16）
('路段雷达B-12', 'RAD-B-12', 'S', 'Navtech RPS-97', '路段B桥面中央', '192.168.2.12', 'M', 16, '2023-03-16 10:00:00', '2023-03-20 14:30:00'),
('路段摄像头B-01', 'CAM-B-01', 'C', 'Hikvision DS-2CD3T45', '路段B起点龙门架', '192.168.2.20', 'O', 16, '2023-03-16 10:15:00', '2023-03-16 10:15:00'),
('诱导屏B-02', 'LED-B-02', 'S', 'LED-X120', '分流口', '192.168.2.22', 'F', 16, '2023-03-16 10:30:00', '2023-03-18 08:00:00'),
-- G6 桥梁E的断面设备（area_id=20）
('桥梁摄像头C-01', 'CAM-C-01', 'C', 'Hikvision DS-2CD3T45', '桥梁中央隔离带', '192.168.3.30', 'O', 20, '2023-03-17 11:00:00', '2023-03-17 11:00:00'),
('桥梁气象站C-05', 'MET-C-05', 'S', 'Vaisala WXT536', '桥梁右侧护栏', '192.168.3.55', 'O', 20, '2023-03-17 11:15:00', '2023-03-17 11:15:00'),
('桥梁传感器C-10', 'SEN-C-10', 'S', 'Bosch SPC-200', '桥梁结构监测点', '192.168.3.60', 'O', 20, '2023-03-17 11:30:00', '2023-03-17 11:30:00'),
-- G45 路段G的断面设备（area_id=22）
('路段摄像头D-01', 'CAM-D-01', 'C', 'Hikvision DS-2CD3T45', '路段D起点', '192.168.4.40', 'O', 22, '2023-03-18 13:00:00', '2023-03-18 13:00:00'),
('诱导屏D-02', 'LED-D-02', 'S', 'LED-X120', '路段D分流口', '192.168.4.42', 'F', 22, '2023-03-18 13:15:00', '2023-03-19 16:00:00'),
-- G6 路段F的断面设备（area_id=21）
('隧道摄像头E-01', 'CAM-E-01', 'C', 'Hikvision DS-2CD3T45', '隧道E入口', '192.168.5.50', 'O', 21, '2023-03-19 14:00:00', '2023-03-19 14:00:00'),
('边缘服务器E-1', 'SRV-E-01', 'S', 'Dell R740', '服务区机房', '192.168.100.5', 'O', 21, '2023-03-19 14:15:00', '2023-03-19 14:15:00'),
-- G2 路段H的断面设备（area_id=23）
('路段摄像头F-01', 'CAM-F-01', 'C', 'Hikvision DS-2CD3T45', '路段F中央', '192.168.6.60', 'O', 23, '2023-03-20 15:00:00', '2023-03-20 15:00:00'),
('路段雷达F-02', 'RAD-F-02', 'S', 'Navtech RPS-97', '路段F终点', '192.168.6.62', 'M', 23, '2023-03-20 15:15:00', '2023-03-21 09:00:00');


-- 新增角色表数据（6条）
INSERT INTO sys_role (
    role_name,         -- 角色名称
    dept_id,           -- 所属部门ID
    remark,            -- 备注
    permissions,       -- 权限(JSON格式)
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
    ('超级管理员', 1, '系统最高权限管理员，拥有所有按钮操作权限',
     '{"user":["add","edit","view","delete"],"dept":["add","edit","view","delete"],"role":["add","edit","view","delete"],"area":["add","edit","view","delete"],"device":["add","edit","view","delete"],"alarm":["edit"],"emergency":["edit"]}',
     '2023-03-01 09:00:00', '2023-03-01 09:00:00'),

    ('系统管理员', 1, '拥有系统所有权限',
     '{"user":["add","edit","view","delete"],"dept":["add","edit","view","delete"],"role":["add","edit","view","delete"],"area":["add","edit","view","delete"],"device":["add","edit","view","delete"],"alarm":["edit"],"emergency":["edit"]}',
     '2023-03-01 09:10:00', '2023-03-01 09:10:00'),

    ('监控指挥员', 2, '负责实时监控和事件确认',
     '{"user":["view"],"dept":["view"],"role":["view"],"area":["view","edit"],"device":["view","edit"],"alarm":["edit"],"emergency":["view"]}',
     '2023-03-01 09:20:00', '2023-03-01 09:20:00'),

    ('应急指挥员', 5, '负责应急事件处置和资源调度',
     '{"user":["view"],"dept":["view"],"role":["view"],"area":["view"],"device":["view"],"alarm":["edit"],"emergency":["edit"]}',
     '2023-03-01 09:30:00', '2023-03-01 09:30:00'),

    ('数据分析员', 4, '负责数据统计和报表生成',
     '{"user":["view"],"dept":["view"],"role":["view"],"area":["view"],"device":["view"],"alarm":["view"],"emergency":["view"]}',
     '2023-03-01 09:40:00', '2023-03-01 09:40:00'),

    ('普通用户', 2, '基础查看权限',
     '{"user":["view"],"dept":["view"],"role":["view"],"area":["view"],"device":["view"],"alarm":["view"],"emergency":["view"]}',
     '2023-03-01 09:50:00', '2023-03-01 09:50:00');


-- 新增用户表数据（8条）
INSERT INTO sys_user (
    name,              -- 姓名
    gender,            -- 性别(M-男, F-女)
    username,          -- 用户名
    password,          -- 密码(加密存储，这里用明文示例)
    phone,             -- 手机号
    id_card,           -- 身份证号
    role_id,           -- 所属角色ID
    dept_id,           -- 所属部门ID
    remark,            -- 备注
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
    ('张伟', 'M', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138001', '110101199001011234', 1, 1, '系统管理员账号', '2023-03-01 10:00:00', '2023-03-01 10:00:00'),
    ('李娜', 'F', 'zhangwei', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138002', '110101199001011235', 2, 2, '监控指挥员', '2023-03-01 10:10:00', '2023-03-01 10:10:00'),
    ('王强', 'M', 'lina', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138003', '110101199001011236', 2, 3, '应急指挥员', '2023-03-01 10:20:00', '2023-03-01 10:20:00'),
    ('刘芳', 'F', 'wangqiang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138004', '110101199001011237', 3, 4, '数据分析员', '2023-03-01 10:30:00', '2023-03-01 10:30:00'),
    ('陈明', 'M', 'liufang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138005', '110101199001011238', 4, 2, '监控指挥员', '2023-03-01 10:40:00', '2023-03-01 10:40:00'),
    ('赵静', 'F', 'chenming', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138006', '110101199001011239', 2, 5, '应急指挥员', '2023-03-01 10:50:00', '2023-03-01 10:50:00'),
    ('孙磊', 'M', 'zhaojing', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138007', '110101199001011240', 3, 6, '路政管理员', '2023-03-01 11:00:00', '2023-03-01 11:00:00'),
    ('周敏', 'F', 'sunlei', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138008', '110101199001011241', 5, 2, '普通用户', '2023-03-01 11:10:00', '2023-03-01 11:10:00');


-- 新增资源表数据（10条）
INSERT INTO sys_resource (
    resource_name,     -- 资源名称
    resource_code,     -- 资源编号
    resource_type,     -- 资源类型
    quantity,          -- 资源数量
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
    ('重型拖车', 'RES-TOW-001', '拖车', 5, '2023-03-01 12:00:00', '2023-03-01 12:00:00'),
    ('轻型拖车', 'RES-TOW-002', '拖车', 8, '2023-03-01 12:05:00', '2023-03-01 12:05:00'),
    ('救护车', 'RES-AMB-001', '救护车', 3, '2023-03-01 12:10:00', '2023-03-01 12:10:00'),
    ('消防车', 'RES-FIRE-001', '消防车', 4, '2023-03-01 12:15:00', '2023-03-01 12:15:00'),
    ('工程车', 'RES-ENG-001', '工程车', 6, '2023-03-01 12:20:00', '2023-03-01 12:20:00'),
    ('灭火器', 'RES-EXT-001', '灭火器', 50, '2023-03-01 12:25:00', '2023-03-01 12:25:00'),
    ('应急照明灯', 'RES-LIGHT-001', '照明设备', 30, '2023-03-01 12:30:00', '2023-03-01 12:30:00'),
    ('应急发电机', 'RES-GEN-001', '发电设备', 10, '2023-03-01 12:35:00', '2023-03-01 12:35:00'),
    ('应急物资箱', 'RES-BOX-001', '应急物资', 20, '2023-03-01 12:40:00', '2023-03-01 12:40:00'),
    ('交通锥', 'RES-CONE-001', '交通设施', 200, '2023-03-01 12:45:00', '2023-03-01 12:45:00');


-- 新增文件存储表数据（5条）
INSERT INTO sys_file (
    file_name,         -- 文件名称
    file_path,         -- 文件存储路径
    file_size,         -- 文件大小(字节)
    file_type,         -- 文件类型
    upload_by,          -- 上传人ID
    create_time         -- 创建时间
)
VALUES
    ('事件截图_20240101_001.jpg', '/uploads/2024/01/01/event_001.jpg', 1024000, 'image/jpeg', 2, '2024-01-01 14:30:00'),
    ('事件截图_20240101_002.jpg', '/uploads/2024/01/01/event_002.jpg', 1536000, 'image/jpeg', 2, '2024-01-01 14:35:00'),
    ('事件视频_20240101_001.mp4', '/uploads/2024/01/01/video_001.mp4', 51200000, 'video/mp4', 3, '2024-01-01 15:00:00'),
    ('处理报告_20240101.pdf', '/uploads/2024/01/01/report_001.pdf', 204800, 'application/pdf', 3, '2024-01-01 18:00:00'),
    ('现场照片_20240102.jpg', '/uploads/2024/01/02/photo_001.jpg', 2560000, 'image/jpeg', 3, '2024-01-02 09:00:00');


-- 新增事件流表数据（10条）
INSERT INTO biz_event_stream (
    event_name,        -- 事件名称
    event_type,        -- 事件类型
    area_id,           -- 所属区域ID
    device_id,         -- 所属设备ID
    location,          -- 地点
    confidence,        -- 置信度
    event_time,        -- 事件发生时间
    create_time         -- 创建时间
)
VALUES
    ('隧道抛洒物检测', '抛洒物', 14, 1, 'G1隧道A-1断面', 0.9523, '2024-01-01 14:30:00', '2024-01-01 14:30:00'),
    ('车辆异常停车', '交通事故', 16, 4, 'G1路段B-1断面', 0.8756, '2024-01-01 14:35:00', '2024-01-01 14:35:00'),
    ('桥梁结构异常', '塌方', 20, 7, 'G6桥梁E-1断面', 0.9234, '2024-01-01 15:00:00', '2024-01-01 15:00:00'),
    ('烟雾检测', '火灾', 22, 10, 'G45路段G-1断面', 0.9876, '2024-01-01 15:30:00', '2024-01-01 15:30:00'),
    ('隧道抛洒物检测', '抛洒物', 15, 2, 'G1隧道A-2断面', 0.9123, '2024-01-02 08:00:00', '2024-01-02 08:00:00'),
    ('车辆追尾事故', '交通事故', 23, 15, 'G2路段H-1断面', 0.9456, '2024-01-02 09:15:00', '2024-01-02 09:15:00'),
    ('隧道烟雾检测', '火灾', 14, 1, 'G1隧道A-1断面', 0.8765, '2024-01-02 10:30:00', '2024-01-02 10:30:00'),
    ('路面塌陷检测', '塌方', 21, 13, 'G6路段F-1断面', 0.9345, '2024-01-02 11:45:00', '2024-01-02 11:45:00'),
    ('车辆逆行检测', '交通事故', 17, 8, 'G4隧道C-1断面', 0.8987, '2024-01-02 14:00:00', '2024-01-02 14:00:00'),
    ('货物散落检测', '抛洒物', 18, 11, 'G4路段D-1断面', 0.9234, '2024-01-02 16:30:00', '2024-01-02 16:30:00');


-- 新增告警规则表数据（8条）
INSERT INTO biz_alarm_rule (
    rule_name,         -- 规则名称
    hazard_type,       -- 危害类型
    associate_type,    -- 关联类型(D-设备, A-区域)
    associate_ids,      -- 关联对象ID列表(逗号分隔)
    match_condition,    -- 匹配条件(JSON格式)
    alarm_level,       -- 告警等级
    is_enabled,        -- 是否启用
    remark,            -- 备注
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
    ('抛洒物告警规则', '抛洒物', 'D', '1,2,3', '{"confidence": {"min": 0.9}}', 'H', 1, '检测到抛洒物且置信度>0.9时触发高级告警', '2023-03-01 13:00:00', '2023-03-01 13:00:00'),
    ('火灾告警规则', '火灾', 'D', '1,2,13', '{"confidence": {"min": 0.85}}', 'E', 1, '检测到火灾且置信度>0.85时触发紧急告警', '2023-03-01 13:10:00', '2023-03-01 13:10:00'),
    ('塌方告警规则', '塌方', 'D', '7,8,9', '{"confidence": {"min": 0.9}}', 'E', 1, '检测到塌方且置信度>0.9时触发紧急告警', '2023-03-01 13:20:00', '2023-03-01 13:20:00'),
    ('交通事故告警规则', '交通事故', 'D', '4,5,6,15', '{"confidence": {"min": 0.85}}', 'H', 1, '检测到交通事故且置信度>0.85时触发高级告警', '2023-03-01 13:30:00', '2023-03-01 13:30:00'),
    ('隧道区域告警规则', '抛洒物', 'A', '6,8,10', '{"confidence": {"min": 0.88}, "event_count": {"min": 2}}', 'M', 1, '隧道区域连续检测到2次以上抛洒物触发中级告警', '2023-03-01 13:40:00', '2023-03-01 13:40:00'),
    ('桥梁区域告警规则', '塌方', 'A', '10', '{"confidence": {"min": 0.92}}', 'H', 1, '桥梁区域检测到塌方触发高级告警', '2023-03-01 13:50:00', '2023-03-01 13:50:00'),
    ('路段区域告警规则', '交通事故', 'A', '7,9,11,12,13', '{"confidence": {"min": 0.9}}', 'M', 1, '路段区域检测到交通事故触发中级告警', '2023-03-01 14:00:00', '2023-03-01 14:00:00'),
    ('综合告警规则', '抛洒物', 'D', '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15', '{"confidence": {"min": 0.95}}', 'E', 1, '所有设备检测到高置信度抛洒物时触发紧急告警', '2023-03-01 14:10:00', '2023-03-01 14:10:00');


-- 新增告警消息表数据（8条）
INSERT INTO biz_alarm_message (
    alarm_name,        -- 告警名称
    alarm_level,       -- 告警等级
    device_id,         -- 发生设备ID
    location,          -- 地点
    alarm_type,        -- 告警分类
    rule_id,           -- 规则来源ID
    alarm_status,      -- 告警状态
    occur_time,        -- 发生时间
    close_time,        -- 关闭时间
    close_reason,      -- 关闭原因
    processing_result, -- 处理结果
    confirmed_by,      -- 确认人ID
    create_time,       -- 创建时间
    update_time        -- 补充缺失的更新时间字段
)
VALUES
    -- 第1行：完整赋值，无问题
    ('隧道A-1断面抛洒物告警', 'H', 1, 'G1隧道A-1断面', '抛洒物', 1, 'C', '2024-01-01 14:30:00', '2024-01-01 15:30:00', '已修复', '现场清理完毕，道路恢复正常', 2, '2024-01-01 14:30:00', '2024-01-01 15:30:00'),
    -- 第2行：完整赋值，无问题
    ('路段B-1断面交通事故告警', 'H', 4, 'G1路段B-1断面', '交通事故', 4, 'C', '2024-01-01 14:35:00', '2024-01-01 17:00:00', '已修复', '事故处理完毕，车辆已移除', 3, '2024-01-01 14:35:00', '2024-01-01 17:00:00'),
    -- 第3行：修复 NULL 写法，补充 update_time
    ('桥梁E-1断面塌方告警', 'E', 7, 'G6桥梁E-1断面', '塌方', 3, 'P', '2024-01-01 15:00:00', NULL, '', NULL, 2, '2024-01-01 15:00:00', '2024-01-01 15:00:00'),
    -- 第4行：完整赋值，无问题
    ('路段G-1断面火灾告警', 'E', 10, 'G45路段G-1断面', '火灾', 2, 'C', '2024-01-01 15:30:00', '2024-01-01 18:00:00', '已修复', '火源已扑灭，现场安全', 3, '2024-01-01 15:30:00', '2024-01-01 18:00:00'),
    -- 第5行：完整赋值，无问题
    ('隧道A-2断面抛洒物告警', 'H', 2, 'G1隧道A-2断面', '抛洒物', 1, 'C', '2024-01-02 08:00:00', '2024-01-02 09:00:00', '误报', '经核实为误报', 2, '2024-01-02 08:00:00', '2024-01-02 09:00:00'),
    -- 第6行：完整赋值，无问题
    ('路段H-1断面交通事故告警', 'H', 15, 'G2路段H-1断面', '交通事故', 4, 'C', '2024-01-02 09:15:00', '2024-01-02 12:00:00', '已修复', '追尾事故处理完毕', 3, '2024-01-02 09:15:00', '2024-01-02 12:00:00'),
    -- 第7行：修复 NULL 写法，补充 update_time
    ('隧道A-1断面火灾告警', 'E', 1, 'G1隧道A-1断面', '火灾', 2, 'O', '2024-01-02 10:30:00', NULL, '', NULL, 2, '2024-01-02 10:30:00', '2024-01-02 10:30:00'),
    -- 第8行：修复 NULL 写法，补充 update_time
    ('路段F-1断面塌方告警', 'E', 13, 'G6路段F-1断面', '塌方', 3, 'P', '2024-01-02 11:45:00', NULL, '', NULL, 2, '2024-01-02 11:45:00', '2024-01-02 11:45:00');
-- 新增告警附件关联表数据（5条）
INSERT INTO biz_alarm_attachment (
    alarm_id,          -- 告警ID
    file_id,           -- 文件ID
    create_time         -- 创建时间
)
VALUES
    (1, 1, '2024-01-01 14:35:00'),
    (1, 2, '2024-01-01 14:35:00'),
    (2, 3, '2024-01-01 14:40:00'),
    (4, 4, '2024-01-01 15:35:00'),
    (6, 5, '2024-01-02 09:20:00');


-- 新增应急事件表数据（5条）
INSERT INTO biz_emergency_event (
    event_name,        -- 事件名称
    event_level,       -- 事件等级
    event_type,        -- 事件类型
    location,          -- 地点
    status,            -- 状态
    receiver_id,       -- 接收人员ID
    alarm_id,          -- 关联告警ID
    create_time,       -- 创建时间
    update_time        -- 更新时间
)
VALUES
    ('G1隧道A-1断面抛洒物应急事件', 'H', '抛洒物', 'G1隧道A-1断面', '已关闭', 2, 1, '2024-01-01 14:35:00', '2024-01-01 15:30:00'),
    ('G1路段B-1断面交通事故应急事件', 'H', '交通事故', 'G1路段B-1断面', '已关闭', 3, 2, '2024-01-01 14:40:00', '2024-01-01 17:00:00'),
    ('G6桥梁E-1断面塌方应急事件', 'E', '塌方', 'G6桥梁E-1断面', '处理中', 2, 3, '2024-01-01 15:05:00', '2024-01-02 11:45:00'),
    ('G45路段G-1断面火灾应急事件', 'E', '火灾', 'G45路段G-1断面', '已关闭', 3, 4, '2024-01-01 15:35:00', '2024-01-01 18:00:00'),
    ('G2路段H-1断面交通事故应急事件', 'H', '交通事故', 'G2路段H-1断面', '已关闭', 3, 6, '2024-01-02 09:20:00', '2024-01-02 12:00:00');


-- 新增事件时间线表数据（15条）
INSERT INTO biz_event_timeline (
    emergency_id,      -- 应急事件ID
    operate_time,      -- 发生时间
    operator_id,       -- 执行者ID
    action_type,       -- 操作类型
    action_text,       -- 文本说明
    resource_ids,      -- 涉及资源ID列表(逗号分隔)
    file_ids,          -- 涉及文件ID列表(逗号分隔)
    departure,         -- 出发地
    destination,       -- 目的地
    remark,            -- 备注
    create_time        -- 创建时间
)
VALUES
    -- 应急事件1的时间线（6条）
    (1, '2024-01-01 14:35:00', 2, '确认', '确认收到告警，启动应急响应', '', '', '', '', '', '2024-01-01 14:35:00'),
    (1, '2024-01-01 14:40:00', 2, '资源绑定', '调度拖车和应急物资', '1,2,9', '', '', '', '', '2024-01-01 14:40:00'),
    (1, '2024-01-01 14:50:00', 2, '现场签到', '到达现场开始处理', '', '', '监控指挥中心', 'G1隧道A-1断面', '', '2024-01-01 14:50:00'),
    (1, '2024-01-01 15:00:00', 2, '行动备注', '开始清理抛洒物', '', '', '', '', '', '2024-01-01 15:00:00'),
    (1, '2024-01-01 15:30:00', 2, '上传资料', '上传处理现场照片', '', '1,2', '', '', '', '2024-01-01 15:30:00'),
    (1, '2024-01-01 15:30:00', 2, '关闭事件', '事件处理完毕，关闭应急事件', '', '', '', '', '', '2024-01-01 15:30:00'),

    -- 应急事件2的时间线（6条）
    (2, '2024-01-01 14:40:00', 3, '确认', '确认收到告警，启动应急响应', '', '', '', '', '', '2024-01-01 14:40:00'),
    (2, '2024-01-01 14:45:00', 3, '资源绑定', '调度救护车和消防车', '3,4', '', '', '', '', '2024-01-01 14:45:00'),
    (2, '2024-01-01 14:55:00', 3, '现场签到', '到达现场开始处理', '', '', '应急救援中心', 'G1路段B-1断面', '', '2024-01-01 14:55:00'),
    (2, '2024-01-01 15:30:00', 3, '行动备注', '事故车辆已移除，现场清理中', '', '', '', '', '', '2024-01-01 15:30:00'),
    (2, '2024-01-01 17:00:00', 3, '上传资料', '上传处理报告', '', '3,4', '', '', '', '2024-01-01 17:00:00'),
    (2, '2024-01-01 17:00:00', 3, '关闭事件', '事件处理完毕，关闭应急事件', '', '', '', '', '', '2024-01-01 17:00:00'),

    -- 应急事件3的时间线（4条）
    (3, '2024-01-01 15:05:00', 2, '确认', '确认收到告警，启动应急响应', '', '', '', '', '', '2024-01-01 15:05:00'),
    (3, '2024-01-01 15:10:00', 2, '资源绑定', '调度工程车和应急物资', '5,8,9', '', '', '', '', '2024-01-01 15:10:00'),
    (3, '2024-01-01 15:30:00', 2, '现场签到', '到达现场开始处理', '', '', '监控指挥中心', 'G6桥梁E-1断面', '', '2024-01-01 15:30:00'),
    (3, '2024-01-02 11:45:00', 2, '行动备注', '塌方处理持续进行中', '', '', '', '', '', '2024-01-02 11:45:00');
-- 新增应急事件资源关联表数据（8条）
INSERT INTO biz_emergency_resource (
    emergency_id,      -- 应急事件ID
    resource_id,       -- 资源ID
    create_time         -- 创建时间
)
VALUES
    (1, 1, '2024-01-01 14:40:00'),
    (1, 2, '2024-01-01 14:40:00'),
    (1, 9, '2024-01-01 14:40:00'),
    (2, 3, '2024-01-01 14:45:00'),
    (2, 4, '2024-01-01 14:45:00'),
    (3, 5, '2024-01-01 15:10:00'),
    (3, 8, '2024-01-01 15:10:00'),
    (3, 9, '2024-01-01 15:10:00');