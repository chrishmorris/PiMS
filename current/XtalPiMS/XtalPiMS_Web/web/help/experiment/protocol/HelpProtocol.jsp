<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Creating a Protocol' />
</jsp:include>

<c:catch var="error">

<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="protocol.png" />
<c:set var="title" value="Help for Protocols"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
<p>In PiMS a Protocol is a template which allows you to define the details you would
like to record for your Experiments.  PiMS provides a set of default Protocols but
you can also create your own to match your usual laboratory practice.<br /><br />
A PiMS Protocol allows you to link experiments together using the output from one
experiment as the input to the next.  To do this you need to describe Input and Output
samples in your Protocol.</p>
<p>Additional experimental details, which you would like to record, are described by
"Parameters" in your Protocol.  These can be used to record numeric,
text and true/false values in your Experiments. Samples or reagents which you do <strong>not</strong>
intend to track in your Experiments should also be described by Parameters.</p>
<p>Any number of Samples and Parameters can be created in a Protocol.</p>
<p>A PiMS Protocol can be used to record both simple and Plate experiments and you can
upload experimental details, from a suitably formatted spreadsheet, for a Plate experiment.
 -see also
<a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#uploadFromSpreadsheet">Plate Experiment</a> help.</p>
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
 <div class="leftcolumn">
  <ul>
  
  <li><a href="http://pims.structuralbiology.eu/docs/videos/protocol.wmv">This Help as a video</a></li>
  
  <li><a href="#viewprotocol">Viewing a protocol</a></li>
  <li><ul>
    <li><a href="#copyprotocol">Copying a protocol</a></li>
    <li><a href="#exportprotocol">Export a protocol</a></li>
    <li><a href="#deleteprotocol">Deleting a protocol</a></li>
  </ul></li>
  
  <li><a href="#createprotocol">Recording protocol</a></li>
  <li><ul>
    <li><a href="#createprotocol">Create new protocol</a></li>
    <li><a href="#uploadprotocol">Upload protocol</a></li>
    <li><a href="#copyexistprotocol">Copy exising protocol</a></li>
  </ul></li>

  <li><a href="#parameters">Parameters</a></li>
  <li><ul>
    <li><a href="#createparameter">Adding a parameter</a></li>
    <li><a href="#editparameter">Editing a parameter</a></li>
    <li><a href="#deleteparameter">Deleting a parameter</a></li>
    <li><a href="#scoreparameter">Score Parameters</a></li>
  </ul></li>
  </ul>
 </div>
 <div class="rightcolumn">
  <ul> 
  <li><a href="#inputsamples">Input samples</a></li>
  <li><ul>
    <li><a href="#createinput">Adding an input sample</a></li>
    <li><a href="#editinput">Editing an input sample</a></li>
    <li><a href="#deleteinput">Deleting an input sample</a></li>
  </ul></li>
  <li><a href="#outputsamples">Output samples</a><br /><br /></li>
  <li><a href="#useprotocol">Using PiMS protocols</a><br /><br /></li>
  <li><a href="#formatSpreadsheet">Using spreadsheets for Plate experiments</a></li>
  <li><a href="http://pims.structuralbiology.eu/docs/videos/NMR.wmv">Help video for NMR protocols</a></li>
  <li><a href="http://pims.structuralbiology.eu/docs/videos/workflow.wmv">Help video for workflows</a></li>
  </ul>
 </div>
 </pimsWidget:box>
 <pimsWidget:box initialState="closed" title="Related Help">
 <a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp">Experiment management</a><br />
 </pimsWidget:box>
</div> <!--end div help-->

