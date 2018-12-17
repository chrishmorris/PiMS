	<%-- Next step box - included by Treatment.jsp --%>
	<c:if test="${selectionExperiment.mayUpdate}">
	<pimsWidget:box title="Next step" initialState="fixed" extraHeader="" extraClasses="noprint">
		<pimsForm:form mode="edit" method="post" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}">
			<pimsForm:formBlock>
			<c:set var="freezeButton" value="" />
			<c:set var="customButton" value="" />
			<c:forEach var="protocol" items="${protocols}">
				<c:choose><c:when test="${'Custom' eq protocol.name }">
					<c:set var="customButton">
						<input type="submit" name="protocolName" value="Custom" />
					</c:set>
				</c:when><c:when test="${'Freeze' eq protocol.name }">
					<c:set var="freezeButton">
						<input type="submit" name="protocolName" value="Freeze" />
						</c:set>
				</c:when><c:otherwise>
					<input type="submit" name="protocolName" value="${protocol.name}" />
				</c:otherwise></c:choose>
			</c:forEach>
			${customButton}
			&nbsp;&nbsp;&nbsp;&nbsp;${freezeButton}
			<input type="hidden" name="barcode" value="${barcode}" />
			<input type="hidden" name="well" value="${well}" />
			<input type="hidden" name="crystal" value="${crystal}" />
			<input type="hidden" name="inputSample" value="Crystal:${lastOutputSample}" />
			</pimsForm:formBlock>
		</pimsForm:form>
		
		<c:if test="${empty experimentChain and not empty otherCoordsWithTreatmentHistory}">
		<hr/><pimsForm:form mode="edit" method="post" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}">
			<pimsForm:formBlock>Copy treatment history from 
			<select name="sourceExperiment">
				<c:forEach var="crystal" items="${otherCoordsWithTreatmentHistory}">
					<option value="${crystal.hook}">Crystal ${crystal.crystalNumber}</option>
				</c:forEach>
			</select>
			<input type="submit" name="copyTreatmentHistory" value="Copy treatment history" />
			<input type="hidden" name="barcode" value="${barcode}" />
			<input type="hidden" name="well" value="${well}" />
			<input type="hidden" name="crystal" value="${crystal}" />
			<input type="hidden" name="selectionExperiment" value="${selectionExperiment.hook}" />
			</pimsForm:formBlock>
		</pimsForm:form>
		</c:if>
	</pimsWidget:box> 
	</c:if>
