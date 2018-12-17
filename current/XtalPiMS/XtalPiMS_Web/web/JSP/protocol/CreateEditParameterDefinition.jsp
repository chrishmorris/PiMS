<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%-- defaults for creation 
Is this obsolete?
--%>
<c:set var="submitaction">/Create/org.pimslims.model.protocol.ParameterDefinition</c:set>
<c:set var="name"></c:set>
<c:set var="description"></c:set>
<c:set var="paramType">${param['paramType']}</c:set><%-- from URL --%>
<c:set var="displayUnit"></c:set>
<c:set var="isMandatory">${false}</c:set>
<c:set var="isGroupLevel">${param['isGroupLevel']}</c:set><%-- from URL --%>
<c:set var="isResult">${param['isResult']}</c:set><%-- from URL --%>
<c:set var="defaultValue"></c:set>
<c:set var="minValue"></c:set>
<c:set var="maxValue"></c:set>
<c:set var="possibleValues"></c:set>
<c:set var="prefix">org.pimslims.model.protocol.ParameterDefinition:</c:set><%-- class name for create ("parameterDefinition:name"), hook plus colon for edit ("parameterDefinition:1234:name") --%>

<c:if test="${!empty pdef}">
	<c:set var="submitaction">/Update/${pdef.protocol._Hook}</c:set>
	<c:set var="name">${pdef.name}</c:set>
	<c:set var="description">${pdef.label}</c:set>
    <c:set var="displayUnit">${pdef.displayUnit}</c:set>
	<c:set var="paramType">${pdef.paramType}</c:set>
	<c:set var="isMandatory">${false}</c:set>
	<c:set var="isGroupLevel">
       <c:choose><c:when test="${pdef.isGroupLevel}">Yes</c:when>
       <c:otherwise>No</c:otherwise></c:choose>
    </c:set>
	<c:set var="isResult">
	   <c:choose><c:when test="${pdef.isResult}">Yes</c:when>
	   <c:otherwise>No</c:otherwise></c:choose>
	</c:set>
	<c:set var="defaultValue">${pdef.defaultValue}</c:set>
	<c:set var="minValue">${pdef.minValue}</c:set>
	<c:set var="maxValue">${pdef.maxValue}</c:set>
	<c:set var="possibleValues">${pdef.possibleValuesAsString}</c:set>
	<c:set var="prefix">${pdef.hook}:</c:set><%-- class name for create ("parameterDefinition:name"), hook plus colon for edit ("parameterDefinition:1234:name") --%>
</c:if>



<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="[[[***]]]" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
    <jsp:param name="isInModalWindow" value='yes' />
</jsp:include>

<script type="text/javascript">
var prefix="${prefix}";
</script>
 
