<%--
  Brick name: newTarget
  Rows: 2
  Columns: 1
  
  Offer a variety of ways to create a new target.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3>Create a new target</h3>
  <div class="brickcontent">
  
 <%-- <img src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/target.png"  
  	style="position:absolute;right:0.5em;top:2em;opacity:0.5;filter:alpha(opacity=0)"/> --%>
  
	<form action="${pageContext.request.contextPath}/RetrieveDBRecord" method="get"  class="grid create" style="" onsubmit="return validateFormFields(this)" enctype="multipart/form-data" >
		<input type="hidden" name="_anchor" value="box1" />
		<p style="font-weight:bold;font-size:90%">Download target details:</p>
		<ul style="text-align:left;"><li>Enter the Database ID</li>
		<li>Select a database from the list</li>
		<li>Click "Get record"</li></ul>
		<![if !IE]><br/><![endif]>
		<div style="text-align:right;">
		<label style="float:left;font-weight:bold; padding-left: .5em;" for="dbid" title="The unique database identifier for the Target e.g. the accession number">Database ID</label>
		<input  style="width:9.5em; margin-bottom: 5px;" type="text" name="dbid" id="dbid"  value=" " onchange="onEdit();" />
		</div>

		<div style="text-align:right">
			<label  style="float:left;font-weight:bold; padding-left: .5em;" for="database" title="Select a database from the list">Database</label>
			<c:import url="/JSP/databases.jsp">
                <c:param name="forTarget" value="yes"></c:param> 
            </c:import>
		</div>
		<div style="text-align:right">
	        	<input name="Submit" type="submit" value="Get record" onclick="dontWarn()" />
		</div>
</form>
</div>


