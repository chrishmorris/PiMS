/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global $, Ajax, Event, PIMS, contextPath, barcode, localName, well */
var currentWellId, wellData, myDV, sTDL, tTDL, tcMP, fntckeydown;

function myresize(e) {
	var wh, th;
	wh = (window.innerHeight ? window.innerHeight : document.documentElement.clientHeight);
	th = wh - $("menu").getHeight() - 20;
	$("timecoursebox").style.height = th + "px";
	$("fulltimecourse").style.height = th - $("timecourse_header").getHeight() - 25 - 7 + "px";
	$("drops").style.height = th + "px";
	$("drops_images").style.height = th - $("drops_header").getHeight() - 7 + "px";
}

function writeImageDiv() {
	var imagesHTML, records, d, i, drop;
	imagesHTML = [];
	records = wellData.records;
	if (0 < records.length) {
		for (d = 0; d < records.length; d++) {
			drop = records[d];
			i = drop.images.length - 1;
			imagesHTML.push('<img id="s_');
			imagesHTML.push(d);
			imagesHTML.push('_');
			imagesHTML.push(i);
			imagesHTML.push('" src="');
			imagesHTML.push(drop.images[i].url);
			imagesHTML.push('" width="231" style="cursor:pointer" />');
		}
	}
	else {
		imagesHTML.push('<p>There are no example salt crystals for this condition.</p>');
	}
	$("drops_images").innerHTML = imagesHTML.join('');	
	$("drops_header").innerHTML = 'Example Salt Crystals for ' + localName + ' ' + well;
}

function renderWells(transport) {
	wellData = transport.responseJSON;
	sTDL.setData(wellData);
	writeImageDiv();
	myDV.setAnnotationData('<span id="annotation" style="margin:0 2px;font-family:arial,helvetica,sans-serif;font-size:12px;font-weight:normal;background-color:#fff;border:1px solid #404040;">&nbsp;Salt Crystal&nbsp;</span>');
}

/* Main data set */
function fetchData() {
	//get well data
	var url, foo;
	url = contextPath + "/SaltCrystalServlet?startIndex=0&results=-1&sort=well&dir=asc&well=" + well + "&localName=" + localName;
	foo = new Ajax.Request(url, {
		method: "get",
		onSuccess: function (transport) {
			renderWells(transport); 
		},
		onFailure: function (transport) {
			alert(transport.responseText);
		}
	});
}


/*
 * Displaying the image, and well info
 */
function scrollTCWindowTo(wellId) {
	var w = wellId;
	if (!w) {
		w = 't_' + tTDL.dropIndex + '_' + tTDL.imageIndex;
	}
	$("fulltimecourse").scrollTop = $(w).offsetTop - 120;
}

function closeFullTimeCourse() {
	$("drops").style.display = "block";
	$("tcbox").style.display = "none";
	$("fulltimecourse").innerHTML = "<img src='" + contextPath + "/images/icons/actions/waiting.gif' />";
	tcMP.stopMovie(null, true);
	document.stopObserving('keydown', fntckeydown);
	//document.observe('keydown', mykeydown);
	//sMP.changeDelay(0);
	//showImage(currentWellId);
}

function showFullTimeCourse() {
	var numImages, html, cimg, curl, cindex, i, im, d, str;
	//sMP.stopMovie(null, true);
	$("drops").style.display = "none";
	$("tcbox").style.display = "block";
	$("fulltimecourse").innerHTML = "";
	numImages = tTDL.countImages();
	html = [];
	cimg = sTDL.getCurrentImage();
	curl = cimg.url;
	cindex = 0;
	for (i = 0; i < numImages; i++) {
		im = tTDL.getImage(0, i);
		if (!im.dateUTC) {
			d = new Date(im.date);
			im.dateUTC = d.toUTCString();
		}
		str = [
			'<div class="timecourseimage" id="timecourseimg_',
			i,
			'">',
			'<img id="t_0_',
			i,
			'" width="140" src="',
			im.url,
			'" onload="scrollTCWindowTo();" />',
			'<br/>',
			im.dateUTC,
			'<br/>(',
			im.timePoint,
			')<\/div>'
		];
		html.push(str.join(''));
		im.color = '#d5cf21';
		if (curl === im.url) {
			cindex = i;
		}
	}
	html.push('<div class="timecourseimage" style="height:');
	html.push($("fulltimecourse").getHeight());
	html.push('px"></div>');
	$("fulltimecourse").innerHTML = html.join('');
	$("timecoursebox")._wellId = currentWellId;
	tTDL.imageIndex = cindex;
	tcMP.initialize();
	//document.stopObserving('keydown', mykeydown);
	document.observe('keydown', fntckeydown);
}