<pimsForm:form action="#" mode="edit" method="post" id="dummyform">
    <pimsForm:formBlock>
        <pimsForm:column1>
            <pimsForm:text name="name" alias="Name" validation="required:true, custom:function(val,alias){ if(val==$('name').oldValue) return ''; return protocol_checkNameUnique(val,alias)}" value="${name}" />            
            <pimsForm:text name="label" alias="Description" value="${description}" />
            <c:if test="${'Int'==paramType || 'Float'==paramType}">
                <pimsForm:text name="displayUnit" alias="Display unit" value="${displayUnit}" />
            </c:if>
            <pimsForm:radio name="isMandatory" alias="Mandatory parameter" value="yes" label="Yes" isChecked="${isMandatory}"/>
            <pimsForm:radio name="isMandatory" alias="" value="no" label="No" isChecked="${!isMandatory}"/>
        </pimsForm:column1>
        <pimsForm:column2>
            <c:set var="groupChecked">${false}</c:set>
            <c:set var="setupChecked">${false}</c:set>
            <c:set var="resultChecked">${false}</c:set>
            <c:choose><c:when test="${isGroupLevel=='Yes'}">
                <c:set var="groupChecked">${true}</c:set>
            </c:when><c:when test="${isGroupLevel=='No' && isResult=='Yes'}">
                <c:set var="resultChecked">${true}</c:set>
            </c:when><c:otherwise>
            <c:set var="setupChecked">${true}</c:set>
            </c:otherwise></c:choose>
            <pimsForm:radio name="isGroupOrResult" alias="Type" value="grouplevel" label="Group level" isChecked="${groupChecked}"/>
            <pimsForm:radio name="isGroupOrResult" alias="" value="setup" label="Set-up" isChecked="${setupChecked}"/>
            <pimsForm:radio name="isGroupOrResult" alias="" value="result" label="Result" isChecked="${resultChecked}"/>
        </pimsForm:column2>
    </pimsForm:formBlock>


    <%-- Choice of free entry or possible values, also default, and range for numbers --%>
    <c:choose><c:when test="${'Boolean'==paramType}">
        
        <%-- For boolean parameters, no need to support "possible values" -
             simple yes/no choice is enough --%>
        <pimsForm:formBlock>
            <pimsForm:column1>
	            <pimsForm:radio name="def" alias="Default value" value="yes" label="Yes" isChecked="${true}"/>
	            <pimsForm:radio name="def" alias="" value="no" label="No" isChecked="no"/>
            </pimsForm:column1>        
        </pimsForm:formBlock>
    
            
    </c:when><c:otherwise>
    
        <%-- Both text and number parameters can be either free entry or 
             restricted to "possible values" --%>
            <c:set var="freechecked"></c:set>
            <c:set var="freehide">hidden</c:set>
            <c:set var="restrictedchecked">checked="checked"</c:set>
            <c:set var="restrictedhide"></c:set>
	        <c:if test="${empty possibleValues}">
	            <c:set var="freechecked">checked="checked"</c:set>
	            <c:set var="freehide"></c:set>
	            <c:set var="restrictedchecked"></c:set>
	            <c:set var="restrictedhide">hidden</c:set>
	        </c:if>
        <pimsForm:formBlock>
                <h4><label><input type="radio" name="restrictEntry" value="yes" ${freechecked} onclick="toggleFreeAndRestrictedValues('free')">Allow free entry of values</label></h4>
        </pimsForm:formBlock>
        <pimsForm:formBlock id="freeentry" extraClasses="subsection ${freehide}">
            <c:choose><c:when test="${'String'==paramType}">

                <%-- For string params, only need "Default value" --%>
                <pimsForm:column1>
                <pimsForm:text name="default" alias="Default value" value="${defaultValue}" />
                </pimsForm:column1>

            </c:when><c:otherwise>
                <%-- For number parameters, need Default value plus 
                     min, max and is/isn't whole number
                --%>
                <pimsForm:column1>
                    <pimsForm:text name="default" alias="Default value"  validation="numeric:true" />
                </pimsForm:column1>
                <pimsForm:column2>
                    <pimsForm:text name="min" alias="Minimum value" value="${minValue}" validation="numeric:true" />
                    <pimsForm:text name="max" alias="Maximum value" value="${minValue}" validation="numeric:true" />
                    <pimsForm:checkbox name="isInt" isChecked="${'Int'==paramType}" label="Restrict to whole numbers" onchange="toggleIntRequired(this)" />
                
                </pimsForm:column2>                     
            </c:otherwise></c:choose>

        </pimsForm:formBlock>
        <pimsForm:formBlock>
                <h4><label><input type="radio" name="restrictEntry" value="no" ${restrictedchecked} onclick="toggleFreeAndRestrictedValues('restricted')">Restrict entry to specific values</label></h4>
        </pimsForm:formBlock>
        <pimsForm:formBlock id="restrictedentry" extraClasses="subsection ${restrictedhide}">
                <pimsForm:column1>
                    <pimsForm:textarea name="allowed" alias="Allowed values" onkeyup="updatePossibleValues()">
                        ${possibleValues}
                    </pimsForm:textarea>
                    <script type="text/javascript">
                    $("allowed").defValue="${defaultValue}";
                    </script>
                </pimsForm:column1>
                <pimsForm:column2>
                    <pimsForm:nonFormFieldInfo label="Default value">
                    <div id="pvdefaults">
                    Enter possible values to the left, each one on its own line. Then select the default value here.
                    </div>
                    </pimsForm:nonFormFieldInfo>
                </pimsForm:column2>
        </pimsForm:formBlock>
    </c:otherwise></c:choose>

    <pimsForm:formBlock>
        <c:choose><c:when test="${empty pdef}">
            <input type="button" style="float:right" onclick="submitForm()" value="Create" />
        </c:when><c:otherwise>
            <input type="button" style="float:right" onclick="submitForm()" value="Save changes" />
        </c:otherwise></c:choose>
    </pimsForm:formBlock>

    <input type="hidden" name="initialParamType" value="${paramType}" />
</pimsForm:form>


