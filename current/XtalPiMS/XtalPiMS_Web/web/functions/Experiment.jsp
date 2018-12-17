<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
  <jsp:param name="HeaderName" value='Functionality for Experiments' />
  <jsp:param name="extraStylesheets" value='custom/functionpages' />
</jsp:include>

<c:catch var="error">

<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/">Home</a>
  : <a href="${pageContext.request['contextPath']}/functions/Functions.jsp" >Functions</a>
</c:set>
<c:set var="icon" value="experiment.png" />
<c:set var="title" value="Experiment Functions"/>
<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp" />
</c:set>


<%-- div containing all your blocks --%>
<div class="functionblocks">

  <%-- the page title--%>
  <pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" actions="${actions}" />
  <div class="shim">&nbsp;</div>

  <%-- Left column of blocks --%>
  <div class="leftcolumn">
<c:if test="${'Leeds' eq perspective.name}">	 
    <pimsWidget:box initialState="fixed" title="Sequencing Orders"> 

			<!-- 
    <ul>
      <li><a href="${pageContext.request['contextPath']}/update/CreateSequencingOrder" title="Create new sequencing order">New</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> 
       <a href="${pageContext.request['contextPath']}/read/AdvancedSearchSOrders">Search</a></li>
      <li><h4>List New:&nbsp;</h4><a href="${pageContext.request['contextPath']}/read/ListSOrders?ostatus=New" title="List of sequencing orders to be processed (new orders)">Short</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
       <a href="${pageContext.request['contextPath']}/read/SearchSequencingOrders" title="List of sequencing orders to be processed (new orders) with samples">Detailed</a></li>
       <li><h4>List All:&nbsp;</h4><a href="${pageContext.request['contextPath']}/read/UserListSOrders" title="List my group last month sequencing orders ">My Last Month's Orders</a></li>
       <li><a href="${pageContext.request['contextPath']}/read/AdvancedSearchSOrders?dateFrom=${dateFrom}&dateTo=${dateTo}&Search=Search" title="List my group last months samples with reference to orders">My Last Month's Samples</a></li>
    </ul> -->
    </pimsWidget:box>
  
  <pimsWidget:box initialState="fixed" title="List Runs (sequencing admin)"> 
    <ul>
      <li><a href="${pageContext.request['contextPath']}/read/ListSOrders?ostatus=LayoutDone">Planned</a></li>
      <li><a href="${pageContext.request['contextPath']}/read/ListSOrders?ostatus=PlateSetupDone">Awaiting Results</a></li>
      <li><a href="${pageContext.request['contextPath']}/read/ListSOrders?ostatus=Completed" title="List last month's completed runs">Completed</a></li>
    </ul>
  </pimsWidget:box>
</c:if>  
  
    <pimsWidget:box initialState="fixed" title="Single Experiments"> 
    <ul>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.experiment.Experiment?status=To_be_run">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.experiment.Experiment">New</a>
      </li>
    </ul>
    </pimsWidget:box>

    <pimsWidget:box initialState="fixed" title="Plate Experiments">
      <ul>
        <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.experiment.ExperimentGroup">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <a href="${pageContext.request['contextPath']}/CreatePlate">New</a>
        </li>
        <li><a href="${pageContext.request.contextPath}/JSP/BarcodeHolder.jsp">Access by Barcode</a>
        </li>
        <li><a href="${pageContext.request['contextPath']}/JSP/plateSequenceCheck/UploadSequences.jsp">PCR Product Sequence Check</a>
        </li>
      </ul>
    </pimsWidget:box>
    
   <pimsWidget:box initialState="fixed" title="Experiment Groups">
      <ul>
        <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.experiment.ExperimentGroup?_only_experiment_groups=true">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.experiment.ExperimentGroup" title="Record a number of similar experiments, not in a plate">New</a>
        </li>
      </ul>
    </pimsWidget:box>
      
  
  </div>

  <%-- Right column of blocks --%>
  <div class="rightcolumn">

      
    <pimsWidget:box initialState="fixed" title="Stocks">
    <ul>
      <li><a href="${pageContext.request.contextPath}/read/PlasmidStockList">Plasmid Stock List</a>
      <li><a href="${pageContext.request.contextPath}/read/CellStockList">Cell Stock List</a>
      </li>
    </ul>
    </pimsWidget:box>
       
    
    <pimsWidget:box initialState="fixed" title="Reference Data">
      <ul>
        <li><h4>Experiment Types:</h4>
            <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.ExperimentType">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
            <c:choose>
              <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.ExperimentType">New</a>
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
        </li><li><h4>Instrument Types:</h4> <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.reference.InstrumentType">Search</a> 
      <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">  <c:choose>
      <c:when test="${isAdmin}">
                <a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.reference.InstrumentType">New</a>
      </c:when>
      <c:otherwise><p>New</p></c:otherwise>
      </c:choose>
              </li>
      </ul>
    </pimsWidget:box>

    <pimsWidget:box initialState="fixed" title="Help">
      <ul><li>
        <h4>Help:</h4>
        <a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp">Experiment</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
        <a href="${pageContext.request['contextPath']}/help/experiment/ExperimentGroup.jsp">Experiment Group</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
        </li>
        <li>
        <a href="${pageContext.request['contextPath']}/help/experiment/plate/HelpCreatePlateExperiment.jsp">Plate Experiment</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
        <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp">Protocol</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""><br />
        <a href="${pageContext.request['contextPath']}/help/experiment/HelpPlasmidStocks.jsp">Plasmid Stocks</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
        <a href="${pageContext.request['contextPath']}/help/experiment/HelpCellStocks.jsp">Cell Stocks</a>        
      </li></ul>
    </pimsWidget:box>
   
  </div>
  <%-- Center column of blocks --%>
    <div class="centercolumn">    
    <pimsWidget:box initialState="fixed" title="Protocols">
    <ul>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.protocol.Protocol">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.Protocol">New</a>
      </li>
    </ul>
    </pimsWidget:box>

    <pimsWidget:box initialState="fixed" title="Containers">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.holder.Holder">Search</a> <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt="">
          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.holder.Holder">New</a>
      </li>
    </ul>
    </pimsWidget:box>
    
    <pimsWidget:box initialState="fixed" title="Instruments">
    <ul>
      <li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Instrument">Search</a> 
      <img src="${pageContext.request['contextPath']}/skins/default/images/pinkcarrot.png" alt=""> <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Instrument">New</a></li>
      
    </ul>
    </pimsWidget:box>
    
    
    </div>

  <%-- This ensures that the following content is pushed below columns --%>
  <div class="shim">&nbsp;</div>

</div>

</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

