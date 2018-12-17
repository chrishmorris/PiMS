<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for DNA Targets' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target and Construct Help</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp">New Target Help</a>
</c:set>
<c:set var="icon" value="target.png" />
<c:set var="title" value="New DNA Target Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      <p>PiMS provides support for DNA (non-protein) Targets. These are Targets which 
        do not lead to protein production.  This functionality can be used to record Target and 
        Construct details for projects where non-coding regions of genes, such as promoter regions,
        are cloned.<br /><br />
        Such Target and Construct records can be used in the same way as standard PiMS Targets
        and Constructs. <br />
        <em>Thanks to the OPPF for the 5&#39;-extension sequences</em>
       </p>
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul>
      <li><a href="#newTarget">New DNA Target</a> Recording non-protein Targets</li>
      <li>
       <ul>
        <li><a href="#navToNewDNATarget">Navigation</a></li>
        <li><a href="#newDNATargetForm">New DNA Target form</a></li>
       </ul>
      </li>
      <li><a href="#targDetails">DNA Target Details</a></li>
      <li>
       <ul>
        <li><a href="#detailsTop">Form details</a> as recorded</li>
        <li><a href="#detailsBottom">Target-related Information</a></li>
       </ul>
      </li>
     </ul>
   </div>
   <div class="rightcolumn">
    <ul> 
        <li><a href="#newConst">Recording a Construct</a> for a DNA Target</li>
        <li>
          <ul>
            <li><a href="#newConstStep1">Step 1</a> Basic details</li>
            <li><a href="#newConstStep2">Step 2</a> Select primers</li>
            <li><a href="#newConstStep3">Step 3</a> Primer Extensions</li>
            <li><a href="#newConstStep4">Step 4</a> Construct Summary</li>
          </ul>
        </li>
        <li><a href="#constDetails">DNA Construct Details</a></li>
      </ul>
    </div>
  </pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 <div class="helpcontents">
  <h3 id="newTarget">New DNA Target: Recording non-protein Targets</h3>
  <div class="textNoFloat" id="navToNewDNATarget">
  To record the details of a DNA Target in PiMS:<br />
  &nbsp; click <a href="javascript:void(0)">New Target</a> in the Target menu<br />, 
  then "new DNA target" at the bottom of the page.
  <br /><br />
	
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/navToNewDNATarget.png" alt="DNA Target menu item" /><br /><br />
  Then click New <a href="javascript:void(0)">DNA</a> Target towards the bottom of the Record New Target page.<br /><br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newTargetOptions.png" alt="Options for recording a new Target" /><br /><br />
 <h3 id="newDNATargetForm">New DNA Target form</h3> 
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNATargetForm.png" alt="New DNA Target form" /><br /><br />
  <ul>
   <li>The 2 fields labelled &#39;Name&#39; and &#39;Sequence name&#39; are mandatory -as indicated by the <span class="required">*</span><br /><br /></li>
   <li>PiMS will provide a <strong>unique</strong> six digit number for the Target <strong>Name<span class="required">*</span></strong> <em>000001 in the example</em><br />
   You may change this to a more meaningful name but it must not be the name of an existing Target in PiMS.<br />
   <strong>note:</strong> this name will prepended to the identifier for any Construct records you create for this Target<br />
   &nbsp; <em>see <a href="#newConst">Recoding a Construct</a> for a DNA Target</em><br /><br />
   &nbsp; If the name you enter is not unique you will see the following warning:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/duplicateTargetIDWarning.jpg" alt="Warning for duplicate Target ID" /><br /><br />
   </li>
   <li>You may enter a GI number to identify an entry for the target in the GenBank database.  
   PiMS will use this value to create a link to the record.<br /><br /></li>
   <li>Select an <strong>Organism</strong> from the list.<br />
    <em>The list contains the names of commonly used model organisms.
    These are provided as <a href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp">Reference data</a> in PiMS.
    If the name of the Organism you wish to use for your Target record does not appear in the list, please contact 
    your PiMS administrator who will be able to update the Reference data.</em><br /><br /></li>
   <li>The <strong>Function description</strong> field is used to categorize the type of DNA element the Target represents.<br />
   Select a value from the list of: Intron, Promoter, Enhancer, 5'-UTR, DNA, genomic DNA, non-coding region, Vector, and CRM.<br />
   You may change this after the target record has been created.<br /><br /></li>
   <li>Select a value from the <strong>Lab Notebook</strong> list <em> -the list only includes the names of Lab Notebooks which you are able to use.</em><br /><br /></li>
   <li>By default the <strong>Scientist</strong> field displays the name recorded for the user who is currently logged in. <em> -Demo PiMS in the example</em><br />
   You may change this by selecting a different name from the list.<br /><br /></li>
   <li>Any additional details can be recorded in the <strong>Comments</strong> field.<br /></li>
   <li>Enter a DNA sequence for the Target<br />
   <strong>note:</strong> if you wish to record details of any Constructs for this Target, the sequence must be &ge;50 nucleotides long.<br />
      &nbsp; <em>see <a href="#newConst">Recoding a Construct</a> for a DNA Target</em><br /><br /></li>
   <li>When you have completed all of the mandatory fields click the
   <input class="button" value="Create" type="submit" /> button.<br />
   The Target information will be recorded and displayed in a PiMS DNA Target details view.<br /><br /></li>
  </ul>
  <a href="#top">back to top</a>   
