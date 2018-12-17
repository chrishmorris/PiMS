<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ page contentType="text/html; charset=utf-8" language="java"
  import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*,org.pimslims.presentation.protocol.*,org.pimslims.model.protocol.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<c:catch var="error">

<jsp:useBean id="protocol" scope="request" type="org.pimslims.presentation.protocol.ProtocolBean" />
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${protocol.values['experimentType'].name} protocol: ${protocol.name}" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>



<!--*********
END EXTRACT TO FILES
*********-->

<script type="text/javascript">
function openBox(headerElem){
	  var box=Element.extend(headerElem).up(1);
	  box.addClassName("collapsibleBox_opened");
	  box.removeClassName("collapsibleBox_closed");
}
var pdefHooks=new Array();
var contextPath="${pageContext.request.contextPath}";
var protocolHook="${protocol.hook}";
function validateAll(){
  //TODO input and output samples, basic protocol details
  var ok=true;
  for(i=0;i<pdefHooks.length;i++){
  	if(!protocol_validatePdef(pdefHooks[i]["hook"], pdefHooks[i]["name"])){
  	  ok=false;
      openBox($(pdefHooks[i]["hook"])); 
      Element.scrollTo($(pdefHooks[i]["hook"]));
  	  break;
  	}
  }
  return ok;
}

function checkMyForm(form) {
	
	for(i=0; i<form.elements.length; i++) {
		if (form.elements[i].name.endsWith(":_measurement")) {

			var basename = form.elements[i].name.substring(0, form.elements[i].name.length-12);
			var measurementField=form.elements[i];
			var valueField;
			var unitField;
			var nameField;
			for(j=0; j<form.elements.length; j++) {
			    
				if (form.elements[j].name==basename+"_amount") {
					valueField = form.elements[j];
				}
				if (form.elements[j].name == basename+"_displayUnit") {
					unitField = form.elements[j];
				}
				if (form.elements[j].name == basename+"name") {
					nameField = form.elements[j];
				}
			}

			if (""!=valueField.value && "[No Units]"==unitField.value) {
				alert('Please select a unit for the value of '+nameField.value+' ['+measurementField.value+']');
				return false;
			}
			if (""==valueField.value && "[No Units]"!=unitField.value) {
				alert('Please select an amount for the unit of '+nameField.value+' ['+measurementField.value+']');
				return false;
			}
		}
	}
	form.submit();
	return true;
}

String.prototype.endsWith = function(str)
{return (this.match(str+"$")==str)}
</script>

<pims:import className="<%= Protocol.class.getName() %>" />


<form action="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.ExperimentGroup" method="get" style="display:none" id="protocol_makeExptGroup">
    <input type="hidden" name="protocol" value="${protocol.hook}" />
</form>


<script>
function toggleUse() {
	var select = $("${protocol.hook}:${Protocol['PROP_ISFORUSE']}");
	select.selectedIndex = 1- select.selectedIndex;
	select.form.submit();
}
</script>

