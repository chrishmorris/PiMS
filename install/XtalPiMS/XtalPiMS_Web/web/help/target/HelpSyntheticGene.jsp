<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Synthetic genes in PiMS' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request.contextPath}/help/HelpTarget.jsp">Target Help</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Help for Synthetic genes"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
     Support for recording Synthetic genes in PiMS is available from version 4.2<br /><br />
     PiMS allows you to record the DNA and protein sequence of the synthetic gene, details of any 5'- and 
     3'-restriction sites, the vector it has been cloned into and the host organism it is to be expressed in.<br/>
     You may also upload any additional files such as the information provided by the supplier.<br /><br />
     PiMS Construct management has been modified to record the details of any Constructs you create based on 
     the synthetic gene DNA sequence. Synthetic genes are linked to a parent PiMS Target. 
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li>Recording a <a href="#newSg">New Synthetic gene</a><br /><br /></li>
        <li><a href="#sgDetails">Synthetic gene details</a></li>
      </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#locatSG">Locating Synthetic genes</a><br /><br /></li>
        <li><a href="#sgConstructs">Constructs for Synthetic genes</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="closed" title="Related Help">
    <ul>
      <li><a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">New Construct Help</a></li>
    </ul>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="newSg">Recording a New Synthetic gene</h3>
  Synthetic genes are recorded in PiMS as a special type of <strong>Output Sample</strong> from a Synthetic gene Experiment linked to a particular Target.<br /><br />
  To record a New Synthetic gene in PiMS first navigate to the View of the parent Target and locate the link to <strong>New Synthetic gene</strong> 
  near the top of the page.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newSGLink.png" alt="Link to record a new Synthetic gene" />
   <br />
   You will then see a form to record the details:<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newSGForm.png" alt="Form to record a new Synthetic gene" />
   <br />
   <ul>
    <li>Enter values for the 2 required fields: <strong>Name<span class="required">*</span></strong> and <strong>DNA sequence<span class="required">*</span></strong> for 
    the Synthetic gene<br /><br /></li>
    <li>You can link your Synthetic gene record to a Vector Sample in PiMS by selecting one from the drop-down list<br />
     &nbsp;<strong>note:</strong> you will need to have created Vector Sample records first<br />
     &nbsp; <em>-PiMS provides Recipes for commonly used Vectors which can be used to record Vector Samples.<br />
     &nbsp; You can see a list by clicking <a href="JavaScript:void(0)">Search Vectors</a> on the Sample Functions page</em> ("More..." in the Sample menu)<br /><br /></li>
    <li>As for most PiMS pages, you can also assign the Synthetic gene record to a <strong>Lab notebook</strong> <em>-PiMS automatically selects the Lab notebook for the parent Target</em><br />
    The <strong>Scientist</strong> responsible for the Synthetic gene can be selected from the drop-down list <em>-the default is the currently logged in user</em> <br /><br /></li>
    <li>You can also enter values for any <strong>5'-</strong> and /or <strong>3'-Restriction sites</strong> in the Synthetic gene sequence, any <strong>Vector resistances</strong> e.g. antibiotic
    resistance markers, <strong>Optimized for expression in:</strong> -the organism that the gene is to be expressed in and the <strong>Protein sequence</strong><br /><br /></li>
    <li>When you have finished click the <input class="button" value="Create" type="submit"/> button to display the Synthetic gene record</li>
   </ul>     
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="sgDetails">Synthetic gene details</h3>
  After successfully submitting a New Synthetic gene form the details will be displayed in a Synthetic gene page:<br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/syntheticGeneView.png" alt="View for a Synthetic gene" />
   <br />
   <ul>
    <li>The page contains a breadcrumb trail with a link to the parent Target
        <pimsWidget:linkWithIcon 
                icon="types/small/target.gif" 
                url="JavaScript:void(0)"
                text="000933" />
    <br /><br /></li>
    <li>The details are displayed in 2 boxes <strong>Basic details</strong> and <strong>Sequences</strong> and 
    can be updated by clicking an appropriate 
    <strong><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt=""/> Make changes...</strong> link<br /><br /></li>
    <li>There are also boxes labelled <strong>Constructs</strong> with a link to record a New construct <em>-see 
    <a href="#sgConstructs">Constructs for Synthetic genes</a></em> and the standard PiMS boxes for recording <strong>External Database Links</strong> 
    and <strong>Notes</strong> and for uploading <strong>Images</strong>, <strong>Attachments</strong>. 
    These can be used to attach any supplier information and data files for the Synthetic gene<br /><br /></li>
    <li>A new box labelled <strong>Synthetic genes</strong> will be displayed on the parent Target details page and a new Synthetic gene Experiment will be included Experiments box</li>
   </ul>   
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="locatSG">Locating Synthetic genes</h3>
  After you have recorded a Synthetic gene in piMS you will be able to find as follows:
  <ul>
    <li>Navigate to the parent Target, locate the box labelled <strong>Synthetic genes</strong> and click the appropriate link e.g.
        <pimsWidget:linkWithIcon 
                icon="types/small/sample.gif" 
                url="JavaScript:void(0)"
                text="SGene001" />
    <br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/synthGenesBox.png" alt="Box for Synthetic genes on Target view" />    </li>
    <li>Navigate to the parent Target, locate the box labelled <strong>Experiments</strong> and click the appropriate link e.g.
        <pimsWidget:linkWithIcon 
                icon="types/small/experiment.gif" 
                url="JavaScript:void(0)"
                text="SGene001" />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targetSGeneExpt.png" alt="Target Experiments" /><br />
        then click the link in the Experiment view to display the Output sample<br /><br /></li>
    <li>Select &quot;Search Samples&quot; from the Sample menu and limit the search to Synthetic gene records by selecting 
     &quot;Synthetic gene&quot; from the the drop-down list of Sample categories OR enter the name of the Synthetic gene in the <strong>Name</strong> field<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/sgSearch.png" alt="Search for synthetic gene" /><br /><br /></li>
    <li>If you have recently recorded or viewed a PiMS Synthetic gene record it will appear as a Sample link in the <strong>History menu</strong></li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="sgConstructs">Constructs for Synthetic genes</h3>
  You can record Constructs for Synthetic genes in the same way as for PiMS Targets.<br />
  Provided that the Synthetic gene has a suitable DNA sequence and you have permission to do so, you will see a
        <pimsWidget:linkWithIcon 
                icon="actions/create/construct.gif" 
                url="JavaScript:void(0)"
                text="New Construct" />link near the top of the page. 
  You will also see a <a href="JavaScript:void(0)">Design new Construct</a> link in the header of the <strong>Constructs</strong> box.<br /><br />
  Clicking either of these links will display Step 1 of the PiMS Construct design functionality.<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/sGeneNewConst.png" alt="Synthetic gene New Construct Step 1" /><br />
  This differs from the standard PiMS Construct design in 2 ways:
  <ol>
   <li>The breadcrumb trail contains an additional link to the Synthetic gene Sample
   <em> -this will also be the case in the Construct view</em><br /><br /></li>
   <li>The DNA sequence recorded for the Synthetic gene is used instead of the Target DNA sequence for Primer and Construct design</li>
  </ol>
  Any Constructs designed for Synthetic genes will be included in the <strong>Constructs</strong> box for both the Synthetic gene and the parent Target.<br /><br />
  &nbsp; <em> -see <a href="${pageContext.request.contextPath}/help/target/HelpNewConstruct.jsp">New Construct Help</a> for more information about recording Constructs in PiMS</em> 
  <div class="toplink"><a href="#">Back to top</a></div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

