<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
     <jsp:param name="HeaderName" value='Using PiMS Target Management' />   
     <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="target.png" />
<c:set var="title" value="Target Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

 <div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
   <div class="help">
    <p style="padding-left:.6em;">
    PIMS Target management allows you to record the details and track the 
    progress of your Targets and Constructs.<br />
    Information recorded for Targets can include target name(s), function, 
    nucleotide and amino-acid sequence, source organism, references to external databases 
    and details of relevant literature references.<br /><br />
    Support for managing <a href="${pageContext.request['contextPath']}/help/target/HelpDnaTarget.jsp">non-Protein Targets</a> was added in PiMS version 2.1<br />
    and for <a href="${pageContext.request['contextPath']}/help/target/HelpNaturalSourceTarget.jsp">Natural Source Targets</a> in PiMS version 2.2<br />
    </p></div>
  </pimsWidget:box>

  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
    <ul>
      <li><a href="#targFunc">Target Functions page</a></li>
      <li><a href="#locateTarget">Locating PiMS Targets</a>
       <ul>
        <li><a href="#targList">Target List</a></li>
       </ul>
      </li>
      <li><a href="#targetDetails">Target details</a>
       <ul>
        <li><a href="#editTarg">Edit Target</a></li>
       </ul>
      </li>  
      <li><a href="#targRelatedDetails">Target-related details</a>
       <ul>
        <li><a href="#sequences">Sequences</a>
         <ul>
          <li><a href="#protParam">Link to ExPASy ProtParam tool</a></li>
          <li><a href="#addSeq">Add a missing sequence</a></li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpRecordDNASeq.jsp">Standards for DNA sequences</a></li>
         </ul>
        </li>
        <li><a href="#constructList">Constructs</a></li>
        <li><a href="#targExp">Target Experiments</a></li>
        <li><a href="#syntheticGenes">Synthetic genes</a></li>    
        <li><a href="#dbRefs">External Database links</a></li>    
        <li><a href="#dbQueries">Database queries</a></li>
        <li><a href="#imageAttach">Images and Attachments</a></li>
        <li><a href="#notes">Notes</a></li>
        <li><a href="#diagramView">Diagram view</a></li>
       </ul>
      </li>
     </ul>
     </div> 
    <div class="rightcolumn">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp">New Target</a></li>
      <li>
        <ul>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp#downloadTarget">Download</a> -from file or database</li>
          <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp#downloadTarget">Create</a> manually -with a form</li>            
        </ul>
      </li>
      <li>Support for <a href="${pageContext.request.contextPath}/help/target/HelpSyntheticGene.jsp">Synthetic genes</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpTargetScoreboard.jsp">Target scoreboard</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpTargetReports.jsp">Reports</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp">Sequence Similarity search</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpDnaTarget.jsp">DNA Targets</a> non-Protein Targets</li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNaturalSourceTarget.jsp">Natural Source Targets</a> -with no sequence</li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpComplex.jsp">Target Complexes</a></li>
    </ul>
    <h4>Construct Help</h4>
    <ul>  
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">New Construct</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#constructDetails">Construct Details</a></li>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpExtensions.jsp">5'-Extensions</a> for Primers</li>
      <li><a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp">Ordering Primers</a></li>
      <li><a href="${pageContext.request.contextPath}/help/HelpMutagenesis.jsp">Site Directed Mutagenesis</a><br /></li>
     </ul>
   </div>
   <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="closed" title="Related Help">
    <ul>
       <li><a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp">Construct</a> Help</li>
       <li><a href="${pageContext.request.contextPath}/help/HelpTargets.jsp">Guide</a> to expert Target management</li>
       <li><a href="${pageContext.request.contextPath}/help/target/HelpSyntheticGene.jsp">Synthetic genes</a> Help</li>
       <li><a href="${pageContext.request.contextPath}/help/target/HelpBlastTarget.jsp">Blast</a> searches in PiMS</li>
    </ul>
  </pimsWidget:box>
</div>
 
