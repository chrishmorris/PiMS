<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

    <c:set var="showGroupLevelParams">${false}</c:set>
    <c:set var="showResultParams">${false}</c:set>
    <c:if test="${true eq param['groupLevelParams']}">
        <c:set var="showGroupLevelParams">${true}</c:set>
    </c:if>
    <c:if test="${true eq param['resultParams']}">
        <c:set var="showResultParams">${true}</c:set>
    </c:if>

    <%--
    * TODO  handle scores
    *       verify group-level parameters handled correctly
    --%>

    <c:forEach var="pd" items="${parameterDefinitions}">
        <c:if test="${!fn:startsWith(pd.name, '__') && pd.isResult eq showResultParams && pd.isGroupLevel eq showGroupLevelParams}">
	        <div class="param pd${pd.dbId}">
	        <h5>${pd.name}<c:if test="${pd.isMandatory}"><span class="required">*</span></c:if></h5>
	        <form action="#" onsubmit="if(validateFormFields(this,true)){updateParameterValue(this);}return false;">
	            <c:choose>
                   <c:when test="${'Boolean' eq pd.paramType}">
                        <select class="paramvalue" name="${pd._Hook}" id="${pd._Hook}">
                            <option value="(various)" disabled="disabled">(Various)</option>
                            <option value="true" selected="selected">Yes</option>
                            <option value="false">No</option>
                        </select>
                   </c:when>
                   <c:when test="${fn:length(pd.possibleValues) gt 0}">
                        <select class="paramvalue" name="${pd._Hook}" id="${pd._Hook}">
                            <option value="(various)" disabled="disabled">(Various)</option>
                            <c:forEach var="pv" items="${pd.possibleValues}">
                                <c:choose><c:when test="${pd.defaultValue eq pv}">
                                    <option value="${pv}" selected="selected">${pv}</option>
                                </c:when><c:otherwise>
                                    <option value="${pv}">${pv}</option>
                                </c:otherwise></c:choose>
                            </c:forEach>
                        </select>
                   </c:when>
                   <c:when test="${'Int' eq pd.paramType || 'Float' eq pd.paramType}">
                        <input class="paramvalue number" type="text" name="${pd._Hook}" id="${pd._Hook}" value=""/>
                        <script type="text/javascript">attachValidation("${pd._Hook}", { numeric:true,
                            <c:if test="${'Int' eq pd.paramType}">wholeNumber:true,</c:if>
                            <c:if test="${!empty pd.minValue}">minimum:${pd.minValue},</c:if>
                            <c:if test="${!empty pd.maxValue}">maximum:${pd.maxValue},</c:if>
                            <c:if test="${pd.isMandatory}">required:true,</c:if>
                            alias:"<c:out value="${pd.name}"/>" } );
                         </script>
                   </c:when>
                   <c:when test="${'String' eq pd.paramType}">
                        <input class="paramvalue string" type="text" name="${pd._Hook}" id="${pd._Hook}" value=""/>
                        <c:if test="${pd.isMandatory}">
                            <script type="text/javascript">attachValidation("${pd._Hook}", { required:true, alias:"<c:out value="${pd.name}"/>"} );</script>
                        </c:if>
                   </c:when>
                </c:choose>
                <input type="hidden" name="${pd._Hook}:displayUnit" id="${pd._Hook}:displayUnit" value="${pd.displayUnit}"/>
                <input type="submit" class="submit" value="Update"/>
	        </form>
	        </div>
        </c:if>
    </c:forEach>
    
    
