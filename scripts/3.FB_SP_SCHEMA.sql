DROP PROCEDURE IF EXISTS `SP_GET_SEQUENCE`;
DELIMITER $$
CREATE DEFINER=`webman`@`localhost` PROCEDURE `SP_GET_SEQUENCE`(IN PI_SEQ_ID VARCHAR(50) , OUT PO_SEQ_NBR INT)
BEGIN
    DECLARE seq INT DEFAULT 0;
    /* ************************************************* */
    DECLARE `cdtSelectSeq` CONDITION FOR SQLSTATE '23000';
    DECLARE EXIT HANDLER FOR `cdtSelectSeq`
    BEGIN
               XA END 'cdtSelectSeq';
               XA PREPARE 'cdtSelectSeq';
               XA ROLLBACK 'cdtSelectSeq'; 
               SELECT -1 AS `PO_SEQ_NBR`;
     END;

     XA START 'cdtSelectSeq';
    /* ************************************************* */
      UPDATE `TB_SEQ_STORE` SET `SEQ_NBR` = LAST_INSERT_ID(`SEQ_NBR` + 1) WHERE `SEQ_ID` = PI_SEQ_ID;
      SET seq = LAST_INSERT_ID();
    /* ************************************************* */
     XA END 'cdtSelectSeq';

     XA PREPARE 'cdtSelectSeq';
         IF seq >= 0 THEN
               XA COMMIT 'cdtSelectSeq';
         ELSE
               XA ROLLBACK 'cdtSelectSeq';
         END IF;
    /* ************************************************* */
    SELECT seq AS `PO_SEQ_NBR`;
    SET PO_SEQ_NBR = seq;
END
$$

DELIMITER ;

