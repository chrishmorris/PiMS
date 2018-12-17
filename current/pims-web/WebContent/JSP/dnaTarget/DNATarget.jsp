<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.text.*,java.util.*,org.pimslims.presentation.*,org.pimslims.lab.sequence.*"  %>
<%@ page import="org.pimslims.model.target.Target"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<pims:import className="org.pimslims.model.target.Target" />
<pims:import className="org.pimslims.model.experiment.Experiment" />
<%-- bean declarations --%>
<jsp:useBean id="targetBean" scope="request" type="org.pimslims.presentation.TargetBean" />
<%-- not used jsp:useBean id="constructBeans" scope="request" type="java.util.Collection" / --%>
<jsp:useBean id="milestones" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructResultBean>" /> 
<jsp:useBean id="gcCont" scope="request" type="java.lang.Float" />

<%-- 
	DNATarget.jsp
	Author: Susy Griffiths YSBL
	Date: 01-Feb-2008
	Servlets: DnaTarget.java
	Re-design: January 2009
--%>

<c:catch var="error">
<c:set var="seqLen" value="${fn:length(fn:replace(targetBean.protSeq, ' ', ''))}" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='DNA Target: ${targetBean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- DnaTarget.jsp -->
<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="${targetBean.name}"/>
<c:set var="actions">
	<c:choose>
		<c:when test="${targetBean.protSeq==null||targetBean.protSeq==''||seqLen<=49}">
			<pimsWidget:linkWithIcon isGreyedOut="true" icon="actions/create/construct_greyedout.gif" url="#" 
        		title="The target must have a DNA sequence before you can record a construct" text="Can't add construct"/>
        </c:when>
            <c:when test="${! mayUpdate}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="You do not have permission to record a construct"
                text="Can't add construct"/>
            </c:when>           
        <c:otherwise><pimsWidget:linkWithIcon  isNext="${empty milestones }"
        		icon="actions/create/construct.gif" 
        		url="${pageContext.request.contextPath}/DNAConstructWizard/${targetBean.hook}?wizard_step=1" 
        		text="New construct"/>
        </c:otherwise>
    </c:choose>
	<pimsWidget:diagramLink hook="${targetBean.pimsTargetHook}"></pimsWidget:diagramLink>
	<%-- custom deleteLink --%>        
    <c:set var="title" value="${targetBean.name}"/><c:set var="deleteQuery" value="" />
    <c:choose><c:when test="${targetBean.mayDelete}">
      <span class="linkwithicon " title="Delete construct "><a  href="${pageContext.request.contextPath}/Delete/${targetBean.hook}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request.contextPath}/Delete/${targetBean.hook}">Delete</a></span>
    </c:when><c:otherwise>
      <pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" />
    </c:otherwise></c:choose>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<c:set var="roleName">targetGroups</c:set>
<c:set var="objectHook">${targetBean.hook}</c:set>
<c:set var="title">Target groups</c:set>


<pimsWidget:box id="details" title="DNA Target Details" initialState="open" >
<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
<pimsForm:formBlock id="blk1">
	<pimsForm:column1>
		<pimsForm:text validation="required:true" value="${targetBean.name}"  name="${targetBean.pimsTargetHook}:${Target['PROP_NAME']}" alias="Name" helpText="The name of the Target" />
		<pimsForm:text validation="required:true" value="${targetBean.protein_name}"  name="${targetBean.proteinNameHook}" alias="Sequence name" helpText="The name of the DNA element" />
		<%--DONT show aliases for DNA Target, used as DNA Target criteria
		<pimsForm:text validation="required:false" value="${targetBean.aliases}"  name="${targetBean.pimsTargetHook}:${Target['PROP_ALIASES']}" alias="Aliases" helpText="The alias of the Target" />
		 --%>
		<pimsForm:mruSelect  hook="${targetBean.pimsTargetHook}" rolename="${Target['PROP_SPECIES']}" required="true" alias="Organism" helpText="The source organism for the DNA Target" />
		<%--TODO should function be the mru for DNA Targets ?--%>
		<pimsForm:text validation="required:false" value="${targetBean.func_desc}"  name="${targetBean.pimsTargetHook}:${Target['PROP_FUNCTIONDESCRIPTION']}" alias="Function Description" helpText="Description of the DNA Target function or role" />
	</pimsForm:column1>
	<pimsForm:column2>
			
		<pimsForm:mruSelect  hook="${targetBean.pimsTargetHook}" rolename="${Target['PROP_CREATOR']}" required="false" alias="Scientist" helpText="The scientist responsible for the Target" />
		<pimsForm:textarea validation="required:false" name="${targetBean.pimsTargetHook}:${Target['PROP_WHYCHOSEN']}" alias="Comments" helpText="Comments e.g. Why Target was selected" ><c:out value="${targetBean.comments}" />
		</pimsForm:textarea>
	</pimsForm:column2>
</pimsForm:formBlock>
<pimsForm:formBlock id="blk4">
	<pimsForm:column2>
		<pimsForm:editSubmit />
	</pimsForm:column2>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

<pimsWidget:box id="sequences" title="DNA Sequence" initialState="closed" >
<pimsForm:form action="/Update" id="ID" method="post" mode="view" boxToOpen="sequences" >
<pimsForm:formBlock id="blk2">
<%-- If there is no MolComponent DNA --%>
	<c:choose>
	<c:when test="${empty targetBean.protSeqHook}">
  <c:if test="${mayUpdate}">
		<input type="button" style="float: left;" class="submit" onclick="if(!document.attributeChanged || confirm('Unsaved changes detected, they will be lost if you proceed. Proceed?')) $('addDNA').submit();" value="Add DNA sequence..." />
	</c:if>
	</c:when>
