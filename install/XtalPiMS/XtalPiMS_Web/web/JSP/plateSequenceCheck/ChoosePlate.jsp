<%--
Display a list of plates available in the system and report 
on what was loaded from the sequence archive

Author: Petr Troshin
Date: November 2007
--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@page import="org.pimslims.lab.sequence.OPPFSequenceLoader"%>
<jsp:useBean id="seqloader" type="OPPFSequenceLoader" scope="request" />


<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Choose a plate" />
</jsp:include>

obsolete


<form method="post" action="${pageContext.request.contextPath}/Edit">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/Edit')}" />


<p>
Files number: ${seqloader.numberOfLoadedFiles}
<br/><a href="${pageContext.request.contextPath}/read/ViewFile/${seqloader.collatedFileHook}/sequences.txt">Collated file</a>
<br/><a href="${pageContext.request.contextPath}/read/ViewFile/${seqloader.originalFileHook}/osequences.zip">Original file</a>
</p>
Rejected file list (having empty sequences)
<c:forEach items="${seqloader.rejectedFiles}" var="file">
<br/> ${file}
</c:forEach>

</form>


<jsp:include page="/JSP/core/Footer.jsp" />
