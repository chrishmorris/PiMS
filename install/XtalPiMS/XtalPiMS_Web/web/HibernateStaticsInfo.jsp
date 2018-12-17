<%--

--%>
<%@ page session="true" %>
<%@ page import="org.pimslims.persistence.HibernateUtil, java.util.*" %>

<%
		String par =  request.getParameter("param");
   if(par != null && par.equals("reset")) {
	   HibernateUtil.getStatistics().clear();
   }
   org.hibernate.stat.Statistics stats = HibernateUtil.getStatistics();
	 pageContext.setAttribute("stats", stats);
   String style = "listodd";
%>

<%@include file="./WEB-INF/jspf/header-min.jspf"%>

<!-- OLD -->

<br /> ------------------General Infomations------------ <br/>


<table class="list">
	<tr>
		<th>PropertyName</th>
		<th>Value</th>
</tr>
<%

    		out.println("<tr>");
   			out.println("<td>"+"Start Time"+"</td>");
    		out.println("<td>"+new Date(stats.getStartTime())+"</td>");
    		out.println("</tr>");

    		out.println("<tr>");
   			out.println("<td>"+"ConnectCount"+"</td>");
    		out.println("<td>"+stats.getConnectCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"TransactionCount"+"</td>");
    		out.println("<td>"+stats.getTransactionCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"SuccessfulTransactionCount"+"</td>");
    		out.println("<td>"+stats.getSuccessfulTransactionCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"PrepareStatementCount"+"</td>");
    		out.println("<td>"+stats.getPrepareStatementCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"CloseStatementCount"+"</td>");
    		out.println("<td>"+stats.getCloseStatementCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"FlushCount"+"</td>");
    		out.println("<td>"+stats.getFlushCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"OptimisticFailureCount"+"</td>");
    		out.println("<td>"+stats.getOptimisticFailureCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"SessionOpenCount"+"</td>");
    		out.println("<td>"+stats.getSessionOpenCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"SessionCloseCount"+"</td>");
    		out.println("<td>"+stats.getSessionCloseCount()+"</td>");
    		out.println("</tr>");

    		    		   out.println("<tr>");
   			out.println("<td>"+"Running Readable Version Count"+"</td>");
    		out.println("<td>"+org.pimslims.dao.ReadableVersionImpl.getReadableVersionNumber());
    		if(org.pimslims.dao.ReadableVersionImpl.getReadableVersionNumber()>0)
    		for(org.pimslims.dao.ReadableVersionImpl rv:org.pimslims.dao.ReadableVersionImpl.getCurrentVersions())
    		{	out.println("<br/>");
    			out.println(Arrays.asList(rv.getStackTrace()));
    			out.println("<br/>");
    		}
    		out.println("</td></tr>");

%>
</table>

 ------------------Collection--------------- <br/>

<table class="list">
	<tr>
		<th>PropertyName</th>
		<th>Value</th>
</tr>
<%


    		    		out.println("<tr>");
   			out.println("<td>"+"CollectionFetchCount"+"</td>");
    		out.println("<td>"+stats.getCollectionFetchCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"CollectionLoadCount"+"</td>");
    		out.println("<td>"+stats.getCollectionLoadCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"CollectionRecreateCount"+"</td>");
    		out.println("<td>"+stats.getCollectionRecreateCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"CollectionRemoveCount"+"</td>");
    		out.println("<td>"+stats.getCollectionRemoveCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"CollectionUpdateCount"+"</td>");
    		out.println("<td>"+stats.getCollectionUpdateCount()+"</td>");
    		out.println("</tr>");

%>
</table>

 ------------------Entity--------------- <br/>

<table class="list">
	<tr>
		<th>PropertyName</th>
		<th>Value</th>
</tr>
<%

    		    		out.println("<tr>");
   			out.println("<td>"+"EntityDeleteCount"+"</td>");
    		out.println("<td>"+stats.getEntityDeleteCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"EntityFetchCount"+"</td>");
    		out.println("<td>"+stats.getEntityFetchCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"EntityInsertCount"+"</td>");
    		out.println("<td>"+stats.getEntityInsertCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"EntityLoadCount"+"</td>");
    		out.println("<td>"+stats.getEntityLoadCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"EntityUpdateCount"+"</td>");
    		out.println("<td>"+stats.getEntityUpdateCount()+"</td>");
    		out.println("</tr>");


%>
</table>
 ------------------Query--------------- <br/>

<table class="list">
	<tr>
		<th>PropertyName</th>
		<th>Value</th>
</tr>
<%

    		    		out.println("<tr>");
   			out.println("<td>"+"QueryExecutionCount"+"</td>");
    		out.println("<td>"+stats.getQueryExecutionCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"QueryExecutionMaxTime"+"</td>");
    		out.println("<td>"+stats.getQueryExecutionMaxTime()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"QueryExecutionMaxTimeQueryString"+"</td>");
    		out.println("<td>"+stats.getQueryExecutionMaxTimeQueryString()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"QueryCacheHitCount"+"</td>");
    		out.println("<td>"+stats.getQueryCacheHitCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"QueryCacheMissCount"+"</td>");
    		out.println("<td>"+stats.getQueryCacheMissCount()+"</td>");
    		out.println("</tr>");


    		    		out.println("<tr>");
   			out.println("<td>"+"QueryCachePutCount"+"</td>");
    		out.println("<td>"+stats.getQueryCachePutCount()+"</td>");
    		out.println("</tr>");


%>
</table>

------------------SecondLevelCache General------------ <br/>

<table class="list">
	<tr>
		<th>PropertyName</th>
		<th>Value</th>
