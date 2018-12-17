<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Guide to using PiMS' />
  <jsp:param name="extraStylesheets" value="helppage" />   
</jsp:include>

<c:catch var="error">

<c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}/">Home</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Guide to using PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<c:set var="top">
   <a href="#overview">Back to top</a>
</c:set>
<div class="help">

  <pimsWidget:box initialState="fixed" id ="overview" title="Overview">
  <div class="help">
      <p style="padding-left:.6em; ">PiMS is a Laboratory Information Management System (LIMS) which
      is being developed to support data management for all stages of protein
      production, from Target selection to Crystallisation.
        <br /><br />
      This guide describes how to use PiMS. It demonstrates how to record your
      data, by creating PiMS records, and how to search for, view, edit and
      delete records.
    <br /><br />
      If you have experienced any problem using PiMS please contact the
     <a href='mailto:pims-defects@dl.ac.uk'>developers</a>.</p>
    </div>  
   </pimsWidget:box>

<h2 id="toc">Contents</h2>
<%--New format Susy 300710 --%>
<pimsWidget:box initialState="fixed" title="Help for:" >
 <div class="leftcolumn">
    <ul>
        <li><a href="#general">General help</a></li>
        <li><a href="#targets">Targets</a></li>
        <li><a href="#constructs">Constructs</a></li>
        <li><a href="#protocols">Protocols</a></li>
        <li><a href="#experiments">Experiments</a></li>
        <li><a href="#plates">Plate Experiments</a></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
        <li><a href="#sample">Samples</a></li>
        <li><a href="#reference">Reference data</a></li>
        <li><a href="#user">User and Access permissions</a></li>
        <li><a href="#oppf">OPPF</a>-specific help</li>
        <li><a href="#expert">Expert</a> -generic user interface</li>                
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="general" initialState="open" title="General Help" extraHeader="${top}">
 <div class="leftcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/HelpGettingStarted.jsp">Getting Started with PiMS</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpBasicConcepts.jsp">Basic Concepts used in PiMS</a></li>
      <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp">Login and Navigation</a>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#homePage">PiMS Homepage</a></li>
          <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#logIn">Logging in</a> to PiMS</li>
          <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#navigation">Navigating</a> around PiMS
            <ul>
              <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#menuBar">PiMS menubar</a> and menus</li>
              <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#functionsPages">Functions</a> pages</li>
              <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#homePageIcons">Icons</a> in the PiMS Homepage</li>
              <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#contextMenus">Context menus</a></li>
              <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp#diagram">Diagrams</a></li>
            </ul></li>
        </ul></li>       
    </ul>
 </div>   
 <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/HelpMRU.jsp">History</a></li>
      <li><a href="${pageContext.request.contextPath}/help/Perspectives.jsp">Perspectives</a></li>       
      <li><a href="${pageContext.request.contextPath}/help/HelpQuickReference.jsp">Quick reference guide</a></li>       
      <li>PiMS <a href="${pageContext.request.contextPath}/help/glossary.jsp">glossary</a> -definitions of PiMS terms</li>
    </ul>
    <h4>Recommended browsers</h4>
    You can use PiMS with:
    <ul>
      <li><a href="http://www.mozilla.com/firefox/">Firefox 4.0 or later</a></li>
      <li>Internet Explorer 8 or later, with <a href="http://www.java.com/en/download/windows_ie.jsp">Java plugin</a></li>
      <li><a href="http://www.apple.com/safari/">Apple Safari 5 or later</a></li>
      <li>Chrome</li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="targets" initialState="open" title="Targets" extraHeader="${top}">
 <div class="leftcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpTarget.jsp">Target Help</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp">New Target</a>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp#downloadTarget">Download</a> -from file or database</li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp#downloadTarget">Create</a> manually -with a form</li>            
          <li><a href="${pageContext.request.contextPath}/help/target/HelpRecordDNASeq.jsp">Recording the DNA Sequence</a></li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpRecordDNASeq.jsp">Standards for DNA sequences</a></li>
        </ul>
      </li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpTarget.jsp#targetDetails">Target Details</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpDnaTarget.jsp">DNA Targets</a> non-Protein Targets</li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNaturalSourceTarget.jsp">Natural Source Targets</a> -with no sequence</li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpComplex.jsp">Complexes</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpSyntheticGene.jsp">Synthetic genes</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpTargetReports.jsp">Target Reports</a>
        <ul><li><a href="${pageContext.request.contextPath}/help/HelpActiveTargetsReport.jsp">Active Target Report</a></li></ul></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpTargetScoreboard.jsp">Target Scoreboard</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpBlastTarget.jsp">Blast</a> searches in PiMS</li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpAutomatedBlast.jsp">Automated Blast</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp">Sequence Similarity search</a><br /><br /></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpTargets.jsp">Guide</a> to expert Target management</li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="constructs" initialState="open" title="Constructs" extraHeader="${top}">
 <div class="leftcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">Recording Constructs</a>
      <%--<li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">Recording Constructs</a> -for non-standard codons</li>--%>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#basicDetails">Basic details</a></li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#step2">Primer overlaps</a> <em>gene-specific region</em></li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#suggPrimers">Possible Primers</a> <em>-designed by PiMS</em></li>    
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#extAndTags">5'-Extensions and Protein tags</a>
            <ul>
              <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#startstop">Start and Stop</a> codons</li>
              <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#extensions">5'-Extensions</a></li>
              <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#prottags">Protein tags</a></li>
            </ul></li>
         </ul></li>
      <li>Recording <a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp#primerless">Primer-less</a> Constructs</li>
      <li>PiMS support for <a href="${pageContext.request.contextPath}/help/HelpMutagenesis.jsp">Mutagenesis</a></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#constructDescription">Construct Details</a>
        <ul>
           <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#progress">Construct Progress</a></li>
           <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#primerDetails">Primers</a></li>
           <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#pcrProd">Insert (the PCR product)</a></li>
           <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#proteins">Proteins</a></li>
        </ul></li>    
      <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp">Ordering Primers</a>
         <ul>
            <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp#listConstructs">The Construct List</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp#setupPlate">Primer order Set-up Grid</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp#createOrder">Creating the Order Form</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp#template">Order form template</a></li>
        </ul></li>
     </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="protocols" initialState="open" title="Protocols" extraHeader="${top}">
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp">Protocol</a> Help
       <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#viewprotocol">Viewing a protocol</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#copyprotocol">Copying a protocol</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#deleteprotocol">Deleting a protocol</a></li>
        </ul></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#createprotocol">Recording a new protocol</a></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#parameters">Parameters</a>
         <ul>
            <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#createparameter">Adding a parameter</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#editparameter">Editing a parameter</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#deleteparameter">Deleting a parameter</a></li>
         </ul></li>     
     </ul>
 </div>
 <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#parameters">Input samples</a>
        <ul>
           <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#createinput">Adding an input sample</a></li>
           <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#editinput">Editing an input sample</a></li>
           <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#deleteinput">Deleting an input sample</a></li>
      </ul></li>
      <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#outputsamples">Output samples</a><br /><br /></li>
      <li><a href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp#useprotocol">Using PiMS protocols</a></li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="experiments" initialState="open" title="Experiments" extraHeader="${top}">
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp">Experiment Help</a></li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Record</a> a new Simple Experiment</li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#viewExp">View and update</a> Experiment details
           <ul>
             <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#expDetails">Details</a></li>
             <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#expParams">Parameters</a></li>
             <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#samples">Input and Output Samples</a></li>
             <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#fileUpload">Images and Attachments</a></li>
             <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#notesUpload">Notes</a></li>
           </ul></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#nextExp">Linking</a> Experiments</li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#searchExp">Search</a> for an Experiment</li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#deleteExp">Delete</a> an Experiment</li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#graph">Graph</a> view<br /><br /></li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/ExperimentGroup.jsp">Experiment Group Help</a><br /><br /></li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpPlasmidStocks.jsp">Plasmid stocks</a></li>
       <li><a href="${pageContext.request.contextPath}/help/experiment/HelpCellStocks.jsp">Cell stocks</a></li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="plates" initialState="open" title="Plate Experiments" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp">Plate Experiment Help</a>
       <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#recordPlateExp">Recording a new plate experiment</a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#details">Plate details</a></li>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#numPlates">Recording multiple Plates</a></li>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#useSpreadsheet">Using a spreadsheet</a></li>
            </ul></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#plateView">Plate Experiment View</a></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#basicdetails">Basic details</a> tab</li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#platesTab">Plates</a> tab
            <ul>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#selectWells">Selecting</a> a range of wells</li>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#viewWellDetails">Viewing</a> the values for a well</li>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#inputSamples">Input samples</a>
                    <ul>
                     <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#chooseSample">Choosing samples</a></li>
                     <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#advancedFill">Advanced Fill options</a></li>
                    </ul></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#parameters">Parameters</a>
              <ul>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#updateParams">Updating parameters</a></li>
                <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#scoreParams">Score parameters</a></li>
              </ul></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#status">Status</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#outputSamples">Output samples</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#experiments">Experiments</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#targetConstruct">Targets and Constructs</a></li>
          </ul></li>
    </ul>          
 </div>
 <div class="rightcolumn">
    <ul>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#nextExp">Next Experiment</a> tab
        <ul>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#nextPlateExp">Create the Next Plate Experiment</a></li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#nextSingleExp">Create a Single Experiment</a></li>
        </ul></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#attachments">Attachments</a> tab</li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#spreadsheet">Spreadsheets</a> importing and exporting data
        <ul>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#uploadFromSpreadsheet">Uploading</a> data from a spreadsheet</li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#formatSpreadsheet">Spreadsheet</a> format</li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#updateFromSpreadsheet">Updating</a> a Plate Experiment</li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#exportToSpreadsheet">Exporting</a> data to a spreadsheet</li>
            <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#cohort">Progress report</a></li>
        </ul></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#locatePlate">Finding</a> Plate Experiments</li>        
    </ul>
 </div>
