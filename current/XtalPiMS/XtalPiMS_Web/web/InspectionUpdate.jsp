<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Plate view for inspection ${inspection.name}' />
</jsp:include>
    <c:choose><c:when test="${inspection._MayDelete}">
      <span class="linkwithicon " title="Delete inspection "><a  href="${pageContext.request['contextPath']}/Delete?hook=${inspection._Hook}"><img class="icon" src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/delete.gif" alt=""/></a><a  href="${pageContext.request['contextPath']}/Delete?hook=${inspection._Hook}">Delete</a></span>
    </c:when><c:otherwise>
      <pimsWidget:linkWithIcon 
        text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
        url="#" isGreyedOut="true"
        onclick="return false" />
    </c:otherwise></c:choose>
    
<h3 style="font-size:14px;margin:0;"> <a href='${pageContext.request.contextPath}/ViewTrialDrops.jsp?barcode=<c:out value="${holder.name}" />&name=<c:out value="${inspection.name}" />'>View Trial Drops</a></h3>
<br />

<pimsWidget:box title="Inspection ${inspection.name}" initialState="fixed">
 <pimsForm:form method="post" action="/Update/Inspection?name=<c:out value='${inspection.name}' />" mode="edit" id="new_plate">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:select name="holderID" alias="Plate" helpText="the plate of this inspection"  validation="required:true">
         <pimsForm:option optionValue="${holder.dbId}" currentValue="${holder.dbId}" alias="${holder.name}" />
       </pimsForm:select>
       <pimsForm:text name="inspectionName" alias="Name" helpText="name of this inspection" value="${inspection.name}" validation="required:true" />
       
        <pimsForm:select name="imagerID" alias="Imager" helpText="the imager of this inspection" validation="required:true">
        <c:forEach var="p" items="${imagers}">
          <pimsForm:option optionValue="${p.dbId}" currentValue="${currentImagerId}" alias="${p.name}" />
        </c:forEach>
       </pimsForm:select>
     </pimsForm:column1>
     <pimsForm:column2>

     <pimsForm:textarea name="description" alias="Description">${inspection.details}</pimsForm:textarea> 
     	
      
     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:editSubmit />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>


<c:forEach var="sub" items="${subs}">
<div style="page-break-before:always"></div>
<pimsWidget:box initialState="closed" title="Drops in subposition:${sub+1}" id="images${sub+1}">
<c:set var="currentRow" value="0"/>
<c:set var="currentColumn" value="0"/>
<c:set var="current" value="0"/>
<div style="overflow:auto">
<table class="orderplate" id="orderplate">
    <tr>
    <th>&nbsp;</th>
    <c:forEach var="col" items="${cols}">
        <th>${col}</th>
    </c:forEach>
    </tr>

    <c:forEach var="row" items="${rows}">
        <tr>
            <th>${rows[currentRow]}</th>
            <c:forEach var="col" items="${cols}">
                     <td>                       
                        <c:choose>
                            <c:when test="${!empty imageViewList[sub][current].url}">
                              <%-- TODO title="${imageViewList[sub][current].condition" --%>
                              <a href="${pageContext.request['contextPath']}/ViewTrialDrops.jsp?barcode=<c:out value="${holder.name}" />&name=<c:out value="${inspection.name}" />&well=${rows[currentRow]}${col}.${sub+1}&subPosition=${sub+1}" > 
                                    <img src="${imageViewList[sub][current].url}" height="70" width="70"/>
                              </a>
                            </c:when><c:otherwise>
                                [None]
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <c:set var="current" value="${1+current}"/>
                <c:set var="currentColumn" value="${1+currentColumn}"/>
            </c:forEach>
            <c:set var="currentRow" value="${1+currentRow}"/>
        </tr>
        <c:set var="currentColumn" value="0"/>
    </c:forEach>
    
</table>
</div>

<c:set var="inspectionName"><c:out value="${inspection.name}"/></c:set>
<pimsForm:form extraClasses="noprint" method="post" enctype="multipart/form-data" action="/Inspection/ImageUpload?inspectionName=${inspectionName}" mode="edit" >
  <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="imageRowCol${sub+1}" alias="Well" helpText="Well of the image to be add (eg:A02)" value="" validation="required:true" />
       <pimsForm:text name="imageUrl${sub+1}" alias="URL" helpText="If you want to add image by URL, input your URL here (eg: http://www.oppf.ox.ac.uk/OPPF/image/veeco.jpg), input 'none' to clear the image" value="" />
       <pimsForm:formItemLabel name="imageUpload" alias="Upload Image" helpText="If you want to upload an image, choose the file here"  />
		    <div class="formfield" >
		    <input type="file" name="imageUpload${sub+1}" />
		    </div>
     <pimsForm:editSubmit />
      </pimsForm:column1>
      <pimsForm:column2>
         <pimsWidget:plateSelector id="imageSelector${sub+1}" callbackFunction="image_RowCol_Selector" />        
      </pimsForm:column2>      
     </pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>
</c:forEach>

<script type="text/javascript">
function image_RowCol_Selector(obj){
    var numWells=obj.selectedWells.length;
    var sub=obj.plate.id.substr(13); //length of id:imageSelector
    $("imageRowCol"+sub).value=obj.selectedWells[0].id;
  }
</script>

<jsp:include page="/JSP/core/Footer.jsp" />
