<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
Author: Petr Troshin
Date: February 2008
Servlets: SportTarget

--%>
obsolete
<c:choose>
<c:when test="${empty leedsConstructs}"><p style="text-align:center;margin:4em 0 2em 0;">None</p></c:when>
<c:otherwise>
<table cellpadding="2" cellspacing="2" class="list">
	<tr>
	<th>&nbsp;</th>
	<th>Name</th>
	<th>Type</th>
	<th>Box</th>
	<th>Designed By</th>
	</tr>
	<c:forEach items="${leedsConstructs}" var="construct">
	   <tr id="${construct.hook}" class="ajax_deletable">
		<td>
			<img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif"
                 id="${construct.hook}_deleteicon" onclick="ajax_delete(this)"/>
		</td>
	   <td>
	     <a href="${pageContext.request.contextPath}/Construct/${construct.hook}">${construct.name}</a>
	   </td>
	   <td>
	     ${construct.type}
	   </td>
	   <td>
	      <pims:getter version="${construct.version}" hook="${construct.box1}" attributes="name" />
	   </td>
	   	<td>
	      <pims:getter version="${construct.version}" hook="${construct.designedBy}" attributes="name" delimiter=" "/>
	   </td>
	   </tr>
	</c:forEach>
</table>
</c:otherwise>
</c:choose>


<!-- OLD -->
