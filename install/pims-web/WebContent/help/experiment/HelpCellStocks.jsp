<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Cell Stocks' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="blank.gif" />
<c:set var="title" value="Help for Cell stocks"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
   <div class="help">
    <p>
      PiMS allows you to record details of stocks of Cells and display this information as a list.<br />
      The details you can record include the name of the Cell stock, the Host strain, the Plasmid it was transformed with, 
      the parent vector, and any antibiotic resistances from the Vector and /or host.
      You can also record the date and place it was stored and by whom.
    </p>
   </div> 
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li><a href="#preReqs">Prerequisites</a></li>
            <ul>
                <li><a href="${pageContext.request['contextPath']}/help/experiment/HelpPlasmidStocks.jsp#holders">Containers</a></li>
            </ul>
        <li><a href="#newStock">Recording Cell stocks</a></li>
        <li><a href="#updateStock">Updating the Cell stock</a></li>
        </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#listCS">Cell Stock List</a></li>
        <li><a href="#linkCS">Linking to other PiMS Experiments</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Recording Experiments</a></li>
      <li><a href="${pageContext.request['contextPath']}/help/experiment/HelpPlasmidStocks.jsp">Recording Plasmid stocks</a></li>
    </ul>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="preReqs">Prerequisites</h3>
  Before you can record a Cell stock you must check that your PiMS contains the following:
  <ul>
   <li>an <strong>Experiment type</strong> called &quot;Cell stock&quot;</li>
   <li>the <strong>Protocol</strong> called &quot;PiMS Cell Stock&quot;</li>
   </ul>
  If it does not then please ask your PiMS Administrator to update the database with these items.
  <ul>
    <li><strong>People:</strong> if you intend to record the details of Cell stocks prepared by other lab members you may also wish 
    to have the relevant PiMS People records.<br /><br /></li>
    <li><strong>Vectors:</strong> to be able to link your cell stock record to details of the relevant Vector, you will also need to 
    have the appropriate Vector Samples.<br />
    PiMS provides Recipes for commonly used Vectors which you can use to record suitable Samples.<br />
    &nbsp; <em> to see a list of the available Vector Recipes click &quot;Search Vectors&quot; in the Recipes box on the Sample functions page.</em><br /><br /></li>
    <li><strong>Cells:</strong> to be able to link your Cell stock to the Host cell or transformed cells in the stock, you will need to
    have PiMS Sample records with the &quot;Cell&quot; Sample type.<br />
    &nbsp; <em> future versions will include a link to details of the relevant Host cells.</em><br /><br /></li>
    <li><strong>Boxes:</strong> to record information about the Boxes or Containers for your Cell stocks, you will need PiMS Holder records.<br />
    PiMS provides Reference data for &quot;Holder types&quot; which can be used to record the details of specific Containers.<br />
    <em>&nbsp; see <a href="${pageContext.request['contextPath']}/help/experiment/HelpPlasmidStocks.jsp#holders">Help for Containers</a> for more details.</em>
 
   </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  
  <h3 id="newStock">Recording cell stocks</h3>
  Cell stocks are a specific type of Experiment in PiMS and so are recorded in the same way.<br />
  The Cell stock Experiment has 3 Input Samples and a single output sample:
  
  <ul>
   <li>A <strong>Vector</strong> input sample representing the parent vector for the plasmid</li>
   <li>A <strong>Plasmid</strong> input sample representing the Plasmid used to transform the cells<br />
   &nbsp; <em> this also allows you to link the stock to the Experiment which produced the purified plasmid</em></li>
   <li>A <strong>Cell</strong> input sample to allow you to link the stock to the Host cells or to the Experiment which produced the transformed 
   cells you are storing</li>
   <li>A <strong>Cell stock</strong> output sample to allow you to record the location of the stock</li>
  </ul>
  It also has two experiment parameters for recording:<br />
  &nbsp; the name of the Host <strong>Strain</strong><br />
  &nbsp; the antibiotic resistances of the Host <strong>Antibiotic Res. (strain)</strong>
  

  <h4>To record a new Cell stock:</h4>    
  <ul>
    <li>Select &quot;New Single Experiment&quot; from the Experiment menu</li>
    <li>Select Cell stock from the drop-down list of Experiment types
     <em>-the PiMS Cell Stock protocol is displayed</em></li>
    <li>You may change the Experiment name suggested by PiMS to e.g. to the name of your Cell stock as in the example below.<br />
    &nbsp; <strong>note:</strong> <em>the name must be unique in PiMS</em><br /><li>
    <li>If you use PiMS to record information for more than one &quot;Lab Notebook&quot;, you will need to select one from the drop-down list.
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/MakeNewCS.png" alt="Record a new Cel stock experiment" />
    </li>
    <li>Click <input type="submit" value="Next >>>" /></li>
  </ul>

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->

  <h3 id="updateStock">Updating the Cell stock</h3>
  After recording a New Cell stock you will see the a PiMS Experiment record displaying the &quot;Experiment name&quot; and other details from the previous step.<br /><br />
  At the top of the page there are the usual links to search pages for all &quot;Experiments&quot; and &quot;Cell stock Experiments&quot;<br />
    There are also links to view the diagram for, to make a copy of and to delete this Cell stock record.<br />
    In addition there is a link to the Cell Stock list <em>(-see <a href="#listCS">Cell Stock list</a> for help</em>) and a link to this guide.
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewCSTop.png" alt="Top of Cell stock record" />
  <h4>Use the Basic Details box to:</h4>
    <ul>
    <li>Set the date the Cell stock was made -Edit the <strong>Start date</strong><br /></li>
    <li>Add a short description of the Cell e.g. the expression product -Edit the <strong>Description</strong></li>
    <li>Link this to a PiMS Construct -Select a Construct from the <strong>Project</strong> field<br />
    &nbsp; <strong>note:</strong> <em>If a &quot;Description&quot; has been recorded for a selected Construct, then this
    will replace the value for the Cell stock.</em></li>
    <li>Record the person who made the Cell stock -Select the person from the list of <strong>Scientists</strong></li>
  </ul>
  
  <h4>Use the Conditions and Results box to:</h4>
  <ul>
    <li>Record the <strong>Antibiotic resistances</strong> of the host strain.</li>
    <li>Record the name of the host <strong>Strain</strong>.</li>
  </ul>
  
  At the bottom of the page you will find the usual PiMS Experiment boxes for Samples, Images, Attachments, Notes and 
  links to External Databases.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewCSBottom.png" alt="Bottom of Cell stock record" />
  
  <h4 id="samples">Use the Samples box to:</h4>
  <ul>
    <li>Update the parent <strong>Vector</strong> -Select the appropriate Sample from a list of PiMS Vector Samples</li>
    <li>Update the <strong>Plasmid</strong> -Select the appropriate Sample from a list of PiMS Plasmid Samples</li>
    <li>Update the <strong>Cell</strong> -Select the appropriate Sample from a list of PiMS Cell Samples</li>
    <li>Update the <strong>Name</strong> of the Cell stock Sample -the output sample</li>
    <li>Record values for the Cell stock Sample's <strong>Holder</strong> (e.g. freezer box) and its position in that holder <strong>Row</strong> and <strong>Column</strong></li>
  </ul>
   
  <div class="toplink"><a href="#">Back to top</a></div>
 
  <!-- ================================================================================ -->


  <h3 id="listCS">Cell Stock List</h3>
  Once you have recorded some Cell stocks you will be able to see the details in a List.
  <br /><br />
  
  To display the Cell Stock list either:<br />
  &nbsp; Click the <a href="JavaScript:void(0)">Cell Stock List</a> link near the top of a Cell stock Experiment.<br />
  &nbsp; or <br />
  &nbsp; From the &quot;Experiment functions&quot; page <em>- "More..." in the Experiment menu</em>
  &nbsp; click &quot;Cell Stock List&quot; in the box labelled &quot;Stocks&quot;
  <br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/CellStockList.png" alt="Cell Stock List" />

    <ul>
    <li><strong>Stock name</strong> is the name of the Output Sample from the Cell stock Experiment</li>
    <li><strong>Box</strong> is the Holder for the Cell Stock Sample</li>
    <li><strong>Position</strong> is the location of the Cell Stock Sample in its Holder<br />
    &nbsp <em>-derived from the <strong>Row</strong> and <strong>Column</strong> values</em></li>
    <li><strong>Date</strong> is the date the stock was made<br />
    &nbsp <em>-the <strong>Start date</strong> of the Cell Stock Experiment</em></li>
    <li><strong>Description</strong> a short description<br />
    &nbsp <em>-either the <strong>Details</strong> field of the Cell Stock Experiment or from a Construct if set</em></li>
    <li><strong>Strain</strong> the name of the Host strain used<br />
    &nbsp; <em>-the value entered for the <strong>Strain</strong> parameter of the Cell Stock Experiment</em></li>
    <li><strong>Antibiotic Resistance</strong> of the Cell Stock<br />
    &nbsp <em>-the resistances are derived from the Vector Input Sample and any value entered for the 
    <strong>Antibiotic Res. (strain)</strong> parameter</em></li>    
    <li><strong>Plasmid</strong> the name of the Plasmid Input Sample<br /></li>
    <li><strong>Vector</strong> the name of the Vector Input Sample<br />
    &nbsp; <em>-derived from the Vector Recipe with a link to this information</em>
    <li><strong>Stored by</strong> is the initials of the Person who made the Cell stock<br />
    &nbsp; <em>-defaults to the Person (Scientist) record linked to the Cell Stock experiment</em></li>
    </ul>
  
  If you have no Cell stock Experiments recorded in PiMS you will see:
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NoCellStocks.png" alt="No Cell Stocks recorded" />
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->

  <h3 id="linkCS">Linking to other PiMS Experiments</h3>
  The Cell stock Experiment can be linked to any PiMS Experiment which has an Output Sample 
  of the type &quot;Plasmid&quot; or &quot;Cell&quot;.
  <div class="toplink"><a href="#">Back to top</a></div>
  
</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

