<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help Creating a Primer Order' />
</jsp:include>

<div class="help">

<h2 id="top">Overview -Creating a Primer Order in PiMS</h2>
PiMS allows you to design the layout of Primers in a 96-well format and use this to create a spreadsheet Primer Order Form, 
based on a template for your chosen supplier.<br />
Starting from a list of PiMS Construct records, the corresponding Primers are assigned to positions in a 96-well plate format.<br />
The default layout is based on the order in which the Constructs were designed in PiMS with
the most recently designed Construct assigned to well H12, but you may edit this layout to suit your needs.<br />
When you are happy with the layout you may create an Order form suitable for printing or sending to the supplier.

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#listConstructs">The Construct List</a></li>
  <li>
   <ul>
    <li><a href="#navToConstructs">Navigation</a></li>
    <li><a href="#selectConstructs">Selecting the Constructs to use</a></li>
<!-- TODO    <li><a href="#sortList">Sorting the List</a></li>  -->   
   </ul>
  </li>
  <li><a href="#setupPlate">Primer order Set-up Grid</a></li>
  <li><a href="#createOrder">Creating the Order Form</li>
  <li><a href="#template">Order form template</a></li>
 </ul>
 <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Back</a> to PiMS Target Help<br />
 <!--THIS IS THE LINK TO ANNE@S NEW HELP INDEX<a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS.  -->
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS.</a>
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3 id="listConstructs">The Construct List</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  When you design a Construct in PiMS <em>(see <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">New Construct</a>)</em>
  you are creating a &#39;Construct Design&#39; Experiment with three output Samples representing the Template, Forward primer and Reverse primer.<br />
  
  To create a Primer Order in PiMS you select the Primers to order by first selecting the parent Construct.<br />
  <h3 id="navToConstructs">Navigation</h3>
  Navigate to the Target Functions page by clicking &quot;More...&quot; in the Target menu.<br />
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/PrimerOrderNavigation.png" alt="Image of Target Functions page showing Constructs box" /><br /><br />
  
  <ul>
   <li>Locate the box labelled <strong>Constructs</strong>.<br /><br /></li>
   <li>To select Constructs from a list of <strong>All Constructs in PiMS:</strong> click <a href="javascript:void(0)">Search All Constructs</a>.<br /><br /></li>
   <li>To select Constructs with <strong>Primers that have not been ordered</strong> (i.e. Primers which are <strong>not</strong> included in a Primer Order Form):
    click <a href="javascript:void(0)">Search Constructs - Primers not ordered</a>.<br /><br /></li>
   <li>To select only the Constructs designed for the Targets in a particular <strong>Target Group:</strong> navigate to the Target Group View and click the links at the top of the page.
	<br /><br /></li>
   <li>You will see a list containing up to 100 PiMS Constructs on each page.<br />
       The first Construct in the list will be the one which was designed last or most recently.<br />
	<img class="imageNoFloat" src="../images/helpScreenshots/targets/ConstructListTop.png" alt="List of Constructs -search results" /><br /><br /></li>
  </ul>
  <h3 id="selectConstructs">Selecting the Constructs to use</h3>
  <ul>	
   <li>Check the boxes against the PiMS Constructs you wish to order Primers for.<br />
   &nbsp; <strong>note:</strong> to select all the Constructs on a page check the box in the top left cell next to the heading <strong>Construct Id</strong><br /><br /></li>
   <li>Scroll to the bottom of the page and you see:<br /><br />
   &nbsp; either <input class="button" value="Create order plate" type="submit" /> Containing selected Constructs<br /><br />
   &nbsp; or <input class="button" value="Create order plate" type="submit" disabled="disabled" />  Containing selected Constructs
   <span class="required">(Order plates must contain between 1 and 96 constructs)</span><br />
   &nbsp; if you have selected more than 96 Constructs <em> -you will need to uncheck some boxes</em><br />
    <img class="imageNoFloat" src="../images/helpScreenshots/targets/ConstructListBottom.png" alt="List of Constructs -more than 96 selected" />
	<br /></li>
   <li>When you have finished your Construct selection, Click the button to continue.<br /><br /></li>
   <li>You may also export the Construct list by clicking the links at the bottom of the list.</li>   
  </ul>
  <!-- TO DO
  <h3 id="sortList">Sorting the List</h3>
  <span class="required">??Not implemented??<br />
  Should be able to sort the list by Target or Organism and the OrderPlate should reflect this</span>
 -->
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <a href="#top">back to top</a>   
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="setupPlate">Primer order Set-up Grid</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
	After you click <input class="button" value="Create order Set-up Grid" type="submit" /> you will see an 8 x 12 grid
	representing the Constructs selected from the list, in a 96-well format.<br />
	The Constructs are assigned to positions based on the order in which they were designed.<br />
	Positions are assigned Column by column.<br /><br />
	This is the <strong>reverse</strong> of the order in which they are displayed in the List.<br />
	The most recently designed Construct wil be assigned to position H12 and oldest of the selected Constructs will be assigned to position A01.
	So, if the default list of 96 Constructs is used, the Constructs in rows 1-8 of the List will be assigned to positions H12-A12, 
	those in rows 9-16 to positions H11 to A11 and those in rows 89-96 to positions H01 to A01.<br /><br />
	In the example, Construct <strong>AAK42179;.N</strong> is in row 96 of the list (5th from the end) and is assigned to well A01 in the grid.<br /><br />
	
	If you slected less than 96 constructs empty wells will contain <strong>[none]</strong> 
	<img class="imageNoFloat" src="../images/helpScreenshots/targets/PrimerOrderSetupGrid.png" alt="Default Primer order setup Plate" /><br />
	<ul>
	 <li>Click <strong>Constructs</strong> to return to a List of All Constructs in PiMS.<br /><br /></li>
	 <li>Click <strong>Progress report</strong> to download a spreadsheet summarising the parameters for Experiments performed using these Constructs.<br /><br /></li>
	 <li>Clicking the <img src="../skins/default/images/icons/actions/viewdiagram.gif" alt="Diagram icon" /> <strong>Diagram</strong> link will provide a link to a PiMS Plate view of the Constructs and their Primers.<br /><br /></li>	 
	 <li>Use the scroll bar to see the contents of all 12 columns.<br /><br /></li>
	 <li>Click <img src="../skins/default/images/icons/actions/edit.gif" alt="Edit icon" /> <strong>Make changes...</strong> to see an editable version of the grid where you may change the Construct assigned to any well.<br />
		<img class="imageNoFloat" src="../images/helpScreenshots/targets/EditPrimerGrid.png" alt="Search Constructs popup" /><br />
	  	Select a different Construct from the <strong><em>Recently Used</em> </strong> section of the drop-down list.<br />
	  	If this is empty -as in the example, select <strong>Search more...</strong> to display a PiMS search form.<br />
		<img class="imageNoFloat" src="../images/helpScreenshots/targets/SearchConstructsPopup.png" alt="Search Constructs popup" /><br />
	  	Click <a href="javascript:void(0)">Select</a> in the first column of your chosen construct to relpace the existing construct.<br /><br /></li>
<!-- TODO	 <li>Information about duplicates <span class="required"><em>need to highlight the wells and list positions in the box header or above the grid</em></span><br /><br /></li>  -->
	</ul>
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <a href="#top">back to top</a>   
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
  <li><h3 id="createOrder">Creating the Order Form</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   When you are satisfied with the layout of Constructs in the Primer order Setup Plate, open the box labelled <strong>Order this plate</strong>
   <br /><img class="imageNoFloat" src="../images/helpScreenshots/targets/OrderPlateBox.png" alt="Order this Plate box open" /><br /><br />
   Click <input class="button" value="Create Order" type="submit" /> and after a short delay, you will see a pop-up 
   asking if you wish to open the Primer order form spreadsheet or save it.<br /><br />
   Click &#39;OK&#39; to open the order with your default spreadsheet application.<br />
   Refreshing the page will create a link to order form spreadsheet in the box labelled <strong>Files</strong>.<br />
   <img class="imageNoFloat" src="../images/helpScreenshots/targets/PrimerOrderFilesBox.png" alt="Files box showing link to order form" /><br /><br />
   The first sheet of the order form will contain your specific contact and shipping details along with any specific order requiremnents.
   These details are derived from an <a href="#template">Order form template</a>.
   Sheets 2 and 3 contain the sequences of the forward and reverse primers to be ordered, along with their position in the primer plates. <br /><br />
   <!-- TODO<span class="required"><em>Need some way of indicating that something has happened to the Setup Plate<br />
   e.g. refresh the files list automatically to include the spreadsheet<br />
   or Change the name of the box header to include &#39;Order form created&#39;<br />
   or Redirect to a different page -the diagram, the list of Plate Experiments -the new ones will be at the top, The Construct search page </em></span><br /><br />
   <span class="required"><em>Change the name of the Files box to &#39;Primer orders&#39;, and don't allow other files to be uploaded</em></span> 
   <br /><br />
   -->
   In adition to creating the spreadsheet Primer order Form, PiMS will have created three Plate Experiments.<br />
   Click the <img src="../skins/default/images/icons/actions/viewdiagram.gif" alt="Diagram icon" /> <strong>Diagram</strong> link to see a representation of these.<br />
   <img class="imageNoFloat" src="../images/helpScreenshots/targets/PrimerOrderDiagram.png" alt="Diagram showing Order and Primer plates" /><br /> 
   <br />
   One Plate Experiment, representing the Primer Order, is created using a Protocol for an Experiment of type Order.<br />
   The Protocol has two output Samples representing the Forward and Reverse Primers.<br />
   There are a number of additional of Parameters which can be recorded.<br />
   <img class="imageNoFloat" src="../images/helpScreenshots/targets/OrderPlateView.png" alt="Plate view of Order" /><br /><br /> 
   
   <!-- TODO <span class="required"><em>Many of these are Leeds-specific and the default values are misleading and have odd names<br />
   they should be displayed conditionally -some could be populated from the Primer and Construct records.<br />
   -->
   The two other Plate Experiments contain the Forward and Reverse Primer Samples.<br />
   These are generated using a PiMS Plate Layout Protocol for an Experiment called &quot;Import Sample&quot;. 
   These represent the Primer plates from your supplier will be available for use in recording additional PiMS Experiments e.g. a PCR Plate Experiment.
   <!-- TODO <span class="required"><em>Should also create a Template Samples plate</em></span> -->
   
  </div> <!-- end div class="textNoFloat" -->
 </div> <!--end div class="helppage"-->
  <a href="#top">back to top</a>   
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>  
 
  <li><h3 id="template">Order form template</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
    Details about creating an order form template .xlt file can be found in the appropriate README file on the 
    <a href="http://www.pims-lims.org/download">Downloads</a> page of the PiMS website.<br />
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <a href="#top">back to top</a>   
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>  
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
