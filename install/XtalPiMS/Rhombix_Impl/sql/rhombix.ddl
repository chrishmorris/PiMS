--------------------------------------------------------
--  File created - Wednesday-December-15-2010   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type STRINGTOTABLETYPE
--------------------------------------------------------

  CREATE OR REPLACE TYPE "STRINGTOTABLETYPE" IS TABLE OF NUMBER



/

--------------------------------------------------------
--  DDL for Sequence CATEGORY_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CATEGORY_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CHEMICAL_CONC_PH_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CHEMICAL_CONC_PH_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 510 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CHEMICAL_CONC_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CHEMICAL_CONC_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 2017 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CHEMICAL_INVENTORY_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CHEMICAL_INVENTORY_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1376 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CHEMICAL_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CHEMICAL_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1679 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence CLASS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "CLASS_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence COMPARTMENT_GEOMETRY_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "COMPARTMENT_GEOMETRY_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1224 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DATA_REFRESH_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DATA_REFRESH_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 3612 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DECK_ITEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DECK_ITEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 15234 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DECK_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DECK_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 197 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DESCRIPTOR_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DESCRIPTOR_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 28 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DEVICE_ATTRIBUTE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DEVICE_ATTRIBUTE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1964 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DEVICE_CAP_ATTRIBUTE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DEVICE_CAP_ATTRIBUTE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DEVICE_CAP_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DEVICE_CAP_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DEVICE_LOC_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DEVICE_LOC_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DEVICE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DEVICE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DROP_SITE_DETAIL_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DROP_SITE_DETAIL_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 122828 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DROP_TYPE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DROP_TYPE_SEQ"  MINVALUE 0 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 20 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DYN_PH_CHEM_CONC_PH_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DYN_PH_CHEM_CONC_PH_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 30 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DYN_PH_FORMULATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DYN_PH_FORMULATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 30 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DYN_PH_FORM_TITRATIONS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DYN_PH_FORM_TITRATIONS_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 30 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence DYN_PH_FORM_TITRATION_DATA_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "DYN_PH_FORM_TITRATION_DATA_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 233 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence ERROR_LOG_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ERROR_LOG_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 11976 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence EXPERIMENT_BATCH_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "EXPERIMENT_BATCH_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 5123 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence EXPERIMENT_BATCH_SESSION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "EXPERIMENT_BATCH_SESSION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence EXPERIMENT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "EXPERIMENT_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 3439 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence EXPERIMENT_WORK_LIST_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "EXPERIMENT_WORK_LIST_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1088695 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence EXP_NOTES_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "EXP_NOTES_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence FINAL_WELL_CONTENTS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "FINAL_WELL_CONTENTS_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 237069 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence FORMULATION_CHEMICAL_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "FORMULATION_CHEMICAL_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 4524 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence FORMULATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "FORMULATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 3020 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence GRID_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "GRID_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 5156 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence IMAGE_ANNOTATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "IMAGE_ANNOTATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 350 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence IMAGE_AUTOSCORE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "IMAGE_AUTOSCORE_SEQ"  MINVALUE 0 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 0 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence IMAGE_CHARACTERISTIC_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "IMAGE_CHARACTERISTIC_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 36025 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence IMAGE_SCHED_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "IMAGE_SCHED_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 12166 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence IMAGE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "IMAGE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1357623 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence LOCATION_POINT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "LOCATION_POINT_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 4681 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence LOCATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "LOCATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 4681 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence LU_AUTOSCORE_TYPE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "LU_AUTOSCORE_TYPE_SEQ"  MINVALUE 0 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence MACROMOLECULE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MACROMOLECULE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 606 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence MACROMOLECULE_VERSION_CHEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MACROMOLECULE_VERSION_CHEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 374 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence MACROMOLECULE_VERSION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MACROMOLECULE_VERSION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1124 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence MODIFIER_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MODIFIER_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 28 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence NOTIFICATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "NOTIFICATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 10258 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PH_REAGENT_RECIP_ITEM_CHEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PH_REAGENT_RECIP_ITEM_CHEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 794 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PLATE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PLATE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 3107 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PLATE_TYPE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PLATE_TYPE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 126 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PLATE_TYPE_TRANSLATION_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PLATE_TYPE_TRANSLATION_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PROCESS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PROCESS_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PROCESS_STEP_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PROCESS_STEP_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 61 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence PROJECT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "PROJECT_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence REAGENT_RECIPE_ITEM_CHEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REAGENT_RECIPE_ITEM_CHEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 28775 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence REAGENT_RECIPE_ITEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REAGENT_RECIPE_ITEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 29568 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence REAGENT_REC_ITEM_OVERRIDE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REAGENT_REC_ITEM_OVERRIDE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence REL_IMAGE_OFFSET_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REL_IMAGE_OFFSET_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 115 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence REL_IMAGE_SCHED_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "REL_IMAGE_SCHED_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 149 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence ROLE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "ROLE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SAMPLE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SAMPLE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 23 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCREEN_RECIPE_ITEM_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SCREEN_RECIPE_ITEM_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 101 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCREEN_REC_ITEM_OVERRIDE_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SCREEN_REC_ITEM_OVERRIDE_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SCREEN_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SCREEN_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 1041 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SHAKE_PATTERN_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "SHAKE_PATTERN_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 21 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence TARGET_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "TARGET_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 29 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence USER_PROCESS_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "USER_PROCESS_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 3101 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence USER_PROCESS_STEP_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "USER_PROCESS_STEP_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 23080 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence USER_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "USER_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 150 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence VQT_QUERY_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "VQT_QUERY_SEQ"  MINVALUE 0 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 51 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence WELL_COMPARTMENT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "WELL_COMPARTMENT_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 255198 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence WELL_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "WELL_SEQ"  MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH 5436 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table BARCODE_POOL
--------------------------------------------------------

  CREATE TABLE "BARCODE_POOL" 
   (	"SITE_ID" NUMBER, 
	"BARCODE" VARCHAR2(80), 
	"STATUS" NUMBER DEFAULT 0, 
	"LU_BARCODE_TYPE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CATEGORY
--------------------------------------------------------

  CREATE TABLE "CATEGORY" 
   (	"CATEGORY_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"IS_LOCKED" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"SITE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CHEMICAL
--------------------------------------------------------

  CREATE TABLE "CHEMICAL" 
   (	"CHEMICAL_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"IS_CUSTOM" NUMBER, 
	"IS_VOLATILE" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CHEMICAL_CATEGORY
--------------------------------------------------------

  CREATE TABLE "CHEMICAL_CATEGORY" 
   (	"CHEMICAL_ID" NUMBER, 
	"CATEGORY_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CHEMICAL_CONC
--------------------------------------------------------

  CREATE TABLE "CHEMICAL_CONC" 
   (	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CONCENTRATION" NUMBER, 
	"LU_CHEM_CONC_UNITS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CHEMICAL_CONC_PH
--------------------------------------------------------

  CREATE TABLE "CHEMICAL_CONC_PH" 
   (	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"LU_PH_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CHEMICAL_INVENTORY
--------------------------------------------------------

  CREATE TABLE "CHEMICAL_INVENTORY" 
   (	"CHEMICAL_INVENTORY_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"BARCODE" VARCHAR2(80), 
	"INVENTORY_NOTES" VARCHAR2(1024), 
	"FORMULATION_ID" NUMBER, 
	"FORMULATION_SITE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table CLARIX_USER_SEARCHES
--------------------------------------------------------

  CREATE TABLE "CLARIX_USER_SEARCHES" 
   (	"USER_ID" NUMBER, 
	"PLATE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"ENTRY_DATE" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table CLASS
--------------------------------------------------------

  CREATE TABLE "CLASS" 
   (	"CLASS_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"SITE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table COMPARTMENT_GEOMETRY
--------------------------------------------------------

  CREATE TABLE "COMPARTMENT_GEOMETRY" 
   (	"COMPARTMENT_GEOMETRY_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"LU_COMPARTMENT_ID" NUMBER, 
	"OFFSET_LONG_AXIS" NUMBER, 
	"OFFSET_SHORT_AXIS" NUMBER, 
	"MAX_VOLUME" NUMBER, 
	"FLOOR_DISTANCE" NUMBER, 
	"NOMINAL_VOLUME" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"LU_DROP_TYPE_ID" NUMBER, 
	"WIDTH_LONG_AXIS" NUMBER, 
	"WIDTH_SHORT_AXIS" NUMBER, 
	"IMAGEABLE" NUMBER DEFAULT 1, 
	"LU_COMPARTMENT_SHAPE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DATA_REFRESH_METRICS
--------------------------------------------------------

  CREATE TABLE "DATA_REFRESH_METRICS" 
   (	"DRM_ID" NUMBER, 
	"JOB_NAME" VARCHAR2(32), 
	"START_TIME" DATE, 
	"END_TIME" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table DECK
--------------------------------------------------------

  CREATE TABLE "DECK" 
   (	"DECK_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"FILENAME" VARCHAR2(256), 
	"IS_CURRENT" NUMBER, 
	"IS_TEMPLATE" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DECK_ITEM
--------------------------------------------------------

  CREATE TABLE "DECK_ITEM" 
   (	"DECK_ITEM_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DECK_ID" NUMBER, 
	"LABWAREID" VARCHAR2(80), 
	"POSITIONID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"DEVICE_TIP_TYPE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"PLATE_ID" NUMBER, 
	"BARCODE" VARCHAR2(256), 
	"LU_LID_STATUS_ID" NUMBER DEFAULT 0, 
	"LID_LABWAREID" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table DESCRIPTOR
--------------------------------------------------------

  CREATE TABLE "DESCRIPTOR" 
   (	"DESCRIPTOR_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"KEYPRESS" CHAR(1), 
	"ORDINAL" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"COLOR" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE
--------------------------------------------------------

  CREATE TABLE "DEVICE" 
   (	"DEVICE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"PARENT_DEVICE_ID" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"INSTANCE" NUMBER, 
	"GROUP_NUM" NUMBER, 
	"UTILIZATION" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"IS_VIRTUAL" NUMBER, 
	"SHORT_NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE_ATTRIBUTE
--------------------------------------------------------

  CREATE TABLE "DEVICE_ATTRIBUTE" 
   (	"SITE_ID" NUMBER, 
	"DEVICE_ATTRIBUTE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(512), 
	"VALUE" VARCHAR2(2048), 
	"DEFAULT_VALUE" VARCHAR2(2048), 
	"LU_DEVICE_ATTRIBUTE_TYPE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE_CAP
--------------------------------------------------------

  CREATE TABLE "DEVICE_CAP" 
   (	"DEVICE_CAP_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"PROCESS_STEP_ID" NUMBER, 
	"COMPUTER" VARCHAR2(80), 
	"DIRECTOR" VARCHAR2(80), 
	"PROGRAM" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"DEVICE_ID" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"LU_DEVICE_CAP_TYPE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"ORDINAL" NUMBER DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE_CAP_ATTRIBUTE
--------------------------------------------------------

  CREATE TABLE "DEVICE_CAP_ATTRIBUTE" 
   (	"DEVICE_CAP_ATTRIBUTE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_CAP_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"VALUE" VARCHAR2(256), 
	"OPERATOR" VARCHAR2(3), 
	"DESCRIPTION" VARCHAR2(256), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"LU_RECORD_STATUS_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE_DISPENSE_MODE
--------------------------------------------------------

  CREATE TABLE "DEVICE_DISPENSE_MODE" 
   (	"DEVICE_DISPENSE_MODE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"LIQUID_CLASS_POSTFIX" VARCHAR2(80), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table DEVICE_TIP_TYPE
--------------------------------------------------------

  CREATE TABLE "DEVICE_TIP_TYPE" 
   (	"DEVICE_TIP_TYPE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"FILENAME" VARCHAR2(256), 
	"MAX_VOLUME" NUMBER, 
	"IS_DISPOSABLE" NUMBER, 
	"LIQUID_CLASS_PREFIX" VARCHAR2(80), 
	"TOTAL_TIPS_PER_STATION" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table DROP_SITE_DETAIL
--------------------------------------------------------

  CREATE TABLE "DROP_SITE_DETAIL" 
   (	"DROP_SITE_DETAIL_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"GRID_ID" NUMBER, 
	"LU_COMPARTMENT_ID" NUMBER, 
	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"ADDITIVE_CHEMICAL_ID" NUMBER, 
	"ADDITIVE_CHEMICAL_CONC_ID" NUMBER, 
	"ADDITIVE_CHEMICAL_CONC_PH_ID" NUMBER, 
	"ADDITIVE_SCREEN_ID" NUMBER, 
	"DROP_RATIO_PROTEIN" NUMBER, 
	"DROP_RATIO_PRECIP" NUMBER, 
	"DROP_RATIO_ADDITIVE" NUMBER, 
	"LU_DISP_ORDER_ID" NUMBER, 
	"TOTAL_DROP_VOLUME" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DYN_PH_CHEMICAL_CONC_PH
--------------------------------------------------------

  CREATE TABLE "DYN_PH_CHEMICAL_CONC_PH" 
   (	"DYN_PH_CHEMICAL_CONC_PH_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"LU_LO_PH_ID" NUMBER, 
	"LU_HI_PH_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DYN_PH_FORMULATION
--------------------------------------------------------

  CREATE TABLE "DYN_PH_FORMULATION" 
   (	"DYN_PH_FORMULATION_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DYN_PH_CHEMICAL_CONC_PH_ID" NUMBER, 
	"BA_CHEMICAL_ID" NUMBER, 
	"BA_CHEMICAL_CONC_ID" NUMBER, 
	"BA_CHEMICAL_CONC_PH_ID" NUMBER, 
	"BA_CATEGORY_ID" NUMBER, 
	"TA_CHEMICAL_ID" NUMBER, 
	"TA_CHEMICAL_CONC_ID" NUMBER, 
	"TA_CHEMICAL_CONC_PH_ID" NUMBER, 
	"TA_CATEGORY_ID" NUMBER, 
	"IS_CONJUGATE_PAIR" NUMBER, 
	"DESCRIPTION" VARCHAR2(1024), 
	"IS_PRIMARY" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"LU_LIQUID_CLASS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DYN_PH_FORM_TITRATIONS
--------------------------------------------------------

  CREATE TABLE "DYN_PH_FORM_TITRATIONS" 
   (	"DYN_PH_FORM_TITRATIONS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DYN_PH_FORMULATION_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"IS_PRIMARY" NUMBER, 
	"FINAL_VOLUME" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table DYN_PH_FORM_TITRATION_DATA
--------------------------------------------------------

  CREATE TABLE "DYN_PH_FORM_TITRATION_DATA" 
   (	"DYN_PH_FORM_TITRATION_DATA_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DYN_PH_FORM_TITRATIONS_ID" NUMBER, 
	"PH" NUMBER, 
	"BA_VOLUME" NUMBER, 
	"TA_VOLUME" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EMAIL_AUTOSCORE
--------------------------------------------------------

  CREATE TABLE "EMAIL_AUTOSCORE" 
   (	"PLATE_ID" NUMBER, 
	"IMAGE_SCHED_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table ERROR_LOG
--------------------------------------------------------

  CREATE TABLE "ERROR_LOG" 
   (	"SITE_ID" NUMBER, 
	"ERROR_LOG_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"ERROR_CODE" NUMBER, 
	"DESCRIPTION" VARCHAR2(2048), 
	"RESPONSE" CHAR(1), 
	"DIRECTOR" VARCHAR2(256), 
	"SCRIPT_FILE" VARCHAR2(1024), 
	"SCRIPT_LINE_NUM" NUMBER, 
	"CREATE_DATETIME" DATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE, 
	"UPDATE_USER_ID" NUMBER, 
	"NOTIFY_DATETIME" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table ERROR_LOG_ARCHIVE
--------------------------------------------------------

  CREATE TABLE "ERROR_LOG_ARCHIVE" 
   (	"SITE_ID" NUMBER, 
	"ERROR_LOG_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"ERROR_CODE" NUMBER, 
	"DESCRIPTION" VARCHAR2(2048), 
	"RESPONSE" CHAR(1), 
	"DIRECTOR" VARCHAR2(256), 
	"SCRIPT_FILE" VARCHAR2(1024), 
	"SCRIPT_LINE_NUM" NUMBER, 
	"CREATE_DATETIME" DATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE, 
	"UPDATE_USER_ID" NUMBER, 
	"NOTIFY_DATETIME" DATE, 
	"TRANSFER_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT" 
   (	"EXPERIMENT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"LU_EXPERIMENT_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(1024), 
	"PLATE_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"REL_IMAGE_SCHED_ID" NUMBER, 
	"LU_EXPERIMENT_STATUS_ID" NUMBER, 
	"TEMPERATURE" NUMBER, 
	"FILL_VOLUME" NUMBER, 
	"SUBMIT_DATETIME" DATE, 
	"FILL_CHEMICAL_ID" NUMBER, 
	"FILL_CHEMICAL_CONC_ID" NUMBER, 
	"FILL_CHEMICAL_CONC_PH_ID" NUMBER, 
	"FILL_BEFORE" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"RUN_STATUS" VARCHAR2(1024), 
	"PROJECT_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT_BATCH
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT_BATCH" 
   (	"EXPERIMENT_BATCH_ID" NUMBER, 
	"SESSION_NUM" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"ORDER_NUM" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT_NOTES
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT_NOTES" 
   (	"SITE_ID" NUMBER, 
	"EXPERIMENT_NOTE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"NOTE" BLOB, 
	"DATE_ADDED" DATE DEFAULT sysdate, 
	"USER_ADDED" NUMBER, 
	"DATE_EDIT" DATE, 
	"USER_EDIT" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT_TYPE_PROCESS
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT_TYPE_PROCESS" 
   (	"EXPERIMENT_TYPE_PROCESS_ID" NUMBER, 
	"LU_EXPERIMENT_TYPE_ID" NUMBER, 
	"PROCESS_ID" NUMBER, 
	"SITE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT_WORK_LIST
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT_WORK_LIST" 
   (	"EXPERIMENT_WORK_LIST_ID" NUMBER, 
	"USER_PROCESS_STEP_ID" NUMBER, 
	"LU_WORK_LIST_ORDER_ID" NUMBER, 
	"IS_VOLATILE" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"LU_BUILD_TYPE_ID" NUMBER, 
	"DEST_ROW" NUMBER, 
	"DEST_COLUMN" NUMBER, 
	"DEST_LU_COMPARTMENT_ID" NUMBER, 
	"VOLUME" NUMBER, 
	"IS_AUTOMATION_BUILT" NUMBER, 
	"LU_LIQUID_CLASS_ID" NUMBER, 
	"COMPLETE_DATETIME" DATE, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table EXPERIMENT_WORK_LIST_ARCHIVE
--------------------------------------------------------

  CREATE TABLE "EXPERIMENT_WORK_LIST_ARCHIVE" 
   (	"EXPERIMENT_WORK_LIST_ID" NUMBER, 
	"USER_PROCESS_STEP_ID" NUMBER, 
	"LU_WORK_LIST_ORDER_ID" NUMBER, 
	"IS_VOLATILE" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"LU_BUILD_TYPE_ID" NUMBER, 
	"DEST_ROW" NUMBER, 
	"DEST_COLUMN" NUMBER, 
	"DEST_LU_COMPARTMENT_ID" NUMBER, 
	"VOLUME" NUMBER, 
	"IS_AUTOMATION_BUILT" NUMBER, 
	"LU_LIQUID_CLASS_ID" NUMBER, 
	"COMPLETE_DATETIME" DATE, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"TRANSFER_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table FINAL_WELL_CONTENTS
--------------------------------------------------------

  CREATE TABLE "FINAL_WELL_CONTENTS" 
   (	"FINAL_WELL_CONTENTS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"CATEGORY_ID" NUMBER, 
	"ROW_NUM" NUMBER, 
	"COLUMN_NUM" NUMBER, 
	"FINAL_CONCENTRATION" NUMBER, 
	"UPDATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"IS_CONSTITUENT" NUMBER DEFAULT 0, 
	"FINAL_PH" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table FORMULATION
--------------------------------------------------------

  CREATE TABLE "FORMULATION" 
   (	"FORMULATION_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(1024), 
	"IS_PRIMARY" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"LU_LIQUID_CLASS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"IS_CONSTITUENT" NUMBER DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table FORMULATION_CHEMICAL
--------------------------------------------------------

  CREATE TABLE "FORMULATION_CHEMICAL" 
   (	"FORMULATION_CHEMICAL_ID" NUMBER, 
	"FORMULATION_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CATEGORY_ID" NUMBER, 
	"LU_CHEM_CONC_UNITS_ID" NUMBER, 
	"FINAL_CONCENTRATION" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table GRID
--------------------------------------------------------

  CREATE TABLE "GRID" 
   (	"GRID_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"OWNER_SCREEN_ID" NUMBER, 
	"PARENT_SCREEN_ID" NUMBER, 
	"PARENT_WELL_ROW" NUMBER, 
	"PARENT_WELL_COLUMN" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"START_WELL_ROW" NUMBER, 
	"END_WELL_ROW" NUMBER, 
	"START_WELL_COLUMN" NUMBER, 
	"END_WELL_COLUMN" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table IMAGE
--------------------------------------------------------

  CREATE TABLE "IMAGE" 
   (	"IMAGE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"IMAGE_SCHED_ID" NUMBER, 
	"WELL_COMPARTMENT_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"FILENAME" VARCHAR2(1024), 
	"MILLIMETERS_PER_PIXEL" NUMBER, 
	"IMAGE_INDEX" NUMBER, 
	"LU_IMAGE_TYPE_ID" NUMBER, 
	"LU_IMAGE_FORMAT_MASK" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"POLARIZATION_ANGLE" NUMBER, 
	"OFFSET_LONG_AXIS" NUMBER, 
	"OFFSET_SHORT_AXIS" NUMBER, 
	"FOCUS_HEIGHT" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table IMAGE_ANNOTATION
--------------------------------------------------------

  CREATE TABLE "IMAGE_ANNOTATION" 
   (	"IMAGE_ANNOTATION_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"IMAGE_SCHED_ID" NUMBER, 
	"WELL_COMPARTMENT_ID" NUMBER, 
	"NOTES" VARCHAR2(1024), 
	"SCORE" NUMBER, 
	"LU_IMAGE_ANNOTATION_METHOD_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table IMAGE_AUTOSCORE
--------------------------------------------------------

  CREATE TABLE "IMAGE_AUTOSCORE" 
   (	"IMAGE_AUTOSCORE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"LU_AUTOSCORE_TYPE_ID" NUMBER, 
	"IMAGE_ID" NUMBER, 
	"AUTOSCORE_VALUE_1" NUMBER, 
	"AUTOSCORE_VALUE_2" NUMBER, 
	"AUTOSCORE_VALUE_3" NUMBER, 
	"START_DATETIME" DATE DEFAULT SYSDATE, 
	"END_DATETIME" DATE, 
	"RESULT_CODE" NUMBER, 
	"RESULT_MESSAGE" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table IMAGE_CHARACTERISTIC
--------------------------------------------------------

  CREATE TABLE "IMAGE_CHARACTERISTIC" 
   (	"IMAGE_CHARACTERISTIC_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"IMAGE_SCHED_ID" NUMBER, 
	"WELL_COMPARTMENT_ID" NUMBER, 
	"DESCRIPTOR_ID" NUMBER, 
	"MODIFIER_ID" NUMBER, 
	"LU_IMAGE_ANNOTATION_METHOD_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table IMAGE_SCHED
--------------------------------------------------------

  CREATE TABLE "IMAGE_SCHED" 
   (	"IMAGE_SCHED_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"PLATE_ID" NUMBER, 
	"LU_IMAGE_SCHED_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"SCHEDULED_START_DATETIME" DATE, 
	"ACTUAL_START_DATETIME" DATE, 
	"ACTUAL_END_DATETIME" DATE, 
	"OFFSET_HOURS" NUMBER, 
	"LU_IMAGE_SETTING_MASK" NUMBER, 
	"LU_IMAGE_TYPE_MASK" NUMBER, 
	"IMAGE_SITE_MASK" VARCHAR2(1536), 
	"POLAR_START_ANGLE" NUMBER, 
	"POLAR_COUNT" NUMBER, 
	"POLAR_ANGLE_DELTA" NUMBER, 
	"LU_IMAGE_FORMAT_ID" NUMBER, 
	"NOTIFICATION_ID" NUMBER, 
	"JPEG_QUALITY" NUMBER, 
	"EDIT_USER_ID" NUMBER, 
	"EDIT_DATETIME" DATE, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table JOB_INTERVAL
--------------------------------------------------------

  CREATE TABLE "JOB_INTERVAL" 
   (	"JOBNAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"ACTIVE" NUMBER, 
	"INTERVAL_TYPE" NUMBER, 
	"START_DATE" DATE, 
	"EVERYDAY" NUMBER, 
	"WEEKDAYS" NUMBER, 
	"EVERYNTHDAY" NUMBER, 
	"EVERYNTHWEEK" NUMBER, 
	"WEEKDAYSARRAY" NUMBER, 
	"MONTHDAY" NUMBER, 
	"EVERYNTHMONTHDAY" NUMBER, 
	"MONTHWEEKDAY" NUMBER, 
	"MONTHARRAY" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LOCATION
--------------------------------------------------------

  CREATE TABLE "LOCATION" 
   (	"LOCATION_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_ID" NUMBER, 
	"LU_LOCATION_STATUS_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DISPLAY_NAME" VARCHAR2(80), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LOCATION_POINT
--------------------------------------------------------

  CREATE TABLE "LOCATION_POINT" 
   (	"LOCATION_POINT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"LOCATION_ID" NUMBER, 
	"IS_ENABLED" NUMBER, 
	"NAME" VARCHAR2(10), 
	"ROBOT_DEVICE_ID" NUMBER, 
	"LU_POINT_TYPE_MASK" NUMBER, 
	"GRIPPER_WIDTH" NUMBER, 
	"LU_TYPE_SIZE_MASK" NUMBER, 
	"MAT_ROW" NUMBER, 
	"MAT_COLUMN" NUMBER, 
	"REQUIRES_OFFSET" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"TEMPERATURE" NUMBER, 
	"PLATE_HEIGHT_MIN" NUMBER DEFAULT 0, 
	"PLATE_HEIGHT_MAX" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LU_AUTOSCORE_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_AUTOSCORE_TYPE" 
   (	"LU_AUTOSCORE_TYPE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(30), 
	"DESCRIPTION" VARCHAR2(80), 
	"LU_IMAGE_TYPE_MASK" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"VALUE_COUNT" NUMBER DEFAULT 1
   ) ;
--------------------------------------------------------
--  DDL for Table LU_BARCODE_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_BARCODE_TYPE" 
   (	"LU_BARCODE_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_BUILD_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_BUILD_TYPE" 
   (	"LU_BUILD_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_CHEM_CONC_UNITS
--------------------------------------------------------

  CREATE TABLE "LU_CHEM_CONC_UNITS" 
   (	"LU_CHEM_CONC_UNITS_ID" NUMBER, 
	"ABBR" VARCHAR2(3), 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_COMPARTMENT
--------------------------------------------------------

  CREATE TABLE "LU_COMPARTMENT" 
   (	"LU_COMPARTMENT_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_COMPARTMENT_SHAPE
--------------------------------------------------------

  CREATE TABLE "LU_COMPARTMENT_SHAPE" 
   (	"LU_COMPARTMENT_SHAPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DEVICE_ATTRIBUTE_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_DEVICE_ATTRIBUTE_TYPE" 
   (	"LU_DEVICE_ATTRIBUTE_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DEVICE_CAP_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_DEVICE_CAP_TYPE" 
   (	"LU_DEVICE_CAP_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DISP_ORDER
--------------------------------------------------------

  CREATE TABLE "LU_DISP_ORDER" 
   (	"LU_DISP_ORDER_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80), 
	"NUM_DROP_COMPONENTS" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DIST_PATTERN
--------------------------------------------------------

  CREATE TABLE "LU_DIST_PATTERN" 
   (	"LU_DIST_PATTERN_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DIST_SCOPE
--------------------------------------------------------

  CREATE TABLE "LU_DIST_SCOPE" 
   (	"LU_DIST_SCOPE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_DROP_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_DROP_TYPE" 
   (	"LU_DROP_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_EXPERIMENT_STATUS
--------------------------------------------------------

  CREATE TABLE "LU_EXPERIMENT_STATUS" 
   (	"LU_EXPERIMENT_STATUS_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_EXPERIMENT_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_EXPERIMENT_TYPE" 
   (	"LU_EXPERIMENT_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80), 
	"DEFAULT_PROCESS_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LU_IMAGE_ANNOTATION_METHOD
--------------------------------------------------------

  CREATE TABLE "LU_IMAGE_ANNOTATION_METHOD" 
   (	"LU_IMAGE_ANNOTATION_METHOD_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_IMAGE_FORMAT
--------------------------------------------------------

  CREATE TABLE "LU_IMAGE_FORMAT" 
   (	"LU_IMAGE_FORMAT_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_IMAGE_SCHED_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_IMAGE_SCHED_TYPE" 
   (	"LU_IMAGE_SCHED_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"IS_VISIBLE" NUMBER, 
	"IS_STANDALONE" NUMBER DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table LU_IMAGE_SETTING
--------------------------------------------------------

  CREATE TABLE "LU_IMAGE_SETTING" 
   (	"LU_IMAGE_SETTING_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_IMAGE_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_IMAGE_TYPE" 
   (	"LU_IMAGE_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_LID_STATUS
--------------------------------------------------------

  CREATE TABLE "LU_LID_STATUS" 
   (	"LU_LID_STATUS_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(64)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_LIQUID_CLASS
--------------------------------------------------------

  CREATE TABLE "LU_LIQUID_CLASS" 
   (	"LU_LIQUID_CLASS_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"WEIGHT" NUMBER, 
	"LIQUID_CLASS_MIDFIX" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_LIQ_CLASS__DEV_DISP_MODE
--------------------------------------------------------

  CREATE TABLE "LU_LIQ_CLASS__DEV_DISP_MODE" 
   (	"LU_LIQUID_CLASS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"DEVICE_TIP_TYPE_ID" NUMBER, 
	"DEVICE_DISPENSE_MODE_ID" NUMBER, 
	"ASP_CLLD" NUMBER DEFAULT 0, 
	"ASP_PLLD" NUMBER DEFAULT 0, 
	"ASP_LLD_SUBMERGE_DEPTH" NUMBER DEFAULT 0, 
	"ASP_LLD_MAX_DIFF" NUMBER DEFAULT 0, 
	"DISP_CLLD" NUMBER DEFAULT 0, 
	"DISP_LLD_SUBMERGE_DEPTH" NUMBER DEFAULT 0, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table LU_LOCATION_STATUS
--------------------------------------------------------

  CREATE TABLE "LU_LOCATION_STATUS" 
   (	"LU_LOCATION_STATUS_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_NOTIFICATION_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_NOTIFICATION_TYPE" 
   (	"LU_NOTIFICATION_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_PH
--------------------------------------------------------

  CREATE TABLE "LU_PH" 
   (	"LU_PH_ID" NUMBER, 
	"VALUE" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table LU_POINT_TYPE
--------------------------------------------------------

  CREATE TABLE "LU_POINT_TYPE" 
   (	"LU_POINT_TYPE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_PROCESS_STEP_MODE
--------------------------------------------------------

  CREATE TABLE "LU_PROCESS_STEP_MODE" 
   (	"LU_PROCESS_STEP_MODE_ID" NUMBER, 
	"NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_RECORD_STATUS
--------------------------------------------------------

  CREATE TABLE "LU_RECORD_STATUS" 
   (	"LU_RECORD_STATUS_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_TYPE_SIZE
--------------------------------------------------------

  CREATE TABLE "LU_TYPE_SIZE" 
   (	"LU_TYPE_SIZE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256)
   ) ;
--------------------------------------------------------
--  DDL for Table LU_WORK_LIST_ORDER
--------------------------------------------------------

  CREATE TABLE "LU_WORK_LIST_ORDER" 
   (	"LU_WORK_LIST_ORDER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MACROMOLECULE
--------------------------------------------------------

  CREATE TABLE "MACROMOLECULE" 
   (	"MACROMOLECULE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"TARGET_ID" NUMBER, 
	"SAMPLE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MACROMOLECULE_CLASS
--------------------------------------------------------

  CREATE TABLE "MACROMOLECULE_CLASS" 
   (	"MACROMOLECULE_ID" NUMBER, 
	"CLASS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MACROMOLECULE_VERSION
--------------------------------------------------------

  CREATE TABLE "MACROMOLECULE_VERSION" 
   (	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"BUFFER_CHEMICAL_ID" NUMBER, 
	"BUFFER_CHEMICAL_CONC_ID" NUMBER, 
	"BUFFER_CHEMICAL_CONC_PH_ID" NUMBER, 
	"BUFFER_FINAL_CONC" VARCHAR2(256), 
	"SAMPLE_VERSION" VARCHAR2(80), 
	"SAMPLE_VERSION_DESC" VARCHAR2(256), 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"MACROMOLECULE_ID" NUMBER, 
	"NEW_CONCENTRATION" VARCHAR2(256), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MACROMOLECULE_VERSION_CHEMICAL
--------------------------------------------------------

  CREATE TABLE "MACROMOLECULE_VERSION_CHEMICAL" 
   (	"MACROMOLECULE_VERSION_CHEM_ID" NUMBER, 
	"MACROMOLECULE_VERSION_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"CATEGORY_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table MODIFIER
--------------------------------------------------------

  CREATE TABLE "MODIFIER" 
   (	"MODIFIER_ID" NUMBER, 
	"DESCRIPTOR_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"ORDINAL" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"COLOR" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"KEYPRESS" CHAR(1)
   ) ;
--------------------------------------------------------
--  DDL for Table NOTIFICATION
--------------------------------------------------------

  CREATE TABLE "NOTIFICATION" 
   (	"NOTIFICATION_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"ADDRESSEE" VARCHAR2(300), 
	"SUBJECT" VARCHAR2(300), 
	"MESSAGE" VARCHAR2(500), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PH_REAGENT_RECIPE_ITEM_CHEM
--------------------------------------------------------

  CREATE TABLE "PH_REAGENT_RECIPE_ITEM_CHEM" 
   (	"PH_REAGENT_RECIPE_ITEM_CHEM_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"REAGENT_RECIPE_ITEM_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"DYN_PH_CHEMICAL_CONC_PH_ID" NUMBER, 
	"STEP_NUM" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"COLOR" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PLAN_TABLE
--------------------------------------------------------

  CREATE TABLE "PLAN_TABLE" 
   (	"STATEMENT_ID" VARCHAR2(30), 
	"TIMESTAMP" DATE, 
	"REMARKS" VARCHAR2(80), 
	"OPERATION" VARCHAR2(30), 
	"OPTIONS" VARCHAR2(30), 
	"OBJECT_NODE" VARCHAR2(128), 
	"OBJECT_OWNER" VARCHAR2(30), 
	"OBJECT_NAME" VARCHAR2(30), 
	"OBJECT_INSTANCE" NUMBER(*,0), 
	"OBJECT_TYPE" VARCHAR2(30), 
	"OPTIMIZER" VARCHAR2(255), 
	"SEARCH_COLUMNS" NUMBER, 
	"ID" NUMBER(*,0), 
	"PARENT_ID" NUMBER(*,0), 
	"POSITION" NUMBER(*,0), 
	"COST" NUMBER(*,0), 
	"CARDINALITY" NUMBER(*,0), 
	"BYTES" NUMBER(*,0), 
	"OTHER_TAG" VARCHAR2(255), 
	"PARTITION_START" VARCHAR2(255), 
	"PARTITION_STOP" VARCHAR2(255), 
	"PARTITION_ID" NUMBER(*,0), 
	"OTHER" LONG, 
	"DISTRIBUTION" VARCHAR2(30), 
	"CPU_COST" NUMBER(*,0), 
	"IO_COST" NUMBER(*,0), 
	"TEMP_SPACE" NUMBER(*,0)
   ) ;
--------------------------------------------------------
--  DDL for Table PLATE
--------------------------------------------------------

  CREATE TABLE "PLATE" 
   (	"PLATE_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"BARCODE" VARCHAR2(256), 
	"DESCRIPTION" VARCHAR2(1024), 
	"LOCATION_ID" NUMBER, 
	"IMAGE_SCHED_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"STORAGE_DEVICE_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PLATE_TYPE
--------------------------------------------------------

  CREATE TABLE "PLATE_TYPE" 
   (	"PLATE_TYPE_ID" NUMBER, 
	"LU_TYPE_SIZE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"TYPE_NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"HEIGHT" NUMBER, 
	"NUM_WELL_COMPARTMENTS" NUMBER, 
	"NUM_WELLS_LONG_AXIS" NUMBER, 
	"NUM_WELLS_SHORT_AXIS" NUMBER, 
	"WELL_PITCH_LONG_AXIS" NUMBER, 
	"WELL_PITCH_SHORT_AXIS" NUMBER, 
	"WELL_VOLUME" NUMBER, 
	"SATELLITE_VOLUME" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"WELL_MASK" VARCHAR2(1536), 
	"WELL_MAP" VARCHAR2(1536), 
	"LABWARE_FILENAME" VARCHAR2(256), 
	"PLATE_Z_OFFSET" NUMBER DEFAULT 0, 
	"IS_VISIBLE" NUMBER DEFAULT 1, 
	"SPACER_PLATE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"LID_LABWARE_FILENAME" VARCHAR2(256), 
	"REQUIRES_OFFSET" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PLATE_TYPE_TRANSLATION
--------------------------------------------------------

  CREATE TABLE "PLATE_TYPE_TRANSLATION" 
   (	"PLATE_TYPE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"LU_COMPARTMENT_ID" NUMBER, 
	"WELL_ID" NUMBER, 
	"POSITIONID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table PLATE_VOL_HEIGHT
--------------------------------------------------------

  CREATE TABLE "PLATE_VOL_HEIGHT" 
   (	"SITE_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"LU_COMPARTMENT_ID" NUMBER, 
	"VOLUME" NUMBER, 
	"HEIGHT" NUMBER, 
	"UPDATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table PROCESS
--------------------------------------------------------

  CREATE TABLE "PROCESS" 
   (	"PROCESS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table PROCESS_STEP
--------------------------------------------------------

  CREATE TABLE "PROCESS_STEP" 
   (	"PROCESS_STEP_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"PROCESS_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"LU_PROCESS_STEP_MODE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"ORDINAL" NUMBER, 
	"REQUIRES_MOTION" NUMBER, 
	"PRIORITY" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table PROJECT
--------------------------------------------------------

  CREATE TABLE "PROJECT" 
   (	"SITE_ID" NUMBER, 
	"PROJECT_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(1024), 
	"CREATE_USER_DATETIME" DATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table REAGENT_RECIPE_ITEM
--------------------------------------------------------

  CREATE TABLE "REAGENT_RECIPE_ITEM" 
   (	"REAGENT_RECIPE_ITEM_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"GRID_ID" NUMBER, 
	"CATEGORY_ID" NUMBER, 
	"LU_DIST_SCOPE_ID" NUMBER, 
	"LU_DIST_PATTERN_ID" NUMBER, 
	"DIST_VALUE1" NUMBER, 
	"DIST_VALUE2" NUMBER, 
	"SCOPE_ROW_NUM" NUMBER, 
	"SCOPE_COL_NUM" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"DIST_VALUE3" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table REAGENT_RECIPE_ITEM_CHEM
--------------------------------------------------------

  CREATE TABLE "REAGENT_RECIPE_ITEM_CHEM" 
   (	"REAGENT_RECIPE_ITEM_CHEM_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"REAGENT_RECIPE_ITEM_ID" NUMBER, 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_CONC_ID" NUMBER, 
	"CHEMICAL_CONC_PH_ID" NUMBER, 
	"STEP_NUM" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"COLOR" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table REAGENT_RECIPE_ITEM_OVERRIDE
--------------------------------------------------------

  CREATE TABLE "REAGENT_RECIPE_ITEM_OVERRIDE" 
   (	"REAGENT_REC_ITEM_OVERRIDE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"REAGENT_RECIPE_ITEM_CHEM_ID" NUMBER, 
	"STEP_NUM" NUMBER, 
	"VALUE" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table REL_IMAGE_OFFSET
--------------------------------------------------------

  CREATE TABLE "REL_IMAGE_OFFSET" 
   (	"REL_IMAGE_OFFSET_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"REL_IMAGE_SCHED_ID" NUMBER, 
	"LU_IMAGE_SCHED_TYPE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"OFFSET_HOURS" NUMBER, 
	"LU_IMAGE_SETTING_MASK" NUMBER, 
	"LU_IMAGE_TYPE_MASK" NUMBER, 
	"POLAR_START_ANGLE" NUMBER, 
	"POLAR_COUNT" NUMBER, 
	"POLAR_ANGLE_DELTA" NUMBER, 
	"LU_IMAGE_FORMAT_ID" NUMBER, 
	"NOTIFICATION_ID" NUMBER, 
	"JPEG_QUALITY" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table REL_IMAGE_SCHED
--------------------------------------------------------

  CREATE TABLE "REL_IMAGE_SCHED" 
   (	"REL_IMAGE_SCHED_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table ROLES
--------------------------------------------------------

  CREATE TABLE "ROLES" 
   (	"ROLE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"ROLE_NAME" VARCHAR2(255), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table RPT_CHEMICAL
--------------------------------------------------------

  CREATE TABLE "RPT_CHEMICAL" 
   (	"CHEMICAL_SITE_ID" NUMBER, 
	"CHEMICAL_SITE_NAME" VARCHAR2(80), 
	"CHEMICAL_ID" NUMBER, 
	"CHEMICAL_NAME" VARCHAR2(80), 
	"CHEMICAL_DESCRIPTION" VARCHAR2(256), 
	"CHEMICAL_CATEGORY" VARCHAR2(80), 
	"CHEMICAL_IS_CUSTOM" VARCHAR2(1), 
	"CHEMICAL_IS_VOLATILE" VARCHAR2(1), 
	"VERSION_CONC" NUMBER, 
	"VERSION_CONC_UNITS" VARCHAR2(3), 
	"VERSION_PH_LOW" NUMBER, 
	"VERSION_PH_HIGH" NUMBER, 
	"VERSION_VISCOSITY" VARCHAR2(80), 
	"FORMULATION_ID" NUMBER, 
	"FORMULATION_IS_PRIMARY" VARCHAR2(1), 
	"FORMULATION_IS_CONSTITUENT" VARCHAR2(1), 
	"FORMULATION_STATUS" VARCHAR2(8), 
	"FORMULATION_CHEM_NAME" VARCHAR2(80), 
	"FORMULATION_CHEM_CONC" NUMBER, 
	"FORMULATION_CHEM_CONC_UNITS" VARCHAR2(3), 
	"FORMULATION_CHEM_PH" NUMBER, 
	"FORMULATION_CHEM_CATEGORY" VARCHAR2(80), 
	"FORMULATION_CHEM_FINAL_CONC" NUMBER, 
	"FORMULATION_BARCODE" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table RPT_EXPERIMENT
--------------------------------------------------------

  CREATE TABLE "RPT_EXPERIMENT" 
   (	"EXPERIMENT_SITE_ID" NUMBER, 
	"EXPERIMENT_SITE_NAME" VARCHAR2(80), 
	"EXPERIMENT_ID" NUMBER, 
	"SCREEN_ID" NUMBER, 
	"GRID_ID" NUMBER, 
	"EXPERIMENT_NAME" VARCHAR2(80), 
	"EXPERIMENT_DESCRIPTION" VARCHAR2(1024), 
	"EXPERIMENT_OWNER" VARCHAR2(16), 
	"EXPERIMENT_TYPE" VARCHAR2(80), 
	"EXPERIMENT_BARCODE" VARCHAR2(256), 
	"EXPERIMENT_STATUS" VARCHAR2(80), 
	"EXPERIMENT_SUBMIT_DATETIME" DATE, 
	"EXPERIMENT_PLATE_TYPE" VARCHAR2(80), 
	"EXPERIMENT_WELL_VOLUME" NUMBER, 
	"EXPERIMENT_TEMPERATURE" NUMBER, 
	"EXPERIMENT_SCREEN_NAME" VARCHAR2(80), 
	"DROP_SITE_NAME" VARCHAR2(256), 
	"DROP_VOLUME" NUMBER, 
	"DROP_MACROMOLECULE_NAME" VARCHAR2(80), 
	"DROP_MACROMOLECULE_TARGET" VARCHAR2(80), 
	"DROP_MACROMOLECULE_SAMPLE" VARCHAR2(80), 
	"DROP_MACROMOLECULE_VERSION" VARCHAR2(80), 
	"DROP_MACROMOLECULE_VOLUME" NUMBER, 
	"DROP_PRECIPITANT_VOLUME" NUMBER, 
	"DROP_ADDITIVE_CHEM_NAME" VARCHAR2(80), 
	"DROP_ADDITIVE_CHEM_CONC" NUMBER, 
	"DROP_ADDITIVE_CHEM_CONC_UNITS" VARCHAR2(3), 
	"DROP_ADDITIVE_CHEM_PH" NUMBER, 
	"DROP_ADDITIVE_CHEM_VOLUME" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table RPT_IMAGE
--------------------------------------------------------

  CREATE TABLE "RPT_IMAGE" 
   (	"IMAGE_SITE_ID" NUMBER, 
	"IMAGE_SITE_NAME" VARCHAR2(80), 
	"IMAGE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"IMAGE_EVENT_DATETIME" DATE, 
	"WELL_NAME" VARCHAR2(4), 
	"DROP_SITE_NAME" VARCHAR2(256), 
	"IMAGE_TYPE" VARCHAR2(80), 
	"IMAGE_INDEX" NUMBER, 
	"IMAGE_FILENAME" VARCHAR2(1281), 
	"ANNOTATION_DESCRIPTOR" VARCHAR2(80), 
	"ANNOTATION_MODIFIER" VARCHAR2(80), 
	"ANNOTATION_NOTES" VARCHAR2(1024)
   ) ;
--------------------------------------------------------
--  DDL for Table RPT_MACROMOLECULE
--------------------------------------------------------

  CREATE TABLE "RPT_MACROMOLECULE" 
   (	"MAC_SITE_ID" NUMBER, 
	"MAC_SITE_NAME" VARCHAR2(80), 
	"MAC_ID" NUMBER, 
	"MAC_NAME" VARCHAR2(80), 
	"MAC_DESCRIPTION" VARCHAR2(256), 
	"MAC_CLASS" VARCHAR2(80), 
	"MAC_TARGET_ID" VARCHAR2(80), 
	"MAC_SAMPLE_ID" VARCHAR2(80), 
	"MAC_SAMPLE_DESCRIPTION" VARCHAR2(2048), 
	"VERSION_NAME" VARCHAR2(80), 
	"VERSION_IS_ACTIVE" VARCHAR2(1), 
	"VERSION_BUFFER_NAME" VARCHAR2(80), 
	"VERSION_BUFFER_FINAL_CONC" VARCHAR2(256), 
	"VERSION_PROTEIN_FINAL_CONC" VARCHAR2(256), 
	"VERSION_CHEM_NAME" VARCHAR2(80), 
	"VERSION_CHEM_IS_LIGAND" VARCHAR2(1)
   ) ;
--------------------------------------------------------
--  DDL for Table RPT_SCREEN
--------------------------------------------------------

  CREATE TABLE "RPT_SCREEN" 
   (	"SCREEN_SITE_ID" NUMBER, 
	"SCREEN_SITE_NAME" VARCHAR2(80), 
	"SCREEN_ID" NUMBER, 
	"GRID_ID" NUMBER, 
	"SCREEN_NAME" VARCHAR2(80), 
	"SCREEN_DESCRIPTION" VARCHAR2(256), 
	"SCREEN_IS_CUSTOM" VARCHAR2(1), 
	"SCREEN_IS_ADDITIVE" VARCHAR2(1), 
	"SCREEN_IS_PUBLISHED" VARCHAR2(1), 
	"SCREEN_STATUS" VARCHAR2(8), 
	"GRID_BOUNDARY" VARCHAR2(9), 
	"GRID_PARENT_SCREEN_NAME" VARCHAR2(80), 
	"GRID_PARENT_WELL_NAME" VARCHAR2(4), 
	"WELL_NAME" VARCHAR2(4), 
	"WELL_CHEM_NAME" VARCHAR2(80), 
	"WELL_CHEM_CONC" NUMBER, 
	"WELL_CHEM_CONC_UNITS" VARCHAR2(3), 
	"WELL_CHEM_PH" NUMBER, 
	"WELL_CHEM_CATEGORY" VARCHAR2(80), 
	"WELL_CHEM_FINAL_CONC" NUMBER, 
	"CONSTITUENT_CHEM_NAME" VARCHAR2(80), 
	"CONSTITUENT_CHEM_CONC" NUMBER, 
	"CONSTITUENT_CHEM_CONC_UNITS" VARCHAR2(3), 
	"CONSTITUENT_CHEM_PH" NUMBER, 
	"CONSTITUENT_CHEM_CATEGORY" VARCHAR2(80), 
	"CONSTITUENT_CHEM_FINAL_CONC" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SAMPLE
--------------------------------------------------------

  CREATE TABLE "SAMPLE" 
   (	"SAMPLE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(2048), 
	"TARGET_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SCHEMA_VERSION
--------------------------------------------------------

  CREATE TABLE "SCHEMA_VERSION" 
   (	"MAJOR" NUMBER, 
	"MINOR" NUMBER, 
	"COMMENTS" VARCHAR2(1024), 
	"SCRIPTING_DATE" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table SCREEN
--------------------------------------------------------

  CREATE TABLE "SCREEN" 
   (	"SCREEN_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"IS_PUBLISHED" NUMBER DEFAULT 0, 
	"IS_CUSTOM" NUMBER, 
	"IS_ADDITIVE" NUMBER, 
	"DESCRIPTION" VARCHAR2(256), 
	"MAX_ROW" NUMBER, 
	"MAX_COLUMN" NUMBER, 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SCREEN_RECIPE_ITEM
--------------------------------------------------------

  CREATE TABLE "SCREEN_RECIPE_ITEM" 
   (	"SCREEN_RECIPE_ITEM_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"GRID_ID" NUMBER, 
	"SOURCE_SCREEN_ID" NUMBER, 
	"PERCENT_WELL_VOLUME" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"COLOR" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SCREEN_RECIPE_ITEM_OVERRIDE
--------------------------------------------------------

  CREATE TABLE "SCREEN_RECIPE_ITEM_OVERRIDE" 
   (	"SCREEN_RECIPE_ITEM_OVERRIDE_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"SCREEN_RECIPE_ITEM_ID" NUMBER, 
	"STEP_NUM" NUMBER, 
	"VALUE" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table SHAKE_PATTERN
--------------------------------------------------------

  CREATE TABLE "SHAKE_PATTERN" 
   (	"SHAKE_PATTERN_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"NAME" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(80), 
	"PATTERN" VARCHAR2(200), 
	"CREATE_USER_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table SITE
--------------------------------------------------------

  CREATE TABLE "SITE" 
   (	"SITE_ID" NUMBER, 
	"DESCRIPTION" VARCHAR2(80), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE
   ) ;
--------------------------------------------------------
--  DDL for Table TARGET
--------------------------------------------------------

  CREATE TABLE "TARGET" 
   (	"TARGET_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"TARGET" VARCHAR2(80), 
	"DESCRIPTION" VARCHAR2(256), 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

  CREATE TABLE "USERS" 
   (	"USER_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"USERNAME" VARCHAR2(16), 
	"PASSWORD" VARCHAR2(50), 
	"FIRST_NAME" VARCHAR2(16), 
	"MIDDLE_NAME" VARCHAR2(20), 
	"LAST_NAME" VARCHAR2(20), 
	"DESCRIPTION" VARCHAR2(80), 
	"EMAIL" VARCHAR2(50), 
	"LU_RECORD_STATUS_ID" NUMBER, 
	"IS_SYSTEM_ACCOUNT" NUMBER DEFAULT 0, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table USER_PROCESS
--------------------------------------------------------

  CREATE TABLE "USER_PROCESS" 
   (	"USER_PROCESS_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"EXPERIMENT_ID" NUMBER, 
	"PROCESS_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table USER_PROCESS_STEP
--------------------------------------------------------

  CREATE TABLE "USER_PROCESS_STEP" 
   (	"USER_PROCESS_STEP_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"USER_PROCESS_ID" NUMBER, 
	"PROCESS_STEP_ID" NUMBER, 
	"START_DATETIME" DATE, 
	"END_DATETIME" DATE, 
	"DEVICE_CAP_ID" NUMBER, 
	"LU_NOTIFICATION_TYPE_ID" NUMBER, 
	"NOTIFICATION_ID" NUMBER, 
	"LU_PROCESS_STEP_MODE_ID" NUMBER, 
	"DECK_ID" NUMBER, 
	"DEVICE_TIP_TYPE_ID" NUMBER, 
	"OIL_PLUG_CHEM_ID" NUMBER, 
	"OIL_PLUG_CHEM_CONC_ID" NUMBER, 
	"OIL_PLUG_CHEM_CONC_PH_ID" NUMBER, 
	"OIL_PLUG_VOLUME" NUMBER, 
	"SHAKE_PATTERN_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table USER_ROLE
--------------------------------------------------------

  CREATE TABLE "USER_ROLE" 
   (	"USER_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"ROLE_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_ADDITIVE
--------------------------------------------------------

  CREATE TABLE "VQT_ADDITIVE" 
   (	"WELL_COMPARTMENT_ID" NUMBER, 
	"ADDITIVE_NAME" VARCHAR2(80), 
	"ADDITIVE_CONC" NUMBER, 
	"ADDITIVE_CONC_UNITS" VARCHAR2(3), 
	"ADDITIVE_PH" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_CHEM
--------------------------------------------------------

  CREATE TABLE "VQT_CHEM" 
   (	"WELL_COMPARTMENT_ID" NUMBER, 
	"WELL_CHEMICAL" VARCHAR2(80), 
	"WELL_CONCENTRATION" NUMBER, 
	"WELL_CONC_UNITS" VARCHAR2(3), 
	"WELL_PH" NUMBER, 
	"WELL_CHEM_IS_CONSTITUENT" VARCHAR2(1), 
	"FINAL_WELL_CONCENTRATION" NUMBER, 
	"WELL_CHEM_CAT_ID" NUMBER, 
	"WELL_CHEM_CAT" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_EXPERIMENT
--------------------------------------------------------

  CREATE TABLE "VQT_EXPERIMENT" 
   (	"USER_ID" NUMBER, 
	"USERNAME" VARCHAR2(16), 
	"EXPERIMENT_ID" NUMBER, 
	"EXPERIMENT_NAME" VARCHAR2(80), 
	"BARCODE" VARCHAR2(256), 
	"EXPERIMENT_SUBMIT_DATE" DATE, 
	"TEMPERATURE" NUMBER, 
	"FIRST_IMAGE_EVENT_DATE" DATE, 
	"PLATE_ID" NUMBER, 
	"EXPERIMENT_TYPE" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_IECOMP
--------------------------------------------------------

  CREATE TABLE "VQT_IECOMP" 
   (	"EXPERIMENT_ID" NUMBER, 
	"IMAGE_EVENT_DATE" DATE, 
	"WELL_COMPARTMENT_ID" NUMBER, 
	"COMPARTMENT" VARCHAR2(20), 
	"WELL_NAME" VARCHAR2(4), 
	"COLUMN_NUM" NUMBER, 
	"ROW_NUM" NUMBER, 
	"DESCRIPTOR_ID" NUMBER, 
	"DESCRIPTOR_NAME" VARCHAR2(80), 
	"MODIFIER_ID" NUMBER, 
	"MODIFIER_NAME" VARCHAR2(80)
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_MOLECULE
--------------------------------------------------------

  CREATE TABLE "VQT_MOLECULE" 
   (	"WELL_COMPARTMENT_ID" NUMBER, 
	"MACROMOLECULE_NAME" VARCHAR2(80), 
	"MACROMOLECULE_CLASS_ID" NUMBER, 
	"MACROMOLECULE_CLASS" VARCHAR2(80), 
	"MACROMOLECULE_ID" NUMBER, 
	"MACROMOLECULE_VERSION" VARCHAR2(80), 
	"TARGET" VARCHAR2(80), 
	"SAMPLE" VARCHAR2(80), 
	"DROP_VOLUME" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table VQT_QUERY
--------------------------------------------------------

  CREATE TABLE "VQT_QUERY" 
   (	"VQT_QUERY_ID" NUMBER, 
	"USER_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"ALIAS" VARCHAR2(80), 
	"QUERYSTR" BLOB, 
	"CREATE_DATETIME" DATE
   ) ;
--------------------------------------------------------
--  DDL for Table WELL
--------------------------------------------------------

  CREATE TABLE "WELL" 
   (	"WELL_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"PLATE_TYPE_ID" NUMBER, 
	"WELL_NUM" NUMBER, 
	"WELL_NAME" VARCHAR2(4), 
	"ROW_NUM" NUMBER, 
	"COLUMN_NUM" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table WELL_COMPARTMENT
--------------------------------------------------------

  CREATE TABLE "WELL_COMPARTMENT" 
   (	"WELL_COMPARTMENT_ID" NUMBER, 
	"SITE_ID" NUMBER, 
	"PLATE_ID" NUMBER, 
	"WELL_ID" NUMBER, 
	"LU_COMPARTMENT_ID" NUMBER, 
	"CREATE_DATETIME" DATE DEFAULT SYSDATE, 
	"CREATE_USER_ID" NUMBER, 
	"UPDATE_DATETIME" DATE DEFAULT SYSDATE, 
	"UPDATE_USER_ID" NUMBER, 
	"OFFSET_LONG_AXIS" NUMBER, 
	"OFFSET_SHORT_AXIS" NUMBER, 
	"FOCUS_HEIGHT" NUMBER, 
	"FIELD_OF_VIEW" NUMBER, 
	"EXPOSURE_BASE" NUMBER, 
	"EXPOSURE_BASE_DARK" NUMBER, 
	"EXPOSURE_BASE_POLAR" NUMBER, 
	"DROP_VOLUME" NUMBER
   ) ;

