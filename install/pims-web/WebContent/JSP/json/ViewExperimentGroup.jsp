<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: 19 Feb 2008
Servlets: 

-->
<%-- bean declarations e.g.:
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />
<jsp:useBean id="constructBeans" scope="request"
type="java.util.Collection<ConstructBean>" />
--%>

<c:catch var="error">
<jsp:useBean id="experiments" scope="request" type="java.util.List<org.pimslims.presentation.WellExperimentBean>" /> 
<%--
//TODO why are these maps? Cant they be lists?

var experiments= --%>
{ updated:{ experiments:[
<c:forEach var="ex" items="${experiments}">
<%--    
    ex<c:out value='${ex.dbId}' />: 
--%>
    { 
        htmlid: "ex<c:out value='${ex.dbId}' />",
        name:"<c:out value='${ex.name}' />", 
        dbId:"<c:out value='${ex.dbId}' />", 
        status:"<c:out value='${ex.status}' />",

        params:{<c:forEach var="p" items="${ex.parameterBeans}" >
            pd<c:out value='${p.definitionDbId}' />: "<c:out value='${p.value}' />", 
        </c:forEach>},        
        inputs:{ <c:forEach var="is" items="${ex.inputSamples}" >
            is${is.refInputSampleDbId}:{
                htmlid: "is<c:out value='${is.refInputSampleDbId}' />", 
                name:"<c:out value='${is.sampleName}' />", 
                hook:"<c:out value='${is.sampleHook}' />", 
                amount:"<c:out value='${is.amount}' />" 
            }, 
        </c:forEach>},
        outputs:{ 
            os${ex.refOutputSampleDbId}:{ 
                htmlid: "os${ex.refOutputSampleDbId}",
                name:"<c:out value='${ex.outputSampleName}' />", 
                hook:"${ex.outputSampleHook}", 
                amount:"" 
            } 
        }

    },
</c:forEach>
]
}
}
</c:catch><c:if test="${error != null}">"/>
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    throw "<error/> <c:out value='${error}'/>"
</c:if>

