/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/**
 * Initialization method for filterScreens.jspf
 */
PIMS.xtal.initFilterScreens = function (config) {
	var url, processManufacturers, processTypes, filt, df, ff, ft, dl, i;

	processManufacturers = function (transport) {
		var reply, el, val, i;
		reply = PIMS.xtal.parseJson(transport.responseText);
		el = PIMS.xtal.getElement("sc_manufacturerName");
		val = df.getProperty("manufacturerName");
		for (i = 0; i < reply.manufacturers.length; i++) {
			el.options[i + 1] = new Option(reply.manufacturers[i], reply.manufacturers[i]);
			if (val && (val === reply.manufacturers[i])) {
				el.value = reply.manufacturers[i];
			}
		}
	};

	processTypes = function (transport) {
		var reply, el, val, i;
		reply = PIMS.xtal.parseJson(transport.responseText);
		el = PIMS.xtal.getElement("sc_type");
		val = df.getProperty("screenType");
		for (i = 0; i < reply.screenTypes.length; i++) {
			el.options[i + 1] = new Option(reply.screenTypes[i], reply.screenTypes[i]);
			if (val && (val === reply.screenTypes[i])) {
				el.value = reply.screenTypes[i];
			}
		}
	};

	url = contextPath + "/ListScreenManufacturers?";
	PIMS.xtal.asyncRequest('GET', url, {success: processManufacturers, scope: this}, ""); 

	url = contextPath + "/ListScreenTypes?";
	PIMS.xtal.asyncRequest('GET', url, {success: processTypes, scope: this}, ""); 

	filt = PIMS.xtal.parseBookmark(PIMS.xtal.sessionBookmarkScreens, config);
	if (!filt) {
		filt = {results: 10};
	}
	if (!filt.sort) {
		filt.sort = "name";
	}
	if (!filt.dir) {
		filt.dir = "asc";
	}

	df = new PIMS.xtal.DataFilter(filt);
	if (config && config.preset) {
		df.addProperties(config.preset);
	}

	ff = new PIMS.xtal.FilterForm(df);
	ft = new PIMS.xtal.FilteredTable(df, "screenstable");
	dl = new PIMS.xtal.DataLoaderHistoryImpl(df, ft, "sc");

	ff.addListener("sc_name", "keyup");
	ff.addListener("sc_manufacturerName", "change");
	ff.addListener("sc_type", "change");
	ff.addListener("sc_numRows", "change");

	PIMS.xtal.addListener("sc_clear", "click", function () {
		PIMS.xtal.getElement("ffScreens").reset();
		ff.updateFilter();
	});

	ff.mapProperty("name", "sc_name");
	ff.mapProperty("manufacturerName", "sc_manufacturerName");
	ff.mapProperty("screenType", "sc_type");
	ff.mapProperty("results", "sc_numRows");

	if (config && config.ignore) {
		for (i = 0; i < config.ignore.length; i++) {
			ff.unmapProperty(config.ignore[i]);
		}
	}

	ff.apply(df.getProperties());

	ft.className = "screensDT";
	ft.servletURL = contextPath + "/ScreensServlet?";
	ft.countServletURL = contextPath + "/ScreensCountServlet?";
	ft.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewConditions;
	ft.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Screens;
	ft.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Screens;

	return ft;
};

/**
 * Initialization method for ViewScreens.jsp
 */
PIMS.xtal.initViewScreens = function () {
	var ft = PIMS.xtal.initFilterScreens();
	ft.initialize();
	PIMS.xtal.initializeHistory();
};
