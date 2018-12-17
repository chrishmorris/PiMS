<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
The files are now ready to be uploaded to PiMS.
</p>
<p>
Log in to your PiMS installation and search for your Purification experiment.
From the view of the experiment you can upload the curve created in the previous step.
It will be found in the JUnicorn folder.  In the example below the experiment has had
a curve attached.
</p>
<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/pims2.jpg" alt="JUnicorn pims2" /><br />
</p>

<p>
You can also upload the akta file, this will also be found in the JUnicorn folder.
When you upload a file of this type, PiMS will try to
fill in data into the experiment parameters.  The akta-PiMS mapping file (see the AktaImportMapping
parameter in the pims contect) is examined for an entry matching each experiment parameter.  If one
is found, then data from the akta file is entered against the experiment parameter.  In the example
below, the fields for Purification Notes, and Fractions Pooled, Volume of Pool, Protein of Pool and
Yield for both pools 1 and 2 have been updated.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/pims1.jpg" alt="JUnicorn pims1" /><br />
</p>
<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