<div class="helpcontents">
 <h3 id="viewprotocol">Viewing a protocol</h3>
 <div class="textNoFloat">
  <p>This is the standard view of a PiMS protocol:</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/protocolAfterCreate.png" alt="View new Protocol" />
  <p>The basic details of the protocol are in the first box; this is open when you first arrive at the page. To edit these basic
  details, enter the information you wish to record and click "Save" at the bottom right of the page.</p>
  <p>"Method" is where you would record a text description of the way you carry out experiments using this protocol. You may wish
  to paste this information in from an existing document. Click "Save" after editing the method.</p>
  <p><a href="#inputsamples">Inputs</a>, <a href="#parameters">Parameters</a>, <a href="#outputsamples">Output samples</a> and <a href="#">Files</a> are discussed in detail below.</p>
  <p>Note that you cannot edit a protocol which has already been used for an experiment. You can, however, copy an existing one and
  make changes to that copy.</p>
  <h4 id="copyprotocol">Copying a protocol</h4>
  <p>To copy a protocol, click on the "Copy Protocol" button in the Details box. A pop-up will appear, asking whether you wish to do this.
  Click "OK". You will see the View of your new protocol.</p>
  <p>If your original protocol was called "Demo protocol", your new one will be called "Demo protocol copy 1". You can, of course, edit
  that name to something more meaningful to you.</p>
  
  <h4 id="exportprotocol">Exporting a protocol</h4>
  <p>To export a protocol, click on the "Export" link just under the title. A XML formate of this protocol will be displayed and you can save it for modify and import.</p>

  <h4 id="deleteprotocol">Deleting a protocol</h4>
  <p>To delete the protocol, click on the "Delete" link just under the title. If you don't have permission to delete it, this link will
  read "Can't delete" and be greyed out.</p>
  <p>Note that you cannot delete a protocol which has been used in an experiment, regardless of your user permissions.</p>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

 <div class="textNoFloat">
 <h3 id="createprotocol">Recording a new PiMS protocol</h3>
 <p>Mouse over the Experiment menu and click "New Protocol". You will see a form for recording the basic details for a new PiMS Protocol.</p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/createProtocol.png" alt="Create protocol form" />
 <ul>
  <li>You need to provide a name for your new protocol; this must not be the same as any existing protocol.</li>
  <li>You must specify the "Type" of Experiment your protocol is to describe; select the right one from the drop-down list.
  To create a record for a "new" Experiment type, you will need to contact your PiMS administrator.</li>
 </ul>
  <p>When you have completed the details click the "Next>>>" button.</p>
  <p><strong>note:</strong> click the "Delete" button case you want to remove this protocol.</p>
  <p>You will see the standard PiMS view page for your Protocol.</p>
  
 <h3 id="uploadprotocol">Upload a new PiMS protocol</h3>
 <p>In above new protocol screen, you can upload a protocol in xml formate. Firstly, click "Browse" buton to find your local protocol xml file. Then, upload it by click the "Upload file" button.</p>
 <p><strong>note:</strong> If your protocol xml file was created by export, please change the protocol's name to avoid the duplicated name error.</p>
 <h3 id="copyexistprotocol">Copy exising protocol</h3>
 <p>If you want to create new protocol based on exising one, input the name of exising protocol in the "Search for protocols
 " field and click "Search" button. Then follow <a href="#copyprotocol">Copy exising protocol</a>.</p>

 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="parameters">Parameter definitions</h3>
 <div class="textNoFloat">
 <p>Protocol parameters are used to describe any values you wish to record in Experiments
 based on this protocol.</p>
 <p>These can be numerical values, such as an incubation temperature or the number of cycles
 in a PCR Experiment.</p>
 <p>A parameter can also describe a free-text value, such as details
 of incubation conditions used in an Experiment.</p>
 <p>You can also use a parameter to describe a "true/false" or "yes/no" value e.g.
 "was this reagent added?"</p>
 <h4 id="createparameter">Adding a parameter</h4>
 <p>To add a new parameter to your protocol, scroll down to the Parameters box. There are three links - "Number", "Text", and "Yes/No":</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/createParameterLinks.png" alt="Links to create a parameter" />
 <p>Click on the one you need. You will be taken to a page where you can enter the details of your new parameter. </p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/parameterForm.png" alt="Form to create a parameter" />
 <p>(This example is for a Number parameter. The form varies slightly for the others.)</p>
 <p>Each parameter, input and output must have a name that is unique within the protocol. The top box lists all of them, to help you avoid
 duplicating names.</p>
 <p>You must give the parameter a name, for example, "Number of cycles" or "Annealing temperature". Additionally, you can enter a short
 description of what the parameter represents.</p>
 <p>For a number parameter, you can also specify:</p>
 <ul>
    <li>whether the number is restricted to integers (whole numbers);</li>
    <li>a minimum value;</li>
    <li>a maximum value;</li>
    <li>a default value, which will be recorded for each experiment unless the scientist changes it; or</li>
    <li>any number of "possible values"; these will be presented to the scientist as a drop-down list.</li>
 </ul>
 <p>Text parameters allow you to specify:</p>
 <ul>
    <li>a default value, which will be recorded for each experiment unless the scientist changes it;</li>
    <li>any number of "possible values"; these will be presented to the scientist as a drop-down list.</li>
 </ul>
 <p>For yes/no parameters, you can specify whether the default answer is yes or no.</p>
 <p>For all parameter types, there are two further questions:</p>
 <ul>
    <li><strong>Must a value always be entered for this parameter?</strong> If you answer Yes, the scientist is required to provide a value
    when entering experiments, even if that is only accepting any default value; they cannot leave it blank.</li>
    <li><strong>Is this a result parameter?</strong> If the parameter relates to the set-up of the experiment, <em>e.g.,</em> Number of
    PCR cycles or Annealing temperature, answer No. Answer Yes if the parameter relates to information recorded as a result of the experiment,
    for example OD, number of colonies.</li>
 </ul>
 <p>Finally, click "Save" to create your new parameter. You will be returned to the view of the protocol, and your new parameter will be shown
 in the Parameters box:</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/parameterAfterCreate.png" alt="View of a parameter in a protocol" />
  <h4 id="editparameter">Editing a parameter</h4>
  <p>On the protocol view page, scroll down to and open the Parameters box. The details of each parameter are shown, as above.</p>
  <p>Make the necessary changes to the parameters. Help on the form fields can be found above in "Creating a parameter".</p>
  <p>Lastly, scroll to the bottom of the page and click Save.</p>
  <p>Remember that you can't change any part of a protocol that has been used in experiments; if this protocol has been used, you won't be able
  to edit the parameter. You would need to make a copy of the protocol, edit that copy, and use it in subsequent experiments.</p>
  <h4 id="deleteparameter">Deleting a parameter</h4>
  <p>On the protocol view page, scroll down to and open the Parameters box. The details of each parameter are shown, as above.</p>
  <p>To delete a parameter, click the green dustbin <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete" /> icon
  beside the parameter name.</p>
  <p>PiMS will ask you to confirm that you really want to delete the parameter. Click "OK" to go ahead with the deletion; if you aren't sure,
  click "Cancel".</p>
  <p>If you click "OK", the dustbin is replaced with a spinning <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" alt="Waiting" />
  icon. After a short delay, the parameter details will flash yellow and disappear. The parameter has been deleted.</p>
  <h4 id="scoreparameter">Score Parameters</h4>
  <p>PiMS supports scoring of experiments, by means of specially-named protocol parameters.  <em> -see <a href="${pageContext.request['contextPath']}/help/experiment/plate/HelpCreatePlateExperiment.jsp#scoreParams">Plate Experiment</a> help</em><br />
   It also associates colours and icons with those scores, making it very easy to get an overview of a plate experiment's success (or otherwise).</p>
   
  <p>Score parameters are recorded in a Protocol in a specific way:</p>
   <ul>
    <li>a name beginning with two underscore characters e.g. __SCORE</li>
    <li>the value you wish to display in the Plate experiment e.g. Correct size?</li>
    <li>a default value of &quot;Unscored&quot;</li> 
    <li>a list of possible values from worst to best e.g. Unscored;No;Maybe;Yes;</li>
   </ul>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

