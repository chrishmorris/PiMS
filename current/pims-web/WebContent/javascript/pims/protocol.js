if(null==contextPath){
  var contextPath="";
}

function protocol_validatePdef(hook,name){
  var pname="";
  if(null!=name) pname=name;
  var error="";
  $(hook+":name").value = $(hook+":name").value.strip();
  if(""==$(hook+":name").value){ error+="\n* Name is required"; }
  var paramType=$(hook+":paramType").value;
  if("Float"==paramType || "Int"==paramType){ 
    if($("_"+hook+":isInt").checked) {
      $(hook+":paramType").value="Int";
    } else {
      $(hook+":paramType").value="Float";
    }
    var min=$(hook+":minValue").value;
    var max=$(hook+":maxValue").value;
    var def=$(hook+":defaultValue").value;
    var minOK=true,maxOK=true,defOK=true;
    if("Float"==$(hook+":paramType").value){
		if(""!=def && !isNumeric(def)){ error+="\n* Default must be numeric"; defOK=false; }	
		if(""!=min && !isNumeric(min)){ error+="\n* Minimum must be numeric"; minOK=false; }	
		if(""!=max && !isNumeric(max)){ error+="\n* Maximum must be numeric"; maxOK=false; }	
	} else { //it's an int
		if(""!=def && !(isNumeric(def) && Math.abs(def*1)==parseInt(Math.abs(def*1)))){ error+="\n* Default must be a whole number"; defOK=false; }	
		if(""!=min && !(isNumeric(min) && Math.abs(min*1)==parseInt(Math.abs(min*1)))){ error+="\n* Minimum must be a whole number"; minOK=false; }	
		if(""!=max && !(isNumeric(max) && Math.abs(max*1)==parseInt(Math.abs(max*1)))){ error+="\n* Maximum must be a whole number"; maxOK=false; }	
	}

	if(defOK && ""!=def) { def=1*def; } else { def=null; }
	if(minOK && ""!=min) { min=1*min; } else { min=null; }
	if(maxOK && ""!=max) { max=1*max; } else { max=null; }

	if(null!=max && null!=min && max<min){ error+="\n* Maximum cannot be less than minimum"; }
	if(null!=def && null!=min && def<min){ error+="\n* Default cannot be less than minimum"; }
	if(null!=def && null!=max && def>max){ error+="\n* Default cannot be more than maximum"; }
  }
  if(""==error){
    if($("_"+hook+":isInt")){ 
      $("_"+hook+":isInt").disabled="disabled";
    }
    return true;
  }
  if(""==pname){
    error="Please correct the following:\n"+error;
  } else {
    error="Cannot save because of problems with parameter '"+pname+"':\n"+error;
  }
  alert(error);
  return false;
}


/*
 * Handles checking/unchecking int checkbox on number parameters,
 * in the main protocol view.
 */
function protocol_setIsInt(hook){
  if($("_"+hook+":isInt").checked){
    $(hook+":paramType").value="Int";
  } else {
    $(hook+":paramType").value="Float";
  }
}

/*
 * PiMS 1341 possible value in protocol editor is not check for type
 */
function validatePossibleValues(type, textstring) {
  //alert("validatePossibleValues ["+type+":"+textstring+"]");
  var IsOK=true;
  var mysplit=textstring.split(";");
  if ("number"==type) { 
    for (var i=0;i<mysplit.length;i++) {
     
      if (!isNumeric(mysplit[i].strip())){
        IsOK = false;
        //alert("mysplit ["+mysplit[i]+"] of totoal "+mysplit.length);
      }
    }
    if (!IsOK) 
      alert("Possible values must be numeric");
  }
}

function stringAmount(text) {
 
  var basename = "";
  if (text.name.match("value"))
  	basename = text.name.substring(0, text.name.length-5);
  if (text.name.match("unit"))
  	basename = text.name.substring(0, text.name.length-4);

  var valuename = basename+"value";
  var unitname = basename+"unit";
  var amountname = basename+"amount";
  
  var value = "";
  var unit = "";
  for(i=0; i<text.form.elements.length; i++) {
    
	if (text.form.elements[i].name==valuename) 
	  value = text.form.elements[i].value;
	
	if (text.form.elements[i].name == unitname) 
	  unit = text.form.elements[i].value;
  }
  
  var amount = "";
  if (""==unit || "[No Units]"==unit) {alert('Please select the unit for the amount'); return false}
  
  amount=value+unit;
  
  for(i=0; i<text.form.elements.length; i++) {
	if (text.form.elements[i].name==amountname) 
	   text.form.elements[i].value = amount;
  }
  return true;
}

function protocol_checkNumOutputs(){
  var outputSampleDivs=$$("#outputSampleshead .protocol_paramsample");
  var count=0;
  for(i=0;i<outputSampleDivs.length;i++){
    os=outputSampleDivs[i];
    if(!os.hasClassName("ajax_deleted")){
      count++;
    }
  }
  if(1==count){
    $("plateExptLink").innerHTML='<a href="'+contextPath+'/CreatePlate?protocol='+protocolHook+'">Record a plate experiment using this protocol</a>';
  } else {
    $("plateExptLink").innerHTML='(Protocols for a plate experiment must have exactly one output.)';
  }
}

function protocol_checkNameUnique(val,alias) {
  val=val.strip();
  for(i=0;i<inputNames.length;i++){
    if(inputNames[i].toLowerCase()==val.toLowerCase()) {
      return "There is already an input sample called "+val+". Name cannot be the same as\n    any other input sample, output sample or parameter."
    }
  }
  for(i=0;i<outputNames.length;i++){
    if(outputNames[i].toLowerCase()==val.toLowerCase()) {
      return "There is already an output sample called "+val+". Name cannot be the same as\n    any other input sample, output sample or parameter."
    }
  }
  for(i=0;i<paramNames.length;i++){
    if(paramNames[i].toLowerCase()==val.toLowerCase()) {
      return "There is already a parameter called "+val+". Name cannot be the same as\n    any other input sample, output sample or parameter."
    } else if(0==val.indexOf("__") && 0==paramNames[i].indexOf("__")){
      return "A protocol can only have one score parameter."
    }
  }
  return "";
}





 


