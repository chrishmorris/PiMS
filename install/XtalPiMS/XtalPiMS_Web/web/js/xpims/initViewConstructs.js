/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterConstructs.jspf
 */
PIMS.xtal.initFilterConstructs = function (config) {
	var ac, filt, df, ff, ft, dl, i;

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			if ('group' === config.ignore[i]) {
				ac = true;
				break;
			}
		}
	}
	if (!ac) {
		ac = new PIMS.xtal.AutoComplete("ct_group", "ct_groupACPopup", "group");
	}

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkConstructs, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "constructName";
	}
	if (!filt.dir) {
		filt.dir = "asc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "constructstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "ct");

	ff.addListener("ct_name", "keyup");
	ff.addListener("ct_description", "keyup");
	ff.addListener("ct_owner", "keyup");
	ff.addListener("ct_group", "keyup");
	ff.addListener("ct_numRows", "change");

	PIMS.xtal.addListener("ct_clear", "click", function () {
		PIMS.xtal.getElement("ffConstructs").reset();
		ff.updateFilter();
	});

	ff.mapProperty("constructName", "ct_name");
	ff.mapProperty("description", "ct_description");
	ff.mapProperty("owner", "ct_owner");
	ff.mapProperty("group", "ct_group");
	ff.mapProperty("results", "ct_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "constructsDT";
	ft.servletURL = contextPath + "/ConstructsServlet?";
	ft.countServletURL = contextPath + "/ConstructsCountServlet?";
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewConstruct;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Constructs;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Constructs;

	return ft;
};

/**
 * Initialization method for ViewConstructs.jsp
 */
PIMS.xtal.initViewConstructs = function () {
	var ft = PIMS.xtal.initFilterConstructs();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