<h3 id="inputsamples">Input Samples</h3>
 <div class="textNoFloat">
 <p>The Input Samples in a PiMS Protocol describe any samples or reagents you wish to  track in your Experiments.  For example,
 you can define an Input sample which is  the Output sample from another experiment and so link experiments together.  This
 approach could enable you to follow the progress of a Construct through a series of  experiments e.g. from PCR to protein
 expression and purification.</p>
 <p>You can also define an Input sample for purposes of tracking its use.  For example, to track the use of a particular batch
 or aliquot of a reagent.  This might be useful for tracing the source of experiment failures.</p>
 <p>Any samples or reagents which you do not intend to track in this way should be described by a <a href="#defineParameters">parameter</a>.</p>
 <h4 id="createinput">Adding an input sample</h4>
 <p>To add a new input sample to your protocol, scroll down to the Inputs box. Click the link labelled "Create new". You will be taken
 to a page where you can enter the details of your new input sample. </p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/inputSampleForm.png" alt="Form to create an input sample" />
 <p>Each parameter, input and output must have a name that is unique within the protocol. The top box lists all of them, to help you avoid
 duplicating names.</p>
 <p>You must give your new input sample a name, for example, "Forward primer".</p>
 <p>Samples in PiMS are grouped into "sample categories". Choose an appropriate category for your input sample. When users record an experiment
 using this protocol, they will only be able to specify a sample in this category.</p>
 <p>Finally, click "Save" to create your new input. You will be returned to the view of the protocol, and your new input will be shown
 in the Inputs box:</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/inputSampleAfterCreate.png" alt="View of an input sample in a protocol" />
  <p>From here, you can specify a default amount as described below.</p>
  <h4 id="editinput">Editing an input sample</h4>
  <p>On the protocol view page, scroll down to and open the Inputs box. The details of each sample are shown, as above.</p>
  <p>Make the necessary changes to the input sample.</p>
  <p>You can specify a default amount - PiMS will assume that this much is added to each experiment using this protocol, unless the scientist
  enters a different amount. Enter a number in the box and select an appropriate unit from the drop-down list.</p>
  <p>Help on the "name" and "Sample category" fields can be found above in "Creating an input sample".</p>
  <p>Lastly, scroll to the bottom of the page and click "Save".</p>
  <p>Remember that you can't change any part of a protocol that has been used in experiments; if this protocol has been used, you won't be able
  to edit the sample. You would need to make a copy of the protocol, edit that copy, and use it in subsequent experiments.</p>
  <h4 id="deleteinput">Deleting an input sample</h4>
  <p>On the protocol view page, scroll down to and open the Inputs box. The details of each sample are shown, as above.</p>
  <p>To delete an input sample, click the green dustbin <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete" /> icon
  beside the parameter name.</p>
  <p>PiMS will ask you to confirm that you really want to delete the input sample. Click "OK" to go ahead with the deletion; if you aren't sure,
  click "Cancel".</p>
  <p>If you click "OK", the dustbin is replaced with a spinning <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" alt="Waiting" />
  icon. After a short delay, the sample details will flash yellow and disappear. The input sample has been deleted.</p>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

