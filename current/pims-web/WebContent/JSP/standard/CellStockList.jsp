<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%-- 
Author: susy
Date: 15 Dec 2009
Servlets: org/pimslims/servlet/standard/CellStockList.java

-->
<%-- bean declarations e.g.: --%>
<jsp:useBean id="csBeans" scope="request" type="java.util.Collection<CellStockBean>" />


<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Cell Stocks' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
    <c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}/">Home</a> : Cell Stocks
    </c:set>
    <c:set var="icon" value="blank.png" />
    <c:set var="title" value="Cell stocks"/>
    <c:set var="actions">
        <pimsWidget:linkWithIcon 
                    icon="misc/help.gif" 
                    url="${pageContext.request.contextPath}/help/experiment/HelpCellStocks.jsp#listCS"
                    text="Help"/>
    </c:set>    
    <pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>

  <c:choose>
   <c:when test="${empty  csBeans}">
     <h4><c:out value="No Cell stocks are recorded in PiMS" /></h4>
  </c:when>
   <c:otherwise>
    <pimsWidget:pageControls>
    </pimsWidget:pageControls>

<%--Attempt to make like other PiMS tables
To be able to export values which use pims tags e.g. pimsWidget:link Need a separate display:column with media="xml excel csv" 
and just property="bean attribute name".
Default is media="all" so for strings omit the media attribute--%>

<pimsWidget:box title="Cell stocks" initialState="open">
  <display:table class="list" id="mytable" name="${csBeans}"
                              pagesize="${pagesize}"
                             sort="list" 
                             partialList="true" size="${resultSize}">

    <display:column escapeXml="false"  style="padding:2px 4px 0 3px;width:20px;" media="html" sortable="true" title="Stock name" >
        <pimsWidget:link bean="${mytable}"/>
     </display:column>
    <display:column escapeXml="true"  style="padding:2px 4px 0 3px;width:20px;" media="xml excel csv" sortable="true" title="Stock name" property="stockName" />

     <display:column escapeXml="false" title="Box" style="width:20px;" media="html" sortable="true" headerClass="sortable" >
        <c:if test="${!empty mytable.rack}">
            <pimsWidget:linkWithIcon icon="types/small/holder.gif" url="${pageContext.request.contextPath}/View/${mytable.pimsHolderHook}" text="${mytable.rack}" />
        </c:if>
     </display:column>
     <display:column escapeXml="true" media="xml excel csv" sortable="true" title="Box" property="rack" />

     <display:column escapeXml="true" title="Position" style="width:10px;" property="rackPosition" sortable="true" headerClass="sortable" >

        <pimsWidget:dateLink date="${mytable.experimentDate}"  />
     </display:column>
     <display:column escapeXml="true" media="xml excel csv" sortable="true" title="Date" property="dateAsString" />

     <display:column escapeXml="true" title="Description" property="description" maxLength="100" />
     <display:column escapeXml="true" title="Strain"  property="strain" sortable="true" headerClass="details" maxLength="100" />
     <display:column escapeXml="true" title="Antibiotic Resistance" property="hostAbRes" sortable="true" headerClass="sortable" />

     <display:column escapeXml="false" title="Plasmid" sortable="true" headerClass="sortable" media="html" >
        <c:if test="${!empty mytable.plasmid}">
            <pimsWidget:linkWithIcon icon="" url="${pageContext.request.contextPath}/View/${mytable.pimsPlasmidHook}" text="${mytable.plasmid}" />
        </c:if>
     </display:column>       
     <display:column escapeXml="true" title="Plasmid" media="xml excel csv" property="plasmid" sortable="true" headerClass="sortable" />

     <display:column escapeXml="false" sortable="true" headerClass="sortable" title="Vector" media="html" >
        <c:if test="${!empty mytable.vector}">
            <pimsWidget:linkWithIcon icon="" url="${pageContext.request.contextPath}/View/${mytable.pimsVectorHook}" text="${mytable.vector}" />
        </c:if>
     </display:column>       
     <display:column escapeXml="true" title="Vector" media="xml excel csv" property="vector" sortable="true" headerClass="sortable" />

     <display:column escapeXml="true"  property="initials" sortable="true" headerClass="sortable" title="Stored by"/>
     <display:setProperty name="paging.banner.group_size" value="10" />
     <display:setProperty name="export.decorated" value="true" />
    <display:setProperty name="paging.banner.item_name" value="Cell stock" />
    <display:setProperty name="paging.banner.items_name" value="Cell stock" />
    <display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
    <display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
  </display:table>
</pimsWidget:box>

</c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
