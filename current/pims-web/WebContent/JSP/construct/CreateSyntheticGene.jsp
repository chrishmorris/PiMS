<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%-- 
Author: susy  Susy Griffiths YSBL
Date: 6 Dec 2010
Servlets: 

-->
<%-- bean declarations --%>
<jsp:useBean id="psgb" scope="request" type="org.pimslims.presentation.construct.SyntheticGeneBean" />
<jsp:useBean id="vectors" scope="request" type="java.util.Collection<org.pimslims.model.sample.Sample>" />
<jsp:useBean id="accessObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='New Synthetic gene' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
<c:set var="title">New Synthetic Gene</c:set>
<c:set var="breadcrumbs"><a 
  href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :
    <pimsWidget:link bean="${psgb.targetBean}" />
   </c:set>

<c:set var="actions" >
</c:set>

<pimsWidget:pageTitle icon="blank.png" title="${title}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>

<c:choose>
    <c:when test="${empty accessObjects}" >
        <h2><c:out value="No Lab Notebooks recorded in PiMS"></c:out></h2>
            <c:set var="formStyle" value="display: none" />
    </c:when>
    <c:otherwise>
        <pimsWidget:box title="New Synthetic gene Details" initialState="fixed">
         <pimsForm:form method="post" action="/update/CreateSyntheticGene" mode="create" id="new_synthetic_gene">
           <pimsForm:formBlock id="blk1">
             <pimsForm:column1>
               <input type="hidden" name="METACLASSNAME" value="org.pimslims.model.sample.Sample"/>       
               <pimsForm:text name="sgName" alias="Name" helpText="The name of the Synthetic gene" validation="required:true,unique:{ obj:'org.pimslims.model.sample.Sample',prop:'name'}" />
               <pimsForm:select name="vector" alias="Vector" helpText="The Synthetic gene vector">
                <option value="[none]"> </option>
                 <c:forEach var="vector" items="${vectors}">
                   <option value="${vector.hook}" ><c:out value="${vector.name}" /></option>
                 </c:forEach>
               </pimsForm:select>
               <%-- --%>
               <pimsForm:text name="fivePrimeSite" alias="5'-Restriction site" helpText="Restriction site at the 5'-end" validation="required:false" />
               <pimsForm:text name="threePrimeSite" alias="3'-Restriction site" helpText="Restriction site at the 3'-end" validation="required:false" />
               <%-- --%>
             </pimsForm:column1>
             <pimsForm:column2>
               <pimsForm:labNotebookField name="accessId" helpText="The Lab notebook this Synthetic gene belongs to" objects="${accessObjects}" />      
              <%--  <pimsForm:select name="personHook"  alias="Scientist" helpText="The scientist responsible for the Synthetic gene"  >
                <option value="${currentPerson.hook}" selected ><c:out value="${currentPerson.name}" /></option>
                  <c:forEach var="p" items="${people}"> 
                    <c:if test="${p.hook != currentPerson.hook}">
                      <option value="${p.hook}" ><c:out value="${p.name}" /></option>
                    </c:if>
                  </c:forEach>
               </pimsForm:select> --%>
               <%-- --%>
               <pimsForm:text name="vectorRes" alias="Vector resistances" helpText="The vector's antibiotic resistance markers" validation="required:false" />
               <pimsForm:text name="expressionHost" alias="Optimized for expression in:" helpText="Expression host the sequence has been optimized for" validation="required:false" />
               <%-- --%>
             </pimsForm:column2>
            </pimsForm:formBlock>
            <pimsForm:formBlock id="blk2">
              <pimsForm:textarea name="dnaSeq" alias="DNA sequence" helpText="The DNA sequence of the Synthetic gene" validation="dnaSequence:true, required:true" onchange="this.value=cleanSequence2(this.value);"/>
              <pimsForm:textarea name="protSeq" alias="Protein sequence" helpText="The protein sequence of the Synthetic gene" validation="proteinSequence:true" onchange="this.value=cleanSequence2(this.value);"/>
           </pimsForm:formBlock>
           <pimsForm:formBlock>
             <pimsForm:submitCreate onclick="dontWarn()" />
           </pimsForm:formBlock>
                <input type="hidden"  name="pims_target_hook" value="<c:out value='${psgb.targetBean.hook}' />"/>    
           </pimsForm:form>
        </pimsWidget:box>  
    </c:otherwise>
</c:choose>        

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
