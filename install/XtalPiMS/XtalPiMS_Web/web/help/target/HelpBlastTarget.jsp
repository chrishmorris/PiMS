<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Blast searches in PiMS ' />
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target Help</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Blast Searches in PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help" id="top">
 <pimsWidget:box initialState="fixed" title="Overview">
    Blast (Basic Local Alignment Search Tool) searches of the PDB (Protein Structure sequence database)
    and TargetDB (Structural Genomics Targets database) can be performed for PiMS Targets.<br />  

    The Blast results are presented in a summary table containing links to the individual 
    &#39;Hit&#39; records and sequence alignments. 
    <br /><br />
    PiMS Blast searches are available via web-service access to the EBI-hosted NCBIBlast program.  
    Consequently, the latest versions of the databases and search tool are used.<br />
    &nbsp; <em> -see <a href="http://nar.oxfordjournals.org/cgi/content/full/gkm291v1?ijkey=HLk7PzQHQ2C7dmC&amp;keytype=ref">
    Web Services</a> at the European Bioinformatics Institute for more details.</em>
 </pimsWidget:box> 
 <pimsWidget:box initialState="fixed" title="Contents">
  <div class="leftcolumn">
  <ul>
   <li><a href="#runBlast">Performing Blast searches</a> in PiMS
    <ul>
     <li><a href="#parameters">Default search parameters</a></li>
    </ul>
   </li> 
   <li><a href="#output">Blast output</a>
    <ul>
     <li><a href="#summary">Summary table</a></li>
     <li><a href="#alignments">Alignments</a></li>
    </ul>
   </li>
  </ul>
  </div>
  <div class="rightcolumn">
   <ul>
    <li><a href="#emblBlast">EMBL Blast</a> for non-Protein Targets</li>
    <li><a href="${pageContext.request.contextPath}/help/target/HelpAutomatedBlast.jsp">Automated Blast searches</a></li> 
   </ul>
  </div>
 </pimsWidget:box>
</div> <!--end div help-->

<div class="helpcontents">
<div class="toplink"><a href="#">Back to top</a></div>
 <h3 id="runBlast">Performing Blast searches in PiMS</h3>
  <div class="textNoFloat">
   <ul>
    <li>First navigate to the Details page for the appropriate Target.<br />
    <strong>either: </strong>from the Target menu select &quot;Search Targets&quot; and perform a PiMS search.<br />
        &nbsp; In the resulting table, click the             <pimsWidget:linkWithIcon 
                icon="types/small/target.gif" 
                url="JavaScript:void(0)"
                text="Target name" />link in the first column of the relevant row.
   		<br />
   	 <strong>or: </strong>if you have recently visited the Target's details page, just select it from your 
   		&quot;History&quot; menu.<br />
   		&nbsp;  <em>-see <a href="${pageContext.request.contextPath}/help/HelpMRU.jsp">History</a> help for more details.</em>
  	<br /><br /></li>
  	
  	<li>Open the box labelled <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.png" alt="plus icon for boxes"/> 
   <strong>Database Queries</strong><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/targetBlast.png" alt="Blast search box" /><br />
  	<li>If your Target has no protein sequence, the box will not be displayed<br /><br /></li>
  	
  	<li>Click the link labelled &#39;PDB Blast results&#39; to initiate a Blast search 
  	for the Target against the PDB database or <br />
  	click the button labelled &#39;TargetDB Blast results&#39; to initiate a Blast search 
  	for the Target against the TargetDb database.<br /><br /></li>
  	<li>After a short delay, the Blast results will be displayed.</li>
   </ul>		
<div class="toplink"><a href="#">Back to top</a></div>

  <h4 id ="parameters">Default search parameters</h4>
  PiMS blast searches are run with the following default parameters:
   <ul>
    <li><strong>Program:</strong> blastp <em>to compare a protein sequence against a 
    protein database</em></li>
    <li><strong>Matrix:</strong> BLOSUM62</li>
    <li><strong>Number of hits and alignments retrieved:</strong> 200</li>
    <li><strong>Expect (E value):</strong> 10</li>
    <li><strong>Low complexity filter:</strong> off</li>
   </ul>
  </div> <!-- end div class="textNoFloat" -->
