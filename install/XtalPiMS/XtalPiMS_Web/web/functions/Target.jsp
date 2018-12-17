<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for Targets' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
  : <a href="${pageContext.request['contextPath']}/functions/Functions.jsp" >Functions</a>
</c:set>
<c:set var="icon" value="target.png" />
<c:set var="title" value="Target Functions"/>
<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/target/HelpTarget.jsp" />
</c:set>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Targets">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Search</a> 
      </li> 
      <li><a href="${pageContext.request.contextPath}/JSP/Upload.jsp">Download Target</a>
      </li>
      <li><a href="${pageContext.request['contextPath']}/spot/SpotNewTarget">New ORF Target</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          <a href="${pageContext.request.contextPath}/dnatarget/NewDnaTarget">New DNA Target</a>
      </li>
      <li>
		  <a href="${pageContext.request.contextPath}/naturalsourcetarget/NewNaturalSourceTarget">New Natural Source Target</a>
      </li>
      <li><a href="${pageContext.request['contextPath']}/spot/SpotReports">Reports</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          <a href="${pageContext.request['contextPath']}/TargetScoreboard">Scoreboard</a>
      </li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Complexes">
    <ul>
      <li><a href="${pageContext.request.contextPath}/BrowseComplex">List</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          <a href="${pageContext.request.contextPath}/NewComplex">New</a>
      </li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Target Groups">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.target.TargetGroup">New</a>
      </li>
    </ul>
    </pimsWidget:box>
    </div> 
  <%-- Right column of blocks --%>
  <div class="rightcolumn">
    <pimsWidget:box initialState="fixed" title="Reference Data">
      <ul>
        <li><h4>Organisms:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.Organism">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 

            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.reference.Organism">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Target Status:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.TargetStatus">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.TargetStatus">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
        </li>
        <li><h4>Database Names:</h4>
            <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.Database">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.Database">New</a>
              </c:when>
              <c:otherwise><p>New</p></c:otherwise>
            </c:choose>
         </li>
        <li><h4>5'-Primer Extensions:</h4>
           <a href="${pageContext.request['contextPath']}/ExtensionsList">List</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
           <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.molecule.Extension">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

           <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.molecule.Extension">New</a>
        </li>
       </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Help">
      <ul><li><h4>Help:</h4>
              <a href="${pageContext.request.contextPath}/help/target/HelpTarget.jsp" target="_blank" >Target</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
              <a href="${pageContext.request.contextPath}/help/target/HelpComplex.jsp" target="_blank">Complex</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
              <a href="${pageContext.request.contextPath}/help/HelpExtensions.jsp" target="_blank">5'-Extensions</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
              <a href="${pageContext.request.contextPath}/help/HelpMutagenesis.jsp">Mutagenesis</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">         
              <a href="${pageContext.request.contextPath}/help/HelpPrimerOrder.jsp">Ordering Primers</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">

          </li>
          <li>
              <a href="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp" target="_blank" >Sequence Similarity Search</a>

          </li>
      </ul> 
    </pimsWidget:box>
  </div>
  <%-- Center column of blocks --%>
    <div class="centercolumn">
    <pimsWidget:box initialState="fixed" title="Constructs">
    <ul>
      <li><h4>Search:</h4>
      <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective?">All Constructs</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""><br />

      <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective?experimentType=Order">Constructs - Primers not ordered</a></li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Molecules">
    <ul>
      <!-- TODO fix PRIV-71 <li><a href="${pageContext.request.contextPath}/LocalSW">Sequence Similarity Search</a> -->

      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.molecule.Molecule?molType=protein">Search Protein</a> 
          <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">   
     </li>

      <li>
          <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.molecule.Molecule?molType=DNA">Search DNA</a>
      </li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Database References">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.core.ExternalDbLink">Search</a> 
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


