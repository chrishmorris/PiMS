/********************
 * Global variables *
 ********************/

// Defined in the HTML:
// protocol
// experiments

/****************************
 * Initialisation functions *
 ****************************/

function experimentGroupInit(){
	verifyInputSampleIntegrity();
	resizeTabSetsOnload(); // see widgets.js
	tidyLayout();
	setUpGroupExperiments();
	populateInputSampleSelects(); //...with samples that appear in the group
	setScoresInfoInDom();
	selectAllWells($("plates"));
	enableDisableDeletion();
}

function platesInit(){
	verifyInputSampleIntegrity();
	resizeTabSetsOnload(); // see widgets.js
	tidyLayout();
	// TODO add touch event handlers 
	setUpPlates($("plates"),experiments,startWellSelect,endWellSelect,updateWellSelect,hideWellContents);
	populateInputSampleSelects(); //...with samples that appear in the plate
	setScoresInfoInDom();
	selectAllWells($("plates"));
	enableDisableDeletion();
}

/**
 * Verifies that there is an InputSample in the first experiment for each RefInputSample shown in the "Input 
 * samples" panel. If not, cull all form info from the "Input samples" panel.
 * 
 * Fixes a known issue with Order plates, which may have a protocol with RefInputSamples but not have 
 * corresponding InputSamples in the experiments. This makes all fill options meaningless, and causes
 * trouble server-side and in Copy From Plate.
 * 
 * See PIMS-3398
 */
function verifyInputSampleIntegrity(){
	if(!experiments){ throw "No experiments object found"; }
	if(!protocol){ throw "No protocol object found"; }
	if(!protocol["inputSamples"]){ throw "No input samples found in protocol"; } //but that would mean PIMS-3398 wasn't a problem

	var firstExperiment=experiments[Object.keys(experiments)[0]];
	if(undefined==firstExperiment){
		throw "No experiments found in experiments object";
	}
	var samplesOK=true;
	for(ris in protocol["inputSamples"]){ //ris is "isNNNNN", the refInputSample identifier
		var exptsample=firstExperiment[ris];
		if(undefined==exptsample){
			samplesOK=false;
			break;
		}
	}
	if(!samplesOK){
		//add (none) to the Input Samples section header
		$("inputshead").innerHTML+=" (none)";
		
		//remove all interaction from it, leaving the explanation
		$("inputsamples").select(".param").each(function(risForm){
			risForm.remove();
		});
		
		//instead of empty Input Samples panel, set a sensible default to open
		var newDefaultPanel="";
		if($("groupparams")) { newDefaultPanel=$("groupparams"); }
		else if($("setupparams")) { newDefaultPanel=$("setupparams"); }
		else if($("resultparams")) { newDefaultPanel=$("resultparams"); }
		else { newDefaultPanel=$("statuspanel"); }
		$("inputsamples").removeClassName("current");
		newDefaultPanel.addClassName("current");
	}
}

/**
 * Rearranges elements on plate/group tab. Sets both halves (and mouseover "tooltip" panel) to full height, 
 * and then sets height of the sub-panels on the left to full height minus combined height of all the headers.
 */
function tidyLayout(){
	$("plates").style.height="100%";
	$("experimentcontents").style.height="100%";
	if ($("paramssamples")) {
		$("experimentcontents").style.width=$("paramssamples").getWidth()-10+"px";
	  $("paramssamples").style.height="100%";
	  var headers=$("paramssamples").select("h4");
	  $("paramssamples").select("div.panel").each(function(dv){
		dv.style.height=( $("plates").getHeight() - headers[0].getHeight()*headers.length - headers.length )+"px";
	  });
    }
}

/**
 * Inspects the "scores" panel and sets the following as properties of its DOM object:
 * 
 * scorePdefId - String in the form "pdNNNN", which can be used for lookups in "protocol" or "experiments"
 * scoresList  - ordered array of the possible values for the score
 * 
 * @return false if no "scores" panel
 */
function setScoresInfoInDom(){
	if (!$("paramssamples")) {return;}
	var scoresDivs=$("paramssamples").select("div.score");
	if(1>scoresDivs.length){ 
		//no score parameter - nothing rendered, user can't click it, nothing to do
		return false;
	}
	scoresDivs.each(function(scoresDiv){
		scoresDiv.scorePdefId=$(scoresDiv).down(".param").id;
		scoresDiv.scoresList=[];
		var counter=0;
		while(null!=scoresDiv.down("."+scoresDiv.pdefName + counter)){
			scoresDiv.scoresList.push(scoresDiv.down("."+scoresDiv.pdefName + counter).down("input").value);
			counter++;
		}
	});
}

/**
 * Populates the SELECTs for the input samples, using samples that are already in the plate.
 * Also does target/construct.
 * TODO change "target/construct" thoughout this file to "project", or the appropriate alias
 * Not sure if this makes scientific sense.
 */
function populateInputSampleSelects(){
	//get the first non-empty experiment, and get the keys from it
	var inputSampleKeys={};
	var experimentBlueprints={};
	var wells=$("plates").wells;
	for(var i=0;i<wells.length;i++){
		if(!wells[i].hasClassName("empty")){
			firstExpt=wells[i]["experiment"];
			Object.keys(firstExpt).each(function(key){
				if(key.startsWith("is") && key.length>2){
					inputSampleKeys[key]={};
				}
			});
			break; //only wanted the first non-empty experiment
		}
	}
	//then, for each key, make a Hash of input sample hook and name
	//(hook is unique, so sample should appear once in any Hash)
	Object.keys(experiments).each(function(exptKey){
		if(null==experiments[exptKey]){
			return;
		}
		var experiment=experiments[exptKey];
		Object.keys(inputSampleKeys).each(function(key){
			if(""!=experiment[key]["hook"]){
				inputSampleKeys[key][experiment[key]["hook"]]=experiment[key]["name"];
			}
		});
		if(""!=experiment["blueprintHook"]){
			experimentBlueprints[experiment["blueprintHook"]] = experiment["blueprintName"];
		}
	});
	//for each input sample key, iterate through the Hash and make an <option> in the
	//relevant <select> if the sample isn't already in it
	if ($("inputsamples")) {
	  Object.keys(inputSampleKeys).each(function(key){
		var sel=$("inputsamples").down("."+key).down("select.sample");
		Object.keys(inputSampleKeys[key]).each(function(hashkey){
			//try to set SELECT's value to hook - if it fails, sample isn't in list, so add it
			sel.value=hashkey;
			if(hashkey!=sel.value){
				var opt=document.createElement("option");
				opt.setAttribute("value",hashkey);
				opt.innerHTML=inputSampleKeys[key][hashkey];
				sel.appendChild(opt);
			}
		});
	});}
	//...and do the same for the Targets/Constructs list
	Object.keys(experimentBlueprints).each(function(key){
		sel=$("targets").down(".paramvalue");
		//try to set SELECT's value to hook - if it fails, sample isn't in list, so add it
		sel.value=key;
		if(key!=sel.value){
			var opt=document.createElement("option");
			opt.setAttribute("value",key);
			opt.innerHTML=experimentBlueprints[key];
			sel.appendChild(opt);
		}
	});
}

/**
 * Readies the plates UI for accepting selections, and ties experiments to wells.
 *
 * Plates are rendered as HTML tables, with table cells (TD) representing individual wells.
 * These cells are served with a CSS class of exNNNN, where NNNN is the PiMS dbId
 * of the corresponding experiment; exNNNN also corresponds to the JSON ID of an experiment
 * in the "experiments" object. This function links each TD to an experiment 
 * object, which can then be accessed as well.experiment.
 * 
 * Additionally, TDs are assigned a row and column number, beginning at 0 in the top left of the
 * top left plate and increasing to the bottom and right. If any plates are absent from the layout,
 * the numbering of the TDs in the remaining plates happens as if they were there.
 * 
 * Some variables assigned to the container element:
 * 
 * selectionInProgress	Contains information about the experiment selection (click/drag) currently
 * 						being made by the user - rectangular limits, and type (add to, remove from, or
 * 						replace existing selection). Note that non-plate experiment groups are handled 
 * 						as a one-row plate, so startRow and endRow are always zero in that case.
 * wells				An array of HTML elements representing the wells in the plate(s), or the experiments 
 * 						in a non-plate group.
 * selectedWells		A subset of "wells", HTML elements representing the currently-selected wells/experiments.
 *
 * @param containerElement The HTML element containing all the plate <table>s
 * @param mouseDownHandler The function called on mousedown on a well
 * @param mouseUpHandler   The function called on mouseup on the container element (means user can 
 * 						   be sloppy about selection endpoint, not having to end on a well)
 * @param mouseOverHandler The function called on mouseover on a well
 * @param mouseOutHandler The function called on mouseup on a well
 */
