<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value="Help on 5'-Extensions in PiMS" />
  <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">Recording Constructs</a>
</c:set>
<c:set var="icon" value="" />
<c:set var="title" value="5'-Extensions for Primers"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <ul>
      PiMS allows you to design and record the details of PCR Primers which you may have used in your experiments.
      To assist in this process, PiMS provides an updatable set of 5'-extensions for Forward and Reverse primers 
      which you may use in your Primer design.<br />
    </ul>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li>The Extensions <a href="#extlist">List</a></li>
        <li>Recording a <a href="#extnew">new</a> Extension</li>
          <ul>
            <li>Recording <a href="#processTags">Protein tags</a></li>
          </ul>  
        </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li>Extension <a href="#extdetails">details</a></li>
          <ul>
            <li><a href="#extupdate">Updating</a> the details for an Extension</li>
          </ul>
        <li><a href="#extuse">Using</a> 5'-Extensions in PiMS</li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="extlist">The Extension List</h3>
  To see a list of all of the Forward and reverse 5'-Extensions in PiMS navigate to the Target functions page ("More..." in the Target menu)
  then click <a href="javascript:void(0)">List</a> in the box labelled Reference Data.
  <br /> 
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionListNav.png" alt="Navigate to List of 5'-Extensions in PiMS" />
    <br />
    You will see a page with 2 boxes which contain lists of all Forward and reverse 5'-Extensions recorded in PiMS.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionList.png" alt="5'-Extensions in PiMS" />
   <ul>
    <li>Click the box title <a href="">Forward Extensions</a> or <a href="">Reverse Extensions</a> to see the relevant list<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionListOpen.png" alt="List of 5'-Extensions in PiMS" /></li>
   </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ================================================================================ -->
  <h3 id="extnew">Recording a new Extension</h3>
  To record the details for a new 5'-Extension in PiMS either:<br />
  &nbsp; click &quot;Record a new <a href="javascript:void(0)">Extension</a>&quot; at the top rhs of the Extensions list<br />
  or<br />
  &nbsp; click <a href="javascript:void(0)">New</a> in the Reference Data brick on the Target functions page<br /><br />
  The example below shows a New Extension form containing some example data.<br /><br />
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newExtensionRecording.png" alt="Form with details for a new Extension" />
    <ul>
     <li>Enter a <strong>Name <span class="required">*</span></strong> <em>-this is required and cannot be the same as any other Extension name. Due to the structure of the underlying PiMS database,
     there may also be name clashes with apparently unrelated PiMS records. A possible solution is to add &#39;Fex&#39; or &#39;Rex&#39; to the name.</em></li>
     <li>Enter a <strong>DNA sequence <span class="required">*</span></strong> <em>-this is required and must be entered 5'- to 3'-</em></li>
     <li>Select a <strong>Direction </strong> <em>-Forward or Reverse</em></li>
     <li>You may also enter a <strong>Related protein tag</strong> <em>-this is the sequence of any fusion protein which would be added 
        to the expressed protein if this particular extension was part of a PCR primer. PiMS automatically adds the protein fusion tag 
        to the appropriate terminus of the predicted expression product. See Recording Protein <a href="#processTags">tags</a> for more details</em><br />
     <li>Enter the name of a <strong>Restriction site</strong> <em>-if the Extension sequence encodes a Restriction enzyme recognition sequence</em></li>
     <li>When you are satisfied with the values click <input class="button" value="Create" type="submit" /> to save the details</li>
    </ul>

    The newly recorded Extension will be added to the Extensions list.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionListPlus.png" alt="List with new extension added" />
        
    <h4 id="processTags">Recording Protein tags</h4>    
        <p>
        &nbsp;<strong>Cleavage sites:</strong> if the protein tag is cleavable you can identify the cleavage site with the pipe <strong>|</strong> symbol<br />
        &nbsp;e.g. the Forward Extension &quot;Standard_5'-Infusion-3C&quot; encodes a 3C-protease cleavage site which is recorded as <strong>LEVLFQ|GP</strong><br /><br />
        &nbsp;If a PiMS Construct is created with this Extension the predicted proteins will have the following N-termini:<br />
        &nbsp; Expressed protein LEVLFQGP....<br />
        &nbsp; Final protein GP....</li>
        </p>
        <p>
        &nbsp;<strong>Alternative codons:</strong> an Extension which ends with an incomplete codon can result in proteins with different N-terminal sequences<br />
        depending on the first nucleotide of the gene-specific primer sequence.<br />
        This can be recorded in PiMS with a forward slash <strong>/</strong><br />
        &nbsp;e.g. the Forward Extension &quot;Novagen Ek/LIC-F +X in frame Met or Ile&quot; is recorded in PiMS as <strong>DDDDK|M/I</strong>.<br />
        &nbsp;The Extension encodes an Enterokinse cleavage site and ends with <strong>AT</strong>.<br />
        &nbsp;A complete codon is formed from first nucleotide of the insert-specific region producing either AT<strong>G</strong> (Met) or AT<strong>A</strong>, AT<strong>C</strong>, AT<strong>T</strong> (Ile).<br /><br />
        &nbsp;If a PiMS Construct is created with this Extension the predicted proteins will have the following N-termini:<br />
        &nbsp; Expressed protein DDDDKM.... or DDDDKI....<br />
        &nbsp; Final protein M.... or I....</li>
        </p>
    

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="extdetails">Extension Details</h3>
  To view the details recorded in PiMS for an Extension, click the name in the appropriate row of the Extensions list.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionView.png" alt="View of Details for an Extension" />
  
  <!-- ................................................................................ -->
  <div id="extupdate">
  To update the details for a 5'-Extension click 
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong><br /> 
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/extensionUpdate.png" alt="Updating the Details for an Extension" />
  <br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="extuse">Using 5'-Extensions in PiMS</h3>
  <em>see PiMS Constructs: <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp#extensions">Primer design</a></em>
  <div class="toplink"><a href="#">Back to top</a></div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

