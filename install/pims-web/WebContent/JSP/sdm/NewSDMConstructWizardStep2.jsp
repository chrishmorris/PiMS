<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.sql.*"  %> 
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ page import="org.pimslims.lab.sequence.*"  %>
<%@ pagebuffer='128kb' %>

<c:catch var="error">
<jsp:useBean id="people" scope="request" type="java.util.ArrayList" />
<jsp:useBean id="constructBean" scope="request" type="ConstructBean" />


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New SDM construct: Step 2' />
</jsp:include>
<!-- NewSDMConstructWizardStep2.jsp -->


<style type="text/css">
body { background:white }
div.codons { border:0px solid red; height:4.5em; font-size:80%;}
div.codon  { position:relative; top:0; text-align:center; border-left:5px solid white; float:left; width:3.5em; height:4.5em;}
div.codonhighlight  { position:relative; top:0; text-align:center; border-left:5px solid white; float:left; width:3.5em; height:4.5em; background-color:yellow; }
div.insertingbefore { border-left:5px solid red; }
div.codon input { width:4em; text-align:center; font-size:80%; }
div.codon select {width:4em; text-align:center; font-size:80%; }
div.selectedfordeletion { background-color:red; }
div.selectedforhighlight { background-color:yellow; }
div.codon .deleteicon { cursor:pointer; }
div.codon .inserticon { display:inline; cursor:pointer; position:absolute; top:6px;left:-9px; }
#codon_0 .inserticon { display:none; }
div.count  { float:left; height:4em; }
</style>


<script type="text/javascript">

wildsequence="${dnaSeq}";
sequence="${dnaSeq}";
codons=[];
newCodons=[]; //used in insertion
codonsPerRow=20;
changedCodons=[]; //used in highlighting changes
contextPath="${pageContext.request.contextPath}";

var codonToAAMap=new Hash();
<c:forEach items="${codonTable}" var="row">codonToAAMap.set("${row.triplet}","${row.aminoAcid1Letter}"); </c:forEach>

var aaToCodonList=new Hash();
<c:forEach items="${codonTable}" var="row">aaToCodonList.set("${row.aminoAcid1Letter}", []); </c:forEach>
<c:forEach items="${codonTable}" var="row">aaToCodonList.get("${row.aminoAcid1Letter}").push("${row.triplet}"); </c:forEach>

var preferredCodonMap=new Hash();
<c:forEach items="${preferredCodonTable}" var="row">preferredCodonMap.set("${row.triplet}","${row.aminoAcid}");</c:forEach>

var deletion={ isSelecting:false, start:-1, end:-1 };

//chop sequence into codons and set original string to null
for(i=0;i<sequence.length;i+=3){
  codons.push(sequence.substr(i,3));
}
sequence=null;

function beginDelete(codonNumber){
  deletion.start=codonNumber*1;
  deletion.end=codonNumber*1;
  deletion.isSelecting=true;
  highlightSelectedRange();
  return false;
}
function codonOnMouseOver(codonNumber){
  if(!deletion.isSelecting) { return false; }
  deletion.end=codonNumber*1;
  highlightSelectedRange();
  return false;
}
function codonOnMouseUp(codonNumber){
  if(!deletion.isSelecting) { return false; }
  deletion.end=codonNumber*1;
  doDeletion();
  return false;
}

function doDeletion(){
  if(confirm("Delete the selected range?")){
    if(deletion.end<deletion.start){
      var temp=deletion.end;
      deletion.end=deletion.start;
      deletion.start=temp;
    }
    var numToDelete=(deletion.end-deletion.start)+1;
    codons.splice(deletion.start,numToDelete);

    // look to see if we have deleted any changed codons
    for(var j=changedCodons.length;j>0;j--){
      if (changedCodons[j] >= deletion.start && changedCodons[j] < deletion.start+numToDelete){
    	changedCodons.splice(j,1);
      }
    }

    // make adjustments to changed indexes
    for(var k=0;k<changedCodons.length;k++){
      if (changedCodons[k] > deletion.start) {
  		changedCodons[k]=changedCodons[k]-numToDelete; 
      }	
  	}
  }
  deletion={ start:-1, end:-1, isSelecting:false };
  renderSequence();
}

