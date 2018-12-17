<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: Susy Griffiths
Date: 24-Apr-2009
Servlets: org.pimslims.servlet.standard.ViewHost.java

-->
<%-- bean declarations --%>
<jsp:useBean id="hostBean" scope="request" type="org.pimslims.presentation.HostBean" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Host: ${hostBean.hostName}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- ViewHost.jsp --%>
<%--TODO need Host List --%>
<h2><a href='${pageContext.request.contextPath}/read/HostList'>Hosts</a> : Host: ${hostBean.hostName} </h2>
<br /><br />
<%--TODO 
	<pimsWidget:copyLink bean="${hostBean}" />
	<pimsWidget:deleteLink bean="${hostBean}" />
--%>
<pimsWidget:box id="details" title="Details" initialState="open" >
	<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
		<pimsForm:formBlock id="blk1">
		 <pimsForm:column1>
		 <%--TODO need valid parameter names--%>
		 	<pimsForm:text name="${hostBean.hostHook}:${hostBean.hostName}" value="${hostBean.hostName}" alias="Name" helpText="The name of the Host" validation="required:true"  />
		 </pimsForm:column1>
		 <pimsForm:column2>
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.hostOrganism}" value="${hostBean.hostOrganism}" alias="Organism" helpText="The Host species" />
			<%--TODO needs to be an MRU for organisms something like:
			<pimsForm:mruSelect  hook="${hostBean.hostHook}" rolename="${Host['PROP_SPECIES']}" required="false" alias="Organism" helpText="The Host species" />
			 --%>
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.strain}" value="${hostBean.strain}" alias="Strain" helpText="The Host strain" />
		 </pimsForm:column2>
		</pimsForm:formBlock>
		<pimsForm:formBlock id="blk2">
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.genotype}" value="${hostBean.genotype}" alias="Genotype" helpText="The genotype of the strain" />
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.harbouredPlasmids}" value="${hostBean.harbouredPlasmids}" alias="Harboured plasmids" helpText="Any plasmids harboured by the strain" />
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.antibioticRes}" value="${hostBean.antibioticRes}" alias="Antibiotic resistances" helpText="Antibiotic resitstances of the strain" />	 	
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.selectableMarkers}" value="${hostBean.selectableMarkers}" alias="Selectable markers" helpText="The strain's selectable markers" />	 	
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.suppliers}" value="${hostBean.suppliers}" alias="Suppliers" helpText="e.g. name of commercial supplier(s),  \'in house\', etc." />	 	
			<pimsForm:text name="${hostBean.hostHook}:${hostBean.hostUse}" value="${hostBean.hostUse}" alias="Use" helpText="What the host can be used for e.g. general plasmid cloning" />	 	
			<pimsForm:textarea validation="required:false" name="${hostBean.hostHook}:${hostBean.comments}" alias="Comments" helpText="Comments e.g. detailed description of the strain or other useful information" ><c:out value="${hostBean.comments}" />
			</pimsForm:textarea>
		</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>

<%--TODO Need to be able to create stocks of the Host -needs a RefSample --%>
<c:set var="newStock"><a href="javascript:void(0)">New Host Stock</a></c:set>
<pimsWidget:box id="stocks" title="Host Stocks" extraHeader="${newStock}" initialState="closed" >
<c:choose>
	    <c:when test="${empty stockBeans}">
	       There are no stocks recorded for Host: ${hostBean.hostName}  <br/>
	    </c:when>
	    <c:otherwise>
	    	<%--TODO Need a list of stocks, location, box, position, scientist, date
	        <jsp:include page="/JSP/standard/HostStocks.jsp"></jsp:include>
	        --%>
	    </c:otherwise>
	</c:choose>
</pimsWidget:box>

<pimsWidget:box id="stocksv2" title="Host Stocks V2" extraHeader="${newStock}" initialState="closed" >
	    	<%--TODO Need a list of stocks, location, box, position, scientist, date
	        <jsp:include page="/JSP/standard/HostStocks.jsp"></jsp:include>
	        --%>
	     <table>
	     	<tr><th>Date</th><th>Stored By</th><th>Num. aliquots</th><th>Location</th><th>Box</th><th>Positions</th><th></th></tr>
	     </table>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