<div class="helpcontents">
 <h3 id="targFunc">PiMS Functions for Targets</h3>
 <div class="textNoFloat">
 Details of the PiMS functions relating to Targets can be view on the &quot;
 Target Functions&quot; page.<br />
 This is reached by clicking &quot;Target&quot; in the horizontal menu bar near the 
 top of each page in PIMS, or by selecting &quot;More...&quot; from the Target 
 drop-down menu.
 <br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetFunctions.png" alt="Target functions page" />
 <br />
 This page contains links to the functions which are also listed in the Target menu:<br />
 Search Targets, New Target, Download Target, New DNA Target, Search Complexes, New Complex,
 Target Groups, Search Lab Notebooks, Similarity Search, Scoreboard, Reports.<br /><br />
 In addition there are links to Search for Molecules (DNA and Protein),  
 Database references and relevant Reference data such as  Organisms (Species or Natural source), 
 Target statuses and Database names.<br /><br />
 <!--TODO Filtering -->
 </div> <!--end div class="textNoFloat"-->
<div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="locateTarget">Locating a Target in PiMS</h3>
 <div class="textNoFloat">
 To locate a PiMS Target record you can either:<br /><br />
 <ul>
  <li>select the Target from the list displayed when you click &quot;Targets&quot; in the breadcrumb trail at the 
  top of Target details page<br />   
  <em>-see <a href="#targList">Target list</a></em></li>
  <li>select the Target from the list of &quot;Targets found&quot; which is displayed when you select 
  &quot;Search Targets&quot; from the Target menu or click the &quot;Search&quot; link in the Target Functions page</li>
  <li>or, if you have recently viewed the Target record, select it from your History list
  <em>-see <a href="${pageContext.request.contextPath}/help/HelpMRU.jsp">History</a> help</em></li>
 </ul>
 <h4 id="targList">Target List</h4>
 To see a list of all Targets currently recorded in PiMS, navigate to a target details page and click &quot;Targets&quot;
 in the &#39;breadcrumb&#39; locator at the top of the page.  <em>-see <a href="#targetDetails">Target details</a></em>
 <br />
 A list of Targets is also displayed when you select &quot;Search Targets&quot; from the Target drop-down menu .<br />
 You will see a page similar to the one below which states the number of Targets recorded 
 in PiMS.<br /><br />
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/browseTargets.png" alt="List of all Targets in PiMS" />
 <div class="textNoFloat">
 <br />
 Each row of the table contains details for one Target.<br />
 This includes information which was entered or selected when the Target was recorded 
 in PiMS: <br />
 The Target name (or identifier), the Protein name (or sequence name for a non-protein Target), a list of Aliases 
 or other names for the Target, the Function description, Comments the name of the Lab Notebook and any Target groups
 the Target belongs to.<br />
 <em> -for help see <a href="#createTarget">New Target</a></em>.
 <br /><br /> 
 The &#39;Status&#39; of each Target is also shown which represents the stage in the 
 protein production and crystallisation pipeline as defined  by TargetDB, and the date 
 this was achieved.
 <br /><br />
 To view the detailed record for a Target, click the link in the second column of the appropriate row 
 or, search for the Target.<br /> <em>- in the Target menu, choose Search Targets.</em>
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="textNoFloat -->
 <div class="toplink"><a href="#">Back to top</a></div>

  
 <h3 id="targetDetails">Target Details</h3>
 The Target details view page includes an icon, identifying the view as a PiMS Target, and updatable information 
 relating to an individual Target.<br />
 This includes the Target names, source organism, function, the name of the scientist responsible for the Target and 
 any additional comments.<br />  
 &nbsp; -the example below shows the top part of the Target details view page for Target &#39;sso1440&#39;<br />
 <div class="textNoFloat"> 
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetDetailsTop.png" alt="target details" />
 <br />
 <ul>
  <li>Click <a href="javascript:void(0)"><strong>Targets</strong></a> in the breadcrumb trail to display a list of all Targets 
  recorded in PiMS. <em>-see <a href="#targList">Target List</a></em><br /><br />
  </li>
  <li>Click <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif" /> <a href="javascript:void(0)"><strong>Diagram</strong></a> 
  to see a graphical representation of the target and its use.
  &nbsp; <em>-see <a href="#targDiagram">Target Diagram</a></em><br /><br /></li>
  <li>If you have permission to delete the Target record you will see a green delete icon and link
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete icon" /> <a href="javascript:void(0)"><strong>Delete</strong></a><br />
   &nbsp; this is not the case in the example where <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" alt="Can't delete icon" /> <strong><span style="color:grey">Can't delete</span></strong> is shown<br /><br /></li>
  <li>In open box labelled &#39;Target Details&#39;, click the context menu link next the the Organism name<br />
   <em> -Bacillus anthracis <img src="/pims_current/skins/default/images/icons/misc/pulldown.gif"/> in the example</em><br />
   You will see links to both the PiMS record for the organism and the NCBI taxonomy record.<br /><br /></li>
  <li id="editTarg">To edit the Target details click <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make changes...</strong>
  <em> -this will not be visible if you do not have permission to edit the Target details.</em><br />
  You will see the editable version of the target details box.<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/editTargetDetails.png" alt="edit target details" />
  <br />
  When you have finished editing, click the <input class="button" value="Save changes" type="submit"/> 
  button or click <span style="font-size:smaller">(Cancel editing)</span> to revert any changes you have made.<br /><br />
  </li>
 </ul>
 </div> <!--end div class="textNoFloat -->
 <div class="toplink"><a href="#">Back to top</a></div>

 <!-- TARGET RELATED DETAILS -the other boxes -->
 <h3 id="targRelatedDetails">Target-related details</h3>
 Below the open Target details box there are a number of closed boxes or horizontal bars for Target-related items.<br /><br />
  <div class="textNoFloat"> 
 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetDetailsbottom.png" alt="Target-related details" /><br />
 To examine the contents of each box, click the plus <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/>  icon.
 <br /><br />
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- SEQUENCES -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="sequences">Seqences</strong> contains the DNA and Protein sequences for the Target.<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetSequences.png" alt="Target sequences box" />
  <br />
  Values for the <strong>Length</strong> and <strong>&#037;GC</strong> of the DNA sequence are displayed, and 
  for the protein sequence, values for the <strong>Length</strong>, <strong>Weight</strong> in Daltons, Molar <strong>Extinction</strong> coefficient, 
  the <strong>Absorbance</strong> of a 0.1% solution and the <strong>pI</strong> are also displayed.<br /><br />
  For each sequence there is also a <a href="javascript:void(0)">Fasta Pop-up</a> link to 
  display the Fasta-formatted sequence in a pop-up window, with an additional <a href="javascript:void(0)">Pop-up</a> link to display the Reverse complement of the DNA sequence.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/seqPopup.png" alt="Sequence pop-up window" />
  <br /><br />
  If the translated Target DNA sequence and the Target protein sequence differ, an additional link to display an alignment between the two will be shown:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/mismatchAlignment.png" alt="Sequence alignment in pop-up window" />
  <br />

  <div id="protParam">If the Target has a protein sequence recorded for it then there will be a link to submit the sequence to 
  the ExPASy ProtParam tool.<br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/protParmLink.png" alt="Link to ProtParam" />
  <br />
  Clicking this link opens the results in a new browser tab or window.</div>
  <br /><br />
  
  You may edit the Target sequences by clicking <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make changes...</strong> in the box.<br />
  &nbsp; <em> -<strong>note:</strong> if your Target lacks either a DNA or Protein sequence, PiMS
  will allow you to add the missing sequence.</em><br /><br />
  
  If your Target DNA sequence starts with a non-standard start codon 'GTG', 'CTG, or 'TTG'  a message willl be displayed in the Sequences box:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targNonStandardStart.png" alt="Sequence pop-up window" /><br />
  You do not need to edit the sequence but when you record a Construct the start codon will not be recognised as coding Methionine.
  
  <h4 id="addSeq">Add a missing Sequence to a Target</h4>
  If your target lacks a DNA and /or protein sequence the &#39;Sequences&#39; box will look something like this:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/noSequences.png" alt="Empty Sequences box" />   
   <ul>
    <li>Click <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make changes...</strong><br />
        <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/addSequences.png" alt="Empty Sequences box edit mode" />   
    </li>
    <li>Paste the sequence into the appropriate box<br /><br /></li>
    <li>Click <input class="button" value="Save changes" type="submit"/><br /><br /></li>
   </ul>
   &nbsp; <strong>note:</strong> see <a href="${pageContext.request.contextPath}/help/target/HelpRecordDNASeq.jsp">DNA sequences</a> for further information about recording DNA sequences in PiMS
   
   <div class="toplink"><a href="#">Back to top</a></div>


  <!-- CONSTRUCTS -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="constructList">Constructs</strong> contains a list detailing the Construct records for the target.<br />
  The list includes links to Experiments recorded for each Construct and the latest &quot;Milestone&quot; which has been achieved.<br />
  In PiMS a <strong id="newMilestone">Milestone</strong> can be set when the success of certain Experiments are recorded.  
  When a Milestone is achieved the Target is progressed along the protein production pipeline.  Target Milestones relate to the 
  &quot;Status&quot; of a Target defined in TargeDB.<br />
  &nbsp; -the example list contains two constructs, one of which has achieved the milestone &#39;PCR&#39;<br /><br />
  &nbsp; <strong>note:</strong> If the Target has no Construct records, you will see 
  &#39;No constructs found!&#39; in place of the list<br />
   <div class="textNoFloat"> 
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constructList.png" alt="construct list" />
 <ul>
  <li>To see the details of an existing construct, click on the link in the first column of the list<br />
  &nbsp; -for help see <a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#constructDetails">Construct Details</a>
  <br /><br /></li>
  <li>To see a list of all Experiments for a particular construct, click on <a href="javascript:void(0)">All Experiments</a> 
  in the relevant row of the list<br />
  &nbsp; -for help see <a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#constructResults">Construct Experiments</a>
  <br /><br /></li>
  <li>To record a new Experiment for a construct, click <a href="javascript:void(0)">Add New Experiment</a><br />
  &nbsp; -for help see <a href="${pageContext.request.contextPath}/help/target/HelpConstruct.jsp#newExp">New Experiment</a><br /><br /></li>
  <li>To record a new construct click <a href="javascript:void(0)">Design new Construct</a> in the box header<br />
  &nbsp; -for help see <a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">New Construct</a><br />
  <br />
  <strong>note:</strong> If your Target record was created without a DNA sequence 
  you will <em>not</em> usually see this link, as it is <em>not</em> possible to record a Construct without a DNA sequence.
  &nbsp; <em> -see <a href="#addSeq">Add a Sequence</a> to a Target</em><br />
  Instead you will see:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetNoDNASeq.png" alt="Construct box for target without DNA sequence" />
  </ul> 
 </div> <!--end div class="textNoFloat -->
  <br />
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- EXPERIMENTS -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="targExp">Experiments</strong> contains a list of Experiment records for the target.<br />
  <!-- TODO NEW IMAGE --><img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetExperiments.png" alt="Target experiments table" />
  <br /><br />
  The details displayed for each Experiment can include:<br />
  &nbsp; <strong>Milestone</strong> -a link to the Target Milestone record if one has been achieved <em>-see <a href="#newMilestone">New Milestone</a></em><br />
  &nbsp; <strong>Experiment</strong> -a link to the PiMS Experiment record <em>-see <a href="../experiment/HelpExperiments.jsp#viewExp">Experiments help</a></em><br />
  &nbsp; <strong>Type of Experiment</strong> -a link to the description of PiMS experiment type, if known<br />
  &nbsp; <strong>Experiment Date</strong> -the date the Experiment was recorded in PiMS<br />
  &nbsp; <strong>Protocol Used</strong> -a link to the details of the PiMS protocol (Experiment template) used<br />
  &nbsp; <strong>Experiment Status</strong> -this can be &#39;To be run&#39;, &#39;In process&#39;, &#39;OK&#39; or &#39;Failed&#39;<br />
  <br />
  &nbsp; <strong>note:</strong> if there are no Experiments recorded for the Target, you
  will see &#39;There are no experiments on this Target&#39; in the Experiments box<br /><br />
  To record a new Experiment for the Target, click the <a href="javascript:void(0)">New Experiment</a> link
  in the box header.<br />
  &nbsp; -for help see <a href="../experiment/HelpExperiments.jsp#recordSimpleExp">New Experiment</a><br /><br />
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- SYNTHETIC GENES -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="syntheticGenes">Synthetic genes</strong> is only displayed if you have recorded any Synthetic genes for your Target. 
  <br />
   In the example the list contains the details for 3 records.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/synthGenesBox.png" alt="Synthetic genes table" />
  <br />
    &nbsp; <em>-see Help for <a href="../target/HelpSyntheticGene.jsp">Synthetic genes</a> in PiMS for more details</em><br /><br />   
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- TARGET GROUPS TODO NO HELP FOR THIS -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="targetGroups">Target groups</strong> PiMS allows you to group Targets according to any criteria you choose.<br />
  You need to create a Target group first then you can add your Targets to it. 
  <br />
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- DATABASE REFS -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="dbRefs">External Database links</strong> contains a list detailing records in remote databases for the target.<br />
  In the example the list contains the details for 1 record.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetDbRefs.png" alt="Target DbRefs table" />
  <br />
  The details displayed for each Externel Database link can include:<br />
  &nbsp; <strong>Database</strong> -the name of remote database -if known<br />
  &nbsp; <strong>Accession</strong> -the unique identifier for the record in the remote database, provides a link to the actual record<br />
  &nbsp; <strong>Type</strong> -the type of record, e.g. sequence, literature reference etc.<br />
  &nbsp; <strong>Sequence release</strong> -the release number of the database<br /><br />
  In the example, the GenBank record was created from the GI number entered when the Target was recorded.<br />
  Click the <a href="javascript:void(0)">73919642</a> link to see the NCBI report page for the Target protein.
  <br /><br />
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- DATABASE QUERIES -->
  <br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong id="dbQueries">Database queries</strong> provides links to submit your Target sequences to query certain remote databases<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/databaseQueries.png" alt="Database queries box" />
  <ul>
   <li>Clicking the links labelled 
    <a href="javascript:void(0)">PDB Blast results</a> or <a href="javascript:void(0)">TargetDB Blast results</a> initiates Blast searches for the Target protein 
    sequence against the PDB or TagetDB respectively<br />
    &nbsp; <em>-see <a href="../target/HelpBlastTarget.jsp">PiMS Blast</a> searches for more details</em>
    <br /><br /></li>
    <li>Clicking <input type="submit" value="Submit Taro query" /> submits the Target protein sequence to the University of Dundee's 
    Target Optimisation Utility</li>
  </ul>
