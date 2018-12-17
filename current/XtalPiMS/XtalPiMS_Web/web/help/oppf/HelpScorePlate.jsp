<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to Scoring results in Plate experiments ' />
</jsp:include>

<div class="help">
<pimsWidget:box initialState="fixed" title="Scoring Plate Experiments">
Functionality has been added to plate experiments to help with scoring results.<br />  
</pimsWidget:box><br /><br />
 
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#parameters">Score Plate Parameters</a></li>
  <li><a href="#action">Score Plate in Action</a></li>
 </ul>
 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS<br />
 
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
 
  <li><h3 id="parameters">ScorePlate parameters</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   
   Some special experiment parameters starting with two underscore characters are the key to the scoreplate functionality.
   <br />
   Protocol editor is used to set up the parameters
   <br />
   <ul>
     <li>The parameter name must start with '__'.</li>
     <li>The parameter description is the parameters label.</li>
     <li>The possible values Yes+++, Yes++, Yes+ Yes, No, Maybe, and Unscored are supported.</li>
     <li>The standatd OPPF protocols contain scoreplate parameters:
       	<ul>
     		<li>__SCORE parameter in PCR experiments shows PCR product size</li>
     		<li>__SEQUENCE parameter in Sequencing experiments allows popup sequence results</li>
     		<li>__MW and __IMW for Soluble and Insoluble expression level in Small Scale Expression experiments</li>
   		</ul>
     </li>
   </ul>
   <br />
   <img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_parameters.jpg" alt="Score Plate Parameters" /><br />
   <br />
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
  <li><h3 id="action">Score Plate in Action</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
    <ul>
    	<li><h4>The __SEQUENCE parameter</h4>
      		The screenshot below shows the use of the __SEQUENCE parameter in a sequencing plate.  Wells are colour coded according to their score. 
      		<br />
      		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_plateview.jpg" alt="Score Plate in Action" /><br />
     		<br />
     		Clicking on a well will load the alignment details in a popup window.  PiMS will look for a zip file attached to
     		this plate.  If one is found PiMS will examine any .seq files within for one matching the well clicked.  A match
     		is attempted by construct name and by well position.  An alignment is attempted between the sequence contained 
     		in the file, and the PCR Product sequence of the experiments construct.
     		<br />
      		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_sequencealign.jpg" alt="Score Plate in Action" /><br />
     	</li>
     	
     	<li><h4>The __SCORE parameter</h4>
     		The __SCORE parameter has been designed to score PCR Product.<br />
     		Notice the expected pcr product size is shown in the well tooltip.
     		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_expectedpcr.jpg" alt="Score Plate in Action" /><br />
     	</li>
     	
     	<li><h4>The __MW and __IMW parameters</h4>
     		The __MW parameter has been designed to score soluble expression level, and the __LMW parameter the insoluble 
     		expression level.<br />
     		Notice the protein molecular weight is shown in the well tooltip.
     		<img class="imageNoFloat" src="${pageContext.request.contextPath}/help-screenshots/oppf/scoreplate_expressionlevel.jpg" alt="Score Plate in Action" /><br />
     	</li>
     	
    </ul>
  </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
 

<jsp:include page="/JSP/core/Footer.jsp" />
