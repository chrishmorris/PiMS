<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Test of form widgets" />
</jsp:include>

<pimsWidget:plateSelector id="plateTest" callbackFunction="pwPlateSelector_exampleCallbackFunction" />

<%
java.util.Map radioSet = new java.util.LinkedHashMap();
radioSet.put("one","Option one");
radioSet.put("two","Option two");
radioSet.put("three","Option three");
radioSet.put("four","Option four");
request.setAttribute("radioSet",radioSet);
%>
<pimsWidget:box id="box1" title="Form" extraHeader="extra stuff here" initialState="open">
<pimsForm:form action="act" id="ID" method="get" mode="view">
	<pimsForm:formBlock id="blk2">
		<pimsForm:column1>
			<pimsForm:text validation="required:true" value="The value" name="fieldname1" alias="Text box" helpText="This is a tooltip" />
			<pimsForm:textarea onchange="alert(this)" name="txt1" alias="Textarea" helpText="This is a tooltip"><c:out value=">A textarea" /></pimsForm:textarea>
			<pimsForm:url value="http://www.google.com" name="url" alias="Link field" />
			<pimsForm:select name="sel1" alias="Select" helpText="This is a tooltip" >
				<pimsForm:option currentValue="two" optionValue="one" alias="One" />
				<pimsForm:option currentValue="two" optionValue="two" alias="Two" />
				<pimsForm:option currentValue="two" optionValue="three" alias="Three" />
			</pimsForm:select>
			<pimsForm:radio name="rad" alias="Single radio button" value="val" onclick="alert('test of onclick')" label="Button label" />
			<pimsForm:radio name="rad2" alias="Checked radio button with a very long name" value="val" label="Button label" isChecked="${true}" />
			<pimsForm:checkbox isChecked="false" name="chk1" label="A checkbox, isChecked=false" />
			<pimsForm:checkbox isChecked="true" name="chk2" label="A checkbox, isChecked=true" />
			<pimsForm:checkbox name="chk3" label="A checkbox, no isChecked" />
		</pimsForm:column1>
		<pimsForm:column2>
			<pimsForm:date value="<%= new java.util.GregorianCalendar()%>" name="date" alias="Date field" helpText="A date field" />
			<pimsForm:text value="a\"'<err>The value" name="fieldname3" alias="Text box" helpText="This is a tooltip" />
			<pimsForm:text value="The value" name="fieldname4" alias="Text box" helpText="This is a tooltip" />
			<pimsForm:radioSet onclick="alert(this.value)" name="radset" alias="Radio button set" labelValuePairs="${radioSet}" checkedValue="three" />
			<pimsForm:editSubmit />
			<pimsForm:submitCreate />
		</pimsForm:column2>
	</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box >

<pimsWidget:box id="box2" title="Fixed" initialState="fixed">

</pimsWidget:box>
<pimsWidget:box id="box3" title="Closed" initialState="closed">

</pimsWidget:box>
<pimsWidget:box id="box4" title="Open" initialState="open">

</pimsWidget:box>
<pimsWidget:box id="box4" title="Default state" extraHeader="(Should be closed)">

</pimsWidget:box>


</body>
</html>

