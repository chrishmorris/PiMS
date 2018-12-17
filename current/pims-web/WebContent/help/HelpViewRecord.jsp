<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help on Viewing a PIMS record' />
</jsp:include>

<div class="help">
<em>Note:</em> This page describes the general PIMS view pages. The following types of
record have custom views - click on the name to see help on viewing that record type:
<ul>
<li><a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp#targetDetails">Targets</a></li>
<li><a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#viewExp">Experiments</a></li>
<li><a href="HelpCreatePlateExperiment.jsp#basicdetails">Plate experiments</a></li>
<li><a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#sampleViewImage">Samples</a></li>

</ul>


<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#navigation">Navigation</a></li>
  <li><a href="#specDetails">Specific details</a></li>
  <li><a href="#assocDetails">Details for associated records</a></li>
  <li><a href="#addAssocDetails">Associating additional records</a></li>
 </ul>
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3 id="navigation">Navigation</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  To see all of the information for a PIMS record you need to navigate to a the record's view page.<br />
  This is displayed when you:<br />
  &nbsp; - save a data input form or<br />
  &nbsp; - click a link to the record 
  <br /><br />
  A standard PIMS View page consists of 3 main areas which contain:
  <ul>
   <li>The Specific details for the record</li>
   <li>Details of any other records which you have associated with the record</li>
   <li>Links for creating additional associations</li>
  </ul>
  <br />
  
  
  </div>
  </div>
  
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="specDetails">Specific details</h3>
  <div class="helpcontents">
  The specific details for the record are the information entered in the mandatory 
  and optional data fields when the record was created.  They are found in a box at 
  the top of the page labelled &quot;Details&quot;. <br />
  This box has a horizontal bar containing several links.
  <br />
  These can include:<br /><br />
  <strong>Diagram</strong> a graphical representation of the relationships 
  between certain PiMS records such as Samples, Experiment and Targets<br />
  &nbsp; <em>-see <a href="HelpExperiments.jsp#graph">Experiment graph</a> for an example.</em><br /><br />
  
  <strong>Delete</strong> to delete the record.<br /> 
  &nbsp; -see also <a href="HelpDeleteRecord.jsp" >Deleting</a> a PIMS record.<br /><br />
  <strong>Create new</strong> to create a new record of the same type.<br /> 
  &nbsp; -<strong>note:</strong> you will only see the <strong>Edit</strong>, 
  <strong>Delete</strong> and <strong>Create new</strong> links if you have permission to update the record.<br /><br />
  <strong>Search</strong> to search for records of the same type.<br /><br />
  <strong>List all</strong> to see a list of all records of the same type.<br /><br /> 
  
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="assocDetails">Details for associated records</h3>
  <div class="helpcontents">
  <h4>Other information associated with the .....</h4>
  <ul>
   <li>Under this heading you will find details of any records which have been 
   associated with the specific record.<br />
   These are delineated in horizontal bands.<br />
   &nbsp; -in this example &#39;Protocols:&#39; and &#39;Experiments:&#39; have been 
   associated with the Experiment type record.<br /><br /><li>
   <li>For each type of associated record there are links in a horizontal bar:<br />
   These include links to:<br />
   &nbsp; <span class="helpText">Add new</span> to associate new records of 
   the same type<br />
   &nbsp; <span class="helpText">Search/Add</span> to add and remove existing 
   associated records<br /><br /></li>
   <li>The amount of information displayed for all associated records can be 
   increased by clicking the plus <img src="../skins/default/images/icons/openbox.gif" class="button" alt="+" /> 
   icon<br />
   or decreased by clicking the minus
   <img src="../skins/default/images/icons/closebox.gif" class="button" alt="-" /> icon. <br />
   </li>
   <li>By default, all associated records are displayed in the less detailed 
   <img src="../skins/default/images/icons/openbox.gif" class="button" alt="+" /> view.<br /><br /></li>
   
   <li>You can also view the details of the individual associated records.<br />
   If only one record is listed click <span class="helpText">View</span>, in the 
   horizontal bar.<br />
   <br />
   If there are multiple records, they will be displayed in a table when you click the 
   <img src="../skins/default/images/icons/openbox.gif" class="button" alt="+" /> icon.<br />
   Then click the link in the first column of
   the appropriate table row. 
   <br /><br /></li>
  </ul>
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>

  <li><h3 id="addAssocDetails">Associating additional records</h3>
  <div class="helpcontents">
  At the bottom of the page, under the heading:
  <h4>Associate other records with this .....</h4>
  there are links <span class="helpText">Add new ...</span> and <span class="helpText">Search/Add ...</span> for associating  (and removing) additional types of records.

  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
 

<jsp:include page="/JSP/core/Footer.jsp" />
