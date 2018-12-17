<%--
SDMPrimerList.JSP
Included in NewSDMConstructWizardStep3.jsp which is called by 
org.pimslims.servlet.spot.CreateMutatedObjective
Displays a list of sense and antisense primers plus length and Tm from r and 
fprimerBeans, calculated from user-entered Tm at Step 2c
This JSP is called by org.pimslims.servlet.spot.CreateMutatedObjective
Author: Susy Griffiths, YSBL-York
Date: Feb 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page import="org.pimslims.presentation.*"  %>
<%@ page import="org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="sPrimerBeans" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="aPrimerBeans" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="primerBean" class="org.pimslims.lab.primer.SDMPrimerBean" scope="request"/>
<!-- spot/PrimerList.jsp -->
<c:choose>
 <c:when test="${sPrimerBeans[0] == null}">
        <c:out value="No suitable Sense primers for this Tm, please enter a new value " />
		<br /><br />
        <input type="button" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
        
 </c:when>
 <c:when test="${aPrimerBeans[0] == null}">
        <c:out value="No suitable AntiSense primers for this Tm, please enter a new value " />
		<br /><br />
		<input type="button" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
 </c:when>
 <c:otherwise>   <!-- Table of Primers-->
	<table border="0" class="list">
	<tr><td colspan="4"><strong>Please select one sense and one antisense primer</strong></td></tr>
	<tr><th class="subhead">select</th><th class="subhead">Tm</th><th class="subhead">Length</th><th>Sequence</th></tr>
	 <c:forEach var="primerBean" items="${sPrimerBeans}" varStatus="status" >
	    
    	<c:choose>
		 <c:when test="${status.first}">
		 
    		<tr>
    			<td>Sense Primer</td>
    			<td></td>
    			<td>
    				<input 
        readonly="readonly" 
        name="fwd_overlap_len" value="${primerBean.primerLength}" />
        		</td>
        		<td>
        			<input style="width: 100%" name="sense_primer" value="${primerBean.primerSeq}"
        onChange="this.form['fwd_overlap_len'].value=this.value.length;" />
    			</td>
    		</tr>
		 	<tr>
 			 	<td class="radio">
 			 		<input type="radio" name="fchoice" value="${status.index}" checked 
                      onclick="this.form['sense_primer'].value='${primerBean.primerSeq}'; this.form['fwd_overlap_len'].value=${primerBean.primerLength};" />
                </td>
		 </c:when>
		 <c:otherwise>
		 <tr>
			  <td class="radio">
			  	<input type="radio" name="fchoice" value="${status.index}" 
			      onclick="this.form['sense_primer'].value='${primerBean.primerSeq}'; this.form['fwd_overlap_len'].value=${primerBean.primerLength};" />
			  </td>
		 </c:otherwise>
		</c:choose>
		<td class="tm"><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${primerBean.primerTm}" /></fmt:formatNumber></td>
		<td class="length"><c:out value="${primerBean.primerLength}" /></td>
		<!-- 
		<td>
			<c:out value="${primerBean.codonsBefore}" />
			<span style="background-color:yellow;">
			<c:out value="${primerBean.mutated}" />
			</span>
			<c:out value="${primerBean.codonsAfter}" />
		</td>
		-->
		<td><c:out value="${primerBean.primerSeq}" /></td>
		<!-- <td><c:out value="${status.count}" /></td>-->
		</tr>
	</c:forEach>
	<tr><th class="subhead">select</th><th class="subhead">Tm</th><th class="subhead">Length</th><th>Sequence</th></tr>

	<c:forEach var="primerBean" items="${aPrimerBeans}" varStatus="rstatus" >
		<c:choose>
		 <c:when test="${rstatus.first}">
    		<tr>
    			<td>AntiSense Primer</td>
    			<td></td>
    			<td><input  name="rev_overlap_len" 
        readonly="readonly" 
        value="${primerBean.primerLength}" /></td>
        		<td>
        			<input style="width: 100%" name="antisense_primer" value="${primerBean.primerSeq}"
        onChange="this.form['rev_overlap_len'].value=this.value.length;" />
    			</td>
    		</tr>
         	<tr>
				<td class="radio">
					<input type="radio" name="rchoice" value="${rstatus.index}" checked 
                  onclick="this.form['antisense_primer'].value='${primerBean.primerSeq}'; this.form['rev_overlap_len'].value=${primerBean.primerLength};" />
				</td>
		 </c:when>
		 <c:otherwise>
		  	<td class="radio">
		  		<input type="radio" name="rchoice" value="${rstatus.index}" 
                  onclick="this.form['antisense_primer'].value='${primerBean.primerSeq}'; this.form['rev_overlap_len'].value=${primerBean.primerLength};" />
		  	</td>
		 </c:otherwise>
		</c:choose>
		<td class="tm"><fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${primerBean.primerTm}" /></fmt:formatNumber></td>
		<td class="length"><c:out value="${primerBean.primerLength}" /></td>
		<!-- 
		<td>
			<c:out value="${primerBean.codonsBefore}" />
			<span style="background-color:yellow;">
			<c:out value="${primerBean.mutated}" />
			</span>
			<c:out value="${primerBean.codonsAfter}" />
		</td>
		-->
		<td><c:out value="${primerBean.primerSeq}" /></td> 
		<!--  <td><c:out value="${rstatus.count}" /></td> -->
		</tr>
	</c:forEach>
	</table>
	
	<div style="text-align:right" >
	<input type="button" style="float:left" value="&lt;&lt;&lt; Back" onClick="history.go(-1);return true;"/>
	&nbsp;
    <input name='Submit' type='submit' value='Save &gt;&gt;&gt;' onclick="dontWarn()"/>&nbsp;
	</div>
 </c:otherwise>
</c:choose>