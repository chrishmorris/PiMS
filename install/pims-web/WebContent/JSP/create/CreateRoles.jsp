<%--
Jsp to represent model object roles supplied as instances
of AttributeToHTML class

Required elements are:
"Roles" instanceof ArrayList roles as the AttributeToHTML instances (required or optional roles)

Optional Attributes

"errorMessages" instanceof Map  error messages
"metaclass" instanceof org.pimslims.metamodel.MetaClass
Example org.pimslims.model.target.Target:protein this is org.pimslims.model.molecule.MolComponent
in example org.pimslims.model.target.Target

For more information @see org.pimslims.servlet.CustomCreate 
see also Create.jsp

Author: Peter Troshin
Date: February 2006

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%--Caller must provide this --%>
<jsp:useBean id="metaclass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="Roles" scope="request" type="java.util.ArrayList" /> <%-- roles as AttributeToHTML instances --%>

<%-- These could be null therefore not suitable as bean
<jsp:useBean id="errorMessages" scope="request" type="java.util.Map" /> <!-- Error messages -->
--%>


<!-- create/CreateRoles.jsp -->

<%-- Iterate other manadatory roles --%>
<c:forEach items="${Roles}" var="attrhtml">
<c:set var="oddKey" value="${attrhtml.name}_ODD"/>
<c:set var="errorDivColor" value=""/>
<c:if	test="${! empty errorMessages[attrhtml.name] || ! empty errorMessages[oddKey]}">
    <c:set var="errorDivColor" value="style='background-color:#fcc'"/>
</c:if>
<div class="formrow" ${errorDivColor}>

<!-- Iterate other engaged hooks -->
            <c:if	test="${! empty errorMessages[attrhtml.name]}">
                <p class="error">
                The supplied ${attrhtml.name} (
                <c:forEach items="${errorMessages[attrhtml.name]}" var="hooklinks">
                     ${hooklinks}&nbsp;
                </c:forEach>
                ) has already been associated with another ${metaclass.alias} and cannot be used again</p>
            </c:if>

<!-- Iterate other odd hooks -->

            <c:if	test="${! empty errorMessages[oddKey]}">
                <p class="error">
                The supplied ${attrhtml.name} (
                <c:forEach items="${errorMessages[oddKey]}" var="hooklinks">
                     &nbsp;${hooklinks}
                </c:forEach>
                 ) is superfluous and cannot be used</p>
            </c:if>

        <label class="label" for="${attrhtml.metaElement.name}">
            <span>${utils:deCamelCase(attrhtml.metaElement.alias)}</span>
            <c:if test="${attrhtml.metaElement.required}"><span class="required">*</span></c:if>
        </label>
		
		
    <div style="margin-left:15em;"><!--new wrapper div for these links - apply CSS in stylesheet-->
    <div style="text-align:right;font-size:smaller;background-color:#99f">
            ${attrhtml.html}
    </div>
            <c:choose>
            <c:when test="${attrhtml.required}">
                <font color="red">${attrhtml.roleAssociations}</font>
        </c:when>
            <c:otherwise >
                    ${attrhtml.roleAssociations}
        </c:otherwise>
        </c:choose>
    </div>
    </div>
</c:forEach>
<!-- create/CreateRoles.jsp -->