function highlightSelectedRange(){
  var start=deletion.start;
  var end=deletion.end;
  if(start>end){ 
    var temp=start;
    start=end;
    end=temp;
  }
  var blocks=$("sequenceEditor").select(".codon");
  blocks.each(function(block){
    var blockNum=block.id.substr(6)*1;
    if(blockNum>=start && blockNum<=end){
      block.addClassName("selectedfordeletion");
    } else {
      block.removeClassName("selectedfordeletion");
    }
  });
  if(document.all && deletion.start != deletion.end){
    doDeletion();
  }
}

function insertCodonsBefore(codonNumber){
  $("codon_"+codonNumber).addClassName("insertingbefore");
  var insertionSequence="";
  var insertionIsValid=false;
  while(!insertionIsValid){
    insertionSequence=prompt("Enter the DNA sequence to insert:",insertionSequence);
    if(null==insertionSequence || "undefined"==insertionSequence){
      $("codon_"+codonNumber).removeClassName("insertingbefore");
      return false;
    }
    insertionIsValid=validateEnteredSequence(insertionSequence);
  }
  if(confirm("Insert the following sequence at the highlighted point?\n\n"+insertionSequence)){
    for(i=0;i<newCodons.length;i++){
      codons.splice(codonNumber+i,0,newCodons[i]);
      changedCodons.push(codonNumber+i);
    }
    newCodons=[];
    renderSequence(codonNumber);
  }
}

function validateEnteredSequence(seq){
  seq=seq.toUpperCase();
  if(seq.length%3 != 0){
    alert("Sequence length must be a multiple of 3");
    return false;
  }
  var regex="^[ACTG]*$";
  if(!seq.match(regex)){
    alert("DNA sequence can only contain A, C, T and G");
    return false; 
  }
  newCodons=[];
  for(i=0;i<seq.length;i+=3){
    var newCodon=seq.substr(i,3);
    newCodons.push(newCodon);
    var newAA=codonToAAMap.get(newCodon);
    if(null==newAA || "undefined"==newAA){
      alert("Codon "+newCodon+" cannot be resolved to an amino acid");
      return false;
      }
  }
  return true;
}

function setAminoAcid(dna,codonNumber){
  dna=dna.value;
  var newAA=codonToAAMap.get(dna); 
  if(null==newAA || "undefined"==newAA){
    $("info").innerHTML="Codon "+dna+" cannot be resolved to an amino acid";
    $("sdm_submit").disabled="disabled";
  } else {
    $("info").innerHTML="&nbsp;";
    $("sdm_submit").disabled="";
    codons[codonNumber]=dna;
    if(document.all){
      //IE is "special", so pander to it
      //TODO PIMS-3632 test in IE
      var numOptions=$("amino_"+codonNumber).options.length;
      for(i=0;i<numOptions;i++){
        $("amino_"+codonNumber).removeChild($("amino_"+codonNumber)[0]);
      }
      var keys=codonToAAMap.keys();
      var selectIndex=2;
      for(var i=0;i<keys.length;i++){
        var key=keys[i];
	    var amino=codonToAAMap.get(key);
	    if(key==dna){ selectIndex=i; }
	    var opt=document.createElement("option");
	    opt.value=dna;
	    opt.text=amino;
	    $("amino_"+codonNumber).options.add(opt);
      }
      $("amino_"+codonNumber).options.selectedIndex=selectIndex;
    } else {
      //Firefox code
      $("amino_"+codonNumber).innerHTML=writeCodonBlockOptions(codons[codonNumber]);
    }
  }
}

function writeCodonBlockOptions(actualCodon){
  var html=[];
  var actualAA=codonToAAMap.get(actualCodon);
  
  aaToCodonList.keys().sort().each(function(aa) {
	  //if (actualAA==aa) {
      html.push(writeAAOptions(aa, actualCodon)); 
  });
  return html.join("");
}
  
function writeAAOptions(aa, actualCodon) {
	var html=[];
    html.push('<optgroup label="'+aa+'">');
    aaToCodonList.get(aa).each(function(codon) {
       html.push(writeCodonOption(codon, actualCodon)); 
    });
    html.push("<\/optgroup>"); 
	return html.join("");
}

