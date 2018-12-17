
function startEdit(elem){
  var frm=toggleViewing(elem);
  //save all form values
  //enable all form fields
  var fieldsToWriteEnable=frm.select("textarea","input[type=text]");
  fieldsToWriteEnable.each(function(field){
    field.removeAttribute("disabled");
    field.removeAttribute("readonly");
    field.readOnly="";
    field.wasValue=field.value;
  });
  var selects=frm.select("select");
  selects.each(function (sel){
    sel.wasValue=sel.value;
  });
  var checkboxes=frm.select("input[type=checkbox]","input[type=radio]");
  checkboxes.each(function (chk){
    chk.removeAttribute("disabled");
    chk.wasChecked=chk.checked;
  });
  
  focusFirstField(frm);
}

/**
 * Sets the focus into the first form field in a given element.
 * 
 * @param elem an element containing the form fields
 * @return false if an exception is thrown
 */
function focusFirstField(elem){
	  var firstField=elem.down("input[type=text],select,textarea");
		if(!firstField || firstField.readOnly){ return; }
		try{
			firstField.focus();
		}catch(err){
			//oh well, it's not the end of the world - and it's
			//probably IE being stupid again
			return false;
		}
}

function cancelEdit(elem){
  dontWarn();
  var frm=toggleViewing(elem);
  //restore saved form values
  var fields=frm.select("textarea","select","input");
  fields.each(function(field){
  	if(null!=field.wasValue){
  		field.value=field.wasValue;
  	} else if (null!=field.wasChecked){
  		field.checked=field.wasChecked;
    }
  })
  //disable all form fields
  var fieldsToMakeReadOnly=frm.select("textarea","input[type='text']");
  fieldsToMakeReadOnly.each(function(field){
    field.setAttribute("readonly","readonly");
    field.readOnly="readonly";
  });
  var checkboxes=frm.select("input[type=checkbox]");
  checkboxes.each(function (chk){
    chk.setAttribute("disabled","disabled");
  });
  var radios=frm.select("input[type=radio]");
  radios.each(function (rad){
    rad.setAttribute("disabled","disabled");
    if(rad.checked){ 
      rad.addClassName("checked");
    } else {
      rad.removeClassName("checked");
    }
  });
}

function toggleViewing(elem){
  elem=$(elem);
  var frm=elem.up("form");
  frm.toggleClassName("viewing");
  return frm;
}