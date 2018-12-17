<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %> 
<%@ taglib tagdir="/WEB-INF/tags/pimsWidget" prefix="pimsWidget"  %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Primer Order Form' />
</jsp:include>

<!-- OLD -->

<c:catch var="error">

<c:if test="${! empty param.linkto}" >
    <c:set var="prm" value="?linkto=${param.linkto}" />
</c:if>

<div style="padding-left: 1em;" class="tabcontent"> 
  <form action="${pageContext.request.contextPath}/OppfPrimerOrderForm" method="post" 
        name="myForm" onsubmit="return submitForm();">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/OppfPrimerOrderForm')}" />

    <h2>Plate Order Experiments</h2>

<c:choose>
<c:when test="${empty holdersOfOrder}">There are no Plate Order experiments to select.</c:when>
<c:otherwise> 

<p>Please select the Plate Order experiment from the list below.<br />
The completed Primer Order spreadsheet will be opened by your browser.</p>
      
<pimsWidget:box title="Plate Order Experiments" initialState="open">
  <span class="pagelinks"><input type="submit" value="Create OrderForm"  name="userreq" /></span>
  <table class="list">
    <%-- table head --%>
    <tr>
        <th>Select</th><th>Plate</th>                        
    </tr>
    <%-- table body --%>
    <c:forEach var="entry" items="${ holdersOfOrder }"> 
      <tr>
        <td style="padding:2px 0 0 3px;width:2em;"><input type="radio" name="primerform" value="${entry.key}" /></td>
        <td style="padding:2px 0 0 3px;">
          <span class="linkwithicon " title="Experiment group"><a href='${pageContext.request.contextPath}/View/<c:out value="${entry.key}"/>' onclick='return warnChange()'><img class="icon" alt="" src="${pageContext.request.contextPath}/skins/default/images/icons/types/small/plate.gif" /></a><a href='${pageContext.request.contextPath}/View/<c:out value="${entry.key}"/>' onclick='return warnChange()'><span class="linktext">${entry.value}</span></a></span>
        </td>
      </tr>
    </c:forEach>
  </table>
  <div style="border-top:1px solid #999999"><input type="submit" value="Create OrderForm"  name="userreq" /></div>
</pimsWidget:box>

</c:otherwise></c:choose>
</form></div>

</c:catch>
<c:if test="${error != null}">
  <p class="error">error ${error}</p>
</c:if>


<script type="text/javascript">
<!--
function submitForm() {

    thisform=document.myForm;
    
    if (0 == thisform.primerform.length)
        alert("No Experiments - go back to the menu");
    
    if (thisform.primerform.length) {
        for (var i=0; i < thisform.primerform.length; i++) {
            if (thisform.primerform[i].checked) { return true; }
        }
    }
    if (!thisform.primerform.length) {
        if (thisform.primerform.checked) { return true; }
    }
        
    alert("Please check one of the Plate Order experiments");
    return false;
} // -->
</script>

<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