<div class="toplink"><a href="#">Back to top</a></div>
   </div> <!-- end div class="textNoFloat" -->
  
   <h3 id="targDetails">DNA Target Details</h3>
   <div class="textNoFloat">
   The DNA Target Details View is displayed after clicking the
   <input class="button" value="Create" type="submit" /> button when you record a new DNA Target in PiMS.<br /> 
   You can also see the details for a DNA Target when you:<br />
   &nbsp; -click the link  in the results from a &#39;Target Search&#39; or the &#39;Browse Targets&#39; page.<br />
   &nbsp; -select &#39;View&#39; from the context menu for a DNA Target record in the &#39;History&#39; brick on the PiMS Home page.
   <br /><br />
   The Target details view page includes an icon, identifying the view as a PiMS Target, a link to a list of all Targets in PiMS, 
   and updatable information, about the individual Target, in a series of PiMS boxes.<br /><br />
	<img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/dnaTargetDetails.png" alt="DNA Target Details View" /> 
	<br />

   <h4 id="detailsTop">Target details as recorded</h4>
   <ul>
    <li>The first box, which is open by default, includes the Target names, source organism, function, the name of the scientist responsible for the Target and 
 	any additional comments.<br />
 	If you have permission to edit the Target details, you will see <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make Changes...</strong>
 	at the bottom right hand side of the box. <em>-this is not displayed in the example</em><br /><br /></li>
 	<li>Open the box labelled <strong>DNA Sequence</strong> to see the Target DNA sequence.<br />
 	The length and &#037; of GC nucleotides in the sequence are also shown.<br />
 	Click <a href="javascript:void(0)">Fasta pop-up</a> to see the entire Target DNA sequence in Fasta format.<br /><br /> 
	 <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/dnaTargetSequence.png" alt="DNA Target Sequence" /></li>
   </ul>	
   <h4 id="detailsBottom">Target-related Information</h4>
   The lower part of the DNA Target Details View contains additional information about the Target.<br />
   This includes details of any Constructs, Experiments and Database references which may have been recorded for the Target.<br /><br /> 
	<ul>
	 <li>Details of any <strong>Constructs</strong> which have been recorded for the Target will appear in the box labelled &#39;Constructs&#39;.<br />
	  &nbsp; <em>-there are no Constructs recorded for the Target in the example.</em><br />
	  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/dnaTargetNoConstructs.png" alt="DNA Target with no Constructs" /><br /><br /></li>
	 <li>Click <a href="javascript:void(0)">New Construct</a> to record the details for a Construct.
	 <em>-see <a href="#newConst">Recoding a New Construct for a DNA Target</a></em><br />
	 &nbsp; <strong>note:</strong> It is not possible to record Construct details if the DNA sequence is &lt;50 nucleotides long.<br />
	 &nbsp; This will be indicated by the following message:<br />
	  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/shortDNATargetWarning.png" alt="Short Target sequence message" />
	 <br /><br /></li>
	 <li>Database References for the Target are listed in the box labelled &#39;Database references&#39;.<br />
	  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/dnaTargetDbRefs.png" alt="DNA Target DbRefs" /><br />
	  Click the link in the &#39;Accession&#39; column to view the web page.<br />
	 <em>&nbsp; -the example shows a link to a GenBank reference created from the value entered for the GI number 
	 when the Target details were recorded in PiMS.</em><br /><br /></li>
	 <li>Open the Box labelled &#39;Blast searches&#39; then click <a href="javascript:void(0)">EMBL Blast results</a> to see the results of a Blast search against the EMBL database.<br />
	 &nbsp; <em> -see <a href="${pageContext.request.contextPath}/help/target/HelpBlastTarget.jsp#emblBlast">Blast help</a> for more details.</em><br /><br /></li>
	 <li>The boxes labelled &#39;Images&#39; and &#39;Attachments&#39; allow you to associate images, with legends, and other files
	 with the Target.<br /><br /></li>
	 <li>The box labelled &#39;Notes&#39; is to record any additional details about the Target.  Each note is saved with the date it was recorded
	 allowing you to track any changes made to the Target detals.</li>
	</ul>
   </div> <!-- end div class="textNoFloat" -->
  <a href="#top">back to top</a>   
