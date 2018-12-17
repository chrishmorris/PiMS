<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@tag import="java.util.*" %>

<%@attribute name="id" required="true"  %>
<%@attribute name="callbackFunction" required="true"  %>
<%--
	TODO support plates other than 96-well. "rows" and "columns"
	should default to 8 and 12 respectively - allows immediate use
	of tag for 96-well plates without need to modify callers later.
	<%@attribute name="rows" required="false"  %>
	<%@attribute name="columns" required="false"  %>
--%>


<%-- 
This tag writes a plate and allows the user to select a 
rectangular range of wells. These are then passed to the
callback function you specify. See below for the format,
and the example callback function in widgets.js called
pwPlateSelector_exampleCallbackFunction.

Non-contiguous ranges are not supported; the callback
function fires onmouseup, so you can only get a rectangular
range.

You should ensure that you design a way for users to undo. If
selecting a range of wells sets a pre-selected construct in 
those wells, make sure that there is a "None" option so that 
the user can clear the construct.

Information about the selected wells is passed to the callback 
function as an object, in the form

{
	plate: {
	  id:"plateSelectorId"
	},
	selectedWells: [
		{ row:0, column:0, id:"A01" },
		{ row:0, column:1, id:"A02" },
		...
		{ row:7, column:11, id:"H12" }
	]
}

Note that the row and column are zero-based.
--%>

<%
List rowLabels=new LinkedList();
rowLabels.add("A");
rowLabels.add("B");
rowLabels.add("C");
rowLabels.add("D");
rowLabels.add("E");
rowLabels.add("F");
rowLabels.add("G");
rowLabels.add("H");
request.setAttribute("rowLabels",rowLabels);
List columnLabels=new LinkedList();
columnLabels.add("01");
columnLabels.add("02");
columnLabels.add("03");
columnLabels.add("04");
columnLabels.add("05");
columnLabels.add("06");
columnLabels.add("07");
columnLabels.add("08");
columnLabels.add("09");
columnLabels.add("10");
columnLabels.add("11");
columnLabels.add("12");
request.setAttribute("columnLabels",columnLabels);
%>


<table id="${id}" class="pw_plate" 
		onmouseover="pwPlateSelector_plateOnMouseOver(this)"
		onmouseout="pwPlateSelector_plateOnMouseOut(this)">
<tr><th>&nbsp;</th>
<c:forEach items="${columnLabels}" var="col">
  <th>${col}</th>
</c:forEach>
<th>&nbsp;</th></tr>

<c:forEach items="${rowLabels}" var="row">
  <tr class="wells"><th>${row}</th>
  <c:forEach items="${columnLabels}" var="col">
    <td class="well" id="${row}${col}_${id}"
        onmouseDown="pwPlateSelector_wellOnMouseDown(this)" 
        onmouseOver="pwPlateSelector_wellOnMouseOver(this)" 
        onmouseup="pwPlateSelector_wellOnMouseUp(this)">&nbsp;</td>
  </c:forEach>
  <th>&nbsp;</th></tr>
</c:forEach>

<tr><th>&nbsp;</th>
<c:forEach items="${columnLabels}" var="col">
  <th>&nbsp;</th>
</c:forEach>
<th>&nbsp;</th></tr>
</table>
<script type="text/javascript">
document.getElementById("${id}").callback=function(obj){${callbackFunction}(obj)};
</script>