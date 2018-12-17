/*jslint bitwise: true, browser: true, eqeqeq: true, newcap: true, onevar: true, regexp: true, undef: true, white: true */
/*global window, $, Event, jsGraphics, Font, setAnnotation */
/*global $, PIMS */
PIMS.xtal.TrialDropList = function (tdlId) {
	this.id = tdlId;
	this.data = null;
	this.dropIndex = 0;
	this.imageIndex = 0; // image profile, e.g. UV
};
PIMS.xtal.TrialDropList.prototype.setData = function (data) {
	this.data = data;
	this.dropIndex = 0;
	this.imageIndex = 0;
};
PIMS.xtal.TrialDropList.prototype.countDrops = function () {
	return this.data.records.length;
};
PIMS.xtal.TrialDropList.prototype.countImages = function () {
	var d = this.getCurrentDrop();
	if (d) {
		return d.images.length;
	}
	return 0;
};
PIMS.xtal.TrialDropList.prototype.getDrop = function (index) {
	return this.data.records[index]; //TODO XP-93 note may contain more than one image
};
PIMS.xtal.TrialDropList.prototype.getImage = function (dIndex, iIndex) {
	return this.getDrop(dIndex).images[iIndex];
};
PIMS.xtal.TrialDropList.prototype.getCurrentDrop = function () {
	return this.getDrop(this.dropIndex);
};
PIMS.xtal.TrialDropList.prototype.getCurrentImage = function () {
	var d = this.getCurrentDrop();
	if (d) {
		return d.images[this.imageIndex];
	}
	return null;
};
PIMS.xtal.TrialDropList.prototype.hasNext = function () {
	if ((this.imageIndex < this.countImages() - 1) ||
		(this.dropIndex < this.countDrops() - 1)) {
		return true;
	}
	return false;
};
PIMS.xtal.TrialDropList.prototype.hasPrev = function () {
	if ((this.imageIndex > 0) ||
		(this.dropIndex > 0)) {
		return true;
	}
	return false;
};
PIMS.xtal.TrialDropList.prototype.moveNext = function () {
	if (this.imageIndex < this.countImages() - 1) {
		this.imageIndex++;
	} else if (this.dropIndex < this.countDrops() - 1) {
		this.dropIndex++;
		this.imageIndex = 0;
	}
};
PIMS.xtal.TrialDropList.prototype.movePrev = function () {
	if (this.imageIndex > 0) {
		this.imageIndex--;
	} else if (this.dropIndex > 0) {
		this.dropIndex--;
		this.imageIndex = this.countImages() - 1;
	}
};
PIMS.xtal.TrialDropList.prototype.moveFirst = function () {
	this.dropIndex = 0;
	this.imageIndex = 0;
};
PIMS.xtal.TrialDropList.prototype.moveLast = function () {
	this.dropIndex = this.countDrops() - 1;
	this.imageIndex = this.countImages() - 1;
};

