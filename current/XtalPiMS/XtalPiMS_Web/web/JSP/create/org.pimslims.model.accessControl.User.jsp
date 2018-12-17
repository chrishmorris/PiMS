<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
    import="org.pimslims.model.accessControl.User,org.pimslims.model.people.Person"    
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new PiMS User' />
</jsp:include>

<p class="help">A PiMS User.</p>
<c:set var="User" value="<%=User.class.getName() %>" />    
<c:set var="Person" value="<%=Person.class.getName() %>" />
<pimsWidget:box title="New user details" initialState="fixed" extraClasses="narrowbox">
<pimsForm:form action="/Create/org.pimslims.model.accessControl.User" method="post"  mode="create">
    <input name="${User}:userGroups" value="${param['userGroups']}" type="hidden" />
	<pimsForm:formBlock>
        <pimsForm:text name="${User}:name" alias="User Name" helpText="User id for logging in, must be unique" validation="required:true" />
    	<pimsForm:text name="${Person}:title" alias="Title" helpText="Title: Mr., Mrs., Ms., Dr., Professor,..."  />
    	<pimsForm:text name="${Person}:givenName" alias="Given Name" helpText="Personal name; first name, in Western names"  />
    	<pimsForm:text name="${Person}:middleInitials" alias="Middle Initials" helpText="Middle initials (including first one)"  />
    	<pimsForm:text name="${Person}:familyName" alias="Family Name" helpText="Surname; last name, in Western names"  validation="required:true" />
    	<input name="METACLASSNAME" type="hidden" value="${User}" />
    	<pimsForm:submitCreate />
	</pimsForm:formBlock> 
	
</pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
