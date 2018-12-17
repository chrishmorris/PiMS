<#foreach table in cfg.getTableMappings()>
grant SELECT, UPDATE, INSERT, DELETE on  ${table.name} to pimsupdate;
grant SELECT on ${table.name} to pimsview;

</#foreach>

<#foreach table in cfg.getTableMappings()>
create or replace public synonym ${table.name} for ${table.name};
</#foreach>