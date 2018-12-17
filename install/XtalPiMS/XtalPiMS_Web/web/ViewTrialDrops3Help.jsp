<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="HeaderName" scope="page" value="Help for View Trial Drops v3" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>

<h3>Help for View Trial Drops v3</h3>
<div>
<table>
<tr><th class="myheader">Key</th><th class="myheader">Function</th></tr>
<tr><th class="myheader">f</th><td class="mycontent">Toggle navigation mode (Search list / Timecourse)</td></tr>
<tr><th class="myheader">a</th><td class="mycontent">Go to previous image in Search list / Timecourse</td></tr>
<tr><th class="myheader">d</th><td class="mycontent">Go to next image in Search list / Timecourse</td></tr>
<tr><th class="myheader">q</th><td class="mycontent">Go to first image in Search list / Timecourse</td></tr>
<tr><th class="myheader">e</th><td class="mycontent">Go to last image in Search list / Timecourse</td></tr>
<tr><th class="myheader">\</th><td class="mycontent">Go to well F01.1 (Search list only)</td></tr>
<tr><th class="myheader">s</th><td class="mycontent">Toggle (Start/Stop) movie</td></tr>
<tr><th class="myheader">z</th><td class="mycontent">Zoom out</td></tr>
<tr><th class="myheader">x</th><td class="mycontent">Reset zoom to 100%</td></tr>
<tr><th class="myheader">c</th><td class="mycontent">Zoom in</td></tr>
<tr><th class="myheader">v</th><td class="mycontent">Zoom to fit (also resets image position to origin)</td></tr>
<tr><th class="myheader">1-9</th><td class="mycontent">Annotate image from Clear (0) to Synchrotron (8)</td></tr>
</table>
<h3>Main Image Mouse Gestures</h3>
<table>
<tr><th class="myheader">Gesture</th><th class="myheader">Function</th></tr>
<tr><th class="myheader">Ctrl+Drag</th><td class="mycontent">Reposition image</td></tr>
<tr><th class="myheader">Drag</th><td class="mycontent">Measure image</td></tr>
<tr><th class="myheader">DblClick</th><td class="mycontent">Zoom in on clicked pixel</td></tr>
<tr><th class="myheader">Ctrl+DblCick</th><td class="mycontent">Zoom out on clicked pixel</td></tr>
</table>
</div>

<%-- HTML ENDS HERE --%>

<%@include file="./WEB-INF/jspf/footer.jspf"%>

