var delay = 0.25;


alert("viewtimecourse.js is believed obsolete");

function processData(transport){
    data = PIMS.xtal.parseJson(transport.responseText);
    fillImageList();
}


function getTimeCourse() {
    
    var url = contextPath + "/ImagesServlet?startIndex=0&results=-1&sort=date&dir=asc&barcode=" + barcode + "&well=" + well;
    PIMS.xtal.asyncRequest('GET', url, {success:processData, scope:this}, ""); 
}
PIMS.xtal.addListener(window, "load", getTimeCourse);

function fillImageList() {
   
    var i = 0;
    
    var text = new Array();
    var t = data.records[0];
    
    text.push("<div>");
    text.push("<span style='float:left;background-color:");
    text.push(t.colour);
    text.push(";padding:2px'>");
    text.push("<table class='list'><tr><td style='background-color:");
    text.push(t.colour);
    text.push(";'>");
    text.push("<img onmouseover='showImage(0)' id='image0' src='");
    text.push(t.url);
    text.push("' alt='");
    text.push(t.barcode);
    text.push("-");
    text.push(t.well);
    text.push("' width='135px'/><\/td><\/tr>");
    
    if (t.inspectionName === inspection) {
        text.push("<tr><th style='width:135px;background-color=#006;color:#fff'>Time Zero<\/th><\/tr>");
    } else {
        text.push("<tr><th style='width:135px;'>Time Zero<\/th><\/tr>");
    }
    
    text.push("<tr><td><img width='16' height='16' style='border: 0pt none ; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;' title='Previous image in sequence' alt='Prev' src='" + contextPath + "/xtal/images/16x16/previous.png'  onclick='prevImage();' />");
    text.push("<img width='16' height='16' style='border: 0pt none ; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;' title='Play Sequence' alt='Play' src='" + contextPath + "/xtal/images/16x16/player_play.png'  onclick='startMovie();' id='play' />");
    text.push("<img width='16' height='16' style='border: 0pt none ; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom; display: none;' title='Stop Sequence' alt='Stop' src='" + contextPath + "/xtal/images/16x16/player_stop.png' onclick='stopImages();' id='stop' />");
    text.push("<img width='16' height='16' style='border: 0pt none ; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;' title='Next image in sequence' alt='Next' src='" + contextPath + "/xtal/images/16x16/next.png' onclick='nextImage();' />");
    text.push("<\/td><\/tr>");
    text.push("<tr><td>Delay:");
    text.push("<img width='16' height='16' src='" + contextPath + "/xtal/images/16x16/minus.png' onclick='decreaseDelay()' alt='-' title='Reduce time between images for playback' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;' />");
    text.push("<img width='16' height='16' src='" + contextPath + "/xtal/images/16x16/plus.png' onclick='increaseDelay()' alt='+' title='Increase time between images for playback' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;' />");
    text.push("(<span id='playerDelay'>" + delay + "</span>s)");
    
    text.push("<\/span>");
    text.push("<\/span>");
    text.push("<\/div>");
    PIMS.xtal.getElement('timezero').innerHTML = text.join('');
    text = new Array();
    //text.push("<div style='width:" + (data.records.length)*150 + "px;'>");
    text.push("<div style='width: 100%'>");
    
    for (var d = 1; d < data.records.length; d++) {
        t = data.records[d];
        
        text.push("<span onmouseover='showImage(" + d + ")' style='float:left;background-color:" + t['Color'] + ";padding:2px'>");
        text.push("<table class='list'><tr><td style='background-color:" + t['Color'] + ";'>");
        text.push("<img id='image" + d + "' src='" + t['url'] + "'  alt='" + t['barcode'] + "-" + t['well'] + "' width='135px'/>");
        text.push("<\/td><\/tr><tr><th style='width:135px'>Time:<\/th><\/tr><tr>");
        if (t['inspectionName'] === inspection) {
            text.push("<td style='width:135px;background-color:#006;color:#fff'>" + t['timePoint'] + "<\/td><\/tr>");
        } else {
            text.push("<td style='width:135px'>" + t['timePoint'] + "<\/td><\/tr>");
        }
        
        text.push("<\/table>");
        text.push("<\/span>");
        i++;
    }
    text.push("<\/div>");
    PIMS.xtal.getElement('images').innerHTML = text.join('');
    showImage(0);
}
function formatDate(oDate) {
    var month = oDate.getMonth()+1;
    return oDate.getDate() + "/" + month + "/" + oDate.getFullYear();
}
function showImage(t_index) {
    PIMS.xtal.getElement('imageView').src=data.records[t_index]['url'];
    PIMS.xtal.getElement('imageView').alt=data.records[t_index]['url'];
    YAHOO.util.Dom.setStyle('imageHolder', 'backgroundColor', data.records[t_index]['colour']);

    var text = new Array();
    text.push("<table class='list'>");
    text.push("<tr><th>Plate:<\/th><td><a href='" + contextPath + "/ViewPlate.jsp?barcode=" + barcode + "'>" + barcode + "<\/a><\/td><\/tr>");
    text.push("<tr><th>Well:<\/th><td>" + well + "<\/td><\/tr>");
    text.push("<tr><th>Imaging System: <\/th><td>" + data.records[t_index]['instrument'] + "<\/td><\/tr>");
    var date = new Date();
    date.setTime(data.records[t_index]['date']);
    text.push("<tr><th>Date:<\/th><td>" + formatDate(date) + "<\/td><\/tr>");
    text.push("<tr><th>Age:<\/th><td>" + data.records[t_index]['timePoint'] + "<\/td><\/tr>");
    if (data.records[t_index]['screen'] != undefined) {
        
        text.push("<tr><th>Screen:<\/th><td>" + data.records[t_index]['screen'] + "<\/td><\/tr>");
        if (data.records[t_index]['condition'] != undefined) {
            text.push("<tr><th>Condition:<\/th><td>" + data.records[t_index]['condition'] + "<\/td><\/tr>");
            if (data.records[t_index]['chemicals'] != undefined) {
                text.push("<tr><th>Chemicals<\/th><td><div style='float:left;'><table><tr><th>Chemical<\/th><th>Quantity<\/th><\/tr>");
                for (var h=0; h<data.records[t_index]['chemicals'].length; h++) {
                    text.push("<tr><td>" + data.records[t_index]['chemicals'][h].name + "<\/td><td>" + data.records[t_index]['chemicals'][h].quantity + "<\/td><\/tr>");
                }
                text.push("<\/table><\/div><\/td><\/tr>");
            }
        }
    }
    if (data.records[t_index]['microImages'] != undefined) {
        
        text.push("<tr><th>MicroImages<\/th><td><div style='width:" + (data.records[t_index]['microImages'].length)*80 + "px;'>");
        
        for (var d = 0; d < data.records[t_index]['microImages'].length; d++) {
            var m = data.records[t_index]['microImages'][d];
            
            text.push("<span style='float:left;width:70px;padding:1px'><a href='ViewMicroscopeImages.jsp?barcode=" + m.barcode + "'>");
            text.push("<img id='microimage" + d + "' src='http://www.oppf.ox.ac.uk/vault/images/micro/" + m.url + "'  alt='" + m.barcode + "-" + m.well + "' style='width:70px;' /><\/a><\/span>");
        }        
        
        text.push("<\/div><\/td><\/tr>");           
    }
    
    text.push("<\/table>");
    PIMS.xtal.getElement('imageInfo').innerHTML = text.join('');
    index = t_index;
}