</tr>
<%


    		    		out.println("<tr>");
   			out.println("<td>"+"SecondLevelCacheHitCount"+"</td>");
    		out.println("<td>"+stats.getSecondLevelCacheHitCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"SecondLevelCacheMissCount"+"</td>");
    		out.println("<td>"+stats.getSecondLevelCacheMissCount()+"</td>");
    		out.println("</tr>");

    		    		out.println("<tr>");
   			out.println("<td>"+"SecondLevelCachePutCount"+"</td>");
    		out.println("<td>"+stats.getSecondLevelCachePutCount()+"</td>");
    		out.println("</tr>");


%>
</table>


<br /> ------------------SecondLevelCache Details------------ <br/>

<table class="list">
	<tr>
		<th>CategoryName</th>
		<th>ElementCountInMemory</th>
		<th>HitCount </th>
		<th>MissCount</th>
		<th>PutCount</th>
		<th>SizeInMemory </th>
</tr>
<%
    	Collection<String> SecondLevelCacheRegionNames=Arrays.asList(stats.getSecondLevelCacheRegionNames());
    	long totalSizeInMemory=0;

    	for(String SecondLevelCacheRegionName: SecondLevelCacheRegionNames)
    	{	org.hibernate.stat.SecondLevelCacheStatistics scStats=stats.getSecondLevelCacheStatistics(SecondLevelCacheRegionName);
    		if(scStats.getSizeInMemory()>0)
    		{
 				style = style.equals("listodd") ? "listeven" : "listodd";
    		out.println("<tr class='"+style+"'>");
   			out.println("<td>"+SecondLevelCacheRegionName+"</td>");
    		out.println("<td>"+scStats.getElementCountInMemory()+"</td>");
    		out.println("<td>"+scStats.getHitCount()+"</td>");
    		out.println("<td>"+scStats.getMissCount()+"</td>");
    		out.println("<td>"+scStats.getPutCount()+"</td>");
    		totalSizeInMemory+=scStats.getSizeInMemory();
    		out.println("<td>"+scStats.getSizeInMemory()/1000+"k</td>");
    		out.println("</tr>");
    		}
    	}

    	out.println("SecondLevelCache totalSizeInMemory: "+totalSizeInMemory/1000+"k");
%>
</table>

 <br /> ------------------Entity Details------------ <br/>

<table class="list">
	<tr>
		<th>EntityName</th>
		<th>LoadCount</th>
		<th>DeleteCount</th>
		<th>FetchCount </th>
		<th>InsertCount</th>
		<th>UpdateCount </th>
	</tr>
<%
      Collection<String> entityNames=Arrays.asList(stats.getEntityNames());

    	for(String entityName: entityNames)
    	{
    		org.hibernate.stat.EntityStatistics entityStats=stats.getEntityStatistics(entityName);
    		if(entityStats.getLoadCount()
    		+entityStats.getDeleteCount()
    		+entityStats.getFetchCount()
    		+entityStats.getInsertCount()
    		+entityStats.getUpdateCount()
    		>0)
    		{
 				style = style.equals("listodd") ? "listeven" : "listodd";
    		out.println("<tr class='"+style+"'>");
   			out.println("<td>"+entityName+"</td>");
    		out.println("<td>"+entityStats.getLoadCount()+"</td>");
    		out.println("<td>"+entityStats.getDeleteCount()+"</td>");
    		out.println("<td>"+entityStats.getFetchCount()+"</td>");
    		out.println("<td>"+entityStats.getInsertCount()+"</td>");
    		out.println("<td>"+entityStats.getUpdateCount()+"</td>");
    		out.println("</tr>");
    		}
    	}
%>
</table>


 <br /><br /> ------------------Query Details------------ <br/>

<table class="list">
	<tr>
		<th>QueryName</th>
		<th>ExecutionCount</th>
		<th>ExecutionRowCount</th>
		<th>ExecutionAvgTime </th>
		<th>ExecutionMaxTime</th>
		<th>CacheHitCount </th>
		<th>CacheMissCount </th>
		<th>CachePutCount</th>
	</tr>

<%
		Collection<String> QueryNames=Arrays.asList(stats.getQueries());
    	for(String QueryName: QueryNames)
    	{	org.hibernate.stat.QueryStatistics queryStats=stats.getQueryStatistics(QueryName);
    		if(queryStats.getExecutionCount()>0)
    		{
				style = style.equals("listodd") ? "listeven" : "listodd";
    		out.println("<tr class='" +style + "'>");
   			out.println("<td>"+QueryName+"</td>");
    		out.println("<td>"+queryStats.getExecutionCount()+"</td>");
    		out.println("<td>"+queryStats.getExecutionRowCount()+"</td>");
    		out.println("<td>"+queryStats.getExecutionAvgTime()+"</td>");
    		out.println("<td>"+queryStats.getExecutionMaxTime()+"</td>");
    		out.println("<td>"+queryStats.getCacheHitCount()+"</td>");
    		out.println("<td>"+queryStats.getCacheMissCount()+"</td>");
    		out.println("<td>"+queryStats.getCachePutCount()+"</td>");
    		out.println("</tr>");
    		}
    	}
%>
</table>


<form method="post" action="/JSP/Info" class="singlebutton cancel">
<input type="hidden" name="param" value="reset" />
	<input class="button"	name="button" value="Reset Hibernate Statistic"
		onclick="return confirm('Are you sure you want to reset statistic?');"
		type="submit" />
</form>



<jsp:include page="/JSP/core/Footer.jsp" />

