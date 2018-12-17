<%@ page contentType="text/html; charset=utf-8" language="java" 
  import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*,org.pimslims.presentation.protocol.*,org.pimslims.model.protocol.*"  
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>

<c:catch var="error">
<jsp:useBean id="experimentTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" /> 


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Create a new protocol' />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.protocol.Protocol">Protocols</a></c:set>
<c:set var="icon" value="protocol.png" />        
<c:set var="title" value="Record a new protocol"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<pimsWidget:box id="box1" title="Choose one of these options:" initialState="open" >
	<pimsForm:form method="post" action="/Create/org.pimslims.model.protocol.Protocol" mode="create">
		<pimsForm:formBlock>
			<h2>Specify basic details, then edit the protocol:</h2>
		</pimsForm:formBlock>
		<pimsForm:formBlock>
		<pimsForm:column1>
		<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.protocol.Protocol"/>
		<input type="hidden" name="org.pimslims.model.protocol.Protocol:isForUse" value="true"/>
		<pimsForm:text alias="Name" name="org.pimslims.model.protocol.Protocol:name" value=""  validation="required:true, unique:{obj:'org.pimslims.model.protocol.Protocol',prop:'name'}"/><%--maxlength="80"--%>

		<pimsForm:select alias="Experiment type" name="org.pimslims.model.protocol.Protocol:experimentType">
	      <c:forEach var="type" items="${experimentTypes}" >
	        <c:choose>
	        <c:when test="${type.hook == param['experimentType']}">
				<option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
			</c:when>
			<c:otherwise>
				<option value="${type.hook}" ><c:out value="${type.name}" /></option>
			</c:otherwise>
	        </c:choose>
	      </c:forEach>
		</pimsForm:select>
		
         <pimsForm:select name="org.pimslims.model.protocol.Protocol:instrumentType" alias="Instrument type">
                    <pimsForm:option optionValue=""  alias="" currentValue="" />
                    <c:forEach items="${instrumentTypes}" var="type">
                      <pimsForm:option optionValue="${type.hook}" currentValue="" alias="${type.name}" />
                    </c:forEach>
          </pimsForm:select>
		
		<pimsForm:labNotebookField name="_OWNER" helpText="The lab notebook this protocol belongs to" objects="${accessObjects}" />
		
		<input type="submit" value="Next >>>" onclick="dontWarn()" /> 
		</pimsForm:column1>
		</pimsForm:formBlock>
		
	</pimsForm:form>
<hr />
	<pimsForm:form method="post"  enctype="multipart/form-data" action="/Create/org.pimslims.model.protocol.Protocol" mode="create">
		<pimsForm:formBlock>
			<h2>Upload a PiMS protocol file:</h2>
		</pimsForm:formBlock>
		<pimsForm:formBlock>
			<pimsForm:column1>
			<pimsForm:nonFormFieldInfo label="File">
			    <input type="file" name="file" id="file" />
			</pimsForm:nonFormFieldInfo>
			
			<pimsForm:labNotebookField name="_OWNER" helpText="The lab notebook this protocol belongs to" objects="${accessObjects}" />
			
    		<input type="submit" value="Upload file" onclick="dontWarn()" />
    		</pimsForm:column1>
		</pimsForm:formBlock>
	</pimsForm:form>
<hr />
	<pimsForm:form method="get" mode="create" action="/Search/org.pimslims.model.protocol.Protocol">
		<pimsForm:formBlock>
			<h2>Copy an existing PiMS protocol and modify it:</h2>
		</pimsForm:formBlock>
		<pimsForm:formBlock>
		<pimsForm:column1>
			<pimsForm:text alias="Search for protocols" name="search_all" value="(Enter a name or part name of a protocol)" onclick="this.value=''"/>
			<input type="submit" value="Search"  onclick="if($('search_all').value=='(Enter a name or part name of a protocol)'){$('search_all').value=''};dontWarn()"/>
		</pimsForm:column1>
		</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>


<!-- javascript -->
<script type="text/javascript">
className = 'Reference input sample';
  attachValidation("org.pimslims.model.protocol.Protocol:name",{required:true,alias:"Name"});
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
