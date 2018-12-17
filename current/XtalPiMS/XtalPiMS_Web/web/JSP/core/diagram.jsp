<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='PiMS diagram' />
</jsp:include>
<!--   diagram.jsp -->
<table id="key" align="center">
<tr>
<td style="text-align:center; width:100px;" >
<img src="${pageContext.request.contextPath}/images/diagram/octagon.gif" 
          					alt="complex or construct" title="complex or construct" />
</td>
<td style="text-align:center; width:100px;" >
<img src="${pageContext.request.contextPath}/images/diagram/invhouse.gif" 
          					alt="image for target" title="image for target" />
</td>
<td style="text-align:center; width:100px;" >
<img src="${pageContext.request.contextPath}/images/diagram/rectangle.gif" 
          					alt="image for experiment group" title="image for experiment group" />
</td>
<td style="text-align:center; width:100px;" >
<img src="${pageContext.request.contextPath}/images/diagram/elipse.gif" 
          					alt="image for experiment" title="image for experiment" />
</td>
<td style="text-align:center; width:100px;" >
<img src="${pageContext.request.contextPath}/images/diagram/diamond.gif" 
          					alt="image for sample" title="image for sample" />
</td> 
</tr>
<tr>
<td style="text-align:center;">Expression<br />Objective</td>
<td style="text-align:center;">Target</td>
<td style="text-align:center;">Plate</td>
<td style="text-align:center;">Experiment</td>
<td style="text-align:center;">Sample</td>
</tr>
</table>

<script>
var width = window.innerWidth;
var height = window.innerHeight;
if (document.documentElement) {
	height = height ? height : document.documentElement.offsetHeight;
    width  = width  ? width  : document.documentElement.offsetWidth;
}
if (document.body) {
	height = height ? height : document.body.offsetHeight;
	width  = width  ? width  : document.body.offsetWidth;
}
height = height ? height : 460;
width  = width  ? width  : 800;
// remove padding
width -=20;
height = height - $('key').getBoundingClientRect().bottom -30;
var src= '${pageContext.request.contextPath}/read/Dot/${hook}?height='
	+height.toString()+'&width='+width.toString();
document.write('<img src="'+src+'&format=png');
document.write('" usemap="#Graph" />');
</script>


<div id="map" />
<script>
 new Ajax.Request(src+"&format=cmapx", {
	  method:"get",
	  onSuccess:function(transport){
	    ${'map'}.innerHTML=transport.responseText;
      },
  	  onFailure:function(transport){
        alert("Sorry, there is an error. Unable to get: "+src);
	}
 });
</script>

<%--
<jsp:include page="/read/Dot/" >
    <jsp:param name="hook" value="${hook}" />
    <jsp:param name="format" value="cmapx" />
</jsp:include> --%>
        

<div id="svg" style="padding-top: 2em" />
<script>
var width = window.innerWidth;
var height = window.innerHeight;
if (document.documentElement) {
    height = height ? height : document.documentElement.offsetHeight;
    width  = width  ? width  : document.documentElement.offsetWidth;
}
if (document.body) {
    height = height ? height : document.body.offsetHeight;
    width  = width  ? width  : document.body.offsetWidth;
}
height = height ? height : 460;
width  = width  ? width  : 800;
// remove padding
width -=20;
height = height -200;
var src= '${pageContext.request.contextPath}/read/Dot/${hook}?height='
    +height.toString()+'&width='+width.toString();
 new Ajax.Request(src+"&format=svg", {
      method:"get",
      onSuccess:function(transport){
        ${'svg'}.innerHTML=transport.responseText;
      },
      onFailure:function(transport){
        alert("Sorry, there is an error. Unable to get: "+src);
    }
 });
</script>

</div>        

<jsp:include page="/JSP/core/Footer.jsp" />
