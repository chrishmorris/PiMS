<%--
  Brick name: samplesReadyForUse
  Rows: 2
  Columns: 1
  
  Recently produced by successful experiments and haven't been used

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <h3 style="margin-bottom:0em" title="Recently produced by successful experiments and haven't been used">Active samples ready for use</h3>
  <div class="brickcontent">
  <span style="text-align:center;display:block;margin-top:0.5em"><b><a href="${pageContext.request.contextPath}/read/SampleProgress?active=true&amp;days_of_no_progress=0&amp;ready=yes&amp;next_exp_type=any&amp;experiment_in_use=no">All Samples Ready For Next</a></b></span>
  <jsp:include page="/read/NewSamples" />
  </div>