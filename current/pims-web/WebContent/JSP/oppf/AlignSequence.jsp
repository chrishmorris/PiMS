<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    %>
<!-- Clustalw.jsp  -->
<%-- Standard PiMS objects for page --%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${pcrsequence.ID} Sequencing" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>


<c:catch var="error">
<jsp:useBean id="alignment"  scope="request" type="org.pimslims.bioinf.local.PimsAlignment" />
<div style="margin-top: 0.6em;" >

<h2><c:out value="${pcrsequence.ID}" /> Sequencing</h2>

<pimsWidget:box title="${pcrsequence.ID}" initialState="closed" >
<table>
<tr>
<td><c:out value="${pcrsequence.waffle}" /></td>
</tr>
<tr>
<td style="font-family: courier new;">${pcrsequence.sequenceforHTML}</td>
</tr>
</table>
</pimsWidget:box>

<pimsWidget:box title="${zipsequence.ID}" initialState="closed" >
<table>
<tr>
<td><c:out value="${zipsequence.waffle}" /></td>
</tr>
<tr>
<td style="font-family: courier new;">${zipsequence.sequenceforHTML}</td>
</tr>
</table>
</pimsWidget:box>

<p>
Finding best orientations...<br />
<c:choose>
	<c:when test="${alignment.isReverseComplement}"> 
		The reverse complement of <c:out value="${alignment.hitName}" />
                 matches better than the actual read
	</c:when>
	<c:otherwise>
		<c:out value="${alignment.hitName}" />  
		matches better than its reverse complement<br/>
	</c:otherwise>
</c:choose>
</p>

<pre>
<c:out value="${clustalw}" />
</pre>
</div>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- note no footer-->
</div></div></body></html>
