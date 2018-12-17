<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.pimslims.presentation.sample.SampleBean"%>
<%@page import="org.pimslims.presentation.construct.ConstructBean"%>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<pims:import className="<%= org.pimslims.model.sample.Sample.class.getName() %>" />

<style type="text/css">
   table.experiment {border-width: 1; border: solid; text-align: center; background-color: #dff; }
   table.sample {border-width: 1; border: solid; text-align: center; background-color: #ffd; }
   table.expblueprint {border-width: 1; border: solid; text-align: center; background-color: #fdf; }
 </style>
 
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ page import="java.util.*"  %>
<title>Provenance of Sample: <c:out value="${sample.name}" /></title>
</head>
<body>
 
<!-- T2CReport.jsp -->
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Sample History Report for ${headerTitle}' />
</jsp:include>


<script>
function setSample(sample) {
	window.location.href=contextPath+'/read/Provenance/'+sample.hook+window.location.search;
}

function showSampleSearch() {
    openModalWindow(contextPath+"/Search/org.pimslims.model.sample.Sample?isInModalWindow=yes&<%= org.pimslims.servlet.QuickSearch.CALLBACK_FUNCTION %>=setSample",
    		"Choose sample for provenance report");
}
</script>
<c:choose><c:when test="${_MHTML }">
  <%-- to be printed, no controls required --%>
</c:when><c:when test="${empty sample }">
  <script>document.body.onload = showSampleSearch;</script>
</c:when><c:otherwise>
<a href="${pageContext.request.contextPath}/read/Provenance/<c:out 
value="${sample.hook}" />?<%= org.pimslims.servlet.report.T2CReport.ACCEPT %>=<%= org.pimslims.servlet.report.T2CReport.PRESENTATION_PDF 
%>&${requestScope['javax.servlet.forward.query_string']}" >
View as PDF
</a>
&nbsp; <%-- TODO put sample name in the URL, but must urlescape it --%>
<a href="${pageContext.request.contextPath}/Save${requestScope['javax.servlet.forward.request_uri']}/PiMS_Sample_Report.doc?${requestScope['javax.servlet.forward.query_string']}">
View as .doc
</a>
&nbsp;
<a href="#" onclick="showSampleSearch(); return false">Change Sample</a>
&nbsp;
<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.core.Bookmark">
Filters
</a>

<pimsWidget:box title="Filter this report" initialState="closed" extraClasses="noscroll" >



<pimsForm:form id="provenance" mode="edit" method="get" action="/read/Provenance/${sample.hook}">
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
                       helpText="Select experiment details for report"
                       selected="${requestScope['_show:parameter.name'] }"
                       options="${allKeywords}"
/>
<%-- propagate show/hide controls (currently unused) --%>
<c:forEach items="${paramValues['_excluded'] }" var="ex">
    <input name="_excluded" value="${ex }" type="hidden" />
</c:forEach>


                <pimsForm:formItem name="" alias="">
                    <input style="float:right" type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" />
                </pimsForm:formItem>
        
</pimsForm:formBlock>

<%-- TODO move this into widgets.js, and pimsForm:form to bookmark any search --%>
<script>

function saveBookmark(formid, url, defaultName) {
	var form = $(formid);
	if ('get'!=form.method) {alert("Sorry, there is an error in PiMS, you cannot bookmark the result of a post");};
	var qs = $H(form.serialize(true)).toQueryString();
	// TODO see https://prototype.lighthouseapp.com/projects/8886/tickets/1386-regression-prototypejs-171-formserialize-is-incompatible-with-multiselects
	var name = prompt("Please enter the name of the bookmark you want to save:", defaultName);
	if (null!=name) { 
	  new Ajax.Request(contextPath+'/Bookmark', {
		method: 'post',
		onSuccess: function(transport) {alert('Bookmark saved')},
		onFailure:function(transport){ 
			  ajax_onFailure(transport); 
			}, 
		parameters: {name: name, url: url+'?'+qs}
	  });  
    }
	return false;
	
}
</script>
<a href="#" onclick="saveBookmark('provenance', '/read/Provenance', '${bookmark.name }');">Bookmark</a>

</pimsForm:form>
</pimsWidget:box>
</c:otherwise></c:choose>

<br />
<c:forEach var="complex" items="${complexes}"> 

<pimsWidget:box title="Complex: ${complex.name}" initialState="closed" >
	
	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Description" value="${complex.details}" alias="Description" />
	</pimsForm:formBlock>
	</pimsForm:form>
	
</pimsWidget:box>

</c:forEach>


<c:forEach var="target" items="${targets}"> 

<pimsWidget:box title="Target: ${target.name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	
	<pimsForm:formBlock>
		<pimsForm:text name="Organism" value="${target.organismBean.name}" alias="Organism" />
		<pimsForm:text name="Scientist" value="${target.creator.name}" alias="Scientist" />
		<pimsForm:text name="Description" value="${target.func_desc}" alias="Description" />
		
		<c:if test="${!empty target.comments && !keywords['Comments'].filteredOut}">
		    <pimsForm:text name="Comments" value="${target.comments}" alias="Comments" />
		</c:if>
		
		
	
    <pimsForm:dna name="${target.dnaSeqHook }" alias="DNA sequence"  helpText="The DNA sequence of the Target" value="${target.dnaSeq}" />
	
    
    <pimsForm:textarea extraClasses="sequence" name="${target.protSeqHook}" alias="Protein sequence" helpText="The sequence of the Target protein" validation="proteinSequence:true" onchange="this.value=cleanSequence(this.value);">
        <pims:sequence sequence="${target.protSeq}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (residues) of the Target protein sequence" > Length: </strong><c:out value="${fn:length(target.protSeq)}"/>
        &nbsp; <strong title="Molecular weight (Da)"> Weight (Da): </strong><fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${protMass}" /></fmt:formatNumber>
        &nbsp; <strong title="Extinction coefficient cm-1 M-1, unreduced"> Extinction: </strong><c:out value="${protEX}" />
        &nbsp; <strong title="Absorbance of a 0.1% solution (1g/L)"> Abs 0.1&#037;: </strong>
        <fmt:formatNumber type="Number" maxFractionDigits="3" ><c:out value="${abs01pc}" /></fmt:formatNumber>
        &nbsp; <strong title="Isoelectric point pI"> pI: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${protPI}" /></fmt:formatNumber> &nbsp;
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${target.hook}?type=targetProtein" />
        <br /><a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>
      </span>
    </div>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

</c:forEach>


<c:forEach var="constructBean" items="${expblueprints}"> 

<pimsWidget:box title="Construct: ${constructBean.name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
		<pimsForm:text name="Organism" value="${constructBean.organismName}" alias="Organism" />
		<pimsForm:text name="Description" value="${constructBean.description}" alias="Description" />
		<c:if test="${!empty constructBean.comments && !keywords['Comments'].filteredOut }">
		  <pimsForm:text name="Comments" value="${constructBean.comments}" alias="Comments" />
		</c:if>
		<pimsForm:text name="Forward Primer" value="${constructBean.fwdPrimerName} - ${constructBean.fwdPrimer}" alias="Forward Primer" />
		<pimsForm:text name="Reverse Primer" value="${constructBean.revPrimerName} - ${constructBean.revPrimer}" alias="Reverse Primer" />
        
        <c:choose>
			    <c:when test="${keywords['Insert'].filteredOut }"><%-- hide --%></c:when>
				<c:when test="${empty constructBean.pcrProductSeqHook}">
		        </c:when> 
		        <c:otherwise>
		             <pimsForm:dna name="${constructBean.pcrProductSeqHook }" alias="Insert"  helpText="The DNA sequence of the PCR Product" value="${constructBean.pcrProductSeq}" />
		        </c:otherwise>
        </c:choose><c:choose>
			    <c:when test="${keywords['Translated sequence'].filteredOut }"><%-- hide --%></c:when>
				<c:when test="${empty constructBean.protSeq}">
		        </c:when> 
		        <c:otherwise>
		              <pimsForm:protein name="_nonesuch" alias="Translated sequence"  helpText="Translated from the Target DNA sequence" value="${constructBean.protSeq}" />
		        </c:otherwise>
        </c:choose><c:choose>
			    <c:when test="${keywords['Expressed protein'].filteredOut }"><%-- hide --%></c:when>
				<c:when test="${empty constructBean.expressedProteinHook}">
		        </c:when> 
		        <c:otherwise>
		             <pimsForm:protein name="${constructBean.expressedProteinHook}" alias="Expressed protein"  helpText="The sequence expressed by this construct" value="${constructBean.expressedProt}" />
		        </c:otherwise>
        </c:choose><c:choose>
			    <c:when test="${keywords['Final protein'].filteredOut }"><%-- hide --%></c:when>
				<c:when test="${empty constructBean.finalProt}">
		        </c:when> 
		        <c:otherwise>
		            <pimsForm:protein name="${constructBean.finalProteinHook}" alias="Final protein"  helpText="Intended sequence e.g. after cleavage" value="${constructBean.finalProt}" />
		        </c:otherwise>
        </c:choose>
   
    </pimsForm:form>
</pimsWidget:box>

</c:forEach>

 
<c:forEach var="experiment" items="${experiments}"> 

  <c:set var="filteredOut" value=""/>
  <c:choose><c:when test="${filterMap[experiment.hook].filteredOut }">
    <c:set var="filteredOut" value="filteredOut noprint"/>
    <%--<a href="${pageContext.request.contextPath}/read/Provenance/<c:out value="${sample.hook}" 
      />?<%= org.pimslims.servlet.report.T2CReport.ACCEPT 
      %>=<%= org.pimslims.servlet.report.T2CReport.PRESENTATION_HTML 
      %><c:forEach items="${paramValues['_excluded'] }" var="ex"><c:if test="${ex ne experiment.hook }">&_excluded=${ex }</c:if></c:forEach>" 
      title="Add to report: Experiment <c:out value="${experiment.name }" />" >
       TODO add product filter and keyword filter 
      Show
    </a> --%>
  </c:when><c:otherwise>
    <%-- <a href="${pageContext.request.contextPath}/read/Provenance/<c:out value="${sample.hook}" 
      />?${requestScope['javax.servlet.forward.query_string']}&_excluded=${experiment.hook}"
      title="Remove from report: Experiment <c:out value="${experiment.name }" />" 
    >
      Hide
    </a> --%>
  </c:otherwise></c:choose> 
  
  <c:if test="${!filterMap[experiment.hook].filteredOut || !_MHTML }">
  <pimsWidget:box title="Experiment: ${experiment.name} - ${experiment.experimentProtocolName}"  initialState="closed" 
    extraClasses="${filteredOut}"
  >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Protocol" value="${experiment.experimentProtocolName}" alias="Protocol" />
		<c:if test="${!empty experiment.details }"><pimsForm:text name="Details" value="${experiment.details}" alias="Details" /></c:if>
		
		<c:choose>
			    <c:when test="${keywords['Method'].filteredOut }"><%-- hide --%></c:when>
				<c:when test="${empty experiment.method}">
			          <!-- no method in protocol -->
		        </c:when> 
		        <c:otherwise>
		            <pimsForm:text name="Method" value="${experiment.method}" alias="Method" helpText="Method description from protocol" />
		        </c:otherwise>
		    </c:choose>
		   
		   
		<pimsForm:text name="Scientist" value="${experiment.creator}" alias="Scientist" />
		<pimsForm:text name="Start date" value="${experiment.startDateOfExperimentString}" alias="Start date" />
		<pimsForm:text name="End date" value="${experiment.endDateOfExperimentString}" alias="End date" />
		
		
		<c:if test="${!keywords['Reagents'].filteredOut }">
		
        <c:forEach var="input" items="${experiment.protocol.reagents}" varStatus="status" >
          <c:choose>
            <c:when test="${status.first}">
            <c:set var="alias" value="Reagents" />
            </c:when>
            <c:otherwise>
            <c:set var="alias" value="" />
            </c:otherwise>
         </c:choose>
         
         
            <c:choose>
                <c:when test="${keywords['Reagents'].filteredOut }"><%-- hide TODO use filteredOut class as below --%>
                </c:when>
                <c:otherwise>
                    <pimsForm:text name="Input Sample" value="${input.name} : ${input.amount} ${input.recipe.name}" alias="${alias}" />
                </c:otherwise>
            </c:choose>
            
        </c:forEach>
	
		<c:forEach var="input" items="${experiment.inputSampleBeans}" varStatus="status">
			<c:choose>
			<c:when test="${status.first}">
			<c:set var="alias" value="Input Samples" />
			</c:when>
			<c:otherwise>
			<c:set var="alias" value="" />
			</c:otherwise>
			</c:choose>
			
			      
		    <pimsForm:text name="Input Sample" value="${input.inputSampleName} : ${input.amount} ${input.sampleName}" alias="${alias}" />
		    
			
		</c:forEach></c:if>
		
		<c:forEach var="parameter" items="${experiment.parameters}" varStatus="status">
			<c:choose>
			<c:when test="${status.first}">
			<c:set var="alias" value="Parameters" />
			</c:when>
			<c:otherwise>
			<c:set var="alias" value="" />
			</c:otherwise>
			</c:choose>
			
			<c:choose>
			    <%-- TODO could filter all setup parameters --%>			    
			    
                <c:when test="${empty parameter.value}" >
                    <%-- omit this parameter --%>
                </c:when><c:when test="${keywords[parameter.name].filteredOut }">
                  <%-- omit this parameter --%>
			    </c:when><c:when test="${fn:startsWith(parameter.name, '__')}" >
		        	<pimsForm:text name="Parameter" value="${parameter.label} : ${parameter.value}" alias="${alias}" />
		    	</c:when>
                <c:when test="${fn:contains(parameter.name, 'Forward Tag')}" >
                    <pimsForm:text name="Parameter" value="${'N-terminal Tag'} : ${parameter.value}" alias="${alias}" />
                </c:when>
                <c:when test="${fn:contains(parameter.name, 'Reverse Tag')}" >
                    <pimsForm:text name="Parameter" value="${'C-terminal Tag'} : ${parameter.value}" alias="${alias}" />
                </c:when>
		   	<c:otherwise>
					<pimsForm:text name="Parameter" value="${parameter.name} : ${parameter.value}" alias="${alias}" />
				</c:otherwise>
			</c:choose>
			
		</c:forEach>
		
		
        <c:if test="${!keywords['Product'].filteredOut }">
          <c:forEach var="output" items="${experiment.outputSampleBeans}" >
            <pimsForm:text name="Product" alias="Product" value="${output.refSampleName }" helpText="Type of sample produced" />
          </c:forEach>
        </c:if>
		
		<c:set var="imageFiles" value="${images[experiment.hook]}" />
	
		<c:forEach items="${imageFiles}" var="file" varStatus="status">
		<%-- TODO could filter images --%>
			<c:choose>
				<c:when test="${status.first}">
				<c:set var="alias" value="Images" />
				</c:when>
				<c:otherwise>
				<c:set var="alias" value="" />
				</c:otherwise>
			</c:choose>
			
			<tr class="formitem ">
			<td class="fieldname" >
				<label for="Image" ><c:out value="${alias}" /></label>
			</td>
			<td class="formfield" >
				<table><%--TODO <table class="list"> --%>
				<tr>
				<td style="padding-left: 0px;">
					<input readonly="readonly" type="text" name="Image" id="Image"  value="${file.title}" />
					<input readonly="readonly" type="text" name="Image" id="Image"  value="${file.legend}" />
				</td>
				<td style="border-left-width: 0px;">
					<a href="${pageContext.request.contextPath}/ViewFile/${file.hook}/${file.name}"
						onclick='return warnChange()'
						title="view file" >
					<img class="thumbnail" src="${pageContext.request.contextPath}/ViewFile/${file.hook}" />
					</a>
				</td>
				</tr>
				</table>
				
			</td>
			</tr>
			
		</c:forEach>
		
	</pimsForm:formBlock>
	</pimsForm:form>
	
  </pimsWidget:box>
  </c:if>
</c:forEach>

<c:if test="${!empty sample }">


<c:set var="name"><c:out value="${sample.name}" /></c:set>
<pimsWidget:box title="Sample: ${name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
	    <c:if test="${!empty location }">
          <pimsForm:text name="Location" value="${location}" alias="Container" />
          
	<pimsForm:nonFormFieldInfo label="> Position in Container" helpText="Use Move/Remove to change this">
                    ${sample.rowAlphaPosition} 
                  <c:if test="${!holder.isPlate && !empty sample.colPosition }">
                    ,
                  </c:if>
                    ${sample.colPosition}
                  <c:if test="${!empty sample.subPosition }">
                    .${sample.subPosition}
                  </c:if>
            </pimsForm:nonFormFieldInfo>
        </c:if>
        <pimsForm:text name="Role" value="${role}" alias="Role" />
		
		<c:forEach var="category" items="${sampleCategories}" varStatus="status">
            
            <c:choose>
            <c:when test="${status.first}">
            <c:set var="alias" value="Categories" />
            </c:when>
            <c:otherwise>
            <c:set var="alias" value="" />
            </c:otherwise>
            </c:choose>
            
            <pimsForm:text name="Category" value="${category.name}" alias="${alias}" />
            
        </c:forEach>
        
           <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${sample.hook}:pageNumber" value="${sample.values['pageNumber']}"
    />
	
	<pimsForm:amount hook="${sample.hook}" propertyName="${Sample['PROP_CURRENTAMOUNT']}" 
	alias="Amount" value="${amount}" />

	<pimsForm:amount hook="${sample.hook}" propertyName="${Sample['PROP_CONCENTRATION']}" value="${concentration}"
	alias="Concentration"  />
                    
			
			
     		 
<pimsForm:textarea alias="Details" name="${sample.hook}:${Sample['PROP_DETAILS']}" ><c:out value="${sample.values['details']}" /></pimsForm:textarea>
        		        

                <pimsForm:boolean name="${sample.hook}:${Sample['PROP_ISACTIVE']}" value="${sample.values['isActive']}" alias="Stock available" />
				
				
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
   
		
	</pimsForm:formBlock>
	</pimsForm:form>

</pimsWidget:box>
</c:if>
<br />
<br /> 

<br />
<c:choose><c:when test="${_MHTML }">
    <img src="${pageContext.request.contextPath}/read/Dot/${sample.hook}?format=png"   />
</c:when><c:when test="${empty sample }">
    <!-- no diagram to show -->
</c:when><c:otherwise>
<jsp:include page="/read/Dot/" >
    <jsp:param name="hook" value="${sample.hook}" />
    <jsp:param name="format" value="svg" />
</jsp:include>
</c:otherwise></c:choose>
<br />

<c:if test="${!empty sample }">
Cite this page as:<br />
<c:forEach var="author" items="${authors}" varStatus="status">
<c:out value="${author.person.name}"></c:out>
<c:if test="${!status.last}">, </c:if> 
<c:if test="${status.last}">;</c:if>    
</c:forEach>

Provenance of <c:out value="${sample.name}" />; viewed 
<script type="text/javascript">
<!--
var currentTime = new Date()
var month = currentTime.getMonth() + 1
var day = currentTime.getDate()
var year = currentTime.getFullYear()
document.write(day + "/" + month + "/" + year+ ";")
//-->
</script>
<c:out value="${requesturl}"></c:out>
</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
