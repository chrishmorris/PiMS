<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>

<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ pagebuffer='128kb' %>

<%-- 
DNAConstructWizardStep4.jsp
Author: susy
Date: 04-Mar-2008
Servlets:  /servlet/dnatarget/DNAConstructWizard.java
Updated for re-design 10-Nov-2008
--%>

<%-- bean declarations e.g.: --%>
<jsp:useBean id="people" scope="request" type="java.util.ArrayList" />
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New DNA Construct: Step 4 of 4' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>
<!-- DNAConstructWizardStep4.jsp -->
<%-- page body here --%>
<h2><a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>
 : New Construct Summary: <c:out value="${constructBean.name}"/></h2>

	<pimsWidget:box id="box1" title="Target DNA Sequence" initialState="closed" >
	 <pimsForm:form action="" method="" mode="view" >
		<pimsForm:formBlock id="b1">
		  <jsp:include page="/JSP/dnaTarget/SeqDisplayChunks.jsp" flush="true">
		    <jsp:param name="dnaSeq" value="${constructBean.targetDnaSeq}" />
		  </jsp:include>
		</pimsForm:formBlock>
	 </pimsForm:form>	
	</pimsWidget:box>

   <c:set var="extraHeader"  scope="page" value="${constructBean.name}"/>
   <pimsWidget:box id="box2" title="Basic Details from Step 1 for: " extraHeader="${extraHeader}"  initialState="closed" >
	 <pimsForm:form action="" method="" mode="view" >
		<pimsForm:formBlock id="b2">

			<pimsForm:text name="construct_id" alias="Construct Id" value="${constructBean.name}" helpText="Unique identifier for the Construct" />
			<pimsForm:text name="target_prot_start" alias="Target DNA start" value="${constructBean.targetDnaStart}" helpText="Start nucleotide for the Construct in the Target DNA sequence"/>
			<pimsForm:text name="target_prot_end" alias="Target DNA end" value="${constructBean.targetDnaEnd}" helpText="End nucleotide for the Construct in the Target DNA sequence"/>
			<pimsForm:text name="des_tm" alias="Primer design Tm" value="${constructBean.desiredTm}" helpText="Tm used for Primer design" />
		</pimsForm:formBlock>
		<hr />			
		<pimsForm:formBlock id="b3">
		  <h3 class="plainHead">Construct DNA Sequence:</h3>
		  <jsp:include page="/JSP/dnaTarget/SeqDisplayChunks.jsp" flush="true">
		    <jsp:param name="dnaSeq" value="${constructBean.dnaSeq}" />
		    <jsp:param name="conChunks" value="${constructChunks}" />
		  </jsp:include>
	    </pimsForm:formBlock>
	 </pimsForm:form>
   </pimsWidget:box>
   

<c:forEach var="primer" items="${constructBean.primers}" >
    <pimsWidget:primer bean="${primer}" />
</c:forEach>
   
<pimsWidget:box id="box4" title="Description and Comments" initialState="open" >
 <pimsForm:form action="/spot/SpotNewConstructWizard?wizard_step=4dna" id="frmStep3" method="post" mode="create" >
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1>
   		<pimsForm:text  value=""  name="description" alias="Description" helpText="Brief description of the Construct e.g. Vector, tags etc." />
		<pimsForm:select name="personHook" alias="Person" validation="required:true" helpText="Scientist responsible for the Construct -defaults to person currently logged in" >
		    <c:if test="${null!=currentPerson}"> 
				<pimsForm:option optionValue="${currentPerson.hook}" currentValue="${currentPerson.hook}" alias="${currentPerson.name}" />	    
            </c:if>
         	<%--<c:forEach var="p" items="${people}"> 
          	<c:if test="${p.hook != currentPerson.hook}"> 
          		<pimsForm:option optionValue="${p.hook}" currentValue="${currentPerson.hook}" alias="${p.name}" /> 
          </c:if>
         </c:forEach> --%>
		</pimsForm:select>
   </pimsForm:column1>
   <pimsForm:column2>
      <pimsForm:textarea name="comments" alias="Comments" helpText="Any other comments" ></pimsForm:textarea>
   </pimsForm:column2>
  </pimsForm:formBlock>
      <input type="hidden" name="dna_target" value="<c:out value='${constructBean.dnaTarget}' />"/>
	<input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
	<!-- <input type="hidden" name="target_name" value="<c:out value='${constructBean.targetName}' />"/> -->
	<input type="hidden" name="target_dna_seq" id="targetds" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
	<input type="hidden" name="target_dna_start" value="<c:out value='${constructBean.targetDnaStart}' />"/>
	<input type="hidden" name="target_dna_end" value="<c:out value='${constructBean.targetDnaEnd}' />"/>
	<input type="hidden" name="construct_id" value="<c:out value='${constructBean.name}' />"/>
	<input type="hidden" name ="desired_tm" value="<c:out value='${constructBean.desiredTm}' />"/>
	<input type="hidden" name="forward_extension" value="<c:out value='${constructBean.forwardTag}' />"/>
	<input type="hidden" name="reverse_extension" value="<c:out value='${constructBean.reverseTag}' />"/>
	<input type="hidden" name="forward_primer" value="<c:out value='${constructBean.fwdPrimer}' />"/>
	<input type="hidden" name="reverse_primer" value="<c:out value='${constructBean.revPrimer}' />"/>
	<input type="hidden" name="fwd_overlap" value="<c:out value='${constructBean.fwdOverlap}' />"/>
	<input type="hidden" name="rev_overlap" value="<c:out value='${constructBean.revOverlap}' />"/>
	<input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
 <pimsForm:formBlock id="blk2">
   	<div style="text-align:right">
      <input type="button" style="float:left" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
      <input name='Submit' type='submit' value='Save Construct' onclick="dontWarn()"/>
    </div>
  </pimsForm:formBlock>
 <script type="text/javascript">
 </script>    
 </pimsForm:form>
</pimsWidget:box>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>
<%--  
The following table is for debugging purposes.  Please comment it out for normal use
<jsp:include page="/ConstructBeanDebug" />    
--%> 
    
<jsp:include page="/JSP/core/Footer.jsp" />
