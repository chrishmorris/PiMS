<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/view.css" type="text/css" />--%>

<%--
Author: Susy Griffiths YSBL
Date: 060707
--%>


<% String dnaSeq=request.getParameter("dnaSeq");%>
<% String protId=request.getParameter("protId");%>


<h4><c:out value="DNA and translated protein sequence for ${param['protId']}" /></h4>

<jsp:include page="/JSP/DNAandProtSeqView.jsp" flush="true">
    <jsp:param name="dnaSeq" value="${param['dnaSeq']}"/>
</jsp:include>

<script type="text/javascript">
if(document.all || !navigator.vendor){
    document.body.style.fontSize="80%";
}
</script>
<!-- OLD -->

<!-- 
//end 
-->

<!-- OLD -->
