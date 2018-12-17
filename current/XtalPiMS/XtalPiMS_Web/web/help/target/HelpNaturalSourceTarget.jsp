<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
     <jsp:param name="HeaderName" value='PiMS help for Natural Source Targets' />   
     <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
<a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target and Construct Help</a>  
</c:set>
<c:set var="icon" value="target.png" />
<c:set var="title" value="Natural Source Targets Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
    <p>From version 2.2 PiMS provides support for Natural Source Targets. These are Targets for 
    which no protein or DNA sequences are currently known, but that need to be recorded in 
    PiMS so that they can be included in Complexes.</p>
    </div>
  </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Contents">
    <div class="help">
    <div class="leftcolumn">
      <ul>
          <li><a href="#newTarget">New Natural Source Target</a> Recording Natural Source Targets</li>
          <li>
           <ul>
            <li><a href="#navToNewNaturalSourceTarget">Navigation</a></li>
            <li><a href="#newNaturalSourceTargetForm">New Natural Source Target form</a></li>
           </ul>
          </li>
       </ul>
    </div>
    </div>
    <div class="rightcolumn">
      <ul>  
          <li><a href="#targDetails">Natural Source Target Details</a></li>
          <li>
           <ul>
            <li><a href="#detailsTop">Form details</a> as recorded</li>
            <li><a href="#detailsBottom">Target-related Information</a></li>
           </ul>
          </li>
      </ul>
    </div> 
    </pimsWidget:box>

 <div class="helpcontents">
 <h3 id="newTarget">New Natural Source Target: Recording Natural Source Targets</h3>
  <div class="textNoFloat" id="navToNewNaturalSourceTarget">
  To record the details of a Natural Source Target in PiMS:<br />
  &nbsp; either select &#39;Create New Target&#39; from the Target menu then "New natural Source Target"
  at the bottom of the page.<br /><br />
	
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/navToNewNaturalSourceTarget.jpg" alt="Natural Source Target menu item" /><br /><br />
 <h4 id="newNaturalSourceTargetForm">New NaturalSource Target form</h4> 
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/newNaturalSourceTargetForm.jpg" alt="New Natural Source Target form" /><br /><br />
  <ul>
   <li>All fields are mandatory -as indicated by the red <span class="required">*</span> after the field names<br /><br /></li>
   <li>PiMS will provide a <strong>unique</strong> six digit number for the Target <strong>name: </strong><em>000064 in the example</em><br />
   You may change this to a more meaningful name but it must not be the name of an existing Target in PiMS.<br /><br />
   </li>
   <li>The <strong>Description</strong> field is used to categorize the type of DNA element the Target represents.<br />
   Select a value from the list of: Intron, Promoter, Enhancer, 5'-UTR, DNA, genomic DNA, non-coding region, Vector, and CRM.<br /><br /></li>
   <li>Select a value from the <strong>Lab Notebook</strong> list <em> -the list only includes the names of Lab Notebooks which you are able to use</em><br /><br /></li>
   <li>Select an <strong>Organism</strong> from the list.<br />
    <em>The list contains the names of commonly used model organisms.
    These are provided as <a href="${pageContext.request['contextPath']}/help/reference/HelpRefData.jsp">Reference data</a> in PiMS.
    If the name of the Organism you wish to use for your Target record does not appear in the list, please contact 
    your PiMS administrator who will be able to update the Reference data.</em><br /><br /></li>
   <li>By default the <strong>Person</strong> field displays the name recorded for the user who is currently logged in. <em> -Demo PiMS in the example</em><br />
   You may change this by selecting a different name from the list.<br /><br /></li>
   <li>When you have completed all of the mandatory fields click the
   <input class="button" value="Submit" type="submit" /> button<br />
   The Target information will be recorded and displayed in a PiMS Target details view.<br /><br /></li>
  </ul>
<div class="toplink"><a href="#">Back to top</a></div>
</div> <!-- end div class="textNoFloat" -->
  
  <h3 id="targDetails">Natural Source Target Details</h3>
  <div class="helpcontents">
   <div class="textNoFloat">
   The Natural Source Target Details View is displayed after clicking the
   <input class="button" value="Submit" type="submit" /> button when you record a new Natural Source Target in PiMS.<br /> 
   You can also see the details for a Natural Source Target when you:<br />
   &nbsp; -click the link in the results from a &#39;Target Search&#39; or the &#39;Browse Targets&#39; page.<br />
   &nbsp; -select &#39;View&#39; from the context menu for a Natural Source Target record in either the &#39;History&#39; or 
   &#39;Active Targets&#39; brick on the PiMS Home page.
   <br />

   <h4 id="detailsTop">Form details as recorded</h4>
    The top part of the Natural Source Target Details View contains a form displaying the information which has been recorded.<br />
   	<br /><br />   
	<img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/naturalSourceTargetDetailsTop.jpg" alt="Natural Source Target Details top part" /> 
	<br />
	<ul>
   	 <li>The form contains links labelled <a href="javascript:void(0)">View</a> next to the <strong>Web page</strong>
     and <strong>Organism</strong> fields.  Clicking these will open a new browser tab displaying the Web page or NCBI 
     Taxonomy page for the Organism.<br /><br /></li>
     <li>Some of the fields are editable.<br /> If you have permission to do so you can change the Target 
	 <strong>Name</strong>, <strong>Web page</strong>, and the <strong>Comments</strong>.
	 You can select different values for the <strong>Description</strong>, <strong>Organism</strong> and <strong>Scientist</strong> 
	 from drop-down lists.<br /><br /></li>
	 <li>You may also enter a <strong>Protein</strong> sequence and <strong>DNA</strong> sequence should they become known.<br /><br /></li>
	 <li>Click <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/viewdiagram.gif" alt="Diagram icon" /> <a href="javascript:void(0)">Diagram</a>
	 to see a diagramatic representation of the Target.<br /></li>
	</ul>
	
   <h4 id="detailsBottom">Target-related Information</h4>
   The lower part of the Natural Source Target Details View contains additional information about the Target.<br />
   This includes details of any Constructs, Experiments and Database references which may have been recorded for the Target.<br /><br /> 
	<img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/naturalSourceTargetDetailsBottom.jpg" alt="Natural Source Target Details lower part" /><br /><br />
	<ul>
	 <li>Details of any <strong>Constructs</strong> which have been recorded for the Target will appear in a Table below the form.<br />
	  &nbsp; <em>-there can be no constructs for a Natural Source Target unless a DNA sequence of more than 50 nucleotides has been recorded.</em><br /><br /></li>
	 
	 <li>Use the <input class="button" value="Browse" type="submit" /> and <input class="button" value="Upload" type="submit" /> buttons
	 to attach files to the Target record.<br /><br /></li>
	</ul>
    <div class="toplink"><a href="#">Back to top</a></div>
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helpcontents"-->

</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>
 
<jsp:include page="/JSP/core/Footer.jsp" />