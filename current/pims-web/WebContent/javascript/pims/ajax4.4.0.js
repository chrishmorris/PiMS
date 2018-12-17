/*
 * Common AJAX functions
 */

var ajaxErrors=new Array();
/*
 * returns true if the AJAX request xmlhttp is in
 * progress, false otherwise.
 */ 
function callInProgress (xmlhttp) { 
	switch (xmlhttp.readyState) {
		case 1: case 2: case 3:
			return true;
			break;
		// Case 4 and 0
		default:
			return false;
			break;
	}
}
function showTimedOutMessage() {
	alert('There was a problem communicating with the server, and the request timed out.\nWhen you click "OK", the page will reload.');
	document.location.reload();	
}

/* Register global responders that will occur on all 
 * AJAX requests
 * These include timeouts
 */
Ajax.Responders.register({
	onCreate: function(request) {
		request['timeoutId'] = window.setTimeout( function(){
			if (callInProgress(request.transport)) {
				request.transport.abort();
				showTimedOutMessage();
			}
		}, 6000000); //shipments can take a long time. Upped this from a minute to something ridiculous.
	},
	onComplete: function(request) {
		// Clear the timeout, the request completed ok
		window.clearTimeout(request['timeoutId']);
	}
});


/*
 * Takes an object of name-value pairs, url-escapes values, concatenates
 * into querystring. 
 *
 * { name:"a value" } >>> &name=a%20value
 */
function makePostBody(parameters){   
  var postbody=''; // "isAjax=true";
  for(param in parameters){
    postbody+="&"+ param +"="+ escape(parameters[param]);
  }
  return postbody;
}  


/*
 * call this first, in every success handler.
 * LATER wrap new Ajax.Request into function
 */
function ajax_checkStillLoggedIn(transport) {
  if(null!=transport.responseText && -1!=transport.responseText.indexOf("j_security_check")) {
	message="Your last action couldn't be completed.\n\n";
	message+="You are no longer logged in to PiMS. This could have happened if you didn't ";
	message+="use PiMS for a while, or if you logged out from another browser window.\n\n";
	message+="When you click \"OK\" below, you will be taken to the login page.";
	alert(message);
	document.location.reload();	
  } else {
    return true;
  }
}



/* not in use yet:
function ajax_update(clickedIcon, parameters, report){
  // could $(clickedIcon).src=contextPath+"/skins/default/images/icons/actions/waiting.gif";
	
  //make the request
  new Ajax.Request(contextPath+"/update/AjaxUpdate", {
	method:"post", 
	// TODO requestHeaders: {Accept: 'application/json'},
	postBody:"_csrf="+ajaxUpdateCSRF+makePostBody(parameters),
	onSuccess:function(transport){ 
      ajax_checkStillLoggedIn(transport);
      var json = transport.responseText.evalJSON();
      new Hash(json).each(function(object) {
        new Hash(object.value).each(function(changed) {
          var key = object.key+":"+changed.key;
          if (null!==$(key)) {
            $(key).value = changed.value;
          } else {
            alert(key+"="+changed.value);
          }
        });
      });
      if (report) {report(clickedIcon);}
	},
	onFailure:function(transport){ 
		//check for "expected" exceptions
        if(-1!=transport.responseText.indexOf("Permission denied") ){
          alert("You do not have permission to update that.");
          return;
        }
        //default failure handling;
        ajax_onFailure(transport);
	}, 
	onCreate: function(transport) {
      transport.clickedIcon=clickedIcon;
      transport.timeoutId = window.setTimeout( function(){
        if (callInProgress(transport)) {
          transport.abort();
          showTimedOutMessage();
		}
      }, 10000); //10-second timeout
	},
	onComplete: function(request) {
      // Clear the timeout, the request completed ok
      window.clearTimeout(request.timeoutId);
	}
  });
} //end ajax_update */

/*
 * Called onclick of a pims:dustbin delete icon.
 *
 * Works up the document tree from the icon until it finds an element of class
 * ajax_deletable, assumes that its ID is a hook, and does an AJAX delete of
 * that hook.
 * 
 * @param clickedIcon The element clicked (not its ID - "this" in the element context)
 * @param options A Javascript object specifying additional options. Currently supported:
 *                cleanup - An optional function to be called after the standard AJAX delete
 *                          success handler. If this function takes an argument, clickedIcon
 *                          will be passed in.
 *                LATER override the default handler with a supplied function
 */
