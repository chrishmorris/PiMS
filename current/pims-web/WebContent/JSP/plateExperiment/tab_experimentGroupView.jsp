<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<div id="plates" class="nonplategroup platescontainer">

<%-- FORM FOR ADDING AND DELETING EXPERIMENTS
    <form action="#" id="addremove" onsubmit="return false">
        <input type="button" disabled="disabled" id="deletebutton"
            value="Delete selected experiments" 
            onclick="deleteSelectedExperiments(this)" />
    </form>
--%>

    <c:forEach var="expt" items="${updateBean.experimentBeans}" varStatus="status">
        <div class="groupexperiment ex${expt.dbId}" title="${expt.name}">
        ${status.count}
        </div>
    </c:forEach>
</div>
<div id="paramssamples">
    <jsp:include page="parametersAndSamples.jsp"/>
</div>

<div id="experimentcontents">&nbsp;</div>