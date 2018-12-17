/*
 * All FORM VALIDATION functions in this file are DEPRECATED and WILL BE REMOVED
 * in a future release. Do not use them.
 *
 * The deprecated functions are marked @deprecated and have been moved to the end
 * of the file.
 *
 * See the documentation in validation.js for the correct way to do Javascript
 * validation in PiMS.
 */


//deprecated?
var fieldsForValidation;

/*
 * When users click to another page, they may lose their edits.
 */
document.attributeChanged = false;
document.warnChanged = true;
function onEdit() {
	//alert("onEdit");
    document.attributeChanged = true;
}
function dontWarn() {
    //alert("dontwarn");
    document.warnChanged = false;
}
function warnChange() {
    if (!document.warnChanged || ! document.attributeChanged) {
        return true; // carry on regardless
    }
    ok = confirm(
            'Are you sure you want to navigate away from this page?'
            +'\nChanges that you have made will be lost.'
            +'\nPress OK to continue, or Cancel to stay on the current page.'
    );
    if (ok) {document.warnChanged = false;}
    return ok;
}
/* 
 * The code below results in this message: 
 *          'Are you sure you want to navigate away from this page?'
            +'\nPress OK to continue, or Cancel to stay on the current page.'
 * If it appears inappropriately, then either someone has called onEdit() when nothing changed,
 * or you need a call to dontWarn() in the onclick event.
 * 
 * */
window.onbeforeunload = function() {
    if (!document.warnChanged || ! document.attributeChanged) {
        return; // carry on regardless
    }
    return false; // FF or IE will make warning message
}

/*
 * Prevent Enter in a text-box from submitting the form. Useful if a barcode scanner appends CR+LF.
 * Takes the ID of a form field, or the field itself.
 * Undo this behaviour with unsuppressSubmitOnEnter(formField). 
 */
function suppressSubmitOnEnter(formField){
	$(formField).observe("keypress", suppressSubmitOnEnter_Implementation);
}
function unsuppressSubmitOnEnter(formField){
	$(formField).stopObserving("keypress", suppressSubmitOnEnter_Implementation);
}

/* 
 * Prevent Enter in a text-box from submitting the form, instead tabbing to the next field (if present). 
 * Useful if a barcode scanner appends CR+LF.
 * Don't call this directly. Use suppressSubmitOnEnter(formField).
 */
function suppressSubmitOnEnter_Implementation(event){
	formField=event.findElement("input");
	formField.onkeypress=function(e){
		if(13==e.keyCode){
			var fields=Form.getElements(formField.up("form"));
			var numFields=fields.length;
			for(i=0;i<numFields;i++){
				if(fields[i]==formField){
					if(i<numFields-1){
						fields[i+1].focus();
					}
					return false;
				}
			}
			return false;
		}
	}
}

/* Called when a page is loaded
 * Redefined at the bottom of the page if necessary
 * An empty function is defined here to prevent errors
 */
function onLoadPims() {}

/*
 * This for the same purpose as above
 */
function onSubmit() {}

/*
 * sets the form's onSubmit handler to askBeforeDeleting()
 *
 * Assumes there is only one form on the page!
 */
function confirmDelete() {
  if (document.forms[1]) {
      document.forms[1].onsubmit = askBeforeDeleting;
  }
}


/* Adds a confirmation dialogue.
 * Called in a page with a Delete button.
 * Returns true if OK is pressed, false if Cancel is pressed.
 */
function askBeforeDeleting() {
	var confirmationMessage;
	confirmationMessage  = "You are about to delete the following from PIMS:\n";
	confirmationMessage += "\n";
	if(null != className) { confirmationMessage += className + ": "; }
	confirmationMessage += objectName + "\n";
	confirmationMessage += "\n";
	confirmationMessage += "If you are sure you want to do this, click OK. Otherwise, click Cancel."

	return window.confirm(confirmationMessage);
}


/********************************************
 * Tab functionality
 */

/**
 * Inserts <input type="hidden" name="_tab" value="tabId"> into frm
 * - used for persistence of tab ID
 */
function tab_appendTabIdToForm(frm,tabId){
  if(!frm["_tab"]){
	var fld=document.createElement("input");
	fld.setAttribute("type","hidden");
	fld.setAttribute("name","_tab");
	fld.setAttribute("value",tabId);
	frm.appendChild(fld);
  }
}

function tab_appendTabIdToFormsInTab(tabHeadId){
  var tabBody=$(tabHeadId+"_body");
  var formsInTab=tabBody.getElementsBySelector("form");
  formsInTab.each(function(frm){
	tab_appendTabIdToForm(frm,tabHeadId);
  });
  //and remove the default mouseover and onclick methods
  // - they're only there to fire this, and it only needs doing once
  tabBody.onmouseover=null;
  tabBody.onkeypress=null;
  tabBody.onkeydown=null;
}

