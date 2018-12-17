<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Getting started guide' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Getting Started with PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      <p style="padding-left:.6em; ">PiMS is a Laboratory Information Management System (LIMS) for use in protein production laboratories.
      It has been designed to manage laboratory information covering the stages from Target selection to the production
      of soluble protein.
      This guide is intended to provide an outline of how to start using PiMS to record your laboratory 
      information. It focuses on the simplest route for recording progress toward the production 
      of pure protein samples. 
      More detailed help for PiMS can be found in the <a href="${pageContext.request['contextPath']}/functions/Help.jsp#toc">Guide to using PiMS</a>.<br /><br />
      An overview of the <a href="${pageContext.request.contextPath}/help/HelpBasicConcepts.jsp">Key Concepts</a> used in PiMS is also available and a 
     <a href="${pageContext.request.contextPath}/help/HelpQuickReference.jsp">Quick Reference Guide</a>, 
     
  and for the laboratory PiMS administrator a <a href="${pageContext.request.contextPath}/help/Setup.jsp">Set Up Guide</a>.</p>       
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
     <ul> 
        <li><a href="http://pims.structuralbiology.eu/videos/tour.wmv">This Help as a Video</a></li>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#login">Log in to PiMS</a></li>
        <li><a href="#homepage">PiMS Homepage and Navigation</a>
            <ul>
                <li><a href="#menus">Menubar and Menus</a></li>
                <li><a href="#functions">Functions pages</a></li>
                <li><a href="#breadcrumbs">Breadcrumb trails</a></li>
                <li><a href="#diagram">Diagrams</a></li>
            </ul></li>
     </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#recordTarget">Recording a Target</a></li>
        <li><a href="#recordConstruct">Recording a Construct</a></li>
         <li><a href="#recordProtocol">Protocols and Experiments</a>
            <ul>
               <li><a href="#protocolSamples">Samples</a></li>
               <li><a href="#parameters">Parameters</a></li>
             </ul>
         </li>
         <li><a href="#recordExperiment">Recording Experiments</a>
             <ul>
                <li><a href="#workflow">Constructing a Workflow</a></li>
             </ul></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="colsed" title="Detailed Help">
    <div class="leftcolumn">  
      <ul>
        <li><a href="${pageContext.request.contextPath}/help/helpLogInAndNav.jsp">PiMS Login and Navigation</a></li>
        <li><a href="${pageContext.request.contextPath}/help/target/HelpNewTarget.jsp">New Target Help</a></li>
        <li><a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">New Construct Help</a></li>
      </ul>
    </div>
    <div class="rightcolumn">    
      <ul>
        <li><a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp#createprotocol">New Protocol Help</a></li>
        <li><a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#recordSimpleExp">New Experiment Help</a></li>
        <li><a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#recordPlateExp">New Plate Experiment Help</a>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="prerequisites">Prerequisites</h3>
  Before you can start using PiMS to record your laboratory information you will need a ask your <strong>PiMS Administrator</strong>
  for a <strong>username</strong> and <strong>password</strong>.<br />  You will also be allocated to a <strong>User group</strong> with
  permissions to record, edit and view information in one or more <strong>Lab notebooks</strong>.<br />
  The Lab notebook allows data from different researchers to be kept separate and stored privately. Most items recorded in PiMS (including Protocols, Experiments, Targets and Samples) 
  can be thought of as <strong>pages</strong> of a Lab notebook.<br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>

  <h3 id="login">Log in to PiMS</h3>
  Once you have a username and password you will be able to Log in to PiMS -click <span class="menutext">Log in</span> in the menubar on the PiMS Homepage.<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/loginMenubar.png" alt="Homepage with login link highlighted" />
  <br />
  Enter your username and password in the <strong>Log in box</strong> then click <input value="Log in" type="submit" /><br />
  <div class="indent" >
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/PIMSLoginScreen.png" alt="PIMS log in screen" />
  </div><br />
  You are now ready to start using PiMS. Your username will be displayed in the menubar next to the <strong>Log out</strong> link <br />
  e.g. <span class="menutext">Log out newuser</span>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="homepage">The Homepage and Navigating around PiMS</h3>
  The PiMS homepage contains a number of labelled boxes called <strong>Bricks</strong>. As you use PiMS, the content of some of these bricks will change.<br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/PIMSHomepage.png" alt="PIMS Homepage" />
  <ul>
    <li>The <strong>Calendar</strong> brick allows you to see the information recorded in PiMS on a particular day or week. 
    A calendar is displayed on the majority of the pages in PiMS.<br /><br /></li>
    <li>The <strong>Quick Search</strong> brick helps you to find PiMS records.<br />
    This brick also illustrates the use of <strong>icons</strong>
    to identify different types of information stored in PiMS:
      <ul>
        <li>Target records are identified with a 
        <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/target.gif" alt="Target icon" /> Target icon</li>
        <li>Samples use a  <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/sample.gif" alt="Sample icon" /> Sample icon</li>
        <li>Experiments use an 
        <img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> Experiment icon</li>
        <li>etc.</li>
      </ul>
    </li> 
    <li>The <strong>Create a New Target</strong> brick can be used to record the details of Targets.<br /><br /></li>
    <li>As you use PiMS, the <strong>History</strong> brick will become populated with links to your most recently viewed PiMS records.<br /><br /></li>
    <li>Similarly, the <strong>My Targets</strong> brick will contain links to your Targets when you record Experiments for them.</li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>  

  <!-- ................................................................................ -->
  <h4 id="menus">Menubar and Menus</h4>  
  Take some time to mouse-over the <strong>Menubar</strong> to see the <strong>drop-down</strong> menus.<br />
  These contain navigation links to some of the most commonly used functions of PiMS.<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/loggedIn.png" alt="Home page after new user is logging in" /><br />
  If you click a link in a menu, you will navigate away from the Homepage.<br />
  To return just click <span class="menutext">Home</span> at the left-hand end of the menubar which is displayed on all pages in PiMS.<br /><br />    
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="functions">Functions pages</h4>
  The last item in most of the Menus is a link called "More..." to a <strong>Functions</strong> page.<br />
  This can also be reached by clicking the relevant name in the Menubar<br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/targetFunctions.png" alt="Target functions page" /><br />
  Functions pages contain the links which are listed in the appropriate menu, along with additional links to Create, Search and View related records.<br />
    e.g. the Target functions page includes links to Molecules, 
    Species (Natural source), Database references, relevant Reference data and Help.  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="breadcrumbs">Breadcrumb trails</h4>
  Most pages in PiMS have a <strong>Breadcrumb trail</strong> just below the Menubar<br />
  For example, there is a Breadcrumb trail on PiMS Target and Construct pages to help you to navigate between Target and Construct details pages.
  The example shows the Breadcrumb trail on a Construct details page<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/construct.png" alt="Construct details Breadcrumb trail" />
  <br />
   <ul>
    <li>To see the corresponding Target details page, click the 
    <pimsWidget:linkWithIcon 
                icon="types/small/target.gif" 
                url="JavaScript:void(0)"
                text="000994" />link.<br /><br /></li>
    <li>To navigate to a list of all Targets in PiMS, click <a href="javascript:void(0)"><strong>Targets</strong></a></li>
   </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="diagram">Diagrams</h4>
  PiMS <strong>Diagrams</strong> are interactive <strong>Graphical</strong> views of the relationships between certain records and can be 
  used as a form of navigation. Each major PiMS entity is represented by a different shape and arrows show how entities relate to each other.<br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/diagramKey.png" alt="Key to the Shapes in diagrams" />
  <br />
    <br /> PiMS Diagrams can be accessed by clicking the  
            <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" /> link on a Construct, Experiment, Sample or Target page.<br />
   Diagrams are interactive: clicking a shape goes to the page that describes it in detail.<br /><br />
   The Diagrams for Experiments and Samples are also referred to as <a href="#workflow">Workflow</a> diagrams and 
   illustrate how Samples feed into Experiments which in turn produce more Samples.
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="recordTarget">Recording a Target</h3>
  The typical starting point for work with PiMS is to create a record of a Target. 
  A PiMS Target is usually (but not always) a full length protein translated from a full length open reading frame.<br /><br />
  The simplest way to record a New target is to download the details from a remote sequence database such as Uniprot, GenBank etc.<br />
  <ul>
    <li>Locate the <strong>Create a New Target</strong> brick on the Homepage<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/createTargetBrick.png" alt="Create Target Brick" /></li>
    <li>Enter an <strong>accession number</strong> for the Target in the <strong>Database ID</strong> field, select a <strong>Database</strong> from the drop-down list<br />
    then click <input type="submit" value="Get record" /><br />
    &nbsp; PiMS will attempt to retrieve the Protein sequence record, using the values you entered.<br />
    &nbsp; If possible, PiMS will also retrieve a corresponding DNA sequence record.<br /><br /></li>
    <li>You can then review the retrieved record(s) and, if appropriate, use them to create a new PiMS Target.<br /><br /></li>
    <li>If it is not possible for you to create a Target in this way, PiMS offers alternative methods, for example:<br />
    &nbsp; -when the Target details are not available in one of the databases supported by PiMS, you can enter the details in a form<br />
    &nbsp; -when the Target does not encode a protein, you can record a <strong>DNA Target</strong><br />
    &nbsp; -when neither the protein nor the DNA sequence is available, you can record a <strong>Natural Source Target</strong>
    <br /><br /></li>
    <li><strong>see</strong> <a href="${pageContext.request['contextPath']}/help/target/HelpNewTarget.jsp">New Target Help</a> for more details</li>
  </ul>  
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ================================================================================ -->
  <h3 id="recordConstruct">Recording a Construct</h3>  
    After you have recorded a Target in PiMS, it is possible to design <strong>Constructs</strong> to represent the 
    physical samples you intend to work on.<br />
    PiMS provides a <strong>Construct design</strong> tool which records the details of all the components required
    for the first experimental step (usually a PCR Experiment): the region of the Target to be used, the PCR Primers and 
    details of the expected protein product(s).<br /><br />
    <strong>note:</strong> the details (including DNA and Protein sequences) recorded for a Construct can be edited.  
    <br /><br />
    To design a new Construct for a Target:
    <ul>
       <li>Navigate to the view of a Target (if you have just recorded a new Target in PiMS, this will be what you will be looking at) e.g.<br />
        <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/sequences.png" alt="New Construct links on Target page" /><br />
       &nbsp; <strong>note:</strong> this is a typical view of a PiMS record with a large icon to identify the type -A Target record in this case, 
       an open box displaying the basic <strong>Details</strong> and a series of closed boxes for additional information.<br /><br /></li> 
       <li>Click the 
        <a href="JavaScript:void(0)">New construct</a> link near the top of the page, or the 
        <a href="JavaScript:void(0)">Design new construct</a> in the header of the
        box labelled <strong>Constructs</strong> and follow the 3 step process of Construct design in PiMS.
        <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/newConstructBasicDetails.png" alt="New Construct Step 1" /></li>
       <li><strong>Step 1: Basic details</strong> in the box labelled <strong>Basic Details</strong> enter values for the 3 required fields indicated by a <span class="required">*</span><br />
        &nbsp; the <strong>Construct id<span class="required">*</span></strong> which must be a unique value for this Target's Constructs<br />
        &nbsp; the <strong>Target protein start<span class="required">*</span></strong> and the <strong>Target protein end<span class="required">*</span></strong> 
        -to define the region of the target protein you wish to express<br />
        &nbsp; <em>by default, these are set to the first and last residues in the translated sequence</em><br /><br /></li>
       <li>Select a Lab Notebook for the Construct<br /><br /></li>
       <li>Click <input type="submit" value="Design primers &gt;" /> and PiMS will attempt to design suitable PCR Primers for the Construct and proceed to the next step.<br />
       &nbsp; alternatively, change the Primer design Tm or click <input type="submit" value="Save Construct" /> record a Construct without designing PCR Primers<br /><br /></li>
       <li><strong>Step 2: Select Primers</strong><br /> 
        <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/newConstTmPrimers.png" alt="New Construct links on Target page" /><br />
       Select one Forward primer and one Reverse primer by clicking the appropriate radio button then click <input type="submit" value="Next &gt;&gt;&gt;" /><br />
           <em>-Primers with a Tm closest to the 'Default Tm' are selected automatically</em><br />
           alternatively, design new PCR Primers with a different Tm or edit the sequence or length of the default Primer(s)<br /><br /></li>
       <li><strong>Step 3: Start &amp; Stop codons, Primer Extensions and Protein tags</strong><br />
       <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/newConstExtAndTags.png" alt="Step 3 Construct design" /><br />
        At this step you can:<br />
        &nbsp; add a Start or Stop codon the 5'-end of your selected Primers<br />
        &nbsp; select a 5'-Extension for you primers from the drop-down lists -or design a new one<br />
        &nbsp; enter sequences to represent any protein tags which will be added to your expressed protein(s)<br /><br /></li>
       <li>Click <input class="button" value="Save" type="submit" /> to record the new Construct and view the details<br />
       <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/construct.png" alt="New Construct details" /><br /><br /></li>
       <li><strong>see</strong> <a href="${pageContext.request['contextPath']}/help/target/HelpNewConstruct.jsp">New Construct Help</a> for more details</li>
   </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="recordProtocol">Protocols and Experiments</h3>
    A <strong>Protocol</strong> is a user-definable, reusable <strong>Experiment template</strong> which provides much of the flexibility of PiMS.<br />
    A Protocol allows you to define exactly what information you record for your Experiments and how you link them together to create a workflow
    <em> -there are no pre-defined workflows in PiMS</em><br /><br />
    
    You can use a Protocol to define the <strong>Type</strong> of <strong>Samples</strong> which are suitable as 
    <strong>Inputs</strong> and <strong>Outputs</strong> for your Experiments. This <strong>Typing</strong> is key to linking Experiments 
    to create a <strong>Workflow</strong> since the Output sample type of one experiment must match the Input sample type of the next experiment.<br /><br />
    A Protocol is also used to define <strong>Parameters</strong> -details and results you would like to (or need to) record.<br />
    You can specify default values or a list of possible values for Parameters.<br /><br />
    
    Protocols are grouped together according to the <strong>Experiment Type</strong> they describe.<br />  
    When you install PiMS it comes with a set of 26 default Protocols which can be used for most protein production 
    work. This includes Protocols of different <strong>Types</strong> describing Construct design, PCR, Cloning, Expression, Purification and Characterisation Experiments.<br />
    Alternatively you can copy and edit the default Protocols, record entirely new Protocols or import Protocols from another PiMS installation.<br />

  <!-- ................................................................................ -->
    <h4>Recording a Protocol</h4>
    <ul>
    <li>Select &quot;New Protocol&quot; from the Experiment menu to see a form with 3 options<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/protocol/createProtocol.png" alt="New protocol options" />
    </li>
    <li>Using the first Option <strong>Specify basic details, then edit the protocol:</strong><br />
    &nbsp; enter a <strong>Name<span class="required">*</span></strong> for the protocol <em>-this must not be the same as the name of any existing Protocol in PiMS</em><br />
    &nbsp; select an <strong>Experiment type</strong> and a <strong>Lab Notebook</strong> from the drop-down lists then click <input type="submit" value="Next&gt;&gt;&gt;" /><br /><br /></li>
    <li>You will see the standard PiMS Protocol view with a series of boxes for recording the details<br />
    <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/protocol/protocolAfterCreate.png" alt="New protocol view" />
    </li>
    <li>The <strong>Basic details</strong> of the protocol are in the first box; this is open when you first arrive at the page.<br />
        The <strong>Method</strong> is where you would record a &quot;Lab method&quot; -e.g. a text description of the way you carry out experiments 
        using this protocol. You can paste this information in from an existing document.<br />
        The other boxes labelled <strong>Inputs</strong>, <strong>Parameters</strong> and <strong>Output samples</strong>
        are for you to define exactly what information you record for your Experiments.<br /><br /></li>
    <li>Alternatives for recording a Protocol are:
        <ul>
            <li><strong>Upload a PiMS Protocol file:</strong> to Import a Protocol from another PiMS installation</li>
            <li><strong>Copy an existing PiMS protocol and modify it:</strong> -also possible from the View of a protocol</li>
        </ul></li>
    </ul>
  <div class="toplink"><a href="#">Back to top</a></div>
    
    <h4 id="protocolSamples">Defining Samples for the new Protocol</h4>
    <ul>
       <li>Open the box labelled <strong>Inputs</strong> and click <a href="JavaScript:void(0)">Add new...</a><br />
       <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/inputSampleForm.png" alt="Form to create an input sample" /></li>
       <li>In the resulting pop-up window enter a <strong>Name<span class="required">*</span></strong> for the Input Sample<br /> 
       <em>-this must be unique within the Protocol. The bottom part of the window lists all of the used names to help you</em><br />
       Select a <strong>Category</strong> to define the <strong>Type</strong> of Sample<br />
       You can also define the &quot;default&quot; amount of the Sample to record in your Experiments and an appropriate unit<br /><br /></li>
       <li>Repeat this to define all of the <strong>Inputs</strong><br />
       The process is the same for defining <strong>Output Samples</strong></li>
    </ul>
    <div class="toplink"><a href="#">Back to top</a></div>
    <h4 id="parameters">Defining Parameters for the new Protocol</h4>
    You can define 3 types of Parameters in your Protocol:<br />
    &nbsp;<strong>Set up Parameters</strong> e.g. incubation details<br />
    &nbsp;<strong>Result Parameters</strong> e.g. a yield from a purification<br />
    &nbsp;<strong>Group level Parameters</strong> are used to define an experimental detail for a group of experiments performed simultaneously<br />
    &nbsp; e.g. the thermocycling conditions for a PCR performed in a plate<br /><br />
    For each of these Parameter types you can also define the type of data the Parameter will record:<br />
    &nbsp;<strong>Number</strong>, <strong>Text</strong> or <strong>Yes/No</strong> 
    <ul>
       <li>Open the relevant box and click to Add a new <a href="JavaScript:void(0)">Number</a>, <a href="JavaScript:void(0)">Text</a> or <a href="JavaScript:void(0)">Yes/No</a> Parameter<br />
       You will see a pop-up window where you can enter the details for your new parameter<br />
       <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/protocol/parameterForm.png" alt="Form to create a Parameter" /><br />
       (This example is for a Number parameter. The form varies slightly for the others.)<br /><br /></li>
       <li>As for Samples, you need to enter a  <strong>Name<span class="required">*</span></strong> for the Parameter which must be unique for the Protocol.<br />
       Additionally, you can enter a short <strong>Description</strong> of what the Parameter represents, define if a value <strong>must</strong> be recorded for the Parameter
       (Mandatory parameter) and enter a <strong>Default value</strong> which will be recorded for each experiment unless you <strong>Allow free entry of values</strong>.
       You can also <strong>Restrict</strong> the entry to a list of specific values<br /><br /></li>
       <li>For a number parameter, you can also specify:
          <ul>
            <li>a unit to display</li>
            <li>whether the number is restricted to integers (whole numbers)</li>
            <li>a minimum value</li>
            <li>a maximum value</li>
          </ul><br /></li>
        <li>Continue adding Parameters until you are happy with your Protocol</li>
    </ul>  
     <strong>see</strong> <a href="${pageContext.request['contextPath']}/help/experiment/protocol/HelpProtocol.jsp#createprotocol">New Protocol Help</a> for more details
    
    <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
    <h3 id="recordExperiment">Recording Experiments</h3>
    Once you have suitable Protocols you can begin recording Experiments in PiMS since the <strong>Protocol</strong> 
    is a <strong>Template</strong> for Experiments.<br />
    By taking care to match the Sample <strong>Category</strong>
    for Output and Input Samples, you can link sequential Experiments together to form a <strong>Workflow</strong>.<br /><br />
    
    A typical <strong>first Experiment</strong> to record in PiMS is a <strong>PCR Experiment</strong> using the <strong>PCR Primers</strong> you have recorded for a <strong>Construct</strong>.<br />
    <strong>note: </strong> this is not compulsory as you can record any type of Experiment at any time in PiMS.
    <ul>
        <li>Navigate to the view of a <strong>Construct</strong> and click the <a href="JavaScript:void(0)">New Experiment</a>
        link in the <strong>Experiments</strong> box.<br />
        Alternatively, select <strong>New Single Experiment</strong> from the Experiment menu<br /><br /></li>
        <li>You will see the New Experiment screen.<br /><br />
        If you have recently recorded or used any Protocols you will see 2 open boxes labelled <strong>Recently used protocols</strong> and <strong>Other protocols</strong>:<br />
        The Protocols will be listed in the top box and can be used to record a new Experiment by clicking 
        <input type="submit" value="Choose" /><br /><br /></li>
  
        <li>Alternatively, the first step is to select an <strong>Experiment type</strong> from the drop-down list in the bottom box and 
        click <input type="submit" value="Next &gt;&gt;&gt;" /><br />  <em> -this may be the only box you see</em><br /><br /></li>
        <li>Select a Protocol from the new drop-down list which appears:<br />
        <br /></li>
        <li>You will then see 2 additional fields to record the <strong>Experiment name</strong> and to select a <strong>Lab Notebook</strong><br />
        &nbsp; <em><strong>note:</strong> PiMS automatically generates a Name but you may edit this</em><br /><br /></li>
        <li>When you are satisfied, click the
        <input class="button" value="Next &gt;&gt;&gt;" type="submit" /> button.<br />
        A new experiment record will be made displayed with all of the information defined in the Protocol recorded in a series of labelled boxes: <br />
        <strong>Basic Details</strong>, <strong>Conditions and Results</strong> -for Parameters and <strong>Samples</strong> for the Input and Output Samples.<br />
        There are 4 additional boxes labelled <strong>Images</strong>, <strong>Attachments</strong>, <strong>Notes</strong> and <strong>External Database Links</strong>
        for adding extra information such as images and data files.<br />
        <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/parameters.png" alt="Create Experiment -view details" /><br /></li>
        <li>If you started from <strong>Construct</strong> view and recorded a new <strong>PCR</strong> Experiment, the 
        <strong>Basic Details</strong> box will look slightly different -it will include a link to the <strong>Construct</strong> and 
        the predicted <strong>PCR Product size</strong> will be displayed.
        Also, if your <strong>PCR Protocol</strong> defines <strong>Input Samples</strong> for <strong>PCR Primers</strong>, PiMS will automatically select the correct Primer Samples for the Construct.<br /><br /></li>
        <li>An alternative way to record a New experiment is to click
           <pimsWidget:linkWithIcon 
                icon="actions/create/experiment.gif" 
                url="JavaScript:void(0)" 
                text="New single experiment" /> from the Protocol view.
           </li>
    </ul>            
      
    <strong>see</strong> <a href="${pageContext.request['contextPath']}/help/experiment/NewExperiment.jsp">New Experiment Help</a> and 
    for more details.<br /><br />
    You can also record several Experiments simultaneously as a <strong>Group</strong> or as an entire <strong>Plate</strong> (e.g as 24, 48 or 96-well Plate).
    <br /><strong>see</strong> <a href="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#recordPlateExp">New Plate Experiment Help</a>
    for more details.
    <div class="toplink"><a href="#">Back to top</a></div>

  <h4 id="workflow">Constructing a Workflow</h4>
  In PiMS a <strong>Workflow</strong> is constructed by <strong>linking</strong> a series of Experiments together 
  where the <strong>Output</strong> Sample from Experiment 1 becomes the <strong>Input</strong> Sample for Experiment 2.<br />
  This is achieved by ensuring that the Output Sample <strong>Category</strong> or <strong>Type</strong> from Experiment 1 <strong>matches</strong> the Input Sample
  <strong>Type</strong> for Experiment 2. Sample <strong>Categories</strong> or <strong>Types</strong> are defined in the <strong>Protocol</strong>.<br /><br />
  To create a Workflow in PiMS:
  <ul>
    <li>Navigate to the view of an Experiment and open the box labelled <strong>Samples</strong>and click the link for an Output Sample.<br /><br /></li>
    <li>Locate the box labelled <strong>Use Sample as input in a New Experiment:</strong> and open it to show a list of possible &#39;Next&#39; experiments represented by:<br />
    &nbsp; the <strong>Role</strong> for the Sample, as an Input Sample for an Experiment defined by a <strong>Protocol</strong> (Experiment template).<br /><br />
    The example is part of the list of possible Next Experiments for a Sample. 
    This particular Sample could be used as an Input Sample called
    <strong>Template</strong> in an Experiment using the Protocol called <strong>PiMS Clone verification</strong><br /><br />
     <img class="imageNoFloat" id="nextExpImage" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/nextExperiment2.png" alt="Output sample -next experiment options" />
    <br /></li>
    <li>Click the <input type="submit" value="New Experiment" /> button for the relevant Protocol to record the Next Experiment<br /><br /></li>
    <li>You can build up a Workflow by repeating this process of selecting the <strong>Next Experiment</strong> 
    for the Output Sample from the subsequent Experiment.<br /><br /></li>
    <li>PiMS provides a <strong>Graphical</strong> view of a Workflow created in this way which can be viewed by clicking the 
            <pimsWidget:linkWithIcon 
                icon="actions/viewdiagram.gif" 
                url="JavaScript:void(0)"
                text="Diagram" />link on a Sample or Experiment view.
         Experiments (represented by Ellipses) are linked via Samples (Diamonds).<br />
    <img class="imageNoFloat" id="expDiagram" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/experimentDiagram.png" alt="Experiment diagram example" /><br />
   </li>
 </ul>

 <strong>see</strong> <a href="${pageContext.request['contextPath']}/help/experiment/HelpExperiments.jsp#nextExp">Next Experiment Help</a>
     for more details.
  <div class="toplink"><a href="#">Back to top</a></div>
   
<%--
  <h3 id=""></h3>
  <div class="toplink"><a href="#">Back to top</a></div>
--%>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

