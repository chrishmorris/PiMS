<%--
Create.jsp works with Create servlet
It displays the generated by Create servlet html in appropriate html mockups
It also main control the generation workflow.
It includes the output from other JSP to actually display attributes
(CreateAttributes) and roles (CreateRoles).
Mandatory parameters are represented as beans


Optional elements are:
"reqAttr" instanceof ArrayList  form elements as AttributeToHTML instances (required attributes)
"optAttr" instanceof ArrayList  form elements as AttributeToHTML instances (optional attributes)
"reqRoles" instanceof ArrayList roles as AttributeToHTML instances (required roles)
"errorMessages" instanceof Map  error messages
For more information @see org.pimslims.servlet.CustomCreate 

Author: Peter Troshin
Date: February 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<%--Caller must provide this --%>
<jsp:useBean id="metaclass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<%-- These could be null therefore not suitable as bean
 <jsp:useBean id="reqAttr" scope="request" type="java.util.ArrayList" /> <!-- form elements as AttributeToHTML instances -->
 <jsp:useBean id="optAttr" scope="request" type="java.util.ArrayList" /> <!-- form elements as AttributeToHTML instances -->
 <jsp:useBean id="errorMessages" scope="request" type="java.util.Map" /> <!-- Error messages -->
--%>
<jsp:useBean id="javascript" scope="request" type="java.lang.String" />
<jsp:useBean id="headerTitle" scope="request" type="java.lang.String" />
<jsp:useBean id="pathInfo" scope="request" type="java.lang.String" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${headerTitle}' />
</jsp:include>

<c:set var="isLabBookEntry">
    <%= org.pimslims.model.core.LabBookEntry.class.isAssignableFrom(((org.pimslims.metamodel.MetaClass)request.getAttribute("metaclass")).getJavaClass()) %>
</c:set>

<jsp:scriptlet>
  String style = "even";
  pageContext.setAttribute("style", style);
</jsp:scriptlet>

<%--<p class="help">${helptext}<br /> --%>

 
<c:if test="${! empty errorMessages['notProvided']}">
    <div class="formrow" style="text-align:left">
        <span style="color:red;font-weight:bold">
        To create a
        ${metaclass.alias}, you first need to specify a
        ${errorMessages['notProvided']}. Please click one of the links below to search for an
        existing ${errorMessages['notProvided']} or create a new one.
        </span>
    </div>
</c:if>

<%-- There may be an error for the attribute does not displayed on the page show it here  --%>
<c:if test="${! empty errorMessages['missedErrorFields']}">
    <div class="formrow" style="text-align:left">
        <span style="color:red;font-weight:bold">
		There has been the following errors: 
		<c:forEach items="${errorMessages['missedErrorFields']}" var="fieldsNames">
			<br />The ${fieldsNames} - ${errorMessages[fieldsNames]}	
		</c:forEach>
        </span>
    </div>
</c:if>

<!-- If this is role -->

<%-- Show other manadatory roles --%>
<c:set var="Roles" value="${reqRoles}" scope="request"/>
<jsp:include page="/JSP/create/CreateRoles.jsp" />


<c:if test="${! fn:contains(pathInfo, ':') && ! empty reqRoles }">
 <div class="formrow" style="text-align:left">
     <p class="help">Next use the form below to fill details of the new ${metaclass.alias},
     then click &quot;Save&quot;.<br /> If you click any link between filling in the form and saving,
     the information that you have typed may be lost.
     </p>
 </div>
</c:if>

<pimsWidget:box title="Details of new ${metaclass.alias}" initialState="fixed" >
<pimsForm:form  action="/Create/${pathInfo}" mode="edit" method="post">
<pimsForm:formBlock>

<input type="hidden" name="METACLASSNAME" value="${metaclass.metaClassName}"/>

<!-- If this is attribute -->

<%-- Iterate other required attributes --%>
<c:set var="Attributes" value="${reqAttr}" scope="request"/>
<jsp:include page="/JSP/create/CreateAttributes.jsp"></jsp:include>

<%-- Iterate other optional attributes --%>
<c:set var="Attributes" value="${optAttr}" scope="request"/>
<jsp:include page="/JSP/create/CreateAttributes.jsp"></jsp:include>

<c:if test="${isLabBookEntry}"> 
<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" 
    objects="${accessObjects}"  currentValue="${notebookHook}" /> 
</c:if>


<%-- see Create.java --%>
<input type="hidden" name="parametersString"  value="${parameters}" />

<input class="submit button"  type="submit" value="Save" onclick="dontWarn(); return checkSequence();"/>
</pimsForm:formBlock></pimsForm:form></pimsWidget:box>



<!-- javascript -->
${javascript}

<jsp:include page="/JSP/core/Footer.jsp" />
