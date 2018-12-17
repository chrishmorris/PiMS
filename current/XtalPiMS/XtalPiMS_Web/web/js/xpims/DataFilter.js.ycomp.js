/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global YAHOO, PIMS, contextPath */

/*
 * TODO Refactor into component parts and setup automatic concatenation
 * and yui-compression.
 * TODO Add more standard FilteredTable functions.
 * TODO Add more page initialization functions.
 */

/**
 * Build PIMS.xtal namespace.
 */
if ((typeof PIMS === "undefined") || (!PIMS)) {
	var PIMS = {};
}
if (!PIMS.xtal) {
	PIMS.xtal = {};
}
// Delegates for YUI functionality
PIMS.xtal.onDOMReady = function(f) {YAHOO.util.Event.onDOMReady(f)};
PIMS.xtal.asyncRequest = function(method, url, callback, string) {
	return YAHOO.util.Connect.asyncRequest(method, url, callback, string); 
}
PIMS.xtal.getElement = function(id) {
  return YAHOO.util.Dom.get(id);
}
PIMS.xtal.addListener = function(name, click, f) {
    YAHOO.util.Event.addListener(name, click, f); 
}
PIMS.xtal.stopEvent = function(e) {
    YAHOO.util.Event.stopEvent(e);
}
PIMS.xtal.parseJson = function(json) {
    return YAHOO.lang.JSON.parse(json);
}
// see http://developer.yahoo.com/yui/history/
PIMS.xtal.initializeHistory = function() {
    YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe");
}
/**
 * DataFilter wraps a hash of filter property/value pairs and
 * provides various methods to manipulate the filter and a custom
 * event to provide notification of changes.
 * 
 * @author Jon Diprose
 * @param filter - a filter hash to use as the initial state of the filter
 */
PIMS.xtal.DataFilter = function (filter) {
	var that, myFilter;

	/**
	 * Fire the onFilterChanged event.
	 * 
	 * Private.
	 * 
	 * @param src - the object initiating the change.
	 */
	function fireOnFilterChanged(src) {
		var clone, queryString;
		if (that.active) {
			clone = that.cloneFilter(myFilter);
			queryString = that.toQueryString(clone);
			that.onFilterChanged.fire(clone, queryString, src);
		}
	}

	/**
	 * Get a property.
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @param prop - the property whose value is to be returned
	 * @param def - default value to return if prop has no value
	 * @return The value of prop, or def if prop has no value
	 */
	this.getProperty = function (prop, def) {
		if (myFilter[prop]) {
			return myFilter[prop];
		}
		return def;
	};

	/**
	 * Set a property.
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @param prop - the property to set
	 * @param value - the value to set
	 * @param src - the object initiating the change
	 */
	this.setProperty = function (prop, value, src) {
		myFilter[prop] = value;
		fireOnFilterChanged(src);
	};

	/**
	 * Get all the properties as an object.
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @return A copy of the filter hash
	 */
	this.getProperties = function () {
		return this.cloneFilter(myFilter);
	};

	/**
	 * Set all the properties from a filter hash
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @param filter - the filter hash to set
	 * @param src - the object initiating the change
	 */
	this.setProperties = function (filter, src) {
		myFilter = this.cloneFilter(filter);
		fireOnFilterChanged(src);
	};

	/**
	 * Add all the properties from a filter hash
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @param filter - the filter hash to add
	 * @param src - the object initiating the change
	 */
	this.addProperties = function (filter, src) {
		var prop;
		for (prop in filter) {
			if (filter.hasOwnProperty(prop)) {
				myFilter[prop] = filter[prop];
			}
		}
		fireOnFilterChanged(src);
	};

	/**
	 * Get all the properties as a string. The string is url-encoded
	 * name=value pairs separated by &, suitable for use as a query
	 * string in an HTTP request.
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @return The filter as a string
	 */
	this.getPropertiesAsString = function () {
		return this.toQueryString(myFilter);
	};

	/**
	 * Set all the properties from a string. The string must be in
	 * the same format as returned by this.getPropertiesAsString.
	 * 
	 * Privileged to allow access to myFilter.
	 * 
	 * @param queryString - the queryString to parse
	 * @param src - the object initiating the change
	 */
	this.setPropertiesFromString = function (queryString, src) {
		myFilter = this.fromQueryString(queryString);
		fireOnFilterChanged(src);
	};

	that = this;

	if (filter) {
		myFilter = this.cloneFilter(filter);
	}
	else {
		myFilter = {results: 10, startIndex: 0};
	}

	this.active = false;
	this.onFilterChanged = new YAHOO.util.CustomEvent("filterChanged", this);

};

/**
 * Convert a queryString into a filter hash.
 * 
 * @param queryString - the query string, ie url-encoded name=value pairs separated by &
 * @return The filter hash
 */
PIMS.xtal.DataFilter.prototype.fromQueryString = function (queryString) {
	var keyValuePairs, i, keyValue, filter;
	filter = {};
	keyValuePairs = queryString.split("&");
	for (i = 0; i < keyValuePairs.length; i++) {
		keyValue = keyValuePairs[i].split("=");
		filter[decodeURIComponent(keyValue[0])] = decodeURIComponent(keyValue[1]);
	}
	return filter;
};

/**
 * Convert a filter hash into a queryString.
 * 
 * @param filter - the filter hash
 * @return The query string, ie url-encoded name=value pairs separated by &
 */
PIMS.xtal.DataFilter.prototype.toQueryString = function (filter) {
	var prop, propList, queryString, i;
	propList = [];
	for (prop in filter) {
		if (filter.hasOwnProperty(prop) && (filter[prop] || 0 === filter[prop])) {
			propList.push(prop);
		}
	}
	propList.sort();
	queryString = [];
	for (i = 0; i < propList.length; i++) {
		queryString.push(encodeURIComponent(propList[i]) +
				"=" +
				encodeURIComponent(filter[propList[i]]));
	}
	return queryString.join("&");
};

/**
 * Clone a filter hash. Note that this is a clone of the wrapped
 * hash, not a clone of the DataFilter instance. This is primarily
 * used to prevent the wrapped filter being modified directly.
 * 
 * @param filter - the filter hash
 * @return The cloned filter hash
 */
PIMS.xtal.DataFilter.prototype.cloneFilter = function (filter) {
	var clone, prop;
	clone = {};
	for (prop in filter) {
		if (filter.hasOwnProperty(prop) && filter[prop]) {
			clone[prop] = filter[prop];
		}
	}
	return clone;
};




/**
 * FilterForm links form elements to a DataFilter instance. Changes
 * to the form cause the filter to be updated, and the form is also
 * updated when the FilterForm is notified of changes to the filter.
 * 
 * @author Jon Diprose
 */
