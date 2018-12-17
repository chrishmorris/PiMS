<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="label" required="true"  %>
<%@attribute name="id" required="true"  %>
<%@attribute name="suppressPadding" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%-- 
 * Makes a tab, for use in a tabbed layout. Must be nested in a <pimsWidget:tabSet />.
 *
 * By default, tabs have padding to keep the content away from the edges. If you need to
 * get rid of this, specify suppressPadding="true".
 * 
 * You can specify an onclick function. This will receive the clicked tab as a parameter.
 * If the function returns true, the tab is switched as expected. If false, the default
 * tab switching behaviour is not run. This allows for customised behaviour, e.g., if
 * several tabs show a set of plates and you don't want several copies of the HTML, have
 * the onclick function modify the one copy and show it - then return false to prevent the
 * default switching behaviour.
 *
--%>
<c:set var="noPaddingClass" value="" />

<c:set var="current"></c:set>
<c:if test="${id eq currentTab}">
    <c:set var="current">current</c:set>
</c:if>

<c:set var="preswitch" value=""/>
<c:if test="${!empty onclick}">
<c:set var="preswitch" value="if(${onclick}(this))"/>
</c:if>

<h3 class="pw_tab ${current}" id="${id}" onclick="${preswitch} switchTab(this)">${label}</h3>
<div id="${id}_body" class="${noPaddingClass} pw_tabbody ${current}">
    <c:choose><c:when test="${!empty suppressPadding}">
        <jsp:doBody />
    </c:when><c:otherwise>
        <div class="pw_tabbodyInner">
        <jsp:doBody />
        </div>
    </c:otherwise></c:choose>
</div>
