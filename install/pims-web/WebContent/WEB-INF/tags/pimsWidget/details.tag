<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectBean"  %>
<%@attribute name="initialState" required="false"  %>
<%@attribute name="title" required="false"  %><%-- default is the alias for the class --%>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<!-- details.tag -->
<c:if test="${empty title}"><c:set var="title" value="${bean.classDisplayName}" /></c:if>
<c:set var="fulltitle">
    <span class="linkwithicon" style="float:right; padding:0; padding-right: 1em; white-space: nowrap;"><%-- TODO remove this span --%>
      <a onclick='return warnChange()' href="${pageContext.request['contextPath']}/Create/${bean.className}">Record New</a>
      <a onclick='return warnChange()' href="${pageContext.request['contextPath']}/Search/${bean.className}">Search</a>
    </span>
	${title}:
	<pimsWidget:link bean="${bean}" />
</c:set>
<pimsWidget:box title="${fulltitle}" initialState="${initialState}" >
    <pimsForm:form action="/Update" mode="view" method="post">
    <pimsForm:formBlock>
    
    <c:if test="${null!=bean.metaClass.attributes['pageNumber']}">
       <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book"
       name="${bean.hook}:pageNumber" value="${bean.values['pageNumber']}"
       />
    </c:if>

    
         <%-- first the required attributes --%> 
         <c:forEach var="element" items="${bean.elements}">
            <c:choose><c:when test="${('lastEditedDate' eq element.name)||('creationDate' eq element.name)}">
              <%-- handled below --%>
            </c:when><c:otherwise>
              <c:if test="${element.required}">
                <c:choose><c:when test="${'name' eq element.name}">
                  <%-- Special handling for name - we know it's a text field and must be unique --%>            
                  <pimsForm:text validation="required:true, unique:{obj:'${bean.className}', prop:'name'}"
                                alias="${utils:deCamelCase(element.alias)}" helpText="${element.helpText}" 
                                 name="${bean.hook}:${element.name}" value="${bean.values[element.name]}"
                  />
                
                </c:when><c:otherwise>
                  <pimsForm:formItem name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" validation="required:true">
                    <pimsForm:formItemLabel name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" helpText="${element.helpText}" validation="required:true" />
                    <div class="formfield">
                      <pims:input attribute="${element}" value="${bean.values[element.name]}" name="${bean.hook}:${element.name}" />
                    </div>
                  </pimsForm:formItem>
                </c:otherwise></c:choose>
              </c:if>
            </c:otherwise></c:choose>
          </c:forEach>
    
          <%-- now the single roles --%>      
          <c:forEach var="entry" items="${bean.metaClass.metaRoles}"><c:if test="${entry.value.high==1}">
          <pimsForm:mruSelect hook="${bean.hook}" rolename="${entry.value.name}" alias="${utils:deCamelCase(entry.value.alias)}" helpText="${entry.value.helpText}" required="${entry.value.low>0}" />          
          </c:if></c:forEach>
    
          <%-- now the attributes that are not required --%>
          <c:forEach var="element" items="${bean.elements}">
            <c:choose><c:when test="${('lastEditedDate' eq element.name)||('creationDate' eq element.name)||('pageNumber' eq element.name)}">
              <%-- handled elsewhere --%>
            </c:when><c:otherwise>
              <c:if test="${!element.required}">
			  <pimsForm:formItem name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:${element.name}" alias="${utils:deCamelCase(element.alias)}" helpText="${element.helpText}" validation="${validation}" />
			    <div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values[element.name]}" name="${bean.hook}:${element.name}" />
			    </div>
			  </pimsForm:formItem>
			  </c:if>
			</c:otherwise></c:choose>
          </c:forEach>
          
          <%-- now the dates of editing --%>
          <pimsForm:formItem alias="Recorded" name="${bean.hook}:creationDate" >
                <pimsForm:formItemLabel name="${bean.hook}:creationDate" alias="Recorded" helpText="When this record was first made"  />
                <div class="formfield">
                  <pimsWidget:dateLink date="${bean.values['creationDate']}" />
                </div>
          </pimsForm:formItem>
          <pimsForm:formItem alias="Last Edited" name="${bean.hook}:lastEditedDate" >
                <pimsForm:formItemLabel name="${bean.hook}:creationDate" alias="Last Edited" helpText="When this record was last updated"  />
                <div class="formfield">
                  <pimsWidget:dateLink date="${bean.values['lastEditedDate']}" />
                </div>
          </pimsForm:formItem>
    </pimsForm:formBlock>
    <pimsForm:editSubmit />
    </pimsForm:form>    
</pimsWidget:box>