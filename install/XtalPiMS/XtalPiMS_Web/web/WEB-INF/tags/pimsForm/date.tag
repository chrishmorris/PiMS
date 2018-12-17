<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm"   tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" type="java.util.Calendar" required="false"  %>
<%@attribute name="validation" required="false"  %>
 

<c:if test="${empty value}"><c:set var="value" value="<%= java.util.Calendar.getInstance() %>" /></c:if>

<c:set var="date">
    <fmt:formatDate value="${value.time}" pattern="<%=org.pimslims.lab.Utils.date_format %>"/>    
</c:set>
<c:set var="timestamp" value="${value.timeInMillis}" />
<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

      
<%-- TODO make this compatible with HTML5. See time.js --%>
<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" />
	<div class="formfield" ><%--style="white-space: nowrap;" --%>
	<img class="editdate" id="${name}_icon" src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/editdate.gif" 
	alt="Edit date" title="Edit date" />
	
<%-- or HTML5 type="datetime-local" --%>	 
<input title="iso" class="date" id="_iso${name}" type="hidden"
  onchange="setDate(this.value, '${name}')"
/>
<input title="timestamp" type="hidden" id="${name}" name="${name}" type="number"  readonly="readonly" />
<input title="human readable" type="text" class="date" id="_hr${name}" type="text" readonly="readonly" />
<%-- could <input title="offset" type="text" class="date" id="_offset${name}"  type="text" readonly="readonly" /> --%>
	
	<%-- noedit --%>
	<span class="date"><pimsWidget:dateLink date="${value}" /></span>
	</div>
	<%-- needed for non-HTML5 browsers --%>
	<script type="text/javascript">
      Calendar.setup({
       			inputField     :    "_iso${name}",     // id of the input field
       			ifFormat       :  	"%Y-%m-%dT%H:%M:%S", // same as html5 datetime-local  
       			daFormat       :     "%Y-%m-%dT%H:%M:%S", // same as html5 datetime-local  
       			button         :    "${name}_icon",  // trigger for the calendar (button ID)
       			align          :    "Tl",           // alignment (defaults to "Bl")
       			singleClick    :    true,
       			step           :    "1"  
      });
      var iso = dateToIsoLocal(new Date(${timestamp}));
      document.getElementById('_iso${name}').value = iso;
      setDate(iso, '${name}');
	</script>
</pimsForm:formItem>
