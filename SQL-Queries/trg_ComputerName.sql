DELIMITER $$

CREATE TRIGGER UNKCOEDatabase.trg_ComputerNameAdd BEFORE INSERT
	ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    
	SET 		rowCount = 0;
    
    SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.ComputerName C
		WHERE C.ComputerName =  NEW.ComputerName;
    
    IF rowCount = 0 THEN
		INSERT INTO UNKCOEDatabase.ComputerName (ComputerName)
		VALUES (NEW.ComputerName);
     
	END IF;
END $$ 

DELIMITER ;
