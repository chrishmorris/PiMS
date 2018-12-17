    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
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
  <#-- ************************************************************ -->
  <#-- Property getters                                             -->
  <#-- ************************************************************ -->
    /** 
     * Property: ${getNonHbName(property.name)}
     */
  <#if propertyName != "serial">
    <#if propertyName = "dbId">
    ${pojo.getPropertyGetModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} get${methodName}() {
        return this.${propertyName};
    }
    
    <#else>
    ${pojo.getPropertyGetModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} get${methodName}() {
        return (${pojo.getJavaTypeName(property, jdk5)})get_prop(${getConstantName(property.name)});
    }

    </#if>
  </#if>
  <#-- ************************************************************ -->
  <#-- Property setters                                             -->
  <#-- ************************************************************ -->
  <#if propertyName = "dbId">
    @Override
  </#if>
  <#if propertyName != "serial">
    <#if propertyName = "dbId">
    ${pojo.getPropertySetModifiers(property)} void set${methodName}(${pojo.getJavaTypeName(property, jdk5)?replace("Set","java.util.Collection")} ${propertyName}) {
        this.${propertyName} = ${propertyName};
    }
    
    <#else>
    ${pojo.getPropertySetModifiers(property)} void set${methodName}(${pojo.getJavaTypeName(property, jdk5)?replace("Set","java.util.Collection")} ${propertyName}) throws org.pimslims.metamodel.ConstraintException {
	    set_prop(${getConstantName(property.name)}, ${propertyName});
    }
    
  <#-- ************************************************************ -->
  <#-- Property adder and remover                                   -->
  <#-- ************************************************************ -->
    <#if inverseProperty != unresolved>
      <#assign methodNameSingular = getSingular(methodName) />
      <#assign propertyNameSingular = getSingular(propertyName) />
  <#-- ************************************************************ -->
  <#-- specific methods for OneToMany                               -->
  <#-- ************************************************************ -->
        <#if c2h.isOneToMany(property)>
    public void add${methodNameSingular}(${inversePojo.shortName} ${propertyNameSingular}) throws org.pimslims.metamodel.ConstraintException {
       add(${getConstantName(property.name)}, ${propertyNameSingular});
    }

    public void remove${methodNameSingular}(${inversePojo.shortName} ${propertyNameSingular}) throws org.pimslims.metamodel.ConstraintException {
       remove(${getConstantName(property.name)}, ${propertyNameSingular});
    }

  <#-- ************************************************************ -->
  <#-- specific methods for ManyToMany                              -->
  <#-- ************************************************************ -->
      <#elseif c2h.isManyToMany(property)>
    public void add${methodNameSingular}(${getDefaultClassName(property)} ${propertyNameSingular}) throws org.pimslims.metamodel.ConstraintException {
       add(${getConstantName(property.name)}, ${propertyNameSingular});
    }
    
    public void remove${methodNameSingular}(${getDefaultClassName(property)} ${propertyNameSingular}) throws org.pimslims.metamodel.ConstraintException {
        remove(${getConstantName(property.name)}, ${propertyNameSingular});
    }

      </#if>
    </#if>
    </#if>
  </#if>
</#foreach>
