<%-- 
*
* Believed unused
*
--%>
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
</jsp:include>
<!-- CreateInputSample.jsp -->
<div style="width:50%">

<c:if test="${null!=protocol}">
    <%@include file="/JSP/protocol/ExistingSamplesAndParams.jsp" %>
</c:if>

<c:forEach items="${errorMessages}" var="entry">
    <c:choose><c:when test="${'missedErrorFields'==entry.key}">
        <c:forEach items="${entry.value}" var="message">
            <!-- Missing: <c:out value="${message}" /> -->	
        </c:forEach>
    </c:when><c:when test="${'notProvided'==entry.key}">
        <c:forEach items="${entry.value}" var="message">
            <!-- Role not yet populated: <c:out value="${message}" /> -->	
        </c:forEach>
    </c:when><c:otherwise>
        <p class="error"><br />${entry.key}: <c:out value="${entry.value}" /></p>	
    </c:otherwise></c:choose>
</c:forEach>

<pimsWidget:box initialState="fixed" title="New input">
<pimsForm:form method="post" action="/Create/org.pimslims.model.protocol.RefInputSample" mode="create">

	<input type="hidden" name="org.pimslims.model.protocol.RefInputSample:protocol" value="${param['protocol']}"/>
	<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.protocol.RefInputSample"/>
	<input type="hidden" name="_OWNER"  value="${protocol.access.name}" />

	<pimsForm:formBlock>
		<pimsForm:text alias="Name" name="org.pimslims.model.protocol.RefInputSample:name" validation="required:true" />
		
		<pimsForm:select alias="Category" name="org.pimslims.model.protocol.RefInputSample:sampleCategory">
		    <c:forEach var="sc" items="${sampleCategories}">
			<option value="${sc.hook}"><c:out value="${sc.name}" /></option>
			</c:forEach>
		</pimsForm:select>

		<pimsForm:measurement alias="Amount" name="org.pimslims.model.protocol.RefInputSample:amount" hook="org.pimslims.model.protocol.RefInputSample" />
		
	</pimsForm:formBlock>

	<pimsForm:formBlock>
		<div style="text-align:right">
			<span style="float:left">
			<a href="#" onclick="confirmAbandon();return false">Cancel</a>
			</span>
			<input type="submit" value="Save" onclick="dontWarn()"/>
		</div>
	</pimsForm:formBlock>

</pimsForm:form>
</pimsWidget:box>

</div>



<!-- javascript -->
<script type="text/javascript">
<!--
attachValidation("org.pimslims.model.protocol.RefInputSample:name", {required:true, custom:function(value,alias){ return protocol_checkNameUnique(value,alias)}, alias:"Name"} );
className = 'Reference input sample';
// -->
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
