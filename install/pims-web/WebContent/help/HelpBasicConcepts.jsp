<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Basic Concepts in PiMS' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Basic Concepts used in PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <p style="padding-left:1.4em;">PiMS can be used without a detailed understanding of the underlying data model.
    However it is very is helpful to have a basic grasp of the essential PiMS concepts.</p>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif" alt="Target icon" /> <a href="#target">Target</a>
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/construct.gif" alt="Construct icon" /> <a href="#construct">Construct</a></li>
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif" alt="Sample icon" /> <a href="#sample">Sample</a>
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> <a href="#experiment">Experiment</a></li>        
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/protocol.gif" alt="Protocol icon" /> <a href="#protocol">Protocol</a></li>
         <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif" alt="Plate experiment icon" /> <a href="#plates">Plate Experiments</a>
      </ul>
    </div>
    <div class="rightcolumn">
      <ul>
         <li><a href="#labnotebook">Lab Notebook</a><br /><br /></li>
         <li><a href="#types">Typing</a> -of items in PiMS</li>
         <li><a href="#naming">Naming</a> items in PiMS<br /><br /></li>
         <li><a href="#datamodel">Fitting it all together</a>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <div class="leftcolumn">  
      <ul>
        <li><a href="${pageContext.request.contextPath}/help/HelpGettingStarted.jsp">Getting Started with PiMS</a></li>
      </ul>
    </div>
<%--    <div class="rightcolumn">    
      <ul>
      </ul>
    </div>
--%>    <div class="shim"></div>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
<%--
  <h3 id=""></h3>
  <div class="toplink"><a href="#">Back to top</a></div>