function tab_showTab(elem){
  if(null==elem){ //set to current tab
    var tabs=document.body.down("div.tabs");
    elem=tabs.down("h3.current");
  }
  elem=$(elem);
  elem.blur();
  var tabSet=elem.up("div.tabs");
  var clickedTab=elem.up("h3.tab");
  if(null==clickedTab || clickedTab.hasClassName("greyedout")) {
    return false;
  }
  var currentTab=elem.up("div.tabs").down("h3.current");
  var clickedTabId=clickedTab.id;
  var clickedTabBody=$(clickedTabId+"_body");

  tab_appendTabIdToFormsInTab(clickedTabId);

  var tabBodies = tabSet.getElementsBySelector(".tabbody");

  for(i=0;i<tabBodies.length;i++){
    var tabBody=tabBodies[i];
    if(tabBody.id==clickedTabBody.id){
      tabBody.addClassName("currenttabbody");
    } else {
      tabBody.removeClassName("currenttabbody");
    }
  }

  currentTab.removeClassName("current");
  clickedTab.addClassName("current");
}

/*
 * End tab functionality
 ********************************************/


/*
 * Removes leading and trailing spaces from the string
 */
function trim(stringToTrim) {
  stringToTrim=stringToTrim.replace(/^\s*/, ''); //trim leading spaces
  stringToTrim=stringToTrim.replace(/\s*$/, ''); //trim trailing spaces
  return stringToTrim;
}


/*
 * Colours the offending field to make the error more obvious
 */
function highlightField(fieldName) {
  //document.forms[1][fieldName].style.backgroundColor='#ffaaaa';
	document.getElementById(fieldName).style.backgroundColor='#ffaaaa';
}


/*
 * Resets a form field's background to transparent
 */
function clearHighlight(fieldName) {
  //document.forms[1][fieldName].style.backgroundColor='transparent';
	document.getElementById(fieldName).style.backgroundColor='transparent';
}


/*
 * Sends the user back one page - equivalent to their clicking the Back button.
 */
function goBack() {
  history.go(-1);
}


/* Function open new window with help information on the attribute in it.
 * @param - parameter - The URL to the Help servlet
 */

// Help window
var nwindow = null;


/*
 * Check whether or not the opened window already closed.
 */
function window_opened() {
	if(!nwindow) {
		return false;
	}
	else if (nwindow.closed){
		return false;
	}
	else {
		return true;
	}
}

// Function to hide and show column in the table
// hide column in the table
function hideColumn (tableName, colIndex) {
  var table = document.all ? document.all.tableName :
              document.getElementById(tableName);
  for (var r = 0; r < table.rows.length; r++)
    table.rows[r].cells[colIndex].style.display = 'none';
}
// show column in the table
function showColumn (tableName, colIndex) {
  var table = document.all ? document.all.tableName :
              document.getElementById(tableName);
  for (var r = 0; r < table.rows.length; r++)
    table.rows[r].cells[colIndex].style.display = '';
}
//show all columns in the table
function showAll(tabName, list) {
	for (var counter = 0; counter < list.options.length; counter++) {
		showColumn(tabName, list.options[counter].index)
	}
}
//show columns in the table
function showColumns (tabName, list) {
	var chosed_el = new Array()
	chosed_el = getSelected(list)
	for (var r = 0; r < chosed_el.length; r++) {
		showColumn (tabName, chosed_el[r]);
	}
}
//hide columns in the table
function hideColumns(tabName, list) {
	var chosed_el = new Array()
	chosed_el = getSelected(list)
	for (var r = 0; r < chosed_el.length; r++) {
		hideColumn (tabName, chosed_el[r]);
	}
}
// Get selected elements from the list
function getSelected(currentList) {
var selected_array = new Array()
var cur_index = 0
	for (var counter = 0; counter < currentList.options.length; counter++) {
		if(currentList.options[counter].selected) {
			selected_array[cur_index]=currentList.options[counter].index
			cur_index++
		}
	}
return selected_array
}

function isValidDnaOrProteinSequence(seq){
	var seq = removeSpaces(seq);
	if(seq.charAt("0") == ">"){
		var firstNewline=seq.indexOf("\n");
		seq = seq.substr(firstNewline);
	}
	if(isValidDnaSequence(seq) || isValidProteinSequence(seq)) {
		return true;
	}
	return false;
}

function cleanSequence(string){
	string=string.toUpperCase();
	string=removeSpaces(string);
	string = string.replace(/[^A-Z]/g, "");
	return string;
}

//modification of cleanSequence to avoid reformatting to single string during editing
function cleanSequence2(string){
	string=string.toUpperCase();
	string=trim(string);
	string = string.replace(/[^A-Z\s{2,}]/g, "");
	return string;
}

