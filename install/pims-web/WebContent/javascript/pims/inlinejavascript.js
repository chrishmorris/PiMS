/********************************************
 * inlinejavascript.js - This is included in the page header on every load, so keep it to 
 * an absolute minimum. It should ONLY contain functions needed during page load.
 * - DO NOT put any other Javascript in here; put it somewhere more appropriate.
 * - DO NOT assume that any other js files, including Prototype/Scriptaculous, have loaded;
 *   use document.getElementById instead of Prototype's $, for example.
 *   TODO it probably isn't necessary to inline this, browsers block for javascript load
 ********************************************/

/**
 * Attaches a validation object to a form field, which can be used to check its value.
 *
 * @param elementId The ID of the field to be validated
 * @param validation A JSON object representing the validation required. See validation.js
 *                   for a list of available validation checks.
 */
function attachValidation(elementId,validation){
  if(!document.getElementById(elementId)){
    doNonExistentFieldError(elementId);
    return false;
  }
  var field=document.getElementById(elementId);
  if(!validation.alias) { validation.alias=elementId; }

  if(validation.requireIfOtherNotEmpty){
    var rione=validation.requireIfOtherNotEmpty;
    if(!rione.push){ //it's not an array, so make it one
      validation.requireIfOtherNotEmpty=new Array(rione);
    }
  }
  if(validation.mutuallyRequire){
    if(!document.getElementById(validation.mutuallyRequire)){
      doNonExistentFieldError(validation.mutuallyRequire);
      return false;
    }
    var otherElement=document.getElementById(validation.mutuallyRequire);
    //rione on this field
    if(!validation.otherAlias) validation.otherAlias=validation.mutuallyRequire; //use the other field's ID as its alias if not provided
    attachValidation(elementId,{requireIfOtherNotEmpty:validation.mutuallyRequire, alias:validation.alias, otherAlias:validation.otherAlias});
    //rione on the other field
    attachValidation(validation.mutuallyRequire,{requireIfOtherNotEmpty:elementId, alias:validation.otherAlias, otherAlias:validation.alias});
    validation.mutuallyRequire=null;
  }

  if(!document.getElementById(elementId).validation){
    document.getElementById(elementId).validation = validation;
  } else {
    for (rule in validation){ //legit use of for-in! - Ed
      	if(rule=="requireIfOtherNotEmpty"){
        //if exists, add to it, else new array with it
        if(document.getElementById(elementId).validation.requireIfOtherNotEmpty){
          existing=document.getElementById(elementId).validation.requireIfOtherNotEmpty;
          document.getElementById(elementId).validation.requireIfOtherNotEmpty=existing.concat(validation[rule]);
        } else {
          document.getElementById(elementId).validation.requireIfOtherNotEmpty = validation.requireIfOtherNotEmpty;
        }
      } else if(null!=validation[rule]){
        document.getElementById(elementId).validation[rule]=validation[rule];
      }
    }
  }
}

/**
 * Alerts a message to the user, stating that we tried to attach validation to
 * a non-existent form field.
 * TODO log this somewhere.
 *
 * @param elementId the ID of the field in question
 * @private
 */
function doNonExistentFieldError(elementId){
   alert("Tried to attach validation to non-existent form field '"+elementId+"'.\nForm validation on this page may not work properly.\n\nPlease let the PiMS developers know about this message.");
}

function matchTextareaHeightToContent(ta){
  //was if(document.all){ return false; } //IE7 horks layout - textarea slides under following textarea - see target sequences
  ta.rows=2;
  ta.style.height="auto";
  var limit=30; //max number of times to increment box height - safer than relying on "while" 
  for(i=0;i<limit;i++){
    if(ta.scrollHeight>ta.offsetHeight){
      ta.rows++;
    }
  }
  if(ta.next && ta.next(".printonly")){
	  ta.next(".printonly").innerHTML=ta.value.replace(/\n/g,"<br/>");
  }
}

/***** On-page calendar functions (not used for popup calendar) ******/
/**
 * Returns the number of days in the month
 * @param date A Javascript Date object
 */
var calendar_days=["S","M","T","W","T","F","S"];
var calendar_months=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

