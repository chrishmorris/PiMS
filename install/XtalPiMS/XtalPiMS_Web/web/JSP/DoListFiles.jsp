<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: cm65
Date: 3 Mar 2010

--%>

<c:catch var="error">

<pimsForm:form extraClasses="hastable" mode="view" action="/Update" method="post">
<table style="">

<tr>
<th style="width: 350px; padding-top: .5em;">File name</th>
<th style="width: 10em; padding-top: .5em;">Date</th>
<th style="padding-top: .5em;">Description</th>
<th style="width:90px; padding-top: .5em;">Delete</th>
</tr>

<c:forEach items="${attachmentFiles}" var="file">
<tr id="${file.hook}" class="ajax_deletable">

    <td>
        <a onclick='return warnChange()' href="${pageContext.request['contextPath']}/read/ViewFile/${file.hook}/<c:out value="${file.name}" />" title="view file" >
            <c:out value="${file.description}" />
        </a>
    </td>
    <td>
    <%-- note we can't use the datelink tag here, because javascript is not executed within a delayed block  --%>
    <a title="<fmt:formatDate value="${file.date.time}" dateStyle="full" type="both" timeZone="GMT" /> GMT"
  onclick='return warnChange()' 
  onmouseover="this.innerHTML=timestampToString(${file.date.timeInMillis}); "
  href="${pageContext.request['contextPath']}/Day/<fmt:formatDate value="${file.date.time}" dateStyle="short" pattern="yyyy-MM-dd" />"
  >
  <fmt:formatDate value="${file.date.time}" pattern="dd MMM yyyy HH:mm:ss z" type="both"  /> 
</a>
    </td>
    <td>
    	<span class="viewonly">
        	<div style="margin-left:0;" class="formfield" >
           		<textarea style="height:15em;"><c:out value="${file.legend}" /></textarea>
           	</div>
        </span>
        <span class="editonly">
        	<div style="margin-left:0;" class="formfield" >
           		<textarea style="height:15em;" name="${file.hook}:legend"><c:out value="${file.legend}" /></textarea>
           	</div>
        </span>
    </td>
    
    <td style="text-align: left;">
         <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" 
                 id="${file.hook}_deleteicon" onclick="ajax_delete(this)"/>
    </td>
</tr>
</c:forEach>
<c:choose><c:when test="${!empty attachmentFiles}">
    <tr>
        <td colspan="4">
            <pimsForm:editSubmit/>
        </td>
    </tr>
</c:when><c:otherwise>
<tr>
    <td colspan="4" style="text-align:center">
        (No attachments)
    </td>
</tr>
</c:otherwise></c:choose>
</table>
</pimsForm:form>

<pimsForm:form extraClasses="hastable" mode="edit" action="/update/ListFiles/${bean.hook}" method="post" enctype="multipart/form-data">
<table style="">
<c:choose>
<c:when test="${bean.mayUpdate}">
<tr>
<td style="width:350px; padding-left: 1.9em; white-space:nowrap;">Upload a file: <input style="" type="file" name="attachmentfile" id="attachmentfile" /></td>
<%--
<td><input style="width:99%" ${readonly} type="text" name="fileDescription" id="fileDescription" maxlength="253" value="${descrValue}" /></td>
--%>
<td><textarea style="width:97%" ${readonly} name="fileDescription" id="fileDescription" maxlength="253">${descrValue}</textarea></td>
<td style="width:120px;padding:0;text-align:center">
    <input type="hidden" name="_anchor" value="${currentBox}" />
    <input type="submit" value="Upload File"  name="userreq"
      onClick="if (''==document.getElementById('attachmentfile').value) {alert('Please choose a file to upload'); return false;} return true" /></td>
</tr>
</c:when>
<c:otherwise>
<tr>
<td colspan="4">You do not have access rights to attach files to
    <c:choose><c:when test="${empty bean.name}">this record</c:when><c:otherwise><c:out value="${bean.name}" /></c:otherwise></c:choose>
</td>
</tr>
</c:otherwise>
</c:choose>
</table>
</pimsForm:form>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    