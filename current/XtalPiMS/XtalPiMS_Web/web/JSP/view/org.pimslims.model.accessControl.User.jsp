<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Custom view of User
--%>

<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.model.target.*;import org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.presentation.ServletUtil" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<c:catch var="error">

<%-- Mandatory parameters --%>
<jsp:useBean id="ObjName" scope="request" type="java.lang.String" />
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${ObjName}: ${bean.name}" />
</jsp:include>  

<!--  view/org.pimslims.model.accessControl.User.jsp -->
<c:set var="breadcrumbs">
    <a title="Search Users" href="${pageContext.request.contextPath}/Search/${bean.className}">Users</a>
</c:set>
<%-- TODO icon --%>        

<c:set var="submitaction">/update/ToggleActive/${bean.hook}</c:set>
<form id="dummy_isactiveform" method="post" style="display:none"
    action="${pageContext.request.contextPath}${submitaction}" >
  <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,submitaction)}" />
</form>      
<c:set var="actions">
    <pimsWidget:deleteLink bean="${bean}" />
    &nbsp;
    <a href="${pageContext.request.contextPath}/update/PasswordChange?username=${bean.name}">Change Password</a>

    <c:choose><c:when test="${'Yes' eq bean.values['isActive'] && isLeader }">
            <a href="javascript:$('dummy_isactiveform').submit();"
              title="Prevent this user from logging on"
            >Make Inactive</a>
    </c:when><c:when test="${'No' eq bean.values['isActive'] && isLeader }">
            <a href="javascript:$('dummy_isactiveform').submit();"
              title="Allow this user to log on"
            >Make Active</a>
    </c:when><c:otherwise>
            <!-- current user is not permitted to change isActive -->
    </c:otherwise></c:choose>
        
</c:set>  
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${bean.name}" actions="${actions}" icon="${User.png}" />

<c:set var="fulltitle">
    <span class="linkwithicon" style="float:right; padding:0; padding-right: 1em; white-space: nowrap;"><%-- TODO remove this span --%>
      <a onclick='return warnChange()' href="${pageContext.request['contextPath']}/Create/${bean.className}">Record New</a>
    </span>
    User:
    <pimsWidget:link bean="${bean}" />
</c:set>
<pimsWidget:box title="${fulltitle}" initialState="open" >
    <pimsForm:form action="/Update" mode="view" method="post">
    <pimsForm:formBlock>
    
    <c:if test="${null!=bean.metaClass.attributes['pageNumber']}">
       <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book"
       name="${bean.hook}:pageNumber" value="${bean.values['pageNumber']}"
       />
    </c:if>
    <%-- just one required attribute --%> 
    <pimsForm:text validation="required:true, unique:{obj:'${bean.className}', prop:'name'}"
                                alias="Name" helpText="Userid for logging in to PiMS" 
                                 name="${bean.hook}:name" value="${bean.values['name']}"
                  />
         
         
    
          <%-- now the single roles --%>      
          <c:forEach var="entry" items="${bean.metaClass.metaRoles}"><c:if test="${entry.value.high==1}">
          <pimsForm:mruSelect hook="${bean.hook}" rolename="${entry.value.name}" alias="${utils:deCamelCase(entry.value.alias)}" helpText="${entry.value.helpText}" required="${entry.value.low>0}" />          
          </c:if></c:forEach>
    
          <%-- now the attributes that are not required --%>
          <c:forEach var="element" items="${bean.elements}">
            <c:choose><c:when test="${('lastEditedDate' eq element.name)||('creationDate' eq element.name)||('pageNumber' eq element.name)||('digestedPassword' eq element.name)}">
              <%-- handled elsewhere --%>
            </c:when><c:otherwise>
              <c:if test="${!element.required}">
              <pimsForm:formItem name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" validation="${validation}">
                <pimsForm:formItemLabel name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" helpText="${element.helpText}" validation="${validation}" />
                <div class="formfield">
                  <pims:input attribute="${element}" value="${bean.values[element.name]}" name="${bean.hook}:${element.name}" />
                </div>
              </pimsForm:formItem>
              </c:if>
            </c:otherwise></c:choose>
          </c:forEach>
          
          <%-- now the dates of editing --%>
          <pimsForm:formItem alias="Recorded" name="${bean.hook}:creationDate" >
                <pimsForm:formItemLabel name="${bean.hook}:creationDate" alias="Recorded" helpText="When this record was first made"  />
                <div class="formfield">
                  <pimsWidget:dateLink date="${bean.values['creationDate']}" />
                </div>
          </pimsForm:formItem>
          <pimsForm:formItem alias="Last Edited" name="${bean.hook}:lastEditedDate" >
                <pimsForm:formItemLabel name="${bean.hook}:creationDate" alias="Last Edited" helpText="When this record was last updated"  />
                <div class="formfield">
                  <pimsWidget:dateLink date="${bean.values['lastEditedDate']}" />
                </div>
          </pimsForm:formItem> 
          <pimsForm:editSubmit />
    </pimsForm:formBlock>
    </pimsForm:form>    
</pimsWidget:box>

<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
</script>


<c:forEach var="entry" items="${bean.metaClass.metaRoles}">
  <c:if test="${entry.value.high!=1 && 'notes' ne entry.key && 'annotations' ne entry.key && 'attachments' ne entry.key && 'externalDbLinks' ne entry.key && 'citations' ne entry.key  }">  
    <c:set var="mayAdd" value="${bean.mayUpdate && (null==entry.value.otherRole || entry.value.otherRole.changeable)}" />
    <pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="${entry.key}" title="${utils:deCamelCase(entry.key)}" 
      mayAdd="${mayAdd}"
    />    
  </c:if>
</c:forEach>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>


<jsp:include page="/JSP/core/Footer.jsp" />
