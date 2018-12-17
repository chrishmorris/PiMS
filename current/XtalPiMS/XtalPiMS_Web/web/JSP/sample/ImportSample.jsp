<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<%
//----------------------------------------------------------------------------------------------
//			new_sample.jsp												
//
//
//		 	Created by Marc Savitsky,OPPF-Oxford  				25 May 2007
//			Modified by														Date
//----------------------------------------------------------------------------------------------
%>


<jsp:useBean id="sampleCategories" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Import a Sample' />
</jsp:include>

<c:set var="icon" value="sample.png" />        
<c:set var="title" value="Import Sample"/>
<c:set var="actions">
	  <a href='../Search/org.pimslims.model.sample.Sample'>Search Samples</a> 	
</c:set>
<pimsWidget:pageTitle breadcrumbs="" title="${title}" actions="${actions}" icon="${icon}" />

<pimsWidget:box title="New sample details" initialState="fixed">
 <pimsForm:form method="post" action="/ImportSample" mode="create" id="new_sample">
   <input type="hidden" id="refsampleId" name="refsampleId" value="${refSampleBean.hook}" />
   <pimsForm:formBlock>
     
     <pimsForm:column1>
       
       <pimsForm:text name="sampleName" alias="Name" helpText="Sample name or Barcode" value="" validation="required:true,unique:{obj:'org.pimslims.model.sample.Sample',prop:'name'}" />
       
       <pimsForm:select name="sampleCategoryId" alias="Sample Category" validation="required:true" >
	 	 <c:forEach var="p" items="${sampleCategories}"> 
             <option value="${p.hook}" ><c:out value="${p.name}" /></option>
         </c:forEach>
      </pimsForm:select>
      
       <pimsForm:textarea name="details" alias="Details">[none]</pimsForm:textarea> 
     
     </pimsForm:column1>
     
     <pimsForm:column2>
     
       <pimsForm:select name="assignTo" alias="Assign To" validation="required:false">
        <option value="${currentUser.hook}" selected ><c:out value="${currentUser.name}" /></option>
	    <c:forEach var="p" items="${users}">
	      <c:if test="${p.hook != currentPerson.hook}">
            <option value="${p.hook}" ><c:out value="${p.name}" /></option>
          </c:if>
        </c:forEach>
       </pimsForm:select>
       
       <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook this sample belongs to" objects="${accessObjects}" />
     	
     	<pimsForm:amount hook="initialamount" propertyName="amount" alias="Amount" value='<%= org.pimslims.lab.Measurement.getMeasurement("0mL") %>' />
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>	
     <pimsForm:submitCreate />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<script type="text/javascript">
attachValidation("sampleCategoryId", {required:true});
</script>

<jsp:include page="/JSP/core/Footer.jsp" />
