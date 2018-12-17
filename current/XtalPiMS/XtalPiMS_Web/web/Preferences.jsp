<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="xtalpims" uri="http://www.pims-lims.org/xtalpims" %>
<c:set var="HeaderName" scope="page" value="Preferences" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>
<h3>Preferences</h3>
<div>
<form id="pref" method="post" action="" onsubmit="updatePrefs(); return false">
<table>
<tr><th class="myheader">Key</th><th class="myheader">Value</th></tr>
<tr><th class="myheader">View Trial Drops Version</th><td class="mycontent"><select id="vtd" name="vtd"><option value="1">Version 1</option><option value="2">Version 2</option><option value="3">Latest</option></select></td></tr>
</table>
<input type="submit" value="Update" />
</form>
</div>
obsolete
<script type="text/javascript">
function selectOpt(opts, val) {
	for (var i = 0; i < opts.length; i++) {
		if (opts[i].value === val) {
			opts[i].selected = 'selected';
		}
		else {
			opts[i].selected = '';
		}
	}
}
function init() {
	var prefs = <xtalpims:prefs />;
	selectOpt($('vtd').options, prefs.vtd);
}
function updatePrefs() {
	new Ajax.Request(contextPath + '/UserPrefsServlet', {
		parameters: $('pref').serialize(true)
	});
}
Event.observe(window, "load", init);
</script>
<%-- HTML ENDS HERE --%>

<%@include file="./WEB-INF/jspf/footer.jspf"%>

