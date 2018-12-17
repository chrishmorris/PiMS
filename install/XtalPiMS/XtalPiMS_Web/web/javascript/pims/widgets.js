/**
 * Toggles a collapsible box opened and closed, unless it's fixed.
 *
 *  @param elem The header element clicked on by the user.  
 */
function toggleCollapsibleBox(elem, src){ 
  var box=$(elem);
  if(!box.hasClassName("collapsiblebox")){
  	box=box.up(".collapsiblebox");
  }
  if(!box){ return false; } //A "Remove" link on the box header might cause this toggle to fire after the box is removed
  if(box.hasClassName("fixedbox")) {
    return false;
  }
  if(box.hasClassName("closedbox") && src!=undefined && src!='') {
    new Ajax.Request(src, {
	  method:"get",
	  onSuccess:function(transport){
	    box.down(".boxcontent").innerHTML=transport.responseText;
      },
  	  onFailure:function(transport){
        box.down(".boxcontent").innerHTML=transport.responseText;
	}
    });
  }  
  box.toggleClassName("closedbox");
  var textareas=box.select("textarea");
  textareas.each(function(ta){
  	matchTextareaHeightToContent(ta);
  });
  if(!box.hasClassName("closedbox")){
	  focusFirstField(box);  
}
}
function openCollapsibleBox(elem){
  var box=$(elem);
  if(!box.hasClassName("collapsiblebox")){
  	box=box.up(".collapsiblebox");
  }
  if(box.hasClassName("fixedbox")) {
    return false;
  }
  box.removeClassName("closedbox");	
  focusFirstField(box);
}
function closeCollapsibleBox(elem){
  var box=$(elem);
  if(!box.hasClassName("collapsiblebox")){
  	box=box.up(".collapsiblebox");
  }
  if(box.hasClassName("fixedbox")) {
    return false;
  }
  box.addClassName("closedbox");	
}


var contextMenuRetried=false; //See PIMS-2975
function showContextMenu(img,menuItems) {
  Element.extend(img); //for IE
  var cm=$("contextmenu_real");
  cm.clickedArrow=img;
  try{
    Position.clone(img,cm,{setWidth:false,setHeight:false} );
    //Element.clonePosition(cm,img,{setWidth:false,setHeight:false} );
    cm.style.display="block";
    cm.innerHTML = "";
    for(i=0;i<menuItems.properties.length;i++){
      cm.innerHTML+=writeContextMenuProperty(menuItems.properties[i]);
    }
    cm.innerHTML="<div class=\"contextmenu_real_property\">" + cm.innerHTML + "</div>";
  
    cm.innerHTML+="<hr/>";
    for(i=0;i<menuItems.actions.length;i++){
      cm.innerHTML+=writeContextMenuAction(menuItems.actions[i]);
    }
  } catch(err){
	//See PIMS-2975  
	if(!contextMenuRetried){
	  contextMenuRetried=true;
	  showContextMenu(img,menuItems);
    }
  }
}

/**
 * Writes the context menu item.
 *
 * @param item A Javascript object containing the menu item info.
 */
function writeContextMenuProperty(item){
  var link="<span class=\"contextmenu_property\">"+item["property"]+":<\/span> "+item["val"]+"<br/>";
  return link;
}
/**
 * Writes the context menu item.
 *
 * @param item A Javascript object containing the menu item info.
 */
function writeContextMenuAction(item){

  var onclick="";
  if(item["onclick"]){ onclick='onclick="'+item["onclick"]+';return false"'; }
  var url = item["url"];
  if (0!=url.indexOf("http") && 0!=url.indexOf(contextPath)) {
  	url = contextPath+url;
  }
  var link ='<a href="'+ url +'" '+onclick+'>';
  link += '<img src="'+ contextPath +'/skins/default/images/icons/'+ item["icon"] +'" alt="" height="16" width="16">';
  link += item["text"];
  link += '</a>';

  return link;
}

/**
 * Hides the context menu - should be called on mouseout of the menu.
 *
 */
function hideContextMenu() {
  $("contextmenu_real").style.display="none";
}

/* For in-page calendar widget 
 * (pimsWidget:calendar, not the JS pop-up one) 
 * Functions to jump 1 month/year forward/back
 */
function calendarLastMonth(calendarId){
  var cal=document.getElementById(calendarId);
  cal.currentDate.setMonth(cal.currentDate.getMonth()-1);
  replaceCalendar(calendarId);
}
function calendarNextMonth(calendarId){
  var cal=document.getElementById(calendarId);
  cal.currentDate.setMonth(cal.currentDate.getMonth()+1);
  replaceCalendar(calendarId);
}
function calendarLastYear(calendarId){
  var cal=document.getElementById(calendarId);
  cal.currentDate.setYear(cal.currentDate.getFullYear()-1);
  replaceCalendar(calendarId);
}
function calendarNextYear(calendarId){
  var cal=document.getElementById(calendarId);
  cal.currentDate.setYear(cal.currentDate.getFullYear()+1);
  replaceCalendar(calendarId);
}
function replaceCalendar(widgetId){
	var widget=document.getElementById(widgetId);
	var newDate=widget.currentDate;
	widget.innerHTML=makeCalendarHTML(widgetId,newDate);
}


function removeAssociate(from, role, toRemove){
	var form =  $("dummy_removeform");
    form['from'].value = from;
    form['role'].value = role;
    form['removed'].value = toRemove;
    form.submit();
}

/*****************************************
 * 
 * Functions to support <pimsWidget:plateSelector>
 * 
 *****************************************/

/**
 * On first mouseover of plate, set up variables needed for selecting
 */
function pwPlateSelector_plateOnMouseOut(plate){
	//empty function
}

function pwPlateSelector_plateOnMouseOver(plate){
	if(!plate.currentSelection){
		pwPlateSelector_initPlate(plate);
	}
}
/**
 * Add .row and .column properties to the well TDs in the plate, etc.
 */
function pwPlateSelector_initPlate(plate){
  $(plate).currentSelection={ startY:-1, endY:-1, startX:-1, endX:-1 }
  plate.isDragging=false;
  var rowNum=0;
  var colNum=0;
  var rows=plate.select("tr.wells");
  rows.each(function(row){
  	var wells=row.select("td.well");
  	wells.each(function(well){
  		well.row=rowNum;
  		well.col=colNum;
  		colNum++;
  	});
  	rowNum++;
  	colNum=0;
  });
}

function pwPlateSelector_wellOnMouseDown(well){
	var plate=$(well).up("table");
	if(!well.row){ pwPlateSelector_initPlate(plate); }
	plate.selectedWells=[];
    plate.currentSelection.startY=well.row;	
    plate.currentSelection.endY=well.row;	
    plate.currentSelection.startX=well.col;	
    plate.currentSelection.endX=well.col;
    plate.isDragging=true;	
	pwPlateSelector_highlightSelectedWells(plate);
}

function pwPlateSelector_wellOnMouseOver(well){
	var plate=$(well).up("table");
	if(!plate.isDragging){ return false; }
    plate.currentSelection.endY=well.row;
    plate.currentSelection.endX=well.col;
	pwPlateSelector_highlightSelectedWells(plate);
}
function pwPlateSelector_wellOnMouseUp(well){
	var plate=$(well).up("table");
	plate.isDragging=false;    
	var plateSelection=new Object();
	plateSelection.plate = { id:plate.id }
	plateSelection.selectedWells=[];
	var wells=$(plate).select("td.selected");
    wells.each(function(well){
    	var wellId=well.id.split("_")[0];
		plateSelection.selectedWells.push({row:well.row, col:well.col, id:wellId });
    });
    plate.callback(plateSelection);
}
function pwPlateSelector_highlightSelectedWells(plate){
  var wells=$(plate).select("td.well");
  var startX=plate.currentSelection.startX;
  var endX=plate.currentSelection.endX;
  var startY=plate.currentSelection.startY;
  var endY=plate.currentSelection.endY;
  var temp=0;
  if(endX<startX){ temp=endX; endX=startX; startX=temp; } 
  if(endY<startY){ temp=endY; endY=startY; startY=temp; } 
  wells.each(function(well){
  	if(well.col>=startX && well.col<=endX && well.row>=startY && well.row<=endY){
  	  well.addClassName("selected");
  	} else {
  	  well.removeClassName("selected");
  	}
  });
}

