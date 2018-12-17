<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<jsp:useBean id="group" scope="request" type="org.pimslims.model.experiment.ExperimentGroup" /> 
<jsp:useBean id="protocol" scope="request" type="org.pimslims.model.protocol.Protocol" /> 
<jsp:useBean id="rows" scope="request" type="java.lang.String[]" /> 
<jsp:useBean id="cols" scope="request" type="java.lang.String[]" /> 
<jsp:useBean id="experiments" scope="request" type="java.util.List<org.pimslims.presentation.WellExperimentBean>" /> 
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Order plate" />
</jsp:include>

<c:set var="actions">
	<pimsWidget:linkWithIcon text="Progress report" title="View a spreadsheet showing experiment progress" icon="actions/blank.gif"
		url="${pageContext.request.contextPath}/read/OutcomesCsv/${group._Hook}/PIMS_Plate_Layout_-_${group.name}_Outcomes.csv"
	 />	
	<%-- <a href='#' onclick=\"alert('Not yet implemented')\">View as list</a> --%>
</c:set>

<pimsWidget:pageTitle title="Primer Order Set-up Grid: ${group.name}"
	breadcrumbs="<a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective'>Constructs</a>"
	icon="construct.png" actions="${actions}"
/>


<c:set var="currentRow" value="0"/>
<c:set var="currentColumn" value="0"/>
<c:set var="currentExpt" value="0"/>

<pimsWidget:box initialState="fixed" title="Primer order plate">
<form method="post" action="${pageContext.request.contextPath}/update/BasicUpdatePlateExperiment" class="grid viewing" onsubmit="return disableEmptyWells(this)">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/update/BasicUpdatePlateExperiment')}" />
<table><tr><td style="border-bottom:1px solid #999;text-align:center;padding:0.5em;font-weight:bold;">
	<span id="multipleconstructinfo" style="visibility:hidden">Information: Two or more wells contain the same construct</span>
	</td></tr>
</table>
<div style="overflow:auto">
<table class="orderplate" id="orderplate">
	<tr>
	<th>&nbsp;</th>
	<c:forEach var="col" items="${cols}">
		<th>${col}</th>
	</c:forEach>
	</tr>

	<c:forEach var="row" items="${rows}">
		<tr>
			<th>${rows[currentRow]}</th>
			<c:forEach var="col" items="${cols}">
				<c:choose><c:when test="${experiments[currentExpt].rowAsInt eq currentRow && experiments[currentExpt].columnAsInt eq currentColumn}">
					<td>
						<span class="viewonly">
						<c:choose>
							<c:when test="${!empty experiments[currentExpt].construct.name}">
								<pimsWidget:link bean="${experiments[currentExpt].construct}" suppressContextMenu="true"/>
							</c:when><c:otherwise>
								[None]
							</c:otherwise>
						</c:choose>
						</span>
						<span class="editonly">
							 <pimsForm:doMruSelect bean="${experiments[currentExpt]}" rolename="researchObjective" onchange="handleConstructChange(this,false);return false;" />
						</span>
					</td>
					<c:set var="currentExpt" value="${1+currentExpt}"/>
				</c:when><c:otherwise>
					<%-- no experiment in this well. TODO set construct --%>
					<td>
						<span class="viewonly">
							[None]
						</span>
						<span class="editonly">
							 
							 <select onclick="" 
							 	id="${rows[currentRow]}${col}" 
							 	name="${rows[currentRow]}${col}" 
							 	onchange="handleConstructChange(this,true);return false;">
							        <optgroup label="Recently Used">
							            <option selected="selected" value="[none]">[none]</option>
							        </optgroup>
							        <optgroup label="Others">
							                <option value="[NONE]">Search More...</option>
							        </optgroup>
							</select>


						</span>
					</td>
				</c:otherwise></c:choose>
				<c:set var="currentColumn" value="${1+currentColumn}"/>
			</c:forEach>
			<c:set var="currentRow" value="${1+currentRow}"/>
		</tr>
		<c:set var="currentColumn" value="0"/>
	</c:forEach>
	
</table>
</div>
<div style="border-top:1px solid #999;">
<pimsForm:editSubmit />
</div>
</form>
</pimsWidget:box>


<pimsWidget:box initialState="closed" title="Order this plate">
	<pimsForm:form mode="create" action="../OrderPlate" method="post" >
	<pimsForm:formBlock>
		<p>If you are sure that:</p>
		<ul>
		<li>the details above are correct</li>
		<li>the plate has not already been ordered</li>
		<li>you really want to order these primers</li>
		</ul>
		<p>click "Create Order" below.</p>
		<input type="hidden" name="inputPlate" value="<c:out value="${group._Hook}" />" />
		<input type="submit" onclick="return orderPlateHasChanges()" value="Create Order" />
	</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>

