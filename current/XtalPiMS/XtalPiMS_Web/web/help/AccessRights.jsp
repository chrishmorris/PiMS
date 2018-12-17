<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@page import="org.pimslims.model.core.LabNotebook"%><jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Access rights management' />
</jsp:include>
<div class="extraswrapper">
<p>
Administrators can define who can view and edit the items of laboratory information
in the PiMS database.<br />
You should have the access rights required for your normal work.<br />


  <div class="facilities" id="currentfacilities">
    <ul class="facilitieslist">
  	 <li><h3>Administrators can: </h3>
		<ul>
       		<li><a href="${pageContext.request.contextPath}/Create/org.pimslims.model.accessControl.User">Create new User</a></li>    
            <%-- TODO change class name to lab note book --%>		
    		<li><a href="${pageContext.request.contextPath}/Create/<%= LabNotebook.class.getName() %>">Create new Lab Notebook</a></li>
    		<li><a href="${pageContext.request.contextPath}/access/Permission">Edit Permissions</a></li>
<!-- 		<li>Add a user to a group</li>
   			<li>Remove a user from a group</li>
-->
			<li>&nbsp;</li>
    	</ul>
    </li>
    <li><h3>All Users can:</h3>
		 <ul>
		  	<li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.User">View the Users</a></li>
  			<li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.UserGroup">View the User groups</a></li>
  			<%-- TODO change class name to lab note book --%>
  			<li><a href="${pageContext.request.contextPath}/Search/<%=org.pimslims.model.core.LabNotebook.class.getName() %>">View the Lab Notebooks</a></li>
  			<li><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.accessControl.Permission">View the Permissions to access data</a></li>
			<li>&nbsp;</li>
		 </ul>
	</li>    
   </ul>
  </div>
  <div class="facilities" id="futurefacilities">
    <ul class="facilitieslist">
     <li><h3>Notes to administrators:</h3></li>
	 <li>         
    	<h4>Create new user</h4> adds a record to PIMS's database describing the user.
    	In later versions of PIMS you will also be able you to specify the password.<br />
    	At present you must add the username and password to the 
    	file: web-users.xml<br />in: /Apache Software Foundation/Tomcat 6.0/conf.
    </li>
    <li>
		 <h4>Create new Lab Note Book</h4> allows you have the option to create a 
		 new Lab Notebook, and a new User group, representing 
		 the members of the collaboration.
    </li>
    </ul>
  </div>
</div>

<jsp:include page="/JSP/core/Footer.jsp" />