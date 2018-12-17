<#-- ************************************************************ -->
<#-- Class declaration                                            -->
<#-- ************************************************************ -->
<#include "JpaClassAnnotation.ftl" />
<#if clazz.abstract?exists && clazz.abstract>${pojo.getClassModifiers()} abstract ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} ${pojo.getExtendsDeclaration()} ${pojo.getImplementsDeclaration()}<#else>${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} ${pojo.getExtendsDeclaration()} ${pojo.getImplementsDeclaration()}</#if>