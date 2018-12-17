/* Global Variables */
var construct_id_store = new Array();
var extNames = new Array();

/* not used
function validateStep1(protSeq) {
   	var frm=document.forms["frmStep1"];
   	if (!isNumeric(frm.target_prot_start.value)) {alert("Target protein start must be Numeric");return false;}
   	if (!isNumeric(frm.target_prot_end.value)) {alert("Target protein end must be Numeric");return false;}
   	
   	//Convert input from strings to numerics
   	var target_prot_start = frm.target_prot_start.value - 0;
	var target_prot_end = frm.target_prot_end.value - 0;
   	
   	if (target_prot_start>target_prot_end){alert('Start position must be smaller than end position');return false;}	
	if (target_prot_start<=0 ||
		target_prot_end>protSeq.length){alert('The start position must be at least 1.\nThe end position must be less than or equal to the sequence length.');return false;}	

	//check if selected sequence contains stop codons
	var selectedSeq = protSeq.substring(target_prot_start-1, target_prot_end-1);
	var stopCodons =0;
	for(var k=0; k<selectedSeq.length; k++){
		if (selectedSeq.charAt(k)=="*"){
		stopCodons++;
		}
	}
	if (stopCodons>0){alert('Selected sequence contains '+stopCodons+ ' stop codons.\nConstruct design will fail.\nPlease adjust your protein start and\\or end positions.');return false;}

	if (target_prot_start==""||target_prot_end==""||frm.construct_id.value=="") {alert('Fields marked with * are required');return false;}

	
	//Need to check that construct id is not only whitespace
	//141107 this is probably better done with strip() method from Prototype, followed by if(""=conId){alert etc..}
	var constId = frm.construct_id.value;
	constId=trimString(constId);
	if(""==constId){alert('Construct id must be a real value'); return false;}
    
	for (i=0;i<construct_id_store.length;i++) {
		var dot =".";
		var dotIndex = construct_id_store[i].indexOf(dot);
		var id = construct_id_store[i].substring(dotIndex+1);
		//alert('Construct id is: '+frm.construct_id.value+'\n construct store has '+construct_id_store.length+' entries'+ 'this one is '+construct_id_store[i]+' id is '+id); return false;
        if (frm.construct_id.value==id) {
            alert('This construct id has already been used for this Target');return false;}
    }			
} */
//TODO check:270407 Step2 not used for version 1.1, separate validation for separate forms
function validateStep2(dnaSeq) {
   	var frm=document.forms["frmStep2"];
	if (!isNumeric(frm.fwd_overlap_len.value)) {alert("Forward Overlap Length must be Numeric");return false;}
	if (!isNumeric(frm.comp_overlap_len.value)) {alert("Complement Overlap Length must be Numeric");return false;}
	
	if (frm.fwd_overlap_len.value>dnaSeq.length){alert('Forward Overlap Length must be <= the DNA sequence length');return false;}	
	if (frm.comp_overlap_len.value>dnaSeq.length){alert('Complement Overlap Length must be <= the DNA sequence length');return false;}
	if (!isValidDnaSequence(frm.forward_primer.value)) {alert('Forward_primer contains illegal characters'); return false;}
	if (!isValidDnaSequence(frm.reverse_primer.value)) {alert('Reverse_primer contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_n.value)) {alert('Expressed protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_c.value)) {alert('Expressed protein C Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_n.value)) {alert('Final protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_c.value)) {alert('Final protein C Terminus contains illegal characters'); return false;}
	if (frm.fwd_overlap_len.value==""||frm.comp_overlap_len.value=="") {alert('Fields marked with * are required');return false;}	
}

