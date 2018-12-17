<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.presentation.*, java.sql.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>

<jsp:useBean id="spotConstructMilestone" scope="request" type="org.pimslims.presentation.construct.ConstructResultBean" />


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS Milestone: ${spotTarget.protein_name} ${spotConstructMilestone.milestoneName} ' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!--SpotConstructMilestoneDetail.jsp-->
  
<h2><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> 
<c:if test="${null!=targetBean}">
: <a href="${pageContext.request.contextPath}/View/${targetBean.hook}">Target <c:out value="${targetBean.protein_name}" /></a>
</c:if>
<c:if test="${null!=constructBean}">
	: <a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=${constructBean.name}"> Construct <c:out value="${constructBean.name}" /> milestones</a>
</c:if>
: Milestone Detail</h2>
<pims:import className="org.pimslims.model.experiment.Experiment" />

<c:if test="${null!=spotConstruct}">
  <h3>Construct: ${spotConstruct.name}</h3>
</c:if>

<pimsWidget:box id="box1" title="Details:" initialState="open" >
 <pimsForm:form action="/Update" method="post" mode="view" >
     <pimsForm:formBlock id="b1blk1">
		<pimsForm:column1>
			<pimsForm:nonFormFieldInfo label ="Milestone type" >
				<c:out value="${spotConstructMilestone.milestoneName}" />
			</pimsForm:nonFormFieldInfo>
			<pimsForm:nonFormFieldInfo label="Experiment">
				<c:if test="${!empty spotConstructMilestone.experiment._Hook}">
					<a href='../View/${spotConstructMilestone.experiment._Hook}'>${spotConstructMilestone.experiment._Name}</a>
				</c:if>							
			</pimsForm:nonFormFieldInfo>
			<pimsForm:nonFormFieldInfo label ="Experiment Date" >
				<pimsWidget:dateLink date="${spotConstructMilestone.dateOfExperiment}"  />
			</pimsForm:nonFormFieldInfo>
		</pimsForm:column1>
		<pimsForm:column2>
			<c:choose>
				<c:when test="${null!=spotConstructMilestone.experimentHook}">
					<pimsForm:mruSelect hook="${spotConstructMilestone.experimentHook}" rolename="creator" required="false" alias="Scientist" helpText="The scientist responsible for the Experiment" />
				</c:when>
				<c:otherwise>
					<pimsForm:nonFormFieldInfo label ="Scientist" />
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${null!=spotConstructMilestone.experimentHook}">
					<pimsForm:textarea validation="required:false" name="${spotConstructMilestone.experimentHook}:${Experiment['PROP_DETAILS']}" alias="Details" helpText="comments, and details of protocol">
						<c:out value="${spotConstructMilestone.comments}" />
					</pimsForm:textarea>					
				</c:when>
				<c:otherwise>
					<pimsForm:textarea validation="required:false" name="${spotConstructMilestone.experimentHook}:${Experiment['PROP_DETAILS']}" alias="Details" helpText="comments, and details of protocol">
					</pimsForm:textarea>					
				</c:otherwise>
			</c:choose>
		</pimsForm:column2>
     </pimsForm:formBlock>
				<pimsForm:column2>
     		 		<pimsForm:editSubmit/>
				</pimsForm:column2>
 </pimsForm:form>
</pimsWidget:box>



<pimsWidget:files bean="${bean}" />
<jsp:include page="/JSP/core/Footer.jsp" />

