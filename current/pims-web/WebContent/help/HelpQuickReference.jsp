<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Quick Reference' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Quick Reference Guide"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<c:set var="extraheader">
    <a href="#">Back to top</a>
</c:set>
<c:set var="iconurl">
  <img src="${pageContext.request.contextPath}/skins/default/images/icons/" />
</c:set>

<div class="help">
  <pimsWidget:box initialState="fixed" title="Index">
    <div class="help">
       <a href="#a" title="">A</a>&nbsp;&nbsp;<a href="#b" title="Bricks">B</a>&nbsp;&nbsp;<a href="#c" title="Complexes, Constructs">C</a>
       &nbsp;&nbsp;<a href="#d" title="Diagrams">D</a>&nbsp;&nbsp;<a href="#e" title="Experiments">E</a>&nbsp;&nbsp;<a href="#f" title="Finding things">F</a>
       &nbsp;&nbsp;<a href="#g" title="">G</a>&nbsp;&nbsp;<a href="#h" title="">H</a>&nbsp;&nbsp;<a href="#i" title="Icons">I</a>
       &nbsp;&nbsp;<a href="#j" title="">J</a>&nbsp;&nbsp;<a href="#k" title="">K</a>&nbsp;&nbsp;<a href="#l" title="">L</a>&nbsp;&nbsp;
       <a href="#m" title="">M</a>&nbsp;&nbsp;<a href="#n" title="Navigation">N</a>&nbsp;&nbsp;<a href="#o" title="">O</a>&nbsp;&nbsp;
       <a href="#p" title="Parameters, Plate Experiments, Protocols">P</a>&nbsp;&nbsp;<a href="#q" title="">Q</a>&nbsp;&nbsp;<a href="#r" title="">R</a>
       &nbsp;&nbsp;<a href="#s" title="Samples, Searches">S</a>&nbsp;&nbsp;<a href="#t" title="Targets">T</a>&nbsp;&nbsp;<a href="#u" title="">U</a>
       &nbsp;&nbsp;<a href="#v" title="">V</a>&nbsp;&nbsp;<a href="#w" title="">W</a>&nbsp;&nbsp;<a href="#x" title="">X</a>&nbsp;&nbsp;
       <a href="#y" title="">Y</a>&nbsp;&nbsp;<a href="#z" title="">Z</a>
    </div>
  </pimsWidget:box>
  <%-- Template for extra boxes
  <pimsWidget:box initialState="fixed" title="" extraHeader="${extraheader}" >
    <dl>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  OR WITH AN ICON:
  <pimsWidget:box initialState="fixed" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/xxx.gif' 
    alt='xxx icon' /> XXX" extraHeader="${extraheader}" >
    <dl>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  --%>
  <pimsWidget:box initialState="fixed" id="b" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/blank.gif' 
    alt='Blank icon' /> Bricks" extraHeader="${extraheader}" >
    <dl>
     <dt>Homepage Bricks:</dt>
      <dd> Boxes on the homepage with links to commonly used PiMS functions</dd>
      <dd> Contents e.g. history brick, are updated as you use PiMS</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="c" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/complex.gif' 
    alt='Complex icon' /> Complexes" extraHeader="${extraheader}" >
    <dl>
     <dt>Recording a Complex:</dt>
      <dd> Target menu -> New Complex</dd>
      <dd> Enter Complex name<span class="required">*</span> and Why Chosen<span class="required">*</span></dd>
      <dd> Select Targets to include as components</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/construct.gif' 
    alt='Construct icon' /> Constructs" extraHeader="${extraheader}" >
    <dl>
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/construct.gif" alt="New Construct icon"/> Recording a Construct:</dt>
      <dd>
       <ul>
        <li>Access from Target view</li>
        <li>Target must have a translatable DNA sequence</li>
        <li>Need a unique ID<span class="required">*</span> <em>-keep this short</em></li>
        <li>Need a Start<span class="required">*</span> and End<span class="required">*</span> <em>-defines region of Target sequence for Construct e.g. a domain etc.</em></li>
        <li>Primer design selects primer pair closest to design Tm <em>-can be changed</em></li>
        <li>Options to add start and stop codons, 5'-Extensions and corresponding N- or C-term protein tags</li>
       </ul>
      </dd> 
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/construct.gif" alt="New Construct icon"/> Recording a Construct -primerless:</dt>
      <dd>
       <ul>
        <li>Access from Target view</li>
        <li>Target must have a translatable DNA sequence</li>
        <li>Need a unique ID<span class="required">*</span> <em>-keep this short</em></li>
        <li>Need a Start<span class="required">*</span> and End<span class="required">*</span> <em>-defines region of Target sequence for Construct e.g. a domain etc.</em></li>
        <li>Click <input type="Submit" value="Save Construct" /></li>
       </ul>
      </dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="d" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif' 
    alt='Diagram icon' /> Diagrams" extraHeader="${extraheader}" >
    <dl>
     <dt>Workflow:</dt>
      <dd> A diagram showing linked Experiments and Samples: Input Sample->Experiment->Output Sample</dd>
      <dd> Click the 
       <img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif' 
        alt='Diagram icon' /> <a href="javascript:void(0)"><strong>Diagram</strong></a> link on a Sample or Experiment page</dd>
     <dt>Target:</dt>
      <dd> A diagram showing the relationship between a Target and its Constructs and any Milestone Experiments</dd>
      <dd> Click the 
       <img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/viewdiagram.gif' 
        alt='Diagram icon' /> <a href="javascript:void(0)"><strong>Diagram</strong></a> link on a  Target or Construct page</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="e" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif' 
    alt='Experiment icon' /> Experiments" extraHeader="${extraheader}" >
    <dl>
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/experiment.gif" alt="New Experimant icon"/> Recording an Experiment:</dt>
      <dd>Experiment menu -> New Single Experiment</dd>
      <dd>Select Experiment Type -> Select Protocol -> Edit Name<span class="required">*</span>  -> Select Lab Notebook -> Click <input type="Submit" value="Next&nbsp;&gt;&gt;&gt;" /></dd>
      <hr />
     <dt>Linking Experiments:</dt>
      <dd>Experiments are linked by Samples e.g. <em>Experiment 1 -> Output Sample 1 -> Experiment 2</em></dd>
      <dd>Need matching Sample Categories i.e. <em>
      The Output <strong>Sample Category</strong> from Experiment 1 must be the same as the Input Sample Category for Experiment 2</em></dd>
      <hr />
     <dt>Types of Experiment:</dt>
      <dd><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif' 
        alt='Experiment icon' /> Single Experiment: </dd>
      <dd><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif' 
        alt='Plate icon' /> Plate Experiment:  experiments performed in 24-, 28- and 96-well plates</dd>
      <dd><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/experimentgroup.gif' 
        alt='Experiment group icon' /> Experiment group:  a group of experiments processed simultaneously but NOT performed in a plate</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="f" title="Finding things" extraHeader="${extraheader}" >
    <dl>
     <dt>History brick:</dt>
      <dd>and /or History menu lists links of recently Viewed PiMS pages</dd>
     <dt>Quick Search brick:</dt>
      <dd>Select a &quot;Type&quot; <em>e.g. Target, Sample etc.</em> and enter a search item</dd>
      <dd> <em> also accessible by clicking <strong>Search</strong> in the menubar</em></dd>
     <dt>Custom search:</dt>
      <dd>Available from Menus or Functions pages -can restrict search to fields and sort results</dd>
     <dt>Browser tools:</dt>
      <dd>e.g. use &quot;Ctrl F&quot; and enter a search term</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="i" title="Icons in PiMS" extraHeader="${extraheader}" >
    <dl>
     <dt>Icons to identify different records in PiMS:</dt>
      <dd>
        <ul>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/person.gif" alt="Person icon" /> <a href="javascript:void(0)">Demo PiMS</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Person record</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif" alt="Target icon" /> <a href="javascript:void(0)">000849</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Target details record</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/construct.gif" alt="Construct icon" /> <a href="javascript:void(0)">Ttx 1576.N</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to Construct details page</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif" alt="Sample icon" /> <a href="javascript:void(0)">000001R1</a><img src="images/icons/pulldown.gif"/> -a link to a Sample record
     </li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/recipe.gif" alt="Recipe icon" /> <a href="javascript:void(0)">10mM Tris Buffer</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Recipe record
     </li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/holder.gif" alt="Holder icon" /> <a href="javascript:void(0)">Rack 1</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Holder record</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> <a href="javascript:void(0)">Purification200110</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Single experiment record</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/experimentgroup.gif" alt="Experiment group icon" /> <a href="javascript:void(0)">Expression group</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to an Experiment group record <em>NOT a Plate experiment</em></li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif" alt="Plate icon" /> <a href="javascript:void(0)">PCR Plate 1</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Plate experiment record</li>
     <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/protocol.gif" alt="Protocol icon" /> <a href="javascript:void(0)">PIMS PCR</a><img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a PiMS Protocol (Experiment template)</li>
    </ul>
      </dd>
     <dt>Icons in the Context menu <img src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif"/>:</dt>
      <dd>
       <ul>
        <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/view.gif" alt="View icon" />View &nbsp;&nbsp; -a link to the Details page for a record</li>
    <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt="Delete icon" />Delete &nbsp;&nbsp; -to delete the record if you have permission to do so</li>
    <!--TODO<li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/viewdiagram.gif" alt="Diagram icon" />View diagram &nbsp;&nbsp; -to see a diagramatic representation of the Record </li>
    <li><img src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/createConstruct.gif" alt="Create construct icon" />New construct...  -a link to the form for recording a New Construct for the Target </li>
    -->
       </ul>
      </dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="n" title="Navigation" extraHeader="${extraheader}" >
    <dl>
     <dt>Menubar:</dt>
      <dd>Horizontal bar at the top of all PiMS pages with &quot;Home&quot;, &quot;Login&quot; &quot;Help&quot; and links to Functions pages</dd>
     <dt>Menus:</dt>
      <dd>Mouse over menubar to see drop-down menus of links to &quot;New&quot;, &quot;Search&quot; and some reports</dd>
     <dt>Functions pages:</dt>
      <dd>Click &quot;More...&quot; at bottom of menu, contains links to additional functions and Reference data</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="p" title="Parameters" extraHeader="${extraheader}" >
    <dl>
     <dt>Types of Parameter:</dt>
     <dd><img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/numbericon.gif" alt="Number icon"/> Number: where the value is numerical e.g. an amount, temperature etc.<br />
     <ul>
      <li>Enter Name <span class="required">*</span>, restrict to whole numbers, add units, max and min values</li>
      <li>Select radio button to make required value</li>
     </ul></dd>
     <dd><img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/stringicon.gif" alt="Text icon"/> Text: for recording a text value e.g. a description
     <ul>
      <li>Enter Name <span class="required">*</span></li>
      <li>Select radio button to make value required</li>
     </ul></dd>
     <dd><img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/booleanicon.gif" alt="Yes/No icon"/>Yes/No: for recording a &quot;Yes or No&quot; or &quot;True or False&quot; value e.g. Has reagent B been added?      
     <ul>
      <li>Enter Name <span class="required">*</span></li>
      <li>Select radio button for default value: Yes or No</li>
      <li>Select radio button to make value required</li>
     </ul></dd>
     <dt>Default value:</dt>
     <dd>a value which will be recorded by default -can be changed</dd>
     <dd>use with Text and Number parameters, radio button for Yes/No Parameters</dd>
     <dt>Possible values:</dt>
     <dd>list of possible values to select from, must be separated by &quot;;&quot; in the Protocol</dd>
      <dd>use with Text and Number parameters</dd>
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/scores/score3.png" alt="Yes icon"/> 
     <img src="${pageContext.request.contextPath}/skins/default/images/icons/scores/score1.png" alt="No icon"/> 
     <img src="${pageContext.request.contextPath}/skins/default/images/icons/scores/score2.png" alt="Maybe icon"/>Score Parameters: used in Plate experiments</dt>
     <dd>
     <ul>
      <li>Enter Name <span class="required">*</span> beginning with two underscore characters e.g. __SCORE</li>
      <li>Default value of &quot;Unscored&quot;</li>
      <li>Possible values: e.g. Unscored;No;Maybe;Yes;</li>
     </ul>
     </dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif' 
    alt='Plate icon' /> Plate Experiments" extraHeader="${extraheader}" >
    <dl>
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/plate.gif" alt="New Plate experiment icon"/> Recording a Plate Experiment:</dt> <dd> Experiment menu -> Select New Plate Experiment</dd>
     <dd>Select Experiment Type<span class="required">*</span> -> Select Protocol<span class="required">*</span> -> Enter Group name<span class="required">*</span></dd>
     <dd>&nbsp;&nbsp;Select Lab Notebook<span class="required">*</span>, 
     Edit Start<span class="required">*</span> and End<span class="required">*</span> dates, Select Plate type<span class="required">*</span>, Click <input type="Submit" value="Create" /></dd>
     <hr />
     <dt>Recording Multiple plates:</dt>
     <dd><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/plate.gif" alt="New Plate experiment icon"/> Record a Plate Experiment -> Enter up to 4 Plate IDs, Click <input type="Submit" value="Create" /></dd>
     <hr />
     <dt>Recording Plate Experiments using a Spreadsheet:</dt>
     <dd>
      <ul>
       <li>Follow steps to <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/plate.gif" alt="New Plate experiment icon"/> Record a Plate Experiment</li>
       <li>For a suitably formatted spreadsheet click <input type="Submit" value="Get Spreadsheet" /> and edit as required</li>
       <li>Browse to spreadsheet and Click <input type="Submit" value="Create" /></li>
      </ul>
     </dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/protocol.gif' 
    alt='Protocol icon' /> Protocols" extraHeader="${extraheader}" >
    <dl>
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/protocol.gif" alt="New Protocol icon"/>
        Recording a Protocol: from scratch:</dt> <dd> Experiment menu -> New Protocol -> First option:
     <ul>
      <li>Enter Name <span class="required">*</span>, 
      Select Experiment type and Lab Notebook, 
      Click <input type="Submit" value="Next&nbsp;&gt;&gt;&gt;" /></li>
      <li>Record details for Inputs, Outputs and Parameters</li>
     </ul></dd>
     <hr />
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/protocol.gif" alt="New Protocol icon"/> Recording a Protocol: upload a file:</dt> <dd> Experiment menu -> New Protocol -> Second option:
     <ul>
      <li>Browse to exported Protocol file, Select Lab Notebook and click   
      <input type="Submit" value="Upload file" /><br />
      <strong>note:</strong> <em>may need to edit file to ensure protocol name is unique in PiMS</em></li>
     </ul></dd>
     <hr />
     <dt><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/create/protocol.gif" alt="New Protocol icon"/> Recording a Protocol: copy existing Protocol:</dt> <dd> Experiment menu -> New Protocol -> Third option:
     <ul>
      <li>Enter search term and  
      Click <input type="Submit" value="Search" /></li>
      <li>Click link to View Protocol, Click <input type="Submit" value="Copy protocol" /></li>
     </ul></dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="s" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif' 
    alt='sample icon' /> Samples" extraHeader="${extraheader}" >
    <dl>
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif' 
    alt='sample icon' /> Input Samples:</dt> <dd> The &quot;Inputs&quot; in a Protocol which describe the components of an Experiment.  Can be the Output Sample from a previous Experiment</dd>
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/sample.gif' 
    alt='sample icon' /> Output Samples:</dt> <dd> The &quot;Outputs&quot; in a Protocol which describe the product(s) of an Experiment.  Can be the Input Sample for the next  Experiment</dd>
     <dt> Sample category:</dt> <dd> A grouping system for Samples used to determine which Samples are suitable as Inputs for Experiments. Samples to be used as Input Samples need to have a Sample category which matches that defined in the relevant protocol.  A Sample may have more than one Sample category.</dd>
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/recipe.gif' 
    alt='recipe icon' /> Recipes:</dt> <dd> Templates for Samples a.k.a. a Reference Samples.  Used to describe the components and their concentrations in a Sample. Can also include a list of suppliers and catalogue references.</dd>
     <dt> Sample component:</dt> <dd> Recipe view -> Add new component -> select component to add -> click <input type="submit" value="Next &gt;&gt;&gt;" />
     </dd>
     <dd>-> Edit to record concentration</dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Searches" extraHeader="${extraheader}" >
    <dl>
     <dt> General:</dt>
     <dd> Searches are case sensitive</dd>
     <dd>Results are paged with configurable page size -default 20 results per page</dd>
     <dd>Results can be exported in 4 formats: Excel, CSV (comma sepatared values), and XML.</dd>
     <hr />
     <dt> Quick Search:</dt> <dd> Home page brick -> Select type -> Enter search terms -> Click <input type="submit" value="Search" /></dd>
     <dd> Click &quot;Search&quot; in menubar -> Select type -> Enter search terms -> Click <input type="submit" value="Search" /></dd>
     <hr />
     <dt> Custom Search:</dt> <dd>Select from Menus or Functions pages -> 
     Enter search terms and/or select options -> Click <input type="submit" value="Search" /></dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" id="t" title="<img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif' 
    alt='Target icon' /> Targets" extraHeader="${extraheader}" >
    <dl>
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/types/small/target.gif' 
     alt="Target icon"/> Types of Target:</dt>
      <dd>Typically a single ORF</dd>
      <dd>Or Natural source Target: protein purified from a tissue, sequence may not be known, not cloned</dd>
      <dd>Or DNA Target e.g. a promoter region, not protein-encoding</dd>
      <hr />
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/create/target.gif' 
     alt="New Target icon"/> Download a Target:</dt>
      <dd> Homepage -> Create a new Target box -> Enter Database ID (Accession no.) + Select Database</dd>
      <dd>Target menu -> New Target -> First option -> Enter Database ID (Accession no.) + Select Database</dd>
      <hr />
     <dt><img src='${pageContext.request.contextPath}/skins/default/images/icons/actions/create/target.gif'
      alt="New Target icon"/> Use a form (manual method):</dt>
      <dd>Target menu -> New Target -> Third option -> Select ORF, DNA or Natural Source -> Enter Name(s)<span class="required">*</span></dd>
    </dl>
    <div class="shim"></div>
  </pimsWidget:box>
</div>


<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

