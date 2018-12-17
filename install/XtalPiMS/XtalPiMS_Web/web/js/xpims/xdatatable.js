function formatDate(oDate) {
    var month = oDate.getMonth()+1;
    return oDate.getDate() + "/" + month + "/" + oDate.getFullYear();
}

function convertDate(date) {
    //date is coming in in UK format... need to convert to US
    if (date != undefined && date != "") {
        var dateEls = date.split('/');
        var newDate = new Date();
        newDate.setFullYear(dateEls[2],dateEls[1]-1,dateEls[0]);
        return newDate;
    }
    
    return "";
}

function getBookmarkHash(newBookmark) {
    var bmHash = {};
    if (newBookmark != null) {
        
        var aPairs = newBookmark.split("_");
        for(var i=0; i<aPairs.length; i++) {
            var sPair = aPairs[i];
            var n = sPair.indexOf("-");
            var sParam = aPairs[i].substring(0,n);
            var sValue = aPairs[i].substring(n+1);
            bmHash[sParam] = sValue;
        }
    }
    return bmHash;
}
    
function DataTable() {
    //variables used in this class...
    this.name;
    this.className;
    this.filter;
    this.myColumnDefs;
    this.responseSchema;    
    this.servletURL;
    this.countServletURL;
    
    this.startIndex;
    this.sortField;
    this.sortDir;
    this.initialConfig;
    this.myDataTable;
    this.myDataSource;
    this.totalRows;
    this.totalPages;
    this.subscriptions;
    this.rowClickEvent = "";
    
    this.numPageLinksToShow = parseInt(4);
    
    //functions in this class...
    this.initializeData = initializeData;
    this.renderTable = renderTable;
    this.processTable = processTable;
    this.bookmarkHandler = bookmarkHandler;
    this.updatePagination = updatePagination;
    this.processCount = processCount;
    this.getTotalCountData = getTotalCountData;
    this.bookmark = bookmark;
    this.getRequestString = getRequestString;
    this.getBookmarkString = getBookmarkString;
    this.getBookmarkHash = getBookmarkHash;
    this.drawPagination = drawPagination;
    this.getNumRows = getNumRows;
    this.getNumRowsAsInt = getNumRowsAsInt;
    
    function getNumRows() {
        return 10;
    }
    
    // JMD Bugfix - many of the overridden versions of getNumRows return
    // strings, which really screws up the math. This method takes care of
    // most of the problems.
    function getNumRowsAsInt() {
    	return parseInt(this.getNumRows());
    }
    
    function drawPagination() {
        //This function adds the required elements to the page...
        var tblEl = PIMS.xtal.getElement(this.name);
        var txt = new Array();
        txt.push("<div id='"+this.name + "_dt-pag-nav' class='dt-pag-nav'>");
        txt.push("<span id='"+this.name + "_startLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='"+contextPath+"/xtal/images/16x16/player_start.png' alt='|<' /></span>");
        txt.push("<span id='"+this.name + "_prevLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='"+contextPath+"/xtal/images/16x16/previous.png' alt='<' /></span>");
        txt.push("<span id='"+this.name + "_pagesLinks'></span>");
        txt.push("<span id='"+this.name + "_nextLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='"+contextPath+"/xtal/images/16x16/next.png' alt='>' /></span>");
        txt.push("<span id='"+this.name + "_endLink' style='padding-left:4px;padding-right:4px;width:16px;height:16px;'><img style='border:0;vertical-align:middle' src='"+contextPath+"/xtal/images/16x16/player_end.png' alt='>|' /></span>");
        txt.push("<\/div>");
        txt.push("<div id='"+this.name + "_table'></div>");
        tblEl.innerHTML = txt.join('');
    }
    
    function padString(n) {
        return n < 10 ? '0' + n : n;
    }
    
    YAHOO.widget.DataTable.formatDate = function(el, oRecord, oColumn, oData) {
        var oDate = oData;
        if ((oDate instanceof Date) && (!isNaN(oDate.getDate()))) {
            el.innerHTML = padString(oDate.getDate()) + '\/' + padString((oDate.getMonth()+1))  + '\/' + oDate.getFullYear() + " " + padString(oDate.getHours())  + ":" + padString(oDate.getMinutes());
        } else 
        { 
            el.innerHTML = ""; 
        }
    };    
    
    function getBookmarkString() {
        var arr = new Array();
        arr.push("startIndex-");
        arr.push(this.startIndex);
        arr.push("_sort-");
        arr.push(this.sortField);
        arr.push("_dir-");
        arr.push(this.sortDir);
        
        for (var i in this.filter) {
            var bod = new Array();
            bod.push("_");
            bod.push(i);
            bod.push("-");
            bod.push(this.filter[i]);
            arr.push(bod.join(''));
        }
        
        return arr.join('');            
    }
    
    function bookmark() {
        var bm = this.getBookmarkString();
        //record the bookmark in the History...
        YAHOO.util.History.navigate(this.name, bm);
        this.updatePagination();
    }
    
    function getBookmarkHash(newBookmark) {
        var bmHash = {};
        if (newBookmark != null) {
            
            var aPairs = newBookmark.split("_");
            for(var i=0; i<aPairs.length; i++) {
                var sPair = aPairs[i];
                var n = sPair.indexOf("-");
                var sParam = aPairs[i].substring(0,n);
                var sValue = aPairs[i].substring(n+1);
                bmHash[sParam] = sValue;
            }
        }
        return bmHash;
    }
    
    function getRequestString(newBookmark) {
        if (newBookmark != null) {
            var tmpHash = getBookmarkHash(newBookmark);
            
            // Validate values        

            var newStart = parseInt(tmpHash["startIndex"],10);
            if(YAHOO.lang.isValue(newStart)) {
                this.startIndex=newStart;
            } else
            {
                this.startIndex = 0;
            }
            
            var newSort = tmpHash["sort"];
            if(YAHOO.lang.isValue(newSort)) {
                this.sortField = newSort;
            }
            
            var newDir = tmpHash["dir"];
            if(YAHOO.lang.isValue(newDir)) {
                this.sortDir=newDir;
            }
        }
        var req = new Array();
        for (var i in this.filter) {
            var bod = new Array();
            bod.push("&");
            bod.push(i);
            bod.push("=");
            bod.push(this.filter[i]);
            req.push(bod.join(''));
        }
        
        return "startIndex="+this.startIndex+"&sort="+this.sortField+"&dir="+this.sortDir + req.join('');                
    }
    
    function initializeData() {

        var requestString = this.getRequestString(YAHOO.util.History.getBookmarkedState(this.name));
        
        this.initialConfig = {
            paginator: {
                rowsThisPage: this.getNumRowsAsInt(),
                startRecordIndex: this.startIndex
            },
            sortedBy: {
                key: this.sortField,
                dir: this.sortDir
            },
            initialRequest: requestString
        };    
    }
    
    function updatePagination() {
        if (this.totalPages <= 1) {
            //don't show pagination!
            PIMS.xtal.getElement(this.name+"_dt-pag-nav").style.display = 'none';
        } else 
        {
            PIMS.xtal.getElement(this.name+"_dt-pag-nav").style.display = 'block';
            //var pageNumber = 1 + (this.currentIndex - (this.currentIndex % this.getNumRows())) / this.getNumRows();
            var pageNumber = 1 + (this.startIndex - (this.startIndex % this.getNumRowsAsInt())) / this.getNumRowsAsInt();
            var txt = "";
            var i;
            if (pageNumber > 1) {
                YAHOO.util.Dom.setStyle([this.name + '_startLink', this.name + '_prevLink'], 'opacity', 1.0); 
                YAHOO.util.Dom.setStyle([this.name + '_startLink', this.name + '_prevLink'], 'cursor', 'pointer'); 
            } else
            {
                YAHOO.util.Dom.setStyle([this.name + '_startLink', this.name + '_prevLink'], 'opacity', 0.5); 
                YAHOO.util.Dom.setStyle([this.name + '_startLink', this.name + '_prevLink'], 'cursor', 'default');             
                
            }
            if (pageNumber - this.numPageLinksToShow > 1) {
                txt+="<span id='page1' style='padding-left:4px;padding-right:4px;cursor:pointer;border:#9999FF solid 2px;width:16px;height:16px; background-color:#CCCCFF;' onclick='"+this.className + ".getPageNum(1)'>1<\/span>...";            
            }
            for (i = pageNumber - this.numPageLinksToShow; i <= pageNumber + this.numPageLinksToShow; i++) {                    
                if ((i > 0) && (i <= this.totalPages)) {
                    if (i === pageNumber) {
                        txt += "<span id='page" + i + "' style='padding-left:4px;padding-right:4px;width:16px;height:16px;border:#9999FF solid 2px; background-color:#9999FF;'>" + i + "<\/span>";
                    }
                    else {
                        txt += "<span id='page" + i + "' style='padding-left:4px;padding-right:4px;cursor:pointer;width:16px;height:16px;border:#9999FF solid 2px; background-color:#CCCCFF;' onclick='"+this.className + ".getPageNum(" + i + ")'>" + i + "<\/span>";                                
                    }
                }
            }
            if (i <= this.totalPages) {
                txt+="...<span id='page" + this.totalPages + "' style='padding-left:4px;padding-right:4px;cursor:pointer;width:16px;height:16px;border:#9999FF solid 2px; background-color:#CCCCFF;' onclick='"+this.className + ".getPageNum(" + this.totalPages + ")'>" + this.totalPages + "<\/span>";
            }
            if (pageNumber == this.totalPages) {
                YAHOO.util.Dom.setStyle([this.name + '_endLink', this.name + '_nextLink'], 'opacity', 0.5); 
                YAHOO.util.Dom.setStyle([this.name + '_endLink', this.name + '_nextLink'], 'cursor', 'default'); 
            } else
            {
                YAHOO.util.Dom.setStyle([this.name + '_endLink', this.name + '_nextLink'], 'opacity', 1.0); 
                YAHOO.util.Dom.setStyle([this.name + '_endLink', this.name + '_nextLink'], 'cursor', 'pointer'); 
                
            }
            
            PIMS.xtal.getElement(this.name + "_pagesLinks").innerHTML = txt;    
        }
    }        
    
    function processCount(transport) {
        var reply = PIMS.xtal.parseJson(transport.responseText);
        this.totalRows = parseInt(reply.count);
        
        this.totalPages = (this.totalRows - this.totalRows % this.getNumRowsAsInt()) / this.getNumRowsAsInt();
        if (this.totalRows % this.getNumRows() > 0) {this.totalPages += 1;} 
        
        if (this.totalRows < this.startIndex) {
            this.startIndex = 0;
            this.bookmark();
        }
        
        this.updatePagination();
    }
    
    function getTotalCountData () {
        var req = new Array();
        for (var k in this.filter) {
            var bod = new Array();
            bod.push(k);
            bod.push("=");
            bod.push(this.filter[k]);
            req.push(bod.join(''));
        }
        
        var filters = "";
        if (req.length > 0) { filters = "&" + req.join("&"); }
        
        var url = this.countServletURL + "count=true" + filters;
        
        var _aborted = undefined;
        if (this._oCountConn && (YAHOO.util.Connect.isCallInProgress(this._oCountConn))) {
        	var _aborted = YAHOO.util.Connect.abort(this._oCountConn);
        }
        this._oCountConn = PIMS.xtal.asyncRequest('GET', url, {success:processCount, scope:this}, "");
    }
    
    
    
    function bookmarkHandler(newBookmark, oSelf) {
        var requestText=oSelf.getRequestString(newBookmark);
        
        oSelf.myDataSource.sendRequest(requestText, oSelf.myDataTable.onDataReturnInitializeTable, oSelf.myDataTable);
    }
    
    function processTable () {
        
        // Instantiate DataSource
        this.myDataSource = new YAHOO.util.DataSource(this.servletURL);
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        this.myDataSource.responseSchema = this.responseSchema;
        
        // JMD Cut down on weirdness
        this.myDataSource.connXhrMode = "ignoreStaleResponses";
        
        // Instantiate DataTable
        this.myDataTable = new YAHOO.widget.DataTable(this.name+"_table", this.myColumnDefs,
            this.myDataSource, this.initialConfig);
        this.myDataSource.dataTable=this;
        if (this.rowClickEvent !== "") {
            this.myDataTable.subscribe("rowClickEvent", this.rowClickEvent);
        }
        // Enables single-mode row selection
        this.myDataTable.set("selectionMode", "single");
        
        this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
        this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow); 
        
        // JMD
        if (this.onRefreshEvent) {
        	this.myDataTable.subscribe("refreshEvent", this.onRefreshEvent);
        }
        
        for (var i in this.subscriptions) {
            this.myDataTable.subscribe(i, this.subscriptions[i]);
        }
        // Override function for custom sorting
        this.myDataTable.sortColumn = function(oColumn) {
            var ds = this.getDataSource().dataTable;
            // Which direction
            var sDir = "asc";
            // Already sorted?
            if(oColumn.key === ds.sortField) {
                sDir = (ds.sortDir === "asc") ?
                "desc" : "asc";
            }
            
            ds.sortField=oColumn.key;
            ds.sortDir=sDir;
            
            ds.bookmark();
        };
        // Custom code to parse the raw server data for Paginator values and page links and sort UI
        this.myDataSource.doBeforeCallback = function(oRequest, oRawResponse, oParsedResponse) {
            var oSelf = this.dataTable;
            var oDataTable = oSelf.myDataTable;
            
            var oRawResponse = PIMS.xtal.parseJson(oRawResponse);
            var endIndex = this.startIndex + oRawResponse.recordsReturned - 1; 
            if (endIndex > this.totalRows) {
                endIndex = this.totalRows;
            }           
            
            // Update the config sortedBy with new values 
            var newSortedBy = { 
                key: oSelf.sortField, 
                dir: oSelf.sortDir 
            } 
            oDataTable.set("sortedBy", newSortedBy);
            
            if (oRawResponse.recentBarcode) {
            	recentBarcode = oRawResponse.recentBarcode;
            }
            
            // Let the DataSource parse the rest of the response
            return oParsedResponse;
        };
    }
    
    // Hook up custom pagination
    this.getPage = function() {
        if (this.startIndex < 0) {
            this.startIndex = 0;
        }
        else if (this.startIndex >= this.totalRows) {
            this.startIndex = this.totalRows - 1;
        }
        this.bookmark();
    };
    
    this.getStartPage = function(e) {
        PIMS.xtal.stopEvent(e);
        if(this.startIndex === 0) {
            return;
        }
        this.startIndex = 0;
        this.getPage();
    };
    
    this.getPreviousPage = function(e) {
        PIMS.xtal.stopEvent(e);
        // Already at first page
        if(this.startIndex === 0) {
            return;
        }
        this.startIndex -= this.getNumRowsAsInt();
        this.getPage();
    };
    
    this.getPageNum = function(pageNum) {
        this.startIndex = (pageNum - 1) * this.getNumRowsAsInt();
        this.getPage();
    };
    
    this.getNextPage = function(e) {
        PIMS.xtal.stopEvent(e);
        
        var currentPage = 1 + (this.startIndex - (this.startIndex % this.getNumRowsAsInt())) / this.getNumRowsAsInt();
        
        if (currentPage === this.totalPages) {
            return;
        }
        
        this.startIndex += this.getNumRowsAsInt();
        this.getPage();
    };
    
    this.getEndPage = function(e) {
        PIMS.xtal.stopEvent(e);
        var currentPage = 1 + (this.startIndex - (this.startIndex % this.getNumRowsAsInt())) / this.getNumRowsAsInt();
        
        if (currentPage === this.totalPages) {
            return;
        }
        
        this.startIndex = (this.totalPages - 1) * this.getNumRowsAsInt();
        
        this.getPage();
    };
    this.getPageClick = function(e) {
        var i=0;
        i++;
    }
    
    this.changeRowCount = function(e) {
        PIMS.xtal.stopEvent(e);

        this.totalPages = (this.totalRows - this.totalRows % this.getNumRowsAsInt()) / this.getNumRowsAsInt();
        if (this.totalRows % this.getNumRowsAsInt() > 0) {this.totalPages += 1;}            
        this.updatePagination();
        this.getPage();
    };
 
    //Renders the table...
    function renderTable() {
        if (sessionBookmark !== 'null') {
            var bmHash = this.getBookmarkHash(sessionBookmark);

            this.startIndex = bmHash["startIndex"];          
            this.sortField = bmHash["sort"];
            this.sortDir = bmHash["dir"];
        } else
        {
            this.startIndex = 0;            
        }
        this.drawPagination();
        this.getTotalCountData();
        this.initializeData();
        //getTotalCountData();
        PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_prevLink"), "click", this.getPreviousPage, this, true);
        PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_nextLink"), "click", this.getNextPage, this, true);
        PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_pagesLinks"), "click", this.getPageClick, this, this);
        PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_startLink"), "click", this.getStartPage, this, true);
        PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_endLink"), "click", this.getEndPage, this, true);
        //if (PIMS.xtal.getElement(this.name+"_numRows") != null) {
        //    PIMS.xtal.addListener(PIMS.xtal.getElement(this.name+"_numRows"), "change", this.changeRowCount, this, true);
        //}
        
        this.myBookmarkedState = YAHOO.util.History.getBookmarkedState(this.name);
        var req = new Array();
        for (var i in this.filter) {
            var bod = new Array();
            bod.push("_");
            bod.push(i);
            bod.push("-");
            bod.push(this.filter[i]);
            
            req.push(bod.join(''));
        }
        this.myInitialState = this.myBookmarkedState || this.getBookmarkString();
        
        YAHOO.util.History.register(this.name, this.myInitialState, this.bookmarkHandler, this);
        
        YAHOO.util.History.onLoadEvent.subscribe(this.processTable, this, true);
    } 
}