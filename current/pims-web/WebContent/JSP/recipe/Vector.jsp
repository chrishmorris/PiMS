<%--
Custom view for a refsample
Servlet: org.pimslims.standard.ViewRefSample
Author: Marc Savitsky
Date: January 2009
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page import="org.pimslims.model.sample.*" %>
<c:catch var="error">
<jsp:useBean id="categories" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />
<jsp:useBean id="components"  scope="request"  type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="refsample"  scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<pims:import className="<%= RefSample.class.getName() %>" />
<pims:import className="<%= ReagentCatalogueEntry.class.getName() %>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Details of Vector: ${refsample.name}" />
    <jsp:param name="mayUpdate" value='${refsample.mayUpdate}' />
</jsp:include>
<!-- ViewVector.jsp -->

<script type="text/javascript">
function validateForm(theForm){
  var reason = "";
  for (var i = 0; i < theForm.elements.length; ++i) {
      var element = theForm.elements[i];
      if (element.name.match("name"))
      	reason += validateName(element.value);
  }
  
  if (reason != "") {
    alert("Some fields need correction:\n" + reason);
    return false;
  }
  return true;
}

function validateName(value){
  if (value=="") 
  	return "A Name is required\n";
  return "";
}

function onLoadPims() {
  var theForm = document.updateRecipe;
  var theForm = document.getElementById("updateRecipe");

    if (null != theForm){
  
      for(j=0;j<theForm.elements.length;j++){
        var formObj=theForm.elements[j];
     }
  }
}
</script>

<c:set var="actionHTML" value="<%= org.pimslims.presentation.AttributeToHTML.ACTION %>" />

<c:set var="breadcrumbs">
	<%--TODO Need to link to list of Vectors e.g. Search restricted to Vectors? --%>
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample">Recipes</a>
</c:set>
<c:set var="icon" value="recipe.png" />        
<c:set var="title" value="${refsample.name}"/>
<c:set var="actions">
	<pimsWidget:copyLink bean="${refsample}" />
	<pimsWidget:deleteLink bean="${refsample}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />

<%--
TESTING SUSY:<br />
<c:forEach items="${vector.values}" var="vals" > 
 <c:out value="${vals}"/><br />
</c:forEach>
--%>
<pimsWidget:box title="Vector Details" initialState="open">
	<pimsForm:form action="/Update" method="post" mode="view">
		<pimsForm:formBlock>
			<pimsForm:column1>
				<pimsForm:text name="${refsample.hook}:${RefSample['PROP_NAME']}" 
						 	value="${refsample.name}"
						   	alias="Name" validation="required:true" />
			</pimsForm:column1>
			<pimsForm:column2>
				<%--<pimsForm:text name="${refsample.hook}:${RefSample['PROP_PH']}"  alias="pH" value="${refsample.values['ph']}" />
                <pimsForm:text name="${refsample.hook}:${RefSample['PROP_SAMPLECOMPONENTS']}"  alias="Length (bp)" value="${refsample.values['refComponent']}" />--%>
<%--TODO for version 4.2 ATTEMPT TO MAKE THIS VALUE EDITABLE
                <pimsForm:text name="${vector.hook}:${Construct['PROP_DETAILS']}"  alias="Length (bp)" value="${vector.length}" helpText="The length of the vector sequence" />
 --%>
<%--NON EDITABLE FIELD SET FROM VAUE IN XML FILE OR SEQUENCE IF SET  --%>
                <pimsForm:nonFormFieldInfo label ="Length (bp)" >
                    <c:out value="${vector.length}" />
                </pimsForm:nonFormFieldInfo>
           
                <pimsForm:select name="${refsample.hook}:${RefSample['PROP_ACCESS']}" alias="Lab Notebook" helpText="The Lab Notebook this vector belongs to">
	     			<c:forEach var="p" items="${accessObjects}">
           				<pimsForm:option optionValue="${p.hook}" currentValue="${refsample.access.hook}" alias="${p.name}"/>
         			</c:forEach>
       			</pimsForm:select>
			</pimsForm:column2>
			
		</pimsForm:formBlock>
		<pimsForm:formBlock>
			<pimsForm:text name=""  alias="Function" value="" />			
     		<pimsForm:textarea alias="Details" name="${refsample.hook}:${RefSample['PROP_DETAILS']}" ><c:out value="${refsample.values['details']}" /></pimsForm:textarea>     		
     	</pimsForm:formBlock>
     		 	 			
     		<pimsForm:editSubmit/>
     		<c:if test="${!mayUpdate ne false}" >
     		 		You cannot change this recipe. Either it has been used, or you do not have permission.
     		 		Owner: ${owner}
     		</c:if>				  		
	</pimsForm:form>
</pimsWidget:box>
        

<%--Associateded Categories not updatable -only Vector so don't display, pass as hidden --%>
<%--Sample component details are part of Vector View so don't need this box --%>

<pimsWidget:multiRoleBox title="Suppliers" objectHook="${refsample.hook}" roleName="refSampleSources" mayAdd="false"  />

