<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new PiMS Lab Notebook' />
</jsp:include>

<pimsWidget:box title="Create Lab Notebook"  initialState="open" >

<p class="help"> 
Make a Lab Notebook. This is a group of pages in PiMS, shared by one or more PiMS users.
</p>

<pimsForm:form method="post" action="/Create/org.pimslims.model.core.LabNotebook" mode="edit" >
<pimsForm:formBlock>
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/Create/org.pimslims.model.core.LabNotebook')}" />
<div class="formrow" >
   <div class="fieldname"><label class="label" for="org.pimslims.model.core.LabNotebook:name"><span title="The name of the Lab Notebook">Name</span>
   <span class="required">*</span></label></div>
   <div class="formfield">
     <input onchange='onEdit()' name="org.pimslims.model.core.LabNotebook:name" id="org.pimslims.model.core.LabNotebook:name" value="" type="text" class="text" maxlength="80" />
   </div>
</div>

<pimsForm:formItem name="Leader" alias="Create a userid for the lab head?"  >
	<label for="Leader"><input type="checkbox"  name="Leader" id="Leader" checked="checked" /> Create a userid for the lab head?</label>
</pimsForm:formItem>

<pimsForm:password name="password" alias="Password" helpText="Login password for new group leader" />


<pimsForm:formItem name="UserGroup" alias="Create a userid for the lab head?"  >
	<label for="UserGroup"><input type="checkbox"  name="UserGroup" id="UserGroup" checked="checked" /> Create a user group for lab members?</label>
</pimsForm:formItem>

<pimsForm:text alias="Maximum Size"  name="maxSize" validation="required:false, wholeNumber:true, minimum:0" /> 
        
    
    <pimsForm:nonFormFieldInfo>
		     <input  type="submit" value="Save" id="submitbutton" onclick="dontWarn()" />
    </pimsForm:nonFormFieldInfo>
</pimsForm:formBlock>
</pimsForm:form>

</pimsWidget:box>


<!-- javascript -->
<script type="text/javascript">
<!--
className = 'Lab Notebook';
function onLoadPims() {
    attachValidation("org.pimslims.model.core.LabNotebook:name",{required:true,alias:'Name'}); 
} // -->
</script>

<jsp:include page="/JSP/core/Footer.jsp" />
