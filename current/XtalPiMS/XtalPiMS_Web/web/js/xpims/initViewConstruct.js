/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for ViewConstruct.jsp
 */
PIMS.xtal.initViewConstruct = function () {
	var url, processConstructData, toggleFilter, ftPlates, ftSamples;

	// Get construct info
	processConstructData = function (transport) {
		var data = PIMS.xtal.parseJson(transport.responseText);
		if (data.records.length > 0) {
			PIMS.xtal.getElement("construct_name").innerHTML = data.records[0].constructName;
			PIMS.xtal.getElement("p_constructName").value = data.records[0].constructName;
			PIMS.xtal.getElement("s_constructName").value = data.records[0].constructName;
			PIMS.xtal.getElement("construct_description").innerHTML = data.records[0].description;
			PIMS.xtal.getElement("construct_history").innerHTML = "Link to PIMS Construct";
			if (data.records[0].targetName) {
				PIMS.xtal.getElement("construct_target").innerHTML = "Link to PIMS Target for " + data.records[0].targetName;
			}
			PIMS.xtal.getElement("construct_owner").innerHTML = data.records[0].owner;
			PIMS.xtal.getElement("construct_group").innerHTML = data.records[0].group;
		} else {
			PIMS.xtal.getElement('content').innerHTML = "<h3>You do not have permission to see this construct</h3>";
		}
	};
	url = contextPath + "/ConstructsServlet?startIndex=0&results=1&sort=constructName&dir=asc&id=" + PIMS.xtal.constructId;
	PIMS.xtal.asyncRequest('GET', url, {success: processConstructData, scope: this}, "");

	// Bind filter toggle listeners
	PIMS.xtal.addListener('show_hide_plate_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_sample_filter', 'click', PIMS.xtal.toggleFilter);

	PIMS.xtal.Imagers.init();

	ftPlates = PIMS.xtal.initFilterPlates({
		preset: {
			sb: 'sb_c_p',
			constructId: PIMS.xtal.constructId
		},
		sbMustMatch: {
			constructId: PIMS.xtal.constructId
		},
		ignore: ['constructName']
	});
	ftSamples = PIMS.xtal.initFilterSamples({
		preset: {
			sb: 'sb_c_s',
			constructId: PIMS.xtal.constructId
		},
		sbMustMatch: {
			constructId: PIMS.xtal.constructId
		},
		ignore: ['constructName']
	});

	ftPlates.initialize();
	ftSamples.initialize();

	PIMS.xtal.getElement("p_constructName").readOnly = 'readonly';
	PIMS.xtal.getElement("s_constructName").readOnly = 'readonly';

	PIMS.xtal.addListener("p_clear", "click", function () {
		PIMS.xtal.getElement("p_constructName").value = PIMS.xtal.getElement("construct_name").innerHTML;
	});
	PIMS.xtal.addListener("s_clear", "click", function () {
		PIMS.xtal.getElement("s_constructName").value = PIMS.xtal.getElement("construct_name").innerHTML;
	});

	PIMS.xtal.initializeHistory();
};
