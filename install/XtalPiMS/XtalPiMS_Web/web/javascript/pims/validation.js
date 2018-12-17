/**************************************

NOTE: You probably won't need to call any of the functions in this file directly
      (apart from validateFormFields if you aren't using the form widgets). Read
      the following for details on how to validate your forms.
      

Validation available
--------------------
The following are the available validation checks:

::NON-EMPTY VALIDATION::
required:true
requireIfOtherNotEmpty:FIELD_ID
mutuallyRequire:FIELD_ID

::STRING VALIDATION::
dnaSequence:true
proteinSequence:true
dnaOrProteinSequence:true
hyperlink:true
emailAddress:true
holderPosition:true
rowPosition:true
date:true
unique:{obj:'HOOK',prop:'PROPERTY_NAME'}   Added by Peter
alphaNumeric:true (including . and - symbols)

::NUMBER VALIDATION::
numeric:true
wholeNumber:true
javaInt:true
javaLong:true
minimum:NUMBER
maximum:NUMBER

::CUSTOM VALIDATION::
custom:FUNCTION

Notes: 
1. Only specify the validation you need. If the validated field is optional, for example,
   there is no need to specify required:false.
2. If you specify wholeNumber, minimum or maximum validation, numeric:true is implied - you
   don't need to state it explicitly.
3. If you specify javaInt or javaLong, the minimum, maximum and wholeNumber checks are
   implied.   
   

Calling validation on form submission
-------------------------------------
You must do two things in order to validate a form:

1) Ensure that the validator is called when the form is submitted
2) Attach validation to the field(s) that need it

Attaching validation to a given element does NOT ensure that the form will be validated
onsubmit. (This avoids overriding existing onsubmit behaviour.)

Strongly recommended:

    Use the pimsForm:form widget, which includes the call to validateFormFields.

Alternative:

   Attach an onsubmit handler to the form, OR an onclick handler to the submit button. Don't
   do both.

       <form onsubmit="return validateFormFields(this)" ... > (recommended)

       <input type="submit" onclick="return validateFormFields(this)" ... /> (alternative)

    Note that the event handler must return the result of validateFormFields. Without this
    return, the validation will be performed, but the browser won't know whether it succeeded 
    and the form will submit regardless.
 

Attaching validation to form elements (using pimsForm widgets)
--------------------------------------------------------------
IMPORTANT: Merely attaching validation to an element does NOT automatically ensure that
           the validation will be performed. You need to call the validation, as described
           above.

Give your widget tag a validation attribute, as shown: 

    <pimsForm:text name="myField" alias="My Field" 
      validation="required:true, wholeNumber:true, minimum:0" />
 
Note that the validation is supplied as a COMMA-SEPARATED list of NAME:VALUE PAIRS. The supplied
validation string is used to create a Javascript object.
 
WRONG: validation="required"
RIGHT: validation="required:true"
  
  
Attaching validation to form elements (non-widget)
--------------------------------------------------
IMPORTANT: Merely attaching validation to an element does NOT automatically ensure that
           the validation will be performed. You need to call the validation, as described
           above.

Write the HTML for the element in the usual way:

    <input type="text" name="myField" id="myField" />

The field MUST have an ID. In order to comply with XHTML standards (not to mention common sense), 
the name and ID must be identical.
 
Then, add a script block to attach the validation:

    <script type="text/javascript">
    attachValidation("myField", {required:true, wholeNumber:true, minimum:0, alias:"My field"} );
    </script>

Unlike when using widgets, you must supply a whole Javascript object, complete with enclosing
braces. See above for guidance on formatting the object.

You should also supply an "alias", a user-friendly name for the field. This is used in error 
messages pertaining to the field; if you don't supply one, the user will see the (often
distinctly unfriendly) field ID instead.

Notes:
1. The script block must come after the field in question - the field must exist before
   you can attach validation to it, or a Javascript error will result. The likely 
   effect is that the individual field will not be validated. For mutuallyRequire
   validation, BOTH fields must already exist.
2. Naturally, you can do the attachValidation calls for all fields in one script block at 
   the end (and this will render marginally faster, by reducing the switches back and forth
   between the HTML renderer and the Javascript interpreter). When iterating through fields 
   and generating the validation automatically, it may be easier just to have a script block 
   after each form field.


Custom validation functions
---------------------------
You can attach a custom validation function for cases not covered by the standard 
functions. This function can be called in addition to, or instead of, any of the standard
functions.

Your function must take two parameters:
val - the value of the field in question, and
alias - the user-friendly name to be reported in error messages

and return:
"" (empty string) if the field validates, or
STRING an explanatory error message if the field does not validate.

The function should be defined in an external .js file. DO NOT write it all out in the
validation object, unless it's a single statement. Even then, for maintainability and
reusability reasons it's probably better in an external file.

Example of custom validation function:

    function isDisgustingColour(val,alias){
      if("lime green"==val || "fluorescent purple"==val){
        return ""; //success, no error message needed
      }
      return alias+" must be lime green or fluorescent purple!" //failure, explain
    }

Using a custom validation function (pimsForm widgets):

    <pimsForm:text name="colorOfKitten" alias="Colour"
      validation="required:true, custom:function(val,alias){return isDisgustingColour(val,alias)}" />
 
Using a custom validation function (non-widget form fields):
 
    <div>Colour: <input type="text" name="colorOfKitten" id="colorOfKitten" /><div>
    <script type="text/javascript">
      attachValidation("colorOfKitten", {
                    required:true,
                    custom:function(val,alias){return isDisgustingColour(val,alias)},
                    alias:"Kitten" } );
    </script>


* 
* End of documentation - validation functions follow 
* 
***************************************/