PIMS.xtal.FilterForm = function (filter) {
	var that, myFilter, timer, propertyMap;

	/**
	 * Cancel the update delay timer, if active.
	 * 
	 * Private.
	 */
	function cancelTimer() {
		if (timer) {
			timer.cancel();
			timer = false;
		}
	}

	/**
	 * Trigger updateFilter after this.typeLag milliseconds.
	 * 
	 * Private.
	 */
	function delayUpdateFilter() {
		cancelTimer();
		timer = YAHOO.lang.later(that.typeLag, that, that.updateFilter);
	}

	/**
	 * Apply the properties to the form. Works with INPUT and SELECT
	 * elements - untested with others.
	 * 
	 * Privileged to allow access to propertyMap.
	 */
	this.apply = function (properties) {
		var prop, el, i;
		for (prop in propertyMap) {
			if (propertyMap.hasOwnProperty(prop) && propertyMap[prop]) {
				el = YAHOO.util.Dom.get(propertyMap[prop]);
				if (properties.hasOwnProperty(prop) && properties[prop]) {
					if (properties[prop] !== el.value) {
						el.value = properties[prop];
					}
				}
				else if ("INPUT" === el.tagName) {
					if ("" !== el.value) {
						el.value = "";
					}
				}
				else if ("SELECT" === el.tagName) {
					for (i = 0; i < el.options.length; i++) {
						if (el.options[i].defaultSelected) {
							if (i !== el.selectedIndex) {
								el.selectedIndex = i;
							}
							break;
						}
					}
				}
				else if (el.value) {
					el.value = undefined;
				}
			}
		}
	};

	/**
	 * Update the DataFilter. Builds a new filter hash from the elements
	 * of propertyMap. Should be OK with most form elements but may need
	 * more work for radio buttons. Also resets startIndex to 0 on the
	 * basis that its a new search.
	 * 
	 * Privileged to allow access to propertyMap.
	 */
	this.updateFilter = function () {
		var properties, prop, el;
		cancelTimer();
		properties = {};
		for (prop in propertyMap) {
			if (propertyMap.hasOwnProperty(prop) && propertyMap[prop]) {
				el = YAHOO.util.Dom.get(propertyMap[prop]);
				properties[prop] = el.value;
			}
		}
		if (myFilter.getProperty("startIndex")) {
			properties.startIndex = 0;
		}
		myFilter.addProperties(properties, this);
	};

	/**
	 * Action to take when the form is updated.
	 * 
	 * Privileged to allow call to delayUpdateFilter().
	 * 
	 * @param e - the event representing the update
	 */
	this.onFormChanged = function (e) {
		if (("keyup" === e.type) || ("keydown" === e.type)) {
			if (e.ctrlKey) {
				return;
			}
			PIMS.xtal.stopEvent(e);
			if (13 === e.keyCode) {
				this.updateFilter();
			}
			else if ((45 < e.keyCode) || (8 === e.keyCode) || (9 === e.keyCode)) {
				delayUpdateFilter();
			}
		}
		else {
			PIMS.xtal.stopEvent(e);
			this.updateFilter();
		}
	};

	/**
	 * Map a form element id to the filter property it represents.
	 * 
	 * Privileged to allow access to propertyMap.
	 * 
	 * @param prop - the filter property
	 * @param id - the id of the form element
	 */
	this.mapProperty = function (prop, id) {
		propertyMap[prop] = id;
	};

	/**
	 * Unmap a property.
	 * 
	 * Privileged to allow access to propertyMap.
	 * 
	 * @param prop - the filter property
	 */
	this.unmapProperty = function (prop, id) {
		if (propertyMap.hasOwnProperty(prop)) {
			delete propertyMap[prop];
		}
	};

	that = this;
	myFilter = filter;
	propertyMap = {};

	this.typeLag = 500;

	myFilter.onFilterChanged.subscribe(this.onFilterChanged, this, true);

};

/**
 * Action to take when notified by the DataFilter that it has changed.
 * 
 * @param type - the type of event received
 * @param args - arg[0] should be the new filter hash, args[1] the query string and args[2] the object that initiated the update
 * @param obj - the FilterForm instance to use
 */
PIMS.xtal.FilterForm.prototype.onFilterChanged = function (type, args, obj) {
	if (obj !== args[2]) {
		obj.apply(args[0]);
	}
};

/**
 * Listen for the specified type of event on the specified element,
 * on receipt of which the DataFilter is updated.
 * 
 * @param el - the element to which to listen
 * @param type - the type of event for which to listen
 */
PIMS.xtal.FilterForm.prototype.addListener = function (el, type) {
	YAHOO.util.Event.addListener(el, type, this.onFormChanged, this, true);
};



/**
 * FilteredTable manages a YAHOO DataTable populated from a
 * YAHOO DataSource based on a DataFilter.
 * 
 * @author Jon Diprose
 * @param filter - the DataFilter instance to wrap
 * @id - the id of the div in which the FilteredTable will be rendered
 */
