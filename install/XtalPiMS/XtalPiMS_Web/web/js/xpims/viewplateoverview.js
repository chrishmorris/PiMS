
function processData(transport){
    data = PIMS.xtal.parseJson(transport.responseText);
    fillImageList();
}

function formatDate(oDate) {
    var month = oDate.getMonth()+1;
    return oDate.getDate() + "/" + month + "/" + oDate.getFullYear();
}

function getData() {
    
    var url = contextPath + "/ImagesServlet?startIndex=0&results=-1&sort=well&dir=asc&barcode=" + barcode + "&inspectionName=" + inspection;
    PIMS.xtal.asyncRequest('GET', url, {success:processData, scope:this}, ""); 
}
PIMS.xtal.addListener(window, "load", getData);    

function fillImageList() {
    PIMS.xtal.getElement('barcode').innerHTML = barcode;
    var date = new Date();
    date.setTime(data.records[0].date);
    PIMS.xtal.getElement('date').innerHTML = formatDate(date);
    PIMS.xtal.getElement('imager').innerHTML = data.records[0].instrument + " (" + data.records[0].temperature + "\u00B0C)";
    var text = new Array();
    text.push("<table><tr><td><\/td>");
    var i = 0;
    for (var v = 1; v <= 12; v++) {
        text.push("<td style='text-align:center'>");
        text.push(v);
        text.push("<\/td>");
    }
    text.push("<\/tr><tr><td>A<\/td>");
    var c = new Array('B','C','D','E','F','G','H');
    var ci = 0;
    for (var d = 0; d < data.records.length; d++) {
        var t = data.records[d];
        if (i == 12) {
            text.push("<\/tr><tr><td>" + c[ci] + "<\/td>");
            i = 0;
            ci++;
        }
        text.push("<td style='background-color:" + t['colour'] + ";padding:2px'>");
        text.push("<a href='ViewTrialDrops.jsp?barcode=" + barcode + "&amp;name=" + inspection + "'><img id='image" + d + "' src='" + t['url'] + "' onmouseover='showImage(" + d + ")' alt='" + t['barcode'] + "-" + t['well'] + "' width='55px'/><\/a>");
        
        text.push("<\/td>");
        
        i++;
    }
    text.push("<\/tr><\/table>");
        
    PIMS.xtal.getElement('image_list').innerHTML = text.join('');
    showImage(0);
}
        
function showImage(index) {
    PIMS.xtal.getElement('imageView').src=data.records[index]['url'];
    PIMS.xtal.getElement('imageView').alt=data.records[index]['url'];
    YAHOO.util.Dom.setStyle('imageView', 'width', '450px');
    YAHOO.util.Dom.setStyle('imageHolder', 'backgroundColor', data.records[index]['colour']);
}
