<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
The JUnicorn tool is used to browse the Akta Unicorn data structures and prepare the data for import into PiMS.
The tool should be installed on a computer with access to the network with the Unicorn installation, and to the
network with your PiMS server installation.
</p>

<p>
In your chosen installation folder, copy the files JUnicorn_fat.jar and junicorn.properties.
</p>

<p>
To run the JUnicorn tool double click on the file called JUnicorn_fat.jar.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/install.jpg" alt="JUnicorn install" /><br />
<br />
<b>The JUnicorn folder</b>
</p>

<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
