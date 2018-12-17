<%--
Display a list of PiMS records.
This fragment is used in e.g the generic view.
It is suitable for use in a delayed box.

If you edit this, you may also need to edit the custom lists. See the mappings in web.xml to find them.

Date: May 2009
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<c:catch var="error">
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />


<!-- JSP/list/Default.jsp ${metaRole.name}-->
<c:if test="${empty pageing || true==pageing}">
	<jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include> 
</c:if>

<c:choose><c:when test="${empty beans}"><p class="empty_parameters" style="border:0;">None</p></c:when><c:otherwise>

<table class="list" >

    <tr class="rowHeader">
        <th><c:choose><c:when test="${null!=actions}">
            ${controlHeader}
        </c:when><c:when test="${'administrator' eq username}">
            <!-- delete -->  
        </c:when><c:otherwise>
            <!--highlight-->&nbsp;
        </c:otherwise></c:choose>   
        </th>
        
            
       <%-- <th><!-- link --></th> --%>
            	
			<c:forEach items="${attributes}" var="elem">
	  			<th>${utils:deCamelCase(elem.key)}</th>
			</c:forEach>
  </tr>
  
<%--Display the content of a table --%>
<c:forEach items="${beans}" var="bean"	varStatus="status2" >
    <tr class="ajax_deletable" id="${bean.hook}">
			
        <c:set var="bean" scope="request" value="${bean}" />
        <c:choose><c:when test="${null!=actions}">
	        <pimsWidget:listItemControl action="${actions[bean.hook]}" bean="${bean}"  />	
	    </c:when><c:when test="${'administrator' eq username}">
	        <pimsWidget:listItemControl action="delete" bean="${bean}"  />  
	    </c:when><c:otherwise>
	        
	    </c:otherwise></c:choose>	
	    
	    
        <td style="width:20px;"><pimsWidget:link bean="${bean}" /></td> 
        
		<c:forEach items="${attributes}" var="elem"	varStatus="status3">
		<td>
            <c:set var="value" value="${bean.values[elem.key]}" />
            <c:set var="typeName" value="${elem.value.type.name}" />
		    <c:choose><c:when test="${null==value || empty value}">
		        <!-- null -->
		    </c:when>
		    <c:otherwise>
                 <c:choose>
                    <c:when test="${'addresses'==elem.key}"><%-- PIMS-447 --%>
                        <c:out value="${fn:replace(value,';',',')}" />
                    </c:when><c:when test="${typeName == 'java.util.GregorianCalendar'}">
                     	<!--<pimsWidget:dateLink date="${value.time}"  />-->
    					<c:out value="${value.time}" />
    				</c:when><c:when test="${typeName == 'java.util.Calendar'}">
                     	<!--<pimsWidget:dateLink date="${value.time}"  />-->
    					<c:out value="${value.time}" />
    				</c:when><c:when test="${typeName == 'java.lang.Boolean'}">
            			<%-- TODO display a Boolean --%>
    				</c:when><c:when test="${typeName == '[Ljava.lang.String;'}">
            			<%-- TODO display an Array --%>
    				</c:when><c:when test="${typeName == 'java.util.List'}">
            			<%-- TODO display a List --%>
    				</c:when><c:when test="${ fn:contains(elem.key,'seqString')}">
           				<%-- too long to display --%>
    				</c:when><c:otherwise>
    					<c:out value="${value}" />
    				</c:otherwise></c:choose>
		    </c:otherwise></c:choose>
		</td></c:forEach>
  				
  		<c:forEach items="${rolenames}" var="name"><td>	                      
		    <c:choose><c:when test="${null!=bean.values[name]}">
		        <pimsWidget:link bean="${bean.values[name]}" />
		    </c:when><c:otherwise>
		         &nbsp;
		     </c:otherwise></c:choose>
	     </td></c:forEach>
    </tr>
</c:forEach>
</table>
</c:otherwise></c:choose> 
<c:if test="${!param.isInModalWindow && metaRole.high ne 1}">
<div class="behaviour_showafteradd" style="display:none; padding:0.25em 0.25em 0.25em 1.9em;border-top:1px solid #999">
	Items to add:
	<pimsForm:form action="/EditRole/${modelObject.hook}/${metaRole.name}" mode="edit" method="post">
		<div class="behaviour_itemstoadd">&nbsp;</div>
		<input type="submit" value="Add" />
	</pimsForm:form>
</div>
</c:if>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if> 

<!-- /JSP/list/Default.jsp  -->

