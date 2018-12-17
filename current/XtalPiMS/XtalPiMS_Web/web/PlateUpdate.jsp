<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:useBean id="holder" scope="request" type="org.pimslims.model.holder.Holder" />
<jsp:useBean id="holderTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="screens" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="allUsers" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Edit plate' />
</jsp:include>
<pimsWidget:pageTitle title="TrialPlate: ${holder.name}"
    breadcrumbs="<a href='${pageContext.request.contextPath}/ViewPlate.jsp?barcode=<c:out value="${holder.name}" />'>Back to plate</a>"
    actions="<a href='${pageContext.request['contextPath']}/ViewByName/org.pimslims.model.experiment.ExperimentGroup:${holder.name}'>Edit Constructs and Samples </a>"
/>
<pimsWidget:box title="Edit Trial Plate details" initialState="fixed">
 <pimsForm:form method="post" action="/EditPlate/org.pimslims.model.holder.Holder:${holder.dbId}" mode="edit" id="edit_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="barcode" alias="Name" helpText="Barcode or name of the plate" value="${holder.name}" validation="required:true" />
        <pimsForm:select name="ownerId" alias="Owner" validation="required:true">
        <c:forEach var="p" items="${allUsers}">
        <pimsForm:option optionValue="${p.hook}" currentValue="${owner.hook}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
        <pimsForm:select name="operatorId" alias="Operator" validation="required:true">
         <pimsForm:option optionValue="${p.hook}" currentValue="${currentOperator.hook}" alias="${p.name}"/>
        <c:forEach var="p" items="${allUsers}">
        <pimsForm:option optionValue="${p.hook}" currentValue="${currentOperator.hook}" alias="${p.name}"/>
        </c:forEach>
        </pimsForm:select>
             <pimsForm:select name="groupId" alias="Group" validation="required:false">
         <c:forEach var="p" items="${groups}"> 
          <pimsForm:option optionValue="${p.hook}" currentValue="${currentGroupHook}" alias="${p.name}" />
          </c:forEach>
      </pimsForm:select>
      
      <c:set var="isActive" value="${sample.values['isActive']}" />
      <pimsForm:radio name="isDestroyed" value="yes" label="Yes" isChecked="${isDestroyed eq 'Yes'}" alias="Destroyed" />
      <pimsForm:radio name="isDestroyed" value="no"  label="No"  isChecked="${isDestroyed ne 'Yes'}" alias="" />
      
     </pimsForm:column1>
     <pimsForm:column2>
     
       <pimsForm:select name="holderTypeId" alias="Holder Type" validation="required:true">
	    <c:forEach var="p" items="${holderTypes}">
	    <pimsForm:option optionValue="${p.hook}" currentValue="${p.hook}" alias="${p.name}" />
	    
        </c:forEach>
       </pimsForm:select>
       
       <pimsForm:select name="screenId" alias="Screen" validation="required:true">
	 	 <c:forEach var="p" items="${screens}"> 
	 	 <pimsForm:option optionValue="${p.hook}" currentValue="${currentScreenHook}" alias="${p.name}" />
         </c:forEach>
      </pimsForm:select>
       <pimsForm:textarea name="description" alias="Description">${holder.details}</pimsForm:textarea> 
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:editSubmit />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<c:set var="newInspection"><a href="${pageContext.request['contextPath']}/Create/Inspection?holder=<c:out value="${holder.name}" />">New Inspection</a></c:set>
<pimsWidget:box id="inspections" title="Inspections" extraHeader="${newInspection}" initialState="open" >
<c:choose>
        <c:when test="${empty inspections}">
           No inspections recorded.<br/>
        </c:when>
        <c:otherwise>
          <table class="list">
			<tr><th align="left">Inspection Name</th><th align="left">Date</th><th align="left">Imager</th></tr>
			
			<c:set var="listodd" value="listodd" scope="page" />
			<c:set var="style" value="listodd" scope="page" />
			<c:forEach items="${inspections}" var="inspection">
			<tr class="<c:out value="${style}" default="listodd" />">
			  <td><a href="${pageContext.request['contextPath']}/Update/Inspection?name=<c:out value="${inspection.inspectionName}" />">${inspection.inspectionName}</a></td>
			  <td>${inspection.date.time}</td>
			  <td>${inspection.imager}</td>
			</tr>
			<c:choose>
			<c:when test="${style==listodd}"><c:set var="style" value="listeven" scope="page" /></c:when>
			<c:otherwise><c:set var="style" value="listodd" scope="page" /></c:otherwise>
			</c:choose>
			</c:forEach>
			</table>
        </c:otherwise>
    </c:choose>
</pimsWidget:box>

 



<jsp:include page="/JSP/core/Footer.jsp" />

