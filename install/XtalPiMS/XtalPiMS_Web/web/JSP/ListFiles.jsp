<%--
Display a list of files associated with a model object
This JSP is called by org.pimslims.servlet.ListFiles


Author: Chris Morris
Date: November 2005
--%>
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Manage files for ${bean.classDisplayName}: ${bean.name}" />
</jsp:include>  
<!--  ListFiles.jsp -->

<pimsWidget:details bean="${bean}" initialState="closed" />
<pimsWidget:files bean="${bean}" initialState="open"  />