function cleanDnaSequence(string){
    string=string.toUpperCase();
    string=removeSpaces(string);
    string = string.replace(/[^ACGT]/g, "");
    return string;
}

function removeSpaces(string) {
	var tstring = "";
	string = '' + string;
	splitstring = string.split(" ");
	for(i = 0; i < splitstring.length; i++)
	tstring += splitstring[i];
	return tstring;
}






function checkSequence() {
    thisElement = getElement(document.forms[1], "org.pimslims.model.refSampleComponent.MolComponent:seqString");
    if (thisElement) {
        var myElement = getElement(document.forms[1], "org.pimslims.model.refSampleComponent.MolComponent:molType");
        if (myElement) {
    	    var myType = myElement.value;
            if (myType=="protein") {
                if (!isValidProteinSequence(thisElement.value)) {
                    alert("Invalid protein sequence, valid characters are [ABCDEFGHIKLMNPQRSTVWYXZ]");
                    return false;
                }
            }
            if (myType=="DNA") {
                if (!isValidDnaSequence(thisElement.value)) {
                    alert("Invalid DNA sequence, valid characters are [ACGTRYMKSWBDHVN]");
                    return false;
                }
            }
            if (myType=="RNA") {
                if (!isValidDnaSequence(thisElement.value)) {
                    alert("Invalid DNA sequence, valid characters are [ACGTRYMKSWBDHVN]");
                    return false;
                }
            }
        }
    }
    return true;
}

function checkProteinSequence(theSeq) {
	if (!isValidProteinSequence(theSeq)) {
		alert("Invalid protein sequence, valid characters are [ABCDEFGHIKLMNPQRSTVWYXZ]");
		return false;
	}
	return true;
}

function checkDNASequence(theSeq) {
	if (!isValidDnaSequence(theSeq)) {
		alert("Invalid DNA sequence, valid characters are [ACGTRYMKSWBDHVN]");
		return false;
	}
	return true;
}

function getElement(thisForm, thisName) {
    for (var index=0;index<thisForm.elements.length;index++) {
    	    myelement=thisForm.elements[index];
       	    if (myelement.name == thisName) {
                return myelement;
            }
    }
    return null;
}

/*
For use with tree structures, for example view of protocols. clicking on the +/- icon opens/closes
the information underneath.
Assumes HTML structure:
<div>
  <div id="TOGGLENAMEhead"><!-- The header bar, containing the title and the +/- button -->
    <img id="TOGGLENAMEimg" src="[path]plus.gif" onClick="toggleView('TOGGLENAME')" />
  </div>
  <div id="TOGGLENAMEbody"><!-- the body, which appears/disappears-->

  </div>
</div>
TODO move to leeds.js, now used only in Leeds pages
 */
function toggleView(divname, path) {
  if(document.getElementById(divname+"body").style) {
    if(document.getElementById(divname+"body").style.display=="block" || document.getElementById(divname+"body").style.display=="") {
      document.getElementById(divname+"body").style.display="none";
      document.getElementById(divname+"img").src=path+"/skins/default/images/icons/openbox.gif";
    } else {
      document.getElementById(divname+"body").style.display="block";
      document.getElementById(divname+"img").src=path+"/skins/default/images/icons/closebox.gif";
    }
  }
}

function scrollSidebar(anchor) {
	var target = document.getElementById(anchor);
	if (!target) {return;}
	var sidebar = document.getElementById('sidebar');
	var offset = 0;
	if (target.y) {
	   offset = target.y - sidebar.y;
	} else if (target.offsetTop) {
		 offset = target.offsetTop;
	}
	sidebar.scrollTop =  offset;
}

function confirmAbandon() {
    var agree=confirm('Are you sure you want to abandon this work?\nIf you click OK, you will lose any information you have entered here.');
    if(agree){
    	window.parent.closeModalWindow();
    }
	return false ;
}

/*****************************************************************
 * For opening and closing collapsible boxes. (ED, Sep 2007)
 * obsolete
function oldToggleCollapsibleBox(headerElem){
  var box=Element.extend(headerElem).up(1);
  box.toggleClassName("collapsibleBox_opened");
  box.toggleClassName("collapsibleBox_closed");
}
function openBox(headerElem){
  var box=Element.extend(headerElem).up(1);
  box.addClassName("collapsibleBox_opened");
  box.removeClassName("collapsibleBox_closed");
}
function closeBox(headerElem){
  var box=Element.extend(headerElem).up(1);
  box.removeClassName("collapsibleBox_opened");
  box.addClassName("collapsibleBox_closed");
} */




/**
 * Highlights a row in a table.
 * @param elem The object clicked (probably a "highlight" icon)
 */
