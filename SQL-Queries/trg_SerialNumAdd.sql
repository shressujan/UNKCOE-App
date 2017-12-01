DELIMITER $$

CREATE TRIGGER UNKCOEDatabase.trg_SerialNumAdd BEFORE INSERT
	ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    
	SET 		rowCount = 0;
    
    SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.SerialNum S
		WHERE S.SerialNum =  NEW.SerialNum;
    
    IF rowCount = 0 THEN
		INSERT INTO UNKCOEDatabase.SerialNum (SerialNum)
		VALUES (NEW.SerialNum);
     
	END IF;
END $$ 

DELIMITER ;
