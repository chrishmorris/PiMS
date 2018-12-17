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

<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />
<jsp:useBean id="transSeqChunks" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="protConChunks" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="protConDNAChunks" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="fixNonstandardStartCodon" scope="request" type="java.lang.String" />

<%--
spot/PrimerDesign.jsp
Form displayed after user selects new construct type = York in Step1
Default Tm for first set of primers calculated from the sequence
User may provide Tm value and primers are re-calculated from the sequence
Re-design by Susy Griffiths November 2008

TODO replace this with /construct/DesignPrimers.jsp
--%>
<c:catch var="error">
SpotNewConstructWizardStep2c.jsp obsolete
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New construct: Choose Primer Overlaps' />
</jsp:include>
<!-- PrimerDesign.jsp -->

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
 : New Construct ${constructBean.name}
	</c:forEach>
</c:set>

<pimsWidget:pageTitle 
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="Step 2: Choose Primer Overlaps"
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
    <pimsForm:formBlock id="b1blk1">
        <p><strong>Please note</strong> that this is <strong>translated</strong> from the ${dnaSource} DNA sequence. <br /> 
		</p>
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b1blk2" >
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
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b1blk3" >
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
	</pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<pimsWidget:box id="box2" title="Basic Details: from Step 1" initialState="closed" >
 <pimsForm:form action="" method="" mode="view" >
	<pimsForm:formBlock id="b2blk1" >
		<pimsForm:text name="construct_id" alias="Construct Id" value="${constructBean.name}" helpText="Unique identifier for the Construct" />
		<pimsForm:text name="target_prot_start" alias="Target protein start" value="${constructBean.targetProtStart}" helpText="Start position of expression product in the Translated Protein Sequence"/>
		<pimsForm:text name="target_prot_end" alias="Target protein end" value="${constructBean.targetProtEnd}" helpText="End position of expression product in the Translated Protein Sequence"/>
		<pimsForm:text name="des_tm" alias="Primer design Tm" value="${constructBean.desiredTm}" helpText="Tm used for Primer design" />
	</pimsForm:formBlock>
	<hr />			
	<pimsForm:formBlock id="b2blk2" >		
		<h3 class="plainHead">Insert DNA sequence:</h3>
		<br /><br />
		<c:set var="dSeqLen" value="${fn:length(constructBean.dnaSeq)}" />
		<div class="sequence">
			<c:forEach items="${protConDNAChunks}" var="dchunk" varStatus="status">
              <c:choose>
              <c:when test="${empty dchunk}"/>
              <c:otherwise>
		 		<c:out value="${dchunk}" />&nbsp;&nbsp;<strong>
     			<c:choose>
        			<c:when test="${status.last}">
         				<c:out value="${dSeqLen}" />
        			</c:when>
        			<c:otherwise>
	       				<c:out value="${status.count*100}"/>
	    			</c:otherwise>
      			</c:choose>
      		</strong><br />
      		</c:otherwise>
      		</c:choose>
			</c:forEach>
		</div>
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b2blk3" >
		<br />		
		<h3 class="plainHead">Insert Protein sequence:</h3>
		<br /><br />
		<c:set var="pSeqLen" value="${fn:length(constructBean.protSeq)}" />
		<div class="sequence">
			<c:forEach items="${protConChunks}" var="pchunk" varStatus="status">
              <c:choose>
              <c:when test="${empty pchunk}"/>
              <c:otherwise>
		 		<c:out value="${pchunk}" />&nbsp;&nbsp;<strong>
     			<c:choose>
        			<c:when test="${status.last}">
         				<c:out value="${pSeqLen}" />
        			</c:when>
        			<c:otherwise>
	       				<c:out value="${status.count*100}"/>
	    			</c:otherwise>
      			</c:choose>
      		</strong><br />
      		</c:otherwise>
      		</c:choose>
			</c:forEach>
			<br />
		</div>
	</pimsForm:formBlock>
	<pimsForm:formBlock id="b2blk4" >
    <%--PIMS-3435 --%>   
    <%--Modified for synthetic gene needs sample hook --%>
        <c:if test="${! empty constructBean.targetDnaSeq}">
            <c:choose> 
                <c:when test="${null ne syntheticGeneBean}" >
                    <c:set var="dpsurl" value="${pageContext.request.contextPath}/read/DNAandProtSeqPopup/${syntheticGeneBean.hook}?start=${constructBean.targetProtStart}&amp;end=${constructBean.targetProtEnd}&amp;protId=${constructBean.name}"/>
                </c:when>
                <c:otherwise>       
                    <c:set var="dpsurl" value="${pageContext.request.contextPath}/read/DNAandProtSeqPopup/${constructBean.targetBean.pimsTargetHook}?start=${constructBean.targetProtStart}&amp;end=${constructBean.targetProtEnd}&amp;protId=${constructBean.name}"/>
                </c:otherwise> 
            </c:choose>
             <a class="popup" href="javascript:widePopUp('${dpsurl}')">Pop-up</a> view of the ${dnaSource} DNA and translated Protein sequence    
        </c:if>

		<br /><br />
	</pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<pimsWidget:box id="box3" title="Suggested Primer Overlaps for Tm ${constructBean.desiredTm} &deg;C:" initialState="open" >
 <pimsForm:form action="/update/CreateExpressionObjective" id="frmStep2c" method="post" mode="create" >
	<pimsForm:formBlock id="b3blk1">
		<%--020209 Revised code to check first codon ATG, GTG or CTG are possible start codons --%>
			<c:if test="${!fn:startsWith(constructBean.dnaSeq, 'ATG')}">
				<c:choose>
					<c:when test="${fn:startsWith(constructBean.dnaSeq, 'GTG')&& constructBean.targetProtStart==1}">
						<c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
						<pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting GTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
					</c:when>
					<c:when test="${fn:startsWith(constructBean.dnaSeq, 'TTG')&& constructBean.targetProtStart==1}">
						<c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
						<pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting TTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
					</c:when>			
					<c:when test="${fn:startsWith(constructBean.dnaSeq, 'CTG')&& constructBean.targetProtStart==1}">
						<c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
						<pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting CTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
					</c:when>			
				</c:choose>
			</c:if>
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
   <c:forEach var="target" items="${constructBean.targetBeans}" >
    <input type="hidden"  name="pims_target_hook" value="<c:out value='${target.hook}' />"/>
    <input type="hidden"  name="target_dna_seq" id="target_dna_seq" value="<c:out value='${target.dnaSeq}' />"/>
    <input type="hidden"  id="reverse_seq" value="<c:out value='${constructBean.reverseSeq}' />"/>
  </c:forEach>
	 <input type="hidden" name="construct_id" value="<c:out value='${constructBean.nameSuffix}' />"/>
	 <input type="hidden" name="target_prot_start" value="<c:out value='${constructBean.targetProtStart}' />"/>
	 <input type="hidden" name="target_prot_end" value="<c:out value='${constructBean.targetProtEnd}' />"/>
    <input type="hidden" id="dnaSeq" name="dnaSeq" value="<c:out value='${constructBean.dnaSeq }' />"/>
    <input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
    <input type="hidden"  name="wizard_step" value="1"/>
    <input type="hidden"  name="sampleHook" value="${sampleHook}"/>
	<hr />
	</pimsForm:formBlock>	 
  </pimsForm:form>

  <pimsForm:form action="/spot/SpotNewConstructWizard" id="frmStep2c" method="post" mode="create" >
	<pimsForm:formBlock id="b3blk4">
	      <jsp:include page="/JSP/spot/PrimerList.jsp" />
	</pimsForm:formBlock>
    <input type="hidden" name="wizard_step" value="2c"/>
    <input type="hidden" name="protein_name" value="<c:out value='${constructBean.targetName}' />"/>
	<input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
	<input type="hidden" name="construct_id" value="<c:out value='${constructBean.name}' />"/>
	<input type="hidden" name="target_prot_start" value="<c:out value='${constructBean.targetProtStart}' />"/>
	<input type="hidden" name="target_prot_end" value="<c:out value='${constructBean.targetProtEnd}' />"/>
	<input type="hidden" name="target_dna_seq" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
	<input type="hidden" name="target_prot_seq" value="<c:out value='${constructBean.targetProtSeq}' />"/>
	<input type="hidden" name ="desired_tm" value="<c:out value='${constructBean.desiredTm}' />"/>
	<input type="hidden" id="dnaSeq" name="dnaSeq" value="<c:out value='${constructBean.dnaSeq }' />"/>
	<input type="hidden" id="fixNonstandardStartCodon" name="fixNonstandardStartCodon" value="<c:out value='${fixNonstandardStartCodon}' />"/>
	<input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>		    
    <input type="hidden"  name="sampleHook" value="${sampleHook}"/>
 </pimsForm:form>	
</pimsWidget:box>

<%-- The following table is for debugging purposes.  Please comment it out for normal use
<jsp:include page="/ConstructBeanDebug" />
 --%>
<%--
  <script type="text/javascript">
<!--
function onLoadPims() {
    var i=0;
    
    <c:forEach items="${milestones}" var="scm">  
        str="<c:out value="${utils:escapeJS(scm.name)}" />";
        construct_id_store[i]=str;
        i++;
    </c:forEach>
} // -->
</script>  --%>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />


