/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for ViewSample.jsp
 */
PIMS.xtal.initViewSample = function () {
	var url, processSampleData, ftAnnotations, ftPlates;

	// Get construct info
	processSampleData = function (transport) {
		var data, rec;
		data = PIMS.xtal.parseJson(transport.responseText);
		if (data.records.length > 0) {
			rec = data.records[0];
			PIMS.xtal.getElement("sample_name").innerHTML = rec.Name;
			PIMS.xtal.getElement("sample_description").innerHTML = rec.Description;
			PIMS.xtal.getElement("sample_history").innerHTML = '<a target="new" href="' + data.SampleLinkURL + '">' + data.SampleLinkTitle + '<\/a>';
			// TODO Populate fully
			PIMS.xtal.getElement("p_sampleName").value = rec.Name;
			PIMS.xtal.getElement("a_sampleName").value = rec.Name;
		} else {
			PIMS.xtal.getElement('content').innerHTML = "<h3>You do not have permission to see this sample</h3>";
		}
	};
	url = contextPath + "/SamplesServlet?sb=viewsample&startIndex=0&results=1&sort=name&dir=asc&sample=" + PIMS.xtal.sampleName;
	PIMS.xtal.asyncRequest('GET', url, {success: processSampleData, scope: this}, "");

	// Bind filter toggle listeners
	PIMS.xtal.addListener('show_hide_annotation_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_plate_filter', 'click', PIMS.xtal.toggleFilter);

	PIMS.xtal.Imagers.init();

	ftAnnotations = PIMS.xtal.initFilterAnnotations({
		preset: {
			sb: 'sb_sa_a',
			sampleName: PIMS.xtal.sampleName
		},
		sbMustMatch: {
			sampleName: PIMS.xtal.sampleName
		},
		ignore: ['sampleName']
	});
	ftPlates = PIMS.xtal.initFilterPlates({
		preset: {
			sb: 'sb_sa_p',
			sampleName: PIMS.xtal.sampleName
		},
		sbMustMatch: {
			sampleName: PIMS.xtal.sampleName
		},
		ignore: ['sampleName']
	});

	ftAnnotations.initialize();
	ftPlates.initialize();

	//PIMS.xtal.getElement("a_sampleName").readOnly = 'readonly';
	PIMS.xtal.getElement("p_sampleName").readOnly = 'readonly';

	PIMS.xtal.initializeHistory();
};
