/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterPlates.jspf
 */
PIMS.xtal.initFilterPlates = function (config) {
	var ac, filt, df, ff, ft, dl, calCDS, calCDE, calLIDS, calLIDE, i, applyImagers;

	applyImagers = function (type, args, obj) {
		PIMS.xtal.Imagers.applyOptions("p_imager", args[0], df.getProperty("imager"));
		PIMS.xtal.Imagers.applyOptions("p_temperature", args[1], df.getProperty("temperature"));
	};

	PIMS.xtal.myImagers.subscribe(applyImagers, this);

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			if ('group' === config.ignore[i]) {
				ac = true;
				break;
			}
		}
	}
	if (!ac) {
		ac = new PIMS.xtal.AutoComplete("p_group", "p_groupACPopup", "group");
	}

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkPlates, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "createDate";
	}
	if (!filt.dir) {
		filt.dir = "desc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "platestable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "plates");

	ff.addListener("p_status", "change");
	ff.addListener("p_imager", "change");
	ff.addListener("p_temperature", "change");
	ff.addListener("p_numberOfCrystals", "change");
	ff.addListener("p_barcode", "keyup");
	ff.addListener("p_description", "keyup");
	ff.addListener("p_owner", "keyup");
	ff.addListener("p_group", "keyup");
	ff.addListener("p_constructName", "keyup");
	ff.addListener("p_sampleName", "keyup");
	ff.addListener("p_numRows", "change");
	ff.addListener("p_createDateStart", "change");
	ff.addListener("p_createDateEnd", "change");
	ff.addListener("p_lastImageDateStart", "change");
	ff.addListener("p_lastImageDateEnd", "change");

	PIMS.xtal.addListener("p_clear", "click", function () {
		PIMS.xtal.getElement("ffPlates").reset();
		ff.updateFilter();
	});

	ff.mapProperty("status", "p_status");
	ff.mapProperty("imager", "p_imager");
	ff.mapProperty("temperature", "p_temperature");
	ff.mapProperty("numberOfCrystals", "p_numberOfCrystals");
	ff.mapProperty("barcode", "p_barcode");
	ff.mapProperty("description", "p_description");
	ff.mapProperty("owner", "p_owner");
	ff.mapProperty("group", "p_group");
	ff.mapProperty("constructName", "p_constructName");
	ff.mapProperty("sampleName", "p_sampleName");
	ff.mapProperty("createDateStart", "p_createDateStart");
	ff.mapProperty("createDateEnd", "p_createDateEnd");
	ff.mapProperty("lastImageDateStart", "p_lastImageDateStart");
	ff.mapProperty("lastImageDateEnd", "p_lastImageDateEnd");
	ff.mapProperty("results", "p_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "platesDT";
	ft.servletURL = contextPath + "/PlatesServlet?";
	ft.countServletURL = contextPath + "/PlatesCountServlet?";
	ft.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Plates;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Plates;

	calCDS = new PIMS.xtal.Calendar("p_createDateStart", "p_createDateStartContainer", true, "p_createDateEnd");
	calCDE = new PIMS.xtal.Calendar("p_createDateEnd", "p_createDateEndContainer", false, "p_createDateStart");
	calLIDS = new PIMS.xtal.Calendar("p_lastImageDateStart", "p_lastImageDateStartContainer", true, "p_lastImageDateEnd");
	calLIDE = new PIMS.xtal.Calendar("p_lastImageDateEnd", "p_lastImageDateEndContainer", false, "p_lastImageDateStart");

	return ft;
};

/**
 * Initialization method for ViewPlates.jsp
 */
PIMS.xtal.initViewPlates = function () {
	PIMS.xtal.Imagers.init();

	var ft = PIMS.xtal.initFilterPlates();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
