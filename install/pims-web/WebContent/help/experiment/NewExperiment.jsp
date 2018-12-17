<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='PiMS Experiment Management Help' />
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="experiment.png" />
<c:set var="title" value="Recording a new experiment"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
PiMS Experiment Management allows you to record the details for Experiments, both
&#39;Simple&#39; or individual Experiments and also Experiments in Plates or in groups.<br />
PiMS experiments use a &#39;Protocol&#39; or &#39;experiment template&#39; to define the Input
Samples, Output Samples and other experimental details you might need to record.
<!-- TODO need link to Protocol help and specific sections throughout -->
You can also associate files (images and data) with the Experiment record, update
the details and search for Experiment records.<br /><br />
A set of PiMS Protocols are provided for you to use and edit.
If you wish to create a new Protocol you can do so by selecting &#39;New Protocol&#39; from the Experiment menu.<br />
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
<div class="leftcolumn">
 <ul>
  <li><a href="#recordSimpleExp">Record</a> a new Simple Experiment
    <ul>
      <li><a href="#expNames">Naming</a> Experiments -in PiMS</li>
    </ul></li>
  
  
 </ul>
</div>
<div class="rightcolumn">
 <ul>
  <li><a href="#nextExp">Linking</a> Experiments</li>
 </ul>
</div>
</pimsWidget:box>
<pimsWidget:box  initialState="closed" title="Related Help">
    <div class="leftcolumn">
        <ul>
        <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp">View and update</a> Experiment details</li>
           <li><a href="${pageContext.request.contextPath}/help/experiment/ExperimentGroup.jsp">Experiment Group Help</a></li>
           <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp">Plate Experiment Help</a></li>
        </ul>
    </div>
    <div class="rightcolumn">
        <ul>
           <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp">Protocol Help</a></li>   
        </ul>    
    </div>
</pimsWidget:box>
</div> <!--end div help-->



 <div class="helpcontents">
 
 <h3 id="sample">Using a sample</h3>
 
 Here is a PiMS page describing a sample:<br />
 <img class="imageNoFloat" id="sampleViewImage" src="${pageContext.request.contextPath}/images/helpScreenshots/sample.png" alt="Sample" />
 <br /><br />


 Towards the bottom of the page you will see a section headed: "Used as inputs to experiments".
 Open this box. It shows a list of past experiments using this sample, and also a list of possible &#39;Next&#39; experiments. 
 On the left is The <strong>Role</strong>, i.e. the part that the sample can play in this experiment. 
 On the right there is a link to the full details of the  <strong>Protocol</strong> (template for the new experiment).
In the middle is the <input class="button" value="New Experiment" type="submit" /> button. Click it and <a href="create">continue</a>.<br />
 

 <img class="imageNoFloat" id="nextExpImage" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/nextexperiment.png" alt="Output sample view page" />
 <br />

 
<div class="toplink"><a href="#">Back to top</a></div>
 
 <h3 id="other">Other starting points for a new experiment</h3>
 
 <p>The usual way to record a new experiment is starting with a sample (aliquot), as described above. 
 There are several other ways to get to the screen below. If you follow one of these other links,
 you will later have to <a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#samples" >tell PiMS the input sample for the experiment</a>.
 The other routes are:<ul>
    <li>From the project (construct)</li>
    <li>From the instrument calendar</li>
    <li>From the protocol</li>
    <li>From the previous experiment - open the Samples box, and click on the Output Sample, then follow the instructions above.</li>
    <li>From the "New Experiment" entry in the "Experiment" menu</li>
 </ul>
 
 </p>
 
<div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="recordSimpleExp">Recording a new experiment</h3>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/new.png" alt="Record a new experiment" />
 
 <div class="textNoFloat">
 <p>The top part of this page allows you to specify the Type of your experiment. 
 Usually PiMS can set this for you, and you can use the other part of the page.
 If the Experiment Type is not right, change it and click "Next".
 </p>
 <p>The lower part of the page specifies the key information for the experiment. 
 Depending how you got here, PiMS may have been able to fill in the se details already.
 Please check them and change them if necessary.<ul>
    <li>The sample to be processed.</li>
    <li>The project (construct) which this activity is part of.</li>
    <li>The protocol to be followed.</li>
    <li>The name of the experiment. This must be different from any other experiment recorded in PiMS.</li>
    <li>The date and time of the start and end of the experiment.</li>
    <li>The Lab Notebook to use. This determines who can read the record of the experiment. This cannot be changed later.</li>
 </ul>When you are happy with these details, click <input class="button" value="Create" type="submit" />.
 </p>

 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 
 <h4 id="expNames">Naming Experiments -suggested names</h4>
  Most records in PiMS include a <strong>Name</strong>. You cannot use the same name for two different records of the same type, 
  e.g. two Experiments cannot have the same name.<br />
  PiMS will often suggest a name for your new Experiment but you can change this.<br /><br />
  The suggested name often includes your <strong>username</strong> to help avoid a clash with an existing record.<br />
  It will also include an abbreviation of the <strong>Experiment Type</strong> e.g.Clo for Cloning, Lig for Ligation etc.<br />
  The name <strong>PCR demo 1</strong> is the name suggested for the first PCR Experiment recorded by the user called &quot;demo&quot;.<br />
  An <strong>Output</strong> Sample is usually given the same name as the Experiment that made it.<br /><br />
  You can also use a name to help you track your work. For example if you work with isotope-labelled Samples
  you can include this in the Sample name.
  <ul>
    <li>Navigate to the view of the relevant Output Sample from an Experiment</li>
    <li>Rename the Sample by adding an appropriate abbreviation e.g. .N15 or .C13</li>
    <li>Locate the box labelled <strong>Use Sample as input in a New Experiment:</strong> and click the appropriate <input type="submit" value="New Experiment" /> button.</li>
    <li>The same abbreviation will be included in the name of the New Experiment and of any Output Samples<br /><br />
    &nbsp; <strong>note:</strong> when you use this method to record a series of Experiments, PiMS adds and updates the Experiment Type abbreviation at the end of the name.<br />
    e.g. Experiment 1 is a PCR Experiment called &quot;Name.C13-PCR&quot; if Experiment 2 is a Cloning Experiment it will be called &quot;Name.C13-Clo&quot;</li>
  </ul>
 <div class="toplink"><a href="#">Back to top</a></div>
 

 


 </div> <!--end div class="helppage"-->

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
