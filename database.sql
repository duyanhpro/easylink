CREATE DATABASE `easylink` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;


-- easylink.tbl_device definition

CREATE TABLE `tbl_device` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `name` varchar(50) NOT NULL,
                              `description` varchar(255) DEFAULT '',
                              `location` varchar(255) DEFAULT '',
                              `street` varchar(255) DEFAULT NULL,
                              `district` varchar(255) DEFAULT NULL,
                              `city` varchar(255) DEFAULT NULL,
                              `device_token` varchar(255) DEFAULT NULL COMMENT 'IoT Device ID',
                              `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                              `lon` float DEFAULT NULL COMMENT 'Kinh do',
                              `lat` float DEFAULT NULL COMMENT 'Vi do',
                              `created_user` int(10) DEFAULT 1,
                              `created_date` datetime DEFAULT NULL,
                              `modified_user` int(10) DEFAULT NULL,
                              `modified_date` datetime DEFAULT NULL,
                              `type_id` int(10) DEFAULT 1,
                              `group_id` int(10) DEFAULT 0,
                              `tags` varchar(255) DEFAULT NULL,
                              `schema_id` int(10) DEFAULT 1,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `username` (`name`),
                              UNIQUE KEY `device_id` (`device_token`),
                              KEY `USER_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=DYNAMIC COMMENT='Trạm sạc. Mỗi trạm sạc tương ứng 1 device ID (1 thiết bị gateway duy nhất)\r\n\r\n';


-- easylink.tbl_device_group definition

