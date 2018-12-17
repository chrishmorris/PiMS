<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" type="org.pimslims.lab.Measurement" required="false"  %>
<%@attribute name="propertyName" required="true"  %>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled">readonly="readonly"</c:set>
</c:if>

<pimsForm:formItem name="${name}" alias="${alias}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" />
	<div class="formfield">
	<pimsForm:doAmount hook="${hook }" propertyName="${propertyName }" value="${value }" />
	</div>
</pimsForm:formItem>