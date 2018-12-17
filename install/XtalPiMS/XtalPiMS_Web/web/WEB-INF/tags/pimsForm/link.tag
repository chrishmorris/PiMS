<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>

<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<pimsForm:formItem name="${name}" alias="${alias}" >
    <pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}"  />
    <div class="formfield">
        <pimsWidget:link bean="${bean}" />
    </div>
</pimsForm:formItem>


