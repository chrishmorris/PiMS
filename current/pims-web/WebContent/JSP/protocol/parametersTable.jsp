<%@ page contentType="text/html; charset=utf-8" language="java" 
  import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*,org.pimslims.presentation.protocol.*,org.pimslims.model.protocol.*"  
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


    <table>
        <tr>
            <th style="width:22em">Name</th>
            <th>Description</th>
            <th>Default value</th>
            <th>Possible values</th>
            <c:if test="${mayUpdate}">
                <th colspan="2" class="noprint">Actions</th>                
            </c:if>
        </tr>
        <c:forEach var="parm" items="${protocol.parameterDefinitions}" >
        <c:if test="${('group'==whichParameters && parm.values['isGroupLevel']=='Yes')
                       || ('setup'==whichParameters && parm.values['isResult']=='No' && parm.values['isGroupLevel']=='No')
                       || ('result'==whichParameters && parm.values['isResult']=='Yes' && parm.values['isGroupLevel']=='No')}">
            <c:set var="parm" value="${parm}" scope="request" />
            <tr id="${parm.hook}" class="ajax_deletable">
                <c:choose><c:when test="${'Boolean'==parm.values['paramType']}">
                    <jsp:include page="/JSP/protocol/BooleanParameter.jsp">
                        <jsp:param name="beanName" value="parm" />
                        <jsp:param name="mayUpdate" value='${mayUpdate}' />
                    </jsp:include>
                </c:when><c:when test="${'String'==parm.values['paramType']}">
                    <jsp:include page="/JSP/protocol/StringParameter.jsp">
                        <jsp:param name="beanName" value="parm" />
                        <jsp:param name="mayUpdate" value='${mayUpdate}' />
                    </jsp:include>
                </c:when><c:when test="${'Float'==parm.values['paramType'] || 'Int'==parm.values['paramType']}">
                    <jsp:include page="/JSP/protocol/NumberParameter.jsp">
                        <jsp:param name="beanName" value="parm" />
                        <jsp:param name="mayUpdate" value='${mayUpdate}' />
                    </jsp:include>
                </c:when><c:otherwise>
                    <td colspan="4"><error>Unsupported parameter type: ${parm.values['paramType']}</error></td>
                </c:otherwise></c:choose>
                <c:if test="${mayUpdate}">
                    <td style="width:40px;" class="noprint">
                    <a href="#">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/edit.gif" 
                          alt="Edit" title="Edit" 
                          id="${parm.hook}_editicon" class="icon"
                          onclick="openModalWindow('${pageContext.request.contextPath}/EditParameterDefinition/${parm.hook}','Edit parameter: ${parm.name}');return false;"
                        />
                    </a>
                    </td>                
                    <td style="width:40px;" class="noprint">
                    <a href="${pageContext.request.contextPath}/Delete/${parm.hook}">
                        <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
                          alt="Delete" title="Delete" 
                          id="${parm.hook}_deleteicon" class="icon"
                          onclick="ajax_delete(this, {cleanup:function(){protocol_checkNumOutputs()} } );return false;"
                        />
                    </a>
                    </td>                
                </c:if>            
            </tr>
        </c:if>
        </c:forEach>
        
        <c:if test="${mayUpdate}">
            <c:choose><c:when test="${'group'==whichParameters}">
                <c:set var="isGroupLevel">Yes</c:set>
                <c:set var="isResult">No</c:set>
            </c:when><c:when test="${'result'==whichParameters}">
                <c:set var="isGroupLevel">No</c:set>
                <c:set var="isResult">Yes</c:set>
            </c:when><c:otherwise>
                <c:set var="isGroupLevel">No</c:set>
                <c:set var="isResult">No</c:set>
            </c:otherwise></c:choose>
            <tr class="noprint">
                <td colspan="6" style="text-align:right; padding-right: 1.2em;">Add new 
                    <a href="#" onclick="openModalWindow('${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.ParameterDefinition?isInModalWindow=yes&amp;protocol=${protocol.hook}&amp;isResult=${isResult}&amp;isGroupLevel=${isGroupLevel}&amp;paramType=Float','New number parameter');return false">Number</a>
                    <a href="#" onclick="openModalWindow('${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.ParameterDefinition?isInModalWindow=yes&amp;protocol=${protocol.hook}&amp;isResult=${isResult}&amp;isGroupLevel=${isGroupLevel}&amp;paramType=String','New text parameter');return false">Text</a>
                    <a href="#" onclick="openModalWindow('${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.ParameterDefinition?isInModalWindow=yes&amp;protocol=${protocol.hook}&amp;isResult=${isResult}&amp;isGroupLevel=${isGroupLevel}&amp;paramType=Boolean','New Yes/No parameter');return false">Yes/No</a>
                </td>
            </tr>
        </c:if>
        </table>
