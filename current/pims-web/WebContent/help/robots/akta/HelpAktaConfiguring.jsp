<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName"
        value='OPPF Alta Experiment Help' />
</jsp:include>

<div class="help">

<p>
Before we can connect we need to enter some information about the Unicorn system we want to connect to.
</p>
<p>
Select configure in the configure menu on the menubar.
</p>
<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/configuremenu.jpg" alt="JUnicorn configuremenu" /><br />
<br />
<b>The JUnicorn configure menu</b>
</p>
<p>
A new window will open with the configurable options available.  This is populated with the current values.
</p>

<p style="text-align: center;">
<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/robot/akta/configure.jpg" alt="JUnicorn configure" /><br />
<br />
<b>The JUnicorn configure window</b>
</p>

<p>
Enter the domain name of the network containing the computer running Unicorn.  The user and password of the
user running Unicorn must next be entered.  Finally the host name of the computer running Unicorn.  When
all these have been entered select the Enter button.
</p>
<p>
Although we have entered a user name, please note that Unicorn offers OPC access only by the Unicorn user
'OPC user'.  Make sure that this user has been created in your Unicorn installation, and has access to the
appropriate folders.  Also please check that the All Users flag is checked in the OPC section of administration/options within the Unicorn Manager.
</p>
<p>JUnicorn is an OPC client and uses DCOM to communicate over a network.  The OPC foundation have a guide
<a href="http://opcfoundation.org/Archive/c218e7b5-4e00-4f95-82ba-7da07eb17883/Using%20OPC%20via%20DCOM%20with%20XP%20SP2%20v1.10.pdf">Using OPC via DCOM with XP SP2</a>
to help with any problems.
</p>

<p>
We are now ready to connect to Unicorn.
</p>
<br />

<a href="HelpAktaOverview.jsp">Back</a> to Akta experiments in PiMS<br />

</div>

<jsp:include page="/JSP/core/Footer.jsp" />