function setUpPlates(containerElement, experimentCollection, mouseDownHandler, mouseUpHandler, mouseOverHandler, mouseOutHandler){
	containerElement.wells=containerElement.select("td.well");
	containerElement.usedWells=containerElement.wells.without("td.empty");
	containerElement.selectedWells=[];
	containerElement.selectionInProgress={ isSelecting:false, selectionType:"replace", startRow:0, endRow:0, startCol:0, endCol:0 };
	containerElement.onselectstart=function(){return false;} //stop select on drag in IE
	containerElement.userSelectionEnabled=true; //used for locking out selection if "group parameters" selected
	containerElement.observe("mouseup",mouseUpHandler);
	containerElement.firstExperiment=null;
	var plates=containerElement.select("table.plate");
	plates.each(function(plate){
		plate.observe("mousedown",mouseDownHandler);
		//TODO plate.observe("touchstart",mouseDownHandler);
		//TODO plate.observe("touchenter",mouseOverHandler);
		//TODO plate.observe("touchmove",mouseOverHandler);
		plate.observe("mouseover",mouseOverHandler);
		plate.observe("mouseout",mouseOutHandler);
		//TODO plate.observe("touchend",mouseOutHandler);
		//TODO plate.observe("touchexit",mouseOutHandler);
		//TODO plate.observe("touchcancelled",mouseOutHandler);
		
		var startRow=0, startCol=0;
		var numRows=plate.select("tr").length-2; //not interested in top and bottom of plate;
		var numCols=plate.down("tr").select("th").length-2; //not interested in left and right of plate;
		if(plate.id=="southwest"||plate.id=="southeast"){ startRow=numRows; }
		if(plate.id=="southeast"||plate.id=="northeast"){ startCol=numCols; }
		var row=startRow;
		plate.select("tr").each(function(tr){
			if(tr.hasClassName("top")||tr.hasClassName("bottom")){ return; }
			var col=startCol;
			tr.select("td.well").each(function(td){
				if(!td.hasClassName("empty")){
					var expClassNames=td.className.match(new RegExp("ex[0-9]+")); //look for "ex1234" in class names
					if(null!=expClassNames){
						var expClass=expClassNames[0];
						td.experiment=experimentCollection[expClass]; //regex matched exNNNNNN, set that experiment to this well
						if(null==containerElement.firstExperiment){
							containerElement.firstExperiment=td.experiment;
						}
					} else {
						//not specifically declared "empty" but no ex1234 class to ID experiment. Something's wrong.
						throw "Well not declared as empty but no experiment specified, at row "+row+", col"+col;
					}
				}
				td.selectionRow=row;
				td.selectionCol=col;

				td.addClassName("row"+row);
				td.addClassName("col"+col);
				td.addClassName("row"+row+"_col"+col); //for easy find by row AND column
				//was td.observe("mousedown",mouseDownHandler);
				// was td.observe("mouseover",mouseOverHandler);
				// was td.observe("mouseout",mouseOutHandler);
				if(td.hasClassName("empty")){
					td.observe("click",addExperimentIfWellEmpty);
				}
				col++;
			});
			row++;
		});
	});
}

/**
 * Readies the experiment group UI for accepting selections, and ties experiments to wells.
 *
 * Non-plate groups are rendered as a series of DIVs, each representing an individual experiment.
 * These DIVs are served with a CSS class of exNNNN, where NNNN is the PiMS dbId
 * of the corresponding experiment; exNNNN also corresponds to the JSON ID of an experiment
 * in the "experiments" object. This function links each DIV to an experiment 
 * object, which can then be accessed as well.experiment.
 * 
 * Additionally, DIVs are assigned a row and column number, so that they can use the same selection
 * code as the plate layout. "Column" increases by one for each experiment, in order. The "row" is 
 * always zero, no matter how many rows the display wraps onto. As far as the selection code is
 * concerned, a non-plate group is a one-row plate.
 */
function setUpGroupExperiments(){
	$("plates").wells=$("plates").select(".groupexperiment");
	$("plates").usedWells=$("plates").wells;
	$("plates").selectedWells=[];
	$("plates").onselectstart=function(){return false;} //stop select on drag in IE
	$("plates").userSelectionEnabled=true; //used for locking out selection if "group parameters" selected
	$("plates").selectionInProgress={ isSelecting:false, selectionType:"replace", startRow:0, endRow:0, startCol:0, endCol:0 };
	$("plates").observe("mouseup",endWellSelect);
	var count=0;
	$("plates").wells.each(function(ex){
		var expClassNames=ex.className.match(new RegExp("ex[0-9]+")); //look for "ex1234" in class names
		if(null!=expClassNames){
			var expClass=expClassNames[0];
			ex.experiment=experiments[expClass]; //regex matched exNNNNNN, set that experiment to this well
		} else {
			//no ex1234 class to ID experiment. Something's wrong.
			ex.style.backgroundColor="red";
			throw "Experiment block shown but no experiment specified";
		}
		ex.selectionRow=0;
		ex.selectionCol=count;	//for selection purposes, this is a one-row plate
		count++;
		ex.observe("mousedown",startWellSelect);
		ex.observe("mouseover",updateWellSelect);
		ex.observe("mouseout",hideWellContents);
	});
}


/****************************
 * Well selection functions *
 ****************************/

/**
 * Selects all wells, with the exception of any empty ones.
 */
function selectAllWells(containerElement){
	var selectedWells=containerElement.selectedWells;
	var wells=containerElement.wells;
	wells.each(function(well){
		if(!well.hasClassName("empty")){
			selectedWells.push(well);
		}
	});
	showSelectedWells(containerElement);
}

function getPlatesContainer(event) {
	var ret = event.currentTarget || event.srcElement;
	if(!ret.hasClassName("platescontainer")){
		ret=ret.up(".platescontainer");
	}
	return ret;
}

/**
 * Handles mousedown over a well (or experiment). Initialises the selectionInProgress
 * option with the correct type, based on whether Ctrl or Shift is pressed, setting
 * the bounds of the current selection to the clicked well.
 *
 * @param event The mouseover event
 * TODO make a similar handler for a touch event
 */
function startWellSelect(e){
	var event = e || window.event;  
	event.preventDefault();
	if (e.stopPropagation) {
        e.stopPropagation();
    } else {
        e.cancelBubble = true;
    }
	var well=event.element();
	if ("TD"!==well.tagName
			&& "DIV"!==well.tagName
	) {
		return;
		// could throw new Error("Well expected: "+well.tagName);
	}
	var platesContainer=getPlatesContainer(event);
	if(!platesContainer.userSelectionEnabled){
		alert("All experiments are automatically selected when updating group-level parameters.")
		return false;
	}
	var selectionInProgress=platesContainer.selectionInProgress;
	var wells=platesContainer.wells;
	if(event.shiftKey){
		selectionInProgress.selectionType="add"; 
	} else if(event.ctrlKey || event.metaKey){
		selectionInProgress.selectionType="remove"; 
	} else {
		selectionInProgress.selectionType="replace";
		wells.each(function(well){
			well.removeClassName("selected");
		});
	}
	selectionInProgress.isSelecting=true;
	selectionInProgress.startRow=well.selectionRow;
	selectionInProgress.endRow=well.selectionRow;
	selectionInProgress.startCol=well.selectionCol;
	selectionInProgress.endCol=well.selectionCol;
	drawSelectionRange(platesContainer);
}

/**
 * Handles mouseover while dragging a selection rectangle. Updates the bounds of the
 * selection, then calls drawSelectionRange() to show the current selection.
 *
 * @param event The mouseover event
 * TODO make a similar handler for a touch event
 */
function updateWellSelect(e){
	var event = e || window.event;  
    event.preventDefault();  
    if (e.stopPropagation) {
        e.stopPropagation();
    } else {
        e.cancelBubble = true;
    }
    var table = this || event.currentTarget;
    if ("TABLE"!==table.tagName) {throw new Error("Expected table: "+table.tagName);}
    var well=event.element();
    if (event.touches) {well=getCell(table, event);}
    if (!well) {
    	throw new Error("No well"); return; // on border between cells
    } 
	if ("TD"!==well.tagName
			  && 	"DIV"!==well.tagName	
	) {
		return; // could throw new Error("Expected well, found: "+well.tagName); 
	}
	var platesContainer=well.up(".platescontainer");
	var selectionInProgress=platesContainer.selectionInProgress;
	if(!selectionInProgress.isSelecting) { showWellContents(well); return false; }
	if(!platesContainer.userSelectionEnabled){
		return false;
	}
	selectionInProgress.endRow=well.selectionRow;
	selectionInProgress.endCol=well.selectionCol;
	drawSelectionRange(platesContainer); 
}
function getCell(table, x, y) {
    for (var j=0; j<table.rows.length;j++) {      
           var row = table.rows.item(j);
           var cells = row.cells;
           for (var i=0; i<cells.length;i++) {
             var td = cells.item(i);
             var rect = td.getBoundingClientRect();
             if (rect.left<=x && rect.right>x
                && rect.top<=y && rect.bottom>y       
             ) {
            	 //alert(td);
                 return td;
             }
           }
    }
}

/**
 * Handles mouseup over well while dragging selection rectangle - finalises selection.
 * 
 * @param event The mouseup event
 * TODO make a similar handler for a touch event
 */
function endWellSelect(e){
	var event = e || window.event; 
	event.preventDefault();	
	var platesContainer=getPlatesContainer(event);
	if (!platesContainer) {throw new Error("No plates container for: "+event.currentTarget);}
	event.stop(); //cancel standard event propagation
	if(!platesContainer.userSelectionEnabled){
		return false;
	}
	var selectionInProgress=platesContainer.selectionInProgress;
	var wells=platesContainer.wells;
	//for bounds checking, swap start and end values if end is above or left of start
	var selLeft=selectionInProgress.startCol;
	var selRight=selectionInProgress.endCol;
	var selTop=selectionInProgress.startRow;
	var selBottom=selectionInProgress.endRow;
	if(selTop>selBottom){ tmp=selTop; selTop=selBottom; selBottom=tmp; }
	if(selLeft>selRight){ tmp=selRight; selRight=selLeft; selLeft=tmp; }
	if("replace"==selectionInProgress.selectionType){
		//wipe array before adding new selection
		platesContainer.selectedWells=[];
	}
	if("add"==selectionInProgress.selectionType || "replace"==selectionInProgress.selectionType){
		//add each well in new selection to existing
		wells.each(function(well){
			if(well.selectionRow>=selTop && well.selectionRow<=selBottom && well.selectionCol>=selLeft && well.selectionCol<=selRight){ 
				if(!well.hasClassName("empty") && 0>platesContainer.selectedWells.indexOf(well)){
					platesContainer.selectedWells.push(well);
				}
			}
		});
	} else if("remove"==selectionInProgress.selectionType){
		//remove each well in the new selection from the existing selection
		wells.each(function(well){
			if(well.selectionRow>=selTop && well.selectionRow<=selBottom && well.selectionCol>=selLeft && well.selectionCol<=selRight){ 
				platesContainer.selectedWells=$(platesContainer.selectedWells).without(well);
			}
		});
	}
	//if user has wiped out entire selection, select the first non-empty well
	if(0==platesContainer.selectedWells.length){
		for(var i=0;i<wells.length;i++){
			if(!wells[i].hasClassName("empty")){
				platesContainer.selectedWells.push(wells[i]);
				break;
			}
		}
	}
	//reset selection in progress object ready for next time
	selectionInProgress.isSelecting=false;
	selectionInProgress.startCol=0;
	selectionInProgress.endCol=0;
	selectionInProgress.startRow=0;
	selectionInProgress.endRow=0;
	selectionInProgress.selectionType="";
	//and lastly, show the new selection
	showSelectedWells(platesContainer); 
}

