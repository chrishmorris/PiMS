<%@ tag body-content="tagdependent" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="path" required="true"  %>

<%-- Passes details of an image to MhtmlFilter for inclusion in a report 
Note that this can only add static images,
because no session is used to fetch the image
--%>

<c:set var="_mhtml_path" value="${path }" scope="request" />
<% if (Boolean.TRUE.equals(request.getAttribute("_MHTML"))) {
	((java.util.Collection) request.getAttribute("_MHTML_IMAGES"))
	    .add(request.getAttribute("_mhtml_path"));
} %>
