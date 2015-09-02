SET NAMES utf8;

CREATE TABLE TB_SEQ_STORE (
         SEQ_ID    CHAR(50)     NOT NULL PRIMARY KEY COMMENT '參數名稱',
         SEQ_NBR   INT UNSIGNED NOT NULL             COMMENT '序號'
       ) ENGINE InnoDB,
         COMMENT '流水號使用記錄檔';

CREATE TABLE TB_LOOKUP (
         LOOKUP_TYPE   VARCHAR(20) NOT NULL COMMENT '參數類別',
         LOOKUP_CDE    VARCHAR(10) NOT NULL COMMENT '參數代碼',
         LOOKUP_NME    VARCHAR(50)                COMMENT '參數名稱',
         DISPLAY_FLAG  BOOLEAN                    COMMENT '是否顯示',
         DISPLAY_ORDER TINYINT UNSIGNED DEFAULT 0 COMMENT '顯示順序',
         DSCR          VARCHAR(100)               COMMENT '參數說明'
       ) ENGINE InnoDB,
         COMMENT '系統參數定義檔';
CREATE INDEX IDX_LOOKUP ON TB_LOOKUP (LOOKUP_TYPE,LOOKUP_CDE);

CREATE TABLE TB_USER_PROF (
         USER_ID      VARCHAR(10) NOT NULL PRIMARY KEY COMMENT '帳號',
         USER_NME     VARCHAR(20)          COMMENT '姓名',
         USER_PWD     VARCHAR(50) NOT NULL COMMENT '密碼',
         USER_CLASS   VARCHAR(1)  NOT NULL COMMENT '帳號等級',
         STATUS_FLAG  VARCHAR(1)  NOT NULL COMMENT '狀況'
       ) ENGINE InnoDB,
         COMMENT '帳號資料檔';

SELECT * FROM  U
		WHERE U. = ''
		<isNotNull property="userId" prepend="AND"> U. = #userId#</isNotNull>
		<isNotNull property="userPwd" prepend="AND"> U. = #userPwd#</isNotNull>
		<isNotNull property="userClass" prepend="AND"> U. IN ($userClass$)</isNotNull>
		
CREATE TABLE TB_CUST_PROF (
         CUST_ID       INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '客戶編號（流水號）',
         CUST_NME      VARCHAR(100) NOT NULL COMMENT '客戶名稱',
         BIZ_NO        VARCHAR(20)           COMMENT '統一編號',
         DELIVER_ADDR  VARCHAR(100)          COMMENT '送貨地址',
         TEL           VARCHAR(50)           COMMENT '聯絡電話（可含市話及手機）',
         MEMO          VARCHAR(100)          COMMENT '備註（休息時間...等）',
         STATUS_FLAG   VARCHAR(1)            COMMENT '狀態旗標',
         USTAMP        VARCHAR(30) NOT NULL  COMMENT '帳號標記',
         TSTAMP        DATETIME    NOT NULL  COMMENT '時間標記'
       ) ENGINE InnoDB,
         COMMENT '客戶基本資料檔';
CREATE INDEX IDX_CUST_NME ON TB_CUST_PROF (CUST_NME);

CREATE TABLE TB_PROD_PROF (
         PROD_ID       INT UNSIGNED  NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '產品編號（流水號）',
         PROD_NME      VARCHAR(100)  NOT NULL             COMMENT '產品名稱（可含品名及規格）',
         UNIT          VARCHAR(20)                        COMMENT '單位',
         PRICE         DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '單價',
         COST          DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '成本（取最後一次進價）',
         SAVE_QTY      INT           NOT NULL DEFAULT 0   COMMENT '安全存量',
         STATUS_FLAG   VARCHAR(1)                         COMMENT '狀態旗標',
         USTAMP        VARCHAR(30)   NOT NULL             COMMENT '帳號標記',
         TSTAMP        DATETIME      NOT NULL             COMMENT '時間標記'
       ) ENGINE InnoDB,
         COMMENT '產品基本資料檔';
CREATE INDEX IDX_PROD_NME ON TB_PROD_PROF (PROD_NME);

