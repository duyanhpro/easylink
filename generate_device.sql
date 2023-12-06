DELIMITER //

CREATE PROCEDURE `easylink`.`Insert6000Records`()
BEGIN
    DECLARE counter INT DEFAULT 0;
    DECLARE device_id INT;

    -- Loop to insert 6000 records
    WHILE counter < 6000 DO
        -- Insert into tbl_device
        INSERT INTO tbl_device (
            name,
            description,
            location,
            street,
            district,
            city,
            device_token,
            status,
            lon,
            lat,
            created_user,
            created_date,
            modified_user,
            modified_date,
            type_id,
            group_id,
            tags,
            schema_id
        )
        VALUES (
            CONCAT('Device', counter),
            CONCAT('Description', counter),
            CONCAT('Location', counter),
            CONCAT('Street', counter),
            CONCAT('District', counter),
            'Hanoi',
            CONCAT('Token', counter),
            FLOOR(RAND() * 2), -- Random status (0 or 1)
            RAND() * 180 - 90, -- Random longitude (-90 to 90)
            RAND() * 360 - 180, -- Random latitude (-180 to 180)
            1, -- Default created_user value
            NOW(),
            NULL,
            NULL,
            1, -- Default type_id value
            1, -- Default group_id value
            'Generated Device',
            1 -- Default schema_id value
        );

        -- Get the last inserted device_id
        SET device_id = LAST_INSERT_ID();

        -- Insert into tbl_device_group
INSERT INTO tbl_device_group (device_id, group_id, inherit)
VALUES (device_id, 1, 0); -- Replace 1 with the desired group_id

SET counter = counter + 1;
END WHILE;
END //

DELIMITER ;

-- Call the stored procedure to insert records
CALL Insert6000Records;