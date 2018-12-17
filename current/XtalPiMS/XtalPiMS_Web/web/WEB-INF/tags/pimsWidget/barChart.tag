<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="bean" required="true" type="org.pimslims.kpi.BarChartBean" %>

<table style="border:1px solid black">
  <caption><a title="${bean.toolTip}" href="${bean.URL }" >${bean.caption }</a></caption>
  <tr><th>Protocol</th>
  <c:forEach var="bar" items="${bean.bars }" >
      <th><a title="${bar.toolTip}" href="${bar.URL }" >${bar.caption }</a></th>
  </c:forEach></tr>
  <tr><th>Av. Hours</th>
  <c:forEach var="bar" items="${bean.bars }" >
    <td><fmt:formatNumber maxFractionDigits="1" value="${bar.width }" /></td>
  </c:forEach></tr>
  <tr><th>Succeeded</th>
  <c:forEach var="bar" items="${bean.bars }" >
    <td><a title="${bar.slices[0].toolTip}" href="${bar.slices[0].URL }">
      <fmt:formatNumber value="${bar.slices[0].length }" maxFractionDigits="0" />    
    </a></td>
  </c:forEach></tr>
  <tr><th>Failed</th>
  <c:forEach var="bar" items="${bean.bars }" >
    <td><a title="${bar.slices[1].toolTip}" href="${bar.slices[1].URL }">
      <fmt:formatNumber value="${bar.slices[1].length }" maxFractionDigits="0" />    
    </a></td>
  </c:forEach></tr>
</table>
    
