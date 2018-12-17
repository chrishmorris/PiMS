
var validationsToAdd;

/**
 * Adds the field and its required checks to be checked on submit.
 * Believed obsolete.
 * @param field  The field needing validation, or its ID
 * @param checks A javascript object containing the validations
 */
function addToValidation(field, checks){
  if(!validationsToAdd) { validationsToAdd=new Array(); }
  checks["field"]=field;
  validationsToAdd[validationsToAdd.length]=checks;
}

function setUpValidationOnLoad(){
  validationsToAdd.each(function (item){
	item["field"]=$(item["field"]);
    frm= item["field"].up("form");
    if(!frm) {
      alert("Tried to add validation to field '"+item["field"].id+"', but no parent form element");
      return false;
    }
    item["form"]=frm; 
    writeExampleText(item);
/*
    var field=validationsToAdd[i];
    field=$(field); //field is now the element, extended with Prototype
    frm= field.up("form");
    if(!frm) alert("Tried to add validation to field '"+field.id+"', but no parent form element");
*/
  });
}

function writeExampleText(validations){
  field=validations["field"];
  example=field.up(".formitemblock").down(".example");
  if(!example || ""!=example.innerHTML.strip()) return false;
  txt="";
  if(validations["required"] && true==validations["required"]) { txt+="Required. " }
  //numbers
  if(validations["javaInt"]) {
    validations["minimum"]=-2147483648;
    validations["maximum"]=2147483647;
    validations["wholeNumber"]=true;
  } else if(validations["javaLong"]) {
    validations["minimum"]=-9223372036854775000;
    validations["maximum"]=9223372036854775000;
    validations["wholeNumber"]=true;
  }
  if(validations["wholeNumber"] || validations["numeric"] || validations["minimum"] || validations["maximum"]) { 
    if(validations["wholeNumber"] && true==validations["wholeNumber"]){
      txt+="Whole number";
    } else {
      txt+="Number";
    }
    if(validations["minimum"] && validations["maximum"]){
      txt+=", " +validations["minimum"] +" to "+ validations["maximum"] +". ";
    } else if(validations["minimum"]) {
      txt+=", minimum "+validations["minimum"]+". ";
    } else if(validations["maximum"]) {
      txt+=", maximum "+validations["maximum"]+". ";
    } else {
      txt+=". ";
    }
  }
  example.innerHTML=txt;
}
