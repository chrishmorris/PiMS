<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Edit experiment group: ${groupBean.name}" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
    <jsp:param name="extraStylesheets" value='plates/plates,plates/scores' />
</jsp:include>


<c:set var="isPlateExperiment" scope="request" value="${false}" />

<c:set var="plateExperimentBeans" scope="request" value="${updateBean.experimentBeans}" />
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/plates4.4.0.js"></script>
<script type="text/javascript">
var groupHook = "${groupBean.hook}";

var experiments={
		<jsp:include page="/JSP/json/PlateExperimentEditExperiments.jsp" />
}
//end of experiments
var protocol={
		<jsp:include page="/JSP/json/PlateExperimentEditProtocol.jsp" />
}
</script>

<pimsWidget:tabSet currentTab="basicdetails">

    <pimsWidget:tab label="Basic Details" id="basicdetails">
        <jsp:include page="tab_basicDetails.jsp"/>
    </pimsWidget:tab>

    <pimsWidget:tab label="Experiments" id="experiment" suppressPadding="true">
        <jsp:include page="tab_experimentGroupView.jsp"/>
    </pimsWidget:tab>
    
    <pimsWidget:tab label="Next experiment" id="nextexperiment">
        <jsp:include page="tab_nextExperiment.jsp">
        <jsp:param name="NextAction" value="/Create/org.pimslims.model.experiment.ExperimentGroup" />
        </jsp:include>
    </pimsWidget:tab>
    
    <pimsWidget:tab label="Attachments" id="attachments">
        <jsp:include page="tab_attachments.jsp"/>
    </pimsWidget:tab>
    
    
</pimsWidget:tabSet>

    <script type="text/javascript">
    Event.observe(window,"load",function(){ 
    	experimentGroupInit(); 
    });
    </script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

</div></div><!-- close #content and #contentwrapper - no standard footer on tabbed layout -->

<!--Modal window, dialog and mask-->
<div id="modalWindow_mask" class="noprint">

  <c:set var="closeLink"><img src="${pageContext.request.contextPath}/skins/default/images/icons/closeModalWindow.png"
    style="cursor:pointer" title="Close and cancel" alt="Close"
    onclick="closeModalWindow()"/></c:set>
  <pimsWidget:box id="modalWindow_window" title="Window" initialState="fixed" extraHeader="${closeLink}">
    <iframe frameborder="0" id="modalWindow_window_iframe" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif"></iframe>
  </pimsWidget:box>
  <pimsWidget:box id="modalWindow_dialog" title="Window" initialState="fixed">&nbsp;</pimsWidget:box>
</div>


<!-- note no footer -->
<jsp:include page="/JSP/core/footerJavascript.jsp" />
</body>
</html>