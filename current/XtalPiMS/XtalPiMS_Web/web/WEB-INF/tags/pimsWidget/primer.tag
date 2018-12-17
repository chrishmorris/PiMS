<%@tag   %>
<!-- primer.tag -->
<%@attribute name="initialState" required="false"  %>
<%@attribute name="bean" required="true" type="org.pimslims.presentation.PrimerBean" %> 
<%@attribute name="sdm" required="false"  %> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<pims:import className="org.pimslims.model.molecule.Primer" />
<c:set var="title">
<c:choose>
    <c:when test="${sdm == true}">
    	 <c:out value="${bean.formFieldsSDMDirection}: ${bean.name}"/>
    </c:when>
    <c:otherwise>
         <c:out  value="${bean.formFieldsDirection}: ${bean.name}"/>
    </c:otherwise>
</c:choose>
</c:set>
<%-- TODO escape bean name--%>
<%--Susy form tags --%>
<pimsWidget:box  title="${title}" initialState="${initialState}" >
  <pimsForm:form id="tabsForm" action="/Update" mode="view" method="post">
<%--Full length --%>
    <pimsForm:formBlock id="blk1">
        <pimsForm:textarea extraClasses="sequence" name="${bean.primerHook}:${Primer['PROP_SEQSTRING']}" alias="Full sequence" id="PrimerSequence" helpText="The full length primer sequence" validation="dnaSequence:true" onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${bean.sequence}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    <div class="formitem" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (bp) of the Primer" > Length: </strong><c:out value="${bean.length}" />
        &nbsp; <strong title="Tm of the primer"> Tm &deg;C: </strong>
        <c:choose>
            <c:when test="${empty bean.tmfullasFloat}">&nbsp;</c:when>
            <c:otherwise><fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.tmfullasFloat}"/></fmt:formatNumber>
            </c:otherwise>
        </c:choose>
        &nbsp; <strong title="&#037; of G and C nucleotides in Primer"> &#037;GC: </strong>
        <c:choose>
            <c:when test="${empty bean.gcFull}">&nbsp;</c:when>
            <c:otherwise>
             <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.gcFull}"/></fmt:formatNumber>
            </c:otherwise>
            </c:choose>
        &nbsp; <strong title="Molecular Weight">Molecular Mass: </strong> 
                <fmt:formatNumber type="Number" maxFractionDigits="0" ><c:out value="${bean.molecularMass}"/></fmt:formatNumber>    
        &nbsp; <a class="popup" href="#" onclick="widePopUp('../JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${constructBean.name}+${bean.formFieldsDirection}" />&amp;seq=<c:out value="${bean.sequence}" />');return false">Fasta pop-up</a>
      </span>
      <hr />
    </div>    
    </pimsForm:formBlock>
    <c:if test="${empty sdm || sdm == false}">
    
    <pimsForm:formBlock id="blk2">
<%--Overlap --%>
        <pimsForm:nonFormFieldInfo label ="Overlap region" helpText="${bean.formFieldsDirection} gene-specific nucleotides" >
            <a class="popup" href="#" onclick="popUp('${pageContext.request['contextPath']}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${constructBean.name}" /> ${bean.formFieldsDirection} Overlap&amp;seq=<c:out value="${bean.overlap}" />');return false">
                <pims:sequence sequence="${bean.overlap}" format='DEFAULT' escapeStyle="TEXT" /></a>
        </pimsForm:nonFormFieldInfo>
    </pimsForm:formBlock>
    <pimsForm:formBlock id="blk3">
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="${bean.formFieldsDirection} gene-specific nucleotides"> Length: </strong>
        <c:choose>
            <c:when test="${empty bean.overlapLength}">&nbsp;</c:when>
            <c:otherwise><c:out value="${bean.overlapLength}" />
            </c:otherwise></c:choose>        
        &nbsp; <strong  title="Tm of Overlap (gene-specific) sequence"> Tm &deg;C: </strong> 
        <c:choose><c:when test="${empty bean.tmGeneFloat}">&nbsp;</c:when><c:otherwise>
            <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.tmGeneFloat}"/></fmt:formatNumber>
        </c:otherwise></c:choose>
        &nbsp; <strong title="&#037; of G and C nucleotides in Primer">&#037;GC: </strong>
        <c:choose>
         <c:when test="${empty bean.gcGeneFloat}">&nbsp;</c:when><c:otherwise>
         <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.gcGeneFloat}"/></fmt:formatNumber>
      </c:otherwise></c:choose> 
      </span>
      <hr />
    </div>
    </pimsForm:formBlock>
    <pimsForm:formBlock id="blk4">