</pimsWidget:box>
<%--
<pimsWidget:box id="robots" initialState="open" title="Robots" extraHeader="${top}" >
 <div class="leftcolumn"> 
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaOverview.jsp">Akta Experiment Help</a>
          <ul>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaInstalling.jsp">Installing the JUnicorn tool</a></li>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaConfiguring.jsp">Configuring the JUnicorn tool</a></li>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaConnecting.jsp">Connecting to Unicorn</a></li>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaBrowsing.jsp">Browsing for results</a></li>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaCreating.jsp">Creating the Akta experiment files</a></li>
            <li><a href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaImporting.jsp">Importing into PiMS</a></li>
          </ul>
       </li>
    </ul>
 </div>
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/robots/HelpCaliper.jsp">Caliper Robot Help</a>
    </ul>
 </div>
</pimsWidget:box> --%>

<pimsWidget:box id="sample" initialState="open" title="Samples" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp">Sample Help</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp#sampleList">Sample list</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp#activeSample">Active Sample report</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp#viewSample">The Sample View</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp#moveSample">Move a Sample</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpSamples.jsp#divideSample">Divide a Sample</a></li>
        </ul></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp">Recipe and Stocks Help</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#recipeSearch">Search</a> for a Recipe</li>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#recipeView">View a PiMS Recipe record</a></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#recipeCreate">Record a new Recipe</a>
                <ul>
                   <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#addCategory">Set sample category</a></li>
                   <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#addComponents">Add components</a></li>
                </ul></li>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#copyRecipe">Copy</a> a Recipe</li>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#recipeEdit">Edit</a> the details for a Recipe</li>
            <li><a href="${pageContext.request.contextPath}/help/HelpRecipeAndStock.jsp#createStock">New Sample</a> or Reagent Stock</li>
        </ul></li>
     </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="user" initialState="open" title="User and Access permissions" extraHeader="${top}" >

 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/AccessRights.jsp">Access Right</a></li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="reference" initialState="open" title="Reference data" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp">Reference Data Help</a>
         <ul>
           <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp#navRefData">Navigate</a> to Reference Data</li>
           <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp#listRefData">Viewing</a> Reference Data</li>
         </ul></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp#searchRefData">Searching</a> the Reference data</li>
       <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp#updateRefData">Updating</a> Reference data</li>
       <li><a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp#useRefData">Using</a> Reference data</li>
    </ul>
 </div>
