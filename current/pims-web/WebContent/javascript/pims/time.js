/* 
Use these functions with input elements like this:
<input  id="_isodate" type="datetime-local" 
  onchange="setDate(this.value, 'date')"
/>
<input name="date" id="date" type="number"  readonly="readonly" />
<input id="_hrdate" type="text" readonly="readonly" />
*/

// for IE8, thanks to Mozilla foundation https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/Date/toISOString
if ( !Date.prototype.toISOString ) {
    ( function() {
        function pad(number) {
            var r = String(number);
            if ( r.length === 1 ) {
                r = '0' + r;
            }
            return r;
        }
        Date.prototype.toISOString = function() {
            return this.getUTCFullYear()
                + '-' + pad( this.getUTCMonth() + 1 )
                + '-' + pad( this.getUTCDate() )
                + 'T' + pad( this.getUTCHours() )
                + ':' + pad( this.getUTCMinutes() )
                + ':' + pad( this.getUTCSeconds() )
                + '.' + String( (this.getUTCMilliseconds()/1000).toFixed(3) ).slice( 2, 5 )
                + 'Z';
        };
    }() );
}


function getClockChange() {
  var offset =  -new Date().getTimezoneOffset();
  var otherOffset = -new Date(new Date().getTime()-182*24*60*60*1000).getTimezoneOffset();
  var clockChange="No clock change";
  if (offset>otherOffset) {
    clockChange="Summer time";
  } else if (offset<otherOffset) {
    clockChange="Winter time";
  } 
  return clockChange;
}

function twoDigits(n) {
  return (n/100).toFixed(2).substr(2);
}
function getOffsetString(date) {
  var offset =  -date.getTimezoneOffset();
  if (0===offset) {return "GMT";}  
  var hours;
  if (0<offset) {
    hours = Math.floor(offset/60);
    return "GMT+"+twoDigits(hours)+twoDigits(offset-60*hours);
  }
  if (0>offset) {
    hours = Math.floor(-offset/60);
    return "GMT-"+twoDigits(hours)+twoDigits(-offset-60*hours);
  }
}

function timestampToString(millisecondsUTC) {
	var date = new Date(millisecondsUTC);
	if ('00:00:00'==date.toLocaleTimeString() || '12:00:00 AM'==date.toLocaleTimeString()	
	) {
		// PiMS legacy timestamps are at midnight
		return date.toLocaleDateString();
	}
	return date.toLocaleString()+" "+getOffsetString(date);
}

/* Convert ISO8601 local time to javascript Date. 
 * Needed because the EcmaScript5 standard says these should be parsed as UTC,
 * and HTML5 adheres to ISO8601
 * To test this, use times just before and after each clock change.
 * */
function isoLocalToDate(iso) {
	  var isoPattern = /(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d[\.\d]*)/;
	  var matches = isoPattern.exec(iso);
	  if (!matches) {
	      return new Date(Date.parse(iso));
	  }
	  return new Date(matches[1],matches[2]-1,matches[3],matches[4],matches[5],matches[6]);
}

function dateToIsoLocal(utc) {
    var minutes = utc.getTimezoneOffset();
    var guess = new Date(utc.getTime()-minutes*60*1000);
    if (guess.getTimezoneOffset()==minutes) {
        var ret = guess.toISOString();
      return ret.substring(0, ret.length-5); // strip .mmmZ
    }
    // try other season
    minutes = new Date(utc.getTime()+1000*60*60*24*182).getTimezoneOffset();
    var guess = new Date(utc.getTime()-minutes*60*1000);
    if (guess.getTimezoneOffset()==minutes) {
        var ret = guess.toISOString();
      return ret.substring(0, ret.length-5);
    }
    throw "Sorry, mistake ";
}


function setDate(iso, id) {
    var date = isoLocalToDate(iso);    
    document.getElementById(id).value = date.getTime();
    //document.getElementById('_offset'+id).value = getOffsetString(date);
    document.getElementById('_hr'+id).value = date.toString();        
}
