<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Sample help' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
A PiMS Sample represents an aliquot that is or was present in the lab,
usually in an Eppendorf or in one of the wells of a plate.
Each Project (Construct) will involve many Samples.
Each time you record Experiments, 
pages are created to record the Sample(s) produced.

</pimsWidget:box>
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="http://pims.structuralbiology.eu/docs/videos/sample.wmv">This Help as a video</a></li>
  <li><a href="#sampleList">Search Samples</a></li>
  <li><a href="#viewSample">The Sample View</a></li> 
  <li><a href="#moveSample">Move a Sample</a></li> 
  <li><a href="#moveSample">Dispose of a Sample</a></li> 
  <li><a href="#moveSample">View Freezer Contents</a></li> 
  <li><a href="#divideSample">Divide a Sample into Aliquots</a></li> 
  <li>Recording and using <a href="HelpRecipeAndStock.jsp">Reagents</a> in PiMS</li>
 </ul>
  <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS.
</pimsWidget:box></div> 
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

<h3 id="navigation">Sample management</h3>

<h4 id="sampleList">Search Samples</h4>
<div class="helpcontents">
You can search all the samples in the database, matching the sample name or details. 
In the Sample menu, choose "Samples".
	<div class="textNoFloat">
	
	 <!-- end div textNoFloat -->
</div> <!-- end div helppage -->
	
	
<h4 id="viewSample">The Sample View</h4>	
	<div class="textNoFloat">
	You can select to view a sample by clicking on the sample name or on the magnifying glass icon beside.  
	The sample view shows attributes of the sample, and the experiment and parameters that were used to create the sample (if appropriate). 
	These values can be changed but must be saved by clicking the 'save' button.<br />
    The container the sample is in is shown, and can be changed by selecting 'Move'.<br />
    If the sample has a value for the amount, the sample can be split into several portions by selecting the Divide Sample button.
	
	<img class="imageNoFloat"
		src="../images/helpScreenshots/sample.png"
		alt="sample list assign to" /><br />
		
	If you want to perform an experiment on the sample, a list of next experiments available can be seen in the 
	'Used as inputs to Experiments' block.<br />
	
	
	</div> <!-- end div textNoFloat -->

<h4 id="moveSample">Move Sample</h4>    
<div class="textNoFloat">
    When you freeze a sample, click "Move" to record where you are putting it.  
    The view of the sample will be updated to show the freezer, shelf, and box.
    When you take the sample out again, click "Remove".
</div> <!-- end div textNoFloat -->

<h4 id="moveSample">Dispose of a Sample</h4>    
<div class="textNoFloat">
    In the screen shot above, "Stock Available" is recorded as "Yes".
    This sample will appear on the list of options when you want to add a Plasmid sample to an experiment.
    When you decide to discard a sample, it usually isn't appropriate to delete the record from PiMS.
    Instead, click "<img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/edit.gif" alt=""/> Make changes...</span>", 
    change "Stock Available" to "No", and click <input type="submit" value="Save changes">.
</div> <!-- end div textNoFloat -->

<h4 id="moveSample">View Freezer Contents</h4>    
<div class="textNoFloat">
    In the Sample menu, choose Containers to see a list that includes your freezers. 
    Click on the link to see a view of the freezer. It includes several shelves. 
    Open the bar for a shelf to see the boxes it contains. Click on the link to a box to see the samples in the box.
</div> <!-- end div textNoFloat -->
	
<h4 id="divideSample">Divide a Sample into Aliquots</h4>	
	<div class="textNoFloat">
	From the sample view select the 'Divide Sample' to split the sample into a number of portions.  
	Equal portions are created with Aliquot X appended to the sample name.  
	Both the name and the amount of each portion can be adjusted afterwards from the view of each portion.
	
	</div> <!-- end div textNoFloat -->
	
	</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>


<jsp:include page="/JSP/core/Footer.jsp" />