</pimsWidget:box>

<pimsWidget:box id="oppf" initialState="open" title="OPPF -specific help" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOppfFunctions.jsp">Oppf Help</a></li>
       <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOperonOrderForm.jsp">Primer Ordering</a>
            <ul>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOperonOrderForm.jsp#design">Designing the Plate Experiment</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOperonOrderForm.jsp#import">Importing Targets and Constructs</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOperonOrderForm.jsp#create">Creating the Order Experiment</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpOperonOrderForm.jsp#select">Selecting the Order Experiment</a></li>
            </ul></li>
        <li><a href="${pageContext.request.contextPath}/help/oppf/HelpScorePlate.jsp">Scoring Plate Results</a>
            <ul>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpScorePlate.jsp#parameters">Score Plate Parameters</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpScorePlate.jsp#action">Score Plate in Action</a></li>
            </ul></li>
   </ul>
 </div>
 <div class="rightcolumn">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/oppf/HelpSequencePlate.jsp">Recording Sequencing Results</a>
            <ul>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpSequencePlate.jsp#parameters">Sequencing Plate Experiments</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpSequencePlate.jsp#recording">Recording Sequencing Results</a></li>
             <li><a href="${pageContext.request.contextPath}/help/oppf/HelpSequencePlate.jsp#results">Examining Sequence Plate Results</a></li>
            </ul></li>
    </ul>
 </div>
