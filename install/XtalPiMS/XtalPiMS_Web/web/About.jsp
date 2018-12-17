<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="bricks" uri="http://www.pims-lims.org/bricks" %>

<c:set var="HeaderName" scope="page" value="About" />

<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>
<bricks:brickGrid columns="3" width="100%">
    <bricks:brickRow>
        <bricks:brick title="About" width="2" height="1" bodyClass="bodyClass" headerClass="headerClass">
            <p><i>xtal</i>PiMS was a project funded by the European BIOXHIT Project (<a target = "new" href="http://www.bioxhit.org">www.bioxhit.org</a>) and also contributed by the PiMS Project - A Laboratory Information Management System for Protein Crystallography (<a target = "new" href="http://www.pims-lims.org">www.pims-lims.org</a>).</p>
            
            <p><i>xtal</i>PiMS project members and developers involved in this project are:</p>
            <ul>
                <li>Ian Berry (OPPF, Oxford, UK) (BIOXHIT Working Group 1 Co-ordinator)</li>
                <li>Jon Diprose (OPPF Oxford)</li>   
            </ul>    
			<p>PiMS developers involved in this project are:</p>
            <ul>
                <li>Bill Lin </li>
                <li>Ed Denial</li>
                <li>Chris Morris (PIMS Project Manager)</li>
            </ul> 
        </bricks:brick>
        <bricks:brick title="Additional Support" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
        
            <p>With the help and support from</p>
            <ul>
                <li>Diederick de Vries (NKI, Amsterdam, The Netherlands)</li>
                <li>Gael Seroul (EMBL Grenoble, France)</li>
                <li>Tassos Perrakis (NKI, Amsterdam)</li>
                <li>Josan Marquez (EMBL Grenoble)</li>
                <li>Robert Esnouf (OPPF Oxford)</li>            
            </ul>
        </bricks:brick>
    </bricks:brickRow>
    <bricks:brickRow>
        <bricks:brick title="Developed By" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
            <center>
            <a target = 'new' href="http://www.oppf.ox.ac.uk"><img style="border: 0" src="./xtal/images/crystallization/oppf-logo.gif"  alt="OPPF"/></a>
           <a target = "new" href="http://www.pims-lims.ac.uk"><img style="border: 0" src="./xtal/images/crystallization/pims-logo.gif"  alt="PIMS"/></a>
            <br/><a target = 'new' href="http://www.nki.nl"><img style="border: 0" src="./xtal/images/crystallization/nki-logo.gif"  alt="NKI"/></a>
            <br/><a target = 'new' href="http://www.embl-grenoble.fr"><img style="border: 0" src="./xtal/images/crystallization/embl-logo.gif"  alt="EMBL"/><img style="border: 0" src="./xtal/images/crystallization/embl-logo2.gif"  alt=" Grenoble"/></a>
        </center>
        </bricks:brick>
        <bricks:brick title="Funded By" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
            <center><a target = "new" href="http://www.bioxhit.org"><img style="border: 0" src="./xtal/images/crystallization/bioxhit-logo.png"  alt="BIOXHIT"/></a></center>
            
        </bricks:brick>
        <bricks:brick title="In Co-operation with" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
            <center>
             <br/><a target = "new" href="http://www.e-htpx.ac.uk"><img style="border: 0" src="./xtal/images/crystallization/ehtpx-logo.gif"  alt="e-HTPX"/></a>
        </center>
        </bricks:brick>
    </bricks:brickRow>
</bricks:brickGrid>

<%-- HTML ENDS HERE --%>

<%@include file="./WEB-INF/jspf/footer.jspf"%>

