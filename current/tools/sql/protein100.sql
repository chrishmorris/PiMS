update core_labbookentry as e set accessid=c.accessid from expe_experiment, core_labbookentry as c  where researchobjec
tiveid=c.dbid and labbookentryid=e.dbid and c.accessid!=e.accessid;

-- TODO put sample component in same lab notebook as molecule 