<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='New Complex' />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/BrowseComplex">Complexes</a></c:set>
<c:set var="icon" value="complex.png" />
<c:set var="title">New Complex</c:set>
<%-- 
<pimsWidget:pageTitle icon="complex.png" title="${title}" breadcrumbs="${breadcrumbs}" />
--%>
<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" />

<pimsWidget:box title="New complex details" initialState="fixed">
 <pimsForm:form method="post" action="/NewComplex" mode="create" id="new_complex">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="complexname" alias="Complex name" helpText="The Complex name" validation="required:true, unique:{obj:'org.pimslims.model.target.Target',prop:'name'}" />
     </pimsForm:column1>
     <pimsForm:column2>
     	<pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook this complex belongs to" objects="${accessObjects}" />
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
      <pimsForm:textarea name="complexwhychosen" alias="Why Chosen"  />
      <pimsForm:textarea name="complexdetails" alias="Details" />
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate onclick="dontWarn()" />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

</c:catch>
<c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error /> 
</c:if>
   
<jsp:include page="/JSP/core/Footer.jsp" />
