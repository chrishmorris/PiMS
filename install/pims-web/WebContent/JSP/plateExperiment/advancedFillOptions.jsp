<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<c:catch var="error">
<!-- advancedFillOptions.jsp -->
<%--
<div>
<h4><span style="font-weight:bold;color:red">TODO </span>One sample, gradient/snake</h4>
<form action="#">
    Sample: <select></select>
    <input type="button" value="Next &gt;" />
</form>
</div>
--%>

<script type="text/javascript">
function disableFormOnSubmitCopy(){
	document.getElementById('cfpcopyspinner').style.display='';
	window.setTimeout("document.getElementById('submitcopybutton').disabled='disabled'",50);
}
function disableFormOnSubmitMerge(){
	document.getElementById('cfpmergespinner').style.display='';
	window.setTimeout("document.getElementById('submitmergebutton').disabled='disabled'",50);
}
</script>
<div>
<h4>Copy from a plate (or group of plates)</h4>
    <c:choose><c:when test="${1>fn:length(groups)}">
        No plate groups containing ${sampleCategory.name} found.
    </c:when><c:otherwise>
		<form action="${pageContext.request.contextPath}/CopyFromPlate" method="get">
		    Plate groups containing ${sampleCategory.name}: <select name="inputGroup">
		    <c:forEach var="grp" items="${groups}">
		        <option value="${grp._Hook}">${grp.name}</option>
		    </c:forEach>
		    </select><br/>
	        <input type="hidden" name="refInputSample" value="${refInputSample._Hook}" />
	        <input type="hidden" name="outputGroup" value="${experimentGroup._Hook}" />
	        <input type="submit" id="submitcopybutton" value="Next &gt;" onclick="disableFormOnSubmitCopy()" />
	        <img id="cfpcopyspinner" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" style="display:none" />
		</form>
	</c:otherwise></c:choose>
</div>


<div>
<h4>Merge by score</h4>
	<c:choose><c:when test="${1>fn:length(groups)}">
        No plate groups containing ${sampleCategory.name} found.
    </c:when><c:otherwise>
		<form action="${pageContext.request.contextPath}/read/MergeByScore" method="get">
    		<!--
    		<input type="radio" name="relationship" value="parentPlate" />
    		-->
    		Plate 
    		<select name="parentGroup">
    			<c:forEach var="grp" items="${siblingGroups}">
		    		<option value="${grp._Hook}">${grp.name}</option>
				</c:forEach>
    		</select> and its siblings<br/>
    
    		<!-- 
    		<input type="radio" name="relationship" value="grandparentPlate" />Children of plate 
    		<select name="grandparentGroup">
    			<c:forEach var="grp" items="${parentGroups}">
		    		<option value="${grp._Hook}">${grp.name}</option>
				</c:forEach>
    		</select><br/>
    		 -->
    		<input type="hidden" name="refInputSample" value="${refInputSample._Hook}" />
			<input type="hidden" name="outputGroup" value="${experimentGroup._Hook}" />
    		<input type="submit" id="submitmergebutton" value="Next &gt;" onclick="disableFormOnSubmitMerge()" />
    		<img id="cfpmergespinner" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" style="display:none" />
		</form>
	</c:otherwise></c:choose>
</div>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

