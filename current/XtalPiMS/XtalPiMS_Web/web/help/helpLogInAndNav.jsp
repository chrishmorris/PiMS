<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help on PIMS Log in and Navigation' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<%-- helpLogInAndNav.html --%>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Logging in and Navigating around PiMS"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
<pimsWidget:box initialState="fixed" title="Contents">
 <div class="leftcolumn">
 <ul>
  <li><a href="#homePage">PIMS Homepage</a></li>
  <li><a href="#logIn">Logging in</a> to PIMS</li>
  <li><a href="#navigation">Navigating</a> around PIMS</li>
  <li>
   <ul>
    <li><a href="#menuBar">PiMS menubar</a> and menus</li>
    <li><a href="#functionsPages">Functions</a> pages</li>
    <li><a href="#homePageIcons">Icons</a> in the PIMS Homepage</li>
    <li><a href="#contextMenus">Context menus</a></li>
    <li><a href="#diagram">Diagrams</a></li>
   </ul>
  </li>
 </ul>
 </div>
 <div class="rightcolumn">
 <ul> 
  <li><a href="Perspectives.jsp">Perspectives</a></li>
  <li><a href="#breadCrumb">Breadcrumb trails</a></li><!-- TODO-->
 </ul>
 </div>
</pimsWidget:box>
</div> <!--end div help-->

  <h3 id="homePage">PIMS Homepage</h3>
  <div class="helpcontents">
  <p class="textNoFloat">
  When you start using PIMS the first view is the PIMS homepage. 
  This page displays a number of features found on all PiMS pages such as the &quot;Header&quot;
  or page title, and the PiMS-logo.  On the Home page the title indicates the version of PiMS which you are using. 
  It also contains a number of labelled boxes or &#39;Bricks&#39; with links to some PiMS functionalities. 
  As you use PiMS these Bricks will become populated with lists of links to certain types of PiMS records.</p>

  <br />
  </div> <!--end div class="textNoFloat"-->
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/PIMSHomepage.png" alt="PIMS Homepage" />
  <div class="toplink"><a href="#">Back to top</a></div>
 
  <h3 id="logIn">Logging in to PIMS</h3>
  <div class="textNoFloat">
  You will need to log in to PIMS if you attempt to view, create or edit any data.<br />
  Click <strong>Log in</strong> in the menu bar 
  to see the page below: 
  <br /><br />
  <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/PIMSLoginScreen.png" alt="PIMS log in screen" />
  <ul>
  <li>Enter your username and password then click <input class="button" value="Login" type="submit" /><br /><br /></li>
  <li>If you details are correct, and you have the necessary permission, you will be logged into PiMS.<br /><br /></li>
  <li>If not you will see a &quot;Permission denied&quot; page.</li>
  </ul>
  </div> <!--end div class="textNoFloat -->
  <div class="textNoFloat">
  <ul>
   <li>You will be automatically logged out of PIMS after 30 minutes if there has been no activity.<br />
   &nbsp; -and you will be re-directed to the log in screen when you next attempt to access information in PIMS<br /><br /></li>
   </ul>
   <strong>note:</strong> after you have logged in a drop-down list labelled &#39;Perspective:&#39;
   will be displayed in the top right hand corner of the page, with &#39;standard&#39; selected.<br />
   Perspectives allow custom views of PiMS data and menus.  <em>-see <a href="Perspectives.jsp">Perspectives</a> for more detail</em>
  </div> <!--end div class="textNoFloat -->
  <div class="toplink"><a href="#">Back to top</a></div>
    
  <h3 id="navigation">Navigating around PIMS</h3>
  <h4 id="menuBar">Drop down Menus</h4>
   All pages in PIMS have a horizontal menu bar located below the PIMS logo and page title.<br />
   This includes a link to the PIMS Homepage &#39;<strong>Home</strong>&#39; and the &#39;<strong>Log in</strong>&#39; or 
   &#39;<strong>Log out</strong>&#39; link.
   &nbsp; <em>-it will include your user-name if you are logged in.</em><br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/loggedIn.png" alt="PIMS menu example" />
   <ul>
    <li>Mouse-over the menu items in the navigation bar to see a drop-down menu of links to various PIMS functionalities.<br />
    &nbsp; -a blue bar appears as you move your mouse-over the list of menu items.<br /><br /></li>
    <li>If you have logged in, clicking a link in the menu will direct you to the appropriate page or form.<br /><br /></li>
	<li id="functionsPages">Click "More..." at the bottom of a menu to be directed to a <strong>Functions</strong> page.<br />

	The example shows the Target functions page.<br />
	<br />Functions pages can also be reached by selecting the last item in a menu, called "More...".<br />
	<img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/targetFunctions.png" alt="Target functions page" />
	
	<br/><br /></li>
	<li>Functions pages contain the links which are listed in the appropriate menu, 
	along with additional links to Create, Search and View related records.<br />
	e.g. the Target functions page includes links to Target groups, Lab Notebooks, Molecules, 
	Species (Natural source), Database references and relevant Reference data.</li>	
  </ul>
  
  <h4 id="homePageIcons">Icons in the PIMS Homepage</h4>
    After you have logged in some of the Home page Bricks may contain lists of links to PiMS records.<br />
    The different types of PiMS records are identified by icons.<br /><br />
    As you use PiMS the <strong>History</strong> brick will also contain links to the PiMS records you have visited or created. 
    <em>( -see <a href="HelpMRU.jsp">History</a> for more details)</em> 
    .<br /><br />
   	The History brick may contain items such as:<br /><br />
	<ul>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/person.gif" alt="Person icon" /> <a href="javascript:void(0)">Demo PiMS</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Person record</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/sample.gif" alt="Sample icon" /> <a href="javascript:void(0)">000001R1</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Sample record
	 &nbsp; <em>-see also the <strong>Active samples older than 7 days</strong> Brick</em></li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/plate.gif" alt="Plate icon" /> <a href="javascript:void(0)">Plate (from order) 63103</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Plate experiment record</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/protocol.gif" alt="Protocol icon" /> <a href="javascript:void(0)">SSPF Purification 1</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a PiMS Protocol (Experiment template)</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/experiment.gif" alt="Experiment icon" /> <a href="javascript:void(0)">SPOT Ttx 1576.N 62368</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Single experiment record</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/construct.gif" alt="Construct icon" /> <a href="javascript:void(0)">Ttx 1576.N</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to Construct details page 
	 &nbsp; <em>-see also the <strong>Constructs no progress for 7 days</strong> Brick</em></li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/target.gif" alt="Target icon" /> <a href="javascript:void(0)">000849</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Target details record</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/location.gif" alt="Location icon" /> <a href="javascript:void(0)">OPPF RH-80 Freezer</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Location details record</li>
	 <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/types/small/holder.gif" alt="Holder icon" /> <a href="javascript:void(0)">000001R1</a>
	 <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/> -a link to a Holder details record</li>
	</ul>
  
   <h4 id="contextMenus">Context menus</h4>
   In addition to icons (see <a href="#homePageIcons">icons</a>), links to PiMS records displayed on the Home page 
   are labelled with a black triangle <img src="${pageContext.request['contextPath']}/skins/default/images/icons/misc/pulldown.gif"/><br />
   This is a context menu icon.<br /><br />
   Click this icon and you will see a short menu of actions you can perform with the record.
   To perform these actions, mouse down the list and click the menu item.<br /><br />
   The example shows the context menu for a Target record in the <strong>History</strong> Brick<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/contextMenuExample.jpg" alt="Example context menu on home page" />
   <br /><br />
   The Target context menu includes the following possible actions :<br /><br />
   <ul>
    <li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/view.gif" alt="View icon" /> View &nbsp;&nbsp;<em>-a link to the Target details page</em></li>
