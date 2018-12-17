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
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.sample.SampleBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<!-- ListSampleBeans.jsp -->

<c:if test="${empty pageing || true==pageing}">
	<jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include>
</c:if>

<c:if test="${empty callbackFunction}">
	<c:set var="callbackFunction" value="selectInMRU" /><%-- TODO may be mistaken --%>
</c:if>

<table class="list" >

    <tr class="rowHeader">
            <th>Name</th>
            <th>Details</th>
            <th>Holder</th>
            <th>Position</th>
            <th>Active</th>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2">
    <tr>
        <c:choose><c:when test="${empty param.isInModalWindow && empty param.isInPopup}">
            
	    </c:when><c:otherwise>
             <c:set var="objectData">
             {
                 name:'${utils:escapeJS(bean.name)}', 
                 hook:'${bean.hook}'
             }
             </c:set>
            <c:choose><c:when test="${!empty param.isGroupExperiment}">
                <%-- same for plates and non-plates --%>
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
        <td style="width:20px;"><pimsWidget:link bean="${bean}" /></td> 
        <td style="width:100px;">
           <c:out value="${utils:truncate(bean.details, 60)}" />
        </td> 
        <td style="width:20px;">
            <c:choose><c:when test="${!empty bean.holder}">
            <pimsWidget:link bean="${bean.holder}" />
            </c:when><c:otherwise>&nbsp;</c:otherwise>
            </c:choose>
        </td> 

        <td><c:out value="${bean.positionInPlate}" /></td>
		<td><c:out value="${bean.isActive}" /></td>
    </tr>
</c:forEach>
</table>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%-- TODO may need
<c:if test="${!param.isInModalWindow && metaRole.high ne 1}">
<div class="behaviour_showafteradd" style="display:none; padding:0.25em 0.25em 0.25em 1.9em;border-top:1px solid #999">
    Items to add:
    <pimsForm:form action="/EditRole/${modelObject.hook}/${metaRole.name}" mode="edit" method="post">
        <div class="behaviour_itemstoadd">&nbsp;</div>
        <input type="submit" value="Add"  />
    </pimsForm:form>
</div>
</c:if> --%>


<!-- /list/org.pimslims.model.sample.Sample.jsp -->

