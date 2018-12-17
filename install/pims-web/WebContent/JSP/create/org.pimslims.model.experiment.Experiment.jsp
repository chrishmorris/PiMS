<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<c:catch var="error">
<%--Caller must provide this --%>
<jsp:useBean id="metaclass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<%-- These could be null therefore not suitable as bean
 <jsp:useBean id="reqAttr" scope="request" type="java.util.ArrayList" /> <!-- form elements as AttributeToHTML instances -->
 <jsp:useBean id="errorMessages" scope="request" type="java.util.Map" /> <!-- Error messages -->
--%>
<jsp:useBean id="javascript" scope="request" type="java.lang.String" />
<jsp:useBean id="headerTitle" scope="request" type="java.lang.String" />
<jsp:useBean id="pathInfo" scope="request" type="java.lang.String" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${headerTitle}' />
</jsp:include>

<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment">Experiments</a>
    <c:if test="${!empty experimentType}">
      : <pimsWidget:link bean="${experimentType }" />
    </c:if>
    <c:if test="${!empty project}">
      : <pimsWidget:link bean="${project }" />
    </c:if>
    <c:if test="${!empty sample}">
      : <pimsWidget:link bean="${sample }" />
    </c:if>
</c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Record a new Experiment"/>
<c:set var="actions">
    <pimsWidget:linkWithIcon name=""
                icon="actions/create/plate.gif" 
                url="${pageContext.request.contextPath}/CreatePlate"
                text="New plate experiment"/>
    <pimsWidget:linkWithIcon name=""
                icon="actions/create/experimentgroup.gif" 
                url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.ExperimentGroup"
                text="New experiment group"/>               
    <pimsWidget:linkWithIcon  name=""
                icon="types/small/experiment.gif" 
                url="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment" 
                text="Search single experiments"/>
</c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>
   

<jsp:scriptlet>
  String style = "even";
  pageContext.setAttribute("style", style);
</jsp:scriptlet>



<%-- There may be an error for the attribute does not displayed on the page show it here  --%>
<c:if test="${! empty errorMessages['missedErrorFields']}">
    <div class="formrow" style="text-align:left">
        <span style="color:red;font-weight:bold">
		There has been the following errors: 
		<c:forEach items="${errorMessages['missedErrorFields']}" var="fieldsNames">
			<br />The ${fieldsNames} - ${errorMessages[fieldsNames]}	
		</c:forEach>
        </span>
    </div>
</c:if>



<pimsWidget:box title="Choose one of these options:" initialState="fixed" >


<c:if test="${!empty instruments}">  
<pimsForm:form  action="/Create/${pathInfo}" mode="edit" method="get">
  <pimsForm:formBlock>   
  <h2>Choose Instrument:</h2>
  <pimsForm:select alias="Instrument" name="instrument">
          <option value="" ></option>
          <c:forEach var="type" items="${instruments}" >
            <c:choose>
            <c:when test="${type.hook == param['instrument']}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
            </c:choose>
          </c:forEach>
  </pimsForm:select>
  <input type="hidden" name="protocol" value="${param['protocol']}" />
  <input type="hidden" name="researchObjective" value="${param['researchObjective']}" />
  <input type="hidden" name="_Sample" value="${param['_Sample']}" />
  <input class="submit button"  type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn(); "/>
</pimsForm:formBlock>
</pimsForm:form>
</c:if>

<%-- <hr /> --%>

<pimsForm:form  action="/Create/${pathInfo}" mode="edit" method="get">
  <pimsForm:formBlock>   
  <h2>Choose Experiment Type:</h2>
  <pimsForm:select alias="Experiment type" name="experimentType">
          <option value="" ></option>
          <c:forEach var="type" items="${experimentTypes}" >
            <c:choose>
            <c:when test="${type.hook == experimentType.hook}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
            </c:choose>
          </c:forEach>
  </pimsForm:select>
  <input type="hidden" name="protocol" value="${param['protocol']}" />
  <input type="hidden" name="researchObjective" value="${param['researchObjective']}" />
  <input type="hidden" name="_Sample" value="${param['_Sample']}" />
  <input class="submit button"  type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn(); "/>
</pimsForm:formBlock>
</pimsForm:form>

<hr />


