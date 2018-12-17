<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="isNext" required="false" type="java.lang.Boolean" %>

<c:if test="${mayUpdate ne false}" >
<div style="width:98%; padding:0 1%; " >
  <pimsForm:formItem extraClasses="toggleview noprint" alias="" name="">
	<div class="editlink">
	<span onclick="startEdit(this)"  
		
	><img src='${pageContext.request["contextPath"]}/skins/default/images/icons/actions/edit.gif' alt="" /> 
	<p ${isNext ? 'tabindex="1"' :'' }>Make changes...</p></span>
	</div>
	<div class="savebutton">
	<span class="canceledit" onclick="cancelEdit(this)">(Cancel editing)</span>
	<input type="submit" value="Save changes" onclick="dontWarn()" tabIndex="1" />
	</div>
  </pimsForm:formItem>
  <div style="clear:both; font-size:0pt; background-color:transparent; height:0px; ">&nbsp;</div>
</div>
</c:if>