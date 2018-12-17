/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global window, $, Event, jsGraphics, Font, setAnnotation */
var PIMS;
if (!PIMS) {
	PIMS = {};
}
if (!PIMS.xtal) {
	PIMS.xtal = {};
}

/* Bodge for wz_jsgraphics-3.0.5.js compatibility with jsLint settings */
var JsGraphics = jsGraphics;
PIMS.xtal.DropView = function (id, image, initial_zoom) {
	var _image, zoom, jg, measuring, endMeasureFn, onMeasureFn, clearDcdetectFn, dcdetect, dcdetectDelay, _stopFn, dragObj, cursorStartX, cursorStartY, elementStartX, elementStartY, dragGofn, dragStopfn;
	this.id = id;
	this.zoomStep = 20;
	this.minZoom = 80;
	this.maxZoom = 400;
	
	_image = image;
	zoom = 100;
	jg = null;
	measuring = false;
	
	if (initial_zoom) {
		zoom = initial_zoom;
	}
	
	this.setImage = function (image) {
		_image = image;
	};
	this.getImage = function () {
		return _image;
	};
	
	this.getCanvas = function () {
		return jg;
	};
	this.initCanvas = function () {
		jg = new JsGraphics(this.id + "_img");
		jg.setPrintable(true);
	};
	
	this.getZoom = function () {
		return zoom;
	};
	this.zoomBy = function (delta) {
		this.zoomTo(zoom + delta);
	};
	this.zoomTo = function (z) {
		var oldZoom, newZoom, el, zel, zw, zh, k, l, t, dx, dy;
		oldZoom = 100;
		if (zoom > 0) {
			oldZoom = zoom;
		}
		newZoom = 100;
		el = $(this.id + "_img");
		if (0 < z) {
			newZoom = this.zoomStep * Math.round(z / this.zoomStep);
			if (this.minZoom && (newZoom < this.minZoom)) {
				zoom = this.minZoom;
			}
			else if (this.maxZoom && (newZoom > this.maxZoom)) {
				zoom = this.maxZoom;
			}
			else {
				zoom = newZoom;
			}
			zel = $(this.id + "_zoom");
			if ((zoom === 100) || (zoom === 200) || (zoom === 400)) {
				while (zel.options.length > 4) {
					zel.options[zel.options.length - 1] = null;
				}
			}
			else {
				zel.options[4] = new Option(zoom + "%", zoom);
			}
			zel.value = zoom;
		}
		else {
			$(this.id + "_zoom").value = "fit";
			if (! this.getImage()) {
				return;
			}
			//zw = el.parentNode.getWidth() / this.getImage().pic.width;
			//zh = el.parentNode.getHeight() / this.getImage().pic.height;
			zw = el.up().getWidth() / this.getImage().width;
			zh = el.up().getHeight() / this.getImage().height;
			if (zw > zh) {
				zw = zh;
			}
			zoom = zw * 100;
		}
		if (! this.getImage()) {
			return;
		}
		this.update();
		
		if (el && (!isNaN(oldZoom))) {
			if ((!el._imgCx) || (!el._imgCy)) {
				//el._imgCx = Math.round(this.getImage().pic.width*oldZoom/200.0);
				//el._imgCy = Math.round(this.getImage().pic.height*oldZoom/200.0);
				el._imgCx = Math.round(this.getImage().width * oldZoom / 200.0);
				el._imgCy = Math.round(this.getImage().height * oldZoom / 200.0);
			}
			k = zoom / oldZoom;
			l = parseInt(el.style.left, 10);
			t = parseInt(el.style.top, 10);
			dx = (k * el._imgCx) - el._imgCx;
			dy = (k * el._imgCy) - el._imgCy;
			l = Math.round(l - dx);
			t = Math.round(t - dy);
			if (0 < z) {
				el.style.left = l + "px";
				el.style.top = t + "px";
			}
			else {
				el.style.left = "0px";
				el.style.top = "0px";
			}
			el._imgCx = (k * el._imgCx);
			el._imgCy = (k * el._imgCy);
		}
	};
	// returns coordinates of event, in current pixels, from top left corner of uncropped image
	function event2imageXY(e, obj) {
		var o, op, ol, ot, l, t;
		o = e.element();
		while (o.parentNode && (obj.id + "_img" !== o.id)) {
			o = o.parentNode;
		}
		if (!o || document === o) {
			return [0, 0];
		}
		
		op = o;
		ol = 0;
		ot = 0;
		while (op.offsetParent) {
			op = op.offsetParent;
			ol = ol + op.offsetLeft;
			ot = ot + op.offsetTop;
		}
		l = (! o.style.left) ? 0 : parseInt(o.style.left, 10);
		t = (! o.style.top) ? 0 : parseInt(o.style.top, 10);

		return [e.pointerX() - l - ol, e.pointerY() - t - ot];
	}
	
	dcdetect = null;
	dcdetectDelay = 250;
	_stopFn = (function (e) {
		e.stop();
	}).bindAsEventListener();
	this.isMeasureGesture = function (e) {
		// TODO Allow for sticky measure- or move-mode
		if ((!e.ctrlKey) && e.isLeftClick() && (!e.shiftKey)) {
			return true;
		}
		return false;
	};
	this.clearDcdetect = function () {
		Event.stopObserving(document, "mouseup", clearDcdetectFn);
		Event.stopObserving(document, "mousemove", _stopFn);
		var d = dcdetect;
		if (d) {
			clearTimeout(d);
		}
		dcdetect = null;
	};
	this.startMeasure = function (e) {
		var d, xy;
		if ( 3==e.which || 2==e.button) {/* not a left click */ return;} 
		if (this.isMeasureGesture(e) && (!measuring)) {
			e.stop();
			d = dcdetect;
			if (d) {
				clearTimeout(d);
			}
			xy = event2imageXY(e, this);
			dcdetect = setTimeout(this.doStartMeasure.bind(this, xy), dcdetectDelay);
			Event.observe(document, "mouseup", clearDcdetectFn);
			Event.observe(document, "mousemove", _stopFn);
		}
	};
	this.alertCoordinates = function (e) {		 
		if ( 1==e.which || 1==e.button) {/* not a right click */ return;} 
		e.stop();
		var xy = event2imageXY(e, this);
		alert(this.getMicrons(xy[0])+","+this.getMicrons(xy[1]));		
		return false;
	};
	
	//TODO subtract coordinates of centre of uncropped image - use naturalWidth
	this.showCoordinates = function (e) {		 
		e.stop();
		var xy = event2imageXY(e, this);
		if (this.xElement) {
			this.xElement.value = this.getMicrons(xy[0]);
		} else {
			this.xElement=$("xElement")
		};
		if (this.yElement) {
			this.yElement.value = this.getMicrons(xy[1]);
		} else {
			this.yElement=$("yElement");
		}
	};
	this.doStartMeasure = function (xy) {
		var measureZoom, img, ruler, measureX1, measureY1, off;
		if (! measuring) {
			jg.setPrintable(false);
			measuring = true;
			measureZoom = this.getZoom() / 100.0;
			img = this.getImage();
			ruler = img.ruler;
			measureX1 = xy[0] / measureZoom;
			measureY1 = xy[1] / measureZoom;
			off = 4;
			if (!ruler) {
				img.ruler = [ measureX1, measureY1, measureX1, measureY1 ];
			} else if (ruler[0] < 0) {
				img.ruler[0] = measureX1;
				img.ruler[1] = measureY1;
				img.ruler[2] = measureX1;
				img.ruler[3] = measureY1;
			} else if (
				(measureX1 > (ruler[0] - off)) &&
				(measureX1 < (ruler[0] + off)) &&
				(measureY1 > (ruler[1] - off)) &&
				(measureY1 < (ruler[1] + off))) {
				// Over x1,y1
				ruler[0] = ruler[2];
				ruler[1] = ruler[3];
			} else if (
				(measureX1 <= (ruler[2] - off)) ||
				(measureX1 >= (ruler[2] + off)) ||
				(measureY1 <= (ruler[3] - off)) ||
				(measureY1 >= (ruler[3] + off))) {
				// Not over x2,y2
				ruler[0] = measureX1;
				ruler[1] = measureY1;
			}

			Event.observe(document, "mouseup", endMeasureFn);
			Event.observe(document, "mousemove", onMeasureFn);
			Event.stopObserving(document, "mousemove", _stopFn);
		}
	};
	this.endMeasure = function (e) {
		jg.setPrintable(true);
		this.onMeasure(e);
		measuring = false;
		Event.stopObserving(document, "mouseup", endMeasureFn);
		Event.stopObserving(document, "mousemove", onMeasureFn);
	};
	this.onMeasure = function (e) {
		var xy, measureZoom, ruler;
		e.stop();
		xy = event2imageXY(e, this);
		measureZoom = this.getZoom() / 100.0;
		ruler = this.getImage().ruler;
		ruler[2] = xy[0] / measureZoom;
		ruler[3] = xy[1] / measureZoom;
		this.update(true);
	};
	clearDcdetectFn = this.clearDcdetect.bindAsEventListener(this);
	endMeasureFn = this.endMeasure.bindAsEventListener(this);
	onMeasureFn = this.onMeasure.bindAsEventListener(this);
	
	this.zoomOnClick = function (e) {
		var xy, o;
		this.clearDcdetect();
		e.stop();
		xy = event2imageXY(e, this);
		o = $(this.id + "_img");
		o._imgCx = xy[0];
		o._imgCy = xy[1];

		if (e.ctrlKey) {
			this.zoomBy(-60);
		}
		else {
			this.zoomBy(60);
		}
	};
	
	// See http://blog.pothoven.net/2007/03/simple-draggable-html-using-prototypejs.html
	// TODO Migrate coords to, eg, dragObj._dragCx
	this.startDrag = function (e) {
		// TODO Allow for sticky measure- or move-mode
		if (e.ctrlKey && e.isLeftClick() && (!e.shiftKey)) {
			dragObj = $(this.id + "_img");
			cursorStartX = e.pointerX();
			cursorStartY = e.pointerY();
			elementStartX = parseInt(dragObj.style.left, 10);
			elementStartY = parseInt(dragObj.style.top, 10);
			Event.observe(document, "mousemove", dragGofn);
			Event.observe(document, "mouseup", dragStopfn);
			e.stop();
		}
	};
	function dragGo(e) { 
		var x, y;
		x = e.pointerX();
		y = e.pointerY();
		dragObj.style.left = (x - cursorStartX + elementStartX) + "px";
		dragObj.style.top = (y - cursorStartY + elementStartY) + "px";
		e.stop();
	}
	function dragStop(e) { 
		Event.stopObserving(document, "mousemove", dragGofn);
		Event.stopObserving(document, "mouseup", dragStopfn);
		dragObj = null;
		e.stop();
	}
	dragGofn = dragGo.bindAsEventListener();
	dragStopfn = dragStop.bindAsEventListener();
	
	this.setAnnotationData = function (html) {
		var el = $("annotation");
		if (!el) {
			document.observe("dom:loaded", this.setAnnotationData.bind(this, html));
			return;
		}
		el.replace(html);
	};
	
	document.observe("dom:loaded", this.initialize.bind(this));
};
PIMS.xtal.DropView.prototype.onzoomchange = function (e) {
	this.zoomTo(e.element().value);
};
PIMS.xtal.DropView.prototype.initialize = function () {
	// TODO Cache element look-ups and observe document.onunload to allow clean-up
	var txt, el, zel;
	txt = [
		"<h3 style='font-size:14px;margin:0;border-bottom:1px solid black' id='",
		this.id,
		"_header",
		"'> ",
		"<span class='noprint' style='float:right;'> Zoom:<select id='",
		this.id,
		"_zoom",
		"' style='font-size:80%'><option value='fit'>Fit</option><option selected='selected' value='100'>100%</option><option value='200'>200%</option><option value='400'>400%</option></select>&nbsp; Annotate:<select style='font-size:80%' id='",
		//this.id,
		//"_annotate",
		"annotation",
		"' disabled='disabled'><option>Annotate</option></select></span>",
		"<span class='noprint' style='float:right;background-color:yellow;padding:0 2px;margin-right:1px' id='",
		this.id,
		"_pix",
		"' /><a href='#'>PIX</a></span>",
		"<span class='noprint' style='float:right;background-color:yellow;padding:0 2px;margin-right:1px' id='",
		this.id,
		"_salt",
		"' /><a href='#'>!Salt!</a></span>",
		"<span id='",
		this.id,
		"_title",
		"'> Image </span> </h3><div style='position:relative;background-color:white;width:100%'><div id='",
		this.id,
		"_img",
		"' style='position:relative;left:0px;top:0px'>",
		//"&nbsp;",
		"<div style='position: absolute; left: 0px; top: 0px; width: 0px; height: 0px;'><img id='",
		this.id,
		"_im' src='#' width='0' height='0'></div>",
		"</div></div><div style='font-size:0;clear:both'>&nbsp;</div>"
	];
	$(this.id).update(txt.join(""));
	$(this.id + "_zoom").value = this.getZoom();
	this.setSalt();
	this.setPix();
	this.initCanvas();
	this.onresize();
	
	el = $(this.id + "_img");
	el.observe("mousedown", this.startMeasure.bindAsEventListener(this));
	el.observe("mousemove", this.showCoordinates.bindAsEventListener(this));
	//TODO el.observe("mouseup", this.alertCoordinates.bindAsEventListener(this));
	el.observe("dblclick", this.zoomOnClick.bind(this));
	el.up().observe("mousedown", this.startDrag.bind(this), true);
	
	zel = $(this.id + "_zoom");
	zel.observe("change", this.onzoomchange.bind(this));
	
	Event.observe(window, "resize", this.onresize.bind(this));
	document.observe("keydown", this.onkeydown.bind(this));
};
PIMS.xtal.DropView.prototype.onresize = function (e) {
	// TODO Change to use offset to remove "menu" id dependency
	var wh, el, z;
	wh = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight;
	$(this.id).style.height = wh - ($("menu").getHeight() + 20) + "px";
	$(this.id).style.overflow = "hidden";
	el = $(this.id + "_img").parentNode;
	el.style.height = $(this.id).getHeight() - $(this.id + "_header").getHeight() + "px";
	el.style.overflow = "hidden";
	z = this.getZoom();
	if (0 === z % this.zoomStep) {
		this.zoomTo(z);
	}
	else {
		this.zoomTo(-1);
	}
};
PIMS.xtal.DropView.prototype.update = function (measure) {
	var image, jg, off, z, title, _t, src, width, height, posx1, posy1, posx2, posy2, scalar, length, textx, texty;
	image = this.getImage();
	if (!image) {
		return;
	}
	if (0 >= this.getZoom()) {
		this.zoomTo(-1);
		return;
	}
	jg = this.getCanvas();
	if (!jg) {
		return;
	}
	
	off = 4; // Size of line-end box
	z = this.getZoom() / 100.0;
	
	if (!measure) {
		title = $(this.id + "_title");
		_t = image.barcode + "-" + image.well + " " + image.timePoint + " <a href=\"update/SelectCrystal?barcode="+image.barcode+"&well="+image.well+"\">Mount crystals... </a>";
		if (title.innerHTML !== _t) {
			title.innerHTML = _t;
		}
		if (image.color) {
			if (title.parentNode.style.backgroundColor !== image.color) {
				title.parentNode.style.backgroundColor = image.color;
			}
		}
		else {
			if ("" !== title.parentNode.style.backgroundColor) {
				title.parentNode.style.backgroundColor = "";
			}
		}
		
		src = image.url;
		//var width = image.pic.width * z;
		//var height = image.pic.height * z;
		width = image.width * z;
		height = image.height * z;
		this.drawImage(src, width, height);
	}
	
	jg.clear();
	if ((image.ruler !== undefined) && (image.ruler[0] >= 0)) {
		posx1 = (image.ruler[0]) * z;
		posy1 = (image.ruler[1]) * z;
		posx2 = (image.ruler[2]) * z;
		posy2 = (image.ruler[3]) * z;
		scalar = image.widthPerPixel / z;
		jg.setStroke(2);
		jg.setColor("#ff0000");
		jg.drawLine(posx1 - off, posy1 - off, posx1 + off, posy1 + off);
		jg.drawLine(posx1 - off, posy1 + off, posx1 + off, posy1 - off);
		jg.drawLine(posx2 - off, posy2 - off, posx2 + off, posy2 + off);
		jg.drawLine(posx2 - off, posy2 + off, posx2 + off, posy2 - off);
		jg.setColor("#00ff00");
		jg.drawLine(posx1, posy1, posx2, posy2);
		length = Math.round(scalar * Math.sqrt(Math.pow(posx2 - posx1, 2) + Math.pow(posy2 - posy1, 2)));
		jg.setColor("#000000");
		if (length > 0) {
			textx = 20 + Math.abs(posx2 - posx1) / 2 + Math.min(posx1, posx2);
			texty = Math.abs(posy2 - posy1) / 2 + Math.min(posy1, posy2);
			if (textx > width - 20) {
				textx -= 20;
			}
			jg.setFont("verdana", "11px", Font.BOLD);
			jg.drawString(length + "microns", textx, texty);
		}
	}
	jg.paint();
};

