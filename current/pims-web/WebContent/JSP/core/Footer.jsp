<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!-- start of footer -->
</div></div>

<c:if test="${empty param['isInModalWindow']}">
	<div class="noprint main_footer">
		<div style="color:#006;width:29em;text-align:left;margin:0 auto">
		<img style="float:left;position:relative;top:-0.25em" src="${pageContext.request.contextPath}/skins/${PIMS_DISTRIBUTION }/footer_logo.png" />
		<span title="locale: ${pageContext.request.locale}">Protein Information Management System<br/>Version <c:out value="${PIMS_VERSION}" /></span>
		</div>
	</div>
</c:if>

<!--Modal window, dialog and mask-->
<div id="modalWindow_mask" class="noprint">

  <c:set var="closeLink"><img src="${pageContext.request.contextPath}/skins/default/images/icons/closeModalWindow.png"
    style="cursor:pointer" title="Close and cancel" alt="Close"
    onclick="closeModalWindow()"/></c:set>
  <pimsWidget:box id="modalWindow_window" title="Window" initialState="fixed" extraHeader="${closeLink}">
    <iframe frameborder="0" id="modalWindow_window_iframe" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif"></iframe>
  </pimsWidget:box>

  <pimsWidget:box id="modalWindow_dialog" title="Window" initialState="fixed">&nbsp;</pimsWidget:box>

  <c:set var="closeLink"><img src="${pageContext.request.contextPath}/skins/default/images/icons/closeModalWindow.png"
    style="cursor:pointer" title="Close and cancel" alt="Close"
    onclick="closeQuickSearchWindow()"/></c:set>
  <pimsWidget:box id="quickSearch_window" title="Quick search" initialState="fixed" extraHeader="${closeLink}">
    <iframe frameborder="0" id="quickSearch_window_iframe" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif"></iframe>
  </pimsWidget:box>

</div>

<jsp:include page="/JSP/core/footerJavascript.jsp" />
</body></html>
