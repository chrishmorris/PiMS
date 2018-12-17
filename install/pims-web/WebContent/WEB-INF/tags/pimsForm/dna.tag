<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm"   tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" type="java.lang.String" required="false"  %>
<%@attribute name="validation" required="false"  %>

<pimsForm:textarea  extraClasses="sequence" name="${name}" 
alias="${alias }"   helpText="${helpText }" validation="dnaSequence:true, custom:function(val,alias){return checkTargetDNASeq(val,alias)}" onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${value}" format='DEFAULT' escapeStyle="NONE" />
</pimsForm:textarea>
 
<div class="formitem viewonly" style="padding-left:12.5em">
     <span align='right'>
        <strong title="Length (bp) of the DNA sequence" > Length: </strong><c:out value="${fn:length(value)}"/>
        &nbsp; <strong title="%GC nucleotides in the DNA sequence" > &#037;GC: </strong>
          <fmt:formatNumber type="Number" maxFractionDigits="2" ><%= new org.pimslims.lab.sequence.DnaSequence(value).getGCContent() %></fmt:formatNumber>

<c:if test="${!_MHTML }">
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${name}?type=targetDNA" />
            &nbsp; <a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    

        <c:if test="${! empty value}">
            <c:set var="rcurl" value="${pageContext.request.contextPath}/read/ReverseComplementPopup/${name}" />
            &nbsp; Reverse complement <a class="popup" href="javascript:widePopUp('${rcurl}')">Pop-up</a>    
        </c:if> 
</c:if>       
	 </span>
</div> 