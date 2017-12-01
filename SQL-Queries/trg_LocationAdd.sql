DROP TRIGGER IF EXISTS UNKCOEDatabase.trg_LocationAdd;
DELIMITER $$

CREATE TRIGGER UNKCOEDatabase.trg_LocationAdd AFTER INSERT
	ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    
	SET 		rowCount = 0;
    
    SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.Location L
		WHERE L.Location =  NEW.Location;
    
    IF rowCount = 0 THEN
		INSERT INTO UNKCOEDatabase.Location (Location)
		VALUES (NEW.Location);
     
	END IF;
END $$ 

DELIMITER ;