PIMS.xtal.FilteredTable = function (filter, id) {
	var that, myFilter, myDataSource, myDataTable, oCountConn;

	/**
	 * Process the JSON returned from the count request.
	 * 
	 * Private.
	 */
	function processCount(transport) {
		var reply, nrows;
		reply = YAHOO.lang.JSON.parse(transport.responseText);
		this.totalRows = parseInt(reply.count, 10);
		nrows = this.getNumRowsAsInt();
		this.totalPages = (this.totalRows - this.totalRows % nrows) / nrows;
		if (this.totalRows % nrows > 0) {
			this.totalPages += 1;
		}

		if (this.totalRows < this.startIndex) {
			this.startIndex = 0;
			// TODO Should we update the filter? Would want a silent update!
		}
		else {
			this.startIndex = parseInt(myFilter.getProperty("startIndex", 0), 10);
		}

		this.updatePagination();
	}

	function notifyFailure(transport){
		if (-1==transport.status) {
			return; // aborted, e.g. user navigated to another page
		}
		alert("Sorry, there has been an error, code: "+transport.status);
		var w=window.open("","w");
		w.document.write(transport.responseText);
	}
	
	
	/**
	 * Get the data and data count using the specified queryString.
	 * 
	 * Privileged to allow access to myDataSource, myDataTable and
	 * reference processCount as a callback.
	 * 
	 * @param queryString - the query string to use
	 */
	this.getData = function (queryString) {
		var req;
		req = this.countServletURL + "count=true&" + queryString;
		if (oCountConn && YAHOO.util.Connect.isCallInProgress(oCountConn)) {
			YAHOO.util.Connect.abort(oCountConn);
		}
		oCountConn = YAHOO.util.Connect.asyncRequest('GET', req, {
			success : processCount,
			failure: notifyFailure,
			scope : this
		}, "");
		myDataSource.sendRequest(queryString,
					myDataTable.onDataReturnReplaceRows,
					myDataTable);
	};

	/**
	 * Sanitize and apply new startIndex to filter.
	 * 
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getPage = function () {
		if (this.startIndex < 0) {
			this.startIndex = 0;
		} else if (this.startIndex >= this.totalRows) {
			this.startIndex = this.totalRows - 1;
		}
		myFilter.setProperty("startIndex", this.startIndex);
	};

	/**
	 * Set startIndex to zero.
	 * 
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getStartPage = function (e) {
		PIMS.xtal.stopEvent(e);
		if (this.startIndex === 0) {
			return;
		}
		this.startIndex = 0;
		this.getPage();
	};

	/**
	 * Decrement startIndex by numRows.
	 * 
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getPreviousPage = function (e) {
		PIMS.xtal.stopEvent(e);
		// Already at first page
		if (this.startIndex === 0) {
			return;
		}
		this.startIndex -= this.getNumRowsAsInt();
		this.getPage();
	};

	/**
	 * Increment startIndex by numRows.
	 * 
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getNextPage = function (e) {
		var nrows, currentPage;
		PIMS.xtal.stopEvent(e);
		nrows = this.getNumRowsAsInt();
		currentPage = 1 + (this.startIndex - (this.startIndex % nrows)) / nrows;
		if (currentPage === this.totalPages) {
			return;
		}
		this.startIndex += nrows;
		this.getPage();
	};

	/**
	 * Set startIndex to first row on last page.
	 * 
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getEndPage = function (e) {
		var nrows, currentPage;
		PIMS.xtal.stopEvent(e);
		nrows = this.getNumRowsAsInt();
		currentPage = 1 + (this.startIndex - (this.startIndex % nrows)) / nrows;
		if (currentPage === this.totalPages) {
			return;
		}
		this.startIndex = (this.totalPages - 1) * nrows;
		this.getPage();
	};

	/**
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.getPageClick = function (e) {
		var el, len, pageNum, nrows, newIndex;
		PIMS.xtal.stopEvent(e);
		el = YAHOO.util.Event.getTarget(e);
		len = this.name.length + 3;
		if (this.name + "_p_" === el.id.substr(0, len)) {
			pageNum = parseInt(el.id.substr(len), 10);
			if (pageNum) {
				nrows = this.getNumRowsAsInt();
				newIndex = (pageNum - 1) * nrows;
				if (newIndex !== this.startIndex) {
					this.startIndex = newIndex;
					this.getPage();
				}
			}
		}
	};

	/**
	 * TODO Still need to figure out how much of this is necessary.
	 */
	this.changeRowCount = function (e) {
		var nrows = this.getNumRowsAsInt();
		PIMS.xtal.stopEvent(e);
		this.totalPages = (this.totalRows - this.totalRows % nrows) / nrows;
		if (this.totalRows % nrows > 0) {
			this.totalPages += 1;
		}
		this.updatePagination();
		this.getPage();
	};

	/**
	 * Get current sort order from the filter. The sort order is returned as
	 * expected by YAHOO DataTable, ie:
	 * {
	 *   key: "sort column name",
	 *   dir: YAHOO.widget.DataTable.CLASS_ASC | YAHOO.widget.DataTable.CLASS_DESC
	 * }
	 * 
	 * @return An object representing the sort order
	 */
	this.getSort = function () {
		return {
			key : myFilter.getProperty("sort"),
			dir : ("desc" === myFilter.getProperty("dir")) ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC
		};
	};

	/**
	 * Apply new sort order to the filter. The sort order must be in the same
	 * format as returned by getSort().
	 * 
	 * @param An object representing the sort order
	 */
	this.setSort = function (oSort) {
		myFilter.addProperties({
			sort : oSort.key,
			dir : (YAHOO.widget.DataTable.CLASS_DESC === oSort.dir) ? "desc" : "asc"
		}, this);
	};

	/**
	 * Initialize the FilteredTable.
	 */
	this.initialize = function () { 
		//alert("FilteredTable.initialize that: +" that+" myFilter: "+myFilter+" myDataSource: " myDataSource+" myDataTable: "+ myDataTable+" oCountConn: "+oCountConn);
		var qs, initialConfig, i;

		qs = myFilter.getPropertiesAsString();
		this.startIndex = parseInt(myFilter.getProperty("startIndex", 0), 10);
		this.renderTable();

		initialConfig = {
				dateOptions : {
					format : "%d/%m/%Y %H:%M",
					locale : "en-GB"
				},
				initialLoad : false,
				selectionMode : "single",
				sortedBy : this.getSort()
			};

		// Instantiate DataSource
		myDataSource = new YAHOO.util.DataSource(this.servletURL);
		myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
		myDataSource.responseSchema = this.responseSchema;
		myDataSource.connXhrMode = "ignoreStaleResponses";
		myDataSource.dataTable = this;

		// Instantiate DataTable
		myDataTable = new YAHOO.widget.DataTable(this.name + "_table", this.myColumnDefs, myDataSource, initialConfig);
		myDataTable.showTableMessage("<img src='xtal/images/ajax-loader.gif' height='24' alt='Loading...' />");
		myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
		myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
		if (this.rowClickEvent) {
			myDataTable.subscribe("rowClickEvent", this.rowClickEvent);
		}
		if (this.onRefreshEvent) {
			myDataTable.subscribe("refreshEvent", this.onRefreshEvent);
		}
		for (i in this.subscriptions) {
			if (this.subscriptions.hasOwnProperty(i) && this.subscriptions[i]) {
				myDataTable.subscribe(i, this.subscriptions[i]);
			}
		}

		/**
		 * Custom sorting.
		 * 
		 * Executes in context of myDataTable?
		 */
		myDataTable.doBeforeSortColumn = function (oColumn, sSortDir) {
			var oSelf;
			oSelf = this.getDataSource().dataTable;
			oSelf.setSort({key: oColumn.key, dir: sSortDir});
			return false;
		};

		/**
		 * Custom code to parse the raw server data for Paginator values and page
		 * links and sort UI.
		 * TODO this seems to be called repeatedly
		 * resulting in many messages "Sorry, there has been an error"
		 * Executes in context of myDataSource?
		 */
		myDataSource.doBeforeCallback = function (oRequest, oRawResponse, oParsedResponse) {
			var oSelf, oSort;

			// Update the DataTable's sortedBy with new values
			oSelf = this.dataTable;
			oSort = oSelf.getSort();
			myDataTable.set("sortedBy", oSort);

			// Dig out any metadata
			if (oParsedResponse.meta.recentBarcode) {
				PIMS.xtal.recentBarcode = oParsedResponse.meta.recentBarcode;
			}

			// Let the DataSource parse the rest of the response
			return oParsedResponse;
		};
		this.getData(qs);

	};

	that = this;
	myFilter = filter;

	//variables used in this class...
	this.name = id;
	this.className = "";
	this.myColumnDefs = undefined;
	this.responseSchema = undefined;
	this.servletURL = "";
	this.countServletURL = "";

	this.startIndex = 0;
	this.totalRows = 0;
	this.totalPages = 0;
	this.subscriptions = undefined;
	this.rowClickEvent = "";

	this.numRows = parseInt(myFilter.getProperty('results', 10), 10);
	this.numPageLinksToShow = 4;

	myFilter.onFilterChanged.subscribe(this.onFilterChanged, this, true);

};

/**
 * Action to take when notified by the DataFilter that it has changed.
 * 
 * @param type - the type of event received
 * @param args - arg[0] should be the new filter hash, args[1] the query string and args[2] the object that initiated the update
 * @param obj - the FilterForm instance to use
 */
PIMS.xtal.FilteredTable.prototype.onFilterChanged = function (type, args, obj) {
	if (obj !== args[2]) {
		if (args[0].hasOwnProperty("results") && args[0].results) {
			this.numRows = args[0].results;
		}
	}
};