<%--Vector features --%>
<c:if test="${!empty vector}" >
<pimsWidget:box title="Features" initialState="open" >
	<pimsForm:form action="/Update" method="post" mode="view">
	<table>
	<tr>
    <c:if test="${mayUpdate }"><th>
        <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.molecule.MoleculeFeature?molecule=${vector.hook }"
          target="add" >
        Add feature...</a>
    </th></c:if>
    <th>Name</th><th>Type</th><th>Start</th><th>End</th><th>Details</th></tr>
    <tr><c:if test="${mayUpdate }"><td></td></c:if>
      <th colspan="5" align="left" >MARKERS <c:if test="${empty vector.markers}">- None</c:if> </th></tr>
    <c:forEach items="${vector.markers}" var="feature" >
      <tr>
          <c:if test="${mayUpdate }"><td><pimsWidget:deleteLink bean="${feature }" /></td>
          </c:if><td>${feature.name}</td><td>${feature.featureType}</td><td>${feature.startSeq}</td><td>${feature.endSeq}</td><td>${feature.details}</td></tr>
    </c:forEach>
    <tr><c:if test="${mayUpdate }"><td></td></c:if>
       <th colspan="5" align="left" >PROMOTERS <c:if test="${empty vector.promoters}">- None</c:if></th></tr>
    <c:forEach items="${vector.promoters}" var="feature" >
       <tr>
          <c:if test="${mayUpdate }"><td><pimsWidget:deleteLink bean="${feature }" /></td>
          </c:if>
       <td>${feature.name}</td><td>${feature.featureType}</td><td>${feature.startSeq}</td><td>${feature.endSeq}</td><td>${feature.details}</td></tr>
    </c:forEach>
    <tr><c:if test="${mayUpdate }"><td></td></c:if>
      <th colspan="5" align="left" >RESISTANCES <c:if test="${empty vector.resistances}">- None</c:if></th></tr>
    <c:forEach items="${vector.resistances}" var="feature" >
       <tr>
          <c:if test="${mayUpdate }"><td><pimsWidget:deleteLink bean="${feature }" /></td>
          </c:if><td>${feature.name}</td><td>${feature.featureType}</td><td>${feature.startSeq}</td><td>${feature.endSeq}</td><td>${feature.details}</td></tr>
    </c:forEach>
    <tr><th colspan="5">&nbsp;</th></tr>
	</table>
	</pimsForm:form>	
</pimsWidget:box>
<%--Put sequence in a separate box so it doesn't distort the view
    <c:if test="${!empty vector.sequence}" > --%>
    <pimsWidget:box title="Vector Sequence" initialState="closed" >
        <pimsForm:form action="/Update" method="post" mode="view">
            <pimsForm:textarea  name="${vector.hook}:seqString" alias="DNA SEQUENCE" id="${vector.hook}:seqString" helpText="The DNA sequence of the Vector" 
                validation="dnaSequence:true" onchange="this.value=cleanSequence2(this.value);">
                <pims:sequence sequence="${vector.sequence}" format='DEFAULT' escapeStyle="NONE" />
            </pimsForm:textarea>            
        <pimsForm:formBlock>                
            <pimsForm:editSubmit/>
            <c:if test="${!mayUpdate ne false}" >
                    You cannot change this recipe. Either it has been used, or you do not have permission.
                    Owner: ${owner}
            </c:if>             
        </pimsForm:formBlock>           
        </pimsForm:form>
    </pimsWidget:box>
    <%--</c:if>--%>
</c:if>

<!-- Associated Samples... -->
<c:choose>
 	<c:when test="${not empty stocks}">
		<c:set var="stockBoxState" value="closed" scope="page" />
	</c:when><c:otherwise>
		<c:set var="stockBoxState" value="open" scope="page" />
	</c:otherwise>
</c:choose>

<c:set var="sampleclass"><%= Sample.class.getName() %></c:set>
<c:set var="samplerefsample"><%= Sample.PROP_REFSAMPLE %></c:set>
<c:set var="samplename"><%= Sample.PROP_NAME %></c:set>
<c:set var="sampleproject"><%= Sample.PROP_ACCESS %></c:set>

<%--TODO Use sample beans to make this --%>
<pimsWidget:box title="Available stocks of this Vector" initialState="${stockBoxState}"  >
		<c:choose>
 			<c:when test="${not empty stocks}">
				<ul>
					<c:forEach items="${stocks}" var="stock" >
						<li><pimsWidget:link bean="${stock}" /></li>
					</c:forEach>
				</ul>
 			</c:when>
			<c:otherwise>
				No Stocks of this Vector
			</c:otherwise>
		</c:choose>
  
    <pimsForm:form action="/update/CreateSampleFromRefSample" method="post" mode="edit">
        <input type="hidden" name="METACLASSNAME" id="METACLASSNAME" value="${sampleclass}" />
        <input type="hidden" name="${sampleclass}:${samplerefsample}" value="${refsample.hook}">
        <pimsForm:formBlock>
                Record new stock for <c:out value="${refsample.name}" /><br/><br/>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:column1>
                <pimsForm:text name="${sampleclass}:${samplename}" alias="Name"
                    validation="required:true, unique:{obj:'org.pimslims.model.sample.Sample',prop:'name'}" />
            </pimsForm:column1>
            <pimsForm:column2>
            	<pimsForm:select name="${sampleclass}:${sampleproject}" alias="Lab Notebook" helpText="The Lab Notebook this recipe belongs to">
	      			<c:forEach var="p" items="${accessObjects}">
           				<option value="${p.hook}" ><c:out value="${p.name}" /></option>
         			</c:forEach>
       			</pimsForm:select>
                <input type="submit" value="Create" />
            </pimsForm:column2>
        </pimsForm:formBlock>
    </pimsForm:form>
	
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>


<pimsWidget:files bean="${refsample}"  />

<pimsWidget:notes bean="${refsample}"  />

<jsp:include page="/JSP/core/Footer.jsp" />


