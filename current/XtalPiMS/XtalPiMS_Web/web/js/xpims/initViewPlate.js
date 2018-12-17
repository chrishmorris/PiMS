/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for ViewPlate.jsp
 */
PIMS.xtal.initViewPlate = function () {
	var url, processPlateData, ftInspections, ftSchedules, ftAnnotations;

	// Get construct info
	processPlateData = function (transport) {
		var data, rec;
		data = PIMS.xtal.parseJson(transport.responseText);
		if (data.records.length > 0) {
			rec = data.records[0];
			PIMS.xtal.getElement("plate_barcode").innerHTML = rec.barcode;
			PIMS.xtal.getElement("plate_type").innerHTML = rec.plateType;
			PIMS.xtal.getElement("plate_runby").innerHTML = rec.runBy;
			PIMS.xtal.getElement('plate_createDate').innerHTML = (new Date(rec.createDate)).toUTCString();
			if (rec.destroyDate) {
				PIMS.xtal.getElement('plate_destroyDate').innerHTML = (new Date(rec.destroyDate)).toUTCString();
			}
			PIMS.xtal.getElement('plate_screen').innerHTML = rec.screen;
			PIMS.xtal.getElement('plate_imager').innerHTML = rec.imager;
			PIMS.xtal.getElement('experiment_owner').innerHTML = rec.owner;
			PIMS.xtal.getElement('experiment_description').innerHTML = rec.description;
			PIMS.xtal.getElement('experiment_sample').innerHTML = rec.sampleName;
			PIMS.xtal.getElement('experiment_construct').innerHTML = rec.constructName;
			PIMS.xtal.getElement("i_barcode").value = rec.barcode;
			PIMS.xtal.getElement("sh_barcode").value = rec.barcode;
			PIMS.xtal.getElement("a_barcode").value = rec.barcode;
		} else {
			PIMS.xtal.getElement('content').innerHTML = "<h3>You do not have permission to see this plate</h3>";
		}
	};
	url = contextPath + "/PlatesServlet?sb=viewplate&startIndex=0&results=1&sort=barcode&dir=asc&barcode=" + PIMS.xtal.barcode;
	PIMS.xtal.asyncRequest('GET', url, {success: processPlateData, scope: this}, "");

	// Microscope images
	url = contextPath + "/ImagesServlet?results=-1&startIndex=0&sort=date&dir=desc&imageType=zoomed&barcode=" + PIMS.xtal.barcode;
	PIMS.xtal.asyncRequest('GET', url, {success: PIMS.xtal.MicroImages.displayMicroImages, scope: this}, "");

	// Bind filter toggle listeners
	PIMS.xtal.addListener('show_hide_inspection_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_schedule_filter', 'click', PIMS.xtal.toggleFilter);
	PIMS.xtal.addListener('show_hide_annotation_filter', 'click', PIMS.xtal.toggleFilter);

	PIMS.xtal.Imagers.init();

	ftInspections = PIMS.xtal.initFilterInspections({
		preset: {
			sb: 'sb_p_i',
			barcode: PIMS.xtal.barcode
		},
		sbMustMatch: {
			barcode: PIMS.xtal.barcode
		},
		ignore: ['barcode']
	});
	ftSchedules = PIMS.xtal.initFilterSchedules({
		preset: {
			sb: 'sb_p_sh',
			barcode: PIMS.xtal.barcode
		},
		sbMustMatch: {
			barcode: PIMS.xtal.barcode
		},
		ignore: ['barcode']
	});
	ftAnnotations = PIMS.xtal.initFilterAnnotations({
		preset: {
			sb: 'sb_p_a',
			barcode: PIMS.xtal.barcode
		},
		sbMustMatch: {
			barcode: PIMS.xtal.barcode
		},
		ignore: ['barcode']
	});

	ftInspections.initialize();
	ftSchedules.initialize();
	ftAnnotations.initialize();

	PIMS.xtal.getElement("i_barcode").readOnly = 'readonly';
	PIMS.xtal.getElement("sh_barcode").readOnly = 'readonly';
	PIMS.xtal.getElement("a_barcode").readOnly = 'readonly';

	PIMS.xtal.addListener("i_clear", "click", function () {
		PIMS.xtal.getElement("i_barcode").value = PIMS.xtal.barcode;
	});
	PIMS.xtal.addListener("sh_clear", "click", function () {
		PIMS.xtal.getElement("sh_barcode").value = PIMS.xtal.barcode;
	});
	PIMS.xtal.addListener("a_clear", "click", function () {
		PIMS.xtal.getElement("a_barcode").value = PIMS.xtal.barcode;
	});

	PIMS.xtal.initializeHistory();
};
