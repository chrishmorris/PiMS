<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

Functionality has been added to PiMS to support purification experiments performed using Akta Purification Robots and Akta Unicorn software..
obsolete
<br />

<pimsWidget:box initialState="fixed" title="Contents">
<ul>
    <li><a href="HelpAktaInstalling.jsp">Installing the JUnicorn tool</a></li>
    <li><a href="HelpAktaConfiguring.jsp">Configuring the JUnicorn tool</a></li>
    <li><a href="HelpAktaConnecting.jsp">Connecting to Unicorn</a></li>
    <li><a href="HelpAktaBrowsing.jsp">Browsing for results</a></li>
    <li><a href="HelpAktaCreating.jsp">Creating the Akta experiment files</a></li>
    <li><a href="HelpAktaImporting.jsp">Importing into PiMS</a></li>
</ul>
</pimsWidget:box>
<h2>JUnicorn Overview</h2>
<p>
JUnicorn is a tool used to process akta experimental data ready to be imported into a PiMS experiment.
<br />
<br />
Akta chromatography systems are widely used in protein purification.  Akta's UNICORN is a complete
package for control and supervision of synthesis and these systems. It consists of control
software and a controller card for interfacing the controlling PC to the liquid handling module.
Menus allow the scientist to select and modify a method; supervise the chromatography process; and to
record, process and store the results.  Unfortunately the results are stored in a proprietary format
that only UNICORN can read.
<br />
<br />
JUnicorn will connect to an installation of akta Unicorn; allow the user to browse the data structure
to find an experiment result file; select a sample from the result file; and extract the data into
a more readable format.
</p>

<p>
Running the JUnicorn tool opens a window as shown below.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/junicorn.jpg" alt="JUnicorn overview" /><br />
<br />
<b>The JUnicorn interface</b>
</p>

<p>
The window consists of four areas.
</p>
<p>
At the top of the screen is a toolbar with 5 buttons; these
control all the functionality of the tool, a tooltip activated by hovering the mouse pointer over
each button shows what each button is for.  The last button is disabled when the tool is first
opened as its action in inappropriate at this stage.
</p>
<p>
Below the toolbar is a text field, this is used to display the current position in the Unicorn data
structure, it is empty when the tool is first loaded as we have not yet connected to a Unicorn system.
</p>
<p>
Below the textfield is a textarea, we use this to show the options currently available to move to in
the Unicorn data
structure, it is empty when the tool is first loaded as we have not yet connected to a Unicorn system.
</p>
<p>
Lastly are two buttons labelled select and cancel, the cancel button can be used to quit the tool, and
the select button to process a result (the same as the last button on the toolbar).  Again this button
is disabled when the tool is first opened as its action in inappropriate at this stage.
</p>

<a href="${pageContext.request.contextPath}/functions/Help.jsp">Guide</a> to using PIMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
