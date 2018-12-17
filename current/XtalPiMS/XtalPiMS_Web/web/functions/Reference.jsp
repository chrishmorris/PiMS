<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for Reference' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
  : <a href="${pageContext.request['contextPath']}/functions/Functions.jsp" >Functions</a>
</c:set>
<c:set var="icon" value="blank.png" />
<c:set var="title" value="Reference Functions"/>
<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp" />
</c:set>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Reference Data">
      <ul>
        <li><h4>Organism:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.Organism">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.reference.Organism">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Target Status:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.TargetStatus">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.TargetStatus">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Database Name:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.Database">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.Database">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Extension [5' for Primer]:</h4>
            <a href="${pageContext.request['contextPath']}/ExtensionsList">Search</a> 
        </li>
        <li><br /></li>
        <li><h4>Experiment Type:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.ExperimentType">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.ExperimentType">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Holder Category:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.HolderCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HolderCategory">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><br /></li>
        <li><h4>Sample Category:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.SampleCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.SampleCategory">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
         </li>
        <li><h4>Chemical:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.molecule.Molecule?molType=other">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.molecule.Molecule">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Molecule Category:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.ComponentCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.ComponentCategory">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
         </li>
        <li><h4>Hazard Phrase:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.HazardPhrase">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HazardPhrase">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
         </li>
        <li><h4>Holder Category:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.HolderCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose><c:when test="${isAdmin}">
              <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HolderCategory">New</a>
              </c:when><c:otherwise><p>New</p></c:otherwise>
            </c:choose>
         </li>
      </ul>
    </pimsWidget:box>
  </div>

  <%-- Right column of blocks --%>
  <div class="centercolumn">
    <pimsWidget:box initialState="fixed" title="Help">
      <ul>
        <li><h4>Help:</h4>
          <a href="${pageContext.request['contextPath']}/help/reference/HelpRefData.jsp">Reference</a> 
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


<!-- OLD LINKS
<a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.protocol.Protocol">Create a New Protocol</a>
<a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.protocol.Protocol">Search All Protocols</a>
<a href="${pageContext.request.contextPath}/Create/org.pimslims.model.sample.RefSample">Create a new Recipe record</a>
<a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.sample.RefSample">Search Recipes</a>
-->