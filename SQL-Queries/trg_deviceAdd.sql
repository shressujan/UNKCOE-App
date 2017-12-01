
DROP TRIGGER  IF EXISTS UNKCOEDatabase.trg_DeviceAdd;

DELIMITER $$
CREATE
    TRIGGER UNKCOEDatabase.trg_DeviceAdd AFTER INSERT
    ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    DECLARE	split_String VARCHAR(100);
    DECLARE delim VARCHAR(1);
    DECLARE	pos INT(1);
    
    
    SET	split_string = NULL;
    SET delim = ',';
    SET pos = 1;
    
    iterator:
    LOOP
    
    SELECT UNKCOEDatabase.Split_String (NEW.Devices, delim, pos) INTO split_string;
    
    -- exit the loop if the list seems empty or was null;
	-- this extra caution is necessary to avoid an endless loop in the proc.
	IF LENGTH(TRIM(split_string)) = 0 OR split_string IS NULL THEN
    LEAVE iterator;
	END IF;
    

	IF LENGTH(TRIM(split_string)) != 0 AND split_string IS NOT NULL THEN
		SET	rowCount = 0;
		SELECT COUNT(*) INTO rowCount
			FROM UNKCOEDatabase.Devices D
			WHERE D.Devices =  TRIM(split_string);
            
		  IF rowCount = 0 THEN
			INSERT INTO UNKCOEDatabase.Devices(Devices)
            VALUES (TRIM(split_string));
		END IF;
        SET pos = pos + 1;
	END IF;

    END LOOP iterator;
	
    END $$
    
DELIMITER ;