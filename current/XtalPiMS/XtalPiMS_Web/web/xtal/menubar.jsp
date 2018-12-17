<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="org.pimslims.presentation.mru.MRUController"%>
<%@page import="org.pimslims.presentation.mru.MRU"%>
<%@page import="org.pimslims.servlet.PIMSServlet"%>
<%@page import="java.util.*"%>

<%--
*************************************************************
* This file contains the menu items for the top menu bar.   *
* It must be in sync with WEB-INF/jspf/menubar.jspf.        *
*************************************************************
--%>


  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/">Xtal Home</a>
    <!-- home submenu would be here -->
  </li>

  

  
  

  <%-- Sample menu  
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/functions/Sample.jsp">Sample</a>
    <ul class="submenu">
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/functions/Sample.jsp">Sample Functions</a></li>

      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.Sample">Search Samples</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/ChooseForCreate/org.pimslims.model.sample.Sample/refSample">New Sample</a></li>
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/read/SampleProgress?active=true&amp;days_of_no_progress=0&amp;ready=yes&amp;next_exp_type=any&amp;experiment_in_use=no">Active Samples Report</a></li>

      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample">Search Recipes</a></li>
     </ul>
  </li> --%>

  <!-- Search -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a href="${pageContext.request['contextPath']}/Search/">Search</a>
    <ul class="submenu">
          <li><a href="${pageContext.request['contextPath']}/ViewPlates.jsp">Plate Experiments</a></li>
          <li><a href="${pageContext.request['contextPath']}/ViewInspections.jsp">Plate Inspections</a></li>
          <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.sample.Sample?sampleCategories=Purified+protein">Samples</a></li>
          <li><a href="${pageContext.request['contextPath']}/ViewConstructs.jsp">Constructs</a></li>
          <li><a href="${pageContext.request['contextPath']}/ViewGroups.jsp">Groups</a></li>
          <li><a href="${pageContext.request['contextPath']}/Search/">More...</a></li>
    </ul>
  </li>
  
  <!-- Screens -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()">
    <a href="${pageContext.request['contextPath']}/IspybResults.jsp">Diffraction</a>
    <ul class="submenu">
      <li><a href="${pageContext.request['contextPath']}/AssembleShipment/?destination=Diamond">Create pins-in-pucks shipment</a></li>
      <li><a href="${pageContext.request['contextPath']}/AssembleShipment/?destination=Diamond&plateshipment=yes">Create plates shipment</a></li>
      <li><a href="${pageContext.request['contextPath']}/RecentShipments">Recent shipments</a></li>
      <li><a href="${pageContext.request['contextPath']}/IspybResults.jsp">Retrieve ISPyB results</a></li>
    </ul>
  </li>
        
  
  <!-- Screens -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a href="${pageContext.request['contextPath']}/ViewScreens.jsp">Screens</a> 
    <ul class="submenu">
      <li><a href="${pageContext.request['contextPath']}/ViewScreens.jsp">Screens</a></li>
      <li><a href="${pageContext.request['contextPath']}/ViewConditions.jsp">Conditions</a></li>
    </ul>
  </li>
  
  <!-- Reference menu -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()">Reference 
    <ul class="submenu">
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.experiment.Instrument">Imagers</a></li>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.ImageType">ImageType</a></li>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.reference.CrystalType">PlateTypes</a></li>
      <li><a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.crystallization.ScoringScheme">ScoringScheme</a></li>
      <!--<li><a href="${pageContext.request['contextPath']}/ViewComponents.jsp">Components</a></li>-->
    </ul>
  </li>


  <!-- help menu -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpIndex.jsp">Help</a>
    <ul class="submenu">
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpIndex.jsp">Guide to using PiMS</a></li>

      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpTargets.jsp">Target Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpComplex.jsp">Complex Help</a></li>
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp" target="_blank" >Sequence Search Help</a></li>

      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/experiment/HelpExperiments.jsp">Experiment Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/experiment/ExperimentGroup.jsp">Experiment Group Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp">Plate Experiment Help</a></li>
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/experiment/protocol/HelpProtocol.jsp">Protocol Help</a></li>

      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpSamples.jsp">Sample Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpPeoplePlaces.jsp">User Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpAccessModel.jsp">Access Help</a></li>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/reference/HelpRefData.jsp">Reference Help</a></li>
      <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/HelpMRU.jsp">History Help</a></li>

	  <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/robots/akta/HelpAktaOverview.jsp">Akta Experiment Help</a></li>
	  <li class="lastinsection"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/robots/HelpCaliper.jsp">Caliper Robot Help</a></li>	
      
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/help/glossary.jsp">Glossary</a></li>
      <li><a onclick="return warnChange()" href='mailto:pims-defects@dl.ac.uk'>Report a problem</a></li>
    
        <!-- <li><a href="${pageContext.request['contextPath']}/Help.jsp">Guide to using Xtal PiMS</a></li> -->
        <% if ("/ViewTrialDrops3.jsp".equals(request.getServletPath())) { %><li><a target="blank" href="${pageContext.request['contextPath']}/ViewTrialDrops3Help.jsp">Help On This Page</a></li><% } %>
        </ul>
  </li>

