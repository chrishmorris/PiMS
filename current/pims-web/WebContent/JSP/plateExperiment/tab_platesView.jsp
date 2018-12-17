<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!-- tab_platesView.jsp -->
<div id="plates" class="platescontainer">

<%-- FORM FOR ADDING AND DELETING EXPERIMENTS--%>
    <form action="#" id="addremove" onsubmit="return false">
        <input type="button" disabled="disabled" id="deletebutton"
            value="Delete selected experiments" 
            onclick="deleteSelectedExperiments(this)" />
    </form>

    <c:if test="${!empty updateBean.northWestPlate}">
        <c:set var="plateCollection" value="${updateBean.northWestPlateLayout}" scope="request" />
        <%-- TODO mispositions a 384 well plate --%>
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northwest" />
            <jsp:param name="plateName" value="${updateBean.northWestPlate.name}" />
            <jsp:param name="plateHook" value="${updateBean.northWestPlate.hook}" />
        </jsp:include>
        <script type="text/javascript">
        $("northwest").holderHook="${updateBean.northWestPlate.hook}";
        $("northwest").holderName="${updateBean.northWestPlate.name}";
        </script>
    </c:if>

    <c:if test="${!empty updateBean.northEastPlate}">
        <c:set var="plateCollection" value="${updateBean.northEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northeast" />
            <jsp:param name="plateName" value="${updateBean.northEastPlate.name}" />
            <jsp:param name="plateHook" value="${updateBean.northEastPlate.hook}" />
        </jsp:include>
        <script type="text/javascript">
        $("northeast").holderHook="${updateBean.northEastPlate.hook}";
        $("northeast").holderName="${updateBean.northEastPlate.name}";
        </script>
    </c:if>

    <c:if test="${!empty updateBean.southWestPlate}">
        <c:set var="plateCollection" value="${updateBean.southWestPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southwest" />
            <jsp:param name="plateName" value="${updateBean.southWestPlate.name}" />
            <jsp:param name="plateHook" value="${updateBean.southWestPlate.hook}" />
        </jsp:include>
        <script type="text/javascript">
        $("southwest").holderHook="${updateBean.southWestPlate.hook}";
        $("southwest").holderName="${updateBean.southWestPlate.name}";
        </script>
    </c:if>

    <c:if test="${!empty updateBean.southEastPlate}">
        <c:set var="plateCollection" value="${updateBean.southEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southeast" />
            <jsp:param name="plateName" value="${updateBean.southEastPlate.name}" />
            <jsp:param name="plateHook" value="${updateBean.southEastPlate.hook}" />
        </jsp:include>
        <script type="text/javascript">
        $("southeast").holderHook="${updateBean.southEastPlate.hook}";
        $("southeast").holderName="${updateBean.southEastPlate.name}";
        </script>
    </c:if>
    

</div>

<div id="paramssamples">
    <jsp:include page="parametersAndSamples.jsp"/>
</div>

<div id="experimentcontents">&nbsp;</div>

<!-- /tab_platesView.jsp -->    
