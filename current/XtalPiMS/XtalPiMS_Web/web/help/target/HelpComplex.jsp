<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Complex Help' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<%-- HelpConstruct.jsp --%>

<c:catch var="error">

<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a> :
  <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Target and Construct Help</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="PiMS Help for Complexes"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      <p style="padding-left:.6em; ">PiMS can group a number of Targets together as a Complex.</p>
    </div>
   </pimsWidget:box>


  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul>
      <li><a href="#newcomplex">Create a new Complex</a></li>
      <li><a href="#viewcomplex">The View of a Complex</a></li>
     </ul>
    </div>
    <div class="rightcolumn">
     <ul>
      <li><a href="#coexpression">Co-expressing a dimer</a></li>
      <li><a href="#complexassembly">Assembly of a construct from separately expressed protein</a></li>
      <!--  <li><a href="#spine2complex">Report the progress on a complex to SPINE2 Target Tracker</a></li> -->
     </ul>
    </div>
  </pimsWidget:box>
</div> <!--end div help-->

<div class="helpcontents">
<h3 id="navigation">Complex management</h3>
  To record a Complex in PiMS:<br />
  &nbsp; select <a href="javascript:void(0)">Create a New Complex</a> from the Target and Complex menu<br />
  <img class="imageNoFloat"
        src="${pageContext.request['contextPath']}/images/helpScreenshots/complexmenu.png"
        alt="complex menu" />

 <h3 id="newComplex">Create a new Complex</h3>
  <div class="textNoFloat">
  <img class="imageNoFloat"
		src="${pageContext.request['contextPath']}/images/helpScreenshots/complexnew.png"
		alt="create a new complex" /><br /><br />
  <ul>
   <li>All fields are mandatory -as indicated by the red <span class="required">*</span> after the field names<br /><br /></li>
   <li>Give your complex a name, and indicate why it was chosen.<br /><br /></li>
   <li>Then add any other information you wish.<br /><br /></li>
   
   <li>When you have completed all of the mandatory fields click the
   <input class="button" value="Create" type="submit" /> button<br />
   The Complex will be recorded and displayed in a PiMS Complex details view.<br /><br /></li>
   
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
   </div> <!-- end div class="textNoFloat" -->
 
 <h3 id="viewcomplex">Complex Details</h3>
   <div class="textNoFloat">	

	You can see the details of your complex in the view complex page. 
	 
	<img class="imageNoFloat" style=" height: 74%; width:74%;"
		src="${pageContext.request['contextPath']}/images/helpScreenshots/complexview.png"
		alt="sample list assign to" />
	<br />	
	Here you can also add new components, or remove components from your complex.
	
	
	
	</div> <!-- end div textNoFloat -->
   
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="coexpression">Co-expressing a dimer</h3>
   <div class="helpcontents">
    <div class="textNoFloat">
     <p>
     PiMS provides standard protocols for co-expressing a dimer. 
     </p>
     <p> 
     If both PCR product nucleotides are cloned into the same plasmid a PiMS Bicistronic protocol can be used.
     </p>
     <p> 
     When a different plasmid for each protein is in the transformed cells, the PiMS Bitrans Transformation protocol should be used.  
     </p>
     <p> 
     Of course you will probably want to tailor these protocols to match your particular experiments.
     </p>
    </div> <!-- end div textNoFloat -->
  <a href="#top">back to top</a>   
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="complexassembly">Assembly of a construct from separately expressed protein</h3>
   <div class="helpcontents">
    <div class="textNoFloat">
     <p>
     Similarly a complex assembled from separately expressed proteins can be represented in PiMS.  
     </p>
     <p> 
     A protocol such as the standard PiMS Co Concentration or PiMS Complexation should be used.  This allows two separately purified proteins to be added for the concentration stage.
     </p>
     <p> 
     You should make you own protocol to allow more than one protein sample to be input at the scale up step appropriate for your experiments.
     </p>
    </div> <!-- end div textNoFloat -->
   </div> <!--end div class="helppage"--> 
  <div class="toplink"><a href="#">Back to top</a></div>

   
</div>
<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
