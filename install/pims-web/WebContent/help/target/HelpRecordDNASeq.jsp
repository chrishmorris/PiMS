<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

 <jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
   <jsp:param name="HeaderName" value='Recording a DNA sequence in PiMS' />
   <jsp:param name="extraStylesheets" value="helppage" /> 
 </jsp:include>

<%-- HelpRecordDnaSeq.jsp --%>

<c:catch var="error">

<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target and Construct Help</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="DNA Sequence Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul>
      <li><a href="#newTargetDNA">New Target</a> DNA sequence</li>
      <li>
       <ul>
        <li><a href="#codingSequence">Coding sequence</a></li>
      	<li><a href="#startAndStop">Start and Stop codons</a></li>
        <li><a href="#rare">Rare amino acids &#39;U&#39; and &#39;O&#39;</a></li>
      	<li><a href="#ambiguity">Ambiguity codons</a></li>
       </ul>
      </li> 
     </ul>
    </div>
    <div class="rightcolumn">
     <ul>   	
      <li><a href="#fromDownload">Downloaded Targets</a></li>
     </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
 </div> <!--end div help-->

 <div class="helpcontents">
 <h3 id="newTargetDNA">Recording a Target DNA sequence</h3>
  When you create a New Target record in PiMS, you are required to enter a DNA sequence.<br />
  This DNA sequence is used when you design PiMS Constructs 
  <em> -see <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">PiMS Construct design</a></em>.<br />
  PiMS recommends that you adopt the following standards for recording Target DNA sequences.
    
  <h4 id ="codingSequence">Coding sequence</h4>
  <ul>  
   <li>The DNA sequence should contain the coding sequence for the Target protein.<br />
   In PiMS version 2.0, this is assumed to be within the first forward reading frame.<br />
   In future versions of PiMS you will be able to select which reading frame to use for 
   Construct design.<br /><br /></li>   

   <li>The DNA sequence should be a multiple of 3 residues long.<br />
   &nbsp; -if it is not you will see a warning:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/DNASeqError.png" alt="DNA sequence multiple of 3 warning" />
   <br /><br /></li>   

   <li>PiMS currently only accepts &#39;ACGT&#39; DNA symbols.<br />
   If your sequence contains other symbols, you will see this warning:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/invalidDNASequenceError.png" alt="Invalid sequence warning" />
   <br />
   &nbsp; <strong>note:</strong> RNA symbols are not accepted, please replace &#39;U&#39; with &#39;T&#39;<br /></li>
  </ul>
  
  <h4 id="startAndStop">Start and Stop codons</h4>
  <ul>
   <li>If your Target DNA sequence starts with a non-standard start codon 'GTG', 'CTG, or 'TTG'  a message willl be displayed in the Sequences box:<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/targNonStandardStart.png" alt="Sequence pop-up window" /><br />
  You do not need to edit the sequence but when you record a Construct the start codon will not be recognised as coding Methionine and you will be prompted to change it.<br /><br /></li>

   <li>If your DNA sequence contains stop codons (TAG, TAA or TGA) these will be accepted.<br />
   However, if you use the DNA sequence for Construct design, you will see either:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/spotNoStopCodonWarning.jpg" alt="Stop codon included" /><br />
   if you include the &#39;terminal&#39; stop codon, or:<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/internalStopCodonsError.jpg" alt="Internal Stop codon(s) included" /><br />
   if you include any &#39;internal&#39; stop codon(s).
   <br /><br /></li>  
  </ul>
   
   <h4 id="rare">Rare amino acids: Selenocysteine and Pyrrolysine</h4>
   Selenocysteine (Sec or U) and Pyrrolysine (Pyl or O) are
   are rare amino acids encoded by TGA and TAG.<br />
   These are recognised as stop codons in PiMS
   and so it is not possible to create constructs encoding these amino acids. 
   <br />

  <h4 id="ambiguity">Ambiguity codons</h4>
  PiMS supports the use of the following symbols for protein sequences but <strong>NOT</strong> the corresponding ambiguity codons:<br /><br />
  &nbsp;  use <strong>B</strong> for Asx (Asn/Asp) in protein sequences, do <strong>NOT</strong> use RAT or RAC in DNA sequences<br />
  &nbsp;  use <strong>Z</strong> for Glx (Gln/Glu) in protein sequences, do <strong>NOT</strong> use SAA or SAG in DNA sequences<br />
  &nbsp;  use <strong>X</strong> for Xaa (Unspecified) in protein sequences, do <strong>NOT</strong> use NNN in DNA sequences<br /><br />

 <div class="toplink"><a href="#">Back to top</a></div>
  
  
 <h3 id="fromDownload">Targets downloaded from Databases or Files</h3>
  
  PiMS allows you to create a target record by downloading from a remote database or 
  uploading from a file.<br />
  &nbsp;  <em> -see <a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp#downloadTarget">Create</a> a new Target -from a file or database record</em>
  <br /><br />
  In some cases, Target records created in this way lack a DNA sequence because
  this does not form part of the record in the remote database (e.g. Uniprot) or PiMS fails to find a suitable DNA sequence record.
  <br /><br />
  Consequently, you will need to add a sequence manually if you wish to be unable to create PiMS Construct records since a DNA sequence is required.<br /><br />
   <ul>
    <li>Locate and open the <strong>Sequences</strong> box on the target details page.<br /><br /></li>
    <li>Click <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" /> <strong>Make changes...</strong></li>
    <li>Paste the DNA sequence into the appropriate box<br /><br /></li>
    <li>Click <input class="button" value="Save changes" type="submit"/><br /><br /></li>
   </ul>


  
 </div><%--End of Helpcontents--%> 

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
