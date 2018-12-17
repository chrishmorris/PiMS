<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="bricks" uri="http://www.pims-lims.org/bricks" %>

<c:set var="HeaderName" scope="page" value="Downloads" />

<%@include file="../WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>

<bricks:brickGrid columns="2" width="100%">
        <bricks:brickRow>
            <bricks:brick title="Welcome to Downloads" width="2" height="1" bodyClass="bodyClass" headerClass="headerClass">
                <p>
                    This area provides some downloads, instructions and the like when the come available.
                </p>            
            </bricks:brick>
        </bricks:brickRow>
        <bricks:brickRow>
        <bricks:brick title="Libraries" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">

        </bricks:brick>
        <bricks:brick title="Web Application Files" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
            
        </bricks:brick>
    </bricks:brickRow>

</bricks:brickGrid>


<%-- HTML ENDS HERE --%>


<%@include file="../WEB-INF/jspf/footer.jspf"%>




