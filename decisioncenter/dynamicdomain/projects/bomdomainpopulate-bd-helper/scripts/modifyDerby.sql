-- Licensed Materials - Property of IBM
-- 5725-B69 5655-Y17 5655-Y31 5724-X98 5724-Y15 5655-V82
-- Copyright IBM Corp. 1987, 2025. All Rights Reserved.
--
-- Note to U.S. Government Users Restricted Rights: 
-- Use, duplication or disclosure restricted by GSA ADP Schedule 
-- Contract with IBM Corp.

-- Update Currencies for the domain
INSERT INTO BOMDOMAINSAMPLE.CURRENCY VALUES ('Pound','Pound', 'return "Pound";');
INSERT INTO BOMDOMAINSAMPLE.CURRENCY VALUES ('JPYen','JP Yen', 'return "JP Yen";');
INSERT INTO BOMDOMAINSAMPLE.CURRENCY VALUES ('PRCRMB','PRC RMB', 'return "PRC RMB";');
DELETE FROM BOMDOMAINSAMPLE.CURRENCY WHERE ITEM_NAME LIKE 'AUSDOLLAR';

INSERT INTO BOMDOMAINSAMPLE.STATUS VALUES ('BLOCKED','Blocked', 'return "Blocked";');

INSERT INTO BOMDOMAINSAMPLE.MESSAGES VALUES ('CustomerPreferences','Customer Preferences', 'return "CustPrefs";');

INSERT INTO BOMDOMAINSAMPLE.STOCK VALUES(4,'CompanyY.PA','CompanyY');
INSERT INTO BOMDOMAINSAMPLE.SECTOR VALUES ('BlackListed Companies',5);
INSERT INTO BOMDOMAINSAMPLE.STOCK VALUES (5,'CompanyX.PA','CompanyX');

COMMIT;
