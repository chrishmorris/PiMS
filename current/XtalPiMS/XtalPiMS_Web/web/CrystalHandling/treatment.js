var Treatment={

		
	/******************************************************************************************
	 * NAVIGATION functions
	 ******************************************************************************************/
	inspection: {},

	getInspection: function(){
		url = contextPath + "/TrialDropServlet?startIndex=0&results=-1&sort=well&dir=asc&barcode="+plateBarcode+"&inspectionName=-&subPosition=-1";
		new Ajax.Request(url, {
			method: "GET",
			onSuccess: function (transport) { 
				if(!transport.responseJSON){
					//die quietly, don't show nav
					return false;
				}
				Treatment.inspection=transport.responseJSON;
				Treatment.writeNav();
			},
			onFailure: function (transport) { 
				//die quietly, don't show nav
			}
		});
	},

	initNav: function(){
		Treatment.getInspection();
	},
	writeNav: function(){
		html='Go to drop: <select name="nav" onChange="Treatment.doNav(this)">';
		Treatment.inspection.records.each(function(r){
			if(well==r.well){
				html+='<option selected="selected" value="'+r.well+'">'+r.well+'</option>';
			} else {
				html+='<option value="'+r.well+'">'+r.well+'</option>';
			}
		});
		html+='</select>';
		$("nav").innerHTML=html;
		
		var wellParts=well.split(".");
		var currentSub=1;
		if(wellParts.length>1){
			currentSub=wellParts[1];
		}
		var subs={};
		Treatment.inspection.records.each(function(r){
			var dropName=r.well;
			var parts=dropName.split(".");
			var wellName=parts[0];
			var subPosition=1;
			if(parts.length>1){
				subPosition=parts[1];
			}
			if(undefined==subs["sub"+subPosition]){
				subs["sub"+subPosition]={};
				subs["sub"+subPosition].subPosition=subPosition;
				subs["sub"+subPosition].drops=[];
			}
			subs["sub"+subPosition].drops.push(r);
		});
		Object.keys(subs).each(function(s){
			var sub=subs[s];
			if(0==sub.drops.length){
				return; //from inner function - "continue"
			} else if(96==sub.drops.length){
				var rowLabels=["","A","B","C","D","E","F","G","H"];
				var tbl='<table style="border:none;border-collapse:collapse" id="sub'+sub.subPosition+'">';
				tbl+='<tr>';
				tbl+='<th style="border:none">&nbsp;</th>';
				for(i=1;i<=12;i++){
					tbl+='<th style="border:none">'+i+'</th>';
				}
				tbl+='</tr>';
				for(r=1;r<=8;r++){
					tbl+='<tr>';
					tbl+='<th style="border:none">'+rowLabels[r]+'</th>';
					for(c=1;c<=12;c++){
						dropNum=(((r-1)*12)+c)-1;
						var drop=sub.drops[dropNum];
						var bg='#ccc';
						if(0<drop.humanScores.length){
							bg=drop.humanScores[0].colour;
						}
						var curr='';
						if(drop.well==well){
							curr=' currentwell';
						}
						tbl+='<td style="border:none"><a href="'+contextPath+'/update/SelectCrystal?barcode='+plateBarcode+'&well='+drop.well+'"><span class="well96'+curr+'" style="background-color:'+bg+'">'+dropNum+'</span></a></td>';
					}
					tbl+='</tr>';
				}
				tbl+='</table>';
				html='<div id="wells">'+tbl+'</div>';
			} else {
				//blast them all out
			}
		});
		$("nav").innerHTML=html;
	},
	showNav: function(subPosition){
		
	},
	doNav: function(elem){
		elem=$(elem);
		var newWell;
		if(elem.id){
			newWell=elem.id;
		} else if(elem.value){
			newWell=elem.value;
		} else {
			return false;
		}
		document.location.href=contextPath+"/update/SelectCrystal?barcode="+plateBarcode+"&well="+newWell;
	},
	
	
	
		
	/******************************************************************************************
	 * MISCELLANEOUS functions
	 ******************************************************************************************/

	/* See viewtrialdrops3.js - this version doesn't care about success or failure and just submits the parent form */
	annotateSynchrotron: function(scheme, mountForm) {
		var ao, annotation, _img, url;
		annotation = "Synchrotron";
		if ("" !== annotation) {
			url = contextPath + "/ScoreImageServlet?barcode=" + plateBarcode + "&well=" + well +  "&scheme=" + scheme + "&annotation=" + annotation;
			new Ajax.Request(url, {
				method: "POST",
				onSuccess: function (transport) { $(mountForm).submit(); },
				onFailure: function (transport) { $(mountForm).submit(); }
			});
		}
	},

	initMountForm: function(mountForm){
		mountForm=$(mountForm);
		var pinField=mountForm.down("[name='pin']");
		if(!pinField){ return; }
		pinField.onkeyup=function(){ Treatment.verifyPinBarcode(pinField) };
		pinField.up().insert('<img src="'+contextPath+'/skins/default/images/icons/actions/waiting.gif" class="pinspinner" style="display:none" />');
		pinField.setStyle({"width": (pinField.getWidth()-30)+"px" });
		var puckField=mountForm.down("[name='puck']");
		puckField.onkeyup=function(){ Treatment.verifyPuckBarcode(puckField) };
		puckField.setStyle({"width":
			(puckField.getWidth()-30)+"px"
		});
		puckField.up().insert('<img src="'+contextPath+'/skins/default/images/icons/actions/waiting.gif" class="puckspinner" style="display:none" />');
		var dummyPinButton=$("createdummypin");
		dummyPinButton.onclick=function(){ Treatment.makeDummyPin(dummyPinButton); }
		var urlField=mountForm.down("input[name=treatmentURL]");
		var loc=window.document.location+"";
		urlField.value=loc.replace(window.document.location.hash,"");
		suppressSubmitOnEnter(pinField);
		suppressSubmitOnEnter(puckField);
	},
	
	/******************************************************************************************
	 * PIN functions
	 * Functions for verifying pin barcode. Where a site uses barcoded pins, these should be registered
	 * in advance so that they can be created in a Lab Notebook that all users can see. This also prevents
	 * bad reads from generating bogus pin records that could cause trouble later.
	 ******************************************************************************************/

	verifyPinBarcode: function(elem){
		if(undefined!=window.pinBarcodeTimeout){
			window.clearTimeout(pinBarcodeTimeout);
		}
		window.pinBarcodeTimeout=window.setTimeout(function(){ Treatment.doVerifyPinBarcode(elem) },1000);
	},
	
	doVerifyPinBarcode: function(elem){
		var field=$(elem);
		var spinner=field.up().down("img");
		field.removeClassName("error");
		var barcode=field.value;
		if(""==barcode){
			if(field.up().down("#dummypinbutton")){
				field.up().down("#dummypinbutton").style.display="inline";
			}
			return;
		} else {
			if(field.up().down("#dummypinbutton")){
				field.up().down("#dummypinbutton").style.display="none";
			}
		}
		spinner.setStyle({ "display":"inline" });
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"get",
			onSuccess:function(transport){ Treatment.doVerifyPinBarcode_onSuccess(transport, elem); },
			onFailure:function(transport){ Treatment.doVerifyPinBarcode_onFailure(transport, elem); },
			parameters:{
				verifyPinBarcode:barcode
			}
		});
	},
	
	doVerifyPinBarcode_onSuccess: function(transport, elem){
		var field=$(elem);
		var spinner=field.up().down("img");
		spinner.setStyle({ "display":"none" });
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Treatment.doVerifyPinBarcode_onFailure(transport, elem);
		}
		var currentBarcode=field.value;
		var checkedBarcode=transport.responseJSON.pinBarcode;
		var exists=transport.responseJSON.pinExists;
		var puckExists=transport.responseJSON.puckExists;
		var mayUpdate=transport.responseJSON.mayUpdate;
		if(currentBarcode!=checkedBarcode){
			return; //something was typed since launching check
		}
		if(!exists){
			if(puckExists){
				var puckField=field.up("form").down("[name='puck']");
				var puckBarcode=puckField.value;
				if(checkedBarcode==puckBarcode){
					alert(checkedBarcode+" is a puck, and is already selected.")
				} else if(""!=puckBarcode){
					if(confirm(checkedBarcode+" is a puck. Use "+puckField.value+" instead of "+checkedBarcode+"?")){
						puckField.value=checkedBarcode;
						Treatment.verifyPuckBarcode(puckField);
					}
				} else {
					puckField.value=checkedBarcode;
					Treatment.verifyPuckBarcode(puckField);
				}
				field.removeClassName("error");
				field.value="";
				
			} else {
				if(confirm("Pin "+checkedBarcode+" does not exist in the database. Create it now?")){
					field.removeClassName("error");
					field.hasErrors=false;
					window.setTimeout(function(){ Treatment.makePin(checkedBarcode); }, 50);
					return;
				} else {
					field.addClassName("error");
					field.hasErrors=true;
				}
			}
		} else if(!mayUpdate) {
			field.addClassName("error");
			alert("You do not have permission to update pin "+checkedBarcode+".\nTalk to your xtalPiMS administrator.");
			field.hasErrors=true;
		} else {
			field.removeClassName("error");
			field.hasErrors=false;
		}
	},
	doVerifyPinBarcode_onFailure: function(transport, elem){
		var field=$(elem);
		var spinner=field.up().down("img");
		spinner.setStyle({ "display":"none" });
		alert("There was a problem and the pin barcode could not be verified.");
	},

	/*
	 * Functions for creating a dummy pin with barcode. Where a site does not use barcoded pins, we need a
	 * way to identify pins - crystals must still be in pins that are identifiable to the database.
	 */
	makeDummyPin:function(formElement){
		var mountForm=$(formElement).up("form");
		var pinField=mountForm.down("[name='pin']");
		pinField.addClassName("updating");
		mountForm.down(".pinspinner").setStyle({ "display":"inline" });
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"post",
			onSuccess:function(transport){ Treatment.makePin_onSuccess(transport, mountForm); },
			onFailure:function(transport){ Treatment.makePin_onFailure(transport, mountForm); },
			parameters:{
				createDummyPin:"yes"
			}
		});
	},
	makePin:function(pinBarcode){
		var mountForm=$("harvestmountform");
		var pinField=mountForm.down("[name='pin']");
		pinField.addClassName("updating");
		pinField.removeClassName("error");
		pinField.hasErrors=false;
		mountForm.down(".pinspinner").setStyle({ "display":"inline" });
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"post",
			onSuccess:function(transport){ Treatment.makePin_onSuccess(transport, mountForm); },
			onFailure:function(transport){ Treatment.makePin_onFailure(transport, mountForm); },
			parameters:{
				createPin:pinBarcode
			}
		});
	},
	makePin_onSuccess: function(transport, mountForm){
		if(!transport.responseJSON || transport.responseJSON.error){
			return Treatment.makeDummyPin_onFailure(transport);
		}
		mountForm=$(mountForm);
		var pinField=mountForm.down("[name='pin']");
		pinField.value=transport.responseJSON.createdPin;
		mountForm.down(".pinspinner").setStyle({ "display":"none" });
		pinField.removeClassName("updating");
		pinField.removeClassName("error");
		pinField.hasErrors=false;
	},
	makePin_onFailure: function(transport, mountForm){
		if(transport.responseJSON && transport.responseJSON.error){
			alert(transport.responseJSON.error);
		}
		mountForm.down(".pinspinner").remove();
		var pinField=mountForm.down("[name='pin']");
		pinField.removeClassName("updating");
	},

	
	/******************************************************************************************
	 * PUCK functions
	 ******************************************************************************************/
	
	/**
	 * set cookie with previous puck barcode. Avoid repeated scan during long mount sessions.
	 */
	setLastPuck: function(puckBarcode){
		var hours=2; //cookie expires after this	
		var date = new Date();
		date.setTime(date.getTime()+(hours*60*60*1000));
		var expires = "; expires="+date.toGMTString();
		var str = "lastPuck="+puckBarcode+expires+"; path=/";
		document.cookie= str;
	},
	/**
	 * get cookie with previous puck barcode. Avoid repeated scan during long mount sessions.
	 */
	getLastPuck: function(){
		var nameEQ = "lastPuck" + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;	
	},

	doPuckPickerColors: function(elem){
		elem=$(elem);
		var picker=elem.up(".puckpicker");
		picker.select("input[type=radio]").each(function(inp){
			if(inp.checked){
				inp.up("label").style.color="#ff0";
				inp.up("label").style.backgroundColor="#333";
			} else {
				inp.up("label").style.color="#333";
				inp.up("label").style.backgroundColor="#fff";
			}
		});
	},
	 
	 
	verifyPuckBarcode: function(puckField){
		if(undefined!=window.puckBarcodeTimeout){
			window.clearTimeout(puckBarcodeTimeout);
		}
		window.puckBarcodeTimeout=window.setTimeout(function(){Treatment.doVerifyPuckBarcode(puckField)},1000);
	},
	doVerifyPuckBarcode: function(puckField){
		var mountForm=puckField.up("form");
		puckField.removeClassName("error");
		var puckContents=mountForm.down(".puckcontents");
		var barcode=puckField.value;
		if(""==barcode){
			if(puckContents.originalHTML){
				puckContents.innerHTML=puckContents.originalHTML;
			}
			return;
		}
		mountForm.down(".puckspinner").setStyle({ "display":"inline" });
		if(!puckContents.originalHTML){
			puckContents.originalHTML=puckContents.innerHTML;
		}
		puckContents.innerHTML='<img src="'+contextPath+'/skins/default/images/icons/actions/waiting.gif" />';
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"get",
			onSuccess:function(transport){ Treatment.doVerifyPuckBarcode_onSuccess(transport, puckField); },
			onFailure:function(transport){ Treatment.doVerifyPuckBarcode_onFailure(transport, puckField); },
			parameters:{
				verifyPuckBarcode:barcode
			}
		});
	},
	doVerifyPuckBarcode_onSuccess: function(transport, puckField){
		puckField=$(puckField);
		var mountForm=puckField.up("form");
		mountForm.down(".puckspinner").setStyle({ "display":"none" });
		ajax_checkStillLoggedIn(transport);
		if(!transport.responseJSON){
			return Treatment.doVerifyPuckBarcode_onFailure(transport);
		}
		var puckContents=mountForm.down(".puckcontents");
		var currentBarcode=puckField.value;
		var checkedBarcode=transport.responseJSON.puckBarcode;
		var exists=transport.responseJSON.puckExists;
		var mayUpdate=transport.responseJSON.mayUpdate;
		if(currentBarcode!=checkedBarcode){
			return; //something was typed since launching check
		}
		if(!exists){
			if(confirm("Puck "+checkedBarcode+" does not exist in the database. Create it now?" )){
				return Treatment.makePuck(checkedBarcode);
			}			
			puckField.addClassName("error");
			puckContents.innerHTML=puckContents.originalHTML;
			//alert(checkedBarcode+" does not exist in the database, or is not a Puck");
			puckField.addClassName("error");
			puckField.hasErrors=true;
		} else if(!mayUpdate){
			puckField.addClassName("error");
			puckContents.innerHTML=puckContents.originalHTML;
			alert("You do not have permission to update puck "+checkedBarcode+"");
			puckField.addClassName("error");
			puckField.hasErrors=true;
		} else {
			Treatment.setLastPuck(checkedBarcode);
			puckField.removeClassName("error");
			puckField.hasErrors=false;
			var puck=transport.responseJSON.pins;
			var firstRadioWritten=false;
			var html='<div style="display:table" class="puckpicker">';
			html+='<div style="display:table-row">';
			var separatorWritten=false;
			var labelStyle="display:table-cell;width:2.5em;height:2.5em;position:relative;top:0;left:0;line-height:2.5em;border:1px solid #666;color:#999;font-size:160%;text-align:center;";
			for(i=1;i<=puck.positions;i++){
				var isFilled=false;
				for(j=0;j<puck.pins.length;j++){
					if(puck.pins[j].puckPosition==i){
						isFilled=true;
						break;
					}
				}
				if(isFilled){
					html+='<label style="'+labelStyle+'background-color:#ccc;">'+i+"";
					//html+="X";
					html+='</label>';
				} else if(!firstRadioWritten){
					html+='<label style="'+labelStyle+'background-color:#ff0;cursor:pointer">'+i+"";
					firstRadioWritten=true;
					html+='<input style="display:none" type="radio" checked="checked" name="puckPosition" value="'+i+'" onclick="Treatment.doPuckPickerColors(this)"/>';
					html+='</label>';
				} else {
					html+='<label style="'+labelStyle+'background-color:#fff;;cursor:pointer">'+i+"<br/>";
					html+='<input style="display:none" type="radio" name="puckPosition" value="'+i+'" onclick="Treatment.doPuckPickerColors(this)" />';
					html+='</label>';
				}
				if(i>=(puck.positions/2) && !separatorWritten){
					separatorWritten=true;
					html+='</div><div style="display:table-row">';
				}
			}
			html+='</div></div>';
			if(firstRadioWritten){
				puckContents.innerHTML=html;
			} else {
				puckContents.innerHTML='<span class="error">Puck is full. Empty it or scan another. </span>';
				puckField.hasErrors=true;
				puckField.addClassName("error");
			}
			if(puck.pins.length>0){
				//Don't show the "Clear puck" button. Instead allow emptying from shipmentview.
				//$("puckcontents").innerHTML+='<input type="button" value="Empty this puck" onclick="clearPuck()"/>';
			}
		}
		Treatment.doPuckPickerColors(mountForm.down(".puckpicker").down());
		Treatment.checkCanSubmitMountForm(mountForm);
	},
	doVerifyPuckBarcode_onFailure: function(transport, mountForm){
		$(mountForm).down(".puckspinner").setStyle({ "display":"none" });
		mountForm.down(".puckcontents").innerHTML=mountForm.down(".puckcontents").originalHTML;
		alert("There was a problem and the puck barcode could not be verified.");
	},

	clearPuck: function(){
		var barcode=$("puck").value;
		$("puckcontents").innerHTML='<img src="'+contextPath+'/skins/default/images/icons/actions/waiting.gif" />';
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"post",
			onSuccess:function(transport){ Treatment.clearPuck_onSuccess(transport); },
			onFailure:function(transport){ Treatment.clearPuck_onFailure(transport); },
			parameters:{
				clearPuck:barcode
			}
		});
	},
	clearPuck_onSuccess: function(transport){
		$("puckcontents").innerHTML=$("puckcontents").originalHTML;
		if(!transport.responseJSON){
			return clearPuck_onFailure(transport);
		} else if(transport.responseJSON.error){
			return alert(transport.responseJSON.error);
		}
		Treatment.doVerifyPuckBarcode_onSuccess(transport);
	},
	clearPuck_onFailure: function(transport){
		$("puckcontents").innerHTML=$("puckcontents").originalHTML;
		alert("There was a problem and the puck could not be emptied");
		Treatment.verifyPuckBarcode();
	},
	makePuck:function(puckBarcode){
		var mountForm=$("harvestmountform");
		var puckField=mountForm.down("[name='puck']");
		puckField.addClassName("updating");
		puckField.removeClassName("error");
		puckField.hasErrors=false;
		mountForm.down(".puckspinner").setStyle({ "display":"inline" });
		new Ajax.Request(window.document.location.pathname.replace("/SelectCrystal","/CrystalTreatment"), {
			method:"post",
			onSuccess:function(transport){ Treatment.makePuck_onSuccess(transport, mountForm); },
			onFailure:function(transport){ Treatment.makePuck_onFailure(transport, mountForm); },
			parameters:{
				createPuck:puckBarcode
			}
		});
	},
	makePuck_onSuccess: function(transport, mountForm){
		if(!transport.responseJSON || transport.responseJSON.error){
			return Treatment.makePuck_onFailure(transport);
		}
		mountForm=$(mountForm);
		var puckField=mountForm.down("[name='puck']");
		puckField.value=transport.responseJSON.createdPuck;
		mountForm.down(".puckspinner").setStyle({ "display":"none" });
		puckField.removeClassName("updating");
		puckField.removeClassName("error");
		puckField.hasErrors=false;
		//lastly, go and get the details of the new puck as if it existed all along
		Treatment.verifyPuckBarcode(puckField);
	},
	makePuck_onFailure: function(transport, mountForm){
		if(transport.responseJSON && transport.responseJSON.error){
			alert(transport.responseJSON.error);
		}
		mountForm.down(".puckspinner").remove();
		var puckField=mountForm.down("[name='puck']");
		puckField.removeClassName("updating");
	},

	
	
	submitMountForm: function(submitButton){
		submitButton=$(submitButton);
		var mountForm=submitButton.up("form");
		var pinField=mountForm.down("[name='pin']");
		var puckField=mountForm.down("[name='puck']");
		var urlField=mountForm.down("[name='treatmentURL']");
		if(!pinField || !puckField){ return false; }
		if(pinField.hasErrors || puckField.hasErrors){ return false; }
		if(pinField.hasClassName("updating") || puckField.hasClassName("updating")){
			window.setTimeout(function(){ Treatment.submitMountForm(submitButton) }, 250);
			return false;
		}
		if(""==pinField.value && ""==puckField.value){
			alert("Pin or puck barcode is required.");
			return false;
		} else if(""==pinField.value && ""!=puckField.value){
			Treatment.makeDummyPin(pinField);
			window.setTimeout(function(){ Treatment.submitMountForm(submitButton) }, 250);
			return false;
		}
		if(urlField){ urlField.value=document.location.href; }
		dontWarn();
		mountForm.down(".shipbutton").value="Submitting...";
		Treatment.annotateSynchrotron('default', mountForm);
		//annotateSynchrotron does the submit.
	},
	

	
	/******************************************************************************************
	 * ADD NOTES functions
	 ******************************************************************************************/
    notesTextarea:{},
    
    goToNotes: function(){
    	openCollapsibleBox("notes");
    	var textareas=$("notes").select("textarea");
    	Treatment.notesTextarea=textareas.last();
    	Treatment.notesTextarea.focus();
    	Treatment.notesTextarea.scrollTo();
    	Treatment.notesTextarea.blu=255;
    	window.setTimeout(Treatment.highlightNotesTextarea,150);
    },
    highlightNotesTextarea: function(){
    	var blu=Treatment.notesTextarea.blu;
    	blu-=15;
    	Treatment.notesTextarea.blu=blu;
    	Treatment.notesTextarea.style.background="rgb(255,255,"+blu+")";
    	if(blu>0){ 
    		window.setTimeout(Treatment.highlightNotesTextarea,10);
    	}else{
    		window.setTimeout(Treatment.fadeNotesTextarea,100);
    	}
    },
    fadeNotesTextarea: function(){
    	var blu=Treatment.notesTextarea.blu;
    	blu+=5;
    	Treatment.notesTextarea.blu=blu;
    	Treatment.notesTextarea.style.background="rgb(255,255,"+blu+")";
    	if(blu<255){ window.setTimeout(Treatment.fadeNotesTextarea,10); }
    }
    
    
}