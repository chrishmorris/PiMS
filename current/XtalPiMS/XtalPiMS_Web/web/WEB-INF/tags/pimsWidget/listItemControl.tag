<%@tag   %>
<%-- Provides the appropriate control for line in a list of beans.
See /JSP/list/.
See also ListRole.java, and ChooseForCreate.java.
--%>
<!-- listItemControl.tag -->
<%@attribute name="initialState" required="false"  %>
<%@attribute name="action" required="true"  %> 
<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<c:choose><c:when test="${null ne param['callbackFunction']}">
            <td style="text-decoration:underline;cursor:pointer" 
            onclick="window.parent.${callbackFunction}({hook:'${bean.hook}',name:'${utils:escapeJS(bean.name)}'})"> 
            Select </td>
</c:when><c:when test="${'' eq action || null eq action}">
            <%-- Nothing --%>
</c:when><c:when test="${'remove' eq action}" >
            <td style="padding:2px 0 0 3px;text-align:center;width:20px;">
            <a href="#" onclick="removeAssociate('${modelObject.hook}','${metaRole.name}','${bean.hook}');  return false;"><img style="border:0" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/remove.gif" alt="Remove" title="Remove (but don't delete)"></a>
            </td>
</c:when><c:when test="${'cant_remove' eq action}" >
            <td style="padding:2px 0 0 3px;text-align:center;width:20px;">
            <img style="border:0" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/remove_no.gif" alt="Can't remove this" title="Can't remove this">
            </td>
</c:when><c:when test="${'delete' eq action}" >
            <td style="text-align:center;width:20px;">
            <a href="${pageContext.request.contextPath}/Delete/${bean.hook}" 
                onclick="ajax_delete(this);return false;"><img style="border:0" 
                    src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
                    alt="Delete" title="Delete" /></a>
            </td>
</c:when><c:when test="${'modalMultiAdd' eq action}" >
            <td style="padding:2px 0 0 3px;text-align:center;width:20px;">
            <c:set var="objectData">
            {
                        hook:'${bean.hook}',
                        name:'${utils:escapeJS(bean.name)}' 
            }
            </c:set>
            <input type="checkbox" onclick="prepareForAdd(this,${objectData})" />
            </td>
</c:when><c:when test="${'modalSingleAdd' eq action}" >
            <td style="padding:2px 0 0 3px;text-align:center;width:20px;">
            <c:set var="objectData">
            {
                        hook:'${bean.hook}',
                        name:'${utils:escapeJS(bean.name)}' 
            }
            </c:set>
            <span style="text-decoration:underline; color:#006;cursor:pointer;" onclick="window.parent.selectInMRU(${objectData})">Add</span>
            </td>
</c:when><c:when test="${'replace' eq action}" >
<%-- PIMS-3527.  --%>
            <td style="padding:2px 0 0 3px;text-align:center;width:20px;">
            <c:set var="objectData">
            {
                        hook:'${bean.hook}',
                        name:'${utils:escapeJS(bean.name)}' 
            }
            </c:set>
            <span style="text-decoration:underline; color:#006;cursor:pointer;" onclick="window.parent.selectInMRU(${objectData})">Replace</span>
            </td>
</c:when><c:otherwise>
    <td>${action}</td>
</c:otherwise></c:choose>

<!-- /listItemControl.tag -->


                                    