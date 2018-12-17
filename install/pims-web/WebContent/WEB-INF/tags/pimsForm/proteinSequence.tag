<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="sequence" required="false"  %>
<%@attribute name="protMass" required="false"  %>
<%@attribute name="protEX" required="false"  %>
<%@attribute name="abs01pc" required="false" %>
<%@attribute name="protPI" required="false"  %>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<script type="text/javascript">
		    var ta = document.getElementById("ProteinSequence");
			matchTextareaHeightToContent(ta);
			ta.style.width="60em";
</script>

<%-- If there is no MolComponent DNA --%>
<c:choose>
<c:when test="${empty hook}">
  <c:if test="${mayUpdate}">
		<input type="button" style="float: left;" class="submit" onclick="if(!document.attributeChanged || confirm('Unsaved changes detected, they will be lost if you proceed. Proceed?')) $('addDNA').submit();" value="Add DNA sequence..." />
	</c:if>
</c:when>
<c:otherwise>
	<pimsForm:formItem extraClasses="textarea" name="ProteinSequence" alias="Protein sequence" validation="proteinSequence:true">
	<pimsForm:formItemLabel name="${hook}" alias="Protein sequence" helpText="The protein sequence of the Target" validation="proteinSequence:true" />
	<div class="formfield">
	    
		<div class="editdisplay">
	    <%-- Must all be on one line - newlines show up inside the textarea --%>
		<textarea  name="${hook}" id="ProteinSequence" onchange="this.value=cleanSequence(this.value);">
<pims:sequence sequence="${sequence}" format='SEQUENCE' escapeStyle="NONE" />
    	</textarea>
		</div>
		
		<div class="viewdisplay">
		<textarea  readonly="readonly" >
<pims:sequence sequence="${sequence}" format='SEQUENCE_WITH_RESIDUES' escapeStyle="NONE" />
    	</textarea>
		</div>

	</div>
	</pimsForm:formItem>
    
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (residues) of the Target protein sequence" > Length: </strong><c:out value="${fn:length(sequence)}"/>
        &nbsp; <strong title="Molecular weight (Da)"> Weight (Da): </strong><fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${protMass}" /></fmt:formatNumber>
        &nbsp; <strong title="Extinction coefficient cm-1 M-1"> Extinction: </strong><c:out value="${protEX}" />
        &nbsp; <strong title="Absorbance of a 0.1% solution (1g/L)"> Abs 0.1&#037;: </strong>
        <fmt:formatNumber type="Number" maxFractionDigits="3" ><c:out value="${abs01pc}" /></fmt:formatNumber>
        &nbsp; <strong title="Isoelectric point pI"> pI: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${protPI}" /></fmt:formatNumber> &nbsp;
        <%--TODO Using wrong popup see SpotTarget.  Needs tp know type of sequence--%>
        <br /><a class="popup" href="#" onclick="widePopUp('../JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${name}" />&amp;seq=<c:out value="${sequence}" />');return false">Fasta pop-up</a>
      </span>
    </div>
</c:otherwise>
</c:choose>