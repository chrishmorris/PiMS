<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:useBean id="notebooks" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="groups" scope="request" type="java.util.List<org.pimslims.presentation.UserGroupBean>" />
<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Edit access permissions' />
</jsp:include>

<style>
.create {background-color: #9f9;}
.read {background-color: #99f}
.update {background-color: #ff9}
.delete {background-color: #f99}
.unlock {background-color: #ccc}
</style>
<h2>Set
		  <span  class="create">Create</span>,
		  <span class="read">Read</span>,
		  <span  class="update">Update</span>, 
          <span  class="delete">Delete</span>, and
          <span  class="unlock">Unlock</span>

  Permissions</h2> 
  
<a href="${pageContext.request.contextPath}/Create/org.pimslims.model.core.LabNotebook">New Lab Notebook</a> <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.accessControl.UserGroup">New User Group</a>
 
<br />
<br />
<br />
  
  
  
<form method="get" action=""><table>
  <tr><th>User Groups to display:</th><th>Lab Notebooks to display:</th></tr>
  <tr><td>
  <select name="userGroup" multiple="multiple" size="4" ><c:forEach items="${groups}" var="group">
      <c:choose><c:when test="${toView[group.hook]}">
          <option value="${group.hook}" selected="selected" ><c:out value="${group.name}" /></option>
      </c:when><c:otherwise>
          <option value="${group.hook}"><c:out value="${group.name}" /></option>
      </c:otherwise></c:choose>
  </c:forEach></select>
  </td><td>
  <select name="notebook" multiple="multiple" size="4" ><c:forEach items="${notebooks}" var="notebook">
      <c:choose><c:when test="${toView[notebook.hook]}">
          <option value="${notebook.hook}" selected="selected" ><c:out value="${notebook.name}" /></option>
      </c:when><c:otherwise>
          <option value="${notebook.hook}"><c:out value="${notebook.name}" /></option>
      </c:otherwise></c:choose>
  </c:forEach></select>
  </td></tr>
</table>
  <input type="submit" value="Show" /><br />
  (To add extra User Groups or Lab Notebooks, use Control-Click)</form>  
  <br />
  
<c:if test="${hasSomeEntries}">  
<form method="post" action="${pageContext.request.contextPath}/access/Permission"
             style="width:auto;background-color:transparent">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/access/Permission')}" /><table border="1">
  <tr>
    <th rowspan="2" >

		  User groups:
	</th>
    <th colspan="8">Lab Notebooks:</th>

  </tr>
  <tr> 
    <c:forEach items="${notebooks}" var="notebook">
      <c:if test="${toView[notebook.hook]}">
      <th>
        <pimsWidget:link bean="${notebook}" />
        <input type="hidden" name="notebook" value="${notebook.hook}" >
      </th>
      </c:if>
    </c:forEach>
  </tr>
  <c:forEach items="${groups}" var="group">  	
      <c:if test="${toView[group.hook]}"><tr>
      <th style="width:15em">
        <pimsWidget:link bean="${group}" />
        <input type="hidden" name="group" value="${group.hook}" >
      </th>      
      <c:forEach items="${notebooks}" var="notebook">
      <c:if test="${toView[notebook.hook]}"><td>
		  <span title="can '${group.name}' users create '${notebook}' pages?" class="create">
            <input   type=checkbox  value="on"
              <c:if test="${group.permissions[notebook.name].create}"> checked=checked </c:if>
              <c:if test="${group.permissions[notebook.name].immutable}"> disabled=disabled </c:if>
            name="${group.name}:create:${notebook.name}" />
		  </span>
		  <span title="can '${group.name}' users read '${notebook}' pages?" class="read">
            <input   type=checkbox  value="on"
              <c:if test="${group.permissions[notebook.name].read}"> checked=checked </c:if>
              <c:if test="${group.permissions[notebook.name].immutable}"> disabled=disabled </c:if>
              name="${group.name}:read:${notebook.name}"
            />
		  </span>
		  <span title="can '${group.name}' users update '${notebook}' pages?" class="update">
            <input  type=checkbox  value="on"
              <c:if test="${group.permissions[notebook.name].update}"> checked=checked </c:if>
              <c:if test="${group.permissions[notebook.name].immutable}"> disabled=disabled </c:if>
              name="${group.name}:update:${notebook.name}"
            />
		  </span>
          <span title="can '${group.name}' users delete '${notebook}' pages?" class="delete">
            <input  type=checkbox  value="on"
              <c:if test="${group.permissions[notebook.name].delete}"> checked=checked </c:if>
              <c:if test="${group.permissions[notebook.name].immutable}"> disabled=disabled </c:if>
              name="${group.name}:delete:${notebook.name}"
            />
          </span>
          <span title="can '${group.name}' users unlock '${notebook}' pages?" class="unlock">
            <input  type=checkbox  value="on"
              <c:if test="${group.permissions[notebook.name].unlock}"> checked=checked </c:if>
              <c:if test="${group.permissions[notebook.name].immutable}"> disabled=disabled </c:if>
              name="${group.name}:unlock:${notebook.name}"
            />
          </span>
		  <div style="width:6.1em;height:1px;"><span style="font-size:0px;">&nbsp;</span></div>
	  </td></c:if>
	  </c:forEach>
	</tr></c:if>
  </c:forEach>
</table>
<input type="submit" value="Save permissions" />
</form></c:if>
</c:catch><c:if test="${error != null}">"/><p class="error">error ${error}</p></c:if>


<h2>Notes</h2>
<p>If you only have a small number of users, who work closely together,
then you will probably not want to restrict what anyone can do.
In that case, you do not need to use this page at all.</p>
<p>Larger labs will want to set up a number of Lab Notebooks,
where the data in each Lab Notebook is private to scientists involved in that Lab Notebook. 
For each collaboration there is a user group and a Lab Notebook.</p>

<p>If the scale of your work is even greater, 
you may need to use this page to set up more sophisticated permissions. 
For each user group and Lab Notebook, there are four permissions. 
These specify that the users in this group may, or may not <span class=create >create new records</span> for this Lab Notebook,
<span class=read>view them</span>, <span class=update>edit them</span>, and <span class=delete>delete them</span>.</p>
<dl>

<dt>Administrator</dt>
<dd>There is a special user called "adminstrator" who has full authority to edit any record. You cannot change the permissions for the administrator.</dd>


<dt>Reference data</dt>
<dd>Data marked as "reference" data can be view by any user,
but changed only by the administrator.
You cannot change the permissions for reference data.</dd>




</dl>


<jsp:include page="/JSP/core/Footer.jsp" />
