<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.sql.*"  %> 
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
<%@ page import="org.pimslims.lab.sequence.*"  %>
<%@ pagebuffer='128kb' %>

<jsp:useBean id="people" scope="request" type="java.util.ArrayList" />
<jsp:useBean id="constructBean" scope="request" type="ConstructBean" />

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New SDM construct: Step 3' />
</jsp:include>
<!-- NewSDMConstructWizardStep3.jsp -->

<h2><a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>
 : 
<!--Add Construct Step ${param['wizard_step']}-->New SDM Construct: <c:out value="${constructBean.name}" /> : Select Primers</h2>

<pimsWidget:box id="box2" title="Basic Details from Step 1" initialState="closed" >
 <pimsForm:form action="" mode="view" method="">
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1>
   		<pimsForm:text name="construct_id" alias="Construct Id" value="${constructBean.name}" helpText="Unique identifier for the Construct" />
   </pimsForm:column1>
   <pimsForm:column2>
        <pimsForm:text name="des_tm" alias="Primer design Tm" value="${constructBean.desiredTm}" helpText="Tm used for Primer design" />
   </pimsForm:column2> 
  </pimsForm:formBlock>
 </pimsForm:form> 
</pimsWidget:box>

<pimsWidget:box id="box1" title="Mutated Protein Sequence from Step 2" initialState="closed" >
 <pimsForm:formBlock>
<div class="sequence" style="font-size:smaller;">
<c:set var="seqLen" value="${fn:length(dnaSeq)/3}" />
<c:choose>
 <c:when test="${seqLen<26}">
  <c:forEach items="${dnaAndProtSeq2}" var="seq" varStatus="status" >
  <c:choose>
   <c:when test="${status.count==1}">
   <c:out value="${seq}" />&nbsp;&nbsp;<fmt:formatNumber type="Number" maxFractionDigits="0" ><c:out value="${seqLen*3}" /></fmt:formatNumber><br />
   </c:when>
   <c:when test="${status.count==2}">
	<c:set var="seq" value="${fn:replace(seq, 'Met','MET')}" />
	<c:set var="seq" value="${fn:replace(seq, 'Ter', 'TER')}" />
   <c:out value="${seq}" />&nbsp;&nbsp;<strong><fmt:formatNumber type="Number" maxFractionDigits="0" ><c:out value="${seqLen}" /></fmt:formatNumber></strong><br /><br />
   </c:when>
  </c:choose>
 </c:forEach>
 </c:when>
 <c:otherwise>
<%-- the number of complete rows to show --%>
<c:set var="cpteRows" value="${(seqLen - (seqLen%25) ) /25 * 2 }" />
<c:set var="resPerRow" scope="page" value="25" />
<c:forEach items="${dnaAndProtSeq2}" var="seq" varStatus="status" begin="0" end ="${cpteRows-1}" >
<c:choose>
<c:when test="${status.count%2==1}">
    <c:out value="${seq}" />&nbsp;&nbsp;<fmt:formatNumber type="Number" maxFractionDigits="0" >
        <c:out value="${resPerRow*(status.count+1)/2*3}" />
    </fmt:formatNumber><br />
</c:when>
<c:when test="${status.count%2==0}">
	<c:set var="seq" value="${fn:replace(seq, 'Met','MET')}" />
	<c:set var="seq" value="${fn:replace(seq, 'Ter', 'TER')}" />
    <c:out value="${seq}" />&nbsp;&nbsp;<strong><fmt:formatNumber type="Number" maxFractionDigits="0" >
        <c:out value="${resPerRow*(status.count)/2}" />
    </fmt:formatNumber></strong>
<br /><br />
</c:when>
</c:choose>
</c:forEach>

