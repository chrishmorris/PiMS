	<%-- Final mount form - included by Treatment.jsp --%>
<pimsForm:form mode="edit" method="post" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}" id="${mountFormId}">
	<c:if test="${not empty nonExistentPin}">
		<p style="color:#600;border:2px solid #600;background-color:#fcc;text-align:center;font-weight:bold;">No pin with barcode ${nonExistentPin} is registered in PiMS</p>
	</c:if>
	<c:if test="${not empty nonExistentPuck}">
		<p style="color:#600;border:2px solid #600;background-color:#fcc;text-align:center;font-weight:bold;">No puck with barcode ${nonExistentPuck} is registered in PiMS</p>
	</c:if>
	<pimsForm:formBlock>
		<pimsForm:column1>

			<pimsForm:text name="pin" alias="Pin barcode" value="${pinBarcode}" validation="" />

			<pimsForm:text name="puck" alias="Puck" value="${puckBarcode}" validation="" />
			<pimsForm:nonFormFieldInfo label="Position in puck"><div class="puckcontents">Scan a puck first<input type="hidden" id="puckPosition" name="puckPosition" value="" /></div></pimsForm:nonFormFieldInfo>
			<br/>&nbsp;
		</pimsForm:column1>
		<pimsForm:column2>
			<input type="hidden" name="inputSample" value="Crystal:${lastOutputSample}" />
			<input type="hidden" name="plateBarcode" value="${barcode}" />
			<input type="hidden" name="well" value="${well}" />
			<input type="hidden" name="crystalNumber" value="${crystal}" />
			<input type="hidden" name="treatmentURL" value="" />
			<input type="hidden" name="ship" value="ship" />
			<input type="submit" class="shipbutton" name="shipbutton" style="float:left" value="Ready to ship" onclick="return Treatment.submitMountForm(this)" />
		</pimsForm:column2>
	</pimsForm:formBlock>
</pimsForm:form>
