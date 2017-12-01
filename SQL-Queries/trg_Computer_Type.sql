DROP TRIGGER IF EXISTS UNKCOEDatabase.trg_Computer_TypeAdd;
DELIMITER $$

CREATE TRIGGER UNKCOEDatabase.trg_Computer_TypeAdd AFTER INSERT
	ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    
	SET 		rowCount = 0;
    
    SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.Computer_Type C
		WHERE C.Computer_Type =  NEW.Computer_Type;
    
    IF rowCount = 0 THEN
		INSERT INTO UNKCOEDatabase.Computer_Type (Computer_Type)
		VALUES (NEW.Computer_Type);
     
	END IF;
END $$ 

DELIMITER ;
