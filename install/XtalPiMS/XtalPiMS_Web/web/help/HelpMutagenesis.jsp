<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Help Guide to Site Directed Mutagenesis in PiMS' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#navigating">Starting site directed mutagenesis</a></li>
  <li><a href="#step1">Step 1</a></li>
  <li><a href="#step2">Step 2</a></li>
  <li><a href="#step3">Step 3</a></li>
  <li><a href="#mutatedconstruct">The mutated construct view</a></li>
 </ul>
 <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide</a> to using PIMS<br />
 <a href="${pageContext.request['contextPath']}/help/target/HelpTarget.jsp">Guide</a> to PIMS Target management <br />
</pimsWidget:box>
</div> <!--end div help-->
<div class="toplink"><a href="#">Back to top</a></div>

 
  <li><h3>Site Directed Mutagenesis</h3>
  <div class="helpcontents">
  PiMS can help in recording mutagenisis and in designing sense and antisense primers.
  <br />
  The general approach to achieve this is to design a construct, forward and reverse primers as per normal;
  and to run the PCR and infusion experiments to make a wild-type expression vector.  
  The mutation is made by designing SDM primers to match a coding region in this wild-type expression 
  vector and running an inverse PCR experiment make a mutated expression vector.
  <br /><br />

  <div class="textNoFloat">
  <h4 id ="navigating">Starting site directed mutagenesis in PiMS</h4>
  From the Construct view select <span class="spotLink">New SDM Primers</span> in the page header, 
  this takes you to the first stage of the SDM construct design process.
  <br /><br />
  <img class="imageNoFloat" src="../images/helpScreenshots/sdm1.jpg" alt="starting site directed mutagenesis" />
  <br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h4 id="step1">Site directed mutagenesis step 1</h4>
   In step 1 of the process you may enter a name for the mutated construct, this should be unique within 
   PiMS;  a default name is offered.  Also you may enter a Tm value on which to base the primer design.
   <br /><br />
   <img class="imageNoFloat" src="../images/helpScreenshots/sdm2.jpg" alt="site directed mutagenesis step 1" />
   <br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h4 id="step2">Site directed mutagenesis step 2</h4>
   Step2 is where the mutation is described.  You may edit amino acids in the protein sequence, or codons 
   in the DNA sequence.  Insertions may be made by clicking on the insert icon at the appropriate spot.
   Deletions are made by clicking on the delete icon above ech codon.  Press next when you have made your mutations.
   <br /><br />
   <img class="imageNoFloat" src="../images/helpScreenshots/sdm3.jpg" alt="site directed mutagenesis step 2" />
   <br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h4 id="step3">Site directed mutagenesis step 3</h4>
   PiMS will suggest suitable sense and antisense primers based on the formula 
   Tm = 81.5 + 0.41(%GC) - 675/N - %mismatch;  considering the terminating bases, GC content and primer length.  
   You may select the most suitable primers  or enter one of your own.  
   Click on Save when you have made your choice.
   <br /><br />
   <img class="imageNoFloat" src="../images/helpScreenshots/sdm4.jpg" alt="site directed mutagenesis step 3" />
   <br /><br />
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <h4 id="mutatedconstruct">The view of the mutated construct</h4>
   The view of the mutated construct shows the associated sequences.  Information about the sense and antisense primers 
   is shown by opening the appropriate box. Values are displayed for the sequence, length, Tm and %GC.
   <br /><br />
   <img class="imageNoFloat" src="../images/helpScreenshots/sdm5.jpg" alt="mutated construct view" />
   <br /><br />
  
   </div> <!-- end div class="textNoFloat" -->
  </div> <!--end div class="helppage"-->
  <div class="toplink"><a href="#">Back to top</a></div>
  </li>
 
 

<jsp:include page="/JSP/core/Footer.jsp" />
