<%--
Display a list of files associated with a model object
This JSP is called by org.pimslims.servlet.ListFiles
and also from the experiment support.
The caller must supply the header and footer for the page.


Author: Petr Troshin
Date: Jan 2007
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<jsp:useBean id="files" scope="request" type="java.util.Collection" />

<c:choose>
<c:when test="${empty files}" >
    No files
</c:when>
<c:otherwise>
<table class="list">
    <tr >
		<th></th>
    <th>Files description</th>
    <th>Click to view</th>
    <th>Size</th></tr>

<c:forEach items="${files}" var="file">
      <tr class="ajax_deletable" id="${file.hook}">
       	<td style="padding:2px 0 0 3px;width:20px;">
   	         <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif"
                 id="${file.hook}_deleteicon" onclick="ajax_delete(this)"/>
		</td>
        <td>
        <c:out value="${file.legend}" />
        </td>
        <td>
        <a href="${pageContext.request.contextPath}/ViewFile/${file.hook}/${file.name}" title="view file" ><c:out value="${file.details}"/></a>
        </td>
        <td>
	        <c:out value="${file.length}"/>
        </td>
      </tr>
</c:forEach>
</table>
</c:otherwise>
</c:choose>

<!-- /DisplayFiles.jsp -->

<!-- OLD -->
