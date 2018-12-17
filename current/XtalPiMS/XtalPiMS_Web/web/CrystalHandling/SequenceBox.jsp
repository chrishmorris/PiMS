<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Shows the sequence of the plate.
Author: Ed Daniel
Date: April 2014
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>

<c:set var="initialState" value="closed" />
<c:set var="wellUrl" value="${pageContext.request.contextPath}/update/SelectCrystal?barcode=${barcode}&well=${well}" />
<c:set var="cursor" value="auto" />

<pimsWidget:box title="Protein sequence" initialState="closed" id="sequencebox">
	<c:set var="formMode" value="view" />
	<c:if test="${empty crystal}"><c:set var="formMode" value="create" /></c:if>
    <pimsForm:form id="tabsForm" action="/update/SelectCrystal?barcode=${barcode}&well=${well}" mode="${formMode}" method="post">
    <input type="hidden" name="barcode" value="${barcode}" />
	<input type="hidden" name="well" value="${well}" />
	<input type="hidden" name="crystal" value="${crystal}" />
    <input type="hidden" name="acronym" value="<c:out value="${acronym}"/>" />
    <pimsForm:formBlock>
	
		<pimsForm:textarea name="proteinsequence" alias="Protein sequence" validation="proteinSequence:true">${sequence}</pimsForm:textarea>

		<c:if test="${!empty platesWithSameSequence && fn:length(platesWithSameSequence)>1 }"><!-- ignore the current plate, so >1 not >0 -->
			<div class="editonly">
			<p>The following plates have the same construct. Select the ones you want to update with the new sequence.</p>
			<div><label><input type="checkbox" checked="checked" disabled="disabled" onclick="return false" name="thisPlate" value="on" />&nbsp;${barcode} (This plate)</label></div>
			<c:forEach var="plate" items="${platesWithSameSequence}">
				<c:if test="${barcode != plate}">
				<div><label><input type="checkbox" class="otherplate" name="alsoUpdatePlate" value="${plate}" />&nbsp;${plate}</label></div>
				</c:if>
			</c:forEach>
			<c:if test="${fn:length(platesWithSameSequence) >2}"><!-- ignore the current plate, so >2 not >1 -->
				<!-- "Select/unselect all" -->
				<hr/>
				<div><label><input type="checkbox" class="otherplate" name="toggleAll" value="on" onclick="sequenceBoxToggleAll(this)"/>&nbsp;Select/unselect all</label></div>
				
			</c:if>
			</div>
		</c:if>
		
	    <input type="hidden" name="updateProtein" value="true" />
		<pimsForm:editSubmit />

    </pimsForm:formBlock>
    </pimsForm:form>
	
	<script type="text/javascript">
	function sequenceBoxToggleAll(elem){
		elem=$(elem);
		var wrapper=elem.up("#sequencebox");
		var boxes=wrapper.select(".otherplate");
		boxes.each(function(box){
			box.checked=elem.checked;
		});
	}
	</script>
	
</pimsWidget:box>

