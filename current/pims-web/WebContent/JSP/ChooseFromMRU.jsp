<%--
Choose list from MRU
param['mru_object_hook'] is hook of modelObject choose for
param['mru_roleName']  is the role will be choose
param['mru_isRequired']  is the role required "true" or "false"

Author: Bill Lin
Date: April 2007
--%>
<!-- ChooseFromMRU.jsp -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@page import="org.pimslims.presentation.mru.MRUController"%>
<%@page import="org.pimslims.presentation.mru.MRURoleChoice"%>
<%@page import="org.pimslims.servlet.PIMSServlet"%>
obsolete
<%  
	String UserName=PIMSServlet.getUsername(request);
	String mo_hook=request.getParameter("mru_object_hook");
	String roleName=request.getParameter("mru_roleName");
	String isRequired=request.getParameter("mru_isRequired");
	
	MRURoleChoice mruChoice =MRUController.getMRURoleChoice(UserName,mo_hook,roleName,isRequired);
	request.setAttribute("MRURoleChoice", mruChoice);
	
%>

	<c:set var="location">
		../EditRole/${requestScope.MRURoleChoice.modelObject_hook}/${requestScope.MRURoleChoice.role_name}
	</c:set>
	<c:if test="${! empty param.searchMoreUrl}"> 
		<c:set var="location" value="${param.searchMoreUrl}"/>
	</c:if>
	<select onchange="onEdit();" name="${requestScope.MRURoleChoice.modelObject_hook}:${requestScope.MRURoleChoice.role_name}"
			id="${requestScope.MRURoleChoice.modelObject_hook}:${requestScope.MRURoleChoice.role_name}"
			onclick="if ('[NONE]'==this.value) {
			 this.selectedIndex=0; 
			top.document.location='${location}';
			}" ><!--  TODO make this work with keyboard - try onkeyup -->
			<c:if test="${! empty requestScope.MRURoleChoice.role_currentHook}">
				<option value="${requestScope.MRURoleChoice.role_currentHook}"
					selected="selected"><c:out value="${requestScope.MRURoleChoice.role_currentObjectName}" /></option>
			</c:if>
			<c:if test="${mayUpdate}">
				<c:if test="${! empty requestScope.MRURoleChoice.possibleMRUItems}">
				<optgroup label="Recently Used">
					<c:forEach items="${ requestScope.MRURoleChoice.possibleMRUItems }" var="entry">
						<option value="${entry.key}"><c:out value="${entry.value}"/></option>
					</c:forEach>
				</optgroup>
				</c:if>
				<optgroup label="Others">
					<option value="[NONE]">Search More...</option>
				</optgroup>
			</c:if>
		</select>
<!-- ChooseFromMRU.jsp -->
