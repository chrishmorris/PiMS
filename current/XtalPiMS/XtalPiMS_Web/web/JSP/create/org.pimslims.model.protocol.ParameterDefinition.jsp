<%-- 
*
* Believed unused
*
--%>
<%@ page contentType="text/html; charset=utf-8" language="java" 
  import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*,org.pimslims.presentation.protocol.*,org.pimslims.model.protocol.*"  
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Details of a parameter' />
</jsp:include>

<!-- OLD -->

<c:set var="paramType" value="String" />
<c:set var="paramTypeForUser" value="text" />
<c:if test="${!empty param['paramType']}"><c:set var="paramType" value="${param['paramType']}" /></c:if>
<c:if test="${'Boolean'==paramType}"><c:set var="paramTypeForUser" value="yes/no" /></c:if>
<c:if test="${'Float'==paramType || 'Int'==paramType}"><c:set var="paramTypeForUser" value="number" /></c:if>

<div style="width:50%">

<c:if test="${null!=protocol}">
	<%@include file="/JSP/protocol/ExistingSamplesAndParams.jsp" %>
</c:if>

<c:forEach items="${errorMessages}" var="entry">
    <c:choose><c:when test="${'missedErrorFields'==entry.key}">
        <c:forEach items="${entry.value}" var="message">
            <!--  Missing: <c:out value="${message}" />	 -->
        </c:forEach>
    </c:when><c:otherwise>
        <p class="error"><br />${entry.key}: <c:out value="${entry.value}" /></p>	
    </c:otherwise></c:choose>
</c:forEach>

<c:set var="propIsMandatory"><%=ParameterDefinition.PROP_ISMANDATORY %></c:set>
<c:set var="propIsResult"><%=ParameterDefinition.PROP_ISRESULT %></c:set>
<c:set var="propIsGroupLevel"><%=ParameterDefinition.PROP_ISGROUPLEVEL %></c:set>

<pimsWidget:box title="New ${paramTypeForUser} parameter" initialState="fixed">

	<pimsForm:form method="post" mode="create"
	      action="${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.ParameterDefinition">
<%--
onsubmit="if(!protocol_validatePdef('org.pimslims.model.protocol.ParameterDefinition'))return false;return validateFormFields(this)"
--%>
		<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.protocol.ParameterDefinition"/>
		<input type="hidden" name="org.pimslims.model.protocol.RefParameterDefinition:protocol" value="${param['protocol']}"/>
		<input type="hidden" name="org.pimslims.model.protocol.ParameterDefinition:paramType" id="org.pimslims.model.protocol.ParameterDefinition:paramType" value="${paramType}" />
		<input type="hidden" name="_OWNER"  value="${protocol.access.name}" />

		<pimsForm:formBlock>
			<pimsForm:text alias="Name" name="org.pimslims.model.protocol.ParameterDefinition:name" validation="required:true" />
			<pimsForm:text alias="Description" name="org.pimslims.model.protocol.ParameterDefinition:label" />
		</pimsForm:formBlock>

<c:choose>
<c:when test="${'Boolean'==paramType}">
		<pimsForm:formBlock>
			<pimsForm:radio name="org.pimslims.model.protocol.ParameterDefinition:defaultValue" isChecked="true" value="yes" label="Yes" alias="Default value" />
			<pimsForm:radio name="org.pimslims.model.protocol.ParameterDefinition:defaultValue" isChecked="false" value="no" label="No" alias="" />
		</pimsForm:formBlock>
</c:when>

<c:when test="${'String'==paramType}">
		<pimsForm:formBlock>
			<pimsForm:text alias="Default value" name="org.pimslims.model.protocol.ParameterDefinition:defaultValue" />
			<pimsForm:textarea alias="Possible values" name="org.pimslims.model.protocol.ParameterDefinition:possibleValues" helpText="separated by semicolons (;)"></pimsForm:textarea>
		</pimsForm:formBlock>

