   SELECT plate_1.name AS barcode,
                 plate.startdate AS createdate,
                 plate.enddate AS destroydate,
                 plate_2.details AS description,
                 construct.commonname AS constructname,
                 construct.labbookentryid AS constructid,
                 owneruser.name AS "owner",
                 "operator".name AS runby,
                 accessobject.name AS "group", 
                 CASE WHEN plate.enddate IS NULL THEN 'active' ELSE 'destroyed'
                 END AS status,
                 lastinpsection.completiontime AS lastimagedate,
                 instrument.name AS imager,
                 instrument.temperature,
                 plate.crystalnumber AS numberofcrystals,
                 holdertype.name AS platetype,
                 reholder.name AS screen,
                 proteinsample.labbookentryid AS proteinsampleid,
                 proteinsample.name AS proteinsamplename,
                 plate_2.accessid 
                   FROM hold_holder plate
                   JOIN hold_abstractholder plate_1 ON plate.abstractholderid = plate_1.labbookentryid
                   JOIN core_labbookentry plate_2 ON plate.abstractholderid = plate_2.dbid   
                   JOIN (SELECT sam_sample.holderid, min(sam_sample.abstractsampleid) AS sampleid FROM sam_sample GROUP BY sam_sample.holderid) 
                         hold_firstsample ON hold_firstsample.holderid = plate.abstractholderid
                   JOIN sam_sample sample ON hold_firstsample.sampleid = sample.abstractsampleid
                   JOIN expe_outputsample outputsamp2_ ON sample.abstractsampleid = outputsamp2_.sampleid
                   JOIN expe_experiment experiment ON outputsamp2_.experimentid = experiment.labbookentryid
                   JOIN core_labbookentry experiment1_ ON experiment.labbookentryid = experiment1_.dbid
                   LEFT JOIN expe_inputsample inputsample ON inputsample.experimentid = experiment.labbookentryid
                   LEFT JOIN sam_abstractsample proteinsample ON inputsample.sampleid = proteinsample.labbookentryid
                   LEFT JOIN sche_scheduledtask lastinpsection ON plate.lasttaskid = lastinpsection.labbookentryid
                   LEFT JOIN targ_researchobjective construct ON experiment.researchobjectiveid = construct.labbookentryid
                   LEFT JOIN acco_user owneruser ON experiment1_.creatorid = owneruser.systemclassid
                   LEFT JOIN acco_user "operator" ON experiment.operatorid = "operator".systemclassid
                   LEFT JOIN expe_instrument instrument ON lastinpsection.instrumentid = instrument.labbookentryid
                   LEFT JOIN ref_abstractholdertype holdertype ON plate_1.holdertypeid = holdertype.publicentryid
                   LEFT JOIN hold_refholderoffset refholdero15_ ON plate.abstractholderid = refholdero15_.holderid
                   LEFT JOIN hold_abstractholder reholder ON refholdero15_.refholderid = reholder.labbookentryid
                   LEFT JOIN core_accessobject accessobject ON accessobject.systemclassid = plate_2.accessid 
