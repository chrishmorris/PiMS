<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>

<script type="text/javascript">
function toggleAllCheckboxes(checkAllBoxElement){
	var isChecked = checkAllBoxElement.checked;
	var parentTable = Element.extend(checkAllBoxElement).up("table");
	<%--alert (parentTable);--%>
	var boxes=parentTable.getElementsByClassName("behaviour_selectAll");
	
	for(i=0;i<boxes.length; i++){
		boxes[i].checked=isChecked;
	}
	disableSubmitIfConstructCountWrong();
	return true;
}

function disableSubmitIfConstructCountWrong(){
  var checkedBoxes=$("orderform").select("input.behaviour_selectAll");
  var count=0;
  checkedBoxes.each(function(box){
    if(box.checked) { count++; };
  });
  if(1>count || 96<count){
    $("orderformsubmit").disabled="disabled";
    $("labNotebookId").disabled="disabled";
    $("counterror").style.visibility="visible";
  } else {
    $("orderformsubmit").disabled="";
    $("labNotebookId").disabled="";
    $("counterror").style.visibility="hidden";
  }  
}
</script>

<c:choose><c:when test="${!empty param.callbackFunction}">
    <c:set var="callbackFunction" value="${param.callbackFunction}" />
</c:when><c:otherwise>
    <c:set var="callbackFunction" value="selectInMRU" />
    <%-- TODO no, we provide the wrong parameters --%>
</c:otherwise></c:choose>

<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructBean>" />

<form style="width:auto;background-color:transparent" action="${pageContext.request.contextPath}/update/CreateOrderPlate" onsubmit="return validateFormFields(this)" method="post" id="orderform">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/update/CreateOrderPlate')}" />
 <display:table class="list" id="mytable" name="${beans}" 
 				pagesize="${pagesize}" 
 				    size="${resultSize}">

  <c:choose><c:when test="${empty param.isInPopup && empty param.isInModalWindow}">
	<display:column escapeXml="false"   headerClass="sortable" title="<input name='checkAll' id='checkAll' onclick='toggleAllCheckboxes(this)' type='checkbox'/> " media="html">
		       <input name="${mytable.hook}" class="behaviour_selectAll"
		        onclick="disableSubmitIfConstructCountWrong()" type="checkbox" />
  	</display:column>
  </c:when><c:when test="${'true' eq param['selectMultiple']}">
	<display:column escapeXml="false"   headerClass="sortable" title="<input name='checkAll' id='checkAll' onclick='toggleAllCheckboxes(this)' type='checkbox'/> " media="html">
		       
		       <c:set var="objectData">
            {
                        hook:'${mytable.hook}',
                        name:'${utils:escapeJS(mytable.name)}' 
            }
            </c:set>
            <input type="checkbox" onclick="prepareForAdd(this,${objectData})" />
  	</display:column>
  </c:when><c:otherwise>
        <display:column escapeXml="false"  style="padding:2px 0 0 3px;width:20px;" media="html" title="" >
            <span style="text-decoration:underline;cursor:pointer" onclick="window.parent.${callbackFunction}({hook:'${mytable.hook}',name:'${utils:escapeJS(mytable.name)}'})"> Select </span>
        </display:column>
  </c:otherwise></c:choose>

	
    <display:column title="Project" sortable="true" headerClass="sortable" media="html">
        <pimsWidget:link  bean="${mytable}" />
    </display:column>
	
	
	
	<display:column title="Forward Primer" sortable="true" headerClass="sortable" media="html">
		<c:if test="<%= mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList %>"><pimsWidget:link  bean="${mytable.fwdPrimerBean}" /></c:if>
	</display:column>
	
	
	<display:column title="Reverse Primer" sortable="true" headerClass="sortable" media="html">
		<c:if test="<%= mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList %>"><pimsWidget:link  bean="${mytable.revPrimerBean}" /></c:if>
	</display:column>
	
		
	<display:column title="Target" sortable="true" headerClass="sortable" media="html">
		<c:if test="<%= mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList %>"><pimsWidget:link  bean="${mytable.targetBean}" /></c:if>
	</display:column>
    <display:column title="Organism" sortable="true" headerClass="sortable" media="html">
        <c:if test="<%= (mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList)  %>">
            <c:if test="${! empty mytable.targetBean.organismBean}"><pimsWidget:link  bean="${mytable.targetBean.organismBean}" /></c:if>
        </c:if>
    </display:column>
	
	
	<display:column title="Latest Milestone" sortable="true" headerClass="sortable" media="html csv xml excel pdf">
		<c:if test="<%= (mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList)  %>">
			${mytable.latestMilestone.status.name}
		</c:if>
	</display:column>
	<%--
	<display:column property="latestMilestoneStatusDate" title="Date" sortable="true" headerClass="sortable" 
					decorator="org.pimslims.servlet.utils.decorators.Date" media="html csv xml excel pdf" /> --%>
	
	<display:column title="Experiment" sortable="true" headerClass="sortable" media="html">
		<c:if test="<%= (mytable instanceof org.pimslims.presentation.construct.ConstructBeanForList
		    && null!= ((org.pimslims.presentation.construct.ConstructBeanForList)mytable).getLatestMilestoneExperiment()
		)  %>">
			<pimsWidget:link  bean="${mytable.latestMilestoneExperiment}" />
		</c:if>
	</display:column>
	
	
 </display:table>
<c:if test="${empty param.isInPopup && empty param.isInModalWindow
    && fn:contains(pageContext.request.requestURI, 'Search')
}"> 

	<p style="padding-left:.55em;">Project
		<select name="labNotebookId" id="labNotebookId">
    		<option value="">Choose...</option>
 			 <c:forEach items="${accessObjects}" var="elem">
   				<option value="${elem.hook}"><c:out value="${elem.name}" /></option>
  			</c:forEach>
 		</select>
 		
 		<input type="submit" id="orderformsubmit" value="Create Order Set-up Grid"/> containing selected constructs 
 		<br />
 		<span class="error" id="counterror">(Order plates must contain between 1 and 96 constructs)</span>
 	</p>
 	
</c:if>
</form>

<c:if test="${empty param.isInPopup && empty param.isInModalWindow}"> 
<script type="text/javascript">
attachValidation("labNotebookId",{ required:true, alias:"Project"});
disableSubmitIfConstructCountWrong();
</script>
</c:if>


<c:if test="${!param.isInModalWindow && metaRole.high ne 1}">
<div class="behaviour_showafteradd" style="display:none; padding:0.25em 0.25em 0.25em 1.9em;border-top:1px solid #999">
    Items to add:
    <pimsForm:form action="/EditRole/${modelObject.hook}/${metaRole.name}" mode="edit" method="post">
        <div class="behaviour_itemstoadd">&nbsp;</div>
        <input type="submit" value="Add"  />
    </pimsForm:form>
</div>
</c:if> 

<!-- OLD -->