function writeCodonOption(codon, actualCodon) {
	var html=[];
    var preferred = '';
    if (preferredCodonMap.get(codon)) {
        preferred = 'style="font-weight: bold" title="preferred for ${expressionOrganism}" ';
    } 
    html.push("<option "+preferred);
    if(actualCodon==codon){
      html.push("selected=\"selected\"");
    } 
    html.push(" value=\""+codon+"\">");   
    html.push(codon);
    html.push("<\/option>");
    return html.join("");
}

function renderSequence(start){
  /*
   * TODO improve performance for longer sequences. Don't need to re-render entire
   *      sequence, only from point of first deleted/inserted codon.
   */
  //alert("renderSequence(start)");
  start=0;
  var html=[];  
  $("sequenceEditor").innerHTML="";

  for(var i=start;i<codons.length+${targetStartCodon}-1;i+=codonsPerRow){
  	html.push(writeCodonRow(i));
  }
  document.getElementById("sequenceEditor").innerHTML+=html.join("");
}

function writeCodonRow(start){
  //alert("writeCodonRow("+codons.length+")");	
  var html=[];
  html.push("<div class=\"codons\" id=\"codons"+start+"\">");
  var rowCodons = codonsPerRow;
  if (0==start) {
	  // pad
	  for (var i=1; i<${targetStartCodon}; i++) {
	      html.push(writeBlankBlock());
	  }
	  rowCodons = rowCodons-${targetStartCodon}+1;
  } else {
	  start = start-${targetStartCodon}+1;
  }
  for(var i=start;i-start<rowCodons;i++){
    if(i<codons.length){
      html.push(writeCodonBlock(i));
    } else {
      break;
    }
  }
  html.push(writeCountBlock(i));
  html.push("");
  html.push("");
  html.push("<\/div>");
  return html.join("");
}
function writeBlankBlock(){
	  var html=[];
	  var style="codon";
	  html.push("<div class=\"codon\"><br /><br />");
	  html.push("<input type=\"text\" maxlength=\"3\"  value=\"\" readonly=readonly /><br/>");
	  html.push("<\/div>");
	  return html.join("");
}
function writeCodonBlock(codonNumber){
  var html=[];
  var style="codon";
  
  //for(var j=0;j<changedCodons.length;j++){
  //  if (changedCodons[j]==codonNumber){
  //    style="codonhighlight";
  //  } else {
  //    style="codon";  
  //  }
  //}
  
  //TODO style to show if preferred
  var preferred = ' not preferred';
  var style = ''; // could: ' style="background-color: pink" ';
  if (preferredCodonMap.get(codons[codonNumber])) {
	  preferred = '';
	  style = '';
  }
  html.push("<div class=\"codon\" id=\"codon_"+codonNumber+"\""
		  + style
          +' title="' +codonToAAMap.get(codons[codonNumber])+(codonNumber+${targetStartCodon})+ preferred+ '" '
		  +" onmouseup=\"return codonOnMouseUp("+codonNumber+")\" onmouseover=\"return codonOnMouseOver("+codonNumber+")\">");

  for(var j=0;j<changedCodons.length;j++){
    if (changedCodons[j]==codonNumber){
    	html.push("<div class=\"selectedforhighlight\">");
    }
  }
  html.push("<img class=\"deleteicon\" src=\"${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif\" onmousedown=\"return beginDelete("+codonNumber+")\"/><br/>");
  html.push("<img class=\"inserticon\" src=\"${pageContext.request.contextPath}/skins/default/images/icons/actions/insert.gif\" onclick=\"insertCodonsBefore("+codonNumber+")\" />");
  html.push("<input type=\"text\" maxlength=\"3\" onkeyup=\"setAminoAcid(this,"+codonNumber
		  +") \"value=\""+codons[codonNumber]+"\" "
		  +" id=\"dna_"+codonNumber+"\"/><br/>");
  html.push("<select id=\"amino_"+codonNumber+"\" onmouseover=\"this.style.width='auto'\" onchange=\"updateCodonToMatchAminoAcid(this,"+codonNumber+")\">");
  html.push(writeCodonBlockOptions(codons[codonNumber]));
  html.push("<\/select>");
  for(var j=0;j<changedCodons.length;j++){
	    if (changedCodons[j]==codonNumber){
	    	html.push("<\/div>");
	    }
	  }
  html.push("<\/div>");
  return html.join("");
}
//TODO PIMS-3631 
function writeCountBlock(codonNumber){
  var html=[];
  html.push("<div class=\"count\" id=\"count_"+codonNumber+"\">");
  html.push("<br/>");
  html.push((codonNumber+${targetStartCodon}-1)*3);
  html.push("<br/>");
  html.push(codonNumber+${targetStartCodon}-1);
  html.push("<\/div>");
  return html.join("");
}

