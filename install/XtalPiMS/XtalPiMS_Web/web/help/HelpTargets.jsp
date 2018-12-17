<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help Guide to using Expert Target Management' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
<p>
PIMS Target management allows you to record details about your Targets and to retrieve target information held in your database.<br />
This includes such  details as the target protein name(s), function, amino-acid and nucleotide sequence, source organism, references to external databases and details of relevant literature references.<br />
see the <a href="${pageContext.request.contextPath}/functions/Target.jsp">Target management</a> homepage.</p>
<p>For an easier to use interface to most of this functionality,
see <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">PiMS Target Management</a></p>.
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp">Create</a> a new Target -manually</li>
  <li><a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp#downloadTarget">Create</a> a new Target -from a file or database record</li>
  <li><a href="HelpTargetScoreboard.jsp">The Target scoreboard</a></li>
 </ul>
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->
 
<jsp:include page="/JSP/core/Footer.jsp" />
