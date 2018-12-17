<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%-- Record a microscope inspection --%>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new inspection' />
</jsp:include>

<pimsWidget:box title="New inspection details" initialState="fixed">
 <pimsForm:form method="post" action="/Create/Inspection" mode="create" id="new_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:select name="holderID" alias="Plate" helpText="the plate of this inspection"  validation="required:true">
         <pimsForm:option optionValue="${holder.dbId}" currentValue="${holder.dbId}" alias="${holder.name}" />
       </pimsForm:select>
       <pimsForm:text name="inspectionName" alias="Name" helpText="name of this inspection" value="${inspectionName}" validation="required:true" />
       
        <pimsForm:select name="imagerID" alias="Imager" helpText="the imager of this inspection" validation="required:true">
        <c:forEach var="p" items="${imagers}">
          <pimsForm:option optionValue="${p.dbId}" currentValue="${defaultImagerID}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
     </pimsForm:column1>
     <pimsForm:column2>

     <pimsForm:textarea name="description" alias="Description">Manually Added</pimsForm:textarea> 
     	
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
