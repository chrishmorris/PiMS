	<%-- Pre-shipment info box/form included by Treatment.jsp --%>
	<pimsWidget:box id="preship" title="Test diffraction results" initialState="fixed" extraHeader="">
	<c:if test="${!empty preShipExperiment}">
	</c:if>
		<c:set var="mode" value="edit" />
		<c:if test="${crystalShipped}">
			<c:set var="mode" value="view" />	
		</c:if>
		<pimsForm:form onsubmit="dontWarn()" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}" method="post" id="shippingform" mode="${mode}">

		<c:choose><c:when test="${'edit'==mode}">
			<p style="text-align:center">Here you can provide useful information for the synchrotron:</p>
		</c:when><c:otherwise>
			<p style="text-align:center">Crystal has been shipped. This information was provided to the synchrotron:</p>
		</c:otherwise></c:choose>
		<pimsForm:formBlock>
		<pimsForm:column1>
			<pimsForm:select name="datacolltype" alias="Data collection type">
				<pimsForm:option alias="OSC" optionValue="OSC" currentValue="${ship_Datacollectiontype}" /> 
				<pimsForm:option alias="SAD" optionValue="SAD" currentValue="${ship_Datacollectiontype}" /> 
				<pimsForm:option alias="MAD" optionValue="MAD" currentValue="${ship_Datacollectiontype}" /> 
			</pimsForm:select>
			<pimsForm:nonFormFieldInfo label="Unit cell dimensions">
				a <input type="text" name="a" value="${ship_a}" style="width:4em" />&nbsp;&nbsp;
				b <input type="text" name="b" value="${ship_b}" style="width:4em" />&nbsp;&nbsp;
				c <input type="text" name="c" value="${ship_c}" style="width:4em" /><br/>
				&#945; <input type="text" name="alpha" value="${ship_alpha}" style="width:4em" />&nbsp;&nbsp;
				&#946; <input type="text" name="beta" value="${ship_beta}" style="width:4em" />&nbsp;&nbsp;
				&#947; <input type="text" name="gamma" value="${ship_gamma}" style="width:4em" />
			</pimsForm:nonFormFieldInfo>
			<input type="hidden" name="treatmentURL" value="" />
			<input type="hidden" name="preShipExperiment" value="${preShipExperiment.hook}" />
		</pimsForm:column1>
		<pimsForm:column2>
			<pimsForm:select alias="Space group" name="spacegroup">
			<c:forEach var="sg" items="${spaceGroups}">
				<pimsForm:option alias="${sg}" optionValue="${sg}" currentValue="${ship_Spacegroup}" /> 
			</c:forEach>
			</pimsForm:select>
			<pimsForm:select alias="Heavy atom" name="heavyatom">
			<c:forEach var="ha" items="${heavyAtoms}">
				<pimsForm:option alias="${ha}" optionValue="${ha}" currentValue="${ship_Anomalousscatterer}" /> 
			</c:forEach>
			</pimsForm:select>
			<pimsForm:text alias="Comments" name="comments" value="${ship_Comments}" />
		</pimsForm:column2>
		</pimsForm:formBlock>
		<c:if test="${'edit'==mode}">
			<p style="text-align:center"><input type="submit" name="updatePreShipInfo" value="Update" /></p>
		</c:if>
		</pimsForm:form>
		<script type="text/javascript">
		var urlField=$("shippingform").down("input[name=treatmentURL]");
		var loc=window.document.location+"";
		urlField.value=loc.replace(window.document.location.hash,"");
		</script>
	</pimsWidget:box> 