/**
 * Called while dragging to select wells. Shows which wells are in the current selection range.
 * 
 * For plates, this is a 2D rectangle numbered from 0,0 at top left of top-left plate.
 * For non-plates, this is 1D, effectively a 1-row plate.
 */
function drawSelectionRange(platesContainer){
	var selectionInProgress=platesContainer.selectionInProgress;
	var wells=platesContainer.wells;
	var selectionClass="selecting";
	if("add"==selectionInProgress.selectionType){
		selectionClass="selectingAdd";
	} else if("remove"==selectionInProgress.selectionType) {
		selectionClass="selectingSubtract";
	}
	var selLeft=selectionInProgress.startCol;
	var selRight=selectionInProgress.endCol;
	var selTop=selectionInProgress.startRow;
	var selBottom=selectionInProgress.endRow;
	if(selTop>selBottom){ tmp=selTop; selTop=selBottom; selBottom=tmp; }
	if(selLeft>selRight){ tmp=selRight; selRight=selLeft; selLeft=tmp; }
	//Performance of loop with 4x96-well plate: FF 9-60ms, IE 45-200ms
	// - varies with size of selection.
	wells.each(function(well){
		if(well.selectionRow>=selTop && well.selectionRow<=selBottom && well.selectionCol>=selLeft && well.selectionCol<=selRight){
			well.addClassName(selectionClass);
		} else {
			well.removeClassName(selectionClass);
		}
	});
}

/**
 * Highlight the selected experiments, and call updateFormValues to ensure that the forms reflect what's
 * currently selected.
 * 
 * @TODO improve performance. Fine in Firefox, not amazing in IE. Class "exNNNN" is believed to be redundant
 * 		 after page initialisation, which would simplify the problem - could simply set well.className to
 * 		 "well" or "groupexperiment" as appropriate in the first loop, doing the same job as the four calls
 * 		 to removeClassName.
 */
function showSelectedWells(platesContainer){
	var wells=platesContainer.wells;
	var selectedWells=platesContainer.selectedWells;
	platesContainer.uppermostSelectionRow=selectedWells[0].selectionRow;
	platesContainer.lowermostSelectionRow=selectedWells[0].selectionRow;
	platesContainer.leftmostSelectionCol=selectedWells[0].selectionCol;
	platesContainer.rightmostSelectionCol=selectedWells[0].selectionCol;
	wells.each(function(well){
		well.removeClassName("selected");
		well.removeClassName("selecting");
		well.removeClassName("selectingAdd");
		well.removeClassName("selectingSubtract");
	});
	selectedWells.each(function(well){
		well.addClassName("selected");
		if(well.selectionRow>platesContainer.lowermostSelectionRow){
			platesContainer.lowermostSelectionRow=well.selectionRow;
		} else if(well.selectionRow<platesContainer.uppermostSelectionRow){
			platesContainer.uppermostSelectionRow=well.selectionRow;
		} 
		if(well.selectionCol>platesContainer.rightmostSelectionCol){
			platesContainer.rightmostSelectionCol=well.selectionCol;
		} else if(well.selectionCol<platesContainer.leftmostSelectionCol){
			platesContainer.leftmostSelectionCol=well.selectionCol;
		} 
	});
	enableDisableDeletion();
	updateFormValues();
}

/**
 * Update all the form fields in the "parameters and samples" area to reflect the current
 * selection of experiments. If a value is the same for all selected experiments, show it,
 * otherwise show "(various)". 
 */
function updateFormValues(){
	if(undefined==$("paramssamples")){
		//this isn't the main window, so bail;
		return false;
	}
	//copy first selected well's values into new object
	var selectedWells=$("plates").selectedWells;
	var paramValues={};
	var firstExpt=selectedWells[0].experiment;
	for (key in firstExpt){
		if(key.startsWith("pd")||key=="status"||key=="milestoneAchieved"||key=="blueprintHook"){
			paramValues[key]=firstExpt[key];
		} else if(key.startsWith("is")){
			paramValues[key]={};
			paramValues[key]["hook"]=firstExpt[key]["hook"];
			paramValues[key]["amount"]=firstExpt[key]["amount"];
		}
	}
	//now compare each param in each expt. if difference found, remove key from "values" object
	selectedWells.each(function(well){
		var expt=well.experiment;
		for(key in paramValues){
			if(key.startsWith("pd")||key=="status"||key=="milestoneAchieved"||key=="blueprintHook"){
				if(expt[key].toString()!=paramValues[key].toString()){
					delete paramValues[key];
				}
			} else if(key.startsWith("is")){
				if(expt[key]["hook"]!=paramValues[key]["hook"]){
					delete paramValues[key]["hook"];
				}
				if(expt[key]["amount"]!=paramValues[key]["amount"]){
					delete paramValues[key]["amount"];
				}
			}
		}
	});
	//and finally, iterate through all isNNNN and pdNNNN values in firstExperiment, look for a form field with that class -
	// if present in paramValues, set that value, otherwise (Various)
	for(key in firstExpt){
		var param=$("paramssamples").select("."+key)[0]; //assume one at most
		if(!param){
			continue;
		}
		if(key.startsWith("pd")){
			if(param.up("div.score")){
				if(key in paramValues){
					param.down("input[value='"+firstExpt[key]+"']").checked="checked";
				} else {
					param.down("input[value='(various)']").checked="checked";
				}
				/*JMD*/
				if(param.down("div#__sequenceLink")){
					param.down("div#__sequenceLink").innerHTML="<br /><a href=\""+contextPath+"/AlignSequence/"+firstExpt.hook+"\"><img src=\""+contextPath+"/skins/default/images/icons/types/small/experiment.gif\"/> View sequence alignment</a>";
				}
			}else if(key in paramValues){
				param.down(".paramvalue").value=firstExpt[key].unescapeHTML();
			} else {
				param.down(".paramvalue").value="(various)";
			}
		} else if(key=="status"||key=="blueprintHook"){
			if(key in paramValues){
				param.down(".paramvalue").value=firstExpt[key];
			} else {
				param.down(".paramvalue").value="(various)";
			}
		} else if(key=="milestoneAchieved" && $("milestone")){
			if(key in paramValues){
				$("milestoneAchieved").value=firstExpt[key];
			} else {
				$("milestoneAchieved").value="(various)";
			}
		} else if(key.startsWith("is")){
			if(undefined!=paramValues[key]["amount"]){
				param.down(".amount").value=parseFloat(firstExpt[key]["amount"]);
			} else {
				param.down(".amount").value="(various)";
			}
			if(undefined!=paramValues[key]["hook"]){
				param.down(".sample").value=firstExpt[key]["hook"];
				//then check value - if not right, option doesn't exist, so make it and set it.
				if(param.down(".sample").value!=firstExpt[key]["hook"]){
					param.down(".sample").innerHTML+="<option value=\""+firstExpt[key]["hook"]+"\">"+firstExpt[key]["name"]+"</option>";
					param.down(".sample").value=firstExpt[key]["hook"];
				}
			} else {
				param.down(".sample").value="(various)";
			}
		}
	}
	exptStatusOnchange();
}


function exptStatusOnchange(){
	if (!$("exptstatus")) {return;};
	if("OK"==$("exptstatus").value){
		var allExptsHaveConstructs=true;
		$("plates").selectedWells.each(function(well){
			if(""==well.experiment.blueprintHook){
				allExptsHaveConstructs=false;
			}
		});
		if($("cantsetmilestone")){
			if(allExptsHaveConstructs){
				$("milestone").style.display="";
				$("cantsetmilestone").style.display="none";
			} else {
				$("milestone").style.display="none";
				$("cantsetmilestone").style.display="";
			}
		}
	} else if($("cantsetmilestone")){
		$("cantsetmilestone").style.display="none";
		$("milestone").style.display="none";
	}
}

/*********************************************
 * Mouseover well contents display functions *
 *********************************************/

/**
 * Show the parameters and samples for this experiment, on mouseover.
 * 
 * @param well The HTML element representing the well (or experiment, in a non-plate group)
 * @return false if well is empty
 */
