<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: susy
Date: 13 Nov 2009
JSP to be included in experimentTabs if the Experiment is a Cell stock experiment
displays holder and position
Servlets: Update

-->
<%-- bean declarations e.g.:
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />
<jsp:useBean id="constructBeans" scope="request"
type="java.util.Collection<ConstructBean>" />
--%>
<jsp:useBean id="outputsamples" scope="request" type="java.util.List<org.pimslims.presentation.experiment.OutputSampleBean>" />

<c:catch var="error">
<%-- page body here --%>
<c:choose><c:when test="${empty outputsamples}">
    <pimsForm:form mode="view" action="/experiment/UpdateOutputSamples" method="post">
    <table>
        <tr>
            <th colspan="3">No output samples are defined in the protocol</th>
        </tr>
    </table>
    </pimsForm:form>
</c:when>
<c:otherwise>
  <c:set var="amountLabel"><%=org.pimslims.model.sample.Sample.PROP_CURRENTAMOUNT %></c:set>
  <c:set var="sampleLabel"><%=org.pimslims.model.sample.Sample.PROP_NAME %></c:set>
  <c:set var="sampleCol"><%=org.pimslims.model.sample.Sample.PROP_COLPOSITION %></c:set>
  <c:set var="sampleRow"><%=org.pimslims.model.sample.Sample.PROP_ROWPOSITION %></c:set>
  <c:set var="sampleHolder"><%=org.pimslims.model.sample.Sample.PROP_HOLDER %></c:set>
  <c:set var="noneHTML" value="[NONE]" />

  <pimsForm:form mode="view" action="/experiment/UpdateOutputSamples" method="post">
    <table border="0">
        <tr>
            <th rowspan="2" style="width:15em;">Outputs</th>
            <td colspan="3" style="border:0;">&nbsp;</td>
        </tr>
        <tr style="">
            <th class="amountheader">Amount</th>
            <th>Sample name</th>
            <th title="The plate or box containing the sample">Holder</th>
            <th style="width:4em" title="The row position for the sample in its holder e.g. A, B, C etc.">Row</th>
            <th style="width:4em" title="The column number for the sample in its holder e.g. 1, 2, 3 etc.">Column</th>
 <%--NOT FOR STOCKS           <th style="width:4em">Copy</th> --%>
            <th style="width:4em">Delete</th>
        </tr>
        <c:forEach items="${outputsamples}" var="row" varStatus="rowStatus">
        
          <tr id="${row.outputSampleHook}" class=" ajax_deletable">
          <th><c:out value="${row.outputSampleName}" /></th>
                <td>
                    <span class="viewonly">
                        ${row.amount}
                    </span>
                    <span class="editonly">
                         <pimsForm:doAmount hook="${row.sampleHook}" propertyName="${amountLabel}" value="${row.amount}" />
                    </span>
                </td>                   
                 <td>
                    <span class="viewonly">
                       <c:choose><c:when test="${!empty row.sampleHook}">
                            <a href="${pageContext.request.contextPath}/View/${row.sampleHook}">${fn:escapeXml(row.sampleName)}</a> <%--Should be context-menu link--%>
                        </c:when><c:otherwise>
                            (None)
                        </c:otherwise></c:choose>
                    </span>
                    <span class="editonly">
                        <input title="sample name" style="width: 10em;" name="${row.sampleHook}:${sampleLabel}" value="<c:out value='${row.sampleName}' />" />
                    </span>
                </td>
                <%--The holder TODO use MRU, see ViewSample.jsp --%>
                 <td>
                    <span class="viewonly">
                     <c:choose>
                      <c:when test="${row.holder == null}" >
                            none
                       </c:when>
                       <c:otherwise>
                           <c:choose>
                            <c:when test="${null eq experimentGroup}">
                                <pimsWidget:link bean="${row.holder}" />
                            </c:when>
                            <c:otherwise>
                                <pimsWidget:link bean="${experimentGroup}" />
                            </c:otherwise>
                           </c:choose>
                       </c:otherwise>
                     </c:choose>
                    </span>
                    <span class="editonly">
                        <select name="${row.sampleHook}:${sampleHolder}" id="${row.sampleHook}:${sampleHolder}" >
                            <pimsForm:option optionValue="${noneHTML}" alias="none" currentValue="${row.holder.hook}" />
                                <c:forEach items="${holders}" var="entry"> 
                                    <pimsForm:option optionValue="${entry.hook}" currentValue="${row.holder.hook}" alias="${fn:escapeXml(entry.name)}" />
                                </c:forEach>
                        </select>
                    
                    </span>
                </td>
                <%--The row position --%>
                <td>
                    <span class="viewonly">
                        ${row.rowAlphaPosition}
                    </span>
                    <span class="editonly">
                        <input title="row position" style="width: 5em;" id="${row.sampleHook}:${sampleRow}" name="${row.sampleHook}:${sampleRow}" value="<c:out value='${row.rowAlphaPosition}' />" />
                        <script type="text/javascript">
                        attachValidation("${row.sampleHook}:${sampleRow}",{rowPosition:true,alias:"Row position"});
                        </script>
                    </span>
                </td>
                <%--The column position --%>
                <td>
                    <span class="viewonly">
                        ${row.colPosition}
                    </span>
                    <span class="editonly">
                        <input title="column position" style="width: 5em;" id="${row.sampleHook}:${sampleCol}" name="${row.sampleHook}:${sampleCol}" value="<c:out value='${row.colPosition}' />" />
                        <script type="text/javascript">
                        attachValidation("${row.sampleHook}:${sampleCol}",{wholeNumber:true,alias:"Column position"});
                        </script>
                    </span>
                </td>                

               <%--copy NOT POSSIBLE FOR STOCKS YET SO REMOVE
                <td style="text-align:center">
                    <c:choose>
                        <c:when test="${'Plasmid stock' == experimentType.name}">
                        </c:when>
                        <c:when test="${'Cell stock' == experimentType.name}">
                        </c:when>
                        <c:otherwise>
                            <img class="copy" 
                             src="${pageContext.request.contextPath}/skins/default/images/icons/actions/copy.gif"
                             title="Make a copy of <c:out value='${row.sampleName}' />"
                             alt="Copy <c:out value='${row.sampleName}' />"
                             onclick="var frm=$(this).up('form');frm.innerHTML+='<input type=\'hidden\' name=\'${row.sampleHook}:copy\' />';frm.submit()" />
                        </c:otherwise>
                    </c:choose>
                 </td>
 --%>                 
                 <td style="text-align:center">
                    
                    <!-- <div id="${row.outputSampleHook}" class=" ajax_deletable"> -->
                    
                        <c:choose><c:when test="${row.mayDelete}">
                            <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete.gif" 
                                alt="Delete" title="Delete"
                                id="${row.outputSampleHook}_deleteicon" 
                                onclick="ajax_delete(this, {cleanup:function(){experiment_checkNumOutputs()} } );return false;"
                            />
                        </c:when><c:otherwise>
                            <img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/delete_no.gif" 
                            alt="Can't delete" title="Can't delete"
                            id="${row.outputSampleHook}_deleteicon" 
                            />
                         </c:otherwise></c:choose>
                    
                    <!-- </div> -->
                 </td>
          </tr>
          
        </c:forEach>
        
        <tr>
            <td colspan="8" style="padding-right:1em">
                <pimsForm:editSubmit/>
            </td>
        </tr>
  </table>
  </pimsForm:form>
</c:otherwise>
</c:choose>
<!--  /outputsamples.jsp -->
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
