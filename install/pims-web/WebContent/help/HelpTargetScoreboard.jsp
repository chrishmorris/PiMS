<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help Guide to the PIMS Target Scoreboard' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#navigating">Navigating to the Scoreboard</a></li>
  <li><a href="#scoreboardExample">Example Scoreboard</a></li>
  <li><a href="#targetStatus">Status</a></li>
 </ul>
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS<br />
 <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Guide</a> to PIMS Target management <br />
</pimsWidget:box>
</div> <!--end div help-->

 
  <h3>Target Scoreboard</h3>
  <div class="helpcontents">
  The Target Scoreboard keeps track of the number of Targets recorded in PIMS. 
  It also displays how many Targets have reached each stage or &#39;Status&#39; in the protein production and crystallisation pipeline,
   and how many 
  are currently at each particular stage. 
  The scoreboard is updated each time a new Target record is created, or the status of an existing Target is updated. 
  When you record a new Target in PIMS, it is automatically assigned the status &#39;Selected&#39;. 
  &nbsp; -see <a href="#targetStatus">Status</a> for more details.

  <div class="textNoFloat">
  <h4 id ="navigating">Navigating to the Scoreboard</h4>
  The Target scoreboard can be accessed by selecting &quot;Target Scoreboard&quot; from the Target menu.
  
   <ul>
    <li>For each &#39;Status&#39; two values are recorded:<br/>
    &nbsp; -&#39;Now&#39; for the number of Targets which are currently at that particular Status<br />
    &nbsp; -&#39;Total&#39; for the number of Targets which have achieved that Status (past and present). </li>
    <li>Click a link to see a representative list of Targets<br />
    &nbsp; e.g. to see a list of Targets which are currently at the &#39;PCR&#39; stage, click <span class="spotLink">15</span> in the &#39;Now&#39; column for &#39;PCR&#39;<br /><br /></li>
   </ul>

   
   <ul>
    <li>To see the Details for a particular Target, click the link in the first column of the table.</li>
   </ul>
  
  <h4 id="targetStatus">Status</h4>
   <ul>
    <li>PIMS provides Reference data for Target Statuses.<br /><br /></li>
    <li>Each Status represents a stage in the protein production and crystallisation pipeline and relates to the &quot;Progression stages&quot; defined by TargetDB.<br /><br /></li>
    
    <li>These stages are also referred to as &quot;Milestones&quot; where they represent the most recent successful experiment.<br /><br /></li>
  
    <li>The possible statuses are:
    <blockquote>
    Selected, PCR, Cloned, ml Expression (small scale), Expressed, Auto selmet scale up, IPTG native scale up, IPTG selmet scale up, Purification, Native trials, Selmet trials, Native xtals, Selmet xtals, Crystals, Trials,  Diffraction, Plate check, Native opt, Selmet opt, Native diffraction, Selmet diffraction, Data set, Structure, PDB, Annotation, Information and Work Stopped</blockquote></li>
    <li>The status is updated <strong>automatically</strong> from the &quot;Construct Results&quot;:<br />
    &nbsp; -the milestone achieved for the most progressed construct for a given Target is used to update the status of that Target.<br />
    &nbsp; -see <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp#newResult">New result</a> for more details about recording results for constructs.<br /><br /></li>
    
    <li>Also, from an Experiment Edit page.<br />
    &nbsp; -there is a drop-down list of Target statuses</li>
   </ul>
  

   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
 
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
