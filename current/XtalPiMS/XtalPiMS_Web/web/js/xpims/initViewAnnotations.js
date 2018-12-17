/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterAnnotations.jspf
 */
PIMS.xtal.initFilterAnnotations = function (config) {
	var filt, df, ff, ft, dl, calDS, calDE, i;

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkAnnotations, config);
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
	ft = new PIMS.xtal.FilteredTable(df, "annotationstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "a");

	ff.addListener("a_barcode", "change");
	ff.addListener("a_dateStart", "change");
	ff.addListener("a_dateEnd", "change");
	ff.addListener("a_description", "keyup");
	ff.addListener("a_name", "keyup");
	ff.addListener("a_numRows", "change");
	ff.addListener("a_well", "change");

	PIMS.xtal.addListener("a_clear", "click", function () {
		PIMS.xtal.getElement("ffAnnotations").reset();
		ff.updateFilter();
	});

	ff.mapProperty("barcode", "a_barcode");
	ff.mapProperty("well", "a_well");
	ff.mapProperty("startDate", "a_dateStart");
	ff.mapProperty("endDate", "a_dateEnd");
	ff.mapProperty("description", "a_description");
	ff.mapProperty("name", "a_name");
	ff.mapProperty("results", "a_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "annotationsDT";
	ft.servletURL = contextPath + "/AnnotationsServlet?";
	ft.countServletURL = contextPath + "/AnnotationsCountServlet?";
	ft.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode; //TODO or well?
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Annotations;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Annotations;

	calDS = new PIMS.xtal.Calendar("a_dateStart", "a_dateStartContainer", true, "a_dateEnd");
	calDE = new PIMS.xtal.Calendar("a_dateEnd", "a_dateEndContainer", false, "a_dateStart");

	return ft;
};
 
/**
 * Initialization method for ViewInspections.jsp
 */
PIMS.xtal.initViewAnnotations = function () {
	var ft = PIMS.xtal.initFilterAnnotations();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
