<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help on Creating a New Construct' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target Help</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpConstruct.jsp">Construct Help</a>
</c:set>
<c:set var="icon" value="construct.png" />
<c:set var="title" value="New Construct Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <ul>
  PiMS offers support for recording the details of a Constructs which require the design of PCR Primers.<br />
  The result is the creation of a &quot;Construct Design&quot; Experiment
  with three Output Samples representing the Forward Primer Sample, the Reverse Primer Sample and a Template Sample.
  All of these are then available for use as Input Samples in suitable PiMS Experiments.<br /><br /> 
    </ul>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
          <li><a href="#newConstruct">Navigation</a></li>
          <ul>
            <li><a href="#noDNASeq">Add a DNA sequence</a></li>
          </ul>
          <li><a href="#basicDetails">Construct design</a> Step 1: Basic details</li>
          <li>Step 2: <a href="#step2">Primer overlaps</a> <em>gene-specific region</em></li>
          <ul>
            <li><a href="#detailsStep1">Details from Step 1</a></li>
            <li><a href="#suggPrimers">Possible Primers</a> <em>-designed by PiMS</em></li>    
          </ul>
        </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li>Step 3: <a href="#extAndTags">5'-Extensions and Protein tags</a></li>
        <ul>
          <li><a href="#startstop">Start and Stop</a> codons</li>
          <li><a href="#extensions">5'-Extensions</a></li>
          <li><a href="#prottags">Protein tags</a></li>
        </ul>
        <li>Recording <a href="#primerless">Primer-less</a> Constructs</li>
        <li><a href="${pageContext.request['contextPath']}/help/target/HelpConstruct.jsp#constructDetails">Construct Details</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <div class="leftcolumn">  
      <ul>
        <li><a href="${pageContext.request['contextPath']}/help/target/HelpExtensions.jsp">5'-Extensions</a> for Primers</li>
        <li><a href="${pageContext.request['contextPath']}/help/HelpPrimerOrder.jsp">Ordering Primers</a></li>
      </ul>
    </div>
    <div class="rightcolumn">    
      <ul>
        <li>PiMS support for <a href="${pageContext.request['contextPath']}/help/HelpMutagenesis.jsp">Mutagenesis</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <!-- Start and stop added, Novagen 3C-LIC F and R-->
  <h3 id="newConstruct">Recording Construct details in PiMS</h3>
  To record a New Construct in PIMS, first navigate to the Target Details page for the 
  Target you wish to use.<br /><br />
  Click the 
  <a href="JavaScript:void(0)">New construct</a> link near the top of the page, or the 
  <a href="JavaScript:void(0)">Design new construct</a> in the header of the
  box labelled <strong>Constructs</strong>.<br />
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/sequences.png" alt="Links to make a New Construct" />

  <!-- ................................................................................ -->
  <h4 id="noDNASeq">Add a DNA sequence</h4>
  If your Target does not have a DNA sequence recorded for it you will not see the links to record a new Construct
  <br />
  Instead you will see &quot;
  Can't add construct&quot; near the top of the page and &quot;You need a DNA sequence to create a New Construct&quot; in the Constructs box header.
  <br />
  You will need to add a DNA sequence to the Target before continuing.
  <ul>
    <li>Open the box labelled <strong>Sequences</strong></li>
    <li>Click <input type="Submit" value="Add DNA sequence.."/> if it is displayed</li> 
    <li>Paste in the appropriate sequence(s) and click <input type="Submit" value="Save changes"/> </li>
  </ul>  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ================================================================================ -->
  <h3 id="basicDetails">Step 1: Basic details</h3>
    You will see a page displaying the protein sequence, translated from the Target DNA 
  sequence, and a form for defining the basic details for the New Construct.

    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructBasicDetails.png" alt="New construct step 1" />
  
  <ul>
   <li>To see the DNA and translated protein sequence together, click <a href="JavaScript:void(0)">Pop-up</a><br />
       <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/DNAandProtSeqPopUp.png" alt="DNA and protein sequence popup" />
   <br /><br /></li>
   <li>Enter values for the <strong>Target protein start<span class="required">*</span></strong> and <strong>Target 
   protein end<span class="required">*</span></strong> -to define the region of the target protein you wish to 
   express.  These are required values as indicated by the <span class="required">*</span>.<br />
   By default, these are set to the first and last residues in the translated sequence.
   <em>1 and 599 in the example</em>.<br /><br />
    &nbsp; -if you enter values which are less than 1 or greater than the sequence 
    length you will see a warning and the fields will be highlighted in pink:<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/spotOutOfBounds.png" alt="Start and Stop out of range" />
    </li>
    <li>You must also enter a unique identifier for the <strong>Construct id*</strong>.<br /></li>
    <li>Select a Lab Notebook for the Construct</li>
    </ul>
    <strong>You now have 2 options:</strong><br />
    <ol>
     <li>Click <input class="button" value="Design primers &gt;" type="submit" /> next to the the form field labelled <strong>Desired Tm</strong><br />
     PiMS will then design PCR Primers for the Construct using the <strong>Target protein start<span class="required">*</span></strong> and <strong>Target 
   protein end<span class="required">*</span></strong> positions you have selected and the <strong>default</strong> melt temperature of 60&deg;C<br />
    &nbsp; <em>-you may change this value</em><br /><br /></li>
    <li>Alternatively, you can save the Construct without designing any Primers by clicking <input class="button" value="Save Construct" type="submit" />
     to record a <a href="#primerless">Primer-less</a> Construct</li>
    </ol>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="step2">Step 2: Primer overlaps <em>gene-specific region</em></h3>
  After entering the Basic details for your Construct you will see a page with 3 boxes displaying:<br /><br />
  <strong>1.</strong> The Translated Protein sequence as in the <a href="#basicDetails">previous</a> step.<br />
  <strong>2.</strong> The Construct Basic details, from the previous step.<br /> 
  <strong>3.</strong> An updatable list of possible Forward and Reverse Primers designed by PiMS.<br />

  <!-- ................................................................................ -->
  <h4 id="detailsStep1">Construct Basic details from Step 1</h4>
  In the example the basic details are:<br /> 
  &nbsp; <strong>Construct id:</strong> 000921.con1<br />
  &nbsp; <strong>Target protein start:</strong> 2<br />
  &nbsp; <strong>Target protein end:</strong> 599<br />
  &nbsp; The Protein and DNA sequences as defined by the Target protein start and end positions. 
  
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/designConstruct1_2.png" alt="New construct translated target sequence" />

  <!-- ................................................................................ -->
  <h4 id="suggPrimers">Possible Primers -designed by PiMS</h4>
  The form shows a list of possible Forward and reverse Primers designed at the default Tm of 60&deg;C<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstTmPrimers.png" alt="New construct type 3 Tm" />
  You now have several options:
  <ol>
   <li>Select one <strong>Forward primer</strong> and one <strong>Reverse primer</strong> 
   by clicking the appropriate radio button.<br />
   &nbsp; <em>-the Primers with a Tm closest to the &#39;Default Tm&#39; are selected automatically.</em><br /><br /></li>
   <li>If PiMS the list does not contain any suitable Forward or Reverse primers you may enter a new Tm value and click 
   <input class="button" value="Recalculate" type="submit" /> <br /><br /></li>