/**
 * Ensure we get an integer value for numRows.
 * 
 * @return numRows as an integer
 */
PIMS.xtal.FilteredTable.prototype.getNumRowsAsInt = function () {
	return parseInt(this.numRows, 10);
};

/**
 * Add the pagination elements to the page.
 */
PIMS.xtal.FilteredTable.prototype.renderTable = function () {
	var txt = [
		"<div id='", this.name, "_dt-pag-nav' class='dt-pag-nav'>",
		"<span id='", this.name, "_startLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='", contextPath, "/xtal/images/16x16/player_start.png' alt='|<' /></span>",
		"<span id='", this.name, "_prevLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='", contextPath, "/xtal/images/16x16/previous.png' alt='<' /></span>",
		"<span id='", this.name, "_pagesLinks'></span>",
		"<span id='", this.name, "_nextLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='", contextPath, "/xtal/images/16x16/next.png' alt='>' /></span>",
		"<span id='", this.name, "_endLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='", contextPath, "/xtal/images/16x16/player_end.png' alt='>|' /></span>",
		"<\/div>",
		"<div id='", this.name, "_table'></div>"
	];
	YAHOO.util.Dom.get(this.name).innerHTML = txt.join("");
	YAHOO.util.Event.addListener(YAHOO.util.Dom.get(this.name + "_prevLink"), "click", this.getPreviousPage, this, true);
	YAHOO.util.Event.addListener(YAHOO.util.Dom.get(this.name + "_nextLink"), "click", this.getNextPage, this, true);
	YAHOO.util.Event.addListener(YAHOO.util.Dom.get(this.name + "_pagesLinks"), "click", this.getPageClick, this, this);
	YAHOO.util.Event.addListener(YAHOO.util.Dom.get(this.name + "_startLink"), "click", this.getStartPage, this, true);
	YAHOO.util.Event.addListener(YAHOO.util.Dom.get(this.name + "_endLink"), "click", this.getEndPage, this, true);
};

/**
 * Update the pagination elements on the page.
 */
PIMS.xtal.FilteredTable.prototype.updatePagination = function () {
	var pageNumber, txt, i, nrows;
	nrows = this.getNumRowsAsInt();
	if ((this.totalPages <= 1) || (nrows < 0)) {
		//don't show pagination!
		YAHOO.util.Dom.get(this.name + "_dt-pag-nav").style.display = 'none';
	} else {
		pageNumber = 1 + (this.startIndex - (this.startIndex % nrows)) / nrows;
		if (pageNumber > 1) {
			YAHOO.util.Dom.setStyle([ this.name + '_startLink', this.name + '_prevLink' ], 'opacity', 1.0);
			YAHOO.util.Dom.setStyle([ this.name + '_startLink', this.name + '_prevLink' ], 'cursor', 'pointer');
		} else {
			YAHOO.util.Dom.setStyle([ this.name + '_startLink', this.name + '_prevLink' ], 'opacity', 0.5);
			YAHOO.util.Dom.setStyle([ this.name + '_startLink', this.name + '_prevLink' ], 'cursor', 'default');
		}
		if (pageNumber === this.totalPages) {
			YAHOO.util.Dom.setStyle([ this.name + '_endLink', this.name + '_nextLink' ], 'opacity', 0.5);
			YAHOO.util.Dom.setStyle([ this.name + '_endLink', this.name + '_nextLink' ], 'cursor', 'default');
		} else {
			YAHOO.util.Dom.setStyle([ this.name + '_endLink', this.name + '_nextLink' ], 'opacity', 1.0);
			YAHOO.util.Dom.setStyle([ this.name + '_endLink', this.name + '_nextLink' ], 'cursor', 'pointer');
		}
		txt = "";
		if (pageNumber - this.numPageLinksToShow > 1) {
			txt += "<span id='" + this.name + "_p_1' style='padding-left:4px;padding-right:4px;cursor:pointer;border:#9999FF solid 2px;width:16px;height:16px; background-color:#CCCCFF;'>1<\/span>...";
		}
		for (i = pageNumber - this.numPageLinksToShow; i <= pageNumber + this.numPageLinksToShow; i++) {
			if ((i > 0) && (i <= this.totalPages)) {
				if (i === pageNumber) {
					txt += "<span id='" + this.name + "_p_" +
							i +
							"' style='padding-left:4px;padding-right:4px;width:16px;height:16px;border:#9999FF solid 2px; background-color:#9999FF;'>" +
							i + "<\/span>";
				} else {
					txt += "<span id='" + this.name + "_p_" +
							i +
							"' style='padding-left:4px;padding-right:4px;cursor:pointer;width:16px;height:16px;border:#9999FF solid 2px; background-color:#CCCCFF;'>" +
							i + "<\/span>";
				}
			}
		}
		if (i <= this.totalPages) {
			txt += "...<span id='" + this.name + "_p_" +
					this.totalPages +
					"' style='padding-left:4px;padding-right:4px;cursor:pointer;width:16px;height:16px;border:#9999FF solid 2px; background-color:#CCCCFF;'>" +
					this.totalPages + "<\/span>";
		}
		YAHOO.util.Dom.get(this.name + "_pagesLinks").innerHTML = txt;
		YAHOO.util.Dom.get(this.name + "_dt-pag-nav").style.display = 'block';
	}
};

/**
 * Date formatted for YAHOO.widget.DataTable that handles invalid dates
 * properly.
 */
PIMS.xtal.FilteredTable.formatValidDate = function (el, oRecord, oColumn, oData) {
	var oConfig;
	if (oData && (oData instanceof Date) && (!isNaN(oData.getDate()))) {
		oConfig = oColumn.dateOptions || this.get("dateOptions");
		el.innerHTML = YAHOO.util.Date.format(oData, oConfig, oConfig.locale);
	}
	else {
		el.innerHTML = "";
	}
};

PIMS.xtal.FilteredTable.formatComponent = function (el, oRecord, oColumn, oData) {
	var txt, i;
	txt = [
		"<table class='list' style='width:100%'><tr><th style='width:50%'>Chemical</th><th>Quantity</th><th>Type</th></tr>"
	];
	for (i = 0; i < oData.length; i++) {
		txt.push(
			"<tr><td>",
			oData[i].componentName,
			"</td><td>",
			oData[i].quantity,
			"</td><td>",
			oData[i].componentType,
			"</td></tr>");
	}
	txt.push("</table>");
	el.innerHTML = txt.join('');
};

PIMS.xtal.FilteredTable.formatCheckbox = function (el, oRecord, oColumn, oData) {
	if (true === oData) {
		el.innerHTML = '<input type="checkbox" disabled="disabled" checked="checked">';
	}
	else {
		el.innerHTML = '<input type="checkbox" disabled="disabled">';
	}
};

/**
 * Make the validDate formatter available using the "validDate" formatter string,
 * etc.
 */
YAHOO.widget.DataTable.Formatter.validDate = PIMS.xtal.FilteredTable.formatValidDate;
YAHOO.widget.DataTable.Formatter.component = PIMS.xtal.FilteredTable.formatComponent;
YAHOO.widget.DataTable.Formatter.checkbox = PIMS.xtal.FilteredTable.formatCheckbox;




