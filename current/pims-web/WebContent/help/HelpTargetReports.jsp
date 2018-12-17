<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help on Construct Management reports' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#constProg">Construct Progress</a></li>
  <li><a href="#constFASTA">All constructs fasta</a></li>
  <li><a href="#exptsSumm">Experiments Summary</a></li>
  <li><a href="${pageContext.request['contextPath']}/help/HelpActiveTargetsReport.jsp">Active targets report</a></li>
  <li><a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Back</a> to the Guide to using PiMS Target and Construct Management</li>
 </ul>
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><div class="helpcontents">
  <div class="textNoFloat">
  To navigate to the reports section of PIMS construct management:<br />
  &nbsp; -select &#39;Reports&#39; from the Target menu 
  </div> <!--end div class="textNoFloat"-->
  <div class="textNoFloat"> 
  
Three different reports are available.
  </div> <!--end div class="textNoFloat -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="constProg">Construct Progress</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   <ul>
    <li>Automatically shows all constructs and their latest experiment.
    <br /><br /></li>
    <li>The results are ordered by descending date -with the most recently recorded Experiment first<br /><br /></li>
    <li>The search can be refined by entering details in the search fields at the top of the page: &#39;Protein name&#39;, &#39;Organism&#39;, &#39;Lab Notebook&#39; and &#39;Milestone&#39; (status).<br /><br /></li>
    
</ul>
  </div> <!--end div class="textNoFloat -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
   
  <li><h3 id="constFASTA">All constructs fasta</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   <ul>
    <li>Automatically displays all construct sequences in FASTA format.
    <br /><br /></li>
    <li>The results are ordered by descending date -with the most recently recorded construct first.<br /><br /></li>
    <li>The search can be refined by entering details in the search fields at the top of the page: &#39;Protein name&#39;, &#39;Organism&#39; and &#39;Lab Notebook&#39;.<br /><br /></li>
    
</ul>
  </div> <!--end div class="textNoFloat -->
  
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
  
  <li><h3 id="exptsSumm">Experiments Summary</h3>
  <div class="helpcontents">
  <div class="textNoFloat">
   <ul>
    <li>Automatically displays all experiments ordered by descending date.
    <br /><br /></li>
    <li>The search can be refined by entering details in the search fields at the top of the page: &#39;Protein name&#39;, &#39;Organism&#39;, &#39;Lab Notebook&#39; and &#39;Milestone&#39; (status).<br /><br /></li>
    </ul>
  </div> <!--end div class="textNoFloat -->
  
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
