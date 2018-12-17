<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="false"  %>
<%@attribute name="onclick" required="false"  %>

<pimsForm:formItem extraClasses="submitCreate" alias="" name="">

<c:choose>
<c:when test="${null ne name}">
	<c:choose>
	<c:when test="${null ne onclick}">
		<input type="submit" id="${name}" value="Create" onclick="${onclick};" />
	</c:when>
	<c:otherwise>
		<input type="submit" id="${name}" value="Create" onclick="dontWarn();"  />
	</c:otherwise>
	</c:choose>
</c:when>
<c:otherwise>
	<c:choose>
	<c:when test="${null ne onclick}">
		<input type="submit" value="Create" onclick="${onclick};" />
	</c:when>
	<c:otherwise>
		<input type="submit" value="Create" onclick="dontWarn();"  />
	</c:otherwise>
	</c:choose>
</c:otherwise>
</c:choose>	
</pimsForm:formItem>