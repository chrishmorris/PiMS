/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterConditions.jspf
 */
PIMS.xtal.initFilterConditions = function (config) {
	var filt, df, ff, ft, dl, url, processScreens, processManufacturers, i;

	// Process list of screens
	processScreens = function (transport) {
		var reply, el, val, i;
		reply = PIMS.xtal.parseJson(transport.responseText);
		el = PIMS.xtal.getElement("c_localName");
		val = df.getProperty("localName");
		if (reply.records.length > 0) {
			el.options[0] = new Option('All', 'all');
			for (i = 0; i < reply.records.length; i++) {
				el.options[i + 1] = new Option(reply.records[i].name, reply.records[i].name);
				if (val && (val === reply.records[i].name)) {
					el.value = reply.records[i].name;
				}
			}
		}
		if (PIMS.xtal.localName) {
			el.value = PIMS.xtal.localName;
			el.disabled = 'disabled';
		}
	};

	// Process list of screen manufacturers
	processManufacturers = function (transport) {
		var reply, el, val, i;
		reply = PIMS.xtal.parseJson(transport.responseText);
		el = PIMS.xtal.getElement("c_manufacturerName");
		val = df.getProperty("manufacturerName");
		for (i = 0; i < reply.manufacturers.length; i++) {
			el.options[i + 1] = new Option(reply.manufacturers[i], reply.manufacturers[i]);
			if (val && (val === reply.manufacturers[i])) {
				el.value = reply.manufacturers[i];
			}
		}
	};

	url = contextPath + "/ScreensServlet?results=-1&sort=name&dir=asc";
	PIMS.xtal.asyncRequest('GET', url, {success: processScreens, scope: this}, "");

	url = contextPath + "/ListScreenManufacturers?";
	PIMS.xtal.asyncRequest('GET', url, {success: processManufacturers, scope: this}, ""); 

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkConditions, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "localName";
	}
	if (!filt.dir) {
		filt.dir = "asc";
	}
	filt.results = -1;

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "conditionstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "c");

	ff.addListener("c_localName", "change");
	ff.addListener("c_manufacturerName", "change");
	ff.addListener("c_localNumber", "keyup");
	ff.addListener("c_manufacturerScreenName", "keyup");
	ff.addListener("c_pH", "keyup");
	ff.addListener("c_volatileCondition", "change");
	ff.addListener("c_saltCondition", "change");
	ff.addListener("c_componentName", "keyup");
	//ff.addListener("numRows", "change");

	PIMS.xtal.addListener("c_clear", "click", function () {
		PIMS.xtal.getElement("ffConditions").reset();
		ff.updateFilter();
	});

	ff.mapProperty("localName", "c_localName");
	ff.mapProperty("manufacturerName", "c_manufacturerName");
	ff.mapProperty("localNumber", "c_localNumber");
	ff.mapProperty("manufacturerScreenName", "c_manufacturerScreenName");
	ff.mapProperty("pH", "c_pH");
	ff.mapProperty("volatileCondition", "c_volatileCondition");
	ff.mapProperty("saltCondition", "c_saltCondition");
	ff.mapProperty("componentName", "c_componentName");
	//ff.mapProperty("results", "c_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "conditionsDT";
	ft.servletURL = contextPath + "/ConditionsServlet?";
	ft.countServletURL = contextPath + "/ConditionsCountServlet?";
	ft.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewCondition;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Conditions;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Conditions;

	return ft;
};

/**
 * Initialization method for ViewConstructs.jsp
 */
PIMS.xtal.initViewConditions = function () {
	var ft, config;

	if (PIMS.xtal.localName) {
		config = {
			preset: {
				localName: PIMS.xtal.localName
			},
			sbMustMatch: {
				localName: PIMS.xtal.localName
			}
		};
	}

	ft = PIMS.xtal.initFilterConditions(config);
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
