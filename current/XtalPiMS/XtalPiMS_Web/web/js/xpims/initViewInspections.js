/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterInspections.jspf
 */
PIMS.xtal.initFilterInspections = function (config) {
	var filt, df, ff, ft, dl, calIDS, calIDE, i, applyImagers;

	applyImagers = function (type, args, obj) {
		PIMS.xtal.Imagers.applyOptions("i_imager", args[0], df.getProperty("imager"));
		PIMS.xtal.Imagers.applyOptions("i_temperature", args[1], df.getProperty("temperature"));
	};

	PIMS.xtal.myImagers.subscribe(applyImagers, this);

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkInspections, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "date";
	}
	if (!filt.dir) {
		filt.dir = "desc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "inspectionstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "i");

	ff.addListener("i_barcode", "keyup");
	ff.addListener("i_inspectionDateStart", "change");
	ff.addListener("i_inspectionDateEnd", "change");
	ff.addListener("i_imager", "change");
	ff.addListener("i_temperature", "change");
	ff.addListener("i_numRows", "change");

	PIMS.xtal.addListener("i_clear", "click", function () {
		PIMS.xtal.getElement("ffInspections").reset();
		ff.updateFilter();
	});

	ff.mapProperty("barcode", "i_barcode");
	ff.mapProperty("inspectionStartDate", "i_inspectionDateStart");
	ff.mapProperty("inspectionEndDate", "i_inspectionDateEnd");
	ff.mapProperty("imager", "i_imager");
	ff.mapProperty("temperature", "i_temperature");
	ff.mapProperty("results", "i_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "inspectionsDT";
	ft.servletURL = contextPath + "/InspectionsServlet?";
	ft.countServletURL = contextPath + "/InspectionsCountServlet?";
	ft.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Inspections;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Inspections;

	calIDS = new PIMS.xtal.Calendar("i_inspectionDateStart", "i_inspectionDateStartContainer", true, "i_inspectionDateEnd");
	calIDE = new PIMS.xtal.Calendar("i_inspectionDateEnd", "i_inspectionDateEndContainer", false, "i_inspectionDateStart");

	return ft;
};

/**
 * Initialization method for ViewInspections.jsp
 */
PIMS.xtal.initViewInspections = function () {
	PIMS.xtal.Imagers.init();
	var ft = PIMS.xtal.initFilterInspections();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
