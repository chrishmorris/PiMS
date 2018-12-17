<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Automated Blast searches in PiMS ' />
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target Help</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpBlastTarget.jsp">Blast Searches</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Automated Blast Searches in PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help" id="top">
<pimsWidget:box initialState="fixed" title="Overview">
You can configure PiMS to run scheduled, automated Blast (Basic Local Alignment Search Tool) searches 
for all Targets in your database, against the PDB (Protein Structure sequence database) and TargetDB 
(Structural Genomics Targets database).<br />  

The &quot;Top Hits&quot; (highest scoring matches), from the most recently performed 
Blast searches, for each Target are stored as PiMS DbRef (Database reference) records 
associated with the Target record.  Details of the Top Hits for all targets are also presented 
in a table. 
<br /><br />
PiMS Blast searches are available via web-service access to the EBI-hosted NCBIBlast program.  
Consequently, the latest versions of the databases and search tool are used.<br />
&nbsp; <em> -see <a href="http://nar.oxfordjournals.org/cgi/content/full/gkm291v1?ijkey=HLk7PzQHQ2C7dmC&amp;keytype=ref">
Web Services</a> at the European Bioinformatics Institute for more details.</em>
</pimsWidget:box> 
<pimsWidget:box initialState="fixed" title="Contents">
<div class="leftcolumn">
 <ul>
  <li><a href="#weeklyReport">Scheduled Blast Report</a>
   <ul>
    <li><a href="#navToReport">Navigating</a> to the Report</li>
    <li><a href="#key">Key</a></li>
    <li><a href="#topHitTable">Top Hits table</a></li>
    <li><a href="#params">Parameters</a><br /><br /></li>
   </ul>
  </li>
 </ul>
 </div>
 <div class="rightcolumn">
 <ul>
  <li> <a href="http://www.pims-lims.org/download/V4_1_0/docs/AutomaticBlast.txt">Automating the searches</a>
  <em>Administrator users can configure PiMS to perform Blast searches automatically at regular intervals</em>
  </li>
 </ul> 
 </div>
</pimsWidget:box>
</div> <!--end div help-->

<div class="helpcontents">
<div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="weeklyReport">Scheduled Blast Report</h3>
  <div class="textNoFloat">
  Your PiMS administrator can configure PiMS to do these searches automatically at regular intervals

  <h4  id="navToReport">Navigating to the Scheduled Blast Report</h4>
  <ul>
   <li>Select &#39;Reports&#39; from the Target menu
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastNav1.jpg" alt="Navigation to report" /><br /><br />
   </li>
   <li>Click Scheduled Blast Report
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastNav2.jpg" alt="Navigation to report" /><br /><br />
   </li>
   <li>If you have not configured PiMS to run scheduled Blast searches you will see the page below<br />
      &nbsp; <em>-ask your PiMS administrator to configure PiMS to perform scheduled Blast searches</em>
   
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastNoHits.jpg" alt="No Top Hits in PiMS" /><br /><br />
   </li>
   <li>If you have configured PiMS to run scheduled Blast searches you should see a Report
   displaying the Top Hits for all Targets in your database</li>
  </ul>
  
  <h4 id ="key">Table Key</h4>
  Near the top of the report you will see a banner labelled &#39;Key&#39;<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastKeyClosed.jpg" alt="Key closed" />
   <br />
  
  Click the <img src="${pageContext.request.contextPath}/skins/default/images/icons/openbox.png" alt="plus icon"/> plus icon and 
  a box will open to display the Key for the table<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastKeyOpen.jpg" alt="Key open" /><br />
   <ul>
    <li>To see a description of the cell contents and colours, mouse over the cells in the key.<br />
    &nbsp; <em>-the example shows the tooltip for the PDB Top Hit with a red background</em></li>
   </ul>
  
  <h4 id="topHitTable">Top Hits table</h4>
  The Table is divided vertically into 3 sections which include:<br />
  &nbsp;<strong> Target details &nbsp;&nbsp; | &nbsp;&nbsp;PDB Hit details &nbsp;&nbsp; | &nbsp;&nbsp;TargetDB Hit details</strong><br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/bioinf/autoBlastTableNoHead.jpg" alt="Results table" />
  <br /><br />
  <ul>
   <li><strong>Target details</strong><br />
   &nbsp; <strong>Target</strong> provides a link to the details page for each Target.<br />
   &nbsp; <em>-the value displayed in the column can include:<br />
   &nbsp;&nbsp; the name of the Target-protein, the name of the Target-organism &amp; 
   the name of the Lab Notebook<br />
   &nbsp; -the cell will have a <strong>green</strong> background if the Target has been solved locally<br />
   </em><br /><br />
   &nbsp; <strong>Length</strong>  is the length of the Target protein sequence.<br /><br /></li>

   <li><strong>PDB Hit details</strong> <em>
   -the date of the most recent scheduled PDB Blast search is shown</em><br />
   &nbsp; <strong>Top Hit</strong> is the PDB identifier for the Top Hit from the most recent scheduled Blast search<br />
   &nbsp; <em>-the date indicates when the top hit was originally recorded in PiMS<br />
   &nbsp; -the identifier for a new Top Hit will be diplayed in <strong>bold</strong> text<br />
   &nbsp; -the cell will have a <strong>red</strong> background if the Top Hit shares &gt;99&#037; Identity with the Target<br />
   &nbsp;&nbsp;<strong>and</strong> the Target was solved elsewhere</em><br /><br />
   &nbsp; <strong>Evalue</strong> is the Expect value for the Hit
   &nbsp; <em> -Blast searches are run with an Expectation threshold of 1.0</em><br /><br />
   &nbsp; <strong>&#037; Identity</strong> is the &#037; of identical amino acids between the Target and the Hit
   -expressed as a &#037; of the Target protein length<br />
   &nbsp; <em> -the <strong>Evalue</strong> &amp; <strong>&#037; Identity</strong> cells will have an orange background 
   if the Top Hit is unchanged but the <strong>&#037; Identity</strong> cell will have a yellow background if it
   is &gt;99&#037;</em><br /><br /></li>

   <li><strong>TargetDB Top details</strong> <em>
   -the date of the latest scheduled TargetDB Blast search is shown</em><br />
   &nbsp; <strong>Top Hit</strong> is the TargetDB identifier for the Top Hit from the most recent scheduled Blast search<br />
   &nbsp; <em>-the date indicates when the top hit was originally recorded in PiMS<br />
   &nbsp; -the identifier for a new Top Hit will be diplayed in <strong>bold</strong> text</em><br /><br />
   &nbsp; <strong>Evalue</strong> and <strong>&#037; Identity</strong> are as for the PDB Top Hits<br /> 
   &nbsp; <em> -the &#037; Identity cells will have a yellow background if the value is &gt;50&#037;</em>
   <br /><br />
   &nbsp; <strong>Status</strong> is the TargetDB status for the Top Hit<br />
    &nbsp; <em> -the Status cells will have a red background if the status of the Top Hit is &#39;Crystallized&#39; or later</em>   
   </li>
  </ul>

  <h4 id ="params">Default search parameters</h4>
  PiMS blast searches are run with the following default parameters:<br />
   <ul>
    <li><strong>Program:</strong> blastp <em>to compare a protein sequence against a 
    protein database</em></li>
    <li><strong>Matrix:</strong> BLOSUM62</li>
    <li><strong>Expect (E value):</strong> 1.0</li>
    <li><strong>Low complexity filter:</strong> off</li>
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