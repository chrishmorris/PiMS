<%--
Jsp to represent model object attributes supplied as instances
of AttributeToHTML class
Required elements are:
"Attributes" instanceof ArrayList  form elements as
AttributeToHTML instances (required or optional attributes)
Optional Attributes
"errorMessages" instanceof Map  error messages

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
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%--Caller must provide this --%>
<jsp:useBean id="Attributes" scope="request" type="java.util.Collection" /> <%-- form elements as AttributeToHTML instances --%>
<%-- These could be null therefore not suitable as bean
 <jsp:useBean id="errorMessages" scope="request" type="java.util.Map" /> <%-- Error messages --%>
<!-- create/CreateAttributes.jsp -->
 
<c:forEach items="${Attributes}" var="attrhtml">
  <c:set var="errorDivColor" value=""/>
  <c:if	test="${! empty errorMessages[attrhtml.name] || ! empty errorMessages[oddKey]}">
	  <c:set var="errorDivColor" value="style='background-color:#fcc'"/>
  </c:if>
  <c:choose><c:when test="${fn:contains(attrhtml.html,'cal_icon')}">
    <pimsForm:date alias="${utils:deCamelCase(attrhtml.metaElement.alias)}" name="${metaclass.metaClassName}:${attrhtml.metaElement.name}" />     
  </c:when><c:otherwise>
    <%-- do it the old way, with HTML written by AttributeToHTML --%>
    <div class="formitem ${errorDivColor}">
			<c:if test="${! empty errorMessages[attrhtml.name]}">
				<p class="error">${errorMessages[attrhtml.name]}</p>
			</c:if>
      <div class="fieldname">
        <label class="label" for="${attrhtml.metaElement.name}">
            <%-- The next two fn:blocks are used to capitalise the first letter --%>
            <span>${utils:deCamelCase(attrhtml.metaElement.alias)}</span>
            <c:if test="${attrhtml.metaElement.required}"><span class="required">*</span></c:if>
        </label>
      </div>
      <div class="formfield">	    
	        ${attrhtml.html}
	  </div>			
  </div>
</c:otherwise></c:choose>
	</c:forEach>
<!-- create/CreateAttributes.jsp -->