/*
 * Try to save the user from losing updates
 */


/**
 * Iterates through all inputs in the form, checks their values against their attached
 * validation objects, and alerts any errors.
 *
 * @param element Either the form element itself, or an input within the form (e.g., the
 *                submit button). This gives you the choice of calling this function with
 *                <form onsubmit="return validateForm(this)" ...>
 *                or
 *                <input type="submit" onclick="return validateForm(this)" ... />
 * @param suppressButtonDisabling If set true, buttons in the form will not be greyed out
 * 				  when the form is submitted. Usually, you want them to be greyed out, so
 * 				  don't specify this at all. See PIMS-3308 for background.
 * @return true if all validated fields are valid, false otherwise.
 */
function validateFormFields(element, suppressButtonDisabling){
  var errMsg="";
  var failedFields=new Array(); //we'll style these as invalid later
  var passedFields=new Array(); //we'll style these as valid later
  var frm = element;
  if(element.form) frm=element.form;
  var inputs=Element.extend(frm).getElements();
  //iterate through inputs
  inputs.each(function(input){

	if(input.disabled){return;}  //ignore disabled fields
    if(input.type!="file"){
      input.value=input.value.strip(); //remove leading/trailing whitespace
    }
    var validation=input.validation;
    if(null==validation) return; //go on to next field
    var val=input.value;
    var thisInputErrMsg="";
    if(validation.javaInt){
      validation.wholeNumber=true;
      validation.minimum=-2147483648;
      validation.maximum=2147483647;
    } else if (validation.javaLong){
      validation.minimum=-9223372036854775000;
      validation.maximum=9223372036854775000;
      validation.wholeNumber=true;
    }

    //add a default alias if not set
    if(!validation.alias) validation.alias = input.id;

    var alias=validation.alias.unescapeHTML();
    
    if(null!=validation.minimum||null!=validation.maximum||validation.wholeNumber){
      validation.numeric=true;
    }
    if(""==val && validation.required){
      thisInputErrMsg="\n* "+alias+" is required."
    } else if(validation.requireIfOtherNotEmpty && ""==val) {
      for(i=0;i<validation.requireIfOtherNotEmpty.length;i++){
        field=$(validation.requireIfOtherNotEmpty[i]);
        if(""!=field.value){
          var otherFieldAlias=field.id;
          if(validation.otherAlias) { otherFieldAlias=validation.otherAlias.unescapeHTML(); }
          if(field.validation&&field.validation.alias) { otherFieldAlias = field.validation.alias; }
          thisInputErrMsg="\n* "+alias+" is required, because "+otherFieldAlias+" is not empty.";
        }
      }
    } 
    // Alphanumeric validation 
    else if(validation.alphaNumeric) { 
    	if(!isAlphaNumeric(val)){
            thisInputErrMsg="\n* "+alias+" must only contain numbers or characters from a to z."
          }
    } else if(validation.dnaSequence || validation.proteinSequence || validation.dnaOrProteinSequence) {
    	//todo we only need to check Protein for validation.dnaOrProteinSequence
      if(validation.dnaOrProteinSequence && !isValidProteinSequence(val) && !isValidDnaSequence(val)){
        thisInputErrMsg="\n* "+alias+" must be a valid DNA or protein sequence."
      } else if(validation.proteinSequence && !isValidProteinSequence(val)){
        thisInputErrMsg="\n* "+alias+" must be a valid protein sequence."
      } else if(validation.dnaSequence && !isValidDnaSequence(val)){
        thisInputErrMsg="\n* "+alias+" must be a valid DNA sequence: only ACGT accepted."
      }
    } else if(validation.hyperlink &&""!=val && !isValidHyperlink(val)){
        thisInputErrMsg="\n* "+alias+" must be a valid hyperlink."
    } else if(validation.emailAddress &&""!=val && !isValidEmail(val)){
        thisInputErrMsg="\n* "+alias+" must be a valid email address."
    } else if(validation.date &&""!=val && !isValidDate(val)) {
    	//TODO no
          thisInputErrMsg="\n* "+alias+" not a valid date:"+val;
    } else if(validation.holderPosition &&""!=val && !isValidHolderPosition(val)) {
          thisInputErrMsg="\n* "+alias+" must be a valid holder position - A1, H12 are OK, A01 is not.";
    } else if(validation.rowPosition &&""!=val && !isValidRowPosition(val)) {
        thisInputErrMsg="\n* "+alias+" must be a valid row position - A-H are OK";
    } else if(validation.numeric &&""!=val){
      //numeric checks
      var valueIsNumeric=isNumeric(val);
	  var valueIsWholeNumber=isWholeNumber(val);
	  if(validation.wholeNumber && !valueIsWholeNumber){
		thisInputErrMsg+=" a whole number";
	  } else if (validation.numeric &&!valueIsNumeric){
	    thisInputErrMsg+=" a number";
	  }
	  if((null!=validation.minimum && null!=validation.maximum) && (!valueIsNumeric || val*1<validation.minimum*1 || val*1 > validation.maximum*1)){
	    thisInputErrMsg+=" between "+validation.minimum+" and "+validation.maximum;
	  }else if(null!=validation.minimum && (!valueIsNumeric || val*1 <validation.minimum*1)){
		thisInputErrMsg+=" not less than "+validation.minimum;
	  } else if(null!=validation.maximum && (!valueIsNumeric || val*1 >validation.maximum*1)){
		thisInputErrMsg+=" not greater than "+validation.maximum;
	  }
	  if(""!=thisInputErrMsg) {
		thisInputErrMsg="\n* "+alias+ " must be"+thisInputErrMsg+".";
	  }
      //end numeric checks
    }
    if(validation.sameAs){
    	var otherField=frm[validation.sameAs];
    	var otherValue=otherField.value;
    	
    	var otherFieldName=otherField.name;
    	if(undefined!=otherField.validation && undefined!=otherField.validation.alias){
    		otherFieldName=otherField.validation.alias;
    	}
    	if(val!=otherValue){
    		thisInputErrMsg+="\n* "+alias+ " must be the same as "+otherFieldName+".";
    	}
    }
    if(validation.custom){
      var customError=validation.custom(val,alias);
      if(""!=customError){
		thisInputErrMsg+="\n* "+customError;
      }
    }
    if(""==thisInputErrMsg){
      passedFields.push(input);
    } else {
      failedFields.push(input);
      errMsg+=thisInputErrMsg;
    }
  }); 
  //end iterate
  failedFields.each(function(field){
    field.addClassName("invalidformfield");
    field.isInvalid=true;
  });
  passedFields.each(function(field){
    field.removeClassName("invalidformfield");
    field.isInvalid=false;
  });
  
  if(""==errMsg){
	  if(!suppressButtonDisabling){
		  setTimeout(function(){ disableFormElements(frm)},100);
	  }
	  return true;
  }

  alert("Please correct the following:\n"+errMsg);
  failedFields[0].focus();
  return false;
}