<!--TODO not working <li>If PiMS is unable to design any Forward or Reverse primers within 5.0&deg;C of the new Tm, you will see a page 
   like this:<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstTmNoPrimers.png" alt="New construct type 3 No Primers" />
   <br />
   &nbsp; -enter a lower Tm value and click <input class="button" value="Recalculate" type="submit" />.<br /><br />
-->
    <ul>
     <li>if you enter a non-numeric value you will see the following warning:<br />
       <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/TmNumericError.png" alt="Warning Tm is not a number" />    
     </li>
     <li>if you enter a value less than 0, you will see the following warning:<br />
        <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/Tmgt0error.png" alt="Warning if Tm is negative" />
     </li>
    </ul>
   <li>You can also edit the sequence of the default selected primer.<br /><br /></li>     
   <li>When you are happy with your Primers, click 
   <input class="button" value="Next &gt;&gt;&gt;" type="submit" /> to continue 
   or click <input class="button" value="&lt;&lt;&lt; Back" type="submit" /> to return to Step 1<br /></li>  
  </ol>

  <!-- ================================================================================ -->
  <h3 id="extAndTags">5'-Extensions and Protein tags</h3>
  After selecting your Forward and Reverse primer sequences you will see a page with 4 boxes displaying:<br /><br />
  <strong>1.</strong> The Translated Protein sequence.<br />
  <strong>2.</strong> The Construct Basic details, from the first step.<br /> 
  <strong>3.</strong> Details of the selected Forward and Reverse Primers from the previous step.<br />
  <strong>4.</strong> A box labelled <strong>Extensions and Tags</strong> with a form for recording additional details for the Primers and Construct.<br />
  
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstTmTags.png" alt="New construct type 3 Extensions and Tags" />
 

  <!-- ................................................................................ -->  
  <h4 id="startstop">Start and Stop codons</h4>
  <ul>
    <li><strong>Add ATG (start codon)?</strong> this field is only displayed if the region you selected 
   in Step 1, to define the start and end positions, did not begin with an &#39;ATG&#39; start codon.<br />
   If you click in the checkbox you will see the following warning:<br /><br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/addATGwarning.png" alt="Warning that ATG will be added to forward primer 5'-end" />    
   <br /><br />
   Click OK to add ATG to the start of the Forward primer coding region.<br />
   &nbsp; <em>you can uncheck the box to remove the ATG</em><br /><br /></li>
   
   <li>If your sequence starts with a <strong>non-standard start codon</strong> and this is the start of the region you selected in Step 1,
   you will see an option to change this to ATG.<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constNonStandardStart.png" alt="Check box to change non-standard start codon to ATG" />    
   If you check the box you will see the following warning:<br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/replaceNonStandardStart.png" alt="Warning to change start to ATG" />    
   <br /><br />
   Click OK to change the start codon to ATG then click <input type="submit" value="Recalculate"/> for PiMS to re-design your Primers.<br />
   &nbsp; <em>you can uncheck the box to leave the sequence unchanged</em><br /><br /></li>
   
   <li><strong>Add stop codon?</strong> this field is only displayed if the region you selected 
   in Step 1, to define the start and end positions, did not end with a stop codon.<br /><br />
   If you check one of the radio buttons the sequence (5'- to 3'-) will be added to the Reverse primer extension.
   </li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
 
  <!-- ................................................................................ -->  
  <h4 id="extensions">5'-Extensions</h4>
    <strong id="extensions">F.Primer 5'-extension</strong> and <strong>R.Primer 5'-extension</strong>
   <br />&nbsp; -the nucleotide sequence of any 5'-extensions which are to be part of the primer sequence.<br /><br />
   PiMS provides a set of 5'-extensions for Forward and Reverse primers which you may use in your Primer design.<br />
   The complete list is displayed if you click  <a href="javascript:void(0)">Extensions List</a><br /><br />
   <ul>
    <li>To add an extension to the 5'-end of your primer sequence -select an extension from the drop-down list<br />
    &nbsp; if you select &quot;none&quot; none will be added<br />
    If you select an Extension which has a Protein tag associated with it, te tag sequence will appear in the appropriate box for <strong>Protein tag details</strong><br /><br /></li>
    <li>To record a <strong>new</strong> extension which isn't in the list select *Record a new 5'-Extension* from the list<br />
    4 new input boxes will appear<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newExtension.png" alt="Recording a new 5'-extension" />
    <ul>
     <li>Enter a Name<span class="required">*</span> and Sequence<span class="required">*</span>  (5'- to 3'-)<br />
     &nbsp; -if you include any non-IUPAC DNA symbols for extension sequence, you will see a warning like this:<br /><br />
     <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/illegalChrsInTags.png" alt="Warning for non-IUPAC symbols" />
     </li>
     <li>You may also enter an N- (or C-) term protein tag<br />
     <em>this is the sequence of any fusion protein which would be added to the 
     expressed protein if this particular extension was part of your PCR primer. PiMS automatically adds the protein fusion tag to the appropriate terminus of the predicted expression product.</em><br />
     <strong>note:</strong> for more details about protein tags see <a href="${pageContext.request['contextPath']}/help/target/HelpExtensions.jsp#processTags">Recording Protein tags</a>
     <br /><br /></li>
    <li>You may also enter the name of a Restriction enzyme site -if the Extension sequence encodes a Restriction enzyme recognition sequence<br /><br /></li>
    <li><strong>note:</strong> you can also record the details for a new Extension using a separate form<br /></li>
    &nbsp; <em>-see Recording a <a href="${pageContext.request['contextPath']}/help/target/HelpExtensions.jsp#extnew">New Extension</a></em>
   </ul>
   </li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->  
  <h4 id="prottags">Protein tag details</h4>
  
  To complete the Construct design you may now enter the sequences of any Protein tags which will be added to the N- or C-terminus
  of your Construct's expression product. e.g. a His tag sequence.<br /><br />
  Fields are provided for both expressed and final proteins to account for protease-cleavable tags.<br />
  &nbsp; <strong>note:</strong> <em> there may already be a sequence(s) in these fields if you selected a 5'-Extension 
  which has an associated protein tag</em>
  <ul>
   <li>For a tag which is not cleaved enter the <strong>same</strong> sequence for <strong>both</strong> the <strong>-expressed</strong> and <strong>-final</strong> tag<br />
   &nbsp; <em>e.g. for an N-term six His tag, enter <strong>HHHHHH</strong> in the inputs labelled <strong>N-term -expressed</strong> and <strong>N-term -final</strong></em><br /><br /></li>
   <li>For a tag which <strong>will</strong> be cleaved enter the full tag sequence for the <strong>-expressed</strong> tag, and the cleaved tag 
   sequence (if any) for the <strong>-final</strong> tag.<br />
   &nbsp; <em>e.g. for a Thrombin cleavable His tag (which cleaves between the R and G in LVPRGS):<br />
   &nbsp;  you might enter <strong>LVPRGSHHHHHH</strong> in the input labelled <strong>N-term -expressed</strong> and <strong>GSHHHHHH</strong> for the <strong>N-term -final</strong></em><br /><br /></li>
   <li>If you have selected a 5'-Extension for your primer which has a protein tag recorded, it will be displayed
   in the appropriate field(s).</li>
  </ul>

  When you are satisfied with the Primer and tag sequences for your Construct
  click <input class="button" value="Save" type="submit" /> to display the 
  <a href="${pageContext.request['contextPath']}/help/target/HelpConstruct.jsp#constructDetails">Construct Details</a> view.<br />
  or click <input class="button" value="&lt;&lt;&lt; Back" type="submit" /> to return to the previous step.
  
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ================================================================================ -->
  <h3 id="primerless">Recording Primer-less Constructs in PiMS</h3>
  You can record the details for a Construct without using the PiMS Primer design tool.<br />
  The first steps are identical to recording a Construct with Primers.
  <ul>
   <li>Navigate to the View of the target you wish to record a Construct for.<br /><br /></li>
   <li>Click the    <a href="JavaScript:void(0)">New construct</a> link near the top of the page, or the 
    <a href="JavaScript:void(0)">Design new construct</a> in the header of the
    box labelled <strong>Constructs</strong>.<br /><br /></li>
   <li>Enter values for the <strong>Target protein start<span class="required">*</span></strong> and <strong>Target 
    protein end<span class="required">*</span></strong> -to define the region of the target protein you wish to 
    express.  By default, these are set to the first and last residues in the translated sequence.<br /><br /></li>
    <li>You must also enter a unique identifier for the <strong>Construct id<span class="required">*</span></strong>.<br /><br /></li>
    <li>Select a Lab Notebook for the Construct<br /><br /></li>
    <li>Click <input class="button" value="Save Construct" type="submit" /></li>
   </ul>
   You will then see the <a href="${pageContext.request['contextPath']}/help/target/HelpConstruct.jsp#plConstructDetails"">Primer-less</a> Construct details View.
  
  <div class="toplink"><a href="#">Back to top</a></div>
  
</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