function showWellContents(well){
	if(!$("experimentcontents")){ return false; }
	var tagName = well.tagName;
	// with <img> icons in the well, we can get the <img>, not the well itself
	// - so make sure we're dealing with the well before proceeding.
	if($(well).up(".well")){
		well=$(well).up(".well");
	} else if($(well).up(".groupexperiment")){
		well=$(well).up(".groupexperiment");
	} 
	
	if(well.hasClassName("empty")){
		hideWellContents();
		return false;
	}
	var expt=well.experiment;
	var scorePdefIds=[];
	if ($("paramssamples")) $("paramssamples").select(".score").each(function(scoreDiv){
		scorePdefIds.push(scoreDiv.scorePdefId);	
	});
	$("experimentcontents").innerHTML="";
	var html=[];
	html.push("<h3>Name: "+expt.name+"</h3>");

	var inputs=[];
	for(inp in protocol.inputSamples){

		//PIMS-3398 - Order plates may have RefInputSample in protocol with no corresponding InputSample in experiment
		if(!expt[inp]){
			continue;
		}

		var input=[];
		input.push(protocol.inputSamples[inp]["name"]+": ");
		input.push(expt[inp]["amount"]);
		input.push(" ");
		if(""==expt[inp]["name"]){
			input.push("Unspecified sample");
		} else {
			input.push(expt[inp]["name"]);
		}
		input.push("<br/>");
		inputs.push(input.join(""));
	}
	html.push(inputs.join(""));
	
	var groupParams=[];
	for(param in protocol.groupParameters){
		var gp=[];
		gp.push(protocol.groupParameters[param]["name"]+": ");
		gp.push(expt[param]);
		gp.push("<br/>");
		groupParams.push(gp.join(""));
	}
	html.push(groupParams.join(""));

	var setupParams=[];
	for(param in protocol.setupParameters){
		if(0>scorePdefIds.indexOf(param)){
			var sp=[];
			sp.push(protocol.setupParameters[param]["name"]+": ");
			sp.push(expt[param]);
			sp.push("<br/>");
			setupParams.push(sp.join(""));
		}
	}
	html.push(setupParams.join(""));

	var resultParams=[];
	for(param in protocol.resultParameters){
		if(0>scorePdefIds.indexOf(param)){
			var rp=[];
			rp.push(protocol.resultParameters[param]["name"]+": ");
			rp.push(expt[param]);
			rp.push("<br/>");
			resultParams.push(rp.join(""));
		}
	}
	html.push(resultParams.join(""));

	var outputs=[];
	for(outp in protocol.outputSamples){
		if (outp in expt ) {
		var output=[];
		output.push(protocol.outputSamples[outp]["name"]+": ");
		output.push(expt[outp]["name"]);		
		output.push("<br/>");
		outputs.push(output.join(""));
		} 
	}
	html.push(outputs.join(""));

	html.push("Target/construct: ");
	if(""!=expt.blueprintName){
		html.push(expt.blueprintName);
	} else {
		html.push("None specified");
	}
	html.push("<br/>Status:"+expt.status);

	if ($("paramssamples")) $("paramssamples").select(".score").each(function(scoreDiv){
		var scorePdefId=scoreDiv.scorePdefId;
		if(undefined!=protocol["resultParameters"][scorePdefId]){
			html.push("<br/>");
			html.push(protocol["resultParameters"][scorePdefId]["name"]+": "+expt[scorePdefId]);
			if (scoreDiv.pdefName == "SCORE"){
				html.push("<br/>");
				html.push("PCR Product length: "+expt.pcrProductSize);
			}
			if (scoreDiv.pdefName == "MW"){
				html.push("<br/>");
				html.push("Protein MW: "+expt.proteinMW);
			}
		} else if(undefined!=protocol["setupParameters"][scorePdefId]){
			html.push("<br/>");
			html.push(protocol["setupParameters"][scorePdefId]["name"]+": "+expt[scorePdefId]);
			if (scoreDiv.pdefName == "SCORE"){
				html.push("<br/>");
				html.push("PCR Product length: "+expt.pcrProductSize);
			}
			if (scoreDiv.pdefName == "MW"){
				html.push("<br/>");
				html.push("Protein MW: "+expt.proteinMW);
			}
		}
	});


	$("experimentcontents").innerHTML=html.join("");
	$("experimentcontents").style.display="block";
}

function hideWellContents(){
	if(!$("experimentcontents")){ return false; }
	$("experimentcontents").style.display="none";
	$("experimentcontents").innerHTML="";
}

/**********************************
 * Params panel display functions *
 **********************************/


/**
 * In the plates/group tab, open the appropriate panel in the "parameters/samples" area to the left.
 * This is assumed (by $header.next()) to be the immediate following sibling of the clicked header.
 * Only one panel can be open at a time, so collapse all the others.
 * 
 * @param header The heading element clicked by the user.
 */
function showPanel(header){
	var oldPanel=$("paramssamples").down("div.current")
	var wells=$("plates").wells;
	if(undefined!=oldPanel){ //safety in case previous panel change fell over
		oldPanel.removeClassName("current");
	}
	//clear content of wells before showing next panel
	if("statuspanel"==oldPanel.id){
		wells.each(function(well){
			well.removeClassName("status_To_be_run"); 
			well.removeClassName("status_In_process"); 
			well.removeClassName("status_OK"); 
			well.removeClassName("status_Failed"); 
		});
	} else if(oldPanel.hasClassName("score")){
		var scorePdefName=oldPanel.pdefName;
		wells.each(function(well){
			var counter=0;
			while(null!=oldPanel.down("."+scorePdefName+counter)){
				well.removeClassName(scorePdefName + counter);
				counter++;
			}
		});
	}
	$(header).next().addClassName("current");
	//if group params panel is being opened, select all wells and lock out selection 
	//mechanism, else unlock it
	if("groupparams"==(header).next().id){
		if("groupparams"!=oldPanel.id){
			//don't do this if the group params panel is already open when user clicks its header.
			$("plates").oldSelection=$("plates").selectedWells.clone();
		}
		$("plates").userSelectionEnabled=false;
		selectAllWells($("plates"));
	} else {
		$("plates").userSelectionEnabled=true;
		if(null!=$("plates").oldSelection){
			$("plates").selectedWells=$("plates").oldSelection.clone();
		}
		$("plates").oldSelection=null;
		showSelectedWells($("plates"));
	}
	setWellIcons(header);
}

/**
 * Where the user clicks something like "Experiments", show an icon in each well linking to the
 * relevant PiMS record. Otherwise, clear the well.
 * 
 * @param clickedHeader 
 */
function setWellIcons(clickedHeader){
	var wells=$("plates").wells;
	var panel=$(clickedHeader).next();
	var sectionId=panel.id;
	if(panel.hasClassName("score")){
		var scorePdefId=panel.down(".param").id; //ASSUME only one
		var scorePdefName=panel.pdefName; 
		var scoresList=panel.scoresList;
	}

	wells.each(function(well){
		if(!well.hasClassName("empty")){
			var html="&nbsp;";
			if("expts"==sectionId){ 
				html="<a href=\""+contextPath+"/View/"+well.experiment.hook+"\"><img src=\""+contextPath+"/skins/default/images/icons/types/small/experiment.gif\"/></a>"; 
			} else if(panel.hasClassName("outputsample")){
				outputSampleKey=clickedHeader.id;
				html="<a href=\""+contextPath+"/View/"+well.experiment[outputSampleKey].hook+"\"><img src=\""+contextPath+"/skins/default/images/icons/types/small/sample.gif\"/></a>"; 
			} else if("statuspanel"==sectionId){
				well.removeClassName("status_To_be_run");
				well.removeClassName("status_In_process");
				well.removeClassName("status_OK");
				well.removeClassName("status_Failed");
				well.addClassName("status_"+well.experiment.status); 
			} else if(panel.hasClassName("score")){
				for(var i=0;i<scoresList.length;i++){
					well.removeClassName(scorePdefName+i);
				}
				var scoreNum=scoresList.indexOf(well.experiment[scorePdefId]);
				well.addClassName(scorePdefName+scoreNum);
			}
			well.innerHTML=html;
		}
	});
}

/*****************************************
 * Experiment status update functions    *
 *****************************************/

function updateExperimentStatuses(elem){
	var wells=$("plates").wells;
	var selectedWells=$("plates").selectedWells;
	//elem is the submit button in the form, or the form; so make sure it's the form before continuing
	elem=$(elem);
	if(elem.form){ elem=elem.form; }
	var statusField=elem.down(".paramvalue");
	if("(various)"==statusField.value){
		alert("No status set - nothing to do here");
		return false;
	}
	expStatus=statusField.value;
	var postBodyParts=[];
	selectedWells.each(function(well){
		postBodyParts.push(well.experiment.hook+":status="+expStatus)
	});
	if($("milestone") && "none"!=$("milestone").style.display){
		if("OK"==expStatus){
			var milestoneAchieved=$("milestoneAchieved").value;
			if("(various)"!=milestoneAchieved){
				selectedWells.each(function(well){
					postBodyParts.push(well.experiment.hook+":milestoneAchieved="+milestoneAchieved)
				});
			}
		} else {
			selectedWells.each(function(well){
				postBodyParts.push(well.experiment.hook+":milestoneAchieved=false")
			});
		}
	}
	var postBody=postBodyParts.join("&");
	updateSelectedExperiments(postBody,updateExperimentStatuses_onSuccess);
}
	
function updateExperimentStatuses_onSuccess(transport){
	var exptsText="{"+transport.responseText+"}";
	try{
		var updatedExperiments=exptsText.evalJSON();
		Object.keys(updatedExperiments).each(function(key){
			var expt=updatedExperiments[key];
			var exptToUpdate=experiments[key];
			exptToUpdate["status"]=expt["status"];
			exptToUpdate["milestoneAchieved"]=expt["milestoneAchieved"];
		});
		setWellIcons($("statushead"));
		closeModalDialog();
	}catch(err){
		alert("Error on updating. The page will reload when you click OK.")
		document.location.reload();
	}}

/****************************************
 * Experiment score update functions    *
 ****************************************/

function updateExperimentScores(elem){
	var wells=$("plates").wells;
	var selectedWells=$("plates").selectedWells;
	//elem is the submit button in the form, or the form; so make sure it's the form before continuing
	elem=$(elem);
	if(elem.form){ elem=elem.form; }
	var scoreDiv=elem.up(".score");
	var scorePdefId=scoreDiv.scorePdefId;
	var pdef=protocol.resultParameters[scorePdefId];
	if(null==pdef || undefined==pdef){
		pdef=protocol.setupParameters[scorePdefId];
	}
	if(null==pdef || undefined==pdef){
		throw "Can't find the parameter definition for score - tried in resultParameters and setupParameters";
	}
	var pdefHook=pdef.hook;
	var valueToSend="";
	var radios=scoreDiv.select("input[type='radio']");
	radios.each(function(rad){
		if(rad.checked) {
			valueToSend=rad.value;
		}
	});
	if("(various)"==valueToSend){
		alert("Nothing to do here. Select a score to apply to all wells, then click Update.");
		return false;
	}
	var postBodyParts=[];
	postBodyParts.push("parameterDefinition="+pdefHook);
	selectedWells.each(function(well){
		postBodyParts.push("&");
		postBodyParts.push(well.experiment.hook);
		postBodyParts.push("=");
		//postBodyParts.push(valueToSend);
		postBodyParts.push(encodeURIComponent(valueToSend)); 
	});
	var postBody=postBodyParts.join("");
	updateSelectedExperiments(postBody,updateExperimentScores_onSuccess);
	scoreDiv.addClassName("updatingScore");
}

