<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help for Constructs' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<%-- HelpConstruct.jsp --%>

<c:catch var="error">

<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target Help</a>
</c:set>
<c:set var="icon" value="construct.png" />
<c:set var="title" value="Construct Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      <p>PiMS allows you to record information about Constructs.<br /> 
      The details you record for a Construct are linked to a Target record and are stored as a PiMS Experiment:
      a &quot;Construct Design&quot; Experiment.<br />
      Information recorded for Constructs includes details of primers and tags. You can also 
      record the details of experiments performed on a construct, and include gel images 
      and data files.<br />
      The values recorded for the Construct Design Experiment are displayed in a Construct details page along with
      some calculated values for relevant DNA and protein sequences.<br /><br />
     This functionality was originally developed in the Scottish Structural Proteomics Facility 
     <a href="http://www.compbio.dundee.ac.uk/">SSPF</a> at the University of Dundee as 
     &#39;SPoT&#39;, which is an acronym for Structural Proteomics Tracker.<br />
    Support for Primer-less Constructs was added in version 3.3
    </p>
    </div>
  </pimsWidget:box>

  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
       <li><a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">New Construct</a></li>
       <li><a href="#constructDetails">Construct Details</a></li>
       <li>
        <ul>
          <li><a href="#progress">Construct Progress</a></li>
          <li><a href="#primerDetails">Primers</a></li>
          <li><a href="#pcrProd">Insert</a> (the PCR product)</li>
          <li><a href="#proteins">Proteins</a></li>
          <li><a href="#experiments">Experiments</a></li>
        </ul>
       </li>            
       <li><a href="#plConstructDetails">Construct Details</a> Primer-less Construct</li>
      </ul>
    </div>
    <div class="rightcolumn">
      <ul>
       <li><a href="#constructResults">Construct Experiments</a></li>
       <li>
        <ul>
         <li><a href="#conDesignExp">Construct Design</a> Experiment</li>
         <li><a href="#conDesignExptDiagram">Diagram</a> of the Experiment</li>
         <li>Recording a <a href="#newExp">New Experiment</a></li>
         <li>List of <a href="#conExpList">Experiments</a> for a Construct</li>
        </ul>
       </li> 
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <div class="leftcolumn">  
    <ul>
         <li>Recording <a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Experiments</a> in PiMS</li>
         <li><a href="${pageContext.request['contextPath']}/help/HelpPrimerOrder.jsp">Ordering Primers</a></li>
    </ul>
    </div>
    <div class="rightcolumn">
    <ul>
          <li>PiMS support for <a href="${pageContext.request['contextPath']}/help/HelpMutagenesis.jsp">Mutagenesis</a></li>
    </ul>
    </div>    
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="constructDetails">Construct Details</h3>
  <!-- Start and stop added, Novagen 3C-LIC F and R-->
  The Construct details page is displayed when you click <input class="button" value="Save" type="submit" /> at the final step of designing a new Construct in PiMS.<br />
  This page is also displayed when you click a Construct link from the Target details page.<br />
  The example below shows the Construct details page for the Construct recorded by following the <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">New Construct</a> help.
  <br /><br />
  The &quot;Actions&quot; (links below the title) allow you to view a Construct 
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif" /> <a href="#diagramView"><strong>Diagram</strong></a>, 
   <a href="javascript:void(0)"><strong>Delete</strong></a> the Construct record,
  view details of Experiment <a href="#conExpList">Milestones</a> recorded for the Construct and 
  design <a href="${pageContext.request.contextPath}/help/HelpMutagenesis.jsp"><strong>SDM</strong></a>  Primers (for Site Directed Mutagenesis).<br /><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/construct.png" alt="New construct Summary" />
  <br />
  The Construct details page has 7 specific boxes displaying:<br /><br />
  <strong>1.</strong> Basic details for the Construct.<br />
  <strong>2.</strong> Progress of the Construct <em> -links to a series of Experiments recorded for this Construct </em><br />
  <strong>3.</strong> Forward Primer.<br />
  <strong>4.</strong> Reverse Primer<br />
  <strong>5.</strong> Insert<br />
  <strong>6.</strong> Proteins <em> -sequences of the predicted expressed and final proteins</em><br />
  <strong>7.</strong> Experiments <em> -a list of Experiments using the Construct</em><br /><br />
  There are also the usual 3 boxes for <strong>Images</strong>, <strong>Attachments</strong> and <strong>Notes</strong>.<br />
  <ul>
   <li>click 
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>
    in the Basic Details box to enter an optional <strong>Description</strong> and <strong>Comments</strong><br />
    and to Select a <strong>Scientist</strong> responsible for the Construct, from the 
    drop-down list. <!-- <em>-the default is the person who is currently logged into PiMS.</em>--><br /><br /></li>

    <li id="progress">Open the box labelled <strong>Progress</strong> for links to the series of Experiments which have been recorded for the Construct.<br />
    These are colour according to the &quot;Status&quot; of the Experiment: Green for OK, Red for Failed<br />
    &nbsp; <em>-initially this will just contain a link to the <a href="#conDesignExp">Construct Design Experiment</a></em>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructProgress.png" alt="New construct Progress box" />

    <li id="primerDetails">Open the boxes labelled <strong>Forward Primer: ..</strong> and <strong>Reverse Primer: ..</strong> to see the details recorded for the Primers<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructPrimers.png" alt="New construct Primer boxes" />
    For each Primer the sequence of the Full length Primer, the Overlap (gene-specific region) and the 5'-Extension (if recorded) is shown,
    along with the Length, Tm&deg;C and GC&#37; for the Full length and Overlap.  The calculated Molecular Mass of the primer is also displayed.<br /><br /></li>
    
    <li id="pcrProd">The box labelled <strong>Insert</strong> displays the DNA sequence as recorded during the Construct design steps.<br />
    It includes any added Start or Stop codons and Extensions.<br />
    The length and GC&#37; are displayed.  You can edit this sequence by clicking
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>.
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructPCR.png" alt="New construct Primer boxes" />
    The example includes the Forward and Reverse Primer sequences and a start and stop codon which were added during the Construct design.<br /><br /></li>
          
    <li id="proteins">Open the box labelled <strong>Proteins</strong> to display the protein sequences recorded for the Construct.
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructProteins.png" alt="New construct Proteins box" />
    These are:<br />
    &nbsp; <strong>Target protein</strong> -the region selected for Construct design<br />
    &nbsp; <em>-residues 2-599 in the example</em><br />
    &nbsp; <strong>Expressed protein</strong> -the expression product, which includes any added N- and C-terminal protein tags<br />
    &nbsp; <em>-in the example there are 8 additional N-terminal residues LEVFQGPG from the tag, plus M from the added Start codon</em><br />    
    &nbsp; <strong>Final protein</strong> -the expression product after any cleavable protein tags have been removed<br />
    &nbsp; <em>-in the example there are 4 additional N-terminal residues GPGM</em><br /><br />
    
    Calculated values for the <strong>Length</strong>, <strong>Weight</strong> in Daltons, <strong>Extinction</strong> coefficient and <strong>pI</strong>
    are also displayed.<br />
      
    The sequences can be edited by clicking
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>.
    <br /><br /></li>      

   <li id="experiments">The box labelled <strong>Experiments</strong> contains details of all Experiments which have been recorded for the Construct.<br />
    -initially this will just contain a link to the <a href="#conDesignExp">Construct Design Experiment</a><br />
    &nbsp; <em> -see <a href="#constructResults">Construct Experiments</a> for more details.</em><br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructExpts.png" alt="New construct Experiment boxe" /></li>
  </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
 <h3 id="plConstructDetails">Construct Details: Primer-less Constructs</h3>
  The Details page for a Primer-less Construct is displayed when you click <input class="button" value="Save Construct" type="submit" /> at the first step of designing a new Construct in PiMS.<br />
  This page is also displayed when you click a suitable Construct link from the Target details page.<br />
  The example below shows the Construct details page for the Construct recorded by following the <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp#primerless">New Construct</a> help.

    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/plcConstructDetails.png" alt="View of Primerless construct" />
  The details are similar to those for a Construct recorded using the PiMS Primer design tool with a series of boxes displaying:<br /><br />
  <strong>1.</strong> Basic details for the Construct.<br />
  <strong>2.</strong> Progress of the Construct <em> -links to a series of Experiments recorded for this Construct </em><br />
  <strong>3.</strong> Insert<br />
  <strong>4.</strong> Proteins <em> -sequences of the predicted expressed and final proteins</em><br /> 
  <strong>5.</strong> Experiments <em> -a list of Experiments using the Construct</em><br /> 
  <ul>
   <li>click 
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>
    in the Basic Details box to enter an optional <strong>Description</strong> and <strong>Comments</strong><br />
    and to Select a <strong>Scientist</strong> responsible for the Construct, from the 
    drop-down list. <!-- <em>-the default is the person who is currently logged into PiMS.</em>--><br />
    You can also select a Vector Sample for your Construct.<br /><br /></li>

    <li>Open the box labelled <strong>Progress</strong> for links to the series of Experiments which have been recorded for the Construct.<br />
    These are colour according to the &quot;Status&quot; of the Experiment: Green for OK, Red for Failed<br />
    &nbsp; <em>-initially this will just contain a link to the <a href="#conDesignExp">Construct Design Experiment</a></em>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructProgress.png" alt="New construct Progress box" />

    <li>The box labelled <strong>Insert</strong> displays the DNA sequence of the Construct.<br />
    The length and GC&#37; are displayed.  You can edit this sequence by clicking
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>.<br /><br /></li>
          
    <li>Open the box labelled <strong>Proteins</strong> to display the protein sequences recorded for the Construct.
    These are:<br />
    &nbsp; <strong>Target protein</strong> -the region selected for Construct design <em>-residues 1-599 in the example</em><br />
    &nbsp; <strong>Expressed protein</strong> -the expression product <em>-which is the same in the example</em>.<br />
    &nbsp; PiMS does not record a sequence for the <strong>Final protein</strong> but this can be added manually.<br /><br />
    
    Calculated values for the <strong>Length</strong>, <strong>Weight</strong> in Daltons, <strong>Extinction</strong> coefficient and <strong>pI</strong>
    are also displayed.<br /><br />
      
    The sequences can be edited by clicking
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" alt="make changes icon" /> <strong>Make changes...</strong>.
    <br />
    Doing this will display an empty field into which you can paste a sequence representing 
    a Final or processed protein e.g. after proteolytic cleavage of a tag.<br />
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/plcConstructProteins.png" alt="New Primerless construct Proteins box" />
    <br /></li>      

   <li>The box labelled <strong>Experiments</strong> contains details of all Experiments which have been recorded for the Construct.<br />
    -initially this will just contain a link to the <a href="#conDesignExp">Construct Design Experiment</a><br />
        &nbsp; <em> -see <a href="#constructResults">Construct Experiments</a> for more details.</em><br /><br /></li>   
    
   <li>The new construct will appear in the &quot;Construct List&quot; in the &quot;Target Details&quot; page.</li>
  </ul>

  There are also the usual 3 boxes for <strong>Images</strong>, <strong>Attachments</strong> and <strong>Notes</strong>.

  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
 <h3 id="constructResults">Construct Experiments</h3>
   Recording Experiments in PiMS typically starts with a Construct.<br />
   The details you record for a Construct are linked to a Target record and are stored as a PiMS Experiment record: a 'Construct design' experiment. 
   <!-- ................................................................................ -->
  <h4 id="conDesignExp">Construct Design Experiment</h4>
    The Construct is recorded as a special type of experiment with no Input samples.<br />
    The Construct design experiment records the information you enter when you create a new Construct record.
    <br /><br />
    The values recorded for the Construct Design Experiment are used to create the Construct details page 
    along with the calculated Tm and % GC for the primers, and the length, pI and Extinction coefficient for the expressed and final protein sequences.
    <ul>
     <li>To see the details of a Construct Design Experiment, click the link in the <strong>Progress</strong> box 
     in the Construct Details page.<br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/newConstructProgress.png" alt="New construct Progress box" />
      <br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constDesignExptTop.png" alt="Construct design experiment top part" />
     </li>
     <li>The Basic details box includes a link to the <img src="${pageContext.request.contextPath}/images/icons/types/construct.gif" /> <a href="JavaScript:void(0)">
     <strong>Construct</strong></a> and the Construct Design Protocol.<br /><br /></li>
     <li>Any protein tags which were added to the Expressed protein are displayed as <strong>Conditions</strong>.<br />
     &nbsp; <em> -these are not recorded for Primer-less Constructs</em><br /><br /></li> 
     <li>PiMS also creates 3 Sample records for the Construct: one each for the Forward primer, the Reverse primer and the Template.<br />
     &nbsp; <em> -only a Template Sample is created for Primer-less Constructs</em><br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constDesignExptSamples.png" alt="Construct design experiment bottom part" />
     </li>         
    </ul>

  <!-- ................................................................................ -->
  <h4 id="conDesignExptDiagram">Diagrams</h4>
  Clicking the <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif" /> <a href="JavaScript:void(0)"><strong>Diagram</strong></a>
  link on the Construct Design Experiment page will display the Construct Experiment diagram:<br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constDesignExptDiagram.png" alt="Construct design diagram" /><br /><br />
   
  The White ellipse with a red border represents the <strong>Construct Design</strong> Experiment.<br />
  The ellipse is a clickable link back to the Experiment view.
  <em>-see <a href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp#viewExp">View</a> Experiment details.</em><br /><br />
  
  The 3 diamonds are links to the three &#39;Output Samples&#39; from the Experiment:<br />
  <strong>Template:</strong> 000994.con1T, <strong>Forward primer:</strong> 000994.con1F and 
  <strong>Reverse primer:</strong> 000994.con1R.<br /><br />
    
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="newExp">Recording a New Experiment -for a Construct</h4>
  There are several ways to record a New Experiment for a Construct.<br />
  <ol>
   <li>Click the <a href="JavaScript:void(0)">New Experiment</a> link in the header of the box labelled 
   <strong>Experiments</strong><br />
   Select the Experiment type and Protocol, enter an Experiment name -or accept the default name generated by PiMS<br />
   Select a  Lab Notebook and click <input type="submit" value="Next&gt;&gt;&gt;" /><br /><br /></li>
   <li>Navigate to the Construct's Target details page and open the box labelled <strong>Constructs</strong>.<br />
   Click the  <a href="#newExp"><strong>New Experiment</strong></a> 
   link for the relevant Construct then proceed as above to select the Experiment Type, Protocol etc.<br /><br /></li>
   <li>Navigate to the view of an Output Sample from an existing Experiment for the Construct.<br />
   Locate and open the box labelled <strong>Use Sample as input in a New Experiment:</strong><br />
   Click <input type="submit" value="New Experiment" /> for the desired Experiment Type</li>
  </ol>
  
   &nbsp; <em>See: Recording <a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#recordSimpleExp">Experiments</a> in PiMS for more information</em>

  <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="workflow">Creating a Workflow</h5>
    
   A typical first Experiment for a Construct with Primers will be a PCR experiment.<br /><br />
   The Primer Samples created for the New Construct can be used as Input Samples for the PCR Experiment.<br />
   The PCR Experiment will create a PCR Product Output Sample and this in turn can be the Input Sample for the next Experiment.<br /><br />
   In this way, a <strong>Workflow</strong> is created and each time you record a new Experiment for the Construct, an extra ellipse 
   will be added to the diagram.<br />
      <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constWorkflow.png" alt="Construct workflow diagram" /><br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="conExpList">List of Experiments for a Construct</h4>
 
 In addition to the list of Experiments in the <strong>Experiments</strong> brick for a Construct, you can also see a list of Experiment Milestones
 by clicking the <a href="JavaScript:void(0)">Milestones</a> link near the top of the Construct details page.<br />
 <br /><br />
 This list is also available from the Target view. Navigate to the Construct's Target details page and open the box labelled <strong>Constructs</strong>.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/constructList.png" alt="List of Constructs for a Target" /><br /><br />
 Then click the <a href="JavaScript:void(0)">All Experiments</a> link for a Construct.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/spotConstResults.png" alt="Construct Experiments" /><br /><br />
 The list includes details of whether a <strong>Milestone</strong> was achieved, the name of the 
 <strong>Person</strong> responsible for the Experiment, the <strong>Date</strong> of 
 the Experiment, a link to the <strong>Experiment</strong> record and a list of any 
 <strong>Files</strong> which are linked to the Experiment record.<br /><br />
 The Experiment representing the &#39;most progressed&#39; stage in the protein production 
 pipeline is highlighted in <strong>bold</strong>.<br />

 <ul>
  <li>To view the details of an Experiment, click on the appropriate link in the 
  <strong>Experiment</strong> column.<br /><br /></li>
  <li>To record a new Experiment using the Construct click the 
    <a href="#newExp"><strong>New Experiment</strong></a> 
   link.</li>
 </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
  
</div><%--End of div class="helppage"--%>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