<h3 id="outputsamples">Output Samples</h3>
 <div class="textNoFloat">
 <p>The product of your experiments is known in PiMS as an "output sample". You can <a href="#createinput">create</a>, <a href="#editinput">edit</a>,
 and <a href="#deleteinput">delete</a> output samples in the same way as for <a href="#inputsamples">input samples</a>.</p>
 </div> <!--end div class="textNoFloat"-->
 <div class="toplink"><a href="#">Back to top</a></div>

<h3 id="useprotocol">Using PiMS protocols</h3>
 <div class="textNoFloat">
 <p>A PiMS Protocol is used as a template which allows you to define the details you would
 like to record for your Experiments.  When you create a new Experiment record, you can
 select which Protocol to use as the template - see <a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Recording a new PiMS
 Experiment</a>. The details of any Parameters, Input and Output samples, which are specified
 in the Protocol, will then be displayed in the the Experiment record for you to update - see <a
 href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#viewExp">View Experiment details</a>.</p>
 <p>The example shows the view of an Experiment record created
 using a PiMS protocol named "PiMS PCR", with the <strong>Basic details</strong> box opened:</p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptView.png" alt="New experiment Details" />
 <ul>
 <li>To view details of the Protocol click the link displaying the protocol name:
 &nbsp; <span class="spotLink"><a href="javascript:void(0)">PiMS PCR</a></span> in this case. You can also view more general details of
 the type of experiment.</li>
 <li>The experiment name "PCR226213" is generated automatically based on the Experiment type but this can be changed.</li>
 <li>Where a Target or Construct is associated with the experiment, you can view it by clicking on the appropriate link. (In
 this case, there is none.)</li>
 <li>You can also View the details for the Scientist responsible for this experiment by clicking their name - "demo" in this case.</li>
 </ul>
 <p>To change any of the details in this box, click on the "Make changes..." link in the bottom right. The form fields are now editable:</p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptDetailsEdit.png" alt="Edit experiment Details" />
 <p>You can now:</p>
 <ul>
 <li>Change the name, start date and end date of the experiment;</li>
 <li>Update the status of the experiment;</li>
 <li>Associate a Target or Construct with the experiment, or remove an existing association;</li>
 <li>Change the lead scientist; and</li>
 <li>Record some details about the experiment.</li>
 </ul>
 <p>When you have made the changes you need, click "Save changes". If, instead, you wish to abandon your changes, click "Cancel editing".</p>
 <div class="toplink"><a href="#">Back to top</a></div>
 <h4 id="exptparameters">Conditions and Results</h4>
  <p>The Conditions and Results box is used to display and update values corresponding to any
    parameters you specified in your Protocol - see <a href="#parameters">Parameters</a>.</p>