function nextImage() {
    index++;
    if (index === data.records.length) {
        index = data.records.length - 1;
    }
    showImage(index);
}

function prevImage() {
    index--;
    if (index < 0) {
        index = 0;
    }
    showImage(index);
}
// 1 => Stop requested
var stopFlag = 0;
var timeoutid = 0;
// Stop the movie
function pause() {
    //console.profileEnd();
    //console.timeEnd('SpeedTest2');
    if (timeoutid !== 0)  {
        clearTimeout(timeoutid);
        timeoutid = 0;
        PIMS.xtal.getElement('play').style.display = "";
        PIMS.xtal.getElement('stop').style.display = "none";
        stopFlag = 0;
    }
}

// Loop over all the regions in all the images
function loop() {
    
    if ((index >= (data.records.length - 1)) || (1 == stopFlag)) {
        pause();
        return;
    }
    else {
        nextImage();            
    }
    
    
    playImages(); 
}

// Start the movie
function startMovie() {
    stopFlag = 0;
    PIMS.xtal.getElement('play').style.display = "none";
    PIMS.xtal.getElement('stop').style.display = "";
    playImages();
}

// Start the timer that will fire the event that will move us to the next frame
function playImages() {
    timeoutid = window.setTimeout(loop, delay * 1000 + 0.01);
}

// Stop the movie
function stopImages() {
    stopFlag = 1;
}

function decreaseDelay() {
    delay -= 0.25;
    if (delay < 0) { 
        delay = 0;
    }
    
    PIMS.xtal.getElement('playerDelay').innerHTML = delay;
}
function increaseDelay() {
    delay += 0.25;
    if (delay > 5) {
        delay = 5;
    }
    
    PIMS.xtal.getElement('playerDelay').innerHTML = delay;
}