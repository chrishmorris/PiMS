<%--
Show the input samples for the experiment.
Managing amounts is not yet supported.
--%>
<%@ page import="org.pimslims.presentation.experiment.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<jsp:useBean id="inputsamples" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.InputSampleBean>" />
<!--  inputsamples.jsp -->

<c:set var="none">[NONE]</c:set>
<c:choose><c:when test="${empty inputsamples}">
    <pimsForm:form mode="view" action="/experiment/UpdateInputSamples" method="post">
	<table>
	  	<tr>
    		<th colspan="3">No input samples are defined in the protocol</th>
	  	</tr>
	</table>
    </pimsForm:form>
</c:when><c:otherwise>
    <pimsForm:form mode="view" action="/experiment/UpdateInputSamples" method="post">
	<table>
	  	<tr>
	    	<th rowspan="2" style="width:15em;">Inputs</th>
	    	<td colspan="2" style="border:0;">&nbsp;</td>
	  	</tr>
		<tr style="">
	  		<th class="amountheader">Amount</th>
	  		<th>Sample/stock</th>
		</tr>
        <c:forEach items="${inputsamples}" var="row" varStatus="rowStatus">
		<tr>
		    <th title="<c:out value="${row.inputSampleDescription}" />"><c:out value="${row.inputSampleName }" /></th>
			<td style="width:10em">
				<span class="viewonly">
					${row.amount}
				</span>
				<span class="editonly"> 
					<pimsForm:doAmount hook="${row.inputSampleHook}" propertyName="amount" value="${row.amount}" />
				</span>
			</td>
			<td>
				<span class="viewonly">
					<c:choose><c:when test="${!empty row.sampleName}">
						<a href="${pageContext.request.contextPath}/View/${row.sampleHook}">${fn:escapeXml(row.sampleName)}</a><!--<span style="color:red;font-weight:bold"> Should be context-menu link</span>-->
					</c:when><c:otherwise>
						(None)
					</c:otherwise></c:choose>
				</span>
				<span class="editonly">

           <select name="${row.inputSampleHook}:sample" id="${row.inputSampleHook}:sample"
            onchange="handleSampleSearchOnclick(this,'${inputSampleHook}','${row.sampleCategoryName}')">
		             <c:if test="${null == sample.hook}">
		               <option value="" selected="selected">${none}</option>
		             </c:if>
		 
		             <c:forEach items="${ row.samplesByUser }" var="entry">
				       <optgroup label="Assigned to: <c:out value='${ entry.key }' />">
					     <c:forEach items="${ entry.value }" var="sample">
						   <c:choose><c:when test="${sample.hook == row.sampleHook}">
		                 <option value="${sample.hook}" selected="selected"><c:out value="${sample.name}"  /></option>
		               </c:when><c:otherwise>
		                 <option value="${sample.hook}"
								 onmouseover="experimentGroup='${utils:escapeJS(experimentGroup.name)}';
		
											  thisSample={name:'<c:out value="${utils:escapeJS(sample.name)}"  />',
											              details:'<c:out value="${sample.details}" />',
											              column:'${sample.colPosition}',
											              row:'${sample.rowPosition}',
											              subposition:'${sample.subPosition}',
											              amount:'${sample.currentAmount}',
											              units:'${sample.amountDisplayUnit}',
											              holder:'<c:out value="${sample.holderName}" />'};
		
											  showSampleToolTip(event, thisSample);"
		
								 onmouseout="hideSampleToolTip();">
		                 <c:out value="${sample.name}" />
		                 </option>
		               </c:otherwise></c:choose>
					     </c:forEach>
			 	       </optgroup>
			         </c:forEach>
			     	 <optgroup label="Search">
						<option value="[SEARCH]">Search Samples</option>
					 </optgroup>
		           </select>

				</span>
			</td>
		</tr>
		</c:forEach>
		<tr>
			<td colspan="3" style="padding-right:1em">
				<pimsForm:editSubmit/>
			</td>
		</tr>
	</table>
	</pimsForm:form>
</c:otherwise>
</c:choose>
<!--  /inputsamples.jsp -->
