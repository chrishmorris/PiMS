<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ pagebuffer='128kb' %>

<%-- 
DNAConstructWizardStep2.jsp
Author: susy
Date: 23-Feb-2008
Servlets:  /servlet/dnatarget/DNAConstructWizard.java
Updated for re-design 05-Nov-2008
 bean declarations e.g.:--%>
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New DNA Construct: Step 2 of 4' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

DNAConstructWizardStep2.jsp obsolete

<c:set var="breadcrumbs">
	<a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
	<c:forEach var="target" items="${constructBean.targetBeans}" >
	<a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
	value="${target.name}" /></a> : New Construct ${constructBean.name}
	</c:forEach>
</c:set>

<pimsWidget:pageTitle 
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="Step 2: Select Primers"
/>

	<pimsWidget:box id="box1" title="Target DNA Sequence" initialState="closed" >
	 <pimsForm:form action="" method="" mode="view" >
		<div class="formblock" >
		  <jsp:include page="/JSP/dnaTarget/SeqDisplayChunks.jsp" flush="true">
		    <jsp:param name="dnaSeq" value="${constructBean.targetDnaSeq}" />
		  </jsp:include>
		</div>
	 </pimsForm:form>	
	</pimsWidget:box>

   <c:set var="extraHeader"  scope="page" value="${constructBean.name}"/>
   <pimsWidget:box id="box2" title="Basic Details from Step 1 for: " extraHeader="${extraHeader}"  initialState="closed" >
	 <pimsForm:form action="" method="" mode="view" >
		<div class="formblock" >
			<pimsForm:text name="construct_id" alias="Construct Id" value="${constructBean.name}" helpText="Unique identifier for the Construct" />
			<pimsForm:text name="target_prot_start" alias="Target DNA start" value="${constructBean.targetDnaStart}" helpText="Start nucleotide for the Construct in the Target DNA sequence"/>
			<pimsForm:text name="target_prot_end" alias="Target DNA end" value="${constructBean.targetDnaEnd}" helpText="End nucleotide for the Construct in the Target DNA sequence"/>
			<pimsForm:text name="des_tm" alias="Primer design Tm" value="${constructBean.desiredTm}" helpText="Tm used for Primer design" />
		</div>
		<hr />			
		<div class="formblock" >		  
		  <h3 class="plainHead">Construct DNA Sequence:</h3>
		  <jsp:include page="/JSP/dnaTarget/SeqDisplayChunks.jsp" flush="true">
		    <jsp:param name="dnaSeq" value="${constructBean.dnaSeq}" />
		    <jsp:param name="conChunks" value="${constructChunks}" />
		  </jsp:include>
	    </div>
	 </pimsForm:form>
   </pimsWidget:box>

	<pimsWidget:box id="box3" title="Suggested Primer Overlaps for Tm ${constructBean.desiredTm} &deg;C:" initialState="open" >
	 <pimsForm:form action="/update/CreateExpressionObjective" id="dnaConStep1" method="post" mode="create" >
	  <pimsForm:formBlock id="blk1">
			<br />Enter a new Tm value below if the primers are not suitable:<br /><br />
		</pimsForm:formBlock>
		<pimsForm:formBlock id="blk2">
		   <pimsForm:column1>
				<pimsForm:text validation="required:true, number:true, minimum:1" value="${constructBean.desiredTm}" name="desired_tm" alias="Primer design Tm" helpText="Tm for primer design" />
		   </pimsForm:column1>
		   <pimsForm:column2>
		   			<input type="submit" value="Recalculate" onclick="dontWarn()" />
		   </pimsForm:column2>
	    </pimsForm:formBlock>
	    <pimsForm:formBlock id="blk3">
		    <input type="hidden" name="dna_target" value="<c:out value='${constructBean.dnaTarget}' />"/>
		    <input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
		    <!--  <input type="hidden" name="target_name" value="<c:out value='${constructBean.targetName}' />"/> -->
		    <input type="hidden" name="target_dna_seq" id="targetds" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
		    <input type="hidden" name ="target_dna_start" value="<c:out value='${constructBean.targetDnaStart}' />"/>
		    <input type="hidden" name ="target_dna_end" value="<c:out value='${constructBean.targetDnaEnd}' />"/>
		    <input type="hidden" name ="construct_id" value="<c:out value='${param["construct_id"]}' />"/>
		    <input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
    		<input type="hidden"  name="wizard_step" value="1dna"/>
			<hr />
	    </pimsForm:formBlock>
	    
	 </pimsForm:form>
	 <pimsForm:form action="/spot/SpotNewConstructWizard?wizard_step=2dna" id="dnaConStep2" method="post" mode="create" >
	    <pimsForm:formBlock id="blk4">
	      <jsp:include page="/JSP/spot/PrimerList.jsp" />
		</pimsForm:formBlock>
		    <input type="hidden" name="dna_target" value="<c:out value='${constructBean.dnaTarget}' />"/>
			<input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
			<input type="hidden" name="target_name" value="<c:out value='${constructBean.targetName}' />"/>
			<input type="hidden" name="target_dna_seq" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
			<input type="hidden" name ="target_dna_start" value="<c:out value='${constructBean.targetDnaStart}' />"/>
			<input type="hidden" name ="target_dna_end" value="<c:out value='${constructBean.targetDnaEnd}' />"/>
			<input type="hidden" name ="construct_id" value="<c:out value='${constructBean.name}' />"/>
			<input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
			<input type="hidden" name ="desired_tm" value="<c:out value='${constructBean.desiredTm}' />"/>
			    
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
