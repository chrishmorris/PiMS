<%--
  Brick name: oldUnusedSamples
  Rows: 2
  Columns: 1
  
  Samples produced before 7 days ago and haven't been used

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <h3 style="margin-bottom:0em" title="Samples produced before 7 days ago and haven't been used">Active samples older than 7 days</h3>
  <div class="brickcontent">
  <span style="text-align:center;display:block;margin-top:0.5em"><b><a href="${pageContext.request.contextPath}/read/SampleProgress?active=true&amp;days_of_no_progress=7&amp;ready=any&amp;next_exp_type=any&amp;experiment_in_use=no">All Samples Older Than 7 Days</a></b></span>
  <jsp:include page="/read/WeekOldSamples" />
  </div>