<%--Extension --%>
        <pimsForm:nonFormFieldInfo label="5'-Extension" helpText="5'-Extension for the ${bean.formFieldsDirection}" >
            <a class="popup" href="#" onclick="popUp('${pageContext.request['contextPath']}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${constructBean.name}" />&amp;seq=<c:out value="${bean.extension}" />');return false">
                <pims:sequence sequence="${bean.extension}" format='DEFAULT' escapeStyle="TEXT" /></a>
        </pimsForm:nonFormFieldInfo>
    </pimsForm:formBlock>
 <%--Leeds table --%>
    <pimsForm:formBlock id="blk5">
         <c:if test="${!empty bean.locationName || !empty bean.boxName || !empty bean.position || !empty bean.restrictionsite || !empty bean.seller || !empty bean.tmSeller || !empty bean.particularity || !empty bean.concentration}">
            <table>
                <tr><td colspan="5">&nbsp;</td></tr>        
                <tr><th></th><td><strong>Storage Location: </strong> 
                 <c:choose>
                    <c:when test="${empty bean.locationName}">&nbsp;</c:when>
                    <c:otherwise>
                    <c:out value="${bean.locationName}" /></c:otherwise></c:choose></td>
                <td title="Tm of gene-specific sequence"><strong>Box: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.boxName}">&nbsp;</c:when>
                        <c:otherwise><c:out value="${bean.boxName}" />
                        </c:otherwise></c:choose></td>
                <td colspan="2" title="&#037; of G and C nucleotides"><strong>Position: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.position}">&nbsp;</c:when>
                        <c:otherwise><c:out value="${bean.position}"/></c:otherwise></c:choose></td></tr>
                <tr><th></th><td colspan="4" ><strong>Restriction site: </strong> 
                    <c:out value="${bean.restrictionsite}" /></td></tr>
                <tr><th></th><td title="Tm of gene-specific sequence"><strong>Seller: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.seller}">&nbsp;</c:when>
                        <c:otherwise><c:out value="${bean.seller}" /></c:otherwise></c:choose></td>
                <td colspan="3" title="Tm of gene-specific sequence"><strong>Tm &deg;C: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.tmSeller}">&nbsp;</c:when>
                        <c:otherwise><c:out value="${bean.tmSeller}"/></c:otherwise></c:choose></td></tr>
                <tr><th></th>
                <td><strong>Grade: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.particularity}">&nbsp;</c:when>
                        <c:otherwise><c:out value="${bean.particularity}" /></c:otherwise></c:choose></td>            
                <td colspan="3" title="Tm of gene-specific sequence"><strong>Concentration: </strong> 
                    <c:choose>
                        <c:when test="${empty bean.concentration}">&nbsp;</c:when><c:otherwise>
                           <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${bean.concentration}"/></fmt:formatNumber>mM
                        </c:otherwise></c:choose></td></tr>
            </table>
         </c:if><%--End Leeds --%>
    </pimsForm:formBlock>
    </c:if><%--End not sdm --%>
    <pimsForm:editSubmit />
    
  </pimsForm:form>
</pimsWidget:box>
<%--End Susy form tags --%>

<!-- /primer.tag -->