<p>The example shows the Conditions and Results box for an Experiment record created using the Protocol
    named "PiMS PCR":</p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptParametersView.png" alt="View experiment parameters" />
 <p>When the experiment is first created, these values will be the defaults specified in the protocol. If there is no default value
 for a given parameter, it will be blank.</p>
 <p>To change the values recorded, click "Make Changes..."; the form fields are now editable:</p>
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptParametersEdit.png" alt="Edit experiment parameters" />
 <p>If a parameter was defined with a limited number of possible values, it will be shown here as a drop-down list instead of a text box.
 A red asterisk next to the parameter name indicates that a value must be entered for it.</p>
 <p>When you have made the changes you need, click "Save changes". If, instead, you wish to abandon your changes, click "Cancel editing".</p>
 <div class="toplink"><a href="#">Back to top</a></div>

<h4 id="exptsamples">Samples</h4>
<p>The Samples box is used to display and update values corresponding to any Input and Output samples you specified in your Protocol.</p>
<!-- TODO See PIMS-716 problem with recording amounts -->
<p>The example shows the Input Samples tab for an Experiment record created using the Protocol
named "PiMS PCR":</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptSamplesView.png" alt="View of samples" />
<p>In each case, the name of the sample is the name you gave to the input or output samples in the protocol. If you specified a default amount,
that amount is set for you - otherwise it will be zero.</p><p>The actual input samples used in the experiment are set to None, except where
 you created this experiment using the output of a previous one.</p>
 <p>The output is created and named automatically.</p>
<p>To enter details of either input or output samples, click the "Make changes..." link under the relevant table:</p>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/exptSamplesEdit.png" alt="Editing samples" />
  <p>Note that you must edit input and output samples separately; you cannot make changes to both input and output samples together.</p>
  <p>For input samples, you can select from recently-used samples of the right category, or search for more.</p>
  <p>For output samples, you can change the name to something more meaningful to you.</p>

 <div class="toplink"><a href="#">Back to top</a></div>

<h3 id="formatSpreadsheet">Using spreadsheets for Plate experiments</h3>
 <div class="textNoFloat">
 A Plate Experiment in PiMS represents the individual records for the wells of a
 multiwell plate.  Rather than create multiple individual Experiment records, you can
 upload the experimental details from a suitably formatted spreadsheet.<br />
 To format a spreadsheet for this, the column headings of the spreadsheet need to
 match certain details of the PiMS Protocol which you intend to use.<br /><br />
For more information on creating spreadsheets for uploading experimental details for a Plate Experiment see:<br /> 
<a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#uploadFromSpreadsheet">Uploading</a> data from spreadsheets.<br />
Also see <a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp">Plate Experiment Help</a>.<br /><br />
  </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
</div>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