<c:otherwise>
  	<pimsForm:textarea  name="${targetBean.protSeqHook}" alias="DNA sequence" helpText="The DNA sequence of the Target" validation="dnaSequence:true" onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${fn:replace(targetBean.protSeq, ' ', '')}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    
    <div class="formitem viewonly" style="padding-left:12.5em">
     <span align='right'><strong title="Length (bp) of the Target DNA sequence" > Length: </strong><c:out value="${seqLen} "/>
      &nbsp; <strong title="%GC nucleotides in the Target DNA sequence" > &#037;GC: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" >
      <c:out value="${gcCont}"/></fmt:formatNumber>  
      <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${targetBean.hook}?type=targetDNA" />
            &nbsp; <a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    

     </span>
    </div>
</c:otherwise>
</c:choose>
</pimsForm:formBlock>
        <pimsForm:editSubmit />
</pimsForm:form>
</pimsWidget:box>



    <c:set var="newConstruct">
      <c:choose>
        <c:when test="${targetBean.protSeq==null||targetBean.protSeq==''}">
           You need a DNA sequence to create a New Construct. <br/>
        </c:when>
        <c:when test="${seqLen<50}">
           DNA sequence must be &ge;50 nucleotides to record Constructs. <br />
        </c:when>
       <c:when test="${!mayUpdate}">
           No permission to Design a Construct<br />
        </c:when>
        <c:otherwise>
            <a href='${pageContext.request.contextPath}/DNAConstructWizard/<c:out value="${targetBean.hook}" />?wizard_step=1' 
              ${empty milestones ? 'tabindex="1"' : '' }
            >Design new Construct</a><br/>            
        </c:otherwise>
      </c:choose>
    </c:set>


<pimsWidget:box id="constructs" title="Constructs" initialState="open" extraHeader="${newConstruct}">
    		<c:choose>
        		<c:when test="${empty milestones }">
           			<br /> No constructs found!<br />
        		</c:when>
        		<c:when test="${! empty milestones}">
            		<table class="list">
                	  <tr><th colspan='2' >Construct</th><th colspan='3' >Latest Experiment</th><th colspan="2" rowspan="2">&nbsp;</th></tr>
                	  <tr><th align='left'>Name</th><th align='left'>Description</th>
                    	<th align='left'>Milestone</th><th align='left'>Experiment Date</th>
                    	<th align='left'>Status</th></tr>
	
                    	<c:forEach items="${milestones}" var="scm">
                		<tr>
                    	<td><pimsWidget:linkWithIcon 
                        icon="types/small/construct.gif" 
                        url="${pageContext.request.contextPath}/View/${scm.constructHook}" 
                        text="${scm.name}"/></td>

                    	<td><c:out value="${scm.constructDescription}" /></td>
                    	<td><c:out value="${scm.milestoneName}" /></td>
                    	<td><pimsWidget:dateLink date="${construct.dateOfExperiment}"  /></td>
                    	<td><c:out value="${scm.result}" /></td>
                    	<td><a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=<c:out value="${scm.name}" />">All Experiments</a></td>
                    	<td>
                        <pimsWidget:linkWithIcon 
                         icon="actions/create/experiment.gif" 
                         url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${scm.constructHook}" 
                         text="New experiment"/></td>
                	 	</tr>
            			</c:forEach>
            		</table>
        		</c:when>
    		</c:choose>
</pimsWidget:box >

<%--Experiments --%>
<pimsWidget:box id="experiments" title="Experiments"  initialState="closed" >
<c:choose>
	    <c:when test="${empty experimentBeans}">
	       There are no experiments on this Target <br/>
	    </c:when>
	    <c:otherwise>
	        <jsp:include page="/JSP/spot/TargetExperiments.jsp"></jsp:include>
	    </c:otherwise>
	</c:choose>
</pimsWidget:box>

<pimsWidget:multiRoleBox objectHook="${targetBean.hook}" roleName="targetGroups" title="Target Groups" />

<%--Target Groups --%>
<jsp:include page="/JSP/spot/TargetGroups.jsp"></jsp:include>

<%--Database Refs --%>
<pimsWidget:externalDbLinks bean="${targetBean}" dbnames="${dbnames}"/>


<%--Blast searches --%>
<c:if test="${!empty (targetBean.protSeq)}">
<pimsWidget:box id="blast" title="Blast searches" initialState="closed" >
	<a href="${pageContext.request.contextPath}/read/Blast/PDBBlast?blast=Run%20EMBL%20Blast&amp;target_protSeq=<c:out value='${targetBean.protSeq}'/>&amp;target_protName=<c:out value='${targetBean.protein_name}'/>&amp;target_hook=<c:out value='${targetBean.hook}' />">EMBL Blast results</a>
	<br />
</pimsWidget:box>
</c:if>

<pimsWidget:files bean="${targetBean}"  />

<pimsWidget:notes bean="${targetBean}"  />


<script type="text/javascript"><!--
function validateForm(form) {
<c:if test="${!empty targetBean.dnaSeqHook}">
    if (!checkDNASequence(form['${targetBean.dnaSeqHook}'].value)) {
	    return false;
	}
</c:if>
<c:if test="${!empty targetBean.protSeqHook}">
    if (!checkProteinSequence(form['${targetBean.protSeqHook}'].value)) {
	    return false;
	}
</c:if>
    return true;
} // -->
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
