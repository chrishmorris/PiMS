<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.model.target.*;import org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.presentation.ServletUtil" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ page import="org.pimslims.model.holder.*" %>

<c:catch var="error">

<%-- Mandatory parameters --%>
<jsp:useBean id="ObjName" scope="request" type="java.lang.String" />
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<pims:import className="<%= Holder.class.getName() %>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${ObjName}: ${bean.name}" />
</jsp:include>  

<!--view/org.pimslims.model.holder.Holder.jsp -->
<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/${bean.className}">${bean.classDisplayName}s</a>
</c:set>
<%-- TODO icon --%>        
<c:set var="actions">
    <pimsWidget:deleteLink bean="${bean}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${bean.name}" actions="${actions}" icon="${icon}" />

<c:if test="${empty title}"><c:set var="title" value="${bean.classDisplayName}" /></c:if>
<c:set var="fulltitle">
    <span class="linkwithicon" style="float:right; padding:0; padding-right: 1em; white-space: nowrap;">
      <a onclick='return warnChange()' href="${pageContext.request.contextPath}/Create/${bean.className}">Record New</a>
      <a onclick='return warnChange()' href="${pageContext.request.contextPath}/Search/${bean.className}">Search</a>
    </span>
	${title}:
	<pimsWidget:link bean="${bean}" />
</c:set>
<pimsWidget:box title="${fulltitle}" initialState="open" >
    <pimsForm:form id="tabsForm" action="/Update" mode="view" method="post">
    <pimsForm:formBlock>
    
       <pimsForm:column1>
       
         <pimsForm:text name="${bean.hook}:${Holder['PROP_NAME']}" 
						 	value="${fn:escapeXml(bean.name)}"
						 	helpText="The name of the holder or the code to identify it.  It is the unique identifier."
						   	alias="Name" validation="required:true" />
         
         <pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_SUPHOLDER']}"
						 	helpText="The holder to which the sub holders belong."
						   	alias="Sup holder" 
						   	required="false" />
						 
		<pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_FIRSTSAMPLE']}"
						 	helpText="The first sample in this holder."
						   	alias="First sample" 
						   	required="false" />
			
        <pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_LASTEDITOR']}"
						 	helpText="Person who last modified entry."
						   	alias="Last editor" 
						   	required="false" />

		<pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_HOLDERTYPE']}"
						 	helpText="The type of holder associated to a holder."
						   	alias="Holder type" 
						   	required="false" />
						   	
		<pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_ACCESS']}"
						 	helpText="N/A"
						   	alias="Lab Notebook" 
						   	required="false" />
						   	
		<pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_CREATOR']}"
						 	helpText="The user who created this entry."
						   	alias="Creator" 
						   	required="false" />
						   	
		<pimsForm:mruSelect hook="${bean.hook}" 
         					rolename="${Holder['PROP_LASTTASK']}"
						 	helpText="The latest task completed on this holder."
						   	alias="Last task" 
						   	required="false" />
	</pimsForm:column1>
	<pimsForm:column2>
						   	
		<pimsForm:formItem name="${bean.hook}:colPosition" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:colPosition" alias="Col position" helpText="The position of the current holder in the parent holder. The first position should be 1." validation="${validation}" />
				<div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values['colPosition']}" name="${bean.hook}:${Holder['PROP_COLPOSITION']}" />
			    </div>
		</pimsForm:formItem>
		
		<pimsForm:formItem name="${bean.hook}:rowPosition" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:rowPosition" alias="Row position" helpText="The position of the current holder in the parent holder. The first position should be 1." validation="${validation}" />
			    <div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values['rowPosition']}" name="${bean.hook}:${Holder['PROP_ROWPOSITION']}" />
			    </div>
		</pimsForm:formItem>
		
		<pimsForm:formItem name="${bean.hook}:subPosition" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:subPosition" alias="Sub position" helpText="The position of the current holder in the parent holder. The first position should be 1." validation="${validation}" />
			    <div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values['subPosition']}" name="${bean.hook}:${Holder['PROP_SUBPOSITION']}" />
			    </div>
		</pimsForm:formItem>
		
		<pimsForm:formItem name="${bean.hook}:crystalNumber" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:crystalNumber" alias="Crystal number" helpText="The number of samples marked as crystals." validation="${validation}" />
			    <div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values['crystalNumber']}" name="${bean.hook}:${Holder['PROP_CRYSTALNUMBER']}" />
			    </div>
		</pimsForm:formItem>
          
          <%-- now the dates of editing --%>
          
        <pimsForm:formItem name="${bean.hook}:startDate" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:startDate" alias="Start date" helpText="N/A" validation="${validation}" />
			    <div class="formfield">
			      <pimsWidget:dateLink date="${bean.values['startDate']}" />
			    </div>
		</pimsForm:formItem>
		
		<pimsForm:formItem name="${bean.hook}:endDate" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:endDate" alias="End date" helpText="N/A" validation="${validation}" />
			    <div class="formfield">
			      <pimsWidget:dateLink date="${bean.values['endDate']}" />
			    </div>
		</pimsForm:formItem>
		
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
          
        </pimsForm:column2>
      </pimsForm:formBlock>
      <pimsForm:formBlock>
      
      	<pimsForm:formItem name="${bean.hook}:details" alias="" validation="${validation}">
			    <pimsForm:formItemLabel name="${bean.hook}:details" alias="Details" helpText="Details field for comments." validation="${validation}" />
			    <div class="formfield">
			      <pims:input attribute="${element}" value="${bean.values[details]}" name="${bean.hook}:${Holder['PROP_DETAILS']}" />
			    </div>
		</pimsForm:formItem>
      
      </pimsForm:formBlock>
      
          <%-- TODO also iterate through single roles --%>
          <pimsForm:editSubmit />
    </pimsForm:form>    
</pimsWidget:box>

<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
</script>


<br /><br />


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>

<pimsWidget:files bean="${bean}"  />

<pimsWidget:notes bean="${bean}"  />

<!-- /ViewHolder.jsp -->

<jsp:include page="/JSP/core/Footer.jsp" />
