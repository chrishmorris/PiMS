	<%-- Info on already-mounted crystal's pin/puck, included by Treatment.jsp --%>
	<pimsWidget:box id="finalmountbox" title="Crystal treatment finished" initialState="fixed" extraHeader="">

	<c:if test="${!fn:startsWith(pinBarcode, 'pin_')}">
		<strong>Pin barcode: </strong> <c:out value="${pinBarcode}"/>
		<c:if test="${pinReused}"> (pin has been re-used since)<br/></c:if>
	</c:if>
	<c:if test="${!empty finalmount_puckBarcode && !empty finalmount_positionInPuck}">
		<br/><strong>Puck barcode: </strong> <c:out value="${finalmount_puckBarcode}"/>, position <c:out value="${finalmount_positionInPuck}"/>
	</c:if>
	
	<c:if test="${finalMountExperiment.mayDelete}">
		<form method="post" style="text-align:center;margin-bottom:1em" id="mountform"
			action="${pageContext.request.contextPath}/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}">
			<input type="hidden" name="hook" value="${finalMountExperiment.hook}" />
			<input type="hidden" name="hook" value="${finalMountSample.hook}" />

			<c:if test="${!empty preShipExperiment}">
				<input type="hidden" name="hook" value="${preShipExperiment.hook}" />
				<input type="hidden" name="hook" value="${preShipSample.hook}" />
			</c:if>
		<c:if test="${!crystalShipped}">
			<input type="submit" name="delete" value="Undo 'Ready for shipping'" onclick="dontWarn()" />
		</c:if>
		</form>
	</c:if>
	</pimsWidget:box> 
