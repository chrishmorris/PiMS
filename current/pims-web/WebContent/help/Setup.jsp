<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='PiMS set up' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Setting up a new PiMS Installation</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Preparing PiMS for your laboratory"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      <p>PiMS is a Laboratory Information Management System (LIMS) for use in protein production laboratories.
      It has been designed to manage laboratory information covering the stages from Target selection to the production
      of soluble protein.</p>
      This guide is intended to provide an outline of how to prepare to us PiMS to record your laboratory 
      information. <br />
      More detailed help for PiMS can be found in the <a href="${pageContext.request['contextPath']}/functions/Help.jsp#toc">Guide to using PiMS</a>.<br /><br />
      An overview of the <a href="${pageContext.request.contextPath}/help/HelpBasicConcepts.jsp">Key Concepts</a> used in PiMS is also available and a 
     <a href="${pageContext.request.contextPath}/help/HelpQuickReference.jsp">Quick Reference Guide</a>.       
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul>
        <li><a href="#login">Log in to PiMS</a></li>
        <li><a href="${pageContext.request.contextPath}/help/HelpAccessModel.jsp#navDataOwners">Lab Notebooks</a></li>
        <li><a href="${pageContext.request.contextPath}/help/HelpAccessModel.jsp#navGroups"">User Groups and Permissions</a></li>
        <li><a href="${pageContext.request.contextPath}/help/HelpAccessModel.jsp#navUsers"">Users</a></li>
        <li><a href="#instruments">Instruments</a></li>
        <li><a href="#freezers">Sample storage</a></li>
        <li><a href="#vectors">Vectors</a></li>
     </ul>
    </div>
    <div class="rightcolumn">
      <ul>
         <li><a href="#recordProtocol">Protocols</a>
         </li>
         <!-- TODO <li><a href="#workflow">Constructing a Workflow</a></li> -->
             
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  

  <h3 id="login">Log in to PiMS</h3>
  To prepare PiMS for the other users in your laboratory, you must log in as adminstrator - click <span class="menutext">Log in</span> in the menubar on the PiMS Homepage.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/loginMenubar.png" alt="Homepage with login link highlighted" />
  <br />
  Enter the administrator's username and password in the <strong>Log in box</strong> then click <input value="Log in" type="submit" /><br />
  <div class="indent" >
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/PIMSLoginScreen.png" alt="PIMS log in screen" />
  </div><br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="instruments">Instruments</h3>
  PiMS provides extra facilities if it is aware of the instruments in your laboratory. 
  It isn't useful to record every machine in the lab. Good reasons to record an instrument are:<ul>
    <li>You want to have a calendar to book access to it.</li>
    <li>You have more than one similar instrument, and wish to track which was used.</li>
    <li>The instrument writes files, and you want to save those files.</li>
  </ul>
  You can record an instrument <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Instrument">here</a>. 
  There is also a link back to that page from the <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Instrument">list of instruments</a>.
  If the instrument is a crystal imager, you also need to record each 
  <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.crystallization.WellImageType">image type</a> it records. See the xtalPiMS installation documentation for more details.
  <br />
  <div class="toplink"><a href="#">Back to top</a></div>

   <h3 id="freezers">Sample storage</h3>
  PiMS can help you keep track of your samples, if you tell it about the places where you store them. 
  It keeps a <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.holder.Holder">list of containers</a>
  including dewars and freezers. A freezer contains shelves, which contain boxes, which contain samples. 
  Similarly, a dewar contains pucks which contain pins which carry samples.<br />
  A container has a name in PiMS, which should be its bar code if it has one. It also has a type, e.g. "81 position box". 
  You specify them both when you <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.holder.Holder">record a new container here</a>.
  <br />
  <div class="toplink"><a href="#">Back to top</a></div>

 <h3 id="vectors">Vectors</h3>
  PiMS comes with a standard <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.sample.RefSample?sampleCategories=Vector">list of vectors</a>.
  Some of them will be of no relevance to you. To delete one, click the down arrow beside the name. A menu will appear. Click "Delete".
  <!-- TODO add a vector --> 
  
  <br />
  <div class="toplink"><a href="#">Back to top</a></div>
 
  <!-- ================================================================================ -->
  <h3 id="recordProtocol">Protocols and Experiments</h3>
    A <strong>Protocol</strong> is a user-definable, reusable <strong>Experiment template</strong> which provides much of the flexibility of PiMS. 
    A Protocol allows you to define exactly what information you record for your Experiments and how you link them together to create a workflow.<br /><br />
    
    You can use a Protocol to define the <strong>Type</strong> of <strong>Samples</strong> which are suitable as 
    <strong>Inputs</strong> and <strong>Outputs</strong> for your Experiments. This <strong>Typing</strong> is key to linking Experiments 
    to create a <strong>Workflow</strong> since the Output sample type of one experiment must match the Input sample type of the next experiment.<br /><br />
    A Protocol is also used to define <strong>Parameters</strong> -details and results you would like to (or need to) record.<br />
    You can specify default values or a list of possible values for Parameters.<br /><br />
    
    Protocols are grouped together according to the <strong>Experiment Type</strong> they describe.<br />  
    When you install PiMS it comes with a set of default Protocols which can be used for most protein production 
    work. This includes Protocols of different <strong>Types</strong> describing Construct design, PCR, Cloning, Expression, Purification and Characterisation Experiments.<br />
    Alternatively you can copy and edit the default Protocols, record entirely new Protocols or import Protocols from another PiMS installation.<br />

  <!-- ................................................................................ -->
    <h4>Removing Protocols</h4>
    View the <a href="${pageContext.request['contextPath']}/Search/org.pimslims.model.protocol.Protocol">list of protocols</a> in PiMS. 
    Some of them will be of no relevance to you. 
    To delete it, click the down arrow beside the name of the protocol. A menu will appear. Click "Delete". 
    <!-- TODO screen shot -->
    (Note that you cannot delete a protocol that has been used to record an experiment.)<br />
    Some protocols will be of no current use, but you may wish to keep them for the future. 
    Click "View", then "Edit", then change "Is for use" to "false", and click "Save Changes". 
    This makes the protocol unavailable for experiments, but you can bring it back into use later.<br /> 
    
    
  <!-- ................................................................................ -->
    <h4>Editing a Protocol</h4>
    If one of the protocols is nearly suitable, then it is worth editing it now to get it just right. See 
    <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp#inputsamples">Input Samples</a> and    
    <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp#parameters">Parameters</a>.
    
     
<h4>Recording a Protocol</h4>
    If you have other Standard Operating Procedures that do not match existing protocols, then see the <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp#createprotocol">New Protocol Help</a>. 

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

