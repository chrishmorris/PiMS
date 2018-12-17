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
<jsp:useBean id="f_extensions" scope="request" type="java.util.List<ExtensionBean>" />
<jsp:useBean id="r_extensions" scope="request" type="java.util.List<ExtensionBean>" />

<%--
spot/SpotNewConstructWizardStep3c.jsp
Form for user to select extensions and enter N- and C-term protein sequences
Modified from SpotNewConstructWizardStep2a
Re-design by Susy Griffiths November 2008
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New construct: Choose Primer Extensions' />
</jsp:include>

<!-- SpotNewConstructWizardStep3c.jsp -->
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
 : New Construct ${constructBean.name}
</c:set>

<pimsWidget:pageTitle
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="Step 3: Choose Primer Extensions"
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
		<h3 class="plainHead">Construct DNA sequence:</h3>
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
		<h3 class="plainHead">Construct Protein sequence:</h3>
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

<pimsWidget:box id="box3" title="Selected Primers" initialState="open" >
<table><c:forEach var="bean" items="${constructBean.primers}" >    
    
    <tr><th title="${bean.formFieldsDirection} Sequence">${bean.formFieldsDirection} Sequence</th>
     <td colspan="4"><span class="sequence"><pims:sequence sequence="${bean.sequence}" format='DEFAULT' escapeStyle="TEXT" /></span>
     </td></tr>
     <tr><th><a class="popup" href="#" onclick="popUp('${pageContext.request.contextPath}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${constructBean.name}+${bean.formFieldsDirection}" />&amp;seq=<c:out value="${bean.sequence}" />');return false">
         Fasta pop-up</a>
     </th>     
     <td><strong>Length: </strong><c:out value="${bean.length}" /></td>     
     <td title="Tm of primer"><strong>Tm &deg;C: </strong>     
      <c:choose>
        <c:when test="${empty bean.tmfullasFloat}">&nbsp;</c:when><c:otherwise> 
        <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.tmfullasFloat}"/></fmt:formatNumber>
      </c:otherwise></c:choose></td>      
     <td colspan="2" title="&#037; of G and C nucleotides in Primer"><strong>&#037;GC: </strong>
      <c:choose>
         <c:when test="${empty bean.gcFull}">&nbsp;</c:when><c:otherwise>
         <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.gcFull}"/></fmt:formatNumber>
      </c:otherwise></c:choose></td></tr>        
</c:forEach></table>
</pimsWidget:box>