function updateExperimentScores_onSuccess(transport){
	var scoreDiv=$("paramssamples").down(".updatingScore");
	scoreDiv.removeClassName("updatingScore");
	var scorePdefId=scoreDiv.scorePdefId;
	var scorePdefName=scoreDiv.pdefName;
	var scoresList=scoreDiv.scoresList;
	var wells=$("plates").wells;
	var selectedWells=$("plates").selectedWells;
	
	var exptsText="{"+transport.responseText+"}";
	try{
		//remove old score icons and colours
		selectedWells.each(function(well){
			var oldScore=well.experiment[scorePdefId];
			well.removeClassName(scorePdefName+scoresList.indexOf(oldScore));
		});
		//parse the response, and update the score parameter for each experiment
		var updatedExperiments=exptsText.evalJSON();
		Object.keys(updatedExperiments).each(function(key){
			var expt=updatedExperiments[key];
			var exptToUpdate=experiments[key];
			exptToUpdate[scorePdefId]=expt[scorePdefId];
		});
		//set new icons and score colours
		selectedWells.each(function(well){
			var newScore=well.experiment[scorePdefId];
			
			well.addClassName(scorePdefName+scoresList.indexOf(newScore));
		});
		//and finish
		closeModalDialog();
	}catch(err){
		alert("Error on updating. The page will reload when you click OK.\n\nError: "+err)
		document.location.reload();
	}
}

/********************************************
 * Experiment parameter update functions    *
 ********************************************/

/**
 * Extracts the hook of the parameter definition and the parameter value from the submitted form, and 
 * passes these to updateParamOnSelectedExperiments.
 * 
 * @param elem The submit button clicked by the user. The function can also handle this being the parent form element.
 */
function updateParameterValue(elem){ 
	 //elem is the submit button in the form, or the form; so make sure it's the form before continuing
	 elem=$(elem);
	 if(elem.form){ elem=elem.form; }
	 var param=elem.down(".paramvalue");
	 updateParamOnSelectedExperiments(param.id,param.value);
}

/**
 * Builds the request body to update a parameter value on the currently selected
 * experiments, then calls updateSelectedExperiments to do the AJAX POST.
 * 
 * @param pdefHook The hook of the ParameterDefinition, in the protocol
 * @param paramValue The value to set for the parameter
 */
function updateParamOnSelectedExperiments(pdefHook,paramValue){
	var wells=$("plates").wells;
	var selectedWells=$("plates").selectedWells;
	var postBodyParts=[]; //Use an array and join("") for string concatenation, for performance
	postBodyParts.push("parameterDefinition="+pdefHook);
	postBodyParts.push("&");
	postBodyParts.push(getPostBodyForExperiments(selectedWells,"",paramValue));
	var postBody=postBodyParts.join("");
	updateSelectedExperiments(postBody,updateParamOnSelectedExperiments_onSuccess);
}

/**
 * Success handler for AJAX update of a parameter value. Parses the JSON response and updates
 * the local copy of "experiments" with the parameter values found in the JSON, which contains
 * only the modified experiments.
 * 
 * Performance: Currently this does all parameters, even though the submitted form only changed one.
 * 				(This avoids having to remember state through the AJAX request/response cycle.)
 *  			Don't optimise yet, in case we want an "Update all" button. If performance gains
 * 				are needed on parameter update, this would be a good place to start.
 * 
 * @param transport The response object, containing the JSON response from the server
 */
function updateParamOnSelectedExperiments_onSuccess(transport){
	var exptsText="{"+transport.responseText+"}";
	try{
		var updatedExperiments=exptsText.evalJSON();
		Object.keys(updatedExperiments).each(function(key){
			var expt=updatedExperiments[key];
			var exptToUpdate=experiments[key];
			Object.keys(expt).each(function(prop){
				if(prop.startsWith("pd")){
					exptToUpdate[prop]=expt[prop];
				}
			});
		});
		closeModalDialog();
	}catch(err){
		alert("Error on updating. The page will reload when you click OK.\n\nError: "+err)
		document.location.reload();
	}
}


/**********************************************
 * Common experiment role update functions    *
 **********************************************/

/**
 * Begin a search for suitable roles, in a modal window. 
 * 
 * @global mruToUpdate Used to store a reference to the SELECT
 * @param sel The SELECT in question
 * @param dbModelClass The PiMS class name, e.g. "org.pimslims.model.sample.Sample"
 * @param extraRequestParameters An object of name-value pairs, to be appended to the search URL
 * @param roleName The user-friendly name of the role, used in the modal window title
 * @TODO Reset the SELECT to its old value once the window is opened - user may cancel.
 */
function showRoleSearch(sel,dbModelClass,extraRequestParameters,roleName){
    mruToUpdate=$(sel); //mruToUpdate is global variable defined in widgets.js
    var searchURL=contextPath+"/Search/"+dbModelClass+
    	"?isInModalWindow=true" +  
    	"&isGroupExperiment=true"+
		"&experimentGroup="+groupHook;
    for(p in extraRequestParameters){
    	searchURL+="&"+p+ "=" +extraRequestParameters[p];
    }
    openModalWindow(searchURL,
    		"Choose "+roleName);

    //put SELECT back as it was, in case user cancels modal window
    if(undefined!=sel.oldValue||""==sel.oldValue){
    	sel.value=sel.oldValue;
    }
    	
}

/**
 * After the user has chosen a role in the pop-up search, add it to the appropriate
 * SELECT and select it.
 * 
 * @global mruToUpdate The SELECT where we clicked Search to start all this off
 * @param resultObject An object provided by the search page, containing name and hook
 * 					   of the chosen role.
 */
function addRoleToSelect(resultObject){
	//first, see if the chosen role was already in the select to begin with. 
	//Set the value...
	mruToUpdate.value=resultObject["hook"];
	//...then check it, and if it isn't what we set it to...
	if(resultObject["hook"]!=mruToUpdate.value){
		//...we need to create the option...
		var opt=document.createElement("option");
		opt.setAttribute("value",resultObject["hook"]);
		opt.innerHTML=resultObject["name"];
		mruToUpdate.appendChild(opt);
		//...and select it
		mruToUpdate.selectedIndex=mruToUpdate.options.length-1;
	}
	//Lastly, tidy up and close the window
	mruToUpdate=null;
	closeModalWindow();
}


/***************************************************
 * Experiment target/construct update functions    *
 ***************************************************/

/**
 * Handle the onchange element of a target SELECT.
 * 
 * @param elem The select (but the function can take an option)
 */
function targetSelectOnChange(elem){
	//make sure we're dealing with the SELECT, not the OPTION
	elem=$(elem);
	if(elem.up("select")) { elem=elem.up("select"); }
	//now what to do
	if("search"==elem.value){
		showRoleSearch(elem,"org.pimslims.model.target.ResearchObjective",{"callbackFunction":"addResearchObjectiveToSelect"},"Target/Construct"); 
		return false;
	}
	//otherwise, just carry on with default event handling
	return true;
}

/**
 * After the user has chosen a target/construct in the pop-up search, add it to the appropriate
 * SELECT and select it.
 *
 * This is just a pass-through function, for consistency with sample search handling
 * 
 * @param resultObject An object provided by the search page, containing name and hook
 * 					   of the chosen sample.
 */
function addResearchObjectiveToSelect(resultObject){
	addRoleToSelect(resultObject);
}

function updateConstructOnSelectedExperiments(elem){
	 elem=$(elem);
	 if(elem.form){ elem=elem.form; }
	 var sel=$(elem).down(".researchObjective");
	 if("(various)"==sel.value){
		alert("Construct is set to (various) - nothing to do here.");
		return false;
	 }
	 var constructHook=sel.value;
	 var postBody=getPostBodyForExperiments($("plates").selectedWells,"researchObjective",constructHook);
	 updateSelectedExperiments(postBody,updateConstructOnSelectedExperiments_onSuccess);
}

function updateConstructOnSelectedExperiments_onSuccess(transport){
	//generic handler
	updateLocalExperimentDataAfterAJAX(transport);
	closeModalDialog();
}


/*****************************************
 * Experiment sample update functions    *
 *****************************************/

/**
 * Handle the onchange element of an input sample SELECT. The search is restricted by the
 * relevant refInputSample's sample category. Clicking "Add" in the search page will call 
 * addSampleToSelect, to finish the job.
 * 
 * @param elem The select (but the function can take an option)
 */
function sampleSelectOnChange(elem){
	//make sure we're dealing with the SELECT, not the OPTION
	elem=$(elem);
	if(elem.up("select")) { elem=elem.up("select"); }
	//now what to do
	if("search"==elem.value){ 
	    var sampleCategory="";
	    var wrapperDiv=$(elem).up("div.param");
	    var classes=$w(wrapperDiv.className); // $w makes an array from the string, separating on whitespace
		for(i=0;i<classes.length;i++){
			if(classes[i].startsWith("is")){
				sampleCategory=protocol["inputSamples"][classes[i]]["sampleCategoryName"]; // Unusually, name not hook. See line 917 of PIMSServlet.
				break;
			}
		}
		if(""==sampleCategory) { throw "Couldn't find 'isNNNN' class for input sample";}
		showRoleSearch(elem,"org.pimslims.model.sample.Sample",{"sampleCategories":sampleCategory },sampleCategory ); 
		return false;
	}
	//otherwise, just carry on with default event handling
	return true;
}	


