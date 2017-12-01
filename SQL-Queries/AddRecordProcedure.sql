DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `AddRecord`(
	IN SerialNum VARCHAR(10),
	IN ComputerName VARCHAR(45),
    IN UserAgent VARCHAR(80),
	IN Admin_UserName VARCHAR(45),
	IN Admin_PassWord VARCHAR(45),
    IN Location VARCHAR(45),
    IN Computer_Type VARCHAR(45),
    IN Computer_Status VARCHAR(45),
	IN Devices VARCHAR(200),
	OUT error_msg VARCHAR (80))
BEGIN

	DECLARE 	rowCount INT;
	SET 		rowCount = 0;
		
	SELECT COUNT(*) INTO rowCount
	FROM UNKCOEDatabase.InventoryDatabase S
	WHERE S.SerialNum =  SerialNum;
			
		IF rowCount > 0 THEN
			SET error_msg = 'Record Already Exists!!';
		
			ELSE
				SET	rowCount = 0;
                
				SELECT COUNT(*) INTO rowCount
				FROM UNKCOEDatabase.InventoryDatabase S
				WHERE S.SerialNum =  SerialNum;
                
                IF rowCount > 0 THEN
					SET error_msg = 'ComputerNamer Already Exists!!';
				ELSE
					/*ADD NEW InventoryRecord in the InventoryDatabase*/
					INSERT INTO UNKCOEDatabase.InventoryDatabase (SerialNum, ComputerName, UserAgent,Admin_UserName, Admin_PassWord,Location, Computer_Type, Computer_Status, Devices)
					VALUES (SerialNum, ComputerName, UserAgent, Admin_UserName, Admin_PassWord,Location, Computer_Type, Computer_Status, Devices);
                    
				END IF;
                
			SET	rowCount = 0;
		
		SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.InventoryDatabase S
		WHERE S.SerialNum =  SerialNum;
			 
		/*MAKE SURE THE Record IS SUCCESSFULLY ADDED*/
				
		IF rowCount = 1 THEN
			SET error_msg = 'New Record Successfully Added';
		ELSE
			SET error_msg = 'Record Insert Error!!';
		END IF;
	END IF;
		

END$$
DELIMITER ;