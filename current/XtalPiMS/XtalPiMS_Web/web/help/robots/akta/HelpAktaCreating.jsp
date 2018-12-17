<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
The last operation in this tool is to create the data for uploading to PiMS.
</p>
<p>
Click on either the process button in the toolbar, or the select button at the foot
of the window.  JUnicorn will now prepare two files that can be uploaded to your
experiment in PiMS.  There is a progress bar to keep you entertained whilst these
files are prepared.
</p>
<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/create.jpg" alt="JUnicorn create" /><br />
</p>

<p>
The first file will be called resultfile_samplename.png.  This contains
a curve of the UV readings from your experiment.  It will be normalised for the inject
point, and the fractions will be superimposed.
</p>
<p>
The other file is called resultfile_samplename.akta.  This contains
all of the data from the experiment in a non-proprietary format so that it can be
understood by PiMS.
</p>
<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
