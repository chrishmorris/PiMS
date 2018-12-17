<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="false"  %>
<%@attribute name="label" required="false"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" required="true" type="java.lang.String" %>
<%@attribute name="onclick" required="false"  %>

<%-- was  <pimsForm:radio name="${sample.hook}:${Sample['PROP_ISACTIVE']}" value="yes" label="Yes" isChecked="${isActive eq 'Yes'}" alias="Stock available" />
				<pimsForm:radio name="${sample.hook}:${Sample['PROP_ISACTIVE']}" value="no"  label="No"  isChecked="${isActive ne 'Yes'}" alias="" />
 --%>


<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="disabled=\"disabled\"" />
</c:if>

<pimsForm:formItem extraClasses="radio" alias="${alias}" name="${name}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="" datatype="${type}" />
	<div class="formfield">
	<span class="viewonly">${value }</span>
	<c:choose><c:when test="${_MHTML }">
	   <!-- no edit controls needed -->
	</c:when><c:when test="${value eq 'Yes'}">
	    <input  checked="checked" class="checked editonly"    
	      type="radio" name="${name}" onclick="${onclick}" value="yes"  />
	    <span  class="editonly" class="radiolabel">Yes</span> 
	    &nbsp;&nbsp;	
	    <input class="editonly"  
	        type="radio" name="${name}" onclick="${onclick}" value="no" />
	    <span class="editonly" class="radiolabel">No</span>
	</c:when><c:otherwise>
	   <input class="editonly"     
	      type="radio" name="${name}" onclick="${onclick}" value="yes"  />
	    <span  class="editonly" class="radiolabel">Yes</span> 
	    &nbsp;&nbsp;	
	    <input class="editonly checked" checked="checked"
	        type="radio" name="${name}" onclick="${onclick}" value="no" />
	    <span class="editonly" class="radiolabel">No</span></c:otherwise></c:choose>	
	</div>
</pimsForm:formItem>