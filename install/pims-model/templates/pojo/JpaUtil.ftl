<#-- ************************************************************ -->
<#-- Constants                                                    -->
<#-- ************************************************************ -->
<#assign unresolved = "Unresolved"/>
<#assign none = "None"/>
<#-- ************************************************************ -->
<#-- Utility methods                                              -->
<#-- ************************************************************ -->
<#-- getSingular(plural)
returns the singular of a plural given property name otherwise
returns the same property name
-->
<#function getSingular plural>
  <#if plural?ends_with("ies")>
    <#return plural?substring(0, plural?length - 3) + "y" />
  <#elseif plural?ends_with("s")>
    <#return plural?substring(0, plural?length - 1) />
  <#else>
    <#return plural />
  </#if>
</#function>
<#-- ************************************************************ -->
<#-- getNewName(name)
returns the new name of the property
-->
<#function getNewName name>
  <#if name?matches("package")>
    <#return "_package" />
  <#else>
    <#return name />
  </#if>
</#function>
<#-- ************************************************************ -->
<#-- getNonHbName(name)
returns a name without hb in front
-->
<#function getNonHbName name>
  <#if name.startsWith("hb")>
    <#return name?substring(2)?uncap_first />
  <#elseif name.startsWith("Hb")>
    <#return name?substring(2)?cap_first />
  <#else>
    <#return name />
  </#if>
</#function>
<#-- ************************************************************ -->
<#-- getConstantName(name)
returns the constant name
-->
<#function getConstantName name>
  <#if name.startsWith("hb")>
    <#return "PROP_" + name?substring(2)?upper_case />
  <#elseif name.startsWith("Hb")>
    <#return "PROP_" + name?substring(2)?upper_case />
  <#else>
    <#return "PROP_" + name?upper_case />
  </#if>
</#function>
<#-- ************************************************************ -->
<#-- getInversePojo(property)
returns the inverse pojo class of a given property and
returns unresolved in case of single and multiple (list) attributes
-->
<#function getInversePojo property>
  <#if c2h.getTag(property).equals("list")>
    <#return unresolved />
  <#elseif c2h.isManyToOne(property)>
    <#return c2j.getPOJOClass(cfg.getClassMapping(property.value.referencedEntityName)) />
  <#elseif c2h.isOneToMany(property)>
    <#return c2j.getPOJOClass(property.value.element.associatedClass) />
  <#elseif c2h.isManyToMany(property)>
    <#return c2j.getPOJOClass(cfg.getClassMapping(property.value.element.type.name)) />
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#return c2j.getPOJOClass(cfg.getClassMapping(property.value.referencedEntityName)) />
  <#else>
    <#return unresolved />
  </#if>
</#function>
<#-- ************************************************************ -->
<#function getInversePropertyName property pojo>
  <#assign inversePropertyName = unresolved />
  <#if c2h.getTag(property).equals("list")>
    <#assign inversePropertyName = unresolved />
  <#elseif c2h.isManyToOne(property)>
    <#foreach column in property.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#if matchColumn.isUnique()>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.getTag(field).equals("one-to-one")>
          <#if getInversePojo(field) = pojo>
            <#assign inversePropertyName = getNonHbName(field.name) />
          </#if>
        </#if>
      </#foreach>
    <#else>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.isOneToManyCollection(field)>
          <#if c2j.getPOJOClass(field.value.element.associatedClass) = pojo>
            <#foreach key in field.value.key.columnIterator>
              <#if key = matchColumn>
                <#assign inversePropertyName = getSingular(getNonHbName(field.name)) />
              </#if>
            </#foreach>
          </#if>
        </#if>
      </#foreach>
    </#if>
  <#elseif c2h.isOneToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key = matchColumn>
              <#assign inversePropertyName = getNonHbName(field.name) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.isManyToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToMany(field)>
        <#if getInversePojo(field) = pojo>
          <#foreach column in field.value.element.columnIterator>
            <#if column = matchColumn>
              <#assign inversePropertyName = getSingular(getNonHbName(field.name)) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.getTag(field).equals("one-to-one")>
        <#if getInversePojo(field) = pojo>
          <#assign inversePropertyName = getNonHbName(field.name) />
        </#if>
      <#elseif c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key.isUnique()>
              <#assign inversePropertyName = getNonHbName(field.name) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  </#if>
  <#return inversePropertyName>