function validateStep2a(dnaSeq){
   	var frm=document.forms["frmStep2a"];
	var targetDnaSeq=frm.target_dna_seq.value;
	var targetDnaSeqLength=targetDnaSeq.length-0;
   	var target_prot_start = frm.target_prot_start.value - 0;
	var target_prot_end = frm.target_prot_end.value - 0;
	var dnaSubSeq=targetDnaSeq.substring((target_prot_start*3)-3,target_prot_end*3); 
	var dnaSubSeqLength=dnaSubSeq.length;
	var fwdOverlapLen=frm.fwd_overlap_len.value -0;
	var revOverlapLen=frm.rev_overlap_len.value -0;

	if (frm.fwd_overlap_len.value==""||frm.rev_overlap_len.value=="") {alert('Fields marked with * are required');return false;}
	if (!isNumeric(frm.fwd_overlap_len.value)||frm.fwd_overlap_len.value<1) {alert("Forward Overlap Length must be a positive number");return false;}
    if (fwdOverlapLen>(dnaSubSeq.length)){alert('Forward Overlap Length '+fwdOverlapLen+' must be shorter than the DNA sequence length '+dnaSubSeqLength);return false;}	
	if (!isNumeric(frm.rev_overlap_len.value)||frm.rev_overlap_len.value<1) {alert("Complement Overlap Length must be a positive number");return false;}
    if (revOverlapLen>(dnaSubSeq.length)){alert('Reverse Overlap Length '+revOverlapLen+' must be shorter than the DNA sequence length '+dnaSubSeqLength);return false;}   	
	if (!isValidDnaSequence(frm.forward_extension.value)||!isValidDnaSequence(frm.reverse_extension.value)) {alert('Extensions contain illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_n.value)) {alert('Expressed protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_c.value)) {alert('Expressed protein C Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_n.value)) {alert('Final protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_c.value)) {alert('Final protein C Terminus contains illegal characters'); return false;}
}

function validateStep2b(dnaSeq){
	var frm=document.forms["frmStep2b"];
	if (frm.forward_primer.value==""||frm.reverse_primer.value=="") {alert('Fields marked with * are required');return false;}
	if (!isValidDnaSequence(frm.forward_primer.value)) {alert('Forward primer contains illegal characters'); return false;}
    if (!isValidDnaSequence(frm.reverse_primer.value)) {alert('Reverse primer contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_n.value)) {alert('Expressed protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_c.value)) {alert('Expressed protein C Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_n.value)) {alert('Final protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_c.value)) {alert('Final protein C Terminus contains illegal characters'); return false;}
	
}
function validateStep2c(tm){
   	var frm=document.forms["frmStep2c"];
   	if(document.getElementById("desired_tm")){
   	if (!isNumeric(document.getElementById("desired_tm").value)) {alert("Tm must be a number");return false;}
   	if (1*(document.getElementById("desired_tm").value) <= 0){alert("Tm must be greater than 0");return false;}   	
	}
}

