<#-- ============================================================ -->
<#-- Property Annotation                                          -->
<#-- ============================================================ -->
<#if pojo.hasIdentifierProperty()>
  <#if property.equals(clazz.identifierProperty)>
${pojo.generateAnnIdGenerator()}
<#-- if this is the id property (getter)-->
<#-- explicitly set the column name for this property -->
  </#if>
</#if>
<#-- cascade conversion between hbm and javax.persistence.CascadeType -->
<#if property.cascade?starts_with("merge")>
   <#assign cascade="MERGE" />
<#elseif property.cascade = "all">
   <#assign cascade="ALL" />
<#else>
   <#assign cascade="none" />
</#if>
<#-- fetch conversion between hbm and javax.persistence.FetchType -->
<#assign fetchmode = c2h.getFetchMode(property)>
<#if fetchmode != "default">
   <#assign fetch="LAZY" />
<#else>
   <#assign fetch="EAGER" />
</#if>
<#-- ************************************************************ -->
<#-- Multi-Attribute Annotation                                   -->
<#-- ************************************************************ -->
<#if c2h.getTag(property).equals("list")>
    // TODO: Annotation for collection of elements.
    @org.pimslims.annotation.Attribute(
        helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}"
    )
<#-- ************************************************************ -->
<#-- ManyToOne Annotation                                         -->
<#-- ************************************************************ -->
<#elseif c2h.isManyToOne(property)>
  <#foreach column in property.columnIterator>
    @${pojo.importType("javax.persistence.ManyToOne")}(<#if cascade != "none">cascade=${pojo.importType("javax.persistence.CascadeType")}.${cascade}, </#if>fetch=${pojo.importType("javax.persistence.FetchType")}.${fetch}, optional=${column.isNullable()?string})
    @${pojo.importType("javax.persistence.JoinColumn")}(name="${column.name}", <#if column.getSqlType()?exists>columnDefinition="${column.getSqlType()}", </#if>unique=${column.isUnique()?string}, nullable=${column.isNullable()?string})
  </#foreach>
    @org.pimslims.annotation.Role(helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}", low = <#if isRequiredProperty(property)>1<#else>0</#if>, high = 1, isChangeable = true, reverseRoleName = "${inversePropertyOri}")
<#-- ************************************************************ -->
<#-- ManyToMany Annotation                                        -->
<#-- ************************************************************ -->
<#elseif c2h.isManyToMany(property)>
    <#assign annotation = pojo.generateCollectionAnnotation(property, cfg) />
    <#if annotation?contains("JoinTable")>
    ${pojo.generateCollectionAnnotation(property, cfg)}
    <#else>
    @${pojo.importType("javax.persistence.ManyToMany")}(cascade=${pojo.importType("javax.persistence.CascadeType")}.MERGE, fetch=${pojo.importType("javax.persistence.FetchType")}.LAZY, mappedBy="${inversePropertyOri}")
    </#if>
    @org.pimslims.annotation.Role(helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}", low = <#if isRequiredProperty(property)>1<#else>0</#if>, high = -1, isChangeable = true, reverseRoleName = "${inversePropertyOri}")
<#-- ************************************************************ -->
<#-- OneToMany Annotation                                         -->
<#-- ************************************************************ -->
<#elseif c2h.isOneToMany(property)>
    @${pojo.importType("javax.persistence.OneToMany")}(fetch=${pojo.importType("javax.persistence.FetchType")}.LAZY, mappedBy="${inversePropertyOri}")
    <#--${pojo.generateCollectionAnnotation(property, cfg)}-->
    @org.pimslims.annotation.Role(helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}", low = <#if isRequiredProperty(property)>1<#else>0</#if>, high = -1, isChangeable = true, reverseRoleName = "${inversePropertyOri}")
<#-- ************************************************************ -->
<#-- OneToOne Annotation                                          -->
<#-- ************************************************************ -->
<#elseif c2h.getTag(property).equals("one-to-one")>
    @${pojo.importType("javax.persistence.OneToOne")} @${pojo.importType("javax.persistence.PrimaryKeyJoinColumn")}
    @org.pimslims.annotation.Role(helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}", low = <#if isRequiredProperty(property)>1<#else>0</#if>, high = 1, isChangeable = true, reverseRoleName = "${inversePropertyOri}")
<#-- ************************************************************ -->
<#-- Basic Annotation                                             -->
<#-- ************************************************************ -->
<#else>
  <#foreach column in property.columnIterator>
    @${pojo.importType("javax.persistence.Basic")}(optional=${column.isNullable()?string})
    @${pojo.importType("javax.persistence.Column")}(name="${column.name}", <#if column.getSqlType()?exists>columnDefinition="${column.getSqlType()}<#if column.getSqlType()?matches("FLOAT")>8</#if>", </#if>unique=${column.isUnique()?string}, nullable=${column.isNullable()?string}) 
  </#foreach>
    @org.pimslims.annotation.Attribute(helpText = "${pojo.getFieldJavaDoc(property, 0)?replace(" * ", "")}"<#if pojo.hasMetaAttribute(property, "contraints")>, constraints = { ${c2j.getMetaAsString(property, "contraints", "")} }</#if>)
</#if>
<#-- ============================================================ -->
<#-- JPA ANNOTATION                                               -->
<#-- ============================================================ -->
