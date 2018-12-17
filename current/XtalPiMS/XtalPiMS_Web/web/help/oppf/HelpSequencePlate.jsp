<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Recording Sequencing Plate results ' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Recording Sequencing Plate Results">
This guide will show how to record sequencing results for a plate in PiMS.
<p>
Plate experiments containing nucleic acids may be sent for sequencing.  The results are returned in an electronic form, 
stored in a compressed file.  This file may be recorded in PiMS as an attachment to the plate experiment.  This process
will automatically score the results against matching wells if possible.
</p>
</pimsWidget:box><br />
 
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#parameters">Sequencing Plate Experiments</a></li>
  <li><a href="#recording">Recording Sequencing Results</a></li>
  <li><a href="#results">Examining Sequence Plate Results</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />
 
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
 
  <li><h3 id="parameters">Sequencing Plate Experiments</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   
   <ul>
     <li>Sequencing experiments wishing to take advantage of this functionality need to be follow protocols with a 
scoreplate Sequencing result parameter. This special experiment parameter starting with two underscore characters 
must have the name '__SEQUENCE'.</li> 
     <li>The description contains the label to be shown when viewing the experiment.</li>
     <li>The supported possible values for this parameter are Yes, No, Maybe, and Unscored.</li> 
   </ul>
   <br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/sequenceparameter.jpg" alt="Sequencing parameter" /><br />
   <br />
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
  <li><h3 id="recording">Recording Sequencing Results</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
    <ul>
    	<li>Select Plate Sequence Results from the OPPF menu. 
      		<br />
      		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/sequenceresultsmenu.jpg" alt="Selecting Sequence Results in the OPPF Menu" /><br />
     	</li>
     	
     	<li>This page shows all of the sequencing plate experiments available to accept a sequencing results file, 
     	    and a filechooser icon to browse for the sequencing results zip file.<br />
     	</li>
     	
     	<li>Browse for the sequencing results file
     		<br />
     		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/sequenceresultsbrowse.jpg" alt="Browsing for the sequence results file" /><br />
     	</li>
     	
     	<li>Select the corresponding sequencing plate, and click the Next button.
     		<br />
     		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/sequenceresultsnext.jpg" alt="Attaching the sequence results to a plate" /><br />
     	</li>
     	
     	<li>The sequencing results file will be processed.  Every well with a corresponding result will have the
     	sequence of its expected PCR product aligned with the sequence of the result.  It will be scored, and the
     	result stored against the Sequence Result parameter.<br />
     	You may check the alignment, and change the result of any well/wells, see the next section (Examining Sequence Plate Results).
     		
     	</li>
     	
    </ul>
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="results">Examining Sequence Plate Results</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
    <ul>
    	<li><h4>The Sequence Result parameter</h4>
      		The screenshot below shows the Sequence Result parameter in a sequencing plate.  Wells are colour coded according to their score. 
      		<br />
      		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_plateview.jpg" alt="Score Plate in Action" /><br />
     		<br />
     		Clicking on a well will load the alignment details in a popup window.  PiMS will look for a zip file attached to
     		this plate.  If one is found PiMS will examine any .seq files within for one matching the well clicked.  A match
     		is attempted by construct name and by well position.  An alignment is attempted between the sequence contained 
     		in the file, and the PCR Product sequence of the experiments construct.
     		<br />
      		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_sequencealign.jpg" alt="Score Plate in Action" /><br />
     		<br />
     		Any score may be adjusted by selecting the well(s) and selecting the score; similar to changing any other parameter.
     		<br />
     	</li>
     	
    </ul>
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  
 

<jsp:include page="/JSP/core/Footer.jsp" />
