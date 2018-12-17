-- drop the constraints
<#foreach table in cfg.getTableMappings()>
  <#list table.getIndexIterator() as index>
drop index ${index.name};
  </#list>
  <#list table.getForeignKeyIterator() as fk>
alter table ${table.name} drop constraint ${fk.name};
  </#list>
</#foreach>

-- drop the tables
<#foreach table in cfg.getTableMappings()>
drop table ${table.name};
drop public synonym ${table.name};
</#foreach>

drop table REVISIONNUMBER;
drop sequence TEST_TARGET;
drop sequence GENERIC_TARGET;
drop sequence hibernate_sequence;
