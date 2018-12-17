<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Screen ${screen.name}' />
</jsp:include>
    <c:choose><c:when test="${screen._MayDelete}">
      <span class="linkwithicon " title="Delete inspection "><a  href="${pageContext.request['contextPath']}/Delete?hook=${screen._Hook}"><img class="icon" src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request['contextPath']}/Delete?hook=${screen._Hook}">Delete</a></span>
    </c:when><c:otherwise>
      <pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" />
    </c:otherwise></c:choose>
    
    <a href="${pageContext.request['contextPath']}/ExportScreen/${screen._Name}">Export as XML</a>
    <%-- TODO export as CSV --%>
    
<pimsWidget:box title="Screen details" initialState="fixed">
 <pimsForm:form method="post" action="/update/EditScreen/${screen.name}" mode="edit" id="new_plate">
   <pimsForm:formBlock>
         <pimsForm:column1>
       <pimsForm:text name="screenName" alias="Name" helpText="name of this screen" value="${screen.name}" validation="required:true" />
      
        <pimsForm:select name="typeName" alias="Type" helpText="the type of this screen" validation="required:true">
        <c:forEach var="p" items="${screenTypeViews}">
          <pimsForm:option optionValue="${p.name}" currentValue="${currentTypeName}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
       
       <pimsForm:select name="manufacturerName" alias="Manufacturer" helpText="the manufacturer of this screen" validation="required:true">
        <c:forEach var="p" items="${manufacturers}">
          <pimsForm:option optionValue="${p.name}" currentValue="${currentManufacturer}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
     </pimsForm:column1>
     
     <pimsForm:column2>

     <pimsForm:textarea name="description" alias="Description"><c:out value="${screen.details}" /></pimsForm:textarea> 
     	
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:editSubmit />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<pimsWidget:box initialState="closed" title="Conditions" id="conditions">
<c:set var="currentRow" value="0"/>
<c:set var="currentColumn" value="0"/>
<c:set var="current" value="0"/>
<div style="overflow:auto">
<table class="orderplate" id="orderplate">
    <tr>
    <th>&nbsp;</th>
    <c:forEach var="col" items="${cols}">
        <th>${col}</th>
    </c:forEach>
    </tr>

    <c:forEach var="row" items="${rows}">
        <tr>
            <th>${rows[currentRow]}</th>
            <c:forEach var="col" items="${cols}">
                     <td>                       
                        <c:choose>
                            <c:when test="${!empty conditions[current].well}">
                            <c:forEach var="component" items="${conditions[current].components}">
                                  <c:out value="${component.componentName}" /> (${component.quantity})<br/>
                            </c:forEach>
                            </c:when><c:otherwise>
                                [None]
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:set var="current" value="${1+current}"/>
                <c:set var="currentColumn" value="${1+currentColumn}"/>
            </c:forEach>
            <c:set var="currentRow" value="${1+currentRow}"/>
        </tr>
        <c:set var="currentColumn" value="0"/>
    </c:forEach>
    
</table>
</div>
</pimsWidget:box>


<jsp:include page="/JSP/core/Footer.jsp" />