CREATE TABLE TB_FACT_PROF (
         FACT_ID       INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '廠商編號（流水號）',
         FACT_NME      VARCHAR(100) NOT NULL COMMENT '廠商名稱',
         BIZ_NO        VARCHAR(20)           COMMENT '統一編號',
         CONTACT       VARCHAR(50)           COMMENT '聯絡人',
         ADDR          VARCHAR(100)          COMMENT '地址',
         TEL           VARCHAR(50)           COMMENT '聯絡電話（可含市話及手機）',
         MEMO          VARCHAR(100)          COMMENT '備註',
         STATUS_FLAG   VARCHAR(1)            COMMENT '狀態旗標',
         USTAMP        VARCHAR(30) NOT NULL  COMMENT '帳號標記',
         TSTAMP        DATETIME    NOT NULL  COMMENT '時間標記'
       ) ENGINE InnoDB,
         COMMENT '廠商基本資料檔';
CREATE INDEX IDX_FACT_NME ON TB_FACT_PROF (FACT_NME);

CREATE TABLE TB_STOCK_PROF (
         STOCK_ID   TINYINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '倉庫編號（流水號）',
         STOCK_NME  VARCHAR(20)      NOT NULL        COMMENT '倉庫名稱',
         ADDR       VARCHAR(100)                     COMMENT '倉庫地址'
       ) ENGINE InnoDB,
         COMMENT '倉庫基本資料檔';

CREATE TABLE TB_PROD_STOCK_QTY (
         STOCK_ID   TINYINT       UNSIGNED NOT NULL             COMMENT '倉庫編號（流水號）',
         PROD_ID    INT           UNSIGNED NOT NULL             COMMENT '產品編號（流水號）',
         QTY        DECIMAL(10,2)          NOT NULL DEFAULT 0   COMMENT '目前存量',
         PRIMARY KEY (STOCK_ID,PROD_ID)
       ) ENGINE InnoDB,
         COMMENT '產品庫存量記錄檔';

CREATE TABLE TB_CUST_PROD_HIS (
         ID         INT UNSIGNED  NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '歴史編號（流水號）',
         CUST_ID    INT UNSIGNED  NOT NULL                            COMMENT '客戶編號',
         PROD_ID    INT UNSIGNED  NOT NULL                            COMMENT '產品編號',
         PRICE      DECIMAL(10,2) NOT NULL DEFAULT 0                  COMMENT '單價',
         OFFER_ID   INT UNSIGNED  NOT NULL                            COMMENT '此單價所依據的出貨單號'
       ) ENGINE InnoDB,
         COMMENT '客戶產品價格歴史記錄檔';
CREATE INDEX IDX_CUST_PROD_HIS ON TB_CUST_PROD_HIS (CUST_ID,PROD_ID);

CREATE TABLE TB_FACT_PROD_HIS (
         ID            INT UNSIGNED  NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '歴史編號（流水號）',
         FACT_ID       INT UNSIGNED  NOT NULL                            COMMENT '廠商編號',
         PROD_ID       INT UNSIGNED  NOT NULL                            COMMENT '產品編號',
         PRICE         DECIMAL(10,2) NOT NULL DEFAULT 0                  COMMENT '單價',
         PURCHASE_ID   INT UNSIGNED  NOT NULL                            COMMENT '此單價所依據的進貨單號'
       ) ENGINE InnoDB,
         COMMENT '廠商產品進價歴史記錄檔';
CREATE INDEX IDX_FACT_PROD_HIS ON TB_FACT_PROD_HIS (FACT_ID,PROD_ID);

CREATE TABLE TB_OFFER_MASTER (
         ID           INT UNSIGNED  NOT NULL PRIMARY KEY COMMENT '出貨單號(yyMMdd0000)',
         OFFER_DATE   DATE          NOT NULL             COMMENT '單據日期',
         CUST_ID      INT UNSIGNED  NOT NULL             COMMENT '客戶編號',
         INVOICE_NBR  VARCHAR(20)                        COMMENT '發票號碼',
         STOCK_ID     TINYINT                            COMMENT '倉庫編號',
         AMT          DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '合計（明細金額sum）',
         DISCOUNT     DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '折讓',
         TOTAL        DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '總計（合計-折讓）',
         COST         DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '進價成本',
         RECEIVE_AMT  DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '己收款',
         MEMO         VARCHAR(100)                       COMMENT '貨單備註',
         BACK         CHAR(1)                            COMMENT '銷貨退回旗標（NULL:銷貨, Y:銷貨退回）',
         STATUS       CHAR(1)       NOT NULL DEFAULT 'N' COMMENT '狀態旗標（N:正常, D:刪除; 預設:N）'
       ) ENGINE InnoDB,
         COMMENT '出貨單主資料檔';
CREATE INDEX IDX_OFFER_MASTER_CUST ON TB_OFFER_MASTER (CUST_ID);

