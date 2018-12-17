/**
 * Work package: Crystal shipping
 * Copyright Edward Daniel, 2013
 */
var Shipment={

	managementURL:window.location.href,
	finalURL:contextPath+"/CreateShipment", //where we submit the final POST
	viewURL:contextPath+"/ViewShipment?shipment=",
	
	init:function(){
		Shipment.watchBarcodeKeyPresses();
		$("submit").onclick=function(){ Shipment.submit() };
	},

	handleTextBoxKeypress:function(event){
		elem=event.element();
		if(elem.keyTimer){ clearTimeout(elem.keyTimer); }
		
		var updateFunction=null;
		if(elem.hasClassName("barcode")){
			updateFunction=Shipment.doAddByBarcode;
		} else if(elem.hasClassName("predictedinfo")){
			updateFunction=Shipment.updatePreShipExperiment;
		} else if(elem.hasClassName("heavyatom")){
			updateFunction=Shipment.updatePreShipExperiment;
		} else if(elem.hasClassName("exptype")){
			updateFunction=Shipment.updatePreShipExperiment;
		} else if(elem.hasClassName("comments")){
			updateFunction=Shipment.updatePreShipExperiment;
		} else if(elem.hasClassName("spacegroup")){
			updateFunction=Shipment.updatePreShipExperiment;
		} else if(elem.hasClassName("target")){
			updateFunction=Shipment.updateTarget;
		} else if(elem.hasClassName("samplename")){
			updateFunction=Shipment.updateSampleName;
		}
		if(!updateFunction){ return false; }

		if(event.keyCode==13){
			updateFunction(elem);
			//and go down a row, if possible
		} else if(event.keyCode==Event.KEY_TAB){
			updateFunction(elem);
			//and take default behaviour, go to next active field
		} else {
			//handle tab
			//handle arrows
			//otherwise:
			elem.keyTimer=setTimeout(function(){ updateFunction(elem)},750);
		}
	},
	
	watchBarcodeKeyPresses:function(){
		$("actions").down("input").onchange=null;
		$$("input[type=text],select").each(function(field){
			field.observe("keyup",Shipment.handleTextBoxKeypress);
			field.observe("change",Shipment.handleTextBoxKeypress);
		});
		
	},

	/*********************************************
	 * Cosmetic stuff
	 *********************************************/
	markUpdating:function(elem){
		$(elem).up("td").addClassName("updating");
	},
	unmarkUpdating:function(elem){
		$(elem).up("td").removeClassName("updating");
	},
	markInvalid:function(elem){
		$(elem).up("td").addClassName("invalid");
	},
	unmarkInvalid:function(elem){
		$(elem).up("td").removeClassName("invalid");
	},
	
	
	/*************************************************
	 * Handle predicted info updates (unit cell, etc.)
	 *************************************************/
	updatePreShipExperiment:function(elem){
		elem=$(elem);
		var valid=false;
		if(elem.hasClassName("comments")){
			valid=Shipment.validateComment(elem);
		} else if(elem.hasClassName("predictedinfo")){
			valid=Shipment.validateUnitCellDimension(elem);
		} else if(elem.hasClassName("spacegroup")){
			valid=Shipment.validateSpaceGroup(elem);
		} else if(elem.hasClassName("exptype")){
			valid=true; //It's a select
		} else if(elem.hasClassName("heavyatom")){
			valid=Shipment.validateHeavyAtom(elem);
		} 
		if(!valid){ return false; }
		var pinHook=elem.up("tr").id;
		Shipment.markUpdating(elem);
		new Ajax.Request(Shipment.managementURL,{
			method:"post",
			onSuccess:Shipment.updatePreShipExperiment_onSuccess,
			onFailure:Shipment.updatePreShipExperiment_onFailure,
			parameters:{
			    "action":"updatePreShipExperiment",
			    "pin":pinHook,
			    "fieldName":elem.name,
			    "fieldValue":elem.value
			}
		});
		/* Response format:
		 {
		 	pin:hook.of.pin:1234,
		 	fieldName:a,
		 	fieldValue:"123"
		 }
		 */
	},
	updatePreShipExperiment_onSuccess:function(transport){
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Shipment.ajaxError(transport.responseText);
		}
		if(transport.responseJSON.error){
			return Shipment.ajaxError(transport.responseJSON.error);
		}
		var updatedPin=transport.responseJSON;
		var pinHook=updatedPin.updated;
		var pinRow=$(pinHook);
		if(!pinRow){ return false; } //maybe pin was removed from shipment?
		var field=pinRow.down("*[name="+updatedPin.fieldName+"]");
		if(field!=$(document.activeElement)){
			field.value=updatedPin.fieldValue;
		}
		Shipment.unmarkUpdating(field);
		
	},
	updatePreShipExperiment_onFailure:function(transport){
		closeModalDialog();
		return Shipment.ajaxError(transport.responseText);
	},
	validateComment:function(elem){
		var c=elem.value;
		var reg = new RegExp("^[^']*$");
		if(!reg.test(c)){
			Shipment.markInvalid(elem);
			alert("Cannot use single quote (apostrophe) in a comment - Diamond will not accept it");
			return false;
		}
		Shipment.unmarkInvalid(elem);
		return true;
	},
	validateUnitCellDimension:function(elem){
		var dim=elem.value;
		if(""!=dim && isNaN(dim)){
			Shipment.markInvalid(elem);
			alert("Dimension must be numeric");
			return false;
		}
		Shipment.unmarkInvalid(elem);
		return true;

	},
	validateSpaceGroup:function(elem){
		var sg=elem.value;
		var reg = new RegExp("^[A-Z][0-9]+$");
		if(!reg.test(sg)){
			Shipment.markInvalid(elem);
			alert("Space group not recognised");
			return false;
		}
		Shipment.unmarkInvalid(elem);
		return true;
	},
	validateHeavyAtom:function(elem){
		var ha=elem.value;
		if(typeof(heavyAtoms) !== "undefined"){
			if(-1==heavyAtoms.indexOf(ha)){
				alert("Heavy atom is not valid");
				return false;
			}
		}
		return true;
	},
	
	/*********************************************
	 * Handle target  updates
	 *********************************************/
	updateTarget:function(elem){
		elem=$(elem); //for IE
		if(!elem.hasClassName("target")){ return false; }
		var newName=elem.value;
		if(!Shipment.validateTargetName(elem)){
			return false;
		}
		var pinHook=elem.up("tr").id;
		Shipment.markUpdating(elem);
		new Ajax.Request(Shipment.managementURL,{
			method:"post",
			onSuccess:Shipment.updateTarget_onSuccess,
			onFailure:Shipment.updateTarget_onFailure,
			parameters:{
				"action":"updateTargetName",
			    "pin":pinHook,
			    "targetName":elem.value
			}
		});
	
	},
	updateTarget_onSuccess:function(transport){
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Shipment.ajaxError(transport.responseText);
		}
		if(transport.responseJSON.error){
			return Shipment.ajaxError(transport.responseJSON.error);
		}
		var updatedPin=transport.responseJSON;
		var pinHook=updatedPin.hook;
		var pinRow=$(pinHook);
		if(!pinRow){ return false; } //maybe pin was removed from shipment?
		var field=pinRow.down(".target");
		if(field!==$(document.activeElement)){
			field.value=updatedPin.targetName;
		}
		Shipment.unmarkUpdating(field);
	},
	updateTarget_onFailure:function(transport){
		closeModalDialog();
		return Shipment.ajaxError(transport.responseText);
	},
	validateTargetName:function(elem){
		var val=elem.value.strip();
		if(""==val){
			Shipment.markInvalid(elem);
			alert("Protein acronym is required");
			return false;
		}
		var reg = new RegExp("\'");
		if(reg.test(val)){
			Shipment.markInvalid(elem);
			alert("Target acronym cannot contain apostrophe");
			return false;
		}
		Shipment.unmarkInvalid(elem);
		return true;
	},
	
	/*********************************************
	 * Handle sample name updates
	 *********************************************/
	updateSampleName:function(elem){
		elem=$(elem); //for IE
		if(!elem.hasClassName("samplename")){ return false; }
		var pinRow=$(elem).up("tr");
		if(!pinRow || !pinRow.id){ 
			alert("Cannot update sample name - parent does not appear to be a pin");
			return false;
		}
		var pinHook=pinRow.id;
		if(!Shipment.validateSampleName(elem)){ return false; }
		Shipment.markUpdating(elem);
		new Ajax.Request(Shipment.managementURL,{
			method:"post",
			onSuccess:Shipment.updateSampleName_onSuccess,
			onFailure:Shipment.updateSampleName_onFailure,
			parameters:{
			    action:"updateSampleName",
			    pin:pinHook,
			    sampleName:elem.value
			}
		});
	},
	updateSampleName_onSuccess:function(transport){
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Shipment.ajaxError(transport.responseText);
		}
		if(transport.responseJSON.error){
			return Shipment.ajaxError(transport.responseJSON.error);
		}
		var newItem=transport.responseJSON;
		if("Pin"==newItem.added){
			rowId=newItem.hook;
			var field=$(rowId).down(".samplename");
			if(field!==$(document.activeElement)){
				field.value=newItem.sampleName;
			}
			Shipment.unmarkUpdating(field);
			//should be the same as the existing value, but change it anyway
		}
	},
	updateSampleName_onFailure:function(transport){
		closeModalDialog();
		return Shipment.ajaxError(transport.responseText);
	},
	validateSampleName:function(elem){
		//Required. For Diamond, can only contain letters, numbers, underscore and dash
		var sampleName=elem.value;
		if(!sampleName){
			Shipment.markInvalid(elem);
			alert("Sample name is required");
			return false;
		}
		var reg = new RegExp("^[-_A-Za-z0-9]*$");
		if(!reg.test(sampleName)){
			Shipment.markInvalid(elem);
			alert("Sample name can only contain letters, numbers, underscore and dash");
			return false;
		}
		Shipment.unmarkInvalid(elem);
		return true;
	},
	
	/*********************************************
	 * Add by barcode - handle pin/puck/dewar scan
	 *********************************************/
	
	addByBarcode:function(){
		var elem=event.element();
		Shipment.doAddByBarcode(elem);
	},
	doAddByBarcode:function(elem){
		$elem=$(elem);
		var barcode=elem.value;
		if(""==barcode){ return false; }
		var dewarNames=$$("span.dewarname");
		dewarNames.each(function(dn){
			if(dn.innerHTML==barcode){
				dn.scrollTo();
				alert("Dewar '"+barcode+"' is already in this shipment");
				return false;
			}
		});
		var puckNames=$$("span.puckname");
		puckNames.each(function(pn){
			if(pn.innerHTML==barcode){
				pn.scrollTo();
				alert("Puck '"+barcode+"' is already in this shipment");
				return false;
			}
		});
		
		elem.value="";
		var puckPosition=Shipment.getPuckPosition(elem); //false if not a pin
		//AJAX
		showUpdatingModalDialog();
		
		var parent="";
		if(elem.up(".shipment_puck")){
			parent=elem.up(".shipment_puck").up("form").id;
		} else if(elem.up(".dewar")){
			parent=elem.up(".dewar").id;
		}
		
		new Ajax.Request(Shipment.managementURL,{
			method:"post",
			onSuccess:Shipment.addByBarcode_onSuccess,
			onFailure:Shipment.addByBarcode_onFailure,
			parameters:{
			    action:"add",
			    barcode:barcode,
			    parent:parent,
			    position:puckPosition
			}
		});
	},
	addByBarcode_onSuccess:function(transport){
		closeModalDialog();
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Shipment.ajaxError(transport.responseText);
		}
		if(transport.responseJSON.error){
			return Shipment.ajaxError(transport.responseJSON.error);
		}
		var newItem=transport.responseJSON;
		if("Pin"==newItem.added){
			Shipment.addPin(newItem);
		} else if("Puck"==newItem.added){
			Shipment.addPuck(newItem);
		} else if("Dewar"==newItem.added){
			Shipment.addDewar(newItem);
		} else {
			return Shipment.ajaxError("Type not recognised (should be Pin, Puck or Dewar)");
		}
	},
	addByBarcode_onFailure:function(transport){
		closeModalDialog();
		return Shipment.ajaxError(transport.responseText);
	},

	addDewar:function(dewar){
		if($(dewar.hook)){
			$(dewar.hook).scrollTo();
			return false;
		}
		var newDewar=$("actions").cloneNode(true);
		var dewarBox=$("dewars").appendChild(newDewar);
		dewarBox.id=dewar.hook;
		dewarBox.addClassName("dewar");
		var dewarBox=$(dewar.hook);
		var boxHeader="Dewar: <span class=\"dewarname\">"+dewar.barcode+"</span>";
		boxHeader+=' <span class="linkwithicon "><a href="#" onclick="Shipment.removeDewar(this);return false"><img alt="" src="/xtalpims/skins/default/images/icons/actions/remove.gif" class="icon" title="Remove this puck from the shipment"></a><a href="#" onclick="Shipment.removeDewar(this);return false"><span class="linktext">Remove from shipment</span></a></span>';
		dewarBox.down("h3").update(boxHeader);
		var newContent='<div class="pucks"></div>';
		newContent+='<form action="#" class="grid" onsubmit="return false">';
		newContent+='<div class="formblock "><div class="column1">';
		newContent+='<div class="formitem "><div class="fieldname">';
		newContent+='<label for="adddewar">Add puck by barcode</label></div><div class="formfield">';
		newContent+='<input type="text" value="" name="addpuck" class="barcode">';
		newContent+='</div></div></div><div class="shim">&nbsp;</div></div>';
		newContent+='</form>';
		dewarBox.down(".boxcontent").innerHTML=newContent;
		dewarBox.down("input[name=addpuck]").focus();
		Shipment.watchBarcodeKeyPresses();
//		for(i=0;i<dewar.contents.length;i++){
//			var puck=dewar.contents[i];
//			Shipment.addPuck(puck);
//		}
		dewar.contents.each(function(puck){
			setTimeout(function(){ Shipment.addPuck(puck); },10);
		});
	},

	removePuck:function(elem){
		puck=$(elem);
		if(!puck.hasClassName("shipment_puck")){
			puck=puck.up(".shipment_puck");
			if(!puck){ return false; }
			puck=puck.up("form");
			if(!puck){ return false; }
		}
		showUpdatingModalDialog();
		new Ajax.Request(Shipment.managementURL,{
			method:"post",
			onSuccess:Shipment.removePuck_onSuccess,
			onFailure:Shipment.removePuck_onFailure,
			parameters:{
			    action:"removePuck",
			    puckToRemove:puck.id
			}
		});
	},
	removePuck_onSuccess:function(transport){
		closeModalDialog();
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON || !transport.responseJSON.removedPuck){
			Shipment.removePuck_onFailure(transport);
			return false;
		}
		var puckForm=$(transport.responseJSON.removedPuck);
		if(!puckForm || !puckForm.down("table.shipment_puck") ){
			return Shipment.ajaxError("Removed puck, but response is not a puck");
		}
		puckForm.remove();
	},
	removePuck_onFailure:function(transport){
		closeModalDialog();
		return Shipment.ajaxError(transport.responseText);
	},
	
	addPuck:function(puck){
		if($(puck.hook)){
			openCollapsibleBox($(puck.hook));
			$(puck.hook).scrollTo();
			return false;
		}
		if(!puck.parent || !$(puck.parent)){
			return Shipment.ajaxError("Parent dewar is not specified");
		}
		var dewar=$(puck.parent);
		var puckTable='<form action="#" onsubmit="return false" id="'+puck.hook+'">';
		puckTable+='<table class="shipment_puck">';
		puckTable+='<tr class="header">';
		puckTable+='<th colspan="15" class="puckhead">Puck: <span class="puckname">'+puck.barcode+'</span> <span class="linkwithicon "><a onclick="Shipment.removePuck(this);return false" href="#"><img title="Remove this puck from the shipment" class="icon" src="/xtalpims/skins/default/images/icons/actions/remove.gif" alt=""></a><a onclick="Shipment.removePuck(this);return false" href="#"><span class="linktext">Remove puck from shipment</span></a></span></th>';
		puckTable+='</tr>';
		puckTable+='<tr class="">';
		puckTable+='<th class="slot"><a onclick="Shipment.removePins(this);return false" href="#"><img title="Remove all pins from this puck (and from the shipment)" class="icon" src="/xtalpims/skins/default/images/icons/actions/remove.gif" alt=""></a></th>';
		puckTable+='<th class="slot">#</th>';
		puckTable+='<th class="label">Pin Barcode<span class="required">*</span></th>';
		puckTable+='<th class="label" title="This must correspond to a protein acronym on the Diamond safety sheet for this shipment">Protein acronym<span class="required">*</span></th>';
		puckTable+='<th class="label" title="Can only contain letters, numbers, dash and underscore">Sample name<span class="required">*</span></th>';
		puckTable+='<th class="unitcell" title="Heavy atom (anomalous scatterer)">Heavy atom</th>';
		puckTable+='<th class="unitcell" title="Predicted space group">Space group</th>';
		puckTable+='<th class="unitcell" title="Experiment type at the beamline">Expt type</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">a</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">b</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">c</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">&#945;</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">&#946;</th>';
		puckTable+='<th class="unitcell" title="Predicted unit cell dimension">&#947;</th>';
		puckTable+='<th title="The URL of the crystal\'s xtalPiMS treatment page will be added automatically">Comments</th>';
		puckTable+='</tr>';
		var oddEven="odd";
		for(i=0;i<puck.positions;i++){
			puckTable+='<tr class="body '+oddEven+'"><th class="slot">&nbsp;</th><th class="slot">'+(i+1)+'</th><td colspan="11">&nbsp;</td></tr>';
			oddEven= (oddEven=="even") ? "odd" : "even";
		}
		puckTable+='</table>';
		puckTable+='</form>';
		dewar.down(".pucks").insert({ bottom:puckTable });
		var puckTable=$(puck.hook);
		var rows=puckTable.select("tr.body");
		for(i=0;i<rows.length;i++){
			Shipment.clearPuckPosition(rows[i]);
		}
		for(i=0;i<puck.contents.length;i++){
			var pin=puck.contents[i];
			var pinRow=rows[pin.puckPosition-1];
			Shipment.fillPuckPosition(pinRow, pin);
		}
	},
	
	addPin:function(pin){
		//If pin is already in shipment, remove it from old location and alert.
		if($(pin.hook)){
			alert("Pin "+pin.barcode+" is already in the shipment; moving it to the new position");
			var currentPuckPosition=Shipment.getPuckPosition($(pin.hook));
			Shipment.clearPuckPosition($(pin.hook), currentPuckPosition);
		}		
		
		var puck=$(pin.parent);
		var puckPosition=pin.puckPosition;
		if(!puck || !puck.hasClassName("puck")){
//			return Shipment.ajaxError("Pin's parent is not a puck, or puck is not in shipment");
		}
		var bodyRows=puck.select("tr.body");
		if(puckPosition-1 > bodyRows.length){
			return Shipment.ajaxError("Specified puck position does not exist in puck");
		}
		var pinRow=bodyRows[puckPosition-1];
		var verifyPuckPosition=Shipment.getPuckPosition(pinRow);
		if(puckPosition!=verifyPuckPosition){
			alert(verifyPuckPosition+" "+puckPosition)
			return Shipment.ajaxError("Puck position mismatch");
		}
		Shipment.fillPuckPosition(pinRow, pin);
		//TODO If pin barcode exists elsewhere in shipment, clear that row!
	},

	/*********************************
	 * Remove a pin from the shipment
	 * (actually from its parent puck)
	 *********************************/
	
	removePins:function(elem){
		var pinsToRemove=[];
		var wrapper=$(elem).up("tr.body");
		if(wrapper){
			pinsToRemove.push(wrapper.id);
		} else {
			wrapper=$(elem).up("table.shipment_puck");
			var rows=wrapper.select("tr.body");
			for(i=0;i<rows.length;i++){
				var row=rows[i];
				if(null!=row.id){
					pinsToRemove.push(row.id);
				}
			}
		}
		var puckHook=wrapper.id;
		showUpdatingModalDialog();
		new Ajax.Request(Shipment.managementURL, {
			method:"post",
			onSuccess:function(transport){ Shipment.removePins_onSuccess(transport); },
			onFailure:function(transport){ Shipment.removePins_onFailure(transport); },
			parameters:{
				action:"removePins",
				pins:pinsToRemove.join()
			}
		});
	},
	removePins_onSuccess:function(transport){
		closeModalDialog();
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON || !transport.responseJSON.removedPins){
			return Shipment.ajaxError("Response failed to specify removed pin");
		}
		var numPins=transport.responseJSON.removedPins.length;
		for(i=0;i<numPins;i++){
			var removed=transport.responseJSON.removedPins[i];
			var pinHook=removed.removedPin;
			var puckHook=removed.removedFromPuck;
			var puckPosition=removed.removedFromPosition;
			if(!pinHook || !puckHook || !puckPosition){
				return Shipment.ajaxError("Response didn't specify all of: Pin hook, puck hook, puck position");
			}
			var pinRow=$(pinHook);
			var puck=$(puckHook);
			if(!pinRow || !puck){
				return Shipment.ajaxError("Response specified an invalid pin or puck");
			}
			Shipment.clearPuckPosition(pinRow,puckPosition);
		}
	},
	removePins_onFailure:function(transport){
		closeModalDialog();
		alert(transport.responseText);
	},

	removeDewar:function(elem){
		//No need for server side action, just remove it from the client
		elem=$(elem);
		var dewar=elem.up(".dewar");
		if(dewar){
			dewar.remove();
		}
	},
	
	getPuckPosition:function(elem){
		elem=$(elem);
		var slotTHs=elem.select("th.slot");
		if(undefined==slotTHs || 0==slotTHs.length){
			elem=elem.up("tr");
			if(!elem){ return false; }
			slotTHs=elem.select("th.slot");
		}
		if(0==slotTHs.length){ return false; }
		return slotTHs[1].innerHTML;
	},
	
	ajaxError:function(msg){
		alert("Sorry, there was an error:\n\n"+msg);
		if(msg.length>200){
			var err=window.open();
			err.document.write(msg);
		}
	},

	clearPuckPosition:function(tableRow){
		tableRow=$(tableRow);
		var positionNumber=Shipment.getPuckPosition(tableRow);
		var html='<th class="slot">&nbsp;</th>';
		html+='<th class="slot rowposition">'+positionNumber+'</th>';
		html+='<td class="label"><input type="text" name="pinbarcode" class="barcode" /></td>';
		html+='<td class="label"><input disabled="disabled" type="text" name="targetHook:name" value=""/></td>';
		html+='<td class="label"><input disabled="disabled" type="text" name="sampleHook:name"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="spacegroup"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="exptype"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="heavyatom"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="a"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="b"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="c"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="alpha"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="beta"  value=""/></td>';
		html+='<td class="unitcell"><input disabled="disabled" type="text" name="gamma"  value=""/></td>';
		html+='<td class="comments"><input disabled="disabled" type="text" name="comments"  value="" value=""/></td>';
		tableRow.update(html);
		tableRow.id=null;
		tableRow.removeClassName("occupied");
		window.setTimeout(Shipment.watchBarcodeKeyPresses,50);
	},
	
	fillPuckPosition:function(tableRow, pin){
		tableRow=$(tableRow);
		var positionNumber=Shipment.getPuckPosition(tableRow);
		var html='<th class="slot"><a href="#" onclick="Shipment.removePins(this);return false"><img alt="" src="/xtalpims/skins/default/images/icons/actions/remove.gif" class="icon" title="Remove this pin from the shipment"></a></th>';
		html+='<th class="slot">'+positionNumber+'</th>';
		html+='<td class="label">&nbsp;<span class="pinname">'+pin.barcode+'</span><input type="hidden" name="barcode" value="'+pin.barcode+'"/></td>';
		html+='<td class="label"><input type="text" class="target" name="'+pin.targetHook+':name" value="'+pin.targetName+'"/></td>';
		html+='<td class="label"><input type="text" class="samplename" name="'+pin.sampleHook+':name" value="'+pin.sampleName+'" /></td>';
		if(typeof heavyAtoms ==='undefined'){
			//This should be an array in the parent document. Give up, show a text box.
			html+='<td class="unitcell"><input type="text" class="heavyatom" name="heavyatom" value="'+pin.heavyAtom+'"/></td>';
		} else {
			//Array of heavy atoms.
			html+='<td class="unitcell"><select name="heavyatom" class="heavyatom">';
			heavyAtoms.each(function(ha){
				if(ha==pin.heavyAtom){
					html+='<option value="'+ha+'" selected="selected">'+ha+'</option>';
				} else {
					html+='<option value="'+ha+'">'+ha+'</option>';
				}
			});
			html+='</select></td>';
		}
		if(typeof spaceGroups ==='undefined'){
			//This should be an array in the parent document. Give up, show a text box.
			html+='<td class="unitcell"><input type="text" class="spacegroup" name="spacegroup" value="'+pin.spaceGroup+'"/></td>';
		} else {
			//Array of permissible space groups.
			html+='<td class="unitcell"><select name="spacegroup" class="spacegroup">';
			spaceGroups.each(function(sg){
				if(sg==pin.spaceGroup){
					html+='<option value="'+sg+'" selected="selected">'+sg+'</option>';
				} else {
					html+='<option value="'+sg+'">'+sg+'</option>';
				}
			});
			html+='</select></td>';
		}
		
		var  exptypes=["OSC","SAD","MAD"];
		html+='<td class="unitcell"><select name="exptype" class="exptype">';
		
		exptypes.each(function(et){
			if(et==pin.experimentType){
				html+='<option value="'+et+'" selected="selected">'+et+'</option>';
			} else {
				html+='<option value="'+et+'">'+et+'</option>';
			}
		});
		html+='</select></td>';

		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="a" value="'+pin.a+'"/></td>';
		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="b" value="'+pin.b+'"/></td>';
		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="c" value="'+pin.c+'"/></td>';
		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="alpha" value="'+pin.alpha+'"/></td>';
		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="beta" value="'+pin.beta+'"/></td>';
		html+='<td class="unitcell"><input type="text" class="predictedinfo" name="gamma" value="'+pin.gamma+'"/></td>';
		html+='<td class="comments"><input type="text" class="comments" name="comments" value="'+pin.comments+'"/></td>';
		tableRow.update(html);
		tableRow.id=pin.hook;
		tableRow.addClassName("occupied");
		window.setTimeout(Shipment.watchBarcodeKeyPresses,50);
	},
	
	submit:function(){
		$("header").scrollTo();
		var updatingFields=$$(".updating");
		if(0!=updatingFields.length){
			alert("Some values are still being saved. Please try again in a moment.");
			return false;
		}
		var invalidFields=$$(".invalid");
		if(0!=invalidFields.length){
			alert("Some values are invalid. They are highlighted in red.");
			return false;
		}
		var dewars=$$(".dewar");
		if(0==dewars.length){
			alert("No dewars in shipment.");
			return false;
		}
		var dewarHooks=new Array;
		var valid=true;
		dewars.each(function(dewar){
			var pucks=dewar.select(".shipment_puck");
			if(0==pucks.length){
				var dewarName=dewar.down(".dewarname").innerHTML;
				if(confirm("Dewar '"+dewarName+"' contains no pucks. Remove it from the shipment?")){
					Shipment.removeDewar(dewar.down(".dewarname"));
				};
				valid=false;
			} else {
				pucks.each(function(puck){
					var pins=puck.select(".occupied");
					if(0==pins.length){
						var puckName=puck.down(".puckname").innerHTML;
						if(confirm("Puck '"+puckName+"' contains no pins. Remove it from the shipment?")){
							Shipment.removePuck(puck.down(".puckname"));
						};
						valid=false;
					}
				});
				if(valid){
					dewarHooks.push(dewar.id);
				}
			}
		});
		if(!valid){ return false; }
		showUpdatingModalDialog();
		$("modalWindow_dialog_body").innerHTML=$("modalWindow_dialog_body").innerHTML.replace("Updating...","Saving shipment...");
		
		new Ajax.Request(Shipment.finalURL, {
			method:"post",
			onSuccess:function(transport){ Shipment.submit_onSuccess(transport); },
			onFailure:function(transport){ Shipment.submit_onFailure(transport); },
			parameters:{
				action:"submitShipment",
				dewarHooks:dewarHooks.join(","),
				shipper:$("shipper").value,
				trackingNumber:$("trackingid").value,
				destination:$("destination").value
			}
		});
		
	},
	submit_onSuccess:function(transport){
		closeModalDialog();
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Shipment.ajaxError(transport.responseText);
		}
		if(transport.responseJSON.error){
			return Shipment.ajaxError(transport.responseJSON.error);
		} else if(transport.responseJSON.errors){
			return Shipment.ajaxError(transport.responseJSON.errors.join("\n"));
		}
		var response=transport.responseJSON;
		//If no errors, document.location.href= View url
		//else process errors
		showUpdatingModalDialog();
		$("modalWindow_dialog_body").innerHTML=$("modalWindow_dialog_body").innerHTML.replace("Updating...","Taking you to the shipment...");
		dontWarn();
		window.document.location.href=Shipment.viewURL+response.experimentGroup;
		
	},
	submit_onFailure:function(transport){
		return Shipment.ajaxError(transport.responseText);
	}
	
}
