<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
Now we must browse the Unicorn data structure to find the relevant results.
</p>
<p>
Double click on any node in the text area to make that node current.  You will see the
path in the text field change to reflect the new current node, and the sub nodes in the
text area change accordingly.
</p>
<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/browse.jpg" alt="JUnicorn browse" /><br />
</p>

<p>
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/home.png" alt="JUnicorn home" /><br />
&nbsp;&nbsp;Use the home button (third in the toolbar) to return to the OPC User node.
</p>

<p>
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/up.png" alt="JUnicorn up" /><br />
&nbsp;&nbsp;Use the up button (forth in the toolbar) to change up one level.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/browseup.jpg" alt="JUnicorn browseup" /><br />
<br />
<b>Browse up</b>
</p>

<p>
Browse the data to find the result file containing your experiment, and then identify the sample
within that result file.  A single click on a sample will activate the Process button (fifth in the
toolbar) and the select button at the bottom of the window.
</p>
<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
