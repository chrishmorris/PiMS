<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="displayUnit" required="false"  %>
<%@attribute name="displayAmount" required="false"  %>
<%-- For creating amounts. When editing, see amount.tag --%>
<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled">readonly="readonly"</c:set>
</c:if>

<c:if test="${empty displayAmount && !empty displayUnit}"><c:set var="displayAmount" value="0.0" /></c:if>

<script type="text/javascript">
var displayAmount="${displayAmount}";
var displayUnit="${displayUnit}";
</script>

<pimsForm:formItem name="${name}" alias="${name}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" datatype="${datatype}" />
	<div class="formfield" >
			
		<input ${disabled} style="width:8.7em;" type="text" class="amount" name="${hook}:_amount" id="${hook}:_amount" 
			    value="${displayAmount}"  
			    onchange="setMeasurement(this);"
			    />
		    &nbsp;
		<select ${disabled} style="width:10em;" class='unit' name="${hook}:_displayUnit" id="${hook}:_displayUnit" 
		        onchange="setMeasurement(this);">
        	<c:choose><c:when test="${empty displayUnit}" > 
        		<option value="[No Units]" selected="selected">Choose Unit:</option>
        	</c:when><c:otherwise>
        		<option value="[No Units]">Choose Unit:</option>
        	</c:otherwise></c:choose>
        	
        	<optgroup label='Mass'>
        	<c:choose><c:when test="${displayUnit == 'kg'}" > 
        		<option value="kg" selected="selected">kg</option>
        	</c:when><c:otherwise>
        		<option value="kg">kg</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'g'}" > 
        		<option value="g" selected="selected">g</option>
        	</c:when><c:otherwise>
        		<option value="g">g</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'mg'}" > 
        		<option value="mg" selected="selected">mg</option>
        	</c:when><c:otherwise>
        		<option value="mg">mg</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'ug'}" > 
        		<option value="ug" selected="selected">&#181;g</option>
        	</c:when><c:otherwise>
        		<option value="ug">&#181;g</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'ng'}" > 
        		<option value="ng" selected="selected">ng</option>
        	</c:when><c:otherwise>
        		<option value="ng">ng</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'pg'}" > 
        		<option value="pg" selected="selected">pg</option>
        	</c:when><c:otherwise>
        		<option value="pg">pg</option>
        	</c:otherwise></c:choose>
        	</optgroup>
        	
        	<optgroup label='Volume'>
        	<c:choose><c:when test="${displayUnit == 'L'}" > 
        		<option value="L" selected="selected">L</option>
        	</c:when><c:otherwise>
        		<option value="L">L</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'mL'}" > 
        		<option value="mL" selected="selected">mL</option>
        	</c:when><c:otherwise>
        		<option value="mL">mL</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'uL'}" > 
        		<option value="uL" selected="selected">&#181;L</option>
        	</c:when><c:otherwise>
        		<option value="uL">&#181;L</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'nL'}" > 
        		<option value="nL" selected="selected">nL</option>
        	</c:when><c:otherwise>
        		<option value="nL">nL</option>
        	</c:otherwise></c:choose>
        	</optgroup>
        	<%--
        	<optgroup label='Concentration'>
            <c:choose><c:when test="${displayUnit == 'M'}" > 
                <option value="M" selected="selected">M</option>
            </c:when><c:otherwise>
                <option value="M">M</option>
            </c:otherwise></c:choose>
            <c:choose><c:when test="${displayUnit == 'mM'}" > 
                <option value="mM" selected="selected">mM</option>
            </c:when><c:otherwise>
                <option value="mM">mM</option>
            </c:otherwise></c:choose>
            <c:choose><c:when test="${displayUnit == 'uM'}" > 
                <option value="uM" selected="selected">&#181;M</option>
            </c:when><c:otherwise>
                <option value="uM">&#181;M</option>
            </c:otherwise></c:choose>
            
            <c:choose><c:when test="${displayUnit == 'mg/mL'}" > 
                <option value="mg/mL" selected="selected">mg/mL</option>
            </c:when><c:otherwise>
                <option value="mg/mL">mg/mL (w/v)</option>
            </c:otherwise></c:choose>            
            <c:choose><c:when test="${displayUnit == 'g/g'}" > 
                <option value="g/g" selected="selected">g/g</option>
            </c:when><c:otherwise>
                <option value="g/g">g/ (w/w)</option>
            </c:otherwise></c:choose>            
            <c:choose><c:when test="${displayUnit == 'L/L'}" > 
                <option value="L/L" selected="selected">L/L</option>
            </c:when><c:otherwise>
                <option value="L/L">L/L (v/v)</option>
            </c:otherwise></c:choose>
            
            <c:choose><c:when test="${displayUnit == '%w/v'}" > 
                <option value="%w/v" selected="selected">%w/v</option>
            </c:when><c:otherwise>
                <option value="%w/v">%w/v</option>
            </c:otherwise></c:choose>            
            <c:choose><c:when test="${displayUnit == '%w/w'}" > 
                <option value="%w/w" selected="selected">%w/w</option>
            </c:when><c:otherwise>
                <option value="%w/w">%w/w</option>
            </c:otherwise></c:choose>            
            <c:choose><c:when test="${displayUnit == '%v/v'}" > 
                <option value="%v/v" selected="selected">%v/v</option>
            </c:when><c:otherwise>
                <option value="%v/v">%v/v</option>
            </c:otherwise></c:choose>
            </optgroup> --%>
        	
       		<optgroup label='Others'>
       		<c:choose><c:when test="${displayUnit == 'IU'}" > 
        		<option value="IU" selected="selected">IU</option>
        	</c:when><c:otherwise>
        		<option value="IU">IU</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'tube'}" > 
        		<option value="tube" selected="selected">tube</option>
        	</c:when><c:otherwise>
        		<option value="tube">tube</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'pellet'}" > 
        		<option value="pellet" selected="selected">pellet</option>
        	</c:when><c:otherwise>
        		<option value="pellet">pellet</option>
        	</c:otherwise></c:choose>
        	
        	<c:choose><c:when test="${displayUnit == 'colonies'}" > 
        		<option value="colonies" selected="selected">colonies</option>
        	</c:when><c:otherwise>
        		<option value="colonies">colonies</option>
        	</c:otherwise></c:choose>
        	</optgroup>
        </select>
		    
		<input ${disabled} type="hidden" class="text" 
		                   name="${hook}:_measurement" 
		                   id="${hook}:_measurement" 
		                   value="${displayAmount}${displayUnit}"/>
    </div>