/**
 * DataLoaderHistoryImpl provides a YAHOO.util.History-based approach
 * to loading data into a FilteredTable based on a DataFilter, such
 * that the state of the filter is represented by a bookmarkable URL
 * at each new search.
 * 
 * @author Jon Diprose
 * @param filter - the DataFilter instance to wrap
 * @param filter - the FilteredTable instance to wrap
 * @param module - the YAHOO.util.History namespace to use
 */
PIMS.xtal.DataLoaderHistoryImpl = function (filter, table, module) {
	var myFilter, myTable, myModule, bm;

	/**
	 * Return the best guess of the initial history state.
	 * 
	 * Private.
	 * 
	 * @return The initial history state
	 */
	function initialState() {
		return myFilter.getPropertiesAsString();
	}

	/**
	 * Navigate to a new history state. Note that any updates
	 * arising from the change of state should be done in the
	 * this.onHistoryChanged method rather than here. This gives
	 * consistent behaviour with browser back/forward actions
	 * as well as filter updates.
	 * 
	 * Privileged to allow access to myModule.
	 * 
	 * @param queryString - the new history state
	 */
	this.navigate = function (queryString) {
		YAHOO.util.History.navigate(myModule, queryString);
	};

	/**
	 * Set the filter and load the data for the specified state.
	 * 
	 * Privileged to allow access to myFilter and myTable.
	 */
	this.onHistoryChanged = function (state) {
		myFilter.setPropertiesFromString(state, this);
		myTable.getData(state);
	};

	myFilter = filter;
	myTable = table;
	myModule = module;

	myFilter.onFilterChanged.subscribe(this.onFilterChanged, this, true);

	YAHOO.util.History.register(myModule, initialState(), this.onHistoryChanged, this, true);

	bm = YAHOO.util.History.getBookmarkedState(myModule);
	if (bm) {
		myFilter.setPropertiesFromString(bm);
	}
	myFilter.active = true;

};

/**
 * Action to take when the filter is updated. Note that any
 * updates arising from the change of state should be done in
 * the this.onHistoryChanged method rather than here. This gives
 * consistent behaviour with browser back/forward actions as well
 * as filter updates.
 */
PIMS.xtal.DataLoaderHistoryImpl.prototype.onFilterChanged = function (name, args, obj) {
	if (obj !== args[2]) {
		obj.navigate(args[1]);
	}
};



/**
 * Class to manage loading of imager and temperature data
 */
PIMS.xtal.Imagers = function () {
	var that, processImagers, onLoad;

	/**
	 * Process the return from the request initiated by getImagerInfo().
	 * This is a list of imager-temperature pairs which are converted
	 * to make a list of imagers and list of temperatures and stored in
	 * this.imagers and this.temperatures.
	 */
	processImagers = function (transport) {
		var reply, myImagers, myTemperatures, t, ts, i;
		reply = YAHOO.lang.JSON.parse(transport.responseText);

		ts = {};
		myImagers = [new Option("All", "all")];
		myTemperatures = [new Option("All", "all")];
		for (i = 0; i < reply.imagers.length; i++) {
			myImagers[i + 1] = new Option(
					reply.imagers[i].name + " (" + reply.imagers[i].temperature +
							"\u00B0C)", reply.imagers[i].name);
			t = "" + reply.imagers[i].temperature;
			ts[t] = 1;
		}

		i = 1;
		for (t in ts) {
			if (ts.hasOwnProperty(t) && (1 === ts[t])) {
				myTemperatures[i] = new Option(t + "\u00B0C", t);
				i++;
			}
		}

		that.imagers = myImagers;
		that.temperatures = myTemperatures;
		that.state = 2;
		onLoad.fire(that.imagers, that.temperatures);
	};

	/**
	 * Get the list of imagers.
	 */
	this.getImagerInfo = function () {
		var url = contextPath + "/ListImagers?";
		if (0 === this.state) {
			this.state = 1;
			YAHOO.util.Connect.asyncRequest('GET', url, {
				success : processImagers,
				scope : this
			}, "");
		}
	};

	/**
	 * Subscribe to this objects load event.
	 */
	this.subscribe = function (callback, obj) {
		if (2 === this.state) {
			callback("load", [this.imagers, this.temperatures], obj);
		}
		else {
			onLoad.subscribe(callback, obj);
		}
	};

	that = this;
	onLoad = new YAHOO.util.CustomEvent("load", this);

	this.state = 0;
	this.imagers = [];
	this.temperatures = [];
};

/**
 * Utility method to set the options on a select element, not
 * specifically for PIMS.xtal.Imagers-related activity.
 */
PIMS.xtal.Imagers.applyOptions = function (id, opts, val) {
	var el, i;
	el = YAHOO.util.Dom.get(id);
	el.options.length = 1;
	for (i = 0; i < opts.length; i++) {
		el.options[i] = new Option(opts[i].text, opts[i].value);
	}
	if (val) {
		el.value = val;
	}
};

/**
 * Instantiate a PIMS.xtal.Imagers object, store it in PIMS.xtal.myImagers
 * and call getImagerInfo() on it.
 */
PIMS.xtal.Imagers.init = function () {
	PIMS.xtal.myImagers = new PIMS.xtal.Imagers();
	PIMS.xtal.myImagers.getImagerInfo();
};



PIMS.xtal.MicroImages = function () {
};

PIMS.xtal.MicroImages.displayMicroImages = function (transport) {
	var microData, txt, i;
	microData = YAHOO.lang.JSON.parse(transport.responseText);
	txt = [];
	for (i = 0; i < microData.records.length; i++) {
		txt.push(
			'<span style="float:left;padding:2px"><table class="list"><tr><td>',
			'<a href="',
			contextPath,
			'/ViewMicroscopeImages.jsp?barcode=',
			microData.records[i].barcode,
			'&well=',
			microData.records[i].well,
			'&date=',
			microData.records[i].date,
			'"><img id="microimage',
			i,
			'" src="',
			microData.records[i].url,
			'" alt="',
			microData.records[i].barcode,
			'-',
			microData.records[i].well,
			'" width="135px" /></a>',
			'</td></tr><tr><th style="width:135px">',
			microData.records[i].barcode,
			'-',
			microData.records[i].well,
			'</th></tr>',
			'</table></span>');
	}
	if (txt.length > 0) {
		YAHOO.util.Dom.get("microscope_images").innerHTML = txt.join('');
	} else {
		YAHOO.util.Dom.get("microscope_images").innerHTML = 'None currently available';
	}
};

PIMS.xtal.MicroImages.getMicroImages = function () {
	if(!document.getElementById("microscope_images")){ return false; }
	var url = contextPath + "/ImagesServlet?results=4&startIndex=0&sort=date&dir=desc&imageType=zoomed";
	YAHOO.util.Connect.asyncRequest('GET', url, {
		success: PIMS.xtal.MicroImages.displayMicroImages,
		scope: this
	}, "");
};




/**
 * AutoComplete servlet wrapper.
 * 
 * @param input - the id of the input element
 * @param container - the id of the container element
 * @param queryParam - the query param to use
 */
