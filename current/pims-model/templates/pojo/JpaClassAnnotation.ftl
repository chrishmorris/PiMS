<#-- ************************************************************ -->
<#-- Class annotation                                             -->
<#-- ************************************************************ -->
<#assign keyNames = "" />
<#assign parent = unresolved />
<#assign parentRoleName = unresolved />
<#foreach property in pojo.getAllPropertiesIterator()>
  <#if isUniqueKeyProperty(property, clazz)>
    <#if c2h.isManyToOne(property)>
      <#assign parent = getDefaultClassName(property) />
      <#assign parentRoleName = getNonHbName(property.name) />
    </#if>
    <#if isBaseProperty(property)>
      <#if keyNames = "">
        <#assign keyNames = '"' + getNonHbName(property.name)?upper_case + '"' />
      <#else>
        <#assign keyNames = keyNames + ', "' + getNonHbName(property.name)?upper_case + '"' />
      </#if>
    <#else>
      <#if keyNames = "">
        <#assign keyNames = '"' + getNonHbName(property.name)?upper_case + 'ID"' />
      <#else>
        <#assign keyNames = keyNames + ', "' + getNonHbName(property.name)?upper_case + 'ID"' />
      </#if>
    </#if>
  </#if>
</#foreach> 
<#assign subclass = "" />
<#foreach entity in cfg.classMappings> 
  <#if c2j.getPOJOClass(entity).getSuperClass()?exists>
    <#if pojo = c2j.getPOJOClass(entity).getSuperClass()>
      <#if subclass = "">
        <#assign subclass = entity.className + ".class" />
      <#else>
        <#assign subclass = subclass + ", " + entity.className + ".class" />
      </#if>
    </#if>
  </#if>
</#foreach>
@${pojo.importType("javax.persistence.Entity")}
@${pojo.importType("javax.persistence.Inheritance")}(strategy = ${pojo.importType("javax.persistence.InheritanceType")}.JOINED)
<#if pojo.getSuperClass()?exists>
    <#assign superclass = pojo.getSuperClass().shortName?upper_case + "ID" />
    <#if pojo.shortName = "Project">
@${pojo.importType("javax.persistence.PrimaryKeyJoinColumn")}(name="ABSTRACTSTORAGEID")
    <#else>
@${pojo.importType("javax.persistence.PrimaryKeyJoinColumn")}(name="${superclass}")
    </#if>
</#if>
@${pojo.importType("javax.persistence.Table")}(name="${clazz.table.name}", uniqueConstraints={@${pojo.importType("javax.persistence.UniqueConstraint")}(columnNames={${keyNames}})})
@org.pimslims.annotation.MetaClass(helpText = "${pojo.getClassJavaDoc(pojo.getDeclarationName(), 0)?replace(" * ", "")}", keyNames = {${keyNames}}, subclasses = {${subclass}}<#if parent != unresolved>, parent = ${parent}.class, parentRoleName= "${parentRoleName}"</#if>)
