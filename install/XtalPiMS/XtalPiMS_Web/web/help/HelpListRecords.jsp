<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help List PiMS records' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#viewList">View</a> a list of PiMS records </li>
  <li><a href="#pageControls">Page display</a> Controls</li>
  <li><a href="#exportOptions">Export</a>  options</li>
  <li><a href="#customLists">Custom</a>  lists</li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />
</pimsWidget:box>
</div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3 id="overview">Listing records in PiMS</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  PiMS records are grouped by type.  For example, a Target record contains the details 
  for an individual Target whereas an Experiment record contains the details for an 
  individual Experiment.<br />
  PiMS provides a means to retrieve and display a List of related records.<br />
  This is achieved by clicking the appropriate &#39;Search... &#39; link in one of 
  the PiMS drop-down menus or on the &#39;Home&#39; or an &#39;Functions&#39; page. 
  <br /><br />
 </div> <!--end div class="textLeft -->
 </div> <!--end div class="helppage"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 </li>
 
<li><h3 id="viewList">View a list of PiMS records</h3>
  <div class="helpcontents">
  <div class="textNoFloat">  
  When you click a &#39;Search... &#39; link, you will be presented with a table 
  listing the details for all records of the appropriate type.<br /><br />
  The example below discusses the result of clicking the &#39;Search Organisations&#39; link 
  in the User menu.<br /><br />
  <br /><br />
  There are a number of options available from this page.<br /><br />
  <ul>
   <li><strong>Select a page of Records to view:</strong> you can select an 
   individual page of Organisation records by clicking either a page number or the 
   &quot;[Prev/Next]&quot; or &quot;[Next/Last]&quot; links<br />
   &nbsp; -page number 1 is shown in the example, displaying records 1-20 <br /><br /></li>
   
   <li><strong>Sort Records:</strong> you can sort the order in which the records are 
   displayed in the table according to any of the columns.<br />
   &nbsp; -this is achieved either by clicking on the column name, or on the up or 
   down arrows next to each column name.<br />
   In this example, the Organisation records have been sorted alphabetically by 
   \&#39;Name\&#39;<br /><br /></li>

   <li><strong>View the Record details:</strong> you can see all of the details 
   recorded for an individual record by clicking the name of the record, which is a link.<br />
   &nbsp; -see also <a href="HelpViewRecord.jsp">Viewing</a> a PiMS record.<br /><br /></li>
   
   <li><strong>Create a new Record:</strong> you can also Create a new record of this 
   type by clicking the link towards the top of the page.<br />
  <br /><br /></li>
  </ul>
 </div> <!--end div class="textLeft -->
 </div> <!--end div class="helppage"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 </li>

<li><h3 id="pageControls">Page display Controls</h3>
  <div class="helpcontents">
  <div class="textNoFloat">  
  There are some additional options which are revealed by clicking the 
   &quot;Page-display controls&quot;<br /><br />
  <br /><br />
  <ul>
   <li><strong>Set the number of Records per page:</strong> you can set the number of 
   records displayed on a page by clicking one of the page size selectors and then 
   the <input class="button" value="Update view" type="submit" /> button.<br />
   &nbsp; -the pager is set to 20 in the example<br /><br /></li>
   
   <li><strong>Show row numbers:</strong> you can add an extra column to the lhs of 
   the table to display row numbers.<br /><br /></li>

   <li><strong>Select which columns to display:</strong> you can choose which details 
   are displayed by clicking in the relevant boxes and then the 
   <input class="button" value="Update view" type="submit" /> button.<br />
   &nbsp; -in the example, the columns diplaying the Fax number, Postal code and 
   the organisation's URL are unchecked and so are not displayed.<br /><br /></li>
  </ul>
 </div> <!--end div class="textLeft -->
 </div> <!--end div class="helppage"-->
 <div class="toplink"><a href="#">Back to top</a></div>
 </li>  

 
  <li><h3 id="customLists">Custom lists</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  There are three places in PiMS where non-standard or custom lists are displayed.<br />
  These are:
   <ul>
    <li><strong>Construct management homepage:</strong> is a list of all targets 
    recorded in PiMS.<br />
    It is reached either by clicking &#39;Target&#39; in the horizontal menu bar, 
    or by selecting &#39;Search Targets&#39; from the Target drop-down menu.<br />
    This list does not have any page display controls or options for sorting and filtering 
    the items in the list.<br />  
    &nbsp; -see <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp#homePage">Construct management</a> homepage for more 
    details.<br /><br /></li>

    <li><strong>Browse Targets:</strong> is a list of all targets recorded 
    in PiMS with their latest progression stage.<br />
    It is reached by clicking the &#39;Browse Targets&#39; link from the Target Functions 
    page.<br />
    This list can be displayed page-by-page, with user-defined number of items per page,
    has sortable columns and export options.<br /><br />
    </li>
 
    <li><strong>Reports:</strong> there are 3 Reports:<br />
    &nbsp;&#39;Construct Progress&#39; -a list of all constructs and their latest 
    experiment<br />
    &nbsp;&#39;All constructs fasta&#39; -a list of all construct sequences in Fasta 
    format<br />
    &nbsp;&#39;Experiments Summary&#39; -a list of all experiments ordered by 
    descending date<br />
    These lists can be reached by clicking selecting &#39;Reports&#39; from the 
    Target drop-down menu.
    The lists can be refined by entering search terms.<br />
    &nbsp; -see <a href="HelpTargetReports.jsp">Reports</a> help for more details
    </li>
   </ul>
  </div> <!--end div class="textNoFloat"-->
  </div> <!--end div class="helppage"-->

 </li>

 

<jsp:include page="/JSP/core/Footer.jsp" />
