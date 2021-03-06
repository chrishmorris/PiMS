<%@ page contentType="text/html; charset=utf-8" language="java"  %>
<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.sequence.*"  %>
<%@ page import="org.pimslims.model.experiment.*"  %>
<%@ page import="org.pimslims.presentation.mru.MRUController"%>
<%@ page import="org.pimslims.presentation.mru.MRURoleChoice"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<c:catch var="error">
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />
<jsp:useBean id="experimentMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="experimentBeans" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" />


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Construct: ${constructBean.name}" />
    
</jsp:include>
<%--SpotConstructDetail.jsp --%>
<c:set var="title">Construct: <c:out value="${constructBean.name}"/></c:set>
<c:set var="breadcrumbs"><a 
  href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :
  <c:forEach var="target" items="${constructBean.targetBeans}" >
    <pimsWidget:link bean="${target}" />
    <c:choose>
        <c:when test = "${!empty constructBean.sgSampleHook && !empty constructBean.sgSampleName}">
        : 
            <pimsWidget:linkWithIcon 
                icon="types/small/sample.gif" 
                url="${pageContext.request.contextPath}/View/${constructBean.sgSampleHook}" 
                text="${constructBean.sgSampleName}" />
        
        </c:when>
        <c:otherwise>
        </c:otherwise>
    </c:choose>
  </c:forEach>  
</c:set>

<c:if test="${fn:length(experimentBeans) le 1}"><c:set var="tabindex" value='tabindex="1"' /></c:if>
<c:set var="actions">

    <a ${tabindex} href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${constructBean.hook}">New Experiment</a>

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
<%--  
	<pimsWidget:linkWithIcon 
        		icon="actions/create/experiment.gif" 
        		url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${constructBean.hook}" 
        		text="New experiment"/>
--%>
<a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=<c:out value="${constructBean.name}" />"> Milestones</a>

<a href="${pageContext.request.contextPath}/update/CreateMutatedObjective/<c:out value="${constructBean.hook}" />">New SDM Primers</a>
</c:set>


<pimsWidget:pageTitle icon="construct.png" title="${title}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>
   
<pimsWidget:box title="Basic Details" initialState="open" >
  <pimsForm:form id="tabsForm" action="/Update" mode="view" method="post">
  <pimsForm:formBlock id="blk1" >
  	<pimsForm:column1>  	
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${constructBean.hook}:pageNumber" value="${constructBean.values['pageNumber']}"
    />
	  	<pimsForm:text  name="${constructBean.hook}:commonName" alias="Construct Name" helpText="The Construct ID" value="${constructBean.name}" />
	  	<pimsForm:textarea  name="${constructBean.hook}:functionDescription" helpText="A brief description e.g. N-term His Tag" alias="Description"><c:out value="${constructBean.description}" /></pimsForm:textarea>
	</pimsForm:column1>
	<pimsForm:column2>
  		<pimsForm:mruSelect hook="${constructBean.hook}" 
      		rolename="owner" alias="Scientist" helpText="The person who designed this construct" required="${false}" />
  		<pimsForm:textarea name="${constructBean.hook}:details" helpText="Any additional details about the Construct" alias="Comments"><c:out value="${constructBean.comments}" /></pimsForm:textarea>
  		
  		<c:if test="${null ne constructBean.vectorInputSampleHook}">  		    
  		  <pimsForm:mruSelect hook="${constructBean.vectorInputSampleHook}" 
            rolename="sample" alias="Vector" helpText="The vector stock used to make this construct" required="${false}" />   
        </c:if>

  		Lab Notebook: ${owner}
  	</pimsForm:column2>
  </pimsForm:formBlock>
  <pimsForm:formBlock id="blk2" >
	<pimsForm:column2>
  		<pimsForm:editSubmit />
  	</pimsForm:column2>
  </pimsForm:formBlock>
  </pimsForm:form>
</pimsWidget:box>


<c:if  test="{empty constructBean.primers}">
        No primers recorded
</c:if>

<c:forEach var="primer" items="${constructBean.primers}" >
    <c:if test="${null ne primer.sequence}">
	<pimsWidget:primer bean="${primer}" sdm="${constructBean.sdmConstruct}" />
	</c:if>
</c:forEach>
	

<pimsWidget:box title="Insert" initialState="closed" id="product" >    
    <pimsForm:form action="/Update"  method="post" mode="view"  boxToOpen="product" >
    <pimsForm:formBlock id="blk3">
      <c:choose>
       <c:when test="${empty constructBean.pcrProductSeqHook }">
       Insert not recorded
       </c:when>
       <c:otherwise>
       
        <pimsForm:dna name="${constructBean.pcrProductSeqHook }" alias="Insert sequence"  helpText="The DNA sequence of the PCR Product" value="${constructBean.pcrProductSeq}" />
            
       </c:otherwise>
      <%--<c:if test="${empty constructBean.pcrProductSeq }">PCR product details not recorded</c:if>--%>
      </c:choose>

    </pimsForm:formBlock>
        <pimsForm:editSubmit />
    </pimsForm:form>
