<%--
Display a list of PiMS records.
This fragment is used in e.g the generic view.
It is suitable for use in a delayed box.

If you edit this, you may also need to edit the custom lists. See the mappings in web.xml to find them.

Date: May 2009
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<c:catch var="error">
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.target.ResearchObjectiveElementBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />


<!-- JSP/list/...ResearchObjectiveElement.jsp -->

<c:if test="${empty pageing || true==pageing}">
	<jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include>
</c:if>

<c:choose><c:when test="${empty beans}">None</c:when><c:otherwise>
<form method="post" action="${pageContext.request.contextPath}/Update" ><table class="list" >

    <tr class="rowHeader">
            <th><!-- link --></th>
            <c:choose>
    			<%-- Add control if some provided --%>
				<c:when test="${'' ne controlHeader && null ne controlHeader}" >
					<th >${controlHeader}</th>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>	
	  		<th>Protein</th>
            <th>Start</th>
            <th>End</th>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2" >
    <c:set var="trClass"></c:set>
    <c:if test="${'delete' eq actions[bean.hook]}" >
        <c:set var="trClass">class="ajax_deletable"</c:set>
    </c:if>
    <tr ${trClass} id="${bean.hook}">
        
		
		<td style="padding:2px 0 0 3px;width:20px;"><pimsWidget:link bean="${bean}" /></td>		
			
<c:set var="bean" scope="request" value="${bean}" />
	    <pimsWidget:listItemControl action="${actions[bean.hook]}" bean="${bean}"  />		
        
        <td><c:out value="${bean.proteinName}" /></td>
        <td><input name="${bean.hook}:approxBeginSeqId" value="<c:out value="${bean.start}" />" /></td>
        <td><input name="${bean.hook}:approxEndSeqId" value="<c:out value="${bean.end}" />" /></td>
		
    </tr>
</c:forEach>
</table>
  <br /><div class="shim">nbsp;nbsp;</div><div class="savebutton"><input type="submit" value="Save changes" /></div>
</form>
</c:otherwise></c:choose>

<c:if test="${param.isInModalWindow && metaRole.high ne 1}">
<div class="behaviour_showafteradd" style="display:none;padding:0.25em;border-top:1px solid #999">
	Items to add:
	<pimsForm:form action="/EditRole/${modelObject.hook}/${metaRole.name}" mode="edit" method="post">
		<div class="behaviour_itemstoadd">&nbsp;</div>
		<input type="submit" value="Add" />
	</pimsForm:form>
</div>
</c:if>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /JSP/list/...ResearchObjectiveElement.jsp -->

