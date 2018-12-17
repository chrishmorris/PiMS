<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help on Deleting a PIMS record' />
</jsp:include>

<div class="help">
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS.
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3 id="item1">Deleting a PIMS record</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  <strong>You can only delete records from PIMS if you have permission to do so.  </strong><br />
  There are two types of records in PIMS:<br />
  &nbsp; records of your laboratory information and &#39;Reference data&#39;, which is provided with PIMS.<br />
  Only an &#39;Administrator&#39; user has permission to modify (edit, and delete) the Reference data, but it is available to all users.<br />
  The Administrator is able to define who has permission to modify your laboratory information.<br />
  &nbsp; -see <a href="AccessRights.jsp">Access rights</a> for more information.<br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h4 id="fromView">Deleting a record: from its View page</h4>  
  <ul><li>
  First navigate to the &quot;View&quot; page for the record.
  This is the page which is displayed by:
  &nbsp; -clicking <input class="button" value="Save" type="submit" /> when you are creating a new record or<br />
  &nbsp; -clicking the link in a table of search results.<br />
  &nbsp; -see <a href="HelpSearchRecords.jsp">Search</a> for records in PIMS.<br /><br />
</li>

<li>If you are not permitted to delete this record, you will see:<br />
<span class="linkwithicon greyedout" title="You can't delete this record"><a onclick="return false" href="#"><img class="icon" src="/V2_3/skins/default/images/icons/actions/delete_no.gif" alt=""/></a><a onclick="return false" href="#">Can't delete</a></span></span>
<br />You could ask your PiMS administrator to delete it for you.<br /><br /> </li>
   
    <li>Otherwise, click the 
<span class="linkwithicon " title="Delete Organisation ActiveMotif"><a onclick="return false" href="#"><img class="icon" src="/V2_3/skins/default/images/icons/actions/delete.gif" alt=""/></a><a onclick="return false" href="#">Delete</a></span></span>

link near to the top of the page.<br />
    &nbsp; <strong>note:</strong> you will only see this link if you have permission to delete the record.<br /><br /></li>
    <li>You will see a warning requesting confirmation.<br /><br /></li>
    <li>Click <input class="button" value="Cancel" type="submit" /> to dismiss the warning without deleting the record.<br /><br /></li>
    <li>Click <input class="button" value="OK" type="submit" /> to continue and you will see a page confirming that the selected record has been deleted.
    <br /><br />
    
   </ul>
 

   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