<pimsWidget:box id="box4" title="Extensions and Tags" initialState="open" >
 <pimsForm:form action="/spot/SpotNewConstructWizard" id="frmStep3c" method="post" mode="create" >
  <input type="hidden" name="wizard_step" value="Save" />
  <pimsForm:formBlock id="b4blk1">  
   <pimsForm:column1>
   		<h3 class="plainHead">Start and Stop codons:</h3>
   		<br /><br />
		<%--Code to check if region to be amplified starts with ATG pass checkbox status to servlet--%>
		<c:if test="${!fn:startsWith(constructBean.dnaSeq, 'ATG')}">
			<c:set var="fp" value="${constructBean.fwdPrimer}" scope="page" />
			<pimsForm:checkbox name="check1" label="Add ATG (start codon)?" isChecked="false" onclick="toggleATG('${fp}');" />
		</c:if>
		<c:if test="${fn:startsWith(constructBean.dnaSeq, 'ATG')}">
			<c:out value="Forward primer starts with ATG" /><br /><br />
		</c:if>

		<%--Code to check if region to be amplified ends with stop codon --%>
		<c:set var="seqEnd" value="${fn:substring(constructBean.dnaSeq, dSeqLen-3, dSeqLen)}" />
		<c:if test="${('TGA'!=seqEnd)&&('TAA'!=seqEnd)&&('TAG'!=seqEnd)}" >
			<c:set var="rp" value="${constructBean.revPrimer}" scope="page" />
		   		<pimsForm:radioSet name="add_stop" alias="Add stop codon?" labelValuePairs="${radioStopMap}" checkedValue="" />
		</c:if>
		<c:if test="${('TGA'==seqEnd)||('TAA'==seqEnd)||('TAG'==seqEnd)}" >
			<c:out value="Reverse  primer ends with ${seqEnd} stop codon" />
			<br /><br />
		</c:if>

   </pimsForm:column1>
   <pimsForm:column2>
		<h3 class="plainHead">Protein tag details:</h3>
		<br /><br />
   		<pimsForm:text validation="proteinSequence:true" value=""  name="expressed_prot_n" alias="N-term -expressed" helpText="Vector- or Primer-encoded N-term of expressed protein. e.g. His-tag sequence" onchange="this.value=cleanSequence(this.value);"/>
   		<pimsForm:text validation="proteinSequence:true" value=""  name="final_prot_n" alias="N-term -final" helpText="Sequence of N-terminus of the final protein, e.g. after tag cleavage" onchange="this.value=cleanSequence(this.value);"/>
   		<pimsForm:text validation="proteinSequence:true" value=""  name="expressed_prot_c" alias="C-term -expressed" helpText="Vector- or Primer-encoded C-term of expressed protein. e.g. His-tag sequence" onchange="this.value=cleanSequence(this.value);"/>
   		<pimsForm:text validation="proteinSequence:true" value=""  name="final_prot_c" alias="C-term -final" helpText="Sequence of C-terminus of the final protein, e.g. after tag cleavage" onchange="this.value=cleanSequence(this.value);"/>
		
   </pimsForm:column2>
   </pimsForm:formBlock>
   
   <pimsForm:formBlock id="b4blk2">
   <h3 class="plainHead">Primer 5'-Extensions:</h3>
   <span style="display:inline"><a href="${pageContext.request.contextPath}/ExtensionsList">Extensions List</a></span>
    <table>
    <tr><th colspan="2"></tr>
  	<tr><th><label for='forward_extension' title='DNA sequence of tag or extension for the Forward primer'>&nbsp;F.Primer 5'-extension</label></th>
	<td><select name="forward_extension" id="forward_extension" size="1" style="width:400px" onchange="newExt('${newExt }'); populateTags(this)" >
 			<option class="none" value="">none</option>
            <option class="none" value="New Extension">*Record a new 5'-extension*</option>
            <c:forEach var="f_extension" items="${f_extensions}">
                <%--230909 hack: have to use class attribute because IE doesn't support event handlers for the option tag --%>
                <option class="<c:out value='${f_extension.encodedTag}'/>" value="<c:out value='${f_extension.exSeq}'/>" ><c:out value="${f_extension.exName}"/>: <c:out value="${utils:truncate(f_extension.exSeq,34)}"/></option>
            </c:forEach>
		</select>
		</td></tr>
   <%--Code to allow user to record a new forward extension--%>  
    <tr class="addNew" id="fexName"><th><label for='new_fExtName' title="Name of New 5'-Extension">Name<span class="required">*</span></label></th>
	<td><input name='new_fExtName' id='new_fExtName' type='text' title="Name -required" value='' disabled="disabled" onblur="ajax_exists(this,'org.pimslims.model.molecule.Substance','name')" /></td></tr>
	<tr class="addNew" id="fexSeq"><th><label for='new_fExtSeq' title="Sequence of New 5'-Extension">Sequence<span class="required">*</span></label></th>
	<td><input name='new_fExtSeq' id='new_fExtSeq' type='text' title="DNA sequence -required" value='' disabled="disabled" onblur="this.value=cleanDnaSequence(this.value);" /></td></tr>

   <%--300909 Code to record optional related protein tag and restriction enzyme--%>		
    <tr class="addNew" id="fexTag"><th><label for='new_fExtTag' title="Related N-terminal protein Tag">N-term protein Tag</label></th>
    <td><input name='new_fExtTag' id='new_fExtTag' type='text' title="N-term protein Tag sequence -optional. Protein sequence characters, | and / allowed" 
    value='' disabled="disabled" onblur="this.value=extTagCheck(this.value);" onchange="populateTags2(this)"/></td></tr>
    <tr class="addNew" id="fexREnz"><th><label for='new_fExtEnz' title="Restriction enzyme site in Extension">Restriction site</label></th>
    <td><input name='new_fExtEnz' id='new_fExtEnz' type='text' title="Restriction enzyme site -optional" value='' disabled="disabled" /></td></tr>

    <tr><th><label for='reverse_extension' title='DNA sequence of tag or extension for the Reverse primer'>&nbsp;R.Primer 5'-extension</label></th>
    <td><select name="reverse_extension" id="reverse_extension" size="1" style="width:400px" onchange="newExt('${newExt }'); populateTags(this)" >
 			<option class="none" value="">none</option>
            <option class="none" value="New Extension">*Record a new 5'-extension*</option>
            <c:forEach var="r_extension" items="${r_extensions}">
                <%--230909 hack: have to use class attribute because IE doesn't support event handlers for the option tag --%>
               <option class="<c:out value='${r_extension.encodedTag}'/>" value="<c:out value='${r_extension.exSeq}'/>" ><c:out value="${r_extension.exName}"/>: <c:out value="${utils:truncate(r_extension.exSeq,34)}" /></option>
            </c:forEach>
		</select></td></tr>
   <%--Code to allow user to record a new reverse extension--%>  
	<tr class="addNew" id="rexName"><th><label for='new_rExtName' title="Name of New 5'-Extension">Name<span class="required">*</span></label></th>
	<td><input name='new_rExtName' id='new_rExtName' type='text' title="Name -required" value='' disabled="disabled" onblur="ajax_exists(this,'org.pimslims.model.molecule.Substance','name')" /></td></tr>
	<tr class="addNew" id="rexSeq"><th><label for='new_rExtSeq' title="Sequence of New 5'-Extension">Sequence<span class="required">*</span></label></th>
	<td><input name='new_rExtSeq' id='new_rExtSeq' type='text' title="DNA sequence -required" value='' disabled="disabled" onblur="this.value=cleanDnaSequence(this.value);" /></td></tr>

   <%--031009 Code to record optional related protein tag and restriction enzyme--%>      
    <tr class="addNew" id="rexTag"><th><label for='new_rExtTag' title="Related C-terminal protein Tag">C-term protein Tag</label></th>
    <td><input name='new_rExtTag' id='new_rExtTag' type='text' title="C-term protein Tag sequence -optional. Protein sequence characters, | and / allowed" 
    value='' disabled="disabled" onblur="this.value=extTagCheck(this.value);" onchange="populateTags2(this)"/></td></tr>
    <tr class="addNew" id="rexREnz"><th><label for='new_rExtEnz' title="Restriction enzyme site in Extension">Restriction site</label></th>
    <td><input name='new_rExtEnz' id='new_rExtEnz' type='text' title="Restriction enzyme site -optional" value='' disabled="disabled" /></td></tr>

   <tr><td colspan="2">
   
   <c:forEach var="target" items="${constructBean.targetBeans}" >
    <input type="hidden"  name="pims_target_hook" value="<c:out value='${target.hook}' />"/>
    <input type="hidden"  name="target_dna_seq"  value="<c:out value='${target.dnaSeq}' />"/>
  </c:forEach>
	<input type="hidden" name="construct_id" value="<c:out value='${constructBean.name}' />"/>
	<input type="hidden" name="target_prot_start" value="<c:out value='${constructBean.targetProtStart}' />"/>
	<input type="hidden" name="target_prot_end" value="<c:out value='${constructBean.targetProtEnd}' />"/>
	<input type="hidden" name="desired_tm" value="<c:out value='${constructBean.desiredTm}' />"/>
	<input type="hidden" id="dnaSeq" name="dnaSeq" value="<c:out value='${constructBean.dnaSeq }' />"/>
	<input type="hidden" id="forward_primer" name="forward_primer" value="<c:out value='${constructBean.fwdPrimer}' />"/>
	<input type="hidden" id="reverse_primer" name="reverse_primer" value="<c:out value='${constructBean.revPrimer}' />"/>
	<input type="hidden" id="fixNonstandardStartCodon" name="fixNonstandardStartCodon" value="<c:out value='${fixNonstandardStartCodon}' />"/>
	<input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>		    
    <input type="hidden" name="sampleHook" value="${sampleHook}"/>
	</td></tr>
  </table>
  
 <script type="text/javascript">
   attachValidation("new_fExtSeq", {required:true,dnaSequence:true, alias:"Forward 5'-extension sequence"});
   attachValidation("new_fExtName", {required:true,custom:function(val,alias){return extNameCheck(val,alias)}, alias:"Forward 5'-extension name"});
   attachValidation("new_rExtSeq", {required:true,dnaSequence:true, alias:"Reverse 5'-extension sequence"});
   attachValidation("new_rExtName", {required:true,custom:function(val,alias){return extNameCheck(val,alias)}, alias:"Reverse 5'-extension name"});
 </script>  
 
  </pimsForm:formBlock>
  <br />
  <pimsForm:formBlock id="b4blk3">
   	<div style="text-align:right">
      <input type="button" style="float:left" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
      <input name="Submit" type="submit" value="Save" onclick="dontWarn()" />
    </div>
   </pimsForm:formBlock>

</pimsForm:form>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>


<jsp:include page="/JSP/core/Footer.jsp" />


