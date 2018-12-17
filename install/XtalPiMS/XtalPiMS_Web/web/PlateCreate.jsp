<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>


<%
//----------------------------------------------------------------------------------------------
//			new_sample.jsp												
//
//
//		 	Created by Marc Savitsky,OPPF-Oxford  				25 May 2007
//----------------------------------------------------------------------------------------------
%>


<jsp:useBean id="holderTypes" scope="request" type="Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="screens" scope="request" type="Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="allUsers" scope="request" type="Collection<org.pimslims.presentation.ModelObjectShortBean>" />
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

<a href="${pageContext.request['contextPath']}/Create/org.pimslims.model.sample.Sample?refSample=Purified Protein">New Sample</a><br /><br />

<pimsWidget:box title="New Trial Plate details" initialState="fixed">
 <pimsForm:form method="post" action="/CreateTrialPlate" mode="create" id="new_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="barcode" alias="Barcode" helpText="Barcode or name of the plate" value="" validation="required:true" />
       
       <%-- protein sample --%>
       <div class="formitem "><div class="fieldname" >
       <label for="sample" title="Purified protein sample to use">Sample</label></div><div class="formfield" >            
       <select name="sample" id="sample"
            onchange="handleSampleSearchOnclick(this,'${inputSampleHook}','Purified protein')">
         <option value="" selected="selected">[NONE]</option>
                     
         <%-- TODO show samples from MRU, something like this:
                     <c:forEach items="${ row.samplesByUser }" var="entry">
                       <optgroup label="Assigned to: <c:out value='${ entry.key }' />">
                         <c:forEach items="${ entry.value }" var="sample">
                           <c:choose><c:when test="${sample.hook == row.sampleHook}">
                         <option value="${sample.hook}" selected="selected"><c:out value="${sample.name}"  /></option>
                       </c:when><c:otherwise>
                         <option value="${sample.hook}"
                                 onmouseover="experimentGroup='';
        
                                              thisSample={name:'<c:out value="${utils:escapeJS(sample.name)}"  />',
                                                          details:'<c:out value="${sample.details}" />',
                                                          componentsnumber:'${sample.sampleComponentsLength}',
                                                          components:'<c:out value="${sample.sampleComponentsString}" />',
                                                          hazardsnumber:'${sample.hazardPhasesLength}',
                                                          hazards:'<c:out value="${sample.hazardPhasesString}" />',
                                                          column:'${sample.colPosition}',
                                                          row:'${sample.rowPosition}',
                                                          subposition:'${sample.subPosition}',
                                                          amount:'${sample.currentAmount}',
                                                          units:'${sample.amountDisplayUnit}',
                                                          specification:'${sample.refSample}',
                                                          holder:'<c:out value="${sample.holderName}" />'};
        
                                              showSampleToolTip(event, thisSample);"
        
                                 onmouseout="hideSampleToolTip();">
                         <c:out value="${sample.name}" />
                         </option>
                       </c:otherwise></c:choose>
                         </c:forEach>
                       </optgroup>
                     </c:forEach> --%>
         <optgroup label="Search">
           <option value="[SEARCH]">Search Samples</option>
         </optgroup>
       </select>
       </div></div>           
       
       <pimsForm:select name="holderTypeId" alias="Holder Type" validation="required:true">
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
     <pimsForm:select name="ownerId" alias="Lab Notebook" validation="required:true">
        <c:forEach var="p" items="${allUsers}">
         <pimsForm:option optionValue="${p.hook}" currentValue="${currentUser.hook}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
        <pimsForm:select name="operatorId" alias="Operator" validation="required:true">
          <c:forEach var="p" items="${allUsers}">
            <pimsForm:option optionValue="${p.hook}" currentValue="${currentUser.hook}" alias="${p.name}" />
          </c:forEach>
        </pimsForm:select> 
        <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook this plate will belong to" objects="${labNotebooks}" />
     <pimsForm:textarea name="description" alias="Description">[none]</pimsForm:textarea> 
     </pimsForm:column2>
     
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
