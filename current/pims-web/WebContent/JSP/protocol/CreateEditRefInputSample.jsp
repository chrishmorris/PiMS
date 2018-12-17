<%@ page contentType="text/html; charset=utf-8" language="java" 
  import="java.util.*,org.pimslims.presentation.*"  
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:useBean id="sampleCategories" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Details of an input' />
    <jsp:param name="isInModalWindow" value='yes' />
</jsp:include>
<!-- CreateEditRefInputSample.jsp -->


<c:choose><c:when test="${!empty bean}">
	<c:set var="hook">${bean.hook}</c:set>
    <c:set var="name">${bean.name}</c:set>
    <c:set var="categoryHook">${bean.sampleCategory.hook}</c:set>
    <c:set var="displayUnit">${bean.displayUnit}</c:set>
    <c:set var="displayValue">${bean.displayValue}</c:set>
    <c:set var="buttonLabel">Save changes</c:set>
    <c:set var="formAction">/Update/${bean.protocol.hook}</c:set>
    <c:set var="protocolHook">${bean.protocol.hook}</c:set>
</c:when><c:otherwise>
	<%-- Defaults for Create --%>
	<c:set var="name"></c:set>
	<c:set var="categoryHook"></c:set>
	<c:set var="displayUnit"></c:set>
	<c:set var="displayValue"></c:set>
	<c:set var="hook">org.pimslims.model.protocol.RefInputSample</c:set>
	<c:set var="buttonLabel">Create</c:set>
	<c:set var="formAction">/Create/org.pimslims.model.protocol.RefInputSample</c:set>
	<c:set var="protocolHook">${param['protocol']}</c:set>
</c:otherwise></c:choose>
 
<pimsForm:form method="post" action="${formAction}" mode="create">

	<input type="hidden" name="${hook}:protocol" value="${protocolHook}"/>
<c:if test="${empty bean}">
	<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.protocol.RefInputSample"/>
    <input type="hidden" name="_OWNER"  value="${protocol.access.name}" />
</c:if>

	<pimsForm:formBlock>
        <pimsForm:column1>
		<pimsForm:text alias="Name" value="${name}" name="${hook}:name" validation="required:true, custom:function(val,alias){ if(val==$('${hook}:name').oldValue) return ''; return protocol_checkNameUnique(val,alias)}" />
		<pimsForm:select alias="Category" name="${hook}:sampleCategory">
		    <c:forEach var="sc" items="${sampleCategories}">
<%--
			<option value="${sc.hook}"><c:out value="${sc.name}" /></option>
--%>
            <pimsForm:option alias="${sc.name}" currentValue="${categoryHook}" optionValue="${sc.hook}"></pimsForm:option>

			</c:forEach>
		</pimsForm:select>
        </pimsForm:column1>
        
        <pimsForm:column2>
        <%--
    		<pimsForm:measurement alias="Amount" name="${hook}:amount" hook="${hook}" />
        --%>
            <pimsForm:measurement alias="Amount" 
                 name="${hook}:amount" 
                 hook="${hook}" 
                 displayUnit="${displayUnit}"
                 displayAmount="${displayValue}" />

        </pimsForm:column2>
        		
	</pimsForm:formBlock>

	<pimsForm:formBlock>
		<div style="text-align:right">
			<input type="submit" value="${buttonLabel}" onclick="dontWarn()"/>
		</div>
	</pimsForm:formBlock>

</pimsForm:form>

<jsp:include page="ExistingSamplesAndParams.jsp" />


<!-- javascript -->
<script type="text/javascript">
$("${hook}:name").oldValue="${name}"; //mess of JSP and Prototype structure here! Assigns existing name onto name field as an attribute "oldValue"

<!--
className = 'Reference input sample';
// -->
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
