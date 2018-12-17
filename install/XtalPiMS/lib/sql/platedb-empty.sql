--
-- PostgreSQL database dump
--

-- Started on 2009-05-06 15:27:21 GMT Standard Time

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1773 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- TOC entry 310 (class 2612 OID 8698753)
-- Name: plperl; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: 
--

CREATE PROCEDURAL LANGUAGE plperl;


--
-- TOC entry 309 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: 
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

--
-- TOC entry 13 (class 1255 OID 8698754)
-- Dependencies: 5 310
-- Name: cslistcount(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION cslistcount(character varying) RETURNS integer
    AS $_$
my $list = shift;
$list =~ s/[^,]//g;
return length($list);
$_$
    LANGUAGE plperl;


ALTER FUNCTION public.cslistcount(character varying) OWNER TO postgres;

--
-- TOC entry 18 (class 1255 OID 8698755)
-- Dependencies: 309 5
-- Name: lastupdated(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION lastupdated() RETURNS "trigger"
    AS $$
begin
new.dtlastupdated = current_timestamp;
return new;
end;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.lastupdated() OWNER TO postgres;

--
-- TOC entry 19 (class 1255 OID 8698756)
-- Dependencies: 309 5
-- Name: new_plate_date(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION new_plate_date() RETURNS "trigger"
    AS $$
BEGIN
NEW.dtsubmitdate = current_timestamp;
RETURN NEW;
END;
$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.new_plate_date() OWNER TO postgres;

--
-- TOC entry 20 (class 1255 OID 8698757)
-- Dependencies: 5
-- Name: plperl_call_handler(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION plperl_call_handler() RETURNS language_handler
    AS '$libdir/plperl', 'plperl_call_handler'
    LANGUAGE c;


ALTER FUNCTION public.plperl_call_handler() OWNER TO postgres;

--
-- TOC entry 21 (class 1255 OID 8698758)
-- Dependencies: 5
-- Name: plpgsql_call_handler(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION plpgsql_call_handler() RETURNS language_handler
    AS '$libdir/plpgsql', 'plpgsql_call_handler'
    LANGUAGE c;


ALTER FUNCTION public.plpgsql_call_handler() OWNER TO postgres;

--
-- TOC entry 22 (class 1255 OID 8698759)
-- Dependencies: 309 5
-- Name: updateLocation(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION "updateLocation"() RETURNS "trigger"
    AS $$BEGIN 
-- Check that imager and date are given
        IF NEW.strbarcode IS NULL THEN
            RAISE EXCEPTION 'barcode cannot be null';
        END IF;
IF NEW.strinstrument IS NULL THEN
            RAISE EXCEPTION 'instrument cannot be null';
        END IF;
        IF NEW.dttimestamp IS NULL THEN
            RAISE EXCEPTION 'date cannot be null';
        END IF;

UPDATE tblplates SET strinstrument=NEW.strinstrument, dtlastimaged=NEW.dttimestamp WHERE strbarcode=NEW.strbarcode;

RETURN NEW;
END;$$
    LANGUAGE plpgsql;


ALTER FUNCTION public."updateLocation"() OWNER TO postgres;

--
-- TOC entry 25 (class 1255 OID 8698760)
-- Dependencies: 5 309
-- Name: updateimager(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION updateimager() RETURNS integer
    AS $$DECLARE
    mviews RECORD;
BEGIN

FOR mviews IN SELECT t.strbarcode, t.strinstrument, u.max
   FROM tblsessions t, ( SELECT s.strbarcode, max(s.dttimestamp) AS max
           FROM tblsessions s
          GROUP BY s.strbarcode) u, tblinstruments v
  WHERE t.dttimestamp = u.max AND u.strbarcode::text = t.strbarcode::text AND t.strinstrument::text = v.strinstrument::text order by t.strbarcode LOOP
EXECUTE 'UPDATE tblplates SET strinstrument='|| quote_literal(mviews.strinstrument) ||', dtlastimaged='|| quote_literal(mviews.max) ||' WHERE strbarcode='|| quote_literal(mviews.strbarcode);

END LOOP;
return 1;
END;$$
    LANGUAGE plpgsql;


ALTER FUNCTION public.updateimager() OWNER TO postgres;

--
-- TOC entry 23 (class 1255 OID 8698761)
-- Dependencies: 5 310
-- Name: wellindex(character); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION wellindex(character) RETURNS integer
    AS $_$
                my $wellref = shift;

                my $column = (ord substr($wellref, 0, 1)) - 64;
                my $row = int(substr($wellref, 1, 2)) - 1;

                return 12 * ($column - 1) + $row;
        $_$
    LANGUAGE plperl;


ALTER FUNCTION public.wellindex(character) OWNER TO postgres;

--
-- TOC entry 24 (class 1255 OID 8698762)
-- Dependencies: 310 5
-- Name: wellref_fromindex(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION wellref_fromindex(integer) RETURNS character
    AS $_$
                # Convert to Well ref number from numeric index  (0-95 to e.g. A01 to 0, H12 to 95)
                my $wellref = shift;

                my $column = int($wellref / 12);
                my $row = $wellref - $column * 12 + 1;

                return sprintf("%s%02d", chr($column+65), $row);
$_$
    LANGUAGE plperl;


ALTER FUNCTION public.wellref_fromindex(integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1244 (class 1259 OID 8698763)
-- Dependencies: 1627 5
-- Name: tblresults; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblresults (
    strbarcode character varying(12) NOT NULL,
    strwell character varying(3) NOT NULL,
    strobservation character varying(20) NOT NULL,
    strobserver character varying(100),
    strchecker character varying(100),
    dtlastupdated timestamp with time zone,
    intsubposition integer DEFAULT 1
);


ALTER TABLE public.tblresults OWNER TO postgres;

--
-- TOC entry 1245 (class 1259 OID 8698766)
-- Dependencies: 1354 5
-- Name: crystalcount; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW crystalcount AS
    SELECT tblresults.strbarcode, count(*) AS xtalcount FROM tblresults WHERE ((tblresults.strobservation)::text = 'Crystals'::text) GROUP BY tblresults.strbarcode;


ALTER TABLE public.crystalcount OWNER TO postgres;

--
-- TOC entry 1246 (class 1259 OID 8698769)
-- Dependencies: 1628 1629 1630 5
-- Name: tblimages; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblimages (
    strsessionid character varying(28) NOT NULL,
    strbarcode character varying(12) NOT NULL,
    dttimestamp timestamp with time zone NOT NULL,
    strwell character varying(3) NOT NULL,
    strbasename character varying(75) NOT NULL,
    strpath character varying(21) NOT NULL,
    strprogver character varying(5),
    strheuristics character varying(10),
    realscore real,
    strclassprog character varying(10) DEFAULT 'wilson'::character varying,
    strparameters character varying(15) DEFAULT '[default]'::character varying,
    intsubposition integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.tblimages OWNER TO postgres;

--
-- TOC entry 1247 (class 1259 OID 8698774)
-- Dependencies: 1355 5
-- Name: crystalimages; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW crystalimages AS
    SELECT tblimages.strsessionid, tblimages.strbarcode, tblimages.dttimestamp, tblimages.strwell, tblresults.strobserver, tblimages.strbasename, tblresults.strchecker, tblresults.strobservation, tblimages.strpath, tblimages.strprogver, tblimages.strheuristics, tblimages.realscore, tblimages.strclassprog, tblimages.strparameters FROM tblimages, tblresults WHERE ((((tblimages.strbarcode)::text = (tblresults.strbarcode)::text) AND ((tblimages.strwell)::text = (tblresults.strwell)::text)) AND (((tblresults.strobservation)::text = 'Crystals'::text) OR ((tblresults.strobservation)::text = 'Synchrotron'::text)));


ALTER TABLE public.crystalimages OWNER TO postgres;

--
-- TOC entry 1248 (class 1259 OID 8698777)
-- Dependencies: 1356 5
-- Name: crystalwells; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW crystalwells AS
    SELECT tblresults.strbarcode, tblresults.strwell FROM tblresults WHERE (((tblresults.strobservation)::text = 'Crystals'::text) OR ((tblresults.strobservation)::text = 'Synchrotron'::text)) ORDER BY tblresults.strbarcode, tblresults.strwell;


ALTER TABLE public.crystalwells OWNER TO postgres;

--
-- TOC entry 1249 (class 1259 OID 8698780)
-- Dependencies: 5
-- Name: tblsessions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblsessions (
    strsessionid character varying(28) NOT NULL,
    strbarcode character varying(12) NOT NULL,
    dttimestamp timestamp with time zone NOT NULL,
    strinstrument character varying(25) NOT NULL
);


ALTER TABLE public.tblsessions OWNER TO postgres;

--
-- TOC entry 1250 (class 1259 OID 8698782)
-- Dependencies: 1357 5
-- Name: daypickcounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW daypickcounts AS
    SELECT date(tblsessions.dttimestamp) AS "day", count(*) AS numpicks FROM tblsessions GROUP BY date(tblsessions.dttimestamp) ORDER BY date(tblsessions.dttimestamp);


ALTER TABLE public.daypickcounts OWNER TO postgres;

--
-- TOC entry 1251 (class 1259 OID 8698785)
-- Dependencies: 1631 1632 1633 5
-- Name: tblplates; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblplates (
    blninhomebase boolean DEFAULT false NOT NULL,
    memdescription text,
    struser character varying(100) NOT NULL,
    dtsubmitdate timestamp with time zone,
    strbarcode character varying(12) NOT NULL,
    dtcreatedate timestamp with time zone,
    strgroup character varying(100),
    intconstruct integer,
    strblock character varying(30),
    dtdestroydate timestamp with time zone,
    intplatetype bigint DEFAULT 1 NOT NULL,
    intscheduleplan bigint DEFAULT 1 NOT NULL,
    strinstrument character varying(25),
    dtlastimaged timestamp with time zone
);


ALTER TABLE public.tblplates OWNER TO postgres;

--
-- TOC entry 1252 (class 1259 OID 8698793)
-- Dependencies: 1358 5
-- Name: daysubmitcounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW daysubmitcounts AS
    SELECT date(tblplates.dtsubmitdate) AS "day", count(*) AS numsubmits FROM tblplates GROUP BY date(tblplates.dtsubmitdate) ORDER BY date(tblplates.dtsubmitdate);


ALTER TABLE public.daysubmitcounts OWNER TO postgres;

--
-- TOC entry 1253 (class 1259 OID 8698796)
-- Dependencies: 1359 5
-- Name: monthpicktotals; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW monthpicktotals AS
    SELECT to_char(timestamptz(daypickcounts."day"), 'YYYYMM'::text) AS "month", to_char(timestamptz(daypickcounts."day"), 'Mon YYYY'::text) AS monthf, sum(daypickcounts.numpicks) AS totalpicks FROM daypickcounts GROUP BY to_char(timestamptz(daypickcounts."day"), 'YYYYMM'::text), to_char(timestamptz(daypickcounts."day"), 'Mon YYYY'::text) ORDER BY to_char(timestamptz(daypickcounts."day"), 'YYYYMM'::text);


ALTER TABLE public.monthpicktotals OWNER TO postgres;

--
-- TOC entry 1254 (class 1259 OID 8698799)
-- Dependencies: 1360 5
-- Name: monthsubmittotals; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW monthsubmittotals AS
    SELECT to_char(timestamptz(daysubmitcounts."day"), 'YYYYMM'::text) AS "month", to_char(timestamptz(daysubmitcounts."day"), 'Mon YYYY'::text) AS monthf, sum(daysubmitcounts.numsubmits) AS totalsubmits FROM daysubmitcounts GROUP BY to_char(timestamptz(daysubmitcounts."day"), 'YYYYMM'::text), to_char(timestamptz(daysubmitcounts."day"), 'Mon YYYY'::text) ORDER BY to_char(timestamptz(daysubmitcounts."day"), 'YYYYMM'::text);


ALTER TABLE public.monthsubmittotals OWNER TO postgres;

--
-- TOC entry 1255 (class 1259 OID 8698802)
-- Dependencies: 1361 5
-- Name: oppfdaysubmitcounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW oppfdaysubmitcounts AS
    SELECT date(tblplates.dtsubmitdate) AS "day", count(*) AS numsubmits FROM tblplates WHERE ((tblplates.strgroup)::text = ('OPPF'::character varying)::text) GROUP BY date(tblplates.dtsubmitdate) ORDER BY date(tblplates.dtsubmitdate);


ALTER TABLE public.oppfdaysubmitcounts OWNER TO postgres;

--
-- TOC entry 1256 (class 1259 OID 8698805)
-- Dependencies: 1362 5
-- Name: oppfmonthsubmittotals; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW oppfmonthsubmittotals AS
    SELECT to_char(timestamptz(oppfdaysubmitcounts."day"), 'YYYYMM'::text) AS "month", to_char(timestamptz(oppfdaysubmitcounts."day"), 'Mon YYYY'::text) AS monthf, sum(oppfdaysubmitcounts.numsubmits) AS totalsubmits FROM oppfdaysubmitcounts GROUP BY to_char(timestamptz(oppfdaysubmitcounts."day"), 'YYYYMM'::text), to_char(timestamptz(oppfdaysubmitcounts."day"), 'Mon YYYY'::text) ORDER BY to_char(timestamptz(oppfdaysubmitcounts."day"), 'YYYYMM'::text);


ALTER TABLE public.oppfmonthsubmittotals OWNER TO postgres;

--
-- TOC entry 1257 (class 1259 OID 8698808)
-- Dependencies: 1363 5
-- Name: picktimecounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW picktimecounts AS
    SELECT date_part('hour'::text, tblsessions.dttimestamp) AS timef, count(*) AS count FROM tblsessions WHERE ((tblsessions.strinstrument)::text = ('Oasis1700 21degC'::character varying)::text) GROUP BY date_part('hour'::text, tblsessions.dttimestamp) ORDER BY date_part('hour'::text, tblsessions.dttimestamp);


ALTER TABLE public.picktimecounts OWNER TO postgres;

--
-- TOC entry 1258 (class 1259 OID 8698811)
-- Dependencies: 1634 5
-- Name: tblinstruments; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblinstruments (
    strinstrument character varying(25) NOT NULL,
    realtemperature real DEFAULT 20.0
);


ALTER TABLE public.tblinstruments OWNER TO postgres;

--
-- TOC entry 1259 (class 1259 OID 8698814)
-- Dependencies: 1364 5
-- Name: plateimagerview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW plateimagerview AS
    SELECT t.strbarcode, t.strinstrument AS instrument, v.realtemperature AS temperature FROM tblsessions t, (SELECT s.strbarcode, max(s.dttimestamp) AS max FROM tblsessions s GROUP BY s.strbarcode) u, tblinstruments v WHERE (((t.dttimestamp = u.max) AND ((u.strbarcode)::text = (t.strbarcode)::text)) AND ((t.strinstrument)::text = (v.strinstrument)::text)) ORDER BY t.strbarcode;


ALTER TABLE public.plateimagerview OWNER TO postgres;

--
-- TOC entry 1260 (class 1259 OID 8698817)
-- Dependencies: 1365 5
-- Name: plateinfoview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW plateinfoview AS
    SELECT p.strbarcode, p.dtcreatedate, p.memdescription, p.struser, p.strgroup, p.dtdestroydate, p.intconstruct, q.xtalcount, r.instrument, r.temperature FROM ((SELECT tblplates.blninhomebase, tblplates.memdescription, tblplates.struser, tblplates.dtsubmitdate, tblplates.strbarcode, tblplates.dtcreatedate, tblplates.strgroup, tblplates.intconstruct, tblplates.strblock, tblplates.dtdestroydate, tblplates.intplatetype, tblplates.intscheduleplan FROM tblplates) p LEFT JOIN (SELECT crystalcount.strbarcode, crystalcount.xtalcount FROM crystalcount) q USING (strbarcode)), ((SELECT tblplates.blninhomebase, tblplates.memdescription, tblplates.struser, tblplates.dtsubmitdate, tblplates.strbarcode, tblplates.dtcreatedate, tblplates.strgroup, tblplates.intconstruct, tblplates.strblock, tblplates.dtdestroydate, tblplates.intplatetype, tblplates.intscheduleplan FROM tblplates) t LEFT JOIN (SELECT plateimagerview.strbarcode, plateimagerview.instrument, plateimagerview.temperature FROM plateimagerview) r USING (strbarcode)) WHERE ((t.strbarcode)::text = (p.strbarcode)::text);


ALTER TABLE public.plateinfoview OWNER TO postgres;

--
-- TOC entry 1261 (class 1259 OID 8698821)
-- Dependencies: 1366 5
-- Name: plates4deg; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW plates4deg AS
    SELECT DISTINCT tblplates.strbarcode, tblplates.intconstruct FROM tblsessions, tblplates WHERE (((((tblsessions.strinstrument)::text = 'Oasis1750 4degC'::text) OR ((tblsessions.strinstrument)::text = 'OasisLS3 4degC'::text)) OR ((tblsessions.strinstrument)::text = 'RI1000-0014'::text)) AND ((tblsessions.strbarcode)::text = (tblplates.strbarcode)::text)) ORDER BY tblplates.strbarcode, tblplates.intconstruct;


ALTER TABLE public.plates4deg OWNER TO postgres;

--
-- TOC entry 1262 (class 1259 OID 8698824)
-- Dependencies: 1367 5
-- Name: plates21deg; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW plates21deg AS
    SELECT tblplates.strbarcode, tblplates.intconstruct FROM (tblplates LEFT JOIN plates4deg USING (strbarcode)) WHERE (plates4deg.strbarcode IS NULL);


ALTER TABLE public.plates21deg OWNER TO postgres;

--
-- TOC entry 1263 (class 1259 OID 8698827)
-- Dependencies: 1368 5
-- Name: submittimecounts; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW submittimecounts AS
    SELECT date_part('hour'::text, tblplates.dtsubmitdate) AS timef, count(*) AS count FROM tblplates GROUP BY date_part('hour'::text, tblplates.dtsubmitdate) ORDER BY date_part('hour'::text, tblplates.dtsubmitdate);


ALTER TABLE public.submittimecounts OWNER TO postgres;

--
-- TOC entry 1264 (class 1259 OID 8698830)
-- Dependencies: 1635 5
-- Name: tblarchives; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblarchives (
    strdirectory character varying(6) NOT NULL,
    dtcompleted timestamp with time zone NOT NULL,
    txtoutput text,
    intnumarchived integer,
    intnumfailed integer,
    strtransfertime character varying(16),
    strprocesstime character varying(16),
    intcompressedby integer,
    blndeleted boolean DEFAULT false,
    intimagesarchived integer
);


ALTER TABLE public.tblarchives OWNER TO postgres;

--
-- TOC entry 1265 (class 1259 OID 8698836)
-- Dependencies: 5
-- Name: tblblocks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblblocks (
    strblock character varying(30) NOT NULL,
    strmanufacturer text,
    strtype text
);


ALTER TABLE public.tblblocks OWNER TO postgres;

--
-- TOC entry 1266 (class 1259 OID 8698841)
-- Dependencies: 5
-- Name: tblblocksources; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblblocksources (
    strblock character varying(30) NOT NULL,
    strwell character varying(3) NOT NULL,
    strsource character varying(128),
    strsourcereagent character varying(3),
    strsourcemanufacturer character varying(128)
);


ALTER TABLE public.tblblocksources OWNER TO postgres;

--
-- TOC entry 1267 (class 1259 OID 8698843)
-- Dependencies: 5
-- Name: tblclassprogs; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblclassprogs (
    strclassprog character varying(10) NOT NULL
);


ALTER TABLE public.tblclassprogs OWNER TO postgres;

--
-- TOC entry 1268 (class 1259 OID 8698845)
-- Dependencies: 5
-- Name: tbldbperformance; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tbldbperformance (
    dttimestamp timestamp with time zone NOT NULL,
    floatduration double precision NOT NULL
);


ALTER TABLE public.tbldbperformance OWNER TO postgres;

--
-- TOC entry 1269 (class 1259 OID 8698847)
-- Dependencies: 5
-- Name: tblgroupmembers; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblgroupmembers (
    strgroup character varying(100) NOT NULL,
    struser character varying(100) NOT NULL
);


ALTER TABLE public.tblgroupmembers OWNER TO postgres;

--
-- TOC entry 1270 (class 1259 OID 8698849)
-- Dependencies: 5
-- Name: tblgroups; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblgroups (
    strgroup character varying(100) NOT NULL,
    strgrouphead character varying(100) NOT NULL,
    strorganisation text
);


ALTER TABLE public.tblgroups OWNER TO postgres;

--
-- TOC entry 1271 (class 1259 OID 8698854)
-- Dependencies: 5
-- Name: tblobservations; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblobservations (
    strobservation character varying(20) NOT NULL,
    intdisplayorder integer,
    strtag character varying(12),
    intcolour integer
);


ALTER TABLE public.tblobservations OWNER TO postgres;

--
-- TOC entry 1272 (class 1259 OID 8698856)
-- Dependencies: 1636 5
-- Name: tblpix; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblpix (
    strbarcode character varying(12),
    strwell character varying(3),
    strfilename character varying(40) NOT NULL,
    dttimestamp timestamp with time zone,
    intsubposition integer DEFAULT 1
);


ALTER TABLE public.tblpix OWNER TO postgres;

--
-- TOC entry 1273 (class 1259 OID 8698859)
-- Dependencies: 5
-- Name: tblplatetype_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tblplatetype_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblplatetype_id_seq OWNER TO postgres;

--
-- TOC entry 1274 (class 1259 OID 8698861)
-- Dependencies: 1637 1638 1639 1640 1641 1642 1643 1644 5
-- Name: tblplatetype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblplatetype (
    platetypeid bigint DEFAULT nextval('tblplatetype_id_seq'::regclass) NOT NULL,
    strplatetype text NOT NULL,
    intcolumns integer DEFAULT 12 NOT NULL,
    introws integer DEFAULT 8 NOT NULL,
    intsubpositions integer DEFAULT 1 NOT NULL,
    intsubpositionheight integer,
    strbarcodepattern text,
    intdefaultscheduleplan bigint DEFAULT 1 NOT NULL,
    CONSTRAINT mincolumns CHECK ((intcolumns > 0)),
    CONSTRAINT minrows CHECK ((introws > 0)),
    CONSTRAINT minsubpositions CHECK ((intsubpositions > 0))
);


ALTER TABLE public.tblplatetype OWNER TO postgres;

--
-- TOC entry 1805 (class 0 OID 0)
-- Dependencies: 1274
-- Name: COLUMN tblplatetype.intsubpositionheight; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN tblplatetype.intsubpositionheight IS 'Height of the well in micrometres';


--
-- TOC entry 1275 (class 1259 OID 8698874)
-- Dependencies: 5
-- Name: tblsaltconditions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblsaltconditions (
    strblock character varying(30) NOT NULL,
    strwell character varying(3) NOT NULL
);


ALTER TABLE public.tblsaltconditions OWNER TO postgres;

--
-- TOC entry 1276 (class 1259 OID 8698876)
-- Dependencies: 5
-- Name: tblscheduleplan_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tblscheduleplan_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblscheduleplan_id_seq OWNER TO postgres;

--
-- TOC entry 1277 (class 1259 OID 8698878)
-- Dependencies: 1645 5
-- Name: tblscheduleplan; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblscheduleplan (
    scheduleplanid bigint DEFAULT nextval('tblscheduleplan_id_seq'::regclass) NOT NULL,
    strname text NOT NULL,
    strdescription text
);


ALTER TABLE public.tblscheduleplan OWNER TO postgres;

--
-- TOC entry 1278 (class 1259 OID 8698884)
-- Dependencies: 5
-- Name: tblscheduleplanoffset_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tblscheduleplanoffset_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblscheduleplanoffset_id_seq OWNER TO postgres;

--
-- TOC entry 1279 (class 1259 OID 8698886)
-- Dependencies: 1646 1647 5
-- Name: tblscheduleplanoffset; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblscheduleplanoffset (
    scheduleplanoffsetid bigint DEFAULT nextval('tblscheduleplanoffset_id_seq'::regclass) NOT NULL,
    scheduleplanid bigint NOT NULL,
    intoffsethoursfromtimezero integer NOT NULL,
    intimagingnumber integer NOT NULL,
    intpriority integer DEFAULT 5 NOT NULL
);


ALTER TABLE public.tblscheduleplanoffset OWNER TO postgres;

--
-- TOC entry 1280 (class 1259 OID 8698890)
-- Dependencies: 1648 1649 1650 5
-- Name: tblschedules; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblschedules (
    strbarcode character varying(12) NOT NULL,
    dttoimage timestamp with time zone DEFAULT date_trunc('milliseconds'::text, now()) NOT NULL,
    intpriority integer DEFAULT 1 NOT NULL,
    strinstrument character varying(25) NOT NULL,
    intstate integer DEFAULT 1,
    dtimaged timestamp with time zone,
    strimagingid character varying(28)
);


ALTER TABLE public.tblschedules OWNER TO postgres;

--
-- TOC entry 1281 (class 1259 OID 8698895)
-- Dependencies: 5
-- Name: tblscreenmatches_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tblscreenmatches_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.tblscreenmatches_id_seq OWNER TO postgres;

--
-- TOC entry 1282 (class 1259 OID 8698897)
-- Dependencies: 1651 5
-- Name: tblscreenmatches; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblscreenmatches (
    strblock character varying(30) NOT NULL,
    intwellno integer NOT NULL,
    memdescription text NOT NULL,
    strmatchblock character varying(30) NOT NULL,
    intmatchwellno integer NOT NULL,
    id bigint DEFAULT nextval('tblscreenmatches_id_seq'::regclass) NOT NULL
);


ALTER TABLE public.tblscreenmatches OWNER TO postgres;

--
-- TOC entry 1283 (class 1259 OID 8698903)
-- Dependencies: 5
-- Name: tblscreens; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblscreens (
    strblock character varying(30) NOT NULL,
    intwellno integer NOT NULL,
    strscreen character varying(128) NOT NULL,
    strconc character varying(20),
    strunit character varying(10),
    realconc double precision,
    realsiconc double precision,
    strsiunit character varying(10),
    strwellref character varying(3)
);


ALTER TABLE public.tblscreens OWNER TO postgres;

--
-- TOC entry 1284 (class 1259 OID 8698905)
-- Dependencies: 1652 5
-- Name: tbltimecoursereq; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tbltimecoursereq (
    stremail character varying(100) NOT NULL,
    strbarcode character varying(12) NOT NULL,
    strwell character varying(3) NOT NULL,
    dttimestamp timestamp with time zone DEFAULT ('now'::text)::timestamp(6) with time zone
);


ALTER TABLE public.tbltimecoursereq OWNER TO postgres;

--
-- TOC entry 1285 (class 1259 OID 8698908)
-- Dependencies: 5
-- Name: tblusercc; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblusercc (
    struser character varying(100) NOT NULL,
    stremailcc character varying(100) NOT NULL
);


ALTER TABLE public.tblusercc OWNER TO postgres;

--
-- TOC entry 1286 (class 1259 OID 8698910)
-- Dependencies: 1653 1654 1655 1656 1657 5
-- Name: tblusers; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tblusers (
    struser character varying(100) NOT NULL,
    blnmailmontage boolean DEFAULT false,
    blnadmin boolean DEFAULT false,
    dtlastvisited timestamp with time zone,
    inetlasthost inet,
    blnenabled boolean DEFAULT true NOT NULL,
    blnmailnotify boolean DEFAULT true,
    blnchecker boolean DEFAULT false
);


ALTER TABLE public.tblusers OWNER TO postgres;

--
-- TOC entry 1287 (class 1259 OID 8698920)
-- Dependencies: 1369 5
-- Name: trialdropview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW trialdropview AS
    SELECT trialdrop.barcode, trialdrop.well, trialdrop.description, trialdrop.saltcondition, trialdrop.synchrotron, trialdrop.imagedate, trialdrop.inspectionname, trialdrop.imager, trialdrop.temperature, trialdrop.imageurl, trialdrop.imagetype, trialdrop.timepoint, trialdrop.width, trialdrop.height, trialdrop.widthperpixel, trialdrop.heightperpixel, trialdrop.conditionid, trialdrop.localname, trialdrop.localnumber, trialdrop.manufacturername, trialdrop.manufacturerscreenname, trialdrop.manufacturercode, trialdrop.manufacturercatcode, trialdrop.volatilecondition, trialdrop.finalph, trialdrop.screenname, trialdrop.componentquantity, trialdrop."type", trialdrop.volatilecomponent, trialdrop.casnumber, trialdrop.ph, trialdrop.imageurl2, trialdrop.safetyid, trialdrop.softwarescorename, trialdrop.version, trialdrop.softwarescoretype, trialdrop.softwarescoredescription, trialdrop.softwarescorecolour, trialdrop.humanscoreversion, trialdrop.humanscoretype, trialdrop.humanscoredescription, trialdrop.colourlong, trialdrop.humanscorename, trialdrop.constructid, trialdrop.constructname, trialdrop.sampleid, trialdrop.samplename, trialdrop.microscopeimageinspectionname, trialdrop.microscopeimagedate, trialdrop.microscopeimager, trialdrop.microscopeimagetemperature, trialdrop.microscopeimagetype, trialdrop.microscopeimagetimepoint, trialdrop.microscopeimagewidth, trialdrop.microscopeimageheight, trialdrop.microscopeimagewidthperpixel, trialdrop.microscopeimageheightperpixel, trialdrop.microscopeimageurl, trialdrop."owner", trialdrop."group" FROM (SELECT (i.strbarcode)::text AS barcode, (((i.strwell)::text || '.'::text) || (i.intsubposition)::text) AS well, p.memdescription AS description, (SELECT CASE WHEN (count(1) = 1) THEN true ELSE false END AS "case" FROM tblsaltconditions sc WHERE (((sc.strblock)::text = (p.strblock)::text) AND ((sc.strwell)::text = (i.strwell)::text))) AS saltcondition, (SELECT CASE WHEN (count(1) = 1) THEN true ELSE false END AS synchrotron FROM tblresults WHERE (((tblresults.strbarcode)::text = (i.strbarcode)::text) AND ((tblresults.strwell)::text = (i.strwell)::text))) AS synchrotron, i.dttimestamp AS imagedate, (i.strsessionid)::text AS inspectionname, (s.strinstrument)::text AS imager, im.realtemperature AS temperature, ((((i.strbarcode)::text || '/'::text) || (i.strbasename)::text) || '.jpg'::text) AS imageurl, 'composite'::text AS imagetype, ('+'::text || ((i.dttimestamp - p.dtcreatedate))::text) AS timepoint, CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 750 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 1280 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 1280 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 691 ELSE NULL::integer END AS width, CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 700 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 960 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 960 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 108 ELSE NULL::integer END AS height, (CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 2.857 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 1.587 ELSE NULL::numeric END)::double precision AS widthperpixel, (CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 2.857 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 1.587 ELSE NULL::numeric END)::double precision AS heightperpixel, (- (1)::bigint) AS conditionid, (bl.strblock)::text AS localname, (bl.strwell)::text AS localnumber, (bl.strsourcemanufacturer)::text AS manufacturername, (bl.strsource)::text AS manufacturerscreenname, (bl.strsourcereagent)::text AS manufacturercode, ''::text AS manufacturercatcode, false AS volatilecondition, (7.0)::double precision AS finalph, (bl.strscreen)::text AS screenname, (((round((bl.realsiconc)::numeric, 3))::text || ' '::text) || (bl.strsiunit)::text) AS componentquantity, ''::text AS "type", false AS volatilecomponent, ''::text AS casnumber, (7.0)::double precision AS ph, ''::text AS imageurl2, (- (1)::bigint) AS safetyid, 'ALICE'::text AS softwarescorename, (i.strprogver)::text AS version, 'software'::text AS softwarescoretype, CASE WHEN ((i.realscore >= (0)::double precision) AND (i.realscore < (1)::double precision)) THEN 'Empty Well'::text WHEN ((i.realscore >= (1)::double precision) AND (i.realscore < (2)::double precision)) THEN 'Empty Drop'::text WHEN ((i.realscore >= (2)::double precision) AND (i.realscore < (3)::double precision)) THEN 'Precipitate'::text WHEN ((i.realscore >= (3)::double precision) AND (i.realscore < (4)::double precision)) THEN 'Slightly better than Precipitate'::text WHEN ((i.realscore >= (4)::double precision) AND (i.realscore < (5)::double precision)) THEN 'Small Crystals'::text WHEN ((i.realscore >= (5)::double precision) AND (i.realscore < (6)::double precision)) THEN 'Some Crystals'::text WHEN (i.realscore >= (6)::double precision) THEN 'Crystals'::text ELSE 'Unclassified'::text END AS softwarescoredescription, CASE WHEN ((i.realscore >= (0)::double precision) AND (i.realscore < (1)::double precision)) THEN 16711680 WHEN ((i.realscore >= (1)::double precision) AND (i.realscore < (2)::double precision)) THEN 16711808 WHEN ((i.realscore >= (2)::double precision) AND (i.realscore < (3)::double precision)) THEN 16711935 WHEN ((i.realscore >= (3)::double precision) AND (i.realscore < (4)::double precision)) THEN 8388863 WHEN ((i.realscore >= (4)::double precision) AND (i.realscore < (5)::double precision)) THEN 255 WHEN ((i.realscore >= (5)::double precision) AND (i.realscore < (6)::double precision)) THEN 33023 WHEN (i.realscore >= (6)::double precision) THEN 65535 ELSE 16737792 END AS softwarescorecolour, '-'::text AS humanscoreversion, 'human'::text AS humanscoretype, (r.strobservation)::text AS humanscoredescription, tblobservations.intcolour AS colourlong, (r.strobserver)::text AS humanscorename, (p.intconstruct)::bigint AS constructid, ('OPPF'::text || (p.intconstruct)::text) AS constructname, (- (1)::bigint) AS sampleid, 'unknown'::text AS samplename, ''::text AS microscopeimageinspectionname, pix.dttimestamp AS microscopeimagedate, 'Pixera'::text AS microscopeimager, (20.0)::double precision AS microscopeimagetemperature, 'zoomed'::text AS microscopeimagetype, (''::text || ((pix.dttimestamp - p.dtcreatedate))::text) AS microscopeimagetimepoint, -1 AS microscopeimagewidth, -1 AS microscopeimageheight, (- (1)::double precision) AS microscopeimagewidthperpixel, (- (1)::double precision) AS microscopeimageheightperpixel, (((i.strbarcode)::text || '/'::text) || (pix.strfilename)::text) AS microscopeimageurl, (p.struser)::text AS "owner", (p.strgroup)::text AS "group" FROM (((((tblplates p JOIN tblimages i USING (strbarcode)) JOIN (tblsessions s JOIN tblinstruments im USING (strinstrument)) USING (strbarcode, strsessionid)) LEFT JOIN (SELECT b.strblock, b.strwell, b.strsource, b.strsourcereagent, b.strsourcemanufacturer, ss.strscreen, ss.realsiconc, ss.strsiunit FROM (tblblocksources b LEFT JOIN tblscreens ss ON ((((b.strblock)::text = (ss.strblock)::text) AND ((b.strwell)::text = (ss.strwellref)::text))))) bl USING (strblock, strwell)) LEFT JOIN (tblresults r LEFT JOIN tblobservations USING (strobservation)) USING (strbarcode, strwell)) LEFT JOIN tblpix pix USING (strbarcode, strwell))) trialdrop;


ALTER TABLE public.trialdropview OWNER TO postgres;

--
-- TOC entry 1288 (class 1259 OID 8698924)
-- Dependencies: 1370 5
-- Name: trialdropviewdev; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW trialdropviewdev AS
    SELECT (i.strbarcode)::text AS barcode, (((i.strwell)::text || '.'::text) || (i.intsubposition)::text) AS well, p.memdescription AS description, (SELECT CASE WHEN (count(1) = 1) THEN true ELSE false END AS "case" FROM tblsaltconditions sc WHERE (((sc.strblock)::text = (p.strblock)::text) AND ((sc.strwell)::text = (i.strwell)::text))) AS saltcondition, (SELECT CASE WHEN (count(1) = 1) THEN true ELSE false END AS synchrotron FROM tblresults WHERE (((tblresults.strbarcode)::text = (i.strbarcode)::text) AND ((tblresults.strwell)::text = (i.strwell)::text))) AS synchrotron, i.dttimestamp AS imagedate, (i.strsessionid)::text AS inspectionname, (s.strinstrument)::text AS imager, im.realtemperature AS temperature, ((((i.strbarcode)::text || '/'::text) || (i.strbasename)::text) || '.jpg'::text) AS imageurl, 'composite'::text AS imagetype, ('+'::text || ((i.dttimestamp - p.dtcreatedate))::text) AS timepoint, CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 750 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 1280 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 1280 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 691 ELSE NULL::integer END AS width, CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 700 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 960 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 960 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 108 ELSE NULL::integer END AS height, (CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 2.857 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 1.587 ELSE NULL::numeric END)::double precision AS widthperpixel, (CASE WHEN ((((s.strinstrument)::text = 'Oasis 1700'::text) OR ((s.strinstrument)::text = 'Oasis 1750'::text)) OR ((s.strinstrument)::text = 'Oasis LS3'::text)) THEN 2.857 WHEN ((s.strinstrument)::text = 'RI1000-0014'::text) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '4413%'::text)) THEN 2.979 WHEN (((s.strinstrument)::text = 'RI182-0005'::text) AND ((i.strbarcode)::text ~~ '1%'::text)) THEN 1.587 ELSE NULL::numeric END)::double precision AS heightperpixel, (- (1)::bigint) AS conditionid, (bl.strblock)::text AS localname, (bl.strwell)::text AS localnumber, (bl.strsourcemanufacturer)::text AS manufacturername, (bl.strsource)::text AS manufacturerscreenname, (bl.strsourcereagent)::text AS manufacturercode, ''::text AS manufacturercatcode, false AS volatilecondition, (7.0)::double precision AS finalph, (bl.strscreen)::text AS screenname, (((round((bl.realsiconc)::numeric, 3))::text || ' '::text) || (bl.strsiunit)::text) AS componentquantity, ''::text AS "type", false AS volatilecomponent, ''::text AS casnumber, (7.0)::double precision AS ph, ''::text AS imageurl2, (- (1)::bigint) AS safetyid, 'ALICE'::text AS softwarescorename, (i.strprogver)::text AS version, 'software'::text AS softwarescoretype, CASE WHEN ((i.realscore >= (0)::double precision) AND (i.realscore < (1)::double precision)) THEN 'Empty Well'::text WHEN ((i.realscore >= (1)::double precision) AND (i.realscore < (2)::double precision)) THEN 'Empty Drop'::text WHEN ((i.realscore >= (2)::double precision) AND (i.realscore < (3)::double precision)) THEN 'Precipitate'::text WHEN ((i.realscore >= (3)::double precision) AND (i.realscore < (4)::double precision)) THEN 'Slightly better than Precipitate'::text WHEN ((i.realscore >= (4)::double precision) AND (i.realscore < (5)::double precision)) THEN 'Small Crystals'::text WHEN ((i.realscore >= (5)::double precision) AND (i.realscore < (6)::double precision)) THEN 'Some Crystals'::text WHEN (i.realscore >= (6)::double precision) THEN 'Crystals'::text ELSE 'Unclassified'::text END AS softwarescoredescription, CASE WHEN ((i.realscore >= (0)::double precision) AND (i.realscore < (1)::double precision)) THEN 16711680 WHEN ((i.realscore >= (1)::double precision) AND (i.realscore < (2)::double precision)) THEN 16711808 WHEN ((i.realscore >= (2)::double precision) AND (i.realscore < (3)::double precision)) THEN 16711935 WHEN ((i.realscore >= (3)::double precision) AND (i.realscore < (4)::double precision)) THEN 8388863 WHEN ((i.realscore >= (4)::double precision) AND (i.realscore < (5)::double precision)) THEN 255 WHEN ((i.realscore >= (5)::double precision) AND (i.realscore < (6)::double precision)) THEN 33023 WHEN (i.realscore >= (6)::double precision) THEN 65535 ELSE 16737792 END AS softwarescorecolour, '-'::text AS humanscoreversion, 'human'::text AS humanscoretype, (r.strobservation)::text AS humanscoredescription, tblobservations.intcolour AS colourlong, (r.strobserver)::text AS humanscorename, (p.intconstruct)::bigint AS constructid, ('OPPF'::text || (p.intconstruct)::text) AS constructname, (- (1)::bigint) AS sampleid, 'unknown'::text AS samplename, ''::text AS microscopeimageinspectionname, pix.dttimestamp AS microscopeimagedate, 'Pixera'::text AS microscopeimager, (20.0)::double precision AS microscopeimagetemperature, 'zoomed'::text AS microscopeimagetype, (''::text || ((pix.dttimestamp - p.dtcreatedate))::text) AS microscopeimagetimepoint, -1 AS microscopeimagewidth, -1 AS microscopeimageheight, (- (1)::double precision) AS microscopeimagewidthperpixel, (- (1)::double precision) AS microscopeimageheightperpixel, (((i.strbarcode)::text || '/'::text) || (pix.strfilename)::text) AS microscopeimageurl, (p.struser)::text AS "owner", (p.strgroup)::text AS "group" FROM (((((tblplates p JOIN tblimages i USING (strbarcode)) JOIN (tblsessions s JOIN tblinstruments im USING (strinstrument)) USING (strbarcode, strsessionid)) LEFT JOIN (SELECT b.strblock, b.strwell, b.strsource, b.strsourcereagent, b.strsourcemanufacturer, ss.strscreen, ss.realsiconc, ss.strsiunit FROM (tblblocksources b LEFT JOIN tblscreens ss ON ((((b.strblock)::text = (ss.strblock)::text) AND ((b.strwell)::text = (ss.strwellref)::text))))) bl USING (strblock, strwell)) LEFT JOIN (tblresults r LEFT JOIN tblobservations USING (strobservation)) USING (strbarcode, strwell)) LEFT JOIN tblpix pix USING (strbarcode, strwell));


ALTER TABLE public.trialdropviewdev OWNER TO postgres;

--
-- TOC entry 1695 (class 2606 OID 8698929)
-- Dependencies: 1271 1271
-- Name: displayorder; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblobservations
    ADD CONSTRAINT displayorder UNIQUE (intdisplayorder);


--
-- TOC entry 1681 (class 2606 OID 8698931)
-- Dependencies: 1264 1264 1264
-- Name: tblarchives_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblarchives
    ADD CONSTRAINT tblarchives_pkey PRIMARY KEY (strdirectory, dtcompleted);


--
-- TOC entry 1683 (class 2606 OID 8698933)
-- Dependencies: 1265 1265
-- Name: tblblocks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblblocks
    ADD CONSTRAINT tblblocks_pkey PRIMARY KEY (strblock);


--
-- TOC entry 1685 (class 2606 OID 8698935)
-- Dependencies: 1266 1266 1266
-- Name: tblblocksources_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblblocksources
    ADD CONSTRAINT tblblocksources_pkey PRIMARY KEY (strblock, strwell);


--
-- TOC entry 1687 (class 2606 OID 8698937)
-- Dependencies: 1267 1267
-- Name: tblclassprogs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblclassprogs
    ADD CONSTRAINT tblclassprogs_pkey PRIMARY KEY (strclassprog);


--
-- TOC entry 1689 (class 2606 OID 8698939)
-- Dependencies: 1268 1268
-- Name: tbldbperformance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tbldbperformance
    ADD CONSTRAINT tbldbperformance_pkey PRIMARY KEY (dttimestamp);


--
-- TOC entry 1691 (class 2606 OID 8698941)
-- Dependencies: 1269 1269 1269
-- Name: tblgroupmembers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblgroupmembers
    ADD CONSTRAINT tblgroupmembers_pkey PRIMARY KEY (strgroup, struser);


--
-- TOC entry 1693 (class 2606 OID 8698943)
-- Dependencies: 1270 1270
-- Name: tblgroups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblgroups
    ADD CONSTRAINT tblgroups_pkey PRIMARY KEY (strgroup);


--
-- TOC entry 1664 (class 2606 OID 8698945)
-- Dependencies: 1246 1246
-- Name: tblimages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblimages
    ADD CONSTRAINT tblimages_pkey PRIMARY KEY (strbasename);


--
-- TOC entry 1679 (class 2606 OID 8698947)
-- Dependencies: 1258 1258
-- Name: tblinstruments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblinstruments
    ADD CONSTRAINT tblinstruments_pkey PRIMARY KEY (strinstrument);


--
-- TOC entry 1697 (class 2606 OID 8698949)
-- Dependencies: 1271 1271
-- Name: tblobservations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblobservations
    ADD CONSTRAINT tblobservations_pkey PRIMARY KEY (strobservation);


--
-- TOC entry 1699 (class 2606 OID 8698951)
-- Dependencies: 1272 1272
-- Name: tblpix_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblpix
    ADD CONSTRAINT tblpix_pkey PRIMARY KEY (strfilename);


--
-- TOC entry 1676 (class 2606 OID 8698953)
-- Dependencies: 1251 1251
-- Name: tblplates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT tblplates_pkey PRIMARY KEY (strbarcode);


--
-- TOC entry 1701 (class 2606 OID 8698955)
-- Dependencies: 1274 1274
-- Name: tblplatetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblplatetype
    ADD CONSTRAINT tblplatetype_pkey PRIMARY KEY (platetypeid);


--
-- TOC entry 1703 (class 2606 OID 8698957)
-- Dependencies: 1274 1274
-- Name: tblplatetype_strplatetype_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblplatetype
    ADD CONSTRAINT tblplatetype_strplatetype_key UNIQUE (strplatetype);


--
-- TOC entry 1660 (class 2606 OID 8698959)
-- Dependencies: 1244 1244 1244
-- Name: tblresults_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblresults
    ADD CONSTRAINT tblresults_pkey PRIMARY KEY (strbarcode, strwell);


--
-- TOC entry 1705 (class 2606 OID 8698961)
-- Dependencies: 1275 1275 1275
-- Name: tblsaltconditions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblsaltconditions
    ADD CONSTRAINT tblsaltconditions_pkey PRIMARY KEY (strblock, strwell);


--
-- TOC entry 1707 (class 2606 OID 8698963)
-- Dependencies: 1277 1277
-- Name: tblscheduleplan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscheduleplan
    ADD CONSTRAINT tblscheduleplan_pkey PRIMARY KEY (scheduleplanid);


--
-- TOC entry 1709 (class 2606 OID 8698965)
-- Dependencies: 1277 1277
-- Name: tblscheduleplan_strname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscheduleplan
    ADD CONSTRAINT tblscheduleplan_strname_key UNIQUE (strname);


--
-- TOC entry 1711 (class 2606 OID 8698967)
-- Dependencies: 1279 1279
-- Name: tblscheduleplanoffset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscheduleplanoffset
    ADD CONSTRAINT tblscheduleplanoffset_pkey PRIMARY KEY (scheduleplanoffsetid);


--
-- TOC entry 1713 (class 2606 OID 8698969)
-- Dependencies: 1279 1279 1279
-- Name: tblscheduleplanoffset_scheduleplanid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscheduleplanoffset
    ADD CONSTRAINT tblscheduleplanoffset_scheduleplanid_key UNIQUE (scheduleplanid, intimagingnumber);


--
-- TOC entry 1715 (class 2606 OID 8698971)
-- Dependencies: 1280 1280 1280
-- Name: tblschedules_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblschedules
    ADD CONSTRAINT tblschedules_pkey PRIMARY KEY (strbarcode, dttoimage);


--
-- TOC entry 1723 (class 2606 OID 8698973)
-- Dependencies: 1282 1282
-- Name: tblscreenmatches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscreenmatches
    ADD CONSTRAINT tblscreenmatches_pkey PRIMARY KEY (id);


--
-- TOC entry 1725 (class 2606 OID 8698975)
-- Dependencies: 1282 1282 1282 1282 1282 1282
-- Name: tblscreenmatches_strblock_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscreenmatches
    ADD CONSTRAINT tblscreenmatches_strblock_key UNIQUE (strblock, intwellno, memdescription, strmatchblock, intmatchwellno);


--
-- TOC entry 1729 (class 2606 OID 8698977)
-- Dependencies: 1283 1283 1283 1283
-- Name: tblscreens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblscreens
    ADD CONSTRAINT tblscreens_pkey PRIMARY KEY (strblock, intwellno, strscreen);


--
-- TOC entry 1668 (class 2606 OID 8698979)
-- Dependencies: 1249 1249
-- Name: tblsessions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblsessions
    ADD CONSTRAINT tblsessions_pkey PRIMARY KEY (strsessionid);


--
-- TOC entry 1731 (class 2606 OID 8698981)
-- Dependencies: 1284 1284 1284 1284
-- Name: tbltimecoursereq_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tbltimecoursereq
    ADD CONSTRAINT tbltimecoursereq_pkey PRIMARY KEY (stremail, strbarcode, strwell);


--
-- TOC entry 1733 (class 2606 OID 8698983)
-- Dependencies: 1285 1285 1285
-- Name: tblusercc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblusercc
    ADD CONSTRAINT tblusercc_pkey PRIMARY KEY (struser, stremailcc);


--
-- TOC entry 1735 (class 2606 OID 8698985)
-- Dependencies: 1286 1286
-- Name: tblusers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tblusers
    ADD CONSTRAINT tblusers_pkey PRIMARY KEY (struser);


--
-- TOC entry 1661 (class 1259 OID 8698986)
-- Dependencies: 1246 1246
-- Name: tblimages_barcodewell_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblimages_barcodewell_idx ON tblimages USING btree (strbarcode, strwell);


--
-- TOC entry 1662 (class 1259 OID 8698987)
-- Dependencies: 1246
-- Name: tblimages_dttimestamp_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblimages_dttimestamp_idx ON tblimages USING btree (dttimestamp);


--
-- TOC entry 1665 (class 1259 OID 8698988)
-- Dependencies: 1246
-- Name: tblimages_strbarcode_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblimages_strbarcode_idx ON tblimages USING btree (strbarcode);


--
-- TOC entry 1666 (class 1259 OID 8698989)
-- Dependencies: 1246
-- Name: tblimages_strsessionid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblimages_strsessionid_idx ON tblimages USING btree (strsessionid);


--
-- TOC entry 1672 (class 1259 OID 8698990)
-- Dependencies: 1251
-- Name: tblplates_block_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblplates_block_idx ON tblplates USING btree (strblock);


--
-- TOC entry 1673 (class 1259 OID 8698991)
-- Dependencies: 1251
-- Name: tblplates_construct_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblplates_construct_idx ON tblplates USING btree (intconstruct);


--
-- TOC entry 1674 (class 1259 OID 8698992)
-- Dependencies: 1251 1251
-- Name: tblplates_groupconstruct_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblplates_groupconstruct_idx ON tblplates USING btree (strgroup, intconstruct);


--
-- TOC entry 1677 (class 1259 OID 8698993)
-- Dependencies: 1251
-- Name: tblplates_struser_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblplates_struser_idx ON tblplates USING btree (struser);


--
-- TOC entry 1658 (class 1259 OID 8698994)
-- Dependencies: 1244
-- Name: tblresults_observation_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblresults_observation_idx ON tblresults USING btree (strobservation);


--
-- TOC entry 1716 (class 1259 OID 8698995)
-- Dependencies: 1280
-- Name: tblschedules_strbarcode_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblschedules_strbarcode_idx ON tblschedules USING btree (strbarcode);


--
-- TOC entry 1717 (class 1259 OID 8698996)
-- Dependencies: 1280
-- Name: tblschedules_strimagingid_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX tblschedules_strimagingid_idx ON tblschedules USING btree (strimagingid);


--
-- TOC entry 1718 (class 1259 OID 8698997)
-- Dependencies: 1280
-- Name: tblschedules_strinstrument_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblschedules_strinstrument_idx ON tblschedules USING btree (strinstrument);


--
-- TOC entry 1719 (class 1259 OID 8698998)
-- Dependencies: 1282
-- Name: tblscreenmatches_block_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblscreenmatches_block_idx ON tblscreenmatches USING btree (strblock);


--
-- TOC entry 1720 (class 1259 OID 8698999)
-- Dependencies: 1282 1282
-- Name: tblscreenmatches_blockwell_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblscreenmatches_blockwell_idx ON tblscreenmatches USING btree (strblock, intwellno);


--
-- TOC entry 1721 (class 1259 OID 8699000)
-- Dependencies: 1282
-- Name: tblscreenmatches_desc_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblscreenmatches_desc_idx ON tblscreenmatches USING btree (memdescription);


--
-- TOC entry 1726 (class 1259 OID 8699001)
-- Dependencies: 1282
-- Name: tblscreenmatches_wellno_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblscreenmatches_wellno_idx ON tblscreenmatches USING btree (intwellno);


--
-- TOC entry 1727 (class 1259 OID 8699002)
-- Dependencies: 1283 1283
-- Name: tblscreens_blockwell; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblscreens_blockwell ON tblscreens USING btree (strblock, intwellno);


--
-- TOC entry 1669 (class 1259 OID 8699003)
-- Dependencies: 1249
-- Name: tblsessions_strbarcode_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblsessions_strbarcode_idx ON tblsessions USING btree (strbarcode);


--
-- TOC entry 1670 (class 1259 OID 8699004)
-- Dependencies: 1249 1249
-- Name: tblsessions_strbarcode_strinstrument_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblsessions_strbarcode_strinstrument_idx ON tblsessions USING btree (strbarcode, strinstrument);


--
-- TOC entry 1671 (class 1259 OID 8699005)
-- Dependencies: 1249
-- Name: tblsessions_strinstrument_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX tblsessions_strinstrument_idx ON tblsessions USING btree (strinstrument);


--
-- TOC entry 1770 (class 2620 OID 8699006)
-- Dependencies: 1251 19
-- Name: tblplates_instgr; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER tblplates_instgr
    BEFORE INSERT ON tblplates
    FOR EACH ROW
    EXECUTE PROCEDURE new_plate_date();


--
-- TOC entry 1768 (class 2620 OID 8699007)
-- Dependencies: 18 1244
-- Name: tblresults_updatetgr; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER tblresults_updatetgr
    BEFORE INSERT OR UPDATE ON tblresults
    FOR EACH ROW
    EXECUTE PROCEDURE lastupdated();


--
-- TOC entry 1769 (class 2620 OID 8699008)
-- Dependencies: 22 1249
-- Name: updatePlateLocation; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER "updatePlateLocation"
    AFTER INSERT ON tblsessions
    FOR EACH ROW
    EXECUTE PROCEDURE "updateLocation"();


--
-- TOC entry 1752 (class 2606 OID 8699009)
-- Dependencies: 1286 1734 1269
-- Name: fk8e1c6878507286d6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroupmembers
    ADD CONSTRAINT fk8e1c6878507286d6 FOREIGN KEY (struser) REFERENCES tblusers(struser);


--
-- TOC entry 1753 (class 2606 OID 8699014)
-- Dependencies: 1269 1692 1270
-- Name: fk8e1c6878bc5325be; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroupmembers
    ADD CONSTRAINT fk8e1c6878bc5325be FOREIGN KEY (strgroup) REFERENCES tblgroups(strgroup);


--
-- TOC entry 1750 (class 2606 OID 8699019)
-- Dependencies: 1682 1265 1266
-- Name: fkc5002149bbc0c1da; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblblocksources
    ADD CONSTRAINT fkc5002149bbc0c1da FOREIGN KEY (strblock) REFERENCES tblblocks(strblock);


--
-- TOC entry 1756 (class 2606 OID 8699024)
-- Dependencies: 1286 1734 1270
-- Name: fkfa9453121d5d7328; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroups
    ADD CONSTRAINT fkfa9453121d5d7328 FOREIGN KEY (strgrouphead) REFERENCES tblusers(struser);


--
-- TOC entry 1740 (class 2606 OID 8699029)
-- Dependencies: 1267 1686 1246
-- Name: know_classprog; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblimages
    ADD CONSTRAINT know_classprog FOREIGN KEY (strclassprog) REFERENCES tblclassprogs(strclassprog) ON UPDATE CASCADE;


--
-- TOC entry 1744 (class 2606 OID 8699034)
-- Dependencies: 1265 1682 1251
-- Name: known_block; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT known_block FOREIGN KEY (strblock) REFERENCES tblblocks(strblock) ON UPDATE CASCADE;


--
-- TOC entry 1736 (class 2606 OID 8699039)
-- Dependencies: 1244 1734 1286
-- Name: known_checker; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblresults
    ADD CONSTRAINT known_checker FOREIGN KEY (strchecker) REFERENCES tblusers(struser) ON UPDATE CASCADE;


--
-- TOC entry 1745 (class 2606 OID 8699044)
-- Dependencies: 1692 1270 1251
-- Name: known_group; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT known_group FOREIGN KEY (strgroup) REFERENCES tblgroups(strgroup) ON UPDATE CASCADE;


--
-- TOC entry 1754 (class 2606 OID 8699049)
-- Dependencies: 1270 1692 1269
-- Name: known_group_groupmembers; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroupmembers
    ADD CONSTRAINT known_group_groupmembers FOREIGN KEY (strgroup) REFERENCES tblgroups(strgroup) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1755 (class 2606 OID 8699054)
-- Dependencies: 1734 1269 1286
-- Name: known_groupmember; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroupmembers
    ADD CONSTRAINT known_groupmember FOREIGN KEY (struser) REFERENCES tblusers(struser) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1737 (class 2606 OID 8699059)
-- Dependencies: 1271 1244 1696
-- Name: known_observation; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblresults
    ADD CONSTRAINT known_observation FOREIGN KEY (strobservation) REFERENCES tblobservations(strobservation) ON UPDATE CASCADE;


--
-- TOC entry 1738 (class 2606 OID 8699064)
-- Dependencies: 1734 1244 1286
-- Name: known_observer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblresults
    ADD CONSTRAINT known_observer FOREIGN KEY (strobserver) REFERENCES tblusers(struser) ON UPDATE CASCADE;


--
-- TOC entry 1760 (class 2606 OID 8699069)
-- Dependencies: 1265 1275 1682
-- Name: known_saltconditions_block; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblsaltconditions
    ADD CONSTRAINT known_saltconditions_block FOREIGN KEY (strblock) REFERENCES tblblocks(strblock) ON UPDATE CASCADE;


--
-- TOC entry 1766 (class 2606 OID 8699074)
-- Dependencies: 1265 1283 1682
-- Name: known_screen_block; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblscreens
    ADD CONSTRAINT known_screen_block FOREIGN KEY (strblock) REFERENCES tblblocks(strblock) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1764 (class 2606 OID 8699079)
-- Dependencies: 1282 1682 1265
-- Name: known_screenmatches_block; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblscreenmatches
    ADD CONSTRAINT known_screenmatches_block FOREIGN KEY (strblock) REFERENCES tblblocks(strblock) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1765 (class 2606 OID 8699084)
-- Dependencies: 1682 1282 1265
-- Name: known_screenmatches_match_block; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblscreenmatches
    ADD CONSTRAINT known_screenmatches_match_block FOREIGN KEY (strmatchblock) REFERENCES tblblocks(strblock) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1746 (class 2606 OID 8699094)
-- Dependencies: 1286 1251 1734
-- Name: known_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT known_user FOREIGN KEY (struser) REFERENCES tblusers(struser) MATCH FULL ON UPDATE CASCADE;


--
-- TOC entry 1767 (class 2606 OID 8699099)
-- Dependencies: 1285 1286 1734
-- Name: known_user_usercc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblusercc
    ADD CONSTRAINT known_user_usercc FOREIGN KEY (struser) REFERENCES tblusers(struser) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1751 (class 2606 OID 8699104)
-- Dependencies: 1265 1682 1266
-- Name: tblblocksources_strblock_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblblocksources
    ADD CONSTRAINT tblblocksources_strblock_fkey FOREIGN KEY (strblock) REFERENCES tblblocks(strblock);


--
-- TOC entry 1757 (class 2606 OID 8699109)
-- Dependencies: 1286 1270 1734
-- Name: tblgroups_strgrouphead_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblgroups
    ADD CONSTRAINT tblgroups_strgrouphead_fkey FOREIGN KEY (strgrouphead) REFERENCES tblusers(struser);


--
-- TOC entry 1741 (class 2606 OID 8699114)
-- Dependencies: 1675 1246 1251
-- Name: tblimages_strbarcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblimages
    ADD CONSTRAINT tblimages_strbarcode_fkey FOREIGN KEY (strbarcode) REFERENCES tblplates(strbarcode);


--
-- TOC entry 1758 (class 2606 OID 8699119)
-- Dependencies: 1272 1251 1675
-- Name: tblpix_strbarcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblpix
    ADD CONSTRAINT tblpix_strbarcode_fkey FOREIGN KEY (strbarcode) REFERENCES tblplates(strbarcode);


--
-- TOC entry 1747 (class 2606 OID 8699124)
-- Dependencies: 1251 1700 1274
-- Name: tblplates_intplatetype_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT tblplates_intplatetype_fkey FOREIGN KEY (intplatetype) REFERENCES tblplatetype(platetypeid);


--
-- TOC entry 1748 (class 2606 OID 8699129)
-- Dependencies: 1251 1277 1706
-- Name: tblplates_intscheduleplan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT tblplates_intscheduleplan_fkey FOREIGN KEY (intscheduleplan) REFERENCES tblscheduleplan(scheduleplanid);


--
-- TOC entry 1749 (class 2606 OID 8699134)
-- Dependencies: 1258 1678 1251
-- Name: tblplates_strinstrument_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplates
    ADD CONSTRAINT tblplates_strinstrument_fkey FOREIGN KEY (strinstrument) REFERENCES tblinstruments(strinstrument);


--
-- TOC entry 1759 (class 2606 OID 8699139)
-- Dependencies: 1706 1274 1277
-- Name: tblplatetype_intdefaultscheduleplan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblplatetype
    ADD CONSTRAINT tblplatetype_intdefaultscheduleplan_fkey FOREIGN KEY (intdefaultscheduleplan) REFERENCES tblscheduleplan(scheduleplanid);


--
-- TOC entry 1739 (class 2606 OID 8699144)
-- Dependencies: 1251 1244 1675
-- Name: tblresults_strbarcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblresults
    ADD CONSTRAINT tblresults_strbarcode_fkey FOREIGN KEY (strbarcode) REFERENCES tblplates(strbarcode);


--
-- TOC entry 1761 (class 2606 OID 8699149)
-- Dependencies: 1277 1706 1279
-- Name: tblscheduleplanoffset_scheduleplanid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblscheduleplanoffset
    ADD CONSTRAINT tblscheduleplanoffset_scheduleplanid_fkey FOREIGN KEY (scheduleplanid) REFERENCES tblscheduleplan(scheduleplanid);


--
-- TOC entry 1762 (class 2606 OID 8699154)
-- Dependencies: 1280 1251 1675
-- Name: tblschedules_strbarcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblschedules
    ADD CONSTRAINT tblschedules_strbarcode_fkey FOREIGN KEY (strbarcode) REFERENCES tblplates(strbarcode);


--
-- TOC entry 1763 (class 2606 OID 8699159)
-- Dependencies: 1280 1678 1258
-- Name: tblschedules_strinstrument_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblschedules
    ADD CONSTRAINT tblschedules_strinstrument_fkey FOREIGN KEY (strinstrument) REFERENCES tblinstruments(strinstrument);


--
-- TOC entry 1742 (class 2606 OID 8699164)
-- Dependencies: 1249 1675 1251
-- Name: tblsessions_strbarcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblsessions
    ADD CONSTRAINT tblsessions_strbarcode_fkey FOREIGN KEY (strbarcode) REFERENCES tblplates(strbarcode);


--
-- TOC entry 1743 (class 2606 OID 8699169)
-- Dependencies: 1678 1258 1249
-- Name: tblsessions_strinstrument_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tblsessions
    ADD CONSTRAINT tblsessions_strinstrument_fkey FOREIGN KEY (strinstrument) REFERENCES tblinstruments(strinstrument);


--
-- TOC entry 1774 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- TOC entry 1775 (class 0 OID 0)
-- Dependencies: 25
-- Name: updateimager(); Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON FUNCTION updateimager() FROM PUBLIC;
REVOKE ALL ON FUNCTION updateimager() FROM postgres;
GRANT ALL ON FUNCTION updateimager() TO postgres;
GRANT ALL ON FUNCTION updateimager() TO PUBLIC;


--
-- TOC entry 1776 (class 0 OID 0)
-- Dependencies: 1244
-- Name: tblresults; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblresults FROM PUBLIC;
REVOKE ALL ON TABLE tblresults FROM postgres;
GRANT ALL ON TABLE tblresults TO postgres;


--
-- TOC entry 1777 (class 0 OID 0)
-- Dependencies: 1245
-- Name: crystalcount; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE crystalcount FROM PUBLIC;
REVOKE ALL ON TABLE crystalcount FROM postgres;
GRANT ALL ON TABLE crystalcount TO postgres;


--
-- TOC entry 1778 (class 0 OID 0)
-- Dependencies: 1246
-- Name: tblimages; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblimages FROM PUBLIC;
REVOKE ALL ON TABLE tblimages FROM postgres;
GRANT ALL ON TABLE tblimages TO postgres;


--
-- TOC entry 1779 (class 0 OID 0)
-- Dependencies: 1247
-- Name: crystalimages; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE crystalimages FROM PUBLIC;
REVOKE ALL ON TABLE crystalimages FROM postgres;
GRANT ALL ON TABLE crystalimages TO postgres;


--
-- TOC entry 1780 (class 0 OID 0)
-- Dependencies: 1248
-- Name: crystalwells; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE crystalwells FROM PUBLIC;
REVOKE ALL ON TABLE crystalwells FROM postgres;
GRANT ALL ON TABLE crystalwells TO postgres;


--
-- TOC entry 1781 (class 0 OID 0)
-- Dependencies: 1249
-- Name: tblsessions; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblsessions FROM PUBLIC;
REVOKE ALL ON TABLE tblsessions FROM postgres;
GRANT ALL ON TABLE tblsessions TO postgres;


--
-- TOC entry 1782 (class 0 OID 0)
-- Dependencies: 1250
-- Name: daypickcounts; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE daypickcounts FROM PUBLIC;
REVOKE ALL ON TABLE daypickcounts FROM postgres;
GRANT ALL ON TABLE daypickcounts TO postgres;


--
-- TOC entry 1783 (class 0 OID 0)
-- Dependencies: 1251
-- Name: tblplates; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblplates FROM PUBLIC;
REVOKE ALL ON TABLE tblplates FROM postgres;
GRANT ALL ON TABLE tblplates TO postgres;


--
-- TOC entry 1784 (class 0 OID 0)
-- Dependencies: 1252
-- Name: daysubmitcounts; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE daysubmitcounts FROM PUBLIC;
REVOKE ALL ON TABLE daysubmitcounts FROM postgres;
GRANT ALL ON TABLE daysubmitcounts TO postgres;


--
-- TOC entry 1785 (class 0 OID 0)
-- Dependencies: 1253
-- Name: monthpicktotals; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE monthpicktotals FROM PUBLIC;
REVOKE ALL ON TABLE monthpicktotals FROM postgres;
GRANT ALL ON TABLE monthpicktotals TO postgres;


--
-- TOC entry 1786 (class 0 OID 0)
-- Dependencies: 1254
-- Name: monthsubmittotals; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE monthsubmittotals FROM PUBLIC;
REVOKE ALL ON TABLE monthsubmittotals FROM postgres;
GRANT ALL ON TABLE monthsubmittotals TO postgres;


--
-- TOC entry 1787 (class 0 OID 0)
-- Dependencies: 1255
-- Name: oppfdaysubmitcounts; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE oppfdaysubmitcounts FROM PUBLIC;
REVOKE ALL ON TABLE oppfdaysubmitcounts FROM postgres;
GRANT ALL ON TABLE oppfdaysubmitcounts TO postgres;


--
-- TOC entry 1788 (class 0 OID 0)
-- Dependencies: 1256
-- Name: oppfmonthsubmittotals; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE oppfmonthsubmittotals FROM PUBLIC;
REVOKE ALL ON TABLE oppfmonthsubmittotals FROM postgres;
GRANT ALL ON TABLE oppfmonthsubmittotals TO postgres;


--
-- TOC entry 1789 (class 0 OID 0)
-- Dependencies: 1257
-- Name: picktimecounts; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE picktimecounts FROM PUBLIC;
REVOKE ALL ON TABLE picktimecounts FROM postgres;
GRANT ALL ON TABLE picktimecounts TO postgres;


--
-- TOC entry 1790 (class 0 OID 0)
-- Dependencies: 1258
-- Name: tblinstruments; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblinstruments FROM PUBLIC;
REVOKE ALL ON TABLE tblinstruments FROM postgres;
GRANT ALL ON TABLE tblinstruments TO postgres;


--
-- TOC entry 1791 (class 0 OID 0)
-- Dependencies: 1259
-- Name: plateimagerview; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE plateimagerview FROM PUBLIC;
REVOKE ALL ON TABLE plateimagerview FROM postgres;
GRANT ALL ON TABLE plateimagerview TO postgres;


--
-- TOC entry 1792 (class 0 OID 0)
-- Dependencies: 1260
-- Name: plateinfoview; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE plateinfoview FROM PUBLIC;
REVOKE ALL ON TABLE plateinfoview FROM postgres;
GRANT ALL ON TABLE plateinfoview TO postgres;


--
-- TOC entry 1793 (class 0 OID 0)
-- Dependencies: 1261
-- Name: plates4deg; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE plates4deg FROM PUBLIC;
REVOKE ALL ON TABLE plates4deg FROM postgres;
GRANT ALL ON TABLE plates4deg TO postgres;


--
-- TOC entry 1794 (class 0 OID 0)
-- Dependencies: 1262
-- Name: plates21deg; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE plates21deg FROM PUBLIC;
REVOKE ALL ON TABLE plates21deg FROM postgres;
GRANT ALL ON TABLE plates21deg TO postgres;


--
-- TOC entry 1795 (class 0 OID 0)
-- Dependencies: 1263
-- Name: submittimecounts; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE submittimecounts FROM PUBLIC;
REVOKE ALL ON TABLE submittimecounts FROM postgres;
GRANT ALL ON TABLE submittimecounts TO postgres;


--
-- TOC entry 1796 (class 0 OID 0)
-- Dependencies: 1264
-- Name: tblarchives; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblarchives FROM PUBLIC;
REVOKE ALL ON TABLE tblarchives FROM postgres;
GRANT ALL ON TABLE tblarchives TO postgres;


--
-- TOC entry 1797 (class 0 OID 0)
-- Dependencies: 1265
-- Name: tblblocks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblblocks FROM PUBLIC;
REVOKE ALL ON TABLE tblblocks FROM postgres;
GRANT ALL ON TABLE tblblocks TO postgres;


--
-- TOC entry 1798 (class 0 OID 0)
-- Dependencies: 1266
-- Name: tblblocksources; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblblocksources FROM PUBLIC;
REVOKE ALL ON TABLE tblblocksources FROM postgres;
GRANT ALL ON TABLE tblblocksources TO postgres;


--
-- TOC entry 1799 (class 0 OID 0)
-- Dependencies: 1267
-- Name: tblclassprogs; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblclassprogs FROM PUBLIC;
REVOKE ALL ON TABLE tblclassprogs FROM postgres;
GRANT ALL ON TABLE tblclassprogs TO postgres;


--
-- TOC entry 1800 (class 0 OID 0)
-- Dependencies: 1268
-- Name: tbldbperformance; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tbldbperformance FROM PUBLIC;
REVOKE ALL ON TABLE tbldbperformance FROM postgres;
GRANT ALL ON TABLE tbldbperformance TO postgres;


--
-- TOC entry 1801 (class 0 OID 0)
-- Dependencies: 1269
-- Name: tblgroupmembers; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblgroupmembers FROM PUBLIC;
REVOKE ALL ON TABLE tblgroupmembers FROM postgres;
GRANT ALL ON TABLE tblgroupmembers TO postgres;


--
-- TOC entry 1802 (class 0 OID 0)
-- Dependencies: 1270
-- Name: tblgroups; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblgroups FROM PUBLIC;
REVOKE ALL ON TABLE tblgroups FROM postgres;
GRANT ALL ON TABLE tblgroups TO postgres;


--
-- TOC entry 1803 (class 0 OID 0)
-- Dependencies: 1271
-- Name: tblobservations; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblobservations FROM PUBLIC;
REVOKE ALL ON TABLE tblobservations FROM postgres;
GRANT ALL ON TABLE tblobservations TO postgres;


--
-- TOC entry 1804 (class 0 OID 0)
-- Dependencies: 1272
-- Name: tblpix; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblpix FROM PUBLIC;
REVOKE ALL ON TABLE tblpix FROM postgres;
GRANT ALL ON TABLE tblpix TO postgres;


--
-- TOC entry 1806 (class 0 OID 0)
-- Dependencies: 1274
-- Name: tblplatetype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblplatetype FROM PUBLIC;
REVOKE ALL ON TABLE tblplatetype FROM postgres;
GRANT ALL ON TABLE tblplatetype TO postgres;


--
-- TOC entry 1807 (class 0 OID 0)
-- Dependencies: 1275
-- Name: tblsaltconditions; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblsaltconditions FROM PUBLIC;
REVOKE ALL ON TABLE tblsaltconditions FROM postgres;
GRANT ALL ON TABLE tblsaltconditions TO postgres;


--
-- TOC entry 1808 (class 0 OID 0)
-- Dependencies: 1277
-- Name: tblscheduleplan; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblscheduleplan FROM PUBLIC;
REVOKE ALL ON TABLE tblscheduleplan FROM postgres;
GRANT ALL ON TABLE tblscheduleplan TO postgres;


--
-- TOC entry 1809 (class 0 OID 0)
-- Dependencies: 1279
-- Name: tblscheduleplanoffset; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblscheduleplanoffset FROM PUBLIC;
REVOKE ALL ON TABLE tblscheduleplanoffset FROM postgres;
GRANT ALL ON TABLE tblscheduleplanoffset TO postgres;


--
-- TOC entry 1810 (class 0 OID 0)
-- Dependencies: 1280
-- Name: tblschedules; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblschedules FROM PUBLIC;
REVOKE ALL ON TABLE tblschedules FROM postgres;
GRANT ALL ON TABLE tblschedules TO postgres;


--
-- TOC entry 1811 (class 0 OID 0)
-- Dependencies: 1282
-- Name: tblscreenmatches; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblscreenmatches FROM PUBLIC;
REVOKE ALL ON TABLE tblscreenmatches FROM postgres;
GRANT ALL ON TABLE tblscreenmatches TO postgres;


--
-- TOC entry 1812 (class 0 OID 0)
-- Dependencies: 1283
-- Name: tblscreens; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblscreens FROM PUBLIC;
REVOKE ALL ON TABLE tblscreens FROM postgres;
GRANT ALL ON TABLE tblscreens TO postgres;


--
-- TOC entry 1813 (class 0 OID 0)
-- Dependencies: 1284
-- Name: tbltimecoursereq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tbltimecoursereq FROM PUBLIC;
REVOKE ALL ON TABLE tbltimecoursereq FROM postgres;
GRANT ALL ON TABLE tbltimecoursereq TO postgres;


--
-- TOC entry 1814 (class 0 OID 0)
-- Dependencies: 1285
-- Name: tblusercc; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblusercc FROM PUBLIC;
REVOKE ALL ON TABLE tblusercc FROM postgres;
GRANT ALL ON TABLE tblusercc TO postgres;


--
-- TOC entry 1815 (class 0 OID 0)
-- Dependencies: 1286
-- Name: tblusers; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE tblusers FROM PUBLIC;
REVOKE ALL ON TABLE tblusers FROM postgres;
GRANT ALL ON TABLE tblusers TO postgres;


-- Completed on 2009-05-06 15:27:26 GMT Standard Time

--
-- PostgreSQL database dump complete
--

