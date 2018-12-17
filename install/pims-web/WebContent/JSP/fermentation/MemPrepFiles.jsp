<%@ page contentType="text/html;" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
Author: pvt43
Date: 7 May 2008
This fragment gets included in to LeedsConstructs Jsps
--%>

<div style="display: block">
  <c:choose>
	<c:when test="${! empty VNTIMap}">
		<h2>Q.C. Files</h2>
		<c:set var="readonly" value="readonly='readonly'" scope="request" />
		<c:set var="files" value="${VNTIMap}" scope="request" />
		<jsp:include page="/JSP/DisplayFiles.jsp" />
	</c:when>
	<c:otherwise>
		<h2>Upload Q.C. Files</h2>
		<c:set var="descrValue" value="VNTIMap" scope="request" />
		<c:set var="readonly" value="readonly='readonly'" scope="request" />
		<c:set var="fileFName" value="file1" scope="request" />
		<jsp:include page="/JSP/UploadFiles.jsp" />
	</c:otherwise>
</c:choose>
  </div>
 <div style="display: block;">
	<c:if test="${! empty otherFiles}">
		<h2>Other files</h2>
		<c:set var="readonly" value="readonly='readonly'" scope="request" />
		<c:set var="files" value="${otherFiles}" scope="request" />
		<jsp:include page="/JSP/DisplayFiles.jsp" />
	</c:if>
		<h2>Upload other related information</h2>
		<c:remove var="readonly" scope="request" />
		<c:remove var="descrValue" scope="request" />
		<c:set var="fileFName" value="file3" scope="request" />
		<jsp:include page="/JSP/UploadFiles.jsp" />
  </div>

<!-- OLD -->
