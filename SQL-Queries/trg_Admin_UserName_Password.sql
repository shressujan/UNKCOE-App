DELIMITER $$

CREATE TRIGGER UNKCOEDatabase.trg_Admin_UserName_PasswordAdd BEFORE INSERT
	ON UNKCOEDatabase.InventoryDatabase
    FOR EACH ROW 
    
    BEGIN
    
    DECLARE 	rowCount INT;
    
	SET 		rowCount = 0;
    
    SELECT COUNT(*) INTO rowCount
		FROM UNKCOEDatabase.Admin_UserName_Password A
		WHERE A.Admin_UserName =  NEW.Admin_UserName 
        OR A.Admin_Password = NEW.Admin_Password;
    
    IF rowCount = 0 THEN
		INSERT INTO UNKCOEDatabase.Admin_UserName_Password (Admin_UserName, Admin_Password)
		VALUES (NEW.Admin_UserName , NEW.Admin_Password);
     
	END IF;
END $$ 

DELIMITER ;
