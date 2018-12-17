-- UserDB Login Roles
-- Change the passwords!
-- Jon Diprose, 29/01/2009

-- Role with write access to tables, for User webapp
-- NB Tables are probably owned by postgres
CREATE ROLE "userdb-admin" LOGIN
  PASSWORD 'pwd-userdb-admin'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

-- Role with limited read access to tables, for tomcat jdbc realm
CREATE ROLE "userdb-tomcat" LOGIN
  PASSWORD 'pwd-userdb-tomcat'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

-- Role with read access to tables, for webapps that use UserDB
CREATE ROLE "userdb-user" LOGIN
  PASSWORD 'pwd-userdb-user'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
