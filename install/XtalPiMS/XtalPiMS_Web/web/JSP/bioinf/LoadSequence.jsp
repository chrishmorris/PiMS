<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Author: Peter Troshin, STFC Daresbury Laboratory
Date: September 2007
--%>

<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.bioinf.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Load sequence for local similarity search' />
</jsp:include>    


<!-- LoadSequence.jsp -->
<c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}/">Home</a>: 
        <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective">Constructs</a>
    </c:set>
<pimsWidget:pageTitle 
    breadcrumbs="${breadcrumbs}"
    icon="construct.png"
    title="Local sequence similarity search"
/>

<pimsWidget:box title="Local sequence similarity search"   initialState="fixed" >
<pimsForm:form  action="/LocalSW" mode="create" id="local_sw"  method="post">

   <pimsForm:formBlock>
      <pimsForm:textarea name="record" alias="Sequence" helpText="Please paste either FASTA formatted or clear nucleotide or protein sequence here" validation="required:true, dnaOrProteinSequence:true"/>
    </pimsForm:formBlock>

	<pimsForm:formBlock>
		<pimsForm:column1>
	     	<pimsForm:radio alias="Sequence type" name="isDNA" value="false" label="protein" isChecked="true"  onclick="if (this.checked) {document.getElementById('matrix').disabled=''; }"/>
			<pimsForm:radio name="isDNA" value="true" label="DNA"  onclick="if (this.checked) {document.getElementById('matrix').disabled='disabled'; }" />
		</pimsForm:column1>
    </pimsForm:formBlock>

	<pimsForm:formBlock>
		<pimsForm:column1>
		
		<pimsForm:select name="matrix" alias="Amino Acid Substitution Matrix" helpText="Weight Matrix: Measure of similarity between residues in the sequences">
		<c:forEach items="${matrixes}" var="nmatrix">
				<c:choose>
				<c:when test="${nmatrix == defaultMatrix}"> 
					<option value="${nmatrix}" selected="selected">${nmatrix}</option>
				</c:when>
				<c:otherwise>
					<option value="${nmatrix}">${nmatrix}</option>
				</c:otherwise>
				</c:choose>
			</c:forEach>
       </pimsForm:select>

     	<pimsForm:text value="${score}" name="score" alias="Cutoff score" helpText="Smith-Waterman score: Is a cut-off score here. Display only sequences with a score more than defined. Score 0 will display all hits found" validation="required:true, wholeNumber:true, minimum:0, maximum:100000" />
     	<pimsForm:text value="${hitsNum}" name="hitsNum" alias="No. hits to display" helpText="Number of hits to display: 0 - unlimited" validation="required:true, wholeNumber:true, minimum:1" />
		</pimsForm:column1>

     	<pimsForm:column2>

    	<pimsForm:text value="${gaps}" name="gaps" alias="Gap open penalty" helpText="Penalty for opening a gap in the alignment" validation="required:true,  minimum:0, maximum:100" />
     	<pimsForm:text value="${gpen}" name="gpen" alias="Gap extend penalty" helpText="Penalty for the extending the gap in the alignment" validation="required:true,  minimum:0, maximum:10" />
  		</pimsForm:column2>
   </pimsForm:formBlock>
  
   <pimsForm:formBlock>
	<div style="text-align: right; padding-right: 1.9em;">
	<input type="submit" onclick="dontWarn()" value="Find" />
	</div>
   </pimsForm:formBlock>

 </pimsForm:form>
</pimsWidget:box>

<!--
<script type="text/javascript">
Element.observe(window,"load",function(){
  addTooltip("score","Smith-Waterman score","Is a cut-off score here. Display only sequences with a score more than defined. Score 0 will display all hits found");
  addTooltip("matrix","Weight Matrix", "Measure of similarity between residues in the sequences");
  addTooltip("hitsNum","Number of hits to display","0 - unlimited");
  addTooltip("gaps","Gap opening penalty","Penalty for opening a gap in the alignment");
  addTooltip("gpen","Gap extending penalty","Penalty for the extending the gap in the alignment");
});

</script>
-->
<!--end of main page content-->

<jsp:include page="/JSP/core/Footer.jsp" />
