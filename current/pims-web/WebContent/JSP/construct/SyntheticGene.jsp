<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<pims:import className="org.pimslims.model.sample.Sample" />
<pims:import className="org.pimslims.model.experiment.Experiment" />
<pims:import className="org.pimslims.model.molecule.Molecule" />

<%-- 
Author: susy
Date: 19 Nov 2010
Servlets: 

-->
<%-- bean declarations --%>
<jsp:useBean id="psgb" scope="request" type="org.pimslims.presentation.construct.SyntheticGeneBean" />
<%--<jsp:useBean id="targetBean" scope="request" type="org.pimslims.presentation.TargetBean" />
<jsp:useBean id="experiment" scope="request" type="org.pimslims.presentation.experiment.ExperimentBean" />
<jsp:useBean id="parameters" scope="request" type="java.util.List<org.pimslims.model.experiment.Parameter>" />
<jsp:useBean id="inputsamples" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.InputSampleBean>" />
--%><jsp:useBean id="gcCont" scope="request" type="java.lang.Float" />
<jsp:useBean id="protPI" scope="request" type="java.lang.Float" />
<jsp:useBean id="protEX" scope="request" type="java.lang.Float" />
<jsp:useBean id="protMass" scope="request" type="java.lang.Float" />
<jsp:useBean id="abs01pc" scope="request" type="java.lang.Float" />
<%--<jsp:useBean id ="sgMilestones" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructResultBean>" />--%>


