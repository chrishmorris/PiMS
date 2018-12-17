<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.presentation.*,org.pimslims.lab.sequence.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<pims:import className="org.pimslims.model.target.Target" />
<pims:import className="org.pimslims.model.experiment.Experiment" />
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />
<%-- not used jsp:useBean id="constructBeans" scope="request" type="java.util.Collection" / --%>
<jsp:useBean id="milestones" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructResultBean>" />
<jsp:useBean id="gcCont" scope="request" type="java.lang.Float" />
<jsp:useBean id="protEX" scope="request" type="java.lang.Float" />
<jsp:useBean id="protMass" scope="request" type="java.lang.Float" />
<jsp:useBean id="abs01pc" scope="request" type="java.lang.Float" />
<jsp:useBean id="experimentMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="experimentBeans" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" />
<jsp:useBean id="compareSeqs" scope="request" type="java.lang.Boolean" />
<%--
//----------------------------------------------------------------------------------------------
//			SpotTarget.jsp
//			Target page
//
//		 	Created by Johan van Niekerk,SSPF-Dundee				10 October 2005
//			Modified by	Susy Griffiths June 2007
			+ Peter Troshin Feb 2008											Date
//----------------------------------------------------------------------------------------------
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Target: ${targetBean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!--SpotTarget.jsp-->

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="${targetBean.name}"/><c:set var="deleteQuery" value="" />
<c:set var="actions">
	<c:choose><c:when test="${empty targetBean.dnaSeq}"><pimsWidget:linkWithIcon 
				isGreyedOut="true"
        		icon="actions/create/construct_greyedout.gif" 
        		url="#" 
        		title="The target must have a DNA sequence before you can record a construct"
        		text="Can't add construct"/>
        	</c:when>
        	<c:when test="${(fn:length(targetBean.dnaSeq)%3) !=0}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="The DNA sequence must be a multiple of 3 nucleotides long to record a construct"
                text="Can't add construct"/>
            </c:when>        	
            <c:when test="${! mayUpdate}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="You do not have permission to record a construct"
                text="Can't add construct"/>
            </c:when>           
        <c:otherwise><pimsWidget:linkWithIcon isNext="${empty milestones }"
        		icon="actions/create/construct.gif" 
        		url="${pageContext.request.contextPath}/update/CreateExpressionObjective/${targetBean.hook}" 
        		text="New construct"/>
        </c:otherwise>
    </c:choose>
	<pimsWidget:diagramLink hook="${targetBean.pimsTargetHook}"></pimsWidget:diagramLink>
	<%-- custom deleteLink --%>
    <c:choose><c:when test="${targetBean.mayDelete}">
      <span class="linkwithicon " title="Delete construct "><a  href="${pageContext.request.contextPath}/Delete/${targetBean.hook}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request.contextPath}/Delete/${targetBean.hook}">Delete</a></span>
    </c:when><c:otherwise>
      <pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" />
    </c:otherwise></c:choose>
    
    <pimsWidget:linkWithIcon 
                icon="actions/create/default.gif" 
                url="${pageContext.request.contextPath}/update/CreateSyntheticGene/${targetBean.hook}" 
                text="New Synthetic gene"/>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box id="details" title="Basic Details" initialState="open" >
<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
<pimsForm:formBlock id="blk1">
	<pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${targetBean.hook}:pageNumber" value="${targetBean.values['pageNumber']}"
    />
		<pimsForm:text validation="required:true" value="${targetBean.name}"  name="${targetBean.pimsTargetHook}:${Target['PROP_NAME']}" alias="Name" helpText="The name of the Target" />
		<c:choose>
		  <c:when test="${!fn:contains(targetBean.aliases,'Natural Source Target')}" >
    		<pimsForm:text validation="required:true" value="${targetBean.protein_name}"  name="${targetBean.proteinNameHook}" alias="Protein name" helpText="The name of the Target protein" />
    		<pimsForm:text validation="required:false" value="${targetBean.aliases}"  name="${targetBean.pimsTargetHook}:${Target['PROP_ALIASES']}" alias="Aliases" helpText="The alias of the Target" />
          </c:when>
          <c:otherwise>
            <pimsForm:text validation="required:true" value="${targetBean.protein_name}"  name="${targetBean.proteinNameHook}" alias="Source name" helpText="The name of the Target source" />
          </c:otherwise>
	    </c:choose>
		<pimsForm:mruSelect  hook="${targetBean.pimsTargetHook}" rolename="${Target['PROP_SPECIES']}" required="true" alias="Organism" helpText="The source organism for the DNA Target" />
		 
		<pimsForm:text validation="required:false" value="${targetBean.func_desc}"  name="${targetBean.pimsTargetHook}:${Target['PROP_FUNCTIONDESCRIPTION']}" alias="Function Description" helpText="The Function Description of the Target" />
		<%-- 
		<pimsForm:textarea validation="required:false" name="${targetBean.pimsTargetHook}:${Target['PROP_FUNCTIONDESCRIPTION']}" alias="Function Description" helpText="The Function Description of the Target" ><c:out value="${targetBean.func_desc}" />
		</pimsForm:textarea>
		--%>
	</pimsForm:column1>
	<pimsForm:column2>
		<pimsForm:nonFormFieldInfo label ="Lab Notebook" helpText="Lab Notebook to which the Target belongs" >
			<c:out value="${targetBean.labNotebook}" />
		</pimsForm:nonFormFieldInfo> 
		<pimsForm:writer  hook="${targetBean.pimsTargetHook}" rolename="${Target['PROP_CREATOR']}" required="false" alias="Scientist" helpText="The scientist responsible for the Target" />
		<pimsForm:textarea validation="required:false" name="${targetBean.pimsTargetHook}:${Target['PROP_WHYCHOSEN']}" alias="Comments" helpText="Comments e.g. Why Target was selected" ><c:out value="${targetBean.comments}" />
		</pimsForm:textarea>

	</pimsForm:column2>
</pimsForm:formBlock>
	<pimsForm:column2>
		<pimsForm:editSubmit />
	</pimsForm:column2>
</pimsForm:form>
</pimsWidget:box>

<%--030209 Checking for non-standard start codon--%>
	<c:set var="startCodon">
	<c:choose>
		<c:when test="${fn:startsWith(targetBean.dnaSeq, 'GTG')}">
			DNA sequence starts with GTG, edit the sequence to use ATG as start codon<br />
		</c:when>
		<c:when test="${fn:startsWith(targetBean.dnaSeq, 'TTG')}">
			DNA sequence starts with TTG, edit the sequence to use ATG as the start codon<br />
		</c:when>
		<c:when test="${fn:startsWith(targetBean.dnaSeq, 'CTG')}">
			DNA sequence starts with CTG, edit the sequence to use ATG as the start codon<br />
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	</c:set>
	
<%--011010 PIMS-876 Comparing Protein sequence and Translated DNA sequence --%>
    <c:set var="matchingSeqs">
    <c:choose>
        <c:when test="${compareSeqs=='false' && targetBean.protSeq  ne ''}" >
            Protein sequence and Translated DNA sequence do not match
        </c:when>
        <c:otherwise>
        </c:otherwise>
    </c:choose>
    </c:set>
	
	<c:set var="initialState" value="closed"/>
	<c:set var="mode" value="view"/>
	<c:if test="${createDNA}">
		<c:set var="initialState" value="open"/>
		<c:set var="mode" value="edit"/>
	</c:if>

<pimsWidget:box id="sequences" title="Sequences" initialState="${initialState}" extraHeader="${startCodon} ${matchingSeqs}">
<pimsForm:form action="/Update" id="ID" method="post" mode="${mode}" boxToOpen="sequences" >
    <%--PIMS-876 DNA sequence and protein sequence should match--%>
    <c:if test="${compareSeqs=='false' && targetBean.protSeq  ne ''}" >
    <pimsForm:formBlock id="blka">
    <p>
        <c:set var="url" value="${pageContext.request.contextPath}/read/CompareTargetSequences/${targetBean.hook}" />
        <a class="popup" href="javascript:popUp('${url}')">Alignment</a>    
      between translated DNA sequence to recorded protein sequence
    </p>
    </pimsForm:formBlock><hr />
</c:if>
<pimsForm:formBlock id="blk2">
<%-- If there is no MolComponent DNA --%>
	<c:choose>
	<c:when test="${empty targetBean.dnaSeqHook}">
  <c:if test="${mayUpdate}">
		<input type="button" style="float: left;" class="submit" onclick="if(!document.attributeChanged || confirm('Unsaved changes detected, they will be lost if you proceed. Proceed?')) $('addDNA').submit();" value="Add DNA sequence..." />
	</c:if>
	</c:when>
<c:otherwise>
	  
  	<pimsForm:textarea  extraClasses="sequence" name="${targetBean.dnaSeqHook}" alias="DNA sequence" id="DNASequence" helpText="The DNA sequence of the Target" validation="dnaSequence:true, custom:function(val,alias){return checkTargetDNASeq(val,alias)}" onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${targetBean.dnaSeq}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    
    <div class="formitem viewonly" style="padding-left:12.5em">
     <span align='right'>
        <strong title="Length (bp) of the Target DNA sequence" > Length: </strong><c:out value="${fn:length(targetBean.dnaSeq)}"/>
        &nbsp; <strong title="%GC nucleotides in the Target DNA sequence" > &#037;GC: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${gcCont}"/></fmt:formatNumber>
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${targetBean.hook}?type=targetDNA" />
            &nbsp; <a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    

        <c:if test="${! empty targetBean.dnaSeq}">
            <c:set var="rcurl" value="${pageContext.request.contextPath}/read/ReverseComplementPopup/${targetBean.hook}" />
            &nbsp; Reverse complement <a class="popup" href="javascript:widePopUp('${rcurl}')">Pop-up</a>    
        </c:if>
	 </span>
    </div>
	
	
</c:otherwise>
</c:choose>
</pimsForm:formBlock>
<pimsForm:formBlock id="blk3">
    <pimsForm:textarea extraClasses="sequence" name="${targetBean.protSeqHook}" alias="Protein sequence" helpText="The sequence of the Target protein" validation="proteinSequence:true" onchange="this.value=cleanSequence(this.value);">
        <pims:sequence sequence="${targetBean.protSeq}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (residues) of the Target protein sequence" > Length: </strong><c:out value="${fn:length(targetBean.protSeq)}"/>
        &nbsp; <strong title="Molecular weight (Da)"> Weight (Da): </strong><fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${protMass}" /></fmt:formatNumber>
        &nbsp; <strong title="Extinction coefficient cm-1 M-1, unreduced"> Extinction: </strong><c:out value="${protEX}" />
        &nbsp; <strong title="Absorbance of a 0.1% solution (1g/L)"> Abs 0.1&#037;: </strong>
        <fmt:formatNumber type="Number" maxFractionDigits="3" ><c:out value="${abs01pc}" /></fmt:formatNumber>
        &nbsp; <strong title="Isoelectric point pI"> pI: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${protPI}" /></fmt:formatNumber> &nbsp;
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${targetBean.hook}?type=targetProtein" />
        <br /><a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>
      </span>
    </div>
</pimsForm:formBlock>
        <pimsForm:editSubmit />
    <c:if test="${createDNA}">
    <script type="text/javascript">document.getElementById("DNASequence").focus();</script>
    </c:if>
</pimsForm:form>

  <hr />  
  <%-- Susy 21-03-2011 PIMS-3537 --%>
  <c:if test="${!empty targetBean.protSeq}" >
  <form action="http://web.expasy.org/cgi-bin/protparam/protparam" method="post" mode="view" style="padding-left: 1.7em; margin-bottom: .2em;" >
      Submit protein sequence to ExPASy ProtParam: <em>opens in a new tab</em>
           <input type="hidden" name="sequence" value="${targetBean.protSeq}" />
           <input type="submit" value="ProtParam" onclick="this.form.target='_blank';return true;" />
  </form>
  </c:if>
  <%--END PIMS-3537 --%> 

</pimsWidget:box>

    <c:set var="newConstruct">
      <c:choose>
        <c:when test="${(fn:length(targetBean.dnaSeq)%3) !=0}" >
           The DNA sequence must be a multiple of 3 nucleotides long to record a construct
        </c:when>
        <c:when test="${! mayUpdate}" >
          No permission to Design a Construct
        </c:when>
        <c:otherwise>
            <a href='${pageContext.request.contextPath}/update/CreateExpressionObjective/<c:out value="${targetBean.hook}"/>' 
                ${empty milestones ? 'tabindex="1"':'' }
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
                <tr><th colspan='2' >Construct</th><th colspan='3' >Latest Experiment</th><th rowspan="2">&nbsp;</th><th rowspan="2">&nbsp;</th></tr>
                <tr><th align='left'>Name</th><th align='left'>Description</th>
                    <th align='left'>Milestone</th><th align='left'>Date</th>
                    <th align='left'>Status</th></tr>

                    <c:forEach items="${milestones}" var="scm">
                <tr>
                    <td>
                      <c:choose>
                      <c:when test="${null == scm.expBlueprint}">
                       <pimsWidget:linkWithIcon 
                        icon="types/small/construct.gif" 
                        url="${pageContext.request.contextPath}/View/${scm.constructHook}" 
                        text="${scm.name}"/>
                     </c:when>
                      <c:otherwise>
                       <pimsWidget:linkWithIcon 
                        icon="types/small/construct.gif" 
                        url="${pageContext.request.contextPath}/read/ConstructView/${scm.constructHook}" 
                        text="${scm.name}"/>
                      </c:otherwise></c:choose>
                    </td>
                    <td><c:out value="${scm.constructDescription}" /></td>
                    <td><c:out value="${scm.milestoneName}" /></td>
                    <td><pimsWidget:dateLink date="${scm.dateOfExperiment}"  /></td> 
                    <td><c:out value="${scm.result}" /></td>
                    <td><a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=<c:out value="${scm.name}" />">All Experiments</a></td>
                    <td><pimsWidget:linkWithIcon 
                        icon="actions/create/experiment.gif" 
                        url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${scm.constructHook}" 
                        text="New experiment"/>
                    </td>
                </tr>
            </c:forEach>
            </table>
        </c:when>
    </c:choose>
    
</pimsWidget:box >


<%
    request.setAttribute("metaClass", experimentMetaClass);
    request.setAttribute("beans", experimentBeans);
 %>
    		
<pimsWidget:box id="experiments" title="Experiments"  initialState="closed" >
<c:choose>
	    <c:when test="${empty experimentBeans}">
	       There are no experiments on this Target <br/>
	    </c:when>
	    <c:otherwise> 
	        <%-- <jsp:include page="/JSP/spot/TargetExperiments.jsp"></jsp:include> --%>
	        <jsp:include page="/JSP/list/org.pimslims.model.experiment.Experiment.jsp"></jsp:include>
	    </c:otherwise>
	</c:choose>
</pimsWidget:box>

<c:choose>
    <c:when test="${!empty crystalTrialExps}">
		<pimsWidget:box id="crystalexperiments" title="Crystal Trial Experiments" initialState="closed" >
	        <table>
			<tr>
			<th align="left">Name</th>
			<th align="left">Experiment Type</th>
			<th align="left">Status</th>
			<th align="left">Date</th>
			</tr>
			<c:forEach items="${crystalTrialExps}" var="plate">
			<tr>
				<td><pimsWidget:link bean="${plate}" /></td>
				<td><c:out value="${plate.expTypeName}" /></td>
				<td><c:out value="${plate.status}" /></td>
				<td><c:out value="${plate.startDateString}" /></td>
			</tr>
			</c:forEach>
			</table>
		</pimsWidget:box>
		</c:when>
        <c:otherwise>
        </c:otherwise>
</c:choose>

<c:if test="${!empty sgbs}">
        <pimsWidget:box id="syntheticgenes" title="Synthetic genes" initialState="closed" >
            <table class="list">
                <tr>
                    <th>Name</th><th>Vector</th>
                        <c:forEach items="${sgbs}" var="sgb" varStatus="status" begin="0" end="0">
                            <c:set var="pars" value="sgb.parameters" />       
                               <c:forEach items="${sgb.parameters}" var="parameter">
                                    <th>
                                       ${parameter.name}
                                    </th>
                                </c:forEach>
                       </c:forEach>       
                </tr>
                <c:forEach items="${sgbs}" var="sgb" >
                    <tr>
                        <td><pimsWidget:link bean="${sgb }" /></td>
                        <td>
                           <c:forEach items="${sgb.inputsamples}" var="input" varStatus="status" begin="0" end="0">       
                                <pimsWidget:linkWithIcon 
                                icon="types/small/blank.gif" 
                                url="${pageContext.request.contextPath}/View/${input.sampleHook}" 
                                text="${fn:escapeXml(input.sampleName)}"/>
                            </c:forEach>
                         </td>
                         <c:forEach items="${sgb.parameters}" var="parameter">
                             <td>
                                ${parameter.value}
                             </td>
                         </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </pimsWidget:box>
</c:if>

      
<pimsWidget:multiRoleBox objectHook="${targetBean.hook}" roleName="targetGroups" title="Target Groups" />

<jsp:include page="/JSP/spot/TargetGroups.jsp"></jsp:include>

<c:set var="newDBRef">
      <c:choose>
        <c:when test="${mayUpdate}">
           <a href='${pageContext.request.contextPath}/ChooseForCreate/org.pimslims.model.core.ExternalDbLink/dbName?search_all=&amp;SUBMIT=Quick+Search&amp;ACTION=parentEntry%3D${targetBean.hook}'>Add New</a>
        </c:when>
        <c:otherwise>
        </c:otherwise>
      </c:choose>
</c:set>
    
<pimsWidget:externalDbLinks bean="${targetBean}" dbnames="${dbnames}"/>

<%-- searches --%>
<c:set var="fasta">
<%-- this looks wierd but it is needed because we have enabled trimSpaces for JspServlet --%>
>${targetBean.name}<%="\n" %>
${targetBean.protSeq}
</c:set >
<c:if test="${!empty (targetBean.protSeq)}">
<pimsWidget:box id="blast" title="Database queries" initialState="closed" >
  <pimsForm:formBlock id="notInForm" >
  <br />
  <h3 class="plainHead">Blast searches:</h3>
	<a href="${pageContext.request.contextPath}/read/Blast/PDBBlast?blast=Run%20PDB%20Blast&amp;target_protSeq=<c:out value='${targetBean.protSeq}'/>&amp;target_protName=<c:out value='${targetBean.protein_name}'/>&amp;target_hook=<c:out value='${targetBean.hook}' />">PDB Blast results</a>
	&nbsp;&nbsp;&nbsp;
	<a href="${pageContext.request.contextPath}/read/Blast/PDBBlast?blast=Run%20TargetDB%20Blast&amp;target_protSeq=<c:out value='${targetBean.protSeq}'/>&amp;target_protName=<c:out value='${targetBean.protein_name}'/>&amp;target_hook=<c:out value='${targetBean.hook}' />">TargetDB Blast results</a>
	&nbsp;&nbsp;&nbsp;
  </pimsForm:formBlock>
   
<%--Original version
	<a href="http://www.compbio.dundee.ac.uk/taro/cgi-taro/v3_targpipe_home.pl?guestlogin=guest">TarO results</a>
    <br /><br />
    <h3>New TarO Inquiry. Note that "guest" inquiries are public, and are saved for one week.</h3>
    TarO user name: <input  name='user' size='15' value='guest'>
    Max. number of sequences for MSA           
    <input type='text' name='msaNum' maxlength='5' size='15' value='100'><br />
    Query Description:                     
    <input type='text' name='function' size='30' maxlength='30' value="PiMS: ${targetBean.name} "> <br />

    <textarea rows='22' name='fastatextarea' cols='90' readonly=readonly >
    ${fasta}
    </textarea>

    <input type='submit' value='Submit' name='B1'>
--%>
  <%--New version by Susy 11-10-10 --%>
  <form method='post' action='http://www.compbio.dundee.ac.uk/taro/cgi-taro/v3_targpipe_save.pl' target="TarO" ENCTYPE='multipart/form-data'>
   <hr />
   <pimsForm:formBlock id="blk5">
   <h3 class="plainHead">TarO: Target Optimisation Utility</h3> <span style="font-size:small;"><em>Developed and Hosted by the <a href="http://nar.oxfordjournals.org/content/36/suppl_2/W190.abstract">Barton Group</a> University of Dundee</em></span>
   <br /><br />
   </pimsForm:formBlock>
   <pimsForm:formBlock id="blk6">
    <pimsForm:column1>
        <h3 class="plainHead">New TarO query:</h3>
        <p><span class="nsfieldname" title="User name for the TarO query, default is guest">User name:</span> <span class="nsformfield"><input  name='user' size='25' value='guest'></span><br /></p>
        <p><span class="nsfieldname"  title="Name for the TarO query to locate results on the server">Query Description:</span> <span class="nsformfield"><input type='text' name='function' size='25' maxlength='20' value="PiMS: ${targetBean.name} "></span><br /></p>
        <p><span class="nsfieldname"  title="Number of sequences for TarO Multiple sequence alignment, default 100">Number of sequences for MSA:</span> <span class="nsformfield"><input type='text' name='msaNum' maxlength='5' size='25' value='100'></span><br /></p>
    </pimsForm:column1>
    <pimsForm:column2>
            <p>View Existing <a href="http://www.compbio.dundee.ac.uk/taro/cgi-taro/v3_targpipe_home.pl?guestlogin=guest">TarO results</a></p>
    </pimsForm:column2>
    </pimsForm:formBlock>
    <pimsForm:formBlock id="blk7">
     <textarea rows='22' name='fastatextarea' cols='90' readonly=readonly >${fasta}</textarea>
    </pimsForm:formBlock>
      <br /><span style="font-size:small; padding-left: 2.15em;" class="pagelinks"><strong>Note:</strong> Results of TarO queries with User name &quot;guest&quot; are visible to everyone and are saved on the TarO server for 8 days</span>    
      
    <pimsForm:formBlock id="blk8">
        <div style="text-align:right">
            <input name="B1" type="submit" value="Submit Taro query" />
        </div><br />
     </pimsForm:formBlock>
  </form>
</pimsWidget:box>
</c:if>

<pimsWidget:files bean="${targetBean}"  />

<pimsWidget:notes bean="${targetBean}"  />

<form style="display: none;" id="addDNA" action="${pageContext.request.contextPath}/CreateDNA/${targetBean.pimsTargetHook}?createDNA=true" method="post" >
    <%-- TODO CSRF token --%></form>

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
