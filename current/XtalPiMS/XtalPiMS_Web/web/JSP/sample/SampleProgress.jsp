<%@ page contentType="text/html; charset=utf-8" language="java"
  import="java.util.*,org.pimslims.presentation.worklist.*,org.pimslims.lab.*, org.pimslims.servlet.sample.SampleProgress"
%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<jsp:useBean id="sampleBeans" scope="request" type="List<SampleBean>" />
<jsp:useBean id="criteria" scope="request" type="SampleCriteria" />
<jsp:useBean id="typeNames" scope="request" type="List<String>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Sample List' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- OLD -->
<script type="text/javascript">
function toggleAllCheckboxes(checkAllBoxElement){
	var isChecked = checkAllBoxElement.checked;
	var parentForm = checkAllBoxElement.form;
	var boxes=Element.extend(parentForm).getElementsByClassName("behaviour_selectAll");

	for(i=0;i<boxes.length; i++){
		boxes[i].checked=isChecked;
	}
	return true;
}
</script>
<div style="margin-left: 0;">
<form action="${pageContext.request.contextPath}/read/SampleProgress" method="get"  style="background-color: transparent; padding-top: .75em; padding-left: .2em; width: auto;"
 onsubmit="return validateFormFields(this)">
<b> Display </b>
<select name="<%= SampleProgress.ACTIVE %>" style="width: 7em;">
    <c:choose><c:when test="${false==criteria.active}">
				 	<option value="no" selected="selected">inactive</option>
				 	<option value="yes" >active</option>
				 	<option value="any">any</option>
    </c:when><c:when test="${true==criteria.active}">
				 	<option value="no"> inactive</option>
				 	<option value="yes"  selected="selected"> active</option>
				 	<option value="any"> any</option>
    </c:when><c:otherwise>
				 	<option value="no"> inactive</option>
				 	<option value="yes"  > active</option>
				 	<option value="any" selected="selected">any</option>
    </c:otherwise></c:choose>
</select>
<b>samples </b>

<input type="hidden" name="<%=SampleProgress.USER_HOOK%>" value="[none]" />
<%--
<b> assigned to </b>

<select name="<%=SampleProgress.USER_HOOK%>" style="width: 7em;">
	<c:if test="${!empty personHookAssignedTo}">
		<option value="${personHookAssignedTo}" selected="selected"> ${personNameAssignedTo}</option>
	</c:if>

	<option value="[none]"> any</option>

	<optgroup label="Persons as a user">
		<c:forEach items="${ userPersons }" var="entry">
			<option value="${entry.key}"><c:out value='${entry.value}' /></option>
		</c:forEach>
 	</optgroup>
 </select> --%>

<b> which were made before</b>
<input name="<%= SampleProgress.DAYS_OF_NO_PROGRESS %>" id="<%= SampleProgress.DAYS_OF_NO_PROGRESS %>" value="${criteria.daysNoProgress}" style="width: 2em;"/>
<script type="text/javascript">
attachValidation("<%=SampleProgress.DAYS_OF_NO_PROGRESS%>",{required:true,wholeNumber:true,alias:"Number of days"});
</script>

<b> days ago, are</b>
<select name="<%=SampleProgress.READY%>" style="width: 8em;">
    <c:choose><c:when test="${false==criteria.readyForNext}">
				 	<option value="no" selected="selected"> not ready</option>
				 	<option value="yes" > ready</option>
				 	<option value="any"> ready/not ready</option>
    </c:when><c:when test="${true==criteria.readyForNext}">
				 	<option value="no"> not ready</option>
				 	<option value="yes"  selected="selected"> ready</option>
				 	<option value="any"> ready/not ready</option>
    </c:when><c:otherwise>
				 	<option value="no"> not ready</option>
				 	<option value="yes"  > ready</option>
				 	<option value="any" selected="selected"> ready/not ready</option>
    </c:otherwise></c:choose>
</select>

<b> for </b>
<select name="<%=SampleProgress.NEXTEXPTYPE%>" style="width: 7em;">
	<c:choose><c:when test="${empty criteria.expTypeNameReadyFor}">
		<option value="<%=SampleProgress.ANY%>" selected="selected"><%=SampleProgress.ANY%></option>
	 </c:when><c:otherwise>
	 	<option value="<%=SampleProgress.ANY%>"><%=SampleProgress.ANY%></option>
		<option value="${criteria.expTypeNameReadyFor}" selected="selected">${criteria.expTypeNameReadyFor}</option>
	 </c:otherwise></c:choose>

    <c:forEach var="name" items="${typeNames}">
   	 <option value="${name}"> <c:out value="${name}"/></option>
	</c:forEach>
</select>

