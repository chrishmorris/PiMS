<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Creating an Operon Primer Order Form ' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Overview">
Create Operon Order Form will insert data from a Primer Order Plate experiment (of experiment type Order) into a 
template spreadsheet (OPERON.xlt).<br />  

The spreadsheet may then be printed or emailed to create the order. 
</pimsWidget:box><br /><br />
 
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#design">Designing the Plate Experiment</a></li>
  <li><a href="#import">Importing Targets and Constructs</a></li>
  <li><a href="#create">Creating the Order Experiment</a></li>
  <li><a href="#select">Selecting the Order Experiment</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />

</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
 
  <li><h3 id="design">Designing the Plate Experiment</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   
   It is easiest to design your plate using the oppf template spreadsheet.  You need to enter data in the columns titled Optic id
   and Oppf no. against the appropriate well in your plate experiment; and the rest of the spreadsheet will complete automatically.
   This spreadsheet should then be saved in comma separated variable (csv) format.
   It will be used to complete the Primer Order plate, the Forward Primer plate, the Reverse 
   Primer plate and the Template plate.
   
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/oppf_plate_design.jpg" alt="OPPF Plate Design Template" /><br />
   <br />
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
  <li><h3 id="import">Importing Targets and Constructs</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   OPPF targets and constructs are designed in Optic.  To save entering them twice, an import function will 
   create all of the PiMS objects needed from a list of Optic targets and constructs.<br /><br /> 
   
   The import function can be found in the oppf menu in the oppf perspective.
   <br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/importfromopticmenu.jpg" alt="Import From Optic Menu" /><br />
   <br />
   Browse to find your saved spreadsheet and then click on the upload button.
   <br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/importfromoptic.jpg" alt="Import From Optic Function" /><br />
   <br />
   Alternatively, this form can be used to load individual targets from Optic.  Enter a list of targets separated by commas, in the 
   input field provided.
   <br />
   It may take some time for all of the objects to be created.
   <br />
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="create">Creating the Operon Order Plate Experiment</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  The Operon Primer Order Form is created from an OPPF Primer Order experiment.  This is a virtual experiment with the sole 
  purpose of organising the data for a template spreadsheet.  It has no equivalent laboratory step.<br /><br />
  <br />
  Select New Plate Experiment from the Experiment Menu.  You will need to give your plate a name, select the Primer Order protocol, 
  and browse for the spreadsheet before the experiment can be saved.<br />
  In the experiment name, enter the plate identifier you wish to appear in the order spreadsheet.
  <br />
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/primerorderplate.jpg" alt="Select the Primer Order Experiment" /><br /><br />
  
  The primer order plate contains both forward and reverse primers as input samples.&nbsp;&nbsp;The values for these were populated
  from the values in the spreadsheet.<br />
  
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="select">Selecting the Order Experiment</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
  Select the Primer Order Form option from the menus to show a list of all the plate experiments of 
  experiment type order.&nbsp;&nbsp;One of the experiments should be chosen from the table by selecting 
  the radio button to its right.&nbsp;&nbsp;Now click on the Create OrderForm button to create the order 
  form spreadsheet.
  <br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/primerOrder.jpg" alt="Select the Primer Order Experiment" /><br /><br />
  
  The OrderForm spreadsheet will be created in the pimsupload folder and attached as an annotation 
  to the experiment group of the plate.&nbsp;&nbsp;It will also be sent to your browser so it can be 
  opened in your favourite spreadsheet viewer.
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/primerOrderForm1.jpg" alt="Primer Order Form page 1" /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/primerOrderForm2.jpg" alt="Primer Order Form page 2" /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/primerOrderForm3.jpg" alt="Primer Order Form page 3" /><br />
  
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