CREATE TABLE TB_OFFER_DETAIL (
         ID           INT           NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '流水號',
         MASTER_ID    INT           NOT NULL                  COMMENT '出貨單號',
         PROD_ID      INT           NOT NULL                  COMMENT '產品編號',
         QTY          DECIMAL(10,2) NOT NULL DEFAULT 0        COMMENT '數量',
         AMT          DECIMAL(10,2) NOT NULL DEFAULT 0        COMMENT '金額（數量x單價）'
       ) ENGINE InnoDB,
         COMMENT '出貨單明細資料檔';
CREATE INDEX IDX_OFFER_MASTER_DETAIL ON TB_OFFER_DETAIL (MASTER_ID);

CREATE TABLE TB_PURCHASE_MASTER (
         ID              INT UNSIGNED  NOT NULL PRIMARY KEY COMMENT '進貨單號(yyMMdd0000)',
         PURCHASE_DATE   DATE          NOT NULL             COMMENT '單據日期',
         FACT_ID         INT UNSIGNED  NOT NULL             COMMENT '廠商編號',
         INVOICE_NBR     VARCHAR(20)                        COMMENT '發票號碼',
         STOCK_ID        TINYINT                            COMMENT '倉庫編號',
         AMT             DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '合計（明細金額sum）',
         DISCOUNT        DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '折讓',
         TOTAL           DECIMAL(10,2) NOT NULL DEFAULT 0   COMMENT '總計（合計-折讓）',
         MEMO            VARCHAR(100)                       COMMENT '貨單備註',
         BACK            CHAR(1)                            COMMENT '進貨退出旗標',
         STATUS          CHAR(1)       NOT NULL DEFAULT 'N' COMMENT '狀態旗標（N:正常, D:刪除; 預設:N）'
       ) ENGINE InnoDB,
         COMMENT '進貨單主資料檔';
CREATE INDEX IDX_PURCHASE_MASTER_FACT ON TB_PURCHASE_MASTER (FACT_ID);

CREATE TABLE TB_PURCHASE_DETAIL (
         ID           INT           NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '流水號',
         MASTER_ID    INT           NOT NULL                  COMMENT '進貨單號',
         PROD_ID      INT           NOT NULL                  COMMENT '產品編號',
         QTY          DECIMAL(10,2) NOT NULL DEFAULT 0        COMMENT '數量',
         AMT          DECIMAL(10,2) NOT NULL DEFAULT 0        COMMENT '金額（數量x單價）'
       ) ENGINE InnoDB,
         COMMENT '進貨單明細資料檔';
CREATE INDEX IDX_PURCHASE_MASTER_DETAIL ON TB_PURCHASE_DETAIL (MASTER_ID);

INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-1','桶',TRUE,1,'單位-桶');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-2','箱',TRUE,2,'單位-箱');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-3','袋',TRUE,3,'單位-袋');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-4','個',TRUE,4,'單位-個');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-5','包',TRUE,5,'單位-包');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-6','罐',TRUE,6,'單位-罐');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-7','件',TRUE,7,'單位-件');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-8','斤',TRUE,8,'單位-斤');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-9','捲',TRUE,9,'單位-捲');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-10','盒',TRUE,10,'單位-盒');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-11','條',TRUE,11,'單位-條');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-12','排',TRUE,12,'單位-排');
INSERT INTO TB_LOOKUP VALUES ('UNIT','UT-13','粒',TRUE,13,'單位-粒');


INSERT INTO TB_LOOKUP VALUES ('OFFER_MEMO','','',TRUE,0,'出貨單附註');


INSERT INTO TB_LOOKUP VALUES ('CATG','CAT-1','涮涮鍋',TRUE,1,'類別-涮涮鍋');
INSERT INTO TB_LOOKUP VALUES ('CATG','CAT-2','幼稚園',TRUE,2,'類別-幼稚園');
INSERT INTO TB_LOOKUP VALUES ('CATG','CAT-3','未分類',TRUE,3,'類別-未分類');


INSERT INTO TB_SEQ_STORE VALUES ('SEQ_TB_OFFER_MASTER_ID', 0);
UPDATE TB_SEQ_STORE SET SEQ_NBR=LAST_INSERT_ID(SEQ_NBR+1) WHERE SEQ_ID='SEQ_TB_OFFER_MASTER_ID';

INSERT INTO TB_SEQ_STORE VALUES ('SEQ_TB_PURCHASE_MASTER_ID', 0);
UPDATE TB_SEQ_STORE SET SEQ_NBR=LAST_INSERT_ID(SEQ_NBR+1) WHERE SEQ_ID='SEQ_TB_PURCHASE_MASTER_ID';
