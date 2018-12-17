<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,java.sql.*"  %> 
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.model.people.*"  %>
<%@ page import="org.pimslims.model.target.*"  %>
<%@ page import="org.pimslims.lab.sequence.*"  %>
<%@ pagebuffer='128kb' %>

<jsp:useBean id="constructBean" scope="request" type="ConstructBean" />

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS New SDM construct: Step 1' />
</jsp:include>
<!-- CreateMutatedObjective.jsp -->

<h2><a href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :  
<c:forEach var="target" items="${constructBean.targetBeans}" >
    <a href='${pageContext.request.contextPath}/View/${target.hook}'>Target <c:out 
    value="${target.name}" /></a> 
  </c:forEach>
 : 
<!--Add Construct Step ${param['wizard_step']}-->New SDM Construct: Basic details</h2>

<pimsWidget:box id="box2" title="Basic Details" initialState="open" >
 <pimsForm:form action="/update/CreateMutatedObjective" id="frmStep1" method="post" mode="create" >
  <pimsForm:formBlock id="blk1">
   <pimsForm:column1><div class="formitem ">
        <div class="fieldname" >
        <label for="construct_id" title="Construct ID must be unique for this Target">Construct id</label>
        <span class="required">*</span></div><div class="formfield" >
            <input  type="text" name="construct_id" id="construct_id"  value="${constructBean.name}" onchange="onEdit();" maxlength="60" />
        </div>
        <script type="text/javascript">
           attachValidation("construct_id", {required:true, custom:function(val,alias){return idValue(val,alias)}, alias:"Construct id"} );
        </script>
        </div>
   </pimsForm:column1>
   <pimsForm:column2>
        Desired Tm <input name="desired_tm" value="78" />
        
Expression organism        
<select name="expressionOrganism">
  <c:forEach items="${expressionOrganisms}" var="organism">
    <c:choose><c:when test="${ organism eq expressionOrganism }">
      <option value="${organism}" selected="selected" >${organism}</option>
    </c:when><c:otherwise>
      <option value="${organism}">${organism}</option>
    </c:otherwise></c:choose>
  </c:forEach>
</select>

   </pimsForm:column2> 
   <c:forEach var="target" items="${constructBean.targetBeans}" >
  	<input type="hidden"  name="pims_researchobjective_hook" value="<c:out value='${constructBean.hook}' />"/>
	<input type="hidden"  id="construct_prot_seq" name="construct_prot_seq" value="<c:out value='${constructBean.protSeq}' />"/>
    <input type="hidden"  name="construct_dna_seq" value="<c:out value='${constructBean.dnaSeq}' />"/>
    <input type="hidden"  name="wizard_step" value="1"/>
  </c:forEach>
   
  </pimsForm:formBlock>
  
  
  <div style="text-align:right">
   	<input name="Submit" style="text-align:right" type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn()" />
    &nbsp;
  </div>
  
  
 </pimsForm:form>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%--  
The following table is for debugging purposes.  Please comment it out for normal use 
<jsp:include page="/ConstructBeanDebug" /> 
--%>

<!-- /CreateMutatedObjective.jsp -->
<jsp:include page="/JSP/core/Footer.jsp" />


