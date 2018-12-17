-- ----------------------------------------------------------------------
-- Extra table: REVISIONNUMBER
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
alter table REVISIONNUMBER owner to pimsadmin;
grant ALL PRIVILEGES on table REVISIONNUMBER to postgres;
grant SELECT on table REVISIONNUMBER to pimsview;
grant SELECT, UPDATE, INSERT, DELETE on table REVISIONNUMBER to pimsupdate;
grant ALL PRIVILEGES on table REVISIONNUMBER to pimsadmin;

-- ----------------------------------------------------------------------
-- Extra table: TOMCATROLE
-- See http://tomcat.apache.org/tomcat-7.0-doc/realm-howto.html#Standard_Realm_Implementations
-- TODO can we merge this with ACCO_USERGROUP?
-- ----------------------------------------------------------------------
create table TOMCATROLE (
  NAME VARCHAR(80),
  ROLE VARCHAR(80)
);
alter table TOMCATROLE owner to pimsadmin;
grant ALL PRIVILEGES on table TOMCATROLE to postgres;
grant SELECT on table TOMCATROLE to pimsview;
grant SELECT, UPDATE, INSERT, DELETE on table TOMCATROLE to pimsupdate;
grant ALL PRIVILEGES on table TOMCATROLE to pimsadmin;
insert into TOMCATROLE values ('administrator', 'pims-administrator');
insert into TOMCATROLE values ('administrator', 'pims-user');

-- ----------------------------------------------------------------------
-- Extra sequence: TEST_TARGET & GENERIC_TARGET
-- ----------------------------------------------------------------------
create sequence TEST_TARGET
  increment 1
  minvalue 1
  maxvalue 9223372036854775807
  start 11
  cache 1;
alter table TEST_TARGET owner to pimsadmin;
grant ALL PRIVILEGES on table TEST_TARGET to postgres;
grant SELECT on table TEST_TARGET to pimsview;
grant SELECT, UPDATE, INSERT, DELETE on table TEST_TARGET to pimsupdate;
grant ALL PRIVILEGES on table TEST_TARGET to pimsadmin;

create sequence GENERIC_TARGET
  increment 1
  minvalue 1
  maxvalue 9223372036854775807
  start 1
  cache 1;
alter table GENERIC_TARGET OWNER TO pimsadmin;
grant ALL PRIVILEGES on table GENERIC_TARGET TO postgres;
grant SELECT on table GENERIC_TARGET TO pimsview;
grant SELECT, UPDATE, INSERT, DELETE on table GENERIC_TARGET TO pimsupdate;
grant ALL PRIVILEGES on table GENERIC_TARGET TO pimsadmin;

CREATE SEQUENCE hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE hibernate_sequence OWNER TO pimsadmin;
GRANT ALL PRIVILEGES ON TABLE hibernate_sequence TO postgres;
GRANT SELECT ON TABLE hibernate_sequence TO pimsview;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE hibernate_sequence TO pimsupdate;
GRANT ALL PRIVILEGES ON TABLE hibernate_sequence TO pimsadmin;