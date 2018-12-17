/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global window, $, Ajax, Event, PIMS, contextPath, barcode, inspection, subposition, initialWell, user */

var currentWellId, // "t_" + tTDL.dropIndex + "_" + tTDL.imageIndex
wellData, // {count:, recordsReturned:, records: [trialDrop.toJSON(), ...]}
  fnmykeydown, fntckeydown;
currentWellId = "";

// Drop View
var myDV = new PIMS.xtal.DropView("dropview", null, -1);
myDV.minZoom = 40;
myDV.maxZoom = 400;
myDV.zoomStep = 20;

//TODO move this to DataFilter.js
function notifyFailure(transport){
	alert("Sorry, there has been an error, code: "+transport.status);
	var w=window.open("","w");
	w.document.write(transport.responseText);
	document.location.reload();
}


function myresize(e) {
	var wh, th;
	wh = (window.innerHeight ? window.innerHeight : document.documentElement.clientHeight);
	th = wh - $("menu").getHeight() - 20;
	$("timecoursebox").style.height = th + "px";
	$("fulltimecourse").style.height = th - $("timecourse_header").getHeight() - 25 - 7 + "px";
}

// for timecourse
function swapImageIn(e) {
	var el, w;
	el = e.element();
	if (el.id && ("t_" === el.id.substring(0, 2))) {
		w = el.id.split("_");
		myDV.setImage(tTDL.getImage(w[1], w[2]));
		myDV.update();
		e.stop();
	}
}

//for timecourse
function swapImageOut(e) {
	var el = e.element();
	if (el.id && ("t_" === el.id.substring(0, 2))) {
		myDV.setImage(sTDL.getCurrentImage());
		myDV.update();
		e.stop();
	}
}

function showImage(wellId) {
	var w = wellId.split("_");
	myDV.setImage(sTDL.getImage(w[1], w[2]));
	myDV.update();
}

function closeFullTimeCourse() {
	$("timecoursebox").style.display = "none";
	$("fulltimecourse").innerHTML = "<img src='" + contextPath + "/images/icons/actions/waiting.gif' />";
	tcMP.stopMovie(null, true);
	document.stopObserving("keydown", fntckeydown);
	document.observe("keydown", fnmykeydown);
	sMP.changeDelay(0);
	showImage(currentWellId);
}

function showFullTimeCourse() {
	var numImages, html, cimg, curl, cindex, i, im, d, str;
	sMP.stopMovie(null, true);
	$("timecoursebox").style.display = "block";
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
			"<div class='timecourseimage' id='timecourseimg_",
			i,
			"'>",
			"<img id='t_0_",
			i,
			"' width='140' src='",
			im.url,
			"' onload='scrollTCWindowTo();' />",
			"<br/>",
			im.dateUTC,
			"<br/>(",
			im.timePoint,
			")<\/div>"
		];
		html.push(str.join(""));
		if (curl === im.url) {
			cindex = i;
		}
	}
	html.push("<div class='timecourseimage' style='height:");
	html.push($("fulltimecourse").getHeight());
	html.push("px'></div>");
	$("fulltimecourse").innerHTML = html.join("");
	$("timecoursebox")._wellId = currentWellId;
	tTDL.imageIndex = cindex;
	tcMP.initialize();
	document.stopObserving("keydown", fnmykeydown);
	document.observe("keydown", fntckeydown);
}

/* Main Nav Key Bindings */
function mykeydown(e) {
	if (e.altKey || e.ctrlKey) {
		return;
	}
	var key = e.which || e.keyCode;
	if (81 === key) {
		sMP.moveFirst(e);
	} else if (69 === key) {
		sMP.moveLast(e);
	} else if (65 === key) {
		sMP.movePrev(e);
	} else if (83 === key) {
		sMP.toggleMovie(e);
	} else if (68 === key) {
		sMP.moveNext(e);
	} else if (70 === key) {
		showFullTimeCourse();
	} else if (220 === key) {
		sMP.setImage(e, 60, 0);
	}
}
fnmykeydown = mykeydown;