function daysInMonth(date){ 
	return 32 - new Date(date.getFullYear(), date.getMonth(), 32).getDate();
}
function makeCalendarHTML(widgetId,dateObject){ //any date in the month is fine
	dateObject.setDate(1);
	var today=new Date();
	var mth=dateObject.getMonth(),	year=dateObject.getFullYear(), 	 dayOfFirst=dateObject.getDay();
	var weekDayCount=dayOfFirst, 	numDays=daysInMonth(dateObject), todaysDate=today.getDate();
	var monthName=calendar_months[dateObject.getMonth()];
	var yearsMatch = dateObject.getFullYear()==today.getFullYear();
	var monthsMatch = dateObject.getMonth()==today.getMonth();
	var html="", imagepath=contextPath+"/skins/default/images/icons/misc/calendar/";
	html+="<table class='cal'><tr><th colspan='7'>";
	html+="<a href='#' class='calendar_last' onclick='calendarLastYear(\""+widgetId+"\");return false;'><"+"img src='"+imagepath+"lastyear.png'/><\/a>";
	html+="<a href='#' class='calendar_last' onclick='calendarLastMonth(\""+widgetId+"\");return false;'><"+"img src='"+imagepath+"lastmonth.png'/><\/a> ";
	html+="<a href='#' class='calendar_next' onclick='calendarNextYear(\""+widgetId+"\");return false;'><"+"img src='"+imagepath+"nextyear.png'/><\/a>";
	html+="<a href='#' class='calendar_next' onclick='calendarNextMonth(\""+widgetId+"\");return false;'><"+"img src='"+imagepath+"nextmonth.png'\/><\/a>";
	html+=monthName+" "+year+"<\/th><\/tr><tr>";
	for(i=0;i<calendar_days.length;i++){
		var classInsert="";
		if(i==0||i==6){var classInsert=" class=\"weekend\"";}
		html+="<th"+classInsert+">"+calendar_days[i]+"<\/th>";
	}
	html+="<\/tr><tr>";
	if(0!=dayOfFirst){ html+="<td colspan='"+dayOfFirst+"'>&nbsp;<\/td>"; }
	for(i=1;i<=numDays;i++){
		var title=monthName+" "+i+", "+year;
		var classInsert="";
		if(monthsMatch && yearsMatch && i==todaysDate){
			classInsert+="today";
			title+=" (Today)";
		}
		if(weekDayCount==0||weekDayCount==6){ classInsert+=" weekend"; }
		html+="<td class='"+classInsert+"'><a href='"+contextPath+"/Day/"+year+"-"+(mth+1)+"-"+i+"' title='"+title+"'>"+i+"<\/a><\/td>";
		weekDayCount++;
		if(weekDayCount>6){
			weekDayCount=0;
			if(i<=numDays){ html+="<\/tr><tr>"; }
		}
	}
	html+="<\/tr><\/table>";
	return html;
}

function writeCalendar(dateObject){
	var count=1;
	while(document.getElementById("calendar"+count)) { count++; }
	var widgetId="calendar"+count;
	document.write('<div class="calendarwidget noprint" id="'+widgetId+'">'+makeCalendarHTML(widgetId,dateObject)+"<\/div>");
	document.getElementById(widgetId).currentDate=dateObject;
}
/***** End on-page calendar functions - see also widgets.js ******/



function focusFirstElement(){
	if (focusElement){
		try{
			focusElement.focus();
		}catch(err){
			//do nothing - IE can't see the field, it doesn't matter anyway
		}
		return;
	}
	var h=document.location.hash;
	var box;
	if(""!=h && "#null"!=h){
		//strip off the # and that's your box
		box=$(h.substr(1));
	} else {
		//first .collapsiblebox that isn't .closedbox
		var boxes=$$(".collapsiblebox");
		for(i=0;i<boxes.length;i++){
			if(!boxes[i].hasClassName("closedbox")){
				box=boxes[i];
				break;
			}
		}
	}
	if(null!=box){
		if(!box.hasClassName("viewing")){ //because IE will barf if field is hidden, and it makes no sense anyway
			//choose the first select or input type=text and give it focus
			var firstField=box.down("input[type=text],select");
			if(!firstField){ return; }
			try{
				firstField.focus();
			}catch(err){
				//oh well, it's not the end of the world - and it's
				//probably IE being stupid again
				return false;
			}
		}
	}
}



//END of inlinejavascript.js
/********************************************/