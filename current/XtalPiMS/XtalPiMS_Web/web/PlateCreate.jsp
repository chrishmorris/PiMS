<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>


<jsp:useBean id="holderTypes" scope="request" type="Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="screens" scope="request" type="Collection<org.pimslims.presentation.ModelObjectBean>" />
<%-- No, we use a hidden input for current user. Might need to do it this way for admin later.
<jsp:useBean id="allUsers" scope="request" type="Collection<org.pimslims.presentation.ModelObjectShortBean>" />
 --%>
 <jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new trial plate' />
</jsp:include>

<script>
function handleSampleSearchOnclick(sel,inputSampleHook,sampleCategoryName){
    if ('[NONE]'==sel.value) {
        sel.selectedIndex=0;
        top.document.location='../Search/org.pimslims.model.sample.RefSample?sampleCategory='+sampleCategoryName;
    } else if ('[SEARCH]'==sel.value) {
         sel.selectedIndex=0;
         showSampleSearch(sel,inputSampleHook, sampleCategoryName);
    }
}
function showSampleSearch(elem,sampleDefID,sampleCategory) {
    currentSampleSearchSelect=elem;
    //search.sampleDef=sampleDefID;
    mruToUpdate=elem;
    openModalWindow(contextPath+"/Search/org.pimslims.model.sample.Sample?isInModalWindow=yes&callbackFunction=chooseSample&sampleCategories="+sampleCategory,"Choose sample");
}
//called by popup iframe
function chooseSample(sampleObject) {
    sampleID  = (null!=sampleObject) ? "sample"+sampleObject.sampleID : "";
    if(null!=sampleObject) {
        sampleObject.sampleID=sampleObject.hook.split(":")[1];
    }

    var elem = currentSampleSearchSelect;
    var numOptions=elem.options.length;
    var found=false;
    for (var j=0;j<numOptions;j++) {
        if (elem.options[j].value == sampleObject.hook) {
            elem.options[j].selected = true;
            found=true;
        }
    }
    if(!found){
        elem.innerHTML+='<option value="'+sampleObject.hook+'">'+sampleObject.name+'</option>';
        elem.selectedIndex=numOptions;
    }

    closeModalWindow();
}
</script>

<%--
<a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.sample.Sample?refSample=Purified Protein">New Sample</a><br /><br />
--%>
 
<pimsWidget:box title="New Trial Plate details" initialState="fixed">
 <pimsForm:form method="post" action="/CreateTrialPlate" mode="create" id="new_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="barcode" alias="Barcode" helpText="Barcode or name of the plate" value="" validation="required:true" />

        <pimsForm:labNotebookField name="_OWNER" helpText="Lab Notebook, used to determine the access rights for the record you are about to create" 
    objects="${accessObjects}"  currentValue="${notebookHook}" validation="required:true"/>

              
       <pimsForm:select name="holderTypeId" alias="Plate Type" validation="required:true">
        <c:forEach var="p" items="${holderTypes}">
         <pimsForm:option optionValue="${p.hook}" currentValue="" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
       <pimsForm:select name="screenId" alias="Screen" validation="required:true">
         <c:forEach var="p" items="${screens}"> 
     <pimsForm:option optionValue="${p.hook}" currentValue="" alias="${p.name}" />
         </c:forEach>
      </pimsForm:select>       
       
     </pimsForm:column1>
     
     <pimsForm:column2>

       <%-- protein sample --%>
       <div class="formitem "><div class="fieldname" >
       <label for="sample" title="Purified protein sample to use">Sample</label></div><div class="formfield" >            
       <select name="sample" id="sample"
            onchange="handleSampleSearchOnclick(this,'${inputSampleHook}','Purified protein')">
         <option value="" selected="selected">[NONE]</option>
                     
         <optgroup label="Search">
           <option value="[SEARCH]">Search Samples</option>
         </optgroup>
       </select>
       </div></div>           


	<!-- Only allow create for current user -->
     <input type="hidden" name="ownerId" validation="required:true" value="${currentUser.hook}" />
     <input type="hidden" name="operatorId" validation="required:true" value="${currentUser.hook}" />
	<%-- Old way - allow create on behalf of everyone else! 
     <pimsForm:select name="ownerId" alias="Owner" validation="required:true">
        <c:forEach var="p" items="${allUsers}">
         <pimsForm:option optionValue="${p.hook}" currentValue="${currentUser.hook}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
        <pimsForm:select name="operatorId" alias="Operator" validation="required:true">
          <c:forEach var="p" items="${allUsers}">
            <pimsForm:option optionValue="${p.hook}" currentValue="${currentUser.hook}" alias="${p.name}" />
          </c:forEach>
        </pimsForm:select> 
	 --%>
     <pimsForm:textarea name="description" alias="Description">[none]</pimsForm:textarea> 
     </pimsForm:column2>
     
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