<%-- now show the tail end of the sequence --%>
<c:if test="${''!=dnaAndProtSeq2[cpteRows]}">
   <c:out value="${dnaAndProtSeq2[cpteRows]}" />&nbsp;&nbsp;
   <fmt:formatNumber type="Number" maxFractionDigits="0" >
       <c:out value="${seqLen*3}" />
   </fmt:formatNumber><br />
   	<c:set var="endSeq" value="${fn:replace(dnaAndProtSeq2[cpteRows+1], 'Met','MET')}" />
	<c:set var="endSeq" value="${fn:replace(endSeq, 'Ter', 'TER')}" />

   <c:out value="${endSeq}" />&nbsp;&nbsp;<strong>
   
<%--   <c:out value="${dnaAndProtSeq2[cpteRows+1]}" />&nbsp;&nbsp;<strong>--%>
   <fmt:formatNumber type="Number" maxFractionDigits="0" >
       <c:out value="${seqLen}" />
   </fmt:formatNumber></strong><br /><br />
</c:if>
 </c:otherwise>
</c:choose>  
</div>

  </pimsForm:formBlock>
</pimsWidget:box>

<pimsWidget:box id="box3" title="Suggested Primers for Tm ${constructBean.desiredTm} &deg;C:" initialState="open" >
 <pimsForm:form action="/update/CreateMutatedObjective" id="frmStep1" method="post" mode="create" >
	<pimsForm:formBlock id="b3blk1">
			<br />Enter a new Tm value below if the primers are not suitable:<br /><br />
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b3blk2">
		<pimsForm:column1>
				<pimsForm:text validation="required:true, number:true, minimum:1" value="${constructBean.desiredTm}" name="desired_tm" alias="Primer design Tm" helpText="Tm for primer design" />
		</pimsForm:column1>
		<pimsForm:column2>
		   			<input type="submit" value="Recalculate" onclick="dontWarn()" />
		</pimsForm:column2>
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b3blk3">
	<input type="hidden"  name="pims_researchobjective_hook" value="<c:out value='${constructBean.hook}' />"/>
	<input type="hidden"  id="construct_prot_seq" name="construct_prot_seq" value="<c:out value='${constructBean.protSeq}' />"/>
    <input type="hidden"  name="construct_dna_seq" value="<c:out value='${constructBean.dnaSeq}' />"/>
    <input type="hidden"  id="reverse_seq" value="<c:out value='${constructBean.reverseSeq}' />"/>
    
    
    <!-- TODO remove, value is fixed: -->
    <input type="hidden"  name="design_type" value="SDM"/>
    
    <input type="hidden"  name="construct_id" value="<c:out value='${constructBean.name}' />" />
    <input type="hidden"  name="wizard_step" value="2"/>
	<hr />
	</pimsForm:formBlock>	 
  </pimsForm:form>

  <pimsForm:form action="/update/CreateMutatedObjective" id="frmStep1" method="post" mode="create" >
	<pimsForm:formBlock id="b3blk4">
	      <jsp:include page="/JSP/sdm/SDMPrimerList.jsp" />
	</pimsForm:formBlock>
	<input type="hidden"  name="pims_researchobjective_hook" value="<c:out value='${constructBean.hook}' />"/>
	<input type="hidden"  id="construct_prot_seq" name="construct_prot_seq" value="<c:out value='${constructBean.protSeq}' />"/>
    <input type="hidden"  name="construct_dna_seq" value="<c:out value='${constructBean.dnaSeq}' />"/>
    <input type="hidden"  id="reverse_seq" value="<c:out value='${constructBean.reverseSeq}' />"/>
    <input type="hidden"  name="design_type" value="SDM"/>
    <input type="hidden"  name="construct_id" value="<c:out value='${constructBean.name}' />" />
    <input type="hidden"  name="wizard_step" value="Save"/>    
 </pimsForm:form>	
</pimsWidget:box>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%--  
The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> 
--%>

<!-- /NewSDMConstructWizardStep3.jsp -->
<jsp:include page="/JSP/core/Footer.jsp" />


