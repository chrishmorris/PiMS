<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to using PiMS Reference Data' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
<p style="padding-left:1.4em; ">When PIMS is installed, it contains data which is known as <em>Reference data</em>,
including a list of public databases, and a list of hazard phrases.
<br />
Some Reference data is required for PiMS to function correctly.  For example, there 
is a set of &#39;Target statuses&#39; which are used to populate the 
<a href="../HelpTargetScoreboard.jsp">Target Scoreboard</a>.<br />

This Reference data can be read by any PIMS user, but can be updated only by 
the administrator.<br />
In addition, PIMS contains details or &#39;specifications&#39; for certain laboratory 
chemicals and reagents. See: <a href="../HelpRecipeAndStock.jsp">Reagent</a> help.</p>
</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#navRefData">Navigate</a> to Reference Data</li>
  <li><a href="#listRefData">Viewing</a> Reference Data</li>
  <li><a href="#searchRefData">Searching</a> the Reference data</li>
  <li><a href="#updateRefData">Updating</a> Reference data</li>
  <li><a href="#useRefData">Using</a> Reference data</li>
 </ul>
 
 <a href="../HelpIndex.jsp">Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>


 <ul><li><h3 id="navRefData">Navigating to Reference data</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
If you are the PiMS administrator for your installation, change to the "admin" perspective, then click &quot;Reference&quot; in the horizontal menu bar to see a list of the 
 Reference data in PiMS.
 <br /><br />
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>
 </li> 
 
  <li><h3 id="listRefData">Viewing Reference data</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
 To view a specific list of PiMS reference data items:<br />
 click the appropriate &quot;Search&quot; link from the Reference data page.
 <br />or<br />select the relevant item from the Reference Data drop-down menu.
 
 </div> <!--end div class="textNoFloat"-->
  <div class="textNoFloat">
  To view all of the details recorded for a specific item, click the link.

 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 </li>
 
 <li><h3 id="searchRefData">Searching Reference data</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
 

   <ul>
    <li>Enter a search term(s) in the appropriate box(es) then click 
    <input class="button" value="Search" type="submit" />.<br /><br /></li>
    <li>If you do not enter any search terms, the details of <em>all</em> Database 
    name records in PIMS will be retrieved.
    <br /><br /></li>
    <li>Searches are case-insensitive and a search term can occur anywhere in the 
    data field<br />
    &nbsp; -for more detailed help see <a href="../HelpSearchRecords.jsp">Searching</a> for records in PIMS.
    <br /><br /></li>
    <li>If you enter a term(s) which does not exist any of the PiMS records for 
    Database names, none will be retrieved.</li>
   </ul></ul>
   
  <h4>Matching records</h4>
  A list of &quot;Database name&quot; records which match your search criteria will 
  be displayed in a table at the bottom of the form.<br />
   <ul>
    
    <li>To see all the details recorded for a Database name, click the link.<br /><br /></li>
    
    <li>If you logged on as an administrator, you can delete unwanted reference data. </li>
   </ul>

 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>

 <ul><li><h3 id="updateRefData">Updating Reference data</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
 To update an item of Reference data, you must first navigate to the appropriate 
 &#39;View&#39; page.
 <br />&nbsp; -see <a href="#listRefData">Viewing</a> Reference Data or
 <a href="#listRefData">Searching</a> the Reference Data.<br />
 You will see the item's &#39;View&#39; page when you click on the link in the first column of the list.<br /><br />
 
   <ul>
    
    <li>An administrator user can also &#39;Edit&#39; or &#39;Delete&#39; this 
    record (see below).<br /><br /></li>
  </ul>		
	
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>
 </li> 
     
  <li><h3 id="useRefData">Using Reference data</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
 Reference data is typically used as a means of reducing duplication in your 
 database and for grouping related records.<br />
 For example, all entries in the Uniprot database share the same database name 
 and base URL.<br />
 In PiMS, all records of Uniprot entries are associated with a single Database 
 name record for the Uniprot Database.
 <br /><br />
 

 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>
 
 </li></ul> 
 
 
 
 
<jsp:include page="/JSP/core/Footer.jsp" />
