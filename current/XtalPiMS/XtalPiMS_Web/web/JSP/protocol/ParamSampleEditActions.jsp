<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="hook" value="${param['hook']}" />
<c:set var="mayUpdate" value="${param['mayUpdate']}" />
<c:set var="disabled" value=""/>
<c:if test="${!mayUpdate}"><c:set var="disabled" value="onfocus=\"this.blur()\""/></c:if>

<span class="protocol_editicons noprint" id="${hook}_editicons">
	<c:choose><c:when test="${mayUpdate}">
    	<a href="${pageContext.request.contextPath}/Delete/${hook}">
     	<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
          alt="Delete" title="Delete"
          id="${hook}_deleteicon" class="protocol_deleteicon"
          onclick="ajax_delete(this, {cleanup:function(){protocol_checkNumOutputs()} } );return false;"
      	/>
      	</a>
	</c:when><c:otherwise>
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" 
          alt="Can't delete" title="Can't delete"
          id="${hook}_deleteicon" class="protocol_deleteicon"
		/>
    </c:otherwise></c:choose>
 </span>
 

<!-- OLD -->
