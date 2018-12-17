<!-- notes.tag -->
<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectBean" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>

<c:set var="extraHeader" value="" />
<c:if test="${empty bean.notes}"><c:set var="extraHeader" value="(None)" /></c:if>


<pimsWidget:box title="Notes" extraHeader="${extraHeader}" id="<%= org.pimslims.servlet.Notes.NOTES %>" initialState="closed" >
<pimsForm:form extraClasses="hastable" action="/update/Notes" mode="edit" method="post">
<table>
<tr>
<th style="width:144px; padding-top: .5em;">Date</th>
<th style="width: 210px; padding-top: .5em;">Recorded by</th>
<th style="padding-top: .5em;" >Details</th>
<th style="width: 88px; padding-top: .5em;" >See also</th>
</tr>
<c:forEach items="${bean.notes}" var="note">
<tr id="${note.hook}" class="ajax_deletable"> 
  <td><pimsWidget:dateLink date="${note.date}" /></td>
  <td><c:if test="${!empty note.person}"><pimsWidget:link bean="${note.person}" /></c:if></td>
  
  <td>
    <div style="margin-left:0;" class="formfield" >
      <textarea style="height:15em;"><c:out value="${note.details}" /></textarea>
    </div>
  </td>
  
  <td><c:forEach var="associate" items="${note.seeAlso}">
    <pimsWidget:link bean="${associate}" />
  </c:forEach></td>
</tr>
</c:forEach>

<c:if test="${empty bean.notes}">
<tr><td colspan="4" style="text-align:center">(No notes)</td></tr>
</c:if>


<c:choose><c:when test="${bean.mayUpdate}">
  <tr>
    <td ><script>document.write(new Date().toLocaleString())</script></td>
    <td>${username}</td>
    <td><textarea style="width:97%" name="${bean.hook }:_addNote" id="${bean.hook }:_addNote"></textarea></td>
    <td ><input type="submit" value="Add" onclick="dontWarn()" /></td>
  </tr>

</c:when><c:otherwise>
  <tr>
    <td colspan="4">You do not have access rights to add notes for <c:out value="${bean.name}" /></td>
  </tr>
</c:otherwise></c:choose>

</table>
<c:if test="${bean.mayUpdate}">
	<script type="text/javascript">attachValidation("${bean.hook }:_addNote",{required:true, alias:"Note"})</script>
</c:if>
</pimsForm:form>
<!-- /notes.tag -->
</pimsWidget:box> 