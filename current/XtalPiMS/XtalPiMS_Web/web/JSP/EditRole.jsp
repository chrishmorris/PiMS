<%--
Manage an association.
This JSP is called by org.pimslims.servlet.EditRole
TODO version not in modal window is obsolete, and does not work
Author: Chris Morris
Date: 30 November 2005
--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<c:catch var="error">
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="addResults" scope="request" type="java.util.Collection" />
<jsp:useBean id="addControl" scope="request" type="java.util.Map" />
<jsp:useBean id="removeControl" scope="request" type="java.util.Map" />
<jsp:useBean id="metaRole" scope="request" type="org.pimslims.metamodel.MetaRole" />
<jsp:useBean id="displayName" scope="request" type="java.lang.String" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<%-- Whether or not draw a header and <table> tag  --%>
<c:set var="head" value="${true}" scope="request"/>
<!-- EditRole.jsp -->

<c:if test="${empty param['isInModalWindow']}">
<c:choose><c:when test="${-1==metaRole.high && null!=metaRole.otherRole }"><%-- TODO or metaRole.getHigh()>modelObject.get(roleName).size() --%>
<br /><a href="${pageContext.request.contextPath}/Create/${metaRole.otherMetaClass.metaClassName}?${metaRole.otherMetaClass.name}:${metaRole.otherRole.roleName}=${bean.hook} ">Create new ${metaRole.otherMetaClass.alias}
with this ${displayName} as ${metaRole.otherRole.alias}</a>
</c:when></c:choose>
<br /><br />
</c:if>

<c:if test="${empty param['isInModalWindow']}">
<%-- Show the object this role is for --%>
<pimsWidget:details bean="${bean}"/>
</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<c:catch var="error">


<c:if test="${param.isInModalWindow}">
<script type="text/javascript">
var itemsToAdd=[];
function prepareForAdd(elem,objData){
    if("undefined"==objData["name"] || null==objData["name"]){
        var newName=$(elem).up("tr").down("span.linktext").innerHTML;
        objData["name"]=newName;
    } 
    elem.objectData=objData;
}
function doAdd(submitButton){
    var checkedBoxes=$(submitButton).up(".boxcontent").select("input[type=checkbox]");
    var rolesData=[];
    checkedBoxes.each(function (chk){
            //rifle through the data and pass it up to the parent window
        if(chk.checked){
            rolesData.push(chk.objectData);
        }
    });
    window.parent.doAfterMultiRoleEdit(rolesData);
}
</script>
</c:if>

<h2 name="search">Search for ${metaRole.otherMetaClass.alias}s to Add</h2>
    <%-- TODO paging control here --%>
    <jsp:include page="/JSP/SearchForm.jsp" />

<br />
    <c:set var="results" value="${addResults}" scope="request" />
    <c:set var="actions" value="${addControl}" scope="request" />
    <c:set var="controlHeader" value="Add?"  scope="request" />
<c:choose><c:when test="${noSearch}" >
    <%-- show nothing --%>
</c:when><c:when test="${empty addResults}" >
    <hr /><h2>No ${metaRole.otherMetaClass.alias}s found</h2>
</c:when><c:otherwise>

<pimsWidget:box title="${metaRole.otherMetaClass.alias}s found" initialState="open">
  <c:if test="${empty param.isInModalWindow}">
    <form action="${pageContext.request.contextPath}/EditRole/${bean.hook}/${metaRole.roleName}" method="post" class="grid">
    <%-- TODO CSRF token --%>
  </c:if>

   <c:set var="beans" value="${addResults}" scope="request" />
   
   <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/list/${metaRole.otherMetaClass.metaClassName}.jsp" />
   </jsp:include>
     
     
  <br/>
  <br/> 
  <c:choose><c:when test="${metaRole.changeable && empty param.isInModalWindow}">
    <input type="submit" value="Add selected items" />
  </c:when><c:when test="${param.isInModalWindow && metaRole.high eq 1}">
    To set the ${metaRole.alias} for the ${metaRole.metaClass.alias} to a specific ${metaRole.otherMetaClass.alias}, click the appropriate link above, then click the "Save changes" button.
  </c:when><c:when test="${param.isInModalWindow && metaRole.high ne 1}">
    <input type="button" onclick="doAdd(this)" value="Add selected" />
  </c:when><c:otherwise>
    This association cannot be changed
  </c:otherwise></c:choose>
  
  <c:if test="${empty param.isInModalWindow}"></form></c:if>
</pimsWidget:box>
</c:otherwise></c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
