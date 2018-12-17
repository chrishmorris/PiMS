<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%-- 
Author: cm65
Date: 3 Mar 2010

--%>

<c:catch var="error">


<pimsForm:form extraClasses="hastable" mode="view" action="/Update" method="post">

<table>
<c:forEach items="${imageFiles}" var="file">
<tr id="${file.hook}" class="ajax_deletable">
    <td>
    <span style="float:left">
    
        <span class="viewonly">
             <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" 
                 id="${file.hook}_deleteicon" onclick="ajax_delete(this)" style="cursor:pointer"/>
            <c:out value="${file.title}" />
        </span>
        <span class="editonly">
             <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" 
                 id="${file.hook}_deleteicon" onclick="ajax_delete(this)" style="cursor:pointer"/>
            <div style="margin-left:0;" class="formfield" >
            <input type="text" title="title" name="${file.hook}:title" id="xxx"  value="<c:out value='${file.title}'/>" />
            </div>
        </span><br />
        <a href="${pageContext.request['contextPath']}/read/ViewFile/${file.hook}/<c:out value="${file.name}" /> 
        onclick='return warnChange()'
        title="view file" >
          <c:choose><c:when test="${'image/tiff'  eq file.mimeType}">
            <%-- See http://www.alternatiff.com/howtoembed.html. You need the plug in --%>
            <embed width=300 height=300 src="${pageContext.request['contextPath']}/read/ViewFile/${file.hook}" type="image/tiff"  />
          </c:when><c:otherwise>
            <img class="thumbnail" src="${pageContext.request['contextPath']}/read/ViewFile/${file.hook}" />
            <br /> <%-- note we can't use the datelink tag here, because javascript is not executed within a delayed block  --%>
    <a title="<fmt:formatDate value="${file.date.time}" dateStyle="full" type="both" timeZone="GMT" /> GMT"
  onclick='return warnChange()' 
  onmouseover="this.innerHTML=timestampToString(${file.date.timeInMillis}); "
  href="${pageContext.request['contextPath']}/Day/<fmt:formatDate value="${file.date.time}" dateStyle="short" pattern="yyyy-MM-dd" />"
  >
  <fmt:formatDate value="${file.date.time}" pattern="dd MMM yyyy HH:mm:ss z" type="both"  /> 
</a>
          </c:otherwise></c:choose>
        </a>
        
            <div style="margin-left:0;" class="formfield" >
                <textarea style="height:15em;" name="${file.hook}:legend"><c:out value="${file.legend}" /></textarea>
            </div>
    </span>
    </td>
</tr>
</c:forEach>
</table>

<c:choose><c:when test="${!empty imageFiles}">
    
            <pimsForm:editSubmit/>
       
</c:when><c:otherwise>

        (No images)
    
</c:otherwise></c:choose>

</pimsForm:form>

<pimsForm:form extraClasses="hastable" mode="edit" action="/update/ListFiles/${bean.hook}" method="post" enctype="multipart/form-data">
<table style="">
<c:choose>
<c:when test="${bean.mayUpdate}">
<tr>
<td style="width:350px;white-space:nowrap;text-align:center">Upload a file:&nbsp;<input style="" type="file" name="imagefile" id="imagefile" /></td>
<td style="width:35%;"><input style="width:95%" ${readonly} type="text" name="fileTitle" id="fileTitle" maxlength="253" value="${descrValue}" /></td>
<td style=""><textarea style="width:95%" ${readonly} name="fileLegend" id="fileLegend" maxlength="253">${descrValue}</textarea></td>
<td style="width:120px;padding:0;text-align:center">
    <input type="hidden" name="_anchor" value="${currentBox}" />
    <input type="submit" value="Upload File"  name="userreq"
      onClick="if (''==document.getElementById('imagefile').value) {alert('Please choose a file to upload'); return false;}; dontWarn(); return true" /></td>
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