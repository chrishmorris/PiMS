<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<pimsWidget:pageTitle title="${groupBean.name}" icon="plate.png" suppressCalendar="true" />
        
<pimsForm:formBlock>

<pimsWidget:files bean="${groupBean}"  />
<pimsWidget:notes bean="${groupBean}"  />
                
</pimsForm:formBlock>
                