<div class="toplink"><a href="#">Back to top</a></div>

  <!-- IMAGES, ATTACHMENTS AND NOTES -->
  <h3 id="imageAttach">Images, Attachments and Notes</h3>
  Boxes labelled &quot;Images&quot;, &quot;Attachments&quot; and &quot;Notes&quot; are found on most PiMS view pages.<br /><br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong>Attachments</strong> allows you to link Files with the Target.<br />
   To make a link from the target to a <strong>file</strong> on your network, open the Attachments box, 
   then click the <input class="button" value="Browse..." type="submit"/> 
   button and navigate to the appropriate file.  Enter a description for the file then click 
   <input class="button" value="Upload File" type="submit"/><br />
   <br />
   The box will be re-displayed with a link to the uploaded file (called Target data.doc in the example below).<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/uploadedFile.png" alt="Link to uploaded file" />
   <br />
   You can update the file details by clicking <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make changes...</strong><br /><br />
   You may also delete the file by clicking the green delete <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete icon" /> icon.<br /><br />
   If you do not have permission to edit the Target, you will not be able to make a 
   link to a file.<br />You will see e.g. &#39;You do not have access rights to attach files to 000002&#39; in place of the Browse button.<br /><br />
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong>Images</strong> allows you to associate images with the Target.<br />
  This is similar to Attachments but you can also record a title and legend for 
  the image.<br />
  These are displayed in a table with a clickable thumbnail to the original image.<br /><br /> 
  
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.gif" alt="plus icon for boxes"/> 
  <strong>Notes</strong> can be used to keep a record of changes to a Target in PiMS. A note contain the date it was recorded
  by whom (the default is the id of the person currently logged in) and a short description.<br />
  A default note is updated each time the Target is viewed, with the date and time, but this is not saved automatically.<br /><br />
  The example shows a note dated 03/12/08 and the default note. 
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetNotes.png" alt="Notes box" />


 </div> <!--end div class="textNoFloat -->
