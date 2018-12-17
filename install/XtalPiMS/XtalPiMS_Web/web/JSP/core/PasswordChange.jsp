<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Change Password for PiMS" />
</jsp:include>
<p class="error">${message }</p>
<c:set var="user" value="${username}"/>
<c:if test="${!empty param['username']}"><c:set var="user" value="${param['username']}"/></c:if>

<div style="margin:10em 33%">
	<pimsWidget:box initialState="fixed" title="Change password for ${user} " extraClasses="noscroll">
	    <div style="">
	    	<div style="text-align:center">
	    		<img src="${pageContext.request.contextPath}/skins/academic/login_logo.png"  alt="" style="padding-top:.75em"/>
	    	</div>
		<pimsForm:form action="/update/PasswordChange" method="post" mode="edit">
		<input type="hidden" name="username" value="${user}" />
		<pimsForm:formBlock>
		  <c:if test="${!isLeader }">
            <pimsForm:password name="oldPassword" alias="Old Password" validation="required:true"/>
          </c:if>
          <pimsForm:password name="newPassword" alias="New Password" validation="required:true"/>
          <pimsForm:password name="confirm" alias="Confirm New Password" validation="sameAs:'newPassword'"/>
		  <pimsForm:nonFormFieldInfo>
		     <input style="float:right;" type="submit" value="Save" id="submitbutton" onclick="dontWarn()" />
		  </pimsForm:nonFormFieldInfo>
		</pimsForm:formBlock>
		</pimsForm:form> 
		</div>
	</pimsWidget:box>
</div>



<jsp:include page="/JSP/core/Footer.jsp" />
