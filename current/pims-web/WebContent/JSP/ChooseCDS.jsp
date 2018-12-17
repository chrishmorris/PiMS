<%--

Author: Petr Troshin
Date: December 2007
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<!-- All of the above are optional -->
<!-- jsp:useBean id="record" scope="request" type="java.lang.String" /-->
<!--  jsp:useBean id="database" scope="request" type="java.lang.String" /-->
<!-- jsp:useBean id="format" scope="request" type="java.lang.Boolean" /-->

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Choose CDS to make a target from" />
</jsp:include>


<pimsWidget:pageTitle
    icon="target.png"
    title="Choose CDS for the New Target:"/>
   
<pimsForm:form mode="edit" id="form" action="/update/TargetRecorder" method="post" >
<c:if test="${!empty cdslist}">
<pimsWidget:box title="Basic Details" initialState="open">
    <div style="text-align:center">
        <c:choose>
        <c:when test="${empty lookedUpSourceURL && type != 'both'}">
            <%--<p>Sorry, PiMS can't lookup a ${otherType} sequence automatically but you can add it later manually</p>
            IMPROVE ERROR--%>
            <p><span class="required">Sorry, PiMS can't find a ${otherType} sequence automatically at the moment.<br />
            If you Create a new Target now you can add a DNA sequence manually later</span></p>
        </c:when>
        <c:when test="${empty lookedUpSourceURL && type == 'both'}">
            <p>The uploaded record will be the source of DNA and protein sequences of the target</p>
        </c:when>
        <c:otherwise>
            <p>PiMS looked up the related <a target="_blank" href="${lookedUpSourceURL}">record</a> which will be used for the ${otherType} sequence of the target</p>
        </c:otherwise>
        </c:choose>
    </div>

    <input type="hidden" name="database" value="${database}" />
    <input type="hidden" name="dbid" value="${dbid}" />
    <input type="hidden" name="fileHook" value="${fileHook}" />

    <pimsForm:formBlock>
        <pimsForm:column1>
            <pimsForm:nonFormFieldInfo label="Database name">${database}</pimsForm:nonFormFieldInfo>
            <pimsForm:nonFormFieldInfo label="Database ID">${dbid}</pimsForm:nonFormFieldInfo>
        </pimsForm:column1>
        <pimsForm:column2>
            <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook for this target" objects="${accessObjects}" />
        </pimsForm:column2>
    </pimsForm:formBlock>    
    <pimsForm:formBlock>    
    </pimsForm:formBlock>    
</pimsWidget:box>

  <pimsWidget:box title="Possible Coding sequences (CDS) for the New Target" initialState="open" >
  

        <table class="list">
        <tr><th>CDS</th><th>Location</th><th>Annotation</th></tr>
            <c:forEach items="${cdslist}" var="item" varStatus="status" >
               <tr><td><input type="radio" name="cds" value="${item.name}"/></td><td>${item.location}</td>
         <td>
         <c:forEach items="${item.annotation}" var="an">
             <span style="font-family:'Courier New',courier,monospace; font-size-adjust: -2; background-color: ${color};" >${an}<br/></span>
         </c:forEach>
         </td>
        </tr>
            </c:forEach>
        <tr><td colspan="3">    
          <input id="submit" style="width:auto; float: right; position: relative; top: 0.2em; margin-bottom: 0.5em;"  type="submit" 
            name="Submit" value="Choose CDS"  />
        </td></tr>
        </table> 
  </pimsWidget:box>
</c:if>	

  <pimsWidget:box title="Record ${database}:${dbid}"> 
    <%-- Note that the textarea must not add white space to the record --%> 
    <textarea readonly="readonly" style="width:100%" onkeyup="matchTextareaHeightToContent(this)" onfocus="matchTextareaHeightToContent(this)"  name="record" id="record"
    ><c:out value="${record}" /></textarea>
  </pimsWidget:box>

    <input type="hidden" name="record" id="record" value="" /><!-- onsubmit, copy text of record into this. -->
</pimsForm:form>    

</c:catch><c:if test="${error != null}">"/><p class="error">error ${error}</p></c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

