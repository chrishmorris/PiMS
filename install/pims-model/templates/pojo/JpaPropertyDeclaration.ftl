    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
<#if !clazz.abstract?exists || !clazz.abstract>
    private static final long serialVersionUID = 1L;
</#if>
<#foreach property in pojo.getAllPropertiesIterator()>
  <#-- hibernate property name (may start with hb) -->
  <#assign hbPropertyName = getNewName(property.name) />
  <#-- property name (without hb) -->
  <#assign propertyName = getNonHbName(hbPropertyName) />
  <#-- hibernate method name (may start with hb) -->
  <#assign hbMethodName = pojo.getPropertyName(property) />
  <#-- method name (without hb) -->
  <#assign methodName = getNonHbName(hbMethodName) />
  <#-- inverse POJO (unresolved in case of base property & one way link) -->
  <#assign inversePojo = getInversePojo(property) />
  <#-- inverse property name (unresolved in case of base property & one way link) -->
  <#assign inverseProperty = getInversePropertyName(property, pojo) />
  <#-- inverse property original name (keep plural but remove hb) -->
  <#assign inversePropertyOri = getInversePropertyOriginalName(property, pojo) />
    /* ------------------------------------------------------------ */
  <#include "JpaPropertyAnnotation.ftl"/>
    ${pojo.getFieldModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} ${propertyName}<#if pojo.hasFieldInitializor(property, jdk5)&&pojo.getFieldInitialization(property, jdk5)!="null"> = ${pojo.getFieldInitialization(property, jdk5)}</#if>;

</#foreach>
