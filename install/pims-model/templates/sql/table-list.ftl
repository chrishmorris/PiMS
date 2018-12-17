<#foreach table in cfg.getTableMappings()>
grant ALL PRIVILEGES on table ${table.name} to pimsadmin;
grant SELECT, UPDATE, INSERT, DELETE on table ${table.name} to pimsupdate;
grant SELECT on table ${table.name} to pimsview;

</#foreach>
