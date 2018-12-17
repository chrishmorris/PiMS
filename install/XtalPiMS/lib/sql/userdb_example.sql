--
-- PostgreSQL database dump
--

-- Started on 2009-02-03 11:47:27 GMT Standard Time

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1547 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- TOC entry 260 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: 
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- TOC entry 247 (class 1247 OID 8609144)
-- Dependencies: 1194
-- Name: breakpoint; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE breakpoint AS (
	func oid,
	linenumber integer,
	targetname text
);


ALTER TYPE public.breakpoint OWNER TO postgres;

--
-- TOC entry 248 (class 1247 OID 8609146)
-- Dependencies: 1195
-- Name: frame; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE frame AS (
	"level" integer,
	targetname text,
	func oid,
	linenumber integer,
	args text
);


ALTER TYPE public.frame OWNER TO postgres;

--
-- TOC entry 249 (class 1247 OID 8609148)
-- Dependencies: 1196
-- Name: proxyinfo; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE proxyinfo AS (
	serverversionstr text,
	serverversionnum integer,
	proxyapiver integer,
	serverprocessid integer
);


ALTER TYPE public.proxyinfo OWNER TO postgres;

--
-- TOC entry 252 (class 1247 OID 8609156)
-- Dependencies: 1198
-- Name: targetinfo; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE targetinfo AS (
	target oid,
	"schema" oid,
	nargs integer,
	argtypes oidvector,
	targetname name,
	argmodes "char"[],
	argnames text[],
	targetlang oid,
	fqname text,
	returnsset boolean,
	returntype oid
);


ALTER TYPE public.targetinfo OWNER TO postgres;

--
-- TOC entry 258 (class 1247 OID 8609172)
-- Dependencies: 1203
-- Name: var; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE var AS (
	name text,
	varclass character(1),
	linenumber integer,
	isunique boolean,
	isconst boolean,
	isnotnull boolean,
	dtype oid,
	value text
);


ALTER TYPE public.var OWNER TO postgres;

--
-- TOC entry 1204 (class 1259 OID 8611364)
-- Dependencies: 5
-- Name: id_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE id_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.id_sequence OWNER TO postgres;