</c:when><c:when test="${'Float'==paramType || 'Int'==paramType}">

		<pimsForm:formBlock>

			<script type="text/javascript">
			function setNumberParamType(){
				if($("_isInt").checked){ 
					$("org.pimslims.model.protocol.ParameterDefinition:paramType").value="Int"; 
				} else {
					$("org.pimslims.model.protocol.ParameterDefinition:paramType").value="Float"; 
				}
			}
			</script>
			<pimsForm:checkbox name="_isInt" label="Restrict to whole numbers only" onclick="setNumberParamType()" isChecked="true" />
			<pimsForm:text alias="Display unit" name="org.pimslims.model.protocol.ParameterDefinition:displayUnit" />
			<pimsForm:text alias="Default value" name="org.pimslims.model.protocol.ParameterDefinition:defaultValue" />
			<pimsForm:text alias="Mininum value" name="org.pimslims.model.protocol.ParameterDefinition:minValue" />
			<pimsForm:text alias="Maximum value" name="org.pimslims.model.protocol.ParameterDefinition:maxValue" />
			<pimsForm:textarea alias="Possible values" name="org.pimslims.model.protocol.ParameterDefinition:possibleValues" helpText="separated by semicolons (;)"></pimsForm:textarea>

		</pimsForm:formBlock>


</c:when>
</c:choose>
	

<div class="protocol_formitem" id="ismandatory">
	<label class="label" for="${propIsMandatory}">Must a value always be entered for this parameter?<span class="required">*</span></label>
<br/>	<input name="org.pimslims.model.protocol.ParameterDefinition:${propIsMandatory}" 
	id="org.pimslims.model.protocol.ParameterDefinition:${propIsMandatory}" 
	value="Yes" type="radio" checked="checked"  />Yes&nbsp;&nbsp;
	<input name="org.pimslims.model.protocol.ParameterDefinition:${propIsMandatory}" 
	id="org.pimslims.model.protocol.ParameterDefinition:${propIsMandatory}" 
	value="No" type="radio"   />No
</div>

<div class="protocol_formitem" id="isresult">
	<label class="label" for="${propIsResult}">Is this a result parameter?<span class="required">*</span></label>
<br/>	<input name="org.pimslims.model.protocol.ParameterDefinition:${propIsResult}" 
	id="org.pimslims.model.protocol.ParameterDefinition:${propIsResult}" onclick="enforceGroupParamCannotBeResult(this)"
	value="Yes" type="radio" />Yes&nbsp;&nbsp;
	<input name="org.pimslims.model.protocol.ParameterDefinition:${propIsResult}" 
	id="org.pimslims.model.protocol.ParameterDefinition:${propIsResult}" onclick="enforceGroupParamCannotBeResult(this)"
	value="No" type="radio" checked="checked" />No
</div>

<div class="protocol_formitem" id="isgroup">
    <label class="label" for="${propIsGroupLevel}">Is this a group level parameter?<span class="required">*</span></label>
<br/>   <input name="org.pimslims.model.protocol.ParameterDefinition:${propIsGroupLevel}" 
    id="org.pimslims.model.protocol.ParameterDefinition:${propIsGroupLevel}"  onclick="enforceGroupParamCannotBeResult(this)"
    value="Yes" type="radio" />Yes&nbsp;&nbsp;
    <input name="org.pimslims.model.protocol.ParameterDefinition:${propIsGroupLevel}" 
    id="org.pimslims.model.protocol.ParameterDefinition:${propIsGroupLevel}" onclick="enforceGroupParamCannotBeResult(this)"
    value="No" type="radio" checked="checked" />No
</div>

<script type="text/javascript">
function enforceGroupParamCannotBeResult(radio){
    if("No"==radio.value){
   	    return true;
    }
    if(radio.up("#isresult")){
        otherYes=$("isgroup").down("[value='Yes']").checked;
        if(!otherYes) {return true; }
    } else if(radio.up("#isgroup")){
        otherYes=$("isresult").down("[value='Yes']").checked;
        if(!otherYes) {return true; }
    }
    alert("A group-level parameter cannot be a result parameter.");
    radio.up(".protocol_formitem").down("[value='No']").checked="checked";
}
</script>

	<pimsForm:formBlock>
		<div style="text-align:right">
			<span style="float:left">
                <a href="#" onclick="confirmAbandon();return false">Cancel</a>
			</span>
			<input type="submit" value="Save" onclick="dontWarn()" />
		</div>
	</pimsForm:formBlock>


	</pimsForm:form>

</pimsWidget:box>

</div>

<script type="text/javascript">
<!--
attachValidation("org.pimslims.model.protocol.ParameterDefinition:name", {custom:function(value,alias){ return protocol_checkNameUnique(value,alias)}, alias:"Name"} );
// -->
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
