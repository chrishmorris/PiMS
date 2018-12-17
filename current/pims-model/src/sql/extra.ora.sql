-- ----------------------------------------------------------------------
-- final tweaks to Oracle database
-- Extra table: REVISIONNUMBER
-- Note that the Oracle implementation is not currently maintained
-- ----------------------------------------------------------------------
create table REVISIONNUMBER (
  -- primary key
  REVISION INTEGER not null,
  -- attributes
  NAME VARCHAR(254) not null,
  RELEASE VARCHAR(254) not null,
  TAG VARCHAR(254) not null,
  AUTHOR VARCHAR(254) not null,
  DATE_ VARCHAR(254) not null,
  primary key (REVISION)
);
grant SELECT on REVISIONNUMBER to pimsview;
grant SELECT, UPDATE, INSERT, DELETE on REVISIONNUMBER to pimsupdate;
-- could grantALL PRIVILEGES on REVISIONNUMBER to pimsadmin;

create table TOMCATROLE (
  NAME VARCHAR(80) not null,
  ROLE VARCHAR(80) not null
);
grant SELECT on TOMCATROLE to pimsview;
grant SELECT, UPDATE, INSERT, DELETE on TOMCATROLE to pimsupdate;
-- could grantALL PRIVILEGES on TOMCATROLE to pimsadmin;
insert into TOMCATROLE values ('administrator', 'pims-administrator');
insert into TOMCATROLE values ('administrator', 'pims-user');


-- ----------------------------------------------------------------------
-- Extra sequence: TEST_TARGET & GENERIC_TARGET
-- ----------------------------------------------------------------------
create sequence TEST_TARGET 
  start with 11 increment by 1
  minvalue 1
  maxvalue 9223372036854775807
  nocache;
grant SELECT on  TEST_TARGET to pimsview;
grant SELECT on TEST_TARGET to pimsupdate;
-- could grant ALL PRIVILEGES on  TEST_TARGET to pimsadmin;

create sequence GENERIC_TARGET
  start with 1 increment by 1
  minvalue 1
  maxvalue 9223372036854775807
  nocache;
grant SELECT on  GENERIC_TARGET TO pimsview;
grant SELECT on GENERIC_TARGET TO pimsupdate;
-- could grant ALL PRIVILEGES on  GENERIC_TARGET TO pimsadmin;

CREATE SEQUENCE hibernate_sequence
  start with 11 increment by 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  nocache;
GRANT SELECT ON hibernate_sequence TO pimsview;
GRANT SELECT ON  hibernate_sequence TO pimsupdate;
-- could GRANT ALL PRIVILEGES ON  hibernate_sequence TO pimsadmin;