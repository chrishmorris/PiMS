<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="id" required="true"  %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="sequence" required="true"  %>
<%@attribute name="gcCont" required="true"  %>

<script type="text/javascript">
function add_residue_numbers(ta, tb, length){
	//if(document.all){ return false; } //IE7 horks layout - textarea slides under following textarea - see target sequences
	ta.style.height="auto";
	alert("add_residue_numbers function ta ["+ta.rows+":"+ta.scrollHeight+":"+ta.offsetHeight+"]");
	if (ta.rows<0) { 
		return false; 
	}
	while(ta.scrollHeight>ta.offsetHeight){
	    ta.rows++;
	}
	alert("add_residue_numbers function rows ["+ta.rows+":"+length+"]");
	tb.value="";
	var x = parseInt((length / ta.rows)-1);
	var z = parseInt((x / 20) + 1) * 20;
	var text = "";
	for(i=1;i<=ta.rows;i++){
		if (i==ta.rows) {
			text=text+" "+length;
		} else {	  
		    text=text+" "+(z*i);
		}
	}  
	tb.rows = ta.rows;
	tb.value = text;
}
</script>

<%-- If there is no MolComponent DNA --%>
	<c:choose>
	<c:when test="${empty hook}">
  <c:if test="${mayUpdate}">
		<input type="button" style="float: left;" class="submit" onclick="if(!document.attributeChanged || confirm('Unsaved changes detected, they will be lost if you proceed. Proceed?')) $('addDNA').submit();" value="Add DNA sequence..." />
	</c:if>
	</c:when>
<c:otherwise>

	<pimsForm:formItem extraClasses="textarea" name="${id}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" />
	
	<div class="formfield" style="height:100px; overflow: auto"> 
	
		<c:set var="disabled" value="" />
		<c:if test="${'view'==formMode}">
			<c:set var="disabled" value="readonly=\"readonly\"" />
		</c:if>
	    
	    <%-- Must all be on one line - newlines show up inside the textarea --%>
	    <div style="width:49em; float:left">
		<textarea style="overflow:hidden;" ${disabled} name="${name}" id="${id}_sequence" 
				onchange="onEdit(); ${onchange}" 
				onkeyup="add_residue_numbers(this, document.getElementById('${id}_residues'), ${fn:length(sequence)})" 
				onfocus="add_residue_numbers(this, document.getElementById('${id}_residues'), ${fn:length(sequence)})" ><jsp:doBody var="body"/>
			<pims:sequence sequence="${sequence}" format='DEFAULT' escapeStyle="NONE" />
		</textarea>
		</div>
		<div style="width:3em; float:right">
		<textarea style="overflow:hidden;" readonly="readonly" name="${name}" id="${id}_residues" noedit="true"><jsp:doBody var="body"/>
		</textarea>
		</div>
		   
		<script type="text/javascript">
			alert("alerted");
			add_residue_numbers(document.getElementById("${id}_sequence"), document.getElementById("${id}_residues"), ${fn:length(sequence)});
		</script>
		
	</div>
	
	</pimsForm:formItem>
	
    
    <div class="formitem viewonly" style="padding-left:12.5em">
     <span align='right'>
        <strong title="Length (bp) of the Target DNA sequence" > Length: </strong><c:out value="${fn:length(sequence)}"/>
        &nbsp; <strong title="%GC nucleotides in the Target DNA sequence" > &#037;GC: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${gcCont}"/></fmt:formatNumber>
	    &nbsp; <a class="popup" href="#" onclick="widePopUp('../JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${name}%20DNA" />&amp;seq=<c:out value="${sequence}" />');return false">Fasta pop-up</a>
	 </span>
    </div>
</c:otherwise>
</c:choose>
