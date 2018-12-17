<%--
Display a list of a model objects.


@param results - the collection (ArrayList) of PlateBean to display
@requestParam listMetaClass MetaClass the model type being listed

Author: Marc Savitsky
Date: August 2007
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib tagdir="/WEB-INF/tags/pimsWidget" prefix="pimsWidget"  %>
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
<jsp:useBean id="results" scope="request" type="java.util.Collection<org.pimslims.presentation.plateExperiment.PlateBean>" />
<!-- List_Plates.jsp -->
*************************************
<c:choose>
	<c:when test="${empty results}">none</c:when>
<c:otherwise> 
	
	<display:table class="list" id="mytable" name="${results}"  sort="list" pagesize="${pagesize}" partialList="true" size="${resultSize}">

    	<display:column escapeXml="false"  style="padding:2px 0 0 3px;width:20px;" media="html" sortable="true" sortProperty="name" title="Name" >
			<pimsWidget:link bean="${mytable}" />
		</display:column>
	
   		<display:column escapeXml="true"  property="expTypeName" sortable="true" headerClass="sortable" title="Experiment Type"/>
   		<display:column escapeXml="true"  property="holderTypeName" sortable="true" headerClass="sortable" title="Holder"/>
   		
   		<display:column escapeXml="true"  property="creator" sortable="true" headerClass="sortable" title="Creator"/>
   		<display:column escapeXml="true"  property="startDateString" sortable="true" headerClass="sortable" title="Start Date"/>
    
 		
  	</display:table>
	
</c:otherwise></c:choose>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /List_Plates.jsp -->

