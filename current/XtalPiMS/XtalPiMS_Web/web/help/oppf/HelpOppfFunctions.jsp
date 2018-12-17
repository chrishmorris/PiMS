<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
	<jsp:param name="HeaderName"
		value='OPPF Functionality Help' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="OPPF Functionality">

Functionality has been added to PiMS to support the work done by the OPPF.
</pimsWidget:box>
<br />

<pimsWidget:box initialState="fixed" title="Contents">
<ul>
	<li><a href="HelpOperonOrderForm.jsp">Primer Ordering</a></li>
	<li><a href="HelpScorePlate.jsp">Scoring Plate Results</a></li>
	<li><a href="HelpSequencePlate.jsp">Recording Sequencing Results</a></li>
</ul>
</pimsWidget:box>
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/oppffunctions.jpg" alt="OPPF Functions" /><br />
<br />  
   
<a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
