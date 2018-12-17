<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new screen' />
</jsp:include>

<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a> :
  <a href="Search/org.pimslims.model.experiment.Experiment?experimentType=Diffraction">Diffraction Experiments</a>
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="Get Diffraction Results"  /> 


<pimsWidget:box title="Get Diffraction Results from Diamond Light Source" initialState="fixed">
 <pimsForm:form method="post" action="/IspybResults" mode="create" >
   <pimsForm:formBlock>
       <pimsForm:text name="userid" alias="DLS Id" helpText="Federal Id" value="" validation="required:true" />
       <pimsForm:password name="password" alias="DLS Password" helpText="Federal password" value="" validation="required:true" />
       <pimsForm:text name="proposal" alias="Proposal" helpText="Beamtime Allocation" value="" validation="required:true" />
       <pimsForm:text name="shipment" alias="Shipment" helpText="crystal shipping" value="" validation="required:true" />
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitButton buttonText="Fetch" />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<c:if test="${null!=beans}">
  <pimsWidget:box title="Data fetched" initialState="open">
        <jsp:include page="/JSP/list/Default.jsp" />
  </pimsWidget:box>
</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if> 
<jsp:include page="/JSP/core/Footer.jsp" />