<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.protocol.Protocol">Protocols</a>
</c:set>
<c:set var="icon" value="protocol.png" />        
<c:set var="title" value="${protocol.name}"/>
<c:set var="actions">
	<pimsWidget:diagramLink hook="${protocol.hook}"/>
	
        &nbsp;
        <a title="List single experiments using this protocol"
         href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment?experimentType=${protocol.values['experimentType'].hook}&amp;protocol=${protocol.hook}"
          >Search experiments</a>
	
	<c:if test="${mayChangeForUse}" >
	  <c:choose><c:when test="${protocol.values['isForUse']=='No'}">
        <pimsWidget:linkWithIcon text="Put Into Use" title="Allow new experiments with this protocol"
         url="#"
         onclick="toggleUse();return false;"
         icon="actions/edit.gif" />
      </c:when><c:otherwise>
        <pimsWidget:linkWithIcon text="Put Out of Use" title="Prevent new experiments with this protocol"
         url="#"
         onclick="toggleUse();return false;"
         icon="actions/edit.gif" />
      </c:otherwise></c:choose>
    </c:if>
	
    <c:choose><c:when test="${protocol.values['isForUse']=='No'}">
        <%-- no more controls are appropriate --%>
    </c:when><c:otherwise>
        <pimsWidget:linkWithIcon text="New single experiment" title="Record a single experiment using this protocol"
         url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?experimentType=${protocol.values['experimentType'].hook}&amp;protocol=${protocol.hook}"
         icon="actions/create/experiment.gif" />
         

        <pimsWidget:linkWithIcon text="New experiment group" title="Record an experiment group using this protocol"
         url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.ExperimentGroup?experimentType=${protocol.values['experimentType'].hook}&amp;protocol=${protocol.hook}"
         onclick="document.forms['protocol_makeExptGroup'].submit();return false;"
         icon="actions/create/experimentgroup.gif" />


	    <c:set var="numOutputSamples" value="${fn:length(protocol.outputSamples)}" />
	    <span id="plateExptLink">
		      <c:choose><c:when test="${1==numOutputSamples}">
		        <pimsWidget:linkWithIcon text="New plate experiment" title="Record a plate experiment using this protocol"
		         url="${pageContext.request.contextPath}/CreatePlate?protocol=${protocol.hook}"
		         icon="actions/create/plate.gif" />
		      </c:when><c:otherwise>
		             <%--<br /> --%>
		             (Protocols for a plate experiment must have exactly one output.)
		             <br />
		     </c:otherwise></c:choose>
	    </span>
	    
    </c:otherwise></c:choose>	
    
    <pimsWidget:linkWithIcon text="Export" icon="actions/export.gif" title="Export this protocol as XML"
       url="${pageContext.request.contextPath}/read/Export/${protocol.hook}" />
    
    <pimsWidget:linkWithIcon text="Copy" icon="actions/copy.gif" url="#"
       title="Make a duplicate which you can edit"
       onclick="if(confirm('Copy this protocol?')){ document.forms['copyProtocol'].submit() } else { return false; }" />
    
    <pimsWidget:deleteLink bean="${protocol}" />

    <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp" />
    
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />

<div style="display:none" id="selenium_protocolHook">${protocol.hook}</div>
<div style="display:none" id="selenium_itemHook">null</div>

<c:set var="disabled" value=""/>
<c:if test="${!mayUpdate}"><c:set var="disabled" value="onfocus=\"this.blur()\""/></c:if>

<form name="copyProtocol" id="copyProtocol" method="post" action="${pageContext.request.contextPath}/update/Copy/${protocol.hook}" class="singlebutton cancel">
    <c:set var="action">/update/Copy/${protocol.hook}</c:set>
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,action)}" />
</form>

