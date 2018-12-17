<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: Susy Griffiths YSBL
Date: 04-Jul-2008
Servlets: /org/pimslims/servlet/dnatarget/ExtensionsList.java 

-->
<%-- bean declarations --%>
<jsp:useBean id="fexs" scope="request" type="java.util.ArrayList" />
<jsp:useBean id="rexs" scope="request" type="java.util.ArrayList" />
<jsp:useBean id="forwardExtension" scope="request" type="java.lang.Long" />
<jsp:useBean id="reverseExtension" scope="request" type="java.lang.Long" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Extensions' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- OLD -->

<%-- page body here --%>

    <c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}/">Home</a> : Extensions
    </c:set>
    <c:set var="icon" value="blank.png" />
    <c:set var="title" value="5'-Extensions for Primers"/>
    <c:set var="actions">
        <pimsWidget:linkWithIcon 
                    icon="actions/create/extension.gif" 
                    url="${pageContext.request.contextPath}/Create/org.pimslims.model.molecule.Extension"
                    text="New Extension"/>    
        <pimsWidget:linkWithIcon 
                    icon="misc/help.gif" 
                    url="${pageContext.request.contextPath}/help/HelpExtensions.jsp#extlist"
                    text="Help"/>
    </c:set>    
    <pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>

<c:set var="numFex" value="${fn:length(fexs)}" />
<c:set var="numRex" value="${fn:length(rexs)}" />

<c:choose>
 <c:when test="${numFex == 0}">
        <c:out value="No Forward Extensions in PiMS" />
 </c:when>
 <c:otherwise>
  <c:set var="extraHeader"  scope="page" value="${numFex} recorded in PiMS"/>
  <pimsWidget:box title="Forward Extensions"  extraHeader="${extraHeader}" initialState="closed" >
   <div class="collapsibleBox_content"> 
	<table border="0" class="list" >
 	 <tr><th>&nbsp;</th><th>Name</th><th class="sortable">Sequence</th></tr>
  	 <c:forEach var="fex" items="${fexs}" varStatus="status" >
       	 <tr>
    
    <td><pimsWidget:link bean="${fex}"/></td>
  	 	<td><c:out value="${fex.exSeq}" /></td></tr>
     </c:forEach>
	</table>
   </div>
  </pimsWidget:box>
 </c:otherwise>
</c:choose>

<c:choose>
 <c:when test="${numRex == 0}">
        <c:out value="No Reverse Extensions in PiMS" />
 </c:when>
 <c:otherwise>
  <c:set var="extraHeader"  scope="page" value="${numRex} recorded in PiMS"/>
  <pimsWidget:box title="Reverse Extensions" extraHeader="${extraHeader}" initialState="closed" >
   <div class="collapsibleBox_content"> 
	<table border="0" class="list" >
 	 <tr><th>&nbsp;</th><th>Name</th><th>Sequence</th></tr>
  	 <c:forEach var="rex" items="${rexs}" varStatus="status" >
       	 <tr>
    
    <td><pimsWidget:link bean="${rex}"/></td>
 	 	<td><c:out value="${rex.exSeq}" /></td></tr>
     </c:forEach>
	</table>
   </div>
  </pimsWidget:box>
 </c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
