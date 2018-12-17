<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:useBean id="notebooks" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="groups" scope="request" type="java.util.List<org.pimslims.presentation.UserGroupBean>" />
<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="My user groups" />
</jsp:include>

<style type="text/css">
div.userInGroup { float:left; width:30%; position:relative; top:0;  }
table.permissions tr { border-top:1px solid #999; }
table.permissions td { position:relative;top:0; }
table.permissions td input { position:relative;top:0; height:100%;width:100%; }
</style>

<c:forEach items="${groups}" var="group">

	<%-- I can administer the group membership if:
		- I am group head
		- I am an administrator
	Otherwise I cannot - including resigning from the group. --%>
	<c:set var="canAdministerMembership" value="${group.head==username || isAdmin}"/>

	<c:set var="title"><c:out value="${group.name}"/></c:set>
	<c:set var="extraClasses">usergroup</c:set>	
	<c:if test="${canAdministerMembership}">
		<c:set var="extraClasses">usergroup canAdmin</c:set>
	</c:if>
	<c:choose><c:when test="${empty group.head}">
		<c:set var="title">${title}&nbsp;<span class="grouphead">(No group head defined)</span></c:set>
	</c:when><c:when test="${group.head==username}">
		<c:set var="title">${title}&nbsp;-&nbsp;<span class="grouphead">You are head of this group</span></c:set>
	</c:when><c:otherwise>
		<c:set var="title">${title}&nbsp;-&nbsp;<span class="grouphead">Group head: ${group.head.name}</span></c:set>
	</c:otherwise></c:choose>
	<pimsWidget:box id="${group.hook}" initialState="closed" title="${title}" extraClasses="${extraClasses}">
	
	<%-- List all group members --%>
	<c:choose><c:when test="${empty group.members}">
		<p>(No members)</p>
	</c:when><c:otherwise>
		<c:forEach var="member" items="${group.members}">
			<c:set var="contextMenu">properties: [
				<c:if test="${isAdmin}">{ property:'Username', val:'${member.username}' }</c:if>
			], actions:[
				{ icon:'types/small/blank.gif', url:'/View/${member.hook}', text:'View' }
				 <%-- { icon:'types/small/blank.gif', url:'/', text:'Alert', onclick:'alert(\'alerted\')' } --%>
			]</c:set>
			<c:choose><c:when test="${!empty member.givenName && !empty member.familyName}">
				<c:set var="memberName"><c:out value="${member.givenName} ${member.familyName}"/></c:set>
			</c:when><c:otherwise>
				<c:set var="memberName"><c:out value="${member.familyName}"/></c:set>
			</c:otherwise></c:choose>
			<div class="userInGroup"><pimsWidget:linkWithIcon 
				icon="types/small/person.gif" 
				url="${pageContext.request.contextPath}/View/${member.hook}" 
				text="${memberName}"
				contextMenu="${contextMenu}"/></div>
		</c:forEach>
	</c:otherwise></c:choose>
	
	<%-- "Add member" form  --%>
	<c:if test="${canAdministerMembership}">
		<div style="clear:both">Add a member to this group: [form here]</div>
	</c:if>
	
	<%-- Group's permissions --%>
	<c:if test="${!empty notebooks}">
	<table class="permissions">
		<tr>
			<th>Projects</th>
			<th>Create</th>
			<th>Read</th>
			<th>Update</th>
			<th>Delete</th>
			<th>Unlock</th>
			<%-- TODO group's admin permissions
			<th>Administer</th>
			--%>
		</tr>
		<c:forEach var="notebook" items="${notebooks}">	
			<%-- I can administer the group's permissions on a Lab Notebook if
				- I am the LN owner
				- I am an administrator.
			Otherwise I cannot. --%>
			<c:set var="groupCanCreate">${group.permissions[notebook.name].create}</c:set>
			<c:set var="groupCanRead">${group.permissions[notebook.name].read}</c:set>
			<c:set var="groupCanUpdate">${group.permissions[notebook.name].update}</c:set>
			<c:set var="groupCanDelete">${group.permissions[notebook.name].delete}</c:set>
			<c:set var="groupCanUnlock">${group.permissions[notebook.name].unlock}</c:set>
			<%-- TODO group's admin permissions
			<c:set var="groupCanAdmin">${group.permissions[notebook.name].admin}</c:set>
			--%>
			<c:set var="userCanAdmin" value="${isAdmin}"/><%--TODO or LN owner, or in a group with admin permissions on LN --%>
			<c:set var="disabledAttribute"></c:set>
			<c:if test="${!userCanAdmin}"><c:set var="disabledAttribute">disabled="disabled"</c:set></c:if>
			<tr>
				<th><c:out value="${notebook.name}"/></th>
				<%-- Create --%>
				<c:set var="checkedAttribute"></c:set>
				<c:if test="${groupCanCreate}"><c:set var="checkedAttribute">checked="checked"</c:set></c:if>
				<td><input type="checkbox" name="${group.name}:create:<c:out value="${notebook.name}"/>" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> create records in the <c:out value="${notebook.name}"/> project?" /></td>
				<%-- Read --%>
				<c:set var="checkedAttribute"></c:set>
				<c:if test="${canRead}"><c:set var="checkedAttribute">checked="checked"</c:set></c:if>
				<td><input type="checkbox" name="<c:out value="${group.name}" />:read:<c:out value="${notebook.name}"/>" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> read records in the <c:out value="${notebook.name}"/> project?" /></td>
				<%-- Update --%>
				<c:set var="checkedAttribute"></c:set>
				<c:if test="${canUpdate}"><c:set var="checkedAttribute">checked="checked"</c:set></c:if>
				<td><input type="checkbox" name="<c:out value="${group.name}" />:update:<c:out value="${notebook.name}"/>" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> update records in the <c:out value="${notebook.name}"/> project?" /></td>
				<%-- Delete --%>
				<c:set var="checkedAttribute"></c:set>
				<c:if test="${canDelete}"><c:set var="checkedAttribute">checked="checked"</c:set></c:if>
				<td><input type="checkbox" name="<c:out value="${group.name}" />:delete:<c:out value="${notebook.name}"/>" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> delete records in the <c:out value="${notebook.name}"/> project?" /></td>
				<%-- Unlock --%>
				<c:set var="checkedAttribute"></c:set>
				<c:if test="${canUnlock}"><c:set var="checkedAttribute">checked="checked"</c:set></c:if>
				<td><input type="checkbox" name="<c:out value="${group.name}" />:unlock:<c:out value="${notebook.name}"/>" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> unlock records in the <c:out value="${notebook.name}"/> project?" /></td>
				<%-- TODO group's admin permissions
				<td><input type="checkbox" name="${group.name}:admin:${notebook.name}" ${disabledAttribute} ${checkedAttribute} 
					title="Can members of <c:out value="${group.name}" /> administer permissions on the <c:out value="${notebook.name}"/> project?" /></td>
				--%>
			</tr>
		</c:forEach>
	</table>
	</c:if>
	
	</pimsWidget:box>
</c:forEach>

<script type="text/javascript">
var username="${username}";
var GroupManagement={
	managementURL:contextPath+"/GroupManagement",
	permissionsURL:contextPath+"/UserGroupPermissions",
	init:function(){
		$$(".canAdmin").each(function(groupBox){
			//add admin's context menu entries to users
			
		});
		$$("table.permissions input[type='checkbox']").each(function(cb){
			//attach AJAX event to all - disabled by JSP if can't use.
			Event.observe(cb,"change",GroupManagement.setPermission);
		});
	}, 
	setPermission:function(evt){
		var elem=evt.findElement();
		if(!elem.name){ return false; }
		var permissionName=elem.name;
		var toEnable=elem.checked ? "enable" : "disable";
		showUpdatingModalDialog();
		new Ajax.Request(GroupManagement.permissionsURL, {
			method:"post",
			parameters:{
				setPermissions:"setPermissions",
				permissionName:toEnable
			},
			onSuccess:GroupManagement.setPermission_onSuccess,
			onFailure:GroupManagement.setPermission_onFailure
		});
		/*
		Response format:
			{
				timestamp: 1234567890123, //System.currentTimeMillis()
				changedPermissions:[
					{
						name:"",
						isEnabled:true|false
					},
					{ ... }, { ... } //Array allows same JSON for multiple permission changes, if needed later
				]
			}
		
			or 
			
			{
				error:"Plain-text error (no HTML) suitable for alert()ing"
			}
		*/
		
	},
	setPermission_onSuccess:function(transport){
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			GroupManagement.setPermission_onFailure(transport);
			closeModalDialog();
			return false;
		}
		var serverTime=transport.responseJSON.timestamp; //System.currentTimeMillis()
		var changedPermissions=transport.responseJSON.changedPermissions;
		if(!changedPermissions || !serverTime){
			closeModalDialog();
			return false;
		}
		changedPermissions.each(function(perm){
			var permissionName=perm.name;
			var isEnabled=perm.isEnabled;
			var permBox=$(permissionName);
			if(!permBox.lastChanged || permBox.lastChanged < serverTime){
				permBox.lastChanged=serverTime;
				permBox.checked=isEnabled;
			}
		});
		closeModalDialog();
	},
	setPermission_onFailure:function(transport){
		closeModalDialog();
		if(!transport.responseJSON){
			ajax_onFailure(transport);
			return false;
		}
		if(transport.responseJSON.error){
			alert(transport.responseJSON.error);
		}
	},

	addUserToGroup:function(evt){
		var elem=evt.findElement();
		//
		showUpdatingModalDialog();
		new Ajax.Request(GroupManagement.permissionsURL, {
			method:"post",
			parameters:{
				user:user,
				group:group
			},
			onSuccess:GroupManagement.addUserToGroup_onSuccess,
			onFailure:GroupManagement.addUserToGroup_onFailure
		});
		/*
		Response format:
			{
				timestamp: 1234567890123, //System.currentTimeMillis()
				group: {
					name: "University of Oulu",
					hook: "hook.of.Group:4321"
				}
				addedUsersToGroup:[
					{ 
						name: "Bob Smith",
						hook: "hook.of.user:1234",
						html: "<span class=\"linkwithincon\"></span>"
					},
					{ ... }, { ... }
				]
			}
		
			or 
			
			{
				error:"Plain-text error (no HTML) suitable for alert()ing"
			}
		*/
		
	},
	addUserToGroup_onSuccess:function(transport){
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			GroupManagement.addUserToGroup_onFailure(transport);
			return false;
		}
		var serverTime=transport.responseJSON.timestamp; //System.currentTimeMillis()
		var changedPermissions=transport.responseJSON.changedPermissions;
		changedPermissions.each(function(perm){
			var permissionName=perm.name;
			var isEnabled=perm.isEnabled;
			var permBox=$(permissionName);
			if(!permBox.lastChanged || permBox.lastChanged < serverTime){
				permBox.lastChanged=serverTime;
				permBox.checked=isEnabled;
			}
		});
		
		closeModalDialog();
	},
	addUserToGroup_onFailure:function(transport){
		ajax_onFailure(transport);
	}

}
GroupManagement.init();
</script>

</c:catch>
<jsp:include page="/JSP/core/Footer.jsp" />