<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
Show the output sample for the experiment.
LATER support managing amounts.
TODO support purification
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!--  outputsamples.jsp -->
obsolete
<c:choose><c:when test="${empty outputsamples}">
    No output samples defined
</c:when>
<c:otherwise>
<h4 class="printonly"> OutputSample </h4>
<table border="0">    
        <tr>
        <th title="The part the output plays in the protocol"><fmt:message key="exp.titles.table.outputsamples.name"/></th>
        <th title="The actual sample"><fmt:message key="exp.titles.table.outputsamples.value"/></th>
        <th title="The amount and units of the sample"><fmt:message key="exp.titles.table.outputsamples.amount"/></th>
        <th title="Make a copy of the sample">Copy</th>
        </tr>
			<c:forEach items="${outputsamples}" var="row"
				varStatus="rowStatus">
				<tr>
					<td><a
						href="${pageContext.request.contextPath}/View/${row.outputSampleHook}"
						title="<c:out value="${row.description}" />">
					<c:out value="${row.outputSampleName}" /> </a>
					</td>
					<td><c:choose>
						<c:when test="${empty row.sampleHook}">
                          none
                        </c:when>
						<c:otherwise>
						    <input name="${row.sampleHook}:<%=org.pimslims.model.sample.Sample.PROP_NAME %>" value="<c:out value='${row.sampleName}' />" />
							<a href="${pageContext.request.contextPath}/View/${row.sampleHook }">
								View
							 </a>
						</c:otherwise>
					</c:choose></td>
					<!-- 
					<td><input type="text" name="${row.sampleHook }:<%=org.pimslims.model.sample.Sample.PROP_CURRENTAMOUNT %>" value="${row.amount}"  size="10"/></td>
					-->
					<td>
					    <pimsForm:doAmount hook="${row.sampleHook}" propertyName="<%=org.pimslims.model.sample.Sample.PROP_CURRENTAMOUNT %>" value="${row.amount}" />
					</td><td>
					<input class="copy" type="image" name="${row.sampleHook}:copy" value="${row.sampleHook}:copy" 
					    title="Make a copy of <c:out value='${row.sampleName}' />" 
					    alt="Copy <c:out value='${row.sampleName}' />" 
					    
					/>
					</td>
				</tr>
			</c:forEach>
		</table>
</c:otherwise>
</c:choose>
<!--  /outputsamples.jsp -->

<!-- OLD -->
