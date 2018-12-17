<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Find Holder by BarCode' />
</jsp:include>      

<!--main page content goes in here-->

<p align="left" style="font-weight: bold;">Find a Holder from its barcode</p>

<p align="left"><span style="color:red;font-weight:bold">${param.message}</span></p>

<form action="${pageContext.request.contextPath}/Barcode" name="myform" method="get" 
    	style="background-color:#ccf">

<p align="left">Enter a barcode and the program will query the database for the appropriate Holder</p>
<p align="left">If the Holder is found, its view is opened, otherwise search Containers.</p>
<div class="formrow" style="text-align: left;">
	<input style="float:right; width:auto" type="submit" 
	name="Submit" value="get record" /> 
	Barcode
	<input name="barcode" type="text" id="barcode" style="width: 10em;" maxlength="25" />
	<input name="classname" type="hidden" id="barcode" value="org.pimslims.model.holder.Holder" />
</div>
</form>

<br />

<!--end of main page content-->
<jsp:include page="/JSP/core/Footer.jsp" />
