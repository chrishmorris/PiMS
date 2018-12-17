<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
If no previous Select Crystal experiments have been done on the plate, it's fair game - anyone can take ownership.
But once that's done, we can't change it. 
So we force the user to accept this explicitly, specifying the LN if he has create access to more than one.

Author: Ed Daniel
Date:January 2014
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%-- 
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
--%>
<c:catch var="error">

<%-- Security --%>
<c:set var="barcode"><c:out value="${barcode}"/></c:set>
<c:set var="well"><c:out value="${well}"/></c:set>
<c:set var="sequence"><c:out value="${sequence}"/></c:set>
<c:set var="acronym"><c:out value="${acronym}"/></c:set>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Take ownership of plate: ${barcode}" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Take ownership: Plate ${barcode}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:if test="${takingOwnership}">
	<pimsWidget:box id="" title="Take ownership" initialState="fixed">
		<p>Once you select a crystal from this plate, it will become yours. This cannot be undone.</p>
		<p>Only proceed if you are certain that this is your plate.</p>
		<a href="${pageContext.request.contextPath}/ViewTrialDrops.jsp?barcode=${barcode}&well=${well}">Go back to the drop viewer</a>
	</pimsWidget:box>
</c:if>

<c:choose><c:when test="${isDiamond}">

	<c:set var="explanatoryText">
		<p>Before selecting a crystal, you need to provide the protein sequence.</p>
	</c:set>
	<c:set var="labNotebookField">
		<pimsForm:nonFormFieldInfo label="Project">${owner}</pimsForm:nonFormFieldInfo>
		<input type="hidden" name="_OWNER" value="${owner}"/>
	</c:set>
	<c:set var="acronymField">
			<pimsForm:nonFormFieldInfo label="Protein acronym">${acronym}</pimsForm:nonFormFieldInfo>
			<input type="hidden" name="acronym" id="acronym" value="${acronym}"/>
	</c:set>
	<c:set var="buttonText" value="Set sequence and proceed &gt;&gt;&gt;" />

</c:when><c:otherwise>

	<c:set var="explanatoryText">
		<p>Before selecting a crystal, you need to provide both</p>
		<ul><li>The Diamond "protein acronym" (identifies the protein on Diamond's risk assessment and shipping forms)</li>
		<li>The sequence of the protein</li></ul>
	</c:set>
	<c:set var="labNotebookField">
		<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" objects="${labNotebooks}"  currentValue="" />
	</c:set>
	<c:set var="acronymField">
		<pimsForm:text name="acronym" value="${acronym}" alias="Protein acronym" validation="required:true" helpText="If shipping to Diamond, this must match the acronym on the safety sheet" />
	</c:set>
	<c:set var="buttonText" value="Take ownership and proceed &gt;&gt;&gt;" />

</c:otherwise></c:choose>

<pimsWidget:box id="" title="Sequence information" initialState="fixed">
	<c:choose><c:when test="${fn:length(labNotebooks)==0}">
		<%-- Shouldn't happen. Servlet should have forwarded to CantHarvest.jsp --%>
		<h2>Can't select crystals in plate ${barcode}</h2>
		<p>You don't have Create permission in any Lab Notebooks.</p>
	</c:when><c:otherwise>
		<pimsForm:form action="/update/SelectCrystal/" mode="edit" method="post" id="">
		<pimsForm:formBlock>
		<h2>Sequence information for plate ${barcode}</h2>
			${explanatoryText}
			${labNotebookField}
			${acronymField}
	  		<pimsForm:textarea name="proteinsequence" alias="Protein sequence" validation="required:true,proteinSequence:true">${sequence}</pimsForm:textarea>
	  		<div id="sequences">&nbsp;</div>
			<input type="hidden" name="barcode" value="${barcode}" />
			<input type="hidden" name="well" value="${well}" />
			<input type="submit" name="takeOwnership" value="${buttonText}" onclick="dontWarn()" />
		</pimsForm:formBlock>
		</pimsForm:form>		
	</c:otherwise></c:choose>
</pimsWidget:box>

<script type="text/javascript">
var Ownership={
	managementURL:contextPath+"/update/SelectCrystal/",
	acronymField:null,
	sequenceField:null,
	init:function(){
		Ownership.acronymField=$("acronym");
		Ownership.sequenceField=$("proteinsequence");
		Ownership.watchAcronymField();
		Ownership.getSequences();
	},
	watchAcronymField:function(){
		var field=Ownership.acronymField;
		field.observe("keyup", function(){ Ownership.handleKeyPress(field) });
		field.observe("click", function(){ Ownership.handleKeyPress(field) });
		field.observe("change", function(){ Ownership.handleKeyPress(field) });
	},
	handleKeyPress:function(){
		var elem=Ownership.acronymField;
		elem.value=elem.value.toUpperCase();
		if(elem.keyTimer){ clearTimeout(elem.keyTimer); }
		elem.keyTimer=setTimeout(function(){ Ownership.getSequences(elem)},750);
	},
	getSequences:function(){
		$("sequences").innerHTML="";
		var acro=Ownership.acronymField.value;
		if(""==acro){ return; };
		$("sequences").innerHTML='<img src="'+contextPath+'/skins/default/images/icons/actions/waiting.gif" />';
		new Ajax.Request(Ownership.managementURL, {
			method:"get",
			onSuccess:Ownership.getSequences_onSuccess,
			onFailure:Ownership.getSequences_onFailure,
			parameters:{
				getsequences:"getsequences",
				acronym:acro
			}
		});
	},
	getSequences_onSuccess:function(transport){
		$("sequences").innerHTML="";
		if(!transport.responseJSON){
			Ownership.getSequences_onFailure(transport);
			return;
		}
		if(transport.responseJSON.acronym!=$("acronym").value){
			//field changed since request
			return false;
		}
		var sequences=transport.responseJSON.sequences;
		if(0==sequences.length){
			return false;
		}
		$("sequences").innerHTML="Existing sequences for "+transport.responseJSON.acronym+":";
		sequences.each(function(seq){
			var row='<div style="margin:0.75em 2em;border-bottom:1px solid #999; min-height:2em">';
			row+='<input type="button" value="Copy" class="copy" style="float:right;margin-left:10px;" />';
			row+='<div class="seq">'+seq+'</div>';
			row+='</div>';
			$("sequences").innerHTML+=row;
		});
		$("sequences").select(".copy").each(function(btn){
			btn.onclick=function(){ Ownership.copySequence(btn); }
		});
	},
	getSequences_onFailure:function(transport){
		alert("Sorry, there was an error:\n\n"+msg);
		if(msg.length>200){
			//Probably an HTML error page, our JSON error messages are short.
			var err=window.open();
			err.document.write(msg);
		} else {
			//JSON error, already handled in alert
		}
	},
	copySequence:function(button){
		var dest=$("proteinsequence");
		var seq=$(button).up("div").down("div.seq").innerHTML;
		dest.value=seq;
	}

}
Ownership.init();
</script>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    


<jsp:include page="/JSP/core/Footer.jsp" />