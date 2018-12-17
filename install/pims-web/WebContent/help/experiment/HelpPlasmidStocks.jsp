<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Plasmid Stocks' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="blank.gif" />
<c:set var="title" value="Help for Plasmid stocks"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
     <p>
      PiMS allows you to record details of stocks of Plasmids and display this information as a list.<br />
      The details you can record include the name of the Plasmid, the parent vector, antibiotic resistences,
      and the bacterial strain it was prepared from.  You can also record the date and place it was stored and by whom.
     </p>
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li><a href="#preReqs">Prerequisites</a></li>
            <ul>
                <li><a href="#holders">Containers</a></li>
            </ul>
        <li><a href="#newStock">Recording Plasmid stocks</a></li>
        <li><a href="#updateStock">Updating the Plasmid stock</a></li>
        </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#listPS">Plasmid Stock List</a></li>
        <li><a href="#linkPS">Linking to other PiMS Experiments</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Recording Experiments</a></li>
    </ul>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="preReqs">Prerequisites</h3>
  Before you can record a Plasmid stock you must check that your PiMS contains the following:
  <ul>
   <li>an <strong>Experiment type</strong> called &quot;Plasmid stock&quot;</li>
   <li>the <strong>Protocol</strong> called &quot;PiMS Plasmid Stock&quot;</li>
   </ul>
  If it does not then please ask your PiMS Administrator to update the database with these items.
  <ul>
    <li><strong>People:</strong> if you intend to record the details of Plasmid stocks prepared by other lab members you may also wish 
    to have the relevant PiMS People records.<br /><br /></li>
    <li><strong>Vectors:</strong> to be able to record the parent Vector for your Plasmid stocks you will also need to 
    have the appropriate Vector Samples.<br />
    PiMS provides Recipes for commonly used Vectors which you can use to record suitable Samples.<br />
    <em> to see a list of the Vector Recipes click &quot;Search Vectors&quot; in the Recipes box on the Sample functions page ("More..." in the Sample menu).</em><br /><br /></li>
    <li><strong>Boxes:</strong> to record information about the Boxes or Containers for your Plasmid stocks, you will need PiMS Holder records.<br />
    PiMS provides Reference data for &quot;Holder types&quot; which can be used to record the details of specific Containers.<br />
    <em>&nbsp; see <a href="#holders">Help for Containers</a> for more details.</em>
 
   </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  
  <h3 id="newStock">Recording plasmid stocks</h3>
  Plasmid stocks are a specific type of Experiment in PiMS and so are recorded in the same way.<br />
  The Plasmid stock Experiment has 2 Input Samples and a single output sample:
  <ul>
   <li>A <strong>Vector</strong> input sample representing the parent vector for the plasmid</li>
   <li>A <strong>Plasmid</strong> input sample to allow you to link this to the Experiment which produced the purified plasmid you are storing</li>
   <li>A <strong>Plasmid stock</strong> output sample to allow you to record the location of the stock</li>
  </ul>
  It also has two experiment parameters for recording the <strong>Concentration</strong> of the stock and
  the bacterial (or other) <strong>strain</strong> it was prepared from.
  

  <h4>To record a new Plasmid stock:</h4>    
  <ul>
    <li>Select &quot;New Single Experiment&quot; from the Experiment menu</li>
    <li>Select Plasmid stock from the drop-down list of Experiment types</li>
    <li>Check that the PiMS Plasmid Stock protocol is displayed, if not please select it</li>
    <li>You may change the Experiment name suggested by PiMS to e.g. the name of your Plasmid as in the example below.<br />
    &nbsp; <strong>note:</strong> <em>the name must be unique in PiMS</em><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/MakeNewPS.png" alt="Another image" />
    </li>
    <li>Select the lab note book to use (if you have only one, then you do not have to select it)</li>
    <li>Click <input type="submit" value="Next >>>" /></li>
  </ul>

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h3 id="updateStock">Updating the Plasmid stock</h3>
  After recording a New Plasmid stock you will see the a PiMS Experiment record displaying the &quot;Experiment name&quot; and other details from the previous step.<br /><br />
  At the top of the page there are the usual links to search pages for all &quot;Experiments&quot; and &quot;Plasmid stock Experiments&quot;<br />
    There are also links to view the diagram for, to make a copy of and to delete this Plasmid stock record.<br />
    In addition there is a link to the Plasmid Stock list <em>(-see <a href="#listPS">Plasmid Stock list</a> for help</em>) and a link to this guide.
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewPSTop.png" alt="Top of Plasmid stock record" />
  <h4>Use the Basic Details box to:</h4>
    <ul>
    <li>Set the date the Plasmid stock was made -Edit the <strong>Start date</strong><br /></li>
    <li>Add a short description of the Plasmid e.g. the expression product -Edit the <strong>Description</strong></li>
    <li>Link this to a PiMS Project -Select a Construct from the <strong>Project</strong> field<br />
    &nbsp; <strong>note:</strong> <em>If a &quot;Description&quot; has been recorded for a selected Construct, then this
    will replace the value for the Plasmid stock.</em></li>
    <li>Record the person who made the Plasmid stock -Select the person from the list of <strong>Scientists</strong></li>
  </ul>
  
  <h4>Use the Conditions and Results box to:</h4>
  <ul>
    <li>Record the <strong>Concentration</strong> of the plasmid (&micro;g/&micro;L).</li>
    <li>Record the <strong>Strain</strong> the plasmid was prepared from.</li>
  </ul>
  
  At the bottom of the page you will find the usual PiMS Experiment boxes for Samples, Images, Attachments, Notes and 
  links to External Databases.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewPSBottom.png" alt="Bottom of Plasmid stock record" />
  
  <h4 id="samples">Use the Samples box to:</h4>
  <ul>
    <li>Update the parent <strong>Vector</strong> -Select the appropriate Sample from a list of PiMS Vector Samples</li>
    <li>Update the <strong>Plasmid</strong> -Select the appropriate Sample from a list of PiMS Plasmid Samples</li>
    <li>Record the <strong>Amount</strong> of the Plasmid stock Sample</li>
    <li>Update the <strong>Name</strong> of the Plasmid stock Sample</li>
    <li>Record values for the Plasmid stock Sample's <strong>Holder</strong> (e.g. freezer box) and its position in that holder <strong>Row</strong> and <strong>Column</strong></li>
  </ul>
   
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ================================================================================ -->
  <h3 id="listPS">Plasmid Stock List</h3>
  Once you have recorded some Plasmid stocks you will be able to see the details in a List.
  <br /><br />
  
  To display the Plasmid Stock list either:<br />
  &nbsp; Click the <a href="JavaScript:void(0)">Plasmid Stock List</a> link near the top of a Plasmid stock Experiment.<br />
  &nbsp; or <br />
  &nbsp; From the &quot;Experiment functions&quot; page <em>- "More..." in the Experiment menu</em>
  click &quot;Plasmid Stock List&quot; in the box labelled &quot;Stocks&quot;
  <br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/PlasmidStockList.png" alt="Plasmid Stock List" />

    <ul>
    <li><strong>Plasmid name</strong> is the name of the Output Sample from the Plasmid stock Experiment</li>
    <li><strong>Box</strong> is the Holder for the Plasmid Stock Sample</li>
    <li><strong>Position</strong> is the location of the Plasmid Stock Sample in its Holder<br />
    &nbsp <em>-derived from the <strong>Row</strong> and <strong>Column</strong> values</em></li>
    <li><strong>Date</strong> is the date the stock was made<br />
    &nbsp <em>-the <strong>Start date</strong> of the Plasmid Stock Experiment</em></li>
    <li><strong>Volume (&micro;L)</strong> is the amount of the Plasmid Stock Sample</li>
    <li><strong>Conc. (&micro;g/&micro;L)</strong> from the &quot;Concentration&quot; parameter of the Plasmid Stock Experiment</li>
    <li><strong>Description</strong> a short description<br />
    &nbsp <em>-either the <strong>Details</strong> field of the Plasmid Stock Experiment or from a Construct if set</em></li>
    <li><strong>Antibiotic Resistance</strong> of the Plasmid Stock<br />
    &nbsp <em>-the resistances recorded for the Vector Input Sample</em></li>    
    <li><strong>Strain</strong> from the &quot;Strain prepared from&quot; parameter of the Plasmid Stock Experiment</li>
    <li><strong>Vector</strong> the name of the Vector Input Sample<br />
    &nbsp; <em>-derived from the Vector Recipe with a link to this information</em>
    <li><strong>Stored by</strong> is the initials of the Person who made the Plasmid stock<br />
    &nbsp; <em>-defaults to the Person (Scientist) record linked to the Plasmid Stock experiment</em></li>
    </ul>
  
  If you have no Plasmid stock Experiments recorded in PiMS you will see:
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NoPlasmidStocks.png" alt="No Plasmid Stocks recorded" />
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="linkPS">Linking to other PiMS Experiments</h3>
  The Plasmid stock Experiment can be linked to any PiMS Experiment which has an Output Sample 
  of the type &quot;Plasmid&quot;.
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="holders">Containers</h3>
  Plasmid and Cell stocks are typically stored in containers (e.g. racks or boxes) in a freezer.<br />
  The PiMS Stock records include this information and you can specify the container and position of each stock.<br /><br />
  
  To be able to use this feature you will need to have PiMS <strong>Holder</strong> records to describe each container.<br />
  PiMS provides Templates called <strong>Holder Types</strong> for some storage containers which you can use to make Holder records.<br />
  <em> -for a list of PiMS Holder Types click &quot;Search&quot; <strong>Holder Types:</strong> in the Reference Data box on the Sample or Experiment functions page.</em>
  <br /><br />
  Examples include 100 and 81-place storage boxes.<br /><br /> 

  The example below shows the Holder Type for the 100-place storage box:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/PlateType.png" alt="100-place Plate type" />

  <h4>To Record a Holder using a Holder Type (Template):</h4>
  <ul>
    <li>Navigate to the Sample or Experiment functions page ("More..." in either menu)</li>
    <li>Locate the box labelled <strong>Containers</strong> and click <a href="JavaScript:void(0)">New</a></li>
  </ul>
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewHolderForm.png" alt="Form for a new Holder" />
  <ul>
    <li>Enter a value for the <strong>Name</strong><span class="required">*</span> <em>-the other fields are not required</em></li>
    <li>Click <input type="Submit" value="Save" /></li>
    <li>In the resulting Holder View click <strong><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt=""/> Make changes...</strong></li>
    <li>Select a Holder Type (Plate type) from the drop-down list, enter a short description and click 
    <input type="Submit" value="Save changes"/><br />
    <em> -the <strong>100-place storage box</strong> has been selected in the example</em></li>
  </ul>
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/NewHolder.png" alt="New Holder details" />
    You will now be able to select this as the container for your Stocks see <a href="#samples">Samples</a>.

  <h4>To See a list of Stocks in a Container:</h4>
  <ul>
    <li>Navigate to the list of Containers in PiMS<br />
        &nbsp; Click  <a href="JavaScript:void(0)">Search</a> in the box labelled <strong>Containers</strong> on the Sample or Experiment functions page</li>
    <li>Click the name of the Container of interest to view the details<br />
    &nbsp; <em>-you can also click the holder link <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/holder.gif"/> in the view of the Plasmid stock</em></li>
    <li>Open the box labelled <strong>Samples</strong> to see a list of the Stocks including their positions<br />
    &nbsp; <em>-there is only 1 Stock in the example</em></li>
  </ul>
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/stocks/SamplesInHolder.png" alt="Samples in the Holder" />
      
  <div class="toplink"><a href="#">Back to top</a></div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

