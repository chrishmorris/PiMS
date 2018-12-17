<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Creating a new Target in PIMS from a file or record' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<div class="help">


<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#notes">General Notes</a></li>
  <li><a href="#newTargetOptions">Create a new Target</a> from a file or database record</li>
  <li>
   <ul>
    <li><a href="#opt1">Download Target details</a> from a remote database</li>
    <li><a href="#opt2">Upload Target details</a> from a local file</li>
    <li><a href="#preview">Preview</a> downloaded Target details</li>
    <li><a href="#multipleCDS">Targets from files or records with multiple CDS</a> (Coding sequences)</li>
   </ul>
  </li>
  <li><a href="#viewTarget">View</a> the target details</li>
	<li><a href="#databaseReferences">View</a> database references &amp; source records</li>
	<li><a href="#expBlue">Experiment Blueprint</a></li>
  <li><a href="${pageContext.request['contextPath']}/help/target/HelpRecordDNASeq.jsp#fromDownload">Recording a DNA sequence</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />
 <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Guide</a> to PIMS Target management <br />

<div class="toplink"><a href="#">Back to top</a></div>

 
  <div class="textNoFloat">
  <h4 id="notes">General notes</h4>
  PiMS supports 3 formats as source records for targets. These are Genbank (both protein and nucleotide), EMBL (nucleotide) and Uniprot/SwissProt (protein).
  There are a number of databases which provide records in these formats. In the majority of cases you only need a database accession number to create a PiMS Target record. 
  If your chosen database is not supported by PiMS, or you would like to use an older record, you may be able to create a PiMS Target record by uploading a file. 
  A PiMS Target requires both a DNA and a matched protein sequence. However, some database records contain only one of these. In this case PiMS will attempt to find a
  record containing a complementary sequence automatically. PiMS will also report where it found a record and, if you are not satisfied with this record, you can replace the sequence with a suitable 
  one after the Target has been recorded in PiMS. Where PiMS is unable to find a complementary sequence you can edit the PiMS Target record manually.
  <div class="techdetails">
  	<p>To find a complementary sequence, PIMS looks for cross-database references in the source file provided.</p> 
  	<ul>
  	 <li>For Genbank protein records PiMS uses the entire protein sequence to record the Target protein sequence. To find a nucleotide sequence, PIMS uses 
  	 the &#39;CDS&#39; feature property "coded_by". This provides the details of the record and the coding region of the DNA sequence.</li>
	 <li>For Genbank nuleotide records with a single CDS, PiMS uses the entire sequence as the Target DNA sequence. The protein sequence is extracted from 
	 the &quot;translation&quot; property of a given CDS feature. In the case of a record containing multiple CDS features, part of the sequence defined 
	 by the &#39;location&#39; property of the feature is used as the Target DNA sequence.</li>
	 <li>For SwissProt/UniProt records, the entire protein sequence provided in the record is used. PIMS will search for an &#39;EMBL CDS&#39; database 
	 accession number in the source record and, if found, will attempt to download the nucleotide sequence encoding the protein.</li>
	 <li>EMBL records usually contain both protein and nucleotide sequences. The sequences are extracted as described for the Genbank nucleotide record.</li>
	</ul>  
	<p><strong>Warning:</strong> While PiMS will make every effort to extract the DNA and matched protein sequence, 
		PiMS does not guarantee that the Protein and DNA sequences will match.<br />PiMS does not perform a sequence comparison.</p>  	 
  </div>           
  In all cases when the appropriate record has been found, the entire record will be attached to the PiMS Target as a file. The file name will indicate 
  whether the record has been used as the source of the Targe DNA or Protein. Where a record contained both sequences, it will be named as &quot;complete&quot;.
  <br/>PiMS will check the uniqueness of a Target's protein and DNA sequences. If either of them are not unique PiMS will be unable to create a Target record.  
  A link to the existing PiMS Target record, with a matching sequence, will be displayed.           
  </div> 
   
   
  <div class="textNoFloat"> 
  <h3 id="newTargetOptions">Create a new Target from a file or database record</h3>
  To create a new Target record from a file in a remote database or a local file, select &quot;Create target from a record&quot; from the Target dropdown menu or click the &quot;Create a new Target from DB record&quot; link from the PIMS homepage or the Target management homepage.<br />

  <img class="imageNoFloat" src="../images/helpScreenshots/targets/target_menu.gif" alt="Navigation to PIMS new Target" />
  <br /><br />
   You will be presented with a form with two options for creating a Target:<br />
   &nbsp; Use Option 1 to create a Target by downloading a record from a remote database<br />
   &nbsp; Use Option 2 if the database record is stored locally<br />

  <h4 id="opt1">Option 1: Download Target details from a remote database</h4>
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/target_from.gif" alt="Download Target details" />
  <br /><br />
  <ul>
   <li>Enter an accession number, or similar database record identifier, for your Target in the &#39;Database Id&#39; field<br />
    &nbsp; -in the example this is Q8ES78<br /><br /></li>
   <li>Select a database from the drop-down list<br />
    &nbsp; -in the example SWISSPROT is selected<br /><br /></li>
   <li>Click 
   <input class="button" value="get record" type="submit" /><br /><br /></li>
   
  </ul>
  </div><!-- end div class="textNoFloat" -->
  
  
  <div class="textNoFloat">
  <h4 id="opt2">Option 2: Upload Target details from a local file</h4>
  <p>Target records which have already been downloaded to your local computer can be used to create a new Target in PIMS.</p>
    <img class="imageNoFloat" src="../images/helpScreenshots/targets/PIMSTargetUpload1.jpg" alt="Upload Target details File" />
  <br />  
  <ul>
   <li>Click the browse button and naviagte to the Target file you wish to upload<br /><br /></li>
   <li>Click <input class="button" value="Upload file" type="submit" /><br /><br /></li>
   <li><strong>note:</strong> only &#39;text&#39; file format is acceptable</li>
  </ul>
  </div><!-- end div class="textNoFloat" -->
 
 
  <div class="textNoFloat">
  <h4 id="preview">Preview Downloaded Target details</h4>
  <p>Once the record has been successfully downloaded/uploaded. PIMS will ask you to choose the lab note book for a target. It will also inform you whether it was able to 
  lookup a complementary sequence or not. By clicking on the links you will be able to view records that will be sources for DNA and protein sequences of the targets.
  </p>     
  <div class="techdetails"> 
  	By this time PIMS checked whether the prospective target's protein and DNA sequences are unique. 
  </div>
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/target_from_Swiss.gif" alt="Preview Target details" />
  <br />  
