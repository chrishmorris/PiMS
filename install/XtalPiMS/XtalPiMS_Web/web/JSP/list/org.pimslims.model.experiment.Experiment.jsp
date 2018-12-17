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
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<c:catch var="error">
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<!-- ListExperimentBeans.jsp -->
 
<c:if test="${empty pageing || true==pageing}">
    <jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include> 
</c:if>

<table   class="list" >

    <tr class="rowHeader">
            
            </th><c:choose>
                <%-- Add control if some provided --%>
                <c:when test="${'' ne controlHeader && null ne controlHeader}" >
                    <th >${controlHeader}</th>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose> 
            <th>Date</th>
            <th>Status</th>
            <th>Experiment</th>
            <!-- <th>Type</th>  -->
            <th>Protocol</th>
            <th>Milestone</th>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2">
    <tr class="ajax_deletable">
        <pimsWidget:listItemControl action="${actions[bean.hook]}" bean="${bean}"  />       
        <td><pimsWidget:dateLink date="${bean.experimentDate}"  /></td>
  		<td><c:out value="${bean.experimentStatus}" default=" " /></td>
  		<td><pimsWidget:link bean="${bean}" /></td>
  		<!--  <td><pimsWidget:link bean="${bean.experimentType}" /></td>  -->
  		<td><c:if test="${null ne bean.protocol}"><pimsWidget:link bean="${bean.protocol}" /></c:if></td>
  		<td><c:if test="${null ne bean.milestone}"><pimsWidget:link bean="${bean.milestone}" /></c:if></td>
    </tr>
</c:forEach>
</table>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /ListExperimentBeans.jsp -->