<%-- labware menu --%>
    <%--
    <% id++; %>
    <li id="menu_<%=id%>" onmouseover="showMenu(<%=id%>)" onmouseout="hideMenus()"><a href="${pageContext.request['contextPath']}/ViewPins.jsf">Labware</a>
        <ul class="submenu" id="submenu_<%=id%>">
            <li><a href="${pageContext.request['contextPath']}/ViewPins.jsf">Pins</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddPin.jsf">+ Add Pin</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddPins.jsf">+ Add Pins</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewCanes.jsf">Canes</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddCane.jsf">+ Add Cane</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewPucks.jsf">Pucks</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddPuck.jsf">+ Add Puck</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewIgloos.jsf">Plate Transporters</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddIgloo.jsf">+ Add Plate Transporter</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewStorageDewars.jsf">Storage Dewars</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddStorageDewar.jsf">+ Add Storage Dewar</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewShipmentDewars.jsf">Shipment Dewars</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddShipmentDewar.jsf">+ Add Shipment Dewar</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewAgents.jsf">Shipping Agents</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddAgent.jsf">+ Add Shipping Agent</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewLocations.jsf">Locations</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddLocation.jsf">+ Add Location</a></li>
        </ul>
    </li>
    --%>
<%-- processes menu --%>
    <%--
    <% id++; %>
    <li id="menu_<%=id%>" onmouseover="showMenu(<%=id%>)" onmouseout="hideMenus()"><a href="${pageContext.request['contextPath']}/ViewProjects.jsf">Processes</a>
        <ul class="submenu" id="submenu_<%=id%>">
            <li><a href="${pageContext.request['contextPath']}/ViewPlates.jsf">Plates</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddPlate.jsf">+ Add Plate</a></li>
            <li><a href="${pageContext.request['contextPath']}/UploadPlate.jsf">+ Upload Plate</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewCrystalDrops.jsf">Crystal Drops</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddCrystalDrop.jsf">+ Add Crystal Drop</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewCryos.jsf">Cryos</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddCryo.jsf">+ Add Cryo</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewDiffractionPlans.jsf">Diffraction Plans</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddDiffractionPlan.jsf">+ Add Diffraction Plan</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewMountedDrops.jsf">Mounted Drops</a></li>
            <li><a href="${pageContext.request['contextPath']}/MountCrystal.jsf">+ Add Mounted Drop</a></li>
            <li><a href="${pageContext.request['contextPath']}/ViewShipments.jsf">Shipments</a></li>
            <li><a href="${pageContext.request['contextPath']}/AddShipment.jsf">+ Add Shipment</a></li>            
            <li><a href="${pageContext.request['contextPath']}/ReturnShipment.jsf">Return Shipment</a></li>            
        </ul>
    </li>
    --%> <%-- <% id++;%>
    <li id="menu_<%=id%>" onmouseover="showMenu(<%=id%>)" onmouseout="hideMenus()">
        <%
            if (user) {
        %>        
        
        <a href="${pageContext.request['contextPath']}/UploadOrder.jsp">Upload</a>
        <ul class="submenu" id="submenu_<%=id%>">
            <li><a href="${pageContext.request['contextPath']}/UploadOrder.jsp">New Order</a></li>
            
            <li><a href="${pageContext.request['contextPath']}/UploadPlateDetails.jsp">Update Plate</a></li>
            <!-- <li><a href="${pageContext.request['contextPath']}/UploadInspection.jsp">New Inspection</a></li> -->
        </ul>
        <% }%>
    </li> --%>