--
-- TOC entry 1549 (class 0 OID 0)
-- Dependencies: 1204
-- Name: id_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('id_sequence', 2377, true);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1197 (class 1259 OID 8609149)
-- Dependencies: 1528 5
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE roles (
    roleid bigint NOT NULL,
    "role" character varying(255),
    description character varying(255),
    defaultrole boolean DEFAULT false NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 1199 (class 1259 OID 8609157)
-- Dependencies: 5
-- Name: userrole; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE userrole (
    userid bigint NOT NULL,
    roleid bigint NOT NULL
);


ALTER TABLE public.userrole OWNER TO postgres;

--
-- TOC entry 1200 (class 1259 OID 8609159)
-- Dependencies: 1529 5
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    userid bigint NOT NULL,
    username character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    firstname character varying(255),
    surname character varying(255),
    email character varying(255),
    phone character varying(255),
    fax character varying(255),
    registrationdate timestamp without time zone,
    notes text,
    enabled boolean DEFAULT true NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 1201 (class 1259 OID 8609165)
-- Dependencies: 1270 5
-- Name: userroleview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW userroleview AS
    SELECT users.username, roles."role" FROM userrole, users, roles WHERE ((((userrole.roleid = roles.roleid) AND (userrole.userid = users.userid)) AND (users.registrationdate IS NOT NULL)) AND (users.enabled IS TRUE));


ALTER TABLE public.userroleview OWNER TO postgres;

--
-- TOC entry 1202 (class 1259 OID 8609168)
-- Dependencies: 1271 5
-- Name: userview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW userview AS
    SELECT users.username, users."password" FROM users WHERE ((users.registrationdate IS NOT NULL) AND (users.enabled IS TRUE));


ALTER TABLE public.userview OWNER TO postgres;

--
-- TOC entry 1542 (class 0 OID 8609149)
-- Dependencies: 1197
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (4, 'manager', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (12, 'pims-user', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (13, 'pims-administrator', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (14, 'pims-view', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (6, 'localuser', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (7, 'user', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (2, 'msadmin', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (5, 'xtaladmin', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (8, 'msuser', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (10, 'ehtpxuser', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (11, 'grouphead', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (1, 'siteadmin', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (15, 'ehtpxadmin', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (17, 'sab', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (18, 'ehtpxtest', NULL, false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (3, 'admin', 'Administrator Account', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (2247, 'protocol', 'Access to OPPF protocol library', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (16, 'student', '', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (2257, 'optic', 'Access to OPTIC', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (2263, 'ehtpx-user', 'An e-HTPX User account for the new e-HTPX website', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (2264, 'ehtpx-administrator', '', false);
INSERT INTO roles (roleid, "role", description, defaultrole) VALUES (9, 'xtaluser', 'Use of OPPF crystallization facility', false);


--
-- TOC entry 1543 (class 0 OID 8609157)
-- Dependencies: 1199
-- Data for Name: userrole; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO userrole (userid, roleid) VALUES (2175, 6);
INSERT INTO userrole (userid, roleid) VALUES (2, 4);
INSERT INTO userrole (userid, roleid) VALUES (2, 6);
INSERT INTO userrole (userid, roleid) VALUES (2, 8);
INSERT INTO userrole (userid, roleid) VALUES (2, 10);
INSERT INTO userrole (userid, roleid) VALUES (2, 12);
INSERT INTO userrole (userid, roleid) VALUES (2, 14);
INSERT INTO userrole (userid, roleid) VALUES (52, 6);
INSERT INTO userrole (userid, roleid) VALUES (52, 8);
INSERT INTO userrole (userid, roleid) VALUES (52, 10);
INSERT INTO userrole (userid, roleid) VALUES (52, 12);
INSERT INTO userrole (userid, roleid) VALUES (52, 14);
INSERT INTO userrole (userid, roleid) VALUES (6, 6);
INSERT INTO userrole (userid, roleid) VALUES (6, 8);
INSERT INTO userrole (userid, roleid) VALUES (6, 10);
INSERT INTO userrole (userid, roleid) VALUES (6, 12);
INSERT INTO userrole (userid, roleid) VALUES (6, 14);
INSERT INTO userrole (userid, roleid) VALUES (7, 1);
INSERT INTO userrole (userid, roleid) VALUES (7, 2);
INSERT INTO userrole (userid, roleid) VALUES (7, 4);
INSERT INTO userrole (userid, roleid) VALUES (7, 5);
INSERT INTO userrole (userid, roleid) VALUES (7, 6);
INSERT INTO userrole (userid, roleid) VALUES (7, 8);
INSERT INTO userrole (userid, roleid) VALUES (7, 10);
INSERT INTO userrole (userid, roleid) VALUES (7, 11);
INSERT INTO userrole (userid, roleid) VALUES (7, 12);
INSERT INTO userrole (userid, roleid) VALUES (7, 13);
INSERT INTO userrole (userid, roleid) VALUES (7, 14);
INSERT INTO userrole (userid, roleid) VALUES (8, 6);
INSERT INTO userrole (userid, roleid) VALUES (8, 8);
INSERT INTO userrole (userid, roleid) VALUES (8, 10);
INSERT INTO userrole (userid, roleid) VALUES (8, 12);
INSERT INTO userrole (userid, roleid) VALUES (8, 14);
INSERT INTO userrole (userid, roleid) VALUES (9, 6);
INSERT INTO userrole (userid, roleid) VALUES (9, 8);
INSERT INTO userrole (userid, roleid) VALUES (9, 10);
INSERT INTO userrole (userid, roleid) VALUES (9, 12);
INSERT INTO userrole (userid, roleid) VALUES (9, 14);
INSERT INTO userrole (userid, roleid) VALUES (10, 6);
INSERT INTO userrole (userid, roleid) VALUES (10, 8);
INSERT INTO userrole (userid, roleid) VALUES (10, 10);
INSERT INTO userrole (userid, roleid) VALUES (10, 12);
INSERT INTO userrole (userid, roleid) VALUES (10, 14);
INSERT INTO userrole (userid, roleid) VALUES (11, 6);
INSERT INTO userrole (userid, roleid) VALUES (11, 8);
INSERT INTO userrole (userid, roleid) VALUES (11, 10);
INSERT INTO userrole (userid, roleid) VALUES (11, 12);
INSERT INTO userrole (userid, roleid) VALUES (11, 14);
INSERT INTO userrole (userid, roleid) VALUES (12, 6);
INSERT INTO userrole (userid, roleid) VALUES (12, 8);
INSERT INTO userrole (userid, roleid) VALUES (12, 10);
INSERT INTO userrole (userid, roleid) VALUES (12, 12);
INSERT INTO userrole (userid, roleid) VALUES (12, 14);
INSERT INTO userrole (userid, roleid) VALUES (14, 5);
INSERT INTO userrole (userid, roleid) VALUES (14, 6);
INSERT INTO userrole (userid, roleid) VALUES (14, 8);
INSERT INTO userrole (userid, roleid) VALUES (14, 10);
INSERT INTO userrole (userid, roleid) VALUES (14, 11);
INSERT INTO userrole (userid, roleid) VALUES (14, 12);
INSERT INTO userrole (userid, roleid) VALUES (14, 14);
INSERT INTO userrole (userid, roleid) VALUES (18, 6);
INSERT INTO userrole (userid, roleid) VALUES (18, 8);
INSERT INTO userrole (userid, roleid) VALUES (18, 10);
INSERT INTO userrole (userid, roleid) VALUES (18, 12);
INSERT INTO userrole (userid, roleid) VALUES (18, 14);
INSERT INTO userrole (userid, roleid) VALUES (19, 6);
INSERT INTO userrole (userid, roleid) VALUES (19, 8);
INSERT INTO userrole (userid, roleid) VALUES (19, 10);
INSERT INTO userrole (userid, roleid) VALUES (19, 12);
INSERT INTO userrole (userid, roleid) VALUES (19, 14);
INSERT INTO userrole (userid, roleid) VALUES (22, 6);
INSERT INTO userrole (userid, roleid) VALUES (22, 8);
INSERT INTO userrole (userid, roleid) VALUES (22, 10);
INSERT INTO userrole (userid, roleid) VALUES (22, 12);
INSERT INTO userrole (userid, roleid) VALUES (22, 14);
INSERT INTO userrole (userid, roleid) VALUES (144, 6);
INSERT INTO userrole (userid, roleid) VALUES (144, 8);
INSERT INTO userrole (userid, roleid) VALUES (144, 10);
INSERT INTO userrole (userid, roleid) VALUES (144, 12);
INSERT INTO userrole (userid, roleid) VALUES (144, 14);
INSERT INTO userrole (userid, roleid) VALUES (28, 6);
INSERT INTO userrole (userid, roleid) VALUES (28, 8);
INSERT INTO userrole (userid, roleid) VALUES (28, 10);
INSERT INTO userrole (userid, roleid) VALUES (28, 11);
INSERT INTO userrole (userid, roleid) VALUES (28, 12);
INSERT INTO userrole (userid, roleid) VALUES (28, 14);
INSERT INTO userrole (userid, roleid) VALUES (29, 6);
INSERT INTO userrole (userid, roleid) VALUES (29, 8);
INSERT INTO userrole (userid, roleid) VALUES (29, 10);
INSERT INTO userrole (userid, roleid) VALUES (29, 12);
INSERT INTO userrole (userid, roleid) VALUES (29, 14);
INSERT INTO userrole (userid, roleid) VALUES (31, 6);
INSERT INTO userrole (userid, roleid) VALUES (31, 8);
INSERT INTO userrole (userid, roleid) VALUES (31, 10);
INSERT INTO userrole (userid, roleid) VALUES (31, 11);
INSERT INTO userrole (userid, roleid) VALUES (31, 12);
INSERT INTO userrole (userid, roleid) VALUES (31, 14);
INSERT INTO userrole (userid, roleid) VALUES (32, 6);
INSERT INTO userrole (userid, roleid) VALUES (32, 8);
INSERT INTO userrole (userid, roleid) VALUES (32, 10);
INSERT INTO userrole (userid, roleid) VALUES (32, 12);
INSERT INTO userrole (userid, roleid) VALUES (32, 14);
INSERT INTO userrole (userid, roleid) VALUES (33, 6);
INSERT INTO userrole (userid, roleid) VALUES (33, 8);
INSERT INTO userrole (userid, roleid) VALUES (33, 10);
INSERT INTO userrole (userid, roleid) VALUES (33, 12);
INSERT INTO userrole (userid, roleid) VALUES (33, 14);
INSERT INTO userrole (userid, roleid) VALUES (35, 6);
INSERT INTO userrole (userid, roleid) VALUES (35, 8);
INSERT INTO userrole (userid, roleid) VALUES (35, 10);
INSERT INTO userrole (userid, roleid) VALUES (35, 12);
INSERT INTO userrole (userid, roleid) VALUES (35, 14);
INSERT INTO userrole (userid, roleid) VALUES (36, 6);
INSERT INTO userrole (userid, roleid) VALUES (36, 8);
INSERT INTO userrole (userid, roleid) VALUES (36, 10);
INSERT INTO userrole (userid, roleid) VALUES (36, 12);
INSERT INTO userrole (userid, roleid) VALUES (36, 14);
INSERT INTO userrole (userid, roleid) VALUES (37, 6);
INSERT INTO userrole (userid, roleid) VALUES (37, 8);
INSERT INTO userrole (userid, roleid) VALUES (37, 10);
INSERT INTO userrole (userid, roleid) VALUES (37, 12);
INSERT INTO userrole (userid, roleid) VALUES (37, 14);
INSERT INTO userrole (userid, roleid) VALUES (38, 6);
INSERT INTO userrole (userid, roleid) VALUES (38, 8);
INSERT INTO userrole (userid, roleid) VALUES (38, 10);
INSERT INTO userrole (userid, roleid) VALUES (38, 12);
INSERT INTO userrole (userid, roleid) VALUES (38, 14);
INSERT INTO userrole (userid, roleid) VALUES (39, 6);
INSERT INTO userrole (userid, roleid) VALUES (39, 8);
INSERT INTO userrole (userid, roleid) VALUES (39, 10);
INSERT INTO userrole (userid, roleid) VALUES (39, 12);
INSERT INTO userrole (userid, roleid) VALUES (39, 14);
INSERT INTO userrole (userid, roleid) VALUES (40, 6);
INSERT INTO userrole (userid, roleid) VALUES (40, 8);
INSERT INTO userrole (userid, roleid) VALUES (40, 10);
INSERT INTO userrole (userid, roleid) VALUES (40, 12);
INSERT INTO userrole (userid, roleid) VALUES (40, 14);
INSERT INTO userrole (userid, roleid) VALUES (41, 6);
INSERT INTO userrole (userid, roleid) VALUES (41, 8);
INSERT INTO userrole (userid, roleid) VALUES (41, 10);
INSERT INTO userrole (userid, roleid) VALUES (41, 12);
INSERT INTO userrole (userid, roleid) VALUES (41, 14);
INSERT INTO userrole (userid, roleid) VALUES (43, 6);
INSERT INTO userrole (userid, roleid) VALUES (43, 8);
INSERT INTO userrole (userid, roleid) VALUES (43, 10);
INSERT INTO userrole (userid, roleid) VALUES (43, 12);
INSERT INTO userrole (userid, roleid) VALUES (43, 14);
INSERT INTO userrole (userid, roleid) VALUES (49, 6);
INSERT INTO userrole (userid, roleid) VALUES (49, 8);
INSERT INTO userrole (userid, roleid) VALUES (49, 10);
INSERT INTO userrole (userid, roleid) VALUES (49, 12);
INSERT INTO userrole (userid, roleid) VALUES (49, 14);
INSERT INTO userrole (userid, roleid) VALUES (56, 6);
INSERT INTO userrole (userid, roleid) VALUES (56, 8);
INSERT INTO userrole (userid, roleid) VALUES (56, 10);
INSERT INTO userrole (userid, roleid) VALUES (56, 12);
INSERT INTO userrole (userid, roleid) VALUES (56, 14);
INSERT INTO userrole (userid, roleid) VALUES (59, 6);
INSERT INTO userrole (userid, roleid) VALUES (59, 8);
INSERT INTO userrole (userid, roleid) VALUES (59, 10);
INSERT INTO userrole (userid, roleid) VALUES (59, 12);
INSERT INTO userrole (userid, roleid) VALUES (59, 14);
INSERT INTO userrole (userid, roleid) VALUES (62, 6);
INSERT INTO userrole (userid, roleid) VALUES (62, 8);
INSERT INTO userrole (userid, roleid) VALUES (62, 10);
INSERT INTO userrole (userid, roleid) VALUES (62, 12);
INSERT INTO userrole (userid, roleid) VALUES (62, 14);
INSERT INTO userrole (userid, roleid) VALUES (71, 6);
INSERT INTO userrole (userid, roleid) VALUES (71, 8);
INSERT INTO userrole (userid, roleid) VALUES (71, 10);
INSERT INTO userrole (userid, roleid) VALUES (71, 12);
INSERT INTO userrole (userid, roleid) VALUES (71, 14);
INSERT INTO userrole (userid, roleid) VALUES (77, 6);
INSERT INTO userrole (userid, roleid) VALUES (77, 8);
INSERT INTO userrole (userid, roleid) VALUES (77, 10);
INSERT INTO userrole (userid, roleid) VALUES (77, 12);
INSERT INTO userrole (userid, roleid) VALUES (77, 14);
INSERT INTO userrole (userid, roleid) VALUES (78, 6);
INSERT INTO userrole (userid, roleid) VALUES (78, 8);
INSERT INTO userrole (userid, roleid) VALUES (78, 10);
INSERT INTO userrole (userid, roleid) VALUES (78, 12);
INSERT INTO userrole (userid, roleid) VALUES (78, 14);
INSERT INTO userrole (userid, roleid) VALUES (79, 6);
INSERT INTO userrole (userid, roleid) VALUES (79, 8);
INSERT INTO userrole (userid, roleid) VALUES (79, 10);
INSERT INTO userrole (userid, roleid) VALUES (79, 12);
INSERT INTO userrole (userid, roleid) VALUES (79, 14);
INSERT INTO userrole (userid, roleid) VALUES (83, 6);
INSERT INTO userrole (userid, roleid) VALUES (83, 8);
INSERT INTO userrole (userid, roleid) VALUES (83, 10);
INSERT INTO userrole (userid, roleid) VALUES (83, 12);
INSERT INTO userrole (userid, roleid) VALUES (83, 14);
INSERT INTO userrole (userid, roleid) VALUES (90, 6);
INSERT INTO userrole (userid, roleid) VALUES (90, 8);
INSERT INTO userrole (userid, roleid) VALUES (90, 10);
INSERT INTO userrole (userid, roleid) VALUES (90, 12);
INSERT INTO userrole (userid, roleid) VALUES (90, 14);
INSERT INTO userrole (userid, roleid) VALUES (94, 6);
INSERT INTO userrole (userid, roleid) VALUES (94, 8);
INSERT INTO userrole (userid, roleid) VALUES (94, 10);
INSERT INTO userrole (userid, roleid) VALUES (94, 12);
INSERT INTO userrole (userid, roleid) VALUES (94, 14);
INSERT INTO userrole (userid, roleid) VALUES (95, 6);
INSERT INTO userrole (userid, roleid) VALUES (95, 8);
INSERT INTO userrole (userid, roleid) VALUES (95, 10);
INSERT INTO userrole (userid, roleid) VALUES (95, 12);
INSERT INTO userrole (userid, roleid) VALUES (95, 14);
INSERT INTO userrole (userid, roleid) VALUES (98, 6);
INSERT INTO userrole (userid, roleid) VALUES (98, 8);
INSERT INTO userrole (userid, roleid) VALUES (98, 10);
INSERT INTO userrole (userid, roleid) VALUES (98, 12);
INSERT INTO userrole (userid, roleid) VALUES (98, 14);
INSERT INTO userrole (userid, roleid) VALUES (100, 6);
INSERT INTO userrole (userid, roleid) VALUES (100, 8);
INSERT INTO userrole (userid, roleid) VALUES (100, 10);
INSERT INTO userrole (userid, roleid) VALUES (100, 12);
INSERT INTO userrole (userid, roleid) VALUES (100, 14);
INSERT INTO userrole (userid, roleid) VALUES (103, 6);
INSERT INTO userrole (userid, roleid) VALUES (103, 8);
INSERT INTO userrole (userid, roleid) VALUES (103, 10);
INSERT INTO userrole (userid, roleid) VALUES (103, 12);
INSERT INTO userrole (userid, roleid) VALUES (103, 14);
INSERT INTO userrole (userid, roleid) VALUES (104, 6);
INSERT INTO userrole (userid, roleid) VALUES (104, 8);
INSERT INTO userrole (userid, roleid) VALUES (104, 10);
INSERT INTO userrole (userid, roleid) VALUES (104, 12);
INSERT INTO userrole (userid, roleid) VALUES (104, 14);
INSERT INTO userrole (userid, roleid) VALUES (106, 6);
INSERT INTO userrole (userid, roleid) VALUES (106, 8);
INSERT INTO userrole (userid, roleid) VALUES (106, 10);
INSERT INTO userrole (userid, roleid) VALUES (106, 12);
INSERT INTO userrole (userid, roleid) VALUES (106, 14);
INSERT INTO userrole (userid, roleid) VALUES (109, 6);
INSERT INTO userrole (userid, roleid) VALUES (109, 8);
INSERT INTO userrole (userid, roleid) VALUES (109, 10);
INSERT INTO userrole (userid, roleid) VALUES (109, 12);
INSERT INTO userrole (userid, roleid) VALUES (109, 14);
INSERT INTO userrole (userid, roleid) VALUES (114, 6);
INSERT INTO userrole (userid, roleid) VALUES (114, 8);
INSERT INTO userrole (userid, roleid) VALUES (114, 10);
INSERT INTO userrole (userid, roleid) VALUES (114, 12);
INSERT INTO userrole (userid, roleid) VALUES (114, 14);
INSERT INTO userrole (userid, roleid) VALUES (116, 6);
INSERT INTO userrole (userid, roleid) VALUES (116, 8);
INSERT INTO userrole (userid, roleid) VALUES (116, 10);
INSERT INTO userrole (userid, roleid) VALUES (116, 12);
INSERT INTO userrole (userid, roleid) VALUES (116, 14);
INSERT INTO userrole (userid, roleid) VALUES (120, 6);
INSERT INTO userrole (userid, roleid) VALUES (120, 8);
INSERT INTO userrole (userid, roleid) VALUES (120, 10);
INSERT INTO userrole (userid, roleid) VALUES (120, 12);
INSERT INTO userrole (userid, roleid) VALUES (120, 14);
INSERT INTO userrole (userid, roleid) VALUES (140, 6);
INSERT INTO userrole (userid, roleid) VALUES (140, 8);
INSERT INTO userrole (userid, roleid) VALUES (140, 10);
INSERT INTO userrole (userid, roleid) VALUES (140, 12);
INSERT INTO userrole (userid, roleid) VALUES (140, 14);
INSERT INTO userrole (userid, roleid) VALUES (150, 6);
INSERT INTO userrole (userid, roleid) VALUES (150, 8);
INSERT INTO userrole (userid, roleid) VALUES (150, 10);
INSERT INTO userrole (userid, roleid) VALUES (150, 12);
INSERT INTO userrole (userid, roleid) VALUES (150, 14);
INSERT INTO userrole (userid, roleid) VALUES (151, 6);
INSERT INTO userrole (userid, roleid) VALUES (151, 8);
INSERT INTO userrole (userid, roleid) VALUES (151, 10);
INSERT INTO userrole (userid, roleid) VALUES (151, 12);
INSERT INTO userrole (userid, roleid) VALUES (151, 14);
INSERT INTO userrole (userid, roleid) VALUES (155, 6);
INSERT INTO userrole (userid, roleid) VALUES (155, 8);
INSERT INTO userrole (userid, roleid) VALUES (155, 10);
INSERT INTO userrole (userid, roleid) VALUES (155, 12);
INSERT INTO userrole (userid, roleid) VALUES (155, 14);
INSERT INTO userrole (userid, roleid) VALUES (156, 6);
INSERT INTO userrole (userid, roleid) VALUES (156, 8);
INSERT INTO userrole (userid, roleid) VALUES (156, 10);
INSERT INTO userrole (userid, roleid) VALUES (156, 12);
INSERT INTO userrole (userid, roleid) VALUES (156, 14);
INSERT INTO userrole (userid, roleid) VALUES (168, 6);
INSERT INTO userrole (userid, roleid) VALUES (168, 8);
INSERT INTO userrole (userid, roleid) VALUES (168, 10);
INSERT INTO userrole (userid, roleid) VALUES (168, 12);
INSERT INTO userrole (userid, roleid) VALUES (168, 14);
INSERT INTO userrole (userid, roleid) VALUES (176, 6);
INSERT INTO userrole (userid, roleid) VALUES (176, 8);
INSERT INTO userrole (userid, roleid) VALUES (176, 10);
INSERT INTO userrole (userid, roleid) VALUES (176, 12);
INSERT INTO userrole (userid, roleid) VALUES (176, 14);
INSERT INTO userrole (userid, roleid) VALUES (205, 6);
INSERT INTO userrole (userid, roleid) VALUES (205, 8);
INSERT INTO userrole (userid, roleid) VALUES (205, 10);
INSERT INTO userrole (userid, roleid) VALUES (205, 12);
INSERT INTO userrole (userid, roleid) VALUES (205, 14);
INSERT INTO userrole (userid, roleid) VALUES (208, 6);
INSERT INTO userrole (userid, roleid) VALUES (208, 8);
INSERT INTO userrole (userid, roleid) VALUES (208, 10);
INSERT INTO userrole (userid, roleid) VALUES (208, 12);
INSERT INTO userrole (userid, roleid) VALUES (208, 14);
INSERT INTO userrole (userid, roleid) VALUES (211, 6);
INSERT INTO userrole (userid, roleid) VALUES (211, 8);
INSERT INTO userrole (userid, roleid) VALUES (211, 10);
INSERT INTO userrole (userid, roleid) VALUES (211, 12);
INSERT INTO userrole (userid, roleid) VALUES (211, 14);
INSERT INTO userrole (userid, roleid) VALUES (213, 6);
INSERT INTO userrole (userid, roleid) VALUES (213, 8);
INSERT INTO userrole (userid, roleid) VALUES (213, 10);
INSERT INTO userrole (userid, roleid) VALUES (213, 12);
INSERT INTO userrole (userid, roleid) VALUES (213, 14);
INSERT INTO userrole (userid, roleid) VALUES (215, 6);
INSERT INTO userrole (userid, roleid) VALUES (215, 8);
INSERT INTO userrole (userid, roleid) VALUES (215, 10);
INSERT INTO userrole (userid, roleid) VALUES (215, 12);
INSERT INTO userrole (userid, roleid) VALUES (215, 14);
INSERT INTO userrole (userid, roleid) VALUES (226, 6);
INSERT INTO userrole (userid, roleid) VALUES (226, 8);
INSERT INTO userrole (userid, roleid) VALUES (226, 10);
INSERT INTO userrole (userid, roleid) VALUES (226, 12);
INSERT INTO userrole (userid, roleid) VALUES (226, 14);
INSERT INTO userrole (userid, roleid) VALUES (230, 6);
INSERT INTO userrole (userid, roleid) VALUES (230, 8);
INSERT INTO userrole (userid, roleid) VALUES (230, 10);
INSERT INTO userrole (userid, roleid) VALUES (230, 12);
INSERT INTO userrole (userid, roleid) VALUES (230, 14);
INSERT INTO userrole (userid, roleid) VALUES (234, 6);
INSERT INTO userrole (userid, roleid) VALUES (234, 8);
INSERT INTO userrole (userid, roleid) VALUES (234, 10);
INSERT INTO userrole (userid, roleid) VALUES (234, 12);
INSERT INTO userrole (userid, roleid) VALUES (234, 14);
INSERT INTO userrole (userid, roleid) VALUES (239, 6);
INSERT INTO userrole (userid, roleid) VALUES (239, 8);
INSERT INTO userrole (userid, roleid) VALUES (239, 10);
INSERT INTO userrole (userid, roleid) VALUES (239, 12);
INSERT INTO userrole (userid, roleid) VALUES (239, 14);
INSERT INTO userrole (userid, roleid) VALUES (241, 6);
INSERT INTO userrole (userid, roleid) VALUES (241, 8);
INSERT INTO userrole (userid, roleid) VALUES (241, 10);
INSERT INTO userrole (userid, roleid) VALUES (241, 12);
INSERT INTO userrole (userid, roleid) VALUES (241, 14);
INSERT INTO userrole (userid, roleid) VALUES (251, 6);
INSERT INTO userrole (userid, roleid) VALUES (251, 8);
INSERT INTO userrole (userid, roleid) VALUES (251, 10);
INSERT INTO userrole (userid, roleid) VALUES (251, 12);
INSERT INTO userrole (userid, roleid) VALUES (251, 14);
INSERT INTO userrole (userid, roleid) VALUES (2, 7);
INSERT INTO userrole (userid, roleid) VALUES (52, 7);
INSERT INTO userrole (userid, roleid) VALUES (6, 7);
INSERT INTO userrole (userid, roleid) VALUES (7, 7);
INSERT INTO userrole (userid, roleid) VALUES (133, 7);
INSERT INTO userrole (userid, roleid) VALUES (8, 7);
INSERT INTO userrole (userid, roleid) VALUES (9, 7);
INSERT INTO userrole (userid, roleid) VALUES (10, 7);
INSERT INTO userrole (userid, roleid) VALUES (209, 7);
INSERT INTO userrole (userid, roleid) VALUES (11, 7);
INSERT INTO userrole (userid, roleid) VALUES (12, 7);
INSERT INTO userrole (userid, roleid) VALUES (13, 7);
INSERT INTO userrole (userid, roleid) VALUES (14, 7);
INSERT INTO userrole (userid, roleid) VALUES (16, 7);
INSERT INTO userrole (userid, roleid) VALUES (17, 7);
INSERT INTO userrole (userid, roleid) VALUES (18, 7);
INSERT INTO userrole (userid, roleid) VALUES (19, 7);
INSERT INTO userrole (userid, roleid) VALUES (20, 7);
INSERT INTO userrole (userid, roleid) VALUES (21, 7);
INSERT INTO userrole (userid, roleid) VALUES (22, 7);
INSERT INTO userrole (userid, roleid) VALUES (25, 7);
INSERT INTO userrole (userid, roleid) VALUES (26, 7);
INSERT INTO userrole (userid, roleid) VALUES (144, 7);
INSERT INTO userrole (userid, roleid) VALUES (28, 7);
INSERT INTO userrole (userid, roleid) VALUES (29, 7);
INSERT INTO userrole (userid, roleid) VALUES (30, 7);
INSERT INTO userrole (userid, roleid) VALUES (31, 7);
INSERT INTO userrole (userid, roleid) VALUES (32, 7);
INSERT INTO userrole (userid, roleid) VALUES (33, 7);
INSERT INTO userrole (userid, roleid) VALUES (35, 7);
INSERT INTO userrole (userid, roleid) VALUES (36, 7);
INSERT INTO userrole (userid, roleid) VALUES (37, 7);
INSERT INTO userrole (userid, roleid) VALUES (38, 7);
INSERT INTO userrole (userid, roleid) VALUES (39, 7);
INSERT INTO userrole (userid, roleid) VALUES (40, 7);
INSERT INTO userrole (userid, roleid) VALUES (41, 7);
INSERT INTO userrole (userid, roleid) VALUES (42, 7);
INSERT INTO userrole (userid, roleid) VALUES (43, 7);
INSERT INTO userrole (userid, roleid) VALUES (45, 7);
INSERT INTO userrole (userid, roleid) VALUES (128, 7);
INSERT INTO userrole (userid, roleid) VALUES (49, 7);
INSERT INTO userrole (userid, roleid) VALUES (54, 7);
INSERT INTO userrole (userid, roleid) VALUES (55, 7);
INSERT INTO userrole (userid, roleid) VALUES (56, 7);
INSERT INTO userrole (userid, roleid) VALUES (57, 7);
INSERT INTO userrole (userid, roleid) VALUES (58, 7);
INSERT INTO userrole (userid, roleid) VALUES (59, 7);
INSERT INTO userrole (userid, roleid) VALUES (61, 7);
INSERT INTO userrole (userid, roleid) VALUES (62, 7);
INSERT INTO userrole (userid, roleid) VALUES (64, 7);
INSERT INTO userrole (userid, roleid) VALUES (65, 7);
INSERT INTO userrole (userid, roleid) VALUES (66, 7);
INSERT INTO userrole (userid, roleid) VALUES (67, 7);
INSERT INTO userrole (userid, roleid) VALUES (68, 7);
INSERT INTO userrole (userid, roleid) VALUES (69, 7);
INSERT INTO userrole (userid, roleid) VALUES (70, 7);
INSERT INTO userrole (userid, roleid) VALUES (71, 7);
INSERT INTO userrole (userid, roleid) VALUES (72, 7);
INSERT INTO userrole (userid, roleid) VALUES (73, 7);
INSERT INTO userrole (userid, roleid) VALUES (75, 7);
INSERT INTO userrole (userid, roleid) VALUES (77, 7);
INSERT INTO userrole (userid, roleid) VALUES (78, 7);
INSERT INTO userrole (userid, roleid) VALUES (79, 7);
INSERT INTO userrole (userid, roleid) VALUES (80, 7);
INSERT INTO userrole (userid, roleid) VALUES (81, 7);
INSERT INTO userrole (userid, roleid) VALUES (82, 7);
INSERT INTO userrole (userid, roleid) VALUES (83, 7);
INSERT INTO userrole (userid, roleid) VALUES (85, 7);
INSERT INTO userrole (userid, roleid) VALUES (86, 7);
INSERT INTO userrole (userid, roleid) VALUES (87, 7);
INSERT INTO userrole (userid, roleid) VALUES (88, 7);
INSERT INTO userrole (userid, roleid) VALUES (89, 7);
INSERT INTO userrole (userid, roleid) VALUES (90, 7);
INSERT INTO userrole (userid, roleid) VALUES (91, 7);
INSERT INTO userrole (userid, roleid) VALUES (92, 7);
INSERT INTO userrole (userid, roleid) VALUES (93, 7);
INSERT INTO userrole (userid, roleid) VALUES (94, 7);
INSERT INTO userrole (userid, roleid) VALUES (95, 7);
INSERT INTO userrole (userid, roleid) VALUES (97, 7);
INSERT INTO userrole (userid, roleid) VALUES (98, 7);
INSERT INTO userrole (userid, roleid) VALUES (99, 7);
INSERT INTO userrole (userid, roleid) VALUES (100, 7);
INSERT INTO userrole (userid, roleid) VALUES (101, 7);
INSERT INTO userrole (userid, roleid) VALUES (102, 7);
INSERT INTO userrole (userid, roleid) VALUES (103, 7);
INSERT INTO userrole (userid, roleid) VALUES (104, 7);
INSERT INTO userrole (userid, roleid) VALUES (105, 7);
INSERT INTO userrole (userid, roleid) VALUES (106, 7);
INSERT INTO userrole (userid, roleid) VALUES (107, 7);
INSERT INTO userrole (userid, roleid) VALUES (108, 7);
INSERT INTO userrole (userid, roleid) VALUES (109, 7);
INSERT INTO userrole (userid, roleid) VALUES (110, 7);
INSERT INTO userrole (userid, roleid) VALUES (111, 7);
INSERT INTO userrole (userid, roleid) VALUES (112, 7);
INSERT INTO userrole (userid, roleid) VALUES (113, 7);
INSERT INTO userrole (userid, roleid) VALUES (114, 7);
INSERT INTO userrole (userid, roleid) VALUES (115, 7);
INSERT INTO userrole (userid, roleid) VALUES (116, 7);
INSERT INTO userrole (userid, roleid) VALUES (117, 7);
INSERT INTO userrole (userid, roleid) VALUES (118, 7);
INSERT INTO userrole (userid, roleid) VALUES (119, 7);
INSERT INTO userrole (userid, roleid) VALUES (120, 7);
INSERT INTO userrole (userid, roleid) VALUES (121, 7);
INSERT INTO userrole (userid, roleid) VALUES (122, 7);
INSERT INTO userrole (userid, roleid) VALUES (123, 7);
INSERT INTO userrole (userid, roleid) VALUES (124, 7);
INSERT INTO userrole (userid, roleid) VALUES (125, 7);
INSERT INTO userrole (userid, roleid) VALUES (126, 7);
INSERT INTO userrole (userid, roleid) VALUES (127, 7);
INSERT INTO userrole (userid, roleid) VALUES (129, 7);
INSERT INTO userrole (userid, roleid) VALUES (130, 7);
INSERT INTO userrole (userid, roleid) VALUES (131, 7);
INSERT INTO userrole (userid, roleid) VALUES (132, 7);
INSERT INTO userrole (userid, roleid) VALUES (134, 7);
INSERT INTO userrole (userid, roleid) VALUES (135, 7);
INSERT INTO userrole (userid, roleid) VALUES (136, 7);
INSERT INTO userrole (userid, roleid) VALUES (137, 7);
INSERT INTO userrole (userid, roleid) VALUES (138, 7);
INSERT INTO userrole (userid, roleid) VALUES (139, 7);
INSERT INTO userrole (userid, roleid) VALUES (140, 7);
INSERT INTO userrole (userid, roleid) VALUES (141, 7);
INSERT INTO userrole (userid, roleid) VALUES (142, 7);
INSERT INTO userrole (userid, roleid) VALUES (143, 7);
INSERT INTO userrole (userid, roleid) VALUES (148, 7);
INSERT INTO userrole (userid, roleid) VALUES (146, 7);
INSERT INTO userrole (userid, roleid) VALUES (147, 7);
INSERT INTO userrole (userid, roleid) VALUES (150, 7);
INSERT INTO userrole (userid, roleid) VALUES (151, 7);
INSERT INTO userrole (userid, roleid) VALUES (153, 7);
INSERT INTO userrole (userid, roleid) VALUES (155, 7);
INSERT INTO userrole (userid, roleid) VALUES (156, 7);
INSERT INTO userrole (userid, roleid) VALUES (158, 7);
INSERT INTO userrole (userid, roleid) VALUES (159, 7);
INSERT INTO userrole (userid, roleid) VALUES (160, 7);
INSERT INTO userrole (userid, roleid) VALUES (161, 7);
INSERT INTO userrole (userid, roleid) VALUES (162, 7);
INSERT INTO userrole (userid, roleid) VALUES (163, 7);
INSERT INTO userrole (userid, roleid) VALUES (164, 7);
INSERT INTO userrole (userid, roleid) VALUES (165, 7);
INSERT INTO userrole (userid, roleid) VALUES (166, 7);
INSERT INTO userrole (userid, roleid) VALUES (167, 7);
INSERT INTO userrole (userid, roleid) VALUES (168, 7);
INSERT INTO userrole (userid, roleid) VALUES (169, 7);
INSERT INTO userrole (userid, roleid) VALUES (170, 7);
INSERT INTO userrole (userid, roleid) VALUES (171, 7);
INSERT INTO userrole (userid, roleid) VALUES (172, 7);
INSERT INTO userrole (userid, roleid) VALUES (173, 7);
INSERT INTO userrole (userid, roleid) VALUES (174, 7);
INSERT INTO userrole (userid, roleid) VALUES (175, 7);
INSERT INTO userrole (userid, roleid) VALUES (176, 7);
INSERT INTO userrole (userid, roleid) VALUES (177, 7);
INSERT INTO userrole (userid, roleid) VALUES (178, 7);
INSERT INTO userrole (userid, roleid) VALUES (179, 7);
INSERT INTO userrole (userid, roleid) VALUES (180, 7);
INSERT INTO userrole (userid, roleid) VALUES (181, 7);
INSERT INTO userrole (userid, roleid) VALUES (182, 7);
INSERT INTO userrole (userid, roleid) VALUES (183, 7);
INSERT INTO userrole (userid, roleid) VALUES (184, 7);
INSERT INTO userrole (userid, roleid) VALUES (185, 7);
INSERT INTO userrole (userid, roleid) VALUES (186, 7);
INSERT INTO userrole (userid, roleid) VALUES (187, 7);
INSERT INTO userrole (userid, roleid) VALUES (188, 7);
INSERT INTO userrole (userid, roleid) VALUES (189, 7);
INSERT INTO userrole (userid, roleid) VALUES (190, 7);
INSERT INTO userrole (userid, roleid) VALUES (191, 7);
INSERT INTO userrole (userid, roleid) VALUES (192, 7);
INSERT INTO userrole (userid, roleid) VALUES (193, 7);
INSERT INTO userrole (userid, roleid) VALUES (194, 7);
INSERT INTO userrole (userid, roleid) VALUES (195, 7);
INSERT INTO userrole (userid, roleid) VALUES (196, 7);
INSERT INTO userrole (userid, roleid) VALUES (197, 7);
INSERT INTO userrole (userid, roleid) VALUES (198, 7);
INSERT INTO userrole (userid, roleid) VALUES (199, 7);
INSERT INTO userrole (userid, roleid) VALUES (200, 7);
INSERT INTO userrole (userid, roleid) VALUES (201, 7);
INSERT INTO userrole (userid, roleid) VALUES (202, 7);
INSERT INTO userrole (userid, roleid) VALUES (203, 7);
INSERT INTO userrole (userid, roleid) VALUES (204, 7);
INSERT INTO userrole (userid, roleid) VALUES (205, 7);
INSERT INTO userrole (userid, roleid) VALUES (206, 7);
INSERT INTO userrole (userid, roleid) VALUES (207, 7);
INSERT INTO userrole (userid, roleid) VALUES (208, 7);
INSERT INTO userrole (userid, roleid) VALUES (210, 7);
INSERT INTO userrole (userid, roleid) VALUES (211, 7);
INSERT INTO userrole (userid, roleid) VALUES (212, 7);
INSERT INTO userrole (userid, roleid) VALUES (213, 7);
INSERT INTO userrole (userid, roleid) VALUES (214, 7);
INSERT INTO userrole (userid, roleid) VALUES (215, 7);
INSERT INTO userrole (userid, roleid) VALUES (226, 7);
INSERT INTO userrole (userid, roleid) VALUES (227, 7);
INSERT INTO userrole (userid, roleid) VALUES (229, 7);
INSERT INTO userrole (userid, roleid) VALUES (230, 7);
INSERT INTO userrole (userid, roleid) VALUES (231, 7);
INSERT INTO userrole (userid, roleid) VALUES (232, 7);
INSERT INTO userrole (userid, roleid) VALUES (233, 7);
INSERT INTO userrole (userid, roleid) VALUES (234, 7);
INSERT INTO userrole (userid, roleid) VALUES (235, 7);
INSERT INTO userrole (userid, roleid) VALUES (236, 7);
INSERT INTO userrole (userid, roleid) VALUES (237, 7);
INSERT INTO userrole (userid, roleid) VALUES (238, 7);
INSERT INTO userrole (userid, roleid) VALUES (239, 7);
INSERT INTO userrole (userid, roleid) VALUES (241, 7);
INSERT INTO userrole (userid, roleid) VALUES (242, 7);
INSERT INTO userrole (userid, roleid) VALUES (243, 7);
INSERT INTO userrole (userid, roleid) VALUES (244, 7);
INSERT INTO userrole (userid, roleid) VALUES (245, 7);
INSERT INTO userrole (userid, roleid) VALUES (246, 7);
INSERT INTO userrole (userid, roleid) VALUES (247, 7);
INSERT INTO userrole (userid, roleid) VALUES (248, 7);
INSERT INTO userrole (userid, roleid) VALUES (249, 7);
INSERT INTO userrole (userid, roleid) VALUES (251, 7);
INSERT INTO userrole (userid, roleid) VALUES (2175, 7);
INSERT INTO userrole (userid, roleid) VALUES (7, 15);
INSERT INTO userrole (userid, roleid) VALUES (2177, 4);
INSERT INTO userrole (userid, roleid) VALUES (2178, 8);
INSERT INTO userrole (userid, roleid) VALUES (2178, 7);
INSERT INTO userrole (userid, roleid) VALUES (2179, 8);
INSERT INTO userrole (userid, roleid) VALUES (2179, 6);
INSERT INTO userrole (userid, roleid) VALUES (2179, 7);
INSERT INTO userrole (userid, roleid) VALUES (258, 8);
INSERT INTO userrole (userid, roleid) VALUES (258, 6);
INSERT INTO userrole (userid, roleid) VALUES (258, 7);
INSERT INTO userrole (userid, roleid) VALUES (2181, 8);
INSERT INTO userrole (userid, roleid) VALUES (2181, 6);
INSERT INTO userrole (userid, roleid) VALUES (2181, 1);
INSERT INTO userrole (userid, roleid) VALUES (2181, 13);
INSERT INTO userrole (userid, roleid) VALUES (2181, 10);
INSERT INTO userrole (userid, roleid) VALUES (2181, 7);
INSERT INTO userrole (userid, roleid) VALUES (2181, 12);
INSERT INTO userrole (userid, roleid) VALUES (2181, 4);
INSERT INTO userrole (userid, roleid) VALUES (2182, 8);
INSERT INTO userrole (userid, roleid) VALUES (2182, 7);
INSERT INTO userrole (userid, roleid) VALUES (251, 13);
INSERT INTO userrole (userid, roleid) VALUES (2183, 8);
INSERT INTO userrole (userid, roleid) VALUES (2183, 7);
INSERT INTO userrole (userid, roleid) VALUES (2184, 10);
INSERT INTO userrole (userid, roleid) VALUES (2184, 7);
INSERT INTO userrole (userid, roleid) VALUES (2184, 15);
INSERT INTO userrole (userid, roleid) VALUES (2185, 10);
INSERT INTO userrole (userid, roleid) VALUES (2185, 7);
INSERT INTO userrole (userid, roleid) VALUES (2185, 15);
INSERT INTO userrole (userid, roleid) VALUES (2189, 8);
INSERT INTO userrole (userid, roleid) VALUES (2189, 6);
INSERT INTO userrole (userid, roleid) VALUES (2189, 7);
INSERT INTO userrole (userid, roleid) VALUES (2190, 18);
INSERT INTO userrole (userid, roleid) VALUES (2190, 10);
INSERT INTO userrole (userid, roleid) VALUES (2190, 7);
INSERT INTO userrole (userid, roleid) VALUES (2185, 18);
INSERT INTO userrole (userid, roleid) VALUES (2191, 18);
INSERT INTO userrole (userid, roleid) VALUES (2191, 10);
INSERT INTO userrole (userid, roleid) VALUES (2191, 15);
INSERT INTO userrole (userid, roleid) VALUES (2184, 18);
INSERT INTO userrole (userid, roleid) VALUES (22, 18);
INSERT INTO userrole (userid, roleid) VALUES (230, 18);
INSERT INTO userrole (userid, roleid) VALUES (2198, 18);
INSERT INTO userrole (userid, roleid) VALUES (2199, 18);
INSERT INTO userrole (userid, roleid) VALUES (2200, 18);
INSERT INTO userrole (userid, roleid) VALUES (2201, 18);
INSERT INTO userrole (userid, roleid) VALUES (2202, 18);
INSERT INTO userrole (userid, roleid) VALUES (2204, 18);
INSERT INTO userrole (userid, roleid) VALUES (2192, 8);
INSERT INTO userrole (userid, roleid) VALUES (2192, 7);
INSERT INTO userrole (userid, roleid) VALUES (2205, 8);
INSERT INTO userrole (userid, roleid) VALUES (2205, 10);
INSERT INTO userrole (userid, roleid) VALUES (2205, 7);
INSERT INTO userrole (userid, roleid) VALUES (2206, 11);
INSERT INTO userrole (userid, roleid) VALUES (2207, 8);
INSERT INTO userrole (userid, roleid) VALUES (2207, 6);
INSERT INTO userrole (userid, roleid) VALUES (2207, 10);
INSERT INTO userrole (userid, roleid) VALUES (2207, 7);
INSERT INTO userrole (userid, roleid) VALUES (2209, 7);
INSERT INTO userrole (userid, roleid) VALUES (2208, 7);
INSERT INTO userrole (userid, roleid) VALUES (12, 11);
INSERT INTO userrole (userid, roleid) VALUES (2212, 7);
INSERT INTO userrole (userid, roleid) VALUES (39, 5);
INSERT INTO userrole (userid, roleid) VALUES (2213, 8);
INSERT INTO userrole (userid, roleid) VALUES (2213, 7);
INSERT INTO userrole (userid, roleid) VALUES (2214, 8);
INSERT INTO userrole (userid, roleid) VALUES (2214, 10);
INSERT INTO userrole (userid, roleid) VALUES (2214, 7);
INSERT INTO userrole (userid, roleid) VALUES (2215, 8);
INSERT INTO userrole (userid, roleid) VALUES (2215, 10);
INSERT INTO userrole (userid, roleid) VALUES (2215, 7);
INSERT INTO userrole (userid, roleid) VALUES (2215, 11);
INSERT INTO userrole (userid, roleid) VALUES (2216, 7);
INSERT INTO userrole (userid, roleid) VALUES (62, 11);
INSERT INTO userrole (userid, roleid) VALUES (133, 11);
INSERT INTO userrole (userid, roleid) VALUES (25, 11);
INSERT INTO userrole (userid, roleid) VALUES (2218, 8);
INSERT INTO userrole (userid, roleid) VALUES (2218, 6);
INSERT INTO userrole (userid, roleid) VALUES (2218, 10);
INSERT INTO userrole (userid, roleid) VALUES (2218, 7);
INSERT INTO userrole (userid, roleid) VALUES (2219, 11);
INSERT INTO userrole (userid, roleid) VALUES (2219, 8);
INSERT INTO userrole (userid, roleid) VALUES (2219, 7);
INSERT INTO userrole (userid, roleid) VALUES (2222, 8);
INSERT INTO userrole (userid, roleid) VALUES (2222, 6);
INSERT INTO userrole (userid, roleid) VALUES (2222, 1);
INSERT INTO userrole (userid, roleid) VALUES (2222, 7);
INSERT INTO userrole (userid, roleid) VALUES (2220, 8);
INSERT INTO userrole (userid, roleid) VALUES (2220, 7);
INSERT INTO userrole (userid, roleid) VALUES (2224, 7);
INSERT INTO userrole (userid, roleid) VALUES (2226, 11);
INSERT INTO userrole (userid, roleid) VALUES (2226, 7);
INSERT INTO userrole (userid, roleid) VALUES (2228, 7);
INSERT INTO userrole (userid, roleid) VALUES (2229, 8);
INSERT INTO userrole (userid, roleid) VALUES (2229, 10);
INSERT INTO userrole (userid, roleid) VALUES (2229, 7);
INSERT INTO userrole (userid, roleid) VALUES (2230, 8);
INSERT INTO userrole (userid, roleid) VALUES (2230, 10);
INSERT INTO userrole (userid, roleid) VALUES (2230, 7);
INSERT INTO userrole (userid, roleid) VALUES (2230, 11);
INSERT INTO userrole (userid, roleid) VALUES (2222, 4);
INSERT INTO userrole (userid, roleid) VALUES (124, 8);
INSERT INTO userrole (userid, roleid) VALUES (124, 10);
INSERT INTO userrole (userid, roleid) VALUES (2231, 8);
INSERT INTO userrole (userid, roleid) VALUES (2231, 10);
INSERT INTO userrole (userid, roleid) VALUES (2231, 7);
INSERT INTO userrole (userid, roleid) VALUES (2232, 18);
INSERT INTO userrole (userid, roleid) VALUES (2232, 10);
INSERT INTO userrole (userid, roleid) VALUES (2232, 7);
INSERT INTO userrole (userid, roleid) VALUES (3019, 8);
INSERT INTO userrole (userid, roleid) VALUES (3019, 7);
INSERT INTO userrole (userid, roleid) VALUES (3003, 8);
INSERT INTO userrole (userid, roleid) VALUES (3003, 7);
INSERT INTO userrole (userid, roleid) VALUES (3004, 8);
INSERT INTO userrole (userid, roleid) VALUES (3004, 7);
INSERT INTO userrole (userid, roleid) VALUES (3005, 8);
INSERT INTO userrole (userid, roleid) VALUES (3005, 7);
INSERT INTO userrole (userid, roleid) VALUES (3006, 8);
INSERT INTO userrole (userid, roleid) VALUES (3006, 7);
INSERT INTO userrole (userid, roleid) VALUES (3007, 8);
INSERT INTO userrole (userid, roleid) VALUES (3007, 7);
INSERT INTO userrole (userid, roleid) VALUES (3008, 8);
INSERT INTO userrole (userid, roleid) VALUES (3008, 7);
INSERT INTO userrole (userid, roleid) VALUES (3009, 8);
INSERT INTO userrole (userid, roleid) VALUES (3009, 7);
INSERT INTO userrole (userid, roleid) VALUES (3010, 8);
INSERT INTO userrole (userid, roleid) VALUES (3010, 7);
INSERT INTO userrole (userid, roleid) VALUES (3011, 8);
INSERT INTO userrole (userid, roleid) VALUES (3011, 7);
INSERT INTO userrole (userid, roleid) VALUES (3012, 8);
INSERT INTO userrole (userid, roleid) VALUES (3012, 7);
INSERT INTO userrole (userid, roleid) VALUES (3013, 8);
INSERT INTO userrole (userid, roleid) VALUES (3013, 7);
INSERT INTO userrole (userid, roleid) VALUES (3014, 8);
INSERT INTO userrole (userid, roleid) VALUES (3014, 7);
INSERT INTO userrole (userid, roleid) VALUES (2208, 8);
INSERT INTO userrole (userid, roleid) VALUES (3016, 8);
INSERT INTO userrole (userid, roleid) VALUES (3016, 7);
INSERT INTO userrole (userid, roleid) VALUES (3017, 8);
INSERT INTO userrole (userid, roleid) VALUES (3017, 7);
INSERT INTO userrole (userid, roleid) VALUES (2212, 8);
INSERT INTO userrole (userid, roleid) VALUES (3018, 8);
INSERT INTO userrole (userid, roleid) VALUES (3018, 7);
INSERT INTO userrole (userid, roleid) VALUES (3015, 8);
INSERT INTO userrole (userid, roleid) VALUES (3015, 7);
INSERT INTO userrole (userid, roleid) VALUES (2213, 6);
INSERT INTO userrole (userid, roleid) VALUES (2213, 10);
INSERT INTO userrole (userid, roleid) VALUES (2191, 7);
INSERT INTO userrole (userid, roleid) VALUES (2206, 7);
INSERT INTO userrole (userid, roleid) VALUES (2209, 8);
INSERT INTO userrole (userid, roleid) VALUES (3000, 8);
INSERT INTO userrole (userid, roleid) VALUES (3000, 7);
INSERT INTO userrole (userid, roleid) VALUES (3001, 8);
INSERT INTO userrole (userid, roleid) VALUES (3001, 7);
INSERT INTO userrole (userid, roleid) VALUES (3002, 8);
INSERT INTO userrole (userid, roleid) VALUES (3002, 7);
INSERT INTO userrole (userid, roleid) VALUES (49, 3);
INSERT INTO userrole (userid, roleid) VALUES (7, 3);
INSERT INTO userrole (userid, roleid) VALUES (2222, 3);
INSERT INTO userrole (userid, roleid) VALUES (2177, 3);
INSERT INTO userrole (userid, roleid) VALUES (2181, 3);
INSERT INTO userrole (userid, roleid) VALUES (2, 3);
INSERT INTO userrole (userid, roleid) VALUES (2221, 8);
INSERT INTO userrole (userid, roleid) VALUES (2221, 11);
INSERT INTO userrole (userid, roleid) VALUES (2221, 10);
INSERT INTO userrole (userid, roleid) VALUES (2221, 7);
INSERT INTO userrole (userid, roleid) VALUES (2236, 8);
INSERT INTO userrole (userid, roleid) VALUES (2236, 6);
INSERT INTO userrole (userid, roleid) VALUES (2236, 7);
INSERT INTO userrole (userid, roleid) VALUES (2238, 8);
INSERT INTO userrole (userid, roleid) VALUES (2238, 7);
INSERT INTO userrole (userid, roleid) VALUES (2237, 8);
INSERT INTO userrole (userid, roleid) VALUES (2237, 7);
INSERT INTO userrole (userid, roleid) VALUES (34, 8);
INSERT INTO userrole (userid, roleid) VALUES (34, 3);
INSERT INTO userrole (userid, roleid) VALUES (34, 6);
INSERT INTO userrole (userid, roleid) VALUES (34, 14);
INSERT INTO userrole (userid, roleid) VALUES (34, 10);
INSERT INTO userrole (userid, roleid) VALUES (34, 7);
INSERT INTO userrole (userid, roleid) VALUES (34, 2);
INSERT INTO userrole (userid, roleid) VALUES (34, 12);
INSERT INTO userrole (userid, roleid) VALUES (2240, 8);
INSERT INTO userrole (userid, roleid) VALUES (2240, 6);
INSERT INTO userrole (userid, roleid) VALUES (2240, 10);
INSERT INTO userrole (userid, roleid) VALUES (2240, 7);
INSERT INTO userrole (userid, roleid) VALUES (2242, 8);
INSERT INTO userrole (userid, roleid) VALUES (2242, 7);
INSERT INTO userrole (userid, roleid) VALUES (2243, 8);
INSERT INTO userrole (userid, roleid) VALUES (2243, 7);
INSERT INTO userrole (userid, roleid) VALUES (259, 2247);
INSERT INTO userrole (userid, roleid) VALUES (19, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3016, 2247);
INSERT INTO userrole (userid, roleid) VALUES (38, 2247);
INSERT INTO userrole (userid, roleid) VALUES (109, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3012, 2247);
INSERT INTO userrole (userid, roleid) VALUES (77, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3014, 2247);
INSERT INTO userrole (userid, roleid) VALUES (205, 2247);
INSERT INTO userrole (userid, roleid) VALUES (36, 2247);
INSERT INTO userrole (userid, roleid) VALUES (214, 2247);
INSERT INTO userrole (userid, roleid) VALUES (7, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2222, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2207, 2247);
INSERT INTO userrole (userid, roleid) VALUES (37, 2247);
INSERT INTO userrole (userid, roleid) VALUES (226, 2247);
INSERT INTO userrole (userid, roleid) VALUES (106, 2247);
INSERT INTO userrole (userid, roleid) VALUES (49, 2247);
INSERT INTO userrole (userid, roleid) VALUES (151, 2247);
INSERT INTO userrole (userid, roleid) VALUES (241, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3001, 2247);
INSERT INTO userrole (userid, roleid) VALUES (168, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3017, 2247);
INSERT INTO userrole (userid, roleid) VALUES (22, 2247);
INSERT INTO userrole (userid, roleid) VALUES (83, 2247);
INSERT INTO userrole (userid, roleid) VALUES (35, 2247);
INSERT INTO userrole (userid, roleid) VALUES (28, 2247);
INSERT INTO userrole (userid, roleid) VALUES (12, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3011, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3000, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3007, 2247);
INSERT INTO userrole (userid, roleid) VALUES (14, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3003, 2247);
INSERT INTO userrole (userid, roleid) VALUES (29, 2247);
INSERT INTO userrole (userid, roleid) VALUES (11, 2247);
INSERT INTO userrole (userid, roleid) VALUES (33, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3008, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2230, 2247);
INSERT INTO userrole (userid, roleid) VALUES (103, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2181, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3009, 2247);
INSERT INTO userrole (userid, roleid) VALUES (230, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3002, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3005, 2247);
INSERT INTO userrole (userid, roleid) VALUES (31, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2189, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3004, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3006, 2247);
INSERT INTO userrole (userid, roleid) VALUES (258, 2247);
INSERT INTO userrole (userid, roleid) VALUES (62, 2247);
INSERT INTO userrole (userid, roleid) VALUES (10, 2247);
INSERT INTO userrole (userid, roleid) VALUES (39, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3018, 2247);
INSERT INTO userrole (userid, roleid) VALUES (40, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3010, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3019, 2247);
INSERT INTO userrole (userid, roleid) VALUES (156, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3013, 2247);
INSERT INTO userrole (userid, roleid) VALUES (34, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2179, 2247);
INSERT INTO userrole (userid, roleid) VALUES (53, 8);
INSERT INTO userrole (userid, roleid) VALUES (53, 2247);
INSERT INTO userrole (userid, roleid) VALUES (53, 6);
INSERT INTO userrole (userid, roleid) VALUES (53, 14);
INSERT INTO userrole (userid, roleid) VALUES (53, 10);
INSERT INTO userrole (userid, roleid) VALUES (53, 7);
INSERT INTO userrole (userid, roleid) VALUES (53, 12);
INSERT INTO userrole (userid, roleid) VALUES (24, 6);
INSERT INTO userrole (userid, roleid) VALUES (24, 14);
INSERT INTO userrole (userid, roleid) VALUES (24, 10);
INSERT INTO userrole (userid, roleid) VALUES (24, 7);
INSERT INTO userrole (userid, roleid) VALUES (24, 8);
INSERT INTO userrole (userid, roleid) VALUES (24, 11);
INSERT INTO userrole (userid, roleid) VALUES (24, 3);
INSERT INTO userrole (userid, roleid) VALUES (24, 2247);
INSERT INTO userrole (userid, roleid) VALUES (24, 5);
INSERT INTO userrole (userid, roleid) VALUES (24, 2);
INSERT INTO userrole (userid, roleid) VALUES (24, 12);
INSERT INTO userrole (userid, roleid) VALUES (2235, 7);
INSERT INTO userrole (userid, roleid) VALUES (3016, 16);
INSERT INTO userrole (userid, roleid) VALUES (3012, 16);
INSERT INTO userrole (userid, roleid) VALUES (3011, 16);
INSERT INTO userrole (userid, roleid) VALUES (3000, 16);
INSERT INTO userrole (userid, roleid) VALUES (3014, 16);
INSERT INTO userrole (userid, roleid) VALUES (3007, 16);
INSERT INTO userrole (userid, roleid) VALUES (3003, 16);
INSERT INTO userrole (userid, roleid) VALUES (7, 16);
INSERT INTO userrole (userid, roleid) VALUES (3008, 16);
INSERT INTO userrole (userid, roleid) VALUES (3009, 16);
INSERT INTO userrole (userid, roleid) VALUES (24, 16);
INSERT INTO userrole (userid, roleid) VALUES (2236, 16);
INSERT INTO userrole (userid, roleid) VALUES (3002, 16);
INSERT INTO userrole (userid, roleid) VALUES (3005, 16);
INSERT INTO userrole (userid, roleid) VALUES (3001, 16);
INSERT INTO userrole (userid, roleid) VALUES (3004, 16);
INSERT INTO userrole (userid, roleid) VALUES (3006, 16);
INSERT INTO userrole (userid, roleid) VALUES (3015, 16);
INSERT INTO userrole (userid, roleid) VALUES (3017, 16);
INSERT INTO userrole (userid, roleid) VALUES (3018, 16);
INSERT INTO userrole (userid, roleid) VALUES (2209, 16);
INSERT INTO userrole (userid, roleid) VALUES (3010, 16);
INSERT INTO userrole (userid, roleid) VALUES (2212, 16);
INSERT INTO userrole (userid, roleid) VALUES (3019, 16);
INSERT INTO userrole (userid, roleid) VALUES (3013, 16);
INSERT INTO userrole (userid, roleid) VALUES (34, 16);
INSERT INTO userrole (userid, roleid) VALUES (154, 8);
INSERT INTO userrole (userid, roleid) VALUES (154, 2247);
INSERT INTO userrole (userid, roleid) VALUES (154, 14);
INSERT INTO userrole (userid, roleid) VALUES (154, 10);
INSERT INTO userrole (userid, roleid) VALUES (154, 7);
INSERT INTO userrole (userid, roleid) VALUES (154, 12);
INSERT INTO userrole (userid, roleid) VALUES (2248, 8);
INSERT INTO userrole (userid, roleid) VALUES (2248, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2248, 6);
INSERT INTO userrole (userid, roleid) VALUES (2248, 10);
INSERT INTO userrole (userid, roleid) VALUES (2248, 7);
INSERT INTO userrole (userid, roleid) VALUES (2254, 8);
INSERT INTO userrole (userid, roleid) VALUES (2254, 7);
INSERT INTO userrole (userid, roleid) VALUES (2252, 8);
INSERT INTO userrole (userid, roleid) VALUES (2252, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2252, 7);
INSERT INTO userrole (userid, roleid) VALUES (259, 2257);
INSERT INTO userrole (userid, roleid) VALUES (19, 2257);
INSERT INTO userrole (userid, roleid) VALUES (16, 2257);
INSERT INTO userrole (userid, roleid) VALUES (38, 2257);
INSERT INTO userrole (userid, roleid) VALUES (121, 2257);
INSERT INTO userrole (userid, roleid) VALUES (111, 2257);
INSERT INTO userrole (userid, roleid) VALUES (109, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2248, 2257);
INSERT INTO userrole (userid, roleid) VALUES (205, 2257);
INSERT INTO userrole (userid, roleid) VALUES (36, 2257);
INSERT INTO userrole (userid, roleid) VALUES (214, 2257);
INSERT INTO userrole (userid, roleid) VALUES (7, 2257);
INSERT INTO userrole (userid, roleid) VALUES (18, 2257);
INSERT INTO userrole (userid, roleid) VALUES (43, 2257);
INSERT INTO userrole (userid, roleid) VALUES (164, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2207, 2257);
INSERT INTO userrole (userid, roleid) VALUES (37, 2257);
INSERT INTO userrole (userid, roleid) VALUES (24, 2257);
INSERT INTO userrole (userid, roleid) VALUES (226, 2257);
INSERT INTO userrole (userid, roleid) VALUES (20, 2257);
INSERT INTO userrole (userid, roleid) VALUES (151, 2257);
INSERT INTO userrole (userid, roleid) VALUES (168, 2257);
INSERT INTO userrole (userid, roleid) VALUES (213, 2257);
INSERT INTO userrole (userid, roleid) VALUES (22, 2257);
INSERT INTO userrole (userid, roleid) VALUES (13, 2257);
INSERT INTO userrole (userid, roleid) VALUES (239, 2257);
INSERT INTO userrole (userid, roleid) VALUES (35, 2257);
INSERT INTO userrole (userid, roleid) VALUES (206, 2257);
INSERT INTO userrole (userid, roleid) VALUES (28, 2257);
INSERT INTO userrole (userid, roleid) VALUES (12, 2257);
INSERT INTO userrole (userid, roleid) VALUES (41, 2257);
INSERT INTO userrole (userid, roleid) VALUES (55, 2257);
INSERT INTO userrole (userid, roleid) VALUES (21, 2257);
INSERT INTO userrole (userid, roleid) VALUES (9, 2257);
INSERT INTO userrole (userid, roleid) VALUES (14, 2257);
INSERT INTO userrole (userid, roleid) VALUES (207, 2257);
INSERT INTO userrole (userid, roleid) VALUES (25, 2257);
INSERT INTO userrole (userid, roleid) VALUES (242, 2257);
INSERT INTO userrole (userid, roleid) VALUES (29, 2257);
INSERT INTO userrole (userid, roleid) VALUES (11, 2257);
INSERT INTO userrole (userid, roleid) VALUES (33, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2230, 2257);
INSERT INTO userrole (userid, roleid) VALUES (30, 2257);
INSERT INTO userrole (userid, roleid) VALUES (42, 2257);
INSERT INTO userrole (userid, roleid) VALUES (103, 2257);
INSERT INTO userrole (userid, roleid) VALUES (230, 2257);
INSERT INTO userrole (userid, roleid) VALUES (17, 2257);
INSERT INTO userrole (userid, roleid) VALUES (31, 2257);
INSERT INTO userrole (userid, roleid) VALUES (155, 2257);
INSERT INTO userrole (userid, roleid) VALUES (26, 2257);
INSERT INTO userrole (userid, roleid) VALUES (154, 2257);
INSERT INTO userrole (userid, roleid) VALUES (32, 2257);
INSERT INTO userrole (userid, roleid) VALUES (62, 2257);
INSERT INTO userrole (userid, roleid) VALUES (10, 2257);
INSERT INTO userrole (userid, roleid) VALUES (39, 2257);
INSERT INTO userrole (userid, roleid) VALUES (40, 2257);
INSERT INTO userrole (userid, roleid) VALUES (156, 2257);
INSERT INTO userrole (userid, roleid) VALUES (53, 2257);
INSERT INTO userrole (userid, roleid) VALUES (34, 2257);
INSERT INTO userrole (userid, roleid) VALUES (250, 18);
INSERT INTO userrole (userid, roleid) VALUES (250, 2257);
INSERT INTO userrole (userid, roleid) VALUES (250, 10);
INSERT INTO userrole (userid, roleid) VALUES (250, 7);
INSERT INTO userrole (userid, roleid) VALUES (250, 15);
INSERT INTO userrole (userid, roleid) VALUES (2217, 8);
INSERT INTO userrole (userid, roleid) VALUES (2217, 11);
INSERT INTO userrole (userid, roleid) VALUES (2217, 7);
INSERT INTO userrole (userid, roleid) VALUES (7, 2264);
INSERT INTO userrole (userid, roleid) VALUES (230, 2264);
INSERT INTO userrole (userid, roleid) VALUES (2185, 2263);
INSERT INTO userrole (userid, roleid) VALUES (7, 2263);
INSERT INTO userrole (userid, roleid) VALUES (230, 2263);
INSERT INTO userrole (userid, roleid) VALUES (148, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2267, 8);
INSERT INTO userrole (userid, roleid) VALUES (2267, 7);
INSERT INTO userrole (userid, roleid) VALUES (2250, 8);
INSERT INTO userrole (userid, roleid) VALUES (2250, 7);
INSERT INTO userrole (userid, roleid) VALUES (2260, 8);
INSERT INTO userrole (userid, roleid) VALUES (2260, 7);
INSERT INTO userrole (userid, roleid) VALUES (2259, 11);
INSERT INTO userrole (userid, roleid) VALUES (2259, 7);
INSERT INTO userrole (userid, roleid) VALUES (2268, 8);
INSERT INTO userrole (userid, roleid) VALUES (2268, 6);
INSERT INTO userrole (userid, roleid) VALUES (2268, 7);
INSERT INTO userrole (userid, roleid) VALUES (2261, 8);
INSERT INTO userrole (userid, roleid) VALUES (2261, 6);
INSERT INTO userrole (userid, roleid) VALUES (2261, 7);
INSERT INTO userrole (userid, roleid) VALUES (2261, 12);
INSERT INTO userrole (userid, roleid) VALUES (2272, 8);
INSERT INTO userrole (userid, roleid) VALUES (2272, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2272, 6);
INSERT INTO userrole (userid, roleid) VALUES (2272, 7);
INSERT INTO userrole (userid, roleid) VALUES (2272, 12);
INSERT INTO userrole (userid, roleid) VALUES (2241, 8);
INSERT INTO userrole (userid, roleid) VALUES (2241, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2241, 7);
INSERT INTO userrole (userid, roleid) VALUES (256, 8);
INSERT INTO userrole (userid, roleid) VALUES (256, 2247);
INSERT INTO userrole (userid, roleid) VALUES (256, 7);
INSERT INTO userrole (userid, roleid) VALUES (2282, 8);
INSERT INTO userrole (userid, roleid) VALUES (2282, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2282, 6);
INSERT INTO userrole (userid, roleid) VALUES (2282, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2282, 10);
INSERT INTO userrole (userid, roleid) VALUES (2282, 7);
INSERT INTO userrole (userid, roleid) VALUES (2282, 12);
INSERT INTO userrole (userid, roleid) VALUES (2285, 8);
INSERT INTO userrole (userid, roleid) VALUES (2285, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2285, 6);
INSERT INTO userrole (userid, roleid) VALUES (2285, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2285, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2285, 10);
INSERT INTO userrole (userid, roleid) VALUES (2285, 7);
INSERT INTO userrole (userid, roleid) VALUES (2285, 12);
INSERT INTO userrole (userid, roleid) VALUES (2280, 7);
INSERT INTO userrole (userid, roleid) VALUES (2280, 12);
INSERT INTO userrole (userid, roleid) VALUES (2284, 11);
INSERT INTO userrole (userid, roleid) VALUES (2284, 7);
INSERT INTO userrole (userid, roleid) VALUES (2284, 12);
INSERT INTO userrole (userid, roleid) VALUES (2287, 8);
INSERT INTO userrole (userid, roleid) VALUES (2287, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2287, 6);
INSERT INTO userrole (userid, roleid) VALUES (2287, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2287, 10);
INSERT INTO userrole (userid, roleid) VALUES (2287, 7);
INSERT INTO userrole (userid, roleid) VALUES (2287, 12);
INSERT INTO userrole (userid, roleid) VALUES (187, 9);
INSERT INTO userrole (userid, roleid) VALUES (244, 9);
INSERT INTO userrole (userid, roleid) VALUES (165, 9);
INSERT INTO userrole (userid, roleid) VALUES (16, 9);
INSERT INTO userrole (userid, roleid) VALUES (121, 9);
INSERT INTO userrole (userid, roleid) VALUES (100, 9);
INSERT INTO userrole (userid, roleid) VALUES (201, 9);
INSERT INTO userrole (userid, roleid) VALUES (143, 9);
INSERT INTO userrole (userid, roleid) VALUES (2248, 9);
INSERT INTO userrole (userid, roleid) VALUES (77, 9);
INSERT INTO userrole (userid, roleid) VALUES (243, 9);
INSERT INTO userrole (userid, roleid) VALUES (174, 9);
INSERT INTO userrole (userid, roleid) VALUES (99, 9);
INSERT INTO userrole (userid, roleid) VALUES (7, 9);
INSERT INTO userrole (userid, roleid) VALUES (150, 9);
INSERT INTO userrole (userid, roleid) VALUES (120, 9);
INSERT INTO userrole (userid, roleid) VALUES (18, 9);
INSERT INTO userrole (userid, roleid) VALUES (71, 9);
INSERT INTO userrole (userid, roleid) VALUES (166, 9);
INSERT INTO userrole (userid, roleid) VALUES (159, 9);
INSERT INTO userrole (userid, roleid) VALUES (24, 9);
INSERT INTO userrole (userid, roleid) VALUES (122, 9);
INSERT INTO userrole (userid, roleid) VALUES (245, 9);
INSERT INTO userrole (userid, roleid) VALUES (144, 9);
INSERT INTO userrole (userid, roleid) VALUES (125, 9);
INSERT INTO userrole (userid, roleid) VALUES (170, 9);
INSERT INTO userrole (userid, roleid) VALUES (248, 9);
INSERT INTO userrole (userid, roleid) VALUES (241, 9);
INSERT INTO userrole (userid, roleid) VALUES (195, 9);
INSERT INTO userrole (userid, roleid) VALUES (52, 9);
INSERT INTO userrole (userid, roleid) VALUES (247, 9);
INSERT INTO userrole (userid, roleid) VALUES (168, 9);
INSERT INTO userrole (userid, roleid) VALUES (2231, 9);
INSERT INTO userrole (userid, roleid) VALUES (79, 9);
INSERT INTO userrole (userid, roleid) VALUES (146, 9);
INSERT INTO userrole (userid, roleid) VALUES (171, 9);
INSERT INTO userrole (userid, roleid) VALUES (124, 9);
INSERT INTO userrole (userid, roleid) VALUES (246, 9);
INSERT INTO userrole (userid, roleid) VALUES (123, 9);
INSERT INTO userrole (userid, roleid) VALUES (190, 9);
INSERT INTO userrole (userid, roleid) VALUES (102, 9);
INSERT INTO userrole (userid, roleid) VALUES (129, 9);
INSERT INTO userrole (userid, roleid) VALUES (2218, 9);
INSERT INTO userrole (userid, roleid) VALUES (167, 9);
INSERT INTO userrole (userid, roleid) VALUES (12, 9);
INSERT INTO userrole (userid, roleid) VALUES (56, 9);
INSERT INTO userrole (userid, roleid) VALUES (101, 9);
INSERT INTO userrole (userid, roleid) VALUES (2287, 9);
INSERT INTO userrole (userid, roleid) VALUES (2205, 9);
INSERT INTO userrole (userid, roleid) VALUES (2238, 9);
INSERT INTO userrole (userid, roleid) VALUES (54, 9);
INSERT INTO userrole (userid, roleid) VALUES (25, 9);
INSERT INTO userrole (userid, roleid) VALUES (142, 9);
INSERT INTO userrole (userid, roleid) VALUES (98, 9);
INSERT INTO userrole (userid, roleid) VALUES (30, 9);
INSERT INTO userrole (userid, roleid) VALUES (93, 9);
INSERT INTO userrole (userid, roleid) VALUES (204, 9);
INSERT INTO userrole (userid, roleid) VALUES (17, 9);
INSERT INTO userrole (userid, roleid) VALUES (2, 9);
INSERT INTO userrole (userid, roleid) VALUES (31, 9);
INSERT INTO userrole (userid, roleid) VALUES (155, 9);
INSERT INTO userrole (userid, roleid) VALUES (140, 9);
INSERT INTO userrole (userid, roleid) VALUES (2280, 9);
INSERT INTO userrole (userid, roleid) VALUES (173, 9);
INSERT INTO userrole (userid, roleid) VALUES (188, 9);
INSERT INTO userrole (userid, roleid) VALUES (141, 9);
INSERT INTO userrole (userid, roleid) VALUES (32, 9);
INSERT INTO userrole (userid, roleid) VALUES (10, 9);
INSERT INTO userrole (userid, roleid) VALUES (39, 9);
INSERT INTO userrole (userid, roleid) VALUES (139, 9);
INSERT INTO userrole (userid, roleid) VALUES (94, 9);
INSERT INTO userrole (userid, roleid) VALUES (138, 9);
INSERT INTO userrole (userid, roleid) VALUES (175, 9);
INSERT INTO userrole (userid, roleid) VALUES (156, 9);
INSERT INTO userrole (userid, roleid) VALUES (53, 9);
INSERT INTO userrole (userid, roleid) VALUES (75, 9);
INSERT INTO userrole (userid, roleid) VALUES (78, 9);
INSERT INTO userrole (userid, roleid) VALUES (6, 9);
INSERT INTO userrole (userid, roleid) VALUES (3013, 9);
INSERT INTO userrole (userid, roleid) VALUES (2179, 9);
INSERT INTO userrole (userid, roleid) VALUES (2268, 9);
INSERT INTO userrole (userid, roleid) VALUES (97, 9);
INSERT INTO userrole (userid, roleid) VALUES (200, 9);
INSERT INTO userrole (userid, roleid) VALUES (38, 9);
INSERT INTO userrole (userid, roleid) VALUES (3012, 9);
INSERT INTO userrole (userid, roleid) VALUES (70, 9);
INSERT INTO userrole (userid, roleid) VALUES (202, 9);
INSERT INTO userrole (userid, roleid) VALUES (2219, 9);
INSERT INTO userrole (userid, roleid) VALUES (2282, 9);
INSERT INTO userrole (userid, roleid) VALUES (172, 9);
INSERT INTO userrole (userid, roleid) VALUES (194, 9);
INSERT INTO userrole (userid, roleid) VALUES (179, 9);
INSERT INTO userrole (userid, roleid) VALUES (43, 9);
INSERT INTO userrole (userid, roleid) VALUES (37, 9);
INSERT INTO userrole (userid, roleid) VALUES (226, 9);
INSERT INTO userrole (userid, roleid) VALUES (115, 9);
INSERT INTO userrole (userid, roleid) VALUES (2214, 9);
INSERT INTO userrole (userid, roleid) VALUES (137, 9);
INSERT INTO userrole (userid, roleid) VALUES (20, 9);
INSERT INTO userrole (userid, roleid) VALUES (176, 9);
INSERT INTO userrole (userid, roleid) VALUES (151, 9);
INSERT INTO userrole (userid, roleid) VALUES (192, 9);
INSERT INTO userrole (userid, roleid) VALUES (177, 9);
INSERT INTO userrole (userid, roleid) VALUES (2252, 9);
INSERT INTO userrole (userid, roleid) VALUES (193, 9);
INSERT INTO userrole (userid, roleid) VALUES (95, 9);
INSERT INTO userrole (userid, roleid) VALUES (81, 9);
INSERT INTO userrole (userid, roleid) VALUES (119, 9);
INSERT INTO userrole (userid, roleid) VALUES (203, 9);
INSERT INTO userrole (userid, roleid) VALUES (2237, 9);
INSERT INTO userrole (userid, roleid) VALUES (22, 9);
INSERT INTO userrole (userid, roleid) VALUES (13, 9);
INSERT INTO userrole (userid, roleid) VALUES (239, 9);
INSERT INTO userrole (userid, roleid) VALUES (2217, 9);
INSERT INTO userrole (userid, roleid) VALUES (80, 9);
INSERT INTO userrole (userid, roleid) VALUES (2183, 9);
INSERT INTO userrole (userid, roleid) VALUES (3011, 9);
INSERT INTO userrole (userid, roleid) VALUES (69, 9);
INSERT INTO userrole (userid, roleid) VALUES (3007, 9);
INSERT INTO userrole (userid, roleid) VALUES (2216, 9);
INSERT INTO userrole (userid, roleid) VALUES (14, 9);
INSERT INTO userrole (userid, roleid) VALUES (238, 9);
INSERT INTO userrole (userid, roleid) VALUES (3003, 9);
INSERT INTO userrole (userid, roleid) VALUES (242, 9);
INSERT INTO userrole (userid, roleid) VALUES (29, 9);
INSERT INTO userrole (userid, roleid) VALUES (3008, 9);
INSERT INTO userrole (userid, roleid) VALUES (103, 9);
INSERT INTO userrole (userid, roleid) VALUES (3009, 9);
INSERT INTO userrole (userid, roleid) VALUES (191, 9);
INSERT INTO userrole (userid, roleid) VALUES (2236, 9);
INSERT INTO userrole (userid, roleid) VALUES (3005, 9);
INSERT INTO userrole (userid, roleid) VALUES (117, 9);
INSERT INTO userrole (userid, roleid) VALUES (198, 9);
INSERT INTO userrole (userid, roleid) VALUES (147, 9);
INSERT INTO userrole (userid, roleid) VALUES (45, 9);
INSERT INTO userrole (userid, roleid) VALUES (199, 9);
INSERT INTO userrole (userid, roleid) VALUES (2213, 9);
INSERT INTO userrole (userid, roleid) VALUES (68, 9);
INSERT INTO userrole (userid, roleid) VALUES (3004, 9);
INSERT INTO userrole (userid, roleid) VALUES (3006, 9);
INSERT INTO userrole (userid, roleid) VALUES (73, 9);
INSERT INTO userrole (userid, roleid) VALUES (153, 9);
INSERT INTO userrole (userid, roleid) VALUES (72, 9);
INSERT INTO userrole (userid, roleid) VALUES (3010, 9);
INSERT INTO userrole (userid, roleid) VALUES (116, 9);
INSERT INTO userrole (userid, roleid) VALUES (2215, 9);
INSERT INTO userrole (userid, roleid) VALUES (118, 9);
INSERT INTO userrole (userid, roleid) VALUES (181, 9);
INSERT INTO userrole (userid, roleid) VALUES (2220, 9);
INSERT INTO userrole (userid, roleid) VALUES (66, 9);
INSERT INTO userrole (userid, roleid) VALUES (104, 9);
INSERT INTO userrole (userid, roleid) VALUES (3014, 9);
INSERT INTO userrole (userid, roleid) VALUES (88, 9);
INSERT INTO userrole (userid, roleid) VALUES (205, 9);
INSERT INTO userrole (userid, roleid) VALUES (183, 9);
INSERT INTO userrole (userid, roleid) VALUES (161, 9);
INSERT INTO userrole (userid, roleid) VALUES (36, 9);
INSERT INTO userrole (userid, roleid) VALUES (87, 9);
INSERT INTO userrole (userid, roleid) VALUES (2222, 9);
INSERT INTO userrole (userid, roleid) VALUES (64, 9);
INSERT INTO userrole (userid, roleid) VALUES (106, 9);
INSERT INTO userrole (userid, roleid) VALUES (67, 9);
INSERT INTO userrole (userid, roleid) VALUES (3001, 9);
INSERT INTO userrole (userid, roleid) VALUES (2228, 9);
INSERT INTO userrole (userid, roleid) VALUES (2272, 9);
INSERT INTO userrole (userid, roleid) VALUES (3015, 9);
INSERT INTO userrole (userid, roleid) VALUES (208, 9);
INSERT INTO userrole (userid, roleid) VALUES (2221, 9);
INSERT INTO userrole (userid, roleid) VALUES (61, 9);
INSERT INTO userrole (userid, roleid) VALUES (210, 9);
INSERT INTO userrole (userid, roleid) VALUES (105, 9);
INSERT INTO userrole (userid, roleid) VALUES (82, 9);
INSERT INTO userrole (userid, roleid) VALUES (35, 9);
INSERT INTO userrole (userid, roleid) VALUES (180, 9);
INSERT INTO userrole (userid, roleid) VALUES (28, 9);
INSERT INTO userrole (userid, roleid) VALUES (212, 9);
INSERT INTO userrole (userid, roleid) VALUES (8, 9);
INSERT INTO userrole (userid, roleid) VALUES (41, 9);
INSERT INTO userrole (userid, roleid) VALUES (2284, 9);
INSERT INTO userrole (userid, roleid) VALUES (21, 9);
INSERT INTO userrole (userid, roleid) VALUES (9, 9);
INSERT INTO userrole (userid, roleid) VALUES (3000, 9);
INSERT INTO userrole (userid, roleid) VALUES (227, 9);
INSERT INTO userrole (userid, roleid) VALUES (207, 9);
INSERT INTO userrole (userid, roleid) VALUES (229, 9);
INSERT INTO userrole (userid, roleid) VALUES (65, 9);
INSERT INTO userrole (userid, roleid) VALUES (186, 9);
INSERT INTO userrole (userid, roleid) VALUES (33, 9);
INSERT INTO userrole (userid, roleid) VALUES (42, 9);
INSERT INTO userrole (userid, roleid) VALUES (2181, 9);
INSERT INTO userrole (userid, roleid) VALUES (3002, 9);
INSERT INTO userrole (userid, roleid) VALUES (86, 9);
INSERT INTO userrole (userid, roleid) VALUES (2285, 9);
INSERT INTO userrole (userid, roleid) VALUES (26, 9);
INSERT INTO userrole (userid, roleid) VALUES (184, 9);
INSERT INTO userrole (userid, roleid) VALUES (154, 9);
INSERT INTO userrole (userid, roleid) VALUES (2259, 9);
INSERT INTO userrole (userid, roleid) VALUES (107, 9);
INSERT INTO userrole (userid, roleid) VALUES (160, 9);
INSERT INTO userrole (userid, roleid) VALUES (185, 9);
INSERT INTO userrole (userid, roleid) VALUES (40, 9);
INSERT INTO userrole (userid, roleid) VALUES (2212, 9);
INSERT INTO userrole (userid, roleid) VALUES (131, 9);
INSERT INTO userrole (userid, roleid) VALUES (178, 9);
INSERT INTO userrole (userid, roleid) VALUES (211, 9);
INSERT INTO userrole (userid, roleid) VALUES (34, 9);
INSERT INTO userrole (userid, roleid) VALUES (234, 9);
INSERT INTO userrole (userid, roleid) VALUES (3016, 9);
INSERT INTO userrole (userid, roleid) VALUES (19, 9);
INSERT INTO userrole (userid, roleid) VALUES (111, 9);
INSERT INTO userrole (userid, roleid) VALUES (2224, 9);
INSERT INTO userrole (userid, roleid) VALUES (109, 9);
INSERT INTO userrole (userid, roleid) VALUES (130, 9);
INSERT INTO userrole (userid, roleid) VALUES (92, 9);
INSERT INTO userrole (userid, roleid) VALUES (2208, 9);
INSERT INTO userrole (userid, roleid) VALUES (113, 9);
INSERT INTO userrole (userid, roleid) VALUES (214, 9);
INSERT INTO userrole (userid, roleid) VALUES (2261, 9);
INSERT INTO userrole (userid, roleid) VALUES (59, 9);
INSERT INTO userrole (userid, roleid) VALUES (236, 9);
INSERT INTO userrole (userid, roleid) VALUES (164, 9);
INSERT INTO userrole (userid, roleid) VALUES (215, 9);
INSERT INTO userrole (userid, roleid) VALUES (90, 9);
INSERT INTO userrole (userid, roleid) VALUES (2207, 9);
INSERT INTO userrole (userid, roleid) VALUES (2192, 9);
INSERT INTO userrole (userid, roleid) VALUES (49, 9);
INSERT INTO userrole (userid, roleid) VALUES (162, 9);
INSERT INTO userrole (userid, roleid) VALUES (132, 9);
INSERT INTO userrole (userid, roleid) VALUES (57, 9);
INSERT INTO userrole (userid, roleid) VALUES (169, 9);
INSERT INTO userrole (userid, roleid) VALUES (3017, 9);
INSERT INTO userrole (userid, roleid) VALUES (135, 9);
INSERT INTO userrole (userid, roleid) VALUES (213, 9);
INSERT INTO userrole (userid, roleid) VALUES (2209, 9);
INSERT INTO userrole (userid, roleid) VALUES (108, 9);
INSERT INTO userrole (userid, roleid) VALUES (58, 9);
INSERT INTO userrole (userid, roleid) VALUES (2226, 9);
INSERT INTO userrole (userid, roleid) VALUES (83, 9);
INSERT INTO userrole (userid, roleid) VALUES (127, 9);
INSERT INTO userrole (userid, roleid) VALUES (206, 9);
INSERT INTO userrole (userid, roleid) VALUES (189, 9);
INSERT INTO userrole (userid, roleid) VALUES (148, 9);
INSERT INTO userrole (userid, roleid) VALUES (133, 9);
INSERT INTO userrole (userid, roleid) VALUES (158, 9);
INSERT INTO userrole (userid, roleid) VALUES (91, 9);
INSERT INTO userrole (userid, roleid) VALUES (196, 9);
INSERT INTO userrole (userid, roleid) VALUES (85, 9);
INSERT INTO userrole (userid, roleid) VALUES (136, 9);
INSERT INTO userrole (userid, roleid) VALUES (55, 9);
INSERT INTO userrole (userid, roleid) VALUES (231, 9);
INSERT INTO userrole (userid, roleid) VALUES (2240, 9);
INSERT INTO userrole (userid, roleid) VALUES (251, 9);
INSERT INTO userrole (userid, roleid) VALUES (11, 9);
INSERT INTO userrole (userid, roleid) VALUES (2229, 9);
INSERT INTO userrole (userid, roleid) VALUES (2230, 9);
INSERT INTO userrole (userid, roleid) VALUES (230, 9);
INSERT INTO userrole (userid, roleid) VALUES (237, 9);
INSERT INTO userrole (userid, roleid) VALUES (114, 9);
INSERT INTO userrole (userid, roleid) VALUES (128, 9);
INSERT INTO userrole (userid, roleid) VALUES (209, 9);
INSERT INTO userrole (userid, roleid) VALUES (2189, 9);
INSERT INTO userrole (userid, roleid) VALUES (2241, 9);
INSERT INTO userrole (userid, roleid) VALUES (258, 9);
INSERT INTO userrole (userid, roleid) VALUES (62, 9);
INSERT INTO userrole (userid, roleid) VALUES (112, 9);
INSERT INTO userrole (userid, roleid) VALUES (3018, 9);
INSERT INTO userrole (userid, roleid) VALUES (250, 9);
INSERT INTO userrole (userid, roleid) VALUES (182, 9);
INSERT INTO userrole (userid, roleid) VALUES (235, 9);
INSERT INTO userrole (userid, roleid) VALUES (163, 9);
INSERT INTO userrole (userid, roleid) VALUES (126, 9);
INSERT INTO userrole (userid, roleid) VALUES (3019, 9);
INSERT INTO userrole (userid, roleid) VALUES (232, 9);
INSERT INTO userrole (userid, roleid) VALUES (134, 9);
INSERT INTO userrole (userid, roleid) VALUES (197, 9);
INSERT INTO userrole (userid, roleid) VALUES (89, 9);
INSERT INTO userrole (userid, roleid) VALUES (110, 9);
INSERT INTO userrole (userid, roleid) VALUES (256, 9);
INSERT INTO userrole (userid, roleid) VALUES (249, 9);
INSERT INTO userrole (userid, roleid) VALUES (233, 9);
INSERT INTO userrole (userid, roleid) VALUES (44, 6);
INSERT INTO userrole (userid, roleid) VALUES (44, 1);
INSERT INTO userrole (userid, roleid) VALUES (44, 13);
INSERT INTO userrole (userid, roleid) VALUES (44, 14);
INSERT INTO userrole (userid, roleid) VALUES (44, 16);
INSERT INTO userrole (userid, roleid) VALUES (44, 2263);
INSERT INTO userrole (userid, roleid) VALUES (44, 10);
INSERT INTO userrole (userid, roleid) VALUES (44, 7);
INSERT INTO userrole (userid, roleid) VALUES (44, 4);
INSERT INTO userrole (userid, roleid) VALUES (44, 15);
INSERT INTO userrole (userid, roleid) VALUES (44, 3);
INSERT INTO userrole (userid, roleid) VALUES (44, 2264);
INSERT INTO userrole (userid, roleid) VALUES (44, 8);
INSERT INTO userrole (userid, roleid) VALUES (44, 2247);
INSERT INTO userrole (userid, roleid) VALUES (44, 2257);
INSERT INTO userrole (userid, roleid) VALUES (44, 9);
INSERT INTO userrole (userid, roleid) VALUES (44, 5);
INSERT INTO userrole (userid, roleid) VALUES (44, 2);
INSERT INTO userrole (userid, roleid) VALUES (44, 12);
INSERT INTO userrole (userid, roleid) VALUES (44, 17);
INSERT INTO userrole (userid, roleid) VALUES (2291, 9);
INSERT INTO userrole (userid, roleid) VALUES (2291, 7);
INSERT INTO userrole (userid, roleid) VALUES (2288, 9);
INSERT INTO userrole (userid, roleid) VALUES (2288, 7);
INSERT INTO userrole (userid, roleid) VALUES (2288, 12);
INSERT INTO userrole (userid, roleid) VALUES (2255, 8);
INSERT INTO userrole (userid, roleid) VALUES (2255, 9);
INSERT INTO userrole (userid, roleid) VALUES (2255, 7);
INSERT INTO userrole (userid, roleid) VALUES (2298, 9);
INSERT INTO userrole (userid, roleid) VALUES (2298, 7);
INSERT INTO userrole (userid, roleid) VALUES (149, 9);
INSERT INTO userrole (userid, roleid) VALUES (149, 7);
INSERT INTO userrole (userid, roleid) VALUES (149, 12);
INSERT INTO userrole (userid, roleid) VALUES (2301, 8);
INSERT INTO userrole (userid, roleid) VALUES (2301, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2301, 9);
INSERT INTO userrole (userid, roleid) VALUES (2301, 7);
INSERT INTO userrole (userid, roleid) VALUES (2301, 12);
INSERT INTO userrole (userid, roleid) VALUES (2302, 8);
INSERT INTO userrole (userid, roleid) VALUES (2302, 7);
INSERT INTO userrole (userid, roleid) VALUES (2305, 8);
INSERT INTO userrole (userid, roleid) VALUES (2305, 7);
INSERT INTO userrole (userid, roleid) VALUES (2295, 8);
INSERT INTO userrole (userid, roleid) VALUES (2295, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2295, 6);
INSERT INTO userrole (userid, roleid) VALUES (2295, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2295, 9);
INSERT INTO userrole (userid, roleid) VALUES (2295, 7);
INSERT INTO userrole (userid, roleid) VALUES (2180, 8);
INSERT INTO userrole (userid, roleid) VALUES (2180, 11);
INSERT INTO userrole (userid, roleid) VALUES (2180, 9);
INSERT INTO userrole (userid, roleid) VALUES (2180, 7);
INSERT INTO userrole (userid, roleid) VALUES (2309, 8);
INSERT INTO userrole (userid, roleid) VALUES (2309, 7);
INSERT INTO userrole (userid, roleid) VALUES (2310, 8);
INSERT INTO userrole (userid, roleid) VALUES (2310, 7);
INSERT INTO userrole (userid, roleid) VALUES (2311, 8);
INSERT INTO userrole (userid, roleid) VALUES (2311, 7);
INSERT INTO userrole (userid, roleid) VALUES (76, 8);
INSERT INTO userrole (userid, roleid) VALUES (76, 11);
INSERT INTO userrole (userid, roleid) VALUES (76, 2264);
INSERT INTO userrole (userid, roleid) VALUES (76, 2247);
INSERT INTO userrole (userid, roleid) VALUES (76, 6);
INSERT INTO userrole (userid, roleid) VALUES (76, 2257);
INSERT INTO userrole (userid, roleid) VALUES (76, 14);
INSERT INTO userrole (userid, roleid) VALUES (76, 9);
INSERT INTO userrole (userid, roleid) VALUES (76, 2263);
INSERT INTO userrole (userid, roleid) VALUES (76, 10);
INSERT INTO userrole (userid, roleid) VALUES (76, 7);
INSERT INTO userrole (userid, roleid) VALUES (76, 12);
INSERT INTO userrole (userid, roleid) VALUES (2223, 8);
INSERT INTO userrole (userid, roleid) VALUES (2223, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2223, 6);
INSERT INTO userrole (userid, roleid) VALUES (2223, 9);
INSERT INTO userrole (userid, roleid) VALUES (2223, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2223, 10);
INSERT INTO userrole (userid, roleid) VALUES (2223, 7);
INSERT INTO userrole (userid, roleid) VALUES (60, 8);
INSERT INTO userrole (userid, roleid) VALUES (60, 2264);
INSERT INTO userrole (userid, roleid) VALUES (60, 2247);
INSERT INTO userrole (userid, roleid) VALUES (60, 6);
INSERT INTO userrole (userid, roleid) VALUES (60, 2257);
INSERT INTO userrole (userid, roleid) VALUES (60, 14);
INSERT INTO userrole (userid, roleid) VALUES (60, 9);
INSERT INTO userrole (userid, roleid) VALUES (60, 2263);
INSERT INTO userrole (userid, roleid) VALUES (60, 10);
INSERT INTO userrole (userid, roleid) VALUES (60, 7);
INSERT INTO userrole (userid, roleid) VALUES (60, 12);
INSERT INTO userrole (userid, roleid) VALUES (23, 8);
INSERT INTO userrole (userid, roleid) VALUES (23, 2264);
INSERT INTO userrole (userid, roleid) VALUES (23, 2247);
INSERT INTO userrole (userid, roleid) VALUES (23, 6);
INSERT INTO userrole (userid, roleid) VALUES (23, 2257);
INSERT INTO userrole (userid, roleid) VALUES (23, 14);
INSERT INTO userrole (userid, roleid) VALUES (23, 9);
INSERT INTO userrole (userid, roleid) VALUES (23, 2263);
INSERT INTO userrole (userid, roleid) VALUES (23, 10);
INSERT INTO userrole (userid, roleid) VALUES (23, 5);
INSERT INTO userrole (userid, roleid) VALUES (23, 7);
INSERT INTO userrole (userid, roleid) VALUES (23, 12);
INSERT INTO userrole (userid, roleid) VALUES (157, 8);
INSERT INTO userrole (userid, roleid) VALUES (157, 2247);
INSERT INTO userrole (userid, roleid) VALUES (157, 6);
INSERT INTO userrole (userid, roleid) VALUES (157, 2257);
INSERT INTO userrole (userid, roleid) VALUES (157, 14);
INSERT INTO userrole (userid, roleid) VALUES (157, 9);
INSERT INTO userrole (userid, roleid) VALUES (157, 2263);
INSERT INTO userrole (userid, roleid) VALUES (157, 10);
INSERT INTO userrole (userid, roleid) VALUES (157, 7);
INSERT INTO userrole (userid, roleid) VALUES (157, 12);
INSERT INTO userrole (userid, roleid) VALUES (84, 8);
INSERT INTO userrole (userid, roleid) VALUES (84, 2264);
INSERT INTO userrole (userid, roleid) VALUES (84, 2247);
INSERT INTO userrole (userid, roleid) VALUES (84, 6);
INSERT INTO userrole (userid, roleid) VALUES (84, 2257);
INSERT INTO userrole (userid, roleid) VALUES (84, 14);
INSERT INTO userrole (userid, roleid) VALUES (84, 9);
INSERT INTO userrole (userid, roleid) VALUES (84, 2263);
INSERT INTO userrole (userid, roleid) VALUES (84, 10);
INSERT INTO userrole (userid, roleid) VALUES (84, 7);
INSERT INTO userrole (userid, roleid) VALUES (84, 12);
INSERT INTO userrole (userid, roleid) VALUES (3, 6);
INSERT INTO userrole (userid, roleid) VALUES (3, 1);
INSERT INTO userrole (userid, roleid) VALUES (3, 13);
INSERT INTO userrole (userid, roleid) VALUES (3, 14);
INSERT INTO userrole (userid, roleid) VALUES (3, 2263);
INSERT INTO userrole (userid, roleid) VALUES (3, 10);
INSERT INTO userrole (userid, roleid) VALUES (3, 7);
INSERT INTO userrole (userid, roleid) VALUES (3, 4);
INSERT INTO userrole (userid, roleid) VALUES (3, 3);
INSERT INTO userrole (userid, roleid) VALUES (3, 2264);
INSERT INTO userrole (userid, roleid) VALUES (3, 8);
INSERT INTO userrole (userid, roleid) VALUES (3, 2247);
INSERT INTO userrole (userid, roleid) VALUES (3, 2257);
INSERT INTO userrole (userid, roleid) VALUES (3, 9);
INSERT INTO userrole (userid, roleid) VALUES (3, 5);
INSERT INTO userrole (userid, roleid) VALUES (3, 12);
INSERT INTO userrole (userid, roleid) VALUES (2318, 11);
INSERT INTO userrole (userid, roleid) VALUES (2318, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2318, 9);
INSERT INTO userrole (userid, roleid) VALUES (2318, 7);
INSERT INTO userrole (userid, roleid) VALUES (2194, 18);
INSERT INTO userrole (userid, roleid) VALUES (2194, 14);
INSERT INTO userrole (userid, roleid) VALUES (2194, 12);
INSERT INTO userrole (userid, roleid) VALUES (2195, 18);
INSERT INTO userrole (userid, roleid) VALUES (2195, 14);
INSERT INTO userrole (userid, roleid) VALUES (2195, 12);
INSERT INTO userrole (userid, roleid) VALUES (2196, 18);
INSERT INTO userrole (userid, roleid) VALUES (2196, 14);
INSERT INTO userrole (userid, roleid) VALUES (2196, 12);
INSERT INTO userrole (userid, roleid) VALUES (2197, 18);
INSERT INTO userrole (userid, roleid) VALUES (2197, 14);
INSERT INTO userrole (userid, roleid) VALUES (2197, 12);
INSERT INTO userrole (userid, roleid) VALUES (2319, 8);
INSERT INTO userrole (userid, roleid) VALUES (2319, 7);
INSERT INTO userrole (userid, roleid) VALUES (2211, 8);
INSERT INTO userrole (userid, roleid) VALUES (2211, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2211, 6);
INSERT INTO userrole (userid, roleid) VALUES (2211, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2211, 9);
INSERT INTO userrole (userid, roleid) VALUES (2211, 10);
INSERT INTO userrole (userid, roleid) VALUES (2211, 7);
INSERT INTO userrole (userid, roleid) VALUES (2325, 8);
INSERT INTO userrole (userid, roleid) VALUES (2325, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2325, 6);
INSERT INTO userrole (userid, roleid) VALUES (2325, 9);
INSERT INTO userrole (userid, roleid) VALUES (2325, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2325, 10);
INSERT INTO userrole (userid, roleid) VALUES (2325, 7);
INSERT INTO userrole (userid, roleid) VALUES (2325, 12);
INSERT INTO userrole (userid, roleid) VALUES (2326, 8);
INSERT INTO userrole (userid, roleid) VALUES (2326, 7);
INSERT INTO userrole (userid, roleid) VALUES (2328, 8);
INSERT INTO userrole (userid, roleid) VALUES (2328, 7);
INSERT INTO userrole (userid, roleid) VALUES (2324, 8);
INSERT INTO userrole (userid, roleid) VALUES (2324, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2324, 6);
INSERT INTO userrole (userid, roleid) VALUES (2324, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2324, 9);
INSERT INTO userrole (userid, roleid) VALUES (2324, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2324, 7);
INSERT INTO userrole (userid, roleid) VALUES (2324, 12);
INSERT INTO userrole (userid, roleid) VALUES (2327, 8);
INSERT INTO userrole (userid, roleid) VALUES (2327, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2327, 6);
INSERT INTO userrole (userid, roleid) VALUES (2327, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2327, 9);
INSERT INTO userrole (userid, roleid) VALUES (2327, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2327, 7);
INSERT INTO userrole (userid, roleid) VALUES (2327, 12);
INSERT INTO userrole (userid, roleid) VALUES (2330, 8);
INSERT INTO userrole (userid, roleid) VALUES (2330, 9);
INSERT INTO userrole (userid, roleid) VALUES (2330, 7);
INSERT INTO userrole (userid, roleid) VALUES (2321, 14);
INSERT INTO userrole (userid, roleid) VALUES (2321, 7);
INSERT INTO userrole (userid, roleid) VALUES (2321, 12);
INSERT INTO userrole (userid, roleid) VALUES (2333, 9);
INSERT INTO userrole (userid, roleid) VALUES (2333, 7);
INSERT INTO userrole (userid, roleid) VALUES (2335, 9);
INSERT INTO userrole (userid, roleid) VALUES (2335, 7);
INSERT INTO userrole (userid, roleid) VALUES (2332, 9);
INSERT INTO userrole (userid, roleid) VALUES (2332, 7);
INSERT INTO userrole (userid, roleid) VALUES (2331, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2331, 9);
INSERT INTO userrole (userid, roleid) VALUES (2331, 7);
INSERT INTO userrole (userid, roleid) VALUES (2290, 8);
INSERT INTO userrole (userid, roleid) VALUES (2290, 7);
INSERT INTO userrole (userid, roleid) VALUES (2340, 9);
INSERT INTO userrole (userid, roleid) VALUES (2340, 7);
INSERT INTO userrole (userid, roleid) VALUES (2338, 8);
INSERT INTO userrole (userid, roleid) VALUES (2338, 7);
INSERT INTO userrole (userid, roleid) VALUES (2342, 8);
INSERT INTO userrole (userid, roleid) VALUES (2342, 7);
INSERT INTO userrole (userid, roleid) VALUES (2344, 8);
INSERT INTO userrole (userid, roleid) VALUES (2344, 7);
INSERT INTO userrole (userid, roleid) VALUES (2233, 8);
INSERT INTO userrole (userid, roleid) VALUES (2233, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2233, 6);
INSERT INTO userrole (userid, roleid) VALUES (2233, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2233, 9);
INSERT INTO userrole (userid, roleid) VALUES (2233, 10);
INSERT INTO userrole (userid, roleid) VALUES (2233, 7);
INSERT INTO userrole (userid, roleid) VALUES (2233, 12);
INSERT INTO userrole (userid, roleid) VALUES (2347, 8);
INSERT INTO userrole (userid, roleid) VALUES (2347, 7);
INSERT INTO userrole (userid, roleid) VALUES (187, 12);
INSERT INTO userrole (userid, roleid) VALUES (244, 12);
INSERT INTO userrole (userid, roleid) VALUES (165, 12);
INSERT INTO userrole (userid, roleid) VALUES (16, 12);
INSERT INTO userrole (userid, roleid) VALUES (121, 12);
INSERT INTO userrole (userid, roleid) VALUES (201, 12);
INSERT INTO userrole (userid, roleid) VALUES (143, 12);
INSERT INTO userrole (userid, roleid) VALUES (2248, 12);
INSERT INTO userrole (userid, roleid) VALUES (243, 12);
INSERT INTO userrole (userid, roleid) VALUES (174, 12);
INSERT INTO userrole (userid, roleid) VALUES (99, 12);
INSERT INTO userrole (userid, roleid) VALUES (166, 12);
INSERT INTO userrole (userid, roleid) VALUES (159, 12);
INSERT INTO userrole (userid, roleid) VALUES (122, 12);
INSERT INTO userrole (userid, roleid) VALUES (245, 12);
INSERT INTO userrole (userid, roleid) VALUES (125, 12);
INSERT INTO userrole (userid, roleid) VALUES (170, 12);
INSERT INTO userrole (userid, roleid) VALUES (248, 12);
INSERT INTO userrole (userid, roleid) VALUES (195, 12);
INSERT INTO userrole (userid, roleid) VALUES (247, 12);
INSERT INTO userrole (userid, roleid) VALUES (2231, 12);
INSERT INTO userrole (userid, roleid) VALUES (146, 12);
INSERT INTO userrole (userid, roleid) VALUES (171, 12);
INSERT INTO userrole (userid, roleid) VALUES (124, 12);
INSERT INTO userrole (userid, roleid) VALUES (246, 12);
INSERT INTO userrole (userid, roleid) VALUES (123, 12);
INSERT INTO userrole (userid, roleid) VALUES (190, 12);
INSERT INTO userrole (userid, roleid) VALUES (102, 12);
INSERT INTO userrole (userid, roleid) VALUES (129, 12);
INSERT INTO userrole (userid, roleid) VALUES (2218, 12);
INSERT INTO userrole (userid, roleid) VALUES (167, 12);
INSERT INTO userrole (userid, roleid) VALUES (101, 12);
INSERT INTO userrole (userid, roleid) VALUES (2205, 12);
INSERT INTO userrole (userid, roleid) VALUES (2238, 12);
INSERT INTO userrole (userid, roleid) VALUES (54, 12);
INSERT INTO userrole (userid, roleid) VALUES (25, 12);
INSERT INTO userrole (userid, roleid) VALUES (142, 12);
INSERT INTO userrole (userid, roleid) VALUES (30, 12);
INSERT INTO userrole (userid, roleid) VALUES (93, 12);
INSERT INTO userrole (userid, roleid) VALUES (204, 12);
INSERT INTO userrole (userid, roleid) VALUES (17, 12);
INSERT INTO userrole (userid, roleid) VALUES (173, 12);
INSERT INTO userrole (userid, roleid) VALUES (188, 12);
INSERT INTO userrole (userid, roleid) VALUES (141, 12);
INSERT INTO userrole (userid, roleid) VALUES (139, 12);
INSERT INTO userrole (userid, roleid) VALUES (138, 12);
INSERT INTO userrole (userid, roleid) VALUES (175, 12);
INSERT INTO userrole (userid, roleid) VALUES (75, 12);
INSERT INTO userrole (userid, roleid) VALUES (3013, 12);
INSERT INTO userrole (userid, roleid) VALUES (2179, 12);
INSERT INTO userrole (userid, roleid) VALUES (2268, 12);
INSERT INTO userrole (userid, roleid) VALUES (97, 12);
INSERT INTO userrole (userid, roleid) VALUES (200, 12);
INSERT INTO userrole (userid, roleid) VALUES (3012, 12);
INSERT INTO userrole (userid, roleid) VALUES (70, 12);
INSERT INTO userrole (userid, roleid) VALUES (202, 12);
INSERT INTO userrole (userid, roleid) VALUES (2219, 12);
INSERT INTO userrole (userid, roleid) VALUES (172, 12);
INSERT INTO userrole (userid, roleid) VALUES (194, 12);
INSERT INTO userrole (userid, roleid) VALUES (179, 12);
INSERT INTO userrole (userid, roleid) VALUES (115, 12);
INSERT INTO userrole (userid, roleid) VALUES (2214, 12);
INSERT INTO userrole (userid, roleid) VALUES (137, 12);
INSERT INTO userrole (userid, roleid) VALUES (20, 12);
INSERT INTO userrole (userid, roleid) VALUES (192, 12);
INSERT INTO userrole (userid, roleid) VALUES (177, 12);
INSERT INTO userrole (userid, roleid) VALUES (2252, 12);
INSERT INTO userrole (userid, roleid) VALUES (193, 12);
INSERT INTO userrole (userid, roleid) VALUES (81, 12);
INSERT INTO userrole (userid, roleid) VALUES (119, 12);
INSERT INTO userrole (userid, roleid) VALUES (203, 12);
INSERT INTO userrole (userid, roleid) VALUES (2237, 12);
INSERT INTO userrole (userid, roleid) VALUES (13, 12);
INSERT INTO userrole (userid, roleid) VALUES (2217, 12);
INSERT INTO userrole (userid, roleid) VALUES (80, 12);
INSERT INTO userrole (userid, roleid) VALUES (2183, 12);
INSERT INTO userrole (userid, roleid) VALUES (3011, 12);
INSERT INTO userrole (userid, roleid) VALUES (69, 12);
INSERT INTO userrole (userid, roleid) VALUES (3007, 12);
INSERT INTO userrole (userid, roleid) VALUES (2216, 12);
INSERT INTO userrole (userid, roleid) VALUES (238, 12);
INSERT INTO userrole (userid, roleid) VALUES (3003, 12);
INSERT INTO userrole (userid, roleid) VALUES (242, 12);
INSERT INTO userrole (userid, roleid) VALUES (3008, 12);
INSERT INTO userrole (userid, roleid) VALUES (3009, 12);
INSERT INTO userrole (userid, roleid) VALUES (191, 12);
INSERT INTO userrole (userid, roleid) VALUES (2236, 12);
INSERT INTO userrole (userid, roleid) VALUES (3005, 12);
INSERT INTO userrole (userid, roleid) VALUES (117, 12);
INSERT INTO userrole (userid, roleid) VALUES (198, 12);
INSERT INTO userrole (userid, roleid) VALUES (147, 12);
INSERT INTO userrole (userid, roleid) VALUES (45, 12);
INSERT INTO userrole (userid, roleid) VALUES (199, 12);
INSERT INTO userrole (userid, roleid) VALUES (2213, 12);
INSERT INTO userrole (userid, roleid) VALUES (68, 12);
INSERT INTO userrole (userid, roleid) VALUES (3004, 12);
INSERT INTO userrole (userid, roleid) VALUES (3006, 12);
INSERT INTO userrole (userid, roleid) VALUES (73, 12);
INSERT INTO userrole (userid, roleid) VALUES (153, 12);
INSERT INTO userrole (userid, roleid) VALUES (72, 12);
INSERT INTO userrole (userid, roleid) VALUES (3010, 12);
INSERT INTO userrole (userid, roleid) VALUES (2215, 12);
INSERT INTO userrole (userid, roleid) VALUES (118, 12);
INSERT INTO userrole (userid, roleid) VALUES (181, 12);
INSERT INTO userrole (userid, roleid) VALUES (2220, 12);
INSERT INTO userrole (userid, roleid) VALUES (66, 12);
INSERT INTO userrole (userid, roleid) VALUES (3014, 12);
INSERT INTO userrole (userid, roleid) VALUES (88, 12);
INSERT INTO userrole (userid, roleid) VALUES (183, 12);
INSERT INTO userrole (userid, roleid) VALUES (161, 12);
INSERT INTO userrole (userid, roleid) VALUES (87, 12);
INSERT INTO userrole (userid, roleid) VALUES (2222, 12);
INSERT INTO userrole (userid, roleid) VALUES (64, 12);
INSERT INTO userrole (userid, roleid) VALUES (67, 12);
INSERT INTO userrole (userid, roleid) VALUES (3001, 12);
INSERT INTO userrole (userid, roleid) VALUES (2228, 12);
INSERT INTO userrole (userid, roleid) VALUES (3015, 12);
INSERT INTO userrole (userid, roleid) VALUES (2221, 12);
INSERT INTO userrole (userid, roleid) VALUES (61, 12);
INSERT INTO userrole (userid, roleid) VALUES (210, 12);
INSERT INTO userrole (userid, roleid) VALUES (105, 12);
INSERT INTO userrole (userid, roleid) VALUES (82, 12);
INSERT INTO userrole (userid, roleid) VALUES (180, 12);
INSERT INTO userrole (userid, roleid) VALUES (212, 12);
INSERT INTO userrole (userid, roleid) VALUES (21, 12);
INSERT INTO userrole (userid, roleid) VALUES (3000, 12);
INSERT INTO userrole (userid, roleid) VALUES (227, 12);
INSERT INTO userrole (userid, roleid) VALUES (207, 12);
INSERT INTO userrole (userid, roleid) VALUES (229, 12);
INSERT INTO userrole (userid, roleid) VALUES (65, 12);
INSERT INTO userrole (userid, roleid) VALUES (186, 12);
INSERT INTO userrole (userid, roleid) VALUES (42, 12);
INSERT INTO userrole (userid, roleid) VALUES (3002, 12);
INSERT INTO userrole (userid, roleid) VALUES (86, 12);
INSERT INTO userrole (userid, roleid) VALUES (26, 12);
INSERT INTO userrole (userid, roleid) VALUES (184, 12);
INSERT INTO userrole (userid, roleid) VALUES (2259, 12);
INSERT INTO userrole (userid, roleid) VALUES (107, 12);
INSERT INTO userrole (userid, roleid) VALUES (160, 12);
INSERT INTO userrole (userid, roleid) VALUES (185, 12);
INSERT INTO userrole (userid, roleid) VALUES (2212, 12);
INSERT INTO userrole (userid, roleid) VALUES (131, 12);
INSERT INTO userrole (userid, roleid) VALUES (178, 12);
INSERT INTO userrole (userid, roleid) VALUES (3016, 12);
INSERT INTO userrole (userid, roleid) VALUES (111, 12);
INSERT INTO userrole (userid, roleid) VALUES (2224, 12);
INSERT INTO userrole (userid, roleid) VALUES (130, 12);
INSERT INTO userrole (userid, roleid) VALUES (92, 12);
INSERT INTO userrole (userid, roleid) VALUES (2208, 12);
INSERT INTO userrole (userid, roleid) VALUES (113, 12);
INSERT INTO userrole (userid, roleid) VALUES (214, 12);
INSERT INTO userrole (userid, roleid) VALUES (236, 12);
INSERT INTO userrole (userid, roleid) VALUES (164, 12);
INSERT INTO userrole (userid, roleid) VALUES (2207, 12);
INSERT INTO userrole (userid, roleid) VALUES (2192, 12);
INSERT INTO userrole (userid, roleid) VALUES (162, 12);
INSERT INTO userrole (userid, roleid) VALUES (132, 12);
INSERT INTO userrole (userid, roleid) VALUES (57, 12);
INSERT INTO userrole (userid, roleid) VALUES (169, 12);
INSERT INTO userrole (userid, roleid) VALUES (3017, 12);
INSERT INTO userrole (userid, roleid) VALUES (135, 12);
INSERT INTO userrole (userid, roleid) VALUES (2209, 12);
INSERT INTO userrole (userid, roleid) VALUES (108, 12);
INSERT INTO userrole (userid, roleid) VALUES (58, 12);
INSERT INTO userrole (userid, roleid) VALUES (2226, 12);
INSERT INTO userrole (userid, roleid) VALUES (127, 12);
INSERT INTO userrole (userid, roleid) VALUES (206, 12);
INSERT INTO userrole (userid, roleid) VALUES (189, 12);
INSERT INTO userrole (userid, roleid) VALUES (148, 12);
INSERT INTO userrole (userid, roleid) VALUES (133, 12);
INSERT INTO userrole (userid, roleid) VALUES (158, 12);
INSERT INTO userrole (userid, roleid) VALUES (91, 12);
INSERT INTO userrole (userid, roleid) VALUES (196, 12);
INSERT INTO userrole (userid, roleid) VALUES (85, 12);
INSERT INTO userrole (userid, roleid) VALUES (136, 12);
INSERT INTO userrole (userid, roleid) VALUES (55, 12);
INSERT INTO userrole (userid, roleid) VALUES (231, 12);
INSERT INTO userrole (userid, roleid) VALUES (2240, 12);
INSERT INTO userrole (userid, roleid) VALUES (2229, 12);
INSERT INTO userrole (userid, roleid) VALUES (2230, 12);
INSERT INTO userrole (userid, roleid) VALUES (237, 12);
INSERT INTO userrole (userid, roleid) VALUES (128, 12);
INSERT INTO userrole (userid, roleid) VALUES (209, 12);
INSERT INTO userrole (userid, roleid) VALUES (2189, 12);
INSERT INTO userrole (userid, roleid) VALUES (2241, 12);
INSERT INTO userrole (userid, roleid) VALUES (258, 12);
INSERT INTO userrole (userid, roleid) VALUES (112, 12);
INSERT INTO userrole (userid, roleid) VALUES (3018, 12);
INSERT INTO userrole (userid, roleid) VALUES (250, 12);
INSERT INTO userrole (userid, roleid) VALUES (182, 12);
INSERT INTO userrole (userid, roleid) VALUES (235, 12);
INSERT INTO userrole (userid, roleid) VALUES (163, 12);
INSERT INTO userrole (userid, roleid) VALUES (126, 12);
INSERT INTO userrole (userid, roleid) VALUES (3019, 12);
INSERT INTO userrole (userid, roleid) VALUES (232, 12);
INSERT INTO userrole (userid, roleid) VALUES (134, 12);
INSERT INTO userrole (userid, roleid) VALUES (197, 12);
INSERT INTO userrole (userid, roleid) VALUES (89, 12);
INSERT INTO userrole (userid, roleid) VALUES (110, 12);
INSERT INTO userrole (userid, roleid) VALUES (256, 12);
INSERT INTO userrole (userid, roleid) VALUES (249, 12);
INSERT INTO userrole (userid, roleid) VALUES (233, 12);
INSERT INTO userrole (userid, roleid) VALUES (2291, 12);
INSERT INTO userrole (userid, roleid) VALUES (2255, 12);
INSERT INTO userrole (userid, roleid) VALUES (2298, 12);
INSERT INTO userrole (userid, roleid) VALUES (2295, 12);
INSERT INTO userrole (userid, roleid) VALUES (2180, 12);
INSERT INTO userrole (userid, roleid) VALUES (2223, 12);
INSERT INTO userrole (userid, roleid) VALUES (2318, 12);
INSERT INTO userrole (userid, roleid) VALUES (2211, 12);
INSERT INTO userrole (userid, roleid) VALUES (2330, 12);
INSERT INTO userrole (userid, roleid) VALUES (2333, 12);
INSERT INTO userrole (userid, roleid) VALUES (2335, 12);
INSERT INTO userrole (userid, roleid) VALUES (2332, 12);
INSERT INTO userrole (userid, roleid) VALUES (2331, 12);
INSERT INTO userrole (userid, roleid) VALUES (2340, 12);
INSERT INTO userrole (userid, roleid) VALUES (2349, 8);
INSERT INTO userrole (userid, roleid) VALUES (2349, 11);
INSERT INTO userrole (userid, roleid) VALUES (2349, 9);
INSERT INTO userrole (userid, roleid) VALUES (2349, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2349, 10);
INSERT INTO userrole (userid, roleid) VALUES (2349, 7);
INSERT INTO userrole (userid, roleid) VALUES (2349, 12);
INSERT INTO userrole (userid, roleid) VALUES (2350, 7);
INSERT INTO userrole (userid, roleid) VALUES (2350, 8);
INSERT INTO userrole (userid, roleid) VALUES (2304, 7);
INSERT INTO userrole (userid, roleid) VALUES (2304, 8);
INSERT INTO userrole (userid, roleid) VALUES (2304, 9);
INSERT INTO userrole (userid, roleid) VALUES (2304, 12);
INSERT INTO userrole (userid, roleid) VALUES (2304, 2247);
INSERT INTO userrole (userid, roleid) VALUES (74, 6);
INSERT INTO userrole (userid, roleid) VALUES (74, 7);
INSERT INTO userrole (userid, roleid) VALUES (74, 8);
INSERT INTO userrole (userid, roleid) VALUES (74, 9);
INSERT INTO userrole (userid, roleid) VALUES (74, 10);
INSERT INTO userrole (userid, roleid) VALUES (74, 12);
INSERT INTO userrole (userid, roleid) VALUES (74, 14);
INSERT INTO userrole (userid, roleid) VALUES (63, 12);
INSERT INTO userrole (userid, roleid) VALUES (63, 14);
INSERT INTO userrole (userid, roleid) VALUES (63, 6);
INSERT INTO userrole (userid, roleid) VALUES (63, 7);
INSERT INTO userrole (userid, roleid) VALUES (63, 8);
INSERT INTO userrole (userid, roleid) VALUES (63, 10);
INSERT INTO userrole (userid, roleid) VALUES (63, 2247);
INSERT INTO userrole (userid, roleid) VALUES (63, 9);
INSERT INTO userrole (userid, roleid) VALUES (2354, 12);
INSERT INTO userrole (userid, roleid) VALUES (2354, 6);
INSERT INTO userrole (userid, roleid) VALUES (2354, 7);
INSERT INTO userrole (userid, roleid) VALUES (2354, 8);
INSERT INTO userrole (userid, roleid) VALUES (2354, 2247);
INSERT INTO userrole (userid, roleid) VALUES (2354, 2257);
INSERT INTO userrole (userid, roleid) VALUES (2354, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2354, 9);
INSERT INTO userrole (userid, roleid) VALUES (2355, 7);
INSERT INTO userrole (userid, roleid) VALUES (2355, 9);
INSERT INTO userrole (userid, roleid) VALUES (2356, 7);
INSERT INTO userrole (userid, roleid) VALUES (2356, 9);
INSERT INTO userrole (userid, roleid) VALUES (2357, 7);
INSERT INTO userrole (userid, roleid) VALUES (2357, 8);
INSERT INTO userrole (userid, roleid) VALUES (2203, 18);
INSERT INTO userrole (userid, roleid) VALUES (2193, 12);
INSERT INTO userrole (userid, roleid) VALUES (2193, 14);
INSERT INTO userrole (userid, roleid) VALUES (2193, 18);
INSERT INTO userrole (userid, roleid) VALUES (2186, 18);
INSERT INTO userrole (userid, roleid) VALUES (2186, 2263);
INSERT INTO userrole (userid, roleid) VALUES (2186, 2264);
INSERT INTO userrole (userid, roleid) VALUES (228, 12);
INSERT INTO userrole (userid, roleid) VALUES (228, 14);
INSERT INTO userrole (userid, roleid) VALUES (228, 6);
INSERT INTO userrole (userid, roleid) VALUES (228, 7);
INSERT INTO userrole (userid, roleid) VALUES (228, 8);
INSERT INTO userrole (userid, roleid) VALUES (228, 10);
INSERT INTO userrole (userid, roleid) VALUES (228, 2247);
INSERT INTO userrole (userid, roleid) VALUES (228, 9);
INSERT INTO userrole (userid, roleid) VALUES (15, 8);
INSERT INTO userrole (userid, roleid) VALUES (15, 11);
INSERT INTO userrole (userid, roleid) VALUES (15, 2264);
INSERT INTO userrole (userid, roleid) VALUES (15, 2247);
INSERT INTO userrole (userid, roleid) VALUES (15, 6);
INSERT INTO userrole (userid, roleid) VALUES (15, 2257);
INSERT INTO userrole (userid, roleid) VALUES (15, 14);
INSERT INTO userrole (userid, roleid) VALUES (15, 9);
INSERT INTO userrole (userid, roleid) VALUES (15, 2263);
INSERT INTO userrole (userid, roleid) VALUES (15, 10);
INSERT INTO userrole (userid, roleid) VALUES (15, 7);
INSERT INTO userrole (userid, roleid) VALUES (15, 12);
INSERT INTO userrole (userid, roleid) VALUES (2361, 7);
INSERT INTO userrole (userid, roleid) VALUES (2361, 9);
INSERT INTO userrole (userid, roleid) VALUES (2359, 7);
INSERT INTO userrole (userid, roleid) VALUES (2359, 8);
INSERT INTO userrole (userid, roleid) VALUES (216, 7);
INSERT INTO userrole (userid, roleid) VALUES (216, 9);
INSERT INTO userrole (userid, roleid) VALUES (216, 12);
INSERT INTO userrole (userid, roleid) VALUES (2363, 7);
INSERT INTO userrole (userid, roleid) VALUES (2363, 9);
INSERT INTO userrole (userid, roleid) VALUES (3, 2);


--
-- TOC entry 1544 (class 0 OID 8609159)
-- Dependencies: 1200
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2232, 'Victoria', 'cc03e747a6afbbcbf8be7668acfebee5', 'Victoria', 'Marshall', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-18 16:40:50.145', 'Sample tracking project for DLS', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2218, 'kathrynascott', 'cc03e747a6afbbcbf8be7668acfebee5', 'kathryn', 'scott', 'jon@strubi.ox.ac.uk', '1234', '', '2007-05-15 13:08:18.678', 'I am a new member of STRUBI working for David Stammers.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (6, 'mayo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chris', 'Mayo', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-04-27 16:12:04', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2219, 'barclay', 'cc03e747a6afbbcbf8be7668acfebee5', 'Neil', 'Barclay', 'jon@strubi.ox.ac.uk', '1234', '', '2007-05-16 10:53:34.95', 'As group head', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2222, 'Michael', 'cc03e747a6afbbcbf8be7668acfebee5', 'Michael', 'Middleton', 'jon@strubi.ox.ac.uk', '1234', '', '2007-05-25 09:48:48.932', 'to gain access for website update', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (8, 'oppf', 'cc03e747a6afbbcbf8be7668acfebee5', 'OPPF', 'test', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-11 15:07:07', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (9, 'lester', 'cc03e747a6afbbcbf8be7668acfebee5', 'Lester', 'Carter', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-16 12:02:22', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (10, 'rebecca', 'cc03e747a6afbbcbf8be7668acfebee5', 'Rebecca', 'Hamer', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-17 15:16:03', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (209, 'chrismcdevitt', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christopher', 'McDevitt', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-05 10:21:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (11, 'mohammad', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mohammad', 'Bahar', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (12, 'jonathan', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jonathan', 'Grimes', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (13, 'daniel.webster', 'cc03e747a6afbbcbf8be7668acfebee5', 'Daniel', 'Webster', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (14, 'dave', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dave', 'Stuart', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (16, 'david.mason', 'cc03e747a6afbbcbf8be7668acfebee5', 'David', 'Mason', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (215, 'xiaoyun', 'cc03e747a6afbbcbf8be7668acfebee5', 'Xiaoyun', 'Ji', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-11-07 13:55:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (24, 'ray', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ray', 'Owens', 'jon@strubi.ox.ac.uk', '1234', '', '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (17, 'william.cookson', 'cc03e747a6afbbcbf8be7668acfebee5', 'Bill', 'Cookson', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (18, 'nathan', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nathan', 'Zaccai', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (19, 'liz', 'cc03e747a6afbbcbf8be7668acfebee5', 'Liz', 'Fry', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (20, 'fogg', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mark', 'Fogg', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (21, 'jonathan.gleadle', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jonathan', 'Gleadle', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (22, 'rene', 'cc03e747a6afbbcbf8be7668acfebee5', 'Rene', 'Assenberg', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (25, 'simon.davis', 'cc03e747a6afbbcbf8be7668acfebee5', 'Simon', 'Davis', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (26, 'roy.bicknell', 'cc03e747a6afbbcbf8be7668acfebee5', 'Roy', 'Bicknell', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (144, 'samantha', 'cc03e747a6afbbcbf8be7668acfebee5', 'Samantha', 'Holmes', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-26 15:34:01', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (28, 'yvonne', 'cc03e747a6afbbcbf8be7668acfebee5', 'Yvonne', 'Jones', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (29, 'geoff', 'cc03e747a6afbbcbf8be7668acfebee5', 'Geoff', 'Sutton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (30, 'nigel.saunders', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nigel', 'Saunders', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (31, 'daves', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dave', 'Stammers', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (32, 'chrism', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christoph', 'Meier', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (33, 'louise', 'cc03e747a6afbbcbf8be7668acfebee5', 'Louise', 'Bird', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (35, 'sarah', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sarah', 'Sainsbury', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (36, 'alderton', 'cc03e747a6afbbcbf8be7668acfebee5', 'David', 'Alderton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (37, 'nick', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nick', 'Berrow', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (39, 'walter', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tom', 'Walter', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (40, 'anil', 'cc03e747a6afbbcbf8be7668acfebee5', 'Anil', 'Verma', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (41, 'jack', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jon', 'Marles-Wright', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (42, 'polly.roy', 'cc03e747a6afbbcbf8be7668acfebee5', 'Polly', 'Roy', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (43, 'vanboxel', 'cc03e747a6afbbcbf8be7668acfebee5', 'Gijs', 'van-Boxel', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-21 15:45:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (45, 'julie', 'cc03e747a6afbbcbf8be7668acfebee5', 'Julie', 'Wilson', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-06-23 15:07:09', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (242, 'hercule', 'cc03e747a6afbbcbf8be7668acfebee5', 'Hercules', 'Student', 'jon@strubi.ox.ac.uk', '1234', '', '2006-10-09 09:30:25', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (239, 'reg', 'cc03e747a6afbbcbf8be7668acfebee5', 'Reg', 'Myers', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-09-06 09:03:29', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (128, 'adrian.lobito', 'cc03e747a6afbbcbf8be7668acfebee5', 'Adrian', 'Lobito', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-05 14:00:38', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (54, 'dhatherl', 'cc03e747a6afbbcbf8be7668acfebee5', 'Debbie', 'Hatherley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (55, 'eevans', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ed', 'Evans', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (56, 'phil', 'cc03e747a6afbbcbf8be7668acfebee5', 'Phil', 'Chamberlain', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (57, 'jwyer', 'cc03e747a6afbbcbf8be7668acfebee5', 'Lars', 'Fugger', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (58, 'jason', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jason', '', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (59, 'heather', 'cc03e747a6afbbcbf8be7668acfebee5', 'Heather', '', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (61, 'fiona.kimberley', 'cc03e747a6afbbcbf8be7668acfebee5', 'Fiona', 'Kimberley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (62, 'gilbert', 'cc03e747a6afbbcbf8be7668acfebee5', 'Robert', 'Gilbert', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (64, 'paula', 'cc03e747a6afbbcbf8be7668acfebee5', 'Paula', 'Salgado', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (65, 'haren.arulanantham', 'cc03e747a6afbbcbf8be7668acfebee5', 'Haren', 'Arulanantham', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (66, 'richard.welford', 'cc03e747a6afbbcbf8be7668acfebee5', 'Rich', 'Welford', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (67, 'amanda.ferguson', 'cc03e747a6afbbcbf8be7668acfebee5', 'Amanda', 'Ferguson', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (68, 'richard.shelley', 'cc03e747a6afbbcbf8be7668acfebee5', 'Richard', 'Shelley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (69, 'matthew.caines', 'cc03e747a6afbbcbf8be7668acfebee5', 'Matthew', 'Caines', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (70, 'nadia.kershaw', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nadia', 'Kershaw', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (71, 'chris.love', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chris', 'Love', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (72, 'alaoui', 'cc03e747a6afbbcbf8be7668acfebee5', 'Abdou', '', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (73, 'c.mcvey', 'cc03e747a6afbbcbf8be7668acfebee5', 'Colin', 'McVey', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (74, 'bal', 'cc03e747a6afbbcbf8be7668acfebee5', 'Bal', 'Dhaliwal', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (75, 'stephan.gadola', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephan', 'Gadola', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2223, 'charlotte', 'cc03e747a6afbbcbf8be7668acfebee5', 'Charlotte', 'Coles', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-05 11:36:58.972', 'Working in Strubi', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (77, 'maria', 'cc03e747a6afbbcbf8be7668acfebee5', 'Maria', 'Harkiolaki', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (78, 'kris', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kris', 'McLeslie', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (79, 'kate', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kate', 'Spanchak', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (80, 'perrakis', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tassos', 'Perrakis', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (81, 'jchen', 'cc03e747a6afbbcbf8be7668acfebee5', 'JiLi', 'Chen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (82, 'michael.mcdonough', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mike', 'McDonough', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (83, 'ross', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ross', 'Robinson', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (85, 'acowper', 'cc03e747a6afbbcbf8be7668acfebee5', 'Alison', 'Cowper', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (86, 'munish.puri', 'cc03e747a6afbbcbf8be7668acfebee5', 'Munish', 'Puri', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (87, 'ggillesp', 'cc03e747a6afbbcbf8be7668acfebee5', 'Gerry', 'Gillespie', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (88, 'murray', 'cc03e747a6afbbcbf8be7668acfebee5', 'James', 'Murray', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (89, 'mark.sleeman', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mark', 'Sleeman', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (90, 'guillaume', 'cc03e747a6afbbcbf8be7668acfebee5', 'Guillaume', 'Stewart-Jones', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (91, 'm.richards', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mark', 'Richards', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (92, 'vivian', 'cc03e747a6afbbcbf8be7668acfebee5', 'Vivian', 'Saridakis', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (93, 'cunliffe', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sharon', 'Cunliffe', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (94, 'hon', 'cc03e747a6afbbcbf8be7668acfebee5', 'Wai-Ching', 'Hon', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (95, 'karin', 'cc03e747a6afbbcbf8be7668acfebee5', 'Karin', 'Anduleit', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (97, 'veerle.foulon', 'cc03e747a6afbbcbf8be7668acfebee5', 'Veerle', 'Foulon', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (98, 'amit', 'cc03e747a6afbbcbf8be7668acfebee5', 'Amit', 'Sharma', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (99, 'xiaodong.xu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Xiaodong', 'Xu', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (100, 'osamu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Osamu', 'Matsumoto', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (101, 'mwahl', 'cc03e747a6afbbcbf8be7668acfebee5', 'Markus', 'Wahl', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (102, 'demin.li', 'cc03e747a6afbbcbf8be7668acfebee5', 'Demin', 'Li', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (103, 'kamel', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kamel', 'El Omari', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (104, 'gregor', 'cc03e747a6afbbcbf8be7668acfebee5', 'Gregor', 'Hofmann', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (105, 'dhaval.sangani', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dhaval', 'Sangani', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (106, 'charlie', 'cc03e747a6afbbcbf8be7668acfebee5', 'Charlie', 'Nichols', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (107, 'tao.deng', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tao', 'Deng', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (108, 'louise.hilton', 'cc03e747a6afbbcbf8be7668acfebee5', 'Louise', 'Hilton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (110, 'josa.wehrfritz', 'cc03e747a6afbbcbf8be7668acfebee5', 'Josa', 'Wehrfritz', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (111, 'axel', 'cc03e747a6afbbcbf8be7668acfebee5', 'Axel', 'Muller', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (112, 'may.mok', 'cc03e747a6afbbcbf8be7668acfebee5', 'May', 'Mok', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (113, 'simon.carr', 'cc03e747a6afbbcbf8be7668acfebee5', 'Simon', 'Carr', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (114, 'masayo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Masayo', 'Kotaka', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (115, 'pojchon', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mint', 'Pojchong', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (116, 'anna', 'cc03e747a6afbbcbf8be7668acfebee5', 'Anna', 'Stamp', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (117, 'enrique', 'cc03e747a6afbbcbf8be7668acfebee5', 'Enrique', 'Rudino', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (118, 'yzhao', 'cc03e747a6afbbcbf8be7668acfebee5', 'Yuguang', 'Zhao', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (119, 'alinden', 'cc03e747a6afbbcbf8be7668acfebee5', 'Anni', 'Linden', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (120, 'stephen.moran', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephen', 'Moran', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (121, 'chao.yu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chao', 'Yu', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (122, 'natalie.ross-smith', 'cc03e747a6afbbcbf8be7668acfebee5', 'Natalie', 'Ross-Smith', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (123, 's.cooray', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sam', 'Cooray', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (125, 'helena.wright', 'cc03e747a6afbbcbf8be7668acfebee5', 'Helena', 'Wright', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (126, 'mieke.sniekers', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mieke', 'Sniekers', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (127, 'tim.searls', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tim', 'Searls', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (129, 'asharma', 'cc03e747a6afbbcbf8be7668acfebee5', 'Asharma', '', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-05 14:03:50', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (130, 'dawn.shepherd', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dawn', 'Shepherd', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-05 14:04:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (132, 'nikolai.lissin', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nikolai', 'Lissin', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-05 14:04:36', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (134, 'elenimumtsidu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Eleni', 'Mumtsidu', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:12:47', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (135, 'larissatextor', 'cc03e747a6afbbcbf8be7668acfebee5', 'Larissa', 'Textor', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:13:00', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (136, 'matthewgroves', 'cc03e747a6afbbcbf8be7668acfebee5', 'Matthew', 'Groves', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:13:06', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (137, 'simonemueller', 'cc03e747a6afbbcbf8be7668acfebee5', 'Simon', 'Mueller', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:13:15', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (138, 'peijianzou', 'cc03e747a6afbbcbf8be7668acfebee5', 'Peijian', 'Zou', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:13:23', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (139, 'simonholton', 'cc03e747a6afbbcbf8be7668acfebee5', 'Simon', 'Holton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 11:13:29', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (140, 'miko', 'cc03e747a6afbbcbf8be7668acfebee5', 'Michael', 'Koch', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-15 15:53:53', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (141, 'gcp', 'cc03e747a6afbbcbf8be7668acfebee5', 'Guido', 'Paesen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-21 11:03:48', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (142, 's.mcphie', 'cc03e747a6afbbcbf8be7668acfebee5', 'Svetla', 'McPhie', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-21 12:53:19', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (143, 'danica', 'cc03e747a6afbbcbf8be7668acfebee5', 'Danica', 'Butler', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-21 14:23:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (146, 'paul', 'cc03e747a6afbbcbf8be7668acfebee5', 'Paul', 'Young', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-08-06 15:30:44', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (147, 'joel', 'cc03e747a6afbbcbf8be7668acfebee5', 'Joel', 'Fillon', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-08-06 15:31:14', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (150, 'damian', 'cc03e747a6afbbcbf8be7668acfebee5', 'Damian', 'Brennan', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-10-22 15:17:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (151, 'kinfai', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kin-Fai', 'Au', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-11-01 16:30:44', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (153, 'emily.flashman', 'cc03e747a6afbbcbf8be7668acfebee5', 'Emily', 'Flashman', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-11-26 12:57:58', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (155, 'joe', 'cc03e747a6afbbcbf8be7668acfebee5', 'Joe', 'Cockburn', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-01-11 10:24:51', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (156, 'susan', 'cc03e747a6afbbcbf8be7668acfebee5', 'Susan', 'Daenke', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-01-19 13:06:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (158, 't.tuthill', 'cc03e747a6afbbcbf8be7668acfebee5', 'Toby', 'Tuthill', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-02-01 13:44:24', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (159, 'sam8', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sam', 'surname', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-02-01 17:53:16', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (160, 'cghw01', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christopher', 'Walker', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-03-04 13:20:52', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (161, 'ataylor', 'cc03e747a6afbbcbf8be7668acfebee5', 'Alan', 'Taylor', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-03-04 13:21:12', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (162, 'jean-baptiste', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jean-Baptiste', 'Reiser', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-03-04 13:24:17', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (163, 'Bern', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andreas', 'Zurbriggen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-03-09 10:17:47', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (164, 'brian.sutton', 'cc03e747a6afbbcbf8be7668acfebee5', 'Brian', 'Sutton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-03-23 14:05:04', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (165, 'janet.fennelly', 'cc03e747a6afbbcbf8be7668acfebee5', 'jan', 'fennelly', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-08 15:22:58', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (166, 'babu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sudhir', 'Babu Pothineni', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-12 09:30:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (167, 'mareike', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mareike', 'Kurz', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-12 09:30:57', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (168, 'toyo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Toyoyuki', 'Ose', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-12 09:31:51', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (169, 'tadeusz', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tadeusz', 'Skarzynski', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-21 11:45:10', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (170, 'kem', 'cc03e747a6afbbcbf8be7668acfebee5', 'Katherine', 'McAuley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-25 14:58:18', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (171, 'tlyms', 'cc03e747a6afbbcbf8be7668acfebee5', 'Thomas', 'Sorensen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-26 09:55:26', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (172, 'kmcluskey', 'cc03e747a6afbbcbf8be7668acfebee5', 'Karen', 'McLuskey', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-26 13:38:24', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (173, 'acosand', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andrew', 'Cosand', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-26 18:33:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (174, 'zhzhang', 'cc03e747a6afbbcbf8be7668acfebee5', 'Zhihong', 'Zhang', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-27 11:51:55', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (175, 'dwatts', 'cc03e747a6afbbcbf8be7668acfebee5', 'David', 'Watts', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-04-29 11:06:22', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (176, 'endre', 'cc03e747a6afbbcbf8be7668acfebee5', 'Endre', '', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-06 12:09:44', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (177, 'tfs', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tony', 'Fordham-Skelton', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-09 13:43:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (178, 'Siegfried_Baesler', 'cc03e747a6afbbcbf8be7668acfebee5', 'Siegfried', 'Baesler', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-11 16:32:58', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (179, 'njb', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nicola', 'Beresford', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-11 16:33:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (180, 'ralf', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ralf', 'Flaig', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-12 13:16:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (181, 'bramschierbeek', 'cc03e747a6afbbcbf8be7668acfebee5', 'Bram', 'Schierbeek', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-19 06:17:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (182, 'yong.yi', 'cc03e747a6afbbcbf8be7668acfebee5', 'Yong', 'Yi', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-24 16:45:17', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (183, 'ranmeged', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ran', 'Meged', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-24 23:32:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (184, 'lebbink', 'cc03e747a6afbbcbf8be7668acfebee5', 'Joyce', 'Lebbink', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-25 11:47:30', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (185, 'ganesh', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ganesh', 'Natrajan', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-25 13:33:15', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (186, 'AnjaWienecke', 'cc03e747a6afbbcbf8be7668acfebee5', 'Anja', 'Wienecke', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-25 13:47:52', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (187, 'pknipscheer', 'cc03e747a6afbbcbf8be7668acfebee5', 'Puck', 'Knipscheer', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-25 17:32:02', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (188, 'angelina', 'cc03e747a6afbbcbf8be7668acfebee5', 'Angelina', 'Huseinovic', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-26 12:14:13', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (189, 'Netvis', 'cc03e747a6afbbcbf8be7668acfebee5', 'Annet', 'Reumer', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-26 14:34:59', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (190, 'sgerwen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Suzan', 'van Gerwen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-26 15:07:02', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (192, 'Patrick', 'cc03e747a6afbbcbf8be7668acfebee5', 'Patrick', 'Celie', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-27 09:15:47', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (193, 'vangelis', 'cc03e747a6afbbcbf8be7668acfebee5', 'E', 'Christodoulou', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-27 11:25:51', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (194, 'diederick', 'cc03e747a6afbbcbf8be7668acfebee5', 'Diederick', 'de Vries', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-27 13:38:58', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (195, 'marouane', 'cc03e747a6afbbcbf8be7668acfebee5', 'Marouane', 'Ben Jelloul', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-27 13:39:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (196, 'chrisulens', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chris', 'Ulens', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-30 21:10:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (197, 'fgorrec', 'cc03e747a6afbbcbf8be7668acfebee5', 'Fabrice', 'Gorrec', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-05-31 17:28:07', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (198, 'loretta', 'cc03e747a6afbbcbf8be7668acfebee5', 'Frank', 'von Delft', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-06-06 14:37:15', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (199, 'bblattmann', 'cc03e747a6afbbcbf8be7668acfebee5', 'Beat', 'Blattmann', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-06-08 11:29:29', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (200, 'stemat', 'cc03e747a6afbbcbf8be7668acfebee5', 'Steve', 'Matthews', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-06-15 14:14:04', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (201, 'amaniqbal', 'cc03e747a6afbbcbf8be7668acfebee5', 'Aman', 'Iqbal', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-18 14:25:12', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (202, 'rzchowdhury', 'cc03e747a6afbbcbf8be7668acfebee5', 'Rasheduzzaman', 'Chowdhury', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-18 14:39:43', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (203, 'r.chowdhury', 'cc03e747a6afbbcbf8be7668acfebee5', 'Rasheduzzaman', 'Chowdhury', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-19 10:03:23', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (204, 'bharti.mackness', 'cc03e747a6afbbcbf8be7668acfebee5', 'Bharti', 'Mackness', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-19 12:30:29', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (205, 'kadlec', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jan', 'Kadlec', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-21 09:27:33', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (206, 'lisa.damato', 'cc03e747a6afbbcbf8be7668acfebee5', 'Lisa', 'DAmato', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-21 11:39:37', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (207, 'zameel', 'cc03e747a6afbbcbf8be7668acfebee5', 'Zameel', 'Cader', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-07-22 15:49:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (208, 'yuliya', 'cc03e747a6afbbcbf8be7668acfebee5', 'Yuliya', 'surname', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-09-06 11:53:08', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (210, 'despoala', 'cc03e747a6afbbcbf8be7668acfebee5', 'Alan', 'Desport', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-05 11:47:42', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (211, 'katya', 'cc03e747a6afbbcbf8be7668acfebee5', 'Katherine', 'Pilicheva', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-06 12:37:00', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (212, 'leanne.kelly', 'cc03e747a6afbbcbf8be7668acfebee5', 'Leanne', 'Kelly', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-07 12:54:37', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (213, 'cameron', 'cc03e747a6afbbcbf8be7668acfebee5', 'Cameron', 'Dunlop', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-17 16:18:58', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (214, 'tom', 'cc03e747a6afbbcbf8be7668acfebee5', 'Thomas', 'Bowden', 'jon@strubi.ox.ac.uk', '1234', NULL, '2005-10-18 11:22:29', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (226, 'stephen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephen', 'Fuller', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-01-19 14:07:51', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (227, 'andy.may', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andy', 'May', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-01-26 12:54:22', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (229, 'sbanerji', 'cc03e747a6afbbcbf8be7668acfebee5', 'Suneale', 'Banerji', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-02-09 16:35:12', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (231, 'bevprosser', 'cc03e747a6afbbcbf8be7668acfebee5', 'Beverley', 'Prosser', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-05-04 13:58:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (232, 'jazkemik', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jasmin', 'Mecinovic', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-06-19 13:58:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (233, 'branwen.hide', 'cc03e747a6afbbcbf8be7668acfebee5', 'Branwen', 'Hide', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-07-04 14:19:30', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (235, 'dennispoon', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dennis', 'Poon', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-07-11 14:19:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (236, 'chia-hua.ho', 'cc03e747a6afbbcbf8be7668acfebee5', 'Maggie', 'Chia-Hua', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-07-13 10:47:07', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (237, 'ashley', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ashley', 'Horsley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-08-22 15:34:11', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (238, 'VSauve', 'cc03e747a6afbbcbf8be7668acfebee5', 'Veronique', 'Sauve', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-08-22 15:34:28', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (241, 'jirving', 'cc03e747a6afbbcbf8be7668acfebee5', 'James', 'Irving', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-09-12 15:16:37', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2224, 'evalkov', 'cc03e747a6afbbcbf8be7668acfebee5', 'Eugene', 'Valkov', 'jon@strubi.ox.ac.uk', '1234', '02075943906', '2007-06-05 15:01:31.007', 'I''m interested in using the OPPF crysallisation facilities for screening conditions for crystallisation of a viral integrase and host factor complexes as part of my research at Imperial College London. I will contact the OPPF shortly with a view to arranging a visit to view the facilities.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (243, 'floss', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sara', 'Brown', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-10-17 11:32:42', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (244, 'george.orriss', 'cc03e747a6afbbcbf8be7668acfebee5', 'George', 'Orriss', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-10-30 14:28:13', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (245, 'ayhan.celik', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ayhan', 'Celik', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-10-31 13:33:23', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (246, 'wei.ge', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ge', 'Wei', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-11-02 13:11:39', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (247, 'thomas.gerken', 'cc03e747a6afbbcbf8be7668acfebee5', 'Thomas', 'Gerken', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-11-07 13:03:13', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (248, 'PennyHandford', 'cc03e747a6afbbcbf8be7668acfebee5', 'Penny', 'Handford', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-11-08 16:24:03', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (249, 'sbhattac', 'cc03e747a6afbbcbf8be7668acfebee5', 'Shoumo', 'Bhattacharya', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-11-08 16:24:10', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2226, 'pcherepanov', 'cc03e747a6afbbcbf8be7668acfebee5', 'Peter', 'Cherepanov', 'jon@strubi.ox.ac.uk', '1234', '02075943906', '2007-06-05 15:01:39.445', 'We are planning to use the OPPF''s protein crystallization screening facility. ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2228, 'Neil.Haig', 'cc03e747a6afbbcbf8be7668acfebee5', 'Neil', 'Haig', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-06 14:08:07.515', 'already vault registered', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2175, 'test', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'test', 'jon@strubi.ox.ac.uk', '1234', '', '2006-11-14 10:14:34.075', 'test', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2177, 'oppfweb', 'cc03e747a6afbbcbf8be7668acfebee5', 'oppf', 'web', 'jon@strubi.ox.ac.uk', '1234', '', '2006-11-23 09:54:38.72', 'to make the website', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (76, 'erika', 'cc03e747a6afbbcbf8be7668acfebee5', 'Erika', 'Mancini', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (133, 'vilmos', 'cc03e747a6afbbcbf8be7668acfebee5', 'Vilmos', 'Fulop', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-14 13:33:08', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (52, 'thil', 'cc03e747a6afbbcbf8be7668acfebee5', 'Thil', 'Batuwangala', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (49, 'jun', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jun', 'Dong', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-01 18:25:30', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2229, 'Kirsty Hewitson', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kirsty', 'Hewitson', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-11 15:06:03.879', 'crystallization.

already nautilus user - code 96 - TW', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2178, 'Ray', 'cc03e747a6afbbcbf8be7668acfebee5', 'Wenming', 'Ji', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-04 13:48:02.857', 'MS Test', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2179, 'john', 'cc03e747a6afbbcbf8be7668acfebee5', 'John', 'Flanagan', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-09 10:48:23.271', 'I am a member of Strubi and I will be designing targets for crystallization.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2230, 'chrisoc', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chris', 'O''Callaghan', 'jon@strubi.ox.ac.uk', '1234', '01865 287787', '2007-06-11 15:06:13.207', 'OPPF', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2180, 'LeaGroup', 'cc03e747a6afbbcbf8be7668acfebee5', 'Susan', 'Lea', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-09 14:07:48.099', 'Ray told me to so that we could use the mass spectroscopy!', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (124, 's.c.lee', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sarah', 'Lee', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (258, 'Shuo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Eric Shuo', 'Chen', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-01-10 14:02:07.52', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2181, 'marc', 'cc03e747a6afbbcbf8be7668acfebee5', 'Marc', 'Savitsky', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-16 11:15:47.289', 'n/a', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2182, 'P450', 'cc03e747a6afbbcbf8be7668acfebee5', 'Wenming', 'Ji', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-16 12:05:02.399', 'MS Test for peptide and protein', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (251, 'administrator', 'cc03e747a6afbbcbf8be7668acfebee5', 'PIMS', 'Admin', 'jon@strubi.ox.ac.uk', '1234', '', '2006-11-09 11:59:06.76', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2183, 'lady1289', 'cc03e747a6afbbcbf8be7668acfebee5', 'Martine', 'Bomb', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-17 15:31:41.399', 'I would like to use the crystallisation and mass spec facilities for some of my protein samples. ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2231, 'janetd', 'cc03e747a6afbbcbf8be7668acfebee5', 'Janet', 'Deane', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-13 09:42:12.416', 'I think I might have already been given a login but I have lost the details, sorry. I am sending some samples for mass spec tomorrow (13th) and haven''t been able to submit sample information.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2220, 'cmouli', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chandramouli', 'Chillakuri', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-05 11:38:36.44', 'I am working as a Research assistant in Prof.Helen Mardon''s group in Nuffield department of Obs and Gyn at the JR hospital. I want to use the facilities of crystallization and mass spectrometry to carry on the structural studies on the protein I am producing.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2186, 'ehtpxtest', 'cc03e747a6afbbcbf8be7668acfebee5', 'ehtpx', 'test', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-25 09:16:00.616', 'test user for ehtpx', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2204, 'user12', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:22:55.266', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (38, 'nahid', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nahid', 'Rahman-Huq', 'jon@strubi.ox.ac.uk', '1234', '', '2004-06-21 15:45:54', '', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2192, 'sacha_jensen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sacha', 'Jensen', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:23:13.813', 'At the moment, access to a mass spectrometry facility.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2190, 'mtg63', 'cc03e747a6afbbcbf8be7668acfebee5', 'Michael', 'Gleaves', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-31 13:57:12.089', 'test portal', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2185, 'graeme', 'cc03e747a6afbbcbf8be7668acfebee5', 'Graeme', 'Winter', 'jon@strubi.ox.ac.uk', '1234', '44 1925 603228', '2007-01-19 08:39:19.914', 'Umm.. want to have a go??!', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2205, 'idcgroup', 'cc03e747a6afbbcbf8be7668acfebee5', 'Amanda ', 'Pagett', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-22 08:49:55.335', 'Because our group would like to use your mass spec facility', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2191, 'mickyt', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mihaela', 'Dediu', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-05 11:19:28.963', 'start working on e-htpx', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2184, 'CNave', 'cc03e747a6afbbcbf8be7668acfebee5', 'Colin', 'Nave', 'jon@strubi.ox.ac.uk', '1234', '01925 603124', '2007-01-18 22:32:13.048', 'To test e-HTPX portal developed by Ian Berry', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2206, 'other', 'cc03e747a6afbbcbf8be7668acfebee5', 'Other', 'Group Head', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-23 10:41:33.169', 'group head for mass spec other charging facility', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2207, 'sam', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sam', 'Wright', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-26 17:38:48.089', 'I need to submit samples for mass spec/crystallisation.  I am a PhD student working in the OPPF (Supervisor = Jonathan Grimes).

I hope you can have me registered soon.

Sam', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (250, 'BM14', 'cc03e747a6afbbcbf8be7668acfebee5', 'BM14', 'Beam', 'jon@strubi.ox.ac.uk', '1234', '', '2006-11-08 17:47:45', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2209, 'MAlmeida', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mara', 'Almeida', 'jon@strubi.ox.ac.uk', '1234', '', '2007-03-13 08:47:28.26', 'mass spec service', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3000, 'amedeo.lamberti', 'cc03e747a6afbbcbf8be7668acfebee5', 'Amedeo', 'Lamberti', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3001, 'andrzej.lyskowski', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andrzej', 'Lyskowski', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3002, 'anita.hoffmann', 'cc03e747a6afbbcbf8be7668acfebee5', 'Anita', 'Hoffmann', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2193, 'user1', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:18:12.598', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2195, 'user3', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:18:41.113', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2194, 'user2', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:19:02.30', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2196, 'user4', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:20:05.409', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2197, 'user5', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:20:27.596', 'test user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2198, 'user6', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:20:53.377', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2199, 'user7', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:21:04.971', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2200, 'user8', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:21:15.705', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2201, 'user9', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:21:49.439', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2202, 'user10', 'cc03e747a6afbbcbf8be7668acfebee5', 'test', 'user', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:22:05.861', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2203, 'user11', 'cc03e747a6afbbcbf8be7668acfebee5', 'user', 'test', 'jon@strubi.ox.ac.uk', '1234', '', '2007-02-20 22:22:35.157', 'ehtpxtest', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3003, 'elena.stolboushkina', 'cc03e747a6afbbcbf8be7668acfebee5', 'Elena', 'Stolboushkina', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3004, 'elisa.pasqualetto', 'cc03e747a6afbbcbf8be7668acfebee5', 'Elisa', 'Pasqualetto', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3005, 'emma.hopkins', 'cc03e747a6afbbcbf8be7668acfebee5', 'Emma', 'Hopkins', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3006, 'james.kean', 'cc03e747a6afbbcbf8be7668acfebee5', 'James', 'Kean', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3007, 'jing.yu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jing', 'Yu', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3009, 'marko.mihelic', 'cc03e747a6afbbcbf8be7668acfebee5', 'Marko', 'Mihelic', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3011, 'noureddine.atmane', 'cc03e747a6afbbcbf8be7668acfebee5', 'Noureddine', 'Atmane', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3012, 'orly.dym', 'cc03e747a6afbbcbf8be7668acfebee5', 'Orly', 'Dym', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3013, 'patrizia.abrusci', 'cc03e747a6afbbcbf8be7668acfebee5', 'Patrizia', 'Abrusci', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3014, 'silvia.speroni', 'cc03e747a6afbbcbf8be7668acfebee5', 'Silvia', 'Speroni', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2208, 'lenski', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ulf', 'Lenski', 'jon@strubi.ox.ac.uk', '1234', '00493094062925', '2007-03-13 08:47:44.744', 'We''ll establish the Helmholtz Protein Sample Production Facility (PSPF) in Berlin. I''m responible for database and bioinformatic in the project.
I would like to see how you are organizing  
your facility. Perhaps we can learn from you.
Currently I''m working on a web-application for user registration, submitting and  refereiing projects, tracking the lab-workflow etc.
Here I''m using perl and the MVC-framework catalyst.


', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2211, 'aleksandra', 'cc03e747a6afbbcbf8be7668acfebee5', 'Aleksandra', 'Wozniak', 'jon@strubi.ox.ac.uk', '1234', '', '2007-03-23 16:28:43.545', 'sfdg', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3016, 'tomislav.kamenski', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tomislav', 'Kamenski', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3017, 'trevor.sweeney', 'cc03e747a6afbbcbf8be7668acfebee5', 'Trevor', 'Sweeney', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2212, 'mcgeehan', 'cc03e747a6afbbcbf8be7668acfebee5', 'John', 'McGeehan', 'jon@strubi.ox.ac.uk', '1234', '02392842053', '2007-04-17 10:13:55.217', 'Access as an external scientist to the OPPF crystallization facility.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3018, 'vlad.pena', 'cc03e747a6afbbcbf8be7668acfebee5', 'Vlad', 'Pena', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2213, 'samanthah', 'cc03e747a6afbbcbf8be7668acfebee5', 'samantha', 'holmes', 'jon@strubi.ox.ac.uk', '1234', '', '2007-04-27 13:22:34.652', 'Mass spec and crystallisation facilities', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3019, 'yann.brelivet', 'cc03e747a6afbbcbf8be7668acfebee5', 'Yann', 'Brelivet', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2214, 'shuyunchen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Shuyun', 'Chen', 'jon@strubi.ox.ac.uk', '1234', '01865 275259', '2007-05-14 16:00:25.89', 'Hi OPPF, I would like to have some soluble protein mass spec''ed. Many thanks for your help.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2215, 'rcallaghan', 'cc03e747a6afbbcbf8be7668acfebee5', 'Richard', 'Callaghan', 'jon@strubi.ox.ac.uk', '1234', '01865 221 834', '2007-05-14 16:00:35.218', 'Several members of my research team use the facilities in conjunction with a joint project grant with Prof Yvonne Jones.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2216, 'c.j.webby', 'cc03e747a6afbbcbf8be7668acfebee5', 'Celia', 'Webby', 'jon@strubi.ox.ac.uk', '1234', '', '2007-05-14 16:53:48.958', 'to use crystallization robots', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3015, 'susana.goncalves', 'cc03e747a6afbbcbf8be7668acfebee5', 'Susana', 'Goncalves', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2233, 'toomy', 'cc03e747a6afbbcbf8be7668acfebee5', 'sakesit', 'chumnarnsilpa', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-22 16:37:20.364', 'Dr.Jonathan Grimes''s PhD. student', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (253, 'lihui.wang', 'cc03e747a6afbbcbf8be7668acfebee5', 'Lihui', 'Wang', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (254, 'christiane.riedinger', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christiane', 'Riedinger', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (255, 'jonas.boehringer', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jonas', 'Boehringer', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (257, 'ornchuma.itsathitphaisarn', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ornchuma', 'Itsathitphaisarn', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (259, 'atunni', 'cc03e747a6afbbcbf8be7668acfebee5', 'Alan', 'iboldi-Tunnicliffe', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (262, 'camilla.oxley', 'cc03e747a6afbbcbf8be7668acfebee5', 'Camilla', 'Oxley', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3010, 'mohamed.ismail', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mohamed', 'Ismail', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3008, 'juan.reguera', 'cc03e747a6afbbcbf8be7668acfebee5', 'Juan', 'Reguera', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-06-19 09:29:47.067', 'EMBO Course', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2236, 'BenLee', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ben', 'Lee', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-04 08:50:18.003', 'I''m working as a summer student in Dr Erika Mancini''s group at Strubi', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2237, 'charlotte.helgstrand', 'cc03e747a6afbbcbf8be7668acfebee5', 'Charlotte', 'Helgstrand', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-05 16:40:32.815', 'want access to various protocols, especially for the transient protein expression in mammalian cells', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2238, 'ZHAORAN', 'cc03e747a6afbbcbf8be7668acfebee5', 'RAN ', 'ZHAO', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-05 16:41:06.768', 'project requires', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (34, 'joanne', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jo', 'Nettleship', 'jon@strubi.ox.ac.uk', '1234', '', '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2240, 'Helen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Helen', 'Spiers', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-10 14:25:02.569', 'Working for Erika in Strubi', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2242, 'sushmita.borthakur', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sushmita', 'Borthakur', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-16 12:05:30.94', 'To use the mass spectrometry services.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2243, 'nickrbrown', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nick', 'Brown', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-18 10:54:07.557', 'To make use of mass spec service', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2235, 'medjbo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jacquelyn', 'Bond', 'jon@strubi.ox.ac.uk', '1234', '0113 343 8603', '2007-07-27 15:00:07.807', 'I wish to investigate protein expression services of my 400kDa protein of interest.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2248, 'dan', 'cc03e747a6afbbcbf8be7668acfebee5', 'Dan', 'Whalen', 'jon@strubi.ox.ac.uk', '1234', '', '2007-07-27 17:11:18.409', 'Use of the OPPF services, particulary bioinformatics (RONN etc)', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (154, 'aleks', 'cc03e747a6afbbcbf8be7668acfebee5', 'Aleks', 'Watson', 'jon@strubi.ox.ac.uk', '1234', '', '2004-12-16 17:10:35', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2254, 'norhakim', 'cc03e747a6afbbcbf8be7668acfebee5', 'Mohd Norhakim', 'Yahya', 'jon@strubi.ox.ac.uk', '1234', '', '2007-08-28 12:09:12.837', 'To use the Mass Spec facilities.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2252, 'sporter', 'cc03e747a6afbbcbf8be7668acfebee5', 'Steven', 'Porter', 'jon@strubi.ox.ac.uk', '1234', '', '2007-08-30 11:04:25.131', 'Have been introduced to OPPF by Karl Harlos. Would like to set up some crystallization screens and need an account to use the robots and view the results.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2261, 'jitka', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jitka', 'Vevodova', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-06 19:45:33.523', 'for bird flu project with DIS and George Brownlee (pathology). ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2217, 'cjsgrp', 'cc03e747a6afbbcbf8be7668acfebee5', 'Chris', 'Schofield', 'jon@strubi.ox.ac.uk', '1234', '', '2007-05-14 16:56:04.691', 'to allow members of the CJS group to use crystallization robots', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2250, 'staunton', 'cc03e747a6afbbcbf8be7668acfebee5', 'David', 'Staunton', 'jon@strubi.ox.ac.uk', '1234', '75253', '2007-09-19 13:04:32.877', 'Would like ES MS on HPLC pure proteins 10-30kDa.
Can you do MALDI TOF on 100kDa proteins?', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (7, 'robert', 'cc03e747a6afbbcbf8be7668acfebee5', 'Robert', 'Esnouf', 'jon@strubi.ox.ac.uk', '1234', '287547', '2004-04-30 16:57:56', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (230, 'stepheng', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephen', 'Graham', 'jon@strubi.ox.ac.uk', '1234', '87547', '2006-03-06 14:52:24', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (148, 'ludovic', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ludovic', 'Launer', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-08-06 15:31:50', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2255, 'ngreene', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nicholas', 'Greene', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-19 13:05:34.064', 'To be able to view crystal trays set up by George Orriss (from Berks group, Department of Biochemistry)', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2260, 'jperiz', 'cc03e747a6afbbcbf8be7668acfebee5', 'Javier', 'Periz', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-19 13:05:50.829', 'Use Mass spectrometry facility', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2267, 'celine', 'cc03e747a6afbbcbf8be7668acfebee5', 'celine', 'jones', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-19 13:06:05.923', 'I would need to use the mass spectrometry.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2259, 'armitage', 'cc03e747a6afbbcbf8be7668acfebee5', 'Judith', 'Armitage', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-19 13:04:54.08', 'Using system', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2268, 'hbak006', 'cc03e747a6afbbcbf8be7668acfebee5', 'Heather', 'Baker', 'jon@strubi.ox.ac.uk', '1234', '', '2007-09-20 15:38:25.165', 'visitng scientist, using crystallization and mass spec', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2189, 'roisin', 'cc03e747a6afbbcbf8be7668acfebee5', 'Roisin', 'Mc Mahon', 'jon@strubi.ox.ac.uk', '1234', '', '2007-01-31 09:17:54.991', 'To submit samples for Mass Spec and to access images from the crystallisation vault.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2272, 'max', 'cc03e747a6afbbcbf8be7668acfebee5', 'Max', 'Crispin', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-03 16:06:21.989', 'Xtallisation facilities', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (260, 'eleanor.bagg', 'cc03e747a6afbbcbf8be7668acfebee5', 'Eleanor', 'Bagg', 'jon@strubi.ox.ac.uk', '1234', '', '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (109, 'oleg', 'cc03e747a6afbbcbf8be7668acfebee5', 'Oleg', 'Iourin', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (234, 'nada', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nada', 'Psachoulia', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-07-11 14:19:11', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2221, 'xxu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Xiaoning', 'Xu', 'jon@strubi.ox.ac.uk', '1234', '01865-222628', '2007-06-05 11:38:48.924', 'Collaboration with Yvonne Jones on CD1/ILT4 project', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2241, 'janeendicott', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jane ', 'Endicott', 'jon@strubi.ox.ac.uk', '1234', NULL, '2007-07-12 13:27:21.455', 'To use MS facility', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (256, 'jane.endicott', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jane', 'Endicott', 'jon@strubi.ox.ac.uk', '1234', '01865275182', '2007-06-25 10:16:12.549', 'updated user', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (131, 'flashman', 'cc03e747a6afbbcbf8be7668acfebee5', 'Emily', 'Flashman', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-07-05 14:04:21', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2282, 'luke', 'cc03e747a6afbbcbf8be7668acfebee5', 'Luke', 'Yates', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-09 18:09:22.845', 'Member of Robert Gilbert''s group whose work will involve collaboration with the OPPF.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2285, 'cbell', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christian', 'Bell', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-09 18:09:59.313', 'Wellcome Trust Rotational project with Christian Siebold and Yvonne Jones', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2284, 'sfeller', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephan', 'Feller', 'jon@strubi.ox.ac.uk', '1234', '01865-222-431', '2007-10-10 09:08:35.112', 'Crystallisation of SH3 domains with peptides ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2280, 'npollock', 'cc03e747a6afbbcbf8be7668acfebee5', 'Naomi', 'Pollock', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-10 09:08:52.721', 'Studying for a DPhil in Clinical Biochemistry under the supervision of Dr Richard Callaghan and Dr Chris McDevitt, who currently uses the OPPF facilities. My project concerns the crystallisation of members of the ABC transporter family of proteins.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2287, 'tomas', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tomas', 'Malinauskas', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-10 11:29:50.41', 'CR-UK DPhil student from prof. Y. Jones group.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (44, 'ian', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ian', 'Berry', 'jon@strubi.ox.ac.uk', '1234', '287547', '2004-06-23 12:10:56', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2290, 'RSchneider', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ralf ', 'Schneider', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-11 11:43:25.34', 'mass spec analyses', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2291, 'Stephen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephen', 'Cheley', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-11 12:43:50.028', 'I wish to explore expression, purification and crystalization of a membrane pore that has been engineered with enzymatic activity', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2295, 'philip', 'cc03e747a6afbbcbf8be7668acfebee5', 'Philip', 'West', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-19 11:52:09.785', 'D.Phil student in Erika Mancini''s group. Would like to have access to tools for work. Thank you.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2288, 'Theodora', 'cc03e747a6afbbcbf8be7668acfebee5', 'Theodora', 'Tsirka', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-10 14:26:53.112', 'To perform crystalization screens using the oppf facility under the supervision of Dr M. Harkiolaki.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (149, 'vaultdemo', 'cc03e747a6afbbcbf8be7668acfebee5', 'Vault', 'Demo', 'jon@strubi.ox.ac.uk', '1234', '', '2004-10-14 16:42:46', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2301, 'MaxSoegaard', 'cc03e747a6afbbcbf8be7668acfebee5', 'Max ', 'Soegaard', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-24 11:03:24.503', 'Need masspec,will possibly need protein production services', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2304, 'Ben', 'cc03e747a6afbbcbf8be7668acfebee5', 'Ben', 'Lee', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-26 12:32:10.952', 'Crystallisation', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2302, 'biochemnick', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nicholas', 'Anthis', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-26 12:34:09.998', 'I am a graduate student in Iain Campbell''s lab (Oxford Biochemistry), and I would like to submit samples for mass spectroscopy.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2298, 'taleksic', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tamara', 'Aleksic', 'jon@strubi.ox.ac.uk', '1234', '01865-222-431', '2007-10-22 09:29:17.961', 'Postdoctoral fellow working in Cell Signalling Group (S. Feller) at WIMM on SH2 domain mediated complex. Aim is to obtain crystal structure. We expect to have protein/peptide ready for crystallographic screen very shortly.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2305, 'JulianaC', 'cc03e747a6afbbcbf8be7668acfebee5', 'Juliana', 'Callaghan', 'jon@strubi.ox.ac.uk', '1234', '01865 275259', '2007-10-29 09:13:45.166', 'Use of mass spectrometry service', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2309, 'joyce.tay', 'cc03e747a6afbbcbf8be7668acfebee5', 'Joyce', 'Tay', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-31 08:38:01.431', 'To use the mass spec facility for work in Penny Handford''s lab.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2310, 'xiaoqiu.wu@imed.se', 'cc03e747a6afbbcbf8be7668acfebee5', 'xiaoqiu', 'wu', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-31 08:38:32.337', 'interested in mass spec service.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2311, 'some2292', 'cc03e747a6afbbcbf8be7668acfebee5', 'Claire', 'Chivers', 'jon@strubi.ox.ac.uk', '1234', '', '2007-10-31 15:40:21.023', 'Im a new DPhil student in the Biochemistry Dept. and I will have samples that require MS', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (60, 'radu', 'cc03e747a6afbbcbf8be7668acfebee5', 'Radu', 'Aricescu', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (23, 'karl', 'cc03e747a6afbbcbf8be7668acfebee5', 'Karl', 'Harlos', 'jon@strubi.ox.ac.uk', '1234', '', '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (157, 'andreas', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andreas', 'Sonnen', 'jon@strubi.ox.ac.uk', '1234', '', '2005-01-21 12:32:30', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (84, 'ren', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jingshan', 'Ren', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (15, 'christian', 'cc03e747a6afbbcbf8be7668acfebee5', 'Christian', 'Siebold', 'jon@strubi.ox.ac.uk', '1234', '', '2004-06-21 15:45:54', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2318, 'olga', 'cc03e747a6afbbcbf8be7668acfebee5', 'Olga', 'Moroz', 'jon@strubi.ox.ac.uk', '1234', '01904328266', '2007-11-13 13:02:29.245', 'SPINE2 collaborative visit', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2319, 'avelayos', 'cc03e747a6afbbcbf8be7668acfebee5', 'Antonio', 'Velayos Baeza', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-15 11:54:00.14', 'to be able to use the mass spectroscopy service', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2325, 'tanmay', 'cc03e747a6afbbcbf8be7668acfebee5', 'Tanmay', 'Bharat', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-29 13:41:17.233', 'Crystallization.
Robert Gilbert Group
Strubi', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2326, 'muhan.wang', 'cc03e747a6afbbcbf8be7668acfebee5', 'Muhan', 'Wang', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-29 17:22:48.018', 'Need to use Mass Spec service', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2328, 'sarahiqbal', 'cc03e747a6afbbcbf8be7668acfebee5', 'Sarah', 'Iqbal', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-29 17:23:10.877', 'In order to get my protein sample masses, as this is the only facility available to us.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2324, 'thomas.brantzos', 'cc03e747a6afbbcbf8be7668acfebee5', 'Thomas', 'Brantzos', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-29 17:24:44.438', 'New member of Yvonne''s group', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2327, 'benjamin', 'cc03e747a6afbbcbf8be7668acfebee5', 'Benjamin', 'Bishop', 'jon@strubi.ox.ac.uk', '1234', '', '2007-11-29 17:25:49.687', 'New member of dept', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2330, 'KirstinLeath', 'cc03e747a6afbbcbf8be7668acfebee5', 'Kirstin', 'Leath', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-05 14:23:37.021', 'To use the cartesian. (Working in Susan Lea''s lab)', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (53, 'nicola', 'cc03e747a6afbbcbf8be7668acfebee5', 'Nicola', 'Abrescia', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2321, 'Roach', 'cc03e747a6afbbcbf8be7668acfebee5', 'Adrian', 'Roach', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-11 10:09:16.654', 'I''m interested in PIMS!', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (191, 'poussin', 'cc03e747a6afbbcbf8be7668acfebee5', 'Pierre', 'Poussin', 'jon@strubi.ox.ac.uk', '1234', '', '2005-05-26 16:44:12', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2333, 'kolenko', 'cc03e747a6afbbcbf8be7668acfebee5', 'Petr', 'Kolenko', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-12 09:25:37.107', 'I want to register because of testing of our proteins with Fluidigm crystallization chips and to have an access to web to view crystallization progres via the web.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2335, 'dohnalek', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jan', 'Dohnalek', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-12 09:25:59.888', 'Crystallisation experiments and remote monitoring', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2332, 'pierreha', 'cc03e747a6afbbcbf8be7668acfebee5', 'Pierre', 'Hassenboehler', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-12 09:26:22.091', 'Microfluidics experiments from IGBMC strasbgourg', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2331, 'eileenm', 'cc03e747a6afbbcbf8be7668acfebee5', 'Eileen', 'McNeill', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-12 09:28:55.449', 'Interest in finding out more about the OPPF facilities and services with regard to new project being set up in the lab at WTCHG.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2340, 'awebb', 'cc03e747a6afbbcbf8be7668acfebee5', 'Andrew', 'Webb', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-20 15:03:14.621', 'Collaboration between GR Screaton and Y Jones. ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2338, 'david_stoddart', 'cc03e747a6afbbcbf8be7668acfebee5', 'David', 'Stoddart', 'jon@strubi.ox.ac.uk', '1234', '', '2007-12-20 15:03:29.34', 'To use mass spec services', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2342, 'jparker', 'cc03e747a6afbbcbf8be7668acfebee5', 'James', 'Parker', 'jon@strubi.ox.ac.uk', '1234', '', '2008-01-17 16:05:47.667', 'Interested in using the facilities, and in particular mass spectrometry. Am a new team leader in the Department of Biochemistry. ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2344, 'PatW', 'cc03e747a6afbbcbf8be7668acfebee5', 'Pat', 'Whiteman', 'jon@strubi.ox.ac.uk', '1234', '', '2008-01-18 13:13:18.357', 'To use mass spec services', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2347, 'wonggroup', 'cc03e747a6afbbcbf8be7668acfebee5', 'Stephen', 'Bell', 'jon@strubi.ox.ac.uk', '1234', '01865 272690', '2008-01-23 13:26:03.387', 'Want to submit samples for mass spec analysis', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (3, 'jon', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jon', 'Diprose', 'jon@strubi.ox.ac.uk', '1234', '', '2004-04-21 14:50:27', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2349, 'UDE', 'cc03e747a6afbbcbf8be7668acfebee5', 'Angela', 'Beke', 'jon@strubi.ox.ac.uk', '1234', '+36 1 466 5465', '2008-02-11 16:30:30.056', 'Using and imaging fluidigm chips, internet access of images

host: Dr. Karl Harlos
Division of Structural Biology
The Wellcome Trust Centre for Human Genetics
', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2350, 'newtonjiang', 'cc03e747a6afbbcbf8be7668acfebee5', 'Pengju', 'Jiang', 'jon@strubi.ox.ac.uk', '1234', '', '2008-02-12 16:44:44.693', 'I plan to send protein samples to OPPF for mass spec analysis. ', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2351, 'Brunella', 'cc03e747a6afbbcbf8be7668acfebee5', 'Brunella', 'Felicetti', 'jon@strubi.ox.ac.uk', '1234', '', '2008-02-13 09:37:39.418', 'access to optic', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (63, 'james', 'cc03e747a6afbbcbf8be7668acfebee5', 'James', 'Brown', 'jon@strubi.ox.ac.uk', '1234', '', '2004-07-02 17:03:25', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2354, 'elena', 'cc03e747a6afbbcbf8be7668acfebee5', 'Elena', 'Seiradake', 'jon@strubi.ox.ac.uk', '1234', '', '2008-03-03 14:04:51.702', 'EY Jones group', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2355, 'walshbm14', 'cc03e747a6afbbcbf8be7668acfebee5', 'Martin', 'Walsh', 'jon@strubi.ox.ac.uk', '1234', '', '2008-03-07 13:47:44.687', 'catching up!', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2356, 'LVallius', 'cc03e747a6afbbcbf8be7668acfebee5', 'Laura', 'Vallius', 'jon@strubi.ox.ac.uk', '1234', '', '2008-03-07 16:06:44.433', 'Crystal trials are part of my DPhil project.', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2357, 'gberridge', 'cc03e747a6afbbcbf8be7668acfebee5', 'Georgina', 'Berridge', 'jon@strubi.ox.ac.uk', '1234', '', '2008-03-12 12:30:54.689', 'Our Mass spec is broken and we need samples running urgently', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (228, 'doryen', 'cc03e747a6afbbcbf8be7668acfebee5', 'Doryen', 'Bubeck', 'jon@strubi.ox.ac.uk', '1234', '', '2006-02-03 16:34:51', NULL, true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2360, 'stuparnham', 'cc03e747a6afbbcbf8be7668acfebee5', 'stu', 'parnham', 'jon@strubi.ox.ac.uk', '1234', '', NULL, 'previous experience
', false);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2361, 'Xiaoxiao', 'cc03e747a6afbbcbf8be7668acfebee5', 'Xiaoxiao', 'Cheng', 'jon@strubi.ox.ac.uk', '1234', '', '2008-04-16 12:52:25.835', 'Simon Davis Group', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2359, 'alison.hole', 'cc03e747a6afbbcbf8be7668acfebee5', 'Alison', 'Hole', 'jon@strubi.ox.ac.uk', '1234', '', '2008-04-16 12:53:28.037', 'For mass spectrometry sample submissions (from Strubi).', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2362, 'spiff', 'cc03e747a6afbbcbf8be7668acfebee5', 'Luis', 'Gonzalez-Ramirez', 'jon@strubi.ox.ac.uk', '1234', '(00-34) 958181621', NULL, 'I am a research in the field of protein crystallization. I investigate the physical-chemistry of this process in order to rationalize the procedures to obtain high quality crystals.  I apply the counterdiffusion technique and make use of the gels to have a mass transport controlled by diffusion.', false);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2363, 'Evotec', 'cc03e747a6afbbcbf8be7668acfebee5', 'John', 'Barker', 'jon@strubi.ox.ac.uk', '1234', '', '2008-04-22 17:09:13.236', 'Access to crystallization images', true);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (216, 'jean.lee', 'cc03e747a6afbbcbf8be7668acfebee5', 'Jean', 'Lee', 'jon@strubi.ox.ac.uk', '1234', NULL, '2006-01-09 17:08:27', NULL, false);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2364, 'xg-historic-head', 'cc03e747a6afbbcbf8be7668acfebee5', 'Pre 01-MAY-07', 'Group Head', 'jon@strubi.ox.ac.uk', '1234', '', '2008-04-25 14:55:28.915', 'Group Head for pre-charging crystallisation plates.

NB: SHOULD BE DISABLED AND NOT A MEMBER OF ANY ROLE!
', false);
INSERT INTO users (userid, username, "password", firstname, surname, email, phone, fax, registrationdate, notes, enabled) VALUES (2, 'oppfadmin', 'cc03e747a6afbbcbf8be7668acfebee5', 'oppf', 'admin', 'jon@strubi.ox.ac.uk', '1234', NULL, '2004-04-21 10:59:13', NULL, true);


--
-- TOC entry 1531 (class 2606 OID 8609176)
-- Dependencies: 1197 1197
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (roleid);


--
-- TOC entry 1533 (class 2606 OID 8609178)
-- Dependencies: 1197 1197
-- Name: roles_role_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_role_key UNIQUE ("role");


--
-- TOC entry 1535 (class 2606 OID 8609180)
-- Dependencies: 1199 1199 1199
-- Name: userrole_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY userrole
    ADD CONSTRAINT userrole_pkey PRIMARY KEY (userid, roleid);


--
-- TOC entry 1537 (class 2606 OID 8609182)
-- Dependencies: 1200 1200
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


--
-- TOC entry 1539 (class 2606 OID 8609184)
-- Dependencies: 1200 1200
-- Name: users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 1540 (class 2606 OID 8609185)
-- Dependencies: 1199 1530 1197
-- Name: fkf02b8ec125d8a137; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userrole
    ADD CONSTRAINT fkf02b8ec125d8a137 FOREIGN KEY (roleid) REFERENCES roles(roleid);


--
-- TOC entry 1541 (class 2606 OID 8609190)
-- Dependencies: 1199 1200 1536
-- Name: fkf02b8ec12b2df6a1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userrole
    ADD CONSTRAINT fkf02b8ec12b2df6a1 FOREIGN KEY (userid) REFERENCES users(userid);


--
-- TOC entry 1548 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- TOC entry 1550 (class 0 OID 0)
-- Dependencies: 1204
-- Name: id_sequence; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE id_sequence FROM PUBLIC;
REVOKE ALL ON TABLE id_sequence FROM postgres;
GRANT ALL ON TABLE id_sequence TO postgres;
GRANT SELECT,UPDATE ON TABLE id_sequence TO "userdb-admin";
GRANT SELECT,UPDATE ON TABLE id_sequence TO "userdb-user";


--
-- TOC entry 1551 (class 0 OID 0)
-- Dependencies: 1197
-- Name: roles; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE roles FROM PUBLIC;
REVOKE ALL ON TABLE roles FROM postgres;
GRANT ALL ON TABLE roles TO postgres;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE roles TO "userdb-admin";
GRANT SELECT ON TABLE roles TO "userdb-user";


--
-- TOC entry 1552 (class 0 OID 0)
-- Dependencies: 1199
-- Name: userrole; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE userrole FROM PUBLIC;
REVOKE ALL ON TABLE userrole FROM postgres;
GRANT ALL ON TABLE userrole TO postgres;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE userrole TO "userdb-admin";
GRANT SELECT ON TABLE userrole TO "userdb-user";


--
-- TOC entry 1553 (class 0 OID 0)
-- Dependencies: 1200
-- Name: users; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE users FROM PUBLIC;
REVOKE ALL ON TABLE users FROM postgres;
GRANT ALL ON TABLE users TO postgres;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE users TO "userdb-admin";
GRANT SELECT ON TABLE users TO "userdb-user";


--
-- TOC entry 1554 (class 0 OID 0)
-- Dependencies: 1201
-- Name: userroleview; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE userroleview FROM PUBLIC;
REVOKE ALL ON TABLE userroleview FROM postgres;
GRANT ALL ON TABLE userroleview TO postgres;
GRANT SELECT ON TABLE userroleview TO "userdb-tomcat";


--
-- TOC entry 1555 (class 0 OID 0)
-- Dependencies: 1202
-- Name: userview; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE userview FROM PUBLIC;
REVOKE ALL ON TABLE userview FROM postgres;
GRANT ALL ON TABLE userview TO postgres;
GRANT SELECT ON TABLE userview TO "userdb-tomcat";


-- Completed on 2009-02-03 11:47:27 GMT Standard Time

--
-- PostgreSQL database dump complete
--