PIMS.xtal.MoviePlayer = function (mpId, tdl) {
	this.id = mpId;
	this.tdl = tdl;
	this.delay = {};
	this.delay.val = 0.3;
	this.delay.step = 0.1;
	this.delay.min = 0.1;
	this.delay.max = 4;
	var interval = null;
	
	function nextFrame() {
		if (this.tdl.hasNext()) {
			this.tdl.moveNext();
			this._update();
		}
		if (!this.tdl.hasNext()) {
			this.stopMovie();
		}
	}
	
	this.startMovie = function (e) {
		if (e) {
			Event.stop(e);
		}
		if (!this.tdl.hasNext()) {
			this.tdl.moveFirst();
		}
		if (this.tdl.hasNext()) {
			this._beforeMovieStart();
			if (interval) {
				clearInterval(interval);
			}
			interval = setInterval(nextFrame.bind(this), this.delay.val * 1000);
		}
	};
	this.stopMovie = function (e, skipAfter) {
		if (e) {
			Event.stop(e);
		}
		if (interval) {
			clearInterval(interval);
			interval = null;
		}
		if (!skipAfter) {
			this._afterMovieStop();
		}
	};
	this.toggleMovie = function (e) {
		if (e) {
			Event.stop(e);
		}
		if (interval) {
			this.stopMovie();
		}
		else {
			this.startMovie();
		}
	};
	this.isPlaying = function () {
		return (null !== interval);
	};
	this._startMovieFn = this.startMovie.bindAsEventListener(this);
};
PIMS.xtal.MoviePlayer.prototype.initialize = function () {
	this.setPlayerIconStates();
	$(this.id + "playerfirst").observe('click', this.moveFirst.bind(this));
	$(this.id + "playerprev").observe('click', this.movePrev.bind(this));
	$(this.id + "playernext").observe('click', this.moveNext.bind(this));
	$(this.id + "playerlast").observe('click', this.moveLast.bind(this));
	$(this.id + "delayup").observe('mousedown', this.changeDelay.bind(this, 1));
	$(this.id + "delaydown").observe('mousedown', this.changeDelay.bind(this, -1));
	//$(this.id + "delayup").observe('mouseup', this.finishSetDelay.bind(this));
	//$(this.id + "delaydown").observe('mouseup', this.finishSetDelay.bind(this));
	this.changeDelay(0);
	this._afterMovieStop();
	this._update();
};
PIMS.xtal.MoviePlayer.prototype._update = function () {
	this.setPlayerIconStates();
	this.update(this.tdl.dropIndex, this.tdl.imageIndex, this.tdl.getCurrentImage(), this.isPlaying());
};
PIMS.xtal.MoviePlayer.prototype.update = function (dropIndex, imageIndex, image, playing) {
	if (console && console.log) {
		console.log("update: [" + dropIndex + "," + imageIndex + "] = " + image.url);
	}
};
PIMS.xtal.MoviePlayer.prototype.setPlayerIconStates = function () {
	$(this.id + "playerfirst").removeClassName("playericondisabled");
	$(this.id + "playerprev").removeClassName("playericondisabled");
	$(this.id + "playernext").removeClassName("playericondisabled");
	$(this.id + "playerlast").removeClassName("playericondisabled");
	if (!this.tdl.hasPrev()) {
		$(this.id + "playerfirst").addClassName("playericondisabled");
		$(this.id + "playerprev").addClassName("playericondisabled");
	}
	if (!this.tdl.hasNext()) {
		$(this.id + "playernext").addClassName("playericondisabled");
		$(this.id + "playerlast").addClassName("playericondisabled");
	}
};
PIMS.xtal.MoviePlayer.prototype._beforeMovieStart = function () {
	$(this.id + "startmovie").stopObserving('click', this._startMovieFn);
	document.onclick = this.stopMovie.bindAsEventListener(this);
	$(this.id + "controls").select("input").each(function (elem) {
		elem.disabled = "disabled";
	});
	$(this.id + "startmovie").addClassName("playericonstop");
	//this._update();
	this.beforeMovieStart();
};
PIMS.xtal.MoviePlayer.prototype.beforeMovieStart = function () {};
PIMS.xtal.MoviePlayer.prototype._afterMovieStop = function () {
	$(this.id + "startmovie").removeClassName("playericonstop");
	$(this.id + "controls").select("input").each(function (elem) {
		elem.disabled = "";
	});
	document.onclick = function () {};
	$(this.id + "startmovie").observe('click', this._startMovieFn);
	this.afterMovieStop();
};
PIMS.xtal.MoviePlayer.prototype.afterMovieStop = function () {};
PIMS.xtal.MoviePlayer.prototype.moveFirst = function (e) {
	if (this.tdl.hasPrev()) {
		this.stopMovie(e, !this.isPlaying());
		this.tdl.moveFirst();
		this._update();
	}
};
PIMS.xtal.MoviePlayer.prototype.movePrev = function (e) {
	if (this.tdl.hasPrev()) {
		this.stopMovie(e, !this.isPlaying());
		this.tdl.movePrev();
		this._update();
	}
};
PIMS.xtal.MoviePlayer.prototype.moveNext = function (e) {
	if (this.tdl.hasNext()) {
		this.stopMovie(e, !this.isPlaying());
		this.tdl.moveNext();
		this._update();
	}
};
PIMS.xtal.MoviePlayer.prototype.moveLast = function (e) {
	if (this.tdl.hasNext()) {
		this.stopMovie(e, !this.isPlaying());
		this.tdl.moveLast();
		this._update();
	}
};
PIMS.xtal.MoviePlayer.prototype.setImage = function (e, dropIndex, imageIndex) {
	this.stopMovie(e, true);
	this.tdl.dropIndex = dropIndex;
	this.tdl.imageIndex = imageIndex;
	this._update();
};
PIMS.xtal.MoviePlayer.prototype.increaseDelay = function () {
	this.changeDelay(1);
};
PIMS.xtal.MoviePlayer.prototype.decreaseDelay = function () {
	this.changeDelay(-1);
};
PIMS.xtal.MoviePlayer.prototype.changeDelay = function (steps) {
	var s, d;
	s = Math.round(steps);
	d = this.delay.val;
	d *= 1000;
	d += (s * 1000 * this.delay.step);
	d = d / 1000;
	if (d < this.delay.min) {
		this.delay.val = this.delay.min;
	} else if (d > this.delay.max) {
		this.delay.val = this.delay.max;
	}
	else {
		this.delay.val = d;
	}
	$(this.id + "delay").value = this.delay.val;
};
PIMS.xtal.MoviePlayer.prototype.finishSetDelay = function () {
	//nothing at present - cancel interval for key repeat
};
