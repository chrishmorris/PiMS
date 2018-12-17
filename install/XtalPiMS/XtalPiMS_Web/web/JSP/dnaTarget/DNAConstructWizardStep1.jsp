<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ pagebuffer='128kb' %>

<%-- 
DNAConstructWizardStep1.jsp
Author: susy
Date: 17-Feb-2008
Servlets: /servlet/dnatarget/DNAConstructWizard.java
Updated for re-design 05-Nov-2008
-->
<%-- bean declarations --%>
<jsp:useBean id="milestones" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructResultBean>" /> 
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New DNA Construct: Step 1 of 4' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>
<!-- DNAConstructWizardStep1.jsp -->
<%-- page body here --%>
<c:set var="targLen" value="${fn:length(constructBean.targetDnaSeq)}" />
<c:choose>
 <c:when test="${targLen <50}" >
  <h2>Sorry, construct design is not available for DNA Targets less than 50 nucleotides long</h2>
  <h2><a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>
 </h2>
 </c:when>
 <c:otherwise>
  <c:set var="breadcrumbs">
    <a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
	<c:forEach var="target" items="${constructBean.targetBeans}" >
	    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
	    value="${target.name}" /></a> 
	</c:forEach>
  </c:set>

<pimsWidget:pageTitle 
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="New Construct: Basic details"
/>

<pimsWidget:box id="box1" title="Target DNA Sequence" initialState="open" >
 <pimsForm:form action="" method="" mode="view" >
	<div class="formblock" >
	  <jsp:include page="/JSP/dnaTarget/SeqDisplayChunks.jsp" flush="true">
	    <jsp:param name="dnaSeq" value="${constructBean.targetDnaSeq}" />
	  </jsp:include>
	</div>
 </pimsForm:form>	
</pimsWidget:box>

<pimsWidget:box id="box2" title="Basic Details" initialState="open" >
 <pimsForm:form action="/update/CreateExpressionObjective" id="dnaConStep1" method="post" mode="create" >
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1>
   		<pimsForm:text validation="required:true, custom:function(val,alias){return idValue(val,alias)}" value="${constructBean.name}"  name="construct_id" alias="Construct id" helpText="Construct ID must be unique for this Target" />
		<pimsForm:text validation="required:true, wholeNumber:true, minimum:1, custom:function(val,alias){return dnaConStart(val,alias)}" value="1"  name="target_dna_start" alias="Start nucleotide" helpText="Start nucleotide for Construct, default is first nucleotide in Target DNA Sequence" />
		<pimsForm:text validation="required:true, wholeNumber:true, minimum:1, custom:function(val,alias){return dnaConEnd(val,alias)}" value="${targLen}" name="target_dna_end" alias="End nucleotide" helpText="End nucleotide for Construct, default is last nucleotide in Target DNA Sequence" />
   		<pimsForm:select name="labNotebookId" alias="Lab Notebook" helpText="The Lab Notebook this construct belongs to">
	     	<c:forEach var="access" items="${accessObjects}">
	     		<c:choose>
   					<c:when test="${constructBean.access.hook == access.hook}">
           				<option value="${access.hook}" selected="selected" ><c:out value="${access.name}" /></option>
           			</c:when>
   					<c:otherwise>
   						<option value="${access.hook}" ><c:out value="${access.name}" /></option>
   					</c:otherwise>
   				</c:choose>
         	</c:forEach>
       	</pimsForm:select>
   </pimsForm:column1>
    <input type="hidden" name="dna_target" value="<c:out value='${constructBean.dnaTarget}' />"/>
	<input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
	<input type="hidden" name="target_name" value="<c:out value='${constructBean.targetName}' />"/>
	<input type="hidden" name="target_dna_seq" id="targetds" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
    <input type="hidden"  name="wizard_step" value="1dna"/>
  </pimsForm:formBlock>
  <pimsForm:formBlock id="blk2">
	<div style="text-align:right">
   	<input name="Submit" style="text-align:right" type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn()" />
	</div>
  </pimsForm:formBlock>
  <script type="text/javascript">
<!--
function onLoadPims() {
	var i=0;
	
    <c:forEach items="${milestones}" var="scm">  
	    str="<c:out value="${utils:escapeJS(scm.name)}" />";
	    /*
	    tar='${param["commonName"]}';
	    if (str.indexOf(tar) >= 0) {
	        str=str.substring(tar.length+1);
	    }
	    */
	    construct_id_store[i]=str;
	    i++;
    </c:forEach>
} // -->
</script>  
 </pimsForm:form>
</pimsWidget:box>
 </c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%--  
The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> 
--%>   
<jsp:include page="/JSP/core/Footer.jsp" />
