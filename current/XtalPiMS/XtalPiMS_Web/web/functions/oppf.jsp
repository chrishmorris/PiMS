<%-- page header --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for OPPF' />
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
<c:set var="title" value="OPPF Functions"/>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
    <pimsWidget:box initialState="fixed" title="Target">
    <ul>
      <li><a href="${pageContext.request.contextPath}/OpticImporter">Import From Optic</a>
      </li> 
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Operon Primer">
    <ul>
      <li><a href="${pageContext.request.contextPath}/OppfPrimerOrderForm">Primer Order Form</a>
      </li>
    </ul>
    </pimsWidget:box>
    <pimsWidget:box initialState="fixed" title="Plate Experiment">
    <ul>
      <li><a href="${pageContext.request.contextPath}/SequenceResult">Enter Sequencing Results</a>
      </li>
    </ul>
    </pimsWidget:box>
  </div>

  <%-- Right column of blocks --%>
  <div class="rightcolumn">
    <pimsWidget:box initialState="fixed" title="Help">
      <ul>
        <li><h4>Help:</h4>
          <a href="${pageContext.request.contextPath}/help/oppf/HelpOppfFunctions.jsp">OPPF</a> 
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
