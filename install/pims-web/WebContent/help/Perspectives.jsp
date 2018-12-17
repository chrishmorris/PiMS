<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Perspectives' />
</jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request['contextPath']}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Perspectives"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
        <p>
        At the top right hand corner of most PiMS pages, there is a drop down list of "Perspectives".
        By changing the perspective, you can change the menu items available to you,
        and also change the way some of the records in PiMS are displayed.</p>
        <p>In future releases of PiMS, there will be perspectives suitable
        for particular roles the in the laboratory.
        At present, most users of PiMS do not need to change perspective.
       </p>
    </div>
  </pimsWidget:box>
</div>
<div class="helpcontents">
 <div class="glossary">
    <h4>There are a number of perspectives available in PiMS:</h4> 
    <dl>
     <dt>Leeds</dt><dd>
        This perspective adds menu entries for construct management
        that support a particular method of working.
        In a future release of PiMS, we will adapt these features
        to make them suitable for general use.
     </dd>
     <dt>SSPF</dt><dd>
        This perspective provides custom views of targets and constructs.
        In a future release of PiMS we will adapt these features
        to make them suitable for general use.
     </dd>
     <dt>standard</dt><dd>Most users will spend most of their time in PiMS
        using the standard perspective.
     </dd>
    </dl>

    <h4>Two perspectives are only available to the administrator: </h4>
    <dl>
     <dt>admin</dt><dd>Contains menus for administering users and for access to Reference data</dd>
     <dt>expert</dt><dd>If you report problems in your PiMS installation,
        we may give you a recovery procedure that involves
        using the expert perspective.
     </dd>
    </dl>
 </div>
</div>
<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />