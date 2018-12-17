<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<%@page import="org.pimslims.presentation.ResearchObjectiveElementBeanI,org.pimslims.model.target.ResearchObjective"%>
<pims:import className="org.pimslims.model.target.ResearchObjective" />
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Design Primers for Construct: ${constructBean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- /construct/DesignOverlaps.jsp -->

<script language="javascript" type="text/javascript">
function validate_Delete() {
   	if (document.forms.length < 5) {
    	alert('You must leave at least one component in the complex');
    	return false;
    }
}

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}
</script>


<c:catch var="error">




<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/search/org.pimslims.model.target.ResearchObjective">Virtual Constructs</a></c:set>
<c:set var="icon" value="construct.png" />        

<c:set var="title"><c:out value="${constructBean.name}" /></c:set>
<c:set var="actions"><pimsWidget:diagramLink hook="${constructBean.hook}" /></c:set>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" actions="${actions}" />


<pimsWidget:box id="box1" title="Basic Details" initialState="closed" >
	<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
	
	<pimsForm:formBlock id="blk1">
		<pimsForm:text validation="required:true" value="${constructBean.name}"  name="${constructBean.hook}:${ResearchObjective['PROP_COMMONNAME']}" alias="Name" helpText="The name of the Construct" />
      <pimsForm:textarea name="${constructBean.hook}:${ResearchObjective['PROP_WHYCHOSEN']}" alias="Why Chosen" 
      helpText="Comments e.g. Why Target was selected" >
      <c:out value="${constructBean.whyChosen}" />
      </pimsForm:textarea>
      <pimsForm:textarea name="${constructBean.hook}:${ResearchObjective['PROP_DETAILS']}" alias="Details" helpText="The Function Description of the Target">
   	  <c:out value="${constructBean.values[ResearchObjective['PROP_DETAILS']]}" />
   	  </pimsForm:textarea>	
		<pimsForm:editSubmit />
	</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>


<%-- copy of multiRoleBox used below --%>
<pimsWidget:box title="Components" 
    src="${pageContext.request['contextPath']}/read/ListRole/${constructBean.hook}/researchObjectiveElements" 
   id="${constructBean.hook}_researchObjectiveElements"
/>



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
 <pimsForm:form action="/update/AddPrimers${requestScope['javax.servlet.forward.path_info']}" id="frmStep3c" method="post" mode="create" >
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
  </c:forEach>
    <input type="hidden" id="forward_primer" name="forward_primer" value="<c:out value='${constructBean.fwdPrimer}' />"/>
    <input type="hidden" id="reverse_primer" name="reverse_primer" value="<c:out value='${constructBean.revPrimer}' />"/>
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
    <p class="error">error ${error}</p><error> </c:if>
    
<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