/**
 * After the user has chosen a sample in the pop-up search, add it to the appropriate
 * SELECT and select it.
 * 
 * This is just a pass-through function, avoids messing with existing callbackFunction in sample search
 * 
 * @param resultObject An object provided by the search page, containing name and hook
 * 					   of the chosen sample.
 */
function addSampleToSelect(resultObject){
	addRoleToSelect(resultObject);
}

/**
 * Begin submission of a sample form. Does basic validation and formatting (concatenation of amount and unit)
 * before passing on to updateSampleOnSelectedExperiments.
 * 
 * @param elem The clicked submit button in the form. The function can also handle this being the form itself.
 */
function updateSampleAndAmount(elem){ 
	//elem is the submit button in the form, or the form; make sure it's the form before continuing
	 elem=$(elem);
	 if(elem.form){ elem=elem.form; }
	 var risHook=elem.down(".risHook").value;
	 //find sample hook to submit - pass it into next function, handle (various) there
	 var sampleHook=elem.down(".sample").value;
	 //and decide whether to submit an amount
	 var amount=elem.down(".amount").value;
	 var displayUnit=elem.down(".displayUnit").value;
	 var amountToSubmit="";
	 if(""!=amount && "(various)"!=amount){
		 if(!isNumeric(amount)){ //uses isNumeric in validation.js - can't validate directly due to (various)
			 alert("Amount must be a numeric value");
			 return false;
		 } else {
			amountToSubmit=amount+displayUnit; 
		 }
	 }
	 updateSampleOnSelectedExperiments(risHook,sampleHook,amountToSubmit);
}

/**
 * Builds the request body to update sample and/or amount on the currently selected
 * experiments, then calls updateSelectedExperiments to do the AJAX POST.
 * 
 * @param risHook The hook of the RefInputSample from the protocol
 * @param sampleHook The hook of the sample to be set
 * @param amount The amount to be set - may be "" or "(various)"
 * @return false if nothing to save, otherwise void
 */
function updateSampleOnSelectedExperiments(risHook,sampleHook,amount){
	var postBodyParts=[];
	if("(various)"==sampleHook && ""==amount){
		alert("No sample or amount specified - nothing to do here.")
		return false;
	}
	var wells=$("plates").wells;
	var selectedWells=$("plates").selectedWells;
	postBodyParts.push("refInputSample=");
	postBodyParts.push(risHook);
	if("(various)"!=sampleHook){ //updating sample
		postBodyParts.push("&");
		postBodyParts.push(getPostBodyForExperiments(selectedWells,"sample",sampleHook));
	}
	if(""!=amount){ //updating amount
		postBodyParts.push("&");
		postBodyParts.push(getPostBodyForExperiments(selectedWells,"amount",amount));
	}
	var postBody=postBodyParts.join("");
	updateSelectedExperiments(postBody,updateSampleOnSelectedExperiments_onSuccess);
}

/**
 * Success handler for AJAX update of sample and/or amount. Calls the generic response handler to
 * parse the JSON in the response, then tidies up. 
 * 
 * "Copy from plate" and other advanced fill options do the AJAX update from a modal window, so 
 * the UI for the input sample in question won't reflect the new reality. In this case, we have to
 * ensure that the relevant samples are in the SELECT and then actually select them.
 * 
 * @param transport The response object, containing the JSON response from the server
 */
function updateSampleOnSelectedExperiments_onSuccess(transport){
	//generic handler
	updateLocalExperimentDataAfterAJAX(transport);
	//these two are necessary if we came from a modalWindow
	populateInputSampleSelects();
	updateFormValues();
	//and close the dialog now we're finished
	closeModalDialog();
}


/*************************
 * AJAX update functions *
 *************************/

/**
 * Makes and returns a POST body, to be used in AJAX submissions where the same value is set
 * for a given property across all experiments in the supplied collection.
 * 
 * @param wellsToUpdate The collection of wells to make POST parameters for
 * @param propertyName The name of the property to be changed on all experiments in wellsToUpdate
 * @param propertyValue The value of the property to be changed on all experiments in wellsToUpdate
 * @return a properly-formatted set of POST parameters, separated by ampersands,
 * 		   in the form wellExperimentHook:propertyName=propertyValue
 */ 
function getPostBodyForExperiments(wellsToUpdate,propertyName,propertyValue){
	var postBodyParts=[];
	wellsToUpdate.each(function(well){
		var parts=[];
		parts.push(well.experiment.hook);
		if(""!=propertyName){
			parts.push(":"+propertyName);
		}
		parts.push("="+propertyValue);
		postBodyParts.push(parts.join(""));
	});
	return postBodyParts.join("&");
}

/**
 * The general update function for plate/group experiments. Shows "Updating..." to the user,
 * and POSTs the supplied parameters to the server, setting up the request to use a specified
 * success handler or a default failure handler.
 * 
 * @param postBody A properly-formatted HTTP POST body
 * @param successHandler A function to handle a successful response
 */
function updateSelectedExperiments(postBody,successHandler){
	showUpdatingModalDialog();
	var req=new Ajax.Request(contextPath+"/update/AjaxPlateExperimentUpdate",{
		method:"post",
		postBody:postBody,
		onSuccess:function(transport){
			successHandler(transport);
		},
		onFailure:function(transport){
			update_onFailure(transport);
		}
	});
}

/**
 * Parse the JSON returned by an AJAX experiment update, and update the 
 * local copy of the experiments data with the changed information.
 *
 * @param transport The response object from the AJAX request
 */
function updateLocalExperimentDataAfterAJAX(transport){
	var exptsText="{"+transport.responseText+"}";
	try{
		var updatedExperiments=exptsText.evalJSON();
		Object.keys(updatedExperiments).each(function(key){
			var expt=updatedExperiments[key];
			var exptToUpdate=experiments[key];
			Object.keys(expt).each(function(prop){
				if(prop.startsWith("is")){
					exptToUpdate[prop]["name"]=expt[prop]["name"];
					exptToUpdate[prop]["hook"]=expt[prop]["hook"];
					exptToUpdate[prop]["amount"]=expt[prop]["amount"];
				} else if("blueprintName"==prop || "blueprintHook"== prop){
					exptToUpdate[prop]=expt[prop];
					exptToUpdate["pcrProductSize"]=expt["pcrProductSize"];
				}
			});
		});
	}catch(err){
		alert("Error on updating. The page will reload when you click OK.\n\nError in updateLocalExperimentDataAfterAJAX:\n"+err.message)
		document.location.reload();
	}
}

/**
 * Default handler for AJAX update failures. Opens a new window/tab containing the response,
 * which is likely to be the HTML for a PiMS error page - if this function is being called by 
 * an AJAX onFailure, the server sent a 4xy or 5xy HTTP response.
 * 
 * @TODO More user-friendly handling of errors.
 * @param transport The AJAX response object.
 */
function update_onFailure(transport){
	alert("Could not save");
	closeModalDialog();
	var w=window.open("","w");
	w.document.write(transport.responseText);
	document.location.reload();
}


/*************************************
 * "Advanced fill options" functions *
 *************************************/

/**
 * Opens a modal window containing the first stage of the Advanced Fill process.
 * 
 * @param risHook The hook of the RefInputSample from the protocol, for which the user
 * 				  will specify varying samples/amounts.
 */
function beginAdvancedFill(risHook){
	openModalWindow(contextPath+"/update/AdvancedFillOptionsServlet/?isInModalWindow=true&experimentGroup="+groupHook+"&refInputSample="+risHook,"Advanced fill options");
}


/***********************************
 * Add/remove experiment functions *
 ***********************************/

/**
 * Disables the "Delete selected experiments" button if all experiments
 * in the group are selected, otherwise enables it.
 */
function enableDisableDeletion(){
	var deleteButton=$("deletebutton");
	if(null==deleteButton){ return false; }
	var container=deleteButton.up(".platescontainer");
	if(container.usedWells.length == container.selectedWells.length){
		deleteButton.disabled="disabled"; //deleting all experiments is risky
	} else {
		deleteButton.disabled="";
	}
	return false;
}

/**
 * Onclick handler for the "Delete selected experiments" button.
 * Makes an AJAX POST to delete the experiments, setting up the
 * correct AJAX response handlers.
 * 
 * @param deleteButton The delete button
 */
function deleteSelectedExperiments(deleteButton){
	if(!confirm("Delete the experiments in the selected wells?")){
		return false;
	}
	
	var container = $(deleteButton).up(".platescontainer");
	var wellsToDelete = container.selectedWells;
	showUpdatingModalDialog();

	var hooks=[];
	wellsToDelete.each(function(well){
		hooks.push(well.experiment.hook);
	});
	var postBody="delete="+hooks.join(",");
	
	/***********
	 * @TODO DELETE_EXPTS
	 Make AJAX POST to /path/to/DeleteServlet, with a postBody of
	 delete=hook1,hook2,hook3
	
	var req=new Ajax.Request(contextPath+"/path/to/DeleteServlet",{
		method:"post",
		postBody:postBody,
		onSuccess:function(transport){
			deleteSelectedExperiments_onSuccess(transport);
		},
		onFailure:function(transport){
			deleteSelectedExperiments_onFailure(transport);
		}
	});
	************/
	
	/*************************************
	 * Below here is temporary dummy code
	 **/
	var transport={};
	var rt="{ deleted:[\"";
	rt+=hooks.join("\",\"");
	rt+="\"]}";
	transport.responseText=rt;
	setTimeout(function(){deleteSelectedExperiments_onSuccess(transport)},500);
	/**
	 * End of temporary dummy code
	 *************************************/
}

/**
 * Success handler for AJAX deletion of experiments from the group.
 * 
 * Nulls out the experiments from the local JSON, then (for plates)
 * hatches out or (for non-plates) removes the relevant UI elements,
 * and selects the first non-empty/remaining experiment.
 * 
 * Assumed JSON response format - NB trailing comma after last hook
 * will break parsing in IE:
 * 
 * { deleted:[
 * 		"hook.of.expt:1", "hook.of.expt:2", "hook.of.expt:3"
 * 	 ]
 * }
 * 
 * @param transport The AJAX response object
 */
