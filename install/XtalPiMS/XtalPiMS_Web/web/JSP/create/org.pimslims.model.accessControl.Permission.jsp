<%--
CreatePermission.jsp works with Create servlet
For more information @see org.pimslims.servlet.CustomCreate 

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Create Permission' />
</jsp:include>

<!-- OLD -->

<jsp:scriptlet>
  String style = "even";
  pageContext.setAttribute("style", style);
</jsp:scriptlet>
<div class="mainform">

<p class="help">Record a permission, to extend the access rights given to users of PiMS.<br />
</p>



<form method="post" action="${pageContext.request.contextPath}/Create/${pathInfo}" >
    <%-- TODO CSRF token --%>
<input type="hidden" name="METACLASSNAME" value="${metaclass.metaClassName}"/>

<c:if test="${! empty errorMessages['notProvided']}">
    <div class="formrow" style="text-align:left">
        <span style="color:red;font-weight:bold">
        To create a
        permission, you first need to specify a
        User Group and a Lab Notebook. Please click one of the links below to search for an
        existing ${errorMessages["notProvided"]} or create a new one.
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

<%-- Iterate other manadatory roles --%>
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




<div class="formrow">
    <label for="owner"  title="Lab Notebook, used to determine the access rights for the record you are about to create.">Owner</label>
    <span title="Owner cannot be changed here, use different login to change it">${owner}</span>
</div> 

<!-- Pass the parameters -->
<input type="hidden" name="parametersString"  value="${parameters}" />

<div class="formrow" >
    <label class="label" for="opType"><span title="Free text, for notes, explanatory comments, etc.">
        operation type
    </span></label>
    <select onchange='onEdit()' id="org.pimslims.model.accessControl.Permission:opType" name="org.pimslims.model.accessControl.Permission:opType" rows="20" >
        <option value="create">create</option>
        <option value="read" selected="selected">read</option>
        <option value="update">update</option>
        <option value="delete">delete</option>
    </select>
</div>
<input type="hidden" name="org.pimslims.model.accessControl.Permission:permissionClass" value="PIMS" />
<input type="hidden" name="org.pimslims.model.accessControl.Permission:roleName" value="rw" />
<input type="hidden" name="org.pimslims.model.accessControl.Permission:permission" value="Yes" />

<input class="submit button" type="submit" value="Save" onclick="return checkSequence();"/>
</form>

</div> <!-- mainform div end -->


<!-- javascript -->
${javascript}

<jsp:include page="/JSP/core/Footer.jsp" />
