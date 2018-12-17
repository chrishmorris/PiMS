<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.sql.*"  %> 
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

<jsp:useBean id="milestones" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructResultBean>" /> 
<jsp:useBean id="constructBean" scope="request" type="ConstructBean" />
<jsp:useBean id="transSeqChunks" scope="request" type="java.util.List<java.lang.String>" />

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New construct: Step 1' />
</jsp:include>
<!-- CreateExpressionObjective.jsp -->
<c:if test="${not empty constructBean.dnaTarget }">
 <jsp:forward page="/JSP/dnaTarget/DNAConstructWizardStep1.jsp"/>
</c:if>

<c:set var="breadcrumbs">
<a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
    <c:forEach var="target" items="${constructBean.targetBeans}" > 
        <pimsWidget:link bean="${target}" />
            <c:choose>
                <c:when test="${null ne syntheticGeneBean}" >
                     : <pimsWidget:link bean="${syntheticGeneBean}" />
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>                 
    </c:forEach>
</c:set>

<pimsWidget:pageTitle 
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="New Construct: Basic details"
/>

<c:set var="dnaSource">
    <c:choose>
        <c:when test="${null ne syntheticGeneBean}" >
            Synthetic gene
        </c:when>
        <c:otherwise>
            Target
        </c:otherwise>
    </c:choose>
</c:set>
<pimsWidget:box id="box1" title="Translated Protein Sequence <em>-translated from the ${dnaSource} DNA sequence</em>" initialState="open" >
 <pimsForm:form action="" method="" mode="view" >
	<div class="formblock" >
		<p><strong>Please note</strong> that this is <strong>translated</strong> from the ${dnaSource} DNA sequence. <br /> 
		</p>
	</div>
	<div class="formblock" >
	<%--PIMS-3432 to account for double and triple terminal stop codons --%>
	<c:choose>
        <c:when test="${fn:endsWith(constructBean.targetProtSeq,'***')}" >
            <c:set var="seqLen" value="${fn:length(constructBean.targetProtSeq)-3}" />
        </c:when>
        <c:when test="${fn:endsWith(constructBean.targetProtSeq,'**')}" >
            <c:set var="seqLen" value="${fn:length(constructBean.targetProtSeq)-2}" />
        </c:when>
		<c:when test="${fn:endsWith(constructBean.targetProtSeq,'*')}" >
			<c:set var="seqLen" value="${fn:length(constructBean.targetProtSeq)-1}" />
		</c:when>
		<c:otherwise>		
			<c:set var="seqLen" value="${fn:length(constructBean.targetProtSeq)}" />
		</c:otherwise>
	</c:choose>
		<div class="sequence">
			<c:forEach items="${transSeqChunks}" var="chunk" varStatus="status">
			  <c:choose>
			  <c:when test="${empty chunk}"/>
			  <c:otherwise>
		 		<c:out value="${chunk}" />&nbsp;&nbsp;<strong>
     			<c:choose>
        			<c:when test="${status.last}">
         				<c:out value="${seqLen}" />
        			</c:when>
        			<c:otherwise>
	       				<c:out value="${status.count*100}" />
	    			</c:otherwise>
      			</c:choose>
      		</strong><br />
      		</c:otherwise>
      		</c:choose>
			</c:forEach>
			<br />
		</div>
	</div>
	<div class="formblock" >
	
    <%--PIMS-3435 --%>
    <%--Modified for synthetic gene needs sample hook --%>
        <c:if test="${! empty constructBean.targetDnaSeq}">
            <c:choose> 
                <c:when test="${null ne syntheticGeneBean}" >
                    <c:set var="dpsurl" value="${pageContext.request.contextPath}/read/DNAandProtSeqPopup/${syntheticGeneBean.hook}"/>
                </c:when>
                <c:otherwise>       
                    <c:set var="dpsurl" value="${pageContext.request.contextPath}/read/DNAandProtSeqPopup/${constructBean.targetBean.pimsTargetHook}"/>
                </c:otherwise> 
            </c:choose>
             <a class="popup" href="javascript:widePopUp('${dpsurl}')">Pop-up</a> view of the ${dnaSource} DNA and translated Protein sequence    
        </c:if>
        <br /><br />
	</div>
 </pimsForm:form>	
</pimsWidget:box>