</pimsForm:formItem>

<script type="text/javascript">

function setMeasurement(field) {
	
	var basename = "";
	if (field.name.match("_amount"))
	  	basename = field.name.substring(0, field.name.length-7);
	if (field.name.match("_displayUnit"))
	  	basename = field.name.substring(0, field.name.length-12);

	var valuename = basename+"_amount";
	var unitname = basename+"_displayUnit";
	var measurementname = basename+"_measurement";

	var valueField;
	var unitField;
	for(i=0; i<field.form.elements.length; i++) {
	    
		if (field.form.elements[i].name==valuename) {
			valueField = field.form.elements[i];
		}
		if (field.form.elements[i].name == unitname) {
			unitField = field.form.elements[i];
		}
	}

	//if(""!=valueField.value && !isNumeric(valueField.value)){
	//	alert('Amount must be numeric ['+valueField.value+']'); 
	//	valueField.value=displayAmount;
	//	unitField.value=displayUnit;
	//	return false;
	//} 
	
	//if (""!=valueField.value && "[No Units]"==unitField.value) {
	//	alert('Please select a unit for the value ['+valueField.value+" "+unitField.value+']');
	//	valueField.value=displayAmount;
	//	unitField.value=displayUnit;
	//	return false
	//}
	//if (""==valueField.value && "[No Units]"!=unitField.value) {
	//	alert('Please select an amount for the unit ['+valueField.value+" "+unitField.value+']');
	//	valueField.value=displayAmount;
	//	if (""==displayUnit) {
	//		unitField.value="[No Units]";
	//	} else {
	//		unitField.value=displayUnit;
	//	}
	//	return false
	//}
	
	//if (""==unitField.value || "[No Units]"==unitField.value) {
	//	alert('Please select the unit for the amount ['+unitField.value+']');
	//	return false
	//}

	//if (""==valueField.value.trim && "[No Units]"!=unitField.value) {
	//	alert('Please select the value for the unit ['+valueField.value+']');
	//	return false
	//}
	  
	var measurement=valueField.value+unitField.value;
	  
	for(i=0; i<field.form.elements.length; i++) {
		if (field.form.elements[i].name==measurementname) {
			field.form.elements[i].value = measurement;
		}
	}
	return true;
}

</script>