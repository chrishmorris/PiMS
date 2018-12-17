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


<jsp:useBean id="users" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new Sample' />
</jsp:include>

<h2><a href='../Search/org.pimslims.model.sample.Sample'> Samples</a> &gt;&gt; New Sample or Reagent Stock</h2>

<pimsWidget:box title="New sample details" initialState="fixed">
 <pimsForm:form method="post" action="/Create/org.pimslims.model.sample.Sample" mode="create" id="new_sample">
   <input type="hidden" id="refsampleId" name="refsampleId" value="${refSampleBean.hook}" />
   <pimsForm:formBlock>
     
     <pimsForm:column1>
       
       <pimsForm:text name="sampleName" alias="Name" helpText="Sample name or Barcode" value="" validation="required:true,unique:{obj:'org.pimslims.model.sample.Sample',prop:'name'}" />
       
       <c:choose>
       	<c:when test="${empty refSampleBean}">
       		<pimsForm:formItem name="sampleRefSample" alias="Recipe" >
				<pimsForm:formItemLabel name="sampleRefSample" alias="Recipe" helpText="Recipe for the sample" validation="required:true" />
				<div class="formfield" >
					<a id="sampleRefSample" href="${pageContext.request.contextPath}/ChooseForCreate/org.pimslims.model.sample.Sample/refSample">Choose Recipe</a>	
				</div>
			</pimsForm:formItem>
      	</c:when><c:otherwise>
      		<pimsForm:formItem name="sampleRefSample" alias="Recipe" >
				<pimsForm:formItemLabel name="sampleRefSample" alias="Recipe" helpText="Recipe for the sample" validation="required:true" />
				<div class="formfield" >
					<pimsWidget:link bean="${refSampleBean}" />
				</div>
			</pimsForm:formItem>
       	</c:otherwise>
       </c:choose>
          
       <%-- 
       <pimsForm:select name="refsampleId" alias="Recipe" validation="required:true" >
	 	 <c:forEach var="p" items="${refsamples}"> 
	 	   <c:choose><c:when test="${p.hook eq refSampleHook}">
             <option value="${p.hook}" selected="selected" ><c:out value="${p.name}" /></option>
           </c:when><c:otherwise>
             <option value="${p.hook}" ><c:out value="${p.name}" /></option>
           </c:otherwise></c:choose>
         </c:forEach>
      </pimsForm:select>
      --%>
      
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
attachValidation("refsampleId", {required:true});
</script>

<jsp:include page="/JSP/core/Footer.jsp" />