<pimsWidget:box id="box2" title="Basic Details" initialState="open" >

 <pimsForm:form action="/update/CreateExpressionObjective" id="frmStep1" method="post" mode="create" >
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1>
<pimsForm:formItem name="construct_id" alias="Construct Id" validation="required:true, custom:function(val,alias){return idValue(val,alias)}" >
    <pimsForm:formItemLabel name="construct_id" alias="Construct Id" helpText="Construct ID must be unique for this Target" 
    validation="required:true, custom:function(val,alias){return idValue(val,alias)}"  /> 
    ${constructBean.targetName}.<input type="text" name="construct_id" id="construct_id"  value="<c:out value='${value}'/>" onchange="onEdit();" />
    
</pimsForm:formItem>
		<pimsForm:text validation="required:true, wholeNumber:true, minimum:1, custom:function(val,alias){return protConStart(val, alias)}" value="1"  name="target_prot_start" alias="Target protein start" helpText="Start position of expression product in the Translated Protein Sequence" />
		<c:choose>
		 <c:when test="${fn:endsWith(constructBean.targetProtSeq,'***')}">
	   		<pimsForm:text validation="required:true, wholeNumber:true,  custom:function(val,alias){return protConEnd(val, alias)}" value="${fn:length(constructBean.targetProtSeq)-3}"  name="target_prot_end" alias="Target protein end" helpText="End position of expression product in the Translated Protein Sequence" />
		 </c:when>
         <c:when test="${fn:endsWith(constructBean.targetProtSeq,'**')}">
            <pimsForm:text validation="required:true, wholeNumber:true,  custom:function(val,alias){return protConEnd(val, alias)}" value="${fn:length(constructBean.targetProtSeq)-2}"  name="target_prot_end" alias="Target protein end" helpText="End position of expression product in the Translated Protein Sequence" />
         </c:when>
         <c:when test="${fn:endsWith(constructBean.targetProtSeq,'*')}">
            <pimsForm:text validation="required:true, wholeNumber:true,  custom:function(val,alias){return protConEnd(val, alias)}" value="${fn:length(constructBean.targetProtSeq)-1}"  name="target_prot_end" alias="Target protein end" helpText="End position of expression product in the Translated Protein Sequence" />
         </c:when>
		 <c:otherwise>
		 	<pimsForm:text validation="required:true, wholeNumber:true,  custom:function(val,alias){return protConEnd(val, alias)}" value="${fn:length(constructBean.targetProtSeq)}"  name="target_prot_end" alias="Target protein end" helpText="End position of expression product in the Translated Protein Sequence" />
		 </c:otherwise>
		</c:choose> 
		
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
   <pimsForm:column2>
       <h3 class="plainHead">Choose one:</h3>
       <ul>
        <li>
          Primer design Tm <input name="desired_tm" value="60" />
          <c:set var="disabled" value="" />
          <c:if test="${empty constructBean.targetProtSeq || 50 > fn:length(constructBean.targetDnaSeq)}"><c:set var="disabled" value="disabled=disabled" /></c:if>
   		  <%-- <input type="hidden"  name="design_type" value="York"/> --%>
   		  <input name="Submit" ${disabled} style="width:auto; overflow:visible;" type="submit" value="Save and Design Primers &gt;" id="primerTm" onclick="this.form.clickbuttonid=this.id; dontWarn();" />
   		  <%--<script>alert($('primerTm').disabled);</script> --%>
   		<br /><br /></li>
   		<c:set var="savePrimerlessConstruct" value="<%= org.pimslims.servlet.spot.CreateExpressionObjective.SAVE_PRIMERLESS_CONSTRUCT %>" />
   		<li>Primerless construct <input name="Submit" style="width:auto; overflow:visible;" type="submit" value="${savePrimerlessConstruct}" id="primerLess" onclick="this.form.clickbuttonid=this.id; dontWarn();" /></li>
   		</ul>
   </pimsForm:column2> 
   <c:forEach var="target" items="${constructBean.targetBeans}" >
  	<input type="hidden"  name="pims_target_hook" value="<c:out value='${target.hook}' />"/>
	<input type="hidden"  id="target_prot_seq" name="target_prot_seq" value="<c:out value='${target.protSeq}' />"/>
    <input type="hidden"  name="target_dna_seq" value="<c:out value='${target.dnaSeq}' />"/>
    <input type="hidden"  name="wizard_step" value="1"/>
    <input type="hidden"  name="sampleHook" value="${sampleHook}"/>
    <input type="hidden"  name="sampleName" value="${sampleName}"/>
  </c:forEach>
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

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%--  
The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> 
--%>


<jsp:include page="/JSP/core/Footer.jsp" />


