<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--

Author: Petr Troshin
Date: May 2006
Modified by Chris Morris january 2009
Re-design applied by Susy Griffiths January 2009
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Record New Target options page" />
</jsp:include>

<!-- Upload.jsp  -->

<c:set var="breadcrumbs">
<a href='${pageContext.request.contextPath}/'>Home</a> : <a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a>
</c:set>
<pimsWidget:pageTitle icon="target.png"
	title="New Target"
	breadcrumbs="${breadcrumbs}"
 />

<p class="error">${message}</p>

<pimsWidget:box id="box1" title="Choose one of these options:" initialState="open" >
 <pimsForm:form action="/RetrieveDBRecord" method="get" mode="create" enctype="multipart/form-data">
	<pimsForm:formBlock id="blk1">
	<h2>Download Target details from a remote database:</h2>
	Enter the Database ID (e.g. accession number) and select the database from the list. e.g. Q44292 and UNIPROT.<br />
	Then click "Get record"<br /><br />
	  <pimsForm:column1>
   		<pimsForm:text value=" " name="dbid" alias="Database ID" helpText="The unique database identifier for the Target e.g. the accession number" />
	  </pimsForm:column1>
	  <pimsForm:column2>
	<!-- always for target -->
	 <pimsForm:nonFormFieldInfo label="Database" helpText="Select a database from the list" >
		<c:import url="/JSP/databases.jsp">
			<c:param name="forTarget" value="yes"></c:param> 
		</c:import>
	 </pimsForm:nonFormFieldInfo>	
	  </pimsForm:column2>
	</pimsForm:formBlock>
	<pimsForm:formBlock id="blk2">
	 <div style="text-align:right">
        	<input name="Submit" type="submit" value="Get record" onclick="dontWarn()" />
	 </div>
	</pimsForm:formBlock>
 </pimsForm:form>
 <hr />
 <pimsForm:form action="/RetrieveDBRecord" method="post" mode="create" enctype="multipart/form-data">
	<pimsForm:formBlock id="blk3">
	<h2>Upload Target details from a local file:</h2>
	UniProt, SwissProt (new format), GenBank and EMBL text formats are supported.
	<br />
	Navigate to the file and click "Upload file".<br /><br />
		<div style="text-align: left;">
		<input style="float:right; width:auto;"	type="submit" 
			 name="submit" value="Upload file" /> 

	<strong>Choose a file</strong> 
	<input type="file" name="file" />
</div>
 	</pimsForm:formBlock>
 </pimsForm:form>
 <hr />
 <pimsForm:form action="" method="" mode="view" >
  <pimsForm:formBlock id="blk4" >
  <h2>Record a New Target manually: <em>-using a form</em></h2>
   <ul>
	<li>New <a href="${pageContext.request.contextPath}/spot/SpotNewTarget">ORF</a> Target</li>
	<li>New <a href="${pageContext.request.contextPath}/dnatarget/NewDnaTarget">DNA</a> Target</li>
	<li>New <a href="${pageContext.request.contextPath}/naturalsourcetarget/NewNaturalSourceTarget">Natural source</a> Target</li>
   </ul>
  </pimsForm:formBlock>
  <pimsForm:formBlock id="blk5" >
  </pimsForm:formBlock>
  <pimsForm:formBlock id="blk6" >
  </pimsForm:formBlock>
  
 </pimsForm:form>
 	
</pimsWidget:box>

<c:if test="${null ne record}">
<pimsWidget:box id="record" title="The record you downloaded" initialState="open" >
  ${record}
</pimsWidget:box>
</c:if>

</c:catch>
<c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
