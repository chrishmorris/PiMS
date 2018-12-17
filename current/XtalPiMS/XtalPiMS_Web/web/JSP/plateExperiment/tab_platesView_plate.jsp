<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!-- tab_platesView_plate.jsp -->
    <table class="plate" id="${param['containerPrefix']}${param['platePosition']}">
        <tr class="top"><th>&nbsp;</th>
        <c:forEach var="col" items="${plateCollection[0]}" varStatus="count">
        <th>${cols[count.count-1]}</th>
        </c:forEach>
        <th>&nbsp;</th></tr>
        
        <c:forEach var="row" items="${plateCollection}" varStatus="rowCount">        
            <tr><th>${rows[rowCount.count-1]}</th>
            <c:forEach var="well" items="${row}" varStatus="colCount">
                <c:choose><c:when test="${!empty well}">
                <td class="well ex${well.dbId}">&nbsp;</td>
                </c:when><c:otherwise>
                <td class="well empty">&nbsp;</td>
                </c:otherwise></c:choose>
            </c:forEach>
            <th>&nbsp;</th></tr>
        </c:forEach>
        <tr class="bottom">
            <th>&nbsp;</th>
            <th colspan="${fn:length(plateCollection[0])}">
                <span class="platename" title="${param['plateHook']}">${param['plateName']}</span>
                <c:if test="${!param['noeditplatename']}">
                <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" title="Edit plate name" onclick="editPlateName(this);return false" />
                <form method="post" action="${pageContext.request.contextPath}/Update">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/Update')}" />
                    <input type="hidden" value="${param['plateName']}" class="platename" name="${param['plateHook']}:name" /> 
                    <input type="hidden" value="plateview" name="_tab" /> 
                </form>
                </c:if>
            </th>
            <th>&nbsp;</th>
        </tr>
    </table>    
<!-- tab_platesView_plate.jsp -->