<div class="toplink"><a href="#">Back to top</a></div>
  
 <h3 id="output">Blast output</h3>
  <div class="textNoFloat">
  The output comprises a Summary table, displaying the name of the database searched, the date the search was 
  performed and information describing any entries in the searched database (Hits) which match the search parameters.<br />
  In addition, alignments between the Target and Hit sequence are also displayed.
  
  <h4 id="summary">Summary table</h4>
  The summary table from a PiMS Blast search, for Target BACAN_GLMS, against the PDB 
  database is shown below<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/blastSummary.png" alt="Blast Results Summary table" /><br />
  
  <ul>
   <li>The column headed <strong>No.</strong> provides a link to the corresponding 
   alignment(s) between the Target and the Hit sequence.<br />
   &nbsp; <em>-see <a href="#alignments">Alignments</a> help for more details</em></li>
   <li>Clicking the link in the column headed <strong>Hit ID</strong> will open a new 
   browser tab to display the database record.</li>
   <li><strong>Description</strong> -a short description of the Hit.  For PDB records 
   this includes the molecule type (protein), length and a name.</li>
   <li><strong>Length</strong> -the length of the Hit</li>
   <li><strong>Score</strong> -a measure of the similarity between the Target and Hit 
   sequence based on the similarity scoring matrix used.<br />
   &nbsp; <em>-BLOSUM62 matrix in this case.</em></li>
   <li><strong>Identity&#037;</strong> -the number of amino acids which are identical between 
   the Target and the Hit as a &#037; of the Target length. <em>-&#037; identity over Target length</em>
   <br /></li>
   <li><strong>Positives&#037;</strong> -the &#037; of identical or related amino acids 
   over the region of the match.</li>
   <li><strong>Evalue</strong> -the Expectation that this number of hits with this score 
   would be found by chance in a database of this size.<br />
   &nbsp; <em>e.g. an Evalue of 1 means you would get <strong>1</strong> Hit, by 
   chance, with this score using this target in this particular database</em><br /><br /></li>
   <li>&nbsp; <strong>note:</strong> to view records for Blast searches against the 
   TargetDB, click the Search <a href="javascript:void(0)">TargetDB</a> link in the table
   header.<br />&nbsp; You will be redirected to the TargetDB search page and you will need to enter 
   the Hit ID in the resulting search form.<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/blastTDBSummary.png" alt="TargetDB blast summary" /><br />
   <br />
       <em>-in the example 2 hits are highlighted in yellow which indicates &gt;50&#037; identity </em><br /><br />
   </li>
   <li>If no matching Hits are found in the searched database, you will be informed as 
   in the example below.<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/blastNoHits.png" alt="No Blast hits found" /><br /><br />
   </li>
   <li>If the search fails, you will the message below.<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/blastFailure.png" alt="Blast failure" />
   </li>   
  </ul>
<div class="toplink"><a href="#">Back to top</a></div>

  <h4 id="alignments">Alignments</h4>
  For each Blast Hit, PiMS displays the alignment between the Target and Hit protein 
  sequences in a table.<br />
  The alignment tables are located at the end of the Summary table.  You can view them 
  by scrolling to the bottom of the Summary table or by clicking the &quot;View 
  Alignments&quot; link at the top of the Summary table.<br/>
  There is also a numbered link to each individual alignment table in the first column 
  of the Summary table.<br /><br />
  An example alignment is shown below.  This is part of the alignment between the Target BACAN_GLMS
  and first Hit from the Blast search.<br />
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/blastAlignment.png" alt="Blast Results Summary table" /><br />
  
   <ul>
    <li>The lengths of the Target and Hit sequences are shown, along with the length, 
    score and E value for the alignment.  Also shown are the number of 
    identical amino acids, the number of identical and similar amino acids (positives) 
    and the number of gaps in the region of the alignment.<br /><br /></li>
    <li>Below this are values for the &#037; of identical residues in the Hit, alignment 
    and Target and the length of the Hit as a &#037; of the Target length.<br /><br /></li>
    
    <li>The sequence alignment is displayed in three rows.  The top and bottom rows are 
    labelled target: and hit: and these are separated by a pattern of identical and similar
    (<span class="required">+</span>) residues in red.</li>
   </ul>
   
   </div> <!-- end div class="textNoFloat" -->
<div class="toplink"><a href="#">Back to top</a></div>
 
 <h3 id="emblBlast">EMBL Blast for non-Protein Targets</h3>
  <div class="textNoFloat"> 
   For non-Protein Targets PiMS provides functionality to run Blast searches against the 
   EMBL nucleotide sequence database.<br />
   <ul>
    <li>Navigate to the Target Details View for a non-Protein or DNA Target.<br />
    Immediately below the Target details table you will see a link<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/emblBlastLink.jpg" alt="EMBL Blast link" /><br /></li>
    <li>Click the link to initiate a Blast search for the Target against the EMBL database.<br /><br /></li>
  	<li>After a short delay, the Blast results will be displayed.<br /></li>
   </ul>
   The results are similar to those for Protein-Target Blast searches against the PDB and TargetDB<br />
   &nbsp; except that n/a is displayed in the column headed Positives&#037;.<br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/emblBlastTable.png" alt="EMBL Blast Result" /><br /><br />
   The alignments also differ slightly from those for Blast searches against the PDB and TargetDB:<br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/emblBlastAlignment.png" alt="EMBL Blast Result" /><br />
    <ul>
     <li>n/a is displayed in the column headed Positives<br /><br /></li>
     <li>The DNA strand of the matching Hit is indicated by <em>plus strand</em> or <em>minus strand</em> in the bottom rhs of the details<br />
     &nbsp; <em>plus strand</em> in the example<br /><br /></li>
     <li>Matching nucleotides are indicated by a red <span class="required">|</span> symbol between the Target and Hit sequence rows.<br /></li>
    </ul>   
   </div> <!-- end div class="textNoFloat" -->
  <div class="toplink"><a href="#">Back to top</a></div>
  </div> <!--end div class="helpcontents"-->

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />