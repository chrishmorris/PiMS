<%--
Custom view for a sample
Servlet: org.pimslims.standard.ViewSample
Author: Chris Morris
Date: December 2006
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
<jsp:useBean id="sample"  scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<pims:import className="<%= Sample.class.getName() %>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Sample/Stock: ${sample.name}" />
    
</jsp:include>
<!-- ViewSample.jsp -->
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/move.js"></script>

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
</script>

<c:set var="noneHTML" value="[NONE]" />

<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.Sample">Samples</a>
	
    <c:forEach items="${ currentLocationTrail }" var="container" varStatus="status" >
      : <pimsWidget:link bean="${ container }" /> 
      <c:set var="containerName" value="${ container.name }" />
    </c:forEach>
</c:set>

<c:set var="icon" value="sample.png" />        
<c:set var="title" value="${sample.name}"/>
<c:set var="actions">
    <c:if test="${mayUpdate}">
        <a href="${pageContext.request.contextPath}/barcodegraph/${sample.hook}" title="Print Barcode" >Barcode</a>
        <c:choose><c:when test="${null!= containerName}">
            <a href="#" onclick="submitRemove( '${sample.name}','${sample.hook}','${containerName}');return false"
               title="Remove ${sample.name} from ${containerName}"
            >Remove</a>
        </c:when><c:otherwise> 
        <a href="#" onclick="containerSearch({hook:'${sample.hook}', name:'<c:out value="${sample.name}" />'}); return false;"
          title="Record container the sample is in" 
          ${(empty currentLocationTrail && sample.values['isActive'] eq 'Yes') ? ' tabindex="1" ':''}
        >Move</a>  
        </c:otherwise></c:choose>
    </c:if>
    <pimsWidget:linkWithIcon isNext="${empty usedIn}"
        text="New Experiment" title="Record new experiment processing this sample" icon="types/small/experiment.gif"
        url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?_Sample=${sample.hook}" />
	<pimsWidget:diagramLink hook="${sample.hook}"/>
	<c:set var="url">${pageContext.request.contextPath}/read/Provenance/${sample.hook}?<%= org.pimslims.servlet.report.T2CReport.ACCEPT %>=<%= org.pimslims.servlet.report.T2CReport.PRESENTATION_HTML %></c:set>
	<pimsWidget:linkWithIcon 
		text="Report" title="Sample Provenance Report" icon="actions/viewdiagram.gif"
		url="${url }" />
	<pimsWidget:deleteLink bean="${sample}" />&nbsp;
	<c:if test="${mayUpdate}">
		<c:choose>
			<c:when test="${divideEnabled}">
	  			<a href="${pageContext.request.contextPath}/update/DivideSample/${sample.hook}">Divide</a> 
			</c:when>
	  		<c:otherwise>&nbsp;<span title="Can't divide sample - set an amount first." >Can't divide</span></c:otherwise>
		</c:choose>
	</c:if>
    <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/HelpSamples.jsp" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />




<pimsWidget:box title="Basic Details" initialState="open">
	<pimsForm:form action="/Update/${sample.hook}" method="post" mode="view">
		<pimsForm:formBlock>
			<pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${sample.hook}:pageNumber" value="${sample.values['pageNumber']}"
    />
	<pimsForm:text name="${sample.hook}:${Sample['PROP_NAME']}" 
						 	value="${sample.name}"
						   	alias="Name" validation="required:true" />

	<pimsForm:amount hook="${sample.hook}" propertyName="${Sample['PROP_CURRENTAMOUNT']}" 
	alias="Amount" value="${amount}" />
			
	<pimsForm:amount hook="${sample.hook}" propertyName="${Sample['PROP_CONCENTRATION']}" value="${concentration}"
	alias="Concentration"  />
                    
			
	<pimsForm:nonFormFieldInfo label="Position in Container" helpText="Use Move/Remove to change this">
                    ${sample.rowAlphaPosition} 
                  <c:if test="${!holder.isPlate && !empty sample.colPosition }">
                    ,
                  </c:if>
                    ${sample.colPosition}
                  <c:if test="${!empty sample.subPosition }">
                    .${sample.subPosition}
                  </c:if>
            </pimsForm:nonFormFieldInfo>
			
     		 
<pimsForm:textarea alias="Details" name="${sample.hook}:${Sample['PROP_DETAILS']}" ><c:out value="${sample.values['details']}" /></pimsForm:textarea>
						   	
			</pimsForm:column1>
			<pimsForm:column2>
<%--TODO use pimsForm:boolean --%>
		        <c:set var="isActive" value="${sample.values['isActive']}" />
			    <pimsForm:radio name="${sample.hook}:${Sample['PROP_ISACTIVE']}" value="yes" label="Yes" isChecked="${isActive eq 'Yes'}" alias="Stock available" />
				<pimsForm:radio name="${sample.hook}:${Sample['PROP_ISACTIVE']}" value="no"  label="No"  isChecked="${isActive ne 'Yes'}" alias="" />

				
				
				<pimsForm:date name="${sample.hook}:${Sample['PROP_USEBYDATE']}" alias="Use by date" value="${sample.values['useByDate']}" validation="required:false" />
				
				<c:set var="viewContent" value="${personAssignedTo.name}" />
				<c:if test="${empty personAssignedTo.name}"><c:set var="viewContent" value="${noneHTML}" /></c:if>
				<%-- <pimsForm:select alias="Assigned to" name="${sample.hook}:${Sample['PROP_ASSIGNTO']}" >
					<pimsForm:option optionValue="${noneHTML}" alias="none" currentValue="${personAssignedTo.hook}" />
					<c:forEach items="${ userPersons }" var="entry"> 
					    <pimsForm:option optionValue="${entry.hook}" currentValue="${personAssignedTo.hook}"  alias="${fn:escapeXml(entry.name)}" />
					</c:forEach>
	     		</pimsForm:select> --%>
	     		
	     		<pimsForm:nonFormFieldInfo label ="Lab Notebook" >
                    <c:out value="${sample.access.name}" />
    			</pimsForm:nonFormFieldInfo>
            
			</pimsForm:column2>
     		</pimsForm:formBlock>
     		
				<pimsForm:column2>
     		 		<pimsForm:editSubmit/>
				</pimsForm:column2>
     		
	</pimsForm:form>
</pimsWidget:box>

<pimsWidget:multiRoleBox title="Sample categories" objectHook="${sample.hook}" roleName="sampleCategories" />


<c:if test="${not empty primer}">
    <pimsWidget:primer bean="${primer}" />
</c:if>	

<c:set var="viewRecipe">
    <c:if test="${null!=refSample}">
        <pimsWidget:link bean="${refSample}" />
    </c:if>
</c:set>
    	
<c:set var="concentration"><%=SampleComponent.PROP_CONCENTRATION%></c:set>
<c:set var="details"><%=SampleComponent.PROP_DETAILS%></c:set>

<pimsWidget:box title="Recipe and/or components" initialState="closed" extraHeader="${viewRecipe}">	
	
	<c:choose>
 		<c:when test="${not empty components}">
 			<table class="list">
				<tr>
					<th>Component</th><th>${concentration}</th><th>${details}</th>
	 			</tr>
	   			<c:forEach var="component" items="${components}">
	     			<tr id="${component.hook}">
	        			<td>${component.values['<%=SampleComponent.PROP_REFCOMPONENT%>'].hook}
	          			  <a href="${pageContext.request.contextPath}/View/${component.values['refComponent'].hook}">
	            			    ${component.values['refComponent'].name}
	          			  </a>
	       			 </td>
	       			 <td><c:out value="${component.values['concentration']}" /></td>
	       			 <td><c:out value="${component.values['details']}" /></td>
	      			 </tr>
    			 </c:forEach>
   			 </table>
 		 </c:when>
 	 	<c:otherwise> <p class="empty_parameters" style="font-size: 90%; border:0">No Components for this sample</p>	 </c:otherwise>
 	</c:choose>
 
</pimsWidget:box>

<%--Experiment details --%>
    <c:set var="extraHeader"  scope="page" value="Not Recorded" />
    <c:if test="${null!=experiment}">
	<c:set var="extraHeader"  scope="page">
	    <pimsWidget:link bean="${experiment}" />
	</c:set>
	</c:if>
    <pimsWidget:box title="Was Output from Experiment:"   initialState="closed"  extraHeader="${extraHeader}" >
      <c:if test="${null!=experiment}">
			<pimsForm:form action="/Update" method="post" mode="view">
            <%-- TODO <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/experiment/parameters/${experiment.values['protocol'].name}.jsp" />
       </jsp:include> --%>
            <%@include file="/JSP/experiment/parameters/Default.jsp"%>
			</pimsForm:form>
     </c:if>
  	</pimsWidget:box>
  	
<pimsWidget:box title="Used as Inputs to Experiments ..." initialState="${empty usedIn? 'open' : 'closed'}" >
  <table class="list">
    <tr><th>Role</th><th>Action</th><th>Protocol</th></tr>
    <c:choose><c:when test="${empty usedIn}">
        <tr><td colspan="3">No experiments are recorded that use this sample.</td></tr>
    </c:when><c:otherwise>
            <tr><td colspan="3">This sample was used in these experiments:</td></tr>
            <c:forEach items="${usedIn}" var="is" >
              <c:if test="${!empty is.sampleBean && is.sampleBean.hook eq sample.hook}">
                <tr>
                    <td style="vertical-align: middle;"><c:out value="${is.inputSampleName}" /></td>
                    <td>
                        <form style="padding-top: 4px; padding-bottom: 4px;" class="activeEvenIfNoUpdate"  action="javascript:alert('already an input'); return false" method="post">
                        <input type="submit" value="Set Sample" disabled="disabled" style="color: gray" title="this sample is already recorded as an input" />
                        </form>
                    </td>
                    <td style="vertical-align: middle;">
                        <pimsWidget:link bean="${is.experimentBean}" />
                    </td>
                    
                </tr>
                </c:if>
            </c:forEach>
    </c:otherwise></c:choose>
  
  <c:choose>
    <c:when test="${empty couldUseIn}">
        <!-- no more experiments it could be used for -->
    </c:when>
    <c:when test="${!mayUpdate}">
        <!-- this user can't record use of this sample -->
    </c:when>
    <c:otherwise>
            <tr><td colspan="3">Was this the sample for these recently viewed experiments?</td></tr>
             
            <c:forEach items="${couldUseIn}" var="is" >
              
                <tr class="missingData">
                    <td style="vertical-align: middle;"><c:out value="${is.inputSampleName}" /></td>
                    <td>
                        <form style="padding-top: 4px; padding-bottom: 4px;" class="activeEvenIfNoUpdate"  action="${pageContext.request.contextPath}/experiment/UpdateInputSamples" method="post">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/experiment/UpdateInputSamples')}" />
                        <input type="hidden" name="${is.inputSampleHook}:sample" value="${sample.hook}" /> 
                        <input type="submit" value="Set Sample" title="add this sample" />
                        </form>
                    </td>
                    <td style="vertical-align: middle;">
                        <pimsWidget:link bean="${is.experimentBean}" />
                    </td>
                </tr>
                
            </c:forEach>

    </c:otherwise>
  </c:choose>        
  
  <!-- use sample in new experiment -->
<c:choose>
    <c:when test="${empty refInputBeans}">


        <pims:import className="<%= org.pimslims.model.protocol.Protocol.class.getName() %>" />
        <p class="empty_parameters" style="display:inline; border:0; font-size: 90%;">No suitable protocols found for using this sample.</p> <%-- <br/><br/> --%>
        <pimsWidget:createLink className="${Protocol['class'].name}" />


    </c:when>
    <c:when test="${!mayUpdate}">
        <tr><td colspan="3">You don't have permission to use this sample.</td></tr>
    </c:when>
    <c:otherwise>

<%-- TODO no, show list of protocols only in CreateExperiment --%>
        <tr><td colspan="3">Use Sample as input in a New Experiment:</td></tr>
            <c:if  test="${empty usedIn}"><c:set var='tabindex' value='tabindex="1"' /></c:if> 
            <c:forEach items="${refInputBeans}" var="ris" >
                <tr>
                    <td style="vertical-align: middle;"><c:out value="${ris.name}" /></td>
                    <td>
                        <form style="padding-top: 4px; padding-bottom: 4px;" class="activeEvenIfNoUpdate"  
                        action="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment" 
                        method="get">
    <%-- not for get <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/Create/org.pimslims.model.experiment.Experiment')}" /> --%>
                        <input type="hidden" name="protocol" value="${ris.protocol.hook}" /> 
                        <input type="hidden" name="_Sample" value="${sample.hook}" />
                        <input type="submit" value="New Experiment" ${tabindex} />
                    </form></td>
                    <td style="vertical-align: middle;">                
                        <pimsWidget:link bean="${ris.protocol}" />
                    </td>
                </tr>
            </c:forEach>
             

    </c:otherwise>
</c:choose>     

    <c:choose><c:when test="${empty usedIn}">
        <tr><td colspan="3">No experiments are recorded that use this sample.</td></tr>
    </c:when><c:otherwise>
            <tr><td colspan="3">This sample was used in these experiments:</td></tr>
            <c:forEach items="${usedIn}" var="is" >
              <c:if test="${!empty is.sampleBean && is.sampleBean.hook eq sample.hook}">
                <tr>
                    <td style="vertical-align: middle;"><c:out value="${is.inputSampleName}" /></td>
                    <td>
                        <form style="padding-top: 4px; padding-bottom: 4px;" class="activeEvenIfNoUpdate"  action="javascript:alert('already an input'); return false" method="post">
                        <input type="submit" value="Set Sample" disabled="disabled" style="color: gray" title="this sample is already recorded as an input" />
                        </form>
                    </td>
                    <td style="vertical-align: middle;">
                        <pimsWidget:link bean="${is.experimentBean}" />
                    </td>
                    
                </tr>
                </c:if>
            </c:forEach>
    </c:otherwise></c:choose>  
</table>               
</pimsWidget:box>



 




<!-- Associated Samples... -->
<%--TODO Use sample beans to make this --%>
<c:choose>
 	<c:when test="${not empty associates}">
    	<pimsWidget:box title="Associated samples" initialState="closed" >
				<ul>
					<c:forEach items="${associates}" var="associate" >
						<li><pimsWidget:link bean="${associate}" /></li>
					</c:forEach>
				</ul>
  		</pimsWidget:box>
 	</c:when>
	<c:otherwise>
    	<pimsWidget:box title="Associated samples"   initialState="open" >
		<p class="empty_parameters" style="border:0; font-size: 90%;">No associated samples</p>
  		</pimsWidget:box>
	</c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>


<pimsWidget:files bean="${sample}"  />

<pimsWidget:notes bean="${sample}"  />

<jsp:include page="/JSP/core/Footer.jsp" />
