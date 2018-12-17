/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterSamples.jspf
 */
PIMS.xtal.initFilterSamples = function (config) {
	var ac, filt, df, ff, ft, dl, calCDS, calCDE, prop, i;

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			if ('group' === config.ignore[i]) {
				ac = true;
				break;
			}
		}
	}
	if (!ac) {
		ac = new PIMS.xtal.AutoComplete("s_group", "s_groupACPopup", "group");
	}

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkSamples, config);
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
	ft = new PIMS.xtal.FilteredTable(df, "samplestable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "samples");

	ff.addListener("s_name", "keyup");
	ff.addListener("s_description", "keyup");
	ff.addListener("s_owner", "keyup");
	ff.addListener("s_group", "keyup");
	ff.addListener("s_pHMin", "keyup");
	ff.addListener("s_pHMax", "keyup");
	ff.addListener("s_molecularWeightMin", "keyup");
	ff.addListener("s_molecularWeightMax", "keyup");
	ff.addListener("s_numSubUnitsMin", "keyup");
	ff.addListener("s_numSubUnitsMax", "keyup");
	ff.addListener("s_batchReference", "keyup");
	ff.addListener("s_origin", "keyup");
	ff.addListener("s_type", "keyup");
	ff.addListener("s_cellularLocation", "keyup");
	ff.addListener("s_concentrationMin", "keyup");
	ff.addListener("s_concentrationMax", "keyup");
	ff.addListener("s_createDateStart", "change");
	ff.addListener("s_createDateEnd", "change");
	ff.addListener("s_giNumber", "keyup");
	ff.addListener("s_targetName", "keyup");
	ff.addListener("s_numRows", "change");

	PIMS.xtal.addListener("s_clear", "click", function () {
		PIMS.xtal.getElement("ffSamples").reset();
		ff.updateFilter();
	});

	ff.mapProperty("name", "s_name");
	ff.mapProperty("description", "s_description");
	ff.mapProperty("owner", "s_owner");
	ff.mapProperty("group", "s_group");
	ff.mapProperty("minPH", "s_pHMin");
	ff.mapProperty("maxPH", "s_pHMax");
	ff.mapProperty("minMolecularWeight", "s_molecularWeightMin");
	ff.mapProperty("maxMolecularWeight", "s_molecularWeightMax");
	ff.mapProperty("minNumSubUnits", "s_numSubUnitsMin");
	ff.mapProperty("maxNumSubUnits", "s_numSubUnitsMax");
	ff.mapProperty("batchReference", "s_batchReference");
	ff.mapProperty("origin", "s_origin");
	ff.mapProperty("type", "s_type");
	ff.mapProperty("cellularLocation", "s_cellularLocation");
	ff.mapProperty("minConcentration", "s_concentrationMin");
	ff.mapProperty("maxConcentration", "s_concentrationMax");
	ff.mapProperty("startDate", "s_createDateStart");
	ff.mapProperty("endDate", "s_createDateEnd");
	ff.mapProperty("giNumber", "s_giNumber");
	ff.mapProperty("constructName", "s_constructName");
	ff.mapProperty("targetName", "s_targetName");
	ff.mapProperty("results", "s_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "samplesDT";
	ft.servletURL = contextPath + "/SamplesServlet?";
	ft.countServletURL = contextPath + "/SamplesCountServlet?";
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewSample;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Samples;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Samples;

	calCDS = new PIMS.xtal.Calendar("s_createDateStart", "s_createDateStartContainer", true, "s_createDateEnd");
	calCDE = new PIMS.xtal.Calendar("s_createDateEnd", "s_createDateEndContainer", false, "s_createDateStart");

	return ft;
};

/**
 * Initialization method for ViewSamples.jsp
 */
PIMS.xtal.initViewSamples = function () {
	var ft = PIMS.xtal.initFilterSamples();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