You can also preview the whole record by clicking on the link to them. Record will be open in separate browser window like shown below 
    <img class="imageNoFloat" src="../images/helpScreenshots/targets/viewCompleteRecord.gif" alt="View Target source record" />

  </div><!-- end div class="textNoFloat" --> 
   
 
  <div class="textNoFloat">
  <h4 id="multipleCDS">Targets from multiple CDS files</h4>
  If the record provided has more than 1 CDS (can happen in case of EMBL and Genbank nucleotide sequences) the first step of recording the target is to identify 
  which CDS feature to treat as a target. You can choose one feature from the list of features found in the record. Features are sorted by their location.    
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/target_from_multiple_cds.gif" alt="Preview Target details File" />
  <br />  
	In this case <a href="#preview">preview target details page is not displayed</a> and you can choose the lab note book for a prospective target on this page (at the same time as choosing CDS).
	Once the CDS is chosen PIMS will check uniqueness of protein and DNA sequences.     
 </div> <!--end div class="textNoFloat"-->



 <div class="textNoFloat">
   
  <h3 id="viewTarget">View the target details</h3>

  The target details will be displayed in a PIMS &quot;View&quot; page
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/viewTarget.gif" alt="View Target details" />
  <ul>
   <li><strong>note:</strong> You may need to edit the Target manually<br />
    &nbsp; -the &#39;Name&#39; field is assigned automatically<br />
    &nbsp; -if you intend to create Construct records for the Target, you will need to 
    make sure that DNA sequence has been found or add it manually<br /> 
      &nbsp; -see <em><a href="HelpRecordDNASeq.jsp#fromDownload">Recording a Target DNA sequence</a></em><br /><br />
      &nbsp;<strong>note:</strong> to edit the Aliases, Lab Notebook and GI Number, you 
      will need to switch to the &#39;Expert&#39; perspective.</li>
  </ul>
 </div> <!--end div class="textNoFloat"-->
 
 <div class="textNoFloat">
  <h3 id="databaseReferences">View the target database references &amp; source records</h3>
	<p>
  The target database references will be displayed in a PIMS target &quot;View&quot; page
  PIMS will try to extract and preserve all database references it can find in the original source record which are related to a chosen CDS.
  For the majority of records it will make a link which takes you directly to the external database record which are related to the target.
  Different source files tend to contain different number of cross database references. SwissProt/UniProt records tend to have many cross database references, while Genbank records have only few of them.
  An example of a record recorded from SwissProt source is below.</p>   
 <img class="imageNoFloat" src="../images/helpScreenshots/targets/viewTargetDbrefsSeparate.gif" alt="View Target database references" />
  <p>Below is an example of target recorded from SwissProt source record. As SwissProt records do not contain nucleotide sequences, nucleotide sequence was taken from EMBLCDS database. Therefore in the list of 
  files attached to the target you can see 2 files (1)TargetName_DNA_record (2)TargetName_Protein_record. Where (1) record was the source of protein sequence, and (2) was the source of nucleotide sequence. 
  <div class="techdetails">
     Please note that database references are taken from the original record only. 
  </div>
  Below is an example of record created from GenBank source file. 
     
  <img class="imageNoFloat" src="../images/helpScreenshots/targets/viewTargetDbrefs.gif" alt="View Target database references" />  
 <p>The original source record database reference is stored in the list of database references. Its type is displayed in the column Type. There are only three types of records - complete, protein, DNA. 
 Where "complete" means that this record was a source of both protein and DNA sequences of the target.</p>
 <p>You can also upload any other information related to the target by using upload file field. Since PIMS version 2.0 it is possible to add a description up to 255 characters to your file. 
 If you have delete permission you will also be able to remove uploaded files from your target.</p>
 </div> <!--end div class="textNoFloat"-->
 
 
 
  <div class="textNoFloat">

  <h4 id ="expBlue">Experiment Blueprint</h4>
  For each new Target recorded in PIMS, an &quot;Experiment Blueprint&quot; with a single &quot;Blueprint component&quot; is automatically created.<br />
  An Experiment Blueprint defines what you are planning to work on.<br />
  This might be a Target, or combination of Targets, Target-domains and /or other molecules, where each of these is a Blueprint component.
  <br /><br />
  So, in the case of a new Target, a new Experiment Blueprint is created using the Target's local name as an identifier.  The Target itself is a Blueprint component within this Experiment Blueprint.
  <br />
  You can add more Blueprint components at any time. <br />
  When you record the details for an experiment in PIMS, the experiment is linked to the Target via the component(s) of an Experiment Blueprint.<br />
  &nbsp; -see <a href="../glossary.jsp#expBlueprint">glossary entry</a> for more details.<br /><br />
  </div> <!-- end div class="textNoFloat" -->
    
    
 <div class="toplink"><a href="#">Back to top</a></div>

</pimsWidget:box>
</div> <!--end div class="helppage"-->

<jsp:include page="/JSP/core/Footer.jsp" />