/*******************************************************
Functions below here are to support validateFormFields
- you probably won't need to call them directly.
*******************************************************/

/**
 * Disable all button/submit elements in the form. To be called after submit - 
 * calling before submit causes a blank submit or no submit at all. Use 
 * setTimeout as follows:
 * 
 * setTimeout(function(){ disableFormElements(frm)},10);
 * 
 * @param frm The form element
 */
function disableFormElements(frm){
	frm.select("input,select").each(function(elem){
		if(elem.type=="button"||elem.type=="submit"){
			elem.disabled="disabled";
			if("Create"==elem.value){
				elem.value="Creating..."
			} else if("Save changes"==elem.value){
				elem.value="Saving..."
			}
		}
	});
}

/**
 * Replace existing validation call with one that specifies whether to suppress button disabling onsubmit. 
 * Needed in cases where submittting through one button should disable buttons but not in another, or otherwise
 * to override this default behaviour. "Get Spreadsheet" on Create Plate is an example.
 * 
 * @param frm either the form element or the form's ID
 * @param suppression true|false Whether to suppress the 
 * See PIMS-3308 for background.
 */
function suppressButtonDisablingOnForm(frm,suppression){
  frm=$(frm);
  frm.onsubmit=function(){return validateFormFields(this,suppression)};
}