PIMS.xtal.DropView.prototype.getMicrons = function(pixels) {
	return Math.round(pixels * this.getImage().widthPerPixel * 100 / this.getZoom());
}

PIMS.xtal.DropView.prototype.drawImage = function (src, width, height) {
	var w, h, el, txt;
	w = parseInt(width, 10);
	h = parseInt(height, 10);
	el = $(this.id + "_im");
	if ((el.width !== w) || (el.height !== h)) {
		txt = [
			"<div style='position: absolute; left: 0px; top: 0px; width: ",
			w,
			"px; height: ",
			h,
			"px;'><img id='",
			this.id,
			"_im' src='",
			src,
			"' width='",
			w,
			"' height='",
			h,
			"' /></div>"
		];
		el.up().replace(txt.join(""));
		// TODO If we are caching els, re-cache _im here!
	}
	else if (el.src !== src) {
		el.src = src;
	}
};
PIMS.xtal.DropView.prototype.onkeydown = function (e) {
	// TODO Make configurable, eg by having this.zoomInKeyCode etc.
	if (e.altKey || e.ctrlKey) {
		return;
	}
	var key = e.which || e.keyCode;
	if (90 === key) {
		this.zoomBy(-this.zoomStep);
	} else if (88 === key) {
		this.zoomTo(100);
	} else if (67 === key) {
		this.zoomBy(this.zoomStep);
	} else if (86 === key) {
		this.zoomTo(-1);
	} else if ((48 <= key) && (57 >= key)) {
		this.annotateKey(key - 48);
	} else if ((96 <= key) && (105 >= key)) {
		this.annotateKey(key - 96);
	}
};

PIMS.xtal.DropView.prototype.annotateKey = function (key) {
	var el, opts, maxopt, opt;
	el = $("annotation");
	opts = el.options;
	maxopt = opts.length - 1;
	opt = maxopt - key;
	if ((opt >= 1) && (opt <= maxopt)) {
		setAnnotation(opts[opt].value);
	}
};

PIMS.xtal.DropView.prototype.setSalt = function (url) {
	this.setWarning($(this.id + "_salt"), url);
};

PIMS.xtal.DropView.prototype.setPix = function (url) {
	this.setWarning($(this.id + "_pix"), url);
};
PIMS.xtal.DropView.prototype.setWarning = function (el, url) {
	if (url) {
		var fc = el.firstChild;
		if (!fc.target) {
			fc.target = "_blank";
		}
		fc.href = url;
		el.style.display = "";
	}
	else {
		el.style.display = "none";
	}
};