<!--
	<li><img src="../skins/default/images/icons/actions/viewdiagram.gif" alt="Diagram icon" />View diagram &nbsp;&nbsp;<em>-to see a diagramatic representation of the Target</em></li>
    <li><img src="../skins/default/images/icons/actions/create/construct.gif" alt="Create construct icon" />New construct... &nbsp;&nbsp;<em>-a link to the form for recording a New Construct for the Target</em></li>
-->
	<li><img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" alt="Delete icon" /> Delete &nbsp;&nbsp;<em>-to delete the record if you have permission to do so</em></li>
   </ul>
   
   
<!-- TODO ADD LATER WHEN USED  
   	 <li><img src="../skins/default/images/icons/actions/create/experiment.gif" alt="Create new Experiment icon"><br /><br /></li>
	 <li><img src="../skins/default/images/icons/actions/create/plate.gif" alt="Create new Plate icon"><br /><br /></li>
	 <li><img src="../skins/default/images/icons/actions/create/protocol.gif" alt="Create new Protocol icon"><br /><br /></li>
	 <li><img src="../skins/default/images/icons/actions/create/sample.gif" alt="Create new Sample icon"><br /><br /></li>
	 <li><img src="../skins/default/images/icons/actions/create/person.gif" alt="Create new Person icon"><br /><br /></li>
 -->   

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
                text="Diagram" />link on a Construct, Experiment, Sample or Target page.<br />
   Diagrams are interactive: clicking a shape goes to the page that describes it in detail.<br /><br />

   The Diagram for a <strong>Target</strong> provides a simple form of navigation between Target, Construct and Experiment records in PiMS.<br />
  <div class="indent"><img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/targets/spotTargetDiagram.png" alt="Target diagram" /></div><br />
 <ul>
  <li>The <strong>Target</strong> from which the Diagram was generated, is represented by an irregular 
  <strong>pentagon</strong> with a red border<br />
  labelled with the Target's &#39;Name&#39; <em>-000914 in the example.</em><br />
  &nbsp; -clicking this symbol will to return you to the Target details page.<br /><br /></li>
  <li>Blue <strong>octagons</strong> represent Constructs which are also called <strong>Research Objectives</strong> 
  <em> &nbsp; -see <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp#diagramView">Research Objective</a> for an explanation</em><br />

  Two Research Objectives are shown: one represents a Construct '000914.con1' and the other is the &#39;Default&#39; 
  Research Objective representing the Full length Target<br />
  &nbsp; -click this symbol to see the relevant Target or Construct details page.<br /><br /></li>

  <li><strong>Experiments</strong> which have achieved a Milestone are represented by blue <strong>ellipses</strong> 
  labelled with the <strong>Experiment type</strong> and the &#39;Name&#39; of the Experiment.<br />
  The example shows that 2 Experiments performed using Construct 000914.con1 have achieved Milestones.<br />
    &nbsp; -click this symbol to see the full details for the Experiment.<br /> 
  <em> -these would also be listed in the Experiments box for the Construct and its parent Target.</em>
  <br /><br /></li>
 </ul>

   The Diagrams for <strong>Experiments</strong> and <strong>Samples</strong> are also referred to as <strong>Workflow</strong> 
   diagrams and illustrate how Samples feed into Experiments which in turn produce more Samples.<br />
   <div class="indent">
      <img class="imageNoFloat" id="expDiagram" src="${pageContext.request.contextPath}/images/helpScreenshots/experiment/experimentDiagram.png" alt="Experiment diagram example" />
   </div>
   <ul>
    <li>The <strong>Experiment</strong> from which the Diagram was generated, is represented by an 
    <strong>ellipse</strong> with a red border<br />
    labelled with the the <strong>Experiment type</strong> and the &#39;Name&#39; of the Experiment <em> &#39;SPOT Construct Design&#39; and &#39;000914.con1-SCD demo 1&#39; in the example.</em><br />
    &nbsp; -clicking this symbol will to return you to the Experiment details page.<br /><br /></li>
    <li>The Blue <strong>diamonds</strong> represent the Experiment's <strong>Output Sample(s)</strong><br />
     &nbsp;<em>-there are 3 in this example &quot;000914.con1F&quot;, &quot;000914.con1R&quot; and &quot;000914.con1T&quot;</em><br />
     &nbsp; -click this symbol to see the full details for the relevant Sample.<br /><br /></li>
    <li>The arrows show that all 3 are <strong>Inputs</strong> to the next Experiment <em>&#39;PCR PCR demo 4&#39;</em></li>
   </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>
    
  <h3 id="breadCrumb">Breadcrumb trails</h3>
  PiMS displays a navigation breadcrumb trail near the top of certain pages.<br /><br />
  For example, there is a breadcrumb trail on PiMS Target and Construct pages to help you to navigate between Target and Construct details pages.
  The example shows the breadcrumb trail on a Construct details page<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/targets/construct.png" alt="Construct details Breadcrumb trail" />
  <br /><br />
   <ul>
    <li>To see the corresponding Target details page, click the <a href="javascript:void(0)"><strong>Target: Target name</strong></a> link.<br />
    &nbsp; <em> -Target:sso1440 in the example</em><br /><br /></li>
    <li>To navigate to a list of all Targets in PiMS, click <a href="javascript:void(0)"><strong>Targets</strong></a><br /><br /></li>
   </ul>
  
  You will also find a breadcrumb trail on certain PiMS reports.<br /><br />
  The example shows the breadcrumb trail on the Scheduled Blast Report.<br />
   <img class="imageNoFloat" src="${pageContext.request['contextPath']}/images/helpScreenshots/breadcrumb2.png" alt="Construct detaisl Breadcrumb trail" />
  <br /><br />
   <ul>
    <li>To see list of PiMS Target and Construct reports, click the <a href="javascript:void(0)"><strong>Reports</strong></a><br /><br /></li>
    <li>To navigate to a list of all Targets in PiMS, click <a href="javascript:void(0)"><strong>Targets</strong></a><br /><br /></li>
   </ul>
  
  <div class="toplink"><a href="#">Back to top</a></div>
  </div> <!--end div class="helpcontents"-->
 

</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
