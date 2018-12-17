/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterGroups.jspf
 */
PIMS.xtal.initFilterGroups = function (config) {
	var filt, df, ff, ft, dl, i;

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkGroups, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "groupName";
	}
	if (!filt.dir) {
		filt.dir = "asc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "groupstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "g");

	ff.addListener("g_name", "keyup");
	ff.addListener("g_organisation", "keyup");
	ff.addListener("g_numRows", "change");

	PIMS.xtal.addListener("g_clear", "click", function () {
		PIMS.xtal.getElement("ffGroups").reset();
		ff.updateFilter();
	});

	ff.mapProperty("groupName", "g_name");
	ff.mapProperty("organisation", "g_organisation");
	ff.mapProperty("results", "g_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "groupsDT";
	ft.servletURL = contextPath + "/GroupsServlet?";
	ft.countServletURL = contextPath + "/GroupsCountServlet?";
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewGroup;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Groups;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Groups;

	return ft;
};

/**
 * Initialization method for ViewGroups.jsp
 */
PIMS.xtal.initViewGroups = function () {
	var ft = PIMS.xtal.initFilterGroups();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