--%>

  <!-- ================================================================================ -->      
  <h3 id="target"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif" alt="Target icon" /> Target</h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/target.png" alt="large Target icon"/>
  A <strong>Target</strong> in PiMS typically describes a single full length protein and its associated DNA sequence.<br /> 
  Biological <strong>Complexes</strong> can be represented in PiMS by linking two (or more) PiMS Targets.<br />
  Additional information and links to external references can be associated with the Target record.<br /><br />  
  PiMS also provides support for <strong>DNA Targets</strong>: which do not lead to protein production, 
  and
  <strong>Natural Source Targets</strong>: for which no protein or DNA sequences are available.
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="construct"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/construct.gif" alt="Construct icon" /> Construct</h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/construct.png" alt="large Construct icon" />
  PiMS <strong>Constructs</strong> are linked to Target records.<br />
  Information recorded for Constructs includes details of PCR primers, protein tags, the expression product (if appropriate) and its relationship to the Target (domain, full length etc.)
  and some calculated values for relevant DNA and protein sequences.<br /><br />
  A Construct is usually the starting point for recording Experiments and the Construct details are stored as a <br /><strong>Construct Design</strong> Experiment in PiMS.<br /><br />
  PiMS also supports recording <strong>Primer-less</strong> Constructs such as those created using traditional cloning methods or with synthetic genes.<br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="sample"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif" alt="Sample icon" /> Sample</h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/sample.png" alt="large Sample icon" />
  A <strong>Sample</strong> is a  definition of a physical Sample and can have a creation date, 
  label, owner, location, amount and a description of its contents.<br /><br />
  Samples also have a <strong>Type</strong> or <strong>Category</strong> (e.g. Buffer, Plasmid, Primer, etc.) and are made and used by Experiments.
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="experiment"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> Experiment</h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/experiment.png" alt="large Experiment icon" />
  <strong>Experiments</strong> take 1 (or more) <strong>Input Sample</strong> and make 1 (or more) <strong>Output Sample</strong>.<br />
  e.g. the <strong>Construct design</strong> Experiment makes 3 <strong>Samples</strong> representing
  a DNA template and a Forward and Reverse Primer.<br /><br /> 
  Images and data files can be linked to PiMS Experiments.
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="protocol"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/protocol.gif" alt="Protocol icon" /> Protocol <em>-Experiment template</em></h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/protocol.png" alt="large Protocol icon" />
  A <strong>Protocol</strong> is a reusable user-defined template describing what you record for your Experiments.
  <br />This provides flexibility and makes it easier to record &quot;ad hoc&quot; experimental information and 
  other unexpected data items.<br /><br /> 
  A <strong>Protocol</strong> describes the <strong>Type</strong> of the Samples which can be used as <strong>Input</strong> Samples 
  and to be produced as <strong>Output</strong> Samples.<br />
  This <strong>Typing</strong> of Samples is the key to linking PiMS Experiments to form a <strong>Workflow</strong>
  -the Output sample type of one experiment must match the Input sample type of the next experiment.<br /><br />
  A Protocol also uses <strong>Parameters</strong> to describe everything else you need to record for an <strong>Experiment</strong> such as
  Numerical, Text and Yes/No values e.g.<br />
  &nbsp; -incubation temperature, number of PCR cycles<br />
  &nbsp; -details of incubation conditions<br />
  &nbsp; -was a reagent added?<br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="plates"><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif" alt="Plate icon" /> Plate Experiments</h3>
  <img class="largeIcon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/plate.png" alt="large Plate icon" />
  PiMS can record Experiments in <strong>Plates</strong> by defining a <strong>Holder</strong> to group Experiments together.<br />
  e.g. a 96-well plates uses a Holder describing 96 Experiments in 12x8 format.
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="labnotebook">Lab Notebook</h3>
  All laboratory information recorded in PiMS (Targets, Experiments, Samples etc.) belongs to a specific <strong>Lab Notebook</strong>.<br />
  This forms the basis for the control of <strong>data access</strong> and <strong>data sharing</strong> in PiMS:
  the individual user's ability to record, edit and view information in PiMS.<br />
  Lab notebooks can be used to organise your laboratory information. e.g. A Lab notebook might correspond to a particular 
   a grant or a collaboration.<br /><br />
  The PiMS <strong>Administrator</strong> is able to create new Lab Notebooks and can specify which PiMS users have access to the information.<br />
  Most items recorded in PiMS can be thought of as <strong>pages</strong> of a Lab notebook.
  <br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="types">Typing -of items in PiMS</h3>
  Most items in PiMS are <strong>Typed</strong> in one way or another.<br />
  <strong>Samples</strong> are assigned a <strong>Type</strong> or <strong>Category</strong> <em> &nbsp;&nbsp; -Forward primer, Purified protein, Cell etc.</em> 
  <br />Similarly, <strong>Experiments</strong> are also assigned a <strong>Type</strong> <em> &nbsp;&nbsp; -PCR, Protein purification, Characterisation etc.</em> 
  and <strong>Experiment Types</strong> are used to group together sets of <strong>Protocols</strong> which achieve the same result <em> e.g. Protein purification</em>. 
  <br /><br />
  A particularly important use of <strong>Types</strong> in PiMS relates to the use of Protocols and the construction of <strong>Workflows</strong>.<br />
  The <strong>Protocol</strong> is used to define the <strong>Type</strong> of Samples which can be used as Inputs and Outputs for an Experiment.<br />
  Experiments can be <strong>linked</strong> together by ensuring that the <strong>Output</strong> Sample Type of the first Experiment matches 
  the <strong>Input</strong> Sample Type of the subsequent Experiment.<br /><br />
  Another advantages of using <strong>Typing</strong> in PiMS are that this allows PiMS to <strong>filter</strong> 
  searches of items in the database and offer sensible options by e.g. limiting the choice of Input Samples for an Experiment.<br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h3 id="naming">Naming items in PiMS</h3>
  Most records in PiMS include a <strong>Name</strong>. You cannot use the same name for two different records of the same type, 
  e.g. two Targets cannot have the same name.<br />
  PiMS will sometimes <strong>suggest</strong> a name for you but you can change this.<br /><br />
  For Experiments, the suggested name often includes your <strong>username</strong> to help avoid a clash with an existing record.<br />
  It will also include an <strong>abbreviation</strong> of the experiment type e.g.Clo for Cloning, Lig for Ligation etc.<br /><br />
  You can also use a <strong>Name</strong> to help you track your work. An example would be for work involving isotope-labelled Samples.<br />
  If you include an appropriate abbreviation in the name of an Output Sample e.g. Name.N15 or Name.C13, the value will be included in the Name of the 
  <strong>Next</strong> Experiment and its Output Sample(s). So, when you create a <strong>Workflow</strong> the &quot;label&quot; will be propagated 
  since an <strong>Output</strong> Sample is usually given the same name as the Experiment that made it.
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="datamodel">Fitting it all together</h3>
    <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/simpleDatamodel.png" alt="Simple PiMS datamodel" />
  <h4>A <em>simplified</em> version of the PiMS datamodel to illustrate how PiMS works.</h4>
  <ul>
    <li>A <strong>Target</strong> can describe a single protein or can be linked to other Targets to form a <strong>Complex</strong></li>
    <li>Multiple <strong>Constructs</strong> can be recorded for a <strong>Target</strong> to define different expression products</li>
    <li>A <strong>Construct</strong> is usually the starting point for <strong>Experiments</strong></li>
    <li>A <strong>Protocol</strong> is a user-configurable Experiment template. It is used to describe the <strong>Input</strong>
    and <strong>Output</strong> Samples and any other information to be recorded for Experiments</li>
    <li><strong>Samples</strong> and <strong>Experiments</strong> are interdependent:<br />
    &nbsp; Samples are used as <strong>Input Samples</strong> for Experiments, which in turn may produce more Samples as <strong>Output Samples</strong></li>
    <li><strong>Output Samples</strong> can feed into further Experiments to build up a <strong>Workflow</strong><br />
    This requires that the Output Sample <strong>Type</strong> from the first Experiment matches the Input Sample 
    <strong>Type</strong> for the subsequent Experiment</li>
    <li>The <strong>Type</strong> of Samples which can be used in (Input) or produced by (Output) an Experiment is defined in the Protocol</li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