<b> experiment, and </b>
<select name="<%=SampleProgress.EXPERIMENT_IN_USE%>" style="width: 12em;">
    <c:choose><c:when test="${false==criteria.alreadyInUse}">
				 	<option value="yes">have been used in</option>
				 	<option value="no" selected="selected"> haven't been used in</option>
				 	<option value="any"> have/haven't been used in</option>
    </c:when><c:when test="${true==criteria.alreadyInUse}">
				 	<option value="yes" selected="selected">have been used in</option>
				 	<option value="no" > haven't been used in</option>
				 	<option value="any"> have/haven't been used in</option>
    </c:when><c:otherwise>
				 	<option value="yes">have been used in</option>
				 	<option value="no" > haven't been used in</option>
				 	<option value="any" selected="selected"> have/haven't been used in</option>
    </c:otherwise></c:choose>

 </select>
<b> other experiments. </b>
<br/>
<input  value="Search" type="submit" onclick="dontWarn()" />
</form>
</div>
<c:if test="${empty sampleBeans}">No matching samples found<br /></c:if>

<c:if test="${!empty sampleBeans}">
	<div style="margin-left: 0;">
	<form action="${pageContext.request.contextPath}/read/SampleProgress" method="get" id="worklist" style="background-color: transparent; width: auto;">
    <table class="list active_sample">
		<tbody><tr>
	   		<th>All<br/>

	   			<input name="checkAll" id="checkAll" onclick="toggleAllCheckboxes(this)" type="checkbox"/>
	   		</th>
	   		<th>Name</th>
	   		<th>Active</th>
			<th>Assigned to</th>
			<th>Produced by</th>
			<th>Result</th>
			<th>Days since last progress</th>
			<th>Next possible Exp</th>

			<th>Used in Experiment</th>
		</tr>
	<c:forEach var="sample" items="${sampleBeans}" ><tr>
		<c:if test="${!sample.mayUpdate}">
		    <td><input name="${sample.sampleHook}" disabled
								type="checkbox" /></td>
		</c:if>
		<c:if test="${sample.mayUpdate}">
		    <td><input name="${sample.sampleHook}"  class="behaviour_selectAll"
								type="checkbox" /></td>
		</c:if>
	    <td><a href="${pageContext.request.contextPath}/View/${sample.sampleHook}"><c:out value="${sample.sampleName}" /></a></td>
	    <td>
	    	 <c:choose><c:when test="${false==sample.active}">No
	    	</c:when><c:when test="${true==sample.active}"> Yes
	   		 </c:when><c:otherwise> Unknown
		    </c:otherwise></c:choose>
	    </td>
	    <td><c:if test="${!empty sample.personHookAssignedTo}"><a href="${pageContext.request.contextPath}/View/${sample.personHookAssignedTo}"><c:out value="${sample.personNameAssignedTo}" /></a></c:if></td>
	    <td><c:if test="${!empty sample.lastExpHook}"><a href="${pageContext.request.contextPath}/View/${sample.lastExpHook}"><c:out value="${sample.lastExpType}" /></a></c:if></td>
	    <td>${sample.lastExpResult}</td>
	    <td>${sample.noProgressDays}</td>
	    <td>
	       <c:forEach var="entry" items="${sample.nextExpTypes}">
	       		<a href="${pageContext.request.contextPath}/View/${entry.key}">
	       		<c:out value="${entry.value}" /></a>
	       		<br/>
			</c:forEach>
	    </td>
	    <td>
	           <c:forEach var="entry" items="${sample.expsUsedIn}">
	       		<a href="${pageContext.request.contextPath}/View/${entry.key}">
	       		<c:out value="${entry.value}" /></a>
	       		<br/>
			</c:forEach>
		</td>

	</tr></c:forEach>
	</tbody></table>


<div style="padding-left: .2em;">
	<h3> Assign samples </h3>
	Assign to:
	<select name="<%=SampleProgress.PERSONHOOKNEWASSIGNEDTO%>" style="width: 7em;">
		<option value="[NONE]" >none</option>
			<c:forEach items="${ userPersons }" var="entry">
				<option value="${entry.key}"><c:out value='${entry.value}' /></option>
			</c:forEach>

	 </select>

	<br/>
	<b> Comments </b> <br/><textarea name="<%=SampleProgress.COMMENTS%>" rows="5" style="width: 50%;"> </textarea>

	<br/>
	<input  value="Save" type="submit" />
		<input type="hidden" name="<%= SampleProgress.ACTIVE %>" value="${criteria.active}" />
		<input type="hidden" name="<%= SampleProgress.DAYS_OF_NO_PROGRESS %>" value="${criteria.daysNoProgress}" />
		<input type="hidden" name="<%= SampleProgress.READY %>" value="${criteria.readyForNext}" />
		<input type="hidden" name="<%= SampleProgress.NEXTEXPTYPE %>" value="${criteria.expTypeNameReadyFor}" />
		<input type="hidden" name="<%= SampleProgress.EXPERIMENT_IN_USE %>" value="${criteria.alreadyInUse}" />
--%>
	</form>
	</div>
	</div>
</c:if>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
