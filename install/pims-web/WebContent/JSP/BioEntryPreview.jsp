<%--

Author: Petr Troshin
Date: November 2007
--%>
<%@ page contentType="text/html; charset=utf-8"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<jsp:useBean id="record" scope="request" type="java.lang.String" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Preview target details ${database}:${dbid}" />
</jsp:include>


<pimsWidget:pageTitle
    icon="target.png"
    title="Preview new target details" />

<pimsWidget:box title="Basic Details" initialState="open">
    <div style="text-align:center">
	    <c:choose>
	    <c:when test="${empty lookedUpSourceURL && type != 'complete'}">
	        <p>Sorry, PiMS can't lookup a ${otherType} sequence automatically but you can add it later manually</p>	        
            
	    </c:when>
	    <c:when test="${empty lookedUpSourceURL && type == 'complete'}">
	        <p>The uploaded record will be the source of DNA and protein sequences of the target</p>
	    </c:when>
	    <c:otherwise>
	        <p>PiMS looked up the related <a target="_blank" href="${lookedUpSourceURL}">record</a> which will be used for the ${otherType} sequence of the target</p>
	    </c:otherwise>
	    </c:choose>
    </div>

    <pimsForm:form mode="edit" action="/update/TargetRecorder" method="post">
    <input type="hidden" name="database" value="${database}" />
    <input type="hidden" name="dbid" value="${dbid}" />
    <input type="hidden" name="fileHook" value="${fileHook}" />
    <input type="hidden" name="record" id="record" value="" /><!-- onsubmit, copy text of record into this. -->

    <pimsForm:formBlock>
        <pimsForm:column1>
            <pimsForm:nonFormFieldInfo label="Database name">${database}</pimsForm:nonFormFieldInfo>
            <pimsForm:nonFormFieldInfo label="Database ID">${dbid}</pimsForm:nonFormFieldInfo>
        </pimsForm:column1>
        <pimsForm:column2>
        	<pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook for this target" objects="${accessObjects}" />
        </pimsForm:column2>
    </pimsForm:formBlock>    
        <input id="submit" type="submit" name="Submit" value="Record target" onclick="$('record').value=$('dummyrecord').value" />
    <pimsForm:formBlock>    
    </pimsForm:formBlock>    
    </pimsForm:form>
</pimsWidget:box>
		
<pimsWidget:box title="Record ${database}:${dbid}">	
<%-- Note that the textarea must not add white space to the record --%>	
<textarea readonly="readonly" style="width:100%" onkeyup="matchTextareaHeightToContent(this)" onfocus="matchTextareaHeightToContent(this)"  name="dummyrecord" id="dummyrecord"
><c:out value="${record}" /></textarea>
</pimsWidget:box>


</c:catch><c:if test="${error != null}">"/><p class="error">error ${error}</p></c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

