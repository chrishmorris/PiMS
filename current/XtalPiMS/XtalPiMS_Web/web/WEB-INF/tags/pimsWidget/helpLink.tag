<%--
* helpLink.tag
*
* Writes a link labelled "Help", with an appropriate icon, pointing to the
* supplied URL (which should be a relevant help page). This should open in a
* new window or tab, if JavaScript is enabled.
*
* LATER Maybe allow overriding of link text, to , e.g., "Help with xyz"
--%>
<%@attribute name="url" required="true" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<pimsWidget:linkWithIcon text="Help" icon="misc/help.gif" url="${url}"
   onclick="window.open('${url}');return false" />