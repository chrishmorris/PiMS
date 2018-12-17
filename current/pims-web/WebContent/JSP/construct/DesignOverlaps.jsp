<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@page import="org.pimslims.presentation.ResearchObjectiveElementBeanI,org.pimslims.model.target.ResearchObjective"%>
<pims:import className="org.pimslims.model.target.ResearchObjective" />
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Design Primer Overlaps for Construct: ${constructBean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- /construct/DesignExtensions.jsp -->

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

<c:if test="${! empty param.linkto}" >
	<c:set var="prm" value="?linkto=${param.linkto}" />
</c:if>


<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/search/org.pimslims.model.target.ResearchObjective">Virtual Constructs</a></c:set>
<c:set var="icon" value="construct.png" />        

<c:set var="title"><c:out value="${constructBean.name}" /></c:set>
<c:set var="actions"><pimsWidget:diagramLink hook="${constructBean.hook}" /></c:set>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" actions="${actions}" />


<pimsWidget:box id="box1" title="Basic Details" initialState="closed" >
	<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
	
	<pimsForm:formBlock id="blk1">
		<pimsForm:text validation="required:true" value="${constructBean.name}"  name="${constructBean.hook}:${ResearchObjective['PROP_COMMONNAME']}" alias="Name" helpText="The name of the Complex" />
      <pimsForm:textarea name="${constructBean.hook}:${ResearchObjective['PROP_WHYCHOSEN']}" alias="Why Chosen" 
      helpText="Comments e.g. Why Target was selected" >
      <c:out value="${constructBean.whyChosen}" />
      </pimsForm:textarea>
      <pimsForm:textarea name="${constructBean.hook}:${ResearchObjective['PROP_DETAILS']}" alias="Details" 
      helpText="The Function Description of the Target">
   	  <c:out value="${constructBean.values[ResearchObjective['PROP_DETAILS']]}" />
   	  </pimsForm:textarea>	
		<pimsForm:editSubmit />
	</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>


<%-- copy of multiRoleBox used below --%>
<c:set var="mayAdd" value="${mayUpdate}" />
<c:set var="boxHeader" scope="page">
    <c:if test="${mayAdd}">
        <a onclick="startMultiRoleEdit(this,'${pageContext.request.contextPath}/update/EditComplexRole/${constructBean.hook}/researchObjectiveElements?isInModalWindow=true&selectMultiple=true&componentType=target');return false;" 
          href="${pageContext.request.contextPath}/EditComplexRole/${constructBean.hook}/researchObjectiveElements">
          Search/Add</a>
        &nbsp;
    </c:if>
</c:set>  
<pimsWidget:box title="Components" 
    src="${pageContext.request['contextPath']}/read/ListRole/${constructBean.hook}/researchObjectiveElements" 
    extraHeader="${boxHeader}"
    id="${constructBean.hook}_researchObjectiveElements"
/>

</div> 


<pimsWidget:box id="box3" title="Suggested Primer Overlaps for Tm ${constructBean.desiredTm} &deg;C:" initialState="open" >
 <pimsForm:form action="/update/AddPrimers${requestScope['javax.servlet.forward.path_info']}" method="get" mode="create" >
    <pimsForm:formBlock id="b3blk1">
        <%--020209 Revised code to check first codon ATG, GTG or CTG are possible start codons --%>
            <c:if test="${!fn:startsWith(constructBean.dnaSeq, 'ATG')}">
                <c:choose>
                    <c:when test="${fn:startsWith(constructBean.dnaSeq, 'GTG')&& constructBean.targetProtStart==1}">
                        <c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
                        <pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting GTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
                    </c:when>
                    <c:when test="${fn:startsWith(constructBean.dnaSeq, 'TTG')&& constructBean.targetProtStart==1}">
                        <c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
                        <pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting TTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
                    </c:when>           
                    <c:when test="${fn:startsWith(constructBean.dnaSeq, 'CTG')&& constructBean.targetProtStart==1}">
                        <c:set var="fp2" value="${constructBean.dnaSeq}" scope="page" />
                        <pimsForm:checkbox name="fixNonstandardStartCodon" label="<strong>Replace starting CTG with ATG?</strong>" isChecked="false" onclick="changeStart('${fp2}');" />
                    </c:when>           
                </c:choose>
            </c:if>
            <br />Enter a new Tm value below if the primers are not suitable:<br /><br />
    </pimsForm:formBlock>
    <pimsForm:formBlock id="b3blk2">
        <pimsForm:column1>
                <pimsForm:text validation="required:true, number:true, minimum:1" value="${constructBean.desiredTm}" name="desired_tm" alias="Primer design Tm" helpText="Tm for primer design" />
        </pimsForm:column1>
        <pimsForm:column2>
                    <input type="submit" value="Recalculate" onclick="dontWarn()" />
        </pimsForm:column2>
    </pimsForm:formBlock>
        
  </pimsForm:form>

  <pimsForm:form action="/update/AddPrimers${requestScope['javax.servlet.forward.path_info']}" method="get" mode="create" >
    <pimsForm:formBlock  >
          <jsp:include page="/JSP/spot/PrimerList.jsp" />
    </pimsForm:formBlock>
 </pimsForm:form>   
</pimsWidget:box>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error> </c:if>
    
<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
