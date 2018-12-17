<%--
Display a list of a model objects.

@param results - the collection (ArrayList) of ExperimentBean to display
@requestParam listMetaClass MetaClass the model type being listed

Author: Marc Savitsky
Date: August 2007
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@page import="org.pimslims.presentation.experiment.ExperimentBean"%>
obsolete
<c:catch>
<script type="text/javascript">
function toggleAllCheckboxes(checkAllBoxElement){
	var isChecked = checkAllBoxElement.checked;
	var parentTable = Element.extend(checkAllBoxElement).up("table");
	<%--alert (parentTable);--%>
	var boxes=parentTable.getElementsByClassName("behaviour_selectAll");
	
	for(i=0;i<boxes.length; i++){
		boxes[i].checked=isChecked;
	}
	return true;
}
</script>
<%--Caller must provide this --%>
<jsp:useBean id="results" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" />

<c:choose>
	<c:when test="${empty results}">none</c:when> 
	<c:otherwise>
    <display:table class="list" id="mytable" name="${results}"  sort="list" pagesize="${pagesize}" partialList="true" size="${resultSize}">

     <display:column escapeXml="false"   headerClass="sortable" title="<input name='checkAll' id='checkAll' onclick='toggleAllCheckboxes(this)' type='checkbox'/> " media="html">
		       <input name="${mytable.experimentHook}" class="behaviour_selectAll"	type="checkbox" />
  	</display:column>

    <display:column escapeXml="false"  style="width:20px;" media="html" sortable="true" sortProperty="name" title="Name" >
		<pimsWidget:link bean="${mytable}" />
	</display:column>
	
   <display:column escapeXml="true"  property="creator" sortable="true" headerClass="sortable" title="Creator"/>
   <display:column escapeXml="true"  property="experimentTypeName" sortable="true" headerClass="sortable" title="Experiment Type"/>
   <display:column escapeXml="true"  property="status" sortable="true" headerClass="sortable" title="Status"/>
   <display:column escapeXml="true"  property="bluePrint" sortable="true" headerClass="sortable" title="Project"/>
   <display:column escapeXml="true"  property="startDateOfExperimentString" sortable="true" headerClass="sortable" title="Start Date"/>
   <display:column escapeXml="true"  property="endDateOfExperimentString" sortable="true" headerClass="sortable" title="End Date"/>
   <display:column escapeXml="true"  property="details" sortable="true" headerClass="sortable" title="Details"/>

    
 	
  	</display:table>

</c:otherwise></c:choose>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /List_Experiments.jsp -->
