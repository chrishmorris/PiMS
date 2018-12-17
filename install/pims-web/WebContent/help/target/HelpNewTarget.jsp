<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
     <jsp:param name="HeaderName" value='New Target help' />   
     <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target and Construct Help</a>
</c:set>
<c:set var="icon" value="target.png" />
<c:set var="title" value="New Target Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul>
      <li><a href="#newTargOpts">New Target options</a></li>
      <li><a href="#downloadTarget">Download Target details</a></li>
      <li>
        <ul>
          <li><a href="${pageContext.request['contextPath']}/help/target/HelpRecordDNASeq.jsp">Recording a DNA sequence</a></li>
        </ul>
      </li>
      <li><a href="#uploadTarget">Upload</a> a Target from file</li>
      <li><a href="#uploadFASTA">Upload</a> Targets to a Target Group</li>
     </ul>
   </div>   
   <div class="rightcolumn">
     <ul>
      <li><a href="#newTargetForm">Record a Target manually</a> -use a form</li>
      <li><a href="${pageContext.request['contextPath']}/help/target/HelpDnaTarget.jsp#newDNATargetForm">Record a new DNA</a></li>
      <li><a href="${pageContext.request['contextPath']}/help/target/HelpNaturalSourceTarget.jsp#newNaturalSourceTargetForm">Record a new Natural Source Target</a></li>
      <li><a href="#newTargView">New Target View</a></li>     
     </ul>
   </div>
  </pimsWidget:box>

