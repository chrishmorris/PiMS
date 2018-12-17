<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<%@ page import="org.pimslims.model.experiment.Experiment" %>
<!-- barChart.jsp  -->
<c:catch var="error">



<pimsWidget:barChart bean="${chart}"  />



</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>