</pimsWidget:box>


<pimsWidget:box id="expert" initialState="open" title="Expert -generic user interface" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/HelpDeleteRecord.jsp">Delete Record</a></li>
    </ul>
 </div>
 <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/HelpViewRecord.jsp">View Record</a>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/HelpViewRecord.jsp#navigation">Navigation</a></li>
          <li><a href="${pageContext.request.contextPath}/help/HelpViewRecord.jsp#specDetails">Specific details</a></li>
          <li><a href="${pageContext.request.contextPath}/help/HelpViewRecord.jsp#assocDetails">Details for associated records</a></li>
          <li><a href="${pageContext.request.contextPath}/help/HelpViewRecord.jsp#addAssocDetails">Associating additional records</a>
        </ul></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpListRecords.jsp">List Records</a>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/HelpListRecords.jsp#viewList">View</a> a list of PiMS records </li>
          <li><a href="${pageContext.request.contextPath}/help/HelpListRecords.jsp#pageControls">Page display</a> Controls</li>
          <li><a href="${pageContext.request.contextPath}/help/HelpListRecords.jsp#exportOptions">Export</a>  options</li>
          <li><a href="${pageContext.request.contextPath}/help/HelpListRecords.jsp#customLists">Custom</a>  lists</li>
        </ul></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpSearchRecords.jsp">Search Records</a>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/HelpSearchRecords.jsp#searchForm">Search form</a></li>
          <li><a href="${pageContext.request.contextPath}/help/HelpSearchRecords.jsp#searchResults">Search results</a></li>
          <li><a href="${pageContext.request.contextPath}/help/HelpCreateRecord.jsp#createAssoc">Associations</a> -searching for records </li>
        </ul></li>
    </ul>
 </div>
</pimsWidget:box>

<%--CONTENTS BOX TEMPLATE
<pimsWidget:box id="" initialState="open" title="" extraHeader="${top}" >
 <div class="leftcolumn">
    <ul>
    </ul>
 </div>
 <div class="leftcolumn">
    <ul>
    </ul>
 </div>
</pimsWidget:box>
--%>

<%--END NEW FORMAT --%>

</div>
<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
