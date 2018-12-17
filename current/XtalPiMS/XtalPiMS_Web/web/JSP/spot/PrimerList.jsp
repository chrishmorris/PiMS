<%--
PrimerList.JSP

Author: Susy Griffiths, YSBL-York
Date: Feb 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="primerBeans" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="primerBean" class="org.pimslims.lab.primer.YorkPrimerBean" scope="request"/>
<jsp:useBean id="rprimerBeans" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="rprimerBean" class="org.pimslims.lab.primer.YorkPrimerBean" scope="request"/>
<jsp:useBean id="constructBean" scope="request" type="org.pimslims.presentation.construct.ConstructBean" />
<!-- spot/PrimerList.jsp -->
<c:choose>
 <c:when test="${primerBeans[0] == null}">
        <c:out value="No suitable Forward primers for this Tm, please enter a new value " />
		<br /><br />
        <input type="button" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
        
 </c:when>
 <c:when test="${rprimerBeans[0] == null}">
        <c:out value="No suitable Reverse primers for this Tm, please enter a new value " />
		<br /><br />
		<input type="button" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
 </c:when>
 <c:otherwise>   <!-- Table of Primers-->
 <h3 class="plainHead">Please select one Forward and one Reverse primer</h3>
	<table border="0" class="list">
	<tr><th class="subhead"></th><th class="subhead">Tm</th><th class="subhead">Length</th><th>Sequence</th></tr>
	 <c:forEach var="primerBean" items="${primerBeans}" varStatus="status" >
	    
    	<c:choose>
		 <c:when test="${status.first}">
		 <%--PIMS-3303 --%>
		 <c:choose>
		  <c:when test="${constructBean.dnaTarget=='dnaTarget' }">
            <tr><th title="Gene-specific region">Forward primer</th><td></td>
            <td><input                 
                readonly="readonly"  
                name="fwd_overlap_len" value="${primerBean.primerLength}" /></td>
                <td><input style="width: 100%" name="forward_primer" value="${primerBean.primerSeq}"
                onChange="this.form['fwd_overlap_len'].value=this.value.length;" 
           /></td></tr>
          </c:when>
          <c:otherwise>
            <tr><th title="Gene-specific region">Forward primer</th><td></td><td><input 
                readonly="readonly"   
                name="fwd_overlap_len" value="${primerBean.primerLength}" /></td><td><input style="width: 100%" name="forward_primer" value="${primerBean.primerSeq}"
                onChange="this.form['fwd_overlap_len'].value=this.value.length;" 
           /></td></tr>
         <%--PIMS-3303 --%>          
          </c:otherwise>
        </c:choose>
		 <tr>
 			 <td class="radio"><input type="radio" name="fchoice" value="${status.index}" checked 
                      onclick="this.form['forward_primer'].value='${primerBean.primerSeq}'; this.form['fwd_overlap_len'].value=${primerBean.primerLength};" 
                  /></td>
		 </c:when>
		 <c:otherwise>
		 <tr>
			  <td class="radio"><input type="radio" name="fchoice" value="${status.index}" 
			      onclick="this.form['forward_primer'].value='${primerBean.primerSeq}'; this.form['fwd_overlap_len'].value=${primerBean.primerLength};" 
			   /></td>
		 </c:otherwise>
		</c:choose>
		<td class="tm"><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${primerBean.primerTm}" /></fmt:formatNumber></td>
		<td class="length"><c:out value="${primerBean.primerLength}" /></td>
		<td><c:out value="${primerBean.primerSeq}" /></td>
		<!-- <td><c:out value="${status.count}" /></td>-->
		</tr>
	</c:forEach>
	<tr style="border-top:double;"><th class="subhead"></th><th class="subhead">Tm</th><th class="subhead">Length</th><th>Sequence</th></tr>

	<c:forEach var="primerBean" items="${rprimerBeans}" varStatus="rstatus" >
		<c:choose>
		 <c:when test="${rstatus.first}">
    <tr><th title="Gene-specific region">Reverse primer</th><td></td>
        <td><input  name="rev_overlap_len"         
        readonly="readonly" 
        value="${primerBean.primerLength}" /></td>
        <td><input style="width: 100%" name="reverse_primer" value="${primerBean.primerSeq}"
        onChange="this.form['rev_overlap_len'].value=this.value.length;" 
    /></td></tr>
         <tr>
			<td class="radio"><input type="radio" name="rchoice" value="${rstatus.index}" checked 
                  onclick="this.form['reverse_primer'].value='${primerBean.primerSeq}'; this.form['rev_overlap_len'].value=${primerBean.primerLength};" 
			/></td>
		 </c:when>
		 <c:otherwise>
		  <td class="radio"><input type="radio" name="rchoice" value="${rstatus.index}" 
                  onclick="this.form['reverse_primer'].value='${primerBean.primerSeq}'; this.form['rev_overlap_len'].value=${primerBean.primerLength};" 
		  /></td>
		 </c:otherwise>
		</c:choose>
		<td class="tm"><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${primerBean.primerTm}" /></fmt:formatNumber></td>
		<td class="length"><c:out value="${primerBean.primerLength}" /></td>
		<td><c:out value="${primerBean.primerSeq}" /></td>
		<!-- <td><c:out value="${rstatus.count}" /></td> -->
		</tr>
	</c:forEach>
	</table>
	
	<div style="text-align:right" >
	<input type="button" style="float:left" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
	&nbsp;
    <input name='Submit' type='submit' value='Next &gt;&gt;&gt;' onclick="dontWarn()"/>&nbsp;
	</div>
 </c:otherwise>
</c:choose>