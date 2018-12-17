<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for Users' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
  : <a href="${pageContext.request['contextPath']}/functions/Functions.jsp" >Functions</a>
</c:set>
<c:set var="icon" value="person.png" />
<c:set var="title" value="User Functions"/>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Access Rights">
      <ul>
        <li><h4>Users:</h4>
            <c:choose><c:when test="${isAdmin}">
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.User">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

              <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.accessControl.User">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>User Groups:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.UserGroup">Search</a> 
        </li>
        <li><h4>Lab Notebooks:</h4>

            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.core.LabNotebook">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.core.LabNotebook">New</a>
        </li>
        <li><h4>Permissions:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.Permission">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request.contextPath}/access/Permission">Edit</a>
              </c:when><c:otherwise><p>Edit</p></c:otherwise>
            </c:choose>
        </li>
      </ul>
    </pimsWidget:box>
    
  </div>

  <%-- Right column of blocks --%>
  <div class="rightcolumn">&nbsp;</div>
  
  
  <%-- Center column of blocks --%>
    <div class="centercolumn">
    
    <pimsWidget:box initialState="fixed" title="Organisations">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.people.Organisation">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.people.Organisation">New</a>
      </li>
    </ul>
    </pimsWidget:box>
    
  </div>
    
  <%-- This ensures that the following content is pushed below columns --%>
  <div class="shim">&nbsp;</div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

