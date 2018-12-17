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


 
<pimsForm:textarea extraClasses="sequence" name="${name}" alias="${alias }" helpText="${helpText }" validation="proteinSequence:true"
     onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${value}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    
    <c:set var="seq" value="<%= new org.pimslims.lab.sequence.ProteinSequence(value) %>" />
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (residues) of the Target protein sequence" > Length: </strong><c:out value="${fn:length(value)}"/>
        &nbsp; <strong title="Molecular weight (Da)"> Weight (Da): </strong><fmt:formatNumber type="Number" maxFractionDigits="1" >
            <c:out value="${seq.mass}" />
        </fmt:formatNumber>
        &nbsp; <strong title="Extinction coefficient cm-1 M-1, unreduced"> Extinction: </strong><c:out value="${seq.extinctionCoefficient}" />
        &nbsp; <strong title="Absorbance of a 0.1% solution (1g/L)"> Abs 0.1&#037;: </strong>
        <fmt:formatNumber type="Number" maxFractionDigits="3" ><c:out value="${seq.absPt1percent}" /></fmt:formatNumber>
        &nbsp; <strong title="Isoelectric point pI"> pI: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${seq['PI'] }" /></fmt:formatNumber> &nbsp;
      <c:if test="${!_MHTML }">
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${targetBean.hook}?type=targetProtein" />
        <br /><a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>
      </c:if>
      </span>
    </div>