/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, contextPath */
function processImagers(transport) {
	alert("listimagers.js - believed unused");
	var reply, temperatures, tot, temperature, i, j, k;
	reply = PIMS.xtal.parseJson(transport.responseText);

	//This is a list of imager-temperature pairs...
	// need to convert to make list of imagers and list of temperatures...
	//and add them to the select items!

	temperatures = {};
	tot = reply.imagers.length;
	temperature = "";
	for (i = 0; i < reply.imagers.length; i++) {
		PIMS.xtal.getElement("imager").options[i + 1] = new Option(
				reply.imagers[i].name + " (" + reply.imagers[i].temperature +
						"\u00B0C)", reply.imagers[i].name);
		temperature = "" + reply.imagers[i].temperature;
		temperatures[temperature] = 1;
		//temperatures[reply.imagers[i].temperature] = "";
	}
	k = 1;
	for (j in temperatures) {
		if (temperatures[j] === 1) {
			PIMS.xtal.getElement("temperature").options[k] = new Option(j +
					"\u00B0C", j);
			k++;
		}
	}
}

function getImagerInfo() {
	var url = contextPath + "/ListImagers?";
	PIMS.xtal.asyncRequest('GET', url, {
		success : processImagers,
		scope : this
	}, "");
}