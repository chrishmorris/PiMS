<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@tag import="java.util.*" %>

<%@attribute name="id" required="true"  %>
<%@attribute name="rows" required="false"  %>
<%@attribute name="cols" required="false"  %>

<%-- 
This tag writes a plate, with no selection or other user interaction present.

It defaults to 8 rows and 12 columns - a 96-well plate. 

The maximum supported size is 10 rows and 12 columns. This covers plate sizes
up to 96-well plates, as well as 9x9 racks.
--%>

<c:if test="${empty rows}"><c:set var="rows">8</c:set></c:if>
<c:if test="${empty cols}"><c:set var="cols">12</c:set></c:if>

<c:if test="${rows lt 1 || cols lt 1 || rows gt 10 || cols gt 12}">
<p class="error">plate.tag supports a maximum of 10 rows and 12 columns. It is being called with rows=${rows} and cols=${cols}.</p>
</c:if> 

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
rowLabels.add("I");
rowLabels.add("J");
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


<table id="${id}" class="pw_plate">
<tr><th>&nbsp;</th>
<c:forEach items="${columnLabels}" var="col" varStatus="count">
  <c:if test="${count.count le cols}">
  <th>${col}</th>
  </c:if>
</c:forEach>
<th>&nbsp;</th></tr>

<c:forEach items="${rowLabels}" var="row" varStatus="rowcount">
  <c:if test="${rowcount.count le rows}">
  <tr class="wells"><th>${row}</th>
  <c:forEach items="${columnLabels}" var="col" varStatus="colcount">
  <c:if test="${colcount.count le cols}">
    <td class="well" id="${row}${col}_${id}">&nbsp;</td>
  </c:if>
  </c:forEach>
  <th>&nbsp;</th></tr>
  </c:if>
</c:forEach>

<tr class="bottom"><th>&nbsp;</th>
  <c:forEach items="${columnLabels}" var="col" varStatus="colcount">
  <c:if test="${colcount.count le cols}">
  <th>&nbsp;</th>
  </c:if>
</c:forEach>
<th>&nbsp;</th></tr>
</table>
