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
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.HolderBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<!-- list/org.pimslims.model.holder.Holder.jsp -->

<c:if test="${empty pageing || true==pageing}">
<jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include>
</c:if>

<c:if test="${empty callbackFunction}">
	<c:set var="callbackFunction" value="selectInMRU" />
</c:if>

<table class="list" >

    <tr class="rowHeader">
            <th><!--highlight-->&nbsp;</th><th><!-- link --></th>
            <%-- <c:forEach items="${attributes}" var="elem">
	  			<th>${utils:deCamelCase(elem.key)}</th>
			</c:forEach> --%>
            <th>Type</th>
            <th>First free position: Row</th>
            <th>First free position: Column</th>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2">
    <tr>
        <c:choose><c:when test="${empty param.isInModalWindow && empty param.isInPopup}">
            
	    </c:when><c:otherwise>
             <c:set var="objectData">
             {
                 name:'${utils:escapeJS(bean.name)}', 
                 hook:'${bean.hook}',
                 rowPosition:'${bean.freeRow}',
                 colPosition:'${bean.freeColumn}'
             }
             </c:set>
             
            <c:choose><c:when test="${!empty param.isGroupExperiment}">
                <td>
                    <span style="text-decoration:underline; color:#006;cursor:pointer;" onclick="window.parent.addSampleToSelect(${objectData})">Add</span>
                </td>
            </c:when><c:when test="${!empty param.selectMultiple}">
                <td>
                    <input type="checkbox" onclick="prepareForAdd(this,${objectData})" />
                </td>
            </c:when><c:otherwise>
            
                <td>
                    <span style="text-decoration:underline; color:#006;cursor:pointer;" onclick="window.parent.${callbackFunction}(${objectData})">Add</span>
                </td>
            </c:otherwise></c:choose>
        </c:otherwise></c:choose>	
        
        <td style="padding:2px 0 0 3px;width:20px;"><pimsWidget:link bean="${bean}" /></td>
        
        <td><c:if test="${null!=bean.values['holderType']}"><pimsWidget:link bean="${bean.values['holderType']}" /></c:if></td>
		<td>${bean.freeRow}</td>
		<td>${bean.freeColumn}</td>
    </tr>
</c:forEach>
</table>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /ListHolderBeans.jsp -->

