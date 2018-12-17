<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='Guide to using the PiMS Access Model' />
</jsp:include>

<div class="help">

<pimsWidget:box initialState="fixed" title="Overview">
<p style="padding-left:1.4em; ">The PiMS Access Model allows for users to view, create and/or amend just the objects in the 
PiMS database (e.g.targets or experiments) that are relevant to them.<br/>  The PiMS administrator can 
use the four functions in the Access Rights box in the User Functions menu to exercise this control.</p>

<br />
  <img class="imageNoFloat" src="../images/helpScreenshots/navPeopPlaces.png" alt="Navigate to Users" />
</pimsWidget:box> 
<pimsWidget:box initialState="fixed" title="Contents">
 <ul>
  <li><a href="#navDataOwners">Lab Notebooks</a></li>
  <li><a href="#navUsers">Users</a></li>
  <li><a href="#navGroups">User Groups</a></li>
  <li><a href="#navPermissions">Permissions</a></li>
 </ul>

 <a href="${pageContext.request.contextPath}/functions/Help.jsp" >Guide</a> to using PIMS.
</pimsWidget:box>
</div> <!--end div help-->

<div class="toplink"><a href="#">Back to top</a></div>


 <li><h3 id="navDataOwners">Lab Notebooks</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
<p>The pages that users create in PiMS are grouped into Lab Notebooks.  
These may be searched and 
created using the link in the Access Rights box.
All the pages in a Lab Notebook have the same access rights.
You will have a Lab Notebook for your group. 
The pages that describe your shared protocols, freezers, and instruments will all be in it. 
If your laboratory operates as a single group, collaborating closely together, 
then you can also use this shared Lab Notebook for your experiments and samples. 
Here are some reasons to create an extra Lab Notebook:<ul>
    <li>You are doing some work for an external customer, or with an external collaborator. 
    You want your partner to see this work, but not the other activities in the lab,
    so you use a Lab Notebook dedicated to this collaboration. </li>
    <li>One of the lab members is working as an independent researcher. 
    Her or is work in progress can be kept private in a separate Lab Notebook. </li>
</ul>
</p>  
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
 
<div class="toplink"><a href="#">Back to top</a></div>
 </li>  


 <li><h3 id="navUsers">Users</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
<p>PiMS Users may be searched, created by selecting Users in the Access Rights box.  To edit a user, first view 
the user from the search results.  Creating a PiMS user by this method is not sufficient for them to log into
PiMS, they must also be created as tomcat users with a password.  The default method of doing this is by 
editing the tomcat-users.xml file, but this may be different on your installation.  Please see your system
administrator for advice about this.</p>
<br />
  <img class="imageNoFloat" src="../images/helpScreenshots/editUser.jpg" alt="Navigate to Users" />
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
 
 
<div class="toplink"><a href="#">Back to top</a></div>
 </li> 
 <li><h3 id="navGroups">User Groups</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
<p>A User Group is set of users with common rights to some lab notebooks.
You will have a User Group with all your laboratory members in it,
which gives them all the right to see the pages describing your shared protocols and other shared resources.
When you make a new Lab Notebook, a new User Group is also created.
If you add someone to that new User Group, they will have the right to 
add pages, read pages, update pages, and delete pages.
</p>  
<p>Here are some examples of other User Group  that you may want to create:<ul>
  <li>Supervisors. You may want a User Group which you give permission to read all pages in all your Lab Notebooks.</li>
  <li>Client. You may want a User Group which has read permission for a specific Lab Notebook but no permission to make changes.</li>
</ul></p>
<br />
  <img class="imageNoFloat" src="../images/helpScreenshots/editUserGroup.jpg" alt="View Group record" />
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
 
<div class="toplink"><a href="#">Back to top</a></div>
 </li> 
 
 

 <li><h3 id="navPermissions">Permissions</h3>
 <div class="helpcontents">
 <div class="textNoFloat">
 <p>For each User Group and each Lab Notebook, there are five permissions you can grant or deny,
 allowing users in that group to create, read, update, and/or delete pages in that lab notebook, and to unlock past experiments.
 </p>
<p> Permissions 
are edited from the link in the Access Rights box.  A number of user groups may be selected by holding the
control key while selecting each group, and a number of lab notebooks may be selected by holding the control
key when clicking on a lab notebook.  In the example below, three user groups and two lab notebooks have 
been selected.  Access is then set up by selecting the appropriate boxes in the table.</p>  

<p>In the example, users in the user group Other Targets may only read objects that belong to the lab 
notebook Other Targets.  they may not create, amend or delete objects.  Users in the user group Other 
interesting group may read, amend or delete objects in the lab notebook Other interesting group but they
can not create new objects.  Users that belong to the user group my targets are allowed full access to both
objects in the lab notebook Other Targets and the lab notebook other interesting group.</p>  
<br />
  <img class="imageNoFloat" src="../images/helpScreenshots/editPermissions.jpg" alt="View Group record" />
 </div> <!--end div class="textNoFloat"-->
 </div> <!--end div class="helppage"-->
<div class="toplink"><a href="#">Back to top</a></div>
 </li>  
 
 
<jsp:include page="/JSP/core/Footer.jsp" />