function toggleHighlight(elem){
	alert("obsolete");
  elem=$(elem);
  var tr=elem.up("tr");
  tr.toggleClassName("highlighted");
  var tds=Element.immediateDescendants(tr);
  for(i=0;i<tds.length;i++){
    tds[i].toggleClassName("highlighted");
  }
}

/*******************************************************************************
 End of deprecated form validation functions
 *******************************************************************************/

/**
 * used by <pimsForm:doAmount> in inputsamples.jsp outputsamples.jsp viewsample.jsp
 */
function sampleOnChange(field, prop){
    //alert("sampleOnChange ["+field.name+":"+prop+"]");
	var index = field.name.lastIndexOf(':');
	var basename = field.name.substring(0,index);
	var amountElement = basename.substring(1);
	var valueElement = basename+":value";
	var unitsElement = basename+":units";
	if (!field.form.elements[valueElement]) {alert(valueElement);}
	var value = field.form.elements[valueElement].value;
	var units = field.form.elements[unitsElement].value;
	if(!isNumeric(value))
		alert("Amount value must be numeric");
	field.form.elements[amountElement].value=value+units;
}

/**
 * This function is used in Leeds sequencing management CompletedSO.jsp and ListPlateSOrdersWithFiles.jsp
 * To display copying... test and send data to the servlet 
 * @param form
 * @return
 */
function wait(form) { 
	if(confirm('Copy experiments to the new Order?')) { 
		var id = $(form).id; 
		var orderId = id.substring('orderForCopying'.length,id.length);
		$('copyHolder'+orderId).innerHTML='Copying' + "<span style='text-decoration: blink'>...</span>"; 
		$(form).submit();
	}
}

/***********************************
 *   Functions for main menu bar   *
 ***********************************/
function initMainMenu(submenus, path){
	  for (var f in submenus) {
		  document.write('<li id="'+f+'" onmouseover="showMenu(this)" onmouseout="hideMenus()">'
     +'<a onclick="return warnChange()" href="'+path+'/functions/'+f+'.jsp" >'
    +f+'</a>\n');
	  };
	
	  var ua=navigator.userAgent;
	  if(-1!=ua.indexOf("Android") || -1!=ua.indexOf("iPad") ||
	-1!=ua.indexOf("iPhone")){
	    initMainMenuTouch(submenus, path);
	  } else {
	    initMainMenuNoTouch(submenus, path);
	  }
}

	/**
	 * Implement submenus as select elements, Android handles these nicely.
	 * LATER possibly long click to go right to main menu item - but that's
	backward.
	 */
	function initMainMenuTouch(submenus, path){
	  $H(submenus).each(function(pair){
	    var parent=$(pair.key);
	    parent.down("a").href=null; //otherwise main menu link can hijack the
	//click, get focus, and do nothing!
	    var h=parent.getHeight();
	    var w=parent.getWidth();
	    var subm='<select onchange="window.document.location=this.value">';
	    subm+='<option value="">Choose:</option>';
	    for(i=0;i<pair.value.length;i++){
	      var item=pair.value[i];
	      if (item[1].indexOf(':')==-1) {
	    	  url=path+item[1];
	      } else {
	          url=item[1];
	      }
	      subm+='<option value="'+url+'">'+item[0]+'</option>';
	    }
	    subm+='</select>';
	    parent.innerHTML += subm;
	    var sel=parent.down("select");
	    sel.style.height=(h+5)+"px";
	    sel.style.width=w+"px";
	    sel.style.position="absolute";
	    sel.style.border="0";
	    sel.style.left="0";
	    sel.style.pixelTop="0";
	    sel.style.opacity="0";
	  });
	}

	/**
	 * Do mouseover dropdowns as nested list elements
	 */
	function initMainMenuNoTouch(submenus, path){
	  $H(submenus).each(function(pair){
	    var parent=$(pair.key);
	    var subm='<ul class="submenu">';
	    for(i=0;i<pair.value.length;i++){
	       var item=pair.value[i];
	       if (item[1].indexOf(':')==-1) {
		    	  url=path+item[1];
		      } else {
		          url=item[1];
		      }
	       subm+='<li><a href="'+url+'">'+item[0]+'</a></li>';
	    }
	    subm+='</ul>';
	    parent.innerHTML += subm;
	    parent.onclick=function(){
	      var disp=$(parent).down("ul.submenu").style.visibility;
	      disp=(disp=="hidden") ? "visible" : "hidden";
	    } 
	  });
	}

/** 
 * Shows the submenu of the supplied main menu element.
 */
function showMenu(menuElement) {
  var submenu = $(menuElement).down("ul.submenu");
  if(undefined!=submenu) {
	  submenu.style.display="block"; 
  }
}

/**
 * Hides all submenus on the main menu bar
 */
function hideMenus() { 
  $("menu").select("ul.submenu").each(function(submenu){
	  submenu.style.display="none"; 
  });
}