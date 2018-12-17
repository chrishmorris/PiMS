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
<c:set var="title" value="Help for Experiments"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
<p style="padding-left:1.4em; ">PiMS Experiment Management allows you to record the details for Experiments, both
&#39;Simple&#39; or individual Experiments and also Experiments in Plates or in groups.<br />
PiMS experiments use a &#39;Protocol&#39; or &#39;experiment template&#39; to define the Input
Samples, Output Samples and other experimental details you might need to record.
<!-- TODO need link to Protocol help and specific sections throughout -->
You can also associate files (images and data) with the Experiment record, update
the details and search for Experiment records.<br /><br />
A set of PiMS Protocols are provided for you to use and edit.
If you wish to create a new Protocol you can do so by selecting &#39;New Protocol&#39; from the Experiment menu.</p>
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
<div class="leftcolumn">
 <ul>
  <li><a href="http://pims.structuralbiology.eu/docs/videos/experiment.wmv">This Help as a video</a></li>
  <li><a href="${pageContext.request.contextPath}/help/experiment/NewExperiment.jsp">Record an experiment</a></li>
  <li><a href="#searchExp">Search</a> for an Experiment</li>
  <li><a href="#deleteExp">Delete</a> an Experiment</li>
  <li><a href="#graph">Graph</a> view</li>
 </ul>
</div>
<div class="rightcolumn">
 <a href="#viewExp">View and update</a> Experiment details:
   <ul>
    <li><a href="#expDetails">Details</a></li>
    <li><a href="#expParams">Parameters</a></li>
    <li><a href="#samples">Input and Output Samples</a></li>
    <li><a href="#fileUpload">Associating images and attachments</a></li>
    <li><a href="#notesUpload">Associating notes</a></li>
   </ul>
  
</div>
</pimsWidget:box>
<pimsWidget:box  initialState="closed" title="Related Help">
    <div class="leftcolumn">
        <ul>
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
<div class="toplink"><a href="#">Back to top</a></div>

 
 <h3 id="searchExp">Search for an Experiment</h3>
 
 <div class="textNoFloat">
 To Search for a Single Experiment in PiMS, Mouse over &#39;Experiment&#39; in the menu bar
 and select &#39;Search Single Experiments&#39;.<br />
 <ul>
 <li>Quick search - searches for your search terms, in all fields</li>
 <li>Advanced search - lets you set your search criteria in more detail</li>
 
  <li>Enter a search term(s) in the appropriate box(es) then click the
 <input class="button" value="Search" type="submit" /> button.</li>
 <li>A list of &quot;Experiments&quot; which  match your search criteria will be
 displayed at the bottom of the form.</li>
 <li>If you do not enter any search terms, the details of <em>all</em> Experiments
 recorded in PIMS will be displayed.</li>
 <li>Matching Experiments are displayed in a table with one Experiment per row.
 The column names correspond to the data input fields used when you created the
 Experiment (see <a href="#recordSimpleExp">Recording</a> a new simple experiment)</li>
 
  <li>You can view all of the details recorded for a matching Experiment by clicking on
  the link in the appropriate row.
  </li>
  <li>Below the results, there is a chart showing some statistics about the experiments you have selected.</li>
  </ul>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 
 
 <h3 id="viewExp">View and update Experiment details</h3>
 <div class="textNoFloat">
 
 The information is displayed in boxes:<br />
 &nbsp; <strong>&#39;Basic Details&#39;, &#39;Setup and Results&#39;, &#39;Samples&#39;,
 &#39;Images&#39;,&#39;Attachments&#39;</strong> and <strong>&#39;Notes&#39;</strong>.<br /><br />

 <h4 id="expDetails">Basic Details box</h4>
  The basic details box is used to record the standard information for an experiment.<br />
  The following information can be recorded in the basic details box:

  <p><strong>Name:</strong> An experiment has a name, which must be different
  from the name of any other experiment. PiMS will suggest a name but you can change
  this <em> -see <a href="#expNames">Experiment names</a></em>.</p>

  <p><strong>Type:</strong> This is the Experiment type which was selected when the
  experiment record was being created.<br />
  <em>&nbsp; -in the example, this is PCR</em></p>

  <p><strong>Status:</strong> You can set the Experiment status from a list of
  &#39;To be run&#39;, &#39;In process&#39;, &#39;OK&#39; or &#39;Failed&#39; from
  the drop-down list.<br />
  The default value is &#39;To be run&#39;.<br />
  <em>&nbsp; -in the example, the status has been set to &#39;To be run&#39;</em></p>

  <p><strong>Protocol:</strong> This is the PiMS protocol which was selected when
  the  experiment record was being created.<br />
  &nbsp; Click the protocol name for more details.</p>

  <p><strong>Start and End date:</strong> An experiment has &#39;Start&#39; and
  &#39;End&#39; dates.<br />
  By default, these are set to the current date.
  A different date can be recorded either by editing the default value, or by
  clicking the calendar
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/editdate.gif" style="cursor: pointer; border: 1px solid red;" id="buttonstartDate" title="Click for calendar" alt="Calendar icon" />
  icon and selecting a new date.</p>

  <p><strong>Project:</strong> You can select the Construct or Target to
  which the experiment relates.

  <p><strong>Scientist:</strong> You can select the Scientist who is responsible for
  the Experiment. <em>By default this is the name of user
  who is currently logged in.</em><br />
  &nbsp;<strong>note:</strong> you may need to ask your administrator to create a new &#39;Person&#39;
  record if the one you are looking for is not in the list.<br />

  <p><strong>Details:</strong> You can record any extra information in the
  &#39;Details&#39; field.<br />
  &nbsp;<strong>note:</strong> if you habitually enter the same details here, it
  would be better to use an experiment Parameter.<br />
  &nbsp; (see <a href="#expParams">Parameters</a>)</p>