<div class="toplink"><a href="#">Back to top</a></div> 
 
 <h3 id="diagramView">Diagram view</h3>

 <div class="textNoFloat">
  PiMS provides a visual representation of Targets and  Constructs and their use -the Diagram View.<br />  
  This can be accessed from the
               <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" /> link on a Target or Construct details page.<br />
  The Diagram view provides a simple form of navigation between Target, Construct and 
  Experiment records in PiMS.<br /><br />
  
  <strong>Research Objective</strong><br />
  For each new Target recorded in PIMS, a &quot;Research Objective&quot; with a 
  single &quot;Research Objective element&quot; is automatically created.<br />
  A Research Objective defines what you are planning to work on.<br />
  This might be a Target, or combination of Targets, Target-domains and /or other molecules, 
  where each of these is a Research Objective element.
  <br /><br />
  So, in the case of a new Target, a new Research Objective record is created using the 
  Target's &#39;Name&#39; as an identifier.  The Target itself is a default Research Objective element
  representing the &#39;Full length&#39; sequence, within this Research Objective.&nbsp;
  Similarly, when you create a new Construct record, another new Research Objective
  record is created using the Construct's name as an identifier.  In this 
  case the construct is a Research Objective element representing the protein you intend to express.
  <br /><br />
  When you record the details for an Experiment in PIMS, the Experiment is linked to 
  the Target via a Research Objective.
