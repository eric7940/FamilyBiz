DROP PROCEDURE IF EXISTS `SP_GET_SEQUENCE`;
DELIMITER $$
CREATE DEFINER=`webman`@`localhost` PROCEDURE `SP_GET_SEQUENCE`(IN `PI_SEQ_ID` VARCHAR(50) , IN `PI_SEQ_PREFIX` VARCHAR(10), OUT `PO_SEQ_NBR` INT)
  LANGUAGE SQL
  NOT DETERMINISTIC
  CONTAINS SQL
  SQL SECURITY DEFINER
  COMMENT ''
BEGIN
    DECLARE seq INT DEFAULT 0;
    
    DECLARE `cdtSelectSeq` CONDITION FOR SQLSTATE '23000';
    DECLARE EXIT HANDLER FOR `cdtSelectSeq`
    BEGIN
               XA END 'cdtSelectSeq';
               XA PREPARE 'cdtSelectSeq';
               XA ROLLBACK 'cdtSelectSeq'; 
               SELECT -1 AS `PO_SEQ_NBR`;
     END;

     XA START 'cdtSelectSeq';
     
     SELECT `SEQ_NBR` INTO seq FROM `TB_SEQ_STORE` WHERE `SEQ_ID` = PI_SEQ_ID AND `SEQ_PREFIX` = PI_SEQ_PREFIX FOR UPDATE;
     IF seq = 0 THEN
       UPDATE `TB_SEQ_STORE` SET `SEQ_PREFIX` = PI_SEQ_PREFIX, `SEQ_NBR` = 1 WHERE `SEQ_ID` = PI_SEQ_ID;
       SET seq = 1;
     ELSE
       UPDATE `TB_SEQ_STORE` SET `SEQ_NBR` = LAST_INSERT_ID(`SEQ_NBR` + 1) WHERE `SEQ_ID` = PI_SEQ_ID;
       SET seq = LAST_INSERT_ID();
     END IF;
      
     XA END 'cdtSelectSeq';

     XA PREPARE 'cdtSelectSeq';
     IF seq >= 0 THEN
       XA COMMIT 'cdtSelectSeq';
     ELSE
       XA ROLLBACK 'cdtSelectSeq';
     END IF;

     SELECT seq AS `PO_SEQ_NBR`;
     SET PO_SEQ_NBR = seq;
END
$$

DELIMITER ;

