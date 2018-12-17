
/*
 * Format textarea value into blocks of *block* characters A-Z only
 * whilst maintaining the cursor position within the text.
 * 
 * parameters characters-per-block, blocks-per-line, textarea
 */
 
function lineWrapAll(block, bline, textarea) { 
  var textareaName = textarea.name;
  /* alert("field name ["+textareaName+"]"); */
  if (textareaName == "seqString") {
    var browserName=navigator.appName; 
    if (browserName == "Microsoft Internet Explorer") {
      lineWrapIE(block, bline, textarea);
    } else {
      lineWrapMoz(block, bline, textarea);
    }
  }
}

function lineWrapMoz(block, bline, textarea) {

  var startPos = textarea.selectionStart;
  var start = lineWrap(block, bline, textarea.value.toUpperCase(), textarea, startPos);
  textarea.selectionStart = start;         // reset cursor
  textarea.selectionEnd = start;                            
}

function lineWrapIE(block, bline, textarea) {
  
  var range = document.selection.createRange();          // get current selection
  
  var range_all = document.body.createTextRange();     // create a selection of the whole textarea
  range_all.moveToElementText(textarea);
  
  // calculate selection start point by moving
  // beginning of range_all to beginning of range
  for (var sel_start = 0; range_all.compareEndPoints('StartToStart', range) < 0; sel_start++) {
    range_all.moveStart('character', 1);
  }
  
  var text = textarea.value.toUpperCase();
  var temp = "";
  
  for (var i = 0; i < text.length; i++) {  // strip CR (frig for IE)
    if (text.charCodeAt(i) != 13) {
      temp += text.charAt(i);
    }
  }
  
  var start = lineWrap(block, bline, temp, textarea, sel_start);
  /* alert("return ["+sel_start+","+start+"]"); */
  
  oRange = textarea.createTextRange();
  oRange.move("character", start);
  oRange.select();
}

function lineWrap(block, bline, text, textarea, startPos) {
  
  var temp = "";
  var chcount = 0;
  var blcount = 1;
  var stcount = 0;
  
  for (var i = 0; i < text.length; i++) {  // strip whitespace 
    var ch = text.substring(i, i+1);       
    if (ch >= 'A' && ch <= 'Z')  {
      temp += ch;
      if (i < startPos) {
        stcount++;
      }
  /*    if (i > 28) {
         alert("increment charcount ["+startPos+","+stcount+","+i+","+ch+"]");
      } 
    } else {
      alert("non char ["+i+","+ch.charCodeAt(0)+"]");
   */
    }      
  }
  
  /* alert("remove whitespace ["+startPos+","+stcount+"]"); */ 
  startPos = stcount;
  text = "" + temp;
  var temp = "";
  
  for (var i = 0; i < text.length; i++) {  // for each character ... 
    var ch = text.substring(i, i+1);       // first character
    if (chcount == block) {                // line has max chacters on this line
      if (blcount == bline) {              // look for new line
        temp += '\n';                      // go to next line
        blcount = 1;
      } else {
        temp += ' ';                       // go to next block
        blcount++;                         // add 1 to blcount
      }
      chcount = 0;
      if (i <= stcount) {                  // adjust cursor if a character has been added
        /* alert("increment start ["+startPos+","+temp+"]"); */
        startPos++;
      }
    }
    
    temp += ch;
    chcount++;                             // so add 1 to chcount
  }
  
  /* alert("return ["+startPos+","+temp+"]"); */
  textarea.value = temp;                   // reset form value
  return startPos;                          
}


