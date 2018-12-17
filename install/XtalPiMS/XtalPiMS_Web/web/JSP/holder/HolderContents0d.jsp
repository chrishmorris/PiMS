<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Generic view for any ModelObjects
Works together with ViewModelObj servlet

@author: Peter Troshin
@date: November 2005
--%>

<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.model.target.*;import org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.presentation.ServletUtil" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ page import="org.pimslims.model.holder.*" %>

<c:catch var="error">

<%-- Mandatory parameters --%>
<jsp:useBean id="ObjName" scope="request" type="java.lang.String" />
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<pims:import className="<%= Holder.class.getName() %>" />

<!-- JSP/holder/HolderContents0d.jsp -->



<table>
        
    <c:forEach var="row" items="${contentArray}" varStatus="rowCount">        
        <tr><th>${rowCount.count}</th>
        <td>
            	
			<c:choose>
				<c:when test="${!empty row}">
	    			<pimsWidget:link bean="${row}" />
	    		</c:when><c:otherwise>
	    						
								
	    		</c:otherwise>
	    	</c:choose>
   
			</td>
        <th>&nbsp;</th>
    	</tr>
    </c:forEach>
</table>

                    <span style="color:#BBBBBB;" 
                          onmouseover="style.color='#000000';"
                          onmouseout="style.color='#BBBBBB';">
                          click to add</span>
                                      
                    <img class="noprint" src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif" alt="" 
                        onclick="contextMenuName='${bean.name}'; 
                        holderDetails={ hook:'${bean.hook}', rowPosition:'0', colPosition:'0', subPosition:'0', name:'${utils:escapeJS(bean.name)}' };
                        showContextMenu(this,{ properties:[ { property:'Name', val:contextMenuName} ],
                        actions:[  
                            {text:'Add Holder', icon:'types/small/holder.gif', onclick:'containableSearch(\'org.pimslims.model.holder.Holder\',\'Holder\',holderDetails);', url:'/Search/org.pimslims.model.holder.Holder?isInModalWindow=yes'},
                            {text:'Add Sample', icon:'types/small/sample.gif', onclick:'containableSearch(\'org.pimslims.model.sample.Sample\',\'Sample\',holderDetails);', url:'/Search/org.pimslims.model.sample.Sample?isInModalWindow=yes'}]
                         })" />
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>

<!-- /Holder1DContent.jsp -->

