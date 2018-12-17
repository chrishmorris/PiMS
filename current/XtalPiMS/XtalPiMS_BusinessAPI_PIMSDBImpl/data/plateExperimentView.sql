CREATE OR REPLACE VIEW trialplateview AS 
 SELECT plate_1.name AS barcode, plate.startdate AS createdate, plate.enddate AS destroydate, plate_2.details AS description, construct.commonname AS constructname, owneruser.name AS "owner", "operator".name AS runby, accessobject.name AS "group", 
        CASE
            WHEN plate.enddate IS NULL THEN 'active'::text
            ELSE 'destroyed'::text
        END AS status, scheduledt10_.completiontime AS lastimagedate, instrument.name AS imager, instrument.temperature, holdertype.name AS platetype, reholder.name AS sreen, plate_2.accessid, COALESCE(hold_crystalnumber.numberofcrystals, 0::bigint)::integer AS numberofcrystals
   FROM hold_holder plate
   JOIN hold_abstractholder plate_1 ON plate.abstractholderid = plate_1.labbookentryid
   JOIN core_labbookentry plate_2 ON plate.abstractholderid = plate_2.dbid
   JOIN hold_lastinspection lastinpsection ON plate.abstractholderid = lastinpsection.holderid
   JOIN sche_scheduledtask scheduledt10_ ON lastinpsection.labbookentryid = scheduledt10_.labbookentryid
   JOIN hold_firstsample ON hold_firstsample.holderid = plate.abstractholderid
   JOIN sam_sample sample ON hold_firstsample.sampleid = sample.abstractsampleid
   LEFT JOIN expe_outputsample outputsamp2_ ON sample.abstractsampleid = outputsamp2_.sampleid
   LEFT JOIN expe_experiment experiment ON outputsamp2_.experimentid = experiment.labbookentryid
   LEFT JOIN core_labbookentry experiment1_ ON experiment.labbookentryid = experiment1_.dbid
   LEFT JOIN targ_researchobjective construct ON experiment.researchobjectiveid = construct.labbookentryid
   LEFT JOIN acco_user owneruser ON experiment1_.creatorid = owneruser.systemclassid
   LEFT JOIN acco_user "operator" ON experiment.operatorid = "operator".systemclassid
   LEFT JOIN expe_instrument instrument ON scheduledt10_.instrumentid = instrument.labbookentryid
   JOIN hold_holdca2abstholders holdercate12_ ON plate.abstractholderid = holdercate12_.abstholderid
   JOIN ref_holdercategory holdercate13_ ON holdercate12_.holdercategoryid = holdercate13_.publicentryid
   LEFT JOIN ref_abstractholdertype holdertype ON plate_1.holdertypeid = holdertype.publicentryid
   LEFT JOIN hold_refholderoffset refholdero15_ ON plate.abstractholderid = refholdero15_.holderid
   LEFT JOIN hold_abstractholder reholder ON refholdero15_.refholderid = reholder.labbookentryid
   LEFT JOIN core_accessobject accessobject ON accessobject.systemclassid = plate_2.accessid
   LEFT JOIN hold_crystalnumber ON hold_crystalnumber.holderid = plate.abstractholderid;

ALTER TABLE trialplateview OWNER TO postgres;
GRANT ALL ON TABLE trialplateview TO postgres;
GRANT ALL ON TABLE trialplateview TO pimsadmin;
GRANT SELECT ON TABLE trialplateview TO pimsupdate;
GRANT SELECT ON TABLE trialplateview TO pimsview;