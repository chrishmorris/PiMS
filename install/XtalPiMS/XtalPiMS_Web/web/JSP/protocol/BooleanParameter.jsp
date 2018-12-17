<%@ page contentType="text/html; charset=utf-8" language="java" 
  import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*,org.pimslims.presentation.protocol.*,org.pimslims.model.protocol.*"  
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="modelObject" value="${requestScope[param['beanName']]}" />
<c:set var="disabled" value=""/>
<c:if test="${!mayUpdate}"><c:set var="disabled" value="onfocus=\"this.blur()\""/></c:if>

<td>
    <img class="icon" style="float:left" src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/booleanicon.gif" alt=""/>
    ${modelObject.name}
    <c:if test="${'Yes'==modelObject.values['isMandatory']}">
        <span class="required">*</span>    
    </c:if>
</td>
    
<td>
    ${modelObject.values['label']}
</td>
    
<td>
    <c:choose>
        <c:when test="${!emptymodelObject.values['defaultValue']}">${modelObject.values['defaultValue']}</c:when>
        <c:otherwise>&nbsp;</c:otherwise>
    </c:choose>
</td>

<td>
    Yes, No
</td>
