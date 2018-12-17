<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Plate Experiments' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
 : <a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp">Experiment Help</a>
  
</c:set>
<c:set var="icon" value="plate.png" />
<c:set var="title" value="Plate Experiment Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <ul>
     <li>PiMS allows you to record the details for Experiments in Plates, up to 4 Plates can be recorded as a group.</li>
     <li>Plate experiments use a 'protocol' or 'template' to define the input samples, output samples 
     and other experimental details you might need to record.</li>
    <li>Each well in the Plate is recorded as a Single experiment in PiMS but the details 
        for the wells can be updated as a Plate, as a subset of wells in the Plate or as individual wells.</li>
    <li>You can also associate files (images and data) with the Plate experiment as a whole or with the 
        individual well experiments.</li>
    <li>You can create a Plate experiment by uploading the details from a spreadsheet and 
        export the Plate experiment to a spreadsheet for use in other applications.</li>
    </ul>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li><a href="#recordPlateExp">Recording a new Plate Experiment</a></li>
          <ul>
            <li><a href="#details">Plate details</a></li>
            <li><a href="#numPlates">Recording multiple Plates</a></li>
            <li><a href="#useSpreadsheet">Using a spreadsheet</a></li>
          </ul>
        <li><a href="#plateView">Plate Experiment View</a></li>
        <li><a href="#basicdetails">Basic details</a> tab</li>
        <li><a href="#platesTab">Plates</a> tab</li>
          <ul>
            <li><a href="#selectWells">Selecting</a> a range of wells</li>
            <li><a href="#viewWellDetails">Viewing</a> the values for a well</li>
            <li><a href="#inputSamples">Input samples</a></li>
              <ul>
                <li><a href="#chooseSample">Choosing samples</a></li>
                <li><a href="#advancedFill">Advanced Fill options</a></li>
                <ul>
                	<li><a href="#copyFromPlate">Copy from Plate</a></li>
               		<li><a href="#mergeByScore">Merge by Score</a></li>
              	</ul>
              </ul>
            <li><a href="#parameters">Parameters</a></li>
              <ul>
                <li><a href="#updateParams">Updating parameters</a></li>
                <li><a href="#scoreParams">Score parameters</a></li>
              </ul>
            <li><a href="#status">Status</a></li>
            <li><a href="#outputSamples">Output samples</a></li>
            <li><a href="#experiments">Experiments</a></li>
            <li><a href="#targetConstruct">Targets and Constructs</a></li>
          </ul>
      </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#nextExp">Next Experiment</a> tab</li>
          <ul>
            <li><a href="#nextPlateExp">Create the Next Plate Experiment</a></li>
            <li><a href="#nextSingleExp">Create a Single Experiment</a></li>
          </ul>
        <li><a href="#attachments">Attachments</a> tab</li>
        <li><a href="#spreadsheet">Spreadsheets</a> importing and exporting data</li>
          <ul>
            <li><a href="#uploadFromSpreadsheet">Uploading</a> data from a spreadsheet</li>
            <li><a href="#formatSpreadsheet">Spreadsheet</a> format</li>
            <li><a href="#updateFromSpreadsheet">Updating</a> a Plate Experiment</li>
            <li><a href="#exportToSpreadsheet">Exporting</a> data to a spreadsheet</li>
            <li><a href="#cohort">Progress report</a></li>
            </ul>
        <li><a href="#locatePlate">Finding</a> Plate Experiments</li>        
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp">Protocol</a> Help</li>
      <li><a href="${pageContext.request['contextPath']}/help/experiment/ExperimentGroup.jsp">Experiment group</a> Help</li>
    </ul>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="recordPlateExp">Recording a New Plate Experiment</h3>

 &nbsp; <strong>note:</strong> before you start, please check that you have PiMS records
 for any Samples, Reagents, Targets and Constructs<br />
 &nbsp; which you will refer to in your plate experiment.<br /><br />

 Mouse over &quot;Experiment&quot; in the menu bar, and select
 <a href="javascript:void(0)">New Plate Experiment</a> to see a &#39;New Plate Experiment&#39; form.<br />

  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/plateBasic.png" alt="Create Plate Experiment form" />
  <!-- ................................................................................ -->
  <h4 id="details">Plate details:</h4>
 <ul>
  <li>Choose the <strong>Type</strong> of Experiment you wish to record.<br /><br /></li>
  <li>Choose the PiMS <strong>Protocol</strong> for the experiment
  &nbsp; <em>-this acts like a template, setting the details which you 
  can specify for your experiment.  The list will <strong>only</strong> contain Protocols for the Experiment Type you have selected 
  and <strong>only</strong> Protocols which have a single Output Sample.</em><br /><br /></li>
  <li>PiMS will suggest a <strong>Group name</strong> for the Plate Experiment which is based on the Experiment type you have selected.<br />
  You can change this value.<br />
  If you are recording only one Plate then the Group name is the bar code or label you will use to find it
  later.<br />
  If you are recording two or more Plates then this will be the name for the group which PiMS will use to process all of the plates together. 
  <em> -see <a href="#numPlates">Recording multiple plates</a></em><br /><br /></li>
  <li>Select a <strong>Lab notebook</strong> from the drop-down list <em> -there may only be one Lab notebook in the list.</em><br /><br /></li>
  <li>Select the <strong>Start</strong> and <strong>End</strong> dates.  By default, these
  are set to the current date.<br />
  To select a different date, either edit the default value, or click the calendar
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/editdate.gif" id="buttonstartDate" title="Click for calendar" alt="Calendar icon"/>
  icon and select a new date.<br /><br />
  <br /></li>
  <li>Choose a <strong>Plate type</strong> from the drop down list.<br />
  &nbsp;<strong>note: </strong>If the list does not contain an appropriate type, please speak to your PiMS administrator.<br /><br /></li>
  <li>If you are only recording the details for a single Plate and you are not uploading data from a spreadsheet<br />
  click the 
  <input class="button" value="Create" type="submit" /> button at the end of the form.<br />
  The details of the plate experiment will be displayed.</li>
 </ul>
  <!-- ................................................................................ -->
  <h4 id="numPlates">Recording multiple plates:</h4>
    If you are processing more than one plate simultaneously, PiMS allows you to record up to 4 plates as a group.<br />
    All of the Plates in the group will be recorded using the same Protocol, Plate type etc. as specified in the Basic details section of the form.
    <ul>
     <li>Enter values in the appropriate number of <strong>Plate ID(s)</strong> fields.<br />
      &nbsp; <em> -if you do not enter any Plate ID(s), only one Plate will be recorded</em><br /><br /></li>
     <li>If are not uploading data from a spreadsheet, click the 
     <input class="button" value="Create" type="submit" /> button at the end of the form.<br />
     The details of the plate experiment will be displayed.</li> 
    </ul>
  <!-- ................................................................................ -->
  <h4 id="useSpreadsheet">Using spreadsheets</h4>
 It is also possible to populate the details for a Plate Experiment by uploading a spreadsheet containing the data.<br />
 PiMS can provide a suitably formatted spreadsheet for the Protocol you have selected for your Plate Experiment.<br />
 <ul>
  <li>Enter the details for a New Plate experiment <em>-see <a href="#details">Plate details</a></em><br /><br /></li>
  <li>Click the <input class="button" value="Get Spreadsheet" type="submit" /> button.<br /><br /></li>
  <li>PiMS will create a .csv file with the appropriate number of rows and columns to match the Protocol and plate type you have selected.
   <em>&nbsp; -see <a href="#formatSpreadsheet">Spreadsheet</a> format for more information.</em><br />
      Save a copy of this file then edit it with a spreadsheet application to enter the relevant information for your Plate Experiment.</li>
 </ul>
 To use a spreadsheet to populate the Plate Experiment:
 <ul>
 <li>Click the Browse button to navigate to the spreadsheet on your computer.<br /><br /></li>
 <li>Click the 
  <input class="button" value="Create" type="submit" /> button at the end of the form.<br />
  The details of the plate experiment will be displayed.</li>
 </ul>
 
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="plateView">Plate Experiment View</h3>
  The Plate experiment View is displayed when you click the 
  <input class="button" value="Create" type="submit" /> button on a New Plate Experiment form<br />
  or<br />
  when you click a link to Plate experiment e.g. 
      <pimsWidget:linkWithIcon 
                icon="types/small/plate.gif" 
                url="JavaScript:void(0)" 
                text="Plate Example" />in the results of a search for Plate Experiments.<br /><br />
  The Plate View has 4 tabs: <a href="#basicdetails"><strong>Basic details</strong></a>, 
  <a href="#platesTab"><strong>Plates</strong></a>, 
  <a href="#nextExp"><strong>Next experiment</strong></a> and 
  <a href="#attachments"><strong>Attachments</strong></a>.<br />
  <!-- ================================================================================ -->

  <h3 id="basicdetails">Basic details tab</h3>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/plateBasicView.png" alt="Basic details tab for a Plate Experiment" />
  This tab displays the information entered in the New Plate Experiment form:<br /><br />
  <strong>Group name</strong> is the name or barcode of the Plate experiment if only one plate was recorded or, 
  the name of a group of plates which were recorded simultaneously.
  &nbsp; <em> -see <a href="#numPlates">Recording multiple Plates</a></em><br />

  <strong>Protocol</strong> a link to the PiMS protocol used as a template for the Experiments in the Plate.<br />
  <strong>Lab notebook</strong><br />
  <strong>Plate type</strong> a link to the details recorded for the selected type of Plate.<br />
  <strong>Start date</strong> and <strong>End date</strong> are links to the &quot;Day View&quot; which lists PiMS records recorded on a particular day.<br />
  <strong>Samples active</strong> indicates if the Output Samples for the Plate experiment are available for use as Input Samples for a new Experiment(s).<br />
  <ul>
   <li>To change the Dates or the &quot;Samples active&quot; status, click <strong><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt=""/> Make changes...</strong><br /><br /></li>
   <li>To set or update the Plate Location, click <a href="JavaScript:void(0)"><strong>Move Plate</strong></a> in the list of actions below the Plate View tabs.<br />
   This will direct you to the screen for defining a hierarchical location for the Plate(s) e.g. -80&deg;C Freezer &gt; Shelf2</li>
  </ul>
  <strong>There are also a links to:</strong><br />  
        <pimsWidget:linkWithIcon 
                icon="actions/delete.gif" 
                url="JavaScript:void(0)" 
                text="Delete" />the Plate Experiment, if you have permission to do so.<br /><br />
                View a  
        <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)" 
                text="Diagram" />for the Plate, which will show links to the next and previous Plate Experiments in a series.<br /><br />
    Create a <a href="JavaScript:void(0)">Progress report</a> as a spreadsheet  
                <em>-see <a href="#cohort">Progress report</a></em>.<br /><br />
    <a href="JavaScript:void(0)">Export this group to spreadsheet (.csv)</a> <em>-see <a href="#exportToSpreadsheet">Exporting</a> data to a spreadsheet</em><br /><br />
    You can also update the Plate Experiment with data in a spreadsheet <em>-see <a href="#updateFromSpreadsheet">Updating</a> a Plate Experiment</em>                

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="platesTab">Plates tab</h3>
  This tab has two areas:<br /><br />
  The left hand side contains a series of horizontal sections:<br />
  <strong>Input samples</strong>, <strong>Parameters</strong>, <strong>Status</strong>, <strong>Output samples</strong>, <strong>Experiments</strong>
  and <strong>Targets and constructs</strong> which can be opened by clicking on the section name.<br /><br />
  The <strong>Input samples</strong> section is open by default, as in the example.<br /><br />
  To the right is a representation of the Plate (or Plates if this is a Plate Experiment group). Any selected wells are highlighted in blue.<br /> 
  The default is for all of the wells to be selected. <em> -in the example, well 'A01' is selected</em>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTab.png" alt="Plates tab for a Plate Experiment" />

  <!-- ................................................................................ -->
  <h4 id="selectWells">Selecting a range of wells</h4>
   <ul>
     <li>To select a single well, click on it. It will be highlighted in blue.<br /><br /></li>
     <li>To select a rectangular block of wells, put the mouse cursor over one well,
     hold down the left mouse button, and move the cursor to another well.<br />
     You will see the selection of blue wells change as the cursor moves across the plate.<br />
     Release the mouse button when you have selected the correct range of wells.<br /><br /></li>
     <li>To select any pattern of wells, hold down Shift key and click individual well(s) in the plate or drag over any range of wells.<br /><br /></li>
     <li>To remove wells from the selection, hold down the Control (Ctrl) key (Apple key on a Mac) and click or drag over the wells.</li>
     
    </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="viewWellDetails">Viewing the values for a well</h4>
  <strong>Tooltips</strong><br />
  To see the values recorded for a particular well, hold the mouse cursor over the well you are
  interested in.  The &quot;tooltip&quot; will be displayed in the left hand area of the view.
  The tooltip lists all the Input samples and Parameter definitions for the selected well, along
  with any values which have been set.<br /><br />
  In the example, the tooltip for well &#39;A01&#39; of the &#39;Plate Example&#39; is shown.<br /><br />
    <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/toolTip.png" alt="Tool tip for well A1" />
  This indicates that:
    <ul>
     <li>The &quot;amounts&quot; for all of the Input samples: Buffer, Forward primer, Polymerase, Reverse primer, Template, Water and dNTPs 
     have been have been specified. <em> -these are the default values from the Protocol PiMS PCR</em><br /></li>
     <li>None of the Input samples have been specified <em> -all are listed as &quot;Unspecified sample&quot;</em></li>
     <li>Values for all of the Parameters A-E have been specified. <em> -again, these are the default values from the Protocol PiMS PCR</em></li>
     <li>The Output sample &quot;PCR Product&quot; has been set to Plate Example:A01</li>
     <li>No Target or Construct has been specified</li>
     <li>The status of the Experiment in well A01 is &quot;To_be_run&quot;</li>
    </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="inputSamples">Input samples</h4>
  The <strong>Input samples</strong> section is open by default, when you click the Plates tab, and displays a list of all of the Input samples from 
  the Protocol which was used to design the Plate Experiment <em> -from the PiMS PCR Protocol in the example</em><br /><br />

  For every sample listed in the protocol, you will see the following:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabSample.png" alt="An Imput sample on the Plates tab" />
   <ul>
     <li>The role the sample plays in the experiment. <em>
     &nbsp; -in the example this is &#39;Buffer&#39;</em><br /><br /></li>
     <li>A box to record the amount of the sample. If a default amount has been specified in the Protocol this will be displayed 
     along with the relevant unit.<br />
      <em> -in the example these are &quot;5&quot; &quot;&micro;L&quot;</em><br /><br /></li>
     <li>A box displaying the name of the &quot;Sample&quot;<br />
     If no default sample has been specified in the Protocol, this will say &quot;None&quot; <em>-as in the example.</em>
      &nbsp; -see <a href="#chooseSample">Choosing samples</a> below.<br /><br /></li>
     <li>An   <input class="button" value="Update" type="submit" /> button to save any changes you make to the sample.<br /><br /></li>
     <li>A link to <a href="#advancedFill">Advanced Fill Options:</a> importing Samples from other Plate Experiments, applying a gradient to a Plate and merging Samples from Plates.</li>
   </ul>
   <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="chooseSample">Choosing samples</h5>
    First select the wells you would like to choose a Sample for. <em> -see <a href="#selectWells">Selecting a range of wells</a></em><br />
    To choose a sample, open the drop-down list for a particular sample in the Input samples section of the Plates view.<br />
    <em> -the example shows the &quot;Buffer&quot; sample</em><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/sampleDropDown.png" alt="Sample drop-down list" /><br />
    <p>If any suitable Samples have already been selected for this Plate experiment, they will appear in the list and can be selected.<br />
    The option &quot;(various)&quot; is displayed when a range of wells has been selected which already have a different samples specified.<br />
    If there are no suitable samples in the list, select the final option &quot;Search...&quot; in the list.<br /><br />
    A popup, displaying a list of all suitable &#39;Sample&#39; records in PiMS, will appear.</p>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/searchPopup.png" alt="Pop-up for searching for a sample" />
    <ul>
     <li>In the popup, scroll down the list to find the correct record in PiMS.<br /><br /></li>
     <li>If you are unable to find the sample you are looking for you can refine the list.<br />
     Either, enter a value in the Search terms box and click the <input type="button" value="Quick search"/> button.<br />
     Or, open the &quot;Search Criteria&quot; box and enter terms in the box(es) and click the <input type="button" value="Search"/> button.<br /><br /></li>
     <li>Click the <a href="JavaScript:void(0)">Add</a> link next to the correct sample.<br />
     The popup will close, and the name of the sample will change to match your selection.  The tooltip for the relevant wells will also be updated to match the changes.<br />
     <em>-in the example the Sample called &quot;PCR buffer 10x&quot; was selected from the search results</em><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/sampleSelected.png" alt="Sample selected in well A01" /><br />
    <br /></li>
     <li>If there is no matching sample record, you will need to close the pop-up window
     and leave the Plate view.<br />
     To create a Sample record, select &#39;Create New Sample&#39; from the Sample menu.  You can then continue recording the details for your plate
     experiment.</li>
    </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
   
   <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="advancedFill">Advanced Fill options -copy from plate</h5>
   <!-- NOT SUPPORTED YET
   PiMS supports 3 Advanced fill options for specifying samples in a Plate experiment.<br />
   To see the available options:
   <ul>
    <li>Click the <a href="JavaScript:void(0)">Advanced Fill options...</a> link for a particular Sample in the Input samples section of the Plates tab.<br /><br /></li>
    <li>A popup containing a list with links to each option is displayed<br />
        <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/advancedFillOptions.png" alt="Advanced fill options" /><br />
        These are: &quot;One sample, gradient/snake&quot;, &quot;Copy from plate (or group of plates)&quot; and &quot;Merge by Score&quot;<br />
         <em> -in the example a Sample called &quot;Buffer&quot; was chosen</em><br /><br /></li>
   </ul>
   <strong>One sample, gradient/snake</strong><br />
   &nbsp;TODO<br /><br />
    -->
   <strong id="copyFromPlate">Copy from plate (or group of plates)</strong><br />
   This feature allows you to select the output samples from one or more Plate Experiments (in a group) to be used as input samples
   in your Plate experiment<br />
   This is only possible where the Samples are of the same type, i.e. they share a Sample category.<br /><br />

   <ul>
    <li>Click the <a href="JavaScript:void(0)">Advanced fill options...</a> link for a particular Sample in the Input samples section of the Plates tab.<br /><br /></li>
    <li>Locate the &quot;Copy from plate (or group of plates)&quot; section.<br />
     If there are any Plate experiments containing suitable output samples, they will be displayed in a drop-down list.<br />
     <em> -in the example, a Sample called &quot;Forward Primer&quot; was chosen and the the list contains one plate: Plate (from order) 63102</em><br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate1.png" alt="Select a plate for Copy from plate" /><br />
    </li>
    <li>Select a Plate experiment from the drop-down list and click the <input type="button" value="Next &gt;"/> button.<br />
    After a short delay the &quot;Copy&quot; screen will be displayed with a diagram of the &quot;Copy from group&quot; on the left and the &quot;to group&quot; on the right.<br />
     <em> -in the example the &quot;Copy from group&quot; is the selected &quot;Plate (from order) 63102&quot; </em><br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate2.png" alt="Copy from plate screen" /><br /><br /></li>
    <li>Using the mouse and the shift and control (Ctrl) keys (Shift and Apple on a Mac), select and de-select the wells in the &quot;Copy from group&quot; plate(s) which you wish to use.<br />
     <em> -by default, all of the wells are selected</em><br /><br /></li>
     <li>Mouse-over the wells of the &quot;to group&quot; plate(s).  You will see blue dots
     <img src="${pageContext.request.contextPath}/skins/default/images/icons/plates/copy_destination.png" alt="Blue dot copy" /> 
     where the selected wells will fit and red crosses 
     <img src="${pageContext.request.contextPath}/skins/default/images/icons/plates/copy_error.png" alt="Red cross can't copy" /> 
     where it will not.<br />
     &nbsp; <strong>note:</strong> the selection can span multiple Plates in the &quot;to group&quot;
     <em> -in the example, Wells A01 to C03 in the &quot;Copy from group&quot; have been selected to be used in wells F11 to H12 of the &quot;to group&quot;</em><br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate3.png" alt="Copy from plate selected wells" /><br /><br /></li>
    <li>Click on a well containing a blue dot to set the copied samples in the &quot;to group&quot; plate.  The blue dots will change to blue plus
     <img src="${pageContext.request.contextPath}/skins/default/images/icons/plates/copy_adding.png" alt="Copying Sample icon in well" /> icons.<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate4.png" alt="Copy from plate samples set in wells" /><br />
     &nbsp; <strong>note:</strong> if you attempt to set a the copied samples to wells which already have samples specified, you will see the following warning<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate6-warning.png" alt="Copy from plate warning" /><br />
     click <input type="button" value="OK"/> to specify the new Sample or <input type="button" value="Cancel"/> to leave the wells unchanged.<br /><br /></li>
    <li>You can copy the selected wells to other locations in the &quot;to group&quot; plate(s) or make a different selection of wells to copy to the same &quot;to group&quot; plate(s).<br /><br /></li>
    <li>Enter a value in the &quot;Amount to copy&quot; field to represent the sample volume.<br /><br /></li>
    <li>When you are happy with the selection, click <input type="button" value="Save"/>. While the details are being updated, you will see <br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate5-updating.png" alt="Copy from plate updating" /><br />
     <br />
     You will then be returned to the Plates tab for your Plate experiment.<br /><br /></li>
    <li>If you do not wish to continue at any stage, click <input type="button" value="Cancel"/> to return to the Plates tab.<br /><br /></li>
    <li>To confirm that the samples have been updated, mouse over the relevant wells in your Plate experiment and check the value in the tooltip. Or, click
     on a well and the new value will be displayed as the selected item in the drop-down list for the relevant Sample in the Input samples section.</li>
   </ul>

   
   <br />
   <strong id="mergeByScore">Merge by Score</strong><br />
   This feature allows you to use the result score from a related set of Plate Experiments to select the output samples to be used as input samples
   in your Plate experiment.  It is especially useful to select wells from a set of rescue plates to take forwards.<br />
   This is only possible where the input plates protocol contains a result parameter that is a score parameter.<br /><br />

   <ul>
    <li>Click the <a href="JavaScript:void(0)">Advanced fill options...</a> link for a particular Sample in the Input samples section of the Plates tab.<br /><br /></li>
    <li>Locate the &quot;Merge by score&quot; section.<br />
     If there are any Plate experiments containing suitable output samples, they will be displayed in a drop-down list.<br />
     <em> -in the example, a Sample called &quot;PCR Product&quot; (having a sample category of nucleic acid) was chosen, 
     and the the list contains plates that output a sample that is a nucleic acid and also have a score result parameter.</em><br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/mergebyScore1.png" alt="Select a plate for Copy from plate" /><br />
    </li>
    <li>Select a Plate experiment from the drop-down list and click the <input type="button" value="Next &gt;"/> button.<br />
    After a short delay the &quot;Merge by Score&quot; screen will be displayed with a diagram of the &quot;Copy from&quot; group, and all of its sibling groups on the left and the 
    &quot;to group&quot; on the right.  The &quot;Copy from groups&quot; are ordered by score, with group scoring best shown at the top.<br />
     <em> -in the example the &quot;Copy from group&quot; is the selected &quot;PCR139 PCR2&quot; and its sibling groups &quot;PCR139 PCR1&quot; and &quot;PCR139 PCR3&quot;.</em>  
     <br />You may need to scroll down to see all of the copy from groups.<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/mergebyScore2.png" alt="Merge by Score screen 2" /><br /><br /></li>
    <li>Using the up and/or down arrows located under each of the &quot;Copy from groups&quot; to order these groups so that the group with most wells to be taken forwards is at the top of the list.<br />
     <br /><br /></li>
     <li>Click on the <input type="button" value="Apply Scores"/> button.
     This will take forwards the wells from the group at the top of the &quot;Copy from groups&quot; into the &quot;to group&quot;
     where the wells will be coloured accordingly.  Notice that the selected wells are coloured more brightly than before they were selected.<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/mergebyScore3.png" alt="Merge by Score screen 3" />
     <br /><br /></li>
     <li>You may select wells from other groups in the &quot;Copy from groups&quot; list by clicking on the appropriate well.  
     Notice that the selected well will now be highlighted to show that it has been selected.<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/mergebyScore4.png" alt="Merge by Score screen 4" />
     <br /><br /></li>
    <li>Enter a value in the &quot;Amount to copy&quot; field to represent the sample volume.<br /><br /></li>
    <li>When you are happy with the selection, click <input type="button" value="Save"/>.<br /> 
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/mergebyScore5.png" alt="Merge by Score screen 5" />
     While the details are being updated, you will see <br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate5-updating.png" alt="Copy from plate updating" /><br />
     <br />
     You will then be returned to the Plates tab for your Plate experiment.<br /><br /></li>
    <li>If you do not wish to continue at any stage, click <input type="button" value="Cancel"/> to return to the Plates tab.<br /><br /></li>
    <li>To confirm that the samples have been updated, mouse over the relevant wells in your Plate experiment and check the value in the tooltip. Or, click
     on a well and the new value will be displayed as the selected item in the drop-down list for the relevant Sample in the Input samples section.</li>
   </ul>

   
   <br />
   
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="parameters">Parameters</h4>
  Clicking the <strong>Parameters</strong> section, in the left hand area of the Plates tab,
  reveals a list of the Parameters in the Protocol used to design the Plate Experiment.<br />
  <em> -the Parameters from the PiMS PCR Protocol are listed in the example</em><br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabParameters.png" alt="Parameters on the Plates tab" />
  <br />
  Most <strong>Parameters</strong> are displayed in the format below. <em> -<a href="#scoreParams">Score parameters</a></em> are exceptions.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabParameter.png" alt="A Parameter on the Plates tab" />
  <br />
 <ul>
  <li>The name of the Parameter -in the example this is <strong>A. Annealing temperature<span class="required">*</span></strong><br />
  &nbsp; <em>-the <span class="required">*</span> means a value must be recorded for this Parameter</em><br /><br /></li>
  <li>Text and number values have a box into which you can type the value you want to record.
  If the protocol has a default value, this will be shown in the box.<br />
  <em>-the example shows the default value &quot;45&quot;, this can be edited</em><br /><br /></li>
  <li>True/false Parameters have a box displaying Yes or No whichever is the default value <em>-No in the example</em><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabParameterTF.png" alt="True/False Parameter on the Plates tab" ><br />
   This can be changed by selecting from the drop-down list.<br />
   &quot;(Various)&quot; is displayed in the box if a range of wells has been selected which have different values.<br /><br /></li>
  <li>Values for a Parameter with a list of possible values are also displayed in a drop-down list with the default value selected.<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabParameterPossVals.png" alt="Possible values Parameter on the Plates tab" ><br />
  <br /></li>
  <li>Each Parameter has an <input type="button" value="Update"/> button.<br /><br /></li>
 </ul>
   <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="updateParams">Updating parameters</h5>
   Parameter values are updated as follows:
   <ul>
    <li>Select the wells you wish to update in the Plate or Plates in the right hand area of the screen.<em> -see <a href="#selectWells">Selecting</a> a range of wells</em><br /><br /></li>
    <li>Enter a value in the box or select one from the drop-down list for the Parameter you wish to update.<br /><br /></li>
    <li>Click the <input type="button" value="Update"/> button for the Parameter.<br />
    While the details are being updated, you will see <br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate5-updating.png" alt="Copy from plate updating" />
     <br />
     You will then be returned to the Plates tab for your Plate experiment.<br /><br /></li>
    <li>To confirm that the Parameter value has been updated, mouse over the relevant wells in your Plate experiment and check the tooltip. Or, click
     on a well and the new value will be displayed as the selected item in the box or drop-down list for the relevant Parameter in the Parameters section.</li>
   </ul>
   
  <div class="toplink"><a href="#">Back to top</a></div>

   <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="scoreParams">Score parameters</h5>
   PiMS supports scoring of experiments, by means of specially-named protocol parameters.  <em> -see <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp">Protocol</a> help</em><br />
   It also associates colours and icons with those scores, making it very easy to get an overview of a plate experiment's success (or otherwise).<br />
   <br />
   Score parameters are recorded in a Protocol in a specific way:
   <ul>
    <li>a name beginning with two underscore characters e.g. __SCORE</li>
    <li>the value you wish to display in the Plate experiment e.g. Correct size?</li>
    <li>a default value of &quot;Unscored&quot;</li> 
    <li>a list of possible values from worst to best e.g. Unscored;No;Maybe;Yes;</li>
   </ul>
    <br />   
   When the protocol you have used for a Plate experiment contains a <strong>Score parameter</strong>
   it will be displayed as a separate section in the Plates tab. <em> -the example shows the display of a Score parameter called &quot;Correct size PCR product?&quot;</em>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabParameterYNM.png" alt="Score Parameter on the Plates tab" /><br />
   Each of the &quot;Possible values is displayed in a list of radio buttons with its associated icon and background colour.<br />
   Initially, the default value &quot;Unscored&quot; will be checked.<br /><br />
   
   Score parameters are updated in the same way as other parameters;<br />
   <ul>
    <li>select the wells you wish to update <em> -see <a href="#selectWells">Selecting</a> a range of wells</em></li>
    <li>click on the appropriate radio button</li>
    <li>click <input type="button" value="Update"/></li> 
   </ul>
   
   The example shows a Plate experiment which has been scored<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabScored.png" alt="Score Parameter set on the Plates tab" /><br />
     
  <br />
   <strong>note:</strong> it is possible to associate different icons and colours with a score parameter but you will need to ask your PiMS administrator to do this.

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="status">Status</h4>
  Clicking the <strong>Status</strong> section, in the left hand area of the Plates tab,
  displays a drop-down list of possible Status values:<br />  &quot;To be run&quot; (default), &quot;In process&quot;, &quot;OK&quot; and &quot;Failed&quot;.<br /><br />
  It also updates the representation of the Plate (or Plates) in the left hand area with each well containing a Status icon:<br /><br />
   &nbsp; <img src="${pageContext.request.contextPath}/skins/default/images/icons/status/status_To_be_run.png" alt="Status To be run icon" /> To be run.<br />
   &nbsp; <img src="${pageContext.request.contextPath}/skins/default/images/icons/status/status_In_process.png" alt="Status In process icon" /> In process.<br />
   &nbsp; <img src="${pageContext.request.contextPath}/skins/default/images/icons/status/status_OK.png" alt="Status OK icon" /> OK.<br />
   &nbsp; <img src="${pageContext.request.contextPath}/skins/default/images/icons/status/status_Failed.png" alt="Status Failed icon" /> Failed.<br /><br />
   The default is <strong>To be run</strong> as in the example:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabDefaultStatus.png" alt="default Status set on the Plates tab" /><br />

   The Status of the individual wells can be updated in the same way as Samples and Parameters;<br />
   <ul>
    <li>Select the wells you wish to update <em> -see <a href="#selectWells">Selecting</a> a range of wells</em></li>
    <li>Select the appropriate Status from the drop-down list</li>
    <li>Click the <input type="button" value="Update"/> button. <br />
    While the details are being updated, you will see <br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate5-updating.png" alt="Copy from plate updating" />
    <br />
     The Plate representation will be updated with the correct Status icons in the appropriate wells.</li> 
   </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="outputSamples">Output Samples</h4>
  When you record a Plate experiment PiMS automatically creates an &quot;Output sample&quot; for each well as defined in the Protocol<br />
  Clicking the <strong>Output samples</strong> section, in the left hand area of the Plates tab,
  displays a representation of the Plate with a sample icon in each well.<br />
  
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabOutputSamples.png" alt="Output Samples on the Plates tab" /><br />
  <br />
  Click on a well to see the details recorded for that individual Sample.
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="experiments">Experiments</h4>
  Clicking the <strong>Experiments</strong> section, in the left hand area of the Plates tab, updates the representation of the Plate
  (or Plates) in the left hand area with each well containing an Experiment 
   <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> icon:<br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabExperiments.png" alt="Experiments on the Plates tab" /><br />
  Click on a well to View and Edit the details recorded for that individual Experiment.
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="targetConstruct">Targets and Constructs</h4>
  Clicking the <strong>Targets and constructs</strong> section, in the left hand area of the Plates tab,
  displays a drop-down list of any Targets and constructs you have assigned to the wells of the Plate experiment.<br /><br />
  The default value for a new Plate experiment is &quot;None&quot;, as in the example.<br />
  If the Plate experiment was created as a &quot;Next experiment&quot; for an existing Plate then any Targets or constructs assigned to the wells will be assigned to the corresponding wels of the new Plate.<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabTargetsConstructs.png" alt="Targets and Constructs on the Plates tab" /><br />

  To assign a Target or Construct to wells in a Plate:
  <ul>
   <li>Open the <strong>Targets and constructs</strong> section of the Plates tab.<br /><br /></li>
   <li>Select the range of wells you wish to assign the Target to. <em>-see <a href="#selectWells">Selecting a range of wells</a></em><br /><br /></li>
   <li>If you have already assigned the Target or Construct to other wells in the Plate, select it from the drop-down list.<br /><br /></li>
   <li>If you have not assigned the target or Construct to other wells in the Plate, select &quot;Search...&quot; from the drop-down list.<br />
    A popup containing a list of your Targets and Constructs will be displayed:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesTabTarg1.png" alt="Targets and Constructs search popup" /><br /><br /></li>
   <li>Scroll down the list or use the Search box to locate the correct target or construct and click <a href="JavaScript:void(0)">Select</a> in the first column.<br /><br /></li>
   <li>You will be returned to the <strong>Targets and constructs</strong> section of the Plates tab with the name of the selected target displayed.<br /><br /></li>
   <li>Click <input type="button" value="Update" /><br /><br /></li>
   <li>While the details are being updated, you will see <br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/copyFromPlate5-updating.png" alt="Copy from plate updating" />
    <br />
     The Plate will be updated with the correct Target or Construct assigned to the appropriate wells.<br /><br /></li> 
   <li>To verify this, mouse over the relevant wells in your Plate experiment and check the tooltip.<br /><br /></li>
   <li>The selected Target or Construct will now be added to the drop-down list.<br />
    &quot;(various)&quot; is displayed in the box if a range of wells has been selected which have different values.</li> 
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="nextExp">Next experiment tab</h3>
  The &quot;Next experiment&quot; tab contains a list of possible Next Experiments for the outputs from your Plate experiment.<br /><br />
  This feature allows you to link PiMS Plate Experiments to match the sequence of Plate experiments you
  might perform in the lab.<br />
  In order to do this, you need to create &#39;Protocols&#39; (experiment templates)
  where the &#39;specification&#39; or &#39;Type&#39; for the Output sample from
  the first Experiment's protocol will match that of an Input sample for the next
  Experiment's protocol.<br />
  PiMS will suggest possible &#39;Next&#39; Experiment types based on the sample &#39;Type&#39;.<br />
 &nbsp; <em> -see <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp">Protocol</a> Help for more details.</em><br /><br />
 
 <h4 id="nextPlateExp">Create the Next Plate Experiment</h4>
 <ul>
  <li>Open a Plate Experiment record and click the &#39;Next experiment&#39; tab.<br />
     You will see a list of possible &#39;Next&#39; experiments represented by:<br />
     &nbsp;<strong>Role</strong> -for the samples as experiment Input Samples as
     defined by a <strong>Protocol</strong> and a <input type="button" value="New Experiment" /> button.<br />
     &nbsp; <em>-in the example, the Output samples from this Plate experiment could be used as Input samples called:<br />
     &nbsp; &#39;Template&#39; in a &#39;PiMS PCR&#39; Plate experiment<br />
     &nbsp; or &#39;Insert&#39; in a &#39;YSBL LIC cloning&#39; Plate experiment etc.</em><br /><br />
  
 <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/nextExpTab.png" alt="Next experiment tab" />
 <br /><br /></li>
  <li>From the list, choose an appropriate Experiment and click <input type="button" value="New Experiment" /> to see a &quot;New Plate Experiment&quot; form
  with Experiment type and Protocol fields populated.<br />
    <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/linkedPlateBasic.png" alt="The next plate experiment" />
    <br /><br /></li>
  <li>Complete the Basic details, and click <input type="button" value="Create" /><br /><br /></li>
  <li>You can then record and update the details for the new Plate experiment.<br />
   &nbsp;<strong>note:</strong> The relevant sample details will be completed.<br />
   &nbsp;<strong>also:</strong> Any Target or Construct, which was defined in the first
   plate experiment, will also be defined for the wells of the new plate.<br /><br /></li>
  </ul>

 <h4 id="nextSingleExp">Create individual Experiments</h4>
  You can also use the Output sample from an individual well of the plate as the
  Input sample for a new experiment.<br />
  <ul>
   <li>Click Plates tab</li>
   <li>Open the &#39;Output samples&#39; section</li>
   <li>Click on the relevant well of the plate</li>
   <li>From the resulting Sample View page, open the box labelled <strong>Use Sample as input in a New Experiment:</strong></li>
   <li>From the list, choose an appropriate Experiment and click <input type="button" value="New Experiment" /> to see a &quot;New Experiment&quot; form
       with Experiment type and Protocol fields populated and all Parameters and Samples as specified in the Protocol.<br />
   &nbsp; -see <a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#nextExp">Linking</a> Experiments</li>
  </ul> 

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="attachments">Attachments tab</h3> 
  The &quot;Attachments&quot; tab allows you to attach files and images to the Plate Experiment and record &quot;Notes&quot; as for other PiMS records.<br />
  Any files or images which are uploaded to the Plate Experiment will also be linked to the individual well experiments.<br /><br />
  Click the <strong>Attachments</strong> tab<br /> 
    <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/attTabClosed.png" alt="The Attachments tab" /><br />
    
    <h5>To upload an Image to the Plate experiment:</h5>
    <ul>
     <li>Open the <strong>Images</strong> box</li>
     <li>Use the <input type="button" value="Browse.." /> button to locate the image on your computer</li>
     <li>Enter a title and a legend (optional) then Click <input type="button" value="Upload File" /></li>
     <li>The image will be displayed as a clickable thumbnail image to the full sise image in the Images box<br />
     <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/attTabPlusImage.png" alt="The Attachments tab with image" /><br /></li>
    </ul>

    <h5>To upload a File to the Plate experiment:</h5>
    <ul>
     <li>Open the <strong>Attachments</strong> box</li>
     <li>Use the <input type="button" value="Browse.." /> button to locate the file on your computer</li>
     <li>Enter a title and a Description (optional) then Click <input type="button" value="Upload File" /></li>
     <li>The file will be saved in the PiMS uploads directory and will be displayed as a link to the file in the Attachments box<br />
     <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesSpreadsheetAttached.png" alt="Attachment tab with file" /><br /></li>
     <li>If you use a spreadsheet to create or update a Plate experiment, PiMS will automatically upload a copy to the Plate experiment 
     and create a link to it in the Attachments box. <em> -see <a href="#spreadsheet">Spreadsheets</a> importing and exporting data</em></li>
    </ul>

    <h5>To add a Note to the Plate experiment:</h5>
    <ul>
     <li>Open the <strong>Notes</strong> box</li>
     <li>Enter some text in the Details field then Click <input type="button" value="Add" /></li>
     <li>The Note will be displayed as a row in the Notes box along with the date it was recorded and the name of the PiMS user who recorded it.<br />
     <img class="imageNoFloat" style="margin-left:0" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/attTabPlusNote.png" alt="Attachment tab with a note" /><br /></li>
    </ul>

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="spreadsheet">Spreadsheets importing and exporting data</h3>
  
  It is possible to populate the details for a Plate Experiment by uploading a spreadsheet containing the data.  
  It is also possible to export the Plate experiment to a spreadsheet for use in other applications.<br />
  
  <!-- ................................................................................ -->
  <h4 id="uploadFromSpreadsheet">Uploading data from a spreadsheet</h4>
 First, complete the <a href="#details">Details</a> for the New Plate experiment:<br />
 &nbsp; -enter a name or barcode for the plate group, select a Lab notebook, Experiment type, Protocol, 
 Start and End dates and a Plate type.<br />
  &nbsp; <strong>note:</strong> if your group contains more than one Plate, you must also enter values for the Plate ID(s).<br /><br />
 
 Click the <input type="button" value="Browse..."/> button, at the bottom of the page, and navigate to the spreadsheet
 on your computer, then click <input type="button" value="Create"/>.
 <br />
 While PiMS is recording the Plate experiment, the button will change to <input disabled="disabled" type="button" value="Creating..."/>.<br />
 <br />
 If the spreadsheet has been formatted correctly <em>( -see <a href="#formatSpreadsheet">Spreadsheet format</a></em>),
 the new Plate experiment will be displayed with the Basic details tab open.<br /><br />
 PiMS can provide a suitably formatted spreadsheet for the Protocol and number of Plates you have specified for your Plate Experiment.<br />
 <ul>
  <li>Click the <input class="button" value="Get Spreadsheet" type="submit" /> button.<br /><br /></li>
  <li>PiMS will create a .csv file with the appropriate number of rows and columns to match the Protocol and plate type you have selected.
   <em>&nbsp; -see <a href="#formatSpreadsheet">Spreadsheet</a> format for more information.</em><br />
      Save a copy then it this file with a spreadsheet application to enter the relevant information for your Plate Experiment.</li>
 </ul>
 
 The spreadsheet will be attached to the Plate experiment and can be accessed from the &quot;Attachments&quot; tab.<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/platesSpreadsheetAttached.png" alt="Attachment tab with spreadsheet" /><br />