/**
 * Checks whether stringToTest is numeric
 *
 * Allows 3; -3; -.3; -0.5; 0.5; 0,5 
 * Disallows .5, , 3., -3.
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is numeric, false otherwise
 */
function isNumeric(stringToTest) {
  regEx = /^\-?[0-9]{1,}[,\.]?[0-9]*$/;
  if(regEx.test(stringToTest)) {
    return true;
  }
  //still here? see if there's an exponent in it
  regEx = /^\-?[0-9]{1,}[,\.]?[0-9]*[e|E]\-?[0-9]+$/;
  return regEx.test(stringToTest);
}

/**
 * Checks whether stringToTest is a whole number
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is a whole number, false otherwise
 */
function isWholeNumber(stringToTest){
  if(!isNumeric(stringToTest)) return false;
  if(Math.abs(stringToTest*1) != parseInt(Math.abs(stringToTest*1))) return false;
  return true;
}

/**
 * Checks whether stringToTest is a alphanumeric only 
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is an alphanumeric, false otherwise
 */
function isAlphaNumeric(stringToTest){
	var numaric = stringToTest;
	for(var j=0; j<numaric.length; j++)	{
		  var alphaa = numaric.charAt(j);
		  var hh = alphaa.charCodeAt(0);
		  if((hh > 47 && hh<58) || (hh > 64 && hh<91) || (hh > 96 && hh<123) || hh==45 || hh==46)  {
		  // do nothing
		  }
		else {
			 return false;
		  }
	}
 return true;
}

/**
 * Checks whether stringToTest is a valid protein sequence
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is empty or a valid protein sequence, false otherwise
 */
function isValidProteinSequence(stringToTest) { //If this function finds any characters that are not in the allowed list, it will return false	
	//can't be anything wrong with it if it's empty
	if(null==stringToTest || ""==stringToTest) return true;

	//this block catches three-letter sequences like "Val Phe Glu Ile Ala Lys" -
	//unfortunately cleanSequence() munges it all into one unbroken string before
	//we get in here, so this never fires
	/*Commented out as it does fire if the sequence is only 3 residues long which 
	//may be the case for N- and C- terminal tags
	var threeLetterGroup = new RegExp("\\w\\w\\w\\s+","mg");
	s = stringToTest.replace(threeLetterGroup, "");
	if (""==s || new RegExp("^\\w\\w\\w$").test(s) ) {
	    alert("Sorry, PIMS does not currently accept three letter code");
	    return false;
	}*/
	
	var whiteSpace = new RegExp("\\s", "mg");
	s = stringToTest.replace(whiteSpace, "");	
	var reg = new RegExp("^[ABCDEFGHIKLMNPQRSTVWYXZabcdefghiklmnpqrstvwyxz]*$");
	return reg.test(s);
}

/**
 * Checks whether stringToTest is a valid DNA sequence
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is empty or a valid DNA sequence, false otherwise
 */
