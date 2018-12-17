<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@page import="org.pimslims.presentation.ResearchObjectiveElementBeanI,org.pimslims.model.target.ResearchObjective"%>
<pims:import className="org.pimslims.model.target.ResearchObjective" />
<jsp:useBean id="complexbean" scope="request" type="org.pimslims.presentation.ComplexBean" />
<jsp:useBean id="mrus" scope="request" type="java.util.Collection" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='View Complex: ${complexbean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- TODO develop this into a view for a ResearchObjective, converge with construct view --%>

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


<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/BrowseComplex">Complexes</a></c:set>
<c:set var="icon" value="complex.png" />        

<c:set var="title"><c:out value="${complexbean.name}" /></c:set>
<c:set var="actions"><pimsWidget:diagramLink hook="${complexbean.blueprintHook}" /></c:set>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" actions="${actions}" />


<pimsWidget:box id="box1" title="Basic Details" initialState="open" >
	<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
	
	<pimsForm:formBlock id="blk1">
		<pimsForm:text validation="required:true" value="${complexbean.name}"  name="${complexbean.blueprintHook}:${ResearchObjective['PROP_COMMONNAME']}" alias="Name" helpText="The name of the Complex" />
      <pimsForm:textarea name="${complexbean.blueprintHook}:${ResearchObjective['PROP_WHYCHOSEN']}" alias="Why Chosen" 
      helpText="Comments e.g. Why Target was selected" >
      <c:out value="${complexbean.whyChosen}" />
      </pimsForm:textarea>
      <pimsForm:textarea name="${complexbean.blueprintHook}:${ResearchObjective['PROP_DETAILS']}" alias="Details" helpText="The Function Description of the Target">
   	  <c:out value="${complexbean.details}" />
   	  </pimsForm:textarea>	
		<pimsForm:editSubmit />
	</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>


<%-- copy of multiRoleBox used below --%>
<c:set var="mayAdd" value="${mayUpdate}" />
<c:set var="boxHeader" scope="page">
    <c:if test="${mayAdd}">
        <a onclick="startMultiRoleEdit(this,'${pageContext.request.contextPath}/update/EditComplexRole/${complexbean.blueprintHook}/researchObjectiveElements?isInModalWindow=true&selectMultiple=true&componentType=target');return false;" 
          href="${pageContext.request.contextPath}/EditComplexRole/${complexbean.blueprintHook}/researchObjectiveElements">
          Search/Add</a>
        &nbsp;
    </c:if>
</c:set>  
<pimsWidget:box title="Components" 
    src="${pageContext.request['contextPath']}/read/ListRole/${complexbean.blueprintHook}/researchObjectiveElements" 
    extraHeader="${boxHeader}"
    id="${complexbean.blueprintHook}_researchObjectiveElements"
/>

</div> 


             
<pimsWidget:multiRoleBox title="Workflows" 
    objectHook="${complexbean.blueprintHook}" roleName="workflows"/> 

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error> </c:if>
    
<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
