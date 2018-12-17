<%--
Display all the protocols.
This JSP is called by org.pimslims.servlet.search.ListProtocol

Author: Chris Morris
Date: Jan 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>

<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="attributes" scope="request" type="java.util.Collection" />
<%-- The caller must have sent the header --%>
<a href="../Create/${metaClass.metaClassName}">
Create a new ${metaClass.alias}</a><hr />
<%-- show the results --%>
<jsp:scriptlet>	request.setAttribute("listMetaClass", metaClass);</jsp:scriptlet>

<TABLE cellpadding="0" width="100%" summary=" with their attributes">
    <tr class="rowHeader">
    <th>?</th>
	<c:forEach items="${attributes}" var="name">
   	<th>${name}</th>
	</c:forEach>
	</tr>

<%--Display the content of a table --%>
	<% String style = "listodd"; %>
    <c:forEach items="${results}" var="mObj"	varStatus="status2">
        <tr class="<%=style%>">
             <td>
                <a href="../View/${mObj.modelObject.hook}" title="Click here to view all information recorded">View protocol</a><br />
                <a href="../Create/org.pimslims.model.experiment.Experiment?protocol=${mObj.hook}">New experiment</a>
             </td>
            <c:forEach items="${attributes}" var="attributeName"	varStatus="status3">
                <td>
                    <c:set var="outString" value="${mObj.attributes[attributeName]}" />
                    <c:if test="${fn:length(outString) > 40}">
                        <c:set var="outString">
                        ${fn:substring(outString, 0, 40)}
                        <b><label title="Not full content is displayed. Click on a view to see full value">
                            <font color='red'>...</font>
                        </label></b>
                        </c:set>
                    </c:if>
                    ${outString}
                </td>
        </c:forEach>
        <% style = style.equals("listodd") ? "listeven" : "listodd";  %>
    </tr>
    </c:forEach>
</table>

<%-- LATER makeBreadCrumb(displayName+"s") --%>
<jsp:include page="/JSP/core/Footer.jsp" />

<!-- OLD -->
