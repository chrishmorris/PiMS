var data;

function processManufacturerData(transport){
	try {
    data = transport.responseText.evalJSON();
    fillData();
	} catch (e) {
		alert("Invalid data: "+transport.responseText);
	}
}

function getScreenData() {
    var url = contextPath + "/ScreenManufacturerServlet";
    var pars = "";
    var ajaxObject = new Ajax.Request(            
        url, 
        {
            method: 'get', 
            parameters: pars,
            onComplete: processScreenData
        }
    );
    
}

function fillData() {

}