<%-- 
This is the form that actually gets submitted, by the Javascript.
--%>
<div style="border:1px solid red;margin-top:2em;display:none;">
<h4>Real form, submitted by Javascript</h4>
<form id="realform" method="post" action="${pageContext.request.contextPath}${submitaction}">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,submitaction)}" />
<input name="${prefix}name" value="" />
<input name="${prefix}label" value="" />
<input name="${prefix}paramType" value="" />
<input name="${prefix}isMandatory" value="" />
<input name="${prefix}isGroupLevel" value="" />
<input name="${prefix}isResult" value="" />
<input name="${prefix}defaultValue" value="" />
<input name="${prefix}displayUnit" value="" />
<input name="${prefix}minValue" value="" />
<input name="${prefix}maxValue" value="" />
<input name="${prefix}possibleValues" value="" />
<input name="${prefix}protocol" value="${protocol.hook}" type="hidden" />
<c:if test="${empty pdef}">
    <input type="hidden" name="METACLASSNAME" value="org.pimslims.model.protocol.ParameterDefinition"/>
    <input type="hidden" name="_OWNER"  value="${protocol.access.name}" />
</c:if>
</form>
</div>

<jsp:include page="ExistingSamplesAndParams.jsp"/>

<script type="text/javascript">
$("name").oldValue="${name}"; //mess of JSP and Prototype structure here! Assigns existing name onto name field as an attribute "oldValue"

function toggleFreeAndRestrictedValues(which){
    if(which=="free"){
        $("freeentry").removeClassName("hidden");
        $("restrictedentry").addClassName("hidden");
        $("freeentry").select("input,textarea").each(function(field){
            field.disabled="";
        });
        $("restrictedentry").select("input,textarea").each(function(field){
            field.disabled="disabled";
        });
    } else {
        $("freeentry").addClassName("hidden");
        $("restrictedentry").removeClassName("hidden");
        $("freeentry").select("input,textarea").each(function(field){
            field.disabled="disabled";
        });
        $("restrictedentry").select("input,textarea").each(function(field){
            field.disabled="";
        });
    }
}

function toggleIntRequired(intCheckbox){
    var box=$(intCheckbox);
    var newValue=box.checked;
    var frm=box.up("form");
    frm.down("#default").validation.wholeNumber=newValue;
    frm.down("#min").validation.wholeNumber=newValue;
    frm.down("#max").validation.wholeNumber=newValue;
}


//call the above, to initialise form.
if($("restrictedentry")){
    if($("dummyform").down("#isInt")){
        toggleIntRequired($("dummyform").down("#isInt"));
    }
}



function updatePossibleValues(){
	var txt=$("allowed");
	var def=txt.defValue; //default value
    var values=allowed.value.split("\n");
    var radios=[];
    var defaultChecked=false;
    values.each(function(v){ 
        v=v.strip();
        if(""==v){
            radios.push("&nbsp;");
        } else {
            var chk="";
            if(def==v){ 
                chk='checked="checked"';
                defaultChecked=true; 
                txt.defValue=v;
            }
            radios.push('<label><input type="radio" '+chk+' onclick="$(\'allowed\').defValue=this.value" name="pvdefault" value="'+v+'"/>'+v+'</label>');
        }
    });
    var output=radios.join("<br/>");
    $("pvdefaults").innerHTML=output;
    //if none checked, check the first
    if(!defaultChecked){
        var firstRad=$("pvdefaults").down("input");
        if(firstRad){
            firstRad.checked="checked";
            txt.defValue=firstRad.value;
        }
    }
    matchTextareaHeightToContent($("allowed"));
}


//initialise "possible values" field - separate by semicolon and place entries on own line
if($("allowed")){
    var allowed=$("allowed");
    var values=allowed.innerHTML.split(";");
    var newValues=[];
    values.each(function(val){
        newValues.push(val.strip());
    });
    allowed.innerHTML=newValues.join("\n");
    if(0<values.length){
            updatePossibleValues();
    } //otherwise leave instructions in place
}