/* TC Key Bindings */
function tckeydown(e) {
	if (e.altKey || e.ctrlKey) {
		return;
	}
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

function formatScore(score) {
	var html;
	if (!score) {
		return "<table id='score' style='width:100%'><tr><td>Never scored</td></tr></table>";
	}
	if (!score.dateVar) {
		score.dateVar = new Date(score.date);
	}
	html = [];
	html.push("<table id='score' style='width:100%;background-color:");
	html.push(score.colour);
	html.push("'><tr><td>");
	html.push(score.dateVar.getUTCDate());
	html.push("/");
	html.push(score.dateVar.getUTCMonth());
	html.push("/");
	html.push(score.dateVar.getUTCFullYear());
	html.push("</td><td>");
	html.push(score.name);
	if ((score.version) && ("-" !== score.version)) {
		html.push(" v");
		html.push(score.version);
	}
	html.push("</td><td style='border-left:1.5em solid ");
	html.push(score.colour);
	html.push("'>");
	html.push(score.description);
	html.push("</td></tr></table>");
	return html.join("");
}

function getAnnotationData(scheme) {
	var url, foo;
	url = contextPath + "/ListAnnotations?scheme=" + scheme;
	//alert(url);
	foo = new Ajax.Request(url, {
		method: "get",
		onSuccess: function (transport) { 
			myDV.setAnnotationData(transport.responseText);
		},
		onFailure: function (transport) {
			notifyFailure(transport);
		}
	});
}
function setAnnotation(value) {
	if (sMP.isPlaying()) {
		sMP.stopMovie();
		return;
	}
	var o = $("annotation");
	o.value = value;
	o.onchange();
}
function updateAnnotation(img, annotation) {
	var c, s, sdi, tdi, d, w, cs, drop, i;
	c = $("annotation_" + annotation).getStyle("backgroundColor");
	s = {
			barcode: img.barcode,
			colour: c,
			date: new Date(),
			description: annotation,
			inspectionName: img.inspectionName,
			name: user,
			sampleId: -1,
			sampleName: "unknown",
			type: "human",
			version: "-",
			well: img.well
		};

	img.color = c;

	w = currentWellId.split("_");
	cs = wellData.records[w[1]].images[w[2]];
	for (d = 0; d < wellData.records.length; d++) {
		drop = wellData.records[d];
		if ((drop.barcode === img.barcode) && (drop.well === img.well)) {
			drop.humanScores.unshift(s);
			sdi = [d, 0];
			for (i = 0; i < drop.images.length; i++) {
				if (drop.images[i].url === img.url) {
					drop.images[i].color = c;
					$("s_" + d + "_" + i).style.backgroundColor = c;
					sdi = [d, i];
				}
			}
		}
	}

	if (tTDL.data) {
		for (d = 0; d < tTDL.countDrops(); d++) {
			drop = tTDL.getDrop(d);
			tdi = [d, 0];
			if ((drop.barcode === img.barcode) && (drop.well === img.well)) {
				drop.humanScores.unshift(s);
				for (i = 0; i < drop.images.length; i++) {
					if (drop.images[i].url === img.url) {
						drop.images[i].color = c;
						tdi = [d, i];
					}
				}
			}
		}
	}

	$("score").replace(formatScore(s));
	showImage(currentWellId);
}
function annotate(scheme) {
	var ao, annotation, _img, url, foo;
	ao = $("annotation");
	annotation = ao.value;
	if ("" !== annotation) {
		_img = myDV.getImage();
		url = contextPath + "/ScoreImageServlet?barcode=" + _img.barcode + "&well=" + _img.well + "&name=" + _img.inspectionName + "&scheme=" + scheme + "&annotation=" + annotation;
		foo = new Ajax.Request(url, {
			method: "POST",
			onSuccess: function (transport) { 
				updateAnnotation(_img, annotation);
			},
			onFailure: function (transport) {
				notifyFailure(transport);
			}
		});
		ao.selectedIndex = 0;
	}
	ao = null;
}

/* Main data set */
function showWells96Plate() {
	var wellsHTML, count, rownum, rows, i, record, wellColor;
	$("wells").innerHTML = "";
	wellsHTML = [];
	count = 0;
	rownum = 0;
	rows = ["A", "B", "C", "D", "E", "F", "G", "H"];
	wellsHTML.push("<table><tr><th>&nbsp;</th>");
	for (i = 1; i <= 12; i++) {
		wellsHTML.push("<th>" + i + "</th>");
	}
	for (i = 0; i < wellData.records.length; i++) {
		record = wellData.records[i];
		if (0 === count) {
			wellsHTML.push("</tr><tr><th>");
			wellsHTML.push(rows[rownum]);
			wellsHTML.push("</th>");
			rownum++;
		}
		wellColor = "#ccc";
		if (record.humanScores && record.humanScores[0]) {
			wellColor = record.humanScores[0].colour;
		} else if (record.softwareScores && record.softwareScores[0]) {
			wellColor = record.softwareScores[record.softwareScores.length - 1].colour;
		}
		record.images[0].color = wellColor;
		wellsHTML.push("<td><span class='well96");
		if (0 < record.microscopeImages.length) {
			wellsHTML.push(" pix");
		}
		wellsHTML.push("' style='background-color:");
		wellsHTML.push(wellColor);
		wellsHTML.push("' id='s_");
		wellsHTML.push(i);
		wellsHTML.push("_0' title='");
		wellsHTML.push(record.well);
		wellsHTML.push("'>&nbsp;</span></td>");
		count++;
		if (count > 11) {
			count = 0;
		}
		if ((barcode === record.barcode) && (initialWell === record.well)) {
			sTDL.dropIndex = i;
		}
	}
	wellsHTML.push("</tr></table>");
	$("wells").innerHTML = wellsHTML.join("");
}

/*
 * Default renderer function for well navigation, if not 96 well plate
 */
function showWellsGeneric() {
	var wellsHTML, i, record, wellColor;
	$("wells").innerHTML = "";
	wellsHTML = [];
	for (i = 0; i < wellData.records.length; i++) {
		record = wellData.records[i];
		wellColor = "#ccc";
		if (record.humanScores && record.humanScores[0]) {
			wellColor = record.humanScores[0].colour;
		} else if (record.softwareScores && record.softwareScores[0]) {
			wellColor = record.softwareScores[record.softwareScores.length - 1].colour;
		}
		record.images[0].color = wellColor;
		wellsHTML.push("<span class='well' style='background-color:");
		wellsHTML.push(wellColor);
		wellsHTML.push("' id='s_");
		wellsHTML.push(i);
		wellsHTML.push("_0' title='");
		wellsHTML.push(record.well);
		wellsHTML.push("'>");
		wellsHTML.push(record.well);
		wellsHTML.push("</span>");
		if ((barcode === record.barcode) && (initialWell === record.well)) {
			sTDL.dropIndex = i;
		}
	}
	$("wells").innerHTML = wellsHTML.join("");
}

function writeImageCache() {
	var imagesHTML = [];
	wellData.records.each(function (record) {
		imagesHTML.push("<img id='");
		imagesHTML.push(record.well);
		imagesHTML.push("_img' src='");
		imagesHTML.push(record.images[0].url);
		imagesHTML.push("' />");
	});
	$("imagecache").innerHTML = imagesHTML.join("");	
}

function renderWells(transport) {
	var numRecords;
	wellData = transport.responseJSON;
	sTDL.setData(wellData);
	$("navigation_head").innerHTML = "<a title='Details of plate: " + barcode.escapeHTML()
			+ "' href='" + contextPath + "/ViewPlate.jsp?barcode=" + encodeURIComponent(barcode) + "'>" + barcode.escapeHTML() + "<\/a>";
	//TODO add links to sample and construct
	$("wells").innerHTML = "";
	numRecords = wellData.records.length;
	writeImageCache();
	$("information").style.display = "";
	if (0 === numRecords) {
		$("wells").innerHTML = "<p style='margin:10px'>Images not available - use the link above to select another imaging session!</p>";
		$("information").style.display = "none";
	}
	else if (96 === numRecords) {
		showWells96Plate();
	} else {
		showWellsGeneric();
	}
	setTimeout(sMP.initialize.bind(sMP), 500); // force image to show.
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
	} else { // TODO XP-93 no, may have UV and visible light from same date
		record.images.sort(sortByDate);
		record.softwareScores.sort(sortByDate);
		html = [
			"<table><tr><th>Earliest</th><th>Latest</th></tr><tr><td><img id='t_0_0_tc0' width='100' src='",
			record.images[0].url,
			"'/></td><td><img id='t_0_",
			numImages - 1, 
			"_tcn' width='100' src='",
			record.images[numImages - 1].url,
			"'/></td></tr><tr class='noprint'><td colspan='2'>" +
			"<a href='#' onclick='showFullTimeCourse(); return false'>Full time course...</a>&nbsp;" +
			// "<a href='ViewAnnotations.jsp?barcode=" + record.barcode +"&well=" + record.well +
			//"' >Annotations...</a>" +
			"</td></tr></table>"
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

function fetchData() {
	//get well data TODO e.g. imageType=uv
	var url, foo;
	url = contextPath + "/TrialDropServlet?startIndex=0&results=-1&sort=well&dir=asc&barcode=" + barcode + "&inspectionName=" + (inspection ? inspection : "-");
	if (subposition) {
		url = url + "&subPosition=" + subposition;
	}
	foo = new Ajax.Request(url, {
		method: "get",
		onSuccess: function (transport) { 
			renderWells(transport); 
		},
		onFailure: function (transport) {
			notifyFailure(transport);
		}
	});
}

function fetchCurrentWellData() {
	var w, url, foo;
	w = currentWellId.split("_");
	// TODO e.g. imageType=uv
	url = contextPath + "/TrialDropServlet?startIndex=0&results=-1&sort=well&dir=asc&barcode=" + barcode + "&well=" + wellData.records[w[1]].well;
	foo = new Ajax.Request(url, {
		method: "get",
		onSuccess: function (transport) { 
			renderTimeCourseEnds(transport); 
		},
		onFailure: function (transport) {
			notifyFailure(transport);
		}
	});
}

/*
* Displaying the image, and well info
*/

function formatQuantity(quantity) {
	var q = quantity.split(" ");
	if ("kg/m3" === q[1]) {
		return (q[0] * 100) + " %w/v";
	}
	if ("m3/m3" === q[1]) {
		return (q[0] * 100) + " %v/v";
	}
	return quantity;
}

function getSaltUrl(record) {
	if (record && record.condition && record.condition.saltCondition) {
		return [
			"ViewSaltCrystals.jsp?localName=",
			record.condition.localName,
			"&well=",
			record.well
		].join("");
	}
	return null;
}
// make link to microscope image
function getPixUrl(record) {
	var pix;
	if (record) {
		pix = record.microscopeImages;
		if (0 < pix.length) {
			return [
				"ViewMicroscopeImages.jsp?barcode=",
				pix[0].barcode,
				"&well=",
				pix[0].well,
				"&date=",
				pix[0].date
			].join("");
		}
	}
	return null;
}
// make "Information" block for current well
function showWellInfo(wellId) {
	var w, record, imag, html, score, b, _url, pix;
	w = wellId.split("_");
	record = wellData.records[w[1]];
	if (!record) {
		return;
	}
	imag = record.images[w[2]];
	if (!imag.dateVar) {
		imag.dateVar = new Date(imag.date);
	}
	$("information_header").innerHTML = "Information - well " + record.well;
	// populate from wellData
	html = [];
	if (0 < record.humanScores.length) {
		score = record.humanScores[0];
	}
	else {
		score = record.softwareScores[w[2]];
	}
	html.push(formatScore(score));
	html.push("<br/>");
	html.push(imag.description);
	html.push("<br/>Taken at: ");
	html.push(imag.dateVar.toUTCString());
	html.push("<br/>Taken by: ");
	html.push(imag.instrument);
	html.push(" (");
	html.push(imag.temperature);
	html.push("&deg;C)");
	html.push("<br/>Screen: ");
	html.push(imag.screen);
	html.push("<br/>Condition: ");
	if (record.condition) {
		html.push(record.condition.localName);
		html.push(" (");
		html.push(record.condition.localNumber);
		html.push(")");
		if (record.condition.components) {
			html.push("<div style='margin-left:2em'>");
			b = false;
			record.condition.components.each(function (component) {
				if (b) {
					html.push("<br/>");
				}
				b = true;
				html.push(formatQuantity(component.quantity));
				html.push(" ");
				html.push(component.componentName);
			});
			html.push("</div>");
		}
		else {
			html.push(")<br/>Unspecified components");
		}
		_url = getSaltUrl(record);
		if (_url) {
			html.push("<br/><a href='");
			html.push(_url);
			html.push("' target='_blank'>Risk of salt crystals!</a>");
			myDV.setSalt(_url);
		}
		else {
			myDV.setSalt();
		}
	}
	else {
		html.push("Unspecified");
	}
	pix = getPixUrl(record);
	if (pix) {
		html.push("<div><a href='");
		html.push(pix);
		html.push("' target='_blank'>View Microscope Images</a></div>");
		myDV.setPix(pix);
	}
	else {
		myDV.setPix();
	}
	html.push("<div id='timecourse'>Time course: <img src='");
	html.push(contextPath);
	html.push("/skins/default/images/icons/actions/waiting.gif' alt='Loading...'/><\/div>");
	$("dropinfo").innerHTML = html.join("");
	$("timecourse").observe("mouseover", swapImageIn);
	$("timecourse").observe("mouseout", swapImageOut);
	// kick off AJAX to fetch timecourse
	// response handler to show first and last timecourse images
	fetchCurrentWellData();
}

function scrollWellWindowTo(wellId) {
	var o = $(wellId);
	if (o) {
		$("wells").scrollTop = o.offsetTop - 120;
	}
}
function scrollTCWindowTo(wellId) {
	var w = wellId;
	if (!w) {
		w = "t_" + tTDL.dropIndex + "_" + tTDL.imageIndex;
	}
	$("fulltimecourse").scrollTop = $(w).offsetTop - 120;
}
function setCurrentWell(wellId) {
	var o;
	if (!wellId) {
		wellId = "s_0_0";
		showWellInfo(wellId);
	} else if (currentWellId) {
		$("wells").down(".currentwell").removeClassName("currentwell");
	}
	o = $(wellId);
	if (o) {
		o.addClassName("currentwell");
		currentWellId = wellId;
		showImage(wellId);
	}
}
function showImageAndWellInfo(wellId) {
	setCurrentWell(wellId);
	showWellInfo(wellId);
}

function handleWellOnClick(e) {
	var el, w;
	el = e.element();
	if ("s_" === el.id.substring(0, 2)) {
		w = el.id.split("_");
		sMP.setImage(e, w[1], w[2]);
	}
	else if (sMP.isPlaying()) {
		sMP.stopMovie(e);
	}
	
}

/* Onload initialization */
function initTrialDrops(mayUpdate) {
	//alert("init");
	myresize();
	$("timecoursebox").style.display = "none";
	Event.observe(window, "resize", myresize);
	document.observe("keydown", fnmykeydown);
	$("wells").observe("click", handleWellOnClick);
	$("fulltimecourse").observe("mouseover", swapImageIn);
	$("fulltimecourse").observe("mouseout", swapImageOut);
	if (false==mayUpdate) {
		// no action
		alert("You are not allowed to annotate these images");
	} else {
		getAnnotationData("default");
		fetchData();
	}
}

/* Main Nav Movie */
var sTDL = new PIMS.xtal.TrialDropList("s");
var sMP = new PIMS.xtal.MoviePlayer("");
sMP.tdl = sTDL;
sMP.update = function (d, i, image, p) {
	var wellId, record;
	wellId = "s_" + d + "_" + i;
	setCurrentWell(wellId);
	scrollWellWindowTo(wellId);
	if (!p) {
		showWellInfo(wellId);
	}
	record = wellData.records[d];
	myDV.setSalt(getSaltUrl(record));
	myDV.setPix(getPixUrl(record));
};
sMP.beforeMovieStart = function () {
	$("information_header").innerHTML = "Information";
	$("dropinfo").innerHTML = "<img src='" + contextPath + "/images/icons/actions/waiting.gif' alt='Loading...'/>";
	if ("s_" === currentWellId.substring(0, 2)) {
		var w = currentWellId.split("_");
		sTDL.dropIndex = w[1];
		sTDL.imageIndex = w[2];
	}
	myDV.setPix();
	myDV.setSalt();
};
sMP.afterMovieStop = function () {
	if (currentWellId) {
		showWellInfo(currentWellId);
	}
};

/* Time Course Movie */
var tTDL = new PIMS.xtal.TrialDropList("t");
var tcMP = new PIMS.xtal.MoviePlayer("tc");
tcMP.delay = sMP.delay; // Share delay settings
tcMP.tdl = tTDL;
tcMP.update = function (d, i, image, p) {
	scrollTCWindowTo("t_" + d + "_" + i);
	myDV.setImage(image);
	myDV.update();
};
