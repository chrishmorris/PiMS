CREATE SEQUENCE test_target
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 11
  CACHE 1;
ALTER TABLE test_target OWNER TO postgres;
GRANT ALL ON TABLE test_target TO postgres;
GRANT SELECT ON TABLE test_target TO pimsview;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE test_target TO pimsupdate;
GRANT ALL ON TABLE test_target TO pimsadmin;
CREATE SEQUENCE generic_target
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE generic_target OWNER TO postgres;
GRANT ALL ON TABLE generic_target TO postgres;
GRANT SELECT ON TABLE generic_target TO pimsview;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE generic_target TO pimsupdate;
GRANT ALL ON TABLE generic_target TO pimsadmin;