function isValidDnaSequence(stringToTest) {	//If this function finds any characters that are not in the allowed list, it will return false
	var whiteSpace = new RegExp("\\s", "mg");
	s = stringToTest.replace(whiteSpace, "");
	//var reg = new RegExp("^[ACGTRYMKSWBDHVNacgtrymkswbdhvn]*$");  only recognise ACGT see PIMS-2136
	var reg = new RegExp("^[ACGTacgt]*$");
	return reg.test(s);	
}

/**
 * Checks whether stringToTest is a valid hyperlink
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is empty or a valid hyperlink, false otherwise
 */
function isValidHyperlink(stringToTest) {
	var reg = new RegExp("http://\\S+");
	if (reg.test(stringToTest)) {
		return true;
	} else {
		return false;
	}	
}

/**
 * Checks whether stringToTest is in a valid format for an email address
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is empty or in a valid email address format, false otherwise
 */
function isValidEmail(stringToTest) {
  regEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return regEx.test(stringToTest);
}

/**
 * Returns true if the format of stringToTest looks like a date
 * in the format DD/MM/YYYY.
 *
 * @param stringToTest The value to check
 * @return true if stringToTest is in the format NN/NN/NNNN, false otherwise
 */
function isDateFormat(stringToTest) {
  regEx = /^[0-3][0-9]\/[0-1][0-9]\/20[0-9][0-9]$/i;
  if(regEx.test(stringToTest)) {
    return true; 
  } else {
    regEx = /^[0-3][0-9]\/[0-1][0-9]\/19[0-9][0-9]$/i;
    return regEx.test(stringToTest);
  }
}


/**
 * Returns true if the format of stringToTest evaluates to a timestamp since epoch
 * 
 * @param stringToTest The value to check
 * @return true if stringToTest is a valid date  
 */
function isValidDate(n) {
  return  !isNaN(parseInt(n)) && isFinite(n);
}

/**
 * Returns "" if the supplied value represents a date equal to or
 * after the date value in the other field, otherwise an error message.
 * 
 * Note that this function must be called using the custom validator mechanism:
 * 
 * custom:function(val,alias){ return isNotBeforeOtherDate(val,alias,'startDate','Start date') }
 *
 * that it must be attached to the LATER date (i.e., end date not start date), and 
 * that it does NOT throw an error if either date is invalid. You MUST use
 * the date:true validation on both fields, and, for safety, ensure that the attachValidation
 * call for the earlier date field comes before that for the later one.
 * 
 * @param val                 The value of the field to be validated
 * @param alias               The user-friendly name of that field
 * @param otherDateFieldId    The ID of the other date field
 * @param otherDateFieldAlias The user-friendly name of that other field
 * @return an error message if either date field is invalid (this should have 
 *         been caught by earlier date:true validation); 
 *         "" if this date is equal to or after the other date;
 *         "" if either date field is empty;
 *         an error message string if this date is before the other date
 */
function isNotBeforeOtherDate(val,alias,otherDateFieldId,otherDateFieldAlias){
  var startDate=$(otherDateFieldId).value;
  var endDate=val;
  if(""==endDate||""==startDate){
  	return "";
  }
  if(!isValidDate(endDate) || !isValidDate(startDate)) { 
    //should already have been nailed by date validation, no need for another error
    return alias+" must not be before "+otherDateFieldAlias+", but one or both is invalid."; 
  }  
  if (parseInt(startDate)>parseInt(endDate)){
    return alias+" must not be before "+otherDateFieldAlias+".";
  }
  return "";
}

/**
 * Returns true if the format of stringToTest evaluates to a 
 * valid holder position 
 * 
 * @param stringToTest The value to check
 * @return true if stringToTest is a valid holder position, false otherwise
 */
 function isValidHolderPosition(string) {
	if(string.length < 2) return false;
	var reg = new RegExp("^[A-Pa-p][0-9][0-9]*$");
	if(! reg.test(string)) {
		return false;
	}
	return true;
}
 
 /**
  * Returns true if the format of stringToTest evaluates to a 
  * valid row position 
  * 
  * @param stringToTest The value to check
  * @return true if stringToTest is a valid holder position, false otherwise
  */
  function isValidRowPosition(string) {
 	if(string.length != 1) return false;
 	var reg = new RegExp("^[A-Pa-p]");
 	if(! reg.test(string)) {
 		return false;
 	}
 	return true;
 }