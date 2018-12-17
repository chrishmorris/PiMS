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
-- Name: acco_usergroup2user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE acco_usergroup2user (
    usergroupid bigint NOT NULL,
    memberuserid bigint NOT NULL
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
    dbid bigint NOT NULL,
    date timestamp with time zone,
    details text,
    authorid bigint,
    parententryid bigint NOT NULL
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
    dbid bigint NOT NULL,
    creationdate timestamp with time zone,
    details text,
    lastediteddate timestamp with time zone,
    accessid bigint,
    creatorid bigint,
    lasteditorid bigint
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
    holderid bigint,
    imageid bigint,
    sampleid bigint,
    scoreid bigint NOT NULL,
    softwareid bigint
);


--
-- Name: cryz_image; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cryz_image (
    filename character varying(254) NOT NULL,
    filepath character varying(254) NOT NULL,
    mimetype character varying(80),
    labbookentryid bigint NOT NULL,
    imagetypeid bigint,
    instrumentid bigint,
    sampleid bigint,
    scheduledtaskid bigint
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
    experimentgroupid bigint,
    experimenttypeid bigint NOT NULL,
    groupid bigint,
    instrumentid bigint,
    methodid bigint,
    operatorid bigint,
    protocolid bigint,
    researchobjectiveid bigint
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
    sampleid bigint,
    order_ integer
);


--
-- Name: expe_instrument; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_instrument (
    model character varying(80),
    name character varying(80) NOT NULL,
    pressure double precision,
    pressuredisplayunit character varying(32),
    serialnumber character varying(80),
    tempdisplayunit character varying(32),
    temperature double precision,
    labbookentryid bigint NOT NULL,
    defaultimagetypeid bigint,
    locationid bigint,
    manufacturerid bigint
);


--
-- Name: expe_instrument2insttype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE expe_instrument2insttype (
    instrumentid bigint NOT NULL,
    instrumenttypeid bigint NOT NULL
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

SELECT pg_catalog.setval('hibernate_sequence', 40000, true);


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
-- Name: hold_holdca2abstholders; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hold_holdca2abstholders (
    abstholderid bigint NOT NULL,
    holdercategoryid bigint NOT NULL
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
    crystalnumber integer,
    enddate timestamp with time zone,
    startdate timestamp with time zone,
    abstractholderid bigint NOT NULL,
    firstsampleid bigint,
    lasttaskid bigint
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
    refholderid bigint NOT NULL,
    refsampleid bigint
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
    componentid bigint NOT NULL,
    categoryid bigint NOT NULL
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
-- Name: mole_extension; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_extension (
    extensiontype character varying(32) NOT NULL,
    isforuse boolean,
    relatedproteintagseq text,
    restrictionenzyme character varying(254),
    moleculeid bigint NOT NULL
);


--
-- Name: mole_host; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_host (
    antibioticresistances character varying(254),
    comments text,
    genotype character varying(254),
    harbouredplasmids character varying(254),
    selectablemarkers character varying(254),
    strain character varying(80),
    use character varying(254),
    abstractcomponentid bigint NOT NULL
);


--
-- Name: mole_host2organisation; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_host2organisation (
    hostid bigint NOT NULL,
    organisationid bigint NOT NULL
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
    relatedresearchobjectiveelementid bigint NOT NULL,
    trialmoleculeid bigint NOT NULL
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
    moleculeid bigint NOT NULL,
    refmoleculeid bigint
);


--
-- Name: mole_primer; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE mole_primer (
    direction character varying(32) NOT NULL,
    gcgene character varying(80),
    isuniversal boolean NOT NULL,
    lengthongene integer,
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
-- Name: ref_holdertypesource; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_holdertypesource (
    catalognum character varying(80) NOT NULL,
    datapageurl character varying(254),
    productname character varying(254),
    publicentryid bigint NOT NULL,
    holdertypeid bigint NOT NULL,
    supplierid bigint NOT NULL
);


--
-- Name: ref_imagetype; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ref_imagetype (
    catorgory character varying(180),
    colourdepth integer,
    name character varying(80) NOT NULL,
    sizex integer,
    sizey integer,
    url character varying(180),
    xlengthperpixel double precision,
    ylengthperpixel double precision,
    publicentryid bigint NOT NULL
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
    issaltcrystal boolean,
    abstractsampleid bigint NOT NULL
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
    refholderid bigint,
    refsampleid bigint,
    supplierid bigint NOT NULL
);


--
-- Name: sam_sampca2abstsa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sam_sampca2abstsa (
    abstractsampleid bigint NOT NULL,
    samplecategoryid bigint NOT NULL
);


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
    assigntoid bigint,
    holderid bigint,
    refsampleid bigint
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
    containerid bigint,
    refcomponentid bigint NOT NULL,
    researchobjectivelementid bigint
);


--
-- Name: sche_scheduledtask; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sche_scheduledtask (
    completiontime timestamp with time zone,
    name character varying(80) NOT NULL,
    priority integer,
    scheduledtime timestamp with time zone NOT NULL,
    state integer,
    labbookentryid bigint NOT NULL,
    holderid bigint NOT NULL,
    instrumentid bigint,
    scheduleplanoffsetid bigint
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
    scheduleplanid bigint NOT NULL,
    order_ integer
);


--
-- Name: targ_alias; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_alias (
    name character varying(255) NOT NULL,
    labbookentryid bigint NOT NULL,
    targetid bigint NOT NULL
);


--
-- Name: targ_milestone; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_milestone (
    date_ timestamp with time zone NOT NULL,
    labbookentryid bigint NOT NULL,
    experimentid bigint,
    statusid bigint NOT NULL,
    targetid bigint NOT NULL
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
-- Name: targ_similarityhit; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE targ_similarityhit (
    description text,
    evalue double precision,
    similaritypercentage double precision,
    labbookentryid bigint NOT NULL,
    attachmentid bigint NOT NULL,
    targetid bigint NOT NULL
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
    groupingtype character varying(80),
    name character varying(80) NOT NULL,
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
    whychosen text,
    labbookentryid bigint NOT NULL,
    moleculeid bigint,
    researchobjectiveid bigint NOT NULL,
    targetid bigint
);


--
-- Data for Name: acco_permission; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_permission (optype, permission, permissionclass, rolename, systemclassid, accessobjectid, usergroupid) FROM stdin;
update	t	PIMS	any	8004	8002	8003
read	t	PIMS	any	8005	8002	8003
create	t	PIMS	any	8006	8002	8003
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
public	8003	\N
\.


--
-- Data for Name: acco_usergroup2user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY acco_usergroup2user (usergroupid, memberuserid) FROM stdin;
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

COPY core_attachment (dbid, date, details, authorid, parententryid) FROM stdin;
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

COPY core_labbookentry (dbid, creationdate, details, lastediteddate, accessid, creatorid, lasteditorid) FROM stdin;
8001	2009-08-03 16:30:51.706+01	\N	\N	8002	\N	\N
14001	2009-08-03 16:31:22.821+01	\N	\N	\N	\N	\N
14002	2009-08-03 16:31:22.896+01	\N	\N	\N	\N	\N
14003	2009-08-03 16:31:22.908+01	\N	\N	\N	\N	\N
14004	2009-08-03 16:31:22.92+01	\N	\N	\N	\N	\N
14005	2009-08-03 16:31:22.933+01	\N	\N	\N	\N	\N
14006	2009-08-03 16:31:22.948+01	\N	\N	\N	\N	\N
14007	2009-08-03 16:31:22.962+01	\N	\N	\N	\N	\N
14008	2009-08-03 16:31:22.978+01	\N	\N	\N	\N	\N
14009	2009-08-03 16:31:22.994+01	\N	\N	\N	\N	\N
14010	2009-08-03 16:31:23.011+01	\N	\N	\N	\N	\N
14011	2009-08-03 16:31:23.029+01	\N	\N	\N	\N	\N
14012	2009-08-03 16:31:23.048+01	\N	\N	\N	\N	\N
14013	2009-08-03 16:31:23.069+01	\N	\N	\N	\N	\N
14014	2009-08-03 16:31:23.089+01	\N	\N	\N	\N	\N
14015	2009-08-03 16:31:23.11+01	\N	\N	\N	\N	\N
14016	2009-08-03 16:31:23.133+01	\N	\N	\N	\N	\N
14017	2009-08-03 16:31:23.156+01	\N	\N	\N	\N	\N
14018	2009-08-03 16:31:23.18+01	\N	\N	\N	\N	\N
14019	2009-08-03 16:31:23.204+01	\N	\N	\N	\N	\N
14020	2009-08-03 16:31:23.229+01	\N	\N	\N	\N	\N
14021	2009-08-03 16:31:23.255+01	\N	\N	\N	\N	\N
14022	2009-08-03 16:31:23.283+01	\N	\N	\N	\N	\N
14023	2009-08-03 16:31:23.31+01	\N	\N	\N	\N	\N
14024	2009-08-03 16:31:23.339+01	\N	\N	\N	\N	\N
14025	2009-08-03 16:31:23.368+01	\N	\N	\N	\N	\N
14026	2009-08-03 16:31:23.398+01	\N	\N	\N	\N	\N
14027	2009-08-03 16:31:23.43+01	\N	\N	\N	\N	\N
14028	2009-08-03 16:31:23.462+01	\N	\N	\N	\N	\N
14029	2009-08-03 16:31:23.494+01	\N	\N	\N	\N	\N
14030	2009-08-03 16:31:23.528+01	\N	\N	\N	\N	\N
14031	2009-08-03 16:31:23.562+01	\N	\N	\N	\N	\N
14032	2009-08-03 16:31:23.597+01	\N	\N	\N	\N	\N
14033	2009-08-03 16:31:23.633+01	\N	\N	\N	\N	\N
14034	2009-08-03 16:31:23.671+01	\N	\N	\N	\N	\N
16001	2009-08-03 16:31:32.782+01	\N	\N	\N	\N	\N
16002	2009-08-03 16:31:32.869+01	\N	\N	\N	\N	\N
16003	2009-08-03 16:31:32.884+01	\N	\N	\N	\N	\N
16004	2009-08-03 16:31:32.898+01	\N	\N	\N	\N	\N
16005	2009-08-03 16:31:32.909+01	\N	\N	\N	\N	\N
16006	2009-08-03 16:31:32.925+01	\N	\N	\N	\N	\N
16007	2009-08-03 16:31:32.937+01	\N	\N	\N	\N	\N
16008	2009-08-03 16:31:32.955+01	\N	\N	\N	\N	\N
16009	2009-08-03 16:31:32.969+01	\N	\N	\N	\N	\N
16010	2009-08-03 16:31:32.989+01	\N	\N	\N	\N	\N
16011	2009-08-03 16:31:33.005+01	\N	\N	\N	\N	\N
16012	2009-08-03 16:31:33.03+01	\N	\N	\N	\N	\N
16013	2009-08-03 16:31:33.048+01	\N	\N	\N	\N	\N
16014	2009-08-03 16:31:33.071+01	\N	\N	\N	\N	\N
16015	2009-08-03 16:31:33.093+01	\N	\N	\N	\N	\N
16016	2009-08-03 16:31:33.118+01	\N	\N	\N	\N	\N
16017	2009-08-03 16:31:33.139+01	\N	\N	\N	\N	\N
16018	2009-08-03 16:31:33.166+01	\N	\N	\N	\N	\N
16019	2009-08-03 16:31:33.192+01	\N	\N	\N	\N	\N
16020	2009-08-03 16:31:33.221+01	\N	\N	\N	\N	\N
16021	2009-08-03 16:31:33.269+01	\N	\N	\N	\N	\N
16022	2009-08-03 16:31:33.296+01	\N	\N	\N	\N	\N
16023	2009-08-03 16:31:33.328+01	\N	\N	\N	\N	\N
16024	2009-08-03 16:31:33.356+01	\N	\N	\N	\N	\N
16025	2009-08-03 16:31:33.389+01	\N	\N	\N	\N	\N
16026	2009-08-03 16:31:33.42+01	\N	\N	\N	\N	\N
16027	2009-08-03 16:31:33.631+01	\N	\N	\N	\N	\N
16028	2009-08-03 16:31:33.662+01	\N	\N	\N	\N	\N
16029	2009-08-03 16:31:33.699+01	\N	\N	\N	\N	\N
16030	2009-08-03 16:31:33.735+01	\N	\N	\N	\N	\N
16031	2009-08-03 16:31:33.775+01	\N	\N	\N	\N	\N
16032	2009-08-03 16:31:33.813+01	\N	\N	\N	\N	\N
16033	2009-08-03 16:31:33.851+01	\N	\N	\N	\N	\N
16034	2009-08-03 16:31:33.892+01	\N	\N	\N	\N	\N
16035	2009-08-03 16:31:33.93+01	\N	\N	\N	\N	\N
16036	2009-08-03 16:31:33.974+01	\N	\N	\N	\N	\N
16037	2009-08-03 16:31:34.014+01	\N	\N	\N	\N	\N
16038	2009-08-03 16:31:34.059+01	\N	\N	\N	\N	\N
16039	2009-08-03 16:31:34.102+01	\N	\N	\N	\N	\N
16040	2009-08-03 16:31:34.149+01	\N	\N	\N	\N	\N
16041	2009-08-03 16:31:34.192+01	\N	\N	\N	\N	\N
16042	2009-08-03 16:31:34.241+01	\N	\N	\N	\N	\N
16043	2009-08-03 16:31:34.288+01	\N	\N	\N	\N	\N
16044	2009-08-03 16:31:34.338+01	\N	\N	\N	\N	\N
16045	2009-08-03 16:31:34.383+01	\N	\N	\N	\N	\N
16046	2009-08-03 16:31:34.434+01	\N	\N	\N	\N	\N
16047	2009-08-03 16:31:34.481+01	\N	\N	\N	\N	\N
16048	2009-08-03 16:31:34.533+01	\N	\N	\N	\N	\N
16049	2009-08-03 16:31:34.584+01	\N	\N	\N	\N	\N
16050	2009-08-03 16:31:34.638+01	\N	\N	\N	\N	\N
16051	2009-08-03 16:31:34.691+01	\N	\N	\N	\N	\N
16052	2009-08-03 16:31:34.752+01	\N	\N	\N	\N	\N
16053	2009-08-03 16:31:34.804+01	\N	\N	\N	\N	\N
16054	2009-08-03 16:31:34.861+01	\N	\N	\N	\N	\N
16055	2009-08-03 16:31:34.915+01	\N	\N	\N	\N	\N
16056	2009-08-03 16:31:34.973+01	\N	\N	\N	\N	\N
16057	2009-08-03 16:31:35.028+01	\N	\N	\N	\N	\N
16058	2009-08-03 16:31:35.091+01	\N	\N	\N	\N	\N
16059	2009-08-03 16:31:35.148+01	\N	\N	\N	\N	\N
16060	2009-08-03 16:31:35.21+01	\N	\N	\N	\N	\N
16061	2009-08-03 16:31:35.269+01	\N	\N	\N	\N	\N
16062	2009-08-03 16:31:35.329+01	\N	\N	\N	\N	\N
16063	2009-08-03 16:31:35.39+01	\N	\N	\N	\N	\N
16064	2009-08-03 16:31:35.455+01	\N	\N	\N	\N	\N
16065	2009-08-03 16:31:35.517+01	\N	\N	\N	\N	\N
16066	2009-08-03 16:31:35.586+01	\N	\N	\N	\N	\N
16067	2009-08-03 16:31:35.652+01	\N	\N	\N	\N	\N
16068	2009-08-03 16:31:35.72+01	\N	\N	\N	\N	\N
16069	2009-08-03 16:31:35.788+01	\N	\N	\N	\N	\N
16070	2009-08-03 16:31:35.861+01	\N	\N	\N	\N	\N
16071	2009-08-03 16:31:35.929+01	\N	\N	\N	\N	\N
16072	2009-08-03 16:31:36.002+01	\N	\N	\N	\N	\N
16073	2009-08-03 16:31:36.072+01	\N	\N	\N	\N	\N
16074	2009-08-03 16:31:36.148+01	\N	\N	\N	\N	\N
16075	2009-08-03 16:31:36.251+01	\N	\N	\N	\N	\N
16076	2009-08-03 16:31:36.326+01	\N	\N	\N	\N	\N
16077	2009-08-03 16:31:36.399+01	\N	\N	\N	\N	\N
16078	2009-08-03 16:31:36.477+01	\N	\N	\N	\N	\N
16079	2009-08-03 16:31:36.553+01	\N	\N	\N	\N	\N
16080	2009-08-03 16:31:36.635+01	\N	\N	\N	\N	\N
16081	2009-08-03 16:31:36.714+01	\N	\N	\N	\N	\N
16082	2009-08-03 16:31:36.794+01	\N	\N	\N	\N	\N
16083	2009-08-03 16:31:36.875+01	\N	\N	\N	\N	\N
16084	2009-08-03 16:31:36.959+01	\N	\N	\N	\N	\N
16085	2009-08-03 16:31:37.045+01	\N	\N	\N	\N	\N
16086	2009-08-03 16:31:37.132+01	\N	\N	\N	\N	\N
16087	2009-08-03 16:31:37.213+01	\N	\N	\N	\N	\N
16088	2009-08-03 16:31:37.299+01	\N	\N	\N	\N	\N
16089	2009-08-03 16:31:37.382+01	\N	\N	\N	\N	\N
16090	2009-08-03 16:31:37.471+01	\N	\N	\N	\N	\N
16091	2009-08-03 16:31:37.556+01	\N	\N	\N	\N	\N
16092	2009-08-03 16:31:37.649+01	\N	\N	\N	\N	\N
16093	2009-08-03 16:31:37.736+01	\N	\N	\N	\N	\N
16094	2009-08-03 16:31:37.832+01	\N	\N	\N	\N	\N
16095	2009-08-03 16:31:37.921+01	\N	\N	\N	\N	\N
16096	2009-08-03 16:31:38.013+01	\N	\N	\N	\N	\N
16097	2009-08-03 16:31:38.107+01	\N	\N	\N	\N	\N
16098	2009-08-03 16:31:38.203+01	\N	\N	\N	\N	\N
16099	2009-08-03 16:31:38.301+01	\N	\N	\N	\N	\N
16100	2009-08-03 16:31:38.397+01	\N	\N	\N	\N	\N
16101	2009-08-03 16:31:38.49+01	\N	\N	\N	\N	\N
16102	2009-08-03 16:31:38.591+01	\N	\N	\N	\N	\N
16103	2009-08-03 16:31:38.686+01	\N	\N	\N	\N	\N
16104	2009-08-03 16:31:38.784+01	\N	\N	\N	\N	\N
16105	2009-08-03 16:31:38.88+01	\N	\N	\N	\N	\N
16106	2009-08-03 16:31:38.983+01	\N	\N	\N	\N	\N
16107	2009-08-03 16:31:39.087+01	\N	\N	\N	\N	\N
16108	2009-08-03 16:31:39.188+01	\N	\N	\N	\N	\N
16109	2009-08-03 16:31:39.289+01	\N	\N	\N	\N	\N
16110	2009-08-03 16:31:39.392+01	\N	\N	\N	\N	\N
16111	2009-08-03 16:31:39.493+01	\N	\N	\N	\N	\N
16112	2009-08-03 16:31:39.60+01	\N	\N	\N	\N	\N
16113	2009-08-03 16:31:39.702+01	\N	\N	\N	\N	\N
16114	2009-08-03 16:31:39.809+01	\N	\N	\N	\N	\N
16115	2009-08-03 16:31:39.913+01	\N	\N	\N	\N	\N
16116	2009-08-03 16:31:40.022+01	\N	\N	\N	\N	\N
16117	2009-08-03 16:31:40.142+01	\N	\N	\N	\N	\N
16118	2009-08-03 16:31:40.254+01	\N	\N	\N	\N	\N
16119	2009-08-03 16:31:40.363+01	\N	\N	\N	\N	\N
16120	2009-08-03 16:31:40.477+01	\N	\N	\N	\N	\N
16121	2009-08-03 16:31:40.589+01	\N	\N	\N	\N	\N
16122	2009-08-03 16:31:40.704+01	\N	\N	\N	\N	\N
16123	2009-08-03 16:31:40.822+01	\N	\N	\N	\N	\N
16124	2009-08-03 16:31:40.938+01	\N	\N	\N	\N	\N
16125	2009-08-03 16:31:41.057+01	\N	\N	\N	\N	\N
16126	2009-08-03 16:31:41.179+01	\N	\N	\N	\N	\N
16127	2009-08-03 16:31:41.297+01	\N	\N	\N	\N	\N
16128	2009-08-03 16:31:41.418+01	\N	\N	\N	\N	\N
16129	2009-08-03 16:31:41.538+01	\N	\N	\N	\N	\N
16130	2009-08-03 16:31:41.663+01	\N	\N	\N	\N	\N
16131	2009-08-03 16:31:41.789+01	\N	\N	\N	\N	\N
16132	2009-08-03 16:31:41.913+01	\N	\N	\N	\N	\N
16133	2009-08-03 16:31:42.041+01	\N	\N	\N	\N	\N
16134	2009-08-03 16:31:42.147+01	\N	\N	\N	\N	\N
16135	2009-08-03 16:31:42.252+01	\N	\N	\N	\N	\N
16136	2009-08-03 16:31:42.328+01	\N	\N	\N	\N	\N
16137	2009-08-03 16:31:42.406+01	\N	\N	\N	\N	\N
16138	2009-08-03 16:31:42.476+01	\N	\N	\N	\N	\N
16139	2009-08-03 16:31:42.546+01	\N	\N	\N	\N	\N
16140	2009-08-03 16:31:42.616+01	\N	\N	\N	\N	\N
16141	2009-08-03 16:31:42.687+01	\N	\N	\N	\N	\N
16142	2009-08-03 16:31:42.757+01	\N	\N	\N	\N	\N
16143	2009-08-03 16:31:42.829+01	\N	\N	\N	\N	\N
16145	2009-08-03 16:31:42.958+01	\N	\N	\N	\N	\N
16146	2009-08-03 16:31:43.016+01	\N	\N	\N	\N	\N
16147	2009-08-03 16:31:43.071+01	\N	\N	\N	\N	\N
16148	2009-08-03 16:31:43.117+01	\N	\N	\N	\N	\N
16149	2009-08-03 16:31:43.158+01	\N	\N	\N	\N	\N
16150	2009-08-03 16:31:43.20+01	\N	\N	\N	\N	\N
16151	2009-08-03 16:31:43.238+01	\N	\N	\N	\N	\N
16153	2009-08-03 16:31:43.309+01	\N	\N	\N	\N	\N
16154	2009-08-03 16:31:43.34+01	\N	\N	\N	\N	\N
16155	2009-08-03 16:31:43.372+01	\N	\N	\N	\N	\N
16156	2009-08-03 16:31:43.40+01	\N	\N	\N	\N	\N
16157	2009-08-03 16:31:43.43+01	\N	\N	\N	\N	\N
16158	2009-08-03 16:31:43.459+01	\N	\N	\N	\N	\N
16159	2009-08-03 16:31:43.486+01	\N	\N	\N	\N	\N
16160	2009-08-03 16:31:43.513+01	\N	\N	\N	\N	\N
16161	2009-08-03 16:31:43.536+01	\N	\N	\N	\N	\N
16162	2009-08-03 16:31:43.563+01	\N	\N	\N	\N	\N
16163	2009-08-03 16:31:43.589+01	\N	\N	\N	\N	\N
16164	2009-08-03 16:31:43.614+01	\N	\N	\N	\N	\N
16165	2009-08-03 16:31:43.638+01	\N	\N	\N	\N	\N
16166	2009-08-03 16:31:43.662+01	\N	\N	\N	\N	\N
22001	2009-08-03 16:32:12.942+01	\N	2009-08-03 16:32:12.996+01	\N	\N	\N
22002	2009-08-03 16:32:13.034+01	\N	\N	\N	\N	\N
22003	2009-08-03 16:32:13.042+01	\N	\N	\N	\N	\N
22004	2009-08-03 16:32:13.075+01	\N	2009-08-03 16:32:13.082+01	\N	\N	\N
22005	2009-08-03 16:32:13.124+01	\N	\N	\N	\N	\N
22006	2009-08-03 16:32:13.132+01	\N	\N	\N	\N	\N
22007	2009-08-03 16:32:13.156+01	\N	2009-08-03 16:32:13.163+01	\N	\N	\N
22008	2009-08-03 16:32:13.21+01	\N	\N	\N	\N	\N
22009	2009-08-03 16:32:13.219+01	\N	\N	\N	\N	\N
22010	2009-08-03 16:32:13.248+01	\N	2009-08-03 16:32:13.256+01	\N	\N	\N
22011	2009-08-03 16:32:13.316+01	\N	\N	\N	\N	\N
22012	2009-08-03 16:32:13.326+01	\N	\N	\N	\N	\N
22013	2009-08-03 16:32:13.944+01	\N	2009-08-03 16:32:13.994+01	\N	\N	\N
22014	2009-08-03 16:32:14.094+01	\N	\N	\N	\N	\N
22015	2009-08-03 16:32:14.102+01	\N	\N	\N	\N	\N
22016	2009-08-03 16:32:14.132+01	\N	2009-08-03 16:32:14.14+01	\N	\N	\N
22017	2009-08-03 16:32:14.18+01	\N	\N	\N	\N	\N
22018	2009-08-03 16:32:14.189+01	\N	\N	\N	\N	\N
22019	2009-08-03 16:32:14.221+01	\N	2009-08-03 16:32:14.23+01	\N	\N	\N
22020	2009-08-03 16:32:14.324+01	\N	\N	\N	\N	\N
22021	2009-08-03 16:32:14.334+01	\N	\N	\N	\N	\N
22022	2009-08-03 16:32:14.366+01	\N	2009-08-03 16:32:14.375+01	\N	\N	\N
22023	2009-08-03 16:32:14.425+01	\N	\N	\N	\N	\N
22024	2009-08-03 16:32:14.435+01	\N	\N	\N	\N	\N
22025	2009-08-03 16:32:14.47+01	\N	2009-08-03 16:32:14.479+01	\N	\N	\N
22026	2009-08-03 16:32:14.504+01	\N	\N	\N	\N	\N
22027	2009-08-03 16:32:14.513+01	\N	\N	\N	\N	\N
22028	2009-08-03 16:32:14.557+01	\N	2009-08-03 16:32:14.568+01	\N	\N	\N
22029	2009-08-03 16:32:14.605+01	\N	\N	\N	\N	\N
22030	2009-08-03 16:32:14.615+01	\N	\N	\N	\N	\N
22031	2009-08-03 16:32:14.648+01	\N	2009-08-03 16:32:14.658+01	\N	\N	\N
22032	2009-08-03 16:32:14.673+01	\N	\N	\N	\N	\N
22033	2009-08-03 16:32:14.682+01	\N	\N	\N	\N	\N
22034	2009-08-03 16:32:14.72+01	\N	2009-08-03 16:32:14.728+01	\N	\N	\N
22035	2009-08-03 16:32:14.744+01	\N	\N	\N	\N	\N
22036	2009-08-03 16:32:14.753+01	\N	\N	\N	\N	\N
22037	2009-08-03 16:32:14.788+01	\N	2009-08-03 16:32:14.798+01	\N	\N	\N
22038	2009-08-03 16:32:14.814+01	\N	\N	\N	\N	\N
22039	2009-08-03 16:32:14.823+01	\N	\N	\N	\N	\N
22040	2009-08-03 16:32:14.857+01	\N	2009-08-03 16:32:14.867+01	\N	\N	\N
22041	2009-08-03 16:32:14.883+01	\N	\N	\N	\N	\N
22042	2009-08-03 16:32:14.895+01	\N	\N	\N	\N	\N
22043	2009-08-03 16:32:14.93+01	\N	2009-08-03 16:32:14.94+01	\N	\N	\N
22044	2009-08-03 16:32:14.988+01	\N	\N	\N	\N	\N
22045	2009-08-03 16:32:15.425+01	\N	\N	\N	\N	\N
22046	2009-08-03 16:32:15.449+01	\N	2009-08-03 16:32:15.454+01	\N	\N	\N
22047	2009-08-03 16:32:15.477+01	\N	\N	\N	\N	\N
22048	2009-08-03 16:32:15.481+01	\N	\N	\N	\N	\N
22049	2009-08-03 16:32:15.496+01	\N	2009-08-03 16:32:15.50+01	\N	\N	\N
22050	2009-08-03 16:32:15.522+01	\N	\N	\N	\N	\N
22051	2009-08-03 16:32:15.527+01	\N	\N	\N	\N	\N
22052	2009-08-03 16:32:15.546+01	\N	2009-08-03 16:32:15.55+01	\N	\N	\N
22053	2009-08-03 16:32:15.571+01	\N	\N	\N	\N	\N
22054	2009-08-03 16:32:15.576+01	\N	\N	\N	\N	\N
22055	2009-08-03 16:32:15.616+01	\N	2009-08-03 16:32:15.621+01	\N	\N	\N
22056	2009-08-03 16:32:15.644+01	\N	\N	\N	\N	\N
22057	2009-08-03 16:32:15.649+01	\N	\N	\N	\N	\N
22058	2009-08-03 16:32:15.667+01	\N	2009-08-03 16:32:15.672+01	\N	\N	\N
22059	2009-08-03 16:32:15.70+01	\N	\N	\N	\N	\N
22060	2009-08-03 16:32:15.705+01	\N	\N	\N	\N	\N
22061	2009-08-03 16:32:15.732+01	\N	2009-08-03 16:32:15.737+01	\N	\N	\N
22062	2009-08-03 16:32:15.752+01	\N	\N	\N	\N	\N
22063	2009-08-03 16:32:15.757+01	\N	\N	\N	\N	\N
22064	2009-08-03 16:32:15.78+01	\N	2009-08-03 16:32:15.785+01	\N	\N	\N
22065	2009-08-03 16:32:15.808+01	\N	\N	\N	\N	\N
22066	2009-08-03 16:32:15.813+01	\N	\N	\N	\N	\N
22067	2009-08-03 16:32:15.834+01	\N	2009-08-03 16:32:15.839+01	\N	\N	\N
22068	2009-08-03 16:32:15.858+01	\N	\N	\N	\N	\N
22069	2009-08-03 16:32:15.863+01	\N	\N	\N	\N	\N
22070	2009-08-03 16:32:15.881+01	\N	2009-08-03 16:32:15.886+01	\N	\N	\N
22071	2009-08-03 16:32:15.916+01	\N	\N	\N	\N	\N
22072	2009-08-03 16:32:15.921+01	\N	\N	\N	\N	\N
22073	2009-08-03 16:32:15.94+01	\N	2009-08-03 16:32:15.945+01	\N	\N	\N
22074	2009-08-03 16:32:15.972+01	\N	\N	\N	\N	\N
22075	2009-08-03 16:32:15.977+01	\N	\N	\N	\N	\N
22076	2009-08-03 16:32:15.999+01	\N	2009-08-03 16:32:16.004+01	\N	\N	\N
22077	2009-08-03 16:32:16.024+01	\N	\N	\N	\N	\N
22078	2009-08-03 16:32:16.029+01	\N	\N	\N	\N	\N
22079	2009-08-03 16:32:16.046+01	\N	2009-08-03 16:32:16.058+01	\N	\N	\N
22080	2009-08-03 16:32:16.114+01	\N	\N	\N	\N	\N
22081	2009-08-03 16:32:16.119+01	\N	\N	\N	\N	\N
22082	2009-08-03 16:32:16.136+01	\N	2009-08-03 16:32:16.141+01	\N	\N	\N
22083	2009-08-03 16:32:16.162+01	\N	\N	\N	\N	\N
22084	2009-08-03 16:32:16.167+01	\N	\N	\N	\N	\N
22085	2009-08-03 16:32:16.191+01	\N	2009-08-03 16:32:16.195+01	\N	\N	\N
22086	2009-08-03 16:32:16.243+01	\N	\N	\N	\N	\N
22087	2009-08-03 16:32:16.248+01	\N	\N	\N	\N	\N
22088	2009-08-03 16:32:16.268+01	\N	2009-08-03 16:32:16.273+01	\N	\N	\N
22089	2009-08-03 16:32:16.291+01	\N	\N	\N	\N	\N
22090	2009-08-03 16:32:16.297+01	\N	\N	\N	\N	\N
22091	2009-08-03 16:32:16.35+01	\N	2009-08-03 16:32:16.356+01	\N	\N	\N
22092	2009-08-03 16:32:16.414+01	\N	\N	\N	\N	\N
22093	2009-08-03 16:32:16.42+01	\N	\N	\N	\N	\N
22094	2009-08-03 16:32:16.444+01	\N	2009-08-03 16:32:16.45+01	\N	\N	\N
22095	2009-08-03 16:32:16.475+01	\N	\N	\N	\N	\N
22096	2009-08-03 16:32:16.481+01	\N	\N	\N	\N	\N
22097	2009-08-03 16:32:16.505+01	\N	2009-08-03 16:32:16.511+01	\N	\N	\N
22098	2009-08-03 16:32:16.539+01	\N	\N	\N	\N	\N
22099	2009-08-03 16:32:16.544+01	\N	\N	\N	\N	\N
22100	2009-08-03 16:32:16.569+01	\N	2009-08-03 16:32:16.575+01	\N	\N	\N
22101	2009-08-03 16:32:16.647+01	\N	\N	\N	\N	\N
22102	2009-08-03 16:32:16.653+01	\N	\N	\N	\N	\N
22103	2009-08-03 16:32:16.675+01	\N	2009-08-03 16:32:16.681+01	\N	\N	\N
22104	2009-08-03 16:32:16.692+01	\N	\N	\N	\N	\N
22105	2009-08-03 16:32:16.697+01	\N	\N	\N	\N	\N
22106	2009-08-03 16:32:16.72+01	\N	2009-08-03 16:32:16.725+01	\N	\N	\N
22107	2009-08-03 16:32:16.755+01	\N	\N	\N	\N	\N
22108	2009-08-03 16:32:16.763+01	\N	\N	\N	\N	\N
22109	2009-08-03 16:32:16.786+01	\N	2009-08-03 16:32:16.791+01	\N	\N	\N
22110	2009-08-03 16:32:16.822+01	\N	\N	\N	\N	\N
22111	2009-08-03 16:32:16.835+01	\N	\N	\N	\N	\N
22112	2009-08-03 16:32:16.858+01	\N	2009-08-03 16:32:16.864+01	\N	\N	\N
22113	2009-08-03 16:32:16.907+01	\N	\N	\N	\N	\N
22114	2009-08-03 16:32:16.914+01	\N	\N	\N	\N	\N
22115	2009-08-03 16:32:16.937+01	\N	2009-08-03 16:32:16.943+01	\N	\N	\N
22116	2009-08-03 16:32:16.963+01	\N	\N	\N	\N	\N
22117	2009-08-03 16:32:16.975+01	\N	\N	\N	\N	\N
22118	2009-08-03 16:32:16.998+01	\N	2009-08-03 16:32:17.004+01	\N	\N	\N
22119	2009-08-03 16:32:17.065+01	\N	\N	\N	\N	\N
22120	2009-08-03 16:32:17.072+01	\N	\N	\N	\N	\N
22121	2009-08-03 16:32:17.093+01	\N	2009-08-03 16:32:17.098+01	\N	\N	\N
22122	2009-08-03 16:32:17.142+01	\N	\N	\N	\N	\N
22123	2009-08-03 16:32:17.149+01	\N	\N	\N	\N	\N
22124	2009-08-03 16:32:17.172+01	\N	2009-08-03 16:32:17.178+01	\N	\N	\N
22125	2009-08-03 16:32:17.19+01	\N	\N	\N	\N	\N
22126	2009-08-03 16:32:17.196+01	\N	\N	\N	\N	\N
22127	2009-08-03 16:32:17.23+01	\N	2009-08-03 16:32:17.236+01	\N	\N	\N
22128	2009-08-03 16:32:17.247+01	\N	\N	\N	\N	\N
22129	2009-08-03 16:32:17.253+01	\N	\N	\N	\N	\N
22130	2009-08-03 16:32:17.277+01	\N	2009-08-03 16:32:17.283+01	\N	\N	\N
22131	2009-08-03 16:32:17.294+01	\N	\N	\N	\N	\N
22132	2009-08-03 16:32:17.301+01	\N	\N	\N	\N	\N
22133	2009-08-03 16:32:17.327+01	\N	2009-08-03 16:32:17.333+01	\N	\N	\N
22134	2009-08-03 16:32:17.354+01	\N	\N	\N	\N	\N
22135	2009-08-03 16:32:17.361+01	\N	\N	\N	\N	\N
22136	2009-08-03 16:32:17.391+01	\N	2009-08-03 16:32:17.397+01	\N	\N	\N
22137	2009-08-03 16:32:17.414+01	\N	\N	\N	\N	\N
22138	2009-08-03 16:32:17.42+01	\N	\N	\N	\N	\N
22139	2009-08-03 16:32:17.445+01	\N	2009-08-03 16:32:17.469+01	\N	\N	\N
22140	2009-08-03 16:32:17.533+01	\N	\N	\N	\N	\N
22141	2009-08-03 16:32:17.539+01	\N	\N	\N	\N	\N
22142	2009-08-03 16:32:17.562+01	\N	2009-08-03 16:32:17.567+01	\N	\N	\N
22143	2009-08-03 16:32:17.592+01	\N	\N	\N	\N	\N
22144	2009-08-03 16:32:17.598+01	\N	\N	\N	\N	\N
22145	2009-08-03 16:32:17.62+01	\N	2009-08-03 16:32:17.627+01	\N	\N	\N
22146	2009-08-03 16:32:17.656+01	\N	\N	\N	\N	\N
22147	2009-08-03 16:32:17.663+01	\N	\N	\N	\N	\N
22148	2009-08-03 16:32:17.686+01	\N	2009-08-03 16:32:17.692+01	\N	\N	\N
22149	2009-08-03 16:32:17.739+01	\N	\N	\N	\N	\N
22150	2009-08-03 16:32:17.746+01	\N	\N	\N	\N	\N
22151	2009-08-03 16:32:17.779+01	\N	2009-08-03 16:32:17.785+01	\N	\N	\N
22152	2009-08-03 16:32:17.815+01	\N	\N	\N	\N	\N
22153	2009-08-03 16:32:17.827+01	\N	\N	\N	\N	\N
22154	2009-08-03 16:32:17.855+01	\N	2009-08-03 16:32:17.862+01	\N	\N	\N
22155	2009-08-03 16:32:17.898+01	\N	\N	\N	\N	\N
22156	2009-08-03 16:32:17.905+01	\N	\N	\N	\N	\N
22157	2009-08-03 16:32:17.933+01	\N	2009-08-03 16:32:17.939+01	\N	\N	\N
22158	2009-08-03 16:32:17.975+01	\N	\N	\N	\N	\N
22159	2009-08-03 16:32:17.982+01	\N	\N	\N	\N	\N
22160	2009-08-03 16:32:18.006+01	\N	2009-08-03 16:32:18.013+01	\N	\N	\N
22161	2009-08-03 16:32:18.053+01	\N	\N	\N	\N	\N
22162	2009-08-03 16:32:18.06+01	\N	\N	\N	\N	\N
22163	2009-08-03 16:32:18.099+01	\N	2009-08-03 16:32:18.106+01	\N	\N	\N
22164	2009-08-03 16:32:18.137+01	\N	\N	\N	\N	\N
22165	2009-08-03 16:32:18.144+01	\N	\N	\N	\N	\N
22166	2009-08-03 16:32:18.169+01	\N	2009-08-03 16:32:18.175+01	\N	\N	\N
22167	2009-08-03 16:32:18.215+01	\N	\N	\N	\N	\N
22168	2009-08-03 16:32:18.222+01	\N	\N	\N	\N	\N
22169	2009-08-03 16:32:18.248+01	\N	2009-08-03 16:32:18.256+01	\N	\N	\N
22170	2009-08-03 16:32:18.329+01	\N	\N	\N	\N	\N
22171	2009-08-03 16:32:18.338+01	\N	\N	\N	\N	\N
22172	2009-08-03 16:32:18.364+01	\N	2009-08-03 16:32:18.371+01	\N	\N	\N
16144	2009-08-03 16:31:42.898+01	\N	2009-08-03 16:32:18.389+01	\N	\N	\N
22173	2009-08-03 16:32:18.413+01	\N	2009-08-03 16:32:18.42+01	\N	\N	\N
22174	2009-08-03 16:32:18.459+01	\N	\N	\N	\N	\N
22175	2009-08-03 16:32:18.472+01	\N	\N	\N	\N	\N
22176	2009-08-03 16:32:18.524+01	\N	2009-08-03 16:32:18.532+01	\N	\N	\N
22177	2009-08-03 16:32:18.561+01	\N	\N	\N	\N	\N
22178	2009-08-03 16:32:18.569+01	\N	\N	\N	\N	\N
22179	2009-08-03 16:32:18.601+01	\N	2009-08-03 16:32:18.608+01	\N	\N	\N
22180	2009-08-03 16:32:18.643+01	\N	\N	\N	\N	\N
22181	2009-08-03 16:32:18.661+01	\N	\N	\N	\N	\N
22182	2009-08-03 16:32:18.688+01	\N	2009-08-03 16:32:18.695+01	\N	\N	\N
22183	2009-08-03 16:32:18.751+01	\N	\N	\N	\N	\N
22184	2009-08-03 16:32:18.759+01	\N	\N	\N	\N	\N
22185	2009-08-03 16:32:18.786+01	\N	2009-08-03 16:32:18.793+01	\N	\N	\N
22186	2009-08-03 16:32:18.807+01	\N	\N	\N	\N	\N
22187	2009-08-03 16:32:18.814+01	\N	\N	\N	\N	\N
22188	2009-08-03 16:32:18.855+01	\N	2009-08-03 16:32:18.867+01	\N	\N	\N
22189	2009-08-03 16:32:18.88+01	\N	\N	\N	\N	\N
22190	2009-08-03 16:32:18.887+01	\N	\N	\N	\N	\N
22191	2009-08-03 16:32:18.915+01	\N	2009-08-03 16:32:18.923+01	\N	\N	\N
22192	2009-08-03 16:32:18.975+01	\N	\N	\N	\N	\N
22193	2009-08-03 16:32:18.983+01	\N	\N	\N	\N	\N
22194	2009-08-03 16:32:19.011+01	\N	2009-08-03 16:32:19.021+01	\N	\N	\N
22195	2009-08-03 16:32:19.071+01	\N	\N	\N	\N	\N
22196	2009-08-03 16:32:19.08+01	\N	\N	\N	\N	\N
22197	2009-08-03 16:32:19.11+01	\N	2009-08-03 16:32:19.118+01	\N	\N	\N
22198	2009-08-03 16:32:19.157+01	\N	\N	\N	\N	\N
22199	2009-08-03 16:32:19.164+01	\N	\N	\N	\N	\N
22200	2009-08-03 16:32:19.26+01	\N	2009-08-03 16:32:19.268+01	\N	\N	\N
22201	2009-08-03 16:32:19.318+01	\N	\N	\N	\N	\N
22202	2009-08-03 16:32:19.326+01	\N	\N	\N	\N	\N
22203	2009-08-03 16:32:19.373+01	\N	2009-08-03 16:32:19.381+01	\N	\N	\N
22204	2009-08-03 16:32:19.45+01	\N	\N	\N	\N	\N
22205	2009-08-03 16:32:19.464+01	\N	\N	\N	\N	\N
22206	2009-08-03 16:32:19.493+01	\N	2009-08-03 16:32:19.501+01	\N	\N	\N
22207	2009-08-03 16:32:19.539+01	\N	\N	\N	\N	\N
22208	2009-08-03 16:32:19.547+01	\N	\N	\N	\N	\N
22209	2009-08-03 16:32:19.674+01	\N	2009-08-03 16:32:19.683+01	\N	\N	\N
22210	2009-08-03 16:32:19.712+01	\N	\N	\N	\N	\N
22211	2009-08-03 16:32:19.722+01	\N	\N	\N	\N	\N
22212	2009-08-03 16:32:19.804+01	\N	2009-08-03 16:32:19.812+01	\N	\N	\N
22213	2009-08-03 16:32:19.836+01	\N	\N	\N	\N	\N
22214	2009-08-03 16:32:19.847+01	\N	\N	\N	\N	\N
22215	2009-08-03 16:32:19.88+01	\N	2009-08-03 16:32:19.888+01	\N	\N	\N
22216	2009-08-03 16:32:19.945+01	\N	\N	\N	\N	\N
22217	2009-08-03 16:32:19.954+01	\N	\N	\N	\N	\N
22218	2009-08-03 16:32:19.988+01	\N	2009-08-03 16:32:19.997+01	\N	\N	\N
22219	2009-08-03 16:32:20.058+01	\N	\N	\N	\N	\N
22220	2009-08-03 16:32:20.068+01	\N	\N	\N	\N	\N
22221	2009-08-03 16:32:20.112+01	\N	2009-08-03 16:32:20.122+01	\N	\N	\N
22222	2009-08-03 16:32:20.141+01	\N	\N	\N	\N	\N
22223	2009-08-03 16:32:20.15+01	\N	\N	\N	\N	\N
22224	2009-08-03 16:32:20.192+01	\N	2009-08-03 16:32:20.203+01	\N	\N	\N
22225	2009-08-03 16:32:20.221+01	\N	\N	\N	\N	\N
22226	2009-08-03 16:32:20.23+01	\N	\N	\N	\N	\N
22227	2009-08-03 16:32:20.308+01	\N	2009-08-03 16:32:20.317+01	\N	\N	\N
22228	2009-08-03 16:32:20.365+01	\N	\N	\N	\N	\N
22229	2009-08-03 16:32:20.375+01	\N	\N	\N	\N	\N
22230	2009-08-03 16:32:20.412+01	\N	2009-08-03 16:32:20.421+01	\N	\N	\N
22231	2009-08-03 16:32:20.44+01	\N	\N	\N	\N	\N
22232	2009-08-03 16:32:20.449+01	\N	\N	\N	\N	\N
22233	2009-08-03 16:32:20.486+01	\N	2009-08-03 16:32:20.496+01	\N	\N	\N
22234	2009-08-03 16:32:20.571+01	\N	\N	\N	\N	\N
22235	2009-08-03 16:32:20.583+01	\N	\N	\N	\N	\N
22236	2009-08-03 16:32:20.628+01	\N	2009-08-03 16:32:20.638+01	\N	\N	\N
22237	2009-08-03 16:32:20.756+01	\N	\N	\N	\N	\N
22238	2009-08-03 16:32:20.767+01	\N	\N	\N	\N	\N
22239	2009-08-03 16:32:20.812+01	\N	2009-08-03 16:32:20.822+01	\N	\N	\N
22240	2009-08-03 16:32:20.898+01	\N	\N	\N	\N	\N
22241	2009-08-03 16:32:20.908+01	\N	\N	\N	\N	\N
22242	2009-08-03 16:32:20.946+01	\N	2009-08-03 16:32:20.956+01	\N	\N	\N
22243	2009-08-03 16:32:21.003+01	\N	\N	\N	\N	\N
22244	2009-08-03 16:32:21.013+01	\N	\N	\N	\N	\N
22245	2009-08-03 16:32:21.048+01	\N	2009-08-03 16:32:21.056+01	\N	\N	\N
22246	2009-08-03 16:32:21.132+01	\N	\N	\N	\N	\N
22247	2009-08-03 16:32:21.141+01	\N	\N	\N	\N	\N
22248	2009-08-03 16:32:21.176+01	\N	2009-08-03 16:32:21.184+01	\N	\N	\N
22249	2009-08-03 16:32:21.229+01	\N	\N	\N	\N	\N
22250	2009-08-03 16:32:21.237+01	\N	\N	\N	\N	\N
22251	2009-08-03 16:32:21.267+01	\N	2009-08-03 16:32:21.275+01	\N	\N	\N
22252	2009-08-03 16:32:21.29+01	\N	\N	\N	\N	\N
22253	2009-08-03 16:32:21.298+01	\N	\N	\N	\N	\N
22254	2009-08-03 16:32:21.33+01	\N	2009-08-03 16:32:21.338+01	\N	\N	\N
22255	2009-08-03 16:32:21.389+01	\N	\N	\N	\N	\N
22256	2009-08-03 16:32:21.398+01	\N	\N	\N	\N	\N
22257	2009-08-03 16:32:21.428+01	\N	2009-08-03 16:32:21.441+01	\N	\N	\N
22258	2009-08-03 16:32:21.494+01	\N	\N	\N	\N	\N
22259	2009-08-03 16:32:21.503+01	\N	\N	\N	\N	\N
22260	2009-08-03 16:32:21.533+01	\N	2009-08-03 16:32:21.543+01	\N	\N	\N
22261	2009-08-03 16:32:21.603+01	\N	\N	\N	\N	\N
22262	2009-08-03 16:32:21.612+01	\N	\N	\N	\N	\N
22263	2009-08-03 16:32:21.648+01	\N	2009-08-03 16:32:21.656+01	\N	\N	\N
22264	2009-08-03 16:32:21.775+01	\N	\N	\N	\N	\N
22265	2009-08-03 16:32:21.786+01	\N	\N	\N	\N	\N
22266	2009-08-03 16:32:21.82+01	\N	2009-08-03 16:32:21.829+01	\N	\N	\N
22267	2009-08-03 16:32:21.949+01	\N	\N	\N	\N	\N
22268	2009-08-03 16:32:21.959+01	\N	\N	\N	\N	\N
22269	2009-08-03 16:32:21.994+01	\N	2009-08-03 16:32:22.005+01	\N	\N	\N
22270	2009-08-03 16:32:22.098+01	\N	\N	\N	\N	\N
22271	2009-08-03 16:32:22.108+01	\N	\N	\N	\N	\N
22272	2009-08-03 16:32:22.144+01	\N	2009-08-03 16:32:22.154+01	\N	\N	\N
22273	2009-08-03 16:32:22.247+01	\N	\N	\N	\N	\N
22274	2009-08-03 16:32:22.258+01	\N	\N	\N	\N	\N
22275	2009-08-03 16:32:22.293+01	\N	2009-08-03 16:32:22.302+01	\N	\N	\N
22276	2009-08-03 16:32:22.40+01	\N	\N	\N	\N	\N
22277	2009-08-03 16:32:22.41+01	\N	\N	\N	\N	\N
22278	2009-08-03 16:32:22.454+01	\N	2009-08-03 16:32:22.472+01	\N	\N	\N
22279	2009-08-03 16:32:22.513+01	\N	\N	\N	\N	\N
22280	2009-08-03 16:32:22.524+01	\N	\N	\N	\N	\N
22281	2009-08-03 16:32:22.56+01	\N	2009-08-03 16:32:22.569+01	\N	\N	\N
22282	2009-08-03 16:32:22.626+01	\N	\N	\N	\N	\N
22283	2009-08-03 16:32:22.636+01	\N	\N	\N	\N	\N
22284	2009-08-03 16:32:22.673+01	\N	2009-08-03 16:32:22.683+01	\N	\N	\N
22285	2009-08-03 16:32:22.733+01	\N	\N	\N	\N	\N
22286	2009-08-03 16:32:22.744+01	\N	\N	\N	\N	\N
22287	2009-08-03 16:32:22.781+01	\N	2009-08-03 16:32:22.79+01	\N	\N	\N
22288	2009-08-03 16:32:22.835+01	\N	\N	\N	\N	\N
22289	2009-08-03 16:32:22.848+01	\N	\N	\N	\N	\N
22290	2009-08-03 16:32:22.886+01	\N	2009-08-03 16:32:22.896+01	\N	\N	\N
22291	2009-08-03 16:32:22.947+01	\N	\N	\N	\N	\N
22292	2009-08-03 16:32:22.957+01	\N	\N	\N	\N	\N
22293	2009-08-03 16:32:22.995+01	\N	2009-08-03 16:32:23.011+01	\N	\N	\N
22294	2009-08-03 16:32:23.046+01	\N	\N	\N	\N	\N
22295	2009-08-03 16:32:23.057+01	\N	\N	\N	\N	\N
22296	2009-08-03 16:32:23.095+01	\N	2009-08-03 16:32:23.106+01	\N	\N	\N
22297	2009-08-03 16:32:23.126+01	\N	\N	\N	\N	\N
22298	2009-08-03 16:32:23.136+01	\N	\N	\N	\N	\N
22299	2009-08-03 16:32:23.175+01	\N	2009-08-03 16:32:23.185+01	\N	\N	\N
22300	2009-08-03 16:32:23.27+01	\N	\N	\N	\N	\N
22301	2009-08-03 16:32:23.292+01	\N	\N	\N	\N	\N
22302	2009-08-03 16:32:23.359+01	\N	2009-08-03 16:32:23.37+01	\N	\N	\N
22303	2009-08-03 16:32:23.417+01	\N	\N	\N	\N	\N
22304	2009-08-03 16:32:23.427+01	\N	\N	\N	\N	\N
22305	2009-08-03 16:32:23.463+01	\N	2009-08-03 16:32:23.473+01	\N	\N	\N
22306	2009-08-03 16:32:23.532+01	\N	\N	\N	\N	\N
22307	2009-08-03 16:32:23.543+01	\N	\N	\N	\N	\N
22308	2009-08-03 16:32:23.584+01	\N	2009-08-03 16:32:23.594+01	\N	\N	\N
22309	2009-08-03 16:32:23.645+01	\N	\N	\N	\N	\N
22310	2009-08-03 16:32:23.656+01	\N	\N	\N	\N	\N
22311	2009-08-03 16:32:23.702+01	\N	2009-08-03 16:32:23.712+01	\N	\N	\N
22312	2009-08-03 16:32:23.761+01	\N	\N	\N	\N	\N
22313	2009-08-03 16:32:23.772+01	\N	\N	\N	\N	\N
22314	2009-08-03 16:32:23.811+01	\N	2009-08-03 16:32:23.821+01	\N	\N	\N
22315	2009-08-03 16:32:23.892+01	\N	\N	\N	\N	\N
22316	2009-08-03 16:32:23.903+01	\N	\N	\N	\N	\N
22317	2009-08-03 16:32:23.943+01	\N	2009-08-03 16:32:23.953+01	\N	\N	\N
22318	2009-08-03 16:32:24.021+01	\N	\N	\N	\N	\N
22319	2009-08-03 16:32:24.032+01	\N	\N	\N	\N	\N
22320	2009-08-03 16:32:24.072+01	\N	2009-08-03 16:32:24.083+01	\N	\N	\N
22321	2009-08-03 16:32:24.121+01	\N	\N	\N	\N	\N
22322	2009-08-03 16:32:24.132+01	\N	\N	\N	\N	\N
22323	2009-08-03 16:32:24.173+01	\N	2009-08-03 16:32:24.184+01	\N	\N	\N
22324	2009-08-03 16:32:24.273+01	\N	\N	\N	\N	\N
22325	2009-08-03 16:32:24.285+01	\N	\N	\N	\N	\N
22326	2009-08-03 16:32:24.325+01	\N	2009-08-03 16:32:24.336+01	\N	\N	\N
22327	2009-08-03 16:32:24.391+01	\N	\N	\N	\N	\N
22328	2009-08-03 16:32:24.403+01	\N	\N	\N	\N	\N
22329	2009-08-03 16:32:24.443+01	\N	2009-08-03 16:32:24.455+01	\N	\N	\N
22330	2009-08-03 16:32:24.519+01	\N	\N	\N	\N	\N
22331	2009-08-03 16:32:24.531+01	\N	\N	\N	\N	\N
22332	2009-08-03 16:32:24.582+01	\N	2009-08-03 16:32:24.593+01	\N	\N	\N
22333	2009-08-03 16:32:24.709+01	\N	\N	\N	\N	\N
22334	2009-08-03 16:32:24.724+01	\N	\N	\N	\N	\N
22335	2009-08-03 16:32:24.772+01	\N	2009-08-03 16:32:24.787+01	\N	\N	\N
22336	2009-08-03 16:32:24.872+01	\N	\N	\N	\N	\N
22337	2009-08-03 16:32:24.883+01	\N	\N	\N	\N	\N
22338	2009-08-03 16:32:24.926+01	\N	2009-08-03 16:32:24.938+01	\N	\N	\N
22339	2009-08-03 16:32:25.023+01	\N	\N	\N	\N	\N
22340	2009-08-03 16:32:25.035+01	\N	\N	\N	\N	\N
22341	2009-08-03 16:32:25.082+01	\N	2009-08-03 16:32:25.093+01	\N	\N	\N
22342	2009-08-03 16:32:25.152+01	\N	\N	\N	\N	\N
22343	2009-08-03 16:32:25.164+01	\N	\N	\N	\N	\N
22344	2009-08-03 16:32:25.206+01	\N	2009-08-03 16:32:25.218+01	\N	\N	\N
22345	2009-08-03 16:32:25.258+01	\N	\N	\N	\N	\N
22346	2009-08-03 16:32:25.269+01	\N	\N	\N	\N	\N
22347	2009-08-03 16:32:25.312+01	\N	2009-08-03 16:32:25.324+01	\N	\N	\N
22348	2009-08-03 16:32:25.365+01	\N	\N	\N	\N	\N
22349	2009-08-03 16:32:25.376+01	\N	\N	\N	\N	\N
22350	2009-08-03 16:32:25.419+01	\N	2009-08-03 16:32:25.432+01	\N	\N	\N
22351	2009-08-03 16:32:25.482+01	\N	\N	\N	\N	\N
22352	2009-08-03 16:32:25.504+01	\N	\N	\N	\N	\N
22353	2009-08-03 16:32:25.579+01	\N	2009-08-03 16:32:25.60+01	\N	\N	\N
22354	2009-08-03 16:32:25.723+01	\N	\N	\N	\N	\N
22355	2009-08-03 16:32:25.747+01	\N	\N	\N	\N	\N
22356	2009-08-03 16:32:25.804+01	\N	2009-08-03 16:32:25.816+01	\N	\N	\N
22357	2009-08-03 16:32:25.945+01	\N	\N	\N	\N	\N
22358	2009-08-03 16:32:25.958+01	\N	\N	\N	\N	\N
22359	2009-08-03 16:32:26.012+01	\N	2009-08-03 16:32:26.024+01	\N	\N	\N
22360	2009-08-03 16:32:26.156+01	\N	\N	\N	\N	\N
22361	2009-08-03 16:32:26.169+01	\N	\N	\N	\N	\N
22362	2009-08-03 16:32:26.214+01	\N	2009-08-03 16:32:26.227+01	\N	\N	\N
22363	2009-08-03 16:32:26.269+01	\N	\N	\N	\N	\N
22364	2009-08-03 16:32:26.282+01	\N	\N	\N	\N	\N
22365	2009-08-03 16:32:26.327+01	\N	2009-08-03 16:32:26.339+01	\N	\N	\N
22366	2009-08-03 16:32:26.433+01	\N	\N	\N	\N	\N
22367	2009-08-03 16:32:26.447+01	\N	\N	\N	\N	\N
22368	2009-08-03 16:32:26.505+01	\N	2009-08-03 16:32:26.519+01	\N	\N	\N
22369	2009-08-03 16:32:26.646+01	\N	\N	\N	\N	\N
22370	2009-08-03 16:32:26.661+01	\N	\N	\N	\N	\N
22371	2009-08-03 16:32:26.712+01	\N	2009-08-03 16:32:26.727+01	\N	\N	\N
22372	2009-08-03 16:32:26.822+01	\N	\N	\N	\N	\N
22373	2009-08-03 16:32:26.836+01	\N	\N	\N	\N	\N
22374	2009-08-03 16:32:26.886+01	\N	2009-08-03 16:32:26.899+01	\N	\N	\N
22375	2009-08-03 16:32:27.018+01	\N	\N	\N	\N	\N
22376	2009-08-03 16:32:27.033+01	\N	\N	\N	\N	\N
22377	2009-08-03 16:32:27.087+01	\N	2009-08-03 16:32:27.102+01	\N	\N	\N
22378	2009-08-03 16:32:27.142+01	\N	\N	\N	\N	\N
22379	2009-08-03 16:32:27.156+01	\N	\N	\N	\N	\N
22380	2009-08-03 16:32:27.221+01	\N	2009-08-03 16:32:27.244+01	\N	\N	\N
22381	2009-08-03 16:32:27.271+01	\N	\N	\N	\N	\N
22382	2009-08-03 16:32:27.286+01	\N	\N	\N	\N	\N
22383	2009-08-03 16:32:27.346+01	\N	2009-08-03 16:32:27.361+01	\N	\N	\N
22384	2009-08-03 16:32:27.477+01	\N	\N	\N	\N	\N
22385	2009-08-03 16:32:27.491+01	\N	\N	\N	\N	\N
22386	2009-08-03 16:32:27.541+01	\N	2009-08-03 16:32:27.555+01	\N	\N	\N
22387	2009-08-03 16:32:27.716+01	\N	\N	\N	\N	\N
22388	2009-08-03 16:32:27.732+01	\N	\N	\N	\N	\N
22389	2009-08-03 16:32:27.801+01	\N	2009-08-03 16:32:27.816+01	\N	\N	\N
22390	2009-08-03 16:32:27.915+01	\N	\N	\N	\N	\N
22391	2009-08-03 16:32:27.93+01	\N	\N	\N	\N	\N
22392	2009-08-03 16:32:27.984+01	\N	2009-08-03 16:32:27.999+01	\N	\N	\N
22393	2009-08-03 16:32:28.027+01	\N	\N	\N	\N	\N
22394	2009-08-03 16:32:28.041+01	\N	\N	\N	\N	\N
22395	2009-08-03 16:32:28.098+01	\N	2009-08-03 16:32:28.112+01	\N	\N	\N
22396	2009-08-03 16:32:28.188+01	\N	\N	\N	\N	\N
22397	2009-08-03 16:32:28.204+01	\N	\N	\N	\N	\N
22398	2009-08-03 16:32:28.258+01	\N	2009-08-03 16:32:28.29+01	\N	\N	\N
22399	2009-08-03 16:32:28.39+01	\N	\N	\N	\N	\N
22400	2009-08-03 16:32:28.405+01	\N	\N	\N	\N	\N
22401	2009-08-03 16:32:28.456+01	\N	2009-08-03 16:32:28.47+01	\N	\N	\N
22402	2009-08-03 16:32:28.506+01	\N	\N	\N	\N	\N
22403	2009-08-03 16:32:28.52+01	\N	\N	\N	\N	\N
22404	2009-08-03 16:32:28.57+01	\N	2009-08-03 16:32:28.586+01	\N	\N	\N
22405	2009-08-03 16:32:28.698+01	\N	\N	\N	\N	\N
22406	2009-08-03 16:32:28.713+01	\N	\N	\N	\N	\N
22407	2009-08-03 16:32:28.763+01	\N	2009-08-03 16:32:28.777+01	\N	\N	\N
22408	2009-08-03 16:32:28.872+01	\N	\N	\N	\N	\N
22409	2009-08-03 16:32:28.886+01	\N	\N	\N	\N	\N
22410	2009-08-03 16:32:28.937+01	\N	2009-08-03 16:32:28.951+01	\N	\N	\N
22411	2009-08-03 16:32:29.034+01	\N	\N	\N	\N	\N
22412	2009-08-03 16:32:29.048+01	\N	\N	\N	\N	\N
22413	2009-08-03 16:32:29.101+01	\N	2009-08-03 16:32:29.116+01	\N	\N	\N
22414	2009-08-03 16:32:29.211+01	\N	\N	\N	\N	\N
22415	2009-08-03 16:32:29.226+01	\N	\N	\N	\N	\N
22416	2009-08-03 16:32:29.289+01	\N	2009-08-03 16:32:29.303+01	\N	\N	\N
22417	2009-08-03 16:32:29.42+01	\N	\N	\N	\N	\N
22418	2009-08-03 16:32:29.435+01	\N	\N	\N	\N	\N
22419	2009-08-03 16:32:29.498+01	\N	2009-08-03 16:32:29.512+01	\N	\N	\N
22420	2009-08-03 16:32:29.602+01	\N	\N	\N	\N	\N
22421	2009-08-03 16:32:29.617+01	\N	\N	\N	\N	\N
22422	2009-08-03 16:32:29.669+01	\N	2009-08-03 16:32:29.684+01	\N	\N	\N
22423	2009-08-03 16:32:29.769+01	\N	\N	\N	\N	\N
22424	2009-08-03 16:32:29.784+01	\N	\N	\N	\N	\N
22425	2009-08-03 16:32:29.838+01	\N	2009-08-03 16:32:29.853+01	\N	\N	\N
22426	2009-08-03 16:32:29.892+01	\N	\N	\N	\N	\N
22427	2009-08-03 16:32:29.906+01	\N	\N	\N	\N	\N
22428	2009-08-03 16:32:29.961+01	\N	2009-08-03 16:32:29.976+01	\N	\N	\N
22429	2009-08-03 16:32:30.074+01	\N	\N	\N	\N	\N
22430	2009-08-03 16:32:30.091+01	\N	\N	\N	\N	\N
22431	2009-08-03 16:32:30.145+01	\N	2009-08-03 16:32:30.16+01	\N	\N	\N
22432	2009-08-03 16:32:30.252+01	\N	\N	\N	\N	\N
22433	2009-08-03 16:32:30.267+01	\N	\N	\N	\N	\N
22434	2009-08-03 16:32:30.324+01	\N	2009-08-03 16:32:30.338+01	\N	\N	\N
22435	2009-08-03 16:32:30.471+01	\N	\N	\N	\N	\N
22436	2009-08-03 16:32:30.487+01	\N	\N	\N	\N	\N
22437	2009-08-03 16:32:30.54+01	\N	2009-08-03 16:32:30.556+01	\N	\N	\N
22438	2009-08-03 16:32:30.585+01	\N	\N	\N	\N	\N
22439	2009-08-03 16:32:30.60+01	\N	\N	\N	\N	\N
22440	2009-08-03 16:32:30.655+01	\N	2009-08-03 16:32:30.67+01	\N	\N	\N
22441	2009-08-03 16:32:30.698+01	\N	\N	\N	\N	\N
22442	2009-08-03 16:32:30.713+01	\N	\N	\N	\N	\N
22443	2009-08-03 16:32:30.768+01	\N	2009-08-03 16:32:30.782+01	\N	\N	\N
22444	2009-08-03 16:32:30.859+01	\N	\N	\N	\N	\N
22445	2009-08-03 16:32:30.874+01	\N	\N	\N	\N	\N
22446	2009-08-03 16:32:30.925+01	\N	2009-08-03 16:32:30.939+01	\N	\N	\N
22447	2009-08-03 16:32:31.022+01	\N	\N	\N	\N	\N
22448	2009-08-03 16:32:31.037+01	\N	\N	\N	\N	\N
22449	2009-08-03 16:32:31.089+01	\N	2009-08-03 16:32:31.103+01	\N	\N	\N
22450	2009-08-03 16:32:31.18+01	\N	\N	\N	\N	\N
22451	2009-08-03 16:32:31.195+01	\N	\N	\N	\N	\N
22452	2009-08-03 16:32:31.25+01	\N	2009-08-03 16:32:31.267+01	\N	\N	\N
22453	2009-08-03 16:32:31.347+01	\N	\N	\N	\N	\N
22454	2009-08-03 16:32:31.363+01	\N	\N	\N	\N	\N
22455	2009-08-03 16:32:31.418+01	\N	2009-08-03 16:32:31.434+01	\N	\N	\N
22456	2009-08-03 16:32:31.548+01	\N	\N	\N	\N	\N
22457	2009-08-03 16:32:31.564+01	\N	\N	\N	\N	\N
22458	2009-08-03 16:32:31.619+01	\N	2009-08-03 16:32:31.635+01	\N	\N	\N
22459	2009-08-03 16:32:31.813+01	\N	\N	\N	\N	\N
22460	2009-08-03 16:32:31.831+01	\N	\N	\N	\N	\N
22461	2009-08-03 16:32:31.888+01	\N	2009-08-03 16:32:31.904+01	\N	\N	\N
22462	2009-08-03 16:32:31.995+01	\N	\N	\N	\N	\N
22463	2009-08-03 16:32:32.011+01	\N	\N	\N	\N	\N
22464	2009-08-03 16:32:32.079+01	\N	2009-08-03 16:32:32.095+01	\N	\N	\N
22465	2009-08-03 16:32:32.211+01	\N	\N	\N	\N	\N
22466	2009-08-03 16:32:32.228+01	\N	\N	\N	\N	\N
22467	2009-08-03 16:32:32.285+01	\N	2009-08-03 16:32:32.304+01	\N	\N	\N
22468	2009-08-03 16:32:32.383+01	\N	\N	\N	\N	\N
22469	2009-08-03 16:32:32.399+01	\N	\N	\N	\N	\N
22470	2009-08-03 16:32:32.468+01	\N	2009-08-03 16:32:32.484+01	\N	\N	\N
22471	2009-08-03 16:32:32.525+01	\N	\N	\N	\N	\N
22472	2009-08-03 16:32:32.555+01	\N	\N	\N	\N	\N
22473	2009-08-03 16:32:32.611+01	\N	2009-08-03 16:32:32.637+01	\N	\N	\N
22474	2009-08-03 16:32:32.767+01	\N	\N	\N	\N	\N
22475	2009-08-03 16:32:32.794+01	\N	\N	\N	\N	\N
22476	2009-08-03 16:32:32.85+01	\N	2009-08-03 16:32:32.864+01	\N	\N	\N
22477	2009-08-03 16:32:32.934+01	\N	\N	\N	\N	\N
22478	2009-08-03 16:32:32.949+01	\N	\N	\N	\N	\N
22479	2009-08-03 16:32:33.002+01	\N	2009-08-03 16:32:33.016+01	\N	\N	\N
22480	2009-08-03 16:32:33.089+01	\N	\N	\N	\N	\N
22481	2009-08-03 16:32:33.103+01	\N	\N	\N	\N	\N
22482	2009-08-03 16:32:33.162+01	\N	2009-08-03 16:32:33.178+01	\N	\N	\N
22483	2009-08-03 16:32:33.256+01	\N	\N	\N	\N	\N
22484	2009-08-03 16:32:33.272+01	\N	\N	\N	\N	\N
22485	2009-08-03 16:32:33.361+01	\N	2009-08-03 16:32:33.376+01	\N	\N	\N
22486	2009-08-03 16:32:33.468+01	\N	\N	\N	\N	\N
22487	2009-08-03 16:32:33.484+01	\N	\N	\N	\N	\N
22488	2009-08-03 16:32:33.541+01	\N	2009-08-03 16:32:33.562+01	\N	\N	\N
22489	2009-08-03 16:32:33.593+01	\N	\N	\N	\N	\N
22490	2009-08-03 16:32:33.612+01	\N	\N	\N	\N	\N
22491	2009-08-03 16:32:33.67+01	\N	2009-08-03 16:32:33.686+01	\N	\N	\N
22492	2009-08-03 16:32:33.715+01	\N	\N	\N	\N	\N
22493	2009-08-03 16:32:33.73+01	\N	\N	\N	\N	\N
22494	2009-08-03 16:32:33.788+01	\N	2009-08-03 16:32:33.81+01	\N	\N	\N
22495	2009-08-03 16:32:33.841+01	\N	\N	\N	\N	\N
22496	2009-08-03 16:32:33.856+01	\N	\N	\N	\N	\N
22497	2009-08-03 16:32:33.914+01	\N	2009-08-03 16:32:33.93+01	\N	\N	\N
22498	2009-08-03 16:32:34.089+01	\N	\N	\N	\N	\N
22499	2009-08-03 16:32:34.106+01	\N	\N	\N	\N	\N
22500	2009-08-03 16:32:34.176+01	\N	2009-08-03 16:32:34.192+01	\N	\N	\N
22501	2009-08-03 16:32:34.286+01	\N	\N	\N	\N	\N
22502	2009-08-03 16:32:34.302+01	\N	\N	\N	\N	\N
22503	2009-08-03 16:32:34.361+01	\N	2009-08-03 16:32:34.377+01	\N	\N	\N
22504	2009-08-03 16:32:34.42+01	\N	\N	\N	\N	\N
22505	2009-08-03 16:32:34.436+01	\N	\N	\N	\N	\N
22506	2009-08-03 16:32:34.495+01	\N	2009-08-03 16:32:34.512+01	\N	\N	\N
22507	2009-08-03 16:32:34.599+01	\N	\N	\N	\N	\N
22508	2009-08-03 16:32:34.616+01	\N	\N	\N	\N	\N
22509	2009-08-03 16:32:34.676+01	\N	2009-08-03 16:32:34.693+01	\N	\N	\N
22510	2009-08-03 16:32:34.725+01	\N	\N	\N	\N	\N
22511	2009-08-03 16:32:34.741+01	\N	\N	\N	\N	\N
22512	2009-08-03 16:32:34.802+01	\N	2009-08-03 16:32:34.819+01	\N	\N	\N
22513	2009-08-03 16:32:34.85+01	\N	\N	\N	\N	\N
22514	2009-08-03 16:32:34.866+01	\N	\N	\N	\N	\N
22515	2009-08-03 16:32:34.936+01	\N	2009-08-03 16:32:34.952+01	\N	\N	\N
22516	2009-08-03 16:32:34.981+01	\N	\N	\N	\N	\N
22517	2009-08-03 16:32:34.997+01	\N	\N	\N	\N	\N
22518	2009-08-03 16:32:35.096+01	\N	2009-08-03 16:32:35.113+01	\N	\N	\N
16152	2009-08-03 16:31:43.272+01	\N	2009-08-03 16:32:35.204+01	\N	\N	\N
22519	2009-08-03 16:32:35.253+01	\N	2009-08-03 16:32:35.269+01	\N	\N	\N
22520	2009-08-03 16:32:35.298+01	\N	\N	\N	\N	\N
22521	2009-08-03 16:32:35.314+01	\N	\N	\N	\N	\N
22522	2009-08-03 16:32:35.374+01	\N	2009-08-03 16:32:35.391+01	\N	\N	\N
22523	2009-08-03 16:32:35.561+01	\N	\N	\N	\N	\N
22524	2009-08-03 16:32:35.581+01	\N	\N	\N	\N	\N
22525	2009-08-03 16:32:35.655+01	\N	2009-08-03 16:32:35.671+01	\N	\N	\N
22526	2009-08-03 16:32:35.738+01	\N	\N	\N	\N	\N
22527	2009-08-03 16:32:35.754+01	\N	\N	\N	\N	\N
22528	2009-08-03 16:32:35.823+01	\N	2009-08-03 16:32:35.844+01	\N	\N	\N
22529	2009-08-03 16:32:35.873+01	\N	\N	\N	\N	\N
22530	2009-08-03 16:32:35.889+01	\N	\N	\N	\N	\N
22531	2009-08-03 16:32:35.946+01	\N	2009-08-03 16:32:35.962+01	\N	\N	\N
22532	2009-08-03 16:32:36.083+01	\N	\N	\N	\N	\N
22533	2009-08-03 16:32:36.101+01	\N	\N	\N	\N	\N
22534	2009-08-03 16:32:36.163+01	\N	2009-08-03 16:32:36.18+01	\N	\N	\N
22535	2009-08-03 16:32:36.213+01	\N	\N	\N	\N	\N
22536	2009-08-03 16:32:36.232+01	\N	\N	\N	\N	\N
22537	2009-08-03 16:32:36.30+01	\N	2009-08-03 16:32:36.318+01	\N	\N	\N
22538	2009-08-03 16:32:36.357+01	\N	\N	\N	\N	\N
22539	2009-08-03 16:32:36.374+01	\N	\N	\N	\N	\N
22540	2009-08-03 16:32:36.456+01	\N	2009-08-03 16:32:36.474+01	\N	\N	\N
22541	2009-08-03 16:32:36.536+01	\N	\N	\N	\N	\N
22542	2009-08-03 16:32:36.554+01	\N	\N	\N	\N	\N
22543	2009-08-03 16:32:36.641+01	\N	2009-08-03 16:32:36.671+01	\N	\N	\N
22544	2009-08-03 16:32:36.717+01	\N	\N	\N	\N	\N
22545	2009-08-03 16:32:36.736+01	\N	\N	\N	\N	\N
22546	2009-08-03 16:32:36.803+01	\N	2009-08-03 16:32:36.821+01	\N	\N	\N
22547	2009-08-03 16:32:36.938+01	\N	\N	\N	\N	\N
22548	2009-08-03 16:32:36.956+01	\N	\N	\N	\N	\N
22549	2009-08-03 16:32:37.02+01	\N	2009-08-03 16:32:37.037+01	\N	\N	\N
22550	2009-08-03 16:32:37.199+01	\N	\N	\N	\N	\N
22551	2009-08-03 16:32:37.217+01	\N	\N	\N	\N	\N
22552	2009-08-03 16:32:37.279+01	\N	2009-08-03 16:32:37.297+01	\N	\N	\N
22553	2009-08-03 16:32:37.527+01	\N	\N	\N	\N	\N
22554	2009-08-03 16:32:37.545+01	\N	\N	\N	\N	\N
22555	2009-08-03 16:32:37.611+01	\N	2009-08-03 16:32:37.629+01	\N	\N	\N
22556	2009-08-03 16:32:37.663+01	\N	\N	\N	\N	\N
22557	2009-08-03 16:32:37.68+01	\N	\N	\N	\N	\N
22558	2009-08-03 16:32:37.753+01	\N	2009-08-03 16:32:37.77+01	\N	\N	\N
22559	2009-08-03 16:32:37.803+01	\N	\N	\N	\N	\N
22560	2009-08-03 16:32:37.821+01	\N	\N	\N	\N	\N
22561	2009-08-03 16:32:37.90+01	\N	2009-08-03 16:32:37.918+01	\N	\N	\N
22562	2009-08-03 16:32:37.951+01	\N	\N	\N	\N	\N
22563	2009-08-03 16:32:37.968+01	\N	\N	\N	\N	\N
22564	2009-08-03 16:32:38.046+01	\N	2009-08-03 16:32:38.068+01	\N	\N	\N
22565	2009-08-03 16:32:38.103+01	\N	\N	\N	\N	\N
22566	2009-08-03 16:32:38.12+01	\N	\N	\N	\N	\N
22567	2009-08-03 16:32:38.185+01	\N	2009-08-03 16:32:38.202+01	\N	\N	\N
22568	2009-08-03 16:32:38.351+01	\N	\N	\N	\N	\N
22569	2009-08-03 16:32:38.369+01	\N	\N	\N	\N	\N
22570	2009-08-03 16:32:38.434+01	\N	2009-08-03 16:32:38.452+01	\N	\N	\N
22571	2009-08-03 16:32:38.559+01	\N	\N	\N	\N	\N
22572	2009-08-03 16:32:38.58+01	\N	\N	\N	\N	\N
22573	2009-08-03 16:32:38.65+01	\N	2009-08-03 16:32:38.674+01	\N	\N	\N
22574	2009-08-03 16:32:38.783+01	\N	\N	\N	\N	\N
22575	2009-08-03 16:32:38.802+01	\N	\N	\N	\N	\N
22576	2009-08-03 16:32:38.87+01	\N	2009-08-03 16:32:38.889+01	\N	\N	\N
22577	2009-08-03 16:32:39.015+01	\N	\N	\N	\N	\N
22578	2009-08-03 16:32:39.035+01	\N	\N	\N	\N	\N
22579	2009-08-03 16:32:39.119+01	\N	2009-08-03 16:32:39.139+01	\N	\N	\N
22580	2009-08-03 16:32:39.174+01	\N	\N	\N	\N	\N
22581	2009-08-03 16:32:39.193+01	\N	\N	\N	\N	\N
22582	2009-08-03 16:32:39.275+01	\N	2009-08-03 16:32:39.294+01	\N	\N	\N
22583	2009-08-03 16:32:39.33+01	\N	\N	\N	\N	\N
22584	2009-08-03 16:32:39.348+01	\N	\N	\N	\N	\N
22585	2009-08-03 16:32:39.417+01	\N	2009-08-03 16:32:39.436+01	\N	\N	\N
22586	2009-08-03 16:32:39.532+01	\N	\N	\N	\N	\N
22587	2009-08-03 16:32:39.553+01	\N	\N	\N	\N	\N
22588	2009-08-03 16:32:39.624+01	\N	2009-08-03 16:32:39.643+01	\N	\N	\N
22589	2009-08-03 16:32:39.686+01	\N	\N	\N	\N	\N
22590	2009-08-03 16:32:39.706+01	\N	\N	\N	\N	\N
22591	2009-08-03 16:32:39.775+01	\N	2009-08-03 16:32:39.795+01	\N	\N	\N
22592	2009-08-03 16:32:39.831+01	\N	\N	\N	\N	\N
22593	2009-08-03 16:32:39.85+01	\N	\N	\N	\N	\N
22594	2009-08-03 16:32:39.936+01	\N	2009-08-03 16:32:39.955+01	\N	\N	\N
22595	2009-08-03 16:32:39.992+01	\N	\N	\N	\N	\N
22596	2009-08-03 16:32:40.011+01	\N	\N	\N	\N	\N
22597	2009-08-03 16:32:40.098+01	\N	2009-08-03 16:32:40.118+01	\N	\N	\N
22598	2009-08-03 16:32:40.298+01	\N	\N	\N	\N	\N
22599	2009-08-03 16:32:40.318+01	\N	\N	\N	\N	\N
22600	2009-08-03 16:32:40.389+01	\N	2009-08-03 16:32:40.409+01	\N	\N	\N
22601	2009-08-03 16:32:40.477+01	\N	\N	\N	\N	\N
22602	2009-08-03 16:32:40.497+01	\N	\N	\N	\N	\N
22603	2009-08-03 16:32:40.587+01	\N	2009-08-03 16:32:40.634+01	\N	\N	\N
22604	2009-08-03 16:32:40.747+01	\N	\N	\N	\N	\N
22605	2009-08-03 16:32:40.781+01	\N	\N	\N	\N	\N
22606	2009-08-03 16:32:40.888+01	\N	2009-08-03 16:32:40.906+01	\N	\N	\N
22607	2009-08-03 16:32:40.953+01	\N	\N	\N	\N	\N
22608	2009-08-03 16:32:40.97+01	\N	\N	\N	\N	\N
22609	2009-08-03 16:32:41.035+01	\N	2009-08-03 16:32:41.054+01	\N	\N	\N
22610	2009-08-03 16:32:41.09+01	\N	\N	\N	\N	\N
22611	2009-08-03 16:32:41.107+01	\N	\N	\N	\N	\N
22612	2009-08-03 16:32:41.187+01	\N	2009-08-03 16:32:41.205+01	\N	\N	\N
22613	2009-08-03 16:32:41.297+01	\N	\N	\N	\N	\N
22614	2009-08-03 16:32:41.315+01	\N	\N	\N	\N	\N
22615	2009-08-03 16:32:41.38+01	\N	2009-08-03 16:32:41.399+01	\N	\N	\N
22616	2009-08-03 16:32:41.45+01	\N	\N	\N	\N	\N
22617	2009-08-03 16:32:41.469+01	\N	\N	\N	\N	\N
22618	2009-08-03 16:32:41.536+01	\N	2009-08-03 16:32:41.556+01	\N	\N	\N
22619	2009-08-03 16:32:41.625+01	\N	\N	\N	\N	\N
22620	2009-08-03 16:32:41.645+01	\N	\N	\N	\N	\N
22621	2009-08-03 16:32:41.714+01	\N	2009-08-03 16:32:41.734+01	\N	\N	\N
22622	2009-08-03 16:32:41.862+01	\N	\N	\N	\N	\N
22623	2009-08-03 16:32:41.883+01	\N	\N	\N	\N	\N
22624	2009-08-03 16:32:41.953+01	\N	2009-08-03 16:32:41.972+01	\N	\N	\N
22625	2009-08-03 16:32:42.07+01	\N	\N	\N	\N	\N
22626	2009-08-03 16:32:42.092+01	\N	\N	\N	\N	\N
22627	2009-08-03 16:32:42.162+01	\N	2009-08-03 16:32:42.182+01	\N	\N	\N
22628	2009-08-03 16:32:42.28+01	\N	\N	\N	\N	\N
22629	2009-08-03 16:32:42.30+01	\N	\N	\N	\N	\N
22630	2009-08-03 16:32:42.37+01	\N	2009-08-03 16:32:42.391+01	\N	\N	\N
22631	2009-08-03 16:32:42.491+01	\N	\N	\N	\N	\N
22632	2009-08-03 16:32:42.512+01	\N	\N	\N	\N	\N
22633	2009-08-03 16:32:42.585+01	\N	2009-08-03 16:32:42.606+01	\N	\N	\N
22634	2009-08-03 16:32:42.691+01	\N	\N	\N	\N	\N
22635	2009-08-03 16:32:42.711+01	\N	\N	\N	\N	\N
22636	2009-08-03 16:32:42.783+01	\N	2009-08-03 16:32:42.806+01	\N	\N	\N
22637	2009-08-03 16:32:42.843+01	\N	\N	\N	\N	\N
22638	2009-08-03 16:32:42.863+01	\N	\N	\N	\N	\N
22639	2009-08-03 16:32:42.936+01	\N	2009-08-03 16:32:42.957+01	\N	\N	\N
22640	2009-08-03 16:32:43.044+01	\N	\N	\N	\N	\N
22641	2009-08-03 16:32:43.065+01	\N	\N	\N	\N	\N
22642	2009-08-03 16:32:43.17+01	\N	2009-08-03 16:32:43.191+01	\N	\N	\N
22643	2009-08-03 16:32:43.229+01	\N	\N	\N	\N	\N
22644	2009-08-03 16:32:43.25+01	\N	\N	\N	\N	\N
22645	2009-08-03 16:32:43.323+01	\N	2009-08-03 16:32:43.344+01	\N	\N	\N
22646	2009-08-03 16:32:43.399+01	\N	\N	\N	\N	\N
22647	2009-08-03 16:32:43.419+01	\N	\N	\N	\N	\N
22648	2009-08-03 16:32:43.509+01	\N	2009-08-03 16:32:43.53+01	\N	\N	\N
22649	2009-08-03 16:32:43.618+01	\N	\N	\N	\N	\N
22650	2009-08-03 16:32:43.639+01	\N	\N	\N	\N	\N
22651	2009-08-03 16:32:43.73+01	\N	2009-08-03 16:32:43.75+01	\N	\N	\N
22652	2009-08-03 16:32:43.872+01	\N	\N	\N	\N	\N
22653	2009-08-03 16:32:43.895+01	\N	\N	\N	\N	\N
22654	2009-08-03 16:32:43.986+01	\N	2009-08-03 16:32:44.007+01	\N	\N	\N
22655	2009-08-03 16:32:44.08+01	\N	\N	\N	\N	\N
22656	2009-08-03 16:32:44.101+01	\N	\N	\N	\N	\N
22657	2009-08-03 16:32:44.185+01	\N	2009-08-03 16:32:44.206+01	\N	\N	\N
22658	2009-08-03 16:32:44.257+01	\N	\N	\N	\N	\N
22659	2009-08-03 16:32:44.277+01	\N	\N	\N	\N	\N
22660	2009-08-03 16:32:44.35+01	\N	2009-08-03 16:32:44.371+01	\N	\N	\N
22661	2009-08-03 16:32:44.54+01	\N	\N	\N	\N	\N
22662	2009-08-03 16:32:44.561+01	\N	\N	\N	\N	\N
22663	2009-08-03 16:32:44.636+01	\N	2009-08-03 16:32:44.657+01	\N	\N	\N
22664	2009-08-03 16:32:44.841+01	\N	\N	\N	\N	\N
22665	2009-08-03 16:32:44.877+01	\N	\N	\N	\N	\N
22666	2009-08-03 16:32:44.953+01	\N	2009-08-03 16:32:44.972+01	\N	\N	\N
22667	2009-08-03 16:32:45.197+01	\N	\N	\N	\N	\N
22668	2009-08-03 16:32:45.218+01	\N	\N	\N	\N	\N
22669	2009-08-03 16:32:45.292+01	\N	2009-08-03 16:32:45.316+01	\N	\N	\N
22670	2009-08-03 16:32:45.444+01	\N	\N	\N	\N	\N
22671	2009-08-03 16:32:45.465+01	\N	\N	\N	\N	\N
22672	2009-08-03 16:32:45.539+01	\N	2009-08-03 16:32:45.56+01	\N	\N	\N
22673	2009-08-03 16:32:45.666+01	\N	\N	\N	\N	\N
22674	2009-08-03 16:32:45.687+01	\N	\N	\N	\N	\N
22675	2009-08-03 16:32:45.761+01	\N	2009-08-03 16:32:45.782+01	\N	\N	\N
22676	2009-08-03 16:32:45.956+01	\N	\N	\N	\N	\N
22677	2009-08-03 16:32:45.977+01	\N	\N	\N	\N	\N
22678	2009-08-03 16:32:46.069+01	\N	2009-08-03 16:32:46.091+01	\N	\N	\N
22679	2009-08-03 16:32:46.251+01	\N	\N	\N	\N	\N
22680	2009-08-03 16:32:46.273+01	\N	\N	\N	\N	\N
22681	2009-08-03 16:32:46.366+01	\N	2009-08-03 16:32:46.388+01	\N	\N	\N
22682	2009-08-03 16:32:46.572+01	\N	\N	\N	\N	\N
22683	2009-08-03 16:32:46.618+01	\N	\N	\N	\N	\N
22684	2009-08-03 16:32:46.748+01	\N	2009-08-03 16:32:46.785+01	\N	\N	\N
22685	2009-08-03 16:32:46.919+01	\N	\N	\N	\N	\N
22686	2009-08-03 16:32:46.941+01	\N	\N	\N	\N	\N
22687	2009-08-03 16:32:47.031+01	\N	2009-08-03 16:32:47.056+01	\N	\N	\N
22688	2009-08-03 16:32:47.199+01	\N	\N	\N	\N	\N
22689	2009-08-03 16:32:47.221+01	\N	\N	\N	\N	\N
22690	2009-08-03 16:32:47.321+01	\N	2009-08-03 16:32:47.344+01	\N	\N	\N
22691	2009-08-03 16:32:47.386+01	\N	\N	\N	\N	\N
22692	2009-08-03 16:32:47.408+01	\N	\N	\N	\N	\N
22693	2009-08-03 16:32:47.51+01	\N	2009-08-03 16:32:47.532+01	\N	\N	\N
22694	2009-08-03 16:32:47.683+01	\N	\N	\N	\N	\N
22695	2009-08-03 16:32:47.71+01	\N	\N	\N	\N	\N
22696	2009-08-03 16:32:47.794+01	\N	2009-08-03 16:32:47.817+01	\N	\N	\N
22697	2009-08-03 16:32:47.949+01	\N	\N	\N	\N	\N
22698	2009-08-03 16:32:47.985+01	\N	\N	\N	\N	\N
22699	2009-08-03 16:32:48.155+01	\N	2009-08-03 16:32:48.196+01	\N	\N	\N
22700	2009-08-03 16:32:48.346+01	\N	\N	\N	\N	\N
22701	2009-08-03 16:32:48.445+01	\N	\N	\N	\N	\N
22702	2009-08-03 16:32:48.535+01	\N	2009-08-03 16:32:48.555+01	\N	\N	\N
22703	2009-08-03 16:32:48.773+01	\N	\N	\N	\N	\N
22704	2009-08-03 16:32:48.796+01	\N	\N	\N	\N	\N
22705	2009-08-03 16:32:48.873+01	\N	2009-08-03 16:32:48.894+01	\N	\N	\N
22706	2009-08-03 16:32:49.025+01	\N	\N	\N	\N	\N
22707	2009-08-03 16:32:49.048+01	\N	\N	\N	\N	\N
22708	2009-08-03 16:32:49.126+01	\N	2009-08-03 16:32:49.148+01	\N	\N	\N
22709	2009-08-03 16:32:49.189+01	\N	\N	\N	\N	\N
22710	2009-08-03 16:32:49.21+01	\N	\N	\N	\N	\N
22711	2009-08-03 16:32:49.289+01	\N	2009-08-03 16:32:49.311+01	\N	\N	\N
22712	2009-08-03 16:32:49.37+01	\N	\N	\N	\N	\N
22713	2009-08-03 16:32:49.391+01	\N	\N	\N	\N	\N
22714	2009-08-03 16:32:49.49+01	\N	2009-08-03 16:32:49.512+01	\N	\N	\N
22715	2009-08-03 16:32:49.553+01	\N	\N	\N	\N	\N
22716	2009-08-03 16:32:49.575+01	\N	\N	\N	\N	\N
22717	2009-08-03 16:32:49.653+01	\N	2009-08-03 16:32:49.676+01	\N	\N	\N
22718	2009-08-03 16:32:49.77+01	\N	\N	\N	\N	\N
22719	2009-08-03 16:32:49.792+01	\N	\N	\N	\N	\N
22720	2009-08-03 16:32:49.889+01	\N	2009-08-03 16:32:49.915+01	\N	\N	\N
22721	2009-08-03 16:32:50.174+01	\N	\N	\N	\N	\N
22722	2009-08-03 16:32:50.198+01	\N	\N	\N	\N	\N
22723	2009-08-03 16:32:50.296+01	\N	2009-08-03 16:32:50.323+01	\N	\N	\N
22724	2009-08-03 16:32:50.383+01	\N	\N	\N	\N	\N
22725	2009-08-03 16:32:50.406+01	\N	\N	\N	\N	\N
22726	2009-08-03 16:32:50.487+01	\N	2009-08-03 16:32:50.51+01	\N	\N	\N
22727	2009-08-03 16:32:50.607+01	\N	\N	\N	\N	\N
22728	2009-08-03 16:32:50.63+01	\N	\N	\N	\N	\N
22729	2009-08-03 16:32:50.712+01	\N	2009-08-03 16:32:50.735+01	\N	\N	\N
22730	2009-08-03 16:32:50.814+01	\N	\N	\N	\N	\N
22731	2009-08-03 16:32:50.837+01	\N	\N	\N	\N	\N
22732	2009-08-03 16:32:50.922+01	\N	2009-08-03 16:32:50.945+01	\N	\N	\N
22733	2009-08-03 16:32:51.025+01	\N	\N	\N	\N	\N
22734	2009-08-03 16:32:51.048+01	\N	\N	\N	\N	\N
22735	2009-08-03 16:32:51.131+01	\N	2009-08-03 16:32:51.154+01	\N	\N	\N
22736	2009-08-03 16:32:51.234+01	\N	\N	\N	\N	\N
22737	2009-08-03 16:32:51.257+01	\N	\N	\N	\N	\N
22738	2009-08-03 16:32:51.368+01	\N	2009-08-03 16:32:51.392+01	\N	\N	\N
22739	2009-08-03 16:32:51.556+01	\N	\N	\N	\N	\N
22740	2009-08-03 16:32:51.582+01	\N	\N	\N	\N	\N
22741	2009-08-03 16:32:51.682+01	\N	2009-08-03 16:32:51.704+01	\N	\N	\N
22742	2009-08-03 16:32:51.82+01	\N	\N	\N	\N	\N
22743	2009-08-03 16:32:51.844+01	\N	\N	\N	\N	\N
22744	2009-08-03 16:32:51.927+01	\N	2009-08-03 16:32:51.95+01	\N	\N	\N
22745	2009-08-03 16:32:52.01+01	\N	\N	\N	\N	\N
22746	2009-08-03 16:32:52.033+01	\N	\N	\N	\N	\N
22747	2009-08-03 16:32:52.115+01	\N	2009-08-03 16:32:52.137+01	\N	\N	\N
22748	2009-08-03 16:32:52.327+01	\N	\N	\N	\N	\N
22749	2009-08-03 16:32:52.351+01	\N	\N	\N	\N	\N
22750	2009-08-03 16:32:52.433+01	\N	2009-08-03 16:32:52.457+01	\N	\N	\N
22751	2009-08-03 16:32:52.50+01	\N	\N	\N	\N	\N
22752	2009-08-03 16:32:52.523+01	\N	\N	\N	\N	\N
22753	2009-08-03 16:32:52.606+01	\N	2009-08-03 16:32:52.629+01	\N	\N	\N
22754	2009-08-03 16:32:52.69+01	\N	\N	\N	\N	\N
22755	2009-08-03 16:32:52.714+01	\N	\N	\N	\N	\N
22756	2009-08-03 16:32:52.815+01	\N	2009-08-03 16:32:52.837+01	\N	\N	\N
22757	2009-08-03 16:32:52.956+01	\N	\N	\N	\N	\N
22758	2009-08-03 16:32:52.98+01	\N	\N	\N	\N	\N
22759	2009-08-03 16:32:53.063+01	\N	2009-08-03 16:32:53.086+01	\N	\N	\N
22760	2009-08-03 16:32:53.261+01	\N	\N	\N	\N	\N
22761	2009-08-03 16:32:53.285+01	\N	\N	\N	\N	\N
22762	2009-08-03 16:32:53.37+01	\N	2009-08-03 16:32:53.394+01	\N	\N	\N
22763	2009-08-03 16:32:53.553+01	\N	\N	\N	\N	\N
22764	2009-08-03 16:32:53.577+01	\N	\N	\N	\N	\N
22765	2009-08-03 16:32:53.682+01	\N	2009-08-03 16:32:53.706+01	\N	\N	\N
22766	2009-08-03 16:32:53.906+01	\N	\N	\N	\N	\N
22767	2009-08-03 16:32:53.931+01	\N	\N	\N	\N	\N
22768	2009-08-03 16:32:54.036+01	\N	2009-08-03 16:32:54.06+01	\N	\N	\N
22769	2009-08-03 16:32:54.267+01	\N	\N	\N	\N	\N
22770	2009-08-03 16:32:54.292+01	\N	\N	\N	\N	\N
22771	2009-08-03 16:32:54.378+01	\N	2009-08-03 16:32:54.402+01	\N	\N	\N
22772	2009-08-03 16:32:54.492+01	\N	\N	\N	\N	\N
22773	2009-08-03 16:32:54.517+01	\N	\N	\N	\N	\N
22774	2009-08-03 16:32:54.603+01	\N	2009-08-03 16:32:54.627+01	\N	\N	\N
22775	2009-08-03 16:32:54.767+01	\N	\N	\N	\N	\N
22776	2009-08-03 16:32:54.791+01	\N	\N	\N	\N	\N
22777	2009-08-03 16:32:54.877+01	\N	2009-08-03 16:32:54.901+01	\N	\N	\N
22778	2009-08-03 16:32:54.966+01	\N	\N	\N	\N	\N
22779	2009-08-03 16:32:54.99+01	\N	\N	\N	\N	\N
22780	2009-08-03 16:32:55.076+01	\N	2009-08-03 16:32:55.099+01	\N	\N	\N
22781	2009-08-03 16:32:55.181+01	\N	\N	\N	\N	\N
22782	2009-08-03 16:32:55.205+01	\N	\N	\N	\N	\N
22783	2009-08-03 16:32:55.31+01	\N	2009-08-03 16:32:55.334+01	\N	\N	\N
22784	2009-08-03 16:32:55.392+01	\N	\N	\N	\N	\N
22785	2009-08-03 16:32:55.415+01	\N	\N	\N	\N	\N
22786	2009-08-03 16:32:55.52+01	\N	2009-08-03 16:32:55.544+01	\N	\N	\N
22787	2009-08-03 16:32:55.631+01	\N	\N	\N	\N	\N
22788	2009-08-03 16:32:55.656+01	\N	\N	\N	\N	\N
22789	2009-08-03 16:32:55.742+01	\N	2009-08-03 16:32:55.767+01	\N	\N	\N
22790	2009-08-03 16:32:55.87+01	\N	\N	\N	\N	\N
22791	2009-08-03 16:32:55.894+01	\N	\N	\N	\N	\N
22792	2009-08-03 16:32:55.98+01	\N	2009-08-03 16:32:56.004+01	\N	\N	\N
22793	2009-08-03 16:32:56.185+01	\N	\N	\N	\N	\N
22794	2009-08-03 16:32:56.21+01	\N	\N	\N	\N	\N
22795	2009-08-03 16:32:56.42+01	\N	2009-08-03 16:32:56.463+01	\N	\N	\N
22796	2009-08-03 16:32:56.621+01	\N	\N	\N	\N	\N
22797	2009-08-03 16:32:56.647+01	\N	\N	\N	\N	\N
22798	2009-08-03 16:32:56.795+01	\N	2009-08-03 16:32:56.823+01	\N	\N	\N
22799	2009-08-03 16:32:56.875+01	\N	\N	\N	\N	\N
22800	2009-08-03 16:32:56.903+01	\N	\N	\N	\N	\N
22801	2009-08-03 16:32:57.045+01	\N	2009-08-03 16:32:57.07+01	\N	\N	\N
22802	2009-08-03 16:32:57.117+01	\N	\N	\N	\N	\N
22803	2009-08-03 16:32:57.143+01	\N	\N	\N	\N	\N
22804	2009-08-03 16:32:57.272+01	\N	2009-08-03 16:32:57.30+01	\N	\N	\N
22805	2009-08-03 16:32:57.387+01	\N	\N	\N	\N	\N
22806	2009-08-03 16:32:57.412+01	\N	\N	\N	\N	\N
22807	2009-08-03 16:32:57.543+01	\N	2009-08-03 16:32:57.568+01	\N	\N	\N
22808	2009-08-03 16:32:57.615+01	\N	\N	\N	\N	\N
22809	2009-08-03 16:32:57.64+01	\N	\N	\N	\N	\N
22810	2009-08-03 16:32:57.751+01	\N	2009-08-03 16:32:57.777+01	\N	\N	\N
22811	2009-08-03 16:32:57.865+01	\N	\N	\N	\N	\N
22812	2009-08-03 16:32:57.90+01	\N	\N	\N	\N	\N
22813	2009-08-03 16:32:58.009+01	\N	2009-08-03 16:32:58.035+01	\N	\N	\N
22814	2009-08-03 16:32:58.10+01	\N	\N	\N	\N	\N
22815	2009-08-03 16:32:58.145+01	\N	\N	\N	\N	\N
22816	2009-08-03 16:32:58.348+01	\N	2009-08-03 16:32:58.371+01	\N	\N	\N
22817	2009-08-03 16:32:58.534+01	\N	\N	\N	\N	\N
22818	2009-08-03 16:32:58.559+01	\N	\N	\N	\N	\N
22819	2009-08-03 16:32:58.671+01	\N	2009-08-03 16:32:58.697+01	\N	\N	\N
22820	2009-08-03 16:32:58.809+01	\N	\N	\N	\N	\N
22821	2009-08-03 16:32:58.836+01	\N	\N	\N	\N	\N
22822	2009-08-03 16:32:58.953+01	\N	2009-08-03 16:32:58.979+01	\N	\N	\N
22823	2009-08-03 16:32:59.159+01	\N	\N	\N	\N	\N
22824	2009-08-03 16:32:59.186+01	\N	\N	\N	\N	\N
22825	2009-08-03 16:32:59.281+01	\N	2009-08-03 16:32:59.307+01	\N	\N	\N
22826	2009-08-03 16:32:59.509+01	\N	\N	\N	\N	\N
22827	2009-08-03 16:32:59.536+01	\N	\N	\N	\N	\N
22828	2009-08-03 16:32:59.654+01	\N	2009-08-03 16:32:59.681+01	\N	\N	\N
22829	2009-08-03 16:32:59.752+01	\N	\N	\N	\N	\N
22830	2009-08-03 16:32:59.783+01	\N	\N	\N	\N	\N
22831	2009-08-03 16:32:59.955+01	\N	2009-08-03 16:33:00.005+01	\N	\N	\N
22832	2009-08-03 16:33:00.059+01	\N	\N	\N	\N	\N
22833	2009-08-03 16:33:00.083+01	\N	\N	\N	\N	\N
22834	2009-08-03 16:33:00.189+01	\N	2009-08-03 16:33:00.213+01	\N	\N	\N
22835	2009-08-03 16:33:00.257+01	\N	\N	\N	\N	\N
22836	2009-08-03 16:33:00.281+01	\N	\N	\N	\N	\N
22837	2009-08-03 16:33:00.396+01	\N	2009-08-03 16:33:00.423+01	\N	\N	\N
22838	2009-08-03 16:33:00.513+01	\N	\N	\N	\N	\N
22839	2009-08-03 16:33:00.539+01	\N	\N	\N	\N	\N
22840	2009-08-03 16:33:00.631+01	\N	2009-08-03 16:33:00.657+01	\N	\N	\N
22841	2009-08-03 16:33:00.79+01	\N	\N	\N	\N	\N
22842	2009-08-03 16:33:00.817+01	\N	\N	\N	\N	\N
22843	2009-08-03 16:33:00.911+01	\N	2009-08-03 16:33:00.937+01	\N	\N	\N
22844	2009-08-03 16:33:01.204+01	\N	\N	\N	\N	\N
22845	2009-08-03 16:33:01.23+01	\N	\N	\N	\N	\N
22846	2009-08-03 16:33:01.322+01	\N	2009-08-03 16:33:01.348+01	\N	\N	\N
22847	2009-08-03 16:33:01.668+01	\N	\N	\N	\N	\N
22848	2009-08-03 16:33:01.695+01	\N	\N	\N	\N	\N
22849	2009-08-03 16:33:01.785+01	\N	2009-08-03 16:33:01.811+01	\N	\N	\N
22850	2009-08-03 16:33:02.112+01	\N	\N	\N	\N	\N
22851	2009-08-03 16:33:02.138+01	\N	\N	\N	\N	\N
22852	2009-08-03 16:33:02.229+01	\N	2009-08-03 16:33:02.255+01	\N	\N	\N
22853	2009-08-03 16:33:02.509+01	\N	\N	\N	\N	\N
22854	2009-08-03 16:33:02.535+01	\N	\N	\N	\N	\N
22855	2009-08-03 16:33:02.626+01	\N	2009-08-03 16:33:02.652+01	\N	\N	\N
22856	2009-08-03 16:33:02.782+01	\N	\N	\N	\N	\N
22857	2009-08-03 16:33:02.809+01	\N	\N	\N	\N	\N
22858	2009-08-03 16:33:02.902+01	\N	2009-08-03 16:33:02.929+01	\N	\N	\N
22859	2009-08-03 16:33:03.064+01	\N	\N	\N	\N	\N
22860	2009-08-03 16:33:03.091+01	\N	\N	\N	\N	\N
22861	2009-08-03 16:33:03.227+01	\N	2009-08-03 16:33:03.254+01	\N	\N	\N
22862	2009-08-03 16:33:03.523+01	\N	\N	\N	\N	\N
22863	2009-08-03 16:33:03.55+01	\N	\N	\N	\N	\N
22864	2009-08-03 16:33:03.644+01	\N	2009-08-03 16:33:03.671+01	\N	\N	\N
22865	2009-08-03 16:33:03.942+01	\N	\N	\N	\N	\N
22866	2009-08-03 16:33:03.971+01	\N	\N	\N	\N	\N
22867	2009-08-03 16:33:04.064+01	\N	2009-08-03 16:33:04.09+01	\N	\N	\N
22868	2009-08-03 16:33:04.414+01	\N	\N	\N	\N	\N
22869	2009-08-03 16:33:04.441+01	\N	\N	\N	\N	\N
22870	2009-08-03 16:33:04.533+01	\N	2009-08-03 16:33:04.559+01	\N	\N	\N
22871	2009-08-03 16:33:04.694+01	\N	\N	\N	\N	\N
22872	2009-08-03 16:33:04.72+01	\N	\N	\N	\N	\N
22873	2009-08-03 16:33:04.814+01	\N	2009-08-03 16:33:04.84+01	\N	\N	\N
22874	2009-08-03 16:33:05.019+01	\N	\N	\N	\N	\N
22875	2009-08-03 16:33:05.046+01	\N	\N	\N	\N	\N
22876	2009-08-03 16:33:05.139+01	\N	2009-08-03 16:33:05.165+01	\N	\N	\N
22877	2009-08-03 16:33:05.341+01	\N	\N	\N	\N	\N
22878	2009-08-03 16:33:05.368+01	\N	\N	\N	\N	\N
22879	2009-08-03 16:33:05.464+01	\N	2009-08-03 16:33:05.49+01	\N	\N	\N
22880	2009-08-03 16:33:05.668+01	\N	\N	\N	\N	\N
22881	2009-08-03 16:33:05.695+01	\N	\N	\N	\N	\N
22882	2009-08-03 16:33:05.816+01	\N	2009-08-03 16:33:05.843+01	\N	\N	\N
22883	2009-08-03 16:33:05.936+01	\N	\N	\N	\N	\N
22884	2009-08-03 16:33:05.964+01	\N	\N	\N	\N	\N
22885	2009-08-03 16:33:06.062+01	\N	2009-08-03 16:33:06.09+01	\N	\N	\N
22886	2009-08-03 16:33:06.252+01	\N	\N	\N	\N	\N
22887	2009-08-03 16:33:06.28+01	\N	\N	\N	\N	\N
22888	2009-08-03 16:33:06.376+01	\N	2009-08-03 16:33:06.404+01	\N	\N	\N
22889	2009-08-03 16:33:06.575+01	\N	\N	\N	\N	\N
22890	2009-08-03 16:33:06.602+01	\N	\N	\N	\N	\N
22891	2009-08-03 16:33:06.699+01	\N	2009-08-03 16:33:06.727+01	\N	\N	\N
22892	2009-08-03 16:33:06.819+01	\N	\N	\N	\N	\N
22893	2009-08-03 16:33:06.846+01	\N	\N	\N	\N	\N
22894	2009-08-03 16:33:06.964+01	\N	2009-08-03 16:33:06.992+01	\N	\N	\N
22895	2009-08-03 16:33:07.257+01	\N	\N	\N	\N	\N
22896	2009-08-03 16:33:07.285+01	\N	\N	\N	\N	\N
22897	2009-08-03 16:33:07.401+01	\N	2009-08-03 16:33:07.428+01	\N	\N	\N
22898	2009-08-03 16:33:07.719+01	\N	\N	\N	\N	\N
22899	2009-08-03 16:33:07.747+01	\N	\N	\N	\N	\N
22900	2009-08-03 16:33:07.864+01	\N	2009-08-03 16:33:07.891+01	\N	\N	\N
22901	2009-08-03 16:33:08.203+01	\N	\N	\N	\N	\N
22902	2009-08-03 16:33:08.231+01	\N	\N	\N	\N	\N
22903	2009-08-03 16:33:08.328+01	\N	2009-08-03 16:33:08.356+01	\N	\N	\N
22904	2009-08-03 16:33:08.638+01	\N	\N	\N	\N	\N
22905	2009-08-03 16:33:08.667+01	\N	\N	\N	\N	\N
22906	2009-08-03 16:33:08.765+01	\N	2009-08-03 16:33:08.792+01	\N	\N	\N
22907	2009-08-03 16:33:09.023+01	\N	\N	\N	\N	\N
22908	2009-08-03 16:33:09.051+01	\N	\N	\N	\N	\N
22909	2009-08-03 16:33:09.161+01	\N	2009-08-03 16:33:09.188+01	\N	\N	\N
22910	2009-08-03 16:33:09.283+01	\N	\N	\N	\N	\N
22911	2009-08-03 16:33:09.31+01	\N	\N	\N	\N	\N
22912	2009-08-03 16:33:09.407+01	\N	2009-08-03 16:33:09.436+01	\N	\N	\N
22913	2009-08-03 16:33:09.531+01	\N	\N	\N	\N	\N
22914	2009-08-03 16:33:09.559+01	\N	\N	\N	\N	\N
22915	2009-08-03 16:33:09.656+01	\N	2009-08-03 16:33:09.684+01	\N	\N	\N
22916	2009-08-03 16:33:09.756+01	\N	\N	\N	\N	\N
22917	2009-08-03 16:33:09.783+01	\N	\N	\N	\N	\N
22918	2009-08-03 16:33:09.881+01	\N	2009-08-03 16:33:09.908+01	\N	\N	\N
22919	2009-08-03 16:33:09.981+01	\N	\N	\N	\N	\N
22920	2009-08-03 16:33:10.01+01	\N	\N	\N	\N	\N
22921	2009-08-03 16:33:10.109+01	\N	2009-08-03 16:33:10.137+01	\N	\N	\N
22922	2009-08-03 16:33:10.233+01	\N	\N	\N	\N	\N
22923	2009-08-03 16:33:10.261+01	\N	\N	\N	\N	\N
22924	2009-08-03 16:33:10.381+01	\N	2009-08-03 16:33:10.409+01	\N	\N	\N
22925	2009-08-03 16:33:10.529+01	\N	\N	\N	\N	\N
22926	2009-08-03 16:33:10.557+01	\N	\N	\N	\N	\N
22927	2009-08-03 16:33:10.656+01	\N	2009-08-03 16:33:10.684+01	\N	\N	\N
22928	2009-08-03 16:33:10.78+01	\N	\N	\N	\N	\N
22929	2009-08-03 16:33:10.808+01	\N	\N	\N	\N	\N
22930	2009-08-03 16:33:10.908+01	\N	2009-08-03 16:33:10.937+01	\N	\N	\N
22931	2009-08-03 16:33:11.104+01	\N	\N	\N	\N	\N
22932	2009-08-03 16:33:11.133+01	\N	\N	\N	\N	\N
22933	2009-08-03 16:33:11.257+01	\N	2009-08-03 16:33:11.286+01	\N	\N	\N
22934	2009-08-03 16:33:11.50+01	\N	\N	\N	\N	\N
22935	2009-08-03 16:33:11.541+01	\N	\N	\N	\N	\N
22936	2009-08-03 16:33:11.648+01	\N	2009-08-03 16:33:11.676+01	\N	\N	\N
22937	2009-08-03 16:33:11.728+01	\N	\N	\N	\N	\N
22938	2009-08-03 16:33:11.756+01	\N	\N	\N	\N	\N
22939	2009-08-03 16:33:11.902+01	\N	2009-08-03 16:33:11.93+01	\N	\N	\N
22940	2009-08-03 16:33:12.317+01	\N	\N	\N	\N	\N
22941	2009-08-03 16:33:12.368+01	\N	\N	\N	\N	\N
22942	2009-08-03 16:33:12.554+01	\N	2009-08-03 16:33:12.586+01	\N	\N	\N
22943	2009-08-03 16:33:12.681+01	\N	\N	\N	\N	\N
22944	2009-08-03 16:33:12.709+01	\N	\N	\N	\N	\N
22945	2009-08-03 16:33:12.811+01	\N	2009-08-03 16:33:12.843+01	\N	\N	\N
22946	2009-08-03 16:33:13.026+01	\N	\N	\N	\N	\N
22947	2009-08-03 16:33:13.059+01	\N	\N	\N	\N	\N
22948	2009-08-03 16:33:13.175+01	\N	2009-08-03 16:33:13.206+01	\N	\N	\N
22949	2009-08-03 16:33:13.263+01	\N	\N	\N	\N	\N
22950	2009-08-03 16:33:13.294+01	\N	\N	\N	\N	\N
22951	2009-08-03 16:33:13.408+01	\N	2009-08-03 16:33:13.439+01	\N	\N	\N
22952	2009-08-03 16:33:13.497+01	\N	\N	\N	\N	\N
22953	2009-08-03 16:33:13.527+01	\N	\N	\N	\N	\N
22954	2009-08-03 16:33:13.66+01	\N	2009-08-03 16:33:13.692+01	\N	\N	\N
22955	2009-08-03 16:33:13.749+01	\N	\N	\N	\N	\N
22956	2009-08-03 16:33:13.779+01	\N	\N	\N	\N	\N
22957	2009-08-03 16:33:13.891+01	\N	2009-08-03 16:33:13.922+01	\N	\N	\N
22958	2009-08-03 16:33:13.978+01	\N	\N	\N	\N	\N
22959	2009-08-03 16:33:14.008+01	\N	\N	\N	\N	\N
22960	2009-08-03 16:33:14.165+01	\N	2009-08-03 16:33:14.197+01	\N	\N	\N
22961	2009-08-03 16:33:14.502+01	\N	\N	\N	\N	\N
22962	2009-08-03 16:33:14.548+01	\N	\N	\N	\N	\N
22963	2009-08-03 16:33:14.661+01	\N	2009-08-03 16:33:14.694+01	\N	\N	\N
22964	2009-08-03 16:33:14.776+01	\N	\N	\N	\N	\N
22965	2009-08-03 16:33:14.807+01	\N	\N	\N	\N	\N
22966	2009-08-03 16:33:14.935+01	\N	2009-08-03 16:33:14.964+01	\N	\N	\N
22967	2009-08-03 16:33:15.219+01	\N	\N	\N	\N	\N
22968	2009-08-03 16:33:15.251+01	\N	\N	\N	\N	\N
22969	2009-08-03 16:33:15.387+01	\N	2009-08-03 16:33:15.418+01	\N	\N	\N
22970	2009-08-03 16:33:15.634+01	\N	\N	\N	\N	\N
22971	2009-08-03 16:33:15.666+01	\N	\N	\N	\N	\N
22972	2009-08-03 16:33:15.778+01	\N	2009-08-03 16:33:15.81+01	\N	\N	\N
22973	2009-08-03 16:33:15.969+01	\N	\N	\N	\N	\N
22974	2009-08-03 16:33:16.001+01	\N	\N	\N	\N	\N
22975	2009-08-03 16:33:16.112+01	\N	2009-08-03 16:33:16.143+01	\N	\N	\N
22976	2009-08-03 16:33:16.349+01	\N	\N	\N	\N	\N
22977	2009-08-03 16:33:16.381+01	\N	\N	\N	\N	\N
22978	2009-08-03 16:33:16.49+01	\N	2009-08-03 16:33:16.531+01	\N	\N	\N
22979	2009-08-03 16:33:16.922+01	\N	\N	\N	\N	\N
22980	2009-08-03 16:33:16.953+01	\N	\N	\N	\N	\N
22981	2009-08-03 16:33:17.065+01	\N	2009-08-03 16:33:17.10+01	\N	\N	\N
22982	2009-08-03 16:33:17.261+01	\N	\N	\N	\N	\N
22983	2009-08-03 16:33:17.293+01	\N	\N	\N	\N	\N
22984	2009-08-03 16:33:17.405+01	\N	2009-08-03 16:33:17.437+01	\N	\N	\N
22985	2009-08-03 16:33:17.598+01	\N	\N	\N	\N	\N
22986	2009-08-03 16:33:17.63+01	\N	\N	\N	\N	\N
22987	2009-08-03 16:33:17.743+01	\N	2009-08-03 16:33:17.775+01	\N	\N	\N
22988	2009-08-03 16:33:18.015+01	\N	\N	\N	\N	\N
22989	2009-08-03 16:33:18.048+01	\N	\N	\N	\N	\N
22990	2009-08-03 16:33:18.163+01	\N	2009-08-03 16:33:18.196+01	\N	\N	\N
22991	2009-08-03 16:33:18.359+01	\N	\N	\N	\N	\N
22992	2009-08-03 16:33:18.391+01	\N	\N	\N	\N	\N
22993	2009-08-03 16:33:18.502+01	\N	2009-08-03 16:33:18.534+01	\N	\N	\N
22994	2009-08-03 16:33:18.596+01	\N	\N	\N	\N	\N
22995	2009-08-03 16:33:18.629+01	\N	\N	\N	\N	\N
22996	2009-08-03 16:33:18.74+01	\N	2009-08-03 16:33:18.771+01	\N	\N	\N
22997	2009-08-03 16:33:18.838+01	\N	\N	\N	\N	\N
22998	2009-08-03 16:33:18.87+01	\N	\N	\N	\N	\N
22999	2009-08-03 16:33:18.983+01	\N	2009-08-03 16:33:19.015+01	\N	\N	\N
23000	2009-08-03 16:33:19.074+01	\N	\N	\N	\N	\N
23001	2009-08-03 16:33:19.108+01	\N	\N	\N	\N	\N
23002	2009-08-03 16:33:19.219+01	\N	2009-08-03 16:33:19.252+01	\N	\N	\N
23003	2009-08-03 16:33:19.311+01	\N	\N	\N	\N	\N
23004	2009-08-03 16:33:19.344+01	\N	\N	\N	\N	\N
23005	2009-08-03 16:33:19.458+01	\N	2009-08-03 16:33:19.491+01	\N	\N	\N
23006	2009-08-03 16:33:19.551+01	\N	\N	\N	\N	\N
23007	2009-08-03 16:33:19.585+01	\N	\N	\N	\N	\N
23008	2009-08-03 16:33:19.70+01	\N	2009-08-03 16:33:19.732+01	\N	\N	\N
23009	2009-08-03 16:33:19.792+01	\N	\N	\N	\N	\N
23010	2009-08-03 16:33:19.825+01	\N	\N	\N	\N	\N
23011	2009-08-03 16:33:19.939+01	\N	2009-08-03 16:33:19.973+01	\N	\N	\N
23012	2009-08-03 16:33:20.033+01	\N	\N	\N	\N	\N
23013	2009-08-03 16:33:20.065+01	\N	\N	\N	\N	\N
23014	2009-08-03 16:33:20.184+01	\N	2009-08-03 16:33:20.217+01	\N	\N	\N
23015	2009-08-03 16:33:20.278+01	\N	\N	\N	\N	\N
23016	2009-08-03 16:33:20.311+01	\N	\N	\N	\N	\N
23017	2009-08-03 16:33:20.427+01	\N	2009-08-03 16:33:20.463+01	\N	\N	\N
23018	2009-08-03 16:33:20.524+01	\N	\N	\N	\N	\N
23019	2009-08-03 16:33:20.557+01	\N	\N	\N	\N	\N
23020	2009-08-03 16:33:20.673+01	\N	2009-08-03 16:33:20.706+01	\N	\N	\N
23021	2009-08-03 16:33:20.764+01	\N	\N	\N	\N	\N
23022	2009-08-03 16:33:20.796+01	\N	\N	\N	\N	\N
23023	2009-08-03 16:33:20.925+01	\N	2009-08-03 16:33:20.958+01	\N	\N	\N
23024	2009-08-03 16:33:21.017+01	\N	\N	\N	\N	\N
23025	2009-08-03 16:33:21.05+01	\N	\N	\N	\N	\N
23026	2009-08-03 16:33:21.168+01	\N	2009-08-03 16:33:21.20+01	\N	\N	\N
23027	2009-08-03 16:33:21.26+01	\N	\N	\N	\N	\N
23028	2009-08-03 16:33:21.292+01	\N	\N	\N	\N	\N
23029	2009-08-03 16:33:21.409+01	\N	2009-08-03 16:33:21.442+01	\N	\N	\N
23030	2009-08-03 16:33:21.503+01	\N	\N	\N	\N	\N
23031	2009-08-03 16:33:21.536+01	\N	\N	\N	\N	\N
23032	2009-08-03 16:33:21.656+01	\N	2009-08-03 16:33:21.69+01	\N	\N	\N
23033	2009-08-03 16:33:21.754+01	\N	\N	\N	\N	\N
23034	2009-08-03 16:33:21.787+01	\N	\N	\N	\N	\N
23035	2009-08-03 16:33:21.902+01	\N	2009-08-03 16:33:21.934+01	\N	\N	\N
23036	2009-08-03 16:33:21.996+01	\N	\N	\N	\N	\N
23037	2009-08-03 16:33:22.028+01	\N	\N	\N	\N	\N
23038	2009-08-03 16:33:22.149+01	\N	2009-08-03 16:33:22.183+01	\N	\N	\N
23039	2009-08-03 16:33:22.245+01	\N	\N	\N	\N	\N
23040	2009-08-03 16:33:22.278+01	\N	\N	\N	\N	\N
23041	2009-08-03 16:33:22.397+01	\N	2009-08-03 16:33:22.433+01	\N	\N	\N
23042	2009-08-03 16:33:22.495+01	\N	\N	\N	\N	\N
23043	2009-08-03 16:33:22.528+01	\N	\N	\N	\N	\N
23044	2009-08-03 16:33:22.649+01	\N	2009-08-03 16:33:22.683+01	\N	\N	\N
23045	2009-08-03 16:33:22.743+01	\N	\N	\N	\N	\N
23046	2009-08-03 16:33:22.776+01	\N	\N	\N	\N	\N
23047	2009-08-03 16:33:22.892+01	\N	2009-08-03 16:33:22.935+01	\N	\N	\N
23048	2009-08-03 16:33:22.996+01	\N	\N	\N	\N	\N
23049	2009-08-03 16:33:23.028+01	\N	\N	\N	\N	\N
23050	2009-08-03 16:33:23.203+01	\N	2009-08-03 16:33:23.237+01	\N	\N	\N
23051	2009-08-03 16:33:23.324+01	\N	\N	\N	\N	\N
23052	2009-08-03 16:33:23.358+01	\N	\N	\N	\N	\N
23053	2009-08-03 16:33:23.504+01	\N	2009-08-03 16:33:23.539+01	\N	\N	\N
23054	2009-08-03 16:33:23.603+01	\N	\N	\N	\N	\N
23055	2009-08-03 16:33:23.637+01	\N	\N	\N	\N	\N
23056	2009-08-03 16:33:23.757+01	\N	2009-08-03 16:33:23.809+01	\N	\N	\N
23057	2009-08-03 16:33:23.981+01	\N	\N	\N	\N	\N
23058	2009-08-03 16:33:24.017+01	\N	\N	\N	\N	\N
23059	2009-08-03 16:33:24.143+01	\N	2009-08-03 16:33:24.178+01	\N	\N	\N
23060	2009-08-03 16:33:24.323+01	\N	\N	\N	\N	\N
23061	2009-08-03 16:33:24.359+01	\N	\N	\N	\N	\N
23062	2009-08-03 16:33:24.484+01	\N	2009-08-03 16:33:24.523+01	\N	\N	\N
23063	2009-08-03 16:33:24.589+01	\N	\N	\N	\N	\N
23064	2009-08-03 16:33:24.623+01	\N	\N	\N	\N	\N
23065	2009-08-03 16:33:24.778+01	\N	2009-08-03 16:33:24.811+01	\N	\N	\N
23066	2009-08-03 16:33:24.94+01	\N	\N	\N	\N	\N
23067	2009-08-03 16:33:24.974+01	\N	\N	\N	\N	\N
23068	2009-08-03 16:33:25.096+01	\N	2009-08-03 16:33:25.13+01	\N	\N	\N
23069	2009-08-03 16:33:25.192+01	\N	\N	\N	\N	\N
23070	2009-08-03 16:33:25.226+01	\N	\N	\N	\N	\N
23071	2009-08-03 16:33:25.344+01	\N	2009-08-03 16:33:25.379+01	\N	\N	\N
23072	2009-08-03 16:33:25.556+01	\N	\N	\N	\N	\N
23073	2009-08-03 16:33:25.593+01	\N	\N	\N	\N	\N
23074	2009-08-03 16:33:25.715+01	\N	2009-08-03 16:33:25.749+01	\N	\N	\N
23075	2009-08-03 16:33:25.923+01	\N	\N	\N	\N	\N
23076	2009-08-03 16:33:25.956+01	\N	\N	\N	\N	\N
23077	2009-08-03 16:33:26.079+01	\N	2009-08-03 16:33:26.114+01	\N	\N	\N
23078	2009-08-03 16:33:26.289+01	\N	\N	\N	\N	\N
23079	2009-08-03 16:33:26.325+01	\N	\N	\N	\N	\N
23080	2009-08-03 16:33:26.504+01	\N	2009-08-03 16:33:26.539+01	\N	\N	\N
23081	2009-08-03 16:33:26.661+01	\N	\N	\N	\N	\N
23082	2009-08-03 16:33:26.696+01	\N	\N	\N	\N	\N
23083	2009-08-03 16:33:26.819+01	\N	2009-08-03 16:33:26.872+01	\N	\N	\N
23084	2009-08-03 16:33:26.935+01	\N	\N	\N	\N	\N
23085	2009-08-03 16:33:26.968+01	\N	\N	\N	\N	\N
23086	2009-08-03 16:33:27.093+01	\N	2009-08-03 16:33:27.128+01	\N	\N	\N
23087	2009-08-03 16:33:27.19+01	\N	\N	\N	\N	\N
23088	2009-08-03 16:33:27.224+01	\N	\N	\N	\N	\N
23089	2009-08-03 16:33:27.346+01	\N	2009-08-03 16:33:27.382+01	\N	\N	\N
23090	2009-08-03 16:33:27.53+01	\N	\N	\N	\N	\N
23091	2009-08-03 16:33:27.566+01	\N	\N	\N	\N	\N
23092	2009-08-03 16:33:27.748+01	\N	2009-08-03 16:33:27.783+01	\N	\N	\N
23093	2009-08-03 16:33:27.846+01	\N	\N	\N	\N	\N
23094	2009-08-03 16:33:27.88+01	\N	\N	\N	\N	\N
23095	2009-08-03 16:33:28.058+01	\N	2009-08-03 16:33:28.096+01	\N	\N	\N
23096	2009-08-03 16:33:28.275+01	\N	\N	\N	\N	\N
23097	2009-08-03 16:33:28.31+01	\N	\N	\N	\N	\N
23098	2009-08-03 16:33:28.463+01	\N	2009-08-03 16:33:28.499+01	\N	\N	\N
23099	2009-08-03 16:33:28.564+01	\N	\N	\N	\N	\N
23100	2009-08-03 16:33:28.601+01	\N	\N	\N	\N	\N
23101	2009-08-03 16:33:28.738+01	\N	2009-08-03 16:33:28.773+01	\N	\N	\N
23102	2009-08-03 16:33:28.95+01	\N	\N	\N	\N	\N
23103	2009-08-03 16:33:28.985+01	\N	\N	\N	\N	\N
23104	2009-08-03 16:33:29.109+01	\N	2009-08-03 16:33:29.144+01	\N	\N	\N
23105	2009-08-03 16:33:29.267+01	\N	\N	\N	\N	\N
23106	2009-08-03 16:33:29.302+01	\N	\N	\N	\N	\N
23107	2009-08-03 16:33:29.427+01	\N	2009-08-03 16:33:29.462+01	\N	\N	\N
23108	2009-08-03 16:33:29.699+01	\N	\N	\N	\N	\N
23109	2009-08-03 16:33:29.734+01	\N	\N	\N	\N	\N
23110	2009-08-03 16:33:29.865+01	\N	2009-08-03 16:33:29.901+01	\N	\N	\N
23111	2009-08-03 16:33:30.229+01	\N	\N	\N	\N	\N
23112	2009-08-03 16:33:30.265+01	\N	\N	\N	\N	\N
23113	2009-08-03 16:33:30.391+01	\N	2009-08-03 16:33:30.428+01	\N	\N	\N
23114	2009-08-03 16:33:30.676+01	\N	\N	\N	\N	\N
23115	2009-08-03 16:33:30.712+01	\N	\N	\N	\N	\N
23116	2009-08-03 16:33:30.837+01	\N	2009-08-03 16:33:30.872+01	\N	\N	\N
23117	2009-08-03 16:33:31.17+01	\N	\N	\N	\N	\N
23118	2009-08-03 16:33:31.207+01	\N	\N	\N	\N	\N
23119	2009-08-03 16:33:31.362+01	\N	2009-08-03 16:33:31.398+01	\N	\N	\N
23120	2009-08-03 16:33:31.463+01	\N	\N	\N	\N	\N
23121	2009-08-03 16:33:31.497+01	\N	\N	\N	\N	\N
23122	2009-08-03 16:33:31.652+01	\N	2009-08-03 16:33:31.688+01	\N	\N	\N
23123	2009-08-03 16:33:31.844+01	\N	\N	\N	\N	\N
23124	2009-08-03 16:33:31.88+01	\N	\N	\N	\N	\N
23125	2009-08-03 16:33:32.036+01	\N	2009-08-03 16:33:32.073+01	\N	\N	\N
23126	2009-08-03 16:33:32.171+01	\N	\N	\N	\N	\N
23127	2009-08-03 16:33:32.221+01	\N	\N	\N	\N	\N
23128	2009-08-03 16:33:32.375+01	\N	2009-08-03 16:33:32.412+01	\N	\N	\N
23129	2009-08-03 16:33:32.478+01	\N	\N	\N	\N	\N
23130	2009-08-03 16:33:32.512+01	\N	\N	\N	\N	\N
23131	2009-08-03 16:33:32.639+01	\N	2009-08-03 16:33:32.676+01	\N	\N	\N
23132	2009-08-03 16:33:32.80+01	\N	\N	\N	\N	\N
23133	2009-08-03 16:33:32.837+01	\N	\N	\N	\N	\N
23134	2009-08-03 16:33:32.964+01	\N	2009-08-03 16:33:33.001+01	\N	\N	\N
23135	2009-08-03 16:33:33.068+01	\N	\N	\N	\N	\N
23136	2009-08-03 16:33:33.106+01	\N	\N	\N	\N	\N
23137	2009-08-03 16:33:33.231+01	\N	2009-08-03 16:33:33.268+01	\N	\N	\N
23138	2009-08-03 16:33:33.48+01	\N	\N	\N	\N	\N
23139	2009-08-03 16:33:33.523+01	\N	\N	\N	\N	\N
23140	2009-08-03 16:33:33.654+01	\N	2009-08-03 16:33:33.691+01	\N	\N	\N
23141	2009-08-03 16:33:34.069+01	\N	\N	\N	\N	\N
23142	2009-08-03 16:33:34.108+01	\N	\N	\N	\N	\N
23143	2009-08-03 16:33:34.234+01	\N	2009-08-03 16:33:34.27+01	\N	\N	\N
23144	2009-08-03 16:33:34.691+01	\N	\N	\N	\N	\N
23145	2009-08-03 16:33:34.728+01	\N	\N	\N	\N	\N
23146	2009-08-03 16:33:34.855+01	\N	2009-08-03 16:33:34.89+01	\N	\N	\N
23147	2009-08-03 16:33:35.282+01	\N	\N	\N	\N	\N
23148	2009-08-03 16:33:35.32+01	\N	\N	\N	\N	\N
23149	2009-08-03 16:33:35.449+01	\N	2009-08-03 16:33:35.499+01	\N	\N	\N
23150	2009-08-03 16:33:35.599+01	\N	\N	\N	\N	\N
23151	2009-08-03 16:33:35.637+01	\N	\N	\N	\N	\N
23152	2009-08-03 16:33:35.823+01	\N	2009-08-03 16:33:35.859+01	\N	\N	\N
23153	2009-08-03 16:33:36.106+01	\N	\N	\N	\N	\N
23154	2009-08-03 16:33:36.144+01	\N	\N	\N	\N	\N
23155	2009-08-03 16:33:36.274+01	\N	2009-08-03 16:33:36.312+01	\N	\N	\N
23156	2009-08-03 16:33:36.379+01	\N	\N	\N	\N	\N
23157	2009-08-03 16:33:36.415+01	\N	\N	\N	\N	\N
23158	2009-08-03 16:33:36.572+01	\N	2009-08-03 16:33:36.611+01	\N	\N	\N
23159	2009-08-03 16:33:36.992+01	\N	\N	\N	\N	\N
23160	2009-08-03 16:33:37.029+01	\N	\N	\N	\N	\N
23161	2009-08-03 16:33:37.194+01	\N	2009-08-03 16:33:37.231+01	\N	\N	\N
23162	2009-08-03 16:33:37.297+01	\N	\N	\N	\N	\N
23163	2009-08-03 16:33:37.333+01	\N	\N	\N	\N	\N
23164	2009-08-03 16:33:37.491+01	\N	2009-08-03 16:33:37.529+01	\N	\N	\N
23165	2009-08-03 16:33:37.60+01	\N	\N	\N	\N	\N
23166	2009-08-03 16:33:37.637+01	\N	\N	\N	\N	\N
23167	2009-08-03 16:33:37.768+01	\N	2009-08-03 16:33:37.806+01	\N	\N	\N
23168	2009-08-03 16:33:38.235+01	\N	\N	\N	\N	\N
23169	2009-08-03 16:33:38.274+01	\N	\N	\N	\N	\N
23170	2009-08-03 16:33:38.406+01	\N	2009-08-03 16:33:38.459+01	\N	\N	\N
23171	2009-08-03 16:33:38.705+01	\N	\N	\N	\N	\N
23172	2009-08-03 16:33:38.744+01	\N	\N	\N	\N	\N
23173	2009-08-03 16:33:38.872+01	\N	2009-08-03 16:33:38.909+01	\N	\N	\N
23174	2009-08-03 16:33:38.978+01	\N	\N	\N	\N	\N
23175	2009-08-03 16:33:39.016+01	\N	\N	\N	\N	\N
23176	2009-08-03 16:33:39.151+01	\N	2009-08-03 16:33:39.189+01	\N	\N	\N
23177	2009-08-03 16:33:39.377+01	\N	\N	\N	\N	\N
23178	2009-08-03 16:33:39.414+01	\N	\N	\N	\N	\N
23179	2009-08-03 16:33:39.573+01	\N	2009-08-03 16:33:39.613+01	\N	\N	\N
23180	2009-08-03 16:33:39.681+01	\N	\N	\N	\N	\N
23181	2009-08-03 16:33:39.719+01	\N	\N	\N	\N	\N
23182	2009-08-03 16:33:39.869+01	\N	2009-08-03 16:33:39.908+01	\N	\N	\N
23183	2009-08-03 16:33:39.975+01	\N	\N	\N	\N	\N
23184	2009-08-03 16:33:40.013+01	\N	\N	\N	\N	\N
23185	2009-08-03 16:33:40.175+01	\N	2009-08-03 16:33:40.213+01	\N	\N	\N
23186	2009-08-03 16:33:40.342+01	\N	\N	\N	\N	\N
23187	2009-08-03 16:33:40.38+01	\N	\N	\N	\N	\N
23188	2009-08-03 16:33:40.542+01	\N	2009-08-03 16:33:40.586+01	\N	\N	\N
23189	2009-08-03 16:33:40.715+01	\N	\N	\N	\N	\N
23190	2009-08-03 16:33:40.753+01	\N	\N	\N	\N	\N
23191	2009-08-03 16:33:40.883+01	\N	2009-08-03 16:33:40.921+01	\N	\N	\N
23192	2009-08-03 16:33:41.277+01	\N	\N	\N	\N	\N
23193	2009-08-03 16:33:41.315+01	\N	\N	\N	\N	\N
23194	2009-08-03 16:33:41.478+01	\N	2009-08-03 16:33:41.515+01	\N	\N	\N
23195	2009-08-03 16:33:41.677+01	\N	\N	\N	\N	\N
23196	2009-08-03 16:33:41.715+01	\N	\N	\N	\N	\N
23197	2009-08-03 16:33:41.912+01	\N	2009-08-03 16:33:41.951+01	\N	\N	\N
23198	2009-08-03 16:33:42.115+01	\N	\N	\N	\N	\N
23199	2009-08-03 16:33:42.154+01	\N	\N	\N	\N	\N
23200	2009-08-03 16:33:42.315+01	\N	2009-08-03 16:33:42.354+01	\N	\N	\N
23201	2009-08-03 16:33:42.743+01	\N	\N	\N	\N	\N
23202	2009-08-03 16:33:42.783+01	\N	\N	\N	\N	\N
23203	2009-08-03 16:33:42.945+01	\N	2009-08-03 16:33:42.983+01	\N	\N	\N
23204	2009-08-03 16:33:43.086+01	\N	\N	\N	\N	\N
23205	2009-08-03 16:33:43.125+01	\N	\N	\N	\N	\N
23206	2009-08-03 16:33:43.292+01	\N	2009-08-03 16:33:43.33+01	\N	\N	\N
23207	2009-08-03 16:33:43.675+01	\N	\N	\N	\N	\N
23208	2009-08-03 16:33:43.715+01	\N	\N	\N	\N	\N
23209	2009-08-03 16:33:43.858+01	\N	2009-08-03 16:33:43.896+01	\N	\N	\N
23210	2009-08-03 16:33:44.304+01	\N	\N	\N	\N	\N
23211	2009-08-03 16:33:44.344+01	\N	\N	\N	\N	\N
23212	2009-08-03 16:33:44.479+01	\N	2009-08-03 16:33:44.518+01	\N	\N	\N
23213	2009-08-03 16:33:44.774+01	\N	\N	\N	\N	\N
23214	2009-08-03 16:33:44.812+01	\N	\N	\N	\N	\N
23215	2009-08-03 16:33:45.017+01	\N	2009-08-03 16:33:45.056+01	\N	\N	\N
23216	2009-08-03 16:33:45.161+01	\N	\N	\N	\N	\N
23217	2009-08-03 16:33:45.20+01	\N	\N	\N	\N	\N
23218	2009-08-03 16:33:45.425+01	\N	2009-08-03 16:33:45.466+01	\N	\N	\N
23219	2009-08-03 16:33:45.605+01	\N	\N	\N	\N	\N
23220	2009-08-03 16:33:45.645+01	\N	\N	\N	\N	\N
23221	2009-08-03 16:33:45.875+01	\N	2009-08-03 16:33:45.914+01	\N	\N	\N
23222	2009-08-03 16:33:46.11+01	\N	\N	\N	\N	\N
23223	2009-08-03 16:33:46.148+01	\N	\N	\N	\N	\N
23224	2009-08-03 16:33:46.293+01	\N	2009-08-03 16:33:46.332+01	\N	\N	\N
23225	2009-08-03 16:33:46.555+01	\N	\N	\N	\N	\N
23226	2009-08-03 16:33:46.596+01	\N	\N	\N	\N	\N
23227	2009-08-03 16:33:46.733+01	\N	2009-08-03 16:33:46.773+01	\N	\N	\N
23228	2009-08-03 16:33:47.004+01	\N	\N	\N	\N	\N
23229	2009-08-03 16:33:47.044+01	\N	\N	\N	\N	\N
23230	2009-08-03 16:33:47.181+01	\N	2009-08-03 16:33:47.22+01	\N	\N	\N
23231	2009-08-03 16:33:47.455+01	\N	\N	\N	\N	\N
23232	2009-08-03 16:33:47.495+01	\N	\N	\N	\N	\N
23233	2009-08-03 16:33:47.662+01	\N	2009-08-03 16:33:47.70+01	\N	\N	\N
23234	2009-08-03 16:33:47.989+01	\N	\N	\N	\N	\N
23235	2009-08-03 16:33:48.029+01	\N	\N	\N	\N	\N
23236	2009-08-03 16:33:48.231+01	\N	2009-08-03 16:33:48.27+01	\N	\N	\N
23237	2009-08-03 16:33:48.506+01	\N	\N	\N	\N	\N
23238	2009-08-03 16:33:48.544+01	\N	\N	\N	\N	\N
23239	2009-08-03 16:33:48.683+01	\N	2009-08-03 16:33:48.722+01	\N	\N	\N
23240	2009-08-03 16:33:48.888+01	\N	\N	\N	\N	\N
23241	2009-08-03 16:33:48.928+01	\N	\N	\N	\N	\N
23242	2009-08-03 16:33:49.09+01	\N	2009-08-03 16:33:49.156+01	\N	\N	\N
23243	2009-08-03 16:33:49.336+01	\N	\N	\N	\N	\N
23244	2009-08-03 16:33:49.402+01	\N	\N	\N	\N	\N
23245	2009-08-03 16:33:49.633+01	\N	2009-08-03 16:33:49.673+01	\N	\N	\N
23246	2009-08-03 16:33:49.808+01	\N	\N	\N	\N	\N
23247	2009-08-03 16:33:49.848+01	\N	\N	\N	\N	\N
23248	2009-08-03 16:33:50.024+01	\N	2009-08-03 16:33:50.064+01	\N	\N	\N
23249	2009-08-03 16:33:50.316+01	\N	\N	\N	\N	\N
23250	2009-08-03 16:33:50.384+01	\N	\N	\N	\N	\N
23251	2009-08-03 16:33:50.564+01	\N	2009-08-03 16:33:50.603+01	\N	\N	\N
23252	2009-08-03 16:33:50.736+01	\N	\N	\N	\N	\N
23253	2009-08-03 16:33:50.775+01	\N	\N	\N	\N	\N
23254	2009-08-03 16:33:50.912+01	\N	2009-08-03 16:33:50.951+01	\N	\N	\N
23255	2009-08-03 16:33:51.215+01	\N	\N	\N	\N	\N
23256	2009-08-03 16:33:51.256+01	\N	\N	\N	\N	\N
23257	2009-08-03 16:33:51.427+01	\N	2009-08-03 16:33:51.468+01	\N	\N	\N
23258	2009-08-03 16:33:51.67+01	\N	\N	\N	\N	\N
23259	2009-08-03 16:33:51.715+01	\N	\N	\N	\N	\N
23260	2009-08-03 16:33:51.917+01	\N	2009-08-03 16:33:51.958+01	\N	\N	\N
23261	2009-08-03 16:33:52.093+01	\N	\N	\N	\N	\N
23262	2009-08-03 16:33:52.133+01	\N	\N	\N	\N	\N
23263	2009-08-03 16:33:52.336+01	\N	2009-08-03 16:33:52.377+01	\N	\N	\N
23264	2009-08-03 16:33:52.514+01	\N	\N	\N	\N	\N
23265	2009-08-03 16:33:52.555+01	\N	\N	\N	\N	\N
23266	2009-08-03 16:33:52.705+01	\N	2009-08-03 16:33:52.745+01	\N	\N	\N
23267	2009-08-03 16:33:53.006+01	\N	\N	\N	\N	\N
23268	2009-08-03 16:33:53.046+01	\N	\N	\N	\N	\N
23269	2009-08-03 16:33:53.191+01	\N	2009-08-03 16:33:53.235+01	\N	\N	\N
23270	2009-08-03 16:33:53.599+01	\N	\N	\N	\N	\N
23271	2009-08-03 16:33:53.653+01	\N	\N	\N	\N	\N
23272	2009-08-03 16:33:53.792+01	\N	2009-08-03 16:33:53.832+01	\N	\N	\N
23273	2009-08-03 16:33:54.163+01	\N	\N	\N	\N	\N
23274	2009-08-03 16:33:54.207+01	\N	\N	\N	\N	\N
23275	2009-08-03 16:33:54.38+01	\N	2009-08-03 16:33:54.42+01	\N	\N	\N
23276	2009-08-03 16:33:54.729+01	\N	\N	\N	\N	\N
23277	2009-08-03 16:33:54.772+01	\N	\N	\N	\N	\N
23278	2009-08-03 16:33:54.912+01	\N	2009-08-03 16:33:54.952+01	\N	\N	\N
23279	2009-08-03 16:33:55.189+01	\N	\N	\N	\N	\N
23280	2009-08-03 16:33:55.231+01	\N	\N	\N	\N	\N
23281	2009-08-03 16:33:55.373+01	\N	2009-08-03 16:33:55.426+01	\N	\N	\N
23282	2009-08-03 16:33:55.664+01	\N	\N	\N	\N	\N
23283	2009-08-03 16:33:55.705+01	\N	\N	\N	\N	\N
23284	2009-08-03 16:33:55.876+01	\N	2009-08-03 16:33:55.918+01	\N	\N	\N
23285	2009-08-03 16:33:56.157+01	\N	\N	\N	\N	\N
23286	2009-08-03 16:33:56.199+01	\N	\N	\N	\N	\N
23287	2009-08-03 16:33:56.342+01	\N	2009-08-03 16:33:56.382+01	\N	\N	\N
23288	2009-08-03 16:33:56.462+01	\N	\N	\N	\N	\N
23289	2009-08-03 16:33:56.502+01	\N	\N	\N	\N	\N
23290	2009-08-03 16:33:56.645+01	\N	2009-08-03 16:33:56.685+01	\N	\N	\N
23291	2009-08-03 16:33:56.758+01	\N	\N	\N	\N	\N
23292	2009-08-03 16:33:56.798+01	\N	\N	\N	\N	\N
23293	2009-08-03 16:33:56.941+01	\N	2009-08-03 16:33:56.982+01	\N	\N	\N
23294	2009-08-03 16:33:57.057+01	\N	\N	\N	\N	\N
23295	2009-08-03 16:33:57.10+01	\N	\N	\N	\N	\N
23296	2009-08-03 16:33:57.246+01	\N	2009-08-03 16:33:57.287+01	\N	\N	\N
23297	2009-08-03 16:33:57.465+01	\N	\N	\N	\N	\N
23298	2009-08-03 16:33:57.506+01	\N	\N	\N	\N	\N
23299	2009-08-03 16:33:57.65+01	\N	2009-08-03 16:33:57.692+01	\N	\N	\N
23300	2009-08-03 16:33:57.963+01	\N	\N	\N	\N	\N
23301	2009-08-03 16:33:58.005+01	\N	\N	\N	\N	\N
23302	2009-08-03 16:33:58.155+01	\N	2009-08-03 16:33:58.196+01	\N	\N	\N
23303	2009-08-03 16:33:58.37+01	\N	\N	\N	\N	\N
23304	2009-08-03 16:33:58.419+01	\N	\N	\N	\N	\N
23305	2009-08-03 16:33:58.595+01	\N	2009-08-03 16:33:58.637+01	\N	\N	\N
23306	2009-08-03 16:33:58.906+01	\N	\N	\N	\N	\N
23307	2009-08-03 16:33:58.949+01	\N	\N	\N	\N	\N
23308	2009-08-03 16:33:59.095+01	\N	2009-08-03 16:33:59.137+01	\N	\N	\N
23309	2009-08-03 16:33:59.213+01	\N	\N	\N	\N	\N
23310	2009-08-03 16:33:59.255+01	\N	\N	\N	\N	\N
23311	2009-08-03 16:33:59.472+01	\N	2009-08-03 16:33:59.542+01	\N	\N	\N
23312	2009-08-03 16:33:59.668+01	\N	\N	\N	\N	\N
23313	2009-08-03 16:33:59.738+01	\N	\N	\N	\N	\N
23314	2009-08-03 16:33:59.994+01	\N	2009-08-03 16:34:00.036+01	\N	\N	\N
23315	2009-08-03 16:34:00.318+01	\N	\N	\N	\N	\N
23316	2009-08-03 16:34:00.361+01	\N	\N	\N	\N	\N
23317	2009-08-03 16:34:00.554+01	\N	2009-08-03 16:34:00.599+01	\N	\N	\N
23318	2009-08-03 16:34:00.848+01	\N	\N	\N	\N	\N
23319	2009-08-03 16:34:00.891+01	\N	\N	\N	\N	\N
23320	2009-08-03 16:34:01.078+01	\N	2009-08-03 16:34:01.12+01	\N	\N	\N
23321	2009-08-03 16:34:01.332+01	\N	\N	\N	\N	\N
23322	2009-08-03 16:34:01.383+01	\N	\N	\N	\N	\N
23323	2009-08-03 16:34:01.535+01	\N	2009-08-03 16:34:01.577+01	\N	\N	\N
23324	2009-08-03 16:34:01.755+01	\N	\N	\N	\N	\N
23325	2009-08-03 16:34:01.798+01	\N	\N	\N	\N	\N
23326	2009-08-03 16:34:01.981+01	\N	2009-08-03 16:34:02.024+01	\N	\N	\N
23327	2009-08-03 16:34:02.277+01	\N	\N	\N	\N	\N
23328	2009-08-03 16:34:02.321+01	\N	\N	\N	\N	\N
23329	2009-08-03 16:34:02.539+01	\N	2009-08-03 16:34:02.582+01	\N	\N	\N
23330	2009-08-03 16:34:02.827+01	\N	\N	\N	\N	\N
23331	2009-08-03 16:34:02.87+01	\N	\N	\N	\N	\N
23332	2009-08-03 16:34:03.02+01	\N	2009-08-03 16:34:03.063+01	\N	\N	\N
23333	2009-08-03 16:34:03.256+01	\N	\N	\N	\N	\N
23334	2009-08-03 16:34:03.299+01	\N	\N	\N	\N	\N
23335	2009-08-03 16:34:03.451+01	\N	2009-08-03 16:34:03.495+01	\N	\N	\N
23336	2009-08-03 16:34:03.674+01	\N	\N	\N	\N	\N
23337	2009-08-03 16:34:03.716+01	\N	\N	\N	\N	\N
23338	2009-08-03 16:34:03.901+01	\N	2009-08-03 16:34:03.951+01	\N	\N	\N
23339	2009-08-03 16:34:04.029+01	\N	\N	\N	\N	\N
23340	2009-08-03 16:34:04.073+01	\N	\N	\N	\N	\N
23341	2009-08-03 16:34:04.226+01	\N	2009-08-03 16:34:04.27+01	\N	\N	\N
23342	2009-08-03 16:34:04.451+01	\N	\N	\N	\N	\N
23343	2009-08-03 16:34:04.494+01	\N	\N	\N	\N	\N
23344	2009-08-03 16:34:04.645+01	\N	2009-08-03 16:34:04.688+01	\N	\N	\N
23345	2009-08-03 16:34:04.946+01	\N	\N	\N	\N	\N
23346	2009-08-03 16:34:04.99+01	\N	\N	\N	\N	\N
23347	2009-08-03 16:34:05.146+01	\N	2009-08-03 16:34:05.189+01	\N	\N	\N
23348	2009-08-03 16:34:05.334+01	\N	\N	\N	\N	\N
23349	2009-08-03 16:34:05.377+01	\N	\N	\N	\N	\N
23350	2009-08-03 16:34:05.641+01	\N	2009-08-03 16:34:05.684+01	\N	\N	\N
23351	2009-08-03 16:34:05.937+01	\N	\N	\N	\N	\N
23352	2009-08-03 16:34:05.98+01	\N	\N	\N	\N	\N
23353	2009-08-03 16:34:06.133+01	\N	2009-08-03 16:34:06.176+01	\N	\N	\N
23354	2009-08-03 16:34:06.406+01	\N	\N	\N	\N	\N
23355	2009-08-03 16:34:06.45+01	\N	\N	\N	\N	\N
23356	2009-08-03 16:34:06.606+01	\N	2009-08-03 16:34:06.651+01	\N	\N	\N
23357	2009-08-03 16:34:06.934+01	\N	\N	\N	\N	\N
23358	2009-08-03 16:34:06.983+01	\N	\N	\N	\N	\N
23359	2009-08-03 16:34:07.17+01	\N	2009-08-03 16:34:07.224+01	\N	\N	\N
23360	2009-08-03 16:34:07.411+01	\N	\N	\N	\N	\N
23361	2009-08-03 16:34:07.455+01	\N	\N	\N	\N	\N
23362	2009-08-03 16:34:07.61+01	\N	2009-08-03 16:34:07.654+01	\N	\N	\N
23363	2009-08-03 16:34:08.042+01	\N	\N	\N	\N	\N
23364	2009-08-03 16:34:08.088+01	\N	\N	\N	\N	\N
23365	2009-08-03 16:34:08.248+01	\N	2009-08-03 16:34:08.293+01	\N	\N	\N
23366	2009-08-03 16:34:08.521+01	\N	\N	\N	\N	\N
23367	2009-08-03 16:34:08.566+01	\N	\N	\N	\N	\N
23368	2009-08-03 16:34:08.722+01	\N	2009-08-03 16:34:08.765+01	\N	\N	\N
23369	2009-08-03 16:34:09.016+01	\N	\N	\N	\N	\N
23370	2009-08-03 16:34:09.06+01	\N	\N	\N	\N	\N
23371	2009-08-03 16:34:09.214+01	\N	2009-08-03 16:34:09.259+01	\N	\N	\N
23372	2009-08-03 16:34:09.493+01	\N	\N	\N	\N	\N
23373	2009-08-03 16:34:09.538+01	\N	\N	\N	\N	\N
23374	2009-08-03 16:34:09.695+01	\N	2009-08-03 16:34:09.741+01	\N	\N	\N
23375	2009-08-03 16:34:09.958+01	\N	\N	\N	\N	\N
23376	2009-08-03 16:34:10.008+01	\N	\N	\N	\N	\N
23377	2009-08-03 16:34:10.198+01	\N	2009-08-03 16:34:10.241+01	\N	\N	\N
23378	2009-08-03 16:34:10.65+01	\N	\N	\N	\N	\N
23379	2009-08-03 16:34:10.696+01	\N	\N	\N	\N	\N
23380	2009-08-03 16:34:10.854+01	\N	2009-08-03 16:34:10.898+01	\N	\N	\N
23381	2009-08-03 16:34:11.115+01	\N	\N	\N	\N	\N
23382	2009-08-03 16:34:11.161+01	\N	\N	\N	\N	\N
23383	2009-08-03 16:34:11.313+01	\N	2009-08-03 16:34:11.359+01	\N	\N	\N
23384	2009-08-03 16:34:11.485+01	\N	\N	\N	\N	\N
23385	2009-08-03 16:34:11.53+01	\N	\N	\N	\N	\N
23386	2009-08-03 16:34:11.683+01	\N	2009-08-03 16:34:11.728+01	\N	\N	\N
23387	2009-08-03 16:34:12.09+01	\N	\N	\N	\N	\N
23388	2009-08-03 16:34:12.134+01	\N	\N	\N	\N	\N
23389	2009-08-03 16:34:12.323+01	\N	2009-08-03 16:34:12.375+01	\N	\N	\N
23390	2009-08-03 16:34:12.879+01	\N	\N	\N	\N	\N
23391	2009-08-03 16:34:12.924+01	\N	\N	\N	\N	\N
23392	2009-08-03 16:34:13.077+01	\N	2009-08-03 16:34:13.121+01	\N	\N	\N
23393	2009-08-03 16:34:13.494+01	\N	\N	\N	\N	\N
23394	2009-08-03 16:34:13.539+01	\N	\N	\N	\N	\N
23395	2009-08-03 16:34:13.693+01	\N	2009-08-03 16:34:13.738+01	\N	\N	\N
23396	2009-08-03 16:34:13.994+01	\N	\N	\N	\N	\N
23397	2009-08-03 16:34:14.039+01	\N	\N	\N	\N	\N
23398	2009-08-03 16:34:14.192+01	\N	2009-08-03 16:34:14.246+01	\N	\N	\N
23399	2009-08-03 16:34:14.541+01	\N	\N	\N	\N	\N
23400	2009-08-03 16:34:14.587+01	\N	\N	\N	\N	\N
23401	2009-08-03 16:34:14.741+01	\N	2009-08-03 16:34:14.785+01	\N	\N	\N
23402	2009-08-03 16:34:15.006+01	\N	\N	\N	\N	\N
23403	2009-08-03 16:34:15.05+01	\N	\N	\N	\N	\N
23404	2009-08-03 16:34:15.25+01	\N	2009-08-03 16:34:15.295+01	\N	\N	\N
23405	2009-08-03 16:34:15.377+01	\N	\N	\N	\N	\N
23406	2009-08-03 16:34:15.422+01	\N	\N	\N	\N	\N
23407	2009-08-03 16:34:15.613+01	\N	2009-08-03 16:34:15.657+01	\N	\N	\N
23408	2009-08-03 16:34:16.067+01	\N	\N	\N	\N	\N
23409	2009-08-03 16:34:16.112+01	\N	\N	\N	\N	\N
23410	2009-08-03 16:34:16.267+01	\N	2009-08-03 16:34:16.313+01	\N	\N	\N
23411	2009-08-03 16:34:16.467+01	\N	\N	\N	\N	\N
23412	2009-08-03 16:34:16.511+01	\N	\N	\N	\N	\N
23413	2009-08-03 16:34:16.666+01	\N	2009-08-03 16:34:16.71+01	\N	\N	\N
23414	2009-08-03 16:34:16.982+01	\N	\N	\N	\N	\N
23415	2009-08-03 16:34:17.029+01	\N	\N	\N	\N	\N
23416	2009-08-03 16:34:17.187+01	\N	2009-08-03 16:34:17.231+01	\N	\N	\N
23417	2009-08-03 16:34:17.454+01	\N	\N	\N	\N	\N
23418	2009-08-03 16:34:17.499+01	\N	\N	\N	\N	\N
23419	2009-08-03 16:34:17.667+01	\N	2009-08-03 16:34:17.712+01	\N	\N	\N
23420	2009-08-03 16:34:18.082+01	\N	\N	\N	\N	\N
23421	2009-08-03 16:34:18.127+01	\N	\N	\N	\N	\N
23422	2009-08-03 16:34:18.283+01	\N	2009-08-03 16:34:18.328+01	\N	\N	\N
23423	2009-08-03 16:34:18.566+01	\N	\N	\N	\N	\N
23424	2009-08-03 16:34:18.612+01	\N	\N	\N	\N	\N
23425	2009-08-03 16:34:18.841+01	\N	2009-08-03 16:34:18.887+01	\N	\N	\N
23426	2009-08-03 16:34:19.149+01	\N	\N	\N	\N	\N
23427	2009-08-03 16:34:19.194+01	\N	\N	\N	\N	\N
23428	2009-08-03 16:34:19.349+01	\N	2009-08-03 16:34:19.396+01	\N	\N	\N
23429	2009-08-03 16:34:19.634+01	\N	\N	\N	\N	\N
23430	2009-08-03 16:34:19.68+01	\N	\N	\N	\N	\N
23431	2009-08-03 16:34:19.838+01	\N	2009-08-03 16:34:19.884+01	\N	\N	\N
23432	2009-08-03 16:34:20.40+01	\N	\N	\N	\N	\N
23433	2009-08-03 16:34:20.445+01	\N	\N	\N	\N	\N
23434	2009-08-03 16:34:20.603+01	\N	2009-08-03 16:34:20.65+01	\N	\N	\N
23435	2009-08-03 16:34:20.89+01	\N	\N	\N	\N	\N
23436	2009-08-03 16:34:20.936+01	\N	\N	\N	\N	\N
23437	2009-08-03 16:34:21.099+01	\N	2009-08-03 16:34:21.146+01	\N	\N	\N
23438	2009-08-03 16:34:21.301+01	\N	\N	\N	\N	\N
23439	2009-08-03 16:34:21.347+01	\N	\N	\N	\N	\N
23440	2009-08-03 16:34:21.51+01	\N	2009-08-03 16:34:21.555+01	\N	\N	\N
23441	2009-08-03 16:34:21.869+01	\N	\N	\N	\N	\N
23442	2009-08-03 16:34:21.916+01	\N	\N	\N	\N	\N
23443	2009-08-03 16:34:22.083+01	\N	2009-08-03 16:34:22.129+01	\N	\N	\N
23444	2009-08-03 16:34:22.286+01	\N	\N	\N	\N	\N
23445	2009-08-03 16:34:22.332+01	\N	\N	\N	\N	\N
23446	2009-08-03 16:34:22.496+01	\N	2009-08-03 16:34:22.541+01	\N	\N	\N
23447	2009-08-03 16:34:22.893+01	\N	\N	\N	\N	\N
23448	2009-08-03 16:34:22.94+01	\N	\N	\N	\N	\N
23449	2009-08-03 16:34:23.099+01	\N	2009-08-03 16:34:23.145+01	\N	\N	\N
23450	2009-08-03 16:34:23.414+01	\N	\N	\N	\N	\N
23451	2009-08-03 16:34:23.459+01	\N	\N	\N	\N	\N
23452	2009-08-03 16:34:23.617+01	\N	2009-08-03 16:34:23.662+01	\N	\N	\N
23453	2009-08-03 16:34:23.782+01	\N	\N	\N	\N	\N
23454	2009-08-03 16:34:23.828+01	\N	\N	\N	\N	\N
23455	2009-08-03 16:34:23.989+01	\N	2009-08-03 16:34:24.034+01	\N	\N	\N
23456	2009-08-03 16:34:24.165+01	\N	\N	\N	\N	\N
23457	2009-08-03 16:34:24.212+01	\N	\N	\N	\N	\N
23458	2009-08-03 16:34:24.372+01	\N	2009-08-03 16:34:24.419+01	\N	\N	\N
23459	2009-08-03 16:34:24.504+01	\N	\N	\N	\N	\N
23460	2009-08-03 16:34:24.549+01	\N	\N	\N	\N	\N
23461	2009-08-03 16:34:24.745+01	\N	2009-08-03 16:34:24.791+01	\N	\N	\N
23462	2009-08-03 16:34:24.873+01	\N	\N	\N	\N	\N
23463	2009-08-03 16:34:24.919+01	\N	\N	\N	\N	\N
23464	2009-08-03 16:34:25.081+01	\N	2009-08-03 16:34:25.127+01	\N	\N	\N
23465	2009-08-03 16:34:25.371+01	\N	\N	\N	\N	\N
23466	2009-08-03 16:34:25.417+01	\N	\N	\N	\N	\N
23467	2009-08-03 16:34:25.579+01	\N	2009-08-03 16:34:25.626+01	\N	\N	\N
23468	2009-08-03 16:34:25.82+01	\N	\N	\N	\N	\N
23469	2009-08-03 16:34:25.866+01	\N	\N	\N	\N	\N
23470	2009-08-03 16:34:26.025+01	\N	2009-08-03 16:34:26.071+01	\N	\N	\N
23471	2009-08-03 16:34:26.155+01	\N	\N	\N	\N	\N
23472	2009-08-03 16:34:26.203+01	\N	\N	\N	\N	\N
23473	2009-08-03 16:34:26.377+01	\N	2009-08-03 16:34:26.424+01	\N	\N	\N
23474	2009-08-03 16:34:26.657+01	\N	\N	\N	\N	\N
23475	2009-08-03 16:34:26.705+01	\N	\N	\N	\N	\N
23476	2009-08-03 16:34:26.87+01	\N	2009-08-03 16:34:26.916+01	\N	\N	\N
23477	2009-08-03 16:34:27.15+01	\N	\N	\N	\N	\N
23478	2009-08-03 16:34:27.196+01	\N	\N	\N	\N	\N
23479	2009-08-03 16:34:27.397+01	\N	2009-08-03 16:34:27.454+01	\N	\N	\N
23480	2009-08-03 16:34:27.539+01	\N	\N	\N	\N	\N
23481	2009-08-03 16:34:27.587+01	\N	\N	\N	\N	\N
23482	2009-08-03 16:34:27.748+01	\N	2009-08-03 16:34:27.796+01	\N	\N	\N
23483	2009-08-03 16:34:28.066+01	\N	\N	\N	\N	\N
23484	2009-08-03 16:34:28.113+01	\N	\N	\N	\N	\N
23485	2009-08-03 16:34:28.274+01	\N	2009-08-03 16:34:28.322+01	\N	\N	\N
23486	2009-08-03 16:34:28.569+01	\N	\N	\N	\N	\N
23487	2009-08-03 16:34:28.617+01	\N	\N	\N	\N	\N
23488	2009-08-03 16:34:28.778+01	\N	2009-08-03 16:34:28.825+01	\N	\N	\N
23489	2009-08-03 16:34:29.058+01	\N	\N	\N	\N	\N
23490	2009-08-03 16:34:29.106+01	\N	\N	\N	\N	\N
23491	2009-08-03 16:34:29.267+01	\N	2009-08-03 16:34:29.313+01	\N	\N	\N
23492	2009-08-03 16:34:29.596+01	\N	\N	\N	\N	\N
23493	2009-08-03 16:34:29.644+01	\N	\N	\N	\N	\N
23494	2009-08-03 16:34:29.844+01	\N	2009-08-03 16:34:29.892+01	\N	\N	\N
23495	2009-08-03 16:34:30.126+01	\N	\N	\N	\N	\N
23496	2009-08-03 16:34:30.173+01	\N	\N	\N	\N	\N
23497	2009-08-03 16:34:30.334+01	\N	2009-08-03 16:34:30.381+01	\N	\N	\N
23498	2009-08-03 16:34:30.854+01	\N	\N	\N	\N	\N
23499	2009-08-03 16:34:30.902+01	\N	\N	\N	\N	\N
23500	2009-08-03 16:34:31.066+01	\N	2009-08-03 16:34:31.119+01	\N	\N	\N
23501	2009-08-03 16:34:31.504+01	\N	\N	\N	\N	\N
23502	2009-08-03 16:34:31.559+01	\N	\N	\N	\N	\N
23503	2009-08-03 16:34:31.761+01	\N	2009-08-03 16:34:31.809+01	\N	\N	\N
23504	2009-08-03 16:34:32.084+01	\N	\N	\N	\N	\N
23505	2009-08-03 16:34:32.132+01	\N	\N	\N	\N	\N
28001	2009-08-03 16:35:02.493+01	\N	\N	\N	\N	\N
30003	2009-08-03 16:35:12.233+01	\N	\N	\N	\N	\N
30004	2009-08-03 16:35:12.259+01	\N	\N	\N	\N	\N
32001	\N	\N	\N	\N	\N	\N
32002	2009-08-03 16:35:21.327+01	\N	\N	\N	\N	\N
32003	2009-08-03 16:35:21.342+01	\N	\N	\N	\N	\N
32004	2009-08-03 16:35:21.352+01	\N	\N	\N	\N	\N
32005	2009-08-03 16:35:21.395+01	\N	\N	\N	\N	\N
32006	2009-08-03 16:35:21.421+01	\N	\N	\N	\N	\N
32007	\N	\N	\N	\N	\N	\N
32008	2009-08-03 16:35:21.454+01	\N	\N	\N	\N	\N
32009	2009-08-03 16:35:21.473+01	\N	\N	\N	\N	\N
32010	2009-08-03 16:35:21.485+01	\N	\N	\N	\N	\N
32011	2009-08-03 16:35:21.504+01	\N	\N	\N	\N	\N
32012	2009-08-03 16:35:21.526+01	\N	\N	\N	\N	\N
32013	2009-08-03 16:35:21.549+01	\N	\N	\N	\N	\N
32014	2009-08-03 16:35:21.573+01	\N	\N	\N	\N	\N
32015	2009-08-03 16:35:21.601+01	\N	\N	\N	\N	\N
32016	2009-08-03 16:35:21.628+01	\N	\N	\N	\N	\N
32017	2009-08-03 16:35:21.698+01	\N	\N	\N	\N	\N
32018	2009-08-03 16:35:21.757+01	\N	\N	\N	\N	\N
32019	2009-08-03 16:35:21.787+01	\N	\N	\N	\N	\N
32020	2009-08-03 16:35:21.807+01	\N	\N	\N	\N	\N
32021	2009-08-03 16:35:21.839+01	\N	\N	\N	\N	\N
32022	\N	The plate is a physival plate that will be kept in fridge	\N	\N	\N	\N
32023	2009-08-03 16:35:21.872+01	\N	\N	\N	\N	\N
32024	2009-08-03 16:35:21.881+01	\N	\N	\N	\N	\N
32025	2009-08-03 16:35:21.898+01	\N	\N	\N	\N	\N
32026	\N	The plate is a physival plate that will be kept in fridge	\N	\N	\N	\N
32027	2009-08-03 16:35:21.923+01	\N	\N	\N	\N	\N
32028	2009-08-03 16:35:21.932+01	\N	\N	\N	\N	\N
32029	2009-08-03 16:35:21.95+01	\N	\N	\N	\N	\N
32030	\N	Capture PCR Product into pOPIN vectors	\N	\N	\N	\N
32031	2009-08-03 16:35:21.976+01	\N	\N	\N	\N	\N
32032	2009-08-03 16:35:21.984+01	\N	\N	\N	\N	\N
32033	2009-08-03 16:35:21.994+01	\N	\N	\N	\N	\N
32034	2009-08-03 16:35:22.004+01	\N	\N	\N	\N	\N
32035	2009-08-03 16:35:22.024+01	\N	\N	\N	\N	\N
32036	2009-08-03 16:35:22.045+01	\N	\N	\N	\N	\N
32037	\N	Transform E.coli with plasmid, generic heat shock protocol	\N	\N	\N	\N
32038	2009-08-03 16:35:22.075+01	\N	\N	\N	\N	\N
32039	2009-08-03 16:35:22.086+01	\N	\N	\N	\N	\N
32040	2009-08-03 16:35:22.098+01	\N	\N	\N	\N	\N
32041	2009-08-03 16:35:22.108+01	\N	\N	\N	\N	\N
32042	2009-08-03 16:35:22.118+01	\N	\N	\N	\N	\N
32043	2009-08-03 16:35:22.137+01	\N	\N	\N	\N	\N
32044	2009-08-03 16:35:22.158+01	\N	\N	\N	\N	\N
32045	2009-08-03 16:35:22.18+01	\N	\N	\N	\N	\N
32046	2009-08-03 16:35:22.202+01	\N	\N	\N	\N	\N
32047	2009-08-03 16:35:22.226+01	\N	\N	\N	\N	\N
32048	2009-08-03 16:35:22.25+01	\N	\N	\N	\N	\N
32049	2009-08-03 16:35:22.274+01	\N	\N	\N	\N	\N
34001	\N	\N	\N	\N	\N	\N
34002	2009-08-03 16:35:31.602+01	\N	\N	\N	\N	\N
34003	2009-08-03 16:35:31.617+01	\N	\N	\N	\N	\N
34004	2009-08-03 16:35:31.626+01	\N	\N	\N	\N	\N
34005	2009-08-03 16:35:31.671+01	\N	\N	\N	\N	\N
34006	2009-08-03 16:35:31.697+01	\N	\N	\N	\N	\N
34007	\N	\N	\N	\N	\N	\N
34008	2009-08-03 16:35:31.726+01	\N	\N	\N	\N	\N
34009	2009-08-03 16:35:31.735+01	\N	\N	\N	\N	\N
34010	2009-08-03 16:35:31.754+01	\N	\N	\N	\N	\N
34011	\N	\N	\N	\N	\N	\N
34012	2009-08-03 16:35:31.795+01	\N	\N	\N	\N	\N
34013	2009-08-03 16:35:31.806+01	\N	\N	\N	\N	\N
34014	2009-08-03 16:35:31.815+01	\N	\N	\N	\N	\N
34015	2009-08-03 16:35:31.825+01	\N	\N	\N	\N	\N
34016	2009-08-03 16:35:31.836+01	\N	\N	\N	\N	\N
34017	2009-08-03 16:35:31.848+01	\N	\N	\N	\N	\N
34018	2009-08-03 16:35:32.477+01	\N	\N	\N	\N	\N
34019	2009-08-03 16:35:32.49+01	\N	\N	\N	\N	\N
34020	2009-08-03 16:35:32.502+01	\N	\N	\N	\N	\N
34021	2009-08-03 16:35:32.516+01	\N	\N	\N	\N	\N
34022	2009-08-03 16:35:32.53+01	\N	\N	\N	\N	\N
34023	2009-08-03 16:35:32.545+01	\N	\N	\N	\N	\N
34024	2009-08-03 16:35:32.561+01	\N	\N	\N	\N	\N
34025	2009-08-03 16:35:32.577+01	\N	\N	\N	\N	\N
34026	2009-08-03 16:35:32.594+01	\N	\N	\N	\N	\N
34027	2009-08-03 16:35:32.624+01	\N	\N	\N	\N	\N
34028	2009-08-03 16:35:32.657+01	\N	\N	\N	\N	\N
34029	2009-08-03 16:35:32.694+01	\N	\N	\N	\N	\N
34030	\N	\N	\N	\N	\N	\N
34031	2009-08-03 16:35:32.734+01	\N	\N	\N	\N	\N
34032	2009-08-03 16:35:32.751+01	\N	\N	\N	\N	\N
34033	2009-08-03 16:35:32.771+01	\N	\N	\N	\N	\N
34034	2009-08-03 16:35:32.791+01	\N	\N	\N	\N	\N
34035	2009-08-03 16:35:32.815+01	\N	\N	\N	\N	\N
34036	2009-08-03 16:35:32.839+01	\N	\N	\N	\N	\N
34037	2009-08-03 16:35:32.864+01	\N	\N	\N	\N	\N
34038	2009-08-03 16:35:32.89+01	\N	\N	\N	\N	\N
34039	2009-08-03 16:35:32.917+01	\N	\N	\N	\N	\N
34040	2009-08-03 16:35:32.946+01	\N	\N	\N	\N	\N
34041	2009-08-03 16:35:32.976+01	\N	\N	\N	\N	\N
34042	2009-08-03 16:35:33.008+01	\N	\N	\N	\N	\N
34043	2009-08-03 16:35:33.04+01	\N	\N	\N	\N	\N
34044	2009-08-03 16:35:33.061+01	\N	\N	\N	\N	\N
34045	2009-08-03 16:35:33.095+01	\N	\N	\N	\N	\N
34046	2009-08-03 16:35:33.128+01	\N	\N	\N	\N	\N
34047	2009-08-03 16:35:33.162+01	\N	\N	\N	\N	\N
34048	2009-08-03 16:35:33.203+01	\N	\N	\N	\N	\N
34049	\N	\N	\N	\N	\N	\N
34050	2009-08-03 16:35:33.238+01	\N	\N	\N	\N	\N
34051	2009-08-03 16:35:33.247+01	\N	\N	\N	\N	\N
34052	2009-08-03 16:35:33.255+01	\N	\N	\N	\N	\N
34053	2009-08-03 16:35:33.265+01	\N	\N	\N	\N	\N
34054	2009-08-03 16:35:33.274+01	\N	\N	\N	\N	\N
36001	2009-08-03 16:35:41.109+01	N-terminal T7-tag plus optional C-terminal\n        His-Tag	2009-08-03 16:35:41.365+01	\N	\N	\N
36003	2009-08-03 16:35:41.412+01	5440bp	\N	\N	\N	\N
36002	2009-08-03 16:35:41.394+01	\N	2009-08-03 16:35:41.401+01	\N	\N	\N
36004	2009-08-03 16:35:41.434+01	\N	\N	\N	\N	\N
36005	2009-08-03 16:35:41.442+01	\N	\N	\N	\N	\N
36006	2009-08-03 16:35:41.447+01	\N	\N	\N	\N	\N
36007	2009-08-03 16:35:41.451+01	BamHI-EcoRI-SacI-SalI-HindIII-NotI-XhoI	\N	\N	\N	\N
36008	2009-08-03 16:35:41.456+01	HHHHHH	\N	\N	\N	\N
36009	2009-08-03 16:35:41.461+01	C|CATGG	\N	\N	\N	\N
36010	2009-08-03 16:35:41.466+01	\N	\N	\N	\N	\N
36011	2009-08-03 16:35:41.472+01	Non-expression: NovaBlue[TM], JM109, DH5alpha; Expression: BL21(DE3), and BL21(DE3)pLysS	\N	\N	\N	\N
36012	2009-08-03 16:35:41.477+01	\N	\N	\N	\N	\N
36013	2009-08-03 16:35:41.52+01	N-terminal 6xHis tag	2009-08-03 16:35:41.533+01	\N	\N	\N
36014	2009-08-03 16:35:41.546+01	\N	\N	\N	\N	\N
36016	2009-08-03 16:35:41.564+01	6354	\N	\N	\N	\N
36015	2009-08-03 16:35:41.553+01	\N	2009-08-03 16:35:41.56+01	\N	\N	\N
36017	2009-08-03 16:35:41.578+01	\N	\N	\N	\N	\N
36018	2009-08-03 16:35:41.585+01	\N	\N	\N	\N	\N
36019	2009-08-03 16:35:41.593+01	MSYYHHHHHHLESTSLYKKAG	\N	\N	\N	\N
36020	2009-08-03 16:35:41.601+01	\N	\N	\N	\N	\N
36021	2009-08-03 16:35:41.609+01	\N	\N	\N	\N	\N
36022	2009-08-03 16:35:41.617+01	bacterial death gene, exchanged for                  gene of interest in the entry clone	\N	\N	\N	\N
36023	2009-08-03 16:35:41.626+01	\N	\N	\N	\N	\N
36024	2009-08-03 16:35:41.634+01	\N	\N	\N	\N	\N
36025	2009-08-03 16:35:41.643+01	Propagation: One Shot[TM] ccdB Survival[TM] 2 T1R; Expression: BL21-AI[TM]	\N	\N	\N	\N
36026	2009-08-03 16:35:41.652+01	\N	\N	\N	\N	\N
36027	2009-08-03 16:35:41.697+01	N-terminal pelB signal sequence for potential\n        periplasmic localization, plus optional C-terminal His-Tag\n    	2009-08-03 16:35:41.715+01	\N	\N	\N
36029	2009-08-03 16:35:41.745+01	5360bp	\N	\N	\N	\N
36028	2009-08-03 16:35:41.727+01	\N	2009-08-03 16:35:41.737+01	\N	\N	\N
36030	2009-08-03 16:35:41.759+01	\N	\N	\N	\N	\N
36031	2009-08-03 16:35:41.77+01	\N	\N	\N	\N	\N
36032	2009-08-03 16:35:41.781+01	MKYLLPTAAAGLLLLAAQPAMA|	\N	\N	\N	\N
36033	2009-08-03 16:35:41.792+01	NcoI-BamHI-EcoRI-SacI-SalI-HindIII-NotI-XhoI	\N	\N	\N	\N
36034	2009-08-03 16:35:41.803+01	C|CATGG	\N	\N	\N	\N
36035	2009-08-03 16:35:41.814+01	CA|TATG	\N	\N	\N	\N
36036	2009-08-03 16:35:41.827+01	HHHHHH	\N	\N	\N	\N
36037	2009-08-03 16:35:41.839+01	\N	\N	\N	\N	\N
36038	2009-08-03 16:35:41.851+01	Non-expression: NovaBlue[TM], JM109, DH5alpha; Expression: BL21(DE3), and BL21(DE3)pLysS	\N	\N	\N	\N
36039	2009-08-03 16:35:41.863+01	\N	\N	\N	\N	\N
36040	2009-08-03 16:35:41.907+01	 N-terminal His-Tag/thrombin/T7 Tag plus optional\n        C-terminal His-Tag	2009-08-03 16:35:41.93+01	\N	\N	\N
36042	2009-08-03 16:35:41.969+01	5369bp	\N	\N	\N	\N
36041	2009-08-03 16:35:41.945+01	\N	2009-08-03 16:35:41.958+01	\N	\N	\N
36043	2009-08-03 16:35:41.985+01	\N	\N	\N	\N	\N
36044	2009-08-03 16:35:41.999+01	\N	\N	\N	\N	\N
36045	2009-08-03 16:35:42.013+01	MGSSHHHHHHSSGLVPR|GSH	\N	\N	\N	\N
36046	2009-08-03 16:35:42.027+01	BamHI-EcoRI-SacI-SalI-HindIII-NotI-XhoI	\N	\N	\N	\N
36047	2009-08-03 16:35:42.041+01	CA|TATG	\N	\N	\N	\N
36048	2009-08-03 16:35:42.057+01	C|CATGG	\N	\N	\N	\N
36049	2009-08-03 16:35:42.072+01	HHHHHH	\N	\N	\N	\N
36050	2009-08-03 16:35:42.088+01	LVPR|GS	\N	\N	\N	\N
36051	2009-08-03 16:35:42.103+01	\N	\N	\N	\N	\N
36052	2009-08-03 16:35:42.119+01	Non-expression: NovaBlue[TM], JM109, DH5alpha; Expression: BL21, BL21(DE3), and BL21(DE3)pLysS, B834, BLR, Rosetta[TM]	\N	\N	\N	\N
36053	2009-08-03 16:35:42.134+01	\N	\N	\N	\N	\N
36054	2009-08-03 16:35:42.178+01	Derived from pTriEx2/Ampicillin, N-Term cleavable\n        secretion leader sequence, C-Term His Tag	2009-08-03 16:35:42.208+01	\N	\N	\N
36055	2009-08-03 16:35:42.226+01	\N	\N	\N	\N	\N
36057	2009-08-03 16:35:42.277+01	\N	\N	\N	\N	\N
36056	2009-08-03 16:35:42.246+01	\N	2009-08-03 16:35:42.262+01	\N	\N	\N
36058	2009-08-03 16:35:42.297+01	\N	\N	\N	\N	\N
36059	2009-08-03 16:35:42.315+01	\N	\N	\N	\N	\N
36060	2009-08-03 16:35:42.332+01	MGILPSPGMPALLSLVSLLSVLLMGCVA|ETG	\N	\N	\N	\N
36061	2009-08-03 16:35:42.35+01	KHHHHHH	\N	\N	\N	\N
36062	2009-08-03 16:35:42.368+01	\N	\N	\N	\N	\N
36063	2009-08-03 16:35:42.417+01	Gateway[TM] Entry vector	2009-08-03 16:35:42.464+01	\N	\N	\N
36065	2009-08-03 16:35:43.388+01	5585	\N	\N	\N	\N
36064	2009-08-03 16:35:43.353+01	\N	2009-08-03 16:35:43.372+01	\N	\N	\N
36066	2009-08-03 16:35:43.511+01	\N	\N	\N	\N	\N
36067	2009-08-03 16:35:43.53+01	\N	\N	\N	\N	\N
36068	2009-08-03 16:35:43.55+01	\N	\N	\N	\N	\N
36069	2009-08-03 16:35:43.57+01	bacterial death gene, exchanged for                  gene of interest in the entry clone	\N	\N	\N	\N
36070	2009-08-03 16:35:43.592+01	\N	\N	\N	\N	\N
36071	2009-08-03 16:35:43.713+01	\N	\N	\N	\N	\N
36072	2009-08-03 16:35:43.733+01	Propagation: One Shot[TM] ccdB Survival[TM] 2 T1R; Non-expression: TOP10, OmniMAX[TM] 2-T1R	\N	\N	\N	\N
36073	2009-08-03 16:35:43.754+01	\N	\N	\N	\N	\N
36074	2009-08-03 16:35:43.804+01	Gateway[TM] Entry vector	2009-08-03 16:35:43.945+01	\N	\N	\N
36076	2009-08-03 16:35:44.011+01	4762	\N	\N	\N	\N
36075	2009-08-03 16:35:43.969+01	\N	2009-08-03 16:35:43.992+01	\N	\N	\N
36077	2009-08-03 16:35:44.037+01	\N	\N	\N	\N	\N
36078	2009-08-03 16:35:44.06+01	\N	\N	\N	\N	\N
36079	2009-08-03 16:35:44.084+01	\N	\N	\N	\N	\N
36080	2009-08-03 16:35:44.107+01	\N	\N	\N	\N	\N
36081	2009-08-03 16:35:44.13+01	bacterial death gene, exchanged for                  gene of interest in the entry clone	\N	\N	\N	\N
36082	2009-08-03 16:35:44.153+01	\N	\N	\N	\N	\N
36083	2009-08-03 16:35:44.176+01	\N	\N	\N	\N	\N
36084	2009-08-03 16:35:44.20+01	Propagation: One Shot[TM] ccdB Survival[TM] 2 T1R; Non-expression: TOP10, OmniMAX[TM] 2-T1R	\N	\N	\N	\N
36085	2009-08-03 16:35:44.223+01	\N	\N	\N	\N	\N
36086	2009-08-03 16:35:44.284+01	\N	2009-08-03 16:35:44.33+01	\N	\N	\N
36088	2009-08-03 16:35:44.403+01	3512	\N	\N	\N	\N
36087	2009-08-03 16:35:44.356+01	\N	2009-08-03 16:35:44.381+01	\N	\N	\N
36089	2009-08-03 16:35:44.431+01	\N	\N	\N	\N	\N
36090	2009-08-03 16:35:44.457+01	\N	\N	\N	\N	\N
36091	2009-08-03 16:35:44.483+01	\N	\N	\N	\N	\N
36092	2009-08-03 16:35:44.508+01	\N	\N	\N	\N	\N
36093	2009-08-03 16:35:44.534+01	fused to bacterial death gene, cloning disrupts                 expression of the lacZalpha-ccdB gene fusion	\N	\N	\N	\N
36094	2009-08-03 16:35:44.56+01	bacterial death gene, cloning disrupts                 expression of the lacZalpha-ccdB gene fusion	\N	\N	\N	\N
36095	2009-08-03 16:35:44.588+01	NsiI-HindIII-KpnI-SacI-BamHI-SpeI-EcoRI-EcoRI-PstI-EcoRV-NotI-XhoI-NsiI-XbaI-ApaI	\N	\N	\N	\N
36096	2009-08-03 16:35:44.611+01	\N	\N	\N	\N	\N
36097	2009-08-03 16:35:44.635+01	One Shot[R] TOP10	\N	\N	\N	\N
36098	2009-08-03 16:35:44.659+01	\N	\N	\N	\N	\N
36099	2009-08-03 16:35:44.711+01	N-terminal His-Tag sequence followed by a\n        thrombin site and three cloning sites	2009-08-03 16:35:44.757+01	\N	\N	\N
36101	2009-08-03 16:35:44.82+01	5708bp	\N	\N	\N	\N
36100	2009-08-03 16:35:44.782+01	\N	2009-08-03 16:35:44.802+01	\N	\N	\N
36102	2009-08-03 16:35:44.942+01	\N	\N	\N	\N	\N
36103	2009-08-03 16:35:44.97+01	\N	\N	\N	\N	\N
36104	2009-08-03 16:35:44.988+01	MGSSHHHHHHSSGLVPR|GSH	\N	\N	\N	\N
36105	2009-08-03 16:35:45.005+01	NdeI-XhoI-BamHI	\N	\N	\N	\N
36106	2009-08-03 16:35:45.023+01	C|CATGG	\N	\N	\N	\N
36107	2009-08-03 16:35:45.041+01	LVPR|GS	\N	\N	\N	\N
36108	2009-08-03 16:35:45.059+01	\N	\N	\N	\N	\N
36109	2009-08-03 16:35:45.078+01	Non-expression:NovaBlue[TM], JM109, DH5alpha; Expression: BL21(DE3), and BL21(DE3)pLysS	\N	\N	\N	\N
36110	2009-08-03 16:35:45.095+01	\N	\N	\N	\N	\N
36111	2009-08-03 16:35:45.139+01	Derived from pET28a/Kanamycin, C-Term His Tag\n    	2009-08-03 16:35:45.258+01	\N	\N	\N
36113	2009-08-03 16:35:45.294+01	\N	\N	\N	\N	\N
36112	2009-08-03 16:35:45.272+01	\N	2009-08-03 16:35:45.284+01	\N	\N	\N
36114	2009-08-03 16:35:45.309+01	\N	\N	\N	\N	\N
36115	2009-08-03 16:35:45.321+01	KHHHHHH	\N	\N	\N	\N
36116	2009-08-03 16:35:45.333+01	\N	\N	\N	\N	\N
36117	2009-08-03 16:35:45.472+01	Derived from pET28a/Kanamycin, N-Term cleavable His\n        Tag	2009-08-03 16:35:45.489+01	\N	\N	\N
36119	2009-08-03 16:35:45.517+01	\N	\N	\N	\N	\N
36118	2009-08-03 16:35:45.501+01	\N	2009-08-03 16:35:45.51+01	\N	\N	\N
36120	2009-08-03 16:35:45.53+01	\N	\N	\N	\N	\N
36121	2009-08-03 16:35:45.54+01	MGSSHHHHHHSSGLEVLFQ|GP	\N	\N	\N	\N
36122	2009-08-03 16:35:45.551+01	\N	\N	\N	\N	\N
36123	2009-08-03 16:35:45.687+01	Derived from pTriEx2/Ampicillin, C-Term His Tag\n    	2009-08-03 16:35:45.705+01	\N	\N	\N
36125	2009-08-03 16:35:45.734+01	GenbankID EF372397	\N	\N	\N	\N
36124	2009-08-03 16:35:45.716+01	\N	2009-08-03 16:35:45.727+01	\N	\N	\N
36126	2009-08-03 16:35:45.747+01	\N	\N	\N	\N	\N
36127	2009-08-03 16:35:45.757+01	KHHHHHH	\N	\N	\N	\N
36128	2009-08-03 16:35:45.767+01	\N	\N	\N	\N	\N
36129	2009-08-03 16:35:45.903+01	Derived from pTriEx2/Ampicillin, N-Term cleavable\n        His Tag	2009-08-03 16:35:45.918+01	\N	\N	\N
36131	2009-08-03 16:35:45.939+01	GenbankID EF372398	\N	\N	\N	\N
36130	2009-08-03 16:35:45.927+01	\N	2009-08-03 16:35:45.935+01	\N	\N	\N
36132	2009-08-03 16:35:45.95+01	\N	\N	\N	\N	\N
36133	2009-08-03 16:35:45.958+01	MAHHHHHHSSGLEVLFQ|GP	\N	\N	\N	\N
36134	2009-08-03 16:35:45.965+01	\N	\N	\N	\N	\N
36135	2009-08-03 16:35:46.002+01	Derived from pTriEx2/Ampicillin, N-Term cleavable\n        secretion leader sequence, C-Term His Tag	2009-08-03 16:35:46.015+01	\N	\N	\N
36137	2009-08-03 16:35:46.038+01	GenbankID EF372394	\N	\N	\N	\N
36136	2009-08-03 16:35:46.025+01	\N	2009-08-03 16:35:46.033+01	\N	\N	\N
36138	2009-08-03 16:35:46.049+01	\N	\N	\N	\N	\N
36139	2009-08-03 16:35:46.057+01	MGILPSPGMPALLSLVSLLSVLLMGCVA|ETG	\N	\N	\N	\N
36140	2009-08-03 16:35:46.065+01	KHHHHHH	\N	\N	\N	\N
36141	2009-08-03 16:35:46.073+01	\N	\N	\N	\N	\N
36142	2009-08-03 16:35:46.108+01	Derived from pTriEx2/Ampicillin, N-Term cleavable\n        secretion leader and separate cleavable N-term His tag	2009-08-03 16:35:46.12+01	\N	\N	\N
36144	2009-08-03 16:35:46.141+01	\N	\N	\N	\N	\N
36143	2009-08-03 16:35:46.129+01	\N	2009-08-03 16:35:46.137+01	\N	\N	\N
36145	2009-08-03 16:35:46.151+01	\N	\N	\N	\N	\N
36146	2009-08-03 16:35:46.159+01	MGILPSPGMPALLSLVSLLSVLLMGCVA|ETMAHHHHHHSSGLEVLFQ|GP	\N	\N	\N	\N
36147	2009-08-03 16:35:46.166+01	\N	\N	\N	\N	\N
36148	2009-08-03 16:35:46.205+01	Derived from pTriEx2/Ampicillin, N-Term His Tag\n    	2009-08-03 16:35:46.218+01	\N	\N	\N
36150	2009-08-03 16:35:46.239+01	\N	\N	\N	\N	\N
36149	2009-08-03 16:35:46.227+01	\N	2009-08-03 16:35:46.234+01	\N	\N	\N
36151	2009-08-03 16:35:46.252+01	\N	\N	\N	\N	\N
36152	2009-08-03 16:35:46.259+01	MAHHHHHHSSG	\N	\N	\N	\N
36153	2009-08-03 16:35:46.267+01	\N	\N	\N	\N	\N
36154	2009-08-03 16:35:46.299+01	Derived from pTriEx2/Ampicillin, combined cleavable\n        N-Term His Tag with GST fusion	2009-08-03 16:35:46.312+01	\N	\N	\N
36156	2009-08-03 16:35:46.333+01	GenbankID EF372395	\N	\N	\N	\N
36155	2009-08-03 16:35:46.321+01	\N	2009-08-03 16:35:46.328+01	\N	\N	\N
36157	2009-08-03 16:35:46.343+01	\N	\N	\N	\N	\N
36158	2009-08-03 16:35:46.351+01	MAHHHHHH-GST-SSGLEVLFQ|GP	\N	\N	\N	\N
36159	2009-08-03 16:35:46.359+01	\N	\N	\N	\N	\N
36160	2009-08-03 16:35:46.392+01	Derived from pET28a/Kanamycin, combined cleavable\n        N-Term His Tag with GST fusion	2009-08-03 16:35:46.406+01	\N	\N	\N
36162	2009-08-03 16:35:46.428+01	\N	\N	\N	\N	\N
36161	2009-08-03 16:35:46.415+01	\N	2009-08-03 16:35:46.423+01	\N	\N	\N
36163	2009-08-03 16:35:46.438+01	\N	\N	\N	\N	\N
36164	2009-08-03 16:35:46.446+01	MGSSHHHHHHSSGLEVLFQ|GP	\N	\N	\N	\N
36165	2009-08-03 16:35:46.454+01	\N	\N	\N	\N	\N
36166	2009-08-03 16:35:46.486+01	Derived from pTriEx2/Ampicillin, combined cleavable\n        N-Term His Tag with MBP fusion	2009-08-03 16:35:46.499+01	\N	\N	\N
36168	2009-08-03 16:35:46.522+01	GenbankID EF372396	\N	\N	\N	\N
36167	2009-08-03 16:35:46.509+01	\N	2009-08-03 16:35:46.517+01	\N	\N	\N
36169	2009-08-03 16:35:46.533+01	\N	\N	\N	\N	\N
36170	2009-08-03 16:35:46.541+01	MAHHHHHHSSG-MBP-LEVLFQ|GP	\N	\N	\N	\N
36171	2009-08-03 16:35:46.549+01	\N	\N	\N	\N	\N
36172	2009-08-03 16:35:46.584+01	Derived from pET28a/Kanamycin, combined cleavable\n        N-Term His Tag with SUMO fusion	2009-08-03 16:35:46.598+01	\N	\N	\N
36174	2009-08-03 16:35:46.621+01	\N	\N	\N	\N	\N
36173	2009-08-03 16:35:46.608+01	\N	2009-08-03 16:35:46.616+01	\N	\N	\N
36175	2009-08-03 16:35:46.632+01	\N	\N	\N	\N	\N
36176	2009-08-03 16:35:46.641+01	MGSSHHHHHH-SUMO|	\N	\N	\N	\N
36177	2009-08-03 16:35:46.649+01	\N	\N	\N	\N	\N
36178	2009-08-03 16:35:46.683+01	 yeast (S. cerevisiae)/E. coli shuttle vector	2009-08-03 16:35:46.698+01	\N	\N	\N
36180	2009-08-03 16:35:46.722+01	5181bp	\N	\N	\N	\N
36179	2009-08-03 16:35:46.708+01	\N	2009-08-03 16:35:46.717+01	\N	\N	\N
36181	2009-08-03 16:35:46.733+01	\N	\N	\N	\N	\N
36182	2009-08-03 16:35:46.789+01	\N	\N	\N	\N	\N
36183	2009-08-03 16:35:46.797+01	\N	\N	\N	\N	\N
36184	2009-08-03 16:35:46.804+01	\N	\N	\N	\N	\N
36185	2009-08-03 16:35:46.812+01	\N	\N	\N	\N	\N
36186	2009-08-03 16:35:46.82+01	DH5alpha	\N	\N	\N	\N
36187	2009-08-03 16:35:46.827+01	\N	\N	\N	\N	\N
36188	2009-08-03 16:35:46.885+01	N-terminal fusion options: His-Tag, S-Tag.\n        C-terminal fusion options: HSV-Tag, His-Tag	2009-08-03 16:35:46.898+01	\N	\N	\N
36190	2009-08-03 16:35:46.921+01	5457bp	\N	\N	\N	\N
36189	2009-08-03 16:35:46.907+01	\N	2009-08-03 16:35:46.916+01	\N	\N	\N
36191	2009-08-03 16:35:46.932+01	\N	\N	\N	\N	\N
36192	2009-08-03 16:35:46.94+01	\N	\N	\N	\N	\N
36193	2009-08-03 16:35:46.948+01	\N	\N	\N	\N	\N
36194	2009-08-03 16:35:46.955+01	\N	\N	\N	\N	\N
36195	2009-08-03 16:35:46.963+01	HHHHHH	\N	\N	\N	\N
36196	2009-08-03 16:35:46.971+01	KETAAAKPGAQHMDS	\N	\N	\N	\N
36197	2009-08-03 16:35:46.979+01	SmaI-PshAI-BseRI-EcoRV-SacI-BamHI-EcoRI-BglII-AscI-PstI-Sse8387I-PinAI-KpnI-NspV-HindIII-NotI-PvuII-Bst1107I-PmlI-XhoI-DraIII	\N	\N	\N	\N
36198	2009-08-03 16:35:46.987+01	C|CATGG	\N	\N	\N	\N
36199	2009-08-03 16:35:46.995+01	GACAA|GAGTC	\N	\N	\N	\N
36200	2009-08-03 16:35:47.003+01	CCC|GGG	\N	\N	\N	\N
36201	2009-08-03 16:35:47.011+01	SerGlnProGluLeuAlaProGluAspProGluAsp	\N	\N	\N	\N
36202	2009-08-03 16:35:47.018+01	HHHHHH	\N	\N	\N	\N
36203	2009-08-03 16:35:47.026+01	LVPA|GS	\N	\N	\N	\N
36204	2009-08-03 16:35:47.034+01	DDDDK|	\N	\N	\N	\N
36205	2009-08-03 16:35:47.041+01	\N	\N	\N	\N	\N
36206	2009-08-03 16:35:47.049+01	Non-expression: NovaBlue[TM]; Expression: NovaBlue[TM] plus bacteriophage CE6,              Origami[TM](DE3)pLacI, Origami 2(DE3)pLacI, Origami B(DE3)pLacI, Rosetta[TM](DE3)pLacI, Rosetta 2(DE3)pLacI,             Rosetta-gami[TM](DE3)pLacI, Rosetta-gami 2(DE3)pLacI, Rosetta-gami B(DE3)pLacI, RosettaBlue(DE3)pLacI, Tuner[TM](DE3)pLacI,              TriEx Sf9, BHK	\N	\N	\N	\N
36207	2009-08-03 16:35:47.058+01	\N	\N	\N	\N	\N
38003	2009-08-04 16:19:26.635+01	\N	\N	\N	\N	\N
38004	2009-08-04 16:19:26.666+01	\N	\N	\N	\N	\N
38011	2009-08-04 16:19:26.822+01	xtalPiMS default score scheme	\N	\N	\N	\N
38012	2009-08-04 16:19:26.854+01	Has been send to synchrotron	\N	\N	\N	\N
38013	2009-08-04 16:19:26.869+01	X-rayable Crystals	\N	\N	\N	\N
38014	2009-08-04 16:19:26.885+01	Crystal needing optimisation	\N	\N	\N	\N
38015	2009-08-04 16:19:26.916+01	Crystal-like precipitate	\N	\N	\N	\N
38016	2009-08-04 16:19:26.932+01	Granular precipitates, etc	\N	\N	\N	\N
38017	2009-08-04 16:19:26.947+01	Insoluble misfolded protein	\N	\N	\N	\N
38018	2009-08-04 16:19:26.963+01	Phase separation, etc	\N	\N	\N	\N
38019	2009-08-04 16:19:26.979+01	Non-protein crystal	\N	\N	\N	\N
38020	2009-08-04 16:19:26.994+01	Nothing in the drop	\N	\N	\N	\N
38021	\N	\N	\N	\N	\N	\N
38022	2009-08-04 16:19:27.135+01	\N	\N	\N	\N	\N
38023	2009-08-04 16:19:27.182+01	\N	\N	\N	\N	\N
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
8003	\N
8004	\N
8005	\N
8006	\N
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

COPY cryz_dropannotation (cmdlineparam, scoredate, labbookentryid, holderid, imageid, sampleid, scoreid, softwareid) FROM stdin;
\.


--
-- Data for Name: cryz_image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_image (filename, filepath, mimetype, labbookentryid, imagetypeid, instrumentid, sampleid, scheduledtaskid) FROM stdin;
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
16711680	Synchrotron	0	38012	38011
16711680	Crystals	1	38013	38011
58880	Optimisable Crystal	2	38014	38011
16776960	Crystal Precipitate	3	38015	38011
15980451	Normal Precipitate	4	38016	38011
15980451	Aggregate	5	38017	38011
15980451	Other	6	38018	38011
11184895	Salt Crystals	7	38019	38011
14474460	Clear	8	38020	38011
\.


--
-- Data for Name: cryz_scoringscheme; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cryz_scoringscheme (name, version, labbookentryid) FROM stdin;
default	\N	38011
\.


--
-- Data for Name: expe_experiment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experiment (enddate, islocked, name, startdate, status, labbookentryid, experimentgroupid, experimenttypeid, groupid, instrumentid, methodid, operatorid, protocolid, researchobjectiveid) FROM stdin;
\.


--
-- Data for Name: expe_experimentgroup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_experimentgroup (enddate, name, purpose, startdate, labbookentryid) FROM stdin;
\.


--
-- Data for Name: expe_inputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_inputsample (amount, amountdisplayunit, amountunit, name, "role", labbookentryid, experimentid, refinputsampleid, sampleid, order_) FROM stdin;
\.


--
-- Data for Name: expe_instrument; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_instrument (model, name, pressure, pressuredisplayunit, serialnumber, tempdisplayunit, temperature, labbookentryid, defaultimagetypeid, locationid, manufacturerid) FROM stdin;
\N	BioStore	\N	\N	\N	\N	21	38003	38001	\N	\N
\N	Microscope	\N	\N	\N	\N	23	38004	38002	\N	\N
\.


--
-- Data for Name: expe_instrument2insttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY expe_instrument2insttype (instrumentid, instrumenttypeid) FROM stdin;
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
\N	card	\N	\N	28001	\N	\N
\.


--
-- Data for Name: hold_holdca2abstholders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdca2abstholders (abstholderid, holdercategoryid) FROM stdin;
28001	24006
\.


--
-- Data for Name: hold_holdca2absthoty; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holdca2absthoty (holdercategoryid, abstractholdertypeid) FROM stdin;
24001	26001
24001	26002
24001	26003
24001	26004
24001	26005
24003	26006
24003	26007
24001	26008
24001	26009
24001	26010
24004	26011
24003	26012
24003	26013
24001	26014
24003	26015
24001	26016
24001	26017
24003	26018
24001	26019
24001	26020
24003	26021
24002	26022
24003	26023
24001	26024
24001	26025
24005	26026
24006	26027
\.


--
-- Data for Name: hold_holder; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hold_holder (crystalnumber, enddate, startdate, abstractholderid, firstsampleid, lasttaskid) FROM stdin;
0	\N	\N	28001	\N	\N
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

COPY hold_refsampleposition (colposition, rowposition, subposition, labbookentryid, refholderid, refsampleid) FROM stdin;
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
22004	glacial acetic acid	0
22004	 ethanoic acid	1
22007	methyl cyanide	0
22007	 ACS	1
22013	ADA	0
22013	 N-(carbamoylmethyl)iminodiacetic acid	1
22016	3,6-bis(Dimethylamino) acridine monohydrochloride compound with zinc chloride	0
22016	 Euchrysine 3RX	1
22019	BIS	0
22028	ATP	0
22028	 ATP-2Na	1
22031	adonite	0
22031	 ribitol	1
22037	Bovine albumin	0
22037	 BSA	1
22037	 Cohn Fraction V	2
22040	AlCl3	0
22043	AEBSF	0
22046	6-aminohexanoic acid	0
22046	 e-aminocaproic acid	1
22049	AMPD	0
22049	 ammediol	1
22052	4-amino-3-methylphenol	0
22052	 2-amino-5-hydroxytoluene	1
22052	 4-hydroxy-2-methylaniline	2
22058	ammonium hydrogen carbonate	0
22064	Salmiac	0
22067	diammonium hydrogen citrate	0
22067	 di-ammonium hydrogen citrate	1
22067	 citric acid,ammonium salt 	2
22070	tri-ammonium citrate	0
22070	 tri-ammonium citrate	1
22070	 citric acid,triammonium salt 	2
22076	formic acid,ammonium salt 	0
22082	ammonium iron(II) sulfate hexahydrate	0
22094	ammonium phosphate dibasic	0
22094	  ammonium hydrogenphosphate	1
22094	 diammonium hydrogenphosphate	2
22094	 di-ammonium hydrogenphosphate	3
22097	ammonium phosphate monobasic	0
22097	  ammonium dihydrogenphosphate	1
22097	 prim-ammonium phosphate	2
22097	 mono-ammonium phosphate	3
22097	 ammonium di-hydrogenphosphate	4
22097	 ammonium di-hydrogen phosphate	5
22100	ammonium sulfate	0
22100	 (NH4)2SO4	1
22103	diammonium tartrate	0
22103	 di-ammonium tartrate	1
22103	 L-(+)-tartaric acid,diammonium salt 	2
22106	trifluoroacetic acid,ammonium salt 	0
22109	ammonium rhodanide	0
22115	3-[(1,1-dimethyl-2-hydroxyethyl)amino]-2-hydroxypropanesulfonic acid	0
22121	ANS	0
22136	nonanedioic acid	0
22139	5,5-diethyl-2,4,6(1H,3H,5H)-pyrimidinetrione	0
22139	 5,5-diethylbarbituric acid	1
22139	 barbitone	2
22139	 veronal	3
22145	barium dichloride	0
22154	amidinobenzene hydrochloride	0
22154	 benzamidinium chloride	1
22154	 benzenecarboximidamide hydrochloride	2
22160	phenolsulphonic acid	0
22160	 benzenesulfaonic acid	1
22163	carboxybenzene	0
22172	oxyneurine	0
22172	 (carboxymethyl)trimethylammonium inner salt	1
22172	 glycine betaine	2
22179	N,N-bis(2-hydroxyethyl)glycine 	0
22185	vitamin H	0
22188	N-biotinoyl-N'-(6-maleimidohexanoyl)hydrazide	0
22191	6,6'-iminodihexylamine	0
22191	 bis(6-aminohexyl)amine	1
22194	Bis-Tris	0
22194	 2,2-Bis(hydroxymethyl)-2,2',2''-nitrilotriethanol	1
22194	 Bis(2-hydroxyethyl)amino-tris(hydroxymethyl)methane	2
22194	 2-Bis(2-hydroxyethyl)amino-2-(hydroxymethyl)-1,3-propanediol 	3
22200	1,3-Bis[tris(hydroxymethyl)methylamino]propane	0
22200	 Bis-Tris propane 	1
22203	acidum boricum	0
22215	omega-bromo-4-nitroacetophenone	0
22215	 4-nitrophenacyl bromide	1
22218	C12E23	0
22221	1,2-Butylene glycol	0
22224	1,3-butylene glycol	0
22227	tetramethylene glycol	0
22227	 1,4-butylene glycol 	1
22236	1-butanol	0
22236	 butyl alcohol	1
22239	tert-butyl alcohol	0
22239	 trimethyl carbinol	1
22239	 2-methyl-2-propanol	2
22242	methyltert-butyl ether	0
22242	 MTBE	1
22245	n-butyl formate	0
22245	 formic acid,butyl ester	1
22248	gamma-hydroxybutyric acid lactone	0
22248	 4-hydroxbutyric acid lactone	1
22248	 GBL	2
22251	dodecyl octaethylene glycol ether	0
22251	dodecyloctaglycol	1
22251	polyoxyethylene (8) lauryl ether	2
22254	4-[cyclohexylamino]-1-butanesulfonic acid	0
22257	cadmium acetate anhydrous	0
22260	cadmium(II) bromide	0
22272	cadmium sulfate	0
22278	Ca acetate	0
22284	Ca chloride	0
22293	calcium sulfate	0
22296	2-oxohexamethyleneimine	0
22296	 aza-2-cycloheptanone	1
22299	3-cyclohexylamino-1-propanesulfonic acid	0
22302	3-(cyclohexylamino)-2-hydroxy-1-propanesulfonic acid	0
22302	 CAPSO Free Acid	1
22305	alpha-Carboxybenzylpenicillin disodium salt	0
22305	Geopen	1
22305	Pyopen	2
22305	Carindapen	3
22305	Fugacillin	4
22305	Microcillin	5
22305	Anabactyl	6
22305	Carbecin	7
22305	Gripenin	8
22305	Pyocianil	9
22305	Pyoclox	10
22320	cesium sulfate	0
22323	Hexadecyltrimethylammonium chloride	0
22326	3-[(3-cholamidopropyl)dimethylammonio]-1-propanesulfonate	0
22329	2-(cyclohexylamino)ethanesulfonic acid	0
22332	sodium p-toluenesulfonchloramide trihydrate	0
22332	 N-chloro-p-toluenesulfonamide sodium salt	1
22332	  tosylchloramide sodium	2
22335	 D-(-)-threo-2-dichloroacetamido-1-(4-nitrophenyl)-1,3-propanediol	0
22335	 chloromycetin	1
22338	methylidyne trichloride,trichloromethane	0
22341	chloronaphthol	0
22344	4-chloro-7-nitro-1,2,3-benzoxadiazole	0
22344	 NBD-chloride	1
22347	 1,2-dilauroyl-sn-glycero-3-phosphocholine	0
22347	 12:0 PC 	1
22350	3alpha,7alpha,12alpha-trihydroxy-5beta-cholanic acid	0
22350	 cholanic acid	1
22356	cobaltous chloride	0
22356	 cobalt dichloride	1
22359	cobaltous chloride hexahydrate	0
22362	copper(I) bromide	0
22362	 cuprous bromide	1
22365	copper(I) chloride	0
22365	 cuprous chloride	1
22368	copper dichloride	0
22368	 cupric chloride	1
22371	cupric chloride dihydrate	0
22374	cupric sulfate pentahydrate	0
22377	(alpha-methylguanido)acetic acid	0
22377	 N-amidinosarcosine	1
22380	(R)-2-amino-3-mercaptopropionic acid	0
22383	CTP	0
22386	bromine cyanide	0
22392	N-(D-glucityl)-N-methyldecanamide	0
22392	MEGA-10	1
22398	sodium deoxycholate	0
22398	 3-alpha,12-alpha-dihydroxy-5-beta-cholanic acid,sodium salt	1
22398	 7-deoxycholic acid,sodium salt	2
22398	 desoxycholic acid,sodium salt	3
22401	dATP	0
22404	dCTP	0
22407	 dGTP, dGTP-Na3	0
22410	dTTP	0
22413	dextran sulfate,sodium salt	0
22413	 dextran sodium sulphate	1
22413	 dextran sodium sulfate	2
22413	 dextranesulfate sodium salt	3
22416	1,6-hexanediamine	0
22416	 hexamethylenediamine	1
22419	1,8-octanediamine	0
22419	 octamethylenediamine	1
22422	1,5-pentanediamine	0
22422	 pentamethylenediamine	1
22422	 Cadaverine	2
22425	di-PIP	0
22425	 di-mu-iodobis(ethylenediamine)diplatinum (II)	1
22425	 platinum(2+),bis(1,2-ethanediamine-N,N')di-mu-iododi-,dinitrate 	2
22431	methylene chloride	0
22434	DCC	0
22437	n-dodecyl b-D-maltoside	0
22437	 dodecyl-b-D-maltoside	1
22437	 lauryl-b-D-maltoside	2
22437	 lauryl maltoside	3
22440	1,2-diheptanoyl-sn-glycero-3-phosphocholine	0
22440	 07:0 PC 	1
22443	DDAO	0
22443	 1-decanamine,N,N-dimethyl-,N-oxide	1
22446	LDAO	0
22446	 lauryldimethylamineN-oxide	1
22446	 DDAO	2
22449	octyl sulfobetaine	0
22449	 SB3-8	1
22449	 n-octyl-N,N-dimethyl-3-ammonio-1-propane sulfonate	2
22452	3-(N,N-dimethylhexadecylammonio)propanesulfonate	0
22452	 3-(hexadecyldimethylammonio)propanesulfonate	1
22452	 3-(palmityldimethylammonio)propanesulfonate	2
22452	 palmityl sulfobetaine	3
22452	 SB3-16	4
22455	DETC	0
22455	 sodium diethyldithiocarbamate trihydrate	1
22455	 cupral	2
22458	aether	0
22458	 ether	1
22458	 ethyl ether	2
22461	DEP	0
22461	 DEPC	1
22461	 diethyl dicarbonate	2
22461	 diethyl oxydiformate	3
22461	 ethoxyformic acid anhydride	4
22464	DMF	0
22464	 dimethyl formamide	1
22467	DMS	0
22470	1,4-dioxane	0
22470	 diethylene oxide	1
22473	dithizone	0
22476	2,6-pyridinedicarboxylic acid	0
22479	3-[N,N-bis(2-hydroxyethyl)amino]-2-hydroxypropanesulfonic acid	0
22479	 N,N-bis(2-hydroxyethyl)-3-amino-2-hydroxypropanesulfonic acid	1
22482	 DTNB	0
22482	 3-carboxy-4-nitrophenyl disulfide	1
22482	 6,6'-dinitro-3,3'-dithiodibenzoic acid	2
22482	 bis(3-carboxy-4-nitrophenyl) disulfide	3
22482	 Ellman's Reagent	4
22485	DTT	0
22485	 Cleland's reagent	1
22485	 1,4 dithio-DL-threitol	2
22488	 1,2-dilauroyl-sn-glycero-3-phosphocholine	0
22488	 12:0 PC 	1
22491	decyl-b-D-maltopyranoside	0
22491	 n-decyl b-D-maltoside	1
22491	 decyl-b-D-maltoside 	2
22494	 1,2-dimyristoyl-sn-glycero-3-phosphocholine	0
22494	 14:0 PC 	1
22497	 1,2-dioleoyl-sn-glycero-3-[phospho-rac-(1-glycerol)] (sodium salt)	0
22497	 18:1,[cis-9] PG 	1
22500	DMSO	0
22500	 methyl sulphoxide	1
22503	dodecyl glucoside	0
22506	SB3-12	0
22506	 3-(N,N-dimethyldodecylammonio)propanesulfonate	1
22506	 3-(N,N-dimethyllaurylammonio)propanesulfonate	2
22506	 3-(lauryldimethylammonio)propanesulfonate	3
22509	 1,2-dioleoyl-sn-glycero-3-phosphocholine	0
22509	 18:1,[cis-9] PC 	1
22512	(S)-2-methyl-1,4,5,6-tetrahydropyrimidine-4-carboxylic acid	0
22512	 THP(B)	1
22515	meso-erythritol	0
22515	 1,2,3,4-butanetetrol	1
22515	 meso-1,2,3,4-tetrahydroxybutane	2
22518	ethyl alcohol	0
22518	  EtOH	1
22519	2-aminoethanol	0
22519	 monoethanolamine	1
22519	 2-aminoethyl alcohol	2
22522	3,8-diamino-5-ethyl-6-phenylphenanthridinium bromide	0
22522	 EtBr	1
22522	 homidium bromide	2
22525	diaminoethanetetra acetic acid	0
22525	 ethylene diamine tetraacetic acid	1
22528	disodium ethylenediaminetetraacetate dihydrate	0
22528	 ethylenediamine tetraacetic acid,disodium salt dihydrate	1
22528	 EDTA-Na	2
22531	ethyl glycol	0
22531	 ethylene glycol monoethyl ether	1
22534	EtOAc	0
22537	1,2-diaminoethane	0
22540	1,2-ethanediol	0
22543	ethylene glycol -bis (beta aminoethylether) - N,N,N',N' tetraacetic acid	0
22543	 glycol ether diamine tetraacetic acid	1
22543	 egtazic acid	2
22543	 ethylene-bis(oxyethylenenitrilo)tetraacetic acid	3
22546	N-[2-hydroxyethyl]piperazine-N'[3-propanesulfonic acid]	0
22546	 4-(2-hydroxyethyl)-1-piperazinepropanesulfonic acid	1
22546	 4-(2-hydroxyethyl)piperazine-1-propanesulfonic acid	2
22546	 N-(2-hydroxyethyl)piperazine-N'-(3-propanesulfonic acid)	3
22546	 HEPPS	4
22549	NEM	0
22552	ethylmercuric phosphate	0
22555	2-butanone	0
22555	 methyl ethyl ketone	1
22558	Woodward's Reagent K	0
22561	fructose	0
22561	 D-fructose	1
22561	 D-levulose	2
22561	 fruit sugar	3
22564	L-fructose	0
22567	formalin	0
22570	amide C1	0
22570	 formic amide	1
22576	5-butylpicolinic acid	0
22576	 5-butylpyridine-2-carboxylic acid	1
22579	D-glucose	0
22579	 dextrose	1
22579	 glucose	2
22585	L-glutathione oxidized	0
22585	 glutathiol	1
22585	 GSSG	2
22588	gamma-L-glutamyl-L-cysteinyl-glycine	0
22588	 GSH	1
22591	(S)-2-aminoglutaric acid	0
22591	 Glu	1
22591	 (S)-2-aminopentanedioic acid	2
22591	 glutaminic acid	3
22594	D-2-aminoglutaramic acid	0
22594	 D-glutamic acid 5-amide	1
22597	glutaric dialdehyde solution	0
22597	 pentane-1,5-dial	1
22600	1,2,3-propanetriol	0
22600	 glycerin	1
22600	 glycerol anhydrous	2
22603	aminoacetic acid	0
22603	 glycocoll	1
22603	 aminoethanoic acid	2
22606	betaine-trimethyl-d9hydrochloride	0
22606	 N,N,N-trimethyl-d9-glycine hydrochloride	1
22609	Gly-Gly-Gly	0
22609	 triglycine	1
22612	guanidine hydrochloride	0
22612	 aminomethanamidine hydrochloride	1
22612	 guanidium chloride	2
22612	 aminoformamidine hydrochloride	3
22615	GDP	0
22618	GTP	0
22618	 5'-GTP-Na2	1
22621	N-[2-hydroxyethyl]piperazine-N'-[4-butanesulfonic acid]	0
22621	 N-(2-hydroxyethyl)piperazine-N'-(4-butanesulfonic acid)	1
22624	N-(2-hydroxyethyl)piperazine-N'-(2-ethanesulfonic acid)	0
22624	 4-(2-hydroxyethyl)piperazine-1-ethanesulfonic acid	1
22627	HEPES-Na	0
22627	 N-(2-hydroxyethyl)piperazine-N'-(2-ethanesulfonic acid) sodium salt	1
22627	 4-(2-Hydroxyethyl)piperazine-1-ethanesulfonic acid sodium salt	2
22630	N-[2-hydroxyethyl]piperazine-N'-[2-hydroxypropanesulfonic acid]	0
22633	4-Hydroxbutyric acid lactone	0
22636	heptyl-b-D-glucopyranoside	0
22636	 heptyl-beta-D-glucopyranoside	1
22639	cobalt hexammine trichloride	0
22639	 hexaamminecobalt(III) chloride	1
22642	cetyl trimethyl ammoniumbromide,palmityltrimethylammonium bromide	0
22642	 cetrimonium bromide	1
22642	 cetyltrimethylammonium bromide	2
22642	 CTAB	3
22642	 cetrimide	4
22645	hexafluoroisopropanol	0
22645	 1,1,1,3,3,3-hexafluoro-2-propanol	1
22648	hexamethylene glycol	0
22651	2,5-hexylene glycol	0
22657	hexyl-beta-D-glucopyranoside	0
22660	HCl	0
22663	acetoin	0
22663	 acetyl methyl carbinol	1
22666	hydroxylammonium chloride	0
22669	5-nitrosalicylaldehyde	0
22672	2-hydroxy-2-methylpropionic acid	0
22672	 2-methyllactic acid	1
22675	glyoxaline	0
22675	 1,3-diaza-2,4-cyclopentadiene	1
22678	surauto	0
22678	 monoiodoacetamide	1
22678	 2-iodoacetamide	2
22678	 alpha-iodoacetamide	3
22678	 alpha-iodo acetamide	4
22681	monoiodoacetic acid	0
22681	 iodoacetate	1
22681	 monoiodine acetate	2
22684	 1,5-I-AEDANS	0
22684	 5-[2-(Iodoacetamido)ethylamino]naphthalene-1-sulfonic acid	1
22684	 N-Iodoacetyl-N'-(5-sulfo-1-naphthyl)ethylenediamine	2
22684	 Hudson-Weber-Reagent	3
22687	ferric chloride hexahydrate	0
22687	 iron(III) chloride hexahydrate	1
22690	ferric chloride hexahydrate	0
22693	ferrous chloride tetrahydrate	0
22696	ferrous sulfate	0
22696	 ferrous sulphate,iron(II) sulfate	1
22699	IPTG	0
22702	sec-propyl alcohol	0
22702	 isopropanol	1
22702	 isopropyl alcohol	2
22705	polypropylene glycol 500 mono-2-aminoethyl mono-2-methoxyethyl ether	0
22711	kanamycin sulfate	0
22711	 kanamycin sulfate salt,kanamycin monosulfate	1
22714	1-beta-D-galactopyranosyl-4-alpha-D-glucopyranose	0
22714	 lactose anhydricum	1
22714	 milk sugar	2
22717	N-dodecanoyl-N-methylglycine sodium salt	0
22717	 sarkosyl	1
22717	 sarkosyl NL	2
22720	lead diacetate trihydrate, bis(acetato)trihydroxytrilead	0
22720	 lead(+2) salt trihydrate	1
22723	acetic acid,lead salt	0
22723	 lead acetate	1
22723	 dibasic lead acetate	2
22723	 lead diacetate	3
22726	acetyl-Leu-Leu-Arg-al trifluoroacetate salt	0
22738	lithium chloride anhydrous	0
22744	lithium citrate, trilithium citrate, citric acid,trilithium salt	0
22744	 1,2,3-Propanetricarboxylic acid,2-hydroxy-,trilithium salt	1
22750	formic acid lithium salt	0
22753	lithium citrate	0
22753	 citric acid,trilithium salt tetrahydrate	1
22753	 lithium citrate tetrahydrate	2
22753	  lithiumcitratetetrahydrate	3
22753	 trilithium citrate tetrahydrate	4
22753	  tri-lithium citrate 4-hydrate	5
22762	salicylic acid,lithium salt	0
22765	lithium sulfate	0
22765	 sulphuric acid,dilithium salt	1
22765	 sulfuric acid,dilithium salt	2
22768	lithium sulfate monohydrate	0
22768	 sulphuric acid,dilithium salt,monohydrate	1
22768	 sulfuric acid,dilithium salt,monohydrate,	2
22771	acetic acid,magnesium salt	0
22777	magnesium chloride	0
22783	magnesium diformate	0
22783	 formic acid,magnesium salt	1
22786	formic acid,magnesium salt magnesium formate	0
22789	magnesium dinitrate	0
22789	 nitric acid,magnesium salt	1
22789	 magnesium(II) nitrate	2
22795	magnesium sulfate	0
22798	magnesium sulfate hydrate	0
22801	kieserite, magnesium sulfate monohydrate	0
22804	magnesium sulfate monohydrate	0
22807	epsom salts	0
22807	 magnesium sulfate heptahydrate	1
22810	manganese dichloride	0
22810	 manganous chloride	1
22810	 manganese(II) chloride,anhydrous	2
22813	manganous chloride dihydrate	0
22813	 manganese dichloride dihydrate	1
22819	manganese dichloride tetrahydrate	0
22819	 manganous chloride tetrahydrate	1
22822	DL-malic acid	0
22822	 (+/-)-2-hydroxysuccinic acid	1
22822	 DL-hydroxybutanedioic acid	2
22825	propanedioc acid	0
22825	 1,3-propanedioic acid	1
22825	 dicarboxymethane	2
22828	mannite	0
22828	 cordycepic acid	1
22828	 mannitol	2
22828	 1,2,3,4,5,6-hexanehexol	3
22834	D-mannose	0
22834	 D-mannopyranose	1
22837	L-mannose	0
22837	 L-mannopyranose	1
22840	benzophenone-4-maleimide	0
22843	beta-mercaptoethanol	0
22843	 2-hydroxyethylmercaptan	1
22843	 BME	2
22843	 thioethylene glycol	3
22846	mercuric acetate	0
22846	 acetic acid,mercury(2+) salt	1
22849	mercuric chloride	0
22849	 mercury bichloride	1
22849	 mercury perchloride	2
22852	salyrganic acid	0
22852	 2-(3-hydroxymercurio-2-methoxypropylcarbamoyl)phenoxyacetic acid	1
22852	 2-[N-(3-Hydroxymercuri-2-methoxypropyl)carbamoyl]phenoxyacetic acid	2
22855	4-morpholineethanesulfonic acid	0
22855	  2-(N-morpholino)ethanesulfonic acid	1
22858	2-(N-morpholino)ethanesulfonic acid sodium salt	0
22858	 4-morpholineethanesulfonic acid sodium salt	1
22861	MeOH	0
22861	 methyl alcohol	1
22864	acetic acid,methyl ester	0
22864	 methyl ethanoate	1
22864	 methyl acetic ester	2
22867	methylmercury(II) chloride	0
22867	 mercury,chloromethyl-	1
22867	 methyl mercury(II) chloride	2
22867	 methylmercuric chloride	3
22867	 chloromethylmercury 	4
22870	MPD	0
22870	 hexylene glycol	1
22873	4-[N-morpholino]butanesulfonic acid	0
22873	 4-(N-morpholino)butanesulfonic acid	1
22876	3-(N-morpholino)propanesulfonic acid	0
22876	 4-morpholinepropanesulfonic acid	1
22879	3-[N-morpholino]-2-hydroxypropanesulfonic acid	0
22879	  beta-hydroxy-4-morpholinepropanesulfonic acid	1
22879	 3-morpholino-2-hydroxypropanesulfonic acid	2
22882	1,2,3,5/4,6-hexahydroxycyclohexane	0
22882	 meso-inositol	1
22882	 i-inositol	2
22885	beta-nicotinamide adenine dinucleotide phosphate sodium salt	0
22885	 b-NADP-Na	1
22885	 b-NADP-sodium salt	2
22885	 coenzyme II sodium salt	3
22885	 NADP	4
22885	 TPN-Na	5
22885	 TPN	6
22885	 triphosphopyridine nucleotide sodium salt	7
22888	 beta-NADPH	0
22888	 2'-NADPH	1
22888	 coenzyme II reduced tetrasodium salt	2
22888	 dihydronicotinamide adenine dinucleotide phosphate tetrasodium salt	3
22888	 NADPH Na4	4
22888	 TPNH2 Na4	5
22888	 triphosphopyridine nucleotide reduced tetrasodium salt	6
22891	NHS	0
22891	 1-hydroxy-2,5-pyrrolidinedione	1
22891	 HOSu	2
22891	 1-hydroxypyrrolidine-2,5-dione	3
22894	nickel chloride	0
22897	nickel chloride hydrate	0
22900	nickel chloride hexahydrate	0
22903	nickel sulphate	0
22903	 nickel sulfate	1
22903	 nickel(II) sulfate hexahydrate	2
22909	N-(D-glucityl)-N-methylnonanamide	0
22909	 MEGA-9	1
22909	 N-methyl-N-nonanoyl-D-glucamine	2
22912	polidocanol	0
22912	 polyoxyethylene 9 lauryl ether	1
22912	 dodecylnonaglycol	2
22912	 dodecyl nonaethylene glycol ether	3
22912	 C12E9	4
22915	polyoxyethylene 8 decyl ether	0
22915	 decyloctaglycol	1
22915	 decyl octaethylene glycol ether	2
22915	 C10E8	3
22918	OG	0
22918	 n-octyl b-D-glucopyranoside	1
22918	 n-Octyl glucoside	2
22918	 OGP	3
22921	OTG	0
22921	 OSGP	1
22924	n-octyl-b-D-glucoside	0
22924	 n-octyl-beta-D-glucopyranoside	1
22924	 BOG	2
22924	 n-octyl glucoside	3
22927	MEGA-8	0
22927	 OMEGA	1
22927	 N-methyl-N-octanoyl-D-glucamine	2
22927	 N-(D-Glucityl)-N-methyloctanamide	3
22930	n-nonyl-b-D-glucopyranoside	0
22930	 NG	1
22930	 n-nonyl-b-D-glucoside	2
22933	phosphoric acid	0
22939	p-chloromercuribenzoate	0
22939	 p-chloromercuribenzoic acid	1
22939	 4-chloromercuribenzoate	2
22942	4-(chloromercuri)benzensulfonic acid,sodium salt	0
22942	 p-chloromercuribenzenesulfonic acid	1
22942	 4-chloromercuribenzenesulfonate	2
22942	 4-chloromercuribenzenesulfonic acid	3
22942	 chloro(4-sulfophenyl)mercury	4
22945	PAR monosodium salt hydrate	0
22945	 PAR	1
22948	pentaerythritol ethoxylate (15/4 EO/OH)	0
22951	pentaerythritol propoxylate (5/4 PO/OH)	0
22954	polyoxyethylene 5 decyl ether	0
22954	 decylpentaglycol	1
22954	 decyl pentaethylene glycol ether	2
22954	 C10E5	3
22957	octylpentaglycol	0
22957	 octyl pentaethylene glycol ether	1
22957	 C8E5	2
22960	diethyl ketone	0
22960	 DEK	1
22960	 dimethylacetone	2
22960	 ethyl ketone	3
22960	 metacetone	4
22960	 propione	5
22963	N-isobutyrylpepstatin	0
22963	 isobutyryl-Val-Val-Sta-Ala-Sta	1
22969	hydroxybenzene	0
22972	phenolsulfonphthalein	0
22975	2,2-dihydroxyacetophenone	0
22978	mercury phenyl acetate	0
22981	1,2-Diacyl-sn-glycero-3-phosphocholine	0
22981	 3-sn-phosphatidylcholine	1
22981	 L-alpha-lecithin	2
22981	 azolectin	3
22984	piperazine-1,4-bis(2-ethanesulfonic acid)	0
22984	 1,4-piperazinediethanesulfonic acid	1
22984	 piperazine-N,N'-bis(2-ethanesulfonic acid)	2
22987	phenylmethylsulphonyl fluoride	0
22987	 alpha-toluenesulfonyl fluoride	1
22987	 benzylsulfonyl fluoride	2
22987	 phenylmethylsulfonyl fluoride	3
22990	poly(sodium acrylate)	0
22990	 sodium polyacrylate	1
22990	 polyacrylic acid 5100 sodium salt	2
22993	PEG 400	0
22993	 poly(ethylene glycol) 400	1
22996	PEG 200	0
22996	 poly(ethylene glycol) 200	1
22999	PEG 300	0
22999	 poly(ethylene glycol) 300	1
23002	PEG 600	0
23002	 poly(ethylene glycol) 600	1
23005	PEG 1000	0
23005	 PEG 1K	1
23005	 poly(ethylene glycol) 1000	2
23008	PEG 1500	0
23008	 PEG 1.5K	1
23008	 poly(ethylene glycol) 1500	2
23011	PEG 2000	0
23011	 PEG 2K	1
23011	 poly(ethylene glycol) 2000	2
23014	PEG 3000	0
23014	 PEG 3K	1
23014	 poly(ethylene glycol) 3000	2
23017	PEG 3350	0
23017	 polyethylene glycol 3.350	1
23017	 poly(ethylene glycol) 3350	2
23020	PEG 3350 monodisperse	0
23020	 polyethylene glycol 3.350 monodisperse	1
23020	 poly(ethylene glycol) 3350 monodisperse	2
23023	PEG 4000	0
23023	 PEG 4K	1
23023	 poly(ethylene glycol) 4000	2
23026	PEG 5000	0
23026	 PEG 5K	1
23026	 poly(ethylene glycol) 5000	2
23029	PEG 6000	0
23029	 PEG 6K	1
23029	 poly(ethylene glycol) 6000	2
23032	PEG 8000	0
23032	 PEG 8K	1
23032	 poly(ethylene glycol) 8000	2
23035	 PEG 10,000	0
23035	 PEG 10K	1
23035	 poly(ethylene glycol) 10,000 	2
23038	 PEG 20,000	0
23038	 PEG 20K	1
23038	 polyethylene glycol 20,000 	2
23041	PEG 550 MME	0
23041	 polyethylene glycol 550 MME	1
23041	 polyethylene glycol MME 550	2
23041	 methoxypolyethylene glycol 550	3
23041	 mono-methyl polyethylene glycol 550	4
23044	PEG 2000 MME	0
23044	 PEG 2K MME	1
23044	 polyethylene glycol 2000 MME	2
23044	 polyethylene glycol MME 2000	3
23044	 methoxypolyethylene glycol 2000	4
23044	 mono-methyl polyethylene glycol 2000	5
23047	PEG 5000 MME	0
23047	 PEG 5K MME	1
23047	 polyethylene glycol 5000 MME	2
23047	 polyethylene glycol MME 5000	3
23047	 methoxypolyethylene glycol 5000	4
23047	 mono-methyl polyethylene glycol 5000	5
23050	ethylene imine polymer	0
23050	 aziridine polymer	1
23050	 epamine	2
23050	 epomine	3
23050	 ethylenimine polymer	4
23050	 montrek	5
23050	 PEI	6
23050	 poly(ethylene imine)	7
23050	 poly(ethyleneimine)	8
23053	PPG 400	0
23053	 poly(propylene oxide)	1
23056	polymixin B sulfate	0
23056	 polymixin B sulphate salt	1
23059	poly(A) potassium salt	0
23062	PVA	0
23062	 elvanol	1
23062	 polyvinyl alcohol	2
23062	 ethenol homopolymer	3
23062	 polyviol	4
23062	 vinol	5
23062	 alvyl	6
23065	polyvidone	0
23065	 povidone	1
23065	 PVP	2
23068	1-palmitoyl-2-oleoyl-sn-glycero-3-phosphocholine	0
23068	 16:0-18:1,[cis-9] PC	1
23068	  1-(cis-9-octadecenoyl)-2-hexadecanoyl-sn-glycero-3-phosphocholine	2
23068	 L-alpha-phosphatidylcholine,beta-palmitoyl-gamma-oleoyl	3
23071	piperazine-N,N'-bis-[2-hydroxypropanesulfonic acid]	0
23071	  piperazine-1,4-bis(2-hydroxypropanesulfonic acid) dihydrate	1
23071	 piperazine-N,N'-bis(2-hydroxypropanesulfonic acid)	2
23074	K(acac)	0
23077	kalii bromidum	0
23080	kalii chloridum	0
23083	citric acid,monopotassium salt	0
23083	 potassium dihydrogen citrate	1
23086	citric acid,tripotassium salt	0
23092	potassium di-hydrogen orthophosphate (mono-basic)	0
23092	 monopotassium phosphate	1
23092	 kalii dihydrogenophosphas	2
23092	 potassium dihydrogen orthophosphate	3
23092	 prim.-potassium phosphate	4
23095	di-potassium hydrogen orthophosphate (di-basic)	0
23095	 di-potassium hydrogen phosphate	1
23095	 di-potassium orthophosphate	2
23095	 phosphoric acid,dipotassium salt	3
23098	di-potassium hydrogen phosphate trihydrate	0
23098	 dipotassium hydrogen phosphate trihydrate	1
23098	 dipotassium phosphate	2
23110	potassium platinum(IV) chloride	0
23113	caustic potash	0
23116	kalii permanganas	0
23119	kalii iodidum	0
23125	potassium/sodium tartrate	0
23125	 K-Na tartrate tetrahydrate	1
23125	 L(+)-tartaric acid potassium sodium salt	2
23125	 Rochelle salt	3
23125	 Seignette salt	4
23125	 sodium potassium tartrate	5
23125	 Na/K tartrate	6
23128	Seignette salt	0
23128	 Rochelle salt	1
23128	 potassium sodium tartrate	2
23128	 sodium potassium L-tartrate	3
23128	 sodium potassium (dl)-tartrate	4
23128	 potassium sodium L(+)-tartrate	5
23128	 monopotassium monosodium tartrate	6
23131	potassium sulfate	0
23134	di-potassium tartrate	0
23134	 potassium tartrate	1
23134	 2,3-dihydroxy-(2R,3R)-butanedioic acid dipotassium salt	2
23134	 tartaric acid dipotassium salt	3
23134	 dipotassium L-(+)-tartrate	4
23134	 neutral potassium tartrate	5
23134	 soluble tartar	6
23137	potassium gold(III) chloride	0
23140	potassium platinum(II) chloride	0
23152	potassium rhodanide	0
23155	proline	0
23155	 (S)-pyrrolidine-2-carboxylic acid	1
23158	isopropanol	0
23158	 propan-2-ol	1
23158	 isopropyl alcohol	2
23158	 sec-propyl alcohol	3
23161	propylene glycol	0
23164	1,3-dihydroxypropane	0
23164	 trimethylene glycol	1
23167	ethyl cyanide	0
23170	azine	0
23170	  azabenzine	1
23173	protamine sulfate	0
23173	  clupeine	1
23176	3-hydroxy-5-(hydroxymethyl)-2-methyl-4-pyridinecarboxaldehyde hydrochloride	0
23176	 PL HCl	1
23179	O-alpha-D-galactopyranosyl-(1-6)-alpha-D-glucopyranosyl beta-D-fructofuranoside	0
23179	 melitose	1
23179	 melitriose	2
23182	(-)-riboflavin	0
23182	 lactoflavin	1
23182	 vitamin B2	2
23182	 vitamin G	3
23182	 6,7-dimethyl-9-d-ribitylisoalloxazine	4
23182	 flavaxin	5
23182	 beflavin	6
23182	 7,8-dimethyl-10-(d-ribo-2,3,4,5-tetrahydroxypentyl)riboflavinequinone	7
23182	 hyflavin	8
23182	 lactoflavin	9
23182	 lactoflavine	10
23182	 ribipca	11
23182	 riboderm	12
23182	 riboflavinequinone	13
23191	argenti nitras	0
23194	acetic acid,sodium salt	0
23194	 Na Acetate	1
23197	acetic acid,sodium salt trihydrate	0
23200	azide	0
23200	 azium	1
23200	 hydrazoic acid,sodium salt 	2
23206	cacodylic acid,sodium salt hydrate	0
23209	sodium cacodylate	0
23209	 cacodylic acid,sodium salt trihydrate	1
23209	 dimethylarsinic acid,sodium salt	2
23209	 dimethylarsonic acid,sodium salt	3
23212	soda ash	0
23215	NaCl	0
23218	sodium citrate dihydrate	0
23218	 sodium citrate tribasic dihydrate	1
23218	 sodium citrate tribasic dihydrate	2
23218	 citric acid,trisodium salt dihydrate	3
23218	 trisodium citrate dihydrate	4
23218	 tri-sodium citrate dihydrate	5
23221	sodium dihydrogen citrate	0
23221	 citric acid,monosodium salt	1
23224	3a,12a-dihydroxy-5b-cholanic acid,sodium salt	0
23224	 7-deoxycholic acid,sodium salt	1
23224	 desoxycholic acid,sodium salt	2
23227	3a,12a-dihydroxy-5b-cholanic acid,sodium salt	0
23227	 7-deoxycholic acid,sodium salt	1
23227	 desoxycholic acid,sodium salt 	2
23230	dodecyl sodium sulfate	0
23230	 dodecyl sulfate sodium salt	1
23230	 lauryl sulfate sodium salt	2
23230	 SDS	3
23230	 sodium lauryl sulfate	4
23236	 formic acid,sodium salt 	0
23239	sodium hexadecyl sulphate	0
23239	 sodium hexadecyl sulfate	1
23239	 cetyl sodium sulfate	2
23239	 sodium monohexadecyl sulfate	3
23239	 sodium palmityl sulfate	4
23239	 sodium n-hexadecyl sulfate	5
23239	 Tergitol anionic 7	6
23239	 1-hexadecanol,hydrogen sulfate,sodium salt	7
23242	sodium bicarbonate	0
23242	 natrii hydrogenocarbonas	1
23245	sodium di-hydrogen orthophosphate dihydrate (monobasic)	0
23245	 natrii dihydrogenophosphas dihydricus	1
23245	 sodium dihydrogen phosphate dihydrate	2
23248	di sodium hydrogen orthophosphate diahydrate (dibasic)	0
23248	 sec-sodium phosphate	1
23248	 di-sodium hydrogen phosphate dihydrate	2
23248	 disodium hydrogen phosphate dihydrate	3
23248	 disodium phosphate	4
23251	sec-Sodium phosphate	0
23251	 Disodium hydrogen phosphate	1
23251	 Disodium phosphate	2
23251	 Sodium hydrogenphosphate	3
23254	caustic soda	0
23260	malonic acid,disodium salt	0
23263	sodium malonate dibasic monohydrate	0
23263	 malonic acid,disodium salt monohydrate	1
23263	 propanedioic acid	2
23266	sodium periodate	0
23269	Chile salpeter	0
23278	trisodium phosphate	0
23281	propionic acid,sodium salt	0
23284	sodium succinate dibasic	0
23284	 succinic acid,disodium salt	1
23284	 disodium succinate	2
23287	sodium sulfate	0
23290	Glauber's salt	0
23290	 sodium sulfate decahydrate	1
23293	L-(+)-tartaric acid disodium salt	0
23293	 disodium tartrate dihydrate	1
23293	 sodium tartrate dihydrate	2
23296	borax	0
23299	sodium isothiocyanate	0
23299	 sodium rhodanate	1
23299	 sodium rhodanide	2
23299	 sodium sulfocyanate	3
23302	sodium thiosulfate pentahydrate	0
23302	 hypo	1
23302	 sodium hyposulfite	2
23305	TCA sodium salt	0
23305	 trichloroacetic acid	1
23305	 TCA	2
23308	 1-stearoyl-2-oleoyl-sn-glycero-3-phosphocholine	0
23308	 18:0-18:1,[cis-9] PC 	1
23311	D-sorbitol	0
23311	 D-Glucitol	1
23314	1,8-diamino-4-azaoctane	0
23314	 N-(3-aminopropyl)-1,4-diaminobutane	1
23317	N-(3-aminopropyl)-1,4-butanediamine trihydrochloride	0
23317	 spermidine tetra-HCl	1
23320	N,N'-bis(3-aminopropyl)-1,4-butanediamine tetrahydrochloride	0
23323	streptomycin sulfate	0
23326	strontium chloride	0
23329	butanedioic acid	0
23332	dihydro-2,5-furandione	0
23332	 succinyloxide	1
23332	 butanedioic anhydride	2
23332	 2,5-diketotetrahydrofuran	3
23332	 succinyl anhydride	4
23332	 tetrahydro-2,5-furandione	5
23335	2,5 pyrolidinedione	0
23335	 butanimide	1
23338	beta-D-fructofuranosyl-alpha-D-glucopyranoside sugar	0
23338	 D(+)-saccharose	1
23338	 alpha-D-glucopyranosyl-beta-D-fructofuranoside	2
23341	bis(sulfo-N-succinimidyl) ethylene glycol disuccinate	0
23341	 ethylene glycol bis(sulfosuccinimidyl succinate)	1
23344	sulfuric acid	0
23347	N-tris[hydroxymethyl]methyl-3-aminobutanesulfonic acid	0
23353	N-tris[hydroxymethyl]methyl-3-aminopropanesulfonic acid	0
23356	3-[N-tris(hydroxymethyl)methylamino]-2-hydroxypropanesulfonic acid	0
23356	 2-hydroxy-3-[tris(hydroxymethyl)methylamino]-1-propanesulfonic acid	1
23356	 N-[tris(hydroxymethyl)methyl]-3-amino-2-hydroxypropanesulfonic acid	2
23359	2-aminoethanesulfonic acid	0
23362	 N,N,N',N',-tetramethylethylenediamine,1,2-Bis(dimethylamino)ethane	0
23362	 TEMEDA	1
23365	N-tris[hydroxymethyl]methyl-2-aminoethanesulfonic acid	0
23365	 2-[(2-hydroxy-1,1-bis(hydroxymethyl)ethyl)amino]ethanesulfonic acid	1
23365	 N-[tris(hydroxymethyl)methyl]-2-aminoethanesulfonic acid	2
23365	 TES Free Acid	3
23371	C8E4	0
23371	 octyl tetraethylene glycol ether	1
23371	 octyltetraglycol	2
23374	myristyl sulfobetaine	0
23374	 3-(N,N-dimethylmyristylammonio)propanesulfonate	1
23374	 3-(N,N-dimethylmyristylammonio)propane-sulfonate	2
23374	 3-(N,N-dimethyltetradecylammonio)propanesulfonate	3
23374	 3-(myristyldimethylammonio)propanesulfonate	4
23374	 N-tetradecyl-N,N-dimethyl-3-ammonio-1-propanesulfonate	5
23374	 SB3-14	6
23377	tetrahydrofuran	0
23377	 THF	1
23380	cyclohexene-1,2-dicarboxylic anhydride	0
23383	tetrakis(acetoxymercuri)methane	0
23383	 acetic acid: trimercuriomethylmercury	1
23383	 TAMM	2
23383	 mercury,tetrakis(acetato)-mu4-methanetetrayltetra- (8CI)	3
23383	 mercury,tetrakis(acetato-O)-mu4-methanetetrayltetra-	4
23386	tetramethylammonium chloride	0
23386	 TMA	1
23389	ethylmercurithiosalicylic acid,sodium salt	0
23389	 2-(ethylmercuriomercapto)benzoic acid,sodium salt	1
23392	2-isopropyl-5-methylphenol	0
23392	 5-methyl-2-(1-methylethyl)phenol	1
23392	 5-methyl-2-isopropylphenol	2
23392	 thyme camphor	3
23392	 thymic acid	4
23395	N-tosyl-L-lysine chloromethyl ketone	0
23398	N-tosyl-L-phenylalanine chloromethyl ketone	0
23398	 L-1-tosylamide-2-phenyl-ethyl chloromethyl ketone	1
23401	tetrakis (2-pyridylmethyl)ethylene diamine	0
23404	mycose	0
23404	 alpha-D-trehalose	1
23404	 D-(+)-trehalose	2
23407	TCA	0
23410	N-[tris(hydroxymethyl)methyl]glycine	0
23413	tris(2-hydroxyethyl)amine	0
23413	 tri(hydroxyethyl)amine	1
23413	 Trolamine	2
23413	 TEA (amino alcohol)	3
23413	 nitrilo-2,2',2''-triethanol	4
23413	 triethylolamine	5
23413	 Daltogen	6
23416	triglycol	0
23419	trifluoroethyl alcohol	0
23419	TFE	1
23422	trimethylammonium chloride	0
23428	trimethylammonium chloride	0
23428	 trimethylammonium hydrochloride	1
23431	acetoxytrimethyllead(IV)	0
23434	TTC	0
23434	 TPTZ	1
23434	 2,3,5-triphenyl-2H-tetrazolium chloride	2
23434	 tetrazolium red	3
23437	tris(hydroxymethyl)aminomethane	0
23437	 2-amino-2-(hydroxymethyl)-1,3-propanediol	1
23437	 THAM	2
23437	 tris base	3
23437	 trometamol 	4
23440	TRIS hydrochloride	0
23440	 tris(hydroxymethyl)aminomethane hydrochloride	1
23443	ruthenium-tris(2,2'-bipyridyl) dichloride	0
23443	 tris(2,2'-bipyridyl)ruthenium(II) chloride hexahydrate	1
23446	4-(1,1,3,3-tetramethylbutyl)phenyl-polyethylene glycol	0
23446	 polyethylene glycol tert-octylphenyl ether	1
23449	(1,1,3,3-Tetramethylbutyl)phenyl-polyethylene glycol	0
23452	Bowman-Birk Inhibitor	0
23458	polyethylene glycol sorbitan monolaurate	0
23461	carbamide	0
23461	 carbonyldiamide	1
23464	vancomycin hydrochloride from Streptomyces orientalis	0
23467	alpha-(5,6-dimethylbenzimidazolyl)cyanocobamide	0
23467	 CN-Cbl	1
23467	 cyanocob(III)alamin	2
23467	 cyanocobalamin	3
23470	5 bromo-4-chloro-3-indolyl-b-D-galactopyranoside	0
23473	xylene (cyanole)	0
23473	 acid blue 147	1
23473	 cyanol FF	2
23473	 XC	3
23479	xylite	0
23482	ytterbium trichloride hexahydrate	0
23485	yttrium trichloride	0
23488	yttrium trichloride	0
23500	sulfuric acid,zinc salt (1:1) heptahydrate	0
23500	 zinc vitriol,heptahydrate	1
23500	 zinc sulfate,heptahydrate	2
23500	 zinc sulphate,heptahydrate	3
23503	 zinc sulfate	0
23503	 sulfuric acid,zinc salt	1
23503	 sulphuric acid,zinc salt	2
23503	 zinc sulfate anhydrous	3
\.


--
-- Data for Name: mole_abstractcomponent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_abstractcomponent (name, labbookentryid, naturalsourceid) FROM stdin;
ACES	22001	\N
acetic acid	22004	\N
acetone	22007	\N
acetonitrile	22010	\N
N-(2-acetamido)iminodiacetic acid	22013	\N
acridine orange : hemi (zinc chloride)	22016	\N
acrylamide	22019	\N
N,N'-(1,2-dihydroxyethylene)bis-acrylamide	22022	\N
adenosine	22025	\N
adenosine 5'-triphosphate disodium salt	22028	\N
adonitol	22031	\N
L-alanine	22034	\N
albumin bovine	22037	\N
aluminium chloride	22040	\N
4-(2-amino ethyl) benzenesulfonyl fluoride HCl	22043	\N
6-aminocaproic acid	22046	\N
2-amino-2-methyl-1,3-propanediol	22049	\N
4-amino-m-cresol	22052	\N
ammonium acetate	22055	\N
ammonium bicarbonate	22058	\N
ammonium bromide	22061	\N
ammonium chloride	22064	\N
ammonium citrate dibasic	22067	\N
ammonium citrate tribasic	22070	\N
ammonium fluoride	22073	\N
ammonium formate	22076	\N
ammonium iodide	22079	\N
ammonium iron(II) sulphate hexahydrate	22082	\N
ammonium nitrate	22085	\N
ammonium oxalate	22088	\N
ammonium persulphate	22091	\N
diammonium hydrogen phosphate	22094	\N
ammonium dihydrogen phosphate	22097	\N
ammonium sulphate	22100	\N
ammonium tartrate dibasic	22103	\N
ammonium trifluoroacetate	22106	\N
ammonium thiocyanate	22109	\N
ampicillin (sodium)	22112	\N
AMPSO	22115	\N
aniline	22118	\N
8-anilino-1-naphthalene sulphonic acid (ammonium salt)	22121	\N
L-arginine	22124	\N
L-ascorbic acid	22127	\N
L-asparagine	22130	\N
L-aspartic acid	22133	\N
azelaic acid	22136	\N
barbital	22139	\N
barium acetate	22142	\N
barium chloride	22145	\N
barium nitrate	22148	\N
barium thiocyanate trihydrate	22151	\N
benzamidine hydrochloride hydrate	22154	\N
benzamidine hydrochloride	22157	\N
benzenesulphonic acid	22160	\N
benzoic acid	22163	\N
BES	22166	\N
beryllium sulphate tetrahydrate	22169	\N
betaine	22172	\N
betaine monohydrate	22173	\N
bestatin hydrochloride	22176	\N
bicine	22179	\N
BigCHAP	22182	\N
D-biotin	22185	\N
biotin-maleimide	22188	\N
bis(hexamethylene)triamine	22191	\N
BIS-TRIS	22194	\N
BIS-TRIS hydrochloride	22197	\N
BIS-TRIS propane	22200	\N
boric acid	22203	\N
Brilliant Blue G	22206	\N
Brilliant Blue R	22209	\N
bromophenol blue,sodium salt	22212	\N
2-bromo-4'-nitroacetophenone	22215	\N
polyoxyethylene(23) lauryl ether	22218	\N
1,2-butanediol	22221	\N
1,3-butanediol	22224	\N
1,4-butanediol	22227	\N
2,3-butanediol	22230	\N
1,4-butanediol diglycidyl ether	22233	\N
n-butanol	22236	\N
tert-butanol	22239	\N
tert-butyl methylether	22242	\N
butyl formate	22245	\N
gamma-butyrolactone	22248	\N
C12E8	22251	\N
CABS	22254	\N
cadmium acetate	22257	\N
cadmium bromide	22260	\N
cadmium chloride	22263	\N
cadmium chloride dihydrate	22266	\N
cadmium iodide	22269	\N
cadmium sulphate	22272	\N
cadmium sulphate hydrate	22275	\N
calcium acetate	22278	\N
calcium carbonate	22281	\N
calcium chloride	22284	\N
calcium chloride dihydrate	22287	\N
calcium chloride hexahydrate	22290	\N
calcium sulphate	22293	\N
e-caprolactam	22296	\N
CAPS	22299	\N
CAPSO	22302	\N
carbenicillin disodium salt	22305	\N
cesium bromide	22308	\N
cesium chloride	22311	\N
cesium iodide	22314	\N
cesium nitrate	22317	\N
cesium sulphate	22320	\N
cetyltrimethylammonium chloride	22323	\N
CHAPS	22326	\N
CHES	22329	\N
chloramine T hydrate	22332	\N
chloramphenicol	22335	\N
chloroform	22338	\N
4 chloro-1-naphthol	22341	\N
7-chloro-4-nitro benz-2-oxa-1,3-diazole	22344	\N
cholesterol	22347	\N
cholic acid	22350	\N
citric acid	22353	\N
cobalt(II) chloride	22356	\N
cobalt(II) chloride hexahydrate	22359	\N
copper bromide	22362	\N
copper chloride	22365	\N
copper(II) chloride	22368	\N
copper(II) chloride dihydrate	22371	\N
copper(II) sulphate pentahydrate	22374	\N
creatine	22377	\N
L-cysteine	22380	\N
cytidine 5'-triphosphate disodium salt	22383	\N
cyanogen bromide	22386	\N
2-cyclohexen-1-one	22389	\N
N-decanoyl-N-methylglucamine	22392	\N
3-(decyldimethylammonio)propane-1-sulfonate	22395	\N
deoxycholic acid (sodium salt)	22398	\N
2'-deoxyadenosine-5'-triphosphate,disodium salt,trihydrate	22401	\N
2'-deoxycytidine 5'-triphosphate disodium salt	22404	\N
2'-deoxyguanosine 5'-triphosphate trisodium salt	22407	\N
2'-deoxythymidine 5'-triphosphate sodium salt	22410	\N
dextran sulphate,sodium salt	22413	\N
1,6-diaminohexane	22416	\N
1,8-diaminooctane	22419	\N
1,5-diaminopentane	22422	\N
di-u-iodobis(ethylenediamine)-di-platinum (II) nitrate	22425	\N
dichloro(ethylenediamine)platinum (II)	22428	\N
dichloromethane	22431	\N
N,N'-dicyclohexylcarbodiimide	22434	\N
DDM	22437	\N
DHPC	22440	\N
N,N-dimethyldecylamine-N-oxide	22443	\N
N,N-dimethyldodecylamine-N-oxide	22446	\N
3-(N,N-dimethyloctylammonio)propane-sulfonate	22449	\N
3-(N,N-dimethylpalmitylammonio)propane-sulfonate	22452	\N
diethyl-dithio-carbamic acid (sodium salt)	22455	\N
diethyl ether	22458	\N
diethyl pyrocarbonate	22461	\N
N,N-dimethylformamide	22464	\N
dimethyl suberimidate dihydrchloride	22467	\N
dioxane	22470	\N
diphenylthiocarbazone	22473	\N
dipicolinic acid	22476	\N
DIPSO	22479	\N
5 5' dithio-bis (2-nitrobenzoic acid)	22482	\N
dithiothreitol	22485	\N
DLPC	22488	\N
DM	22491	\N
DMPC	22494	\N
DOPG	22497	\N
dimethylsulphoxide	22500	\N
dodecyl-b-D-glucopyranoside	22503	\N
3-(dodecyldimethylammonio)propanesulfonate	22506	\N
DOPC	22509	\N
ectoine	22512	\N
erythritol	22515	\N
ethanol	22518	\N
ethanolamine	22519	\N
ethidium bromide	22522	\N
EDTA	22525	\N
EDTA disodium salt	22528	\N
2-ethoxyethanol	22531	\N
ethyl acetate	22534	\N
ethylenediamine	22537	\N
ethylene glycol	22540	\N
EGTA	22543	\N
EPPS	22546	\N
N ethylmaleimide	22549	\N
ethyl mercuryphosphate	22552	\N
ethyl methylketone	22555	\N
2-ethyl-5-phenylisoxazolium sulfonate	22558	\N
D-(-)-fructose	22561	\N
L-(+)-fructose	22564	\N
formaldehyde solution	22567	\N
formamide	22570	\N
formic acid	22573	\N
fusaric acid	22576	\N
D-(+)-glucose	22579	\N
L-(-)-glucose	22582	\N
glutathione (oxidised form)	22585	\N
glutathione (reduced form)	22588	\N
L-glutamic acid	22591	\N
D-glutamine	22594	\N
glutaraldehyde	22597	\N
glycerol	22600	\N
glycine	22603	\N
glycine betaine	22606	\N
glycyl-glycyl-glycine	22609	\N
guanidinium chloride	22612	\N
guanosine 5'-diphosphate (GDP) - sodium salt	22615	\N
guanosine 5'-triphosphate di sodium salt	22618	\N
HEPBS	22621	\N
HEPES	22624	\N
HEPES sodium salt	22627	\N
HEPPSO	22630	\N
1,2,3-heptanetriol	22633	\N
n-heptyl-b-D-glucopyranoside	22636	\N
hexaaminecobalt trichloride	22639	\N
hexadecyltrimethylammonium bromide	22642	\N
hexafluoro isopropanol	22645	\N
1,6-hexanediol	22648	\N
2,5-hexanediol	22651	\N
1,2,3-hexanetriol	22654	\N
hexyl-b-D-glucopyranoside	22657	\N
hydrochloric acid	22660	\N
3' hydroxy-2-butanone	22663	\N
hydroxylamine hydrochloride	22666	\N
2-hydroxy-5-nitrobenzaldehyde	22669	\N
alpha-hydroxyisobutyric acid	22672	\N
imidazole	22675	\N
iodoacetamide	22678	\N
iodoacetic acid	22681	\N
N-iodo acetyl-N-(5-sulfo-1-naphthyl) ethyldiamine	22684	\N
iron(III) chloride	22687	\N
iron(III) chloride hexahydrate	22690	\N
iron(II) chloride tetrahydrate	22693	\N
iron(II) sulphate	22696	\N
Isopropyl-b-D-thiogalactopyranoside	22699	\N
iso-propanol	22702	\N
O-(2-aminopropyl)-O'-(2-methoxyethyl)polypropylene glycol	22705	\N
alpha,omega-diamine poly(oxyethylene-co-oxypropylene)	22708	\N
kanamycin monosulphate	22711	\N
lactose	22714	\N
N-lauroylsarcosine  sodium salt	22717	\N
lead(II) acetate trihydrate	22720	\N
lead(II) acetate	22723	\N
leupeptin trifluoroacetate salt	22726	\N
lithium acetate	22729	\N
lithium acetate dihydrate	22732	\N
lithium bromide	22735	\N
lithium chloride	22738	\N
lithium chloride (hydrate)	22741	\N
lithium citrate (anhydrous)	22744	\N
lithium fluoride	22747	\N
lithium formate	22750	\N
tri-lithium citrate tetrahydrate	22753	\N
lithium nitrate	22756	\N
lithium perchlorate	22759	\N
lithium salicylate	22762	\N
lithium sulphate	22765	\N
lithium sulphate monohydrate	22768	\N
magnesium acetate tetrahydrate	22771	\N
magnesium bromide	22774	\N
magnesium chloride anhydrous	22777	\N
magnesium chloride  hexahydrate	22780	\N
magnesium formate	22783	\N
magnesium formate dihydrate	22786	\N
magnesium nitrate	22789	\N
magnesium nitrate hexahydrate	22792	\N
magnesium sulphate	22795	\N
magnesium sulphate hydrate	22798	\N
magnesium sulphate monohydrate	22801	\N
magnesium sulphate hexahydrate	22804	\N
magnesium sulphate heptahydrate	22807	\N
manganese(II) chloride	22810	\N
manganese(II) chloride dihydrate	22813	\N
manganese(II) chloride monohydrate	22816	\N
manganese(II) chloride tetrahydrate	22819	\N
malic acid	22822	\N
malonic acid	22825	\N
D-mannitol	22828	\N
L-mannitol	22831	\N
D-(+)-mannose	22834	\N
L-(-)- mannose	22837	\N
4-(N-maleimido) benzophenone	22840	\N
2-mercaptoethanol	22843	\N
mercury(II) acetate	22846	\N
mercury(II) chloride	22849	\N
mersalyl acid	22852	\N
MES	22855	\N
MES sodium salt	22858	\N
methanol	22861	\N
methyl acetate	22864	\N
methylmercury chloride	22867	\N
2-methyl-2,4-pentanediol	22870	\N
MOBS	22873	\N
MOPS	22876	\N
MOPSO	22879	\N
myo-inositol	22882	\N
NADP sodium salt	22885	\N
NADPH tetrasodium salt	22888	\N
N-hydroxysuccinimide	22891	\N
nickel(II) chloride	22894	\N
nickel(II) chloride hydrate	22897	\N
nickel(II) chloride hexahydrate	22900	\N
nickel(II) sulphate hexahydrate	22903	\N
nitric acid	22906	\N
N-nonanoyl-N-methylglucamine	22909	\N
nonaethylene glycol monododecyl ether	22912	\N
octaethylene glycol monodecyl ether	22915	\N
octyl b-D-glucopyranoside	22918	\N
n-octyl b-D-thioglucopyranoside	22921	\N
beta-octylglucoside	22924	\N
N-octanoyl-N-methylglucamine	22927	\N
nonyl-b-D-glucopyranoside	22930	\N
orthophosphoric acid	22933	\N
paratone-N	22936	\N
PCMB	22939	\N
PCMBS	22942	\N
4-(2-pyridylazo)resorcinol mono sodium salt monohydrate	22945	\N
pentaerythritol ethoxylate (3/4 EO/OH)	22948	\N
pentaerythritol propoxylate	22951	\N
pentaethylene glycol monodecyl ether	22954	\N
pentaethylene glycol monooctyl ether	22957	\N
3-pentanone	22960	\N
pepsinostreptin	22963	\N
1,10 phenanthroline	22966	\N
phenol	22969	\N
phenol red	22972	\N
phenylglyoxal hydrate	22975	\N
phenylmercury acetate	22978	\N
L-alpha-phosphatidylcholine	22981	\N
PIPES	22984	\N
PMSF	22987	\N
poly(acrylic acid sodium salt) 5100	22990	\N
polyethylene glycol 400	22993	\N
polyethylene glycol 200	22996	\N
polyethylene glycol 300	22999	\N
polyethylene glycol 600	23002	\N
polyethylene glycol 1000	23005	\N
polyethylene glycol 1500	23008	\N
polyethylene glycol 2000	23011	\N
polyethylene glycol 3000	23014	\N
polyethylene glycol 3350	23017	\N
polyethylene glycol 3350 monodisperse	23020	\N
polyethylene glycol 4000	23023	\N
polyethylene glycol 5000	23026	\N
polyethylene glycol 6000	23029	\N
polyethylene glycol 8000	23032	\N
polyethylene glycol 10000	23035	\N
polyethylene glycol 20000	23038	\N
polyethylene glycol monomethyl ether 550	23041	\N
polyethylene glycol monomethyl ether 2000	23044	\N
polyethylene glycol monomethyl ether 5000	23047	\N
polyethyleneimine	23050	\N
polypropylene glycol P400	23053	\N
polymixin B sulphate	23056	\N
polyoxyadenylic acid,potassium salt	23059	\N
poly(vinyl alcohol)	23062	\N
polyvinylpyrrolidone K15	23065	\N
POPC	23068	\N
POPSO	23071	\N
potassium acetate	23074	\N
potassium bromide	23077	\N
potassium chloride	23080	\N
potassium citrate	23083	\N
tripotassium citrate	23086	\N
potassium cyanate	23089	\N
potassium dihydrogen phosphate	23092	\N
dipotassium hydrogen phosphate	23095	\N
potassium phosphate dibasic trihydrate	23098	\N
potassium fluoride	23101	\N
potassium formate	23104	\N
potassium hexachloroiridate(IV)	23107	\N
potassium hexachloroplatinate(IV)	23110	\N
potassium hydroxide	23113	\N
potassium permanganate	23116	\N
potassium iodide	23119	\N
potassium nitrate	23122	\N
potassium sodium tartrate tetrahydrate	23125	\N
potassium sodium tartrate	23128	\N
potassium sulphate	23131	\N
dipotassium tartrate	23134	\N
potassium tetrachloroaurate(III)	23137	\N
potassium tetrachloroplatinate(II)	23140	\N
potassium tetracyanoplatinate(II)	23143	\N
dipotassium tetraraiodomercurate(II)	23146	\N
potassium tetranitroplatinate(II)	23149	\N
potassium thiocyanate	23152	\N
L-proline	23155	\N
2-propanol	23158	\N
1,2-propanediol	23161	\N
1,3-propanediol	23164	\N
propionitrile	23167	\N
pyridine	23170	\N
protamine sulphate	23173	\N
pyridoxal hydrochloride	23176	\N
D-(+)-raffinose	23179	\N
riboflavin	23182	\N
rubidium bromide	23185	\N
rubidium chloride	23188	\N
silver nitrate	23191	\N
sodium acetate	23194	\N
sodium acetate trihydrate	23197	\N
sodium azide	23200	\N
sodium bromide	23203	\N
sodium cacodylate hydrate	23206	\N
sodium cacodylate trihydrate	23209	\N
sodium carbonate (anhydrous)	23212	\N
sodium chloride	23215	\N
sodium citrate	23218	\N
sodium citrate monobasic	23221	\N
sodium deoxycholate	23224	\N
sodium deoxycholate monohydrate	23227	\N
sodium dodecyl sulphate	23230	\N
sodium fluoride	23233	\N
sodium formate	23236	\N
sodium n-hexadecyl sulphate	23239	\N
sodium hydrogen carbonate	23242	\N
sodium dihydrogen phosphate	23245	\N
disodium hydrogen phosphate	23248	\N
sodium phosphate dibasic	23251	\N
sodium hydroxide	23254	\N
sodium iodide	23257	\N
sodium malonate dibasic	23260	\N
sodium malonate	23263	\N
sodium meta periodate	23266	\N
sodium nitrate	23269	\N
sodium nitrite	23272	\N
sodium perchlorate	23275	\N
sodium phosphate	23278	\N
sodium propionate	23281	\N
sodium succinate	23284	\N
sodium sulphate	23287	\N
sodium sulphate decahydrate	23290	\N
disodium tartrate	23293	\N
sodium tetraborate	23296	\N
sodium thiocyanate	23299	\N
sodium thiosulphate pentahydrate	23302	\N
sodium trichloroacetate	23305	\N
SOPC	23308	\N
sorbitol	23311	\N
spermidine	23314	\N
spermidine trihydrochloride	23317	\N
spermine tetrahydrochloride	23320	\N
streptomycin sulphate	23323	\N
strontium dichloride	23326	\N
succinic acid	23329	\N
succinic anhydride	23332	\N
succinimide	23335	\N
sucrose	23338	\N
Sulfo-EGS	23341	\N
sulphuric acid	23344	\N
TABS	23347	\N
tacsimate	23350	\N
TAPS	23353	\N
TAPSO	23356	\N
taurine	23359	\N
TEMED	23362	\N
TES	23365	\N
tetracycline hydrochloride	23368	\N
tetraethylene glycol monooctyl ether	23371	\N
tetradecyl-N-N-dimethyl-3-ammonio-1-propanesulfonate	23374	\N
tetrahydrofurane	23377	\N
3,4,5,6-tetrahydrophthalic-anhydride	23380	\N
tetrakis (acetoxymercury) methane	23383	\N
tetramethyl ammonium chloride	23386	\N
thimerosal	23389	\N
thymol	23392	\N
TLCK	23395	\N
TPCK	23398	\N
TPEN	23401	\N
trehalose	23404	\N
trichloroacetic acid	23407	\N
tricine	23410	\N
triethanolamine	23413	\N
triethylene glycol	23416	\N
2,2,2-trifluoroethanol	23419	\N
trimethylamine hydrochloride	23422	\N
trimethylamine N-oxide	23425	\N
trimethyl ammonium chloride	23428	\N
trimethyllead acetate	23431	\N
2,3,5 triphenyl tetrazolium chloride	23434	\N
TRIS	23437	\N
TRIS HCl	23440	\N
tris (2,2' bipyridyl)dichloro ruthenium(11) hexahydrate	23443	\N
t-octylphenoxypolyethoxyethanol	23446	\N
polyethylene glycol tert-octylphenyl ether	23449	\N
trypsin-chymotrypsin inhibitor (Soy bean)	23452	\N
trypsin inhibitor (lima bean)	23455	\N
polyoxyethylene sorbitan monolaurate	23458	\N
urea	23461	\N
vancomycin hydrochloride	23464	\N
vitamin B12	23467	\N
X-GAL	23470	\N
xylene cyanol FF	23473	\N
xylenol orange tetrasodium salt	23476	\N
xylitol	23479	\N
ytterbium(III) chloride hexahydrate	23482	\N
yttrium(III) chloride hexahydrate	23485	\N
yttrium(III) chloride	23488	\N
zinc acetate dihydrate	23491	\N
zinc acetate	23494	\N
zinc chloride	23497	\N
zinc sulphate heptahydrate	23500	\N
zinc sulphate	23503	\N
Tfor	30003	\N
Trev	30004	\N
pET21d	36003	\N
pDEST17	36016	\N
pET26b	36029	\N
pET28a	36042	\N
pOPING-Hygro	36057	\N
pDONR207	36065	\N
pDONR221	36076	\N
pCR[TM]-Blunt	36088	\N
pET15b	36101	\N
pOPINA	36113	\N
pOPINB	36119	\N
pOPINE	36125	\N
pOPINF	36131	\N
pOPING	36137	\N
pOPINH	36144	\N
pOPINI	36150	\N
pOPINJ	36156	\N
pOPINK	36162	\N
pOPINM	36168	\N
pOPINS	36174	\N
YEp352	36180	\N
pTriEx-2	36190	\N
\.


--
-- Data for Name: mole_compca2components; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_compca2components (componentid, categoryid) FROM stdin;
22001	12001
22004	12001
22004	12012
22007	12017
22010	12017
22013	12001
22013	12012
22016	12038
22019	12010
22022	12010
22025	12035
22028	12025
22028	12035
22031	12025
22034	12041
22037	12025
22040	12025
22043	12024
22046	12024
22046	12025
22049	12001
22052	12015
22055	12013
22055	12025
22055	12016
22058	12016
22061	12025
22061	12016
22064	12013
22064	12025
22064	12016
22067	12013
22067	12016
22070	12001
22073	12016
22076	12013
22076	12016
22079	12016
22082	12016
22085	12013
22085	12025
22085	12016
22088	12016
22091	12027
22094	12013
22094	12016
22097	12013
22097	12016
22100	12013
22100	12016
22103	12025
22106	12025
22109	12025
22112	12026
22115	12001
22118	12038
22121	12038
22124	12041
22127	12042
22130	12041
22133	12041
22136	12036
22136	12024
22139	12024
22142	12016
22145	12025
22148	12016
22151	12016
22154	12024
22154	12025
22157	12025
22160	12016
22163	12036
22166	12001
22169	12025
22172	12025
22173	12025
22176	12024
22179	12001
22182	12005
22185	12042
22188	12032
22191	12024
22194	12001
22197	12001
22200	12001
22203	12012
22206	12038
22209	12038
22212	12038
22215	12024
22218	12005
22221	12025
22221	12010
22224	12017
22224	12010
22227	12013
22227	12025
22230	12003
22233	12029
22236	12017
22236	12025
22239	12013
22239	12025
22242	12025
22245	12024
22248	12017
22248	12010
22251	12005
22254	12001
22257	12016
22260	12016
22263	12016
22266	12016
22269	12016
22272	12016
22275	12016
22278	12025
22278	12016
22281	12016
22284	12016
22287	12016
22290	12016
22293	12016
22296	12025
22299	12001
22302	12001
22305	12026
22308	12016
22311	12025
22311	12016
22314	12016
22317	12016
22320	12016
22323	12005
22326	12005
22329	12001
22332	12015
22335	12026
22338	12017
22341	12033
22344	12038
22347	12031
22350	12005
22353	12012
22356	12025
22356	12016
22359	12025
22359	12016
22362	12016
22365	12016
22368	12025
22368	12016
22371	12016
22374	12016
22377	12033
22380	12025
22380	12041
22383	12035
22386	12032
22389	12033
22389	12010
22392	12005
22395	12005
22398	12005
22401	12035
22404	12035
22407	12035
22410	12035
22413	12025
22416	12025
22416	12010
22419	12025
22419	12010
22422	12010
22425	12007
22428	12007
22431	12025
22434	12032
22437	12005
22440	12031
22443	12005
22446	12005
22449	12005
22452	12005
22455	12024
22458	12017
22461	12032
22464	12017
22464	12025
22467	12029
22470	12017
22470	12025
22473	12002
22476	12002
22479	12001
22482	12032
22485	12025
22485	12014
22485	12032
22488	12031
22491	12005
22494	12031
22497	12031
22500	12017
22500	12025
22503	12005
22506	12005
22509	12031
22512	12025
22515	12025
22515	12037
22518	12017
22518	12013
22518	12025
22519	12025
22522	12038
22525	12025
22525	12002
22528	12025
22528	12002
22531	12013
22534	12025
22537	12025
22540	12025
22540	12003
22543	12025
22543	12002
22546	12001
22549	12032
22552	12007
22555	12025
22558	12032
22561	12025
22561	12037
22564	12025
22564	12037
22567	12036
22570	12017
22573	12012
22576	12024
22579	12025
22579	12037
22582	12025
22582	12037
22585	12014
22588	12014
22591	12041
22594	12025
22594	12041
22597	12036
22597	12029
22600	12003
22603	12001
22606	12025
22609	12025
22612	12025
22612	12039
22615	12035
22618	12035
22621	12001
22624	12001
22627	12001
22630	12001
22633	12025
22636	12005
22639	12013
22642	12013
22642	12025
22642	12016
22645	12025
22648	12013
22648	12010
22651	12025
22651	12029
22654	12025
22654	12010
22657	12005
22660	12012
22663	12017
22666	12024
22669	12024
22672	12002
22675	12001
22678	12024
22678	12032
22681	12024
22681	12032
22684	12038
22687	12025
22687	12016
22690	12025
22690	12016
22693	12025
22693	12016
22696	12016
22699	12037
22699	12023
22702	12017
22702	12013
22705	12013
22708	12013
22711	12026
22714	12025
22714	12037
22717	12005
22720	12016
22720	12007
22723	12016
22723	12007
22726	12024
22729	12016
22732	12016
22735	12025
22738	12013
22738	12016
22741	12013
22741	12016
22744	12016
22747	12016
22750	12016
22753	12016
22756	12025
22756	12016
22759	12025
22762	12025
22765	12025
22765	12016
22768	12025
22768	12016
22771	12016
22774	12016
22777	12016
22780	12016
22783	12013
22783	12016
22786	12013
22786	12016
22789	12016
22792	12016
22795	12013
22795	12025
22795	12016
22798	12013
22798	12025
22798	12016
22801	12013
22801	12025
22801	12016
22804	12013
22804	12025
22804	12016
22807	12013
22807	12025
22807	12016
22810	12025
22810	12016
22813	12025
22813	12016
22816	12025
22816	12016
22819	12025
22819	12016
22822	12016
22822	12012
22825	12012
22828	12025
22828	12037
22831	12025
22831	12037
22834	12025
22834	12037
22837	12025
22837	12037
22840	12029
22843	12014
22846	12007
22849	12007
22852	12007
22855	12001
22858	12001
22861	12017
22861	12013
22861	12025
22864	12025
22867	12007
22870	12013
22873	12001
22876	12001
22879	12001
22882	12025
22882	12037
22885	12028
22888	12028
22891	12032
22894	12025
22894	12016
22897	12025
22897	12016
22900	12025
22900	12016
22903	12016
22906	12012
22909	12005
22912	12005
22915	12005
22918	12005
22921	12005
22924	12025
22924	12005
22927	12005
22930	12005
22933	12001
22933	12012
22936	12003
22939	12024
22939	12032
22939	12007
22942	12024
22942	12032
22942	12007
22945	12038
22948	12013
22951	12013
22954	12005
22957	12005
22960	12025
22960	12010
22963	12024
22966	12024
22966	12002
22969	12025
22969	12039
22972	12038
22975	12032
22978	12007
22981	12031
22984	12001
22987	12024
22990	12013
22993	12013
22996	12013
22999	12013
23002	12013
23005	12013
23008	12013
23011	12013
23014	12013
23017	12013
23020	12013
23023	12013
23026	12013
23029	12013
23032	12013
23035	12013
23038	12013
23041	12013
23044	12013
23047	12013
23050	12013
23050	12005
23050	12002
23053	12013
23053	12025
23056	12026
23059	12040
23062	12025
23065	12013
23065	12025
23068	12031
23071	12001
23074	12016
23077	12016
23080	12013
23080	12025
23080	12016
23083	12016
23086	12016
23089	12025
23092	12013
23092	12025
23092	12016
23095	12013
23095	12025
23095	12016
23098	12013
23098	12016
23101	12016
23104	12016
23107	12007
23110	12007
23113	12012
23116	12016
23119	12025
23119	12016
23122	12025
23122	12016
23125	12013
23125	12016
23128	12013
23128	12016
23131	12016
23134	12016
23137	12007
23140	12007
23143	12007
23146	12007
23149	12007
23152	12013
23152	12025
23152	12016
23155	12041
23158	12017
23158	12013
23161	12013
23161	12003
23164	12025
23164	12010
23167	12025
23170	12025
23173	12013
23176	12032
23179	12025
23179	12037
23182	12042
23185	12025
23185	12016
23188	12025
23188	12016
23191	12007
23194	12013
23194	12016
23197	12025
23197	12001
23197	12016
23200	12036
23200	12024
23203	12025
23203	12016
23206	12025
23206	12016
23209	12001
23212	12001
23215	12013
23215	12025
23215	12016
23218	12013
23218	12025
23218	12001
23218	12016
23221	12013
23221	12025
23221	12001
23221	12016
23224	12005
23227	12005
23230	12005
23233	12025
23233	12016
23236	12017
23236	12013
23236	12016
23239	12005
23242	12016
23245	12013
23245	12001
23245	12016
23248	12001
23248	12016
23251	12001
23251	12016
23254	12012
23257	12025
23257	12016
23260	12013
23260	12025
23260	12016
23263	12013
23263	12025
23263	12016
23266	12032
23269	12016
23272	12016
23275	12025
23275	12016
23278	12016
23281	12001
23284	12013
23284	12001
23287	12013
23290	12016
23293	12016
23296	12016
23299	12025
23302	12025
23305	12025
23305	12039
23308	12031
23311	12025
23311	12003
23311	12037
23314	12013
23314	12024
23314	12025
23317	12013
23317	12024
23320	12013
23320	12025
23323	12026
23326	12025
23326	12016
23329	12013
23329	12016
23329	12012
23332	12032
23335	12032
23338	12025
23338	12037
23341	12032
23344	12012
23347	12001
23350	12013
23350	12001
23350	12016
23350	12012
23353	12001
23356	12001
23359	12025
23359	12030
23362	12027
23365	12001
23368	12026
23371	12005
23374	12005
23377	12017
23377	12025
23380	12032
23383	12007
23386	12025
23389	12036
23389	12007
23392	12025
23395	12024
23398	12024
23401	12002
23404	12025
23404	12037
23407	12013
23407	12039
23410	12001
23413	12017
23416	12013
23419	12017
23422	12016
23425	12025
23425	12016
23425	12032
23428	12025
23431	12007
23434	12038
23437	12001
23440	12001
23443	12027
23446	12005
23449	12005
23452	12024
23455	12024
23458	12005
23461	12025
23461	12039
23464	12026
23467	12042
23470	12033
23473	12038
23476	12038
23479	12025
23479	12003
23482	12007
23485	12025
23488	12025
23491	12016
23494	12025
23494	12016
23497	12025
23500	12016
23503	12025
23503	12016
30003	12048
30004	12049
36003	12018
36016	12018
36029	12018
36042	12018
36057	12018
36065	12018
36076	12018
36088	12018
36101	12018
36113	12018
36119	12018
36125	12018
36131	12018
36137	12018
36144	12018
36150	12018
36156	12018
36162	12018
36168	12018
36174	12018
36180	12018
36190	12018
\.


--
-- Data for Name: mole_construct; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_construct (constructstatus, "function", markerdetails, promoterdetails, resistancedetails, sequencetype, moleculeid) FROM stdin;
empty	Prokaryotic Expression Vector	\N	\N	\N	DNA	36003
empty	E.coli Expression Vector	\N	\N	\N	DNA	36016
empty	Prokaryotic Expression Vector	\N	\N	\N	DNA	36029
empty	Prokaryotic Expression Vector	\N	\N	\N	DNA	36042
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36057
empty	E.coli Entry Vector -no longer available\n        	\N	\N	\N	DNA	36065
empty	E.coli Entry Vector	\N	\N	\N	DNA	36076
empty	PCR Cloning Vector for cloning of blunt-end PCR\n            product	\N	\N	\N	DNA	36088
empty	Prokaryotic Expression Vector	\N	\N	\N	DNA	36101
empty	In-Fusion[TM] E.coli Expression Vector\n        	\N	\N	\N	DNA	36113
empty	In-Fusion[TM] E.coli Expression Vector\n        	\N	\N	\N	DNA	36119
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36125
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36131
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36137
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36144
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36150
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36156
empty	In-Fusion[TM] E.coli Expression Vector\n        	\N	\N	\N	DNA	36162
empty	In-Fusion[TM] Multisystem Expression\n            Vector	\N	\N	\N	DNA	36168
empty	In-Fusion[TM] E.coli Expression Vector\n        	\N	\N	\N	DNA	36174
empty	Yeast YE-type (episomal) shuttle vector, cloning\n            vector	\N	\N	\N	DNA	36180
empty	Multisystem Expression Vector	\N	\N	\N	DNA	36190
\.


--
-- Data for Name: mole_extension; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_extension (extensiontype, isforuse, relatedproteintagseq, restrictionenzyme, moleculeid) FROM stdin;
forward	t	\N	\N	30003
reverse	t	\N	\N	30004
\.


--
-- Data for Name: mole_host; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_host (antibioticresistances, comments, genotype, harbouredplasmids, selectablemarkers, strain, use, abstractcomponentid) FROM stdin;
\.


--
-- Data for Name: mole_host2organisation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_host2organisation (hostid, organisationid) FROM stdin;
\.


--
-- Data for Name: mole_molecule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_molecule (casnum, empiricalformula, isvolatile, moltype, molecularmass, seqdetails, seqstring, abstractcomponentid) FROM stdin;
7365-82-4	H2NCOCH2NHCH2CH2SO3H	\N	other	182.19999999999999	\N	\N	22001
64-19-7	CH3COOH	\N	other	60.052979999999998	\N	\N	22004
67-64-1	CH3COCH3	\N	other	58.080669999999998	\N	\N	22007
75-05-8	CH3CN	\N	other	41.052909999999997	\N	\N	22010
26239-55-4	H2NCOCH2N(CH2CO2H)2	\N	other	190.15000000000001	\N	\N	22013
10127-02-3	C17H20N3Cl 0.5(ZnCl2)	\N	other	369.95999999999998	\N	\N	22016
79-06-1	C3H5NO	\N	other	71.079999999999998	\N	\N	22019
868-63-3	C8H12N2O4	\N	other	200.19	\N	\N	22022
58-61-7	C10H13N5O4	\N	other	267.24000000000001	\N	\N	22025
987-65-5	C10H14N5Na2O13P3.xH2O	\N	other	551.13999999999999	\N	\N	22028
488-81-3	C5H12O5	\N	other	152.15000000000001	\N	\N	22031
56-41-7	C3H7NO2	\N	other	89.090000000000003	\N	\N	22034
9048-46-8	\N	\N	other	66000	\N	\N	22037
7446-70-0	AlCl3	\N	other	133.34	\N	\N	22040
30827-99-7	C8H10NO2SF.HCl	\N	other	239.5	\N	\N	22043
60-32-2	NH2(CH2)5COOH	\N	other	131.16999999999999	\N	\N	22046
115-69-5	(HOCH2)2C(NH2)CH3	\N	other	105.14	\N	\N	22049
2835-99-6	H2NC6H3(CH3)OH	\N	other	123.15000000000001	\N	\N	22052
631-61-8	CH3COONH4	\N	other	77.079999999999998	\N	\N	22055
1066-33-7	NH4HCO3	\N	other	79.060000000000002	\N	\N	22058
12124-97-9	NH4Br	\N	other	97.939999999999998	\N	\N	22061
12125-02-9	NH4Cl	\N	other	53.490000000000002	\N	\N	22064
3012-65-5	HOC(CO2H)(CH2CO2NH4)2	\N	other	226.18000000000001	\N	\N	22067
3458-72-8	HOC(COONH4)(CH2COONH4)2	\N	other	243.22	\N	\N	22070
12125-01-8	HNH4F	\N	other	37.039999999999999	\N	\N	22073
540-69-2	CH5NO2	\N	other	63.060000000000002	\N	\N	22076
12027-06-4	NH4I	\N	other	144.94	\N	\N	22079
7783-85-9	(NH4)2Fe(SO4)2.6H2O	\N	other	392.13999999999999	\N	\N	22082
6484-52-2	NH4NO3	\N	other	80.040000000000006	\N	\N	22085
6009-70-7	(COONH4)2.H2O	\N	other	142.11000000000001	\N	\N	22088
7727-54-0	H8N2O8S2	\N	other	228.19999999999999	\N	\N	22091
7783-28-0	(NH4)2HPO4	\N	other	132.06	\N	\N	22094
7722-76-1	NH4H2PO4	\N	other	115.03	\N	\N	22097
7783-20-2	(NH4)2SO4	\N	other	132.13999999999999	\N	\N	22100
3164-29-2	(NH4)2C4H4O6	\N	other	184.15000000000001	\N	\N	22103
3336-58-1	CF3COONH4	\N	other	131.05000000000001	\N	\N	22106
1762-95-4	NH4SCN	\N	other	76.120000000000005	\N	\N	22109
69-52-3	C16H18N3NaO4S	\N	other	371.39999999999998	\N	\N	22112
68399-79-1	C7H17NO5S	\N	other	227.28	\N	\N	22115
62-53-3	C6H7N	\N	other	93.129999999999995	\N	\N	22118
28836-03-5	C16H16N2O3S	\N	other	316.37	\N	\N	22121
74-79-3	C6H14N4O2	\N	other	174.19999999999999	\N	\N	22124
58-81-7	C6H8O6	\N	other	176.12	\N	\N	22127
70-47-3	C4H8N2O3	\N	other	132.09999999999999	\N	\N	22130
56-84-8	C4H7NO4	\N	other	133.09999999999999	\N	\N	22133
123-99-9	HO2C(CH2)7CO2H	\N	other	128.22	\N	\N	22136
57-44-3	C8H12N2O3	\N	other	184.19	\N	\N	22139
543-80-6	(CH3COO)2Ba	\N	other	255.40000000000001	\N	\N	22142
106361-37-2	BaCl2	\N	other	208.22999999999999	\N	\N	22145
10022-31-8	Ba(NO3)2	\N	other	261.33999999999997	\N	\N	22148
304676-17-3	C2BaN2S2.xH2O	\N	other	253.49000000000001	\N	\N	22151
206752-36-5	C6H5C(=NH)NH2.HCl.xH2O	\N	other	156.61000000000001	\N	\N	22154
1669-14-0	C6H5C(=NH)NH2.HCl	\N	other	157.61000000000001	\N	\N	22157
98-11-3	C6H6O3S	\N	other	158.18000000000001	\N	\N	22160
65-85-0	C6H5COOH	\N	other	122.12	\N	\N	22163
10191-18-1	C6H15NO5S	\N	other	213.30000000000001	\N	\N	22166
7787-56-6	BeSO4.4H2O	\N	other	177.13999999999999	\N	\N	22169
107-43-7	(CH3)3N+CH2COO	\N	other	117.15000000000001	\N	\N	22172
17146-86-0	C5H11NO2.H2O	\N	other	135.16	\N	\N	22173
65391-42-6	C16H24N2O4.HCl	\N	other	344.82999999999998	\N	\N	22176
150-25-4	C6H13NO4	\N	other	163.16999999999999	\N	\N	22179
86303-22-2	C42H75N3O16	\N	other	878.10000000000002	\N	\N	22182
58-85-5	C10H16N2O3S	\N	other	244.31	\N	\N	22185
\N	C20H29N5O5S	\N	other	451.54000000000002	\N	\N	22188
143-23-7	NH2(CH2)6NH(CH2)6NH2	\N	other	215.38	\N	\N	22191
6976-37-0	C8H19NO5	\N	other	209.24000000000001	\N	\N	22194
124763-51-5	C8H19NO5.HCl	\N	other	245.69999999999999	\N	\N	22197
64431-96-5	CH2[CH2NHC(CH2OH)3]2	\N	other	282.32999999999998	\N	\N	22200
10043-35-3	BH3O3	\N	other	61.829999999999998	\N	\N	22203
6104-58-1	C47H48N3O7S2Na	\N	other	854	\N	\N	22206
6104-59-2	\N	\N	other	\N	\N	\N	22209
62625-28-9	C19H9Br4NaO5S	\N	other	691.94000000000005	\N	\N	22212
99-81-0	C8H6BrNO3	\N	other	244.03999999999999	\N	\N	22215
9002-92-0	\N	\N	other	1198	\N	\N	22218
584-03-2	CH3CH2CH(OH)CH2OH	\N	other	90.120000000000005	\N	\N	22221
107-88-0	CH3CH(OH)CH2CH2OH	\N	other	90.120000000000005	\N	\N	22224
110-63-4	HO(CH2)4OH	\N	other	90.120000000000005	\N	\N	22227
513-85-9	CH3CH(OH)CH(OH)CH3	\N	other	90.120000000000005	\N	\N	22230
2425-79-8	C10H18O4	\N	other	202.25	\N	\N	22233
71-36-3	CH3(CH2)3OH	\N	other	74.120000000000005	\N	\N	22236
75-65-0	(CH3)3COH	\N	other	74.120000000000005	\N	\N	22239
1634-04-4	(CH3)3COCH3	\N	other	88.150000000000006	\N	\N	22242
592-84-7	HCOO(CH2)3CH3	\N	other	102.13	\N	\N	22245
96-48-0	C4H6O2	\N	other	69.090000000000003	\N	\N	22248
3055-98-9	C28H58O9	\N	other	538.75	\N	\N	22251
161308-34-5	C10H21NO3S	\N	other	235.34	\N	\N	22254
89759-80-8	Cd(CH3COO)2	\N	other	230.49000000000001	\N	\N	22257
7789-42-6	CdBr2	\N	other	272.22000000000003	\N	\N	22260
10108-64-2	CdCl2	\N	other	183.31999999999999	\N	\N	22263
72589-96-9	CdCl2.2H2O	\N	other	219.34999999999999	\N	\N	22266
7790-80-9	CdI2	\N	other	366.22000000000003	\N	\N	22269
10124-36-4	CdSO4	\N	other	208.47	\N	\N	22272
7790-84-3	(CdSO4)3.8H2O	\N	other	769.53999999999996	\N	\N	22275
62-54-4	(CH3CO2)2Ca	\N	other	158.16999999999999	\N	\N	22278
471-34-1	CaCO3	\N	other	100.09	\N	\N	22281
10043-52-4	CaCl2	\N	other	110.98	\N	\N	22284
10035-04-8	CaCl2.2H2O	\N	other	147.00999999999999	\N	\N	22287
7774-34-7	CaCl2.6H2O	\N	other	219.08000000000001	\N	\N	22290
7778-18-9	CaSO4	\N	other	136.13999999999999	\N	\N	22293
105-60-2	C6H11NO	\N	other	113.16	\N	\N	22296
1135-40-6	C6H11NH(CH2)3SO3H	\N	other	221.31999999999999	\N	\N	22299
76463-39-5	C9H19NO4S	\N	other	237.31999999999999	\N	\N	22302
4800-94-6	C17H16N2Na2O6S	\N	other	422.36000000000001	\N	\N	22305
7787-69-1	CsBr	\N	other	212.81	\N	\N	22308
7647-17-8	CsCl	\N	other	168.36000000000001	\N	\N	22311
7789-17-5	CsI	\N	other	259.81	\N	\N	22314
7789-18-6	CsNO3	\N	other	194.91	\N	\N	22317
10294-54-9	Cs2SO4	\N	other	361.87	\N	\N	22320
112-02-7	C19H42ClN	\N	other	320	\N	\N	22323
75621-03-3	C32H58N2O7S	\N	other	614.89999999999998	\N	\N	22326
103-47-9	C8H17NO3S	\N	other	207.28999999999999	\N	\N	22329
127-65-1	C7H7ClNNaO2S.3H2O	\N	other	227.63999999999999	\N	\N	22332
56-75-7	C11H12Cl2N2O5	\N	other	323.13	\N	\N	22335
67-66-3	CHCl3	\N	other	119.38	\N	\N	22338
604-44-4	C10H7ClO	\N	other	178.61000000000001	\N	\N	22341
10199-89-0	C6H2ClN3O3	\N	other	199.55000000000001	\N	\N	22344
57-88-5	C27H46O	\N	other	386.64999999999998	\N	\N	22347
81-25-4	C24H40O5	\N	other	408.56999999999999	\N	\N	22350
77-92-9	HOC(COOH)(CH2COOH)2	\N	other	192.12	\N	\N	22353
7646-79-9	CbCl2	\N	other	129.84	\N	\N	22356
7791-13-1	CoCl26H2O	\N	other	273.93000000000001	\N	\N	22359
7787-70-4	CuBr	\N	other	143.44999999999999	\N	\N	22362
7758-89-6	CuCl	\N	other	99	\N	\N	22365
7447-39-4	CuCl2	\N	other	134.44999999999999	\N	\N	22368
10125-13-0	CuCl2. 2H2O	\N	other	170.47999999999999	\N	\N	22371
7758-99-8	CuO4S.5H2O	\N	other	249.69	\N	\N	22374
57-00-1	H2NC(=NH)N(CH3)CH2CO2H	\N	other	131.13	\N	\N	22377
52-90-4	C3H7NO2S	\N	other	121.16	\N	\N	22380
81012-87-5	C9H14N3Na2O14P3	\N	other	529.13999999999999	\N	\N	22383
506-68-3	CNBr	\N	other	105.92	\N	\N	22386
930-68-7	C6H8O	\N	other	96.129999999999995	\N	\N	22389
85261-20-7	C17H35NO6	\N	other	349.45999999999998	\N	\N	22392
15163-36-7	C15H33NO3S	\N	other	307.60000000000002	\N	\N	22395
302-95-4	C24H39NaO4	\N	other	414.55000000000001	\N	\N	22398
1927-31-7	C10H14N5Na2O12P3	\N	other	535.14999999999998	\N	\N	22401
102783-51-7	C9H14N3Na2O13P3	\N	other	511.12	\N	\N	22404
93919-41-6	C10H14N5Na3O13P3	\N	other	573.13	\N	\N	22407
18423-43-3	C10H16N2O14P3Na	\N	other	504.14999999999998	\N	\N	22410
9011-18-1	\N	\N	other	50000	\N	\N	22413
124-09-4	NH2(CH2)6NH2	\N	other	116.2	\N	\N	22416
373-44-4	NH2(CH2)8NH2	\N	other	144.25999999999999	\N	\N	22419
462-94-2	NH2(CH2)5NH2	\N	other	102.18000000000001	\N	\N	22422
109998-76-7	C4H16I2N6O6Pt2	\N	other	888.20000000000005	\N	\N	22425
14096-51-6	C2H6Cl2N2Pt	\N	other	324.06999999999999	\N	\N	22428
75-09-2	CH2Cl2	\N	other	84.930000000000007	\N	\N	22431
538-75-0	C13H22N2	\N	other	206.33000000000001	\N	\N	22434
69227-93-6	C24H46O11	\N	other	510.45999999999998	\N	\N	22437
39036-04-9	C22H44NO8P	\N	other	481.56	\N	\N	22440
2605-79-0	CH3(CH2)9N(O)(CH3)2	\N	other	201.34999999999999	\N	\N	22443
1643-20-5	CH3(CH2)11N(O)(CH3)2	\N	other	229.40000000000001	\N	\N	22446
15178-76-4	C13H29NO3S	\N	other	279.44	\N	\N	22449
2281-11-0	C21H45NO3S	\N	other	391.64999999999998	\N	\N	22452
20624-25-3	C5H10NNaS2.3H2O	\N	other	225.31	\N	\N	22455
60-29-7	C4H10O	\N	other	74.120000000000005	\N	\N	22458
1609-47-8	C6H10O5	\N	other	162.13999999999999	\N	\N	22461
68-12-2	HCON(CH3)2	\N	other	73.090000000000003	\N	\N	22464
34490-86-3	C10H20N2O2.2HCl	\N	other	273.19999999999999	\N	\N	22467
123-91-1	C4H8O2	\N	other	88.109999999999999	\N	\N	22470
60-10-6	C13H12N4S	\N	other	256.32999999999998	\N	\N	22473
499-83-2	C7H5NO4	\N	other	167.12	\N	\N	22476
68399-80-4	C7H17NO6S	\N	other	261.27999999999997	\N	\N	22479
69-78-3	C14H8N2O8S2	\N	other	396.35000000000002	\N	\N	22482
03/12/3483	C4H10O2S2	\N	other	154.25	\N	\N	22485
18194-25-7	C32H44NO8P	\N	other	657.86000000000001	\N	\N	22488
82494-09-5	C22H42O11	\N	other	482.56	\N	\N	22491
18194-24-6	C36H72NO8P	\N	other	677.92999999999995	\N	\N	22494
56421-10-4	C42H78NaO10P	\N	other	797.02999999999997	\N	\N	22497
67-68-5	(CH3)2SO	\N	other	78.129999999999995	\N	\N	22500
59122-55-3	C18H36O6	\N	other	348.47000000000003	\N	\N	22503
14933-08-5	C17H37NO3S	\N	other	335.55000000000001	\N	\N	22506
4235-95-4	C44H84NO8P	\N	other	786.11000000000001	\N	\N	22509
96702-03-3	C6H10N2O2	\N	other	142.16	\N	\N	22512
149-32-6	HOCH2[CH(OH)]2CH2OH	\N	other	122.12	\N	\N	22515
64-17-5	C2H5OH	\N	other	46.07	\N	\N	22518
141-43-5	C2H7NO	\N	other	61.079999999999998	\N	\N	22519
1239-45-8	C21H20BrN	\N	other	394.31	\N	\N	22522
60-00-4	C10H16N2O8	\N	other	292.24000000000001	\N	\N	22525
6381-92-6	C10H14N2O8Na2.2H2O	\N	other	372.24000000000001	\N	\N	22528
110-80-5	C2H5OCH2CH2OH	\N	other	90.120000000000005	\N	\N	22531
141-78-6	C4H8O2	\N	other	88.109999999999999	\N	\N	22534
107-15-3	NH2CH2CH2NH2	\N	other	60.100000000000001	\N	\N	22537
107-21-1	C2H6O2	\N	other	62.07	\N	\N	22540
67-42-5	[-CH2OCH2CH2N(CH2CO2H)2]2	\N	other	380.35000000000002	\N	\N	22543
16052-06-5	C9H20N2O4S	\N	other	252.33000000000001	\N	\N	22546
128-53-0	C6H7NO2	\N	other	125.13	\N	\N	22549
2235-25-8	C2H7ClHgO2	\N	other	326.5	\N	\N	22552
78-93-3	C4H8O	\N	other	72.109999999999999	\N	\N	22555
4156-16-5	C11H11NO4S	\N	other	253.27000000000001	\N	\N	22558
57-48-7	C6H12O6	\N	other	180.16	\N	\N	22561
7776-48-9	C6H12O6	\N	other	180.16	\N	\N	22564
50-00-0	CH2O	\N	other	30.030000000000001	\N	\N	22567
75-12-7	HCONH2	\N	other	45.039999999999999	\N	\N	22570
64-18-6	HCOOH	\N	other	46.030000000000001	\N	\N	22573
536-69-6	C10H13NO2	\N	other	179.22	\N	\N	22576
50-99-7	C6H12O6	\N	other	180.16	\N	\N	22579
921-60-8	C6H12O6	\N	other	180.16	\N	\N	22582
27025-41-8	C20H32N6O12S2	\N	other	612.63	\N	\N	22585
70-18-8	C10H17N3O6S	\N	other	307.31999999999999	\N	\N	22588
56-86-0	C5H9NO4	\N	other	147.13	\N	\N	22591
5959-95-5	C5H10N2O3	\N	other	146.13999999999999	\N	\N	22594
111-30-8	OCH(CH2)3CHO	\N	other	100.12	\N	\N	22597
56-81-5	HOCH2CH(OH)CH2OH	\N	other	92.090000000000003	\N	\N	22600
56-40-6	NH2CH2COOH	\N	other	75.069999999999993	\N	\N	22603
\N	(CD3)3N(Cl)CH2CO2H	\N	other	162.66	\N	\N	22606
556-33-2	C6H11N3O4	\N	other	189.16999999999999	\N	\N	22609
50-01-1	NH2C(=NH)NH2.HCl	\N	other	95.530000000000001	\N	\N	22612
43139-22-6	C10H15N5O11P2	\N	other	443.19999999999999	\N	\N	22615
56001-37-7	C10H14N5Na2O14P3.xH2O	\N	other	567.13999999999999	\N	\N	22618
161308-36-7	C10H22N2O4S	\N	other	266.36000000000001	\N	\N	22621
7365-45-9	C8H18N2O4S	\N	other	238.30000000000001	\N	\N	22624
75277-39-3	C8H17N2NaO4S	\N	other	260.29000000000002	\N	\N	22627
68399-78-0	C9H20N2O5S	\N	other	268.30000000000001	\N	\N	22630
103404-57-5	CH3(CH2)3CH(OH)CH(OH)CH2OH	\N	other	148.19999999999999	\N	\N	22633
78617-12-6	C13H26O6	\N	other	278.33999999999997	\N	\N	22636
10534-89-1	Co(NH3)6Cl3	\N	other	267.48000000000002	\N	\N	22639
57-09-0	CH3(CH2)15N(Br)(CH3)3	\N	other	364.44999999999999	\N	\N	22642
920-66-1	(CF3)2CHOH	\N	other	168.03999999999999	\N	\N	22645
629-11-8	HO(CH2)6OH	\N	other	118.17	\N	\N	22648
2935-44-6	CH3CH(OH)CH2CH2CH(OH)CH3	\N	other	118.17	\N	\N	22651
25323-24-4	CH3CH2CH2CH(OH)CH(OH)CH2OH	\N	other	134.16999999999999	\N	\N	22654
59080-45-4	C12H24O6	\N	other	264.31999999999999	\N	\N	22657
7647-01-0	HCl	\N	other	36.460000000000001	\N	\N	22660
513-86-0	C4H8O2	\N	other	88.109999999999999	\N	\N	22663
01/11/5470	H3NOHCl	\N	other	69.489999999999995	\N	\N	22666
97-51-8	C7H5NO4	\N	other	167.12	\N	\N	22669
594-61-6	C4H8O3	\N	other	104.09999999999999	\N	\N	22672
288-32-4	C3H4N2	\N	other	68.079999999999998	\N	\N	22675
144-48-9	C2H4INO	\N	other	184.964	\N	\N	22678
64-69-7	C2H3IO2	\N	other	185.94800000000001	\N	\N	22681
36930-63-9	C14H15IN2O4S	\N	other	434.25	\N	\N	22684
10025-77-1	FeCl3.6H2O	\N	other	270.30000000000001	\N	\N	22687
10025-77-1	FeCl3.6H2O	\N	other	270.30000000000001	\N	\N	22690
13478-10-9	Cl2Fe.4H2O	\N	other	198.81	\N	\N	22693
7720-78-7	FeSO4	\N	other	151.91	\N	\N	22696
367-93-1	C9H18O5S	\N	other	238.30000000000001	\N	\N	22699
67-63-0	C3H8O	\N	other	60.100000000000001	\N	\N	22702
77110-54-4	CH3OCH2CH2O[CH(CH3)CH2O]nCH2CH(NH2)CH3	\N	other	\N	\N	\N	22705
65605-36-9	CH3CH(NH2)CH2[OCH(CH3)CH2]l(OCH2CH2)m[OCH2CH(CH3)]nNH2	\N	other	\N	\N	\N	22708
25389-94-0	C18H36N4O11.H2O4S	\N	other	582.58000000000004	\N	\N	22711
63-42-3	C12H22O11	\N	other	342.30000000000001	\N	\N	22714
137-16-6	C15H28NNaO3	\N	other	293.38	\N	\N	22717
6080-56-4	Pb(CH3CO2)2.3H2O	\N	other	379.32999999999998	\N	\N	22720
301-04-2	Pb(O2C2H3)2	\N	other	325.28699999999998	\N	\N	22723
147385-61-3	C20H38N6O4	\N	other	426.55000000000001	\N	\N	22726
546-89-4	LiOOCCH3	\N	other	65.989999999999995	\N	\N	22729
6108-17-4	LiOOCCH3.2H2O	\N	other	102.02	\N	\N	22732
7550-35-8	LiBr	\N	other	86.849999999999994	\N	\N	22735
7447-41-8	LiCl	\N	other	42.390000000000001	\N	\N	22738
85144-11-2	ClLi .H2O	\N	other	42.390000000000001	\N	\N	22741
919-16-4	C6H5Li3O7	\N	other	209.923	\N	\N	22744
7789-24-4	LiF	\N	other	25.940000000000001	\N	\N	22747
556-63-8	LiCHOO	\N	other	51.960000000000001	\N	\N	22750
6080-58-6	C6H7LiO7	\N	other	281.98599999999999	\N	\N	22753
7790-69-4	LiNO3	\N	other	68.950000000000003	\N	\N	22756
09/03/7791	LiClO4	\N	other	106.39	\N	\N	22759
552-38-5	HOC6H4CO2Li	\N	other	144.05000000000001	\N	\N	22762
10377-48-7	H3LiO4S	\N	other	109.94	\N	\N	22765
10102-25-7	Li2SO4.H2O	\N	other	127.95999999999999	\N	\N	22768
16674-78-5	(CH3COO)2Mg.4H2O	\N	other	214.44999999999999	\N	\N	22771
7789-48-2	MgBr2	\N	other	184.11000000000001	\N	\N	22774
7786-30-3	MgCl2	\N	other	95.209999999999994	\N	\N	22777
7791-18-6	MgCl2 .6H2O	\N	other	203.30000000000001	\N	\N	22780
557-39-1	C2H2MgO4	\N	other	114.34	\N	\N	22783
6150-82-9	C2H2MgO4.2H2O	\N	other	150.37	\N	\N	22786
10377-60-3	MgN2O6	\N	other	148.315	\N	\N	22789
13446-18-9	Mg(NO3)2.6H2O	\N	other	256.41000000000003	\N	\N	22792
7487-88-9	MgSO4	\N	other	120.37	\N	\N	22795
22189-08-8	MgSO4.xH2O	\N	other	120.37	\N	\N	22798
14168-73-1	MgSO4.1H2O	\N	other	138.38	\N	\N	22801
17830-18-1	MgSO4.6H2O	\N	other	228.46000000000001	\N	\N	22804
10034-99-8	MgSO4.7H2O	\N	other	246.47	\N	\N	22807
05/01/7773	MnCl2	\N	other	125.84	\N	\N	22810
20603-88-7	Cl2H4MnO2	\N	other	161.874	\N	\N	22813
64333-01-3	MnCl2.H2O	\N	other	143.86000000000001	\N	\N	22816
13446-34-9	MnCl2.4H2O	\N	other	197.91	\N	\N	22819
6915-15-7	C4H6O	\N	other	134.09	\N	\N	22822
141-82-2	CH2(COOH)2	\N	other	104.06	\N	\N	22825
69-65-8	C6H14O6	\N	other	182.16999999999999	\N	\N	22828
643-01-6	\N	\N	other	182.16999999999999	\N	\N	22831
3458-28-4	C6H12O6	\N	other	180.16	\N	\N	22834
10030-80-5	C6H12O6	\N	other	180.16	\N	\N	22837
92944-71-3	C17H11NO3	\N	other	277.26999999999998	\N	\N	22840
60-24-2	C2H6OS	\N	other	78.129999999999995	\N	\N	22843
1600-27-7	(CH3COO)2Hg	\N	other	318.68000000000001	\N	\N	22846
7487-94-7	HgCl2	\N	other	271.5	\N	\N	22849
486-67-9	HOHgCH2CH(OCH3)CH2NHCOC6H4OCH2CO2H	\N	other	483.87	\N	\N	22852
4432-31-9	C6H13NO4S	\N	other	195.24000000000001	\N	\N	22855
71119-23-8	C6H12NO4SNa	\N	other	217.22	\N	\N	22858
67-56-1	CH3OH	\N	other	32.039999999999999	\N	\N	22861
79-20-9	CH3COOCH3	\N	other	74.079999999999998	\N	\N	22864
115-09-3	CH3HgCl	\N	other	251.08000000000001	\N	\N	22867
107-41-5	CH3CH(OH)CH2C(CH3)2OH	\N	other	118.17	\N	\N	22870
115724-21-5	C8H17NO4S	\N	other	223.28999999999999	\N	\N	22873
1132-61-2	C7H15NO4S	\N	other	209.25999999999999	\N	\N	22876
68399-77-9	C7H15NO5S	\N	other	225.25999999999999	\N	\N	22879
87-89-8	C6H12O6	\N	other	180.16	\N	\N	22882
1184-16-3	C21H27N7Na2O17P3	\N	other	765.38999999999999	\N	\N	22885
2646-71-1	C21H26N7Na4O17P3	\N	other	833.35000000000002	\N	\N	22888
6066-82-6	C4H5NO3	\N	other	115.09	\N	\N	22891
7718-54-9	NiCl2	\N	other	129.59999999999999	\N	\N	22894
69098-15-3	NiCl2.xH2O	\N	other	129.59999999999999	\N	\N	22897
7791-20-0	NiCl2.6H2O	\N	other	237.69	\N	\N	22900
10101-97-0	NiO4S.6H2O	\N	other	262.85000000000002	\N	\N	22903
7697-37-2	HNO3	\N	other	63.009999999999998	\N	\N	22906
85261-19-4	C16H33NO6	\N	other	335.44	\N	\N	22909
3055-99-0	C30H62O10	\N	other	582.80999999999995	\N	\N	22912
24233-81-6	CH3(CH2)9(OCH2CH2)8OH	\N	other	510.69999999999999	\N	\N	22915
29836-26-8	C14H28O6	\N	other	292.37	\N	\N	22918
85618-21-9	C14H28O5S	\N	other	308.39999999999998	\N	\N	22921
29836-26-8	C14H28O6	\N	other	292.36900000000003	\N	\N	22924
85316-98-9	C15H31NO6	\N	other	321.41000000000003	\N	\N	22927
69984-73-2	C15H30O6	\N	other	306.39999999999998	\N	\N	22930
7664-38-2	HP304	\N	other	98.079999999999998	\N	\N	22933
\N	\N	\N	other	\N	\N	\N	22936
59-85-8	C7H5ClHgO2	\N	other	357.15600000000001	\N	\N	22939
554-77-8	C6H5ClHgO3S	\N	other	393.21100000000001	\N	\N	22942
16593-81-0	C11H8N3NaO2  H2O	\N	other	237.19	\N	\N	22945
30599-15-6	C[CH2(OCH2CH2)nOH]4	\N	other	\N	\N	\N	22948
9051-49-4	C[CH2[OCH2CH(CH3)]nOH]4	\N	other	\N	\N	\N	22951
23244-49-7	C20H42O6	\N	other	378.54000000000002	\N	\N	22954
19327-40-3	C18H38O6	\N	other	350.49000000000001	\N	\N	22957
96-22-0	CH3CH2COCH2CH3	\N	other	86.129999999999995	\N	\N	22960
51724-57-3	C33H61N5O9	\N	other	671.87	\N	\N	22963
90412-47-8	C12N2D8	\N	other	1988.25	\N	\N	22966
108-95-2	C6H5OH	\N	other	94.109999999999999	\N	\N	22969
143-74-8	C19H14O5S	\N	other	354.38	\N	\N	22972
1075-06-5	C6H5COCHO.xH2O	\N	other	134.13	\N	\N	22975
62-38-4	C6H5HgOCOCH3	\N	other	336.74000000000001	\N	\N	22978
8002-43-5	C10H19NO8PR2	\N	other	\N	\N	\N	22981
5625-37-6	C8H18N2O6S2	\N	other	302.37	\N	\N	22984
329-98-6	C7H7FO2S	\N	other	174.19	\N	\N	22987
07/04/9003	\N	\N	other	\N	\N	\N	22990
25322-68-3	H(OCH2CH2)nOH	\N	other	400	\N	\N	22993
25322-68-3	H(OCH2CH2)nOH	\N	other	200	\N	\N	22996
25322-68-3	H(OCH2CH2)nOH	\N	other	300	\N	\N	22999
25322-68-3	H(OCH2CH2)nOH	\N	other	600	\N	\N	23002
25322-68-3	H(OCH2CH2)nOH	\N	other	1000	\N	\N	23005
25322-68-3	H(OCH2CH2)nOH	\N	other	1500	\N	\N	23008
25322-68-3	H(OCH2CH2)nOH	\N	other	2000	\N	\N	23011
25322-68-3	H(OCH2CH2)nOH	\N	other	3000	\N	\N	23014
25322-68-3	H(OCH2CH2)nOH	\N	other	3350	\N	\N	23017
25322-68-3	H(OCH2CH2)nOH	\N	other	3350	\N	\N	23020
25322-68-3	H(OCH2CH2)nOH	\N	other	4000	\N	\N	23023
25322-68-3	H(OCH2CH2)nOH	\N	other	5000	\N	\N	23026
25322-68-3	H(OCH2CH2)nOH	\N	other	6000	\N	\N	23029
25322-68-3	H(OCH2CH2)nOH	\N	other	8000	\N	\N	23032
25322-68-3	H(OCH2CH2)nOH	\N	other	10000	\N	\N	23035
25322-68-3	H(OCH2CH2)nOH	\N	other	20000	\N	\N	23038
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	550	\N	\N	23041
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	2000	\N	\N	23044
9004-74-4	CH3OCH3OCH3O(CH2CH2O)nH	\N	other	5000	\N	\N	23047
9002-98-6	(C2H5N)n	\N	other	43.078000000000003	\N	\N	23050
25322-69-4	\N	\N	other	\N	\N	\N	23053
1405-20-5	C55H96N16O13.2H2SO4	\N	other	1385.6099999999999	\N	\N	23056
24937-83-5	\N	\N	other	\N	\N	\N	23059
9002-89-5	[-CH2CHOH-]n	\N	other	\N	\N	\N	23062
9003-39-8	(C6H9NO)n	\N	other	\N	\N	\N	23065
59491-62-2	C42H82NO8P	\N	other	760.08000000000004	\N	\N	23068
68189-43-5	C10H22NO8S2.2H2O	\N	other	398.44999999999999	\N	\N	23071
127-08-2	C2H3KO2	\N	other	98.140000000000001	\N	\N	23074
03/02/7758	Kbr	\N	other	119	\N	\N	23077
7447-40-7	KCl	\N	other	74.549999999999997	\N	\N	23080
866-83-1	KH2C6H5O7	\N	other	230.21000000000001	\N	\N	23083
06/05/6100	HOC(COOK)(CH2COOK)2.H2O	\N	other	324.41000000000003	\N	\N	23086
590-28-3	KOCN	\N	other	81.120000000000005	\N	\N	23089
7778-77-0	KH2PO4	\N	other	136.09	\N	\N	23092
04/11/7758	K2HPO4	\N	other	174.18000000000001	\N	\N	23095
16788-57-1	K2HPO4.3H2O	\N	other	228.22	\N	\N	23098
7789-23-3	KF	\N	other	58.100000000000001	\N	\N	23101
590-29-4	HCOOK	\N	other	84.120000000000005	\N	\N	23104
16920-56-2	K2IrCl6	\N	other	481.14999999999998	\N	\N	23107
16921-30-5	K2PtCl6	\N	other	485.99000000000001	\N	\N	23110
1310-58-3	KOH	\N	other	56.109999999999999	\N	\N	23113
7722-64-7	KMnO4	\N	other	158.03	\N	\N	23116
7681-11-0	KI	\N	other	166.00299999999999	\N	\N	23119
7757-79-1	KNO3	\N	other	101.09999999999999	\N	\N	23122
6381-59-5	KOCOCH(OH)CH(OH)COONa.4H2O	\N	other	282.22000000000003	\N	\N	23125
304-59-6	C4H4KNaO6	\N	other	210.15899999999999	\N	\N	23128
7778-80-5	K2SO4	\N	other	174.25999999999999	\N	\N	23131
921-53-9	K2C4H4O6	\N	other	226.27000000000001	\N	\N	23134
13682-61-6	KauCl4	\N	other	377.88	\N	\N	23137
10025-99-7	K2PtCl4	\N	other	415.08999999999997	\N	\N	23140
562-76-5	K2PtCN4	\N	other	377.33999999999997	\N	\N	23143
7783-33-7	K2Hg(I)4	\N	other	786.39999999999998	\N	\N	23146
13815-39-9	K2Pt(NO2)4	\N	other	459.31	\N	\N	23149
333-20-0	KSCN	\N	other	97.180000000000007	\N	\N	23152
147-85-3	C5H9NO2	\N	other	115.13	\N	\N	23155
67-63-0	CH3CH(OH)CH3	\N	other	60.100000000000001	\N	\N	23158
57-55-6	CH3CH(OH)CH2OH	\N	other	76.090000000000003	\N	\N	23161
504-63-2	HO(CH2)3OH	\N	other	76.090000000000003	\N	\N	23164
107-12-0	CH3CH2CN	\N	other	55.079999999999998	\N	\N	23167
110-86-1	C5H5N	\N	other	79.099999999999994	\N	\N	23170
9009-65-8	\N	\N	other	\N	\N	\N	23173
65-22-5	C8H9NO3.HCl	\N	other	203.62	\N	\N	23176
17629-30-0	C18H32O16.5H2O	\N	other	594.50999999999999	\N	\N	23179
83-88-5	C17H20N4O6	\N	other	376.36000000000001	\N	\N	23182
7789-39-1	RbBr	\N	other	165.37	\N	\N	23185
09/11/7791	RbCl	\N	other	120.92	\N	\N	23188
7761-88-8	AgNO3	\N	other	169.87	\N	\N	23191
127-09-3	CH3COONa	\N	other	82.030000000000001	\N	\N	23194
6131-90-4	CH3COONa.3H2O	\N	other	136.08000000000001	\N	\N	23197
26628-22-8	NaN3	\N	other	65.010000000000005	\N	\N	23200
7647-15-6	NaBr	\N	other	102.89	\N	\N	23203
\N	(CH3)2AsO2Na	\N	other	159.97999999999999	\N	\N	23206
6131-99-3	(CH3)2AsO2Na.3H2O	\N	other	214.03	\N	\N	23209
497-19-8	Na2CO3	\N	other	105.98999999999999	\N	\N	23212
7647-14-5	NaCl	\N	other	58.439999999999998	\N	\N	23215
03/04/6132	HOC(COONa)(CH2COONa)2.2H2O	\N	other	294.10000000000002	\N	\N	23218
18996-35-5	HOC(COONa)(CH2COOH)2	\N	other	214.11000000000001	\N	\N	23221
302-95-4	C24H39O4Na	\N	other	414.55000000000001	\N	\N	23224
145224-92-6	C24H39O4Na.H2O	\N	other	432.56999999999999	\N	\N	23227
151-21-3	C12H25OSO2Na	\N	other	288.38	\N	\N	23230
7681-49-4	FNa	\N	other	41.990000000000002	\N	\N	23233
141-53-7	HCOONa	\N	other	68.010000000000005	\N	\N	23236
1120-01-0	C16H33NaSO4	\N	other	344.54000000000002	\N	\N	23239
144-55-8	NaHCO3	\N	other	84.010000000000005	\N	\N	23242
13472-35-0	NaH2PO4.2H2O	\N	other	156.00999999999999	\N	\N	23245
10028-24-7	Na2HPO4.2H2O	\N	other	177.99000000000001	\N	\N	23248
7558-79-4	Na2HPO4	\N	other	141.96000000000001	\N	\N	23251
1310-73-2	NaOH	\N	other	40	\N	\N	23254
7681-82-5	NaI	\N	other	149.88999999999999	\N	\N	23257
141-95-7	CH2(COONa)2	\N	other	148.03	\N	\N	23260
26522-85-0	CH2(CO2Na)2.H2O	\N	other	166.03999999999999	\N	\N	23263
7790-28-5	NaIO4	\N	other	213.88999999999999	\N	\N	23266
7631-99-4	NaNO3	\N	other	84.989999999999995	\N	\N	23269
7632-00-0	NaNO2	\N	other	69	\N	\N	23272
7601-89-0	NaClO4	\N	other	122.44	\N	\N	23275
7601-54-9	Na3PO4	\N	other	163.94	\N	\N	23278
137-40-6	CH3CH2COONa	\N	other	96.060000000000002	\N	\N	23281
150-90-3	NaOOCCH2CH2COONa	\N	other	162.05000000000001	\N	\N	23284
7757-82-6	Na2SO4	\N	other	142.03999999999999	\N	\N	23287
7727-73-3	Na2SO4.10H2O	\N	other	322.19999999999999	\N	\N	23290
6106-24-7	C4H4Na2O6.2H2O	\N	other	230.08000000000001	\N	\N	23293
1330-43-4	Na2B4O7	\N	other	201.22	\N	\N	23296
540-72-7	NaSCN	\N	other	81.069999999999993	\N	\N	23299
10102-17-7	Na2O3S2  5H2O	\N	other	248.18000000000001	\N	\N	23302
650-51-1	CCl3CO2Na	\N	other	185.37	\N	\N	23305
56421-10-4	C44H86NO8P	\N	other	788.13	\N	\N	23308
50-70-4	C6H14O6	\N	other	182.16999999999999	\N	\N	23311
124-20-9	NH2(CH2)3NH(CH2)4NH2	\N	other	145.25	\N	\N	23314
334-50-9	NH2(CH2)3NH(CH2)4NH2.3HCl	\N	other	254.63	\N	\N	23317
306-67-2	C10H26N4.4HCl	\N	other	348.18000000000001	\N	\N	23320
3810-74-0	(C21H39N7O12)2.3H2SO4	\N	other	1457.3800000000001	\N	\N	23323
10476-85-4	SnCl2	\N	other	158.53	\N	\N	23326
110-15-6	C4H6O4	\N	other	118.09	\N	\N	23329
108-30-5	C4H4O3	\N	other	100.06999999999999	\N	\N	23332
123-56-8	C4H5NO2	\N	other	99.090000000000003	\N	\N	23335
57-50-1	C12H22O11	\N	other	342.30000000000001	\N	\N	23338
167410-92-6	C18H20N2O18S2	\N	other	616.48000000000002	\N	\N	23341
7664-93-9	H2SO4	\N	other	98.079999999999998	\N	\N	23344
54960-65-5	C8H19NO6S	\N	other	257.30000000000001	\N	\N	23347
\N	\N	\N	other	\N	\N	\N	23350
29915-38-6	C7H17NO6S	\N	other	243.28	\N	\N	23353
68399-81-5	C7H17NO7S	\N	other	259.27999999999997	\N	\N	23356
125-35-7	NH2CH2CH2SO3H	\N	other	125.15000000000001	\N	\N	23359
110-18-9	C6H16N2	\N	other	116.2	\N	\N	23362
7365-44-8	C6H15NO6S	\N	other	229.25	\N	\N	23365
64-75-5	C22H24N2O8.HCl	\N	other	480.89999999999998	\N	\N	23368
19327-39-0	CH3(CH2)7(OCH2CH2)4OH	\N	other	306.44	\N	\N	23371
14933-09-6	C19H41NO3S	\N	other	363.60000000000002	\N	\N	23374
109-99-9	C4H8O	\N	other	72.109999999999999	\N	\N	23377
2426-02-0	C8H8O3	\N	other	151.15000000000001	\N	\N	23380
25201-30-3	C(HgOOCCH3)4	\N	other	1054.5799999999999	\N	\N	23383
75-57-0	(CH3)4N(Cl)	\N	other	109.59999999999999	\N	\N	23386
54-64-8	C9H9HgNaO2S	\N	other	404.81	\N	\N	23389
89-83-8	C10H140	\N	other	150.22	\N	\N	23392
4272-74-6	C14H21ClN2O3S.HCl	\N	other	369.31	\N	\N	23395
402-71-1	C17H18ClNO3S	\N	other	351.85000000000002	\N	\N	23398
16858-02-9	C26H28N6	\N	other	424.54000000000002	\N	\N	23401
6138-23-4	C12H22O11	\N	other	342.30000000000001	\N	\N	23404
76-03-9	C2HCl3O2	\N	other	163.38999999999999	\N	\N	23407
01/04/5704	C6H13NO5	\N	other	179.16999999999999	\N	\N	23410
102-71-6	C6H15NO3	\N	other	149.19	\N	\N	23413
112-27-6	HO(CH2CH2O)2CH2CH2OH	\N	other	150.16999999999999	\N	\N	23416
75-89-8	C2H3F3O	\N	other	100.04000000000001	\N	\N	23419
593-81-7	(CH3)3N.HCl	\N	other	95.569999999999993	\N	\N	23422
1184-78-7	(CH3)3N(O)	\N	other	75.109999999999999	\N	\N	23425
593-81-7	(CH3)3N.HCl	\N	other	95.569999999999993	\N	\N	23428
5711-19-3	CH3CO2Pb(CH3)3	\N	other	311.35000000000002	\N	\N	23431
298-96-4	C19H15ClN4	\N	other	334.80000000000001	\N	\N	23434
77-86-1	NH2C(CH2OH)	\N	other	121.14	\N	\N	23437
1185-53-1	NH2C(CH2OH)3.HCl	\N	other	157.59999999999999	\N	\N	23440
50525-27-4	C30H24Cl2N6Ru.6H2O	\N	other	748.62	\N	\N	23443
9002-93-1	C14H22O(C2H4O)9-10	\N	other	\N	\N	\N	23446
9036-19-5	C14H22O(C2H4O)7-8	\N	other	\N	\N	\N	23449
37330-34-0	\N	\N	other	\N	\N	\N	23452
9035-81-8	\N	\N	other	\N	\N	\N	23455
9005-64-5	C58H11O26	\N	other	\N	\N	\N	23458
57-13-6	CH4N2O	\N	other	60.060000000000002	\N	\N	23461
1404-93-9	C66H75Cl2N9O24.HCl	\N	other	1485.71	\N	\N	23464
68-19-9	C63H88CoN14O14P	\N	other	1355.3699999999999	\N	\N	23467
7240-90-6	C14H15BrClNO6	\N	other	408.63	\N	\N	23470
2650-17-1	C25H27N2NaO6S2	\N	other	538.61000000000001	\N	\N	23473
3618-43-7	C31H28N2Na4O13S	\N	other	760.58000000000004	\N	\N	23476
87-99-0	HOCH2[CH(OH)]3CH2OH	\N	other	152.15000000000001	\N	\N	23479
10035-01-5	YbCl3.6H2O	\N	other	387.49000000000001	\N	\N	23482
10025-94-2	Cl3Y.6H2O	\N	other	303.36000000000001	\N	\N	23485
10361-92-9	YCl3	\N	other	195.25999999999999	\N	\N	23488
5970-45-6	C4H6O4Zn.2H2O	\N	other	219.50999999999999	\N	\N	23491
557-34-6	(CH3CO2)2Zn	\N	other	183.47999999999999	\N	\N	23494
7646-85-7	ZnCl2	\N	other	136.30000000000001	\N	\N	23497
7446-20-0	ZnSO4.7H2O	\N	other	287.56	\N	\N	23500
7733-02-0	ZnSO4	\N	other	161.45400000000001	\N	\N	23503
\N	\N	\N	DNA	\N	Forward Extension	GGCCAATTGGCCAATT	30003
\N	\N	\N	DNA	\N	Reverse Extension	AATTGGCCAATTGGCC	30004
\N	\N	\N	DNA	\N	\N	\N	36003
\N	\N	\N	DNA	\N	\N	\N	36016
\N	\N	\N	DNA	\N	\N	\N	36029
\N	\N	\N	DNA	\N	\N	\N	36042
\N	\N	\N	DNA	\N	\N	\N	36057
\N	\N	\N	DNA	\N	\N	\N	36065
\N	\N	\N	DNA	\N	\N	\N	36076
\N	\N	\N	DNA	\N	\N	\N	36088
\N	\N	\N	DNA	\N	\N	\N	36101
\N	\N	\N	DNA	\N	\N	\N	36113
\N	\N	\N	DNA	\N	\N	\N	36119
\N	\N	\N	DNA	\N	\N	\N	36125
\N	\N	\N	DNA	\N	\N	\N	36131
\N	\N	\N	DNA	\N	\N	\N	36137
\N	\N	\N	DNA	\N	\N	\N	36144
\N	\N	\N	DNA	\N	\N	\N	36150
\N	\N	\N	DNA	\N	\N	\N	36156
\N	\N	\N	DNA	\N	\N	\N	36162
\N	\N	\N	DNA	\N	\N	\N	36168
\N	\N	\N	DNA	\N	\N	\N	36174
\N	\N	\N	DNA	\N	\N	\N	36180
\N	\N	\N	DNA	\N	\N	\N	36190
\.


--
-- Data for Name: mole_molecule2relareobel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_molecule2relareobel (relatedresearchobjectiveelementid, trialmoleculeid) FROM stdin;
\.


--
-- Data for Name: mole_moleculefeature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY mole_moleculefeature (endseqid, featuretype, name, ordering, startseqid, status, labbookentryid, moleculeid, refmoleculeid) FROM stdin;
4845	resistance	Ampicillin	\N	3988	\N	36004	36003	\N
327	promoter	T7 Promoter	\N	311	\N	36005	36003	\N
239	Tag	T7-Tag coding sequence	\N	207	\N	36006	36003	\N
203	MCS	Multiple Cloning sites BamHI-XhoI	\N	158	\N	36007	36003	\N
157	Tag	C-Term His Tag coding sequence	\N	140	\N	36008	36003	\N
239	Cloning site	NcoI cloning site for native expression -no tag	\N	234	\N	36009	36003	\N
\N	ori	pBR322 origin	\N	3227	\N	36010	36003	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36011	36003	\N
3430	resistance	Ampicillin	\N	2570	\N	36017	36016	\N
40	promoter	T7 Promoter	\N	21	\N	36018	36016	\N
130	Tag	N-term His Tag coding sequence	\N	113	\N	36019	36016	\N
264	recombination site	attR1 site	\N	140	\N	36020	36016	\N
1032	antibiotic resistance	Chloramphenicol resistance gene (CmR)	\N	373	\N	36021	36016	\N
1679	selectable marker	ccdB gene	\N	1374	\N	36022	36016	\N
1844	recombination site	attR1 site	\N	1720	\N	36023	36016	\N
4248	ori	pBR322 origin	\N	3575	\N	36024	36016	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36025	36016	\N
4798	resistance	Kannamycin	\N	3986	\N	36030	36029	\N
377	promoter	T7 Promoter	\N	361	\N	36031	36029	\N
289	Leader sequence	pelB coding sequence	\N	224	not cleaved	36032	36029	\N
225	MCS	Multiple Cloning sites NcoI-XhoI	\N	158	\N	36033	36029	\N
225	Cloning site	NcoI cloning site for adding N-term pelB tag	\N	220	\N	36034	36029	\N
287	Cloning site	NdeI cloning site for native expression -no tag	\N	282	\N	36035	36029	\N
157	Tag	C-Term His Tag coding sequence	\N	140	\N	36036	36029	\N
\N	ori	pBR322 origin	\N	3277	\N	36037	36029	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36038	36029	\N
4807	resistance	Kannamycin	\N	3995	\N	36043	36042	\N
386	promoter	T7 Promoter	\N	370	\N	36044	36042	\N
287	Tag	Thrombin cleavable N-term His Tag coding sequence	\N	270	\N	36045	36042	\N
203	MCS	Multiple Cloning sites BamHI-XhoI	\N	158	\N	36046	36042	\N
242	Cloning site	NdeI cloning site for adding N-term His Tag	\N	237	\N	36047	36042	\N
301	Cloning site	NcoI cloning site for native expression -no tag	\N	296	\N	36048	36042	\N
157	Tag	C-Term His Tag coding sequence	\N	140	\N	36049	36042	\N
\N	cleavage site	Thrombin cleavage site	\N	\N	not cleaved	36050	36042	\N
\N	ori	pBR322 origin	\N	3286	\N	36051	36042	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36052	36042	\N
\N	resistance	Ampicillin	\N	\N	\N	36058	36057	\N
\N	resistance	Hygromycin	\N	\N	\N	36059	36057	\N
\N	Leader sequence	N-Term cleavable secretion leader sequence	\N	\N	not cleaved	36060	36057	\N
\N	Tag	C-Term His Tag sequence	\N	\N	\N	36061	36057	\N
3528	resistance	Gentamycin	\N	4061	\N	36066	36065	\N
506	recombination site	attP1 site	\N	332	\N	36067	36065	\N
2265	antibiotic resistance	Chloramphenicol resistance gene (CmR)	\N	1606	\N	36068	36065	\N
1264	selectable marker	ccdB gene	\N	959	\N	36069	36065	\N
2744	recombination site	attP2 site	\N	2513	\N	36070	36065	\N
5582	ori	pUC origin	\N	4909	\N	36071	36065	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36072	36065	\N
2506	resistance	Chloramphenical	\N	1847	\N	36077	36076	\N
3965	resistance	Kanamycin	\N	3156	\N	36078	36076	\N
801	recombination site	attP1 site	\N	570	\N	36079	36076	\N
2265	antibiotic resistance	Chloramphenicol resistance gene (CmR)	\N	1606	\N	36080	36076	\N
1502	selectable marker	ccdB gene	\N	1197	\N	36081	36076	\N
2985	recombination site	attP2 site	\N	2754	\N	36082	36076	\N
4759	ori	pUC origin	\N	4086	\N	36083	36076	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36084	36076	\N
2025	resistance	Kanamycin	\N	1321	\N	36089	36088	\N
2605	resistance	Zeocin	\N	2231	\N	36090	36088	\N
419	promoter	T7 Promoter	\N	400	\N	36091	36088	\N
216	promoter	Lac Promoter Operator	\N	95	\N	36092	36088	\N
570	selectable marker	LacZ-alpha ORF	\N	217	\N	36093	36088	\N
882	selectable marker	ccdB gene ORF	\N	580	\N	36094	36088	\N
418	MCS	Multiple Cloning sites SmaI-DraIII	\N	269	\N	36095	36088	\N
3386	ori	pUC origin	\N	2673	\N	36096	36088	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36097	36088	\N
5500	resistance	Ampicillin	\N	4643	\N	36102	36101	\N
479	promoter	T7 Promoter	\N	463	\N	36103	36101	\N
353	Tag	Thrombin cleavable N-term His Tag coding sequence	\N	337	\N	36104	36101	\N
335	MCS	Multiple Cloning sites NdeI-BamHI	\N	319	\N	36105	36101	\N
294	Cloning site	NcoI cloning site for native expression -no tag	\N	289	\N	36106	36101	\N
\N	cleavage site	Thrombin cleavage site	\N	\N	not cleaved	36107	36101	\N
\N	ori	pBR322 origin	\N	3882	\N	36108	36101	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36109	36101	\N
\N	resistance	Kannamycin	\N	\N	\N	36114	36113	\N
\N	Tag	C-Term His Tag sequence	\N	\N	\N	36115	36113	\N
\N	resistance	Kannamycin	\N	\N	\N	36120	36119	\N
\N	Tag	N-Term cleavable His Tag sequence	\N	\N	not cleaved	36121	36119	\N
\N	resistance	Ampicillin	\N	\N	\N	36126	36125	\N
\N	Tag	C-Term His Tag sequence	\N	\N	\N	36127	36125	\N
\N	resistance	Ampicillin	\N	\N	\N	36132	36131	\N
\N	Tag	N-Term cleavable His Tag sequence	\N	\N	not cleaved	36133	36131	\N
\N	resistance	Ampicillin	\N	\N	\N	36138	36137	\N
\N	Leader sequence	N-Term cleavable secretion leader sequence	\N	\N	not cleaved	36139	36137	\N
\N	Tag	C-Term His Tag sequence	\N	\N	\N	36140	36137	\N
\N	resistance	Ampicillin	\N	\N	\N	36145	36144	\N
\N	Combined Leader sequence and Tag	N-Term cleavable secretion leader and cleavable N-his tag	\N	\N	not cleaved	36146	36144	\N
\N	resistance	Ampicillin	\N	\N	\N	36151	36150	\N
\N	Tag	N-Term His Tag sequence	\N	\N	\N	36152	36150	\N
\N	resistance	Ampicillin	\N	\N	\N	36157	36156	\N
\N	Tag	N-Term cleavable His Tag with GST fusion sequence	\N	\N	not cleaved	36158	36156	\N
\N	resistance	Kannamycin	\N	\N	\N	36163	36162	\N
\N	Tag	N-Term cleavable His Tag with GST fusion sequence	\N	\N	not cleaved	36164	36162	\N
\N	resistance	Ampicillin	\N	\N	\N	36169	36168	\N
\N	Tag	N-Term cleavable His Tag with MBP fusion sequence	\N	\N	not cleaved	36170	36168	\N
\N	resistance	Kannamycin	\N	\N	\N	36175	36174	\N
\N	Tag	N-Term cleavable His Tag with SUMO fusion sequence	\N	\N	not cleaved	36176	36174	\N
4025	resistance	Ampicillin	\N	3165	\N	36181	36180	\N
231	MCS	Multiple Cloning sites EcoRI-NarI	\N	15	\N	36182	36180	\N
4799	ori	pBR322 origin	\N	4180	\N	36183	36180	\N
367	selection	URA3	\N	1167	\N	36184	36180	\N
-2768	recombination system	Yeast 2 micron sequence	\N	1384	\N	36185	36180	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36186	36180	\N
5395	resistance	Ampicillin	\N	4538	\N	36191	36190	\N
1726	promoter	Chicken actin Promoter	\N	1449	\N	36192	36190	\N
2166	promoter	T7 Promoter	\N	2150	\N	36193	36190	\N
2318	promoter	p10 Promoter	\N	2205	\N	36194	36190	\N
2356	Tag	N-term His Tag coding sequence	\N	2339	\N	36195	36190	\N
2410	Tag	N-term S-Tag coding sequence	\N	2366	\N	36196	36190	\N
2668	MCS	Multiple Cloning sites SmaI-DraIII	\N	2333	\N	36197	36190	\N
2306	Cloning site	NcoI cloning site for native expression -no tag	\N	2301	\N	36198	36190	\N
2480	Cloning site	PshAI cloning site for cleavable enterokinase tag	\N	2471	\N	36199	36190	\N
2438	Cloning site	SmaI cloning site for cleavable thrombin tag	\N	2433	\N	36200	36190	\N
2632	Tag	C-term HSV-Tag coding sequence	\N	2597	\N	36201	36190	\N
2662	Tag	C-term His-Tag coding sequence	\N	2639	\N	36202	36190	\N
\N	cleavage site	Thrombin cleavage site	\N	\N	not cleaved	36203	36190	\N
\N	cleavage site	Enterokinase cleavage site	\N	\N	not cleaved	36204	36190	\N
\N	ori	pUC origin	\N	3936	\N	36205	36190	\N
\N	Hosts	Recommended Hosts	\N	\N	\N	36206	36190	\N
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
14002	Inchinnan Business Park	0
14002	3 Fountain Drive	1
14003	Unit 4	0
14003	Northfield Business Park	1
14003	Northfield Road	2
14003	Cambridgeshire	3
14004	73 Knowl Piece	0
14004	Wilbury Way	1
14004	Hetrts	2
14005	MERCK Biosciences Ltd	0
14005	Boulevard Industrial Park	1
14005	Padge Road	2
14005	Beeston	3
14006	Delta House	0
14006	Chilworth Science Park	1
14007	Wellington House 	0
14007	East Road	1
14007	Cambridgeshire	2
14008	Qiagen House	0
14008	Fleming Way	1
14008	West Sussex	2
14009	Gebouw California	0
14009	Hogehilweg 15	1
14009	Zuidoost	2
14010	66A Cambridge Road	0
14011	Amersham Place	0
14011	Buckinghamshire	1
14012	434 West Dussel Drive 	0
14013	700 Industrial Park Drive	0
14014	Delph Court	0
14014	Sherdley Business Park Industrial Estate	1
14014	Sullivans Way	2
14015	Bio-Rad House	0
14015	Maxted Road	1
14015	Hertfordshire	2
14016	MERCK Biosciences Ltd	0
14016	Boulevard Industrial Park	1
14016	Padge Road	2
14016	Beeston	3
14017	Bishop Meadow Road	0
14018	ZGZ	0
14018	Zapfholzweg 1	1
14019	34 Journey	0
14019	California	1
14020	Loebstedter Strasse 7	0
14021	Boulevard Industrial Park	0
14021	Padge Road	1
14021	Beeston	2
14022	Invitrogen Ltd	0
14022	Inchinnan Business Park	1
14022	3 Fountain Drive	2
14023	172 E. Aurora Street	0
14024	Perbio Science UK Ltd	0
14024	Unit 9 Atley Way	1
14024	North Nelson Industrial Estate	2
14024	Northumberland	3
14025	6 Caberston Road	0
14026	40 Broadwater Road	0
14026	P.O.Box 8	1
14026	Hertfordshire	2
14027	Fancy Road	0
14027	Dorset	1
14028	Nissanstrasse 2	0
14029	BioCampus Cologne	0
14029	Nattermannallee 1	1
14030	90 Long Acre	0
14030	Covent Garden	1
14031	1290 Terra Bella Ave.	0
14031	Mountain View	1
14032	104 Avenue Franklin Roosevelt - Box 25	0
14032	B-1330 Rixensar	1
14033	500 Cummings Center	0
14033	Suite 2450	1
14033	Beverly 	2
14034	LGC Standards	0
14034	Queens Road	1
14034	Middlesex	2
\.


--
-- Data for Name: peop_organisation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY peop_organisation (city, country, emailaddress, faxnumber, name, organisationtype, phonenumber, postalcode, url, labbookentryid) FROM stdin;
London	UK	info@bioline.com	0208 4522822	Bioline Ltd	Supplier	0208 8305300	NW2 6EW	http://www.bioline.com/n_uk.htm	14001
Paisley	Scotland	euroinfo@invitrogen.com	0141 814 6260	Invitrogen/Life Technologies	Supplier	0141 814 6100	PA4 9RF	http://www.invitrogen.com/	14002
Soham	UK	enquiries@moleculardimensions.com	01353 722119	Molecular Dimensions	Supplier	01353 722177	CB7 5UE	http://www.moleculardimensions.com/uk	14003
Hitchin	UK	info@uk.neb.com	0800 435682	New England Biolabs (UK) Ltd	Supplier	0800 318486	SG4 0TY	http://www.neb.uk.com/	14004
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	Novagen	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/html/NVG/home.html	14005
Southampton	UK	ukcustserve@promega.com	0800 181037	Promega	Supplier	0800 378994	SO16 7NS	www.promega.com/uk/	14006
Cambridge	UK	ukorders@qbiogene.com	0800 085 3090	Qbiogene	Supplier	0800 328 8401	CB1 1BH	http://www.qbiogene.com	14007
Crawley	UK	CustomerService-uk@qiagen.com	1293422922	Qiagen Ltd	Supplier	01293 422911	RH10 9NQ	http://www1.qiagen.com/	14008
Amsterdam	The Netherlands	orders.europe@stratagene.com	0800 9173283	Stratagene	Supplier	0800 9173282	1101 CB	http://www.stratagene.com	14009
Stansted	UK	websales@agarscientific.com	01279 815106	Agar Scientific	Supplier	01279 813519	CM24 8DA	http://www.agarscientific.com/	14010
Little Chalfont	UK	orders.gb@amersham.com	01494 544350	Amersham Biosciences UK Limited	Supplier	0870 606 1921	HP7 9NA	http://www1.amershambiosciences.com/aptrix/upp01077.nsf/Content/uk_homepage	14011
Maumee	USA	info@anatrace.com	1 419 891 3037	Anatrace Inc.	Supplier	1 419 891 3030	OH 43537	http://www.anatrace.com/	14012
Alabaster	USA	orders@avantilipids.com	1 205 663-00756	Avanti Polar Lipids, Inc	Supplier	1 205 663-2494	AL 35007	http://www.avantilipids.com	14013
St. Helens	UK	\N	01744 73 00 64	Bachem (UK) Ltd	Supplier	01744 61 21 08	WA9 5GL	http://www.bachem.com/	14014
Hemel Hempstead	UK	uk.lsg.marketing@bio-rad.com	020 8328 2550	Bio-Rad Laboratories Ltd	Supplier	0800 181134	HP2 7DX	http://www.bio-rad.com	14015
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	Calbiochem	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/html/CBC/home.html	14016
Loughborough	UK	sales@fisher.co.uk	01509 231893	Fisher Scientific UK Ltd	Supplier	01509 231166	LE11 5RG	http://www.fisher.co.uk/index.htm	14017
Luckenwalde	Germany	info@glycon.de	49 3371  681170	Glycon Biochemicals	Supplier	49 3371  681171	D-14943	http://www.glycon.de/	14018
Aliso Viejo	USA	info@hrmail.com	1 949 425 1611	Hampton research Corp.	Supplier	1 949 425 1321	CA 92656-3317	http://www.hamptonresearch.com/	14019
Jena	Germany	info@jenabioscience.com	49 3641 464991	Jena Bioscience GmbH	Supplier	49 3641 464952	D-07749	http://www.jenabioscience.com	14020
Nottingham	UK	customer.service@merckbiosciences.co.uk	0115 943 0951	MERCK Biosciences Ltd	Supplier	0800 622935	 ND9 2JR	http://www.merckbiosciences.co.uk/home.asp	14021
Paisley	UK	euroinfo@invitrogen.com	1 541 335 0202	Molecular Probes	Supplier	1 541 335 0485	PA4 9RF	http://www.probes.com/	14022
Waterbury	USA	sales@pfaltzandbauer.com	203 574 3181	Pfaltz & Bauer	Supplier	1 203 574 0075	CT 06708	http://www.pfaltzandbauer.com/	14023
Cramlington	UK	uk.info@perbio.com	0800 085 8772	Pierce	Supplier	0800 252 185	NE231WA	http://www.piercenet.com/	14024
Walkerburn	UK	info@rathburn.co.uk	01896 870 633	Rathburn Chemicals Ltd	Supplier	01896 870 651	EH43 6AU	http://www.rathburn.co.uk/	14025
Welwyn Garden City	UK	\N	01707 338297	Roche Products Ltd	Supplier	01707 366000	AL7 3AY	http://www.rocheuk.com	14026
Poole	UK	ukcustsv@europe.sial.com	0800 378785	Sigma-Aldrich Company Ltd	Supplier	0800 717181	BH12 4QH	http://www.sigmaaldrich.com/	14027
Neuss	Germany	biochem@wako-chemicals.de	49 2131 311100	Wako	Supplier	49 2131 3110	D-41468	http://www.wako-chemicals.de/framebio.htm	14028
Cologne	Germany	oligo-eu@operon.com	\N	Operon Biotechnologies GmbH	Supplier	49 221 170 90 270	50829	http://www.operon.com	14029
London	UK	support@mwgdna.com	\N	MWG Biotech	Supplier	0800 032 313 5	WC2E 9RZ	http://www.mwg-biotech.com	14030
\N	USA	orders@clontech.com	800 424 1350	Clontech	Supplier	800 662 2566	CA 94043	http://www.clontech.com	14031
\N	Belgium	euro-orders@activemotif.com	32 (0)2 653 0050	ActiveMotif	Supplier	0800/169 31 47	\N	http://www.activemotif.com	14032
\N	USA	� websales@agencourt.com	978-867-2601	Agencourt Bioscience Corporation	Supplier	800-361-7780	MA 1915	http://www.agencourt.com	14033
Teddington	UK	uksales@lgcstandards.com	+44 (0)20 8943 7554	ATCC	Supplier	+44 (0)20 8943 8480	TW11 0LY	http://www.lgcstandards-atcc.org/Home/tabid/477/Default.aspx	14034
\N	\N	\N	\N	Invitrogen	\N	\N	\N	\N	36014
\N	\N	\N	\N	OPPF	\N	\N	\N	\N	36055
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
34028	Induction Batch	0
34028	AutoInduction Batch	1
34028	Fed Batch	2
34028	Optimization	3
34037	Yes	0
34037	No	1
34041	Yes	0
34041	No	1
34041	Returned	2
\.


--
-- Data for Name: prot_parameterdefinition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_parameterdefinition (defaultvalue, displayunit, isgrouplevel, ismandatory, isresult, label, "maxvalue", "minvalue", name, paramtype, unit, labbookentryid, protocolid, order_) FROM stdin;
\N	\N	f	t	f	\N	\N	\N	Forward Tag	String	\N	32005	32001	0
\N	\N	f	t	f	\N	\N	\N	Reverse Tag	String	\N	32006	32001	1
Reverse Phase	\N	f	t	f	\N	\N	\N	Purification type	String	\N	32011	32007	0
25 nmole	\N	f	t	f	\N	\N	\N	Starting Synthesis Scale	String	\N	32012	32007	1
TE	\N	f	t	f	\N	\N	\N	BufferType	String	\N	32013	32007	2
Concentration	\N	f	t	f	\N	\N	\N	Normalization	String	\N	32014	32007	3
AvrII	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	32015	32007	4
0.0	\N	f	t	f	\N	\N	\N	Tm seller	Float	\N	32016	32007	5
0.0	\N	f	t	f	\N	\N	\N	Tm full	Float	\N	32017	32007	6
0.0	\N	f	t	f	\N	\N	\N	Tm gene	Float	\N	32018	32007	7
400	\N	f	t	f	\N	\N	\N	volumn in nL	Float	\N	32019	32007	8
0.0	\N	f	t	f	\N	\N	\N	OD	Float	\N	32020	32007	9
0	\N	f	t	f	\N	\N	\N	Length on the gene	Int	\N	32021	32007	10
\N	\N	f	t	f	\N	\N	\N	Forward Primer Layout Notes	String	\N	32025	32022	0
\N	\N	f	t	f	\N	\N	\N	Reverse Primer Layout Notes	String	\N	32029	32026	0
5	\N	f	t	f	\N	\N	\N	Reaction volume/uL	Float	\N	32035	32030	0
\N	\N	f	t	f	\N	\N	\N	Infusion Notes	String	\N	32036	32030	1
\N	\N	f	t	f	\N	\N	\N	Volume of cells plated out	Float	\N	32043	32037	0
Incubate 30 min on ice,   Heat shock 30 sec,   Incubate a further 2 min on ice	\N	f	t	f	\N	\N	\N	DNA Incubation conditions	String	\N	32044	32037	1
42	\N	f	t	f	\N	\N	\N	Heat shock temperature	Float	\N	32045	32037	2
Incubate 60 min at 37C	\N	f	t	f	\N	\N	\N	Culture conditions	String	\N	32046	32037	3
true	\N	f	t	f	\N	\N	\N	Is the culture shaken?	Boolean	\N	32047	32037	4
\N	\N	f	t	f	\N	\N	\N	Transformation Result	Int	\N	32048	32037	5
\N	\N	f	t	f	\N	\N	\N	Transformation Notes	String	\N	32049	32037	6
\N	\N	f	t	f	\N	\N	\N	Marker	String	\N	34005	34001	0
\N	\N	f	t	f	\N	\N	\N	Restriction sites	String	\N	34006	34001	1
\N	\N	f	t	f	\N	\N	\N	Organism	String	\N	34010	34007	0
\N	\N	f	t	f	\N	\N	\N	Batch	String	\N	34027	34011	0
AutoInduction Batch	\N	f	t	f	\N	\N	\N	Culture Type	String	\N	34028	34011	1
10	\N	f	t	f	\N	\N	\N	Culture Volume	Int	\N	34029	34011	2
\N	\N	f	t	f	\N	\N	\N	Template Name	String	\N	34032	34030	0
\N	\N	f	t	f	\N	\N	\N	Template Concentration	Float	\N	34033	34030	1
\N	\N	f	t	f	\N	\N	\N	Template Volume	Float	\N	34034	34030	2
\N	\N	f	t	f	\N	\N	\N	Template Length	Int	\N	34035	34030	3
\N	\N	f	t	f	\N	\N	\N	Required Read Length	Int	\N	34036	34030	4
No	\N	f	t	f	\N	\N	\N	CD Provided?	String	\N	34037	34030	5
\N	\N	f	t	f	\N	\N	\N	Primer Name	String	\N	34038	34030	6
\N	\N	f	t	f	\N	\N	\N	Primer Volume	Float	\N	34039	34030	7
\N	\N	f	t	f	\N	\N	\N	Primer Concentration	Float	\N	34040	34030	8
No	\N	f	t	f	\N	\N	\N	Return Samples?	String	\N	34041	34030	9
\N	\N	f	t	f	\N	\N	\N	Account Number	String	\N	34042	34030	10
\N	\N	f	t	f	\N	\N	\N	Principal Investigator	String	\N	34043	34030	11
\N	\N	f	t	f	\N	\N	\N	User	String	\N	34044	34030	12
\N	\N	f	t	f	\N	\N	\N	User Phone	String	\N	34045	34030	13
\N	\N	f	t	f	\N	\N	\N	User Email	String	\N	34046	34030	14
\N	\N	f	t	f	\N	\N	\N	Department	String	\N	34047	34030	15
\N	\N	f	t	f	\N	\N	\N	Order ID	String	\N	34048	34030	16
\.


--
-- Data for Name: prot_protocol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol (isforuse, methoddescription, name, objective, labbookentryid, experimenttypeid) FROM stdin;
\N	\N	SPOT Construct Primer Design	\N	32001	1
\N	\N	Primer Order Plate	Order a plate of primer for targets	32007	71
\N	\N	PIMS Plate Layout - Forward Primers	Design Plate Layout	32022	57
\N	\N	PIMS Plate Layout - Reverse Primers	Design Plate Layout	32026	57
\N	\N	PIMS Bicistronic InFusion	Capture PCR Product into pOPIN vectors	32030	5
\N	\N	PIMS Bitrans Transformation	Transform E.coli with plasmid, generic heat shock protocol	32037	10
\N	\N	Entry clone	Gateway Entry Clone design	34001	2
\N	\N	Deep-Frozen culture	Record Deep-Frozen culture stock	34007	3
\N	\N	Generic fermenter culture	Recording fermentation batches	34011	72
\N	\N	Sequencing order	Supply samples and other required details for sequencing	34030	74
\N	\N	Leeds sequencing setup	Prepare sample for sequencing on the robot	34049	75
\N	\N	CrystalTrial	Do crystal trial	38021	36
\.


--
-- Data for Name: prot_protocol_remarks; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_protocol_remarks (protocolid, remark, order_) FROM stdin;
32007	created by PIMS	0
32022	Based on Lab activities requirements from OPPF	0
32026	Based on Lab activities requirements from OPPF	0
32030	Based on Lab activities requirements from OPPF	0
32037	Based on Lab activities requirements from OPPF	0
34001	Used by construct management	0
34007	Used by construct management	0
34011	Used by specific UI for recording Leeds fermentation batches	0
34030	Used by specific UI for sequencing	0
34049	Used by specific UI for sequencing	0
\.


--
-- Data for Name: prot_refinputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refinputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
0	uL	Forward Primer	L	32009	32007	10020
0	uL	Reverse Primer	L	32010	32007	10043
3.0000000000000001e-006	uL	Forward Primer	L	32024	32022	10037
3.0000000000000001e-006	uL	Reverse Primer	L	32028	32026	10037
1.9999999999999999e-006	uL	PCR Product A	L	32032	32030	10032
1.9999999999999999e-006	uL	PCR Product B	L	32033	32030	10032
9.9999999999999995e-007	uL	Vector	L	32034	32030	10053
5.0000000000000002e-005	uL	Competent cells	L	32039	32037	10011
1.9999999999999999e-006	uL	Expression Vector A	L	32040	32037	10036
1.9999999999999999e-006	uL	Expression Vector B	L	32041	32037	10036
0.00029999999999999997	uL	Culture media	L	32042	32037	10012
\N	uL	Reverse Primer	L	34002	34001	10043
\N	uL	Forward primer	L	34003	34001	10020
\N	uL	Plasmid	L	34004	34001	10036
\N	uL	Entry Clone	L	34008	34007	10036
\N	uL	Strain	L	34009	34007	10051
0	uL	Construct	L	34022	34011	10036
0	uL	Antibiotic A	L	34023	34011	10002
0	uL	Antibiotic B	L	34024	34011	10002
0	uL	Antibiotic C	L	34025	34011	10002
0	mL	Culture	L	34026	34011	10056
2e-008	uL	Template	L	34051	34049	10032
2e-008	uL	Primer	L	34052	34049	10037
2e-008	uL	Water	L	34053	34049	10049
2e-008	uL	Premix	L	34054	34049	10057
9.9999999999999995e-008	nL	Purified protein	L	38023	38021	10040
\.


--
-- Data for Name: prot_refoutputsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY prot_refoutputsample (amount, displayunit, name, unit, labbookentryid, protocolid, samplecategoryid) FROM stdin;
0	uL	Forward Primer	L	32002	32001	10020
0	uL	Reverse Primer	L	32003	32001	10043
0	uL	Template	L	32004	32001	10032
\N	uL	Primer	L	32008	32007	10037
3.0000000000000001e-006	uL	Forward Primer	L	32023	32022	10020
3.0000000000000001e-006	uL	Reverse Primer	L	32027	32026	10043
\N	uL	Vector	L	32031	32030	10036
\N	mL	Cells	L	32038	32037	10056
0.050000000000000003	g	Tube 1	kg	34012	34011	10056
0.050000000000000003	g	Tube 2	kg	34013	34011	10056
0.050000000000000003	g	Tube 3	kg	34014	34011	10056
0.050000000000000003	g	Tube 4	kg	34015	34011	10056
0.050000000000000003	g	Tube 5	kg	34016	34011	10056
0.050000000000000003	g	Tube 6	kg	34017	34011	10056
0.050000000000000003	g	Tube 7	kg	34018	34011	10056
0.050000000000000003	g	Tube 8	kg	34019	34011	10056
0.050000000000000003	g	Tube 9	kg	34020	34011	10056
0.050000000000000003	g	Tube 10	kg	34021	34011	10056
\N	uL	DNA	L	34031	34030	10032
2e-008	uL	DNA	L	34050	34049	10032
9.9999999999999995e-008	nL	Trial Drop	L	38022	38021	38005
\.


--
-- Data for Name: ref_abstractholdertype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_abstractholdertype (name, publicentryid) FROM stdin;
CrystalQuick plate, round wells	26001
CrystalQuick plate, square wells	26002
CrystalQuick plate, low profile plate	26003
96 well IMP@CT plate for Protein crystallisation	26004
CrystalQuick plate, square wells, Low Birefringence	26005
Cellstar 24 well plate	26006
Cryschem plate	26007
96 well PCR plate half skirted	26008
Thermo-Fast 96 skirted	26009
0.1ml 96-well, thin wall plates (lower profile, skirted)	26010
48 well 6.0ml storage plate	26011
Falcon Multiwell 24 well plate	26012
CombiClover plate	26013
CompactClover plate	26014
CombiClover Junior plate	26015
96 round bottom (Crystal Ex)	26016
96 well rd-bottom	26017
24-well comboplate 	26018
Intelliplate	26019
96 well hanging drop	26020
24-well vdxm	26021
48 well vdx	26022
Linbro	26023
Crystal Clear Strips D/1	26024
Crystal Clear Strips P/1	26025
96 deep well	26026
card	26027
\.


--
-- Data for Name: ref_componentcategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_componentcategory (name, publicentryid) FROM stdin;
Buffering agent	12001
Chelator	12002
Cryo coolant	12003
CSI	12004
Detergent	12005
Gas	12006
Heavy atom compound	12007
Metal	12008
Nucleation suppressant	12009
Organic	12010
PCR Product	12011
pH conjugate	12012
Precipitant	12013
Reducing agent	12014
Oxidising agent	12015
Salt	12016
Solvent	12017
Vector	12018
Construct	12019
Plasmid	12020
Competent cell	12021
Primer	12022
Inducer	12023
Inhibitor	12024
Additive	12025
Antibiotic	12026
Catalyst	12027
Co-factor	12028
Cross linker	12029
Ligand	12030
Lipid	12031
Modifying agent	12032
Substrate	12033
Enzyme	12034
Nucleotide	12035
Preservative	12036
Sugar	12037
StainsAndDyes	12038
Denaturant	12039
Oligonucleotide	12040
Amino acid	12041
Vitamin	12042
Final Protein	12043
Expressed Protein	12044
Template	12045
Protein	12046
Nucleic acid	12047
Forward Extension	12048
Reverse Extension	12049
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
Clusters of orthologous groups of proteins	COG	http://www.ncbi.nlm.nih.gov/COG 	20002
DDBJ-DNA Data Bank of Japan	DDBJ	http://www.ddbj.nig.ac.jp 	20003
Genomes at the EBI	EBI Genomes	http://www.ebi.ac.uk/genomes 	20004
Eukaryote Gene Orthologs	EGO	http://www.tigr.org/tdb/tgi/ego/ 	20005
EMBL-Nucleotide Sequence Database	EMBL-Bank	http://www.ebi.ac.uk/embl.html	20006
EMBL Coding sequences	EMBLCDS	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-id+4Flds1F1PA4+-lib+EMBLCDS	20007
EMBL Contig database	EMBLCON	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-id+4Flds1F1PA4+-lib+EMBLCON	20008
NCBI gene-specific information database	Entrez Gene	http://www.ncbi.nih.gov/entrez/query.fcgi?db=gene	20009
NCBI Entrez Genome Project database	Entrez Genomes	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Genome	20010
Entrez Protein database	Entrez-Protein	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Protein	20011
EXProt (database for EXPerimentally verified Protein functions)	EXProt	http://www.cmbi.kun.nl/EXProt/ 	20012
Entrez Nucleotides database	GenBank	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Nucleotide	20013
Gene ontology consortium database	GO	http://www.geneontology.org/ 	20014
Gene Ontology Annotation project	GOA	http://www.ebi.ac.uk/GOA 	20015
Human Genome Variation Database	HGVBase	http://hgvbase.cgb.ki.se/	20016
Integrated resource of Protein Families, Domains and Sites	InterPro	http://www.ebi.ac.uk/interpro/	20017
International Protein Index	IPI	http://www.ebi.ac.uk/IPI/IPIhelp.html	20018
International Union of Biochemistry and Molecular Biology Nomenclature database	IUBMB Nomenclature database	http://www.chem.qmul.ac.uk/iubmb 	20019
International Union of Pure and Applied Chemistry Nomenclature database	IUPAC Nomenclature database	http://www.chem.qmul.ac.uk/iupac 	20020
Kyoto encyclopedia of genes and genomes	KEGG	http://www.genome.jp/kegg 	20021
Molecular Modeling Database (MMDB)	MMDB	http://www.ncbi.nlm.nih.gov/Structure 	20022
EBI Macromolecular Structure Database (EMSD)	MSD	http://www.ebi.ac.uk/msd 	20023
NCBI Taxonomy database	NCBI Taxonomy	http://www.ncbi.nlm.nih.gov/Taxonomy/ 	20024
The PANTHER (Protein ANalysis THrough Evolutionary Relationships) Classification System	PANTHER	http://panther.celera.com/ 	20025
European Patent Abstracts	Patent Abstracts	http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-page+LibInfo+-lib+PATABS	20026
RCSB ProteinData Bank	PDB	http://www.rcsb.org/pdb 	20027
PDB-Ligand database	PDB-Ligand	http://www.idrtech.com/PDB-Ligand/ 	20028
Protein Data Bank of Transmembrane Proteins	PDBTM	http://pdbtm.enzim.hu/	20029
Protein Extraction, Description and ANalysis Tool	PEDANT	http://pedant.gsf.de/ 	20030
PubMed	PubMed	http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=PubMed	20031
NCBI Reference Sequences	RefSeq	http://ncbi.nih.gov/RefSeq/	20032
Structural classification of protein database	SCOP	http://scop.mrc-lmb.cam.ac.uk/scop 	20033
Superfamily HMM library and genome assignments server	SUPERFAMILY	http://supfam.org/ 	20034
Swiss-Prot protein knowledgebase	Swiss-Prot	http://www.expasy.org/sprot 	20035
Target registration database	TargetDB	http://targetdb.pdb.org/ 	20036
Transport Classification database	TCDB	http://www.tcdb.org/	20037
The Institute for Genomic Research Comprehensive Microbial Resource	TIGR CMR	http://cmr.tigr.org/tigr-scripts/CMR/CmrHomePage.cgi	20038
The Institute for Genomic Research Gene Indices	TIGR GI	http://www.tigr.org/tdb/tgi.shtml 	20039
The Institute for Genomic Research Microbial resource	TIGR MDB	http://www.tigr.org/tdb/mdb/mdbcomplete.html 	20040
Topology of protein structures database	TOPS	http://www.tops.leeds.ac.uk 	20041
Genomic Comparisons of Membrane transport Systems	TransportDB	http://www.membranetransport.org/ 	20042
UniProt Archive	UniParc	http://www.ebi.uniprot.org/uniprot-srv/uniParcSearch.do?pager.offset=	20043
The Universal Protein Resource	UniProt	http://www.uniprot.org/ 	20044
UniProt Knowledgebase	UniProtKB	http://www.ebi.uniprot.org/uniprot-srv/index.do	20045
UniProt Reference Clusters	UniRef	http://www.ebi.uniprot.org/uniprot-srv/uniRefSearch.do	20046
UniVec database	UniVec	http://www.ncbi.nlm.nih.gov/VecScreen/UniVec.html 	20047
Vector database	VectorDB	http://genome-www2.stanford.edu/vectordb/ 	20048
unspecified	unspecified	\N	20049
\.


--
-- Data for Name: ref_experimenttype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_experimenttype (name, publicentryid) FROM stdin;
SPOTConstructDesign	1
Entry clone	2
Deep-Frozen culture	3
PCR	4
Cloning	5
Digest	6
Recombination	7
LIC annealing	8
LIC polymerase reaction	9
Transformation	10
Ligation	11
Cloning design	12
Primer design	13
Optic Construct Design	14
Expression	15
Small scale expression	16
Production scale expression	17
Purification	18
DNA purification	19
Mini prep	20
Plasmid prep	21
Protein purification	22
Chromatography	23
Proteolysis	24
Refold	25
Dialysis	26
Concentration	27
Fractionation	28
Lysis	29
Protein production summary	30
Membrane preparation	31
Crystallogenesis	32
Drop set-up	33
Native trials	34
Selmet trials	35
Trials	36
Drop scoring	37
Drop imaging	38
Native crystal	39
Selmet crystal	40
Crystal	41
Native optimization	42
Selmet optimization	43
Mount crystal	44
Crystallization plate inspection	45
Crystallography	46
Diffraction	47
Native diffraction	48
Selmet diffraction	49
Data set	50
Crystal Structure	51
NMR	52
Preparative experiment	53
Buffer prep	54
Solution prep	55
Culture	56
Import sample	57
Fermentation	58
Characterisation	59
Gel	60
OD measurement	61
Light scattering	62
Mass spec	63
Dish culture	64
Other	65
Annotation	66
Information	67
PDB	68
Plate check	69
Unspecified	70
Order	71
Fermenter culture	72
Cleavage	73
Sequencing order	74
Sequencing	75
Complexation	76
Cell stock	77
Plasmid stock	78
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
96 well plate	24001
48 well plate	24002
24 well plate	24003
48 deep well plate	24004
96 deep well plate	24005
card	24006
Box	24007
Clone saver card	24008
Screen	38006
Additive	38007
Optimization	38008
SparseMatrix	38009
TrialPlate	38010
\.


--
-- Data for Name: ref_holdertype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_holdertype (holdholderflag, maxcolumn, maxrow, maxsubposition, maxvolume, maxvolumedisplayunit, abstractholdertypeid, defaultscheduleplanid) FROM stdin;
\N	12	8	3	320	uL	26001	\N
\N	12	8	3	320	uL	26002	\N
\N	12	8	1	320	uL	26003	\N
\N	12	8	0	10	uL	26004	\N
\N	12	8	3	320	uL	26005	\N
\N	6	4	0	1.5	mL	26006	\N
\N	6	4	1	1.5	mL	26007	\N
\N	12	8	0	200	uL	26008	\N
\N	12	8	0	200	uL	26009	\N
\N	12	8	0	100	uL	26010	\N
\N	6	8	0	6	mL	26011	\N
\N	6	4	0	3.5	mL	26012	\N
\N	6	4	4	1.5	mL	26013	\N
\N	12	8	4	250	uL	26014	\N
\N	6	4	4	0.5	mL	26015	\N
\N	12	8	1	210	uL	26016	\N
\N	12	8	0	360	uL	26017	\N
\N	6	4	0	3.2999999999999998	mL	26018	\N
\N	12	8	2	300	uL	26019	\N
\N	12	8	0	\N	\N	26020	\N
\N	6	4	0	0.5	mL	26021	\N
\N	8	6	0	0.29999999999999999	mL	26022	\N
\N	6	4	0	1	mL	26023	\N
\N	12	8	1	100	uL	26024	\N
\N	12	8	1	100	uL	26025	\N
\N	12	8	0	2	mL	26026	\N
\N	10	10	0	100	uL	26027	\N
\.


--
-- Data for Name: ref_holdertypesource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_holdertypesource (catalognum, datapageurl, productname, publicentryid, holdertypeid, supplierid) FROM stdin;
\.


--
-- Data for Name: ref_imagetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_imagetype (catorgory, colourdepth, name, sizex, sizey, url, xlengthperpixel, ylengthperpixel, publicentryid) FROM stdin;
composite	8	BioStore default type	1280	960	http://localhost:8080/xtalpims/images/OutputJPGImages2	2.375	2.323	38001
zoomed	256	Microscope default type	1280	960	http://www.oppf.ox.ac.uk/vault/images/lowres/	2.9790000000000001	2.9790000000000001	38002
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
\N	\N	\N	\N	\N	\N	\N	\N	Pseudomonas syringae pv. phaseolicola	319	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6002
\N	\N	\N	\N	\N	\N	\N	\N	Sphingomonas paucimobilis	13689	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6003
\N	\N	\N	\N	\N	\N	\N	\N	Bacteroides fragilis	817	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6004
\N	\N	\N	\N	\N	\N	\N	\N	Streptomyces coelicolor	1902	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6005
\N	\N	\N	\N	\N	\N	\N	\N	Escherichia coli	562	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6006
\N	\N	\N	\N	\N	\N	\N	\N	Bordetella bronchiseptica	518	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6007
\N	\N	\N	\N	\N	\N	\N	\N	Sulfolobus islandicus rudivirus 1 variant XX	282066	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6008
\N	\N	\N	\N	\N	\N	\N	\N	Pectobacterium chrysanthemi	556	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6009
\N	\N	\N	\N	\N	\N	\N	\N	Bacillus anthracis	1392	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6010
\N	\N	\N	\N	\N	\N	\N	\N	Geobacter sulfurreducens PCA	243231	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6011
\N	\N	\N	\N	\N	\N	\N	\N	Staphylococcus epidermidis	1282	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6012
\N	\N	\N	\N	\N	\N	\N	\N	Sulfolobus solfataricus	2287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6013
\N	\N	\N	\N	\N	\N	\N	\N	Streptomyces cattleya	29303	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6014
\N	\N	\N	\N	\N	\N	\N	\N	Arabidopsis thaliana	3702	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6015
\N	\N	\N	\N	\N	\N	\N	\N	Caenorhabditis elegans	6239	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6016
\N	\N	\N	\N	\N	\N	\N	\N	Danio rerio	7955	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6017
\N	\N	\N	\N	\N	\N	\N	\N	Dictyostelium discoideum	44689	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6018
\N	\N	\N	\N	\N	\N	\N	\N	Drosophila melanogaster	7227	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6019
\N	\N	\N	\N	\N	\N	\N	\N	Homo sapiens	9606	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6020
\N	\N	\N	\N	\N	\N	\N	\N	Mus musculus	10090	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6021
\N	\N	\N	\N	\N	\N	\N	\N	Mycoplasma pneumoniae	2104	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6022
\N	\N	\N	\N	\N	\N	\N	\N	Mycobacterium tuberculosis H37Rv	83332	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6023
\N	\N	\N	\N	\N	\N	\N	\N	Oryza sativa	4530	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6024
\N	\N	\N	\N	\N	\N	\N	\N	Plasmodium falciparum	5833	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6025
\N	\N	\N	\N	\N	\N	\N	\N	Pongo pygmaeus	9600	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6026
\N	\N	\N	\N	\N	\N	\N	\N	Pseudomonas aeruginosa	287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6027
\N	\N	\N	\N	\N	\N	\N	\N	Rattus norvegicus	10116	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6028
\N	\N	\N	\N	\N	\N	\N	\N	Saccharomyces cerevisiae	4932	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6029
\N	\N	\N	\N	\N	\N	\N	\N	Salmonella typhimurium LT2	99287	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6030
\N	\N	\N	\N	\N	\N	\N	\N	Schizosaccharomyces pombe	4896	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6031
\N	\N	\N	\N	\N	\N	\N	\N	Staphylococcus aureus	1280	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6032
\N	\N	\N	\N	\N	\N	\N	\N	Thermotoga maritima	2336	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6033
\N	\N	\N	\N	\N	\N	\N	\N	Xenopus laevis	8355	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6034
\N	\N	\N	\N	\N	\N	\N	\N	Zea mays	4577	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6035
\N	\N	\N	\N	\N	\N	\N	\N	Bacillus subtilis	1423	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6036
\N	\N	\N	\N	\N	\N	\N	\N	Nematostella vectensis	45351	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6037
\N	\N	\N	\N	\N	\N	\N	\N	Neisseria gonorrhoeae	 485	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	6038
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
2	Gateway Entry clone design
3	Deep-Frozen culture stock recording
4	Polymerase chain reaction
5	Replication of DNA in a foreign host
6	Restriction enzyme digest of DNA
7	Recombinational cloning e.g. Gateway&reg; technology
8	Ligation-independent cloning - annealing reaction
9	Ligation-independent cloning - polymerase reaction
10	Introduction of DNA into cells
11	Enzymatic joining of DNA fragments
12	\N
13	\N
14	\N
15	Production of a recombinant protein from a cloned gene
16	Small scale expression e.g. trials
17	Large scale expression
18	\N
19	Purify DNA of interest
20	Small scale purification of plasmid DNA
21	Purification of plasmid DNA
22	Purify protein of interest
23	Chromatographic separation techniques e.g. HPLC, TLC, Gel filtration 
24	Enzymatic digestion of proteins
25	Enabling a denatured or unfolded protein to adopt its native conformation 
26	Buffer exchange through a membrane
27	Reduce the volume of a solution
28	Separation of a mixture into its component parts
29	Cell disruption
30	Summary of expression and purification details
31	Preparation of cell membranes
32	Crystallization experiment
33	Set up a crystallization experiment
34	Crystallization trials with native protein
35	Crystallization trials with Seleneomethionine-labelled protein
36	Crystallization trials
37	User-defined score from a crystallization experiment
38	Imaging of a crystallization experiment
39	Crystallization experiment with native protein
40	Crystallization experiment with Seleneomethionine-labelled protein
41	Record crystallization success
42	Crystallization optimization with native protein
43	Crystallization optimization with Seleneomethionine-labelled protein
44	Crystal mounting e.g. in a loop or capllary
45	Viewing a crystallization plate
46	\N
47	Obtain diffraction data for crystal
48	Obtain diffraction data for native protein crystal
49	Obtain diffraction data for Seleneomethionine-labelled protein crystal
50	Obtain diffraction data set
51	Obtain crystal structure
52	\N
53	\N
54	Preparation of a buffer
55	Preparation of a solution
56	Growth of cells in liquid media
57	Import of a reagent or sample into PiMS
58	Large scale culture under controlled conditions
59	Protein characterisation e.g. SDS-PAGE, DLS, CD, etc.
60	Gel Electrophoresis e.g. SDS-PAGE, agarose gel
61	Optical density or absorbance measurement
62	Dynamic light scattering, DLS
63	Mass spectrometry analysis
64	Growth of cells on a solid support, e.g. agar plate
65	\N
66	\N
67	\N
68	Record deposition of crystal coordinates in PDB
69	Test of transformation by growth on solid support
70	Unspecified experiment type
71	Order samples
72	Fermentation experiments
73	\N
74	\N
75	\N
76	Mixing of protein samples to form a complex
77	Recording a cell stock
78	Recording a plasmid stock
2001	PIMS default target status
2002	PIMS default target status
2003	PIMS default target status
2004	PIMS default target status
2005	PIMS default target status
2006	PIMS default target status
2007	PIMS default target status
2008	PIMS default target status
2009	PIMS default target status
2010	PIMS default target status
2011	PIMS default target status
2012	PIMS default target status
2013	PIMS default target status
2014	PIMS default target status
2015	PIMS default target status
2016	PIMS default target status
2017	PIMS default target status
2018	PIMS default target status
2019	PIMS default target status
2020	PIMS default target status
2021	PIMS default target status
2022	PIMS default target status
2023	PIMS default target status
2024	PIMS default target status
2025	PIMS default target status
2026	PIMS default target status
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
6002	\N
6003	\N
6004	\N
6005	\N
6006	\N
6007	\N
6008	\N
6009	\N
6010	\N
6011	\N
6012	\N
6013	\N
6014	\N
6015	\N
6016	\N
6017	\N
6018	\N
6019	\N
6020	\N
6021	\N
6022	\N
6023	\N
6024	\N
6025	\N
6026	\N
6027	\N
6028	\N
6029	\N
6030	\N
6031	\N
6032	\N
6033	\N
6034	\N
6035	\N
6036	\N
6037	\N
6038	\N
10001	Compound with both hydrophilic and hydrophobic groups. e.g. sufactant, detergent, phospholipid
10002	Substance which inhibits the growth of a micro-organism
10003	Immunoglobulin with defined specificity
10004	System which can neutalise effects of addition of acids and bases
10005	The weak acid or weak base that would comprise a Buffer or Buffer Solution
10006	Untransformed or native cell or cells: Bacterial, Yeast, Eukaryotic, etc.
10007	A Chemical or Biochemical
10008	Reagent for use in chromatography experiments
10009	Commercial kit for cloning experiments
10010	\N
10011	Cell or cells capable of DNA uptake
10012	Liquid or solid substance used for the growth of cells
10013	Substance with polar (water soluble) and non-polar(hydrophobic) domains
10014	Reagent for use in electrophoresis experiments
10015	Restriction enzyme
10016	Modifying enzyme e.g. kinase, phosphatase, etc.
10017	Polymerase enzyme e.g. Taq, Pfu, Reverse transcriptase, etc. 
10018	Ligase enzyme
10019	 Protease enzyme e.g. TEV
10020	Used in combination with a Reverse Primer in PCR
10021	Protein which stimulates cellular proliferation
10022	Reagent used for preparing heavy-atom derivatives of protein crystals
10023	Substance which regulates celluar functions
10024	Substance which inhibits the activity of a molecule
10025	\N
10026	Hydrocarbon containing organic compound, insoluble in water
10027	Size standards used in elctrophoreis
10028	\N
10029	Latex beads, magnetic beads etc.
10030	Reagents for use in microscopy
10031	\N
10032	DNA, RNA etc.
10033	Commercial kit for nucleic acid purification"
10034	Commercial kit for modifying nucleic acids"
10035	Nucleotide compounds and solutions e.g.dATP, NADP, etc.
10036	A recombinant or insert-containing vector
10037	e.g. PCR-primer
10038	Drugs and pharmaceuticals
10039	Amino acid polymer
10041	Chemical prepared using a radioactive element
10042	Cells with Target
10043	Used in combination with a Forward Primer in PCR
10044	PIMS default category
10045	Crystallization screen
10046	\N
10047	\N
10048	Solution
10049	Solvent
10050	Stains and Dyes
10051	A strain of an organism
10052	Substance acted on by an enzyme
10053	Expression vector, cloning vector, etc.
10054	Vitamins and Derivatives
10055	Sample which contains the Target protein
10056	Cell or cells which have been modified to contain foreign DNA such as a plasmid: e.g. E.coli transformed with pDEST17
10057	Commercial kit
10058	Affinity Resin
10059	Standard Primer for Sequencing 
12001	PIMS default category
12002	PIMS default category
12003	PIMS default category
12004	PIMS default category
12005	PIMS default category
12006	PIMS default category
12007	PIMS default category
12008	PIMS default category
12009	PIMS default category
12010	PIMS default category
12011	PIMS default category
12012	PIMS default category
12013	PIMS default category
12014	PIMS default category
12015	PIMS default category
12016	PIMS default category
12017	PIMS default category
12018	PIMS default category
12019	PIMS default category
12020	PIMS default category
12021	PIMS default category
12022	PIMS default category
12023	PIMS default category
12024	PIMS default category
12025	PIMS default category
12026	PIMS default category
12027	PIMS default category
12028	PIMS default category
12029	PIMS default category
12030	PIMS default category
12031	PIMS default category
12032	PIMS default category
12033	PIMS default category
12034	PIMS default category
12035	PIMS default category
12036	PIMS default category
12037	PIMS default category
12038	PIMS default category
12039	PIMS default category
12040	PIMS default category
12041	PIMS default category
12042	PIMS default category
12043	PIMS default category
12044	PIMS default category
12045	PIMS default category
12046	PIMS default category
12047	PIMS default category
12048	PIMS default category
12049	PIMS default category
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
20002	Phylogenetic classification of proteins encoded in complete genomes
20003	All known nucleotide and protein sequences
20004	EBI's collection of databases of complete and unfinished viral , pro- and eukaryotic genomes
20005	TIGR database for orthologous genes in Eukaryotes
20006	Europe's primary nucleotide sequence resource
20007	Database of nucleotide sequences coding sequences
20008	Assembly information on all accession.versions and sequence locations for complete genomes and other long sequences (>350kb)
20009	A searchable database of genes, from RefSeq genomes.
20010	NCBI's searchable collection of complete and incomplete (in-progress) large-scale sequencing, assembly, annotation, and mapping projects for cellular organisms.
20011	All protein sequences: imported from SwissProt, PIR, PRF, PDB, and translations from annotated coding regions in GenBank and RefSeq
20012	Sequences of proteins with experimentally verified function
20013	Entrez nucleotide sequences from several sources, including GenBank, RefSeq, and PDB
20014	Gene annotation language database
20015	The EBI's Gene Ontology Annotation (GOA) project to provide assignments of GO terms to gene products for all organisms with completely sequenced genomes
20016	Catalog of normal human gene and genome variation, to be developed into a Phenotype/Genotype database
20017	A database of protein families, domains and functional sites, provides an integrated view of the commonly used protein signature databases such as PROSITE and Pfam.
20018	A database of annotated proteins for a limited number of higher eukaryotic species whose genomic sequence has been completely determined but where there are a large number of predicted protein sequences that are not yet in UniProt.
20019	Nomenclature of enzymes, membrane transporters, electron transport proteins and other proteins
20020	Nomenclature of biochemical and organic compounds approved by the IUBMB-IUPAC Joint Commission
20021	Integrated suite of databases on genes, proteins and metabolic pathways
20022	NCBI's database of macromolecular 3D structures, plus tools for their visualization and comparative analysis
20023	EBI's macromolecular structure database
20024	Names of all organisms that are represented in the genetic databases with at least one nucleotide or protein sequence
20025	Classification system for gene products by biological function
20026	A set of biotechnology-related abstracts of patent applications derived from data products of the European Patent Office (EPO).
20027	Protein structure databank: all publicly available 3D structures of proteins and nucleic acids
20028	3D structure database of small molecular ligands bound to larger biolomocules deposited in the Protein Data Bank (PDB).
20029	Transmembrane proteins from the PDB
20030	Pre-computed analyses of genomic sequences
20031	Database of citations and abstracts to biomedical and other life science journal literature.
20032	The RefSeq collection aims to provide a comprehensive, integrated, non-redundant set of sequences, including genomic DNA, transcript (RNA), and protein products, for major research organisms.
20033	Structural classification of all proteins of known structure according to their evolutionary, functional and structural relationships.
20034	Assignments of proteins to 1539 SCOP structural superfamilies
20035	Now UniProt/Swiss-Prot: expertly curated protein sequence database, section of the UniProt knowledgebase
20036	Target data from worldwide structural genomics projects, provides status and tracking information on the progress of the production and solutions of structures
20037	Transport Classification database for membrane transport proteins
20038	Information on all of the publicly available, complete prokaryotic genomes.
20039	Organism-specific databases of publicly available EST and gene sequences and analyses
20040	Alphabetical listing of published TIGR Microbial genomes
20041	Database of topological descriptions of protein structures
20042	Database describing the predicted cytoplasmic membrane transport protein complement for organisms whose complete genome sequence is available
20043	A comprehensive repository, reflecting the history of all protein sequences.
20044	Repository of protein sequence and function created by joining the information contained in Swiss-Prot, TrEMBL, and PIR
20045	The central access point for extensive curated protein information, including function, classification, and cross-reference
20046	Non-redundant Reference database combines closely related sequences into a single record to speed searches
20047	Vector sequences, adapters, linkers and primers used in DNA cloning, and tools to check for vector contamination
20048	Sequences of cloning vectors
20049	Unspecified database
24001	PIMS default category
24002	PIMS default category
24003	PIMS default category
24004	PIMS default category
24005	PIMS default category
24006	PIMS default category
24007	PIMS default category
24008	PIMS default category
26001	96 well plate
26002	96 well plate
26003	96 well plate
26004	96 well plate
26005	96 well plate
26006	24-well plate
26007	24-well plate
26008	96 Well PCR plate Polystyrene Structure , Flat bottomed , Clear , Sterile , With Lid. Half skirted, FOR ABI
26009	96 Well PCR plate, Low profile, natural
26010	skirted low profile plate, for mixing small samples
26011	for small scale growth
26012	transparent, flat bottomed
26013	24 well plate
26014	96 well plate
26015	24 well plate
26016	96 well plate
26017	96 well plate
26018	24 well plate
26019	96 well plate
26020	97 well plate
26021	24 well plate
26022	48 well plate
26023	24 well plate
26024	96 well plate
26025	96 well plate
26026	for solution making
26027	Clone saver card
38001	\N
38002	\N
10040	The purified protein which will be crystallized in trialplate
38005	The sample may contains crystal
38006	PIMS default category for screen
38007	Sub category of screen
38008	Sub category of screen
38009	Sub category of screen
38010	PIMS default category for Crystal Trial
\.


--
-- Data for Name: ref_samplecategory; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_samplecategory (name, publicentryid) FROM stdin;
Amphiphile	10001
Antibiotic	10002
Antibody	10003
Buffer	10004
Buffering Agent	10005
Cell Line	10006
Chemical/Biochemical	10007
Chromatography	10008
Cloning kit	10009
Crystal	10010
Competent cells	10011
Culture media	10012
Detergent	10013
Electrophoresis	10014
Enzyme -restriction	10015
Enzyme -modifying	10016
Enzyme -polymerase	10017
Enzyme -ligase	10018
Enzyme -protease	10019
Forward Primer	10020
Growth factor	10021
Heavy atom reagent	10022
Hormone	10023
Inhibitor	10024
InSoluble Protein Sample	10025
Lipid	10026
Marker	10027
Membrane	10028
Microparticle	10029
Microscopy	10030
Mounted Crystal	10031
Nucleic acid	10032
Nucleic acid purification kit	10033
Nucleic acid modification kit	10034
Nucleotide	10035
Plasmid	10036
Primer	10037
Pharmacopeia	10038
Polyamino acid	10039
Purified protein	10040
Radiochemical	10041
Recombinant Cells	10042
Reverse Primer	10043
Screen	10044
Screen solution	10045
Solubilised Membrane	10046
Soluble Protein Sample	10047
Solution	10048
Solvent	10049
Stains and Dyes	10050
Strain	10051
Substrate	10052
Vector	10053
Vitamins and Derivatives	10054
Target -containing sample	10055
Transformed cells	10056
Kit	10057
Affinity Resin	10058
Standard Sequencing Primer	10059
TrialDrop	38005
\.


--
-- Data for Name: ref_targetstatus; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_targetstatus (name, publicentryid) FROM stdin;
Selected	2001
PCR	2002
Cloned	2003
Expressed	2004
Small Scale Expression	2005
Soluble	2006
Production Scale expression	2007
Purified	2008
In Crystallization	2009
Crystallized	2010
Diffraction Quality Crystals	2011
Native Diffraction Data	2012
Diffraction	2013
Phasing Diffraction Data	2014
HSQC	2015
NMR Assigned	2016
NMR NOE	2017
Crystal Structure	2018
NMR Structure	2019
In PDB	2020
In BMRB	2021
Molecular Function	2022
Biological Process	2023
Cellular Component	2024
Work Stopped	2025
Other	2026
\.


--
-- Data for Name: ref_workflowitem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ref_workflowitem (publicentryid, experimenttypeid, statusid) FROM stdin;
4001	4	2002
4002	5	2003
4003	6	2003
4004	7	2003
4005	8	2003
4006	9	2003
4007	10	2003
4008	11	2003
4009	19	2003
4010	20	2003
4011	21	2003
4012	15	2004
4013	16	2005
4014	17	2007
4015	18	2008
4016	19	2008
4017	20	2008
4018	22	2008
4019	23	2008
4020	24	2008
4021	25	2008
4022	26	2008
4023	27	2008
4024	28	2008
4025	29	2008
4026	30	2008
4027	32	2009
4028	33	2009
4029	34	2009
4030	35	2009
4031	36	2009
4032	37	2009
4033	38	2009
4034	42	2009
4035	43	2009
4036	45	2009
4037	39	2010
4038	40	2010
4039	41	2010
4040	44	2011
4041	46	2013
4042	47	2013
4043	48	2013
4044	49	2013
4045	50	2013
4046	51	2018
4047	53	2026
4048	54	2026
4049	55	2026
4050	56	2026
4051	57	2026
4052	58	2026
4053	59	2026
4054	60	2026
4055	61	2026
4056	62	2026
4057	63	2026
4058	64	2026
4059	69	2026
4060	65	2026
4061	66	2026
4062	67	2026
4063	31	2026
4064	68	2020
\.


--
-- Data for Name: revisionnumber; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY revisionnumber (revision, name, "release", tag, author, date) FROM stdin;
38	pims	3_2	pims_3_2_38	Anne	09/06/2009
\.


--
-- Data for Name: sam_abstractsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_abstractsample (ionicstrength, isactive, ishazard, name, ph, labbookentryid) FROM stdin;
\N	\N	\N	KOD DNA Polymerase	\N	16001
\N	\N	\N	KOD Hot Start DNA Polymerase	\N	16003
\N	\N	\N	KOD XL DNA Polymerase	\N	16005
\N	\N	\N	LIC qualfied T4 Polymerase	\N	16007
\N	\N	\N	Taq Polymerase	\N	16009
\N	\N	\N	VentR DNA Polymerase	\N	16011
\N	\N	\N	Vent (exo-) DNA Polymerase	\N	16013
\N	\N	\N	Deep VentR DNA Polymerase	\N	16015
\N	\N	\N	Pfu Polymerase	\N	16017
\N	\N	\N	T4 DNA Ligase	\N	16019
\N	\N	\N	Pfu Turbo Polymerase	\N	16022
\N	\N	\N	Pfu Turbo Cx Polymerase	\N	16024
\N	\N	\N	Pfu Ultra	\N	16026
\N	\N	\N	BIO-X-ACT Short Mix	\N	16028
\N	\N	\N	BseRI	\N	16030
\N	\N	\N	10x PCR buffer	\N	16032
\N	\N	\N	NEBuffer 2	\N	16033
\N	\N	\N	KOD Reaction Buffer 1	\N	16035
\N	\N	\N	KOD Reaction Buffer 2	\N	16037
\N	\N	\N	KOD Hot Start Buffer	\N	16039
\N	\N	\N	BIO-X-ACT Buffer	\N	16041
\N	\N	\N	HyperLadder I	\N	16043
\N	\N	\N	HyperLadder II	\N	16045
\N	\N	\N	1kbp ladder	\N	16047
\N	\N	\N	100bp ladder	\N	16049
\N	\N	\N	dATP 25umol	\N	16051
\N	\N	\N	dCTP 25umol	\N	16053
\N	\N	\N	dGTP 25umol	\N	16055
\N	\N	\N	dTTP 25umol	\N	16057
\N	\N	\N	dNTP mix 10mM	\N	16059
\N	\N	\N	dATP 25 mM	\N	16061
\N	\N	\N	dTTP 25 mM	\N	16062
\N	\N	\N	KOD dNTP Mix	\N	16063
\N	\N	\N	BIO-X-ACT dNTP Mix	\N	16065
\N	\N	\N	pET Duet	\N	16067
\N	\N	\N	PCR Cloning kit with Gateway technology	\N	16069
\N	\N	\N	Zero Blunt PCR Cloning Kit	\N	16071
\N	\N	\N	Topo TA	\N	16073
\N	\N	\N	Zero Blunt TOPO PCR Cloning Kit	\N	16075
\N	\N	\N	In-Fusion CF Dry-Down PCR Cloning Kit	\N	16077
\N	\N	\N	Quick Change SDM kit	\N	16079
\N	\N	\N	Nova Blue Singles	\N	16081
\N	\N	\N	Nova Blue Giga Singles	\N	16083
\N	\N	\N	HT96 BL21(DE3)	\N	16085
\N	\N	\N	HT96 NovaBlue	\N	16087
\N	\N	\N	Rosetta 2(DE3) Singles	\N	16089
\N	\N	\N	Origami (DE3) Singles	\N	16091
\N	\N	\N	Rosetta-gami 2	\N	16093
\N	\N	\N	BL21	\N	16095
\N	\N	\N	DE3 Competent cell set	\N	16097
\N	\N	\N	B834(DE3)	\N	16099
\N	\N	\N	XL1 Blue Competent Cells	\N	16101
\N	\N	\N	Xl10 Gold Ultracompetent cells	\N	16103
\N	\N	\N	BL21-Gold Competent cells	\N	16105
\N	\N	\N	BL21 CodonPlus -RIL Competent cells	\N	16107
\N	\N	\N	One Shot TOP10	\N	16109
\N	\N	\N	MAX Efficiency DH5a TI Phage-Resistant Competent cells	\N	16111
\N	\N	\N	TAM1 Competent E.Coli	\N	16113
\N	\N	\N	Multishot Stripwell OmniMAX	\N	16115
\N	\N	\N	Qiagen PCR clean up	\N	16117
\N	\N	\N	Qiagen Plasmid mini kit (25)	\N	16119
\N	\N	\N	Qiagen Plasmid midi kit (25)	\N	16121
\N	\N	\N	Qiaquick 96 plates	\N	16123
\N	\N	\N	Agencourt AMPure 60ml Kit	\N	16125
\N	\N	\N	GS96 medium	\N	16127
\N	\N	\N	SOC medium	\N	16129
\N	\N	\N	Luria broth (LB)	\N	16131
\N	\N	\N	Overnight Express Autoinduction System	\N	16132
\N	\N	\N	SelenoMet Medium Base plus Nutrient Mix	\N	16134
\N	\N	\N	SelenoMethionine solution	\N	16136
\N	\N	\N	Methionine solution	\N	16138
\N	\N	\N	Power Broth	\N	16140
\N	\N	\N	IPTG 1M	\N	16142
\N	\N	\N	DMSO	\N	16143
\N	\N	\N	Betaine	\N	16144
\N	\N	\N	DTT 100mM	\N	16145
\N	\N	\N	EDTA 25mM	\N	16146
\N	\N	\N	10x T4 polymerase buffer	\N	16147
\N	\N	\N	Kanamycin 30mg/ml	\N	16148
\N	\N	\N	Tetracycline 12mg/ml	\N	16149
\N	\N	\N	Chloramphenicol 34mg/ml	\N	16150
\N	\N	\N	Carbenicillin 50mg/ml	\N	16151
\N	\N	\N	Ethanol	\N	16152
\N	\N	\N	Water	\N	16153
\N	\N	\N	Isopropanol	\N	16154
\N	\N	\N	Ethanol 70% v/v in water	\N	16155
\N	\N	\N	TE buffer pH 8.0	\N	16156
\N	\N	\N	Invitrogen Primer	\N	16157
\N	\N	\N	Invitrogen / Life Technologies Primer	\N	16159
\N	\N	\N	Operon Primer	\N	16161
\N	\N	\N	MWG Primer	\N	16163
\N	\N	\N	Forward Primer	\N	16165
\N	\N	\N	Reverse Primer	\N	16166
\N	\N	f	ACES	\N	22002
\N	\N	t	Acetic acid	\N	22005
\N	\N	t	Acetone	\N	22008
\N	\N	t	Acetonitrile	\N	22011
\N	\N	f	N-(2-Acetamido)iminodiacetic acid	\N	22014
\N	\N	t	Acridine orange : hemi (zinc chloride)	\N	22017
\N	\N	t	Acrylamide	\N	22020
\N	\N	t	N,N'-(1,2-Dihydroxyethylene)bis-acrylamide	\N	22023
\N	\N	f	Adenosine	\N	22026
\N	\N	t	Adenosine 5'-triphosphate disodium salt	\N	22029
\N	\N	f	Adonitol	\N	22032
\N	\N	f	L-Alanine	\N	22035
\N	\N	f	Albumin bovine	\N	22038
\N	\N	f	Aluminium chloride	\N	22041
\N	\N	f	4-(2-Amino ethyl) benzenesulfonyl fluoride HCl	\N	22044
\N	\N	t	6-Aminocaproic acid	\N	22047
\N	\N	t	2-Amino-2-methyl-1,3-propanediol	\N	22050
\N	\N	t	4-Amino-m-cresol	\N	22053
\N	\N	f	Ammonium acetate	\N	22056
\N	\N	t	Ammonium bicarbonate	\N	22059
\N	\N	f	Ammonium bromide	\N	22062
\N	\N	t	Ammonium chloride	\N	22065
\N	\N	t	Ammonium citrate dibasic	\N	22068
\N	\N	t	Ammonium citrate tribasic	\N	22071
\N	\N	t	Ammonium fluoride	\N	22074
\N	\N	t	Ammonium formate	\N	22077
\N	\N	t	Ammonium iodide	\N	22080
\N	\N	f	Ammonium iron(II) sulphate hexahydrate	\N	22083
\N	\N	t	Ammonium nitrate	\N	22086
\N	\N	t	Ammonium oxalate	\N	22089
\N	\N	t	Ammonium persulphate	\N	22092
\N	\N	f	di-Ammonium hydrogen phosphate	\N	22095
\N	\N	f	Ammonium dihydrogen phosphate	\N	22098
\N	\N	t	Ammonium sulphate	\N	22101
\N	\N	f	Ammonium tartrate dibasic	\N	22104
\N	\N	f	Ammonium trifluoroacetate	\N	22107
\N	\N	t	Ammonium thiocyanate	\N	22110
\N	\N	t	Ampicillin (sodium)	\N	22113
\N	\N	f	AMPSO	\N	22116
\N	\N	t	Aniline	\N	22119
\N	\N	t	8-Anilino-1-naphthalene sulphonic acid (ammonium salt)	\N	22122
\N	\N	f	L-Arginine	\N	22125
\N	\N	f	L-Ascorbic acid	\N	22128
\N	\N	f	L-Asparagine	\N	22131
\N	\N	f	L-Aspartic acid	\N	22134
\N	\N	t	Azelaic acid	\N	22137
\N	\N	t	Barbital	\N	22140
\N	\N	t	Barium acetate	\N	22143
\N	\N	t	Barium chloride	\N	22146
\N	\N	t	Barium nitrate	\N	22149
\N	\N	t	Barium thiocyanate trihydrate	\N	22152
\N	\N	t	Benzamidine hydrochloride hydrate	\N	22155
\N	\N	t	Benzamidine hydrochloride	\N	22158
\N	\N	t	Benzenesulphonic acid	\N	22161
\N	\N	t	Benzoic acid	\N	22164
\N	\N	f	BES	\N	22167
\N	\N	f	Beryllium sulphate tetrahydrate	\N	22170
\N	\N	f	Betaine monohydrate	\N	22174
\N	\N	f	Bestatin hydrochloride	\N	22177
\N	\N	f	Bicine	\N	22180
\N	\N	t	BigCHAP	\N	22183
\N	\N	f	D-Biotin	\N	22186
\N	\N	f	Biotin-maleimide	\N	22189
\N	\N	t	Bis(hexamethylene)triamine	\N	22192
\N	\N	f	BIS-TRIS	\N	22195
\N	\N	f	BIS-TRIS hydrochloride	\N	22198
\N	\N	f	BIS-TRIS propane	\N	22201
\N	\N	f	Boric acid	\N	22204
\N	\N	t	Brilliant Blue G	\N	22207
\N	\N	t	Brilliant Blue R	\N	22210
\N	\N	f	Bromophenol blue,sodium salt	\N	22213
\N	\N	t	2-Brom-4'-nitroacetophenone	\N	22216
\N	\N	t	BRIJ (R) 35	\N	22219
\N	\N	f	1,2-Butanediol	\N	22222
\N	\N	f	1,3-Butanediol	\N	22225
\N	\N	f	1,4-Butanediol	\N	22228
\N	\N	f	2,3-Butanediol	\N	22231
\N	\N	t	1,4-Butanediol diglycidyl ether	\N	22234
\N	\N	t	n-Butanol	\N	22237
\N	\N	t	tert-Butanol	\N	22240
\N	\N	f	tert-Butyl methylether	\N	22243
\N	\N	f	Butyl formate	\N	22246
\N	\N	t	gamma-Butyrolactone	\N	22249
\N	\N	f	C12E8	\N	22252
\N	\N	t	CABS	\N	22255
\N	\N	t	Cadmium acetate	\N	22258
\N	\N	t	Cadmium bromide	\N	22261
\N	\N	f	Cadmium chloride	\N	22264
\N	\N	f	Cadmium chloride dihydrate	\N	22267
\N	\N	t	Cadmium iodide	\N	22270
\N	\N	f	Cadmium sulphate	\N	22273
\N	\N	f	Cadmium sulphate hydrate	\N	22276
\N	\N	f	Calcium acetate	\N	22279
\N	\N	t	Calcium carbonate	\N	22282
\N	\N	f	Calcium chloride	\N	22285
\N	\N	f	Calcium chloride dihydrate	\N	22288
\N	\N	t	Calcium chloride hexahydrate	\N	22291
\N	\N	t	Calcium sulfate	\N	22294
\N	\N	f	e-Caprolactam	\N	22297
\N	\N	f	CAPS	\N	22300
\N	\N	f	CAPSO	\N	22303
\N	\N	t	Carbenicillin disodium salt	\N	22306
\N	\N	t	Cesium bromide	\N	22309
\N	\N	f	Cesium chloride	\N	22312
\N	\N	t	Cesium iodide	\N	22315
\N	\N	t	Cesium nitrate	\N	22318
\N	\N	f	Cesium sulfate	\N	22321
\N	\N	t	Cetyltrimethyl ammonium chloride	\N	22324
\N	\N	t	CHAPS	\N	22327
\N	\N	f	CHES	\N	22330
\N	\N	t	Chloramine T hydrate	\N	22333
\N	\N	t	Chloramphenicol	\N	22336
\N	\N	t	Chloroform	\N	22339
\N	\N	t	4 Chloro-1-naphthol	\N	22342
\N	\N	t	7-Chloro-4-nitro benz-2-oxa-1,3-diazole	\N	22345
\N	\N	f	Cholesterol	\N	22348
\N	\N	f	Cholic acid	\N	22351
\N	\N	t	Citric acid	\N	22354
\N	\N	f	Cobalt(II) chloride	\N	22357
\N	\N	t	Cobalt(II) chloride hexahydrate	\N	22360
\N	\N	f	Copper bromide	\N	22363
\N	\N	t	Copper chloride	\N	22366
\N	\N	f	Copper(II) chloride	\N	22369
\N	\N	t	Copper(II) chloride dihydrate	\N	22372
\N	\N	t	Copper(II) sulphate pentahydrate	\N	22375
\N	\N	f	Creatine	\N	22378
\N	\N	f	L-Cysteine	\N	22381
\N	\N	t	Cytidine 5'-triphosphate disodium salt	\N	22384
\N	\N	t	Cyanogen bromide	\N	22387
\N	\N	t	2-Cyclohexen-1-one	\N	22390
\N	\N	f	N-Decanoyl-N-methylglucamine	\N	22393
\N	\N	f	Zwittergent(R) 3-10	\N	22396
\N	\N	t	Deoxycholic acid (sodium salt)	\N	22399
\N	\N	f	2'-Deoxyadenosine-5'-triphosphate,disodium salt,trihydrate	\N	22402
\N	\N	t	2'-Deoxycytidine 5'-triphosphate disodium salt	\N	22405
\N	\N	t	2'-Deoxyguanosine 5'-triphosphate trisodium salt	\N	22408
\N	\N	t	2'-Deoxythymidine 5'-triphosphate sodium salt	\N	22411
\N	\N	f	Dextran sulphate,sodium salt	\N	22414
\N	\N	t	1,6-Diaminohexane	\N	22417
\N	\N	f	1,8-Diaminooctane	\N	22420
\N	\N	t	1,5-Diaminopentane	\N	22423
\N	\N	f	di-u-Iodobis(ethylenediamine)-di-platinum (II) nitrate	\N	22426
\N	\N	f	Dichloro(ethylenediamine)platinum (II)	\N	22429
\N	\N	f	Dichloromethane	\N	22432
\N	\N	t	N,N'-Dicyclohexylcarbodiimide	\N	22435
\N	\N	f	DDM	\N	22438
\N	\N	f	DHPC	\N	22441
\N	\N	t	N,N-Dimethyldecylamine-N-oxide	\N	22444
\N	\N	f	N,N-Dimethyldodecylamine-N-oxide	\N	22447
\N	\N	t	Zwittergent(R) 3-8	\N	22450
\N	\N	t	Zwittergent(R) 3-16	\N	22453
\N	\N	f	Diethyl-dithio-carbamic acid (sodium salt)	\N	22456
\N	\N	t	Diethyl ether	\N	22459
\N	\N	t	Diethyl pyrocarbonate	\N	22462
\N	\N	t	N,N-Dimethylformamide	\N	22465
\N	\N	t	Dimethyl suberimidate dihydrchloride	\N	22468
\N	\N	f	Dioxane	\N	22471
\N	\N	t	Diphenylthiocarbazone	\N	22474
\N	\N	t	Dipicolinic acid	\N	22477
\N	\N	f	DIPSO	\N	22480
\N	\N	t	5 5' Dithio-bis (2-nitrobenzoic acid)	\N	22483
\N	\N	t	Dithiothreitol	\N	22486
\N	\N	f	DLPC	\N	22489
\N	\N	f	DM	\N	22492
\N	\N	f	DMPC	\N	22495
\N	\N	f	DOPG	\N	22498
\N	\N	t	Dimethylsulphoxide	\N	22501
\N	\N	f	Dodecyl-b-D-glucopyranoside	\N	22504
\N	\N	t	Zwittergent(R) 3-12	\N	22507
\N	\N	f	DOPC	\N	22510
\N	\N	f	Ectoine	\N	22513
\N	\N	f	Erythritol	\N	22516
\N	\N	f	Ethanolamine	\N	22520
\N	\N	t	Ethidium bromide	\N	22523
\N	\N	t	EDTA	\N	22526
\N	\N	f	EDTA disodium salt	\N	22529
\N	\N	t	2-Ethoxyethanol	\N	22532
\N	\N	f	Ethyl acetate	\N	22535
\N	\N	f	Ethylenediamine	\N	22538
\N	\N	t	Ethylene glycol	\N	22541
\N	\N	f	EGTA	\N	22544
\N	\N	t	EPPS	\N	22547
\N	\N	t	N Ethylmaleimide	\N	22550
\N	\N	f	Ethyl mercuryphosphate	\N	22553
\N	\N	f	Ethyl methylketone	\N	22556
\N	\N	f	2-Ethyl-5-phenylisoxazolium sulfonate	\N	22559
\N	\N	f	D-(-)-Fructose	\N	22562
\N	\N	f	L-(+)-Fructose	\N	22565
\N	\N	t	Formaldehyde solution	\N	22568
\N	\N	t	Formamide	\N	22571
\N	\N	t	Formic acid	\N	22574
\N	\N	t	Fusaric acid	\N	22577
\N	\N	f	D-(+)-Glucose	\N	22580
\N	\N	f	L-(-)-Glucose	\N	22583
\N	\N	t	Glutathione (oxidised form)	\N	22586
\N	\N	f	Glutathione (reduced form)	\N	22589
\N	\N	f	L-Glutamic acid	\N	22592
\N	\N	f	D-Glutamine	\N	22595
\N	\N	t	Glutaraldehyde	\N	22598
\N	\N	f	Glycerol	\N	22601
\N	\N	f	Glycine	\N	22604
\N	\N	f	Glycine betaine	\N	22607
\N	\N	f	Glycyl-glycyl-glycine	\N	22610
\N	\N	t	Guanidinium chloride	\N	22613
\N	\N	f	Guanosine 5'-diphosphate (GDP) - sodium salt	\N	22616
\N	\N	f	Guanosine 5'-triphosphate di sodium salt	\N	22619
\N	\N	t	HEPBS	\N	22622
\N	\N	f	HEPES	\N	22625
\N	\N	f	HEPES sodium salt	\N	22628
\N	\N	f	HEPPSO	\N	22631
\N	\N	f	1,2,3-Heptanetriol	\N	22634
\N	\N	f	n-Heptyl-b-D-glucopyranoside	\N	22637
\N	\N	t	Hexaminecobalt trichloride	\N	22640
\N	\N	f	Hexadecyltrimethylammonium bromide	\N	22643
\N	\N	f	Hexafluoro isopropanol	\N	22646
\N	\N	f	1,6-Hexanediol	\N	22649
\N	\N	t	2,5-Hexanediol	\N	22652
\N	\N	f	1,2,3-Hexanetriol	\N	22655
\N	\N	f	Hexyl-b-D-glucopyranoside	\N	22658
\N	\N	t	Hydrochloric acid	\N	22661
\N	\N	t	3' Hydroxy-2-butanone	\N	22664
\N	\N	t	Hydroxylamine hydrochloride	\N	22667
\N	\N	t	2-Hydroxy-5-nitrobenzaldehyde	\N	22670
\N	\N	t	alpha Hydroxyisobutyric acid	\N	22673
\N	\N	t	Imidazole	\N	22676
\N	\N	t	Iodoacetamide	\N	22679
\N	\N	t	Iodoacetic acid	\N	22682
\N	\N	f	N-Iodo acetyl-N-(5-sulfo-1-naphthyl) Ethyldiamine	\N	22685
\N	\N	f	Iron(III) chloride	\N	22688
\N	\N	f	Iron(III) chloride hexahydrate	\N	22691
\N	\N	t	Iron(II) chloride tetrahydrate	\N	22694
\N	\N	t	Iron(II) sulfate	\N	22697
\N	\N	t	IPTG	\N	22700
\N	\N	t	Iso-propanol	\N	22703
\N	\N	t	Jeffamine(R) M-600	\N	22706
\N	\N	f	Jeffamine(R) ED-2001	\N	22709
\N	\N	f	Kanamycin monosulphate	\N	22712
\N	\N	f	Lactose	\N	22715
\N	\N	f	N-Lauroylsarcosine  sodium salt	\N	22718
\N	\N	t	Lead(II) acetate trihydrate	\N	22721
\N	\N	t	Lead(II) acetate	\N	22724
\N	\N	f	Leupeptin trifluoroacetate salt	\N	22727
\N	\N	f	Lithium acetate	\N	22730
\N	\N	f	Lithium acetate dihydrate	\N	22733
\N	\N	f	Lithium bromide	\N	22736
\N	\N	f	Lithium chloride	\N	22739
\N	\N	t	Lithium chloride (hydrate)	\N	22742
\N	\N	f	Lithium citrate (anhydrous)	\N	22745
\N	\N	t	Lithium fluoride	\N	22748
\N	\N	f	Lithium formate	\N	22751
\N	\N	f	tri-Lithium citrate tetrahydrate	\N	22754
\N	\N	t	Lithium nitrate	\N	22757
\N	\N	t	Lithium perchlorate	\N	22760
\N	\N	t	Lithium salicylate	\N	22763
\N	\N	t	Lithium sulphate	\N	22766
\N	\N	f	Lithium sulphate monohydrate	\N	22769
\N	\N	f	Magnesium acetate tetrahydrate	\N	22772
\N	\N	t	Magnesium bromide	\N	22775
\N	\N	f	Magnesium chloride anhydrous	\N	22778
\N	\N	f	Magnesium chloride  hexahydrate	\N	22781
\N	\N	f	Magnesium formate	\N	22784
\N	\N	f	Magnesium formate dihydrate	\N	22787
\N	\N	f	Magnesium nitrate	\N	22790
\N	\N	f	Magnesium nitrate hexahydrate	\N	22793
\N	\N	f	Magnesium sulphate	\N	22796
\N	\N	f	Magnesium sulphate hydrate	\N	22799
\N	\N	f	Magnesium sulphate monohydrate	\N	22802
\N	\N	f	Magnesium sulphate hexahydrate	\N	22805
\N	\N	f	Magnesium sulphate heptahydrate	\N	22808
\N	\N	t	Manganese(II) chloride	\N	22811
\N	\N	f	Manganese(II) chloride dihydrate	\N	22814
\N	\N	t	Manganese(II) chloride monohydrate	\N	22817
\N	\N	f	Manganese(II) chloride tetrahydrate	\N	22820
\N	\N	f	Malic acid	\N	22823
\N	\N	f	Malonic acid	\N	22826
\N	\N	f	D-Mannitol	\N	22829
\N	\N	f	L-Mannitol	\N	22832
\N	\N	f	D-(+)-Mannose	\N	22835
\N	\N	f	L-(-)-Mannose	\N	22838
\N	\N	t	4-(N-Maleimido) benzophenone	\N	22841
\N	\N	t	2-Mercaptoethanol	\N	22844
\N	\N	t	Mercury(II) acetate	\N	22847
\N	\N	t	Mercury(II) chloride	\N	22850
\N	\N	t	Mersalyl acid	\N	22853
\N	\N	f	MES	\N	22856
\N	\N	f	MES sodium salt	\N	22859
\N	\N	t	Methanol	\N	22862
\N	\N	f	Methyl acetate	\N	22865
\N	\N	t	Methylmercury chloride	\N	22868
\N	\N	f	2-Methyl-2,4-pentanediol	\N	22871
\N	\N	t	MOBS	\N	22874
\N	\N	t	MOPS	\N	22877
\N	\N	t	MOPSO	\N	22880
\N	\N	f	Inositol	\N	22883
\N	\N	t	NADP sodium salt	\N	22886
\N	\N	t	NADPH tetrasodium salt	\N	22889
\N	\N	f	N-Hydroxysuccinimide	\N	22892
\N	\N	t	Nickel(II) chloride	\N	22895
\N	\N	t	Nickel(II) chloride hydrate	\N	22898
\N	\N	t	Nickel(II) chloride hexahydrate	\N	22901
\N	\N	t	Nickel(II) sulphate hexahydrate	\N	22904
\N	\N	t	Nitric acid	\N	22907
\N	\N	t	N-Nonanoyl-N-methylglucamine	\N	22910
\N	\N	t	Nonaethylene glycol monododecyl ether	\N	22913
\N	\N	t	Octaethylene glycol monodecyl ether	\N	22916
\N	\N	f	octyl b-D-Glucopyranoside	\N	22919
\N	\N	f	n-octyl b-D-Thioglucopyranoside	\N	22922
\N	\N	t	beta-Octylglucoside	\N	22925
\N	\N	f	N-Octanoyl-N-methylglucamine	\N	22928
\N	\N	f	nonyl-b-D-Glucopyranoside	\N	22931
\N	\N	t	Orthophosphoric acid	\N	22934
\N	\N	f	Paratone-N	\N	22937
\N	\N	t	PCMB	\N	22940
\N	\N	t	PCMBS	\N	22943
\N	\N	t	4-(2-Pyridylazo)resorcinol mono sodium salt monohydrate	\N	22946
\N	\N	f	Pentaerythritol ethoxylate (3/4 EO/OH)	\N	22949
\N	\N	f	Pentaerythritol propoxylate	\N	22952
\N	\N	f	Pentaethylene glycol monodecyl ether	\N	22955
\N	\N	f	Pentaethylene glycol monooctyl ether	\N	22958
\N	\N	t	3-Pentanone	\N	22961
\N	\N	f	Pepsinostreptin	\N	22964
\N	\N	t	1,10 Phenanthroline	\N	22967
\N	\N	t	Phenol	\N	22970
\N	\N	t	Phenol red	\N	22973
\N	\N	t	Phenylglyoxal hydrate	\N	22976
\N	\N	t	Phenylmercury acetate	\N	22979
\N	\N	t	L-alpha-Phosphatidylcholine	\N	22982
\N	\N	t	PIPES	\N	22985
\N	\N	t	PMSF	\N	22988
\N	\N	t	Poly(acrylic acid sodium salt) 5100	\N	22991
\N	\N	f	Polyethylene glycol 400	\N	22994
\N	\N	f	Polyethylene glycol 200	\N	22997
\N	\N	f	Polyethylene glycol 300	\N	23000
\N	\N	f	Polyethylene glycol 600	\N	23003
\N	\N	f	Polyethylene glycol 1000	\N	23006
\N	\N	f	Polyethylene glycol 1500	\N	23009
\N	\N	f	Polyethylene glycol 2000	\N	23012
\N	\N	f	Polyethylene glycol 3000	\N	23015
\N	\N	f	Polyethylene glycol 3350	\N	23018
\N	\N	f	Polyethylene glycol 3350 monodisperse	\N	23021
\N	\N	f	Polyethylene glycol 4000	\N	23024
\N	\N	f	Polyethylene glycol 5000	\N	23027
\N	\N	f	Polyethylene glycol 6000	\N	23030
\N	\N	f	Polyethylene glycol 8000	\N	23033
\N	\N	f	Polyethylene glycol 10000	\N	23036
\N	\N	f	Polyethylene glycol 20000	\N	23039
\N	\N	f	Polyethylene glycol monomethyl ether 550	\N	23042
\N	\N	f	Polyethylene glycol monomethyl ether 2000	\N	23045
\N	\N	f	Polyethylene glycol monomethyl ether 5000	\N	23048
\N	\N	f	Polyethyleneimine	\N	23051
\N	\N	f	Polypropylene glycol P400	\N	23054
\N	\N	t	Polymixin B sulphate	\N	23057
\N	\N	f	Polyoxyadenylic acid,potassium salt	\N	23060
\N	\N	f	Poly(vinyl alcohol)	\N	23063
\N	\N	f	Polyvinylpyrrolidone K15	\N	23066
\N	\N	f	POPC	\N	23069
\N	\N	f	POPSO	\N	23072
\N	\N	f	Potassium acetate	\N	23075
\N	\N	f	Potassium bromide	\N	23078
\N	\N	f	Potassium chloride	\N	23081
\N	\N	f	Potassium citrate	\N	23084
\N	\N	f	Tripotassium citrate	\N	23087
\N	\N	t	Potassium cyanate	\N	23090
\N	\N	f	Potassium dihydrogen phosphate	\N	23093
\N	\N	f	Dipotassium hydrogen phosphate	\N	23096
\N	\N	f	Potassium phosphate dibasic trihydrate	\N	23099
\N	\N	t	Potassium fluoride	\N	23102
\N	\N	f	Potassium formate	\N	23105
\N	\N	t	Potassium hexachloroiridate(IV)	\N	23108
\N	\N	t	Potassium hexachloroplatinate(IV)	\N	23111
\N	\N	t	Potassium hydroxide	\N	23114
\N	\N	t	Potassium permanganate	\N	23117
\N	\N	f	Potassium iodide	\N	23120
\N	\N	f	Potassium nitrate	\N	23123
\N	\N	f	Potassium sodium tartrate tetrahydrate	\N	23126
\N	\N	f	Potassium sodium tartrate	\N	23129
\N	\N	f	Potassium sulphate	\N	23132
\N	\N	f	Dipotassium tartrate	\N	23135
\N	\N	t	Potassium tetrachloroaurate(III)	\N	23138
\N	\N	t	Potassium tetrachloroplatinate(II)	\N	23141
\N	\N	t	Potassium tetracyanoplatinate(II)	\N	23144
\N	\N	t	Dipotassium tetraraiodomercurate(II)	\N	23147
\N	\N	f	Potassium tetranitroplatinate(II)	\N	23150
\N	\N	t	Potassium thiocyanate	\N	23153
\N	\N	f	L-Proline	\N	23156
\N	\N	t	2-Propanol	\N	23159
\N	\N	f	1,2-Propanediol	\N	23162
\N	\N	f	1,3-Propanediol	\N	23165
\N	\N	t	Propionitrile	\N	23168
\N	\N	t	Pyridine	\N	23171
\N	\N	t	Protamine sulphate	\N	23174
\N	\N	t	Pyridoxal hydrochloride	\N	23177
\N	\N	f	D-(+)-Raffinose	\N	23180
\N	\N	f	Riboflavin	\N	23183
\N	\N	f	Rubidium bromide	\N	23186
\N	\N	f	Rubidium chloride	\N	23189
\N	\N	t	Silver nitrate	\N	23192
\N	\N	f	Sodium acetate	\N	23195
\N	\N	f	Sodium acetate trihydrate	\N	23198
\N	\N	t	Sodium azide	\N	23201
\N	\N	f	Sodium bromide	\N	23204
\N	\N	f	Sodium cacodylate hydrate	\N	23207
\N	\N	t	Sodium cacodylate trihydrate	\N	23210
\N	\N	t	Sodium carbonate (anhydrous)	\N	23213
\N	\N	f	Sodium chloride	\N	23216
\N	\N	f	Sodium citrate	\N	23219
\N	\N	f	Sodium citrate monobasic	\N	23222
\N	\N	t	Sodium deoxycholate	\N	23225
\N	\N	t	Sodium deoxycholate monohydrate	\N	23228
\N	\N	t	Sodium dodecyl sulphate	\N	23231
\N	\N	t	Sodium fluoride	\N	23234
\N	\N	t	Sodium formate	\N	23237
\N	\N	t	Sodium n-hexadecyl sulphate	\N	23240
\N	\N	f	Sodium hydrogen carbonate	\N	23243
\N	\N	f	Sodium dihydrogen phosphate	\N	23246
\N	\N	f	Disodium hydrogen phosphate	\N	23249
\N	\N	f	Sodium phosphate dibasic	\N	23252
\N	\N	t	Sodium hydroxide	\N	23255
\N	\N	t	Sodium iodide	\N	23258
\N	\N	f	Sodium malonate dibasic	\N	23261
\N	\N	f	Sodium malonate	\N	23264
\N	\N	t	Sodium meta periodate	\N	23267
\N	\N	t	Sodium nitrate	\N	23270
\N	\N	t	Sodium nitrite	\N	23273
\N	\N	t	Sodium perchlorate	\N	23276
\N	\N	t	Sodium phosphate	\N	23279
\N	\N	t	Sodium propionate	\N	23282
\N	\N	t	Sodium succinate	\N	23285
\N	\N	f	Sodium sulphate	\N	23288
\N	\N	f	Sodium sulphate decahydrate	\N	23291
\N	\N	f	Disodium tartrate	\N	23294
\N	\N	t	Sodium tetraborate	\N	23297
\N	\N	t	Sodium thiocyanate	\N	23300
\N	\N	t	Sodium thiosulphate pentahydrate	\N	23303
\N	\N	t	Sodium trichloroacetate	\N	23306
\N	\N	f	SOPC	\N	23309
\N	\N	f	Sorbitol	\N	23312
\N	\N	f	Spermidine	\N	23315
\N	\N	t	Spermidine trihydrochloride	\N	23318
\N	\N	t	Spermine tetrahydrochloride	\N	23321
\N	\N	t	Streptomycin sulphate	\N	23324
\N	\N	t	Strontium dichloride	\N	23327
\N	\N	t	Succinic acid	\N	23330
\N	\N	t	Succinic anhydride	\N	23333
\N	\N	t	Succinimide	\N	23336
\N	\N	f	Sucrose	\N	23339
\N	\N	t	Sulfo-EGS	\N	23342
\N	\N	t	Sulphuric acid	\N	23345
\N	\N	f	TABS	\N	23348
\N	\N	t	Tacsimate(TM)	\N	23351
\N	\N	f	TAPS	\N	23354
\N	\N	t	TAPSO	\N	23357
\N	\N	t	Taurine	\N	23360
\N	\N	t	TEMED	\N	23363
\N	\N	f	TES	\N	23366
\N	\N	t	Tetracycline hydrochloride	\N	23369
\N	\N	t	Tetraethylene glycol monooctyl ether	\N	23372
\N	\N	t	Tetradecyl-N-N-dimethyl-3-ammonio-1-propanesulfonate	\N	23375
\N	\N	t	Tetrahydrofurane	\N	23378
\N	\N	t	3,4,5,6-Tetrahydrophthalic-anhydride	\N	23381
\N	\N	t	Tetrakis (acetoxymercury) methane	\N	23384
\N	\N	t	Tetramethyl ammonium chloride	\N	23387
\N	\N	t	Thimerosal	\N	23390
\N	\N	t	Thymol	\N	23393
\N	\N	t	TLCK	\N	23396
\N	\N	t	TPCK	\N	23399
\N	\N	t	TPEN	\N	23402
\N	\N	f	Trehalose	\N	23405
\N	\N	t	Trichloroacetic acid	\N	23408
\N	\N	f	Tricine	\N	23411
\N	\N	t	Triethanolamine	\N	23414
\N	\N	t	Triethylene glycol	\N	23417
\N	\N	t	2,2,2-Trifluoroethanol	\N	23420
\N	\N	t	Trimethylamine hydrochloride	\N	23423
\N	\N	t	Trimethylamine N-oxide	\N	23426
\N	\N	t	Trimethyl ammonium chloride	\N	23429
\N	\N	t	Trimethyllead acetate	\N	23432
\N	\N	t	2,3,5 Triphenyl tetrazolium chloride	\N	23435
\N	\N	f	TRIS	\N	23438
\N	\N	t	Trizma(R) hydrochloride	\N	23441
\N	\N	f	Tris (2,2' bipyridyl)dichloro ruthenium(11) hexahydrate	\N	23444
\N	\N	t	Triton(R) X-100	\N	23447
\N	\N	t	Triton(R) X-114	\N	23450
\N	\N	f	Trypsin-chymotrypsin inhibitor (Soy bean)	\N	23453
\N	\N	f	Trypsin inhibitor (lima bean)	\N	23456
\N	\N	f	Tween(R) 20;polyoxyethylene sorbitan monolaurate	\N	23459
\N	\N	t	Urea	\N	23462
\N	\N	t	Vancomycin hydrochloride	\N	23465
\N	\N	t	Vitamin B12	\N	23468
\N	\N	t	X-GAL	\N	23471
\N	\N	t	Xylene cyanol FF	\N	23474
\N	\N	t	Xylenol orange tetrasodium salt	\N	23477
\N	\N	f	Xylitol	\N	23480
\N	\N	t	Ytterbium(III) chloride hexahydrate	\N	23483
\N	\N	t	Yttrium(III) chloride hexahydrate	\N	23486
\N	\N	t	Yttrium(III) chloride	\N	23489
\N	\N	t	Zinc acetate dihydrate	\N	23492
\N	\N	t	Zinc acetate	\N	23495
\N	\N	t	Zinc chloride	\N	23498
\N	\N	t	Zinc sulphate heptahydrate	\N	23501
\N	\N	t	Zinc sulphate	\N	23504
\N	\N	\N	pET-21d Vector	\N	36001
\N	\N	\N	pDEST17 Vector	\N	36013
\N	\N	\N	pET-26b Vector	\N	36027
\N	\N	\N	pET-28a Vector	\N	36040
\N	\N	\N	pOPING-Hygro Vector	\N	36054
\N	\N	\N	pDONR207 Vector	\N	36063
\N	\N	\N	pDONR221 Vector	\N	36074
\N	\N	\N	pCR[TM]-Blunt Vector	\N	36086
\N	\N	\N	pET15b Vector	\N	36099
\N	\N	\N	pOPINA Vector	\N	36111
\N	\N	\N	pOPINB Vector	\N	36117
\N	\N	\N	pOPINE Vector	\N	36123
\N	\N	\N	pOPINF Vector	\N	36129
\N	\N	\N	pOPING Vector	\N	36135
\N	\N	\N	pOPINH Vector	\N	36142
\N	\N	\N	pOPINI Vector	\N	36148
\N	\N	\N	pOPINJ Vector	\N	36154
\N	\N	\N	pOPINK Vector	\N	36160
\N	\N	\N	pOPINM Vector	\N	36166
\N	\N	\N	pOPINS Vector	\N	36172
\N	\N	\N	YEp352 Vector	\N	36178
\N	\N	\N	pTriEx-2 Vector	\N	36188
\.


--
-- Data for Name: sam_abstsa2hazaph; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_abstsa2hazaph (otherrole, hazardphraseid) FROM stdin;
22005	18223
22005	18049
22005	18186
22005	18010
22005	18196
22005	18171
22008	18219
22008	18167
22008	18181
22008	18152
22008	18224
22008	18171
22008	18011
22011	18219
22011	18031
22011	18159
22011	18172
22011	18222
22011	18196
22011	18011
22017	18185
22017	18222
22017	18024
22017	18166
22020	18119
22020	18221
22020	18035
22020	18098
22020	18053
22020	18085
22020	18083
22020	18205
22020	18196
22020	18086
22020	18023
22023	18222
22023	18024
22023	18184
22023	18052
22023	18171
22029	18169
22044	18169
22044	18048
22044	18166
22047	18224
22047	18184
22047	18052
22047	18171
22050	18224
22050	18184
22050	18052
22050	18171
22053	18222
22053	18189
22053	18052
22053	18028
22053	18171
22056	18169
22059	18222
22059	18186
22059	18196
22059	18052
22059	18028
22059	18171
22062	18169
22062	18166
22065	18050
22065	18222
22065	18166
22065	18028
22068	18224
22068	18051
22068	18171
22071	18224
22071	18184
22071	18052
22071	18171
22074	18031
22074	18221
22074	18196
22074	18171
22077	18224
22077	18184
22077	18052
22077	18171
22080	18224
22080	18052
22080	18171
22080	18188
22083	18224
22083	18184
22083	18052
22083	18171
22086	18226
22086	18224
22086	18072
22086	18008
22086	18051
22086	18190
22086	18171
22086	18160
22089	18169
22089	18027
22089	18222
22092	18226
22092	18222
22092	18082
22092	18008
22092	18168
22092	18166
22092	18052
22092	18028
22092	18171
22092	18188
22095	18224
22095	18184
22095	18052
22095	18171
22098	18224
22098	18184
22098	18051
22098	18171
22101	18224
22101	18184
22101	18052
22101	18171
22107	18224
22107	18184
22107	18052
22107	18171
22110	18156
22110	18046
22110	18222
22110	18024
22113	18185
22113	18222
22113	18082
22113	18166
22113	18052
22113	18171
22119	18225
22119	18185
22119	18031
22119	18221
22119	18174
22119	18213
22119	18222
22119	18024
22119	18104
22119	18072
22119	18088
22119	18196
22122	18222
22122	18189
22122	18224
22122	18052
22122	18028
22122	18171
22134	18169
22134	18166
22140	18222
22140	18184
22140	18028
22140	18188
22143	18174
22143	18222
22143	18025
22146	18221
22146	18035
22146	18022
22146	18196
22149	18226
22149	18174
22149	18222
22149	18025
22149	18008
22152	18156
22152	18046
22152	18222
22152	18024
22155	18224
22155	18184
22155	18052
22155	18171
22158	18224
22158	18184
22158	18052
22158	18171
22161	18223
22161	18048
22161	18186
22161	18196
22161	18028
22161	18171
22164	18050
22164	18222
22164	18028
22164	18171
22167	18224
22167	18052
22167	18171
22170	18225
22170	18107
22170	18036
22170	18035
22170	18213
22170	18096
22170	18083
22170	18205
22170	18220
22170	18196
22170	18052
22170	18103
22177	18169
22177	18166
22180	18169
22180	18166
22183	18221
22183	18186
22183	18118
22183	18166
22183	18205
22183	18196
22183	18052
22183	18023
22192	18223
22192	18048
22192	18186
22192	18196
22192	18028
22192	18171
22195	18224
22195	18184
22195	18052
22195	18171
22204	18050
22204	18184
22204	18166
22204	18056
22204	18196
22204	18190
22204	18054
22204	18171
22204	18188
22207	18184
22207	18166
22207	18052
22207	18171
22210	18169
22210	18166
22216	18223
22216	18036
22216	18085
22216	18048
22216	18052
22216	18054
22219	18080
22219	18222
22219	18186
22219	18056
22219	18028
22219	18171
22228	18222
22228	18184
22228	18028
22228	18188
22234	18174
22234	18053
22234	18222
22234	18189
22234	18083
22234	18171
22234	18023
22237	18156
22237	18149
22237	18080
22237	18222
22237	18197
22237	18189
22237	18055
22237	18010
22237	18028
22237	18171
22237	18124
22240	18219
22240	18159
22240	18022
22240	18222
22240	18152
22240	18011
22246	18219
22246	18159
22246	18181
22246	18152
22246	18224
22246	18168
22246	18051
22246	18011
22249	18050
22249	18222
22249	18184
22249	18028
22249	18171
22255	18224
22255	18184
22255	18052
22255	18171
22258	18225
22258	18105
22258	18213
22258	18222
22258	18024
22258	18212
22261	18225
22261	18221
22261	18105
22261	18213
22261	18222
22261	18024
22261	18212
22264	18105
22264	18117
22264	18118
22264	18205
22264	18196
22264	18220
22264	18099
22264	18212
22264	18225
22264	18035
22264	18036
22264	18213
22264	18085
22264	18086
22267	18105
22267	18117
22267	18118
22267	18205
22267	18196
22267	18220
22267	18099
22267	18212
22267	18225
22267	18035
22267	18036
22267	18213
22267	18085
22267	18086
22270	18225
22270	18221
22270	18105
22270	18213
22270	18168
22270	18196
22270	18047
22270	18032
22270	18212
22270	18129
22273	18225
22273	18221
22273	18105
22273	18213
22273	18205
22273	18196
22273	18099
22273	18028
22273	18212
22273	18103
22276	18225
22276	18221
22276	18105
22276	18213
22276	18205
22276	18196
22276	18099
22276	18028
22276	18212
22276	18103
22279	18184
22279	18052
22279	18171
22282	18080
22282	18224
22282	18055
22282	18191
22282	18171
22285	18050
22285	18224
22285	18168
22285	18166
22288	18050
22288	18224
22288	18171
22291	18050
22291	18224
22291	18184
22291	18171
22294	18169
22294	18166
22300	18169
22300	18166
22303	18169
22303	18166
22306	18185
22306	18222
22306	18082
22306	18166
22309	18224
22309	18184
22309	18052
22309	18171
22312	18169
22315	18185
22315	18222
22315	18082
22315	18166
22315	18196
22315	18052
22318	18226
22318	18224
22318	18008
22318	18052
22318	18160
22321	18169
22321	18166
22324	18080
22324	18213
22324	18222
22324	18104
22324	18056
22324	18191
22324	18212
22324	18171
22327	18185
22327	18224
22327	18052
22327	18171
22330	18050
22330	18222
22330	18171
22333	18223
22333	18081
22333	18147
22333	18048
22333	18186
22333	18045
22333	18166
22333	18196
22333	18028
22333	18171
22336	18185
22336	18221
22336	18082
22336	18085
22336	18205
22336	18196
22339	18092
22339	18222
22339	18072
22339	18184
22339	18056
22339	18028
22339	18188
22342	18224
22342	18184
22342	18052
22342	18171
22345	18224
22345	18056
22348	18169
22348	18166
22351	18169
22351	18166
22354	18080
22354	18224
22354	18055
22354	18186
22354	18171
22357	18225
22357	18221
22357	18105
22357	18213
22357	18082
22357	18205
22357	18166
22357	18196
22357	18028
22357	18212
22357	18103
22360	18225
22360	18221
22360	18105
22360	18213
22360	18082
22360	18205
22360	18166
22360	18196
22360	18028
22360	18212
22360	18103
22363	18169
22363	18166
22366	18225
22366	18105
22366	18213
22366	18222
22366	18166
22366	18028
22366	18212
22369	18225
22369	18105
22369	18213
22369	18222
22369	18184
22369	18052
22369	18028
22369	18212
22369	18171
22372	18225
22372	18169
22372	18050
22372	18035
22372	18222
22372	18196
22375	18225
22375	18105
22375	18213
22375	18053
22375	18222
22375	18166
22375	18028
22375	18212
22378	18169
22384	18031
22384	18221
22384	18184
22384	18166
22384	18196
22384	18052
22384	18171
22387	18225
22387	18174
22387	18038
22387	18213
22387	18048
22387	18189
22387	18104
22387	18184
22387	18220
22387	18196
22387	18110
22387	18212
22390	18221
22390	18167
22390	18186
22390	18030
22390	18196
22390	18028
22396	18169
22396	18224
22396	18135
22396	18052
22399	18222
22399	18184
22399	18166
22399	18054
22399	18028
22399	18171
22405	18031
22405	18221
22405	18184
22405	18166
22405	18196
22405	18052
22405	18171
22408	18224
22408	18184
22408	18166
22408	18052
22408	18171
22411	18224
22411	18184
22411	18052
22411	18171
22414	18169
22414	18174
22414	18224
22414	18196
22414	18052
22414	18188
22417	18223
22417	18027
22417	18048
22417	18186
22417	18166
22417	18196
22417	18054
22417	18171
22420	18223
22420	18033
22420	18186
22420	18196
22420	18171
22423	18223
22423	18048
22423	18186
22423	18196
22423	18171
22429	18222
22429	18189
22429	18224
22429	18052
22429	18171
22432	18185
22432	18169
22432	18167
22432	18222
22432	18072
22435	18221
22435	18080
22435	18033
22435	18189
22435	18168
22435	18083
22435	18196
22435	18028
22435	18171
22444	18224
22444	18184
22444	18052
22444	18171
22447	18223
22447	18048
22447	18186
22447	18196
22447	18171
22450	18185
22450	18224
22450	18052
22450	18171
22453	18224
22453	18184
22453	18052
22453	18171
22456	18080
22456	18222
22456	18056
22456	18191
22456	18028
22456	18171
22459	18012
22459	18159
22459	18123
22459	18222
22459	18181
22459	18152
22459	18021
22459	18028
22459	18218
22459	18175
22459	18124
22462	18222
22462	18184
22462	18052
22462	18028
22462	18171
22465	18221
22465	18050
22465	18118
22465	18205
22465	18196
22465	18023
22468	18224
22468	18184
22468	18052
22468	18171
22474	18224
22474	18184
22474	18052
22474	18171
22477	18224
22477	18184
22477	18052
22477	18171
22480	18169
22480	18166
22483	18224
22483	18184
22483	18052
22483	18171
22486	18222
22486	18184
22486	18052
22486	18028
22486	18171
22498	18031
22498	18221
22498	18167
22498	18085
22498	18186
22498	18205
22498	18196
22498	18052
22498	18086
22498	18171
22501	18224
22501	18184
22501	18052
22501	18171
22507	18223
22507	18048
22507	18186
22507	18171
16152	18219
16152	18159
16152	18147
16152	18011
22523	18185
22523	18125
22523	18174
22523	18222
22523	18042
22523	18224
22523	18220
22523	18196
22523	18052
22523	18028
22523	18171
22526	18050
22526	18224
22526	18171
22532	18221
22532	18024
22532	18117
22532	18010
22532	18118
22532	18205
22532	18196
22541	18222
22541	18028
22544	18169
22547	18224
22547	18184
22547	18052
22547	18171
22550	18221
22550	18174
22550	18048
22550	18042
22550	18186
22550	18083
22550	18196
22550	18026
22550	18171
22553	18156
22553	18174
22553	18104
22553	18134
22553	18184
22553	18047
22553	18196
22553	18220
22553	18212
22553	18225
22553	18038
22553	18213
22553	18110
22568	18031
22568	18221
22568	18048
22568	18186
22568	18072
22568	18083
22568	18196
22568	18171
22571	18221
22571	18118
22571	18205
22571	18196
22574	18223
22574	18049
22574	18183
22574	18166
22574	18171
22577	18169
22577	18222
22577	18166
22577	18028
22586	18224
22586	18184
22586	18052
22586	18171
22598	18221
22598	18082
22598	18048
22598	18029
22598	18186
22598	18166
22598	18196
22598	18028
22598	18171
22601	18169
22601	18167
22613	18053
22613	18222
22613	18166
22613	18028
22619	18169
22622	18224
22622	18184
22622	18052
22622	18171
22625	18169
22625	18166
22628	18169
22628	18166
22631	18186
22631	18190
22634	18169
22634	18166
22640	18224
22640	18052
22640	18171
22649	18185
22649	18169
22649	18167
22652	18222
22652	18184
22652	18052
22652	18028
22652	18171
22655	18169
22655	18166
22661	18223
22661	18048
22661	18224
22661	18029
22661	18196
22661	18054
22661	18171
22664	18053
22664	18224
22664	18010
22664	18184
22664	18171
22667	18225
22667	18095
22667	18213
22667	18053
22667	18222
22667	18104
22667	18168
22667	18083
22667	18166
22667	18028
22667	18188
22670	18053
22670	18224
22670	18184
22670	18171
22673	18224
22673	18184
22673	18052
22673	18171
22676	18223
22676	18048
22676	18170
22676	18186
22676	18196
22676	18028
22679	18185
22679	18221
22679	18035
22679	18082
22679	18166
22679	18196
22682	18223
22682	18221
22682	18049
22682	18035
22682	18186
22682	18166
22682	18196
22685	18169
22685	18166
22688	18223
22688	18172
22688	18048
22688	18186
22688	18028
22688	18171
22694	18223
22694	18048
22694	18186
22694	18196
22694	18028
22694	18171
22697	18222
22697	18184
22697	18052
22697	18028
22697	18171
22700	18185
22700	18123
22700	18222
22700	18072
22700	18021
22703	18169
22703	18219
22703	18159
22703	18050
22703	18147
22703	18224
22703	18054
22703	18171
22703	18011
22706	18185
22706	18053
22706	18027
22706	18222
22706	18171
22718	18169
22718	18166
22721	18225
22721	18119
22721	18095
22721	18221
22721	18105
22721	18213
22721	18118
22721	18205
22721	18196
22721	18047
22721	18212
22727	18169
22727	18166
22730	18169
22730	18166
22733	18169
22733	18166
22736	18223
22736	18028
22739	18185
22739	18222
22739	18052
22739	18190
22739	18028
22739	18171
22742	18222
22742	18052
22742	18028
22742	18171
22745	18028
22748	18046
22748	18221
22748	18035
22748	18186
22748	18166
22748	18196
22748	18052
22748	18171
22754	18169
22757	18226
22757	18169
22757	18008
22757	18166
22760	18226
22760	18224
22760	18008
22760	18184
22760	18052
22760	18171
22760	18160
22763	18053
22763	18222
22763	18184
22763	18166
22763	18028
22763	18171
22766	18187
22766	18222
22766	18120
22766	18166
22766	18196
22766	18052
22766	18028
22766	18171
22769	18187
22769	18222
22769	18120
22769	18166
22769	18196
22769	18052
22769	18028
22769	18171
22772	18169
22772	18166
22775	18224
22775	18184
22775	18052
22775	18171
22778	18171
22781	18169
22781	18166
22787	18169
22787	18166
22790	18226
22790	18008
22790	18160
22793	18226
22793	18224
22793	18008
22793	18184
22793	18052
22793	18171
22793	18160
22796	18169
22796	18166
22805	18169
22805	18166
22811	18222
22811	18028
22817	18213
22817	18222
22817	18184
22817	18166
22817	18028
22817	18108
22820	18222
22820	18028
22820	18108
22823	18080
22823	18222
22823	18055
22823	18184
22823	18028
22823	18171
22826	18225
22826	18187
22826	18080
22826	18222
22826	18055
22826	18028
22826	18171
22829	18169
22838	18169
22838	18166
22841	18224
22841	18184
22841	18052
22841	18171
22844	18225
22844	18107
22844	18221
22844	18213
22844	18033
22844	18048
22844	18025
22844	18186
22844	18196
22844	18171
22847	18225
22847	18156
22847	18174
22847	18105
22847	18038
22847	18213
22847	18184
22847	18220
22847	18196
22847	18110
22847	18047
22847	18212
22850	18225
22850	18100
22850	18105
22850	18213
22850	18085
22850	18048
22850	18042
22850	18186
22850	18220
22850	18196
22850	18212
22853	18156
22853	18174
22853	18105
22853	18038
22853	18213
22853	18184
22853	18196
22853	18047
22853	18212
22856	18169
22856	18166
22859	18169
22859	18166
22862	18185
22862	18219
22862	18031
22862	18221
22862	18159
22862	18147
22862	18060
22862	18196
22862	18011
22865	18219
22865	18215
22865	18159
22865	18050
22865	18123
22865	18224
22865	18171
22865	18011
22865	18175
22865	18124
22868	18225
22868	18156
22868	18174
22868	18105
22868	18038
22868	18213
22868	18184
22868	18220
22868	18196
22868	18110
22868	18047
22868	18212
22871	18053
22871	18224
22871	18184
22871	18171
22874	18224
22874	18184
22874	18052
22874	18171
22877	18224
22877	18184
22877	18052
22877	18171
22880	18224
22880	18184
22880	18052
22880	18171
22883	18169
22883	18166
22886	18224
22886	18184
22886	18052
22886	18171
22889	18224
22889	18184
22889	18052
22889	18171
22892	18169
22892	18166
22895	18185
22895	18105
22895	18035
22895	18213
22895	18053
22895	18085
22895	18083
22895	18205
22895	18196
22895	18212
22898	18031
22898	18221
22898	18174
22898	18172
22898	18082
22898	18085
22898	18186
22898	18205
22898	18196
22898	18052
22898	18171
22901	18225
22901	18185
22901	18221
22901	18105
22901	18035
22901	18213
22901	18053
22901	18085
22901	18202
22901	18083
22901	18196
22901	18212
22904	18225
22904	18185
22904	18105
22904	18213
22904	18222
22904	18082
22904	18072
22904	18166
22904	18028
22904	18212
22907	18226
22907	18223
22907	18167
22907	18049
22907	18008
22907	18184
22907	18196
22907	18171
22910	18169
22910	18166
22913	18169
22913	18167
22916	18169
22919	18169
22922	18169
22922	18166
22925	18224
22925	18052
22928	18169
22928	18166
22931	18224
22931	18184
22931	18052
22931	18171
22934	18223
22934	18048
22934	18186
22934	18196
22934	18171
22940	18156
22940	18221
22940	18174
22940	18038
22940	18184
22940	18196
22940	18047
22946	18169
22946	18224
22946	18184
22946	18052
22946	18171
22961	18219
22961	18159
22961	18123
22961	18181
22961	18152
22961	18224
22961	18170
22961	18054
22961	18011
22961	18124
22967	18221
22967	18035
22967	18024
22967	18184
22967	18168
22967	18196
22967	18171
22970	18223
22970	18221
22970	18174
22970	18048
22970	18034
22970	18196
22973	18224
22973	18184
22973	18052
22973	18171
22976	18222
22976	18184
22976	18166
22976	18052
22976	18028
22976	18171
22979	18225
22979	18169
22979	18221
22979	18167
22979	18105
22979	18035
22979	18213
22979	18101
22979	18048
22979	18196
22979	18212
22979	18188
22982	18169
22982	18174
22982	18196
22982	18188
22985	18169
22985	18166
22988	18221
22988	18035
22988	18048
22988	18186
22988	18196
22988	18171
22991	18050
22991	18224
22991	18184
22991	18171
23057	18169
23057	18166
23057	18028
23060	18169
23060	18166
23066	18169
23066	18166
23072	18169
23072	18166
23075	18167
23075	18049
23075	18010
23075	18171
23078	18224
23078	18055
23078	18184
23078	18171
23081	18169
23081	18166
23090	18169
23090	18222
23090	18028
23096	18050
23096	18224
23096	18184
23096	18171
23102	18031
23102	18221
23102	18196
23102	18171
23105	18169
23105	18166
23108	18222
23108	18024
23108	18184
23108	18052
23108	18171
23111	18221
23111	18080
23111	18035
23111	18082
23111	18186
23111	18166
23111	18196
23111	18171
23114	18223
23114	18049
23114	18186
23114	18196
23114	18028
23114	18171
23117	18225
23117	18226
23117	18105
23117	18213
23117	18222
23117	18008
23117	18028
23117	18212
23123	18226
23123	18169
23123	18160
23126	18169
23132	18169
23132	18166
23138	18224
23138	18184
23138	18052
23138	18171
23141	18221
23141	18080
23141	18035
23141	18082
23141	18186
23141	18166
23141	18056
23141	18196
23141	18171
23144	18225
23144	18046
23144	18174
23144	18105
23144	18038
23144	18213
23144	18147
23144	18220
23144	18196
23144	18212
23144	18175
23147	18225
23147	18156
23147	18046
23147	18174
23147	18105
23147	18038
23147	18213
23147	18220
23147	18196
23147	18212
23153	18156
23153	18109
23153	18046
23153	18213
23153	18222
23153	18024
23159	18169
23159	18219
23159	18159
23159	18050
23159	18147
23159	18224
23159	18171
23159	18011
23159	18124
23168	18185
23168	18219
23168	18174
23168	18159
23168	18050
23168	18035
23168	18022
23168	18152
23168	18220
23168	18196
23168	18011
23168	18040
23171	18219
23171	18174
23171	18222
23171	18024
23171	18171
23171	18011
23177	18222
23177	18184
23177	18180
23177	18028
23186	18169
23186	18166
23189	18169
23189	18166
23192	18225
23192	18223
23192	18105
23192	18213
23192	18048
23192	18196
23192	18212
23192	18171
23195	18169
23195	18166
23198	18169
23201	18225
23201	18046
23201	18174
23201	18105
23201	18213
23201	18042
23201	18220
23201	18196
23201	18212
23204	18169
23207	18225
23207	18221
23207	18174
23207	18105
23207	18213
23207	18196
23207	18164
23207	18032
23207	18212
23210	18225
23210	18221
23210	18174
23210	18105
23210	18213
23210	18196
23210	18164
23210	18032
23210	18212
23213	18050
23213	18224
23213	18166
23213	18171
23216	18169
23222	18169
23222	18166
23225	18184
23225	18166
23225	18054
23225	18028
23225	18171
23228	18184
23228	18166
23228	18054
23228	18028
23228	18171
23231	18222
23231	18189
23231	18052
23231	18028
23231	18171
23234	18046
23234	18221
23234	18035
23234	18053
23234	18184
23234	18166
23234	18196
23237	18224
23237	18196
23237	18052
23237	18171
23240	18169
23240	18166
23243	18169
23249	18169
23249	18135
23255	18223
23255	18049
23255	18048
23255	18189
23255	18196
23255	18171
23258	18050
23258	18224
23258	18056
23258	18171
23261	18169
23261	18166
23264	18169
23264	18166
23267	18226
23267	18222
23267	18008
23267	18052
23267	18028
23267	18171
23270	18226
23270	18172
23270	18222
23270	18186
23270	18008
23270	18052
23270	18028
23270	18171
23270	18160
23273	18225
23273	18226
23273	18221
23273	18035
23273	18213
23273	18104
23273	18008
23273	18196
23276	18226
23276	18156
23276	18172
23276	18222
23276	18009
23276	18166
23276	18028
23279	18223
23279	18048
23279	18186
23279	18196
23279	18171
23282	18222
23282	18184
23282	18026
23282	18188
23285	18224
23285	18184
23285	18052
23285	18171
23297	18185
23297	18119
23297	18120
23300	18156
23300	18109
23300	18046
23300	18213
23300	18222
23300	18024
23303	18050
23303	18056
23303	18054
23306	18105
23306	18213
23306	18224
23306	18192
23306	18054
23306	18212
23315	18223
23315	18048
23315	18186
23315	18196
23315	18171
23318	18224
23318	18184
23318	18052
23318	18171
23321	18053
23321	18224
23321	18184
23321	18171
23324	18222
23324	18028
23327	18080
23327	18224
23327	18055
23327	18171
23327	18175
23330	18080
23330	18224
23330	18055
23330	18186
23330	18171
23333	18224
23333	18170
23333	18055
23336	18169
23336	18224
23336	18166
23342	18224
23342	18052
23342	18171
23345	18178
23345	18223
23345	18049
23345	18196
23345	18171
23351	18224
23351	18052
23351	18171
23354	18169
23354	18166
23357	18224
23357	18184
23357	18052
23357	18171
23360	18184
23360	18052
23360	18171
23363	18223
23363	18219
23363	18159
23363	18048
23363	18025
23363	18186
23363	18196
23363	18171
23363	18011
23366	18169
23366	18166
23369	18224
23369	18184
23369	18052
23369	18171
23372	18224
23372	18184
23372	18052
23372	18171
23375	18224
23375	18184
23375	18052
23375	18171
23378	18219
23378	18159
23378	18181
23378	18224
23378	18051
23378	18021
23378	18011
23378	18175
23381	18224
23381	18184
23381	18052
23381	18171
23387	18185
23387	18221
23387	18174
23387	18035
23387	18196
23387	18052
23387	18026
23387	18171
23390	18225
23390	18156
23390	18174
23390	18105
23390	18038
23390	18213
23390	18184
23390	18220
23390	18196
23390	18047
23390	18212
23393	18107
23393	18174
23393	18213
23393	18048
23393	18186
23393	18196
23393	18028
23393	18171
23396	18224
23396	18184
23396	18052
23396	18171
23399	18080
23399	18224
23399	18055
23399	18184
23399	18171
23402	18224
23402	18184
23402	18052
23402	18171
23408	18225
23408	18223
23408	18105
23408	18049
23408	18213
23408	18186
23408	18196
23408	18212
23408	18171
23414	18050
23414	18224
23414	18171
23417	18224
23417	18184
23417	18052
23417	18171
23420	18080
23420	18222
23420	18089
23420	18024
23420	18186
23420	18010
23420	18056
23420	18171
23423	18224
23423	18184
23423	18052
23423	18171
23426	18172
23426	18189
23426	18224
23426	18052
23426	18171
23429	18224
23429	18184
23429	18052
23429	18171
23432	18225
23432	18095
23432	18221
23432	18105
23432	18213
23432	18072
23432	18118
23432	18205
23432	18196
23432	18047
23432	18212
23435	18224
23435	18184
23435	18052
23435	18171
23441	18224
23441	18184
23441	18052
23441	18171
23444	18169
23444	18166
23447	18080
23447	18222
23447	18224
23447	18168
23447	18191
23447	18028
23447	18171
23450	18080
23450	18189
23450	18224
23450	18055
23450	18171
23465	18185
23465	18224
23465	18083
23468	18169
23468	18222
23468	18166
23474	18224
23474	18184
23474	18052
23474	18171
23477	18169
23477	18224
23477	18166
23477	18052
23483	18224
23483	18184
23483	18052
23483	18171
23486	18224
23486	18184
23486	18052
23486	18171
23489	18189
23489	18224
23489	18052
23489	18171
23492	18050
23492	18222
23492	18184
23492	18028
23492	18171
23495	18050
23495	18224
23495	18184
23495	18171
23498	18225
23498	18223
23498	18105
23498	18213
23498	18048
23498	18186
23498	18196
23498	18028
23498	18212
23498	18171
23501	18225
23501	18105
23501	18213
23501	18053
23501	18224
23501	18170
23501	18166
23501	18212
23504	18225
23504	18050
23504	18224
23504	18056
23504	18054
\.


--
-- Data for Name: sam_crystalsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_crystalsample (a, alpha, b, beta, c, colour, crystaltype, gamma, morphology, spacegroup, x, y, z, sampleid) FROM stdin;
\.


--
-- Data for Name: sam_refsample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_refsample (issaltcrystal, abstractsampleid) FROM stdin;
\N	16001
\N	16003
\N	16005
\N	16007
\N	16009
\N	16011
\N	16013
\N	16015
\N	16017
\N	16019
\N	16022
\N	16024
\N	16026
\N	16028
\N	16030
\N	16032
\N	16033
\N	16035
\N	16037
\N	16039
\N	16041
\N	16043
\N	16045
\N	16047
\N	16049
\N	16051
\N	16053
\N	16055
\N	16057
\N	16059
\N	16061
\N	16062
\N	16063
\N	16065
\N	16067
\N	16069
\N	16071
\N	16073
\N	16075
\N	16077
\N	16079
\N	16081
\N	16083
\N	16085
\N	16087
\N	16089
\N	16091
\N	16093
\N	16095
\N	16097
\N	16099
\N	16101
\N	16103
\N	16105
\N	16107
\N	16109
\N	16111
\N	16113
\N	16115
\N	16117
\N	16119
\N	16121
\N	16123
\N	16125
\N	16127
\N	16129
\N	16131
\N	16132
\N	16134
\N	16136
\N	16138
\N	16140
\N	16142
\N	16143
\N	16144
\N	16145
\N	16146
\N	16147
\N	16148
\N	16149
\N	16150
\N	16151
\N	16152
\N	16153
\N	16154
\N	16155
\N	16156
\N	16157
\N	16159
\N	16161
\N	16163
\N	16165
\N	16166
\N	22002
\N	22005
\N	22008
\N	22011
\N	22014
\N	22017
\N	22020
\N	22023
\N	22026
\N	22029
\N	22032
\N	22035
\N	22038
\N	22041
\N	22044
\N	22047
\N	22050
\N	22053
\N	22056
\N	22059
\N	22062
\N	22065
\N	22068
\N	22071
\N	22074
\N	22077
\N	22080
\N	22083
\N	22086
\N	22089
\N	22092
\N	22095
\N	22098
\N	22101
\N	22104
\N	22107
\N	22110
\N	22113
\N	22116
\N	22119
\N	22122
\N	22125
\N	22128
\N	22131
\N	22134
\N	22137
\N	22140
\N	22143
\N	22146
\N	22149
\N	22152
\N	22155
\N	22158
\N	22161
\N	22164
\N	22167
\N	22170
\N	22174
\N	22177
\N	22180
\N	22183
\N	22186
\N	22189
\N	22192
\N	22195
\N	22198
\N	22201
\N	22204
\N	22207
\N	22210
\N	22213
\N	22216
\N	22219
\N	22222
\N	22225
\N	22228
\N	22231
\N	22234
\N	22237
\N	22240
\N	22243
\N	22246
\N	22249
\N	22252
\N	22255
\N	22258
\N	22261
\N	22264
\N	22267
\N	22270
\N	22273
\N	22276
\N	22279
\N	22282
\N	22285
\N	22288
\N	22291
\N	22294
\N	22297
\N	22300
\N	22303
\N	22306
\N	22309
\N	22312
\N	22315
\N	22318
\N	22321
\N	22324
\N	22327
\N	22330
\N	22333
\N	22336
\N	22339
\N	22342
\N	22345
\N	22348
\N	22351
\N	22354
\N	22357
\N	22360
\N	22363
\N	22366
\N	22369
\N	22372
\N	22375
\N	22378
\N	22381
\N	22384
\N	22387
\N	22390
\N	22393
\N	22396
\N	22399
\N	22402
\N	22405
\N	22408
\N	22411
\N	22414
\N	22417
\N	22420
\N	22423
\N	22426
\N	22429
\N	22432
\N	22435
\N	22438
\N	22441
\N	22444
\N	22447
\N	22450
\N	22453
\N	22456
\N	22459
\N	22462
\N	22465
\N	22468
\N	22471
\N	22474
\N	22477
\N	22480
\N	22483
\N	22486
\N	22489
\N	22492
\N	22495
\N	22498
\N	22501
\N	22504
\N	22507
\N	22510
\N	22513
\N	22516
\N	22520
\N	22523
\N	22526
\N	22529
\N	22532
\N	22535
\N	22538
\N	22541
\N	22544
\N	22547
\N	22550
\N	22553
\N	22556
\N	22559
\N	22562
\N	22565
\N	22568
\N	22571
\N	22574
\N	22577
\N	22580
\N	22583
\N	22586
\N	22589
\N	22592
\N	22595
\N	22598
\N	22601
\N	22604
\N	22607
\N	22610
\N	22613
\N	22616
\N	22619
\N	22622
\N	22625
\N	22628
\N	22631
\N	22634
\N	22637
\N	22640
\N	22643
\N	22646
\N	22649
\N	22652
\N	22655
\N	22658
\N	22661
\N	22664
\N	22667
\N	22670
\N	22673
\N	22676
\N	22679
\N	22682
\N	22685
\N	22688
\N	22691
\N	22694
\N	22697
\N	22700
\N	22703
\N	22706
\N	22709
\N	22712
\N	22715
\N	22718
\N	22721
\N	22724
\N	22727
\N	22730
\N	22733
\N	22736
\N	22739
\N	22742
\N	22745
\N	22748
\N	22751
\N	22754
\N	22757
\N	22760
\N	22763
\N	22766
\N	22769
\N	22772
\N	22775
\N	22778
\N	22781
\N	22784
\N	22787
\N	22790
\N	22793
\N	22796
\N	22799
\N	22802
\N	22805
\N	22808
\N	22811
\N	22814
\N	22817
\N	22820
\N	22823
\N	22826
\N	22829
\N	22832
\N	22835
\N	22838
\N	22841
\N	22844
\N	22847
\N	22850
\N	22853
\N	22856
\N	22859
\N	22862
\N	22865
\N	22868
\N	22871
\N	22874
\N	22877
\N	22880
\N	22883
\N	22886
\N	22889
\N	22892
\N	22895
\N	22898
\N	22901
\N	22904
\N	22907
\N	22910
\N	22913
\N	22916
\N	22919
\N	22922
\N	22925
\N	22928
\N	22931
\N	22934
\N	22937
\N	22940
\N	22943
\N	22946
\N	22949
\N	22952
\N	22955
\N	22958
\N	22961
\N	22964
\N	22967
\N	22970
\N	22973
\N	22976
\N	22979
\N	22982
\N	22985
\N	22988
\N	22991
\N	22994
\N	22997
\N	23000
\N	23003
\N	23006
\N	23009
\N	23012
\N	23015
\N	23018
\N	23021
\N	23024
\N	23027
\N	23030
\N	23033
\N	23036
\N	23039
\N	23042
\N	23045
\N	23048
\N	23051
\N	23054
\N	23057
\N	23060
\N	23063
\N	23066
\N	23069
\N	23072
\N	23075
\N	23078
\N	23081
\N	23084
\N	23087
\N	23090
\N	23093
\N	23096
\N	23099
\N	23102
\N	23105
\N	23108
\N	23111
\N	23114
\N	23117
\N	23120
\N	23123
\N	23126
\N	23129
\N	23132
\N	23135
\N	23138
\N	23141
\N	23144
\N	23147
\N	23150
\N	23153
\N	23156
\N	23159
\N	23162
\N	23165
\N	23168
\N	23171
\N	23174
\N	23177
\N	23180
\N	23183
\N	23186
\N	23189
\N	23192
\N	23195
\N	23198
\N	23201
\N	23204
\N	23207
\N	23210
\N	23213
\N	23216
\N	23219
\N	23222
\N	23225
\N	23228
\N	23231
\N	23234
\N	23237
\N	23240
\N	23243
\N	23246
\N	23249
\N	23252
\N	23255
\N	23258
\N	23261
\N	23264
\N	23267
\N	23270
\N	23273
\N	23276
\N	23279
\N	23282
\N	23285
\N	23288
\N	23291
\N	23294
\N	23297
\N	23300
\N	23303
\N	23306
\N	23309
\N	23312
\N	23315
\N	23318
\N	23321
\N	23324
\N	23327
\N	23330
\N	23333
\N	23336
\N	23339
\N	23342
\N	23345
\N	23348
\N	23351
\N	23354
\N	23357
\N	23360
\N	23363
\N	23366
\N	23369
\N	23372
\N	23375
\N	23378
\N	23381
\N	23384
\N	23387
\N	23390
\N	23393
\N	23396
\N	23399
\N	23402
\N	23405
\N	23408
\N	23411
\N	23414
\N	23417
\N	23420
\N	23423
\N	23426
\N	23429
\N	23432
\N	23435
\N	23438
\N	23441
\N	23444
\N	23447
\N	23450
\N	23453
\N	23456
\N	23459
\N	23462
\N	23465
\N	23468
\N	23471
\N	23474
\N	23477
\N	23480
\N	23483
\N	23486
\N	23489
\N	23492
\N	23495
\N	23498
\N	23501
\N	23504
\N	36001
\N	36013
\N	36027
\N	36040
\N	36054
\N	36063
\N	36074
\N	36086
\N	36099
\N	36111
\N	36117
\N	36123
\N	36129
\N	36135
\N	36142
\N	36148
\N	36154
\N	36160
\N	36166
\N	36172
\N	36178
\N	36188
\.


--
-- Data for Name: sam_refsamplesource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_refsamplesource (catalognum, datapageurl, productgroupname, productname, labbookentryid, refholderid, refsampleid, supplierid) FROM stdin;
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16002	\N	16001	14005
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16004	\N	16003	14005
71087	http://www.merckbiosciences.co.uk/product/71087	\N	\N	16006	\N	16005	14005
70099	http://www.emdbiosciences.com/product/70099	\N	\N	16008	\N	16007	14005
M0373	http://www.neb.com/nebecomm/products_Intl/productM0273.asp	\N	\N	16010	\N	16009	14004
M0254	http://www.neb.com/nebecomm/products_Intl/productM0254.asp	\N	\N	16012	\N	16011	14004
M0257	http://www.neb.com/nebecomm/products_Intl/productM0257.asp	\N	\N	16014	\N	16013	14004
M0258	http://www.neb.com/nebecomm/products_Intl/productM0258.asp	\N	\N	16016	\N	16015	14004
M7741	http://www.promega.com/tbs/9pim210/9pim210.html	\N	\N	16018	\N	16017	14006
M1801	http://www.promega.com/tbs/9pim774/9pim774.html	\N	\N	16020	\N	16019	14006
600136	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600136	\N	\N	16021	\N	16017	14009
600250	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600250	\N	\N	16023	\N	16022	14009
600410	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600410	\N	\N	16025	\N	16024	14009
600380	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=600380	\N	\N	16027	\N	16026	14009
BIO-25025	http://www.bioline.com/h_prod_detail.asp?user_prodname=BIO%2DX%2DACT%99+Short+Mix	\N	\N	16029	\N	16028	14001
R0581S	http://www.neb.uk.com/frameset.asp?searchType=products&yider=BseRI&submit.x=0&submit.y=0	\N	\N	16031	\N	16030	14004
R0581S	http://www.neb.uk.com/frameset.asp?searchType=products&yider=BseRI&submit.x=0&submit.y=0	\N	\N	16034	\N	16033	14004
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16036	\N	16035	14005
71085	http://www.merckbiosciences.co.uk/product/71085	\N	\N	16038	\N	16037	14005
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16040	\N	16039	14005
BIO-25025	http://www.bioline.com/h_prod_detail.asp?user_prodname=BIO%2DX%2DACT%99+Short+Mix	\N	\N	16042	\N	16041	14001
BIO-33025	http://www.bioline.com/n_uk.htm	\N	\N	16044	\N	16043	14001
BIO-33039	http://www.bioline.com/n_uk.htm	\N	\N	16046	\N	16045	14001
G5711	http://www.promega.com/techserv/apps/cloning/cloning4.htm	\N	\N	16048	\N	16047	14006
G2101	http://www.promega.com/techserv/apps/cloning/cloning4.htm	\N	\N	16050	\N	16049	14006
BIO-39036	http://www.bioline.com/n_uk.htm	\N	\N	16052	\N	16051	14001
BIO-39038	http://www.bioline.com/n_uk.htm	\N	\N	16054	\N	16053	14001
BIO-39037	http://www.bioline.com/n_uk.htm	\N	\N	16056	\N	16055	14001
BIO-39039	http://www.bioline.com/n_uk.htm	\N	\N	16058	\N	16057	14001
U1511	http://www.promega.com/search/Default.aspx?indexes=0&page=1&query=U1511	\N	\N	16060	\N	16059	14006
71086	http://www.merckbiosciences.co.uk/product/71086	\N	\N	16064	\N	16063	14005
BIO-39028	http://www.bioline.com/h_prod_detail.asp?user_prodname=dNTP+Mix	\N	\N	16066	\N	16065	14001
71146	http://www.emdbiosciences.com/product/71146	\N	\N	16068	\N	16067	14005
12535	http://www.invitrogen.com/search.cfm?category=&searchterm=12535	\N	\N	16070	\N	16069	14002
K2700	http://www.invitrogen.com/search.cfm?category=&searchterm=K2700	\N	\N	16072	\N	16071	14002
K4500	http://www.invitrogen.com/search.cfm?category=&searchterm=K4500	\N	\N	16074	\N	16073	14002
K2800	http://www.invitrogen.com/search.cfm?category=&searchterm=K2800	\N	\N	16076	\N	16075	14002
639605	http://www.clontech.com/products/detail.asp?product_id=10406&tabno=2	\N	\N	16078	\N	16077	14031
200518	http://search.stratagene.com/?index=19012&lastq=&doc0=0&sortsel=rel&opt=ANY&query=200518	\N	\N	16080	\N	16079	14009
70181	http://www.emdbiosciences.com/product/70181	\N	\N	16082	\N	16081	14005
71227	http://www.emdbiosciences.com/product/71227	\N	\N	16084	\N	16083	14005
71012	http://www.emdbiosciences.com/product/71012	\N	\N	16086	\N	16085	14005
71011	http://www.emdbiosciences.com/product/71011	\N	\N	16088	\N	16087	14005
71400	http://www.emdbiosciences.com/product/71400	\N	\N	16090	\N	16089	14005
70630	http://www.emdbiosciences.com/product/70630	\N	\N	16092	\N	16091	14005
71350	http://www.emdbiosciences.com/product/71350	\N	\N	16094	\N	16093	14005
69449	http://www.emdbiosciences.com/product/69449	\N	\N	16096	\N	16095	14005
71207	http://www.emdbiosciences.com/product/71207	\N	\N	16098	\N	16097	14005
69041	http://www.emdbiosciences.com/product/69041	\N	\N	16100	\N	16099	14005
200249	http://www.stratagene.com/manuals/200314.pdf	\N	\N	16102	\N	16101	14009
200315	http://www.stratagene.com/manuals/200314.pdf	\N	\N	16104	\N	16103	14009
230130	http://www.stratagene.com/manuals/230130.pdf	\N	\N	16106	\N	16105	14009
230240	http://www.stratagene.com/manuals/230240.pdf	\N	\N	16108	\N	16107	14009
C4040	http://www.invitrogen.com/search.cfm?category=&searchterm=C4040	\N	\N	16110	\N	16109	14002
12034-013	http://www.invitrogen.com/search.cfm?category=&searchterm=12034	\N	\N	16112	\N	16111	14002
11596	http://www.activemotif.com/catalog/transfect_expr/rapidtrans	\N	\N	16114	\N	16113	14032
440011	http://www.invitrogen.com	\N	\N	16116	\N	16115	14002
28104	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=28104	\N	\N	16118	\N	16117	14008
12123	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=12123	\N	\N	16120	\N	16119	14008
12143	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=12143	\N	\N	16122	\N	16121	14008
28181	http://www1.qiagen.com/Search/search.aspx?category=1&SearchTerm=28181	\N	\N	16124	\N	16123	14008
130	http://www.agencourt.com/products/spri_reagents/ampure/	\N	\N	16126	\N	16125	14033
3101	http://www.qbiogene.com/search/index.shtml	\N	\N	16128	\N	16127	14007
3031	http://www.qbiogene.com/search/index.shtml	\N	\N	16130	\N	16129	14007
71300	http://www.emdbiosciences.com/product/71300	\N	\N	16133	\N	16132	14005
MD12-501	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16135	\N	16134	14003
MD12-503	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16137	\N	16136	14003
MD12-504	http://www.moleculardimensions.com/uk/search.ihtml	\N	\N	16139	\N	16138	14003
MD12-106-6	http://www.moleculardimensions.com	\N	\N	16141	\N	16140	14003
Unknown	http://www.invitrogen.com/content.cfm?pageid=9714&CID=TN-Prods-Oligos	\N	\N	16158	\N	16157	14002
Unknown	http://www.invitrogen.com/content.cfm?pageid=9714&CID=TN-Prods-Oligos	\N	\N	16160	\N	16159	14002
Unknown	https://www.operon.com/products_main.php	\N	\N	16162	\N	16161	14029
Unknown	http://www.mwg-biotech.com/html/s_synthesis/s_overview.shtml	\N	\N	16164	\N	16163	14030
69743-3	http://www.merckbiosciences.co.uk/product/69743	\N	\N	36002	\N	36001	14005
11803-012	http://products.invitrogen.com/ivgn/en/US/adirect/invitrogen?cmd=catProductDetail&productID=11803012	\N	\N	36015	\N	36013	36014
69862-3	http://www.merckbiosciences.co.uk/product/69862	\N	\N	36028	\N	36027	14005
69864-3	http://www.merckbiosciences.co.uk/product/69864	\N	\N	36041	\N	36040	14005
unknown	\N	\N	\N	36056	\N	36054	36055
11803-012	http://products.invitrogen.com/ivgn/en/US/adirect/invitrogen	\N	\N	36064	\N	36063	36014
11803-012	http://products.invitrogen.com/ivgn/en/US/adirect/invitrogen?cmd=catProductDetail&productID=12536017	\N	\N	36075	\N	36074	36014
K2700-20	http://products.invitrogen.com/ivgn/en/US/adirect/invitrogen?cmd=catProductDetail&productID=K270020	\N	\N	36087	\N	36086	36014
69661-3	http://www.merckbiosciences.co.uk/product/69661	\N	\N	36100	\N	36099	14005
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36112	\N	36111	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36118	\N	36117	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36124	\N	36123	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36130	\N	36129	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36136	\N	36135	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36143	\N	36142	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36149	\N	36148	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36155	\N	36154	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36161	\N	36160	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36167	\N	36166	36055
unknown	http://www.oppf.ox.ac.uk/OPPF/public/services/cloning.jsp	\N	\N	36173	\N	36172	36055
37673	http://www.lgcstandards-atcc.org/LGCAdvancedCatalogueSearch/ProductDescription/tabid/1068/Default.aspx?ATCCNum=37673&Template=vectors	\N	\N	36179	\N	36178	14034
70826-3	http://www.merckbiosciences.co.uk/product/70826	\N	\N	36189	\N	36188	14005
\.


--
-- Data for Name: sam_sampca2abstsa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sampca2abstsa (abstractsampleid, samplecategoryid) FROM stdin;
16001	10017
16003	10017
16005	10017
16007	10017
16009	10017
16011	10017
16013	10017
16015	10017
16017	10017
16019	10018
16022	10017
16024	10017
16026	10017
16028	10017
16030	10015
16032	10004
16033	10004
16035	10004
16037	10004
16039	10004
16041	10004
16043	10027
16045	10027
16047	10027
16049	10027
16051	10035
16053	10035
16055	10035
16057	10035
16059	10035
16061	10035
16062	10035
16063	10035
16065	10035
16067	10053
16069	10009
16071	10009
16073	10009
16075	10009
16077	10009
16079	10057
16081	10011
16083	10011
16085	10011
16087	10011
16089	10011
16091	10011
16093	10011
16095	10011
16097	10011
16099	10011
16101	10011
16103	10011
16105	10011
16107	10011
16109	10011
16111	10011
16113	10011
16115	10011
16117	10033
16119	10033
16121	10033
16123	10033
16125	10033
16127	10012
16129	10012
16131	10012
16132	10012
16134	10012
16136	10012
16138	10012
16140	10012
16142	10048
16143	10049
16145	10048
16146	10048
16147	10004
16148	10002
16149	10002
16150	10002
16151	10002
16153	10049
16154	10049
16155	10049
16156	10004
16157	10037
16159	10037
16161	10037
16163	10037
16165	10020
16166	10043
22002	10004
22002	10007
22002	10005
22005	10007
22005	10005
22008	10049
22011	10049
22014	10004
22014	10007
22014	10005
22017	10050
22020	10014
22023	10014
22026	10007
22026	10035
22029	10007
22029	10035
22032	10007
22035	10007
22038	10007
22041	10007
22044	10007
22044	10024
22047	10007
22047	10024
22050	10004
22050	10007
22050	10005
22053	10007
22056	10007
22059	10007
22062	10007
22065	10007
22068	10007
22071	10004
22071	10007
22071	10005
22074	10007
22077	10007
22080	10007
22083	10007
22086	10007
22089	10007
22092	10007
22092	10014
22095	10007
22098	10007
22101	10007
22104	10007
22107	10007
22110	10007
22113	10007
22113	10002
22116	10004
22116	10007
22116	10005
22119	10007
22122	10050
22125	10007
22128	10054
22131	10007
22134	10007
22137	10007
22137	10024
22140	10007
22140	10024
22143	10007
22146	10007
22149	10007
22152	10007
22155	10007
22155	10024
22158	10007
22158	10024
22161	10007
22164	10007
22167	10004
22167	10007
22167	10005
22170	10007
16144	10007
22174	10007
22177	10007
22177	10024
22180	10004
22180	10007
22180	10005
22183	10013
22186	10054
22189	10054
22192	10007
22195	10004
22195	10007
22195	10005
22198	10004
22198	10007
22198	10005
22201	10004
22201	10007
22201	10005
22204	10007
22207	10050
22210	10050
22213	10050
22216	10007
22219	10013
22222	10007
22225	10049
22228	10007
22231	10007
22234	10007
22237	10007
22237	10049
22240	10007
22240	10049
22243	10007
22246	10007
22246	10024
22249	10007
22252	10013
22255	10004
22255	10007
22255	10005
22258	10007
22261	10007
22264	10007
22267	10007
22270	10007
22273	10007
22276	10007
22279	10007
22282	10007
22285	10007
22288	10007
22291	10007
22294	10007
22297	10007
22300	10004
22300	10007
22300	10005
22303	10004
22303	10007
22303	10005
22306	10007
22306	10002
22309	10007
22312	10007
22315	10007
22318	10007
22321	10007
22324	10013
22327	10013
22330	10004
22330	10007
22330	10005
22333	10007
22336	10007
22336	10002
22339	10049
22342	10052
22345	10050
22348	10026
22351	10013
22354	10007
22357	10007
22360	10007
22363	10007
22366	10007
22369	10007
22372	10007
22375	10007
22378	10052
22381	10007
22384	10007
22384	10035
22387	10007
22390	10007
22393	10013
22396	10013
22399	10007
22399	10013
22402	10007
22402	10035
22405	10007
22405	10035
22408	10007
22408	10035
22411	10007
22411	10035
22414	10007
22417	10007
22420	10007
22423	10007
22426	10007
22426	10022
22429	10007
22429	10022
22432	10007
22435	10007
22438	10013
22441	10026
22444	10013
22447	10013
22450	10013
22453	10013
22456	10007
22456	10024
22459	10007
22459	10049
22462	10007
22465	10007
22465	10049
22468	10007
22471	10007
22471	10049
22474	10007
22477	10007
22480	10004
22480	10007
22480	10005
22483	10007
22486	10007
22489	10026
22492	10013
22495	10026
22498	10026
22501	10007
22501	10049
22504	10007
22504	10013
22507	10013
22510	10026
22513	10007
22516	10007
16152	10007
16152	10049
22520	10007
22523	10050
22526	10007
22529	10007
22532	10007
22535	10007
22538	10007
22541	10007
22544	10007
22547	10004
22547	10007
22547	10005
22550	10007
22553	10007
22553	10022
22556	10007
22559	10007
22562	10007
22565	10007
22568	10007
22571	10007
22571	10049
22574	10007
22577	10007
22577	10024
22577	10002
22580	10007
22583	10007
22586	10007
22589	10007
22592	10007
22595	10007
22598	10007
22601	10007
22604	10004
22604	10007
22604	10005
22607	10007
22610	10007
22613	10007
22616	10007
22616	10035
22619	10007
22619	10035
22622	10004
22622	10007
22622	10005
22625	10004
22625	10007
22625	10005
22628	10004
22628	10007
22628	10005
22631	10004
22631	10007
22631	10005
22634	10007
22634	10001
22637	10013
22640	10007
22643	10007
22646	10007
22646	10008
22649	10007
22652	10007
22655	10007
22658	10013
22661	10007
22664	10007
22664	10049
22667	10007
22667	10024
22670	10007
22670	10024
22673	10007
22676	10004
22676	10007
22676	10005
22679	10007
22679	10024
22682	10007
22682	10024
22685	10050
22688	10007
22691	10007
22694	10007
22697	10007
22700	10007
22703	10007
22703	10049
22706	10007
22709	10007
22712	10007
22712	10002
22715	10007
22718	10007
22718	10013
22721	10007
22721	10022
22724	10007
22724	10022
22727	10007
22727	10024
22730	10007
22733	10007
22736	10007
22739	10007
22742	10007
22745	10007
22748	10007
22751	10007
22754	10007
22757	10007
22760	10007
22763	10007
22766	10007
22769	10007
22772	10007
22775	10007
22778	10007
22781	10007
22784	10007
22787	10007
22790	10007
22793	10007
22796	10007
22799	10007
22802	10007
22805	10007
22808	10007
22811	10007
22814	10007
22817	10007
22820	10007
22823	10007
22826	10007
22829	10007
22832	10007
22835	10007
22838	10007
22841	10007
22844	10007
22847	10007
22847	10022
22850	10007
22850	10022
22853	10007
22853	10022
22856	10004
22856	10007
22856	10005
22859	10004
22859	10007
22859	10005
22862	10007
22862	10049
22865	10007
22868	10007
22868	10022
22871	10007
22874	10004
22874	10007
22874	10005
22877	10004
22877	10007
22877	10005
22880	10004
22880	10007
22880	10005
22883	10007
22886	10007
22886	10035
22889	10007
22889	10035
22892	10007
22895	10007
22898	10007
22901	10007
22904	10007
22907	10007
22910	10013
22913	10013
22916	10013
22919	10013
22922	10013
22925	10007
22925	10013
22928	10013
22931	10007
22931	10013
22934	10004
22934	10007
22934	10005
22937	10007
22940	10007
22940	10024
22940	10022
22943	10007
22943	10024
22943	10022
22946	10050
22949	10007
22952	10007
22955	10013
22958	10013
22961	10007
22964	10007
22964	10024
22967	10007
22967	10024
22970	10007
22973	10050
22976	10007
22979	10007
22979	10022
22982	10026
22985	10004
22985	10007
22985	10005
22988	10007
22988	10024
22991	10007
22994	10007
22997	10007
23000	10007
23003	10007
23006	10007
23009	10007
23012	10007
23015	10007
23018	10007
23021	10007
23024	10007
23027	10007
23030	10007
23033	10007
23036	10007
23039	10007
23042	10007
23045	10007
23048	10007
23051	10007
23051	10013
23054	10007
23057	10007
23057	10002
23060	10032
23060	10007
23063	10007
23066	10007
23069	10026
23072	10004
23072	10007
23072	10005
23075	10007
23078	10007
23081	10007
23084	10007
23087	10007
23090	10007
23093	10007
23096	10007
23099	10007
23102	10007
23105	10007
23108	10007
23108	10022
23111	10007
23111	10022
23114	10007
23117	10007
23120	10007
23123	10007
23126	10007
23129	10007
23132	10007
23135	10007
23138	10007
23138	10022
23141	10007
23141	10022
23144	10007
23144	10022
23147	10007
23147	10022
23150	10007
23150	10022
23153	10007
23156	10007
23159	10007
23159	10049
23162	10007
23165	10007
23168	10007
23171	10007
23174	10007
23177	10007
23180	10007
23183	10054
23186	10007
23189	10007
23192	10007
23192	10022
23195	10004
23195	10007
23198	10004
23198	10007
23198	10005
23201	10007
23201	10024
23204	10007
23207	10007
23210	10004
23210	10007
23210	10005
23213	10004
23213	10007
23213	10005
23216	10007
23219	10004
23219	10007
23219	10005
23222	10004
23222	10007
23222	10005
23225	10013
23228	10013
23231	10013
23234	10007
23237	10007
23237	10049
23240	10007
23240	10013
23243	10007
23246	10004
23246	10007
23246	10005
23249	10004
23249	10007
23249	10005
23252	10004
23252	10007
23252	10005
23255	10007
23258	10007
23261	10007
23264	10007
23267	10007
23270	10007
23273	10007
23276	10007
23279	10007
23282	10007
23282	10005
23285	10007
23285	10005
23288	10007
23291	10007
23294	10007
23297	10007
23300	10007
23303	10007
23306	10007
23309	10026
23312	10007
23315	10007
23315	10024
23318	10007
23318	10024
23321	10007
23324	10007
23324	10002
23327	10007
23330	10007
23333	10007
23336	10007
23339	10007
23342	10007
23345	10007
23348	10004
23348	10007
23348	10005
23351	10004
23351	10007
23351	10005
23354	10004
23354	10007
23354	10005
23357	10004
23357	10007
23357	10005
23360	10007
23363	10007
23366	10004
23366	10007
23366	10005
23369	10007
23369	10002
23372	10013
23375	10013
23378	10007
23378	10049
23381	10007
23384	10007
23384	10022
23387	10007
23390	10007
23390	10022
23393	10007
23396	10007
23396	10024
23399	10007
23399	10024
23402	10007
23405	10007
23408	10007
23411	10004
23411	10007
23411	10005
23414	10004
23414	10007
23414	10049
23417	10007
23420	10049
23423	10007
23426	10007
23429	10007
23432	10007
23432	10022
23435	10050
23438	10004
23438	10007
23438	10005
23441	10004
23441	10007
23441	10005
23444	10007
23447	10013
23450	10013
23453	10007
23453	10024
23456	10007
23456	10024
23459	10013
23462	10007
23465	10007
23465	10002
23468	10054
23471	10052
23474	10050
23477	10050
23480	10007
23483	10007
23483	10022
23486	10007
23489	10007
23492	10007
23495	10007
23498	10007
23501	10007
23504	10007
36001	10053
36013	10053
36027	10053
36040	10053
36054	10053
36063	10053
36074	10053
36086	10053
36099	10053
36111	10053
36117	10053
36123	10053
36129	10053
36135	10053
36142	10053
36148	10053
36154	10053
36160	10053
36166	10053
36172	10053
36178	10053
36188	10053
\.


--
-- Data for Name: sam_sample; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_sample (amountdisplayunit, amountunit, batchnum, colposition, currentamount, currentamountflag, initialamount, rowposition, subposition, usebydate, abstractsampleid, assigntoid, holderid, refsampleid) FROM stdin;
\.


--
-- Data for Name: sam_samplecomponent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sam_samplecomponent (concdisplayunit, concentration, concentrationerror, concentrationunit, ph, purity, labbookentryid, abstractsampleid, containerid, refcomponentid, researchobjectivelementid) FROM stdin;
\N	\N	\N	\N	\N	\N	22003	22002	\N	22001	\N
\N	\N	\N	\N	\N	\N	22006	22005	\N	22004	\N
\N	\N	\N	\N	\N	\N	22009	22008	\N	22007	\N
\N	\N	\N	\N	\N	\N	22012	22011	\N	22010	\N
\N	\N	\N	\N	\N	\N	22015	22014	\N	22013	\N
\N	\N	\N	\N	\N	\N	22018	22017	\N	22016	\N
\N	\N	\N	\N	\N	\N	22021	22020	\N	22019	\N
\N	\N	\N	\N	\N	\N	22024	22023	\N	22022	\N
\N	\N	\N	\N	\N	\N	22027	22026	\N	22025	\N
\N	\N	\N	\N	\N	\N	22030	22029	\N	22028	\N
\N	\N	\N	\N	\N	\N	22033	22032	\N	22031	\N
\N	\N	\N	\N	\N	\N	22036	22035	\N	22034	\N
\N	\N	\N	\N	\N	\N	22039	22038	\N	22037	\N
\N	\N	\N	\N	\N	\N	22042	22041	\N	22040	\N
\N	\N	\N	\N	\N	\N	22045	22044	\N	22043	\N
\N	\N	\N	\N	\N	\N	22048	22047	\N	22046	\N
\N	\N	\N	\N	\N	\N	22051	22050	\N	22049	\N
\N	\N	\N	\N	\N	\N	22054	22053	\N	22052	\N
\N	\N	\N	\N	\N	\N	22057	22056	\N	22055	\N
\N	\N	\N	\N	\N	\N	22060	22059	\N	22058	\N
\N	\N	\N	\N	\N	\N	22063	22062	\N	22061	\N
\N	\N	\N	\N	\N	\N	22066	22065	\N	22064	\N
\N	\N	\N	\N	\N	\N	22069	22068	\N	22067	\N
\N	\N	\N	\N	\N	\N	22072	22071	\N	22070	\N
\N	\N	\N	\N	\N	\N	22075	22074	\N	22073	\N
\N	\N	\N	\N	\N	\N	22078	22077	\N	22076	\N
\N	\N	\N	\N	\N	\N	22081	22080	\N	22079	\N
\N	\N	\N	\N	\N	\N	22084	22083	\N	22082	\N
\N	\N	\N	\N	\N	\N	22087	22086	\N	22085	\N
\N	\N	\N	\N	\N	\N	22090	22089	\N	22088	\N
\N	\N	\N	\N	\N	\N	22093	22092	\N	22091	\N
\N	\N	\N	\N	\N	\N	22096	22095	\N	22094	\N
\N	\N	\N	\N	\N	\N	22099	22098	\N	22097	\N
\N	\N	\N	\N	\N	\N	22102	22101	\N	22100	\N
\N	\N	\N	\N	\N	\N	22105	22104	\N	22103	\N
\N	\N	\N	\N	\N	\N	22108	22107	\N	22106	\N
\N	\N	\N	\N	\N	\N	22111	22110	\N	22109	\N
\N	\N	\N	\N	\N	\N	22114	22113	\N	22112	\N
\N	\N	\N	\N	\N	\N	22117	22116	\N	22115	\N
\N	\N	\N	\N	\N	\N	22120	22119	\N	22118	\N
\N	\N	\N	\N	\N	\N	22123	22122	\N	22121	\N
\N	\N	\N	\N	\N	\N	22126	22125	\N	22124	\N
\N	\N	\N	\N	\N	\N	22129	22128	\N	22127	\N
\N	\N	\N	\N	\N	\N	22132	22131	\N	22130	\N
\N	\N	\N	\N	\N	\N	22135	22134	\N	22133	\N
\N	\N	\N	\N	\N	\N	22138	22137	\N	22136	\N
\N	\N	\N	\N	\N	\N	22141	22140	\N	22139	\N
\N	\N	\N	\N	\N	\N	22144	22143	\N	22142	\N
\N	\N	\N	\N	\N	\N	22147	22146	\N	22145	\N
\N	\N	\N	\N	\N	\N	22150	22149	\N	22148	\N
\N	\N	\N	\N	\N	\N	22153	22152	\N	22151	\N
\N	\N	\N	\N	\N	\N	22156	22155	\N	22154	\N
\N	\N	\N	\N	\N	\N	22159	22158	\N	22157	\N
\N	\N	\N	\N	\N	\N	22162	22161	\N	22160	\N
\N	\N	\N	\N	\N	\N	22165	22164	\N	22163	\N
\N	\N	\N	\N	\N	\N	22168	22167	\N	22166	\N
\N	\N	\N	\N	\N	\N	22171	22170	\N	22169	\N
\N	\N	\N	\N	\N	\N	22175	22174	\N	22173	\N
\N	\N	\N	\N	\N	\N	22178	22177	\N	22176	\N
\N	\N	\N	\N	\N	\N	22181	22180	\N	22179	\N
\N	\N	\N	\N	\N	\N	22184	22183	\N	22182	\N
\N	\N	\N	\N	\N	\N	22187	22186	\N	22185	\N
\N	\N	\N	\N	\N	\N	22190	22189	\N	22188	\N
\N	\N	\N	\N	\N	\N	22193	22192	\N	22191	\N
\N	\N	\N	\N	\N	\N	22196	22195	\N	22194	\N
\N	\N	\N	\N	\N	\N	22199	22198	\N	22197	\N
\N	\N	\N	\N	\N	\N	22202	22201	\N	22200	\N
\N	\N	\N	\N	\N	\N	22205	22204	\N	22203	\N
\N	\N	\N	\N	\N	\N	22208	22207	\N	22206	\N
\N	\N	\N	\N	\N	\N	22211	22210	\N	22209	\N
\N	\N	\N	\N	\N	\N	22214	22213	\N	22212	\N
\N	\N	\N	\N	\N	\N	22217	22216	\N	22215	\N
\N	\N	\N	\N	\N	\N	22220	22219	\N	22218	\N
\N	\N	\N	\N	\N	\N	22223	22222	\N	22221	\N
\N	\N	\N	\N	\N	\N	22226	22225	\N	22224	\N
\N	\N	\N	\N	\N	\N	22229	22228	\N	22227	\N
\N	\N	\N	\N	\N	\N	22232	22231	\N	22230	\N
\N	\N	\N	\N	\N	\N	22235	22234	\N	22233	\N
\N	\N	\N	\N	\N	\N	22238	22237	\N	22236	\N
\N	\N	\N	\N	\N	\N	22241	22240	\N	22239	\N
\N	\N	\N	\N	\N	\N	22244	22243	\N	22242	\N
\N	\N	\N	\N	\N	\N	22247	22246	\N	22245	\N
\N	\N	\N	\N	\N	\N	22250	22249	\N	22248	\N
\N	\N	\N	\N	\N	\N	22253	22252	\N	22251	\N
\N	\N	\N	\N	\N	\N	22256	22255	\N	22254	\N
\N	\N	\N	\N	\N	\N	22259	22258	\N	22257	\N
\N	\N	\N	\N	\N	\N	22262	22261	\N	22260	\N
\N	\N	\N	\N	\N	\N	22265	22264	\N	22263	\N
\N	\N	\N	\N	\N	\N	22268	22267	\N	22266	\N
\N	\N	\N	\N	\N	\N	22271	22270	\N	22269	\N
\N	\N	\N	\N	\N	\N	22274	22273	\N	22272	\N
\N	\N	\N	\N	\N	\N	22277	22276	\N	22275	\N
\N	\N	\N	\N	\N	\N	22280	22279	\N	22278	\N
\N	\N	\N	\N	\N	\N	22283	22282	\N	22281	\N
\N	\N	\N	\N	\N	\N	22286	22285	\N	22284	\N
\N	\N	\N	\N	\N	\N	22289	22288	\N	22287	\N
\N	\N	\N	\N	\N	\N	22292	22291	\N	22290	\N
\N	\N	\N	\N	\N	\N	22295	22294	\N	22293	\N
\N	\N	\N	\N	\N	\N	22298	22297	\N	22296	\N
\N	\N	\N	\N	\N	\N	22301	22300	\N	22299	\N
\N	\N	\N	\N	\N	\N	22304	22303	\N	22302	\N
\N	\N	\N	\N	\N	\N	22307	22306	\N	22305	\N
\N	\N	\N	\N	\N	\N	22310	22309	\N	22308	\N
\N	\N	\N	\N	\N	\N	22313	22312	\N	22311	\N
\N	\N	\N	\N	\N	\N	22316	22315	\N	22314	\N
\N	\N	\N	\N	\N	\N	22319	22318	\N	22317	\N
\N	\N	\N	\N	\N	\N	22322	22321	\N	22320	\N
\N	\N	\N	\N	\N	\N	22325	22324	\N	22323	\N
\N	\N	\N	\N	\N	\N	22328	22327	\N	22326	\N
\N	\N	\N	\N	\N	\N	22331	22330	\N	22329	\N
\N	\N	\N	\N	\N	\N	22334	22333	\N	22332	\N
\N	\N	\N	\N	\N	\N	22337	22336	\N	22335	\N
\N	\N	\N	\N	\N	\N	22340	22339	\N	22338	\N
\N	\N	\N	\N	\N	\N	22343	22342	\N	22341	\N
\N	\N	\N	\N	\N	\N	22346	22345	\N	22344	\N
\N	\N	\N	\N	\N	\N	22349	22348	\N	22347	\N
\N	\N	\N	\N	\N	\N	22352	22351	\N	22350	\N
\N	\N	\N	\N	\N	\N	22355	22354	\N	22353	\N
\N	\N	\N	\N	\N	\N	22358	22357	\N	22356	\N
\N	\N	\N	\N	\N	\N	22361	22360	\N	22359	\N
\N	\N	\N	\N	\N	\N	22364	22363	\N	22362	\N
\N	\N	\N	\N	\N	\N	22367	22366	\N	22365	\N
\N	\N	\N	\N	\N	\N	22370	22369	\N	22368	\N
\N	\N	\N	\N	\N	\N	22373	22372	\N	22371	\N
\N	\N	\N	\N	\N	\N	22376	22375	\N	22374	\N
\N	\N	\N	\N	\N	\N	22379	22378	\N	22377	\N
\N	\N	\N	\N	\N	\N	22382	22381	\N	22380	\N
\N	\N	\N	\N	\N	\N	22385	22384	\N	22383	\N
\N	\N	\N	\N	\N	\N	22388	22387	\N	22386	\N
\N	\N	\N	\N	\N	\N	22391	22390	\N	22389	\N
\N	\N	\N	\N	\N	\N	22394	22393	\N	22392	\N
\N	\N	\N	\N	\N	\N	22397	22396	\N	22395	\N
\N	\N	\N	\N	\N	\N	22400	22399	\N	22398	\N
\N	\N	\N	\N	\N	\N	22403	22402	\N	22401	\N
\N	\N	\N	\N	\N	\N	22406	22405	\N	22404	\N
\N	\N	\N	\N	\N	\N	22409	22408	\N	22407	\N
\N	\N	\N	\N	\N	\N	22412	22411	\N	22410	\N
\N	\N	\N	\N	\N	\N	22415	22414	\N	22413	\N
\N	\N	\N	\N	\N	\N	22418	22417	\N	22416	\N
\N	\N	\N	\N	\N	\N	22421	22420	\N	22419	\N
\N	\N	\N	\N	\N	\N	22424	22423	\N	22422	\N
\N	\N	\N	\N	\N	\N	22427	22426	\N	22425	\N
\N	\N	\N	\N	\N	\N	22430	22429	\N	22428	\N
\N	\N	\N	\N	\N	\N	22433	22432	\N	22431	\N
\N	\N	\N	\N	\N	\N	22436	22435	\N	22434	\N
\N	\N	\N	\N	\N	\N	22439	22438	\N	22437	\N
\N	\N	\N	\N	\N	\N	22442	22441	\N	22440	\N
\N	\N	\N	\N	\N	\N	22445	22444	\N	22443	\N
\N	\N	\N	\N	\N	\N	22448	22447	\N	22446	\N
\N	\N	\N	\N	\N	\N	22451	22450	\N	22449	\N
\N	\N	\N	\N	\N	\N	22454	22453	\N	22452	\N
\N	\N	\N	\N	\N	\N	22457	22456	\N	22455	\N
\N	\N	\N	\N	\N	\N	22460	22459	\N	22458	\N
\N	\N	\N	\N	\N	\N	22463	22462	\N	22461	\N
\N	\N	\N	\N	\N	\N	22466	22465	\N	22464	\N
\N	\N	\N	\N	\N	\N	22469	22468	\N	22467	\N
\N	\N	\N	\N	\N	\N	22472	22471	\N	22470	\N
\N	\N	\N	\N	\N	\N	22475	22474	\N	22473	\N
\N	\N	\N	\N	\N	\N	22478	22477	\N	22476	\N
\N	\N	\N	\N	\N	\N	22481	22480	\N	22479	\N
\N	\N	\N	\N	\N	\N	22484	22483	\N	22482	\N
\N	\N	\N	\N	\N	\N	22487	22486	\N	22485	\N
\N	\N	\N	\N	\N	\N	22490	22489	\N	22488	\N
\N	\N	\N	\N	\N	\N	22493	22492	\N	22491	\N
\N	\N	\N	\N	\N	\N	22496	22495	\N	22494	\N
\N	\N	\N	\N	\N	\N	22499	22498	\N	22497	\N
\N	\N	\N	\N	\N	\N	22502	22501	\N	22500	\N
\N	\N	\N	\N	\N	\N	22505	22504	\N	22503	\N
\N	\N	\N	\N	\N	\N	22508	22507	\N	22506	\N
\N	\N	\N	\N	\N	\N	22511	22510	\N	22509	\N
\N	\N	\N	\N	\N	\N	22514	22513	\N	22512	\N
\N	\N	\N	\N	\N	\N	22517	22516	\N	22515	\N
\N	\N	\N	\N	\N	\N	22521	22520	\N	22519	\N
\N	\N	\N	\N	\N	\N	22524	22523	\N	22522	\N
\N	\N	\N	\N	\N	\N	22527	22526	\N	22525	\N
\N	\N	\N	\N	\N	\N	22530	22529	\N	22528	\N
\N	\N	\N	\N	\N	\N	22533	22532	\N	22531	\N
\N	\N	\N	\N	\N	\N	22536	22535	\N	22534	\N
\N	\N	\N	\N	\N	\N	22539	22538	\N	22537	\N
\N	\N	\N	\N	\N	\N	22542	22541	\N	22540	\N
\N	\N	\N	\N	\N	\N	22545	22544	\N	22543	\N
\N	\N	\N	\N	\N	\N	22548	22547	\N	22546	\N
\N	\N	\N	\N	\N	\N	22551	22550	\N	22549	\N
\N	\N	\N	\N	\N	\N	22554	22553	\N	22552	\N
\N	\N	\N	\N	\N	\N	22557	22556	\N	22555	\N
\N	\N	\N	\N	\N	\N	22560	22559	\N	22558	\N
\N	\N	\N	\N	\N	\N	22563	22562	\N	22561	\N
\N	\N	\N	\N	\N	\N	22566	22565	\N	22564	\N
\N	\N	\N	\N	\N	\N	22569	22568	\N	22567	\N
\N	\N	\N	\N	\N	\N	22572	22571	\N	22570	\N
\N	\N	\N	\N	\N	\N	22575	22574	\N	22573	\N
\N	\N	\N	\N	\N	\N	22578	22577	\N	22576	\N
\N	\N	\N	\N	\N	\N	22581	22580	\N	22579	\N
\N	\N	\N	\N	\N	\N	22584	22583	\N	22582	\N
\N	\N	\N	\N	\N	\N	22587	22586	\N	22585	\N
\N	\N	\N	\N	\N	\N	22590	22589	\N	22588	\N
\N	\N	\N	\N	\N	\N	22593	22592	\N	22591	\N
\N	\N	\N	\N	\N	\N	22596	22595	\N	22594	\N
\N	\N	\N	\N	\N	\N	22599	22598	\N	22597	\N
\N	\N	\N	\N	\N	\N	22602	22601	\N	22600	\N
\N	\N	\N	\N	\N	\N	22605	22604	\N	22603	\N
\N	\N	\N	\N	\N	\N	22608	22607	\N	22606	\N
\N	\N	\N	\N	\N	\N	22611	22610	\N	22609	\N
\N	\N	\N	\N	\N	\N	22614	22613	\N	22612	\N
\N	\N	\N	\N	\N	\N	22617	22616	\N	22615	\N
\N	\N	\N	\N	\N	\N	22620	22619	\N	22618	\N
\N	\N	\N	\N	\N	\N	22623	22622	\N	22621	\N
\N	\N	\N	\N	\N	\N	22626	22625	\N	22624	\N
\N	\N	\N	\N	\N	\N	22629	22628	\N	22627	\N
\N	\N	\N	\N	\N	\N	22632	22631	\N	22630	\N
\N	\N	\N	\N	\N	\N	22635	22634	\N	22633	\N
\N	\N	\N	\N	\N	\N	22638	22637	\N	22636	\N
\N	\N	\N	\N	\N	\N	22641	22640	\N	22639	\N
\N	\N	\N	\N	\N	\N	22644	22643	\N	22642	\N
\N	\N	\N	\N	\N	\N	22647	22646	\N	22645	\N
\N	\N	\N	\N	\N	\N	22650	22649	\N	22648	\N
\N	\N	\N	\N	\N	\N	22653	22652	\N	22651	\N
\N	\N	\N	\N	\N	\N	22656	22655	\N	22654	\N
\N	\N	\N	\N	\N	\N	22659	22658	\N	22657	\N
\N	\N	\N	\N	\N	\N	22662	22661	\N	22660	\N
\N	\N	\N	\N	\N	\N	22665	22664	\N	22663	\N
\N	\N	\N	\N	\N	\N	22668	22667	\N	22666	\N
\N	\N	\N	\N	\N	\N	22671	22670	\N	22669	\N
\N	\N	\N	\N	\N	\N	22674	22673	\N	22672	\N
\N	\N	\N	\N	\N	\N	22677	22676	\N	22675	\N
\N	\N	\N	\N	\N	\N	22680	22679	\N	22678	\N
\N	\N	\N	\N	\N	\N	22683	22682	\N	22681	\N
\N	\N	\N	\N	\N	\N	22686	22685	\N	22684	\N
\N	\N	\N	\N	\N	\N	22689	22688	\N	22687	\N
\N	\N	\N	\N	\N	\N	22692	22691	\N	22690	\N
\N	\N	\N	\N	\N	\N	22695	22694	\N	22693	\N
\N	\N	\N	\N	\N	\N	22698	22697	\N	22696	\N
\N	\N	\N	\N	\N	\N	22701	22700	\N	22699	\N
\N	\N	\N	\N	\N	\N	22704	22703	\N	22702	\N
\N	\N	\N	\N	\N	\N	22707	22706	\N	22705	\N
\N	\N	\N	\N	\N	\N	22710	22709	\N	22708	\N
\N	\N	\N	\N	\N	\N	22713	22712	\N	22711	\N
\N	\N	\N	\N	\N	\N	22716	22715	\N	22714	\N
\N	\N	\N	\N	\N	\N	22719	22718	\N	22717	\N
\N	\N	\N	\N	\N	\N	22722	22721	\N	22720	\N
\N	\N	\N	\N	\N	\N	22725	22724	\N	22723	\N
\N	\N	\N	\N	\N	\N	22728	22727	\N	22726	\N
\N	\N	\N	\N	\N	\N	22731	22730	\N	22729	\N
\N	\N	\N	\N	\N	\N	22734	22733	\N	22732	\N
\N	\N	\N	\N	\N	\N	22737	22736	\N	22735	\N
\N	\N	\N	\N	\N	\N	22740	22739	\N	22738	\N
\N	\N	\N	\N	\N	\N	22743	22742	\N	22741	\N
\N	\N	\N	\N	\N	\N	22746	22745	\N	22744	\N
\N	\N	\N	\N	\N	\N	22749	22748	\N	22747	\N
\N	\N	\N	\N	\N	\N	22752	22751	\N	22750	\N
\N	\N	\N	\N	\N	\N	22755	22754	\N	22753	\N
\N	\N	\N	\N	\N	\N	22758	22757	\N	22756	\N
\N	\N	\N	\N	\N	\N	22761	22760	\N	22759	\N
\N	\N	\N	\N	\N	\N	22764	22763	\N	22762	\N
\N	\N	\N	\N	\N	\N	22767	22766	\N	22765	\N
\N	\N	\N	\N	\N	\N	22770	22769	\N	22768	\N
\N	\N	\N	\N	\N	\N	22773	22772	\N	22771	\N
\N	\N	\N	\N	\N	\N	22776	22775	\N	22774	\N
\N	\N	\N	\N	\N	\N	22779	22778	\N	22777	\N
\N	\N	\N	\N	\N	\N	22782	22781	\N	22780	\N
\N	\N	\N	\N	\N	\N	22785	22784	\N	22783	\N
\N	\N	\N	\N	\N	\N	22788	22787	\N	22786	\N
\N	\N	\N	\N	\N	\N	22791	22790	\N	22789	\N
\N	\N	\N	\N	\N	\N	22794	22793	\N	22792	\N
\N	\N	\N	\N	\N	\N	22797	22796	\N	22795	\N
\N	\N	\N	\N	\N	\N	22800	22799	\N	22798	\N
\N	\N	\N	\N	\N	\N	22803	22802	\N	22801	\N
\N	\N	\N	\N	\N	\N	22806	22805	\N	22804	\N
\N	\N	\N	\N	\N	\N	22809	22808	\N	22807	\N
\N	\N	\N	\N	\N	\N	22812	22811	\N	22810	\N
\N	\N	\N	\N	\N	\N	22815	22814	\N	22813	\N
\N	\N	\N	\N	\N	\N	22818	22817	\N	22816	\N
\N	\N	\N	\N	\N	\N	22821	22820	\N	22819	\N
\N	\N	\N	\N	\N	\N	22824	22823	\N	22822	\N
\N	\N	\N	\N	\N	\N	22827	22826	\N	22825	\N
\N	\N	\N	\N	\N	\N	22830	22829	\N	22828	\N
\N	\N	\N	\N	\N	\N	22833	22832	\N	22831	\N
\N	\N	\N	\N	\N	\N	22836	22835	\N	22834	\N
\N	\N	\N	\N	\N	\N	22839	22838	\N	22837	\N
\N	\N	\N	\N	\N	\N	22842	22841	\N	22840	\N
\N	\N	\N	\N	\N	\N	22845	22844	\N	22843	\N
\N	\N	\N	\N	\N	\N	22848	22847	\N	22846	\N
\N	\N	\N	\N	\N	\N	22851	22850	\N	22849	\N
\N	\N	\N	\N	\N	\N	22854	22853	\N	22852	\N
\N	\N	\N	\N	\N	\N	22857	22856	\N	22855	\N
\N	\N	\N	\N	\N	\N	22860	22859	\N	22858	\N
\N	\N	\N	\N	\N	\N	22863	22862	\N	22861	\N
\N	\N	\N	\N	\N	\N	22866	22865	\N	22864	\N
\N	\N	\N	\N	\N	\N	22869	22868	\N	22867	\N
\N	\N	\N	\N	\N	\N	22872	22871	\N	22870	\N
\N	\N	\N	\N	\N	\N	22875	22874	\N	22873	\N
\N	\N	\N	\N	\N	\N	22878	22877	\N	22876	\N
\N	\N	\N	\N	\N	\N	22881	22880	\N	22879	\N
\N	\N	\N	\N	\N	\N	22884	22883	\N	22882	\N
\N	\N	\N	\N	\N	\N	22887	22886	\N	22885	\N
\N	\N	\N	\N	\N	\N	22890	22889	\N	22888	\N
\N	\N	\N	\N	\N	\N	22893	22892	\N	22891	\N
\N	\N	\N	\N	\N	\N	22896	22895	\N	22894	\N
\N	\N	\N	\N	\N	\N	22899	22898	\N	22897	\N
\N	\N	\N	\N	\N	\N	22902	22901	\N	22900	\N
\N	\N	\N	\N	\N	\N	22905	22904	\N	22903	\N
\N	\N	\N	\N	\N	\N	22908	22907	\N	22906	\N
\N	\N	\N	\N	\N	\N	22911	22910	\N	22909	\N
\N	\N	\N	\N	\N	\N	22914	22913	\N	22912	\N
\N	\N	\N	\N	\N	\N	22917	22916	\N	22915	\N
\N	\N	\N	\N	\N	\N	22920	22919	\N	22918	\N
\N	\N	\N	\N	\N	\N	22923	22922	\N	22921	\N
\N	\N	\N	\N	\N	\N	22926	22925	\N	22924	\N
\N	\N	\N	\N	\N	\N	22929	22928	\N	22927	\N
\N	\N	\N	\N	\N	\N	22932	22931	\N	22930	\N
\N	\N	\N	\N	\N	\N	22935	22934	\N	22933	\N
\N	\N	\N	\N	\N	\N	22938	22937	\N	22936	\N
\N	\N	\N	\N	\N	\N	22941	22940	\N	22939	\N
\N	\N	\N	\N	\N	\N	22944	22943	\N	22942	\N
\N	\N	\N	\N	\N	\N	22947	22946	\N	22945	\N
\N	\N	\N	\N	\N	\N	22950	22949	\N	22948	\N
\N	\N	\N	\N	\N	\N	22953	22952	\N	22951	\N
\N	\N	\N	\N	\N	\N	22956	22955	\N	22954	\N
\N	\N	\N	\N	\N	\N	22959	22958	\N	22957	\N
\N	\N	\N	\N	\N	\N	22962	22961	\N	22960	\N
\N	\N	\N	\N	\N	\N	22965	22964	\N	22963	\N
\N	\N	\N	\N	\N	\N	22968	22967	\N	22966	\N
\N	\N	\N	\N	\N	\N	22971	22970	\N	22969	\N
\N	\N	\N	\N	\N	\N	22974	22973	\N	22972	\N
\N	\N	\N	\N	\N	\N	22977	22976	\N	22975	\N
\N	\N	\N	\N	\N	\N	22980	22979	\N	22978	\N
\N	\N	\N	\N	\N	\N	22983	22982	\N	22981	\N
\N	\N	\N	\N	\N	\N	22986	22985	\N	22984	\N
\N	\N	\N	\N	\N	\N	22989	22988	\N	22987	\N
\N	\N	\N	\N	\N	\N	22992	22991	\N	22990	\N
\N	\N	\N	\N	\N	\N	22995	22994	\N	22993	\N
\N	\N	\N	\N	\N	\N	22998	22997	\N	22996	\N
\N	\N	\N	\N	\N	\N	23001	23000	\N	22999	\N
\N	\N	\N	\N	\N	\N	23004	23003	\N	23002	\N
\N	\N	\N	\N	\N	\N	23007	23006	\N	23005	\N
\N	\N	\N	\N	\N	\N	23010	23009	\N	23008	\N
\N	\N	\N	\N	\N	\N	23013	23012	\N	23011	\N
\N	\N	\N	\N	\N	\N	23016	23015	\N	23014	\N
\N	\N	\N	\N	\N	\N	23019	23018	\N	23017	\N
\N	\N	\N	\N	\N	\N	23022	23021	\N	23020	\N
\N	\N	\N	\N	\N	\N	23025	23024	\N	23023	\N
\N	\N	\N	\N	\N	\N	23028	23027	\N	23026	\N
\N	\N	\N	\N	\N	\N	23031	23030	\N	23029	\N
\N	\N	\N	\N	\N	\N	23034	23033	\N	23032	\N
\N	\N	\N	\N	\N	\N	23037	23036	\N	23035	\N
\N	\N	\N	\N	\N	\N	23040	23039	\N	23038	\N
\N	\N	\N	\N	\N	\N	23043	23042	\N	23041	\N
\N	\N	\N	\N	\N	\N	23046	23045	\N	23044	\N
\N	\N	\N	\N	\N	\N	23049	23048	\N	23047	\N
\N	\N	\N	\N	\N	\N	23052	23051	\N	23050	\N
\N	\N	\N	\N	\N	\N	23055	23054	\N	23053	\N
\N	\N	\N	\N	\N	\N	23058	23057	\N	23056	\N
\N	\N	\N	\N	\N	\N	23061	23060	\N	23059	\N
\N	\N	\N	\N	\N	\N	23064	23063	\N	23062	\N
\N	\N	\N	\N	\N	\N	23067	23066	\N	23065	\N
\N	\N	\N	\N	\N	\N	23070	23069	\N	23068	\N
\N	\N	\N	\N	\N	\N	23073	23072	\N	23071	\N
\N	\N	\N	\N	\N	\N	23076	23075	\N	23074	\N
\N	\N	\N	\N	\N	\N	23079	23078	\N	23077	\N
\N	\N	\N	\N	\N	\N	23082	23081	\N	23080	\N
\N	\N	\N	\N	\N	\N	23085	23084	\N	23083	\N
\N	\N	\N	\N	\N	\N	23088	23087	\N	23086	\N
\N	\N	\N	\N	\N	\N	23091	23090	\N	23089	\N
\N	\N	\N	\N	\N	\N	23094	23093	\N	23092	\N
\N	\N	\N	\N	\N	\N	23097	23096	\N	23095	\N
\N	\N	\N	\N	\N	\N	23100	23099	\N	23098	\N
\N	\N	\N	\N	\N	\N	23103	23102	\N	23101	\N
\N	\N	\N	\N	\N	\N	23106	23105	\N	23104	\N
\N	\N	\N	\N	\N	\N	23109	23108	\N	23107	\N
\N	\N	\N	\N	\N	\N	23112	23111	\N	23110	\N
\N	\N	\N	\N	\N	\N	23115	23114	\N	23113	\N
\N	\N	\N	\N	\N	\N	23118	23117	\N	23116	\N
\N	\N	\N	\N	\N	\N	23121	23120	\N	23119	\N
\N	\N	\N	\N	\N	\N	23124	23123	\N	23122	\N
\N	\N	\N	\N	\N	\N	23127	23126	\N	23125	\N
\N	\N	\N	\N	\N	\N	23130	23129	\N	23128	\N
\N	\N	\N	\N	\N	\N	23133	23132	\N	23131	\N
\N	\N	\N	\N	\N	\N	23136	23135	\N	23134	\N
\N	\N	\N	\N	\N	\N	23139	23138	\N	23137	\N
\N	\N	\N	\N	\N	\N	23142	23141	\N	23140	\N
\N	\N	\N	\N	\N	\N	23145	23144	\N	23143	\N
\N	\N	\N	\N	\N	\N	23148	23147	\N	23146	\N
\N	\N	\N	\N	\N	\N	23151	23150	\N	23149	\N
\N	\N	\N	\N	\N	\N	23154	23153	\N	23152	\N
\N	\N	\N	\N	\N	\N	23157	23156	\N	23155	\N
\N	\N	\N	\N	\N	\N	23160	23159	\N	23158	\N
\N	\N	\N	\N	\N	\N	23163	23162	\N	23161	\N
\N	\N	\N	\N	\N	\N	23166	23165	\N	23164	\N
\N	\N	\N	\N	\N	\N	23169	23168	\N	23167	\N
\N	\N	\N	\N	\N	\N	23172	23171	\N	23170	\N
\N	\N	\N	\N	\N	\N	23175	23174	\N	23173	\N
\N	\N	\N	\N	\N	\N	23178	23177	\N	23176	\N
\N	\N	\N	\N	\N	\N	23181	23180	\N	23179	\N
\N	\N	\N	\N	\N	\N	23184	23183	\N	23182	\N
\N	\N	\N	\N	\N	\N	23187	23186	\N	23185	\N
\N	\N	\N	\N	\N	\N	23190	23189	\N	23188	\N
\N	\N	\N	\N	\N	\N	23193	23192	\N	23191	\N
\N	\N	\N	\N	\N	\N	23196	23195	\N	23194	\N
\N	\N	\N	\N	\N	\N	23199	23198	\N	23197	\N
\N	\N	\N	\N	\N	\N	23202	23201	\N	23200	\N
\N	\N	\N	\N	\N	\N	23205	23204	\N	23203	\N
\N	\N	\N	\N	\N	\N	23208	23207	\N	23206	\N
\N	\N	\N	\N	\N	\N	23211	23210	\N	23209	\N
\N	\N	\N	\N	\N	\N	23214	23213	\N	23212	\N
\N	\N	\N	\N	\N	\N	23217	23216	\N	23215	\N
\N	\N	\N	\N	\N	\N	23220	23219	\N	23218	\N
\N	\N	\N	\N	\N	\N	23223	23222	\N	23221	\N
\N	\N	\N	\N	\N	\N	23226	23225	\N	23224	\N
\N	\N	\N	\N	\N	\N	23229	23228	\N	23227	\N
\N	\N	\N	\N	\N	\N	23232	23231	\N	23230	\N
\N	\N	\N	\N	\N	\N	23235	23234	\N	23233	\N
\N	\N	\N	\N	\N	\N	23238	23237	\N	23236	\N
\N	\N	\N	\N	\N	\N	23241	23240	\N	23239	\N
\N	\N	\N	\N	\N	\N	23244	23243	\N	23242	\N
\N	\N	\N	\N	\N	\N	23247	23246	\N	23245	\N
\N	\N	\N	\N	\N	\N	23250	23249	\N	23248	\N
\N	\N	\N	\N	\N	\N	23253	23252	\N	23251	\N
\N	\N	\N	\N	\N	\N	23256	23255	\N	23254	\N
\N	\N	\N	\N	\N	\N	23259	23258	\N	23257	\N
\N	\N	\N	\N	\N	\N	23262	23261	\N	23260	\N
\N	\N	\N	\N	\N	\N	23265	23264	\N	23263	\N
\N	\N	\N	\N	\N	\N	23268	23267	\N	23266	\N
\N	\N	\N	\N	\N	\N	23271	23270	\N	23269	\N
\N	\N	\N	\N	\N	\N	23274	23273	\N	23272	\N
\N	\N	\N	\N	\N	\N	23277	23276	\N	23275	\N
\N	\N	\N	\N	\N	\N	23280	23279	\N	23278	\N
\N	\N	\N	\N	\N	\N	23283	23282	\N	23281	\N
\N	\N	\N	\N	\N	\N	23286	23285	\N	23284	\N
\N	\N	\N	\N	\N	\N	23289	23288	\N	23287	\N
\N	\N	\N	\N	\N	\N	23292	23291	\N	23290	\N
\N	\N	\N	\N	\N	\N	23295	23294	\N	23293	\N
\N	\N	\N	\N	\N	\N	23298	23297	\N	23296	\N
\N	\N	\N	\N	\N	\N	23301	23300	\N	23299	\N
\N	\N	\N	\N	\N	\N	23304	23303	\N	23302	\N
\N	\N	\N	\N	\N	\N	23307	23306	\N	23305	\N
\N	\N	\N	\N	\N	\N	23310	23309	\N	23308	\N
\N	\N	\N	\N	\N	\N	23313	23312	\N	23311	\N
\N	\N	\N	\N	\N	\N	23316	23315	\N	23314	\N
\N	\N	\N	\N	\N	\N	23319	23318	\N	23317	\N
\N	\N	\N	\N	\N	\N	23322	23321	\N	23320	\N
\N	\N	\N	\N	\N	\N	23325	23324	\N	23323	\N
\N	\N	\N	\N	\N	\N	23328	23327	\N	23326	\N
\N	\N	\N	\N	\N	\N	23331	23330	\N	23329	\N
\N	\N	\N	\N	\N	\N	23334	23333	\N	23332	\N
\N	\N	\N	\N	\N	\N	23337	23336	\N	23335	\N
\N	\N	\N	\N	\N	\N	23340	23339	\N	23338	\N
\N	\N	\N	\N	\N	\N	23343	23342	\N	23341	\N
\N	\N	\N	\N	\N	\N	23346	23345	\N	23344	\N
\N	\N	\N	\N	\N	\N	23349	23348	\N	23347	\N
\N	\N	\N	\N	\N	\N	23352	23351	\N	23350	\N
\N	\N	\N	\N	\N	\N	23355	23354	\N	23353	\N
\N	\N	\N	\N	\N	\N	23358	23357	\N	23356	\N
\N	\N	\N	\N	\N	\N	23361	23360	\N	23359	\N
\N	\N	\N	\N	\N	\N	23364	23363	\N	23362	\N
\N	\N	\N	\N	\N	\N	23367	23366	\N	23365	\N
\N	\N	\N	\N	\N	\N	23370	23369	\N	23368	\N
\N	\N	\N	\N	\N	\N	23373	23372	\N	23371	\N
\N	\N	\N	\N	\N	\N	23376	23375	\N	23374	\N
\N	\N	\N	\N	\N	\N	23379	23378	\N	23377	\N
\N	\N	\N	\N	\N	\N	23382	23381	\N	23380	\N
\N	\N	\N	\N	\N	\N	23385	23384	\N	23383	\N
\N	\N	\N	\N	\N	\N	23388	23387	\N	23386	\N
\N	\N	\N	\N	\N	\N	23391	23390	\N	23389	\N
\N	\N	\N	\N	\N	\N	23394	23393	\N	23392	\N
\N	\N	\N	\N	\N	\N	23397	23396	\N	23395	\N
\N	\N	\N	\N	\N	\N	23400	23399	\N	23398	\N
\N	\N	\N	\N	\N	\N	23403	23402	\N	23401	\N
\N	\N	\N	\N	\N	\N	23406	23405	\N	23404	\N
\N	\N	\N	\N	\N	\N	23409	23408	\N	23407	\N
\N	\N	\N	\N	\N	\N	23412	23411	\N	23410	\N
\N	\N	\N	\N	\N	\N	23415	23414	\N	23413	\N
\N	\N	\N	\N	\N	\N	23418	23417	\N	23416	\N
\N	\N	\N	\N	\N	\N	23421	23420	\N	23419	\N
\N	\N	\N	\N	\N	\N	23424	23423	\N	23422	\N
\N	\N	\N	\N	\N	\N	23427	23426	\N	23425	\N
\N	\N	\N	\N	\N	\N	23430	23429	\N	23428	\N
\N	\N	\N	\N	\N	\N	23433	23432	\N	23431	\N
\N	\N	\N	\N	\N	\N	23436	23435	\N	23434	\N
\N	\N	\N	\N	\N	\N	23439	23438	\N	23437	\N
\N	\N	\N	\N	\N	\N	23442	23441	\N	23440	\N
\N	\N	\N	\N	\N	\N	23445	23444	\N	23443	\N
\N	\N	\N	\N	\N	\N	23448	23447	\N	23446	\N
\N	\N	\N	\N	\N	\N	23451	23450	\N	23449	\N
\N	\N	\N	\N	\N	\N	23454	23453	\N	23452	\N
\N	\N	\N	\N	\N	\N	23457	23456	\N	23455	\N
\N	\N	\N	\N	\N	\N	23460	23459	\N	23458	\N
\N	\N	\N	\N	\N	\N	23463	23462	\N	23461	\N
\N	\N	\N	\N	\N	\N	23466	23465	\N	23464	\N
\N	\N	\N	\N	\N	\N	23469	23468	\N	23467	\N
\N	\N	\N	\N	\N	\N	23472	23471	\N	23470	\N
\N	\N	\N	\N	\N	\N	23475	23474	\N	23473	\N
\N	\N	\N	\N	\N	\N	23478	23477	\N	23476	\N
\N	\N	\N	\N	\N	\N	23481	23480	\N	23479	\N
\N	\N	\N	\N	\N	\N	23484	23483	\N	23482	\N
\N	\N	\N	\N	\N	\N	23487	23486	\N	23485	\N
\N	\N	\N	\N	\N	\N	23490	23489	\N	23488	\N
\N	\N	\N	\N	\N	\N	23493	23492	\N	23491	\N
\N	\N	\N	\N	\N	\N	23496	23495	\N	23494	\N
\N	\N	\N	\N	\N	\N	23499	23498	\N	23497	\N
\N	\N	\N	\N	\N	\N	23502	23501	\N	23500	\N
\N	\N	\N	\N	\N	\N	23505	23504	\N	23503	\N
\N	\N	\N	\N	\N	\N	36012	36001	\N	36003	\N
\N	\N	\N	\N	\N	\N	36026	36013	\N	36016	\N
\N	\N	\N	\N	\N	\N	36039	36027	\N	36029	\N
\N	\N	\N	\N	\N	\N	36053	36040	\N	36042	\N
\N	\N	\N	\N	\N	\N	36062	36054	\N	36057	\N
\N	\N	\N	\N	\N	\N	36073	36063	\N	36065	\N
\N	\N	\N	\N	\N	\N	36085	36074	\N	36076	\N
\N	\N	\N	\N	\N	\N	36098	36086	\N	36088	\N
\N	\N	\N	\N	\N	\N	36110	36099	\N	36101	\N
\N	\N	\N	\N	\N	\N	36116	36111	\N	36113	\N
\N	\N	\N	\N	\N	\N	36122	36117	\N	36119	\N
\N	\N	\N	\N	\N	\N	36128	36123	\N	36125	\N
\N	\N	\N	\N	\N	\N	36134	36129	\N	36131	\N
\N	\N	\N	\N	\N	\N	36141	36135	\N	36137	\N
\N	\N	\N	\N	\N	\N	36147	36142	\N	36144	\N
\N	\N	\N	\N	\N	\N	36153	36148	\N	36150	\N
\N	\N	\N	\N	\N	\N	36159	36154	\N	36156	\N
\N	\N	\N	\N	\N	\N	36165	36160	\N	36162	\N
\N	\N	\N	\N	\N	\N	36171	36166	\N	36168	\N
\N	\N	\N	\N	\N	\N	36177	36172	\N	36174	\N
\N	\N	\N	\N	\N	\N	36187	36178	\N	36180	\N
\N	\N	\N	\N	\N	\N	36207	36188	\N	36190	\N
\.


--
-- Data for Name: sche_scheduledtask; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduledtask (completiontime, name, priority, scheduledtime, state, labbookentryid, holderid, instrumentid, scheduleplanoffsetid) FROM stdin;
\.


--
-- Data for Name: sche_scheduleplan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplan (name, labbookentryid) FROM stdin;
\.


--
-- Data for Name: sche_scheduleplanoffset; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sche_scheduleplanoffset (offsettime, priority, labbookentryid, scheduleplanid, order_) FROM stdin;
\.


--
-- Data for Name: targ_alias; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_alias (name, labbookentryid, targetid) FROM stdin;
\.


--
-- Data for Name: targ_milestone; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_milestone (date_, labbookentryid, experimentid, statusid, targetid) FROM stdin;
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
-- Data for Name: targ_similarityhit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_similarityhit (description, evalue, similaritypercentage, labbookentryid, attachmentid, targetid) FROM stdin;
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

COPY targ_targetgroup (groupingtype, name, labbookentryid, targetgroupid) FROM stdin;
\.


--
-- Data for Name: targ_targgr2targets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY targ_targgr2targets (targetgroupid, targetid) FROM stdin;
\.


--
-- Data for Name: trag_researchobjectiveelement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY trag_researchobjectiveelement (alwaysincluded, approxbeginseqid, approxendseqid, componenttype, "domain", status, whychosen, labbookentryid, moleculeid, researchobjectiveid, targetid) FROM stdin;
\.


--
-- Name: acco_permission_permissionclass_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_permissionclass_key UNIQUE (permissionclass, optype, rolename, accessobjectid, usergroupid);


--
-- Name: acco_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT acco_permission_pkey PRIMARY KEY (systemclassid);


--
-- Name: acco_user_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_name_key UNIQUE (name);


--
-- Name: acco_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT acco_user_pkey PRIMARY KEY (systemclassid);


--
-- Name: acco_usergroup2user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup2user
    ADD CONSTRAINT acco_usergroup2user_pkey PRIMARY KEY (usergroupid, memberuserid);


--
-- Name: acco_usergroup_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_name_key UNIQUE (name);


--
-- Name: acco_usergroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT acco_usergroup_pkey PRIMARY KEY (systemclassid);


--
-- Name: core_accessobject_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT core_accessobject_name_key UNIQUE (name);


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
    ADD CONSTRAINT core_note2relatedentrys_pkey PRIMARY KEY (noteid, labbookentryid);


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
-- Name: cryz_score_value_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT cryz_score_value_key UNIQUE (value, scoringschemeid);


--
-- Name: cryz_scoringscheme_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_name_key UNIQUE (name);


--
-- Name: cryz_scoringscheme_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cryz_scoringscheme
    ADD CONSTRAINT cryz_scoringscheme_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_experiment_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT expe_experiment_name_key UNIQUE (name);


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
-- Name: expe_instrument2insttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_instrument2insttype
    ADD CONSTRAINT expe_instrument2insttype_pkey PRIMARY KEY (instrumentid, instrumenttypeid);


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
-- Name: expe_outputsample_sampleid_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT expe_outputsample_sampleid_key UNIQUE (sampleid);


--
-- Name: expe_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT expe_parameter_pkey PRIMARY KEY (labbookentryid);


--
-- Name: expe_software_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY expe_software
    ADD CONSTRAINT expe_software_name_key UNIQUE (name, version);


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
-- Name: hold_abstractholder_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT hold_abstractholder_name_key UNIQUE (name);


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
    ADD CONSTRAINT hold_holdca2absthoty_pkey PRIMARY KEY (holdercategoryid, abstractholdertypeid);


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
-- Name: hold_refholderoffset_rowoffset_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT hold_refholderoffset_rowoffset_key UNIQUE (rowoffset, coloffset, suboffset, holderid);


--
-- Name: hold_refsampleposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_refsampleposition_rowposition_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT hold_refsampleposition_rowposition_key UNIQUE (rowposition, colposition, subposition, refholderid);


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
-- Name: mole_abstractcomponent_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT mole_abstractcomponent_name_key UNIQUE (name);


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
-- Name: mole_extension_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_extension
    ADD CONSTRAINT mole_extension_pkey PRIMARY KEY (moleculeid);


--
-- Name: mole_host2organisation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_host2organisation
    ADD CONSTRAINT mole_host2organisation_pkey PRIMARY KEY (hostid, organisationid);


--
-- Name: mole_host_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY mole_host
    ADD CONSTRAINT mole_host_pkey PRIMARY KEY (abstractcomponentid);


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
-- Name: peop_organisation_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT peop_organisation_name_key UNIQUE (name);


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
-- Name: prot_parameterdefinition_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_name_key UNIQUE (name, protocolid);


--
-- Name: prot_parameterdefinition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_parameterdefinition
    ADD CONSTRAINT prot_parameterdefinition_pkey PRIMARY KEY (labbookentryid);


--
-- Name: prot_protocol_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY prot_protocol
    ADD CONSTRAINT prot_protocol_name_key UNIQUE (name);


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
-- Name: ref_abstractholdertype_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_name_key UNIQUE (name);


--
-- Name: ref_abstractholdertype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_abstractholdertype
    ADD CONSTRAINT ref_abstractholdertype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_componentcategory_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_componentcategory
    ADD CONSTRAINT ref_componentcategory_name_key UNIQUE (name);


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
-- Name: ref_dbname_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_name_key UNIQUE (name);


--
-- Name: ref_dbname_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_dbname
    ADD CONSTRAINT ref_dbname_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_experimenttype_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_name_key UNIQUE (name);


--
-- Name: ref_experimenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_experimenttype
    ADD CONSTRAINT ref_experimenttype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_hazardphrase_classification_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_classification_key UNIQUE (classification, code);


--
-- Name: ref_hazardphrase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_hazardphrase
    ADD CONSTRAINT ref_hazardphrase_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_holdercategory_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdercategory
    ADD CONSTRAINT ref_holdercategory_name_key UNIQUE (name);


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
-- Name: ref_holdertypesource_catalognum_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdertypesource
    ADD CONSTRAINT ref_holdertypesource_catalognum_key UNIQUE (catalognum, holdertypeid);


--
-- Name: ref_holdertypesource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_holdertypesource
    ADD CONSTRAINT ref_holdertypesource_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_imagetype_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_name_key UNIQUE (name);


--
-- Name: ref_imagetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT ref_imagetype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_instrumenttype_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_name_key UNIQUE (name);


--
-- Name: ref_instrumenttype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_instrumenttype
    ADD CONSTRAINT ref_instrumenttype_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_organism_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_organism
    ADD CONSTRAINT ref_organism_name_key UNIQUE (name);


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
-- Name: ref_samplecategory_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_name_key UNIQUE (name);


--
-- Name: ref_samplecategory_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_samplecategory
    ADD CONSTRAINT ref_samplecategory_pkey PRIMARY KEY (publicentryid);


--
-- Name: ref_targetstatus_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ref_targetstatus
    ADD CONSTRAINT ref_targetstatus_name_key UNIQUE (name);


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
-- Name: sam_abstractsample_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_name_key UNIQUE (name);


--
-- Name: sam_abstractsample_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstractsample
    ADD CONSTRAINT sam_abstractsample_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sam_abstsa2hazaph_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_abstsa2hazaph
    ADD CONSTRAINT sam_abstsa2hazaph_pkey PRIMARY KEY (otherrole, hazardphraseid);


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
-- Name: sam_refsamplesource_catalognum_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_catalognum_key UNIQUE (catalognum, refsampleid, refholderid);


--
-- Name: sam_refsamplesource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT sam_refsamplesource_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sam_sampca2abstsa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sam_sampca2abstsa
    ADD CONSTRAINT sam_sampca2abstsa_pkey PRIMARY KEY (abstractsampleid, samplecategoryid);


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
-- Name: sche_scheduledtask_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_name_key UNIQUE (name);


--
-- Name: sche_scheduledtask_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sche_scheduledtask_scheduledtime_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT sche_scheduledtask_scheduledtime_key UNIQUE (scheduledtime, holderid);


--
-- Name: sche_scheduleplan_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_name_key UNIQUE (name);


--
-- Name: sche_scheduleplan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplan
    ADD CONSTRAINT sche_scheduleplan_pkey PRIMARY KEY (labbookentryid);


--
-- Name: sche_scheduleplanoffset_offsettime_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT sche_scheduleplanoffset_offsettime_key UNIQUE (offsettime, scheduleplanid);


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
-- Name: targ_researchobjective_commonname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_researchobjective_commonname_key UNIQUE (commonname);


--
-- Name: targ_researchobjective_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT targ_researchobjective_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_similarityhit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_similarityhit
    ADD CONSTRAINT targ_similarityhit_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_target2nuclac_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2nuclac
    ADD CONSTRAINT targ_target2nuclac_pkey PRIMARY KEY (nuctargetid, nucleicacidid);


--
-- Name: targ_target2projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target2projects
    ADD CONSTRAINT targ_target2projects_pkey PRIMARY KEY (targetid, projectid);


--
-- Name: targ_target_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_name_key UNIQUE (name);


--
-- Name: targ_target_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT targ_target_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_targetgroup_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_name_key UNIQUE (name);


--
-- Name: targ_targetgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT targ_targetgroup_pkey PRIMARY KEY (labbookentryid);


--
-- Name: targ_targgr2targets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY targ_targgr2targets
    ADD CONSTRAINT targ_targgr2targets_pkey PRIMARY KEY (targetgroupid, targetid);


--
-- Name: trag_researchobjectiveelement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT trag_researchobjectiveelement_pkey PRIMARY KEY (labbookentryid);


--
-- Name: hold_holder_startdate_inx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX hold_holder_startdate_inx ON hold_holder USING btree (startdate);


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
-- Name: fk14ff48dc32790645; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT fk14ff48dc32790645 FOREIGN KEY (lasteditorid) REFERENCES acco_user(systemclassid);


--
-- Name: fk14ff48dc3ce3876a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT fk14ff48dc3ce3876a FOREIGN KEY (accessid) REFERENCES core_accessobject(systemclassid);


--
-- Name: fk14ff48dc610514ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_labbookentry
    ADD CONSTRAINT fk14ff48dc610514ae FOREIGN KEY (creatorid) REFERENCES acco_user(systemclassid);


--
-- Name: fk15020243385506e5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_accessobject
    ADD CONSTRAINT fk15020243385506e5 FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid);


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
-- Name: fk1d5c137ba4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_extension
    ADD CONSTRAINT fk1d5c137ba4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk1db21283385506e5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT fk1db21283385506e5 FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid);


--
-- Name: fk1db21283fadc62cf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup
    ADD CONSTRAINT fk1db21283fadc62cf FOREIGN KEY (headerid) REFERENCES acco_user(systemclassid);


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
-- Name: fk28d12df259eec984; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT fk28d12df259eec984 FOREIGN KEY (parameterdefinitionid) REFERENCES prot_parameterdefinition(labbookentryid);


--
-- Name: fk28d12df26c002e1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_parameter
    ADD CONSTRAINT fk28d12df26c002e1f FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid);


--
-- Name: fk292a329e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk292a329e571067e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329e571067e FOREIGN KEY (manufacturerid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fk292a329ea9533f97; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329ea9533f97 FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid);


--
-- Name: fk292a329eae8a461a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument
    ADD CONSTRAINT fk292a329eae8a461a FOREIGN KEY (defaultimagetypeid) REFERENCES ref_imagetype(publicentryid);


--
-- Name: fk2a93f84145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT fk2a93f84145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk2a93f8415c64595b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_researchobjective
    ADD CONSTRAINT fk2a93f8415c64595b FOREIGN KEY (ownerid) REFERENCES peop_person(labbookentryid);


--
-- Name: fk2cdc1e5745a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT fk2cdc1e5745a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk2cdc1e57d3971edb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_abstractcomponent
    ADD CONSTRAINT fk2cdc1e57d3971edb FOREIGN KEY (naturalsourceid) REFERENCES ref_organism(publicentryid);


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
-- Name: fk3363f4118b86e8a1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_targetgroup
    ADD CONSTRAINT fk3363f4118b86e8a1 FOREIGN KEY (targetgroupid) REFERENCES targ_targetgroup(labbookentryid);


--
-- Name: fk340aa18f45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT fk340aa18f45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk340aa18fb4e81f71; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holdertypeposition
    ADD CONSTRAINT fk340aa18fb4e81f71 FOREIGN KEY (holdertypeid) REFERENCES ref_holdertype(abstractholdertypeid);


--
-- Name: fk34b0597c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT fk34b0597c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk34b0597cb5ac617d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT fk34b0597cb5ac617d FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid);


--
-- Name: fk34b0597ccbb3f861; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_personingroup
    ADD CONSTRAINT fk34b0597ccbb3f861 FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid);


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
-- Name: fk3792bdb8a3c3865e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_refsamplesource
    ADD CONSTRAINT fk3792bdb8a3c3865e FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid);


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
-- Name: fk39c1e55c385506e5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT fk39c1e55c385506e5 FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid);


--
-- Name: fk39c1e55cb5ac617d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_user
    ADD CONSTRAINT fk39c1e55cb5ac617d FOREIGN KEY (personid) REFERENCES peop_person(labbookentryid);


--
-- Name: fk3b3c825a6033c3a7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2user
    ADD CONSTRAINT fk3b3c825a6033c3a7 FOREIGN KEY (memberuserid) REFERENCES acco_user(systemclassid);


--
-- Name: fk3b3c825ac72f3607; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_usergroup2user
    ADD CONSTRAINT fk3b3c825ac72f3607 FOREIGN KEY (usergroupid) REFERENCES acco_usergroup(systemclassid);


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
-- Name: fk471823711f3823e7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT fk471823711f3823e7 FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid);


--
-- Name: fk4718237145a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_methodparameter
    ADD CONSTRAINT fk4718237145a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk4872a629f8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_imagetype
    ADD CONSTRAINT fk4872a629f8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


--
-- Name: fk4942a49266ad0c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_note
    ADD CONSTRAINT fk4942a49266ad0c9 FOREIGN KEY (attachmentid) REFERENCES core_attachment(dbid);


--
-- Name: fk4a04edda2915dc4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT fk4a04edda2915dc4a FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid);


--
-- Name: fk4a04edda45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT fk4a04edda45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk4a04edda578d9e96; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_parameter
    ADD CONSTRAINT fk4a04edda578d9e96 FOREIGN KEY (parameterdefinitionid) REFERENCES cryz_parameterdefinition(labbookentryid);


--
-- Name: fk4a9d5e579cc3e68a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fk4a9d5e579cc3e68a FOREIGN KEY (categoryid) REFERENCES ref_componentcategory(publicentryid);


--
-- Name: fk4a9d5e57d24dca52; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_compca2components
    ADD CONSTRAINT fk4a9d5e57d24dca52 FOREIGN KEY (componentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fk4ab4c22a3e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22a3e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk4ab4c22a4eb8a6ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22a4eb8a6ae FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid);


--
-- Name: fk4ab4c22ac7c01158; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22ac7c01158 FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid);


--
-- Name: fk4ab4c22ad8711492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_sample
    ADD CONSTRAINT fk4ab4c22ad8711492 FOREIGN KEY (assigntoid) REFERENCES peop_person(labbookentryid);


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
-- Name: fk57c8291c58259e17; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_project
    ADD CONSTRAINT fk57c8291c58259e17 FOREIGN KEY (projectid) REFERENCES targ_project(labbookentryid);


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
-- Name: fk63447cc09b62c547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_host2organisation
    ADD CONSTRAINT fk63447cc09b62c547 FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fk63447cc0cec6b38c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_host2organisation
    ADD CONSTRAINT fk63447cc0cec6b38c FOREIGN KEY (hostid) REFERENCES mole_host(abstractcomponentid);


--
-- Name: fk6390c30e45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT fk6390c30e45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk6390c30ed1cc5705; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT fk6390c30ed1cc5705 FOREIGN KEY (proteinid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk6390c30ed3135493; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_target
    ADD CONSTRAINT fk6390c30ed3135493 FOREIGN KEY (speciesid) REFERENCES ref_organism(publicentryid);


--
-- Name: fk65481d1345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT fk65481d1345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk65481d135336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_alias
    ADD CONSTRAINT fk65481d135336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fk6c4167c098dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_crystalsample
    ADD CONSTRAINT fk6c4167c098dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fk703f362019c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f362019c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fk703f362045a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f362045a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk703f36204eb8a6ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f36204eb8a6ae FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid);


--
-- Name: fk703f36205a5e25f1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduledtask
    ADD CONSTRAINT fk703f36205a5e25f1 FOREIGN KEY (scheduleplanoffsetid) REFERENCES sche_scheduleplanoffset(labbookentryid);


--
-- Name: fk77cc02e245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk77cc02e25d2a5e28; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e25d2a5e28 FOREIGN KEY (refoutputsampleid) REFERENCES prot_refoutputsample(labbookentryid);


--
-- Name: fk77cc02e26c002e1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e26c002e1f FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid);


--
-- Name: fk77cc02e298dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_outputsample
    ADD CONSTRAINT fk77cc02e298dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


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
-- Name: fk7c3c7eb35336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT fk7c3c7eb35336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fk7c3c7eb36c002e1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT fk7c3c7eb36c002e1f FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid);


--
-- Name: fk7c3c7eb3abbc44da; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_milestone
    ADD CONSTRAINT fk7c3c7eb3abbc44da FOREIGN KEY (statusid) REFERENCES ref_targetstatus(publicentryid);


--
-- Name: fk7c4a35b245a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT fk7c4a35b245a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk7c4a35b24eb8a6ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT fk7c4a35b24eb8a6ae FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid);


--
-- Name: fk7c4a35b2a3c3865e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refholderoffset
    ADD CONSTRAINT fk7c4a35b2a3c3865e FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid);


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
-- Name: fk8779ffad2915dc4a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad2915dc4a FOREIGN KEY (imageid) REFERENCES cryz_image(labbookentryid);


--
-- Name: fk8779ffad2a5694f8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad2a5694f8 FOREIGN KEY (scoreid) REFERENCES cryz_score(labbookentryid);


--
-- Name: fk8779ffad45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk8779ffad4eb8a6ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad4eb8a6ae FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid);


--
-- Name: fk8779ffad98dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffad98dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fk8779ffadf92d0173; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_dropannotation
    ADD CONSTRAINT fk8779ffadf92d0173 FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid);


--
-- Name: fk8a00037645a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT fk8a00037645a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk8a0003769b62c547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_group
    ADD CONSTRAINT fk8a0003769b62c547 FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid);


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
-- Name: fk8e3939f31caead24; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f31caead24 FOREIGN KEY (researchobjectivelementid) REFERENCES trag_researchobjectiveelement(labbookentryid);


--
-- Name: fk8e3939f33e228950; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f33e228950 FOREIGN KEY (abstractsampleid) REFERENCES sam_abstractsample(labbookentryid);


--
-- Name: fk8e3939f3403528f2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sam_samplecomponent
    ADD CONSTRAINT fk8e3939f3403528f2 FOREIGN KEY (containerid) REFERENCES sam_samplecomponent(labbookentryid);


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
-- Name: fk96600bec36f6621b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT fk96600bec36f6621b FOREIGN KEY (refmoleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk96600bec45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT fk96600bec45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk96600beca4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_moleculefeature
    ADD CONSTRAINT fk96600beca4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fk987c4be345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_organisation
    ADD CONSTRAINT fk987c4be345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk98bf9b1819c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT fk98bf9b1819c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fk98bf9b1845a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT fk98bf9b1845a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fk98bf9b18f92d0173; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_method
    ADD CONSTRAINT fk98bf9b18f92d0173 FOREIGN KEY (softwareid) REFERENCES expe_software(labbookentryid);


--
-- Name: fk997749c392c0da4d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT fk997749c392c0da4d FOREIGN KEY (authorid) REFERENCES acco_user(systemclassid);


--
-- Name: fk997749c3c4219d47; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_attachment
    ADD CONSTRAINT fk997749c3c4219d47 FOREIGN KEY (parententryid) REFERENCES core_labbookentry(dbid);


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
-- Name: fka28270e14eb8a6ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT fka28270e14eb8a6ae FOREIGN KEY (holderid) REFERENCES hold_holder(abstractholderid);


--
-- Name: fka28270e1a9533f97; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holderlocation
    ADD CONSTRAINT fka28270e1a9533f97 FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid);


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
-- Name: fka6a7e4cd4e441f33; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertypesource
    ADD CONSTRAINT fka6a7e4cd4e441f33 FOREIGN KEY (holdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- Name: fka6a7e4cd7f9149b9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertypesource
    ADD CONSTRAINT fka6a7e4cd7f9149b9 FOREIGN KEY (supplierid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fka6a7e4cdf8f98163; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertypesource
    ADD CONSTRAINT fka6a7e4cdf8f98163 FOREIGN KEY (publicentryid) REFERENCES ref_publicentry(dbid);


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
-- Name: fkbe856486a3c3865e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT fkbe856486a3c3865e FOREIGN KEY (refholderid) REFERENCES hold_refholder(abstractholderid);


--
-- Name: fkbe856486c7c01158; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_refsampleposition
    ADD CONSTRAINT fkbe856486c7c01158 FOREIGN KEY (refsampleid) REFERENCES sam_refsample(abstractsampleid);


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
-- Name: fkc4a62d1e9b7d0c8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY peop_person
    ADD CONSTRAINT fkc4a62d1e9b7d0c8 FOREIGN KEY (currentgroupid) REFERENCES peop_group(labbookentryid);


--
-- Name: fkc856919345a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_similarityhit
    ADD CONSTRAINT fkc856919345a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkc85691935336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_similarityhit
    ADD CONSTRAINT fkc85691935336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fkc85691935fd6f0e9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY targ_similarityhit
    ADD CONSTRAINT fkc85691935fd6f0e9 FOREIGN KEY (attachmentid) REFERENCES core_externaldblink(attachmentid);


--
-- Name: fkc9a2ac0c19c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c19c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fkc9a2ac0c45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkc9a2ac0c8ebd226f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c8ebd226f FOREIGN KEY (scheduledtaskid) REFERENCES sche_scheduledtask(labbookentryid);


--
-- Name: fkc9a2ac0c98dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0c98dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fkc9a2ac0cd024defb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_image
    ADD CONSTRAINT fkc9a2ac0cd024defb FOREIGN KEY (imagetypeid) REFERENCES ref_imagetype(publicentryid);


--
-- Name: fkca2b414322c2d4dc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cryz_score
    ADD CONSTRAINT fkca2b414322c2d4dc FOREIGN KEY (scoringschemeid) REFERENCES cryz_scoringscheme(labbookentryid);


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
-- Name: fkced55fd6c002e1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT fkced55fd6c002e1f FOREIGN KEY (experimentid) REFERENCES expe_experiment(labbookentryid);


--
-- Name: fkced55fd98dfed6c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT fkced55fd98dfed6c FOREIGN KEY (sampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fkced55fdf53af16c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_inputsample
    ADD CONSTRAINT fkced55fdf53af16c FOREIGN KEY (refinputsampleid) REFERENCES prot_refinputsample(labbookentryid);


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
-- Name: fkd0e6aab419c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab419c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fkd0e6aab41f3823e7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab41f3823e7 FOREIGN KEY (methodid) REFERENCES expe_method(labbookentryid);


--
-- Name: fkd0e6aab43d987f26; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab43d987f26 FOREIGN KEY (operatorid) REFERENCES acco_user(systemclassid);


--
-- Name: fkd0e6aab445a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab445a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd0e6aab46f99111a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab46f99111a FOREIGN KEY (protocolid) REFERENCES prot_protocol(labbookentryid);


--
-- Name: fkd0e6aab4805b47b5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab4805b47b5 FOREIGN KEY (experimentgroupid) REFERENCES expe_experimentgroup(labbookentryid);


--
-- Name: fkd0e6aab49b03e5d3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab49b03e5d3 FOREIGN KEY (experimenttypeid) REFERENCES ref_experimenttype(publicentryid);


--
-- Name: fkd0e6aab4cbb3f861; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab4cbb3f861 FOREIGN KEY (groupid) REFERENCES peop_group(labbookentryid);


--
-- Name: fkd0e6aab4e71ea01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_experiment
    ADD CONSTRAINT fkd0e6aab4e71ea01 FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid);


--
-- Name: fkd1fd763248a415ec; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ref_holdertype
    ADD CONSTRAINT fkd1fd763248a415ec FOREIGN KEY (defaultscheduleplanid) REFERENCES sche_scheduleplan(labbookentryid);


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
-- Name: fkd29339139b62c547; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT fkd29339139b62c547 FOREIGN KEY (organisationid) REFERENCES peop_organisation(labbookentryid);


--
-- Name: fkd2933913a9533f97; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY loca_location
    ADD CONSTRAINT fkd2933913a9533f97 FOREIGN KEY (locationid) REFERENCES loca_location(labbookentryid);


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
-- Name: fkd4df77f95336a9b3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT fkd4df77f95336a9b3 FOREIGN KEY (targetid) REFERENCES targ_target(labbookentryid);


--
-- Name: fkd4df77f9a4680548; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT fkd4df77f9a4680548 FOREIGN KEY (moleculeid) REFERENCES mole_molecule(abstractcomponentid);


--
-- Name: fkd4df77f9e71ea01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY trag_researchobjectiveelement
    ADD CONSTRAINT fkd4df77f9e71ea01 FOREIGN KEY (researchobjectiveid) REFERENCES targ_researchobjective(labbookentryid);


--
-- Name: fkd686dcccc405d0f0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mole_host
    ADD CONSTRAINT fkd686dcccc405d0f0 FOREIGN KEY (abstractcomponentid) REFERENCES mole_abstractcomponent(labbookentryid);


--
-- Name: fkd6ba63d419c4eb73; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument2insttype
    ADD CONSTRAINT fkd6ba63d419c4eb73 FOREIGN KEY (instrumentid) REFERENCES expe_instrument(labbookentryid);


--
-- Name: fkd6ba63d432c1d927; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY expe_instrument2insttype
    ADD CONSTRAINT fkd6ba63d432c1d927 FOREIGN KEY (instrumenttypeid) REFERENCES ref_instrumenttype(publicentryid);


--
-- Name: fkd6ff0bce45a40a7b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT fkd6ff0bce45a40a7b FOREIGN KEY (labbookentryid) REFERENCES core_labbookentry(dbid);


--
-- Name: fkd6ff0bce4e441f33; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT fkd6ff0bce4e441f33 FOREIGN KEY (holdertypeid) REFERENCES ref_abstractholdertype(publicentryid);


--
-- Name: fkd6ff0bcef334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_abstractholder
    ADD CONSTRAINT fkd6ff0bcef334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


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
-- Name: fkf0b2d05ce48c56b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sche_scheduleplanoffset
    ADD CONSTRAINT fkf0b2d05ce48c56b FOREIGN KEY (scheduleplanid) REFERENCES sche_scheduleplan(labbookentryid);


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
-- Name: fkf63805a3ab86c9f7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY core_externaldblink
    ADD CONSTRAINT fkf63805a3ab86c9f7 FOREIGN KEY (dbnameid) REFERENCES ref_dbname(publicentryid);


--
-- Name: fkf8cc82a0385506e5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acco_permission
    ADD CONSTRAINT fkf8cc82a0385506e5 FOREIGN KEY (systemclassid) REFERENCES core_systemclass(dbid);


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
-- Name: fkffe6cd2c2c9101c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT fkffe6cd2c2c9101c FOREIGN KEY (firstsampleid) REFERENCES sam_sample(abstractsampleid);


--
-- Name: fkffe6cd2cc7ef2198; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT fkffe6cd2cc7ef2198 FOREIGN KEY (lasttaskid) REFERENCES sche_scheduledtask(labbookentryid);


--
-- Name: fkffe6cd2cf334a492; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hold_holder
    ADD CONSTRAINT fkffe6cd2cf334a492 FOREIGN KEY (abstractholderid) REFERENCES hold_abstractholder(labbookentryid);


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
-- Name: acco_usergroup2user; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE acco_usergroup2user FROM PUBLIC;
REVOKE ALL ON TABLE acco_usergroup2user FROM postgres;
GRANT ALL ON TABLE acco_usergroup2user TO postgres;
GRANT ALL ON TABLE acco_usergroup2user TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE acco_usergroup2user TO pimsupdate;
GRANT SELECT ON TABLE acco_usergroup2user TO pimsview;


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
-- Name: expe_instrument2insttype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE expe_instrument2insttype FROM PUBLIC;
REVOKE ALL ON TABLE expe_instrument2insttype FROM postgres;
GRANT ALL ON TABLE expe_instrument2insttype TO postgres;
GRANT ALL ON TABLE expe_instrument2insttype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE expe_instrument2insttype TO pimsupdate;
GRANT SELECT ON TABLE expe_instrument2insttype TO pimsview;


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
GRANT SELECT,UPDATE ON TABLE generic_target TO pimsupdate;


--
-- Name: hibernate_sequence; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE hibernate_sequence FROM PUBLIC;
REVOKE ALL ON TABLE hibernate_sequence FROM postgres;
GRANT ALL ON TABLE hibernate_sequence TO postgres;
GRANT ALL ON TABLE hibernate_sequence TO pimsadmin;
GRANT SELECT ON TABLE hibernate_sequence TO pimsview;
GRANT SELECT,UPDATE ON TABLE hibernate_sequence TO pimsupdate;


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
-- Name: mole_extension; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_extension FROM PUBLIC;
REVOKE ALL ON TABLE mole_extension FROM postgres;
GRANT ALL ON TABLE mole_extension TO postgres;
GRANT ALL ON TABLE mole_extension TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_extension TO pimsupdate;
GRANT SELECT ON TABLE mole_extension TO pimsview;


--
-- Name: mole_host; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_host FROM PUBLIC;
REVOKE ALL ON TABLE mole_host FROM postgres;
GRANT ALL ON TABLE mole_host TO postgres;
GRANT ALL ON TABLE mole_host TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_host TO pimsupdate;
GRANT SELECT ON TABLE mole_host TO pimsview;


--
-- Name: mole_host2organisation; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE mole_host2organisation FROM PUBLIC;
REVOKE ALL ON TABLE mole_host2organisation FROM postgres;
GRANT ALL ON TABLE mole_host2organisation TO postgres;
GRANT ALL ON TABLE mole_host2organisation TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE mole_host2organisation TO pimsupdate;
GRANT SELECT ON TABLE mole_host2organisation TO pimsview;


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
-- Name: ref_holdertypesource; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_holdertypesource FROM PUBLIC;
REVOKE ALL ON TABLE ref_holdertypesource FROM postgres;
GRANT ALL ON TABLE ref_holdertypesource TO postgres;
GRANT ALL ON TABLE ref_holdertypesource TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_holdertypesource TO pimsupdate;
GRANT SELECT ON TABLE ref_holdertypesource TO pimsview;


--
-- Name: ref_imagetype; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE ref_imagetype FROM PUBLIC;
REVOKE ALL ON TABLE ref_imagetype FROM postgres;
GRANT ALL ON TABLE ref_imagetype TO postgres;
GRANT ALL ON TABLE ref_imagetype TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE ref_imagetype TO pimsupdate;
GRANT SELECT ON TABLE ref_imagetype TO pimsview;


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
-- Name: sam_sample; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sam_sample FROM PUBLIC;
REVOKE ALL ON TABLE sam_sample FROM postgres;
GRANT ALL ON TABLE sam_sample TO postgres;
GRANT ALL ON TABLE sam_sample TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sam_sample TO pimsupdate;
GRANT SELECT ON TABLE sam_sample TO pimsview;


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
-- Name: sche_scheduledtask; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sche_scheduledtask FROM PUBLIC;
REVOKE ALL ON TABLE sche_scheduledtask FROM postgres;
GRANT ALL ON TABLE sche_scheduledtask TO postgres;
GRANT ALL ON TABLE sche_scheduledtask TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE sche_scheduledtask TO pimsupdate;
GRANT SELECT ON TABLE sche_scheduledtask TO pimsview;


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
GRANT ALL ON TABLE targ_alias TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_alias TO pimsupdate;
GRANT SELECT ON TABLE targ_alias TO pimsview;


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
-- Name: targ_similarityhit; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE targ_similarityhit FROM PUBLIC;
REVOKE ALL ON TABLE targ_similarityhit FROM postgres;
GRANT ALL ON TABLE targ_similarityhit TO postgres;
GRANT ALL ON TABLE targ_similarityhit TO pimsadmin;
GRANT INSERT,SELECT,UPDATE,DELETE ON TABLE targ_similarityhit TO pimsupdate;
GRANT SELECT ON TABLE targ_similarityhit TO pimsview;


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
GRANT SELECT,UPDATE ON TABLE test_target TO pimsupdate;


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