<%-- 
*
*    "Details" box 
*
--%>
<pimsWidget:box title="Basic Details" initialState="open">
    <pimsForm:form action="/Update" method="post" mode="view">
        <pimsForm:formBlock>
            <pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${protocol.hook}:pageNumber" value="${protocol.values['pageNumber']}"
    />
                <pimsForm:text name="${protocol.hook}:name" value="${fn:escapeXml(protocol.name)}" alias="Name" 
                    validation="required:true, unique:{obj:'org.pimslims.model.protocol.Protocol', prop:'name'}" />
                <pimsForm:select name="${protocol.hook}:${Protocol['PROP_EXPERIMENTTYPE']}" alias="Experiment type">
                    <c:forEach items="${experimentTypes}" var="type">
                      <pimsForm:option optionValue="${type.hook}" currentValue="${protocol.values['experimentType'].hook}" alias="${type.name}" />
                    </c:forEach>
              </pimsForm:select>
                <pimsForm:select name="${protocol.hook}:${Protocol['PROP_INSTRUMENTTYPE']}" alias="Instrument type">
                    <pimsForm:option optionValue="[none]"  alias="" currentValue="${protocol.values['instrumentType'].hook}" />
                    <c:forEach items="${instrumentTypes}" var="type">
                      <pimsForm:option optionValue="${type.hook}" currentValue="${protocol.values['instrumentType'].hook}" alias="${type.name}" />
                    </c:forEach>
              </pimsForm:select>
            </pimsForm:column1>
            <pimsForm:column2> 
                <pimsForm:text name="${protocol.hook}:objective" value="${fn:escapeXml(protocol.values['objective'])}" alias="Objective" />
                <pimsForm:nonFormFieldInfo label="Lab Notebook"><c:out value="${protocol.values['access'].name}" /></pimsForm:nonFormFieldInfo>
	            <pimsForm:select alias="Is for use" name="${protocol.hook}:${Protocol['PROP_ISFORUSE']}">
	                <c:choose>
	                <c:when test="${protocol.values[Protocol['PROP_ISFORUSE']]=='No'}">
	                    <pimsForm:option optionValue="No" currentValue="No" alias="No" />
	                    <pimsForm:option optionValue="Yes" currentValue="No" alias="Yes" />
	                </c:when>
	                <c:when test="${protocol.values[Protocol['PROP_ISFORUSE']]=='Yes'}">
	                    <pimsForm:option optionValue="No" currentValue="Yes" alias="No" />
	                    <pimsForm:option optionValue="Yes" currentValue="Yes" alias="Yes" />
	                </c:when>
	                <c:otherwise>
	                    <pimsForm:option optionValue="" currentValue="" alias="" />
	                    <pimsForm:option optionValue="No" currentValue="" alias="No" />
	                    <pimsForm:option optionValue="Yes" currentValue="" alias="Yes" />
	                </c:otherwise>
	                </c:choose>
	            </pimsForm:select>
            </pimsForm:column2>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:textarea name="${protocol.hook}:remarks" alias="Remarks">${fn:escapeXml(protocol.values['remarks'])}</pimsForm:textarea>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:textarea name="${protocol.hook}:details" alias="Details">${fn:escapeXml(protocol.values['details'])}</pimsForm:textarea>
        </pimsForm:formBlock>

        <c:if test="${mayUpdate}">
            <pimsForm:editSubmit />
        </c:if>    
        
    </pimsForm:form> 
</pimsWidget:box>

<%-- 
*
*    "Method" box 
*
--%>
<pimsWidget:box title="Method" extraHeader="" id="protocol_method" initialState="closed"  >
    <pimsForm:form action="/Update" mode="view" method="post">
        <pimsForm:formBlock>
            <pimsForm:textarea name="${protocol.hook}:methodDescription" alias="Method">${fn:escapeXml(protocol.values['methodDescription'])}</pimsForm:textarea>
        </pimsForm:formBlock>
        <c:if test="${mayUpdate}">
            <pimsForm:editSubmit />
        </c:if>
    </pimsForm:form>
</pimsWidget:box>

<%-- 
*
*    "Input samples" box 
*
--%>
<pimsWidget:box title="Inputs" extraHeader="" initialState="closed"  >
  <table>
        <tr>
            <th style="width:22em">Name</th>
            <th>Default amount</th>
            <th>Category</th>
            <c:if test="${mayUpdate}">
                <th colspan="2" class="noprint">Actions</th>                
            </c:if>
        </tr>
	    <c:forEach var="input" items="${protocol.inputSamples}" >
	        <tr class="ajax_deletable" id="${input.hook}">
                <td>${input.name}</td>                
                <td>${input.displayValue}${input.displayUnit}</td>                
                <td>${input.sampleCategory.name}</td>                
                <c:if test="${mayUpdate}">
                    <td style="width:40px;text-align:center" class="noprint">
                    <a href="#">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" 
                          alt="Edit" title="Edit" 
                          id="${input.hook}_editicon" class="icon"
                          onclick="openModalWindow('${pageContext.request.contextPath}/update/EditRefInputSample/${input.hook}','Edit input sample: ${input.name}');return false;"
                        />
                    </a>
                    </td>                
                    <td style="width:40px;text-align:center" class="noprint">
                    <a href="${pageContext.request.contextPath}/Delete/${hook}">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
				          alt="Delete" title="Delete" 
				          id="${input.hook}_deleteicon" class="icon"
				          onclick="ajax_delete(this, {cleanup:function(){protocol_checkNumOutputs()} } );return false;"
				        />
			        </a>
                    </td>                
                </c:if>
            </tr>
        </c:forEach>
        <c:if test="${mayUpdate}">
            <tr class="noprint"><td colspan="5" style="text-align:right">
                <a href="#" onclick="openModalWindow('${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.RefInputSample?isInModalWindow=yes&amp;protocol=${protocol.hook}','New input sample');return false">Add new...</a>
            </td></tr>
        </c:if>
    </table>

