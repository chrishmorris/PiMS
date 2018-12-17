<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<c:set var="action">${pageContext.request.contextPath}<c:out value="${param['NextAction']}" /></c:set>

    <h2>Perform new experiment with these samples:</h2> <table style="border-collapse:collapse">
        <tr>
          <th>Role</th>
          <th>Action</th>
          <th>Protocol</th>
        </tr>
    <c:forEach items="${refInputBeans}" var="ris" >
        <tr>
          <td style="vertical-align: middle;border-top:1px solid #999">${ris.name}</td>
          <td style="border-top:1px solid #999"><form style="padding-top: 4px; padding-bottom: 4px;" action="${action}" method="get" >
              <input type="hidden" name="refInputSample" value="${ris.hook}" />
              <input type="hidden"  name="protocol" value="${ris.protocol.hook}" />
              <input type="hidden"  name="experimentType" value="${ris.protocolExperimentType.hook}" />
              <input type="hidden"  name="numExperiments" value="${fn:length(updateBean.experimentBeans)}" />
              <!-- TODO remove dependencies on model objects -->
              <input type="hidden" name="inputPlate" value="${holder._Hook}" />
              <input type="hidden" name="inputGroup" value="${groupBean.hook}" />
              <input type="hidden" name="labNotebookId" value="${group.access._Hook}" />
              <input type="submit" value="New Experiment"/>
          </form></td>
          <td style="vertical-align: middle;border-top:1px solid #999">
            <pimsWidget:link bean="${ris.protocol}" />
          </td>
        </tr>
    </c:forEach>
    </table>
    <c:if test="${empty refInputBeans}">
        no suitable protocols found
    </c:if>
