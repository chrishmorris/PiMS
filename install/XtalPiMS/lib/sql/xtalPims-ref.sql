--
-- PostgreSQL database dump
--

-- Started on 2009-06-10 09:56:04

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1711 (class 1259 OID 62935)
-- Dependencies: 6
-- Name: acco_permission; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_permission (
    optype character varying(32) NOT NULL,
    permission boolean NOT NULL,
    permissionclass character varying(32) NOT NULL,
    rolename character varying(32) NOT NULL,
    systemclassid bigint NOT NULL,
    accessobjectid bigint NOT NULL,
    usergroupid bigint NOT NULL
);


ALTER TABLE public.acco_permission OWNER TO postgres;

--
-- TOC entry 1712 (class 1259 OID 62938)
-- Dependencies: 6
-- Name: acco_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_user (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL,
    personid bigint
);


ALTER TABLE public.acco_user OWNER TO postgres;

--
-- TOC entry 1713 (class 1259 OID 62941)
-- Dependencies: 6
-- Name: acco_usergroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL,
    headerid bigint
);


ALTER TABLE public.acco_usergroup OWNER TO postgres;

--
-- TOC entry 1714 (class 1259 OID 62944)
-- Dependencies: 6
-- Name: acco_usergroup2leaders; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup2leaders (
    ledgroupid bigint NOT NULL,
    leaderid bigint NOT NULL
);


ALTER TABLE public.acco_usergroup2leaders OWNER TO postgres;

--
-- TOC entry 1715 (class 1259 OID 62947)
-- Dependencies: 6
-- Name: acco_usergroup2members; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup2members (
    usergroupid bigint NOT NULL,
    memberid bigint NOT NULL
);


ALTER TABLE public.acco_usergroup2members OWNER TO postgres;

--
-- TOC entry 1716 (class 1259 OID 62950)
-- Dependencies: 6
-- Name: core_accessobject; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_accessobject (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL
);


ALTER TABLE public.core_accessobject OWNER TO postgres;

--
-- TOC entry 1717 (class 1259 OID 62953)
-- Dependencies: 6
-- Name: core_annotation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_annotation (
    filename character varying(254),
    legend text,
    mimetype character varying(80),
    name character varying(80),
    title character varying(254),
    attachmentid bigint NOT NULL
);


ALTER TABLE public.core_annotation OWNER TO postgres;

--
-- TOC entry 1718 (class 1259 OID 62959)
-- Dependencies: 6
-- Name: core_applicationdata; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_applicationdata (
    application character varying(80) NOT NULL,
    keyword character varying(80) NOT NULL,
    type character varying(80),
    value text NOT NULL,
    attachmentid bigint NOT NULL
);


ALTER TABLE public.core_applicationdata OWNER TO postgres;

--
-- TOC entry 1719 (class 1259 OID 62965)
-- Dependencies: 6
-- Name: core_attachment; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_attachment (
    date timestamp with time zone,
    dbid bigint NOT NULL,
    details text,
    authorid bigint,
    parententryid bigint NOT NULL
);


ALTER TABLE public.core_attachment OWNER TO postgres;

--
-- TOC entry 1721 (class 1259 OID 62974)
-- Dependencies: 6
-- Name: core_bookcitation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_bookcitation (
    bookseries character varying(254),
    booktitle character varying(254),
    isbn character varying(80),
    publisher character varying(254),
    publishercity character varying(254),
    volume character varying(80),
    citationid bigint NOT NULL
);


ALTER TABLE public.core_bookcitation OWNER TO postgres;

--
-- TOC entry 1722 (class 1259 OID 62980)
-- Dependencies: 6
-- Name: core_citation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_citation (
    authors text,
    editors text,
    firstpage character varying(80),
    lastpage character varying(80),
    status character varying(80),
    title character varying,
    year integer,
    attachmentid bigint NOT NULL
);


ALTER TABLE public.core_citation OWNER TO postgres;

--
-- TOC entry 1723 (class 1259 OID 62986)
-- Dependencies: 6
-- Name: core_conferencecitation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_conferencecitation (
    abstractnumber character varying(80),
    city character varying(80),
    conferencesite character varying(80),
    conferencetitle character varying(254),
    country character varying(80),
    enddate timestamp with time zone,
    startdate timestamp with time zone,
    stateprovince character varying(80),
    citationid bigint NOT NULL
);


ALTER TABLE public.core_conferencecitation OWNER TO postgres;

--
-- TOC entry 1724 (class 1259 OID 62992)
-- Dependencies: 6
-- Name: core_externaldblink; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_externaldblink (
    code character varying(80),
    release character varying(80),
    url text,
    attachmentid bigint NOT NULL,
    dbnameid bigint NOT NULL
);


ALTER TABLE public.core_externaldblink OWNER TO postgres;

--
-- TOC entry 1725 (class 1259 OID 62998)
-- Dependencies: 6
-- Name: core_journalcitation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_journalcitation (
    astm character varying(80),
    csd character varying(80),
    issn character varying(80),
    issue character varying(80),
    journalabbreviation character varying(80),
    journalfullname character varying(254),
    volume character varying(80),
    citationid bigint NOT NULL
);


ALTER TABLE public.core_journalcitation OWNER TO postgres;

--
-- TOC entry 1726 (class 1259 OID 63004)
-- Dependencies: 6
-- Name: core_labbookentry; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_labbookentry (
    creationdate timestamp with time zone,
    lastediteddate timestamp with time zone,
    dbid bigint NOT NULL,
    accessid bigint,
    details text,
    lasteditorid bigint,
    creatorid bigint
);


ALTER TABLE public.core_labbookentry OWNER TO postgres;

--
-- TOC entry 1727 (class 1259 OID 63013)
-- Dependencies: 6
-- Name: core_note; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_note (
    name character varying(80),
    attachmentid bigint NOT NULL
);


ALTER TABLE public.core_note OWNER TO postgres;

--
-- TOC entry 1728 (class 1259 OID 63016)
-- Dependencies: 6
-- Name: core_note2relatedentrys; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_note2relatedentrys (
    noteid bigint NOT NULL,
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.core_note2relatedentrys OWNER TO postgres;

--
-- TOC entry 1720 (class 1259 OID 62968)
-- Dependencies: 6
-- Name: core_systemclass; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_systemclass (
    dbid bigint NOT NULL,
    details text
);


ALTER TABLE public.core_systemclass OWNER TO postgres;

--
-- TOC entry 1729 (class 1259 OID 63019)
-- Dependencies: 6
-- Name: core_thesiscitation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_thesiscitation (
    city character varying(80),
    country character varying(80),
    institution character varying(254),
    stateprovince character varying(80),
    citationid bigint NOT NULL
);


ALTER TABLE public.core_thesiscitation OWNER TO postgres;

--
-- TOC entry 1730 (class 1259 OID 63022)
-- Dependencies: 6
-- Name: cryz_cypade_possva; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_cypade_possva (
    parameterdefinitionid bigint NOT NULL,
    possiblevalue character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.cryz_cypade_possva OWNER TO postgres;

--
-- TOC entry 1731 (class 1259 OID 63025)
-- Dependencies: 6
-- Name: cryz_dropannotation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_dropannotation (
    cmdlineparam text,
    scoredate timestamp with time zone,
    labbookentryid bigint NOT NULL,
    holderid bigint,
    scoreid bigint NOT NULL,
    imageid bigint,
    softwareid bigint,
    sampleid bigint
);


ALTER TABLE public.cryz_dropannotation OWNER TO postgres;

--
-- TOC entry 1732 (class 1259 OID 63031)
-- Dependencies: 6
-- Name: cryz_image; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_image (
    filename character varying(254) NOT NULL,
    filepath character varying(254) NOT NULL,
    mimetype character varying(80),
    labbookentryid bigint NOT NULL,
    instrumentid bigint,
    imagetypeid bigint,
    scheduledtaskid bigint,
    sampleid bigint
);


ALTER TABLE public.cryz_image OWNER TO postgres;

--
-- TOC entry 1733 (class 1259 OID 63037)
-- Dependencies: 6
-- Name: cryz_parameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_parameter (
    value character varying(254),
    labbookentryid bigint NOT NULL,
    parameterdefinitionid bigint NOT NULL,
    imageid bigint NOT NULL
);


ALTER TABLE public.cryz_parameter OWNER TO postgres;

--
-- TOC entry 1734 (class 1259 OID 63040)
-- Dependencies: 6
-- Name: cryz_parameterdefinition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_parameterdefinition (
    defaultvalue character varying(80),
    displayunit character varying(32),
    label text,
    maxvalue character varying(80),
    minvalue character varying(80),
    name character varying(80),
    paramtype character varying(32),
    unit character varying(32),
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.cryz_parameterdefinition OWNER TO postgres;

--
-- TOC entry 1735 (class 1259 OID 63046)
-- Dependencies: 6
-- Name: cryz_score; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_score (
    color character varying(80),
    name character varying(80),
    value integer NOT NULL,
    labbookentryid bigint NOT NULL,
    scoringschemeid bigint NOT NULL
);


ALTER TABLE public.cryz_score OWNER TO postgres;

--
-- TOC entry 1736 (class 1259 OID 63049)
-- Dependencies: 6
-- Name: cryz_scoringscheme; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_scoringscheme (
    name character varying(80) NOT NULL,
    version character varying(80),
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.cryz_scoringscheme OWNER TO postgres;

--
-- TOC entry 1737 (class 1259 OID 63052)
-- Dependencies: 6
-- Name: expe_experiment; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_experiment (
    enddate timestamp with time zone NOT NULL,
    islocked boolean,
    name character varying(80) NOT NULL,
    startdate timestamp with time zone NOT NULL,
    status character varying(32),
    labbookentryid bigint NOT NULL,
    protocolid bigint,
    experimenttypeid bigint NOT NULL,
    researchobjectiveid bigint,
    instrumentid bigint,
    experimentgroupid bigint,
    operatorid bigint,
    methodid bigint,
    groupid bigint
);


ALTER TABLE public.expe_experiment OWNER TO postgres;

--
-- TOC entry 1738 (class 1259 OID 63055)
-- Dependencies: 6
-- Name: expe_experimentgroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_experimentgroup (
    enddate timestamp with time zone,
    name character varying(80) NOT NULL,
    purpose character varying(80) NOT NULL,
    startdate timestamp with time zone,
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.expe_experimentgroup OWNER TO postgres;

--
-- TOC entry 1739 (class 1259 OID 63058)
-- Dependencies: 6
-- Name: expe_inputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_inputsample (
    amount double precision,
    amountdisplayunit character varying(32),
    amountunit character varying(32),
    name character varying(80),
    role character varying(80),
    labbookentryid bigint NOT NULL,
    experimentid bigint NOT NULL,
    refinputsampleid bigint,
    sampleid bigint
);


ALTER TABLE public.expe_inputsample OWNER TO postgres;

--
-- TOC entry 1740 (class 1259 OID 63061)
-- Dependencies: 6
-- Name: expe_instrument; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_instrument (
    model character varying(80),
    name character varying(80) NOT NULL,
    serialnumber character varying(80),
    labbookentryid bigint NOT NULL,
    tempdisplayunit character varying(32),
    pressuredisplayunit character varying(32),
    pressure double precision,
    temperature double precision,
    locationid bigint,
    defaultimagetypeid bigint,
    manufacturerid bigint
);


ALTER TABLE public.expe_instrument OWNER TO postgres;

--
-- TOC entry 1741 (class 1259 OID 63064)
-- Dependencies: 6
-- Name: expe_method; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_method (
    name character varying(80),
    procedure_ text,
    task character varying(80),
    labbookentryid bigint NOT NULL,
    softwareid bigint,
    instrumentid bigint
);


ALTER TABLE public.expe_method OWNER TO postgres;

--
-- TOC entry 1742 (class 1259 OID 63070)
-- Dependencies: 6
-- Name: expe_methodparameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_methodparameter (
    name character varying(80) NOT NULL,
    paramtype character varying(32),
    unit character varying(32),
    value character varying(254),
    labbookentryid bigint NOT NULL,
    methodid bigint NOT NULL
);


ALTER TABLE public.expe_methodparameter OWNER TO postgres;

--
-- TOC entry 1743 (class 1259 OID 63073)
-- Dependencies: 6
-- Name: expe_outputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_outputsample (
    amount double precision,
    amountdisplayunit character varying(32),
    amountunit character varying(32),
    name character varying(80),
    role character varying(80),
    labbookentryid bigint NOT NULL,
    experimentid bigint NOT NULL,
    refoutputsampleid bigint,
    sampleid bigint
);


ALTER TABLE public.expe_outputsample OWNER TO postgres;

--
-- TOC entry 1744 (class 1259 OID 63076)
-- Dependencies: 6
-- Name: expe_parameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_parameter (
    name character varying(80),
    paramtype character varying(32),
    unit character varying(32),
    value character varying(254),
    labbookentryid bigint NOT NULL,
    parameterdefinitionid bigint,
    experimentid bigint NOT NULL
);


ALTER TABLE public.expe_parameter OWNER TO postgres;

--
-- TOC entry 1745 (class 1259 OID 63079)
-- Dependencies: 6
-- Name: expe_software; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_software (
    name character varying(80) NOT NULL,
    vendoraddress character varying(254),
    vendorname character varying(254),
    vendorwebaddress text,
    version character varying(80) NOT NULL,
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.expe_software OWNER TO postgres;

--
-- TOC entry 1746 (class 1259 OID 63085)
-- Dependencies: 6
-- Name: expe_software_tasks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_software_tasks (
    softwareid bigint NOT NULL,
    task character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.expe_software_tasks OWNER TO postgres;

--
-- TOC entry 1749 (class 1259 OID 63092)
-- Dependencies: 6
-- Name: hold_abstractholder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_abstractholder (
    colposition integer,
    name character varying(80) NOT NULL,
    rowposition integer,
    subposition integer,
    labbookentryid bigint NOT NULL,
    abstractholderid bigint,
    holdertypeid bigint
);


ALTER TABLE public.hold_abstractholder OWNER TO postgres;

--
-- TOC entry 1750 (class 1259 OID 63095)
-- Dependencies: 6
-- Name: hold_holdca2abstholders; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdca2abstholders (
    holdercategoryid bigint NOT NULL,
    abstholderid bigint NOT NULL
);


ALTER TABLE public.hold_holdca2abstholders OWNER TO postgres;

--
-- TOC entry 1751 (class 1259 OID 63098)
-- Dependencies: 6
-- Name: hold_holdca2absthoty; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdca2absthoty (
    holdercategoryid bigint NOT NULL,
    abstractholdertypeid bigint NOT NULL
);


ALTER TABLE public.hold_holdca2absthoty OWNER TO postgres;

--
-- TOC entry 1752 (class 1259 OID 63101)
-- Dependencies: 6
-- Name: hold_holder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holder (
    enddate timestamp with time zone,
    startdate timestamp with time zone,
    abstractholderid bigint NOT NULL,
    crystalnumber integer,
    firstsampleid bigint,
    lasttaskid bigint
);


ALTER TABLE public.hold_holder OWNER TO postgres;

--
-- TOC entry 1753 (class 1259 OID 63104)
-- Dependencies: 6
-- Name: hold_holderlocation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holderlocation (
    enddate timestamp with time zone,
    startdate timestamp with time zone NOT NULL,
    labbookentryid bigint NOT NULL,
    holderid bigint NOT NULL,
    locationid bigint NOT NULL
);


ALTER TABLE public.hold_holderlocation OWNER TO postgres;

--
-- TOC entry 1754 (class 1259 OID 63107)
-- Dependencies: 6
-- Name: hold_holdertypeposition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdertypeposition (
    colposition integer,
    maxvolume double precision,
    maxvolumediplayunit character varying(32),
    name character varying(32),
    rowposition integer,
    subposition integer,
    labbookentryid bigint NOT NULL,
    holdertypeid bigint NOT NULL
);


ALTER TABLE public.hold_holdertypeposition OWNER TO postgres;

--
-- TOC entry 1755 (class 1259 OID 63110)
-- Dependencies: 6
-- Name: hold_refholder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_refholder (
    abstractholderid bigint NOT NULL
);


ALTER TABLE public.hold_refholder OWNER TO postgres;

--
-- TOC entry 1756 (class 1259 OID 63113)
-- Dependencies: 6
-- Name: hold_refholderoffset; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_refholderoffset (
    coloffset integer,
    rowoffset integer,
    suboffset integer,
    labbookentryid bigint NOT NULL,
    holderid bigint NOT NULL,
    refholderid bigint NOT NULL
);


ALTER TABLE public.hold_refholderoffset OWNER TO postgres;

--
-- TOC entry 1757 (class 1259 OID 63116)
-- Dependencies: 6
-- Name: hold_refsampleposition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_refsampleposition (
    colposition integer,
    rowposition integer,
    subposition integer,
    labbookentryid bigint NOT NULL,
    refholderid bigint NOT NULL,
    refsampleid bigint
);


ALTER TABLE public.hold_refsampleposition OWNER TO postgres;

--
-- TOC entry 1758 (class 1259 OID 63119)
-- Dependencies: 6
-- Name: inst_instty2inst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE inst_instty2inst (
    instrumenttypeid bigint NOT NULL,
    instrumentid bigint NOT NULL
);


ALTER TABLE public.inst_instty2inst OWNER TO postgres;

--
-- TOC entry 1759 (class 1259 OID 63122)
-- Dependencies: 6
-- Name: loca_location; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE loca_location (
    locationtype character varying(80),
    name character varying(80) NOT NULL,
    pressure double precision,
    pressuredisplayunit character varying(32),
    tempdisplayunit character varying(32),
    temperature double precision,
    labbookentryid bigint NOT NULL,
    locationid bigint,
    organisationid bigint
);


ALTER TABLE public.loca_location OWNER TO postgres;

--
-- TOC entry 1760 (class 1259 OID 63125)
-- Dependencies: 6
-- Name: mole_abstco_keywords; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstco_keywords (
    abstractcomponentid bigint NOT NULL,
    keyword character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.mole_abstco_keywords OWNER TO postgres;

--
-- TOC entry 1761 (class 1259 OID 63128)
-- Dependencies: 6
-- Name: mole_abstco_synonyms; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstco_synonyms (
    abstractcomponentid bigint NOT NULL,
    synonym character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.mole_abstco_synonyms OWNER TO postgres;

--
-- TOC entry 1762 (class 1259 OID 63131)
-- Dependencies: 6
-- Name: mole_abstractcomponent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstractcomponent (
    name character varying(80) NOT NULL,
    labbookentryid bigint NOT NULL,
    naturalsourceid bigint
);


ALTER TABLE public.mole_abstractcomponent OWNER TO postgres;

--
-- TOC entry 1763 (class 1259 OID 63134)
-- Dependencies: 6
-- Name: mole_compca2components; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_compca2components (
    categoryid bigint NOT NULL,
    componentid bigint NOT NULL
);


ALTER TABLE public.mole_compca2components OWNER TO postgres;

--
-- TOC entry 1764 (class 1259 OID 63137)
-- Dependencies: 6
-- Name: mole_construct; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_construct (
    constructstatus character varying(80) NOT NULL,
    function text,
    markerdetails text,
    promoterdetails text,
    resistancedetails text,
    sequencetype character varying(80),
    moleculeid bigint NOT NULL
);


ALTER TABLE public.mole_construct OWNER TO postgres;

--
-- TOC entry 1765 (class 1259 OID 63143)
-- Dependencies: 6
-- Name: mole_molecule; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_molecule (
    casnum character varying(32),
    empiricalformula character varying(80),
    isvolatile boolean,
    moltype character varying(80) NOT NULL,
    molecularmass double precision,
    seqdetails text,
    seqstring text,
    abstractcomponentid bigint NOT NULL
);


ALTER TABLE public.mole_molecule OWNER TO postgres;

--
-- TOC entry 1766 (class 1259 OID 63149)
-- Dependencies: 6
-- Name: mole_molecule2relareobel; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_molecule2relareobel (
    trialmoleculeid bigint NOT NULL,
    relatedresearchobjectiveelementid bigint NOT NULL
);


ALTER TABLE public.mole_molecule2relareobel OWNER TO postgres;

--
-- TOC entry 1767 (class 1259 OID 63152)
-- Dependencies: 6
-- Name: mole_moleculefeature; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_moleculefeature (
    endseqid integer,
    featuretype character varying(80) NOT NULL,
    name character varying(80) NOT NULL,
    ordering integer,
    startseqid integer,
    status character varying(80),
    labbookentryid bigint NOT NULL,
    refmoleculeid bigint,
    moleculeid bigint NOT NULL
);


ALTER TABLE public.mole_moleculefeature OWNER TO postgres;

--
-- TOC entry 1768 (class 1259 OID 63155)
-- Dependencies: 6
-- Name: mole_primer; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_primer (
    direction character varying(32) NOT NULL,
    gcgene character varying(80),
    isuniversal boolean NOT NULL,
    lengthongene character varying(80),
    meltingtemperature double precision,
    meltingtemperaturegene double precision,
    meltingtemperatureseller double precision,
    opticaldensity character varying(32),
    particularity text,
    restrictionsite text,
    moleculeid bigint NOT NULL
);


ALTER TABLE public.mole_primer OWNER TO postgres;

--
-- TOC entry 1769 (class 1259 OID 63161)
-- Dependencies: 6
-- Name: peop_group; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_group (
    name character varying(80),
    url character varying(254),
    labbookentryid bigint NOT NULL,
    organisationid bigint NOT NULL
);


ALTER TABLE public.peop_group OWNER TO postgres;

--
-- TOC entry 1770 (class 1259 OID 63164)
-- Dependencies: 6
-- Name: peop_orga_addresses; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_orga_addresses (
    organisationid bigint NOT NULL,
    address character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.peop_orga_addresses OWNER TO postgres;

--
-- TOC entry 1771 (class 1259 OID 63167)
-- Dependencies: 6
-- Name: peop_organisation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_organisation (
    city character varying(80),
    country character varying(80),
    emailaddress character varying(80),
    faxnumber character varying(80),
    name character varying(80) NOT NULL,
    organisationtype character varying(80),
    phonenumber character varying(80),
    postalcode character varying(80),
    url character varying(254),
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.peop_organisation OWNER TO postgres;

--
-- TOC entry 1772 (class 1259 OID 63173)
-- Dependencies: 6
-- Name: peop_persingr_phonnu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_persingr_phonnu (
    personingroupid bigint NOT NULL,
    phonenumber character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.peop_persingr_phonnu OWNER TO postgres;

--
-- TOC entry 1773 (class 1259 OID 63176)
-- Dependencies: 6
-- Name: peop_person; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_person (
    familyname character varying(80) NOT NULL,
    familytitle character varying(32),
    givenname character varying(80),
    title character varying(80),
    labbookentryid bigint NOT NULL,
    currentgroupid bigint
);


ALTER TABLE public.peop_person OWNER TO postgres;

--
-- TOC entry 1774 (class 1259 OID 63179)
-- Dependencies: 6
-- Name: peop_person_middin; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_person_middin (
    personid bigint NOT NULL,
    middleinitial character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.peop_person_middin OWNER TO postgres;

--
-- TOC entry 1775 (class 1259 OID 63182)
-- Dependencies: 6
-- Name: peop_personingroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_personingroup (
    deliveryaddress character varying(254),
    emailaddress character varying(80),
    enddate timestamp with time zone,
    faxnumber character varying(80),
    mailingaddress character varying(254),
    position_ character varying(80),
    startdate timestamp with time zone,
    labbookentryid bigint NOT NULL,
    personid bigint NOT NULL,
    groupid bigint NOT NULL
);


ALTER TABLE public.peop_personingroup OWNER TO postgres;

--
-- TOC entry 1776 (class 1259 OID 63188)
-- Dependencies: 6
-- Name: prot_parade_possva; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_parade_possva (
    parameterdefinitionid bigint NOT NULL,
    possiblevalue character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.prot_parade_possva OWNER TO postgres;

--
-- TOC entry 1777 (class 1259 OID 63191)
-- Dependencies: 6
-- Name: prot_parameterdefinition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_parameterdefinition (
    defaultvalue character varying(80),
    displayunit character varying(32),
    isgrouplevel boolean NOT NULL,
    ismandatory boolean NOT NULL,
    isresult boolean NOT NULL,
    label text,
    maxvalue character varying(80),
    minvalue character varying(80),
    name character varying(80) NOT NULL,
    paramtype character varying(32) NOT NULL,
    unit character varying(32),
    labbookentryid bigint NOT NULL,
    protocolid bigint NOT NULL,
    order_ integer
);


ALTER TABLE public.prot_parameterdefinition OWNER TO postgres;

--
-- TOC entry 1778 (class 1259 OID 63197)
-- Dependencies: 6
-- Name: prot_protocol; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_protocol (
    isforuse boolean,
    methoddescription text,
    name character varying(80) NOT NULL,
    objective character varying(254),
    labbookentryid bigint NOT NULL,
    experimenttypeid bigint NOT NULL
);


ALTER TABLE public.prot_protocol OWNER TO postgres;

--
-- TOC entry 1779 (class 1259 OID 63203)
-- Dependencies: 6
-- Name: prot_protocol_remarks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_protocol_remarks (
    protocolid bigint NOT NULL,
    remark character varying(255),
    order_ integer NOT NULL
);


ALTER TABLE public.prot_protocol_remarks OWNER TO postgres;

--
-- TOC entry 1780 (class 1259 OID 63206)
-- Dependencies: 6
-- Name: prot_refinputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_refinputsample (
    amount double precision,
    displayunit character varying(32),
    name character varying(80),
    unit character varying(32),
    labbookentryid bigint NOT NULL,
    protocolid bigint NOT NULL,
    samplecategoryid bigint NOT NULL
);


ALTER TABLE public.prot_refinputsample OWNER TO postgres;

--
-- TOC entry 1781 (class 1259 OID 63209)
-- Dependencies: 6
-- Name: prot_refoutputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_refoutputsample (
    amount double precision,
    displayunit character varying(32),
    name character varying(80),
    unit character varying(32),
    labbookentryid bigint NOT NULL,
    protocolid bigint NOT NULL,
    samplecategoryid bigint NOT NULL
);


ALTER TABLE public.prot_refoutputsample OWNER TO postgres;

--
-- TOC entry 1782 (class 1259 OID 63212)
-- Dependencies: 6
-- Name: ref_abstractholdertype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_abstractholdertype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_abstractholdertype OWNER TO postgres;

--
-- TOC entry 1783 (class 1259 OID 63215)
-- Dependencies: 6
-- Name: ref_componentcategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_componentcategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_componentcategory OWNER TO postgres;

--
-- TOC entry 1784 (class 1259 OID 63218)
-- Dependencies: 6
-- Name: ref_crystaltype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_crystaltype (
    ressubpos integer NOT NULL,
    holdertypeid bigint NOT NULL
);


ALTER TABLE public.ref_crystaltype OWNER TO postgres;

--
-- TOC entry 1785 (class 1259 OID 63221)
-- Dependencies: 6
-- Name: ref_dbname; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_dbname (
    fullname text,
    name character varying(80) NOT NULL,
    url text,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_dbname OWNER TO postgres;

--
-- TOC entry 1786 (class 1259 OID 63227)
-- Dependencies: 6
-- Name: ref_experimenttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_experimenttype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_experimenttype OWNER TO postgres;

--
-- TOC entry 1787 (class 1259 OID 63230)
-- Dependencies: 6
-- Name: ref_hazardphrase; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_hazardphrase (
    classification character varying(32) NOT NULL,
    code character varying(80) NOT NULL,
    phrase character varying(254),
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_hazardphrase OWNER TO postgres;

--
-- TOC entry 1788 (class 1259 OID 63233)
-- Dependencies: 6
-- Name: ref_holdercategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_holdercategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_holdercategory OWNER TO postgres;

--
-- TOC entry 1789 (class 1259 OID 63236)
-- Dependencies: 6
-- Name: ref_holdertype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_holdertype (
    holdholderflag boolean,
    maxcolumn integer,
    maxrow integer,
    maxsubposition integer,
    maxvolume double precision,
    maxvolumedisplayunit character varying(32),
    abstractholdertypeid bigint NOT NULL,
    defaultscheduleplanid bigint
);


ALTER TABLE public.ref_holdertype OWNER TO postgres;

--
-- TOC entry 1820 (class 1259 OID 65064)
-- Dependencies: 6
-- Name: ref_imagetype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_imagetype (
    publicentryid bigint NOT NULL,
    ylengthperpixel double precision,
    xlengthperpixel double precision,
    sizey integer,
    name character varying(80) NOT NULL,
    sizex integer,
    catorgory character varying(180),
    colourdepth integer,
    url character varying(180)
);


ALTER TABLE public.ref_imagetype OWNER TO postgres;

--
-- TOC entry 1790 (class 1259 OID 63239)
-- Dependencies: 6
-- Name: ref_instrumenttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_instrumenttype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_instrumenttype OWNER TO postgres;

--
-- TOC entry 1791 (class 1259 OID 63242)
-- Dependencies: 6
-- Name: ref_organism; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_organism (
    atccnumber character varying(80),
    cellline character varying(80),
    celllocation text,
    celltype character varying(80),
    fraction character varying(254),
    genemnemonic character varying(254),
    genus character varying(80),
    ictvcode character varying(254),
    name character varying(80) NOT NULL,
    ncbitaxonomyid character varying(254),
    organ character varying(80),
    organelle character varying(254),
    organismacronym character varying(254),
    plasmid character varying(254),
    plasmiddetails text,
    scientificname character varying(80),
    secretion text,
    species character varying(80),
    strain character varying(80),
    subvariant character varying(254),
    tissue character varying(80),
    variant character varying(80),
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_organism OWNER TO postgres;

--
-- TOC entry 1792 (class 1259 OID 63248)
-- Dependencies: 6
-- Name: ref_pintype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_pintype (
    length double precision,
    looplength double precision,
    looptype character varying(80),
    wirewidth double precision,
    abstractholdertypeid bigint NOT NULL
);


ALTER TABLE public.ref_pintype OWNER TO postgres;

--
-- TOC entry 1793 (class 1259 OID 63251)
-- Dependencies: 6
-- Name: ref_publicentry; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_publicentry (
    dbid bigint NOT NULL,
    details text
);


ALTER TABLE public.ref_publicentry OWNER TO postgres;

--
-- TOC entry 1794 (class 1259 OID 63254)
-- Dependencies: 6
-- Name: ref_samplecategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_samplecategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_samplecategory OWNER TO postgres;

--
-- TOC entry 1795 (class 1259 OID 63257)
-- Dependencies: 6
-- Name: ref_targetstatus; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_targetstatus (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


ALTER TABLE public.ref_targetstatus OWNER TO postgres;

--
-- TOC entry 1796 (class 1259 OID 63260)
-- Dependencies: 6
-- Name: ref_workflowitem; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_workflowitem (
    publicentryid bigint NOT NULL,
    experimenttypeid bigint NOT NULL,
    statusid bigint
);


ALTER TABLE public.ref_workflowitem OWNER TO postgres;

--
-- TOC entry 1797 (class 1259 OID 63263)
-- Dependencies: 6
-- Name: revisionnumber; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE revisionnumber (
    revision integer NOT NULL,
    name character varying(254) NOT NULL,
    release character varying(254) NOT NULL,
    tag character varying(254) NOT NULL,
    author character varying(254) NOT NULL,
    date character varying(254) NOT NULL
);


ALTER TABLE public.revisionnumber OWNER TO postgres;

--
-- TOC entry 1798 (class 1259 OID 63269)
-- Dependencies: 6
-- Name: sam_abstractsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_abstractsample (
    ionicstrength double precision,
    isactive boolean,
    ishazard boolean,
    name character varying(80) NOT NULL,
    ph double precision,
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.sam_abstractsample OWNER TO postgres;

--
-- TOC entry 1799 (class 1259 OID 63272)
-- Dependencies: 6
-- Name: sam_abstsa2hazaph; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_abstsa2hazaph (
    otherrole bigint NOT NULL,
    hazardphraseid bigint NOT NULL
);


ALTER TABLE public.sam_abstsa2hazaph OWNER TO postgres;

--
-- TOC entry 1800 (class 1259 OID 63275)
-- Dependencies: 6
-- Name: sam_crystalsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_crystalsample (
    a double precision,
    alpha double precision,
    b double precision,
    beta double precision,
    c double precision,
    colour character varying(254),
    crystaltype character varying(254),
    gamma double precision,
    morphology character varying(254),
    spacegroup character varying(32),
    x double precision,
    y double precision,
    z double precision,
    sampleid bigint NOT NULL
);


ALTER TABLE public.sam_crystalsample OWNER TO postgres;

--
-- TOC entry 1801 (class 1259 OID 63281)
-- Dependencies: 6
-- Name: sam_refsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_refsample (
    abstractsampleid bigint NOT NULL,
    issaltcrystal boolean
);


ALTER TABLE public.sam_refsample OWNER TO postgres;

--
-- TOC entry 1802 (class 1259 OID 63284)
-- Dependencies: 6
-- Name: sam_refsamplesource; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_refsamplesource (
    catalognum character varying(80) NOT NULL,
    datapageurl character varying(254),
    productgroupname character varying(254),
    productname character varying(254),
    labbookentryid bigint NOT NULL,
    refsampleid bigint,
    supplierid bigint NOT NULL,
    refholderid bigint
);


ALTER TABLE public.sam_refsamplesource OWNER TO postgres;

--
-- TOC entry 1803 (class 1259 OID 63290)
-- Dependencies: 6
-- Name: sam_sampca2abstsa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_sampca2abstsa (
    samplecategoryid bigint NOT NULL,
    abstractsampleid bigint NOT NULL
);


ALTER TABLE public.sam_sampca2abstsa OWNER TO postgres;

--
-- TOC entry 1804 (class 1259 OID 63293)
-- Dependencies: 6
-- Name: sam_sample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_sample (
    amountdisplayunit character varying(32),
    amountunit character varying(32),
    batchnum character varying(32),
    colposition integer,
    currentamount double precision,
    currentamountflag boolean,
    initialamount double precision,
    rowposition integer,
    subposition integer,
    usebydate timestamp with time zone,
    abstractsampleid bigint NOT NULL,
    assigntoid bigint,
    holderid bigint,
    refsampleid bigint
);


ALTER TABLE public.sam_sample OWNER TO postgres;

--
-- TOC entry 1805 (class 1259 OID 63296)
-- Dependencies: 6
-- Name: sam_samplecomponent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_samplecomponent (
    concdisplayunit character varying(32),
    concentration double precision,
    concentrationerror double precision,
    concentrationunit character varying(32),
    ph double precision,
    purity double precision,
    labbookentryid bigint NOT NULL,
    abstractsampleid bigint NOT NULL,
    refcomponentid bigint NOT NULL,
    researchobjectivelementid bigint,
    containerid bigint
);


ALTER TABLE public.sam_samplecomponent OWNER TO postgres;

--
-- TOC entry 1806 (class 1259 OID 63299)
-- Dependencies: 6
-- Name: sche_scheduledtask; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduledtask (
    priority integer,
    scheduledtime timestamp with time zone NOT NULL,
    state integer,
    labbookentryid bigint NOT NULL,
    completiontime timestamp with time zone,
    name character varying(80) NOT NULL,
    holderid bigint NOT NULL,
    scheduleplanoffsetid bigint,
    instrumentid bigint
);


ALTER TABLE public.sche_scheduledtask OWNER TO postgres;

--
-- TOC entry 1807 (class 1259 OID 63302)
-- Dependencies: 6
-- Name: sche_scheduleplan; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduleplan (
    name character varying(80) NOT NULL,
    labbookentryid bigint NOT NULL
);


ALTER TABLE public.sche_scheduleplan OWNER TO postgres;

--
-- TOC entry 1808 (class 1259 OID 63305)
-- Dependencies: 6
-- Name: sche_scheduleplanoffset; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduleplanoffset (
    offsettime bigint NOT NULL,
    priority integer,
    labbookentryid bigint NOT NULL,
    order_ integer,
    scheduleplanid bigint NOT NULL
);


ALTER TABLE public.sche_scheduleplanoffset OWNER TO postgres;

--
-- TOC entry 1819 (class 1259 OID 64704)
-- Dependencies: 6
-- Name: targ_alias; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_alias (
    labbookentryid bigint NOT NULL,
    name character varying(255) NOT NULL,
    targetid bigint NOT NULL
);


ALTER TABLE public.targ_alias OWNER TO postgres;

--
-- TOC entry 1809 (class 1259 OID 63308)
-- Dependencies: 6
-- Name: targ_milestone; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_milestone (
    date_ timestamp with time zone NOT NULL,
    labbookentryid bigint NOT NULL,
    statusid bigint NOT NULL,
    experimentid bigint,
    targetid bigint NOT NULL
);


ALTER TABLE public.targ_milestone OWNER TO postgres;

--
-- TOC entry 1810 (class 1259 OID 63311)
-- Dependencies: 6
-- Name: targ_project; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_project (
    completename text NOT NULL,
    shortname character varying(80) NOT NULL,
    startdate timestamp with time zone,
    labbookentryid bigint NOT NULL,
    projectid bigint
);


ALTER TABLE public.targ_project OWNER TO postgres;

--
-- TOC entry 1811 (class 1259 OID 63317)
-- Dependencies: 6
-- Name: targ_researchobjective; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_researchobjective (
    biochemicalfunction character varying(254),
    biologicalprocess character varying(254),
    catalyticactivity character varying(254),
    celllocation character varying(80),
    commonname character varying(80) NOT NULL,
    functiondescription text,
    localname character varying(80),
    pathway character varying(254),
    similaritydetails text,
    systematicname character varying(80),
    whychosen text,
    labbookentryid bigint NOT NULL,
    ownerid bigint
);


ALTER TABLE public.targ_researchobjective OWNER TO postgres;

--
-- TOC entry 1812 (class 1259 OID 63323)
-- Dependencies: 6
-- Name: targ_target; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_target (
    biochemicalfunction character varying(254),
    biologicalprocess character varying(254),
    catalyticactivity character varying(254),
    celllocation character varying(80),
    functiondescription text,
    genename character varying(80),
    name character varying(80) NOT NULL,
    orf character varying(80),
    pathway character varying(254),
    similaritydetails text,
    topology character varying(254),
    whychosen text,
    labbookentryid bigint NOT NULL,
    speciesid bigint,
    proteinid bigint NOT NULL
);


ALTER TABLE public.targ_target OWNER TO postgres;

--
-- TOC entry 1813 (class 1259 OID 63329)
-- Dependencies: 6
-- Name: targ_target2nuclac; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_target2nuclac (
    nuctargetid bigint NOT NULL,
    nucleicacidid bigint NOT NULL
);


ALTER TABLE public.targ_target2nuclac OWNER TO postgres;

--
-- TOC entry 1814 (class 1259 OID 63332)
-- Dependencies: 6
-- Name: targ_target2projects; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_target2projects (
    targetid bigint NOT NULL,
    projectid bigint NOT NULL
);


ALTER TABLE public.targ_target2projects OWNER TO postgres;

--
-- TOC entry 1815 (class 1259 OID 63338)
-- Dependencies: 6
-- Name: targ_targetgroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_targetgroup (
    completename text,
    groupingtype character varying(80),
    namingsystem character varying(32) NOT NULL,
    shortname character varying(32) NOT NULL,
    labbookentryid bigint NOT NULL,
    targetgroupid bigint
);


ALTER TABLE public.targ_targetgroup OWNER TO postgres;

--
-- TOC entry 1816 (class 1259 OID 63344)
-- Dependencies: 6
-- Name: targ_targgr2targets; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_targgr2targets (
    targetgroupid bigint NOT NULL,
    targetid bigint NOT NULL
);


ALTER TABLE public.targ_targgr2targets OWNER TO postgres;

--
-- TOC entry 1818 (class 1259 OID 63349)
-- Dependencies: 6
-- Name: trag_researchobjectiveelement; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE trag_researchobjectiveelement (
    alwaysincluded boolean,
    approxbeginseqid integer,
    approxendseqid integer,
    componenttype character varying(32) NOT NULL,
    domain character varying(80),
    status character varying(80),
    whychosen text NOT NULL,
    labbookentryid bigint NOT NULL,
    researchobjectiveid bigint NOT NULL,
    targetid bigint,
    moleculeid bigint
);


ALTER TABLE public.trag_researchobjectiveelement OWNER TO postgres;

--
-- TOC entry 1747 (class 1259 OID 63088)
-- Dependencies: 6
-- Name: generic_target; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE generic_target
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.generic_target OWNER TO postgres;

--
-- TOC entry 3021 (class 0 OID 0)
-- Dependencies: 1747
-- Name: generic_target; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('generic_target', 1, false);


--
-- TOC entry 1748 (class 1259 OID 63090)
-- Dependencies: 6
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 3023 (class 0 OID 0)
-- Dependencies: 1748
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 40000, true);


--
-- TOC entry 1817 (class 1259 OID 63347)
-- Dependencies: 6
-- Name: test_target; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE test_target
    START WITH 11
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.test_target OWNER TO postgres;

--
-- TOC entry 3025 (class 0 OID 0)
-- Dependencies: 1817
-- Name: test_target; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('test_target', 11, false);


--
-- TOC entry 2803 (class 0 OID 62935)
-- Dependencies: 1711
-- Data for Name: acco_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_permission (optype, permission, permissionclass, rolename, systemclassid, accessobjectid, usergroupid) FROM stdin;
update	t	PIMS	any	8006	8002	8004
read	t	PIMS	any	8007	8002	8004
create	t	PIMS	any	8008	8002	8004
\.


--
-- TOC entry 2804 (class 0 OID 62938)
-- Dependencies: 1712
-- Data for Name: acco_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_user (name, systemclassid, personid) FROM stdin;
\.


--
-- TOC entry 2805 (class 0 OID 62941)
-- Dependencies: 1713
-- Data for Name: acco_usergroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup (name, systemclassid, headerid) FROM stdin;
public	8004	\N
\.


--
-- TOC entry 2806 (class 0 OID 62944)
-- Dependencies: 1714
-- Data for Name: acco_usergroup2leaders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup2leaders (ledgroupid, leaderid) FROM stdin;
\.


--
-- TOC entry 2807 (class 0 OID 62947)
-- Dependencies: 1715
-- Data for Name: acco_usergroup2members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup2members (usergroupid, memberid) FROM stdin;
\.


--
-- TOC entry 2808 (class 0 OID 62950)
-- Dependencies: 1716
-- Data for Name: core_accessobject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_accessobject (name, systemclassid) FROM stdin;
public	8002
\.


--
-- TOC entry 2809 (class 0 OID 62953)
-- Dependencies: 1717
-- Data for Name: core_annotation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_annotation (filename, legend, mimetype, name, title, attachmentid) FROM stdin;
\.


--
-- TOC entry 2810 (class 0 OID 62959)
-- Dependencies: 1718
-- Data for Name: core_applicationdata; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_applicationdata (application, keyword, type, value, attachmentid) FROM stdin;
\.


--
-- TOC entry 2811 (class 0 OID 62965)
-- Dependencies: 1719
-- Data for Name: core_attachment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_attachment (date, dbid, details, authorid, parententryid) FROM stdin;
\.


--
-- TOC entry 2813 (class 0 OID 62974)
-- Dependencies: 1721
-- Data for Name: core_bookcitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_bookcitation (bookseries, booktitle, isbn, publisher, publishercity, volume, citationid) FROM stdin;
\.


--
-- TOC entry 2814 (class 0 OID 62980)
-- Dependencies: 1722
-- Data for Name: core_citation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_citation (authors, editors, firstpage, lastpage, status, title, year, attachmentid) FROM stdin;
\.


--
-- TOC entry 2815 (class 0 OID 62986)
-- Dependencies: 1723
-- Data for Name: core_conferencecitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_conferencecitation (abstractnumber, city, conferencesite, conferencetitle, country, enddate, startdate, stateprovince, citationid) FROM stdin;
\.


--
-- TOC entry 2816 (class 0 OID 62992)
-- Dependencies: 1724
-- Data for Name: core_externaldblink; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_externaldblink (code, release, url, attachmentid, dbnameid) FROM stdin;
\.


--
-- TOC entry 2817 (class 0 OID 62998)
-- Dependencies: 1725
-- Data for Name: core_journalcitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_journalcitation (astm, csd, issn, issue, journalabbreviation, journalfullname, volume, citationid) FROM stdin;
\.


--
-- TOC entry 2818 (class 0 OID 63004)
-- Dependencies: 1726
-- Data for Name: core_labbookentry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_labbookentry (creationdate, lastediteddate, dbid, accessid, details, lasteditorid, creatorid) FROM stdin;
2009-06-10 09:55:15.531+01	\N	38005	\N	\N	\N	\N
2009-06-10 09:55:15.625+01	\N	38006	\N	\N	\N	\N
2009-06-10 09:55:15.64+01	\N	38007	\N	\N	\N	\N
2009-06-10 09:55:15.656+01	\N	38008	\N	\N	\N	\N
2009-06-10 09:55:15.656+01	\N	38009	\N	\N	\N	\N
2009-06-10 09:55:15.671+01	\N	38010	\N	\N	\N	\N
2009-06-10 09:55:15.687+01	\N	38011	\N	\N	\N	\N
\N	\N	38018	\N	\N	\N	\N
2009-06-10 09:55:15.953+01	\N	38019	\N	\N	\N	\N
2009-06-10 09:55:15.968+01	\N	38020	\N	\N	\N	\N
2009-02-12 14:59:23.237+00	\N	22565	\N	\N	\N	\N
2009-02-12 15:00:45.434+00	\N	23727	\N	\N	\N	\N
2009-02-12 15:01:18.14+00	\N	24017	\N	\N	\N	\N
2009-02-12 14:57:41.30+00	\N	8001	8002	\N	\N	\N
2009-02-12 14:58:12.776+00	\N	14001	\N	\N	\N	\N
2009-02-12 14:58:12.853+00	\N	14003	\N	\N	\N	\N
2009-02-12 14:58:12.87+00	\N	14005	\N	\N	\N	\N
2009-02-12 14:58:12.889+00	\N	14007	\N	\N	\N	\N
2009-02-12 14:58:12.91+00	\N	14009	\N	\N	\N	\N
2009-02-12 14:58:12.933+00	\N	14011	\N	\N	\N	\N
2009-02-12 14:58:12.955+00	\N	14013	\N	\N	\N	\N
2009-02-12 14:58:12.98+00	\N	14015	\N	\N	\N	\N
2009-02-12 14:58:13.005+00	\N	14017	\N	\N	\N	\N
2009-02-12 14:58:13.033+00	\N	14019	\N	\N	\N	\N
2009-02-12 14:58:13.062+00	\N	14021	\N	\N	\N	\N
2009-02-12 14:58:13.092+00	\N	14023	\N	\N	\N	\N
2009-02-12 14:58:13.123+00	\N	14025	\N	\N	\N	\N
2009-02-12 14:58:13.156+00	\N	14027	\N	\N	\N	\N
2009-02-12 14:58:13.271+00	\N	14029	\N	\N	\N	\N
2009-02-12 14:58:13.307+00	\N	14031	\N	\N	\N	\N
2009-02-12 14:58:13.345+00	\N	14033	\N	\N	\N	\N
2009-02-12 14:58:13.384+00	\N	14035	\N	\N	\N	\N
2009-02-12 14:58:13.423+00	\N	14037	\N	\N	\N	\N
2009-02-12 14:58:13.465+00	\N	14039	\N	\N	\N	\N
2009-02-12 14:58:13.507+00	\N	14041	\N	\N	\N	\N
2009-02-12 14:58:13.551+00	\N	14043	\N	\N	\N	\N
2009-02-12 14:58:13.597+00	\N	14045	\N	\N	\N	\N
2009-02-12 14:58:13.643+00	\N	14047	\N	\N	\N	\N
2009-02-12 14:58:13.692+00	\N	14049	\N	\N	\N	\N
2009-02-12 14:58:13.745+00	\N	14051	\N	\N	\N	\N
2009-02-12 14:58:13.799+00	\N	14053	\N	\N	\N	\N
2009-02-12 14:58:13.853+00	\N	14055	\N	\N	\N	\N
2009-02-12 14:58:13.907+00	\N	14057	\N	\N	\N	\N
2009-02-12 14:58:13.963+00	\N	14059	\N	\N	\N	\N
2009-02-12 14:58:14.019+00	\N	14061	\N	\N	\N	\N
2009-02-12 14:58:14.078+00	\N	14063	\N	\N	\N	\N
2009-02-12 14:58:14.138+00	\N	14065	\N	\N	\N	\N
2009-02-12 14:58:23.481+00	\N	16001	\N	\N	\N	\N
2009-02-12 14:58:23.569+00	\N	16003	\N	\N	\N	\N
2009-02-12 14:58:23.589+00	\N	16004	\N	\N	\N	\N
2009-02-12 14:58:23.612+00	\N	16006	\N	\N	\N	\N
2009-02-12 14:58:23.63+00	\N	16007	\N	\N	\N	\N
2009-02-12 14:58:23.654+00	\N	16009	\N	\N	\N	\N
2009-02-12 14:58:23.675+00	\N	16010	\N	\N	\N	\N
2009-02-12 14:58:23.702+00	\N	16012	\N	\N	\N	\N
2009-02-12 14:58:23.727+00	\N	16013	\N	\N	\N	\N
2009-02-12 14:58:23.757+00	\N	16015	\N	\N	\N	\N
2009-02-12 14:58:23.784+00	\N	16016	\N	\N	\N	\N
2009-02-12 14:58:23.816+00	\N	16018	\N	\N	\N	\N
2009-02-12 14:58:23.845+00	\N	16019	\N	\N	\N	\N
2009-02-12 14:58:23.88+00	\N	16021	\N	\N	\N	\N
2009-02-12 14:58:23.911+00	\N	16022	\N	\N	\N	\N
2009-02-12 14:58:23.948+00	\N	16024	\N	\N	\N	\N
2009-02-12 14:58:23.982+00	\N	16025	\N	\N	\N	\N
2009-02-12 14:58:24.023+00	\N	16027	\N	\N	\N	\N
2009-02-12 14:58:24.06+00	\N	16028	\N	\N	\N	\N
2009-02-12 14:58:24.103+00	\N	16030	\N	\N	\N	\N
2009-02-12 14:58:24.17+00	\N	16031	\N	\N	\N	\N
2009-02-12 14:58:24.213+00	\N	16032	\N	\N	\N	\N
2009-02-12 14:58:24.932+00	\N	16034	\N	\N	\N	\N
2009-02-12 14:58:24.975+00	\N	16035	\N	\N	\N	\N
2009-02-12 14:58:25.024+00	\N	16037	\N	\N	\N	\N
2009-02-12 14:58:25.07+00	\N	16038	\N	\N	\N	\N
2009-02-12 14:58:25.121+00	\N	16040	\N	\N	\N	\N
2009-02-12 14:58:25.168+00	\N	16041	\N	\N	\N	\N
2009-02-12 14:58:25.225+00	\N	16043	\N	\N	\N	\N
2009-02-12 14:58:25.276+00	\N	16044	\N	\N	\N	\N
2009-02-12 14:58:25.334+00	\N	16046	\N	\N	\N	\N
2009-02-12 14:58:25.39+00	\N	16047	\N	\N	\N	\N
2009-02-12 14:58:25.449+00	\N	16049	\N	\N	\N	\N
2009-02-12 14:58:25.512+00	\N	16051	\N	\N	\N	\N
2009-02-12 14:58:25.57+00	\N	16052	\N	\N	\N	\N
2009-02-12 14:58:25.634+00	\N	16054	\N	\N	\N	\N
2009-02-12 14:58:25.696+00	\N	16055	\N	\N	\N	\N
2009-02-12 14:58:25.782+00	\N	16057	\N	\N	\N	\N
2009-02-12 14:58:25.867+00	\N	16058	\N	\N	\N	\N
2009-02-12 14:58:25.957+00	\N	16060	\N	\N	\N	\N
2009-02-12 14:58:26.044+00	\N	16061	\N	\N	\N	\N
2009-02-12 14:58:26.111+00	\N	16063	\N	\N	\N	\N
2009-02-12 14:58:26.208+00	\N	16064	\N	\N	\N	\N
2009-02-12 14:58:26.306+00	\N	16066	\N	\N	\N	\N
2009-02-12 14:58:26.374+00	\N	16067	\N	\N	\N	\N
2009-02-12 14:58:26.447+00	\N	16069	\N	\N	\N	\N
2009-02-12 14:58:26.517+00	\N	16070	\N	\N	\N	\N
2009-02-12 14:58:26.591+00	\N	16072	\N	\N	\N	\N
2009-02-12 14:58:26.662+00	\N	16073	\N	\N	\N	\N
2009-02-12 14:58:26.742+00	\N	16075	\N	\N	\N	\N
2009-02-12 14:58:26.793+00	\N	16076	\N	\N	\N	\N
2009-02-12 14:58:26.844+00	\N	16078	\N	\N	\N	\N
2009-02-12 14:58:26.889+00	\N	16079	\N	\N	\N	\N
2009-02-12 14:58:26.935+00	\N	16081	\N	\N	\N	\N
2009-02-12 14:58:26.974+00	\N	16082	\N	\N	\N	\N
2009-02-12 14:58:27.014+00	\N	16084	\N	\N	\N	\N
2009-02-12 14:58:27.049+00	\N	16085	\N	\N	\N	\N
2009-02-12 14:58:27.087+00	\N	16087	\N	\N	\N	\N
2009-02-12 14:58:27.122+00	\N	16088	\N	\N	\N	\N
2009-02-12 14:58:27.161+00	\N	16090	\N	\N	\N	\N
2009-02-12 14:58:27.196+00	\N	16091	\N	\N	\N	\N
2009-02-12 14:58:27.236+00	\N	16093	\N	\N	\N	\N
2009-02-12 14:58:27.275+00	\N	16095	\N	\N	\N	\N
2009-02-12 14:58:27.309+00	\N	16097	\N	\N	\N	\N
2009-02-12 14:58:27.341+00	\N	16098	\N	\N	\N	\N
2009-02-12 14:58:27.377+00	\N	16100	\N	\N	\N	\N
2009-02-12 14:58:27.408+00	\N	16101	\N	\N	\N	\N
2009-02-12 14:58:27.441+00	\N	16103	\N	\N	\N	\N
2009-02-12 14:58:27.469+00	\N	16104	\N	\N	\N	\N
2009-02-12 14:58:27.50+00	\N	16106	\N	\N	\N	\N
2009-02-12 14:58:27.528+00	\N	16107	\N	\N	\N	\N
2009-02-12 14:58:27.559+00	\N	16109	\N	\N	\N	\N
2009-02-12 14:58:27.586+00	\N	16110	\N	\N	\N	\N
2009-02-12 14:58:27.67+00	\N	16112	\N	\N	\N	\N
2009-02-12 14:58:27.695+00	\N	16113	\N	\N	\N	\N
2009-02-12 14:58:27.724+00	\N	16115	\N	\N	\N	\N
2009-02-12 14:58:27.749+00	\N	16116	\N	\N	\N	\N
2009-02-12 14:58:27.778+00	\N	16118	\N	\N	\N	\N
2009-02-12 14:58:27.804+00	\N	16119	\N	\N	\N	\N
2009-02-12 14:58:27.832+00	\N	16121	\N	\N	\N	\N
2009-02-12 14:58:27.858+00	\N	16122	\N	\N	\N	\N
2009-02-12 14:58:27.886+00	\N	16124	\N	\N	\N	\N
2009-02-12 14:58:27.912+00	\N	16125	\N	\N	\N	\N
2009-02-12 14:58:27.944+00	\N	16127	\N	\N	\N	\N
2009-02-12 14:58:27.968+00	\N	16128	\N	\N	\N	\N
2009-02-12 14:58:27.995+00	\N	16130	\N	\N	\N	\N
2009-02-12 14:58:28.019+00	\N	16131	\N	\N	\N	\N
2009-02-12 14:58:28.045+00	\N	16133	\N	\N	\N	\N
2009-02-12 14:58:28.069+00	\N	16134	\N	\N	\N	\N
2009-02-12 14:58:28.095+00	\N	16136	\N	\N	\N	\N
2009-02-12 14:58:28.119+00	\N	16137	\N	\N	\N	\N
2009-02-12 14:58:28.144+00	\N	16139	\N	\N	\N	\N
2009-02-12 14:58:28.168+00	\N	16140	\N	\N	\N	\N
2009-02-12 14:58:28.194+00	\N	16142	\N	\N	\N	\N
2009-02-12 14:58:28.219+00	\N	16143	\N	\N	\N	\N
2009-02-12 14:58:28.244+00	\N	16145	\N	\N	\N	\N
2009-02-12 14:58:28.268+00	\N	16146	\N	\N	\N	\N
2009-02-12 14:58:28.294+00	\N	16148	\N	\N	\N	\N
2009-02-12 14:58:28.317+00	\N	16149	\N	\N	\N	\N
2009-02-12 14:58:28.343+00	\N	16151	\N	\N	\N	\N
2009-02-12 14:58:28.366+00	\N	16152	\N	\N	\N	\N
2009-02-12 14:58:28.39+00	\N	16154	\N	\N	\N	\N
2009-02-12 14:58:28.415+00	\N	16155	\N	\N	\N	\N
2009-02-12 14:58:28.44+00	\N	16157	\N	\N	\N	\N
2009-02-12 14:58:28.462+00	\N	16158	\N	\N	\N	\N
2009-02-12 14:58:28.487+00	\N	16160	\N	\N	\N	\N
2009-02-12 14:58:28.509+00	\N	16161	\N	\N	\N	\N
2009-02-12 14:58:28.534+00	\N	16163	\N	\N	\N	\N
2009-02-12 14:58:28.557+00	\N	16164	\N	\N	\N	\N
2009-02-12 14:58:28.583+00	\N	16166	\N	\N	\N	\N
2009-02-12 14:58:28.606+00	\N	16167	\N	\N	\N	\N
2009-02-12 14:58:28.631+00	\N	16169	\N	\N	\N	\N
2009-02-12 14:58:28.654+00	\N	16170	\N	\N	\N	\N
2009-02-12 14:58:28.681+00	\N	16172	\N	\N	\N	\N
2009-02-12 14:58:28.704+00	\N	16173	\N	\N	\N	\N
2009-02-12 14:58:28.732+00	\N	16175	\N	\N	\N	\N
2009-02-12 14:58:28.755+00	\N	16176	\N	\N	\N	\N
2009-02-12 14:58:28.793+00	\N	16178	\N	\N	\N	\N
2009-02-12 14:58:28.816+00	\N	16179	\N	\N	\N	\N
2009-02-12 14:58:28.841+00	\N	16181	\N	\N	\N	\N
2009-02-12 14:58:28.864+00	\N	16182	\N	\N	\N	\N
2009-02-12 14:58:28.889+00	\N	16184	\N	\N	\N	\N
2009-02-12 14:58:28.913+00	\N	16185	\N	\N	\N	\N
2009-02-12 14:58:28.939+00	\N	16187	\N	\N	\N	\N
2009-02-12 14:58:28.966+00	\N	16188	\N	\N	\N	\N
2009-02-12 14:58:28.993+00	\N	16190	\N	\N	\N	\N
2009-02-12 14:58:29.016+00	\N	16191	\N	\N	\N	\N
2009-02-12 14:58:29.042+00	\N	16193	\N	\N	\N	\N
2009-02-12 14:58:29.065+00	\N	16194	\N	\N	\N	\N
2009-02-12 14:58:29.09+00	\N	16196	\N	\N	\N	\N
2009-02-12 14:58:29.113+00	\N	16197	\N	\N	\N	\N
2009-02-12 14:58:29.136+00	\N	16199	\N	\N	\N	\N
2009-02-12 14:58:29.161+00	\N	16201	\N	\N	\N	\N
2009-02-12 14:58:29.184+00	\N	16202	\N	\N	\N	\N
2009-02-12 14:58:29.211+00	\N	16204	\N	\N	\N	\N
2009-02-12 14:58:29.232+00	\N	16205	\N	\N	\N	\N
2009-02-12 14:58:29.257+00	\N	16207	\N	\N	\N	\N
2009-02-12 14:58:29.321+00	\N	16208	\N	\N	\N	\N
2009-02-12 14:58:29.345+00	\N	16210	\N	\N	\N	\N
2009-02-12 14:58:29.366+00	\N	16211	\N	\N	\N	\N
2009-02-12 14:58:29.389+00	\N	16213	\N	\N	\N	\N
2009-02-12 14:58:29.41+00	\N	16214	\N	\N	\N	\N
2009-02-12 14:58:29.433+00	\N	16216	\N	\N	\N	\N
2009-02-12 14:58:29.479+00	\N	16220	\N	\N	\N	\N
2009-02-12 14:58:29.502+00	\N	16222	\N	\N	\N	\N
2009-02-12 14:58:29.526+00	\N	16224	\N	\N	\N	\N
2009-02-12 14:58:29.551+00	\N	16226	\N	\N	\N	\N
2009-02-12 14:58:29.574+00	\N	16228	\N	\N	\N	\N
2009-02-12 14:58:29.598+00	\N	16230	\N	\N	\N	\N
2009-02-12 14:58:29.621+00	\N	16232	\N	\N	\N	\N
2009-02-12 14:58:29.668+00	\N	16236	\N	\N	\N	\N
2009-02-12 14:58:29.693+00	\N	16238	\N	\N	\N	\N
2009-02-12 14:58:29.719+00	\N	16240	\N	\N	\N	\N
2009-02-12 14:58:29.744+00	\N	16242	\N	\N	\N	\N
2009-02-12 14:58:29.769+00	\N	16244	\N	\N	\N	\N
2009-02-12 14:58:29.794+00	\N	16246	\N	\N	\N	\N
2009-02-12 14:58:29.818+00	\N	16247	\N	\N	\N	\N
2009-02-12 14:58:29.844+00	\N	16249	\N	\N	\N	\N
2009-02-12 14:58:29.867+00	\N	16250	\N	\N	\N	\N
2009-02-12 14:58:29.893+00	\N	16252	\N	\N	\N	\N
2009-02-12 14:58:29.916+00	\N	16253	\N	\N	\N	\N
2009-02-12 14:58:29.942+00	\N	16255	\N	\N	\N	\N
2009-02-12 14:59:01.637+00	2009-02-12 14:59:01.695+00	22001	\N	\N	\N	\N
2009-02-12 14:59:01.743+00	\N	22003	\N	\N	\N	\N
2009-02-12 14:59:01.756+00	\N	22005	\N	\N	\N	\N
2009-02-12 14:59:01.799+00	2009-02-12 14:59:01.816+00	22006	\N	\N	\N	\N
2009-02-12 14:59:01.877+00	\N	22008	\N	\N	\N	\N
2009-02-12 14:59:01.893+00	\N	22010	\N	\N	\N	\N
2009-02-12 14:59:01.931+00	2009-02-12 14:59:01.95+00	22011	\N	\N	\N	\N
2009-02-12 14:59:02.027+00	\N	22013	\N	\N	\N	\N
2009-02-12 14:59:02.043+00	\N	22015	\N	\N	\N	\N
2009-02-12 14:59:02.091+00	2009-02-12 14:59:02.107+00	22016	\N	\N	\N	\N
2009-02-12 14:59:02.201+00	\N	22018	\N	\N	\N	\N
2009-02-12 14:59:02.222+00	\N	22020	\N	\N	\N	\N
2009-02-12 14:59:02.289+00	2009-02-12 14:59:02.308+00	22021	\N	\N	\N	\N
2009-02-12 14:59:02.358+00	\N	22023	\N	\N	\N	\N
2009-02-12 14:59:02.377+00	\N	22025	\N	\N	\N	\N
2009-02-12 14:59:02.439+00	2009-02-12 14:59:02.464+00	22026	\N	\N	\N	\N
2009-02-12 14:59:02.55+00	\N	22028	\N	\N	\N	\N
2009-02-12 14:59:02.573+00	\N	22030	\N	\N	\N	\N
2009-02-12 14:59:02.645+00	2009-02-12 14:59:02.672+00	22031	\N	\N	\N	\N
2009-02-12 14:59:02.902+00	\N	22033	\N	\N	\N	\N
2009-02-12 14:59:02.928+00	\N	22035	\N	\N	\N	\N
2009-02-12 14:59:03.008+00	2009-02-12 14:59:03.033+00	22036	\N	\N	\N	\N
2009-02-12 14:59:03.164+00	\N	22038	\N	\N	\N	\N
2009-02-12 14:59:03.19+00	\N	22040	\N	\N	\N	\N
2009-02-12 14:59:03.281+00	2009-02-12 14:59:03.313+00	22041	\N	\N	\N	\N
2009-02-12 14:59:03.377+00	\N	22043	\N	\N	\N	\N
2009-02-12 14:59:03.404+00	\N	22045	\N	\N	\N	\N
2009-02-12 14:59:03.52+00	2009-02-12 14:59:03.555+00	22046	\N	\N	\N	\N
2009-02-12 14:59:03.646+00	\N	22048	\N	\N	\N	\N
2009-02-12 14:59:03.677+00	\N	22050	\N	\N	\N	\N
2009-02-12 14:59:03.782+00	2009-02-12 14:59:03.874+00	22051	\N	\N	\N	\N
2009-02-12 14:59:03.925+00	\N	22053	\N	\N	\N	\N
2009-02-12 14:59:03.955+00	\N	22055	\N	\N	\N	\N
2009-02-12 14:59:04.064+00	2009-02-12 14:59:04.10+00	22056	\N	\N	\N	\N
2009-02-12 14:59:04.155+00	\N	22058	\N	\N	\N	\N
2009-02-12 14:59:04.187+00	\N	22060	\N	\N	\N	\N
2009-02-12 14:59:04.302+00	2009-02-12 14:59:04.337+00	22061	\N	\N	\N	\N
2009-02-12 14:59:04.395+00	\N	22063	\N	\N	\N	\N
2009-02-12 14:59:04.429+00	\N	22065	\N	\N	\N	\N
2009-02-12 14:59:04.55+00	2009-02-12 14:59:04.586+00	22066	\N	\N	\N	\N
2009-02-12 14:59:04.646+00	\N	22068	\N	\N	\N	\N
2009-02-12 14:59:04.682+00	\N	22070	\N	\N	\N	\N
2009-02-12 14:59:04.812+00	2009-02-12 14:59:04.854+00	22071	\N	\N	\N	\N
2009-02-12 14:59:05.038+00	\N	22073	\N	\N	\N	\N
2009-02-12 14:59:05.077+00	\N	22075	\N	\N	\N	\N
2009-02-12 14:59:05.242+00	2009-02-12 14:59:05.283+00	22076	\N	\N	\N	\N
2009-02-12 14:59:05.507+00	\N	22078	\N	\N	\N	\N
2009-02-12 14:59:05.548+00	\N	22080	\N	\N	\N	\N
2009-02-12 14:59:05.69+00	2009-02-12 14:59:05.732+00	22081	\N	\N	\N	\N
2009-02-12 14:59:06.182+00	\N	22083	\N	\N	\N	\N
2009-02-12 14:59:06.339+00	\N	22085	\N	\N	\N	\N
2009-02-12 14:59:06.663+00	2009-02-12 14:59:06.724+00	22086	\N	\N	\N	\N
2009-02-12 14:59:07.123+00	\N	22088	\N	\N	\N	\N
2009-02-12 14:59:07.201+00	\N	22090	\N	\N	\N	\N
2009-02-12 14:59:07.428+00	2009-02-12 14:59:07.482+00	22091	\N	\N	\N	\N
2009-02-12 14:59:07.598+00	\N	22093	\N	\N	\N	\N
2009-02-12 14:59:07.644+00	\N	22095	\N	\N	\N	\N
2009-02-12 14:59:07.802+00	2009-02-12 14:59:07.848+00	22096	\N	\N	\N	\N
2009-02-12 14:59:08.185+00	\N	22098	\N	\N	\N	\N
2009-02-12 14:59:09.111+00	\N	22100	\N	\N	\N	\N
2009-02-12 14:59:09.157+00	2009-02-12 14:59:09.17+00	22101	\N	\N	\N	\N
2009-02-12 14:59:09.203+00	\N	22103	\N	\N	\N	\N
2009-02-12 14:59:09.216+00	\N	22105	\N	\N	\N	\N
2009-02-12 14:59:09.267+00	2009-02-12 14:59:09.28+00	22106	\N	\N	\N	\N
2009-02-12 14:59:09.328+00	\N	22108	\N	\N	\N	\N
2009-02-12 14:59:09.341+00	\N	22110	\N	\N	\N	\N
2009-02-12 14:59:09.384+00	2009-02-12 14:59:09.397+00	22111	\N	\N	\N	\N
2009-02-12 14:59:09.435+00	\N	22113	\N	\N	\N	\N
2009-02-12 14:59:09.447+00	\N	22115	\N	\N	\N	\N
2009-02-12 14:59:09.482+00	2009-02-12 14:59:09.495+00	22116	\N	\N	\N	\N
2009-02-12 14:59:09.551+00	\N	22118	\N	\N	\N	\N
2009-02-12 14:59:09.564+00	\N	22120	\N	\N	\N	\N
2009-02-12 14:59:09.598+00	2009-02-12 14:59:09.61+00	22121	\N	\N	\N	\N
2009-02-12 14:59:09.653+00	\N	22123	\N	\N	\N	\N
2009-02-12 14:59:09.672+00	\N	22125	\N	\N	\N	\N
2009-02-12 14:59:09.714+00	2009-02-12 14:59:09.726+00	22126	\N	\N	\N	\N
2009-02-12 14:59:09.77+00	\N	22128	\N	\N	\N	\N
2009-02-12 14:59:09.783+00	\N	22130	\N	\N	\N	\N
2009-02-12 14:59:09.817+00	2009-02-12 14:59:09.828+00	22131	\N	\N	\N	\N
2009-02-12 14:59:09.87+00	\N	22133	\N	\N	\N	\N
2009-02-12 14:59:09.883+00	\N	22135	\N	\N	\N	\N
2009-02-12 14:59:09.916+00	2009-02-12 14:59:09.927+00	22136	\N	\N	\N	\N
2009-02-12 14:59:09.97+00	\N	22138	\N	\N	\N	\N
2009-02-12 14:59:09.981+00	\N	22140	\N	\N	\N	\N
2009-02-12 14:59:10.027+00	2009-02-12 14:59:10.038+00	22141	\N	\N	\N	\N
2009-02-12 14:59:10.109+00	\N	22143	\N	\N	\N	\N
2009-02-12 14:59:10.12+00	\N	22145	\N	\N	\N	\N
2009-02-12 14:59:10.153+00	2009-02-12 14:59:10.164+00	22146	\N	\N	\N	\N
2009-02-12 14:59:10.201+00	\N	22148	\N	\N	\N	\N
2009-02-12 14:59:10.212+00	\N	22150	\N	\N	\N	\N
2009-02-12 14:59:10.247+00	2009-02-12 14:59:10.26+00	22151	\N	\N	\N	\N
2009-02-12 14:59:10.353+00	\N	22153	\N	\N	\N	\N
2009-02-12 14:59:10.365+00	\N	22155	\N	\N	\N	\N
2009-02-12 14:59:10.406+00	2009-02-12 14:59:10.418+00	22156	\N	\N	\N	\N
2009-02-12 14:59:10.462+00	\N	22158	\N	\N	\N	\N
2009-02-12 14:59:10.474+00	\N	22160	\N	\N	\N	\N
2009-02-12 14:59:10.515+00	2009-02-12 14:59:10.526+00	22161	\N	\N	\N	\N
2009-02-12 14:59:10.571+00	\N	22163	\N	\N	\N	\N
2009-02-12 14:59:10.582+00	\N	22165	\N	\N	\N	\N
2009-02-12 14:59:10.623+00	2009-02-12 14:59:10.635+00	22166	\N	\N	\N	\N
2009-02-12 14:59:10.685+00	\N	22168	\N	\N	\N	\N
2009-02-12 14:59:10.697+00	\N	22170	\N	\N	\N	\N
2009-02-12 14:59:10.732+00	2009-02-12 14:59:10.743+00	22171	\N	\N	\N	\N
2009-02-12 14:59:10.761+00	\N	22173	\N	\N	\N	\N
2009-02-12 14:59:10.773+00	\N	22175	\N	\N	\N	\N
2009-02-12 14:59:10.807+00	2009-02-12 14:59:10.818+00	22176	\N	\N	\N	\N
2009-02-12 14:59:10.863+00	\N	22178	\N	\N	\N	\N
2009-02-12 14:59:10.874+00	\N	22180	\N	\N	\N	\N
2009-02-12 14:59:10.909+00	2009-02-12 14:59:10.92+00	22181	\N	\N	\N	\N
2009-02-12 14:59:10.966+00	\N	22183	\N	\N	\N	\N
2009-02-12 14:59:10.978+00	\N	22185	\N	\N	\N	\N
2009-02-12 14:59:11.015+00	2009-02-12 14:59:11.028+00	22186	\N	\N	\N	\N
2009-02-12 14:59:11.134+00	\N	22188	\N	\N	\N	\N
2009-02-12 14:59:11.145+00	\N	22190	\N	\N	\N	\N
2009-02-12 14:59:11.175+00	2009-02-12 14:59:11.185+00	22191	\N	\N	\N	\N
2009-02-12 14:59:11.209+00	\N	22193	\N	\N	\N	\N
2009-02-12 14:59:11.218+00	\N	22195	\N	\N	\N	\N
2009-02-12 14:59:11.246+00	2009-02-12 14:59:11.256+00	22196	\N	\N	\N	\N
2009-02-12 14:59:11.336+00	\N	22198	\N	\N	\N	\N
2009-02-12 14:59:11.347+00	\N	22200	\N	\N	\N	\N
2009-02-12 14:59:11.376+00	2009-02-12 14:59:11.396+00	22201	\N	\N	\N	\N
2009-02-12 14:59:11.444+00	\N	22203	\N	\N	\N	\N
2009-02-12 14:59:11.454+00	\N	22205	\N	\N	\N	\N
2009-02-12 14:59:11.482+00	2009-02-12 14:59:11.491+00	22206	\N	\N	\N	\N
2009-02-12 14:59:11.504+00	\N	22208	\N	\N	\N	\N
2009-02-12 14:59:11.512+00	\N	22210	\N	\N	\N	\N
2009-02-12 14:59:11.549+00	2009-02-12 14:59:11.565+00	22211	\N	\N	\N	\N
2009-02-12 14:59:11.58+00	\N	22213	\N	\N	\N	\N
2009-02-12 14:59:11.59+00	\N	22215	\N	\N	\N	\N
2009-02-12 14:59:11.62+00	2009-02-12 14:59:11.63+00	22216	\N	\N	\N	\N
2009-02-12 14:59:11.645+00	\N	22218	\N	\N	\N	\N
2009-02-12 14:59:11.654+00	\N	22220	\N	\N	\N	\N
2009-02-12 14:59:11.685+00	2009-02-12 14:59:11.709+00	22221	\N	\N	\N	\N
2009-02-12 14:59:11.736+00	\N	22223	\N	\N	\N	\N
2009-02-12 14:59:11.746+00	\N	22225	\N	\N	\N	\N
2009-02-12 14:59:11.784+00	2009-02-12 14:59:11.796+00	22226	\N	\N	\N	\N
2009-02-12 14:59:11.818+00	\N	22228	\N	\N	\N	\N
2009-02-12 14:59:11.828+00	\N	22230	\N	\N	\N	\N
2009-02-12 14:59:11.859+00	2009-02-12 14:59:11.87+00	22231	\N	\N	\N	\N
2009-02-12 14:59:11.917+00	\N	22233	\N	\N	\N	\N
2009-02-12 14:59:11.928+00	\N	22235	\N	\N	\N	\N
2009-02-12 14:59:11.959+00	2009-02-12 14:59:11.969+00	22236	\N	\N	\N	\N
2009-02-12 14:59:12.009+00	\N	22238	\N	\N	\N	\N
2009-02-12 14:59:12.02+00	\N	22240	\N	\N	\N	\N
2009-02-12 14:59:12.052+00	2009-02-12 14:59:12.062+00	22241	\N	\N	\N	\N
2009-02-12 14:59:12.106+00	\N	22243	\N	\N	\N	\N
2009-02-12 14:59:12.118+00	\N	22245	\N	\N	\N	\N
2009-02-12 14:59:12.153+00	2009-02-12 14:59:12.164+00	22246	\N	\N	\N	\N
2009-02-12 14:59:12.235+00	\N	22248	\N	\N	\N	\N
2009-02-12 14:59:12.247+00	\N	22250	\N	\N	\N	\N
2009-02-12 14:59:12.282+00	2009-02-12 14:59:12.294+00	22251	\N	\N	\N	\N
2009-02-12 14:59:12.342+00	\N	22253	\N	\N	\N	\N
2009-02-12 14:59:12.353+00	\N	22255	\N	\N	\N	\N
2009-02-12 14:59:12.401+00	2009-02-12 14:59:12.419+00	22256	\N	\N	\N	\N
2009-02-12 14:59:12.499+00	\N	22258	\N	\N	\N	\N
2009-02-12 14:59:12.51+00	\N	22260	\N	\N	\N	\N
2009-02-12 14:59:12.547+00	2009-02-12 14:59:12.561+00	22261	\N	\N	\N	\N
2009-02-12 14:59:12.617+00	\N	22263	\N	\N	\N	\N
2009-02-12 14:59:12.629+00	\N	22265	\N	\N	\N	\N
2009-02-12 14:59:12.702+00	2009-02-12 14:59:12.721+00	22266	\N	\N	\N	\N
2009-02-12 14:59:12.79+00	\N	22268	\N	\N	\N	\N
2009-02-12 14:59:12.80+00	\N	22270	\N	\N	\N	\N
2009-02-12 14:59:12.847+00	2009-02-12 14:59:12.857+00	22271	\N	\N	\N	\N
2009-02-12 14:59:12.899+00	\N	22273	\N	\N	\N	\N
2009-02-12 14:59:12.909+00	\N	22275	\N	\N	\N	\N
2009-02-12 14:59:12.942+00	2009-02-12 14:59:12.951+00	22276	\N	\N	\N	\N
2009-02-12 14:59:13+00	\N	22278	\N	\N	\N	\N
2009-02-12 14:59:13.025+00	\N	22280	\N	\N	\N	\N
2009-02-12 14:59:13.058+00	2009-02-12 14:59:13.068+00	22281	\N	\N	\N	\N
2009-02-12 14:59:13.172+00	\N	22283	\N	\N	\N	\N
2009-02-12 14:59:13.184+00	\N	22285	\N	\N	\N	\N
2009-02-12 14:59:13.22+00	2009-02-12 14:59:13.231+00	22286	\N	\N	\N	\N
2009-02-12 14:58:29.456+00	2009-02-12 14:59:13.252+00	16218	\N	\N	\N	\N
2009-02-12 14:59:13.331+00	2009-02-12 14:59:13.349+00	22288	\N	\N	\N	\N
2009-02-12 14:59:13.391+00	\N	22290	\N	\N	\N	\N
2009-02-12 14:59:13.413+00	\N	22292	\N	\N	\N	\N
2009-02-12 14:59:13.461+00	2009-02-12 14:59:13.497+00	22293	\N	\N	\N	\N
2009-02-12 14:59:13.564+00	\N	22295	\N	\N	\N	\N
2009-02-12 14:59:13.578+00	\N	22297	\N	\N	\N	\N
2009-02-12 14:59:13.618+00	2009-02-12 14:59:13.631+00	22298	\N	\N	\N	\N
2009-02-12 14:59:13.688+00	\N	22300	\N	\N	\N	\N
2009-02-12 14:59:13.70+00	\N	22302	\N	\N	\N	\N
2009-02-12 14:59:13.759+00	2009-02-12 14:59:13.774+00	22303	\N	\N	\N	\N
2009-02-12 14:59:13.864+00	\N	22305	\N	\N	\N	\N
2009-02-12 14:59:13.894+00	\N	22307	\N	\N	\N	\N
2009-02-12 14:59:13.939+00	2009-02-12 14:59:13.952+00	22308	\N	\N	\N	\N
2009-02-12 14:59:13.973+00	\N	22310	\N	\N	\N	\N
2009-02-12 14:59:13.985+00	\N	22312	\N	\N	\N	\N
2009-02-12 14:59:14.045+00	2009-02-12 14:59:14.06+00	22313	\N	\N	\N	\N
2009-02-12 14:59:14.081+00	\N	22315	\N	\N	\N	\N
2009-02-12 14:59:14.094+00	\N	22317	\N	\N	\N	\N
2009-02-12 14:59:14.138+00	2009-02-12 14:59:14.151+00	22318	\N	\N	\N	\N
2009-02-12 14:59:14.23+00	\N	22320	\N	\N	\N	\N
2009-02-12 14:59:14.244+00	\N	22322	\N	\N	\N	\N
2009-02-12 14:59:14.289+00	2009-02-12 14:59:14.302+00	22323	\N	\N	\N	\N
2009-02-12 14:59:14.38+00	\N	22325	\N	\N	\N	\N
2009-02-12 14:59:14.394+00	\N	22327	\N	\N	\N	\N
2009-02-12 14:59:14.439+00	2009-02-12 14:59:14.453+00	22328	\N	\N	\N	\N
2009-02-12 14:59:14.495+00	\N	22330	\N	\N	\N	\N
2009-02-12 14:59:14.508+00	\N	22332	\N	\N	\N	\N
2009-02-12 14:59:14.587+00	2009-02-12 14:59:14.598+00	22333	\N	\N	\N	\N
2009-02-12 14:59:14.644+00	\N	22335	\N	\N	\N	\N
2009-02-12 14:59:14.655+00	\N	22337	\N	\N	\N	\N
2009-02-12 14:59:14.695+00	2009-02-12 14:59:14.709+00	22338	\N	\N	\N	\N
2009-02-12 14:59:14.80+00	\N	22340	\N	\N	\N	\N
2009-02-12 14:59:14.813+00	\N	22342	\N	\N	\N	\N
2009-02-12 14:59:14.853+00	2009-02-12 14:59:14.865+00	22343	\N	\N	\N	\N
2009-02-12 14:59:14.926+00	\N	22345	\N	\N	\N	\N
2009-02-12 14:59:14.939+00	\N	22347	\N	\N	\N	\N
2009-02-12 14:59:15.016+00	2009-02-12 14:59:15.031+00	22348	\N	\N	\N	\N
2009-02-12 14:59:15.078+00	\N	22350	\N	\N	\N	\N
2009-02-12 14:59:15.101+00	\N	22352	\N	\N	\N	\N
2009-02-12 14:59:15.146+00	2009-02-12 14:59:15.159+00	22353	\N	\N	\N	\N
2009-02-12 14:59:15.189+00	\N	22355	\N	\N	\N	\N
2009-02-12 14:59:15.202+00	\N	22357	\N	\N	\N	\N
2009-02-12 14:59:15.245+00	2009-02-12 14:59:15.259+00	22358	\N	\N	\N	\N
2009-02-12 14:59:15.335+00	\N	22360	\N	\N	\N	\N
2009-02-12 14:59:15.349+00	\N	22362	\N	\N	\N	\N
2009-02-12 14:59:15.393+00	2009-02-12 14:59:15.407+00	22363	\N	\N	\N	\N
2009-02-12 14:59:15.487+00	\N	22365	\N	\N	\N	\N
2009-02-12 14:59:15.501+00	\N	22367	\N	\N	\N	\N
2009-02-12 14:59:15.558+00	2009-02-12 14:59:15.578+00	22368	\N	\N	\N	\N
2009-02-12 14:59:15.602+00	\N	22370	\N	\N	\N	\N
2009-02-12 14:59:15.616+00	\N	22372	\N	\N	\N	\N
2009-02-12 14:59:15.673+00	2009-02-12 14:59:15.687+00	22373	\N	\N	\N	\N
2009-02-12 14:59:15.711+00	\N	22375	\N	\N	\N	\N
2009-02-12 14:59:15.725+00	\N	22377	\N	\N	\N	\N
2009-02-12 14:59:15.786+00	2009-02-12 14:59:15.801+00	22378	\N	\N	\N	\N
2009-02-12 14:59:15.866+00	\N	22380	\N	\N	\N	\N
2009-02-12 14:59:15.881+00	\N	22382	\N	\N	\N	\N
2009-02-12 14:59:15.928+00	2009-02-12 14:59:15.944+00	22383	\N	\N	\N	\N
2009-02-12 14:59:15.968+00	\N	22385	\N	\N	\N	\N
2009-02-12 14:59:15.983+00	\N	22387	\N	\N	\N	\N
2009-02-12 14:59:16.03+00	2009-02-12 14:59:16.047+00	22388	\N	\N	\N	\N
2009-02-12 14:59:16.149+00	\N	22390	\N	\N	\N	\N
2009-02-12 14:59:16.164+00	\N	22392	\N	\N	\N	\N
2009-02-12 14:59:16.222+00	2009-02-12 14:59:16.237+00	22393	\N	\N	\N	\N
2009-02-12 14:59:16.386+00	\N	22395	\N	\N	\N	\N
2009-02-12 14:59:16.402+00	\N	22397	\N	\N	\N	\N
2009-02-12 14:59:16.461+00	2009-02-12 14:59:16.477+00	22398	\N	\N	\N	\N
2009-02-12 14:59:16.583+00	\N	22400	\N	\N	\N	\N
2009-02-12 14:59:16.599+00	\N	22402	\N	\N	\N	\N
2009-02-12 14:59:16.649+00	2009-02-12 14:59:16.685+00	22403	\N	\N	\N	\N
2009-02-12 14:59:16.733+00	\N	22405	\N	\N	\N	\N
2009-02-12 14:59:16.761+00	\N	22407	\N	\N	\N	\N
2009-02-12 14:59:16.823+00	2009-02-12 14:59:16.837+00	22408	\N	\N	\N	\N
2009-02-12 14:59:16.949+00	\N	22410	\N	\N	\N	\N
2009-02-12 14:59:16.965+00	\N	22412	\N	\N	\N	\N
2009-02-12 14:59:17.021+00	2009-02-12 14:59:17.035+00	22413	\N	\N	\N	\N
2009-02-12 14:59:17.109+00	\N	22415	\N	\N	\N	\N
2009-02-12 14:59:17.135+00	\N	22417	\N	\N	\N	\N
2009-02-12 14:59:17.185+00	2009-02-12 14:59:17.213+00	22418	\N	\N	\N	\N
2009-02-12 14:59:17.255+00	\N	22420	\N	\N	\N	\N
2009-02-12 14:59:17.28+00	\N	22422	\N	\N	\N	\N
2009-02-12 14:59:17.365+00	2009-02-12 14:59:17.39+00	22423	\N	\N	\N	\N
2009-02-12 14:59:17.51+00	\N	22425	\N	\N	\N	\N
2009-02-12 14:59:17.525+00	\N	22427	\N	\N	\N	\N
2009-02-12 14:59:17.574+00	2009-02-12 14:59:17.591+00	22428	\N	\N	\N	\N
2009-02-12 14:59:17.694+00	\N	22430	\N	\N	\N	\N
2009-02-12 14:59:17.711+00	\N	22432	\N	\N	\N	\N
2009-02-12 14:59:17.757+00	2009-02-12 14:59:17.77+00	22433	\N	\N	\N	\N
2009-02-12 14:59:17.864+00	\N	22435	\N	\N	\N	\N
2009-02-12 14:59:17.879+00	\N	22437	\N	\N	\N	\N
2009-02-12 14:59:17.928+00	2009-02-12 14:59:17.942+00	22438	\N	\N	\N	\N
2009-02-12 14:59:18.109+00	\N	22440	\N	\N	\N	\N
2009-02-12 14:59:18.127+00	\N	22442	\N	\N	\N	\N
2009-02-12 14:59:18.183+00	2009-02-12 14:59:18.199+00	22443	\N	\N	\N	\N
2009-02-12 14:59:18.402+00	\N	22445	\N	\N	\N	\N
2009-02-12 14:59:18.421+00	\N	22447	\N	\N	\N	\N
2009-02-12 14:59:18.478+00	2009-02-12 14:59:18.495+00	22448	\N	\N	\N	\N
2009-02-12 14:59:18.639+00	\N	22450	\N	\N	\N	\N
2009-02-12 14:59:18.657+00	\N	22452	\N	\N	\N	\N
2009-02-12 14:59:18.716+00	2009-02-12 14:59:18.733+00	22453	\N	\N	\N	\N
2009-02-12 14:59:18.875+00	\N	22455	\N	\N	\N	\N
2009-02-12 14:59:18.893+00	\N	22457	\N	\N	\N	\N
2009-02-12 14:59:18.952+00	2009-02-12 14:59:18.968+00	22458	\N	\N	\N	\N
2009-02-12 14:59:19.11+00	\N	22460	\N	\N	\N	\N
2009-02-12 14:59:19.128+00	\N	22462	\N	\N	\N	\N
2009-02-12 14:59:19.198+00	2009-02-12 14:59:19.215+00	22463	\N	\N	\N	\N
2009-02-12 14:59:19.281+00	\N	22465	\N	\N	\N	\N
2009-02-12 14:59:19.305+00	\N	22467	\N	\N	\N	\N
2009-02-12 14:59:19.364+00	2009-02-12 14:59:19.381+00	22468	\N	\N	\N	\N
2009-02-12 14:59:19.469+00	\N	22470	\N	\N	\N	\N
2009-02-12 14:59:19.487+00	\N	22472	\N	\N	\N	\N
2009-02-12 14:59:19.547+00	2009-02-12 14:59:19.564+00	22473	\N	\N	\N	\N
2009-02-12 14:59:19.64+00	\N	22475	\N	\N	\N	\N
2009-02-12 14:59:19.658+00	\N	22477	\N	\N	\N	\N
2009-02-12 14:59:19.722+00	2009-02-12 14:59:19.739+00	22478	\N	\N	\N	\N
2009-02-12 14:59:19.804+00	\N	22480	\N	\N	\N	\N
2009-02-12 14:59:19.821+00	\N	22482	\N	\N	\N	\N
2009-02-12 14:59:19.882+00	2009-02-12 14:59:19.907+00	22483	\N	\N	\N	\N
2009-02-12 14:59:19.985+00	\N	22485	\N	\N	\N	\N
2009-02-12 14:59:20.003+00	\N	22487	\N	\N	\N	\N
2009-02-12 14:59:20.063+00	2009-02-12 14:59:20.081+00	22488	\N	\N	\N	\N
2009-02-12 14:59:20.135+00	\N	22490	\N	\N	\N	\N
2009-02-12 14:59:20.16+00	\N	22492	\N	\N	\N	\N
2009-02-12 14:59:20.217+00	2009-02-12 14:59:20.235+00	22493	\N	\N	\N	\N
2009-02-12 14:59:20.264+00	\N	22495	\N	\N	\N	\N
2009-02-12 14:59:20.28+00	\N	22497	\N	\N	\N	\N
2009-02-12 14:59:20.342+00	2009-02-12 14:59:20.36+00	22498	\N	\N	\N	\N
2009-02-12 14:59:20.438+00	\N	22500	\N	\N	\N	\N
2009-02-12 14:59:20.456+00	\N	22502	\N	\N	\N	\N
2009-02-12 14:59:20.514+00	2009-02-12 14:59:20.531+00	22503	\N	\N	\N	\N
2009-02-12 14:59:20.614+00	\N	22505	\N	\N	\N	\N
2009-02-12 14:59:20.633+00	\N	22507	\N	\N	\N	\N
2009-02-12 14:59:20.737+00	2009-02-12 14:59:20.769+00	22508	\N	\N	\N	\N
2009-02-12 14:59:20.884+00	\N	22510	\N	\N	\N	\N
2009-02-12 14:59:20.90+00	\N	22512	\N	\N	\N	\N
2009-02-12 14:59:20.953+00	2009-02-12 14:59:20.971+00	22513	\N	\N	\N	\N
2009-02-12 14:59:21.053+00	\N	22515	\N	\N	\N	\N
2009-02-12 14:59:21.072+00	\N	22517	\N	\N	\N	\N
2009-02-12 14:59:21.149+00	2009-02-12 14:59:21.168+00	22518	\N	\N	\N	\N
2009-02-12 14:59:21.214+00	\N	22520	\N	\N	\N	\N
2009-02-12 14:59:21.232+00	\N	22522	\N	\N	\N	\N
2009-02-12 14:59:21.294+00	2009-02-12 14:59:21.324+00	22523	\N	\N	\N	\N
2009-02-12 14:59:21.491+00	\N	22525	\N	\N	\N	\N
2009-02-12 14:59:21.514+00	\N	22527	\N	\N	\N	\N
2009-02-12 14:59:21.577+00	2009-02-12 14:59:21.597+00	22528	\N	\N	\N	\N
2009-02-12 14:59:21.706+00	\N	22530	\N	\N	\N	\N
2009-02-12 14:59:21.726+00	\N	22532	\N	\N	\N	\N
2009-02-12 14:59:21.789+00	2009-02-12 14:59:21.808+00	22533	\N	\N	\N	\N
2009-02-12 14:59:21.867+00	\N	22535	\N	\N	\N	\N
2009-02-12 14:59:21.886+00	\N	22537	\N	\N	\N	\N
2009-02-12 14:59:21.948+00	2009-02-12 14:59:21.966+00	22538	\N	\N	\N	\N
2009-02-12 14:59:22.106+00	\N	22540	\N	\N	\N	\N
2009-02-12 14:59:22.125+00	\N	22542	\N	\N	\N	\N
2009-02-12 14:59:22.189+00	2009-02-12 14:59:22.21+00	22543	\N	\N	\N	\N
2009-02-12 14:59:22.297+00	\N	22545	\N	\N	\N	\N
2009-02-12 14:59:22.316+00	\N	22547	\N	\N	\N	\N
2009-02-12 14:59:22.393+00	2009-02-12 14:59:22.409+00	22548	\N	\N	\N	\N
2009-02-12 14:59:22.503+00	\N	22550	\N	\N	\N	\N
2009-02-12 14:59:22.523+00	\N	22552	\N	\N	\N	\N
2009-02-12 14:59:22.588+00	2009-02-12 14:59:22.607+00	22553	\N	\N	\N	\N
2009-02-12 14:59:22.773+00	\N	22555	\N	\N	\N	\N
2009-02-12 14:59:22.793+00	\N	22557	\N	\N	\N	\N
2009-02-12 14:59:22.86+00	2009-02-12 14:59:22.879+00	22558	\N	\N	\N	\N
2009-02-12 14:59:23.004+00	\N	22560	\N	\N	\N	\N
2009-02-12 14:59:23.023+00	\N	22562	\N	\N	\N	\N
2009-02-12 14:59:23.089+00	2009-02-12 14:59:23.108+00	22563	\N	\N	\N	\N
2009-02-12 14:59:23.257+00	\N	22567	\N	\N	\N	\N
2009-02-12 14:59:23.358+00	2009-02-12 14:59:23.402+00	22568	\N	\N	\N	\N
2009-02-12 14:59:23.539+00	\N	22570	\N	\N	\N	\N
2009-02-12 14:59:23.557+00	\N	22572	\N	\N	\N	\N
2009-02-12 14:59:23.617+00	2009-02-12 14:59:23.636+00	22573	\N	\N	\N	\N
2009-02-12 14:59:23.693+00	\N	22575	\N	\N	\N	\N
2009-02-12 14:59:23.711+00	\N	22577	\N	\N	\N	\N
2009-02-12 14:59:23.771+00	2009-02-12 14:59:23.791+00	22578	\N	\N	\N	\N
2009-02-12 14:59:23.848+00	\N	22580	\N	\N	\N	\N
2009-02-12 14:59:23.868+00	\N	22582	\N	\N	\N	\N
2009-02-12 14:59:23.931+00	2009-02-12 14:59:23.95+00	22583	\N	\N	\N	\N
2009-02-12 14:59:24.011+00	\N	22585	\N	\N	\N	\N
2009-02-12 14:59:24.03+00	\N	22587	\N	\N	\N	\N
2009-02-12 14:59:24.094+00	2009-02-12 14:59:24.113+00	22588	\N	\N	\N	\N
2009-02-12 14:59:24.218+00	\N	22590	\N	\N	\N	\N
2009-02-12 14:59:24.237+00	\N	22592	\N	\N	\N	\N
2009-02-12 14:59:24.316+00	2009-02-12 14:59:24.336+00	22593	\N	\N	\N	\N
2009-02-12 14:59:24.538+00	\N	22595	\N	\N	\N	\N
2009-02-12 14:59:24.559+00	\N	22597	\N	\N	\N	\N
2009-02-12 14:59:24.639+00	2009-02-12 14:59:24.659+00	22598	\N	\N	\N	\N
2009-02-12 14:59:24.883+00	\N	22600	\N	\N	\N	\N
2009-02-12 14:59:24.904+00	\N	22602	\N	\N	\N	\N
2009-02-12 14:59:24.969+00	2009-02-12 14:59:24.99+00	22603	\N	\N	\N	\N
2009-02-12 14:59:25.053+00	\N	22605	\N	\N	\N	\N
2009-02-12 14:59:25.082+00	\N	22607	\N	\N	\N	\N
2009-02-12 14:59:25.149+00	2009-02-12 14:59:25.168+00	22608	\N	\N	\N	\N
2009-02-12 14:59:25.305+00	\N	22610	\N	\N	\N	\N
2009-02-12 14:59:25.325+00	\N	22612	\N	\N	\N	\N
2009-02-12 14:59:25.417+00	2009-02-12 14:59:25.438+00	22613	\N	\N	\N	\N
2009-02-12 14:59:25.606+00	\N	22615	\N	\N	\N	\N
2009-02-12 14:59:25.627+00	\N	22617	\N	\N	\N	\N
2009-02-12 14:59:25.694+00	2009-02-12 14:59:25.714+00	22618	\N	\N	\N	\N
2009-02-12 14:59:25.838+00	\N	22620	\N	\N	\N	\N
2009-02-12 14:59:25.858+00	\N	22622	\N	\N	\N	\N
2009-02-12 14:59:25.926+00	2009-02-12 14:59:25.947+00	22623	\N	\N	\N	\N
2009-02-12 14:59:26.102+00	\N	22625	\N	\N	\N	\N
2009-02-12 14:59:26.122+00	\N	22627	\N	\N	\N	\N
2009-02-12 14:59:26.193+00	2009-02-12 14:59:26.214+00	22628	\N	\N	\N	\N
2009-02-12 14:59:26.263+00	\N	22630	\N	\N	\N	\N
2009-02-12 14:59:26.284+00	\N	22632	\N	\N	\N	\N
2009-02-12 14:59:26.368+00	2009-02-12 14:59:26.39+00	22633	\N	\N	\N	\N
2009-02-12 14:59:26.425+00	\N	22635	\N	\N	\N	\N
2009-02-12 14:59:26.446+00	\N	22637	\N	\N	\N	\N
2009-02-12 14:59:26.515+00	2009-02-12 14:59:26.535+00	22638	\N	\N	\N	\N
2009-02-12 14:59:26.694+00	\N	22640	\N	\N	\N	\N
2009-02-12 14:59:26.715+00	\N	22642	\N	\N	\N	\N
2009-02-12 14:59:26.814+00	2009-02-12 14:59:26.834+00	22643	\N	\N	\N	\N
2009-02-12 14:59:27.132+00	\N	22645	\N	\N	\N	\N
2009-02-12 14:59:27.168+00	\N	22647	\N	\N	\N	\N
2009-02-12 14:59:27.271+00	2009-02-12 14:59:27.29+00	22648	\N	\N	\N	\N
2009-02-12 14:59:27.408+00	\N	22650	\N	\N	\N	\N
2009-02-12 14:59:27.427+00	\N	22652	\N	\N	\N	\N
2009-02-12 14:59:27.492+00	2009-02-12 14:59:27.511+00	22653	\N	\N	\N	\N
2009-02-12 14:59:27.543+00	\N	22655	\N	\N	\N	\N
2009-02-12 14:59:27.574+00	\N	22657	\N	\N	\N	\N
2009-02-12 14:59:27.643+00	2009-02-12 14:59:27.664+00	22658	\N	\N	\N	\N
2009-02-12 14:59:27.765+00	\N	22660	\N	\N	\N	\N
2009-02-12 14:59:27.791+00	\N	22662	\N	\N	\N	\N
2009-02-12 14:59:27.861+00	2009-02-12 14:59:27.883+00	22663	\N	\N	\N	\N
2009-02-12 14:59:28.031+00	\N	22665	\N	\N	\N	\N
2009-02-12 14:59:28.053+00	\N	22667	\N	\N	\N	\N
2009-02-12 14:59:28.125+00	2009-02-12 14:59:28.147+00	22668	\N	\N	\N	\N
2009-02-12 14:59:28.20+00	\N	22670	\N	\N	\N	\N
2009-02-12 14:59:28.224+00	\N	22672	\N	\N	\N	\N
2009-02-12 14:59:28.297+00	2009-02-12 14:59:28.319+00	22673	\N	\N	\N	\N
2009-02-12 14:59:28.486+00	\N	22675	\N	\N	\N	\N
2009-02-12 14:59:28.509+00	\N	22677	\N	\N	\N	\N
2009-02-12 14:59:28.581+00	2009-02-12 14:59:28.603+00	22678	\N	\N	\N	\N
2009-02-12 14:59:28.739+00	\N	22680	\N	\N	\N	\N
2009-02-12 14:59:28.761+00	\N	22682	\N	\N	\N	\N
2009-02-12 14:59:28.841+00	2009-02-12 14:59:28.864+00	22683	\N	\N	\N	\N
2009-02-12 14:59:28.983+00	\N	22685	\N	\N	\N	\N
2009-02-12 14:59:29.041+00	\N	22687	\N	\N	\N	\N
2009-02-12 14:59:29.17+00	2009-02-12 14:59:29.207+00	22688	\N	\N	\N	\N
2009-02-12 14:59:29.348+00	\N	22690	\N	\N	\N	\N
2009-02-12 14:59:29.368+00	\N	22692	\N	\N	\N	\N
2009-02-12 14:59:29.451+00	2009-02-12 14:59:29.471+00	22693	\N	\N	\N	\N
2009-02-12 14:59:29.632+00	\N	22695	\N	\N	\N	\N
2009-02-12 14:59:29.656+00	\N	22697	\N	\N	\N	\N
2009-02-12 14:59:29.753+00	2009-02-12 14:59:29.776+00	22698	\N	\N	\N	\N
2009-02-12 14:59:29.898+00	\N	22700	\N	\N	\N	\N
2009-02-12 14:59:29.922+00	\N	22702	\N	\N	\N	\N
2009-02-12 14:59:30.002+00	2009-02-12 14:59:30.025+00	22703	\N	\N	\N	\N
2009-02-12 14:59:30.148+00	\N	22705	\N	\N	\N	\N
2009-02-12 14:59:30.171+00	\N	22707	\N	\N	\N	\N
2009-02-12 14:59:30.252+00	2009-02-12 14:59:30.298+00	22708	\N	\N	\N	\N
2009-02-12 14:59:30.355+00	\N	22710	\N	\N	\N	\N
2009-02-12 14:59:30.378+00	\N	22712	\N	\N	\N	\N
2009-02-12 14:59:30.459+00	2009-02-12 14:59:30.482+00	22713	\N	\N	\N	\N
2009-02-12 14:59:30.622+00	\N	22715	\N	\N	\N	\N
2009-02-12 14:59:30.646+00	\N	22717	\N	\N	\N	\N
2009-02-12 14:59:30.73+00	2009-02-12 14:59:30.753+00	22718	\N	\N	\N	\N
2009-02-12 14:59:30.879+00	\N	22720	\N	\N	\N	\N
2009-02-12 14:59:30.903+00	\N	22722	\N	\N	\N	\N
2009-02-12 14:59:30.985+00	2009-02-12 14:59:31.009+00	22723	\N	\N	\N	\N
2009-02-12 14:59:31.203+00	\N	22725	\N	\N	\N	\N
2009-02-12 14:59:31.228+00	\N	22727	\N	\N	\N	\N
2009-02-12 14:59:31.326+00	2009-02-12 14:59:31.35+00	22728	\N	\N	\N	\N
2009-02-12 14:59:31.389+00	\N	22730	\N	\N	\N	\N
2009-02-12 14:59:31.411+00	\N	22732	\N	\N	\N	\N
2009-02-12 14:59:31.492+00	2009-02-12 14:59:31.516+00	22733	\N	\N	\N	\N
2009-02-12 14:59:31.555+00	\N	22735	\N	\N	\N	\N
2009-02-12 14:59:31.579+00	\N	22737	\N	\N	\N	\N
2009-02-12 14:59:31.661+00	2009-02-12 14:59:31.684+00	22738	\N	\N	\N	\N
2009-02-12 14:59:31.795+00	\N	22740	\N	\N	\N	\N
2009-02-12 14:59:31.819+00	\N	22742	\N	\N	\N	\N
2009-02-12 14:59:31.902+00	2009-02-12 14:59:31.927+00	22743	\N	\N	\N	\N
2009-02-12 14:59:32.054+00	\N	22745	\N	\N	\N	\N
2009-02-12 14:59:32.079+00	\N	22747	\N	\N	\N	\N
2009-02-12 14:59:32.162+00	2009-02-12 14:59:32.186+00	22748	\N	\N	\N	\N
2009-02-12 14:59:32.297+00	\N	22750	\N	\N	\N	\N
2009-02-12 14:59:32.321+00	\N	22752	\N	\N	\N	\N
2009-02-12 14:59:32.404+00	2009-02-12 14:59:32.428+00	22753	\N	\N	\N	\N
2009-02-12 14:59:32.537+00	\N	22755	\N	\N	\N	\N
2009-02-12 14:59:32.561+00	\N	22757	\N	\N	\N	\N
2009-02-12 14:59:32.644+00	2009-02-12 14:59:32.668+00	22758	\N	\N	\N	\N
2009-02-12 14:59:32.831+00	\N	22760	\N	\N	\N	\N
2009-02-12 14:59:32.856+00	\N	22762	\N	\N	\N	\N
2009-02-12 14:59:32.94+00	2009-02-12 14:59:32.967+00	22763	\N	\N	\N	\N
2009-02-12 14:59:33.221+00	\N	22765	\N	\N	\N	\N
2009-02-12 14:59:33.248+00	\N	22767	\N	\N	\N	\N
2009-02-12 14:59:33.333+00	2009-02-12 14:59:33.364+00	22768	\N	\N	\N	\N
2009-02-12 14:59:33.496+00	\N	22770	\N	\N	\N	\N
2009-02-12 14:59:33.521+00	\N	22772	\N	\N	\N	\N
2009-02-12 14:59:33.621+00	2009-02-12 14:59:33.645+00	22773	\N	\N	\N	\N
2009-02-12 14:59:33.807+00	\N	22775	\N	\N	\N	\N
2009-02-12 14:59:33.834+00	\N	22777	\N	\N	\N	\N
2009-02-12 14:59:33.91+00	2009-02-12 14:59:33.934+00	22778	\N	\N	\N	\N
2009-02-12 14:59:34.042+00	\N	22780	\N	\N	\N	\N
2009-02-12 14:59:34.066+00	\N	22782	\N	\N	\N	\N
2009-02-12 14:59:34.161+00	2009-02-12 14:59:34.184+00	22783	\N	\N	\N	\N
2009-02-12 14:59:34.242+00	\N	22785	\N	\N	\N	\N
2009-02-12 14:59:34.264+00	\N	22787	\N	\N	\N	\N
2009-02-12 14:59:34.346+00	2009-02-12 14:59:34.373+00	22788	\N	\N	\N	\N
2009-02-12 14:59:34.488+00	\N	22790	\N	\N	\N	\N
2009-02-12 14:59:34.513+00	\N	22792	\N	\N	\N	\N
2009-02-12 14:59:34.595+00	2009-02-12 14:59:34.619+00	22793	\N	\N	\N	\N
2009-02-12 14:59:34.735+00	\N	22795	\N	\N	\N	\N
2009-02-12 14:59:34.76+00	\N	22797	\N	\N	\N	\N
2009-02-12 14:59:34.843+00	2009-02-12 14:59:34.868+00	22798	\N	\N	\N	\N
2009-02-12 14:59:34.985+00	\N	22800	\N	\N	\N	\N
2009-02-12 14:59:35.01+00	\N	22802	\N	\N	\N	\N
2009-02-12 14:59:35.093+00	2009-02-12 14:59:35.118+00	22803	\N	\N	\N	\N
2009-02-12 14:59:35.235+00	\N	22805	\N	\N	\N	\N
2009-02-12 14:59:35.261+00	\N	22807	\N	\N	\N	\N
2009-02-12 14:59:35.409+00	2009-02-12 14:59:35.435+00	22808	\N	\N	\N	\N
2009-02-12 14:59:35.567+00	\N	22810	\N	\N	\N	\N
2009-02-12 14:59:35.592+00	\N	22812	\N	\N	\N	\N
2009-02-12 14:59:35.673+00	2009-02-12 14:59:35.698+00	22813	\N	\N	\N	\N
2009-02-12 14:59:35.741+00	\N	22815	\N	\N	\N	\N
2009-02-12 14:59:35.765+00	\N	22817	\N	\N	\N	\N
2009-02-12 14:59:35.846+00	2009-02-12 14:59:35.871+00	22818	\N	\N	\N	\N
2009-02-12 14:59:35.914+00	\N	22820	\N	\N	\N	\N
2009-02-12 14:59:35.938+00	\N	22822	\N	\N	\N	\N
2009-02-12 14:59:36.022+00	2009-02-12 14:59:36.047+00	22823	\N	\N	\N	\N
2009-02-12 14:59:36.152+00	\N	22825	\N	\N	\N	\N
2009-02-12 14:59:36.182+00	\N	22827	\N	\N	\N	\N
2009-02-12 14:59:36.265+00	2009-02-12 14:59:36.291+00	22828	\N	\N	\N	\N
2009-02-12 14:59:36.514+00	\N	22830	\N	\N	\N	\N
2009-02-12 14:59:36.539+00	\N	22832	\N	\N	\N	\N
2009-02-12 14:59:36.635+00	2009-02-12 14:59:36.66+00	22833	\N	\N	\N	\N
2009-02-12 14:59:36.795+00	\N	22835	\N	\N	\N	\N
2009-02-12 14:59:36.82+00	\N	22837	\N	\N	\N	\N
2009-02-12 14:59:36.903+00	2009-02-12 14:59:36.928+00	22838	\N	\N	\N	\N
2009-02-12 14:59:36.989+00	\N	22840	\N	\N	\N	\N
2009-02-12 14:59:37.014+00	\N	22842	\N	\N	\N	\N
2009-02-12 14:59:37.099+00	2009-02-12 14:59:37.124+00	22843	\N	\N	\N	\N
2009-02-12 14:59:37.243+00	\N	22845	\N	\N	\N	\N
2009-02-12 14:59:37.269+00	\N	22847	\N	\N	\N	\N
2009-02-12 14:59:37.353+00	2009-02-12 14:59:37.379+00	22848	\N	\N	\N	\N
2009-02-12 14:59:37.433+00	\N	22850	\N	\N	\N	\N
2009-02-12 14:59:37.458+00	\N	22852	\N	\N	\N	\N
2009-02-12 14:59:37.543+00	2009-02-12 14:59:37.569+00	22853	\N	\N	\N	\N
2009-02-12 14:59:37.614+00	\N	22855	\N	\N	\N	\N
2009-02-12 14:59:37.639+00	\N	22857	\N	\N	\N	\N
2009-02-12 14:59:37.744+00	2009-02-12 14:59:37.772+00	22858	\N	\N	\N	\N
2009-02-12 14:59:37.817+00	\N	22860	\N	\N	\N	\N
2009-02-12 14:59:37.842+00	\N	22862	\N	\N	\N	\N
2009-02-12 14:59:37.967+00	2009-02-12 14:59:37.993+00	22863	\N	\N	\N	\N
2009-02-12 14:58:29.645+00	2009-02-12 14:59:38.136+00	16234	\N	\N	\N	\N
2009-02-12 14:59:38.206+00	2009-02-12 14:59:38.232+00	22865	\N	\N	\N	\N
2009-02-12 14:59:38.277+00	\N	22867	\N	\N	\N	\N
2009-02-12 14:59:38.303+00	\N	22869	\N	\N	\N	\N
2009-02-12 14:59:38.39+00	2009-02-12 14:59:38.416+00	22870	\N	\N	\N	\N
2009-02-12 14:59:38.678+00	\N	22872	\N	\N	\N	\N
2009-02-12 14:59:38.705+00	\N	22874	\N	\N	\N	\N
2009-02-12 14:59:38.813+00	2009-02-12 14:59:38.84+00	22875	\N	\N	\N	\N
2009-02-12 14:59:38.946+00	\N	22877	\N	\N	\N	\N
2009-02-12 14:59:38.973+00	\N	22879	\N	\N	\N	\N
2009-02-12 14:59:39.079+00	2009-02-12 14:59:39.104+00	22880	\N	\N	\N	\N
2009-02-12 14:59:39.15+00	\N	22882	\N	\N	\N	\N
2009-02-12 14:59:39.174+00	\N	22884	\N	\N	\N	\N
2009-02-12 14:59:39.271+00	2009-02-12 14:59:39.297+00	22885	\N	\N	\N	\N
2009-02-12 14:59:39.474+00	\N	22887	\N	\N	\N	\N
2009-02-12 14:59:39.499+00	\N	22889	\N	\N	\N	\N
2009-02-12 14:59:39.585+00	2009-02-12 14:59:39.611+00	22890	\N	\N	\N	\N
2009-02-12 14:59:39.655+00	\N	22892	\N	\N	\N	\N
2009-02-12 14:59:39.681+00	\N	22894	\N	\N	\N	\N
2009-02-12 14:59:39.772+00	2009-02-12 14:59:39.799+00	22895	\N	\N	\N	\N
2009-02-12 14:59:39.847+00	\N	22897	\N	\N	\N	\N
2009-02-12 14:59:39.873+00	\N	22899	\N	\N	\N	\N
2009-02-12 14:59:39.986+00	2009-02-12 14:59:40.013+00	22900	\N	\N	\N	\N
2009-02-12 14:59:40.102+00	\N	22902	\N	\N	\N	\N
2009-02-12 14:59:40.129+00	\N	22904	\N	\N	\N	\N
2009-02-12 14:59:40.241+00	2009-02-12 14:59:40.268+00	22905	\N	\N	\N	\N
2009-02-12 14:59:40.335+00	\N	22907	\N	\N	\N	\N
2009-02-12 14:59:40.362+00	\N	22909	\N	\N	\N	\N
2009-02-12 14:59:40.453+00	2009-02-12 14:59:40.48+00	22910	\N	\N	\N	\N
2009-02-12 14:59:40.65+00	\N	22912	\N	\N	\N	\N
2009-02-12 14:59:40.677+00	\N	22914	\N	\N	\N	\N
2009-02-12 14:59:40.768+00	2009-02-12 14:59:40.796+00	22915	\N	\N	\N	\N
2009-02-12 14:59:41.032+00	\N	22917	\N	\N	\N	\N
2009-02-12 14:59:41.06+00	\N	22919	\N	\N	\N	\N
2009-02-12 14:59:41.153+00	2009-02-12 14:59:41.181+00	22920	\N	\N	\N	\N
2009-02-12 14:59:41.522+00	\N	22922	\N	\N	\N	\N
2009-02-12 14:59:41.551+00	\N	22924	\N	\N	\N	\N
2009-02-12 14:59:41.644+00	2009-02-12 14:59:41.672+00	22925	\N	\N	\N	\N
2009-02-12 14:59:41.721+00	\N	22927	\N	\N	\N	\N
2009-02-12 14:59:41.748+00	\N	22929	\N	\N	\N	\N
2009-02-12 14:59:41.842+00	2009-02-12 14:59:41.87+00	22930	\N	\N	\N	\N
2009-02-12 14:59:41.919+00	\N	22932	\N	\N	\N	\N
2009-02-12 14:59:41.947+00	\N	22934	\N	\N	\N	\N
2009-02-12 14:59:42.063+00	2009-02-12 14:59:42.092+00	22935	\N	\N	\N	\N
2009-02-12 14:59:42.141+00	\N	22937	\N	\N	\N	\N
2009-02-12 14:59:42.169+00	\N	22939	\N	\N	\N	\N
2009-02-12 14:59:42.285+00	2009-02-12 14:59:42.313+00	22940	\N	\N	\N	\N
2009-02-12 14:59:42.363+00	\N	22942	\N	\N	\N	\N
2009-02-12 14:59:42.39+00	\N	22944	\N	\N	\N	\N
2009-02-12 14:59:42.486+00	2009-02-12 14:59:42.514+00	22945	\N	\N	\N	\N
2009-02-12 14:59:42.735+00	\N	22947	\N	\N	\N	\N
2009-02-12 14:59:42.764+00	\N	22949	\N	\N	\N	\N
2009-02-12 14:59:42.86+00	2009-02-12 14:59:42.889+00	22950	\N	\N	\N	\N
2009-02-12 14:59:43.058+00	\N	22952	\N	\N	\N	\N
2009-02-12 14:59:43.086+00	\N	22954	\N	\N	\N	\N
2009-02-12 14:59:43.18+00	2009-02-12 14:59:43.209+00	22955	\N	\N	\N	\N
2009-02-12 14:59:43.365+00	\N	22957	\N	\N	\N	\N
2009-02-12 14:59:43.393+00	\N	22959	\N	\N	\N	\N
2009-02-12 14:59:43.489+00	2009-02-12 14:59:43.517+00	22960	\N	\N	\N	\N
2009-02-12 14:59:43.696+00	\N	22962	\N	\N	\N	\N
2009-02-12 14:59:43.726+00	\N	22964	\N	\N	\N	\N
2009-02-12 14:59:43.843+00	2009-02-12 14:59:43.872+00	22965	\N	\N	\N	\N
2009-02-12 14:59:43.922+00	\N	22967	\N	\N	\N	\N
2009-02-12 14:59:43.949+00	\N	22969	\N	\N	\N	\N
2009-02-12 14:59:44.066+00	2009-02-12 14:59:44.094+00	22970	\N	\N	\N	\N
2009-02-12 14:59:44.143+00	\N	22972	\N	\N	\N	\N
2009-02-12 14:59:44.171+00	\N	22974	\N	\N	\N	\N
2009-02-12 14:59:44.267+00	2009-02-12 14:59:44.295+00	22975	\N	\N	\N	\N
2009-02-12 14:59:44.431+00	\N	22977	\N	\N	\N	\N
2009-02-12 14:59:44.46+00	\N	22979	\N	\N	\N	\N
2009-02-12 14:59:44.557+00	2009-02-12 14:59:44.586+00	22980	\N	\N	\N	\N
2009-02-12 14:59:44.636+00	\N	22982	\N	\N	\N	\N
2009-02-12 14:59:44.665+00	\N	22984	\N	\N	\N	\N
2009-02-12 14:59:44.763+00	2009-02-12 14:59:44.793+00	22985	\N	\N	\N	\N
2009-02-12 14:59:44.843+00	\N	22987	\N	\N	\N	\N
2009-02-12 14:59:44.872+00	\N	22989	\N	\N	\N	\N
2009-02-12 14:59:44.992+00	2009-02-12 14:59:45.022+00	22990	\N	\N	\N	\N
2009-02-12 14:59:45.073+00	\N	22992	\N	\N	\N	\N
2009-02-12 14:59:45.102+00	\N	22994	\N	\N	\N	\N
2009-02-12 14:59:45.223+00	2009-02-12 14:59:45.252+00	22995	\N	\N	\N	\N
2009-02-12 14:59:45.505+00	\N	22997	\N	\N	\N	\N
2009-02-12 14:59:45.535+00	\N	22999	\N	\N	\N	\N
2009-02-12 14:59:45.634+00	2009-02-12 14:59:45.664+00	23000	\N	\N	\N	\N
2009-02-12 14:59:45.76+00	\N	23002	\N	\N	\N	\N
2009-02-12 14:59:45.79+00	\N	23004	\N	\N	\N	\N
2009-02-12 14:59:45.89+00	2009-02-12 14:59:45.92+00	23005	\N	\N	\N	\N
2009-02-12 14:59:46.019+00	\N	23007	\N	\N	\N	\N
2009-02-12 14:59:46.049+00	\N	23009	\N	\N	\N	\N
2009-02-12 14:59:46.145+00	2009-02-12 14:59:46.174+00	23010	\N	\N	\N	\N
2009-02-12 14:59:46.223+00	\N	23012	\N	\N	\N	\N
2009-02-12 14:59:46.251+00	\N	23014	\N	\N	\N	\N
2009-02-12 14:59:46.357+00	2009-02-12 14:59:46.386+00	23015	\N	\N	\N	\N
2009-02-12 14:59:46.435+00	\N	23017	\N	\N	\N	\N
2009-02-12 14:59:46.463+00	\N	23019	\N	\N	\N	\N
2009-02-12 14:59:46.582+00	2009-02-12 14:59:46.613+00	23020	\N	\N	\N	\N
2009-02-12 14:59:46.753+00	\N	23022	\N	\N	\N	\N
2009-02-12 14:59:46.783+00	\N	23024	\N	\N	\N	\N
2009-02-12 14:59:46.885+00	2009-02-12 14:59:46.915+00	23025	\N	\N	\N	\N
2009-02-12 14:59:46.992+00	\N	23027	\N	\N	\N	\N
2009-02-12 14:59:47.022+00	\N	23029	\N	\N	\N	\N
2009-02-12 14:59:47.126+00	2009-02-12 14:59:47.157+00	23030	\N	\N	\N	\N
2009-02-12 14:59:47.257+00	\N	23032	\N	\N	\N	\N
2009-02-12 14:59:47.287+00	\N	23034	\N	\N	\N	\N
2009-02-12 14:59:47.389+00	2009-02-12 14:59:47.419+00	23035	\N	\N	\N	\N
2009-02-12 14:59:47.609+00	\N	23037	\N	\N	\N	\N
2009-02-12 14:59:47.64+00	\N	23039	\N	\N	\N	\N
2009-02-12 14:59:47.742+00	2009-02-12 14:59:47.773+00	23040	\N	\N	\N	\N
2009-02-12 14:59:47.918+00	\N	23042	\N	\N	\N	\N
2009-02-12 14:59:47.95+00	\N	23044	\N	\N	\N	\N
2009-02-12 14:59:48.053+00	2009-02-12 14:59:48.085+00	23045	\N	\N	\N	\N
2009-02-12 14:59:48.233+00	\N	23047	\N	\N	\N	\N
2009-02-12 14:59:48.264+00	\N	23049	\N	\N	\N	\N
2009-02-12 14:59:48.369+00	2009-02-12 14:59:48.40+00	23050	\N	\N	\N	\N
2009-02-12 14:59:48.548+00	\N	23052	\N	\N	\N	\N
2009-02-12 14:59:48.579+00	\N	23054	\N	\N	\N	\N
2009-02-12 14:59:48.684+00	2009-02-12 14:59:48.715+00	23055	\N	\N	\N	\N
2009-02-12 14:59:48.841+00	\N	23057	\N	\N	\N	\N
2009-02-12 14:59:48.872+00	\N	23059	\N	\N	\N	\N
2009-02-12 14:59:48.977+00	2009-02-12 14:59:49.009+00	23060	\N	\N	\N	\N
2009-02-12 14:59:49.063+00	\N	23062	\N	\N	\N	\N
2009-02-12 14:59:49.094+00	\N	23064	\N	\N	\N	\N
2009-02-12 14:59:49.20+00	2009-02-12 14:59:49.232+00	23065	\N	\N	\N	\N
2009-02-12 14:59:49.359+00	\N	23067	\N	\N	\N	\N
2009-02-12 14:59:49.391+00	\N	23069	\N	\N	\N	\N
2009-02-12 14:59:49.545+00	2009-02-12 14:59:49.577+00	23070	\N	\N	\N	\N
2009-02-12 14:59:49.632+00	\N	23072	\N	\N	\N	\N
2009-02-12 14:59:49.677+00	\N	23074	\N	\N	\N	\N
2009-02-12 14:59:49.782+00	2009-02-12 14:59:49.813+00	23075	\N	\N	\N	\N
2009-02-12 14:59:49.892+00	\N	23077	\N	\N	\N	\N
2009-02-12 14:59:49.922+00	\N	23079	\N	\N	\N	\N
2009-02-12 14:59:50.053+00	2009-02-12 14:59:50.084+00	23080	\N	\N	\N	\N
2009-02-12 14:59:50.211+00	\N	23082	\N	\N	\N	\N
2009-02-12 14:59:50.242+00	\N	23084	\N	\N	\N	\N
2009-02-12 14:59:50.373+00	2009-02-12 14:59:50.405+00	23085	\N	\N	\N	\N
2009-02-12 14:59:50.581+00	\N	23087	\N	\N	\N	\N
2009-02-12 14:59:50.613+00	\N	23089	\N	\N	\N	\N
2009-02-12 14:59:50.742+00	2009-02-12 14:59:50.773+00	23090	\N	\N	\N	\N
2009-02-12 14:59:50.875+00	\N	23092	\N	\N	\N	\N
2009-02-12 14:59:50.906+00	\N	23094	\N	\N	\N	\N
2009-02-12 14:59:51.012+00	2009-02-12 14:59:51.043+00	23095	\N	\N	\N	\N
2009-02-12 14:59:51.098+00	\N	23097	\N	\N	\N	\N
2009-02-12 14:59:51.129+00	\N	23099	\N	\N	\N	\N
2009-02-12 14:59:51.236+00	2009-02-12 14:59:51.268+00	23100	\N	\N	\N	\N
2009-02-12 14:59:51.519+00	\N	23102	\N	\N	\N	\N
2009-02-12 14:59:51.551+00	\N	23104	\N	\N	\N	\N
2009-02-12 14:59:51.659+00	2009-02-12 14:59:51.691+00	23105	\N	\N	\N	\N
2009-02-12 14:59:51.893+00	\N	23107	\N	\N	\N	\N
2009-02-12 14:59:51.925+00	\N	23109	\N	\N	\N	\N
2009-02-12 14:59:52.034+00	2009-02-12 14:59:52.067+00	23110	\N	\N	\N	\N
2009-02-12 14:59:52.419+00	\N	23112	\N	\N	\N	\N
2009-02-12 14:59:52.452+00	\N	23114	\N	\N	\N	\N
2009-02-12 14:59:52.561+00	2009-02-12 14:59:52.593+00	23115	\N	\N	\N	\N
2009-02-12 14:59:52.773+00	\N	23117	\N	\N	\N	\N
2009-02-12 14:59:52.806+00	\N	23119	\N	\N	\N	\N
2009-02-12 14:59:52.927+00	2009-02-12 14:59:52.959+00	23120	\N	\N	\N	\N
2009-02-12 14:59:53.113+00	\N	23122	\N	\N	\N	\N
2009-02-12 14:59:53.147+00	\N	23124	\N	\N	\N	\N
2009-02-12 14:59:53.32+00	2009-02-12 14:59:53.377+00	23125	\N	\N	\N	\N
2009-02-12 14:59:53.656+00	\N	23127	\N	\N	\N	\N
2009-02-12 14:59:53.687+00	\N	23129	\N	\N	\N	\N
2009-02-12 14:59:53.828+00	2009-02-12 14:59:53.863+00	23130	\N	\N	\N	\N
2009-02-12 14:59:54.11+00	\N	23132	\N	\N	\N	\N
2009-02-12 14:59:54.146+00	\N	23134	\N	\N	\N	\N
2009-02-12 14:59:54.298+00	2009-02-12 14:59:54.335+00	23135	\N	\N	\N	\N
2009-02-12 14:59:54.613+00	\N	23137	\N	\N	\N	\N
2009-02-12 14:59:54.649+00	\N	23139	\N	\N	\N	\N
2009-02-12 14:59:54.773+00	2009-02-12 14:59:54.808+00	23140	\N	\N	\N	\N
2009-02-12 14:59:54.922+00	\N	23142	\N	\N	\N	\N
2009-02-12 14:59:54.956+00	\N	23144	\N	\N	\N	\N
2009-02-12 14:59:55.105+00	2009-02-12 14:59:55.141+00	23145	\N	\N	\N	\N
2009-02-12 14:59:55.365+00	\N	23147	\N	\N	\N	\N
2009-02-12 14:59:55.401+00	\N	23149	\N	\N	\N	\N
2009-02-12 14:59:55.55+00	2009-02-12 14:59:55.585+00	23150	\N	\N	\N	\N
2009-02-12 14:59:55.648+00	\N	23152	\N	\N	\N	\N
2009-02-12 14:59:55.682+00	\N	23154	\N	\N	\N	\N
2009-02-12 14:59:55.834+00	2009-02-12 14:59:55.87+00	23155	\N	\N	\N	\N
2009-02-12 14:59:56.095+00	\N	23157	\N	\N	\N	\N
2009-02-12 14:59:56.131+00	\N	23159	\N	\N	\N	\N
2009-02-12 14:59:56.285+00	2009-02-12 14:59:56.322+00	23160	\N	\N	\N	\N
2009-02-12 14:59:56.521+00	\N	23162	\N	\N	\N	\N
2009-02-12 14:59:56.557+00	\N	23164	\N	\N	\N	\N
2009-02-12 14:59:56.708+00	2009-02-12 14:59:56.747+00	23165	\N	\N	\N	\N
2009-02-12 14:59:56.945+00	\N	23167	\N	\N	\N	\N
2009-02-12 14:59:56.981+00	\N	23169	\N	\N	\N	\N
2009-02-12 14:59:57.191+00	2009-02-12 14:59:57.224+00	23170	\N	\N	\N	\N
2009-02-12 14:59:57.523+00	\N	23172	\N	\N	\N	\N
2009-02-12 14:59:57.558+00	\N	23174	\N	\N	\N	\N
2009-02-12 14:59:57.676+00	2009-02-12 14:59:57.712+00	23175	\N	\N	\N	\N
2009-02-12 14:59:57.90+00	\N	23177	\N	\N	\N	\N
2009-02-12 14:59:57.935+00	\N	23179	\N	\N	\N	\N
2009-02-12 14:59:58.054+00	2009-02-12 14:59:58.093+00	23180	\N	\N	\N	\N
2009-02-12 14:59:58.159+00	\N	23182	\N	\N	\N	\N
2009-02-12 14:59:58.192+00	\N	23184	\N	\N	\N	\N
2009-02-12 14:59:58.311+00	2009-02-12 14:59:58.345+00	23185	\N	\N	\N	\N
2009-02-12 14:59:58.431+00	\N	23187	\N	\N	\N	\N
2009-02-12 14:59:58.464+00	\N	23189	\N	\N	\N	\N
2009-02-12 14:59:58.607+00	2009-02-12 14:59:58.641+00	23190	\N	\N	\N	\N
2009-02-12 14:59:58.701+00	\N	23192	\N	\N	\N	\N
2009-02-12 14:59:58.734+00	\N	23194	\N	\N	\N	\N
2009-02-12 14:59:58.85+00	2009-02-12 14:59:58.885+00	23195	\N	\N	\N	\N
2009-02-12 14:59:59.025+00	\N	23197	\N	\N	\N	\N
2009-02-12 14:59:59.059+00	\N	23199	\N	\N	\N	\N
2009-02-12 14:59:59.202+00	2009-02-12 14:59:59.247+00	23200	\N	\N	\N	\N
2009-02-12 14:59:59.627+00	\N	23202	\N	\N	\N	\N
2009-02-12 14:59:59.662+00	\N	23204	\N	\N	\N	\N
2009-02-12 14:59:59.805+00	2009-02-12 14:59:59.84+00	23205	\N	\N	\N	\N
2009-02-12 14:59:59.928+00	\N	23207	\N	\N	\N	\N
2009-02-12 14:59:59.963+00	\N	23209	\N	\N	\N	\N
2009-02-12 15:00:00.081+00	2009-02-12 15:00:00.116+00	23210	\N	\N	\N	\N
2009-02-12 15:00:00.258+00	\N	23212	\N	\N	\N	\N
2009-02-12 15:00:00.292+00	\N	23214	\N	\N	\N	\N
2009-02-12 15:00:00.409+00	2009-02-12 15:00:00.443+00	23215	\N	\N	\N	\N
2009-02-12 15:00:00.556+00	\N	23217	\N	\N	\N	\N
2009-02-12 15:00:00.591+00	\N	23219	\N	\N	\N	\N
2009-02-12 15:00:00.708+00	2009-02-12 15:00:00.742+00	23220	\N	\N	\N	\N
2009-02-12 15:00:00.857+00	\N	23222	\N	\N	\N	\N
2009-02-12 15:00:00.892+00	\N	23224	\N	\N	\N	\N
2009-02-12 15:00:01.01+00	2009-02-12 15:00:01.045+00	23225	\N	\N	\N	\N
2009-02-12 15:00:01.161+00	\N	23227	\N	\N	\N	\N
2009-02-12 15:00:01.196+00	\N	23229	\N	\N	\N	\N
2009-02-12 15:00:01.343+00	2009-02-12 15:00:01.378+00	23230	\N	\N	\N	\N
2009-02-12 15:00:01.603+00	\N	23232	\N	\N	\N	\N
2009-02-12 15:00:01.646+00	\N	23234	\N	\N	\N	\N
2009-02-12 15:00:01.805+00	2009-02-12 15:00:01.841+00	23235	\N	\N	\N	\N
2009-02-12 15:00:02.007+00	\N	23237	\N	\N	\N	\N
2009-02-12 15:00:02.05+00	\N	23239	\N	\N	\N	\N
2009-02-12 15:00:02.164+00	2009-02-12 15:00:02.198+00	23240	\N	\N	\N	\N
2009-02-12 15:00:02.288+00	\N	23242	\N	\N	\N	\N
2009-02-12 15:00:02.323+00	\N	23244	\N	\N	\N	\N
2009-02-12 15:00:02.444+00	2009-02-12 15:00:02.479+00	23245	\N	\N	\N	\N
2009-02-12 15:00:02.763+00	\N	23247	\N	\N	\N	\N
2009-02-12 15:00:02.799+00	\N	23249	\N	\N	\N	\N
2009-02-12 15:00:02.921+00	2009-02-12 15:00:02.957+00	23250	\N	\N	\N	\N
2009-02-12 15:00:03.019+00	\N	23252	\N	\N	\N	\N
2009-02-12 15:00:03.054+00	\N	23254	\N	\N	\N	\N
2009-02-12 15:00:03.175+00	2009-02-12 15:00:03.21+00	23255	\N	\N	\N	\N
2009-02-12 15:00:03.30+00	\N	23257	\N	\N	\N	\N
2009-02-12 15:00:03.335+00	\N	23259	\N	\N	\N	\N
2009-02-12 15:00:03.483+00	2009-02-12 15:00:03.518+00	23260	\N	\N	\N	\N
2009-02-12 15:00:03.694+00	\N	23262	\N	\N	\N	\N
2009-02-12 15:00:03.729+00	\N	23264	\N	\N	\N	\N
2009-02-12 15:00:03.852+00	2009-02-12 15:00:03.889+00	23265	\N	\N	\N	\N
2009-02-12 15:00:04.148+00	\N	23267	\N	\N	\N	\N
2009-02-12 15:00:04.185+00	\N	23269	\N	\N	\N	\N
2009-02-12 15:00:04.308+00	2009-02-12 15:00:04.345+00	23270	\N	\N	\N	\N
2009-02-12 15:00:04.578+00	\N	23272	\N	\N	\N	\N
2009-02-12 15:00:04.615+00	\N	23274	\N	\N	\N	\N
2009-02-12 15:00:04.778+00	2009-02-12 15:00:04.814+00	23275	\N	\N	\N	\N
2009-02-12 15:00:05.105+00	\N	23277	\N	\N	\N	\N
2009-02-12 15:00:05.142+00	\N	23279	\N	\N	\N	\N
2009-02-12 15:00:05.294+00	2009-02-12 15:00:05.331+00	23280	\N	\N	\N	\N
2009-02-12 15:00:05.624+00	\N	23282	\N	\N	\N	\N
2009-02-12 15:00:05.661+00	\N	23284	\N	\N	\N	\N
2009-02-12 15:00:05.789+00	2009-02-12 15:00:05.825+00	23285	\N	\N	\N	\N
2009-02-12 15:00:05.946+00	\N	23287	\N	\N	\N	\N
2009-02-12 15:00:05.982+00	\N	23289	\N	\N	\N	\N
2009-02-12 15:00:06.106+00	2009-02-12 15:00:06.142+00	23290	\N	\N	\N	\N
2009-02-12 15:00:06.32+00	\N	23292	\N	\N	\N	\N
2009-02-12 15:00:06.357+00	\N	23294	\N	\N	\N	\N
2009-02-12 15:00:06.482+00	2009-02-12 15:00:06.519+00	23295	\N	\N	\N	\N
2009-02-12 15:00:06.613+00	\N	23297	\N	\N	\N	\N
2009-02-12 15:00:06.65+00	\N	23299	\N	\N	\N	\N
2009-02-12 15:00:06.782+00	2009-02-12 15:00:06.818+00	23300	\N	\N	\N	\N
2009-02-12 15:00:06.942+00	\N	23302	\N	\N	\N	\N
2009-02-12 15:00:06.979+00	\N	23304	\N	\N	\N	\N
2009-02-12 15:00:07.134+00	2009-02-12 15:00:07.171+00	23305	\N	\N	\N	\N
2009-02-12 15:00:07.237+00	\N	23307	\N	\N	\N	\N
2009-02-12 15:00:07.289+00	\N	23309	\N	\N	\N	\N
2009-02-12 15:00:07.443+00	2009-02-12 15:00:07.481+00	23310	\N	\N	\N	\N
2009-02-12 15:00:07.604+00	\N	23312	\N	\N	\N	\N
2009-02-12 15:00:07.641+00	\N	23314	\N	\N	\N	\N
2009-02-12 15:00:07.768+00	2009-02-12 15:00:07.806+00	23315	\N	\N	\N	\N
2009-02-12 15:00:07.959+00	\N	23317	\N	\N	\N	\N
2009-02-12 15:00:07.997+00	\N	23319	\N	\N	\N	\N
2009-02-12 15:00:08.125+00	2009-02-12 15:00:08.162+00	23320	\N	\N	\N	\N
2009-02-12 15:00:08.431+00	\N	23322	\N	\N	\N	\N
2009-02-12 15:00:08.468+00	\N	23324	\N	\N	\N	\N
2009-02-12 15:00:08.651+00	2009-02-12 15:00:08.688+00	23325	\N	\N	\N	\N
2009-02-12 15:00:08.812+00	\N	23327	\N	\N	\N	\N
2009-02-12 15:00:08.849+00	\N	23329	\N	\N	\N	\N
2009-02-12 15:00:09.035+00	2009-02-12 15:00:09.073+00	23330	\N	\N	\N	\N
2009-02-12 15:00:09.14+00	\N	23332	\N	\N	\N	\N
2009-02-12 15:00:09.178+00	\N	23334	\N	\N	\N	\N
2009-02-12 15:00:09.364+00	2009-02-12 15:00:09.401+00	23335	\N	\N	\N	\N
2009-02-12 15:00:09.469+00	\N	23337	\N	\N	\N	\N
2009-02-12 15:00:09.506+00	\N	23339	\N	\N	\N	\N
2009-02-12 15:00:09.694+00	2009-02-12 15:00:09.74+00	23340	\N	\N	\N	\N
2009-02-12 15:00:09.865+00	\N	23342	\N	\N	\N	\N
2009-02-12 15:00:09.903+00	\N	23344	\N	\N	\N	\N
2009-02-12 15:00:10.09+00	2009-02-12 15:00:10.131+00	23345	\N	\N	\N	\N
2009-02-12 15:00:10.199+00	\N	23347	\N	\N	\N	\N
2009-02-12 15:00:10.238+00	\N	23349	\N	\N	\N	\N
2009-02-12 15:00:10.401+00	2009-02-12 15:00:10.441+00	23350	\N	\N	\N	\N
2009-02-12 15:00:10.573+00	\N	23352	\N	\N	\N	\N
2009-02-12 15:00:10.61+00	\N	23354	\N	\N	\N	\N
2009-02-12 15:00:10.764+00	2009-02-12 15:00:10.801+00	23355	\N	\N	\N	\N
2009-02-12 15:00:10.866+00	\N	23357	\N	\N	\N	\N
2009-02-12 15:00:10.902+00	\N	23359	\N	\N	\N	\N
2009-02-12 15:00:11.062+00	2009-02-12 15:00:11.10+00	23360	\N	\N	\N	\N
2009-02-12 15:00:11.349+00	\N	23362	\N	\N	\N	\N
2009-02-12 15:00:11.388+00	\N	23364	\N	\N	\N	\N
2009-02-12 15:00:11.551+00	2009-02-12 15:00:11.591+00	23365	\N	\N	\N	\N
2009-02-12 15:00:11.751+00	\N	23367	\N	\N	\N	\N
2009-02-12 15:00:11.79+00	\N	23369	\N	\N	\N	\N
2009-02-12 15:00:11.954+00	2009-02-12 15:00:11.993+00	23370	\N	\N	\N	\N
2009-02-12 15:00:12.258+00	\N	23372	\N	\N	\N	\N
2009-02-12 15:00:12.298+00	\N	23374	\N	\N	\N	\N
2009-02-12 15:00:12.432+00	2009-02-12 15:00:12.472+00	23375	\N	\N	\N	\N
2009-02-12 15:00:12.756+00	\N	23377	\N	\N	\N	\N
2009-02-12 15:00:12.797+00	\N	23379	\N	\N	\N	\N
2009-02-12 15:00:12.962+00	2009-02-12 15:00:13.003+00	23380	\N	\N	\N	\N
2009-02-12 15:00:13.103+00	\N	23382	\N	\N	\N	\N
2009-02-12 15:00:13.142+00	\N	23384	\N	\N	\N	\N
2009-02-12 15:00:13.306+00	2009-02-12 15:00:13.345+00	23385	\N	\N	\N	\N
2009-02-12 15:00:13.414+00	\N	23387	\N	\N	\N	\N
2009-02-12 15:00:13.454+00	\N	23389	\N	\N	\N	\N
2009-02-12 15:00:13.618+00	2009-02-12 15:00:13.659+00	23390	\N	\N	\N	\N
2009-02-12 15:00:13.73+00	\N	23392	\N	\N	\N	\N
2009-02-12 15:00:13.771+00	\N	23394	\N	\N	\N	\N
2009-02-12 15:00:13.938+00	2009-02-12 15:00:13.978+00	23395	\N	\N	\N	\N
2009-02-12 15:00:14.112+00	\N	23397	\N	\N	\N	\N
2009-02-12 15:00:14.152+00	\N	23399	\N	\N	\N	\N
2009-02-12 15:00:14.303+00	2009-02-12 15:00:14.343+00	23400	\N	\N	\N	\N
2009-02-12 15:00:14.538+00	\N	23402	\N	\N	\N	\N
2009-02-12 15:00:14.817+00	\N	23404	\N	\N	\N	\N
2009-02-12 15:00:14.954+00	2009-02-12 15:00:14.994+00	23405	\N	\N	\N	\N
2009-02-12 15:00:15.378+00	\N	23407	\N	\N	\N	\N
2009-02-12 15:00:15.419+00	\N	23409	\N	\N	\N	\N
2009-02-12 15:00:15.556+00	2009-02-12 15:00:15.596+00	23410	\N	\N	\N	\N
2009-02-12 15:00:16.068+00	\N	23412	\N	\N	\N	\N
2009-02-12 15:00:16.109+00	\N	23414	\N	\N	\N	\N
2009-02-12 15:00:16.246+00	2009-02-12 15:00:16.288+00	23415	\N	\N	\N	\N
2009-02-12 15:00:16.734+00	\N	23417	\N	\N	\N	\N
2009-02-12 15:00:16.795+00	\N	23419	\N	\N	\N	\N
2009-02-12 15:00:16.931+00	2009-02-12 15:00:16.972+00	23420	\N	\N	\N	\N
2009-02-12 15:00:17.356+00	\N	23422	\N	\N	\N	\N
2009-02-12 15:00:17.398+00	\N	23424	\N	\N	\N	\N
2009-02-12 15:00:17.537+00	2009-02-12 15:00:17.579+00	23425	\N	\N	\N	\N
2009-02-12 15:00:17.776+00	\N	23427	\N	\N	\N	\N
2009-02-12 15:00:17.816+00	\N	23429	\N	\N	\N	\N
2009-02-12 15:00:17.953+00	2009-02-12 15:00:17.993+00	23430	\N	\N	\N	\N
2009-02-12 15:00:18.188+00	\N	23432	\N	\N	\N	\N
2009-02-12 15:00:18.229+00	\N	23434	\N	\N	\N	\N
2009-02-12 15:00:18.43+00	2009-02-12 15:00:18.471+00	23435	\N	\N	\N	\N
2009-02-12 15:00:18.86+00	\N	23437	\N	\N	\N	\N
2009-02-12 15:00:18.913+00	\N	23439	\N	\N	\N	\N
2009-02-12 15:00:19.051+00	2009-02-12 15:00:19.092+00	23440	\N	\N	\N	\N
2009-02-12 15:00:19.483+00	\N	23442	\N	\N	\N	\N
2009-02-12 15:00:19.525+00	\N	23444	\N	\N	\N	\N
2009-02-12 15:00:19.666+00	2009-02-12 15:00:19.708+00	23445	\N	\N	\N	\N
2009-02-12 15:00:20.194+00	\N	23447	\N	\N	\N	\N
2009-02-12 15:00:20.236+00	\N	23449	\N	\N	\N	\N
2009-02-12 15:00:20.374+00	2009-02-12 15:00:20.415+00	23450	\N	\N	\N	\N
2009-02-12 15:00:20.615+00	\N	23452	\N	\N	\N	\N
2009-02-12 15:00:20.657+00	\N	23454	\N	\N	\N	\N
2009-02-12 15:00:20.798+00	2009-02-12 15:00:20.839+00	23455	\N	\N	\N	\N
2009-02-12 15:00:21.124+00	\N	23457	\N	\N	\N	\N
2009-02-12 15:00:21.166+00	\N	23459	\N	\N	\N	\N
2009-02-12 15:00:21.308+00	2009-02-12 15:00:21.35+00	23460	\N	\N	\N	\N
2009-02-12 15:00:21.618+00	\N	23462	\N	\N	\N	\N
2009-02-12 15:00:21.661+00	\N	23464	\N	\N	\N	\N
2009-02-12 15:00:21.804+00	2009-02-12 15:00:21.846+00	23465	\N	\N	\N	\N
2009-02-12 15:00:22.124+00	\N	23467	\N	\N	\N	\N
2009-02-12 15:00:22.165+00	\N	23469	\N	\N	\N	\N
2009-02-12 15:00:22.338+00	2009-02-12 15:00:22.379+00	23470	\N	\N	\N	\N
2009-02-12 15:00:22.518+00	\N	23472	\N	\N	\N	\N
2009-02-12 15:00:22.56+00	\N	23474	\N	\N	\N	\N
2009-02-12 15:00:22.703+00	2009-02-12 15:00:22.748+00	23475	\N	\N	\N	\N
2009-02-12 15:00:23.002+00	\N	23477	\N	\N	\N	\N
2009-02-12 15:00:23.044+00	\N	23479	\N	\N	\N	\N
2009-02-12 15:00:23.187+00	2009-02-12 15:00:23.23+00	23480	\N	\N	\N	\N
2009-02-12 15:00:23.468+00	\N	23482	\N	\N	\N	\N
2009-02-12 15:00:23.511+00	\N	23484	\N	\N	\N	\N
2009-02-12 15:00:23.655+00	2009-02-12 15:00:23.699+00	23485	\N	\N	\N	\N
2009-02-12 15:00:23.839+00	\N	23487	\N	\N	\N	\N
2009-02-12 15:00:23.88+00	\N	23489	\N	\N	\N	\N
2009-02-12 15:00:24.056+00	2009-02-12 15:00:24.096+00	23490	\N	\N	\N	\N
2009-02-12 15:00:24.479+00	\N	23492	\N	\N	\N	\N
2009-02-12 15:00:24.519+00	\N	23494	\N	\N	\N	\N
2009-02-12 15:00:24.696+00	2009-02-12 15:00:24.739+00	23495	\N	\N	\N	\N
2009-02-12 15:00:25.175+00	\N	23497	\N	\N	\N	\N
2009-02-12 15:00:25.219+00	\N	23499	\N	\N	\N	\N
2009-02-12 15:00:25.397+00	2009-02-12 15:00:25.44+00	23500	\N	\N	\N	\N
2009-02-12 15:00:25.906+00	\N	23502	\N	\N	\N	\N
2009-02-12 15:00:25.947+00	\N	23504	\N	\N	\N	\N
2009-02-12 15:00:26.084+00	2009-02-12 15:00:26.125+00	23505	\N	\N	\N	\N
2009-02-12 15:00:26.54+00	\N	23507	\N	\N	\N	\N
2009-02-12 15:00:26.583+00	\N	23509	\N	\N	\N	\N
2009-02-12 15:00:26.727+00	2009-02-12 15:00:26.769+00	23510	\N	\N	\N	\N
2009-02-12 15:00:27.112+00	\N	23512	\N	\N	\N	\N
2009-02-12 15:00:27.155+00	\N	23514	\N	\N	\N	\N
2009-02-12 15:00:27.302+00	2009-02-12 15:00:27.346+00	23515	\N	\N	\N	\N
2009-02-12 15:00:27.488+00	\N	23517	\N	\N	\N	\N
2009-02-12 15:00:27.541+00	\N	23519	\N	\N	\N	\N
2009-02-12 15:00:27.686+00	2009-02-12 15:00:27.729+00	23520	\N	\N	\N	\N
2009-02-12 15:00:27.871+00	\N	23522	\N	\N	\N	\N
2009-02-12 15:00:27.914+00	\N	23524	\N	\N	\N	\N
2009-02-12 15:00:28.059+00	2009-02-12 15:00:28.103+00	23525	\N	\N	\N	\N
2009-02-12 15:00:28.231+00	\N	23527	\N	\N	\N	\N
2009-02-12 15:00:28.274+00	\N	23529	\N	\N	\N	\N
2009-02-12 15:00:28.419+00	2009-02-12 15:00:28.463+00	23530	\N	\N	\N	\N
2009-02-12 15:00:28.574+00	\N	23532	\N	\N	\N	\N
2009-02-12 15:00:28.617+00	\N	23534	\N	\N	\N	\N
2009-02-12 15:00:28.764+00	2009-02-12 15:00:28.808+00	23535	\N	\N	\N	\N
2009-02-12 15:00:28.953+00	\N	23537	\N	\N	\N	\N
2009-02-12 15:00:28.997+00	\N	23539	\N	\N	\N	\N
2009-02-12 15:00:29.179+00	2009-02-12 15:00:29.222+00	23540	\N	\N	\N	\N
2009-02-12 15:00:29.399+00	\N	23542	\N	\N	\N	\N
2009-02-12 15:00:29.442+00	\N	23544	\N	\N	\N	\N
2009-02-12 15:00:29.588+00	2009-02-12 15:00:29.632+00	23545	\N	\N	\N	\N
2009-02-12 15:00:29.777+00	\N	23547	\N	\N	\N	\N
2009-02-12 15:00:29.83+00	\N	23549	\N	\N	\N	\N
2009-02-12 15:00:29.977+00	2009-02-12 15:00:30.021+00	23550	\N	\N	\N	\N
2009-02-12 15:00:30.267+00	\N	23552	\N	\N	\N	\N
2009-02-12 15:00:30.312+00	\N	23554	\N	\N	\N	\N
2009-02-12 15:00:30.495+00	2009-02-12 15:00:30.54+00	23555	\N	\N	\N	\N
2009-02-12 15:00:30.856+00	\N	23557	\N	\N	\N	\N
2009-02-12 15:00:30.90+00	\N	23559	\N	\N	\N	\N
2009-02-12 15:00:31.054+00	2009-02-12 15:00:31.096+00	23560	\N	\N	\N	\N
2009-02-12 15:00:31.173+00	\N	23562	\N	\N	\N	\N
2009-02-12 15:00:31.216+00	\N	23564	\N	\N	\N	\N
2009-02-12 15:00:31.445+00	2009-02-12 15:00:31.488+00	23565	\N	\N	\N	\N
2009-02-12 15:00:31.874+00	\N	23567	\N	\N	\N	\N
2009-02-12 15:00:31.92+00	\N	23569	\N	\N	\N	\N
2009-02-12 15:00:32.14+00	2009-02-12 15:00:32.185+00	23570	\N	\N	\N	\N
2009-02-12 15:00:32.333+00	\N	23572	\N	\N	\N	\N
2009-02-12 15:00:32.377+00	\N	23574	\N	\N	\N	\N
2009-02-12 15:00:32.526+00	2009-02-12 15:00:32.57+00	23575	\N	\N	\N	\N
2009-02-12 15:00:32.819+00	\N	23577	\N	\N	\N	\N
2009-02-12 15:00:32.863+00	\N	23579	\N	\N	\N	\N
2009-02-12 15:00:33.034+00	2009-02-12 15:00:33.078+00	23580	\N	\N	\N	\N
2009-02-12 15:00:33.157+00	\N	23582	\N	\N	\N	\N
2009-02-12 15:00:33.202+00	\N	23584	\N	\N	\N	\N
2009-02-12 15:00:33.354+00	2009-02-12 15:00:33.399+00	23585	\N	\N	\N	\N
2009-02-12 15:00:33.479+00	\N	23587	\N	\N	\N	\N
2009-02-12 15:00:33.525+00	\N	23589	\N	\N	\N	\N
2009-02-12 15:00:33.679+00	2009-02-12 15:00:33.725+00	23590	\N	\N	\N	\N
2009-02-12 15:00:33.805+00	\N	23592	\N	\N	\N	\N
2009-02-12 15:00:33.849+00	\N	23594	\N	\N	\N	\N
2009-02-12 15:00:34.01+00	2009-02-12 15:00:34.055+00	23595	\N	\N	\N	\N
2009-02-12 15:00:34.133+00	\N	23597	\N	\N	\N	\N
2009-02-12 15:00:34.177+00	\N	23599	\N	\N	\N	\N
2009-02-12 15:00:34.382+00	2009-02-12 15:00:34.427+00	23600	\N	\N	\N	\N
2009-02-12 15:00:34.857+00	\N	23602	\N	\N	\N	\N
2009-02-12 15:00:34.904+00	\N	23604	\N	\N	\N	\N
2009-02-12 15:00:35.058+00	2009-02-12 15:00:35.104+00	23605	\N	\N	\N	\N
2009-02-12 15:00:35.22+00	\N	23607	\N	\N	\N	\N
2009-02-12 15:00:35.265+00	\N	23609	\N	\N	\N	\N
2009-02-12 15:00:35.453+00	2009-02-12 15:00:35.498+00	23610	\N	\N	\N	\N
2009-02-12 15:00:35.867+00	\N	23612	\N	\N	\N	\N
2009-02-12 15:00:35.913+00	\N	23614	\N	\N	\N	\N
2009-02-12 15:00:36.103+00	2009-02-12 15:00:36.149+00	23615	\N	\N	\N	\N
2009-02-12 15:00:36.442+00	\N	23617	\N	\N	\N	\N
2009-02-12 15:00:36.488+00	\N	23619	\N	\N	\N	\N
2009-02-12 15:00:36.645+00	2009-02-12 15:00:36.691+00	23620	\N	\N	\N	\N
2009-02-12 15:00:36.912+00	\N	23622	\N	\N	\N	\N
2009-02-12 15:00:36.957+00	\N	23624	\N	\N	\N	\N
2009-02-12 15:00:37.111+00	2009-02-12 15:00:37.165+00	23625	\N	\N	\N	\N
2009-02-12 15:00:37.459+00	\N	23627	\N	\N	\N	\N
2009-02-12 15:00:37.505+00	\N	23629	\N	\N	\N	\N
2009-02-12 15:00:37.661+00	2009-02-12 15:00:37.708+00	23630	\N	\N	\N	\N
2009-02-12 15:00:38.255+00	\N	23632	\N	\N	\N	\N
2009-02-12 15:00:38.301+00	\N	23634	\N	\N	\N	\N
2009-02-12 15:00:38.457+00	2009-02-12 15:00:38.517+00	23635	\N	\N	\N	\N
2009-02-12 15:00:38.742+00	\N	23637	\N	\N	\N	\N
2009-02-12 15:00:38.788+00	\N	23639	\N	\N	\N	\N
2009-02-12 15:00:38.945+00	2009-02-12 15:00:38.992+00	23640	\N	\N	\N	\N
2009-02-12 15:00:39.218+00	\N	23642	\N	\N	\N	\N
2009-02-12 15:00:39.265+00	\N	23644	\N	\N	\N	\N
2009-02-12 15:00:39.423+00	2009-02-12 15:00:39.469+00	23645	\N	\N	\N	\N
2009-02-12 15:00:39.812+00	\N	23647	\N	\N	\N	\N
2009-02-12 15:00:39.858+00	\N	23649	\N	\N	\N	\N
2009-02-12 15:00:40.015+00	2009-02-12 15:00:40.061+00	23650	\N	\N	\N	\N
2009-02-12 15:00:40.288+00	\N	23652	\N	\N	\N	\N
2009-02-12 15:00:40.336+00	\N	23654	\N	\N	\N	\N
2009-02-12 15:00:40.495+00	2009-02-12 15:00:40.542+00	23655	\N	\N	\N	\N
2009-02-12 15:00:40.625+00	\N	23657	\N	\N	\N	\N
2009-02-12 15:00:40.672+00	\N	23659	\N	\N	\N	\N
2009-02-12 15:00:40.829+00	2009-02-12 15:00:40.874+00	23660	\N	\N	\N	\N
2009-02-12 15:00:40.966+00	\N	23662	\N	\N	\N	\N
2009-02-12 15:00:41.012+00	\N	23664	\N	\N	\N	\N
2009-02-12 15:00:41.17+00	2009-02-12 15:00:41.216+00	23665	\N	\N	\N	\N
2009-02-12 15:00:41.30+00	\N	23667	\N	\N	\N	\N
2009-02-12 15:00:41.347+00	\N	23669	\N	\N	\N	\N
2009-02-12 15:00:41.505+00	2009-02-12 15:00:41.553+00	23670	\N	\N	\N	\N
2009-02-12 15:00:41.638+00	\N	23672	\N	\N	\N	\N
2009-02-12 15:00:41.685+00	\N	23674	\N	\N	\N	\N
2009-02-12 15:00:41.846+00	2009-02-12 15:00:41.892+00	23675	\N	\N	\N	\N
2009-02-12 15:00:41.975+00	\N	23677	\N	\N	\N	\N
2009-02-12 15:00:42.021+00	\N	23679	\N	\N	\N	\N
2009-02-12 15:00:42.188+00	2009-02-12 15:00:42.235+00	23680	\N	\N	\N	\N
2009-02-12 15:00:42.319+00	\N	23682	\N	\N	\N	\N
2009-02-12 15:00:42.366+00	\N	23684	\N	\N	\N	\N
2009-02-12 15:00:42.527+00	2009-02-12 15:00:42.575+00	23685	\N	\N	\N	\N
2009-02-12 15:00:42.659+00	\N	23687	\N	\N	\N	\N
2009-02-12 15:00:42.707+00	\N	23689	\N	\N	\N	\N
2009-02-12 15:00:42.869+00	2009-02-12 15:00:42.917+00	23690	\N	\N	\N	\N
2009-02-12 15:00:43.002+00	\N	23692	\N	\N	\N	\N
2009-02-12 15:00:43.048+00	\N	23694	\N	\N	\N	\N
2009-02-12 15:00:43.219+00	2009-02-12 15:00:43.266+00	23695	\N	\N	\N	\N
2009-02-12 15:00:43.349+00	\N	23697	\N	\N	\N	\N
2009-02-12 15:00:43.396+00	\N	23699	\N	\N	\N	\N
2009-02-12 15:00:43.559+00	2009-02-12 15:00:43.606+00	23700	\N	\N	\N	\N
2009-02-12 15:00:43.691+00	\N	23702	\N	\N	\N	\N
2009-02-12 15:00:43.739+00	\N	23704	\N	\N	\N	\N
2009-02-12 15:00:43.903+00	2009-02-12 15:00:43.951+00	23705	\N	\N	\N	\N
2009-02-12 15:00:44.037+00	\N	23707	\N	\N	\N	\N
2009-02-12 15:00:44.084+00	\N	23709	\N	\N	\N	\N
2009-02-12 15:00:44.257+00	2009-02-12 15:00:44.304+00	23710	\N	\N	\N	\N
2009-02-12 15:00:44.389+00	\N	23712	\N	\N	\N	\N
2009-02-12 15:00:44.436+00	\N	23714	\N	\N	\N	\N
2009-02-12 15:00:44.60+00	2009-02-12 15:00:44.647+00	23715	\N	\N	\N	\N
2009-02-12 15:00:44.733+00	\N	23717	\N	\N	\N	\N
2009-02-12 15:00:44.781+00	\N	23719	\N	\N	\N	\N
2009-02-12 15:00:44.946+00	2009-02-12 15:00:44.995+00	23720	\N	\N	\N	\N
2009-02-12 15:00:45.081+00	\N	23722	\N	\N	\N	\N
2009-02-12 15:00:45.128+00	\N	23724	\N	\N	\N	\N
2009-02-12 15:00:45.301+00	2009-02-12 15:00:45.349+00	23725	\N	\N	\N	\N
2009-02-12 15:00:45.482+00	\N	23729	\N	\N	\N	\N
2009-02-12 15:00:45.647+00	2009-02-12 15:00:45.695+00	23730	\N	\N	\N	\N
2009-02-12 15:00:45.781+00	\N	23732	\N	\N	\N	\N
2009-02-12 15:00:45.83+00	\N	23734	\N	\N	\N	\N
2009-02-12 15:00:45.996+00	2009-02-12 15:00:46.045+00	23735	\N	\N	\N	\N
2009-02-12 15:00:46.132+00	\N	23737	\N	\N	\N	\N
2009-02-12 15:00:46.179+00	\N	23739	\N	\N	\N	\N
2009-02-12 15:00:46.352+00	2009-02-12 15:00:46.402+00	23740	\N	\N	\N	\N
2009-02-12 15:00:46.488+00	\N	23742	\N	\N	\N	\N
2009-02-12 15:00:46.537+00	\N	23744	\N	\N	\N	\N
2009-02-12 15:00:46.703+00	2009-02-12 15:00:46.753+00	23745	\N	\N	\N	\N
2009-02-12 15:00:46.84+00	\N	23747	\N	\N	\N	\N
2009-02-12 15:00:46.889+00	\N	23749	\N	\N	\N	\N
2009-02-12 15:00:47.132+00	2009-02-12 15:00:47.191+00	23750	\N	\N	\N	\N
2009-02-12 15:00:47.315+00	\N	23752	\N	\N	\N	\N
2009-02-12 15:00:47.364+00	\N	23754	\N	\N	\N	\N
2009-02-12 15:00:47.568+00	2009-02-12 15:00:47.616+00	23755	\N	\N	\N	\N
2009-02-12 15:00:47.704+00	\N	23757	\N	\N	\N	\N
2009-02-12 15:00:47.754+00	\N	23759	\N	\N	\N	\N
2009-02-12 15:00:47.921+00	2009-02-12 15:00:47.971+00	23760	\N	\N	\N	\N
2009-02-12 15:00:48.221+00	\N	23762	\N	\N	\N	\N
2009-02-12 15:00:48.27+00	\N	23764	\N	\N	\N	\N
2009-02-12 15:00:48.437+00	2009-02-12 15:00:48.489+00	23765	\N	\N	\N	\N
2009-02-12 15:00:48.692+00	\N	23767	\N	\N	\N	\N
2009-02-12 15:00:48.742+00	\N	23769	\N	\N	\N	\N
2009-02-12 15:00:48.91+00	2009-02-12 15:00:48.96+00	23770	\N	\N	\N	\N
2009-02-12 15:00:49.058+00	\N	23772	\N	\N	\N	\N
2009-02-12 15:00:49.107+00	\N	23774	\N	\N	\N	\N
2009-02-12 15:00:49.311+00	2009-02-12 15:00:49.361+00	23775	\N	\N	\N	\N
2009-02-12 15:00:49.526+00	\N	23777	\N	\N	\N	\N
2009-02-12 15:00:49.576+00	\N	23779	\N	\N	\N	\N
2009-02-12 15:00:49.745+00	2009-02-12 15:00:49.796+00	23780	\N	\N	\N	\N
2009-02-12 15:00:49.884+00	\N	23782	\N	\N	\N	\N
2009-02-12 15:00:49.94+00	\N	23784	\N	\N	\N	\N
2009-02-12 15:00:50.108+00	2009-02-12 15:00:50.158+00	23785	\N	\N	\N	\N
2009-02-12 15:00:50.401+00	\N	23787	\N	\N	\N	\N
2009-02-12 15:00:50.451+00	\N	23789	\N	\N	\N	\N
2009-02-12 15:00:50.622+00	2009-02-12 15:00:50.672+00	23790	\N	\N	\N	\N
2009-02-12 15:00:50.927+00	\N	23792	\N	\N	\N	\N
2009-02-12 15:00:50.976+00	\N	23794	\N	\N	\N	\N
2009-02-12 15:00:51.146+00	2009-02-12 15:00:51.196+00	23795	\N	\N	\N	\N
2009-02-12 15:00:51.441+00	\N	23797	\N	\N	\N	\N
2009-02-12 15:00:51.492+00	\N	23799	\N	\N	\N	\N
2009-02-12 15:00:51.749+00	2009-02-12 15:00:51.799+00	23800	\N	\N	\N	\N
2009-02-12 15:00:51.966+00	\N	23802	\N	\N	\N	\N
2009-02-12 15:00:52.017+00	\N	23804	\N	\N	\N	\N
2009-02-12 15:00:52.187+00	2009-02-12 15:00:52.238+00	23805	\N	\N	\N	\N
2009-02-12 15:00:52.328+00	\N	23807	\N	\N	\N	\N
2009-02-12 15:00:52.378+00	\N	23809	\N	\N	\N	\N
2009-02-12 15:00:52.559+00	2009-02-12 15:00:52.608+00	23810	\N	\N	\N	\N
2009-02-12 15:00:52.697+00	\N	23812	\N	\N	\N	\N
2009-02-12 15:00:52.747+00	\N	23814	\N	\N	\N	\N
2009-02-12 15:00:52.918+00	2009-02-12 15:00:52.969+00	23815	\N	\N	\N	\N
2009-02-12 15:00:53.177+00	\N	23817	\N	\N	\N	\N
2009-02-12 15:00:53.238+00	\N	23819	\N	\N	\N	\N
2009-02-12 15:00:53.487+00	2009-02-12 15:00:53.538+00	23820	\N	\N	\N	\N
2009-02-12 15:00:53.628+00	\N	23822	\N	\N	\N	\N
2009-02-12 15:00:53.677+00	\N	23824	\N	\N	\N	\N
2009-02-12 15:00:53.928+00	2009-02-12 15:00:53.979+00	23825	\N	\N	\N	\N
2009-02-12 15:00:54.237+00	\N	23827	\N	\N	\N	\N
2009-02-12 15:00:54.287+00	\N	23829	\N	\N	\N	\N
2009-02-12 15:00:54.498+00	2009-02-12 15:00:54.549+00	23830	\N	\N	\N	\N
2009-02-12 15:00:54.638+00	\N	23832	\N	\N	\N	\N
2009-02-12 15:00:54.689+00	\N	23834	\N	\N	\N	\N
2009-02-12 15:00:54.863+00	2009-02-12 15:00:54.914+00	23835	\N	\N	\N	\N
2009-02-12 15:00:55.164+00	\N	23837	\N	\N	\N	\N
2009-02-12 15:00:55.214+00	\N	23839	\N	\N	\N	\N
2009-02-12 15:00:55.396+00	2009-02-12 15:00:55.445+00	23840	\N	\N	\N	\N
2009-02-12 15:00:55.615+00	\N	23842	\N	\N	\N	\N
2009-02-12 15:00:55.666+00	\N	23844	\N	\N	\N	\N
2009-02-12 15:00:55.84+00	2009-02-12 15:00:55.891+00	23845	\N	\N	\N	\N
2009-02-12 15:00:56.222+00	\N	23847	\N	\N	\N	\N
2009-02-12 15:00:56.272+00	\N	23849	\N	\N	\N	\N
2009-02-12 15:00:56.453+00	2009-02-12 15:00:56.504+00	23850	\N	\N	\N	\N
2009-02-12 15:00:56.955+00	\N	23852	\N	\N	\N	\N
2009-02-12 15:00:57.006+00	\N	23854	\N	\N	\N	\N
2009-02-12 15:00:57.182+00	2009-02-12 15:00:57.233+00	23855	\N	\N	\N	\N
2009-02-12 15:00:57.575+00	\N	23857	\N	\N	\N	\N
2009-02-12 15:00:57.626+00	\N	23859	\N	\N	\N	\N
2009-02-12 15:00:57.799+00	2009-02-12 15:00:57.851+00	23860	\N	\N	\N	\N
2009-02-12 15:00:58.263+00	\N	23862	\N	\N	\N	\N
2009-02-12 15:00:58.316+00	\N	23864	\N	\N	\N	\N
2009-02-12 15:00:58.532+00	2009-02-12 15:00:58.593+00	23865	\N	\N	\N	\N
2009-02-12 15:00:58.683+00	\N	23867	\N	\N	\N	\N
2009-02-12 15:00:58.734+00	\N	23869	\N	\N	\N	\N
2009-02-12 15:00:58.95+00	2009-02-12 15:00:59.001+00	23870	\N	\N	\N	\N
2009-02-12 15:00:59.216+00	\N	23872	\N	\N	\N	\N
2009-02-12 15:00:59.268+00	\N	23874	\N	\N	\N	\N
2009-02-12 15:00:59.486+00	2009-02-12 15:00:59.537+00	23875	\N	\N	\N	\N
2009-02-12 15:00:59.682+00	\N	23877	\N	\N	\N	\N
2009-02-12 15:00:59.732+00	\N	23879	\N	\N	\N	\N
2009-02-12 15:00:59.95+00	2009-02-12 15:01:00.002+00	23880	\N	\N	\N	\N
2009-02-12 15:01:00.094+00	\N	23882	\N	\N	\N	\N
2009-02-12 15:01:00.146+00	\N	23884	\N	\N	\N	\N
2009-02-12 15:01:00.325+00	2009-02-12 15:01:00.377+00	23885	\N	\N	\N	\N
2009-02-12 15:01:00.59+00	\N	23887	\N	\N	\N	\N
2009-02-12 15:01:00.668+00	\N	23889	\N	\N	\N	\N
2009-02-12 15:01:00.849+00	2009-02-12 15:01:00.90+00	23890	\N	\N	\N	\N
2009-02-12 15:01:00.995+00	\N	23892	\N	\N	\N	\N
2009-02-12 15:01:01.051+00	\N	23894	\N	\N	\N	\N
2009-02-12 15:01:01.246+00	2009-02-12 15:01:01.302+00	23895	\N	\N	\N	\N
2009-02-12 15:01:01.623+00	\N	23897	\N	\N	\N	\N
2009-02-12 15:01:01.681+00	\N	23899	\N	\N	\N	\N
2009-02-12 15:01:01.899+00	2009-02-12 15:01:01.957+00	23900	\N	\N	\N	\N
2009-02-12 15:01:02.50+00	\N	23902	\N	\N	\N	\N
2009-02-12 15:01:02.557+00	\N	23904	\N	\N	\N	\N
2009-02-12 15:01:02.754+00	2009-02-12 15:01:02.81+00	23905	\N	\N	\N	\N
2009-02-12 15:01:03.463+00	\N	23907	\N	\N	\N	\N
2009-02-12 15:01:03.523+00	\N	23909	\N	\N	\N	\N
2009-02-12 15:01:03.722+00	2009-02-12 15:01:03.778+00	23910	\N	\N	\N	\N
2009-02-12 15:01:04.364+00	\N	23912	\N	\N	\N	\N
2009-02-12 15:01:04.422+00	\N	23914	\N	\N	\N	\N
2009-02-12 15:01:04.623+00	2009-02-12 15:01:04.682+00	23915	\N	\N	\N	\N
2009-02-12 15:01:04.832+00	\N	23917	\N	\N	\N	\N
2009-02-12 15:01:04.899+00	\N	23919	\N	\N	\N	\N
2009-02-12 15:01:05.188+00	2009-02-12 15:01:05.246+00	23920	\N	\N	\N	\N
2009-02-12 15:01:05.613+00	\N	23922	\N	\N	\N	\N
2009-02-12 15:01:05.67+00	\N	23924	\N	\N	\N	\N
2009-02-12 15:01:05.87+00	2009-02-12 15:01:05.928+00	23925	\N	\N	\N	\N
2009-02-12 15:01:06.033+00	\N	23927	\N	\N	\N	\N
2009-02-12 15:01:06.091+00	\N	23929	\N	\N	\N	\N
2009-02-12 15:01:06.351+00	2009-02-12 15:01:06.411+00	23930	\N	\N	\N	\N
2009-02-12 15:01:06.963+00	\N	23932	\N	\N	\N	\N
2009-02-12 15:01:07.021+00	\N	23934	\N	\N	\N	\N
2009-02-12 15:01:07.265+00	2009-02-12 15:01:07.322+00	23935	\N	\N	\N	\N
2009-02-12 15:01:07.427+00	\N	23937	\N	\N	\N	\N
2009-02-12 15:01:07.485+00	\N	23939	\N	\N	\N	\N
2009-02-12 15:01:07.748+00	2009-02-12 15:01:07.807+00	23940	\N	\N	\N	\N
2009-02-12 15:01:07.91+00	\N	23942	\N	\N	\N	\N
2009-02-12 15:01:07.967+00	\N	23944	\N	\N	\N	\N
2009-02-12 15:01:08.166+00	2009-02-12 15:01:08.225+00	23945	\N	\N	\N	\N
2009-02-12 15:01:08.871+00	\N	23947	\N	\N	\N	\N
2009-02-12 15:01:08.932+00	\N	23949	\N	\N	\N	\N
2009-02-12 15:01:09.142+00	2009-02-12 15:01:09.20+00	23950	\N	\N	\N	\N
2009-02-12 15:01:09.575+00	\N	23952	\N	\N	\N	\N
2009-02-12 15:01:09.633+00	\N	23954	\N	\N	\N	\N
2009-02-12 15:01:09.834+00	2009-02-12 15:01:09.891+00	23955	\N	\N	\N	\N
2009-02-12 15:01:09.993+00	\N	23957	\N	\N	\N	\N
2009-02-12 15:01:10.052+00	\N	23959	\N	\N	\N	\N
2009-02-12 15:01:10.271+00	2009-02-12 15:01:10.33+00	23960	\N	\N	\N	\N
2009-02-12 15:01:10.616+00	\N	23962	\N	\N	\N	\N
2009-02-12 15:01:10.673+00	\N	23964	\N	\N	\N	\N
2009-02-12 15:01:10.923+00	2009-02-12 15:01:10.98+00	23965	\N	\N	\N	\N
2009-02-12 15:01:11.082+00	\N	23967	\N	\N	\N	\N
2009-02-12 15:01:11.139+00	\N	23969	\N	\N	\N	\N
2009-02-12 15:01:11.345+00	2009-02-12 15:01:11.407+00	23970	\N	\N	\N	\N
2009-02-12 15:01:11.522+00	\N	23972	\N	\N	\N	\N
2009-02-12 15:01:11.581+00	\N	23974	\N	\N	\N	\N
2009-02-12 15:01:11.831+00	2009-02-12 15:01:11.888+00	23975	\N	\N	\N	\N
2009-02-12 15:01:12.084+00	\N	23977	\N	\N	\N	\N
2009-02-12 15:01:12.141+00	\N	23979	\N	\N	\N	\N
2009-02-12 15:01:12.39+00	2009-02-12 15:01:12.448+00	23980	\N	\N	\N	\N
2009-02-12 15:01:12.659+00	\N	23982	\N	\N	\N	\N
2009-02-12 15:01:12.721+00	\N	23984	\N	\N	\N	\N
2009-02-12 15:01:12.926+00	2009-02-12 15:01:12.984+00	23985	\N	\N	\N	\N
2009-02-12 15:01:13.50+00	\N	23987	\N	\N	\N	\N
2009-02-12 15:01:13.559+00	\N	23989	\N	\N	\N	\N
2009-02-12 15:01:13.823+00	2009-02-12 15:01:13.884+00	23990	\N	\N	\N	\N
2009-02-12 15:01:14.127+00	\N	23992	\N	\N	\N	\N
2009-02-12 15:01:14.185+00	\N	23994	\N	\N	\N	\N
2009-02-12 15:01:14.483+00	2009-02-12 15:01:14.54+00	23995	\N	\N	\N	\N
2009-02-12 15:01:14.784+00	\N	23997	\N	\N	\N	\N
2009-02-12 15:01:14.845+00	\N	23999	\N	\N	\N	\N
2009-02-12 15:01:15.116+00	2009-02-12 15:01:15.177+00	24000	\N	\N	\N	\N
2009-02-12 15:01:15.745+00	\N	24002	\N	\N	\N	\N
2009-02-12 15:01:15.805+00	\N	24004	\N	\N	\N	\N
2009-02-12 15:01:16.068+00	2009-02-12 15:01:16.128+00	24005	\N	\N	\N	\N
2009-02-12 15:01:16.283+00	\N	24007	\N	\N	\N	\N
2009-02-12 15:01:16.341+00	\N	24009	\N	\N	\N	\N
2009-02-12 15:01:16.592+00	2009-02-12 15:01:16.65+00	24010	\N	\N	\N	\N
2009-02-12 15:01:17.185+00	\N	24012	\N	\N	\N	\N
2009-02-12 15:01:17.247+00	\N	24014	\N	\N	\N	\N
2009-02-12 15:01:17.452+00	2009-02-12 15:01:17.513+00	24015	\N	\N	\N	\N
2009-02-12 15:01:18.203+00	\N	24019	\N	\N	\N	\N
2009-02-12 15:01:18.411+00	2009-02-12 15:01:18.469+00	24020	\N	\N	\N	\N
2009-02-12 15:01:18.854+00	\N	24022	\N	\N	\N	\N
2009-02-12 15:01:18.914+00	\N	24024	\N	\N	\N	\N
2009-02-12 15:01:19.227+00	2009-02-12 15:01:19.286+00	24025	\N	\N	\N	\N
2009-02-12 15:01:19.438+00	\N	24027	\N	\N	\N	\N
2009-02-12 15:01:19.498+00	\N	24029	\N	\N	\N	\N
2009-02-12 15:01:19.845+00	2009-02-12 15:01:19.905+00	24030	\N	\N	\N	\N
2009-02-12 15:01:20.116+00	\N	24032	\N	\N	\N	\N
2009-02-12 15:01:20.176+00	\N	24034	\N	\N	\N	\N
2009-02-12 15:01:20.526+00	2009-02-12 15:01:20.585+00	24035	\N	\N	\N	\N
2009-02-12 15:01:20.879+00	\N	24037	\N	\N	\N	\N
2009-02-12 15:01:20.95+00	\N	24039	\N	\N	\N	\N
2009-02-12 15:01:21.161+00	2009-02-12 15:01:21.224+00	24040	\N	\N	\N	\N
2009-02-12 15:01:21.567+00	\N	24042	\N	\N	\N	\N
2009-02-12 15:01:21.628+00	\N	24044	\N	\N	\N	\N
2009-02-12 15:01:21.848+00	2009-02-12 15:01:21.911+00	24045	\N	\N	\N	\N
2009-02-12 15:01:22.255+00	\N	24047	\N	\N	\N	\N
2009-02-12 15:01:22.316+00	\N	24049	\N	\N	\N	\N
2009-02-12 15:01:22.522+00	2009-02-12 15:01:22.582+00	24050	\N	\N	\N	\N
2009-02-12 15:01:22.94+00	\N	24052	\N	\N	\N	\N
2009-02-12 15:01:23+00	\N	24054	\N	\N	\N	\N
2009-02-12 15:01:23.256+00	2009-02-12 15:01:23.315+00	24055	\N	\N	\N	\N
2009-02-12 15:01:23.766+00	\N	24057	\N	\N	\N	\N
2009-02-12 15:01:23.827+00	\N	24059	\N	\N	\N	\N
2009-02-12 15:01:24.131+00	2009-02-12 15:01:24.19+00	24060	\N	\N	\N	\N
2009-02-12 15:01:24.545+00	\N	24062	\N	\N	\N	\N
2009-02-12 15:01:24.608+00	\N	24064	\N	\N	\N	\N
2009-02-12 15:01:24.821+00	2009-02-12 15:01:24.882+00	24065	\N	\N	\N	\N
2009-02-12 15:01:25.131+00	\N	24067	\N	\N	\N	\N
2009-02-12 15:01:25.191+00	\N	24069	\N	\N	\N	\N
2009-02-12 15:01:25.402+00	2009-02-12 15:01:25.469+00	24070	\N	\N	\N	\N
2009-02-12 15:01:25.627+00	\N	24072	\N	\N	\N	\N
2009-02-12 15:01:25.687+00	\N	24074	\N	\N	\N	\N
2009-02-12 15:01:25.995+00	2009-02-12 15:01:26.055+00	24075	\N	\N	\N	\N
2009-02-12 15:01:26.258+00	\N	24077	\N	\N	\N	\N
2009-02-12 15:01:26.318+00	\N	24079	\N	\N	\N	\N
2009-02-12 15:01:26.589+00	2009-02-12 15:01:26.65+00	24080	\N	\N	\N	\N
2009-02-12 15:01:26.951+00	\N	24082	\N	\N	\N	\N
2009-02-12 15:01:27.011+00	\N	24084	\N	\N	\N	\N
2009-02-12 15:01:27.27+00	2009-02-12 15:01:27.343+00	24085	\N	\N	\N	\N
2009-02-12 15:01:27.549+00	\N	24087	\N	\N	\N	\N
2009-02-12 15:01:27.611+00	\N	24089	\N	\N	\N	\N
2009-02-12 15:01:27.825+00	2009-02-12 15:01:27.885+00	24090	\N	\N	\N	\N
2009-02-12 15:01:28.299+00	\N	24092	\N	\N	\N	\N
2009-02-12 15:01:28.361+00	\N	24094	\N	\N	\N	\N
2009-02-12 15:01:28.62+00	2009-02-12 15:01:28.682+00	24095	\N	\N	\N	\N
2009-02-12 15:01:28.983+00	\N	24097	\N	\N	\N	\N
2009-02-12 15:01:29.044+00	\N	24099	\N	\N	\N	\N
2009-02-12 15:01:29.367+00	2009-02-12 15:01:29.429+00	24100	\N	\N	\N	\N
2009-02-12 15:01:29.635+00	\N	24102	\N	\N	\N	\N
2009-02-12 15:01:29.697+00	\N	24104	\N	\N	\N	\N
2009-02-12 15:01:30.008+00	2009-02-12 15:01:30.069+00	24105	\N	\N	\N	\N
2009-02-12 15:01:30.278+00	\N	24107	\N	\N	\N	\N
2009-02-12 15:01:30.341+00	\N	24109	\N	\N	\N	\N
2009-02-12 15:01:30.571+00	2009-02-12 15:01:30.634+00	24110	\N	\N	\N	\N
2009-02-12 15:01:31.037+00	\N	24112	\N	\N	\N	\N
2009-02-12 15:01:31.098+00	\N	24114	\N	\N	\N	\N
2009-02-12 15:01:31.314+00	2009-02-12 15:01:31.375+00	24115	\N	\N	\N	\N
2009-02-12 15:01:31.93+00	\N	24117	\N	\N	\N	\N
2009-02-12 15:01:31.995+00	\N	24119	\N	\N	\N	\N
2009-02-12 15:01:32.225+00	2009-02-12 15:01:32.286+00	24120	\N	\N	\N	\N
2009-02-12 15:01:32.785+00	\N	24122	\N	\N	\N	\N
2009-02-12 15:01:32.847+00	\N	24124	\N	\N	\N	\N
2009-02-12 15:01:33.11+00	2009-02-12 15:01:33.174+00	24125	\N	\N	\N	\N
2009-02-12 15:01:33.647+00	\N	24127	\N	\N	\N	\N
2009-02-12 15:01:33.714+00	\N	24129	\N	\N	\N	\N
2009-02-12 15:01:33.928+00	2009-02-12 15:01:33.99+00	24130	\N	\N	\N	\N
2009-02-12 15:01:34.345+00	\N	24132	\N	\N	\N	\N
2009-02-12 15:01:34.406+00	\N	24134	\N	\N	\N	\N
2009-02-12 15:01:34.622+00	2009-02-12 15:01:34.687+00	24135	\N	\N	\N	\N
2009-02-12 15:01:35.063+00	\N	24137	\N	\N	\N	\N
2009-02-12 15:01:35.127+00	\N	24139	\N	\N	\N	\N
2009-02-12 15:01:35.394+00	2009-02-12 15:01:35.458+00	24140	\N	\N	\N	\N
2009-02-12 15:01:35.813+00	\N	24142	\N	\N	\N	\N
2009-02-12 15:01:35.875+00	\N	24144	\N	\N	\N	\N
2009-02-12 15:01:36.093+00	2009-02-12 15:01:36.158+00	24145	\N	\N	\N	\N
2009-02-12 15:01:36.274+00	\N	24147	\N	\N	\N	\N
2009-02-12 15:01:36.347+00	\N	24149	\N	\N	\N	\N
2009-02-12 15:01:36.567+00	2009-02-12 15:01:36.629+00	24150	\N	\N	\N	\N
2009-02-12 15:01:36.742+00	\N	24152	\N	\N	\N	\N
2009-02-12 15:01:36.805+00	\N	24154	\N	\N	\N	\N
2009-02-12 15:01:37.021+00	2009-02-12 15:01:37.084+00	24155	\N	\N	\N	\N
2009-02-12 15:01:37.194+00	\N	24157	\N	\N	\N	\N
2009-02-12 15:01:37.258+00	\N	24159	\N	\N	\N	\N
2009-02-12 15:01:37.48+00	2009-02-12 15:01:37.545+00	24160	\N	\N	\N	\N
2009-02-12 15:01:37.821+00	\N	24162	\N	\N	\N	\N
2009-02-12 15:01:37.885+00	\N	24164	\N	\N	\N	\N
2009-02-12 15:01:38.103+00	2009-02-12 15:01:38.166+00	24165	\N	\N	\N	\N
2009-02-12 15:01:38.574+00	\N	24167	\N	\N	\N	\N
2009-02-12 15:01:38.638+00	\N	24169	\N	\N	\N	\N
2009-02-12 15:01:38.874+00	2009-02-12 15:01:38.938+00	24170	\N	\N	\N	\N
2009-02-12 15:01:39.201+00	\N	24172	\N	\N	\N	\N
2009-02-12 15:01:39.265+00	\N	24174	\N	\N	\N	\N
2009-02-12 15:01:39.534+00	2009-02-12 15:01:39.596+00	24175	\N	\N	\N	\N
2009-02-12 15:01:40.01+00	\N	24177	\N	\N	\N	\N
2009-02-12 15:01:40.089+00	\N	24179	\N	\N	\N	\N
2009-02-12 15:01:40.345+00	2009-02-12 15:01:40.402+00	24180	\N	\N	\N	\N
2009-02-12 15:01:40.503+00	\N	24182	\N	\N	\N	\N
2009-02-12 15:01:40.559+00	\N	24184	\N	\N	\N	\N
2009-02-12 15:01:40.864+00	2009-02-12 15:01:40.926+00	24185	\N	\N	\N	\N
2009-02-12 15:01:41.036+00	\N	24187	\N	\N	\N	\N
2009-02-12 15:01:41.096+00	\N	24189	\N	\N	\N	\N
2009-02-12 15:01:41.416+00	2009-02-12 15:01:41.477+00	24190	\N	\N	\N	\N
2009-02-12 15:01:41.877+00	\N	24192	\N	\N	\N	\N
2009-02-12 15:01:41.94+00	\N	24194	\N	\N	\N	\N
2009-02-12 15:01:42.202+00	2009-02-12 15:01:42.262+00	24195	\N	\N	\N	\N
2009-02-12 15:01:42.62+00	\N	24197	\N	\N	\N	\N
2009-02-12 15:01:42.683+00	\N	24199	\N	\N	\N	\N
2009-02-12 15:01:42.945+00	2009-02-12 15:01:43.007+00	24200	\N	\N	\N	\N
2009-02-12 15:01:43.311+00	\N	24202	\N	\N	\N	\N
2009-02-12 15:01:43.372+00	\N	24204	\N	\N	\N	\N
2009-02-12 15:01:43.597+00	2009-02-12 15:01:43.659+00	24205	\N	\N	\N	\N
2009-02-12 15:01:43.914+00	\N	24207	\N	\N	\N	\N
2009-02-12 15:01:43.977+00	\N	24209	\N	\N	\N	\N
2009-02-12 15:01:44.241+00	2009-02-12 15:01:44.303+00	24210	\N	\N	\N	\N
2009-02-12 15:01:44.665+00	\N	24212	\N	\N	\N	\N
2009-02-12 15:01:44.727+00	\N	24214	\N	\N	\N	\N
2009-02-12 15:01:45.04+00	2009-02-12 15:01:45.102+00	24215	\N	\N	\N	\N
2009-02-12 15:01:45.47+00	\N	24217	\N	\N	\N	\N
2009-02-12 15:01:45.532+00	\N	24219	\N	\N	\N	\N
2009-02-12 15:01:45.745+00	2009-02-12 15:01:45.809+00	24220	\N	\N	\N	\N
2009-02-12 15:01:46.065+00	\N	24222	\N	\N	\N	\N
2009-02-12 15:01:46.128+00	\N	24224	\N	\N	\N	\N
2009-02-12 15:01:46.345+00	2009-02-12 15:01:46.419+00	24225	\N	\N	\N	\N
2009-02-12 15:01:46.674+00	\N	24227	\N	\N	\N	\N
2009-02-12 15:01:46.738+00	\N	24229	\N	\N	\N	\N
2009-02-12 15:01:47.003+00	2009-02-12 15:01:47.066+00	24230	\N	\N	\N	\N
2009-02-12 15:01:47.178+00	\N	24232	\N	\N	\N	\N
2009-02-12 15:01:47.241+00	\N	24234	\N	\N	\N	\N
2009-02-12 15:01:47.465+00	2009-02-12 15:01:47.528+00	24235	\N	\N	\N	\N
2009-02-12 15:01:47.786+00	\N	24237	\N	\N	\N	\N
2009-02-12 15:01:47.849+00	\N	24239	\N	\N	\N	\N
2009-02-12 15:01:48.067+00	2009-02-12 15:01:48.131+00	24240	\N	\N	\N	\N
2009-02-12 15:01:48.494+00	\N	24242	\N	\N	\N	\N
2009-02-12 15:01:48.56+00	\N	24244	\N	\N	\N	\N
2009-02-12 15:01:48.777+00	2009-02-12 15:01:48.84+00	24245	\N	\N	\N	\N
2009-02-12 15:01:49.05+00	\N	24247	\N	\N	\N	\N
2009-02-12 15:01:49.125+00	\N	24249	\N	\N	\N	\N
2009-02-12 15:01:49.486+00	2009-02-12 15:01:49.548+00	24250	\N	\N	\N	\N
2009-02-12 15:01:49.908+00	\N	24252	\N	\N	\N	\N
2009-02-12 15:01:49.972+00	\N	24254	\N	\N	\N	\N
2009-02-12 15:01:50.198+00	2009-02-12 15:01:50.26+00	24255	\N	\N	\N	\N
2009-02-12 15:01:50.57+00	\N	24257	\N	\N	\N	\N
2009-02-12 15:01:50.633+00	\N	24259	\N	\N	\N	\N
2009-02-12 15:01:50.853+00	2009-02-12 15:01:50.917+00	24260	\N	\N	\N	\N
2009-02-12 15:01:51.336+00	\N	24262	\N	\N	\N	\N
2009-02-12 15:01:51.40+00	\N	24264	\N	\N	\N	\N
2009-02-12 15:01:51.669+00	2009-02-12 15:01:51.736+00	24265	\N	\N	\N	\N
2009-02-12 15:01:51.999+00	\N	24267	\N	\N	\N	\N
2009-02-12 15:01:52.062+00	\N	24269	\N	\N	\N	\N
2009-02-12 15:01:52.286+00	2009-02-12 15:01:52.35+00	24270	\N	\N	\N	\N
2009-02-12 15:01:52.909+00	\N	24272	\N	\N	\N	\N
2009-02-12 15:01:52.975+00	\N	24274	\N	\N	\N	\N
2009-02-12 15:01:53.213+00	2009-02-12 15:01:53.276+00	24275	\N	\N	\N	\N
2009-02-12 15:01:53.586+00	\N	24277	\N	\N	\N	\N
2009-02-12 15:01:53.651+00	\N	24279	\N	\N	\N	\N
2009-02-12 15:01:53.872+00	2009-02-12 15:01:53.936+00	24280	\N	\N	\N	\N
2009-02-12 15:01:54.31+00	\N	24282	\N	\N	\N	\N
2009-02-12 15:01:54.375+00	\N	24284	\N	\N	\N	\N
2009-02-12 15:01:54.596+00	2009-02-12 15:01:54.661+00	24285	\N	\N	\N	\N
2009-02-12 15:01:54.976+00	\N	24287	\N	\N	\N	\N
2009-02-12 15:01:55.039+00	\N	24289	\N	\N	\N	\N
2009-02-12 15:01:55.268+00	2009-02-12 15:01:55.333+00	24290	\N	\N	\N	\N
2009-02-12 15:01:55.647+00	\N	24292	\N	\N	\N	\N
2009-02-12 15:01:55.712+00	\N	24294	\N	\N	\N	\N
2009-02-12 15:01:55.985+00	2009-02-12 15:01:56.049+00	24295	\N	\N	\N	\N
2009-02-12 15:01:56.623+00	\N	24297	\N	\N	\N	\N
2009-02-12 15:01:56.688+00	\N	24299	\N	\N	\N	\N
2009-02-12 15:01:56.911+00	2009-02-12 15:01:56.976+00	24300	\N	\N	\N	\N
2009-02-12 15:01:57.291+00	\N	24302	\N	\N	\N	\N
2009-02-12 15:01:57.355+00	\N	24304	\N	\N	\N	\N
2009-02-12 15:01:57.944+00	2009-02-12 15:01:58.014+00	24305	\N	\N	\N	\N
2009-02-12 15:01:58.181+00	\N	24307	\N	\N	\N	\N
2009-02-12 15:01:58.239+00	\N	24309	\N	\N	\N	\N
2009-02-12 15:01:58.44+00	2009-02-12 15:01:58.504+00	24310	\N	\N	\N	\N
2009-02-12 15:01:59.015+00	\N	24312	\N	\N	\N	\N
2009-02-12 15:01:59.11+00	\N	24314	\N	\N	\N	\N
2009-02-12 15:01:59.378+00	2009-02-12 15:01:59.443+00	24315	\N	\N	\N	\N
2009-02-12 15:02:00.156+00	\N	24317	\N	\N	\N	\N
2009-02-12 15:02:00.22+00	\N	24319	\N	\N	\N	\N
2009-02-12 15:02:00.438+00	2009-02-12 15:02:00.501+00	24320	\N	\N	\N	\N
2009-02-12 15:02:01.037+00	\N	24322	\N	\N	\N	\N
2009-02-12 15:02:01.102+00	\N	24324	\N	\N	\N	\N
2009-02-12 15:02:01.325+00	2009-02-12 15:02:01.39+00	24325	\N	\N	\N	\N
2009-02-12 15:02:01.751+00	\N	24327	\N	\N	\N	\N
2009-02-12 15:02:01.815+00	\N	24329	\N	\N	\N	\N
2009-02-12 15:02:02.033+00	2009-02-12 15:02:02.097+00	24330	\N	\N	\N	\N
2009-02-12 15:02:02.533+00	\N	24332	\N	\N	\N	\N
2009-02-12 15:02:02.599+00	\N	24334	\N	\N	\N	\N
2009-02-12 15:02:02.82+00	2009-02-12 15:02:02.885+00	24335	\N	\N	\N	\N
2009-02-12 15:02:03.201+00	\N	24337	\N	\N	\N	\N
2009-02-12 15:02:03.264+00	\N	24339	\N	\N	\N	\N
2009-02-12 15:02:03.533+00	2009-02-12 15:02:03.596+00	24340	\N	\N	\N	\N
2009-02-12 15:02:03.709+00	\N	24342	\N	\N	\N	\N
2009-02-12 15:02:03.772+00	\N	24344	\N	\N	\N	\N
2009-02-12 15:02:04.045+00	2009-02-12 15:02:04.11+00	24345	\N	\N	\N	\N
2009-02-12 15:02:04.693+00	\N	24347	\N	\N	\N	\N
2009-02-12 15:02:04.76+00	\N	24349	\N	\N	\N	\N
2009-02-12 15:02:04.984+00	2009-02-12 15:02:05.049+00	24350	\N	\N	\N	\N
2009-02-12 15:02:05.263+00	\N	24352	\N	\N	\N	\N
2009-02-12 15:02:05.327+00	\N	24354	\N	\N	\N	\N
2009-02-12 15:02:05.547+00	2009-02-12 15:02:05.611+00	24355	\N	\N	\N	\N
2009-02-12 15:02:05.978+00	\N	24357	\N	\N	\N	\N
2009-02-12 15:02:06.044+00	\N	24359	\N	\N	\N	\N
2009-02-12 15:02:06.296+00	2009-02-12 15:02:06.36+00	24360	\N	\N	\N	\N
2009-02-12 15:02:06.677+00	\N	24362	\N	\N	\N	\N
2009-02-12 15:02:06.742+00	\N	24364	\N	\N	\N	\N
2009-02-12 15:02:06.968+00	2009-02-12 15:02:07.035+00	24365	\N	\N	\N	\N
2009-02-12 15:02:07.555+00	\N	24367	\N	\N	\N	\N
2009-02-12 15:02:07.62+00	\N	24369	\N	\N	\N	\N
2009-02-12 15:02:07.843+00	2009-02-12 15:02:07.909+00	24370	\N	\N	\N	\N
2009-02-12 15:02:08.228+00	\N	24372	\N	\N	\N	\N
2009-02-12 15:02:08.293+00	\N	24374	\N	\N	\N	\N
2009-02-12 15:02:08.641+00	2009-02-12 15:02:08.707+00	24375	\N	\N	\N	\N
2009-02-12 15:02:09.079+00	\N	24377	\N	\N	\N	\N
2009-02-12 15:02:09.145+00	\N	24379	\N	\N	\N	\N
2009-02-12 15:02:09.372+00	2009-02-12 15:02:09.436+00	24380	\N	\N	\N	\N
2009-02-12 15:02:09.753+00	\N	24382	\N	\N	\N	\N
2009-02-12 15:02:09.818+00	\N	24384	\N	\N	\N	\N
2009-02-12 15:02:10.043+00	2009-02-12 15:02:10.109+00	24385	\N	\N	\N	\N
2009-02-12 15:02:10.864+00	\N	24387	\N	\N	\N	\N
2009-02-12 15:02:10.931+00	\N	24389	\N	\N	\N	\N
2009-02-12 15:02:11.16+00	2009-02-12 15:02:11.227+00	24390	\N	\N	\N	\N
2009-02-12 15:02:11.55+00	\N	24392	\N	\N	\N	\N
2009-02-12 15:02:11.615+00	\N	24394	\N	\N	\N	\N
2009-02-12 15:02:11.84+00	2009-02-12 15:02:11.905+00	24395	\N	\N	\N	\N
2009-02-12 15:02:12.124+00	\N	24397	\N	\N	\N	\N
2009-02-12 15:02:12.19+00	\N	24399	\N	\N	\N	\N
2009-02-12 15:02:12.437+00	2009-02-12 15:02:12.502+00	24400	\N	\N	\N	\N
2009-02-12 15:02:12.927+00	\N	24402	\N	\N	\N	\N
2009-02-12 15:02:12.995+00	\N	24404	\N	\N	\N	\N
2009-02-12 15:02:13.224+00	2009-02-12 15:02:13.29+00	24405	\N	\N	\N	\N
2009-02-12 15:02:13.509+00	\N	24407	\N	\N	\N	\N
2009-02-12 15:02:13.574+00	\N	24409	\N	\N	\N	\N
2009-02-12 15:02:13.80+00	2009-02-12 15:02:13.865+00	24410	\N	\N	\N	\N
2009-02-12 15:02:14.342+00	\N	24412	\N	\N	\N	\N
2009-02-12 15:02:14.409+00	\N	24414	\N	\N	\N	\N
2009-02-12 15:02:14.644+00	2009-02-12 15:02:14.734+00	24415	\N	\N	\N	\N
2009-02-12 15:02:15.111+00	\N	24417	\N	\N	\N	\N
2009-02-12 15:02:15.178+00	\N	24419	\N	\N	\N	\N
2009-02-12 15:02:15.409+00	2009-02-12 15:02:15.476+00	24420	\N	\N	\N	\N
2009-02-12 15:02:15.647+00	\N	24422	\N	\N	\N	\N
2009-02-12 15:02:15.712+00	\N	24424	\N	\N	\N	\N
2009-02-12 15:02:15.94+00	2009-02-12 15:02:16.005+00	24425	\N	\N	\N	\N
2009-02-12 15:02:16.173+00	\N	24427	\N	\N	\N	\N
2009-02-12 15:02:16.239+00	\N	24429	\N	\N	\N	\N
2009-02-12 15:02:16.468+00	2009-02-12 15:02:16.535+00	24430	\N	\N	\N	\N
2009-02-12 15:02:16.653+00	\N	24432	\N	\N	\N	\N
2009-02-12 15:02:16.72+00	\N	24434	\N	\N	\N	\N
2009-02-12 15:02:17.076+00	2009-02-12 15:02:17.141+00	24435	\N	\N	\N	\N
2009-02-12 15:02:17.26+00	\N	24437	\N	\N	\N	\N
2009-02-12 15:02:17.33+00	\N	24439	\N	\N	\N	\N
2009-02-12 15:02:17.57+00	2009-02-12 15:02:17.639+00	24440	\N	\N	\N	\N
2009-02-12 15:02:17.994+00	\N	24442	\N	\N	\N	\N
2009-02-12 15:02:18.067+00	\N	24444	\N	\N	\N	\N
2009-02-12 15:02:18.317+00	2009-02-12 15:02:18.391+00	24445	\N	\N	\N	\N
2009-02-12 15:02:18.683+00	\N	24447	\N	\N	\N	\N
2009-02-12 15:02:18.755+00	\N	24449	\N	\N	\N	\N
2009-02-12 15:02:19+00	2009-02-12 15:02:19.086+00	24450	\N	\N	\N	\N
2009-02-12 15:02:19.214+00	\N	24452	\N	\N	\N	\N
2009-02-12 15:02:19.284+00	\N	24454	\N	\N	\N	\N
2009-02-12 15:02:19.526+00	2009-02-12 15:02:19.596+00	24455	\N	\N	\N	\N
2009-02-12 15:02:19.942+00	\N	24457	\N	\N	\N	\N
2009-02-12 15:02:20.014+00	\N	24459	\N	\N	\N	\N
2009-02-12 15:02:20.265+00	2009-02-12 15:02:20.338+00	24460	\N	\N	\N	\N
2009-02-12 15:02:20.69+00	\N	24462	\N	\N	\N	\N
2009-02-12 15:02:20.766+00	\N	24464	\N	\N	\N	\N
2009-02-12 15:02:21.068+00	2009-02-12 15:02:21.162+00	24465	\N	\N	\N	\N
2009-02-12 15:02:21.293+00	\N	24467	\N	\N	\N	\N
2009-02-12 15:02:21.364+00	\N	24469	\N	\N	\N	\N
2009-02-12 15:02:21.609+00	2009-02-12 15:02:21.68+00	24470	\N	\N	\N	\N
2009-02-12 15:02:22.084+00	\N	24472	\N	\N	\N	\N
2009-02-12 15:02:22.157+00	\N	24474	\N	\N	\N	\N
2009-02-12 15:02:22.411+00	2009-02-12 15:02:22.484+00	24475	\N	\N	\N	\N
2009-02-12 15:02:22.842+00	\N	24477	\N	\N	\N	\N
2009-02-12 15:02:22.915+00	\N	24479	\N	\N	\N	\N
2009-02-12 15:02:23.16+00	2009-02-12 15:02:23.256+00	24480	\N	\N	\N	\N
2009-02-12 15:02:23.606+00	\N	24482	\N	\N	\N	\N
2009-02-12 15:02:23.679+00	\N	24484	\N	\N	\N	\N
2009-02-12 15:02:23.926+00	2009-02-12 15:02:23.996+00	24485	\N	\N	\N	\N
2009-02-12 15:02:24.407+00	\N	24487	\N	\N	\N	\N
2009-02-12 15:02:24.481+00	\N	24489	\N	\N	\N	\N
2009-02-12 15:02:24.792+00	2009-02-12 15:02:24.863+00	24490	\N	\N	\N	\N
2009-02-12 15:02:25.24+00	\N	24492	\N	\N	\N	\N
2009-02-12 15:02:25.313+00	\N	24494	\N	\N	\N	\N
2009-02-12 15:02:25.561+00	2009-02-12 15:02:25.633+00	24495	\N	\N	\N	\N
2009-02-12 15:02:26.328+00	\N	24497	\N	\N	\N	\N
2009-02-12 15:02:26.403+00	\N	24499	\N	\N	\N	\N
2009-02-12 15:02:26.657+00	2009-02-12 15:02:26.738+00	24500	\N	\N	\N	\N
2009-02-12 15:02:27.336+00	\N	24502	\N	\N	\N	\N
2009-02-12 15:02:27.41+00	\N	24504	\N	\N	\N	\N
2009-02-12 15:02:27.715+00	2009-02-12 15:02:27.787+00	24505	\N	\N	\N	\N
2009-02-12 15:02:28.196+00	\N	24507	\N	\N	\N	\N
2009-02-12 15:02:28.272+00	\N	24509	\N	\N	\N	\N
2009-02-12 15:03:00.714+00	\N	30001	\N	\N	\N	\N
\N	\N	32001	\N	\N	\N	\N
2009-02-12 15:03:08.797+00	\N	32003	\N	\N	\N	\N
2009-02-12 15:03:08.821+00	\N	32005	\N	\N	\N	\N
2009-02-12 15:03:08.889+00	\N	32007	\N	\N	\N	\N
2009-02-12 15:03:08.959+00	\N	32009	\N	\N	\N	\N
2009-02-12 15:03:09.029+00	\N	32011	\N	\N	\N	\N
2009-02-12 15:03:09.067+00	\N	32013	\N	\N	\N	\N
2009-02-12 15:03:09.14+00	\N	32015	\N	\N	\N	\N
2009-02-12 15:03:09.199+00	\N	32017	\N	\N	\N	\N
2009-02-12 15:03:09.234+00	\N	32019	\N	\N	\N	\N
\N	\N	32021	\N	\N	\N	\N
2009-02-12 15:03:09.332+00	\N	32023	\N	\N	\N	\N
2009-02-12 15:03:09.353+00	\N	32025	\N	\N	\N	\N
2009-02-12 15:03:09.385+00	\N	32027	\N	\N	\N	\N
2009-02-12 15:03:09.408+00	\N	32029	\N	\N	\N	\N
2009-02-12 15:03:09.434+00	\N	32031	\N	\N	\N	\N
2009-02-12 15:03:09.47+00	\N	32033	\N	\N	\N	\N
2009-02-12 15:03:09.494+00	\N	32035	\N	\N	\N	\N
2009-02-12 15:03:09.535+00	\N	32037	\N	\N	\N	\N
2009-02-12 15:03:09.574+00	\N	32039	\N	\N	\N	\N
2009-02-12 15:03:09.603+00	\N	32041	\N	\N	\N	\N
2009-02-12 15:03:09.63+00	\N	32043	\N	\N	\N	\N
2009-02-12 15:03:09.657+00	\N	32045	\N	\N	\N	\N
2009-02-12 15:03:09.675+00	\N	32047	\N	\N	\N	\N
2009-02-12 15:03:09.702+00	\N	32049	\N	\N	\N	\N
\N	\N	32051	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2009-02-12 15:03:09.736+00	\N	32053	\N	\N	\N	\N
2009-02-12 15:03:09.764+00	\N	32055	\N	\N	\N	\N
2009-02-12 15:03:09.786+00	\N	32057	\N	\N	\N	\N
\N	\N	32059	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2009-02-12 15:03:09.905+00	\N	32061	\N	\N	\N	\N
2009-02-12 15:03:09.915+00	\N	32063	\N	\N	\N	\N
2009-02-12 15:03:09.932+00	\N	32065	\N	\N	\N	\N
\N	\N	32067	\N	Capture PCR Product into pOPIN vectors	\N	\N
2009-02-12 15:03:09.974+00	\N	32069	\N	\N	\N	\N
2009-02-12 15:03:09.987+00	\N	32071	\N	\N	\N	\N
2009-02-12 15:03:10.002+00	\N	32073	\N	\N	\N	\N
2009-02-12 15:03:10.013+00	\N	32075	\N	\N	\N	\N
2009-02-12 15:03:10.034+00	\N	32077	\N	\N	\N	\N
2009-02-12 15:03:10.056+00	\N	32079	\N	\N	\N	\N
\N	\N	32081	\N	Transform E.coli with plasmid, generic heat shock protocol	\N	\N
2009-02-12 15:03:10.092+00	\N	32083	\N	\N	\N	\N
2009-02-12 15:03:10.103+00	\N	32085	\N	\N	\N	\N
2009-02-12 15:03:10.116+00	\N	32087	\N	\N	\N	\N
2009-02-12 15:03:10.128+00	\N	32089	\N	\N	\N	\N
2009-02-12 15:03:10.14+00	\N	32091	\N	\N	\N	\N
2009-02-12 15:03:10.161+00	\N	32093	\N	\N	\N	\N
2009-02-12 15:03:10.184+00	\N	32095	\N	\N	\N	\N
2009-02-12 15:03:10.208+00	\N	32097	\N	\N	\N	\N
2009-02-12 15:03:10.23+00	\N	32099	\N	\N	\N	\N
2009-02-12 15:03:10.287+00	\N	32101	\N	\N	\N	\N
2009-02-12 15:03:10.31+00	\N	32103	\N	\N	\N	\N
2009-02-12 15:03:10.334+00	\N	32105	\N	\N	\N	\N
\N	\N	32107	\N	Concentration of Target Protein	\N	\N
2009-02-12 15:03:10.371+00	\N	32109	\N	\N	\N	\N
2009-02-12 15:03:10.383+00	\N	32111	\N	\N	\N	\N
2009-02-12 15:03:10.395+00	\N	32113	\N	\N	\N	\N
2009-02-12 15:03:10.413+00	\N	32115	\N	\N	\N	\N
2009-02-12 15:03:10.433+00	\N	32117	\N	\N	\N	\N
2009-02-12 15:03:10.453+00	\N	32119	\N	\N	\N	\N
2009-02-12 15:03:10.474+00	\N	32121	\N	\N	\N	\N
2009-02-12 15:03:10.495+00	\N	32123	\N	\N	\N	\N
2009-02-12 15:03:10.517+00	\N	32125	\N	\N	\N	\N
\N	\N	32127	\N	\N	\N	\N
2009-02-12 15:03:10.549+00	\N	32129	\N	\N	\N	\N
2009-02-12 15:03:10.562+00	\N	32131	\N	\N	\N	\N
2009-02-12 15:03:10.575+00	\N	32133	\N	\N	\N	\N
2009-02-12 15:03:10.588+00	\N	32135	\N	\N	\N	\N
2009-02-12 15:03:10.609+00	\N	32137	\N	\N	\N	\N
2009-02-12 15:03:10.63+00	\N	32139	\N	\N	\N	\N
2009-02-12 15:03:10.651+00	\N	32141	\N	\N	\N	\N
\N	\N	32143	\N	set up one reaction per insert	\N	\N
2009-02-12 15:03:10.68+00	\N	32145	\N	\N	\N	\N
2009-02-12 15:03:10.693+00	\N	32147	\N	\N	\N	\N
2009-02-12 15:03:10.705+00	\N	32149	\N	\N	\N	\N
2009-02-12 15:03:10.722+00	\N	32151	\N	\N	\N	\N
2009-02-12 15:03:10.742+00	\N	32153	\N	\N	\N	\N
2009-02-12 15:03:10.763+00	\N	32155	\N	\N	\N	\N
2009-02-12 15:03:10.784+00	\N	32157	\N	\N	\N	\N
\N	\N	32159	\N	Parameter number of isolated colonies defined for output from protocol	\N	\N
2009-02-12 15:03:10.813+00	\N	32161	\N	\N	\N	\N
2009-02-12 15:03:10.826+00	\N	32163	\N	\N	\N	\N
2009-02-12 15:03:10.846+00	\N	32165	\N	\N	\N	\N
2009-02-12 15:03:10.865+00	\N	32167	\N	\N	\N	\N
2009-02-12 15:03:10.884+00	\N	32169	\N	\N	\N	\N
2009-02-12 15:03:10.904+00	\N	32171	\N	\N	\N	\N
\N	\N	32173	\N	\N	\N	\N
2009-02-12 15:03:10.933+00	\N	32175	\N	\N	\N	\N
2009-02-12 15:03:10.975+00	\N	32177	\N	\N	\N	\N
2009-02-12 15:03:10.988+00	\N	32179	\N	\N	\N	\N
2009-02-12 15:03:11+00	\N	32181	\N	\N	\N	\N
2009-02-12 15:03:11.058+00	\N	32183	\N	\N	\N	\N
2009-02-12 15:03:11.071+00	\N	32185	\N	\N	\N	\N
2009-02-12 15:03:11.085+00	\N	32187	\N	\N	\N	\N
2009-02-12 15:03:11.118+00	\N	32189	\N	\N	\N	\N
2009-02-12 15:03:11.14+00	\N	32191	\N	\N	\N	\N
\N	\N	32193	\N	\N	\N	\N
2009-02-12 15:03:11.176+00	\N	32195	\N	\N	\N	\N
2009-02-12 15:03:11.187+00	\N	32197	\N	\N	\N	\N
2009-02-12 15:03:11.196+00	\N	32199	\N	\N	\N	\N
2009-02-12 15:03:11.224+00	\N	32201	\N	\N	\N	\N
2009-02-12 15:03:11.257+00	\N	32203	\N	\N	\N	\N
2009-02-12 15:03:11.273+00	\N	32205	\N	\N	\N	\N
2009-02-12 15:03:11.289+00	\N	32207	\N	\N	\N	\N
2009-02-12 15:03:11.316+00	\N	32209	\N	\N	\N	\N
\N	\N	32211	\N	\N	\N	\N
2009-02-12 15:03:11.355+00	\N	32213	\N	\N	\N	\N
2009-02-12 15:03:11.368+00	\N	32215	\N	\N	\N	\N
2009-02-12 15:03:11.382+00	\N	32217	\N	\N	\N	\N
2009-02-12 15:03:11.394+00	\N	32219	\N	\N	\N	\N
2009-02-12 15:03:11.411+00	\N	32221	\N	\N	\N	\N
2009-02-12 15:03:11.423+00	\N	32223	\N	\N	\N	\N
2009-02-12 15:03:11.443+00	\N	32225	\N	\N	\N	\N
2009-02-12 15:03:11.457+00	\N	32227	\N	\N	\N	\N
2009-02-12 15:03:11.471+00	\N	32229	\N	\N	\N	\N
2009-02-12 15:03:11.494+00	\N	32231	\N	\N	\N	\N
2009-02-12 15:03:11.516+00	\N	32233	\N	\N	\N	\N
2009-02-12 15:03:11.539+00	\N	32235	\N	\N	\N	\N
2009-02-12 15:03:11.59+00	\N	32237	\N	\N	\N	\N
2009-02-12 15:03:11.613+00	\N	32239	\N	\N	\N	\N
2009-02-12 15:03:11.634+00	\N	32241	\N	\N	\N	\N
2009-02-12 15:03:11.656+00	\N	32243	\N	\N	\N	\N
\N	\N	32245	\N	Use buffers and reagents from Qiagen kit.	\N	\N
2009-02-12 15:03:11.691+00	\N	32247	\N	\N	\N	\N
2009-02-12 15:03:11.707+00	\N	32249	\N	\N	\N	\N
2009-02-12 15:03:11.721+00	\N	32251	\N	\N	\N	\N
2009-02-12 15:03:11.734+00	\N	32253	\N	\N	\N	\N
2009-02-12 15:03:11.75+00	\N	32255	\N	\N	\N	\N
2009-02-12 15:03:11.766+00	\N	32257	\N	\N	\N	\N
2009-02-12 15:03:11.777+00	\N	32259	\N	\N	\N	\N
2009-02-12 15:03:11.805+00	\N	32261	\N	\N	\N	\N
2009-02-12 15:03:11.827+00	\N	32263	\N	\N	\N	\N
2009-02-12 15:03:11.848+00	\N	32265	\N	\N	\N	\N
2009-02-12 15:03:11.896+00	\N	32267	\N	\N	\N	\N
\N	\N	32269	\N	\N	\N	\N
2009-02-12 15:03:11.943+00	\N	32271	\N	\N	\N	\N
2009-02-12 15:03:11.973+00	\N	32273	\N	\N	\N	\N
2009-02-12 15:03:11.989+00	\N	32275	\N	\N	\N	\N
2009-02-12 15:03:12.002+00	\N	32277	\N	\N	\N	\N
2009-02-12 15:03:12.068+00	\N	32279	\N	\N	\N	\N
2009-02-12 15:03:12.081+00	\N	32281	\N	\N	\N	\N
2009-02-12 15:03:12.093+00	\N	32283	\N	\N	\N	\N
2009-02-12 15:03:12.108+00	\N	32285	\N	\N	\N	\N
2009-02-12 15:03:12.127+00	\N	32287	\N	\N	\N	\N
2009-02-12 15:03:12.144+00	\N	32289	\N	\N	\N	\N
2009-02-12 15:03:12.161+00	\N	32291	\N	\N	\N	\N
2009-02-12 15:03:12.179+00	\N	32293	\N	\N	\N	\N
2009-02-12 15:03:12.197+00	\N	32295	\N	\N	\N	\N
\N	\N	32297	\N	Use buffers and reagents from Qiagen kit.	\N	\N
2009-02-12 15:03:12.227+00	\N	32299	\N	\N	\N	\N
2009-02-12 15:03:12.24+00	\N	32301	\N	\N	\N	\N
2009-02-12 15:03:12.253+00	\N	32303	\N	\N	\N	\N
2009-02-12 15:03:12.265+00	\N	32305	\N	\N	\N	\N
2009-02-12 15:03:12.278+00	\N	32307	\N	\N	\N	\N
2009-02-12 15:03:12.292+00	\N	32309	\N	\N	\N	\N
2009-02-12 15:03:12.302+00	\N	32311	\N	\N	\N	\N
2009-02-12 15:03:12.321+00	\N	32313	\N	\N	\N	\N
2009-02-12 15:03:12.341+00	\N	32315	\N	\N	\N	\N
2009-02-12 15:03:12.36+00	\N	32317	\N	\N	\N	\N
2009-02-12 15:03:12.38+00	\N	32319	\N	\N	\N	\N
\N	\N	32321	\N	Parameter number of isolated colonies defined for output from protocol	\N	\N
2009-02-12 15:03:12.416+00	\N	32323	\N	\N	\N	\N
2009-02-12 15:03:12.43+00	\N	32325	\N	\N	\N	\N
2009-02-12 15:03:12.45+00	\N	32327	\N	\N	\N	\N
2009-02-12 15:03:12.476+00	\N	32329	\N	\N	\N	\N
2009-02-12 15:03:12.504+00	\N	32331	\N	\N	\N	\N
2009-02-12 15:03:12.523+00	\N	32333	\N	\N	\N	\N
\N	\N	32335	\N	\N	\N	\N
2009-02-12 15:03:12.553+00	\N	32337	\N	\N	\N	\N
2009-02-12 15:03:12.566+00	\N	32339	\N	\N	\N	\N
2009-02-12 15:03:12.578+00	\N	32341	\N	\N	\N	\N
2009-02-12 15:03:12.591+00	\N	32343	\N	\N	\N	\N
2009-02-12 15:03:12.635+00	\N	32345	\N	\N	\N	\N
2009-02-12 15:03:12.65+00	\N	32347	\N	\N	\N	\N
2009-02-12 15:03:12.665+00	\N	32349	\N	\N	\N	\N
2009-02-12 15:03:12.689+00	\N	32351	\N	\N	\N	\N
\N	\N	32353	\N	\N	\N	\N
2009-02-12 15:03:12.741+00	\N	32355	\N	\N	\N	\N
2009-02-12 15:03:12.76+00	\N	32357	\N	\N	\N	\N
2009-02-12 15:03:12.774+00	\N	32359	\N	\N	\N	\N
2009-02-12 15:03:12.805+00	\N	32361	\N	\N	\N	\N
2009-02-12 15:03:12.824+00	\N	32363	\N	\N	\N	\N
2009-02-12 15:03:12.843+00	\N	32365	\N	\N	\N	\N
2009-02-12 15:03:12.862+00	\N	32367	\N	\N	\N	\N
2009-02-12 15:03:12.882+00	\N	32369	\N	\N	\N	\N
2009-02-12 15:03:12.901+00	\N	32371	\N	\N	\N	\N
2009-02-12 15:03:12.92+00	\N	32373	\N	\N	\N	\N
2009-02-12 15:03:12.952+00	\N	32375	\N	\N	\N	\N
2009-02-12 15:03:12.972+00	\N	32377	\N	\N	\N	\N
2009-02-12 15:03:12.992+00	\N	32379	\N	\N	\N	\N
2009-02-12 15:03:13.013+00	\N	32381	\N	\N	\N	\N
2009-02-12 15:03:13.034+00	\N	32383	\N	\N	\N	\N
2009-02-12 15:03:13.057+00	\N	32385	\N	\N	\N	\N
2009-02-12 15:03:13.088+00	\N	32387	\N	\N	\N	\N
2009-02-12 15:03:13.109+00	\N	32389	\N	\N	\N	\N
2009-02-12 15:03:13.131+00	\N	32391	\N	\N	\N	\N
2009-02-12 15:03:13.162+00	\N	32393	\N	\N	\N	\N
\N	\N	32395	\N	\N	\N	\N
\N	\N	34001	\N	\N	\N	\N
2009-02-12 15:03:22.977+00	\N	34003	\N	\N	\N	\N
2009-02-12 15:03:23.026+00	\N	34005	\N	\N	\N	\N
2009-02-12 15:03:23.051+00	\N	34007	\N	\N	\N	\N
2009-02-12 15:03:23.123+00	\N	34009	\N	\N	\N	\N
2009-02-12 15:03:23.174+00	\N	34011	\N	\N	\N	\N
\N	\N	34013	\N	\N	\N	\N
2009-02-12 15:03:23.232+00	\N	34015	\N	\N	\N	\N
2009-02-12 15:03:23.257+00	\N	34017	\N	\N	\N	\N
2009-02-12 15:03:23.296+00	\N	34019	\N	\N	\N	\N
\N	\N	34021	\N	\N	\N	\N
2009-02-12 15:03:23.364+00	\N	34023	\N	\N	\N	\N
2009-02-12 15:03:23.396+00	\N	34025	\N	\N	\N	\N
2009-02-12 15:03:23.419+00	\N	34027	\N	\N	\N	\N
2009-02-12 15:03:23.444+00	\N	34029	\N	\N	\N	\N
2009-02-12 15:03:23.469+00	\N	34031	\N	\N	\N	\N
2009-02-12 15:03:23.496+00	\N	34033	\N	\N	\N	\N
2009-02-12 15:03:23.525+00	\N	34035	\N	\N	\N	\N
2009-02-12 15:03:23.555+00	\N	34037	\N	\N	\N	\N
2009-02-12 15:03:23.585+00	\N	34039	\N	\N	\N	\N
2009-02-12 15:03:23.617+00	\N	34041	\N	\N	\N	\N
2009-02-12 15:03:23.651+00	\N	34043	\N	\N	\N	\N
2009-02-12 15:03:23.695+00	\N	34045	\N	\N	\N	\N
2009-02-12 15:03:23.742+00	\N	34047	\N	\N	\N	\N
2009-02-12 15:03:23.785+00	\N	34049	\N	\N	\N	\N
2009-02-12 15:03:23.829+00	\N	34051	\N	\N	\N	\N
2009-02-12 15:03:23.919+00	\N	34053	\N	\N	\N	\N
2009-02-12 15:03:24.007+00	\N	34055	\N	\N	\N	\N
2009-02-12 15:03:24.10+00	\N	34057	\N	\N	\N	\N
\N	\N	34059	\N	\N	\N	\N
2009-02-12 15:03:24.183+00	\N	34061	\N	\N	\N	\N
2009-02-12 15:03:24.232+00	\N	34063	\N	\N	\N	\N
2009-02-12 15:03:24.275+00	\N	34065	\N	\N	\N	\N
2009-02-12 15:03:24.322+00	\N	34067	\N	\N	\N	\N
2009-02-12 15:03:24.371+00	\N	34069	\N	\N	\N	\N
2009-02-12 15:03:24.423+00	\N	34071	\N	\N	\N	\N
2009-02-12 15:03:24.477+00	\N	34073	\N	\N	\N	\N
2009-02-12 15:03:24.534+00	\N	34075	\N	\N	\N	\N
2009-02-12 15:03:24.594+00	\N	34077	\N	\N	\N	\N
2009-02-12 15:03:24.658+00	\N	34079	\N	\N	\N	\N
2009-02-12 15:03:24.723+00	\N	34081	\N	\N	\N	\N
2009-02-12 15:03:24.789+00	\N	34083	\N	\N	\N	\N
2009-02-12 15:03:24.856+00	\N	34085	\N	\N	\N	\N
2009-02-12 15:03:24.955+00	\N	34087	\N	\N	\N	\N
2009-02-12 15:03:24.998+00	\N	34089	\N	\N	\N	\N
2009-02-12 15:03:25.069+00	\N	34091	\N	\N	\N	\N
2009-02-12 15:03:25.143+00	\N	34093	\N	\N	\N	\N
2009-02-12 15:03:25.22+00	\N	34095	\N	\N	\N	\N
2009-02-12 15:03:25.279+00	\N	34097	\N	\N	\N	\N
\N	\N	34099	\N	\N	\N	\N
2009-02-12 15:03:25.506+00	\N	34101	\N	\N	\N	\N
2009-02-12 15:03:25.658+00	\N	34103	\N	\N	\N	\N
2009-02-12 15:03:25.742+00	\N	34105	\N	\N	\N	\N
2009-02-12 15:03:25.763+00	\N	34107	\N	\N	\N	\N
2009-02-12 15:03:25.781+00	\N	34109	\N	\N	\N	\N
2009-02-12 15:03:25.806+00	\N	34111	\N	\N	\N	\N
\N	\N	36001	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:35.065+00	\N	36003	\N	\N	\N	\N
2009-02-12 15:03:35.093+00	\N	36005	\N	\N	\N	\N
2009-02-12 15:03:35.111+00	\N	36007	\N	\N	\N	\N
2009-02-12 15:03:35.141+00	\N	36009	\N	\N	\N	\N
2009-02-12 15:03:35.208+00	\N	36011	\N	\N	\N	\N
2009-02-12 15:03:35.256+00	\N	36013	\N	\N	\N	\N
2009-02-12 15:03:35.30+00	\N	36015	\N	\N	\N	\N
2009-02-12 15:03:35.347+00	\N	36017	\N	\N	\N	\N
\N	\N	36019	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:35.403+00	\N	36021	\N	\N	\N	\N
2009-02-12 15:03:35.441+00	\N	36023	\N	\N	\N	\N
2009-02-12 15:03:35.465+00	\N	36025	\N	\N	\N	\N
2009-02-12 15:03:35.59+00	\N	36027	\N	\N	\N	\N
2009-02-12 15:03:35.634+00	\N	36029	\N	\N	\N	\N
\N	\N	36031	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:35.69+00	\N	36033	\N	\N	\N	\N
2009-02-12 15:03:35.721+00	\N	36035	\N	\N	\N	\N
2009-02-12 15:03:35.768+00	\N	36037	\N	\N	\N	\N
2009-02-12 15:03:35.815+00	\N	36039	\N	\N	\N	\N
2009-02-12 15:03:35.863+00	\N	36041	\N	\N	\N	\N
\N	\N	36043	\N	\N	\N	\N
2009-02-12 15:03:35.923+00	\N	36045	\N	\N	\N	\N
2009-02-12 15:03:35.946+00	\N	36047	\N	\N	\N	\N
2009-02-12 15:03:35.972+00	\N	36049	\N	\N	\N	\N
2009-02-12 15:03:36.035+00	\N	36051	\N	\N	\N	\N
2009-02-12 15:03:36.093+00	\N	36053	\N	\N	\N	\N
2009-02-12 15:03:36.152+00	\N	36055	\N	\N	\N	\N
2009-02-12 15:03:36.215+00	\N	36057	\N	\N	\N	\N
\N	\N	36059	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:36.282+00	\N	36061	\N	\N	\N	\N
2009-02-12 15:03:36.31+00	\N	36063	\N	\N	\N	\N
\N	\N	36065	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:36.367+00	\N	36067	\N	\N	\N	\N
2009-02-12 15:03:36.40+00	\N	36069	\N	\N	\N	\N
2009-02-12 15:03:36.435+00	\N	36071	\N	\N	\N	\N
2009-02-12 15:03:36.473+00	\N	36073	\N	\N	\N	\N
2009-02-12 15:03:36.514+00	\N	36075	\N	\N	\N	\N
2009-02-12 15:03:36.558+00	\N	36077	\N	\N	\N	\N
2009-02-12 15:03:36.604+00	\N	36079	\N	\N	\N	\N
2009-02-12 15:03:36.652+00	\N	36081	\N	\N	\N	\N
2009-02-12 15:03:36.703+00	\N	36083	\N	\N	\N	\N
2009-02-12 15:03:36.759+00	\N	36085	\N	\N	\N	\N
\N	\N	36087	\N	\N	\N	\N
2009-02-12 15:03:36.816+00	\N	36089	\N	\N	\N	\N
2009-02-12 15:03:36.845+00	\N	36091	\N	\N	\N	\N
2009-02-12 15:03:36.87+00	\N	36093	\N	\N	\N	\N
2009-02-12 15:03:36.908+00	\N	36095	\N	\N	\N	\N
2009-02-12 15:03:36.964+00	\N	36097	\N	\N	\N	\N
\N	\N	36099	\N	\N	\N	\N
2009-02-12 15:03:37.021+00	\N	36101	\N	\N	\N	\N
2009-02-12 15:03:37.04+00	\N	36103	\N	\N	\N	\N
2009-02-12 15:03:37.074+00	\N	36105	\N	\N	\N	\N
2009-02-12 15:03:37.121+00	\N	36107	\N	\N	\N	\N
2009-02-12 15:03:37.17+00	\N	36109	\N	\N	\N	\N
\N	\N	36111	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:37.232+00	\N	36113	\N	\N	\N	\N
2009-02-12 15:03:37.263+00	\N	36115	\N	\N	\N	\N
2009-02-12 15:03:37.316+00	\N	36117	\N	\N	\N	\N
2009-02-12 15:03:37.369+00	\N	36119	\N	\N	\N	\N
\N	\N	36121	\N	PCR performed using high-fidelity enzymes	\N	\N
2009-02-12 15:03:37.425+00	\N	36123	\N	\N	\N	\N
2009-02-12 15:03:37.447+00	\N	36125	\N	\N	\N	\N
2009-02-12 15:03:37.474+00	\N	36127	\N	\N	\N	\N
2009-02-12 15:03:37.514+00	\N	36129	\N	\N	\N	\N
2009-02-12 15:03:37.548+00	\N	36131	\N	\N	\N	\N
2009-02-12 15:03:37.618+00	\N	36133	\N	\N	\N	\N
2009-02-12 15:03:37.685+00	\N	36135	\N	\N	\N	\N
\N	\N	36137	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:37.757+00	\N	36139	\N	\N	\N	\N
2009-02-12 15:03:37.789+00	\N	36141	\N	\N	\N	\N
2009-02-12 15:03:37.847+00	\N	36143	\N	\N	\N	\N
2009-02-12 15:03:37.901+00	\N	36145	\N	\N	\N	\N
2009-02-12 15:03:37.959+00	\N	36147	\N	\N	\N	\N
\N	\N	36149	\N	PCR performed using high-fidelity enzymes	\N	\N
2009-02-12 15:03:38.027+00	\N	36151	\N	\N	\N	\N
2009-02-12 15:03:38.062+00	\N	36153	\N	\N	\N	\N
2009-02-12 15:03:38.106+00	\N	36155	\N	\N	\N	\N
2009-02-12 15:03:38.152+00	\N	36157	\N	\N	\N	\N
2009-02-12 15:03:38.201+00	\N	36159	\N	\N	\N	\N
2009-02-12 15:03:38.256+00	\N	36161	\N	\N	\N	\N
2009-02-12 15:03:38.311+00	\N	36163	\N	\N	\N	\N
2009-02-12 15:03:38.37+00	\N	36165	\N	\N	\N	\N
2009-02-12 15:03:38.484+00	\N	36167	\N	\N	\N	\N
2009-02-12 15:03:38.594+00	\N	36169	\N	\N	\N	\N
2009-02-12 15:03:38.705+00	\N	36171	\N	\N	\N	\N
2009-02-12 15:03:38.822+00	\N	36173	\N	\N	\N	\N
2009-02-12 15:03:38.939+00	\N	36175	\N	\N	\N	\N
2009-02-12 15:03:39.059+00	\N	36177	\N	\N	\N	\N
2009-02-12 15:03:39.182+00	\N	36179	\N	\N	\N	\N
2009-02-12 15:03:39.31+00	\N	36181	\N	\N	\N	\N
2009-02-12 15:03:39.437+00	\N	36183	\N	\N	\N	\N
2009-02-12 15:03:39.568+00	\N	36185	\N	\N	\N	\N
2009-02-12 15:03:39.702+00	\N	36187	\N	\N	\N	\N
2009-02-12 15:03:39.84+00	\N	36189	\N	\N	\N	\N
\N	\N	36191	\N	\N	\N	\N
2009-02-12 15:03:40.022+00	\N	36193	\N	\N	\N	\N
2009-02-12 15:03:40.088+00	\N	36195	\N	\N	\N	\N
2009-02-12 15:03:40.134+00	\N	36197	\N	\N	\N	\N
2009-02-12 15:03:40.165+00	\N	36199	\N	\N	\N	\N
2009-02-12 15:03:40.211+00	\N	36201	\N	\N	\N	\N
2009-02-12 15:03:40.276+00	\N	36203	\N	\N	\N	\N
\N	\N	36205	\N	\N	\N	\N
2009-02-12 15:03:40.341+00	\N	36207	\N	\N	\N	\N
2009-02-12 15:03:40.362+00	\N	36209	\N	\N	\N	\N
2009-02-12 15:03:40.398+00	\N	36211	\N	\N	\N	\N
2009-02-12 15:03:40.444+00	\N	36213	\N	\N	\N	\N
2009-02-12 15:03:40.491+00	\N	36215	\N	\N	\N	\N
\N	\N	36217	\N	PCR performed using high-fidelity enzymes	\N	\N
2009-02-12 15:03:40.548+00	\N	36219	\N	\N	\N	\N
2009-02-12 15:03:40.58+00	\N	36221	\N	\N	\N	\N
2009-02-12 15:03:40.632+00	\N	36223	\N	\N	\N	\N
2009-02-12 15:03:40.669+00	\N	36225	\N	\N	\N	\N
2009-02-12 15:03:40.705+00	\N	36227	\N	\N	\N	\N
2009-02-12 15:03:40.756+00	\N	36229	\N	\N	\N	\N
2009-02-12 15:03:40.80+00	\N	36231	\N	\N	\N	\N
2009-02-12 15:03:40.893+00	\N	36233	\N	\N	\N	\N
2009-02-12 15:03:40.986+00	\N	36235	\N	\N	\N	\N
\N	\N	36237	\N	PCR performed using high-fidelity enzymes	\N	\N
2009-02-12 15:03:41.067+00	\N	36239	\N	\N	\N	\N
2009-02-12 15:03:41.083+00	\N	36241	\N	\N	\N	\N
2009-02-12 15:03:41.106+00	\N	36243	\N	\N	\N	\N
2009-02-12 15:03:41.132+00	\N	36245	\N	\N	\N	\N
2009-02-12 15:03:41.155+00	\N	36247	\N	\N	\N	\N
2009-02-12 15:03:41.187+00	\N	36249	\N	\N	\N	\N
2009-02-12 15:03:41.218+00	\N	36251	\N	\N	\N	\N
2009-02-12 15:03:41.276+00	\N	36253	\N	\N	\N	\N
2009-02-12 15:03:41.329+00	\N	36255	\N	\N	\N	\N
\N	\N	36257	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:41.381+00	\N	36259	\N	\N	\N	\N
2009-02-12 15:03:41.405+00	\N	36261	\N	\N	\N	\N
2009-02-12 15:03:41.422+00	\N	36263	\N	\N	\N	\N
2009-02-12 15:03:41.439+00	\N	36265	\N	\N	\N	\N
2009-02-12 15:03:41.479+00	\N	36267	\N	\N	\N	\N
2009-02-12 15:03:41.513+00	\N	36269	\N	\N	\N	\N
2009-02-12 15:03:41.548+00	\N	36271	\N	\N	\N	\N
2009-02-12 15:03:41.583+00	\N	36273	\N	\N	\N	\N
2009-02-12 15:03:41.619+00	\N	36275	\N	\N	\N	\N
\N	\N	36277	\N	\N	\N	\N
2009-02-12 15:03:41.659+00	\N	36279	\N	\N	\N	\N
2009-02-12 15:03:41.685+00	\N	36281	\N	\N	\N	\N
\N	\N	36283	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:41.83+00	\N	36285	\N	\N	\N	\N
2009-02-12 15:03:41.843+00	\N	36287	\N	\N	\N	\N
2009-02-12 15:03:41.867+00	\N	36289	\N	\N	\N	\N
2009-02-12 15:03:41.889+00	\N	36291	\N	\N	\N	\N
2009-02-12 15:03:41.912+00	\N	36293	\N	\N	\N	\N
2009-02-12 15:03:41.936+00	\N	36295	\N	\N	\N	\N
\N	\N	36297	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:41.976+00	\N	36299	\N	\N	\N	\N
2009-02-12 15:03:41.994+00	\N	36301	\N	\N	\N	\N
2009-02-12 15:03:42.014+00	\N	36303	\N	\N	\N	\N
2009-02-12 15:03:42.04+00	\N	36305	\N	\N	\N	\N
2009-02-12 15:03:42.066+00	\N	36307	\N	\N	\N	\N
2009-02-12 15:03:42.093+00	\N	36309	\N	\N	\N	\N
2009-02-12 15:03:42.12+00	\N	36311	\N	\N	\N	\N
2009-02-12 15:03:42.147+00	\N	36313	\N	\N	\N	\N
\N	\N	36315	\N	Purification of Target Protein	\N	\N
2009-02-12 15:03:42.18+00	\N	36317	\N	\N	\N	\N
2009-02-12 15:03:42.194+00	\N	36319	\N	\N	\N	\N
2009-02-12 15:03:42.224+00	\N	36321	\N	\N	\N	\N
2009-02-12 15:03:42.247+00	\N	36323	\N	\N	\N	\N
2009-02-12 15:03:42.27+00	\N	36325	\N	\N	\N	\N
2009-02-12 15:03:42.294+00	\N	36327	\N	\N	\N	\N
\.


--
-- TOC entry 2819 (class 0 OID 63013)
-- Dependencies: 1727
-- Data for Name: core_note; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_note (name, attachmentid) FROM stdin;
\.


--
-- TOC entry 2820 (class 0 OID 63016)
-- Dependencies: 1728
-- Data for Name: core_note2relatedentrys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_note2relatedentrys (noteid, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2812 (class 0 OID 62968)
-- Dependencies: 1720
-- Data for Name: core_systemclass; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_systemclass (dbid, details) FROM stdin;
8002	\N
8004	\N
8006	\N
8007	\N
8008	\N
\.


--
-- TOC entry 2821 (class 0 OID 63019)
-- Dependencies: 1729
-- Data for Name: core_thesiscitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_thesiscitation (city, country, institution, stateprovince, citationid) FROM stdin;
\.


--
-- TOC entry 2822 (class 0 OID 63022)
-- Dependencies: 1730
-- Data for Name: cryz_cypade_possva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_cypade_possva (parameterdefinitionid, possiblevalue, order_) FROM stdin;
\.


--
-- TOC entry 2823 (class 0 OID 63025)
-- Dependencies: 1731
-- Data for Name: cryz_dropannotation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_dropannotation (cmdlineparam, scoredate, labbookentryid, holderid, scoreid, imageid, softwareid, sampleid) FROM stdin;
\.


--
-- TOC entry 2824 (class 0 OID 63031)
-- Dependencies: 1732
-- Data for Name: cryz_image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_image (filename, filepath, mimetype, labbookentryid, instrumentid, imagetypeid, scheduledtaskid, sampleid) FROM stdin;
\.


--
-- TOC entry 2825 (class 0 OID 63037)
-- Dependencies: 1733
-- Data for Name: cryz_parameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_parameter (value, labbookentryid, parameterdefinitionid, imageid) FROM stdin;
\.


--
-- TOC entry 2826 (class 0 OID 63040)
-- Dependencies: 1734
-- Data for Name: cryz_parameterdefinition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_parameterdefinition (defaultvalue, displayunit, label, maxvalue, minvalue, name, paramtype, unit, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2827 (class 0 OID 63046)
-- Dependencies: 1735
-- Data for Name: cryz_score; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_score (color, name, value, labbookentryid, scoringschemeid) FROM stdin;
\.


--
-- TOC entry 2828 (class 0 OID 63049)
-- Dependencies: 1736
-- Data for Name: cryz_scoringscheme; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_scoringscheme (name, version, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2829 (class 0 OID 63052)
-- Dependencies: 1737
-- Data for Name: expe_experiment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experiment (enddate, islocked, name, startdate, status, labbookentryid, protocolid, experimenttypeid, researchobjectiveid, instrumentid, experimentgroupid, operatorid, methodid, groupid) FROM stdin;
\.


--
-- TOC entry 2830 (class 0 OID 63055)
-- Dependencies: 1738
-- Data for Name: expe_experimentgroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experimentgroup (enddate, name, purpose, startdate, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2831 (class 0 OID 63058)
-- Dependencies: 1739
-- Data for Name: expe_inputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_inputsample (amount, amountdisplayunit, amountunit, name, role, labbookentryid, experimentid, refinputsampleid, sampleid) FROM stdin;
\.


--
-- TOC entry 2832 (class 0 OID 63061)
-- Dependencies: 1740
-- Data for Name: expe_instrument; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_instrument (model, name, serialnumber, labbookentryid, tempdisplayunit, pressuredisplayunit, pressure, temperature, locationid, defaultimagetypeid, manufacturerid) FROM stdin;
\N	Oasis 1700	\N	38005	\N	\N	\N	20.5	\N	38001	\N
\N	Oasis 1750	\N	38006	\N	\N	\N	6.5	\N	38001	\N
\N	Oasis LS3	\N	38007	\N	\N	\N	6.5	\N	38001	\N
\N	RI1000-0014	\N	38008	\N	\N	\N	6.5	\N	38002	\N
\N	RI182-0005	\N	38009	\N	\N	\N	23	\N	38002	\N
\N	Pixera	\N	38010	\N	\N	\N	23	\N	38004	\N
\N	RI182-0005-108	\N	38011	\N	\N	\N	23	\N	38003	\N
\.


--
-- TOC entry 2833 (class 0 OID 63064)
-- Dependencies: 1741
-- Data for Name: expe_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_method (name, procedure_, task, labbookentryid, softwareid, instrumentid) FROM stdin;
\.


--
-- TOC entry 2834 (class 0 OID 63070)
-- Dependencies: 1742
-- Data for Name: expe_methodparameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_methodparameter (name, paramtype, unit, value, labbookentryid, methodid) FROM stdin;
\.


--
-- TOC entry 2835 (class 0 OID 63073)
-- Dependencies: 1743
-- Data for Name: expe_outputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_outputsample (amount, amountdisplayunit, amountunit, name, role, labbookentryid, experimentid, refoutputsampleid, sampleid) FROM stdin;
\.


--
-- TOC entry 2836 (class 0 OID 63076)
-- Dependencies: 1744
-- Data for Name: expe_parameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_parameter (name, paramtype, unit, value, labbookentryid, parameterdefinitionid, experimentid) FROM stdin;
\.


--
-- TOC entry 2837 (class 0 OID 63079)
-- Dependencies: 1745
-- Data for Name: expe_software; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_software (name, vendoraddress, vendorname, vendorwebaddress, version, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2838 (class 0 OID 63085)
-- Dependencies: 1746
-- Data for Name: expe_software_tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_software_tasks (softwareid, task, order_) FROM stdin;
\.


--
-- TOC entry 2839 (class 0 OID 63092)
-- Dependencies: 1749
-- Data for Name: hold_abstractholder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_abstractholder (colposition, name, rowposition, subposition, labbookentryid, abstractholderid, holdertypeid) FROM stdin;
\N	card	\N	\N	30001	\N	\N
\.


--
-- TOC entry 2840 (class 0 OID 63095)
-- Dependencies: 1750
-- Data for Name: hold_holdca2abstholders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdca2abstholders (holdercategoryid, abstholderid) FROM stdin;
26011	30001
\.


--
-- TOC entry 2841 (class 0 OID 63098)
-- Dependencies: 1751
-- Data for Name: hold_holdca2absthoty; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdca2absthoty (holdercategoryid, abstractholdertypeid) FROM stdin;
26001	28001
26001	28003
26001	28005
26001	28007
26001	28009
26005	28011
26005	28013
26001	28015
26001	28017
26001	28019
26007	28021
26005	28023
26005	28025
26001	28027
26005	28029
26001	28031
26001	28033
26005	28035
26001	28037
26001	28039
26005	28041
26003	28043
26005	28045
26001	28047
26001	28049
26009	28051
26011	28053
\.


--
-- TOC entry 2842 (class 0 OID 63101)
-- Dependencies: 1752
-- Data for Name: hold_holder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holder (enddate, startdate, abstractholderid, crystalnumber, firstsampleid, lasttaskid) FROM stdin;
\N	\N	30001	\N	\N	\N
\.


--
-- TOC entry 2843 (class 0 OID 63104)
-- Dependencies: 1753
-- Data for Name: hold_holderlocation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holderlocation (enddate, startdate, labbookentryid, holderid, locationid) FROM stdin;
\.


--
-- TOC entry 2844 (class 0 OID 63107)
-- Dependencies: 1754
-- Data for Name: hold_holdertypeposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdertypeposition (colposition, maxvolume, maxvolumediplayunit, name, rowposition, subposition, labbookentryid, holdertypeid) FROM stdin;
\.


--
-- TOC entry 2845 (class 0 OID 63110)
-- Dependencies: 1755
-- Data for Name: hold_refholder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refholder (abstractholderid) FROM stdin;
\.


--
-- TOC entry 2846 (class 0 OID 63113)
-- Dependencies: 1756
-- Data for Name: hold_refholderoffset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refholderoffset (coloffset, rowoffset, suboffset, labbookentryid, holderid, refholderid) FROM stdin;
\.


--
-- TOC entry 2847 (class 0 OID 63116)
-- Dependencies: 1757
-- Data for Name: hold_refsampleposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refsampleposition (colposition, rowposition, subposition, labbookentryid, refholderid, refsampleid) FROM stdin;
\.


--
-- TOC entry 2848 (class 0 OID 63119)
-- Dependencies: 1758
-- Data for Name: inst_instty2inst; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY inst_instty2inst (instrumenttypeid, instrumentid) FROM stdin;
\.


--
-- TOC entry 2849 (class 0 OID 63122)
-- Dependencies: 1759
-- Data for Name: loca_location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY loca_location (locationtype, name, pressure, pressuredisplayunit, tempdisplayunit, temperature, labbookentryid, locationid, organisationid) FROM stdin;
\.


--
-- TOC entry 2850 (class 0 OID 63125)
-- Dependencies: 1760
-- Data for Name: mole_abstco_keywords; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_abstco_keywords (abstractcomponentid, keyword, order_) FROM stdin;
\.


--
-- TOC entry 2851 (class 0 OID 63128)
-- Dependencies: 1761
-- Data for Name: mole_abstco_synonyms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_abstco_synonyms (abstractcomponentid, synonym, order_) FROM stdin;
22001	N-[2-acetamido]-2-aminoethanesulfonic acid	0
22001	 N-(2-acetamido)-2-aminoethanesulfonic acid	1
22001	 N-(carbamoylmethyl)-2-aminoethanesulfonic acid	2
22001	 N-(carbamoylmethyl)taurine	3
22006	glacial acetic acid	0
22006	 ethanoic acid	1
22011	methyl cyanide	0
22011	 ACS	1
22021	ADA	0
22021	 N-(carbamoylmethyl)iminodiacetic acid	1
22026	3,6-bis(Dimethylamino) acridine monohydrochloride compound with zinc chloride	0
22026	 Euchrysine 3RX	1
22031	BIS	0
22046	ATP	0
22046	 ATP-2Na	1
22051	adonite	0
22051	 ribitol	1
22061	Bovine albumin	0
22061	 BSA	1
22061	 Cohn Fraction V	2
22066	AlCl3	0
22071	AEBSF	0
22076	6-aminohexanoic acid	0
22076	 e-aminocaproic acid	1
22081	AMPD	0
22081	 ammediol	1
22086	4-amino-3-methylphenol	0
22086	 2-amino-5-hydroxytoluene	1
22086	 4-hydroxy-2-methylaniline	2
22096	ammonium hydrogen carbonate	0
22106	Salmiac	0
22111	diammonium hydrogen citrate	0
22111	 di-ammonium hydrogen citrate	1
22111	 citric acid,ammonium salt 	2
22116	tri-ammonium citrate	0
22116	 tri-ammonium citrate	1
22116	 citric acid,triammonium salt 	2
22126	formic acid,ammonium salt 	0
22136	ammonium iron(II) sulfate hexahydrate	0
22156	ammonium phosphate dibasic	0
22156	  ammonium hydrogenphosphate	1
22156	 diammonium hydrogenphosphate	2
22156	 di-ammonium hydrogenphosphate	3
22161	ammonium phosphate monobasic	0
22161	  ammonium dihydrogenphosphate	1
22161	 prim-ammonium phosphate	2
22161	 mono-ammonium phosphate	3
22161	 ammonium di-hydrogenphosphate	4
22161	 ammonium di-hydrogen phosphate	5
22166	ammonium sulfate	0
22166	 (NH4)2SO4	1
22171	diammonium tartrate	0
22171	 di-ammonium tartrate	1
22171	 L-(+)-tartaric acid,diammonium salt 	2
22176	trifluoroacetic acid,ammonium salt 	0
22181	ammonium rhodanide	0
22191	3-[(1,1-dimethyl-2-hydroxyethyl)amino]-2-hydroxypropanesulfonic acid	0
22201	ANS	0
22226	nonanedioic acid	0
22231	5,5-diethyl-2,4,6(1H,3H,5H)-pyrimidinetrione	0
22231	 5,5-diethylbarbituric acid	1
22231	 barbitone	2
22231	 veronal	3
22241	barium dichloride	0
22256	amidinobenzene hydrochloride	0
22256	 benzamidinium chloride	1
22256	 benzenecarboximidamide hydrochloride	2
22266	phenolsulphonic acid	0
22266	 benzenesulfaonic acid	1
22271	carboxybenzene	0
22286	oxyneurine	0
22286	 (carboxymethyl)trimethylammonium inner salt	1
22286	 glycine betaine	2
22298	N,N-bis(2-hydroxyethyl)glycine 	0
22308	vitamin H	0
22313	N-biotinoyl-N'-(6-maleimidohexanoyl)hydrazide	0
22318	6,6'-iminodihexylamine	0
22318	 bis(6-aminohexyl)amine	1
22323	Bis-Tris	0
22323	 2,2-Bis(hydroxymethyl)-2,2',2''-nitrilotriethanol	1
22323	 Bis(2-hydroxyethyl)amino-tris(hydroxymethyl)methane	2
22323	 2-Bis(2-hydroxyethyl)amino-2-(hydroxymethyl)-1,3-propanediol 	3
22333	1,3-Bis[tris(hydroxymethyl)methylamino]propane	0
22333	 Bis-Tris propane 	1
22338	acidum boricum	0
22358	omega-bromo-4-nitroacetophenone	0
22358	 4-nitrophenacyl bromide	1
22363	C12E23	0
22368	1,2-Butylene glycol	0
22373	1,3-butylene glycol	0
22378	tetramethylene glycol	0
22378	 1,4-butylene glycol 	1
22393	1-butanol	0
22393	 butyl alcohol	1
22398	tert-butyl alcohol	0
22398	 trimethyl carbinol	1
22398	 2-methyl-2-propanol	2
22403	methyltert-butyl ether	0
22403	 MTBE	1
22408	n-butyl formate	0
22408	 formic acid,butyl ester	1
22413	gamma-hydroxybutyric acid lactone	0
22413	 4-hydroxbutyric acid lactone	1
22413	 GBL	2
22418	dodecyl octaethylene glycol ether	0
22418	dodecyloctaglycol	1
22418	polyoxyethylene (8) lauryl ether	2
22423	4-[cyclohexylamino]-1-butanesulfonic acid	0
22428	cadmium acetate anhydrous	0
22433	cadmium(II) bromide	0
22453	cadmium sulfate	0
22463	Ca acetate	0
22473	Ca chloride	0
22488	calcium sulfate	0
22493	2-oxohexamethyleneimine	0
22493	 aza-2-cycloheptanone	1
22498	3-cyclohexylamino-1-propanesulfonic acid	0
22503	3-(cyclohexylamino)-2-hydroxy-1-propanesulfonic acid	0
22503	 CAPSO Free Acid	1
22508	alpha-Carboxybenzylpenicillin disodium salt	0
22508	Geopen	1
22508	Pyopen	2
22508	Carindapen	3
22508	Fugacillin	4
22508	Microcillin	5
22508	Anabactyl	6
22508	Carbecin	7
22508	Gripenin	8
22508	Pyocianil	9
22508	Pyoclox	10
22533	cesium sulfate	0
22538	Hexadecyltrimethylammonium chloride	0
22543	3-[(3-cholamidopropyl)dimethylammonio]-1-propanesulfonate	0
22548	2-(cyclohexylamino)ethanesulfonic acid	0
22553	sodium p-toluenesulfonchloramide trihydrate	0
22553	 N-chloro-p-toluenesulfonamide sodium salt	1
22553	  tosylchloramide sodium	2
22558	 D-(-)-threo-2-dichloroacetamido-1-(4-nitrophenyl)-1,3-propanediol	0
22558	 chloromycetin	1
22563	methylidyne trichloride,trichloromethane	0
22568	chloronaphthol	0
22573	4-chloro-7-nitro-1,2,3-benzoxadiazole	0
22573	 NBD-chloride	1
22578	 1,2-dilauroyl-sn-glycero-3-phosphocholine	0
22578	 12:0 PC 	1
22583	3alpha,7alpha,12alpha-trihydroxy-5beta-cholanic acid	0
22583	 cholanic acid	1
22593	cobaltous chloride	0
22593	 cobalt dichloride	1
22598	cobaltous chloride hexahydrate	0
22603	copper(I) bromide	0
22603	 cuprous bromide	1
22608	copper(I) chloride	0
22608	 cuprous chloride	1
22613	copper dichloride	0
22613	 cupric chloride	1
22618	cupric chloride dihydrate	0
22623	cupric sulfate pentahydrate	0
22628	(alpha-methylguanido)acetic acid	0
22628	 N-amidinosarcosine	1
22633	(R)-2-amino-3-mercaptopropionic acid	0
22638	CTP	0
22643	bromine cyanide	0
22653	N-(D-glucityl)-N-methyldecanamide	0
22653	MEGA-10	1
22663	sodium deoxycholate	0
22663	 3-alpha,12-alpha-dihydroxy-5-beta-cholanic acid,sodium salt	1
22663	 7-deoxycholic acid,sodium salt	2
22663	 desoxycholic acid,sodium salt	3
22668	dATP	0
22673	dCTP	0
22678	 dGTP, dGTP-Na3	0
22683	dTTP	0
22688	dextran sulfate,sodium salt	0
22688	 dextran sodium sulphate	1
22688	 dextran sodium sulfate	2
22688	 dextranesulfate sodium salt	3
22693	1,6-hexanediamine	0
22693	 hexamethylenediamine	1
22698	1,8-octanediamine	0
22698	 octamethylenediamine	1
22703	1,5-pentanediamine	0
22703	 pentamethylenediamine	1
22703	 Cadaverine	2
22708	di-PIP	0
22708	 di-mu-iodobis(ethylenediamine)diplatinum (II)	1
22708	 platinum(2+),bis(1,2-ethanediamine-N,N')di-mu-iododi-,dinitrate 	2
22718	methylene chloride	0
22723	DCC	0
22728	n-dodecyl b-D-maltoside	0
22728	 dodecyl-b-D-maltoside	1
22728	 lauryl-b-D-maltoside	2
22728	 lauryl maltoside	3
22733	1,2-diheptanoyl-sn-glycero-3-phosphocholine	0
22733	 07:0 PC 	1
22738	DDAO	0
22738	 1-decanamine,N,N-dimethyl-,N-oxide	1
22743	LDAO	0
22743	 lauryldimethylamineN-oxide	1
22743	 DDAO	2
22748	octyl sulfobetaine	0
22748	 SB3-8	1
22748	 n-octyl-N,N-dimethyl-3-ammonio-1-propane sulfonate	2
22753	3-(N,N-dimethylhexadecylammonio)propanesulfonate	0
22753	 3-(hexadecyldimethylammonio)propanesulfonate	1
22753	 3-(palmityldimethylammonio)propanesulfonate	2
22753	 palmityl sulfobetaine	3
22753	 SB3-16	4
22758	DETC	0
22758	 sodium diethyldithiocarbamate trihydrate	1
22758	 cupral	2
22763	aether	0
22763	 ether	1
22763	 ethyl ether	2
22768	DEP	0
22768	 DEPC	1
22768	 diethyl dicarbonate	2
22768	 diethyl oxydiformate	3
22768	 ethoxyformic acid anhydride	4
22773	DMF	0
22773	 dimethyl formamide	1
22778	DMS	0
22783	1,4-dioxane	0
22783	 diethylene oxide	1
22788	dithizone	0
22793	2,6-pyridinedicarboxylic acid	0
22798	3-[N,N-bis(2-hydroxyethyl)amino]-2-hydroxypropanesulfonic acid	0
22798	 N,N-bis(2-hydroxyethyl)-3-amino-2-hydroxypropanesulfonic acid	1
22803	 DTNB	0
22803	 3-carboxy-4-nitrophenyl disulfide	1
22803	 6,6'-dinitro-3,3'-dithiodibenzoic acid	2
22803	 bis(3-carboxy-4-nitrophenyl) disulfide	3
22803	 Ellman's Reagent	4
22808	DTT	0
22808	 Cleland's reagent	1
22808	 1,4 dithio-DL-threitol	2
22813	 1,2-dilauroyl-sn-glycero-3-phosphocholine	0
22813	 12:0 PC 	1
22818	decyl-b-D-maltopyranoside	0
22818	 n-decyl b-D-maltoside	1
22818	 decyl-b-D-maltoside 	2
22823	 1,2-dimyristoyl-sn-glycero-3-phosphocholine	0
22823	 14:0 PC 	1
22828	 1,2-dioleoyl-sn-glycero-3-[phospho-rac-(1-glycerol)] (sodium salt)	0
22828	 18:1,[cis-9] PG 	1
22833	DMSO	0
22833	 methyl sulphoxide	1
22838	dodecyl glucoside	0
22843	SB3-12	0
22843	 3-(N,N-dimethyldodecylammonio)propanesulfonate	1
22843	 3-(N,N-dimethyllaurylammonio)propanesulfonate	2
22843	 3-(lauryldimethylammonio)propanesulfonate	3
22848	 1,2-dioleoyl-sn-glycero-3-phosphocholine	0
22848	 18:1,[cis-9] PC 	1
22853	(S)-2-methyl-1,4,5,6-tetrahydropyrimidine-4-carboxylic acid	0
22853	 THP(B)	1
22858	meso-erythritol	0
22858	 1,2,3,4-butanetetrol	1
22858	 meso-1,2,3,4-tetrahydroxybutane	2
22863	ethyl alcohol	0
22863	  EtOH	1
22865	2-aminoethanol	0
22865	 monoethanolamine	1
22865	 2-aminoethyl alcohol	2
22870	3,8-diamino-5-ethyl-6-phenylphenanthridinium bromide	0
22870	 EtBr	1
22870	 homidium bromide	2
22875	diaminoethanetetra acetic acid	0
22875	 ethylene diamine tetraacetic acid	1
22880	disodium ethylenediaminetetraacetate dihydrate	0
22880	 ethylenediamine tetraacetic acid,disodium salt dihydrate	1
22880	 EDTA-Na	2
22885	ethyl glycol	0
22885	 ethylene glycol monoethyl ether	1
22890	EtOAc	0
22895	1,2-diaminoethane	0
22900	1,2-ethanediol	0
22905	ethylene glycol -bis (beta aminoethylether) - N,N,N',N' tetraacetic acid	0
22905	 glycol ether diamine tetraacetic acid	1
22905	 egtazic acid	2
22905	 ethylene-bis(oxyethylenenitrilo)tetraacetic acid	3
22910	N-[2-hydroxyethyl]piperazine-N'[3-propanesulfonic acid]	0
22910	 4-(2-hydroxyethyl)-1-piperazinepropanesulfonic acid	1
22910	 4-(2-hydroxyethyl)piperazine-1-propanesulfonic acid	2
22910	 N-(2-hydroxyethyl)piperazine-N'-(3-propanesulfonic acid)	3
22910	 HEPPS	4
22915	NEM	0
22920	ethylmercuric phosphate	0
22925	2-butanone	0
22925	 methyl ethyl ketone	1
22930	Woodward's Reagent K	0
22935	fructose	0
22935	 D-fructose	1
22935	 D-levulose	2
22935	 fruit sugar	3
22940	L-fructose	0
22945	formalin	0
22950	amide C1	0
22950	 formic amide	1
22960	5-butylpicolinic acid	0
22960	 5-butylpyridine-2-carboxylic acid	1
22965	D-glucose	0
22965	 dextrose	1
22965	 glucose	2
22975	L-glutathione oxidized	0
22975	 glutathiol	1
22975	 GSSG	2
22980	gamma-L-glutamyl-L-cysteinyl-glycine	0
22980	 GSH	1
22985	(S)-2-aminoglutaric acid	0
22985	 Glu	1
22985	 (S)-2-aminopentanedioic acid	2
22985	 glutaminic acid	3
22990	D-2-aminoglutaramic acid	0
22990	 D-glutamic acid 5-amide	1
22995	glutaric dialdehyde solution	0
22995	 pentane-1,5-dial	1
23000	1,2,3-propanetriol	0
23000	 glycerin	1
23000	 glycerol anhydrous	2
23005	aminoacetic acid	0
23005	 glycocoll	1
23005	 aminoethanoic acid	2
23010	betaine-trimethyl-d9hydrochloride	0
23010	 N,N,N-trimethyl-d9-glycine hydrochloride	1
23015	Gly-Gly-Gly	0
23015	 triglycine	1
23020	guanidine hydrochloride	0
23020	 aminomethanamidine hydrochloride	1
23020	 guanidium chloride	2
23020	 aminoformamidine hydrochloride	3
23025	GDP	0
23030	GTP	0
23030	 5'-GTP-Na2	1
23035	N-[2-hydroxyethyl]piperazine-N'-[4-butanesulfonic acid]	0
23035	 N-(2-hydroxyethyl)piperazine-N'-(4-butanesulfonic acid)	1
23040	N-(2-hydroxyethyl)piperazine-N'-(2-ethanesulfonic acid)	0
23040	 4-(2-hydroxyethyl)piperazine-1-ethanesulfonic acid	1
23045	HEPES-Na	0
23045	 N-(2-hydroxyethyl)piperazine-N'-(2-ethanesulfonic acid) sodium salt	1
23045	 4-(2-Hydroxyethyl)piperazine-1-ethanesulfonic acid sodium salt	2
23050	N-[2-hydroxyethyl]piperazine-N'-[2-hydroxypropanesulfonic acid]	0
23055	4-Hydroxbutyric acid lactone	0
23060	heptyl-b-D-glucopyranoside	0
23060	 heptyl-beta-D-glucopyranoside	1
23065	cobalt hexammine trichloride	0
23065	 hexaamminecobalt(III) chloride	1
23070	cetyl trimethyl ammoniumbromide,palmityltrimethylammonium bromide	0
23070	 cetrimonium bromide	1
23070	 cetyltrimethylammonium bromide	2
23070	 CTAB	3
23070	 cetrimide	4
23075	hexafluoroisopropanol	0
23075	 1,1,1,3,3,3-hexafluoro-2-propanol	1
23080	hexamethylene glycol	0
23085	2,5-hexylene glycol	0
23095	hexyl-beta-D-glucopyranoside	0
23100	HCl	0
23105	acetoin	0
23105	 acetyl methyl carbinol	1
23110	hydroxylammonium chloride	0
23115	5-nitrosalicylaldehyde	0
23120	2-hydroxy-2-methylpropionic acid	0
23120	 2-methyllactic acid	1
23125	glyoxaline	0
23125	 1,3-diaza-2,4-cyclopentadiene	1
23130	surauto	0
23130	 monoiodoacetamide	1
23130	 2-iodoacetamide	2
23130	 alpha-iodoacetamide	3
23130	 alpha-iodo acetamide	4
23135	monoiodoacetic acid	0
23135	 iodoacetate	1
23135	 monoiodine acetate	2
23140	 1,5-I-AEDANS	0
23140	 5-[2-(Iodoacetamido)ethylamino]naphthalene-1-sulfonic acid	1
23140	 N-Iodoacetyl-N'-(5-sulfo-1-naphthyl)ethylenediamine	2
23140	 Hudson-Weber-Reagent	3
23145	ferric chloride hexahydrate	0
23145	 iron(III) chloride hexahydrate	1
23150	ferric chloride hexahydrate	0
23155	ferrous chloride tetrahydrate	0
23160	ferrous sulfate	0
23160	 ferrous sulphate,iron(II) sulfate	1
23165	IPTG	0
23170	sec-propyl alcohol	0
23170	 isopropanol	1
23170	 isopropyl alcohol	2
23175	polypropylene glycol 500 mono-2-aminoethyl mono-2-methoxyethyl ether	0
23185	kanamycin sulfate	0
23185	 kanamycin sulfate salt,kanamycin monosulfate	1
23190	1-beta-D-galactopyranosyl-4-alpha-D-glucopyranose	0
23190	 lactose anhydricum	1
23190	 milk sugar	2
23195	N-dodecanoyl-N-methylglycine sodium salt	0
23195	 sarkosyl	1
23195	 sarkosyl NL	2
23200	lead diacetate trihydrate, bis(acetato)trihydroxytrilead	0
23200	 lead(+2) salt trihydrate	1
23205	acetic acid,lead salt	0
23205	 lead acetate	1
23205	 dibasic lead acetate	2
23205	 lead diacetate	3
23210	acetyl-Leu-Leu-Arg-al trifluoroacetate salt	0
23230	lithium chloride anhydrous	0
23240	lithium citrate, trilithium citrate, citric acid,trilithium salt	0
23240	 1,2,3-Propanetricarboxylic acid,2-hydroxy-,trilithium salt	1
23250	formic acid lithium salt	0
23255	lithium citrate	0
23255	 citric acid,trilithium salt tetrahydrate	1
23255	 lithium citrate tetrahydrate	2
23255	  lithiumcitratetetrahydrate	3
23255	 trilithium citrate tetrahydrate	4
23255	  tri-lithium citrate 4-hydrate	5
23270	salicylic acid,lithium salt	0
23275	lithium sulfate	0
23275	 sulphuric acid,dilithium salt	1
23275	 sulfuric acid,dilithium salt	2
23280	lithium sulfate monohydrate	0
23280	 sulphuric acid,dilithium salt,monohydrate	1
23280	 sulfuric acid,dilithium salt,monohydrate,	2
23285	acetic acid,magnesium salt	0
23295	magnesium chloride	0
23305	magnesium diformate	0
23305	 formic acid,magnesium salt	1
23310	formic acid,magnesium salt magnesium formate	0
23315	magnesium dinitrate	0
23315	 nitric acid,magnesium salt	1
23315	 magnesium(II) nitrate	2
23325	magnesium sulfate	0
23330	magnesium sulfate hydrate	0
23335	kieserite, magnesium sulfate monohydrate	0
23340	magnesium sulfate monohydrate	0
23345	epsom salts	0
23345	 magnesium sulfate heptahydrate	1
23350	manganese dichloride	0
23350	 manganous chloride	1
23350	 manganese(II) chloride,anhydrous	2
23355	manganous chloride dihydrate	0
23355	 manganese dichloride dihydrate	1
23365	manganese dichloride tetrahydrate	0
23365	 manganous chloride tetrahydrate	1
23370	DL-malic acid	0
23370	 (+/-)-2-hydroxysuccinic acid	1
23370	 DL-hydroxybutanedioic acid	2
23375	propanedioc acid	0
23375	 1,3-propanedioic acid	1
23375	 dicarboxymethane	2
23380	mannite	0
23380	 cordycepic acid	1
23380	 mannitol	2
23380	 1,2,3,4,5,6-hexanehexol	3
23390	D-mannose	0
23390	 D-mannopyranose	1
23395	L-mannose	0
23395	 L-mannopyranose	1
23400	benzophenone-4-maleimide	0
23405	beta-mercaptoethanol	0
23405	 2-hydroxyethylmercaptan	1
23405	 BME	2
23405	 thioethylene glycol	3
23410	mercuric acetate	0
23410	 acetic acid,mercury(2+) salt	1
23415	mercuric chloride	0
23415	 mercury bichloride	1
23415	 mercury perchloride	2
23420	salyrganic acid	0
23420	 2-(3-hydroxymercurio-2-methoxypropylcarbamoyl)phenoxyacetic acid	1
23420	 2-[N-(3-Hydroxymercuri-2-methoxypropyl)carbamoyl]phenoxyacetic acid	2
23425	4-morpholineethanesulfonic acid	0
23425	  2-(N-morpholino)ethanesulfonic acid	1
23430	2-(N-morpholino)ethanesulfonic acid sodium salt	0
23430	 4-morpholineethanesulfonic acid sodium salt	1
23435	MeOH	0
23435	 methyl alcohol	1
23440	acetic acid,methyl ester	0
23440	 methyl ethanoate	1
23440	 methyl acetic ester	2
23445	methylmercury(II) chloride	0
23445	 mercury,chloromethyl-	1
23445	 methyl mercury(II) chloride	2
23445	 methylmercuric chloride	3
23445	 chloromethylmercury 	4
23450	MPD	0
23450	 hexylene glycol	1
23455	4-[N-morpholino]butanesulfonic acid	0
23455	 4-(N-morpholino)butanesulfonic acid	1
23460	3-(N-morpholino)propanesulfonic acid	0
23460	 4-morpholinepropanesulfonic acid	1
23465	3-[N-morpholino]-2-hydroxypropanesulfonic acid	0
23465	  beta-hydroxy-4-morpholinepropanesulfonic acid	1
23465	 3-morpholino-2-hydroxypropanesulfonic acid	2
23470	1,2,3,5/4,6-hexahydroxycyclohexane	0
23470	 meso-inositol	1
23470	 i-inositol	2
23475	beta-nicotinamide adenine dinucleotide phosphate sodium salt	0
23475	 b-NADP-Na	1
23475	 b-NADP-sodium salt	2
23475	 coenzyme II sodium salt	3
23475	 NADP	4
23475	 TPN-Na	5
23475	 TPN	6
23475	 triphosphopyridine nucleotide sodium salt	7
23480	 beta-NADPH	0
23480	 2'-NADPH	1
23480	 coenzyme II reduced tetrasodium salt	2
23480	 dihydronicotinamide adenine dinucleotide phosphate tetrasodium salt	3
23480	 NADPH Na4	4
23480	 TPNH2 Na4	5
23480	 triphosphopyridine nucleotide reduced tetrasodium salt	6
23485	NHS	0
23485	 1-hydroxy-2,5-pyrrolidinedione	1
23485	 HOSu	2
23485	 1-hydroxypyrrolidine-2,5-dione	3
23490	nickel chloride	0
23495	nickel chloride hydrate	0
23500	nickel chloride hexahydrate	0
23505	nickel sulphate	0
23505	 nickel sulfate	1
23505	 nickel(II) sulfate hexahydrate	2
23515	N-(D-glucityl)-N-methylnonanamide	0
23515	 MEGA-9	1
23515	 N-methyl-N-nonanoyl-D-glucamine	2
23520	polidocanol	0
23520	 polyoxyethylene 9 lauryl ether	1
23520	 dodecylnonaglycol	2
23520	 dodecyl nonaethylene glycol ether	3
23520	 C12E9	4
23525	polyoxyethylene 8 decyl ether	0
23525	 decyloctaglycol	1
23525	 decyl octaethylene glycol ether	2
23525	 C10E8	3
23530	OG	0
23530	 n-octyl b-D-glucopyranoside	1
23530	 n-Octyl glucoside	2
23530	 OGP	3
23535	OTG	0
23535	 OSGP	1
23540	n-octyl-b-D-glucoside	0
23540	 n-octyl-beta-D-glucopyranoside	1
23540	 BOG	2
23540	 n-octyl glucoside	3
23545	MEGA-8	0
23545	 OMEGA	1
23545	 N-methyl-N-octanoyl-D-glucamine	2
23545	 N-(D-Glucityl)-N-methyloctanamide	3
23550	n-nonyl-b-D-glucopyranoside	0
23550	 NG	1
23550	 n-nonyl-b-D-glucoside	2
23555	phosphoric acid	0
23565	p-chloromercuribenzoate	0
23565	 p-chloromercuribenzoic acid	1
23565	 4-chloromercuribenzoate	2
23570	4-(chloromercuri)benzensulfonic acid,sodium salt	0
23570	 p-chloromercuribenzenesulfonic acid	1
23570	 4-chloromercuribenzenesulfonate	2
23570	 4-chloromercuribenzenesulfonic acid	3
23570	 chloro(4-sulfophenyl)mercury	4
23575	PAR monosodium salt hydrate	0
23575	 PAR	1
23580	pentaerythritol ethoxylate (15/4 EO/OH)	0
23585	pentaerythritol propoxylate (5/4 PO/OH)	0
23590	polyoxyethylene 5 decyl ether	0
23590	 decylpentaglycol	1
23590	 decyl pentaethylene glycol ether	2
23590	 C10E5	3
23595	octylpentaglycol	0
23595	 octyl pentaethylene glycol ether	1
23595	 C8E5	2
23600	diethyl ketone	0
23600	 DEK	1
23600	 dimethylacetone	2
23600	 ethyl ketone	3
23600	 metacetone	4
23600	 propione	5
23605	N-isobutyrylpepstatin	0
23605	 isobutyryl-Val-Val-Sta-Ala-Sta	1
23615	hydroxybenzene	0
23620	phenolsulfonphthalein	0
23625	2,2-dihydroxyacetophenone	0
23630	mercury phenyl acetate	0
23635	1,2-Diacyl-sn-glycero-3-phosphocholine	0
23635	 3-sn-phosphatidylcholine	1
23635	 L-alpha-lecithin	2
23635	 azolectin	3
23640	piperazine-1,4-bis(2-ethanesulfonic acid)	0
23640	 1,4-piperazinediethanesulfonic acid	1
23640	 piperazine-N,N'-bis(2-ethanesulfonic acid)	2
23645	phenylmethylsulphonyl fluoride	0
23645	 alpha-toluenesulfonyl fluoride	1
23645	 benzylsulfonyl fluoride	2
23645	 phenylmethylsulfonyl fluoride	3
23650	poly(sodium acrylate)	0
23650	 sodium polyacrylate	1
23650	 polyacrylic acid 5100 sodium salt	2
23655	PEG 400	0
23655	 poly(ethylene glycol) 400	1
23660	PEG 200	0
23660	 poly(ethylene glycol) 200	1
23665	PEG 300	0
23665	 poly(ethylene glycol) 300	1
23670	PEG 600	0
23670	 poly(ethylene glycol) 600	1
23675	PEG 1000	0
23675	 PEG 1K	1
23675	 poly(ethylene glycol) 1000	2
23680	PEG 1500	0
23680	 PEG 1.5K	1
23680	 poly(ethylene glycol) 1500	2
23685	PEG 2000	0
23685	 PEG 2K	1
23685	 poly(ethylene glycol) 2000	2
23690	PEG 3000	0
23690	 PEG 3K	1
23690	 poly(ethylene glycol) 3000	2
23695	PEG 3350	0
23695	 polyethylene glycol 3.350	1
23695	 poly(ethylene glycol) 3350	2
23700	PEG 3350 monodisperse	0
23700	 polyethylene glycol 3.350 monodisperse	1
23700	 poly(ethylene glycol) 3350 monodisperse	2
23705	PEG 4000	0
23705	 PEG 4K	1
23705	 poly(ethylene glycol) 4000	2
23710	PEG 5000	0
23710	 PEG 5K	1
23710	 poly(ethylene glycol) 5000	2
23715	PEG 6000	0
23715	 PEG 6K	1
23715	 poly(ethylene glycol) 6000	2
23720	PEG 8000	0
23720	 PEG 8K	1
23720	 poly(ethylene glycol) 8000	2
23725	 PEG 10,000	0
23725	 PEG 10K	1
23725	 poly(ethylene glycol) 10,000 	2
23730	 PEG 20,000	0
23730	 PEG 20K	1
23730	 polyethylene glycol 20,000 	2
23735	PEG 550 MME	0
23735	 polyethylene glycol 550 MME	1
23735	 polyethylene glycol MME 550	2
23735	 methoxypolyethylene glycol 550	3
23735	 mono-methyl polyethylene glycol 550	4
23740	PEG 2000 MME	0
23740	 PEG 2K MME	1
23740	 polyethylene glycol 2000 MME	2
23740	 polyethylene glycol MME 2000	3
23740	 methoxypolyethylene glycol 2000	4
23740	 mono-methyl polyethylene glycol 2000	5
23745	PEG 5000 MME	0
23745	 PEG 5K MME	1
23745	 polyethylene glycol 5000 MME	2
23745	 polyethylene glycol MME 5000	3
23745	 methoxypolyethylene glycol 5000	4
23745	 mono-methyl polyethylene glycol 5000	5
23750	ethylene imine polymer	0
23750	 aziridine polymer	1
23750	 epamine	2
23750	 epomine	3
23750	 ethylenimine polymer	4
23750	 montrek	5
23750	 PEI	6
23750	 poly(ethylene imine)	7
23750	 poly(ethyleneimine)	8
23755	PPG 400	0
23755	 poly(propylene oxide)	1
23760	polymixin B sulfate	0
23760	 polymixin B sulphate salt	1
23765	poly(A) potassium salt	0
23770	PVA	0
23770	 elvanol	1
23770	 polyvinyl alcohol	2
23770	 ethenol homopolymer	3
23770	 polyviol	4
23770	 vinol	5
23770	 alvyl	6
23775	polyvidone	0
23775	 povidone	1
23775	 PVP	2
23780	1-palmitoyl-2-oleoyl-sn-glycero-3-phosphocholine	0
23780	 16:0-18:1,[cis-9] PC	1
23780	  1-(cis-9-octadecenoyl)-2-hexadecanoyl-sn-glycero-3-phosphocholine	2
23780	 L-alpha-phosphatidylcholine,beta-palmitoyl-gamma-oleoyl	3
23785	piperazine-N,N'-bis-[2-hydroxypropanesulfonic acid]	0
23785	  piperazine-1,4-bis(2-hydroxypropanesulfonic acid) dihydrate	1
23785	 piperazine-N,N'-bis(2-hydroxypropanesulfonic acid)	2
23790	K(acac)	0
23795	kalii bromidum	0
23800	kalii chloridum	0
23805	citric acid,monopotassium salt	0
23805	 potassium dihydrogen citrate	1
23810	citric acid,tripotassium salt	0
23820	potassium di-hydrogen orthophosphate (mono-basic)	0
23820	 monopotassium phosphate	1
23820	 kalii dihydrogenophosphas	2
23820	 potassium dihydrogen orthophosphate	3
23820	 prim.-potassium phosphate	4
23825	di-potassium hydrogen orthophosphate (di-basic)	0
23825	 di-potassium hydrogen phosphate	1
23825	 di-potassium orthophosphate	2
23825	 phosphoric acid,dipotassium salt	3
23830	di-potassium hydrogen phosphate trihydrate	0
23830	 dipotassium hydrogen phosphate trihydrate	1
23830	 dipotassium phosphate	2
23850	potassium platinum(IV) chloride	0
23855	caustic potash	0
23860	kalii permanganas	0
23865	kalii iodidum	0
23875	potassium/sodium tartrate	0
23875	 K-Na tartrate tetrahydrate	1
23875	 L(+)-tartaric acid potassium sodium salt	2
23875	 Rochelle salt	3
23875	 Seignette salt	4
23875	 sodium potassium tartrate	5
23875	 Na/K tartrate	6
23880	Seignette salt	0
23880	 Rochelle salt	1
23880	 potassium sodium tartrate	2
23880	 sodium potassium L-tartrate	3
23880	 sodium potassium (dl)-tartrate	4
23880	 potassium sodium L(+)-tartrate	5
23880	 monopotassium monosodium tartrate	6
23885	potassium sulfate	0
23890	di-potassium tartrate	0
23890	 potassium tartrate	1
23890	 2,3-dihydroxy-(2R,3R)-butanedioic acid dipotassium salt	2
23890	 tartaric acid dipotassium salt	3
23890	 dipotassium L-(+)-tartrate	4
23890	 neutral potassium tartrate	5
23890	 soluble tartar	6
23895	potassium gold(III) chloride	0
23900	potassium platinum(II) chloride	0
23920	potassium rhodanide	0
23925	proline	0
23925	 (S)-pyrrolidine-2-carboxylic acid	1
23930	isopropanol	0
23930	 propan-2-ol	1
23930	 isopropyl alcohol	2
23930	 sec-propyl alcohol	3
23935	propylene glycol	0
23940	1,3-dihydroxypropane	0
23940	 trimethylene glycol	1
23945	ethyl cyanide	0
23950	azine	0
23950	  azabenzine	1
23955	protamine sulfate	0
23955	  clupeine	1
23960	3-hydroxy-5-(hydroxymethyl)-2-methyl-4-pyridinecarboxaldehyde hydrochloride	0
23960	 PL HCl	1
23965	O-alpha-D-galactopyranosyl-(1-6)-alpha-D-glucopyranosyl beta-D-fructofuranoside	0
23965	 melitose	1
23965	 melitriose	2
23970	(-)-riboflavin	0
23970	 lactoflavin	1
23970	 vitamin B2	2
23970	 vitamin G	3
23970	 6,7-dimethyl-9-d-ribitylisoalloxazine	4
23970	 flavaxin	5
23970	 beflavin	6
23970	 7,8-dimethyl-10-(d-ribo-2,3,4,5-tetrahydroxypentyl)riboflavinequinone	7
23970	 hyflavin	8
23970	 lactoflavin	9
23970	 lactoflavine	10
23970	 ribipca	11
23970	 riboderm	12
23970	 riboflavinequinone	13
23985	argenti nitras	0
23990	acetic acid,sodium salt	0
23990	 Na Acetate	1
23995	acetic acid,sodium salt trihydrate	0
24000	azide	0
24000	 azium	1
24000	 hydrazoic acid,sodium salt 	2
24010	cacodylic acid,sodium salt hydrate	0
24015	sodium cacodylate	0
24015	 cacodylic acid,sodium salt trihydrate	1
24015	 dimethylarsinic acid,sodium salt	2
24015	 dimethylarsonic acid,sodium salt	3
24020	soda ash	0
24025	NaCl	0
24030	sodium citrate dihydrate	0
24030	 sodium citrate tribasic dihydrate	1
24030	 sodium citrate tribasic dihydrate	2
24030	 citric acid,trisodium salt dihydrate	3
24030	 trisodium citrate dihydrate	4
24030	 tri-sodium citrate dihydrate	5
24035	sodium dihydrogen citrate	0
24035	 citric acid,monosodium salt	1
24040	3a,12a-dihydroxy-5b-cholanic acid,sodium salt	0
24040	 7-deoxycholic acid,sodium salt	1
24040	 desoxycholic acid,sodium salt	2
24045	3a,12a-dihydroxy-5b-cholanic acid,sodium salt	0
24045	 7-deoxycholic acid,sodium salt	1
24045	 desoxycholic acid,sodium salt 	2
24050	dodecyl sodium sulfate	0
24050	 dodecyl sulfate sodium salt	1
24050	 lauryl sulfate sodium salt	2
24050	 SDS	3
24050	 sodium lauryl sulfate	4
24060	 formic acid,sodium salt 	0
24065	sodium hexadecyl sulphate	0
24065	 sodium hexadecyl sulfate	1
24065	 cetyl sodium sulfate	2
24065	 sodium monohexadecyl sulfate	3
24065	 sodium palmityl sulfate	4
24065	 sodium n-hexadecyl sulfate	5
24065	 Tergitol anionic 7	6
24065	 1-hexadecanol,hydrogen sulfate,sodium salt	7
24070	sodium bicarbonate	0
24070	 natrii hydrogenocarbonas	1
24075	sodium di-hydrogen orthophosphate dihydrate (monobasic)	0
24075	 natrii dihydrogenophosphas dihydricus	1
24075	 sodium dihydrogen phosphate dihydrate	2
24080	di sodium hydrogen orthophosphate diahydrate (dibasic)	0
24080	 sec-sodium phosphate	1
24080	 di-sodium hydrogen phosphate dihydrate	2
24080	 disodium hydrogen phosphate dihydrate	3
24080	 disodium phosphate	4
24085	sec-Sodium phosphate	0
24085	 Disodium hydrogen phosphate	1
24085	 Disodium phosphate	2
24085	 Sodium hydrogenphosphate	3
24090	caustic soda	0
24100	malonic acid,disodium salt	0
24105	sodium malonate dibasic monohydrate	0
24105	 malonic acid,disodium salt monohydrate	1
24105	 propanedioic acid	2
24110	sodium periodate	0
24115	Chile salpeter	0
24130	trisodium phosphate	0
24135	propionic acid,sodium salt	0
24140	sodium succinate dibasic	0
24140	 succinic acid,disodium salt	1
24140	 disodium succinate	2
24145	sodium sulfate	0
24150	Glauber's salt	0
24150	 sodium sulfate decahydrate	1
24155	L-(+)-tartaric acid disodium salt	0
24155	 disodium tartrate dihydrate	1
24155	 sodium tartrate dihydrate	2
24160	borax	0
24165	sodium isothiocyanate	0
24165	 sodium rhodanate	1
24165	 sodium rhodanide	2
24165	 sodium sulfocyanate	3
24170	sodium thiosulfate pentahydrate	0
24170	 hypo	1
24170	 sodium hyposulfite	2
24175	TCA sodium salt	0
24175	 trichloroacetic acid	1
24175	 TCA	2
24180	 1-stearoyl-2-oleoyl-sn-glycero-3-phosphocholine	0
24180	 18:0-18:1,[cis-9] PC 	1
24185	D-sorbitol	0
24185	 D-Glucitol	1
24190	1,8-diamino-4-azaoctane	0
24190	 N-(3-aminopropyl)-1,4-diaminobutane	1
24195	N-(3-aminopropyl)-1,4-butanediamine trihydrochloride	0
24195	 spermidine tetra-HCl	1
24200	N,N'-bis(3-aminopropyl)-1,4-butanediamine tetrahydrochloride	0
24205	streptomycin sulfate	0
24210	strontium chloride	0
24215	butanedioic acid	0
24220	dihydro-2,5-furandione	0
24220	 succinyloxide	1
24220	 butanedioic anhydride	2
24220	 2,5-diketotetrahydrofuran	3
24220	 succinyl anhydride	4
24220	 tetrahydro-2,5-furandione	5
24225	2,5 pyrolidinedione	0
24225	 butanimide	1
24230	beta-D-fructofuranosyl-alpha-D-glucopyranoside sugar	0
24230	 D(+)-saccharose	1
24230	 alpha-D-glucopyranosyl-beta-D-fructofuranoside	2
24235	bis(sulfo-N-succinimidyl) ethylene glycol disuccinate	0
24235	 ethylene glycol bis(sulfosuccinimidyl succinate)	1
24240	sulfuric acid	0
24245	N-tris[hydroxymethyl]methyl-3-aminobutanesulfonic acid	0
24255	N-tris[hydroxymethyl]methyl-3-aminopropanesulfonic acid	0
24260	3-[N-tris(hydroxymethyl)methylamino]-2-hydroxypropanesulfonic acid	0
24260	 2-hydroxy-3-[tris(hydroxymethyl)methylamino]-1-propanesulfonic acid	1
24260	 N-[tris(hydroxymethyl)methyl]-3-amino-2-hydroxypropanesulfonic acid	2
24265	2-aminoethanesulfonic acid	0
24270	 N,N,N',N',-tetramethylethylenediamine,1,2-Bis(dimethylamino)ethane	0
24270	 TEMEDA	1
24275	N-tris[hydroxymethyl]methyl-2-aminoethanesulfonic acid	0
24275	 2-[(2-hydroxy-1,1-bis(hydroxymethyl)ethyl)amino]ethanesulfonic acid	1
24275	 N-[tris(hydroxymethyl)methyl]-2-aminoethanesulfonic acid	2
24275	 TES Free Acid	3
24285	C8E4	0
24285	 octyl tetraethylene glycol ether	1
24285	 octyltetraglycol	2
24290	myristyl sulfobetaine	0
24290	 3-(N,N-dimethylmyristylammonio)propanesulfonate	1
24290	 3-(N,N-dimethylmyristylammonio)propane-sulfonate	2
24290	 3-(N,N-dimethyltetradecylammonio)propanesulfonate	3
24290	 3-(myristyldimethylammonio)propanesulfonate	4
24290	 N-tetradecyl-N,N-dimethyl-3-ammonio-1-propanesulfonate	5
24290	 SB3-14	6
24295	tetrahydrofuran	0
24295	 THF	1
24300	cyclohexene-1,2-dicarboxylic anhydride	0
24305	tetrakis(acetoxymercuri)methane	0
24305	 acetic acid: trimercuriomethylmercury	1
24305	 TAMM	2
24305	 mercury,tetrakis(acetato)-mu4-methanetetrayltetra- (8CI)	3
24305	 mercury,tetrakis(acetato-O)-mu4-methanetetrayltetra-	4
24310	tetramethylammonium chloride	0
24310	 TMA	1
24315	ethylmercurithiosalicylic acid,sodium salt	0
24315	 2-(ethylmercuriomercapto)benzoic acid,sodium salt	1
24320	2-isopropyl-5-methylphenol	0
24320	 5-methyl-2-(1-methylethyl)phenol	1
24320	 5-methyl-2-isopropylphenol	2
24320	 thyme camphor	3
24320	 thymic acid	4
24325	N-tosyl-L-lysine chloromethyl ketone	0
24330	N-tosyl-L-phenylalanine chloromethyl ketone	0
24330	 L-1-tosylamide-2-phenyl-ethyl chloromethyl ketone	1
24335	tetrakis (2-pyridylmethyl)ethylene diamine	0
24340	mycose	0
24340	 alpha-D-trehalose	1
24340	 D-(+)-trehalose	2
24345	TCA	0
24350	N-[tris(hydroxymethyl)methyl]glycine	0
24355	tris(2-hydroxyethyl)amine	0
24355	 tri(hydroxyethyl)amine	1
24355	 Trolamine	2
24355	 TEA (amino alcohol)	3
24355	 nitrilo-2,2',2''-triethanol	4
24355	 triethylolamine	5
24355	 Daltogen	6
24360	triglycol	0
24365	trifluoroethyl alcohol	0
24365	TFE	1
24370	trimethylammonium chloride	0
24380	trimethylammonium chloride	0
24380	 trimethylammonium hydrochloride	1
24385	acetoxytrimethyllead(IV)	0
24390	TTC	0
24390	 TPTZ	1
24390	 2,3,5-triphenyl-2H-tetrazolium chloride	2
24390	 tetrazolium red	3
24395	tris(hydroxymethyl)aminomethane	0
24395	 2-amino-2-(hydroxymethyl)-1,3-propanediol	1
24395	 THAM	2
24395	 tris base	3
24395	 trometamol 	4
24400	TRIS hydrochloride	0
24400	 tris(hydroxymethyl)aminomethane hydrochloride	1
24405	ruthenium-tris(2,2'-bipyridyl) dichloride	0
24405	 tris(2,2'-bipyridyl)ruthenium(II) chloride hexahydrate	1
24410	4-(1,1,3,3-tetramethylbutyl)phenyl-polyethylene glycol	0
24410	 polyethylene glycol tert-octylphenyl ether	1
24415	(1,1,3,3-Tetramethylbutyl)phenyl-polyethylene glycol	0
24420	Bowman-Birk Inhibitor	0
24430	polyethylene glycol sorbitan monolaurate	0
24435	carbamide	0
24435	 carbonyldiamide	1
24440	vancomycin hydrochloride from Streptomyces orientalis	0
24445	alpha-(5,6-dimethylbenzimidazolyl)cyanocobamide	0
24445	 CN-Cbl	1
24445	 cyanocob(III)alamin	2
24445	 cyanocobalamin	3
24450	5 bromo-4-chloro-3-indolyl-b-D-galactopyranoside	0
24455	xylene (cyanole)	0
24455	 acid blue 147	1
24455	 cyanol FF	2
24455	 XC	3
24465	xylite	0
24470	ytterbium trichloride hexahydrate	0
24475	yttrium trichloride	0
24480	yttrium trichloride	0
24500	sulfuric acid,zinc salt (1:1) heptahydrate	0
24500	 zinc vitriol,heptahydrate	1
24500	 zinc sulfate,heptahydrate	2
24500	 zinc sulphate,heptahydrate	3
24505	 zinc sulfate	0
24505	 sulfuric acid,zinc salt	1
24505	 sulphuric acid,zinc salt	2
24505	 zinc sulfate anhydrous	3
\.


--
-- TOC entry 2852 (class 0 OID 63131)
-- Dependencies: 1762
-- Data for Name: mole_abstractcomponent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_abstractcomponent (name, labbookentryid, naturalsourceid) FROM stdin;
ACES	22001	\N
acetic acid	22006	\N
acetone	22011	\N
acetonitrile	22016	\N
N-(2-acetamido)iminodiacetic acid	22021	\N
acridine orange : hemi (zinc chloride)	22026	\N
acrylamide	22031	\N
N,N'-(1,2-dihydroxyethylene)bis-acrylamide	22036	\N
adenosine	22041	\N
adenosine 5'-triphosphate disodium salt	22046	\N
adonitol	22051	\N
L-alanine	22056	\N
albumin bovine	22061	\N
aluminium chloride	22066	\N
4-(2-amino ethyl) benzenesulfonyl fluoride HCl	22071	\N
6-aminocaproic acid	22076	\N
2-amino-2-methyl-1,3-propanediol	22081	\N
4-amino-m-cresol	22086	\N
ammonium acetate	22091	\N
ammonium bicarbonate	22096	\N
ammonium bromide	22101	\N
ammonium chloride	22106	\N
ammonium citrate dibasic	22111	\N
ammonium citrate tribasic	22116	\N
ammonium fluoride	22121	\N
ammonium formate	22126	\N
ammonium iodide	22131	\N
ammonium iron(II) sulphate hexahydrate	22136	\N
ammonium nitrate	22141	\N
ammonium oxalate	22146	\N
ammonium persulphate	22151	\N
diammonium hydrogen phosphate	22156	\N
ammonium dihydrogen phosphate	22161	\N
ammonium sulphate	22166	\N
ammonium tartrate dibasic	22171	\N
ammonium trifluoroacetate	22176	\N
ammonium thiocyanate	22181	\N
ampicillin (sodium)	22186	\N
AMPSO	22191	\N
aniline	22196	\N
8-anilino-1-naphthalene sulphonic acid (ammonium salt)	22201	\N
L-arginine	22206	\N
L-ascorbic acid	22211	\N
L-asparagine	22216	\N
L-aspartic acid	22221	\N
azelaic acid	22226	\N
barbital	22231	\N
barium acetate	22236	\N
barium chloride	22241	\N
barium nitrate	22246	\N
barium thiocyanate trihydrate	22251	\N
benzamidine hydrochloride hydrate	22256	\N
benzamidine hydrochloride	22261	\N
benzenesulphonic acid	22266	\N
benzoic acid	22271	\N
BES	22276	\N
beryllium sulphate tetrahydrate	22281	\N
betaine	22286	\N
betaine monohydrate	22288	\N
bestatin hydrochloride	22293	\N
bicine	22298	\N
BigCHAP	22303	\N
D-biotin	22308	\N
biotin-maleimide	22313	\N
bis(hexamethylene)triamine	22318	\N
BIS-TRIS	22323	\N
BIS-TRIS hydrochloride	22328	\N
BIS-TRIS propane	22333	\N
boric acid	22338	\N
Brilliant Blue G	22343	\N
Brilliant Blue R	22348	\N
bromophenol blue,sodium salt	22353	\N
2-bromo-4'-nitroacetophenone	22358	\N
polyoxyethylene(23) lauryl ether	22363	\N
1,2-butanediol	22368	\N
1,3-butanediol	22373	\N
1,4-butanediol	22378	\N
2,3-butanediol	22383	\N
1,4-butanediol diglycidyl ether	22388	\N
n-butanol	22393	\N
tert-butanol	22398	\N
tert-butyl methylether	22403	\N
butyl formate	22408	\N
gamma-butyrolactone	22413	\N
C12E8	22418	\N
CABS	22423	\N
cadmium acetate	22428	\N
cadmium bromide	22433	\N
cadmium chloride	22438	\N
cadmium chloride dihydrate	22443	\N
cadmium iodide	22448	\N
cadmium sulphate	22453	\N
cadmium sulphate hydrate	22458	\N
calcium acetate	22463	\N
calcium carbonate	22468	\N
calcium chloride	22473	\N
calcium chloride dihydrate	22478	\N
calcium chloride hexahydrate	22483	\N
calcium sulphate	22488	\N
e-caprolactam	22493	\N
CAPS	22498	\N
CAPSO	22503	\N
carbenicillin disodium salt	22508	\N
cesium bromide	22513	\N
cesium chloride	22518	\N
cesium iodide	22523	\N
cesium nitrate	22528	\N
cesium sulphate	22533	\N
cetyltrimethylammonium chloride	22538	\N
CHAPS	22543	\N
CHES	22548	\N
chloramine T hydrate	22553	\N
chloramphenicol	22558	\N
chloroform	22563	\N
4 chloro-1-naphthol	22568	\N
7-chloro-4-nitro benz-2-oxa-1,3-diazole	22573	\N
cholesterol	22578	\N
cholic acid	22583	\N
citric acid	22588	\N
cobalt(II) chloride	22593	\N
cobalt(II) chloride hexahydrate	22598	\N
copper bromide	22603	\N
copper chloride	22608	\N
copper(II) chloride	22613	\N
copper(II) chloride dihydrate	22618	\N
copper(II) sulphate pentahydrate	22623	\N
creatine	22628	\N
L-cysteine	22633	\N
cytidine 5'-triphosphate disodium salt	22638	\N
cyanogen bromide	22643	\N
2-cyclohexen-1-one	22648	\N
N-decanoyl-N-methylglucamine	22653	\N
3-(decyldimethylammonio)propane-1-sulfonate	22658	\N
deoxycholic acid (sodium salt)	22663	\N
2'-deoxyadenosine-5'-triphosphate,disodium salt,trihydrate	22668	\N
2'-deoxycytidine 5'-triphosphate disodium salt	22673	\N
2'-deoxyguanosine 5'-triphosphate trisodium salt	22678	\N
2'-deoxythymidine 5'-triphosphate sodium salt	22683	\N
dextran sulphate,sodium salt	22688	\N
1,6-diaminohexane	22693	\N
1,8-diaminooctane	22698	\N
1,5-diaminopentane	22703	\N
di-u-iodobis(ethylenediamine)-di-platinum (II) nitrate	22708	\N
dichloro(ethylenediamine)platinum (II)	22713	\N
dichloromethane	22718	\N
N,N'-dicyclohexylcarbodiimide	22723	\N
DDM	22728	\N
DHPC	22733	\N
N,N-dimethyldecylamine-N-oxide	22738	\N
N,N-dimethyldodecylamine-N-oxide	22743	\N
3-(N,N-dimethyloctylammonio)propane-sulfonate	22748	\N
3-(N,N-dimethylpalmitylammonio)propane-sulfonate	22753	\N
diethyl-dithio-carbamic acid (sodium salt)	22758	\N
diethyl ether	22763	\N
diethyl pyrocarbonate	22768	\N
N,N-dimethylformamide	22773	\N
dimethyl suberimidate dihydrchloride	22778	\N
dioxane	22783	\N
diphenylthiocarbazone	22788	\N
dipicolinic acid	22793	\N
DIPSO	22798	\N
5 5' dithio-bis (2-nitrobenzoic acid)	22803	\N
dithiothreitol	22808	\N
DLPC	22813	\N
DM	22818	\N
DMPC	22823	\N
DOPG	22828	\N
dimethylsulphoxide	22833	\N
dodecyl-b-D-glucopyranoside	22838	\N
3-(dodecyldimethylammonio)propanesulfonate	22843	\N
DOPC	22848	\N
ectoine	22853	\N
erythritol	22858	\N
ethanol	22863	\N
ethanolamine	22865	\N
ethidium bromide	22870	\N
EDTA	22875	\N
EDTA disodium salt	22880	\N
2-ethoxyethanol	22885	\N
ethyl acetate	22890	\N
ethylenediamine	22895	\N
ethylene glycol	22900	\N
EGTA	22905	\N
EPPS	22910	\N
N ethylmaleimide	22915	\N
ethyl mercuryphosphate	22920	\N
ethyl methylketone	22925	\N
2-ethyl-5-phenylisoxazolium sulfonate	22930	\N
D-(-)-fructose	22935	\N
L-(+)-fructose	22940	\N
formaldehyde solution	22945	\N
formamide	22950	\N
formic acid	22955	\N
fusaric acid	22960	\N
D-(+)-glucose	22965	\N
L-(-)-glucose	22970	\N
glutathione (oxidised form)	22975	\N
glutathione (reduced form)	22980	\N
L-glutamic acid	22985	\N
D-glutamine	22990	\N
glutaraldehyde	22995	\N
glycerol	23000	\N
glycine	23005	\N
glycine betaine	23010	\N
glycyl-glycyl-glycine	23015	\N
guanidinium chloride	23020	\N
guanosine 5'-diphosphate (GDP) - sodium salt	23025	\N
guanosine 5'-triphosphate di sodium salt	23030	\N
HEPBS	23035	\N
HEPES	23040	\N
HEPES sodium salt	23045	\N
HEPPSO	23050	\N
1,2,3-heptanetriol	23055	\N
n-heptyl-b-D-glucopyranoside	23060	\N
hexaaminecobalt trichloride	23065	\N
hexadecyltrimethylammonium bromide	23070	\N
hexafluoro isopropanol	23075	\N
1,6-hexanediol	23080	\N
2,5-hexanediol	23085	\N
1,2,3-hexanetriol	23090	\N
hexyl-b-D-glucopyranoside	23095	\N
hydrochloric acid	23100	\N
3' hydroxy-2-butanone	23105	\N
hydroxylamine hydrochloride	23110	\N
2-hydroxy-5-nitrobenzaldehyde	23115	\N
alpha-hydroxyisobutyric acid	23120	\N
imidazole	23125	\N
iodoacetamide	23130	\N
iodoacetic acid	23135	\N
N-iodo acetyl-N-(5-sulfo-1-naphthyl) ethyldiamine	23140	\N
iron(III) chloride	23145	\N
iron(III) chloride hexahydrate	23150	\N
iron(II) chloride tetrahydrate	23155	\N
iron(II) sulphate	23160	\N
Isopropyl-b-D-thiogalactopyranoside	23165	\N
iso-propanol	23170	\N
O-(2-aminopropyl)-O'-(2-methoxyethyl)polypropylene glycol	23175	\N
alpha,omega-diamine poly(oxyethylene-co-oxypropylene)	23180	\N
kanamycin monosulphate	23185	\N
lactose	23190	\N
N-lauroylsarcosine  sodium salt	23195	\N
lead(II) acetate trihydrate	23200	\N
lead(II) acetate	23205	\N
leupeptin trifluoroacetate salt	23210	\N
lithium acetate	23215	\N
lithium acetate dihydrate	23220	\N
lithium bromide	23225	\N
lithium chloride	23230	\N
lithium chloride (hydrate)	23235	\N
lithium citrate (anhydrous)	23240	\N
lithium fluoride	23245	\N
lithium formate	23250	\N
tri-lithium citrate tetrahydrate	23255	\N
lithium nitrate	23260	\N
lithium perchlorate	23265	\N
lithium salicylate	23270	\N
lithium sulphate	23275	\N
lithium sulphate monohydrate	23280	\N
magnesium acetate tetrahydrate	23285	\N
magnesium bromide	23290	\N
magnesium chloride anhydrous	23295	\N
magnesium chloride  hexahydrate	23300	\N
magnesium formate	23305	\N
magnesium formate dihydrate	23310	\N
magnesium nitrate	23315	\N
magnesium nitrate hexahydrate	23320	\N
magnesium sulphate	23325	\N
magnesium sulphate hydrate	23330	\N
magnesium sulphate monohydrate	23335	\N
magnesium sulphate hexahydrate	23340	\N
magnesium sulphate heptahydrate	23345	\N
manganese(II) chloride	23350	\N
manganese(II) chloride dihydrate	23355	\N
manganese(II) chloride monohydrate	23360	\N
manganese(II) chloride tetrahydrate	23365	\N
malic acid	23370	\N
malonic acid	23375	\N
D-mannitol	23380	\N
L-mannitol	23385	\N
D-(+)-mannose	23390	\N
L-(-)- mannose	23395	\N
4-(N-maleimido) benzophenone	23400	\N
2-mercaptoethanol	23405	\N
mercury(II) acetate	23410	\N
mercury(II) chloride	23415	\N
mersalyl acid	23420	\N
MES	23425	\N
MES sodium salt	23430	\N
methanol	23435	\N
methyl acetate	23440	\N
methylmercury chloride	23445	\N
2-methyl-2,4-pentanediol	23450	\N
MOBS	23455	\N
MOPS	23460	\N
MOPSO	23465	\N
myo-inositol	23470	\N
NADP sodium salt	23475	\N
NADPH tetrasodium salt	23480	\N
N-hydroxysuccinimide	23485	\N
nickel(II) chloride	23490	\N
nickel(II) chloride hydrate	23495	\N
nickel(II) chloride hexahydrate	23500	\N
nickel(II) sulphate hexahydrate	23505	\N
nitric acid	23510	\N
N-nonanoyl-N-methylglucamine	23515	\N
nonaethylene glycol monododecyl ether	23520	\N
octaethylene glycol monodecyl ether	23525	\N
octyl b-D-glucopyranoside	23530	\N
n-octyl b-D-thioglucopyranoside	23535	\N
beta-octylglucoside	23540	\N
N-octanoyl-N-methylglucamine	23545	\N
nonyl-b-D-glucopyranoside	23550	\N
orthophosphoric acid	23555	\N
paratone-N	23560	\N
PCMB	23565	\N
PCMBS	23570	\N
4-(2-pyridylazo)resorcinol mono sodium salt monohydrate	23575	\N
pentaerythritol ethoxylate (3/4 EO/OH)	23580	\N
pentaerythritol propoxylate	23585	\N
pentaethylene glycol monodecyl ether	23590	\N
pentaethylene glycol monooctyl ether	23595	\N
3-pentanone	23600	\N
pepsinostreptin	23605	\N
1,10 phenanthroline	23610	\N
phenol	23615	\N
phenol red	23620	\N
phenylglyoxal hydrate	23625	\N
phenylmercury acetate	23630	\N
L-alpha-phosphatidylcholine	23635	\N
PIPES	23640	\N
PMSF	23645	\N
poly(acrylic acid sodium salt) 5100	23650	\N
polyethylene glycol 400	23655	\N
polyethylene glycol 200	23660	\N
polyethylene glycol 300	23665	\N
polyethylene glycol 600	23670	\N
polyethylene glycol 1000	23675	\N
polyethylene glycol 1500	23680	\N
polyethylene glycol 2000	23685	\N
polyethylene glycol 3000	23690	\N
polyethylene glycol 3350	23695	\N
polyethylene glycol 3350 monodisperse	23700	\N
polyethylene glycol 4000	23705	\N
polyethylene glycol 5000	23710	\N
polyethylene glycol 6000	23715	\N
polyethylene glycol 8000	23720	\N
polyethylene glycol 10000	23725	\N
polyethylene glycol 20000	23730	\N
polyethylene glycol monomethyl ether 550	23735	\N
polyethylene glycol monomethyl ether 2000	23740	\N
polyethylene glycol monomethyl ether 5000	23745	\N
polyethyleneimine	23750	\N
polypropylene glycol P400	23755	\N
polymixin B sulphate	23760	\N
polyoxyadenylic acid,potassium salt	23765	\N
poly(vinyl alcohol)	23770	\N
polyvinylpyrrolidone K15	23775	\N
POPC	23780	\N
POPSO	23785	\N
potassium acetate	23790	\N
potassium bromide	23795	\N
potassium chloride	23800	\N
potassium citrate	23805	\N
tripotassium citrate	23810	\N
potassium cyanate	23815	\N
potassium dihydrogen phosphate	23820	\N
dipotassium hydrogen phosphate	23825	\N
potassium phosphate dibasic trihydrate	23830	\N
potassium fluoride	23835	\N
potassium formate	23840	\N
potassium hexachloroiridate(IV)	23845	\N
potassium hexachloroplatinate(IV)	23850	\N
potassium hydroxide	23855	\N
potassium permanganate	23860	\N
potassium iodide	23865	\N
potassium nitrate	23870	\N
potassium sodium tartrate tetrahydrate	23875	\N
potassium sodium tartrate	23880	\N
potassium sulphate	23885	\N
dipotassium tartrate	23890	\N
potassium tetrachloroaurate(III)	23895	\N
potassium tetrachloroplatinate(II)	23900	\N
potassium tetracyanoplatinate(II)	23905	\N
dipotassium tetraraiodomercurate(II)	23910	\N
potassium tetranitroplatinate(II)	23915	\N
potassium thiocyanate	23920	\N
L-proline	23925	\N
2-propanol	23930	\N
1,2-propanediol	23935	\N
1,3-propanediol	23940	\N
propionitrile	23945	\N
pyridine	23950	\N
protamine sulphate	23955	\N
pyridoxal hydrochloride	23960	\N
D-(+)-raffinose	23965	\N
riboflavin	23970	\N
rubidium bromide	23975	\N
rubidium chloride	23980	\N
silver nitrate	23985	\N
sodium acetate	23990	\N
sodium acetate trihydrate	23995	\N
sodium azide	24000	\N
sodium bromide	24005	\N
sodium cacodylate hydrate	24010	\N
sodium cacodylate trihydrate	24015	\N
sodium carbonate (anhydrous)	24020	\N
sodium chloride	24025	\N
sodium citrate	24030	\N
sodium citrate monobasic	24035	\N
sodium deoxycholate	24040	\N
sodium deoxycholate monohydrate	24045	\N
sodium dodecyl sulphate	24050	\N
sodium fluoride	24055	\N
sodium formate	24060	\N
sodium n-hexadecyl sulphate	24065	\N
sodium hydrogen carbonate	24070	\N
sodium dihydrogen phosphate	24075	\N
disodium hydrogen phosphate	24080	\N
sodium phosphate dibasic	24085	\N
sodium hydroxide	24090	\N
sodium iodide	24095	\N
sodium malonate dibasic	24100	\N
sodium malonate	24105	\N
sodium meta periodate	24110	\N
sodium nitrate	24115	\N
sodium nitrite	24120	\N
sodium perchlorate	24125	\N
sodium phosphate	24130	\N
sodium propionate	24135	\N
sodium succinate	24140	\N
sodium sulphate	24145	\N
sodium sulphate decahydrate	24150	\N
disodium tartrate	24155	\N
sodium tetraborate	24160	\N
sodium thiocyanate	24165	\N
sodium thiosulphate pentahydrate	24170	\N
sodium trichloroacetate	24175	\N
SOPC	24180	\N
sorbitol	24185	\N
spermidine	24190	\N
spermidine trihydrochloride	24195	\N
spermine tetrahydrochloride	24200	\N
streptomycin sulphate	24205	\N
strontium dichloride	24210	\N
succinic acid	24215	\N
succinic anhydride	24220	\N
succinimide	24225	\N
sucrose	24230	\N
Sulfo-EGS	24235	\N
sulphuric acid	24240	\N
TABS	24245	\N
tacsimate	24250	\N
TAPS	24255	\N
TAPSO	24260	\N
taurine	24265	\N
TEMED	24270	\N
TES	24275	\N
tetracycline hydrochloride	24280	\N
tetraethylene glycol monooctyl ether	24285	\N
tetradecyl-N-N-dimethyl-3-ammonio-1-propanesulfonate	24290	\N
tetrahydrofurane	24295	\N
3,4,5,6-tetrahydrophthalic-anhydride	24300	\N
tetrakis (acetoxymercury) methane	24305	\N
tetramethyl ammonium chloride	24310	\N
thimerosal	24315	\N
thymol	24320	\N
TLCK	24325	\N
TPCK	24330	\N
TPEN	24335	\N
trehalose	24340	\N
trichloroacetic acid	24345	\N
tricine	24350	\N
triethanolamine	24355	\N
triethylene glycol	24360	\N
2,2,2-trifluoroethanol	24365	\N
trimethylamine hydrochloride	24370	\N
trimethylamine N-oxide	24375	\N
trimethyl ammonium chloride	24380	\N
trimethyllead acetate	24385	\N
2,3,5 triphenyl tetrazolium chloride	24390	\N
TRIS	24395	\N
TRIS HCl	24400	\N
tris (2,2' bipyridyl)dichloro ruthenium(11) hexahydrate	24405	\N
t-octylphenoxypolyethoxyethanol	24410	\N
polyethylene glycol tert-octylphenyl ether	24415	\N
trypsin-chymotrypsin inhibitor (Soy bean)	24420	\N
trypsin inhibitor (lima bean)	24425	\N
polyoxyethylene sorbitan monolaurate	24430	\N
urea	24435	\N
vancomycin hydrochloride	24440	\N
vitamin B12	24445	\N
X-GAL	24450	\N
xylene cyanol FF	24455	\N
xylenol orange tetrasodium salt	24460	\N
xylitol	24465	\N
ytterbium(III) chloride hexahydrate	24470	\N
yttrium(III) chloride hexahydrate	24475	\N
yttrium(III) chloride	24480	\N
zinc acetate dihydrate	24485	\N
zinc acetate	24490	\N
zinc chloride	24495	\N
zinc sulphate heptahydrate	24500	\N
zinc sulphate	24505	\N
\.


--
-- TOC entry 2853 (class 0 OID 63134)
-- Dependencies: 1763
-- Data for Name: mole_compca2components; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_compca2components (categoryid, componentid) FROM stdin;
12001	22001
12001	22006
12023	22006
12033	22011
12033	22016
12001	22021
12023	22021
12075	22026
12019	22031
12019	22036
12069	22041
12069	22046
12049	22046
12049	22051
12081	22056
12049	22061
12049	22066
12047	22071
12049	22076
12047	22076
12001	22081
12029	22086
12049	22091
12031	22091
12025	22091
12031	22096
12049	22101
12031	22101
12049	22106
12031	22106
12025	22106
12031	22111
12025	22111
12001	22116
12031	22121
12031	22126
12025	22126
12031	22131
12031	22136
12049	22141
12031	22141
12025	22141
12031	22146
12053	22151
12031	22156
12025	22156
12031	22161
12025	22161
12031	22166
12025	22166
12049	22171
12049	22176
12049	22181
12051	22186
12001	22191
12075	22196
12075	22201
12081	22206
12083	22211
12081	22216
12081	22221
12047	22226
12071	22226
12047	22231
12031	22236
12049	22241
12031	22246
12031	22251
12049	22256
12047	22256
12049	22261
12031	22266
12071	22271
12001	22276
12049	22281
12049	22286
12049	22288
12047	22293
12001	22298
12009	22303
12083	22308
12063	22313
12047	22318
12001	22323
12001	22328
12001	22333
12023	22338
12075	22343
12075	22348
12075	22353
12047	22358
12009	22363
12019	22368
12049	22368
12033	22373
12019	22373
12049	22378
12025	22378
12005	22383
12057	22388
12033	22393
12049	22393
12049	22398
12025	22398
12049	22403
12047	22408
12033	22413
12019	22413
12009	22418
12001	22423
12031	22428
12031	22433
12031	22438
12031	22443
12031	22448
12031	22453
12031	22458
12049	22463
12031	22463
12031	22468
12031	22473
12031	22478
12031	22483
12031	22488
12049	22493
12001	22498
12001	22503
12051	22508
12031	22513
12049	22518
12031	22518
12031	22523
12031	22528
12031	22533
12009	22538
12009	22543
12001	22548
12029	22553
12051	22558
12033	22563
12065	22568
12075	22573
12061	22578
12009	22583
12023	22588
12049	22593
12031	22593
12049	22598
12031	22598
12031	22603
12031	22608
12049	22613
12031	22613
12031	22618
12031	22623
12065	22628
12049	22633
12081	22633
12069	22638
12063	22643
12019	22648
12065	22648
12009	22653
12009	22658
12009	22663
12069	22668
12069	22673
12069	22678
12069	22683
12049	22688
12019	22693
12049	22693
12019	22698
12049	22698
12019	22703
12013	22708
12013	22713
12049	22718
12063	22723
12009	22728
12061	22733
12009	22738
12009	22743
12009	22748
12009	22753
12047	22758
12033	22763
12063	22768
12033	22773
12049	22773
12057	22778
12033	22783
12049	22783
12003	22788
12003	22793
12001	22798
12063	22803
12049	22808
12063	22808
12027	22808
12061	22813
12009	22818
12061	22823
12061	22828
12033	22833
12049	22833
12009	22838
12009	22843
12061	22848
12049	22853
12049	22858
12073	22858
12033	22863
12049	22863
12025	22863
12049	22865
12075	22870
12049	22875
12003	22875
12049	22880
12003	22880
12025	22885
12049	22890
12049	22895
12049	22900
12005	22900
12049	22905
12003	22905
12001	22910
12063	22915
12013	22920
12049	22925
12063	22930
12049	22935
12073	22935
12049	22940
12073	22940
12071	22945
12033	22950
12023	22955
12047	22960
12049	22965
12073	22965
12049	22970
12073	22970
12027	22975
12027	22980
12081	22985
12049	22990
12081	22990
12071	22995
12057	22995
12005	23000
12001	23005
12049	23010
12049	23015
12049	23020
12077	23020
12069	23025
12069	23030
12001	23035
12001	23040
12001	23045
12001	23050
12049	23055
12009	23060
12025	23065
12049	23070
12031	23070
12025	23070
12049	23075
12019	23080
12025	23080
12049	23085
12057	23085
12019	23090
12049	23090
12009	23095
12023	23100
12033	23105
12047	23110
12047	23115
12003	23120
12001	23125
12047	23130
12063	23130
12047	23135
12063	23135
12075	23140
12049	23145
12031	23145
12049	23150
12031	23150
12049	23155
12031	23155
12031	23160
12073	23165
12045	23165
12033	23170
12025	23170
12025	23175
12025	23180
12051	23185
12049	23190
12073	23190
12009	23195
12031	23200
12013	23200
12031	23205
12013	23205
12047	23210
12031	23215
12031	23220
12049	23225
12031	23230
12025	23230
12031	23235
12025	23235
12031	23240
12031	23245
12031	23250
12031	23255
12049	23260
12031	23260
12049	23265
12049	23270
12049	23275
12031	23275
12049	23280
12031	23280
12031	23285
12031	23290
12031	23295
12031	23300
12031	23305
12025	23305
12031	23310
12025	23310
12031	23315
12031	23320
12049	23325
12031	23325
12025	23325
12049	23330
12031	23330
12025	23330
12049	23335
12031	23335
12025	23335
12049	23340
12031	23340
12025	23340
12049	23345
12031	23345
12025	23345
12049	23350
12031	23350
12049	23355
12031	23355
12049	23360
12031	23360
12049	23365
12031	23365
12023	23370
12031	23370
12023	23375
12049	23380
12073	23380
12049	23385
12073	23385
12049	23390
12073	23390
12049	23395
12073	23395
12057	23400
12027	23405
12013	23410
12013	23415
12013	23420
12001	23425
12001	23430
12033	23435
12049	23435
12025	23435
12049	23440
12013	23445
12025	23450
12001	23455
12001	23460
12001	23465
12049	23470
12073	23470
12055	23475
12055	23480
12063	23485
12049	23490
12031	23490
12049	23495
12031	23495
12049	23500
12031	23500
12031	23505
12023	23510
12009	23515
12009	23520
12009	23525
12009	23530
12009	23535
12049	23540
12009	23540
12009	23545
12009	23550
12001	23555
12023	23555
12005	23560
12047	23565
12063	23565
12013	23565
12047	23570
12063	23570
12013	23570
12075	23575
12025	23580
12025	23585
12009	23590
12009	23595
12019	23600
12049	23600
12047	23605
12047	23610
12003	23610
12049	23615
12077	23615
12075	23620
12063	23625
12013	23630
12061	23635
12001	23640
12047	23645
12025	23650
12025	23655
12025	23660
12025	23665
12025	23670
12025	23675
12025	23680
12025	23685
12025	23690
12025	23695
12025	23700
12025	23705
12025	23710
12025	23715
12025	23720
12025	23725
12025	23730
12025	23735
12025	23740
12025	23745
12025	23750
12009	23750
12003	23750
12049	23755
12025	23755
12051	23760
12079	23765
12049	23770
12049	23775
12025	23775
12061	23780
12001	23785
12031	23790
12031	23795
12049	23800
12031	23800
12025	23800
12031	23805
12031	23810
12049	23815
12049	23820
12031	23820
12025	23820
12049	23825
12031	23825
12025	23825
12031	23830
12025	23830
12031	23835
12031	23840
12013	23845
12013	23850
12023	23855
12031	23860
12049	23865
12031	23865
12049	23870
12031	23870
12031	23875
12025	23875
12031	23880
12025	23880
12031	23885
12031	23890
12013	23895
12013	23900
12013	23905
12013	23910
12013	23915
12049	23920
12031	23920
12025	23920
12081	23925
12033	23930
12025	23930
12025	23935
12005	23935
12019	23940
12049	23940
12049	23945
12049	23950
12025	23955
12063	23960
12049	23965
12073	23965
12083	23970
12049	23975
12031	23975
12049	23980
12031	23980
12013	23985
12031	23990
12025	23990
12001	23995
12049	23995
12031	23995
12047	24000
12071	24000
12049	24005
12031	24005
12049	24010
12031	24010
12001	24015
12001	24020
12049	24025
12031	24025
12025	24025
12001	24030
12049	24030
12031	24030
12025	24030
12001	24035
12049	24035
12031	24035
12025	24035
12009	24040
12009	24045
12009	24050
12049	24055
12031	24055
12033	24060
12031	24060
12025	24060
12009	24065
12031	24070
12001	24075
12031	24075
12025	24075
12001	24080
12031	24080
12001	24085
12031	24085
12023	24090
12049	24095
12031	24095
12049	24100
12031	24100
12025	24100
12049	24105
12031	24105
12025	24105
12063	24110
12031	24115
12031	24120
12049	24125
12031	24125
12031	24130
12001	24135
12001	24140
12025	24140
12025	24145
12031	24150
12031	24155
12031	24160
12049	24165
12049	24170
12049	24175
12077	24175
12061	24180
12049	24185
12005	24185
12073	24185
12049	24190
12047	24190
12025	24190
12047	24195
12025	24195
12049	24200
12025	24200
12051	24205
12049	24210
12031	24210
12023	24215
12031	24215
12025	24215
12063	24220
12063	24225
12049	24230
12073	24230
12063	24235
12023	24240
12001	24245
12001	24250
12023	24250
12031	24250
12025	24250
12001	24255
12001	24260
12049	24265
12059	24265
12053	24270
12001	24275
12051	24280
12009	24285
12009	24290
12033	24295
12049	24295
12063	24300
12013	24305
12049	24310
12071	24315
12013	24315
12049	24320
12047	24325
12047	24330
12003	24335
12049	24340
12073	24340
12025	24345
12077	24345
12001	24350
12033	24355
12025	24360
12033	24365
12031	24370
12049	24375
12031	24375
12063	24375
12049	24380
12013	24385
12075	24390
12001	24395
12001	24400
12053	24405
12009	24410
12009	24415
12047	24420
12047	24425
12009	24430
12049	24435
12077	24435
12051	24440
12083	24445
12065	24450
12075	24455
12075	24460
12049	24465
12005	24465
12013	24470
12049	24475
12049	24480
12031	24485
12049	24490
12031	24490
12049	24495
12031	24500
12049	24505
12031	24505
\.


--
-- TOC entry 2854 (class 0 OID 63137)
-- Dependencies: 1764
-- Data for Name: mole_construct; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_construct (constructstatus, function, markerdetails, promoterdetails, resistancedetails, sequencetype, moleculeid) FROM stdin;
\.


--
-- TOC entry 2855 (class 0 OID 63143)
-- Dependencies: 1765
-- Data for Name: mole_molecule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_molecule (casnum, empiricalformula, isvolatile, moltype, molecularmass, seqdetails, seqstring, abstractcomponentid) FROM stdin;
7365-82-4	H2NCOCH2NHCH2CH2SO3H	\N	other	182.19999999999999	\N	\N	22001
64-19-7	CH3COOH	\N	other	60.052979999999998	\N	\N	22006
67-64-1	CH3COCH3	\N	other	58.080669999999998	\N	\N	22011
75-05-8	CH3CN	\N	other	41.052909999999997	\N	\N	22016
26239-55-4	H2NCOCH2N(CH2CO2H)2	\N	other	190.15000000000001	\N	\N	22021
10127-02-3	C17H20N3Cl 0.5(ZnCl2)	\N	other	369.95999999999998	\N	\N	22026
79-06-1	C3H5NO	\N	other	71.079999999999998	\N	\N	22031
868-63-3	C8H12N2O4	\N	other	200.19	\N	\N	22036
58-61-7	C10H13N5O4	\N	other	267.24000000000001	\N	\N	22041
987-65-5	C10H14N5Na2O13P3.xH2O	\N	other	551.13999999999999	\N	\N	22046
488-81-3	C5H12O5	\N	other	152.15000000000001	\N	\N	22051
56-41-7	C3H7NO2	\N	other	89.090000000000003	\N	\N	22056
9048-46-8	\N	\N	other	66000	\N	\N	22061
7446-70-0	AlCl3	\N	other	133.34	\N	\N	22066
30827-99-7	C8H10NO2SF.HCl	\N	other	239.5	\N	\N	22071
60-32-2	NH2(CH2)5COOH	\N	other	131.16999999999999	\N	\N	22076
115-69-5	(HOCH2)2C(NH2)CH3	\N	other	105.14	\N	\N	22081
2835-99-6	H2NC6H3(CH3)OH	\N	other	123.15000000000001	\N	\N	22086
631-61-8	CH3COONH4	\N	other	77.079999999999998	\N	\N	22091
1066-33-7	NH4HCO3	\N	other	79.060000000000002	\N	\N	22096
12124-97-9	NH4Br	\N	other	97.939999999999998	\N	\N	22101
12125-02-9	NH4Cl	\N	other	53.490000000000002	\N	\N	22106
3012-65-5	HOC(CO2H)(CH2CO2NH4)2	\N	other	226.18000000000001	\N	\N	22111
3458-72-8	HOC(COONH4)(CH2COONH4)2	\N	other	243.22	\N	\N	22116
12125-01-8	HNH4F	\N	other	37.039999999999999	\N	\N	22121
540-69-2	CH5NO2	\N	other	63.060000000000002	\N	\N	22126
12027-06-4	NH4I	\N	other	144.94	\N	\N	22131
7783-85-9	(NH4)2Fe(SO4)2.6H2O	\N	other	392.13999999999999	\N	\N	22136
6484-52-2	NH4NO3	\N	other	80.040000000000006	\N	\N	22141
6009-70-7	(COONH4)2.H2O	\N	other	142.11000000000001	\N	\N	22146
7727-54-0	H8N2O8S2	\N	other	228.19999999999999	\N	\N	22151
7783-28-0	(NH4)2HPO4	\N	other	132.06	\N	\N	22156
7722-76-1	NH4H2PO4	\N	other	115.03	\N	\N	22161
7783-20-2	(NH4)2SO4	\N	other	132.13999999999999	\N	\N	22166
3164-29-2	(NH4)2C4H4O6	\N	other	184.15000000000001	\N	\N	22171
3336-58-1	CF3COONH4	\N	other	131.05000000000001	\N	\N	22176
1762-95-4	NH4SCN	\N	other	76.120000000000005	\N	\N	22181
69-52-3	C16H18N3NaO4S	\N	other	371.39999999999998	\N	\N	22186
68399-79-1	C7H17NO5S	\N	other	227.28	\N	\N	22191
62-53-3	C6H7N	\N	other	93.129999999999995	\N	\N	22196
28836-03-5	C16H16N2O3S	\N	other	316.37	\N	\N	22201
74-79-3	C6H14N4O2	\N	other	174.19999999999999	\N	\N	22206
58-81-7	C6H8O6	\N	other	176.12	\N	\N	22211
70-47-3	C4H8N2O3	\N	other	132.09999999999999	\N	\N	22216
56-84-8	C4H7NO4	\N	other	133.09999999999999	\N	\N	22221
123-99-9	HO2C(CH2)7CO2H	\N	other	128.22	\N	\N	22226
57-44-3	C8H12N2O3	\N	other	184.19	\N	\N	22231
543-80-6	(CH3COO)2Ba	\N	other	255.40000000000001	\N	\N	22236
106361-37-2	BaCl2	\N	other	208.22999999999999	\N	\N	22241
10022-31-8	Ba(NO3)2	\N	other	261.33999999999997	\N	\N	22246
304676-17-3	C2BaN2S2.xH2O	\N	other	253.49000000000001	\N	\N	22251
206752-36-5	C6H5C(=NH)NH2.HCl.xH2O	\N	other	156.61000000000001	\N	\N	22256
1669-14-0	C6H5C(=NH)NH2.HCl	\N	other	157.61000000000001	\N	\N	22261
98-11-3	C6H6O3S	\N	other	158.18000000000001	\N	\N	22266
65-85-0	C6H5COOH	\N	other	122.12	\N	\N	22271
10191-18-1	C6H15NO5S	\N	other	213.30000000000001	\N	\N	22276
7787-56-6	BeSO4.4H2O	\N	other	177.13999999999999	\N	\N	22281
107-43-7	(CH3)3N+CH2COO	\N	other	117.15000000000001	\N	\N	22286
17146-86-0	C5H11NO2.H2O	\N	other	135.16	\N	\N	22288
65391-42-6	C16H24N2O4.HCl	\N	other	344.82999999999998	\N	\N	22293
150-25-4	C6H13NO4	\N	other	163.16999999999999	\N	\N	22298
86303-22-2	C42H75N3O16	\N	other	878.10000000000002	\N	\N	22303
58-85-5	C10H16N2O3S	\N	other	244.31	\N	\N	22308
\N	C20H29N5O5S	\N	other	451.54000000000002	\N	\N	22313
143-23-7	NH2(CH2)6NH(CH2)6NH2	\N	other	215.38	\N	\N	22318
6976-37-0	C8H19NO5	\N	other	209.24000000000001	\N	\N	22323
124763-51-5	C8H19NO5.HCl	\N	other	245.69999999999999	\N	\N	22328
64431-96-5	CH2[CH2NHC(CH2OH)3]2	\N	other	282.32999999999998	\N	\N	22333
10043-35-3	BH3O3	\N	other	61.829999999999998	\N	\N	22338
6104-58-1	C47H48N3O7S2Na	\N	other	854	\N	\N	22343
6104-59-2	\N	\N	other	\N	\N	\N	22348
62625-28-9	C19H9Br4NaO5S	\N	other	691.94000000000005	\N	\N	22353
99-81-0	C8H6BrNO3	\N	other	244.03999999999999	\N	\N	22358
9002-92-0	\N	\N	other	1198	\N	\N	22363
584-03-2	CH3CH2CH(OH)CH2OH	\N	other	90.120000000000005	\N	\N	22368
107-88-0	CH3CH(OH)CH2CH2OH	\N	other	90.120000000000005	\N	\N	22373
110-63-4	HO(CH2)4OH	\N	other	90.120000000000005	\N	\N	22378
513-85-9	CH3CH(OH)CH(OH)CH3	\N	other	90.120000000000005	\N	\N	22383
2425-79-8	C10H18O4	\N	other	202.25	\N	\N	22388
71-36-3	CH3(CH2)3OH	\N	other	74.120000000000005	\N	\N	22393
75-65-0	(CH3)3COH	\N	other	74.120000000000005	\N	\N	22398
1634-04-4	(CH3)3COCH3	\N	other	88.150000000000006	\N	\N	22403
592-84-7	HCOO(CH2)3CH3	\N	other	102.13	\N	\N	22408
96-48-0	C4H6O2	\N	other	69.090000000000003	\N	\N	22413
3055-98-9	C28H58O9	\N	other	538.75	\N	\N	22418
161308-34-5	C10H21NO3S	\N	other	235.34	\N	\N	22423
89759-80-8	Cd(CH3COO)2	\N	other	230.49000000000001	\N	\N	22428
7789-42-6	CdBr2	\N	other	272.22000000000003	\N	\N	22433
10108-64-2	CdCl2	\N	other	183.31999999999999	\N	\N	22438
72589-96-9	CdCl2.2H2O	\N	other	219.34999999999999	\N	\N	22443
7790-80-9	CdI2	\N	other	366.22000000000003	\N	\N	22448
10124-36-4	CdSO4	\N	other	208.47	\N	\N	22453
7790-84-3	(CdSO4)3.8H2O	\N	other	769.53999999999996	\N	\N	22458
62-54-4	(CH3CO2)2Ca	\N	other	158.16999999999999	\N	\N	22463
471-34-1	CaCO3	\N	other	100.09	\N	\N	22468
10043-52-4	CaCl2	\N	other	110.98	\N	\N	22473
10035-04-8	CaCl2.2H2O	\N	other	147.00999999999999	\N	\N	22478
7774-34-7	CaCl2.6H2O	\N	other	219.08000000000001	\N	\N	22483
7778-18-9	CaSO4	\N	other	136.13999999999999	\N	\N	22488
105-60-2	C6H11NO	\N	other	113.16	\N	\N	22493
1135-40-6	C6H11NH(CH2)3SO3H	\N	other	221.31999999999999	\N	\N	22498
76463-39-5	C9H19NO4S	\N	other	237.31999999999999	\N	\N	22503
4800-94-6	C17H16N2Na2O6S	\N	other	422.36000000000001	\N	\N	22508
7787-69-1	CsBr	\N	other	212.81	\N	\N	22513
7647-17-8	CsCl	\N	other	168.36000000000001	\N	\N	22518
7789-17-5	CsI	\N	other	259.81	\N	\N	22523
7789-18-6	CsNO3	\N	other	194.91	\N	\N	22528
10294-54-9	Cs2SO4	\N	other	361.87	\N	\N	22533
112-02-7	C19H42ClN	\N	other	320	\N	\N	22538
75621-03-3	C32H58N2O7S	\N	other	614.89999999999998	\N	\N	22543
103-47-9	C8H17NO3S	\N	other	207.28999999999999	\N	\N	22548
127-65-1	C7H7ClNNaO2S.3H2O	\N	other	227.63999999999999	\N	\N	22553
56-75-7	C11H12Cl2N2O5	\N	other	323.13	\N	\N	22558
67-66-3	CHCl3	\N	other	119.38	\N	\N	22563
604-44-4	C10H7ClO	\N	other	178.61000000000001	\N	\N	22568
10199-89-0	C6H2ClN3O3	\N	other	199.55000000000001	\N	\N	22573
57-88-5	C27H46O	\N	other	386.64999999999998	\N	\N	22578
81-25-4	C24H40O5	\N	other	408.56999999999999	\N	\N	22583
77-92-9	HOC(COOH)(CH2COOH)2	\N	other	192.12	\N	\N	22588
7646-79-9	CbCl2	\N	other	129.84	\N	\N	22593
7791-13-1	CoCl26H2O	\N	other	273.93000000000001	\N	\N	22598
7787-70-4	CuBr	\N	other	143.44999999999999	\N	\N	22603
7758-89-6	CuCl	\N	other	99	\N	\N	22608
7447-39-4	CuCl2	\N	other	134.44999999999999	\N	\N	22613
10125-13-0	CuCl2. 2H2O	\N	other	170.47999999999999	\N	\N	22618
7758-99-8	CuO4S.5H2O	\N	other	249.69	\N	\N	22623
57-00-1	H2NC(=NH)N(CH3)CH2CO2H	\N	other	131.13	\N	\N	22628
52-90-4	C3H7NO2S	\N	other	121.16	\N	\N	22633
81012-87-5	C9H14N3Na2O14P3	\N	other	529.13999999999999	\N	\N	22638
506-68-3	CNBr	\N	other	105.92	\N	\N	22643
930-68-7	C6H8O	\N	other	96.129999999999995	\N	\N	22648
85261-20-7	C17H35NO6	\N	other	349.45999999999998	\N	\N	22653
15163-36-7	C15H33NO3S	\N	other	307.60000000000002	\N	\N	22658
302-95-4	C24H39NaO4	\N	other	414.55000000000001	\N	\N	22663
1927-31-7	C10H14N5Na2O12P3	\N	other	535.14999999999998	\N	\N	22668
102783-51-7	C9H14N3Na2O13P3	\N	other	511.12	\N	\N	22673
93919-41-6	C10H14N5Na3O13P3	\N	other	573.13	\N	\N	22678
18423-43-3	C10H16N2O14P3Na	\N	other	504.14999999999998	\N	\N	22683
9011-18-1	\N	\N	other	50000	\N	\N	22688
124-09-4	NH2(CH2)6NH2	\N	other	116.2	\N	\N	22693
373-44-4	NH2(CH2)8NH2	\N	other	144.25999999999999	\N	\N	22698
462-94-2	NH2(CH2)5NH2	\N	other	102.18000000000001	\N	\N	22703
109998-76-7	C4H16I2N6O6Pt2	\N	other	888.20000000000005	\N	\N	22708
14096-51-6	C2H6Cl2N2Pt	\N	other	324.06999999999999	\N	\N	22713
75-09-2	CH2Cl2	\N	other	84.930000000000007	\N	\N	22718
538-75-0	C13H22N2	\N	other	206.33000000000001	\N	\N	22723
69227-93-6	C24H46O11	\N	other	510.45999999999998	\N	\N	22728
39036-04-9	C22H44NO8P	\N	other	481.56	\N	\N	22733
2605-79-0	CH3(CH2)9N(O)(CH3)2	\N	other	201.34999999999999	\N	\N	22738
1643-20-5	CH3(CH2)11N(O)(CH3)2	\N	other	229.40000000000001	\N	\N	22743
15178-76-4	C13H29NO3S	\N	other	279.44	\N	\N	22748
2281-11-0	C21H45NO3S	\N	other	391.64999999999998	\N	\N	22753
20624-25-3	C5H10NNaS2.3H2O	\N	other	225.31	\N	\N	22758
60-29-7	C4H10O	\N	other	74.120000000000005	\N	\N	22763
1609-47-8	C6H10O5	\N	other	162.13999999999999	\N	\N	22768
68-12-2	HCON(CH3)2	\N	other	73.090000000000003	\N	\N	22773
34490-86-3	C10H20N2O2.2HCl	\N	other	273.19999999999999	\N	\N	22778
123-91-1	C4H8O2	\N	other	88.109999999999999	\N	\N	22783
60-10-6	C13H12N4S	\N	other	256.32999999999998	\N	\N	22788
499-83-2	C7H5NO4	\N	other	167.12	\N	\N	22793
68399-80-4	C7H17NO6S	\N	other	261.27999999999997	\N	\N	22798
69-78-3	C14H8N2O8S2	\N	other	396.35000000000002	\N	\N	22803
03/12/3483	C4H10O2S2	\N	other	154.25	\N	\N	22808
18194-25-7	C32H44NO8P	\N	other	657.86000000000001	\N	\N	22813
82494-09-5	C22H42O11	\N	other	482.56	\N	\N	22818
18194-24-6	C36H72NO8P	\N	other	677.92999999999995	\N	\N	22823
56421-10-4	C42H78NaO10P	\N	other	797.02999999999997	\N	\N	22828
67-68-5	(CH3)2SO	\N	other	78.129999999999995	\N	\N	22833
59122-55-3	C18H36O6	\N	other	348.47000000000003	\N	\N	22838
14933-08-5	C17H37NO3S	\N	other	335.55000000000001	\N	\N	22843
4235-95-4	C44H84NO8P	\N	other	786.11000000000001	\N	\N	22848
96702-03-3	C6H10N2O2	\N	other	142.16	\N	\N	22853
149-32-6	HOCH2[CH(OH)]2CH2OH	\N	other	122.12	\N	\N	22858
64-17-5	C2H5OH	\N	other	46.07	\N	\N	22863
141-43-5	C2H7NO	\N	other	61.079999999999998	\N	\N	22865
1239-45-8	C21H20BrN	\N	other	394.31	\N	\N	22870
60-00-4	C10H16N2O8	\N	other	292.24000000000001	\N	\N	22875
6381-92-6	C10H14N2O8Na2.2H2O	\N	other	372.24000000000001	\N	\N	22880
110-80-5	C2H5OCH2CH2OH	\N	other	90.120000000000005	\N	\N	22885
141-78-6	C4H8O2	\N	other	88.109999999999999	\N	\N	22890
107-15-3	NH2CH2CH2NH2	\N	other	60.100000000000001	\N	\N	22895
107-21-1	C2H6O2	\N	other	62.07	\N	\N	22900
67-42-5	[-CH2OCH2CH2N(CH2CO2H)2]2	\N	other	380.35000000000002	\N	\N	22905
16052-06-5	C9H20N2O4S	\N	other	252.33000000000001	\N	\N	22910
128-53-0	C6H7NO2	\N	other	125.13	\N	\N	22915
2235-25-8	C2H7ClHgO2	\N	other	326.5	\N	\N	22920
78-93-3	C4H8O	\N	other	72.109999999999999	\N	\N	22925
4156-16-5	C11H11NO4S	\N	other	253.27000000000001	\N	\N	22930
57-48-7	C6H12O6	\N	other	180.16	\N	\N	22935
7776-48-9	C6H12O6	\N	other	180.16	\N	\N	22940
50-00-0	CH2O	\N	other	30.030000000000001	\N	\N	22945
75-12-7	HCONH2	\N	other	45.039999999999999	\N	\N	22950
64-18-6	HCOOH	\N	other	46.030000000000001	\N	\N	22955
536-69-6	C10H13NO2	\N	other	179.22	\N	\N	22960
50-99-7	C6H12O6	\N	other	180.16	\N	\N	22965
921-60-8	C6H12O6	\N	other	180.16	\N	\N	22970
27025-41-8	C20H32N6O12S2	\N	other	612.63	\N	\N	22975
70-18-8	C10H17N3O6S	\N	other	307.31999999999999	\N	\N	22980
56-86-0	C5H9NO4	\N	other	147.13	\N	\N	22985
5959-95-5	C5H10N2O3	\N	other	146.13999999999999	\N	\N	22990
111-30-8	OCH(CH2)3CHO	\N	other	100.12	\N	\N	22995
56-81-5	HOCH2CH(OH)CH2OH	\N	other	92.090000000000003	\N	\N	23000
56-40-6	NH2CH2COOH	\N	other	75.069999999999993	\N	\N	23005
\N	(CD3)3N(Cl)CH2CO2H	\N	other	162.66	\N	\N	23010
556-33-2	C6H11N3O4	\N	other	189.16999999999999	\N	\N	23015
50-01-1	NH2C(=NH)NH2.HCl	\N	other	95.530000000000001	\N	\N	23020
43139-22-6	C10H15N5O11P2	\N	other	443.19999999999999	\N	\N	23025
56001-37-7	C10H14N5Na2O14P3.xH2O	\N	other	567.13999999999999	\N	\N	23030
161308-36-7	C10H22N2O4S	\N	other	266.36000000000001	\N	\N	23035
7365-45-9	C8H18N2O4S	\N	other	238.30000000000001	\N	\N	23040
75277-39-3	C8H17N2NaO4S	\N	other	260.29000000000002	\N	\N	23045
68399-78-0	C9H20N2O5S	\N	other	268.30000000000001	\N	\N	23050
103404-57-5	CH3(CH2)3CH(OH)CH(OH)CH2OH	\N	other	148.19999999999999	\N	\N	23055
78617-12-6	C13H26O6	\N	other	278.33999999999997	\N	\N	23060
10534-89-1	Co(NH3)6Cl3	\N	other	267.48000000000002	\N	\N	23065
57-09-0	CH3(CH2)15N(Br)(CH3)3	\N	other	364.44999999999999	\N	\N	23070
920-66-1	(CF3)2CHOH	\N	other	168.03999999999999	\N	\N	23075
629-11-8	HO(CH2)6OH	\N	other	118.17	\N	\N	23080
2935-44-6	CH3CH(OH)CH2CH2CH(OH)CH3	\N	other	118.17	\N	\N	23085
25323-24-4	CH3CH2CH2CH(OH)CH(OH)CH2OH	\N	other	134.16999999999999	\N	\N	23090
59080-45-4	C12H24O6	\N	other	264.31999999999999	\N	\N	23095
7647-01-0	HCl	\N	other	36.460000000000001	\N	\N	23100
513-86-0	C4H8O2	\N	other	88.109999999999999	\N	\N	23105
01/11/5470	H3NOHCl	\N	other	69.489999999999995	\N	\N	23110
97-51-8	C7H5NO4	\N	other	167.12	\N	\N	23115
594-61-6	C4H8O3	\N	other	104.09999999999999	\N	\N	23120
288-32-4	C3H4N2	\N	other	68.079999999999998	\N	\N	23125
144-48-9	C2H4INO	\N	other	184.964	\N	\N	23130
64-69-7	C2H3IO2	\N	other	185.94800000000001	\N	\N	23135
36930-63-9	C14H15IN2O4S	\N	other	434.25	\N	\N	23140
10025-77-1	FeCl3.6H2O	\N	other	270.30000000000001	\N	\N	23145
10025-77-1	FeCl3.6H2O	\N	other	270.30000000000001	\N	\N	23150
13478-10-9	Cl2Fe.4H2O	\N	other	198.81	\N	\N	23155
7720-78-7	FeSO4	\N	other	151.91	\N	\N	23160
367-93-1	C9H18O5S	\N	other	238.30000000000001	\N	\N	23165
67-63-0	C3H8O	\N	other	60.100000000000001	\N	\N	23170
77110-54-4	CH3OCH2CH2O[CH(CH3)CH2O]nCH2CH(NH2)CH3	\N	other	\N	\N	\N	23175
65605-36-9	CH3CH(NH2)CH2[OCH(CH3)CH2]l(OCH2CH2)m[OCH2CH(CH3)]nNH2	\N	other	\N	\N	\N	23180
25389-94-0	C18H36N4O11.H2O4S	\N	other	582.58000000000004	\N	\N	23185
63-42-3	C12H22O11	\N	other	342.30000000000001	\N	\N	23190
137-16-6	C15H28NNaO3	\N	other	293.38	\N	\N	23195
6080-56-4	Pb(CH3CO2)2.3H2O	\N	other	379.32999999999998	\N	\N	23200
301-04-2	Pb(O2C2H3)2	\N	other	325.28699999999998	\N	\N	23205
147385-61-3	C20H38N6O4	\N	other	426.55000000000001	\N	\N	23210
546-89-4	LiOOCCH3	\N	other	65.989999999999995	\N	\N	23215
6108-17-4	LiOOCCH3.2H2O	\N	other	102.02	\N	\N	23220
7550-35-8	LiBr	\N	other	86.849999999999994	\N	\N	23225
7447-41-8	LiCl	\N	other	42.390000000000001	\N	\N	23230
85144-11-2	ClLi .H2O	\N	other	42.390000000000001	\N	\N	23235
919-16-4	C6H5Li3O7	\N	other	209.923	\N	\N	23240
7789-24-4	LiF	\N	other	25.940000000000001	\N	\N	23245
556-63-8	LiCHOO	\N	other	51.960000000000001	\N	\N	23250
6080-58-6	C6H7LiO7	\N	other	281.98599999999999	\N	\N	23255
7790-69-4	LiNO3	\N	other	68.950000000000003	\N	\N	23260
09/03/7791	LiClO4	\N	other	106.39	\N	\N	23265
552-38-5	HOC6H4CO2Li	\N	other	144.05000000000001	\N	\N	23270
10377-48-7	H3LiO4S	\N	other	109.94	\N	\N	23275
10102-25-7	Li2SO4.H2O	\N	other	127.95999999999999	\N	\N	23280
16674-78-5	(CH3COO)2Mg.4H2O	\N	other	214.44999999999999	\N	\N	23285
7789-48-2	MgBr2	\N	other	184.11000000000001	\N	\N	23290
7786-30-3	MgCl2	\N	other	95.209999999999994	\N	\N	23295
7791-18-6	MgCl2 .6H2O	\N	other	203.30000000000001	\N	\N	23300
557-39-1	C2H2MgO4	\N	other	114.34	\N	\N	23305
6150-82-9	C2H2MgO4.2H2O	\N	other	150.37	\N	\N	23310
10377-60-3	MgN2O6	\N	other	148.315	\N	\N	23315
13446-18-9	Mg(NO3)2.6H2O	\N	other	256.41000000000003	\N	\N	23320
7487-88-9	MgSO4	\N	other	120.37	\N	\N	23325
22189-08-8	MgSO4.xH2O	\N	other	120.37	\N	\N	23330
14168-73-1	MgSO4.1H2O	\N	other	138.38	\N	\N	23335
17830-18-1	MgSO4.6H2O	\N	other	228.46000000000001	\N	\N	23340
10034-99-8	MgSO4.7H2O	\N	other	246.47	\N	\N	23345
05/01/7773	MnCl2	\N	other	125.84	\N	\N	23350
20603-88-7	Cl2H4MnO2	\N	other	161.874	\N	\N	23355
64333-01-3	MnCl2.H2O	\N	other	143.86000000000001	\N	\N	23360
13446-34-9	MnCl2.4H2O	\N	other	197.91	\N	\N	23365
6915-15-7	C4H6O	\N	other	134.09	\N	\N	23370
141-82-2	CH2(COOH)2	\N	other	104.06	\N	\N	23375
69-65-8	C6H14O6	\N	other	182.16999999999999	\N	\N	23380
643-01-6	\N	\N	other	182.16999999999999	\N	\N	23385
3458-28-4	C6H12O6	\N	other	180.16	\N	\N	23390
10030-80-5	C6H12O6	\N	other	180.16	\N	\N	23395
92944-71-3	C17H11NO3	\N	other	277.26999999999998	\N	\N	23400
60-24-2	C2H6OS	\N	other	78.129999999999995	\N	\N	23405
1600-27-7	(CH3COO)2Hg	\N	other	318.68000000000001	\N	\N	23410
7487-94-7	HgCl2	\N	other	271.5	\N	\N	23415
486-67-9	HOHgCH2CH(OCH3)CH2NHCOC6H4OCH2CO2H	\N	other	483.87	\N	\N	23420
4432-31-9	C6H13NO4S	\N	other	195.24000000000001	\N	\N	23425
71119-23-8	C6H12NO4SNa	\N	other	217.22	\N	\N	23430
67-56-1	CH3OH	\N	other	32.039999999999999	\N	\N	23435
79-20-9	CH3COOCH3	\N	other	74.079999999999998	\N	\N	23440
115-09-3	CH3HgCl	\N	other	251.08000000000001	\N	\N	23445
107-41-5	CH3CH(OH)CH2C(CH3)2OH	\N	other	118.17	\N	\N	23450
115724-21-5	C8H17NO4S	\N	other	223.28999999999999	\N	\N	23455
1132-61-2	C7H15NO4S	\N	other	209.25999999999999	\N	\N	23460
68399-77-9	C7H15NO5S	\N	other	225.25999999999999	\N	\N	23465
87-89-8	C6H12O6	\N	other	180.16	\N	\N	23470
1184-16-3	C21H27N7Na2O17P3	\N	other	765.38999999999999	\N	\N	23475
2646-71-1	C21H26N7Na4O17P3	\N	other	833.35000000000002	\N	\N	23480
6066-82-6	C4H5NO3	\N	other	115.09	\N	\N	23485
7718-54-9	NiCl2	\N	other	129.59999999999999	\N	\N	23490
69098-15-3	NiCl2.xH2O	\N	other	129.59999999999999	\N	\N	23495
7791-20-0	NiCl2.6H2O	\N	other	237.69	\N	\N	23500
10101-97-0	NiO4S.6H2O	\N	other	262.85000000000002	\N	\N	23505
7697-37-2	HNO3	\N	other	63.009999999999998	\N	\N	23510
85261-19-4	C16H33NO6	\N	other	335.44	\N	\N	23515
3055-99-0	C30H62O10	\N	other	582.80999999999995	\N	\N	23520
24233-81-6	CH3(CH2)9(OCH2CH2)8OH	\N	other	510.69999999999999	\N	\N	23525
29836-26-8	C14H28O6	\N	other	292.37	\N	\N	23530
85618-21-9	C14H28O5S	\N	other	308.39999999999998	\N	\N	23535
29836-26-8	C14H28O6	\N	other	292.36900000000003	\N	\N	23540
85316-98-9	C15H31NO6	\N	other	321.41000000000003	\N	\N	23545
69984-73-2	C15H30O6	\N	other	306.39999999999998	\N	\N	23550
7664-38-2	HP304	\N	other	98.079999999999998	\N	\N	23555
\N	\N	\N	other	\N	\N	\N	23560
59-85-8	C7H5ClHgO2	\N	other	357.15600000000001	\N	\N	23565
554-77-8	C6H5ClHgO3S	\N	other	393.21100000000001	\N	\N	23570
16593-81-0	C11H8N3NaO2  H2O	\N	other	237.19	\N	\N	23575
30599-15-6	C[CH2(OCH2CH2)nOH]4	\N	other	\N	\N	\N	23580
9051-49-4	C[CH2[OCH2CH(CH3)]nOH]4	\N	other	\N	\N	\N	23585
23244-49-7	C20H42O6	\N	other	378.54000000000002	\N	\N	23590
19327-40-3	C18H38O6	\N	other	350.49000000000001	\N	\N	23595
96-22-0	CH3CH2COCH2CH3	\N	other	86.129999999999995	\N	\N	23600
51724-57-3	C33H61N5O9	\N	other	671.87	\N	\N	23605
90412-47-8	C12N2D8	\N	other	1988.25	\N	\N	23610
108-95-2	C6H5OH	\N	other	94.109999999999999	\N	\N	23615
143-74-8	C19H14O5S	\N	other	354.38	\N	\N	23620
1075-06-5	C6H5COCHO.xH2O	\N	other	134.13	\N	\N	23625
62-38-4	C6H5HgOCOCH3	\N	other	336.74000000000001	\N	\N	23630
8002-43-5	C10H19NO8PR2	\N	other	\N	\N	\N	23635
5625-37-6	C8H18N2O6S2	\N	other	302.37	\N	\N	23640
329-98-6	C7H7FO2S	\N	other	174.19	\N	\N	23645
07/04/9003	\N	\N	other	\N	\N	\N	23650
25322-68-3	H(OCH2CH2)nOH	\N	other	400	\N	\N	23655
25322-68-3	H(OCH2CH2)nOH	\N	other	200	\N	\N	23660
25322-68-3	H(OCH2CH2)nOH	\N	other	300	\N	\N	23665
25322-68-3	H(OCH2CH2)nOH	\N	other	600	\N	\N	23670
25322-68-3	H(OCH2CH2)nOH	\N	other	1000	\N	\N	23675
25322-68-3	H(OCH2CH2)nOH	\N	other	1500	\N	\N	23680
25322-68-3	H(OCH2CH2)nOH	\N	other	2000	\N	\N	23685
25322-68-3	H(OCH2CH2)nOH	\N	other	3000	\N	\N	23690
25322-68-3	H(OCH2CH2)nOH	\N	other	3350	\N	\N	23695
25322-68-3	H(OCH2CH2)nOH	\N	other	3350	\N	\N	23700
25322-68-3	H(OCH2CH2)nOH	\N	other	4000	\N	\N	23705
25322-68-3	H(OCH2CH2)nOH	\N	other	5000	\N	\N	23710
25322-68-3	H(OCH2CH2)nOH	\N	other	6000	\N	\N	23715
25322-68-3	H(OCH2CH2)nOH	\N	other	8000	\N	\N	23720
25322-68-3	H(OCH2CH2)nOH	\N	other	10000	\N	\N	23725
25322-68-3	H(OCH2CH2)nOH	\N	other	20000	\N	\N	23730
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	550	\N	\N	23735
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	2000	\N	\N	23740
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	5000	\N	\N	23745
9002-98-6	(C2H5N)n	\N	other	43.078000000000003	\N	\N	23750
25322-69-4	\N	\N	other	\N	\N	\N	23755
1405-20-5	C55H96N16O13.2H2SO4	\N	other	1385.6099999999999	\N	\N	23760
24937-83-5	\N	\N	other	\N	\N	\N	23765
9002-89-5	[-CH2CHOH-]n	\N	other	\N	\N	\N	23770
9003-39-8	(C6H9NO)n	\N	other	\N	\N	\N	23775
59491-62-2	C42H82NO8P	\N	other	760.08000000000004	\N	\N	23780
68189-43-5	C10H22NO8S2.2H2O	\N	other	398.44999999999999	\N	\N	23785
127-08-2	C2H3KO2	\N	other	98.140000000000001	\N	\N	23790
03/02/7758	Kbr	\N	other	119	\N	\N	23795
7447-40-7	KCl	\N	other	74.549999999999997	\N	\N	23800
866-83-1	KH2C6H5O7	\N	other	230.21000000000001	\N	\N	23805
06/05/6100	HOC(COOK)(CH2COOK)2.H2O	\N	other	324.41000000000003	\N	\N	23810
590-28-3	KOCN	\N	other	81.120000000000005	\N	\N	23815
7778-77-0	KH2PO4	\N	other	136.09	\N	\N	23820
04/11/7758	K2HPO4	\N	other	174.18000000000001	\N	\N	23825
16788-57-1	K2HPO4.3H2O	\N	other	228.22	\N	\N	23830
7789-23-3	KF	\N	other	58.100000000000001	\N	\N	23835
590-29-4	HCOOK	\N	other	84.120000000000005	\N	\N	23840
16920-56-2	K2IrCl6	\N	other	481.14999999999998	\N	\N	23845
16921-30-5	K2PtCl6	\N	other	485.99000000000001	\N	\N	23850
1310-58-3	KOH	\N	other	56.109999999999999	\N	\N	23855
7722-64-7	KMnO4	\N	other	158.03	\N	\N	23860
7681-11-0	KI	\N	other	166.00299999999999	\N	\N	23865
7757-79-1	KNO3	\N	other	101.09999999999999	\N	\N	23870
6381-59-5	KOCOCH(OH)CH(OH)COONa.4H2O	\N	other	282.22000000000003	\N	\N	23875
304-59-6	C4H4KNaO6	\N	other	210.15899999999999	\N	\N	23880
7778-80-5	K2SO4	\N	other	174.25999999999999	\N	\N	23885
921-53-9	K2C4H4O6	\N	other	226.27000000000001	\N	\N	23890
13682-61-6	KauCl4	\N	other	377.88	\N	\N	23895
10025-99-7	K2PtCl4	\N	other	415.08999999999997	\N	\N	23900
562-76-5	K2PtCN4	\N	other	377.33999999999997	\N	\N	23905
7783-33-7	K2Hg(I)4	\N	other	786.39999999999998	\N	\N	23910
13815-39-9	K2Pt(NO2)4	\N	other	459.31	\N	\N	23915
333-20-0	KSCN	\N	other	97.180000000000007	\N	\N	23920
147-85-3	C5H9NO2	\N	other	115.13	\N	\N	23925
67-63-0	CH3CH(OH)CH3	\N	other	60.100000000000001	\N	\N	23930
57-55-6	CH3CH(OH)CH2OH	\N	other	76.090000000000003	\N	\N	23935
504-63-2	HO(CH2)3OH	\N	other	76.090000000000003	\N	\N	23940
107-12-0	CH3CH2CN	\N	other	55.079999999999998	\N	\N	23945
110-86-1	C5H5N	\N	other	79.099999999999994	\N	\N	23950
9009-65-8	\N	\N	other	\N	\N	\N	23955
65-22-5	C8H9NO3.HCl	\N	other	203.62	\N	\N	23960
17629-30-0	C18H32O16.5H2O	\N	other	594.50999999999999	\N	\N	23965
83-88-5	C17H20N4O6	\N	other	376.36000000000001	\N	\N	23970
7789-39-1	RbBr	\N	other	165.37	\N	\N	23975
09/11/7791	RbCl	\N	other	120.92	\N	\N	23980
7761-88-8	AgNO3	\N	other	169.87	\N	\N	23985
127-09-3	CH3COONa	\N	other	82.030000000000001	\N	\N	23990
6131-90-4	CH3COONa.3H2O	\N	other	136.08000000000001	\N	\N	23995
26628-22-8	NaN3	\N	other	65.010000000000005	\N	\N	24000
7647-15-6	NaBr	\N	other	102.89	\N	\N	24005
\N	(CH3)2AsO2Na	\N	other	159.97999999999999	\N	\N	24010
6131-99-3	(CH3)2AsO2Na.3H2O	\N	other	214.03	\N	\N	24015
497-19-8	Na2CO3	\N	other	105.98999999999999	\N	\N	24020
7647-14-5	NaCl	\N	other	58.439999999999998	\N	\N	24025
03/04/6132	HOC(COONa)(CH2COONa)2.2H2O	\N	other	294.10000000000002	\N	\N	24030
18996-35-5	HOC(COONa)(CH2COOH)2	\N	other	214.11000000000001	\N	\N	24035
302-95-4	C24H39O4Na	\N	other	414.55000000000001	\N	\N	24040
145224-92-6	C24H39O4Na.H2O	\N	other	432.56999999999999	\N	\N	24045
151-21-3	C12H25OSO2Na	\N	other	288.38	\N	\N	24050
7681-49-4	FNa	\N	other	41.990000000000002	\N	\N	24055
141-53-7	HCOONa	\N	other	68.010000000000005	\N	\N	24060
1120-01-0	C16H33NaSO4	\N	other	344.54000000000002	\N	\N	24065
144-55-8	NaHCO3	\N	other	84.010000000000005	\N	\N	24070
13472-35-0	NaH2PO4.2H2O	\N	other	156.00999999999999	\N	\N	24075
10028-24-7	Na2HPO4.2H2O	\N	other	177.99000000000001	\N	\N	24080
7558-79-4	Na2HPO4	\N	other	141.96000000000001	\N	\N	24085
1310-73-2	NaOH	\N	other	40	\N	\N	24090
7681-82-5	NaI	\N	other	149.88999999999999	\N	\N	24095
141-95-7	CH2(COONa)2	\N	other	148.03	\N	\N	24100
26522-85-0	CH2(CO2Na)2.H2O	\N	other	166.03999999999999	\N	\N	24105
7790-28-5	NaIO4	\N	other	213.88999999999999	\N	\N	24110
7631-99-4	Na2NO3	\N	other	84.989999999999995	\N	\N	24115
7632-00-0	NaNO2	\N	other	69	\N	\N	24120
7601-89-0	NaClO4	\N	other	122.44	\N	\N	24125
7601-54-9	Na3PO4	\N	other	163.94	\N	\N	24130
137-40-6	CH3CH2COONa	\N	other	96.060000000000002	\N	\N	24135
150-90-3	NaOOCCH2CH2COONa	\N	other	162.05000000000001	\N	\N	24140
7757-82-6	Na2SO4	\N	other	142.03999999999999	\N	\N	24145
7727-73-3	Na2SO4.10H2O	\N	other	322.19999999999999	\N	\N	24150
6106-24-7	C4H4Na2O6.2H2O	\N	other	230.08000000000001	\N	\N	24155
1330-43-4	Na2B4O7	\N	other	201.22	\N	\N	24160
540-72-7	NaSCN	\N	other	81.069999999999993	\N	\N	24165
10102-17-7	Na2O3S2  5H2O	\N	other	248.18000000000001	\N	\N	24170
650-51-1	CCl3CO2Na	\N	other	185.37	\N	\N	24175
56421-10-4	C44H86NO8P	\N	other	788.13	\N	\N	24180
50-70-4	C6H14O6	\N	other	182.16999999999999	\N	\N	24185
124-20-9	NH2(CH2)3NH(CH2)4NH2	\N	other	145.25	\N	\N	24190
334-50-9	NH2(CH2)3NH(CH2)4NH2.3HCl	\N	other	254.63	\N	\N	24195
306-67-2	C10H26N4.4HCl	\N	other	348.18000000000001	\N	\N	24200
3810-74-0	(C21H39N7O12)2.3H2SO4	\N	other	1457.3800000000001	\N	\N	24205
10476-85-4	SnCl2	\N	other	158.53	\N	\N	24210
110-15-6	C4H6O4	\N	other	118.09	\N	\N	24215
108-30-5	C4H4O3	\N	other	100.06999999999999	\N	\N	24220
123-56-8	C4H5NO2	\N	other	99.090000000000003	\N	\N	24225
57-50-1	C12H22O11	\N	other	342.30000000000001	\N	\N	24230
167410-92-6	C18H20N2O18S2	\N	other	616.48000000000002	\N	\N	24235
7664-93-9	H2SO4	\N	other	98.079999999999998	\N	\N	24240
54960-65-5	C8H19NO6S	\N	other	257.30000000000001	\N	\N	24245
\N	\N	\N	other	\N	\N	\N	24250
29915-38-6	C7H17NO6S	\N	other	243.28	\N	\N	24255
68399-81-5	C7H17NO7S	\N	other	259.27999999999997	\N	\N	24260
125-35-7	NH2CH2CH2SO3H	\N	other	125.15000000000001	\N	\N	24265
110-18-9	C6H16N2	\N	other	116.2	\N	\N	24270
7365-44-8	C6H15NO6S	\N	other	229.25	\N	\N	24275
64-75-5	C22H24N2O8.HCl	\N	other	480.89999999999998	\N	\N	24280
19327-39-0	CH3(CH2)7(OCH2CH2)4OH	\N	other	306.44	\N	\N	24285
14933-09-6	C19H41NO3S	\N	other	363.60000000000002	\N	\N	24290
109-99-9	C4H8O	\N	other	72.109999999999999	\N	\N	24295
2426-02-0	C8H8O3	\N	other	151.15000000000001	\N	\N	24300
25201-30-3	C(HgOOCCH3)4	\N	other	1054.5799999999999	\N	\N	24305
75-57-0	(CH3)4N(Cl)	\N	other	109.59999999999999	\N	\N	24310
54-64-8	C9H9HgNaO2S	\N	other	404.81	\N	\N	24315
89-83-8	C10H140	\N	other	150.22	\N	\N	24320
4272-74-6	C14H21ClN2O3S.HCl	\N	other	369.31	\N	\N	24325
402-71-1	C17H18ClNO3S	\N	other	351.85000000000002	\N	\N	24330
16858-02-9	C26H28N6	\N	other	424.54000000000002	\N	\N	24335
6138-23-4	C12H22O11	\N	other	342.30000000000001	\N	\N	24340
76-03-9	C2HCl3O2	\N	other	163.38999999999999	\N	\N	24345
01/04/5704	C6H13NO5	\N	other	179.16999999999999	\N	\N	24350
102-71-6	C6H15NO3	\N	other	149.19	\N	\N	24355
112-27-6	HO(CH2CH2O)2CH2CH2OH	\N	other	150.16999999999999	\N	\N	24360
75-89-8	C2H3F3O	\N	other	100.04000000000001	\N	\N	24365
593-81-7	(CH3)3N.HCl	\N	other	95.569999999999993	\N	\N	24370
1184-78-7	(CH3)3N(O)	\N	other	75.109999999999999	\N	\N	24375
593-81-7	(CH3)3N.HCl	\N	other	95.569999999999993	\N	\N	24380
5711-19-3	CH3CO2Pb(CH3)3	\N	other	311.35000000000002	\N	\N	24385
298-96-4	C19H15ClN4	\N	other	334.80000000000001	\N	\N	24390
77-86-1	NH2C(CH2OH)	\N	other	121.14	\N	\N	24395
1185-53-1	NH2C(CH2OH)3.HCl	\N	other	157.59999999999999	\N	\N	24400
50525-27-4	C30H24Cl2N6Ru.6H2O	\N	other	748.62	\N	\N	24405
9002-93-1	C14H22O(C2H4O)9-10	\N	other	\N	\N	\N	24410
9036-19-5	C14H22O(C2H4O)7-8	\N	other	\N	\N	\N	24415
37330-34-0	\N	\N	other	\N	\N	\N	24420
9035-81-8	\N	\N	other	\N	\N	\N	24425
9005-64-5	C58H11O26	\N	other	\N	\N	\N	24430
57-13-6	CH4N2O	\N	other	60.060000000000002	\N	\N	24435
1404-93-9	C66H75Cl2N9O24.HCl	\N	other	1485.71	\N	\N	24440
68-19-9	C63H88CoN14O14P	\N	other	1355.3699999999999	\N	\N	24445
7240-90-6	C14H15BrClNO6	\N	other	408.63	\N	\N	24450
2650-17-1	C25H27N2NaO6S2	\N	other	538.61000000000001	\N	\N	24455
3618-43-7	C31H28N2Na4O13S	\N	other	760.58000000000004	\N	\N	24460
87-99-0	HOCH2[CH(OH)]3CH2OH	\N	other	152.15000000000001	\N	\N	24465
10035-01-5	YbCl3.6H2O	\N	other	387.49000000000001	\N	\N	24470
10025-94-2	Cl3Y.6H2O	\N	other	303.36000000000001	\N	\N	24475
10361-92-9	YCl3	\N	other	195.25999999999999	\N	\N	24480
5970-45-6	C4H6O4Zn.2H2O	\N	other	219.50999999999999	\N	\N	24485
557-34-6	(CH3CO2)2Zn	\N	other	183.47999999999999	\N	\N	24490
7646-85-7	ZnCl2	\N	other	136.30000000000001	\N	\N	24495
7446-20-0	ZnSO4.7H2O	\N	other	287.56	\N	\N	24500
7733-02-0	ZnSO4	\N	other	161.45400000000001	\N	\N	24505
\.


--
-- TOC entry 2856 (class 0 OID 63149)
-- Dependencies: 1766
-- Data for Name: mole_molecule2relareobel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_molecule2relareobel (trialmoleculeid, relatedresearchobjectiveelementid) FROM stdin;
\.


--
-- TOC entry 2857 (class 0 OID 63152)
-- Dependencies: 1767
-- Data for Name: mole_moleculefeature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_moleculefeature (endseqid, featuretype, name, ordering, startseqid, status, labbookentryid, refmoleculeid, moleculeid) FROM stdin;
\.


--
-- TOC entry 2858 (class 0 OID 63155)
-- Dependencies: 1768
-- Data for Name: mole_primer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_primer (direction, gcgene, isuniversal, lengthongene, meltingtemperature, meltingtemperaturegene, meltingtemperatureseller, opticaldensity, particularity, restrictionsite, moleculeid) FROM stdin;
\.


--
-- TOC entry 2859 (class 0 OID 63161)
-- Dependencies: 1769
-- Data for Name: peop_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_group (name, url, labbookentryid, organisationid) FROM stdin;
\.


--
-- TOC entry 2860 (class 0 OID 63164)
-- Dependencies: 1770
-- Data for Name: peop_orga_addresses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_orga_addresses (organisationid, address, order_) FROM stdin;
14001	16 The Egde Business Centre	0
14001	Humber Road	1
14003	Inchinnan Business Park	0
14003	3 Fountain Drive	1
14005	Unit 4	0
14005	Northfield Business Park	1
14005	Northfield Road	2
14005	Cambridgeshire	3
14007	73 Knowl Piece	0
14007	Wilbury Way	1
14007	Hetrts	2
14009	MERCK Biosciences Ltd	0
14009	Boulevard Industrial Park	1
14009	Padge Road	2
14009	Beeston	3
14011	Delta House	0
14011	Chilworth Science Park	1
14013	Wellington House 	0
14013	East Road	1
14013	Cambridgeshire	2
14015	Qiagen House	0
14015	Fleming Way	1
14015	West Sussex	2
14017	Gebouw California	0
14017	Hogehilweg 15	1
14017	Zuidoost	2
14019	66A Cambridge Road	0
14021	Amersham Place	0
14021	Buckinghamshire	1
14023	434 West Dussel Drive 	0
14025	700 Industrial Park Drive	0
14027	Delph Court	0
14027	Sherdley Business Park Industrial Estate	1
14027	Sullivans Way	2
14029	Bio-Rad House	0
14029	Maxted Road	1
14029	Hertfordshire	2
14031	MERCK Biosciences Ltd	0
14031	Boulevard Industrial Park	1
14031	Padge Road	2
14031	Beeston	3
14033	Bishop Meadow Road	0
14035	ZGZ	0
14035	Zapfholzweg 1	1
14037	34 Journey	0
14037	California	1
14039	Loebstedter Strasse 7	0
14041	Boulevard Industrial Park	0
14041	Padge Road	1
14041	Beeston	2
14043	Invitrogen Ltd	0
14043	Inchinnan Business Park	1
14043	3 Fountain Drive	2
14045	172 E. Aurora Street	0
14047	Perbio Science UK Ltd	0
14047	Unit 9 Atley Way	1
14047	North Nelson Industrial Estate	2
14047	Northumberland	3
14049	6 Caberston Road	0
14051	40 Broadwater Road	0
14051	P.O.Box 8	1
14051	Hertfordshire	2
14053	Fancy Road	0
14053	Dorset	1
14055	Nissanstrasse 2	0
14057	BioCampus Cologne	0
14057	Nattermannallee 1	1
14059	90 Long Acre	0
14059	Covent Garden	1
14061	1290 Terra Bella Ave.	0
14061	Mountain View	1
14063	104 Avenue Franklin Roosevelt - Box 25	0
14063	B-1330 Rixensar	1
14065	500 Cummings Center	0
14065	Suite 2450	1
14065	Beverly 	2
\.


--
-- TOC entry 2861 (class 0 OID 63167)
-- Dependencies: 1771
-- Data for Name: peop_organisation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_organisation (city, country, emailaddress, faxnumber, name, organisationtype, phonenumber, postalcode, url, labbookentryid) FROM stdin;
London	UK	info@bioline.com	0208 4522822	Bioline Ltd	Supplier	0208 8305300	NW2 6EW	http://www.bioline.com/n_uk.htm	14001
Paisley	Scotland	euroinfo@invitrogen.com	0141 814 6260	Invitrogen/Life Technologies	Supplier	0141 814 6100	PA4 9RF	http://www.invitrogen.com/	14003
Soham	UK	enquiries@moleculardimensions.com	01353 722119	Molecular Dimensions	Supplier	01353 722177	CB7 5UE	http://www.moleculardimensions.com/uk	14005
Hitchin	UK	info@uk.neb.com	0800 435682	New England Biolabs (UK) Ltd	Supplier	0800 318486	SG4 0TY	http://www.neb.uk.com/	14007
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	Novagen	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/html/NVG/home.html	14009
Southampton	UK	ukcustserve@promega.com	0800 181037	Promega	Supplier	0800 378994	SO16 7NS	www.promega.com/uk/	14011
Cambridge	UK	ukorders@qbiogene.com	0800 085 3090	Qbiogene	Supplier	0800 328 8401	CB1 1BH	http://www.qbiogene.com	14013
Crawley	UK	CustomerService-uk@qiagen.com	1293422922	Qiagen Ltd	Supplier	01293 422911	RH10 9NQ	http://www1.qiagen.com/	14015
Amsterdam	The Netherlands	orders.europe@stratagene.com	0800 9173283	Stratagene	Supplier	0800 9173282	1101 CB	http://www.stratagene.com	14017
Stansted	UK	websales@agarscientific.com	01279 815106	Agar Scientific	Supplier	01279 813519	CM24 8DA	http://www.agarscientific.com/	14019
Little Chalfont	UK	orders.gb@amersham.com	01494 544350	Amersham Biosciences UK Limited	Supplier	0870 606 1921	HP7 9NA	http://www1.amershambiosciences.com/aptrix/upp01077.nsf/Content/uk_homepage	14021
Maumee	USA	info@anatrace.com	1 419 891 3037	Anatrace Inc.	Supplier	1 419 891 3030	OH 43537	http://www.anatrace.com/	14023
Alabaster	USA	orders@avantilipids.com	1 205 663-00756	Avanti Polar Lipids, Inc	Supplier	1 205 663-2494	AL 35007	http://www.avantilipids.com	14025
St. Helens	UK	\N	01744 73 00 64	Bachem (UK) Ltd	Supplier	01744 61 21 08	WA9 5GL	http://www.bachem.com/	14027
Hemel Hempstead	UK	uk.lsg.marketing@bio-rad.com	020 8328 2550	Bio-Rad Laboratories Ltd	Supplier	0800 181134	HP2 7DX	http://www.bio-rad.com	14029
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	Calbiochem	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/html/CBC/home.html	14031
Loughborough	UK	sales@fisher.co.uk	01509 231893	Fisher Scientific UK Ltd	Supplier	01509 231166	LE11 5RG	http://www.fisher.co.uk/index.htm	14033
Luckenwalde	Germany	info@glycon.de	49 3371  681170	Glycon Biochemicals	Supplier	49 3371  681171	D-14943	http://www.glycon.de/	14035
Aliso Viejo	USA	info@hrmail.com	1 949 425 1611	Hampton research Corp.	Supplier	1 949 425 1321	CA 92656-3317	http://www.hamptonresearch.com/	14037
Jena	Germany	info@jenabioscience.com	49 3641 464991	Jena Bioscience GmbH	Supplier	49 3641 464952	D-07749	http://www.jenabioscience.com	14039
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	MERCK Biosciences Ltd	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/home.asp	14041
Paisley	UK	euroinfo@invitrogen.com	1 541 335 0202	Molecular Probes	Supplier	1 541 335 0485	PA4 9RF	http://www.probes.com/	14043
Waterbury	USA	sales@pfaltzandbauer.com	203 574 3181	Pfaltz & Bauer	Supplier	1 203 574 0075	CT 06708	http://www.pfaltzandbauer.com/	14045
Cramlington	UK	uk.info@perbio.com	0800 085 8772	Pierce	Supplier	0800 252 185	NE231WA	http://www.piercenet.com/	14047
Walkerburn	UK	info@rathburn.co.uk	01896 870 633	Rathburn Chemicals Ltd	Supplier	01896 870 651	EH43 6AU	http://www.rathburn.co.uk/	14049
Welwyn Garden City	UK	\N	01707 338297	Roche Products Ltd	Supplier	01707 366000	AL7 3AY	http://www.rocheuk.com	14051
Poole	UK	ukcustsv@europe.sial.com	0800 378785	Sigma-Aldrich Company Ltd	Supplier	0800 717181	BH12 4QH	http://www.sigmaaldrich.com/	14053
Neuss	Germany	biochem@wako-chemicals.de	49 2131 311100	Wako	Supplier	49 2131 3110	D-41468	http://www.wako-chemicals.de/framebio.htm	14055
Cologne	Germany	oligo-eu@operon.com	\N	Operon Biotechnologies GmbH	Supplier	49 221 170 90 270	50829	http://www.operon.com	14057
London	UK	support@mwgdna.com	\N	MWG Biotech	Supplier	0800 032 313 5	WC2E 9RZ	http://www.mwg-biotech.com	14059
\N	USA	orders@clontech.com	800 424 1350	Clontech	Supplier	800 662 2566	CA 94043	http://www.clontech.com	14061
\N	Belgium	euro-orders@activemotif.com	32 (0)2 653 0050	ActiveMotif	Supplier	0800/169 31 47	\N	http://www.activemotif.com	14063
\N	USA	� websales@agencourt.com	978-867-2601	Agencourt Bioscience Corporation	Supplier	800-361-7780	MA 1915	http://www.agencourt.com	14065
\.


--
-- TOC entry 2862 (class 0 OID 63173)
-- Dependencies: 1772
-- Data for Name: peop_persingr_phonnu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_persingr_phonnu (personingroupid, phonenumber, order_) FROM stdin;
\.


--
-- TOC entry 2863 (class 0 OID 63176)
-- Dependencies: 1773
-- Data for Name: peop_person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_person (familyname, familytitle, givenname, title, labbookentryid, currentgroupid) FROM stdin;
\.


--
-- TOC entry 2864 (class 0 OID 63179)
-- Dependencies: 1774
-- Data for Name: peop_person_middin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_person_middin (personid, middleinitial, order_) FROM stdin;
\.


--
-- TOC entry 2865 (class 0 OID 63182)
-- Dependencies: 1775
-- Data for Name: peop_personingroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_personingroup (deliveryaddress, emailaddress, enddate, faxnumber, mailingaddress, position_, startdate, labbookentryid, personid, groupid) FROM stdin;
\.


--
-- TOC entry 2866 (class 0 OID 63188)
-- Dependencies: 1776
-- Data for Name: prot_parade_possva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_parade_possva (parameterdefinitionid, possiblevalue, order_) FROM stdin;
34055	Induction Batch	0
34055	AutoInduction Batch	1
34055	Fed Batch	2
34055	Optimization	3
34075	Yes	0
34075	No	1
34083	Yes	0
34083	No	1
34083	Returned	2
36011	Superdex 75	0
36011	Superdex 200 10/30	1
36011	Suparose 6	2
36095	Yes	0
36095	No	1
36095	Maybe	2
36095	Unscored	3
36097	Yes	0
36097	No	1
36097	Maybe	2
36097	Unscored	3
36105	Yes	0
36105	No	1
36105	Maybe	2
36105	Unscored	3
36107	Yes	0
36107	No	1
36107	Maybe	2
36107	Unscored	3
36189	Yes	0
36189	No	1
36189	Maybe	2
36189	Unscored	3
36201	Yes	0
36201	No	1
36201	Maybe	2
36201	Unscored	3
36203	Yes	0
36203	No	1
36203	Maybe	2
36203	Unscored	3
36211	Yes	0
36211	No	1
36211	Maybe	2
36211	Unscored	3
36213	Yes	0
36213	No	1
36213	Maybe	2
36213	Unscored	3
36267	Superdex 75	0
36267	Superdex 200 10/30	1
36267	Suparose 6	2
\.


--
-- TOC entry 2867 (class 0 OID 63191)
-- Dependencies: 1777
-- Data for Name: prot_parameterdefinition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_parameterdefinition (defaultvalue, displayunit, isgrouplevel, ismandatory, isresult, label, maxvalue, minvalue, name, paramtype, unit, labbookentryid, protocolid, order_) FROM stdin;
\N	\N	f	t	f	\N	\N	\N	Reverse Overlap	Int	\N	32009	32001	0
\N	\N	f	t	f	\N	\N	\N	Forward Tag	String	\N	32011	32001	1
\N	\N	f	t	f	\N	\N	\N	Fwd. Primer GC	Float	\N	32013	32001	2
\N	\N	f	t	f	\N	\N	\N	Forward Overlap	Int	\N	32015	32001	3
\N	\N	f	t	f	\N	\N	\N	Rev. Primer GC	Float	\N	32017	32001	4
\N	\N	f	t	f	\N	\N	\N	Reverse Tag	String	\N	32019	32001	5
Reverse Phase	\N	f	t	f	\N	\N	\N	Purification type	String	\N	32029	32021	0
25 nmole	\N	f	t	f	\N	\N	\N	Starting Synthesis Scale	String	\N	32031	32021	1
TE	\N	f	t	f	\N	\N	\N	BufferType	String	\N	32033	32021	2
Concentration	\N	f	t	f	\N	\N	\N	Normalization	String	\N	32035	32021	3
AvrII	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	32037	32021	4
0.0	\N	f	t	f	\N	\N	\N	Tm seller	Float	\N	32039	32021	5
0.0	\N	f	t	f	\N	\N	\N	Tm full	Float	\N	32041	32021	6
0.0	\N	f	t	f	\N	\N	\N	Tm gene	Float	\N	32043	32021	7
400	\N	f	t	f	\N	\N	\N	volumn in nL	Float	\N	32045	32021	8
0.0	\N	f	t	f	\N	\N	\N	OD	Float	\N	32047	32021	9
0	\N	f	t	f	\N	\N	\N	Length on the gene	Int	\N	32049	32021	10
\N	\N	f	t	f	\N	\N	\N	Forward Primer Layout Notes	String	\N	32057	32051	0
\N	\N	f	t	f	\N	\N	\N	Reverse Primer Layout Notes	String	\N	32065	32059	0
5	\N	f	t	f	\N	\N	\N	Reaction volume/uL	Float	\N	32077	32067	0
\N	\N	f	t	f	\N	\N	\N	Infusion Notes	String	\N	32079	32067	1
\N	\N	f	t	f	\N	\N	\N	Volume of cells plated out	Float	\N	32093	32081	0
Incubate 30 min on ice,   Heat shock 30 sec,   Incubate a further 2 min on ice	\N	f	t	f	\N	\N	\N	DNA Incubation conditions	String	\N	32095	32081	1
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32097	32081	2
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32099	32081	3
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32101	32081	4
\N	\N	f	t	f	\N	\N	\N	Transformation Result	Int	\N	32103	32081	5
\N	\N	f	t	f	\N	\N	\N	Transformation Notes	String	\N	32105	32081	6
\N	\N	f	t	f	\N	\N	\N	Concentrator type	String	\N	32115	32107	0
\N	\N	f	t	f	\N	\N	\N	Concentrator speed (g)	Int	\N	32117	32107	1
\N	\N	f	t	f	\N	\N	\N	Output volume	Int	\N	32119	32107	2
\N	\N	f	t	f	\N	\N	\N	Output concentration	Int	\N	32121	32107	3
\N	\N	f	t	f	\N	\N	\N	Output yield	Int	\N	32123	32107	4
\N	\N	f	t	f	\N	\N	\N	Concentrator Notes	String	\N	32125	32107	5
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32137	32127	0
false	\N	f	t	f	\N	\N	\N	Is the culture stirred?	Boolean	\N	32139	32127	1
false	\N	f	t	f	\N	\N	\N	Is the culture labelled?	Boolean	\N	32141	32127	2
50	\N	f	t	f	\N	\N	\N	LIC vector concentration ng/ul	Float	\N	32153	32143	0
Substitute 2ul H2O for 2ul LIC-prepared insert	\N	f	t	f	\N	\N	\N	Control	String	\N	32155	32143	1
Incubate 5 min at room temperature,    Cool on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32157	32143	2
\N	\N	f	t	f	\N	\N	\N	A. Number of colonies	Int	\N	32165	32159	0
Plate out and incubate overnight at 37C	\N	f	t	f	\N	\N	\N	B. Growth conditions	String	\N	32167	32159	1
LB agar plus 30ug/ml kanamycin	\N	f	t	f	\N	\N	\N	C. Plate type	String	\N	32169	32159	2
2mm	\N	f	t	f	\N	\N	\N	D. Colony size	String	\N	32171	32159	3
\N	\N	f	t	f	\N	\N	\N	Insert concentration pmol/ul	Float	\N	32189	32173	0
Warm to room temperature,    Incubate 30 min at 22C,    Heat for 5 min at 75C	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32191	32173	1
Incubate 5 min on ice,   Heat shock 30 sec,   Incubate a further 5 min on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32203	32193	0
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32205	32193	1
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32207	32193	2
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32209	32193	3
\N	\N	f	t	f	Plasmid concentration ug/ml	\N	\N	A. Plasmid concentration ug/ml	Float	\N	32231	32211	0
\N	\N	f	t	f	Plasmid volume for 8 ug	\N	\N	B. Plasmid volume for 8 ug	Float	\N	32233	32211	1
    Incubate 37C for 2 hours	\N	f	t	f	Incubation conditions	\N	\N	C. Digest conditions	String	\N	32235	32211	2
true	\N	f	t	f	\N	\N	\N	D. Gel purified	Boolean	\N	32237	32211	3
8	\N	f	t	f	\N	\N	\N	E. Linearised vector yield, pmol	Float	\N	32239	32211	4
Warm to room temperature,    Incubate 30 min at 22C,    Heat for 5 min at 75C	\N	f	t	f	\N	\N	\N	F. Incubation conditions	String	\N	32241	32211	5
50	\N	f	t	f	Final vector concentration ng/ul	\N	\N	G. Final vector concentration ng/ul	Float	\N	32243	32211	6
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32261	32245	0
Qiagen Plasmid midi kit Cat.12123	\N	f	t	f	\N	\N	\N	Kit name	String	\N	32263	32245	1
\N	\N	f	t	f	\N	\N	\N	Yield	Float	\N	32265	32245	2
\N	\N	f	t	f	\N	\N	\N	Concentration ug/ml	Float	\N	32267	32245	3
45	\N	f	t	f	\N	\N	\N	A. Annealing temperature	Float	\N	32287	32269	0
35	\N	f	t	f	\N	\N	\N	B. Number of cycles	Int	\N	32289	32269	1
Anneal 30 sec at 45C,    Extend 30 sec at 72C,    Denature 30 sec at 94C	\N	f	t	f	\N	\N	\N	C. Thermocycling conditions	String	\N	32291	32269	2
100	\N	f	t	f	\N	\N	\N	D. Primer concentration uM	Float	\N	32293	32269	3
50	\N	f	t	f	\N	\N	\N	E. Template concentration ng/ul	Float	\N	32295	32269	4
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32313	32297	0
Qiagen Plasmid midi kit 12143	\N	f	t	f	\N	\N	\N	Kit name	String	\N	32315	32297	1
\N	\N	f	t	f	\N	\N	\N	Plasmid yield	Float	\N	32317	32297	2
\N	\N	f	t	f	\N	\N	\N	Plasmid concentration ug/ml	Float	\N	32319	32297	3
\N	\N	f	t	f	\N	\N	\N	Number of colonies	Int	\N	32327	32321	0
false	\N	f	t	f	\N	\N	\N	Is the culture labelled?	Boolean	\N	32329	32321	1
Incubate overnight at 37C	\N	f	t	f	\N	\N	\N	Growth conditions	String	\N	32331	32321	2
\N	\N	f	t	f	\N	\N	\N	Plate type	String	\N	32333	32321	3
Incubate 5 min on ice,   Heat shock 30 sec,   Incubate a further 5 min on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32345	32335	0
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32347	32335	1
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32349	32335	2
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32351	32335	3
B834	\N	f	t	f	\N	\N	\N	A. Expression strain	String	\N	32361	32353	0
\N	\N	f	t	f	\N	\N	\N	B. Construct ID and location	String	\N	32363	32353	1
true	\N	f	t	f	\N	\N	\N	C. Is the culture shaken?	Boolean	\N	32365	32353	2
160	\N	f	t	f	\N	\N	\N	D. Shaker speed rpm	Int	\N	32367	32353	3
2.0	\N	f	t	f	\N	\N	\N	E. Time (hours) before induction	Float	\N	32369	32353	4
0.6	\N	f	t	f	\N	\N	\N	F. OD before induction	Float	\N	32371	32353	5
37	\N	f	t	f	\N	\N	\N	G. Temperatue C	Float	\N	32373	32353	6
IPTG	\N	f	t	f	\N	\N	\N	H. Autoinduction/IPTG	String	\N	32375	32353	7
2	\N	f	t	f	\N	\N	\N	I. Sample time interval (hours)	Float	\N	32377	32353	8
\N	\N	f	t	f	\N	\N	\N	J. OD sample 1	Float	\N	32379	32353	9
\N	\N	f	t	f	\N	\N	\N	K. OD sample 2	Float	\N	32381	32353	10
\N	\N	f	t	f	\N	\N	\N	L. OD sample 3	Float	\N	32383	32353	11
\N	\N	f	t	f	\N	\N	\N	M. Optimal expression time (hours)	Float	\N	32385	32353	12
Sonication	\N	f	t	f	\N	\N	\N	N. Solubilization method	String	\N	32387	32353	13
\N	\N	f	t	f	\N	\N	\N	O. Solubilization details	String	\N	32389	32353	14
true	\N	f	t	f	\N	\N	\N	P. Gel image attached?	Boolean	\N	32391	32353	15
Markers in lane 1, etc.	\N	f	t	f	\N	\N	\N	Q. Gel details	String	\N	32393	32353	16
\N	\N	f	t	f	\N	\N	\N	Marker	String	\N	34009	34001	0
\N	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	34011	34001	1
\N	\N	f	t	f	\N	\N	\N	Organism	String	\N	34019	34013	0
\N	\N	f	t	f	\N	\N	\N	Batch	String	\N	34053	34021	0
AutoInduction Batch	\N	f	t	f	\N	\N	\N	Culture Type	String	\N	34055	34021	1
10	\N	f	t	f	\N	\N	\N	Culture Volume	Int	\N	34057	34021	2
\N	\N	f	t	f	\N	\N	\N	Reference Number	Int	\N	34063	34059	0
\N	\N	f	t	f	\N	\N	\N	Template Name	String	\N	34065	34059	1
\N	\N	f	t	f	\N	\N	\N	Template Concentration	Float	\N	34067	34059	2
\N	\N	f	t	f	\N	\N	\N	Template Volume	Float	\N	34069	34059	3
\N	\N	f	t	f	\N	\N	\N	Template Length	Int	\N	34071	34059	4
\N	\N	f	t	f	\N	\N	\N	Required Read Length	Int	\N	34073	34059	5
No	\N	f	t	f	\N	\N	\N	CD Provided?	String	\N	34075	34059	6
\N	\N	f	t	f	\N	\N	\N	Primer Name	String	\N	34077	34059	7
\N	\N	f	t	f	\N	\N	\N	Primer Volume	Float	\N	34079	34059	8
\N	\N	f	t	f	\N	\N	\N	Primer Concentration	Float	\N	34081	34059	9
No	\N	f	t	f	\N	\N	\N	Return Samples?	String	\N	34083	34059	10
\N	\N	f	t	f	\N	\N	\N	Account Number	String	\N	34085	34059	11
\N	\N	f	t	f	\N	\N	\N	Principal Investigator	String	\N	34087	34059	12
\N	\N	f	t	f	\N	\N	\N	User	String	\N	34089	34059	13
\N	\N	f	t	f	\N	\N	\N	User Phone	String	\N	34091	34059	14
\N	\N	f	t	f	\N	\N	\N	User Email	String	\N	34093	34059	15
\N	\N	f	t	f	\N	\N	\N	Department	String	\N	34095	34059	16
\N	\N	f	t	f	\N	\N	\N	Order ID	String	\N	34097	34059	17
Superdex 200 10/30	\N	f	t	f	\N	\N	\N	SEC Gel	String	\N	36011	36001	0
\N	\N	f	t	f	\N	\N	\N	Sample1 Fractions	String	\N	36013	36001	1
\N	\N	f	f	f	\N	\N	\N	Sample2 Fractions	String	\N	36015	36001	2
\N	\N	f	f	f	\N	\N	\N	Sample3 Fractions	String	\N	36017	36001	3
\N	\N	f	f	f	\N	\N	\N	Complex Protocol	String	\N	36027	36019	0
\N	\N	f	f	f	\N	\N	\N	Cofactors	String	\N	36029	36019	1
\N	\N	f	f	f	\N	\N	\N	PCT Result	String	\N	36037	36031	0
\N	\N	f	t	f	\N	\N	\N	Initial Concentration	String	\N	36039	36031	1
\N	\N	f	t	f	\N	\N	\N	Final Concentration	String	\N	36041	36031	2
\N	\N	f	t	f	\N	\N	\N	Forward Site	String	\N	36051	36043	0
\N	\N	f	t	f	\N	\N	\N	Forward Annealing Temp	Float	\N	36053	36043	1
\N	\N	f	t	f	\N	\N	\N	Reverse Site	String	\N	36055	36043	2
\N	\N	f	t	f	\N	\N	\N	Reverse Annealing Temp	Float	\N	36057	36043	3
\N	\N	f	t	f	\N	\N	\N	Beanline	String	\N	36069	36065	0
\N	\N	f	t	f	\N	\N	\N	Data collection date	String	\N	36071	36065	1
\N	\N	f	t	f	\N	\N	\N	Data collection place	String	\N	36073	36065	2
\N	\N	f	t	f	\N	\N	\N	Indicative resolution	String	\N	36075	36065	3
\N	\N	f	t	f	\N	\N	\N	Dataset	String	\N	36077	36065	4
\N	\N	f	t	f	\N	\N	\N	Peak Dataset	String	\N	36079	36065	5
\N	\N	f	t	f	\N	\N	\N	Infl Dataset	String	\N	36081	36065	6
\N	\N	f	t	f	\N	\N	\N	HE Rem Dataset	String	\N	36083	36065	7
\N	\N	f	t	f	\N	\N	\N	LE Rem Dataset	String	\N	36085	36065	8
Unscored	\N	f	t	f	Soluble Expression Level	\N	\N	__MW	String	\N	36095	36087	0
Unscored	\N	f	t	f	InSoluble Expression Level	\N	\N	__IMW	String	\N	36097	36087	1
Unscored	\N	f	t	f	Soluble Expression Level	\N	\N	__MW	String	\N	36105	36099	0
Unscored	\N	f	t	f	InSoluble Expression Level	\N	\N	__IMW	String	\N	36107	36099	1
\N	\N	f	f	f	\N	\N	\N	Observer Molecular Weight (kDa)	Int	\N	36109	36099	2
\N	\N	f	f	f	\N	\N	\N	IMAC Trace	String	\N	36117	36111	0
\N	\N	f	f	f	\N	\N	\N	IMAC Protocol	String	\N	36119	36111	1
21	\N	f	t	f	\N	\N	\N	Total Volume (uL)	Float	\N	36133	36121	0
\N	\N	f	f	f	\N	\N	\N	Notes	String	\N	36135	36121	1
\N	\N	f	f	f	\N	\N	\N	MALS Trace	String	\N	36143	36137	0
\N	\N	f	f	f	\N	\N	\N	MALS Protocol	String	\N	36145	36137	1
\N	\N	f	f	f	\N	\N	\N	MALS Analysis	String	\N	36147	36137	2
94	\N	f	t	f	\N	\N	\N	Denat/oC	Int	\N	36167	36149	0
30	\N	f	t	f	\N	\N	\N	Denat/s	Int	\N	36169	36149	1
60	\N	f	t	f	\N	\N	\N	Anneal/oC	Int	\N	36171	36149	2
30	\N	f	t	f	\N	\N	\N	Anneal time/s	Int	\N	36173	36149	3
72	\N	f	t	f	\N	\N	\N	Extension/oC	Int	\N	36175	36149	4
2	\N	f	t	f	\N	\N	\N	Extension/min	Int	\N	36177	36149	5
68	\N	f	t	f	\N	\N	\N	Final extension/oC	Int	\N	36179	36149	6
2	\N	f	t	f	\N	\N	\N	Final extension/min	Int	\N	36181	36149	7
30	\N	f	t	f	\N	\N	\N	No. Cycles	Int	\N	36183	36149	8
50	\N	f	t	f	\N	\N	\N	Total Volume (uL)	Float	\N	36185	36149	9
\N	\N	f	t	f	\N	\N	\N	PCR's Notes	String	\N	36187	36149	10
Unscored	\N	f	t	f	PCR Product	\N	\N	__SCORE	String	\N	36189	36149	11
Unscored	\N	f	t	f	Soluble Expression Level	\N	\N	__MW	String	\N	36201	36191	0
Unscored	\N	f	t	f	InSoluble Expression Level	\N	\N	__IMW	String	\N	36203	36191	1
Unscored	\N	f	t	f	Soluble Expression Level	\N	\N	__MW	String	\N	36211	36205	0
Unscored	\N	f	t	f	InSoluble Expression Level	\N	\N	__IMW	String	\N	36213	36205	1
\N	\N	f	f	f	\N	\N	\N	Observer Molecular Weight (kDa)	Int	\N	36215	36205	2
50	\N	f	t	f	\N	\N	\N	Total Volume (uL)	Float	\N	36233	36217	0
\N	\N	f	f	f	\N	\N	\N	Notes	String	\N	36235	36217	1
10	\N	f	t	f	\N	\N	\N	Total Volume (uL)	Float	\N	36253	36237	0
\N	\N	f	f	f	\N	\N	\N	Notes	String	\N	36255	36237	1
Superdex 200 10/30	\N	f	t	f	\N	\N	\N	SEC Gel	String	\N	36267	36257	0
\N	\N	f	t	f	\N	\N	\N	SEC Protocol	String	\N	36269	36257	1
\N	\N	f	t	f	\N	\N	\N	Sample1 Fractions	String	\N	36271	36257	2
\N	\N	f	f	f	\N	\N	\N	Sample2 Fractions	String	\N	36273	36257	3
\N	\N	f	f	f	\N	\N	\N	Sample3 Fractions	String	\N	36275	36257	4
\N	\N	f	f	f	\N	\N	\N	Cryroprotectant	String	\N	36289	36283	0
\N	\N	f	f	f	\N	\N	\N	Cryo concentration	Float	\N	36291	36283	1
\N	\N	f	f	f	\N	\N	\N	Other Additives	String	\N	36293	36283	2
\N	\N	f	f	f	\N	\N	\N	Soak Time	Float	\N	36295	36283	3
\N	\N	f	f	f	\N	\N	\N	Protocol Description	String	\N	36305	36297	0
\N	\N	f	f	f	\N	\N	\N	Temperature	Float	\N	36307	36297	1
\N	\N	f	f	f	\N	\N	\N	Setup Program	String	\N	36309	36297	2
\N	\N	f	f	f	\N	\N	\N	Plate Barcode	String	\N	36311	36297	3
\N	\N	f	f	f	\N	\N	\N	Well Number	String	\N	36313	36297	4
\N	\N	f	f	f	\N	\N	\N	Screen Definition	String	\N	36321	36315	0
\N	\N	f	f	f	\N	\N	\N	Temperature	Float	\N	36323	36315	1
\N	\N	f	f	f	\N	\N	\N	Setup Program	String	\N	36325	36315	2
\N	\N	f	f	f	\N	\N	\N	Plate Barcode	String	\N	36327	36315	3
\.


--
-- TOC entry 2868 (class 0 OID 63197)
-- Dependencies: 1778
-- Data for Name: prot_protocol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol (isforuse, methoddescription, name, objective, labbookentryid, experimenttypeid) FROM stdin;
\N	\N	SPOT Construct Primer Design	\N	32001	1
\N	\N	Primer Order Plate	Order a plate of primer for targets	32021	141
\N	\N	PIMS Plate Layout - Forward Primers	Design Plate Layout	32051	113
\N	\N	PIMS Plate Layout - Reverse Primers	Design Plate Layout	32059	113
\N	\N	PIMS Bicistronic InFusion	Capture PCR Product into pOPIN vectors	32067	9
\N	\N	PIMS Bitrans Transformation	Transform E.coli with plasmid, generic heat shock protocol	32081	19
\N	\N	PiMS Co Concentration	Concentration of Target Proteins	32107	53
\N	\N	PiMS Culture	Growth of E.coli in liquid culture	32127	111
\N	\N	PiMS LIC annealing	anneal LIC T4 polymerase treated insert and vector, generate recombinant plasmid	32143	15
\N	\N	PiMS LIC Plate culture	Growth of isolated colonies	32159	127
\N	\N	PiMS LIC-polymerase reaction	generate vector-compatible S/S overhangs	32173	17
\N	\N	PiMS LIC transformation	Transform E.coli with LIC annealing product, heat shock protocol	32193	19
\N	\N	PiMS LIC vector preparation	generate insert-compatible S/S overhangs	32211	17
\N	\N	PiMS Miniprep	Purify plasmid from small volume culture	32245	39
\N	\N	PiMS PCR	Amplify target DNA	32269	7
\N	\N	PiMS Plasmid prep	Purify plasmid from culture	32297	41
\N	\N	PiMS Plate culture	Growth of isolated colonies	32321	127
\N	\N	PiMS Transformation	Transform E.coli with plasmid, generic heat shock protocol	32335	19
\N	\N	PiMS Trial expression	Determine conditions for optimal expression of target protein	32353	31
\N	\N	PiMS Unspecified	To allow the user to create an experiment without a protocol	32395	139
\N	\N	Entry clone	Gateway Entry Clone design	34001	3
\N	\N	Deep-Frozen culture	Record Deep-Frozen culture stock	34013	5
\N	\N	Generic fermenter culture	Recording fermentation batches	34021	143
\N	\N	Sequencing order	Supply samples and other required details for sequencing	34059	147
\N	\N	Leeds sequencing setup	Prepare sample for sequencing on the robot	34099	149
\N	\N	CS Chromatography	Purification of Target Protein	36001	45
\N	\N	CS Complexation	Combination of Protein Samples	36019	151
\N	\N	CS Concentration	Concentration of Target Protein	36031	53
\N	\N	CS Construct Design	\N	36043	27
\N	\N	CS Deglycosylation	Removal of Glycans	36059	43
\N	\N	CS Diffraction	Collection of data from diffraction on beamline	36065	93
\N	\N	CS Eukaryotic Expression	Large Scale Expression of Target Protein	36087	33
\N	\N	CS Eukaryotic Expression Trial	Small Scale Expression of Target Protein	36099	31
\N	\N	CS IMAC	Purification of Target Protein	36111	45
\N	\N	CS Ligation	Ligation	36121	21
\N	\N	CS MALS	Purification of Target Protein	36137	117
\N	\N	CS PCR	Amplify target DNA	36149	7
\N	\N	CS Prokaryotic Expression	Large Scale Expression of Target Protein	36191	33
\N	\N	CS Prokaryotic Expression Trial	Small Scale Expression of Target Protein	36205	31
\N	\N	CS Restriction Digest PCR	Prepare PCR Product for Cloning	36217	11
\N	\N	CS Restriction Digest Vector	Prepare Vector for Cloning	36237	11
\N	\N	CS Size Exclusion Chromatography	Purification of Target Protein	36257	43
\N	\N	CS Solubilisation Expression	\N	36277	49
\N	\N	CS Crystal Harvest	Mounting a crystal onto a pin	36283	87
\N	\N	CS Crystal Optimization	Optimization of crystallization screen	36297	63
\N	\N	CS Crystal Screen	Sparse-matrix crystallization screen	36315	71
\N	\N	CrystalTrial	Do crystal trial	38018	71
\.


--
-- TOC entry 2869 (class 0 OID 63203)
-- Dependencies: 1779
-- Data for Name: prot_protocol_remarks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol_remarks (protocolid, remark, order_) FROM stdin;
32021	created by PIMS	0
32051	Based on Lab activities requirements from OPPF	0
32059	Based on Lab activities requirements from OPPF	0
32067	Based on Lab activities requirements from OPPF	0
32081	Based on Lab activities requirements from OPPF	0
32107	Based on Lab activities requirements from OPPF	0
32127	Use appropriate selective medium and container	0
32143	for control reaction, substitute 2ul H2O for 2ul LIC polymerase reaction	0
32159	use appropriate pre-warmed selective agar support	0
32193	Edit protocol for specific cells	0
32245	based on use of Qiagen Plasmid mini kit Cat. 12123	0
32297	Based on use of Qiagen Plasmid midi kit Cat. 12143	0
32321	use appropriate pre-warmed selective agar support	0
32335	Edit protocol for specific cells	0
32353	Based on Lab activities requirements from YSBL	0
34001	Used by construct management	0
34013	Used by construct management	0
34021	Used by specific UI for recording Leeds fermentation batches	0
34059	Used by specific UI for sequencing	0
34099	Used by specific UI for sequencing	0
36001	Based on Lab activities requirements from OPPF	0
36019	Based on Lab activities requirements from OPPF	0
36031	Based on Lab activities requirements from OPPF	0
36059	Based on Lab activities requirements from OPPF	0
36065	Based on Lab activities requirements from OPPF	0
36111	Based on Lab activities requirements from OPPF	0
36121	Based on Lab activities requirements from STRUBI	0
36137	Based on Lab activities requirements from OPPF	0
36149	Based on Lab activities requirements from STRUBI	0
36217	Based on Lab activities requirements from STRUBI	0
36237	Based on Lab activities requirements from STRUBI	0
36257	Based on Lab activities requirements from OPPF	0
36283	Based on Lab activities requirements from OPPF	0
36297	Based on Lab activities requirements from OPPF	0
36315	Based on Lab activities requirements from OPPF	0
\.


--
-- TOC entry 2870 (class 0 OID 63206)
-- Dependencies: 1780
-- Data for Name: prot_refinputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refinputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
0	uL	Forward Primer	L	32025	32021	10039
0	uL	Reverse Primer	L	32027	32021	10085
3.0000000000000001e-006	uL	Forward Primer	L	32055	32051	10073
3.0000000000000001e-006	uL	Reverse Primer	L	32063	32059	10073
1.9999999999999999e-006	uL	PCR Product A	L	32071	32067	10063
1.9999999999999999e-006	uL	PCR Product B	L	32073	32067	10063
9.9999999999999995e-007	uL	Vector	L	32075	32067	10105
5.0000000000000002e-005	uL	Competent cells	L	32085	32081	10021
1.9999999999999999e-006	uL	Expression Vector A	L	32087	32081	10071
1.9999999999999999e-006	uL	Expression Vector B	L	32089	32081	10071
0.00029999999999999997	uL	Culture media	L	32091	32081	10023
\N	uL	Purified Protein A	L	32111	32107	10109
\N	uL	Purified Protein B	L	32113	32107	10109
0.025000000000000001	mL	Culture media	L	32131	32127	10023
2.5000000000000001e-005	uL	Antibiotic	L	32133	32127	10003
0.0001	uL	Innoculum	L	32135	32127	10111
9.9999999999999995e-007	uL	LIC vector	L	32147	32143	10105
1.9999999999999999e-006	uL	Insert	L	32149	32143	10063
9.9999999999999995e-007	uL	Chelating agent	L	32151	32143	10095
0.00014999999999999999	uL	Cells	L	32163	32159	10111
\N	uL	Insert	L	32177	32173	10063
1.9999999999999999e-006	uL	Buffer	L	32179	32173	10007
1.9999999999999999e-006	uL	dATP	L	32181	32173	10069
9.9999999999999995e-007	uL	Reducing agent	L	32183	32173	10095
\N	uL	Water	L	32185	32173	10097
3.9999999999999998e-007	uL	Polymerase	L	32187	32173	10033
5.0000000000000002e-005	uL	Competent cells	L	32197	32193	10021
1.9999999999999999e-006	uL	LIC-product	L	32199	32193	10063
0.0001	uL	Culture media	L	32201	32193	10023
8.0000000000000005e-009	ug	Vector	kg	32215	32211	10063
7.9999999999999996e-006	uL	Restriction enzyme	L	32217	32211	10029
1.5999999999999999e-005	uL	Restriction buffer	L	32219	32211	10007
1.9999999999999999e-006	uL	Polymerase buffer	L	32221	32211	10007
1.9999999999999999e-006	uL	dTTP	L	32223	32211	10069
9.9999999999999995e-007	uL	Reducing agent	L	32225	32211	10095
\N	uL	Water	L	32227	32211	10097
3.9999999999999998e-007	uL	Polymerase	L	32229	32211	10033
0.0050000000000000001	mL	Culture media	L	32249	32245	10023
5.0000000000000004e-006	uL	Antibiotic	L	32251	32245	10095
1.0000000000000001e-005	uL	Cells	L	32253	32245	10111
0.00055999999999999995	mL	Precipitant	L	32255	32245	10097
0.001	mL	Wash	L	32257	32245	10097
5.0000000000000002e-005	uL	Resuspension buffer	L	32259	32245	10007
5.0000000000000004e-006	uL	Buffer	L	32273	32269	10007
5.0000000000000004e-006	uL	dNTPs	L	32275	32269	10069
4.9999999999999998e-007	uL	Template	L	32277	32269	10063
9.9999999999999995e-007	uL	Forward primer	L	32279	32269	10039
9.9999999999999995e-007	uL	Reverse primer	L	32281	32269	10085
\N	uL	Water	L	32283	32269	10097
9.9999999999999995e-007	uL	Polymerase	L	32285	32269	10033
0.025000000000000001	mL	Culture media	L	32301	32297	10023
2.5000000000000001e-005	uL	Antibiotic	L	32303	32297	10003
2.5000000000000001e-005	uL	Cells	L	32305	32297	10111
0.0035000000000000001	mL	Precipitant	L	32307	32297	10097
0.002	mL	Wash	L	32309	32297	10097
5.0000000000000002e-005	uL	Resuspension buffer	L	32311	32297	10097
0.00014999999999999999	uL	Cells	L	32325	32321	10111
5.0000000000000002e-005	uL	Competent cells	L	32339	32335	10021
1.9999999999999999e-006	uL	Vector	L	32341	32335	10105
0.0001	uL	Culture media	L	32343	32335	10023
0.001	mL	Culture media	L	32357	32353	10023
9.9999999999999995e-007	uL	Antibiotic	L	32359	32353	10003
\N	uL	Reverse Primer	L	34003	34001	10085
\N	uL	Forward primer	L	34005	34001	10039
\N	uL	Plasmid	L	34007	34001	10071
\N	uL	Entry Clone	L	34015	34013	10071
\N	uL	Strain	L	34017	34013	10101
0	uL	Construct	L	34043	34021	10071
0	uL	Antibiotic A	L	34045	34021	10003
0	uL	Antibiotic B	L	34047	34021	10003
0	uL	Antibiotic C	L	34049	34021	10003
0	mL	Culture	L	34051	34021	10111
0.002	uL	Sample	L	34103	34099	10063
0.002	uL	Primer	L	34105	34099	10073
0.002	uL	Template	L	34107	34099	10063
0.002	uL	Water	L	34109	34099	10097
0.002	uL	Premix	L	34111	34099	10113
0	uL	Input Sample	L	36009	36001	10093
0	uL	Input Sample 1	L	36023	36019	10093
0	uL	Input Sample 2	L	36025	36019	10093
0	uL	Input Sample	L	36035	36031	10093
0	uL	Input Sample	L	36063	36059	10093
0	uL	Mounted Crystal	L	36067	36065	10061
0	uL	Plasmid	L	36093	36087	10071
0	uL	Plasmid	L	36103	36099	10071
0	uL	Input Sample	L	36115	36111	10093
\N	uL	Vector	L	36125	36121	10105
\N	uL	PCR product	L	36127	36121	10063
9.9999999999999995e-007	uL	Quick Ligase	L	36129	36121	10035
1.0000000000000001e-005	uL	Buffer-type	L	36131	36121	10007
0	uL	Input Sample	L	36141	36137	10093
9.9999999999999995e-007	uL	Template	L	36153	36149	10063
5.0000000000000004e-006	uL	Buffer-type	L	36155	36149	10007
2.4999999999999999e-007	uL	Enzyme	L	36157	36149	10033
9.9999999999999995e-007	uL	Forward Primer	L	36159	36149	10039
9.9999999999999995e-007	uL	Reverse Primer	L	36161	36149	10085
3.9999999999999998e-006	uL	Nucleotide	L	36163	36149	10069
\N	uL	Water	L	36165	36149	10097
0	uL	Plasmid	L	36199	36191	10071
0	uL	Plasmid	L	36209	36205	10071
\N	uL	PCR product	L	36221	36217	10063
4.9999999999999998e-007	uL	Restriction Enzyme PCR 1	L	36223	36217	10029
4.9999999999999998e-007	uL	Restriction Enzyme PCR 2	L	36225	36217	10029
\N	uL	Buffer-type	L	36227	36217	10007
\N	uL	Albumin	L	36229	36217	10013
\N	uL	Water	L	36231	36217	10097
2.0000000000000001e-009	ug	Vector	kg	36241	36237	10105
4.9999999999999998e-007	uL	Restriction Enzyme Vector 1	L	36243	36237	10029
4.9999999999999998e-007	uL	Restriction Enzyme Vector 2	L	36245	36237	10029
\N	uL	Buffer-type	L	36247	36237	10007
\N	uL	Albumin	L	36249	36237	10013
\N	uL	Water	L	36251	36237	10097
0	uL	Input Sample	L	36265	36257	10093
0	uL	Eukaryotic Pellet	L	36281	36277	10049
0	uL	Crystal Hit	L	36287	36283	10019
0	uL	Purified protein	L	36301	36297	10093
0	uL	Crystal Hits	L	36303	36297	10019
0	uL	Purified protein	L	36319	36315	10093
9.9999999999999995e-008	nL	Purified protein	L	38020	38018	10079
\.


--
-- TOC entry 2871 (class 0 OID 63209)
-- Dependencies: 1781
-- Data for Name: prot_refoutputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refoutputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
0	uL	Forward Primer	L	32003	32001	10039
0	uL	Reverse Primer	L	32005	32001	10085
0	uL	Template	L	32007	32001	10063
\N	uL	Primer	L	32023	32021	10073
3.0000000000000001e-006	uL	Forward Primer	L	32053	32051	10039
3.0000000000000001e-006	uL	Reverse Primer	L	32061	32059	10085
\N	uL	Vector	L	32069	32067	10071
\N	mL	Cells	L	32083	32081	10111
8.0000000000000007e-005	uL	Concentrated Protein	L	32109	32107	10079
\N	mL	Culture	L	32129	32127	10111
\N	mL	LIC-product	L	32145	32143	10063
\N	\N	Colonies	\N	32161	32159	10111
\N	mL	LIC insert	L	32175	32173	10063
\N	mL	Cells	L	32195	32193	10111
\N	mL	LIC vector	L	32213	32211	10063
4.9999999999999998e-008	uL	Purified plasmid	L	32247	32245	10063
\N	uL	PCR product	L	32271	32269	10063
\N	mL	Purified plasmid	L	32299	32297	10063
\N	\N	Colonies	\N	32323	32321	10111
\N	mL	Cells	L	32337	32335	10111
\N	mL	Culture	L	32355	32353	10111
0.050000000000000003	g	Tube 1	kg	34023	34021	10111
0.050000000000000003	g	Tube 2	kg	34025	34021	10111
0.050000000000000003	g	Tube 3	kg	34027	34021	10111
0.050000000000000003	g	Tube 4	kg	34029	34021	10111
0.050000000000000003	g	Tube 5	kg	34031	34021	10111
0.050000000000000003	g	Tube 6	kg	34033	34021	10111
0.050000000000000003	g	Tube 7	kg	34035	34021	10111
0.050000000000000003	g	Tube 8	kg	34037	34021	10111
0.050000000000000003	g	Tube 9	kg	34039	34021	10111
0.050000000000000003	g	Tube 10	kg	34041	34021	10111
\N	uL	DNA	L	34061	34059	10063
\N	uL	DNA	L	34101	34099	10063
0	uL	Output Sample 1	L	36003	36001	10093
0	uL	Output Sample 2	L	36005	36001	10093
0	uL	Output Sample 3	L	36007	36001	10093
0	uL	Output Sample	L	36021	36019	10093
0	uL	Concentrated protein	L	36033	36031	10093
0	uL	Forward Primer	L	36045	36043	10039
0	uL	Reverse Primer	L	36047	36043	10085
0	uL	Template	L	36049	36043	10063
0	uL	Output Sample	L	36061	36059	10093
0	uL	Eukaryotic Supernatant	L	36089	36087	10093
0	uL	Eukaryotic Pellet	L	36091	36087	10049
0	uL	Protein	L	36101	36099	10109
0	uL	Output Sample	L	36113	36111	10093
\N	uL	Plasmid	L	36123	36121	10071
0	uL	Output Sample	L	36139	36137	10093
\N	uL	PCR product	L	36151	36149	10063
0	uL	Extracellular Protein	L	36193	36191	10093
0	uL	Intracellular Protein	L	36195	36191	10093
0	uL	Membrane Pellet	L	36197	36191	10049
0	uL	Protein	L	36207	36205	10109
\N	uL	PCR product	L	36219	36217	10063
\N	uL	Vector	L	36239	36237	10105
0	uL	Output Sample 1	L	36259	36257	10093
0	uL	Output Sample 2	L	36261	36257	10093
0	uL	Output Sample 3	L	36263	36257	10093
0	uL	Eukaryotic Supernatant	L	36279	36277	10093
0	uL	Mounted Crystal	L	36285	36283	10061
0	uL	Crystal Hits	L	36299	36297	10019
0	uL	Crystal Hits	L	36317	36315	10019
9.9999999999999995e-008	nL	Trial Drop	L	38019	38018	38012
\.


--
-- TOC entry 2872 (class 0 OID 63212)
-- Dependencies: 1782
-- Data for Name: ref_abstractholdertype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_abstractholdertype (name, publicentryid) FROM stdin;
CrystalQuick plate, round wells	28001
CrystalQuick plate, square wells	28003
CrystalQuick plate, low profile plate	28005
96 well IMP@CT plate for Protein crystallisation	28007
CrystalQuick plate, square wells, Low Birefringence	28009
Cellstar 24 well plate	28011
Cryschem plate	28013
96 well PCR plate half skirted	28015
Thermo-Fast 96 skirted	28017
0.1ml 96-well, thin wall plates (lower profile, skirted)	28019
48 well 6.0ml storage plate	28021
Falcon Multiwell 24 well plate	28023
CombiClover plate	28025
CompactClover plate	28027
CombiClover Junior plate	28029
96 round bottom (Crystal Ex)	28031
96 well rd-bottom	28033
24-well comboplate 	28035
Intelliplate	28037
96 well hanging drop	28039
24-well vdxm	28041
48 well vdx	28043
Linbro	28045
Crystal Clear Strips D/1	28047
Crystal Clear Strips P/1	28049
96 deep well	28051
card	28053
\.


--
-- TOC entry 2873 (class 0 OID 63215)
-- Dependencies: 1783
-- Data for Name: ref_componentcategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_componentcategory (name, publicentryid) FROM stdin;
Buffering agent	12001
Chelator	12003
Cryo coolant	12005
CSI	12007
Detergent	12009
Gas	12011
Heavy atom compound	12013
Metal	12015
Nucleation suppressant	12017
Organic	12019
PCR Product	12021
pH conjugate	12023
Precipitant	12025
Reducing agent	12027
Oxidising agent	12029
Salt	12031
Solvent	12033
Vector	12035
Construct	12037
Plasmid	12039
Competent cell	12041
Primer	12043
Inducer	12045
Inhibitor	12047
Additive	12049
Antibiotic	12051
Catalyst	12053
Co-factor	12055
Cross linker	12057
Ligand	12059
Lipid	12061
Modifying agent	12063
Substrate	12065
Enzyme	12067
Nucleotide	12069
Preservative	12071
Sugar	12073
StainsAndDyes	12075
Denaturant	12077
Oligonucleotide	12079
Amino acid	12081
Vitamin	12083
Final Protein	12085
Expressed Protein	12087
Template	12089
Protein	12091
Nucleic acid	12093
Forward Extension	12095
Reverse Extension	12097
\.


--
-- TOC entry 2874 (class 0 OID 63218)
-- Dependencies: 1784
-- Data for Name: ref_crystaltype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_crystaltype (ressubpos, holdertypeid) FROM stdin;
\.


--
-- TOC entry 2875 (class 0 OID 63221)
-- Dependencies: 1785
-- Data for Name: ref_dbname; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_dbname (fullname, name, url, publicentryid) FROM stdin;
CATH Protein Structure Classification	CATH	http://cathwww.biochem.ucl.ac.uk/latest/index.html	20001
Clusters of orthologous groups of proteins	COG	http://www.ncbi.nlm.nih.gov/COG 	20003
DDBJ-DNA Data Bank of Japan	DDBJ	http://www.ddbj.nig.ac.jp 	20005
Genomes at the EBI	EBI Genomes	http://www.ebi.ac.uk/genomes 	20007
Eukaryote Gene Orthologs	EGO	http://www.tigr.org/tdb/tgi/ego/ 	20009
EMBL-Nucleotide Sequence Database	EMBL-Bank	http://www.ebi.ac.uk/embl.html	20011
EMBL Coding sequences	EMBLCDS	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-id+4Flds1F1PA4+-lib+EMBLCDS	20013
EMBL Contig database	EMBLCON	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-id+4Flds1F1PA4+-lib+EMBLCON	20015
NCBI gene-specific information database	Entrez Gene	http://www.ncbi.nih.gov/entrez/query.fcgi?db=gene	20017
NCBI Entrez Genome Project database	Entrez Genomes	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Genome	20019
Entrez Protein database	Entrez-Protein	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Protein	20021
EXProt (database for EXPerimentally verified Protein functions)	EXProt	http://www.cmbi.kun.nl/EXProt/ 	20023
Entrez Nucleotides database	GenBank	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Nucleotide	20025
Gene ontology consortium database	GO	http://www.geneontology.org/ 	20027
Gene Ontology Annotation project	GOA	http://www.ebi.ac.uk/GOA 	20029
Human Genome Variation Database	HGVBase	http://hgvbase.cgb.ki.se/	20031
Integrated resource of Protein Families, Domains and Sites	InterPro	http://www.ebi.ac.uk/interpro/	20033
International Protein Index	IPI	http://www.ebi.ac.uk/IPI/IPIhelp.html	20035
International Union of Biochemistry and Molecular Biology Nomenclature database	IUBMB Nomenclature database	http://www.chem.qmul.ac.uk/iubmb 	20037
International Union of Pure and Applied Chemistry Nomenclature database	IUPAC Nomenclature database	http://www.chem.qmul.ac.uk/iupac 	20039
Kyoto encyclopedia of genes and genomes	KEGG	http://www.genome.jp/kegg 	20041
Molecular Modeling Database (MMDB)	MMDB	http://www.ncbi.nlm.nih.gov/Structure 	20043
EBI Macromolecular Structure Database (EMSD)	MSD	http://www.ebi.ac.uk/msd 	20045
NCBI Taxonomy database	NCBI Taxonomy	http://www.ncbi.nlm.nih.gov/Taxonomy/ 	20047
The PANTHER (Protein ANalysis THrough Evolutionary Relationships) Classification System	PANTHER	http://panther.celera.com/ 	20049
European Patent Abstracts	Patent Abstracts	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-lib+PATABS	20051
RCSB ProteinData Bank	PDB	http://www.rcsb.org/pdb 	20053
PDB-Ligand database	PDB-Ligand	http://www.idrtech.com/PDB-Ligand/ 	20055
Protein Data Bank of Transmembrane Proteins	PDBTM	http://pdbtm.enzim.hu/	20057
Protein Extraction, Description and ANalysis Tool	PEDANT	http://pedant.gsf.de/ 	20059
PubMed	PubMed	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=PubMed	20061
NCBI Reference Sequences	RefSeq	http://ncbi.nih.gov/RefSeq/	20063
Structural classification of protein database	SCOP	http://scop.mrc-lmb.cam.ac.uk/scop 	20065
Superfamily HMM library and genome assignments server	SUPERFAMILY	http://supfam.org/ 	20067
Swiss-Prot protein knowledgebase	Swiss-Prot	http://www.expasy.org/sprot 	20069
Target registration database	TargetDB	http://targetdb.pdb.org/ 	20071
Transport Classification database	TCDB	http://www.tcdb.org/	20073
The Institute for Genomic Research Comprehensive Microbial Resource	TIGR CMR	http://cmr.tigr.org/tigr-scripts/CMR/CmrHomePage.cgi	20075
The Institute for Genomic Research Gene Indices	TIGR GI	http://www.tigr.org/tdb/tgi.shtml 	20077
The Institute for Genomic Research Microbial resource	TIGR MDB	http://www.tigr.org/tdb/mdb/mdbcomplete.html 	20079
Topology of protein structures database	TOPS	http://www.tops.leeds.ac.uk 	20081
Genomic Comparisons of Membrane transport Systems	TransportDB	http://www.membranetransport.org/ 	20083
UniProt Archive	UniParc	http://www.ebi.uniprot.org/uniprot-srv/uniParcSearch.do?pager.offset=	20085
The Universal Protein Resource	UniProt	http://www.uniprot.org/ 	20087
UniProt Knowledgebase	UniProtKB	http://www.ebi.uniprot.org/uniprot-srv/index.do	20089
UniProt Reference Clusters	UniRef	http://www.ebi.uniprot.org/uniprot-srv/uniRefSearch.do	20091
UniVec database	UniVec	http://www.ncbi.nlm.nih.gov/VecScreen/UniVec.html 	20093
Vector database	VectorDB	http://genome-www2.stanford.edu/vectordb/ 	20095
unspecified	unspecified	\N	20097
\.


--
-- TOC entry 2876 (class 0 OID 63227)
-- Dependencies: 1786
-- Data for Name: ref_experimenttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_experimenttype (name, publicentryid) FROM stdin;
SPOTConstructDesign	1
Entry clone	3
Deep-Frozen culture	5
PCR	7
Cloning	9
Digest	11
Recombination	13
LIC annealing	15
LIC polymerase reaction	17
Transformation	19
Ligation	21
Cloning design	23
Primer design	25
Optic Construct Design	27
Expression	29
Small scale expression	31
Production scale expression	33
Purification	35
DNA purification	37
Mini prep	39
Plasmid prep	41
Protein purification	43
Chromatography	45
Proteolysis	47
Refold	49
Dialysis	51
Concentration	53
Fractionation	55
Lysis	57
Protein production summary	59
Membrane preparation	61
Crystallogenesis	63
Drop set-up	65
Native trials	67
Selmet trials	69
Trials	71
Drop scoring	73
Drop imaging	75
Native crystal	77
Selmet crystal	79
Crystal	81
Native optimization	83
Selmet optimization	85
Mount crystal	87
Crystallization plate inspection	89
Crystallography	91
Diffraction	93
Native diffraction	95
Selmet diffraction	97
Data set	99
Crystal Structure	101
NMR	103
Preparative experiment	105
Buffer prep	107
Solution prep	109
Culture	111
Import sample	113
Fermentation	115
Characterisation	117
Gel	119
OD measurement	121
Light scattering	123
Mass spec	125
Dish culture	127
Other	129
Annotation	131
Information	133
PDB	135
Plate check	137
Unspecified	139
Order	141
Fermenter culture	143
Cleavage	145
Sequencing order	147
Sequencing	149
Complexation	151
\.


--
-- TOC entry 2877 (class 0 OID 63230)
-- Dependencies: 1787
-- Data for Name: ref_hazardphrase; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_hazardphrase (classification, code, phrase, publicentryid) FROM stdin;
MSDS	R1	Explosive when dry.	18001
MSDS	R2	Risk of explosion by shock, friction, fire or other sources of ignition.	18002
MSDS	R3	Extreme risk of explosion by shock, friction, fire or other sources of ignition.	18003
MSDS	R4	Forms very sensitive explosive metallic compounds.	18004
MSDS	R5	Heating may cause an explosion.	18005
MSDS	R6	Explosive with or without contact with air.	18006
MSDS	R7	May cause fire.	18007
MSDS	R8	Contact with combustible material may cause fire.	18008
MSDS	R9	Explosive when mixed with combustible material.	18009
MSDS	R10	Flammable.	18010
MSDS	R11	Highly flammable.	18011
MSDS	R12	Extremely flammable.	18012
MSDS	R13	Extremely flammable liquefied gas.	18013
MSDS	R14	Reacts violently with water.	18014
MSDS	R14/15	Reacts violently with water, liberating extremely flammable gases.	18015
MSDS	R15	Contact with water liberates extremely flammable gases.	18016
MSDS	R15/29	Contact with water liberates toxic, extremely flammable gas.	18017
MSDS	R16	Explosive when mixed with oxidising substances.	18018
MSDS	R17	Spontaneously flammable in air.	18019
MSDS	R18	In use, may form flammable/explosive vapour air-mixture.	18020
MSDS	R19	May form explosive peroxides.	18021
MSDS	R20	Harmful by inhalation.	18022
MSDS	R20/21	Harmful by inhalation and in contact with skin.	18023
MSDS	R20/21/22	Harmful by inhalation, in contact with skin and if swallowed.	18024
MSDS	R20/22	Harmful by inhalation and if swallowed.	18025
MSDS	R21	Harmful in contact with skin.	18026
MSDS	R21/22	Harmful in contact with skin and if swallowed.	18027
MSDS	R22	Harmful if swallowed.	18028
MSDS	R23	Toxic by inhalation.	18029
MSDS	R23/24	Toxic by inhalation and in contact with skin.	18030
MSDS	R23/24/25	Toxic by inhalation, in contact with skin and if swallowed.	18031
MSDS	R23/25	Toxic by inhalation and if swallowed.	18032
MSDS	R24	Toxic in contact with skin.	18033
MSDS	R24/25	Toxic in contact with skin and if swallowed.	18034
MSDS	R25	Toxic if swallowed.	18035
MSDS	R26	Very toxic by inhalation.	18036
MSDS	R26/27	Very toxic by inhalation and in contact with skin.	18037
MSDS	R26/27/28	Very toxic by inhalation, in contact with skin and if swallowed.	18038
MSDS	R26/28	Very toxic by inhalation and if swallowed.	18039
MSDS	R27	Very toxic in contact with skin.	18040
MSDS	R27/28	Very toxic in contact with skin and if swallowed.	18041
MSDS	R28	Very toxic if swallowed.	18042
MSDS	R29	Contact with water liberates toxic gas.	18043
MSDS	R30	Can become highly flammable in use.	18044
MSDS	R31	Contact with acids liberates toxic gas.	18045
MSDS	R32	Contact with acids liberates very toxic gas.	18046
MSDS	R33	Danger of cumulative effects.	18047
MSDS	R34	Causes burns.	18048
MSDS	R35	Causes severe burns.	18049
MSDS	R36	Irritating to eyes.	18050
MSDS	R36/37	Irritating to eyes and respiratory system.	18051
MSDS	R36/37/38	Irritating to eyes, respiratory system and skin.	18052
MSDS	R36/38	Irritating to eyes and skin.	18053
MSDS	R37	Irritating to respiratory system.	18054
MSDS	R37/38	Irritating to respiratory system and skin.	18055
MSDS	R38	Irritating to skin.	18056
MSDS	R39	Danger of very serious irreversible effects.	18057
MSDS	R39/23	Toxic: danger of very serious irreversible effects through inhalation.	18058
MSDS	R39/23/24	Toxic: danger of very serious irreversible effects through inhalation and in contact with skin.	18059
MSDS	R39/23/24/25	Toxic: danger of very serious irreversible effects through inhalation, in contact with skin and if swallowed.	18060
MSDS	R39/23/25	Toxic: danger of very serious irreversible effects through inhalation and if swallowed.	18061
MSDS	R39/24	Toxic: danger of very serious irreversible effects in contact with skin.	18062
MSDS	R39/24/25	Toxic: danger of very serious irreversible effects in contact with skin and if swallowed.	18063
MSDS	R39/25	Toxic: danger of very serious irreversible effects if swallowed.	18064
MSDS	R39/26	Very toxic: danger of very serious irreversible effects through inhalation.	18065
MSDS	R39/26/27	Very toxic: danger of very serious irreversible effects through inhalation and in contact with skin.	18066
MSDS	R39/26/27/28	Very toxic: danger of very serious irreversible effects through inhalation, in contact with skin and if swallowed.	18067
MSDS	R39/26/28	Very toxic: danger of very serious irreversible effects through inhalation and if swallowed.	18068
MSDS	R39/27	Very toxic: danger of very serious irreversible effects in contact with skin.	18069
MSDS	R39/27/28	Very toxic: danger of very serious irreversible effects in contact with skin and if swallowed.	18070
MSDS	R39/28	Very toxic: danger of very serious irreversible effects if swallowed.	18071
MSDS	R40	Limited evidence of a carcinogenic effect.	18072
MSDS	R40/20	Harmful: possible risk of irreversible effects through inhalation.	18073
MSDS	R40/20/21	Harmful: possible risk of irreversible effects through inhalation and in contact with skin.	18074
MSDS	R40/20/21/22	Harmful: possible risk of irreversible effects through inhalation, in contact with skin and if swallowed.	18075
MSDS	R40/20/22	Harmful: possible risk of irreversible effects through inhalation and if swallowed.	18076
MSDS	R40/21	Harmful: possible risk of irreversible effects in contact with skin.	18077
MSDS	R40/21/22	Harmful: possible risk of irreversible effects in contact with skin and if swallowed.	18078
MSDS	R40/22	Harmful: possible risk of irreversible effects if swallowed.	18079
MSDS	R41	Risk of serious damage to eyes.	18080
MSDS	R42	May cause sensitization by inhalation.	18081
MSDS	R42/43	May cause sensitization by inhalation and skin contact.	18082
MSDS	R43	May cause sensitization by skin contact.	18083
MSDS	R44	Risk of explosion if heated under confinement.	18084
MSDS	R45	May cause cancer.	18085
MSDS	R46	May cause heritable genetic damage.	18086
MSDS	R47	May cause birth defects.	18087
MSDS	R48	Danger of serious damage to health by prolonged exposure.	18088
MSDS	R48/20	Harmful: danger of serious damage to health by prolonged exposure through inhalation.	18089
MSDS	R48/20/21	Harmful: danger of serious damage to health by prolonged exposure through inhalation and in contact with skin.	18090
MSDS	R48/20/21/22	Harmful: danger of serious damage to health by prolonged exposure through inhalation, in contact with skin and if swallowed.	18091
MSDS	R48/20/22	Harmful: danger of serious damage to health by prolonged exposure through inhalation and if swallowed.	18092
MSDS	R48/21	Harmful: danger of serious damage to health by prolonged exposure in contact with skin.	18093
MSDS	R48/21/22	Harmful: danger of serious damage to health by prolonged exposure in contact with skin and if swallowed.	18094
MSDS	R48/22	Harmful: danger of serious damage to health by prolonged exposure if swallowed.	18095
MSDS	R48/23	Toxic: danger of serious damage to health by prolonged exposure through inhalation.	18096
MSDS	R48/23/24	Toxic: danger of serious damage to health by prolonged exposure through inhalation and in contact with skin.	18097
MSDS	R48/23/24/25	Toxic: danger of serious damage to health by prolonged exposure through inhalation, in contact with skin and if swallowed.	18098
MSDS	R48/23/25	Toxic: danger of serious damage to health by prolonged exposure through inhalation and if swallowed.	18099
MSDS	R48/24	Toxic: danger of serious damage to health by prolonged exposure in contact with skin.	18100
MSDS	R48/24/25	Toxic: danger of serious damage to health by prolonged exposure in contact with skin and if swallowed.	18101
MSDS	R48/25	Toxic: danger of serious damage to health by prolonged exposure if swallowed.	18102
MSDS	R49	May cause cancer by inhalation.	18103
MSDS	R50	Very toxic to aquatic organisms.	18104
MSDS	R50/53	Very toxic to aquatic organisms, may cause long-term adverse effects in the aquatic environment.	18105
MSDS	R51	Toxic to aquatic organisms.	18106
MSDS	R51/53	Toxic to aquatic organisms, may cause long-term adverse effects in the aquatic environment.	18107
MSDS	R52	Harmful to aquatic organisms.	18108
MSDS	R52/53	Harmful to aquatic organisms, may cause long-term adverse effects in the aquatic environment.	18109
MSDS	R53	May cause long-term adverse effects in the aquatic environment.	18110
MSDS	R54	Toxic to flora.	18111
MSDS	R55	Toxic to fauna.	18112
MSDS	R56	Toxic to soil organisms.	18113
MSDS	R57	Toxic to bees.	18114
MSDS	R58	May cause long-term adverse effects in the environment.	18115
MSDS	R59	Dangerous for the ozone layer.	18116
MSDS	R60	May impair fertility.	18117
MSDS	R61	May cause harm to the unborn child.	18118
MSDS	R62	Possible risk of impaired fertility.	18119
MSDS	R63	Possible risk of harm to the unborn child.	18120
MSDS	R64	May cause harm to breast-fed babies.	18121
MSDS	R65	Harmful: may cause lung damage if swallowed.	18122
MSDS	R66	Repeated exposure may cause skin dryness or cracking.	18123
MSDS	R67	Vapours may cause drowsiness and dizziness.	18124
MSDS	R68	Possible risks of irreversible effects.	18125
MSDS	R68/20	Harmful: possible risk of irreversible effects through inhalation.	18126
MSDS	R68/20/21	Harmful: possible risk of irreversible effects through inhalation and in contact with skin.	18127
MSDS	R68/20/21/22	Harmful: possible risk of irreversible effects through inhalation, in contact with skin and if swallowed.	18128
MSDS	R68/20/22	Harmful: possible risk of irreversible effects through inhalation and if swallowed.	18129
MSDS	R68/21	Harmful: possible risk of irreversible effects in contact with skin.	18130
MSDS	R68/21/22	Harmful: possible risk of irreversible effects in contact with skin and if swallowed.	18131
MSDS	R68/22	Harmful: possible risk of irreversible effects if swallowed.	18132
MSDS	S1	Keep locked up.	18133
MSDS	S1/2	Keep locked up and out of the reach of children.	18134
MSDS	S2	Keep out of the reach of children.	18135
MSDS	S3	Keep in a cool place.	18136
MSDS	S3/7	Keep container tightly closed in a cool place.	18137
MSDS	S3/7/9	Keep container tightly closed in a cool, well-ventilated place.	18138
MSDS	S3/9	Keep in a cool, well-ventilated place.	18139
MSDS	S3/9/14	Keep in a cool, well-ventilated place away from (incompatible materials to be indicated by the manufacturer).	18140
MSDS	S3/9/14/49	Keep only in the original container in a cool, well-ventilated place away from ... (incompatible materials to be indicated by the manufacturer).	18141
MSDS	S3/9/49	Keep only in original container in a cool, well-ventilated place.	18142
MSDS	S3/14	Keep in a cool place away from ... (incompatible materials to be specified by the manufacturer).	18143
MSDS	S4	Keep away from living quarters.	18144
MSDS	S5	Keep contents under ... (appropriate liquid to be specified by the manufacturer).	18145
MSDS	S6	Keep under ... (inert gas to be specified by the manufacturer).	18146
MSDS	S7	Keep container tightly closed.	18147
MSDS	S7/8	Keep container tightly closed and dry.	18148
MSDS	S7/9	Keep container tightly closed and in a well-ventilated place.	18149
MSDS	S7/47	Keep container tightly closed and at a temperature not exceeding ... (to be specified by the manufacturer).	18150
MSDS	S8	Keep container dry.	18151
MSDS	S9	Keep container in a well-ventilated place.	18152
MSDS	S10	---	18153
MSDS	S11	---	18154
MSDS	S12	Do not keep the container sealed.	18155
MSDS	S13	Keep away from food, drink and animal feedingstuffs.	18156
MSDS	S14	Keep away from ... (incompatible materials to be indicated by the manufacturer).	18157
MSDS	S15	Keep away from heat.	18158
MSDS	S16	Keep away from sources of ignition -- No smoking.	18159
MSDS	S17	Keep away from combustible material.	18160
MSDS	S18	Handle and open container with care.	18161
MSDS	S19	---	18162
MSDS	S20	When using do not eat or drink.	18163
MSDS	S20/21	When using do not eat, drink or smoke.	18164
MSDS	S21	When using do not smoke.	18165
MSDS	S22	Do not breathe dust.	18166
MSDS	S23	Do not breathe gas/fumes/vapour/spray (appropriate wording to be specified by the manufacturer).	18167
MSDS	S24	Avoid contact with the skin.	18168
MSDS	S24/25	Avoid contact with skin and eyes.	18169
MSDS	S25	Avoid contact with eyes.	18170
MSDS	S26	In case of contact with eyes, rinse immediately with plenty of water and seek medical advice.	18171
MSDS	S27	Take off immediately all contaminated clothing.	18172
MSDS	S27/28	After contact with skin, take off immediately all contaminated clothing, and wash immediately with plenty of (to be specified by the manufacturer).	18173
MSDS	S28	After contact with skin, wash immediately with plenty of ... (to be specified by the manufacturer).	18174
MSDS	S29	Do not empty into drains.	18175
MSDS	S29/35	Do not empty into drains; dispose of this material and its container in a safe way.	18176
MSDS	S29/56	Do not empty into drains, dispose of this material and its container at hazardous or special waste collection point.	18177
MSDS	S30	Never add water to this product.	18178
MSDS	S31	---	18179
MSDS	S32	---	18180
MSDS	S33	Take precautionary measures against static discharges.	18181
MSDS	S34	Avoid shock and friction.	18182
MSDS	S35	This material and its container must be disposed of in a safe way.	18183
MSDS	S36	Wear suitable protective clothing.	18184
MSDS	S36/37	Wear suitable protective clothing and gloves.	18185
MSDS	S36/37/39	Wear suitable protective clothing, gloves and eye/face protection.	18186
MSDS	S36/39	Wear suitable protective clothing and eye/face protection.	18187
MSDS	S37	Wear suitable gloves.	18188
MSDS	S37/39	Wear suitable gloves and eye/face protection.	18189
MSDS	S38	In case of insufficient ventilation, wear suitable respiratory equipment.	18190
MSDS	S39	Wear eye/face protection.	18191
MSDS	S40	To clean the floor and all objects contaminated by this material, use ... (to be specified by the manufacturer).	18192
MSDS	S41	In case of fire and/or explosion do not breathe fumes.	18193
MSDS	S42	During fumigation/spraying wear suitable respiratory equipment (appropriate wording to specified by the manufacturer).	18194
MSDS	S44	If you feel unwell, seek medical advice (show label where possible).	18195
MSDS	S45	In case of accident or if you feel unwell, seek medical advice immediately (show the label where possible).	18196
MSDS	S46	If swallowed, seek medical advice immediately and show container or label.	18197
MSDS	S47	Keep at temperature not exceeding ... (to be specified by the manufacturer).	18198
MSDS	S47/49	Keep only in the original container at a temperature not exceeding ... (to be specified by the manufacturer).	18199
MSDS	S48	Keep wet with ... (appropriate material to be specified by the manufacturer).	18200
MSDS	S49	Keep only in the original container.	18201
MSDS	S50	Do not mix with ... (to be specified by the manufacturer).	18202
MSDS	S51	Use only in well-ventilated areas.	18203
MSDS	S52	Not recommended for interior use on large surface areas.	18204
MSDS	S53	Avoid exposure -- obtain special instructions before use.	18205
MSDS	S54	---	18206
MSDS	S55	---	18207
MSDS	S56	Dispose of this material and its container to hazardous or special waste collection point.	18208
MSDS	S57	Use appropriate container to avoid environmental contamination.	18209
MSDS	S58	---	18210
MSDS	S59	Refer to manufacturer/supplier for information on recovery/ recycling.	18211
MSDS	S60	This material and its container must be disposed of as hazardous waste.	18212
MSDS	S61	Avoid release to the environment. Refer to special instructions/safety data sheets.	18213
MSDS	S62	If swallowed, do not induce vomiting: seek medical advice immediately and show this container or label.	18214
MSDS	S63	In case of accident by inhalation: remove casualty to fresh air and keep at rest.	18215
MSDS	S64	If swallowed, rinse mouth with water (only if the person is conscious).	18216
MSDS	E	Explosive	18217
MSDS	F+	Extremely flammable	18218
MSDS	F	Highly flammable	18219
MSDS	T+	Very toxic	18220
MSDS	T	Toxic	18221
MSDS	Xn	Harmful	18222
MSDS	C	Corrosive	18223
MSDS	Xi	Irritant	18224
MSDS	N	Dangerous for the Environment	18225
MSDS	O	Oxidizing	18226
\.


--
-- TOC entry 2878 (class 0 OID 63233)
-- Dependencies: 1788
-- Data for Name: ref_holdercategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_holdercategory (name, publicentryid) FROM stdin;
96 well plate	26001
48 well plate	26003
24 well plate	26005
48 deep well plate	26007
96 deep well plate	26009
card	26011
Box	26013
Clone saver card	26015
Screen	38013
Additive	38014
Optimization	38015
SparseMatrix	38016
TrialPlate	38017
\.


--
-- TOC entry 2879 (class 0 OID 63236)
-- Dependencies: 1789
-- Data for Name: ref_holdertype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_holdertype (holdholderflag, maxcolumn, maxrow, maxsubposition, maxvolume, maxvolumedisplayunit, abstractholdertypeid, defaultscheduleplanid) FROM stdin;
\N	12	8	3	320	uL	28001	\N
\N	12	8	3	320	uL	28003	\N
\N	12	8	1	320	uL	28005	\N
\N	12	8	0	10	uL	28007	\N
\N	12	8	3	320	uL	28009	\N
\N	6	4	0	1.5	mL	28011	\N
\N	6	4	1	1.5	mL	28013	\N
\N	12	8	0	200	uL	28015	\N
\N	12	8	0	200	uL	28017	\N
\N	12	8	0	100	uL	28019	\N
\N	6	8	0	6	mL	28021	\N
\N	6	4	0	3.5	mL	28023	\N
\N	6	4	4	1.5	mL	28025	\N
\N	12	8	4	250	uL	28027	\N
\N	6	4	4	0.5	mL	28029	\N
\N	12	8	1	210	uL	28031	\N
\N	12	8	0	360	uL	28033	\N
\N	6	4	0	3.2999999999999998	mL	28035	\N
\N	12	8	2	300	uL	28037	\N
\N	12	8	0	\N	\N	28039	\N
\N	6	4	0	0.5	mL	28041	\N
\N	8	6	0	0.29999999999999999	mL	28043	\N
\N	6	4	0	1	mL	28045	\N
\N	12	8	1	100	uL	28047	\N
\N	12	8	1	100	uL	28049	\N
\N	12	8	0	2	mL	28051	\N
\N	10	10	0	100	uL	28053	\N
\.


--
-- TOC entry 2909 (class 0 OID 65064)
-- Dependencies: 1820
-- Data for Name: ref_imagetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_imagetype (publicentryid, ylengthperpixel, xlengthperpixel, sizey, name, sizex, catorgory, colourdepth, url) FROM stdin;
38001	2.8570000000000002	2.8570000000000002	700	Oasis 750*700	750	composite	8	http://www.oppf.ox.ac.uk/vault/images/lowres/
38002	2.9790000000000001	2.9790000000000001	960	RI 1280*960	1280	composite	8	http://www.oppf.ox.ac.uk/vault/images/lowres/
38003	1.587	1.587	108	RI 691*108	691	composite	8	http://www.oppf.ox.ac.uk/vault/images/lowres/
38004	1.587	1.587	960	Pixera	1260	zoomed	256	http://www.oppf.ox.ac.uk/vault/images/micro/
\.


--
-- TOC entry 2880 (class 0 OID 63239)
-- Dependencies: 1790
-- Data for Name: ref_instrumenttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_instrumenttype (name, publicentryid) FROM stdin;
\.


--
-- TOC entry 2881 (class 0 OID 63242)
-- Dependencies: 1791
-- Data for Name: ref_organism; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_organism (atccnumber, cellline, celllocation, celltype, fraction, genemnemonic, genus, ictvcode, name, ncbitaxonomyid, organ, organelle, organismacronym, plasmid, plasmiddetails, scientificname, secretion, species, strain, subvariant, tissue, variant, publicentryid) FROM stdin;
\N	\N	\N	\N	\N	\N	\N	\N	Sulfolobus solfataricus P2	273057	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6001
\N	\N	\N	\N	\N	\N	\N	\N	Pseudomonas syringae pv. phaseolicola	319	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6003
\N	\N	\N	\N	\N	\N	\N	\N	Sphingomonas paucimobilis	13689	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6005
\N	\N	\N	\N	\N	\N	\N	\N	Bacteroides fragilis	817	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6007
\N	\N	\N	\N	\N	\N	\N	\N	Streptomyces coelicolor	1902	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6009
\N	\N	\N	\N	\N	\N	\N	\N	Escherichia coli	562	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6011
\N	\N	\N	\N	\N	\N	\N	\N	Bordetella bronchiseptica	518	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6013
\N	\N	\N	\N	\N	\N	\N	\N	Sulfolobus islandicus rudivirus 1 variant XX	282066	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6015
\N	\N	\N	\N	\N	\N	\N	\N	Pectobacterium chrysanthemi	556	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6017
\N	\N	\N	\N	\N	\N	\N	\N	Bacillus anthracis	1392	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6019
\N	\N	\N	\N	\N	\N	\N	\N	Geobacter sulfurreducens PCA	243231	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6021
\N	\N	\N	\N	\N	\N	\N	\N	Staphylococcus epidermidis	1282	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6023
\N	\N	\N	\N	\N	\N	\N	\N	Sulfolobus solfataricus	2287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6025
\N	\N	\N	\N	\N	\N	\N	\N	Streptomyces cattleya	29303	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6027
\N	\N	\N	\N	\N	\N	\N	\N	Arabidopsis thaliana	3702	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6029
\N	\N	\N	\N	\N	\N	\N	\N	Caenorhabditis elegans	6239	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6031
\N	\N	\N	\N	\N	\N	\N	\N	Danio rerio	7955	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6033
\N	\N	\N	\N	\N	\N	\N	\N	Dictyostelium discoideum	44689	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6035
\N	\N	\N	\N	\N	\N	\N	\N	Drosophila melanogaster	7227	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6037
\N	\N	\N	\N	\N	\N	\N	\N	Homo sapiens	9606	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6039
\N	\N	\N	\N	\N	\N	\N	\N	Mus musculus	10090	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6041
\N	\N	\N	\N	\N	\N	\N	\N	Mycoplasma pneumoniae	2104	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6043
\N	\N	\N	\N	\N	\N	\N	\N	Oryza sativa	4530	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6045
\N	\N	\N	\N	\N	\N	\N	\N	Plasmodium falciparum	5833	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6047
\N	\N	\N	\N	\N	\N	\N	\N	Pongo pygmaeus	9600	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6049
\N	\N	\N	\N	\N	\N	\N	\N	Pseudomonas aeruginosa	287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6051
\N	\N	\N	\N	\N	\N	\N	\N	Rattus norvegicus	10116	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6053
\N	\N	\N	\N	\N	\N	\N	\N	Saccharomyces cerevisiae	4932	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6055
\N	\N	\N	\N	\N	\N	\N	\N	Salmonella typhimurium LT2	99287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6057
\N	\N	\N	\N	\N	\N	\N	\N	Schizosaccharomyces pombe	4896	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6059
\N	\N	\N	\N	\N	\N	\N	\N	Staphylococcus aureus	1280	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6061
\N	\N	\N	\N	\N	\N	\N	\N	Thermotoga maritima	2336	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6063
\N	\N	\N	\N	\N	\N	\N	\N	Xenopus laevis	8355	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6065
\N	\N	\N	\N	\N	\N	\N	\N	Zea mays	4577	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6067
\N	\N	\N	\N	\N	\N	\N	\N	Bacillus subtilis	1423	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6069
\N	\N	\N	\N	\N	\N	\N	\N	Nematostella vectensis	45351	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6071
\N	\N	\N	\N	\N	\N	\N	\N	Neisseria gonorrhoeae	 485	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6073
\.


--
-- TOC entry 2882 (class 0 OID 63248)
-- Dependencies: 1792
-- Data for Name: ref_pintype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_pintype (length, looplength, looptype, wirewidth, abstractholdertypeid) FROM stdin;
\.


--
-- TOC entry 2883 (class 0 OID 63251)
-- Dependencies: 1793
-- Data for Name: ref_publicentry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_publicentry (dbid, details) FROM stdin;
38001	\N
38002	\N
38003	\N
38004	\N
10079	The purified protein which will be crystallized in trialplate
38012	The sample may contains crystal
38013	PIMS default category for screen
38014	Sub category of screen
38015	Sub category of screen
38016	Sub category of screen
38017	PIMS default category for Crystal Trial
1	Primer-design details for new construct
3	Gateway Entry clone design
5	Deep-Frozen culture stock recording
7	Polymerase chain reaction
9	Replication of DNA in a foreign host
11	Restriction enzyme digest of DNA
13	Recombinational cloning e.g. Gateway&reg; technology
15	Ligation-independent cloning - annealing reaction
17	Ligation-independent cloning - polymerase reaction
19	Introduction of DNA into cells
21	Enzymatic joining of DNA fragments
23	\N
25	\N
27	\N
29	Production of a recombinant protein from a cloned gene
31	Small scale expression e.g. trials
33	Large scale expression
35	\N
37	Purify DNA of interest
39	Small scale purification of plasmid DNA
41	Purification of plasmid DNA
43	Purify protein of interest
45	Chromatographic separation techniques e.g. HPLC, TLC, Gel filtration 
47	Enzymatic digestion of proteins
49	Enabling a denatured or unfolded protein to adopt its native conformation 
51	Buffer exchange through a membrane
53	Reduce the volume of a solution
55	Separation of a mixture into its component parts
57	Cell disruption
18143	\N
59	Summary of expression and purification details
61	Preparation of cell membranes
63	Crystallization experiment
65	Set up a crystallization experiment
67	Crystallization trials with native protein
69	Crystallization trials with Seleneomethionine-labelled protein
71	Crystallization trials
73	User-defined score from a crystallization experiment
75	Imaging of a crystallization experiment
77	Crystallization experiment with native protein
79	Crystallization experiment with Seleneomethionine-labelled protein
81	Record crystallization success
83	Crystallization optimization with native protein
85	Crystallization optimization with Seleneomethionine-labelled protein
87	Crystal mounting e.g. in a loop or capllary
89	Viewing a crystallization plate
91	\N
93	Obtain diffraction data for crystal
95	Obtain diffraction data for native protein crystal
97	Obtain diffraction data for Seleneomethionine-labelled protein crystal
99	Obtain diffraction data set
101	Obtain crystal structure
103	\N
105	\N
107	Preparation of a buffer
109	Preparation of a solution
111	Growth of cells in liquid media
113	Import of a reagent or sample into PiMS
115	Large scale culture under controlled conditions
117	Protein characterisation e.g. SDS-PAGE, DLS, CD, etc.
119	Gel Electrophoresis e.g. SDS-PAGE, agarose gel
121	Optical density or absorbance measurement
123	Dynamic light scattering, DLS
125	Mass spectrometry analysis
127	Growth of cells on a solid support, e.g. agar plate
129	\N
131	\N
133	\N
135	Record deposition of crystal coordinates in PDB
137	Test of transformation by growth on solid support
139	Unspecified experiment type
141	Order samples
143	Fermentation experiments
145	\N
147	\N
149	\N
151	Mixing of protein samples to form a complex
2001	PIMS default target status
2003	PIMS default target status
2005	PIMS default target status
2007	PIMS default target status
2009	PIMS default target status
2011	PIMS default target status
2013	PIMS default target status
2015	PIMS default target status
2017	PIMS default target status
2019	PIMS default target status
2021	PIMS default target status
2023	PIMS default target status
2025	PIMS default target status
2027	PIMS default target status
2029	PIMS default target status
2031	PIMS default target status
2033	PIMS default target status
2035	PIMS default target status
2037	PIMS default target status
2039	PIMS default target status
2041	PIMS default target status
2043	PIMS default target status
2045	PIMS default target status
2047	PIMS default target status
2049	PIMS default target status
2051	PIMS default target status
4001	\N
4002	\N
4003	\N
4004	\N
4005	\N
4006	\N
4007	\N
4008	\N
4009	\N
4010	\N
4011	\N
4012	\N
4013	\N
4014	\N
4015	\N
4016	\N
4017	\N
4018	\N
4019	\N
4020	\N
4021	\N
4022	\N
4023	\N
4024	\N
4025	\N
4026	\N
4027	\N
4028	\N
4029	\N
4030	\N
4031	\N
4032	\N
4033	\N
4034	\N
4035	\N
4036	\N
4037	\N
4038	\N
4039	\N
4040	\N
4041	\N
4042	\N
4043	\N
4044	\N
4045	\N
4046	\N
4047	\N
4048	\N
4049	\N
4050	\N
4051	\N
4052	\N
4053	\N
4054	\N
4055	\N
4056	\N
4057	\N
4058	\N
4059	\N
4060	\N
4061	\N
4062	\N
4063	\N
4064	\N
6001	\N
6003	\N
6005	\N
6007	\N
6009	\N
6011	\N
6013	\N
6015	\N
6017	\N
6019	\N
6021	\N
6023	\N
6025	\N
6027	\N
6029	\N
6031	\N
6033	\N
6035	\N
6037	\N
6039	\N
6041	\N
6043	\N
6045	\N
6047	\N
6049	\N
6051	\N
6053	\N
6055	\N
6057	\N
6059	\N
6061	\N
6063	\N
6065	\N
6067	\N
6069	\N
6071	\N
6073	\N
10001	Compound with both hydrophilic and hydrophobic groups. e.g. sufactant, detergent, phospholipid
10003	Substance which inhibits the growth of a micro-organism
10005	Immunoglobulin with defined specificity
10007	System which can neutalise effects of addition of acids and bases
10009	The weak acid or weak base that would comprise a Buffer or Buffer Solution
10011	Untransformed or native cell or cells: Bacterial, Yeast, Eukaryotic, etc.
10013	A Chemical or Biochemical
10015	Reagent for use in chromatography experiments
10017	Commercial kit for cloning experiments
10019	\N
10021	Cell or cells capable of DNA uptake
10023	Liquid or solid substance used for the growth of cells
10025	Substance with polar (water soluble) and non-polar(hydrophobic) domains
10027	Reagent for use in electrophoresis experiments
10029	Restriction enzyme
10031	Modifying enzyme e.g. kinase, phosphatase, etc.
10033	Polymerase enzyme e.g. Taq, Pfu, Reverse transcriptase, etc. 
10035	Ligase enzyme
10037	 Protease enzyme e.g. TEV
10039	Used in combination with a Reverse Primer in PCR
10041	Protein which stimulates cellular proliferation
10043	Reagent used for preparing heavy-atom derivatives of protein crystals
10045	Substance which regulates celluar functions
10047	Substance which inhibits the activity of a molecule
10049	\N
10051	Hydrocarbon containing organic compound, insoluble in water
10053	Size standards used in elctrophoreis
10055	\N
10057	Latex beads, magnetic beads etc.
10059	Reagents for use in microscopy
10061	\N
10063	DNA, RNA etc.
10065	Commercial kit for nucleic acid purification"
10067	Commercial kit for modifying nucleic acids"
10069	Nucleotide compounds and solutions e.g.dATP, NADP, etc.
10071	A recombinant or insert-containing vector
10073	e.g. PCR-primer
10075	Drugs and pharmaceuticals
10077	Amino acid polymer
10081	Chemical prepared using a radioactive element
10083	Cells with Target
10085	Used in combination with a Forward Primer in PCR
10087	PIMS default category
10089	Crystallization screen
10091	\N
10093	\N
10095	Solution
10097	Solvent
10099	Stains and Dyes
10101	A strain of an organism
10103	Substance acted on by an enzyme
10105	Expression vector, cloning vector, etc.
10107	Vitamins and Derivatives
10109	Sample which contains the Target protein
10111	Cell or cells which have been modified to contain foreign DNA such as a plasmid: e.g. E.coli transformed with pDEST17
10113	Commercial kit
10115	Affinity Resin
12001	PIMS default category
12003	PIMS default category
12005	PIMS default category
12007	PIMS default category
12009	PIMS default category
12011	PIMS default category
12013	PIMS default category
12015	PIMS default category
12017	PIMS default category
12019	PIMS default category
12021	PIMS default category
12023	PIMS default category
12025	PIMS default category
12027	PIMS default category
12029	PIMS default category
12031	PIMS default category
12033	PIMS default category
12035	PIMS default category
12037	PIMS default category
12039	PIMS default category
12041	PIMS default category
12043	PIMS default category
12045	PIMS default category
12047	PIMS default category
12049	PIMS default category
12051	PIMS default category
12053	PIMS default category
12055	PIMS default category
12057	PIMS default category
12059	PIMS default category
12061	PIMS default category
12063	PIMS default category
12065	PIMS default category
12067	PIMS default category
12069	PIMS default category
12071	PIMS default category
12073	PIMS default category
12075	PIMS default category
12077	PIMS default category
12079	PIMS default category
12081	PIMS default category
12083	PIMS default category
12085	PIMS default category
12087	PIMS default category
12089	PIMS default category
12091	PIMS default category
12093	PIMS default category
12095	PIMS default category
12097	PIMS default category
18001	\N
18002	\N
18003	\N
18004	\N
18005	\N
18006	\N
18007	\N
18008	\N
18009	\N
18010	\N
18011	\N
18012	\N
18013	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18014	\N
18015	\N
18016	\N
18017	\N
18018	\N
18019	\N
18020	\N
18021	\N
18022	\N
18023	\N
18024	\N
18025	\N
18026	\N
18027	\N
18028	\N
18029	\N
18030	\N
18031	\N
18032	\N
18033	\N
18034	\N
18035	\N
18036	\N
18037	\N
18038	\N
18039	\N
18040	\N
18041	\N
18042	\N
18043	\N
18044	\N
18045	\N
18046	\N
18047	\N
18048	\N
18049	\N
18050	\N
18051	\N
18052	\N
18053	\N
18054	\N
18055	\N
18056	\N
18057	\N
18058	\N
18059	\N
18060	\N
18061	\N
18062	\N
18063	\N
18064	\N
18065	\N
18066	\N
18067	\N
18068	\N
18069	\N
18070	\N
18071	\N
18072	The phrase has been changed by ATP 28 (6 August 2001).The corresponding phrase used in earlier cards reads: Possible risk of irreversible effects.
18073	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18074	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18075	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18076	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18077	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18078	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18079	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18080	\N
18081	\N
18082	\N
18083	\N
18084	\N
18085	\N
18086	\N
18087	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18088	\N
18089	\N
18090	\N
18091	\N
18092	\N
18093	\N
18094	\N
18095	\N
18096	\N
18097	\N
18098	\N
18099	\N
18100	\N
18101	\N
18102	\N
18103	\N
18104	\N
18105	\N
18106	\N
18107	\N
18108	\N
18109	\N
18110	\N
18111	\N
18112	\N
18113	\N
18114	\N
18115	\N
18116	\N
18117	\N
18118	\N
18119	\N
18120	\N
18121	\N
18122	\N
18123	\N
18124	\N
18125	\N
18126	\N
18127	\N
18128	\N
18129	\N
18130	\N
18131	\N
18132	Safety Phrases
18133	\N
18134	\N
18135	\N
18136	\N
18137	\N
18138	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18139	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18140	\N
18141	\N
18142	\N
18144	\N
18145	\N
18146	\N
18147	\N
18148	\N
18149	\N
18150	\N
18151	\N
18152	\N
18153	\N
18154	\N
18155	\N
18156	\N
18157	\N
18158	\N
18159	\N
18160	\N
18161	\N
18162	\N
18163	\N
18164	\N
18165	\N
18166	\N
18167	\N
18168	\N
18169	\N
18170	\N
18171	\N
18172	\N
18173	\N
18174	\N
18175	\N
18176	\N
18177	\N
18178	\N
18179	\N
18180	\N
18181	\N
18182	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18183	\N
18184	\N
18185	\N
18186	\N
18187	\N
18188	\N
18189	\N
18190	\N
18191	\N
18192	\N
18193	\N
18194	\N
18195	The phrase has been deleted by ATP 28 (6 August 2001),but may still appear in cards not modified since then.
18196	\N
18197	\N
18198	\N
18199	\N
18200	\N
18201	\N
18202	\N
18203	\N
18204	\N
18205	\N
18206	\N
18207	\N
18208	\N
18209	\N
18210	\N
18211	\N
18212	\N
18213	\N
18214	\N
18215	\N
18216	\N
18217	Category of danger: Physico/chemical
18218	Category of danger: Physico/chemical
18219	Category of danger: Physico/chemical
18220	Category of danger: Health
18221	Category of danger: Health
18222	Category of danger: Health
18223	Category of danger: Health
18224	Category of danger: Health
18225	Category of danger: Environmental
18226	Category of danger: Physico/chemical
20001	A hierarchical domain classification of protein structures in the Brookhaven protein databank
20003	Phylogenetic classification of proteins encoded in complete genomes
20005	All known nucleotide and protein sequences
20007	EBI's collection of databases of complete and unfinished viral , pro- and eukaryotic genomes
20009	TIGR database for orthologous genes in Eukaryotes
20011	Europe's primary nucleotide sequence resource
20013	Database of nucleotide sequences coding sequences
20015	Assembly information on all accession.versions and sequence locations for complete genomes and other long sequences (>350kb)
20017	A searchable database of genes, from RefSeq genomes.
20019	NCBI's searchable collection of complete and incomplete (in-progress) large-scale sequencing, assembly, annotation, and mapping projects for cellular organisms.
20021	All protein sequences: imported from SwissProt, PIR, PRF, PDB, and translations from annotated coding regions in GenBank and RefSeq
20023	Sequences of proteins with experimentally verified function
20025	Entrez nucleotide sequences from several sources, including GenBank, RefSeq, and PDB
20027	Gene annotation language database
20029	The EBI's Gene Ontology Annotation (GOA) project to provide assignments of GO terms to gene products for all organisms with completely sequenced genomes
20031	Catalog of normal human gene and genome variation, to be developed into a Phenotype/Genotype database
20033	A database of protein families, domains and functional sites, provides an integrated view of the commonly used protein signature databases such as PROSITE and Pfam.
20035	A database of annotated proteins for a limited number of higher eukaryotic species whose genomic sequence has been completely determined but where there are a large number of predicted protein sequences that are not yet in UniProt.
20037	Nomenclature of enzymes, membrane transporters, electron transport proteins and other proteins
20039	Nomenclature of biochemical and organic compounds approved by the IUBMB-IUPAC Joint Commission
20041	Integrated suite of databases on genes, proteins and metabolic pathways
20043	NCBI's database of macromolecular 3D structures, plus tools for their visualization and comparative analysis
20045	EBI's macromolecular structure database
20047	Names of all organisms that are represented in the genetic databases with at least one nucleotide or protein sequence
20049	Classification system for gene products by biological function
20051	A set of biotechnology-related abstracts of patent applications derived from data products of the European Patent Office (EPO).
20053	Protein structure databank: all publicly available 3D structures of proteins and nucleic acids
20055	3D structure database of small molecular ligands bound to larger biolomocules deposited in the Protein Data Bank (PDB).
20057	Transmembrane proteins from the PDB
20059	Pre-computed analyses of genomic sequences
20061	Database of citations and abstracts to biomedical and other life science journal literature.
20063	The RefSeq collection aims to provide a comprehensive, integrated, non-redundant set of sequences, including genomic DNA, transcript (RNA), and protein products, for major research organisms.
20065	Structural classification of all proteins of known structure according to their evolutionary, functional and structural relationships.
20067	Assignments of proteins to 1539 SCOP structural superfamilies
20069	Now UniProt/Swiss-Prot: expertly curated protein sequence database, section of the UniProt knowledgebase
20071	Target data from worldwide structural genomics projects, provides status and tracking information on the progress of the production and solutions of structures
20073	Transport Classification database for membrane transport proteins
20075	Information on all of the publicly available, complete prokaryotic genomes.
20077	Organism-specific databases of publicly available EST and gene sequences and analyses
20079	Alphabetical listing of published TIGR Microbial genomes
20081	Database of topological descriptions of protein structures
20083	Database describing the predicted cytoplasmic membrane transport protein complement for organisms whose complete genome sequence is available
20085	A comprehensive repository, reflecting the history of all protein sequences.
20087	Repository of protein sequence and function created by joining the information contained in Swiss-Prot, TrEMBL, and PIR
20089	The central access point for extensive curated protein information, including function, classification, and cross-reference
20091	Non-redundant Reference database combines closely related sequences into a single record to speed searches
20093	Vector sequences, adapters, linkers and primers used in DNA cloning, and tools to check for vector contamination
20095	Sequences of cloning vectors
20097	Unspecified database
26001	PIMS default category
26003	PIMS default category
26005	PIMS default category
26007	PIMS default category
26009	PIMS default category
26011	PIMS default category
26013	PIMS default category
26015	PIMS default category
28001	96 well plate
28003	96 well plate
28005	96 well plate
28007	96 well plate
28009	96 well plate
28011	24-well plate
28013	24-well plate
28015	96 Well PCR plate Polystyrene Structure , Flat bottomed , Clear , Sterile , With Lid. Half skirted, FOR ABI
28017	96 Well PCR plate, Low profile, natural
28019	skirted low profile plate, for mixing small samples
28021	for small scale growth
28023	transparent, flat bottomed
28025	24 well plate
28027	96 well plate
28029	24 well plate
28031	96 well plate
28033	96 well plate
28035	24 well plate
28037	96 well plate
28039	97 well plate
28041	24 well plate
28043	48 well plate
28045	24 well plate
28047	96 well plate
28049	96 well plate
28051	for solution making
28053	Clone saver card
\.


--
-- TOC entry 2884 (class 0 OID 63254)
-- Dependencies: 1794
-- Data for Name: ref_samplecategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_samplecategory (name, publicentryid) FROM stdin;
Amphiphile	10001
Antibiotic	10003
Antibody	10005
Buffer	10007
Buffering Agent	10009
Cell Line	10011
Chemical/Biochemical	10013
Chromatography	10015
Cloning kit	10017
Crystal	10019
Competent cells	10021
Culture media	10023
Detergent	10025
Electrophoresis	10027
Enzyme -restriction	10029
Enzyme -modifying	10031
Enzyme -polymerase	10033
Enzyme -ligase	10035
Enzyme -protease	10037
Forward Primer	10039
Growth factor	10041
Heavy atom reagent	10043
Hormone	10045
Inhibitor	10047
InSoluble Protein Sample	10049
Lipid	10051
Marker	10053
Membrane	10055
Microparticle	10057
Microscopy	10059
Mounted Crystal	10061
Nucleic acid	10063
Nucleic acid purification kit	10065
Nucleic acid modification kit	10067
Nucleotide	10069
Plasmid	10071
Primer	10073
Pharmacopeia	10075
Polyamino acid	10077
Purified protein	10079
Radiochemical	10081
Recombinant Cells	10083
Reverse Primer	10085
Screen	10087
Screen solution	10089
Solubilised Membrane	10091
Soluble Protein Sample	10093
Solution	10095
Solvent	10097
Stains and Dyes	10099
Strain	10101
Substrate	10103
Vector	10105
Vitamins and Derivatives	10107
Target -containing sample	10109
Transformed cells	10111
Kit	10113
Affinity Resin	10115
TrialDrop	38012
\.


--
-- TOC entry 2885 (class 0 OID 63257)
-- Dependencies: 1795
-- Data for Name: ref_targetstatus; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_targetstatus (name, publicentryid) FROM stdin;
Selected	2001
PCR	2003
Cloned	2005
Expressed	2007
Small Scale Expression	2009
Soluble	2011
Production Scale expression	2013
Purified	2015
In Crystallization	2017
Crystallized	2019
Diffraction Quality Crystals	2021
Native Diffraction Data	2023
Diffraction	2025
Phasing Diffraction Data	2027
HSQC	2029
NMR Assigned	2031
NMR NOE	2033
Crystal Structure	2035
NMR Structure	2037
In PDB	2039
In BMRB	2041
Molecular Function	2043
Biological Process	2045
Cellular Component	2047
Work Stopped	2049
Other	2051
\.


--
-- TOC entry 2886 (class 0 OID 63260)
-- Dependencies: 1796
-- Data for Name: ref_workflowitem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_workflowitem (publicentryid, experimenttypeid, statusid) FROM stdin;
4001	7	2003
4002	9	2005
4003	11	2005
4004	13	2005
4005	15	2005
4006	17	2005
4007	19	2005
4008	21	2005
4009	37	2005
4010	39	2005
4011	41	2005
4012	29	2007
4013	31	2009
4014	33	2013
4015	35	2015
4016	37	2015
4017	39	2015
4018	43	2015
4019	45	2015
4020	47	2015
4021	49	2015
4022	51	2015
4023	53	2015
4024	55	2015
4025	57	2015
4026	59	2015
4027	63	2017
4028	65	2017
4029	67	2017
4030	69	2017
4031	71	2017
4032	73	2017
4033	75	2017
4034	83	2017
4035	85	2017
4036	89	2017
4037	77	2019
4038	79	2019
4039	81	2019
4040	87	2021
4041	91	2025
4042	93	2025
4043	95	2025
4044	97	2025
4045	99	2025
4046	101	2035
4047	105	2051
4048	107	2051
4049	109	2051
4050	111	2051
4051	113	2051
4052	115	2051
4053	117	2051
4054	119	2051
4055	121	2051
4056	123	2051
4057	125	2051
4058	127	2051
4059	137	2051
4060	129	2051
4061	131	2051
4062	133	2051
4063	61	2051
4064	135	2039
\.


--
-- TOC entry 2887 (class 0 OID 63263)
-- Dependencies: 1797
-- Data for Name: revisionnumber; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY revisionnumber (revision, name, release, tag, author, date) FROM stdin;
36	pims	2_3	pims_2_3_36	Anne	27/08/2008
37	pims	3_2	pims_3_2_37	bill_v_upgrader	27/02/2009
\.


--
-- TOC entry 2888 (class 0 OID 63269)
-- Dependencies: 1798
-- Data for Name: sam_abstractsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_abstractsample (ionicstrength, isactive, ishazard, name, ph, labbookentryid) FROM stdin;
\N	\N	\N	KOD DNA Polymerase	\N	16001
\N	\N	\N	KOD Hot Start DNA Polymerase	\N	16004
\N	\N	\N	KOD XL DNA Polymerase	\N	16007
\N	\N	\N	LIC qualfied T4 Polymerase	\N	16010
\N	\N	\N	Taq Polymerase	\N	16013
\N	\N	\N	VentR DNA Polymerase	\N	16016
\N	\N	\N	Vent (exo-) DNA Polymerase	\N	16019
\N	\N	\N	Deep VentR DNA Polymerase	\N	16022
\N	\N	\N	Pfu Polymerase	\N	16025
\N	\N	\N	T4 DNA Ligase	\N	16028
\N	\N	\N	Pfu Turbo Polymerase	\N	16032
\N	\N	\N	Pfu Turbo Cx Polymerase	\N	16035
\N	\N	\N	Pfu Ultra	\N	16038
\N	\N	\N	BIO-X-ACT Short Mix	\N	16041
\N	\N	\N	BseRI	\N	16044
\N	\N	\N	10x PCR buffer	\N	16047
\N	\N	\N	NEBuffer 2	\N	16049
\N	\N	\N	KOD Reaction Buffer 1	\N	16052
\N	\N	\N	KOD Reaction Buffer 2	\N	16055
\N	\N	\N	KOD Hot Start Buffer	\N	16058
\N	\N	\N	BIO-X-ACT Buffer	\N	16061
\N	\N	\N	HyperLadder I	\N	16064
\N	\N	\N	HyperLadder II	\N	16067
\N	\N	\N	1kbp ladder	\N	16070
\N	\N	\N	100bp ladder	\N	16073
\N	\N	\N	dATP 25umol	\N	16076
\N	\N	\N	dCTP 25umol	\N	16079
\N	\N	\N	dGTP 25umol	\N	16082
\N	\N	\N	dTTP 25umol	\N	16085
\N	\N	\N	dNTP mix 10mM	\N	16088
\N	\N	\N	dATP 25 mM	\N	16091
\N	\N	\N	dTTP 25 mM	\N	16093
\N	\N	\N	KOD dNTP Mix	\N	16095
\N	\N	\N	BIO-X-ACT dNTP Mix	\N	16098
\N	\N	\N	pET Duet	\N	16101
\N	\N	\N	PCR Cloning kit with Gateway technology	\N	16104
\N	\N	\N	Zero Blunt PCR Cloning Kit	\N	16107
\N	\N	\N	Topo TA	\N	16110
\N	\N	\N	Zero Blunt TOPO PCR Cloning Kit	\N	16113
\N	\N	\N	In-Fusion CF Dry-Down PCR Cloning Kit	\N	16116
\N	\N	\N	Quick Change SDM kit	\N	16119
\N	\N	\N	Nova Blue Singles	\N	16122
\N	\N	\N	Nova Blue Giga Singles	\N	16125
\N	\N	\N	HT96 BL21(DE3)	\N	16128
\N	\N	\N	HT96 NovaBlue	\N	16131
\N	\N	\N	Rosetta 2(DE3) Singles	\N	16134
\N	\N	\N	Origami (DE3) Singles	\N	16137
\N	\N	\N	Rosetta-gami 2	\N	16140
\N	\N	\N	BL21	\N	16143
\N	\N	\N	DE3 Competent cell set	\N	16146
\N	\N	\N	B834(DE3)	\N	16149
\N	\N	\N	XL1 Blue Competent Cells	\N	16152
\N	\N	\N	Xl10 Gold Ultracompetent cells	\N	16155
\N	\N	\N	BL21-Gold Competent cells	\N	16158
\N	\N	\N	BL21 CodonPlus -RIL Competent cells	\N	16161
\N	\N	\N	One Shot TOP10	\N	16164
\N	\N	\N	MAX Efficiency DH5a TI Phage-Resistant Competent cells	\N	16167
\N	\N	\N	TAM1 Competent E.Coli	\N	16170
\N	\N	\N	Multishot Stripwell OmniMAX	\N	16173
\N	\N	\N	Qiagen PCR clean up	\N	16176
\N	\N	\N	Qiagen Plasmid mini kit (25)	\N	16179
\N	\N	\N	Qiagen Plasmid midi kit (25)	\N	16182
\N	\N	\N	Qiaquick 96 plates	\N	16185
\N	\N	\N	Agencourt AMPure 60ml Kit	\N	16188
\N	\N	\N	GS96 medium	\N	16191
\N	\N	\N	SOC medium	\N	16194
\N	\N	\N	Luria broth (LB)	\N	16197
\N	\N	\N	Overnight Express Autoinduction System	\N	16199
\N	\N	\N	SelenoMet Medium Base plus Nutrient Mix	\N	16202
\N	\N	\N	SelenoMethionine solution	\N	16205
\N	\N	\N	Methionine solution	\N	16208
\N	\N	\N	Power Broth	\N	16211
\N	\N	\N	IPTG 1M	\N	16214
\N	\N	\N	DMSO	\N	16216
\N	\N	\N	Betaine	\N	16218
\N	\N	\N	DTT 100mM	\N	16220
\N	\N	\N	EDTA 25mM	\N	16222
\N	\N	\N	10x T4 polymerase buffer	\N	16224
\N	\N	\N	Kanamycin 30mg/ml	\N	16226
\N	\N	\N	Tetracycline 12mg/ml	\N	16228
\N	\N	\N	Chloramphenicol 34mg/ml	\N	16230
\N	\N	\N	Carbenicillin 50mg/ml	\N	16232
\N	\N	\N	Ethanol	\N	16234
\N	\N	\N	Water	\N	16236
\N	\N	\N	Isopropanol	\N	16238
\N	\N	\N	Ethanol 70% v/v in water	\N	16240
\N	\N	\N	TE buffer pH 8.0	\N	16242
\N	\N	\N	Invitrogen Primer	\N	16244
\N	\N	\N	Invitrogen / Life Technologies Primer	\N	16247
\N	\N	\N	Operon Primer	\N	16250
\N	\N	\N	MWG Primer	\N	16253
\N	\N	f	ACES	\N	22003
\N	\N	t	Acetic acid	\N	22008
\N	\N	t	Acetone	\N	22013
\N	\N	t	Acetonitrile	\N	22018
\N	\N	f	N-(2-Acetamido)iminodiacetic acid	\N	22023
\N	\N	t	Acridine orange : hemi (zinc chloride)	\N	22028
\N	\N	t	Acrylamide	\N	22033
\N	\N	t	N,N'-(1,2-Dihydroxyethylene)bis-acrylamide	\N	22038
\N	\N	f	Adenosine	\N	22043
\N	\N	t	Adenosine 5'-triphosphate disodium salt	\N	22048
\N	\N	f	Adonitol	\N	22053
\N	\N	f	L-Alanine	\N	22058
\N	\N	f	Albumin bovine	\N	22063
\N	\N	f	Aluminium chloride	\N	22068
\N	\N	f	4-(2-Amino ethyl) benzenesulfonyl fluoride HCl	\N	22073
\N	\N	t	6-Aminocaproic acid	\N	22078
\N	\N	t	2-Amino-2-methyl-1,3-propanediol	\N	22083
\N	\N	t	4-Amino-m-cresol	\N	22088
\N	\N	f	Ammonium acetate	\N	22093
\N	\N	t	Ammonium bicarbonate	\N	22098
\N	\N	f	Ammonium bromide	\N	22103
\N	\N	t	Ammonium chloride	\N	22108
\N	\N	t	Ammonium citrate dibasic	\N	22113
\N	\N	t	Ammonium citrate tribasic	\N	22118
\N	\N	t	Ammonium fluoride	\N	22123
\N	\N	t	Ammonium formate	\N	22128
\N	\N	t	Ammonium iodide	\N	22133
\N	\N	f	Ammonium iron(II) sulphate hexahydrate	\N	22138
\N	\N	t	Ammonium nitrate	\N	22143
\N	\N	t	Ammonium oxalate	\N	22148
\N	\N	t	Ammonium persulphate	\N	22153
\N	\N	f	di-Ammonium hydrogen phosphate	\N	22158
\N	\N	f	Ammonium dihydrogen phosphate	\N	22163
\N	\N	t	Ammonium sulphate	\N	22168
\N	\N	f	Ammonium tartrate dibasic	\N	22173
\N	\N	f	Ammonium trifluoroacetate	\N	22178
\N	\N	t	Ammonium thiocyanate	\N	22183
\N	\N	t	Ampicillin (sodium)	\N	22188
\N	\N	f	AMPSO	\N	22193
\N	\N	t	Aniline	\N	22198
\N	\N	t	8-Anilino-1-naphthalene sulphonic acid (ammonium salt)	\N	22203
\N	\N	f	L-Arginine	\N	22208
\N	\N	f	L-Ascorbic acid	\N	22213
\N	\N	f	L-Asparagine	\N	22218
\N	\N	f	L-Aspartic acid	\N	22223
\N	\N	t	Azelaic acid	\N	22228
\N	\N	t	Barbital	\N	22233
\N	\N	t	Barium acetate	\N	22238
\N	\N	t	Barium chloride	\N	22243
\N	\N	t	Barium nitrate	\N	22248
\N	\N	t	Barium thiocyanate trihydrate	\N	22253
\N	\N	t	Benzamidine hydrochloride hydrate	\N	22258
\N	\N	t	Benzamidine hydrochloride	\N	22263
\N	\N	t	Benzenesulphonic acid	\N	22268
\N	\N	t	Benzoic acid	\N	22273
\N	\N	f	BES	\N	22278
\N	\N	f	Beryllium sulphate tetrahydrate	\N	22283
\N	\N	f	Betaine monohydrate	\N	22290
\N	\N	f	Bestatin hydrochloride	\N	22295
\N	\N	f	Bicine	\N	22300
\N	\N	t	BigCHAP	\N	22305
\N	\N	f	D-Biotin	\N	22310
\N	\N	f	Biotin-maleimide	\N	22315
\N	\N	t	Bis(hexamethylene)triamine	\N	22320
\N	\N	f	BIS-TRIS	\N	22325
\N	\N	f	BIS-TRIS hydrochloride	\N	22330
\N	\N	f	BIS-TRIS propane	\N	22335
\N	\N	f	Boric acid	\N	22340
\N	\N	t	Brilliant Blue G	\N	22345
\N	\N	t	Brilliant Blue R	\N	22350
\N	\N	f	Bromophenol blue,sodium salt	\N	22355
\N	\N	t	2-Brom-4'-nitroacetophenone	\N	22360
\N	\N	t	BRIJ (R) 35	\N	22365
\N	\N	f	1,2-Butanediol	\N	22370
\N	\N	f	1,3-Butanediol	\N	22375
\N	\N	f	1,4-Butanediol	\N	22380
\N	\N	f	2,3-Butanediol	\N	22385
\N	\N	t	1,4-Butanediol diglycidyl ether	\N	22390
\N	\N	t	n-Butanol	\N	22395
\N	\N	t	tert-Butanol	\N	22400
\N	\N	f	tert-Butyl methylether	\N	22405
\N	\N	f	Butyl formate	\N	22410
\N	\N	t	gamma-Butyrolactone	\N	22415
\N	\N	f	C12E8	\N	22420
\N	\N	t	CABS	\N	22425
\N	\N	t	Cadmium acetate	\N	22430
\N	\N	t	Cadmium bromide	\N	22435
\N	\N	f	Cadmium chloride	\N	22440
\N	\N	f	Cadmium chloride dihydrate	\N	22445
\N	\N	t	Cadmium iodide	\N	22450
\N	\N	f	Cadmium sulphate	\N	22455
\N	\N	f	Cadmium sulphate hydrate	\N	22460
\N	\N	f	Calcium acetate	\N	22465
\N	\N	t	Calcium carbonate	\N	22470
\N	\N	f	Calcium chloride	\N	22475
\N	\N	f	Calcium chloride dihydrate	\N	22480
\N	\N	t	Calcium chloride hexahydrate	\N	22485
\N	\N	t	Calcium sulfate	\N	22490
\N	\N	f	e-Caprolactam	\N	22495
\N	\N	f	CAPS	\N	22500
\N	\N	f	CAPSO	\N	22505
\N	\N	t	Carbenicillin disodium salt	\N	22510
\N	\N	t	Cesium bromide	\N	22515
\N	\N	f	Cesium chloride	\N	22520
\N	\N	t	Cesium iodide	\N	22525
\N	\N	t	Cesium nitrate	\N	22530
\N	\N	f	Cesium sulfate	\N	22535
\N	\N	t	Cetyltrimethyl ammonium chloride	\N	22540
\N	\N	t	CHAPS	\N	22545
\N	\N	f	CHES	\N	22550
\N	\N	t	Chloramine T hydrate	\N	22555
\N	\N	t	Chloramphenicol	\N	22560
\N	\N	t	Chloroform	\N	22565
\N	\N	t	4 Chloro-1-naphthol	\N	22570
\N	\N	t	7-Chloro-4-nitro benz-2-oxa-1,3-diazole	\N	22575
\N	\N	f	Cholesterol	\N	22580
\N	\N	f	Cholic acid	\N	22585
\N	\N	t	Citric acid	\N	22590
\N	\N	f	Cobalt(II) chloride	\N	22595
\N	\N	t	Cobalt(II) chloride hexahydrate	\N	22600
\N	\N	f	Copper bromide	\N	22605
\N	\N	t	Copper chloride	\N	22610
\N	\N	f	Copper(II) chloride	\N	22615
\N	\N	t	Copper(II) chloride dihydrate	\N	22620
\N	\N	t	Copper(II) sulphate pentahydrate	\N	22625
\N	\N	f	Creatine	\N	22630
\N	\N	f	L-Cysteine	\N	22635
\N	\N	t	Cytidine 5'-triphosphate disodium salt	\N	22640
\N	\N	t	Cyanogen bromide	\N	22645
\N	\N	t	2-Cyclohexen-1-one	\N	22650
\N	\N	f	N-Decanoyl-N-methylglucamine	\N	22655
\N	\N	f	Zwittergent(R) 3-10	\N	22660
\N	\N	t	Deoxycholic acid (sodium salt)	\N	22665
\N	\N	f	2'-Deoxyadenosine-5'-triphosphate,disodium salt,trihydrate	\N	22670
\N	\N	t	2'-Deoxycytidine 5'-triphosphate disodium salt	\N	22675
\N	\N	t	2'-Deoxyguanosine 5'-triphosphate trisodium salt	\N	22680
\N	\N	t	2'-Deoxythymidine 5'-triphosphate sodium salt	\N	22685
\N	\N	f	Dextran sulphate,sodium salt	\N	22690
\N	\N	t	1,6-Diaminohexane	\N	22695
\N	\N	f	1,8-Diaminooctane	\N	22700
\N	\N	t	1,5-Diaminopentane	\N	22705
\N	\N	f	di-u-Iodobis(ethylenediamine)-di-platinum (II) nitrate	\N	22710
\N	\N	f	Dichloro(ethylenediamine)platinum (II)	\N	22715
\N	\N	f	Dichloromethane	\N	22720
\N	\N	t	N,N'-Dicyclohexylcarbodiimide	\N	22725
\N	\N	f	DDM	\N	22730
\N	\N	f	DHPC	\N	22735
\N	\N	t	N,N-Dimethyldecylamine-N-oxide	\N	22740
\N	\N	f	N,N-Dimethyldodecylamine-N-oxide	\N	22745
\N	\N	t	Zwittergent(R) 3-8	\N	22750
\N	\N	t	Zwittergent(R) 3-16	\N	22755
\N	\N	f	Diethyl-dithio-carbamic acid (sodium salt)	\N	22760
\N	\N	t	Diethyl ether	\N	22765
\N	\N	t	Diethyl pyrocarbonate	\N	22770
\N	\N	t	N,N-Dimethylformamide	\N	22775
\N	\N	t	Dimethyl suberimidate dihydrchloride	\N	22780
\N	\N	f	Dioxane	\N	22785
\N	\N	t	Diphenylthiocarbazone	\N	22790
\N	\N	t	Dipicolinic acid	\N	22795
\N	\N	f	DIPSO	\N	22800
\N	\N	t	5 5' Dithio-bis (2-nitrobenzoic acid)	\N	22805
\N	\N	t	Dithiothreitol	\N	22810
\N	\N	f	DLPC	\N	22815
\N	\N	f	DM	\N	22820
\N	\N	f	DMPC	\N	22825
\N	\N	f	DOPG	\N	22830
\N	\N	t	Dimethylsulphoxide	\N	22835
\N	\N	f	Dodecyl-b-D-glucopyranoside	\N	22840
\N	\N	t	Zwittergent(R) 3-12	\N	22845
\N	\N	f	DOPC	\N	22850
\N	\N	f	Ectoine	\N	22855
\N	\N	f	Erythritol	\N	22860
\N	\N	f	Ethanolamine	\N	22867
\N	\N	t	Ethidium bromide	\N	22872
\N	\N	t	EDTA	\N	22877
\N	\N	f	EDTA disodium salt	\N	22882
\N	\N	t	2-Ethoxyethanol	\N	22887
\N	\N	f	Ethyl acetate	\N	22892
\N	\N	f	Ethylenediamine	\N	22897
\N	\N	t	Ethylene glycol	\N	22902
\N	\N	f	EGTA	\N	22907
\N	\N	t	EPPS	\N	22912
\N	\N	t	N Ethylmaleimide	\N	22917
\N	\N	f	Ethyl mercuryphosphate	\N	22922
\N	\N	f	Ethyl methylketone	\N	22927
\N	\N	f	2-Ethyl-5-phenylisoxazolium sulfonate	\N	22932
\N	\N	f	D-(-)-Fructose	\N	22937
\N	\N	f	L-(+)-Fructose	\N	22942
\N	\N	t	Formaldehyde solution	\N	22947
\N	\N	t	Formamide	\N	22952
\N	\N	t	Formic acid	\N	22957
\N	\N	t	Fusaric acid	\N	22962
\N	\N	f	D-(+)-Glucose	\N	22967
\N	\N	f	L-(-)-Glucose	\N	22972
\N	\N	t	Glutathione (oxidised form)	\N	22977
\N	\N	f	Glutathione (reduced form)	\N	22982
\N	\N	f	L-Glutamic acid	\N	22987
\N	\N	f	D-Glutamine	\N	22992
\N	\N	t	Glutaraldehyde	\N	22997
\N	\N	f	Glycerol	\N	23002
\N	\N	f	Glycine	\N	23007
\N	\N	f	Glycine betaine	\N	23012
\N	\N	f	Glycyl-glycyl-glycine	\N	23017
\N	\N	t	Guanidinium chloride	\N	23022
\N	\N	f	Guanosine 5'-diphosphate (GDP) - sodium salt	\N	23027
\N	\N	f	Guanosine 5'-triphosphate di sodium salt	\N	23032
\N	\N	t	HEPBS	\N	23037
\N	\N	f	HEPES	\N	23042
\N	\N	f	HEPES sodium salt	\N	23047
\N	\N	f	HEPPSO	\N	23052
\N	\N	f	1,2,3-Heptanetriol	\N	23057
\N	\N	f	n-Heptyl-b-D-glucopyranoside	\N	23062
\N	\N	t	Hexaminecobalt trichloride	\N	23067
\N	\N	f	Hexadecyltrimethylammonium bromide	\N	23072
\N	\N	f	Hexafluoro isopropanol	\N	23077
\N	\N	f	1,6-Hexanediol	\N	23082
\N	\N	t	2,5-Hexanediol	\N	23087
\N	\N	f	1,2,3-Hexanetriol	\N	23092
\N	\N	f	Hexyl-b-D-glucopyranoside	\N	23097
\N	\N	t	Hydrochloric acid	\N	23102
\N	\N	t	3' Hydroxy-2-butanone	\N	23107
\N	\N	t	Hydroxylamine hydrochloride	\N	23112
\N	\N	t	2-Hydroxy-5-nitrobenzaldehyde	\N	23117
\N	\N	t	alpha Hydroxyisobutyric acid	\N	23122
\N	\N	t	Imidazole	\N	23127
\N	\N	t	Iodoacetamide	\N	23132
\N	\N	t	Iodoacetic acid	\N	23137
\N	\N	f	N-Iodo acetyl-N-(5-sulfo-1-naphthyl) Ethyldiamine	\N	23142
\N	\N	f	Iron(III) chloride	\N	23147
\N	\N	f	Iron(III) chloride hexahydrate	\N	23152
\N	\N	t	Iron(II) chloride tetrahydrate	\N	23157
\N	\N	t	Iron(II) sulfate	\N	23162
\N	\N	t	IPTG	\N	23167
\N	\N	t	Iso-propanol	\N	23172
\N	\N	t	Jeffamine(R) M-600	\N	23177
\N	\N	f	Jeffamine(R) ED-2001	\N	23182
\N	\N	f	Kanamycin monosulphate	\N	23187
\N	\N	f	Lactose	\N	23192
\N	\N	f	N-Lauroylsarcosine  sodium salt	\N	23197
\N	\N	t	Lead(II) acetate trihydrate	\N	23202
\N	\N	t	Lead(II) acetate	\N	23207
\N	\N	f	Leupeptin trifluoroacetate salt	\N	23212
\N	\N	f	Lithium acetate	\N	23217
\N	\N	f	Lithium acetate dihydrate	\N	23222
\N	\N	f	Lithium bromide	\N	23227
\N	\N	f	Lithium chloride	\N	23232
\N	\N	t	Lithium chloride (hydrate)	\N	23237
\N	\N	f	Lithium citrate (anhydrous)	\N	23242
\N	\N	t	Lithium fluoride	\N	23247
\N	\N	f	Lithium formate	\N	23252
\N	\N	f	tri-Lithium citrate tetrahydrate	\N	23257
\N	\N	t	Lithium nitrate	\N	23262
\N	\N	t	Lithium perchlorate	\N	23267
\N	\N	t	Lithium salicylate	\N	23272
\N	\N	t	Lithium sulphate	\N	23277
\N	\N	f	Lithium sulphate monohydrate	\N	23282
\N	\N	f	Magnesium acetate tetrahydrate	\N	23287
\N	\N	t	Magnesium bromide	\N	23292
\N	\N	f	Magnesium chloride anhydrous	\N	23297
\N	\N	f	Magnesium chloride  hexahydrate	\N	23302
\N	\N	f	Magnesium formate	\N	23307
\N	\N	f	Magnesium formate dihydrate	\N	23312
\N	\N	f	Magnesium nitrate	\N	23317
\N	\N	f	Magnesium nitrate hexahydrate	\N	23322
\N	\N	f	Magnesium sulphate	\N	23327
\N	\N	f	Magnesium sulphate hydrate	\N	23332
\N	\N	f	Magnesium sulphate monohydrate	\N	23337
\N	\N	f	Magnesium sulphate hexahydrate	\N	23342
\N	\N	f	Magnesium sulphate heptahydrate	\N	23347
\N	\N	t	Manganese(II) chloride	\N	23352
\N	\N	f	Manganese(II) chloride dihydrate	\N	23357
\N	\N	t	Manganese(II) chloride monohydrate	\N	23362
\N	\N	f	Manganese(II) chloride tetrahydrate	\N	23367
\N	\N	f	Malic acid	\N	23372
\N	\N	f	Malonic acid	\N	23377
\N	\N	f	D-Mannitol	\N	23382
\N	\N	f	L-Mannitol	\N	23387
\N	\N	f	D-(+)-Mannose	\N	23392
\N	\N	f	L-(-)-Mannose	\N	23397
\N	\N	t	4-(N-Maleimido) benzophenone	\N	23402
\N	\N	t	2-Mercaptoethanol	\N	23407
\N	\N	t	Mercury(II) acetate	\N	23412
\N	\N	t	Mercury(II) chloride	\N	23417
\N	\N	t	Mersalyl acid	\N	23422
\N	\N	f	MES	\N	23427
\N	\N	f	MES sodium salt	\N	23432
\N	\N	t	Methanol	\N	23437
\N	\N	f	Methyl acetate	\N	23442
\N	\N	t	Methylmercury chloride	\N	23447
\N	\N	f	2-Methyl-2,4-pentanediol	\N	23452
\N	\N	t	MOBS	\N	23457
\N	\N	t	MOPS	\N	23462
\N	\N	t	MOPSO	\N	23467
\N	\N	f	Inositol	\N	23472
\N	\N	t	NADP sodium salt	\N	23477
\N	\N	t	NADPH tetrasodium salt	\N	23482
\N	\N	f	N-Hydroxysuccinimide	\N	23487
\N	\N	t	Nickel(II) chloride	\N	23492
\N	\N	t	Nickel(II) chloride hydrate	\N	23497
\N	\N	t	Nickel(II) chloride hexahydrate	\N	23502
\N	\N	t	Nickel(II) sulphate hexahydrate	\N	23507
\N	\N	t	Nitric acid	\N	23512
\N	\N	t	N-Nonanoyl-N-methylglucamine	\N	23517
\N	\N	t	Nonaethylene glycol monododecyl ether	\N	23522
\N	\N	t	Octaethylene glycol monodecyl ether	\N	23527
\N	\N	f	octyl b-D-Glucopyranoside	\N	23532
\N	\N	f	n-octyl b-D-Thioglucopyranoside	\N	23537
\N	\N	t	beta-Octylglucoside	\N	23542
\N	\N	f	N-Octanoyl-N-methylglucamine	\N	23547
\N	\N	f	nonyl-b-D-Glucopyranoside	\N	23552
\N	\N	t	Orthophosphoric acid	\N	23557
\N	\N	f	Paratone-N	\N	23562
\N	\N	t	PCMB	\N	23567
\N	\N	t	PCMBS	\N	23572
\N	\N	t	4-(2-Pyridylazo)resorcinol mono sodium salt monohydrate	\N	23577
\N	\N	f	Pentaerythritol ethoxylate (3/4 EO/OH)	\N	23582
\N	\N	f	Pentaerythritol propoxylate	\N	23587
\N	\N	f	Pentaethylene glycol monodecyl ether	\N	23592
\N	\N	f	Pentaethylene glycol monooctyl ether	\N	23597
\N	\N	t	3-Pentanone	\N	23602
\N	\N	f	Pepsinostreptin	\N	23607
\N	\N	t	1,10 Phenanthroline	\N	23612
\N	\N	t	Phenol	\N	23617
\N	\N	t	Phenol red	\N	23622
\N	\N	t	Phenylglyoxal hydrate	\N	23627
\N	\N	t	Phenylmercury acetate	\N	23632
\N	\N	t	L-alpha-Phosphatidylcholine	\N	23637
\N	\N	t	PIPES	\N	23642
\N	\N	t	PMSF	\N	23647
\N	\N	t	Poly(acrylic acid sodium salt) 5100	\N	23652
\N	\N	f	Polyethylene glycol 400	\N	23657
\N	\N	f	Polyethylene glycol 200	\N	23662
\N	\N	f	Polyethylene glycol 300	\N	23667
\N	\N	f	Polyethylene glycol 600	\N	23672
\N	\N	f	Polyethylene glycol 1000	\N	23677
\N	\N	f	Polyethylene glycol 1500	\N	23682
\N	\N	f	Polyethylene glycol 2000	\N	23687
\N	\N	f	Polyethylene glycol 3000	\N	23692
\N	\N	f	Polyethylene glycol 3350	\N	23697
\N	\N	f	Polyethylene glycol 3350 monodisperse	\N	23702
\N	\N	f	Polyethylene glycol 4000	\N	23707
\N	\N	f	Polyethylene glycol 5000	\N	23712
\N	\N	f	Polyethylene glycol 6000	\N	23717
\N	\N	f	Polyethylene glycol 8000	\N	23722
\N	\N	f	Polyethylene glycol 10000	\N	23727
\N	\N	f	Polyethylene glycol 20000	\N	23732
\N	\N	f	Polyethylene glycol monomethyl ether 550	\N	23737
\N	\N	f	Polyethylene glycol monomethyl ether 2000	\N	23742
\N	\N	f	Polyethylene glycol monomethyl ether 5000	\N	23747
\N	\N	f	Polyethyleneimine	\N	23752
\N	\N	f	Polypropylene glycol P400	\N	23757
\N	\N	t	Polymixin B sulphate	\N	23762
\N	\N	f	Polyoxyadenylic acid,potassium salt	\N	23767
\N	\N	f	Poly(vinyl alcohol)	\N	23772
\N	\N	f	Polyvinylpyrrolidone K15	\N	23777
\N	\N	f	POPC	\N	23782
\N	\N	f	POPSO	\N	23787
\N	\N	f	Potassium acetate	\N	23792
\N	\N	f	Potassium bromide	\N	23797
\N	\N	f	Potassium chloride	\N	23802
\N	\N	f	Potassium citrate	\N	23807
\N	\N	f	Tripotassium citrate	\N	23812
\N	\N	t	Potassium cyanate	\N	23817
\N	\N	f	Potassium dihydrogen phosphate	\N	23822
\N	\N	f	Dipotassium hydrogen phosphate	\N	23827
\N	\N	f	Potassium phosphate dibasic trihydrate	\N	23832
\N	\N	t	Potassium fluoride	\N	23837
\N	\N	f	Potassium formate	\N	23842
\N	\N	t	Potassium hexachloroiridate(IV)	\N	23847
\N	\N	t	Potassium hexachloroplatinate(IV)	\N	23852
\N	\N	t	Potassium hydroxide	\N	23857
\N	\N	t	Potassium permanganate	\N	23862
\N	\N	f	Potassium iodide	\N	23867
\N	\N	f	Potassium nitrate	\N	23872
\N	\N	f	Potassium sodium tartrate tetrahydrate	\N	23877
\N	\N	f	Potassium sodium tartrate	\N	23882
\N	\N	f	Potassium sulphate	\N	23887
\N	\N	f	Dipotassium tartrate	\N	23892
\N	\N	t	Potassium tetrachloroaurate(III)	\N	23897
\N	\N	t	Potassium tetrachloroplatinate(II)	\N	23902
\N	\N	t	Potassium tetracyanoplatinate(II)	\N	23907
\N	\N	t	Dipotassium tetraraiodomercurate(II)	\N	23912
\N	\N	f	Potassium tetranitroplatinate(II)	\N	23917
\N	\N	t	Potassium thiocyanate	\N	23922
\N	\N	f	L-Proline	\N	23927
\N	\N	t	2-Propanol	\N	23932
\N	\N	f	1,2-Propanediol	\N	23937
\N	\N	f	1,3-Propanediol	\N	23942
\N	\N	t	Propionitrile	\N	23947
\N	\N	t	Pyridine	\N	23952
\N	\N	t	Protamine sulphate	\N	23957
\N	\N	t	Pyridoxal hydrochloride	\N	23962
\N	\N	f	D-(+)-Raffinose	\N	23967
\N	\N	f	Riboflavin	\N	23972
\N	\N	f	Rubidium bromide	\N	23977
\N	\N	f	Rubidium chloride	\N	23982
\N	\N	t	Silver nitrate	\N	23987
\N	\N	f	Sodium acetate	\N	23992
\N	\N	f	Sodium acetate trihydrate	\N	23997
\N	\N	t	Sodium azide	\N	24002
\N	\N	f	Sodium bromide	\N	24007
\N	\N	f	Sodium cacodylate hydrate	\N	24012
\N	\N	t	Sodium cacodylate trihydrate	\N	24017
\N	\N	t	Sodium carbonate (anhydrous)	\N	24022
\N	\N	f	Sodium chloride	\N	24027
\N	\N	f	Sodium citrate	\N	24032
\N	\N	f	Sodium citrate monobasic	\N	24037
\N	\N	t	Sodium deoxycholate	\N	24042
\N	\N	t	Sodium deoxycholate monohydrate	\N	24047
\N	\N	t	Sodium dodecyl sulphate	\N	24052
\N	\N	t	Sodium fluoride	\N	24057
\N	\N	t	Sodium formate	\N	24062
\N	\N	t	Sodium n-hexadecyl sulphate	\N	24067
\N	\N	f	Sodium hydrogen carbonate	\N	24072
\N	\N	f	Sodium dihydrogen phosphate	\N	24077
\N	\N	f	Disodium hydrogen phosphate	\N	24082
\N	\N	f	Sodium phosphate dibasic	\N	24087
\N	\N	t	Sodium hydroxide	\N	24092
\N	\N	t	Sodium iodide	\N	24097
\N	\N	f	Sodium malonate dibasic	\N	24102
\N	\N	f	Sodium malonate	\N	24107
\N	\N	t	Sodium meta periodate	\N	24112
\N	\N	t	Sodium nitrate	\N	24117
\N	\N	t	Sodium nitrite	\N	24122
\N	\N	t	Sodium perchlorate	\N	24127
\N	\N	t	Sodium phosphate	\N	24132
\N	\N	t	Sodium propionate	\N	24137
\N	\N	t	Sodium succinate	\N	24142
\N	\N	f	Sodium sulphate	\N	24147
\N	\N	f	Sodium sulphate decahydrate	\N	24152
\N	\N	f	Disodium tartrate	\N	24157
\N	\N	t	Sodium tetraborate	\N	24162
\N	\N	t	Sodium thiocyanate	\N	24167
\N	\N	t	Sodium thiosulphate pentahydrate	\N	24172
\N	\N	t	Sodium trichloroacetate	\N	24177
\N	\N	f	SOPC	\N	24182
\N	\N	f	Sorbitol	\N	24187
\N	\N	f	Spermidine	\N	24192
\N	\N	t	Spermidine trihydrochloride	\N	24197
\N	\N	t	Spermine tetrahydrochloride	\N	24202
\N	\N	t	Streptomycin sulphate	\N	24207
\N	\N	t	Strontium dichloride	\N	24212
\N	\N	t	Succinic acid	\N	24217
\N	\N	t	Succinic anhydride	\N	24222
\N	\N	t	Succinimide	\N	24227
\N	\N	f	Sucrose	\N	24232
\N	\N	t	Sulfo-EGS	\N	24237
\N	\N	t	Sulphuric acid	\N	24242
\N	\N	f	TABS	\N	24247
\N	\N	t	Tacsimate(TM)	\N	24252
\N	\N	f	TAPS	\N	24257
\N	\N	t	TAPSO	\N	24262
\N	\N	t	Taurine	\N	24267
\N	\N	t	TEMED	\N	24272
\N	\N	f	TES	\N	24277
\N	\N	t	Tetracycline hydrochloride	\N	24282
\N	\N	t	Tetraethylene glycol monooctyl ether	\N	24287
\N	\N	t	Tetradecyl-N-N-dimethyl-3-ammonio-1-propanesulfonate	\N	24292
\N	\N	t	Tetrahydrofurane	\N	24297
\N	\N	t	3,4,5,6-Tetrahydrophthalic-anhydride	\N	24302
\N	\N	t	Tetrakis (acetoxymercury) methane	\N	24307
\N	\N	t	Tetramethyl ammonium chloride	\N	24312
\N	\N	t	Thimerosal	\N	24317
\N	\N	t	Thymol	\N	24322
\N	\N	t	TLCK	\N	24327
\N	\N	t	TPCK	\N	24332
\N	\N	t	TPEN	\N	24337
\N	\N	f	Trehalose	\N	24342
\N	\N	t	Trichloroacetic acid	\N	24347
\N	\N	f	Tricine	\N	24352
\N	\N	t	Triethanolamine	\N	24357
\N	\N	t	Triethylene glycol	\N	24362
\N	\N	t	2,2,2-Trifluoroethanol	\N	24367
\N	\N	t	Trimethylamine hydrochloride	\N	24372
\N	\N	t	Trimethylamine N-oxide	\N	24377
\N	\N	t	Trimethyl ammonium chloride	\N	24382
\N	\N	t	Trimethyllead acetate	\N	24387
\N	\N	t	2,3,5 Triphenyl tetrazolium chloride	\N	24392
\N	\N	f	TRIS	\N	24397
\N	\N	t	Trizma(R) hydrochloride	\N	24402
\N	\N	f	Tris (2,2' bipyridyl)dichloro ruthenium(11) hexahydrate	\N	24407
\N	\N	t	Triton(R) X-100	\N	24412
\N	\N	t	Triton(R) X-114	\N	24417
\N	\N	f	Trypsin-chymotrypsin inhibitor (Soy bean)	\N	24422
\N	\N	f	Trypsin inhibitor (lima bean)	\N	24427
\N	\N	f	Tween(R) 20;polyoxyethylene sorbitan monolaurate	\N	24432
\N	\N	t	Urea	\N	24437
\N	\N	t	Vancomycin hydrochloride	\N	24442
\N	\N	t	Vitamin B12	\N	24447
\N	\N	t	X-GAL	\N	24452
\N	\N	t	Xylene cyanol FF	\N	24457
\N	\N	t	Xylenol orange tetrasodium salt	\N	24462
\N	\N	f	Xylitol	\N	24467
\N	\N	t	Ytterbium(III) chloride hexahydrate	\N	24472
\N	\N	t	Yttrium(III) chloride hexahydrate	\N	24477
\N	\N	t	Yttrium(III) chloride	\N	24482
\N	\N	t	Zinc acetate dihydrate	\N	24487
\N	\N	t	Zinc acetate	\N	24492
\N	\N	t	Zinc chloride	\N	24497
\N	\N	t	Zinc sulphate heptahydrate	\N	24502
\N	\N	t	Zinc sulphate	\N	24507
\.


--
-- TOC entry 2889 (class 0 OID 63272)
-- Dependencies: 1799
-- Data for Name: sam_abstsa2hazaph; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_abstsa2hazaph (otherrole, hazardphraseid) FROM stdin;
22008	18223
22008	18049
22008	18186
22008	18010
22008	18196
22008	18171
22013	18219
22013	18167
22013	18181
22013	18152
22013	18224
22013	18171
22013	18011
22018	18219
22018	18031
22018	18159
22018	18172
22018	18222
22018	18196
22018	18011
22028	18185
22028	18222
22028	18024
22028	18166
22033	18119
22033	18221
22033	18035
22033	18098
22033	18053
22033	18085
22033	18083
22033	18205
22033	18196
22033	18086
22033	18023
22038	18222
22038	18024
22038	18184
22038	18052
22038	18171
22048	18169
22073	18169
22073	18048
22073	18166
22078	18224
22078	18184
22078	18052
22078	18171
22083	18224
22083	18184
22083	18052
22083	18171
22088	18222
22088	18189
22088	18052
22088	18028
22088	18171
22093	18169
22098	18222
22098	18186
22098	18196
22098	18052
22098	18028
22098	18171
22103	18169
22103	18166
22108	18050
22108	18222
22108	18166
22108	18028
22113	18224
22113	18051
22113	18171
22118	18224
22118	18184
22118	18052
22118	18171
22123	18031
22123	18221
22123	18196
22123	18171
22128	18224
22128	18184
22128	18052
22128	18171
22133	18224
22133	18052
22133	18171
22133	18188
22138	18224
22138	18184
22138	18052
22138	18171
22143	18226
22143	18224
22143	18072
22143	18008
22143	18051
22143	18190
22143	18171
22143	18160
22148	18169
22148	18027
22148	18222
22153	18226
22153	18222
22153	18082
22153	18008
22153	18168
22153	18166
22153	18052
22153	18028
22153	18171
22153	18188
22158	18224
22158	18184
22158	18052
22158	18171
22163	18224
22163	18184
22163	18051
22163	18171
22168	18224
22168	18184
22168	18052
22168	18171
22178	18224
22178	18184
22178	18052
22178	18171
22183	18156
22183	18046
22183	18222
22183	18024
22188	18185
22188	18222
22188	18082
22188	18166
22188	18052
22188	18171
22198	18225
22198	18185
22198	18031
22198	18221
22198	18174
22198	18213
22198	18222
22198	18024
22198	18104
22198	18072
22198	18088
22198	18196
22203	18222
22203	18189
22203	18224
22203	18052
22203	18028
22203	18171
22223	18169
22223	18166
22233	18222
22233	18184
22233	18028
22233	18188
22238	18174
22238	18222
22238	18025
22243	18221
22243	18035
22243	18022
22243	18196
22248	18226
22248	18174
22248	18222
22248	18025
22248	18008
22253	18156
22253	18046
22253	18222
22253	18024
22258	18224
22258	18184
22258	18052
22258	18171
22263	18224
22263	18184
22263	18052
22263	18171
22268	18223
22268	18048
22268	18186
22268	18196
22268	18028
22268	18171
22273	18050
22273	18222
22273	18028
22273	18171
22278	18224
22278	18052
22278	18171
22283	18225
22283	18107
22283	18036
22283	18035
22283	18213
22283	18096
22283	18083
22283	18205
22283	18220
22283	18196
22283	18052
22283	18103
22295	18169
22295	18166
22300	18169
22300	18166
22305	18221
22305	18186
22305	18118
22305	18166
22305	18205
22305	18196
22305	18052
22305	18023
22320	18223
22320	18048
22320	18186
22320	18196
22320	18028
22320	18171
22325	18224
22325	18184
22325	18052
22325	18171
22340	18050
22340	18184
22340	18166
22340	18056
22340	18196
22340	18190
22340	18054
22340	18171
22340	18188
22345	18184
22345	18166
22345	18052
22345	18171
22350	18169
22350	18166
22360	18223
22360	18036
22360	18085
22360	18048
22360	18052
22360	18054
22365	18080
22365	18222
22365	18186
22365	18056
22365	18028
22365	18171
22380	18222
22380	18184
22380	18028
22380	18188
22390	18174
22390	18053
22390	18222
22390	18189
22390	18083
22390	18171
22390	18023
22395	18156
22395	18149
22395	18080
22395	18222
22395	18197
22395	18189
22395	18055
22395	18010
22395	18028
22395	18171
22395	18124
22400	18219
22400	18159
22400	18022
22400	18222
22400	18152
22400	18011
22410	18219
22410	18159
22410	18181
22410	18152
22410	18224
22410	18168
22410	18051
22410	18011
22415	18050
22415	18222
22415	18184
22415	18028
22415	18171
22425	18224
22425	18184
22425	18052
22425	18171
22430	18225
22430	18105
22430	18213
22430	18222
22430	18024
22430	18212
22435	18225
22435	18221
22435	18105
22435	18213
22435	18222
22435	18024
22435	18212
22440	18105
22440	18117
22440	18118
22440	18205
22440	18196
22440	18220
22440	18099
22440	18212
22440	18225
22440	18035
22440	18036
22440	18213
22440	18085
22440	18086
22445	18105
22445	18117
22445	18118
22445	18205
22445	18196
22445	18220
22445	18099
22445	18212
22445	18225
22445	18035
22445	18036
22445	18213
22445	18085
22445	18086
22450	18225
22450	18221
22450	18105
22450	18213
22450	18168
22450	18196
22450	18047
22450	18032
22450	18212
22450	18129
22455	18225
22455	18221
22455	18105
22455	18213
22455	18205
22455	18196
22455	18099
22455	18028
22455	18212
22455	18103
22460	18225
22460	18221
22460	18105
22460	18213
22460	18205
22460	18196
22460	18099
22460	18028
22460	18212
22460	18103
22465	18184
22465	18052
22465	18171
22470	18080
22470	18224
22470	18055
22470	18191
22470	18171
22475	18050
22475	18224
22475	18168
22475	18166
22480	18050
22480	18224
22480	18171
22485	18050
22485	18224
22485	18184
22485	18171
22490	18169
22490	18166
22500	18169
22500	18166
22505	18169
22505	18166
22510	18185
22510	18222
22510	18082
22510	18166
22515	18224
22515	18184
22515	18052
22515	18171
22520	18169
22525	18185
22525	18222
22525	18082
22525	18166
22525	18196
22525	18052
22530	18226
22530	18224
22530	18008
22530	18052
22530	18160
22535	18169
22535	18166
22540	18080
22540	18213
22540	18222
22540	18104
22540	18056
22540	18191
22540	18212
22540	18171
22545	18185
22545	18224
22545	18052
22545	18171
22550	18050
22550	18222
22550	18171
22555	18223
22555	18081
22555	18147
22555	18048
22555	18186
22555	18045
22555	18166
22555	18196
22555	18028
22555	18171
22560	18185
22560	18221
22560	18082
22560	18085
22560	18205
22560	18196
22565	18092
22565	18222
22565	18072
22565	18184
22565	18056
22565	18028
22565	18188
22570	18224
22570	18184
22570	18052
22570	18171
22575	18224
22575	18056
22580	18169
22580	18166
22585	18169
22585	18166
22590	18080
22590	18224
22590	18055
22590	18186
22590	18171
22595	18225
22595	18221
22595	18105
22595	18213
22595	18082
22595	18205
22595	18166
22595	18196
22595	18028
22595	18212
22595	18103
22600	18225
22600	18221
22600	18105
22600	18213
22600	18082
22600	18205
22600	18166
22600	18196
22600	18028
22600	18212
22600	18103
22605	18169
22605	18166
22610	18225
22610	18105
22610	18213
22610	18222
22610	18166
22610	18028
22610	18212
22615	18225
22615	18105
22615	18213
22615	18222
22615	18184
22615	18052
22615	18028
22615	18212
22615	18171
22620	18225
22620	18169
22620	18050
22620	18035
22620	18222
22620	18196
22625	18225
22625	18105
22625	18213
22625	18053
22625	18222
22625	18166
22625	18028
22625	18212
22630	18169
22640	18031
22640	18221
22640	18184
22640	18166
22640	18196
22640	18052
22640	18171
22645	18225
22645	18174
22645	18038
22645	18213
22645	18048
22645	18189
22645	18104
22645	18184
22645	18220
22645	18196
22645	18110
22645	18212
22650	18221
22650	18167
22650	18186
22650	18030
22650	18196
22650	18028
22660	18169
22660	18224
22660	18135
22660	18052
22665	18222
22665	18184
22665	18166
22665	18054
22665	18028
22665	18171
22675	18031
22675	18221
22675	18184
22675	18166
22675	18196
22675	18052
22675	18171
22680	18224
22680	18184
22680	18166
22680	18052
22680	18171
22685	18224
22685	18184
22685	18052
22685	18171
22690	18169
22690	18174
22690	18224
22690	18196
22690	18052
22690	18188
22695	18223
22695	18027
22695	18048
22695	18186
22695	18166
22695	18196
22695	18054
22695	18171
22700	18223
22700	18033
22700	18186
22700	18196
22700	18171
22705	18223
22705	18048
22705	18186
22705	18196
22705	18171
22715	18222
22715	18189
22715	18224
22715	18052
22715	18171
22720	18185
22720	18169
22720	18167
22720	18222
22720	18072
22725	18221
22725	18080
22725	18033
22725	18189
22725	18168
22725	18083
22725	18196
22725	18028
22725	18171
22740	18224
22740	18184
22740	18052
22740	18171
22745	18223
22745	18048
22745	18186
22745	18196
22745	18171
22750	18185
22750	18224
22750	18052
22750	18171
22755	18224
22755	18184
22755	18052
22755	18171
22760	18080
22760	18222
22760	18056
22760	18191
22760	18028
22760	18171
22765	18012
22765	18159
22765	18123
22765	18222
22765	18181
22765	18152
22765	18021
22765	18028
22765	18218
22765	18175
22765	18124
22770	18222
22770	18184
22770	18052
22770	18028
22770	18171
22775	18221
22775	18050
22775	18118
22775	18205
22775	18196
22775	18023
22780	18224
22780	18184
22780	18052
22780	18171
22790	18224
22790	18184
22790	18052
22790	18171
22795	18224
22795	18184
22795	18052
22795	18171
22800	18169
22800	18166
22805	18224
22805	18184
22805	18052
22805	18171
22810	18222
22810	18184
22810	18052
22810	18028
22810	18171
22830	18031
22830	18221
22830	18167
22830	18085
22830	18186
22830	18205
22830	18196
22830	18052
22830	18086
22830	18171
22835	18224
22835	18184
22835	18052
22835	18171
22845	18223
22845	18048
22845	18186
22845	18171
16234	18219
16234	18159
16234	18147
16234	18011
22872	18185
22872	18125
22872	18174
22872	18222
22872	18042
22872	18224
22872	18220
22872	18196
22872	18052
22872	18028
22872	18171
22877	18050
22877	18224
22877	18171
22887	18221
22887	18024
22887	18117
22887	18010
22887	18118
22887	18205
22887	18196
22902	18222
22902	18028
22907	18169
22912	18224
22912	18184
22912	18052
22912	18171
22917	18221
22917	18174
22917	18048
22917	18042
22917	18186
22917	18083
22917	18196
22917	18026
22917	18171
22922	18156
22922	18174
22922	18104
22922	18134
22922	18184
22922	18047
22922	18196
22922	18220
22922	18212
22922	18225
22922	18038
22922	18213
22922	18110
22947	18031
22947	18221
22947	18048
22947	18186
22947	18072
22947	18083
22947	18196
22947	18171
22952	18221
22952	18118
22952	18205
22952	18196
22957	18223
22957	18049
22957	18183
22957	18166
22957	18171
22962	18169
22962	18222
22962	18166
22962	18028
22977	18224
22977	18184
22977	18052
22977	18171
22997	18221
22997	18082
22997	18048
22997	18029
22997	18186
22997	18166
22997	18196
22997	18028
22997	18171
23002	18169
23002	18167
23022	18053
23022	18222
23022	18166
23022	18028
23032	18169
23037	18224
23037	18184
23037	18052
23037	18171
23042	18169
23042	18166
23047	18169
23047	18166
23052	18186
23052	18190
23057	18169
23057	18166
23067	18224
23067	18052
23067	18171
23082	18185
23082	18169
23082	18167
23087	18222
23087	18184
23087	18052
23087	18028
23087	18171
23092	18169
23092	18166
23102	18223
23102	18048
23102	18224
23102	18029
23102	18196
23102	18054
23102	18171
23107	18053
23107	18224
23107	18010
23107	18184
23107	18171
23112	18225
23112	18095
23112	18213
23112	18053
23112	18222
23112	18104
23112	18168
23112	18083
23112	18166
23112	18028
23112	18188
23117	18053
23117	18224
23117	18184
23117	18171
23122	18224
23122	18184
23122	18052
23122	18171
23127	18223
23127	18048
23127	18170
23127	18186
23127	18196
23127	18028
23132	18185
23132	18221
23132	18035
23132	18082
23132	18166
23132	18196
23137	18223
23137	18221
23137	18049
23137	18035
23137	18186
23137	18166
23137	18196
23142	18169
23142	18166
23147	18223
23147	18172
23147	18048
23147	18186
23147	18028
23147	18171
23157	18223
23157	18048
23157	18186
23157	18196
23157	18028
23157	18171
23162	18222
23162	18184
23162	18052
23162	18028
23162	18171
23167	18185
23167	18123
23167	18222
23167	18072
23167	18021
23172	18169
23172	18219
23172	18159
23172	18050
23172	18147
23172	18224
23172	18054
23172	18171
23172	18011
23177	18185
23177	18053
23177	18027
23177	18222
23177	18171
23197	18169
23197	18166
23202	18225
23202	18119
23202	18095
23202	18221
23202	18105
23202	18213
23202	18118
23202	18205
23202	18196
23202	18047
23202	18212
23212	18169
23212	18166
23217	18169
23217	18166
23222	18169
23222	18166
23227	18223
23227	18028
23232	18185
23232	18222
23232	18052
23232	18190
23232	18028
23232	18171
23237	18222
23237	18052
23237	18028
23237	18171
23242	18028
23247	18046
23247	18221
23247	18035
23247	18186
23247	18166
23247	18196
23247	18052
23247	18171
23257	18169
23262	18226
23262	18169
23262	18008
23262	18166
23267	18226
23267	18224
23267	18008
23267	18184
23267	18052
23267	18171
23267	18160
23272	18053
23272	18222
23272	18184
23272	18166
23272	18028
23272	18171
23277	18187
23277	18222
23277	18120
23277	18166
23277	18196
23277	18052
23277	18028
23277	18171
23282	18187
23282	18222
23282	18120
23282	18166
23282	18196
23282	18052
23282	18028
23282	18171
23287	18169
23287	18166
23292	18224
23292	18184
23292	18052
23292	18171
23297	18171
23302	18169
23302	18166
23312	18169
23312	18166
23317	18226
23317	18008
23317	18160
23322	18226
23322	18224
23322	18008
23322	18184
23322	18052
23322	18171
23322	18160
23327	18169
23327	18166
23342	18169
23342	18166
23352	18222
23352	18028
23362	18213
23362	18222
23362	18184
23362	18166
23362	18028
23362	18108
23367	18222
23367	18028
23367	18108
23372	18080
23372	18222
23372	18055
23372	18184
23372	18028
23372	18171
23377	18225
23377	18187
23377	18080
23377	18222
23377	18055
23377	18028
23377	18171
23382	18169
23397	18169
23397	18166
23402	18224
23402	18184
23402	18052
23402	18171
23407	18225
23407	18107
23407	18221
23407	18213
23407	18033
23407	18048
23407	18025
23407	18186
23407	18196
23407	18171
23412	18225
23412	18156
23412	18174
23412	18105
23412	18038
23412	18213
23412	18184
23412	18220
23412	18196
23412	18110
23412	18047
23412	18212
23417	18225
23417	18100
23417	18105
23417	18213
23417	18085
23417	18048
23417	18042
23417	18186
23417	18220
23417	18196
23417	18212
23422	18156
23422	18174
23422	18105
23422	18038
23422	18213
23422	18184
23422	18196
23422	18047
23422	18212
23427	18169
23427	18166
23432	18169
23432	18166
23437	18185
23437	18219
23437	18031
23437	18221
23437	18159
23437	18147
23437	18060
23437	18196
23437	18011
23442	18219
23442	18215
23442	18159
23442	18050
23442	18123
23442	18224
23442	18171
23442	18011
23442	18175
23442	18124
23447	18225
23447	18156
23447	18174
23447	18105
23447	18038
23447	18213
23447	18184
23447	18220
23447	18196
23447	18110
23447	18047
23447	18212
23452	18053
23452	18224
23452	18184
23452	18171
23457	18224
23457	18184
23457	18052
23457	18171
23462	18224
23462	18184
23462	18052
23462	18171
23467	18224
23467	18184
23467	18052
23467	18171
23472	18169
23472	18166
23477	18224
23477	18184
23477	18052
23477	18171
23482	18224
23482	18184
23482	18052
23482	18171
23487	18169
23487	18166
23492	18185
23492	18105
23492	18035
23492	18213
23492	18053
23492	18085
23492	18083
23492	18205
23492	18196
23492	18212
23497	18031
23497	18221
23497	18174
23497	18172
23497	18082
23497	18085
23497	18186
23497	18205
23497	18196
23497	18052
23497	18171
23502	18225
23502	18185
23502	18221
23502	18105
23502	18035
23502	18213
23502	18053
23502	18085
23502	18202
23502	18083
23502	18196
23502	18212
23507	18225
23507	18185
23507	18105
23507	18213
23507	18222
23507	18082
23507	18072
23507	18166
23507	18028
23507	18212
23512	18226
23512	18223
23512	18167
23512	18049
23512	18008
23512	18184
23512	18196
23512	18171
23517	18169
23517	18166
23522	18169
23522	18167
23527	18169
23532	18169
23537	18169
23537	18166
23542	18224
23542	18052
23547	18169
23547	18166
23552	18224
23552	18184
23552	18052
23552	18171
23557	18223
23557	18048
23557	18186
23557	18196
23557	18171
23567	18156
23567	18221
23567	18174
23567	18038
23567	18184
23567	18196
23567	18047
23577	18169
23577	18224
23577	18184
23577	18052
23577	18171
23602	18219
23602	18159
23602	18123
23602	18181
23602	18152
23602	18224
23602	18170
23602	18054
23602	18011
23602	18124
23612	18221
23612	18035
23612	18024
23612	18184
23612	18168
23612	18196
23612	18171
23617	18223
23617	18221
23617	18174
23617	18048
23617	18034
23617	18196
23622	18224
23622	18184
23622	18052
23622	18171
23627	18222
23627	18184
23627	18166
23627	18052
23627	18028
23627	18171
23632	18225
23632	18169
23632	18221
23632	18167
23632	18105
23632	18035
23632	18213
23632	18101
23632	18048
23632	18196
23632	18212
23632	18188
23637	18169
23637	18174
23637	18196
23637	18188
23642	18169
23642	18166
23647	18221
23647	18035
23647	18048
23647	18186
23647	18196
23647	18171
23652	18050
23652	18224
23652	18184
23652	18171
23762	18169
23762	18166
23762	18028
23767	18169
23767	18166
23777	18169
23777	18166
23787	18169
23787	18166
23792	18167
23792	18049
23792	18010
23792	18171
23797	18224
23797	18055
23797	18184
23797	18171
23802	18169
23802	18166
23817	18169
23817	18222
23817	18028
23827	18050
23827	18224
23827	18184
23827	18171
23837	18031
23837	18221
23837	18196
23837	18171
23842	18169
23842	18166
23847	18222
23847	18024
23847	18184
23847	18052
23847	18171
23852	18221
23852	18080
23852	18035
23852	18082
23852	18186
23852	18166
23852	18196
23852	18171
23857	18223
23857	18049
23857	18186
23857	18196
23857	18028
23857	18171
23862	18225
23862	18226
23862	18105
23862	18213
23862	18222
23862	18008
23862	18028
23862	18212
23872	18226
23872	18169
23872	18160
23877	18169
23887	18169
23887	18166
23897	18224
23897	18184
23897	18052
23897	18171
23902	18221
23902	18080
23902	18035
23902	18082
23902	18186
23902	18166
23902	18056
23902	18196
23902	18171
23907	18225
23907	18046
23907	18174
23907	18105
23907	18038
23907	18213
23907	18147
23907	18220
23907	18196
23907	18212
23907	18175
23912	18225
23912	18156
23912	18046
23912	18174
23912	18105
23912	18038
23912	18213
23912	18220
23912	18196
23912	18212
23922	18156
23922	18109
23922	18046
23922	18213
23922	18222
23922	18024
23932	18169
23932	18219
23932	18159
23932	18050
23932	18147
23932	18224
23932	18171
23932	18011
23932	18124
23947	18185
23947	18219
23947	18174
23947	18159
23947	18050
23947	18035
23947	18022
23947	18152
23947	18220
23947	18196
23947	18011
23947	18040
23952	18219
23952	18174
23952	18222
23952	18024
23952	18171
23952	18011
23962	18222
23962	18184
23962	18180
23962	18028
23977	18169
23977	18166
23982	18169
23982	18166
23987	18225
23987	18223
23987	18105
23987	18213
23987	18048
23987	18196
23987	18212
23987	18171
23992	18169
23992	18166
23997	18169
24002	18225
24002	18046
24002	18174
24002	18105
24002	18213
24002	18042
24002	18220
24002	18196
24002	18212
24007	18169
24012	18225
24012	18221
24012	18174
24012	18105
24012	18213
24012	18196
24012	18164
24012	18032
24012	18212
24017	18225
24017	18221
24017	18174
24017	18105
24017	18213
24017	18196
24017	18164
24017	18032
24017	18212
24022	18050
24022	18224
24022	18166
24022	18171
24027	18169
24037	18169
24037	18166
24042	18184
24042	18166
24042	18054
24042	18028
24042	18171
24047	18184
24047	18166
24047	18054
24047	18028
24047	18171
24052	18222
24052	18189
24052	18052
24052	18028
24052	18171
24057	18046
24057	18221
24057	18035
24057	18053
24057	18184
24057	18166
24057	18196
24062	18224
24062	18196
24062	18052
24062	18171
24067	18169
24067	18166
24072	18169
24082	18169
24082	18135
24092	18223
24092	18049
24092	18048
24092	18189
24092	18196
24092	18171
24097	18050
24097	18224
24097	18056
24097	18171
24102	18169
24102	18166
24107	18169
24107	18166
24112	18226
24112	18222
24112	18008
24112	18052
24112	18028
24112	18171
24117	18226
24117	18172
24117	18222
24117	18186
24117	18008
24117	18052
24117	18028
24117	18171
24117	18160
24122	18225
24122	18226
24122	18221
24122	18035
24122	18213
24122	18104
24122	18008
24122	18196
24127	18226
24127	18156
24127	18172
24127	18222
24127	18009
24127	18166
24127	18028
24132	18223
24132	18048
24132	18186
24132	18196
24132	18171
24137	18222
24137	18184
24137	18026
24137	18188
24142	18224
24142	18184
24142	18052
24142	18171
24162	18185
24162	18119
24162	18120
24167	18156
24167	18109
24167	18046
24167	18213
24167	18222
24167	18024
24172	18050
24172	18056
24172	18054
24177	18105
24177	18213
24177	18224
24177	18192
24177	18054
24177	18212
24192	18223
24192	18048
24192	18186
24192	18196
24192	18171
24197	18224
24197	18184
24197	18052
24197	18171
24202	18053
24202	18224
24202	18184
24202	18171
24207	18222
24207	18028
24212	18080
24212	18224
24212	18055
24212	18171
24212	18175
24217	18080
24217	18224
24217	18055
24217	18186
24217	18171
24222	18224
24222	18170
24222	18055
24227	18169
24227	18224
24227	18166
24237	18224
24237	18052
24237	18171
24242	18178
24242	18223
24242	18049
24242	18196
24242	18171
24252	18224
24252	18052
24252	18171
24257	18169
24257	18166
24262	18224
24262	18184
24262	18052
24262	18171
24267	18184
24267	18052
24267	18171
24272	18223
24272	18219
24272	18159
24272	18048
24272	18025
24272	18186
24272	18196
24272	18171
24272	18011
24277	18169
24277	18166
24282	18224
24282	18184
24282	18052
24282	18171
24287	18224
24287	18184
24287	18052
24287	18171
24292	18224
24292	18184
24292	18052
24292	18171
24297	18219
24297	18159
24297	18181
24297	18224
24297	18051
24297	18021
24297	18011
24297	18175
24302	18224
24302	18184
24302	18052
24302	18171
24312	18185
24312	18221
24312	18174
24312	18035
24312	18196
24312	18052
24312	18026
24312	18171
24317	18225
24317	18156
24317	18174
24317	18105
24317	18038
24317	18213
24317	18184
24317	18220
24317	18196
24317	18047
24317	18212
24322	18107
24322	18174
24322	18213
24322	18048
24322	18186
24322	18196
24322	18028
24322	18171
24327	18224
24327	18184
24327	18052
24327	18171
24332	18080
24332	18224
24332	18055
24332	18184
24332	18171
24337	18224
24337	18184
24337	18052
24337	18171
24347	18225
24347	18223
24347	18105
24347	18049
24347	18213
24347	18186
24347	18196
24347	18212
24347	18171
24357	18050
24357	18224
24357	18171
24362	18224
24362	18184
24362	18052
24362	18171
24367	18080
24367	18222
24367	18089
24367	18024
24367	18186
24367	18010
24367	18056
24367	18171
24372	18224
24372	18184
24372	18052
24372	18171
24377	18172
24377	18189
24377	18224
24377	18052
24377	18171
24382	18224
24382	18184
24382	18052
24382	18171
24387	18225
24387	18095
24387	18221
24387	18105
24387	18213
24387	18072
24387	18118
24387	18205
24387	18196
24387	18047
24387	18212
24392	18224
24392	18184
24392	18052
24392	18171
24402	18224
24402	18184
24402	18052
24402	18171
24407	18169
24407	18166
24412	18080
24412	18222
24412	18224
24412	18168
24412	18191
24412	18028
24412	18171
24417	18080
24417	18189
24417	18224
24417	18055
24417	18171
24442	18185
24442	18224
24442	18083
24447	18169
24447	18222
24447	18166
24457	18224
24457	18184
24457	18052
24457	18171
24462	18169
24462	18224
24462	18166
24462	18052
24472	18224
24472	18184
24472	18052
24472	18171
24477	18224
24477	18184
24477	18052
24477	18171
24482	18189
24482	18224
24482	18052
24482	18171
24487	18050
24487	18222
24487	18184
24487	18028
24487	18171
24492	18050
24492	18224
24492	18184
24492	18171
24497	18225
24497	18223
24497	18105
24497	18213
24497	18048
24497	18186
24497	18196
24497	18028
24497	18212
24497	18171
24502	18225
24502	18105
24502	18213
24502	18053
24502	18224
24502	18170
24502	18166
24502	18212
24507	18225
24507	18050
24507	18224
24507	18056
24507	18054
\.


--
-- TOC entry 2890 (class 0 OID 63275)
-- Dependencies: 1800
-- Data for Name: sam_crystalsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_crystalsample (a, alpha, b, beta, c, colour, crystaltype, gamma, morphology, spacegroup, x, y, z, sampleid) FROM stdin;
\.


--
-- TOC entry 2891 (class 0 OID 63281)
-- Dependencies: 1801
-- Data for Name: sam_refsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_refsample (abstractsampleid, issaltcrystal) FROM stdin;
16001	\N
16004	\N
16007	\N
16010	\N
16013	\N
16016	\N
16019	\N
16022	\N
16025	\N
16028	\N
16032	\N
16035	\N
16038	\N
16041	\N
16044	\N
16047	\N
16049	\N
16052	\N
16055	\N
16058	\N
16061	\N
16064	\N
16067	\N
16070	\N
16073	\N
16076	\N
16079	\N
16082	\N
16085	\N
16088	\N
16091	\N
16093	\N
16095	\N
16098	\N
16101	\N
16104	\N
16107	\N
16110	\N
16113	\N
16116	\N
16119	\N
16122	\N
16125	\N
16128	\N
16131	\N
16134	\N
16137	\N
16140	\N
16143	\N
16146	\N
16149	\N
16152	\N
16155	\N
16158	\N
16161	\N
16164	\N
16167	\N
16170	\N
16173	\N
16176	\N
16179	\N
16182	\N
16185	\N
16188	\N
16191	\N
16194	\N
16197	\N
16199	\N
16202	\N
16205	\N
16208	\N
16211	\N
16214	\N
16216	\N
16218	\N
16220	\N
16222	\N
16224	\N
16226	\N
16228	\N
16230	\N
16232	\N
16234	\N
16236	\N
16238	\N
16240	\N
16242	\N
16244	\N
16247	\N
16250	\N
16253	\N
22003	\N
22008	\N
22013	\N
22018	\N
22023	\N
22028	\N
22033	\N
22038	\N
22043	\N
22048	\N
22053	\N
22058	\N
22063	\N
22068	\N
22073	\N
22078	\N
22083	\N
22088	\N
22093	\N
22098	\N
22103	\N
22108	\N
22113	\N
22118	\N
22123	\N
22128	\N
22133	\N
22138	\N
22143	\N
22148	\N
22153	\N
22158	\N
22163	\N
22168	\N
22173	\N
22178	\N
22183	\N
22188	\N
22193	\N
22198	\N
22203	\N
22208	\N
22213	\N
22218	\N
22223	\N
22228	\N
22233	\N
22238	\N
22243	\N
22248	\N
22253	\N
22258	\N
22263	\N
22268	\N
22273	\N
22278	\N
22283	\N
22290	\N
22295	\N
22300	\N
22305	\N
22310	\N
22315	\N
22320	\N
22325	\N
22330	\N
22335	\N
22340	\N
22345	\N
22350	\N
22355	\N
22360	\N
22365	\N
22370	\N
22375	\N
22380	\N
22385	\N
22390	\N
22395	\N
22400	\N
22405	\N
22410	\N
22415	\N
22420	\N
22425	\N
22430	\N
22435	\N
22440	\N
22445	\N
22450	\N
22455	\N
22460	\N
22465	\N
22470	\N
22475	\N
22480	\N
22485	\N
22490	\N
22495	\N
22500	\N
22505	\N
22510	\N
22515	\N
22520	\N
22525	\N
22530	\N
22535	\N
22540	\N
22545	\N
22550	\N
22555	\N
22560	\N
22565	\N
22570	\N
22575	\N
22580	\N
22585	\N
22590	\N
22595	\N
22600	\N
22605	\N
22610	\N
22615	\N
22620	\N
22625	\N
22630	\N
22635	\N
22640	\N
22645	\N
22650	\N
22655	\N
22660	\N
22665	\N
22670	\N
22675	\N
22680	\N
22685	\N
22690	\N
22695	\N
22700	\N
22705	\N
22710	\N
22715	\N
22720	\N
22725	\N
22730	\N
22735	\N
22740	\N
22745	\N
22750	\N
22755	\N
22760	\N
22765	\N
22770	\N
22775	\N
22780	\N
22785	\N
22790	\N
22795	\N
22800	\N
22805	\N
22810	\N
22815	\N
22820	\N
22825	\N
22830	\N
22835	\N
22840	\N
22845	\N
22850	\N
22855	\N
22860	\N
22867	\N
22872	\N
22877	\N
22882	\N
22887	\N
22892	\N
22897	\N
22902	\N
22907	\N
22912	\N
22917	\N
22922	\N
22927	\N
22932	\N
22937	\N
22942	\N
22947	\N
22952	\N
22957	\N
22962	\N
22967	\N
22972	\N
22977	\N
22982	\N
22987	\N
22992	\N
22997	\N
23002	\N
23007	\N
23012	\N
23017	\N
23022	\N
23027	\N
23032	\N
23037	\N
23042	\N
23047	\N
23052	\N
23057	\N
23062	\N
23067	\N
23072	\N
23077	\N
23082	\N
23087	\N
23092	\N
23097	\N
23102	\N
23107	\N
23112	\N
23117	\N
23122	\N
23127	\N
23132	\N
23137	\N
23142	\N
23147	\N
23152	\N
23157	\N
23162	\N
23167	\N
23172	\N
23177	\N
23182	\N
23187	\N
23192	\N
23197	\N
23202	\N
23207	\N
23212	\N
23217	\N
23222	\N
23227	\N
23232	\N
23237	\N
23242	\N
23247	\N
23252	\N
23257	\N
23262	\N
23267	\N
23272	\N
23277	\N
23282	\N
23287	\N
23292	\N
23297	\N
23302	\N
23307	\N
23312	\N
23317	\N
23322	\N
23327	\N
23332	\N
23337	\N
23342	\N
23347	\N
23352	\N
23357	\N
23362	\N
23367	\N
23372	\N
23377	\N
23382	\N
23387	\N
23392	\N
23397	\N
23402	\N
23407	\N
23412	\N
23417	\N
23422	\N
23427	\N
23432	\N
23437	\N
23442	\N
23447	\N
23452	\N
23457	\N
23462	\N
23467	\N
23472	\N
23477	\N
23482	\N
23487	\N
23492	\N
23497	\N
23502	\N
23507	\N
23512	\N
23517	\N
23522	\N
23527	\N
23532	\N
23537	\N
23542	\N
23547	\N
23552	\N
23557	\N
23562	\N
23567	\N
23572	\N
23577	\N
23582	\N
23587	\N
23592	\N
23597	\N
23602	\N
23607	\N
23612	\N
23617	\N
23622	\N
23627	\N
23632	\N
23637	\N
23642	\N
23647	\N
23652	\N
23657	\N
23662	\N
23667	\N
23672	\N
23677	\N
23682	\N
23687	\N
23692	\N
23697	\N
23702	\N
23707	\N
23712	\N
23717	\N
23722	\N
23727	\N
23732	\N
23737	\N
23742	\N
23747	\N
23752	\N
23757	\N
23762	\N
23767	\N
23772	\N
23777	\N
23782	\N
23787	\N
23792	\N
23797	\N
23802	\N
23807	\N
23812	\N
23817	\N
23822	\N
23827	\N
23832	\N
23837	\N
23842	\N
23847	\N
23852	\N
23857	\N
23862	\N
23867	\N
23872	\N
23877	\N
23882	\N
23887	\N
23892	\N
23897	\N
23902	\N
23907	\N
23912	\N
23917	\N
23922	\N
23927	\N
23932	\N
23937	\N
23942	\N
23947	\N
23952	\N
23957	\N
23962	\N
23967	\N
23972	\N
23977	\N
23982	\N
23987	\N
23992	\N
23997	\N
24002	\N
24007	\N
24012	\N
24017	\N
24022	\N
24027	\N
24032	\N
24037	\N
24042	\N
24047	\N
24052	\N
24057	\N
24062	\N
24067	\N
24072	\N
24077	\N
24082	\N
24087	\N
24092	\N
24097	\N
24102	\N
24107	\N
24112	\N
24117	\N
24122	\N
24127	\N
24132	\N
24137	\N
24142	\N
24147	\N
24152	\N
24157	\N
24162	\N
24167	\N
24172	\N
24177	\N
24182	\N
24187	\N
24192	\N
24197	\N
24202	\N
24207	\N
24212	\N
24217	\N
24222	\N
24227	\N
24232	\N
24237	\N
24242	\N
24247	\N
24252	\N
24257	\N
24262	\N
24267	\N
24272	\N
24277	\N
24282	\N
24287	\N
24292	\N
24297	\N
24302	\N
24307	\N
24312	\N
24317	\N
24322	\N
24327	\N
24332	\N
24337	\N
24342	\N
24347	\N
24352	\N
24357	\N
24362	\N
24367	\N
24372	\N
24377	\N
24382	\N
24387	\N
24392	\N
24397	\N
24402	\N
24407	\N
24412	\N
24417	\N
24422	\N
24427	\N
24432	\N
24437	\N
24442	\N
24447	\N
24452	\N
24457	\N
24462	\N
24467	\N
24472	\N
24477	\N
24482	\N
24487	\N
24492	\N
24497	\N
24502	\N
24507	\N
\.


--
-- TOC entry 2892 (class 0 OID 63284)
-- Dependencies: 1802
-- Data for Name: sam_refsamplesource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_refsamplesource (catalognum, datapageurl, productgroupname, productname, labbookentryid, refsampleid, supplierid, refholderid) FROM stdin;
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16003	16001	14009	\N
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16006	16004	14009	\N
71087	http://www.merckbiosciences.co.uk/product/71087	\N	\N	16009	16007	14009	\N
70099	http://www.emdbiosciences.com/product/70099	\N	\N	16012	16010	14009	\N
M0373	http://www.neb.com/nebecomm/products_Intl/productM0273.asp	\N	\N	16015	16013	14007	\N
M0254	http://www.neb.com/nebecomm/products_Intl/productM0254.asp	\N	\N	16018	16016	14007	\N
M0257	http://www.neb.com/nebecomm/products_Intl/productM0257.asp	\N	\N	16021	16019	14007	\N
M0258	http://www.neb.com/nebecomm/products_Intl/productM0258.asp	\N	\N	16024	16022	14007	\N
M7741	http://www.promega.com/tbs/9pim210/9pim210.html	\N	\N	16027	16025	14011	\N
M1801	http://www.promega.com/tbs/9pim774/9pim774.html	\N	\N	16030	16028	14011	\N
600136	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600136	\N	\N	16031	16025	14017	\N
600250	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600250	\N	\N	16034	16032	14017	\N
600410	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600410	\N	\N	16037	16035	14017	\N
600380	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600380	\N	\N	16040	16038	14017	\N
BIO-25025	http://www.bioline.com/h_prod_detail.asp?user_prodname=BIO%2DX%2DACT%99+Short+Mix	\N	\N	16043	16041	14001	\N
R0581S	http://www.neb.uk.com/frameset.asp?searchType=products&yider=BseRI&submit.x=0&submit.y=0	\N	\N	16046	16044	14007	\N
R0581S	http://www.neb.uk.com/frameset.asp?searchType=products&yider=BseRI&submit.x=0&submit.y=0	\N	\N	16051	16049	14007	\N
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16054	16052	14009	\N
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16057	16055	14009	\N
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16060	16058	14009	\N
BIO-25025	http://www.bioline.com/h_prod_detail.asp?user_prodname=BIO%2DX%2DACT%99+Short+Mix	\N	\N	16063	16061	14001	\N
BIO-33025	http://www.bioline.com/n_uk.htm	\N	\N	16066	16064	14001	\N
BIO-33039	http://www.bioline.com/n_uk.htm	\N	\N	16069	16067	14001	\N
G5711	http://www.promega.com/techserv/apps/cloning/cloning4.htm	\N	\N	16072	16070	14011	\N
G2101	http://www.promega.com/techserv/apps/cloning/cloning4.htm	\N	\N	16075	16073	14011	\N
BIO-39036	http://www.bioline.com/n_uk.htm	\N	\N	16078	16076	14001	\N
BIO-39038	http://www.bioline.com/n_uk.htm	\N	\N	16081	16079	14001	\N
BIO-39037	http://www.bioline.com/n_uk.htm	\N	\N	16084	16082	14001	\N
BIO-39039	http://www.bioline.com/n_uk.htm	\N	\N	16087	16085	14001	\N
U1511	http://www.promega.com/search/Default.aspx?indexes=0&page=1&query=U1511	\N	\N	16090	16088	14011	\N
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16097	16095	14009	\N
BIO-39028	http://www.bioline.com/h_prod_detail.asp?user_prodname=dNTP+Mix	\N	\N	16100	16098	14001	\N
71146	http://www.emdbiosciences.com/product/71146	\N	\N	16103	16101	14009	\N
12535	http://www.invitrogen.com/search.cfm?category=&searchterm=12535	\N	\N	16106	16104	14003	\N
K2700	http://www.invitrogen.com/search.cfm?category=&searchterm=K2700	\N	\N	16109	16107	14003	\N
K4500	http://www.invitrogen.com/search.cfm?category=&searchterm=K4500	\N	\N	16112	16110	14003	\N
K2800	http://www.invitrogen.com/search.cfm?category=&searchterm=K2800	\N	\N	16115	16113	14003	\N
639605	http://www.clontech.com/products/detail.asp?product_id=10406&tabno=2	\N	\N	16118	16116	14061	\N
200518	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=200518	\N	\N	16121	16119	14017	\N
70181	http://www.emdbiosciences.com/product/70181	\N	\N	16124	16122	14009	\N
71227	http://www.emdbiosciences.com/product/71227	\N	\N	16127	16125	14009	\N
71012	http://www.emdbiosciences.com/product/71012	\N	\N	16130	16128	14009	\N
71011	http://www.emdbiosciences.com/product/71011	\N	\N	16133	16131	14009	\N
71400	http://www.emdbiosciences.com/product/71400	\N	\N	16136	16134	14009	\N
70630	http://www.emdbiosciences.com/product/70630	\N	\N	16139	16137	14009	\N
71350	http://www.emdbiosciences.com/product/71350	\N	\N	16142	16140	14009	\N
69449	http://www.emdbiosciences.com/product/69449	\N	\N	16145	16143	14009	\N
71207	http://www.emdbiosciences.com/product/71207	\N	\N	16148	16146	14009	\N
69041	http://www.emdbiosciences.com/product/69041	\N	\N	16151	16149	14009	\N
200249	http://www.stratagene.com/manuals/200314.pdf	\N	\N	16154	16152	14017	\N
200315	http://www.stratagene.com/manuals/200314.pdf	\N	\N	16157	16155	14017	\N
230130	http://www.stratagene.com/manuals/230130.pdf	\N	\N	16160	16158	14017	\N
230240	http://www.stratagene.com/manuals/230240.pdf	\N	\N	16163	16161	14017	\N
C4040	http://www.invitrogen.com/search.cfm?category=&searchterm=C4040	\N	\N	16166	16164	14003	\N
12034-013	http://www.invitrogen.com/search.cfm?category=&searchterm=12034	\N	\N	16169	16167	14003	\N
11596	http://www.activemotif.com/catalog/transfect_expr/rapidtrans	\N	\N	16172	16170	14063	\N
440011	http://www.invitrogen.com	\N	\N	16175	16173	14003	\N
28104	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=28104	\N	\N	16178	16176	14015	\N
12123	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=12123	\N	\N	16181	16179	14015	\N
12143	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=12143	\N	\N	16184	16182	14015	\N
28181	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=28181	\N	\N	16187	16185	14015	\N
130	http://www.agencourt.com/products/spri_reagents/ampure/	\N	\N	16190	16188	14065	\N
3101	http://www.qbiogene.com/search/index.shtml	\N	\N	16193	16191	14013	\N
3031	http://www.qbiogene.com/search/index.shtml	\N	\N	16196	16194	14013	\N
71300	http://www.emdbiosciences.com/product/71300	\N	\N	16201	16199	14009	\N
MD12-501	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16204	16202	14005	\N
MD12-503	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16207	16205	14005	\N
MD12-504	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16210	16208	14005	\N
MD12-106-6	http://www.moleculardimensions.com	\N	\N	16213	16211	14005	\N
Unknown	http://www.invitrogen.com/content.cfm?pageid=9714&CID=TN-Prods-Oligos	\N	\N	16246	16244	14003	\N
Unknown	http://www.invitrogen.com/content.cfm?pageid=9714&CID=TN-Prods-Oligos	\N	\N	16249	16247	14003	\N
Unknown	https://www.operon.com/products_main.php	\N	\N	16252	16250	14057	\N
Unknown	http://www.mwg-biotech.com/html/s_synthesis/s_overview.shtml	\N	\N	16255	16253	14059	\N
\.


--
-- TOC entry 2893 (class 0 OID 63290)
-- Dependencies: 1803
-- Data for Name: sam_sampca2abstsa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sampca2abstsa (samplecategoryid, abstractsampleid) FROM stdin;
10033	16001
10033	16004
10033	16007
10033	16010
10033	16013
10033	16016
10033	16019
10033	16022
10033	16025
10035	16028
10033	16032
10033	16035
10033	16038
10033	16041
10029	16044
10007	16047
10007	16049
10007	16052
10007	16055
10007	16058
10007	16061
10053	16064
10053	16067
10053	16070
10053	16073
10069	16076
10069	16079
10069	16082
10069	16085
10069	16088
10069	16091
10069	16093
10069	16095
10069	16098
10105	16101
10017	16104
10017	16107
10017	16110
10017	16113
10017	16116
10113	16119
10021	16122
10021	16125
10021	16128
10021	16131
10021	16134
10021	16137
10021	16140
10021	16143
10021	16146
10021	16149
10021	16152
10021	16155
10021	16158
10021	16161
10021	16164
10021	16167
10021	16170
10021	16173
10065	16176
10065	16179
10065	16182
10065	16185
10065	16188
10023	16191
10023	16194
10023	16197
10023	16199
10023	16202
10023	16205
10023	16208
10023	16211
10095	16214
10097	16216
10095	16220
10095	16222
10007	16224
10003	16226
10003	16228
10003	16230
10003	16232
10097	16236
10097	16238
10097	16240
10007	16242
10073	16244
10073	16247
10073	16250
10073	16253
10007	22003
10013	22003
10009	22003
10013	22008
10009	22008
10097	22013
10097	22018
10007	22023
10013	22023
10009	22023
10099	22028
10027	22033
10027	22038
10069	22043
10013	22043
10069	22048
10013	22048
10013	22053
10013	22058
10013	22063
10013	22068
10013	22073
10047	22073
10013	22078
10047	22078
10007	22083
10013	22083
10009	22083
10013	22088
10013	22093
10013	22098
10013	22103
10013	22108
10013	22113
10007	22118
10013	22118
10009	22118
10013	22123
10013	22128
10013	22133
10013	22138
10013	22143
10013	22148
10027	22153
10013	22153
10013	22158
10013	22163
10013	22168
10013	22173
10013	22178
10013	22183
10013	22188
10003	22188
10007	22193
10013	22193
10009	22193
10013	22198
10099	22203
10013	22208
10107	22213
10013	22218
10013	22223
10013	22228
10047	22228
10013	22233
10047	22233
10013	22238
10013	22243
10013	22248
10013	22253
10013	22258
10047	22258
10013	22263
10047	22263
10013	22268
10013	22273
10007	22278
10013	22278
10009	22278
10013	22283
10013	16218
10013	22290
10013	22295
10047	22295
10007	22300
10013	22300
10009	22300
10025	22305
10107	22310
10107	22315
10013	22320
10007	22325
10013	22325
10009	22325
10007	22330
10013	22330
10009	22330
10007	22335
10013	22335
10009	22335
10013	22340
10099	22345
10099	22350
10099	22355
10013	22360
10025	22365
10013	22370
10097	22375
10013	22380
10013	22385
10013	22390
10013	22395
10097	22395
10013	22400
10097	22400
10013	22405
10013	22410
10047	22410
10013	22415
10025	22420
10007	22425
10013	22425
10009	22425
10013	22430
10013	22435
10013	22440
10013	22445
10013	22450
10013	22455
10013	22460
10013	22465
10013	22470
10013	22475
10013	22480
10013	22485
10013	22490
10013	22495
10007	22500
10013	22500
10009	22500
10007	22505
10013	22505
10009	22505
10013	22510
10003	22510
10013	22515
10013	22520
10013	22525
10013	22530
10013	22535
10025	22540
10025	22545
10007	22550
10013	22550
10009	22550
10013	22555
10013	22560
10003	22560
10097	22565
10103	22570
10099	22575
10051	22580
10025	22585
10013	22590
10013	22595
10013	22600
10013	22605
10013	22610
10013	22615
10013	22620
10013	22625
10103	22630
10013	22635
10069	22640
10013	22640
10013	22645
10013	22650
10025	22655
10025	22660
10013	22665
10025	22665
10069	22670
10013	22670
10069	22675
10013	22675
10069	22680
10013	22680
10069	22685
10013	22685
10013	22690
10013	22695
10013	22700
10013	22705
10043	22710
10013	22710
10043	22715
10013	22715
10013	22720
10013	22725
10025	22730
10051	22735
10025	22740
10025	22745
10025	22750
10025	22755
10013	22760
10047	22760
10013	22765
10097	22765
10013	22770
10013	22775
10097	22775
10013	22780
10013	22785
10097	22785
10013	22790
10013	22795
10007	22800
10013	22800
10009	22800
10013	22805
10013	22810
10051	22815
10025	22820
10051	22825
10051	22830
10013	22835
10097	22835
10013	22840
10025	22840
10025	22845
10051	22850
10013	22855
10013	22860
10013	16234
10097	16234
10013	22867
10099	22872
10013	22877
10013	22882
10013	22887
10013	22892
10013	22897
10013	22902
10013	22907
10007	22912
10013	22912
10009	22912
10013	22917
10043	22922
10013	22922
10013	22927
10013	22932
10013	22937
10013	22942
10013	22947
10013	22952
10097	22952
10013	22957
10013	22962
10047	22962
10003	22962
10013	22967
10013	22972
10013	22977
10013	22982
10013	22987
10013	22992
10013	22997
10013	23002
10007	23007
10013	23007
10009	23007
10013	23012
10013	23017
10013	23022
10069	23027
10013	23027
10069	23032
10013	23032
10007	23037
10013	23037
10009	23037
10007	23042
10013	23042
10009	23042
10007	23047
10013	23047
10009	23047
10007	23052
10013	23052
10009	23052
10013	23057
10001	23057
10025	23062
10013	23067
10013	23072
10013	23077
10015	23077
10013	23082
10013	23087
10013	23092
10025	23097
10013	23102
10013	23107
10097	23107
10013	23112
10047	23112
10013	23117
10047	23117
10013	23122
10007	23127
10013	23127
10009	23127
10013	23132
10047	23132
10013	23137
10047	23137
10099	23142
10013	23147
10013	23152
10013	23157
10013	23162
10013	23167
10013	23172
10097	23172
10013	23177
10013	23182
10013	23187
10003	23187
10013	23192
10013	23197
10025	23197
10043	23202
10013	23202
10043	23207
10013	23207
10013	23212
10047	23212
10013	23217
10013	23222
10013	23227
10013	23232
10013	23237
10013	23242
10013	23247
10013	23252
10013	23257
10013	23262
10013	23267
10013	23272
10013	23277
10013	23282
10013	23287
10013	23292
10013	23297
10013	23302
10013	23307
10013	23312
10013	23317
10013	23322
10013	23327
10013	23332
10013	23337
10013	23342
10013	23347
10013	23352
10013	23357
10013	23362
10013	23367
10013	23372
10013	23377
10013	23382
10013	23387
10013	23392
10013	23397
10013	23402
10013	23407
10043	23412
10013	23412
10043	23417
10013	23417
10043	23422
10013	23422
10007	23427
10013	23427
10009	23427
10007	23432
10013	23432
10009	23432
10013	23437
10097	23437
10013	23442
10043	23447
10013	23447
10013	23452
10007	23457
10013	23457
10009	23457
10007	23462
10013	23462
10009	23462
10007	23467
10013	23467
10009	23467
10013	23472
10069	23477
10013	23477
10069	23482
10013	23482
10013	23487
10013	23492
10013	23497
10013	23502
10013	23507
10013	23512
10025	23517
10025	23522
10025	23527
10025	23532
10025	23537
10013	23542
10025	23542
10025	23547
10013	23552
10025	23552
10007	23557
10013	23557
10009	23557
10013	23562
10043	23567
10013	23567
10047	23567
10043	23572
10013	23572
10047	23572
10099	23577
10013	23582
10013	23587
10025	23592
10025	23597
10013	23602
10013	23607
10047	23607
10013	23612
10047	23612
10013	23617
10099	23622
10013	23627
10043	23632
10013	23632
10051	23637
10007	23642
10013	23642
10009	23642
10013	23647
10047	23647
10013	23652
10013	23657
10013	23662
10013	23667
10013	23672
10013	23677
10013	23682
10013	23687
10013	23692
10013	23697
10013	23702
10013	23707
10013	23712
10013	23717
10013	23722
10013	23727
10013	23732
10013	23737
10013	23742
10013	23747
10013	23752
10025	23752
10013	23757
10013	23762
10003	23762
10063	23767
10013	23767
10013	23772
10013	23777
10051	23782
10007	23787
10013	23787
10009	23787
10013	23792
10013	23797
10013	23802
10013	23807
10013	23812
10013	23817
10013	23822
10013	23827
10013	23832
10013	23837
10013	23842
10043	23847
10013	23847
10043	23852
10013	23852
10013	23857
10013	23862
10013	23867
10013	23872
10013	23877
10013	23882
10013	23887
10013	23892
10043	23897
10013	23897
10043	23902
10013	23902
10043	23907
10013	23907
10043	23912
10013	23912
10043	23917
10013	23917
10013	23922
10013	23927
10013	23932
10097	23932
10013	23937
10013	23942
10013	23947
10013	23952
10013	23957
10013	23962
10013	23967
10107	23972
10013	23977
10013	23982
10043	23987
10013	23987
10007	23992
10013	23992
10007	23997
10013	23997
10009	23997
10013	24002
10047	24002
10013	24007
10013	24012
10007	24017
10013	24017
10009	24017
10007	24022
10013	24022
10009	24022
10013	24027
10007	24032
10013	24032
10009	24032
10007	24037
10013	24037
10009	24037
10025	24042
10025	24047
10025	24052
10013	24057
10013	24062
10097	24062
10013	24067
10025	24067
10013	24072
10007	24077
10013	24077
10009	24077
10007	24082
10013	24082
10009	24082
10007	24087
10013	24087
10009	24087
10013	24092
10013	24097
10013	24102
10013	24107
10013	24112
10013	24117
10013	24122
10013	24127
10013	24132
10013	24137
10009	24137
10013	24142
10009	24142
10013	24147
10013	24152
10013	24157
10013	24162
10013	24167
10013	24172
10013	24177
10051	24182
10013	24187
10013	24192
10047	24192
10013	24197
10047	24197
10013	24202
10013	24207
10003	24207
10013	24212
10013	24217
10013	24222
10013	24227
10013	24232
10013	24237
10013	24242
10007	24247
10013	24247
10009	24247
10007	24252
10013	24252
10009	24252
10007	24257
10013	24257
10009	24257
10007	24262
10013	24262
10009	24262
10013	24267
10013	24272
10007	24277
10013	24277
10009	24277
10013	24282
10003	24282
10025	24287
10025	24292
10013	24297
10097	24297
10013	24302
10043	24307
10013	24307
10013	24312
10043	24317
10013	24317
10013	24322
10013	24327
10047	24327
10013	24332
10047	24332
10013	24337
10013	24342
10013	24347
10007	24352
10013	24352
10009	24352
10007	24357
10013	24357
10097	24357
10013	24362
10097	24367
10013	24372
10013	24377
10013	24382
10043	24387
10013	24387
10099	24392
10007	24397
10013	24397
10009	24397
10007	24402
10013	24402
10009	24402
10013	24407
10025	24412
10025	24417
10013	24422
10047	24422
10013	24427
10047	24427
10025	24432
10013	24437
10013	24442
10003	24442
10107	24447
10103	24452
10099	24457
10099	24462
10013	24467
10043	24472
10013	24472
10013	24477
10013	24482
10013	24487
10013	24492
10013	24497
10013	24502
10013	24507
\.


--
-- TOC entry 2894 (class 0 OID 63293)
-- Dependencies: 1804
-- Data for Name: sam_sample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sample (amountdisplayunit, amountunit, batchnum, colposition, currentamount, currentamountflag, initialamount, rowposition, subposition, usebydate, abstractsampleid, assigntoid, holderid, refsampleid) FROM stdin;
\.


--
-- TOC entry 2895 (class 0 OID 63296)
-- Dependencies: 1805
-- Data for Name: sam_samplecomponent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_samplecomponent (concdisplayunit, concentration, concentrationerror, concentrationunit, ph, purity, labbookentryid, abstractsampleid, refcomponentid, researchobjectivelementid, containerid) FROM stdin;
\N	\N	\N	\N	\N	\N	22005	22003	22001	\N	\N
\N	\N	\N	\N	\N	\N	22010	22008	22006	\N	\N
\N	\N	\N	\N	\N	\N	22015	22013	22011	\N	\N
\N	\N	\N	\N	\N	\N	22020	22018	22016	\N	\N
\N	\N	\N	\N	\N	\N	22025	22023	22021	\N	\N
\N	\N	\N	\N	\N	\N	22030	22028	22026	\N	\N
\N	\N	\N	\N	\N	\N	22035	22033	22031	\N	\N
\N	\N	\N	\N	\N	\N	22040	22038	22036	\N	\N
\N	\N	\N	\N	\N	\N	22045	22043	22041	\N	\N
\N	\N	\N	\N	\N	\N	22050	22048	22046	\N	\N
\N	\N	\N	\N	\N	\N	22055	22053	22051	\N	\N
\N	\N	\N	\N	\N	\N	22060	22058	22056	\N	\N
\N	\N	\N	\N	\N	\N	22065	22063	22061	\N	\N
\N	\N	\N	\N	\N	\N	22070	22068	22066	\N	\N
\N	\N	\N	\N	\N	\N	22075	22073	22071	\N	\N
\N	\N	\N	\N	\N	\N	22080	22078	22076	\N	\N
\N	\N	\N	\N	\N	\N	22085	22083	22081	\N	\N
\N	\N	\N	\N	\N	\N	22090	22088	22086	\N	\N
\N	\N	\N	\N	\N	\N	22095	22093	22091	\N	\N
\N	\N	\N	\N	\N	\N	22100	22098	22096	\N	\N
\N	\N	\N	\N	\N	\N	22105	22103	22101	\N	\N
\N	\N	\N	\N	\N	\N	22110	22108	22106	\N	\N
\N	\N	\N	\N	\N	\N	22115	22113	22111	\N	\N
\N	\N	\N	\N	\N	\N	22120	22118	22116	\N	\N
\N	\N	\N	\N	\N	\N	22125	22123	22121	\N	\N
\N	\N	\N	\N	\N	\N	22130	22128	22126	\N	\N
\N	\N	\N	\N	\N	\N	22135	22133	22131	\N	\N
\N	\N	\N	\N	\N	\N	22140	22138	22136	\N	\N
\N	\N	\N	\N	\N	\N	22145	22143	22141	\N	\N
\N	\N	\N	\N	\N	\N	22150	22148	22146	\N	\N
\N	\N	\N	\N	\N	\N	22155	22153	22151	\N	\N
\N	\N	\N	\N	\N	\N	22160	22158	22156	\N	\N
\N	\N	\N	\N	\N	\N	22165	22163	22161	\N	\N
\N	\N	\N	\N	\N	\N	22170	22168	22166	\N	\N
\N	\N	\N	\N	\N	\N	22175	22173	22171	\N	\N
\N	\N	\N	\N	\N	\N	22180	22178	22176	\N	\N
\N	\N	\N	\N	\N	\N	22185	22183	22181	\N	\N
\N	\N	\N	\N	\N	\N	22190	22188	22186	\N	\N
\N	\N	\N	\N	\N	\N	22195	22193	22191	\N	\N
\N	\N	\N	\N	\N	\N	22200	22198	22196	\N	\N
\N	\N	\N	\N	\N	\N	22205	22203	22201	\N	\N
\N	\N	\N	\N	\N	\N	22210	22208	22206	\N	\N
\N	\N	\N	\N	\N	\N	22215	22213	22211	\N	\N
\N	\N	\N	\N	\N	\N	22220	22218	22216	\N	\N
\N	\N	\N	\N	\N	\N	22225	22223	22221	\N	\N
\N	\N	\N	\N	\N	\N	22230	22228	22226	\N	\N
\N	\N	\N	\N	\N	\N	22235	22233	22231	\N	\N
\N	\N	\N	\N	\N	\N	22240	22238	22236	\N	\N
\N	\N	\N	\N	\N	\N	22245	22243	22241	\N	\N
\N	\N	\N	\N	\N	\N	22250	22248	22246	\N	\N
\N	\N	\N	\N	\N	\N	22255	22253	22251	\N	\N
\N	\N	\N	\N	\N	\N	22260	22258	22256	\N	\N
\N	\N	\N	\N	\N	\N	22265	22263	22261	\N	\N
\N	\N	\N	\N	\N	\N	22270	22268	22266	\N	\N
\N	\N	\N	\N	\N	\N	22275	22273	22271	\N	\N
\N	\N	\N	\N	\N	\N	22280	22278	22276	\N	\N
\N	\N	\N	\N	\N	\N	22285	22283	22281	\N	\N
\N	\N	\N	\N	\N	\N	22292	22290	22288	\N	\N
\N	\N	\N	\N	\N	\N	22297	22295	22293	\N	\N
\N	\N	\N	\N	\N	\N	22302	22300	22298	\N	\N
\N	\N	\N	\N	\N	\N	22307	22305	22303	\N	\N
\N	\N	\N	\N	\N	\N	22312	22310	22308	\N	\N
\N	\N	\N	\N	\N	\N	22317	22315	22313	\N	\N
\N	\N	\N	\N	\N	\N	22322	22320	22318	\N	\N
\N	\N	\N	\N	\N	\N	22327	22325	22323	\N	\N
\N	\N	\N	\N	\N	\N	22332	22330	22328	\N	\N
\N	\N	\N	\N	\N	\N	22337	22335	22333	\N	\N
\N	\N	\N	\N	\N	\N	22342	22340	22338	\N	\N
\N	\N	\N	\N	\N	\N	22347	22345	22343	\N	\N
\N	\N	\N	\N	\N	\N	22352	22350	22348	\N	\N
\N	\N	\N	\N	\N	\N	22357	22355	22353	\N	\N
\N	\N	\N	\N	\N	\N	22362	22360	22358	\N	\N
\N	\N	\N	\N	\N	\N	22367	22365	22363	\N	\N
\N	\N	\N	\N	\N	\N	22372	22370	22368	\N	\N
\N	\N	\N	\N	\N	\N	22377	22375	22373	\N	\N
\N	\N	\N	\N	\N	\N	22382	22380	22378	\N	\N
\N	\N	\N	\N	\N	\N	22387	22385	22383	\N	\N
\N	\N	\N	\N	\N	\N	22392	22390	22388	\N	\N
\N	\N	\N	\N	\N	\N	22397	22395	22393	\N	\N
\N	\N	\N	\N	\N	\N	22402	22400	22398	\N	\N
\N	\N	\N	\N	\N	\N	22407	22405	22403	\N	\N
\N	\N	\N	\N	\N	\N	22412	22410	22408	\N	\N
\N	\N	\N	\N	\N	\N	22417	22415	22413	\N	\N
\N	\N	\N	\N	\N	\N	22422	22420	22418	\N	\N
\N	\N	\N	\N	\N	\N	22427	22425	22423	\N	\N
\N	\N	\N	\N	\N	\N	22432	22430	22428	\N	\N
\N	\N	\N	\N	\N	\N	22437	22435	22433	\N	\N
\N	\N	\N	\N	\N	\N	22442	22440	22438	\N	\N
\N	\N	\N	\N	\N	\N	22447	22445	22443	\N	\N
\N	\N	\N	\N	\N	\N	22452	22450	22448	\N	\N
\N	\N	\N	\N	\N	\N	22457	22455	22453	\N	\N
\N	\N	\N	\N	\N	\N	22462	22460	22458	\N	\N
\N	\N	\N	\N	\N	\N	22467	22465	22463	\N	\N
\N	\N	\N	\N	\N	\N	22472	22470	22468	\N	\N
\N	\N	\N	\N	\N	\N	22477	22475	22473	\N	\N
\N	\N	\N	\N	\N	\N	22482	22480	22478	\N	\N
\N	\N	\N	\N	\N	\N	22487	22485	22483	\N	\N
\N	\N	\N	\N	\N	\N	22492	22490	22488	\N	\N
\N	\N	\N	\N	\N	\N	22497	22495	22493	\N	\N
\N	\N	\N	\N	\N	\N	22502	22500	22498	\N	\N
\N	\N	\N	\N	\N	\N	22507	22505	22503	\N	\N
\N	\N	\N	\N	\N	\N	22512	22510	22508	\N	\N
\N	\N	\N	\N	\N	\N	22517	22515	22513	\N	\N
\N	\N	\N	\N	\N	\N	22522	22520	22518	\N	\N
\N	\N	\N	\N	\N	\N	22527	22525	22523	\N	\N
\N	\N	\N	\N	\N	\N	22532	22530	22528	\N	\N
\N	\N	\N	\N	\N	\N	22537	22535	22533	\N	\N
\N	\N	\N	\N	\N	\N	22542	22540	22538	\N	\N
\N	\N	\N	\N	\N	\N	22547	22545	22543	\N	\N
\N	\N	\N	\N	\N	\N	22552	22550	22548	\N	\N
\N	\N	\N	\N	\N	\N	22557	22555	22553	\N	\N
\N	\N	\N	\N	\N	\N	22562	22560	22558	\N	\N
\N	\N	\N	\N	\N	\N	22567	22565	22563	\N	\N
\N	\N	\N	\N	\N	\N	22572	22570	22568	\N	\N
\N	\N	\N	\N	\N	\N	22577	22575	22573	\N	\N
\N	\N	\N	\N	\N	\N	22582	22580	22578	\N	\N
\N	\N	\N	\N	\N	\N	22587	22585	22583	\N	\N
\N	\N	\N	\N	\N	\N	22592	22590	22588	\N	\N
\N	\N	\N	\N	\N	\N	22597	22595	22593	\N	\N
\N	\N	\N	\N	\N	\N	22602	22600	22598	\N	\N
\N	\N	\N	\N	\N	\N	22607	22605	22603	\N	\N
\N	\N	\N	\N	\N	\N	22612	22610	22608	\N	\N
\N	\N	\N	\N	\N	\N	22617	22615	22613	\N	\N
\N	\N	\N	\N	\N	\N	22622	22620	22618	\N	\N
\N	\N	\N	\N	\N	\N	22627	22625	22623	\N	\N
\N	\N	\N	\N	\N	\N	22632	22630	22628	\N	\N
\N	\N	\N	\N	\N	\N	22637	22635	22633	\N	\N
\N	\N	\N	\N	\N	\N	22642	22640	22638	\N	\N
\N	\N	\N	\N	\N	\N	22647	22645	22643	\N	\N
\N	\N	\N	\N	\N	\N	22652	22650	22648	\N	\N
\N	\N	\N	\N	\N	\N	22657	22655	22653	\N	\N
\N	\N	\N	\N	\N	\N	22662	22660	22658	\N	\N
\N	\N	\N	\N	\N	\N	22667	22665	22663	\N	\N
\N	\N	\N	\N	\N	\N	22672	22670	22668	\N	\N
\N	\N	\N	\N	\N	\N	22677	22675	22673	\N	\N
\N	\N	\N	\N	\N	\N	22682	22680	22678	\N	\N
\N	\N	\N	\N	\N	\N	22687	22685	22683	\N	\N
\N	\N	\N	\N	\N	\N	22692	22690	22688	\N	\N
\N	\N	\N	\N	\N	\N	22697	22695	22693	\N	\N
\N	\N	\N	\N	\N	\N	22702	22700	22698	\N	\N
\N	\N	\N	\N	\N	\N	22707	22705	22703	\N	\N
\N	\N	\N	\N	\N	\N	22712	22710	22708	\N	\N
\N	\N	\N	\N	\N	\N	22717	22715	22713	\N	\N
\N	\N	\N	\N	\N	\N	22722	22720	22718	\N	\N
\N	\N	\N	\N	\N	\N	22727	22725	22723	\N	\N
\N	\N	\N	\N	\N	\N	22732	22730	22728	\N	\N
\N	\N	\N	\N	\N	\N	22737	22735	22733	\N	\N
\N	\N	\N	\N	\N	\N	22742	22740	22738	\N	\N
\N	\N	\N	\N	\N	\N	22747	22745	22743	\N	\N
\N	\N	\N	\N	\N	\N	22752	22750	22748	\N	\N
\N	\N	\N	\N	\N	\N	22757	22755	22753	\N	\N
\N	\N	\N	\N	\N	\N	22762	22760	22758	\N	\N
\N	\N	\N	\N	\N	\N	22767	22765	22763	\N	\N
\N	\N	\N	\N	\N	\N	22772	22770	22768	\N	\N
\N	\N	\N	\N	\N	\N	22777	22775	22773	\N	\N
\N	\N	\N	\N	\N	\N	22782	22780	22778	\N	\N
\N	\N	\N	\N	\N	\N	22787	22785	22783	\N	\N
\N	\N	\N	\N	\N	\N	22792	22790	22788	\N	\N
\N	\N	\N	\N	\N	\N	22797	22795	22793	\N	\N
\N	\N	\N	\N	\N	\N	22802	22800	22798	\N	\N
\N	\N	\N	\N	\N	\N	22807	22805	22803	\N	\N
\N	\N	\N	\N	\N	\N	22812	22810	22808	\N	\N
\N	\N	\N	\N	\N	\N	22817	22815	22813	\N	\N
\N	\N	\N	\N	\N	\N	22822	22820	22818	\N	\N
\N	\N	\N	\N	\N	\N	22827	22825	22823	\N	\N
\N	\N	\N	\N	\N	\N	22832	22830	22828	\N	\N
\N	\N	\N	\N	\N	\N	22837	22835	22833	\N	\N
\N	\N	\N	\N	\N	\N	22842	22840	22838	\N	\N
\N	\N	\N	\N	\N	\N	22847	22845	22843	\N	\N
\N	\N	\N	\N	\N	\N	22852	22850	22848	\N	\N
\N	\N	\N	\N	\N	\N	22857	22855	22853	\N	\N
\N	\N	\N	\N	\N	\N	22862	22860	22858	\N	\N
\N	\N	\N	\N	\N	\N	22869	22867	22865	\N	\N
\N	\N	\N	\N	\N	\N	22874	22872	22870	\N	\N
\N	\N	\N	\N	\N	\N	22879	22877	22875	\N	\N
\N	\N	\N	\N	\N	\N	22884	22882	22880	\N	\N
\N	\N	\N	\N	\N	\N	22889	22887	22885	\N	\N
\N	\N	\N	\N	\N	\N	22894	22892	22890	\N	\N
\N	\N	\N	\N	\N	\N	22899	22897	22895	\N	\N
\N	\N	\N	\N	\N	\N	22904	22902	22900	\N	\N
\N	\N	\N	\N	\N	\N	22909	22907	22905	\N	\N
\N	\N	\N	\N	\N	\N	22914	22912	22910	\N	\N
\N	\N	\N	\N	\N	\N	22919	22917	22915	\N	\N
\N	\N	\N	\N	\N	\N	22924	22922	22920	\N	\N
\N	\N	\N	\N	\N	\N	22929	22927	22925	\N	\N
\N	\N	\N	\N	\N	\N	22934	22932	22930	\N	\N
\N	\N	\N	\N	\N	\N	22939	22937	22935	\N	\N
\N	\N	\N	\N	\N	\N	22944	22942	22940	\N	\N
\N	\N	\N	\N	\N	\N	22949	22947	22945	\N	\N
\N	\N	\N	\N	\N	\N	22954	22952	22950	\N	\N
\N	\N	\N	\N	\N	\N	22959	22957	22955	\N	\N
\N	\N	\N	\N	\N	\N	22964	22962	22960	\N	\N
\N	\N	\N	\N	\N	\N	22969	22967	22965	\N	\N
\N	\N	\N	\N	\N	\N	22974	22972	22970	\N	\N
\N	\N	\N	\N	\N	\N	22979	22977	22975	\N	\N
\N	\N	\N	\N	\N	\N	22984	22982	22980	\N	\N
\N	\N	\N	\N	\N	\N	22989	22987	22985	\N	\N
\N	\N	\N	\N	\N	\N	22994	22992	22990	\N	\N
\N	\N	\N	\N	\N	\N	22999	22997	22995	\N	\N
\N	\N	\N	\N	\N	\N	23004	23002	23000	\N	\N
\N	\N	\N	\N	\N	\N	23009	23007	23005	\N	\N
\N	\N	\N	\N	\N	\N	23014	23012	23010	\N	\N
\N	\N	\N	\N	\N	\N	23019	23017	23015	\N	\N
\N	\N	\N	\N	\N	\N	23024	23022	23020	\N	\N
\N	\N	\N	\N	\N	\N	23029	23027	23025	\N	\N
\N	\N	\N	\N	\N	\N	23034	23032	23030	\N	\N
\N	\N	\N	\N	\N	\N	23039	23037	23035	\N	\N
\N	\N	\N	\N	\N	\N	23044	23042	23040	\N	\N
\N	\N	\N	\N	\N	\N	23049	23047	23045	\N	\N
\N	\N	\N	\N	\N	\N	23054	23052	23050	\N	\N
\N	\N	\N	\N	\N	\N	23059	23057	23055	\N	\N
\N	\N	\N	\N	\N	\N	23064	23062	23060	\N	\N
\N	\N	\N	\N	\N	\N	23069	23067	23065	\N	\N
\N	\N	\N	\N	\N	\N	23074	23072	23070	\N	\N
\N	\N	\N	\N	\N	\N	23079	23077	23075	\N	\N
\N	\N	\N	\N	\N	\N	23084	23082	23080	\N	\N
\N	\N	\N	\N	\N	\N	23089	23087	23085	\N	\N
\N	\N	\N	\N	\N	\N	23094	23092	23090	\N	\N
\N	\N	\N	\N	\N	\N	23099	23097	23095	\N	\N
\N	\N	\N	\N	\N	\N	23104	23102	23100	\N	\N
\N	\N	\N	\N	\N	\N	23109	23107	23105	\N	\N
\N	\N	\N	\N	\N	\N	23114	23112	23110	\N	\N
\N	\N	\N	\N	\N	\N	23119	23117	23115	\N	\N
\N	\N	\N	\N	\N	\N	23124	23122	23120	\N	\N
\N	\N	\N	\N	\N	\N	23129	23127	23125	\N	\N
\N	\N	\N	\N	\N	\N	23134	23132	23130	\N	\N
\N	\N	\N	\N	\N	\N	23139	23137	23135	\N	\N
\N	\N	\N	\N	\N	\N	23144	23142	23140	\N	\N
\N	\N	\N	\N	\N	\N	23149	23147	23145	\N	\N
\N	\N	\N	\N	\N	\N	23154	23152	23150	\N	\N
\N	\N	\N	\N	\N	\N	23159	23157	23155	\N	\N
\N	\N	\N	\N	\N	\N	23164	23162	23160	\N	\N
\N	\N	\N	\N	\N	\N	23169	23167	23165	\N	\N
\N	\N	\N	\N	\N	\N	23174	23172	23170	\N	\N
\N	\N	\N	\N	\N	\N	23179	23177	23175	\N	\N
\N	\N	\N	\N	\N	\N	23184	23182	23180	\N	\N
\N	\N	\N	\N	\N	\N	23189	23187	23185	\N	\N
\N	\N	\N	\N	\N	\N	23194	23192	23190	\N	\N
\N	\N	\N	\N	\N	\N	23199	23197	23195	\N	\N
\N	\N	\N	\N	\N	\N	23204	23202	23200	\N	\N
\N	\N	\N	\N	\N	\N	23209	23207	23205	\N	\N
\N	\N	\N	\N	\N	\N	23214	23212	23210	\N	\N
\N	\N	\N	\N	\N	\N	23219	23217	23215	\N	\N
\N	\N	\N	\N	\N	\N	23224	23222	23220	\N	\N
\N	\N	\N	\N	\N	\N	23229	23227	23225	\N	\N
\N	\N	\N	\N	\N	\N	23234	23232	23230	\N	\N
\N	\N	\N	\N	\N	\N	23239	23237	23235	\N	\N
\N	\N	\N	\N	\N	\N	23244	23242	23240	\N	\N
\N	\N	\N	\N	\N	\N	23249	23247	23245	\N	\N
\N	\N	\N	\N	\N	\N	23254	23252	23250	\N	\N
\N	\N	\N	\N	\N	\N	23259	23257	23255	\N	\N
\N	\N	\N	\N	\N	\N	23264	23262	23260	\N	\N
\N	\N	\N	\N	\N	\N	23269	23267	23265	\N	\N
\N	\N	\N	\N	\N	\N	23274	23272	23270	\N	\N
\N	\N	\N	\N	\N	\N	23279	23277	23275	\N	\N
\N	\N	\N	\N	\N	\N	23284	23282	23280	\N	\N
\N	\N	\N	\N	\N	\N	23289	23287	23285	\N	\N
\N	\N	\N	\N	\N	\N	23294	23292	23290	\N	\N
\N	\N	\N	\N	\N	\N	23299	23297	23295	\N	\N
\N	\N	\N	\N	\N	\N	23304	23302	23300	\N	\N
\N	\N	\N	\N	\N	\N	23309	23307	23305	\N	\N
\N	\N	\N	\N	\N	\N	23314	23312	23310	\N	\N
\N	\N	\N	\N	\N	\N	23319	23317	23315	\N	\N
\N	\N	\N	\N	\N	\N	23324	23322	23320	\N	\N
\N	\N	\N	\N	\N	\N	23329	23327	23325	\N	\N
\N	\N	\N	\N	\N	\N	23334	23332	23330	\N	\N
\N	\N	\N	\N	\N	\N	23339	23337	23335	\N	\N
\N	\N	\N	\N	\N	\N	23344	23342	23340	\N	\N
\N	\N	\N	\N	\N	\N	23349	23347	23345	\N	\N
\N	\N	\N	\N	\N	\N	23354	23352	23350	\N	\N
\N	\N	\N	\N	\N	\N	23359	23357	23355	\N	\N
\N	\N	\N	\N	\N	\N	23364	23362	23360	\N	\N
\N	\N	\N	\N	\N	\N	23369	23367	23365	\N	\N
\N	\N	\N	\N	\N	\N	23374	23372	23370	\N	\N
\N	\N	\N	\N	\N	\N	23379	23377	23375	\N	\N
\N	\N	\N	\N	\N	\N	23384	23382	23380	\N	\N
\N	\N	\N	\N	\N	\N	23389	23387	23385	\N	\N
\N	\N	\N	\N	\N	\N	23394	23392	23390	\N	\N
\N	\N	\N	\N	\N	\N	23399	23397	23395	\N	\N
\N	\N	\N	\N	\N	\N	23404	23402	23400	\N	\N
\N	\N	\N	\N	\N	\N	23409	23407	23405	\N	\N
\N	\N	\N	\N	\N	\N	23414	23412	23410	\N	\N
\N	\N	\N	\N	\N	\N	23419	23417	23415	\N	\N
\N	\N	\N	\N	\N	\N	23424	23422	23420	\N	\N
\N	\N	\N	\N	\N	\N	23429	23427	23425	\N	\N
\N	\N	\N	\N	\N	\N	23434	23432	23430	\N	\N
\N	\N	\N	\N	\N	\N	23439	23437	23435	\N	\N
\N	\N	\N	\N	\N	\N	23444	23442	23440	\N	\N
\N	\N	\N	\N	\N	\N	23449	23447	23445	\N	\N
\N	\N	\N	\N	\N	\N	23454	23452	23450	\N	\N
\N	\N	\N	\N	\N	\N	23459	23457	23455	\N	\N
\N	\N	\N	\N	\N	\N	23464	23462	23460	\N	\N
\N	\N	\N	\N	\N	\N	23469	23467	23465	\N	\N
\N	\N	\N	\N	\N	\N	23474	23472	23470	\N	\N
\N	\N	\N	\N	\N	\N	23479	23477	23475	\N	\N
\N	\N	\N	\N	\N	\N	23484	23482	23480	\N	\N
\N	\N	\N	\N	\N	\N	23489	23487	23485	\N	\N
\N	\N	\N	\N	\N	\N	23494	23492	23490	\N	\N
\N	\N	\N	\N	\N	\N	23499	23497	23495	\N	\N
\N	\N	\N	\N	\N	\N	23504	23502	23500	\N	\N
\N	\N	\N	\N	\N	\N	23509	23507	23505	\N	\N
\N	\N	\N	\N	\N	\N	23514	23512	23510	\N	\N
\N	\N	\N	\N	\N	\N	23519	23517	23515	\N	\N
\N	\N	\N	\N	\N	\N	23524	23522	23520	\N	\N
\N	\N	\N	\N	\N	\N	23529	23527	23525	\N	\N
\N	\N	\N	\N	\N	\N	23534	23532	23530	\N	\N
\N	\N	\N	\N	\N	\N	23539	23537	23535	\N	\N
\N	\N	\N	\N	\N	\N	23544	23542	23540	\N	\N
\N	\N	\N	\N	\N	\N	23549	23547	23545	\N	\N
\N	\N	\N	\N	\N	\N	23554	23552	23550	\N	\N
\N	\N	\N	\N	\N	\N	23559	23557	23555	\N	\N
\N	\N	\N	\N	\N	\N	23564	23562	23560	\N	\N
\N	\N	\N	\N	\N	\N	23569	23567	23565	\N	\N
\N	\N	\N	\N	\N	\N	23574	23572	23570	\N	\N
\N	\N	\N	\N	\N	\N	23579	23577	23575	\N	\N
\N	\N	\N	\N	\N	\N	23584	23582	23580	\N	\N
\N	\N	\N	\N	\N	\N	23589	23587	23585	\N	\N
\N	\N	\N	\N	\N	\N	23594	23592	23590	\N	\N
\N	\N	\N	\N	\N	\N	23599	23597	23595	\N	\N
\N	\N	\N	\N	\N	\N	23604	23602	23600	\N	\N
\N	\N	\N	\N	\N	\N	23609	23607	23605	\N	\N
\N	\N	\N	\N	\N	\N	23614	23612	23610	\N	\N
\N	\N	\N	\N	\N	\N	23619	23617	23615	\N	\N
\N	\N	\N	\N	\N	\N	23624	23622	23620	\N	\N
\N	\N	\N	\N	\N	\N	23629	23627	23625	\N	\N
\N	\N	\N	\N	\N	\N	23634	23632	23630	\N	\N
\N	\N	\N	\N	\N	\N	23639	23637	23635	\N	\N
\N	\N	\N	\N	\N	\N	23644	23642	23640	\N	\N
\N	\N	\N	\N	\N	\N	23649	23647	23645	\N	\N
\N	\N	\N	\N	\N	\N	23654	23652	23650	\N	\N
\N	\N	\N	\N	\N	\N	23659	23657	23655	\N	\N
\N	\N	\N	\N	\N	\N	23664	23662	23660	\N	\N
\N	\N	\N	\N	\N	\N	23669	23667	23665	\N	\N
\N	\N	\N	\N	\N	\N	23674	23672	23670	\N	\N
\N	\N	\N	\N	\N	\N	23679	23677	23675	\N	\N
\N	\N	\N	\N	\N	\N	23684	23682	23680	\N	\N
\N	\N	\N	\N	\N	\N	23689	23687	23685	\N	\N
\N	\N	\N	\N	\N	\N	23694	23692	23690	\N	\N
\N	\N	\N	\N	\N	\N	23699	23697	23695	\N	\N
\N	\N	\N	\N	\N	\N	23704	23702	23700	\N	\N
\N	\N	\N	\N	\N	\N	23709	23707	23705	\N	\N
\N	\N	\N	\N	\N	\N	23714	23712	23710	\N	\N
\N	\N	\N	\N	\N	\N	23719	23717	23715	\N	\N
\N	\N	\N	\N	\N	\N	23724	23722	23720	\N	\N
\N	\N	\N	\N	\N	\N	23729	23727	23725	\N	\N
\N	\N	\N	\N	\N	\N	23734	23732	23730	\N	\N
\N	\N	\N	\N	\N	\N	23739	23737	23735	\N	\N
\N	\N	\N	\N	\N	\N	23744	23742	23740	\N	\N
\N	\N	\N	\N	\N	\N	23749	23747	23745	\N	\N
\N	\N	\N	\N	\N	\N	23754	23752	23750	\N	\N
\N	\N	\N	\N	\N	\N	23759	23757	23755	\N	\N
\N	\N	\N	\N	\N	\N	23764	23762	23760	\N	\N
\N	\N	\N	\N	\N	\N	23769	23767	23765	\N	\N
\N	\N	\N	\N	\N	\N	23774	23772	23770	\N	\N
\N	\N	\N	\N	\N	\N	23779	23777	23775	\N	\N
\N	\N	\N	\N	\N	\N	23784	23782	23780	\N	\N
\N	\N	\N	\N	\N	\N	23789	23787	23785	\N	\N
\N	\N	\N	\N	\N	\N	23794	23792	23790	\N	\N
\N	\N	\N	\N	\N	\N	23799	23797	23795	\N	\N
\N	\N	\N	\N	\N	\N	23804	23802	23800	\N	\N
\N	\N	\N	\N	\N	\N	23809	23807	23805	\N	\N
\N	\N	\N	\N	\N	\N	23814	23812	23810	\N	\N
\N	\N	\N	\N	\N	\N	23819	23817	23815	\N	\N
\N	\N	\N	\N	\N	\N	23824	23822	23820	\N	\N
\N	\N	\N	\N	\N	\N	23829	23827	23825	\N	\N
\N	\N	\N	\N	\N	\N	23834	23832	23830	\N	\N
\N	\N	\N	\N	\N	\N	23839	23837	23835	\N	\N
\N	\N	\N	\N	\N	\N	23844	23842	23840	\N	\N
\N	\N	\N	\N	\N	\N	23849	23847	23845	\N	\N
\N	\N	\N	\N	\N	\N	23854	23852	23850	\N	\N
\N	\N	\N	\N	\N	\N	23859	23857	23855	\N	\N
\N	\N	\N	\N	\N	\N	23864	23862	23860	\N	\N
\N	\N	\N	\N	\N	\N	23869	23867	23865	\N	\N
\N	\N	\N	\N	\N	\N	23874	23872	23870	\N	\N
\N	\N	\N	\N	\N	\N	23879	23877	23875	\N	\N
\N	\N	\N	\N	\N	\N	23884	23882	23880	\N	\N
\N	\N	\N	\N	\N	\N	23889	23887	23885	\N	\N
\N	\N	\N	\N	\N	\N	23894	23892	23890	\N	\N
\N	\N	\N	\N	\N	\N	23899	23897	23895	\N	\N
\N	\N	\N	\N	\N	\N	23904	23902	23900	\N	\N
\N	\N	\N	\N	\N	\N	23909	23907	23905	\N	\N
\N	\N	\N	\N	\N	\N	23914	23912	23910	\N	\N
\N	\N	\N	\N	\N	\N	23919	23917	23915	\N	\N
\N	\N	\N	\N	\N	\N	23924	23922	23920	\N	\N
\N	\N	\N	\N	\N	\N	23929	23927	23925	\N	\N
\N	\N	\N	\N	\N	\N	23934	23932	23930	\N	\N
\N	\N	\N	\N	\N	\N	23939	23937	23935	\N	\N
\N	\N	\N	\N	\N	\N	23944	23942	23940	\N	\N
\N	\N	\N	\N	\N	\N	23949	23947	23945	\N	\N
\N	\N	\N	\N	\N	\N	23954	23952	23950	\N	\N
\N	\N	\N	\N	\N	\N	23959	23957	23955	\N	\N
\N	\N	\N	\N	\N	\N	23964	23962	23960	\N	\N
\N	\N	\N	\N	\N	\N	23969	23967	23965	\N	\N
\N	\N	\N	\N	\N	\N	23974	23972	23970	\N	\N
\N	\N	\N	\N	\N	\N	23979	23977	23975	\N	\N
\N	\N	\N	\N	\N	\N	23984	23982	23980	\N	\N
\N	\N	\N	\N	\N	\N	23989	23987	23985	\N	\N
\N	\N	\N	\N	\N	\N	23994	23992	23990	\N	\N
\N	\N	\N	\N	\N	\N	23999	23997	23995	\N	\N
\N	\N	\N	\N	\N	\N	24004	24002	24000	\N	\N
\N	\N	\N	\N	\N	\N	24009	24007	24005	\N	\N
\N	\N	\N	\N	\N	\N	24014	24012	24010	\N	\N
\N	\N	\N	\N	\N	\N	24019	24017	24015	\N	\N
\N	\N	\N	\N	\N	\N	24024	24022	24020	\N	\N
\N	\N	\N	\N	\N	\N	24029	24027	24025	\N	\N
\N	\N	\N	\N	\N	\N	24034	24032	24030	\N	\N
\N	\N	\N	\N	\N	\N	24039	24037	24035	\N	\N
\N	\N	\N	\N	\N	\N	24044	24042	24040	\N	\N
\N	\N	\N	\N	\N	\N	24049	24047	24045	\N	\N
\N	\N	\N	\N	\N	\N	24054	24052	24050	\N	\N
\N	\N	\N	\N	\N	\N	24059	24057	24055	\N	\N
\N	\N	\N	\N	\N	\N	24064	24062	24060	\N	\N
\N	\N	\N	\N	\N	\N	24069	24067	24065	\N	\N
\N	\N	\N	\N	\N	\N	24074	24072	24070	\N	\N
\N	\N	\N	\N	\N	\N	24079	24077	24075	\N	\N
\N	\N	\N	\N	\N	\N	24084	24082	24080	\N	\N
\N	\N	\N	\N	\N	\N	24089	24087	24085	\N	\N
\N	\N	\N	\N	\N	\N	24094	24092	24090	\N	\N
\N	\N	\N	\N	\N	\N	24099	24097	24095	\N	\N
\N	\N	\N	\N	\N	\N	24104	24102	24100	\N	\N
\N	\N	\N	\N	\N	\N	24109	24107	24105	\N	\N
\N	\N	\N	\N	\N	\N	24114	24112	24110	\N	\N
\N	\N	\N	\N	\N	\N	24119	24117	24115	\N	\N
\N	\N	\N	\N	\N	\N	24124	24122	24120	\N	\N
\N	\N	\N	\N	\N	\N	24129	24127	24125	\N	\N
\N	\N	\N	\N	\N	\N	24134	24132	24130	\N	\N
\N	\N	\N	\N	\N	\N	24139	24137	24135	\N	\N
\N	\N	\N	\N	\N	\N	24144	24142	24140	\N	\N
\N	\N	\N	\N	\N	\N	24149	24147	24145	\N	\N
\N	\N	\N	\N	\N	\N	24154	24152	24150	\N	\N
\N	\N	\N	\N	\N	\N	24159	24157	24155	\N	\N
\N	\N	\N	\N	\N	\N	24164	24162	24160	\N	\N
\N	\N	\N	\N	\N	\N	24169	24167	24165	\N	\N
\N	\N	\N	\N	\N	\N	24174	24172	24170	\N	\N
\N	\N	\N	\N	\N	\N	24179	24177	24175	\N	\N
\N	\N	\N	\N	\N	\N	24184	24182	24180	\N	\N
\N	\N	\N	\N	\N	\N	24189	24187	24185	\N	\N
\N	\N	\N	\N	\N	\N	24194	24192	24190	\N	\N
\N	\N	\N	\N	\N	\N	24199	24197	24195	\N	\N
\N	\N	\N	\N	\N	\N	24204	24202	24200	\N	\N
\N	\N	\N	\N	\N	\N	24209	24207	24205	\N	\N
\N	\N	\N	\N	\N	\N	24214	24212	24210	\N	\N
\N	\N	\N	\N	\N	\N	24219	24217	24215	\N	\N
\N	\N	\N	\N	\N	\N	24224	24222	24220	\N	\N
\N	\N	\N	\N	\N	\N	24229	24227	24225	\N	\N
\N	\N	\N	\N	\N	\N	24234	24232	24230	\N	\N
\N	\N	\N	\N	\N	\N	24239	24237	24235	\N	\N
\N	\N	\N	\N	\N	\N	24244	24242	24240	\N	\N
\N	\N	\N	\N	\N	\N	24249	24247	24245	\N	\N
\N	\N	\N	\N	\N	\N	24254	24252	24250	\N	\N
\N	\N	\N	\N	\N	\N	24259	24257	24255	\N	\N
\N	\N	\N	\N	\N	\N	24264	24262	24260	\N	\N
\N	\N	\N	\N	\N	\N	24269	24267	24265	\N	\N
\N	\N	\N	\N	\N	\N	24274	24272	24270	\N	\N
\N	\N	\N	\N	\N	\N	24279	24277	24275	\N	\N
\N	\N	\N	\N	\N	\N	24284	24282	24280	\N	\N
\N	\N	\N	\N	\N	\N	24289	24287	24285	\N	\N
\N	\N	\N	\N	\N	\N	24294	24292	24290	\N	\N
\N	\N	\N	\N	\N	\N	24299	24297	24295	\N	\N
\N	\N	\N	\N	\N	\N	24304	24302	24300	\N	\N
\N	\N	\N	\N	\N	\N	24309	24307	24305	\N	\N
\N	\N	\N	\N	\N	\N	24314	24312	24310	\N	\N
\N	\N	\N	\N	\N	\N	24319	24317	24315	\N	\N
\N	\N	\N	\N	\N	\N	24324	24322	24320	\N	\N
\N	\N	\N	\N	\N	\N	24329	24327	24325	\N	\N
\N	\N	\N	\N	\N	\N	24334	24332	24330	\N	\N
\N	\N	\N	\N	\N	\N	24339	24337	24335	\N	\N
\N	\N	\N	\N	\N	\N	24344	24342	24340	\N	\N
\N	\N	\N	\N	\N	\N	24349	24347	24345	\N	\N
\N	\N	\N	\N	\N	\N	24354	24352	24350	\N	\N
\N	\N	\N	\N	\N	\N	24359	24357	24355	\N	\N
\N	\N	\N	\N	\N	\N	24364	24362	24360	\N	\N
\N	\N	\N	\N	\N	\N	24369	24367	24365	\N	\N
\N	\N	\N	\N	\N	\N	24374	24372	24370	\N	\N
\N	\N	\N	\N	\N	\N	24379	24377	24375	\N	\N
\N	\N	\N	\N	\N	\N	24384	24382	24380	\N	\N
\N	\N	\N	\N	\N	\N	24389	24387	24385	\N	\N
\N	\N	\N	\N	\N	\N	24394	24392	24390	\N	\N
\N	\N	\N	\N	\N	\N	24399	24397	24395	\N	\N
\N	\N	\N	\N	\N	\N	24404	24402	24400	\N	\N
\N	\N	\N	\N	\N	\N	24409	24407	24405	\N	\N
\N	\N	\N	\N	\N	\N	24414	24412	24410	\N	\N
\N	\N	\N	\N	\N	\N	24419	24417	24415	\N	\N
\N	\N	\N	\N	\N	\N	24424	24422	24420	\N	\N
\N	\N	\N	\N	\N	\N	24429	24427	24425	\N	\N
\N	\N	\N	\N	\N	\N	24434	24432	24430	\N	\N
\N	\N	\N	\N	\N	\N	24439	24437	24435	\N	\N
\N	\N	\N	\N	\N	\N	24444	24442	24440	\N	\N
\N	\N	\N	\N	\N	\N	24449	24447	24445	\N	\N
\N	\N	\N	\N	\N	\N	24454	24452	24450	\N	\N
\N	\N	\N	\N	\N	\N	24459	24457	24455	\N	\N
\N	\N	\N	\N	\N	\N	24464	24462	24460	\N	\N
\N	\N	\N	\N	\N	\N	24469	24467	24465	\N	\N
\N	\N	\N	\N	\N	\N	24474	24472	24470	\N	\N
\N	\N	\N	\N	\N	\N	24479	24477	24475	\N	\N
\N	\N	\N	\N	\N	\N	24484	24482	24480	\N	\N
\N	\N	\N	\N	\N	\N	24489	24487	24485	\N	\N
\N	\N	\N	\N	\N	\N	24494	24492	24490	\N	\N
\N	\N	\N	\N	\N	\N	24499	24497	24495	\N	\N
\N	\N	\N	\N	\N	\N	24504	24502	24500	\N	\N
\N	\N	\N	\N	\N	\N	24509	24507	24505	\N	\N
\.


--
-- TOC entry 2896 (class 0 OID 63299)
-- Dependencies: 1806
-- Data for Name: sche_scheduledtask; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduledtask (priority, scheduledtime, state, labbookentryid, completiontime, name, holderid, scheduleplanoffsetid, instrumentid) FROM stdin;
\.


--
-- TOC entry 2897 (class 0 OID 63302)
-- Dependencies: 1807
-- Data for Name: sche_scheduleplan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplan (name, labbookentryid) FROM stdin;
\.


--
-- TOC entry 2898 (class 0 OID 63305)
-- Dependencies: 1808
-- Data for Name: sche_scheduleplanoffset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplanoffset (offsettime, priority, labbookentryid, order_, scheduleplanid) FROM stdin;
\.


--
-- TOC entry 2908 (class 0 OID 64704)
-- Dependencies: 1819
-- Data for Name: targ_alias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_alias (labbookentryid, name, targetid) FROM stdin;
\.


--
-- TOC entry 2899 (class 0 OID 63308)
-- Dependencies: 1809
-- Data for Name: targ_milestone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_milestone (date_, labbookentryid, statusid, experimentid, targetid) FROM stdin;
\.


--
-- TOC entry 2900 (class 0 OID 63311)
-- Dependencies: 1810
-- Data for Name: targ_project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_project (completename, shortname, startdate, labbookentryid, projectid) FROM stdin;
public	public	\N	8001	\N
\.


--
-- TOC entry 2901 (class 0 OID 63317)
-- Dependencies: 1811
-- Data for Name: targ_researchobjective; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_researchobjective (biochemicalfunction, biologicalprocess, catalyticactivity, celllocation, commonname, functiondescription, localname, pathway, similaritydetails, systematicname, whychosen, labbookentryid, ownerid) FROM stdin;
\.


--
-- TOC entry 2902 (class 0 OID 63323)
-- Dependencies: 1812
-- Data for Name: targ_target; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target (biochemicalfunction, biologicalprocess, catalyticactivity, celllocation, functiondescription, genename, name, orf, pathway, similaritydetails, topology, whychosen, labbookentryid, speciesid, proteinid) FROM stdin;
\.


--
-- TOC entry 2903 (class 0 OID 63329)
-- Dependencies: 1813
-- Data for Name: targ_target2nuclac; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target2nuclac (nuctargetid, nucleicacidid) FROM stdin;
\.


--
-- TOC entry 2904 (class 0 OID 63332)
-- Dependencies: 1814
-- Data for Name: targ_target2projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target2projects (targetid, projectid) FROM stdin;
\.


--
-- TOC entry 2905 (class 0 OID 63338)
-- Dependencies: 1815
-- Data for Name: targ_targetgroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_targetgroup (completename, groupingtype, namingsystem, shortname, labbookentryid, targetgroupid) FROM stdin;
\.


--
-- TOC entry 2906 (class 0 OID 63344)
-- Dependencies: 1816
-- Data for Name: targ_targgr2targets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_targgr2targets (targetgroupid, targetid) FROM stdin;
\.


--
-- TOC entry 2907 (class 0 OID 63349)
-- Dependencies: 1818
-- Data for Name: trag_researchobjectiveelement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY trag_researchobjectiveelement (alwaysincluded, approxbeginseqid, approxendseqid, componenttype, domain, status, whychosen, labbookentryid, researchobjectiveid, targetid, moleculeid) FROM stdin;
\.


--
-- TOC entry 2089 (class 2606 OID 65177)
-- Dependencies: 1711 1711 1711 1711 1711 1711
-- Name: acco_permission_permissionclass_optype_rolename_accessobjectid_; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_permissionclass_optype_rolename_accessobjectid_ UNIQUE (permissionclass, optype, rolename, accessobjectid, usergroupid);


--
-- TOC entry 2091 (class 2606 OID 63358)
-- Dependencies: 1711 1711
-- Name: acco_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_pkey PRIMARY KEY (systemclassid);


--
-- TOC entry 2094 (class 2606 OID 65163)
-- Dependencies: 1712 1712
-- Name: acco_user_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2097 (class 2606 OID 63362)
-- Dependencies: 1712 1712
-- Name: acco_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_pkey PRIMARY KEY (systemclassid);


--
-- TOC entry 2104 (class 2606 OID 65125)
-- Dependencies: 1714 1714 1714
-- Name: acco_usergroup2leaders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_usergroup2leaders_pkey PRIMARY KEY (leaderid, ledgroupid);


--
-- TOC entry 2106 (class 2606 OID 65137)
-- Dependencies: 1715 1715 1715
-- Name: acco_usergroup2members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_usergroup2members_pkey PRIMARY KEY (memberid, usergroupid);


--
-- TOC entry 2100 (class 2606 OID 65155)
-- Dependencies: 1713 1713
-- Name: acco_usergroup_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2102 (class 2606 OID 63370)
-- Dependencies: 1713 1713
-- Name: acco_usergroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_pkey PRIMARY KEY (systemclassid);


--
-- TOC entry 2108 (class 2606 OID 65179)
-- Dependencies: 1716 1716
-- Name: core_accessobject_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_accessobject_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2110 (class 2606 OID 63374)
-- Dependencies: 1716 1716
-- Name: core_accessobject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_accessobject_pkey PRIMARY KEY (systemclassid);


--
-- TOC entry 2112 (class 2606 OID 63376)
-- Dependencies: 1717 1717
-- Name: core_annotation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT core_annotation_pkey PRIMARY KEY (attachmentid);


--
-- TOC entry 2114 (class 2606 OID 63378)
-- Dependencies: 1718 1718
-- Name: core_applicationdata_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT core_applicationdata_pkey PRIMARY KEY (attachmentid);


--
-- TOC entry 2118 (class 2606 OID 63380)
-- Dependencies: 1719 1719
-- Name: core_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_pkey PRIMARY KEY (dbid);


--
-- TOC entry 2122 (class 2606 OID 63384)
-- Dependencies: 1721 1721
-- Name: core_bookcitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT core_bookcitation_pkey PRIMARY KEY (citationid);


--
-- TOC entry 2124 (class 2606 OID 63386)
-- Dependencies: 1722 1722
-- Name: core_citation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT core_citation_pkey PRIMARY KEY (attachmentid);


--
-- TOC entry 2126 (class 2606 OID 63388)
-- Dependencies: 1723 1723
-- Name: core_conferencecitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT core_conferencecitation_pkey PRIMARY KEY (citationid);


--
-- TOC entry 2129 (class 2606 OID 63390)
-- Dependencies: 1724 1724
-- Name: core_externaldblink_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_externaldblink_pkey PRIMARY KEY (attachmentid);


--
-- TOC entry 2131 (class 2606 OID 63392)
-- Dependencies: 1725 1725
-- Name: core_journalcitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT core_journalcitation_pkey PRIMARY KEY (citationid);


--
-- TOC entry 2136 (class 2606 OID 63394)
-- Dependencies: 1726 1726
-- Name: core_labbookentry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labbookentry_pkey PRIMARY KEY (dbid);


--
-- TOC entry 2140 (class 2606 OID 65923)
-- Dependencies: 1728 1728 1728
-- Name: core_note2relatedentrys_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note2relatedentrys_pkey PRIMARY KEY (labbookentryid, noteid);


--
-- TOC entry 2138 (class 2606 OID 63402)
-- Dependencies: 1727 1727
-- Name: core_note_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT core_note_pkey PRIMARY KEY (attachmentid);


--
-- TOC entry 2120 (class 2606 OID 64723)
-- Dependencies: 1720 1720
-- Name: core_systemclass_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_systemclass
    ADD CONSTRAINT core_systemclass_pkey PRIMARY KEY (dbid);


--
-- TOC entry 2142 (class 2606 OID 63404)
-- Dependencies: 1729 1729
-- Name: core_thesiscitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT core_thesiscitation_pkey PRIMARY KEY (citationid);


--
-- TOC entry 2144 (class 2606 OID 63406)
-- Dependencies: 1730 1730 1730
-- Name: cryz_cypade_possva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_cypade_possva
    ADD CONSTRAINT cryz_cypade_possva_pkey PRIMARY KEY (parameterdefinitionid, order_);


--
-- TOC entry 2151 (class 2606 OID 63408)
-- Dependencies: 1731 1731
-- Name: cryz_dropannotation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropannotation_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2153 (class 2606 OID 63410)
-- Dependencies: 1732 1732 1732
-- Name: cryz_image_filepath_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_filepath_key UNIQUE (filepath, filename);


--
-- TOC entry 2157 (class 2606 OID 63412)
-- Dependencies: 1732 1732
-- Name: cryz_image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2163 (class 2606 OID 63414)
-- Dependencies: 1733 1733
-- Name: cryz_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_parameter_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2165 (class 2606 OID 63416)
-- Dependencies: 1734 1734
-- Name: cryz_parameterdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT cryz_parameterdefinition_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2167 (class 2606 OID 63418)
-- Dependencies: 1735 1735
-- Name: cryz_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2170 (class 2606 OID 65523)
-- Dependencies: 1735 1735 1735
-- Name: cryz_score_value_scoringschemeid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_value_scoringschemeid_must_be_unique UNIQUE (value, scoringschemeid);


--
-- TOC entry 2172 (class 2606 OID 65525)
-- Dependencies: 1736 1736
-- Name: cryz_scoringscheme_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2174 (class 2606 OID 63424)
-- Dependencies: 1736 1736
-- Name: cryz_scoringscheme_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2181 (class 2606 OID 65575)
-- Dependencies: 1737 1737
-- Name: expe_experiment_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2184 (class 2606 OID 63428)
-- Dependencies: 1737 1737
-- Name: expe_experiment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2188 (class 2606 OID 63430)
-- Dependencies: 1738 1738
-- Name: expe_experimentgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT expe_experimentgroup_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2193 (class 2606 OID 63432)
-- Dependencies: 1739 1739
-- Name: expe_inputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inputsample_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2198 (class 2606 OID 63434)
-- Dependencies: 1740 1740
-- Name: expe_instrument_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2201 (class 2606 OID 63436)
-- Dependencies: 1741 1741
-- Name: expe_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2204 (class 2606 OID 63438)
-- Dependencies: 1742 1742
-- Name: expe_methodparameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methodparameter_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2210 (class 2606 OID 63440)
-- Dependencies: 1743 1743
-- Name: expe_outputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outputsample_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2214 (class 2606 OID 63444)
-- Dependencies: 1744 1744
-- Name: expe_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2216 (class 2606 OID 65791)
-- Dependencies: 1745 1745 1745
-- Name: expe_software_name_version_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_name_version_must_be_unique UNIQUE (name, version);


--
-- TOC entry 2218 (class 2606 OID 63448)
-- Dependencies: 1745 1745
-- Name: expe_software_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2220 (class 2606 OID 63450)
-- Dependencies: 1746 1746 1746
-- Name: expe_software_tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software_tasks
    ADD CONSTRAINT expe_software_tasks_pkey PRIMARY KEY (softwareid, order_);


--
-- TOC entry 2224 (class 2606 OID 65673)
-- Dependencies: 1749 1749
-- Name: hold_abstractholder_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstractholder_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2226 (class 2606 OID 63454)
-- Dependencies: 1749 1749
-- Name: hold_abstractholder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstractholder_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2228 (class 2606 OID 65655)
-- Dependencies: 1750 1750 1750
-- Name: hold_holdca2abstholders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT hold_holdca2abstholders_pkey PRIMARY KEY (holdercategoryid, abstholderid);


--
-- TOC entry 2230 (class 2606 OID 65873)
-- Dependencies: 1751 1751 1751
-- Name: hold_holdca2absthoty_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT hold_holdca2absthoty_pkey PRIMARY KEY (abstractholdertypeid, holdercategoryid);


--
-- TOC entry 2234 (class 2606 OID 63460)
-- Dependencies: 1752 1752
-- Name: hold_holder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_pkey PRIMARY KEY (abstractholderid);


--
-- TOC entry 2236 (class 2606 OID 63462)
-- Dependencies: 1753 1753
-- Name: hold_holderlocation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holderlocation_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2240 (class 2606 OID 63464)
-- Dependencies: 1754 1754
-- Name: hold_holdertypeposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdertypeposition_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2243 (class 2606 OID 63466)
-- Dependencies: 1755 1755
-- Name: hold_refholder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT hold_refholder_pkey PRIMARY KEY (abstractholderid);


--
-- TOC entry 2245 (class 2606 OID 63468)
-- Dependencies: 1756 1756
-- Name: hold_refholderoffset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refholderoffset_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2247 (class 2606 OID 65423)
-- Dependencies: 1756 1756 1756 1756 1756
-- Name: hold_refholderoffset_rowoffset_coloffset_suboffset_holderid_mus; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refholderoffset_rowoffset_coloffset_suboffset_holderid_mus UNIQUE (rowoffset, coloffset, suboffset, holderid);


--
-- TOC entry 2251 (class 2606 OID 63472)
-- Dependencies: 1757 1757
-- Name: hold_refsampleposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2253 (class 2606 OID 65711)
-- Dependencies: 1757 1757 1757 1757 1757
-- Name: hold_refsampleposition_rowposition_colposition_subposition_refh; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_rowposition_colposition_subposition_refh UNIQUE (rowposition, colposition, subposition, refholderid);


--
-- TOC entry 2257 (class 2606 OID 65843)
-- Dependencies: 1758 1758 1758
-- Name: inst_instty2inst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT inst_instty2inst_pkey PRIMARY KEY (instrumentid, instrumenttypeid);


--
-- TOC entry 2261 (class 2606 OID 63478)
-- Dependencies: 1759 1759
-- Name: loca_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2263 (class 2606 OID 63480)
-- Dependencies: 1760 1760 1760
-- Name: mole_abstco_keywords_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstco_keywords
    ADD CONSTRAINT mole_abstco_keywords_pkey PRIMARY KEY (abstractcomponentid, order_);


--
-- TOC entry 2265 (class 2606 OID 63482)
-- Dependencies: 1761 1761 1761
-- Name: mole_abstco_synonyms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstco_synonyms
    ADD CONSTRAINT mole_abstco_synonyms_pkey PRIMARY KEY (abstractcomponentid, order_);


--
-- TOC entry 2268 (class 2606 OID 65777)
-- Dependencies: 1762 1762
-- Name: mole_abstractcomponent_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstractcomponent_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2270 (class 2606 OID 63486)
-- Dependencies: 1762 1762
-- Name: mole_abstractcomponent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstractcomponent_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2272 (class 2606 OID 65859)
-- Dependencies: 1763 1763 1763
-- Name: mole_compca2components_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT mole_compca2components_pkey PRIMARY KEY (componentid, categoryid);


--
-- TOC entry 2274 (class 2606 OID 63490)
-- Dependencies: 1764 1764
-- Name: mole_construct_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT mole_construct_pkey PRIMARY KEY (moleculeid);


--
-- TOC entry 2278 (class 2606 OID 65779)
-- Dependencies: 1766 1766 1766
-- Name: mole_molecule2relareobel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT mole_molecule2relareobel_pkey PRIMARY KEY (relatedresearchobjectiveelementid, trialmoleculeid);


--
-- TOC entry 2276 (class 2606 OID 63494)
-- Dependencies: 1765 1765
-- Name: mole_molecule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT mole_molecule_pkey PRIMARY KEY (abstractcomponentid);


--
-- TOC entry 2280 (class 2606 OID 63496)
-- Dependencies: 1767 1767
-- Name: mole_moleculefeature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_moleculefeature_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2284 (class 2606 OID 63498)
-- Dependencies: 1768 1768
-- Name: mole_primer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT mole_primer_pkey PRIMARY KEY (moleculeid);


--
-- TOC entry 2287 (class 2606 OID 63500)
-- Dependencies: 1769 1769
-- Name: peop_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2289 (class 2606 OID 63502)
-- Dependencies: 1770 1770 1770
-- Name: peop_orga_addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_orga_addresses
    ADD CONSTRAINT peop_orga_addresses_pkey PRIMARY KEY (organisationid, order_);


--
-- TOC entry 2291 (class 2606 OID 65443)
-- Dependencies: 1771 1771
-- Name: peop_organisation_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_organisation_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2293 (class 2606 OID 63506)
-- Dependencies: 1771 1771
-- Name: peop_organisation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_organisation_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2295 (class 2606 OID 63508)
-- Dependencies: 1772 1772 1772
-- Name: peop_persingr_phonnu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_persingr_phonnu
    ADD CONSTRAINT peop_persingr_phonnu_pkey PRIMARY KEY (personingroupid, order_);


--
-- TOC entry 2300 (class 2606 OID 63510)
-- Dependencies: 1774 1774 1774
-- Name: peop_person_middin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_person_middin
    ADD CONSTRAINT peop_person_middin_pkey PRIMARY KEY (personid, order_);


--
-- TOC entry 2298 (class 2606 OID 63512)
-- Dependencies: 1773 1773
-- Name: peop_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2304 (class 2606 OID 63514)
-- Dependencies: 1775 1775
-- Name: peop_personingroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_personingroup_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2306 (class 2606 OID 63516)
-- Dependencies: 1776 1776 1776
-- Name: prot_parade_possva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parade_possva
    ADD CONSTRAINT prot_parade_possva_pkey PRIMARY KEY (parameterdefinitionid, order_);


--
-- TOC entry 2309 (class 2606 OID 65757)
-- Dependencies: 1777 1777 1777
-- Name: prot_parameterdefinition_name_protocolid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_name_protocolid_must_be_unique UNIQUE (name, protocolid);


--
-- TOC entry 2311 (class 2606 OID 63520)
-- Dependencies: 1777 1777
-- Name: prot_parameterdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2314 (class 2606 OID 65725)
-- Dependencies: 1778 1778
-- Name: prot_protocol_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2316 (class 2606 OID 63524)
-- Dependencies: 1778 1778
-- Name: prot_protocol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2318 (class 2606 OID 63526)
-- Dependencies: 1779 1779 1779
-- Name: prot_protocol_remarks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol_remarks
    ADD CONSTRAINT prot_protocol_remarks_pkey PRIMARY KEY (protocolid, order_);


--
-- TOC entry 2320 (class 2606 OID 63528)
-- Dependencies: 1780 1780
-- Name: prot_refinputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinputsample_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2322 (class 2606 OID 63530)
-- Dependencies: 1780 1780 1780
-- Name: prot_refinputsample_protocolid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinputsample_protocolid_key UNIQUE (protocolid, name);


--
-- TOC entry 2328 (class 2606 OID 63532)
-- Dependencies: 1781 1781
-- Name: prot_refoutputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refoutputsample_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2330 (class 2606 OID 63534)
-- Dependencies: 1781 1781 1781
-- Name: prot_refoutputsample_protocolid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refoutputsample_protocolid_key UNIQUE (protocolid, name);


--
-- TOC entry 2332 (class 2606 OID 65833)
-- Dependencies: 1782 1782
-- Name: ref_abstractholdertype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2334 (class 2606 OID 63538)
-- Dependencies: 1782 1782
-- Name: ref_abstractholdertype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2336 (class 2606 OID 65871)
-- Dependencies: 1783 1783
-- Name: ref_componentcategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_componentcategory_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2338 (class 2606 OID 63542)
-- Dependencies: 1783 1783
-- Name: ref_componentcategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_componentcategory_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2340 (class 2606 OID 63544)
-- Dependencies: 1784 1784
-- Name: ref_crystaltype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT ref_crystaltype_pkey PRIMARY KEY (holdertypeid);


--
-- TOC entry 2342 (class 2606 OID 65829)
-- Dependencies: 1785 1785
-- Name: ref_dbname_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2344 (class 2606 OID 63548)
-- Dependencies: 1785 1785
-- Name: ref_dbname_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2346 (class 2606 OID 65831)
-- Dependencies: 1786 1786
-- Name: ref_experimenttype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2348 (class 2606 OID 63552)
-- Dependencies: 1786 1786
-- Name: ref_experimenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2350 (class 2606 OID 65841)
-- Dependencies: 1787 1787 1787
-- Name: ref_hazardphrase_classification_code_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_classification_code_must_be_unique UNIQUE (classification, code);


--
-- TOC entry 2352 (class 2606 OID 63556)
-- Dependencies: 1787 1787
-- Name: ref_hazardphrase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2354 (class 2606 OID 65885)
-- Dependencies: 1788 1788
-- Name: ref_holdercategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdercategory_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2356 (class 2606 OID 63560)
-- Dependencies: 1788 1788
-- Name: ref_holdercategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdercategory_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2359 (class 2606 OID 63562)
-- Dependencies: 1789 1789
-- Name: ref_holdertype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_pkey PRIMARY KEY (abstractholdertypeid);


--
-- TOC entry 2471 (class 2606 OID 65901)
-- Dependencies: 1820 1820
-- Name: ref_imagetype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2473 (class 2606 OID 65068)
-- Dependencies: 1820 1820
-- Name: ref_imagetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2361 (class 2606 OID 65855)
-- Dependencies: 1790 1790
-- Name: ref_instrumenttype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2363 (class 2606 OID 63566)
-- Dependencies: 1790 1790
-- Name: ref_instrumenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2365 (class 2606 OID 65903)
-- Dependencies: 1791 1791
-- Name: ref_organism_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2367 (class 2606 OID 63570)
-- Dependencies: 1791 1791
-- Name: ref_organism_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2369 (class 2606 OID 63572)
-- Dependencies: 1792 1792
-- Name: ref_pintype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT ref_pintype_pkey PRIMARY KEY (abstractholdertypeid);


--
-- TOC entry 2371 (class 2606 OID 63574)
-- Dependencies: 1793 1793
-- Name: ref_publicentry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_publicentry
    ADD CONSTRAINT ref_publicentry_pkey PRIMARY KEY (dbid);


--
-- TOC entry 2373 (class 2606 OID 65899)
-- Dependencies: 1794 1794
-- Name: ref_samplecategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2375 (class 2606 OID 63578)
-- Dependencies: 1794 1794
-- Name: ref_samplecategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2377 (class 2606 OID 65857)
-- Dependencies: 1795 1795
-- Name: ref_targetstatus_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targetstatus_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2379 (class 2606 OID 63582)
-- Dependencies: 1795 1795
-- Name: ref_targetstatus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targetstatus_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2381 (class 2606 OID 63584)
-- Dependencies: 1796 1796
-- Name: ref_workflowitem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workflowitem_pkey PRIMARY KEY (publicentryid);


--
-- TOC entry 2385 (class 2606 OID 63586)
-- Dependencies: 1797 1797
-- Name: revisionnumber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY revisionnumber
    ADD CONSTRAINT revisionnumber_pkey PRIMARY KEY (revision);


--
-- TOC entry 2387 (class 2606 OID 65341)
-- Dependencies: 1798 1798
-- Name: sam_abstractsample_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2389 (class 2606 OID 63590)
-- Dependencies: 1798 1798
-- Name: sam_abstractsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2391 (class 2606 OID 65317)
-- Dependencies: 1799 1799 1799
-- Name: sam_abstsa2hazaph_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa2hazaph_pkey PRIMARY KEY (hazardphraseid, otherrole);


--
-- TOC entry 2393 (class 2606 OID 63594)
-- Dependencies: 1800 1800
-- Name: sam_crystalsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT sam_crystalsample_pkey PRIMARY KEY (sampleid);


--
-- TOC entry 2395 (class 2606 OID 63596)
-- Dependencies: 1801 1801
-- Name: sam_refsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT sam_refsample_pkey PRIMARY KEY (abstractsampleid);


--
-- TOC entry 2397 (class 2606 OID 65379)
-- Dependencies: 1802 1802 1802 1802
-- Name: sam_refsamplesource_catalognum_refsampleid_refholderid_must_be_; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_catalognum_refsampleid_refholderid_must_be_ UNIQUE (catalognum, refsampleid, refholderid);


--
-- TOC entry 2399 (class 2606 OID 63600)
-- Dependencies: 1802 1802
-- Name: sam_refsamplesource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2404 (class 2606 OID 65329)
-- Dependencies: 1803 1803 1803
-- Name: sam_sampca2abstsa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_sampca2abstsa_pkey PRIMARY KEY (samplecategoryid, abstractsampleid);


--
-- TOC entry 2408 (class 2606 OID 63604)
-- Dependencies: 1804 1804
-- Name: sam_sample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_pkey PRIMARY KEY (abstractsampleid);


--
-- TOC entry 2415 (class 2606 OID 63606)
-- Dependencies: 1805 1805
-- Name: sam_samplecomponent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_samplecomponent_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2417 (class 2606 OID 63610)
-- Dependencies: 1806 1806
-- Name: sche_scheduledtask_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2419 (class 2606 OID 65409)
-- Dependencies: 1806 1806 1806
-- Name: sche_scheduledtask_scheduledtime_holderid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_scheduledtime_holderid_must_be_unique UNIQUE (scheduledtime, holderid);


--
-- TOC entry 2424 (class 2606 OID 65389)
-- Dependencies: 1807 1807
-- Name: sche_scheduleplan_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2426 (class 2606 OID 63616)
-- Dependencies: 1807 1807
-- Name: sche_scheduleplan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2428 (class 2606 OID 65387)
-- Dependencies: 1808 1808 1808
-- Name: sche_scheduleplanoffset_offsettime_scheduleplanid_must_be_uniqu; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheduleplanoffset_offsettime_scheduleplanid_must_be_uniqu UNIQUE (offsettime, scheduleplanid);


--
-- TOC entry 2430 (class 2606 OID 63620)
-- Dependencies: 1808 1808
-- Name: sche_scheduleplanoffset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheduleplanoffset_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2468 (class 2606 OID 64708)
-- Dependencies: 1819 1819
-- Name: targ_alias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2434 (class 2606 OID 63622)
-- Dependencies: 1809 1809
-- Name: targ_milestone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2438 (class 2606 OID 63624)
-- Dependencies: 1810 1810
-- Name: targ_project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2441 (class 2606 OID 64721)
-- Dependencies: 1811 1811
-- Name: targ_researchobjective_commonname_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_researchobjective_commonname_must_be_unique UNIQUE (commonname);


--
-- TOC entry 2443 (class 2606 OID 63626)
-- Dependencies: 1811 1811
-- Name: targ_researchobjective_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_researchobjective_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2452 (class 2606 OID 65217)
-- Dependencies: 1813 1813 1813
-- Name: targ_target2nuclac_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT targ_target2nuclac_pkey PRIMARY KEY (nucleicacidid, nuctargetid);


--
-- TOC entry 2454 (class 2606 OID 65229)
-- Dependencies: 1814 1814 1814
-- Name: targ_target2projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_target2projects_pkey PRIMARY KEY (projectid, targetid);


--
-- TOC entry 2446 (class 2606 OID 65247)
-- Dependencies: 1812 1812
-- Name: targ_target_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_name_must_be_unique UNIQUE (name);


--
-- TOC entry 2448 (class 2606 OID 63636)
-- Dependencies: 1812 1812
-- Name: targ_target_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2456 (class 2606 OID 65285)
-- Dependencies: 1815 1815 1815
-- Name: targ_targetgroup_namingsystem_shortname_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_namingsystem_shortname_must_be_unique UNIQUE (namingsystem, shortname);


--
-- TOC entry 2458 (class 2606 OID 63640)
-- Dependencies: 1815 1815
-- Name: targ_targetgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2461 (class 2606 OID 65273)
-- Dependencies: 1816 1816 1816
-- Name: targ_targgr2targets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_targgr2targets_pkey PRIMARY KEY (targetid, targetgroupid);


--
-- TOC entry 2463 (class 2606 OID 63644)
-- Dependencies: 1818 1818
-- Name: trag_researchobjectiveelement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_researchobjectiveelement_pkey PRIMARY KEY (labbookentryid);


--
-- TOC entry 2087 (class 1259 OID 65175)
-- Dependencies: 1711
-- Name: acco_permission_acceob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_permission_acceob_inx ON acco_permission USING btree (accessobjectid);


--
-- TOC entry 2092 (class 1259 OID 65169)
-- Dependencies: 1711
-- Name: acco_permission_usergroup_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_permission_usergroup_inx ON acco_permission USING btree (usergroupid);


--
-- TOC entry 2095 (class 1259 OID 65161)
-- Dependencies: 1712
-- Name: acco_user_person_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_user_person_inx ON acco_user USING btree (personid);


--
-- TOC entry 2098 (class 1259 OID 65153)
-- Dependencies: 1713
-- Name: acco_usergroup_header_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_usergroup_header_inx ON acco_usergroup USING btree (headerid);


--
-- TOC entry 2115 (class 1259 OID 65909)
-- Dependencies: 1719
-- Name: core_attachment_author_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_attachment_author_inx ON core_attachment USING btree (authorid);


--
-- TOC entry 2116 (class 1259 OID 65915)
-- Dependencies: 1719
-- Name: core_attachment_pareen_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_attachment_pareen_inx ON core_attachment USING btree (parententryid);


--
-- TOC entry 2127 (class 1259 OID 65921)
-- Dependencies: 1724
-- Name: core_extedbli_dbname_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_extedbli_dbname_inx ON core_externaldblink USING btree (dbnameid);


--
-- TOC entry 2132 (class 1259 OID 65191)
-- Dependencies: 1726
-- Name: core_labboen_access_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_access_inx ON core_labbookentry USING btree (accessid);


--
-- TOC entry 2133 (class 1259 OID 65197)
-- Dependencies: 1726
-- Name: core_labboen_creator_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_creator_inx ON core_labbookentry USING btree (creatorid);


--
-- TOC entry 2134 (class 1259 OID 65185)
-- Dependencies: 1726
-- Name: core_labboen_lasteditor_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_lasteditor_inx ON core_labbookentry USING btree (lasteditorid);


--
-- TOC entry 2160 (class 1259 OID 65479)
-- Dependencies: 1733
-- Name: cryz_cypa_cypade_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_cypa_cypade_inx ON cryz_parameter USING btree (parameterdefinitionid);


--
-- TOC entry 2161 (class 1259 OID 65485)
-- Dependencies: 1733
-- Name: cryz_cypa_image_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_cypa_image_inx ON cryz_parameter USING btree (imageid);


--
-- TOC entry 2145 (class 1259 OID 65491)
-- Dependencies: 1731
-- Name: cryz_dropan_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_holder_inx ON cryz_dropannotation USING btree (holderid);


--
-- TOC entry 2146 (class 1259 OID 65503)
-- Dependencies: 1731
-- Name: cryz_dropan_image_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_image_inx ON cryz_dropannotation USING btree (imageid);


--
-- TOC entry 2147 (class 1259 OID 65515)
-- Dependencies: 1731
-- Name: cryz_dropan_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_sample_inx ON cryz_dropannotation USING btree (sampleid);


--
-- TOC entry 2148 (class 1259 OID 65497)
-- Dependencies: 1731
-- Name: cryz_dropan_score_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_score_inx ON cryz_dropannotation USING btree (scoreid);


--
-- TOC entry 2149 (class 1259 OID 65509)
-- Dependencies: 1731
-- Name: cryz_dropan_software_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_software_inx ON cryz_dropannotation USING btree (softwareid);


--
-- TOC entry 2154 (class 1259 OID 65461)
-- Dependencies: 1732
-- Name: cryz_image_imagetype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_imagetype_inx ON cryz_image USING btree (imagetypeid);


--
-- TOC entry 2155 (class 1259 OID 65455)
-- Dependencies: 1732
-- Name: cryz_image_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_instrument_inx ON cryz_image USING btree (instrumentid);


--
-- TOC entry 2158 (class 1259 OID 65473)
-- Dependencies: 1732
-- Name: cryz_image_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_sample_inx ON cryz_image USING btree (sampleid);


--
-- TOC entry 2159 (class 1259 OID 65467)
-- Dependencies: 1732
-- Name: cryz_image_scheta_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_scheta_inx ON cryz_image USING btree (scheduledtaskid);


--
-- TOC entry 2168 (class 1259 OID 65521)
-- Dependencies: 1735
-- Name: cryz_score_scorsc_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_score_scorsc_inx ON cryz_score USING btree (scoringschemeid);


--
-- TOC entry 2175 (class 1259 OID 65555)
-- Dependencies: 1737
-- Name: expe_experiment_expegr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_expegr_inx ON expe_experiment USING btree (experimentgroupid);


--
-- TOC entry 2176 (class 1259 OID 65537)
-- Dependencies: 1737
-- Name: expe_experiment_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_expety_inx ON expe_experiment USING btree (experimenttypeid);


--
-- TOC entry 2177 (class 1259 OID 65573)
-- Dependencies: 1737
-- Name: expe_experiment_group_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_group_inx ON expe_experiment USING btree (groupid);


--
-- TOC entry 2178 (class 1259 OID 65549)
-- Dependencies: 1737
-- Name: expe_experiment_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_instrument_inx ON expe_experiment USING btree (instrumentid);


--
-- TOC entry 2179 (class 1259 OID 65567)
-- Dependencies: 1737
-- Name: expe_experiment_method_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_method_inx ON expe_experiment USING btree (methodid);


--
-- TOC entry 2182 (class 1259 OID 65561)
-- Dependencies: 1737
-- Name: expe_experiment_operator_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_operator_inx ON expe_experiment USING btree (operatorid);


--
-- TOC entry 2185 (class 1259 OID 65531)
-- Dependencies: 1737
-- Name: expe_experiment_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_protocol_inx ON expe_experiment USING btree (protocolid);


--
-- TOC entry 2186 (class 1259 OID 65543)
-- Dependencies: 1737
-- Name: expe_experiment_reseob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_reseob_inx ON expe_experiment USING btree (researchobjectiveid);


--
-- TOC entry 2189 (class 1259 OID 65581)
-- Dependencies: 1739
-- Name: expe_inpusa_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_experiment_inx ON expe_inputsample USING btree (experimentid);


--
-- TOC entry 2190 (class 1259 OID 65587)
-- Dependencies: 1739
-- Name: expe_inpusa_refinsa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_refinsa_inx ON expe_inputsample USING btree (refinputsampleid);


--
-- TOC entry 2191 (class 1259 OID 65593)
-- Dependencies: 1739
-- Name: expe_inpusa_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_sample_inx ON expe_inputsample USING btree (sampleid);


--
-- TOC entry 2194 (class 1259 OID 65821)
-- Dependencies: 1740
-- Name: expe_instrument_defaimty_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_defaimty_inx ON expe_instrument USING btree (defaultimagetypeid);


--
-- TOC entry 2195 (class 1259 OID 65815)
-- Dependencies: 1740
-- Name: expe_instrument_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_location_inx ON expe_instrument USING btree (locationid);


--
-- TOC entry 2196 (class 1259 OID 65827)
-- Dependencies: 1740
-- Name: expe_instrument_manu_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_manu_inx ON expe_instrument USING btree (manufacturerid);


--
-- TOC entry 2199 (class 1259 OID 65803)
-- Dependencies: 1741
-- Name: expe_method_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_method_instrument_inx ON expe_method USING btree (instrumentid);


--
-- TOC entry 2202 (class 1259 OID 65797)
-- Dependencies: 1741
-- Name: expe_method_software_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_method_software_inx ON expe_method USING btree (softwareid);


--
-- TOC entry 2205 (class 1259 OID 65809)
-- Dependencies: 1742
-- Name: expe_methpa_method_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_methpa_method_inx ON expe_methodparameter USING btree (methodid);


--
-- TOC entry 2206 (class 1259 OID 65611)
-- Dependencies: 1743
-- Name: expe_outpsa_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_experiment_inx ON expe_outputsample USING btree (experimentid);


--
-- TOC entry 2207 (class 1259 OID 65617)
-- Dependencies: 1743
-- Name: expe_outpsa_refousa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_refousa_inx ON expe_outputsample USING btree (refoutputsampleid);


--
-- TOC entry 2208 (class 1259 OID 65623)
-- Dependencies: 1743
-- Name: expe_outpsa_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_sample_inx ON expe_outputsample USING btree (sampleid);


--
-- TOC entry 2211 (class 1259 OID 65605)
-- Dependencies: 1744
-- Name: expe_parameter_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_parameter_experiment_inx ON expe_parameter USING btree (experimentid);


--
-- TOC entry 2212 (class 1259 OID 65599)
-- Dependencies: 1744
-- Name: expe_parameter_parade_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_parameter_parade_inx ON expe_parameter USING btree (parameterdefinitionid);


--
-- TOC entry 2221 (class 1259 OID 65671)
-- Dependencies: 1749
-- Name: hold_abstho_holdertype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_abstho_holdertype_inx ON hold_abstractholder USING btree (holdertypeid);


--
-- TOC entry 2222 (class 1259 OID 65653)
-- Dependencies: 1749
-- Name: hold_abstho_supholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_abstho_supholder_inx ON hold_abstractholder USING btree (abstractholderid);


--
-- TOC entry 2231 (class 1259 OID 65679)
-- Dependencies: 1752
-- Name: hold_holder_firssa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holder_firssa_inx ON hold_holder USING btree (firstsampleid);


--
-- TOC entry 2232 (class 1259 OID 65685)
-- Dependencies: 1752
-- Name: hold_holder_lasttask_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holder_lasttask_inx ON hold_holder USING btree (lasttaskid);


--
-- TOC entry 2237 (class 1259 OID 65691)
-- Dependencies: 1753
-- Name: hold_holdlo_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdlo_holder_inx ON hold_holderlocation USING btree (holderid);


--
-- TOC entry 2238 (class 1259 OID 65697)
-- Dependencies: 1753
-- Name: hold_holdlo_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdlo_location_inx ON hold_holderlocation USING btree (locationid);


--
-- TOC entry 2241 (class 1259 OID 65717)
-- Dependencies: 1754
-- Name: hold_holdtypo_holdertype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdtypo_holdertype_inx ON hold_holdertypeposition USING btree (holdertypeid);


--
-- TOC entry 2248 (class 1259 OID 65415)
-- Dependencies: 1756
-- Name: hold_refhoof_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refhoof_holder_inx ON hold_refholderoffset USING btree (holderid);


--
-- TOC entry 2249 (class 1259 OID 65421)
-- Dependencies: 1756
-- Name: hold_refhoof_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refhoof_refholder_inx ON hold_refholderoffset USING btree (refholderid);


--
-- TOC entry 2254 (class 1259 OID 65703)
-- Dependencies: 1757
-- Name: hold_refsapo_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refsapo_refholder_inx ON hold_refsampleposition USING btree (refholderid);


--
-- TOC entry 2255 (class 1259 OID 65709)
-- Dependencies: 1757
-- Name: hold_refsapo_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refsapo_refsample_inx ON hold_refsampleposition USING btree (refsampleid);


--
-- TOC entry 2258 (class 1259 OID 65203)
-- Dependencies: 1759
-- Name: loca_location_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX loca_location_location_inx ON loca_location USING btree (locationid);


--
-- TOC entry 2259 (class 1259 OID 65209)
-- Dependencies: 1759
-- Name: loca_location_orga_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX loca_location_orga_inx ON loca_location USING btree (organisationid);


--
-- TOC entry 2266 (class 1259 OID 65775)
-- Dependencies: 1762
-- Name: mole_abstco_natuso_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_abstco_natuso_inx ON mole_abstractcomponent USING btree (naturalsourceid);


--
-- TOC entry 2281 (class 1259 OID 65769)
-- Dependencies: 1767
-- Name: mole_molefe_molecule_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_molefe_molecule_inx ON mole_moleculefeature USING btree (moleculeid);


--
-- TOC entry 2282 (class 1259 OID 65763)
-- Dependencies: 1767
-- Name: mole_molefe_refmo_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_molefe_refmo_inx ON mole_moleculefeature USING btree (refmoleculeid);


--
-- TOC entry 2285 (class 1259 OID 65429)
-- Dependencies: 1769
-- Name: peop_group_orga_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_group_orga_inx ON peop_group USING btree (organisationid);


--
-- TOC entry 2301 (class 1259 OID 65441)
-- Dependencies: 1775
-- Name: peop_persingr_group_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_persingr_group_inx ON peop_personingroup USING btree (groupid);


--
-- TOC entry 2302 (class 1259 OID 65435)
-- Dependencies: 1775
-- Name: peop_persingr_person_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_persingr_person_inx ON peop_personingroup USING btree (personid);


--
-- TOC entry 2296 (class 1259 OID 65449)
-- Dependencies: 1773
-- Name: peop_person_currgr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_person_currgr_inx ON peop_person USING btree (currentgroupid);


--
-- TOC entry 2307 (class 1259 OID 65755)
-- Dependencies: 1777
-- Name: prot_parade_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_parade_protocol_inx ON prot_parameterdefinition USING btree (protocolid);


--
-- TOC entry 2312 (class 1259 OID 65723)
-- Dependencies: 1778
-- Name: prot_protocol_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_protocol_expety_inx ON prot_protocol USING btree (experimenttypeid);


--
-- TOC entry 2323 (class 1259 OID 65749)
-- Dependencies: 1780
-- Name: prot_refinsa_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refinsa_protocol_inx ON prot_refinputsample USING btree (protocolid);


--
-- TOC entry 2324 (class 1259 OID 65743)
-- Dependencies: 1780
-- Name: prot_refinsa_sampca_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refinsa_sampca_inx ON prot_refinputsample USING btree (samplecategoryid);


--
-- TOC entry 2325 (class 1259 OID 65737)
-- Dependencies: 1781
-- Name: prot_refousa_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refousa_protocol_inx ON prot_refoutputsample USING btree (protocolid);


--
-- TOC entry 2326 (class 1259 OID 65731)
-- Dependencies: 1781
-- Name: prot_refousa_sampca_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refousa_sampca_inx ON prot_refoutputsample USING btree (samplecategoryid);


--
-- TOC entry 2357 (class 1259 OID 65839)
-- Dependencies: 1789
-- Name: ref_holdertype_defascpl_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_holdertype_defascpl_inx ON ref_holdertype USING btree (defaultscheduleplanid);


--
-- TOC entry 2382 (class 1259 OID 65891)
-- Dependencies: 1796
-- Name: ref_workit_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_workit_expety_inx ON ref_workflowitem USING btree (experimenttypeid);


--
-- TOC entry 2383 (class 1259 OID 65897)
-- Dependencies: 1796
-- Name: ref_workit_status_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_workit_status_inx ON ref_workflowitem USING btree (statusid);


--
-- TOC entry 2400 (class 1259 OID 65371)
-- Dependencies: 1802
-- Name: sam_refsaso_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_refholder_inx ON sam_refsamplesource USING btree (refholderid);


--
-- TOC entry 2401 (class 1259 OID 65377)
-- Dependencies: 1802
-- Name: sam_refsaso_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_refsample_inx ON sam_refsamplesource USING btree (refsampleid);


--
-- TOC entry 2402 (class 1259 OID 65365)
-- Dependencies: 1802
-- Name: sam_refsaso_supplier_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_supplier_inx ON sam_refsamplesource USING btree (supplierid);


--
-- TOC entry 2410 (class 1259 OID 65303)
-- Dependencies: 1805
-- Name: sam_sampco_abstsa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_abstsa_inx ON sam_samplecomponent USING btree (abstractsampleid);


--
-- TOC entry 2411 (class 1259 OID 65315)
-- Dependencies: 1805
-- Name: sam_sampco_container_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_container_inx ON sam_samplecomponent USING btree (containerid);


--
-- TOC entry 2412 (class 1259 OID 65309)
-- Dependencies: 1805
-- Name: sam_sampco_refco_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_refco_inx ON sam_samplecomponent USING btree (refcomponentid);


--
-- TOC entry 2413 (class 1259 OID 65297)
-- Dependencies: 1805
-- Name: sam_sampco_reseobel_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_reseobel_inx ON sam_samplecomponent USING btree (researchobjectivelementid);


--
-- TOC entry 2405 (class 1259 OID 65347)
-- Dependencies: 1804
-- Name: sam_sample_assignto_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_assignto_inx ON sam_sample USING btree (assigntoid);


--
-- TOC entry 2406 (class 1259 OID 65353)
-- Dependencies: 1804
-- Name: sam_sample_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_holder_inx ON sam_sample USING btree (holderid);


--
-- TOC entry 2409 (class 1259 OID 65359)
-- Dependencies: 1804
-- Name: sam_sample_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_refsample_inx ON sam_sample USING btree (refsampleid);


--
-- TOC entry 2431 (class 1259 OID 65385)
-- Dependencies: 1808
-- Name: sche_scheplof_schepl_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheplof_schepl_inx ON sche_scheduleplanoffset USING btree (scheduleplanid);


--
-- TOC entry 2420 (class 1259 OID 65395)
-- Dependencies: 1806
-- Name: sche_scheta_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_holder_inx ON sche_scheduledtask USING btree (holderid);


--
-- TOC entry 2421 (class 1259 OID 65407)
-- Dependencies: 1806
-- Name: sche_scheta_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_instrument_inx ON sche_scheduledtask USING btree (instrumentid);


--
-- TOC entry 2422 (class 1259 OID 65401)
-- Dependencies: 1806
-- Name: sche_scheta_scheplof_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_scheplof_inx ON sche_scheduledtask USING btree (scheduleplanoffsetid);


--
-- TOC entry 2469 (class 1259 OID 64719)
-- Dependencies: 1819
-- Name: targ_alias_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_alias_target_inx ON targ_alias USING btree (targetid);


--
-- TOC entry 2432 (class 1259 OID 65259)
-- Dependencies: 1809
-- Name: targ_milestone_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_experiment_inx ON targ_milestone USING btree (experimentid);


--
-- TOC entry 2435 (class 1259 OID 65253)
-- Dependencies: 1809
-- Name: targ_milestone_status_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_status_inx ON targ_milestone USING btree (statusid);


--
-- TOC entry 2436 (class 1259 OID 65265)
-- Dependencies: 1809
-- Name: targ_milestone_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_target_inx ON targ_milestone USING btree (targetid);


--
-- TOC entry 2439 (class 1259 OID 65291)
-- Dependencies: 1810
-- Name: targ_project_project_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_project_project_inx ON targ_project USING btree (projectid);


--
-- TOC entry 2444 (class 1259 OID 65647)
-- Dependencies: 1811
-- Name: targ_reseob_owner_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_reseob_owner_inx ON targ_researchobjective USING btree (ownerid);


--
-- TOC entry 2449 (class 1259 OID 65245)
-- Dependencies: 1812
-- Name: targ_target_protein_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_target_protein_inx ON targ_target USING btree (proteinid);


--
-- TOC entry 2450 (class 1259 OID 65215)
-- Dependencies: 1812
-- Name: targ_target_species_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_target_species_inx ON targ_target USING btree (speciesid);


--
-- TOC entry 2459 (class 1259 OID 65271)
-- Dependencies: 1815
-- Name: targ_targgr_targgr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_targgr_targgr_inx ON targ_targetgroup USING btree (targetgroupid);


--
-- TOC entry 2464 (class 1259 OID 65641)
-- Dependencies: 1818
-- Name: trag_reseobel_molecule_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_molecule_inx ON trag_researchobjectiveelement USING btree (moleculeid);


--
-- TOC entry 2465 (class 1259 OID 65629)
-- Dependencies: 1818
-- Name: trag_reseobel_reseob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_reseob_inx ON trag_researchobjectiveelement USING btree (researchobjectiveid);


--
-- TOC entry 2466 (class 1259 OID 65635)
-- Dependencies: 1818
-- Name: trag_reseobel_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_target_inx ON trag_researchobjectiveelement USING btree (targetid);


--
-- TOC entry 2478 (class 2606 OID 65170)
-- Dependencies: 1716 1711 2109
-- Name: acco_permission_acceob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_acceob_fk FOREIGN KEY (accessobjectid) REFERENCES core_accessobject(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2476 (class 2606 OID 64734)
-- Dependencies: 1720 1711 2119
-- Name: acco_permission_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- TOC entry 2477 (class 2606 OID 65164)
-- Dependencies: 1713 1711 2101
-- Name: acco_permission_usergroup_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_usergroup_fk FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- TOC entry 2485 (class 2606 OID 65126)
-- Dependencies: 1713 1714 2101
-- Name: acco_user_ledgroups_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_user_ledgroups_fk FOREIGN KEY (ledgroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- TOC entry 2480 (class 2606 OID 65156)
-- Dependencies: 2297 1712 1773
-- Name: acco_user_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_person_fk FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2479 (class 2606 OID 64729)
-- Dependencies: 1720 1712 2119
-- Name: acco_user_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- TOC entry 2489 (class 2606 OID 65138)
-- Dependencies: 1713 1715 2101
-- Name: acco_user_usergroups_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_user_usergroups_fk FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- TOC entry 2482 (class 2606 OID 65148)
-- Dependencies: 2096 1713 1712
-- Name: acco_usergroup_header_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_header_fk FOREIGN KEY (headerid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2486 (class 2606 OID 65131)
-- Dependencies: 1712 1714 2096
-- Name: acco_usergroup_leaders_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_usergroup_leaders_fk FOREIGN KEY (leaderid) REFERENCES acco_user(systemclassid) ON DELETE CASCADE;


--
-- TOC entry 2490 (class 2606 OID 65143)
-- Dependencies: 1712 1715 2096
-- Name: acco_usergroup_members_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_usergroup_members_fk FOREIGN KEY (memberid) REFERENCES acco_user(systemclassid) ON DELETE CASCADE;


--
-- TOC entry 2481 (class 2606 OID 64724)
-- Dependencies: 1720 1713 2119
-- Name: acco_usergroup_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- TOC entry 2491 (class 2606 OID 64739)
-- Dependencies: 2119 1716 1720
-- Name: core_acceob_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_acceob_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- TOC entry 2493 (class 2606 OID 65084)
-- Dependencies: 1719 1717 2117
-- Name: core_annotation_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT core_annotation_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- TOC entry 2495 (class 2606 OID 65119)
-- Dependencies: 1718 1719 2117
-- Name: core_applda_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT core_applda_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- TOC entry 2496 (class 2606 OID 65904)
-- Dependencies: 2096 1712 1719
-- Name: core_attachment_author_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_author_fk FOREIGN KEY (authorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2497 (class 2606 OID 65910)
-- Dependencies: 1719 2135 1726
-- Name: core_attachment_pareen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_pareen_fk FOREIGN KEY (parententryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2499 (class 2606 OID 65099)
-- Dependencies: 1722 1721 2123
-- Name: core_bookci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT core_bookci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- TOC entry 2501 (class 2606 OID 65089)
-- Dependencies: 1719 1722 2117
-- Name: core_citation_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT core_citation_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- TOC entry 2503 (class 2606 OID 65104)
-- Dependencies: 1722 1723 2123
-- Name: core_confci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT core_confci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- TOC entry 2505 (class 2606 OID 65079)
-- Dependencies: 1719 1724 2117
-- Name: core_extedbli_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_extedbli_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- TOC entry 2506 (class 2606 OID 65916)
-- Dependencies: 1724 1785 2343
-- Name: core_extedbli_dbname_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_extedbli_dbname_fk FOREIGN KEY (dbnameid) REFERENCES ref_dbname(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2508 (class 2606 OID 65094)
-- Dependencies: 1722 1725 2123
-- Name: core_jourci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT core_jourci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- TOC entry 2511 (class 2606 OID 65186)
-- Dependencies: 1716 1726 2109
-- Name: core_labboen_access_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_access_fk FOREIGN KEY (accessid) REFERENCES core_accessobject(systemclassid);


--
-- TOC entry 2512 (class 2606 OID 65192)
-- Dependencies: 1712 1726 2096
-- Name: core_labboen_creator_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_creator_fk FOREIGN KEY (creatorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2510 (class 2606 OID 65180)
-- Dependencies: 1726 1712 2096
-- Name: core_labboen_lasteditor_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_lasteditor_fk FOREIGN KEY (lasteditorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2514 (class 2606 OID 65114)
-- Dependencies: 2117 1727 1719
-- Name: core_note_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT core_note_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- TOC entry 2517 (class 2606 OID 65924)
-- Dependencies: 1727 1728 2137
-- Name: core_note_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note_otherrole_fk FOREIGN KEY (noteid) REFERENCES core_note(attachmentid) ON DELETE CASCADE;


--
-- TOC entry 2518 (class 2606 OID 65929)
-- Dependencies: 1726 2135 1728
-- Name: core_note_relaen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note_relaen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2520 (class 2606 OID 65109)
-- Dependencies: 2123 1729 1722
-- Name: core_thesci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT core_thesci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- TOC entry 2537 (class 2606 OID 65474)
-- Dependencies: 1734 1733 2164
-- Name: cryz_cypa_cypade_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_cypade_fk FOREIGN KEY (parameterdefinitionid) REFERENCES cryz_parameterdefinition(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2538 (class 2606 OID 65480)
-- Dependencies: 1732 1733 2156
-- Name: cryz_cypa_image_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_image_fk FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2536 (class 2606 OID 64849)
-- Dependencies: 2135 1733 1726
-- Name: cryz_cypa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2540 (class 2606 OID 64839)
-- Dependencies: 1726 1734 2135
-- Name: cryz_cypade_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT cryz_cypade_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2524 (class 2606 OID 65486)
-- Dependencies: 1752 1731 2233
-- Name: cryz_dropan_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE SET NULL;


--
-- TOC entry 2526 (class 2606 OID 65498)
-- Dependencies: 1732 1731 2156
-- Name: cryz_dropan_image_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_image_fk FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2523 (class 2606 OID 64854)
-- Dependencies: 1726 1731 2135
-- Name: cryz_dropan_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2528 (class 2606 OID 65510)
-- Dependencies: 1804 2407 1731
-- Name: cryz_dropan_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2525 (class 2606 OID 65492)
-- Dependencies: 1735 1731 2166
-- Name: cryz_dropan_score_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_score_fk FOREIGN KEY (scoreid) REFERENCES cryz_score(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2527 (class 2606 OID 65504)
-- Dependencies: 1745 1731 2217
-- Name: cryz_dropan_software_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_software_fk FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2532 (class 2606 OID 65456)
-- Dependencies: 1820 1732 2472
-- Name: cryz_image_imagetype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_imagetype_fk FOREIGN KEY (imagetypeid) REFERENCES ref_imagetype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2531 (class 2606 OID 65450)
-- Dependencies: 1740 1732 2197
-- Name: cryz_image_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2530 (class 2606 OID 64844)
-- Dependencies: 1726 2135 1732
-- Name: cryz_image_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2534 (class 2606 OID 65468)
-- Dependencies: 1804 1732 2407
-- Name: cryz_image_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2533 (class 2606 OID 65462)
-- Dependencies: 1806 1732 2416
-- Name: cryz_image_scheta_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_scheta_fk FOREIGN KEY (scheduledtaskid) REFERENCES sche_scheduledtask(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2542 (class 2606 OID 64859)
-- Dependencies: 1726 1735 2135
-- Name: cryz_score_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2543 (class 2606 OID 65516)
-- Dependencies: 2173 1735 1736
-- Name: cryz_score_scorsc_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_scorsc_fk FOREIGN KEY (scoringschemeid) REFERENCES cryz_scoringscheme(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2545 (class 2606 OID 64864)
-- Dependencies: 1726 1736 2135
-- Name: cryz_scorsc_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scorsc_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2557 (class 2606 OID 64869)
-- Dependencies: 1738 2135 1726
-- Name: expe_expegr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT expe_expegr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2552 (class 2606 OID 65550)
-- Dependencies: 1737 1738 2187
-- Name: expe_experiment_expegr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_expegr_fk FOREIGN KEY (experimentgroupid) REFERENCES expe_experimentgroup(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2549 (class 2606 OID 65532)
-- Dependencies: 1786 1737 2347
-- Name: expe_experiment_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2555 (class 2606 OID 65568)
-- Dependencies: 1737 2286 1769
-- Name: expe_experiment_group_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_group_fk FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2551 (class 2606 OID 65544)
-- Dependencies: 1737 1740 2197
-- Name: expe_experiment_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2547 (class 2606 OID 64874)
-- Dependencies: 1726 2135 1737
-- Name: expe_experiment_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2554 (class 2606 OID 65562)
-- Dependencies: 1737 2200 1741
-- Name: expe_experiment_method_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_method_fk FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2553 (class 2606 OID 65556)
-- Dependencies: 1712 2096 1737
-- Name: expe_experiment_operator_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_operator_fk FOREIGN KEY (operatorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- TOC entry 2548 (class 2606 OID 65526)
-- Dependencies: 1737 1778 2315
-- Name: expe_experiment_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2550 (class 2606 OID 65538)
-- Dependencies: 2442 1737 1811
-- Name: expe_experiment_reseob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_reseob_fk FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2560 (class 2606 OID 65576)
-- Dependencies: 1739 2183 1737
-- Name: expe_inpusa_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2559 (class 2606 OID 64879)
-- Dependencies: 2135 1739 1726
-- Name: expe_inpusa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2561 (class 2606 OID 65582)
-- Dependencies: 2319 1780 1739
-- Name: expe_inpusa_refinsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_refinsa_fk FOREIGN KEY (refinputsampleid) REFERENCES prot_refinputsample(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2562 (class 2606 OID 65588)
-- Dependencies: 2407 1804 1739
-- Name: expe_inpusa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2566 (class 2606 OID 65816)
-- Dependencies: 1740 1820 2472
-- Name: expe_instrument_defaimty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_defaimty_fk FOREIGN KEY (defaultimagetypeid) REFERENCES ref_imagetype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2622 (class 2606 OID 65844)
-- Dependencies: 1758 2362 1790
-- Name: expe_instrument_instty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT expe_instrument_instty_fk FOREIGN KEY (instrumenttypeid) REFERENCES ref_instrumenttype(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2564 (class 2606 OID 64994)
-- Dependencies: 1740 1726 2135
-- Name: expe_instrument_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2565 (class 2606 OID 65810)
-- Dependencies: 1740 1759 2260
-- Name: expe_instrument_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2567 (class 2606 OID 65822)
-- Dependencies: 1740 1771 2292
-- Name: expe_instrument_manu_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_manu_fk FOREIGN KEY (manufacturerid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2571 (class 2606 OID 65798)
-- Dependencies: 1741 1740 2197
-- Name: expe_method_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2569 (class 2606 OID 64984)
-- Dependencies: 1741 2135 1726
-- Name: expe_method_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2570 (class 2606 OID 65792)
-- Dependencies: 1741 1745 2217
-- Name: expe_method_software_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_software_fk FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2573 (class 2606 OID 64989)
-- Dependencies: 1742 1726 2135
-- Name: expe_methpa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methpa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2574 (class 2606 OID 65804)
-- Dependencies: 1742 1741 2200
-- Name: expe_methpa_method_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methpa_method_fk FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2577 (class 2606 OID 65606)
-- Dependencies: 1737 2183 1743
-- Name: expe_outpsa_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2576 (class 2606 OID 64889)
-- Dependencies: 2135 1726 1743
-- Name: expe_outpsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2578 (class 2606 OID 65612)
-- Dependencies: 1781 2327 1743
-- Name: expe_outpsa_refousa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_refousa_fk FOREIGN KEY (refoutputsampleid) REFERENCES prot_refoutputsample(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2579 (class 2606 OID 65618)
-- Dependencies: 1804 2407 1743
-- Name: expe_outpsa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2583 (class 2606 OID 65600)
-- Dependencies: 1737 2183 1744
-- Name: expe_parameter_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2581 (class 2606 OID 64884)
-- Dependencies: 1726 2135 1744
-- Name: expe_parameter_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2582 (class 2606 OID 65594)
-- Dependencies: 2310 1777 1744
-- Name: expe_parameter_parade_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_parade_fk FOREIGN KEY (parameterdefinitionid) REFERENCES prot_parameterdefinition(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2585 (class 2606 OID 64979)
-- Dependencies: 1745 1726 2135
-- Name: expe_software_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2610 (class 2606 OID 63645)
-- Dependencies: 1755 1749 2225
-- Name: fk1177e05ff334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT fk1177e05ff334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- TOC entry 2688 (class 2606 OID 63650)
-- Dependencies: 1782 2370 1793
-- Name: fk12b307d4f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT fk12b307d4f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2521 (class 2606 OID 63655)
-- Dependencies: 2164 1730 1734
-- Name: fk12d769d2578d9e96; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_cypade_possva
    ADD CONSTRAINT fk12d769d2578d9e96 FOREIGN KEY (parameterdefinitionid) REFERENCES cryz_parameterdefinition(labbookentryid);


--
-- TOC entry 2671 (class 2606 OID 63660)
-- Dependencies: 1726 1778 2135
-- Name: fk139af87045a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT fk139af87045a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2672 (class 2606 OID 63665)
-- Dependencies: 2347 1786 1778
-- Name: fk139af8709b03e5d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT fk139af8709b03e5d3 FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid);


--
-- TOC entry 2509 (class 2606 OID 63680)
-- Dependencies: 1726 1716 2109
-- Name: fk14ff48dc3ce3876a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT fk14ff48dc3ce3876a FOREIGN KEY (accessid) REFERENCES core_accessobject(systemclassid);


--
-- TOC entry 2507 (class 2606 OID 63695)
-- Dependencies: 1725 2123 1722
-- Name: fk17270e5ef6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT fk17270e5ef6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- TOC entry 2698 (class 2606 OID 63700)
-- Dependencies: 1787 1793 2370
-- Name: fk1d43c997f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT fk1d43c997f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2556 (class 2606 OID 63710)
-- Dependencies: 2135 1738 1726
-- Name: fk1deae42b45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT fk1deae42b45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2637 (class 2606 OID 63715)
-- Dependencies: 1765 1764 2275
-- Name: fk28bb5b13a4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT fk28bb5b13a4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- TOC entry 2580 (class 2606 OID 63720)
-- Dependencies: 1726 1744 2135
-- Name: fk28d12df245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT fk28d12df245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2563 (class 2606 OID 63735)
-- Dependencies: 1726 1740 2135
-- Name: fk292a329e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2773 (class 2606 OID 63745)
-- Dependencies: 1726 1811 2135
-- Name: fk2a93f84145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT fk2a93f84145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2630 (class 2606 OID 63755)
-- Dependencies: 1726 1762 2135
-- Name: fk2cdc1e5745a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT fk2cdc1e5745a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2500 (class 2606 OID 63765)
-- Dependencies: 1719 1722 2117
-- Name: fk3336a78766ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT fk3336a78766ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- TOC entry 2788 (class 2606 OID 63770)
-- Dependencies: 1815 1726 2135
-- Name: fk3363f41145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT fk3363f41145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2607 (class 2606 OID 63780)
-- Dependencies: 1754 2135 1726
-- Name: fk340aa18f45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT fk340aa18f45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2662 (class 2606 OID 63790)
-- Dependencies: 2135 1775 1726
-- Name: fk34b0597c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT fk34b0597c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2731 (class 2606 OID 63805)
-- Dependencies: 2135 1726 1802
-- Name: fk3792bdb845a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb845a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2732 (class 2606 OID 63810)
-- Dependencies: 2292 1802 1771
-- Name: fk3792bdb87f9149b9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb87f9149b9 FOREIGN KEY (supplierid) REFERENCES peop_organisation(labbookentryid);


--
-- TOC entry 2733 (class 2606 OID 63820)
-- Dependencies: 2394 1802 1801
-- Name: fk3792bdb8c7c01158; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb8c7c01158 FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid);


--
-- TOC entry 2700 (class 2606 OID 63825)
-- Dependencies: 1793 2370 1788
-- Name: fk387e5ff6f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT fk387e5ff6f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2667 (class 2606 OID 63830)
-- Dependencies: 1726 2135 1777
-- Name: fk38f93cc445a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT fk38f93cc445a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2668 (class 2606 OID 63835)
-- Dependencies: 2315 1777 1778
-- Name: fk38f93cc46f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT fk38f93cc46f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- TOC entry 2729 (class 2606 OID 63850)
-- Dependencies: 1801 1798 2388
-- Name: fk3e8c1b5d3e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT fk3e8c1b5d3e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- TOC entry 2639 (class 2606 OID 63855)
-- Dependencies: 1765 2269 1762
-- Name: fk45b3e7aac405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT fk45b3e7aac405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- TOC entry 2572 (class 2606 OID 63865)
-- Dependencies: 2135 1726 1742
-- Name: fk4718237145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT fk4718237145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2513 (class 2606 OID 63870)
-- Dependencies: 1727 2117 1719
-- Name: fk4942a49266ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT fk4942a49266ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- TOC entry 2535 (class 2606 OID 63880)
-- Dependencies: 1733 2135 1726
-- Name: fk4a04edda45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT fk4a04edda45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2742 (class 2606 OID 63890)
-- Dependencies: 1804 2388 1798
-- Name: fk4ab4c22a3e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22a3e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- TOC entry 2690 (class 2606 OID 63910)
-- Dependencies: 1793 1783 2370
-- Name: fk55cebc0ff8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT fk55cebc0ff8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2770 (class 2606 OID 63915)
-- Dependencies: 2135 1810 1726
-- Name: fk57c8291c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT fk57c8291c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2649 (class 2606 OID 63925)
-- Dependencies: 1765 2275 1768
-- Name: fk5e0f47f7a4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT fk5e0f47f7a4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- TOC entry 2713 (class 2606 OID 63930)
-- Dependencies: 1793 2370 1795
-- Name: fk5e9b390ff8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT fk5e9b390ff8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2620 (class 2606 OID 63935)
-- Dependencies: 1758 1740 2197
-- Name: fk5e9e49419c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT fk5e9e49419c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- TOC entry 2621 (class 2606 OID 63940)
-- Dependencies: 2362 1790 1758
-- Name: fk5e9e49432c1d927; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT fk5e9e49432c1d927 FOREIGN KEY (instrumenttypeid) REFERENCES ref_instrumenttype(publicentryid);


--
-- TOC entry 2776 (class 2606 OID 63945)
-- Dependencies: 2135 1726 1812
-- Name: fk6390c30e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT fk6390c30e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2727 (class 2606 OID 63960)
-- Dependencies: 1800 2407 1804
-- Name: fk6c4167c098dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT fk6c4167c098dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- TOC entry 2755 (class 2606 OID 63965)
-- Dependencies: 1806 1726 2135
-- Name: fk703f362045a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f362045a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2483 (class 2606 OID 63995)
-- Dependencies: 2101 1713 1714
-- Name: fk724b8b7b5e56f7c7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT fk724b8b7b5e56f7c7 FOREIGN KEY (ledgroupid) REFERENCES acco_usergroup(systemclassid);


--
-- TOC entry 2484 (class 2606 OID 64000)
-- Dependencies: 1712 1714 2096
-- Name: fk724b8b7b9a61164b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT fk724b8b7b9a61164b FOREIGN KEY (leaderid) REFERENCES acco_user(systemclassid);


--
-- TOC entry 2575 (class 2606 OID 64005)
-- Dependencies: 1726 1743 2135
-- Name: fk77cc02e245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2494 (class 2606 OID 64025)
-- Dependencies: 1718 1719 2117
-- Name: fk78db483a66ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT fk78db483a66ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- TOC entry 2765 (class 2606 OID 64030)
-- Dependencies: 1809 2135 1726
-- Name: fk7c3c7eb345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT fk7c3c7eb345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2612 (class 2606 OID 64050)
-- Dependencies: 1726 2135 1756
-- Name: fk7c4a35b245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT fk7c4a35b245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2498 (class 2606 OID 64065)
-- Dependencies: 1722 1721 2123
-- Name: fk7de59170f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT fk7de59170f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- TOC entry 2723 (class 2606 OID 64070)
-- Dependencies: 2388 1798 1799
-- Name: fk84a833fa6d03126f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT fk84a833fa6d03126f FOREIGN KEY (otherrole) REFERENCES sam_abstractsample(labbookentryid);


--
-- TOC entry 2724 (class 2606 OID 64075)
-- Dependencies: 2351 1799 1787
-- Name: fk84a833fabe6bb47b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT fk84a833fabe6bb47b FOREIGN KEY (hazardphraseid) REFERENCES ref_hazardphrase(publicentryid);


--
-- TOC entry 2628 (class 2606 OID 64080)
-- Dependencies: 2269 1760 1762
-- Name: fk85e2b8f7c405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstco_keywords
    ADD CONSTRAINT fk85e2b8f7c405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- TOC entry 2654 (class 2606 OID 64085)
-- Dependencies: 2292 1770 1771
-- Name: fk8636d8899b62c547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_orga_addresses
    ADD CONSTRAINT fk8636d8899b62c547 FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid);


--
-- TOC entry 2522 (class 2606 OID 64100)
-- Dependencies: 1731 1726 2135
-- Name: fk8779ffad45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2651 (class 2606 OID 64115)
-- Dependencies: 2135 1726 1769
-- Name: fk8a00037645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT fk8a00037645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2780 (class 2606 OID 64125)
-- Dependencies: 1813 2447 1812
-- Name: fk8ccd4c764983192f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT fk8ccd4c764983192f FOREIGN KEY (nuctargetid) REFERENCES targ_target(labbookentryid);


--
-- TOC entry 2781 (class 2606 OID 64130)
-- Dependencies: 1765 2275 1813
-- Name: fk8ccd4c76d488084e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT fk8ccd4c76d488084e FOREIGN KEY (nucleicacidid) REFERENCES mole_molecule(abstractcomponentid);


--
-- TOC entry 2747 (class 2606 OID 64140)
-- Dependencies: 1805 1798 2388
-- Name: fk8e3939f33e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f33e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- TOC entry 2748 (class 2606 OID 64150)
-- Dependencies: 1726 1805 2135
-- Name: fk8e3939f345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2749 (class 2606 OID 64155)
-- Dependencies: 1805 1762 2269
-- Name: fk8e3939f3918b07df; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f3918b07df FOREIGN KEY (refcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- TOC entry 2519 (class 2606 OID 64165)
-- Dependencies: 2123 1729 1722
-- Name: fk96337253f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT fk96337253f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- TOC entry 2645 (class 2606 OID 64175)
-- Dependencies: 1767 2135 1726
-- Name: fk96600bec45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT fk96600bec45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2655 (class 2606 OID 64185)
-- Dependencies: 1726 1771 2135
-- Name: fk987c4be345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT fk987c4be345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2568 (class 2606 OID 64195)
-- Dependencies: 1741 2135 1726
-- Name: fk98bf9b1845a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT fk98bf9b1845a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2721 (class 2606 OID 64220)
-- Dependencies: 1798 2135 1726
-- Name: fk9a3000cc45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT fk9a3000cc45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2603 (class 2606 OID 64225)
-- Dependencies: 1726 1753 2135
-- Name: fka28270e145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT fka28270e145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2715 (class 2606 OID 64240)
-- Dependencies: 1796 2347 1786
-- Name: fka54d839e9b03e5d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839e9b03e5d3 FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid);


--
-- TOC entry 2716 (class 2606 OID 64245)
-- Dependencies: 2378 1796 1795
-- Name: fka54d839eabbc44da; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839eabbc44da FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid);


--
-- TOC entry 2717 (class 2606 OID 64250)
-- Dependencies: 1793 2370 1796
-- Name: fka54d839ef8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839ef8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2487 (class 2606 OID 64255)
-- Dependencies: 1712 2096 1715
-- Name: fka7d9f80a16a03b9c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT fka7d9f80a16a03b9c FOREIGN KEY (memberid) REFERENCES acco_user(systemclassid);


--
-- TOC entry 2488 (class 2606 OID 64260)
-- Dependencies: 1713 2101 1715
-- Name: fka7d9f80ac72f3607; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT fka7d9f80ac72f3607 FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid);


--
-- TOC entry 2760 (class 2606 OID 64265)
-- Dependencies: 1807 2135 1726
-- Name: fka8cd91f245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT fka8cd91f245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2709 (class 2606 OID 64270)
-- Dependencies: 2333 1782 1792
-- Name: fka949ba63938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT fka949ba63938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- TOC entry 2711 (class 2606 OID 64275)
-- Dependencies: 1794 1793 2370
-- Name: fkace29f4f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT fkace29f4f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2705 (class 2606 OID 64280)
-- Dependencies: 1793 1790 2370
-- Name: fkad88e8edf8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT fkad88e8edf8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2492 (class 2606 OID 64285)
-- Dependencies: 1717 2117 1719
-- Name: fkb1cfdbcf66ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT fkb1cfdbcf66ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- TOC entry 2682 (class 2606 OID 64290)
-- Dependencies: 2135 1726 1781
-- Name: fkb1d35d0645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d0645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2683 (class 2606 OID 64295)
-- Dependencies: 1778 1781 2315
-- Name: fkb1d35d066f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d066f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- TOC entry 2684 (class 2606 OID 64300)
-- Dependencies: 2374 1781 1794
-- Name: fkb1d35d06b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d06b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- TOC entry 2633 (class 2606 OID 64305)
-- Dependencies: 2337 1783 1763
-- Name: fkb2e6e6579cc3e68a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fkb2e6e6579cc3e68a FOREIGN KEY (categoryid) REFERENCES ref_componentcategory(publicentryid);


--
-- TOC entry 2634 (class 2606 OID 64310)
-- Dependencies: 1763 1762 2269
-- Name: fkb2e6e657d24dca52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fkb2e6e657d24dca52 FOREIGN KEY (componentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- TOC entry 2696 (class 2606 OID 64315)
-- Dependencies: 1793 1786 2370
-- Name: fkb37f6603f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT fkb37f6603f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2657 (class 2606 OID 64320)
-- Dependencies: 2303 1772 1775
-- Name: fkb5660fced18a88ad; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_persingr_phonnu
    ADD CONSTRAINT fkb5660fced18a88ad FOREIGN KEY (personingroupid) REFERENCES peop_personingroup(labbookentryid);


--
-- TOC entry 2694 (class 2606 OID 64325)
-- Dependencies: 1793 1785 2370
-- Name: fkb6ca7555f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT fkb6ca7555f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2515 (class 2606 OID 64330)
-- Dependencies: 2137 1728 1727
-- Name: fkbb2a4c2c1488c4a7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT fkbb2a4c2c1488c4a7 FOREIGN KEY (noteid) REFERENCES core_note(attachmentid);


--
-- TOC entry 2516 (class 2606 OID 64335)
-- Dependencies: 1726 1728 2135
-- Name: fkbb2a4c2c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT fkbb2a4c2c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2616 (class 2606 OID 64340)
-- Dependencies: 2135 1757 1726
-- Name: fkbe85648645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT fkbe85648645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2661 (class 2606 OID 64355)
-- Dependencies: 1774 1773 2297
-- Name: fkbeda3f82b5ac617d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person_middin
    ADD CONSTRAINT fkbeda3f82b5ac617d FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid);


--
-- TOC entry 2591 (class 2606 OID 64360)
-- Dependencies: 1750 2355 1788
-- Name: fkc400ddb0651418f9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT fkc400ddb0651418f9 FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid);


--
-- TOC entry 2592 (class 2606 OID 64365)
-- Dependencies: 2225 1750 1749
-- Name: fkc400ddb0e1a02ab2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT fkc400ddb0e1a02ab2 FOREIGN KEY (abstholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- TOC entry 2641 (class 2606 OID 64370)
-- Dependencies: 1766 1765 2275
-- Name: fkc46d9b1d7f20b0de; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT fkc46d9b1d7f20b0de FOREIGN KEY (trialmoleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- TOC entry 2642 (class 2606 OID 64375)
-- Dependencies: 2462 1818 1766
-- Name: fkc46d9b1dbf1274b8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT fkc46d9b1dbf1274b8 FOREIGN KEY (relatedresearchobjectiveelementid) REFERENCES trag_researchobjectiveelement(labbookentryid);


--
-- TOC entry 2658 (class 2606 OID 64380)
-- Dependencies: 1726 1773 2135
-- Name: fkc4a62d1e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT fkc4a62d1e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2529 (class 2606 OID 64395)
-- Dependencies: 2135 1732 1726
-- Name: fkc9a2ac0c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2541 (class 2606 OID 64410)
-- Dependencies: 1726 1735 2135
-- Name: fkca2b414345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT fkca2b414345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2558 (class 2606 OID 64415)
-- Dependencies: 1726 1739 2135
-- Name: fkced55fd45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT fkced55fd45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2738 (class 2606 OID 64435)
-- Dependencies: 2388 1803 1798
-- Name: fkcfdca6f33e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT fkcfdca6f33e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- TOC entry 2739 (class 2606 OID 64440)
-- Dependencies: 1803 1794 2374
-- Name: fkcfdca6f3b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT fkcfdca6f3b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- TOC entry 2584 (class 2606 OID 64445)
-- Dependencies: 1745 2135 1726
-- Name: fkd053761e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT fkd053761e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2502 (class 2606 OID 64450)
-- Dependencies: 2123 1722 1723
-- Name: fkd0c3b4a3f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT fkd0c3b4a3f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- TOC entry 2546 (class 2606 OID 64465)
-- Dependencies: 1726 2135 1737
-- Name: fkd0e6aab445a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab445a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2702 (class 2606 OID 64505)
-- Dependencies: 2333 1789 1782
-- Name: fkd1fd7632938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT fkd1fd7632938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- TOC entry 2544 (class 2606 OID 64510)
-- Dependencies: 2135 1726 1736
-- Name: fkd23f4ee545a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT fkd23f4ee545a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2624 (class 2606 OID 64515)
-- Dependencies: 1726 1759 2135
-- Name: fkd293391345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT fkd293391345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2629 (class 2606 OID 64530)
-- Dependencies: 1762 2269 1761
-- Name: fkd3b25d25c405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstco_synonyms
    ADD CONSTRAINT fkd3b25d25c405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- TOC entry 2784 (class 2606 OID 64535)
-- Dependencies: 2447 1812 1814
-- Name: fkd41b835e5336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT fkd41b835e5336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- TOC entry 2785 (class 2606 OID 64540)
-- Dependencies: 1814 2437 1810
-- Name: fkd41b835e58259e17; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT fkd41b835e58259e17 FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid);


--
-- TOC entry 2795 (class 2606 OID 64545)
-- Dependencies: 2135 1726 1818
-- Name: fkd4df77f945a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT fkd4df77f945a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2587 (class 2606 OID 64565)
-- Dependencies: 1726 2135 1749
-- Name: fkd6ff0bce45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT fkd6ff0bce45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2676 (class 2606 OID 64580)
-- Dependencies: 1726 2135 1780
-- Name: fkdd40275945a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd40275945a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2677 (class 2606 OID 64585)
-- Dependencies: 1778 2315 1780
-- Name: fkdd4027596f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd4027596f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- TOC entry 2678 (class 2606 OID 64590)
-- Dependencies: 1794 2374 1780
-- Name: fkdd402759b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd402759b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- TOC entry 2707 (class 2606 OID 64595)
-- Dependencies: 1791 1793 2370
-- Name: fke721b71ef8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT fke721b71ef8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- TOC entry 2791 (class 2606 OID 64600)
-- Dependencies: 1812 1816 2447
-- Name: fke8fbe9ba5336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT fke8fbe9ba5336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- TOC entry 2792 (class 2606 OID 64605)
-- Dependencies: 1815 1816 2457
-- Name: fke8fbe9ba8b86e8a1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT fke8fbe9ba8b86e8a1 FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid);


--
-- TOC entry 2762 (class 2606 OID 64610)
-- Dependencies: 2135 1726 1808
-- Name: fkf0b2d0545a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT fkf0b2d0545a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2666 (class 2606 OID 64620)
-- Dependencies: 1777 1776 2310
-- Name: fkf2966e5059eec984; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parade_possva
    ADD CONSTRAINT fkf2966e5059eec984 FOREIGN KEY (parameterdefinitionid) REFERENCES prot_parameterdefinition(labbookentryid);


--
-- TOC entry 2504 (class 2606 OID 64625)
-- Dependencies: 1719 2117 1724
-- Name: fkf63805a366ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT fkf63805a366ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- TOC entry 2474 (class 2606 OID 64645)
-- Dependencies: 2109 1711 1716
-- Name: fkf8cc82a0a3967be9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT fkf8cc82a0a3967be9 FOREIGN KEY (accessobjectid) REFERENCES core_accessobject(systemclassid);


--
-- TOC entry 2475 (class 2606 OID 64650)
-- Dependencies: 1711 1713 2101
-- Name: fkf8cc82a0c72f3607; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT fkf8cc82a0c72f3607 FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid);


--
-- TOC entry 2539 (class 2606 OID 64655)
-- Dependencies: 2135 1734 1726
-- Name: fkf9b406ad45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT fkf9b406ad45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- TOC entry 2675 (class 2606 OID 64660)
-- Dependencies: 1779 1778 2315
-- Name: fkfb7c68046f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol_remarks
    ADD CONSTRAINT fkfb7c68046f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- TOC entry 2692 (class 2606 OID 64665)
-- Dependencies: 2358 1784 1789
-- Name: fkfbc9a184b4e81f71; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT fkfbc9a184b4e81f71 FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid);


--
-- TOC entry 2595 (class 2606 OID 64675)
-- Dependencies: 1751 2355 1788
-- Name: fkfd882103651418f9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT fkfd882103651418f9 FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid);


--
-- TOC entry 2596 (class 2606 OID 64680)
-- Dependencies: 1751 2333 1782
-- Name: fkfd882103938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT fkfd882103938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- TOC entry 2586 (class 2606 OID 64685)
-- Dependencies: 1746 1745 2217
-- Name: fkff38ce0df92d0173; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software_tasks
    ADD CONSTRAINT fkff38ce0df92d0173 FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid);


--
-- TOC entry 2599 (class 2606 OID 64690)
-- Dependencies: 2225 1752 1749
-- Name: fkffe6cd2cf334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT fkffe6cd2cf334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- TOC entry 2594 (class 2606 OID 65661)
-- Dependencies: 1750 1788 2355
-- Name: hold_abstho_holdca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT hold_abstho_holdca_fk FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2590 (class 2606 OID 65666)
-- Dependencies: 1749 1782 2333
-- Name: hold_abstho_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2588 (class 2606 OID 64904)
-- Dependencies: 2135 1726 1749
-- Name: hold_abstho_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2593 (class 2606 OID 65656)
-- Dependencies: 1750 1749 2225
-- Name: hold_abstho_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT hold_abstho_otherrole_fk FOREIGN KEY (abstholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2589 (class 2606 OID 65648)
-- Dependencies: 2225 1749 1749
-- Name: hold_abstho_supholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_supholder_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2600 (class 2606 OID 64909)
-- Dependencies: 1752 2225 1749
-- Name: hold_holder_abstho_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_abstho_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2601 (class 2606 OID 65674)
-- Dependencies: 1752 1804 2407
-- Name: hold_holder_firssa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_firssa_fk FOREIGN KEY (firstsampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2602 (class 2606 OID 65680)
-- Dependencies: 1752 1806 2416
-- Name: hold_holder_lasttask_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_lasttask_fk FOREIGN KEY (lasttaskid) REFERENCES sche_scheduledtask(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2605 (class 2606 OID 65686)
-- Dependencies: 1752 2233 1753
-- Name: hold_holdlo_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- TOC entry 2604 (class 2606 OID 64919)
-- Dependencies: 1726 2135 1753
-- Name: hold_holdlo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2606 (class 2606 OID 65692)
-- Dependencies: 1753 1759 2260
-- Name: hold_holdlo_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2609 (class 2606 OID 65712)
-- Dependencies: 1754 1789 2358
-- Name: hold_holdtypo_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdtypo_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid) ON DELETE CASCADE;


--
-- TOC entry 2608 (class 2606 OID 64929)
-- Dependencies: 1726 1754 2135
-- Name: hold_holdtypo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdtypo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2611 (class 2606 OID 64914)
-- Dependencies: 1755 2225 1749
-- Name: hold_refholder_abstho_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT hold_refholder_abstho_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2614 (class 2606 OID 65410)
-- Dependencies: 1752 1756 2233
-- Name: hold_refhoof_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- TOC entry 2613 (class 2606 OID 64814)
-- Dependencies: 1756 2135 1726
-- Name: hold_refhoof_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2615 (class 2606 OID 65416)
-- Dependencies: 1755 1756 2242
-- Name: hold_refhoof_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE SET NULL;


--
-- TOC entry 2617 (class 2606 OID 64924)
-- Dependencies: 2135 1757 1726
-- Name: hold_refsapo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2618 (class 2606 OID 65698)
-- Dependencies: 1757 1755 2242
-- Name: hold_refsapo_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE CASCADE;


--
-- TOC entry 2619 (class 2606 OID 65704)
-- Dependencies: 1757 1801 2394
-- Name: hold_refsapo_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2625 (class 2606 OID 64744)
-- Dependencies: 1759 1726 2135
-- Name: loca_location_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2626 (class 2606 OID 65198)
-- Dependencies: 1759 1759 2260
-- Name: loca_location_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2627 (class 2606 OID 65204)
-- Dependencies: 1771 1759 2292
-- Name: loca_location_orga_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_orga_fk FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2635 (class 2606 OID 65860)
-- Dependencies: 1783 1763 2337
-- Name: mole_abstco_categories_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT mole_abstco_categories_fk FOREIGN KEY (categoryid) REFERENCES ref_componentcategory(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2631 (class 2606 OID 64959)
-- Dependencies: 2135 1726 1762
-- Name: mole_abstco_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstco_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2632 (class 2606 OID 65770)
-- Dependencies: 1762 1791 2366
-- Name: mole_abstco_natuso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstco_natuso_fk FOREIGN KEY (naturalsourceid) REFERENCES ref_organism(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2638 (class 2606 OID 64969)
-- Dependencies: 2275 1764 1765
-- Name: mole_construct_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT mole_construct_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- TOC entry 2640 (class 2606 OID 64964)
-- Dependencies: 1765 1762 2269
-- Name: mole_molecule_abstco_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT mole_molecule_abstco_fk FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2782 (class 2606 OID 65218)
-- Dependencies: 1812 1813 2447
-- Name: mole_molecule_nuctargets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT mole_molecule_nuctargets_fk FOREIGN KEY (nuctargetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2644 (class 2606 OID 65785)
-- Dependencies: 1766 1818 2462
-- Name: mole_molecule_relareobel_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT mole_molecule_relareobel_fk FOREIGN KEY (relatedresearchobjectiveelementid) REFERENCES trag_researchobjectiveelement(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2646 (class 2606 OID 64954)
-- Dependencies: 1726 1767 2135
-- Name: mole_molefe_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2648 (class 2606 OID 65764)
-- Dependencies: 1767 1765 2275
-- Name: mole_molefe_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- TOC entry 2647 (class 2606 OID 65758)
-- Dependencies: 1767 1765 2275
-- Name: mole_molefe_refmo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_refmo_fk FOREIGN KEY (refmoleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- TOC entry 2650 (class 2606 OID 64974)
-- Dependencies: 1768 1765 2275
-- Name: mole_primer_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT mole_primer_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- TOC entry 2652 (class 2606 OID 64819)
-- Dependencies: 1726 1769 2135
-- Name: peop_group_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2653 (class 2606 OID 65424)
-- Dependencies: 1771 1769 2292
-- Name: peop_group_orga_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_orga_fk FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2656 (class 2606 OID 64829)
-- Dependencies: 1771 1726 2135
-- Name: peop_orga_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_orga_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2665 (class 2606 OID 65436)
-- Dependencies: 2286 1775 1769
-- Name: peop_persingr_group_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_group_fk FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2663 (class 2606 OID 64824)
-- Dependencies: 1726 1775 2135
-- Name: peop_persingr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2664 (class 2606 OID 65430)
-- Dependencies: 1773 1775 2297
-- Name: peop_persingr_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_person_fk FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2660 (class 2606 OID 65444)
-- Dependencies: 1773 1769 2286
-- Name: peop_person_currgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_currgr_fk FOREIGN KEY (currentgroupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2659 (class 2606 OID 64834)
-- Dependencies: 1773 1726 2135
-- Name: peop_person_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2669 (class 2606 OID 64949)
-- Dependencies: 1726 1777 2135
-- Name: prot_parade_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parade_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2670 (class 2606 OID 65750)
-- Dependencies: 1777 1778 2315
-- Name: prot_parade_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parade_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2674 (class 2606 OID 65718)
-- Dependencies: 1778 1786 2347
-- Name: prot_protocol_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2673 (class 2606 OID 64934)
-- Dependencies: 2135 1726 1778
-- Name: prot_protocol_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2679 (class 2606 OID 64944)
-- Dependencies: 2135 1780 1726
-- Name: prot_refinsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2681 (class 2606 OID 65744)
-- Dependencies: 1780 1778 2315
-- Name: prot_refinsa_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2680 (class 2606 OID 65738)
-- Dependencies: 1780 1794 2374
-- Name: prot_refinsa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2685 (class 2606 OID 64939)
-- Dependencies: 1781 1726 2135
-- Name: prot_refousa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2687 (class 2606 OID 65732)
-- Dependencies: 1781 1778 2315
-- Name: prot_refousa_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2686 (class 2606 OID 65726)
-- Dependencies: 1781 1794 2374
-- Name: prot_refousa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2689 (class 2606 OID 65009)
-- Dependencies: 1793 1782 2370
-- Name: ref_absthoty_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_absthoty_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2636 (class 2606 OID 65865)
-- Dependencies: 1763 2269 1762
-- Name: ref_compca_components_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT ref_compca_components_fk FOREIGN KEY (componentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2691 (class 2606 OID 65044)
-- Dependencies: 1793 1783 2370
-- Name: ref_compca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_compca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2693 (class 2606 OID 65024)
-- Dependencies: 1789 1784 2358
-- Name: ref_crysty_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT ref_crysty_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid) ON DELETE CASCADE;


--
-- TOC entry 2695 (class 2606 OID 64999)
-- Dependencies: 2370 1785 1793
-- Name: ref_dbname_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2697 (class 2606 OID 65004)
-- Dependencies: 1793 1786 2370
-- Name: ref_expety_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_expety_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2699 (class 2606 OID 65029)
-- Dependencies: 1793 1787 2370
-- Name: ref_hazaph_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazaph_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2598 (class 2606 OID 65879)
-- Dependencies: 2333 1782 1751
-- Name: ref_holdca_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT ref_holdca_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2597 (class 2606 OID 65874)
-- Dependencies: 1788 2355 1751
-- Name: ref_holdca_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT ref_holdca_otherrole_fk FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2701 (class 2606 OID 65049)
-- Dependencies: 1793 1788 2370
-- Name: ref_holdca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2703 (class 2606 OID 65019)
-- Dependencies: 1782 1789 2333
-- Name: ref_holdertype_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2704 (class 2606 OID 65834)
-- Dependencies: 1789 1807 2425
-- Name: ref_holdertype_defascpl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_defascpl_fk FOREIGN KEY (defaultscheduleplanid) REFERENCES sche_scheduleplan(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2802 (class 2606 OID 65069)
-- Dependencies: 1793 1820 2370
-- Name: ref_imagetype_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2623 (class 2606 OID 65849)
-- Dependencies: 1740 1758 2197
-- Name: ref_instty_inst_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT ref_instty_inst_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2706 (class 2606 OID 65034)
-- Dependencies: 1793 1790 2370
-- Name: ref_instty_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instty_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2708 (class 2606 OID 65074)
-- Dependencies: 1793 1791 2370
-- Name: ref_organism_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2710 (class 2606 OID 65014)
-- Dependencies: 1782 1792 2333
-- Name: ref_pintype_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT ref_pintype_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2712 (class 2606 OID 65059)
-- Dependencies: 1794 1793 2370
-- Name: ref_sampca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_sampca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2714 (class 2606 OID 65039)
-- Dependencies: 1793 1795 2370
-- Name: ref_targst_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targst_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2719 (class 2606 OID 65886)
-- Dependencies: 1786 1796 2347
-- Name: ref_workit_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2718 (class 2606 OID 65054)
-- Dependencies: 2370 1796 1793
-- Name: ref_workit_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2720 (class 2606 OID 65892)
-- Dependencies: 2378 1796 1795
-- Name: ref_workit_status_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_status_fk FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2726 (class 2606 OID 65323)
-- Dependencies: 1787 1799 2351
-- Name: sam_abstsa_hazaph_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa_hazaph_fk FOREIGN KEY (hazardphraseid) REFERENCES ref_hazardphrase(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2722 (class 2606 OID 64774)
-- Dependencies: 1798 1726 2135
-- Name: sam_abstsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2725 (class 2606 OID 65318)
-- Dependencies: 2388 1799 1798
-- Name: sam_abstsa_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa_otherrole_fk FOREIGN KEY (otherrole) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2740 (class 2606 OID 65330)
-- Dependencies: 1798 1803 2388
-- Name: sam_abstsa_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_abstsa_otherrole_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2741 (class 2606 OID 65335)
-- Dependencies: 2374 1803 1794
-- Name: sam_abstsa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_abstsa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE CASCADE;


--
-- TOC entry 2728 (class 2606 OID 64784)
-- Dependencies: 2407 1804 1800
-- Name: sam_cryssa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT sam_cryssa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE CASCADE;


--
-- TOC entry 2730 (class 2606 OID 64789)
-- Dependencies: 1801 2388 1798
-- Name: sam_refsample_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT sam_refsample_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2734 (class 2606 OID 64794)
-- Dependencies: 2135 1726 1802
-- Name: sam_refsaso_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2736 (class 2606 OID 65366)
-- Dependencies: 1755 1802 2242
-- Name: sam_refsaso_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE SET NULL;


--
-- TOC entry 2737 (class 2606 OID 65372)
-- Dependencies: 1802 2394 1801
-- Name: sam_refsaso_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2735 (class 2606 OID 65360)
-- Dependencies: 1771 1802 2292
-- Name: sam_refsaso_supplier_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_supplier_fk FOREIGN KEY (supplierid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2752 (class 2606 OID 65298)
-- Dependencies: 1798 1805 2388
-- Name: sam_sampco_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2754 (class 2606 OID 65310)
-- Dependencies: 1805 1805 2414
-- Name: sam_sampco_container_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_container_fk FOREIGN KEY (containerid) REFERENCES sam_samplecomponent(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2750 (class 2606 OID 64769)
-- Dependencies: 1805 2135 1726
-- Name: sam_sampco_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2753 (class 2606 OID 65304)
-- Dependencies: 1762 1805 2269
-- Name: sam_sampco_refco_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_refco_fk FOREIGN KEY (refcomponentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2751 (class 2606 OID 65292)
-- Dependencies: 1818 1805 2462
-- Name: sam_sampco_reseobel_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_reseobel_fk FOREIGN KEY (researchobjectivelementid) REFERENCES trag_researchobjectiveelement(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2743 (class 2606 OID 64779)
-- Dependencies: 2388 1804 1798
-- Name: sam_sample_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2744 (class 2606 OID 65342)
-- Dependencies: 1773 1804 2297
-- Name: sam_sample_assignto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_assignto_fk FOREIGN KEY (assigntoid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2745 (class 2606 OID 65348)
-- Dependencies: 1752 1804 2233
-- Name: sam_sample_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE SET NULL;


--
-- TOC entry 2746 (class 2606 OID 65354)
-- Dependencies: 1801 1804 2394
-- Name: sam_sample_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- TOC entry 2761 (class 2606 OID 64804)
-- Dependencies: 1726 1807 2135
-- Name: sche_schepl_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_schepl_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2763 (class 2606 OID 64799)
-- Dependencies: 2135 1726 1808
-- Name: sche_scheplof_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheplof_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2764 (class 2606 OID 65380)
-- Dependencies: 1808 1807 2425
-- Name: sche_scheplof_schepl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheplof_schepl_fk FOREIGN KEY (scheduleplanid) REFERENCES sche_scheduleplan(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2757 (class 2606 OID 65390)
-- Dependencies: 1752 1806 2233
-- Name: sche_scheta_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- TOC entry 2759 (class 2606 OID 65402)
-- Dependencies: 1740 1806 2197
-- Name: sche_scheta_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2756 (class 2606 OID 64809)
-- Dependencies: 1726 2135 1806
-- Name: sche_scheta_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2758 (class 2606 OID 65396)
-- Dependencies: 1808 1806 2429
-- Name: sche_scheta_scheplof_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_scheplof_fk FOREIGN KEY (scheduleplanoffsetid) REFERENCES sche_scheduleplanoffset(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2800 (class 2606 OID 64709)
-- Dependencies: 1819 1726 2135
-- Name: targ_alias_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2801 (class 2606 OID 64714)
-- Dependencies: 1812 1819 2447
-- Name: targ_alias_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2768 (class 2606 OID 65254)
-- Dependencies: 1737 1809 2183
-- Name: targ_milestone_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2766 (class 2606 OID 64754)
-- Dependencies: 2135 1726 1809
-- Name: targ_milestone_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2767 (class 2606 OID 65248)
-- Dependencies: 1809 1795 2378
-- Name: targ_milestone_status_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_status_fk FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2769 (class 2606 OID 65260)
-- Dependencies: 1812 1809 2447
-- Name: targ_milestone_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2771 (class 2606 OID 64764)
-- Dependencies: 2135 1726 1810
-- Name: targ_project_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2772 (class 2606 OID 65286)
-- Dependencies: 1810 1810 2437
-- Name: targ_project_project_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_project_fk FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2786 (class 2606 OID 65230)
-- Dependencies: 1812 1814 2447
-- Name: targ_project_targets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_project_targets_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2774 (class 2606 OID 64899)
-- Dependencies: 1726 2135 1811
-- Name: targ_reseob_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_reseob_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2775 (class 2606 OID 65642)
-- Dependencies: 2297 1811 1773
-- Name: targ_reseob_owner_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_reseob_owner_fk FOREIGN KEY (ownerid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2777 (class 2606 OID 64749)
-- Dependencies: 1812 1726 2135
-- Name: targ_target_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2783 (class 2606 OID 65223)
-- Dependencies: 1765 1813 2275
-- Name: targ_target_nuclac_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT targ_target_nuclac_fk FOREIGN KEY (nucleicacidid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- TOC entry 2787 (class 2606 OID 65235)
-- Dependencies: 1814 2437 1810
-- Name: targ_target_projects_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_target_projects_fk FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2779 (class 2606 OID 65240)
-- Dependencies: 2275 1765 1812
-- Name: targ_target_protein_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_protein_fk FOREIGN KEY (proteinid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- TOC entry 2778 (class 2606 OID 65210)
-- Dependencies: 1791 1812 2366
-- Name: targ_target_species_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_species_fk FOREIGN KEY (speciesid) REFERENCES ref_organism(publicentryid) ON DELETE SET NULL;


--
-- TOC entry 2793 (class 2606 OID 65274)
-- Dependencies: 1815 1816 2457
-- Name: targ_target_targgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_target_targgr_fk FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2789 (class 2606 OID 64759)
-- Dependencies: 1815 2135 1726
-- Name: targ_targgr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targgr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2794 (class 2606 OID 65279)
-- Dependencies: 1812 1816 2447
-- Name: targ_targgr_targets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_targgr_targets_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2790 (class 2606 OID 65266)
-- Dependencies: 1815 1815 2457
-- Name: targ_targgr_targgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targgr_targgr_fk FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2796 (class 2606 OID 64894)
-- Dependencies: 1726 1818 2135
-- Name: trag_reseobel_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- TOC entry 2799 (class 2606 OID 65636)
-- Dependencies: 1818 1765 2275
-- Name: trag_reseobel_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- TOC entry 2797 (class 2606 OID 65624)
-- Dependencies: 2442 1818 1811
-- Name: trag_reseobel_reseob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_reseob_fk FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid) ON DELETE CASCADE;


--
-- TOC entry 2798 (class 2606 OID 65630)
-- Dependencies: 2447 1812 1818
-- Name: trag_reseobel_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE SET NULL;


--
-- TOC entry 2643 (class 2606 OID 65780)
-- Dependencies: 1766 1765 2275
-- Name: trag_reseobel_triamo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT trag_reseobel_triamo_fk FOREIGN KEY (trialmoleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- TOC entry 2913 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- TOC entry 2914 (class 0 OID 0)
-- Dependencies: 1711
-- Name: acco_permission; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_permission FROM PUBLIC;
REVOKE ALL ON TABLE acco_permission FROM postgres;
GRANT ALL ON TABLE acco_permission TO postgres;
GRANT ALL ON TABLE acco_permission TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE acco_permission TO pimsupdate;
GRANT SELECT ON TABLE acco_permission TO pimsview;


--
-- TOC entry 2915 (class 0 OID 0)
-- Dependencies: 1712
-- Name: acco_user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_user FROM PUBLIC;
REVOKE ALL ON TABLE acco_user FROM postgres;
GRANT ALL ON TABLE acco_user TO postgres;
GRANT ALL ON TABLE acco_user TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE acco_user TO pimsupdate;
GRANT SELECT ON TABLE acco_user TO pimsview;


--
-- TOC entry 2916 (class 0 OID 0)
-- Dependencies: 1713
-- Name: acco_usergroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup FROM postgres;
GRANT ALL ON TABLE acco_usergroup TO postgres;
GRANT ALL ON TABLE acco_usergroup TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE acco_usergroup TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup TO pimsview;


--
-- TOC entry 2917 (class 0 OID 0)
-- Dependencies: 1714
-- Name: acco_usergroup2leaders; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup2leaders FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup2leaders FROM postgres;
GRANT ALL ON TABLE acco_usergroup2leaders TO postgres;
GRANT ALL ON TABLE acco_usergroup2leaders TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE acco_usergroup2leaders TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup2leaders TO pimsview;


--
-- TOC entry 2918 (class 0 OID 0)
-- Dependencies: 1715
-- Name: acco_usergroup2members; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup2members FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup2members FROM postgres;
GRANT ALL ON TABLE acco_usergroup2members TO postgres;
GRANT ALL ON TABLE acco_usergroup2members TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE acco_usergroup2members TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup2members TO pimsview;


--
-- TOC entry 2919 (class 0 OID 0)
-- Dependencies: 1716
-- Name: core_accessobject; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_accessobject FROM PUBLIC;
REVOKE ALL ON TABLE core_accessobject FROM postgres;
GRANT ALL ON TABLE core_accessobject TO postgres;
GRANT ALL ON TABLE core_accessobject TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_accessobject TO pimsupdate;
GRANT SELECT ON TABLE core_accessobject TO pimsview;


--
-- TOC entry 2920 (class 0 OID 0)
-- Dependencies: 1717
-- Name: core_annotation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_annotation FROM PUBLIC;
REVOKE ALL ON TABLE core_annotation FROM postgres;
GRANT ALL ON TABLE core_annotation TO postgres;
GRANT ALL ON TABLE core_annotation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_annotation TO pimsupdate;
GRANT SELECT ON TABLE core_annotation TO pimsview;


--
-- TOC entry 2921 (class 0 OID 0)
-- Dependencies: 1718
-- Name: core_applicationdata; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_applicationdata FROM PUBLIC;
REVOKE ALL ON TABLE core_applicationdata FROM postgres;
GRANT ALL ON TABLE core_applicationdata TO postgres;
GRANT ALL ON TABLE core_applicationdata TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_applicationdata TO pimsupdate;
GRANT SELECT ON TABLE core_applicationdata TO pimsview;


--
-- TOC entry 2922 (class 0 OID 0)
-- Dependencies: 1719
-- Name: core_attachment; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_attachment FROM PUBLIC;
REVOKE ALL ON TABLE core_attachment FROM postgres;
GRANT ALL ON TABLE core_attachment TO postgres;
GRANT ALL ON TABLE core_attachment TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_attachment TO pimsupdate;
GRANT SELECT ON TABLE core_attachment TO pimsview;


--
-- TOC entry 2923 (class 0 OID 0)
-- Dependencies: 1721
-- Name: core_bookcitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_bookcitation FROM PUBLIC;
REVOKE ALL ON TABLE core_bookcitation FROM postgres;
GRANT ALL ON TABLE core_bookcitation TO postgres;
GRANT ALL ON TABLE core_bookcitation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_bookcitation TO pimsupdate;
GRANT SELECT ON TABLE core_bookcitation TO pimsview;


--
-- TOC entry 2924 (class 0 OID 0)
-- Dependencies: 1722
-- Name: core_citation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_citation FROM PUBLIC;
REVOKE ALL ON TABLE core_citation FROM postgres;
GRANT ALL ON TABLE core_citation TO postgres;
GRANT ALL ON TABLE core_citation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_citation TO pimsupdate;
GRANT SELECT ON TABLE core_citation TO pimsview;


--
-- TOC entry 2925 (class 0 OID 0)
-- Dependencies: 1723
-- Name: core_conferencecitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_conferencecitation FROM PUBLIC;
REVOKE ALL ON TABLE core_conferencecitation FROM postgres;
GRANT ALL ON TABLE core_conferencecitation TO postgres;
GRANT ALL ON TABLE core_conferencecitation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_conferencecitation TO pimsupdate;
GRANT SELECT ON TABLE core_conferencecitation TO pimsview;


--
-- TOC entry 2926 (class 0 OID 0)
-- Dependencies: 1724
-- Name: core_externaldblink; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_externaldblink FROM PUBLIC;
REVOKE ALL ON TABLE core_externaldblink FROM postgres;
GRANT ALL ON TABLE core_externaldblink TO postgres;
GRANT ALL ON TABLE core_externaldblink TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_externaldblink TO pimsupdate;
GRANT SELECT ON TABLE core_externaldblink TO pimsview;


--
-- TOC entry 2927 (class 0 OID 0)
-- Dependencies: 1725
-- Name: core_journalcitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_journalcitation FROM PUBLIC;
REVOKE ALL ON TABLE core_journalcitation FROM postgres;
GRANT ALL ON TABLE core_journalcitation TO postgres;
GRANT ALL ON TABLE core_journalcitation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_journalcitation TO pimsupdate;
GRANT SELECT ON TABLE core_journalcitation TO pimsview;


--
-- TOC entry 2928 (class 0 OID 0)
-- Dependencies: 1726
-- Name: core_labbookentry; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_labbookentry FROM PUBLIC;
REVOKE ALL ON TABLE core_labbookentry FROM postgres;
GRANT ALL ON TABLE core_labbookentry TO postgres;
GRANT ALL ON TABLE core_labbookentry TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_labbookentry TO pimsupdate;
GRANT SELECT ON TABLE core_labbookentry TO pimsview;


--
-- TOC entry 2929 (class 0 OID 0)
-- Dependencies: 1727
-- Name: core_note; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_note FROM PUBLIC;
REVOKE ALL ON TABLE core_note FROM postgres;
GRANT ALL ON TABLE core_note TO postgres;
GRANT ALL ON TABLE core_note TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_note TO pimsupdate;
GRANT SELECT ON TABLE core_note TO pimsview;


--
-- TOC entry 2930 (class 0 OID 0)
-- Dependencies: 1728
-- Name: core_note2relatedentrys; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_note2relatedentrys FROM PUBLIC;
REVOKE ALL ON TABLE core_note2relatedentrys FROM postgres;
GRANT ALL ON TABLE core_note2relatedentrys TO postgres;
GRANT ALL ON TABLE core_note2relatedentrys TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_note2relatedentrys TO pimsupdate;
GRANT SELECT ON TABLE core_note2relatedentrys TO pimsview;


--
-- TOC entry 2931 (class 0 OID 0)
-- Dependencies: 1720
-- Name: core_systemclass; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_systemclass FROM PUBLIC;
REVOKE ALL ON TABLE core_systemclass FROM postgres;
GRANT ALL ON TABLE core_systemclass TO postgres;
GRANT ALL ON TABLE core_systemclass TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_systemclass TO pimsupdate;
GRANT SELECT ON TABLE core_systemclass TO pimsview;


--
-- TOC entry 2932 (class 0 OID 0)
-- Dependencies: 1729
-- Name: core_thesiscitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_thesiscitation FROM PUBLIC;
REVOKE ALL ON TABLE core_thesiscitation FROM postgres;
GRANT ALL ON TABLE core_thesiscitation TO postgres;
GRANT ALL ON TABLE core_thesiscitation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE core_thesiscitation TO pimsupdate;
GRANT SELECT ON TABLE core_thesiscitation TO pimsview;


--
-- TOC entry 2933 (class 0 OID 0)
-- Dependencies: 1730
-- Name: cryz_cypade_possva; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_cypade_possva FROM PUBLIC;
REVOKE ALL ON TABLE cryz_cypade_possva FROM postgres;
GRANT ALL ON TABLE cryz_cypade_possva TO postgres;
GRANT ALL ON TABLE cryz_cypade_possva TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_cypade_possva TO pimsupdate;
GRANT SELECT ON TABLE cryz_cypade_possva TO pimsview;


--
-- TOC entry 2934 (class 0 OID 0)
-- Dependencies: 1731
-- Name: cryz_dropannotation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_dropannotation FROM PUBLIC;
REVOKE ALL ON TABLE cryz_dropannotation FROM postgres;
GRANT ALL ON TABLE cryz_dropannotation TO postgres;
GRANT ALL ON TABLE cryz_dropannotation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_dropannotation TO pimsupdate;
GRANT SELECT ON TABLE cryz_dropannotation TO pimsview;


--
-- TOC entry 2935 (class 0 OID 0)
-- Dependencies: 1732
-- Name: cryz_image; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_image FROM PUBLIC;
REVOKE ALL ON TABLE cryz_image FROM postgres;
GRANT ALL ON TABLE cryz_image TO postgres;
GRANT ALL ON TABLE cryz_image TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_image TO pimsupdate;
GRANT SELECT ON TABLE cryz_image TO pimsview;


--
-- TOC entry 2936 (class 0 OID 0)
-- Dependencies: 1733
-- Name: cryz_parameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_parameter FROM PUBLIC;
REVOKE ALL ON TABLE cryz_parameter FROM postgres;
GRANT ALL ON TABLE cryz_parameter TO postgres;
GRANT ALL ON TABLE cryz_parameter TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_parameter TO pimsupdate;
GRANT SELECT ON TABLE cryz_parameter TO pimsview;


--
-- TOC entry 2937 (class 0 OID 0)
-- Dependencies: 1734
-- Name: cryz_parameterdefinition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_parameterdefinition FROM PUBLIC;
REVOKE ALL ON TABLE cryz_parameterdefinition FROM postgres;
GRANT ALL ON TABLE cryz_parameterdefinition TO postgres;
GRANT ALL ON TABLE cryz_parameterdefinition TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_parameterdefinition TO pimsupdate;
GRANT SELECT ON TABLE cryz_parameterdefinition TO pimsview;


--
-- TOC entry 2938 (class 0 OID 0)
-- Dependencies: 1735
-- Name: cryz_score; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_score FROM PUBLIC;
REVOKE ALL ON TABLE cryz_score FROM postgres;
GRANT ALL ON TABLE cryz_score TO postgres;
GRANT ALL ON TABLE cryz_score TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_score TO pimsupdate;
GRANT SELECT ON TABLE cryz_score TO pimsview;


--
-- TOC entry 2939 (class 0 OID 0)
-- Dependencies: 1736
-- Name: cryz_scoringscheme; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_scoringscheme FROM PUBLIC;
REVOKE ALL ON TABLE cryz_scoringscheme FROM postgres;
GRANT ALL ON TABLE cryz_scoringscheme TO postgres;
GRANT ALL ON TABLE cryz_scoringscheme TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE cryz_scoringscheme TO pimsupdate;
GRANT SELECT ON TABLE cryz_scoringscheme TO pimsview;


--
-- TOC entry 2940 (class 0 OID 0)
-- Dependencies: 1737
-- Name: expe_experiment; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_experiment FROM PUBLIC;
REVOKE ALL ON TABLE expe_experiment FROM postgres;
GRANT ALL ON TABLE expe_experiment TO postgres;
GRANT ALL ON TABLE expe_experiment TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_experiment TO pimsupdate;
GRANT SELECT ON TABLE expe_experiment TO pimsview;


--
-- TOC entry 2941 (class 0 OID 0)
-- Dependencies: 1738
-- Name: expe_experimentgroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_experimentgroup FROM PUBLIC;
REVOKE ALL ON TABLE expe_experimentgroup FROM postgres;
GRANT ALL ON TABLE expe_experimentgroup TO postgres;
GRANT ALL ON TABLE expe_experimentgroup TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_experimentgroup TO pimsupdate;
GRANT SELECT ON TABLE expe_experimentgroup TO pimsview;


--
-- TOC entry 2942 (class 0 OID 0)
-- Dependencies: 1739
-- Name: expe_inputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_inputsample FROM PUBLIC;
REVOKE ALL ON TABLE expe_inputsample FROM postgres;
GRANT ALL ON TABLE expe_inputsample TO postgres;
GRANT ALL ON TABLE expe_inputsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_inputsample TO pimsupdate;
GRANT SELECT ON TABLE expe_inputsample TO pimsview;


--
-- TOC entry 2943 (class 0 OID 0)
-- Dependencies: 1740
-- Name: expe_instrument; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_instrument FROM PUBLIC;
REVOKE ALL ON TABLE expe_instrument FROM postgres;
GRANT ALL ON TABLE expe_instrument TO postgres;
GRANT ALL ON TABLE expe_instrument TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_instrument TO pimsupdate;
GRANT SELECT ON TABLE expe_instrument TO pimsview;


--
-- TOC entry 2944 (class 0 OID 0)
-- Dependencies: 1741
-- Name: expe_method; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_method FROM PUBLIC;
REVOKE ALL ON TABLE expe_method FROM postgres;
GRANT ALL ON TABLE expe_method TO postgres;
GRANT ALL ON TABLE expe_method TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_method TO pimsupdate;
GRANT SELECT ON TABLE expe_method TO pimsview;


--
-- TOC entry 2945 (class 0 OID 0)
-- Dependencies: 1742
-- Name: expe_methodparameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_methodparameter FROM PUBLIC;
REVOKE ALL ON TABLE expe_methodparameter FROM postgres;
GRANT ALL ON TABLE expe_methodparameter TO postgres;
GRANT ALL ON TABLE expe_methodparameter TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_methodparameter TO pimsupdate;
GRANT SELECT ON TABLE expe_methodparameter TO pimsview;


--
-- TOC entry 2946 (class 0 OID 0)
-- Dependencies: 1743
-- Name: expe_outputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_outputsample FROM PUBLIC;
REVOKE ALL ON TABLE expe_outputsample FROM postgres;
GRANT ALL ON TABLE expe_outputsample TO postgres;
GRANT ALL ON TABLE expe_outputsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_outputsample TO pimsupdate;
GRANT SELECT ON TABLE expe_outputsample TO pimsview;


--
-- TOC entry 2947 (class 0 OID 0)
-- Dependencies: 1744
-- Name: expe_parameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_parameter FROM PUBLIC;
REVOKE ALL ON TABLE expe_parameter FROM postgres;
GRANT ALL ON TABLE expe_parameter TO postgres;
GRANT ALL ON TABLE expe_parameter TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_parameter TO pimsupdate;
GRANT SELECT ON TABLE expe_parameter TO pimsview;


--
-- TOC entry 2948 (class 0 OID 0)
-- Dependencies: 1745
-- Name: expe_software; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_software FROM PUBLIC;
REVOKE ALL ON TABLE expe_software FROM postgres;
GRANT ALL ON TABLE expe_software TO postgres;
GRANT ALL ON TABLE expe_software TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_software TO pimsupdate;
GRANT SELECT ON TABLE expe_software TO pimsview;


--
-- TOC entry 2949 (class 0 OID 0)
-- Dependencies: 1746
-- Name: expe_software_tasks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_software_tasks FROM PUBLIC;
REVOKE ALL ON TABLE expe_software_tasks FROM postgres;
GRANT ALL ON TABLE expe_software_tasks TO postgres;
GRANT ALL ON TABLE expe_software_tasks TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE expe_software_tasks TO pimsupdate;
GRANT SELECT ON TABLE expe_software_tasks TO pimsview;


--
-- TOC entry 2950 (class 0 OID 0)
-- Dependencies: 1749
-- Name: hold_abstractholder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_abstractholder FROM PUBLIC;
REVOKE ALL ON TABLE hold_abstractholder FROM postgres;
GRANT ALL ON TABLE hold_abstractholder TO postgres;
GRANT ALL ON TABLE hold_abstractholder TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_abstractholder TO pimsupdate;
GRANT SELECT ON TABLE hold_abstractholder TO pimsview;


--
-- TOC entry 2951 (class 0 OID 0)
-- Dependencies: 1750
-- Name: hold_holdca2abstholders; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdca2abstholders FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdca2abstholders FROM postgres;
GRANT ALL ON TABLE hold_holdca2abstholders TO postgres;
GRANT ALL ON TABLE hold_holdca2abstholders TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_holdca2abstholders TO pimsupdate;
GRANT SELECT ON TABLE hold_holdca2abstholders TO pimsview;


--
-- TOC entry 2952 (class 0 OID 0)
-- Dependencies: 1751
-- Name: hold_holdca2absthoty; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdca2absthoty FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdca2absthoty FROM postgres;
GRANT ALL ON TABLE hold_holdca2absthoty TO postgres;
GRANT ALL ON TABLE hold_holdca2absthoty TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_holdca2absthoty TO pimsupdate;
GRANT SELECT ON TABLE hold_holdca2absthoty TO pimsview;


--
-- TOC entry 2953 (class 0 OID 0)
-- Dependencies: 1752
-- Name: hold_holder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holder FROM PUBLIC;
REVOKE ALL ON TABLE hold_holder FROM postgres;
GRANT ALL ON TABLE hold_holder TO postgres;
GRANT ALL ON TABLE hold_holder TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_holder TO pimsupdate;
GRANT SELECT ON TABLE hold_holder TO pimsview;


--
-- TOC entry 2954 (class 0 OID 0)
-- Dependencies: 1753
-- Name: hold_holderlocation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holderlocation FROM PUBLIC;
REVOKE ALL ON TABLE hold_holderlocation FROM postgres;
GRANT ALL ON TABLE hold_holderlocation TO postgres;
GRANT ALL ON TABLE hold_holderlocation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_holderlocation TO pimsupdate;
GRANT SELECT ON TABLE hold_holderlocation TO pimsview;


--
-- TOC entry 2955 (class 0 OID 0)
-- Dependencies: 1754
-- Name: hold_holdertypeposition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdertypeposition FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdertypeposition FROM postgres;
GRANT ALL ON TABLE hold_holdertypeposition TO postgres;
GRANT ALL ON TABLE hold_holdertypeposition TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_holdertypeposition TO pimsupdate;
GRANT SELECT ON TABLE hold_holdertypeposition TO pimsview;


--
-- TOC entry 2956 (class 0 OID 0)
-- Dependencies: 1755
-- Name: hold_refholder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refholder FROM PUBLIC;
REVOKE ALL ON TABLE hold_refholder FROM postgres;
GRANT ALL ON TABLE hold_refholder TO postgres;
GRANT ALL ON TABLE hold_refholder TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_refholder TO pimsupdate;
GRANT SELECT ON TABLE hold_refholder TO pimsview;


--
-- TOC entry 2957 (class 0 OID 0)
-- Dependencies: 1756
-- Name: hold_refholderoffset; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refholderoffset FROM PUBLIC;
REVOKE ALL ON TABLE hold_refholderoffset FROM postgres;
GRANT ALL ON TABLE hold_refholderoffset TO postgres;
GRANT ALL ON TABLE hold_refholderoffset TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_refholderoffset TO pimsupdate;
GRANT SELECT ON TABLE hold_refholderoffset TO pimsview;


--
-- TOC entry 2958 (class 0 OID 0)
-- Dependencies: 1757
-- Name: hold_refsampleposition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refsampleposition FROM PUBLIC;
REVOKE ALL ON TABLE hold_refsampleposition FROM postgres;
GRANT ALL ON TABLE hold_refsampleposition TO postgres;
GRANT ALL ON TABLE hold_refsampleposition TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE hold_refsampleposition TO pimsupdate;
GRANT SELECT ON TABLE hold_refsampleposition TO pimsview;


--
-- TOC entry 2959 (class 0 OID 0)
-- Dependencies: 1758
-- Name: inst_instty2inst; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE inst_instty2inst FROM PUBLIC;
REVOKE ALL ON TABLE inst_instty2inst FROM postgres;
GRANT ALL ON TABLE inst_instty2inst TO postgres;
GRANT ALL ON TABLE inst_instty2inst TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE inst_instty2inst TO pimsupdate;
GRANT SELECT ON TABLE inst_instty2inst TO pimsview;


--
-- TOC entry 2960 (class 0 OID 0)
-- Dependencies: 1759
-- Name: loca_location; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE loca_location FROM PUBLIC;
REVOKE ALL ON TABLE loca_location FROM postgres;
GRANT ALL ON TABLE loca_location TO postgres;
GRANT ALL ON TABLE loca_location TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE loca_location TO pimsupdate;
GRANT SELECT ON TABLE loca_location TO pimsview;


--
-- TOC entry 2961 (class 0 OID 0)
-- Dependencies: 1760
-- Name: mole_abstco_keywords; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstco_keywords FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstco_keywords FROM postgres;
GRANT ALL ON TABLE mole_abstco_keywords TO postgres;
GRANT ALL ON TABLE mole_abstco_keywords TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_abstco_keywords TO pimsupdate;
GRANT SELECT ON TABLE mole_abstco_keywords TO pimsview;


--
-- TOC entry 2962 (class 0 OID 0)
-- Dependencies: 1761
-- Name: mole_abstco_synonyms; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstco_synonyms FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstco_synonyms FROM postgres;
GRANT ALL ON TABLE mole_abstco_synonyms TO postgres;
GRANT ALL ON TABLE mole_abstco_synonyms TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_abstco_synonyms TO pimsupdate;
GRANT SELECT ON TABLE mole_abstco_synonyms TO pimsview;


--
-- TOC entry 2963 (class 0 OID 0)
-- Dependencies: 1762
-- Name: mole_abstractcomponent; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstractcomponent FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstractcomponent FROM postgres;
GRANT ALL ON TABLE mole_abstractcomponent TO postgres;
GRANT ALL ON TABLE mole_abstractcomponent TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_abstractcomponent TO pimsupdate;
GRANT SELECT ON TABLE mole_abstractcomponent TO pimsview;


--
-- TOC entry 2964 (class 0 OID 0)
-- Dependencies: 1763
-- Name: mole_compca2components; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_compca2components FROM PUBLIC;
REVOKE ALL ON TABLE mole_compca2components FROM postgres;
GRANT ALL ON TABLE mole_compca2components TO postgres;
GRANT ALL ON TABLE mole_compca2components TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_compca2components TO pimsupdate;
GRANT SELECT ON TABLE mole_compca2components TO pimsview;


--
-- TOC entry 2965 (class 0 OID 0)
-- Dependencies: 1764
-- Name: mole_construct; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_construct FROM PUBLIC;
REVOKE ALL ON TABLE mole_construct FROM postgres;
GRANT ALL ON TABLE mole_construct TO postgres;
GRANT ALL ON TABLE mole_construct TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_construct TO pimsupdate;
GRANT SELECT ON TABLE mole_construct TO pimsview;


--
-- TOC entry 2966 (class 0 OID 0)
-- Dependencies: 1765
-- Name: mole_molecule; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_molecule FROM PUBLIC;
REVOKE ALL ON TABLE mole_molecule FROM postgres;
GRANT ALL ON TABLE mole_molecule TO postgres;
GRANT ALL ON TABLE mole_molecule TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_molecule TO pimsupdate;
GRANT SELECT ON TABLE mole_molecule TO pimsview;


--
-- TOC entry 2967 (class 0 OID 0)
-- Dependencies: 1766
-- Name: mole_molecule2relareobel; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_molecule2relareobel FROM PUBLIC;
REVOKE ALL ON TABLE mole_molecule2relareobel FROM postgres;
GRANT ALL ON TABLE mole_molecule2relareobel TO postgres;
GRANT ALL ON TABLE mole_molecule2relareobel TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_molecule2relareobel TO pimsupdate;
GRANT SELECT ON TABLE mole_molecule2relareobel TO pimsview;


--
-- TOC entry 2968 (class 0 OID 0)
-- Dependencies: 1767
-- Name: mole_moleculefeature; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_moleculefeature FROM PUBLIC;
REVOKE ALL ON TABLE mole_moleculefeature FROM postgres;
GRANT ALL ON TABLE mole_moleculefeature TO postgres;
GRANT ALL ON TABLE mole_moleculefeature TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_moleculefeature TO pimsupdate;
GRANT SELECT ON TABLE mole_moleculefeature TO pimsview;


--
-- TOC entry 2969 (class 0 OID 0)
-- Dependencies: 1768
-- Name: mole_primer; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_primer FROM PUBLIC;
REVOKE ALL ON TABLE mole_primer FROM postgres;
GRANT ALL ON TABLE mole_primer TO postgres;
GRANT ALL ON TABLE mole_primer TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE mole_primer TO pimsupdate;
GRANT SELECT ON TABLE mole_primer TO pimsview;


--
-- TOC entry 2970 (class 0 OID 0)
-- Dependencies: 1769
-- Name: peop_group; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_group FROM PUBLIC;
REVOKE ALL ON TABLE peop_group FROM postgres;
GRANT ALL ON TABLE peop_group TO postgres;
GRANT ALL ON TABLE peop_group TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_group TO pimsupdate;
GRANT SELECT ON TABLE peop_group TO pimsview;


--
-- TOC entry 2971 (class 0 OID 0)
-- Dependencies: 1770
-- Name: peop_orga_addresses; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_orga_addresses FROM PUBLIC;
REVOKE ALL ON TABLE peop_orga_addresses FROM postgres;
GRANT ALL ON TABLE peop_orga_addresses TO postgres;
GRANT ALL ON TABLE peop_orga_addresses TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_orga_addresses TO pimsupdate;
GRANT SELECT ON TABLE peop_orga_addresses TO pimsview;


--
-- TOC entry 2972 (class 0 OID 0)
-- Dependencies: 1771
-- Name: peop_organisation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_organisation FROM PUBLIC;
REVOKE ALL ON TABLE peop_organisation FROM postgres;
GRANT ALL ON TABLE peop_organisation TO postgres;
GRANT ALL ON TABLE peop_organisation TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_organisation TO pimsupdate;
GRANT SELECT ON TABLE peop_organisation TO pimsview;


--
-- TOC entry 2973 (class 0 OID 0)
-- Dependencies: 1772
-- Name: peop_persingr_phonnu; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_persingr_phonnu FROM PUBLIC;
REVOKE ALL ON TABLE peop_persingr_phonnu FROM postgres;
GRANT ALL ON TABLE peop_persingr_phonnu TO postgres;
GRANT ALL ON TABLE peop_persingr_phonnu TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_persingr_phonnu TO pimsupdate;
GRANT SELECT ON TABLE peop_persingr_phonnu TO pimsview;


--
-- TOC entry 2974 (class 0 OID 0)
-- Dependencies: 1773
-- Name: peop_person; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_person FROM PUBLIC;
REVOKE ALL ON TABLE peop_person FROM postgres;
GRANT ALL ON TABLE peop_person TO postgres;
GRANT ALL ON TABLE peop_person TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_person TO pimsupdate;
GRANT SELECT ON TABLE peop_person TO pimsview;


--
-- TOC entry 2975 (class 0 OID 0)
-- Dependencies: 1774
-- Name: peop_person_middin; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_person_middin FROM PUBLIC;
REVOKE ALL ON TABLE peop_person_middin FROM postgres;
GRANT ALL ON TABLE peop_person_middin TO postgres;
GRANT ALL ON TABLE peop_person_middin TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_person_middin TO pimsupdate;
GRANT SELECT ON TABLE peop_person_middin TO pimsview;


--
-- TOC entry 2976 (class 0 OID 0)
-- Dependencies: 1775
-- Name: peop_personingroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_personingroup FROM PUBLIC;
REVOKE ALL ON TABLE peop_personingroup FROM postgres;
GRANT ALL ON TABLE peop_personingroup TO postgres;
GRANT ALL ON TABLE peop_personingroup TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE peop_personingroup TO pimsupdate;
GRANT SELECT ON TABLE peop_personingroup TO pimsview;


--
-- TOC entry 2977 (class 0 OID 0)
-- Dependencies: 1776
-- Name: prot_parade_possva; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_parade_possva FROM PUBLIC;
REVOKE ALL ON TABLE prot_parade_possva FROM postgres;
GRANT ALL ON TABLE prot_parade_possva TO postgres;
GRANT ALL ON TABLE prot_parade_possva TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_parade_possva TO pimsupdate;
GRANT SELECT ON TABLE prot_parade_possva TO pimsview;


--
-- TOC entry 2978 (class 0 OID 0)
-- Dependencies: 1777
-- Name: prot_parameterdefinition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_parameterdefinition FROM PUBLIC;
REVOKE ALL ON TABLE prot_parameterdefinition FROM postgres;
GRANT ALL ON TABLE prot_parameterdefinition TO postgres;
GRANT ALL ON TABLE prot_parameterdefinition TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_parameterdefinition TO pimsupdate;
GRANT SELECT ON TABLE prot_parameterdefinition TO pimsview;


--
-- TOC entry 2979 (class 0 OID 0)
-- Dependencies: 1778
-- Name: prot_protocol; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_protocol FROM PUBLIC;
REVOKE ALL ON TABLE prot_protocol FROM postgres;
GRANT ALL ON TABLE prot_protocol TO postgres;
GRANT ALL ON TABLE prot_protocol TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_protocol TO pimsupdate;
GRANT SELECT ON TABLE prot_protocol TO pimsview;


--
-- TOC entry 2980 (class 0 OID 0)
-- Dependencies: 1779
-- Name: prot_protocol_remarks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_protocol_remarks FROM PUBLIC;
REVOKE ALL ON TABLE prot_protocol_remarks FROM postgres;
GRANT ALL ON TABLE prot_protocol_remarks TO postgres;
GRANT ALL ON TABLE prot_protocol_remarks TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_protocol_remarks TO pimsupdate;
GRANT SELECT ON TABLE prot_protocol_remarks TO pimsview;


--
-- TOC entry 2981 (class 0 OID 0)
-- Dependencies: 1780
-- Name: prot_refinputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_refinputsample FROM PUBLIC;
REVOKE ALL ON TABLE prot_refinputsample FROM postgres;
GRANT ALL ON TABLE prot_refinputsample TO postgres;
GRANT ALL ON TABLE prot_refinputsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_refinputsample TO pimsupdate;
GRANT SELECT ON TABLE prot_refinputsample TO pimsview;


--
-- TOC entry 2982 (class 0 OID 0)
-- Dependencies: 1781
-- Name: prot_refoutputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_refoutputsample FROM PUBLIC;
REVOKE ALL ON TABLE prot_refoutputsample FROM postgres;
GRANT ALL ON TABLE prot_refoutputsample TO postgres;
GRANT ALL ON TABLE prot_refoutputsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE prot_refoutputsample TO pimsupdate;
GRANT SELECT ON TABLE prot_refoutputsample TO pimsview;


--
-- TOC entry 2983 (class 0 OID 0)
-- Dependencies: 1782
-- Name: ref_abstractholdertype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_abstractholdertype FROM PUBLIC;
REVOKE ALL ON TABLE ref_abstractholdertype FROM postgres;
GRANT ALL ON TABLE ref_abstractholdertype TO postgres;
GRANT ALL ON TABLE ref_abstractholdertype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_abstractholdertype TO pimsupdate;
GRANT SELECT ON TABLE ref_abstractholdertype TO pimsview;


--
-- TOC entry 2984 (class 0 OID 0)
-- Dependencies: 1783
-- Name: ref_componentcategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_componentcategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_componentcategory FROM postgres;
GRANT ALL ON TABLE ref_componentcategory TO postgres;
GRANT ALL ON TABLE ref_componentcategory TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_componentcategory TO pimsupdate;
GRANT SELECT ON TABLE ref_componentcategory TO pimsview;


--
-- TOC entry 2985 (class 0 OID 0)
-- Dependencies: 1784
-- Name: ref_crystaltype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_crystaltype FROM PUBLIC;
REVOKE ALL ON TABLE ref_crystaltype FROM postgres;
GRANT ALL ON TABLE ref_crystaltype TO postgres;
GRANT ALL ON TABLE ref_crystaltype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_crystaltype TO pimsupdate;
GRANT SELECT ON TABLE ref_crystaltype TO pimsview;


--
-- TOC entry 2986 (class 0 OID 0)
-- Dependencies: 1785
-- Name: ref_dbname; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_dbname FROM PUBLIC;
REVOKE ALL ON TABLE ref_dbname FROM postgres;
GRANT ALL ON TABLE ref_dbname TO postgres;
GRANT ALL ON TABLE ref_dbname TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_dbname TO pimsupdate;
GRANT SELECT ON TABLE ref_dbname TO pimsview;


--
-- TOC entry 2987 (class 0 OID 0)
-- Dependencies: 1786
-- Name: ref_experimenttype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_experimenttype FROM PUBLIC;
REVOKE ALL ON TABLE ref_experimenttype FROM postgres;
GRANT ALL ON TABLE ref_experimenttype TO postgres;
GRANT ALL ON TABLE ref_experimenttype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_experimenttype TO pimsupdate;
GRANT SELECT ON TABLE ref_experimenttype TO pimsview;


--
-- TOC entry 2988 (class 0 OID 0)
-- Dependencies: 1787
-- Name: ref_hazardphrase; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_hazardphrase FROM PUBLIC;
REVOKE ALL ON TABLE ref_hazardphrase FROM postgres;
GRANT ALL ON TABLE ref_hazardphrase TO postgres;
GRANT ALL ON TABLE ref_hazardphrase TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_hazardphrase TO pimsupdate;
GRANT SELECT ON TABLE ref_hazardphrase TO pimsview;


--
-- TOC entry 2989 (class 0 OID 0)
-- Dependencies: 1788
-- Name: ref_holdercategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_holdercategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_holdercategory FROM postgres;
GRANT ALL ON TABLE ref_holdercategory TO postgres;
GRANT ALL ON TABLE ref_holdercategory TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_holdercategory TO pimsupdate;
GRANT SELECT ON TABLE ref_holdercategory TO pimsview;


--
-- TOC entry 2990 (class 0 OID 0)
-- Dependencies: 1789
-- Name: ref_holdertype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_holdertype FROM PUBLIC;
REVOKE ALL ON TABLE ref_holdertype FROM postgres;
GRANT ALL ON TABLE ref_holdertype TO postgres;
GRANT ALL ON TABLE ref_holdertype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_holdertype TO pimsupdate;
GRANT SELECT ON TABLE ref_holdertype TO pimsview;


--
-- TOC entry 2991 (class 0 OID 0)
-- Dependencies: 1820
-- Name: ref_imagetype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_imagetype FROM PUBLIC;
REVOKE ALL ON TABLE ref_imagetype FROM postgres;
GRANT ALL ON TABLE ref_imagetype TO postgres;
GRANT SELECT ON TABLE ref_imagetype TO pimsview;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_imagetype TO pimsupdate;
GRANT ALL ON TABLE ref_imagetype TO pimsadmin;


--
-- TOC entry 2992 (class 0 OID 0)
-- Dependencies: 1790
-- Name: ref_instrumenttype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_instrumenttype FROM PUBLIC;
REVOKE ALL ON TABLE ref_instrumenttype FROM postgres;
GRANT ALL ON TABLE ref_instrumenttype TO postgres;
GRANT ALL ON TABLE ref_instrumenttype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_instrumenttype TO pimsupdate;
GRANT SELECT ON TABLE ref_instrumenttype TO pimsview;


--
-- TOC entry 2993 (class 0 OID 0)
-- Dependencies: 1791
-- Name: ref_organism; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_organism FROM PUBLIC;
REVOKE ALL ON TABLE ref_organism FROM postgres;
GRANT ALL ON TABLE ref_organism TO postgres;
GRANT ALL ON TABLE ref_organism TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_organism TO pimsupdate;
GRANT SELECT ON TABLE ref_organism TO pimsview;


--
-- TOC entry 2994 (class 0 OID 0)
-- Dependencies: 1792
-- Name: ref_pintype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_pintype FROM PUBLIC;
REVOKE ALL ON TABLE ref_pintype FROM postgres;
GRANT ALL ON TABLE ref_pintype TO postgres;
GRANT ALL ON TABLE ref_pintype TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_pintype TO pimsupdate;
GRANT SELECT ON TABLE ref_pintype TO pimsview;


--
-- TOC entry 2995 (class 0 OID 0)
-- Dependencies: 1793
-- Name: ref_publicentry; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_publicentry FROM PUBLIC;
REVOKE ALL ON TABLE ref_publicentry FROM postgres;
GRANT ALL ON TABLE ref_publicentry TO postgres;
GRANT ALL ON TABLE ref_publicentry TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_publicentry TO pimsupdate;
GRANT SELECT ON TABLE ref_publicentry TO pimsview;


--
-- TOC entry 2996 (class 0 OID 0)
-- Dependencies: 1794
-- Name: ref_samplecategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_samplecategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_samplecategory FROM postgres;
GRANT ALL ON TABLE ref_samplecategory TO postgres;
GRANT ALL ON TABLE ref_samplecategory TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_samplecategory TO pimsupdate;
GRANT SELECT ON TABLE ref_samplecategory TO pimsview;


--
-- TOC entry 2997 (class 0 OID 0)
-- Dependencies: 1795
-- Name: ref_targetstatus; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_targetstatus FROM PUBLIC;
REVOKE ALL ON TABLE ref_targetstatus FROM postgres;
GRANT ALL ON TABLE ref_targetstatus TO postgres;
GRANT ALL ON TABLE ref_targetstatus TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_targetstatus TO pimsupdate;
GRANT SELECT ON TABLE ref_targetstatus TO pimsview;


--
-- TOC entry 2998 (class 0 OID 0)
-- Dependencies: 1796
-- Name: ref_workflowitem; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_workflowitem FROM PUBLIC;
REVOKE ALL ON TABLE ref_workflowitem FROM postgres;
GRANT ALL ON TABLE ref_workflowitem TO postgres;
GRANT ALL ON TABLE ref_workflowitem TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ref_workflowitem TO pimsupdate;
GRANT SELECT ON TABLE ref_workflowitem TO pimsview;


--
-- TOC entry 2999 (class 0 OID 0)
-- Dependencies: 1797
-- Name: revisionnumber; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE revisionnumber FROM PUBLIC;
REVOKE ALL ON TABLE revisionnumber FROM postgres;
GRANT ALL ON TABLE revisionnumber TO postgres;
GRANT ALL ON TABLE revisionnumber TO pimsadmin;
GRANT SELECT ON TABLE revisionnumber TO pimsview;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE revisionnumber TO pimsupdate;


--
-- TOC entry 3000 (class 0 OID 0)
-- Dependencies: 1798
-- Name: sam_abstractsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_abstractsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_abstractsample FROM postgres;
GRANT ALL ON TABLE sam_abstractsample TO postgres;
GRANT ALL ON TABLE sam_abstractsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_abstractsample TO pimsupdate;
GRANT SELECT ON TABLE sam_abstractsample TO pimsview;


--
-- TOC entry 3001 (class 0 OID 0)
-- Dependencies: 1799
-- Name: sam_abstsa2hazaph; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_abstsa2hazaph FROM PUBLIC;
REVOKE ALL ON TABLE sam_abstsa2hazaph FROM postgres;
GRANT ALL ON TABLE sam_abstsa2hazaph TO postgres;
GRANT ALL ON TABLE sam_abstsa2hazaph TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_abstsa2hazaph TO pimsupdate;
GRANT SELECT ON TABLE sam_abstsa2hazaph TO pimsview;


--
-- TOC entry 3002 (class 0 OID 0)
-- Dependencies: 1800
-- Name: sam_crystalsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_crystalsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_crystalsample FROM postgres;
GRANT ALL ON TABLE sam_crystalsample TO postgres;
GRANT ALL ON TABLE sam_crystalsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_crystalsample TO pimsupdate;
GRANT SELECT ON TABLE sam_crystalsample TO pimsview;


--
-- TOC entry 3003 (class 0 OID 0)
-- Dependencies: 1801
-- Name: sam_refsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_refsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_refsample FROM postgres;
GRANT ALL ON TABLE sam_refsample TO postgres;
GRANT ALL ON TABLE sam_refsample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_refsample TO pimsupdate;
GRANT SELECT ON TABLE sam_refsample TO pimsview;


--
-- TOC entry 3004 (class 0 OID 0)
-- Dependencies: 1802
-- Name: sam_refsamplesource; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_refsamplesource FROM PUBLIC;
REVOKE ALL ON TABLE sam_refsamplesource FROM postgres;
GRANT ALL ON TABLE sam_refsamplesource TO postgres;
GRANT ALL ON TABLE sam_refsamplesource TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_refsamplesource TO pimsupdate;
GRANT SELECT ON TABLE sam_refsamplesource TO pimsview;


--
-- TOC entry 3005 (class 0 OID 0)
-- Dependencies: 1803
-- Name: sam_sampca2abstsa; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_sampca2abstsa FROM PUBLIC;
REVOKE ALL ON TABLE sam_sampca2abstsa FROM postgres;
GRANT ALL ON TABLE sam_sampca2abstsa TO postgres;
GRANT ALL ON TABLE sam_sampca2abstsa TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_sampca2abstsa TO pimsupdate;
GRANT SELECT ON TABLE sam_sampca2abstsa TO pimsview;


--
-- TOC entry 3006 (class 0 OID 0)
-- Dependencies: 1804
-- Name: sam_sample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_sample FROM PUBLIC;
REVOKE ALL ON TABLE sam_sample FROM postgres;
GRANT ALL ON TABLE sam_sample TO postgres;
GRANT ALL ON TABLE sam_sample TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_sample TO pimsupdate;
GRANT SELECT ON TABLE sam_sample TO pimsview;


--
-- TOC entry 3007 (class 0 OID 0)
-- Dependencies: 1805
-- Name: sam_samplecomponent; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_samplecomponent FROM PUBLIC;
REVOKE ALL ON TABLE sam_samplecomponent FROM postgres;
GRANT ALL ON TABLE sam_samplecomponent TO postgres;
GRANT ALL ON TABLE sam_samplecomponent TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sam_samplecomponent TO pimsupdate;
GRANT SELECT ON TABLE sam_samplecomponent TO pimsview;


--
-- TOC entry 3008 (class 0 OID 0)
-- Dependencies: 1806
-- Name: sche_scheduledtask; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduledtask FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduledtask FROM postgres;
GRANT ALL ON TABLE sche_scheduledtask TO postgres;
GRANT ALL ON TABLE sche_scheduledtask TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sche_scheduledtask TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduledtask TO pimsview;


--
-- TOC entry 3009 (class 0 OID 0)
-- Dependencies: 1807
-- Name: sche_scheduleplan; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduleplan FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduleplan FROM postgres;
GRANT ALL ON TABLE sche_scheduleplan TO postgres;
GRANT ALL ON TABLE sche_scheduleplan TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sche_scheduleplan TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduleplan TO pimsview;


--
-- TOC entry 3010 (class 0 OID 0)
-- Dependencies: 1808
-- Name: sche_scheduleplanoffset; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduleplanoffset FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduleplanoffset FROM postgres;
GRANT ALL ON TABLE sche_scheduleplanoffset TO postgres;
GRANT ALL ON TABLE sche_scheduleplanoffset TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE sche_scheduleplanoffset TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduleplanoffset TO pimsview;


--
-- TOC entry 3011 (class 0 OID 0)
-- Dependencies: 1819
-- Name: targ_alias; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_alias FROM PUBLIC;
REVOKE ALL ON TABLE targ_alias FROM postgres;
GRANT ALL ON TABLE targ_alias TO postgres;
GRANT SELECT ON TABLE targ_alias TO pimsview;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_alias TO pimsupdate;
GRANT ALL ON TABLE targ_alias TO pimsadmin;


--
-- TOC entry 3012 (class 0 OID 0)
-- Dependencies: 1809
-- Name: targ_milestone; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_milestone FROM PUBLIC;
REVOKE ALL ON TABLE targ_milestone FROM postgres;
GRANT ALL ON TABLE targ_milestone TO postgres;
GRANT ALL ON TABLE targ_milestone TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_milestone TO pimsupdate;
GRANT SELECT ON TABLE targ_milestone TO pimsview;


--
-- TOC entry 3013 (class 0 OID 0)
-- Dependencies: 1810
-- Name: targ_project; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_project FROM PUBLIC;
REVOKE ALL ON TABLE targ_project FROM postgres;
GRANT ALL ON TABLE targ_project TO postgres;
GRANT ALL ON TABLE targ_project TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_project TO pimsupdate;
GRANT SELECT ON TABLE targ_project TO pimsview;


--
-- TOC entry 3014 (class 0 OID 0)
-- Dependencies: 1811
-- Name: targ_researchobjective; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_researchobjective FROM PUBLIC;
REVOKE ALL ON TABLE targ_researchobjective FROM postgres;
GRANT ALL ON TABLE targ_researchobjective TO postgres;
GRANT ALL ON TABLE targ_researchobjective TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_researchobjective TO pimsupdate;
GRANT SELECT ON TABLE targ_researchobjective TO pimsview;


--
-- TOC entry 3015 (class 0 OID 0)
-- Dependencies: 1812
-- Name: targ_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target FROM PUBLIC;
REVOKE ALL ON TABLE targ_target FROM postgres;
GRANT ALL ON TABLE targ_target TO postgres;
GRANT ALL ON TABLE targ_target TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_target TO pimsupdate;
GRANT SELECT ON TABLE targ_target TO pimsview;


--
-- TOC entry 3016 (class 0 OID 0)
-- Dependencies: 1813
-- Name: targ_target2nuclac; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target2nuclac FROM PUBLIC;
REVOKE ALL ON TABLE targ_target2nuclac FROM postgres;
GRANT ALL ON TABLE targ_target2nuclac TO postgres;
GRANT ALL ON TABLE targ_target2nuclac TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_target2nuclac TO pimsupdate;
GRANT SELECT ON TABLE targ_target2nuclac TO pimsview;


--
-- TOC entry 3017 (class 0 OID 0)
-- Dependencies: 1814
-- Name: targ_target2projects; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target2projects FROM PUBLIC;
REVOKE ALL ON TABLE targ_target2projects FROM postgres;
GRANT ALL ON TABLE targ_target2projects TO postgres;
GRANT ALL ON TABLE targ_target2projects TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_target2projects TO pimsupdate;
GRANT SELECT ON TABLE targ_target2projects TO pimsview;


--
-- TOC entry 3018 (class 0 OID 0)
-- Dependencies: 1815
-- Name: targ_targetgroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_targetgroup FROM PUBLIC;
REVOKE ALL ON TABLE targ_targetgroup FROM postgres;
GRANT ALL ON TABLE targ_targetgroup TO postgres;
GRANT ALL ON TABLE targ_targetgroup TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_targetgroup TO pimsupdate;
GRANT SELECT ON TABLE targ_targetgroup TO pimsview;


--
-- TOC entry 3019 (class 0 OID 0)
-- Dependencies: 1816
-- Name: targ_targgr2targets; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_targgr2targets FROM PUBLIC;
REVOKE ALL ON TABLE targ_targgr2targets FROM postgres;
GRANT ALL ON TABLE targ_targgr2targets TO postgres;
GRANT ALL ON TABLE targ_targgr2targets TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE targ_targgr2targets TO pimsupdate;
GRANT SELECT ON TABLE targ_targgr2targets TO pimsview;


--
-- TOC entry 3020 (class 0 OID 0)
-- Dependencies: 1818
-- Name: trag_researchobjectiveelement; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE trag_researchobjectiveelement FROM PUBLIC;
REVOKE ALL ON TABLE trag_researchobjectiveelement FROM postgres;
GRANT ALL ON TABLE trag_researchobjectiveelement TO postgres;
GRANT ALL ON TABLE trag_researchobjectiveelement TO pimsadmin;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE trag_researchobjectiveelement TO pimsupdate;
GRANT SELECT ON TABLE trag_researchobjectiveelement TO pimsview;


--
-- TOC entry 3022 (class 0 OID 0)
-- Dependencies: 1747
-- Name: generic_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE generic_target FROM PUBLIC;
REVOKE ALL ON SEQUENCE generic_target FROM postgres;
GRANT ALL ON SEQUENCE generic_target TO postgres;
GRANT ALL ON SEQUENCE generic_target TO pimsadmin;
GRANT SELECT ON SEQUENCE generic_target TO pimsview;
GRANT SELECT,UPDATE ON SEQUENCE generic_target TO pimsupdate;


--
-- TOC entry 3024 (class 0 OID 0)
-- Dependencies: 1748
-- Name: hibernate_sequence; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE hibernate_sequence FROM PUBLIC;
REVOKE ALL ON SEQUENCE hibernate_sequence FROM postgres;
GRANT ALL ON SEQUENCE hibernate_sequence TO postgres;
GRANT ALL ON SEQUENCE hibernate_sequence TO pimsadmin;
GRANT SELECT ON SEQUENCE hibernate_sequence TO pimsview;
GRANT SELECT,UPDATE ON SEQUENCE hibernate_sequence TO pimsupdate;


--
-- TOC entry 3026 (class 0 OID 0)
-- Dependencies: 1817
-- Name: test_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE test_target FROM PUBLIC;
REVOKE ALL ON SEQUENCE test_target FROM postgres;
GRANT ALL ON SEQUENCE test_target TO postgres;
GRANT ALL ON SEQUENCE test_target TO pimsadmin;
GRANT SELECT ON SEQUENCE test_target TO pimsview;
GRANT SELECT,UPDATE ON SEQUENCE test_target TO pimsupdate;


-- Completed on 2009-06-10 09:56:05

--
-- PostgreSQL database dump complete
--