<pimsForm:form  action="/Create/${pathInfo}" mode="edit" method="post">
<pimsForm:formBlock>
<h2>Record the new experiment:</h2>

  
<c:set var="searchMore">
    top.document.location='${pageContext.request["contextPath"]}/ChooseForCreate/org.pimslims.model.experiment.Experiment/_Sample';
</c:set>
<pimsForm:select alias="Sample" 
  name="_Sample"
  onchange="if ('[SearchMore]'==this.value) { ${searchMore} }"
  >
      <option value="" >[none]</option>
      
      <c:forEach var="type" items="${samples}" >   
        <c:choose>
            <c:when test="${type.hook == param['_Sample']}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
        </c:choose>
      </c:forEach>  
      <%-- TODO needs a fix <option value="[SearchMore]" >Search more...</option> --%>
</pimsForm:select>


<c:set var="searchMore">
    top.document.location='${pageContext.request["contextPath"]}/ChooseForCreate/org.pimslims.model.experiment.Experiment/researchObjective';
</c:set>
<pimsForm:select alias="Project" 
  name="org.pimslims.model.experiment.Experiment:researchObjective"
  onchange="if ('[SearchMore]'==this.value) { ${searchMore} }"
  >
      <option value="" >[none]</option>
      
      <c:forEach var="type" items="${projects}" >   
        <c:choose>
            <c:when test="${type.hook == project.hook}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
        </c:choose>
      </c:forEach>  
      <%-- TODO needs a fix <option value="[SearchMore]" >Search more...</option> --%>
</pimsForm:select>
  

<c:if test="${!empty instruments}">   
  <pimsForm:select alias="Instrument" 
  name="org.pimslims.model.experiment.Experiment:instrument"  
  >
      <option value="" >[none]</option>
      <c:forEach var="type" items="${instruments}" >  
        <c:choose>
            <c:when test="${type.hook == param['instrument']}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
        </c:choose>
      </c:forEach>   
      
  </pimsForm:select>
</c:if>

<c:set var="searchMore">
    top.document.location='${pageContext.request["contextPath"]}/ChooseForCreate/org.pimslims.model.experiment.Experiment/protocol';
</c:set>
<pimsForm:select alias="Protocol" name="org.pimslims.model.experiment.Experiment:protocol"
    validation="required:true" onchange="if ('[SearchMore]'==this.value) { ${searchMore} }"
>
      <option value="" >Choose Protocol:</option>
      <c:if test="${!empty protocols}">           
          <c:forEach var="type" items="${protocols}" >            
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>            
          </c:forEach>           
      </c:if>
      <c:if test="${!empty recentProtocols}">
          <optGroup label="Recent Protocols"> 
          <c:forEach var="type" items="${recentProtocols}" >
            <c:choose>
            <c:when test="${type.hook == param['protocol']}">
                <option value="${type.hook}" selected="selected"><c:out value="${type.name}" /></option>
            </c:when>
            <c:otherwise>
                <option value="${type.hook}" ><c:out value="${type.name}" /></option>
            </c:otherwise>
            </c:choose>
          </c:forEach>
          </optGroup>
      </c:if>
      <option value="[SearchMore]" >Search more...</option>
</pimsForm:select>
        

<div class="formitem ">
            <div class="fieldname">
        <label class="label" for="name">
            <span>Name</span>
            <span class="required">*</span></label>
      </div>
      <div class="formfield">
        <input onchange='onEdit()' name="org.pimslims.model.experiment.Experiment:name" 
        id="org.pimslims.model.experiment.Experiment:name" 
        value="${suggestedName}" type="text" class="text" maxlength="80" /></div>
</div>


<pimsForm:date alias="Start date" name="org.pimslims.model.experiment.Experiment:startDate" />         
<pimsForm:date alias="End date" name="org.pimslims.model.experiment.Experiment:endDate" />     

<pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" 
    objects="${accessObjects}"  currentValue="${notebookHook}" />

<input class="submit button"  type="submit" value="Create" onclick="dontWarn(); "/>
</pimsForm:formBlock></pimsForm:form></pimsWidget:box>



<!-- form javascript -->
<script type="text/javascript">
<!--
className = 'Experiment';
function onLoadPims() {
attachValidation("org.pimslims.model.experiment.Experiment:startDate",{required:true,alias:'startDate'});
attachValidation("org.pimslims.model.experiment.Experiment:name",{required:true,alias:'name'});
attachValidation("org.pimslims.model.experiment.Experiment:endDate",{required:true,alias:'endDate'});

} 
// -->
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