<em> -in the example a spreadsheet called &quot;PIMS_Culture.csv&quot; was used to record the Plate experiment</em><br /><br />   

  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ................................................................................ -->
  <h4 id="formatSpreadsheet">Spreadsheet format</h4>
  
  To be able to populate the details for a Plate experiment using a spreadsheet, it is important that the spreadsheet is formatted correctly.
  <ul>
   <li>The spreadsheet should contain one row for each well in the Plate or group of Plates.</li>
   <li>There must be <strong>at least</strong> as many rows as there are wells in the Plates(s).</li>
   <li>The spreadsheet must be saved as &#39;CSV&#39; or &#39;Comma Separated Values&#39; format.</li>
   <li>The column headings must match the names of Parameters and Samples for the relevant PiMS protocol.<br />
       &nbsp; <em>-any headings which do not match will be ignored</em></li>
  </ul> 

 <h5>Column headers: all spreadsheets</h5>
 <div class="glossary">
 <dl>
  <dt>&quot;PlateId&quot;</dt><dd>
  This is either the <strong>Group name</strong> if your Plate Experiment contains only 1 Plate<br />
  or the values of the <strong>Plate ID(s)</strong> for the individual Plates in a group<br />
  This allows you to specify the details for e.g. Plate 1 well C06 as distinct from Plate 2 well C06.</dd>
  <dt>&quot;Well&quot;</dt><dd>
  Can also be called &quot;well&quot;, &quot;Position&quot;, &quot;position&quot;,
  or &quot;Pos&quot;.<br />
  This is the well that this row of the spreadsheet applies to.  e.g. C2, C02.
  </dd>

  <dt>&quot;Construct&quot;</dt><dd>
  Can also be called &quot;construct&quot;, or &quot;Experimental construct&quot;.<br />
  This is the name of a construct, recorded in PiMS, which the experiment in this
  well relates to.
  <!-- TODO how do you define a Construct rather than a Target? see PIMS-680 -->
  </dd>

  <dt>&quot;Target&quot;</dt><dd>
  Can also be called &quot;target&quot;.<br />
  This is the &#39;name&#39; (not 'Target ID') of a target, recorded in
  PiMS, which this well relates to.<br />
  &nbsp; <strong>note:</strong> Data in this column is only used if no &#39;Construct&#39;
  was specified<br />
  - at present, an experiment in PiMS cannot be linked independently to both a Construct
  and a Target.
  </dd>
  </dl>
 <h5>Column headers: protocol-specific details</h5>
  Any additional column headers relate to Protocol-specific details.  You may include a
  column for each Parameter definition and Input sample in your Protocol.  The values
  which you enter in the column cells, for the individual wells of the plate, must be
  of the correct &#39;type&#39; e.g. numeric, text, true&#47;false etc. for Parmeter definitions
  and the names of existing PiMS Sample records for Input samples.  No value will be recorded for the well of the
  Plate Experiment if these rules are not followed.<br /><br />

  <dl>
  <dt>&quot;Parameter name&quot;</dt><dd>
  The name of a <a href="#parameters">Parameter</a> definition in the PiMS
  Protocol.<br />
  You may have any number of &#39;Parameter name&#39; columns in your spreadsheet to
  coincide with those in your Protocol.<br />
  The column header(s) <strong>must</strong> match the Parameter definition name(s) in the protocol.
  <br />
  The values in each column will be recorded as Parameter values for the relevant Parameter
  definition and must be of the correct Parameter-type.
  <br /><br />
  For example, the Protocol named "My Culture protocol" has a true/false
  Parameter named "Shake culture?"<br /><br />
  To record a Plate experiment using this Protocol, by uploading data from a spreadsheet,
  the spreadsheet would need a column headed "Shake culture?"<br /><br />
  You would also need to record &#39;TRUE&#39; or &#39;FALSE&#39; for each value in
  that column.<br />
  &nbsp; <strong>note:</strong> Default parameter values will be overridden by
  values in the spreadsheet<br />
  &nbsp; -if you leave a spreadsheet cell empty, no value will be recorded.
  </dd>

  <dt>&quot;Input sample name&quot;</dt><dd>
  The name of an Input sample definition or Reference input sample, defined in the
  Protocol for this Experiment.<br />
  You may have any number of &#39;Input sample name&#39; columns in your spreadsheet
  to coincide with those in your Protocol.<br />

  The values (sample names) in this column, will be recorded as values for this Input
  sample.<br />
  They <strong>must</strong> match the names of existing &#39;Sample&#39; records in
  PiMS.<br /><br />

  For example the Protocol named "My Culture protocol" has a Reference input
  sample named "Culture media"<br /><br />
  To record a Plate experiment using this Protocol, by uploading data from a spreadsheet,
  the spreadsheet would need a column headed "Culture media"<br /><br />
  If you wished to specify a specific culture media Sample for the individual wells of
  the Plate Experiment, you would need to record the "name" of existing PiMS
  Sample records in this column.
  </dd>
 </dl>
 </div>
  If you experience any difficulties creating or using spreadsheet please contact the <a href="mailto:pims-defects@dl.ac.uk">PiMS developers</a>.
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="updateFromSpreadsheet">Updating a Plate Experiment</h4>
  After you have recorded a Plate Experiment, you may update the details recorded with values in a spreadsheet (saved as .csv format).<br />
  <ul>
   <li>Click on the &quot;Basic details&quot; tab and use the <input type="button" value="Browse..." /> button to navigate to your spreadsheet.<br /><br /></li>
   <li>Click <input type="button" value="Save" />.  You will see a warning:<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/updateWithSpreadsheet.png" alt="Warning when updating Plate with a spreadsheet" /><br /><br />
    Click <input type="button" value="OK" /> to continue or <input type="button" value="Cancel" /> if you do not wish to proceed.<br />
     You will then be returned to the Plates tab for your Plate experiment.<br /><br /></li>
    <li>If you clicked &quot;OK&quot; to confirm that the values have been updated, mouse over the relevant wells in your Plate experiment and check the tooltip. Or, click
     on a well and the new value will be displayed as the selected item in the box or drop-down list for the relevant Parameter in the Parameters section.</li>
    <!-- CURRENTLY GIVES AN NPE See PIMS-3332 -->
  </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="exportToSpreadsheet">Exporting data to a spreadsheet</h4>
  You can export the data recorded for a Plate Experiment (or group of plates) from PiMS to a .csv file which can be opened with a spreadsheet application.<br />
  <ul>
   <li>Click the Basic details tab for a Plate Experiment<br /><br /></li>
   <li>Click <a href="JavaScript:void(0)">Export this group to spreadsheet...</a><br /><br /></li>
   <li>PiMS will create a .csv file formatted to match the Plate experiment(s) with 1 row per well.<br />
       Either save the file or it open with a spreadsheet application.<br />
       <em>-the file can be edited and used for <a href="#updateFromSpreadsheet">Updating a Plate Experiment</em><br /><br /></li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="cohort">Progress report</h4>
    PiMS will create a &quot;Progress report&quot; summarising values recorded for all wells in a Plate Experiment.<br />
    The report is exported from PiMS as a .csv file which can be opened with a spreadsheet application.<br />
    The report contains 1 column for each Parameter in the Plate Experiment's Protocol and 1 row for each well in the Plate Experiment.<br /><br />
    To create a Progress report:
    <ul>
     <li>Click <a href="JavaScript:void(0)">Progress report</a> in the Basic details tab for a Plate Experiment.<br />
     After a short delay the Progress report will either open with your default spreadsheet application or you will see a pop-up window 
     with options to open or save the file <em>-as in the example</em><br />
         <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/plateProgress.png" alt="Creating the Progress report for a Plate Experiment" />
     </li>
     <li>The file will be named with &quot;Outcomes&quot; appended to the Plate experiment name <em>Plate Example Outcomes.csv in the example</em> </li>
    </ul> 
    <strong>note:</strong> If the Plate Experiment has been linked to another Plate Experiment by creating the <a href="#nextExp">Next Experiment</a> 
    these values will also be included in the report with additional columns for each Parameter in the Next Experiment's Protocol.<br /><br />
    <strong>note:</strong> Similarly, if selected wells of the Plate Experiment have been linked to Single Experiments, these values will be included in the report.<br /><br />
    

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="locatePlate">Finding Plate Experiments</h3>
   PiMS allows you to locate Plate experiments which you have recorded.<br />
   If you have recently viewed a Plate Experiment it can be accessed as a link in the History brick or menu<br /><br />
   
   To find a Plate Experiment which you have not viewed recently, select <strong>Search Plate Experiments</strong> from the Experiments menu<br />
   
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/plateSearch.png" alt="Links to Search plate experiments" /><br /><br />
   You will see the Search plate experiments page.<br />
   This contains a list of Plate experiments recorded in PiMS below 2 boxes labelled <strong>Quick Search</strong> and <strong>Advanced Search</strong>.<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/searchPlates.png" alt="Search plate experiments view" /><br /><br />
   <ul>
    <li>Open the <strong>Quick Search</strong> box and enter a search term<br />
    &nbsp; -this search finds matches in the <strong>Group name</strong> and is case sensitive<br /><br /></li>
    <li>Open the <strong>Advanced Search</strong> box to narrow your search.<br />
    &nbsp; -this search option allows you to search for Plate experiments by Group name and also by Experiment type and Holder type<br /><br />
    The example shows the result of an Advanced search for Plate Experiments with the following criteria:<br />
    &nbsp; <strong>Name</strong> contains &quot;Example&quot; <em>-case sensitive Group name</em><br />
    &nbsp; <strong>Experiment Type</strong> PCR <em>-selected from the drop-down list</em><br />
    &nbsp; <strong>Holder Type</strong> 0.1ml 96-well, thin wall plates (lower profile, skirted)<em>-selected from the drop-down list</em><br /><br />
    &nbsp; -there are 3 Plate Experiments which match the search criteria from a total of 42 recorded in PiMS<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/plates/searchPlatesAdvanced.png" alt="Advanced search plate experiments result" /><br /><br />
    </li>
   <ul>
  <div class="toplink"><a href="#">Back to top</a></div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

