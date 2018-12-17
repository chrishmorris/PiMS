    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
<#foreach property in pojo.getAllPropertiesIterator()>
<#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
    public static final String ${getConstantName(property.name)} = "${getNonHbName(property.name)}";
</#if>

</#foreach>