function sortByDate(a, b) {
	return a.date - b.date;
}
function renderTimeCourseEnds(transport) {
	var html, record, numImages, cimg, cruler, i, im, d;
	tTDL.setData(transport.responseJSON);
	record = tTDL.getCurrentDrop();
	numImages = tTDL.countImages();
	if (0 === numImages) {
		html = ["Never imaged"];
	} else if (1 === numImages) {
		html = ["Only imaged once"];
	} else {
		record.images.sort(sortByDate);
		record.softwareScores.sort(sortByDate);
		html = [
			'<table><tr><th>Earliest</th><th>Latest</th></tr><tr><td><img id="t_0_0_tc0" width="100" src="',
			record.images[0].url,
			'"/></td><td><img id="t_0_',
			numImages - 1,
			'_tcn" width="100" src="',
			record.images[numImages - 1].url,
			'"/></td></tr><tr><td colspan="2"><a href="#" onclick="showFullTimeCourse(); return false">Full time course...<\a></td></tr></table>'
		];
	}
	$("timecourse").innerHTML = html.join("");
	cimg = sTDL.getCurrentImage();
	cruler = cimg.ruler;
	if (!cruler) {
		cimg.ruler = [-1, -1, -1, -1];
		cruler = cimg.ruler;
	}
	for (i = 0; i < numImages; i++) {
		im = tTDL.getImage(0, i);
		if (!im.dateUTC) {
			d = new Date(im.date);
			im.dateUTC = d.toUTCString();
		}
		im.ruler = cruler;
	}
}

function fetchCurrentWellData() {
	var w, url, foo;
	w = currentWellId.split('_');
	url = contextPath + "/TrialDropServlet?startIndex=0&results=-1&sort=well&dir=asc&barcode=" + barcode + "&well=" + wellData.records[w[1]].well;
	foo = new Ajax.Request(url, {
		method: "get",
		onSuccess: function (transport) { 
			renderTimeCourseEnds(transport); 
		},
		onFailure: function (transport) {
			alert(transport.responseText);
		}
	});
}

function handleWellOnClick(e) {
	var el, w, d;
	el = e.element();
	if ('s_' === el.id.substring(0, 2)) {
		w = el.id.split('_');
		d = {};
		d.records = [sTDL.getDrop(w[1])];
		d.records[0].images.sort(sortByDate);
		tTDL.setData(d);
		sTDL.dropIndex = w[1];
		sTDL.imageIndex = w[2];
		showFullTimeCourse();
	}
}

function swapImageIn(e) {
	var el, w;
	el = e.element();
	if (el.id && ('t_' === el.id.substring(0, 2))) {
		w = el.id.split('_');
		myDV.setImage(tTDL.getImage(w[1], w[2]));
		myDV.update();
		e.stop();
	}
}
function swapImageOut(e) {
	var el = e.element();
	if (el.id && ('t_' === el.id.substring(0, 2))) {
		myDV.setImage(sTDL.getCurrentImage());
		myDV.update();
		e.stop();
	}
}

/* TC Key Bindings */
function tckeydown(e) {
	var key = e.which || e.keyCode;
	if (81 === key) {
		tcMP.moveFirst(e);
	} else if (69 === key) {
		tcMP.moveLast(e);
	} else if (65 === key) {
		tcMP.movePrev(e);
	} else if (83 === key) {
		tcMP.toggleMovie(e);
	} else if (68 === key) {
		tcMP.moveNext(e);
	} else if (70 === key) {
		closeFullTimeCourse();
	}
}
fntckeydown = tckeydown;

/*
* Onload initialisation
*/
function initTrialDrops(mayUpdate) {
	myresize();
	//$("timecoursebox").style.display="none";
	$("tcbox").style.display = "none";
	Event.observe(window, 'resize', myresize);
	//document.observe('keydown', mykeydown);
	//$('wells').observe('click', handleWellOnClick);
	$('drops_images').observe('click', handleWellOnClick);
	//$("fulltimecourse").observe("mouseover", swapImageIn);
	//$("fulltimecourse").observe("mouseout", swapImageOut);
	fetchData();
}

currentWellId = "";

myDV = new PIMS.xtal.DropView('dropview', null, -1);
myDV.minZoom = 40;
myDV.maxZoom = 400;
myDV.zoomStep = 20;

sTDL = new PIMS.xtal.TrialDropList('s');

/* TC Movie */
tTDL = new PIMS.xtal.TrialDropList('t');
tcMP = new PIMS.xtal.MoviePlayer('tc');
tcMP.tdl = tTDL;
tcMP.update = function (d, i, image, p) {
	scrollTCWindowTo('t_' + d + '_' + i);
	myDV.setImage(image);
	myDV.update();
};