CREATE TABLE `tbl_device_group` (
                                    `device_id` int(10) unsigned NOT NULL,
                                    `group_id` int(10) unsigned NOT NULL,
                                    `inherit` tinyint(1) DEFAULT 0 COMMENT 'False if direct relationship, True if not',
                                    KEY `FK_tbl_user_group_tbl_group` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Mapping between device & group. Include inherited relationship';


-- easylink.tbl_device_status definition

CREATE TABLE `tbl_device_status` (
                                     `device_token` varchar(255) NOT NULL,
                                     `telemetry` varchar(1023) NOT NULL DEFAULT '' COMMENT 'latest telemetry record',
                                     `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT 'Trạng thái hoạt động chung của trạm. 1 là OK. Khác là mã lỗi',
                                     `event_time` datetime DEFAULT NULL COMMENT 'latest event time',
                                     UNIQUE KEY `device_id` (`device_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Lưu trang thái cập nhật latest của device. Update mỗi khi nhận được message từ device.\r\nỞ pha sau sẽ chuyển sang redis để tăng hiệu năng write\r\n\r\n';

-- easylink.tbl_device_type definition

CREATE TABLE `tbl_device_type` (
                                   `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `name` varchar(100) NOT NULL,
                                   `description` varchar(255) DEFAULT '',
                                   `created_user` int(10) DEFAULT 1,
                                   `created_date` datetime DEFAULT NULL,
                                   `modified_user` int(10) DEFAULT NULL,
                                   `modified_date` datetime DEFAULT NULL,
                                   `sensors` varchar(1024) DEFAULT NULL,
                                   `schema_id` int(10) DEFAULT 1 COMMENT 'Reserved fields',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `username` (`name`),
                                   KEY `USER_NAME` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Device Type. Mang tính chất thông tin để hỗ trợ lọc trên báo cáo. Không ảnh hưởng tới hoạt động device';

-- easylink.tbl_group definition

CREATE TABLE `tbl_group` (
                             `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) NOT NULL,
                             `status` int(10) NOT NULL COMMENT '0: inactive, 1: active',
                             `created_user` int(10) DEFAULT 1,
                             `created_date` datetime DEFAULT NULL,
                             `modified_date` datetime DEFAULT NULL,
                             `modified_user` int(10) DEFAULT NULL,
                             `parent_id` int(10) unsigned DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `tbl_group_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='nhom nguoi dung';


-- easylink.tbl_parameter definition

CREATE TABLE `tbl_parameter` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `name` varchar(50) NOT NULL COMMENT 'Parameter name',
                                 `value` varchar(1024) NOT NULL COMMENT 'Parameter value',
                                 `type` varchar(255) DEFAULT '1',
                                 `created_date` datetime DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Luu cac tham so cau hinh he thong';


-- easylink.tbl_permission definition

CREATE TABLE `tbl_permission` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `resource` varchar(128) NOT NULL DEFAULT '*',
                                  `action` varchar(128) NOT NULL DEFAULT '*' COMMENT 'allowed actions on the resource. Can be multiple action separated by comma',
                                  `condition` varchar(255) DEFAULT NULL COMMENT 'Spring EL syntax to check for the permission. Action is allowed if condition is fulfilled',
                                  `allow` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0=false: deny action; 1=true: allow action',
                                  `description` varchar(255) DEFAULT NULL,
                                  `created_user` int(10) DEFAULT 1,
                                  `created_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `resource_action_instance` (`resource`,`action`,`condition`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Detail each permission\r\n';


-- easylink.tbl_role definition

CREATE TABLE `tbl_role` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `name` varchar(50) NOT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                            `editable` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '0 (false): system role, can not edit.  1(true):  normal role, can edit',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Bảng các quyền';


-- easylink.tbl_role_permission definition

CREATE TABLE `tbl_role_permission` (
                                       `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `created_date` datetime DEFAULT NULL,
                                       `created_user` int(10) DEFAULT NULL,
                                       `modified_date` datetime DEFAULT NULL,
                                       `modified_user` int(10) DEFAULT NULL,
                                       `role_id` int(10) unsigned NOT NULL,
                                       `permission_id` int(10) unsigned NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `role_id_permission_id` (`role_id`,`permission_id`),
                                       KEY `FK_tbl_role_permission_tbl_permission` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Mapping between user & group. One user can belong to many group';


-- easylink.tbl_role_tree definition

CREATE TABLE `tbl_role_tree` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `created_date` datetime DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `child_id` int(10) unsigned NOT NULL COMMENT 'ID of child role',
                                 `parent_id` int(10) unsigned NOT NULL COMMENT 'ID of parent role',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `parent_id_child_id` (`child_id`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Represent role hierachy.\r\nOne role can have many child roles. \r\nOne role can have many parent roles.\r\nChild role  will inherit all priviledges from parent roles';


-- easylink.tbl_rpc_log definition

CREATE TABLE `tbl_rpc_log` (
                               `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                               `request` varchar(5000) DEFAULT NULL,
                               `response` varchar(5000) DEFAULT NULL,
                               `status` tinyint(4) DEFAULT NULL,
                               `created_user` int(11) DEFAULT NULL,
                               `created_date` datetime DEFAULT NULL,
                               `modified_date` datetime DEFAULT NULL,
                               `modified_user` int(11) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Log of RPC command and response';


-- easylink.tbl_rule definition

CREATE TABLE `tbl_rule` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) DEFAULT NULL,
                            `condition` varchar(1024) DEFAULT NULL COMMENT 'Condition expression',
                            `action_type` varchar(50) DEFAULT NULL COMMENT 'Create alarm, Send chat, run command, call url....',
                            `action` varchar(1024) DEFAULT NULL COMMENT 'Metadata to describe action: could be json, etc...',
                            `active_time` varchar(1024) DEFAULT NULL COMMENT 'reserved:  array of time schedule to active rule',
                            `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
                            `scope` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1: global (all devices), 0: per device, 2: per city?',
                            `min_interval` int(11) NOT NULL DEFAULT 0 COMMENT 'Min interval between 2 action trigger (in seconds)',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Alarm rule';


-- easylink.tbl_rule_device definition

CREATE TABLE `tbl_rule_device` (
                                   `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `created_date` datetime DEFAULT NULL,
                                   `created_user` int(10) DEFAULT NULL,
                                   `modified_date` datetime DEFAULT NULL,
                                   `modified_user` int(10) DEFAULT NULL,
                                   `rule_id` int(10) unsigned NOT NULL,
                                   `device_id` int(10) unsigned NOT NULL,
                                   `device_token` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Showing which device/station will apply this rule';


-- easylink.tbl_rule_group definition

CREATE TABLE `tbl_rule_group` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `created_date` datetime DEFAULT NULL,
                                  `created_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `rule_id` int(10) unsigned NOT NULL,
                                  `group_id` int(10) unsigned NOT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Showing which group will apply this rule. All device in group will apply.';


-- easylink.tbl_schema definition

CREATE TABLE `tbl_schema` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) NOT NULL,
                              `data_schema` varchar(3000) DEFAULT '' COMMENT 'Data model',
                              `table_name` varchar(255) NOT NULL COMMENT 'table in analytics database',
                              `topic` varchar(255) NOT NULL COMMENT 'MQTT topic',
                              `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: active',
                              `created_user` int(10) DEFAULT 1,
                              `created_date` datetime DEFAULT NULL,
                              `modified_user` int(10) DEFAULT NULL,
                              `modified_date` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `tbl_device_type_UN` (`topic`),
                              UNIQUE KEY `tbl_table_name_UN` (`table_name`),
                              UNIQUE KEY `username` (`name`),
                              KEY `USER_NAME` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Configuration for data schema';


-- easylink.tbl_user definition

CREATE TABLE `tbl_user` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `username` varchar(50) NOT NULL,
                            `password` varchar(255) NOT NULL DEFAULT '',
                            `full_name` varchar(255) DEFAULT NULL,
                            `email` varchar(255) DEFAULT NULL,
                            `phone` varchar(20) DEFAULT NULL,
                            `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                            `type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: internal, 1: customer',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `username` (`username`),
                            KEY `USER_NAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- easylink.tbl_user_group definition

CREATE TABLE `tbl_user_group` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `created_date` datetime DEFAULT NULL,
                                  `created_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `user_id` int(10) unsigned NOT NULL,
                                  `group_id` int(10) unsigned NOT NULL,
                                  `inherit` tinyint(1) DEFAULT 0,
                                  PRIMARY KEY (`id`),
                                  KEY `FK_tbl_user_group_tbl_group` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Mapping between user & group. One user can belong to many group';


-- easylink.tbl_user_role definition

CREATE TABLE `tbl_user_role` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `created_date` datetime DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `user_id` int(10) unsigned NOT NULL,
                                 `role_id` int(10) unsigned NOT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`),
                                 KEY `FK_tbl_user_role_tbl_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Mapping between user and role. One user can has many roles. Many users may have the same role.';