You can change these values by selecting Make Changes, and
when you have finished, click on the
<input class="button" value="Save Changes" type="submit" /> button. <br />
The updated Experiment details tab will be re-displayed.

<h4 id="expParams">Unlocking an experiment</h4>
<p>When you set the status of an experiment to either "OK" or "Failed", 
the record of the experiment is locked. You cannot update a locked records.
Your PiMS administrator can unlock the experiment,
and if appropriate can also give permission to unlock experiments to certain users.</p>

<h4 id="expParams">Setup and Results</h4>
  <p>The &quot;Parameters&quot; of a PiMS experiment are the details
  you intend to record for a specific experiment.<br />
  Examples might include possible options for the set up of an experiment, such as
  the &#39;number of cycles&#39; to perform for a PCR experiment or details which
  describe an experimental observation, such as the &#39;number of colonies&#39;
  which grew.</p>
  <p>Parameters are described in a PiMS protocol (experiment template) as &#39;Parameter
  definitions&#39;.  These make it possible to record the same information in a
  consistent way for all experiments which use that protocol.<br />
  You may create any number of Parameter definitions for a protocol in PiMS.
  </p>
  The example shows the &#39;Parameters&#39; box for an experiment using the default
  PiMS PCR protocol.<br />
  The names of the Parameter definitions are on the left and the Parameter values,
  which relate to an individual experiment, are on the right and can be updated.
  <br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/parameters.png" alt="New Experiment parameters tab" />
  <br /><br />
  <ul>
   <li>To change the Parameter value, edit the text in the appropriate box, then click &#39;Save Changes&#39;</li>
  </ul><br />

<h4 id="samples">Samples</h4>
  The example shows the &#39;Samples&#39; box for an experiment record created
  using the default PiMS PCR protocol.<br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/samples.png" alt="New Experiment samples" />
  <br /><br />

  <p>The Input samples table contains a list of reagents and also samples created in
  other experiments.<br /> You can specify the actual samples used.</p>


   <ul>
    <li>The Input sample information is displayed in 4 columns.  Mouse over the column
    heading for a description.<br />
    &nbsp; <strong>Role:</strong> the role the sample plays in the experiment e.g. &#39;Buffer&#39;.<br />
    &nbsp; <strong>Amount:</strong> the amount (e.g. volume) of the sample. <br />
    &nbsp; <strong>Sample:</strong> the record for the actual sample used, you may
    select this from a drop-down list.<br /><br />
    &nbsp; <strong>note:</strong> if you are not interested in recording the
    particular batch of a sample used, but only want to record the type,<br />
    &nbsp; you may prefer to create a Parameter definition for this in your protocol,
    rather than an Input sample.
    </li>
   </ul>
  <br />
  <p>An <strong>output sample</strong> is the product of an experiment.<br />
  Most experiments produce one output sample. Exceptions are QA experiments, which
  produce only information, and some experiments which have more than one product.</p>
  <p>Records of the output samples for an Experiment are created for you automatically
  when you create the record of the experiment.
  You rarely need to alter them.</p>

   <ul>
    <li>The Output sample information is displayed in 3 columns.  Mouse over the
    column heading for a description.<br />
    &nbsp; <strong>Role:</strong> the role the sample plays in the experiment. If
    there is only one output sample, this is likely to say &#39;Product&#39;.<br />
    &nbsp; <strong>Amount:</strong> the amount (e.g. volume) of the sample. <br />
    &nbsp; <strong>Sample:</strong> a clickable link to more information on the
    output sample.<br />
    &nbsp; Click this link to use the Output sample as an Input sample for another
    Experiment<br />
    &nbsp; <em>see -<a href="#nextExp">Linking</a> Experiments for more details.</em>
    </li>
   </ul>

