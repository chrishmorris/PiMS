-- temporarily make all boolean columns boolean ones
-- so can load Oracle dump
alter table ACCO_PERMISSION alter column PERMISSION TYPE boolean using cast (PERMISSION as boolean)      ;
alter table EXPE_EXPERIMENT alter column ISLOCKED TYPE boolean using cast (ISLOCKED as boolean)           ;
alter table MOLE_EXTENSION alter column ISFORUSE TYPE boolean using cast (ISFORUSE as boolean)    ;
alter table MOLE_MOLECULE alter column ISVOLATILE TYPE boolean using cast (ISVOLATILE as boolean)      ;
alter table MOLE_PRIMER alter column ISUNIVERSAL TYPE boolean using cast (ISUNIVERSAL as boolean)         ;

alter table PROT_PARAMETERDEFINITION alter column ISGROUPLEVEL TYPE boolean using cast (ISGROUPLEVEL as boolean) ;
alter table PROT_PARAMETERDEFINITION alter column ISMANDATORY TYPE boolean using cast (ISMANDATORY as boolean) ;
alter table PROT_PARAMETERDEFINITION alter column ISRESULT TYPE boolean using cast (ISRESULT as boolean);

alter table PROT_PROTOCOL alter column ISFORUSE TYPE boolean using cast (ISFORUSE as boolean)   ;
alter table REF_HOLDERTYPE alter column HOLDHOLDERFLAG TYPE boolean using cast (HOLDHOLDERFLAG as boolean)        ;

alter table SAM_ABSTRACTSAMPLE alter column ISHAZARD TYPE boolean using cast (ISHAZARD as boolean)  ;
alter table SAM_ABSTRACTSAMPLE alter column ISACTIVE TYPE boolean using cast (ISACTIVE as boolean)  ;

alter table SAM_REFSAMPLE alter column isSaltCrystal TYPE boolean using cast (isSaltCrystal as boolean)  ;
alter table SAM_SAMPLE alter column CURRENTAMOUNTFLAG TYPE boolean using cast (CURRENTAMOUNTFLAG as boolean)         ;
alter table TRAG_RESEARCHOBJECTIVEELEMENT alter column ALWAYSINCLUDED TYPE boolean using cast (ALWAYSINCLUDED as boolean)           ;