//obsolete?
function pwPlateSelector_exampleCallbackFunction(obj){
  var msg="This message comes from the example callback ";
  msg+="function pwPlateSelector_exampleCallbackFunction "
  msg+="in widgets.js.\n\n"
  msg+="Plate selector id: "+obj.plate.id+"\n";	
  msg+="Selected wells: ";
  var numWells=obj.selectedWells.length;
  for(i=0;i<numWells;i++){
  	msg+=obj.selectedWells[i].id;
  	if(i!=numWells-1){
  		msg+=", ";
  	}
  }
  alert(msg);
}

/**
 * Functions to support adding roles to MRU via popup list
 * 
 */
var mruToUpdate=null;
function startMRURoleAdd(mru,windowURL,windowTitle){
	mruToUpdate=mru;
	openModalWindow(windowURL,windowTitle);
}

function selectInMRU(obj){
	var newOption=document.createElement("option");

	//this block should be unnecessary - should be a 'name' in everything
	if(obj['name'] && ""!=obj['name']){ 
	newOption.text=obj["name"];
	} else if(obj['Localname']){
		newOption.text=obj["Localname"];
	} else if(obj['Organismname']){
		newOption.text=obj["Organismname"];
	} else {
		newOption.text="Chosen object";
	}
	
	newOption.value=obj["hook"];
	if(document.all){
		mruToUpdate.add(newOption);
	} else {
		mruToUpdate.add(newOption,null);
	}
	mruToUpdate.selectedIndex=mruToUpdate.options.length-1;
	closeModalWindow();
	mruToUpdate=null;
}

/**
 * Functions to support multi-role edit
 *
 */
var divRolesToAdd=null;
 
function startMultiRoleEdit(elem,searchURL){
	//TODO if no searchURL, set to default
	divRolesToAdd=$(elem).up('div.collapsiblebox');
	if(divRolesToAdd.hasClassName("closedbox")){
		//open box and load content
		divRolesToAdd.down("h3").onclick(); 
	}
	openModalWindow(searchURL,"Search and add");
}

function doAfterMultiRoleEdit(data){
	//show hidden div with "Add" button in it
	//parse data into hidden input and show names of new roles
	//scroll to hidden div
	if(!divRolesToAdd['rolesToAdd']){
		divRolesToAdd['rolesToAdd']=new Array();
	}
	data.each(function(role){
		divRolesToAdd["rolesToAdd"].push(role);
	});	
	showRolesToAdd(divRolesToAdd);	
	closeModalWindow();
}

/**
 * Shows roles that have been selected and have yet to be added.
 *
 */
function showRolesToAdd(elem){
	elem=$(elem);
	if(!elem.hasClassName("collapsiblebox")){
		elem=elem.up(".collapsiblebox");
	}
	var rolesToAdd=elem["rolesToAdd"];
	var displayString="";
	rolesToAdd.each(function(role){
		displayString+="<span class=\"behaviour_removerole\"><input type=\"hidden\" name=\"add\" value=\""+ role.hook +"\" />";
		displayString+=role.name+"<\/span> ";
	});
	elem.down(".behaviour_itemstoadd").innerHTML=displayString;
	elem.down(".behaviour_showafteradd").style.display="";
}

/**
 * Functions to support pimsWidget:tabSet and pimsWidget:tab
 * 
 */
function resizeTabSetsOnload(){
	//code assumes multiple tabsets, but in all likelihood there will be only one
	document.body.style.overflow="hidden";
	var bottomMargin=25; //pixels under 
	var windowHeight=document.viewport.getHeight();
	Element.extend(document.body);
	var tabSets=document.body.getElementsByClassName("pw_tabset");
	for(var i=0;i<tabSets.length;i++){
	    var ts=tabSets[i];
	    var tabBodies=ts.select(".pw_tabbody");
	    var tabTop=tabBodies[0].cumulativeOffset().top;
	    tabBodies.each(function(tb){ 
	        tb.style.height= windowHeight-(tabTop+bottomMargin)+"px";
	    });
	    ts.style.height= windowHeight-(tabTop+bottomMargin)+"px";
	}
}