<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS Synthetic Gene: ${psgb.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- PiMSSyntheticGene --%>
<c:set var="title">Synthetic Gene: <c:out value="${psgb.sgeneName}"/></c:set>
<c:set var="breadcrumbs"><a 
  href='${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target'>Targets</a> :
   <pimsWidget:link bean="${psgb.targetBean}" /></c:set>


<c:set var="actions">
   <c:choose>
        <c:when test="${empty psgb.dnaSeq}"><pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="The target must have a DNA sequence before you can record a construct"
                text="Can't add construct"/>
        </c:when>
        <c:when test="${(fn:length(psgb.dnaSeq)%3) !=0}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="The DNA sequence must be a multiple of 3 nucleotides long to record a construct"
                text="Can't add construct"/>
        </c:when>           
        <c:when test="${! mayUpdate}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="actions/create/construct_greyedout.gif" 
                url="#" 
                title="You do not have permission to record a construct"
                text="Can't add construct"/>
        </c:when>
        <%--TODO use Sample hook and modify CreateExpressionObjective --%>           
        <c:otherwise>
            <pimsWidget:linkWithIcon 
                icon="actions/create/construct.gif" 
                url="${pageContext.request.contextPath}/update/CreateExpressionObjective/${psgb.hook}" 
                text="New construct"/>
            <c:set var="newConstruct">
                <a href='${pageContext.request.contextPath}/update/CreateExpressionObjective/${psgb.hook}'>Design new Construct</a><br/>
            </c:set>
                
        </c:otherwise>
    </c:choose>
<%-- custom deleteLink --%>
    <c:choose>
        <c:when test="${psgb.mayDelete}">
            <span class="linkwithicon " title="Delete Synthetic gene "><a  href="${pageContext.request.contextPath}/Delete/${psgb.sgeneHook}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request.contextPath}/Delete/${psgb.sgeneHook}">Delete</a></span>
        </c:when>
        <c:otherwise>
            <pimsWidget:linkWithIcon 
            text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
            url="#" isGreyedOut="true"
            onclick="return false" />
        </c:otherwise>
    </c:choose>
</c:set>

<pimsWidget:pageTitle icon="blank.png" title="${title}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>
<pimsWidget:box id="details" title="Basic Details" initialState="open" >
    <pimsForm:form action="/Update" id="ID" method="post" mode="view" >
        <pimsForm:formBlock id="sgene1">
          <pimsForm:column1>
            <pimsForm:text validation="required:true" value="${psgb.sgeneName}"  name="${psgb.sgeneHook}:${Sample['PROP_NAME']}" alias="Name" helpText="The name of the Synthetic gene" />
          </pimsForm:column1>
          <pimsForm:column2>
                <pimsForm:nonFormFieldInfo label ="Lab Notebook" helpText="Lab Notebook to which the Synthetic gene belongs" >
                    <c:out value="${psgb.labNotebook}" />
                </pimsForm:nonFormFieldInfo>
                <pimsForm:writer  hook="${psgb.hook}" rolename="${Sample['PROP_CREATOR']}" required="false" alias="Scientist" helpText="The scientist responsible for the Synthetic gene" />
          </pimsForm:column2>
        </pimsForm:formBlock>
        <pimsForm:formBlock id="sgene11">
         <pimsForm:editSubmit />
        </pimsForm:formBlock>
     </pimsForm:form>
   <hr />
     <%--Input Sample is the Vector but this uses /experiment/UpdateInputSamples --%>
    <pimsForm:form mode="view" action="/experiment/UpdateInputSamples" method="post">
        <pimsForm:formBlock id="sgene2">
         <pimsForm:column1>
            <c:forEach items="${psgb.inputsamples}" var="input" varStatus="status" begin="0" end="0">       

        <%--011210 attempt to make it look right --%>
            <pimsForm:formItem name="${input.sampleHook}${fn:escapeXml(input.sampleName)}" alias="${input.inputSampleName }" validation="" >
                <pimsForm:formItemLabel name="${input.sampleHook}${fn:escapeXml(input.sampleName)}" alias="${input.inputSampleName }" helpText="The Synthetic gene vector" validation="${validation}" />
                <div class="formfield">
                    <span class="viewonly">
                        <c:choose>
                            <c:when test="${!empty input.sampleName}">
                            <a href="${pageContext.request.contextPath}/View/${input.sampleHook}">${fn:escapeXml(input.sampleName)}</a>
                            </c:when>
                            <c:otherwise>
                                (None)
                            </c:otherwise>
                        </c:choose>
                    </span> 
                    <span class="editonly">
                     <select name="${input.inputSampleHook}:sample" id="${input.inputSampleHook}:sample"
                        onchange="handleSampleSearchOnclick(this,'${inputSampleHook}','${input.sampleCategoryName}')">
                        <c:if test="${null == sample.hook}">
                          <option value="" selected="selected">${none}</option>
                      </c:if>
        
                        <c:forEach items="${ input.samplesByUser }" var="entry">
                         <optgroup label="Assigned to: <c:out value='${ entry.key }' />">
                           <c:forEach items="${ entry.value }" var="sample">
                             <c:choose>
                                <c:when test="${sample.hook == input.sampleHook}">
                                 <option value="${sample.hook}" selected="selected"><c:out value="${sample.name}"  /></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${sample.hook}" ><c:out value="${sample.name}" /></option>
                                </c:otherwise>
                             </c:choose>
                          </c:forEach>
                         </optgroup>
                        </c:forEach>
                         <optgroup label="Search">
                          <option value="[SEARCH]">Search Samples</option>
                         </optgroup>
                    </select>
                </span>
              </div>
            </pimsForm:formItem>
        <%--END --%>
        </c:forEach>
        </pimsForm:column1>
        </pimsForm:formBlock>
        <pimsForm:formBlock id="sgene12">
         <pimsForm:editSubmit />
        </pimsForm:formBlock>
    </pimsForm:form>     
    <hr />
    <pimsForm:form action="/Update" id="ID" method="post" mode="view" >
        <pimsForm:formBlock id="sgene3">
            <pimsForm:column1>
            <c:forEach items="${psgb.parameters}" var="parameter" varStatus="status" begin="0" end="1">
                <c:set var="labelName" value="${parameter.parameterDefinition.name}" />
                <c:if test="${null==parameter.parameterDefinition}">
                    <c:set var="labelName" value="${parameter.name}" />
                </c:if>
                    <pimsForm:text name="${parameter._Hook}:value" value="${parameter.value}" alias="${labelName}" helpText="${parameter.parameterDefinition.label}" />
            </c:forEach>
            <c:if test="${empty psgb.targetBean.name}">
                <pimsForm:mruSelect hook="${psgb.experimentBean.hook}" rolename="${Experiment['PROP_RESEARCHOBJECTIVE']}" alias="Target" helpText="The Target for the Synthetic gene" required="${true}" />
            </c:if>
            </pimsForm:column1>
            <pimsForm:column2>
            <c:forEach items="${psgb.parameters}" var="parameter" varStatus="status" begin="2" >
                <c:set var="labelName" value="${parameter.parameterDefinition.name}" />
                <c:if test="${null==parameter.parameterDefinition}">
                    <c:set var="labelName" value="${parameter.name}" />
                </c:if>
                    <pimsForm:text name="${parameter._Hook}:value" value="${parameter.value}" alias="${labelName}" helpText="${parameter.parameterDefinition.label}" />    
            </c:forEach>
            <c:if test="${empty psgb.targetBean.name}">
                <pimsForm:mruSelect hook="${psgb.experimentBean.hook}" rolename="${Experiment['PROP_RESEARCHOBJECTIVE']}" alias="Target" helpText="The Target for the Synthetic gene" required="${true}" />
            </c:if>
            </pimsForm:column2>
        </pimsForm:formBlock>
        <pimsForm:formBlock id="sgene13">
         <pimsForm:editSubmit />
        </pimsForm:formBlock>
    </pimsForm:form>

</pimsWidget:box>


<pimsWidget:box id="sequences" title="Sequences" initialState="${initialState}" >
<pimsForm:form action="/Update" id="ID" method="post" mode="${mode}" boxToOpen="sequences" >
<pimsForm:formBlock id="blk1">
<%-- If there is no MolComponent DNA --%>
    <c:choose>
    <c:when test="${empty psgb.sgDnaSeqHook}">
        <c:if test="${mayUpdate}"><br />
          <input type="button" style="float: left;" class="submit" onclick="if(!document.attributeChanged || confirm('Unsaved changes detected, they will be lost if you proceed. Proceed?')) $('addDNA').submit();" value="Add DNA sequence..." />
        <br /><br /></c:if>
    </c:when>
    <c:otherwise>
     <pimsForm:textarea  extraClasses="sequence" name="${psgb.sgDnaSeqHook}:${Molecule['PROP_SEQSTRING'] }" alias="DNA sequence" id="DNASequence" helpText="The Synthetic gene DNA sequence" validation="dnaSequence:true" onchange="this.value=cleanSequence2(this.value);">
            <pims:sequence sequence="${psgb.dnaSeq}" format='DEFAULT' escapeStyle="NONE" />
     </pimsForm:textarea>
    
     <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
            <strong title="Length (bp) of the DNA sequence" > Length: </strong><c:out value="${fn:length(fn:replace(psgb.dnaSeq, ' ', ''))}"/>
            &nbsp; <strong title="%GC nucleotides in the Target DNA sequence" > &#037;GC: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${gcCont}"/></fmt:formatNumber>
            <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${psgb.sgDnaSeqHook}?type=sGeneDNA" />
                &nbsp; <a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    
            <c:if test="${! empty psgb.dnaSeq}">
                <c:set var="rcurl" value="${pageContext.request.contextPath}/read/ReverseComplementPopup/${psgb.sgDnaSeqHook}" />
                &nbsp; Reverse complement <a class="popup" href="javascript:widePopUp('${rcurl}')">Pop-up</a>    
            </c:if>
         </span>
        </div>
    </c:otherwise>
</c:choose>
</pimsForm:formBlock>
<pimsForm:formBlock id="blk3">
    <pimsForm:textarea extraClasses="sequence" name="${psgb.sgProteinSeqHook}:${Molecule['PROP_SEQSTRING'] }" alias="Protein sequence" helpText="The Protein sequence of the Synthetic gene" validation="proteinSequence:true" onchange="this.value=cleanSequence(this.value);">
        <pims:sequence sequence="${psgb.proteinSeq}" format='DEFAULT' escapeStyle="NONE" />
    </pimsForm:textarea>
    <c:if test="${! empty psgb.proteinSeq}" >
    <div class="formitem viewonly" style="padding-left:12.5em">
      <span align='right'>
        <strong title="Length (residues) of the protein sequence" > Length: </strong><c:out value="${fn:length(fn:replace(psgb.proteinSeq, ' ', ''))}"/>
        &nbsp; <strong title="Molecular weight (Da)"> Weight (Da): </strong><fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${protMass}" /></fmt:formatNumber>
        &nbsp; <strong title="Extinction coefficient cm-1 M-1"> Extinction: </strong><c:out value="${protEX}" />
        &nbsp; <strong title="Absorbance of a 0.1% solution (1g/L)"> Abs 0.1&#037;: </strong>
        <fmt:formatNumber type="Number" maxFractionDigits="3" ><c:out value="${abs01pc}" /></fmt:formatNumber>
        &nbsp; <strong title="Isoelectric point pI"> pI: </strong><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${protPI}" /></fmt:formatNumber> &nbsp;
        <c:set var="fastaurl" value="${pageContext.request.contextPath}/read/FastaSequencePopup/${psgb.sgProteinSeqHook}?type=sGeneProtein" />
        <br /><a class="popup" href="javascript:widePopUp('${fastaurl}')">Fasta Pop-up</a>    
      </span>
    </div>
    </c:if>
</pimsForm:formBlock>
        <pimsForm:editSubmit />
    <c:if test="${createDNA}">
    <script type="text/javascript">document.getElementById("DNASequence").focus();</script>
    </c:if>
</pimsForm:form>
</pimsWidget:box>

<c:choose>
    <c:when test="${'Leeds' eq perspective.name}">
        <pimsWidget:box id="leeds" title="Leeds Constructs" initialState="closed" >
             <c:if test="${! empty leedsConstructs}">
                <c:import url="/JSP/construct/LeedsConstructList.jsp"/>
             </c:if>
             Add&nbsp;new&nbsp;<a href="${pageContext.request.contextPath}/NewConstruct">EntryClone</a>&nbsp;/&nbsp;<a href="${pageContext.request.contextPath}/NewConstruct?type=Expression">Expression</a>&nbsp;/&nbsp;<a href="${pageContext.request.contextPath}/NewConstruct?type=DeepFrozenCulture">Deep-Frozen&nbsp;culture</a><br/>
         <br/>
        </pimsWidget:box>
    </c:when>
    <c:otherwise>
    <%--CONSTRUCTS--%>
    <c:if test="${!empty milestones}">
        <pimsWidget:box id="constructs" title="Constructs" initialState="closed" extraHeader="${newConstruct}">
            <table class="list">
                <tr><th colspan='2' >Construct</th><th colspan='3' >Latest Experiment</th><th rowspan="2">&nbsp;</th><th rowspan="2">&nbsp;</th></tr>
                <tr><th align='left'>Name</th><th align='left'>Description</th>
                    <th align='left'>Milestone</th><th align='left'>Date</th>
                    <th align='left'>Status</th>
                </tr>            
                <c:forEach items="${milestones}" var="construct" >
                    <c:if test="${true == construct.syntheticGeneMilestone && construct.sgeneHook eq psgb.hook}">
                         <tr>
                            <td>
                              <c:choose>
                              <c:when test="${null == construct.expBlueprint}">
                                   <pimsWidget:linkWithIcon 
                                    icon="types/small/construct.gif" 
                                    url="${pageContext.request.contextPath}/View/${construct.constructHook}" 
                                    text="${construct.name}"/>
                              </c:when>
                              <c:otherwise>
                                   <pimsWidget:linkWithIcon 
                                    icon="types/small/construct.gif" 
                                    url="${pageContext.request.contextPath}/read/ConstructView/${construct.constructHook}" 
                                    text="${construct.name}"/>
                              </c:otherwise>
                              </c:choose>
                            </td>
                            <td><c:out value="${construct.constructDescription}" /></td>
                            <td><c:out value="${construct.milestoneName}" /></td>
                            <td><pimsWidget:dateLink date="${construct.dateOfExperiment}"  /></td>
                            <td><c:out value="${construct.result}" /></td>
                            <td><a href="${pageContext.request.contextPath}/spot/SpotConstructMilestone?commonName=<c:out value="${construct.name}" />">All Experiments</a></td>
                            <td><pimsWidget:linkWithIcon 
                                icon="actions/create/experiment.gif" 
                                url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${construct.constructHook}" 
                                text="New experiment"/>
                            </td>
                        </tr>
                     </c:if>
                   </c:forEach>
               </table>
        </pimsWidget:box>
    </c:if>
    </c:otherwise>
</c:choose>
 
<pimsWidget:externalDbLinks bean="${psgb}" dbnames="${dbnames}"/>
<pimsWidget:files bean="${psgb}"  />

<pimsWidget:notes bean="${psgb}"  />

<%--END --%>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>

<script type="text/javascript">//<!--
search=new Object();

var currentSampleSearchSelect=null;

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
    search.sampleDef=sampleDefID; //unused
    mruToUpdate=elem;
    openModalWindow(contextPath+"/Search/org.pimslims.model.sample.Sample?isInModalWindow=yes&callbackFunction=chooseSample&sampleCategories="+sampleCategory,"Choose sample");
}
/**
 * @deprecated? Now use MRU handling functions in widgets.js
 */
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

//--></script>    
<jsp:include page="/JSP/core/Footer.jsp" />
