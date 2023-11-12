-- easylink.tbl_alarm definition

CREATE TABLE `alarm` (
                         `device_token` varchar(50) NOT NULL,
                         `event_time` datetime DEFAULT NULL,
                         `content` varchar(255) DEFAULT NULL,
                         `type` varchar(50) DEFAULT NULL,
                         `level` tinyint(4) NOT NULL COMMENT 'Info, Warning, Error, Critical',
                         `rule_id` int(11) DEFAULT NULL COMMENT 'null or 0: sent from device. Else: rule_id'
)
    ENGINE=OLAP
DUPLICATE KEY(`device_token`)
PARTITION BY date_trunc('day', event_time)
DISTRIBUTED BY HASH(`device_token`, `event_time`)
PROPERTIES (
"replication_num" = "1",
"in_memory" = "false",
"storage_format" = "DEFAULT",
"enable_persistent_index" = "false",
"replicated_storage" = "true",
"compression" = "LZ4"
);


-- easylink.sensor_data2 definition

CREATE TABLE `sensor_data2` (
                                `device_token` varchar(255) NOT NULL COMMENT "",
                                `event_time` datetime NOT NULL COMMENT "",
                                `temp1` decimal64(16, 2) NULL COMMENT "",
                                `hum1` decimal64(16, 2) NULL COMMENT "",
                                `temp2` decimal64(16, 2) NULL COMMENT "",
                                `hum2` decimal64(16, 2) NULL COMMENT "",
                                `door1` int(11) NULL COMMENT "",
                                `door2` int(11) NULL COMMENT "",
                                `vol1` decimal64(16, 2) NULL COMMENT "",
                                `cur1` decimal64(16, 2) NULL COMMENT "",
                                `vol2` decimal64(16, 2) NULL COMMENT "",
                                `cur2` decimal64(16, 2) NULL COMMENT "",
                                `vol3` decimal64(16, 2) NULL COMMENT "",
                                `cur3` decimal64(16, 2) NULL COMMENT "",
                                `vol4` decimal64(16, 2) NULL COMMENT "",
                                `cur4` decimal64(16, 2) NULL COMMENT ""
) ENGINE=OLAP
DUPLICATE KEY(`device_token`)
PARTITION BY date_trunc('day', event_time)
DISTRIBUTED BY HASH(`device_token`, `event_time`)
PROPERTIES (
"replication_num" = "1",
"in_memory" = "false",
"storage_format" = "DEFAULT",
"enable_persistent_index" = "false",
"replicated_storage" = "true",
"compression" = "LZ4"
);