function validateStep3() {
   	var frm=document.forms["frmStep3"];	
	if (frm.description.value.length>30) {alert('Description is restricted to 30 characters.');return false;}	
}
function validateStep3c() {
   	var frm=document.forms["frmStep3c"];
	if (!isValidDnaSequence(frm.forward_extension.value)||!isValidDnaSequence(frm.reverse_extension.value)) {alert('Extensions contain illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_n.value)) {alert('Expressed protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.expressed_prot_c.value)) {alert('Expressed protein C Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_n.value)) {alert('Final protein N Terminus contains illegal characters'); return false;}
	if (!isValidProteinSequence(frm.final_prot_c.value)) {alert('Final protein C Terminus contains illegal characters'); return false;}
	
}
function removeSpaces(string) {
	var tstring = "";
	string = '' + string;
	splitstring = string.split(" ");
	for(i = 0; i < splitstring.length; i++)
	tstring += splitstring[i];
	return tstring;
}
//Function for New Target DNA sequence
function checkTargetDNASeq(val, alias){
	var seqAsString = cleanSequence(val);
	var lenSeq = seqAsString.length;
	if (seqAsString.length%3!=0){
		return alias+ " is not valid.  Length is " +lenSeq+ ": has to be a multiple of 3 "
		} return "";
}
//Function to convert to upper case and check is sequence starts with ATG -new Target
function checkATG(){
	var seq = document.getElementById("dnaSeq").value;
	seq=cleanSequence2(seq);
	if ((seq.substring(0,3)!="ATG")) {
//		return alias+ "does not start with ATG"
		var addATG=confirm("Sequence does not start with ATG.\nAdd ATG to DNA sequence?");
		if(addATG){
			return "ATG"+seq;
			}
		else{
			return seq;
			}
		}return seq;
}
//Function to convert to upper case and trim
function newProtSeq(){
	var seq = document.getElementById("protSeq").value;
	seq=cleanSequence2(seq);
	return seq;
}
// fix start codon
function toggleATG(fp){
	var isChecked = $("check1").checked; 
	var codon = fp.substring(0,3) ;
	if (isChecked) {
		var prompt = "Add ATG to 5'-end of Forward primer?\nAlso adds M to the start of the protein";
		if ('TTG'==codon || 'GTG'==codon) {
			prompt = "Replace codon at 5'-end of Forward primer with ATG?";
		}
		if (! confirm(prompt)) {return};
		// ensure primer starts with ATG
		if ('ATG'!=codon) {
			if ('TTG'==codon || 'GTG'==codon) {
				fp = fp.substring(3);
			}
			fp = 'ATG' + fp;//add ATG to the 5'-end
		}
	} else {
		// ensure primer does not start with ATG
		if ('ATG'==codon) {
			fp = fp.substring(3);
			var first = $("dnaSeq").value.substring(0,3);
			if ('GTG'==first || 'TTG'==first) {
				fp = first+fp;
			}
		}
	}
	//$("forward_primer").value = fp; //this is wrong 23-11-09 PiMS-3046
	return isChecked;
}
//function to get checkbox status, if isChecked and starts with GTG or TTG, or if
//!isChecked and doesn't start with GTG or TTG, we do nothing
function changeStart(fp2){
	var isChecked = $("fixNonstandardStartCodon").checked;
	var start = fp2.substring(0,3);
	//var designType = document.getElementById("design_type").value 
	if((isChecked && fp2.substring(0,3)=='TTG')||(isChecked && fp2.substring(0,3)=='GTG')||(isChecked && fp2.substring(0,3)=='CTG')){/*sequence begins with TTG or GTG or CTG*/
		//if ("York"==designType){
					var changeToATG=confirm("To replace the starting " +start+ " with ATG for the Forward primers\nClick \"OK\" then click the \"Recalculate\" button");
		//	}
		//else{
		//	var changeToATG=confirm("Replace starting " +start+ " with ATG in the Forward primer?");
		//}
		if(changeToATG){
			return true;
		}
		else{
			$("fixNonstandardStartCodon").checked="";
			return false;
		}
	}
	return isChecked;
}  
//function to get radio button status, if isChecked and starts with TTA or TGA or CTA, 
//or if !isChecked and doesn't start with one of these, we do nothing
function toggleSTOP(rp){
	var isChecked;
	var stopCodon = "";
	var newrp = "";
	var addStop = document.getElementsByName("add_stop");
	for(var j=0; j<addStop.length; j++){
		if (addStop[j].checked){
			isChecked=addStop[j].id;
			stopCodon=addStop[j].value;
			break;
			}
		if(isChecked && rp.substring(0,2)!='TAA'||'TGA'||'CTA' ){/*sequence doesn't begin with stop codon*/
			newrp = addStop + rp;//add stop codon to the 5'-end
			}
		else if(!isChecked && rp.substring(0,2)=='TAA'||'TGA'||'CTA') {/*sequence begins with stop codon*/
				rp = rp.substr (2);
			}		
	}if(""!=stopCodon){alert("Add stop codon "+isChecked+" (5'-3'- "+stopCodon+ ")?"); 
			}return stopCodon;
}
//function to trim a string -modified from Marc's in NewTarget
function trimString(str){
	str=str.replace(/^\s+|\s+$/g,"");
	return str;
}
//Function for New DNA Target
function checkLength(val,alias){
 if(val.length <50){
 	return alias+" length must be at least 50 nucleotides"
 	} return "";
 }
//Function for cleaning a DNA sequence after display in pims:sequence tag
function tidySeq(val,alias){
	$("dnaSeq").value = cleanSequence(val);
	return "";	
}
//Function for cleaning a sequence after display in pims:sequence tag
function tidySeq2(val,alias){
	val = cleanSequence(val);
	return "";	
}
//Functions for Protein construct to replace validateStep1
function protConStart(val,alias){
	var valNum = 1*val;
	var end = 1*document.getElementById("target_prot_end").value;
	var targseq = document.getElementById("target_prot_seq").value;
	var maxLen = 1*targseq.length;
	if (0==maxLen) {
		return ""; // no recorded sequence, nothing to check
	}
	if (end < valNum){
    	return alias+" " +val+" must be smaller than the Target protein end position "+ end 
	}if(valNum>maxLen){
		return alias+" must be less than or equal to the length of the Target protein sequence "+maxLen
	}	
	//check if selected sequence contains stop codons
	var selectedSeq = targseq.substring(valNum-1, end);
	
	var stopCodons =0;
	for(var k=0; k<selectedSeq.length; k++){
		if (selectedSeq.charAt(k)=="*"){
		stopCodons++;
		}
	}
	if (stopCodons>0){
		return "Selected sequence contains "+stopCodons+ " stop codons.\nConstruct design will fail.\nPlease adjust your protein start and\\or end positions"		
		}
	//Check if Tm design type selected, DNA seq is >=50	
	var targDNAseq = (selectedSeq.length+1)*3;
	var clickedbutton = $('frmStep1').clickbuttonid;
    if(1*(targDNAseq) <50 && clickedbutton=="primerTm"){
		return "DNA sequence must be at least 50 nucleotides for Tm based primer design\n   Please adjust the Target protein start and \\or end"
		}
	return "";
}
function protConEnd(val,alias){
	var endNum = 1*val;
	var start = 1*document.getElementById("target_prot_start").value;
	var targseq = document.getElementById("target_prot_seq").value;
	var maxLen = 1*targseq.length;
	if (0==maxLen) {
		return ""; // no recorded sequence, nothing to check
	}
	if (endNum<1) {
		return "Target protein end must be not less than 1."
	}
	var partseq = targseq.substring(start-1,endNum);
	if(endNum>maxLen){
		return alias+" must be less than or equal to the Target protein sequence "+maxLen
	}return "";
}
//Functions for DNA Target Construct
function dnaConStart(val,alias){
	var valNum = 1*val;
	var end = 1*document.getElementById("target_dna_end").value;
	var targseq = document.getElementById("targetds").value;
	var maxLen = 1*targseq.length;
	var partseq = targseq.substring(valNum-1,end);
	var sublen = 1*partseq.length;
	if(sublen <50 ){
		return "Selected region "+sublen+" is too short -must be at least 50 nucleotides"
    }if (end < valNum){
    	return alias+" " +val+" must be smaller than the end nucleotide "+ end 
	}if(valNum>maxLen){
		return alias+" must be less than or equal to the Target DNA sequence "+maxLen
	}return "";
}
function dnaConEnd(val,alias){
	var endNum = 1*val;
	var start = 1*document.getElementById("target_dna_start").value;
	var targseq = document.getElementById("targetds").value;
	var maxLen = 1*targseq.length;
	var partseq = targseq.substring(start-1,endNum);
	var sublen = 1*partseq.length;
	if(endNum>maxLen){
		return alias+" must be less than or equal to the target DNA sequence length "+maxLen
	}if(sublen <50 ){
		return "Adjust the start and or end positions"
    }return "";
}
// used in DNAConstructWizardStep1 only
function idValue(val,alias){
 	val = val.strip();
 	if("" == val){
 		return alias+" must be a real value"
 	}
 	if(construct_id_store.length>0){
 		for (i=0;i<construct_id_store.length;i++){
			var dot =".";
			var dotIndex = construct_id_store[i].indexOf(dot);
			var id = construct_id_store[i].substring(dotIndex+1);
    		if (val==id){
    			return "This "+alias+ " has already been used for this Target"
    		} 
    	} 
	} 
	return "";
}
//Function to add new fields to create a new 5'-extension
function newExt(newExt){
	var selFExt = $F("forward_extension");  //Prototype function to get the value of the selected option
	var selRExt = $F("reverse_extension");
	var addNew = "New Extension";
	if(addNew == selFExt){
		//alert("You want to "+selFExt);		
		$("fexName").removeClassName("addNew");
		$("fexSeq").removeClassName("addNew");		
        $("fexREnz").removeClassName("addNew");      
		$("new_fExtName").disabled="";
		$("new_fExtSeq").disabled="";
        $("new_fExtEnz").disabled="";
        if ($("frmStep3c")){    //only record protein tag if this is a SPOT construct, NOT for DNA constructs
            $("fexTag").removeClassName("addNew");
            $("new_fExtTag").disabled="";
            }
	}if(addNew == selRExt){
		//alert("You want to "+selRExt);		
		$("rexName").removeClassName("addNew");
		$("rexSeq").removeClassName("addNew");		
        $("rexREnz").removeClassName("addNew");      
		$("new_rExtName").disabled="";
		$("new_rExtSeq").disabled="";
        $("new_rExtEnz").disabled="";
        if ($("frmStep3c")){    //only record protein tag if this is a SPOT construct, NOT for DNA constructs
            $("rexTag").removeClassName("addNew");
            $("new_rExtTag").disabled="";
            }
	}if(addNew != selFExt) {
		$("fexName").addClassName("addNew");
		$("fexSeq").addClassName("addNew");		
		$("new_fExtName").disabled="disabled";
		$("new_fExtSeq").disabled="disabled";
        $("fexREnz").addClassName("addNew");       
        $("new_fExtEnz").disabled="disabled";
        if ($("frmStep3c")){
            $("fexTag").addClassName("addNew");
            $("new_fExtTag").disabled="disabled";          
            }
    }if(addNew != selRExt){	
		$("rexName").addClassName("addNew");
		$("rexSeq").addClassName("addNew");		
		$("new_rExtName").disabled="disabled";
		$("new_rExtSeq").disabled="diabled";		
        $("rexREnz").addClassName("addNew");       
        $("new_rExtEnz").disabled="disabled";
        if ($("frmStep3c")){        
            $("rexTag").addClassName("addNew");
            $("new_rExtTag").disabled="disabled";
            }
	}
}
function extNameCheck(val, alias){
	for(i=0; i<extNames.length; i++){
		var name=trimString(extNames[i]);
		if(val==name){
			return "A 5'-extension named "+val+ " already exists.\n   Please enter a different name"
		}
	}return "";
}
//Function to ensure that the user-entered tag is only valid protein sequence or '/' or '|'
function extTagCheck(string){
    //alert('Tag is ' +string);
    var goodTag = string.replace(/[^ACDEFGHIKLMNPQRSTVWYXZabcdefghiklmnpqrstvwyxz/\*|]*/g, "");
    goodTag = goodTag.toUpperCase();
    return goodTag;
}
//Function to process encoded tag from an Extension, to model cleavage based on | in sequence
function getTag(string){
    //var tagSeq="";
    var startstring=string.replace(/\*/, "");
    var splitstring=new Array();
    if (startstring =="none"){
        splitstring[0] = "";
        splitstring[1] = "";
        splitstring[2] = "";
        }
    else  {
        splitstring = startstring.split("|");
        for (i=0; i<splitstring.length; i++){
            splitstring[i] = extTagCheck(splitstring[i]); //validation to remove user-entered invalid characters
        }
        if(splitstring.length==1){
            splitstring[1] = splitstring[0];
            splitstring[2] = splitstring[0];
            }
           else{splitstring[2] = splitstring[0] + splitstring[1]; } 
        }
    return splitstring;
}
//Function to call getTag
function populateTags(elem){
    var chosen = elem.selectedIndex;
    var choices = elem.options;
    var tagThingy = getTag(choices[chosen].className);
    var fwd = "forward_extension";
    var rev = "reverse_extension";
    if(elem.name == fwd){
        $('expressed_prot_n').value = tagThingy[2];
        $('final_prot_n').value = tagThingy[1];       
    }
    if(elem.name == rev){
        $('expressed_prot_c').value = tagThingy[2];
        $('final_prot_c').value = tagThingy[0];
    }
    //alert('The direction is ' +elem.name);
}
//Function call to getTag for user-entered Tag to record a New Extension
function populateTags2(elem){
    //alert('Getting ' +elem.name);
    var subToTest = elem.name.substring(0,5);
    var newF = "new_f";
    var newR = "new_r";
    var newTag = elem.value;
    var tagBits = getTag(newTag);
    if (subToTest == newF){
        if(""==(tagBits[1])){
            $('final_prot_n').value = tagBits[0];
        }
        else{
            $('final_prot_n').value = tagBits[1];
        }
    $('expressed_prot_n').value = tagBits[2]; 
    }
    if (subToTest == newR){
            $('final_prot_c').value = tagBits[0];
            $('expressed_prot_c').value = tagBits[2]; 
    }   
}
/* Pop-up window functions uesd in SPOT and dnaTarget pages. Removed from
 * own file SpotDisplayTools.js to reduce script/HTTP overhead.
 * 
 * Functions should be removed from use ASAP
 */
function popUp(URL) {
	newwindow=window.open(URL,'my_window','menubar=0,resizable=1,width=700,height=500,scrollbars=yes');
}

function widePopUp(URL) {
	newwindow=window.open(URL,'my_window','menubar=0,resizable=1,width=900,height=600,scrollbars=yes,screenX=100,screenY=100');
}