<pimsWidget:box initialState="open" title="Files">
	<pimsForm:form mode="view" action="#" method="get">
		<c:set var="annotations" value="${group.annotations}"/>
		<pimsForm:formBlock>
			<h2>Current files</h2>
		</pimsForm:formBlock>
	<c:choose>
	<c:when test="${empty annotations}" >
	    <p>No files</p>
	</c:when>
	<c:otherwise>
		<table>
	    	<tr>
				<th>&nbsp;</th>
		    	<th style="width:30%">File</th>
		    	<th>Description</th>
	    	</tr>
	
			<c:forEach items="${annotations}" var="file">
	      		<tr id="${file._Hook}" class="ajax_deletable">
		       		<td style="width:20px;">
		             <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
        		     	 id="${file._Hook}_deleteicon" onclick="ajax_delete(this)" style="cursor:pointer"/>
					</td>
		        	<td>
			        	<a href="${pageContext.request.contextPath}/ViewFile/${file._Hook}/${file.name}" title="view file" target="file">
							<c:out value="${file.name}" />
			        	</a> 
		        	</td>
		        	<td>
			        	<c:out value="${file.details}" />
		        	</td>
	      		</tr>
			</c:forEach>
		</table>
	</c:otherwise>
	</c:choose>
	</pimsForm:form>
	<hr/>
	<pimsForm:form mode="create" action="/ListFiles/${group._Hook}" method="post" enctype="multipart/form-data" >
		<c:choose>
		<c:when test="${mayUpdate}">
			<pimsForm:formBlock>
			<pimsForm:column1>
		    	<h2>Upload a file</h2>
				<pimsForm:text name="fileDescription" alias="Description" value="${descrValue}" /><%-- maxlength="253" --%>
				<pimsForm:nonFormFieldInfo label="File">
			      	<input style="width: 32em; " type="file" name="file" id="file" /> 
		    	</pimsForm:nonFormFieldInfo> 
				<input type="submit" value="Upload"  name="userreq" 
			      	onClick="if (''==document.getElementById('file').value) {alert('Please choose a file to upload'); return false;} return true" />    
			</pimsForm:column1>
			</pimsForm:formBlock>
		</c:when>
		<c:otherwise>
			<p>Note: You do not have access rights to upload the files for this ${metaClass.alias}</p>
		</c:otherwise>
		</c:choose>
	</pimsForm:form>

</pimsWidget:box>


<form action="${pageContext.request.contextPath}/update/AddExperimentToGroup/${group._Hook}" method="post" style="display:none" id="addExpt">
    <%-- TODO CSRF token --%>
	<input type="hidden" name="group" id="group" value="${group._Hook}" />
	<input type="hidden" name="construct" id="construct" value="" />
	<input type="hidden" name="position" id="position" value="" />
</form>

<script type="text/javascript">
var plateHasChanges=false;

function orderPlateHasChanges(){
	if (plateHasChanges){
		alert("Your primer plate has unsaved changes.\nYou must save them before you can order the plate.");
		return false;
	}
	return true;
}

//check for 2 or more wells with same construct
function handleConstructChange(sel,isEmptyWell){
  if(sel) {
  	plateHasChanges=true;
  }
  var callbackFunction='chooseResearchObjective';
  if(isEmptyWell){
	callbackFunction='createExperimentInWell'  
  }
  
  if(sel && "[NONE]"==sel.value){
    //"Search more" was chosen.
    //"None" is lowercase [none], "Search more..." is uppercase [NONE]
    // - confusing as anything but at least we can tell the difference

	//disable default action - go to add/remove page
	sel.onclick=null;
	
	//and do our own
  	return searchMore('org.pimslims.model.target.ResearchObjective',sel,callbackFunction);
  }
  $("multipleconstructinfo").style.visibility="hidden";
  var constructs=new Array();
  var cells=$("orderplate").select("td");
  for(i=0;i<cells.length;i++){
    var td=cells[i];
    var sel=td.down("select");
    if(!sel || "[none]"==sel.value.toLowerCase()){ continue; }
    if(-1==constructs.indexOf(sel.value)){
      constructs.push(sel.value);
    } else {
      $("multipleconstructinfo").style.visibility="visible";
      break;
    }
  }
}

//global variable to store clicked select while searching for more
var selectToUpdate;

function searchMore(className,sel,callbackFunction){
  selectToUpdate=sel;
  var searchUrl=contextPath+"/Search/"+className;
  searchUrl+="?callbackFunction="+callbackFunction;
  searchUrl+="&isInModalWindow=yes"
  openModalWindow(searchUrl,"Choose target or construct");
  return false;
}


/**
 * Callback function from searchMore
 * Takes an object representing a construct, adds an option to
 * the select in question, and selects it
 */
function chooseResearchObjective(robj){
  var sel=selectToUpdate;
  sel.innerHTML="<option value=\""+robj.hook+"\">"+robj.name+"</option>"+sel.innerHTML;
  sel.value=robj.hook;
  closeModalWindow();
  plateHasChanges=true;
}

/**
 * Callback function from searchMore
 * submits a form to create an experiment in the well, with the right hook
 * 
 */
function createExperimentInWell(robj){
  var sel=selectToUpdate;
  $("position").value=sel.id;
  $("construct").value=robj.hook;
  closeModalWindow();
  openModalDialog("<img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif' alt=''/> Updating...","Please wait");
  $("addExpt").submit();
}

/**
 * Disable all SELECTs whose id is shorter than 5 characters.
 * Fix for PIMS-2824 - SELECTs in empty wells have id "A01", "H12", but in wells with
 * experiments they have a full-length PIMS hook.
 *
 * @param frm The form in question
 */
function disableEmptyWells(frm){
  var selects=frm.select("select");
  selects.each(function(sel){
    if(sel.id.length<5){
      sel.disabled="disabled";
    }
  });
  return true;
}

//call construct change handler, to display message about matching constructs if needed
handleConstructChange();
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
