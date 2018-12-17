<%@ page contentType="text/html; charset=utf-8" language="java" 
 import="java.util.*,org.pimslims.model.protocol.*,org.pimslims.lab.*"%>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<fmt:setLocale value='en_UK' />
<%@ page buffer = "64kb" %>
<jsp:useBean id="protocolParameters" scope="request" type="java.util.Map<Protocol, java.util.Set<ParameterDefinition>>" />
<%--
SelectParametersOfTargets.JSP
@param protocolParameters - map of Protocol,Set<ParameterDefinition> provided by ReportExperimentParameters servlet 
@author Bill Lin
@date August 2007
Redesign by Susanne Griffiths July 2010 NOTE: problem with the Map in Eclipse
@
--%>

<%--TODO Susy make this conditional depending on referer --%>
<c:set var="tgr" value="TargetGroupReport" />

<c:set var="breadcrumbs">
<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> : 
  <c:if test="${fn:contains(referrer,tgr)}">
    <jsp:useBean id="groupName" scope="request" type="java.lang.String" />
    <c:set var="tgrBreadcrumb" value="${fn:substringAfter(referrer, tgr)}"/>
    <c:set var="tgrHook" value="${fn:substringAfter(referrer, tgr)}"/>
    <c:set var="tgrName" value="${groupName}"/>
    
      <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a> : 
      <a href="${pageContext.request.contextPath}/report/TargetGroupReport<c:out value="${tgrBreadcrumb}"/>"><c:out value="${groupName}"/></a> :
  </c:if>
  Parameters
</c:set>
<c:set var="icon" value="blank.png" />        
<c:set var="title" value="Select Parameters to Compare"/>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}"/>

	<form action="${pageContext.request.contextPath}/report/ReportTargetParameters" method="get" id="worklist" style="background-color: transparent; width: auto;">
    <c:forEach var="entry" items="${protocolParameters}" >
     <c:set var="protocol">
      <a href="${pageContext.request.contextPath}/View/${entry.key._Hook}"><c:out value="${entry.key.name}" /></a>
     </c:set>
     <pimsWidget:box title="" initialState="open" extraHeader="${protocol}">
      <div class="collapsibleBox_content"> 
        <table border="0" class="list" >
            <c:forEach var="parameter" items="${entry.value}" >
              <tr>
               <td>
                <input name="${parameter._Hook}"  class="behaviour_selectAll"
                            type="checkbox" /> 
                <a href="${pageContext.request.contextPath}/View/${parameter._Hook}"><c:out value="${parameter.name}" /></a>
          </td>                    
         </tr>
           </c:forEach>
        </table>
      </div>      
     </pimsWidget:box>
    </c:forEach>


	<c:forEach var="targetHook" items="${targetHooks}" >
	 <input name="${targetHook}"  class="behaviour_selectAll"
							type="hidden" value="on"> 
	</c:forEach>	
    <input type="hidden" name ="labNotebookId" value="<c:out value='${constructBean.access.hook}' />"/>
  	

<%--Susy 20/07/2010 variables to make the breadcrumb trail in the report --%>
  	<c:if test="${fn:contains(referrer,tgr)}">
         <input type="hidden" name="tgrHook" value="<c:out value='${tgrHook}'/>" />
         <input type="hidden" name="tgrName" value="<c:out value='${tgrName}'/>" />
      </c:if>

    <br/>
   	
	<input  value="Next: View report" type="submit">
	</form>




<!-- SelectParametersOfTargets.jsp -->

