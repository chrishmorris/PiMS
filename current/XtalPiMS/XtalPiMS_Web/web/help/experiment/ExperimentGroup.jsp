<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Experiment groups' />
</jsp:include>
<div class="helpcontents">
<p>The experiment group screens allow you to enter several similar experiments at once, more easily than entering them individually. All
experiments in a group must share the same protocol.</p>

<h2>Creating a new group</h2>

<p>From the Experiment menu, select New Experiment Group.</p>
<img src="${pageContext.request.contextPath}/images/helpScreenshots/exptGroup/create.png" alt=""/>
<p>You must give the group a name, and say how many experiments are in the group. You also need to choose a protocol for the experiments.</p>

<p>Click "Save and continue". There will be a short delay while the experiments are created.</p>

<h2>Editing experiments in the group</h2>


<p>The experiment group page consists of a number of tabs. In brief, these are:</p>

<ul><li>Basic details - The information common to the whole group, which you entered when creating the group</li>
<li>Experiments - Allows you to see and make changes to details of the experiments in the group</li>
<li>Next Experiment - Allows you do record the following experiment</li>
<li>Attachments - For images and files</li></ul>


<h2>Experiments</h2>

<img src="${pageContext.request.contextPath}/images/helpScreenshots/exptGroup/groupview.png" alt=""/>


<h3>Selecting experiments to change</h3>

<p>Experiments in the group are numbered consecutively from 1. There is a grid of checkboxes on the right of the tab, each box
corresponding to an experiment. To select or deselect an experiment, click on the corresponding checkbox.</p>


<h2>Choosing samples</h2>

<p>As with single experiments, you can record which samples (and how much) you used in your experiments.</p>

<img src="${pageContext.request.contextPath}/images/helpScreenshots/exptGroup/sampleselector.png" alt=""/>

<p>The sample selector consists of:</p>
<ul><li>A magnifying glass icon - this lets you view the currently selected sample</li>
<li>A dropdown list of samples, with the option to search for more</li>
<li>A box for the amount</li></ul>

<h3>View icon</h3>
<p>Click the icon to view the selected sample.</p>

<p>The icon will be greyed out if a sample is not selected.</p>

<h3>List of samples</h3>
<p>The drop-down list shows the currently-selected sample - or (None) if no sample is selected - and allows you to change it.</p>

<p>In the Quick Setup and Group view tabs, this may read (Various). This means that not all experiments (in Quick setup), or not all
selected experiments (in Group view), use the same sample. You cannot select (Various) yourself.</p>

<p>If you have recently viewed any samples that can be used here, they will be available in the drop-down list. Simply select one.</p>

<p>If the sample you want is not in the list, click Search...</p>

<p>A pop-up window appears over the page, and you will be shown a list of samples that can be used. Click Select beside the sample
you want. The window will close, and the sample will be added to the dropdown list and selected for you. If you decide that you
don't want to use any of the samples listed, click the [x] in the top right corner of the pop-up to close it.</p>



<h3>Amount</h3>
<p>Simply enter a number. The unit is automatically recorded for you.</p>

<h3>And finally...</h3>
<p>Don't forget to save your changes.</p>

</div>
<jsp:include page="/JSP/core/Footer.jsp" />