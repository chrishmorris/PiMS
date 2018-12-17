<%--
Author: Petr Troshin aka pvt43
Date: 8 May 2008
Servlets:  This is to be included in the result part (to display the search results) of CustomSearch.jsp

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<jsp:useBean id="results" scope="request" type="java.util.Collection" />
suspected obsolete
 <display:table class="list" id="mytable" name="${results}"  sort="list" pagesize="${pagesize}" >


   	<display:column escapeXml="false"  style="padding:2px 0 0 3px; white-space: nowrap; width:20px; " media="html" title="" >
	     	<a href="${pageContext.request.contextPath}/ViewPrimer/${mytable.hook}">${mytable.name}</a>
		</display:column>

		<display:column title="Location" escapeXml="true" sortable="true" headerClass="sortable" >
					<pims:getter version="${mytable.sample._Version}" hook="${mytable.location}" attributes="name" />
		</display:column>
		<display:column title="Box" escapeXml="true" sortable="true" headerClass="sortable" >
				<pims:getter version="${mytable.sample._Version}" hook="${mytable.box}" attributes="name" />
		</display:column>
		<display:column title="Position" property="position" escapeXml="true" sortable="true" headerClass="sortable" />

		<display:column media="html" title="Cloning Design Experiment" sortable="true" headerClass="sortable" >
			<a href="${pageContext.request.contextPath}/FullPrimerForm/${mytable.cloningDesignExperiment._Hook}">${mytable.cloningDesignExperiment.name}</a>
		</display:column>
		<%--Generate alternatives for export --%>
		<display:column media="excel csv xml" title="Cloning Design Experiment" sortable="true" headerClass="sortable" >
			${mytable.cloningDesignExperiment.name}
		</display:column>

	

 </display:table>
