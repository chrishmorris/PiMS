<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="value" type="org.pimslims.lab.Measurement" required="false"  %>
<%@attribute name="propertyName" required="true"  %>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled">readonly="readonly"</c:set>
</c:if>

<input type='hidden' name='${hook}:${propertyName}' id="${hook}:${propertyName}" value='${value}'/> 

<input class='amount' type='text' name='_${hook}:${propertyName }:value' 
id='_${hook}:${propertyName }:value' value='${value.displayValue}' 
  onChange='sampleOnChange(this, "${propertyName}")' />

<select class='units' name='_${hook}:${propertyName}:units' 
onChange='sampleOnChange(this, "${propertyName }")'>
<option value='${value.displayUnit}' selected='selected'>${value.displayUnit}</option>
<option value='[No Units]' >[No Units]</option>

<c:if test="${'concentration' ne propertyName}">
<optgroup label='Mass'>
  <option value='kg'>kg</option>
  <option value='g'>g</option>
  <option value='mg'>mg</option>
  <option value='ug'>ug</option>
  <option value='ng'>ng</option>
  <option value='pg'>pg</option>
</optgroup><optgroup label='Volume'><option value='L'>L</option>
  <option value='mL'>mL</option>
  <option value='uL'>uL</option>
  <option value='nL' >nL</option>
</optgroup><optgroup label='Others'><option value='IU'>IU</option>
  <option value='tube'>tube</option>
  <option value='pellet'>pellet</option>
  <option value='colonies'>colonies</option>
</optgroup>  
</c:if>

<c:if test="${'concentration' eq propertyName}">         
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
            </optgroup> 
  </c:if>        
</select>             
<c:if test="${null ne value and '' ne value}">
		  <span class="selectnoedit">${value}</span>
</c:if>