</div> <!--end div help-->

 <div class="helpcontents">
  <div class="textNoFloat">
  <h3 id="newTargOpts">Options for recording a New Target in PiMS</h3>
  To record a New Target in PIMS, first select &quot;New Target&quot; from the Target menu.<br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/newTargetOptions.png" alt="Navigating to New Target form" /><br />
  You will see a form with three options:
  <ul>
   <li>Download Target details from a remote database:</li>
   <li>Upload Target details from a local file:</li>
   <li>Record a new Target manually</li>
  </ul>
  
  </div> <!--end div class="textNoFloat -->
    <div class="toplink"><a href="#">Back to top</a></div>
    
  <h3 id="downloadTarget">New Target option 1: Download Target details</h3>
  This is the simplest way to record the details for a Target in PiMS.<br />
   &nbsp;<strong>note: </strong>you can also use the <strong>Create a new Target</strong> brick on the homepage.<br />
  If you know an accession number for your Target in a database such as Uniprot, GenBank etc:
  <ul>
   <li>Enter the accession number in the form field labelled <strong>Database ID</strong><br /><br /></li>
   <li>Select the Database from the drop-down list<br /><br /></li>
   <li>Click <input type="submit" value="Get record" /><br /><br /></li>
   <li>PiMS will attempt to retrieve the record from the selected database. If this is successful, 
   you will be able to preview the details before using it to create a new Target<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/previewTargetDetails.png" alt="Preview of downloaded Target details"/><br /><br /></li>
   <li>At this point you can also select a <strong>Lab Notebook</strong> to determine who will be able to view the new target details<br /><br /></li> 
   <li>If you are happy with the downloaded file, click <input type="submit" value="Record target" /> to display a PiMS <a href="#newTargView">New Target view</a></li>
  </ul>
   &nbsp; <em>-See <a href="${pageContext.request['contextPath']}/help/target/HelpUploadTarget.jsp">Detailed</a> help for more information.</em> 
    <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="uploadTarget">New Target option 2: Upload Target details from a local file</h3>  
  Target records which have already been downloaded to your local computer can be used to create new Targets in PIMS.<br />
  <ul>
   <li>Locate the second option on the New Target page<br />
    -you can also click <a href="JavaScript:void(0)">New ORF Target</a> in 
   the &quot;Targets&quot; brick on the <strong>Target Functions</strong> page<br /><br /></li>
   <li>Click the browse button and navigate to the file containing the Target record you wish to upload<br />
   &nbsp; <strong>note:</strong> the file to upload must be a &#39;.text&#39; file format<br /><br /></li>
   <li>Click <input class="button" value="Upload file" type="submit" /><br /><br /></li>
   <li>If the file is uploaded successfully you will be able to preview the details and select a Lab notebook for the Target<br /><br /></li>
   <li>Click <input type="submit" value="Record target" /> to display a PiMS <a href="#newTargView">New Target view</a></li>
  </ul>
  
  <h3 id="uploadFASTA">Upload Targets to a Target Group</h3>
  You can create multiple Targets for a Target group by uploading a file containing FASTA formatted DNA sequences. 
  <ul>
    <li>Navigate to a Target Group page by:<br />
     &nbsp; -selecting &quot;Target Groups&quot; from the Target menu then click one of the links in the search results<br />
     &nbsp; -or clicking <a href="JavaScript:void(0)">Search</a> in the &quot;Target Groups&quot; brick on the target Functions page<br />
     &nbsp; -or clicking the <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/targetgroup.gif" alt="target group icon"/><a href="JavaScript:void(0)">Target group</a> 
     link in the box labelled <strong>Target Group</strong> box on a Target Details page<br /><br /></li>
    <li>Navigate to the Box labelled <strong>Upload Target Details</strong><br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/targetGroupView.png" alt="View of a Target group"/><br /><br /></li>
    <li>Click the browse button and navigate to the file containing the FASTA formatted Target DNA sequences you wish to upload<br />
    &nbsp; <strong>note:</strong> the file to upload must meet the following criteria:<br />
        <ol>
            <li>the file must be be a &#39;.text&#39; file format</li>
            <li>the Target DNA sequence must be preceded by a FASTA header line which begins with &quot;&gt;&quot;</li>
            <li>the Target name must follow the &quot;&gt;&quot; symbol with no spaces</li>
            <li>any text following the Target name will be used to record the Target description</li>
            <li>the DNA sequence must start on a new line and can be upper or lower case</li>
        </ol>
        <br />An example file is shown below:<br />
        <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/exampleFASTA_file.png" alt="Example FASTA sequence file for uploading "/><br /><br /></li>
        
    <li>If the Target DNA sequences encode proteins, check the box labelled &quot;Coding sequences&quot; <em>-this is the default</em><br />
        If the Target DNA sequences do <strong>not</strong> encode proteins, leave the box unchecked<br /><br /></li>
    <li>Click <input class="button" value="Save" type="submit" /> -if successful, the new Targets will be added to the list in the <strong>Targets</strong> box<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/uploadedFASTA_Targets.png" alt="Targets uploaded to Target group"/><br /><br /></li>
   <li>PiMS will automatically pre-pend the Target name with a &quot;Well position&quot; which relates to a 96-well plate<br />
   &nbsp; e.g. A01, B01 etc.<br />
   This can be removed by editing the Target details</li>
  </ul>
    <div class="toplink"><a href="#">Back to top</a></div>
  
  <h3 id="newTargetForm">New Target option 3: Record a New Target manually</h3>
  If the details for your Target are not available in one of the remote databases supported by PiMS, you can still make
  a Target record in PiMS by entering the details in a form.
  
  <ul>
    <li>Navigate to the third option on the Target options page -this is near the bottom of the page.<br />
    You will find links to forms for recording a new <a href="JavaScript:void(0)">ORF</a> Target, a new <a href="JavaScript:void(0)">DNA</a> Target
    or a new <a href="JavaScript:void(0)">Natural Source</a> Target.<br />
    You can also find links for recording new Targets in the <strong>Targets</strong> brick on the Target Functions page.
    This section describes how to record an <a href="JavaScript:void(0)">ORF</a> Target.<br />
    &nbsp; <em>for help recording the other types of target see 
    <a href="${pageContext.request['contextPath']}/help/target/HelpDnaTarget.jsp">DNA</a> Target help and 
    <a href="${pageContext.request['contextPath']}/help/target/HelpNaturalSourceTarget.jsp">Natural source</a> Target help</em> 
     <br /><br /></li>
    <li>Click the New <a href="JavaScript:void(0)">ORF</a> Target link</li>
  </ul>  
  An example of a PiMS New Target form is shown below.  The values entered are for Target 
  000194.<br />
  <div class="textNoFloat">
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/spotNewTargetData.png" alt="New Target form with data" />
  <br />
  <ul>
   <li>You must enter <strong>unique</strong> values for the Target <strong>Name</strong> and 
   <strong>Protein name</strong>.<br />
   You may use the same name for both but they should not have been used for any existing
   PiMS Targets.<br />
   PiMS will suggest a possible value for &#39;Name&#39; e.g. a number which 
   has not been used for a Target name, but you may change this.<br /><br /></li>
   <li>Select a Lab Notebook, Organism (species) and Person from the drop-down lists.
   <br /><br />
   If the item you require is not included in list, you will 
   need to create a new record:<br />
   &nbsp; To create a new Species <%--Not really, that would be playing God...--%>record <br />
   &nbsp; &nbsp; -click &#39;Target&#39; in the menubar<br /> 
   &nbsp; &nbsp; -then click <a href="JavaScript:void(0)">Search</a> Organism in the <strong>Reference data</strong> 
   brick on the Target Functions page<br />
   &nbsp; &nbsp; -then click the <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/organism.gif" alt="organism icon"/><a href="JavaScript:void(0)">New Organism</a> link<br />
   <br />&nbsp; To create a new Person record<br />
   &nbsp; &nbsp; -click &#39;User&#39; in the menubar<br />
   &nbsp; &nbsp; -then click <a href="JavaScript:void(0)">New</a> in the <strong>Person</strong> brick on the User Functions page.<br />
   <br />&nbsp; Complete the form and click Submit<br />
   &nbsp; The new item should then appear in the drop-down list in the New Target form.<br /><br />
   &nbsp; <strong>note:</strong> Only Administrator users can create a new Lab Notebook record.
   <br /><br /></li>
   <li>Use <strong>single letter codes</strong> for the Protein sequence.  <em>e.g. for Methionine use <strong>M</strong> not Met</em><br /><br /></li>
   <li>When you have finished, click the &quot;Create&quot; button at the bottom of the form.<br />
   Clicking create without completing all of the mandatory fields will produce a warning:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/spotRequiredFieldsWarning.png" alt="Incomplete form warning" /><br /></li>
   <li>If the DNA or Protein sequence contains non-IUPAC symbols, you will see a warning like this:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/invalidSequenceError.png" alt="Invalid sequence warning" /><br />
   &nbsp;<strong>note:</strong> PiMS does not currently support &#39;U&#39; or &#39;O&#39; 
   in protein sequences and only accepts ACGT for DNA sequences.<br />
   &nbsp; <em> -see <a href="${pageContext.request['contextPath']}/help/target/HelpRecordDNASeq.jsp">Recording a DNA sequence</a></em></li>
  </ul>
  </div> <!--end div class="textNoFloat -->
  <div class="toplink"><a href="#">Back to top</a></div>
   
  <h3 id="newTargView">New Target view</h3>
  <div class="textNoFloat">
  After successfully submitting the form the Target details will be displayed in a 
  &quot;Target Details&quot; page.<br />
  &nbsp; <em> -see <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp#targetDetails">Target Details</a> for more 
  information.</em><br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/targetDetailsTop.png" alt="target details" />
  </div> <!--end div class="textNoFloat -->
  
    <div class="toplink"><a href="#">Back to top</a></div>
  </div> <!--end div class="helpcontent"-->

</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

