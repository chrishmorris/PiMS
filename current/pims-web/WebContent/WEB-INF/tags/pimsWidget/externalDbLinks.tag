<!-- externlDbLink.tag -->
<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectBean" %> 
<%@attribute name="dbnames" required="true" type="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>

<script type="text/javascript">
function checkForm(theForm){
	if (theForm.elements['code'].value == "accession number") {
		theForm.elements['code'].value = "";
	}
	if (theForm.elements['code'].value == "" && theForm.elements['url'].value == "") {
		theForm.elements['code'].value = "accession number";
		alert("Either an accession number or a URL is required to make a meaningful link.");
		return false;
	}
  return true;
}
</script>

<pims:import className="org.pimslims.model.core.ExternalDbLink" />

<pimsWidget:box title="External Database Links" id="<%= org.pimslims.servlet.ExternalDbLinks.EXTERNALDBLINKS %>" initialState="closed" >
<pimsForm:form action="/update/ExternalDbLinks" mode="edit" method="post">
<input type="hidden" name="modelobject" id="xxx"  value="${bean.hook}" />
<table>
<tr>
<th style="width:15em;">DataBase</th>
<th style="width:15em;">Id</th>
<th >URL</th>
<th >Details</th>
<th style="width:6em;">Delete</th>
</tr>
<c:forEach items="${bean.externalDbLinks}" var="dbref">
<tr id="${dbref.hook}" class="ajax_deletable"> 
  <td><c:out value="${dbref.name}" /></td>
  
  <td>
  <c:choose><c:when test="${!empty dbref.link}">
  	<a href="<c:out value="${dbref.link}" />" target="_blank"><c:out value="${dbref.accession}" /></a>
  </c:when><c:otherwise>
  	<c:out value="${dbref.accession}" />
  </c:otherwise>
  </c:choose>
  </td>
  
  <td>
  <c:choose><c:when test="${!empty dbref.url}">
  	<a href="<c:out value="${dbref.realUrl}" />" target="_blank"><c:out value="${dbref.url}" /></a>
  </c:when><c:otherwise>
  	<c:out value="${dbref.url}" />
  </c:otherwise>
  </c:choose>
  </td>
  
  <td>
   <c:set var="thedetails" value="${dbref.type}" />
   <c:choose>
    <c:when test="${fn:contains(thedetails,'top hit::')}" >
        <c:out value="${fn:substringBefore(dbref.type,'::')}" />
    </c:when>
    <c:otherwise>
        <c:out value="${dbref.type}" />
    </c:otherwise>
   </c:choose>
   </td>
  
  <td style="padding-left: 3.4em;">
             <img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" 
             	 id="${dbref.hook}_deleteicon" onclick="ajax_delete(this)" style="cursor:pointer"/>
	</td>
</tr>
</c:forEach>
 
<c:choose><c:when test="${bean.mayUpdate}">
  <tr>
    <td> 
    	<select style="width:15em;" onchange="onEdit();" name="${ExternalDbLink['PROP_DBNAME']}"
		id="${requestScope.MRURoleChoice.modelObject_hook}:dbrefDbname"
		onclick="if ('[NONE]'==this.value) {
		 this.selectedIndex=0;
		top.document.location='../ChooseForCreate/org.pimslims.model.core.ExternalDbLink/dbName?search_all=&amp;SUBMIT=Quick+Search&amp;ACTION=parentEntry%3D${bean.hook}';
		}" >
			<c:forEach var="n" items="${dbnames}"> 
          		<option value="${n.name}"><c:out value="${n.name}"/></option>
          	</c:forEach>
		</select>
    </td>
    <td>
    	<div style="margin-left:0;" class="formfield" >
    		<!-- 
			<input type="text" name="${ExternalDbLink['PROP_CODE']}" value="<c:out value='${dbref.accession}'/>" />
			-->
			<input type="text" name="${ExternalDbLink['PROP_CODE']}" value="accession number" />
		</div>
    </td>
    <td>
    	<div style="margin-left:0;" class="formfield" >
			<input type="text" name="${ExternalDbLink['PROP_URL']}" value="<c:out value='${dbref.url}'/>" />
		</div>
    </td>
    <td><textarea style="width:96%" type="text" name="${ExternalDbLink['PROP_DETAILS']}"  maxlength="253" value="" ></textarea></td>
    <td><input type="submit" value="Add" onclick="dontWarn(); return checkForm(this.form)" /></td>
  </tr>

</c:when><c:otherwise>
  <tr>
    <td colspan="4">You do not have access rights to add database links for <c:out value="${bean.name}" /></td>
  </tr>
</c:otherwise></c:choose>

</table>
</pimsForm:form>
<!-- /externlDbLink.tag -->
</pimsWidget:box> 