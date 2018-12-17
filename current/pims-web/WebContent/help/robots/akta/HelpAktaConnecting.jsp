<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
Having set the configuration options it is now time to connect to the Unicorn software.
</p>
<p>
Click on the first button in the toolbar.
</p>
<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/connect.jpg" alt="JUnicorn connect" /><br />
</p>

<p>
JUnicorn will try to connect to the Unicorn system specified in the configuration.  It is
a constraint within Unicorn that DCOM connections are made using the Unicorn user OPC User.
Please make sure that this user has been set up by the Unicorn administrator, with appropriate
access.
</p>

<p>
JUnicorn will show the current path within the Unicorn data structure in the text field, in this
case it is just OPC User.  The sub nodes available are shown in the text area, in the example below
these are c:Default, c:Failed and c:test.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/connected.jpg" alt="JUnicorn connected" /><br />
<br />
<b>Connected to Unicorn</b>
</p>
<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