function submitForm(){
    var dummy=$("dummyform");
    var realform=$("realform");
    if(!validateFormFields(dummy,true)){
        return false;
    }
    var def=dummy.down("#default");
    var min=dummy.down("#min");
    var max=dummy.down("#max");
    var msg="";
    if(null!=min){ //number parameter
        if(""==$("freeentry").style.display){
	        if(""!=min.value && ""!=max.value && 1*max.value<1*min.value){
	            msg+="\n* Minimum value cannot be greater than maximum value";
	        }
	        if(""!=def.value && ""!=max.value && 1*max.value<1*def.value){
	            msg+="\n* Default value cannot be greater than maximum value";
	        }
	        if(""!=def.value && ""!=min.value && 1*min.value>1*def.value){
	            msg+="\n* Default value cannot be less than minimum value";
	        }
        } else {
            //"possible values" validation
            //if no possible values, fail
            //for each entry, is it numeric? do isInt if needed
        }
    }

   	//copy to real form
     realform[prefix+"name"].value = dummy["name"].value;
     realform[prefix+"label"].value = dummy["label"].value;

     dummy.getInputs("radio","isMandatory").each(function(rad){
         if(rad.checked){
             realform[prefix+"isMandatory"].value = rad.value;
         }
     });

     if(dummy["initialParamType"].value=="Boolean"||dummy["initialParamType"].value=="String"){
         realform[prefix+"paramType"].value = dummy["initialParamType"].value;
     } else if(dummy["isInt"].checked){
         realform[prefix+"paramType"].value = "Int";
     } else {
         realform[prefix+"paramType"].value = "Float";
     }
       
     var type;
     dummy.getInputs("radio","isGroupOrResult").each(function(rad){
         if(rad.checked){
             type=rad.value;
         }
     });
     if(!type || "setup"==type){
         realform[prefix+"isGroupLevel"].value = "No";
         realform[prefix+"isResult"].value = "No";
     } else if("grouplevel"==type){
         realform[prefix+"isGroupLevel"].value = "Yes";
         realform[prefix+"isResult"].value = "No";
     } else {
         realform[prefix+"isGroupLevel"].value = "No";
         realform[prefix+"isResult"].value = "Yes";
     }

     if("Boolean"==dummy["initialParamType"].value){
         realform[prefix+"paramType"].value = "Boolean";
         var radios=dummy.select("input[name='def']");
         radios.each(function(rad){
        	   if(rad.checked){
            	   realform[prefix+"defaultValue"].value = rad.value;
        	   }
         });
         //obtain default from radio buttons
     } else {
        if(!$("freeentry").hasClassName("hidden")){
            /*
             * free entry
                */
                //default value
                realform[prefix+"defaultValue"].value = dummy["default"].value;
	        if("String"==dummy["initialParamType"].value){
                //param type
	            realform[prefix+"paramType"].value = "String";
	        } else {
		        //number param
                   //param type
                realform[prefix+"displayUnit"].value = dummy["displayUnit"].value;
                if(dummy["isInt"].checked){
                    realform[prefix+"paramType"].value = "Int";
                } else {
                    realform[prefix+"paramType"].value = "Float";
                }
                //min and max
                   realform[prefix+"minValue"].value = dummy["min"].value;
                   realform[prefix+"maxValue"].value = dummy["max"].value;
               }
	           realform[prefix+"possibleValues"].value = "";
        } else {
            /*
             * restricted entry
             */
             realform[prefix+"minValue"].value = "";
             realform[prefix+"maxValue"].value = "";

             //param type
               if("String"==dummy["initialParamType"].value){
                   realform[prefix+"paramType"].value = "String";
               } else {
                   realform[prefix+"paramType"].value = "Float";
               }

            //possible values
            var radios=$("pvdefaults").select("input");
            if(""==radios){
            	msg+="\n* If you choose to restrict entries, you must enter the allowed values";   
            } else {
	            var pvs=[];
                radios.each(function(rad){
	                var val=rad.value.strip();
                    pvs.push(val);
                    if(-1<val.indexOf(";")){
                        msg+="\n* "+val+" contains a ; (semi-colon), but this is not allowed in possible values";
                    }
                    if("String"!=dummy["initialParamType"].value && !isNumeric(val)){
                        msg+="\n* "+val+" is not numeric";
                    }
                    if(val==$("allowed").defValue){
                        realform[prefix+"defaultValue"].value = val;
                    }
                });
                realform[prefix+"possibleValues"].value = pvs.join(";");
            }
        }
    }
        
    if(""==msg){
        // submit real form here
        document.warnChanged=false;
        realform.submit();
        
    } else {
        msg="Please correct the following:\n"+msg;
        alert(msg);
        return false;
    }
}

</script>


<jsp:include page="/JSP/core/Footer.jsp">
    <jsp:param name="isInModalWindow" value='yes' />
</jsp:include>
