/* 
 * Move a containable into a container
 * */

var destination = {hook:null, rowPosition:0, colPosition:0, subPosition:0, name:'this container'};
var content = {hook:null};
var containable = {hook:null, name:null};

function containableSearch( dbModelClass, roleName, container){    
    destination = container;     
    var searchURL=contextPath+"/Search/"+dbModelClass+
    	"?isInModalWindow=yes" +
    	"&callbackFunction=submitMoveContainable";    
    openModalWindow(searchURL,
    		"Choose "+roleName);    	
}
function containerSearch( sample){    
    containable = sample;     
    var searchURL=contextPath+"/Search/org.pimslims.model.holder.Holder"+
    	"?isInModalWindow=yes" +
    	//TODO showNextFree=yes
    	"&callbackFunction=submitMoveToContainer";    
    openModalWindow(searchURL,
    		"Choose Container");    	
}


/* @param containable: a representation of the thing to move, e.g.
 * {name:'000918.con1F',  hook:'org.pimslims.model.sample.Sample:100372'  }
*/
function submitMoveContainable(sample){
	submitMove(sample, destination);
}

function submitMoveToContainer(container) {
	var position = '';
	if (''!=container.rowPosition) {
		position = ":"+container.rowPosition+","+container.colPosition;
		
	}
	var ok=confirm("Move "+containable.name+" to "+container.name+position+"?");
	if (ok) {submitMove(containable, container);}
}

function submitMove(sample, container) {
	$("dummy_copyform").action=contextPath+"/update/Move/"+sample['hook']
	+"?action=addto&destination="+container.hook+"&rowPosition="+container.rowPosition+"&colPosition="+container.colPosition+"&subPosition="+container.subPosition;
    $("dummy_copyform").submit();
}
// obsolete
function locationSearch(containable){    
    content = containable;    
    var searchURL=contextPath+"/Search/org.pimslims.model.location.Location" 
    	+ "?isInModalWindow=yes&callbackFunction=submitMoveToLocation";    
    openModalWindow(searchURL,
    		"Choose Location");    	
}
//obsolete
function submitMoveToLocation(location){
	$("dummy_copyform").action=contextPath+"/update/Move/"+content['hook']
	+"?action=addto&destination="+location.hook
	+"&rowPosition=0&colPosition=0&subPosition=0";
    $("dummy_copyform").submit();
}

function submitRemove(name,hook,container){
	  var from="here?";
	  if (null!=container) {
		  from = container+"?";
	  }
	  if(confirm('Are you sure you want to remove '+name+' from '+from)){ 
	  	$("dummy_copyform").action=contextPath+"/update/Move/"+hook+"?action=remove";
	    $("dummy_copyform").submit();
	  }
}
