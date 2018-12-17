

function getData() {
    var url = contextPath + "/ImagesServlet?sort=well&dir=asc&results=100&imageType=zoomed&barcode=" + barcode;
    PIMS.xtal.asyncRequest('GET', url, {success:processData, scope:this}, ""); 
}
PIMS.xtal.addListener(window, "load", getData);

function padString(n) {
    return n < 10 ? '0' + n : n;
}

function formatDate(oData) {
    var oDate = new Date(oData);
    if ((oDate instanceof Date) && (!isNaN(oDate.getDate()))) {
        return padString(oDate.getDate()) + '\/' + padString((oDate.getMonth()+1))  + '\/' + oDate.getFullYear() + " " + padString(oDate.getHours())  + ":" + padString(oDate.getMinutes());
    } else 
    { 
        return ""; 
    }
}

var data;
function processData(transport) {
    data = PIMS.xtal.parseJson(transport.responseText);
    var i = 0;
    
    var text = new Array();   
 
    text.push("<div style='width: 100%;'>");
    
    var _imageToShow = 0;
    
    for (var d = 0; d < data.records.length; d++) {
        var t = data.records[d];
       
        text.push("<span onmouseover='showImage(" + d + ")' style='float:left;background-color:" + t['colour'] + ";padding:2px'>");
        text.push("<table class='list'><tr><td style='background-color:" + t['colour'] + ";'>");
        text.push("<img id='image" + d + "' src='" + t['url'] + "'  alt='" + t['barcode'] + "-" + t['well'] + "' width='135px'/>");
        text.push("<\/td><\/tr><tr><th style='width:135px'>" + t['barcode'] + "-" + t['well'] + "<\/th><\/tr>");
        text.push("<\/table>");
        text.push("<\/span>");
        
        if ((t['well'] == well) && (t['date'] == date)) {
        	_imageToShow = d;
        }
        i++;
    }
    //text += "<\/tr><\/table>";
    text.push("<\/div>");
    PIMS.xtal.getElement('images').innerHTML = text.join('');
    showImage(_imageToShow);
}

function showImage(t_index) {
    PIMS.xtal.getElement('imageView').src=data.records[t_index]['url'];
    PIMS.xtal.getElement('imageView').alt=data.records[t_index]['barcode'] + "-" + data.records[t_index]['well'];
    
    var text = "<table class='list'>";
    text += "<tr><th>Plate:<\/th><td><a href='" + contextPath + "/ViewPlate.jsp?barcode=" + data.records[t_index]['barcode'] + "'>" + data.records[t_index]['barcode'] + "<\/a><\/td><\/tr>";
    text += "<tr><th>Well:<\/th><td>" + data.records[t_index]['well'] + "<\/td><\/tr>";
    text += "<tr><th>Date:<\/th><td>" + formatDate(data.records[t_index]['date']) + "<\/td><\/tr>";
    
    text += "<\/table>";
    PIMS.xtal.getElement('imageInfo').innerHTML = text;
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