<div class="toplink"><a href="#">Back to top</a></div> 

 <h4 id="targDiagram">Target Diagram</h4>
  Targets, Research Objectives and Experiments are represented by different shaped symbols linked by arrows. 
  <br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/diagramKey.png" alt="Key to the Shapes in diagrams" />
  <br /><br /> 
  The example below shows the result of clicking the
  <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" /> link from the Target details page for Target 000914.<br />
 <div class="indent">
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/spotTargetDiagram.png" alt="Target diagram" /></div>
 <ul>
  <li>The <strong>Target</strong> is represented by an irregular <strong>pentagon</strong>
  with a red border labelled: &#39;Target&#39;, followed by the Target's &#39;Name&#39; <em>-000914 in the example.</em><br />
  &nbsp; -click this symbol to return to the Target details page.<br /><br /></li>
  <li>Blue <strong>octagons</strong> represent Constructs which are also called <strong>Research Objectives</strong> 
  <em> &nbsp; -see <a href="${pageContext.request.contextPath}/help/target/HelpTarget.jsp#diagramView">Research Objective</a> for an explanation</em><br />

  Two Research Objectives are shown: one represents a Construct '000914.con1' and the other is the &#39;Default&#39; 
  Research Objective representing the Full length Target<br />
  &nbsp; -click this symbol to see the relevant Target or Construct details page.<br /><br /></li>

  <li><strong>Experiments</strong> which have achieved a Milestone are represented by blue <strong>ellipses</strong> 
  labelled with the <strong>Experiment type</strong> and the &#39;Name&#39; of the Experiment.<br />
  The example shows that 2 Experiments performed using Construct 000914.con1 have achieved Milestones.<br />
    &nbsp; -click this symbol to see the full details for the Experiment.<br /> 
  <em> -these would also be listed in the Experiments box for the Construct and its parent Target.</em>
  <br /><br /></li>
  <li>A brief description of the specific items in a diagram can be seen in a tooltip when you mouse-over the symbol.
  <br /></li>
 </ul>
