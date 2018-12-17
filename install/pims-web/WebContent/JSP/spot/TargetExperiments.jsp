<%--
/**
 * JSP to display a summary of all the experiments for a target
 * TODO better to get these by Ajax when the box is opened.
 *
 * @author Marc Savitsky
 */
--%>

<%@ page contentType="text/html; charset=utf-8" language="java"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:useBean id="experimentBeans" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" /> 
<!-- TargetExperiments.jsp -->

<table class="list">
<tr><th align="left">Date</th><th align="left">Status</th>
<th align="left">Experiment</th><th align="left">Type</th>
<th align="left">Protocol</th><th align="left">Milestone</th></tr>

<c:forEach items="${experimentBeans}" var="experimentBean">
<tr>
  <td><pimsWidget:dateLink date="${experimentBean.experimentDate}"  /></td>
  <td><c:out value="${experimentBean.experimentStatus}" default=" " /></td>
  
  <td><pimsWidget:link bean="${experimentBean}" /></td>
  <td><pimsWidget:link bean="${experimentBean.experimentType}" /></td>
  <td><c:if test="${null ne experimentBean.protocol}"><pimsWidget:link bean="${experimentBean.protocol}" /></c:if></td>
  <td><c:if test="${null ne experimentBean.milestone}"><pimsWidget:link bean="${experimentBean.milestone}" /></c:if></td>
</tr>
</c:forEach>
</table>



