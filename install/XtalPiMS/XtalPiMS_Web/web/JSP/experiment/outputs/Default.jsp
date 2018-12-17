<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
Show the output sample for the experiment.
LATER support managing amounts.
TODO support purification
--%>
<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!--  /JSP/experiment/outputs/Default.jsp -->
<c:choose><c:when test="${empty outputsamples}">
    <pimsForm:form mode="view" action="/experiment/UpdateOutputSamples" method="post">
    <table>
        <tr>
            <th colspan="3">No output samples are defined in the protocol</th>
        </tr>
    </table>
    </pimsForm:form>
</c:when>
<c:otherwise>
  <c:set var="amountLabel"><%=org.pimslims.model.sample.Sample.PROP_CURRENTAMOUNT %></c:set>
  <c:set var="sampleLabel"><%=org.pimslims.model.sample.Sample.PROP_NAME %></c:set>
  <pimsForm:form mode="view" action="/experiment/UpdateOutputSamples" method="post">
  <table border="0">
        <tr>
            <th rowspan="2" style="width:15em;">Outputs</th>
            <td colspan="3" style="border:0;">&nbsp;</td>
        </tr>
        <tr style="">
            <th class="amountheader">Amount</th>
            <th>Sample name</th>
            <th style="width:5em">Copy</th>
            <th style="width:5em">Delete</th>
        </tr>
		<c:forEach items="${outputsamples}" var="row" varStatus="rowStatus">
		
		  <tr id="${row.outputSampleHook}" class=" ajax_deletable">
		  <th><c:out value="${row.outputSampleName}" /></th>
                <td>
                    <span class="viewonly">
                        ${row.amount}
                    </span>
                    <span class="editonly">
                        <pimsForm:doAmount hook="${row.sampleHook}" propertyName="${amountLabel}" value="${row.amount}" />
                    </span>
                </td>                   
				 <td>
                    <span class="viewonly">
				       <c:choose><c:when test="${!empty row.sampleHook}">
                            <a ${'OK' eq modelObject.values['status'] ? ' tabindex="1" ' : '' }
                             href="${pageContext.request.contextPath}/View/${row.sampleHook}">${fn:escapeXml(row.sampleName)}</a> <%--Should be context-menu link--%>
                        </c:when><c:otherwise>
                            (None)
                        </c:otherwise></c:choose>
                    </span>
                    <span class="editonly">
                        <input title="sample name" style="width: 30em;" name="${row.sampleHook}:${sampleLabel}" value="<c:out value='${row.sampleName}' />" />
                    </span>
				</td>
				
                <td style="text-align: left; padding-left: 2.9em;">
                    <img class="copy" 
                        src="${pageContext.request.contextPath}/skins/default/images/icons/actions/copy.gif"
                        title="Make a copy of <c:out value='${row.sampleName}' />"
                        alt="Copy <c:out value='${row.sampleName}' />"
                        onclick="var frm=$(this).up('form');frm.innerHTML+='<input type=\'hidden\' name=\'${row.sampleHook}:copy\' />';frm.submit()"
                    />
                 </td>
                 
                 <td style="text-align: left; padding-left: 2.9em;">
                 	
                 	<!-- <div id="${row.outputSampleHook}" class=" ajax_deletable"> -->
                 	
                    	<c:choose><c:when test="${row.mayDelete}">
                    		<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
          						alt="Delete" title="Delete"
          						id="${row.outputSampleHook}_deleteicon" 
          						onclick="ajax_delete(this, {cleanup:function(){experiment_checkNumOutputs()} } );return false;"
      						/>
      					</c:when><c:otherwise>
							<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" 
          					alt="Can't delete" title="Can't delete"
          					id="${row.outputSampleHook}_deleteicon" 
							/>
   						 </c:otherwise></c:choose>
                    
                    <!-- </div> -->
                 </td>
		  </tr>
		  
		</c:forEach>
		
        <tr>
            <td colspan="5" style="padding-right:1em">
                <pimsForm:editSubmit/>
            </td>
        </tr>
  </table>
  </pimsForm:form>
</c:otherwise>
</c:choose>
<!--  /outputsamples.jsp -->
