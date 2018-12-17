<%--
Generates an <img> tag linking to a QR code with the specified content. 
The content is URL-encoded for you.

If no content is provided, a placeholder is inserted; Javascript in the footer changes this to document.location.
 --%>
<%@ tag body-content="empty" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="content" required="false"  %><%-- The content of the QR code, eg a URL --%>
<%@attribute name="size" required="false"  %><%-- Default 100. The size (in pixels) of the square image, including a "quiet area" around the code itself --%>
<%@attribute name="style" required="false"  %><%-- Any one-off styles, e.g., "float:right" --%> 
<%@attribute name="extraClasses" required="false" %><%-- A space-separated list of additional classes. "qrcode" and "printonly" are always applied. --%>

<c:if test="${empty size}"><c:set var="size" value="100" /></c:if>
<c:if test="${empty content}"><c:set var="content" value="[[CURRENTPAGE]]" /></c:if>

<c:set var="classes">qrcode printonly ${extraClasses}</c:set>
<c:set var="style">height:${size}px;width:${size}px;${style}</c:set>

<c:set var="src"><c:url value="/public/qr">
	<c:param name="content" value="${content}" />
	<c:param name="size" value="${size}" />
</c:url></c:set>

<img class="${classes}" style="${style}" alt="QR Code" src="${src}" />