<%--
Show the experiment record
--%>
<!-- experimentTabs.jsp -->
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>

<%-- Standard PiMS objects for page --%>
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment">Experiments</a>	
	 : <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment?experimentType=${bean.experimentTypeHook}">${bean.experimentTypeName} Experiments</a>
	 <c:if test="${!empty experimentGroup}">
	 : <pimsWidget:link bean="${experimentGroup}" />
    </c:if>
</c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="${bean.name}"/>

<c:set var="oss" value="" />
<c:forEach  var="output" items="${outputsamples}" >
    <c:set var="oss" value="${oss}&amp;hook=${output.sampleHook}" />
</c:forEach>


<c:set var="submitaction">/Update/${bean.hook}</c:set>
<form id="dummy_unlockform" method="post" style="display:none"
    action="${pageContext.request.contextPath}${submitaction}" >
    <%-- 
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,submitaction)}" /> --%>
    <input type="hidden" name="${bean.hook}:isLocked" value="No" />
</form>
<c:set var="actions">
	<pimsWidget:diagramLink hook="${bean.hook}"/>
	<pimsWidget:copyLink bean="${bean}" />
	<%-- custom deleteLink --%>
	<c:choose><c:when test="${bean.mayDelete}">
    <span class="linkwithicon " title="Delete experiment and output samples"><a  href="${pageContext.request.contextPath}/Delete?hook=${bean.hook}${oss}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request.contextPath}/Delete?hook=${bean.hook}${oss}">Delete</a></span>
    </c:when><c:otherwise><pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" /></c:otherwise></c:choose>
        
        <c:choose><c:when test="${'Yes' eq modelObject.values['isLocked'] && mayUnlock }">
            <a href="javascript:$('dummy_unlockform').submit();">Unlock</a>
        </c:when><c:when test="${'Yes' eq modelObject.values['isLocked'] }">
            <span title="You do not have permission to unlock pages in this Lab Notebook">Can't unlock</span>
        </c:when><c:otherwise>
            <span title="To lock this page, set status to OK or Failed">Not locked</span>
        </c:otherwise></c:choose>
        
        <c:choose>
        <c:when test="${'Plasmid stock' == experimentType.name}" >
                <pimsWidget:linkWithIcon 
                    icon="types/small/blank.gif" 
                    url="${pageContext.request.contextPath}/read/PlasmidStockList"
                    text="Plasmid Stock list"/>
                <pimsWidget:linkWithIcon 
                    icon="misc/help.gif" 
                    url="${pageContext.request.contextPath}/help/experiment/HelpPlasmidStocks.jsp#newStock"
                    text="Help for Plasmid stocks"/>
        </c:when><c:when test="${'Cell stock' == experimentType.name}" >
                <pimsWidget:linkWithIcon 
                    icon="types/small/blank.gif" 
                    url="${pageContext.request.contextPath}/read/CellStockList"
                    text="Cell Stock list"/>
                <pimsWidget:linkWithIcon 
                    icon="misc/help.gif" 
                    url="${pageContext.request.contextPath}/help/experiment/HelpCellStocks.jsp#newStock"
                    text="Help for Cell stocks"/>
        </c:when><c:otherwise>
            <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp" />
        </c:otherwise>
        </c:choose>
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />



 
<pimsWidget:box title="Basic Details" initialState="open" >
   <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/experiment/attributes/${experimentType.name}.jsp" />
   </jsp:include>
</pimsWidget:box> 

<pimsWidget:box title="Conditions and Results" id="parameters" initialState="${'To_be_run' eq modelObject.values['status'] ? 'open' : 'closed' }" >

    <pimsForm:form id="tabsForm" action="/update/ExperimentUpdate" mode="view" method="post" boxToOpen="parameters">
    <div>
       <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/experiment/parameters/${protocolNameEscaped}.jsp" />
       </jsp:include>
    </div>
    </pimsForm:form>
</pimsWidget:box>

<pimsWidget:box title="Samples" id="samples" initialState="${'OK' eq modelObject.values['status'] ? 'open' : 'closed' }" >
       <%@include file="inputsamples.jsp"%>
       <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/experiment/outputs/${experimentType.name}.jsp" />
       </jsp:include> 
</pimsWidget:box>


<pimsWidget:files bean="${bean}"  />

<pimsWidget:notes bean="${bean}"  />

<pimsWidget:externalDbLinks bean="${bean}" dbnames="${dbnames}"/>

<script type="text/javascript">
/* override chooseSample in plateexperiment.js */
function samplePickerOnSelect(sampleObject){
  //alert("hi")
}
</script>

<!-- /experimentTabs.JSP -->
