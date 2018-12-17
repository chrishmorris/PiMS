<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%-- make validation --%>
<c:set var="validation" value=""/>
<c:if test="${parameter.parameterDefinition.isMandatory}">
    <c:set var="validation" value="required:true" />
</c:if>
<c:if test="${parameter.paramType=='Int' || parameter.paramType=='Integer'}">
   <%--comma--%><c:if test="${!empty validation}"><c:set var="validation" value="${validation}," /></c:if>
   <c:set var="validation" value="${validation} wholeNumber:true" />
</c:if>
<c:if test="${parameter.paramType==longtype || parameter.paramType==doubletype || parameter.paramType==floattype}">
   <%--comma--%><c:if test="${!empty validation}"><c:set var="validation" value="${validation}," /></c:if>
   <c:set var="validation" value="${validation} numeric:true" />
</c:if>
<c:if test="${!empty parameter.parameterDefinition.minValue}">
   <%--comma--%><c:if test="${!empty validation}"><c:set var="validation" value="${validation}," /></c:if>
   <c:set var="validation" value="${validation} minimum:${parameter.parameterDefinition.minValue}" />
</c:if>
<c:if test="${!empty parameter.parameterDefinition.maxValue}">
   <%--comma--%><c:if test="${!empty validation}"><c:set var="validation" value="${validation}," /></c:if>
   <c:set var="validation" value="${validation} maximum:${parameter.parameterDefinition.maxValue}" />
</c:if>
<%-- End of make validation --%>

<div class="formitem" >
	<div class="fieldname">${labelName}<c:if test="${parameter.parameterDefinition.isMandatory}"><span class="required">*</span></c:if></div>
	<div class="formfield" >
	
	<c:choose>
		<c:when test="${!empty parameter.parameterDefinition.possibleValues}">
			<span class="editonly">
		    <select name="${parameter._Hook}:value" id="${parameter._Hook}:value" alias="${labelName}">
                <c:forEach items="${parameter.parameterDefinition.possibleValues}" var="pdValue">
                    <pimsForm:option alias="${pdValue}" currentValue="${parameter.value}" optionValue="${pdValue}" />
				</c:forEach>
			</select>
			</span><span class="viewonly">${parameter.value}</span>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${parameter.paramType=='Int' || parameter.paramType=='Integer' || parameter.paramType==longtype }">
					<span class="editonly">
					<input type="text" name="${parameter._Hook}:value" id="${parameter._Hook}:value" value="<c:out value='${parameter.value}' />" />
					</span><span class="viewonly"><c:out value='${parameter.value}' /></span>
					<c:if test="${!empty validation}"><script type="text/javascript">attachValidation("${parameter._Hook}:value", { ${validation}, alias:"<c:out value="${utils:escapeJS(parameter.parameterDefinition.name)}"/>" })</script></c:if>
				</c:when>
				<c:when test="${parameter.paramType=='Float' || parameter.paramType==floattype || parameter.paramType==doubletype}">
					<span class="editonly">
					<input type="text" name="${parameter._Hook}:value" id="${parameter._Hook}:value" value="<c:out value='${parameter.value}' />" />
					</span><span class="viewonly"><c:out value='${parameter.value}' /></span>
					<c:if test="${!empty validation}"><script type="text/javascript">attachValidation("${parameter._Hook}:value", { ${validation}, alias:"<c:out value="${utils:escapeJS(parameter.parameterDefinition.name)}"/>" })</script></c:if>
				</c:when>	
				<c:when test="${parameter.paramType=='String' || parameter.paramType==stringtype}">
					<span class="editonly">
					<input type="text" name="${parameter._Hook}:value" id="${parameter._Hook}:value" value="<c:out value='${parameter.value}' />" />
					</span><span class="viewonly"><c:out value='${parameter.value}' /></span>
					<c:if test="${!empty validation}"><script type="text/javascript">attachValidation("${parameter._Hook}:value", { ${validation}, alias:"<c:out value="${utils:escapeJS(parameter.parameterDefinition.name)}"/>" })</script></c:if>
				</c:when>	 
				<c:when test="${parameter.paramType=='Boolean' || parameter.paramType==booleantype}">
					<span class="editonly">
				    <select name="${parameter._Hook}:value" id="${parameter._Hook}:value" alias="${labelName}">
               		    <pimsForm:option alias="Yes" currentValue="${parameter.value}" optionValue="true" />
		       		    <pimsForm:option alias="No"  currentValue="${parameter.value}" optionValue="false" />
					</select>
					</span><span class="viewonly">
						<c:choose>
							<c:when test="${'true'==parameter.value || 'yes'==parameter.value}">Yes</c:when>
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</span>
				</c:when>
				<c:otherwise><error message="Unknown type: ${parameter.paramType}" /><c:out value='${parameter.value}' /></c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>

	</div>
</div>	