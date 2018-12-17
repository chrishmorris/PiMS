<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: susy
Date: 28-Feb-2008
Servlets: Servlets: /servlet/dnatarget/DNAConstructWizard.java
To display sequence in 10 blocks of ten.
If the including JSP is step 2 a jsp:param is set for the construct Sequence in the include
the JSP uses this sequence.
Otherwise the targetDNA sequence is used
-->
<%-- bean declarations --%>
<c:catch var="error">

<%-- page body here --%>
<%--caller must provide String dnaSeq --%>

<c:set var="seqLen" value="${fn:length(param['dnaSeq'])}" />

  <p class='sequence'>
  <c:choose>
   <c:when test="${empty param['conChunks']}">
  	<c:choose>
    <c:when test="${empty chunks}">
    DNA sequence has not been formatted correctly<br />
    <c:out value="${param['dnaSeq']}" />
   	</c:when>
   	<c:otherwise>
 		<c:forEach items="${chunks}" var="chunk" varStatus="status" >
 		<c:choose>
              <c:when test="${empty chunk}"/>
        <c:otherwise>		
     	<c:out value="${chunk}" />&nbsp;&nbsp;<strong>
     		<c:choose>
        		<c:when test="${status.last}">
         		<c:out value="${seqLen}" />
        		</c:when>
        		<c:otherwise>
	       		<c:out value="${status.count*100}"/>
	    		</c:otherwise>
      		</c:choose>
      		</strong><br />
      		</c:otherwise>
      		</c:choose>
    	</c:forEach>
   	 </c:otherwise>
  	 </c:choose>
  	</c:when>
  	<c:otherwise>
     <c:choose>
     <c:when test="${empty constructChunks}">
     DNA sequence has not been formatted correctly<br />
     <c:out value="${param['dnaSeq']}" />
    </c:when>
    <c:otherwise>
 		<c:forEach items="${constructChunks}" var="chunk" varStatus="status" >
        <c:choose>
              <c:when test="${empty chunk}"/>
        <c:otherwise>       
     	<c:out value="${chunk}" />&nbsp;&nbsp;<strong>
     		<c:choose>
        		<c:when test="${status.last}">
         		<c:out value="${seqLen}" />
        		</c:when>
        		<c:otherwise>
	       		<c:out value="${status.count*100}"/>
	    		</c:otherwise>
      		</c:choose>
      		</strong><br />
      		</c:otherwise>
      		</c:choose>
    	</c:forEach>
   	 </c:otherwise>
  	</c:choose>  
  </c:otherwise>
  </c:choose>
  </p>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    

<!-- OLD -->
