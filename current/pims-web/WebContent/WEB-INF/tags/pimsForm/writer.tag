<%@ taglib prefix="pimsForm"   tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@tag import="org.pimslims.presentation.mru.MRUController"%>
<%@tag import="org.pimslims.presentation.mru.MRURoleChoice"%>
<%@attribute name="hook" required="true"  %>
<%@attribute name="rolename" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="required" required="true"  %>

<c:set var="hook" scope="request" value="${hook}"/>
<c:set var="rolename" scope="request" value="${rolename}"/>
<c:set var="required" scope="request" value="${required}"/>

<%
try {
	MRURoleChoice mruChoice =MRUController.getMRURoleChoice((String)request.getAttribute("username"),(String)request.getAttribute("hook"),(String)request.getAttribute("rolename"),(String)request.getAttribute("required"));
	request.setAttribute("MRURoleChoice", mruChoice);
} catch (NullPointerException ex) {
	%> NPE ${rolename} ${username} ${hook} ${required}<%
}
%>

<pimsForm:formItem name="${hook}:${rolename}" alias="${alias}" validation="required:${required}">
<pimsForm:formItemLabel name="${hook}:${rolename}" alias="${alias}" helpText="${helpText}" />
<div class="formfield">
	<span class="selectnoedit">
	   <c:choose><c:when test="${null eq requestScope.MRURoleChoice.role_currentBean}">
	       none
	   </c:when><c:otherwise>
	       <pimsWidget:link bean="${requestScope.MRURoleChoice.role_currentBean}" />
	   </c:otherwise></c:choose>
	</span>
	<select  name="${hook}:${rolename}"
		id="${hook}:${rolename}" >
		<%--  TODO make this work with keyboard - try onkeyup --%>
		<c:if test="${mayUpdate}">
			<c:forEach items="${writers}" var="user">
				<c:choose>
					<c:when test="${user._Hook == requestScope.MRURoleChoice.role_currentHook}">
					<option value="${user._Hook}" onmouseup="onEdit();" selected="selected"><c:out value="${user.name}"/></option>
					</c:when><c:otherwise>
					<option value="${user._Hook}" onmouseup="onEdit();"><c:out value="${user.name}"/></option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:if>
	</select>
</div>
</pimsForm:formItem>

