/********************************************************
 * Javascript functions to support Crystal Handling UI. *
 * Included in HarvestingBox.jsp                        *
 ********************************************************/

function setImageOffsets(){
	var imgTopLeftCorner=$("dropimage").cumulativeOffset();
	imgTop=imgTopLeftCorner.top;
	imgLeft=imgTopLeftCorner.left;
	
}

function getCoords(e){
	setImageOffsets();
	imgX = Event.pointerX(e) - imgLeft;
	imgY = Event.pointerY(e) - imgTop;
}

function setCoords(e){
	setImageOffsets();
	if(!$("x")) return false;
	if(-1==xScale|| -1==yScale) return false;
	if(!selectingNew) return false; //Disable click to move crosshairs - make fine adjustments in text box.
	imgX = (Event.pointerX(e) - imgLeft) * xScale;
	imgY = (Event.pointerY(e) - imgTop) * yScale;
	$("x").value=parseInt(imgX);
	$("y").value=parseInt(imgY);
	drawCrosshairs(imgX,imgY,parseInt($("r").value),xtalNumber,'magenta',2);
}

function updateCoordsOnChange(){
	if(!$("x") || !$("y") ) return false;
	if(""==$("x").value || ""==$("y").value || ""==$("r").value){ return false; }
	imgX = parseInt($("x").value);
	imgY = parseInt($("y").value);
	imgR = parseInt($("r").value);
		drawCrosshairs(imgX,imgY,$("r").value,xtalNumber,'magenta',2);
}

function drawCrosshairs(x,y,r,xtalNumber,color,borderThickness){ 
	setImageOffsets();
	if(-1==xScale|| -1==yScale){
		//image hasn't loaded yet
		window.setTimeout(function(){
			drawCrosshairs(x,y,r,xtalNumber,color,borderThickness)
		},100);
		return false;
	}
	x=(x/xScale)+xOffset;
	y=(y/yScale)+yOffset;
	if(r<100){ r=100;} //minimum circle radius
	r=r/xScale;
	if($('xtal'+xtalNumber+'_link')){
		$('xtal'+xtalNumber+'_link').remove();
	}
	var crosshairs="";
	crosshairs+='<a href="'+wellUrl+'&crystal='+xtalNumber+'" id="xtal'+xtalNumber+'_link">';
	crosshairs+='<div id="xtal'+xtalNumber+'" style="position:absolute;top:'+(y-r)+'px;left:'+(x-r)+'px; height:'+2*r+'px;width:'+2*r+'px;border:'+borderThickness+'px solid '+color+';border-radius:'+r+'px;">';
	if(0!=xtalNumber){
		crosshairs+='<span style="color:'+color+';position:absolute;top:'+(r+10)+'px;left:'+(r+10)+'px;border:1px solid '+color+';background-color:yellow;font-weight:bold;padding:2px;">'+xtalNumber+'</span>';
	}
	crosshairs+='<div style="position:absolute;top:'+(r-15)+'px;left:'+r+'px; height:30px;border-left:1px solid '+color+'"></div>';
	crosshairs+='<div style="position:absolute;top:'+r+'px;left:'+(r-15)+'px; width:30px;border-top:1px solid '+color+'; text-align:right; padding-top:5px">';
	crosshairs+='</div>';
	crosshairs+='</div>';
	crosshairs+='</a>';
	$("dropimage").up("div").innerHTML += crosshairs;
	Event.observe("dropimage","click",setCoords);
}

function openNotesBox(){
	var notesBox=$("notes");
	if(null==notesBox || undefined==notesBox){
		return false;
	}
	openCollapsibleBox(notesBox);
	$("notes").scrollTo();			
}


function setXYScales(){
	if($("dropimage").getWidth() <50){
		//image hasn't loaded yet; width should be 0, but use 50 as a safety (padding, IE silliness, etc.)
		window.setTimeout(setXYScales,100);
		return false;
	}
	xScale=(imageWidth  * imageWidthPerPixel ) / $("dropimage").getWidth(); //microns per pixel at image display size (which can vary).
	yScale=(imageHeight * imageHeightPerPixel) / $("dropimage").getHeight();
}