</pimsWidget:box>



<%-- 
*
*    "Group Parameters" box 
*
--%>
<pimsWidget:box title="Group level parameters" extraHeader="" initialState="closed">
         <c:set var="whichParameters" value="group" scope="request" />
        <jsp:include page="/JSP/protocol/parametersTable.jsp" />
     
</pimsWidget:box>

<%-- 
*
*    "Set-up Parameters" box 
*
--%>
<pimsWidget:box title="Set-up parameters" extraHeader="" initialState="closed">
        <c:set var="whichParameters" value="setup" scope="request" />
        <jsp:include page="/JSP/protocol/parametersTable.jsp" />
     
</pimsWidget:box>


<%-- 
*
*    "Result Parameters" box 
*
--%>
<pimsWidget:box title="Result parameters" extraHeader="" initialState="closed">
    <pimsForm:form action="/Update" mode="view" method="post" extraClasses="hastable">
        <c:set var="whichParameters" value="result" scope="request" />
	    <jsp:include page="/JSP/protocol/parametersTable.jsp" />
    </pimsForm:form>
</pimsWidget:box>



<c:set var="boxHeader" scope="page">Output Samples</c:set>

<pimsWidget:box id="outputSamples" title="${boxHeader}" extraHeader="${extraHeader}"  initialState="closed"  >
    <table>
        <tr>
            <th style="width:22em">Name</th>
            <th>Default amount</th>
            <th>Category</th>
            <c:if test="${mayUpdate}">
                <th colspan="2" class="noprint">Actions</th>                
            </c:if>
        </tr>
        <c:forEach var="output" items="${protocol.outputSamples}" >
            <tr class="ajax_deletable" id="${output.hook}">
                <td>${output.name}</td>                
                <td>${output.displayValue}${output.displayUnit}</td>                
                <td>${output.sampleCategory.name}</td>                
                <c:if test="${mayUpdate}">
                    <td style="width:40px;text-align:center" class="noprint">
                    <a href="#">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" 
                          alt="Edit" title="Edit" 
                          id="${output.hook}_editicon" class="icon"
                          onclick="openModalWindow('${pageContext.request.contextPath}/update/EditRefOutputSample/${output.hook}','Edit output sample: ${output.name}');return false;"
                        />
                    </a>
                    </td>                
                    <td style="width:40px;text-align:center" class="noprint">
                    <a href="${pageContext.request.contextPath}/Delete/${output.hook}">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
                          alt="Delete" title="Delete" 
                          id="${output.hook}_deleteicon" class="icon"
                          onclick="ajax_delete(this, {cleanup:function(){protocol_checkNumOutputs()} } );return false;"
                        />
                    </a>
                    </td>                
                </c:if>
            </tr>
            </c:forEach>

	        <c:if test="${mayUpdate}">
	            <tr class="noprint"><td colspan="5" style="text-align:right">
                    <a href="#" onclick="openModalWindow('${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.RefOutputSample?isInModalWindow=yes&amp;protocol=${protocol.hook}','New output sample');return false">Add new...</a>
	            </td></tr>
	        </c:if>

        </table>
    
</pimsWidget:box>


<script type="text/javascript">
var str=document.location.pathname.toString();
var bits=str.split("/");
var hook=bits[bits.length-1];
if($(hook)){
  openBox($(hook));
  Element.scrollTo($(hook));
  $("selenium_itemHook").innerHTML=hook; //Needed for selenium test

}
</script>


 
<pimsWidget:externalDbLinks bean="${bean}" dbnames="${dbnames}"/> 

<pimsWidget:files bean="${protocolBean}"  />

<pimsWidget:notes bean="${protocolBean}"  />


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />

