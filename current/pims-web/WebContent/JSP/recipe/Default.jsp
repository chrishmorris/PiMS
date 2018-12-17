<%--
Custom view for a refsample
Servlet: org.pimslims.standard.ViewRefSample
Author: Marc Savitsky
Date: January 2009
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
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
    <jsp:param name="HeaderName" value="Recipe for stock chemical/solution: ${refsample.name}" />
    <jsp:param name="mayUpdate" value='${refsample.mayUpdate}' />
</jsp:include>
<!-- ViewRefSample.jsp -->

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

function validConcentration(amount, alias) {
  if (amount == "")	return ""; 
  // split into value and units
  var unit = "";
  var val = "";
  // split the amount into value and units
  for (var i = amount.length; i > 0; i--) {
    if (IsNumeric(amount.charAt(i-1))) 
    	val = amount.charAt(i-1)+val;
    if (!IsNumeric(amount.charAt(i-1))) 
        unit = trim(amount.charAt(i-1)+unit);
  } 
  //alert("validateAmount  ["+val+":"+unit+"]");
  if (val.indexOf('-') >= 0)
    return alias+" can not be negative";
  if (unit!="M" && unit!="mM" && unit!="uM" && unit!="nM") 
    return alias+": units should be Molar: M, mM etc.";

    <%--PIMS-1317 mg/ml not accepted
    if (unit!="M" && unit!="mM" && unit!="mg/ml") 
      return alias+": units should be one of M mM or mg/ml";
    --%>    
  return "";
}

function IsNumeric(stringToTest) {
  regEx = /^[0-9-.]*$/;
  return regEx.test(stringToTest); 
}

function onLoadPims() {
  var theForm = document.updateRecipe;
  var theForm = document.getElementById("updateRecipe");
  
  if(null != theForm){
    for(j=0;j<theForm.elements.length;j++){
        var formObj=theForm.elements[j];
    
        if (formObj.name.indexOf('concentration') > 0) {
      
         var value = formObj.value;
         var unit = formObj.name.replace("concentration", "unit");
         var amount = formObj.name.replace("concentration", "value");
      
        for(i=0;i<theForm.elements.length;i++) {
          var myObj=theForm.elements[i];
        
          if (myObj.name == unit) 
            myObj.value = getUnits(value);
        
          if (myObj.name == amount) 
           myObj.value = getAmount(value);
        }
       }
    }
  }
}

function getUnits(valueStr) {
  var unitStr = "";
  for (var i = 0; i < valueStr.length; i++) {
    if (!IsNumeric(valueStr.charAt(i))) {
        unitStr = valueStr.substring(i);
        break;
    }
  }
  return unitStr;
}

function getAmount(valueStr) {
  var amtStr = "";
  for (var i = valueStr.length; i > 0; i--) {
    if (IsNumeric(valueStr.charAt(i-1))) 
        amtStr = trim(valueStr.charAt(i-1)+amtStr);
  }
  return amtStr;
}


</script>

<c:set var="actionHTML" value="<%= org.pimslims.presentation.AttributeToHTML.ACTION %>" />

<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample">Recipes</a>
</c:set>
<c:set var="icon" value="recipe.png" />        
<c:set var="title" value="${refsample.name}"/>
<c:set var="actions">
	<pimsWidget:copyLink bean="${refsample}" />
	<pimsWidget:deleteLink bean="${refsample}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />


<pimsWidget:box title="Basic Details" initialState="open">
	<pimsForm:form action="/Update" method="post" mode="view">
		<pimsForm:formBlock>
			<pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${refsample.hook}:pageNumber" value="${refsample.values['pageNumber']}"
    />
			<pimsForm:text name="${refsample.hook}:${RefSample['PROP_NAME']}" 
						 	value="${refsample.name}"
						   	alias="Name" validation="required:true" />

			<pimsForm:text name="${refsample.hook}:${RefSample['PROP_PH']}"  alias="pH" value="${refsample.values['ph']}" />
			<pimsForm:nonFormFieldInfo label="Lab Notebook"><c:out value="${refsample.access.name}" /></pimsForm:nonFormFieldInfo>
            
       			
			</pimsForm:column1>
			<pimsForm:column2>
			
     		<pimsForm:textarea alias="Details" name="${refsample.hook}:${RefSample['PROP_DETAILS']}" ><c:out value="${refsample.values['details']}" /></pimsForm:textarea>
     		
			</pimsForm:column2>
     		</pimsForm:formBlock>
     		
     		 		<pimsForm:editSubmit/>
     		 		<c:if test="${!mayUpdate ne false}" >
     		 		<p class="empty_parameters">You cannot change this recipe. Either it has been used, or you do not have permission.
     		 		Owner: ${owner}</p>
     		 		</c:if>
				
     		
	</pimsForm:form>
</pimsWidget:box>

<pimsWidget:multiRoleBox title="Sample categories" objectHook="${refsample.hook}" roleName="sampleCategories"/>	
    	
<c:set var="concentration"><%=SampleComponent.PROP_CONCENTRATION%></c:set>
<c:set var="details"><%=SampleComponent.PROP_DETAILS%></c:set>

<c:set var="newComponent">
    <c:if test="${mayUpdate}">
       <a href="${pageContext.request.contextPath}/ChooseForCreate/org.pimslims.model.sample.SampleComponent/refComponent?${actionHTML}=abstractSample%3D${refsample.hook}" >Add new component</a>
    </c:if>
</c:set>

<pimsWidget:box title="Recipe" initialState="closed" extraHeader="${newComponent}" id="Recipe" >	
	
	<c:choose>
 		<c:when test="${not empty components}">
 			<pimsForm:form action="/Update" id="updateRecipe" method="post" mode="view">
 			<table>
				<tr>
					<th>Component</th>
					<th>${concentration}</th>
					<th style="width:40%">${details}</th>
					<th style="width:4em">delete</th>
	 			</tr>
	   			<c:forEach var="component" items="${components}">
	   				<tr id="${component.hook}" class=" ajax_deletable">
	        			<td>
	        			${component.values['<%=SampleComponent.PROP_REFCOMPONENT%>'].hook}
	          			  <a href="${pageContext.request.contextPath}/View/${component.values['refComponent'].hook}">
	            			    ${component.values['refComponent'].name}
	          			  </a>
	       			 </td>
	       			 
	       			 <td>
	       			 
	       			 <span class="viewonly">${component.values['concentration']}</span>
                     <span class="editonly"> 
                        <pimsForm:doAmount hook="${component.hook}" propertyName="concentration" value="${component.values['concentration']}" />
                     </span>
	       			 </td>
	       			 
	       			 <td>
	       			 	<span class="viewonly">
							${component.values['details']}
						</span>
						<span class="editonly">
							<input type="text" name="${component.hook}:${details}" id="${component.hook}:${details}" value="${component.values['details']}" />
						</span>
	       			 </td>
	       			 
	       			 <td style="text-align:center;">
        				<c:choose><c:when test="${component.mayDelete}">
        				<a href="${pageContext.request.contextPath}/Delete/${component.hook}">
     						<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif"
          						alt="Delete" title="Delete"
          						id="${component.hook}_deleteicon"
          						class="icon"
          						onclick="ajax_delete(this, {cleanup:function(){experiment_checkNumOutputs()} } );return false;"
      					/>
      					</a>
      					</c:when><c:otherwise>
							<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" 
          					alt="Can't delete" title="Can't delete"
							/>
   						 </c:otherwise></c:choose>
      				</td>
	      			 </tr>
    			 </c:forEach>
   			 </table>
   			 
   			 <pimsForm:formBlock>
     		 	<pimsForm:editSubmit />
     		</pimsForm:formBlock>
     		
   			 </pimsForm:form>
 		 </c:when>
 	 	<c:otherwise> <p class="empty_parameters" style="border:0;">No Components for this recipe</p> <%--<br/>--%> </c:otherwise>
 	</c:choose>
 	
</pimsWidget:box>

<pimsWidget:multiRoleBox title="Suppliers" objectHook="${refsample.hook}" roleName="refSampleSources" mayAdd="false" />


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
<pimsWidget:box title="Available stocks of this recipe" initialState="${stockBoxState}"  >
	
		<c:choose>
 			<c:when test="${not empty stocks}">
				<ul>
					<c:forEach items="${stocks}" var="stock" >
						<li><pimsWidget:link bean="${stock}" /></li>
					</c:forEach>
				</ul>
 			</c:when>
			<c:otherwise>
				<p class="empty_parameters">No associated samples</p>
			</c:otherwise>
		</c:choose>
  	<pimsForm:form action="/update/CreateSampleFromRefSample" method="post" mode="edit">
        <input type="hidden" name="METACLASSNAME" id="METACLASSNAME" value="${sampleclass}" />
        <input type="hidden" name="${sampleclass}:${samplerefsample}" value="${refsample.hook}" />
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
            </pimsForm:column2>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:submitCreate onclick="dontWarn()" />
        </pimsForm:formBlock>       
   	</pimsForm:form>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>


<pimsWidget:files bean="${refsample}"  />

<pimsWidget:notes bean="${refsample}"  />

<jsp:include page="/JSP/core/Footer.jsp" />

<!-- ViewRefSample.jsp -->