PIMS.xtal.AutoComplete = function (input, container, queryParam) {
	var oDS, oAC;
	
	this.getDS = function () {
		return oDS;
	};

	this.getAC = function () {
		return oAC;
	};
	
	// Use an XHRDataSource
	oDS = new YAHOO.widget.DS_XHR("AutoCompleteServlet", ["\n", "\n"]);
	// Set the responseType
	oDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	oDS.scriptQueryParam = queryParam;
	oDS.maxCacheEntries = 15;
	oDS.queryMatchContains = true;
	oDS.queryMatchSubset = true;
	// Instantiate the AutoComplete
	oAC = new YAHOO.widget.AutoComplete(input, container, oDS);
	oAC.animVert = false;
	oAC.minQueryLength = 1;
	oAC.maxResultsDisplayed = 15;
};




/**
 * Calendar wrapper. Adds clear (and close) functionality to calendar
 * widget and provides methods to get the change event fired on the
 * relevant input element.
 * 
 * @param input - the id of the input element to manage
 * @param container - the id of the container div in which the calendar will be rendered
 * @param start - set to true if this calendar is the start of a range, otherwise false
 * @param linkedInput - the id of the input element that represents the other end of the range
 */
PIMS.xtal.Calendar = function (input, container, start, linkedInput) {
	var myId, myContainer, myStart, myLinkedInput, cal;

	/**
	 * Fire the change event on the managed input element.
	 */
	function fireChange() {
		var el, evt;
		el = document.getElementById(myId);
		if (document.createEvent) {
			evt = document.createEvent("HTMLEvents");
			evt.initEvent("change", true, true);
			el.dispatchEvent(evt);
		}
		else {
			evt = document.createEventObject();
			el.fireEvent("onchange", evt);
		}
	}

	/**
	 * Show the calendar widget.
	 */
	this.show = function () {
		var xy, curr, linkedDate;
		xy = YAHOO.util.Dom.getXY(myId);
		xy[1] = xy[1] + 20;

		curr = YAHOO.util.Dom.get(myId).value;
		cal.cfg.setProperty("selected", curr);
		if (curr) {
			curr = curr.split("/");
			cal.cfg.setProperty("pagedate", curr[1] + "/" + curr[2]);
		}

		linkedDate = YAHOO.util.Dom.get(myLinkedInput).value;
		if (myStart) {
			if (linkedDate) {
				cal.cfg.setProperty("maxdate", linkedDate);
			}
			else {
				cal.cfg.setProperty("maxdate", new Date());
			}
		}
		else {
			cal.cfg.setProperty("maxdate", new Date());
			if (linkedDate) {
				cal.cfg.setProperty("mindate", linkedDate);
			}
		}
		cal.render();

		YAHOO.util.Dom.setStyle(myContainer, "display", "block");
		YAHOO.util.Dom.setXY(myContainer, xy);
	};

	/**
	 * Handle the calendar widget's selectEvent. Default action is
	 * to hide the calendar.
	 */
	this.onSelect = function (type, args, obj) {
		cal.hide();
	};

	/**
	 * Handle the calendar widget's clearEvent. Default action is
	 * to hide the calendar.
	 */
	this.onClear = function (type, args, obj) {
		cal.hide();
	};

	/**
	 * Handle the calendar widget's hideEvent. Default action is
	 * to set the input element's value appropriately and fire its
	 * change event if necessary.
	 */
	this.onHide = function (type, args, obj) {
		var el, prev, sel, date, newDate;
		el = document.getElementById(myId);
		prev = el.value;
		sel = cal.getSelectedDates();
		if (sel.length > 0) {
			date = sel[0];
			newDate = date.getDate() + "/" + (1 + date.getMonth()) + "/" + date.getFullYear();
		}
		else {
			newDate = "";
		}
		if (prev !== newDate) {
			el.value = newDate;
			fireChange();
		}
	};

	myId = input;
	myContainer = container;
	myStart = start;
	myLinkedInput = linkedInput;

	cal = new YAHOO.widget.Calendar(myContainer);
	cal.selectEvent.subscribe(this.onSelect, this, true);
	cal.clearEvent.subscribe(this.onClear, this, true);
	cal.hideEvent.subscribe(this.onHide, this, true);
	cal.cfg.setProperty("MDY_DAY_POSITION", 1);
	cal.cfg.setProperty("MDY_MONTH_POSITION", 2);
	cal.cfg.setProperty("MDY_YEAR_POSITION", 3);
	cal.cfg.setProperty("MD_DAY_POSITION", 1);
	cal.cfg.setProperty("MD_MONTH_POSITION", 2);
	cal.renderFooter = function (html) {
		html.push("<tfoot><td colspan='7'><a class='pims-cal-clear' href='#'>Clear</a> <a class='pims-cal-close' href='#'>Close</a></td></tfoot>");
		return html;
	};
	cal.domEventMap = {
			"pims-cal-clear": {
				tag: "a",
				event: "click",
				handler: function (e) {
					cal.clear();
					YAHOO.util.Event.preventDefault(e);
				},
				scope: cal,
				correct: true
			},
			"pims-cal-close": {
				tag: "a",
				event: "click",
				handler: function (e) {
					cal.hide();
					YAHOO.util.Event.preventDefault(e);
				},
				scope: this,
				correct: true
			}
		};
	cal.render();

	YAHOO.util.Event.addListener(myId + "_show", "click", this.show, this, true);
};




/**
 * Default set of event handlers for use by FilteredTable instances.
 */
PIMS.xtal.FilteredTable.handlers = {};

/**
 * Select rows with barcodes matching recentBarcode on refresh.
 */
PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode = function () {
	// Runs in context of DataTable
	var i, oRec;
	for (i = 0; i < this.getRecordSet().getLength(); i++) {
		oRec = this.getRecordSet().getRecord(i);
		if (PIMS.xtal.recentBarcode && (oRec.getData('barcode') === PIMS.xtal.recentBarcode)) {
			this.selectRow(oRec);
		}
	}
};

/**
 * Navigate to ViewConstruct.jsp?id=... on row click.
 */
PIMS.xtal.FilteredTable.handlers.rowClickViewConstruct = function (oArgs) {
	var elRecord, constructId;
	elRecord = this.getRecord(oArgs.target);
	constructId = elRecord.getData("constructId");
	if (constructId) {
		window.location.href = contextPath + "/ViewConstruct.jsp?id=" + constructId;
	}
};

/**
 * Navigate to ViewGroup.jsp?name=... on row click.
 */
PIMS.xtal.FilteredTable.handlers.rowClickViewGroup = function (oArgs) {
	var elRecord, name;
	elRecord = this.getRecord(oArgs.target);
	name = elRecord.getData("groupName");
	if (name) {
		window.location.href = contextPath + "/ViewGroup.jsp?name=" + name;
	}
};

/**
 * Navigate to ViewTrialDrops.jsp?barcode=... on row click.
 */
PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops = function (oArgs) {
	var elRecord, myBarcode, myInspectionName, myWell, url;
	elRecord = this.getRecord(oArgs.target);
	myBarcode = elRecord.getData("barcode");
	if (!myBarcode) {
		return;
	}
	myInspectionName = elRecord.getData("inspectionName");
	myWell = elRecord.getData("well");
	PIMS.xtal.recentBarcode = myBarcode;
	this.unselectAllRows();
	this.selectRow(elRecord);
	url = contextPath + "/ViewTrialDrops.jsp?barcode=" + myBarcode;
	if (myInspectionName) {
		url += "&name=" + myInspectionName;
	}
	if (myWell) {
		url += "&well=" + myWell;
	}
	window.location.href = url;
};

/**
 * Navigate to ViewSample.jsp?id=... on row click.
 */
PIMS.xtal.FilteredTable.handlers.rowClickViewSample = function (oArgs) {
	var elRecord, id;
	elRecord = this.getRecord(oArgs.target);
	id = elRecord.getData("id");
	if (id) {
		window.location.href = contextPath + "/ViewSample.jsp?id=" + id;
	}
};

/**
 * Navigate to the screen on row click.
 */
PIMS.xtal.FilteredTable.handlers.rowClickViewConditions = function (oArgs) {
	var elRecord, name;
	elRecord = this.getRecord(oArgs.target);
	name = elRecord.getData("name");
	if (name) {
		window.location.href = contextPath + "/update/EditScreen/" + name;
	}
};

/**
 * Default set of column definitions for use by FilteredTable instances.
 */
PIMS.xtal.FilteredTable.columnDefs = {};

/**
 * Annotations column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Annotations = [
	{key: "barcode", label: "Barcode", resizeable: true, sortable: true /*, width: 106 */},
	{key: "well", label: "Well", resizeable: true, sortable: true /*, width: 121 */},
	{key: "date", label: "Date", formatter: "date", resizeable: true, sortable: true /*, width: 133 */},
	{key: "description", label: "Description", resizeable: true /*, width: 200 */},
	{key: "name", label: "Annotator", resizeable: true, sortable: true /*, width: 137 */},
	{key: "version", label: "Version", resizeable: true /*, width: 61 */},
	{key: "type", label: "Type", resizeable: true, sortable: true /*, width: 80 */},
	{key: "inspectionName", label: "Inspection", sortable: true, resizeable: true /*, width: 82 */}
];

/**
 * Conditions column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Conditions = [
	{key: "localName", label: "Local Name", sortable: true, resizeable: true},
	{key: "localNumber", label: "Well Number", sortable: true, resizeable: true},
	{key: "manufacturerName", label: "Manufacturer Name", sortable: true, resizeable: true},
	{key: "manufacturerScreenName", label: "Manufacturer Screen Name", sortable: true, resizeable: true},
	{key: "manufacturerCode", label: "Code", sortable: true, resizeable: true},
	{key: "manufacturerCatCode", label: "Catalogue Code", sortable: true, resizeable: true},
	{key: "finalPH", label: "Final pH", sortable: true, resizeable: true},
	{key: "volatileCondition", label: "Volatile", formatter: "checkbox", sortable: true, resizeable: true},
	{key: "saltCondition", label: "Salt Crystals", formatter: "checkbox", sortable: true, resizeable: true},
	{key: "components", label: "Components", formatter: "component", sortable: true, resizeable: true}
];

/**
 * Constructs column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Constructs = [
	{key: "constructName", label: "Name", sortable: true, resizeable: true},
	{key: "description", label: "Description", resizeable: true},
	{key: "constructLink", label: "Details", resizeable: true},
	{key: "targetLink", label: "Target", resizeable: true},
	{key: "owner", label: "Owner", sortable: true, resizeable: true},
	{key: "group", label: "Group", sortable: true, resizeable: true}
];

/**
 * Groups column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Groups = [
	{key: "groupName", label: "Name", sortable: true, resizeable: true},
	{key: "groupHead", label: "Group Head", sortable: true, resizeable: true},
	{key: "organisation", label: "Organisation", sortable: true, resizeable: true}
];

/**
 * Plate inspections column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Inspections = [
	{key: "date", label: "Date", formatter: "date", sortable: true, resizeable: true},
	{key: "barcode", label: "Barcode", sortable: true, resizeable: true},
	{key: "imager", label: "Imaging System", sortable: true, resizeable: true},
	{key: "details", label: "Description", sortable: false, resizeable: true}
];

/**
 * Plate experiments column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Plates = [
	{key: "link", label: "", resizeable: false, sortable: false, width: 6},
	{key: "barcode", label: "Barcode", resizeable: true, sortable: true, width: 100},
	{key: "createDate", label: "Created", resizeable: true, formatter: "date", sortable: true, width: 110},
	{key: "description", label: "Description", resizeable: true, width: 200},
	{key: "owner", label: "Owner", resizeable: true, sortable: true, width: 70},
	{key: "lastImageDate", label: "Last Imaged", formatter: PIMS.xtal.FilteredTable.formatValidDate, resizeable: true, sortable: true, width: 110},
	{key: "imager", label: "Imager", resizeable: true, sortable: true, width: 100},
	{key: "status", label: "Status", resizeable: true, width: 60},
	{key: "numberOfCrystals", label: "Crystals", resizeable: true, sortable: true, width: 80},
	{key: "sampleName", label: "Sample", sortable: true, resizeable: true, width: 80},
	{key: "constructName", label: "Construct", sortable: true, resizeable: true, width: 80}
];

/**
 * Samples column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Samples = [
	{key: "name", label: "Name", resizeable: true, sortable: true, width: 106},
	{key: "createDate", label: "Create Date", resizeable: true, formatter: "validDate", sortable: true, width: 121},
	{key: "description", label: "Description", resizeable: true, width: 220},
	{key: "owner", label: "Owner", resizeable: true, sortable: true, width: 73},
	{key: "group", label: "Owner", resizeable: true, sortable: true, width: 73},
	{key: "constructName", label: "Construct", sortable: true, resizeable: true, width: 91},
	{key: "targetName", label: "Target", sortable: true, resizeable: true, width: 91}
];

/**
 * Schedules column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Schedules = [
	{key: "dateToImage", label: "Date To Image", formatter: "validDate", sortable: true, resizeable: true},
	{key: "barcode", label: "Barcode", sortable: true, resizeable: true},
	{key: "imager", label: "Imaging System", sortable: true, resizeable: true},
	{key: "temperature", label: "Temperature", sortable: true, resizeable: true},
	{key: "priority", label: "Priority", sortable: true, resizeable: true},
	{key: "state", label: "State", sortable: true, resizeable: true}
];

/**
 * Screens column definitions.
 */
PIMS.xtal.FilteredTable.columnDefs.Screens = [
	{key: "name", label: "Name", resizeable: true, sortable: true},
	{key: "manufacturerName", label: "Manufacturer", resizeable: true, sortable: true},
	{key: "screenType", label: "Type", resizeable: true, sortable: true}
];

/**
 * Default set of response schema for use by FilteredTable instances.
 */
PIMS.xtal.FilteredTable.responseSchema = {};

