<#include "JpaUtil.ftl"/>
<#-- ************************************************************ -->
<#-- Class template                                               -->
<#-- ************************************************************ -->
${pojo.getPackageDeclaration()}
/**
 * Generated ${date} by Hibernate Tools ${version}
 *
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 *
 * Copyright (c) 2007
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
<#assign classbody>
import org.pimslims.metamodel.WritableVersion;

<#include "JpaClassDeclaration.ftl"/> {

<#include "JpaConstants.ftl"/>

<#include "JpaPropertyDeclaration.ftl"/>

<#include "JpaConstructors.ftl"/>

<#include "JpaPropertyAccessors.ftl"/>

}
</#assign>

${pojo.generateImports()}
${classbody}

