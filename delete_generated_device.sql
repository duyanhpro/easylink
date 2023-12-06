use easylink;
DROP PROCEDURE IF EXISTS Insert6000Records;
delete from tbl_device_group where device_id in (select id from tbl_device where tags = 'Generated Device');
delete from tbl_device_status where device_token in (select device_token from tbl_device where tags = 'Generated Device');
delete from tbl_device where tags = 'Generated Device';