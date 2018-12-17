<%-- 
* Makes a collapsible box listing all the objects in a given role, 
* e.g., targets in a target group. If the user has the right permissions,
* they can add or remove roles.
*
* Be aware that the list of possible roles may be very long.
--%>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="objectHook" required="true"  %><%-- The hook of the object currently viewing. Example: "org.pimslims.model.target.Target:1234" --%>
<%@attribute name="roleName" required="true"  %><%-- the datamodel name of the role. Example: "targetGroups" --%>
<%@attribute name="title" required="true"  %><%-- The user-friendly name of the role, used in the box header. Example: "Target Groups" --%>
<%@attribute name="mayAdd" required="false"  %><%-- True if can associates. Default is page variable mayUpdate --%>
<%@attribute name="functionAfterDelete" required="false"  %><%-- A Javascript function to call after a row in the list is deleted --%>

<%-- functionAfterDelete: 
* Either 
* - the name of an existing function:
*     theFunction
* - or an anonymous function:
*     function(icon){alert('Hello\n'+icon)}
*     function(){ doStuff(); doMoreStuff(); }
* The clicked icon is passed in when the function is called.
*
--%>

<c:if test="${null eq mayAdd}" >
    <c:set var="mayAdd" value="${mayUpdate}" />
</c:if>
<c:set var="boxHeader" scope="page">
<c:set var="queryString"></c:set>
    <c:if test="${mayAdd}">
        <a onclick="startMultiRoleEdit(this,'${pageContext.request.contextPath}/EditRole/${objectHook}/${roleName}?isInModalWindow=true&selectMultiple=true');return false;" href="${pageContext.request.contextPath}/EditRole/${objectHook}/${roleName}">Search/Add</a>
        <c:set var="queryString">canAddRoles=true</c:set>
        &nbsp;
    </c:if>
    <%-- <a href="${pageContext.request.contextPath}/Create/${otherClass.name}?${otherRoleName}=${objectHook}">Create new</a> --%>
</c:set>  
<pimsWidget:box title="${title}" 
    src="${pageContext.request['contextPath']}/read/ListRole/${objectHook}/${roleName}?${queryString}" 
    extraHeader="${boxHeader}"
    id="${objectHook}_${roleName}"
/>
<c:if test="${!empty functionAfterDelete}">
<script type="text/javascript">
document.getElementById("${objectHook}_${roleName}").afterDelete=${functionAfterDelete};
</script>
</c:if> 