<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

	<c:choose>
		<c:when test="${!empty parameter.parameterDefinition.possibleValues}">
			<c:set var="labelName" value="${parameter.parameterDefinition.name}" />
			<c:if test="${fn:startsWith(parameter.parameterDefinition.name, '__')}" >
				<c:set var="labelName" value="${parameter.parameterDefinition.label}" />
			</c:if>
		    <pimsForm:select name="${parameter._Hook}:value" alias="${labelName}" helpText="This is a tooltip" datatype="${parameter.paramType}">
                <c:forEach items="${parameter.parameterDefinition.possibleValues}" var="pdValue">
                    <pimsForm:option alias="${pdValue}" currentValue="${parameter.value}" optionValue="${pdValue}" />
				</c:forEach>
			</pimsForm:select>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${parameter.paramType=='Int' || parameter.paramType=='Integer' || parameter.paramType==longtype }">
					<pimsForm:text name="${parameter._Hook}:value" value="${parameter.value}" alias="${parameter.parameterDefinition.name}" validation="${validation}" datatype="${parameter.paramType}" />
					${parameter.parameterDefinition.displayUnit}                
				</c:when>
				<c:when test="${parameter.paramType==floattype || parameter.paramType==doubletype}">
					<c:choose>
						<c:when test="${fn:contains(parameter.parameterDefinition.name, 'Yield')}">
				  
				    		<pimsForm:nonFormFieldInfo label="${parameter.parameterDefinition.name}" datatype="${parameter.paramType}" >
				    			
								<!-- display for user -->
								<span id="${parameter._Hook}:value">
									<c:out value="${parameter.value}" /> 
								</span>
								<c:out value="${parameter.parameterDefinition.displayUnit}" />
								<!-- field to send to server -->
								<input type="hidden" name="${parameter._Hook}:value" value="${parameter.value}" />
								
							</pimsForm:nonFormFieldInfo>
							
				    	</c:when>
						<c:otherwise>
							<pimsForm:text name="${parameter._Hook}:value" value="${parameter.value}" alias="${parameter.parameterDefinition.name}" validation="${validation}" datatype="${parameter.paramType}" onchange="updatePool('${parameter.parameterDefinition.name}', this.value);"/>
				    		${parameter.parameterDefinition.displayUnit}
						</c:otherwise>
					</c:choose>
				</c:when>	
				<c:when test="${parameter.paramType==stringtype}">
				    <pimsForm:text name="${parameter._Hook}:value" value="${parameter.value}" alias="${parameter.parameterDefinition.name}" validation="${validation}" datatype="${parameter.paramType}" />
				</c:when>	 
				<c:when test="${parameter.paramType==booleantype}">
	                <pimsForm:radio name="${parameter._Hook}:value" value="true" isChecked="${'true'==parameter.value}" label="Yes" alias="${parameter.parameterDefinition.name}" datatype="${parameter.paramType}" />
	                <pimsForm:radio name="${parameter._Hook}:value" value="false" isChecked="${'true'!=parameter.value}" label="No" datatype="${parameter.paramType}" />
					<c:set var="blocksWritten" value="${1+blocksWritten}" /><%--Add extra, two blocks written--%>
				</c:when>
				<c:otherwise>Unknown type: ${parameter.paramType} ${parameter.value}<error /></c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<c:set var="blocksWritten" value="${1+blocksWritten}" />
