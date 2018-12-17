<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${headerTitle}' />
</jsp:include>

<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/">Home</a>: 
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.people.Person">People</a>
</c:set>
<pimsWidget:pageTitle 
    breadcrumbs="${breadcrumbs}"
    icon="person.png"
    title="New Person"
/>

obsolete
<pimsWidget:box title="Details of the new person" initialState="fixed">
    <pimsForm:form mode="create" action="/Create/org.pimslims.model.people.Person" method="post">
        <input type="hidden" name="METACLASSNAME" value="org.pimslims.model.people.Person" />
        <input type="hidden" name="parametersString" value="" />
        <pimsForm:formBlock>

            <pimsForm:column1>
                <pimsForm:text alias="Family name" name="org.pimslims.model.people.Person:familyName" validation="required:true" 
                    helpText="'Last name' for Western names" />
                <pimsForm:text alias="Given name" name="org.pimslims.model.people.Person:givenName" 
                    helpText="'First name' for Western names" />
                <pimsForm:text alias="Middle initials" name="org.pimslims.model.people.Person:middleInitials" />
            </pimsForm:column1>

            <pimsForm:column2>
                <pimsForm:text alias="Salutation, e.g., Mr." name="org.pimslims.model.people.Person:title" />
                <pimsForm:text alias="Title, e.g., Jr." name="org.pimslims.model.people.Person:familyTitle" />
	
				<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" objects="${accessObjects}"/>
				
            </pimsForm:column2>

        </pimsForm:formBlock>
        
        <pimsForm:formBlock>
                <pimsForm:textarea alias="Details" name="org.pimslims.model.people.Person:details" />
        </pimsForm:formBlock>
        <pimsForm:formBlock>
                <pimsForm:submitCreate />
        </pimsForm:formBlock>
    </pimsForm:form>
</pimsWidget:box>


<jsp:include page="/JSP/core/Footer.jsp" />