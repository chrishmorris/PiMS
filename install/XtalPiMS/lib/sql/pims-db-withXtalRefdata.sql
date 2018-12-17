--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: 
--

CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
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


--
-- Name: acco_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_user (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL,
    personid bigint
);


--
-- Name: acco_usergroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL,
    headerid bigint
);


--
-- Name: acco_usergroup2leaders; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup2leaders (
    ledgroupid bigint NOT NULL,
    leaderid bigint NOT NULL
);


--
-- Name: acco_usergroup2members; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup2members (
    usergroupid bigint NOT NULL,
    memberid bigint NOT NULL
);


--
-- Name: core_accessobject; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_accessobject (
    name character varying(80) NOT NULL,
    systemclassid bigint NOT NULL
);


--
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


--
-- Name: core_applicationdata; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_applicationdata (
    application character varying(80) NOT NULL,
    keyword character varying(80) NOT NULL,
    "type" character varying(80),
    value text NOT NULL,
    attachmentid bigint NOT NULL
);


--
-- Name: core_attachment; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_attachment (
    date timestamp with time zone,
    dbid bigint NOT NULL,
    details text,
    parententryid bigint NOT NULL,
    authorid bigint
);


--
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


--
-- Name: core_citation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_citation (
    authors text,
    editors text,
    firstpage character varying(80),
    lastpage character varying(80),
    status character varying(80),
    title character varying,
    "year" integer,
    attachmentid bigint NOT NULL
);


--
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


--
-- Name: core_externaldblink; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_externaldblink (
    code character varying(80),
    "release" character varying(80),
    url text,
    attachmentid bigint NOT NULL,
    dbnameid bigint NOT NULL
);


--
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


--
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


--
-- Name: core_note; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_note (
    name character varying(80),
    attachmentid bigint NOT NULL
);


--
-- Name: core_note2relatedentrys; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_note2relatedentrys (
    noteid bigint NOT NULL,
    labbookentryid bigint NOT NULL
);


--
-- Name: core_systemclass; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_systemclass (
    dbid bigint NOT NULL,
    details text
);


--
-- Name: core_thesiscitation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE core_thesiscitation (
    city character varying(80),
    country character varying(80),
    institution character varying(254),
    stateprovince character varying(80),
    citationid bigint NOT NULL
);


--
-- Name: cryz_cypade_possva; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_cypade_possva (
    parameterdefinitionid bigint NOT NULL,
    possiblevalue character varying(255),
    order_ integer NOT NULL
);


--
-- Name: cryz_dropannotation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_dropannotation (
    cmdlineparam text,
    scoredate timestamp with time zone,
    labbookentryid bigint NOT NULL,
    scoreid bigint NOT NULL,
    sampleid bigint,
    imageid bigint,
    softwareid bigint,
    holderid bigint
);


--
-- Name: cryz_image; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_image (
    filename character varying(254) NOT NULL,
    filepath character varying(254) NOT NULL,
    mimetype character varying(80),
    labbookentryid bigint NOT NULL,
    instrumentid bigint,
    scheduledtaskid bigint,
    sampleid bigint,
    imagetypeid bigint
);


--
-- Name: cryz_parameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_parameter (
    value character varying(254),
    labbookentryid bigint NOT NULL,
    parameterdefinitionid bigint NOT NULL,
    imageid bigint NOT NULL
);


--
-- Name: cryz_parameterdefinition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_parameterdefinition (
    defaultvalue character varying(80),
    displayunit character varying(32),
    label text,
    "maxvalue" character varying(80),
    "minvalue" character varying(80),
    name character varying(80),
    paramtype character varying(32),
    unit character varying(32),
    labbookentryid bigint NOT NULL
);


--
-- Name: cryz_score; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_score (
    color character varying(80),
    name character varying(80),
    value integer NOT NULL,
    labbookentryid bigint NOT NULL,
    scoringschemeid bigint NOT NULL
);


--
-- Name: cryz_scoringscheme; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_scoringscheme (
    name character varying(80) NOT NULL,
    version character varying(80),
    labbookentryid bigint NOT NULL
);


--
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
    instrumentid bigint,
    groupid bigint,
    operatorid bigint,
    researchobjectiveid bigint,
    experimentgroupid bigint,
    experimenttypeid bigint NOT NULL,
    methodid bigint
);


--
-- Name: expe_experimentgroup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_experimentgroup (
    enddate timestamp with time zone,
    name character varying(80) NOT NULL,
    purpose character varying(80) NOT NULL,
    startdate timestamp with time zone,
    labbookentryid bigint NOT NULL
);


--
-- Name: expe_inputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_inputsample (
    amount double precision,
    amountdisplayunit character varying(32),
    amountunit character varying(32),
    name character varying(80),
    "role" character varying(80),
    labbookentryid bigint NOT NULL,
    experimentid bigint NOT NULL,
    refinputsampleid bigint,
    sampleid bigint
);


--
-- Name: expe_instrument; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_instrument (
    model character varying(80),
    name character varying(80) NOT NULL,
    serialnumber character varying(80),
    labbookentryid bigint NOT NULL,
    pressuredisplayunit character varying(32),
    temperature double precision,
    pressure double precision,
    tempdisplayunit character varying(32),
    manufacturerid bigint,
    defaultimagetypeid bigint,
    locationid bigint
);


--
-- Name: expe_method; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_method (
    name character varying(80),
    procedure_ text,
    task character varying(80),
    labbookentryid bigint NOT NULL,
    instrumentid bigint,
    softwareid bigint
);


--
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


--
-- Name: expe_outputsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_outputsample (
    amount double precision,
    amountdisplayunit character varying(32),
    amountunit character varying(32),
    name character varying(80),
    "role" character varying(80),
    labbookentryid bigint NOT NULL,
    experimentid bigint NOT NULL,
    refoutputsampleid bigint,
    sampleid bigint
);


--
-- Name: expe_parameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_parameter (
    name character varying(80),
    paramtype character varying(32),
    unit character varying(32),
    value character varying(254),
    labbookentryid bigint NOT NULL,
    experimentid bigint NOT NULL,
    parameterdefinitionid bigint
);


--
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


--
-- Name: expe_software_tasks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_software_tasks (
    softwareid bigint NOT NULL,
    task character varying(255),
    order_ integer NOT NULL
);


--
-- Name: generic_target; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE generic_target
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: generic_target; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('generic_target', 1, false);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 46000, true);


--
-- Name: hold_abstractholder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_abstractholder (
    colposition integer,
    name character varying(80) NOT NULL,
    rowposition integer,
    subposition integer,
    labbookentryid bigint NOT NULL,
    holdertypeid bigint,
    abstractholderid bigint
);


--
-- Name: hold_crystalnumber; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW hold_crystalnumber AS
    SELECT dropannota1_0.holderid, count(dropannota1_0.labbookentryid) AS numberofcrystals FROM cryz_dropannotation dropannota1_0 WHERE (dropannota1_0.scoreid = 134690) GROUP BY dropannota1_0.holderid;


--
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
    holderid bigint,
    refsampleid bigint,
    assigntoid bigint
);


--
-- Name: hold_firstsample; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW hold_firstsample AS
    SELECT sam_sample.holderid, min(sam_sample.abstractsampleid) AS sampleid FROM sam_sample GROUP BY sam_sample.holderid;


--
-- Name: hold_holdca2abstholders; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdca2abstholders (
    holdercategoryid bigint NOT NULL,
    abstholderid bigint NOT NULL
);


--
-- Name: hold_holdca2absthoty; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdca2absthoty (
    holdercategoryid bigint NOT NULL,
    abstractholdertypeid bigint NOT NULL
);


--
-- Name: hold_holder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holder (
    enddate timestamp with time zone,
    startdate timestamp with time zone,
    abstractholderid bigint NOT NULL
);


--
-- Name: hold_holderlocation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holderlocation (
    enddate timestamp with time zone,
    startdate timestamp with time zone NOT NULL,
    labbookentryid bigint NOT NULL,
    holderid bigint NOT NULL,
    locationid bigint NOT NULL
);


--
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


--
-- Name: sche_scheduledtask; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduledtask (
    priority integer,
    scheduledtime timestamp with time zone NOT NULL,
    state integer,
    labbookentryid bigint NOT NULL,
    name character varying(80) NOT NULL,
    completiontime timestamp with time zone,
    instrumentid bigint,
    holderid bigint NOT NULL,
    scheduleplanoffsetid bigint
);


--
-- Name: hold_lastinspection; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW hold_lastinspection AS
    SELECT inpsection_0.holderid, inpsection_0.labbookentryid FROM sche_scheduledtask inpsection_0 WHERE ((inpsection_0.completiontime IS NOT NULL) AND (inpsection_0.completiontime = (SELECT max(inpsection_01.completiontime) AS max FROM sche_scheduledtask inpsection_01 WHERE (inpsection_01.holderid = inpsection_0.holderid) GROUP BY inpsection_01.holderid)));


--
-- Name: hold_refholder; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_refholder (
    abstractholderid bigint NOT NULL
);


--
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


--
-- Name: hold_refsampleposition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_refsampleposition (
    colposition integer,
    rowposition integer,
    subposition integer,
    labbookentryid bigint NOT NULL,
    refsampleid bigint,
    refholderid bigint NOT NULL
);


--
-- Name: inst_instty2inst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE inst_instty2inst (
    instrumenttypeid bigint NOT NULL,
    instrumentid bigint NOT NULL
);


--
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


--
-- Name: mole_abstco_keywords; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstco_keywords (
    abstractcomponentid bigint NOT NULL,
    keyword character varying(255),
    order_ integer NOT NULL
);


--
-- Name: mole_abstco_synonyms; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstco_synonyms (
    abstractcomponentid bigint NOT NULL,
    synonym character varying(255),
    order_ integer NOT NULL
);


--
-- Name: mole_abstractcomponent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_abstractcomponent (
    name character varying(80) NOT NULL,
    labbookentryid bigint NOT NULL,
    naturalsourceid bigint
);


--
-- Name: mole_compca2components; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_compca2components (
    categoryid bigint NOT NULL,
    componentid bigint NOT NULL
);


--
-- Name: mole_construct; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_construct (
    constructstatus character varying(80) NOT NULL,
    "function" text,
    markerdetails text,
    promoterdetails text,
    resistancedetails text,
    sequencetype character varying(80),
    moleculeid bigint NOT NULL
);


--
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


--
-- Name: mole_molecule2relareobel; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_molecule2relareobel (
    trialmoleculeid bigint NOT NULL,
    relatedresearchobjectiveelementid bigint NOT NULL
);


--
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


--
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


--
-- Name: peop_group; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_group (
    name character varying(80),
    url character varying(254),
    labbookentryid bigint NOT NULL,
    organisationid bigint NOT NULL
);


--
-- Name: peop_orga_addresses; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_orga_addresses (
    organisationid bigint NOT NULL,
    address character varying(255),
    order_ integer NOT NULL
);


--
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


--
-- Name: peop_persingr_phonnu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_persingr_phonnu (
    personingroupid bigint NOT NULL,
    phonenumber character varying(255),
    order_ integer NOT NULL
);


--
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


--
-- Name: peop_person_middin; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE peop_person_middin (
    personid bigint NOT NULL,
    middleinitial character varying(255),
    order_ integer NOT NULL
);


--
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
    groupid bigint NOT NULL,
    personid bigint NOT NULL
);


--
-- Name: prot_parade_possva; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_parade_possva (
    parameterdefinitionid bigint NOT NULL,
    possiblevalue character varying(255),
    order_ integer NOT NULL
);


--
-- Name: prot_parameterdefinition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_parameterdefinition (
    defaultvalue character varying(80),
    displayunit character varying(32),
    isgrouplevel boolean NOT NULL,
    ismandatory boolean NOT NULL,
    isresult boolean NOT NULL,
    label text,
    "maxvalue" character varying(80),
    "minvalue" character varying(80),
    name character varying(80) NOT NULL,
    paramtype character varying(32) NOT NULL,
    unit character varying(32),
    labbookentryid bigint NOT NULL,
    protocolid bigint NOT NULL,
    order_ integer
);


--
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


--
-- Name: prot_protocol_remarks; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE prot_protocol_remarks (
    protocolid bigint NOT NULL,
    remark character varying(255),
    order_ integer NOT NULL
);


--
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


--
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


--
-- Name: ref_abstractholdertype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_abstractholdertype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_componentcategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_componentcategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_crystaltype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_crystaltype (
    ressubpos integer NOT NULL,
    holdertypeid bigint NOT NULL
);


--
-- Name: ref_dbname; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_dbname (
    fullname text,
    name character varying(80) NOT NULL,
    url text,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_experimenttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_experimenttype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_hazardphrase; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_hazardphrase (
    classification character varying(32) NOT NULL,
    code character varying(80) NOT NULL,
    phrase character varying(254),
    publicentryid bigint NOT NULL
);


--
-- Name: ref_holdercategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_holdercategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
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


--
-- Name: ref_imagetype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_imagetype (
    publicentryid bigint NOT NULL,
    sizey integer,
    sizex integer,
    url character varying(180),
    xlengthperpixel double precision,
    colourdepth integer,
    name character varying(80) NOT NULL,
    ylengthperpixel double precision,
    catorgory character varying(180)
);


--
-- Name: ref_instrumenttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_instrumenttype (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
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


--
-- Name: ref_pintype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_pintype (
    length double precision,
    looplength double precision,
    looptype character varying(80),
    wirewidth double precision,
    abstractholdertypeid bigint NOT NULL
);


--
-- Name: ref_publicentry; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_publicentry (
    dbid bigint NOT NULL,
    details text
);


--
-- Name: ref_samplecategory; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_samplecategory (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_targetstatus; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_targetstatus (
    name character varying(80) NOT NULL,
    publicentryid bigint NOT NULL
);


--
-- Name: ref_workflowitem; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_workflowitem (
    publicentryid bigint NOT NULL,
    experimenttypeid bigint NOT NULL,
    statusid bigint
);


--
-- Name: revisionnumber; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE revisionnumber (
    revision integer NOT NULL,
    name character varying(254) NOT NULL,
    "release" character varying(254) NOT NULL,
    tag character varying(254) NOT NULL,
    author character varying(254) NOT NULL,
    date character varying(254) NOT NULL
);


--
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


--
-- Name: sam_abstsa2hazaph; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_abstsa2hazaph (
    otherrole bigint NOT NULL,
    hazardphraseid bigint NOT NULL
);


--
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


--
-- Name: sam_refsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_refsample (
    abstractsampleid bigint NOT NULL,
    issaltcrystal boolean
);


--
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


--
-- Name: sam_sampca2abstsa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_sampca2abstsa (
    samplecategoryid bigint NOT NULL,
    abstractsampleid bigint NOT NULL
);


--
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


--
-- Name: sam_trialsample; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_trialsample (
    sampleid bigint NOT NULL,
    insynchrotron boolean,
    conditionid bigint,
    proteinid bigint
);


--
-- Name: sche_scheduleplan; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduleplan (
    name character varying(80) NOT NULL,
    labbookentryid bigint NOT NULL
);


--
-- Name: sche_scheduleplanoffset; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduleplanoffset (
    offsettime bigint NOT NULL,
    priority integer,
    labbookentryid bigint NOT NULL,
    order_ integer,
    scheduleplanid bigint NOT NULL
);


--
-- Name: targ_alias; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_alias (
    labbookentryid bigint NOT NULL,
    name character varying(255) NOT NULL,
    targetid bigint NOT NULL
);


--
-- Name: targ_milestone; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_milestone (
    date_ timestamp with time zone NOT NULL,
    labbookentryid bigint NOT NULL,
    experimentid bigint,
    targetid bigint NOT NULL,
    statusid bigint NOT NULL
);


--
-- Name: targ_project; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_project (
    completename text NOT NULL,
    shortname character varying(80) NOT NULL,
    startdate timestamp with time zone,
    labbookentryid bigint NOT NULL,
    projectid bigint
);


--
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


--
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
    proteinid bigint NOT NULL,
    speciesid bigint
);


--
-- Name: targ_target2nuclac; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_target2nuclac (
    nuctargetid bigint NOT NULL,
    nucleicacidid bigint NOT NULL
);


--
-- Name: targ_target2projects; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_target2projects (
    targetid bigint NOT NULL,
    projectid bigint NOT NULL
);


--
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


--
-- Name: targ_targgr2targets; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_targgr2targets (
    targetgroupid bigint NOT NULL,
    targetid bigint NOT NULL
);


--
-- Name: test_target; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE test_target
    START WITH 11
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: test_target; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('test_target', 11, false);


--
-- Name: trag_researchobjectiveelement; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE trag_researchobjectiveelement (
    alwaysincluded boolean,
    approxbeginseqid integer,
    approxendseqid integer,
    componenttype character varying(32) NOT NULL,
    "domain" character varying(80),
    status character varying(80),
    whychosen text NOT NULL,
    labbookentryid bigint NOT NULL,
    targetid bigint,
    researchobjectiveid bigint NOT NULL,
    moleculeid bigint
);


--
-- Data for Name: acco_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_permission (optype, permission, permissionclass, rolename, systemclassid, accessobjectid, usergroupid) FROM stdin;
update	t	PIMS	any	8006	8002	8004
read	t	PIMS	any	8007	8002	8004
create	t	PIMS	any	8008	8002	8004
\.


--
-- Data for Name: acco_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_user (name, systemclassid, personid) FROM stdin;
\.


--
-- Data for Name: acco_usergroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup (name, systemclassid, headerid) FROM stdin;
public	8004	\N
\.


--
-- Data for Name: acco_usergroup2leaders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup2leaders (ledgroupid, leaderid) FROM stdin;
\.


--
-- Data for Name: acco_usergroup2members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup2members (usergroupid, memberid) FROM stdin;
\.


--
-- Data for Name: core_accessobject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_accessobject (name, systemclassid) FROM stdin;
public	8002
\.


--
-- Data for Name: core_annotation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_annotation (filename, legend, mimetype, name, title, attachmentid) FROM stdin;
\.


--
-- Data for Name: core_applicationdata; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_applicationdata (application, keyword, "type", value, attachmentid) FROM stdin;
\.


--
-- Data for Name: core_attachment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_attachment (date, dbid, details, parententryid, authorid) FROM stdin;
\.


--
-- Data for Name: core_bookcitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_bookcitation (bookseries, booktitle, isbn, publisher, publishercity, volume, citationid) FROM stdin;
\.


--
-- Data for Name: core_citation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_citation (authors, editors, firstpage, lastpage, status, title, "year", attachmentid) FROM stdin;
\.


--
-- Data for Name: core_conferencecitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_conferencecitation (abstractnumber, city, conferencesite, conferencetitle, country, enddate, startdate, stateprovince, citationid) FROM stdin;
\.


--
-- Data for Name: core_externaldblink; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_externaldblink (code, "release", url, attachmentid, dbnameid) FROM stdin;
\.


--
-- Data for Name: core_journalcitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_journalcitation (astm, csd, issn, issue, journalabbreviation, journalfullname, volume, citationid) FROM stdin;
\.


--
-- Data for Name: core_labbookentry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_labbookentry (creationdate, lastediteddate, dbid, accessid, details, lasteditorid, creatorid) FROM stdin;
2008-11-21 11:13:50.12+00	\N	8001	8002	\N	\N	\N
2008-11-21 11:14:22.50+00	\N	14001	\N	\N	\N	\N
2008-11-21 11:14:22.616+00	\N	14003	\N	\N	\N	\N
2008-11-21 11:14:22.635+00	\N	14005	\N	\N	\N	\N
2008-11-21 11:14:22.654+00	\N	14007	\N	\N	\N	\N
2008-11-21 11:14:22.676+00	\N	14009	\N	\N	\N	\N
2008-11-21 11:14:22.699+00	\N	14011	\N	\N	\N	\N
2008-11-21 11:14:22.723+00	\N	14013	\N	\N	\N	\N
2008-11-21 11:14:22.749+00	\N	14015	\N	\N	\N	\N
2008-11-21 11:14:22.776+00	\N	14017	\N	\N	\N	\N
2008-11-21 11:14:22.804+00	\N	14019	\N	\N	\N	\N
2008-11-21 11:14:22.834+00	\N	14021	\N	\N	\N	\N
2008-11-21 11:14:22.865+00	\N	14023	\N	\N	\N	\N
2008-11-21 11:14:22.897+00	\N	14025	\N	\N	\N	\N
2008-11-21 11:14:23.257+00	\N	14027	\N	\N	\N	\N
2008-11-21 11:14:23.294+00	\N	14029	\N	\N	\N	\N
2008-11-21 11:14:23.332+00	\N	14031	\N	\N	\N	\N
2008-11-21 11:14:23.371+00	\N	14033	\N	\N	\N	\N
2008-11-21 11:14:23.411+00	\N	14035	\N	\N	\N	\N
2008-11-21 11:14:23.454+00	\N	14037	\N	\N	\N	\N
2008-11-21 11:14:23.501+00	\N	14039	\N	\N	\N	\N
2008-11-21 11:14:23.547+00	\N	14041	\N	\N	\N	\N
2008-11-21 11:14:23.593+00	\N	14043	\N	\N	\N	\N
2008-11-21 11:14:23.64+00	\N	14045	\N	\N	\N	\N
2008-11-21 11:14:23.689+00	\N	14047	\N	\N	\N	\N
2008-11-21 11:14:23.74+00	\N	14049	\N	\N	\N	\N
2008-11-21 11:14:23.791+00	\N	14051	\N	\N	\N	\N
2008-11-21 11:14:23.844+00	\N	14053	\N	\N	\N	\N
2008-11-21 11:14:23.898+00	\N	14055	\N	\N	\N	\N
2008-11-21 11:14:23.953+00	\N	14057	\N	\N	\N	\N
2008-11-21 11:14:24.01+00	\N	14059	\N	\N	\N	\N
2008-11-21 11:14:24.069+00	\N	14061	\N	\N	\N	\N
2008-11-21 11:14:24.129+00	\N	14063	\N	\N	\N	\N
2008-11-21 11:14:24.191+00	\N	14065	\N	\N	\N	\N
2008-11-21 11:14:34.981+00	\N	16001	\N	\N	\N	\N
2008-11-21 11:14:35.064+00	\N	16003	\N	\N	\N	\N
2008-11-21 11:14:35.083+00	\N	16004	\N	\N	\N	\N
2008-11-21 11:14:35.104+00	\N	16006	\N	\N	\N	\N
2008-11-21 11:14:35.121+00	\N	16007	\N	\N	\N	\N
2008-11-21 11:14:35.144+00	\N	16009	\N	\N	\N	\N
2008-11-21 11:14:35.164+00	\N	16010	\N	\N	\N	\N
2008-11-21 11:14:35.188+00	\N	16012	\N	\N	\N	\N
2008-11-21 11:14:35.209+00	\N	16013	\N	\N	\N	\N
2008-11-21 11:14:35.236+00	\N	16015	\N	\N	\N	\N
2008-11-21 11:14:35.26+00	\N	16016	\N	\N	\N	\N
2008-11-21 11:14:35.29+00	\N	16018	\N	\N	\N	\N
2008-11-21 11:14:35.317+00	\N	16019	\N	\N	\N	\N
2008-11-21 11:14:35.35+00	\N	16021	\N	\N	\N	\N
2008-11-21 11:14:35.378+00	\N	16022	\N	\N	\N	\N
2008-11-21 11:14:35.411+00	\N	16024	\N	\N	\N	\N
2008-11-21 11:14:35.442+00	\N	16025	\N	\N	\N	\N
2008-11-21 11:14:35.477+00	\N	16027	\N	\N	\N	\N
2008-11-21 11:14:35.512+00	\N	16028	\N	\N	\N	\N
2008-11-21 11:14:35.552+00	\N	16030	\N	\N	\N	\N
2008-11-21 11:14:35.613+00	\N	16031	\N	\N	\N	\N
2008-11-21 11:14:35.651+00	\N	16032	\N	\N	\N	\N
2008-11-21 11:14:35.694+00	\N	16034	\N	\N	\N	\N
2008-11-21 11:14:35.733+00	\N	16035	\N	\N	\N	\N
2008-11-21 11:14:35.767+00	\N	16037	\N	\N	\N	\N
2008-11-21 11:14:35.799+00	\N	16038	\N	\N	\N	\N
2008-11-21 11:14:35.834+00	\N	16040	\N	\N	\N	\N
2008-11-21 11:14:35.866+00	\N	16041	\N	\N	\N	\N
2008-11-21 11:14:35.902+00	\N	16043	\N	\N	\N	\N
2008-11-21 11:14:35.935+00	\N	16044	\N	\N	\N	\N
2008-11-21 11:14:35.974+00	\N	16046	\N	\N	\N	\N
2008-11-21 11:14:36.009+00	\N	16047	\N	\N	\N	\N
2008-11-21 11:14:36.048+00	\N	16049	\N	\N	\N	\N
2008-11-21 11:14:36.088+00	\N	16051	\N	\N	\N	\N
2008-11-21 11:14:36.125+00	\N	16052	\N	\N	\N	\N
2008-11-21 11:14:36.166+00	\N	16054	\N	\N	\N	\N
2008-11-21 11:14:36.205+00	\N	16055	\N	\N	\N	\N
2008-11-21 11:14:36.243+00	\N	16057	\N	\N	\N	\N
2008-11-21 11:14:36.279+00	\N	16058	\N	\N	\N	\N
2008-11-21 11:14:36.319+00	\N	16060	\N	\N	\N	\N
2008-11-21 11:14:36.357+00	\N	16061	\N	\N	\N	\N
2008-11-21 11:14:36.399+00	\N	16063	\N	\N	\N	\N
2008-11-21 11:14:36.438+00	\N	16064	\N	\N	\N	\N
2008-11-21 11:14:36.481+00	\N	16066	\N	\N	\N	\N
2008-11-21 11:14:36.522+00	\N	16067	\N	\N	\N	\N
2008-11-21 11:14:36.568+00	\N	16069	\N	\N	\N	\N
2008-11-21 11:14:36.608+00	\N	16070	\N	\N	\N	\N
2008-11-21 11:14:36.654+00	\N	16072	\N	\N	\N	\N
2008-11-21 11:14:36.696+00	\N	16073	\N	\N	\N	\N
2008-11-21 11:14:36.741+00	\N	16075	\N	\N	\N	\N
2008-11-21 11:14:36.782+00	\N	16076	\N	\N	\N	\N
2008-11-21 11:14:36.828+00	\N	16078	\N	\N	\N	\N
2008-11-21 11:14:36.871+00	\N	16079	\N	\N	\N	\N
2008-11-21 11:14:36.916+00	\N	16081	\N	\N	\N	\N
2008-11-21 11:14:36.969+00	\N	16082	\N	\N	\N	\N
2008-11-21 11:14:37.025+00	\N	16084	\N	\N	\N	\N
2008-11-21 11:14:37.069+00	\N	16085	\N	\N	\N	\N
2008-11-21 11:14:37.116+00	\N	16087	\N	\N	\N	\N
2008-11-21 11:14:37.162+00	\N	16088	\N	\N	\N	\N
2008-11-21 11:14:37.21+00	\N	16090	\N	\N	\N	\N
2008-11-21 11:14:37.256+00	\N	16091	\N	\N	\N	\N
2008-11-21 11:14:37.306+00	\N	16093	\N	\N	\N	\N
2008-11-21 11:14:37.355+00	\N	16095	\N	\N	\N	\N
2008-11-21 11:14:37.40+00	\N	16097	\N	\N	\N	\N
2008-11-21 11:14:37.439+00	\N	16098	\N	\N	\N	\N
2008-11-21 11:14:37.48+00	\N	16100	\N	\N	\N	\N
2008-11-21 11:14:37.519+00	\N	16101	\N	\N	\N	\N
2008-11-21 11:14:37.56+00	\N	16103	\N	\N	\N	\N
2008-11-21 11:14:37.598+00	\N	16104	\N	\N	\N	\N
2008-11-21 11:14:37.638+00	\N	16106	\N	\N	\N	\N
2008-11-21 11:14:37.675+00	\N	16107	\N	\N	\N	\N
2008-11-21 11:14:37.714+00	\N	16109	\N	\N	\N	\N
2008-11-21 11:14:37.752+00	\N	16110	\N	\N	\N	\N
2008-11-21 11:14:37.792+00	\N	16112	\N	\N	\N	\N
2008-11-21 11:14:37.828+00	\N	16113	\N	\N	\N	\N
2008-11-21 11:14:37.866+00	\N	16115	\N	\N	\N	\N
2008-11-21 11:14:37.901+00	\N	16116	\N	\N	\N	\N
2008-11-21 11:14:37.935+00	\N	16118	\N	\N	\N	\N
2008-11-21 11:14:37.964+00	\N	16119	\N	\N	\N	\N
2008-11-21 11:14:38.011+00	\N	16121	\N	\N	\N	\N
2008-11-21 11:14:38.04+00	\N	16122	\N	\N	\N	\N
2008-11-21 11:14:38.072+00	\N	16124	\N	\N	\N	\N
2008-11-21 11:14:38.101+00	\N	16125	\N	\N	\N	\N
2008-11-21 11:14:38.133+00	\N	16127	\N	\N	\N	\N
2008-11-21 11:14:38.163+00	\N	16128	\N	\N	\N	\N
2008-11-21 11:14:38.196+00	\N	16130	\N	\N	\N	\N
2008-11-21 11:14:38.225+00	\N	16131	\N	\N	\N	\N
2008-11-21 11:14:38.258+00	\N	16133	\N	\N	\N	\N
2008-11-21 11:14:38.288+00	\N	16134	\N	\N	\N	\N
2008-11-21 11:14:38.32+00	\N	16136	\N	\N	\N	\N
2008-11-21 11:14:38.348+00	\N	16137	\N	\N	\N	\N
2008-11-21 11:14:38.379+00	\N	16139	\N	\N	\N	\N
2008-11-21 11:14:38.408+00	\N	16140	\N	\N	\N	\N
2008-11-21 11:14:38.439+00	\N	16142	\N	\N	\N	\N
2008-11-21 11:14:38.468+00	\N	16143	\N	\N	\N	\N
2008-11-21 11:14:38.50+00	\N	16145	\N	\N	\N	\N
2008-11-21 11:14:38.529+00	\N	16146	\N	\N	\N	\N
2008-11-21 11:14:38.561+00	\N	16148	\N	\N	\N	\N
2008-11-21 11:14:38.591+00	\N	16149	\N	\N	\N	\N
2008-11-21 11:14:38.623+00	\N	16151	\N	\N	\N	\N
2008-11-21 11:14:38.653+00	\N	16152	\N	\N	\N	\N
2008-11-21 11:14:38.686+00	\N	16154	\N	\N	\N	\N
2008-11-21 11:14:38.717+00	\N	16155	\N	\N	\N	\N
2008-11-21 11:14:38.752+00	\N	16157	\N	\N	\N	\N
2008-11-21 11:14:38.784+00	\N	16158	\N	\N	\N	\N
2008-11-21 11:14:38.818+00	\N	16160	\N	\N	\N	\N
2008-11-21 11:14:38.85+00	\N	16161	\N	\N	\N	\N
2008-11-21 11:14:38.986+00	\N	16163	\N	\N	\N	\N
2008-11-21 11:14:39.016+00	\N	16164	\N	\N	\N	\N
2008-11-21 11:14:39.044+00	\N	16166	\N	\N	\N	\N
2008-11-21 11:14:39.069+00	\N	16167	\N	\N	\N	\N
2008-11-21 11:14:39.096+00	\N	16169	\N	\N	\N	\N
2008-11-21 11:14:39.12+00	\N	16170	\N	\N	\N	\N
2008-11-21 11:14:39.148+00	\N	16172	\N	\N	\N	\N
2008-11-21 11:14:39.172+00	\N	16173	\N	\N	\N	\N
2008-11-21 11:14:39.20+00	\N	16175	\N	\N	\N	\N
2008-11-21 11:14:39.223+00	\N	16176	\N	\N	\N	\N
2008-11-21 11:14:39.248+00	\N	16178	\N	\N	\N	\N
2008-11-21 11:14:39.272+00	\N	16179	\N	\N	\N	\N
2008-11-21 11:14:39.296+00	\N	16181	\N	\N	\N	\N
2008-11-21 11:14:39.317+00	\N	16182	\N	\N	\N	\N
2008-11-21 11:14:39.341+00	\N	16184	\N	\N	\N	\N
2008-11-21 11:14:39.363+00	\N	16185	\N	\N	\N	\N
2008-11-21 11:14:39.387+00	\N	16187	\N	\N	\N	\N
2008-11-21 11:14:39.409+00	\N	16188	\N	\N	\N	\N
2008-11-21 11:14:39.434+00	\N	16190	\N	\N	\N	\N
2008-11-21 11:14:39.457+00	\N	16191	\N	\N	\N	\N
2008-11-21 11:14:39.482+00	\N	16193	\N	\N	\N	\N
2008-11-21 11:14:39.504+00	\N	16194	\N	\N	\N	\N
2008-11-21 11:14:39.529+00	\N	16196	\N	\N	\N	\N
2008-11-21 11:14:39.552+00	\N	16197	\N	\N	\N	\N
2008-11-21 11:14:39.576+00	\N	16199	\N	\N	\N	\N
2008-11-21 11:14:39.601+00	\N	16201	\N	\N	\N	\N
2008-11-21 11:14:39.624+00	\N	16202	\N	\N	\N	\N
2008-11-21 11:14:39.65+00	\N	16204	\N	\N	\N	\N
2008-11-21 11:14:39.673+00	\N	16205	\N	\N	\N	\N
2008-11-21 11:14:39.698+00	\N	16207	\N	\N	\N	\N
2008-11-21 11:14:39.722+00	\N	16208	\N	\N	\N	\N
2008-11-21 11:14:39.855+00	\N	16210	\N	\N	\N	\N
2008-11-21 11:14:39.877+00	\N	16211	\N	\N	\N	\N
2008-11-21 11:14:39.90+00	\N	16213	\N	\N	\N	\N
2008-11-21 11:14:39.921+00	\N	16214	\N	\N	\N	\N
2008-11-21 11:14:39.943+00	\N	16216	\N	\N	\N	\N
2008-11-21 11:14:39.987+00	\N	16220	\N	\N	\N	\N
2008-11-21 11:14:40.008+00	\N	16222	\N	\N	\N	\N
2008-11-21 11:14:40.03+00	\N	16224	\N	\N	\N	\N
2008-11-21 11:14:40.053+00	\N	16226	\N	\N	\N	\N
2008-11-21 11:14:40.076+00	\N	16228	\N	\N	\N	\N
2008-11-21 11:14:40.099+00	\N	16230	\N	\N	\N	\N
2008-11-21 11:14:40.122+00	\N	16232	\N	\N	\N	\N
2008-11-21 11:14:40.17+00	\N	16236	\N	\N	\N	\N
2008-11-21 11:14:40.193+00	\N	16238	\N	\N	\N	\N
2008-11-21 11:14:40.217+00	\N	16240	\N	\N	\N	\N
2008-11-21 11:14:40.241+00	\N	16242	\N	\N	\N	\N
2008-11-21 11:14:40.265+00	\N	16244	\N	\N	\N	\N
2008-11-21 11:14:40.29+00	\N	16246	\N	\N	\N	\N
2008-11-21 11:14:40.312+00	\N	16247	\N	\N	\N	\N
2008-11-21 11:14:40.338+00	\N	16249	\N	\N	\N	\N
2008-11-21 11:14:40.361+00	\N	16250	\N	\N	\N	\N
2008-11-21 11:14:40.386+00	\N	16252	\N	\N	\N	\N
2008-11-21 11:14:40.409+00	\N	16253	\N	\N	\N	\N
2008-11-21 11:14:40.434+00	\N	16255	\N	\N	\N	\N
2008-11-21 11:15:11.706+00	2008-11-21 11:15:11.817+00	22001	\N	\N	\N	\N
2008-11-21 11:15:11.941+00	\N	22003	\N	\N	\N	\N
2008-11-21 11:15:11.953+00	\N	22005	\N	\N	\N	\N
2008-11-21 11:15:11.993+00	2008-11-21 11:15:12.008+00	22006	\N	\N	\N	\N
2008-11-21 11:15:12.068+00	\N	22008	\N	\N	\N	\N
2008-11-21 11:15:12.082+00	\N	22010	\N	\N	\N	\N
2008-11-21 11:15:12.118+00	2008-11-21 11:15:12.136+00	22011	\N	\N	\N	\N
2008-11-21 11:15:12.209+00	\N	22013	\N	\N	\N	\N
2008-11-21 11:15:12.224+00	\N	22015	\N	\N	\N	\N
2008-11-21 11:15:12.269+00	2008-11-21 11:15:12.285+00	22016	\N	\N	\N	\N
2008-11-21 11:15:12.375+00	\N	22018	\N	\N	\N	\N
2008-11-21 11:15:12.393+00	\N	22020	\N	\N	\N	\N
2008-11-21 11:15:12.456+00	2008-11-21 11:15:12.475+00	22021	\N	\N	\N	\N
2008-11-21 11:15:12.523+00	\N	22023	\N	\N	\N	\N
2008-11-21 11:15:12.541+00	\N	22025	\N	\N	\N	\N
2008-11-21 11:15:12.601+00	2008-11-21 11:15:12.632+00	22026	\N	\N	\N	\N
2008-11-21 11:15:12.749+00	\N	22028	\N	\N	\N	\N
2008-11-21 11:15:12.806+00	\N	22030	\N	\N	\N	\N
2008-11-21 11:15:12.904+00	2008-11-21 11:15:12.96+00	22031	\N	\N	\N	\N
2008-11-21 11:15:13.247+00	\N	22033	\N	\N	\N	\N
2008-11-21 11:15:13.267+00	\N	22035	\N	\N	\N	\N
2008-11-21 11:15:13.326+00	2008-11-21 11:15:13.345+00	22036	\N	\N	\N	\N
2008-11-21 11:15:13.431+00	\N	22038	\N	\N	\N	\N
2008-11-21 11:15:13.446+00	\N	22040	\N	\N	\N	\N
2008-11-21 11:15:13.487+00	2008-11-21 11:15:13.503+00	22041	\N	\N	\N	\N
2008-11-21 11:15:13.533+00	\N	22043	\N	\N	\N	\N
2008-11-21 11:15:13.547+00	\N	22045	\N	\N	\N	\N
2008-11-21 11:15:13.60+00	2008-11-21 11:15:13.617+00	22046	\N	\N	\N	\N
2008-11-21 11:15:13.661+00	\N	22048	\N	\N	\N	\N
2008-11-21 11:15:13.676+00	\N	22050	\N	\N	\N	\N
2008-11-21 11:15:13.72+00	2008-11-21 11:15:13.735+00	22051	\N	\N	\N	\N
2008-11-21 11:15:13.758+00	\N	22053	\N	\N	\N	\N
2008-11-21 11:15:13.773+00	\N	22055	\N	\N	\N	\N
2008-11-21 11:15:13.819+00	2008-11-21 11:15:13.837+00	22056	\N	\N	\N	\N
2008-11-21 11:15:13.86+00	\N	22058	\N	\N	\N	\N
2008-11-21 11:15:13.874+00	\N	22060	\N	\N	\N	\N
2008-11-21 11:15:13.921+00	2008-11-21 11:15:13.937+00	22061	\N	\N	\N	\N
2008-11-21 11:15:13.96+00	\N	22063	\N	\N	\N	\N
2008-11-21 11:15:13.976+00	\N	22065	\N	\N	\N	\N
2008-11-21 11:15:14.015+00	2008-11-21 11:15:14.028+00	22066	\N	\N	\N	\N
2008-11-21 11:15:14.046+00	\N	22068	\N	\N	\N	\N
2008-11-21 11:15:14.058+00	\N	22070	\N	\N	\N	\N
2008-11-21 11:15:14.095+00	2008-11-21 11:15:14.11+00	22071	\N	\N	\N	\N
2008-11-21 11:15:14.158+00	\N	22073	\N	\N	\N	\N
2008-11-21 11:15:14.172+00	\N	22075	\N	\N	\N	\N
2008-11-21 11:15:14.216+00	2008-11-21 11:15:14.228+00	22076	\N	\N	\N	\N
2008-11-21 11:15:14.281+00	\N	22078	\N	\N	\N	\N
2008-11-21 11:15:14.293+00	\N	22080	\N	\N	\N	\N
2008-11-21 11:15:14.327+00	2008-11-21 11:15:14.339+00	22081	\N	\N	\N	\N
2008-11-21 11:15:14.392+00	\N	22083	\N	\N	\N	\N
2008-11-21 11:15:14.405+00	\N	22085	\N	\N	\N	\N
2008-11-21 11:15:14.438+00	2008-11-21 11:15:14.452+00	22086	\N	\N	\N	\N
2008-11-21 11:15:14.502+00	\N	22088	\N	\N	\N	\N
2008-11-21 11:15:14.514+00	\N	22090	\N	\N	\N	\N
2008-11-21 11:15:14.559+00	2008-11-21 11:15:14.574+00	22091	\N	\N	\N	\N
2008-11-21 11:15:14.596+00	\N	22093	\N	\N	\N	\N
2008-11-21 11:15:14.607+00	\N	22095	\N	\N	\N	\N
2008-11-21 11:15:14.639+00	2008-11-21 11:15:14.651+00	22096	\N	\N	\N	\N
2008-11-21 11:15:14.803+00	\N	22098	\N	\N	\N	\N
2008-11-21 11:15:14.815+00	\N	22100	\N	\N	\N	\N
2008-11-21 11:15:14.852+00	2008-11-21 11:15:14.862+00	22101	\N	\N	\N	\N
2008-11-21 11:15:14.89+00	\N	22103	\N	\N	\N	\N
2008-11-21 11:15:14.901+00	\N	22105	\N	\N	\N	\N
2008-11-21 11:15:15.044+00	2008-11-21 11:15:15.054+00	22106	\N	\N	\N	\N
2008-11-21 11:15:15.092+00	\N	22108	\N	\N	\N	\N
2008-11-21 11:15:15.103+00	\N	22110	\N	\N	\N	\N
2008-11-21 11:15:15.137+00	2008-11-21 11:15:15.248+00	22111	\N	\N	\N	\N
2008-11-21 11:15:15.278+00	\N	22113	\N	\N	\N	\N
2008-11-21 11:15:15.288+00	\N	22115	\N	\N	\N	\N
2008-11-21 11:15:15.314+00	2008-11-21 11:15:15.324+00	22116	\N	\N	\N	\N
2008-11-21 11:15:15.365+00	\N	22118	\N	\N	\N	\N
2008-11-21 11:15:15.477+00	\N	22120	\N	\N	\N	\N
2008-11-21 11:15:15.501+00	2008-11-21 11:15:15.51+00	22121	\N	\N	\N	\N
2008-11-21 11:15:15.54+00	\N	22123	\N	\N	\N	\N
2008-11-21 11:15:15.549+00	\N	22125	\N	\N	\N	\N
2008-11-21 11:15:15.579+00	2008-11-21 11:15:15.588+00	22126	\N	\N	\N	\N
2008-11-21 11:15:15.619+00	\N	22128	\N	\N	\N	\N
2008-11-21 11:15:15.628+00	\N	22130	\N	\N	\N	\N
2008-11-21 11:15:15.653+00	2008-11-21 11:15:15.661+00	22131	\N	\N	\N	\N
2008-11-21 11:15:15.693+00	\N	22133	\N	\N	\N	\N
2008-11-21 11:15:15.703+00	\N	22135	\N	\N	\N	\N
2008-11-21 11:15:15.729+00	2008-11-21 11:15:15.738+00	22136	\N	\N	\N	\N
2008-11-21 11:15:15.771+00	\N	22138	\N	\N	\N	\N
2008-11-21 11:15:15.783+00	\N	22140	\N	\N	\N	\N
2008-11-21 11:15:15.819+00	2008-11-21 11:15:15.827+00	22141	\N	\N	\N	\N
2008-11-21 11:15:15.881+00	\N	22143	\N	\N	\N	\N
2008-11-21 11:15:15.892+00	\N	22145	\N	\N	\N	\N
2008-11-21 11:15:15.918+00	2008-11-21 11:15:15.927+00	22146	\N	\N	\N	\N
2008-11-21 11:15:15.955+00	\N	22148	\N	\N	\N	\N
2008-11-21 11:15:15.964+00	\N	22150	\N	\N	\N	\N
2008-11-21 11:15:15.991+00	2008-11-21 11:15:16.001+00	22151	\N	\N	\N	\N
2008-11-21 11:15:16.07+00	\N	22153	\N	\N	\N	\N
2008-11-21 11:15:16.081+00	\N	22155	\N	\N	\N	\N
2008-11-21 11:15:16.113+00	2008-11-21 11:15:16.123+00	22156	\N	\N	\N	\N
2008-11-21 11:15:16.158+00	\N	22158	\N	\N	\N	\N
2008-11-21 11:15:16.167+00	\N	22160	\N	\N	\N	\N
2008-11-21 11:15:16.20+00	2008-11-21 11:15:16.209+00	22161	\N	\N	\N	\N
2008-11-21 11:15:16.244+00	\N	22163	\N	\N	\N	\N
2008-11-21 11:15:16.254+00	\N	22165	\N	\N	\N	\N
2008-11-21 11:15:16.286+00	2008-11-21 11:15:16.296+00	22166	\N	\N	\N	\N
2008-11-21 11:15:16.33+00	\N	22168	\N	\N	\N	\N
2008-11-21 11:15:16.34+00	\N	22170	\N	\N	\N	\N
2008-11-21 11:15:16.367+00	2008-11-21 11:15:16.376+00	22171	\N	\N	\N	\N
2008-11-21 11:15:16.389+00	\N	22173	\N	\N	\N	\N
2008-11-21 11:15:16.398+00	\N	22175	\N	\N	\N	\N
2008-11-21 11:15:16.427+00	2008-11-21 11:15:16.436+00	22176	\N	\N	\N	\N
2008-11-21 11:15:16.472+00	\N	22178	\N	\N	\N	\N
2008-11-21 11:15:16.481+00	\N	22180	\N	\N	\N	\N
2008-11-21 11:15:16.509+00	2008-11-21 11:15:16.519+00	22181	\N	\N	\N	\N
2008-11-21 11:15:16.555+00	\N	22183	\N	\N	\N	\N
2008-11-21 11:15:16.564+00	\N	22185	\N	\N	\N	\N
2008-11-21 11:15:16.594+00	2008-11-21 11:15:16.606+00	22186	\N	\N	\N	\N
2008-11-21 11:15:16.662+00	\N	22188	\N	\N	\N	\N
2008-11-21 11:15:16.672+00	\N	22190	\N	\N	\N	\N
2008-11-21 11:15:16.754+00	2008-11-21 11:15:16.765+00	22191	\N	\N	\N	\N
2008-11-21 11:15:16.789+00	\N	22193	\N	\N	\N	\N
2008-11-21 11:15:16.798+00	\N	22195	\N	\N	\N	\N
2008-11-21 11:15:16.826+00	2008-11-21 11:15:16.835+00	22196	\N	\N	\N	\N
2008-11-21 11:15:16.913+00	\N	22198	\N	\N	\N	\N
2008-11-21 11:15:16.926+00	\N	22200	\N	\N	\N	\N
2008-11-21 11:15:16.954+00	2008-11-21 11:15:16.963+00	22201	\N	\N	\N	\N
2008-11-21 11:15:17.008+00	\N	22203	\N	\N	\N	\N
2008-11-21 11:15:17.019+00	\N	22205	\N	\N	\N	\N
2008-11-21 11:15:17.047+00	2008-11-21 11:15:17.056+00	22206	\N	\N	\N	\N
2008-11-21 11:15:17.07+00	\N	22208	\N	\N	\N	\N
2008-11-21 11:15:17.084+00	\N	22210	\N	\N	\N	\N
2008-11-21 11:15:17.113+00	2008-11-21 11:15:17.129+00	22211	\N	\N	\N	\N
2008-11-21 11:15:17.143+00	\N	22213	\N	\N	\N	\N
2008-11-21 11:15:17.152+00	\N	22215	\N	\N	\N	\N
2008-11-21 11:15:17.188+00	2008-11-21 11:15:17.197+00	22216	\N	\N	\N	\N
2008-11-21 11:15:17.212+00	\N	22218	\N	\N	\N	\N
2008-11-21 11:15:17.221+00	\N	22220	\N	\N	\N	\N
2008-11-21 11:15:17.488+00	2008-11-21 11:15:17.509+00	22221	\N	\N	\N	\N
2008-11-21 11:15:17.563+00	\N	22223	\N	\N	\N	\N
2008-11-21 11:15:17.573+00	\N	22225	\N	\N	\N	\N
2008-11-21 11:15:17.61+00	2008-11-21 11:15:17.621+00	22226	\N	\N	\N	\N
2008-11-21 11:15:17.642+00	\N	22228	\N	\N	\N	\N
2008-11-21 11:15:17.651+00	\N	22230	\N	\N	\N	\N
2008-11-21 11:15:17.682+00	2008-11-21 11:15:17.692+00	22231	\N	\N	\N	\N
2008-11-21 11:15:17.74+00	\N	22233	\N	\N	\N	\N
2008-11-21 11:15:17.764+00	\N	22235	\N	\N	\N	\N
2008-11-21 11:15:17.796+00	2008-11-21 11:15:17.806+00	22236	\N	\N	\N	\N
2008-11-21 11:15:17.867+00	\N	22238	\N	\N	\N	\N
2008-11-21 11:15:17.877+00	\N	22240	\N	\N	\N	\N
2008-11-21 11:15:17.911+00	2008-11-21 11:15:17.921+00	22241	\N	\N	\N	\N
2008-11-21 11:15:17.962+00	\N	22243	\N	\N	\N	\N
2008-11-21 11:15:17.972+00	\N	22245	\N	\N	\N	\N
2008-11-21 11:15:18.002+00	2008-11-21 11:15:18.011+00	22246	\N	\N	\N	\N
2008-11-21 11:15:18.058+00	\N	22248	\N	\N	\N	\N
2008-11-21 11:15:18.068+00	\N	22250	\N	\N	\N	\N
2008-11-21 11:15:18.10+00	2008-11-21 11:15:18.11+00	22251	\N	\N	\N	\N
2008-11-21 11:15:18.15+00	\N	22253	\N	\N	\N	\N
2008-11-21 11:15:18.16+00	\N	22255	\N	\N	\N	\N
2008-11-21 11:15:18.197+00	2008-11-21 11:15:18.207+00	22256	\N	\N	\N	\N
2008-11-21 11:15:18.256+00	\N	22258	\N	\N	\N	\N
2008-11-21 11:15:18.271+00	\N	22260	\N	\N	\N	\N
2008-11-21 11:15:18.303+00	2008-11-21 11:15:18.313+00	22261	\N	\N	\N	\N
2008-11-21 11:15:18.371+00	\N	22263	\N	\N	\N	\N
2008-11-21 11:15:18.381+00	\N	22265	\N	\N	\N	\N
2008-11-21 11:15:18.414+00	2008-11-21 11:15:18.424+00	22266	\N	\N	\N	\N
2008-11-21 11:15:18.48+00	\N	22268	\N	\N	\N	\N
2008-11-21 11:15:18.49+00	\N	22270	\N	\N	\N	\N
2008-11-21 11:15:18.526+00	2008-11-21 11:15:18.537+00	22271	\N	\N	\N	\N
2008-11-21 11:15:18.58+00	\N	22273	\N	\N	\N	\N
2008-11-21 11:15:18.59+00	\N	22275	\N	\N	\N	\N
2008-11-21 11:15:18.65+00	2008-11-21 11:15:18.663+00	22276	\N	\N	\N	\N
2008-11-21 11:15:18.714+00	\N	22278	\N	\N	\N	\N
2008-11-21 11:15:18.731+00	\N	22280	\N	\N	\N	\N
2008-11-21 11:15:18.765+00	2008-11-21 11:15:18.775+00	22281	\N	\N	\N	\N
2008-11-21 11:15:18.878+00	\N	22283	\N	\N	\N	\N
2008-11-21 11:15:18.889+00	\N	22285	\N	\N	\N	\N
2008-11-21 11:15:18.923+00	2008-11-21 11:15:18.934+00	22286	\N	\N	\N	\N
2008-11-21 11:14:39.965+00	2008-11-21 11:15:18.954+00	16218	\N	\N	\N	\N
2008-11-21 11:15:19.008+00	2008-11-21 11:15:19.053+00	22288	\N	\N	\N	\N
2008-11-21 11:15:19.07+00	\N	22290	\N	\N	\N	\N
2008-11-21 11:15:19.079+00	\N	22292	\N	\N	\N	\N
2008-11-21 11:15:19.111+00	2008-11-21 11:15:19.121+00	22293	\N	\N	\N	\N
2008-11-21 11:15:19.156+00	\N	22295	\N	\N	\N	\N
2008-11-21 11:15:19.166+00	\N	22297	\N	\N	\N	\N
2008-11-21 11:15:19.20+00	2008-11-21 11:15:19.214+00	22298	\N	\N	\N	\N
2008-11-21 11:15:19.262+00	\N	22300	\N	\N	\N	\N
2008-11-21 11:15:19.274+00	\N	22302	\N	\N	\N	\N
2008-11-21 11:15:19.312+00	2008-11-21 11:15:19.326+00	22303	\N	\N	\N	\N
2008-11-21 11:15:19.411+00	\N	22305	\N	\N	\N	\N
2008-11-21 11:15:19.424+00	\N	22307	\N	\N	\N	\N
2008-11-21 11:15:19.465+00	2008-11-21 11:15:19.647+00	22308	\N	\N	\N	\N
2008-11-21 11:15:19.668+00	\N	22310	\N	\N	\N	\N
2008-11-21 11:15:19.68+00	\N	22312	\N	\N	\N	\N
2008-11-21 11:15:19.733+00	2008-11-21 11:15:19.749+00	22313	\N	\N	\N	\N
2008-11-21 11:15:19.77+00	\N	22315	\N	\N	\N	\N
2008-11-21 11:15:19.789+00	\N	22317	\N	\N	\N	\N
2008-11-21 11:15:19.831+00	2008-11-21 11:15:19.847+00	22318	\N	\N	\N	\N
2008-11-21 11:15:19.918+00	\N	22320	\N	\N	\N	\N
2008-11-21 11:15:19.931+00	\N	22322	\N	\N	\N	\N
2008-11-21 11:15:19.973+00	2008-11-21 11:15:19.986+00	22323	\N	\N	\N	\N
2008-11-21 11:15:20.06+00	\N	22325	\N	\N	\N	\N
2008-11-21 11:15:20.077+00	\N	22327	\N	\N	\N	\N
2008-11-21 11:15:20.118+00	2008-11-21 11:15:20.13+00	22328	\N	\N	\N	\N
2008-11-21 11:15:20.168+00	\N	22330	\N	\N	\N	\N
2008-11-21 11:15:20.182+00	\N	22332	\N	\N	\N	\N
2008-11-21 11:15:20.263+00	2008-11-21 11:15:20.275+00	22333	\N	\N	\N	\N
2008-11-21 11:15:20.309+00	\N	22335	\N	\N	\N	\N
2008-11-21 11:15:20.319+00	\N	22337	\N	\N	\N	\N
2008-11-21 11:15:20.355+00	2008-11-21 11:15:20.374+00	22338	\N	\N	\N	\N
2008-11-21 11:15:20.459+00	\N	22340	\N	\N	\N	\N
2008-11-21 11:15:20.472+00	\N	22342	\N	\N	\N	\N
2008-11-21 11:15:20.511+00	2008-11-21 11:15:20.523+00	22343	\N	\N	\N	\N
2008-11-21 11:15:20.576+00	\N	22345	\N	\N	\N	\N
2008-11-21 11:15:20.589+00	\N	22347	\N	\N	\N	\N
2008-11-21 11:15:20.627+00	2008-11-21 11:15:20.639+00	22348	\N	\N	\N	\N
2008-11-21 11:15:20.675+00	\N	22350	\N	\N	\N	\N
2008-11-21 11:15:20.687+00	\N	22352	\N	\N	\N	\N
2008-11-21 11:15:20.726+00	2008-11-21 11:15:20.738+00	22353	\N	\N	\N	\N
2008-11-21 11:15:20.758+00	\N	22355	\N	\N	\N	\N
2008-11-21 11:15:20.77+00	\N	22357	\N	\N	\N	\N
2008-11-21 11:15:20.811+00	2008-11-21 11:15:20.823+00	22358	\N	\N	\N	\N
2008-11-21 11:15:20.894+00	\N	22360	\N	\N	\N	\N
2008-11-21 11:15:20.907+00	\N	22362	\N	\N	\N	\N
2008-11-21 11:15:20.948+00	2008-11-21 11:15:20.96+00	22363	\N	\N	\N	\N
2008-11-21 11:15:21.032+00	\N	22365	\N	\N	\N	\N
2008-11-21 11:15:21.047+00	\N	22367	\N	\N	\N	\N
2008-11-21 11:15:21.097+00	2008-11-21 11:15:21.111+00	22368	\N	\N	\N	\N
2008-11-21 11:15:21.133+00	\N	22370	\N	\N	\N	\N
2008-11-21 11:15:21.146+00	\N	22372	\N	\N	\N	\N
2008-11-21 11:15:21.198+00	2008-11-21 11:15:21.211+00	22373	\N	\N	\N	\N
2008-11-21 11:15:21.234+00	\N	22375	\N	\N	\N	\N
2008-11-21 11:15:21.251+00	\N	22377	\N	\N	\N	\N
2008-11-21 11:15:21.305+00	2008-11-21 11:15:21.319+00	22378	\N	\N	\N	\N
2008-11-21 11:15:21.378+00	\N	22380	\N	\N	\N	\N
2008-11-21 11:15:21.391+00	\N	22382	\N	\N	\N	\N
2008-11-21 11:15:21.435+00	2008-11-21 11:15:21.451+00	22383	\N	\N	\N	\N
2008-11-21 11:15:21.473+00	\N	22385	\N	\N	\N	\N
2008-11-21 11:15:21.487+00	\N	22387	\N	\N	\N	\N
2008-11-21 11:15:21.531+00	2008-11-21 11:15:21.549+00	22388	\N	\N	\N	\N
2008-11-21 11:15:21.637+00	\N	22390	\N	\N	\N	\N
2008-11-21 11:15:21.651+00	\N	22392	\N	\N	\N	\N
2008-11-21 11:15:21.705+00	2008-11-21 11:15:21.719+00	22393	\N	\N	\N	\N
2008-11-21 11:15:21.857+00	\N	22395	\N	\N	\N	\N
2008-11-21 11:15:21.872+00	\N	22397	\N	\N	\N	\N
2008-11-21 11:15:21.926+00	2008-11-21 11:15:21.941+00	22398	\N	\N	\N	\N
2008-11-21 11:15:22.034+00	\N	22400	\N	\N	\N	\N
2008-11-21 11:15:22.049+00	\N	22402	\N	\N	\N	\N
2008-11-21 11:15:22.096+00	2008-11-21 11:15:22.111+00	22403	\N	\N	\N	\N
2008-11-21 11:15:22.134+00	\N	22405	\N	\N	\N	\N
2008-11-21 11:15:22.147+00	\N	22407	\N	\N	\N	\N
2008-11-21 11:15:22.191+00	2008-11-21 11:15:22.205+00	22408	\N	\N	\N	\N
2008-11-21 11:15:22.314+00	\N	22410	\N	\N	\N	\N
2008-11-21 11:15:22.329+00	\N	22412	\N	\N	\N	\N
2008-11-21 11:15:22.383+00	2008-11-21 11:15:22.397+00	22413	\N	\N	\N	\N
2008-11-21 11:15:22.48+00	\N	22415	\N	\N	\N	\N
2008-11-21 11:15:22.494+00	\N	22417	\N	\N	\N	\N
2008-11-21 11:15:22.553+00	2008-11-21 11:15:22.583+00	22418	\N	\N	\N	\N
2008-11-21 11:15:22.606+00	\N	22420	\N	\N	\N	\N
2008-11-21 11:15:22.621+00	\N	22422	\N	\N	\N	\N
2008-11-21 11:15:22.667+00	2008-11-21 11:15:22.683+00	22423	\N	\N	\N	\N
2008-11-21 11:15:22.768+00	\N	22425	\N	\N	\N	\N
2008-11-21 11:15:22.783+00	\N	22427	\N	\N	\N	\N
2008-11-21 11:15:22.843+00	2008-11-21 11:15:22.874+00	22428	\N	\N	\N	\N
2008-11-21 11:15:22.994+00	\N	22430	\N	\N	\N	\N
2008-11-21 11:15:23.009+00	\N	22432	\N	\N	\N	\N
2008-11-21 11:15:23.083+00	2008-11-21 11:15:23.097+00	22433	\N	\N	\N	\N
2008-11-21 11:15:23.183+00	\N	22435	\N	\N	\N	\N
2008-11-21 11:15:23.196+00	\N	22437	\N	\N	\N	\N
2008-11-21 11:15:23.239+00	2008-11-21 11:15:23.252+00	22438	\N	\N	\N	\N
2008-11-21 11:15:23.40+00	\N	22440	\N	\N	\N	\N
2008-11-21 11:15:23.414+00	\N	22442	\N	\N	\N	\N
2008-11-21 11:15:23.46+00	2008-11-21 11:15:23.474+00	22443	\N	\N	\N	\N
2008-11-21 11:15:23.80+00	\N	22445	\N	\N	\N	\N
2008-11-21 11:15:23.816+00	\N	22447	\N	\N	\N	\N
2008-11-21 11:15:23.866+00	2008-11-21 11:15:23.881+00	22448	\N	\N	\N	\N
2008-11-21 11:15:24.018+00	\N	22450	\N	\N	\N	\N
2008-11-21 11:15:24.036+00	\N	22452	\N	\N	\N	\N
2008-11-21 11:15:24.087+00	2008-11-21 11:15:24.102+00	22453	\N	\N	\N	\N
2008-11-21 11:15:24.236+00	\N	22455	\N	\N	\N	\N
2008-11-21 11:15:24.252+00	\N	22457	\N	\N	\N	\N
2008-11-21 11:15:24.302+00	2008-11-21 11:15:24.318+00	22458	\N	\N	\N	\N
2008-11-21 11:15:24.454+00	\N	22460	\N	\N	\N	\N
2008-11-21 11:15:24.47+00	\N	22462	\N	\N	\N	\N
2008-11-21 11:15:24.529+00	2008-11-21 11:15:24.547+00	22463	\N	\N	\N	\N
2008-11-21 11:15:24.603+00	\N	22465	\N	\N	\N	\N
2008-11-21 11:15:24.618+00	\N	22467	\N	\N	\N	\N
2008-11-21 11:15:24.667+00	2008-11-21 11:15:24.682+00	22468	\N	\N	\N	\N
2008-11-21 11:15:24.76+00	\N	22470	\N	\N	\N	\N
2008-11-21 11:15:24.776+00	\N	22472	\N	\N	\N	\N
2008-11-21 11:15:24.825+00	2008-11-21 11:15:24.841+00	22473	\N	\N	\N	\N
2008-11-21 11:15:24.907+00	\N	22475	\N	\N	\N	\N
2008-11-21 11:15:24.923+00	\N	22477	\N	\N	\N	\N
2008-11-21 11:15:24.972+00	2008-11-21 11:15:24.987+00	22478	\N	\N	\N	\N
2008-11-21 11:15:25.05+00	\N	22480	\N	\N	\N	\N
2008-11-21 11:15:25.065+00	\N	22482	\N	\N	\N	\N
2008-11-21 11:15:25.115+00	2008-11-21 11:15:25.13+00	22483	\N	\N	\N	\N
2008-11-21 11:15:25.199+00	\N	22485	\N	\N	\N	\N
2008-11-21 11:15:25.215+00	\N	22487	\N	\N	\N	\N
2008-11-21 11:15:25.265+00	2008-11-21 11:15:25.281+00	22488	\N	\N	\N	\N
2008-11-21 11:15:25.329+00	\N	22490	\N	\N	\N	\N
2008-11-21 11:15:25.345+00	\N	22492	\N	\N	\N	\N
2008-11-21 11:15:25.395+00	2008-11-21 11:15:25.411+00	22493	\N	\N	\N	\N
2008-11-21 11:15:25.437+00	\N	22495	\N	\N	\N	\N
2008-11-21 11:15:25.452+00	\N	22497	\N	\N	\N	\N
2008-11-21 11:15:25.505+00	2008-11-21 11:15:25.522+00	22498	\N	\N	\N	\N
2008-11-21 11:15:25.598+00	\N	22500	\N	\N	\N	\N
2008-11-21 11:15:25.615+00	\N	22502	\N	\N	\N	\N
2008-11-21 11:15:25.67+00	2008-11-21 11:15:25.686+00	22503	\N	\N	\N	\N
2008-11-21 11:15:25.761+00	\N	22505	\N	\N	\N	\N
2008-11-21 11:15:25.779+00	\N	22507	\N	\N	\N	\N
2008-11-21 11:15:25.837+00	2008-11-21 11:15:25.856+00	22508	\N	\N	\N	\N
2008-11-21 11:15:25.947+00	\N	22510	\N	\N	\N	\N
2008-11-21 11:15:25.964+00	\N	22512	\N	\N	\N	\N
2008-11-21 11:15:26.019+00	2008-11-21 11:15:26.038+00	22513	\N	\N	\N	\N
2008-11-21 11:15:26.122+00	\N	22515	\N	\N	\N	\N
2008-11-21 11:15:26.141+00	\N	22517	\N	\N	\N	\N
2008-11-21 11:15:26.215+00	2008-11-21 11:15:26.234+00	22518	\N	\N	\N	\N
2008-11-21 11:15:26.279+00	\N	22520	\N	\N	\N	\N
2008-11-21 11:15:26.298+00	\N	22522	\N	\N	\N	\N
2008-11-21 11:15:26.359+00	2008-11-21 11:15:26.378+00	22523	\N	\N	\N	\N
2008-11-21 11:15:26.49+00	\N	22525	\N	\N	\N	\N
2008-11-21 11:15:26.508+00	\N	22527	\N	\N	\N	\N
2008-11-21 11:15:26.571+00	2008-11-21 11:15:26.588+00	22528	\N	\N	\N	\N
2008-11-21 11:15:26.682+00	\N	22530	\N	\N	\N	\N
2008-11-21 11:15:26.699+00	\N	22532	\N	\N	\N	\N
2008-11-21 11:15:26.755+00	2008-11-21 11:15:26.775+00	22533	\N	\N	\N	\N
2008-11-21 11:15:26.832+00	\N	22535	\N	\N	\N	\N
2008-11-21 11:15:26.848+00	\N	22537	\N	\N	\N	\N
2008-11-21 11:15:26.904+00	2008-11-21 11:15:26.92+00	22538	\N	\N	\N	\N
2008-11-21 11:15:27.05+00	\N	22540	\N	\N	\N	\N
2008-11-21 11:15:27.068+00	\N	22542	\N	\N	\N	\N
2008-11-21 11:15:27.126+00	2008-11-21 11:15:27.143+00	22543	\N	\N	\N	\N
2008-11-21 11:15:27.225+00	\N	22545	\N	\N	\N	\N
2008-11-21 11:15:27.245+00	\N	22547	\N	\N	\N	\N
2008-11-21 11:15:27.347+00	2008-11-21 11:15:27.367+00	22548	\N	\N	\N	\N
2008-11-21 11:15:27.474+00	\N	22550	\N	\N	\N	\N
2008-11-21 11:15:27.495+00	\N	22552	\N	\N	\N	\N
2008-11-21 11:15:27.553+00	2008-11-21 11:15:27.57+00	22553	\N	\N	\N	\N
2008-11-21 11:15:27.751+00	\N	22555	\N	\N	\N	\N
2008-11-21 11:15:27.785+00	\N	22557	\N	\N	\N	\N
2008-11-21 11:15:27.914+00	2008-11-21 11:15:27.965+00	22558	\N	\N	\N	\N
2008-11-21 11:15:28.098+00	\N	22560	\N	\N	\N	\N
2008-11-21 11:15:28.115+00	\N	22562	\N	\N	\N	\N
2008-11-21 11:15:28.172+00	2008-11-21 11:15:28.189+00	22563	\N	\N	\N	\N
2008-11-21 11:15:28.304+00	\N	22565	\N	\N	\N	\N
2008-11-21 11:15:28.321+00	\N	22567	\N	\N	\N	\N
2008-11-21 11:15:28.377+00	2008-11-21 11:15:28.395+00	22568	\N	\N	\N	\N
2008-11-21 11:15:28.474+00	\N	22570	\N	\N	\N	\N
2008-11-21 11:15:28.495+00	\N	22572	\N	\N	\N	\N
2008-11-21 11:15:28.552+00	2008-11-21 11:15:28.57+00	22573	\N	\N	\N	\N
2008-11-21 11:15:28.624+00	\N	22575	\N	\N	\N	\N
2008-11-21 11:15:28.641+00	\N	22577	\N	\N	\N	\N
2008-11-21 11:15:28.699+00	2008-11-21 11:15:28.719+00	22578	\N	\N	\N	\N
2008-11-21 11:15:28.774+00	\N	22580	\N	\N	\N	\N
2008-11-21 11:15:28.801+00	\N	22582	\N	\N	\N	\N
2008-11-21 11:15:28.866+00	2008-11-21 11:15:28.886+00	22583	\N	\N	\N	\N
2008-11-21 11:15:28.951+00	\N	22585	\N	\N	\N	\N
2008-11-21 11:15:28.971+00	\N	22587	\N	\N	\N	\N
2008-11-21 11:15:29.042+00	2008-11-21 11:15:29.063+00	22588	\N	\N	\N	\N
2008-11-21 11:15:29.174+00	\N	22590	\N	\N	\N	\N
2008-11-21 11:15:29.214+00	\N	22592	\N	\N	\N	\N
2008-11-21 11:15:29.344+00	2008-11-21 11:15:29.375+00	22593	\N	\N	\N	\N
2008-11-21 11:15:29.617+00	\N	22595	\N	\N	\N	\N
2008-11-21 11:15:29.638+00	\N	22597	\N	\N	\N	\N
2008-11-21 11:15:29.72+00	2008-11-21 11:15:29.744+00	22598	\N	\N	\N	\N
2008-11-21 11:15:29.93+00	\N	22600	\N	\N	\N	\N
2008-11-21 11:15:29.952+00	\N	22602	\N	\N	\N	\N
2008-11-21 11:15:30.026+00	2008-11-21 11:15:30.045+00	22603	\N	\N	\N	\N
2008-11-21 11:15:30.106+00	\N	22605	\N	\N	\N	\N
2008-11-21 11:15:30.125+00	\N	22607	\N	\N	\N	\N
2008-11-21 11:15:30.195+00	2008-11-21 11:15:30.217+00	22608	\N	\N	\N	\N
2008-11-21 11:15:30.373+00	\N	22610	\N	\N	\N	\N
2008-11-21 11:15:30.397+00	\N	22612	\N	\N	\N	\N
2008-11-21 11:15:30.484+00	2008-11-21 11:15:30.508+00	22613	\N	\N	\N	\N
2008-11-21 11:15:30.665+00	\N	22615	\N	\N	\N	\N
2008-11-21 11:15:30.682+00	\N	22617	\N	\N	\N	\N
2008-11-21 11:15:30.739+00	2008-11-21 11:15:30.756+00	22618	\N	\N	\N	\N
2008-11-21 11:15:30.867+00	\N	22620	\N	\N	\N	\N
2008-11-21 11:15:30.885+00	\N	22622	\N	\N	\N	\N
2008-11-21 11:15:30.945+00	2008-11-21 11:15:30.963+00	22623	\N	\N	\N	\N
2008-11-21 11:15:31.101+00	\N	22625	\N	\N	\N	\N
2008-11-21 11:15:31.119+00	\N	22627	\N	\N	\N	\N
2008-11-21 11:15:31.179+00	2008-11-21 11:15:31.197+00	22628	\N	\N	\N	\N
2008-11-21 11:15:31.244+00	\N	22630	\N	\N	\N	\N
2008-11-21 11:15:31.263+00	\N	22632	\N	\N	\N	\N
2008-11-21 11:15:31.343+00	2008-11-21 11:15:31.366+00	22633	\N	\N	\N	\N
2008-11-21 11:15:31.40+00	\N	22635	\N	\N	\N	\N
2008-11-21 11:15:31.419+00	\N	22637	\N	\N	\N	\N
2008-11-21 11:15:31.483+00	2008-11-21 11:15:31.502+00	22638	\N	\N	\N	\N
2008-11-21 11:15:31.65+00	\N	22640	\N	\N	\N	\N
2008-11-21 11:15:31.67+00	\N	22642	\N	\N	\N	\N
2008-11-21 11:15:31.751+00	2008-11-21 11:15:31.77+00	22643	\N	\N	\N	\N
2008-11-21 11:15:31.971+00	\N	22645	\N	\N	\N	\N
2008-11-21 11:15:31.991+00	\N	22647	\N	\N	\N	\N
2008-11-21 11:15:32.072+00	2008-11-21 11:15:32.091+00	22648	\N	\N	\N	\N
2008-11-21 11:15:32.209+00	\N	22650	\N	\N	\N	\N
2008-11-21 11:15:32.23+00	\N	22652	\N	\N	\N	\N
2008-11-21 11:15:32.295+00	2008-11-21 11:15:32.315+00	22653	\N	\N	\N	\N
2008-11-21 11:15:32.349+00	\N	22655	\N	\N	\N	\N
2008-11-21 11:15:32.372+00	\N	22657	\N	\N	\N	\N
2008-11-21 11:15:32.438+00	2008-11-21 11:15:32.459+00	22658	\N	\N	\N	\N
2008-11-21 11:15:32.554+00	\N	22660	\N	\N	\N	\N
2008-11-21 11:15:32.575+00	\N	22662	\N	\N	\N	\N
2008-11-21 11:15:32.642+00	2008-11-21 11:15:32.662+00	22663	\N	\N	\N	\N
2008-11-21 11:15:32.803+00	\N	22665	\N	\N	\N	\N
2008-11-21 11:15:32.824+00	\N	22667	\N	\N	\N	\N
2008-11-21 11:15:32.89+00	2008-11-21 11:15:32.91+00	22668	\N	\N	\N	\N
2008-11-21 11:15:32.959+00	\N	22670	\N	\N	\N	\N
2008-11-21 11:15:32.979+00	\N	22672	\N	\N	\N	\N
2008-11-21 11:15:33.048+00	2008-11-21 11:15:33.069+00	22673	\N	\N	\N	\N
2008-11-21 11:15:33.221+00	\N	22675	\N	\N	\N	\N
2008-11-21 11:15:33.242+00	\N	22677	\N	\N	\N	\N
2008-11-21 11:15:33.31+00	2008-11-21 11:15:33.33+00	22678	\N	\N	\N	\N
2008-11-21 11:15:33.455+00	\N	22680	\N	\N	\N	\N
2008-11-21 11:15:33.477+00	\N	22682	\N	\N	\N	\N
2008-11-21 11:15:33.548+00	2008-11-21 11:15:33.569+00	22683	\N	\N	\N	\N
2008-11-21 11:15:33.683+00	\N	22685	\N	\N	\N	\N
2008-11-21 11:15:33.732+00	\N	22687	\N	\N	\N	\N
2008-11-21 11:15:33.852+00	2008-11-21 11:15:33.889+00	22688	\N	\N	\N	\N
2008-11-21 11:15:34.005+00	\N	22690	\N	\N	\N	\N
2008-11-21 11:15:34.025+00	\N	22692	\N	\N	\N	\N
2008-11-21 11:15:34.104+00	2008-11-21 11:15:34.123+00	22693	\N	\N	\N	\N
2008-11-21 11:15:34.273+00	\N	22695	\N	\N	\N	\N
2008-11-21 11:15:34.296+00	\N	22697	\N	\N	\N	\N
2008-11-21 11:15:34.387+00	2008-11-21 11:15:34.409+00	22698	\N	\N	\N	\N
2008-11-21 11:15:34.525+00	\N	22700	\N	\N	\N	\N
2008-11-21 11:15:34.548+00	\N	22702	\N	\N	\N	\N
2008-11-21 11:15:34.625+00	2008-11-21 11:15:34.647+00	22703	\N	\N	\N	\N
2008-11-21 11:15:34.769+00	\N	22705	\N	\N	\N	\N
2008-11-21 11:15:34.792+00	\N	22707	\N	\N	\N	\N
2008-11-21 11:15:34.869+00	2008-11-21 11:15:34.894+00	22708	\N	\N	\N	\N
2008-11-21 11:15:34.947+00	\N	22710	\N	\N	\N	\N
2008-11-21 11:15:34.968+00	\N	22712	\N	\N	\N	\N
2008-11-21 11:15:35.045+00	2008-11-21 11:15:35.066+00	22713	\N	\N	\N	\N
2008-11-21 11:15:35.199+00	\N	22715	\N	\N	\N	\N
2008-11-21 11:15:35.225+00	\N	22717	\N	\N	\N	\N
2008-11-21 11:15:35.302+00	2008-11-21 11:15:35.324+00	22718	\N	\N	\N	\N
2008-11-21 11:15:35.443+00	\N	22720	\N	\N	\N	\N
2008-11-21 11:15:35.466+00	\N	22722	\N	\N	\N	\N
2008-11-21 11:15:35.543+00	2008-11-21 11:15:35.574+00	22723	\N	\N	\N	\N
2008-11-21 11:15:35.764+00	\N	22725	\N	\N	\N	\N
2008-11-21 11:15:35.787+00	\N	22727	\N	\N	\N	\N
2008-11-21 11:15:35.885+00	2008-11-21 11:15:35.908+00	22728	\N	\N	\N	\N
2008-11-21 11:15:35.946+00	\N	22730	\N	\N	\N	\N
2008-11-21 11:15:35.968+00	\N	22732	\N	\N	\N	\N
2008-11-21 11:15:36.042+00	2008-11-21 11:15:36.064+00	22733	\N	\N	\N	\N
2008-11-21 11:15:36.102+00	\N	22735	\N	\N	\N	\N
2008-11-21 11:15:36.124+00	\N	22737	\N	\N	\N	\N
2008-11-21 11:15:36.21+00	2008-11-21 11:15:36.246+00	22738	\N	\N	\N	\N
2008-11-21 11:15:36.417+00	\N	22740	\N	\N	\N	\N
2008-11-21 11:15:36.454+00	\N	22742	\N	\N	\N	\N
2008-11-21 11:15:36.579+00	2008-11-21 11:15:36.604+00	22743	\N	\N	\N	\N
2008-11-21 11:15:36.722+00	\N	22745	\N	\N	\N	\N
2008-11-21 11:15:36.745+00	\N	22747	\N	\N	\N	\N
2008-11-21 11:15:36.823+00	2008-11-21 11:15:36.846+00	22748	\N	\N	\N	\N
2008-11-21 11:15:36.949+00	\N	22750	\N	\N	\N	\N
2008-11-21 11:15:36.971+00	\N	22752	\N	\N	\N	\N
2008-11-21 11:15:37.051+00	2008-11-21 11:15:37.074+00	22753	\N	\N	\N	\N
2008-11-21 11:15:37.176+00	\N	22755	\N	\N	\N	\N
2008-11-21 11:15:37.199+00	\N	22757	\N	\N	\N	\N
2008-11-21 11:15:37.277+00	2008-11-21 11:15:37.299+00	22758	\N	\N	\N	\N
2008-11-21 11:15:37.451+00	\N	22760	\N	\N	\N	\N
2008-11-21 11:15:37.474+00	\N	22762	\N	\N	\N	\N
2008-11-21 11:15:37.554+00	2008-11-21 11:15:37.577+00	22763	\N	\N	\N	\N
2008-11-21 11:15:37.815+00	\N	22765	\N	\N	\N	\N
2008-11-21 11:15:37.841+00	\N	22767	\N	\N	\N	\N
2008-11-21 11:15:37.92+00	2008-11-21 11:15:37.949+00	22768	\N	\N	\N	\N
2008-11-21 11:15:38.072+00	\N	22770	\N	\N	\N	\N
2008-11-21 11:15:38.095+00	\N	22772	\N	\N	\N	\N
2008-11-21 11:15:38.191+00	2008-11-21 11:15:38.214+00	22773	\N	\N	\N	\N
2008-11-21 11:15:38.369+00	\N	22775	\N	\N	\N	\N
2008-11-21 11:15:38.393+00	\N	22777	\N	\N	\N	\N
2008-11-21 11:15:38.473+00	2008-11-21 11:15:38.496+00	22778	\N	\N	\N	\N
2008-11-21 11:15:38.605+00	\N	22780	\N	\N	\N	\N
2008-11-21 11:15:38.628+00	\N	22782	\N	\N	\N	\N
2008-11-21 11:15:38.724+00	2008-11-21 11:15:38.747+00	22783	\N	\N	\N	\N
2008-11-21 11:15:38.804+00	\N	22785	\N	\N	\N	\N
2008-11-21 11:15:38.827+00	\N	22787	\N	\N	\N	\N
2008-11-21 11:15:38.909+00	2008-11-21 11:15:38.935+00	22788	\N	\N	\N	\N
2008-11-21 11:15:39.046+00	\N	22790	\N	\N	\N	\N
2008-11-21 11:15:39.07+00	\N	22792	\N	\N	\N	\N
2008-11-21 11:15:39.152+00	2008-11-21 11:15:39.176+00	22793	\N	\N	\N	\N
2008-11-21 11:15:39.285+00	\N	22795	\N	\N	\N	\N
2008-11-21 11:15:39.309+00	\N	22797	\N	\N	\N	\N
2008-11-21 11:15:39.391+00	2008-11-21 11:15:39.414+00	22798	\N	\N	\N	\N
2008-11-21 11:15:39.523+00	\N	22800	\N	\N	\N	\N
2008-11-21 11:15:39.549+00	\N	22802	\N	\N	\N	\N
2008-11-21 11:15:39.627+00	2008-11-21 11:15:39.65+00	22803	\N	\N	\N	\N
2008-11-21 11:15:39.78+00	\N	22805	\N	\N	\N	\N
2008-11-21 11:15:39.803+00	\N	22807	\N	\N	\N	\N
2008-11-21 11:15:39.918+00	2008-11-21 11:15:39.943+00	22808	\N	\N	\N	\N
2008-11-21 11:15:40.074+00	\N	22810	\N	\N	\N	\N
2008-11-21 11:15:40.097+00	\N	22812	\N	\N	\N	\N
2008-11-21 11:15:40.176+00	2008-11-21 11:15:40.20+00	22813	\N	\N	\N	\N
2008-11-21 11:15:40.241+00	\N	22815	\N	\N	\N	\N
2008-11-21 11:15:40.265+00	\N	22817	\N	\N	\N	\N
2008-11-21 11:15:40.345+00	2008-11-21 11:15:40.369+00	22818	\N	\N	\N	\N
2008-11-21 11:15:40.41+00	\N	22820	\N	\N	\N	\N
2008-11-21 11:15:40.434+00	\N	22822	\N	\N	\N	\N
2008-11-21 11:15:40.515+00	2008-11-21 11:15:40.542+00	22823	\N	\N	\N	\N
2008-11-21 11:15:40.584+00	\N	22825	\N	\N	\N	\N
2008-11-21 11:15:40.607+00	\N	22827	\N	\N	\N	\N
2008-11-21 11:15:40.688+00	2008-11-21 11:15:40.712+00	22828	\N	\N	\N	\N
2008-11-21 11:15:40.931+00	\N	22830	\N	\N	\N	\N
2008-11-21 11:15:40.956+00	\N	22832	\N	\N	\N	\N
2008-11-21 11:15:41.056+00	2008-11-21 11:15:41.08+00	22833	\N	\N	\N	\N
2008-11-21 11:15:41.211+00	\N	22835	\N	\N	\N	\N
2008-11-21 11:15:41.235+00	\N	22837	\N	\N	\N	\N
2008-11-21 11:15:41.316+00	2008-11-21 11:15:41.34+00	22838	\N	\N	\N	\N
2008-11-21 11:15:41.399+00	\N	22840	\N	\N	\N	\N
2008-11-21 11:15:41.424+00	\N	22842	\N	\N	\N	\N
2008-11-21 11:15:41.506+00	2008-11-21 11:15:41.532+00	22843	\N	\N	\N	\N
2008-11-21 11:15:41.649+00	\N	22845	\N	\N	\N	\N
2008-11-21 11:15:41.674+00	\N	22847	\N	\N	\N	\N
2008-11-21 11:15:41.756+00	2008-11-21 11:15:41.781+00	22848	\N	\N	\N	\N
2008-11-21 11:15:41.824+00	\N	22850	\N	\N	\N	\N
2008-11-21 11:15:41.848+00	\N	22852	\N	\N	\N	\N
2008-11-21 11:15:41.93+00	2008-11-21 11:15:41.956+00	22853	\N	\N	\N	\N
2008-11-21 11:15:41.998+00	\N	22855	\N	\N	\N	\N
2008-11-21 11:15:42.022+00	\N	22857	\N	\N	\N	\N
2008-11-21 11:15:42.127+00	2008-11-21 11:15:42.154+00	22858	\N	\N	\N	\N
2008-11-21 11:15:42.198+00	\N	22860	\N	\N	\N	\N
2008-11-21 11:15:42.222+00	\N	22862	\N	\N	\N	\N
2008-11-21 11:15:42.342+00	2008-11-21 11:15:42.367+00	22863	\N	\N	\N	\N
2008-11-21 11:14:40.144+00	2008-11-21 11:15:42.505+00	16234	\N	\N	\N	\N
2008-11-21 11:15:42.575+00	2008-11-21 11:15:42.601+00	22865	\N	\N	\N	\N
2008-11-21 11:15:42.645+00	\N	22867	\N	\N	\N	\N
2008-11-21 11:15:42.67+00	\N	22869	\N	\N	\N	\N
2008-11-21 11:15:42.756+00	2008-11-21 11:15:42.782+00	22870	\N	\N	\N	\N
2008-11-21 11:15:43.039+00	\N	22872	\N	\N	\N	\N
2008-11-21 11:15:43.066+00	\N	22874	\N	\N	\N	\N
2008-11-21 11:15:43.171+00	2008-11-21 11:15:43.197+00	22875	\N	\N	\N	\N
2008-11-21 11:15:43.308+00	\N	22877	\N	\N	\N	\N
2008-11-21 11:15:43.333+00	\N	22879	\N	\N	\N	\N
2008-11-21 11:15:43.437+00	2008-11-21 11:15:43.463+00	22880	\N	\N	\N	\N
2008-11-21 11:15:43.509+00	\N	22882	\N	\N	\N	\N
2008-11-21 11:15:43.536+00	\N	22884	\N	\N	\N	\N
2008-11-21 11:15:43.622+00	2008-11-21 11:15:43.648+00	22885	\N	\N	\N	\N
2008-11-21 11:15:43.953+00	\N	22887	\N	\N	\N	\N
2008-11-21 11:15:43.987+00	\N	22889	\N	\N	\N	\N
2008-11-21 11:15:44.065+00	2008-11-21 11:15:44.088+00	22890	\N	\N	\N	\N
2008-11-21 11:15:44.128+00	\N	22892	\N	\N	\N	\N
2008-11-21 11:15:44.151+00	\N	22894	\N	\N	\N	\N
2008-11-21 11:15:44.23+00	2008-11-21 11:15:44.253+00	22895	\N	\N	\N	\N
2008-11-21 11:15:44.293+00	\N	22897	\N	\N	\N	\N
2008-11-21 11:15:44.317+00	\N	22899	\N	\N	\N	\N
2008-11-21 11:15:44.427+00	2008-11-21 11:15:44.454+00	22900	\N	\N	\N	\N
2008-11-21 11:15:44.54+00	\N	22902	\N	\N	\N	\N
2008-11-21 11:15:44.566+00	\N	22904	\N	\N	\N	\N
2008-11-21 11:15:44.678+00	2008-11-21 11:15:44.705+00	22905	\N	\N	\N	\N
2008-11-21 11:15:44.771+00	\N	22907	\N	\N	\N	\N
2008-11-21 11:15:44.798+00	\N	22909	\N	\N	\N	\N
2008-11-21 11:15:44.904+00	2008-11-21 11:15:44.932+00	22910	\N	\N	\N	\N
2008-11-21 11:15:45.097+00	\N	22912	\N	\N	\N	\N
2008-11-21 11:15:45.124+00	\N	22914	\N	\N	\N	\N
2008-11-21 11:15:45.217+00	2008-11-21 11:15:45.243+00	22915	\N	\N	\N	\N
2008-11-21 11:15:45.467+00	\N	22917	\N	\N	\N	\N
2008-11-21 11:15:45.496+00	\N	22919	\N	\N	\N	\N
2008-11-21 11:15:45.589+00	2008-11-21 11:15:45.616+00	22920	\N	\N	\N	\N
2008-11-21 11:15:45.94+00	\N	22922	\N	\N	\N	\N
2008-11-21 11:15:45.97+00	\N	22924	\N	\N	\N	\N
2008-11-21 11:15:46.066+00	2008-11-21 11:15:46.093+00	22925	\N	\N	\N	\N
2008-11-21 11:15:46.141+00	\N	22927	\N	\N	\N	\N
2008-11-21 11:15:46.167+00	\N	22929	\N	\N	\N	\N
2008-11-21 11:15:46.261+00	2008-11-21 11:15:46.288+00	22930	\N	\N	\N	\N
2008-11-21 11:15:46.334+00	\N	22932	\N	\N	\N	\N
2008-11-21 11:15:46.36+00	\N	22934	\N	\N	\N	\N
2008-11-21 11:15:46.473+00	2008-11-21 11:15:46.501+00	22935	\N	\N	\N	\N
2008-11-21 11:15:46.549+00	\N	22937	\N	\N	\N	\N
2008-11-21 11:15:46.575+00	\N	22939	\N	\N	\N	\N
2008-11-21 11:15:46.69+00	2008-11-21 11:15:46.717+00	22940	\N	\N	\N	\N
2008-11-21 11:15:46.765+00	\N	22942	\N	\N	\N	\N
2008-11-21 11:15:46.808+00	\N	22944	\N	\N	\N	\N
2008-11-21 11:15:46.97+00	2008-11-21 11:15:47.01+00	22945	\N	\N	\N	\N
2008-11-21 11:15:47.209+00	\N	22947	\N	\N	\N	\N
2008-11-21 11:15:47.235+00	\N	22949	\N	\N	\N	\N
2008-11-21 11:15:47.321+00	2008-11-21 11:15:47.348+00	22950	\N	\N	\N	\N
2008-11-21 11:15:47.499+00	\N	22952	\N	\N	\N	\N
2008-11-21 11:15:47.528+00	\N	22954	\N	\N	\N	\N
2008-11-21 11:15:47.622+00	2008-11-21 11:15:47.649+00	22955	\N	\N	\N	\N
2008-11-21 11:15:47.801+00	\N	22957	\N	\N	\N	\N
2008-11-21 11:15:47.829+00	\N	22959	\N	\N	\N	\N
2008-11-21 11:15:47.921+00	2008-11-21 11:15:47.948+00	22960	\N	\N	\N	\N
2008-11-21 11:15:48.12+00	\N	22962	\N	\N	\N	\N
2008-11-21 11:15:48.147+00	\N	22964	\N	\N	\N	\N
2008-11-21 11:15:48.259+00	2008-11-21 11:15:48.286+00	22965	\N	\N	\N	\N
2008-11-21 11:15:48.34+00	\N	22967	\N	\N	\N	\N
2008-11-21 11:15:48.366+00	\N	22969	\N	\N	\N	\N
2008-11-21 11:15:48.479+00	2008-11-21 11:15:48.506+00	22970	\N	\N	\N	\N
2008-11-21 11:15:48.557+00	\N	22972	\N	\N	\N	\N
2008-11-21 11:15:48.585+00	\N	22974	\N	\N	\N	\N
2008-11-21 11:15:48.679+00	2008-11-21 11:15:48.707+00	22975	\N	\N	\N	\N
2008-11-21 11:15:48.839+00	\N	22977	\N	\N	\N	\N
2008-11-21 11:15:48.867+00	\N	22979	\N	\N	\N	\N
2008-11-21 11:15:48.961+00	2008-11-21 11:15:48.989+00	22980	\N	\N	\N	\N
2008-11-21 11:15:49.04+00	\N	22982	\N	\N	\N	\N
2008-11-21 11:15:49.068+00	\N	22984	\N	\N	\N	\N
2008-11-21 11:15:49.163+00	2008-11-21 11:15:49.191+00	22985	\N	\N	\N	\N
2008-11-21 11:15:49.24+00	\N	22987	\N	\N	\N	\N
2008-11-21 11:15:49.268+00	\N	22989	\N	\N	\N	\N
2008-11-21 11:15:49.384+00	2008-11-21 11:15:49.412+00	22990	\N	\N	\N	\N
2008-11-21 11:15:49.46+00	\N	22992	\N	\N	\N	\N
2008-11-21 11:15:49.488+00	\N	22994	\N	\N	\N	\N
2008-11-21 11:15:49.607+00	2008-11-21 11:15:49.646+00	22995	\N	\N	\N	\N
2008-11-21 11:15:50.051+00	\N	22997	\N	\N	\N	\N
2008-11-21 11:15:50.08+00	\N	22999	\N	\N	\N	\N
2008-11-21 11:15:50.18+00	2008-11-21 11:15:50.21+00	23000	\N	\N	\N	\N
2008-11-21 11:15:50.316+00	\N	23002	\N	\N	\N	\N
2008-11-21 11:15:50.345+00	\N	23004	\N	\N	\N	\N
2008-11-21 11:15:50.444+00	2008-11-21 11:15:50.473+00	23005	\N	\N	\N	\N
2008-11-21 11:15:50.566+00	\N	23007	\N	\N	\N	\N
2008-11-21 11:15:50.594+00	\N	23009	\N	\N	\N	\N
2008-11-21 11:15:50.694+00	2008-11-21 11:15:50.722+00	23010	\N	\N	\N	\N
2008-11-21 11:15:50.772+00	\N	23012	\N	\N	\N	\N
2008-11-21 11:15:50.799+00	\N	23014	\N	\N	\N	\N
2008-11-21 11:15:50.995+00	2008-11-21 11:15:51.044+00	23015	\N	\N	\N	\N
2008-11-21 11:15:51.093+00	\N	23017	\N	\N	\N	\N
2008-11-21 11:15:51.121+00	\N	23019	\N	\N	\N	\N
2008-11-21 11:15:51.237+00	2008-11-21 11:15:51.267+00	23020	\N	\N	\N	\N
2008-11-21 11:15:51.403+00	\N	23022	\N	\N	\N	\N
2008-11-21 11:15:51.432+00	\N	23024	\N	\N	\N	\N
2008-11-21 11:15:51.53+00	2008-11-21 11:15:51.562+00	23025	\N	\N	\N	\N
2008-11-21 11:15:51.635+00	\N	23027	\N	\N	\N	\N
2008-11-21 11:15:51.664+00	\N	23029	\N	\N	\N	\N
2008-11-21 11:15:51.762+00	2008-11-21 11:15:51.792+00	23030	\N	\N	\N	\N
2008-11-21 11:15:51.888+00	\N	23032	\N	\N	\N	\N
2008-11-21 11:15:51.917+00	\N	23034	\N	\N	\N	\N
2008-11-21 11:15:52.016+00	2008-11-21 11:15:52.049+00	23035	\N	\N	\N	\N
2008-11-21 11:15:52.238+00	\N	23037	\N	\N	\N	\N
2008-11-21 11:15:52.268+00	\N	23039	\N	\N	\N	\N
2008-11-21 11:15:52.369+00	2008-11-21 11:15:52.399+00	23040	\N	\N	\N	\N
2008-11-21 11:15:52.545+00	\N	23042	\N	\N	\N	\N
2008-11-21 11:15:52.575+00	\N	23044	\N	\N	\N	\N
2008-11-21 11:15:52.676+00	2008-11-21 11:15:52.707+00	23045	\N	\N	\N	\N
2008-11-21 11:15:52.851+00	\N	23047	\N	\N	\N	\N
2008-11-21 11:15:52.881+00	\N	23049	\N	\N	\N	\N
2008-11-21 11:15:52.982+00	2008-11-21 11:15:53.012+00	23050	\N	\N	\N	\N
2008-11-21 11:15:53.158+00	\N	23052	\N	\N	\N	\N
2008-11-21 11:15:53.189+00	\N	23054	\N	\N	\N	\N
2008-11-21 11:15:53.293+00	2008-11-21 11:15:53.324+00	23055	\N	\N	\N	\N
2008-11-21 11:15:53.449+00	\N	23057	\N	\N	\N	\N
2008-11-21 11:15:53.492+00	\N	23059	\N	\N	\N	\N
2008-11-21 11:15:53.594+00	2008-11-21 11:15:53.624+00	23060	\N	\N	\N	\N
2008-11-21 11:15:53.676+00	\N	23062	\N	\N	\N	\N
2008-11-21 11:15:53.706+00	\N	23064	\N	\N	\N	\N
2008-11-21 11:15:53.807+00	2008-11-21 11:15:53.837+00	23065	\N	\N	\N	\N
2008-11-21 11:15:53.96+00	\N	23067	\N	\N	\N	\N
2008-11-21 11:15:53.991+00	\N	23069	\N	\N	\N	\N
2008-11-21 11:15:54.143+00	2008-11-21 11:15:54.174+00	23070	\N	\N	\N	\N
2008-11-21 11:15:54.229+00	\N	23072	\N	\N	\N	\N
2008-11-21 11:15:54.259+00	\N	23074	\N	\N	\N	\N
2008-11-21 11:15:54.363+00	2008-11-21 11:15:54.394+00	23075	\N	\N	\N	\N
2008-11-21 11:15:54.472+00	\N	23077	\N	\N	\N	\N
2008-11-21 11:15:54.502+00	\N	23079	\N	\N	\N	\N
2008-11-21 11:15:54.629+00	2008-11-21 11:15:54.66+00	23080	\N	\N	\N	\N
2008-11-21 11:15:54.782+00	\N	23082	\N	\N	\N	\N
2008-11-21 11:15:54.812+00	\N	23084	\N	\N	\N	\N
2008-11-21 11:15:54.938+00	2008-11-21 11:15:54.968+00	23085	\N	\N	\N	\N
2008-11-21 11:15:55.141+00	\N	23087	\N	\N	\N	\N
2008-11-21 11:15:55.173+00	\N	23089	\N	\N	\N	\N
2008-11-21 11:15:55.301+00	2008-11-21 11:15:55.332+00	23090	\N	\N	\N	\N
2008-11-21 11:15:55.433+00	\N	23092	\N	\N	\N	\N
2008-11-21 11:15:55.464+00	\N	23094	\N	\N	\N	\N
2008-11-21 11:15:55.571+00	2008-11-21 11:15:55.603+00	23095	\N	\N	\N	\N
2008-11-21 11:15:55.657+00	\N	23097	\N	\N	\N	\N
2008-11-21 11:15:55.688+00	\N	23099	\N	\N	\N	\N
2008-11-21 11:15:55.793+00	2008-11-21 11:15:55.824+00	23100	\N	\N	\N	\N
2008-11-21 11:15:56.072+00	\N	23102	\N	\N	\N	\N
2008-11-21 11:15:56.103+00	\N	23104	\N	\N	\N	\N
2008-11-21 11:15:56.209+00	2008-11-21 11:15:56.241+00	23105	\N	\N	\N	\N
2008-11-21 11:15:56.454+00	\N	23107	\N	\N	\N	\N
2008-11-21 11:15:56.485+00	\N	23109	\N	\N	\N	\N
2008-11-21 11:15:56.591+00	2008-11-21 11:15:56.622+00	23110	\N	\N	\N	\N
2008-11-21 11:15:56.96+00	\N	23112	\N	\N	\N	\N
2008-11-21 11:15:56.997+00	\N	23114	\N	\N	\N	\N
2008-11-21 11:15:57.105+00	2008-11-21 11:15:57.137+00	23115	\N	\N	\N	\N
2008-11-21 11:15:57.312+00	\N	23117	\N	\N	\N	\N
2008-11-21 11:15:57.344+00	\N	23119	\N	\N	\N	\N
2008-11-21 11:15:57.454+00	2008-11-21 11:15:57.485+00	23120	\N	\N	\N	\N
2008-11-21 11:15:57.636+00	\N	23122	\N	\N	\N	\N
2008-11-21 11:15:57.667+00	\N	23124	\N	\N	\N	\N
2008-11-21 11:15:57.772+00	2008-11-21 11:15:57.803+00	23125	\N	\N	\N	\N
2008-11-21 11:15:58.05+00	\N	23127	\N	\N	\N	\N
2008-11-21 11:15:58.082+00	\N	23129	\N	\N	\N	\N
2008-11-21 11:15:58.213+00	2008-11-21 11:15:58.245+00	23130	\N	\N	\N	\N
2008-11-21 11:15:58.469+00	\N	23132	\N	\N	\N	\N
2008-11-21 11:15:58.501+00	\N	23134	\N	\N	\N	\N
2008-11-21 11:15:58.633+00	2008-11-21 11:15:58.665+00	23135	\N	\N	\N	\N
2008-11-21 11:15:58.914+00	\N	23137	\N	\N	\N	\N
2008-11-21 11:15:58.946+00	\N	23139	\N	\N	\N	\N
2008-11-21 11:15:59.059+00	2008-11-21 11:15:59.091+00	23140	\N	\N	\N	\N
2008-11-21 11:15:59.194+00	\N	23142	\N	\N	\N	\N
2008-11-21 11:15:59.226+00	\N	23144	\N	\N	\N	\N
2008-11-21 11:15:59.367+00	2008-11-21 11:15:59.399+00	23145	\N	\N	\N	\N
2008-11-21 11:15:59.603+00	\N	23147	\N	\N	\N	\N
2008-11-21 11:15:59.636+00	\N	23149	\N	\N	\N	\N
2008-11-21 11:15:59.768+00	2008-11-21 11:15:59.801+00	23150	\N	\N	\N	\N
2008-11-21 11:15:59.858+00	\N	23152	\N	\N	\N	\N
2008-11-21 11:15:59.89+00	\N	23154	\N	\N	\N	\N
2008-11-21 11:16:00.026+00	2008-11-21 11:16:00.064+00	23155	\N	\N	\N	\N
2008-11-21 11:16:00.267+00	\N	23157	\N	\N	\N	\N
2008-11-21 11:16:00.298+00	\N	23159	\N	\N	\N	\N
2008-11-21 11:16:00.402+00	2008-11-21 11:16:00.434+00	23160	\N	\N	\N	\N
2008-11-21 11:16:00.611+00	\N	23162	\N	\N	\N	\N
2008-11-21 11:16:00.644+00	\N	23164	\N	\N	\N	\N
2008-11-21 11:16:00.776+00	2008-11-21 11:16:00.81+00	23165	\N	\N	\N	\N
2008-11-21 11:16:00.985+00	\N	23167	\N	\N	\N	\N
2008-11-21 11:16:01.017+00	\N	23169	\N	\N	\N	\N
2008-11-21 11:16:01.147+00	2008-11-21 11:16:01.178+00	23170	\N	\N	\N	\N
2008-11-21 11:16:01.471+00	\N	23172	\N	\N	\N	\N
2008-11-21 11:16:01.504+00	\N	23174	\N	\N	\N	\N
2008-11-21 11:16:01.617+00	2008-11-21 11:16:01.65+00	23175	\N	\N	\N	\N
2008-11-21 11:16:01.833+00	\N	23177	\N	\N	\N	\N
2008-11-21 11:16:01.865+00	\N	23179	\N	\N	\N	\N
2008-11-21 11:16:02.089+00	2008-11-21 11:16:02.135+00	23180	\N	\N	\N	\N
2008-11-21 11:16:02.20+00	\N	23182	\N	\N	\N	\N
2008-11-21 11:16:02.232+00	\N	23184	\N	\N	\N	\N
2008-11-21 11:16:02.337+00	2008-11-21 11:16:02.369+00	23185	\N	\N	\N	\N
2008-11-21 11:16:02.448+00	\N	23187	\N	\N	\N	\N
2008-11-21 11:16:02.481+00	\N	23189	\N	\N	\N	\N
2008-11-21 11:16:02.618+00	2008-11-21 11:16:02.651+00	23190	\N	\N	\N	\N
2008-11-21 11:16:02.709+00	\N	23192	\N	\N	\N	\N
2008-11-21 11:16:02.742+00	\N	23194	\N	\N	\N	\N
2008-11-21 11:16:02.853+00	2008-11-21 11:16:02.886+00	23195	\N	\N	\N	\N
2008-11-21 11:16:03.021+00	\N	23197	\N	\N	\N	\N
2008-11-21 11:16:03.055+00	\N	23199	\N	\N	\N	\N
2008-11-21 11:16:03.196+00	2008-11-21 11:16:03.23+00	23200	\N	\N	\N	\N
2008-11-21 11:16:03.604+00	\N	23202	\N	\N	\N	\N
2008-11-21 11:16:03.639+00	\N	23204	\N	\N	\N	\N
2008-11-21 11:16:03.78+00	2008-11-21 11:16:03.813+00	23205	\N	\N	\N	\N
2008-11-21 11:16:03.899+00	\N	23207	\N	\N	\N	\N
2008-11-21 11:16:03.933+00	\N	23209	\N	\N	\N	\N
2008-11-21 11:16:04.048+00	2008-11-21 11:16:04.082+00	23210	\N	\N	\N	\N
2008-11-21 11:16:04.219+00	\N	23212	\N	\N	\N	\N
2008-11-21 11:16:04.254+00	\N	23214	\N	\N	\N	\N
2008-11-21 11:16:04.37+00	2008-11-21 11:16:04.403+00	23215	\N	\N	\N	\N
2008-11-21 11:16:04.515+00	\N	23217	\N	\N	\N	\N
2008-11-21 11:16:04.55+00	\N	23219	\N	\N	\N	\N
2008-11-21 11:16:04.666+00	2008-11-21 11:16:04.714+00	23220	\N	\N	\N	\N
2008-11-21 11:16:04.828+00	\N	23222	\N	\N	\N	\N
2008-11-21 11:16:04.863+00	\N	23224	\N	\N	\N	\N
2008-11-21 11:16:04.979+00	2008-11-21 11:16:05.013+00	23225	\N	\N	\N	\N
2008-11-21 11:16:05.126+00	\N	23227	\N	\N	\N	\N
2008-11-21 11:16:05.16+00	\N	23229	\N	\N	\N	\N
2008-11-21 11:16:05.301+00	2008-11-21 11:16:05.335+00	23230	\N	\N	\N	\N
2008-11-21 11:16:05.552+00	\N	23232	\N	\N	\N	\N
2008-11-21 11:16:05.587+00	\N	23234	\N	\N	\N	\N
2008-11-21 11:16:05.73+00	2008-11-21 11:16:05.765+00	23235	\N	\N	\N	\N
2008-11-21 11:16:05.936+00	\N	23237	\N	\N	\N	\N
2008-11-21 11:16:05.972+00	\N	23239	\N	\N	\N	\N
2008-11-21 11:16:06.091+00	2008-11-21 11:16:06.126+00	23240	\N	\N	\N	\N
2008-11-21 11:16:06.211+00	\N	23242	\N	\N	\N	\N
2008-11-21 11:16:06.244+00	\N	23244	\N	\N	\N	\N
2008-11-21 11:16:06.356+00	2008-11-21 11:16:06.388+00	23245	\N	\N	\N	\N
2008-11-21 11:16:06.647+00	\N	23247	\N	\N	\N	\N
2008-11-21 11:16:06.681+00	\N	23249	\N	\N	\N	\N
2008-11-21 11:16:06.793+00	2008-11-21 11:16:06.826+00	23250	\N	\N	\N	\N
2008-11-21 11:16:06.888+00	\N	23252	\N	\N	\N	\N
2008-11-21 11:16:06.922+00	\N	23254	\N	\N	\N	\N
2008-11-21 11:16:07.041+00	2008-11-21 11:16:07.076+00	23255	\N	\N	\N	\N
2008-11-21 11:16:07.175+00	\N	23257	\N	\N	\N	\N
2008-11-21 11:16:07.211+00	\N	23259	\N	\N	\N	\N
2008-11-21 11:16:07.357+00	2008-11-21 11:16:07.391+00	23260	\N	\N	\N	\N
2008-11-21 11:16:07.56+00	\N	23262	\N	\N	\N	\N
2008-11-21 11:16:07.595+00	\N	23264	\N	\N	\N	\N
2008-11-21 11:16:07.713+00	2008-11-21 11:16:07.747+00	23265	\N	\N	\N	\N
2008-11-21 11:16:07.995+00	\N	23267	\N	\N	\N	\N
2008-11-21 11:16:08.03+00	\N	23269	\N	\N	\N	\N
2008-11-21 11:16:08.149+00	2008-11-21 11:16:08.185+00	23270	\N	\N	\N	\N
2008-11-21 11:16:08.411+00	\N	23272	\N	\N	\N	\N
2008-11-21 11:16:08.447+00	\N	23274	\N	\N	\N	\N
2008-11-21 11:16:08.595+00	2008-11-21 11:16:08.631+00	23275	\N	\N	\N	\N
2008-11-21 11:16:08.913+00	\N	23277	\N	\N	\N	\N
2008-11-21 11:16:08.948+00	\N	23279	\N	\N	\N	\N
2008-11-21 11:16:09.095+00	2008-11-21 11:16:09.131+00	23280	\N	\N	\N	\N
2008-11-21 11:16:09.412+00	\N	23282	\N	\N	\N	\N
2008-11-21 11:16:09.448+00	\N	23284	\N	\N	\N	\N
2008-11-21 11:16:09.579+00	2008-11-21 11:16:09.614+00	23285	\N	\N	\N	\N
2008-11-21 11:16:09.732+00	\N	23287	\N	\N	\N	\N
2008-11-21 11:16:09.767+00	\N	23289	\N	\N	\N	\N
2008-11-21 11:16:09.887+00	2008-11-21 11:16:09.922+00	23290	\N	\N	\N	\N
2008-11-21 11:16:10.093+00	\N	23292	\N	\N	\N	\N
2008-11-21 11:16:10.128+00	\N	23294	\N	\N	\N	\N
2008-11-21 11:16:10.248+00	2008-11-21 11:16:10.283+00	23295	\N	\N	\N	\N
2008-11-21 11:16:10.371+00	\N	23297	\N	\N	\N	\N
2008-11-21 11:16:10.407+00	\N	23299	\N	\N	\N	\N
2008-11-21 11:16:10.527+00	2008-11-21 11:16:10.563+00	23300	\N	\N	\N	\N
2008-11-21 11:16:10.683+00	\N	23302	\N	\N	\N	\N
2008-11-21 11:16:10.719+00	\N	23304	\N	\N	\N	\N
2008-11-21 11:16:10.869+00	2008-11-21 11:16:10.906+00	23305	\N	\N	\N	\N
2008-11-21 11:16:10.97+00	\N	23307	\N	\N	\N	\N
2008-11-21 11:16:11.006+00	\N	23309	\N	\N	\N	\N
2008-11-21 11:16:11.158+00	2008-11-21 11:16:11.193+00	23310	\N	\N	\N	\N
2008-11-21 11:16:11.312+00	\N	23312	\N	\N	\N	\N
2008-11-21 11:16:11.348+00	\N	23314	\N	\N	\N	\N
2008-11-21 11:16:11.47+00	2008-11-21 11:16:11.506+00	23315	\N	\N	\N	\N
2008-11-21 11:16:11.654+00	\N	23317	\N	\N	\N	\N
2008-11-21 11:16:11.69+00	\N	23319	\N	\N	\N	\N
2008-11-21 11:16:11.823+00	2008-11-21 11:16:11.86+00	23320	\N	\N	\N	\N
2008-11-21 11:16:12.118+00	\N	23322	\N	\N	\N	\N
2008-11-21 11:16:12.154+00	\N	23324	\N	\N	\N	\N
2008-11-21 11:16:12.331+00	2008-11-21 11:16:12.366+00	23325	\N	\N	\N	\N
2008-11-21 11:16:12.484+00	\N	23327	\N	\N	\N	\N
2008-11-21 11:16:12.519+00	\N	23329	\N	\N	\N	\N
2008-11-21 11:16:12.696+00	2008-11-21 11:16:12.731+00	23330	\N	\N	\N	\N
2008-11-21 11:16:12.795+00	\N	23332	\N	\N	\N	\N
2008-11-21 11:16:12.831+00	\N	23334	\N	\N	\N	\N
2008-11-21 11:16:13.012+00	2008-11-21 11:16:13.049+00	23335	\N	\N	\N	\N
2008-11-21 11:16:13.115+00	\N	23337	\N	\N	\N	\N
2008-11-21 11:16:13.151+00	\N	23339	\N	\N	\N	\N
2008-11-21 11:16:13.333+00	2008-11-21 11:16:13.37+00	23340	\N	\N	\N	\N
2008-11-21 11:16:13.491+00	\N	23342	\N	\N	\N	\N
2008-11-21 11:16:13.528+00	\N	23344	\N	\N	\N	\N
2008-11-21 11:16:13.707+00	2008-11-21 11:16:13.747+00	23345	\N	\N	\N	\N
2008-11-21 11:16:13.814+00	\N	23347	\N	\N	\N	\N
2008-11-21 11:16:13.851+00	\N	23349	\N	\N	\N	\N
2008-11-21 11:16:14.018+00	2008-11-21 11:16:14.056+00	23350	\N	\N	\N	\N
2008-11-21 11:16:14.181+00	\N	23352	\N	\N	\N	\N
2008-11-21 11:16:14.218+00	\N	23354	\N	\N	\N	\N
2008-11-21 11:16:14.375+00	2008-11-21 11:16:14.413+00	23355	\N	\N	\N	\N
2008-11-21 11:16:14.478+00	\N	23357	\N	\N	\N	\N
2008-11-21 11:16:14.516+00	\N	23359	\N	\N	\N	\N
2008-11-21 11:16:14.671+00	2008-11-21 11:16:14.708+00	23360	\N	\N	\N	\N
2008-11-21 11:16:14.947+00	\N	23362	\N	\N	\N	\N
2008-11-21 11:16:14.987+00	\N	23364	\N	\N	\N	\N
2008-11-21 11:16:15.146+00	2008-11-21 11:16:15.185+00	23365	\N	\N	\N	\N
2008-11-21 11:16:15.342+00	\N	23367	\N	\N	\N	\N
2008-11-21 11:16:15.381+00	\N	23369	\N	\N	\N	\N
2008-11-21 11:16:15.541+00	2008-11-21 11:16:15.581+00	23370	\N	\N	\N	\N
2008-11-21 11:16:15.823+00	\N	23372	\N	\N	\N	\N
2008-11-21 11:16:15.862+00	\N	23374	\N	\N	\N	\N
2008-11-21 11:16:15.991+00	2008-11-21 11:16:16.047+00	23375	\N	\N	\N	\N
2008-11-21 11:16:16.32+00	\N	23377	\N	\N	\N	\N
2008-11-21 11:16:16.36+00	\N	23379	\N	\N	\N	\N
2008-11-21 11:16:16.518+00	2008-11-21 11:16:16.556+00	23380	\N	\N	\N	\N
2008-11-21 11:16:16.651+00	\N	23382	\N	\N	\N	\N
2008-11-21 11:16:16.689+00	\N	23384	\N	\N	\N	\N
2008-11-21 11:16:16.846+00	2008-11-21 11:16:16.883+00	23385	\N	\N	\N	\N
2008-11-21 11:16:16.951+00	\N	23387	\N	\N	\N	\N
2008-11-21 11:16:16.989+00	\N	23389	\N	\N	\N	\N
2008-11-21 11:16:17.15+00	2008-11-21 11:16:17.19+00	23390	\N	\N	\N	\N
2008-11-21 11:16:17.259+00	\N	23392	\N	\N	\N	\N
2008-11-21 11:16:17.298+00	\N	23394	\N	\N	\N	\N
2008-11-21 11:16:17.46+00	2008-11-21 11:16:17.50+00	23395	\N	\N	\N	\N
2008-11-21 11:16:17.63+00	\N	23397	\N	\N	\N	\N
2008-11-21 11:16:17.67+00	\N	23399	\N	\N	\N	\N
2008-11-21 11:16:17.80+00	2008-11-21 11:16:17.839+00	23400	\N	\N	\N	\N
2008-11-21 11:16:18.036+00	\N	23402	\N	\N	\N	\N
2008-11-21 11:16:18.076+00	\N	23404	\N	\N	\N	\N
2008-11-21 11:16:18.209+00	2008-11-21 11:16:18.249+00	23405	\N	\N	\N	\N
2008-11-21 11:16:18.616+00	\N	23407	\N	\N	\N	\N
2008-11-21 11:16:18.655+00	\N	23409	\N	\N	\N	\N
2008-11-21 11:16:18.785+00	2008-11-21 11:16:18.824+00	23410	\N	\N	\N	\N
2008-11-21 11:16:19.38+00	\N	23412	\N	\N	\N	\N
2008-11-21 11:16:19.419+00	\N	23414	\N	\N	\N	\N
2008-11-21 11:16:19.555+00	2008-11-21 11:16:19.593+00	23415	\N	\N	\N	\N
2008-11-21 11:16:20.019+00	\N	23417	\N	\N	\N	\N
2008-11-21 11:16:20.058+00	\N	23419	\N	\N	\N	\N
2008-11-21 11:16:20.19+00	2008-11-21 11:16:20.227+00	23420	\N	\N	\N	\N
2008-11-21 11:16:20.598+00	\N	23422	\N	\N	\N	\N
2008-11-21 11:16:20.636+00	\N	23424	\N	\N	\N	\N
2008-11-21 11:16:20.786+00	2008-11-21 11:16:20.828+00	23425	\N	\N	\N	\N
2008-11-21 11:16:21.009+00	\N	23427	\N	\N	\N	\N
2008-11-21 11:16:21.048+00	\N	23429	\N	\N	\N	\N
2008-11-21 11:16:21.176+00	2008-11-21 11:16:21.215+00	23430	\N	\N	\N	\N
2008-11-21 11:16:21.399+00	\N	23432	\N	\N	\N	\N
2008-11-21 11:16:21.438+00	\N	23434	\N	\N	\N	\N
2008-11-21 11:16:21.627+00	2008-11-21 11:16:21.668+00	23435	\N	\N	\N	\N
2008-11-21 11:16:22.053+00	\N	23437	\N	\N	\N	\N
2008-11-21 11:16:22.095+00	\N	23439	\N	\N	\N	\N
2008-11-21 11:16:22.222+00	2008-11-21 11:16:22.26+00	23440	\N	\N	\N	\N
2008-11-21 11:16:22.633+00	\N	23442	\N	\N	\N	\N
2008-11-21 11:16:22.674+00	\N	23444	\N	\N	\N	\N
2008-11-21 11:16:22.809+00	2008-11-21 11:16:22.849+00	23445	\N	\N	\N	\N
2008-11-21 11:16:23.324+00	\N	23447	\N	\N	\N	\N
2008-11-21 11:16:23.375+00	\N	23449	\N	\N	\N	\N
2008-11-21 11:16:23.512+00	2008-11-21 11:16:23.553+00	23450	\N	\N	\N	\N
2008-11-21 11:16:23.748+00	\N	23452	\N	\N	\N	\N
2008-11-21 11:16:23.807+00	\N	23454	\N	\N	\N	\N
2008-11-21 11:16:23.945+00	2008-11-21 11:16:23.986+00	23455	\N	\N	\N	\N
2008-11-21 11:16:24.243+00	\N	23457	\N	\N	\N	\N
2008-11-21 11:16:24.284+00	\N	23459	\N	\N	\N	\N
2008-11-21 11:16:24.418+00	2008-11-21 11:16:24.458+00	23460	\N	\N	\N	\N
2008-11-21 11:16:24.714+00	\N	23462	\N	\N	\N	\N
2008-11-21 11:16:24.755+00	\N	23464	\N	\N	\N	\N
2008-11-21 11:16:24.892+00	2008-11-21 11:16:24.933+00	23465	\N	\N	\N	\N
2008-11-21 11:16:25.195+00	\N	23467	\N	\N	\N	\N
2008-11-21 11:16:25.237+00	\N	23469	\N	\N	\N	\N
2008-11-21 11:16:25.408+00	2008-11-21 11:16:25.476+00	23470	\N	\N	\N	\N
2008-11-21 11:16:25.691+00	\N	23472	\N	\N	\N	\N
2008-11-21 11:16:25.729+00	\N	23474	\N	\N	\N	\N
2008-11-21 11:16:25.857+00	2008-11-21 11:16:25.896+00	23475	\N	\N	\N	\N
2008-11-21 11:16:26.122+00	\N	23477	\N	\N	\N	\N
2008-11-21 11:16:26.165+00	\N	23479	\N	\N	\N	\N
2008-11-21 11:16:26.31+00	2008-11-21 11:16:26.353+00	23480	\N	\N	\N	\N
2008-11-21 11:16:26.593+00	\N	23482	\N	\N	\N	\N
2008-11-21 11:16:26.636+00	\N	23484	\N	\N	\N	\N
2008-11-21 11:16:26.777+00	2008-11-21 11:16:26.819+00	23485	\N	\N	\N	\N
2008-11-21 11:16:26.954+00	\N	23487	\N	\N	\N	\N
2008-11-21 11:16:26.994+00	\N	23489	\N	\N	\N	\N
2008-11-21 11:16:27.17+00	2008-11-21 11:16:27.211+00	23490	\N	\N	\N	\N
2008-11-21 11:16:27.616+00	\N	23492	\N	\N	\N	\N
2008-11-21 11:16:27.66+00	\N	23494	\N	\N	\N	\N
2008-11-21 11:16:27.836+00	2008-11-21 11:16:27.878+00	23495	\N	\N	\N	\N
2008-11-21 11:16:28.31+00	\N	23497	\N	\N	\N	\N
2008-11-21 11:16:28.353+00	\N	23499	\N	\N	\N	\N
2008-11-21 11:16:28.528+00	2008-11-21 11:16:28.571+00	23500	\N	\N	\N	\N
2008-11-21 11:16:29.051+00	\N	23502	\N	\N	\N	\N
2008-11-21 11:16:29.092+00	\N	23504	\N	\N	\N	\N
2008-11-21 11:16:29.224+00	2008-11-21 11:16:29.264+00	23505	\N	\N	\N	\N
2008-11-21 11:16:29.657+00	\N	23507	\N	\N	\N	\N
2008-11-21 11:16:29.70+00	\N	23509	\N	\N	\N	\N
2008-11-21 11:16:29.843+00	2008-11-21 11:16:29.884+00	23510	\N	\N	\N	\N
2008-11-21 11:16:30.216+00	\N	23512	\N	\N	\N	\N
2008-11-21 11:16:30.257+00	\N	23514	\N	\N	\N	\N
2008-11-21 11:16:30.397+00	2008-11-21 11:16:30.438+00	23515	\N	\N	\N	\N
2008-11-21 11:16:30.59+00	\N	23517	\N	\N	\N	\N
2008-11-21 11:16:30.77+00	\N	23519	\N	\N	\N	\N
2008-11-21 11:16:30.91+00	2008-11-21 11:16:30.952+00	23520	\N	\N	\N	\N
2008-11-21 11:16:31.094+00	\N	23522	\N	\N	\N	\N
2008-11-21 11:16:31.137+00	\N	23524	\N	\N	\N	\N
2008-11-21 11:16:31.28+00	2008-11-21 11:16:31.325+00	23525	\N	\N	\N	\N
2008-11-21 11:16:31.43+00	\N	23527	\N	\N	\N	\N
2008-11-21 11:16:31.47+00	\N	23529	\N	\N	\N	\N
2008-11-21 11:16:31.608+00	2008-11-21 11:16:31.648+00	23530	\N	\N	\N	\N
2008-11-21 11:16:31.751+00	\N	23532	\N	\N	\N	\N
2008-11-21 11:16:31.793+00	\N	23534	\N	\N	\N	\N
2008-11-21 11:16:31.933+00	2008-11-21 11:16:31.975+00	23535	\N	\N	\N	\N
2008-11-21 11:16:32.128+00	\N	23537	\N	\N	\N	\N
2008-11-21 11:16:32.17+00	\N	23539	\N	\N	\N	\N
2008-11-21 11:16:32.344+00	2008-11-21 11:16:32.386+00	23540	\N	\N	\N	\N
2008-11-21 11:16:32.561+00	\N	23542	\N	\N	\N	\N
2008-11-21 11:16:32.605+00	\N	23544	\N	\N	\N	\N
2008-11-21 11:16:32.751+00	2008-11-21 11:16:32.795+00	23545	\N	\N	\N	\N
2008-11-21 11:16:32.937+00	\N	23547	\N	\N	\N	\N
2008-11-21 11:16:32.98+00	\N	23549	\N	\N	\N	\N
2008-11-21 11:16:33.126+00	2008-11-21 11:16:33.168+00	23550	\N	\N	\N	\N
2008-11-21 11:16:33.405+00	\N	23552	\N	\N	\N	\N
2008-11-21 11:16:33.447+00	\N	23554	\N	\N	\N	\N
2008-11-21 11:16:33.636+00	2008-11-21 11:16:33.678+00	23555	\N	\N	\N	\N
2008-11-21 11:16:33.982+00	\N	23557	\N	\N	\N	\N
2008-11-21 11:16:34.026+00	\N	23559	\N	\N	\N	\N
2008-11-21 11:16:34.175+00	2008-11-21 11:16:34.218+00	23560	\N	\N	\N	\N
2008-11-21 11:16:34.295+00	\N	23562	\N	\N	\N	\N
2008-11-21 11:16:34.338+00	\N	23564	\N	\N	\N	\N
2008-11-21 11:16:34.551+00	2008-11-21 11:16:34.593+00	23565	\N	\N	\N	\N
2008-11-21 11:16:34.976+00	\N	23567	\N	\N	\N	\N
2008-11-21 11:16:35.019+00	\N	23569	\N	\N	\N	\N
2008-11-21 11:16:35.231+00	2008-11-21 11:16:35.274+00	23570	\N	\N	\N	\N
2008-11-21 11:16:35.418+00	\N	23572	\N	\N	\N	\N
2008-11-21 11:16:35.461+00	\N	23574	\N	\N	\N	\N
2008-11-21 11:16:35.611+00	2008-11-21 11:16:35.656+00	23575	\N	\N	\N	\N
2008-11-21 11:16:35.903+00	\N	23577	\N	\N	\N	\N
2008-11-21 11:16:35.946+00	\N	23579	\N	\N	\N	\N
2008-11-21 11:16:36.093+00	2008-11-21 11:16:36.135+00	23580	\N	\N	\N	\N
2008-11-21 11:16:36.224+00	\N	23582	\N	\N	\N	\N
2008-11-21 11:16:36.266+00	\N	23584	\N	\N	\N	\N
2008-11-21 11:16:36.411+00	2008-11-21 11:16:36.455+00	23585	\N	\N	\N	\N
2008-11-21 11:16:36.531+00	\N	23587	\N	\N	\N	\N
2008-11-21 11:16:36.576+00	\N	23589	\N	\N	\N	\N
2008-11-21 11:16:36.725+00	2008-11-21 11:16:36.77+00	23590	\N	\N	\N	\N
2008-11-21 11:16:36.847+00	\N	23592	\N	\N	\N	\N
2008-11-21 11:16:36.891+00	\N	23594	\N	\N	\N	\N
2008-11-21 11:16:37.042+00	2008-11-21 11:16:37.086+00	23595	\N	\N	\N	\N
2008-11-21 11:16:37.163+00	\N	23597	\N	\N	\N	\N
2008-11-21 11:16:37.206+00	\N	23599	\N	\N	\N	\N
2008-11-21 11:16:37.385+00	2008-11-21 11:16:37.439+00	23600	\N	\N	\N	\N
2008-11-21 11:16:37.852+00	\N	23602	\N	\N	\N	\N
2008-11-21 11:16:37.897+00	\N	23604	\N	\N	\N	\N
2008-11-21 11:16:38.048+00	2008-11-21 11:16:38.093+00	23605	\N	\N	\N	\N
2008-11-21 11:16:38.205+00	\N	23607	\N	\N	\N	\N
2008-11-21 11:16:38.249+00	\N	23609	\N	\N	\N	\N
2008-11-21 11:16:38.431+00	2008-11-21 11:16:38.474+00	23610	\N	\N	\N	\N
2008-11-21 11:16:38.832+00	\N	23612	\N	\N	\N	\N
2008-11-21 11:16:38.876+00	\N	23614	\N	\N	\N	\N
2008-11-21 11:16:39.06+00	2008-11-21 11:16:39.104+00	23615	\N	\N	\N	\N
2008-11-21 11:16:39.389+00	\N	23617	\N	\N	\N	\N
2008-11-21 11:16:39.434+00	\N	23619	\N	\N	\N	\N
2008-11-21 11:16:39.585+00	2008-11-21 11:16:39.63+00	23620	\N	\N	\N	\N
2008-11-21 11:16:39.843+00	\N	23622	\N	\N	\N	\N
2008-11-21 11:16:39.919+00	\N	23624	\N	\N	\N	\N
2008-11-21 11:16:40.145+00	2008-11-21 11:16:40.187+00	23625	\N	\N	\N	\N
2008-11-21 11:16:40.458+00	\N	23627	\N	\N	\N	\N
2008-11-21 11:16:40.503+00	\N	23629	\N	\N	\N	\N
2008-11-21 11:16:40.659+00	2008-11-21 11:16:40.704+00	23630	\N	\N	\N	\N
2008-11-21 11:16:41.259+00	\N	23632	\N	\N	\N	\N
2008-11-21 11:16:41.308+00	\N	23634	\N	\N	\N	\N
2008-11-21 11:16:41.466+00	2008-11-21 11:16:41.512+00	23635	\N	\N	\N	\N
2008-11-21 11:16:41.733+00	\N	23637	\N	\N	\N	\N
2008-11-21 11:16:41.779+00	\N	23639	\N	\N	\N	\N
2008-11-21 11:16:41.936+00	2008-11-21 11:16:41.982+00	23640	\N	\N	\N	\N
2008-11-21 11:16:42.203+00	\N	23642	\N	\N	\N	\N
2008-11-21 11:16:42.251+00	\N	23644	\N	\N	\N	\N
2008-11-21 11:16:42.422+00	2008-11-21 11:16:42.47+00	23645	\N	\N	\N	\N
2008-11-21 11:16:42.799+00	\N	23647	\N	\N	\N	\N
2008-11-21 11:16:42.845+00	\N	23649	\N	\N	\N	\N
2008-11-21 11:16:43.003+00	2008-11-21 11:16:43.049+00	23650	\N	\N	\N	\N
2008-11-21 11:16:43.271+00	\N	23652	\N	\N	\N	\N
2008-11-21 11:16:43.318+00	\N	23654	\N	\N	\N	\N
2008-11-21 11:16:43.489+00	2008-11-21 11:16:43.537+00	23655	\N	\N	\N	\N
2008-11-21 11:16:43.618+00	\N	23657	\N	\N	\N	\N
2008-11-21 11:16:43.665+00	\N	23659	\N	\N	\N	\N
2008-11-21 11:16:43.827+00	2008-11-21 11:16:43.873+00	23660	\N	\N	\N	\N
2008-11-21 11:16:43.954+00	\N	23662	\N	\N	\N	\N
2008-11-21 11:16:43.999+00	\N	23664	\N	\N	\N	\N
2008-11-21 11:16:44.159+00	2008-11-21 11:16:44.205+00	23665	\N	\N	\N	\N
2008-11-21 11:16:44.287+00	\N	23667	\N	\N	\N	\N
2008-11-21 11:16:44.333+00	\N	23669	\N	\N	\N	\N
2008-11-21 11:16:44.507+00	2008-11-21 11:16:44.555+00	23670	\N	\N	\N	\N
2008-11-21 11:16:44.638+00	\N	23672	\N	\N	\N	\N
2008-11-21 11:16:44.685+00	\N	23674	\N	\N	\N	\N
2008-11-21 11:16:44.847+00	2008-11-21 11:16:44.894+00	23675	\N	\N	\N	\N
2008-11-21 11:16:44.976+00	\N	23677	\N	\N	\N	\N
2008-11-21 11:16:45.022+00	\N	23679	\N	\N	\N	\N
2008-11-21 11:16:45.183+00	2008-11-21 11:16:45.229+00	23680	\N	\N	\N	\N
2008-11-21 11:16:45.312+00	\N	23682	\N	\N	\N	\N
2008-11-21 11:16:45.358+00	\N	23684	\N	\N	\N	\N
2008-11-21 11:16:45.532+00	2008-11-21 11:16:45.582+00	23685	\N	\N	\N	\N
2008-11-21 11:16:45.665+00	\N	23687	\N	\N	\N	\N
2008-11-21 11:16:45.712+00	\N	23689	\N	\N	\N	\N
2008-11-21 11:16:45.875+00	2008-11-21 11:16:45.922+00	23690	\N	\N	\N	\N
2008-11-21 11:16:46.005+00	\N	23692	\N	\N	\N	\N
2008-11-21 11:16:46.051+00	\N	23694	\N	\N	\N	\N
2008-11-21 11:16:46.213+00	2008-11-21 11:16:46.261+00	23695	\N	\N	\N	\N
2008-11-21 11:16:46.355+00	\N	23697	\N	\N	\N	\N
2008-11-21 11:16:46.403+00	\N	23699	\N	\N	\N	\N
2008-11-21 11:16:46.568+00	2008-11-21 11:16:46.616+00	23700	\N	\N	\N	\N
2008-11-21 11:16:46.701+00	\N	23702	\N	\N	\N	\N
2008-11-21 11:16:46.749+00	\N	23704	\N	\N	\N	\N
2008-11-21 11:16:46.911+00	2008-11-21 11:16:46.958+00	23705	\N	\N	\N	\N
2008-11-21 11:16:47.041+00	\N	23707	\N	\N	\N	\N
2008-11-21 11:16:47.088+00	\N	23709	\N	\N	\N	\N
2008-11-21 11:16:47.251+00	2008-11-21 11:16:47.309+00	23710	\N	\N	\N	\N
2008-11-21 11:16:47.395+00	\N	23712	\N	\N	\N	\N
2008-11-21 11:16:47.443+00	\N	23714	\N	\N	\N	\N
2008-11-21 11:16:47.608+00	2008-11-21 11:16:47.657+00	23715	\N	\N	\N	\N
2008-11-21 11:16:47.741+00	\N	23717	\N	\N	\N	\N
2008-11-21 11:16:47.788+00	\N	23719	\N	\N	\N	\N
2008-11-21 11:16:47.951+00	2008-11-21 11:16:47.998+00	23720	\N	\N	\N	\N
2008-11-21 11:16:48.083+00	\N	23722	\N	\N	\N	\N
2008-11-21 11:16:48.131+00	\N	23724	\N	\N	\N	\N
2008-11-21 11:16:48.31+00	2008-11-21 11:16:48.359+00	23725	\N	\N	\N	\N
2008-11-21 11:16:48.443+00	\N	23727	\N	\N	\N	\N
2008-11-21 11:16:48.492+00	\N	23729	\N	\N	\N	\N
2008-11-21 11:16:48.657+00	2008-11-21 11:16:48.705+00	23730	\N	\N	\N	\N
2008-11-21 11:16:48.788+00	\N	23732	\N	\N	\N	\N
2008-11-21 11:16:48.835+00	\N	23734	\N	\N	\N	\N
2008-11-21 11:16:48.999+00	2008-11-21 11:16:49.058+00	23735	\N	\N	\N	\N
2008-11-21 11:16:49.144+00	\N	23737	\N	\N	\N	\N
2008-11-21 11:16:49.193+00	\N	23739	\N	\N	\N	\N
2008-11-21 11:16:49.36+00	2008-11-21 11:16:49.409+00	23740	\N	\N	\N	\N
2008-11-21 11:16:49.494+00	\N	23742	\N	\N	\N	\N
2008-11-21 11:16:49.544+00	\N	23744	\N	\N	\N	\N
2008-11-21 11:16:49.708+00	2008-11-21 11:16:49.757+00	23745	\N	\N	\N	\N
2008-11-21 11:16:49.841+00	\N	23747	\N	\N	\N	\N
2008-11-21 11:16:49.90+00	\N	23749	\N	\N	\N	\N
2008-11-21 11:16:50.143+00	2008-11-21 11:16:50.194+00	23750	\N	\N	\N	\N
2008-11-21 11:16:50.318+00	\N	23752	\N	\N	\N	\N
2008-11-21 11:16:50.365+00	\N	23754	\N	\N	\N	\N
2008-11-21 11:16:50.568+00	2008-11-21 11:16:50.617+00	23755	\N	\N	\N	\N
2008-11-21 11:16:50.712+00	\N	23757	\N	\N	\N	\N
2008-11-21 11:16:50.762+00	\N	23759	\N	\N	\N	\N
2008-11-21 11:16:50.934+00	2008-11-21 11:16:50.984+00	23760	\N	\N	\N	\N
2008-11-21 11:16:51.22+00	\N	23762	\N	\N	\N	\N
2008-11-21 11:16:51.269+00	\N	23764	\N	\N	\N	\N
2008-11-21 11:16:51.435+00	2008-11-21 11:16:51.486+00	23765	\N	\N	\N	\N
2008-11-21 11:16:51.684+00	\N	23767	\N	\N	\N	\N
2008-11-21 11:16:51.745+00	\N	23769	\N	\N	\N	\N
2008-11-21 11:16:51.915+00	2008-11-21 11:16:51.965+00	23770	\N	\N	\N	\N
2008-11-21 11:16:52.052+00	\N	23772	\N	\N	\N	\N
2008-11-21 11:16:52.101+00	\N	23774	\N	\N	\N	\N
2008-11-21 11:16:52.307+00	2008-11-21 11:16:52.355+00	23775	\N	\N	\N	\N
2008-11-21 11:16:52.516+00	\N	23777	\N	\N	\N	\N
2008-11-21 11:16:52.564+00	\N	23779	\N	\N	\N	\N
2008-11-21 11:16:52.744+00	2008-11-21 11:16:52.795+00	23780	\N	\N	\N	\N
2008-11-21 11:16:52.883+00	\N	23782	\N	\N	\N	\N
2008-11-21 11:16:52.931+00	\N	23784	\N	\N	\N	\N
2008-11-21 11:16:53.102+00	2008-11-21 11:16:53.151+00	23785	\N	\N	\N	\N
2008-11-21 11:16:53.386+00	\N	23787	\N	\N	\N	\N
2008-11-21 11:16:53.435+00	\N	23789	\N	\N	\N	\N
2008-11-21 11:16:53.614+00	2008-11-21 11:16:53.665+00	23790	\N	\N	\N	\N
2008-11-21 11:16:53.905+00	\N	23792	\N	\N	\N	\N
2008-11-21 11:16:53.956+00	\N	23794	\N	\N	\N	\N
2008-11-21 11:16:54.126+00	2008-11-21 11:16:54.175+00	23795	\N	\N	\N	\N
2008-11-21 11:16:54.412+00	\N	23797	\N	\N	\N	\N
2008-11-21 11:16:54.461+00	\N	23799	\N	\N	\N	\N
2008-11-21 11:16:54.718+00	2008-11-21 11:16:54.768+00	23800	\N	\N	\N	\N
2008-11-21 11:16:54.933+00	\N	23802	\N	\N	\N	\N
2008-11-21 11:16:54.983+00	\N	23804	\N	\N	\N	\N
2008-11-21 11:16:55.152+00	2008-11-21 11:16:55.202+00	23805	\N	\N	\N	\N
2008-11-21 11:16:55.288+00	\N	23807	\N	\N	\N	\N
2008-11-21 11:16:55.336+00	\N	23809	\N	\N	\N	\N
2008-11-21 11:16:55.516+00	2008-11-21 11:16:55.567+00	23810	\N	\N	\N	\N
2008-11-21 11:16:55.655+00	\N	23812	\N	\N	\N	\N
2008-11-21 11:16:55.705+00	\N	23814	\N	\N	\N	\N
2008-11-21 11:16:55.877+00	2008-11-21 11:16:55.925+00	23815	\N	\N	\N	\N
2008-11-21 11:16:56.126+00	\N	23817	\N	\N	\N	\N
2008-11-21 11:16:56.175+00	\N	23819	\N	\N	\N	\N
2008-11-21 11:16:56.437+00	2008-11-21 11:16:56.488+00	23820	\N	\N	\N	\N
2008-11-21 11:16:56.579+00	\N	23822	\N	\N	\N	\N
2008-11-21 11:16:56.629+00	\N	23824	\N	\N	\N	\N
2008-11-21 11:16:56.877+00	2008-11-21 11:16:56.926+00	23825	\N	\N	\N	\N
2008-11-21 11:16:57.168+00	\N	23827	\N	\N	\N	\N
2008-11-21 11:16:57.218+00	\N	23829	\N	\N	\N	\N
2008-11-21 11:16:57.447+00	2008-11-21 11:16:57.499+00	23830	\N	\N	\N	\N
2008-11-21 11:16:57.587+00	\N	23832	\N	\N	\N	\N
2008-11-21 11:16:57.637+00	\N	23834	\N	\N	\N	\N
2008-11-21 11:16:57.81+00	2008-11-21 11:16:57.859+00	23835	\N	\N	\N	\N
2008-11-21 11:16:58.101+00	\N	23837	\N	\N	\N	\N
2008-11-21 11:16:58.15+00	\N	23839	\N	\N	\N	\N
2008-11-21 11:16:58.325+00	2008-11-21 11:16:58.376+00	23840	\N	\N	\N	\N
2008-11-21 11:16:58.556+00	\N	23842	\N	\N	\N	\N
2008-11-21 11:16:58.608+00	\N	23844	\N	\N	\N	\N
2008-11-21 11:16:58.783+00	2008-11-21 11:16:58.834+00	23845	\N	\N	\N	\N
2008-11-21 11:16:59.156+00	\N	23847	\N	\N	\N	\N
2008-11-21 11:16:59.207+00	\N	23849	\N	\N	\N	\N
2008-11-21 11:16:59.38+00	2008-11-21 11:16:59.43+00	23850	\N	\N	\N	\N
2008-11-21 11:16:59.951+00	\N	23852	\N	\N	\N	\N
2008-11-21 11:16:59.999+00	\N	23854	\N	\N	\N	\N
2008-11-21 11:17:00.161+00	2008-11-21 11:17:00.209+00	23855	\N	\N	\N	\N
2008-11-21 11:17:00.528+00	\N	23857	\N	\N	\N	\N
2008-11-21 11:17:00.578+00	\N	23859	\N	\N	\N	\N
2008-11-21 11:17:00.747+00	2008-11-21 11:17:00.797+00	23860	\N	\N	\N	\N
2008-11-21 11:17:01.204+00	\N	23862	\N	\N	\N	\N
2008-11-21 11:17:01.268+00	\N	23864	\N	\N	\N	\N
2008-11-21 11:17:01.48+00	2008-11-21 11:17:01.531+00	23865	\N	\N	\N	\N
2008-11-21 11:17:01.621+00	\N	23867	\N	\N	\N	\N
2008-11-21 11:17:01.705+00	\N	23869	\N	\N	\N	\N
2008-11-21 11:17:02.022+00	2008-11-21 11:17:02.077+00	23870	\N	\N	\N	\N
2008-11-21 11:17:02.289+00	\N	23872	\N	\N	\N	\N
2008-11-21 11:17:02.342+00	\N	23874	\N	\N	\N	\N
2008-11-21 11:17:02.56+00	2008-11-21 11:17:02.612+00	23875	\N	\N	\N	\N
2008-11-21 11:17:02.741+00	\N	23877	\N	\N	\N	\N
2008-11-21 11:17:02.792+00	\N	23879	\N	\N	\N	\N
2008-11-21 11:17:03.007+00	2008-11-21 11:17:03.063+00	23880	\N	\N	\N	\N
2008-11-21 11:17:03.168+00	\N	23882	\N	\N	\N	\N
2008-11-21 11:17:03.219+00	\N	23884	\N	\N	\N	\N
2008-11-21 11:17:03.391+00	2008-11-21 11:17:03.439+00	23885	\N	\N	\N	\N
2008-11-21 11:17:03.605+00	\N	23887	\N	\N	\N	\N
2008-11-21 11:17:03.656+00	\N	23889	\N	\N	\N	\N
2008-11-21 11:17:03.831+00	2008-11-21 11:17:03.883+00	23890	\N	\N	\N	\N
2008-11-21 11:17:03.98+00	\N	23892	\N	\N	\N	\N
2008-11-21 11:17:04.033+00	\N	23894	\N	\N	\N	\N
2008-11-21 11:17:04.22+00	2008-11-21 11:17:04.271+00	23895	\N	\N	\N	\N
2008-11-21 11:17:04.60+00	\N	23897	\N	\N	\N	\N
2008-11-21 11:17:04.66+00	\N	23899	\N	\N	\N	\N
2008-11-21 11:17:04.831+00	2008-11-21 11:17:04.878+00	23900	\N	\N	\N	\N
2008-11-21 11:17:05.362+00	\N	23902	\N	\N	\N	\N
2008-11-21 11:17:05.415+00	\N	23904	\N	\N	\N	\N
2008-11-21 11:17:05.593+00	2008-11-21 11:17:05.644+00	23905	\N	\N	\N	\N
2008-11-21 11:17:06.215+00	\N	23907	\N	\N	\N	\N
2008-11-21 11:17:06.266+00	\N	23909	\N	\N	\N	\N
2008-11-21 11:17:06.448+00	2008-11-21 11:17:06.499+00	23910	\N	\N	\N	\N
2008-11-21 11:17:07.032+00	\N	23912	\N	\N	\N	\N
2008-11-21 11:17:07.087+00	\N	23914	\N	\N	\N	\N
2008-11-21 11:17:07.263+00	2008-11-21 11:17:07.314+00	23915	\N	\N	\N	\N
2008-11-21 11:17:07.445+00	\N	23917	\N	\N	\N	\N
2008-11-21 11:17:07.495+00	\N	23919	\N	\N	\N	\N
2008-11-21 11:17:07.749+00	2008-11-21 11:17:07.799+00	23920	\N	\N	\N	\N
2008-11-21 11:17:08.14+00	\N	23922	\N	\N	\N	\N
2008-11-21 11:17:08.192+00	\N	23924	\N	\N	\N	\N
2008-11-21 11:17:08.367+00	2008-11-21 11:17:08.42+00	23925	\N	\N	\N	\N
2008-11-21 11:17:08.513+00	\N	23927	\N	\N	\N	\N
2008-11-21 11:17:08.568+00	\N	23929	\N	\N	\N	\N
2008-11-21 11:17:08.787+00	2008-11-21 11:17:08.84+00	23930	\N	\N	\N	\N
2008-11-21 11:17:09.423+00	\N	23932	\N	\N	\N	\N
2008-11-21 11:17:09.479+00	\N	23934	\N	\N	\N	\N
2008-11-21 11:17:09.695+00	2008-11-21 11:17:09.747+00	23935	\N	\N	\N	\N
2008-11-21 11:17:09.839+00	\N	23937	\N	\N	\N	\N
2008-11-21 11:17:09.891+00	\N	23939	\N	\N	\N	\N
2008-11-21 11:17:10.112+00	2008-11-21 11:17:10.165+00	23940	\N	\N	\N	\N
2008-11-21 11:17:10.26+00	\N	23942	\N	\N	\N	\N
2008-11-21 11:17:10.312+00	\N	23944	\N	\N	\N	\N
2008-11-21 11:17:10.489+00	2008-11-21 11:17:10.542+00	23945	\N	\N	\N	\N
2008-11-21 11:17:11.156+00	\N	23947	\N	\N	\N	\N
2008-11-21 11:17:11.206+00	\N	23949	\N	\N	\N	\N
2008-11-21 11:17:11.376+00	2008-11-21 11:17:11.43+00	23950	\N	\N	\N	\N
2008-11-21 11:17:11.774+00	\N	23952	\N	\N	\N	\N
2008-11-21 11:17:11.827+00	\N	23954	\N	\N	\N	\N
2008-11-21 11:17:12.004+00	2008-11-21 11:17:12.058+00	23955	\N	\N	\N	\N
2008-11-21 11:17:12.15+00	\N	23957	\N	\N	\N	\N
2008-11-21 11:17:12.201+00	\N	23959	\N	\N	\N	\N
2008-11-21 11:17:12.389+00	2008-11-21 11:17:12.441+00	23960	\N	\N	\N	\N
2008-11-21 11:17:12.70+00	\N	23962	\N	\N	\N	\N
2008-11-21 11:17:12.754+00	\N	23964	\N	\N	\N	\N
2008-11-21 11:17:12.977+00	2008-11-21 11:17:13.031+00	23965	\N	\N	\N	\N
2008-11-21 11:17:13.127+00	\N	23967	\N	\N	\N	\N
2008-11-21 11:17:13.18+00	\N	23969	\N	\N	\N	\N
2008-11-21 11:17:13.358+00	2008-11-21 11:17:13.41+00	23970	\N	\N	\N	\N
2008-11-21 11:17:13.513+00	\N	23972	\N	\N	\N	\N
2008-11-21 11:17:13.567+00	\N	23974	\N	\N	\N	\N
2008-11-21 11:17:13.787+00	2008-11-21 11:17:13.838+00	23975	\N	\N	\N	\N
2008-11-21 11:17:14.015+00	\N	23977	\N	\N	\N	\N
2008-11-21 11:17:14.071+00	\N	23979	\N	\N	\N	\N
2008-11-21 11:17:14.296+00	2008-11-21 11:17:14.35+00	23980	\N	\N	\N	\N
2008-11-21 11:17:14.528+00	\N	23982	\N	\N	\N	\N
2008-11-21 11:17:14.583+00	\N	23984	\N	\N	\N	\N
2008-11-21 11:17:14.771+00	2008-11-21 11:17:14.823+00	23985	\N	\N	\N	\N
2008-11-21 11:17:15.29+00	\N	23987	\N	\N	\N	\N
2008-11-21 11:17:15.345+00	\N	23989	\N	\N	\N	\N
2008-11-21 11:17:15.572+00	2008-11-21 11:17:15.626+00	23990	\N	\N	\N	\N
2008-11-21 11:17:15.846+00	\N	23992	\N	\N	\N	\N
2008-11-21 11:17:15.898+00	\N	23994	\N	\N	\N	\N
2008-11-21 11:17:16.173+00	2008-11-21 11:17:16.227+00	23995	\N	\N	\N	\N
2008-11-21 11:17:16.445+00	\N	23997	\N	\N	\N	\N
2008-11-21 11:17:16.50+00	\N	23999	\N	\N	\N	\N
2008-11-21 11:17:16.728+00	2008-11-21 11:17:16.785+00	24000	\N	\N	\N	\N
2008-11-21 11:17:17.308+00	\N	24002	\N	\N	\N	\N
2008-11-21 11:17:17.361+00	\N	24004	\N	\N	\N	\N
2008-11-21 11:17:17.586+00	2008-11-21 11:17:17.64+00	24005	\N	\N	\N	\N
2008-11-21 11:17:17.779+00	\N	24007	\N	\N	\N	\N
2008-11-21 11:17:17.833+00	\N	24009	\N	\N	\N	\N
2008-11-21 11:17:18.061+00	2008-11-21 11:17:18.116+00	24010	\N	\N	\N	\N
2008-11-21 11:17:18.634+00	\N	24012	\N	\N	\N	\N
2008-11-21 11:17:18.688+00	\N	24014	\N	\N	\N	\N
2008-11-21 11:17:18.877+00	2008-11-21 11:17:18.931+00	24015	\N	\N	\N	\N
2008-11-21 11:17:19.51+00	\N	24017	\N	\N	\N	\N
2008-11-21 11:17:19.564+00	\N	24019	\N	\N	\N	\N
2008-11-21 11:17:19.739+00	2008-11-21 11:17:19.789+00	24020	\N	\N	\N	\N
2008-11-21 11:17:20.126+00	\N	24022	\N	\N	\N	\N
2008-11-21 11:17:20.179+00	\N	24024	\N	\N	\N	\N
2008-11-21 11:17:20.435+00	2008-11-21 11:17:20.487+00	24025	\N	\N	\N	\N
2008-11-21 11:17:20.632+00	\N	24027	\N	\N	\N	\N
2008-11-21 11:17:20.682+00	\N	24029	\N	\N	\N	\N
2008-11-21 11:17:20.988+00	2008-11-21 11:17:21.046+00	24030	\N	\N	\N	\N
2008-11-21 11:17:21.229+00	\N	24032	\N	\N	\N	\N
2008-11-21 11:17:21.284+00	\N	24034	\N	\N	\N	\N
2008-11-21 11:17:21.611+00	2008-11-21 11:17:21.664+00	24035	\N	\N	\N	\N
2008-11-21 11:17:21.928+00	\N	24037	\N	\N	\N	\N
2008-11-21 11:17:21.983+00	\N	24039	\N	\N	\N	\N
2008-11-21 11:17:22.171+00	2008-11-21 11:17:22.227+00	24040	\N	\N	\N	\N
2008-11-21 11:17:22.541+00	\N	24042	\N	\N	\N	\N
2008-11-21 11:17:22.594+00	\N	24044	\N	\N	\N	\N
2008-11-21 11:17:22.788+00	2008-11-21 11:17:22.841+00	24045	\N	\N	\N	\N
2008-11-21 11:17:23.152+00	\N	24047	\N	\N	\N	\N
2008-11-21 11:17:23.207+00	\N	24049	\N	\N	\N	\N
2008-11-21 11:17:23.394+00	2008-11-21 11:17:23.45+00	24050	\N	\N	\N	\N
2008-11-21 11:17:23.764+00	\N	24052	\N	\N	\N	\N
2008-11-21 11:17:23.818+00	\N	24054	\N	\N	\N	\N
2008-11-21 11:17:24.056+00	2008-11-21 11:17:24.109+00	24055	\N	\N	\N	\N
2008-11-21 11:17:24.505+00	\N	24057	\N	\N	\N	\N
2008-11-21 11:17:24.563+00	\N	24059	\N	\N	\N	\N
2008-11-21 11:17:24.838+00	2008-11-21 11:17:24.892+00	24060	\N	\N	\N	\N
2008-11-21 11:17:25.215+00	\N	24062	\N	\N	\N	\N
2008-11-21 11:17:25.269+00	\N	24064	\N	\N	\N	\N
2008-11-21 11:17:25.454+00	2008-11-21 11:17:25.509+00	24065	\N	\N	\N	\N
2008-11-21 11:17:25.739+00	\N	24067	\N	\N	\N	\N
2008-11-21 11:17:25.795+00	\N	24069	\N	\N	\N	\N
2008-11-21 11:17:25.983+00	2008-11-21 11:17:26.041+00	24070	\N	\N	\N	\N
2008-11-21 11:17:26.181+00	\N	24072	\N	\N	\N	\N
2008-11-21 11:17:26.246+00	\N	24074	\N	\N	\N	\N
2008-11-21 11:17:26.518+00	2008-11-21 11:17:26.576+00	24075	\N	\N	\N	\N
2008-11-21 11:17:26.76+00	\N	24077	\N	\N	\N	\N
2008-11-21 11:17:26.816+00	\N	24079	\N	\N	\N	\N
2008-11-21 11:17:27.052+00	2008-11-21 11:17:27.107+00	24080	\N	\N	\N	\N
2008-11-21 11:17:27.397+00	\N	24082	\N	\N	\N	\N
2008-11-21 11:17:27.451+00	\N	24084	\N	\N	\N	\N
2008-11-21 11:17:27.683+00	2008-11-21 11:17:27.739+00	24085	\N	\N	\N	\N
2008-11-21 11:17:27.925+00	\N	24087	\N	\N	\N	\N
2008-11-21 11:17:27.981+00	\N	24089	\N	\N	\N	\N
2008-11-21 11:17:28.172+00	2008-11-21 11:17:28.229+00	24090	\N	\N	\N	\N
2008-11-21 11:17:28.694+00	\N	24092	\N	\N	\N	\N
2008-11-21 11:17:28.747+00	\N	24094	\N	\N	\N	\N
2008-11-21 11:17:28.983+00	2008-11-21 11:17:29.041+00	24095	\N	\N	\N	\N
2008-11-21 11:17:29.318+00	\N	24097	\N	\N	\N	\N
2008-11-21 11:17:29.374+00	\N	24099	\N	\N	\N	\N
2008-11-21 11:17:29.658+00	2008-11-21 11:17:29.717+00	24100	\N	\N	\N	\N
2008-11-21 11:17:29.921+00	\N	24102	\N	\N	\N	\N
2008-11-21 11:17:29.98+00	\N	24104	\N	\N	\N	\N
2008-11-21 11:17:30.269+00	2008-11-21 11:17:30.328+00	24105	\N	\N	\N	\N
2008-11-21 11:17:30.518+00	\N	24107	\N	\N	\N	\N
2008-11-21 11:17:30.575+00	\N	24109	\N	\N	\N	\N
2008-11-21 11:17:30.771+00	2008-11-21 11:17:30.829+00	24110	\N	\N	\N	\N
2008-11-21 11:17:31.214+00	\N	24112	\N	\N	\N	\N
2008-11-21 11:17:31.273+00	\N	24114	\N	\N	\N	\N
2008-11-21 11:17:31.474+00	2008-11-21 11:17:31.533+00	24115	\N	\N	\N	\N
2008-11-21 11:17:32.035+00	\N	24117	\N	\N	\N	\N
2008-11-21 11:17:32.096+00	\N	24119	\N	\N	\N	\N
2008-11-21 11:17:32.296+00	2008-11-21 11:17:32.364+00	24120	\N	\N	\N	\N
2008-11-21 11:17:32.828+00	\N	24122	\N	\N	\N	\N
2008-11-21 11:17:32.886+00	\N	24124	\N	\N	\N	\N
2008-11-21 11:17:33.127+00	2008-11-21 11:17:33.187+00	24125	\N	\N	\N	\N
2008-11-21 11:17:33.622+00	\N	24127	\N	\N	\N	\N
2008-11-21 11:17:33.683+00	\N	24129	\N	\N	\N	\N
2008-11-21 11:17:33.885+00	2008-11-21 11:17:33.945+00	24130	\N	\N	\N	\N
2008-11-21 11:17:34.272+00	\N	24132	\N	\N	\N	\N
2008-11-21 11:17:34.331+00	\N	24134	\N	\N	\N	\N
2008-11-21 11:17:34.53+00	2008-11-21 11:17:34.59+00	24135	\N	\N	\N	\N
2008-11-21 11:17:34.943+00	\N	24137	\N	\N	\N	\N
2008-11-21 11:17:35.003+00	\N	24139	\N	\N	\N	\N
2008-11-21 11:17:35.249+00	2008-11-21 11:17:35.306+00	24140	\N	\N	\N	\N
2008-11-21 11:17:35.633+00	\N	24142	\N	\N	\N	\N
2008-11-21 11:17:35.693+00	\N	24144	\N	\N	\N	\N
2008-11-21 11:17:35.897+00	2008-11-21 11:17:35.968+00	24145	\N	\N	\N	\N
2008-11-21 11:17:36.074+00	\N	24147	\N	\N	\N	\N
2008-11-21 11:17:36.133+00	\N	24149	\N	\N	\N	\N
2008-11-21 11:17:36.336+00	2008-11-21 11:17:36.395+00	24150	\N	\N	\N	\N
2008-11-21 11:17:36.498+00	\N	24152	\N	\N	\N	\N
2008-11-21 11:17:36.555+00	\N	24154	\N	\N	\N	\N
2008-11-21 11:17:36.756+00	2008-11-21 11:17:36.814+00	24155	\N	\N	\N	\N
2008-11-21 11:17:36.917+00	\N	24157	\N	\N	\N	\N
2008-11-21 11:17:36.977+00	\N	24159	\N	\N	\N	\N
2008-11-21 11:17:37.182+00	2008-11-21 11:17:37.242+00	24160	\N	\N	\N	\N
2008-11-21 11:17:37.487+00	\N	24162	\N	\N	\N	\N
2008-11-21 11:17:37.56+00	\N	24164	\N	\N	\N	\N
2008-11-21 11:17:37.764+00	2008-11-21 11:17:37.824+00	24165	\N	\N	\N	\N
2008-11-21 11:17:38.202+00	\N	24167	\N	\N	\N	\N
2008-11-21 11:17:38.261+00	\N	24169	\N	\N	\N	\N
2008-11-21 11:17:38.472+00	2008-11-21 11:17:38.533+00	24170	\N	\N	\N	\N
2008-11-21 11:17:38.795+00	\N	24172	\N	\N	\N	\N
2008-11-21 11:17:38.852+00	\N	24174	\N	\N	\N	\N
2008-11-21 11:17:39.113+00	2008-11-21 11:17:39.17+00	24175	\N	\N	\N	\N
2008-11-21 11:17:39.569+00	\N	24177	\N	\N	\N	\N
2008-11-21 11:17:39.627+00	\N	24179	\N	\N	\N	\N
2008-11-21 11:17:39.834+00	2008-11-21 11:17:39.892+00	24180	\N	\N	\N	\N
2008-11-21 11:17:39.991+00	\N	24182	\N	\N	\N	\N
2008-11-21 11:17:40.051+00	\N	24184	\N	\N	\N	\N
2008-11-21 11:17:40.328+00	2008-11-21 11:17:40.385+00	24185	\N	\N	\N	\N
2008-11-21 11:17:40.501+00	\N	24187	\N	\N	\N	\N
2008-11-21 11:17:40.56+00	\N	24189	\N	\N	\N	\N
2008-11-21 11:17:40.848+00	2008-11-21 11:17:40.906+00	24190	\N	\N	\N	\N
2008-11-21 11:17:41.293+00	\N	24192	\N	\N	\N	\N
2008-11-21 11:17:41.353+00	\N	24194	\N	\N	\N	\N
2008-11-21 11:17:41.599+00	2008-11-21 11:17:41.656+00	24195	\N	\N	\N	\N
2008-11-21 11:17:41.997+00	\N	24197	\N	\N	\N	\N
2008-11-21 11:17:42.057+00	\N	24199	\N	\N	\N	\N
2008-11-21 11:17:42.299+00	2008-11-21 11:17:42.357+00	24200	\N	\N	\N	\N
2008-11-21 11:17:42.649+00	\N	24202	\N	\N	\N	\N
2008-11-21 11:17:42.708+00	\N	24204	\N	\N	\N	\N
2008-11-21 11:17:42.908+00	2008-11-21 11:17:42.965+00	24205	\N	\N	\N	\N
2008-11-21 11:17:43.205+00	\N	24207	\N	\N	\N	\N
2008-11-21 11:17:43.275+00	\N	24209	\N	\N	\N	\N
2008-11-21 11:17:43.517+00	2008-11-21 11:17:43.578+00	24210	\N	\N	\N	\N
2008-11-21 11:17:43.912+00	\N	24212	\N	\N	\N	\N
2008-11-21 11:17:43.972+00	\N	24214	\N	\N	\N	\N
2008-11-21 11:17:44.267+00	2008-11-21 11:17:44.324+00	24215	\N	\N	\N	\N
2008-11-21 11:17:44.668+00	\N	24217	\N	\N	\N	\N
2008-11-21 11:17:44.726+00	\N	24219	\N	\N	\N	\N
2008-11-21 11:17:44.927+00	2008-11-21 11:17:44.986+00	24220	\N	\N	\N	\N
2008-11-21 11:17:45.234+00	\N	24222	\N	\N	\N	\N
2008-11-21 11:17:45.294+00	\N	24224	\N	\N	\N	\N
2008-11-21 11:17:45.496+00	2008-11-21 11:17:45.557+00	24225	\N	\N	\N	\N
2008-11-21 11:17:45.822+00	\N	24227	\N	\N	\N	\N
2008-11-21 11:17:45.881+00	\N	24229	\N	\N	\N	\N
2008-11-21 11:17:46.128+00	2008-11-21 11:17:46.187+00	24230	\N	\N	\N	\N
2008-11-21 11:17:46.295+00	\N	24232	\N	\N	\N	\N
2008-11-21 11:17:46.355+00	\N	24234	\N	\N	\N	\N
2008-11-21 11:17:46.562+00	2008-11-21 11:17:46.622+00	24235	\N	\N	\N	\N
2008-11-21 11:17:46.867+00	\N	24237	\N	\N	\N	\N
2008-11-21 11:17:46.926+00	\N	24239	\N	\N	\N	\N
2008-11-21 11:17:47.127+00	2008-11-21 11:17:47.185+00	24240	\N	\N	\N	\N
2008-11-21 11:17:47.542+00	\N	24242	\N	\N	\N	\N
2008-11-21 11:17:47.602+00	\N	24244	\N	\N	\N	\N
2008-11-21 11:17:47.805+00	2008-11-21 11:17:47.866+00	24245	\N	\N	\N	\N
2008-11-21 11:17:48.07+00	\N	24247	\N	\N	\N	\N
2008-11-21 11:17:48.129+00	\N	24249	\N	\N	\N	\N
2008-11-21 11:17:48.472+00	2008-11-21 11:17:48.53+00	24250	\N	\N	\N	\N
2008-11-21 11:17:48.87+00	\N	24252	\N	\N	\N	\N
2008-11-21 11:17:48.931+00	\N	24254	\N	\N	\N	\N
2008-11-21 11:17:49.147+00	2008-11-21 11:17:49.205+00	24255	\N	\N	\N	\N
2008-11-21 11:17:49.50+00	\N	24257	\N	\N	\N	\N
2008-11-21 11:17:49.564+00	\N	24259	\N	\N	\N	\N
2008-11-21 11:17:49.77+00	2008-11-21 11:17:49.831+00	24260	\N	\N	\N	\N
2008-11-21 11:17:50.222+00	\N	24262	\N	\N	\N	\N
2008-11-21 11:17:50.282+00	\N	24264	\N	\N	\N	\N
2008-11-21 11:17:50.53+00	2008-11-21 11:17:50.594+00	24265	\N	\N	\N	\N
2008-11-21 11:17:50.842+00	\N	24267	\N	\N	\N	\N
2008-11-21 11:17:50.902+00	\N	24269	\N	\N	\N	\N
2008-11-21 11:17:51.108+00	2008-11-21 11:17:51.185+00	24270	\N	\N	\N	\N
2008-11-21 11:17:51.773+00	\N	24272	\N	\N	\N	\N
2008-11-21 11:17:51.835+00	\N	24274	\N	\N	\N	\N
2008-11-21 11:17:52.044+00	2008-11-21 11:17:52.106+00	24275	\N	\N	\N	\N
2008-11-21 11:17:52.406+00	\N	24277	\N	\N	\N	\N
2008-11-21 11:17:52.47+00	\N	24279	\N	\N	\N	\N
2008-11-21 11:17:52.686+00	2008-11-21 11:17:52.749+00	24280	\N	\N	\N	\N
2008-11-21 11:17:53.10+00	\N	24282	\N	\N	\N	\N
2008-11-21 11:17:53.162+00	\N	24284	\N	\N	\N	\N
2008-11-21 11:17:53.387+00	2008-11-21 11:17:53.45+00	24285	\N	\N	\N	\N
2008-11-21 11:17:53.754+00	\N	24287	\N	\N	\N	\N
2008-11-21 11:17:53.815+00	\N	24289	\N	\N	\N	\N
2008-11-21 11:17:54.026+00	2008-11-21 11:17:54.088+00	24290	\N	\N	\N	\N
2008-11-21 11:17:54.388+00	\N	24292	\N	\N	\N	\N
2008-11-21 11:17:54.452+00	\N	24294	\N	\N	\N	\N
2008-11-21 11:17:54.718+00	2008-11-21 11:17:54.782+00	24295	\N	\N	\N	\N
2008-11-21 11:17:55.329+00	\N	24297	\N	\N	\N	\N
2008-11-21 11:17:55.407+00	\N	24299	\N	\N	\N	\N
2008-11-21 11:17:55.623+00	2008-11-21 11:17:55.687+00	24300	\N	\N	\N	\N
2008-11-21 11:17:55.988+00	\N	24302	\N	\N	\N	\N
2008-11-21 11:17:56.051+00	\N	24304	\N	\N	\N	\N
2008-11-21 11:17:56.264+00	2008-11-21 11:17:56.326+00	24305	\N	\N	\N	\N
2008-11-21 11:17:56.487+00	\N	24307	\N	\N	\N	\N
2008-11-21 11:17:56.551+00	\N	24309	\N	\N	\N	\N
2008-11-21 11:17:56.77+00	2008-11-21 11:17:56.834+00	24310	\N	\N	\N	\N
2008-11-21 11:17:57.353+00	\N	24312	\N	\N	\N	\N
2008-11-21 11:17:57.418+00	\N	24314	\N	\N	\N	\N
2008-11-21 11:17:57.684+00	2008-11-21 11:17:57.747+00	24315	\N	\N	\N	\N
2008-11-21 11:17:58.441+00	\N	24317	\N	\N	\N	\N
2008-11-21 11:17:58.507+00	\N	24319	\N	\N	\N	\N
2008-11-21 11:17:58.727+00	2008-11-21 11:17:58.792+00	24320	\N	\N	\N	\N
2008-11-21 11:17:59.624+00	\N	24322	\N	\N	\N	\N
2008-11-21 11:17:59.683+00	\N	24324	\N	\N	\N	\N
2008-11-21 11:17:59.881+00	2008-11-21 11:17:59.939+00	24325	\N	\N	\N	\N
2008-11-21 11:18:00.281+00	\N	24327	\N	\N	\N	\N
2008-11-21 11:18:00.344+00	\N	24329	\N	\N	\N	\N
2008-11-21 11:18:00.563+00	2008-11-21 11:18:00.628+00	24330	\N	\N	\N	\N
2008-11-21 11:18:01.043+00	\N	24332	\N	\N	\N	\N
2008-11-21 11:18:01.108+00	\N	24334	\N	\N	\N	\N
2008-11-21 11:18:01.346+00	2008-11-21 11:18:01.411+00	24335	\N	\N	\N	\N
2008-11-21 11:18:01.722+00	\N	24337	\N	\N	\N	\N
2008-11-21 11:18:01.785+00	\N	24339	\N	\N	\N	\N
2008-11-21 11:18:02.048+00	2008-11-21 11:18:02.111+00	24340	\N	\N	\N	\N
2008-11-21 11:18:02.223+00	\N	24342	\N	\N	\N	\N
2008-11-21 11:18:02.286+00	\N	24344	\N	\N	\N	\N
2008-11-21 11:18:02.558+00	2008-11-21 11:18:02.623+00	24345	\N	\N	\N	\N
2008-11-21 11:18:03.209+00	\N	24347	\N	\N	\N	\N
2008-11-21 11:18:03.274+00	\N	24349	\N	\N	\N	\N
2008-11-21 11:18:03.494+00	2008-11-21 11:18:03.556+00	24350	\N	\N	\N	\N
2008-11-21 11:18:03.765+00	\N	24352	\N	\N	\N	\N
2008-11-21 11:18:03.827+00	\N	24354	\N	\N	\N	\N
2008-11-21 11:18:04.046+00	2008-11-21 11:18:04.112+00	24355	\N	\N	\N	\N
2008-11-21 11:18:04.479+00	\N	24357	\N	\N	\N	\N
2008-11-21 11:18:04.547+00	\N	24359	\N	\N	\N	\N
2008-11-21 11:18:04.79+00	2008-11-21 11:18:04.856+00	24360	\N	\N	\N	\N
2008-11-21 11:18:05.173+00	\N	24362	\N	\N	\N	\N
2008-11-21 11:18:05.246+00	\N	24364	\N	\N	\N	\N
2008-11-21 11:18:05.462+00	2008-11-21 11:18:05.523+00	24365	\N	\N	\N	\N
2008-11-21 11:18:06.016+00	\N	24367	\N	\N	\N	\N
2008-11-21 11:18:06.078+00	\N	24369	\N	\N	\N	\N
2008-11-21 11:18:06.297+00	2008-11-21 11:18:06.375+00	24370	\N	\N	\N	\N
2008-11-21 11:18:06.701+00	\N	24372	\N	\N	\N	\N
2008-11-21 11:18:06.76+00	\N	24374	\N	\N	\N	\N
2008-11-21 11:18:07.075+00	2008-11-21 11:18:07.138+00	24375	\N	\N	\N	\N
2008-11-21 11:18:07.501+00	\N	24377	\N	\N	\N	\N
2008-11-21 11:18:07.567+00	\N	24379	\N	\N	\N	\N
2008-11-21 11:18:07.803+00	2008-11-21 11:18:07.868+00	24380	\N	\N	\N	\N
2008-11-21 11:18:08.184+00	\N	24382	\N	\N	\N	\N
2008-11-21 11:18:08.248+00	\N	24384	\N	\N	\N	\N
2008-11-21 11:18:08.462+00	2008-11-21 11:18:08.524+00	24385	\N	\N	\N	\N
2008-11-21 11:18:09.239+00	\N	24387	\N	\N	\N	\N
2008-11-21 11:18:09.322+00	\N	24389	\N	\N	\N	\N
2008-11-21 11:18:09.54+00	2008-11-21 11:18:09.605+00	24390	\N	\N	\N	\N
2008-11-21 11:18:09.917+00	\N	24392	\N	\N	\N	\N
2008-11-21 11:18:09.98+00	\N	24394	\N	\N	\N	\N
2008-11-21 11:18:10.195+00	2008-11-21 11:18:10.258+00	24395	\N	\N	\N	\N
2008-11-21 11:18:10.475+00	\N	24397	\N	\N	\N	\N
2008-11-21 11:18:10.54+00	\N	24399	\N	\N	\N	\N
2008-11-21 11:18:10.781+00	2008-11-21 11:18:10.847+00	24400	\N	\N	\N	\N
2008-11-21 11:18:11.261+00	\N	24402	\N	\N	\N	\N
2008-11-21 11:18:11.325+00	\N	24404	\N	\N	\N	\N
2008-11-21 11:18:11.54+00	2008-11-21 11:18:11.604+00	24405	\N	\N	\N	\N
2008-11-21 11:18:11.819+00	\N	24407	\N	\N	\N	\N
2008-11-21 11:18:11.885+00	\N	24409	\N	\N	\N	\N
2008-11-21 11:18:12.119+00	2008-11-21 11:18:12.184+00	24410	\N	\N	\N	\N
2008-11-21 11:18:12.652+00	\N	24412	\N	\N	\N	\N
2008-11-21 11:18:12.716+00	\N	24414	\N	\N	\N	\N
2008-11-21 11:18:12.932+00	2008-11-21 11:18:12.996+00	24415	\N	\N	\N	\N
2008-11-21 11:18:13.385+00	\N	24417	\N	\N	\N	\N
2008-11-21 11:18:13.451+00	\N	24419	\N	\N	\N	\N
2008-11-21 11:18:13.673+00	2008-11-21 11:18:13.781+00	24420	\N	\N	\N	\N
2008-11-21 11:18:13.978+00	\N	24422	\N	\N	\N	\N
2008-11-21 11:18:14.04+00	\N	24424	\N	\N	\N	\N
2008-11-21 11:18:14.253+00	2008-11-21 11:18:14.317+00	24425	\N	\N	\N	\N
2008-11-21 11:18:14.483+00	\N	24427	\N	\N	\N	\N
2008-11-21 11:18:14.549+00	\N	24429	\N	\N	\N	\N
2008-11-21 11:18:14.784+00	2008-11-21 11:18:14.848+00	24430	\N	\N	\N	\N
2008-11-21 11:18:14.963+00	\N	24432	\N	\N	\N	\N
2008-11-21 11:18:15.027+00	\N	24434	\N	\N	\N	\N
2008-11-21 11:18:15.306+00	2008-11-21 11:18:15.372+00	24435	\N	\N	\N	\N
2008-11-21 11:18:15.49+00	\N	24437	\N	\N	\N	\N
2008-11-21 11:18:15.558+00	\N	24439	\N	\N	\N	\N
2008-11-21 11:18:15.785+00	2008-11-21 11:18:15.861+00	24440	\N	\N	\N	\N
2008-11-21 11:18:16.182+00	\N	24442	\N	\N	\N	\N
2008-11-21 11:18:16.247+00	\N	24444	\N	\N	\N	\N
2008-11-21 11:18:16.475+00	2008-11-21 11:18:16.544+00	24445	\N	\N	\N	\N
2008-11-21 11:18:16.816+00	\N	24447	\N	\N	\N	\N
2008-11-21 11:18:16.888+00	\N	24449	\N	\N	\N	\N
2008-11-21 11:18:17.132+00	2008-11-21 11:18:17.197+00	24450	\N	\N	\N	\N
2008-11-21 11:18:17.313+00	\N	24452	\N	\N	\N	\N
2008-11-21 11:18:17.378+00	\N	24454	\N	\N	\N	\N
2008-11-21 11:18:17.607+00	2008-11-21 11:18:17.675+00	24455	\N	\N	\N	\N
2008-11-21 11:18:17.999+00	\N	24457	\N	\N	\N	\N
2008-11-21 11:18:18.066+00	\N	24459	\N	\N	\N	\N
2008-11-21 11:18:18.309+00	2008-11-21 11:18:18.375+00	24460	\N	\N	\N	\N
2008-11-21 11:18:18.698+00	\N	24462	\N	\N	\N	\N
2008-11-21 11:18:18.766+00	\N	24464	\N	\N	\N	\N
2008-11-21 11:18:19.048+00	2008-11-21 11:18:19.115+00	24465	\N	\N	\N	\N
2008-11-21 11:18:19.241+00	\N	24467	\N	\N	\N	\N
2008-11-21 11:18:19.306+00	\N	24469	\N	\N	\N	\N
2008-11-21 11:18:19.531+00	2008-11-21 11:18:19.599+00	24470	\N	\N	\N	\N
2008-11-21 11:18:19.975+00	\N	24472	\N	\N	\N	\N
2008-11-21 11:18:20.045+00	\N	24474	\N	\N	\N	\N
2008-11-21 11:18:20.273+00	2008-11-21 11:18:20.352+00	24475	\N	\N	\N	\N
2008-11-21 11:18:20.675+00	\N	24477	\N	\N	\N	\N
2008-11-21 11:18:20.742+00	\N	24479	\N	\N	\N	\N
2008-11-21 11:18:20.972+00	2008-11-21 11:18:21.042+00	24480	\N	\N	\N	\N
2008-11-21 11:18:21.368+00	\N	24482	\N	\N	\N	\N
2008-11-21 11:18:21.433+00	\N	24484	\N	\N	\N	\N
2008-11-21 11:18:21.674+00	2008-11-21 11:18:21.739+00	24485	\N	\N	\N	\N
2008-11-21 11:18:22.117+00	\N	24487	\N	\N	\N	\N
2008-11-21 11:18:22.185+00	\N	24489	\N	\N	\N	\N
2008-11-21 11:18:22.467+00	2008-11-21 11:18:22.538+00	24490	\N	\N	\N	\N
2008-11-21 11:18:22.86+00	\N	24492	\N	\N	\N	\N
2008-11-21 11:18:22.926+00	\N	24494	\N	\N	\N	\N
2008-11-21 11:18:23.167+00	2008-11-21 11:18:23.234+00	24495	\N	\N	\N	\N
2008-11-21 11:18:23.874+00	\N	24497	\N	\N	\N	\N
2008-11-21 11:18:23.943+00	\N	24499	\N	\N	\N	\N
2008-11-21 11:18:24.174+00	2008-11-21 11:18:24.247+00	24500	\N	\N	\N	\N
2008-11-21 11:18:24.79+00	\N	24502	\N	\N	\N	\N
2008-11-21 11:18:24.856+00	\N	24504	\N	\N	\N	\N
2008-11-21 11:18:25.139+00	2008-11-21 11:18:25.208+00	24505	\N	\N	\N	\N
2008-11-21 11:18:25.593+00	\N	24507	\N	\N	\N	\N
2008-11-21 11:18:25.661+00	\N	24509	\N	\N	\N	\N
2008-11-21 11:18:51.956+00	\N	30001	\N	\N	\N	\N
\N	\N	32001	\N	Capture PCR Product into pOPIN vectors	\N	\N
2008-11-21 11:19:01.619+00	\N	32003	\N	\N	\N	\N
2008-11-21 11:19:01.659+00	\N	32005	\N	\N	\N	\N
2008-11-21 11:19:01.682+00	\N	32007	\N	\N	\N	\N
2008-11-21 11:19:01.702+00	\N	32009	\N	\N	\N	\N
2008-11-21 11:19:01.764+00	\N	32011	\N	\N	\N	\N
2008-11-21 11:19:01.811+00	\N	32013	\N	\N	\N	\N
\N	\N	32015	\N	Transform E.coli with plasmid, generic heat shock protocol	\N	\N
2008-11-21 11:19:01.873+00	\N	32017	\N	\N	\N	\N
2008-11-21 11:19:01.892+00	\N	32019	\N	\N	\N	\N
2008-11-21 11:19:01.913+00	\N	32021	\N	\N	\N	\N
2008-11-21 11:19:01.934+00	\N	32023	\N	\N	\N	\N
2008-11-21 11:19:01.956+00	\N	32025	\N	\N	\N	\N
2008-11-21 11:19:01.998+00	\N	32027	\N	\N	\N	\N
2008-11-21 11:19:02.043+00	\N	32029	\N	\N	\N	\N
2008-11-21 11:19:02.09+00	\N	32031	\N	\N	\N	\N
2008-11-21 11:19:02.139+00	\N	32033	\N	\N	\N	\N
2008-11-21 11:19:02.191+00	\N	32035	\N	\N	\N	\N
2008-11-21 11:19:02.30+00	\N	32037	\N	\N	\N	\N
2008-11-21 11:19:02.392+00	\N	32039	\N	\N	\N	\N
\N	\N	32041	\N	Concentration of Target Protein	\N	\N
2008-11-21 11:19:02.516+00	\N	32043	\N	\N	\N	\N
2008-11-21 11:19:02.536+00	\N	32045	\N	\N	\N	\N
2008-11-21 11:19:02.556+00	\N	32047	\N	\N	\N	\N
2008-11-21 11:19:02.623+00	\N	32049	\N	\N	\N	\N
2008-11-21 11:19:02.73+00	\N	32051	\N	\N	\N	\N
2008-11-21 11:19:02.771+00	\N	32053	\N	\N	\N	\N
2008-11-21 11:19:02.816+00	\N	32055	\N	\N	\N	\N
2008-11-21 11:19:02.863+00	\N	32057	\N	\N	\N	\N
2008-11-21 11:19:02.913+00	\N	32059	\N	\N	\N	\N
\N	\N	32061	\N	\N	\N	\N
2008-11-21 11:19:03.242+00	\N	32063	\N	\N	\N	\N
2008-11-21 11:19:03.278+00	\N	32065	\N	\N	\N	\N
2008-11-21 11:19:03.302+00	\N	32067	\N	\N	\N	\N
2008-11-21 11:19:03.326+00	\N	32069	\N	\N	\N	\N
2008-11-21 11:19:03.368+00	\N	32071	\N	\N	\N	\N
2008-11-21 11:19:03.415+00	\N	32073	\N	\N	\N	\N
2008-11-21 11:19:03.464+00	\N	32075	\N	\N	\N	\N
\N	\N	32077	\N	set up one reaction per insert	\N	\N
2008-11-21 11:19:03.533+00	\N	32079	\N	\N	\N	\N
2008-11-21 11:19:03.555+00	\N	32081	\N	\N	\N	\N
2008-11-21 11:19:03.579+00	\N	32083	\N	\N	\N	\N
2008-11-21 11:19:03.605+00	\N	32085	\N	\N	\N	\N
2008-11-21 11:19:03.648+00	\N	32087	\N	\N	\N	\N
2008-11-21 11:19:03.693+00	\N	32089	\N	\N	\N	\N
2008-11-21 11:19:03.74+00	\N	32091	\N	\N	\N	\N
\N	\N	32093	\N	Parameter number of isolated colonies defined for output from protocol	\N	\N
2008-11-21 11:19:03.794+00	\N	32095	\N	\N	\N	\N
2008-11-21 11:19:03.817+00	\N	32097	\N	\N	\N	\N
2008-11-21 11:19:03.856+00	\N	32099	\N	\N	\N	\N
2008-11-21 11:19:03.895+00	\N	32101	\N	\N	\N	\N
2008-11-21 11:19:03.938+00	\N	32103	\N	\N	\N	\N
2008-11-21 11:19:03.984+00	\N	32105	\N	\N	\N	\N
\N	\N	32107	\N	\N	\N	\N
2008-11-21 11:19:04.035+00	\N	32109	\N	\N	\N	\N
2008-11-21 11:19:04.056+00	\N	32111	\N	\N	\N	\N
2008-11-21 11:19:04.082+00	\N	32113	\N	\N	\N	\N
2008-11-21 11:19:04.107+00	\N	32115	\N	\N	\N	\N
2008-11-21 11:19:04.134+00	\N	32117	\N	\N	\N	\N
2008-11-21 11:19:04.165+00	\N	32119	\N	\N	\N	\N
2008-11-21 11:19:04.196+00	\N	32121	\N	\N	\N	\N
2008-11-21 11:19:04.25+00	\N	32123	\N	\N	\N	\N
2008-11-21 11:19:04.306+00	\N	32125	\N	\N	\N	\N
\N	\N	32127	\N	\N	\N	\N
2008-11-21 11:19:04.536+00	\N	32129	\N	\N	\N	\N
2008-11-21 11:19:04.57+00	\N	32131	\N	\N	\N	\N
2008-11-21 11:19:04.607+00	\N	32133	\N	\N	\N	\N
2008-11-21 11:19:04.646+00	\N	32135	\N	\N	\N	\N
2008-11-21 11:19:04.713+00	\N	32137	\N	\N	\N	\N
2008-11-21 11:19:04.78+00	\N	32139	\N	\N	\N	\N
2008-11-21 11:19:04.851+00	\N	32141	\N	\N	\N	\N
2008-11-21 11:19:04.925+00	\N	32143	\N	\N	\N	\N
\N	\N	32145	\N	\N	\N	\N
2008-11-21 11:19:05.032+00	\N	32147	\N	\N	\N	\N
2008-11-21 11:19:05.054+00	\N	32149	\N	\N	\N	\N
2008-11-21 11:19:05.084+00	\N	32151	\N	\N	\N	\N
2008-11-21 11:19:05.111+00	\N	32153	\N	\N	\N	\N
2008-11-21 11:19:05.141+00	\N	32155	\N	\N	\N	\N
2008-11-21 11:19:05.17+00	\N	32157	\N	\N	\N	\N
2008-11-21 11:19:05.204+00	\N	32159	\N	\N	\N	\N
2008-11-21 11:19:05.241+00	\N	32161	\N	\N	\N	\N
2008-11-21 11:19:05.281+00	\N	32163	\N	\N	\N	\N
2008-11-21 11:19:05.349+00	\N	32165	\N	\N	\N	\N
2008-11-21 11:19:05.419+00	\N	32167	\N	\N	\N	\N
2008-11-21 11:19:05.491+00	\N	32169	\N	\N	\N	\N
2008-11-21 11:19:05.566+00	\N	32171	\N	\N	\N	\N
2008-11-21 11:19:05.644+00	\N	32173	\N	\N	\N	\N
2008-11-21 11:19:05.724+00	\N	32175	\N	\N	\N	\N
2008-11-21 11:19:05.808+00	\N	32177	\N	\N	\N	\N
\N	\N	32179	\N	Use buffers and reagents from Qiagen kit.	\N	\N
2008-11-21 11:19:05.933+00	\N	32181	\N	\N	\N	\N
2008-11-21 11:19:05.957+00	\N	32183	\N	\N	\N	\N
2008-11-21 11:19:05.985+00	\N	32185	\N	\N	\N	\N
2008-11-21 11:19:06.017+00	\N	32187	\N	\N	\N	\N
2008-11-21 11:19:06.051+00	\N	32189	\N	\N	\N	\N
2008-11-21 11:19:06.088+00	\N	32191	\N	\N	\N	\N
2008-11-21 11:19:06.122+00	\N	32193	\N	\N	\N	\N
2008-11-21 11:19:06.191+00	\N	32195	\N	\N	\N	\N
2008-11-21 11:19:06.26+00	\N	32197	\N	\N	\N	\N
2008-11-21 11:19:06.332+00	\N	32199	\N	\N	\N	\N
2008-11-21 11:19:06.406+00	\N	32201	\N	\N	\N	\N
\N	\N	32203	\N	\N	\N	\N
2008-11-21 11:19:06.478+00	\N	32205	\N	\N	\N	\N
2008-11-21 11:19:06.504+00	\N	32207	\N	\N	\N	\N
2008-11-21 11:19:06.534+00	\N	32209	\N	\N	\N	\N
2008-11-21 11:19:06.565+00	\N	32211	\N	\N	\N	\N
2008-11-21 11:19:06.605+00	\N	32213	\N	\N	\N	\N
2008-11-21 11:19:06.642+00	\N	32215	\N	\N	\N	\N
2008-11-21 11:19:06.682+00	\N	32217	\N	\N	\N	\N
2008-11-21 11:19:06.728+00	\N	32219	\N	\N	\N	\N
2008-11-21 11:19:06.792+00	\N	32221	\N	\N	\N	\N
2008-11-21 11:19:06.858+00	\N	32223	\N	\N	\N	\N
2008-11-21 11:19:06.926+00	\N	32225	\N	\N	\N	\N
2008-11-21 11:19:06.995+00	\N	32227	\N	\N	\N	\N
2008-11-21 11:19:07.067+00	\N	32229	\N	\N	\N	\N
\N	\N	32231	\N	Use buffers and reagents from Qiagen kit.	\N	\N
2008-11-21 11:19:07.179+00	\N	32233	\N	\N	\N	\N
2008-11-21 11:19:07.206+00	\N	32235	\N	\N	\N	\N
2008-11-21 11:19:07.236+00	\N	32237	\N	\N	\N	\N
2008-11-21 11:19:07.267+00	\N	32239	\N	\N	\N	\N
2008-11-21 11:19:07.304+00	\N	32241	\N	\N	\N	\N
2008-11-21 11:19:07.347+00	\N	32243	\N	\N	\N	\N
2008-11-21 11:19:07.418+00	\N	32245	\N	\N	\N	\N
2008-11-21 11:19:07.554+00	\N	32247	\N	\N	\N	\N
2008-11-21 11:19:07.691+00	\N	32249	\N	\N	\N	\N
2008-11-21 11:19:07.856+00	\N	32251	\N	\N	\N	\N
2008-11-21 11:19:07.991+00	\N	32253	\N	\N	\N	\N
\N	\N	32255	\N	Parameter number of isolated colonies defined for output from protocol	\N	\N
2008-11-21 11:19:08.417+00	\N	32257	\N	\N	\N	\N
2008-11-21 11:19:08.434+00	\N	32259	\N	\N	\N	\N
2008-11-21 11:19:08.461+00	\N	32261	\N	\N	\N	\N
2008-11-21 11:19:08.486+00	\N	32263	\N	\N	\N	\N
2008-11-21 11:19:08.511+00	\N	32265	\N	\N	\N	\N
2008-11-21 11:19:08.537+00	\N	32267	\N	\N	\N	\N
\N	\N	32269	\N	\N	\N	\N
2008-11-21 11:19:08.576+00	\N	32271	\N	\N	\N	\N
2008-11-21 11:19:08.592+00	\N	32273	\N	\N	\N	\N
2008-11-21 11:19:08.607+00	\N	32275	\N	\N	\N	\N
2008-11-21 11:19:08.625+00	\N	32277	\N	\N	\N	\N
2008-11-21 11:19:08.655+00	\N	32279	\N	\N	\N	\N
2008-11-21 11:19:08.684+00	\N	32281	\N	\N	\N	\N
2008-11-21 11:19:08.713+00	\N	32283	\N	\N	\N	\N
2008-11-21 11:19:08.744+00	\N	32285	\N	\N	\N	\N
\N	\N	32287	\N	\N	\N	\N
2008-11-21 11:19:08.783+00	\N	32289	\N	\N	\N	\N
2008-11-21 11:19:08.799+00	\N	32291	\N	\N	\N	\N
2008-11-21 11:19:08.816+00	\N	32293	\N	\N	\N	\N
2008-11-21 11:19:08.842+00	\N	32295	\N	\N	\N	\N
2008-11-21 11:19:08.866+00	\N	32297	\N	\N	\N	\N
2008-11-21 11:19:08.891+00	\N	32299	\N	\N	\N	\N
2008-11-21 11:19:08.917+00	\N	32301	\N	\N	\N	\N
2008-11-21 11:19:08.942+00	\N	32303	\N	\N	\N	\N
2008-11-21 11:19:08.968+00	\N	32305	\N	\N	\N	\N
2008-11-21 11:19:08.995+00	\N	32307	\N	\N	\N	\N
2008-11-21 11:19:09.022+00	\N	32309	\N	\N	\N	\N
2008-11-21 11:19:09.05+00	\N	32311	\N	\N	\N	\N
2008-11-21 11:19:09.076+00	\N	32313	\N	\N	\N	\N
2008-11-21 11:19:09.102+00	\N	32315	\N	\N	\N	\N
2008-11-21 11:19:09.129+00	\N	32317	\N	\N	\N	\N
2008-11-21 11:19:09.157+00	\N	32319	\N	\N	\N	\N
2008-11-21 11:19:09.185+00	\N	32321	\N	\N	\N	\N
2008-11-21 11:19:09.213+00	\N	32323	\N	\N	\N	\N
2008-11-21 11:19:09.241+00	\N	32325	\N	\N	\N	\N
2008-11-21 11:19:09.27+00	\N	32327	\N	\N	\N	\N
\N	\N	32329	\N	\N	\N	\N
2008-11-21 11:19:19.613+00	\N	34003	\N	\N	\N	\N
2008-11-21 11:19:19.677+00	\N	34005	\N	\N	\N	\N
2008-11-21 11:19:19.716+00	\N	34007	\N	\N	\N	\N
2008-11-21 11:19:19.752+00	\N	34009	\N	\N	\N	\N
2008-11-21 11:19:19.791+00	\N	34011	\N	\N	\N	\N
2008-11-21 11:19:19.833+00	\N	34013	\N	\N	\N	\N
2008-11-21 11:19:19.877+00	\N	34015	\N	\N	\N	\N
2008-11-21 11:19:19.924+00	\N	34017	\N	\N	\N	\N
2008-11-21 11:19:19.974+00	\N	34019	\N	\N	\N	\N
2008-11-21 11:19:20.027+00	\N	34021	\N	\N	\N	\N
2008-11-21 11:19:20.064+00	\N	34023	\N	\N	\N	\N
2008-11-21 11:19:20.122+00	\N	34025	\N	\N	\N	\N
\N	\N	34027	\N	\N	\N	\N
2008-11-21 11:19:20.202+00	\N	34029	\N	\N	\N	\N
2008-11-21 11:19:20.239+00	\N	34031	\N	\N	\N	\N
2008-11-21 11:19:20.263+00	\N	34033	\N	\N	\N	\N
2008-11-21 11:19:20.307+00	\N	34035	\N	\N	\N	\N
2008-11-21 11:19:20.352+00	\N	34037	\N	\N	\N	\N
\N	\N	34039	\N	\N	\N	\N
2008-11-21 11:19:20.401+00	\N	34041	\N	\N	\N	\N
2008-11-21 11:19:20.425+00	\N	34043	\N	\N	\N	\N
2008-11-21 11:19:20.464+00	\N	34045	\N	\N	\N	\N
\N	\N	34047	\N	\N	\N	\N
2008-11-21 11:19:20.534+00	\N	34049	\N	\N	\N	\N
2008-11-21 11:19:20.567+00	\N	34051	\N	\N	\N	\N
2008-11-21 11:19:20.59+00	\N	34053	\N	\N	\N	\N
2008-11-21 11:19:20.614+00	\N	34055	\N	\N	\N	\N
2008-11-21 11:19:20.64+00	\N	34057	\N	\N	\N	\N
2008-11-21 11:19:20.666+00	\N	34059	\N	\N	\N	\N
2008-11-21 11:19:20.694+00	\N	34061	\N	\N	\N	\N
2008-11-21 11:19:20.723+00	\N	34063	\N	\N	\N	\N
2008-11-21 11:19:20.753+00	\N	34065	\N	\N	\N	\N
2008-11-21 11:19:20.785+00	\N	34067	\N	\N	\N	\N
2008-11-21 11:19:20.818+00	\N	34069	\N	\N	\N	\N
2008-11-21 11:19:20.861+00	\N	34071	\N	\N	\N	\N
2008-11-21 11:19:20.907+00	\N	34073	\N	\N	\N	\N
2008-11-21 11:19:20.948+00	\N	34075	\N	\N	\N	\N
2008-11-21 11:19:20.992+00	\N	34077	\N	\N	\N	\N
2008-11-21 11:19:21.083+00	\N	34079	\N	\N	\N	\N
2008-11-21 11:19:21.17+00	\N	34081	\N	\N	\N	\N
2008-11-21 11:19:21.261+00	\N	34083	\N	\N	\N	\N
\N	\N	34085	\N	\N	\N	\N
2008-11-21 11:19:22.219+00	\N	34087	\N	\N	\N	\N
2008-11-21 11:19:22.253+00	\N	34089	\N	\N	\N	\N
2008-11-21 11:19:22.289+00	\N	34091	\N	\N	\N	\N
2008-11-21 11:19:22.328+00	\N	34093	\N	\N	\N	\N
2008-11-21 11:19:22.368+00	\N	34095	\N	\N	\N	\N
2008-11-21 11:19:22.412+00	\N	34097	\N	\N	\N	\N
2008-11-21 11:19:22.457+00	\N	34099	\N	\N	\N	\N
2008-11-21 11:19:22.506+00	\N	34101	\N	\N	\N	\N
2008-11-21 11:19:22.562+00	\N	34103	\N	\N	\N	\N
2008-11-21 11:19:22.618+00	\N	34105	\N	\N	\N	\N
2008-11-21 11:19:22.676+00	\N	34107	\N	\N	\N	\N
2008-11-21 11:19:22.735+00	\N	34109	\N	\N	\N	\N
\N	\N	36001	\N	\N	\N	\N
2008-11-21 11:19:32.36+00	\N	36003	\N	\N	\N	\N
2008-11-21 11:19:32.39+00	\N	36005	\N	\N	\N	\N
2008-11-21 11:19:32.411+00	\N	36007	\N	\N	\N	\N
2008-11-21 11:19:32.498+00	\N	36009	\N	\N	\N	\N
2008-11-21 11:19:32.549+00	\N	36011	\N	\N	\N	\N
2008-11-21 11:19:32.599+00	\N	36013	\N	\N	\N	\N
2008-11-21 11:19:32.651+00	\N	36015	\N	\N	\N	\N
2008-11-21 11:19:32.706+00	\N	36017	\N	\N	\N	\N
2008-11-21 11:19:32.764+00	\N	36019	\N	\N	\N	\N
\N	\N	36021	\N	\N	\N	\N
2008-11-21 11:19:32.831+00	\N	36023	\N	\N	\N	\N
2008-11-21 11:19:32.853+00	\N	36025	\N	\N	\N	\N
2008-11-21 11:19:32.878+00	\N	36027	\N	\N	\N	\N
2008-11-21 11:19:32.933+00	\N	36029	\N	\N	\N	\N
2008-11-21 11:19:32.984+00	\N	36031	\N	\N	\N	\N
2008-11-21 11:19:33.039+00	\N	36033	\N	\N	\N	\N
2008-11-21 11:19:33.093+00	\N	36035	\N	\N	\N	\N
2008-11-21 11:19:33.147+00	\N	36037	\N	\N	\N	\N
2008-11-21 11:19:33.203+00	\N	36039	\N	\N	\N	\N
2008-11-21 11:19:33.266+00	\N	36041	\N	\N	\N	\N
2008-11-21 11:19:33.329+00	\N	36043	\N	\N	\N	\N
\N	\N	36045	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2008-11-21 11:19:33.39+00	\N	36047	\N	\N	\N	\N
2008-11-21 11:19:33.422+00	\N	36049	\N	\N	\N	\N
2008-11-21 11:19:33.461+00	\N	36051	\N	\N	\N	\N
\N	\N	36053	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2008-11-21 11:19:33.504+00	\N	36055	\N	\N	\N	\N
2008-11-21 11:19:33.524+00	\N	36057	\N	\N	\N	\N
2008-11-21 11:19:33.563+00	\N	36059	\N	\N	\N	\N
\N	\N	36061	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2008-11-21 11:19:33.606+00	\N	36063	\N	\N	\N	\N
2008-11-21 11:19:33.634+00	\N	36065	\N	\N	\N	\N
2008-11-21 11:19:33.683+00	\N	36067	\N	\N	\N	\N
\N	\N	36069	\N	Dilute the primers before PCR	\N	\N
2008-11-21 11:19:33.732+00	\N	36071	\N	\N	\N	\N
2008-11-21 11:19:33.752+00	\N	36073	\N	\N	\N	\N
2008-11-21 11:19:33.776+00	\N	36075	\N	\N	\N	\N
2008-11-21 11:19:33.822+00	\N	36077	\N	\N	\N	\N
\N	\N	36079	\N	Dilute the primers before PCR	\N	\N
2008-11-21 11:19:33.868+00	\N	36081	\N	\N	\N	\N
2008-11-21 11:19:33.887+00	\N	36083	\N	\N	\N	\N
2008-11-21 11:19:33.908+00	\N	36085	\N	\N	\N	\N
2008-11-21 11:19:33.946+00	\N	36087	\N	\N	\N	\N
\N	\N	36089	\N	\N	\N	\N
2008-11-21 11:19:33.984+00	\N	36091	\N	\N	\N	\N
2008-11-21 11:19:34+00	\N	36093	\N	\N	\N	\N
2008-11-21 11:19:34.02+00	\N	36095	\N	\N	\N	\N
\N	\N	36097	\N	PCR performed using high-fidelity enzymes	\N	\N
2008-11-21 11:19:34.062+00	\N	36099	\N	\N	\N	\N
2008-11-21 11:19:34.084+00	\N	36101	\N	\N	\N	\N
2008-11-21 11:19:34.108+00	\N	36103	\N	\N	\N	\N
2008-11-21 11:19:34.134+00	\N	36105	\N	\N	\N	\N
2008-11-21 11:19:34.159+00	\N	36107	\N	\N	\N	\N
2008-11-21 11:19:34.187+00	\N	36109	\N	\N	\N	\N
2008-11-21 11:19:34.217+00	\N	36111	\N	\N	\N	\N
2008-11-21 11:19:34.244+00	\N	36113	\N	\N	\N	\N
2008-11-21 11:19:34.276+00	\N	36115	\N	\N	\N	\N
2008-11-21 11:19:34.371+00	\N	36117	\N	\N	\N	\N
2008-11-21 11:19:34.419+00	\N	36119	\N	\N	\N	\N
2008-11-21 11:19:34.467+00	\N	36121	\N	\N	\N	\N
2008-11-21 11:19:34.517+00	\N	36123	\N	\N	\N	\N
2008-11-21 11:19:34.567+00	\N	36125	\N	\N	\N	\N
2008-11-21 11:19:34.619+00	\N	36127	\N	\N	\N	\N
2008-11-21 11:19:34.673+00	\N	36129	\N	\N	\N	\N
2008-11-21 11:19:34.727+00	\N	36131	\N	\N	\N	\N
2008-11-21 11:19:34.782+00	\N	36133	\N	\N	\N	\N
2008-11-21 11:19:34.839+00	\N	36135	\N	\N	\N	\N
2008-11-21 11:19:34.897+00	\N	36137	\N	\N	\N	\N
2008-11-21 11:19:35.291+00	\N	36139	\N	\N	\N	\N
2008-11-21 11:19:35.331+00	\N	36141	\N	\N	\N	\N
\N	\N	36143	\N	Purify PCR product	\N	\N
2008-11-21 11:19:35.374+00	\N	36145	\N	\N	\N	\N
2008-11-21 11:19:35.394+00	\N	36147	\N	\N	\N	\N
2008-11-21 11:19:35.414+00	\N	36149	\N	\N	\N	\N
2008-11-21 11:19:35.439+00	\N	36151	\N	\N	\N	\N
\N	\N	36153	\N	Capture PCR Product into pOPIN vectors	\N	\N
2008-11-21 11:19:35.468+00	\N	36155	\N	\N	\N	\N
2008-11-21 11:19:35.48+00	\N	36157	\N	\N	\N	\N
2008-11-21 11:19:35.498+00	\N	36159	\N	\N	\N	\N
2008-11-21 11:19:35.523+00	\N	36161	\N	\N	\N	\N
2008-11-21 11:19:35.547+00	\N	36163	\N	\N	\N	\N
\N	\N	36165	\N	Transform E.coli with plasmid, generic heat shock protocol	\N	\N
2008-11-21 11:19:35.578+00	\N	36167	\N	\N	\N	\N
2008-11-21 11:19:35.601+00	\N	36169	\N	\N	\N	\N
2008-11-21 11:19:35.617+00	\N	36171	\N	\N	\N	\N
2008-11-21 11:19:35.635+00	\N	36173	\N	\N	\N	\N
2008-11-21 11:19:35.666+00	\N	36175	\N	\N	\N	\N
2008-11-21 11:19:35.693+00	\N	36177	\N	\N	\N	\N
2008-11-21 11:19:35.719+00	\N	36179	\N	\N	\N	\N
2008-11-21 11:19:35.747+00	\N	36181	\N	\N	\N	\N
2008-11-21 11:19:35.775+00	\N	36183	\N	\N	\N	\N
2008-11-21 11:19:35.802+00	\N	36185	\N	\N	\N	\N
2008-11-21 11:19:35.829+00	\N	36187	\N	\N	\N	\N
\N	\N	36189	\N	Purify plasmid from culture	\N	\N
2008-11-21 11:19:35.864+00	\N	36191	\N	\N	\N	\N
2008-11-21 11:19:35.884+00	\N	36193	\N	\N	\N	\N
2008-11-21 11:19:35.903+00	\N	36195	\N	\N	\N	\N
2008-11-21 11:19:35.921+00	\N	36197	\N	\N	\N	\N
2008-11-21 11:19:35.951+00	\N	36199	\N	\N	\N	\N
2008-11-21 11:19:35.976+00	\N	36201	\N	\N	\N	\N
2008-11-21 11:19:36.002+00	\N	36203	\N	\N	\N	\N
\N	\N	36205	\N	Verify the target DNA	\N	\N
2008-11-21 11:19:36.033+00	\N	36207	\N	\N	\N	\N
2008-11-21 11:19:36.053+00	\N	36209	\N	\N	\N	\N
2008-11-21 11:19:36.074+00	\N	36211	\N	\N	\N	\N
2008-11-21 11:19:36.093+00	\N	36213	\N	\N	\N	\N
2008-11-21 11:19:36.123+00	\N	36215	\N	\N	\N	\N
2008-11-21 11:19:36.149+00	\N	36217	\N	\N	\N	\N
\N	\N	36219	\N	Small Scale Expression of Target Protein	\N	\N
2008-11-21 11:19:36.183+00	\N	36221	\N	\N	\N	\N
2008-11-21 11:19:36.203+00	\N	36223	\N	\N	\N	\N
2008-11-21 11:19:36.224+00	\N	36225	\N	\N	\N	\N
2008-11-21 11:19:36.245+00	\N	36227	\N	\N	\N	\N
2008-11-21 11:19:36.265+00	\N	36229	\N	\N	\N	\N
2008-11-21 11:19:36.283+00	\N	36231	\N	\N	\N	\N
2008-11-21 11:19:36.314+00	\N	36233	\N	\N	\N	\N
2008-11-21 11:19:36.343+00	\N	36235	\N	\N	\N	\N
2008-11-21 11:19:36.362+00	\N	36237	\N	\N	\N	\N
2008-11-21 11:19:36.968+00	\N	36239	\N	\N	\N	\N
2008-11-21 11:19:36.998+00	\N	36241	\N	\N	\N	\N
\N	\N	36243	\N	Sequence target DNA	\N	\N
2008-11-21 11:19:37.034+00	\N	36245	\N	\N	\N	\N
2008-11-21 11:19:37.056+00	\N	36247	\N	\N	\N	\N
2008-11-21 11:19:37.077+00	\N	36249	\N	\N	\N	\N
2008-11-21 11:19:37.10+00	\N	36251	\N	\N	\N	\N
2008-11-21 11:19:37.125+00	\N	36253	\N	\N	\N	\N
\N	\N	36255	\N	Large Scale Expression of Target Protein	\N	\N
2008-11-21 11:19:37.158+00	\N	36257	\N	\N	\N	\N
2008-11-21 11:19:37.17+00	\N	36259	\N	\N	\N	\N
2008-11-21 11:19:37.19+00	\N	36261	\N	\N	\N	\N
2008-11-21 11:19:37.209+00	\N	36263	\N	\N	\N	\N
2008-11-21 11:19:37.228+00	\N	36265	\N	\N	\N	\N
2008-11-21 11:19:37.241+00	\N	36267	\N	\N	\N	\N
2008-11-21 11:19:37.269+00	\N	36269	\N	\N	\N	\N
2008-11-21 11:19:37.298+00	\N	36271	\N	\N	\N	\N
2008-11-21 11:19:37.323+00	\N	36273	\N	\N	\N	\N
2008-11-21 11:19:37.348+00	\N	36275	\N	\N	\N	\N
2008-11-21 11:19:37.373+00	\N	36277	\N	\N	\N	\N
2008-11-21 11:19:37.399+00	\N	36279	\N	\N	\N	\N
2008-11-21 11:19:37.425+00	\N	36281	\N	\N	\N	\N
\N	\N	36283	\N	\N	\N	\N
2008-11-21 11:19:37.459+00	\N	36285	\N	\N	\N	\N
2008-11-21 11:19:37.471+00	\N	36287	\N	\N	\N	\N
2008-11-21 11:19:37.484+00	\N	36289	\N	\N	\N	\N
2008-11-21 11:19:37.501+00	\N	36291	\N	\N	\N	\N
2008-11-21 11:19:37.514+00	\N	36293	\N	\N	\N	\N
2008-11-21 11:19:37.526+00	\N	36295	\N	\N	\N	\N
2008-11-21 11:19:37.547+00	\N	36297	\N	\N	\N	\N
2008-11-21 11:19:37.57+00	\N	36299	\N	\N	\N	\N
2008-11-21 11:19:37.591+00	\N	36301	\N	\N	\N	\N
\N	\N	36303	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:37.623+00	\N	36305	\N	\N	\N	\N
2008-11-21 11:19:37.636+00	\N	36307	\N	\N	\N	\N
2008-11-21 11:19:37.653+00	\N	36309	\N	\N	\N	\N
2008-11-21 11:19:37.664+00	\N	36311	\N	\N	\N	\N
2008-11-21 11:19:37.675+00	\N	36313	\N	\N	\N	\N
2008-11-21 11:19:37.698+00	\N	36315	\N	\N	\N	\N
2008-11-21 11:19:37.72+00	\N	36317	\N	\N	\N	\N
2008-11-21 11:19:37.741+00	\N	36319	\N	\N	\N	\N
2008-11-21 11:19:37.762+00	\N	36321	\N	\N	\N	\N
2008-11-21 11:19:37.784+00	\N	36323	\N	\N	\N	\N
2008-11-21 11:19:37.806+00	\N	36325	\N	\N	\N	\N
2008-11-21 11:19:37.828+00	\N	36327	\N	\N	\N	\N
\N	\N	36397	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:37.865+00	\N	36329	\N	\N	\N	\N
2008-11-21 11:19:37.886+00	\N	36331	\N	\N	\N	\N
2008-11-21 11:19:37.91+00	\N	36333	\N	\N	\N	\N
2008-11-21 11:19:37.932+00	\N	36335	\N	\N	\N	\N
2008-11-21 11:19:37.954+00	\N	36337	\N	\N	\N	\N
2008-11-21 11:19:37.976+00	\N	36339	\N	\N	\N	\N
2008-11-21 11:19:37.999+00	\N	36341	\N	\N	\N	\N
2008-11-21 11:19:38.05+00	\N	36343	\N	\N	\N	\N
2008-11-21 11:19:38.074+00	\N	36345	\N	\N	\N	\N
2008-11-21 11:19:38.097+00	\N	36347	\N	\N	\N	\N
\N	\N	36349	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:38.163+00	\N	36351	\N	\N	\N	\N
2008-11-21 11:19:38.176+00	\N	36353	\N	\N	\N	\N
2008-11-21 11:19:38.193+00	\N	36355	\N	\N	\N	\N
2008-11-21 11:19:38.203+00	\N	36357	\N	\N	\N	\N
2008-11-21 11:19:38.214+00	\N	36359	\N	\N	\N	\N
2008-11-21 11:19:38.236+00	\N	36361	\N	\N	\N	\N
2008-11-21 11:19:38.256+00	\N	36363	\N	\N	\N	\N
2008-11-21 11:19:38.276+00	\N	36365	\N	\N	\N	\N
2008-11-21 11:19:38.296+00	\N	36367	\N	\N	\N	\N
2008-11-21 11:19:38.316+00	\N	36369	\N	\N	\N	\N
2008-11-21 11:19:38.337+00	\N	36371	\N	\N	\N	\N
2008-11-21 11:19:38.357+00	\N	36373	\N	\N	\N	\N
2008-11-21 11:19:38.378+00	\N	36375	\N	\N	\N	\N
2008-11-21 11:19:38.399+00	\N	36377	\N	\N	\N	\N
2008-11-21 11:19:38.42+00	\N	36379	\N	\N	\N	\N
2008-11-21 11:19:38.441+00	\N	36381	\N	\N	\N	\N
2008-11-21 11:19:38.463+00	\N	36383	\N	\N	\N	\N
2008-11-21 11:19:38.484+00	\N	36385	\N	\N	\N	\N
2008-11-21 11:19:38.505+00	\N	36387	\N	\N	\N	\N
2008-11-21 11:19:38.527+00	\N	36389	\N	\N	\N	\N
2008-11-21 11:19:38.55+00	\N	36391	\N	\N	\N	\N
2008-11-21 11:19:38.572+00	\N	36393	\N	\N	\N	\N
2008-11-21 11:19:38.595+00	\N	36395	\N	\N	\N	\N
\N	\N	36493	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:40.374+00	\N	36495	\N	\N	\N	\N
2008-11-21 11:19:40.387+00	\N	36497	\N	\N	\N	\N
2008-11-21 11:19:40.415+00	\N	36499	\N	\N	\N	\N
2008-11-21 11:19:40.424+00	\N	36501	\N	\N	\N	\N
2008-11-21 11:19:40.434+00	\N	36503	\N	\N	\N	\N
2008-11-21 11:19:40.455+00	\N	36505	\N	\N	\N	\N
2008-11-21 11:19:40.474+00	\N	36507	\N	\N	\N	\N
2008-11-21 11:19:40.489+00	\N	36509	\N	\N	\N	\N
2008-11-21 11:19:40.504+00	\N	36511	\N	\N	\N	\N
2008-11-21 11:19:40.531+00	\N	36513	\N	\N	\N	\N
2008-11-21 11:19:40.551+00	\N	36515	\N	\N	\N	\N
2008-11-21 11:19:40.571+00	\N	36517	\N	\N	\N	\N
2008-11-21 11:19:40.591+00	\N	36519	\N	\N	\N	\N
2008-11-21 11:19:40.61+00	\N	36521	\N	\N	\N	\N
2008-11-21 11:19:40.63+00	\N	36523	\N	\N	\N	\N
2008-11-21 11:19:40.651+00	\N	36525	\N	\N	\N	\N
2008-11-21 11:19:40.671+00	\N	36527	\N	\N	\N	\N
2008-11-21 11:19:40.691+00	\N	36529	\N	\N	\N	\N
2008-11-21 11:19:40.711+00	\N	36531	\N	\N	\N	\N
2008-11-21 11:19:40.732+00	\N	36533	\N	\N	\N	\N
2008-11-21 11:19:40.752+00	\N	36535	\N	\N	\N	\N
2008-11-21 11:19:40.773+00	\N	36537	\N	\N	\N	\N
2008-11-21 11:19:40.794+00	\N	36539	\N	\N	\N	\N
\N	\N	36541	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:40.823+00	\N	36543	\N	\N	\N	\N
2008-11-21 11:19:40.835+00	\N	36545	\N	\N	\N	\N
2008-11-21 11:19:40.853+00	\N	36547	\N	\N	\N	\N
2008-11-21 11:19:40.862+00	\N	36549	\N	\N	\N	\N
2008-11-21 11:19:40.885+00	\N	36551	\N	\N	\N	\N
2008-11-21 11:19:40.904+00	\N	36553	\N	\N	\N	\N
2008-11-21 11:19:40.923+00	\N	36555	\N	\N	\N	\N
2008-11-21 11:19:40.942+00	\N	36557	\N	\N	\N	\N
2008-11-21 11:19:40.961+00	\N	36559	\N	\N	\N	\N
2008-11-21 11:19:40.981+00	\N	36561	\N	\N	\N	\N
2008-11-21 11:19:41+00	\N	36563	\N	\N	\N	\N
2008-11-21 11:19:41.02+00	\N	36565	\N	\N	\N	\N
2008-11-21 11:19:41.04+00	\N	36567	\N	\N	\N	\N
2008-11-21 11:19:41.06+00	\N	36569	\N	\N	\N	\N
2008-11-21 11:19:41.08+00	\N	36571	\N	\N	\N	\N
2008-11-21 11:19:41.10+00	\N	36573	\N	\N	\N	\N
2008-11-21 11:19:41.121+00	\N	36575	\N	\N	\N	\N
2008-11-21 11:19:41.141+00	\N	36577	\N	\N	\N	\N
2008-11-21 11:19:41.162+00	\N	36579	\N	\N	\N	\N
2008-11-21 11:19:41.183+00	\N	36581	\N	\N	\N	\N
2008-11-21 11:19:41.208+00	\N	36583	\N	\N	\N	\N
\N	\N	36585	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:41.236+00	\N	36587	\N	\N	\N	\N
2008-11-21 11:19:41.25+00	\N	36589	\N	\N	\N	\N
2008-11-21 11:19:41.269+00	\N	36591	\N	\N	\N	\N
2008-11-21 11:19:41.294+00	\N	36593	\N	\N	\N	\N
2008-11-21 11:19:41.315+00	\N	36595	\N	\N	\N	\N
2008-11-21 11:19:41.335+00	\N	36597	\N	\N	\N	\N
2008-11-21 11:19:41.356+00	\N	36599	\N	\N	\N	\N
2008-11-21 11:19:41.376+00	\N	36601	\N	\N	\N	\N
2008-11-21 11:19:41.397+00	\N	36603	\N	\N	\N	\N
2008-11-21 11:19:41.418+00	\N	36605	\N	\N	\N	\N
2008-11-21 11:19:41.439+00	\N	36607	\N	\N	\N	\N
2008-11-21 11:19:41.461+00	\N	36609	\N	\N	\N	\N
2008-11-21 11:19:41.482+00	\N	36611	\N	\N	\N	\N
2008-11-21 11:19:41.504+00	\N	36613	\N	\N	\N	\N
2008-11-21 11:19:41.526+00	\N	36615	\N	\N	\N	\N
2008-11-21 11:19:41.548+00	\N	36617	\N	\N	\N	\N
2008-11-21 11:19:41.57+00	\N	36619	\N	\N	\N	\N
2008-11-21 11:19:41.593+00	\N	36621	\N	\N	\N	\N
2008-11-21 11:19:41.615+00	\N	36623	\N	\N	\N	\N
2008-11-21 11:19:41.637+00	\N	36625	\N	\N	\N	\N
\N	\N	36627	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:41.667+00	\N	36629	\N	\N	\N	\N
2008-11-21 11:19:41.68+00	\N	36631	\N	\N	\N	\N
2008-11-21 11:19:41.699+00	\N	36633	\N	\N	\N	\N
2008-11-21 11:19:41.723+00	\N	36635	\N	\N	\N	\N
2008-11-21 11:19:41.744+00	\N	36637	\N	\N	\N	\N
2008-11-21 11:19:41.765+00	\N	36639	\N	\N	\N	\N
2008-11-21 11:19:41.785+00	\N	36641	\N	\N	\N	\N
2008-11-21 11:19:41.806+00	\N	36643	\N	\N	\N	\N
2008-11-21 11:19:41.828+00	\N	36645	\N	\N	\N	\N
2008-11-21 11:19:41.849+00	\N	36647	\N	\N	\N	\N
2008-11-21 11:19:41.871+00	\N	36649	\N	\N	\N	\N
2008-11-21 11:19:41.893+00	\N	36651	\N	\N	\N	\N
2008-11-21 11:19:41.915+00	\N	36653	\N	\N	\N	\N
2008-11-21 11:19:41.937+00	\N	36655	\N	\N	\N	\N
2008-11-21 11:19:41.959+00	\N	36657	\N	\N	\N	\N
2008-11-21 11:19:41.981+00	\N	36659	\N	\N	\N	\N
2008-11-21 11:19:42.003+00	\N	36661	\N	\N	\N	\N
2008-11-21 11:19:42.026+00	\N	36663	\N	\N	\N	\N
2008-11-21 11:19:42.049+00	\N	36665	\N	\N	\N	\N
2008-11-21 11:19:42.072+00	\N	36667	\N	\N	\N	\N
\N	\N	36669	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:42.102+00	\N	36671	\N	\N	\N	\N
2008-11-21 11:19:42.116+00	\N	36673	\N	\N	\N	\N
2008-11-21 11:19:42.135+00	\N	36675	\N	\N	\N	\N
2008-11-21 11:19:42.146+00	\N	36677	\N	\N	\N	\N
2008-11-21 11:19:42.171+00	\N	36679	\N	\N	\N	\N
2008-11-21 11:19:42.192+00	\N	36681	\N	\N	\N	\N
2008-11-21 11:19:42.219+00	\N	36683	\N	\N	\N	\N
2008-11-21 11:19:42.241+00	\N	36685	\N	\N	\N	\N
2008-11-21 11:19:42.262+00	\N	36687	\N	\N	\N	\N
2008-11-21 11:19:42.283+00	\N	36689	\N	\N	\N	\N
2008-11-21 11:19:42.305+00	\N	36691	\N	\N	\N	\N
2008-11-21 11:19:42.327+00	\N	36693	\N	\N	\N	\N
2008-11-21 11:19:42.349+00	\N	36695	\N	\N	\N	\N
2008-11-21 11:19:42.371+00	\N	36697	\N	\N	\N	\N
2008-11-21 11:19:42.393+00	\N	36699	\N	\N	\N	\N
2008-11-21 11:19:42.415+00	\N	36701	\N	\N	\N	\N
2008-11-21 11:19:42.438+00	\N	36703	\N	\N	\N	\N
2008-11-21 11:19:42.46+00	\N	36705	\N	\N	\N	\N
2008-11-21 11:19:42.483+00	\N	36707	\N	\N	\N	\N
2008-11-21 11:19:42.507+00	\N	36709	\N	\N	\N	\N
2008-11-21 11:19:42.539+00	\N	36711	\N	\N	\N	\N
\N	\N	36713	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:42.591+00	\N	36715	\N	\N	\N	\N
2008-11-21 11:19:42.606+00	\N	36717	\N	\N	\N	\N
2008-11-21 11:19:42.617+00	\N	36719	\N	\N	\N	\N
2008-11-21 11:19:42.636+00	\N	36721	\N	\N	\N	\N
2008-11-21 11:19:42.647+00	\N	36723	\N	\N	\N	\N
2008-11-21 11:19:42.673+00	\N	36725	\N	\N	\N	\N
2008-11-21 11:19:42.694+00	\N	36727	\N	\N	\N	\N
2008-11-21 11:19:42.715+00	\N	36729	\N	\N	\N	\N
2008-11-21 11:19:42.737+00	\N	36731	\N	\N	\N	\N
2008-11-21 11:19:42.759+00	\N	36733	\N	\N	\N	\N
2008-11-21 11:19:42.78+00	\N	36735	\N	\N	\N	\N
2008-11-21 11:19:42.802+00	\N	36737	\N	\N	\N	\N
2008-11-21 11:19:42.823+00	\N	36739	\N	\N	\N	\N
2008-11-21 11:19:39.429+00	\N	36399	\N	\N	\N	\N
2008-11-21 11:19:39.442+00	\N	36401	\N	\N	\N	\N
2008-11-21 11:19:39.457+00	\N	36403	\N	\N	\N	\N
2008-11-21 11:19:39.465+00	\N	36405	\N	\N	\N	\N
2008-11-21 11:19:39.475+00	\N	36407	\N	\N	\N	\N
2008-11-21 11:19:39.496+00	\N	36409	\N	\N	\N	\N
2008-11-21 11:19:39.514+00	\N	36411	\N	\N	\N	\N
2008-11-21 11:19:39.535+00	\N	36413	\N	\N	\N	\N
2008-11-21 11:19:39.553+00	\N	36415	\N	\N	\N	\N
2008-11-21 11:19:39.571+00	\N	36417	\N	\N	\N	\N
2008-11-21 11:19:39.589+00	\N	36419	\N	\N	\N	\N
2008-11-21 11:19:39.608+00	\N	36421	\N	\N	\N	\N
2008-11-21 11:19:39.627+00	\N	36423	\N	\N	\N	\N
2008-11-21 11:19:39.644+00	\N	36425	\N	\N	\N	\N
2008-11-21 11:19:39.661+00	\N	36427	\N	\N	\N	\N
2008-11-21 11:19:39.678+00	\N	36429	\N	\N	\N	\N
2008-11-21 11:19:39.696+00	\N	36431	\N	\N	\N	\N
2008-11-21 11:19:39.714+00	\N	36433	\N	\N	\N	\N
2008-11-21 11:19:39.734+00	\N	36435	\N	\N	\N	\N
2008-11-21 11:19:39.755+00	\N	36437	\N	\N	\N	\N
2008-11-21 11:19:39.777+00	\N	36439	\N	\N	\N	\N
2008-11-21 11:19:39.798+00	\N	36441	\N	\N	\N	\N
2008-11-21 11:19:39.819+00	\N	36443	\N	\N	\N	\N
\N	\N	36445	\N	Purification of Target Protein	\N	\N
2008-11-21 11:19:39.848+00	\N	36447	\N	\N	\N	\N
2008-11-21 11:19:39.86+00	\N	36449	\N	\N	\N	\N
2008-11-21 11:19:39.876+00	\N	36451	\N	\N	\N	\N
2008-11-21 11:19:39.886+00	\N	36453	\N	\N	\N	\N
2008-11-21 11:19:39.896+00	\N	36455	\N	\N	\N	\N
2008-11-21 11:19:39.918+00	\N	36457	\N	\N	\N	\N
2008-11-21 11:19:39.937+00	\N	36459	\N	\N	\N	\N
2008-11-21 11:19:39.957+00	\N	36461	\N	\N	\N	\N
2008-11-21 11:19:39.977+00	\N	36463	\N	\N	\N	\N
2008-11-21 11:19:39.997+00	\N	36465	\N	\N	\N	\N
2008-11-21 11:19:40.016+00	\N	36467	\N	\N	\N	\N
2008-11-21 11:19:40.036+00	\N	36469	\N	\N	\N	\N
2008-11-21 11:19:40.057+00	\N	36471	\N	\N	\N	\N
2008-11-21 11:19:40.077+00	\N	36473	\N	\N	\N	\N
2008-11-21 11:19:40.097+00	\N	36475	\N	\N	\N	\N
2008-11-21 11:19:40.117+00	\N	36477	\N	\N	\N	\N
2008-11-21 11:19:40.138+00	\N	36479	\N	\N	\N	\N
2008-11-21 11:19:40.206+00	\N	36481	\N	\N	\N	\N
2008-11-21 11:19:40.227+00	\N	36483	\N	\N	\N	\N
2008-11-21 11:19:40.249+00	\N	36485	\N	\N	\N	\N
2008-11-21 11:19:40.27+00	\N	36487	\N	\N	\N	\N
2008-11-21 11:19:40.292+00	\N	36489	\N	\N	\N	\N
2008-11-21 11:19:40.314+00	\N	36491	\N	\N	\N	\N
2008-11-21 11:19:42.845+00	\N	36741	\N	\N	\N	\N
2008-11-21 11:19:42.868+00	\N	36743	\N	\N	\N	\N
2008-11-21 11:19:42.89+00	\N	36745	\N	\N	\N	\N
2008-11-21 11:19:42.913+00	\N	36747	\N	\N	\N	\N
2008-11-21 11:19:42.934+00	\N	36749	\N	\N	\N	\N
2008-11-21 11:19:42.956+00	\N	36751	\N	\N	\N	\N
2008-11-21 11:19:42.977+00	\N	36753	\N	\N	\N	\N
2008-11-21 11:19:42.999+00	\N	36755	\N	\N	\N	\N
\N	\N	36757	\N	Removal of Tag peptides	\N	\N
2008-11-21 11:19:43.026+00	\N	36759	\N	\N	\N	\N
2008-11-21 11:19:43.041+00	\N	36761	\N	\N	\N	\N
2008-11-21 11:19:43.05+00	\N	36763	\N	\N	\N	\N
2008-11-21 11:19:43.068+00	\N	36765	\N	\N	\N	\N
2008-11-21 11:19:43.079+00	\N	36767	\N	\N	\N	\N
2008-11-21 11:19:43.102+00	\N	36769	\N	\N	\N	\N
2008-11-21 11:19:43.122+00	\N	36771	\N	\N	\N	\N
2008-11-21 11:19:43.141+00	\N	36773	\N	\N	\N	\N
2008-11-21 11:19:43.161+00	\N	36775	\N	\N	\N	\N
2008-11-21 11:19:43.185+00	\N	36777	\N	\N	\N	\N
\N	\N	36779	\N	Concentration of Target Protein	\N	\N
2008-11-21 11:19:43.21+00	\N	36781	\N	\N	\N	\N
2008-11-21 11:19:43.221+00	\N	36783	\N	\N	\N	\N
2008-11-21 11:19:43.242+00	\N	36785	\N	\N	\N	\N
2008-11-21 11:19:43.259+00	\N	36787	\N	\N	\N	\N
2008-11-21 11:19:43.276+00	\N	36789	\N	\N	\N	\N
2008-11-21 11:19:43.292+00	\N	36791	\N	\N	\N	\N
2008-11-21 11:19:43.31+00	\N	36793	\N	\N	\N	\N
2008-11-21 11:19:43.327+00	\N	36795	\N	\N	\N	\N
\N	\N	36797	\N	Mass spectrometry analysis	\N	\N
2008-11-21 11:19:43.349+00	\N	36799	\N	\N	\N	\N
2008-11-21 11:19:43.36+00	\N	36801	\N	\N	\N	\N
2008-11-21 11:19:43.381+00	\N	36803	\N	\N	\N	\N
2008-11-21 11:19:43.407+00	\N	36805	\N	\N	\N	\N
2008-11-21 11:19:43.432+00	\N	36807	\N	\N	\N	\N
2008-11-21 11:19:43.45+00	\N	36809	\N	\N	\N	\N
\N	\N	38001	\N	\N	\N	\N
2008-11-21 11:19:51.541+00	\N	38003	\N	\N	\N	\N
2008-11-21 11:19:51.633+00	\N	38005	\N	\N	\N	\N
2008-11-21 11:19:51.669+00	\N	38007	\N	\N	\N	\N
2008-11-21 11:19:51.706+00	\N	38009	\N	\N	\N	\N
2008-11-21 11:19:51.741+00	\N	38011	\N	\N	\N	\N
2008-11-21 11:19:51.779+00	\N	38013	\N	\N	\N	\N
2008-11-21 11:19:51.819+00	\N	38015	\N	\N	\N	\N
2008-11-21 11:19:51.846+00	\N	38017	\N	\N	\N	\N
2008-11-21 11:19:51.891+00	\N	38019	\N	\N	\N	\N
2008-11-21 11:19:51.938+00	\N	38021	\N	\N	\N	\N
\N	\N	38023	\N	\N	\N	\N
2008-11-21 11:19:52.127+00	\N	38025	\N	\N	\N	\N
2008-11-21 11:19:52.187+00	\N	38027	\N	\N	\N	\N
2008-11-21 11:19:52.236+00	\N	38029	\N	\N	\N	\N
2008-11-21 11:19:52.276+00	\N	38031	\N	\N	\N	\N
2008-11-21 11:19:52.317+00	\N	38033	\N	\N	\N	\N
2008-11-21 11:19:52.359+00	\N	38035	\N	\N	\N	\N
2008-11-21 11:19:52.401+00	\N	38037	\N	\N	\N	\N
2008-11-21 11:19:52.45+00	\N	38039	\N	\N	\N	\N
2008-11-21 11:19:52.495+00	\N	38041	\N	\N	\N	\N
2008-11-21 11:19:52.573+00	\N	38043	\N	\N	\N	\N
2008-11-21 11:19:52.622+00	\N	38045	\N	\N	\N	\N
2008-11-21 11:19:52.665+00	\N	38047	\N	\N	\N	\N
2008-11-21 11:19:52.708+00	\N	38049	\N	\N	\N	\N
2008-11-21 11:19:52.75+00	\N	38051	\N	\N	\N	\N
2008-11-21 11:19:52.791+00	\N	38053	\N	\N	\N	\N
2008-11-21 11:19:52.829+00	\N	38055	\N	\N	\N	\N
\N	\N	38057	\N	\N	\N	\N
2008-11-21 11:19:52.896+00	\N	38059	\N	\N	\N	\N
2008-11-21 11:19:52.912+00	\N	38061	\N	\N	\N	\N
2008-11-21 11:19:52.943+00	\N	38063	\N	\N	\N	\N
2008-11-21 11:19:52.968+00	\N	38065	\N	\N	\N	\N
2008-11-21 11:19:52.995+00	\N	38067	\N	\N	\N	\N
2008-11-21 11:19:53.021+00	\N	38069	\N	\N	\N	\N
2008-11-21 11:19:53.046+00	\N	38071	\N	\N	\N	\N
2008-11-21 11:19:53.073+00	\N	38073	\N	\N	\N	\N
2008-11-21 11:19:53.099+00	\N	38075	\N	\N	\N	\N
2008-11-21 11:19:53.125+00	\N	38077	\N	\N	\N	\N
2008-11-21 11:19:53.151+00	\N	38079	\N	\N	\N	\N
2008-11-21 11:19:53.178+00	\N	38081	\N	\N	\N	\N
2008-11-21 11:19:53.206+00	\N	38083	\N	\N	\N	\N
2008-11-21 11:19:53.233+00	\N	38085	\N	\N	\N	\N
2008-11-21 11:19:53.261+00	\N	38087	\N	\N	\N	\N
2008-11-21 11:19:53.29+00	\N	38089	\N	\N	\N	\N
2008-11-21 11:19:53.318+00	\N	38091	\N	\N	\N	\N
2008-11-21 11:19:53.346+00	\N	38093	\N	\N	\N	\N
2008-11-21 11:19:53.376+00	\N	38095	\N	\N	\N	\N
2008-11-21 11:19:53.404+00	\N	38097	\N	\N	\N	\N
2008-11-21 11:19:53.432+00	\N	38099	\N	\N	\N	\N
2008-11-21 11:19:53.459+00	\N	38101	\N	\N	\N	\N
2008-11-21 11:19:53.487+00	\N	38103	\N	\N	\N	\N
2008-11-21 11:19:53.515+00	\N	38105	\N	\N	\N	\N
2008-11-21 11:19:53.544+00	\N	38107	\N	\N	\N	\N
2008-11-21 11:19:53.572+00	\N	38109	\N	\N	\N	\N
2008-11-21 11:19:53.602+00	\N	38111	\N	\N	\N	\N
2008-11-21 11:19:53.632+00	\N	38113	\N	\N	\N	\N
2008-11-21 11:19:53.661+00	\N	38115	\N	\N	\N	\N
2008-11-21 11:19:53.69+00	\N	38117	\N	\N	\N	\N
2008-11-21 11:19:53.719+00	\N	38119	\N	\N	\N	\N
2008-11-21 11:19:53.835+00	\N	38121	\N	\N	\N	\N
2008-11-21 11:19:53.862+00	\N	38123	\N	\N	\N	\N
2008-11-21 11:19:53.889+00	\N	38125	\N	\N	\N	\N
2008-11-21 11:19:53.916+00	\N	38127	\N	\N	\N	\N
2008-11-21 11:19:53.943+00	\N	38129	\N	\N	\N	\N
2008-11-21 11:19:53.97+00	\N	38131	\N	\N	\N	\N
2008-11-21 11:19:53.997+00	\N	38133	\N	\N	\N	\N
\N	\N	38135	\N	\N	\N	\N
2008-11-21 11:19:54.033+00	\N	38137	\N	\N	\N	\N
2008-11-21 11:19:54.053+00	\N	38139	\N	\N	\N	\N
2008-11-21 11:19:54.084+00	\N	38141	\N	\N	\N	\N
2008-11-21 11:19:54.10+00	\N	38143	\N	\N	\N	\N
2008-11-21 11:19:54.123+00	\N	38145	\N	\N	\N	\N
2008-11-21 11:19:54.143+00	\N	38147	\N	\N	\N	\N
2008-11-21 11:19:54.163+00	\N	38149	\N	\N	\N	\N
2008-11-21 11:19:54.184+00	\N	38151	\N	\N	\N	\N
\N	\N	38153	\N	\N	\N	\N
2008-11-21 11:19:54.222+00	\N	38155	\N	\N	\N	\N
2008-11-21 11:19:54.244+00	\N	38157	\N	\N	\N	\N
2008-11-21 11:19:54.275+00	\N	38159	\N	\N	\N	\N
2008-11-21 11:19:54.299+00	\N	38161	\N	\N	\N	\N
2008-11-21 11:19:54.321+00	\N	38163	\N	\N	\N	\N
2008-11-21 11:19:54.345+00	\N	38165	\N	\N	\N	\N
2008-11-21 11:19:54.367+00	\N	38167	\N	\N	\N	\N
2008-11-21 11:19:54.389+00	\N	38169	\N	\N	\N	\N
2008-11-21 11:19:54.411+00	\N	38171	\N	\N	\N	\N
2008-11-21 11:19:54.435+00	\N	38173	\N	\N	\N	\N
2008-11-21 11:19:54.458+00	\N	38175	\N	\N	\N	\N
2008-11-21 11:19:54.483+00	\N	38177	\N	\N	\N	\N
\N	\N	38179	\N	\N	\N	\N
2008-11-21 11:19:54.526+00	\N	38181	\N	\N	\N	\N
2008-11-21 11:19:54.558+00	\N	38183	\N	\N	\N	\N
2008-11-21 11:19:54.588+00	\N	38185	\N	\N	\N	\N
2008-11-21 11:19:54.609+00	\N	38187	\N	\N	\N	\N
2008-11-21 11:19:54.632+00	\N	38189	\N	\N	\N	\N
2008-11-21 11:19:54.655+00	\N	38191	\N	\N	\N	\N
2008-11-21 11:19:54.677+00	\N	38193	\N	\N	\N	\N
2008-11-21 11:19:54.723+00	\N	38195	\N	\N	\N	\N
2008-11-21 11:19:54.745+00	\N	38197	\N	\N	\N	\N
2008-11-21 11:19:54.77+00	\N	38199	\N	\N	\N	\N
2008-11-21 11:19:54.793+00	\N	38201	\N	\N	\N	\N
2008-11-21 11:19:54.821+00	\N	38203	\N	\N	\N	\N
2008-11-21 11:19:54.844+00	\N	38205	\N	\N	\N	\N
2008-11-21 11:19:54.872+00	\N	38207	\N	\N	\N	\N
2008-11-21 11:19:54.899+00	\N	38209	\N	\N	\N	\N
2008-11-21 11:19:54.922+00	\N	38211	\N	\N	\N	\N
\N	\N	38213	\N	\N	\N	\N
2008-11-21 11:19:54.989+00	\N	38215	\N	\N	\N	\N
2008-11-21 11:19:55.011+00	\N	38217	\N	\N	\N	\N
2008-11-21 11:19:55.054+00	\N	38219	\N	\N	\N	\N
2008-11-21 11:19:55.073+00	\N	38221	\N	\N	\N	\N
2008-11-21 11:19:55.093+00	\N	38223	\N	\N	\N	\N
2008-11-21 11:19:55.13+00	\N	38225	\N	\N	\N	\N
2008-11-21 11:19:55.147+00	\N	38227	\N	\N	\N	\N
2008-11-21 11:19:55.171+00	\N	38229	\N	\N	\N	\N
2008-11-21 11:19:55.189+00	\N	38231	\N	\N	\N	\N
2008-11-21 11:19:55.208+00	\N	38233	\N	\N	\N	\N
2008-11-21 11:19:55.226+00	\N	38235	\N	\N	\N	\N
2008-11-21 11:19:55.244+00	\N	38237	\N	\N	\N	\N
\N	\N	38239	\N	\N	\N	\N
2008-11-21 11:19:55.269+00	\N	38241	\N	\N	\N	\N
2008-11-21 11:19:55.312+00	\N	38243	\N	\N	\N	\N
2008-11-21 11:19:55.341+00	\N	38245	\N	\N	\N	\N
2008-11-21 11:19:55.364+00	\N	38247	\N	\N	\N	\N
2008-11-21 11:19:55.386+00	\N	38249	\N	\N	\N	\N
2008-11-21 11:19:55.407+00	\N	38251	\N	\N	\N	\N
2008-11-21 11:19:55.429+00	\N	38253	\N	\N	\N	\N
2008-11-21 11:19:55.46+00	\N	38255	\N	\N	\N	\N
2008-11-21 11:19:55.483+00	\N	38257	\N	\N	\N	\N
2008-11-21 11:19:55.505+00	\N	38259	\N	\N	\N	\N
2008-11-21 11:19:55.528+00	\N	38261	\N	\N	\N	\N
2008-11-21 11:19:55.55+00	\N	38263	\N	\N	\N	\N
2008-11-21 11:19:55.573+00	\N	38265	\N	\N	\N	\N
2008-11-21 11:19:55.606+00	\N	38267	\N	\N	\N	\N
2008-11-21 11:19:55.628+00	\N	38269	\N	\N	\N	\N
2008-11-21 11:19:55.655+00	\N	38271	\N	\N	\N	\N
\N	\N	40001	\N	\N	\N	\N
2008-11-21 11:20:04.83+00	\N	40003	\N	\N	\N	\N
2008-11-21 11:20:04.908+00	\N	40005	\N	\N	\N	\N
2008-11-21 11:20:04.952+00	\N	40007	\N	\N	\N	\N
2008-11-21 11:20:05.058+00	\N	40009	\N	\N	\N	\N
2008-11-21 11:20:05.139+00	\N	40011	\N	\N	\N	\N
2008-11-21 11:20:05.218+00	\N	40013	\N	\N	\N	\N
2008-11-21 11:20:05.30+00	\N	40015	\N	\N	\N	\N
2008-11-21 11:20:05.385+00	\N	40017	\N	\N	\N	\N
2008-11-21 11:20:05.473+00	\N	40019	\N	\N	\N	\N
2008-11-21 11:20:05.565+00	\N	40021	\N	\N	\N	\N
2008-11-21 11:20:05.658+00	\N	40023	\N	\N	\N	\N
2008-11-21 11:20:05.757+00	\N	40025	\N	\N	\N	\N
2008-11-21 11:20:05.855+00	\N	40027	\N	\N	\N	\N
\N	\N	40029	\N	\N	\N	\N
2008-11-21 11:20:05.995+00	\N	40031	\N	\N	\N	\N
2008-11-21 11:20:06.04+00	\N	40033	\N	\N	\N	\N
2008-11-21 11:20:06.132+00	\N	40035	\N	\N	\N	\N
2008-11-21 11:20:06.211+00	\N	40037	\N	\N	\N	\N
2008-11-21 11:20:06.293+00	\N	40039	\N	\N	\N	\N
2008-11-21 11:20:06.377+00	\N	40041	\N	\N	\N	\N
2008-11-21 11:20:06.464+00	\N	40043	\N	\N	\N	\N
2008-11-21 11:20:06.557+00	\N	40045	\N	\N	\N	\N
2008-11-21 11:20:06.649+00	\N	40047	\N	\N	\N	\N
2008-11-21 11:20:06.744+00	\N	40049	\N	\N	\N	\N
\N	\N	40051	\N	\N	\N	\N
2008-11-21 11:20:06.825+00	\N	40053	\N	\N	\N	\N
2008-11-21 11:20:06.871+00	\N	40055	\N	\N	\N	\N
2008-11-21 11:20:06.965+00	\N	40057	\N	\N	\N	\N
2008-11-21 11:20:07.048+00	\N	40059	\N	\N	\N	\N
2008-11-21 11:20:07.133+00	\N	40061	\N	\N	\N	\N
2008-11-21 11:20:07.22+00	\N	40063	\N	\N	\N	\N
2008-11-21 11:20:07.31+00	\N	40065	\N	\N	\N	\N
\N	\N	40067	\N	\N	\N	\N
2008-11-21 11:20:07.482+00	\N	40069	\N	\N	\N	\N
2008-11-21 11:20:07.528+00	\N	40071	\N	\N	\N	\N
2008-11-21 11:20:07.628+00	\N	40073	\N	\N	\N	\N
2008-11-21 11:20:07.712+00	\N	40075	\N	\N	\N	\N
2008-11-21 11:20:07.798+00	\N	40077	\N	\N	\N	\N
2008-11-21 11:20:07.887+00	\N	40079	\N	\N	\N	\N
2008-11-21 11:20:07.977+00	\N	40081	\N	\N	\N	\N
2008-11-21 11:20:08.069+00	\N	40083	\N	\N	\N	\N
2008-11-21 11:20:08.162+00	\N	40085	\N	\N	\N	\N
2008-11-21 11:20:08.258+00	\N	40087	\N	\N	\N	\N
\N	\N	40089	\N	\N	\N	\N
2008-11-21 11:20:08.322+00	\N	40091	\N	\N	\N	\N
2008-11-21 11:20:08.38+00	\N	40093	\N	\N	\N	\N
2008-11-21 11:20:08.423+00	\N	40095	\N	\N	\N	\N
2008-11-21 11:20:08.465+00	\N	40097	\N	\N	\N	\N
2008-11-21 11:20:08.505+00	\N	40099	\N	\N	\N	\N
2008-11-21 11:20:08.547+00	\N	40101	\N	\N	\N	\N
2008-11-21 11:20:08.589+00	\N	40103	\N	\N	\N	\N
2008-11-21 11:20:08.631+00	\N	40105	\N	\N	\N	\N
2008-11-21 11:20:08.674+00	\N	40107	\N	\N	\N	\N
2008-11-21 11:20:08.718+00	\N	40109	\N	\N	\N	\N
2008-11-21 11:20:08.775+00	\N	40111	\N	\N	\N	\N
2008-11-21 11:20:08.821+00	\N	40113	\N	\N	\N	\N
\N	\N	40115	\N	\N	\N	\N
2008-11-21 11:20:08.874+00	\N	40117	\N	\N	\N	\N
2008-11-21 11:20:08.909+00	\N	40119	\N	\N	\N	\N
2008-11-21 11:20:08.955+00	\N	40121	\N	\N	\N	\N
2008-11-21 11:20:08.995+00	\N	40123	\N	\N	\N	\N
2008-11-21 11:20:09.041+00	\N	40125	\N	\N	\N	\N
2008-11-21 11:20:09.099+00	\N	40127	\N	\N	\N	\N
2008-11-21 11:20:09.139+00	\N	40129	\N	\N	\N	\N
2008-11-21 11:20:09.179+00	\N	40131	\N	\N	\N	\N
2008-11-21 11:20:09.22+00	\N	40133	\N	\N	\N	\N
2008-11-21 11:20:09.262+00	\N	40135	\N	\N	\N	\N
2008-11-21 11:20:09.304+00	\N	40137	\N	\N	\N	\N
2008-11-21 11:20:09.348+00	\N	40139	\N	\N	\N	\N
2008-11-21 11:20:09.391+00	\N	40141	\N	\N	\N	\N
2008-11-21 11:20:09.438+00	\N	40143	\N	\N	\N	\N
2008-11-21 11:20:09.474+00	\N	40145	\N	\N	\N	\N
2008-11-21 11:20:09.51+00	\N	40147	\N	\N	\N	\N
2008-11-21 11:20:09.547+00	\N	40149	\N	\N	\N	\N
2008-11-21 11:20:09.584+00	\N	40151	\N	\N	\N	\N
\N	\N	40153	\N	\N	\N	\N
2008-11-21 11:20:09.727+00	\N	40155	\N	\N	\N	\N
2008-11-21 11:20:09.757+00	\N	40157	\N	\N	\N	\N
2008-11-21 11:20:09.84+00	\N	40159	\N	\N	\N	\N
2008-11-21 11:20:10.413+00	\N	40161	\N	\N	\N	\N
2008-11-21 11:20:10.434+00	\N	40163	\N	\N	\N	\N
2008-11-21 11:20:10.454+00	\N	40165	\N	\N	\N	\N
2008-11-21 11:20:10.475+00	\N	40167	\N	\N	\N	\N
2008-11-21 11:20:10.495+00	\N	40169	\N	\N	\N	\N
2008-11-21 11:20:10.516+00	\N	40171	\N	\N	\N	\N
2008-11-21 11:20:10.539+00	\N	40173	\N	\N	\N	\N
\N	\N	40175	\N	\N	\N	\N
2008-11-21 11:20:10.61+00	\N	40177	\N	\N	\N	\N
2008-11-21 11:20:10.623+00	\N	40179	\N	\N	\N	\N
2008-11-21 11:20:10.652+00	\N	40181	\N	\N	\N	\N
2008-11-21 11:20:10.667+00	\N	40183	\N	\N	\N	\N
2008-11-21 11:20:10.702+00	\N	40185	\N	\N	\N	\N
2008-11-21 11:20:10.718+00	\N	40187	\N	\N	\N	\N
2008-11-21 11:20:10.737+00	\N	40189	\N	\N	\N	\N
2008-11-21 11:20:10.756+00	\N	40191	\N	\N	\N	\N
2008-11-21 11:20:10.78+00	\N	40193	\N	\N	\N	\N
2008-11-21 11:20:11.315+00	\N	40195	\N	\N	\N	\N
2008-11-21 11:20:11.34+00	\N	40197	\N	\N	\N	\N
2008-11-21 11:20:11.362+00	\N	40199	\N	\N	\N	\N
2008-11-21 11:20:11.384+00	\N	40201	\N	\N	\N	\N
2008-11-21 11:20:11.407+00	\N	40203	\N	\N	\N	\N
2008-11-21 11:20:11.43+00	\N	40205	\N	\N	\N	\N
2008-11-21 11:20:11.453+00	\N	40207	\N	\N	\N	\N
2008-11-21 11:20:11.476+00	\N	40209	\N	\N	\N	\N
2008-11-21 11:20:11.499+00	\N	40211	\N	\N	\N	\N
\N	\N	40213	\N	\N	\N	\N
2008-11-21 11:20:11.535+00	\N	40215	\N	\N	\N	\N
2008-11-21 11:20:11.559+00	\N	40217	\N	\N	\N	\N
2008-11-21 11:20:11.589+00	\N	40219	\N	\N	\N	\N
2008-11-21 11:20:11.607+00	\N	40221	\N	\N	\N	\N
2008-11-21 11:20:11.624+00	\N	40223	\N	\N	\N	\N
2008-11-21 11:20:11.643+00	\N	40225	\N	\N	\N	\N
2008-11-21 11:20:11.661+00	\N	40227	\N	\N	\N	\N
2008-11-21 11:20:11.679+00	\N	40229	\N	\N	\N	\N
2008-11-21 11:20:11.698+00	\N	40231	\N	\N	\N	\N
2008-11-21 11:20:11.717+00	\N	40233	\N	\N	\N	\N
\N	\N	40235	\N	\N	\N	\N
2008-11-21 11:20:11.747+00	\N	40237	\N	\N	\N	\N
2008-11-21 11:20:11.777+00	\N	40239	\N	\N	\N	\N
2008-11-21 11:20:11.793+00	\N	40241	\N	\N	\N	\N
2008-11-21 11:20:11.814+00	\N	40243	\N	\N	\N	\N
2008-11-21 11:20:11.831+00	\N	40245	\N	\N	\N	\N
2008-11-21 11:20:11.848+00	\N	40247	\N	\N	\N	\N
2008-11-21 11:20:11.865+00	\N	40249	\N	\N	\N	\N
2008-11-21 11:20:11.882+00	\N	40251	\N	\N	\N	\N
2008-11-21 11:20:11.899+00	\N	40253	\N	\N	\N	\N
2008-11-21 11:20:11.92+00	\N	40255	\N	\N	\N	\N
2008-11-21 11:20:11.938+00	\N	40257	\N	\N	\N	\N
2008-11-21 11:20:11.957+00	\N	40259	\N	\N	\N	\N
2008-11-21 11:20:11.975+00	\N	40261	\N	\N	\N	\N
2008-11-21 11:20:11.994+00	\N	40263	\N	\N	\N	\N
\N	\N	40265	\N	\N	\N	\N
2008-11-21 11:20:12.026+00	\N	40267	\N	\N	\N	\N
2008-11-21 11:20:12.047+00	\N	40269	\N	\N	\N	\N
2008-11-21 11:20:12.081+00	\N	40271	\N	\N	\N	\N
2008-11-21 11:20:12.104+00	\N	40273	\N	\N	\N	\N
2008-11-21 11:20:12.126+00	\N	40275	\N	\N	\N	\N
2008-11-21 11:20:12.148+00	\N	40277	\N	\N	\N	\N
2008-11-21 11:20:12.171+00	\N	40279	\N	\N	\N	\N
2008-11-21 11:20:12.193+00	\N	40281	\N	\N	\N	\N
2008-11-21 11:20:12.216+00	\N	40283	\N	\N	\N	\N
2008-11-21 11:20:12.239+00	\N	40285	\N	\N	\N	\N
2008-11-21 11:20:12.264+00	\N	40287	\N	\N	\N	\N
2008-11-21 11:20:12.288+00	\N	40289	\N	\N	\N	\N
2008-11-21 11:20:12.312+00	\N	40291	\N	\N	\N	\N
2008-11-21 11:20:12.336+00	\N	40293	\N	\N	\N	\N
\N	2009-02-10 15:25:57.974+00	34001	\N	\N	\N	\N
2009-02-10 15:25:58.036+00	\N	42017	\N	\N	\N	\N
2009-02-10 15:25:58.067+00	\N	42019	\N	\N	\N	\N
2009-02-10 15:25:57.849+00	2009-02-11 11:59:03.67+00	42013	\N	\N	\N	\N
\N	2009-02-11 11:59:03.67+00	42009	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2009-02-10 15:25:57.802+00	2009-02-11 11:59:03.67+00	42011	\N	\N	\N	\N
2009-02-10 15:25:57.927+00	2009-02-11 11:59:03.67+00	42015	\N	\N	\N	\N
2009-02-10 15:25:57.52+00	2009-02-11 11:59:31.201+00	42003	\N	\N	\N	\N
\N	2009-02-11 11:59:31.201+00	42001	\N	The plate is a physival plate that will be kept in fridge	\N	\N
2009-02-10 15:25:57.739+00	2009-02-11 11:59:31.201+00	42007	\N	\N	\N	\N
2009-02-10 15:25:57.63+00	2009-02-11 11:59:31.201+00	42005	\N	\N	\N	\N
2009-05-01 13:41:08.161+01	\N	44005	\N	\N	\N	\N
2009-05-01 13:41:08.208+01	\N	44006	\N	\N	\N	\N
2009-05-01 13:41:08.224+01	\N	44007	\N	\N	\N	\N
2009-05-01 13:41:08.255+01	\N	44008	\N	\N	\N	\N
2009-05-01 13:41:08.255+01	\N	44009	\N	\N	\N	\N
2009-05-01 13:41:08.286+01	\N	44010	\N	\N	\N	\N
2009-05-01 13:41:08.302+01	\N	44011	\N	\N	\N	\N
\N	\N	44018	\N	\N	\N	\N
2009-05-01 13:41:08.739+01	\N	44019	\N	\N	\N	\N
2009-05-01 13:41:08.77+01	\N	44020	\N	\N	\N	\N
\.


--
-- Data for Name: core_note; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_note (name, attachmentid) FROM stdin;
\.


--
-- Data for Name: core_note2relatedentrys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_note2relatedentrys (noteid, labbookentryid) FROM stdin;
\.


--
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
-- Data for Name: core_thesiscitation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY core_thesiscitation (city, country, institution, stateprovince, citationid) FROM stdin;
\.


--
-- Data for Name: cryz_cypade_possva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_cypade_possva (parameterdefinitionid, possiblevalue, order_) FROM stdin;
\.


--
-- Data for Name: cryz_dropannotation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_dropannotation (cmdlineparam, scoredate, labbookentryid, scoreid, sampleid, imageid, softwareid, holderid) FROM stdin;
\.


--
-- Data for Name: cryz_image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_image (filename, filepath, mimetype, labbookentryid, instrumentid, scheduledtaskid, sampleid, imagetypeid) FROM stdin;
\.


--
-- Data for Name: cryz_parameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_parameter (value, labbookentryid, parameterdefinitionid, imageid) FROM stdin;
\.


--
-- Data for Name: cryz_parameterdefinition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_parameterdefinition (defaultvalue, displayunit, label, "maxvalue", "minvalue", name, paramtype, unit, labbookentryid) FROM stdin;
\.


--
-- Data for Name: cryz_score; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_score (color, name, value, labbookentryid, scoringschemeid) FROM stdin;
\.


--
-- Data for Name: cryz_scoringscheme; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_scoringscheme (name, version, labbookentryid) FROM stdin;
\.


--
-- Data for Name: expe_experiment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experiment (enddate, islocked, name, startdate, status, labbookentryid, protocolid, instrumentid, groupid, operatorid, researchobjectiveid, experimentgroupid, experimenttypeid, methodid) FROM stdin;
\.


--
-- Data for Name: expe_experimentgroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experimentgroup (enddate, name, purpose, startdate, labbookentryid) FROM stdin;
\.


--
-- Data for Name: expe_inputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_inputsample (amount, amountdisplayunit, amountunit, name, "role", labbookentryid, experimentid, refinputsampleid, sampleid) FROM stdin;
\.


--
-- Data for Name: expe_instrument; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_instrument (model, name, serialnumber, labbookentryid, pressuredisplayunit, temperature, pressure, tempdisplayunit, manufacturerid, defaultimagetypeid, locationid) FROM stdin;
\N	Oasis 1700	\N	44005	\N	20.5	\N	\N	\N	44001	\N
\N	Oasis 1750	\N	44006	\N	6.5	\N	\N	\N	44001	\N
\N	Oasis LS3	\N	44007	\N	6.5	\N	\N	\N	44001	\N
\N	RI1000-0014	\N	44008	\N	6.5	\N	\N	\N	44002	\N
\N	RI182-0005	\N	44009	\N	23	\N	\N	\N	44002	\N
\N	Pixera	\N	44010	\N	23	\N	\N	\N	44004	\N
\N	RI182-0005-108	\N	44011	\N	23	\N	\N	\N	44003	\N
\.


--
-- Data for Name: expe_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_method (name, procedure_, task, labbookentryid, instrumentid, softwareid) FROM stdin;
\.


--
-- Data for Name: expe_methodparameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_methodparameter (name, paramtype, unit, value, labbookentryid, methodid) FROM stdin;
\.


--
-- Data for Name: expe_outputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_outputsample (amount, amountdisplayunit, amountunit, name, "role", labbookentryid, experimentid, refoutputsampleid, sampleid) FROM stdin;
\.


--
-- Data for Name: expe_parameter; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_parameter (name, paramtype, unit, value, labbookentryid, experimentid, parameterdefinitionid) FROM stdin;
\.


--
-- Data for Name: expe_software; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_software (name, vendoraddress, vendorname, vendorwebaddress, version, labbookentryid) FROM stdin;
\.


--
-- Data for Name: expe_software_tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_software_tasks (softwareid, task, order_) FROM stdin;
\.


--
-- Data for Name: hold_abstractholder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_abstractholder (colposition, name, rowposition, subposition, labbookentryid, holdertypeid, abstractholderid) FROM stdin;
\N	card	\N	\N	30001	\N	\N
\.


--
-- Data for Name: hold_holdca2abstholders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdca2abstholders (holdercategoryid, abstholderid) FROM stdin;
26011	30001
\.


--
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
-- Data for Name: hold_holder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holder (enddate, startdate, abstractholderid) FROM stdin;
\N	\N	30001
\.


--
-- Data for Name: hold_holderlocation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holderlocation (enddate, startdate, labbookentryid, holderid, locationid) FROM stdin;
\.


--
-- Data for Name: hold_holdertypeposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdertypeposition (colposition, maxvolume, maxvolumediplayunit, name, rowposition, subposition, labbookentryid, holdertypeid) FROM stdin;
\.


--
-- Data for Name: hold_refholder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refholder (abstractholderid) FROM stdin;
\.


--
-- Data for Name: hold_refholderoffset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refholderoffset (coloffset, rowoffset, suboffset, labbookentryid, holderid, refholderid) FROM stdin;
\.


--
-- Data for Name: hold_refsampleposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_refsampleposition (colposition, rowposition, subposition, labbookentryid, refsampleid, refholderid) FROM stdin;
\.


--
-- Data for Name: inst_instty2inst; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY inst_instty2inst (instrumenttypeid, instrumentid) FROM stdin;
\.


--
-- Data for Name: loca_location; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY loca_location (locationtype, name, pressure, pressuredisplayunit, tempdisplayunit, temperature, labbookentryid, locationid, organisationid) FROM stdin;
\.


--
-- Data for Name: mole_abstco_keywords; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_abstco_keywords (abstractcomponentid, keyword, order_) FROM stdin;
\.


--
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
-- Data for Name: mole_construct; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_construct (constructstatus, "function", markerdetails, promoterdetails, resistancedetails, sequencetype, moleculeid) FROM stdin;
\.


--
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
-- Data for Name: mole_molecule2relareobel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_molecule2relareobel (trialmoleculeid, relatedresearchobjectiveelementid) FROM stdin;
\.


--
-- Data for Name: mole_moleculefeature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_moleculefeature (endseqid, featuretype, name, ordering, startseqid, status, labbookentryid, refmoleculeid, moleculeid) FROM stdin;
\.


--
-- Data for Name: mole_primer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_primer (direction, gcgene, isuniversal, lengthongene, meltingtemperature, meltingtemperaturegene, meltingtemperatureseller, opticaldensity, particularity, restrictionsite, moleculeid) FROM stdin;
\.


--
-- Data for Name: peop_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_group (name, url, labbookentryid, organisationid) FROM stdin;
\.


--
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
-- Data for Name: peop_persingr_phonnu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_persingr_phonnu (personingroupid, phonenumber, order_) FROM stdin;
\.


--
-- Data for Name: peop_person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_person (familyname, familytitle, givenname, title, labbookentryid, currentgroupid) FROM stdin;
\.


--
-- Data for Name: peop_person_middin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_person_middin (personid, middleinitial, order_) FROM stdin;
\.


--
-- Data for Name: peop_personingroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_personingroup (deliveryaddress, emailaddress, enddate, faxnumber, mailingaddress, position_, startdate, labbookentryid, groupid, personid) FROM stdin;
\.


--
-- Data for Name: prot_parade_possva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_parade_possva (parameterdefinitionid, possiblevalue, order_) FROM stdin;
34081	Induction Batch	0
34081	AutoInduction Batch	1
34081	Fed Batch	2
34081	Optimization	3
34103	Yes	0
34103	No	1
34103	Returned	2
36141	Yes	0
36141	No	1
36141	Maybe	2
36141	Unscored	3
36237	Yes+++	0
36237	Yes++	1
36237	Yes+	2
36237	Yes	3
36237	No	4
36237	Maybe	5
36237	Unscored	6
36239	Yes+++	0
36239	Yes++	1
36239	Yes+	2
36239	Yes	3
36239	No	4
36239	Maybe	5
36239	Unscored	6
36251	Yes	0
36251	No	1
36251	Maybe	2
36251	Unscored	3
36297	Sonication	0
36297	Cell Disruptor	1
36297	Chemical	2
36297	None	3
36297	Other	4
36345	1ml His Column	0
36345	5ml His Column	1
36345	S200	2
36345	S75	3
36361	1ml HisTrap FF	0
36361	1ml GSTrap FF	1
36363	HiPrep 26/10 Desalting	0
36409	1ml HisTrap FF	0
36409	1ml GSTrap FF	1
36411	HiLoad 16/60 Superdex 75	0
36411	HiLoad 16/60 Superdex 200	1
36457	5ml HisTrap FF	0
36459	HiPrep 26/10 Desalting	0
36505	5ml HisTrap FF	0
36507	HiLoad 16/60 Superdex 75	0
36507	HiLoad 16/60 Superdex 200	1
36551	1ml HisTrap FF	0
36551	1ml GSTrap FF	1
36593	HiPrep 26/10 Desalting	0
36635	HiLoad 16/60 Superdex 75	0
36635	HiLoad 16/60 Superdex 200	1
36679	5ml HiTrap Q	0
36679	Resource Q	1
36679	Resource S	2
38029	0	0
38029	1S	1
38029	2S	2
38029	3S	3
38029	I	4
38031	Native	0
38031	Selmet	1
38033	BL21 (DE3)	0
38033	C43 (DE3)	1
38033	B834 (DE3)	2
38037	Auto-induction	0
38037	LB	1
38037	Simple-semet	2
38037	Auto-semet	3
38039	0.2	0
38039	0.4	1
38043	15	0
38043	20	1
38043	25	2
38045	Glycerol Stock	0
38045	Colonies	1
38047	48 Hours	0
38047	Overnight	1
38049	1:10	0
38049	1:100	1
38051	0.6	0
38051	0.1	1
38053	200	0
38053	250	1
38053	275	2
38065	Small	0
38065	Large	1
38087	Lysozyme -Freeze thaw	0
38087	Cell disruptor	1
38087	Sonicate	2
38093	GE	0
38093	Qiagen	1
38115	150	0
38115	500	1
38117	10% glycerol	0
38117	2mM DTT	1
38119	S75	0
38119	S200	1
38159	0	0
38159	1S	1
38159	2S	2
38159	3S	3
38159	I	4
38161	Native	0
38161	Selmet	1
38163	BL21 (DE3)	0
38163	C43 (DE3)	1
38163	B834 (DE3)	2
38167	Auto-induction	0
38167	Auto-semet	1
38169	Glycerol Stock	0
38169	Colonies	1
38171	48 Hours	0
38171	Overnight	1
38173	1:10	0
38173	1:100	1
38175	200	0
38175	250	1
38175	275	2
38185	0	0
38185	1S	1
38185	2S	2
38185	3S	3
38185	I	4
38187	Native	0
38187	Selmet	1
38189	BL21 (DE3)	0
38189	C43 (DE3)	1
38189	B834 (DE3)	2
38193	LB	0
38193	Simple-semet	1
38193	TPB	2
38195	0.2	0
38195	0.4	1
38199	15	0
38199	20	1
38199	25	2
38201	Glycerol Stock	0
38201	Colonies	1
38203	48 Hours	0
38203	Overnight	1
38205	1:10	0
38205	1:100	1
38207	0.6	0
38207	0.1	1
38209	200	0
38209	250	1
38209	275	2
38219	0	0
38219	1S	1
38219	2S	2
38219	3S	3
38219	I	4
38221	Native	0
38221	Selmet	1
38223	BL21 (DE3)	0
38223	C43 (DE3)	1
38223	B834 (DE3)	2
38227	Auto-induction	0
38227	Auto-semet	1
38229	Glycerol Stock	0
38229	Colonies	1
38231	48 Hours	0
38231	Overnight	1
38233	1:10	0
38233	1:100	1
38235	200	0
38235	250	1
38235	275	2
38245	0	0
38245	1S	1
38245	2S	2
38245	3S	3
38245	I	4
38247	Native	0
38247	Selmet	1
38249	BL21 (DE3)	0
38249	C43 (DE3)	1
38249	B834 (DE3)	2
38253	LB	0
38253	Simple-semet	1
38253	TPB	2
38255	0.2	0
38255	0.4	1
38259	15	0
38259	20	1
38259	25	2
38261	Glycerol Stock	0
38261	Colonies	1
38263	48 Hours	0
38263	Overnight	1
38265	1:10	0
38265	1:100	1
38267	0.6	0
38267	0.1	1
38269	200	0
38269	250	1
38269	275	2
40159	LIC+	0
40159	LIC-	1
40159	Baculo	2
40271	Native	0
40271	Selmet	1
40273	BL21 (DE3)	0
40273	C43 (DE3)	1
40273	B834 (DE3)	2
40275	IPTG	0
40275	Auto-induction	1
40285	15	0
40285	20	1
40285	25	2
40287	48 Hours	0
40287	Overnight	1
40289	1:10	0
40289	1:100	1
40291	0.6	0
40291	0.1	1
40293	200	0
40293	250	1
40293	275	2
\.


--
-- Data for Name: prot_parameterdefinition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_parameterdefinition (defaultvalue, displayunit, isgrouplevel, ismandatory, isresult, label, "maxvalue", "minvalue", name, paramtype, unit, labbookentryid, protocolid, order_) FROM stdin;
5	\N	f	t	f	\N	\N	\N	Reaction volume/uL	Float	\N	32011	32001	0
\N	\N	f	t	f	\N	\N	\N	Infusion Notes	String	\N	32013	32001	1
\N	\N	f	t	f	\N	\N	\N	Volume of cells plated out	Float	\N	32027	32015	0
Incubate 30 min on ice,   Heat shock 30 sec,   Incubate a further 2 min on ice	\N	f	t	f	\N	\N	\N	DNA Incubation conditions	String	\N	32029	32015	1
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32031	32015	2
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32033	32015	3
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32035	32015	4
\N	\N	f	t	f	\N	\N	\N	Transformation Result	Int	\N	32037	32015	5
\N	\N	f	t	f	\N	\N	\N	Transformation Notes	String	\N	32039	32015	6
\N	\N	f	t	f	\N	\N	\N	Concentrator type	String	\N	32049	32041	0
\N	\N	f	t	f	\N	\N	\N	Concentrator speed (g)	Int	\N	32051	32041	1
\N	\N	f	t	f	\N	\N	\N	Output volume	Int	\N	32053	32041	2
\N	\N	f	t	f	\N	\N	\N	Output concentration	Int	\N	32055	32041	3
\N	\N	f	t	f	\N	\N	\N	Output yield	Int	\N	32057	32041	4
\N	\N	f	t	f	\N	\N	\N	Concentrator Notes	String	\N	32059	32041	5
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32071	32061	0
false	\N	f	t	f	\N	\N	\N	Is the culture stirred?	Boolean	\N	32073	32061	1
false	\N	f	t	f	\N	\N	\N	Is the culture labelled?	Boolean	\N	32075	32061	2
50	\N	f	t	f	\N	\N	\N	LIC vector concentration ng/ul	Float	\N	32087	32077	0
Substitute 2ul H2O for 2ul LIC-prepared insert	\N	f	t	f	\N	\N	\N	Control	String	\N	32089	32077	1
Incubate 5 min at room temperature,    Cool on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32091	32077	2
\N	\N	f	t	f	\N	\N	\N	A. Number of colonies	Int	\N	32099	32093	0
Plate out and incubate overnight at 37C	\N	f	t	f	\N	\N	\N	B. Growth conditions	String	\N	32101	32093	1
LB agar plus 30ug/ml kanamycin	\N	f	t	f	\N	\N	\N	C. Plate type	String	\N	32103	32093	2
2mm	\N	f	t	f	\N	\N	\N	D. Colony size	String	\N	32105	32093	3
\N	\N	f	t	f	\N	\N	\N	Insert concentration pmol/ul	Float	\N	32123	32107	0
Warm to room temperature,    Incubate 30 min at 22C,    Heat for 5 min at 75C	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32125	32107	1
Incubate 5 min on ice,   Heat shock 30 sec,   Incubate a further 5 min on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32137	32127	0
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32139	32127	1
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32141	32127	2
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32143	32127	3
\N	\N	f	t	f	Plasmid concentration ug/ml	\N	\N	A. Plasmid concentration ug/ml	Float	\N	32165	32145	0
\N	\N	f	t	f	Plasmid volume for 8 ug	\N	\N	B. Plasmid volume for 8 ug	Float	\N	32167	32145	1
    Incubate 37C for 2 hours	\N	f	t	f	Incubation conditions	\N	\N	C. Digest conditions	String	\N	32169	32145	2
true	\N	f	t	f	\N	\N	\N	D. Gel purified	Boolean	\N	32171	32145	3
8	\N	f	t	f	\N	\N	\N	E. Linearised vector yield, pmol	Float	\N	32173	32145	4
Warm to room temperature,    Incubate 30 min at 22C,    Heat for 5 min at 75C	\N	f	t	f	\N	\N	\N	F. Incubation conditions	String	\N	32175	32145	5
50	\N	f	t	f	Final vector concentration ng/ul	\N	\N	G. Final vector concentration ng/ul	Float	\N	32177	32145	6
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32195	32179	0
Qiagen Plasmid midi kit Cat.12123	\N	f	t	f	\N	\N	\N	Kit name	String	\N	32197	32179	1
\N	\N	f	t	f	\N	\N	\N	Yield	Float	\N	32199	32179	2
\N	\N	f	t	f	\N	\N	\N	Concentration ug/ml	Float	\N	32201	32179	3
45	\N	f	t	f	\N	\N	\N	A. Annealing temperature	Float	\N	32221	32203	0
35	\N	f	t	f	\N	\N	\N	B. Number of cycles	Int	\N	32223	32203	1
Anneal 30 sec at 45C,    Extend 30 sec at 72C,    Denature 30 sec at 94C	\N	f	t	f	\N	\N	\N	C. Thermocycling conditions	String	\N	32225	32203	2
100	\N	f	t	f	\N	\N	\N	D. Primer concentration uM	Float	\N	32227	32203	3
50	\N	f	t	f	\N	\N	\N	E. Template concentration ng/ul	Float	\N	32229	32203	4
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32247	32231	0
Qiagen Plasmid midi kit 12143	\N	f	t	f	\N	\N	\N	Kit name	String	\N	32249	32231	1
\N	\N	f	t	f	\N	\N	\N	Plasmid yield	Float	\N	32251	32231	2
\N	\N	f	t	f	\N	\N	\N	Plasmid concentration ug/ml	Float	\N	32253	32231	3
\N	\N	f	t	f	\N	\N	\N	Number of colonies	Int	\N	32261	32255	0
false	\N	f	t	f	\N	\N	\N	Is the culture labelled?	Boolean	\N	32263	32255	1
Incubate overnight at 37C	\N	f	t	f	\N	\N	\N	Growth conditions	String	\N	32265	32255	2
\N	\N	f	t	f	\N	\N	\N	Plate type	String	\N	32267	32255	3
Incubate 5 min on ice,   Heat shock 30 sec,   Incubate a further 5 min on ice	\N	f	t	f	\N	\N	\N	Incubation conditions	String	\N	32279	32269	0
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32281	32269	1
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32283	32269	2
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32285	32269	3
B834	\N	f	t	f	\N	\N	\N	A. Expression strain	String	\N	32295	32287	0
\N	\N	f	t	f	\N	\N	\N	B. Construct ID and location	String	\N	32297	32287	1
true	\N	f	t	f	\N	\N	\N	C. Is the culture shaken?	Boolean	\N	32299	32287	2
160	\N	f	t	f	\N	\N	\N	D. Shaker speed rpm	Int	\N	32301	32287	3
2.0	\N	f	t	f	\N	\N	\N	E. Time (hours) before induction	Float	\N	32303	32287	4
0.6	\N	f	t	f	\N	\N	\N	F. OD before induction	Float	\N	32305	32287	5
37	\N	f	t	f	\N	\N	\N	G. Temperatue C	Float	\N	32307	32287	6
IPTG	\N	f	t	f	\N	\N	\N	H. Autoinduction/IPTG	String	\N	32309	32287	7
2	\N	f	t	f	\N	\N	\N	I. Sample time interval (hours)	Float	\N	32311	32287	8
\N	\N	f	t	f	\N	\N	\N	J. OD sample 1	Float	\N	32313	32287	9
\N	\N	f	t	f	\N	\N	\N	K. OD sample 2	Float	\N	32315	32287	10
\N	\N	f	t	f	\N	\N	\N	L. OD sample 3	Float	\N	32317	32287	11
\N	\N	f	t	f	\N	\N	\N	M. Optimal expression time (hours)	Float	\N	32319	32287	12
Sonication	\N	f	t	f	\N	\N	\N	N. Solubilization method	String	\N	32321	32287	13
\N	\N	f	t	f	\N	\N	\N	O. Solubilization details	String	\N	32323	32287	14
true	\N	f	t	f	\N	\N	\N	P. Gel image attached?	Boolean	\N	32325	32287	15
Markers in lane 1, etc.	\N	f	t	f	\N	\N	\N	Q. Gel details	String	\N	32327	32287	16
Reverse Phase	\N	f	t	f	\N	\N	\N	Purification type	String	\N	34005	34001	0
25 nmole	\N	f	t	f	\N	\N	\N	Starting Synthesis Scale	String	\N	34007	34001	1
TE	\N	f	t	f	\N	\N	\N	BufferType	String	\N	34009	34001	2
Concentration	\N	f	t	f	\N	\N	\N	Normalization	String	\N	34011	34001	3
AvrII	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	34013	34001	4
0.0	\N	f	t	f	\N	\N	\N	Tm seller	Float	\N	34015	34001	5
0.0	\N	f	t	f	\N	\N	\N	Tm full	Float	\N	34017	34001	6
0.0	\N	f	t	f	\N	\N	\N	Tm gene	Float	\N	34019	34001	7
400	\N	f	t	f	\N	\N	\N	volumn in nL	Float	\N	34021	34001	8
0.0	\N	f	t	f	\N	\N	\N	OD	Float	\N	34023	34001	9
0	\N	f	t	f	\N	\N	\N	Length on the gene	Int	\N	34025	34001	10
\N	\N	f	t	f	\N	\N	\N	Marker	String	\N	34035	34027	0
\N	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	34037	34027	1
\N	\N	f	t	f	\N	\N	\N	Organism	String	\N	34045	34039	0
\N	\N	f	t	f	\N	\N	\N	Batch	String	\N	34079	34047	0
AutoInduction Batch	\N	f	t	f	\N	\N	\N	Culture Type	String	\N	34081	34047	1
10	\N	f	t	f	\N	\N	\N	Culture Volume	Int	\N	34083	34047	2
\N	\N	f	t	f	\N	\N	\N	Reference Number	Int	\N	34087	34085	0
\N	\N	f	t	f	\N	\N	\N	Template Name	String	\N	34089	34085	1
\N	\N	f	t	f	\N	\N	\N	Template Concentration	Float	\N	34091	34085	2
\N	\N	f	t	f	\N	\N	\N	Template Volume	Float	\N	34093	34085	3
\N	\N	f	t	f	\N	\N	\N	Template Length	Int	\N	34095	34085	4
\N	\N	f	t	f	\N	\N	\N	Primer Name	String	\N	34097	34085	5
\N	\N	f	t	f	\N	\N	\N	Primer Volume	Float	\N	34099	34085	6
\N	\N	f	t	f	\N	\N	\N	Primer Concentration	Float	\N	34101	34085	7
No	\N	f	t	f	\N	\N	\N	Return Sample?	String	\N	34103	34085	8
\N	\N	f	t	f	\N	\N	\N	Account Number	String	\N	34105	34085	9
\N	\N	f	t	f	\N	\N	\N	User Email	String	\N	34107	34085	10
\N	\N	f	t	f	\N	\N	\N	Order ID	String	\N	34109	34085	11
\N	\N	f	t	f	\N	\N	\N	Reverse Overlap	Int	\N	36009	36001	0
\N	\N	f	t	f	\N	\N	\N	Forward Tag	String	\N	36011	36001	1
\N	\N	f	t	f	\N	\N	\N	Fwd. Primer GC	Float	\N	36013	36001	2
\N	\N	f	t	f	\N	\N	\N	Forward Overlap	Int	\N	36015	36001	3
\N	\N	f	t	f	\N	\N	\N	Rev. Primer GC	Float	\N	36017	36001	4
\N	\N	f	t	f	\N	\N	\N	Reverse Tag	String	\N	36019	36001	5
\N	\N	f	t	f	\N	\N	\N	Forward Primer Tm	Float	\N	36029	36021	0
\N	\N	f	t	f	\N	\N	\N	Forward Primer	String	\N	36031	36021	1
\N	\N	f	t	f	\N	\N	\N	Forward Overlap	String	\N	36033	36021	2
\N	\N	f	t	f	\N	\N	\N	Forward Tag	String	\N	36035	36021	3
\N	\N	f	t	f	\N	\N	\N	Reverse Tag	String	\N	36037	36021	4
\N	\N	f	t	f	\N	\N	\N	Reverse Overlap	String	\N	36039	36021	5
\N	\N	f	t	f	\N	\N	\N	Reverse Primer	String	\N	36041	36021	6
\N	\N	f	t	f	\N	\N	\N	Reverse Primer Tm	Float	\N	36043	36021	7
\N	\N	f	t	f	\N	\N	\N	Forward Primer Layout Notes	String	\N	36051	36045	0
\N	\N	f	t	f	\N	\N	\N	Reverse Primer Layout Notes	String	\N	36059	36053	0
\N	\N	f	t	f	\N	\N	\N	Template Layout Notes	String	\N	36067	36061	0
\N	\N	f	t	f	\N	\N	\N	Primer Dilution Notes	String	\N	36077	36069	0
\N	\N	f	t	f	\N	\N	\N	Reverse Dilution Notes	String	\N	36087	36079	0
2	\N	f	t	f	\N	\N	\N	HOT start /min	Int	\N	36117	36097	0
94	\N	f	t	f	\N	\N	\N	Denat/oC	Int	\N	36119	36097	1
30	\N	f	t	f	\N	\N	\N	Denat/s	Int	\N	36121	36097	2
60	\N	f	t	f	\N	\N	\N	Anneal/oC	Int	\N	36123	36097	3
30	\N	f	t	f	\N	\N	\N	Anneal time/s	Int	\N	36125	36097	4
72	\N	f	t	f	\N	\N	\N	Extension/oC	Int	\N	36127	36097	5
2	\N	f	t	f	\N	\N	\N	Extension/min	Int	\N	36129	36097	6
68	\N	f	t	f	\N	\N	\N	Final extension/oC	Int	\N	36131	36097	7
2	\N	f	t	f	\N	\N	\N	Final extension/min	Int	\N	36133	36097	8
30	\N	f	t	f	\N	\N	\N	No. Cycles	Int	\N	36135	36097	9
\N	\N	f	t	f	\N	\N	\N	PCR's Notes	String	\N	36137	36097	10
\N	\N	f	t	f	\N	\N	\N	Original plate position	String	\N	36139	36097	11
Unscored	\N	f	t	f	PCR Product	\N	\N	__SCORE	String	\N	36141	36097	12
true	\N	f	t	f	Was purified DNA checked on gel?	\N	\N	Checked on gel?	Boolean	\N	36151	36143	0
5	\N	f	t	f	\N	\N	\N	Reaction volume/uL	Float	\N	36161	36153	0
\N	\N	f	t	f	\N	\N	\N	Infusion Notes	String	\N	36163	36153	1
\N	\N	f	t	f	\N	\N	\N	Volume of cells plated out	Float	\N	36175	36165	0
Incubate 30 min on ice,   Heat shock 30 sec,   Incubate a further 2 min on ice	\N	f	t	f	\N	\N	\N	DNA Incubation conditions	String	\N	36177	36165	1
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	36179	36165	2
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	36181	36165	3
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	36183	36165	4
\N	\N	f	t	f	\N	\N	\N	Transformation Result	Int	\N	36185	36165	5
\N	\N	f	t	f	\N	\N	\N	Transformation Notes	String	\N	36187	36165	6
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	36199	36189	0
Qiagen Plasmid turbo	\N	f	t	f	\N	\N	\N	Kit name	String	\N	36201	36189	1
\N	\N	f	t	f	\N	\N	\N	PlasmidPrep Notes	String	\N	36203	36189	2
\N	\N	f	t	f	\N	\N	\N	Verification Result	Int	\N	36215	36205	0
\N	\N	f	t	f	\N	\N	\N	Verification Notes	String	\N	36217	36205	1
20	\N	f	t	f	\N	\N	\N	Induction Temperature	Int	\N	36233	36219	0
22	\N	f	t	f	\N	\N	\N	Induction time/hr	Int	\N	36235	36219	1
Unscored	\N	f	t	f	Soluble Expression level	\N	\N	__MW	String	\N	36237	36219	2
Unscored	\N	f	t	f	InSoluble Expression level	\N	\N	__IMW	String	\N	36239	36219	3
\N	\N	f	t	f	\N	\N	\N	Expression Notes	String	\N	36241	36219	4
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36409	36397	0
\N	\N	f	t	f	\N	\N	\N	B Column 2	String	\N	36411	36397	1
\N	\N	f	f	f	\N	\N	\N	C Purification Notes	String	\N	36413	36397	2
\N	\N	f	t	f	\N	\N	\N	D Fractions Pooled Pool 1	String	\N	36415	36397	3
\N	mL	f	t	f	\N	\N	\N	E Volume of Pool Pool 1	Float	\N	36417	36397	4
\N	mg/ml	f	t	f	\N	\N	\N	F [Protein of Pool] Pool 1	Float	\N	36419	36397	5
\N	mg	f	t	f	\N	\N	\N	G Yield Pool 1	Float	\N	36421	36397	6
\N	\N	f	t	f	\N	\N	\N	H Oligomeric State Pool 1	String	\N	36423	36397	7
\N	\N	f	f	f	\N	\N	\N	I Fractions Pooled Pool 2	String	\N	36425	36397	8
\N	mL	f	f	f	\N	\N	\N	J Volume of Pool Pool 2	Float	\N	36427	36397	9
\N	mg/ml	f	f	f	\N	\N	\N	K [Protein of Pool] Pool 2	Float	\N	36429	36397	10
\N	mg	f	f	f	\N	\N	\N	L Yield Pool 2	Float	\N	36431	36397	11
\N	\N	f	f	f	\N	\N	\N	M Oligomeric State Pool 2	String	\N	36433	36397	12
\N	\N	f	f	f	\N	\N	\N	N Fractions Pooled Pool 3	String	\N	36435	36397	13
\N	mL	f	f	f	\N	\N	\N	O Volume of Pool Pool 3	Float	\N	36437	36397	14
\N	mg/ml	f	f	f	\N	\N	\N	P [Protein of Pool] Pool 3	Float	\N	36439	36397	15
\N	mg	f	f	f	\N	\N	\N	Q Yield Pool 3	Float	\N	36441	36397	16
\N	\N	f	f	f	\N	\N	\N	R Oligomeric State Pool 3	String	\N	36443	36397	17
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36457	36445	0
\N	\N	f	t	f	\N	\N	\N	B Column 2	String	\N	36459	36445	1
\N	\N	f	f	f	\N	\N	\N	C Purification Notes	String	\N	36461	36445	2
\N	\N	f	t	f	\N	\N	\N	D Fractions Pooled Pool 1	String	\N	36463	36445	3
\N	mL	f	t	f	\N	\N	\N	E Volume of Pool Pool 1	Float	\N	36465	36445	4
\N	mg/ml	f	t	f	\N	\N	\N	F [Protein of Pool] Pool 1	Float	\N	36467	36445	5
Unscored	\N	f	t	f	Sequencing Result	\N	\N	__SEQUENCE	String	\N	36251	36243	0
\N	\N	f	t	f	\N	\N	\N	Sequencing Notes	String	\N	36253	36243	1
25	\N	f	t	f	\N	\N	\N	Induction Temperature	Int	\N	36269	36255	0
22	\N	f	t	f	\N	\N	\N	Induction time/hr	Int	\N	36271	36255	1
\N	\N	f	t	f	\N	\N	\N	Expression level	Int	\N	36273	36255	2
\N	\N	f	t	f	\N	\N	\N	Weight of Pellet	Int	\N	36275	36255	3
\N	\N	f	t	f	\N	\N	\N	Expression Notes	String	\N	36277	36255	4
Soluble	\N	f	t	f	\N	\N	\N	Soluble/Insoluble	String	\N	36279	36255	5
false	\N	f	t	f	\N	\N	\N	Selenomethionine Labelled	Boolean	\N	36281	36255	6
Cell Disruptor	\N	f	t	f	\N	\N	\N	Method of Cell Disruption	String	\N	36297	36283	0
\N	\N	f	t	f	\N	\N	\N	Volume of Lysate (ml)	Float	\N	36299	36283	1
\N	\N	f	t	f	\N	\N	\N	Lysis Notes	String	\N	36301	36283	2
20	\N	f	t	f	\N	\N	\N	Fractions Pooled Pool 1	Int	\N	36315	36303	0
22	\N	f	t	f	\N	\N	\N	Volume of Pool Pool 1	Int	\N	36317	36303	1
\N	\N	f	t	f	\N	\N	\N	[Protein of Pool] Pool 1	Int	\N	36319	36303	2
\N	\N	f	t	f	\N	\N	\N	Yield Pool 1	Int	\N	36321	36303	3
\N	\N	f	t	f	\N	\N	\N	Oligomeric State Pool 1	Int	\N	36323	36303	4
20	\N	f	t	f	\N	\N	\N	Fractions Pooled Pool 2	Int	\N	36325	36303	5
22	\N	f	t	f	\N	\N	\N	Volume of Pool Pool 2	Int	\N	36327	36303	6
\N	\N	f	t	f	\N	\N	\N	[Protein of Pool] Pool 2	Int	\N	36329	36303	7
\N	\N	f	t	f	\N	\N	\N	Yield Pool 2	Int	\N	36331	36303	8
\N	\N	f	t	f	\N	\N	\N	Oligomeric State Pool 2	Int	\N	36333	36303	9
20	\N	f	t	f	\N	\N	\N	Fractions Pooled Pool 3	Int	\N	36335	36303	10
22	\N	f	t	f	\N	\N	\N	Volume of Pool Pool 3	Int	\N	36337	36303	11
\N	\N	f	t	f	\N	\N	\N	[Protein of Pool] Pool 3	Int	\N	36339	36303	12
\N	\N	f	t	f	\N	\N	\N	Yield Pool 3	Int	\N	36341	36303	13
\N	\N	f	t	f	\N	\N	\N	Oligomeric State Pool 3	Int	\N	36343	36303	14
\N	\N	f	t	f	\N	\N	\N	Column Type	String	\N	36345	36303	15
\N	\N	f	t	f	\N	\N	\N	Purification Notes	String	\N	36347	36303	16
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36361	36349	0
\N	\N	f	t	f	\N	\N	\N	B Column 2	String	\N	36363	36349	1
\N	\N	f	f	f	\N	\N	\N	C Purification Notes	String	\N	36365	36349	2
\N	\N	f	t	f	\N	\N	\N	D Fractions Pooled Pool 1	String	\N	36367	36349	3
\N	mL	f	t	f	\N	\N	\N	E Volume of Pool Pool 1	Float	\N	36369	36349	4
\N	mg/ml	f	t	f	\N	\N	\N	F [Protein of Pool] Pool 1	Float	\N	36371	36349	5
\N	mg	f	t	f	\N	\N	\N	G Yield Pool 1	Float	\N	36373	36349	6
\N	\N	f	t	f	\N	\N	\N	H Oligomeric State Pool 1	String	\N	36375	36349	7
\N	\N	f	f	f	\N	\N	\N	I Fractions Pooled Pool 2	String	\N	36377	36349	8
\N	mL	f	f	f	\N	\N	\N	J Volume of Pool Pool 2	Float	\N	36379	36349	9
\N	mg/ml	f	f	f	\N	\N	\N	K [Protein of Pool] Pool 2	Float	\N	36381	36349	10
\N	mg	f	f	f	\N	\N	\N	L Yield Pool 2	Float	\N	36383	36349	11
\N	\N	f	f	f	\N	\N	\N	M Oligomeric State Pool 2	String	\N	36385	36349	12
\N	\N	f	f	f	\N	\N	\N	N Fractions Pooled Pool 3	String	\N	36387	36349	13
\N	mL	f	f	f	\N	\N	\N	O Volume of Pool Pool 3	Float	\N	36389	36349	14
\N	mg/ml	f	f	f	\N	\N	\N	P [Protein of Pool] Pool 3	Float	\N	36391	36349	15
\N	mg	f	f	f	\N	\N	\N	Q Yield Pool 3	Float	\N	36393	36349	16
\N	\N	f	f	f	\N	\N	\N	R Oligomeric State Pool 3	String	\N	36395	36349	17
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36505	36493	0
\N	\N	f	t	f	\N	\N	\N	B Column 2	String	\N	36507	36493	1
\N	\N	f	f	f	\N	\N	\N	C Purification Notes	String	\N	36509	36493	2
\N	\N	f	t	f	\N	\N	\N	D Fractions Pooled Pool 1	String	\N	36511	36493	3
\N	mL	f	t	f	\N	\N	\N	E Volume of Pool Pool 1	Float	\N	36513	36493	4
\N	mg/ml	f	t	f	\N	\N	\N	F [Protein of Pool] Pool 1	Float	\N	36515	36493	5
\N	mg	f	t	f	\N	\N	\N	G Yield Pool 1	Float	\N	36517	36493	6
\N	\N	f	t	f	\N	\N	\N	H Oligomeric State Pool 1	String	\N	36519	36493	7
\N	\N	f	f	f	\N	\N	\N	I Fractions Pooled Pool 2	String	\N	36521	36493	8
\N	mL	f	f	f	\N	\N	\N	J Volume of Pool Pool 2	Float	\N	36523	36493	9
\N	mg/ml	f	f	f	\N	\N	\N	K [Protein of Pool] Pool 2	Float	\N	36525	36493	10
\N	mg	f	f	f	\N	\N	\N	L Yield Pool 2	Float	\N	36527	36493	11
\N	\N	f	f	f	\N	\N	\N	M Oligomeric State Pool 2	String	\N	36529	36493	12
\N	\N	f	f	f	\N	\N	\N	N Fractions Pooled Pool 3	String	\N	36531	36493	13
\N	mL	f	f	f	\N	\N	\N	O Volume of Pool Pool 3	Float	\N	36533	36493	14
\N	mg/ml	f	f	f	\N	\N	\N	P [Protein of Pool] Pool 3	Float	\N	36535	36493	15
\N	mg	f	f	f	\N	\N	\N	Q Yield Pool 3	Float	\N	36537	36493	16
\N	\N	f	f	f	\N	\N	\N	R Oligomeric State Pool 3	String	\N	36539	36493	17
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36551	36541	0
\N	\N	f	f	f	\N	\N	\N	B Purification Notes	String	\N	36553	36541	1
\N	\N	f	t	f	\N	\N	\N	C Fractions Pooled Pool 1	String	\N	36555	36541	2
\N	mL	f	t	f	\N	\N	\N	D Volume of Pool Pool 1	Float	\N	36557	36541	3
\N	mg/ml	f	t	f	\N	\N	\N	E [Protein of Pool] Pool 1	Float	\N	36559	36541	4
\N	mg	f	t	f	\N	\N	\N	F Yield Pool 1	Float	\N	36561	36541	5
\N	\N	f	t	f	\N	\N	\N	G Oligomeric State Pool 1	String	\N	36563	36541	6
\N	\N	f	f	f	\N	\N	\N	H Fractions Pooled Pool 2	String	\N	36565	36541	7
\N	mL	f	f	f	\N	\N	\N	I Volume of Pool Pool 2	Float	\N	36567	36541	8
\N	mg/ml	f	f	f	\N	\N	\N	J [Protein of Pool] Pool 2	Float	\N	36569	36541	9
\N	mg	f	f	f	\N	\N	\N	K Yield Pool 2	Float	\N	36571	36541	10
\N	mg	f	t	f	\N	\N	\N	G Yield Pool 1	Float	\N	36469	36445	6
\N	\N	f	t	f	\N	\N	\N	H Oligomeric State Pool 1	String	\N	36471	36445	7
\N	\N	f	f	f	\N	\N	\N	I Fractions Pooled Pool 2	String	\N	36473	36445	8
\N	mL	f	f	f	\N	\N	\N	J Volume of Pool Pool 2	Float	\N	36475	36445	9
\N	mg/ml	f	f	f	\N	\N	\N	K [Protein of Pool] Pool 2	Float	\N	36477	36445	10
\N	mg	f	f	f	\N	\N	\N	L Yield Pool 2	Float	\N	36479	36445	11
\N	\N	f	f	f	\N	\N	\N	M Oligomeric State Pool 2	String	\N	36481	36445	12
\N	\N	f	f	f	\N	\N	\N	N Fractions Pooled Pool 3	String	\N	36483	36445	13
\N	mL	f	f	f	\N	\N	\N	O Volume of Pool Pool 3	Float	\N	36485	36445	14
\N	mg/ml	f	f	f	\N	\N	\N	P [Protein of Pool] Pool 3	Float	\N	36487	36445	15
\N	mg	f	f	f	\N	\N	\N	Q Yield Pool 3	Float	\N	36489	36445	16
\N	\N	f	f	f	\N	\N	\N	R Oligomeric State Pool 3	String	\N	36491	36445	17
\N	\N	f	f	f	\N	\N	\N	L Oligomeric State Pool 2	String	\N	36573	36541	11
\N	\N	f	f	f	\N	\N	\N	M Fractions Pooled Pool 3	String	\N	36575	36541	12
\N	mL	f	f	f	\N	\N	\N	N Volume of Pool Pool 3	Float	\N	36577	36541	13
\N	mg/ml	f	f	f	\N	\N	\N	O [Protein of Pool] Pool 3	Float	\N	36579	36541	14
\N	mg	f	f	f	\N	\N	\N	P Yield Pool 3	Float	\N	36581	36541	15
\N	\N	f	f	f	\N	\N	\N	Q Oligomeric State Pool 3	String	\N	36583	36541	16
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36593	36585	0
\N	\N	f	f	f	\N	\N	\N	B Purification Notes	String	\N	36595	36585	1
\N	\N	f	t	f	\N	\N	\N	C Fractions Pooled Pool 1	String	\N	36597	36585	2
\N	mL	f	t	f	\N	\N	\N	D Volume of Pool Pool 1	Float	\N	36599	36585	3
\N	mg/ml	f	t	f	\N	\N	\N	E [Protein of Pool] Pool 1	Float	\N	36601	36585	4
\N	mg	f	t	f	\N	\N	\N	F Yield Pool 1	Float	\N	36603	36585	5
\N	\N	f	t	f	\N	\N	\N	G Oligomeric State Pool 1	String	\N	36605	36585	6
\N	\N	f	f	f	\N	\N	\N	H Fractions Pooled Pool 2	String	\N	36607	36585	7
\N	mL	f	f	f	\N	\N	\N	I Volume of Pool Pool 2	Float	\N	36609	36585	8
\N	mg/ml	f	f	f	\N	\N	\N	J [Protein of Pool] Pool 2	Float	\N	36611	36585	9
\N	mg	f	f	f	\N	\N	\N	K Yield Pool 2	Float	\N	36613	36585	10
\N	\N	f	f	f	\N	\N	\N	L Oligomeric State Pool 2	String	\N	36615	36585	11
\N	\N	f	f	f	\N	\N	\N	M Fractions Pooled Pool 3	String	\N	36617	36585	12
\N	mL	f	f	f	\N	\N	\N	N Volume of Pool Pool 3	Float	\N	36619	36585	13
\N	mg/ml	f	f	f	\N	\N	\N	O [Protein of Pool] Pool 3	Float	\N	36621	36585	14
\N	mg	f	f	f	\N	\N	\N	P Yield Pool 3	Float	\N	36623	36585	15
\N	\N	f	f	f	\N	\N	\N	Q Oligomeric State Pool 3	String	\N	36625	36585	16
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36635	36627	0
\N	\N	f	f	f	\N	\N	\N	B Purification Notes	String	\N	36637	36627	1
\N	\N	f	t	f	\N	\N	\N	C Fractions Pooled Pool 1	String	\N	36639	36627	2
\N	mL	f	t	f	\N	\N	\N	D Volume of Pool Pool 1	Float	\N	36641	36627	3
\N	mg/ml	f	t	f	\N	\N	\N	E [Protein of Pool] Pool 1	Float	\N	36643	36627	4
\N	mg	f	t	f	\N	\N	\N	F Yield Pool 1	Float	\N	36645	36627	5
\N	\N	f	t	f	\N	\N	\N	G Oligomeric State Pool 1	String	\N	36647	36627	6
\N	\N	f	f	f	\N	\N	\N	H Fractions Pooled Pool 2	String	\N	36649	36627	7
\N	mL	f	f	f	\N	\N	\N	I Volume of Pool Pool 2	Float	\N	36651	36627	8
\N	mg/ml	f	f	f	\N	\N	\N	J [Protein of Pool] Pool 2	Float	\N	36653	36627	9
\N	mg	f	f	f	\N	\N	\N	K Yield Pool 2	Float	\N	36655	36627	10
\N	\N	f	f	f	\N	\N	\N	L Oligomeric State Pool 2	String	\N	36657	36627	11
\N	\N	f	f	f	\N	\N	\N	M Fractions Pooled Pool 3	String	\N	36659	36627	12
\N	mL	f	f	f	\N	\N	\N	N Volume of Pool Pool 3	Float	\N	36661	36627	13
\N	mg/ml	f	f	f	\N	\N	\N	O [Protein of Pool] Pool 3	Float	\N	36663	36627	14
\N	mg	f	f	f	\N	\N	\N	P Yield Pool 3	Float	\N	36665	36627	15
\N	\N	f	f	f	\N	\N	\N	Q Oligomeric State Pool 3	String	\N	36667	36627	16
\N	\N	f	t	f	\N	\N	\N	A Column 1	String	\N	36679	36669	0
\N	\N	f	f	f	\N	\N	\N	B Purification Notes	String	\N	36681	36669	1
\N	\N	f	t	f	\N	\N	\N	C Fractions Pooled Pool 1	String	\N	36683	36669	2
\N	mL	f	t	f	\N	\N	\N	D Volume of Pool Pool 1	Float	\N	36685	36669	3
\N	mg/ml	f	t	f	\N	\N	\N	E [Protein of Pool] Pool 1	Float	\N	36687	36669	4
\N	mg	f	t	f	\N	\N	\N	F Yield Pool 1	Float	\N	36689	36669	5
\N	\N	f	t	f	\N	\N	\N	G Oligomeric State Pool 1	String	\N	36691	36669	6
\N	\N	f	f	f	\N	\N	\N	H Fractions Pooled Pool 2	String	\N	36693	36669	7
\N	mL	f	f	f	\N	\N	\N	I Volume of Pool Pool 2	Float	\N	36695	36669	8
\N	mg/ml	f	f	f	\N	\N	\N	J [Protein of Pool] Pool 2	Float	\N	36697	36669	9
\N	mg	f	f	f	\N	\N	\N	K Yield Pool 2	Float	\N	36699	36669	10
\N	\N	f	f	f	\N	\N	\N	L Oligomeric State Pool 2	String	\N	36701	36669	11
\N	\N	f	f	f	\N	\N	\N	M Fractions Pooled Pool 3	String	\N	36703	36669	12
\N	mL	f	f	f	\N	\N	\N	N Volume of Pool Pool 3	Float	\N	36705	36669	13
\N	mg/ml	f	f	f	\N	\N	\N	O [Protein of Pool] Pool 3	Float	\N	36707	36669	14
\N	mg	f	f	f	\N	\N	\N	P Yield Pool 3	Float	\N	36709	36669	15
\N	\N	f	f	f	\N	\N	\N	Q Oligomeric State Pool 3	String	\N	36711	36669	16
\N	\N	f	f	f	\N	\N	\N	A Purification Notes	String	\N	36725	36713	0
\N	\N	f	t	f	\N	\N	\N	B Fractions Pooled Pool 1	String	\N	36727	36713	1
\N	mL	f	t	f	\N	\N	\N	C Volume of Pool Pool 1	Float	\N	36729	36713	2
\N	mg/ml	f	t	f	\N	\N	\N	D [Protein of Pool] Pool 1	Float	\N	36731	36713	3
\N	mg	f	t	f	\N	\N	\N	E Yield Pool 1	Float	\N	36733	36713	4
\N	\N	f	t	f	\N	\N	\N	F Oligomeric State Pool 1	String	\N	36735	36713	5
\N	\N	f	f	f	\N	\N	\N	G Fractions Pooled Pool 2	String	\N	36737	36713	6
\N	mL	f	f	f	\N	\N	\N	H Volume of Pool Pool 2	Float	\N	36739	36713	7
\N	mg/ml	f	f	f	\N	\N	\N	I [Protein of Pool] Pool 2	Float	\N	36741	36713	8
\N	mg	f	f	f	\N	\N	\N	J Yield Pool 2	Float	\N	36743	36713	9
\N	\N	f	f	f	\N	\N	\N	K Oligomeric State Pool 2	String	\N	36745	36713	10
\N	\N	f	f	f	\N	\N	\N	L Fractions Pooled Pool 3	String	\N	36747	36713	11
\N	mL	f	f	f	\N	\N	\N	M Volume of Pool Pool 3	Float	\N	36749	36713	12
\N	mg/ml	f	f	f	\N	\N	\N	N [Protein of Pool] Pool 3	Float	\N	36751	36713	13
\N	mg	f	f	f	\N	\N	\N	O Yield Pool 3	Float	\N	36753	36713	14
\N	\N	f	f	f	\N	\N	\N	P Oligomeric State Pool 3	String	\N	36755	36713	15
\N	\N	f	t	f	\N	\N	\N	Incubation temperature	Int	\N	36769	36757	0
\N	\N	f	t	f	\N	\N	\N	Incubation time	Int	\N	36771	36757	1
\N	\N	f	t	f	\N	\N	\N	Yield	Int	\N	36773	36757	2
\N	\N	f	t	f	\N	\N	\N	% Cleavage	Int	\N	36775	36757	3
\N	\N	f	t	f	\N	\N	\N	Cleavage Notes	String	\N	36777	36757	4
\N	\N	f	t	f	\N	\N	\N	Concentrator type	String	\N	36785	36779	0
\N	\N	f	t	f	\N	\N	\N	Concentrator speed (g)	Int	\N	36787	36779	1
\N	\N	f	t	f	\N	\N	\N	Output volume	Int	\N	36789	36779	2
\N	\N	f	t	f	\N	\N	\N	Output concentration	Int	\N	36791	36779	3
\N	\N	f	t	f	\N	\N	\N	Output yield	Int	\N	36793	36779	4
\N	\N	f	t	f	\N	\N	\N	Concentrator Notes	String	\N	36795	36779	5
\N	\N	f	t	f	\N	\N	\N	Molecular Weight	Float	\N	36803	36797	0
\N	\N	f	t	f	\N	\N	\N	Mass Spec Notes	String	\N	36805	36797	1
\N	\N	f	t	f	\N	\N	\N	% SelMet	Int	\N	36807	36797	2
\N	\N	f	t	f	\N	\N	\N	Is Expected Molecular Weight	Boolean	\N	36809	36797	3
\N	\N	f	t	f	\N	\N	\N	Protein pI	Float	\N	38003	38001	0
\N	\N	f	t	f	\N	\N	\N	TMHMM	String	\N	38005	38001	1
\N	\N	f	t	f	\N	\N	\N	GRAVY/PI Cluster	String	\N	38007	38001	2
\N	\N	f	t	f	\N	\N	\N	Length	Int	\N	38009	38001	3
\N	\N	f	t	f	\N	\N	\N	GRAVY	Float	\N	38011	38001	4
\N	\N	f	t	f	\N	\N	\N	Weight	Float	\N	38013	38001	5
\N	\N	f	t	f	\N	\N	\N	Disorder	String	\N	38015	38001	6
\N	\N	f	t	f	\N	\N	\N	PSSM	String	\N	38017	38001	7
\N	\N	f	t	f	\N	\N	\N	SignalP	String	\N	38019	38001	8
\N	\N	f	t	f	\N	\N	\N	Extinction	Float	\N	38021	38001	9
0	\N	f	t	f	\N	\N	\N	A. Solubility Result	String	\N	38029	38023	0
Native	\N	f	t	f	\N	\N	\N	B. Native/Selmet	String	\N	38031	38023	1
BL21 (DE3)	\N	f	t	f	\N	\N	\N	C. Cell Line	String	\N	38033	38023	2
100ug/ml Ampicillin	\N	f	t	f	\N	\N	\N	D. Antibiotic	String	\N	38035	38023	3
TPB	\N	f	t	f	\N	\N	\N	E. Media	String	\N	38037	38023	4
0.2	\N	f	t	f	\N	\N	\N	F. IPTG concentration (mM)	Float	\N	38039	38023	5
37	\N	f	t	f	\N	\N	\N	G. Pre-Induction Temperature (C)	Float	\N	38041	38023	6
25	\N	f	t	f	\N	\N	\N	H. Post-Induction Temperature (C)	Float	\N	38043	38023	7
Glycerol Stock	\N	f	t	f	\N	\N	\N	I. Inoculum type (Glycerol Stock/ Colonies	String	\N	38045	38023	8
Overnight	\N	f	t	f	\N	\N	\N	J. Induction duration	String	\N	38047	38023	9
1:100	\N	f	t	f	\N	\N	\N	K. Ratio of Inoculum	String	\N	38049	38023	10
0.6	\N	f	t	f	\N	\N	\N	L. OD(600nm) at Induction	Float	\N	38051	38023	11
200	\N	f	t	f	\N	\N	\N	M. Shaker R.P.M.	Int	\N	38053	38023	12
500	\N	f	t	f	\N	\N	\N	N. Scale-up volume (ml)	Int	\N	38055	38023	13
\N	\N	f	t	f	\N	\N	\N	1a. Date of Expression	String	\N	38063	38057	0
Large	\N	f	t	f	\N	\N	\N	1b. Scale	String	\N	38065	38057	1
Native	\N	f	t	f	\N	\N	\N	1c. Native/Seleno	String	\N	38067	38057	2
\N	\N	f	t	f	\N	\N	\N	1d. Lysis date	String	\N	38069	38057	3
\N	\N	f	t	f	\N	\N	\N	1e. Date frozen	String	\N	38071	38057	4
\N	\N	f	t	f	\N	\N	\N	1f. Mass and Extinction coeff	String	\N	38073	38057	5
\N	\N	f	t	f	\N	\N	\N	1g. Mass of cells/L culture (g)	Float	\N	38075	38057	6
\N	\N	f	t	f	\N	\N	\N	1h. Expected yield (mg)	Float	\N	38077	38057	7
\N	\N	f	t	f	\N	\N	\N	1i. Final yield (mg)	Float	\N	38079	38057	8
\N	\N	f	t	f	\N	\N	\N	1j. Freezer location	String	\N	38081	38057	9
\N	\N	f	t	f	\N	\N	\N	1k. Aliquots (number)	String	\N	38083	38057	10
\N	\N	f	t	f	\N	\N	\N	1l. Concentration (mg/ml)	Float	\N	38085	38057	11
\N	\N	f	t	f	\N	\N	\N	2a. Lysis method	String	\N	38087	38057	12
Akta	\N	f	t	f	\N	\N	\N	2b. Bench or Akta	String	\N	38089	38057	13
\N	\N	f	t	f	\N	\N	\N	2c. Akta method	String	\N	38091	38057	14
\N	\N	f	t	f	\N	\N	\N	2d. Brand of Ni resin	String	\N	38093	38057	15
\N	\N	f	t	f	\N	\N	\N	2e. Volume of Ni resin (ml)	String	\N	38095	38057	16
\N	\N	f	t	f	\N	\N	\N	2f. [Immidazole] for lysis	String	\N	38097	38057	17
\N	\N	f	t	f	\N	\N	\N	2g. [Immidazole] for wash	String	\N	38099	38057	18
\N	\N	f	t	f	\N	\N	\N	2h. Recommended [Immidazole]	String	\N	38101	38057	19
\N	\N	f	t	f	\N	\N	\N	2i. Yield (mg) after desalt	Float	\N	38103	38057	20
\N	\N	f	t	f	\N	\N	\N	3a. Amount TEV added (mg)	String	\N	38105	38057	21
\N	\N	f	t	f	\N	\N	\N	3b. TEV efficiency	String	\N	38107	38057	22
\N	\N	f	t	f	\N	\N	\N	3c. Length of incubation	String	\N	38109	38057	23
1	\N	f	t	f	\N	\N	\N	3d. Amount DTT added (mM)	Float	\N	38111	38057	24
\N	\N	f	t	f	\N	\N	\N	3e. Yield (mg) after TEV	Float	\N	38113	38057	25
150	\N	f	t	f	\N	\N	\N	4a. GF [NaCL] (mM)	Int	\N	38115	38057	26
\N	\N	f	t	f	\N	\N	\N	4b. Other additives	String	\N	38117	38057	27
S200	\N	f	t	f	\N	\N	\N	4c. GF column	String	\N	38119	38057	28
\N	\N	f	t	f	\N	\N	\N	4d. Yield (mg) after GF	Float	\N	38121	38057	29
\N	\N	f	t	f	\N	\N	\N	4e. Pooled fractions	String	\N	38123	38057	30
\N	\N	f	t	f	\N	\N	\N	5a. Final concentration	String	\N	38125	38057	31
\N	\N	f	t	f	\N	\N	\N	5b. Final buffer	String	\N	38127	38057	32
\N	\N	f	t	f	\N	\N	\N	5c. Concentration 2xPCT	String	\N	38129	38057	33
\N	\N	f	t	f	\N	\N	\N	5d. SOP used	String	\N	38131	38057	34
\N	\N	f	t	f	\N	\N	\N	5e. Purification Notes	String	\N	38133	38057	35
0	\N	f	t	f	\N	\N	\N	A. Solubility Result	String	\N	38159	38153	0
Native	\N	f	t	f	\N	\N	\N	B. Native/Selmet	String	\N	38161	38153	1
BL21 (DE3)	\N	f	t	f	\N	\N	\N	C. Cell Line	String	\N	38163	38153	2
100ug/ml Ampicillin	\N	f	t	f	\N	\N	\N	D. Antibiotic	String	\N	38165	38153	3
Auto-induction	\N	f	t	f	\N	\N	\N	E. Media	String	\N	38167	38153	4
Glycerol Stock	\N	f	t	f	\N	\N	\N	F. Inoculum type (Glycerol Stock/ Colonies	String	\N	38169	38153	5
Overnight	\N	f	t	f	\N	\N	\N	G. Induction duration	String	\N	38171	38153	6
1:100	\N	f	t	f	\N	\N	\N	H. Ratio of Inoculum	String	\N	38173	38153	7
200	\N	f	t	f	\N	\N	\N	I. Shaker R.P.M.	Int	\N	38175	38153	8
500	\N	f	t	f	\N	\N	\N	J. Scale-up volume (ml)	Int	\N	38177	38153	9
0	\N	f	t	f	\N	\N	\N	A. Solubility Result	String	\N	38185	38179	0
Native	\N	f	t	f	\N	\N	\N	B. Native/Selmet	String	\N	38187	38179	1
BL21 (DE3)	\N	f	t	f	\N	\N	\N	C. Cell Line	String	\N	38189	38179	2
100ug/ml Ampicillin	\N	f	t	f	\N	\N	\N	D. Antibiotic	String	\N	38191	38179	3
TPB	\N	f	t	f	\N	\N	\N	E. Media	String	\N	38193	38179	4
0.2	\N	f	t	f	\N	\N	\N	F. IPTG concentration (mM)	Float	\N	38195	38179	5
37	\N	f	t	f	\N	\N	\N	G. Pre-Induction Temperature (C)	Float	\N	38197	38179	6
25	\N	f	t	f	\N	\N	\N	H. Post-Induction Temperature (C)	Float	\N	38199	38179	7
Glycerol Stock	\N	f	t	f	\N	\N	\N	I. Inoculum type (Glycerol Stock/ Colonies	String	\N	38201	38179	8
Overnight	\N	f	t	f	\N	\N	\N	J. Induction duration	String	\N	38203	38179	9
1:100	\N	f	t	f	\N	\N	\N	K. Ratio of Inoculum	String	\N	38205	38179	10
0.6	\N	f	t	f	\N	\N	\N	L. OD(600nm) at Induction	Float	\N	38207	38179	11
200	\N	f	t	f	\N	\N	\N	M. Shaker R.P.M.	Int	\N	38209	38179	12
500	\N	f	t	f	\N	\N	\N	N. Scale-up volume (ml)	Int	\N	38211	38179	13
0	\N	f	t	f	\N	\N	\N	A. Solubility Result	String	\N	38219	38213	0
Native	\N	f	t	f	\N	\N	\N	B. Native/Selmet	String	\N	38221	38213	1
BL21 (DE3)	\N	f	t	f	\N	\N	\N	C. Cell Line	String	\N	38223	38213	2
100ug/ml Ampicillin	\N	f	t	f	\N	\N	\N	D. Antibiotic	String	\N	38225	38213	3
Auto-induction	\N	f	t	f	\N	\N	\N	E. Media	String	\N	38227	38213	4
Glycerol Stock	\N	f	t	f	\N	\N	\N	F. Inoculum type (Glycerol Stock/ Colonies	String	\N	38229	38213	5
Overnight	\N	f	t	f	\N	\N	\N	G. Induction duration	String	\N	38231	38213	6
1:100	\N	f	t	f	\N	\N	\N	H. Ratio of Inoculum	String	\N	38233	38213	7
200	\N	f	t	f	\N	\N	\N	I. Shaker R.P.M.	Int	\N	38235	38213	8
500	\N	f	t	f	\N	\N	\N	J. Scale-up volume (ml)	Int	\N	38237	38213	9
0	\N	f	t	f	\N	\N	\N	A. Solubility Result	String	\N	38245	38239	0
Native	\N	f	t	f	\N	\N	\N	B. Native/Selmet	String	\N	38247	38239	1
BL21 (DE3)	\N	f	t	f	\N	\N	\N	C. Cell Line	String	\N	38249	38239	2
100ug/ml Ampicillin	\N	f	t	f	\N	\N	\N	D. Antibiotic	String	\N	38251	38239	3
TPB	\N	f	t	f	\N	\N	\N	E. Media	String	\N	38253	38239	4
0.2	\N	f	t	f	\N	\N	\N	F. IPTG concentration (mM)	Float	\N	38255	38239	5
37	\N	f	t	f	\N	\N	\N	G. Pre-Induction Temperature (C)	Float	\N	38257	38239	6
25	\N	f	t	f	\N	\N	\N	H. Post-Induction Temperature (C)	Float	\N	38259	38239	7
Glycerol Stock	\N	f	t	f	\N	\N	\N	I. Inoculum type (Glycerol Stock/ Colonies	String	\N	38261	38239	8
Overnight	\N	f	t	f	\N	\N	\N	J. Induction duration	String	\N	38263	38239	9
1:100	\N	f	t	f	\N	\N	\N	K. Ratio of Inoculum	String	\N	38265	38239	10
0.6	\N	f	t	f	\N	\N	\N	L. OD(600nm) at Induction	Float	\N	38267	38239	11
200	\N	f	t	f	\N	\N	\N	M. Shaker R.P.M.	Int	\N	38269	38239	12
500	\N	f	t	f	\N	\N	\N	N. Scale-up volume (ml)	Int	\N	38271	38239	13
\N	\N	f	t	f	\N	\N	\N	A. Extension time (sec)	Float	\N	40009	40001	0
35	\N	f	t	f	\N	\N	\N	B. Number of cycles	Int	\N	40011	40001	1
Anneal 30 sec at 45C,    Extend 30 sec at 72C,    Denature 30 sec at 94C	\N	f	t	f	\N	\N	\N	C. Thermocycling conditions	String	\N	40013	40001	2
100	\N	f	t	f	Primer stock concentration uM	\N	\N	D. Primer stock conc. uM	Float	\N	40015	40001	3
20	\N	f	t	f	Primer working concentration uM	\N	\N	E. Primer working conc. uM	Float	\N	40017	40001	4
P	\N	f	t	f	Plasmid DNA: P, Genomic DNA: G	\N	\N	F. Template type	String	\N	40019	40001	5
50	\N	f	t	f	Concentration of Template ng/ul	\N	\N	G. Template conc. ng/ul	Float	\N	40021	40001	6
\N	\N	f	t	f	Expected size (bp) of PCR product	\N	\N	H. Expected size	String	\N	40023	40001	7
true	\N	f	t	f	Is the PCR product the expected size?	\N	\N	I. Correct size?	Boolean	\N	40025	40001	8
KOD Hot Start DNA Polymerase (Novagen)	\N	f	t	f	Type of polymerase used	\N	\N	J. Polymerase	String	\N	40027	40001	9
\N	\N	f	t	f	Size of Insert in bp	\N	\N	A. Insert size (bp)	Int	\N	40035	40029	0
\N	\N	f	t	f	Amount (ng) insert required for LIC reaction (0.2pmol)	\N	\N	B. ng Insert needed	Int	\N	40037	40029	1
\N	\N	f	t	f	Concentration of Insert ng/ul	\N	\N	C. Insert conc. ng/ul	Float	\N	40039	40029	2
pET-YSBLIC	\N	f	t	f	Which LIC vector used	\N	\N	D. LIC vector	String	\N	40041	40029	3
50.0	\N	f	t	f	Concentration of vector ng/ul	\N	\N	E. Vector conc. ng/ul	Float	\N	40043	40029	4
Novagen LIC qualified T4 DNA pol	\N	f	t	f	T4 polymerase used for LIC reaction	\N	\N	F. T4 polymerase	String	\N	40045	40029	5
Nova Blue Giga Singles	\N	f	t	f	Competent cells used for Transformation	\N	\N	G. Competent cells	String	\N	40047	40029	6
Yes	\N	f	t	f	Any colonies? Yes/No, or number	\N	\N	H. Colonies	String	\N	40049	40029	7
NcoI	\N	f	t	f	Name of first restriction enzyme	\N	\N	A. Enzyme 1	String	\N	40057	40051	0
NdeI	\N	f	t	f	Name of second restriction enzyme	\N	\N	B. Enzyme 2	String	\N	40059	40051	1
1 hour at 37C	\N	f	t	f	\N	\N	\N	C. Digest conditions	String	\N	40061	40051	2
\N	\N	f	t	f	Expected size (bp) of digestion product(s)	\N	\N	D. Expected fragment size(s)	String	\N	40063	40051	3
true	\N	f	t	f	Are the digest products the expected size?	\N	\N	E. Correct product(s)?	Boolean	\N	40065	40051	4
\N	\N	f	t	f	\N	\N	\N	A. Extension time (sec)	Float	\N	40073	40067	0
35	\N	f	t	f	\N	\N	\N	B. Number of cycles	Int	\N	40075	40067	1
Anneal 30 sec at 42C,    Extend 30 sec at 72C,    Denature 30 sec at 94C	\N	f	t	f	\N	\N	\N	C. Thermocycling conditions	String	\N	40077	40067	2
\N	\N	f	t	f	Expected size (bp) of PCR product	\N	\N	D. Expected size	String	\N	40079	40067	3
true	\N	f	t	f	Is the PCR product the expected size?	\N	\N	E. Correct size?	Boolean	\N	40081	40067	4
KOD Hot Start DNA Polymerase (Novagen)	\N	f	t	f	Type of polymerase used	\N	\N	F. Polymerase	String	\N	40083	40067	5
Reverse	\N	f	t	f	T7 primer forward or reverse?	\N	\N	G. T7 primer	String	\N	40085	40067	6
Forward	\N	f	t	f	Gene-specific primer forward or reverse?	\N	\N	H. Gene-specific primer	String	\N	40087	40067	7
BL21.DE3	\N	f	t	f	\N	\N	\N	A. Expression strain used	String	\N	40093	40089	0
Autoinduction	\N	f	t	f	Induction method	\N	\N	B. Autoinduction/IPTG	String	\N	40095	40089	1
10 ml Auto-induction medium	\N	f	t	f	Type of growth medium used	\N	\N	C. Growth medium	String	\N	40097	40089	2
1.0	\N	f	t	f	Concentration of IPTG mM -if used	\N	\N	D. IPTG conc. mM	Float	\N	40099	40089	3
37	\N	f	t	f	Expression temperature degrees C	\N	\N	E. Expression temp.	Int	\N	40101	40089	4
Overnight	\N	f	t	f	Length of time of Expression	\N	\N	F. Expression time	String	\N	40103	40089	5
Sonication	\N	f	t	f	Method used to lyse cells	\N	\N	G. Lysis method	String	\N	40105	40089	6
true	\N	f	t	f	Any protein expressed?	\N	\N	H. Expression?	Boolean	\N	40107	40089	7
false	\N	f	t	f	Soluble protein expressed?	\N	\N	I. Soluble?	Boolean	\N	40109	40089	8
\N	\N	f	t	f	Expected size of protein kDa	\N	\N	J. Expected size	String	\N	40111	40089	9
\N	\N	f	t	f	Actual size of protein kDa	\N	\N	K. Actual size	String	\N	40113	40089	10
60	\N	f	t	f	Annealing temperature	\N	\N	A. Annealing temp.	Float	\N	40127	40115	0
35	\N	f	t	f	Number of cycles	\N	\N	B. Num. cycles	Int	\N	40129	40115	1
5 min at 94C	\N	f	t	f	Initial denaturation conditions	\N	\N	C. Initial denaturation	String	\N	40131	40115	2
Anneal 30 sec at 60C, Extend 70 sec at 72C, Denature 30 sec at 94C	\N	f	t	f	Thermocycling conditions	\N	\N	D. Thermocycling	String	\N	40133	40115	3
3 min at 72C	\N	f	t	f	Final extension conditions	\N	\N	E. Final extension	String	\N	40135	40115	4
Overnight at 4C	\N	f	t	f	\N	\N	\N	F. Hold	String	\N	40137	40115	5
P	\N	f	t	f	Plasmid DNA: P, Genomic DNA: G	\N	\N	G. Template type	String	\N	40139	40115	6
\N	\N	f	t	f	Expected size (bp) of PCR product	\N	\N	H. Expected size	String	\N	40141	40115	7
true	\N	f	t	f	Is the PCR product the expected size?	\N	\N	I. Correct size?	Boolean	\N	40143	40115	8
\N	\N	f	t	f	Any comments about the PCR product	\N	\N	J. Comments	String	\N	40145	40115	9
true	\N	f	t	f	\N	\N	\N	K. PCR clean-up?	Boolean	\N	40147	40115	10
\N	\N	f	t	f	ID of plate used for PCR clean-up	\N	\N	L. Clean-up plate ID	String	\N	40149	40115	11
false	\N	f	t	f	Was the PCR product gel-purified?	\N	\N	M. Gel purify?	Boolean	\N	40151	40115	12
LIC+	\N	f	t	f	Type of vector used: LIC +/- cleavable C-term	\N	\N	A. Vector: LIC+, LIC-, Baculo	String	\N	40159	40153	0
\N	\N	f	t	f	ID of T4 polymerase reaction plate	\N	\N	B. T4 pol Plate ID	String	\N	40161	40153	1
\N	\N	f	t	f	ID of LIC-annealing reaction plate	\N	\N	C. LIC-annealing Plate ID	String	\N	40163	40153	2
\N	\N	f	t	f	ID and position in 24-well transformation plate	\N	\N	D. Transformation plate details	String	\N	40165	40153	3
Incubate 5 min on ice,   Heat shock 30 sec,   Incubate a further 5 min on ice	\N	f	t	f	\N	\N	\N	E. Transformation conditions	String	\N	40167	40153	4
42	\N	f	t	f	\N	\N	\N	F. Heat shock temperature	Float	\N	40169	40153	5
Yes	\N	f	t	f	Any colonies? Yes/No, or number	\N	\N	G. Colonies	String	\N	40171	40153	6
2	\N	f	t	f	Number of colonies processed	\N	\N	H. Minipreps	Int	\N	40173	40153	7
\N	\N	f	t	f	Plasmid concentration ug/ml	\N	\N	A. Plasmid concentration ug/ml	Float	\N	40195	40175	0
\N	\N	f	t	f	Plasmid volume for 50 ug	\N	\N	B. Plasmid volume for 50 ug	Float	\N	40197	40175	1
    Incubate 37C for 1 hour 50 min	\N	f	t	f	Digest conditions	\N	\N	C. Digest conditions	String	\N	40199	40175	2
true	\N	f	t	f	Was the linearised vector gel purified?	\N	\N	D. Gel purified	Boolean	\N	40201	40175	3
50	\N	f	t	f	\N	\N	\N	E. Linearised vector yield ug	Float	\N	40203	40175	4
50	\N	f	t	f	\N	\N	\N	F. Linearised vector conc. ug/ul	Float	\N	40205	40175	5
11	\N	f	t	f	Volume of linearized vector in T4 reaction	\N	\N	G. Vol. Vector in T4 reaction ul	Float	\N	40207	40175	6
Warm to room temperature,    Incubate 30 min at 22C,    Heat for 5 min at 75C	\N	f	t	f	Conditions for LIC T4 reaction	\N	\N	H. Incubation conditions	String	\N	40209	40175	7
50	\N	f	t	f	Final vector concentration ng/ul	\N	\N	I. Final vector concentration ng/ul	Float	\N	40211	40175	8
Pfu	\N	f	t	f	Polymerase used: Pfu/KOD	\N	\N	A. Polymerase	String	\N	40219	40213	0
true	\N	f	t	f	Is the PCR product the expected size?	\N	\N	B. Correct size?	Boolean	\N	40221	40213	1
\N	\N	f	t	f	Any comments about the PCR product	\N	\N	C. Comments	String	\N	40223	40213	2
false	\N	f	t	f	Was the PCR reaction optimised?	\N	\N	D. Optimised?	Boolean	\N	40225	40213	3
Anneal 30 sec at 60C, Extend 70 sec at 72C, Denature 30 sec at 94C	\N	f	t	f	Details of optimised conditions	\N	\N	E. Optimisation	String	\N	40227	40213	4
\N	\N	f	t	f	ID of purified plasmid plate	\N	\N	F. Pure plasmid Plate ID	String	\N	40229	40213	5
Reverse	\N	f	t	f	T7 primer forward or reverse?	\N	\N	G. T7 primer	String	\N	40231	40213	6
Forward	\N	f	t	f	Gene-specific primer forward or reverse?	\N	\N	H. Gene-specific primer	String	\N	40233	40213	7
1 of 2	\N	f	t	f	Number of clones processed e.g. 1 of 2, 2 of 2	\N	\N	A. Clones processed?	String	\N	40239	40235	0
Autoinduction medium	\N	f	t	f	Details of culture media used for expression	\N	\N	B. Culture media	String	\N	40241	40235	1
true	\N	f	t	f	Protein expression in BL21	\N	\N	C. Expressed in BL21	Boolean	\N	40243	40235	2
false	\N	f	t	f	Protein expression in Rosetta?	\N	\N	D. Expressed in Rosetta	Boolean	\N	40245	40235	3
false	\N	f	t	f	Protein expression in BL*?	\N	\N	E. Expressed in BL*	Boolean	\N	40247	40235	4
false	\N	f	t	f	Protein expression in B834?	\N	\N	F. Expressed in B834	Boolean	\N	40249	40235	5
false	\N	f	t	f	Soluble protein expressed?	\N	\N	G. Soluble?	Boolean	\N	40251	40235	6
BL21	\N	f	t	f	Which strains produced soluble expression?	\N	\N	H. Soluble in strain	String	\N	40253	40235	7
\N	\N	f	t	f	ID and position in 24-well transformation plate	\N	\N	I. Transformation plate details	String	\N	40255	40235	8
\N	\N	f	t	f	Expected size of protein kDa	\N	\N	J. Expected size	String	\N	40257	40235	9
\N	\N	f	t	f	Actual size of protein kDa	\N	\N	K. Actual size	String	\N	40259	40235	10
false	\N	f	t	f	Does expression need optimising?	\N	\N	L. Optimisation reqired?	Boolean	\N	40261	40235	11
\N	\N	f	t	f	Comments about the trials	\N	\N	M. Comments	String	\N	40263	40235	12
Native	\N	f	t	f	\N	\N	\N	A. Native/Selmet	String	\N	40271	40265	0
BL21 (DE3)	\N	f	t	f	\N	\N	\N	B. Cell Line	String	\N	40273	40265	1
\N	\N	f	t	f	\N	\N	\N	C. Induction method	String	\N	40275	40265	2
\N	\N	f	t	f	\N	\N	\N	D. Media	String	\N	40277	40265	3
\N	\N	f	t	f	\N	\N	\N	E. Antibiotic	String	\N	40279	40265	4
500	\N	f	t	f	\N	\N	\N	F. Scale-up volume (ml)	Int	\N	40281	40265	5
37	\N	f	t	f	\N	\N	\N	G. Pre-Induction Temperature (C)	Float	\N	40283	40265	6
25	\N	f	t	f	\N	\N	\N	H. Post-Induction Temperature (C)	Float	\N	40285	40265	7
Overnight	\N	f	t	f	\N	\N	\N	I. Induction duration	String	\N	40287	40265	8
1:100	\N	f	t	f	\N	\N	\N	J. Ratio of Inoculum	String	\N	40289	40265	9
0.6	\N	f	t	f	\N	\N	\N	K. OD(600nm) at Induction	Float	\N	40291	40265	10
200	\N	f	t	f	\N	\N	\N	L. Shaker speed (rpm)	Int	\N	40293	40265	11
\N	\N	f	t	f	\N	\N	\N	Forward Primer Layout Notes	String	\N	42007	42001	0
\N	\N	f	t	f	\N	\N	\N	Reverse Primer Layout Notes	String	\N	42015	42009	0
\.


--
-- Data for Name: prot_protocol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol (isforuse, methoddescription, name, objective, labbookentryid, experimenttypeid) FROM stdin;
\N	\N	PIMS Bicistronic InFusion	Capture PCR Product into pOPIN vectors	32001	9
\N	\N	PIMS Bitrans Transformation	Transform E.coli with plasmid, generic heat shock protocol	32015	19
\N	\N	PiMS Co Concentration	Concentration of Target Proteins	32041	53
\N	\N	PiMS Culture	Growth of E.coli in liquid culture	32061	111
\N	\N	PiMS LIC annealing	anneal LIC T4 polymerase treated insert and vector, generate recombinant plasmid	32077	15
\N	\N	PiMS LIC Plate culture	Growth of isolated colonies	32093	127
\N	\N	PiMS LIC-polymerase reaction	generate vector-compatible S/S overhangs	32107	17
\N	\N	PiMS LIC transformation	Transform E.coli with LIC annealing product, heat shock protocol	32127	19
\N	\N	PiMS LIC vector preparation	generate insert-compatible S/S overhangs	32145	17
\N	\N	PiMS Miniprep	Purify plasmid from small volume culture	32179	39
\N	\N	PiMS PCR	Amplify target DNA	32203	7
\N	\N	PiMS Plasmid prep	Purify plasmid from culture	32231	41
\N	\N	PiMS Plate culture	Growth of isolated colonies	32255	127
\N	\N	PiMS Transformation	Transform E.coli with plasmid, generic heat shock protocol	32269	19
\N	\N	PiMS Trial expression	Determine conditions for optimal expression of target protein	32287	31
\N	\N	PiMS Unspecified	To allow the user to create an experiment without a protocol	32329	139
\N	\N	Entry clone	Gateway Entry Clone design	34027	3
\N	\N	Deep-Frozen culture	Record Deep-Frozen culture stock	34039	5
\N	\N	Generic fermenter culture	Recording fermentation batches	34047	143
\N	\N	Sequencing order	Supply samples and other required details for sequencing	34085	147
\N	\N	SPOT Construct Primer Design	\N	36001	1
\N	\N	OPPF Construct Primer Design	\N	36021	27
\N	\N	OPPF Plate Layout - Forward Primers	Design Plate Layout	36045	113
\N	\N	OPPF Plate Layout - Reverse Primers	Design Plate Layout	36053	113
\N	\N	OPPF Plate Layout - Template	Design Plate Layout	36061	113
\N	\N	OPPF Dilution - Forward Primer	Dilute Primer for PCR	36069	105
\N	\N	OPPF Dilution - Reverse Primer	Dilute Primer for PCR	36079	105
\N	\N	OPPF Primer Order Plate	Order a plate of primer for targets	36089	141
\N	\N	OPPF PCR	Amplify target DNA	36097	7
\N	\N	OPPF PCR Cleanup	Purify PCR product	36143	37
\N	\N	OPPF InFusion	Capture PCR Product into pOPIN vectors	36153	9
\N	\N	OPPF Transformation	Transform E.coli with plasmid, generic heat shock protocol	36165	19
\N	\N	OPPF Plasmid prep	Purify plasmid from culture	36189	41
\N	\N	OPPF Verification	Verify the target DNA	36205	117
\N	\N	OPPF TrialExpression	Small Scale Expression of Target Protein	36219	31
\N	\N	OPPF Sequencing	Sequence target DNA	36243	117
\N	\N	OPPF ScaleUp Expression	Large Scale Expression of Target Protein	36255	33
\N	\N	OPPF ScaleUp Cell Lysis	Lysis of Cell Culture	36283	57
\N	\N	OPPF ScaleUp Purification	Purification of Target Protein	36303	43
\N	\N	OPPF Purification Affinity Desalt	Purification of Target Protein	36349	43
\N	\N	OPPF Purification Affinity Gel Filtration	Purification of Target Protein	36397	43
\N	\N	OPPF Purification Mammalian Affinity Desalt	Purification of Target Protein	36445	43
\N	\N	OPPF Purification Mammalian Affinity Gel Filtration	Purification of Target Protein	36493	43
\N	\N	OPPF Purification Affinity with Gradient Elution	Purification of Target Protein	36541	43
\N	\N	OPPF Purification Desalt	Purification of Target Protein	36585	43
\N	\N	OPPF Purification Gel Filtration	Purification of Target Protein	36627	43
\N	\N	OPPF Purification Ion Exchange	Purification of Target Protein	36669	43
\N	\N	OPPF Purification Batch Affinity	Purification of Target Protein	36713	43
\N	\N	OPPF Tag Cleavage	Removal of Tag peptides	36757	145
\N	\N	OPPF ScaleUp Concentration	Concentration of Target Protein	36779	53
\N	\N	OPPF Mass Spectrometry	Mass spectrometry analysis	36797	125
\N	\N	SPOT Construct Bioinformatics	\N	38001	1
\N	\N	SSPF Large Scale Expression 1	Large Scale Expression of Target Protein	38023	33
\N	\N	SSPF Purification 1	Purification of Target Protein	38057	43
\N	\N	PiMS Hamilton PCR	Amplify target DNA on the Hamilton robot	38135	7
\N	\N	SSPF Autoinduction Trial Expression	Expression Trials -Auto-inducuble expression	38153	31
\N	\N	SSPF Inducible Trial Expression	Expression Trials -Inducuble expression	38179	31
\N	\N	SSPF Large Scale Autoinducible Expression	Large Scale Autoinducible Expression of Target Protein	38213	33
\N	\N	SSPF Large Scale Inducible Expression	Large Scale Inducible Expression of Target Protein	38239	33
\N	\N	YSBL Standard PCR	Amplify target DNA	40001	7
\N	\N	YSBL LIC cloning	Clone PCR products by LIC	40029	9
\N	\N	YSBL Check Digest	Check cloned insert by Restriction enzyme digest	40051	11
\N	\N	YSBL Check PCR	Check cloned insert by PCR	40067	7
\N	\N	YSBL Trial expression	Determine conditions for optimal expression of target protein	40089	31
\N	\N	HiTel Standard PCR	Amplify target DNA	40115	7
\N	\N	HiTel LIC cloning	Clone PCR products by LIC	40153	9
\N	\N	HiTel LIC vector preparation	generate insert-compatible S/S overhangs	40175	17
\N	\N	HiTel Clone verification	Check cloned insert by PCR	40213	7
\N	\N	HiTel Trial expression	Determine conditions for optimal expression of target protein	40235	31
\N	\N	YSBL Large Scale Expression 1	Large Scale Expression of Target Protein	40265	33
\N	\N	PIMS Plate Layout - Forward Primers	Design Plate Layout	42001	113
\N	\N	PIMS Plate Layout - Reverse Primers	Design Plate Layout	42009	113
\N	\N	Primer Order Plate	Order a plate of primer for targets	34001	141
\N	\N	CrystalTrial	Do crystal trial	44018	71
\.


--
-- Data for Name: prot_protocol_remarks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol_remarks (protocolid, remark, order_) FROM stdin;
32001	Based on Lab activities requirements from OPPF	0
32015	Based on Lab activities requirements from OPPF	0
32041	Based on Lab activities requirements from OPPF	0
32061	Use appropriate selective medium and container	0
32077	for control reaction, substitute 2ul H2O for 2ul LIC polymerase reaction	0
32093	use appropriate pre-warmed selective agar support	0
32127	Edit protocol for specific cells	0
32179	based on use of Qiagen Plasmid mini kit Cat. 12123	0
32231	Based on use of Qiagen Plasmid midi kit Cat. 12143	0
32255	use appropriate pre-warmed selective agar support	0
32269	Edit protocol for specific cells	0
32287	Based on Lab activities requirements from YSBL	0
34027	Used by construct management	0
34039	Used by construct management	0
34047	Used by specific UI for recording Leeds fermentation batches	0
34085	Used by specific UI for sequencing	0
36045	Based on Lab activities requirements from OPPF	0
36053	Based on Lab activities requirements from OPPF	0
36061	Based on Lab activities requirements from OPPF	0
36069	Based on Lab activities requirements from OPPF	0
36079	Based on Lab activities requirements from OPPF	0
36089	created by PIMS for OPPF	0
36097	Based on Lab activities requirements from OPPF	0
36143	Based on Lab activities requirements from OPPF	0
36153	Based on Lab activities requirements from OPPF	0
36165	Based on Lab activities requirements from OPPF	0
36189	Based on Lab activities requirements from OPPF	0
36205	Based on Lab activities requirements from OPPF	0
36219	Based on Lab activities requirements from OPPF	0
36243	Based on Lab activities requirements from OPPF	0
36255	Based on Lab activities requirements from OPPF	0
36283	Based on Lab activities requirements from OPPF	0
36303	Based on Lab activities requirements from OPPF	0
36349	Based on Lab activities requirements from OPPF	0
36397	Based on Lab activities requirements from OPPF	0
36445	Based on Lab activities requirements from OPPF	0
36493	Based on Lab activities requirements from OPPF	0
36541	Based on Lab activities requirements from OPPF	0
36585	Based on Lab activities requirements from OPPF	0
36627	Based on Lab activities requirements from OPPF	0
36669	Based on Lab activities requirements from OPPF	0
36713	Based on Lab activities requirements from OPPF	0
36757	Based on Lab activities requirements from OPPF	0
36779	Based on Lab activities requirements from OPPF	0
36797	Based on Lab activities requirements from OPPF	0
38023	Based on Lab activities requirements from SSPF St. Andrews	0
38057	Based on Lab activities requirements from SSPF St. Andrews	0
38153	Based on Lab activities requirements from SSPF St. Andrews	0
38179	Based on Lab activities requirements from SSPF St. Andrews	0
38213	Based on Lab activities requirements from SSPF St. Andrews	0
38239	Based on Lab activities requirements from SSPF St. Andrews	0
40001	KOD Hot start method	0
40115	KOD Hot start method.  Plasmid P template use 10ng/50uL Genomic G template use 50ng/50uL	0
40175	1ml digest of Sigma Maxi Prep from 500ml or 1L culture.  2-400ul T4 reaction	0
40213	Use PfU first, KOD if first PCR fails	0
40235	Based on HiTel protocol	0
40265	Based on Lab activities requirements from YSBL	0
34001	created by PIMS	0
42009	Based on Lab activities requirements from OPPF	0
42001	Based on Lab activities requirements from OPPF	0
\.


--
-- Data for Name: prot_refinputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refinputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
1.9999999999999999e-006	uL	PCR Product A	L	32005	32001	10057
1.9999999999999999e-006	uL	PCR Product B	L	32007	32001	10057
9.9999999999999995e-007	uL	Vector	L	32009	32001	10097
5.0000000000000002e-005	uL	Competent cells	L	32019	32015	10019
1.9999999999999999e-006	uL	Expression Vector A	L	32021	32015	10065
1.9999999999999999e-006	uL	Expression Vector B	L	32023	32015	10065
0.00029999999999999997	uL	Culture media	L	32025	32015	10021
\N	uL	Purified Protein A	L	32045	32041	10101
\N	uL	Purified Protein B	L	32047	32041	10101
0.025000000000000001	mL	Culture media	L	32065	32061	10021
2.5000000000000001e-005	uL	Antibiotic	L	32067	32061	10003
0.0001	uL	Innoculum	L	32069	32061	10103
9.9999999999999995e-007	uL	LIC vector	L	32081	32077	10097
1.9999999999999999e-006	uL	Insert	L	32083	32077	10057
9.9999999999999995e-007	uL	Chelating agent	L	32085	32077	10087
0.00014999999999999999	uL	Cells	L	32097	32093	10103
\N	uL	Insert	L	32111	32107	10057
1.9999999999999999e-006	uL	Buffer	L	32113	32107	10007
1.9999999999999999e-006	uL	dATP	L	32115	32107	10063
9.9999999999999995e-007	uL	Reducing agent	L	32117	32107	10087
\N	uL	Water	L	32119	32107	10089
3.9999999999999998e-007	uL	Polymerase	L	32121	32107	10031
5.0000000000000002e-005	uL	Competent cells	L	32131	32127	10019
1.9999999999999999e-006	uL	LIC-product	L	32133	32127	10057
0.0001	uL	Culture media	L	32135	32127	10021
8.0000000000000005e-009	ug	Vector	kg	32149	32145	10057
7.9999999999999996e-006	uL	Restriction enzyme	L	32151	32145	10027
1.5999999999999999e-005	uL	Restriction buffer	L	32153	32145	10007
1.9999999999999999e-006	uL	Polymerase buffer	L	32155	32145	10007
1.9999999999999999e-006	uL	dTTP	L	32157	32145	10063
9.9999999999999995e-007	uL	Reducing agent	L	32159	32145	10087
\N	uL	Water	L	32161	32145	10089
3.9999999999999998e-007	uL	Polymerase	L	32163	32145	10031
0.0050000000000000001	mL	Culture media	L	32183	32179	10021
5.0000000000000004e-006	uL	Antibiotic	L	32185	32179	10087
1.0000000000000001e-005	uL	Cells	L	32187	32179	10103
0.00055999999999999995	mL	Precipitant	L	32189	32179	10089
0.001	mL	Wash	L	32191	32179	10089
5.0000000000000002e-005	uL	Resuspension buffer	L	32193	32179	10007
5.0000000000000004e-006	uL	Buffer	L	32207	32203	10007
5.0000000000000004e-006	uL	dNTPs	L	32209	32203	10063
4.9999999999999998e-007	uL	Template	L	32211	32203	10057
9.9999999999999995e-007	uL	Forward primer	L	32213	32203	10037
9.9999999999999995e-007	uL	Reverse primer	L	32215	32203	10079
\N	uL	Water	L	32217	32203	10089
9.9999999999999995e-007	uL	Polymerase	L	32219	32203	10031
0.025000000000000001	mL	Culture media	L	32235	32231	10021
2.5000000000000001e-005	uL	Antibiotic	L	32237	32231	10003
2.5000000000000001e-005	uL	Cells	L	32239	32231	10103
0.0035000000000000001	mL	Precipitant	L	32241	32231	10089
0.002	mL	Wash	L	32243	32231	10089
5.0000000000000002e-005	uL	Resuspension buffer	L	32245	32231	10089
0.00014999999999999999	uL	Cells	L	32259	32255	10103
5.0000000000000002e-005	uL	Competent cells	L	32273	32269	10019
1.9999999999999999e-006	uL	Vector	L	32275	32269	10097
0.0001	uL	Culture media	L	32277	32269	10021
0.001	mL	Culture media	L	32291	32287	10021
9.9999999999999995e-007	uL	Antibiotic	L	32293	32287	10003
\N	uL	Reverse Primer	L	34029	34027	10079
\N	uL	Forward primer	L	34031	34027	10037
\N	uL	Plasmid	L	34033	34027	10065
\N	uL	Entry Clone	L	34041	34039	10065
\N	uL	Strain	L	34043	34039	10093
0	uL	Construct	L	34069	34047	10065
0	uL	Antibiotic A	L	34071	34047	10003
0	uL	Antibiotic B	L	34073	34047	10003
0	uL	Antibiotic C	L	34075	34047	10003
0	mL	Culture	L	34077	34047	10103
3.0000000000000001e-006	uL	Forward Primer	L	36049	36045	10037
3.0000000000000001e-006	uL	Reverse Primer	L	36057	36053	10079
1.9999999999999999e-006	uL	Template	L	36065	36061	10057
1.0000000000000001e-005	uL	Forward Primer	L	36073	36069	10037
9.0000000000000006e-005	uL	Buffer	L	36075	36069	10007
1.0000000000000001e-005	uL	Reverse Primer	L	36083	36079	10079
9.0000000000000006e-005	uL	Buffer	L	36085	36079	10007
0	uL	Forward Primer	L	36093	36089	10037
0	uL	Reverse Primer	L	36095	36089	10079
1.9999999999999999e-006	uL	Template	L	36101	36097	10057
\N	uL	Buffer-type	L	36103	36097	10007
3.9999999999999998e-007	uL	Enzyme	L	36105	36097	10031
3.0000000000000001e-006	uL	Forward Primer	L	36107	36097	10037
3.0000000000000001e-006	uL	Reverse Primer	L	36109	36097	10079
\N	uL	Additive	L	36111	36097	10013
\N	uL	Nucleotide	L	36113	36097	10063
\N	uL	Salt	L	36115	36097	10013
5.0000000000000002e-005	uL	PCR product	L	36147	36143	10057
9.0000000000000006e-005	uL	Kit	L	36149	36143	10059
1.9999999999999999e-006	uL	PCR Product	L	36157	36153	10057
9.9999999999999995e-007	uL	Vector	L	36159	36153	10097
5.0000000000000002e-005	uL	Competent cells	L	36169	36165	10019
1.9999999999999999e-006	uL	Expression Vector	L	36171	36165	10065
0.00029999999999999997	uL	Culture media	L	36173	36165	10021
0.014999999999999999	mL	Culture media	L	36193	36189	10021
1.5e-005	uL	Antibiotic	L	36195	36189	10003
2.5000000000000001e-005	uL	Transformed cells	L	36197	36189	10103
1.9999999999999999e-006	uL	Purified plasmid	L	36209	36205	10057
3.0000000000000001e-006	uL	Forward Primer	L	36211	36205	10037
3.0000000000000001e-006	uL	Reverse Primer	L	36213	36205	10079
\N	uL	Purified plasmid	L	36223	36219	10057
0.001	mL	Culture media	L	36225	36219	10021
9.9999999999999995e-007	uL	Antibiotic A	L	36227	36219	10003
9.9999999999999995e-007	uL	Antibiotic B	L	36229	36219	10003
\N	colonies	Strain	number	36231	36219	10019
1.9999999999999999e-006	uL	Purified plasmid	L	36247	36243	10057
3.0000000000000001e-006	uL	Sequencing Primer	L	36249	36243	10067
9.9999999999999995e-007	uL	Purified plasmid	L	36259	36255	10057
1	L	Culture media	L	36261	36255	10021
0.001	mL	Antibiotic A	L	36263	36255	10003
0.001	mL	Antibiotic B	L	36265	36255	10003
\N	colonies	Strain	number	36267	36255	10019
\N	mL	Culture	L	36287	36283	10101
0.040000000000000001	mL	Lysis Buffer	L	36289	36283	10007
3	tablets	Protease Inhibitor	number	36291	36283	10045
0.0001	uL	DNAse	L	36293	36283	10029
\N	uL	Lysozyme	L	36295	36283	10029
\N	\N	Ni Wash	L	36307	36303	10007
\N	\N	Ni Elute	L	36309	36303	10007
\N	\N	GFin	L	36311	36303	10007
0.0080000000000000002	mL	Supernatant	L	36313	36303	10101
\N	mL	Affinity Wash Buffer	L	36353	36349	10007
\N	mL	Affinity Elution Buffer	L	36355	36349	10007
\N	mL	Gel Filtration/Desalt Buffer	L	36357	36349	10007
0.0080000000000000002	mL	Supernatant	L	36359	36349	10101
\N	mL	Affinity Wash Buffer	L	36401	36397	10007
\N	mL	Affinity Elution Buffer	L	36403	36397	10007
\N	mL	Gel Filtration/Desalt Buffer	L	36405	36397	10007
0.0080000000000000002	mL	Supernatant	L	36407	36397	10101
\N	mL	Affinity Wash Buffer	L	36449	36445	10007
\N	mL	Affinity Elution Buffer	L	36451	36445	10007
\N	mL	Gel Filtration/Desalt Buffer	L	36453	36445	10007
0.0080000000000000002	mL	Supernatant	L	36455	36445	10101
\N	mL	Affinity Wash Buffer	L	36497	36493	10007
\N	mL	Affinity Elution Buffer	L	36499	36493	10007
\N	mL	Gel Filtration/Desalt Buffer	L	36501	36493	10007
0.0080000000000000002	mL	Supernatant	L	36503	36493	10101
\N	mL	Affinity Wash Buffer	L	36545	36541	10007
\N	mL	Affinity Elution Buffer	L	36547	36541	10007
0.0080000000000000002	mL	Supernatant	L	36549	36541	10101
\N	mL	Gel Filtration/Desalt Buffer	L	36589	36585	10007
0.0080000000000000002	mL	Supernatant	L	36591	36585	10101
\N	mL	Gel Filtration/Desalt Buffer	L	36631	36627	10007
0.0080000000000000002	mL	Supernatant	L	36633	36627	10101
\N	mL	Buffer A	L	36673	36669	10007
\N	mL	Buffer B	L	36675	36669	10007
0.0080000000000000002	mL	Supernatant	L	36677	36669	10101
\N	mL	Affinity Resin	L	36717	36713	10107
\N	mL	Affinity Wash Buffer	L	36719	36713	10007
\N	mL	Affinity Elution Buffer	L	36721	36713	10007
0.0080000000000000002	mL	Supernatant	L	36723	36713	10101
\N	\N	Cleavage Buffer	L	36763	36757	10007
\N	\N	Protease	L	36765	36757	10035
\N	mL	Purified Protein	L	36767	36757	10101
\N	uL	Purified Protein	L	36783	36779	10101
8.0000000000000007e-005	uL	Concentrated Protein	L	36801	36797	10101
0.0001	uL	Inoculum	L	38027	38023	10103
\N	mL	Pellet	L	38061	38057	10103
5.0000000000000004e-006	uL	Buffer	L	38139	38135	10007
5.0000000000000004e-006	uL	dNTPs	L	38141	38135	10063
4.9999999999999998e-007	uL	Template	L	38143	38135	10057
9.9999999999999995e-007	uL	Forward primer	L	38145	38135	10037
9.9999999999999995e-007	uL	Reverse primer	L	38147	38135	10079
\N	uL	Water	L	38149	38135	10089
9.9999999999999995e-007	uL	Polymerase	L	38151	38135	10031
\N	uL	Purified plasmid	L	38157	38153	10057
\N	uL	Purified plasmid	L	38183	38179	10057
\N	uL	Purified plasmid	L	38217	38213	10057
\N	uL	Purified plasmid	L	38243	38239	10057
9.9999999999999995e-007	uL	Forward primer	L	40005	40001	10037
9.9999999999999995e-007	uL	Reverse primer	L	40007	40001	10079
\N	uL	Insert	L	40033	40029	10057
9.9999999999999995e-007	uL	Possible clone	L	40055	40051	10057
9.9999999999999995e-007	uL	Possible clone	L	40071	40067	10057
9.9999999999999995e-007	uL	Test clone	L	40091	40089	10057
4.9999999999999998e-007	uL	Template	L	40119	40115	10057
5.0000000000000004e-006	uL	Forward primer	L	40121	40115	10037
5.0000000000000004e-006	uL	Reverse primer	L	40123	40115	10079
\N	uL	Water	L	40125	40115	10089
5.0000000000000004e-006	uL	Insert	L	40157	40153	10057
4.9999999999999998e-008	ug	Vector	kg	40179	40175	10057
5.0000000000000002e-005	uL	Restriction enzyme	L	40181	40175	10027
0.0001	uL	Restriction buffer	L	40183	40175	10007
2.0000000000000002e-005	uL	Polymerase buffer	L	40185	40175	10007
2.0000000000000002e-005	uL	dTTP	L	40187	40175	10063
1.0000000000000001e-005	uL	Reducing agent	L	40189	40175	10087
\N	uL	Water	L	40191	40175	10089
3.9999999999999998e-006	uL	Polymerase	L	40193	40175	10031
4.9999999999999998e-007	uL	Template	L	40217	40213	10057
1.9999999999999999e-006	uL	Recombinant plasmid	L	40237	40235	10057
\N	uL	Clone	L	40269	40265	10057
0	uL	Forward Primer	L	42017	34001	10037
0	uL	Reverse Primer	L	42019	34001	10079
3.0000000000000001e-006	uL	Reverse Primer	L	42013	42009	10067
3.0000000000000001e-006	uL	Forward Primer	L	42005	42001	10067
9.9999999999999995e-008	nL	Purified protein	L	44020	44018	10073
\.


--
-- Data for Name: prot_refoutputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refoutputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
\N	uL	Vector	L	32003	32001	10065
\N	mL	Cells	L	32017	32015	10103
8.0000000000000007e-005	uL	Concentrated Protein	L	32043	32041	10073
\N	mL	Culture	L	32063	32061	10103
\N	mL	LIC-product	L	32079	32077	10057
\N	\N	Colonies	\N	32095	32093	10103
\N	mL	LIC insert	L	32109	32107	10057
\N	mL	Cells	L	32129	32127	10103
\N	mL	LIC vector	L	32147	32145	10057
4.9999999999999998e-008	uL	Purified plasmid	L	32181	32179	10057
\N	uL	PCR product	L	32205	32203	10057
\N	mL	Purified plasmid	L	32233	32231	10057
\N	\N	Colonies	\N	32257	32255	10103
\N	mL	Cells	L	32271	32269	10103
\N	mL	Culture	L	32289	32287	10103
\N	uL	Primer	L	34003	34001	10067
0.050000000000000003	g	Tube 1	kg	34049	34047	10103
0.050000000000000003	g	Tube 2	kg	34051	34047	10103
0.050000000000000003	g	Tube 3	kg	34053	34047	10103
0.050000000000000003	g	Tube 4	kg	34055	34047	10103
0.050000000000000003	g	Tube 5	kg	34057	34047	10103
0.050000000000000003	g	Tube 6	kg	34059	34047	10103
0.050000000000000003	g	Tube 7	kg	34061	34047	10103
0.050000000000000003	g	Tube 8	kg	34063	34047	10103
0.050000000000000003	g	Tube 9	kg	34065	34047	10103
0.050000000000000003	g	Tube 10	kg	34067	34047	10103
0	uL	Forward Primer	L	36003	36001	10037
0	uL	Reverse Primer	L	36005	36001	10079
0	uL	Template	L	36007	36001	10057
0	uL	Forward Primer	L	36023	36021	10037
0	uL	Reverse Primer	L	36025	36021	10079
0	uL	Template	L	36027	36021	10057
3.0000000000000001e-006	uL	Forward Primer	L	36047	36045	10037
3.0000000000000001e-006	uL	Reverse Primer	L	36055	36053	10079
1.9999999999999999e-006	uL	Template	L	36063	36061	10057
3.0000000000000001e-006	uL	Forward Primer	L	36071	36069	10037
3.0000000000000001e-006	uL	Reverse Primer	L	36081	36079	10079
\N	uL	Primer	L	36091	36089	10067
\N	uL	PCR product	L	36099	36097	10057
\N	uL	PCR product	L	36145	36143	10057
\N	uL	Vector	L	36155	36153	10065
\N	mL	Cells	L	36167	36165	10103
\N	mL	Purified plasmid	L	36191	36189	10057
\N	uL	Verification Product	L	36207	36205	10057
1.9999999999999999e-006	uL	Purified plasmid	L	36221	36219	10057
\N	uL	Sequencing product	L	36245	36243	10057
8.0000000000000007e-005	g	Culture	kg	36257	36255	10101
8.0000000000000007e-005	uL	Supernatant	L	36285	36283	10101
8.0000000000000007e-005	uL	Purified Protein	L	36305	36303	10101
8.0000000000000007e-005	uL	Purified Protein	L	36351	36349	10101
8.0000000000000007e-005	uL	Purified Protein	L	36399	36397	10101
8.0000000000000007e-005	mL	Purified Protein	L	36447	36445	10101
8.0000000000000007e-005	mL	Purified Protein	L	36495	36493	10101
8.0000000000000007e-005	mL	Purified Protein	L	36543	36541	10101
8.0000000000000007e-005	mL	Purified Protein	L	36587	36585	10101
8.0000000000000007e-005	mL	Purified Protein	L	36629	36627	10101
8.0000000000000007e-005	mL	Purified Protein	L	36671	36669	10101
8.0000000000000007e-005	mL	Purified Protein	L	36715	36713	10101
\N	uL	Cleaved Protein	L	36759	36757	10101
\N	uL	Uncleaved Protein	L	36761	36757	10101
8.0000000000000007e-005	uL	Concentrated Protein	L	36781	36779	10073
\N	mL	Mass Spec Output	L	36799	36797	10073
\N	mL	Pellet	L	38025	38023	10103
\N	uL	Purified Protein	L	38059	38057	10073
\N	uL	PCR product	L	38137	38135	10057
\N	mL	Pellet	L	38155	38153	10103
\N	mL	Pellet	L	38181	38179	10103
\N	mL	Pellet	L	38215	38213	10103
\N	mL	Pellet	L	38241	38239	10103
\N	uL	PCR product	L	40003	40001	10057
\N	uL	Possible clone	L	40031	40029	10057
\N	uL	Fragments	L	40053	40051	10057
\N	uL	PCR product	L	40069	40067	10057
\N	uL	PCR product	L	40117	40115	10057
\N	uL	Recombinant plasmid	L	40155	40153	10057
\N	mL	LIC vector	L	40177	40175	10097
\N	uL	PCR product	L	40215	40213	10057
\N	mL	Culture	L	40267	40265	10103
3.0000000000000001e-006	uL	Forward Primer	L	42003	42001	10037
3.0000000000000001e-006	uL	Reverse Primer	L	42011	42009	10079
9.9999999999999995e-008	nL	Trial Drop	L	44019	44018	44012
\.


--
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
-- Data for Name: ref_crystaltype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_crystaltype (ressubpos, holdertypeid) FROM stdin;
\.


--
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
\.


--
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
Screen	44013
Additive	44014
Optimization	44015
SparseMatrix	44016
TrialPlate	44017
\.


--
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
-- Data for Name: ref_imagetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_imagetype (publicentryid, sizey, sizex, url, xlengthperpixel, colourdepth, name, ylengthperpixel, catorgory) FROM stdin;
44001	700	750	http://www.oppf.ox.ac.uk/vault/images/lowres/	2.8570000000000002	8	Oasis 750*700	2.8570000000000002	composite
44002	960	1280	http://www.oppf.ox.ac.uk/vault/images/lowres/	2.9790000000000001	8	RI 1280*960	2.9790000000000001	composite
44003	108	691	http://www.oppf.ox.ac.uk/vault/images/lowres/	1.587	8	RI 691*108	1.587	composite
44004	960	1260	http://www.oppf.ox.ac.uk/vault/images/micro/	1.587	256	Pixera	1.587	zoomed
\.


--
-- Data for Name: ref_instrumenttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_instrumenttype (name, publicentryid) FROM stdin;
\.


--
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
-- Data for Name: ref_pintype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_pintype (length, looplength, looptype, wirewidth, abstractholdertypeid) FROM stdin;
\.


--
-- Data for Name: ref_publicentry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_publicentry (dbid, details) FROM stdin;
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
10019	Cell or cells capable of DNA uptake
10021	Liquid or solid substance used for the growth of cells
10023	Substance with polar (water soluble) and non-polar(hydrophobic) domains
10025	Reagent for use in electrophoresis experiments
10027	Restriction enzyme
10029	Modifying enzyme e.g. kinase, phosphatase, etc.
10031	Polymerase enzyme e.g. Taq, Pfu, Reverse transcriptase, etc. 
10033	Ligase enzyme
10035	 Protease enzyme e.g. TEV
10037	Used in combination with a Reverse Primer in PCR
10039	Protein which stimulates cellular proliferation
10041	Reagent used for preparing heavy-atom derivatives of protein crystals
10043	Substance which regulates celluar functions
10045	Substance which inhibits the activity of a molecule
10047	Hydrocarbon containing organic compound, insoluble in water
10049	Size standards used in elctrophoreis
10051	\N
10053	Latex beads, magnetic beads etc.
10055	Reagents for use in microscopy
10057	DNA, RNA etc.
10059	Commercial kit for nucleic acid purification"
10061	Commercial kit for modifying nucleic acids"
10063	Nucleotide compounds and solutions e.g.dATP, NADP, etc.
10065	A recombinant or insert-containing vector
10067	e.g. PCR-primer
10069	Drugs and pharmaceuticals
10071	Amino acid polymer
10075	Chemical prepared using a radioactive element
10077	Cells with Target
10079	Used in combination with a Forward Primer in PCR
10081	PIMS default category
10083	Crystallization screen
10085	\N
10087	Solution
10089	Solvent
10091	Stains and Dyes
10093	A strain of an organism
10095	Substance acted on by an enzyme
10097	Expression vector, cloning vector, etc.
10099	Vitamins and Derivatives
10101	Sample which contains the Target protein
10103	Cell or cells which have been modified to contain foreign DNA such as a plasmid: e.g. E.coli transformed with pDEST17
10105	Commercial kit
10107	Affinity Resin
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
18143	\N
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
44001	\N
44002	\N
44003	\N
44004	\N
10073	The purified protein which will be crystallized in trialplate
44012	The sample may contains crystal
44013	PIMS default category for screen
44014	Sub category of screen
44015	Sub category of screen
44016	Sub category of screen
44017	PIMS default category for Crystal Trial
\.


--
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
Competent cells	10019
Culture media	10021
Detergent	10023
Electrophoresis	10025
Enzyme -restriction	10027
Enzyme -modifying	10029
Enzyme -polymerase	10031
Enzyme -ligase	10033
Enzyme -protease	10035
Forward Primer	10037
Growth factor	10039
Heavy atom reagent	10041
Hormone	10043
Inhibitor	10045
Lipid	10047
Marker	10049
Membrane	10051
Microparticle	10053
Microscopy	10055
Nucleic acid	10057
Nucleic acid purification kit	10059
Nucleic acid modification kit	10061
Nucleotide	10063
Plasmid	10065
Primer	10067
Pharmacopeia	10069
Polyamino acid	10071
Purified protein	10073
Radiochemical	10075
Recombinant Cells	10077
Reverse Primer	10079
Screen	10081
Screen solution	10083
Solubilised Membrane	10085
Solution	10087
Solvent	10089
Stains and Dyes	10091
Strain	10093
Substrate	10095
Vector	10097
Vitamins and Derivatives	10099
Target -containing sample	10101
Transformed cells	10103
Kit	10105
Affinity Resin	10107
TrialDrop	44012
\.


--
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
-- Data for Name: revisionnumber; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY revisionnumber (revision, name, "release", tag, author, date) FROM stdin;
36	pims	2_3	pims_2_3_36	Anne	27/08/2008
37	pims	3_2	pims_3_2_37	bill_v_upgrader	27/02/2009
\.


--
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
-- Data for Name: sam_crystalsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_crystalsample (a, alpha, b, beta, c, colour, crystaltype, gamma, morphology, spacegroup, x, y, z, sampleid) FROM stdin;
\.


--
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
-- Data for Name: sam_sampca2abstsa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sampca2abstsa (samplecategoryid, abstractsampleid) FROM stdin;
10031	16001
10031	16004
10031	16007
10031	16010
10031	16013
10031	16016
10031	16019
10031	16022
10031	16025
10033	16028
10031	16032
10031	16035
10031	16038
10031	16041
10027	16044
10007	16047
10007	16049
10007	16052
10007	16055
10007	16058
10007	16061
10049	16064
10049	16067
10049	16070
10049	16073
10063	16076
10063	16079
10063	16082
10063	16085
10063	16088
10063	16091
10063	16093
10063	16095
10063	16098
10097	16101
10017	16104
10017	16107
10017	16110
10017	16113
10017	16116
10105	16119
10019	16122
10019	16125
10019	16128
10019	16131
10019	16134
10019	16137
10019	16140
10019	16143
10019	16146
10019	16149
10019	16152
10019	16155
10019	16158
10019	16161
10019	16164
10019	16167
10019	16170
10019	16173
10059	16176
10059	16179
10059	16182
10059	16185
10059	16188
10021	16191
10021	16194
10021	16197
10021	16199
10021	16202
10021	16205
10021	16208
10021	16211
10087	16214
10089	16216
10087	16220
10087	16222
10007	16224
10003	16226
10003	16228
10003	16230
10003	16232
10089	16236
10089	16238
10089	16240
10007	16242
10067	16244
10067	16247
10067	16250
10067	16253
10007	22003
10013	22003
10009	22003
10013	22008
10009	22008
10089	22013
10089	22018
10007	22023
10013	22023
10009	22023
10091	22028
10025	22033
10025	22038
10063	22043
10013	22043
10063	22048
10013	22048
10013	22053
10013	22058
10013	22063
10013	22068
10013	22073
10045	22073
10013	22078
10045	22078
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
10013	22153
10025	22153
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
10091	22203
10013	22208
10099	22213
10013	22218
10013	22223
10013	22228
10045	22228
10013	22233
10045	22233
10013	22238
10013	22243
10013	22248
10013	22253
10013	22258
10045	22258
10013	22263
10045	22263
10013	22268
10013	22273
10007	22278
10013	22278
10009	22278
10013	22283
10013	16218
10013	22290
10013	22295
10045	22295
10007	22300
10013	22300
10009	22300
10023	22305
10099	22310
10099	22315
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
10091	22345
10091	22350
10091	22355
10013	22360
10023	22365
10013	22370
10089	22375
10013	22380
10013	22385
10013	22390
10089	22395
10013	22395
10089	22400
10013	22400
10013	22405
10013	22410
10045	22410
10013	22415
10023	22420
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
10023	22540
10023	22545
10007	22550
10013	22550
10009	22550
10013	22555
10013	22560
10003	22560
10089	22565
10095	22570
10091	22575
10047	22580
10023	22585
10013	22590
10013	22595
10013	22600
10013	22605
10013	22610
10013	22615
10013	22620
10013	22625
10095	22630
10013	22635
10063	22640
10013	22640
10013	22645
10013	22650
10023	22655
10023	22660
10013	22665
10023	22665
10063	22670
10013	22670
10063	22675
10013	22675
10063	22680
10013	22680
10063	22685
10013	22685
10013	22690
10013	22695
10013	22700
10013	22705
10041	22710
10013	22710
10041	22715
10013	22715
10013	22720
10013	22725
10023	22730
10047	22735
10023	22740
10023	22745
10023	22750
10023	22755
10013	22760
10045	22760
10089	22765
10013	22765
10013	22770
10089	22775
10013	22775
10013	22780
10089	22785
10013	22785
10013	22790
10013	22795
10007	22800
10013	22800
10009	22800
10013	22805
10013	22810
10047	22815
10023	22820
10047	22825
10047	22830
10089	22835
10013	22835
10013	22840
10023	22840
10023	22845
10047	22850
10013	22855
10013	22860
10089	16234
10013	16234
10013	22867
10091	22872
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
10041	22922
10013	22922
10013	22927
10013	22932
10013	22937
10013	22942
10013	22947
10089	22952
10013	22952
10013	22957
10013	22962
10045	22962
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
10063	23027
10013	23027
10063	23032
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
10023	23062
10013	23067
10013	23072
10013	23077
10015	23077
10013	23082
10013	23087
10013	23092
10023	23097
10013	23102
10089	23107
10013	23107
10013	23112
10045	23112
10013	23117
10045	23117
10013	23122
10007	23127
10013	23127
10009	23127
10013	23132
10045	23132
10013	23137
10045	23137
10091	23142
10013	23147
10013	23152
10013	23157
10013	23162
10013	23167
10089	23172
10013	23172
10013	23177
10013	23182
10013	23187
10003	23187
10013	23192
10013	23197
10023	23197
10041	23202
10013	23202
10041	23207
10013	23207
10013	23212
10045	23212
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
10041	23412
10013	23412
10041	23417
10013	23417
10041	23422
10013	23422
10007	23427
10013	23427
10009	23427
10007	23432
10013	23432
10009	23432
10089	23437
10013	23437
10013	23442
10041	23447
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
10063	23477
10013	23477
10063	23482
10013	23482
10013	23487
10013	23492
10013	23497
10013	23502
10013	23507
10013	23512
10023	23517
10023	23522
10023	23527
10023	23532
10023	23537
10013	23542
10023	23542
10023	23547
10013	23552
10023	23552
10007	23557
10013	23557
10009	23557
10013	23562
10041	23567
10013	23567
10045	23567
10041	23572
10013	23572
10045	23572
10091	23577
10013	23582
10013	23587
10023	23592
10023	23597
10013	23602
10013	23607
10045	23607
10013	23612
10045	23612
10013	23617
10091	23622
10013	23627
10041	23632
10013	23632
10047	23637
10007	23642
10013	23642
10009	23642
10013	23647
10045	23647
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
10023	23752
10013	23757
10013	23762
10003	23762
10057	23767
10013	23767
10013	23772
10013	23777
10047	23782
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
10041	23847
10013	23847
10041	23852
10013	23852
10013	23857
10013	23862
10013	23867
10013	23872
10013	23877
10013	23882
10013	23887
10013	23892
10041	23897
10013	23897
10041	23902
10013	23902
10041	23907
10013	23907
10041	23912
10013	23912
10041	23917
10013	23917
10013	23922
10013	23927
10089	23932
10013	23932
10013	23937
10013	23942
10013	23947
10013	23952
10013	23957
10013	23962
10013	23967
10099	23972
10013	23977
10013	23982
10041	23987
10013	23987
10007	23992
10013	23992
10007	23997
10013	23997
10009	23997
10013	24002
10045	24002
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
10023	24042
10023	24047
10023	24052
10013	24057
10089	24062
10013	24062
10013	24067
10023	24067
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
10047	24182
10013	24187
10013	24192
10045	24192
10013	24197
10045	24197
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
10023	24287
10023	24292
10089	24297
10013	24297
10013	24302
10041	24307
10013	24307
10013	24312
10041	24317
10013	24317
10013	24322
10013	24327
10045	24327
10013	24332
10045	24332
10013	24337
10013	24342
10013	24347
10007	24352
10013	24352
10009	24352
10007	24357
10089	24357
10013	24357
10013	24362
10089	24367
10013	24372
10013	24377
10013	24382
10041	24387
10013	24387
10091	24392
10007	24397
10013	24397
10009	24397
10007	24402
10013	24402
10009	24402
10013	24407
10023	24412
10023	24417
10013	24422
10045	24422
10013	24427
10045	24427
10023	24432
10013	24437
10013	24442
10003	24442
10099	24447
10095	24452
10091	24457
10091	24462
10013	24467
10041	24472
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
-- Data for Name: sam_sample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sample (amountdisplayunit, amountunit, batchnum, colposition, currentamount, currentamountflag, initialamount, rowposition, subposition, usebydate, abstractsampleid, holderid, refsampleid, assigntoid) FROM stdin;
\.


--
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
-- Data for Name: sam_trialsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_trialsample (sampleid, insynchrotron, conditionid, proteinid) FROM stdin;
\.


--
-- Data for Name: sche_scheduledtask; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduledtask (priority, scheduledtime, state, labbookentryid, name, completiontime, instrumentid, holderid, scheduleplanoffsetid) FROM stdin;
\.


--
-- Data for Name: sche_scheduleplan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplan (name, labbookentryid) FROM stdin;
\.


--
-- Data for Name: sche_scheduleplanoffset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplanoffset (offsettime, priority, labbookentryid, order_, scheduleplanid) FROM stdin;
\.


--
-- Data for Name: targ_alias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_alias (labbookentryid, name, targetid) FROM stdin;
\.


--
-- Data for Name: targ_milestone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_milestone (date_, labbookentryid, experimentid, targetid, statusid) FROM stdin;
\.


--
-- Data for Name: targ_project; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_project (completename, shortname, startdate, labbookentryid, projectid) FROM stdin;
public	public	\N	8001	\N
\.


--
-- Data for Name: targ_researchobjective; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_researchobjective (biochemicalfunction, biologicalprocess, catalyticactivity, celllocation, commonname, functiondescription, localname, pathway, similaritydetails, systematicname, whychosen, labbookentryid, ownerid) FROM stdin;
\.


--
-- Data for Name: targ_target; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target (biochemicalfunction, biologicalprocess, catalyticactivity, celllocation, functiondescription, genename, name, orf, pathway, similaritydetails, topology, whychosen, labbookentryid, proteinid, speciesid) FROM stdin;
\.


--
-- Data for Name: targ_target2nuclac; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target2nuclac (nuctargetid, nucleicacidid) FROM stdin;
\.


--
-- Data for Name: targ_target2projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_target2projects (targetid, projectid) FROM stdin;
\.


--
-- Data for Name: targ_targetgroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_targetgroup (completename, groupingtype, namingsystem, shortname, labbookentryid, targetgroupid) FROM stdin;
\.


--
-- Data for Name: targ_targgr2targets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_targgr2targets (targetgroupid, targetid) FROM stdin;
\.


--
-- Data for Name: trag_researchobjectiveelement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY trag_researchobjectiveelement (alwaysincluded, approxbeginseqid, approxendseqid, componenttype, "domain", status, whychosen, labbookentryid, targetid, researchobjectiveid, moleculeid) FROM stdin;
\.


--
-- Name: acco_permission_permissionclass_optype_rolename_accessobjectid_; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_permissionclass_optype_rolename_accessobjectid_ UNIQUE (permissionclass, optype, rolename, accessobjectid, usergroupid);


--
-- Name: acco_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_pkey PRIMARY KEY (systemclassid);


--
-- Name: acco_user_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_name_must_be_unique UNIQUE (name);


--
-- Name: acco_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_pkey PRIMARY KEY (systemclassid);


--
-- Name: acco_usergroup2leaders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_usergroup2leaders_pkey PRIMARY KEY (leaderid, ledgroupid);


--
-- Name: acco_usergroup2members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_usergroup2members_pkey PRIMARY KEY (memberid, usergroupid);


--
-- Name: acco_usergroup_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_name_must_be_unique UNIQUE (name);


--
-- Name: acco_usergroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_pkey PRIMARY KEY (systemclassid);


--
-- Name: core_accessobject_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_accessobject_name_must_be_unique UNIQUE (name);


--
-- Name: core_accessobject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_accessobject_pkey PRIMARY KEY (systemclassid);


--
-- Name: core_annotation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT core_annotation_pkey PRIMARY KEY (attachmentid);


--
-- Name: core_applicationdata_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT core_applicationdata_pkey PRIMARY KEY (attachmentid);


--
-- Name: core_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_pkey PRIMARY KEY (dbid);


--
-- Name: core_bookcitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT core_bookcitation_pkey PRIMARY KEY (citationid);


--
-- Name: core_citation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT core_citation_pkey PRIMARY KEY (attachmentid);


--
-- Name: core_conferencecitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT core_conferencecitation_pkey PRIMARY KEY (citationid);


--
-- Name: core_externaldblink_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_externaldblink_pkey PRIMARY KEY (attachmentid);


--
-- Name: core_journalcitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT core_journalcitation_pkey PRIMARY KEY (citationid);


--
-- Name: core_labbookentry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labbookentry_pkey PRIMARY KEY (dbid);


--
-- Name: core_note2relatedentrys_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note2relatedentrys_pkey PRIMARY KEY (labbookentryid, noteid);


--
-- Name: core_note_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT core_note_pkey PRIMARY KEY (attachmentid);


--
-- Name: core_systemclass_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_systemclass
    ADD CONSTRAINT core_systemclass_pkey PRIMARY KEY (dbid);


--
-- Name: core_thesiscitation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT core_thesiscitation_pkey PRIMARY KEY (citationid);


--
-- Name: cryz_cypade_possva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_cypade_possva
    ADD CONSTRAINT cryz_cypade_possva_pkey PRIMARY KEY (parameterdefinitionid, order_);


--
-- Name: cryz_dropannotation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropannotation_pkey PRIMARY KEY (labbookentryid);


--
-- Name: cryz_image_filepath_filename_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_filepath_filename_must_be_unique UNIQUE (filepath, filename);


--
-- Name: cryz_image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_pkey PRIMARY KEY (labbookentryid);


--
-- Name: cryz_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_parameter_pkey PRIMARY KEY (labbookentryid);


--
-- Name: cryz_parameterdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT cryz_parameterdefinition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: cryz_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_pkey PRIMARY KEY (labbookentryid);


--
-- Name: cryz_score_value_scoringschemeid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_value_scoringschemeid_must_be_unique UNIQUE (value, scoringschemeid);


--
-- Name: cryz_scoringscheme_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_name_must_be_unique UNIQUE (name);


--
-- Name: cryz_scoringscheme_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_experiment_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_name_must_be_unique UNIQUE (name);


--
-- Name: expe_experiment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_experimentgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT expe_experimentgroup_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_inputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inputsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_instrument_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_methodparameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methodparameter_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_outputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outputsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_software_name_version_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_name_version_must_be_unique UNIQUE (name, version);


--
-- Name: expe_software_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_software_tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software_tasks
    ADD CONSTRAINT expe_software_tasks_pkey PRIMARY KEY (softwareid, order_);


--
-- Name: hold_abstractholder_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstractholder_name_must_be_unique UNIQUE (name);


--
-- Name: hold_abstractholder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstractholder_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_holdca2abstholders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT hold_holdca2abstholders_pkey PRIMARY KEY (abstholderid, holdercategoryid);


--
-- Name: hold_holdca2absthoty_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT hold_holdca2absthoty_pkey PRIMARY KEY (abstractholdertypeid, holdercategoryid);


--
-- Name: hold_holder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_pkey PRIMARY KEY (abstractholderid);


--
-- Name: hold_holderlocation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holderlocation_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_holdertypeposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdertypeposition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_refholder_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT hold_refholder_pkey PRIMARY KEY (abstractholderid);


--
-- Name: hold_refholderoffset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refholderoffset_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_refholderoffset_rowoffset_coloffset_suboffset_holderid_mus; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refholderoffset_rowoffset_coloffset_suboffset_holderid_mus UNIQUE (rowoffset, coloffset, suboffset, holderid);


--
-- Name: hold_refsampleposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_refsampleposition_rowposition_colposition_subposition_refh; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_rowposition_colposition_subposition_refh UNIQUE (rowposition, colposition, subposition, refholderid);


--
-- Name: inst_instty2inst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT inst_instty2inst_pkey PRIMARY KEY (instrumentid, instrumenttypeid);


--
-- Name: loca_location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_pkey PRIMARY KEY (labbookentryid);


--
-- Name: mole_abstco_keywords_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstco_keywords
    ADD CONSTRAINT mole_abstco_keywords_pkey PRIMARY KEY (abstractcomponentid, order_);


--
-- Name: mole_abstco_synonyms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstco_synonyms
    ADD CONSTRAINT mole_abstco_synonyms_pkey PRIMARY KEY (abstractcomponentid, order_);


--
-- Name: mole_abstractcomponent_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstractcomponent_name_must_be_unique UNIQUE (name);


--
-- Name: mole_abstractcomponent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstractcomponent_pkey PRIMARY KEY (labbookentryid);


--
-- Name: mole_compca2components_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT mole_compca2components_pkey PRIMARY KEY (componentid, categoryid);


--
-- Name: mole_construct_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT mole_construct_pkey PRIMARY KEY (moleculeid);


--
-- Name: mole_molecule2relareobel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT mole_molecule2relareobel_pkey PRIMARY KEY (relatedresearchobjectiveelementid, trialmoleculeid);


--
-- Name: mole_molecule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT mole_molecule_pkey PRIMARY KEY (abstractcomponentid);


--
-- Name: mole_moleculefeature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_moleculefeature_pkey PRIMARY KEY (labbookentryid);


--
-- Name: mole_primer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT mole_primer_pkey PRIMARY KEY (moleculeid);


--
-- Name: peop_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_pkey PRIMARY KEY (labbookentryid);


--
-- Name: peop_orga_addresses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_orga_addresses
    ADD CONSTRAINT peop_orga_addresses_pkey PRIMARY KEY (organisationid, order_);


--
-- Name: peop_organisation_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_organisation_name_must_be_unique UNIQUE (name);


--
-- Name: peop_organisation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_organisation_pkey PRIMARY KEY (labbookentryid);


--
-- Name: peop_persingr_phonnu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_persingr_phonnu
    ADD CONSTRAINT peop_persingr_phonnu_pkey PRIMARY KEY (personingroupid, order_);


--
-- Name: peop_person_middin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_person_middin
    ADD CONSTRAINT peop_person_middin_pkey PRIMARY KEY (personid, order_);


--
-- Name: peop_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_pkey PRIMARY KEY (labbookentryid);


--
-- Name: peop_personingroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_personingroup_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_parade_possva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parade_possva
    ADD CONSTRAINT prot_parade_possva_pkey PRIMARY KEY (parameterdefinitionid, order_);


--
-- Name: prot_parameterdefinition_name_protocolid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_name_protocolid_must_be_unique UNIQUE (name, protocolid);


--
-- Name: prot_parameterdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_protocol_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_name_must_be_unique UNIQUE (name);


--
-- Name: prot_protocol_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_protocol_remarks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol_remarks
    ADD CONSTRAINT prot_protocol_remarks_pkey PRIMARY KEY (protocolid, order_);


--
-- Name: prot_refinputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinputsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_refinputsample_protocolid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinputsample_protocolid_key UNIQUE (protocolid, name);


--
-- Name: prot_refoutputsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refoutputsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_refoutputsample_protocolid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refoutputsample_protocolid_key UNIQUE (protocolid, name);


--
-- Name: ref_abstractholdertype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_name_must_be_unique UNIQUE (name);


--
-- Name: ref_abstractholdertype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_componentcategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_componentcategory_name_must_be_unique UNIQUE (name);


--
-- Name: ref_componentcategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_componentcategory_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_crystaltype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT ref_crystaltype_pkey PRIMARY KEY (holdertypeid);


--
-- Name: ref_dbname_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_name_must_be_unique UNIQUE (name);


--
-- Name: ref_dbname_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_experimenttype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_name_must_be_unique UNIQUE (name);


--
-- Name: ref_experimenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_hazardphrase_classification_code_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_classification_code_must_be_unique UNIQUE (classification, code);


--
-- Name: ref_hazardphrase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_holdercategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdercategory_name_must_be_unique UNIQUE (name);


--
-- Name: ref_holdercategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdercategory_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_holdertype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_pkey PRIMARY KEY (abstractholdertypeid);


--
-- Name: ref_imagetype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_name_must_be_unique UNIQUE (name);


--
-- Name: ref_imagetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_instrumenttype_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_name_must_be_unique UNIQUE (name);


--
-- Name: ref_instrumenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_organism_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_name_must_be_unique UNIQUE (name);


--
-- Name: ref_organism_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_pintype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT ref_pintype_pkey PRIMARY KEY (abstractholdertypeid);


--
-- Name: ref_publicentry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_publicentry
    ADD CONSTRAINT ref_publicentry_pkey PRIMARY KEY (dbid);


--
-- Name: ref_samplecategory_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_name_must_be_unique UNIQUE (name);


--
-- Name: ref_samplecategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_targetstatus_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targetstatus_name_must_be_unique UNIQUE (name);


--
-- Name: ref_targetstatus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targetstatus_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_workflowitem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workflowitem_pkey PRIMARY KEY (publicentryid);


--
-- Name: revisionnumber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY revisionnumber
    ADD CONSTRAINT revisionnumber_pkey PRIMARY KEY (revision);


--
-- Name: sam_abstractsample_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_name_must_be_unique UNIQUE (name);


--
-- Name: sam_abstractsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sam_abstsa2hazaph_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa2hazaph_pkey PRIMARY KEY (hazardphraseid, otherrole);


--
-- Name: sam_crystalsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT sam_crystalsample_pkey PRIMARY KEY (sampleid);


--
-- Name: sam_refsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT sam_refsample_pkey PRIMARY KEY (abstractsampleid);


--
-- Name: sam_refsamplesource_catalognum_refsampleid_refholderid_must_be_; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_catalognum_refsampleid_refholderid_must_be_ UNIQUE (catalognum, refsampleid, refholderid);


--
-- Name: sam_refsamplesource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sam_sampca2abstsa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_sampca2abstsa_pkey PRIMARY KEY (samplecategoryid, abstractsampleid);


--
-- Name: sam_sample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_pkey PRIMARY KEY (abstractsampleid);


--
-- Name: sam_samplecomponent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_samplecomponent_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sam_trialsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_trialsample
    ADD CONSTRAINT sam_trialsample_pkey PRIMARY KEY (sampleid);


--
-- Name: sche_scheduledtask_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sche_scheduledtask_scheduledtime_holderid_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_scheduledtime_holderid_must_be_unique UNIQUE (scheduledtime, holderid);


--
-- Name: sche_scheduleplan_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_name_must_be_unique UNIQUE (name);


--
-- Name: sche_scheduleplan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sche_scheduleplanoffset_offsettime_scheduleplanid_must_be_uniqu; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheduleplanoffset_offsettime_scheduleplanid_must_be_uniqu UNIQUE (offsettime, scheduleplanid);


--
-- Name: sche_scheduleplanoffset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheduleplanoffset_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_alias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_milestone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_researchobjective_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_researchobjective_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_target2nuclac_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT targ_target2nuclac_pkey PRIMARY KEY (nucleicacidid, nuctargetid);


--
-- Name: targ_target2projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_target2projects_pkey PRIMARY KEY (projectid, targetid);


--
-- Name: targ_target_name_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_name_must_be_unique UNIQUE (name);


--
-- Name: targ_target_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_targetgroup_namingsystem_shortname_must_be_unique; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_namingsystem_shortname_must_be_unique UNIQUE (namingsystem, shortname);


--
-- Name: targ_targetgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_targgr2targets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_targgr2targets_pkey PRIMARY KEY (targetid, targetgroupid);


--
-- Name: trag_researchobjectiveelement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_researchobjectiveelement_pkey PRIMARY KEY (labbookentryid);


--
-- Name: acco_permission_acceob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_permission_acceob_inx ON acco_permission USING btree (accessobjectid);


--
-- Name: acco_permission_usergroup_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_permission_usergroup_inx ON acco_permission USING btree (usergroupid);


--
-- Name: acco_user_person_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_user_person_inx ON acco_user USING btree (personid);


--
-- Name: acco_usergroup_header_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX acco_usergroup_header_inx ON acco_usergroup USING btree (headerid);


--
-- Name: core_attachment_author_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_attachment_author_inx ON core_attachment USING btree (authorid);


--
-- Name: core_attachment_pareen_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_attachment_pareen_inx ON core_attachment USING btree (parententryid);


--
-- Name: core_extedbli_dbname_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_extedbli_dbname_inx ON core_externaldblink USING btree (dbnameid);


--
-- Name: core_labboen_access_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_access_inx ON core_labbookentry USING btree (accessid);


--
-- Name: core_labboen_creator_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_creator_inx ON core_labbookentry USING btree (creatorid);


--
-- Name: core_labboen_lasteditor_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX core_labboen_lasteditor_inx ON core_labbookentry USING btree (lasteditorid);


--
-- Name: cryz_cypa_cypade_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_cypa_cypade_inx ON cryz_parameter USING btree (parameterdefinitionid);


--
-- Name: cryz_cypa_image_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_cypa_image_inx ON cryz_parameter USING btree (imageid);


--
-- Name: cryz_dropan_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_holder_inx ON cryz_dropannotation USING btree (holderid);


--
-- Name: cryz_dropan_image_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_image_inx ON cryz_dropannotation USING btree (imageid);


--
-- Name: cryz_dropan_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_sample_inx ON cryz_dropannotation USING btree (sampleid);


--
-- Name: cryz_dropan_score_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_score_inx ON cryz_dropannotation USING btree (scoreid);


--
-- Name: cryz_dropan_software_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_dropan_software_inx ON cryz_dropannotation USING btree (softwareid);


--
-- Name: cryz_image_imagetype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_imagetype_inx ON cryz_image USING btree (imagetypeid);


--
-- Name: cryz_image_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_instrument_inx ON cryz_image USING btree (instrumentid);


--
-- Name: cryz_image_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_sample_inx ON cryz_image USING btree (sampleid);


--
-- Name: cryz_image_scheta_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_image_scheta_inx ON cryz_image USING btree (scheduledtaskid);


--
-- Name: cryz_score_scorsc_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX cryz_score_scorsc_inx ON cryz_score USING btree (scoringschemeid);


--
-- Name: expe_experiment_expegr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_expegr_inx ON expe_experiment USING btree (experimentgroupid);


--
-- Name: expe_experiment_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_expety_inx ON expe_experiment USING btree (experimenttypeid);


--
-- Name: expe_experiment_group_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_group_inx ON expe_experiment USING btree (groupid);


--
-- Name: expe_experiment_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_instrument_inx ON expe_experiment USING btree (instrumentid);


--
-- Name: expe_experiment_method_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_method_inx ON expe_experiment USING btree (methodid);


--
-- Name: expe_experiment_operator_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_operator_inx ON expe_experiment USING btree (operatorid);


--
-- Name: expe_experiment_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_protocol_inx ON expe_experiment USING btree (protocolid);


--
-- Name: expe_experiment_reseob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_experiment_reseob_inx ON expe_experiment USING btree (researchobjectiveid);


--
-- Name: expe_inpusa_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_experiment_inx ON expe_inputsample USING btree (experimentid);


--
-- Name: expe_inpusa_refinsa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_refinsa_inx ON expe_inputsample USING btree (refinputsampleid);


--
-- Name: expe_inpusa_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_inpusa_sample_inx ON expe_inputsample USING btree (sampleid);


--
-- Name: expe_instrument_defaimty_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_defaimty_inx ON expe_instrument USING btree (defaultimagetypeid);


--
-- Name: expe_instrument_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_location_inx ON expe_instrument USING btree (locationid);


--
-- Name: expe_instrument_manu_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_instrument_manu_inx ON expe_instrument USING btree (manufacturerid);


--
-- Name: expe_method_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_method_instrument_inx ON expe_method USING btree (instrumentid);


--
-- Name: expe_method_software_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_method_software_inx ON expe_method USING btree (softwareid);


--
-- Name: expe_methpa_method_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_methpa_method_inx ON expe_methodparameter USING btree (methodid);


--
-- Name: expe_outpsa_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_experiment_inx ON expe_outputsample USING btree (experimentid);


--
-- Name: expe_outpsa_refousa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_refousa_inx ON expe_outputsample USING btree (refoutputsampleid);


--
-- Name: expe_outpsa_sample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_outpsa_sample_inx ON expe_outputsample USING btree (sampleid);


--
-- Name: expe_parameter_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_parameter_experiment_inx ON expe_parameter USING btree (experimentid);


--
-- Name: expe_parameter_parade_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX expe_parameter_parade_inx ON expe_parameter USING btree (parameterdefinitionid);


--
-- Name: hold_abstho_holdertype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_abstho_holdertype_inx ON hold_abstractholder USING btree (holdertypeid);


--
-- Name: hold_abstho_supholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_abstho_supholder_inx ON hold_abstractholder USING btree (abstractholderid);


--
-- Name: hold_holdlo_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdlo_holder_inx ON hold_holderlocation USING btree (holderid);


--
-- Name: hold_holdlo_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdlo_location_inx ON hold_holderlocation USING btree (locationid);


--
-- Name: hold_holdtypo_holdertype_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holdtypo_holdertype_inx ON hold_holdertypeposition USING btree (holdertypeid);


--
-- Name: hold_refhoof_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refhoof_holder_inx ON hold_refholderoffset USING btree (holderid);


--
-- Name: hold_refhoof_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refhoof_refholder_inx ON hold_refholderoffset USING btree (refholderid);


--
-- Name: hold_refsapo_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refsapo_refholder_inx ON hold_refsampleposition USING btree (refholderid);


--
-- Name: hold_refsapo_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_refsapo_refsample_inx ON hold_refsampleposition USING btree (refsampleid);


--
-- Name: loca_location_location_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX loca_location_location_inx ON loca_location USING btree (locationid);


--
-- Name: loca_location_orga_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX loca_location_orga_inx ON loca_location USING btree (organisationid);


--
-- Name: mole_abstco_natuso_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_abstco_natuso_inx ON mole_abstractcomponent USING btree (naturalsourceid);


--
-- Name: mole_molefe_molecule_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_molefe_molecule_inx ON mole_moleculefeature USING btree (moleculeid);


--
-- Name: mole_molefe_refmo_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX mole_molefe_refmo_inx ON mole_moleculefeature USING btree (refmoleculeid);


--
-- Name: peop_group_orga_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_group_orga_inx ON peop_group USING btree (organisationid);


--
-- Name: peop_persingr_group_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_persingr_group_inx ON peop_personingroup USING btree (groupid);


--
-- Name: peop_persingr_person_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_persingr_person_inx ON peop_personingroup USING btree (personid);


--
-- Name: peop_person_currgr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX peop_person_currgr_inx ON peop_person USING btree (currentgroupid);


--
-- Name: prot_parade_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_parade_protocol_inx ON prot_parameterdefinition USING btree (protocolid);


--
-- Name: prot_protocol_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_protocol_expety_inx ON prot_protocol USING btree (experimenttypeid);


--
-- Name: prot_refinsa_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refinsa_protocol_inx ON prot_refinputsample USING btree (protocolid);


--
-- Name: prot_refinsa_sampca_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refinsa_sampca_inx ON prot_refinputsample USING btree (samplecategoryid);


--
-- Name: prot_refousa_protocol_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refousa_protocol_inx ON prot_refoutputsample USING btree (protocolid);


--
-- Name: prot_refousa_sampca_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX prot_refousa_sampca_inx ON prot_refoutputsample USING btree (samplecategoryid);


--
-- Name: ref_holdertype_defascpl_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_holdertype_defascpl_inx ON ref_holdertype USING btree (defaultscheduleplanid);


--
-- Name: ref_workit_expety_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_workit_expety_inx ON ref_workflowitem USING btree (experimenttypeid);


--
-- Name: ref_workit_status_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX ref_workit_status_inx ON ref_workflowitem USING btree (statusid);


--
-- Name: sam_refsaso_refholder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_refholder_inx ON sam_refsamplesource USING btree (refholderid);


--
-- Name: sam_refsaso_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_refsample_inx ON sam_refsamplesource USING btree (refsampleid);


--
-- Name: sam_refsaso_supplier_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_refsaso_supplier_inx ON sam_refsamplesource USING btree (supplierid);


--
-- Name: sam_sampco_abstsa_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_abstsa_inx ON sam_samplecomponent USING btree (abstractsampleid);


--
-- Name: sam_sampco_container_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_container_inx ON sam_samplecomponent USING btree (containerid);


--
-- Name: sam_sampco_refco_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_refco_inx ON sam_samplecomponent USING btree (refcomponentid);


--
-- Name: sam_sampco_reseobel_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sampco_reseobel_inx ON sam_samplecomponent USING btree (researchobjectivelementid);


--
-- Name: sam_sample_assignto_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_assignto_inx ON sam_sample USING btree (assigntoid);


--
-- Name: sam_sample_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_holder_inx ON sam_sample USING btree (holderid);


--
-- Name: sam_sample_refsample_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_sample_refsample_inx ON sam_sample USING btree (refsampleid);


--
-- Name: sam_triasa_condition_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_triasa_condition_inx ON sam_trialsample USING btree (conditionid);


--
-- Name: sam_triasa_protein_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sam_triasa_protein_inx ON sam_trialsample USING btree (proteinid);


--
-- Name: sche_scheplof_schepl_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheplof_schepl_inx ON sche_scheduleplanoffset USING btree (scheduleplanid);


--
-- Name: sche_scheta_comptime_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_comptime_inx ON sche_scheduledtask USING btree (completiontime);


--
-- Name: sche_scheta_holder_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_holder_inx ON sche_scheduledtask USING btree (holderid);


--
-- Name: sche_scheta_instrument_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_instrument_inx ON sche_scheduledtask USING btree (instrumentid);


--
-- Name: sche_scheta_scheplof_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX sche_scheta_scheplof_inx ON sche_scheduledtask USING btree (scheduleplanoffsetid);


--
-- Name: targ_alias_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_alias_target_inx ON targ_alias USING btree (targetid);


--
-- Name: targ_milestone_experiment_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_experiment_inx ON targ_milestone USING btree (experimentid);


--
-- Name: targ_milestone_status_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_status_inx ON targ_milestone USING btree (statusid);


--
-- Name: targ_milestone_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_milestone_target_inx ON targ_milestone USING btree (targetid);


--
-- Name: targ_project_project_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_project_project_inx ON targ_project USING btree (projectid);


--
-- Name: targ_reseob_owner_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_reseob_owner_inx ON targ_researchobjective USING btree (ownerid);


--
-- Name: targ_target_protein_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_target_protein_inx ON targ_target USING btree (proteinid);


--
-- Name: targ_target_species_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_target_species_inx ON targ_target USING btree (speciesid);


--
-- Name: targ_targgr_targgr_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX targ_targgr_targgr_inx ON targ_targetgroup USING btree (targetgroupid);


--
-- Name: trag_reseobel_molecule_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_molecule_inx ON trag_researchobjectiveelement USING btree (moleculeid);


--
-- Name: trag_reseobel_reseob_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_reseob_inx ON trag_researchobjectiveelement USING btree (researchobjectiveid);


--
-- Name: trag_reseobel_target_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX trag_reseobel_target_inx ON trag_researchobjectiveelement USING btree (targetid);


--
-- Name: acco_permission_acceob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_acceob_fk FOREIGN KEY (accessobjectid) REFERENCES core_accessobject(systemclassid) ON DELETE SET NULL;


--
-- Name: acco_permission_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- Name: acco_permission_usergroup_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_usergroup_fk FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- Name: acco_user_ledgroups_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_user_ledgroups_fk FOREIGN KEY (ledgroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- Name: acco_user_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_person_fk FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- Name: acco_user_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- Name: acco_user_usergroups_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_user_usergroups_fk FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid) ON DELETE CASCADE;


--
-- Name: acco_usergroup_header_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_header_fk FOREIGN KEY (headerid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- Name: acco_usergroup_leaders_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT acco_usergroup_leaders_fk FOREIGN KEY (leaderid) REFERENCES acco_user(systemclassid) ON DELETE CASCADE;


--
-- Name: acco_usergroup_members_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT acco_usergroup_members_fk FOREIGN KEY (memberid) REFERENCES acco_user(systemclassid) ON DELETE CASCADE;


--
-- Name: acco_usergroup_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- Name: core_acceob_systcl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_acceob_systcl_fk FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid) ON DELETE CASCADE;


--
-- Name: core_annotation_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT core_annotation_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- Name: core_applda_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT core_applda_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- Name: core_attachment_author_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_author_fk FOREIGN KEY (authorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- Name: core_attachment_pareen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT core_attachment_pareen_fk FOREIGN KEY (parententryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: core_bookci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT core_bookci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- Name: core_citation_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT core_citation_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- Name: core_confci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT core_confci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- Name: core_extedbli_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_extedbli_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- Name: core_extedbli_dbname_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT core_extedbli_dbname_fk FOREIGN KEY (dbnameid) REFERENCES ref_dbname(publicentryid) ON DELETE SET NULL;


--
-- Name: core_jourci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT core_jourci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- Name: core_labboen_access_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_access_fk FOREIGN KEY (accessid) REFERENCES core_accessobject(systemclassid);


--
-- Name: core_labboen_creator_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_creator_fk FOREIGN KEY (creatorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- Name: core_labboen_lasteditor_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT core_labboen_lasteditor_fk FOREIGN KEY (lasteditorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- Name: core_note_attachment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT core_note_attachment_fk FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid) ON DELETE CASCADE;


--
-- Name: core_note_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note_otherrole_fk FOREIGN KEY (noteid) REFERENCES core_note(attachmentid) ON DELETE CASCADE;


--
-- Name: core_note_relaen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT core_note_relaen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: core_thesci_citation_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT core_thesci_citation_fk FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid) ON DELETE CASCADE;


--
-- Name: cryz_cypa_cypade_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_cypade_fk FOREIGN KEY (parameterdefinitionid) REFERENCES cryz_parameterdefinition(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_cypa_image_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_image_fk FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid) ON DELETE CASCADE;


--
-- Name: cryz_cypa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT cryz_cypa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: cryz_cypade_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT cryz_cypade_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: cryz_dropan_image_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_image_fk FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_dropan_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: cryz_dropan_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: cryz_dropan_score_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_score_fk FOREIGN KEY (scoreid) REFERENCES cryz_score(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_dropan_software_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT cryz_dropan_software_fk FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_image_imagetype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_imagetype_fk FOREIGN KEY (imagetypeid) REFERENCES ref_imagetype(publicentryid) ON DELETE SET NULL;


--
-- Name: cryz_image_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_image_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: cryz_image_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: cryz_image_scheta_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT cryz_image_scheta_fk FOREIGN KEY (scheduledtaskid) REFERENCES sche_scheduledtask(labbookentryid) ON DELETE SET NULL;


--
-- Name: cryz_score_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: cryz_score_scorsc_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_scorsc_fk FOREIGN KEY (scoringschemeid) REFERENCES cryz_scoringscheme(labbookentryid) ON DELETE CASCADE;


--
-- Name: cryz_scorsc_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scorsc_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_expegr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT expe_expegr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_experiment_expegr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_expegr_fk FOREIGN KEY (experimentgroupid) REFERENCES expe_experimentgroup(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_group_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_group_fk FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_experiment_method_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_method_fk FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_operator_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_operator_fk FOREIGN KEY (operatorid) REFERENCES acco_user(systemclassid) ON DELETE SET NULL;


--
-- Name: expe_experiment_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_experiment_reseob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_reseob_fk FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_inpusa_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- Name: expe_inpusa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_inpusa_refinsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_refinsa_fk FOREIGN KEY (refinputsampleid) REFERENCES prot_refinputsample(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_inpusa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT expe_inpusa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: expe_instrument_defaimty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_defaimty_fk FOREIGN KEY (defaultimagetypeid) REFERENCES ref_imagetype(publicentryid) ON DELETE SET NULL;


--
-- Name: expe_instrument_instty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT expe_instrument_instty_fk FOREIGN KEY (instrumenttypeid) REFERENCES ref_instrumenttype(publicentryid) ON DELETE CASCADE;


--
-- Name: expe_instrument_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_instrument_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_instrument_manu_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT expe_instrument_manu_fk FOREIGN KEY (manufacturerid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_method_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_method_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_method_software_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT expe_method_software_fk FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_methpa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methpa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_methpa_method_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT expe_methpa_method_fk FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid) ON DELETE CASCADE;


--
-- Name: expe_outpsa_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- Name: expe_outpsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_outpsa_refousa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_refousa_fk FOREIGN KEY (refoutputsampleid) REFERENCES prot_refoutputsample(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_outpsa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outpsa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: expe_parameter_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE CASCADE;


--
-- Name: expe_parameter_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: expe_parameter_parade_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_parade_fk FOREIGN KEY (parameterdefinitionid) REFERENCES prot_parameterdefinition(labbookentryid) ON DELETE SET NULL;


--
-- Name: expe_software_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: fk1177e05ff334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT fk1177e05ff334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- Name: fk12b307d4f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT fk12b307d4f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk12d769d2578d9e96; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_cypade_possva
    ADD CONSTRAINT fk12d769d2578d9e96 FOREIGN KEY (parameterdefinitionid) REFERENCES cryz_parameterdefinition(labbookentryid);


--
-- Name: fk139af87045a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT fk139af87045a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk139af8709b03e5d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT fk139af8709b03e5d3 FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid);


--
-- Name: fk14ff48dc3ce3876a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT fk14ff48dc3ce3876a FOREIGN KEY (accessid) REFERENCES core_accessobject(systemclassid);


--
-- Name: fk17270e5ef6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_journalcitation
    ADD CONSTRAINT fk17270e5ef6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- Name: fk1d43c997f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT fk1d43c997f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk1deae42b45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experimentgroup
    ADD CONSTRAINT fk1deae42b45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk28bb5b13a4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT fk28bb5b13a4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk28d12df245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT fk28d12df245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk292a329e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk2a93f84145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT fk2a93f84145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk2cdc1e5745a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT fk2cdc1e5745a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk3336a78766ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_citation
    ADD CONSTRAINT fk3336a78766ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fk3363f41145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT fk3363f41145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk340aa18f45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT fk340aa18f45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk34b0597c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT fk34b0597c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk3792bdb845a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb845a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk3792bdb87f9149b9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb87f9149b9 FOREIGN KEY (supplierid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fk3792bdb8c7c01158; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb8c7c01158 FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid);


--
-- Name: fk387e5ff6f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT fk387e5ff6f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk38f93cc445a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT fk38f93cc445a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk38f93cc46f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT fk38f93cc46f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- Name: fk3e8c1b5d3e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT fk3e8c1b5d3e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk45b3e7aac405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT fk45b3e7aac405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fk4718237145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT fk4718237145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk4942a49266ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT fk4942a49266ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fk4a04edda45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT fk4a04edda45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk4ab4c22a3e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22a3e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk55cebc0ff8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT fk55cebc0ff8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk57c8291c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT fk57c8291c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk5e0f47f7a4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT fk5e0f47f7a4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk5e9b390ff8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT fk5e9b390ff8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk5e9e49419c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT fk5e9e49419c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fk5e9e49432c1d927; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT fk5e9e49432c1d927 FOREIGN KEY (instrumenttypeid) REFERENCES ref_instrumenttype(publicentryid);


--
-- Name: fk6390c30e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT fk6390c30e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk6c4167c098dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT fk6c4167c098dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fk703f362045a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f362045a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk724b8b7b5e56f7c7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT fk724b8b7b5e56f7c7 FOREIGN KEY (ledgroupid) REFERENCES acco_usergroup(systemclassid);


--
-- Name: fk724b8b7b9a61164b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2leaders
    ADD CONSTRAINT fk724b8b7b9a61164b FOREIGN KEY (leaderid) REFERENCES acco_user(systemclassid);


--
-- Name: fk77cc02e245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk78db483a66ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_applicationdata
    ADD CONSTRAINT fk78db483a66ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fk7c3c7eb345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT fk7c3c7eb345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk7c4a35b245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT fk7c4a35b245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk7de59170f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_bookcitation
    ADD CONSTRAINT fk7de59170f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- Name: fk84a833fa6d03126f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT fk84a833fa6d03126f FOREIGN KEY (otherrole) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk84a833fabe6bb47b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT fk84a833fabe6bb47b FOREIGN KEY (hazardphraseid) REFERENCES ref_hazardphrase(publicentryid);


--
-- Name: fk85e2b8f7c405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstco_keywords
    ADD CONSTRAINT fk85e2b8f7c405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fk8636d8899b62c547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_orga_addresses
    ADD CONSTRAINT fk8636d8899b62c547 FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fk8779ffad45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk8a00037645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT fk8a00037645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk8ccd4c764983192f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT fk8ccd4c764983192f FOREIGN KEY (nuctargetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fk8ccd4c76d488084e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT fk8ccd4c76d488084e FOREIGN KEY (nucleicacidid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk8e3939f33e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f33e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk8e3939f345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk8e3939f3918b07df; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f3918b07df FOREIGN KEY (refcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fk96337253f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_thesiscitation
    ADD CONSTRAINT fk96337253f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- Name: fk96600bec45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT fk96600bec45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk987c4be345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT fk987c4be345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk98bf9b1845a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT fk98bf9b1845a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk9a3000cc45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT fk9a3000cc45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fka28270e145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT fka28270e145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fka54d839e9b03e5d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839e9b03e5d3 FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid);


--
-- Name: fka54d839eabbc44da; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839eabbc44da FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid);


--
-- Name: fka54d839ef8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT fka54d839ef8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fka7d9f80a16a03b9c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT fka7d9f80a16a03b9c FOREIGN KEY (memberid) REFERENCES acco_user(systemclassid);


--
-- Name: fka7d9f80ac72f3607; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2members
    ADD CONSTRAINT fka7d9f80ac72f3607 FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid);


--
-- Name: fka8cd91f245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT fka8cd91f245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fka949ba63938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT fka949ba63938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- Name: fkace29f4f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT fkace29f4f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fkad88e8edf8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT fkad88e8edf8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fkb1cfdbcf66ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_annotation
    ADD CONSTRAINT fkb1cfdbcf66ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fkb1d35d0645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d0645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkb1d35d066f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d066f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- Name: fkb1d35d06b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT fkb1d35d06b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- Name: fkb2e6e6579cc3e68a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fkb2e6e6579cc3e68a FOREIGN KEY (categoryid) REFERENCES ref_componentcategory(publicentryid);


--
-- Name: fkb2e6e657d24dca52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fkb2e6e657d24dca52 FOREIGN KEY (componentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fkb37f6603f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT fkb37f6603f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fkb5660fced18a88ad; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_persingr_phonnu
    ADD CONSTRAINT fkb5660fced18a88ad FOREIGN KEY (personingroupid) REFERENCES peop_personingroup(labbookentryid);


--
-- Name: fkb6ca7555f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT fkb6ca7555f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fkbb2a4c2c1488c4a7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT fkbb2a4c2c1488c4a7 FOREIGN KEY (noteid) REFERENCES core_note(attachmentid);


--
-- Name: fkbb2a4c2c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note2relatedentrys
    ADD CONSTRAINT fkbb2a4c2c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkbe85648645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT fkbe85648645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkbeda3f82b5ac617d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person_middin
    ADD CONSTRAINT fkbeda3f82b5ac617d FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid);


--
-- Name: fkc400ddb0651418f9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT fkc400ddb0651418f9 FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid);


--
-- Name: fkc400ddb0e1a02ab2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT fkc400ddb0e1a02ab2 FOREIGN KEY (abstholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- Name: fkc46d9b1d7f20b0de; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT fkc46d9b1d7f20b0de FOREIGN KEY (trialmoleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fkc46d9b1dbf1274b8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT fkc46d9b1dbf1274b8 FOREIGN KEY (relatedresearchobjectiveelementid) REFERENCES trag_researchobjectiveelement(labbookentryid);


--
-- Name: fkc4a62d1e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT fkc4a62d1e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkc9a2ac0c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkca2b414345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT fkca2b414345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkced55fd45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT fkced55fd45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkcfdca6f33e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT fkcfdca6f33e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fkcfdca6f3b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT fkcfdca6f3b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- Name: fkd053761e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT fkd053761e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd0c3b4a3f6883d51; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_conferencecitation
    ADD CONSTRAINT fkd0c3b4a3f6883d51 FOREIGN KEY (citationid) REFERENCES core_citation(attachmentid);


--
-- Name: fkd0e6aab445a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab445a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd1fd7632938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT fkd1fd7632938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- Name: fkd23f4ee545a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT fkd23f4ee545a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd293391345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT fkd293391345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd3b25d25c405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstco_synonyms
    ADD CONSTRAINT fkd3b25d25c405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fkd41b835e5336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT fkd41b835e5336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fkd41b835e58259e17; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT fkd41b835e58259e17 FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid);


--
-- Name: fkd4df77f945a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT fkd4df77f945a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd6ff0bce45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT fkd6ff0bce45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkdd40275945a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd40275945a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkdd4027596f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd4027596f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- Name: fkdd402759b4e92575; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT fkdd402759b4e92575 FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid);


--
-- Name: fke721b71ef8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT fke721b71ef8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fke8fbe9ba5336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT fke8fbe9ba5336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fke8fbe9ba8b86e8a1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT fke8fbe9ba8b86e8a1 FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid);


--
-- Name: fkf0b2d0545a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT fkf0b2d0545a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkf2966e5059eec984; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parade_possva
    ADD CONSTRAINT fkf2966e5059eec984 FOREIGN KEY (parameterdefinitionid) REFERENCES prot_parameterdefinition(labbookentryid);


--
-- Name: fkf63805a366ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT fkf63805a366ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fkf8cc82a0a3967be9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT fkf8cc82a0a3967be9 FOREIGN KEY (accessobjectid) REFERENCES core_accessobject(systemclassid);


--
-- Name: fkf8cc82a0c72f3607; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT fkf8cc82a0c72f3607 FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid);


--
-- Name: fkf9b406ad45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameterdefinition
    ADD CONSTRAINT fkf9b406ad45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkfb7c68046f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol_remarks
    ADD CONSTRAINT fkfb7c68046f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- Name: fkfbc9a184b4e81f71; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT fkfbc9a184b4e81f71 FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid);


--
-- Name: fkfd882103651418f9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT fkfd882103651418f9 FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid);


--
-- Name: fkfd882103938cec55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT fkfd882103938cec55 FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- Name: fkff38ce0df92d0173; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_software_tasks
    ADD CONSTRAINT fkff38ce0df92d0173 FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid);


--
-- Name: fkffe6cd2cf334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT fkffe6cd2cf334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


--
-- Name: hold_abstho_holdca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT hold_abstho_holdca_fk FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid) ON DELETE CASCADE;


--
-- Name: hold_abstho_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE SET NULL;


--
-- Name: hold_abstho_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: hold_abstho_supholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstho_supholder_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE SET NULL;


--
-- Name: hold_holder_abstho_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT hold_holder_abstho_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- Name: hold_holdlo_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- Name: hold_holdlo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: hold_holdlo_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT hold_holdlo_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- Name: hold_holdtypo_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdtypo_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid) ON DELETE CASCADE;


--
-- Name: hold_holdtypo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT hold_holdtypo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: hold_refholder_abstho_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholder
    ADD CONSTRAINT hold_refholder_abstho_fk FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- Name: hold_refhoof_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- Name: hold_refhoof_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: hold_refhoof_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refhoof_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE SET NULL;


--
-- Name: hold_refsapo_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: hold_refsapo_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE CASCADE;


--
-- Name: hold_refsapo_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsapo_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: loca_location_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: loca_location_location_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_location_fk FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid) ON DELETE SET NULL;


--
-- Name: loca_location_orga_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT loca_location_orga_fk FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- Name: mole_abstco_categories_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT mole_abstco_categories_fk FOREIGN KEY (categoryid) REFERENCES ref_componentcategory(publicentryid) ON DELETE CASCADE;


--
-- Name: mole_abstco_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstco_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: mole_abstco_natuso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstco_natuso_fk FOREIGN KEY (naturalsourceid) REFERENCES ref_organism(publicentryid) ON DELETE SET NULL;


--
-- Name: mole_construct_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_construct
    ADD CONSTRAINT mole_construct_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- Name: mole_molecule_abstco_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule
    ADD CONSTRAINT mole_molecule_abstco_fk FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE CASCADE;


--
-- Name: mole_molecule_nuctargets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT mole_molecule_nuctargets_fk FOREIGN KEY (nuctargetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- Name: mole_molecule_relareobel_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT mole_molecule_relareobel_fk FOREIGN KEY (relatedresearchobjectiveelementid) REFERENCES trag_researchobjectiveelement(labbookentryid) ON DELETE CASCADE;


--
-- Name: mole_molefe_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: mole_molefe_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- Name: mole_molefe_refmo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT mole_molefe_refmo_fk FOREIGN KEY (refmoleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- Name: mole_primer_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_primer
    ADD CONSTRAINT mole_primer_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- Name: peop_group_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: peop_group_orga_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT peop_group_orga_fk FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid) ON DELETE CASCADE;


--
-- Name: peop_orga_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_orga_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: peop_persingr_group_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_group_fk FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- Name: peop_persingr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: peop_persingr_person_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT peop_persingr_person_fk FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid) ON DELETE CASCADE;


--
-- Name: peop_person_currgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_currgr_fk FOREIGN KEY (currentgroupid) REFERENCES peop_group(labbookentryid) ON DELETE SET NULL;


--
-- Name: peop_person_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT peop_person_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: prot_parade_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parade_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: prot_parade_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parade_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- Name: prot_protocol_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- Name: prot_protocol_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: prot_refinsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: prot_refinsa_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- Name: prot_refinsa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refinputsample
    ADD CONSTRAINT prot_refinsa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE SET NULL;


--
-- Name: prot_refousa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: prot_refousa_protocol_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_protocol_fk FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid) ON DELETE CASCADE;


--
-- Name: prot_refousa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY prot_refoutputsample
    ADD CONSTRAINT prot_refousa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE SET NULL;


--
-- Name: ref_absthoty_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_absthoty_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_compca_components_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT ref_compca_components_fk FOREIGN KEY (componentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE CASCADE;


--
-- Name: ref_compca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_compca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_crysty_holdertype_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_crystaltype
    ADD CONSTRAINT ref_crysty_holdertype_fk FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid) ON DELETE CASCADE;


--
-- Name: ref_dbname_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_expety_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_expety_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_hazaph_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazaph_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_holdca_abstho_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2abstholders
    ADD CONSTRAINT ref_holdca_abstho_fk FOREIGN KEY (abstholderid) REFERENCES hold_abstractholder(labbookentryid) ON DELETE CASCADE;


--
-- Name: ref_holdca_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT ref_holdca_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- Name: ref_holdca_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdca2absthoty
    ADD CONSTRAINT ref_holdca_otherrole_fk FOREIGN KEY (holdercategoryid) REFERENCES ref_holdercategory(publicentryid) ON DELETE CASCADE;


--
-- Name: ref_holdca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_holdertype_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- Name: ref_holdertype_defascpl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT ref_holdertype_defascpl_fk FOREIGN KEY (defaultscheduleplanid) REFERENCES sche_scheduleplan(labbookentryid) ON DELETE SET NULL;


--
-- Name: ref_imagetype_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_instty_inst_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inst_instty2inst
    ADD CONSTRAINT ref_instty_inst_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE CASCADE;


--
-- Name: ref_instty_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instty_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_organism_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_pintype_absthoty_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_pintype
    ADD CONSTRAINT ref_pintype_absthoty_fk FOREIGN KEY (abstractholdertypeid) REFERENCES ref_abstractholdertype(publicentryid) ON DELETE CASCADE;


--
-- Name: ref_sampca_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_sampca_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_targst_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targst_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_workit_expety_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_expety_fk FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid) ON DELETE SET NULL;


--
-- Name: ref_workit_publen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_publen_fk FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid) ON DELETE CASCADE;


--
-- Name: ref_workit_status_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_workflowitem
    ADD CONSTRAINT ref_workit_status_fk FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid) ON DELETE SET NULL;


--
-- Name: sam_abstsa_hazaph_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa_hazaph_fk FOREIGN KEY (hazardphraseid) REFERENCES ref_hazardphrase(publicentryid) ON DELETE CASCADE;


--
-- Name: sam_abstsa_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstsa_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sam_abstsa_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa_otherrole_fk FOREIGN KEY (otherrole) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- Name: sam_abstsa_otherrole_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_abstsa_otherrole_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- Name: sam_abstsa_sampca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_abstsa_sampca_fk FOREIGN KEY (samplecategoryid) REFERENCES ref_samplecategory(publicentryid) ON DELETE CASCADE;


--
-- Name: sam_cryssa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT sam_cryssa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE CASCADE;


--
-- Name: sam_refsample_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsample
    ADD CONSTRAINT sam_refsample_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- Name: sam_refsaso_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sam_refsaso_refholder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_refholder_fk FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid) ON DELETE SET NULL;


--
-- Name: sam_refsaso_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: sam_refsaso_supplier_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsaso_supplier_fk FOREIGN KEY (supplierid) REFERENCES peop_organisation(labbookentryid) ON DELETE SET NULL;


--
-- Name: sam_sampco_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- Name: sam_sampco_container_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_container_fk FOREIGN KEY (containerid) REFERENCES sam_samplecomponent(labbookentryid) ON DELETE SET NULL;


--
-- Name: sam_sampco_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sam_sampco_refco_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_refco_fk FOREIGN KEY (refcomponentid) REFERENCES mole_abstractcomponent(labbookentryid) ON DELETE SET NULL;


--
-- Name: sam_sampco_reseobel_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT sam_sampco_reseobel_fk FOREIGN KEY (researchobjectivelementid) REFERENCES trag_researchobjectiveelement(labbookentryid) ON DELETE SET NULL;


--
-- Name: sam_sample_abstsa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_abstsa_fk FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid) ON DELETE CASCADE;


--
-- Name: sam_sample_assignto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_assignto_fk FOREIGN KEY (assigntoid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- Name: sam_sample_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE SET NULL;


--
-- Name: sam_sample_refsample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT sam_sample_refsample_fk FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: sam_triasa_condition_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_trialsample
    ADD CONSTRAINT sam_triasa_condition_fk FOREIGN KEY (conditionid) REFERENCES sam_refsample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: sam_triasa_protein_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_trialsample
    ADD CONSTRAINT sam_triasa_protein_fk FOREIGN KEY (proteinid) REFERENCES sam_sample(abstractsampleid) ON DELETE SET NULL;


--
-- Name: sam_triasa_sample_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_trialsample
    ADD CONSTRAINT sam_triasa_sample_fk FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid) ON DELETE CASCADE;


--
-- Name: sche_schepl_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_schepl_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sche_scheplof_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheplof_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sche_scheplof_schepl_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheplof_schepl_fk FOREIGN KEY (scheduleplanid) REFERENCES sche_scheduleplan(labbookentryid) ON DELETE CASCADE;


--
-- Name: sche_scheta_holder_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_holder_fk FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid) ON DELETE CASCADE;


--
-- Name: sche_scheta_instrument_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_instrument_fk FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid) ON DELETE SET NULL;


--
-- Name: sche_scheta_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: sche_scheta_scheplof_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheta_scheplof_fk FOREIGN KEY (scheduleplanoffsetid) REFERENCES sche_scheduleplanoffset(labbookentryid) ON DELETE SET NULL;


--
-- Name: targ_alias_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_alias_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT targ_alias_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_milestone_experiment_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_experiment_fk FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid) ON DELETE SET NULL;


--
-- Name: targ_milestone_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_milestone_status_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_status_fk FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid) ON DELETE SET NULL;


--
-- Name: targ_milestone_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT targ_milestone_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_project_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_project_project_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT targ_project_project_fk FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid) ON DELETE SET NULL;


--
-- Name: targ_project_targets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_project_targets_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_reseob_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_reseob_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_reseob_owner_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_reseob_owner_fk FOREIGN KEY (ownerid) REFERENCES peop_person(labbookentryid) ON DELETE SET NULL;


--
-- Name: targ_target_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_target_nuclac_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT targ_target_nuclac_fk FOREIGN KEY (nucleicacidid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- Name: targ_target_projects_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_target_projects_fk FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_target_protein_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_protein_fk FOREIGN KEY (proteinid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- Name: targ_target_species_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_species_fk FOREIGN KEY (speciesid) REFERENCES ref_organism(publicentryid) ON DELETE SET NULL;


--
-- Name: targ_target_targgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_target_targgr_fk FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_targgr_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targgr_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: targ_targgr_targets_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_targgr_targets_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE CASCADE;


--
-- Name: targ_targgr_targgr_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targgr_targgr_fk FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid) ON DELETE SET NULL;


--
-- Name: trag_reseobel_labboen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_labboen_fk FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid) ON DELETE CASCADE;


--
-- Name: trag_reseobel_molecule_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_molecule_fk FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE SET NULL;


--
-- Name: trag_reseobel_reseob_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_reseob_fk FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid) ON DELETE CASCADE;


--
-- Name: trag_reseobel_target_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_reseobel_target_fk FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid) ON DELETE SET NULL;


--
-- Name: trag_reseobel_triamo_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_molecule2relareobel
    ADD CONSTRAINT trag_reseobel_triamo_fk FOREIGN KEY (trialmoleculeid) REFERENCES mole_molecule(abstractcomponentid) ON DELETE CASCADE;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: acco_permission; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_permission FROM PUBLIC;
REVOKE ALL ON TABLE acco_permission FROM postgres;
GRANT ALL ON TABLE acco_permission TO postgres;
GRANT ALL ON TABLE acco_permission TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_permission TO pimsupdate;
GRANT SELECT ON TABLE acco_permission TO pimsview;


--
-- Name: acco_user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_user FROM PUBLIC;
REVOKE ALL ON TABLE acco_user FROM postgres;
GRANT ALL ON TABLE acco_user TO postgres;
GRANT ALL ON TABLE acco_user TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_user TO pimsupdate;
GRANT SELECT ON TABLE acco_user TO pimsview;


--
-- Name: acco_usergroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup FROM postgres;
GRANT ALL ON TABLE acco_usergroup TO postgres;
GRANT ALL ON TABLE acco_usergroup TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_usergroup TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup TO pimsview;


--
-- Name: acco_usergroup2leaders; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup2leaders FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup2leaders FROM postgres;
GRANT ALL ON TABLE acco_usergroup2leaders TO postgres;
GRANT ALL ON TABLE acco_usergroup2leaders TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_usergroup2leaders TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup2leaders TO pimsview;


--
-- Name: acco_usergroup2members; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup2members FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup2members FROM postgres;
GRANT ALL ON TABLE acco_usergroup2members TO postgres;
GRANT ALL ON TABLE acco_usergroup2members TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_usergroup2members TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup2members TO pimsview;


--
-- Name: core_accessobject; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_accessobject FROM PUBLIC;
REVOKE ALL ON TABLE core_accessobject FROM postgres;
GRANT ALL ON TABLE core_accessobject TO postgres;
GRANT ALL ON TABLE core_accessobject TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_accessobject TO pimsupdate;
GRANT SELECT ON TABLE core_accessobject TO pimsview;


--
-- Name: core_annotation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_annotation FROM PUBLIC;
REVOKE ALL ON TABLE core_annotation FROM postgres;
GRANT ALL ON TABLE core_annotation TO postgres;
GRANT ALL ON TABLE core_annotation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_annotation TO pimsupdate;
GRANT SELECT ON TABLE core_annotation TO pimsview;


--
-- Name: core_applicationdata; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_applicationdata FROM PUBLIC;
REVOKE ALL ON TABLE core_applicationdata FROM postgres;
GRANT ALL ON TABLE core_applicationdata TO postgres;
GRANT ALL ON TABLE core_applicationdata TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_applicationdata TO pimsupdate;
GRANT SELECT ON TABLE core_applicationdata TO pimsview;


--
-- Name: core_attachment; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_attachment FROM PUBLIC;
REVOKE ALL ON TABLE core_attachment FROM postgres;
GRANT ALL ON TABLE core_attachment TO postgres;
GRANT ALL ON TABLE core_attachment TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_attachment TO pimsupdate;
GRANT SELECT ON TABLE core_attachment TO pimsview;


--
-- Name: core_bookcitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_bookcitation FROM PUBLIC;
REVOKE ALL ON TABLE core_bookcitation FROM postgres;
GRANT ALL ON TABLE core_bookcitation TO postgres;
GRANT ALL ON TABLE core_bookcitation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_bookcitation TO pimsupdate;
GRANT SELECT ON TABLE core_bookcitation TO pimsview;


--
-- Name: core_citation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_citation FROM PUBLIC;
REVOKE ALL ON TABLE core_citation FROM postgres;
GRANT ALL ON TABLE core_citation TO postgres;
GRANT ALL ON TABLE core_citation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_citation TO pimsupdate;
GRANT SELECT ON TABLE core_citation TO pimsview;


--
-- Name: core_conferencecitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_conferencecitation FROM PUBLIC;
REVOKE ALL ON TABLE core_conferencecitation FROM postgres;
GRANT ALL ON TABLE core_conferencecitation TO postgres;
GRANT ALL ON TABLE core_conferencecitation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_conferencecitation TO pimsupdate;
GRANT SELECT ON TABLE core_conferencecitation TO pimsview;


--
-- Name: core_externaldblink; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_externaldblink FROM PUBLIC;
REVOKE ALL ON TABLE core_externaldblink FROM postgres;
GRANT ALL ON TABLE core_externaldblink TO postgres;
GRANT ALL ON TABLE core_externaldblink TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_externaldblink TO pimsupdate;
GRANT SELECT ON TABLE core_externaldblink TO pimsview;


--
-- Name: core_journalcitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_journalcitation FROM PUBLIC;
REVOKE ALL ON TABLE core_journalcitation FROM postgres;
GRANT ALL ON TABLE core_journalcitation TO postgres;
GRANT ALL ON TABLE core_journalcitation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_journalcitation TO pimsupdate;
GRANT SELECT ON TABLE core_journalcitation TO pimsview;


--
-- Name: core_labbookentry; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_labbookentry FROM PUBLIC;
REVOKE ALL ON TABLE core_labbookentry FROM postgres;
GRANT ALL ON TABLE core_labbookentry TO postgres;
GRANT ALL ON TABLE core_labbookentry TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_labbookentry TO pimsupdate;
GRANT SELECT ON TABLE core_labbookentry TO pimsview;


--
-- Name: core_note; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_note FROM PUBLIC;
REVOKE ALL ON TABLE core_note FROM postgres;
GRANT ALL ON TABLE core_note TO postgres;
GRANT ALL ON TABLE core_note TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_note TO pimsupdate;
GRANT SELECT ON TABLE core_note TO pimsview;


--
-- Name: core_note2relatedentrys; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_note2relatedentrys FROM PUBLIC;
REVOKE ALL ON TABLE core_note2relatedentrys FROM postgres;
GRANT ALL ON TABLE core_note2relatedentrys TO postgres;
GRANT ALL ON TABLE core_note2relatedentrys TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_note2relatedentrys TO pimsupdate;
GRANT SELECT ON TABLE core_note2relatedentrys TO pimsview;


--
-- Name: core_systemclass; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_systemclass FROM PUBLIC;
REVOKE ALL ON TABLE core_systemclass FROM postgres;
GRANT ALL ON TABLE core_systemclass TO postgres;
GRANT ALL ON TABLE core_systemclass TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_systemclass TO pimsupdate;
GRANT SELECT ON TABLE core_systemclass TO pimsview;


--
-- Name: core_thesiscitation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE core_thesiscitation FROM PUBLIC;
REVOKE ALL ON TABLE core_thesiscitation FROM postgres;
GRANT ALL ON TABLE core_thesiscitation TO postgres;
GRANT ALL ON TABLE core_thesiscitation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE core_thesiscitation TO pimsupdate;
GRANT SELECT ON TABLE core_thesiscitation TO pimsview;


--
-- Name: cryz_cypade_possva; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_cypade_possva FROM PUBLIC;
REVOKE ALL ON TABLE cryz_cypade_possva FROM postgres;
GRANT ALL ON TABLE cryz_cypade_possva TO postgres;
GRANT ALL ON TABLE cryz_cypade_possva TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_cypade_possva TO pimsupdate;
GRANT SELECT ON TABLE cryz_cypade_possva TO pimsview;


--
-- Name: cryz_dropannotation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_dropannotation FROM PUBLIC;
REVOKE ALL ON TABLE cryz_dropannotation FROM postgres;
GRANT ALL ON TABLE cryz_dropannotation TO postgres;
GRANT ALL ON TABLE cryz_dropannotation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_dropannotation TO pimsupdate;
GRANT SELECT ON TABLE cryz_dropannotation TO pimsview;


--
-- Name: cryz_image; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_image FROM PUBLIC;
REVOKE ALL ON TABLE cryz_image FROM postgres;
GRANT ALL ON TABLE cryz_image TO postgres;
GRANT ALL ON TABLE cryz_image TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_image TO pimsupdate;
GRANT SELECT ON TABLE cryz_image TO pimsview;


--
-- Name: cryz_parameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_parameter FROM PUBLIC;
REVOKE ALL ON TABLE cryz_parameter FROM postgres;
GRANT ALL ON TABLE cryz_parameter TO postgres;
GRANT ALL ON TABLE cryz_parameter TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_parameter TO pimsupdate;
GRANT SELECT ON TABLE cryz_parameter TO pimsview;


--
-- Name: cryz_parameterdefinition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_parameterdefinition FROM PUBLIC;
REVOKE ALL ON TABLE cryz_parameterdefinition FROM postgres;
GRANT ALL ON TABLE cryz_parameterdefinition TO postgres;
GRANT ALL ON TABLE cryz_parameterdefinition TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_parameterdefinition TO pimsupdate;
GRANT SELECT ON TABLE cryz_parameterdefinition TO pimsview;


--
-- Name: cryz_score; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_score FROM PUBLIC;
REVOKE ALL ON TABLE cryz_score FROM postgres;
GRANT ALL ON TABLE cryz_score TO postgres;
GRANT ALL ON TABLE cryz_score TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_score TO pimsupdate;
GRANT SELECT ON TABLE cryz_score TO pimsview;


--
-- Name: cryz_scoringscheme; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cryz_scoringscheme FROM PUBLIC;
REVOKE ALL ON TABLE cryz_scoringscheme FROM postgres;
GRANT ALL ON TABLE cryz_scoringscheme TO postgres;
GRANT ALL ON TABLE cryz_scoringscheme TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE cryz_scoringscheme TO pimsupdate;
GRANT SELECT ON TABLE cryz_scoringscheme TO pimsview;


--
-- Name: expe_experiment; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_experiment FROM PUBLIC;
REVOKE ALL ON TABLE expe_experiment FROM postgres;
GRANT ALL ON TABLE expe_experiment TO postgres;
GRANT ALL ON TABLE expe_experiment TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_experiment TO pimsupdate;
GRANT SELECT ON TABLE expe_experiment TO pimsview;


--
-- Name: expe_experimentgroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_experimentgroup FROM PUBLIC;
REVOKE ALL ON TABLE expe_experimentgroup FROM postgres;
GRANT ALL ON TABLE expe_experimentgroup TO postgres;
GRANT ALL ON TABLE expe_experimentgroup TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_experimentgroup TO pimsupdate;
GRANT SELECT ON TABLE expe_experimentgroup TO pimsview;


--
-- Name: expe_inputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_inputsample FROM PUBLIC;
REVOKE ALL ON TABLE expe_inputsample FROM postgres;
GRANT ALL ON TABLE expe_inputsample TO postgres;
GRANT ALL ON TABLE expe_inputsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_inputsample TO pimsupdate;
GRANT SELECT ON TABLE expe_inputsample TO pimsview;


--
-- Name: expe_instrument; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_instrument FROM PUBLIC;
REVOKE ALL ON TABLE expe_instrument FROM postgres;
GRANT ALL ON TABLE expe_instrument TO postgres;
GRANT ALL ON TABLE expe_instrument TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_instrument TO pimsupdate;
GRANT SELECT ON TABLE expe_instrument TO pimsview;


--
-- Name: expe_method; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_method FROM PUBLIC;
REVOKE ALL ON TABLE expe_method FROM postgres;
GRANT ALL ON TABLE expe_method TO postgres;
GRANT ALL ON TABLE expe_method TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_method TO pimsupdate;
GRANT SELECT ON TABLE expe_method TO pimsview;


--
-- Name: expe_methodparameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_methodparameter FROM PUBLIC;
REVOKE ALL ON TABLE expe_methodparameter FROM postgres;
GRANT ALL ON TABLE expe_methodparameter TO postgres;
GRANT ALL ON TABLE expe_methodparameter TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_methodparameter TO pimsupdate;
GRANT SELECT ON TABLE expe_methodparameter TO pimsview;


--
-- Name: expe_outputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_outputsample FROM PUBLIC;
REVOKE ALL ON TABLE expe_outputsample FROM postgres;
GRANT ALL ON TABLE expe_outputsample TO postgres;
GRANT ALL ON TABLE expe_outputsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_outputsample TO pimsupdate;
GRANT SELECT ON TABLE expe_outputsample TO pimsview;


--
-- Name: expe_parameter; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_parameter FROM PUBLIC;
REVOKE ALL ON TABLE expe_parameter FROM postgres;
GRANT ALL ON TABLE expe_parameter TO postgres;
GRANT ALL ON TABLE expe_parameter TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_parameter TO pimsupdate;
GRANT SELECT ON TABLE expe_parameter TO pimsview;


--
-- Name: expe_software; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_software FROM PUBLIC;
REVOKE ALL ON TABLE expe_software FROM postgres;
GRANT ALL ON TABLE expe_software TO postgres;
GRANT ALL ON TABLE expe_software TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_software TO pimsupdate;
GRANT SELECT ON TABLE expe_software TO pimsview;


--
-- Name: expe_software_tasks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_software_tasks FROM PUBLIC;
REVOKE ALL ON TABLE expe_software_tasks FROM postgres;
GRANT ALL ON TABLE expe_software_tasks TO postgres;
GRANT ALL ON TABLE expe_software_tasks TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_software_tasks TO pimsupdate;
GRANT SELECT ON TABLE expe_software_tasks TO pimsview;


--
-- Name: generic_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE generic_target FROM PUBLIC;
REVOKE ALL ON TABLE generic_target FROM postgres;
GRANT ALL ON TABLE generic_target TO postgres;
GRANT ALL ON TABLE generic_target TO pimsadmin;
GRANT SELECT ON TABLE generic_target TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE generic_target TO pimsupdate;


--
-- Name: hibernate_sequence; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hibernate_sequence FROM PUBLIC;
REVOKE ALL ON TABLE hibernate_sequence FROM postgres;
GRANT ALL ON TABLE hibernate_sequence TO postgres;
GRANT ALL ON TABLE hibernate_sequence TO pimsadmin;
GRANT SELECT ON TABLE hibernate_sequence TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hibernate_sequence TO pimsupdate;


--
-- Name: hold_abstractholder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_abstractholder FROM PUBLIC;
REVOKE ALL ON TABLE hold_abstractholder FROM postgres;
GRANT ALL ON TABLE hold_abstractholder TO postgres;
GRANT ALL ON TABLE hold_abstractholder TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_abstractholder TO pimsupdate;
GRANT SELECT ON TABLE hold_abstractholder TO pimsview;


--
-- Name: hold_crystalnumber; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_crystalnumber FROM PUBLIC;
REVOKE ALL ON TABLE hold_crystalnumber FROM postgres;
GRANT ALL ON TABLE hold_crystalnumber TO postgres;
GRANT ALL ON TABLE hold_crystalnumber TO pimsadmin;
GRANT SELECT ON TABLE hold_crystalnumber TO pimsupdate;
GRANT SELECT ON TABLE hold_crystalnumber TO pimsview;


--
-- Name: sam_sample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_sample FROM PUBLIC;
REVOKE ALL ON TABLE sam_sample FROM postgres;
GRANT ALL ON TABLE sam_sample TO postgres;
GRANT ALL ON TABLE sam_sample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_sample TO pimsupdate;
GRANT SELECT ON TABLE sam_sample TO pimsview;


--
-- Name: hold_firstsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_firstsample FROM PUBLIC;
REVOKE ALL ON TABLE hold_firstsample FROM postgres;
GRANT ALL ON TABLE hold_firstsample TO postgres;
GRANT ALL ON TABLE hold_firstsample TO pimsadmin;
GRANT SELECT ON TABLE hold_firstsample TO pimsupdate;
GRANT SELECT ON TABLE hold_firstsample TO pimsview;


--
-- Name: hold_holdca2abstholders; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdca2abstholders FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdca2abstholders FROM postgres;
GRANT ALL ON TABLE hold_holdca2abstholders TO postgres;
GRANT ALL ON TABLE hold_holdca2abstholders TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_holdca2abstholders TO pimsupdate;
GRANT SELECT ON TABLE hold_holdca2abstholders TO pimsview;


--
-- Name: hold_holdca2absthoty; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdca2absthoty FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdca2absthoty FROM postgres;
GRANT ALL ON TABLE hold_holdca2absthoty TO postgres;
GRANT ALL ON TABLE hold_holdca2absthoty TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_holdca2absthoty TO pimsupdate;
GRANT SELECT ON TABLE hold_holdca2absthoty TO pimsview;


--
-- Name: hold_holder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holder FROM PUBLIC;
REVOKE ALL ON TABLE hold_holder FROM postgres;
GRANT ALL ON TABLE hold_holder TO postgres;
GRANT ALL ON TABLE hold_holder TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_holder TO pimsupdate;
GRANT SELECT ON TABLE hold_holder TO pimsview;


--
-- Name: hold_holderlocation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holderlocation FROM PUBLIC;
REVOKE ALL ON TABLE hold_holderlocation FROM postgres;
GRANT ALL ON TABLE hold_holderlocation TO postgres;
GRANT ALL ON TABLE hold_holderlocation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_holderlocation TO pimsupdate;
GRANT SELECT ON TABLE hold_holderlocation TO pimsview;


--
-- Name: hold_holdertypeposition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_holdertypeposition FROM PUBLIC;
REVOKE ALL ON TABLE hold_holdertypeposition FROM postgres;
GRANT ALL ON TABLE hold_holdertypeposition TO postgres;
GRANT ALL ON TABLE hold_holdertypeposition TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_holdertypeposition TO pimsupdate;
GRANT SELECT ON TABLE hold_holdertypeposition TO pimsview;


--
-- Name: sche_scheduledtask; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduledtask FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduledtask FROM postgres;
GRANT ALL ON TABLE sche_scheduledtask TO postgres;
GRANT ALL ON TABLE sche_scheduledtask TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sche_scheduledtask TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduledtask TO pimsview;


--
-- Name: hold_lastinspection; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_lastinspection FROM PUBLIC;
REVOKE ALL ON TABLE hold_lastinspection FROM postgres;
GRANT ALL ON TABLE hold_lastinspection TO postgres;
GRANT ALL ON TABLE hold_lastinspection TO pimsadmin;
GRANT SELECT ON TABLE hold_lastinspection TO pimsupdate;
GRANT SELECT ON TABLE hold_lastinspection TO pimsview;


--
-- Name: hold_refholder; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refholder FROM PUBLIC;
REVOKE ALL ON TABLE hold_refholder FROM postgres;
GRANT ALL ON TABLE hold_refholder TO postgres;
GRANT ALL ON TABLE hold_refholder TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_refholder TO pimsupdate;
GRANT SELECT ON TABLE hold_refholder TO pimsview;


--
-- Name: hold_refholderoffset; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refholderoffset FROM PUBLIC;
REVOKE ALL ON TABLE hold_refholderoffset FROM postgres;
GRANT ALL ON TABLE hold_refholderoffset TO postgres;
GRANT ALL ON TABLE hold_refholderoffset TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_refholderoffset TO pimsupdate;
GRANT SELECT ON TABLE hold_refholderoffset TO pimsview;


--
-- Name: hold_refsampleposition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hold_refsampleposition FROM PUBLIC;
REVOKE ALL ON TABLE hold_refsampleposition FROM postgres;
GRANT ALL ON TABLE hold_refsampleposition TO postgres;
GRANT ALL ON TABLE hold_refsampleposition TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE hold_refsampleposition TO pimsupdate;
GRANT SELECT ON TABLE hold_refsampleposition TO pimsview;


--
-- Name: inst_instty2inst; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE inst_instty2inst FROM PUBLIC;
REVOKE ALL ON TABLE inst_instty2inst FROM postgres;
GRANT ALL ON TABLE inst_instty2inst TO postgres;
GRANT ALL ON TABLE inst_instty2inst TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE inst_instty2inst TO pimsupdate;
GRANT SELECT ON TABLE inst_instty2inst TO pimsview;


--
-- Name: loca_location; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE loca_location FROM PUBLIC;
REVOKE ALL ON TABLE loca_location FROM postgres;
GRANT ALL ON TABLE loca_location TO postgres;
GRANT ALL ON TABLE loca_location TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE loca_location TO pimsupdate;
GRANT SELECT ON TABLE loca_location TO pimsview;


--
-- Name: mole_abstco_keywords; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstco_keywords FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstco_keywords FROM postgres;
GRANT ALL ON TABLE mole_abstco_keywords TO postgres;
GRANT ALL ON TABLE mole_abstco_keywords TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_abstco_keywords TO pimsupdate;
GRANT SELECT ON TABLE mole_abstco_keywords TO pimsview;


--
-- Name: mole_abstco_synonyms; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstco_synonyms FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstco_synonyms FROM postgres;
GRANT ALL ON TABLE mole_abstco_synonyms TO postgres;
GRANT ALL ON TABLE mole_abstco_synonyms TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_abstco_synonyms TO pimsupdate;
GRANT SELECT ON TABLE mole_abstco_synonyms TO pimsview;


--
-- Name: mole_abstractcomponent; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_abstractcomponent FROM PUBLIC;
REVOKE ALL ON TABLE mole_abstractcomponent FROM postgres;
GRANT ALL ON TABLE mole_abstractcomponent TO postgres;
GRANT ALL ON TABLE mole_abstractcomponent TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_abstractcomponent TO pimsupdate;
GRANT SELECT ON TABLE mole_abstractcomponent TO pimsview;


--
-- Name: mole_compca2components; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_compca2components FROM PUBLIC;
REVOKE ALL ON TABLE mole_compca2components FROM postgres;
GRANT ALL ON TABLE mole_compca2components TO postgres;
GRANT ALL ON TABLE mole_compca2components TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_compca2components TO pimsupdate;
GRANT SELECT ON TABLE mole_compca2components TO pimsview;


--
-- Name: mole_construct; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_construct FROM PUBLIC;
REVOKE ALL ON TABLE mole_construct FROM postgres;
GRANT ALL ON TABLE mole_construct TO postgres;
GRANT ALL ON TABLE mole_construct TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_construct TO pimsupdate;
GRANT SELECT ON TABLE mole_construct TO pimsview;


--
-- Name: mole_molecule; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_molecule FROM PUBLIC;
REVOKE ALL ON TABLE mole_molecule FROM postgres;
GRANT ALL ON TABLE mole_molecule TO postgres;
GRANT ALL ON TABLE mole_molecule TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_molecule TO pimsupdate;
GRANT SELECT ON TABLE mole_molecule TO pimsview;


--
-- Name: mole_molecule2relareobel; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_molecule2relareobel FROM PUBLIC;
REVOKE ALL ON TABLE mole_molecule2relareobel FROM postgres;
GRANT ALL ON TABLE mole_molecule2relareobel TO postgres;
GRANT ALL ON TABLE mole_molecule2relareobel TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_molecule2relareobel TO pimsupdate;
GRANT SELECT ON TABLE mole_molecule2relareobel TO pimsview;


--
-- Name: mole_moleculefeature; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_moleculefeature FROM PUBLIC;
REVOKE ALL ON TABLE mole_moleculefeature FROM postgres;
GRANT ALL ON TABLE mole_moleculefeature TO postgres;
GRANT ALL ON TABLE mole_moleculefeature TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_moleculefeature TO pimsupdate;
GRANT SELECT ON TABLE mole_moleculefeature TO pimsview;


--
-- Name: mole_primer; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_primer FROM PUBLIC;
REVOKE ALL ON TABLE mole_primer FROM postgres;
GRANT ALL ON TABLE mole_primer TO postgres;
GRANT ALL ON TABLE mole_primer TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_primer TO pimsupdate;
GRANT SELECT ON TABLE mole_primer TO pimsview;


--
-- Name: peop_group; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_group FROM PUBLIC;
REVOKE ALL ON TABLE peop_group FROM postgres;
GRANT ALL ON TABLE peop_group TO postgres;
GRANT ALL ON TABLE peop_group TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_group TO pimsupdate;
GRANT SELECT ON TABLE peop_group TO pimsview;


--
-- Name: peop_orga_addresses; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_orga_addresses FROM PUBLIC;
REVOKE ALL ON TABLE peop_orga_addresses FROM postgres;
GRANT ALL ON TABLE peop_orga_addresses TO postgres;
GRANT ALL ON TABLE peop_orga_addresses TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_orga_addresses TO pimsupdate;
GRANT SELECT ON TABLE peop_orga_addresses TO pimsview;


--
-- Name: peop_organisation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_organisation FROM PUBLIC;
REVOKE ALL ON TABLE peop_organisation FROM postgres;
GRANT ALL ON TABLE peop_organisation TO postgres;
GRANT ALL ON TABLE peop_organisation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_organisation TO pimsupdate;
GRANT SELECT ON TABLE peop_organisation TO pimsview;


--
-- Name: peop_persingr_phonnu; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_persingr_phonnu FROM PUBLIC;
REVOKE ALL ON TABLE peop_persingr_phonnu FROM postgres;
GRANT ALL ON TABLE peop_persingr_phonnu TO postgres;
GRANT ALL ON TABLE peop_persingr_phonnu TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_persingr_phonnu TO pimsupdate;
GRANT SELECT ON TABLE peop_persingr_phonnu TO pimsview;


--
-- Name: peop_person; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_person FROM PUBLIC;
REVOKE ALL ON TABLE peop_person FROM postgres;
GRANT ALL ON TABLE peop_person TO postgres;
GRANT ALL ON TABLE peop_person TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_person TO pimsupdate;
GRANT SELECT ON TABLE peop_person TO pimsview;


--
-- Name: peop_person_middin; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_person_middin FROM PUBLIC;
REVOKE ALL ON TABLE peop_person_middin FROM postgres;
GRANT ALL ON TABLE peop_person_middin TO postgres;
GRANT ALL ON TABLE peop_person_middin TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_person_middin TO pimsupdate;
GRANT SELECT ON TABLE peop_person_middin TO pimsview;


--
-- Name: peop_personingroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE peop_personingroup FROM PUBLIC;
REVOKE ALL ON TABLE peop_personingroup FROM postgres;
GRANT ALL ON TABLE peop_personingroup TO postgres;
GRANT ALL ON TABLE peop_personingroup TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE peop_personingroup TO pimsupdate;
GRANT SELECT ON TABLE peop_personingroup TO pimsview;


--
-- Name: prot_parade_possva; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_parade_possva FROM PUBLIC;
REVOKE ALL ON TABLE prot_parade_possva FROM postgres;
GRANT ALL ON TABLE prot_parade_possva TO postgres;
GRANT ALL ON TABLE prot_parade_possva TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_parade_possva TO pimsupdate;
GRANT SELECT ON TABLE prot_parade_possva TO pimsview;


--
-- Name: prot_parameterdefinition; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_parameterdefinition FROM PUBLIC;
REVOKE ALL ON TABLE prot_parameterdefinition FROM postgres;
GRANT ALL ON TABLE prot_parameterdefinition TO postgres;
GRANT ALL ON TABLE prot_parameterdefinition TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_parameterdefinition TO pimsupdate;
GRANT SELECT ON TABLE prot_parameterdefinition TO pimsview;


--
-- Name: prot_protocol; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_protocol FROM PUBLIC;
REVOKE ALL ON TABLE prot_protocol FROM postgres;
GRANT ALL ON TABLE prot_protocol TO postgres;
GRANT ALL ON TABLE prot_protocol TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_protocol TO pimsupdate;
GRANT SELECT ON TABLE prot_protocol TO pimsview;


--
-- Name: prot_protocol_remarks; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_protocol_remarks FROM PUBLIC;
REVOKE ALL ON TABLE prot_protocol_remarks FROM postgres;
GRANT ALL ON TABLE prot_protocol_remarks TO postgres;
GRANT ALL ON TABLE prot_protocol_remarks TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_protocol_remarks TO pimsupdate;
GRANT SELECT ON TABLE prot_protocol_remarks TO pimsview;


--
-- Name: prot_refinputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_refinputsample FROM PUBLIC;
REVOKE ALL ON TABLE prot_refinputsample FROM postgres;
GRANT ALL ON TABLE prot_refinputsample TO postgres;
GRANT ALL ON TABLE prot_refinputsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_refinputsample TO pimsupdate;
GRANT SELECT ON TABLE prot_refinputsample TO pimsview;


--
-- Name: prot_refoutputsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE prot_refoutputsample FROM PUBLIC;
REVOKE ALL ON TABLE prot_refoutputsample FROM postgres;
GRANT ALL ON TABLE prot_refoutputsample TO postgres;
GRANT ALL ON TABLE prot_refoutputsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE prot_refoutputsample TO pimsupdate;
GRANT SELECT ON TABLE prot_refoutputsample TO pimsview;


--
-- Name: ref_abstractholdertype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_abstractholdertype FROM PUBLIC;
REVOKE ALL ON TABLE ref_abstractholdertype FROM postgres;
GRANT ALL ON TABLE ref_abstractholdertype TO postgres;
GRANT ALL ON TABLE ref_abstractholdertype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_abstractholdertype TO pimsupdate;
GRANT SELECT ON TABLE ref_abstractholdertype TO pimsview;


--
-- Name: ref_componentcategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_componentcategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_componentcategory FROM postgres;
GRANT ALL ON TABLE ref_componentcategory TO postgres;
GRANT ALL ON TABLE ref_componentcategory TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_componentcategory TO pimsupdate;
GRANT SELECT ON TABLE ref_componentcategory TO pimsview;


--
-- Name: ref_crystaltype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_crystaltype FROM PUBLIC;
REVOKE ALL ON TABLE ref_crystaltype FROM postgres;
GRANT ALL ON TABLE ref_crystaltype TO postgres;
GRANT ALL ON TABLE ref_crystaltype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_crystaltype TO pimsupdate;
GRANT SELECT ON TABLE ref_crystaltype TO pimsview;


--
-- Name: ref_dbname; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_dbname FROM PUBLIC;
REVOKE ALL ON TABLE ref_dbname FROM postgres;
GRANT ALL ON TABLE ref_dbname TO postgres;
GRANT ALL ON TABLE ref_dbname TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_dbname TO pimsupdate;
GRANT SELECT ON TABLE ref_dbname TO pimsview;


--
-- Name: ref_experimenttype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_experimenttype FROM PUBLIC;
REVOKE ALL ON TABLE ref_experimenttype FROM postgres;
GRANT ALL ON TABLE ref_experimenttype TO postgres;
GRANT ALL ON TABLE ref_experimenttype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_experimenttype TO pimsupdate;
GRANT SELECT ON TABLE ref_experimenttype TO pimsview;


--
-- Name: ref_hazardphrase; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_hazardphrase FROM PUBLIC;
REVOKE ALL ON TABLE ref_hazardphrase FROM postgres;
GRANT ALL ON TABLE ref_hazardphrase TO postgres;
GRANT ALL ON TABLE ref_hazardphrase TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_hazardphrase TO pimsupdate;
GRANT SELECT ON TABLE ref_hazardphrase TO pimsview;


--
-- Name: ref_holdercategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_holdercategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_holdercategory FROM postgres;
GRANT ALL ON TABLE ref_holdercategory TO postgres;
GRANT ALL ON TABLE ref_holdercategory TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_holdercategory TO pimsupdate;
GRANT SELECT ON TABLE ref_holdercategory TO pimsview;


--
-- Name: ref_holdertype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_holdertype FROM PUBLIC;
REVOKE ALL ON TABLE ref_holdertype FROM postgres;
GRANT ALL ON TABLE ref_holdertype TO postgres;
GRANT ALL ON TABLE ref_holdertype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_holdertype TO pimsupdate;
GRANT SELECT ON TABLE ref_holdertype TO pimsview;


--
-- Name: ref_imagetype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_imagetype FROM PUBLIC;
REVOKE ALL ON TABLE ref_imagetype FROM postgres;
GRANT ALL ON TABLE ref_imagetype TO postgres;
GRANT SELECT ON TABLE ref_imagetype TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_imagetype TO pimsupdate;
GRANT ALL ON TABLE ref_imagetype TO pimsadmin;


--
-- Name: ref_instrumenttype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_instrumenttype FROM PUBLIC;
REVOKE ALL ON TABLE ref_instrumenttype FROM postgres;
GRANT ALL ON TABLE ref_instrumenttype TO postgres;
GRANT ALL ON TABLE ref_instrumenttype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_instrumenttype TO pimsupdate;
GRANT SELECT ON TABLE ref_instrumenttype TO pimsview;


--
-- Name: ref_organism; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_organism FROM PUBLIC;
REVOKE ALL ON TABLE ref_organism FROM postgres;
GRANT ALL ON TABLE ref_organism TO postgres;
GRANT ALL ON TABLE ref_organism TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_organism TO pimsupdate;
GRANT SELECT ON TABLE ref_organism TO pimsview;


--
-- Name: ref_pintype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_pintype FROM PUBLIC;
REVOKE ALL ON TABLE ref_pintype FROM postgres;
GRANT ALL ON TABLE ref_pintype TO postgres;
GRANT ALL ON TABLE ref_pintype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_pintype TO pimsupdate;
GRANT SELECT ON TABLE ref_pintype TO pimsview;


--
-- Name: ref_publicentry; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_publicentry FROM PUBLIC;
REVOKE ALL ON TABLE ref_publicentry FROM postgres;
GRANT ALL ON TABLE ref_publicentry TO postgres;
GRANT ALL ON TABLE ref_publicentry TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_publicentry TO pimsupdate;
GRANT SELECT ON TABLE ref_publicentry TO pimsview;


--
-- Name: ref_samplecategory; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_samplecategory FROM PUBLIC;
REVOKE ALL ON TABLE ref_samplecategory FROM postgres;
GRANT ALL ON TABLE ref_samplecategory TO postgres;
GRANT ALL ON TABLE ref_samplecategory TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_samplecategory TO pimsupdate;
GRANT SELECT ON TABLE ref_samplecategory TO pimsview;


--
-- Name: ref_targetstatus; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_targetstatus FROM PUBLIC;
REVOKE ALL ON TABLE ref_targetstatus FROM postgres;
GRANT ALL ON TABLE ref_targetstatus TO postgres;
GRANT ALL ON TABLE ref_targetstatus TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_targetstatus TO pimsupdate;
GRANT SELECT ON TABLE ref_targetstatus TO pimsview;


--
-- Name: ref_workflowitem; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_workflowitem FROM PUBLIC;
REVOKE ALL ON TABLE ref_workflowitem FROM postgres;
GRANT ALL ON TABLE ref_workflowitem TO postgres;
GRANT ALL ON TABLE ref_workflowitem TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_workflowitem TO pimsupdate;
GRANT SELECT ON TABLE ref_workflowitem TO pimsview;


--
-- Name: revisionnumber; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE revisionnumber FROM PUBLIC;
REVOKE ALL ON TABLE revisionnumber FROM postgres;
GRANT ALL ON TABLE revisionnumber TO postgres;
GRANT ALL ON TABLE revisionnumber TO pimsadmin;
GRANT SELECT ON TABLE revisionnumber TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE revisionnumber TO pimsupdate;


--
-- Name: sam_abstractsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_abstractsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_abstractsample FROM postgres;
GRANT ALL ON TABLE sam_abstractsample TO postgres;
GRANT ALL ON TABLE sam_abstractsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_abstractsample TO pimsupdate;
GRANT SELECT ON TABLE sam_abstractsample TO pimsview;


--
-- Name: sam_abstsa2hazaph; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_abstsa2hazaph FROM PUBLIC;
REVOKE ALL ON TABLE sam_abstsa2hazaph FROM postgres;
GRANT ALL ON TABLE sam_abstsa2hazaph TO postgres;
GRANT ALL ON TABLE sam_abstsa2hazaph TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_abstsa2hazaph TO pimsupdate;
GRANT SELECT ON TABLE sam_abstsa2hazaph TO pimsview;


--
-- Name: sam_crystalsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_crystalsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_crystalsample FROM postgres;
GRANT ALL ON TABLE sam_crystalsample TO postgres;
GRANT ALL ON TABLE sam_crystalsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_crystalsample TO pimsupdate;
GRANT SELECT ON TABLE sam_crystalsample TO pimsview;


--
-- Name: sam_refsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_refsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_refsample FROM postgres;
GRANT ALL ON TABLE sam_refsample TO postgres;
GRANT ALL ON TABLE sam_refsample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_refsample TO pimsupdate;
GRANT SELECT ON TABLE sam_refsample TO pimsview;


--
-- Name: sam_refsamplesource; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_refsamplesource FROM PUBLIC;
REVOKE ALL ON TABLE sam_refsamplesource FROM postgres;
GRANT ALL ON TABLE sam_refsamplesource TO postgres;
GRANT ALL ON TABLE sam_refsamplesource TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_refsamplesource TO pimsupdate;
GRANT SELECT ON TABLE sam_refsamplesource TO pimsview;


--
-- Name: sam_sampca2abstsa; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_sampca2abstsa FROM PUBLIC;
REVOKE ALL ON TABLE sam_sampca2abstsa FROM postgres;
GRANT ALL ON TABLE sam_sampca2abstsa TO postgres;
GRANT ALL ON TABLE sam_sampca2abstsa TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_sampca2abstsa TO pimsupdate;
GRANT SELECT ON TABLE sam_sampca2abstsa TO pimsview;


--
-- Name: sam_samplecomponent; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_samplecomponent FROM PUBLIC;
REVOKE ALL ON TABLE sam_samplecomponent FROM postgres;
GRANT ALL ON TABLE sam_samplecomponent TO postgres;
GRANT ALL ON TABLE sam_samplecomponent TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_samplecomponent TO pimsupdate;
GRANT SELECT ON TABLE sam_samplecomponent TO pimsview;


--
-- Name: sam_trialsample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_trialsample FROM PUBLIC;
REVOKE ALL ON TABLE sam_trialsample FROM postgres;
GRANT ALL ON TABLE sam_trialsample TO postgres;
GRANT SELECT ON TABLE sam_trialsample TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_trialsample TO pimsupdate;
GRANT ALL ON TABLE sam_trialsample TO pimsadmin;


--
-- Name: sche_scheduleplan; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduleplan FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduleplan FROM postgres;
GRANT ALL ON TABLE sche_scheduleplan TO postgres;
GRANT ALL ON TABLE sche_scheduleplan TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sche_scheduleplan TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduleplan TO pimsview;


--
-- Name: sche_scheduleplanoffset; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduleplanoffset FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduleplanoffset FROM postgres;
GRANT ALL ON TABLE sche_scheduleplanoffset TO postgres;
GRANT ALL ON TABLE sche_scheduleplanoffset TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sche_scheduleplanoffset TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduleplanoffset TO pimsview;


--
-- Name: targ_alias; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_alias FROM PUBLIC;
REVOKE ALL ON TABLE targ_alias FROM postgres;
GRANT ALL ON TABLE targ_alias TO postgres;
GRANT SELECT ON TABLE targ_alias TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_alias TO pimsupdate;
GRANT ALL ON TABLE targ_alias TO pimsadmin;


--
-- Name: targ_milestone; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_milestone FROM PUBLIC;
REVOKE ALL ON TABLE targ_milestone FROM postgres;
GRANT ALL ON TABLE targ_milestone TO postgres;
GRANT ALL ON TABLE targ_milestone TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_milestone TO pimsupdate;
GRANT SELECT ON TABLE targ_milestone TO pimsview;


--
-- Name: targ_project; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_project FROM PUBLIC;
REVOKE ALL ON TABLE targ_project FROM postgres;
GRANT ALL ON TABLE targ_project TO postgres;
GRANT ALL ON TABLE targ_project TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_project TO pimsupdate;
GRANT SELECT ON TABLE targ_project TO pimsview;


--
-- Name: targ_researchobjective; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_researchobjective FROM PUBLIC;
REVOKE ALL ON TABLE targ_researchobjective FROM postgres;
GRANT ALL ON TABLE targ_researchobjective TO postgres;
GRANT ALL ON TABLE targ_researchobjective TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_researchobjective TO pimsupdate;
GRANT SELECT ON TABLE targ_researchobjective TO pimsview;


--
-- Name: targ_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target FROM PUBLIC;
REVOKE ALL ON TABLE targ_target FROM postgres;
GRANT ALL ON TABLE targ_target TO postgres;
GRANT ALL ON TABLE targ_target TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_target TO pimsupdate;
GRANT SELECT ON TABLE targ_target TO pimsview;


--
-- Name: targ_target2nuclac; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target2nuclac FROM PUBLIC;
REVOKE ALL ON TABLE targ_target2nuclac FROM postgres;
GRANT ALL ON TABLE targ_target2nuclac TO postgres;
GRANT ALL ON TABLE targ_target2nuclac TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_target2nuclac TO pimsupdate;
GRANT SELECT ON TABLE targ_target2nuclac TO pimsview;


--
-- Name: targ_target2projects; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_target2projects FROM PUBLIC;
REVOKE ALL ON TABLE targ_target2projects FROM postgres;
GRANT ALL ON TABLE targ_target2projects TO postgres;
GRANT ALL ON TABLE targ_target2projects TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_target2projects TO pimsupdate;
GRANT SELECT ON TABLE targ_target2projects TO pimsview;


--
-- Name: targ_targetgroup; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_targetgroup FROM PUBLIC;
REVOKE ALL ON TABLE targ_targetgroup FROM postgres;
GRANT ALL ON TABLE targ_targetgroup TO postgres;
GRANT ALL ON TABLE targ_targetgroup TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_targetgroup TO pimsupdate;
GRANT SELECT ON TABLE targ_targetgroup TO pimsview;


--
-- Name: targ_targgr2targets; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_targgr2targets FROM PUBLIC;
REVOKE ALL ON TABLE targ_targgr2targets FROM postgres;
GRANT ALL ON TABLE targ_targgr2targets TO postgres;
GRANT ALL ON TABLE targ_targgr2targets TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_targgr2targets TO pimsupdate;
GRANT SELECT ON TABLE targ_targgr2targets TO pimsview;


--
-- Name: test_target; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE test_target FROM PUBLIC;
REVOKE ALL ON TABLE test_target FROM postgres;
GRANT ALL ON TABLE test_target TO postgres;
GRANT ALL ON TABLE test_target TO pimsadmin;
GRANT SELECT ON TABLE test_target TO pimsview;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE test_target TO pimsupdate;


--
-- Name: trag_researchobjectiveelement; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE trag_researchobjectiveelement FROM PUBLIC;
REVOKE ALL ON TABLE trag_researchobjectiveelement FROM postgres;
GRANT ALL ON TABLE trag_researchobjectiveelement TO postgres;
GRANT ALL ON TABLE trag_researchobjectiveelement TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE trag_researchobjectiveelement TO pimsupdate;
GRANT SELECT ON TABLE trag_researchobjectiveelement TO pimsview;


--
-- PostgreSQL database dump complete
--

