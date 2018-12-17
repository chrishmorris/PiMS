<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='PiMS Functions' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
</c:set>
<c:set var="icon" value="blank.png" />
<c:set var="title" value="PiMS Functions"/>
<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/HelpBasicConcepts.jsp" />
</c:set>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Functions">
      <ul>
        <li>
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/target.gif" alt=""> 
          <h4>Targets</h4>
          <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Search</a>
          | <a href="${pageContext.request.contextPath}/functions/Target.jsp">More...</a>
        </li>
        <li>
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/experiment.gif" alt=""> 
          <h4>Experiments</h4>
          <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment">Search</a>
          | <a href="${pageContext.request.contextPath}/functions/Experiment.jsp">More...</a>
        </li> 
        <li>
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/sample.gif" alt=""> 
          <h4>Samples</h4>
          <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.Sample">Search</a>
          | <a href="${pageContext.request.contextPath}/functions/Sample.jsp">More...</a>
        </li> 
        <li>
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/user.gif" alt=""> 
          <h4>Users</h4>
          <a href="${pageContext.request.contextPath}/functions/User.jsp">User Management</a>
        </li>  
        
        
        <li>          
          <h4>Reference data</h4>
          <a href="${pageContext.request.contextPath}/functions/Reference.jsp">More...</a>
        </li>      
      </ul>
    </pimsWidget:box>
  </div>

  <%-- Right column of blocks --%>
  <div class="centercolumn">
    <pimsWidget:box initialState="fixed" title="Help">
      <ul><li>          
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/help.gif" alt=""> 
          <h4>Help</h4>
          <a href="${pageContext.request.contextPath}/functions/Help.jsp">Guide to Using PiMS</a>
        </li> 
        <li>
          <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/help.gif" alt=""> <h4>Help</h4>
          <a href="${pageContext.request['contextPath']}/help/HelpBasicConcepts.jsp">Basic Concepts of PiMS</a> 
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
