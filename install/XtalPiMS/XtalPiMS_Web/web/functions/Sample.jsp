<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for Samples' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
  : <a href="${pageContext.request['contextPath']}/functions/Functions.jsp" >Functions</a>
</c:set>
<c:set var="icon" value="sample.png" />
<c:set var="title" value="Sample Functions"/>
<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/HelpSamples.jsp" />
</c:set>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Samples">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.sample.Sample">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request.contextPath}/ChooseForCreate/org.pimslims.model.sample.Sample/refSample">New Sample or Reagent Stock</a>
      </li> 
      <li><a href="${pageContext.request.contextPath}/ImportSample">Import Target containing Sample</a>
      </li>
      <li><a href="${pageContext.request.contextPath}/read/SampleProgress?active=true&amp;days_of_no_progress=0&amp;ready=yes&amp;next_exp_type=any&amp;experiment_in_use=no">Active Samples Report</a>
      </li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Recipes">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.sample.RefSample">New</a>
      </li>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample?sampleCategories=Vector">Search Vectors</a></li>
    </ul>
    </pimsWidget:box>    
    
  </div>
  
  <%-- Right column of blocks --%>
  <div class="rightcolumn">    
    <pimsWidget:box initialState="fixed" title="Help">
    <ul><li><h4>Help:</h4>
      <a href="${pageContext.request['contextPath']}/help/HelpSamples.jsp">Sample</a>
    </li></ul> 
    </pimsWidget:box>
  </div>
  
  <%-- Center column of blocks --%>
    <div class="centercolumn">
    <pimsWidget:box initialState="fixed" title="Containers">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.holder.Holder">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.holder.Holder">New</a>
      </li>
    </ul>
    </pimsWidget:box>
    
    <pimsWidget:box initialState="fixed" title="Reference Data">
      <ul>
        <li><h4>Sample Categories:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.SampleCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.SampleCategory">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Chemicals:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.molecule.Molecule?molType=other">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.molecule.Molecule">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Molecule Categories:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.ComponentCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.ComponentCategory">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Hazard Phrases:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.HazardPhrase">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HazardPhrase">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Holder Categories:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.HolderCategory">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HolderCategory">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Holder Types:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.HolderType">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.HolderType">New</a>
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


    
