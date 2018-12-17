<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new screen' />
</jsp:include>

<pimsWidget:box title="New screen details" initialState="fixed">
 <pimsForm:form method="post" action="/Create/Screen" mode="create" id="new_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="screenName" alias="Name" helpText="name of this screen" value="${inspectionName}" validation="required:true" />
      
        <pimsForm:select name="typeName" alias="Type" helpText="the type of this screen" validation="required:true">
        <c:forEach var="p" items="${screenTypeViews}">
          <pimsForm:option optionValue="${p.name}" currentValue="${p.name}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
       
       <pimsForm:select name="manufacturerName" alias="Manufacturer" helpText="the manufacturer of this screen" validation="required:true">
        <c:forEach var="p" items="${manufacturers}">
          <pimsForm:option optionValue="${p.name}" currentValue="${p.name}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
       
     </pimsForm:column1>
     <pimsForm:column2>

     <pimsForm:textarea name="description" alias="Description">[none]</pimsForm:textarea> 
     	
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
