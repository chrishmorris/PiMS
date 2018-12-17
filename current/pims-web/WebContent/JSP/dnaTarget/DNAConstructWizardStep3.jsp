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
DNAConstructWizardStep3.jsp
Author: susy
Date: 04-Mar-2008
Servlets:  /servlet/dnatarget/DNAConstructWizard.java
Updated for re-design 06-Nov-2008
--%>
<%-- bean declarations e.g.: --%>
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />
<jsp:useBean id="f_extensions" scope="request" type="java.util.List<org.pimslims.presentation.construct.ExtensionBean>" />
<jsp:useBean id="r_extensions" scope="request" type="java.util.List<org.pimslims.presentation.construct.ExtensionBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New DNA Construct: Step 3 of 4' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!--  DNAConstructWizardStep3.jsp -->
<%-- page body here --%>

<c:set var="breadcrumbs">
<a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach> : New Construct ${constructBean.name}
</c:set>

<pimsWidget:pageTitle
    icon="construct.png"
    breadcrumbs="${breadcrumbs}"
    title="Step 3: Choose Primer Extensions"
/>

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
   
<pimsWidget:box title="Selected Primers" initialState="open" >
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
    <c:set var="extensionsList">
            <a href="${pageContext.request.contextPath}/ExtensionsList">Extensions List</a>
    </c:set>

	<pimsWidget:box id="box4" title="Primer 5'-Extensions" extraHeader="${extensionsList}"initialState="open" >
	 <pimsForm:form action="/spot/SpotNewConstructWizard?wizard_step=Save" id="dnaConStep3" method="post" mode="create" >
  <table>
  <tr><th><label for='forward_extension' title='DNA sequence of tag or extension for the Forward primer'>&nbsp;F.Primer 5'-extension</label></th>
	<td>

	<select name="forward_extension" id="forward_extension" size="1" style="width:400px" onchange="newExt('${newExt }')" >
 			<option value="">none</option>
            <option value="New Extension">*Record a new 5'-extension*</option>
 			<c:forEach var="f_extension" items="${f_extensions}">
                <option value="<c:out value='${f_extension.exSeq}'/>" ><c:out value="${f_extension.exName}"/>: <c:out value="${utils:truncate(f_extension.exSeq,34)}"/></option>
 			</c:forEach>
		</select>
		</td></tr>

   <%--Code to allow user to record a new forward extension --%>  
	<tr class="addNew" id="fexName"><th><label for='new_fExtName' title="Name of New 5'-Extension">Name<span class="required">*</span></label></th>
	<td><input name='new_fExtName' id='new_fExtName' type='text' title="Name -required" value='' disabled="disabled" onblur="ajax_exists(this,'org.pimslims.model.molecule.Substance','name')" /></td></tr>
	<tr class="addNew" id="fexSeq"><th><label for='new_fExtSeq' title="Sequence of New 5'-Extension">Sequence<span class="required">*</span></label></th>
	<td><input name='new_fExtSeq' id='new_fExtSeq' type='text' title="DNA sequence -required" value='' disabled="disabled" onblur="this.value=cleanDnaSequence(this.value);" /></td></tr>
   <%--051009 Code to record optional restriction enzyme--%>        
    <tr class="addNew" id="fexREnz"><th><label for='new_fExtEnz' title="Restriction enzyme site in Extension">Restriction site</label></th>
    <td><input name='new_fExtEnz' id='new_fExtEnz' type='text' title="Restriction enzyme site -optional" value='' disabled="disabled" /></td></tr>
		
    <tr><th><label for='reverse_extension' title='DNA sequence of tag or extension for the Reverse primer'>&nbsp;R. Primer 5'-extension</label></th>
	<td><select name="reverse_extension" id="reverse_extension" size="1" style="width:400px" onchange="newExt('${newExt }')">
 			<option value="">none</option>
            <option value="New Extension">*Record a new 5'-extension*</option>
            <c:forEach var="r_extension" items="${r_extensions}">
                <option value="<c:out value='${r_extension.exSeq}'/>" ><c:out value="${r_extension.exName}"/>: <c:out value="${utils:truncate(r_extension.exSeq,34)}" /></option>
            </c:forEach>
		</select></td></tr>
   <%--Code to allow user to record a new reverse extension --%>  
	<tr class="addNew" id="rexName"><th><label for='new_rExtName' title="Name of New 5'-Extension">Name<span class="required">*</span></label></th>
	<td><input name='new_rExtName' id='new_rExtName' type='text' title="Name -required" value='' disabled="disabled" onblur="ajax_exists(this,'org.pimslims.model.molecule.Substance','name')" /></td></tr>
	<tr class="addNew" id="rexSeq"><th><label for='new_rExtSeq' title="Sequence of New 5'-Extension">Sequence<span class="required">*</span></label></th>
	<td><input name='new_rExtSeq' id='new_rExtSeq' type='text' title="DNA sequence -required" value='' disabled="disabled" onblur="this.value=cleanDnaSequence(this.value);" /></td></tr>
   <%--051009 Code to record optional restriction enzyme--%>        
    <tr class="addNew" id="rexREnz"><th><label for='new_rExtEnz' title="Restriction enzyme site in Extension">Restriction site</label></th>
    <td><input name='new_rExtEnz' id='new_rExtEnz' type='text' title="Restriction enzyme site -optional" value='' disabled="disabled" /></td></tr>

   <tr><td colspan="2">
    <input type="hidden" name="dna_target" value="<c:out value='${constructBean.dnaTarget}' />"/>
	<input type="hidden" name="pims_target_hook" value="<c:out value='${constructBean.targetHook}' />"/>
	<!-- <input type="hidden" name="target_name" value="<c:out value='${constructBean.targetName}' />"/> -->
	<input type="hidden" name="target_dna_seq" id="targetds" value="<c:out value='${constructBean.targetDnaSeq}' />"/>
	<input type="hidden" name="target_dna_start" value="<c:out value='${constructBean.targetDnaStart}' />"/>
	<input type="hidden" name="target_dna_end" value="<c:out value='${constructBean.targetDnaEnd}' />"/>
	<input type="hidden" name="construct_id" value="<c:out value='${constructBean.name}' />"/>
	<input type="hidden" name ="desired_tm" value="<c:out value='${constructBean.desiredTm}' />"/>
	<input type="hidden" name="forward_primer" value="<c:out value='${constructBean.fwdPrimer}' />"/>
	<input type="hidden" name="reverse_primer" value="<c:out value='${constructBean.revPrimer}' />"/>
	<input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
	<div style="text-align:right">
    <input type="button" style="float:left" value="&lt;&lt;&lt;Back" onClick="history.go(-1);return true;"/>
	&nbsp;
	<input name="Submit" type="submit" value="Save Construct" onclick="dontWarn()" />
	</div></td></tr>
  </table>
 
 
  <script type="text/javascript">
  function onLoadPims() {
	var i=0;
    <c:forEach var="ftag" items="${forward_tags}">  
	    str="${ftag.key}";
	    extNames[i]=str;
	    i++;
    </c:forEach>
    <c:forEach var="rtag" items="${reverse_tags}">  
	    str="${rtag.key}";
	    extNames[i]=str;
	    i++;
    </c:forEach>
  } 
  </script>
<%--</form>--%>
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
