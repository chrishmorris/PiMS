<%@page isErrorPage="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="HeaderName" scope="page" value="Error Page" />

<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>

<h2>Error</h2>
<p>An error has been detected, please review the details below and act on it
accordingly</p>
<p>The developers will have been notified of the problem and will deal with
it as necessary</p>
<table class="list">
    <tr>
        <th>Message:</th>
        <td><%=exception.getMessage()%></td>
    </tr>
    <tr>
        <th colspan="2"><span style="float: left">Stack trace:</span><span
            onclick="showStack();" id="show" style="float: right"><img
            src="images/plus.png" alt="(show)" /></span><span id="hide"
            onclick="hideStack();" style="display: none; float: right"><img
            src="images/minus.png" alt="(hide)" /></span></th>
    </tr>
    <tr>
        <td colspan="2" id="stackTrace" style="display: none">
        <%
            StackTraceElement elements[] = exception.getStackTrace();
            for (int i = 0, n = elements.length; i < n; i++) {
                out.println(elements[i].getFileName() + ":"
                        + elements[i].getLineNumber() + ">> "
                        + elements[i].getMethodName() + "()<br/>");
            }
        %>
        </td>
    </tr>
</table>

<script type="text/javascript">
<!--
function showStack() {
    Element.show('hide');
    Element.show('stackTrace');    
    Element.hide('show');    
}

function hideStack() {
    Element.hide('hide');
    Element.hide('stackTrace');    
    Element.show('show');    
}    
-->
</script>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>

