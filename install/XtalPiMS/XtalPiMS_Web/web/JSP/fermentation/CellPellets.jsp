<%-- @Author Petr Troshin aka pvt43 --%>
<%--
This page displays a list of Cell Pellets
on the second tab of a Culture page
--%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

suspected obsolete
<fmt:setLocale value='en_UK' />

<table border="2px" bgcolor="white">
<tr>
<th></th>
<th>Number</th>
<th>Barcode</th>
<th>Cell Mass</th>
<th>Location</th>
<th>Tower</th>
<th>Box</th>
<th>Used</th>
<th>Membrane Prep</th>
<th>Done by</th>
<th>Date</th>
<th>Prep Type</th>
<th>Construct</th>
</tr>
<tr>
<td><a href="${pageContext.request.contextPath}/Construct/${row.hook}">${row.name}"</a></td>
<td>Number</td>
<td>Barcode</td>
<td>Cell Mass</td>
<td>Location</td>
<td>Tower</td>
<td>Box</td>
<td>Used</td>
<td>Membrane Prep</td>
<td>Done by</td>
<td>Date</td>
<td>Prep Type</td>
<td>Construct</td>
</tr>
</table>

<display:table class="list" id="row" name="${requestScope.construct.deepFrozenCultures}"
  							 defaultsort="1" pagesize="${param.pagerSize}">


 <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="" >
     <a href="${pageContext.request.contextPath}/Construct/${row.hook}">${row.name}"</a>
 </display:column>

 <c:if test="${param.rowNum == 'on'}">
  	 <display:column style="padding:2px 0 0 3px;width:20px;" title="Row num" >${row_rowNum}</display:column>
 </c:if>

   		<display:column escapeXml="true"  property="name" title="Name" />
   		<display:column title="Designed By" >
			<pims:getter version="${row.version}" hook="${row.designedBy}" attributes="name" delimiter=" " />
   		</display:column>
   		<display:column title="Location" >
				<pims:getter version="${row.version}" hook="${row.location1}" attributes="name" />
   		</display:column>
   		<display:column  title="Box" >
				<pims:getter version="${row.version}" hook="${row.box1}" attributes="name" />
   		</display:column>
 			<display:column escapeXml="true"  property="position1" title="Position" />
			<display:column style="padding:2px 0 0 3px;width:20px;" title="VNTI&nbsp;map" >
			<c:set var="file" value="${row.VNTIMap}"/>
				<c:choose>
					<c:when test="${! empty file}">
		        <a href="${pageContext.request.contextPath}/read/ViewFile/${file.hook}/${file.name}" title="view file" >Map</a>
					</c:when>
						<c:otherwise></c:otherwise>
				</c:choose>
			</display:column>

	<display:setProperty name="paging.banner.group_size" value="15" />
	<display:setProperty name="export.decorated" value="false" />
	<display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
	<display:setProperty name="export.amount" value="page" /> <!-- set list if want to export all -->
	<display:setProperty name="paging.banner.item_name" value="Record" />
  <display:setProperty name="paging.banner.items_name" value="Records" />
  </display:table>

<a href="${pageContext.request.contextPath}/Addnew/">Add new Pellet</a>

<h3>Add pellets</h3>
<table>
	<tr>
		<td>
		Number of pellets
		</td>
		<td>
		<input type="text" name="pelletsNum" id="pelletsNum" style="width: 10em;" />
		</td>
	</tr>
</table>

Location-Tower-Box
Deep-Frozen Culture
Date started 
Date Finished
Done By 
Saved By



<!-- OLD -->
