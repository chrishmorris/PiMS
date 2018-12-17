<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="group" scope="request" type="org.pimslims.model.experiment.ExperimentGroup" /> 
<jsp:useBean id="rows" scope="request" type="String[]" /> 
<jsp:useBean id="cols" scope="request" type="String[]" /> 
<jsp:useBean id="experiments" scope="request" type="java.util.List<org.pimslims.presentation.WellExperimentBean>" /> 
<c:catch var="error">
obsolete
<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Order plate" />
</jsp:include>

<c:set var="actions">
	<pimsWidget:linkWithIcon text="Progress report" title="View a spreadsheet showing experiment progress" icon="actions/blank.gif"
		url="${pageContext.request['contextPath']}/read/OutcomesCsv/${group._Hook}/PIMS_Plate_Layout_-_<c:out value="${group.name}" />_Outcomes.csv"
	 />	
	<pimsWidget:linkWithIcon text="Diagram" title="View workflow diagram" icon="actions/viewdiagram.gif"
		url="${pageContext.request['contextPath']}/Graph/${group._Hook}?graphType=EXPERIMENTGROUP"
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
<form method="post" action="/update/UpdatePlateExperiment" class="grid viewing">
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
							 <pimsForm:doMruSelect rolename="researchObjective"  bean="${experiments[currentExpt]}" onchange="handleConstructChange(this,false);return false;" />
						</span>
					</td>
					<c:set var="currentExpt" value="${1+currentExpt}"/>
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




<form action="${pageContext.request["contextPath"]}/update/AddExperimentToGroup/${group._Hook}" method="post" style="display:none" id="addExpt">
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
  openModalDialog("<img src='${pageContext.request["contextPath"]}/skins/default/images/icons/actions/waiting.gif' alt=''/> Updating...","Please wait");
  $("addExpt").submit();
}

//call contruct change handler, to display message about matching constructs if needed
handleConstructChange();
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