</#function>
<#-- ************************************************************ -->
<#function getInversePropertyOriginalName property pojo>
  <#assign inversePropertyName = unresolved />
  <#if c2h.getTag(property).equals("list")>
    <#assign inversePropertyName = unresolved />
  <#elseif c2h.isManyToOne(property)>
    <#foreach column in property.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#if matchColumn.isUnique()>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.getTag(field).equals("one-to-one")>
          <#if getInversePojo(field) = pojo>
            <#assign inversePropertyName = getNonHbName(field.name) />
          </#if>
        </#if>
      </#foreach>
    <#else>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.isOneToManyCollection(field)>
          <#if c2j.getPOJOClass(field.value.element.associatedClass) = pojo>
            <#foreach key in field.value.key.columnIterator>
              <#if key = matchColumn>
                <#assign inversePropertyName = getNonHbName(field.name) />
              </#if>
            </#foreach>
          </#if>
        </#if>
      </#foreach>
    </#if>
  <#elseif c2h.isOneToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key = matchColumn>
              <#assign inversePropertyName = getNonHbName(field.name) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.isManyToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToMany(field)>
        <#if getInversePojo(field) = pojo>
          <#foreach column in field.value.element.columnIterator>
            <#if column = matchColumn>
              <#assign inversePropertyName = getNonHbName(field.name) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.getTag(field).equals("one-to-one")>
        <#if getInversePojo(field) = pojo>
          <#assign inversePropertyName = getNonHbName(field.name) />
        </#if>
      <#elseif c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key.isUnique()>
              <#assign inversePropertyName = getNonHbName(field.name) />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  </#if>
  <#return inversePropertyName>
</#function>
<#-- ************************************************************ -->
<#function getInverseProperty property pojo>
  <#assign inverseProperty = unresolved />
  <#if c2h.getTag(property).equals("list")>
    <#assign inverseProperty = unresolved />
  <#elseif c2h.isManyToOne(property)>
    <#foreach column in property.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#if matchColumn.isUnique()>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.getTag(field).equals("one-to-one")>
          <#if getInversePojo(field) = pojo>
            <#assign inverseProperty = field />
          </#if>
        </#if>
      </#foreach>
    <#else>
      <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
        <#if c2h.isOneToManyCollection(field)>
          <#if c2j.getPOJOClass(field.value.element.associatedClass) = pojo>
            <#foreach key in field.value.key.columnIterator>
              <#if key = matchColumn>
                <#assign inverseProperty = field />
              </#if>
            </#foreach>
          </#if>
        </#if>
      </#foreach>
    </#if>
  <#elseif c2h.isOneToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key = matchColumn>
              <#assign inverseProperty = field />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.isManyToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#assign matchColumn = column />
    </#foreach>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToMany(field)>
        <#if getInversePojo(field) = pojo>
          <#foreach column in field.value.element.columnIterator>
            <#if column = matchColumn>
              <#assign inverseProperty = field />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.getTag(field).equals("one-to-one")>
        <#if getInversePojo(field) = pojo>
          <#assign inverseProperty = field />
        </#if>
      <#elseif c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#foreach key in field.columnIterator>
            <#if key.isUnique()>
              <#assign inverseProperty = field />
            </#if>
          </#foreach>
        </#if>
      </#if>
    </#foreach>
  </#if>
  <#return inverseProperty>
</#function>
<#-- ************************************************************ -->
<#function getInverseDefaultClassName property pojo>
  <#assign defaultClassName = unresolved />
  <#if c2h.isManyToOne(property)>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isOneToManyCollection(field)>
        <#if c2j.getPOJOClass(field.value.element.associatedClass) = pojo>
          <#assign defaultClassName = field.value.element.associatedClass.className />
        </#if>
      <#elseif c2h.getTag(field).equals("one-to-one")>
        <#if getInversePojo(field) = pojo>
          <#assign defaultClassName = field.value.referencedEntityName />
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.isOneToMany(property)>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToOne(field)>
        <#if c2j.getPOJOClass(cfg.getClassMapping(field.value.referencedEntityName)) = pojo>
          <#assign defaultClassName = field.value.referencedEntityName />
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.isManyToMany(property)>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.isManyToMany(field)>
        <#if getInversePojo(field) = pojo>
          <#assign defaultClassName = field.value.element.type.name />
        </#if>
      </#if>
    </#foreach>
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#foreach field in getInversePojo(property).getAllPropertiesIterator()>
      <#if c2h.getTag(field).equals("one-to-one")>
        <#if getInversePojo(field) = pojo>
          <#assign defaultClassName = field.value.referencedEntityName />
        </#if>
      <#elseif c2h.isManyToOne(field)>
        <#if getInversePojo(field) = pojo>
          <#assign defaultClassName = field.value.referencedEntityName />
        </#if>
      </#if>
    </#foreach>
  </#if>
  <#return defaultClassName>