function deleteSelectedExperiments_onSuccess(transport){

	var exptsText=transport.responseText;
	
	var container=$("deletebutton").up(".platescontainer");
	var isPlateExperiment=!container.hasClassName("nonplategroup");
	
	try{
		var responseObject=exptsText.evalJSON();
		var deletedHooks=responseObject.deleted;
		if(undefined==deletedHooks){
			throw "No 'deleted' value in response";
		}
		
		/* Assumption is that selection can't have changed (modalDialog prevents it)
		 * and that if any deletes had failed we would be in the onFailure handler
		 * - but do a basic check anyway, number deleted should equal number selected
		 */
		if(container.selectedWells.length != deletedHooks.length){
			throw "Number of experiments selected doesn't match number deleted"
		}
		
		container.selectedWells.each(function(well){
			var expClassNames=well.className.match(new RegExp("ex[0-9]+")); //look for "ex1234" in class names
			var expClass=expClassNames[0];
			
			//kill off the JS record of the experiment
			experiments[expClass]=null;

			//tidy up the UI
			if(isPlateExperiment){
				well.removeClassName(expClass); //disassociate from dead experiment
				well.addClassName("empty");     //hatch out
				well.innerHTML="&nbsp;"; //remove any icon from well
				well.observe("click",addExperimentIfWellEmpty);
			} else {
				well.remove();
			}
		});
		
		//wipe selection and replace with first surviving experiment
		container.selectedWells=[];
		var wells=container.wells;
		for(var i=0;i<wells.length;i++){
			if(!wells[i].hasClassName("empty")){
				container.selectedWells.push(wells[i]);
				break;
			}
		}
		showSelectedWells(container);
		
	}catch(err){
		alert("Error on updating. The page will reload when you click OK.\n\nError in deleteSelectedExperiments_onSuccess:\n"+err.message)
		document.location.reload();
	}
	closeModalDialog();
}

function deleteSelectedExperiments_onFailure(transport){
	closeModalDialog();
	if("409"==transport.status){
		//HTTP response code "Conflict"
		alert("Some experiments could not be deleted.\nThis page will reload when you click OK.");
	} else {
		alert("There was a problem on the server.\nThis page will reload when you click OK.")
	}
	document.location.reload();
}

function addExperimentIfWellEmpty(event){
	var well=$(event.target);
	if ("TD"!==well.tagName) {throw new Error("Well expected: "+well.tagName);}
	if(!well.hasClassName("empty")){
		return true;
	}
	var plate=well.up("table");
	var rowName=well.up("tr").down("th").innerHTML;
	var colName=plate.down("tr").select("th")[well.selectionCol+1].innerHTML;
	var previouslySelected = $(well).up(".platescontainer").selectedWells;

	previouslySelected.each(function(sw){
		sw.removeClassName("selected");
	});
	well.addClassName("selected");
	
	if(!confirm("Add an experiment to this well?\n\nPlate: "+well.up("table").holderName+", well: "+rowName+colName)){
		well.removeClassName("selected");
		previouslySelected.each(function(sw){
			sw.addClassName("selected");
		});
		return false;
	}

	showUpdatingModalDialog();

	well.addClassName("addingexperiment");

	//ajax request
	var postBody="holder="+well.up("table").holderHook;
	postBody+="&well="+rowName+colName;
	/***********
	 * @TODO ADD_EXPTS

	var req=new Ajax.Request(contextPath+"/path/to/CreateServlet",{
		method:"post",
		postBody:postBody,
		onSuccess:function(transport){
			addExperimentIfWellEmpty_onSuccess(transport);
		},
		onFailure:function(transport){
			addExperimentIfWellEmpty_onFailure(transport);
		}
	});
	************/
	
	/*************************************
	 * Below here is temporary dummy code
	 **/
	var transport={};
	var rt="{ }";
	transport.responseText=rt;
	setTimeout(function(){addExperimentIfWellEmpty_onSuccess(transport)},500);
	/**
	 * End of temporary dummy code
	 *************************************/
	
	//see /JSON/PlateExperimentEditExperiments.jsp for response format
	
}


function addExperimentIfWellEmpty_onSuccess(transport){
	var well=$(document.body).down(".addingexperiment");
	well.removeClassName("addingexperiment");
	//put new experiment object into JS array -
	//TODO experiments["exNNNN"] = deepClone() the one in the JSON
	
	//TODO well.experiment=experiments["exNNNN"];
	
	//TODO set innerHTML to link to experiment (Hint: inspect a well, to see what the <a href= is like)
	//TODO remove onclick event (not empty any more!)
	//TODO remove class "empty"
	//TODO select this well - temporary warning: BEWARE! It looks like it's selected, due to pre-AJAX stuff.
	closeModalDialog();
}

function addExperimentIfWellEmpty_onFailure(transport){
	closeModalDialog();
	alert("There was a problem on the server.\nThis page will reload when you click OK.")
	document.location.reload();
}


//http://stackoverflow.com/questions/728360/copying-an-object-in-javascript
function deepClone(obj){
	if(obj == null || typeof(obj) != 'object'){
		return obj;
	}
	var temp = new obj.constructor(); 
	for(var key in obj){
		temp[key] = deepClone(obj[key]);
	}
	return temp;
}


/*******************************
 * "Copy from plate" functions *
 *******************************/

function copyFromPlateAddWellTitles(){
	var inputPlates=$("inputplates");
	var firstExpt=inputPlates.wells[0].experiment;
	var outputKey="";
	inputPlates.wells.each(function(well){
		if(well.hasClassName("empty")){ return;} //from anonymous function, keep looping through wells
		if(""==outputKey){
			for (key in firstExpt){
				if(key.startsWith("os")){
					outputKey=key;
				}
			}
			if(""==outputKey){
				throw("Couldn't determine output sample for first experiment in input plate.")
			}
		}
		var st=well.experiment.status.replace(/_/g," ");
		well.title=st+"; contains "+well.experiment[outputKey]["name"];
	});
}

function showStatusesInInputPlates(){
	var inputPlates=$("inputplates");
	inputPlates.wells.each(function(well){
		if(well.hasClassName("empty")){ return;} //from anonymous function, keep looping through wells
		well.addClassName("status_"+well.experiment.status);
	});
}

function showRefInputSamplesInOutputPlates(){
	var outputPlates=$("outputplates");
	var ris="is"+refInputSample.dbId;
	outputPlates.wells.each(function(well){
		if(well.hasClassName("empty")){ return;} //from anonymous function, keep looping through wells
		var expt=well.experiment;
		well.existingSampleName=expt[ris]["name"];
		if(""!=expt[ris]["hook"]){
			well.addClassName("filled");
			well.title=expt.name+", contains "+expt[ris]["name"];
		} else {
			well.removeClassName("filled");
			well.title=expt.name+" (empty)";
		}
	});
}

function destinationPlatesOnmousedown(event){
	var inputPlates=$("inputplates");
	var outputPlates=$("outputplates");
	if(outputPlates.overTheEdge){
		return false;
	}
	var outputSampleIdentifier="";
	
	for(prop in inputPlates.firstExperiment){
		if(prop.startsWith("os")){ //assume only one
			outputSampleIdentifier=prop;
			break;
		}
	}
	var outputPlateTopLeftSelected=event.target;
	var changedWells=outputPlates.changedWells;
	var outputPlateTopmostSelectionRow=outputPlateTopLeftSelected.selectionRow;
	var outputPlateLeftmostSelectionCol=outputPlateTopLeftSelected.selectionCol;
	var overlapsExistingSamples=false;
	var selectionIncludesEmptyWell=false;
	outputPlates.selectedWells.each(function(well){
		if(well.hasClassName("filled")){
			overlapsExistingSamples=true;
		} else if(well.hasClassName("empty")){
			selectionIncludesEmptyWell=true;
		}
	});
	if(overlapsExistingSamples){
		outputPlates.suppressMouseEvents=true;
		var confirmation=confirm("This will overwrite the samples already recorded here. Continue?");
		outputPlates.suppressMouseEvents=false;
		if(!confirmation){
			outputPlates.selectedWells.each(function(well){
				well.removeClassName("destination");
			});
			return false;
		}
	}
	outputPlates.selectedWells.each(function(destinationWell){
		//Make no assumptions about the order being the same in both selectedWells arrays
		if(!destinationWell.hasClassName("empty")){
			if(0>changedWells.indexOf(destinationWell)){
				changedWells.push(destinationWell);
			}
			var srcRow=(destinationWell.selectionRow-outputPlateTopmostSelectionRow)+inputPlates.uppermostSelectionRow;
			var srcCol=(destinationWell.selectionCol-outputPlateLeftmostSelectionCol)+inputPlates.leftmostSelectionCol;
			var srcExperiment=inputPlates.down(".row"+srcRow+"_col"+srcCol).experiment;
			destinationWell.experiment["is"+refInputSample.dbId]["name"]=srcExperiment[outputSampleIdentifier]["name"];
			destinationWell.experiment["is"+refInputSample.dbId]["hook"]=srcExperiment[outputSampleIdentifier]["hook"];
			//destinationWell.title=destinationWell.experiment["is"+refInputSample.dbId]["name"];
			destinationWell.addClassName("adding");
			var wellTitle=destinationWell.experiment.name;
			if(destinationWell.hasClassName("filled")){
				wellTitle+=", replacing "+destinationWell.existingSampleName+" with "+srcExperiment[outputSampleIdentifier]["name"];
			} else {
				wellTitle+=", adding "+srcExperiment[outputSampleIdentifier]["name"];
			}
			destinationWell.title=wellTitle;
		}
		destinationWell.removeClassName("destination");
	});
	if(selectionIncludesEmptyWell){
		alert("Your selection included at least one empty well. Samples were not copied to or from empty wells.");
	}
}

function destinationPlatesOnmouseup(event){
	//do nothing
}

