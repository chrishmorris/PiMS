<%--
Display a list of PiMS records.
This fragment is used in e.g the generic view.
It is suitable for use in a delayed box

Date: May 2009
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<%@ page import="org.pimslims.model.protocol.*" %>

<c:catch var="error">
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.protocol.ProtocolBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<!-- JSP/list/org.pimslims.model.protocol.Protocol.jsp -->


<c:if test="${empty pageing || true==pageing}">
    <jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include> 
</c:if>

<pims:import className="<%= Protocol.class.getName() %>" />
<table class="list" >

    <tr class="rowHeader">
            <th>Name<!-- link --></th>
            <c:choose>
                <%-- Add control if some provided --%>
                <c:when test="${'' ne controlHeader && null ne controlHeader}" >
                    <th >${controlHeader}</th>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose> 
            <th>Details</th>
            <th style="width:100px;">Is for use</th>
            <th>Objective</th>
            <th>Remarks</th>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2">
    <tr>
		
		<td style="width:20px;"><pimsWidget:link bean="${bean}" /></td>	
        <pimsWidget:listItemControl action="${actions[bean.hook]}" bean="${bean}"  />
		<td><c:out value="${fn:escapeXml(bean.values['details'])}" /></td>
		<td><c:out value="${fn:escapeXml(bean.values['isForUse'])}" /></td>
		<td><c:out value="${fn:escapeXml(bean.values['objective'])}" /></td>
		<td><c:out value="${fn:escapeXml(bean.values['remarks'])}" /></td>
    </tr>
</c:forEach>
</table>
<c:if test="${!param.isInModalWindow && metaRole.high ne 1}">
<div class="behaviour_showafteradd" style="display:none; padding:0.25em 0.25em 0.25em 1.9em;border-top:1px solid #999">
    Items to add:
    <pimsForm:form action="/EditRole/${modelObject.hook}/${metaRole.name}" mode="edit" method="post">
        <div class="behaviour_itemstoadd">&nbsp;</div>
        <input type="submit" value="Add"  />
    </pimsForm:form>
</div>
</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /JSP/list/org.pimslims.model.protocol.Protocol.jsp -->

