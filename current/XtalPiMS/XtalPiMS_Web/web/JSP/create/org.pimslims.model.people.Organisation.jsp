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
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.people.Organisation">Organisations</a>
</c:set>
<pimsWidget:pageTitle 
    breadcrumbs="${breadcrumbs}"
    icon="organisation.png"
    title="New Organisation"
/>

<pimsWidget:box title="Details of the new organisation" initialState="fixed">
    <pimsForm:form mode="create" action="/Create/org.pimslims.model.people.Organisation" method="post">
        <input type="hidden" name="METACLASSNAME" value="org.pimslims.model.people.Organisation" />
        <input type="hidden" name="parametersString" value="" />
        <pimsForm:formBlock>

            <pimsForm:column1>
                <pimsForm:text alias="Name" name="org.pimslims.model.people.Organisation:name" validation="required:true"/>
                <pimsForm:text alias="Organisation type" name="org.pimslims.model.people.Organisation:organisationType" />

            </pimsForm:column1>
            <pimsForm:column2>
                <pimsForm:textarea alias="Address" name="org.pimslims.model.people.Organisation:addresses" />
            </pimsForm:column2>
        </pimsForm:formBlock>
        <%-- Second formBlock allows for expansion of Address field--%>        
        <pimsForm:formBlock>

            <pimsForm:column1>
                <pimsForm:text alias="Phone number" name="org.pimslims.model.people.Organisation:phoneNumber" />
                <pimsForm:text alias="Fax number" name="org.pimslims.model.people.Organisation:faxNumber" />
                <pimsForm:text alias="Email address" name="org.pimslims.model.people.Organisation:emailAddress" validation="emailAddress:true" />
                <pimsForm:text alias="Website" name="org.pimslims.model.people.Organisation:url" />
            </pimsForm:column1>

            <pimsForm:column2>
                <pimsForm:text alias="City" name="org.pimslims.model.people.Organisation:city" />
                <pimsForm:text alias="Country" name="org.pimslims.model.people.Organisation:country" />
                <pimsForm:text alias="Postal code" name="org.pimslims.model.people.Organisation:postalCode" />

				<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" objects="${accessObjects}"/>
                
            </pimsForm:column2>

        </pimsForm:formBlock>
        
        <pimsForm:formBlock>
                <pimsForm:textarea alias="Details" name="org.pimslims.model.people.Organisation:details" />
        </pimsForm:formBlock>
        <pimsForm:formBlock>
                <pimsForm:submitCreate />
        </pimsForm:formBlock>
    </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />