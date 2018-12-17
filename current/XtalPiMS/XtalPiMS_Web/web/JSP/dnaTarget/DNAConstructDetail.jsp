<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%-- 
Author: susy
Date: 06-Mar-2008
Servlets: /servlet/spot/SpotConstructDetail.java

-->
<%-- bean declarations --%>
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />


<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Construct: ${constructBean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- DNAConstructDetail.jsp -->

<c:set var="title">Construct: <c:out value="${constructBean.name}"/></c:set>
<c:set var="breadcrumbs"><a 
  href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :
  <c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>  
</c:set>
<c:set var="actions">
<pimsWidget:diagramLink hook="${constructBean.hook}" />
<%-- custom deleteLink --%>
<c:choose><c:when test="${constructBean.mayDelete}">
    <span class="linkwithicon " title="Delete construct "><a  href="${pageContext.request.contextPath}/Delete/${constructBean.hook}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request.contextPath}/Delete/${constructBean.hook}">Delete</a></span>
</c:when><c:otherwise>
    <pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" />
</c:otherwise></c:choose>

    <pimsWidget:linkWithIcon 
                icon="actions/create/experiment.gif" 
                url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${constructBean.hook}" 
                text="New experiment"/>

<a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=<c:out value="${constructBean.name}" />">All Experiments</a>
<a href="${pageContext.request.contextPath}/update/CreateMutatedObjective/<c:out value="${constructBean.hook}" />">New SDM Primers</a>
</c:set>
<pimsWidget:pageTitle icon="construct.png" title="${title}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>


<pimsWidget:box title="Basic Details" initialState="open" >
  <pimsForm:form id="tabsForm" action="/Update" mode="view" method="post">
  <pimsForm:formBlock id="blk1" >
    <pimsForm:column1>      
        <pimsForm:text  name="${constructBean.hook}:commonName" alias="Construct Name" helpText="The Construct ID" value="${constructBean.name}" />
        <pimsForm:textarea  name="${constructBean.hook}:functionDescription" helpText="A brief description e.g. N-term His Tag" alias="Description"><c:out value="${constructBean.description}" /></pimsForm:textarea>
    </pimsForm:column1>
    <pimsForm:column2>
        <pimsForm:mruSelect hook="${constructBean.hook}" 
            rolename="owner" alias="Scientist" helpText="The person who designed this construct" required="${false}" />
        <pimsForm:textarea name="${constructBean.hook}:details" helpText="Any additional details about the Construct" alias="Comments"><c:out value="${constructBean.comments}" /></pimsForm:textarea>
        <c:if test="${!mayUpdate}">Owner: ${owner}</c:if>
    </pimsForm:column2>
  </pimsForm:formBlock>
  <pimsForm:formBlock id="blk2" >
    <pimsForm:column2>
        <pimsForm:editSubmit />
    </pimsForm:column2>
  </pimsForm:formBlock>
  </pimsForm:form>
</pimsWidget:box>



<c:forEach var="primer" items="${constructBean.primers}" >
    <pimsWidget:primer bean="${primer}" />
</c:forEach>    

	
<pimsWidget:box title="Predicted PCR Product" initialState="closed" id="product" >    
    <pimsForm:form action="/Update"  method="post" mode="view"  boxToOpen="product" >
    <pimsForm:formBlock id="blk3">
      <c:if test="${empty constructBean.pcrProductSeq }">PCR product details not recorded</c:if>
      <pimsForm:textarea  name="${constructBean.pcrProductSeqHook}" alias="Sequence" helpText="The DNA sequence of the predicted PCR product" validation="dnaSequence:true/*, custom:function(val,alias){return checkTargetDNASeq(val,alias)}*/" onchange="this.value=cleanSequence2(this.value);">
        <pims:sequence sequence="${constructBean.pcrProductSeq}" format='DEFAULT' escapeStyle="NONE" />
      </pimsForm:textarea>

      <div class="formitem viewonly" style="padding-left:12.5em">
        <span align='right' title="Length (bp) of the Target DNA sequence" ><strong> Length: </strong></span><c:out value="${constructBean.pcrProductSize}"/>
        <span align='right' title="%GC nucleotides in the Target DNA sequence" ><strong> &nbsp;&#037;GC: </strong></span><fmt:formatNumber type="Number" maxFractionDigits="2" >
        <c:out value="${constructBean.pcrProductGC}"/></fmt:formatNumber>&nbsp;
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${constructBean.pcrProductSeqHook}" />
            &nbsp; <a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    
       </div>
    </pimsForm:formBlock>
        <pimsForm:editSubmit />
    </pimsForm:form>
</pimsWidget:box>	


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if> 

<%-- The following table is for debugging purposes.  Please comment it out for normal use 
<br /><jsp:include page="/ConstructBeanDebug" />
 --%>   
<jsp:include page="/JSP/core/Footer.jsp" />