function destinationPlatesOnmouseover(event){
	var inputPlates=$("inputplates");
	var outputPlates=$("outputplates");
	var well=event.target || event.srcElement;
	if ('TD'!==well.tagName) {return;};
	var rowOffset=well.selectionRow-inputPlates.uppermostSelectionRow;
	var colOffset=well.selectionCol-inputPlates.leftmostSelectionCol;
	var selectedInputs=inputPlates.selectedWells;
	outputPlates.overTheEdge=false;
	outputPlates.selectedWells=[];
	selectedInputs.each(function(inputWell){
		var destinationRow=inputWell.selectionRow+rowOffset;
		var destinationCol=inputWell.selectionCol+colOffset;
		var destinationWell=outputPlates.down(".row"+destinationRow+"_col"+destinationCol);
		if(undefined==destinationWell){
			outputPlates.overTheEdge=true;
		} else {
			outputPlates.selectedWells.push(destinationWell);
		}
		//also fail if this already contains a sample?
	});
	if(outputPlates.overTheEdge){		
		//assign "error" image
		outputPlates.selectedWells.each(function(well){
			well.addClassName("error");
		});
	} else {
		outputPlates.selectedWells.each(function(well){
			well.addClassName("destination");
		});
	}
	outputPlates.wells.each(function(well){
		//clear any "selection" classes, error or OK
	});
	outputPlates.selectedWells.each(function(outputWell){
		//show the right icon
	});
}

function destinationPlatesOnmouseout(event){
	var outputPlates=$("outputplates");
	if(outputPlates.suppressMouseEvents){
		return false;
	}
	outputPlates.wells.each(function(well){
		well.removeClassName("error");
		well.removeClassName("destination");
	});
	
}

function cancelCopyFromPlate(){
	if(confirm("Are you sure you want to abandon your work in this window?\nClick OK to abandon, or Cancel to keep working.")){
		window.parent.closeModalWindow();
	}
	return false;
}

function doCopyFromPlate(elem){
	var frm=$(elem).up("form");
	var unitField=frm.down(".displayunit");
	var amountField=frm.down(".amount");
	if(""==amountField.value){
		alert("An amount is required.");
		return false;
	} else if(!isNumeric(amountField.value) || amountField.value<=0){
		alert("Amount must be a number greater than zero");
		return false;
	}
	var amountToSubmit=amountField.value+""+unitField.value;
	var outputPlates=$("outputplates");

	var inputSampleLabel="is"+refInputSample.dbId;
	var postBodyParts=[];
	var changedWells=outputPlates.changedWells;
	postBodyParts.push("refInputSample=");
	postBodyParts.push(refInputSample.hook);
	changedWells.each(function(well){
			if(!well.experiment) { return; } //from anonymous function, keep looping through wells
		
			postBodyParts.push("&");
			postBodyParts.push(well.experiment.hook);
			postBodyParts.push(":sample=");
			postBodyParts.push(well.experiment[inputSampleLabel]["hook"]);

			postBodyParts.push("&");
			postBodyParts.push(well.experiment.hook);
			postBodyParts.push(":amount=");
			postBodyParts.push(amountToSubmit);
	});
	var postBody=postBodyParts.join("");
	window.parent.document.getElementById("modalWindow_window").style.display="none";
	window.parent.updateSelectedExperiments(postBody,window.parent.updateSampleOnSelectedExperiments_onSuccess);	
}


/*******************************
 * "Edit plate name" functions *
 *******************************/
function editPlateName(icon){
	var currentName=$(icon).up("th").down("span.platename").innerHTML;
	var newName=prompt("New name for this plate:",currentName);
	if(null==newName){ //user cancelled
		return false;
	} else if(""==newName.strip()){
		alert("Plate name cannot be blank");
		return false;
	} else if(currentName.strip()==newName.strip()){
		alert("Name has not changed");
		return false;
	}
	//OK, we're going to submit this...
	var frm=icon.up("th").down("form");
	var field=frm.down("input.platename");
	field.value=newName;
	showUpdatingModalDialog();
	frm.submit();
}

/*******************************
 * "Merge by Score functions   *
 *******************************/
function applyWellScores(){
	var firstLi=$("mergeinputgroups").firstDescendant();
	var wells = $(firstLi).select(".well");
	wells.each(function(well){
		if(!well.hasClassName("empty")){
			applyScoreToWell(well);
		}
	});
}

function applyScoreToWell(elem){
	var radio=$(elem).firstDescendant();
	if (null != radio) {
		radio.checked=true;
		var radioName=$w(radio.name)[0];
		var radioSet = $("mergeinputgroups").select('.'+radioName);
		radioSet.each(function(rad) {
			if (rad.checked) {
				rad.up().removeClassName("mergebyscore_notselected");
				setOutputWell(rad.up());
			} else {
				rad.up().addClassName("mergebyscore_notselected");
			}
		});
	}
}

/**
 * set output well
 * elem is the <td> element
 */
function setOutputWell(elem)  {
	var rowcolClass;
	var scoreClass;
	var exClass;
	var inputPlates=$("mergeinputgroups");
	var outputSampleIdentifier="";
	
	for(prop in inputPlates.firstExperiment){
		if(prop.startsWith("os")){ //assume only one
			outputSampleIdentifier=prop;
			break;
		}
	}
	
	$(elem).classNames().each(function(className) {
		if(className.startsWith("row")&&className.include("col")){
			rowcolClass=className;
		}
		if(className.startsWith("SCORE")){
			scoreClass=className;
		}
		if(className.startsWith("ex")){
			exClass=className;
		}
	});
	
	var srcExperiment=$(elem).experiment;
	var outputwell=$("outputplates").down(rowcolClass);
	var changedWells=$("outputplates").changedWells;
	
	$("outputplates").wells.each(function(well) {
		if (well.hasClassName(rowcolClass)) {
			outputwell= well;
		}
	});
	
	outputwell.classNames().each(function(className) {
		if(className.startsWith("SCORE") || className.startsWith("inputexperiment_")){
			outputwell.removeClassName(className);
		}
	});	
			
	if(!outputwell.hasClassName("empty")){
		if(0>changedWells.indexOf(outputwell)){
			changedWells.push(outputwell);
		}	
	
		outputwell.experiment["is"+refInputSample.dbId]["name"]=srcExperiment[outputSampleIdentifier]["name"];
		outputwell.experiment["is"+refInputSample.dbId]["hook"]=srcExperiment[outputSampleIdentifier]["hook"];
				
		var wellTitle=outputwell.experiment.name;
		if(outputwell.hasClassName("filled")){
			wellTitle+=", replacing "+outputwell.existingSampleName+" with "+srcExperiment[outputSampleIdentifier]["name"];
		} else {
			wellTitle+=", adding "+srcExperiment[outputSampleIdentifier]["name"];
		}
		outputwell.title=wellTitle;
	}
	
	outputwell.addClassName(scoreClass);
	outputwell.addClassName("inputexperiment_"+exClass);
}


function setWellScores(){
	var wells=$("mergeinputgroups").wells;
	var scorePdefId=panel.down(".param").id; //ASSUME only one
	var scorePdefName=panel.pdefName; 
	var scoresList=panel.scoresList;

	wells.each(function(well){
		if(!well.hasClassName("empty")){
			var html="&nbsp;";
			var scoreNum=scoresList.indexOf(well.experiment[scorePdefId]);
			well.addClassName(scorePdefName+scoreNum);
			well.innerHTML=html;
		}
	});
}

function showPlateScores(elem) {
	var wells=$(elem).wells;
	wells.each(function(well) {
		if (!well.hasClassName("empty")) {
			well.addClassName("SCORE"+well.experiment.score);
			well.addClassName("mergebyscore_notselected");
			well.innerHTML='<input type="radio" class="radioset'+well.selectionRow+'_'+well.selectionCol+'" name="radioset'+well.selectionRow+'_'+well.selectionCol+'" style="display:none" />';
		}
	});
}

// TODO test for a touch event
function selectWellToMerge(event) {
	var event = e || window.event;  
	var elem=event.target || event.srcElement;
	applyScoreToWell(elem);
	if (e.stopPropagation) {
	          e.stopPropagation();
	} else {
	          e.cancelBubble = true;
	}
}

/**
 * action up icon clicked 
 */
function inputgroupMoveUp(elem) {
	var group=$(elem).up('[id=mergeinputgroup]');
	var previous=$(group).previous();
	var parent=$(group).up('[id=mergeinputgroups]');
	group.remove();
	parent.insertBefore(group, previous);
	resetGroupList(parent);
}

/**
 * action down icon clicked 
 */
function inputgroupMoveDown(elem) {
	var group=$(elem).up('[id=mergeinputgroup]');
	var next=$(group).next();
	var parent=$(group).up('[id=mergeinputgroups]');
	next.remove();
	parent.insertBefore(next, group);
	resetGroupList(parent);
}

/**
 * Set the up/down arrows for input groups 
 * elem is $("mergeinputgroups")
 */
function resetGroupList(elem) {
	var children = $(elem).childElements();
	children.each(function(child) {
		var icons = $(child).down('[id=mergeinputplate_icons]');
		var iconchildren = $(icons).childElements();
		iconchildren.each(function(iconchild) {
			iconchild.removeClassName("mergeinputgroup_noicon");
		});
		var seperator = $(child).down('[id=mergeinputplate_separator]');
		seperator.removeClassName("mergeinputgroup_noicon");
	});
	
	var firstborn = children[0]; 
	var upicon = firstborn.down('[id=mergeinputgroup_icon_up]');
	upicon.addClassName("mergeinputgroup_noicon");
	
	var lastborn = children[children.length-1]; 
	var downicon = lastborn.down('[id=mergeinputgroup_icon_down]');
	downicon.addClassName("mergeinputgroup_noicon");
	
	var seperator = $(lastborn).down('[id=mergeinputplate_separator]');
	seperator.addClassName("mergeinputgroup_noicon");
}