function ajax_delete(clickedIcon,options){
  var clickedIcon=clickedIcon;
  if(!confirm("Are you sure you want to delete? This cannot be undone.")){
	return false;
  }
  var hook=Element.extend(clickedIcon).up(".ajax_deletable").id;
  if(!hook || !$(hook)){ alert("Can't delete - no object of class ajax_deletable");return false; }
  $(clickedIcon).onclick="";
  $(clickedIcon).src=contextPath+"/skins/default/images/icons/actions/waiting.gif";
  
  //disable all inputs and selects inside $(hook)
  var fields = $(hook).getElementsBySelector("input", "textarea", "select");
  fields.each(function(field){
  	field.disabled="disabled";
  });

  //make the request
  new Ajax.Request(contextPath+"/Delete", {
	method:"post",
	postBody:"isAJAX=true&hook="+hook+"&_csrf="+ajaxDeleteCSRF,
	onSuccess:function(transport){ 
	  //do the standard after-delete actions, then any additional cleanup
	  ajax_delete_onSuccess(transport);
	  if(options && options["cleanup"]){ 
		  options["cleanup"](clickedIcon); 
	  } else if(clickedIcon.up(".collapsiblebox").afterDelete) {
		  clickedIcon.up(".collapsiblebox").afterDelete(clickedIcon);
	  }
	},
	onFailure:function(transport){ 
      transport.clickedIcon=clickedIcon;	
	  ajax_delete_onFailure(transport); 
	}, 
	onCreate: function(transport) {
	  transport.clickedIcon=clickedIcon;
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 10000); //10-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });
} //end ajax_delete


function ajax_delete_onFailure(transport){
  if(transport.clickedIcon){
    transport.clickedIcon.src=contextPath+"/images/icons/actions/delete_no.gif";
  }
  //check for "expected" exceptions
  if(-1!=transport.responseText.indexOf("you are not allowed to delete")
		||   -1!=transport.responseText.indexOf("Permission denied")
  ){
    alert("You do not have permission to delete that.");
    return;
  }
  if(-1!=transport.responseText.indexOf("cannot currently be deleted")){
    alert("That record can't be deleted, because it is linked to another.");
    return;
  }
  
  //default failure handling;
  ajax_onFailure(transport);
  
}

/*
 * The default AJAX delete success handler.
 * @param transport The AJAX request object
 */
function ajax_delete_onSuccess(transport) {
  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var xmlAttrs=docRoot.getElementsByTagName("object");
  var hooks=new Array();
  for(var i=0;i<xmlAttrs.length;i++) {
    var attr=xmlAttrs[i];
	var hook=attr.getAttribute("hook");
    hooks[hooks.length]=hook;
  }	
  for(var i=0;i<hooks.length;i++){
	var hook=hooks[i];
	if($(hook+"_editicons")){
	  $(hook+"_editicons").style.display="none";
	}

    $(hook).style.backgroundColor="yellow";
    setTimeout(function(){ $(hook).style.display="none"; },250);
    setTimeout(function(){ $(hook).remove(); },500);

	Element.addClassName(hook,"ajax_deleted");
  }
} //end ajax_delete_onSuccess

/*
 * The default AJAX failure handler.
 * @param transport The AJAX request object
 */
function ajax_onFailure(transport) {
  errwin=window.open("");
  errwin.document.write(transport.responseText);
  errwin.document.close();
}
// Custom AJAX delete use in Leeds sequence management 
function ajax_custom_delete(clickedIcon,url,hook) {
  var clickedIcon=clickedIcon;
  if(!confirm("Are you sure you want to delete? This cannot be undone.")){
		return false;
  }

  if(!hook || !$(hook)){ alert("Can't delete - no object of class ajax_deletable");return false; }
  $(hook+"_deleteicon").onclick="";
  $(hook+"_deleteicon").src=contextPath+"/skins/default/images/icons/actions/waiting.gif";
  
  //make the request
  new Ajax.Request(contextPath+url, {
	method:"post",
	postBody:"isAJAX=true&hook="+hook,
	onSuccess:function(transport){ 
	  //do the standard after-delete actions, then any additional cleanup
	  ajax_delete_onSuccess(transport);
	  $(hook+"_deleteicon").src=contextPath+"/images/icons/actions/delete_no.gif";
	},
	onFailure:function(transport){ 
      transport.clickedIcon=clickedIcon;	
	  ajax_delete_onFailure(transport); 
	}, 
	onCreate: function(transport) {
	  transport.clickedIcon=clickedIcon;
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 30000); //30-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });
} //end ajax_delete


function checkUnique(field){
    if(!field.validation || !field.validation["unique"]) { return false; }
    if(!field.validation["unique"]["obj"] || !field.validation["unique"]["prop"]) { return false; }
    
    //no need to check, if it's the original (pre-edit) value
    if(field.wasValue && field.wasValue==field.value){ 
        field.removeClassName("invalidformfield");
    	return false;
    }
    
   //    ajax_exists(field,field.validation["unique"]["obj"],field.validation["unique"]["prop"]);
    var object=field.validation["unique"]["obj"];
    var property=field.validation["unique"]["prop"];
    new Ajax.Request(contextPath+"/read/AjaxExists/"+object, {
    method:"get",
    parameters:property+"="+field.value,
    onSuccess:function(transport){ 
      checkUnique_onSuccess(transport, field, object);
    },
    onFailure:function(transport){ 
      ajax_onFailure(transport); 
    }, 
    onCreate: function(transport) {
      transport['timeoutId'] = window.setTimeout( function(){
        if (callInProgress(transport)) {
          transport.abort();
          showTimedOutMessage();
        }
      }, 10000); //10-second timeout
    },
    onComplete: function(request) {
      window.clearTimeout(request['timeoutId']);
    }
  });
}

function checkUnique_onSuccess(transport, field, object) {
  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var object_array=object.split(".");
  var objectName=object_array[4];
  var fieldName=field.name; 
  var field_array=field.name.split(":");
  if (field_array.length > 2) {
    fieldName=field_array[2]; 
  }
  var xmlAttrs=docRoot.getElementsByTagName("modelobject");
  for(var i=0;i<xmlAttrs.length;i++) {
    var attr=xmlAttrs[i];
    var hook=attr.getAttribute("hook");
    if (hook != null) {
        field.addClassName("invalidformfield");
        
        alert("A "+objectName+" with this "+fieldName+" already exists");
        field.isInvalid=true;
        
    } else {
        field.removeClassName("invalidformfield");
        field.isInvalid=false;
    }
  } 
} //end checkUnique_onSuccess



/*
 * Called onblur of a pims:input type=text.
 *
 * checks that an instance of object.property does not exist with the value field.value already in the database
 * 
 */
function ajax_exists(field, object, property){
    new Ajax.Request(contextPath+"/read/AjaxExists/"+object, {
	method:"get",
	parameters:property+"="+field.value,
	onSuccess:function(transport){ 
	  //do the standard after-delete actions, then any additional cleanup
	  ajax_exists_onSuccess(transport, field, object);
	},
	onFailure:function(transport){ 
	  ajax_onFailure(transport); 
	}, 
	onCreate: function(transport) {
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 30000); //30-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });
  return false;
  
} //end ajax_unique

/*
 * The default AJAX unique success handler.
 * @param transport The AJAX request object
 * Modified by Susy Sept2010 to improve error for duplicate AbstractComponent check for new extensions
 */
function ajax_exists_onSuccess(transport, field, object) {

  ajax_checkStillLoggedIn(transport);
  
  var docRoot=transport.responseXML.documentElement;

  var object_array=object.split(".");
  var objectName=object_array[4]; 
  
  var fieldName=field.name; 
  var field_array=field.name.split(":");
  if (field_array.length > 2) {
  	fieldName=field_array[2]; 
  }
  
  var xmlAttrs=docRoot.getElementsByTagName("modelobject");
  for(var i=0;i<xmlAttrs.length;i++) {
    var attr=xmlAttrs[i];
	var hook=attr.getAttribute("hook");
	if (hook != null) {
        highlightField(field.name);
        setAjaxError(field.name);
        alert("A "+objectName+" with this "+fieldName+" already exists");
    } else {
    	clearAjaxError(field.name);
        field.style.backgroundColor='#ffffff';
    }
  }	
} //end ajax_unique_onSuccess


/*
 * Called onblur of a pims:input type=text.
 *
 * checks that an instance of object.property does not exist with the value field.value already in the database
 * 
 */
function ajax_validate(field,object,property){
    new Ajax.Request(contextPath+"/read/AjaxValidate/"+object, {
	method:"get",
	parameters:property+"="+field.value,
	onSuccess:function(transport){ 
	  //do the standard after-delete actions, then any additional cleanup
	  ajax_validate_onSuccess(transport, field);
	  if(options && options["cleanup"]){ options["cleanup"](clickedIcon); } 
	},
	onFailure:function(transport){ 
	  ajax_onFailure(transport); 
	}, 
	onCreate: function(transport) {
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 10000); //10-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });
  return false;
  
} //end ajax_validate

/*
 * The default AJAX validate success handler.
 * @param transport The AJAX request object
 */
function ajax_validate_onSuccess(transport, field) {

  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var xmlAttrs=docRoot.getElementsByTagName("modelobject");
  for(var i=0;i<xmlAttrs.length;i++) {
    var attr=xmlAttrs[i];
	var error=attr.getAttribute("error");
	if (error != null) {
        highlightField(field.name);
        setAjaxError(field.name);
        alert(error);
        document.getElementById(field.name).value="";
    } else {
    	clearAjaxError(field.name);
    	document.getElementById(field.name).style.backgroundColor='ffffff';
    }
  }	
} //end ajax_validate_onSuccess


function setAjaxError(name) {
	for(var i=0;i<ajaxErrors.length;i++) {
    	var err=ajaxErrors[i];
    	if (err==name) return;
    }
    ajaxErrors[ajaxErrors.length]=name;
}

function clearAjaxError(name) {
    var array = new Array();
	for(var i=0;i<ajaxErrors.length;i++) {
    	var err=ajaxErrors[i];
    	if (err!=name) {
    		array[array.length]=err;
    	}
    }
    ajaxErrors=array;
}