<div class="toplink"><a href="#">Back to top</a></div> 

 <h4 id="constDiagram">Construct Diagram</h4>
 Clicking the
  <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" />link from the Construct details page for Construct 000914.con1.
 <div class="indent">
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/spotConstDiagram.png" alt="Construct diagram" />
 </div>
  This is a subset of the Target diagram only showing symbols which are relevant to the particular Construct.
<div class="toplink"><a href="#">Back to top</a></div> 
 
 <h4 id="constExptDiagram">Construct Experiments diagram</h4>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constDesignExptDiagram.png" alt="Construct design diagram" /><br /><br />
  
  The White ellipse with a red border represents the <strong>Construct Design</strong> Experiment, which is 
  recorded automatically when you create a new Construct.<br />
  Clicking the ellipse will open an Experiment view page displaying the full details.
  <em>-see <a href="../experiment/HelpExperiments.jsp#viewExp">View</a> Experiment details.</em><br /><br />
  
  The 3 diamonds are links to the three &#39;Output Samples&#39; from the Experiment:<br />
  <strong>Template:</strong> Construct nameT, <strong>Forward primer:</strong> Construct nameF and 
  <strong>Reverse primer:</strong> Construct nameR.<br /><br />
   
  Each time you record a new Experiment for the Construct, an extra ellipse 
  will be added to the diagram, labelled with the name of the Experiment. 
 </div> <!--end div class="textNoFloat -->
<div class="toplink"><a href="#">Back to top</a></div>
 </div> <!--end div class="helpcontents"-->

</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>
 
<jsp:include page="/JSP/core/Footer.jsp" />