function updateCodonToMatchAminoAcid(sel,codonNumber){
	//TODO PIMS-3632
  codons[codonNumber]=sel.value;
  $("dna_"+codonNumber).value=sel.value;
  changedCodons.push(codonNumber);

  var blocks=$("sequenceEditor").select(".codon");
  blocks.each(function(block){
    var blockNum=block.id.substr(6)*1;
    if(blockNum==codonNumber){
      block.addClassName("selectedforhighlight");
    }
  });
}

function getSequence(){  
  var newsequence="";
  for(var i=0;i<codons.length;i++){
	  newsequence=(newsequence+codons[i]);
  }
  var elem = document.getElementById('construct_dna_seq');
  elem.value=newsequence;
}

function checkOnSubmit() {
	var newsequence="";
	for(var i=0;i<codons.length;i++){
		newsequence=(newsequence+codons[i]);
	}
	if (wildsequence==newsequence) {
		alert("There have been no changes to the sequence");
		return false;
	}
	return true;
}

</script>


<h2><a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>
 : 
<!--Add Construct Step ${param['wizard_step']}-->New SDM Construct: <c:out value="${constructBean.name}" /> : Make Mutation</h2>

<pimsWidget:box id="box2" title="Basic Details from Step 1" initialState="closed" >
 <pimsForm:form action="" mode="view" method="">
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1>
   		<pimsForm:text name="construct_id" alias="Construct Id" value="${constructBean.name}" helpText="Unique identifier for the Construct" />
   </pimsForm:column1>
   <pimsForm:column2>
        <pimsForm:text name="des_tm" alias="Primer design Tm" value="${constructBean.desiredTm}" helpText="Tm used for Primer design" />
        <pimsForm:text name="expressionOrganism" alias="Expression Organism" value="${expressionOrganism}" helpText="Organism for codon preferences" />
   </pimsForm:column2> 
  </pimsForm:formBlock>
 </pimsForm:form> 
</pimsWidget:box>

<pimsWidget:box id="box1" title="Mutate Protein Sequence" initialState="open" >
 <pimsForm:form action="/update/CreateMutatedObjective" id="frmStep1" method="post" mode="create" >
 
  <input type="hidden"  name="pims_researchobjective_hook" value="<c:out value='${constructBean.hook}' />"/>
  <input type="hidden"  id="construct_prot_seq" name="construct_prot_seq" value="<c:out value='${constructBean.protSeq}' />"/>
  <input type="hidden"  id="construct_dna_seq" name="construct_dna_seq" value="<c:out value='${constructBean.dnaSeq}' />"/>
  
  
    <!-- TODO remove, value is fixed: -->
  <input type="hidden"  name="design_type" value="SDM"/>
  
  <input type="hidden"  name="construct_id" value="<c:out value='${constructBean.name}' />" />
  <input type="hidden"  name="desired_tm" value="<c:out value='${constructBean.desiredTm}' />" />
  <input type="hidden"  name="wizard_step" value="2"/>
  
 <pimsForm:formBlock>

<div id="insertion"></div>
<div id="info">&nbsp;</div>
<div id="sequenceEditor"></div>

<script type="text/javascript">
renderSequence();
</script>

  </pimsForm:formBlock>
  <pimsForm:formBlock>    
	<div style="text-align:right">
   	<input name="Submit" style="text-align:right" id="sdm_submit" type="submit" value="Next &gt;&gt;&gt;" onclick="getSequence(); return checkOnSubmit()" />
	</div>
  </pimsForm:formBlock>  
 </pimsForm:form>	
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%--  
The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> 
--%>

<!-- /NewSDMConstructWizardStep2.jsp -->
<jsp:include page="/JSP/core/Footer.jsp" />


