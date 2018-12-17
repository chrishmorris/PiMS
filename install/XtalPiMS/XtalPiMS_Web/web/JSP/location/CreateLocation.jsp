<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>&#13;
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>&#13;
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>&#13;


<script type="text/javascript">
function validForm(theForm){
  var reason = "";
  for (var i = 0; i < theForm.elements.length; ++i) {
      var element = theForm.elements[i];
      if (element.name.match("locationName") && element.value=="") {
    	  reason += "Location Name is mandatory\n";
      }
      if (element.value != "") {
        if (element.name.match("locationTemperature") && !element.name.match("Unit"))
          if (!isNumeric(element.value))
      	    reason += "Temperature should be numeric\n";
     
        if (element.name.match("locationPressure") && !element.name.match("Unit"))
          if (!isNumeric(element.value))
      	    reason += "Pressure should be numeric\n";  
     }    	
  }
  
  if (reason != "") {
    alert("Some fields need correction:\n" + reason);
    return false;
  }
  dontWarn();
  return true;
}

</script>

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new Location' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>
obsolete
<%-- page body here --%>
<br />
<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.location.Location">Locations</a>
</c:set>
<c:set var="title" value="Record a new Location:"/>

<%-- 
<c:set var="actions">
 <a href="${pageContext.request.contextPath}/help/HelpExtensions.jsp">Help</a>
</c:set>
--%>

<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}"actions="${actions}" icon="${icon}"  />

<%--Later set this based on type of Extension to create depending on where this is called from
and also use to set the term as in ExtensionView--%>
<c:set var="dir" value="forward" scope="page" />

<pimsWidget:box id="box1" title="Location details" initialState="open" >
	<pimsForm:form method="post" action="/Create/org.pimslims.model.location.Location" mode="create">
		<pimsForm:formBlock>
			<pimsForm:column1>
				<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.location.Location"/>		
				<pimsForm:text alias="Name" name="locationName" value=""  validation="required:true, unique:{obj:'org.pimslims.model.location.Location',prop:'name'}" helpText="The location ID or common name" /><%--maxlength="80"--%>
			
				<pimsForm:select alias="Location type" name="locationType" helpText="The type of the location" >
					<pimsForm:option helpText="No location defined" currentValue="none" optionValue="none" alias="[none]" />
					<pimsForm:option helpText="A freezer with a lid -opens upwards" currentValue="none" optionValue="Chest freezer" alias="Chest freezer" />
					<pimsForm:option helpText="A freezer with a door -opens outwards" currentValue="none" optionValue="Upright freezer" alias="Upright freezer" />
					<pimsForm:option helpText="4&deg;C refrigerator" currentValue="none" optionValue="Fridge" alias="Fridge" />
					<pimsForm:option helpText="Clone Saver card" currentValue="none" optionValue="Card" alias="Card" />
					<pimsForm:option helpText="A box or holder with positions for tubes" currentValue="none" optionValue="Rack" alias="Rack" />
					<pimsForm:option helpText="A horizontal suface often inside a fridge or upright freezer" currentValue="none" optionValue="Shelf" alias="Shelf" />
					<pimsForm:option helpText="A tall holder with positions for boxes, trays or racks" currentValue="none" optionValue="Tower" alias="Tower" />
					<pimsForm:option helpText="A Plate or holder for samples" currentValue="none" optionValue="Tray" alias="Tray" />
				</pimsForm:select>
			</pimsForm:column1>
		
			<pimsForm:column2>
				<pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook this location belongs to - usually choose one you share with your colleagues" objects="${accessObjects}" />
			
				<pimsForm:formItem name="locationTemperature" alias="Temperature" >
				<pimsForm:formItemLabel name="locationTemperature" alias="Temperature" helpText="The temperature of the location"  />
				
				<div class="formfield" >
					<input style="width:6em;" type="text" name="locationTemperature" id="locationTemperature"  value=""  />
					<select style="width:6em;" name="locationTemperatureUnit" id="locationTemperatureUnit" >
						<option value="none">[none]</option>	
						<option value="C" selected="selected">&deg;C</option>	
						<option value="F">&deg;F</option>	
						<option value="K">&deg;K</option>	
					</select>
				</div>
				</pimsForm:formItem>
			
				<pimsForm:formItem name="locationPressure" alias="Pressure" >
				<pimsForm:formItemLabel name="locationPressure" alias="Pressure" helpText="The temperature of the location"  />
				
				<div class="formfield" >
					<input style="width:6em;" type="text" name="locationPressure" id="locationPressure"  value=""  />
					<select style="width:6em;" name="locationPressureUnit" id="locationPressureUnit" >
						<option value="none">[none]</option>	
						<option value="Pa">Pa</option>	
						<option value="bar">bar</option>	
						<option value="atm">atm</option>
						<option value="psi">psi</option>	
					</select>
				</div>
				</pimsForm:formItem>
			</pimsForm:column2>
		</pimsForm:formBlock>
				
		<pimsForm:formBlock>
      		<pimsForm:select alias="Parent location" name="locationParent" helpText="The holding location of the Location" >		
        	<option value="none">[none]</option>
	    	<c:forEach var="p" items="${locations}"> 
	    		<c:choose>
	    		<c:when test="${location != null && p._Hook == location}">
             		<option value="${p._Hook}" selected="selected"><c:out value="${p._Name}" /></option>
            	</c:when>
            	<c:otherwise>
            		<option value="${p._Hook}"><c:out value="${p._Name}" /></option>
           	 	</c:otherwise>
            	</c:choose> 
        	</c:forEach>
      		</pimsForm:select>
    	</pimsForm:formBlock>   
		
		<pimsForm:formBlock>
     		<pimsForm:submitCreate onclick="return validForm(this.form);" />
   		</pimsForm:formBlock>		
	</pimsForm:form>	
</pimsWidget:box>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />

