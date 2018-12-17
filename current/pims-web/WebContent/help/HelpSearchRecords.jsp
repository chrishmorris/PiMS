<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help on Searching for records in PiMS' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#searchForm">Search form</a></li>
  <li><a href="#searchResults">Search results</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PiMS.
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 

 <li><h3 id="overview">Performing searches in PiMS</h3>
  <div class="helpcontents">
  PiMS provides a search facility to help you to locate information which has been stored as PiMS records.<br />
  This can be either data which you have entered, or reference data which is provided when PiMS is installed.<br /><br />
  When creating a PiMS record, it is often neccessary to find &#39;other&#39; information to associate with the new record, this can be achieved with a PiMS search. 
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3>An example search</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  This example shows how to search PiMS to find records of &#39;Organisations&#39;.
  <br />
  An organisation can be a public institute, such as a University department,  or a private company such as a reagent supplier.<br />
  This is like searching for entries in an address book.<br /><br />
  
  To search for organisations you can:<br />
  &nbsp; -select &quot;Search Organisations&quot; from the People &amp; Places menu or
  <br />&nbsp; -click the &quot;Search Suppliers&quot; link in the Sample &amp; Reagent 
  Functions page.<br />
  &nbsp;<strong>note:</strong> To search for other types of information in PiMS you 
  need to click the appropriate search link.
  </div> <!--end div class="textNoFloat"-->

  <div class="textNoFloat">
  You will see a form with multiple fields for searching for organisations in PiMS.
  
  </div> <!--end div class="textNoFloat"-->
  <div class="textNoFloat">
  <strong>notes:</strong>
  <ul>
   <li>The search form shows the number of Organisations records currently recorded in PiMS.<br />In this example, there are 28.
   <br /><br /></li>

   <li>There are also at least two search fields.<br />
   &nbsp; -one labelled &quot;Search in all fields&quot; allows you to search for a matching term in all of the Organisation data fields.<br />
   &nbsp; -the additional field(s) are to search for records with information matching specific data fields for an Organisation<br /><br />
   <strong>note:</strong> a search field is only shown if PiMS contains a record with information recorded for that field.<br />
   So, in this example, there are 9 additional search fields indicating that PiMS contains Organisation records with information recorded for the Fax number, Postal code, Country, Organisation type, E.mail address, URL, Telephone number, Name and City data fields.<br /><br /></li>
   
   <li>For help on each search field, mouse-over the field name to reveal tool-tips or click on the field name for a printable list describing all of the search fields. <br /><br /></li>
  
   <li>To perform a search, enter a search term(s) in the appropriate fields and then click 
   <input class="button" value="Search" type="submit" /><br /><br /></li>
   <li>If you do not enter <em>any</em> search terms, the details of <em>all</em> relevant PiMS records will be retrieved.<br />
   &nbsp; - in this case, the details for all 28 Organisation records will be retrieved.
   <br /><br /></li>
   <li>If you enter a term which does not match the corresponding entries for existing records, none will be retrieved.
   <br /><br /></li>
   <li>A list of appropriate records matching your search criteria will be displayed in a table at the bottom of the form.<br /><br /></li>
   <li><strong>note:</strong> If PiMS does not contain <em>any</em> appropriate records, there will be no search fields displayed but you may use the link at the bottom of the page to create a new record.<br />
   &nbsp; -In this example, click the <span class="spotLink">Create a new Organisation</span> link.<br /><br /></li>
  </ul>
  </div> <!--end div class="textNoFloat -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="searchResults">Search results</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  A list of records which match your search criteria will be displayed in a table 
  the bottom of the form.<br />
  A blue progress bar will be displayed as the records are being retrieved from 
  PiMS.<br /><br />
  To see all details recorded for a particular organisation, click 
link in the first column of the appropriate row.<br />
    &nbsp; see <a href="HelpViewRecord.jsp" >Viewing</a> a PiMS record for help.<br /><br />
   </div> <!--end div class="textLeft -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

 

<jsp:include page="/JSP/core/Footer.jsp" />
