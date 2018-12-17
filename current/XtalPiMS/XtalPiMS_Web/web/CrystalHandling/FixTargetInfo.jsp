<%-- Form to fprovide acronym and sequence, if either missing - included by Treatment.jsp --%>
<pimsWidget:box id="fixTarget" title="Provide protein information" initialState="fixed" extraHeader="">
	<pimsForm:form action="/update/SelectCrystal/?barcode=${barcode}" mode="edit" method="post" id="">
	<pimsForm:formBlock>
		<p class="error">You must provide both protein sequence and protein acronym before continuing. The acronym is used to identify the protein on the Diamond Experimental Risk Assessment ("safety sheet").</p>
		<input type="hidden" name="barcode" value="${barcode}" />
		<input type="hidden" name="well" value="${well}" />
		<input type="hidden" name="crystal" value="${crystal}" />
  		<pimsForm:textarea name="proteinsequence" alias="Protein sequence" validation="required:true,proteinSequence:true">${sequence}</pimsForm:textarea>
 		<pimsForm:text name="acronym" value="${acronym}" alias="Protein acronym" validation="required:true" helpText="If shipping to Diamond, this must match the acronym on the safety sheet" />
		<input type="submit" name="updateProtein" value="Update protein information" onclick="dontWarn()" />
	</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box> 
