<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="bricks" uri="http://www.pims-lims.org/bricks" %>

<c:set var="HeaderName" scope="page" value="Documentation" />

<%@include file="../WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>

<bricks:brickGrid columns="4" width="100%">
    <bricks:brickRow>
        <bricks:brick title="xtalPIMS Documentation" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
            <p>
                This area of the site gives access to the any documentation available for the project.
            </p>            
        </bricks:brick>
        <bricks:brick title="Documents" width="3" height="1" bodyClass="bodyClass" headerClass="headerClass">

        </bricks:brick>
    </bricks:brickRow>

</bricks:brickGrid>

<% if (true) throw new ServletException("test"); %>

<%-- HTML ENDS HERE --%>


<%@include file="../WEB-INF/jspf/footer.jspf"%>




