<%--
Display a form for searching for objects of a data model type.
This is used by list.jsp and others.
See
    org.pimslims.servlet.Search
for methods to create the beans it uses.

The form's action URL is taken from the request.
Note that this is changed by calling RequestDispatcher.forward().
The servlet should normally "include" a JSP, not forward to one.

Author: Chris Morris
Date: December 2005

Additions by Peter Troshin August 2006
Total records in the database 
number of records match the search criterea are added. 
@see Search servlet for more info. 

@requestParam searchMetaClass org.pimslims.metamodel.MetaClass

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<!-- SearchForm.jsp -->
<p style="color:black; padding:0; display:inline; font-weight: bold;">${totalRecords}&nbsp;${searchMetaClass.alias}(s)</p> recorded in the database. 
<c:if test="${! noSearch}"><span class="error">${resultSize} match search criteria.</span></c:if> <br />

<pims:import className="<%= org.pimslims.presentation.AttributeToHTML.class.getName() %>" />
<c:if test="${totalRecords > 0}">
  <pimsWidget:quickSearch value="${fn:escapeXml(param['search_all'])}" initialState="open" ><%--
    Pass on ACTION parameter for CustomCreate
    --%><input name="${AttributeToHTML['ACTION']}" value="${param.ACTION}" type="hidden" />

    <%-- for pop-up role handling --%>
    <c:if test="${!empty param.selectMultiple}">
      <input type="hidden" name="selectMultiple" value="${param.selectMultiple}"/>
    </c:if>
    <c:if test="${!empty param.isInModalWindow}">
      <input type="hidden" name="isInModalWindow" value="${param.isInModalWindow}"/>
    </c:if>
    <c:if test="${!empty param.isInPopup}">
      <input type="hidden" name="isInPopup" value="yes"/>
    </c:if>
    <c:if test="${!empty param.hook}">
      <input type="hidden" name="hook" value="${param.hook}"/>
    </c:if>
    <c:if test="${!empty param.sampleCategory}">
      <input type="hidden" name="sampleCategory" value="${param.sampleCategory}"/>
    </c:if>
    <c:if test="${!empty param.experimentGroup}">
      <input type="hidden" name="experimentGroup" value="${param.experimentGroup}"/>
    </c:if>
    <c:if test="${!empty param.callbackFunction}">
      <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
    </c:if>
    <c:if test="${!empty param['_metaClass']}">
      <input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
    </c:if>

  </pimsWidget:quickSearch>

<c:set var="initialState" value="closed"/>
<c:if test="${criteria['SUBMIT'] eq 'Search'}"><c:set var="initialState" value="open"/></c:if>
<pimsWidget:box title="Advanced Search" initialState="${initialState}" >
<form method="get" action="${requestScope['javax.servlet.forward.request_uri']}" class="searchform" >
<%--
Pass on ACTION parameter for CustomCreate
--%><input name="${AttributeToHTML['ACTION']}" value="${param.ACTION}" type="hidden" />

  <table style="border-collapse:collapse">
  <c:forEach items="${searchAttributes}" var="attribute">
	<c:if test="${!empty searchMetaClass.attributes[attribute]}">
      <c:if test="${!searchMetaClass.attributes[attribute].hidden}">
      <tr>
        <td>
        <label class="label" for="${searchMetaClass.attributes[attribute].name}">
            <%-- The next two fn:blocks are used to capitalise the first letter --%>
            <span style="color: black;">${fn:toUpperCase(fn:substring(searchMetaClass.attributes[attribute].alias,0,1))}${fn:substring(searchMetaClass.attributes[attribute].alias,1,-1)}</span>
            <c:if test="${searchMetaClass.attributes[attribute].required}"><span class="required">*</span></c:if>
        </label>
        </td>
        <td>
          
          <c:choose>
            <c:when test="${searchMetaClass.attributes[attribute].name == 'status'}">
              <pims:input attribute="${searchMetaClass.attributes[attribute]}" value="${criteria[attribute]}" /> 
            </c:when>
            <c:otherwise>
              <input name="${attribute}" value="<c:out value='${criteria[attribute]}' />" />
            </c:otherwise>
          </c:choose>
          
        </td>
      </tr>
      </c:if>
    </c:if>
  </c:forEach>

	<%-- for pop-up role handling --%>
    <c:if test="${!empty param.selectMultiple}">
      <input type="hidden" name="selectMultiple" value="${param.selectMultiple}"/>
    </c:if>    <c:if test="${!empty param.isInModalWindow}">
      <input type="hidden" name="isInModalWindow" value="${param.isInModalWindow}"/>
    </c:if>
	<c:if test="${!empty param.isInPopup}">
	  <input type="hidden" name="isInPopup" value="yes"/>
	</c:if>
	<c:if test="${!empty param.hook}">
	  <input type="hidden" name="hook" value="${param.hook}"/>
	</c:if>
	<c:if test="${!empty param.sampleCategory}">
	  <input type="hidden" name="sampleCategory" value="${param.sampleCategory}"/>
	</c:if>
	<c:if test="${!empty param.experimentGroup}">
	  <input type="hidden" name="experimentGroup" value="${param.experimentGroup}"/>
	</c:if>
	<c:if test="${!empty param.callbackFunction}">
	  <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
	</c:if>

	<c:if test="${totalRecords > 0}">
		<%-- Make button if there is something to send --%>
		<tr><td colspan="2"><input type="submit" name="SUBMIT" value="Search" onClick="dontWarn()"/></td></tr>
	</c:if>
</table>
</form>
</pimsWidget:box>
</c:if><%-- end if there are some records --%>
<!-- /SearchForm.jsp -->

