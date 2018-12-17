//Needs Prototype

/*pixel offsets between cursor position and tooltip*/
var tooltipXOffset=5;
var tooltipYOffset=5;

/**
 * Sets up the events necessary for a tooltip on a given element, 
 * or set of elements.
 * 
 * This should be the only method you need to call from your page.
 * For safety, it should be called on page load, as follows:
 *
 * Event.observe(window,"load",function(){
 *   addTooltip("theID","the header","the body");
 *   //repeat for other elements that need a tooltip
 * }
 * @param elementId The ID of the element to which the tooltip should be attached. If
                    there is no element with that ID, the function will attempt to find
                    elements with the CLASS NAME elementId and attach the same tooltip
                    to all of them. If no such elements are found, the function will
                    return false.
 * @param tipHeader The header text of the tooltip - ideally two or three words at most.
 * @param tipBody The body text of the tooltip. Keep it clear and concise.
 */
function addTooltip(elementId,tipHeader,tipBody) {
	alert("obsolete");
  var elem=$(elementId);
  var elementsToAttach=new Array();
  if(!elem) {
	//no element with ID elementId, any with it as a classname?
    elementsToAttach=$$("."+elementId);
  } else {
    elementsToAttach.push(elem);
  }
  if(0==elementsToAttach.length){
    //no elements with class or ID elementId, no point in being here
	return false;
  }
  elementsToAttach.each(function(ele){
    ele.addClassName("hasTooltip");
	Event.observe(ele,"mouseover", function (){showTooltip(tipHeader,tipBody) });
	Event.observe(ele,"mouseover", positionTooltip);
	Event.observe(ele,"mousemove", positionTooltip);
	Event.observe(ele,"mouseout", hideTooltip);
  	Event.observe(ele,"click", hideTooltip);
  	if(ele.match("select")){
  	  var children=ele.getElementsBySelector("optgroup","select");
  	  children.each(function(opt){
  	    Event.observe(opt,"mouseover",hideTooltip);
      });
    }
  });
}

/*
 * You shouldn't need to call any of the functions below here.
 ***************************************************************************
 */

function showTooltip(tipHeader,tipBody) {
	if(!$("tooltip")){
      var tt=document.createElement("div");
      tt.setAttribute("id","tooltip");
      document.body.appendChild(tt);
      var tth=document.createElement("div");
      tth.setAttribute("id","tooltip_head");
      tt.appendChild(tth);
      var ttb=document.createElement("div");
      ttb.setAttribute("id","tooltip_body");
      tt.appendChild(ttb);
	}
	$("tooltip_head").innerHTML=tipHeader;
	$("tooltip_body").innerHTML=tipBody;
	$("tooltip").show();
}

function positionTooltip(e) {
  if(!$("tooltip")) return false; //there should be one! IE doesn't think so, first time around

  ttMouseX=tooltipXOffset+Event.pointerX(e);
  ttMouseY=tooltipYOffset+Event.pointerY(e);
  var elem=Element.extend(Event.element(e));
  if(elem.match("option")||elem.match("optgroup")||
     (elem.match("select")&&elem.down("option").visible()) ){
    ttMouseX=-5000;
    ttMouseY=-5000;
  }

  //TODO stop tooltip falling off the edge

	$("tooltip").setStyle({top:ttMouseY+"px",left:ttMouseX+"px"});
}

function hideTooltip() {
	if(!$("tooltip")) return false; //there should be one! This is a fail-safe
	$("tooltip").hide();
}



// Marc's show and hide sample tooltips for sample in the drop-down list of experiment'

function showSampleToolTip(event, mysample) {
  //alert("showSampleToolTip ["+mysample.name+":"+mysample.details+"]");

  if (!event) var event = window.event;
	// e gives access to the event in all browsers

  if (!document.getElementById("sample_tooltip")) {
  	if (document.all) {
		document.body.innerHTML+="<div id='sample_tooltip'>?<\/div>"
	} else {
		var tooltipDiv = document.createElement("div");
		tooltipDiv.id="sample_tooltip";
		document.body.appendChild(tooltipDiv);
	}
  }

  title="Sample: "+mysample.name+" ";
  data=""
  if ((mysample.details!="") && (mysample.details!=null))
    data+="Details: "+mysample.details+"<br/>";

  // data+="No Components: "+mysample.componentsnumber+"<br/>";
  // data+="No Hazards: "+mysample.hazardsnumber+"<br/>";

  if ((mysample.components!="") && (mysample.components!=null)) {
    if (mysample.components.indexOf(":")>= 0) {
      data+="Components:<br/>";
      var component_array=mysample.components.split(":");
      var component_num=0;
      while (component_num < component_array.length) {
        data+="&nbsp;&nbsp;&nbsp;"+component_array[component_num]+"<br/>";
        component_num+=1;
      }
    }
  }

  if ((mysample.hazards!="") && (mysample.hazards!=null)) {
    if (mysample.hazards.indexOf(":")>= 0) {
      data+="Hazards:<br/>";
      var hazard_array=mysample.hazards.split(":");
      var hazard_num=0;
      while (hazard_num < hazard_array.length) {
        data+="&nbsp;&nbsp;&nbsp;"+hazard_array[hazard_num]+"<br/>";
        hazard_num+=1;
      }
    }
  }

  data+="Amount: "+mysample.amount+" "+mysample.units+"<br/>";

  if ((mysample.specification!="") && (mysample.specification!=null))
    data+="Specification: "+mysample.specification+"<br/>";

  if ((mysample.holder!="") && (mysample.holder!=null))
    data+="Holder: "+mysample.holder+"<br/>";

  if ((mysample.column!="") && (mysample.column!=null))
  	data+="Column: "+mysample.column+"<br/>";

  if ((mysample.row!="") && (mysample.row!=null))
    data+="Row: "+mysample.row+"<br/>";

  if ((mysample.subposition!="") && (mysample.subposition!=null))
    data+="SubPosition: "+mysample.subposition+"<br/>";

  var tip=document.getElementById("sample_tooltip");
  tip.innerHTML='<div id="sample_tooltip_title">'+title+'<\/div>'+data;
  tip.style.display="block";

  tooltipTop=event.clientY-100;
  //if(tooltipTop<0) { tooltipTop=0; }
  tooltipLeft=event.clientX+150;
  tip.style.position="absolute";
  tip.style.top=(tooltipTop)+"px";
  tip.style.left=(tooltipLeft)+"px";
  tip.style.background="#FFC";
  tip.style.border="thin solid #006";
  tip.style.padding="4px";

}

function hideSampleToolTip() {
	if(tip=document.getElementById("sample_tooltip")) { //yes, that's meant to be a single equals
		tip.style.display="none";
	}
}