<%@attribute name="from" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<%@attribute name="role" required="true" type="String"  %>
<%@attribute name="remove" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<form action="${pageContext.request.contextPath}/update/AjaxRemove" method="post" onsubmit='return warnChange()'>
    <input type="hidden" name="from" value="${from.hook }" />
    <input type="hidden" name="role" value="${role}" />
    <input type="hidden" name="removed" value="${remove}" />
    <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/remove.gif" onclick="this.form.submit()" />
</form>

<%-- TODO Ed please turn into AJAX --%>