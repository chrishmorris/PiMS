<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectBean" %>
<%@attribute name="rolename" required="true"  %>
<%@attribute name="onchange" required="false"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="current" value="${bean.values[rolename]}" />
<select  name="${bean.hook}:${rolename}"
        id="${bean.hook}:${rolename}"
        onclick="if ('[NONE]'==this.value) {
         this.selectedIndex=0;
        top.document.location='../EditRole/${bean.hook}/${rolename}';
        }" ><%--  TODO make this work with keyboard - try onkeyup --%>
        <c:if test="${!empty current}"> 
            <option value="${current.hook}" selected="selected" onmouseup="${onchange}"><c:out value="${current.name}"/></option>
        </c:if>
        <optgroup label="Recently Used" class="mru-recent">
            <c:forEach var="entry" items="${bean.mru[rolename]}">
                <option value="${entry.key}" onmouseup="${onchange}"><c:out value="${entry.value}" /></option>
            </c:forEach>
        </optgroup>
        <optgroup label="Others" class="mru-others">
                <option value="[NONE]">Search More...</option>
        </optgroup>
</select>
