<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display a list of model objects.
This JSP is called by org.pimslims.servlet.Search

Author: Chris Morris
Date: 14 November 2005
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<%@ page import="org.pimslims.model.experiment.Experiment" %>
<!-- search/org.pimslims.model.experiment.Experiment  -->
<c:catch var="error">

<c:if test="${!_MHTML }">
<script type="text/javascript">
var submittedProtocol="${param['protocol']}"; //the value submitted in any previous POST/GET

function expt_exptypeOnchange(){
  var val=$("<%=Experiment.PROP_EXPERIMENTTYPE %>").value;
  if(""==val) {
    $("protocol_options").innerHTML="[any]";
    return false;
  }
  $("protocol_options").innerHTML="<img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif'/>";

  new Ajax.Request("${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.ExperimentGroup", {
	method:"get",
	parameters:"isAJAX=true&experimentType="+val,
	onSuccess:function(transport){
	  expt_getProtocolsOnSuccess(transport);
	},
	onFailure:function(transport){
	  ajax_default_onFailure(transport);
	},
	onCreate: function(transport) {
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 10000); //10-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });
}

function expt_getProtocolsOnSuccess(transport){
  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var protocols=docRoot.getElementsByTagName("object");

  var html="";
  if(0==protocols.length){
    html+="[any]";
    html+="<input type='hidden' name='<%=Experiment.PROP_PROTOCOL %>' id='<%=Experiment.PROP_PROTOCOL %>' />";
  } else {
    html+="<select name='<%=Experiment.PROP_PROTOCOL %>' id='<%=Experiment.PROP_PROTOCOL %>' >";
    html+="<option value=''>[any]<\/option><optgroup label='Active protocols'>";
    for(var i=0;i<protocols.length;i++) {
      var attr=protocols[i];
	  var hook=attr.getAttribute("hook");
	  var name=attr.getAttribute("name");
	  if(hook==submittedProtocol){
	    html+="<option id='prot"+i+"' value='"+hook+"' selected='selected'>"+name+"<\/option>";
	  } else {
	    html+="<option id='prot"+i+"' value='"+hook+"'>"+name+"<\/option>";
	  }
    }
    <%-- TODO add inactive protocols --%>
    html+="</optgroup><\/select>";
  }
  $("protocol_options").innerHTML=html;
}

function new_experiment() {
	var type = document.getElementById('<%=Experiment.PROP_EXPERIMENTTYPE %>').value;
	if (type!="") {
		window.location=contextPath+"/Create/org.pimslims.model.experiment.Experiment?experimentType="+type;
	} else {
		window.location=contextPath+"/Create/org.pimslims.model.experiment.Experiment";
	}
	
}

</script></c:if>

<jsp:useBean id="searchMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="experimentTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />
<%-- 
<jsp:useBean id="_labNotebooks" scope="request" type="java.util.Map<org.pimslims.presentation.ModelObjectShortBean, java.lang.String>" />
--%>

<c:set var="actions">
  <c:if test="${!_MHTML }">
	<pimsWidget:linkWithIcon name=""
        		icon="actions/create/experiment.gif" 
        		url="javascript:new_experiment()"
        		text="New experiment"/>
	<pimsWidget:linkWithIcon  name=""
        		icon="types/small/plate.gif" 
        		url="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.ExperimentGroup" 
        		text="Search plate experiments"/>
  </c:if>
</c:set>
<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/Search/">Search</a>
</c:set>
<pimsWidget:pageTitle icon="experiment.png"
	title="Search experiments"
	actions="${actions}"
	breadcrumbs="${breadcrumbs}" />

<div style="margin-right:10em" class="slimline_forms">


<pimsWidget:pageControls>
	<input type="hidden" name="status" value="${fn:escapeXml(param.status)}"/>
	<c:if test="${!empty param.experimentType}"><input type="hidden" name="experimentType" value="${param.experimentType}"/></c:if>
	<c:if test="${!empty param.name}"><input type="hidden" name="name" value="${fn:escapeXml(param.name)}"/></c:if>
	<c:if test="${!empty param.details}"><input type="hidden" name="details" value="${fn:escapeXml(param.details)}"/></c:if>
	<c:if test="${!empty param.protocol}"><input type="hidden" name="protocol" value="${param.protocol}"/></c:if>
</pimsWidget:pageControls>

<pimsWidget:quickSearch initialState="open" value="${fn:escapeXml(param['search_all'])}" />

<c:if test="${!_MHTML }">
<pimsWidget:box title="Advanced Search" initialState="closed" extraClasses="noscroll">
	<pimsForm:form mode="edit" method="get" action="/Search/org.pimslims.model.experiment.Experiment" onsubmit="return true;">
		<input type="hidden" name="pagesize" value="${param.pagesize}"/>
		<pimsForm:formBlock>
			<pimsForm:column1>
			    <pimsForm:date name="_START" alias="Start" helpText="Beginning of time range to report" value="${start}" />
				 
				<pimsForm:select name="_presentation" alias="Presentation" helpText="Preferred display of search results" >
				    
				     <pimsForm:option currentValue="${param['_presentation']}" optionValue="List" alias="List" helpText="A table of experiments" />
				     <pimsForm:option currentValue="${param['_presentation']}" optionValue="KPI" alias="KPI" helpText="Attrition Bar Chart" />
				     <pimsForm:option currentValue="${param['_presentation']}" optionValue="throughput" alias="Throughput" helpText="Construct throughput table" />
				</pimsForm:select> 

				<pimsForm:select name="status" alias="Status">
				    <c:choose>
				     
				     <c:when test="${ ''==param['status']}">
				      <option value="" selected="selected">[any]</option>
				     </c:when>
				     <c:otherwise>
				      <option value="" >[any]</option>
				    </c:otherwise>
				    </c:choose>
				     <pimsForm:option currentValue="${param['status']}" optionValue="To_be_run" alias="To be run" />
				     <pimsForm:option currentValue="${param['status']}" optionValue="In_process" alias="In process" />
				     <pimsForm:option currentValue="${param['status']}" optionValue="OK" alias="OK" />
				     <pimsForm:option currentValue="${param['status']}" optionValue="Failed" alias="Failed" />
				</pimsForm:select>

				<pimsForm:selectMultiple  alias="Lab Notebooks" name="access" 
                       helpText="Filter by Lab Notebook"
                       selected="${paramValues['access'] }"
                       options="${labNotebooks}"
                    />
                
			</pimsForm:column1>
			
			<pimsForm:column2>
                <pimsForm:date name="_END" alias="End" helpText="End of time range to report" value="${null}" />
				<pimsForm:select alias="Experiment Type" name="<%=Experiment.PROP_EXPERIMENTTYPE %>" onchange="expt_exptypeOnchange()">
				    <option value="">[any]</option>
			    	<c:forEach items="${experimentTypes}" var="type">
			    		<pimsForm:option currentValue="${param['experimentType']}" optionValue="${type.hook}" alias="${type.name}" />
			    	</c:forEach>
				</pimsForm:select>

			    <pimsForm:formItem name="protocol" alias="Protocol">
				    <pimsForm:formItemLabel name="protocol" alias="Protocol" />
					<div id="protocol_options">[any]</div>
				</pimsForm:formItem>

			    <pimsForm:formItem name="" alias="">
					<input style="float:right" type="submit" name="SUBMIT" value="Search" onclick="dontWarn()" />
				</pimsForm:formItem>
				
				
			</pimsForm:column2>
		</pimsForm:formBlock>
		
		<c:set var="keyword" value="<%= org.pimslims.servlet.report.T2CReport.KEYWORD %>" />		 
        <pimsForm:selectMultiple  alias="Show Parameters:" name="${keyword }" 
                       helpText="Select experiment details for table"
                       selected="${paramValues[keyword] }"
                       options="${allKeywords}"
        />  
	</pimsForm:form>
</pimsWidget:box>
</c:if>
<%-- TODO 
<pimsWidget:box title="Filter this report" initialState="closed" extraClasses="noscroll" >

<pimsForm:form action="/Bookmark" mode="edit" method="post">

                <pimsForm:formItem name="" alias="">&nbsp;
                    <input  name="name" title="Filter name" value="${bookmark.name }" />
                    <input  name="url" type="hidden" value="/Search/org.pimslims.model.experiment.Experiment?${requestScope['javax.servlet.forward.query_string']}" />
                    <input " type="submit" name="SUBMIT" value="Save Filter" onClick="dontWarn()" />
                </pimsForm:formItem>
</pimsForm:form>

<pimsForm:form mode="edit" method="get" action="/Search/org.pimslims.model.experiment.Experiment${sample.hook}">
<pimsForm:formBlock>
<c:set var="product" value="<%= org.pimslims.servlet.report.T2CReport.PRODUCT %>" />
<pimsForm:selectMultiple  alias="Show only experiments making:" name="${product }" 
                       helpText="Category of output sample produced"
                       selected="${selectedProducts }"
                       options="${sampleCategoryList}"
/>
<br />
<c:set var="keyword" value="<%= org.pimslims.servlet.report.T2CReport.KEYWORD %>" />
<pimsForm:selectMultiple  alias="Show Keywords:" name="${keyword }" 
                       helpText="Select experiment details for table"
                       selected="${requestScope['_show:parameter.name'] }"
                       options="${allKeywords}"
/>


                <pimsForm:formItem name="" alias="">
                    <input style="float:right" type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" />
                </pimsForm:formItem>
        
</pimsForm:formBlock></pimsForm:form>
</pimsWidget:box> --%>
<%-- TODO script 
<pimsWidget:box title="Script" initialState="closed">
  select experiment from Experiment as experiment where<br />
  <textarea name="_script" style="width: 90%"></textarea><br />
  and experiment.access in (:labnotebooks);
  <input style="float:right" type="submit" name="SUBMIT" value="Search" onclick="dontWarn()" />
</pimsWidget:box>
--%>
<c:if test="${!_MHTML }">
  <!--update the select after search-->
  <script type="text/javascript">
  expt_exptypeOnchange(); //make an AJAX request to get the protocols for the expType
  </script>
</c:if>
</div>

<strong>${totalRecords}</strong> single experiments recorded in the database<br/>
<strong>${resultSize}</strong> match search criteria

<hr />

<form id="selectedExperiments" action="${pageContext.request.contextPath}/report/ReportExperimentParameters" method="get"  style="background-color: transparent; width: auto;">

<h2>
  <c:if test="${null!=experimentType}">
    <a href="../View/${experimentType._Hook}"><c:out value="${experimentType.name}" /></a>
  </c:if>
  ${resultSize} Single Experiments
</h2>


<c:if test="${!empty experimentType._Hook}" >
	<a href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?experimentType=${experimentType._Hook}">
	Record a new <c:out value="${experimentType.name}" /> Experiment</a><br /><br />
</c:if>

 
<c:choose><c:when test="${empty experiments}" >
    <h2>No Single Experiments found</h2>
</c:when><c:otherwise>
    
    <%
        request.setAttribute("metaClass", searchMetaClass);
        request.setAttribute("beans", experiments);
        request.setAttribute("chooseExp", true);
    %>
    <c:set var="experimentClassName"><%=Experiment.class.getName()%></c:set>
    <pimsWidget:box title="Experiments found" initialState="open">
    <form id="selectedExperiments" action="${pageContext.request.contextPath}/report/ReportExperimentParameters" method="get"  style="background-color: transparent; width: auto;">
    
	    <jsp:include page="/JSP/list/org.pimslims.model.experiment.Experiment.jsp" />
		
			<%--<div style="padding:0.25em 0.5em"><input type="submit" name="SUBMIT" value="Compare Parameters of Selected Experiments" /></div> --%>
		
			<!--Make delete collection of experiment button if the user is an administrator  -->
			<c:if test="${isAdmin}">
			<div style="padding:0.25em 0.5em">
			<input type="button" value="Delete selected single experiments" onclick="search_exp_deleteSelected(this);" />
			</div>
		</c:if>
		<script type="text/javascript">
		function search_exp_deleteSelected(elm) {
			if (confirm('Delete selected single experiments?')) {
				elm.form.action="${pageContext.request.contextPath}/update/DeleteCollection/${experimentClassName}"
				elm.form.method="POST"
				elm.form.submit()
			}
		}
		</script>
		</c:if> 
        </div> --%>
	</pimsWidget:box>
</c:when><c:when test="${ param['_presentation'] eq 'KPI' }">
    <pimsWidget:box title="Experiments found" initialState="open">
      <pimsWidget:barChart bean="${chart}"  />
    </pimsWidget:box>
</c:when><c:when test="${ param['_presentation'] eq 'throughput' }">
  <pimsWidget:box title="Experiments found" initialState="open">
    <table class="list">
      <thead><tr><th>Construct</th>
        <c:forEach items="${throughput.columnInterator}" var="column" ><th><c:out value="${column.name }" /></th></c:forEach>
      </tr></thead>
      <tbody><c:forEach items="${throughput.rowInterator}" var="row" >
        <tr>
          <th><c:out value="${row.name }" /></th>
          <c:forEach items="${row.cellIterator}" var="cell" ><td>
            <c:set var="value" value="${cell.value}" scope="request"></c:set>            
            <c:choose><c:when test="${fn:contains(value, 'Calendar') }" >
              <pimsWidget:dateLink date="${value }" />
            </c:when><c:otherwise>
              <c:out value="${value }" /> 
            </c:otherwise></c:choose>         
          </td></c:forEach>
        </tr>
      </c:forEach></tbody>
      </table>
    </pimsWidget:box>
</c:when><c:otherwise>
  Sorry, presentation: ${ param['_presentation'] } is not supported
</c:otherwise>
</c:choose>

</form>
</c:if>####


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

