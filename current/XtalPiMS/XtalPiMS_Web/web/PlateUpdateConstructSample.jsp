<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:useBean id="holder" scope="request" type="org.pimslims.model.holder.Holder" />
<jsp:useBean id="rows" scope="request" type="String[]" /> 
<jsp:useBean id="cols" scope="request" type="String[]" /> 
<jsp:useBean id="experiments" scope="request" type="java.util.List<org.pimslims.presentation.WellExperimentBean>[]" /> 


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Edit plate' />
</jsp:include>
<pimsWidget:pageTitle title="TrialPlate: ${holder.name}"
    breadcrumbs="<a href='${pageContext.request.contextPath}/ViewPlate.jsp?barcode=<c:out value="${holder.name}" />'>Back to plate</a>"
    actions="${actions}"
/>
obsolete
<c:forEach var="sub" items="${subs}">
<pimsWidget:box initialState="closed" title="Constructs in subposition:${sub+1}" id="constructs${sub+1}">
<c:set var="currentRow" value="0"/>
<c:set var="currentColumn" value="0"/>
<c:set var="currentExpt" value="0"/>
<form method="post" action="/update/UpdatePlateExperiment" class="grid viewing">
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
                            <c:when test="${!empty experiments[sub][currentExpt].construct.name}">
                                <pimsWidget:link bean="${experiments[sub][currentExpt].construct}" suppressContextMenu="true"/>
                            </c:when><c:otherwise>
                                [None]
                            </c:otherwise>
                        </c:choose>
                        </span>
                        <span class="editonly">
                             <pimsForm:doMruSelect  bean="${experiments[sub][currentExpt]}" rolename="researchObjective" onchange="handleConstructChange(this,false);return false;" />
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
<pimsForm:form method="post" action="/UpdatePlateConstruct/org.pimslims.model.holder.Holder:${holder.dbId}" mode="edit" >
  <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="constructName${sub+1}" alias="Construct Name" helpText="name of the construct, use 'none' to remove the construct" value="" validation="required:true" />
        <pimsForm:text name="constructRowColStart${sub+1}" alias="Start Position" helpText="Start row,column to be set (eg:A01)" value="" validation="required:true" />
        <pimsForm:text name="constructRowColEnd${sub+1}" alias="End Position" helpText="End row,column to be set (eg:B02)" value="" validation="required:true" />
     <pimsForm:editSubmit />
      </pimsForm:column1>
      <pimsForm:column2>
         <pimsWidget:plateSelector id="constructSelector${sub+1}" callbackFunction="construct_RowColStartEnd_Selector" />        
      </pimsForm:column2>
     </pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

<pimsWidget:box initialState="closed" title="Protein Samples in subposition:${sub+1}" id="samples${sub+1}">
<c:set var="currentRow" value="0"/>
<c:set var="currentColumn" value="0"/>
<c:set var="currentExpt" value="0"/>
<form method="post" action="/update/UpdatePlateExperiment" class="grid viewing">
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
                            <c:when test="${!empty experiments[sub][currentExpt].inputSamples[0].sampleName}">
                                <pimsWidget:link bean="${experiments[sub][currentExpt].inputSamples[0]}" suppressContextMenu="true"/>
                            </c:when><c:otherwise>
                                [None]
                            </c:otherwise>
                        </c:choose>
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
</form>
<pimsForm:form method="post" action="/UpdatePlateConstruct/org.pimslims.model.holder.Holder:${holder.dbId}" mode="edit" >
  <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="sampleName${sub+1}" alias="Sample Name" helpText="name of the sample, using 'none' to remove the sample" value="" validation="required:true" />
        <pimsForm:text name="sampleRowColStart${sub+1}" alias="Start Position" helpText="Start row,column to be set (eg:A01)" value="" validation="required:true" />
        <pimsForm:text name="sampleRowColEnd${sub+1}" alias="End Position" helpText="End row,column to be set (eg:B02)" value="" validation="required:true" />   
        <pimsForm:editSubmit />
      </pimsForm:column1>
      <pimsForm:column2>
         <pimsWidget:plateSelector id="sampleSelector${sub+1}" callbackFunction="sample_RowColStartEnd_Selector" />        
      </pimsForm:column2>
     </pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>
</c:forEach>
<script type="text/javascript">
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
}

function handleSampleChange(sel,isEmptyWell){
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
	    return searchMore('org.pimslims.model.sample.Sample',sel,callbackFunction);
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

function construct_RowColStartEnd_Selector(obj){
	  var numWells=obj.selectedWells.length;
	  var sub=obj.plate.id.substr(17);//length of id:constructSelector
	  $("constructRowColStart"+sub).value=obj.selectedWells[0].id;
	  $("constructRowColEnd"+sub).value=obj.selectedWells[numWells-1].id;
	}
function sample_RowColStartEnd_Selector(obj){
    var numWells=obj.selectedWells.length;
    var sub=obj.plate.id.substr(14); //length of id:sampleSelector
    $("sampleRowColStart"+sub).value=obj.selectedWells[0].id;
    $("sampleRowColEnd"+sub).value=obj.selectedWells[numWells-1].id;
  }
//call contruct change handler, to display message about matching constructs if needed
handleConstructChange();
</script>

<jsp:include page="/JSP/core/Footer.jsp" />