</#function>
<#-- ************************************************************ -->
<#function getDefaultClassName property>
  <#assign defaultClassName = unresolved />
  <#if c2h.isManyToOne(property)>
    <#assign defaultClassName = property.value.referencedEntityName />
  <#elseif c2h.isOneToMany(property)>
    <#assign defaultClassName = property.value.element.associatedClass.className />
  <#elseif c2h.isManyToMany(property)>
    <#assign defaultClassName = property.value.element.type.name />
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#assign defaultClassName = property.value.referencedEntityName />
  </#if>
  <#return defaultClassName>
</#function>
<#-- ************************************************************ -->
<#function isRequiredProperty property>
  <#assign isRequired = false />
  <#if c2h.isManyToOne(property)>
    <#foreach column in property.columnIterator>
      <#if !column.isNullable()>
        <#assign isRequired = true />
      </#if>
    </#foreach>
  <#elseif c2h.isOneToMany(property)>
    <#foreach column in property.value.key.columnIterator>
      <#if !column.isNullable()>
        <#assign isRequired = true />
      </#if>
    </#foreach>
  <#elseif c2h.isManyToMany(property)>
    <#-- TODO: HACK special case for mandatory many-to-many to HolderCategory -->
    <#if getDefaultClassName(property) = "org.pimslims.pojo.holder.HolderCategory">
      <#assign isRequired = true />
    <#else>
      <#foreach column in property.value.key.columnIterator>
        <#if !column.isNullable()>
          <#assign isRequired = false />
        </#if>
      </#foreach>
    </#if>
  <#elseif c2h.getTag(property).equals("one-to-one")>
    <#-- TODO: HACK special case for mandatory one-to-one to ContentStorage -->
    <#if getDefaultClassName(property) = "org.pimslims.pojo.implementation.ContentStorage">
      <#assign isRequired = true />
    </#if>
  <#else>
    <#foreach column in property.columnIterator>
      <#if !column.isNullable()>
        <#assign isRequired = true />
      </#if>
    </#foreach>
  </#if>
  <#return isRequired />
</#function>
<#-- ************************************************************ -->
<#function isUniqueKeyProperty property clazz>
  <#assign isUniqueKey = false />
  <#foreach key in clazz.table.getUniqueKeyIterator()>
    <#foreach keycolumn in key.getColumns()>
      <#foreach column in property.columnIterator>
        <#if column = keycolumn>
          <#assign isUniqueKey = true />
        </#if>
      </#foreach>
    </#foreach>
  </#foreach>
  <#return isUniqueKey />
</#function>
<#-- ************************************************************ -->
<#function getUniqueKeyNumber clazz>
  <#assign uniqueKeyNumber = 0 />
  <#if clazz.table.getUniqueKeyIterator().hasNext() >
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isUniqueKeyProperty(property, clazz) && !isMemopsProject(property) && !isSerial(property)>
        <#assign uniqueKeyNumber = uniqueKeyNumber + 1 />
      </#if>
    </#foreach>
  </#if>
  <#return uniqueKeyNumber />
</#function>
<#-- ************************************************************ -->
<#function getParent pojo>
  <#assign parent = unresolved />
  <#foreach property in pojo.getAllPropertiesIterator()>
    <#if isUniqueKeyProperty(property, clazz) && c2h.isManyToOne(property)>
      <#assign parent = property />
    </#if>
  </#foreach>
  <#return parent />
</#function>
<#-- ************************************************************ -->
<#function isParent property pojo>
  <#assign result = false>
  <#if property = getParent(pojo)>
    <#assign result = true>
  </#if>
  <#return result />
</#function>
<#-- ************************************************************ -->
<#function isBaseProperty property>
  <#if c2h.isManyToOne(property)>
    <#return false />
  <#elseif c2h.isOneToMany(property)>
    <#return false />
  <#elseif c2h.isManyToMany(property)>
    <#return false />
  <#else>
    <#return true />
  </#if>
</#function>
<#-- ************************************************************ -->
<#function getMemopsProjectParentPropertyName pojo>
  <#assign result = none />
  <#foreach property in pojo.getAllPropertiesIterator()>
    <#if c2h.isManyToOne(property)>
      <#assign inversePojo = getInversePojo(property)/>
      <#if inversePojo.packageName?contains("mplementation") && inversePojo.shortName = "Project">
        <#assign result = property.name />
      </#if>
    </#if>
  </#foreach>
  <#return result />
</#function>
<#-- ************************************************************ -->
<#function isMemopsProject property>
  <#assign result = false />
  <#if c2h.isManyToOne(property)>
    <#assign inversePojo = getInversePojo(property)/>
    <#if inversePojo.packageName?contains("mplementation") && inversePojo.shortName = "Project">
      <#assign result = true />
    </#if>
  </#if>
  <#return result />
