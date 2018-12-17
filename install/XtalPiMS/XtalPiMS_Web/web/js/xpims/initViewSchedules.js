/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterSchedules.jspf
 */
PIMS.xtal.initFilterSchedules = function (config) {
	var filt, df, ff, ft, dl, calDS, calDE, i, applyImagers;

	applyImagers = function (type, args, obj) {
		PIMS.xtal.Imagers.applyOptions("sh_imager", args[0], df.getProperty("imager"));
		PIMS.xtal.Imagers.applyOptions("sh_temperature", args[1], df.getProperty("temperature"));
	};

	PIMS.xtal.myImagers.subscribe(applyImagers, this);

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkSchedules, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "dateToImage";
	}
	if (!filt.dir) {
		filt.dir = "asc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "schedulestable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "sh");

	ff.addListener("sh_barcode", "keyup");
	ff.addListener("sh_dateStart", "change");
	ff.addListener("sh_dateEnd", "change");
	ff.addListener("sh_imager", "change");
	ff.addListener("sh_temperature", "change");
	ff.addListener("sh_numRows", "change");

	PIMS.xtal.addListener("sh_clear", "click", function () {
		PIMS.xtal.getElement("ffSchedules").reset();
		ff.updateFilter();
	});

	ff.mapProperty("barcode", "sh_barcode");
	ff.mapProperty("startDate", "sh_dateStart");
	ff.mapProperty("endDate", "sh_dateEnd");
	ff.mapProperty("imager", "sh_imager");
	ff.mapProperty("temperature", "sh_temperature");
	ff.mapProperty("results", "sh_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "schedulesDT";
	ft.servletURL = contextPath + "/SchedulesServlet?";
	ft.countServletURL = contextPath + "/SchedulesCountServlet?";
	ft.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Schedules;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Schedules;

	calDS = new PIMS.xtal.Calendar("sh_dateStart", "sh_dateStartContainer", true, "sh_dateEnd");
	calDE = new PIMS.xtal.Calendar("sh_dateEnd", "sh_dateEndContainer", false, "sh_dateStart");

	return ft;
};

/**
 * Initialization method for ViewSchedules.jsp, if it existed
 */
PIMS.xtal.initViewSchedules = function () {
	PIMS.xtal.Imagers.init();

	var ft = PIMS.xtal.initFilterSchedules();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