</pimsWidget:box>


<%--Protein details -not for DNA Construct --%>
<c:if test="${empty constructBean.dnaTarget }">
  <c:set var="targProtLength" >
		<c:choose>
		 <c:when test="${fn:endsWith(constructBean.targetProtSeq,'*')}">
	   		${fn:length(constructBean.targetProtSeq)-1}
		 </c:when>
		 <c:otherwise>
		 	${fn:length(constructBean.targetProtSeq)}
		 </c:otherwise>
		</c:choose>
  </c:set>
		
  <%-- TODO support polycistronic constructs by putting this in a loop --%>
  <pimsWidget:box title="Proteins" initialState="closed" id="protein" > 
    
    <pimsForm:form action="#"  method="get" mode="view"  boxToOpen="protein" >    
    <pimsForm:formBlock >  
    <div class="formitem viewonly" >
      <strong>Target protein length:</strong>
       <c:choose>
        <c:when test="${targProtLength == '0'}">
         <c:out value="No Protein sequence recorded for this Target" />
        </c:when>
        <c:otherwise> <c:out value="${targProtLength}  "/>
        </c:otherwise>
       </c:choose>
    </div><div class="formitem viewonly" >
      <strong title="Residues translated from the Target DNA sequence">Construct Start residue:</strong> <c:out value="${constructBean.targetProtStart} " />
      &nbsp; <strong>Construct End Residue:</strong> <c:out value="${constructBean.targetProtEnd}" />
    </div>
    
    <pimsForm:protein name="_nonesuch" alias="Translated sequence"  helpText="Translated from the Target DNA sequence" value="${constructBean.protSeq}" />
	
    <hr />
    </pimsForm:formBlock>
    </pimsForm:form>

    <pimsForm:form action="/Update"  method="post" mode="view"  boxToOpen="protein" >
    <pimsForm:formBlock ><c:if test="${null ne constructBean.expressedProteinHook}">
    
      
    <pimsForm:protein name="${constructBean.expressedProteinHook}" alias="Expressed protein"  helpText="The sequence expressed by this construct" value="${constructBean.expressedProt}" />
    
    
  </c:if>
        
        <c:if test="${'' ne constructBean.finalProt}"><hr />
        
        <pimsForm:protein name="${constructBean.finalProteinHook}" alias="Final protein"  helpText="Intended sequence e.g. after cleavage" value="${constructBean.finalProt}" />
    
      </c:if>
    </pimsForm:formBlock>
        <pimsForm:editSubmit />
    </pimsForm:form>
  </pimsWidget:box>			
</c:if>

<%
    request.setAttribute("metaClass", experimentMetaClass);
    request.setAttribute("beans", experimentBeans);
 %>
 
<c:if test="${fn:length(experimentBeans) le 1}"><c:set var="tabindex" value='tabindex="1"' /></c:if>
<c:set var="newExperiment">
    <a ${tabindex} href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${constructBean.hook}">New Experiment</a>
</c:set>
<pimsWidget:box id="experiments" title="Experiments" extraHeader="${newExperiment}" initialState="open" >
<c:choose>
	    <c:when test="${empty experimentBeans}">
	       There are no experiments for this Construct <br/>
	    </c:when>
	    <c:otherwise> 
	        <jsp:include page="/JSP/list/org.pimslims.model.experiment.Experiment.jsp"></jsp:include>
	    </c:otherwise>
	</c:choose>
</pimsWidget:box>


<c:choose>
    <c:when test="${!empty crystalTrialExps}">
        <pimsWidget:box id="crystalexperiments" title="Crystal Trial Experiments" initialState="closed" >
            <table><%-- TODO<table class="list"> --%>
            <tr>
            <th align="left">Name</th>
            <th align="left">Experiment Type</th>
            <th align="left">Status</th>
            <th align="left">Date</th>
            </tr>
            <c:forEach items="${crystalTrialExps}" var="plate">
            <tr>
                <td><pimsWidget:link bean="${plate}" /></td>
                <td><c:out value="${plate.expTypeName}" /></td>
                <td><c:out value="${plate.status}" /></td>
                <td><c:out value="${plate.startDateString}" /></td>
            </tr>
            </c:forEach>
            </table>
        </pimsWidget:box>
        </c:when>
        <c:otherwise>
        </c:otherwise>
</c:choose>


<pimsWidget:multiRoleBox title="Workflows" 
    objectHook="${constructBean.hook}" roleName="workflows"/> 

<pimsWidget:files bean="${constructBean}"  />

<pimsWidget:notes bean="${constructBean}"  />

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%-- The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> --%>

<jsp:include page="/JSP/core/Footer.jsp" />