</#function>
<#-- ************************************************************ -->
<#function isDbId property>
  <#if property.name = "dbId">
    <#return true />
  <#else>
    <#return false />
  </#if>
</#function>
<#-- ************************************************************ -->
<#function isSerial property>
  <#if property.name = "serial" || property.name = "hbSerial">
    <#return true />
  <#else>
    <#return false />
  </#if>
</#function>
<#-- ************************************************************ -->
<#function isRequiredPropertyForConstructor property pojo>
  <#if isRequiredProperty(property)
       && !isMemopsProject(property)
       && !isDbId(property)
       && !isSerial(property)>
    <#if pojo.shortName = "AbstractStorage" && (property.name = "format" || property.name = "isModifiable")>
      <#return false>
    <#elseif pojo.shortName = "Url" && property.name = "protocol">
      <#return false>
    <#elseif pojo.hasFieldInitializor(property, jdk5)>
      <#if isBaseProperty(property)>
        <#if pojo.getFieldInitialization(property, jdk5) != "null">
          <#if pojo.packageName?contains("mplementation")>
            <#return true>
          <#elseif pojo.shortName = "Permission">
            <#return true>
          <#elseif property.name = "hbNamingSystem">
            <#return true>
          <#else>
            <#return false>
          </#if>
        <#else>
          <#return true>
        </#if>
      <#else>
        <#return true>
      </#if>
    <#else>
      <#return true>
    </#if>
  <#elseif pojo.shortName = "AbstractStorage" && getNonHbName(property.name) = "url">
    <#return true>
  <#else>
    <#return false>
  </#if>
</#function>
<#-- ************************************************************ -->
<#function getConstructorRequiredArgs pojo>
  <#assign arguments = "" />
  <#if pojo.getSuperClass()?exists>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo) && !isParent(property, pojo)>
        <#assign arguments = arguments + ", " + c2j.getJavaTypeName(property, jdk5) + " " + getNonHbName(property.name) />
      </#if>
    </#foreach>
    <#assign arguments = arguments + getConstructorRequiredArgs(pojo.getSuperClass()) />
  <#else>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo) && !isParent(property, pojo)>
        <#assign arguments = arguments + ", " + c2j.getJavaTypeName(property, jdk5) + " " + getNonHbName(property.name) />
      </#if>
    </#foreach>
  </#if>
  <#return arguments />
</#function>
<#-- ************************************************************ -->
<#function getConstructorParentArg pojo>
  <#assign parentArg = "" />
  <#if pojo.getSuperClass()?exists>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo) && isParent(property, pojo)>
        <#assign parentArg = parentArg + ", " + c2j.getJavaTypeName(property, jdk5) + " " + getNonHbName(property.name) />
      </#if>
    </#foreach>
    <#assign parentArg = parentArg + getConstructorParentArg(pojo.getSuperClass()) />
  <#else>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo) && isParent(property, pojo)>
        <#assign parentArg = parentArg + ", " + c2j.getJavaTypeName(property, jdk5) + " " + getNonHbName(property.name) />
      </#if>
    </#foreach>
  </#if>
  <#return parentArg />
</#function>
<#-- ************************************************************ -->
<#function getConstructorRequiredArgsBody pojo>
  <#assign body = "" />
  <#if pojo.getSuperClass()?exists>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo)>
        <#assign body = body + "attributes.put(" + getConstantName(property.name) + ", " + getNonHbName(property.name) + ");\n        " />
      </#if>
    </#foreach>
    <#assign body = body + getConstructorRequiredArgsBody(pojo.getSuperClass()) />
  <#else>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if isRequiredPropertyForConstructor(property, pojo)>
        <#assign body = body + "attributes.put(" + getConstantName(property.name) + ", " + getNonHbName(property.name) + ");\n        " />
      </#if>
    </#foreach>
  </#if>
  <#return body />
</#function>
<#-- ************************************************************ -->
<#function getAllInheritedProperties pojo>
  <#assign propertyList = "" />
  <#if pojo.getSuperClass()?exists>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if !isMemopsProject(property) && !isDbId(property)>
        <#assign propertyList = propertyList + ", " + property.name />
      </#if>
    </#foreach>
    <#assign propertyList = propertyList + getAllInheritedProperties(pojo.getSuperClass()) />
  <#else>
    <#foreach property in pojo.getAllPropertiesIterator()>
      <#if !isMemopsProject(property) && !isDbId(property)>
        <#assign propertyList = propertyList + ", " + property.name />
      </#if>
    </#foreach>
  </#if>
  <#return propertyList />
</#function>