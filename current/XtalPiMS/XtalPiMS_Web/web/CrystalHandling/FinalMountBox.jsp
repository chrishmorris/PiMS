	<%-- Final mount form - included by Treatment.jsp --%>


	<c:if test="${selectionExperiment.mayUpdate}">
	<c:set var="mountFormId" value="mountform" />
	<pimsWidget:box id="finalmount" title="Finish crystal treatment" initialState="fixed" extraHeader="">
	<%@include file="/CrystalHandling/FinalMountForm.jsp" %>
	</pimsWidget:box> 
	</c:if>