/**
 * AnnotationsSerlvet/AnnotationsCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Annotations = {
	resultsList: "records",
	fields: ["barcode", "well", {key: "date", parser: YAHOO.util.DataSource.parseDate}, "description", "name", "version", "type", "colour", "inspectionName"]
};

/**
 * ConditionsSerlvet/ConditionsCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Conditions = {
	resultsList: "records",
	fields: ["localName", "localNumber", "finalPH", "manufacturerCatCode", "manufacturerCode", "manufacturerName", "manufacturerScreenName", "volatileCondition", "saltCondition", "well", "components"]
};

/**
 * ConstructsSerlvet/ConstructsCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Constructs = {
	resultsList: "records",
	fields: ["constructName", "constructId", "description", "constructLink", "group", "owner", "targetLink"]
};

/**
 * GroupsSerlvet/GroupsCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Groups = {
	resultsList: "records",
	fields: ["groupId", "groupName", "groupHead", "organisation"]
};

/**
 * InspectionsSerlvet/InspectionsCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Inspections = {
	resultsList: "records",
	fields: [{key: "date", parser: YAHOO.util.DataSource.parseDate}, "barcode", "imager", "inspectionName", "details"]
};

/**
 * PlatesSerlvet/PlatesCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Plates = {
	resultsList: "records",
	fields: ["link", "barcode", {key: "createDate", parser: YAHOO.util.DataSource.parseDate}, "description", {key: "lastImageDate", parser: YAHOO.util.DataSource.parseDate}, "imager", "numberOfCrystals", "status", "constructId", "constructName", "sampleId", "sampleName", "owner", "username"],
	metaFields: {recentBarcode: "recentBarcode"}
};

/**
 * SamplesSerlvet/SamplesCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Samples = {
	resultsList: "records",
	fields: ["id", "name", {key: "createDate", parser: YAHOO.util.DataSource.parseDate}, "description", "owner", "groupName", "constructId", "constructName", "targetId", "targetName"]
};

/**
 * SchedulesSerlvet/SchedulesCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Schedules = {
	resultsList: "records",
	fields: [{key: "dateToImage", parser: YAHOO.util.DataSource.parseDate}, "barcode", "imager", "temperature", "priority", "state"]
};

/**
 * ScreensSerlvet/ScreensCountSerlvet response schema.
 */
PIMS.xtal.FilteredTable.responseSchema.Screens = {
	resultsList: "records",
	fields: ["name", "manufacturerName", "screenType"]
};




/**
 * Code for parsing a session bookmark into a filter hash. If the second
 * argument has a property "sbMustMatch", the session bookmark is discarded
 * unless its properties match those specified in the sbMustMatch property.
 * 
 * @param bookmark - the session bookmark to be parsed
 * @param config - config object which may contain sbMustMatch restrictions
 * @return The filter hash represented by this bookmark
 */
PIMS.xtal.parseBookmark = function (bookmark, config) {
	var filt, bm, prop;
	if (bookmark) {
		// TODO Convert sessionBookmark writer to use URLencoded query string
		// NB Java's URLEncoder encodes " " as "+" rather than "%20"
		//bm = bookmark.replace(/_/g, "&");
		//bm = bm.replace(/-/g, "=");
		bm = bookmark.replace(/\+/g, " ");
		filt = PIMS.xtal.DataFilter.prototype.fromQueryString(bm);
		if (config && config.sbMustMatch) {
			for (prop in config.sbMustMatch) {
				if (config.sbMustMatch.hasOwnProperty(prop)) {
					if (config.sbMustMatch[prop] !== filt[prop]) {
						filt = null;
						break;
					}
				}
			}
		}
	}
	return filt;
};




/**
 * Code to toggle a filter block
 */
PIMS.xtal.toggleFilter = function (e) {
	var el, filt;
	el = YAHOO.util.Event.getTarget(e);
	if ('show_hide_' !== el.id.substr(0, 10)) {
		el = el.parentNode;
	}
	filt = YAHOO.util.Dom.get(el.id.substr(10));
	if ('none' === filt.style.display) {
		filt.style.display = 'block'; 
		el.innerHTML = '<img src="xtal/images/16x16/minus.png" />&nbsp;Hide Filters';
	}
	else {
		filt.style.display = 'none'; 
		el.innerHTML = '<img src="xtal/images/16x16/plus.png" />&nbsp;Show Filters';
	}
};




/**
 * Initialization method for Home.jsp
 */
PIMS.xtal.initHome = function () {
	var weekago, dfA, dfG, dfI, dlA, dlG, dlI, ftA, ftG, ftI;

	if(document.getElementById("microscope_images")){
		PIMS.xtal.MicroImages.getMicroImages();
	}
	weekago = new Date((new Date()).getTime() - 7 * 24 * 60 * 60 * 1000);
	weekago = weekago.getUTCDate() + '/' + (1 + weekago.getUTCMonth()) + '/' + weekago.getUTCFullYear();

	if(document.getElementById("annotationstable")){
		dfA = new PIMS.xtal.DataFilter({results: 4, 
			startDate: weekago, // not needed, could just show 4 most recent
			sort: "date", dir: "desc", 
			sb: "h_a" //TODO remove, probably unused
			});
		ftA = new PIMS.xtal.FilteredTable(dfA, "annotationstable");
		dlA = new PIMS.xtal.DataLoaderHistoryImpl(dfA, ftA, "ann");
		ftA.servletURL = contextPath + "/AnnotationsServlet?";
		ftA.countServletURL = contextPath + "/AnnotationsCountServlet?";
		ftA.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
		ftA.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops;
		ftA.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Annotations;
		ftA.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Annotations;
		ftA.initialize();
	}

//	if(document.getElementById("groupstable")){
//		dfG = new PIMS.xtal.DataFilter({results: 4, sort: "groupName", dir: "asc", sb: "h_g"});
//		ftG = new PIMS.xtal.FilteredTable(dfG, "groupstable");
//		dlG = new PIMS.xtal.DataLoaderHistoryImpl(dfG, ftG, "grp");
//		ftG.servletURL = contextPath + "/GroupsServlet?";
//		ftG.countServletURL = contextPath + "/GroupsCountServlet?";
//		ftG.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewGroup;
//		ftG.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Groups;
//		ftG.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Groups;
//		ftG.initialize();
//	}
	if(document.getElementById("inspectionstable")){
		dfI = new PIMS.xtal.DataFilter({results: 4, 
			inspectionStartDate: weekago, // not needed, could just show 4 most recent
			sort: "date", dir: "desc", 
			sb: "h_i" //TODO remove, not used
				});
		ftI = new PIMS.xtal.FilteredTable(dfI, "inspectionstable");
		dlI = new PIMS.xtal.DataLoaderHistoryImpl(dfI, ftI, "ins");
		ftI.servletURL = contextPath + "/InspectionsServlet?";
		ftI.countServletURL = contextPath + "/InspectionsCountServlet?";
		ftI.onRefreshEvent = PIMS.xtal.FilteredTable.handlers.onRefreshSelectRecentBarcode;
		ftI.rowClickEvent = PIMS.xtal.FilteredTable.handlers.rowClickViewTrialDrops;
		ftI.myColumnDefs = PIMS.xtal.FilteredTable.columnDefs.Inspections;
		ftI.responseSchema = PIMS.xtal.FilteredTable.responseSchema.Inspections;
		ftI.initialize();
	}
	PIMS.xtal.initializeHistory();
};
