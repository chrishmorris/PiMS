var data;

function processScreenData(transport){
	try {
    data = transport.responseText.evalJSON();
    PIMS.xtal.getElement("screen_name").innerHTML = data['name'];    
    PIMS.xtal.getElement("screen_manufacturer").innerHTML = data['manufacturerName'];
    PIMS.xtal.getElement("screen_type").innerHTML = data['type'];
	} catch (e) {
		alert("Invalid data: "+transport.responseText);
	}
}

function getScreenData() {
    var url = contextPath + "/ScreenServlet?id=" + screenId;
    PIMS.xtal.asyncRequest('GET', url, {success:processScreenData, scope:this}, ""); 
}

PIMS.xtal.Event.addListener(window, "load", getScreenData);

PIMS.xtal.Event.addListener(window, "load", function() {
    YAHOO.xtalpims.componentsTable = new function() {
        function padString(n) {
            return n < 10 ? '0' + n : n;
        }
        
        YAHOO.widget.DataTable.formatDate = function(el, oRecord, oColumn, oData) {
            var oDate = oData;
            if(oDate instanceof Date) {
                el.innerHTML = padString(oDate.getDate()) + '\/' + padString((oDate.getMonth()+1))  + '\/' + oDate.getFullYear() + " " + padString(oDate.getHours())  + ":" + padString(oDate.getMinutes());
            } else { el.innerHTML = YAHOO.lang.isValue(oData) ? oData : ""; }
        }
        
        YAHOO.widget.DataTable.formatCheckbox = function( el , oRecord , oColumn , oData ) {
            var oBool = oData;
            if (oBool == "true")
            {
                el.innerHTML = "<input type='checkbox' disabled='disabled' checked='checked'>";
            } 
            else
            {
                el.innerHTML = "<input type='checkbox' disabled='disabled'>";
            }
        }
        var totalRows = 0;
        var totalPages = 0;
        var numResults = parseInt(PIMS.xtal.getElement("numRows").value);
        var numPageLinksToShow = 4;
        var currentIndex = parseInt(0);
        
        updatePagination = function() {           
            numResults = parseInt(PIMS.xtal.getElement("numRows").value);
            if (totalPages === 1) {
                $('navigation').hide();
            }
            else {
            var pageNumber = 1 + (currentIndex - (currentIndex % numResults)) / numResults;
            var txt = "";
            for (var i = pageNumber - numPageLinksToShow; i <= pageNumber + numPageLinksToShow; i++) {                    
                if ((i > 0) && (i <= totalPages)) {
                    if (i === pageNumber) {
                        txt += "<span id='page" + i + "' style='padding-left:4px;padding-right:4px;border:#9999FF solid 2px; background-color:#9999FF;' onclick='YAHOO.xtalpims.componentsTable.getPageNum(" + i + ")'>" + i + "<\/span>";
                    }
                    else {
                        txt += "<span id='page" + i + "' style='padding-left:4px;padding-right:4px;border:#9999FF solid 2px; background-color:#CCCCFF;' onclick='YAHOO.xtalpims.componentsTable.getPageNum(" + i + ")'>" + i + "<\/span>";                                
                    }
                }
            }
            $('navigation').show();
            $("pagesLinks").innerHTML = txt;                    
            }
        }        

        this.processCount = function(transport){
        	try {
            var reply = transport.responseText.evalJSON();
            totalRows = parseInt(reply.count);  
            totalPages = (totalRows - totalRows % numResults) / numResults;    
            if (totalRows % numResults > 0) {totalPages += 1;}            
            updatePagination();
            } catch (e) {
        		alert("Invalid data: "+transport.responseText);
        	}
        };
        
        this.getTotalCountData = function() {                    
            var url = contextPath + "/ScreenServlet?count=true";
            var m = new Ajax.Request(
                url, 
                {
                    method: 'get', 
                    onComplete: this.processCount
                }
            );                    
        };       
        //this.getTotalCountData();

        
        // Column definitions
        var myColumnDefs = [
            {key:"Well", label:"Well"},
            {key:"LocalName", label:"Local Name"},
            {key:"LocalNumber", label:"Local Number"},
            {key:"ManufacturerName", label:"Manufacturer Name"},
            {key:"ManufacturerScreenName", label:"Manufacturer Screen Name"},
            {key:"ManufacturerCode", label:"Manufacturer Code"},
            {key:"ManufacturerCatCode", label:"Catalogue Code"},
            {key:"VolatileCondition", label:"Volatile", formatter:YAHOO.widget.DataTable.formatCheckbox},
            {key:"FinalpH", label:"Final pH"},
            {key:"Components", label:"Components"}
        ];
        
        // DataSource instance
        this.myDataSource = new YAHOO.util.DataSource(contextPath + "/ScreenServlet?");
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        this.myDataSource.responseSchema = {
            resultsList: "records",
            fields: ["Well", "LocalName", "LocalNumber","ManufacturerName","ManufacturerScreenName", "ManufacturerCode", 
                        "ManufacturerCatCode", "VolatileCondition", "FinalpH", "Components", "Id"]
        };
        
        // DataTable instance
        var oConfigs = {                    
            initialRequest: "startIndex=" + currentIndex + "&results=" + numResults + "&sort=Name&dir=asc&id=" + screenId//,// Initial values
            //sortedBy:{key:"Name", dir:"asc"} // Set up initial column headers 
        };
        this.myDataTable = new YAHOO.widget.DataTable("components", myColumnDefs,
            this.myDataSource, oConfigs);
        
        // Custom code to parse the raw server data for Paginator values and page links
        this.myDataSource.doBeforeCallback = function(oRequest, oRawResponse, oParsedResponse) {
            //updatePagination();
            // Update the DataTable Paginator with new values
            
            var oSelf = YAHOO.xtalpims.componentsTable;
            var oDataTable = oSelf.myDataTable;
            
            // Get Paginator values
            var oRawResponse = PIMS.xtal.parseJson(oRawResponse); 
            var endIndex = currentIndex + oRawResponse.recordsReturned - 1; 
            if (endIndex > totalRows) {
                endIndex = totalRows;
            }   
            totalRows = oRawResponse.records.size();
            $('screen_name').innerHTML = oRawResponse.Name;    
            $('screen_manufacturer').innerHTML = oRawResponse.ManufacturerName;
            $('screen_type').innerHTML = oRawResponse.Type;
            // Update the links UI
            if (currentIndex > 0) {
                $('startLink').style.cursor = 'pointer';
                $('prev10Link').style.cursor = 'pointer';
                $('prevLink').style.cursor = 'pointer';
            } else {
                $('startLink').style.cursor = 'default';
                $('prev10Link').style.cursor = 'default';
                $('prevLink').style.cursor = 'default';        
            }
            var pageNumber = 1 + (currentIndex - (currentIndex % numResults)) / numResults;
           
            if (pageNumber < totalPages) {
                $('endLink').style.cursor = 'pointer';
                $('next10Link').style.cursor = 'pointer';
                $('nextLink').style.cursor = 'pointer';
            } else {
                $('endLink').style.cursor = 'default';
                $('next10Link').style.cursor = 'default';
                $('nextLink').style.cursor = 'default';        
            }            
            
            // Update the config sortedBy with new values
            var sortCol = oRawResponse.sort; // Which column is sorted 
            var sortDir = oRawResponse.dir; // Which sort direction 
            
            // Update the config sortedBy with new values 
            var newSortedBy = { 
                key: sortCol, 
                dir: sortDir 
            } 
            oDataTable.set("sortedBy", newSortedBy);
            
            // Let the DataSource parse the rest of the response
            return oParsedResponse;
        };
        
        // Hook up custom pagination
        this.getPage = function(nStartRecordIndex, nResults) {
            // If a new value is not passed in
            // use the old value
            if (currentIndex < 0) {currentIndex = 0;}
            if (currentIndex > totalRows) {currentIndex = (totalPages - 1) * numResults;}
            
            var oSortedBy = this.myDataTable.get("sortedBy");
            var newRequest = "startIndex=" + currentIndex + "&results=" + numResults + "&sort=" + oSortedBy.key + "&dir=" + oSortedBy.dir;
            updatePagination();
            this.myDataSource.sendRequest(newRequest, this.myDataTable.onDataReturnInitializeTable, this.myDataTable);
        };
        this.getPageNum = function(pageNum) {
            currentIndex = (pageNum - 1) * numResults;
            this.getPage(currentIndex);
        };
        
        this.getPreviousPage = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            // Already at first page
            if(currentIndex === 0) {
                return;
            }
            currentIndex -= numResults;
            this.getPage(currentIndex);
        };
        this.getNextPage = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            
            var currentPage = 1 + (currentIndex - (currentIndex % numResults)) / numResults;
            
            if (currentPage === totalPages) {
                return;
            }
                
            currentIndex += numResults;
            this.getPage(currentIndex);
        };
        this.getPrevious10Page = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            // Already at first page
            if(currentIndex === 0) {
                return;
            }
            currentIndex = currentIndex - 10*numResults;
            
            this.getPage(currentIndex);
        };
        this.getNext10Page = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            // Already at last page
            var currentPage = 1 + (currentIndex - (currentIndex % numResults)) / numResults;
            
            if (currentPage === totalPages) {
                return;
            }
            currentIndex = (currentIndex + 10*numResults);
            
            this.getPage(currentIndex);
        };
        this.getStartPage = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            if(currentIndex === 0) {
                return;
            }
            currentIndex = 0;                   
            this.getPage(currentIndex);
        };
        this.getEndPage = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            var currentPage = 1 + (currentIndex - (currentIndex % numResults)) / numResults;
            
            if (currentPage === totalPages) {
                return;
            }
            
            currentIndex = (totalPages - 1) * numResults;
            
            this.getPage(currentIndex);
        };
        
        this.changeRowCount = function(e) {
            PIMS.xtal.Event.stopEvent(e);
            numResults = parseInt(PIMS.xtal.getElement("numRows").value);
            totalPages = totalRows / numResults;
            if (totalRows % numResults > 0) {totalPages += 1;}
            updatePagination();
            this.getPage(currentIndex);
        };

        
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("prevLink"), "click", this.getPreviousPage, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("nextLink"), "click", this.getNextPage, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("prev10Link"), "click", this.getPrevious10Page, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("next10Link"), "click", this.getNext10Page, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("startLink"), "click", this.getStartPage, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("endLink"), "click", this.getEndPage, this, true);
        PIMS.xtal.Event.addListener(PIMS.xtal.getElement("numRows"), "change", this.changeRowCount, this, true);
        // Enables single-mode row selection
        this.myDataTable.set("selectionMode", "single");
        
        this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
        this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow); 
        
        //this.myDataTable.subscribe("rowClickEvent", function(oArgs){                               
        //    var elRecord = this.getRecord(oArgs.target);
        //    var id = elRecord.getData("Name");
        //    window.location.href=contextPath + "/ViewScreen.jsp?id=" + id;
        //}); 
        // Override function for custom sorting
        this.myDataTable.sortColumn = function(oColumn) {
            // Which direction
            var sDir = "asc";
            // Already sorted?
            if(oColumn.key === this.get("sortedBy").key) {
                sDir = (this.get("sortedBy").dir === "asc") ?
                "desc" : "asc";
            }
            
            var newRequest = "sort=" + oColumn.key + "&dir=" + sDir + "&results=" + numResults + "&startIndex=0";
            this.getDataSource().sendRequest(newRequest, this.onDataReturnInitializeTable, this);
        };
        
    };
});