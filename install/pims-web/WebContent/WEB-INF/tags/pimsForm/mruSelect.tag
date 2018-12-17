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
<%@attribute name="currentBean" required="false" type="org.pimslims.presentation.ModelObjectShortBean"   %>
<%@attribute name="required" required="true"  %>
<%@attribute name="searchMore" required="false"  %><%-- snippet of js to execute, default changes location to EditRole --%>
<%@attribute name="action" required="false"  %>
<c:set var="hook" scope="request" value="${hook}"/>
<c:set var="rolename" scope="request" value="${rolename}"/>
<c:set var="required" scope="request" value="${required}"/>
<c:if test="${empty action}"><c:set var="action" value="EditRole" /></c:if>
<%-- TODO use doMruSelect tag instead of this --%>

<%
try {
	MRURoleChoice mruChoice =MRUController.getMRURoleChoice((String)request.getAttribute("username"),(String)request.getAttribute("hook"),(String)request.getAttribute("rolename"),(String)request.getAttribute("required"));
	request.setAttribute("MRURoleChoice", mruChoice);
} catch (NullPointerException ex) {
	%> NPE ${rolename} ${username} ${hook} ${required}<%
}
%>
<c:choose><c:when test="${empty currentBean }">
  <c:set var="currentBean" value="${requestScope.MRURoleChoice.role_currentBean}" />
  <c:set var="currentHook" value="${requestScope.MRURoleChoice.role_currentHook}" />
  <c:set var="currentObjectName" value="${requestScope.MRURoleChoice.role_currentObjectName}" />
</c:when><c:otherwise>
  <c:set var="currentHook" value="${currentBean.hook}" />
  <c:set var="currentObjectName" value="${currentBean.name}" />
</c:otherwise></c:choose>
<%-- 
<c:set var="location">${pageContext.request.contextPath}/${action}/${hook}/${rolename}</c:set>
<c:if test="${! empty param.searchMoreUrl}">
    <c:set var="location" value="${param.searchMoreUrl}"/>
</c:if> --%>

<c:if test="${empty searchMore}">	
    <c:set var="searchMore">startMRURoleAdd(this,'${pageContext.request["contextPath"]}/EditRole/${hook}/${rolename}?isInModalWindow=true','Choose ${alias}');</c:set>
</c:if>

<pimsForm:formItem name="${hook}:${rolename}" alias="${alias}" validation="required:${required}">
<pimsForm:formItemLabel name="${hook}:${rolename}" alias="${alias}" helpText="${helpText}" />
<div class="formfield">
	<span class="selectnoedit">
	   <c:choose><c:when test="${null eq currentBean}">
	       none
	   </c:when><c:otherwise>
	       <pimsWidget:link bean="${currentBean}" />
	   </c:otherwise></c:choose>
	</span> 
	<select  name="${hook}:${rolename}"
		id="${hook}:${rolename}"
		onchange="if ('[NONE]'==this.value) { ${searchMore} }" >
        <option value="[none]">(none)</option>
		<%--  TODO make this work with keyboard - try onkeyup --%>
		<c:if test="${! empty currentHook}">
 			<option value="${currentHook}" onmouseup="onEdit();"
				selected="selected"><c:out value="${currentObjectName}" /></option>
		</c:if>
		<c:if test="${mayUpdate}">
			<c:if test="${! empty requestScope.MRURoleChoice.possibleMRUItems}">
			<optgroup label="Recently Used">
				<c:forEach items="${ requestScope.MRURoleChoice.possibleMRUItems }" var="entry">
					<option value="${entry.key}" onmouseup="onEdit();"><c:out value="${entry.value}"/></option>
				</c:forEach>
			</optgroup>
			</c:if>
			<optgroup label="Others">
				<option value="[NONE]">Search More...</option>
			</optgroup>
		</c:if>
	</select>
</div>
</pimsForm:formItem>