<div class="toplink"><a href="#">Back to top</a></div>
  
  <h3 id="newConst">Recording a New Construct for a DNA Target</h3>
   <div class="textNoFloat">
   To record a new &#39;Construct&#39; for a DNA Target either:<br /> 
   &nbsp; -click <a href="javascript:void(0)">New construct</a> on the Target Details page<br />&nbsp; or: <br />
   &nbsp; -select 
   <a href="javascript:void(0)">New Construct...</a>
   from the &#39;Context menu&#39; for a DNA Target in the &#39;History&#39; brick on the Home page.<br />  
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/navNewDNAConstruct.jpg" alt="New Construct in Target context menu" /><br />
   You will see Step 1 of the New DNA Construct wizard. 
    <h4 id="newConstStep1">New DNA Construct Step 1:  Basic details</h4>
    The DNA Target sequence is displayed along with a form to define the region of the sequence to be used for the Construct.<br />  
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep1.png" alt="New DNA Construct Step 1" /><br /><br />
	<ul>
	 <li>Enter the start and end nucleotides for the Construct.<br />
	 By default these are the  first and last nucleotides in the DNA sequence. <em> 1 and 576 in the example</em><br /><br /></li>
	 <li>Enter an identifier for the new Construct; this will be appended to the Target name to create the Construct Name
	 <br /><br /></li>
	 <li>Click <input class="button" value="Next &gt;&gt;&gt;" type="submit" /> to proceed to Step 2.<br />
	 If you enter unsuitable values for the start and end nucleotides you will see an appropriate warning.<br /><br />
      </li>
	</ul>
	<a href="#top">back to top</a>   
 <div class="toplink"><a href="#">Back to top</a></div>
	
    <h4 id="newConstStep2">New DNA Construct Step 2:  Select Primers</h4>
    Step 2 displays 3 PiMS boxes.<br />
    The first contains the Target DNA sequence.<br /><br />
    The second, labelled &#39;Basic details from Step 1 for:&#39; contains the Construct Id, generated from the Target Name and the 
    id value entered in Step 1&nbsp; <em> -in the example this is 000918.con1</em>,<br />
    and the Start and End nucleotides defined in Step 1&nbsp; <em> -in the example these are 145, 500</em>.<br /><br />
    
    It also shows that a default value of 60&deg;C is used to design Primers for the Construct.<br /><br />
    Below this is the Construct DNA sequence.<br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep2Top.png" alt="New DNA Construct Step 2 Top part" /><br /><br />
	
	The third box contains a list of suggested Forward and Reverse Primers:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep2Bottom.png" alt="New DNA Construct Step 2 Lower part" /><br /><br />
	 <ul>
	  <li>If there are no suitable primers enter a different Primer design Tm value and click <input class="button" value="Recalculate" type="submit" />
	  to see updated lists.<br /><br /></li>
	  <li>Click the radio buttons to select 1 Forward and 1 Reverse primer then Click 
	  <input class="button" value="Next &gt;&gt;&gt;" type="submit" /> to proceed to Step 3<br /><br /></li>
	  <li>Click <input class="button" value="&lt;&lt;&lt; Back" type="submit" /> or the Browser back button, to return to Step 1<br /><br /></li>
	 </ul>
	<a href="#top">back to top</a>   
 <div class="toplink"><a href="#">Back to top</a></div>
	 
    <h4 id="newConstStep3">New DNA Construct Step 3:  Primer Extensions</h4>
    Step 3 displays 4 PiMS boxes.
    These contain the &quot;Target DNA sequence&quot; and the &quot;Basic details from Step 1&quot;.<br /><br />
    The third box displays the details of the primers selected in Step 2 including: the sequence, length, Tm and &#037; of GC nucleotides 
    for the Forward and Reverse primer.<br /><br />
    The fourth box, labelled &quot;Primer 5&#39;-Extensions&quot; includes drop-down lists of 5&#39;-extensions for the primers.<br />
    &nbsp; <strong>note:</strong> the 5&#39;-extensions are PiMS Reference data.<br />
   	You can see a complete list of all the 5&#39;-extensions in PiMS by clicking <a href="javascript:void(0)">Extensions List</a><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep3.png" alt="New DNA Construct Step 3" /><br /><br /> 
	 <ul>
	  <li>Click in a box to see the list of available 5&#39;-Extensions for the Forward or reverse primer.<br />
	   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep3Ext.png" alt="New DNA Construct Step 3 with Extensions list" /><br /><br /></li>
	  <li>Select a 5&#39;-Extension to add to your primer sequence<br /><br /></li>
	  <li>or select &#39;none&#39; if you do not wish to add a 5&#39;-Extension to your primer.<br /><br /></li>
	  <li>If the list does not contain a suitable sequence, select *Record a new 5&#39;-extension*<br />
	  You will see two new fields to enter a Name and Sequence for a new 5&#39;-Extension for the Forward or Reverse primer.<br />
	  &nbsp; <em>-the example shows the fields displayed for recording a new Forward primer extension</em><br />
	   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep3NewExt.png" alt="New DNA Construct Step 3 with Extensions list" /><br /><br /></li>
	  <li>When you have completed your selection click <input class="button" value="Next &gt;&gt;&gt;" type="submit" /> to proceed to Step 4<br /><br /></li>
	  <li>or click <input class="button" value="&lt;&lt;&lt; Back" type="submit" /> or the Browser back button, to return to Step 2.<br /><br /></li>
	 </ul>
    <h4 id="newConstStep4">New DNA Construct Step4: Construct Summary</h4>
    Step 4 displays 4 PiMS boxes.<br />
    These contain the &quot;Target DNA sequence&quot; and the &quot;Basic details from Step 1&quot;.<br /><br />
    The third box displays the &quot;Primer details and properties&quot;.<br />
    This includes the Length, Tm and &#037;GC for the both the complete sequence
    and the gene-specific region, or &#39;Overlap&#39;, for both the Forward and Reverse primers.  The sequence of 
    any &#39;-extension is also shown.<br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newDNAConstStep4.png" alt="New DNA Construct Step 4" /><br /><br />
    The fourth box contains a form for recording some additional details:<br /><br />
     <ul>
      <li>You may enter a short <strong>Description</strong> for the Construct.<br />
      More details can be entered in the <strong>Comments</strong> field</li>
      <li>Select a <strong>Person</strong> from the drop-down list <em>-the name of the user who is the currently logged in user is selected by default</em></li>
	  <li>Click
	  <input class="button" value="Submit" type="submit" /> to create a new Construct record in PiMS or Click the 
	  <input class="button" value="Back" type="submit" /> button, or the Browser back button, to return to Step 3<br /><br /></li>
	 </ul>
	 Clicking <input class="button" value="Submit" type="submit" /> takes you to <strong>Constructs</strong> table on the parent Target details View<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/dnaTargetConstructList.png" alt="DNA Construct List" /><br /><br />
     The Table includes the Construct Name, derived from the Target name and the Construct ID <em>&nbsp; - &#39;000918&#39; and &#39;con1&#39; in the example</em><br />
      and any description entered in Step 4 <em> &nbsp; - &#39;Novagen EK/LIC&#39; in the example</em>.<br />  
   </div> <!-- end div class="textNoFloat" -->

<div class="toplink"><a href="#">Back to top</a></div>
  <h3 id="constDetails">DNA Construct Details</h3>
  
   <strong>The view of a DNA Construct  includes:</strong><br /><br />
    <ul>
     <li>&nbsp; <strong>Sequences</strong> of the entire Forward &amp; Reverse Primers, the Gene-specific region and any 5&#39;-Extension<br /><br /></li>
     <li>&nbsp; The <strong>Length</strong>, <strong>Tm</strong> and <strong>&#037;GC</strong> for the entire Forward &amp; Reverse Primers and the Gene-specific region<br /><br /></li>
     <li>&nbsp; <strong>Predicted PCR product</strong> sequence, length and &#037;GC<br /><br /></li>
     <li>&nbsp; Editable  of a <strong>Description</strong> and <strong>Comments</strong> fields<br /><br /></li>
     <li>&nbsp; A link to the PiMS record for the <strong>Scientist</strong> responsible for the Construct<br /><br /></li>
    </ul>
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helpcontents"-->
<div class="toplink"><a href="#">Back to top</a></div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
