<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/view.css" type="text/css" />

<%
//----------------------------------------------------------------------------------------------
//			popup_seq_construct.jsp										
//			Popup page for construct Fasta sequences
//
//		 	Created by Johan van Niekerk,SSPF-Dundee				10 October 2005
//			Modified by														Date
//----------------------------------------------------------------------------------------------
%>

<% String header=request.getParameter("header") ;%>
<% String seq=request.getParameter("seq").replaceAll(" ", "");%>



<html>
<head>
<title>Fasta sequence</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body style="font-family:'Courier New',monospace">
<span class='sequence'><% out.println(">"+header);%></span>
<span class='sequence'>
<% 
int cols=0;
out.print("<br/>");
for (int i = 0; i<seq.length();i++) {
	out.print(seq.charAt(i));
	cols++;
	if(cols==100){
		out.print("<br>");
		cols=0;
	}
}
out.print("<br>");
%>
</span>
<script type="text/javascript">
if(document.all || !navigator.vendor){
    document.body.style.fontSize="80%";
}
</script>
</body>
</html>


<!-- OLD -->