function switchTab(clickedTab){
	if($(clickedTab).hasClassName("current")){ return false; }
	var tabs=$(clickedTab).up(".pw_tabset").select(".pw_tab"); //get all the tab headers...
	tabs=tabs.concat($(clickedTab).up(".pw_tabset").select(".pw_tabbody")); ///...and all the tab bodies
	tabs.each(function(tab){ 
		tab.removeClassName("current"); 
	});
	clickedTab.addClassName("current");
	clickedTab.next("div.pw_tabbody").addClassName("current");
}


function contextmenu_rename(hook, message){

	var destinationURL=contextPath+"/update/AjaxUpdate";
	var menuIcon=$("contextmenu_real").clickedArrow;
	var currentName=menuIcon.up("span.linkwithicon").down("span.linktext").innerHTML;
	
	if(null==message){
		message="Enter the new name for "+currentName+":";
	}
	var newName=window.prompt(message, currentName);
	if(null==newName){ return false; } //cancel button
	newName=newName.strip();
	if(""==newName){
		alert("Name is required");
		return false;
	} else if(currentName==newName){ return false; }
	
	showUpdatingModalDialog();
	
	//Real AJAX request. Comment this out to use the test responses below.
	new Ajax.Request(destinationURL,{
		method:"post",
		onSuccess:contextmenu_rename_onSuccess,
		onFailure:contextmenu_rename_onFailure,
		postBody:hook+":name="+newName
	});
	return false;

	// Test the success case with mock transport object	
//		var transport={};
//		transport.responseJSON={};
//		transport.responseJSON[hook]={ name:newName }
//		setTimeout(function(){ contextmenu_rename_onSuccess(transport) },50);
//		return false;

	
	// Test the failure case with mock transport object	
//		var transport={};
//		transport.responseJSON={
//			error:"Failure test, should alert and abort"
//		}
//		setTimeout(function(){ contextmenu_rename_onSuccess(transport) },50);
//		return false;
	
}

/* Success (as defined by Prototype Ajax.Request.onSuccess) handler for contextmenu_rename AJAX response.
 * Expects one of the following JSON responses, which must be served with an application/json mimetype 
 * so that it ends up in transport.responseJSON (not transport.responseText).
 * 
 * Success case - the record was renamed on the server, and the UI will be updated:
 * 
 * { "hook.of.item:1234":{ name:"New name" } }
 * 
 * Failure case
 * 
 * {s
 *   error:"A ResearchObjective called \"New name\" already exists, please try another name"
 * }
 * 
 * User not logged in: Server sends full login page as text/html; ajax_checkStillLoggedIn catches this.
 * 
 * Stack trace: Server sends full login page as text/html with HTTP 500, will be handled by
 *              contextmenu_rename_onFailure instead.
 *              
 * JSON not served as application/json: JSON will be in transport.responseText and displayed to the user,
 *              with no effect on the current page.
 */
function contextmenu_rename_onSuccess(transport){
	closeModalDialog();
	ajax_checkStillLoggedIn(transport);
	if(!transport.responseJSON){
		return ajax_onFailure(transport);
	}
	var response=transport.responseJSON;
	if(response.error){
		alert(response.error);
		return;
	}

	//Don't know the hook, so we need to iterate through the properties. There should be
	//only one, whose name is the hook.
	for(prop in response){
		var hook=prop;
		var newName=response[prop]["name"];
		break; //should be only one
	}
	
	if(!newName || !hook){
		return contextmenu_rename_errorInSuccessHandler("Could not find object name or hook in response");
	}

	//Find all "link with icon" links on the page whose href contains hook, and swap in the new name.
	var links=$$("span.linkwithicon");
	links.each(function(link){
		if(-1!=link.down("a").href.indexOf(hook)){
			link.down("span.linktext").innerHTML=newName;
		}
	});
}
function contextmenu_rename_onFailure(transport){
	closeModalDialog();
	ajax_onFailure(transport);
}
function contextmenu_rename_errorInSuccessHandler(msg){
	if(!msg){ msg="Something went wrong."; }
	alert(msg+"\n\nPlease reload the page.");
	return false;
}