<h4 id="fileUpload">Images box -Associating images with an Experiment</h4>
  You can associate files: images, spreadsheets, etc. with the experiment.
  <br />
  An example of the &#39;Images&#39; box for a new Experiment is shown below.<br />
  &nbsp; <strong>note:</strong> there is currently just one images associated with this
  Experiment.<br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/images.png" alt="New Experiment Files tab" />
  <br /><br />
  <ul>
   <li>Enter a &#39;Title&#39; and a &#39;Legend&#39; for the image.</li>
   <li>Use the <input class="button" value="Browse" type="submit" /> button to
   navigate to the image file.</li>
   <li>Then click <input class="button" value="Upload" type="submit" /></li>
   <li>The re-displayed page will include the &#39;Title&#39; and the &#39;Legend&#39; which you
   entered, and a thumbnail image.  You can click on the thumbnail to display the full image.<br /><br />
   &nbsp;<strong>note:</strong> there will also be a delete icon, to remove the
   image, if you have permission to do so.</li>
 </ul>

<h4 id="notesUpload">Notes box - Writing notes for an Experiment</h4>
  You can write notes to be associated with the experiment.
  In addition, a note is created automatically when an experiment is unlocked.
  <br />
  An example of the &#39;Notes&#39; box for a new Experiment is shown below.<br />
  &nbsp; <strong>note:</strong> there is currently no notes associated with this
  Experiment.<br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/attachmentsandnotes.png" alt="Experiment with Files tab" />
   <br /><br />
  <ul>
   <li>Enter your note in the &#39;Details&#39; text area.</li>
   <li>Then click <input class="button" value="Add" type="submit" /></li>
   <li>The re-displayed page will include the &#39;Title&#39; and the &#39;Legend&#39; which you
   entered, and a thumbnail image.  You can click on the thumbnail to display the full image.</li>
 </ul>

 

 </div> <!--end div class="textNoFloat"-->
<div class="toplink"><a href="#">Back to top</a></div>



 <h3 id="deleteExp">Delete an Experiment</h3>
 
 <div class="textNoFloat">
  To delete an experiment record from PIMS, first search for it.<br />
  &nbsp; -see <a href="#searchExp">Search</a> for an Experiment.<br />
  Experiments which match your search criteria are displayed in a table with 1 experiment
  record per row.<br /><br />
  <ul>
   <li>If you have permission to delete the record for a particular Experiment,
   the first column in the relevant row will contains a delete icon:
   <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete icon"/></li>
   <li>Click the icon and you will see a warning, requesting confirmation.</li>
   <li>Click <input class="button" value="OK" type="submit" /> to continue and
   you will see a page confirming that the selected record has been deleted.</li>
  </ul>
   &nbsp; <strong>note:</strong>  If you do not have permission to delete the
   Experiment record, the delete icon will be grayed out,
  <img class="button" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" alt="Can't delete this Experiment" title="Can't delete this Experiment" />
   <br /> &nbsp; and cannot be clicked.
  <br />&nbsp; Your PiMS administrator will be able to delete it for you.

 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="graph">Graph View</h3>
 
 <div class="textNoFloat">
 PiMS provides a visual representation of a series of linked Experiments.  This can
 be accessed from the &#39;Diagram&#39; link on a Sample view page.
 &nbsp; -see <a href="#nextExp">Linking</a> experiments.
<br /><br />
 An example is shown below.<br />
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/graph.jpg" alt="Linked experiments graph" /><br /><br />
 <ul>
  <li>Each experiment is represented as an ellipse displaying the Experiment type.<br />
  <em> -these are &#39;Transformation&#39; and &#39;Culture&#39; in the example</em></li>
  <li>Each Sample, which links the experiments, is represented by a rhombus displaying
  the sample name.<br />
  &nbsp; the Sample from which the graph was obtained is represented by an unfilled
  triangle with a red border<br />
  <em> -this is &#39;Transformation78204 Cells&#39; in the example</em><br />
  &nbsp; all other Samples are represented by a blue filled triangle
  <em> e.g. &#39;Culture78214 Culture&#39; in the example</em></li>
  <li>Arrows are used to indicate the order of the experiments in the series.</li>
  <li>A tooltip containing details about the sample or experiment can be seen
  if you mouse-over the image.</li>
  <li>To see a menu with actions for a sample or experiment click on the appropriate shape.</li>
 </ul>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 </div> <!--end div class="helppage"-->

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
