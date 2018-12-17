<%--
Create a container
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%--Caller must provide this --%>
<jsp:useBean id="metaclass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<%-- These could be null therefore not suitable as bean
 <jsp:useBean id="reqAttr" scope="request" type="java.util.ArrayList" /> <!-- form elements as AttributeToHTML instances -->
 <jsp:useBean id="optAttr" scope="request" type="java.util.ArrayList" /> <!-- form elements as AttributeToHTML instances -->
 <jsp:useBean id="reqRoles" scope="request" type="java.util.ArrayList" /> <!-- roles as AttributeToHTML instances -->
 <jsp:useBean id="errorMessages" scope="request" type="java.util.Map" /> <!-- Error messages -->
--%>
<jsp:useBean id="javascript" scope="request" type="java.lang.String" />
<jsp:useBean id="headerTitle" scope="request" type="java.lang.String" />
<jsp:useBean id="pathInfo" scope="request" type="java.lang.String" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${headerTitle}' />
</jsp:include>


<jsp:scriptlet>
  String style = "even";
  pageContext.setAttribute("style", style);
</jsp:scriptlet>

<pims:import className="org.pimslims.model.holder.Holder" />

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



<pimsWidget:box title="Details of new Container" initialState="fixed" >
<pimsForm:form  action="/Create/${pathInfo}" mode="edit" method="post">
<pimsForm:formBlock>

<c:if test="${!empty lastCreated}">
	<div style="border:1px solid #000;margin:1em 10em; text-align:center; font-weight:bold; padding:0.5em">
	<a href="${pageContext.request.contextPath}/View/${lastCreated._Hook}">${lastCreated.name}</a> was created.
	</div>
</c:if>

<input type="hidden" name="METACLASSNAME" value="${Holder['class'].name}"/>

<!-- If this is attribute -->

<%-- Iterate other required attributes --%>
<c:set var="Attributes" value="${reqAttr}" scope="request"/>
<jsp:include page="/JSP/create/CreateAttributes.jsp"></jsp:include>

<pimsForm:select alias="Container type" name="${Holder['class'].name}:${Holder.PROP_HOLDERTYPE}">
          <c:forEach var="type" items="${holderTypes}" >
          		<c:choose><c:when test="${type.hook eq lastCreated_HolderType}">
	            	<option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
	            </c:when><c:otherwise>
	            	<option value="${type.hook}" ><c:out value="${type.name}" /></option>
	            </c:otherwise></c:choose>
          </c:forEach>
        </pimsForm:select>

<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" 
    objects="${accessObjects}"  currentValue="${notebookHook}" /> 

<pimsForm:checkbox label="I want to create another Container like this" name="_bulkcreate" isChecked="${isBulkCreate}"></pimsForm:checkbox>


<!-- Pass the parameters -->
<input type="hidden" name="parametersString"  value="${parameters}" />

<input class="submit button"  type="submit" value="Save" onclick="dontWarn(); return checkSequence();"/>
</pimsForm:formBlock></pimsForm:form></pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
