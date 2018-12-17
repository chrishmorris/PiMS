function openModalWindow(url,title){
  showModalWindow();
  var modalWindow=$("modalWindow_window");
  $("modalWindow_window_iframe").src=url;
  $("modalWindow_window_head").innerHTML=title;
  $("modalWindow_window_iframe").setStyle({
    height:(modalWindow.getHeight()-$("modalWindow_window_head").getHeight()-15)+"px",
    width:modalWindow.getWidth()-2+"px"
  });
  return false;
}
function openModalDialog(content,title){
  showModalDialog();
  $("modalWindow_dialog_body").innerHTML=content;
  $("modalWindow_dialog_head").innerHTML=title;
}

function showUpdatingModalDialog(){
	openModalDialog("<img src='"+contextPath+"/skins/default/images/icons/actions/waiting.gif' alt='' />"
	    +" Updating..."
	  	,"Saving changes"
	);
}

function showModalWindow(){
  var offsets=document.viewport.getScrollOffsets();
  $("modalWindow_mask").setStyle({ top:offsets["top"]+"px"});
  $("modalWindow_mask").style.display="block";
  $("modalWindow_window").style.display="block";
}
function showModalDialog(){
  $("modalWindow_mask").style.display="block";
  $("modalWindow_dialog").style.display="block";
}

function closeModalDialog(){
	closeModalWindow();
}
function closeModalWindow(){
  if(!$("modalWindow_mask")){
    return false;
  }
  $("modalWindow_window").style.display="none";
  $("modalWindow_dialog").style.display="none";
  $("modalWindow_mask").style.display="none";
  $("modalWindow_window_iframe").src=contextPath+"/skins/default/images/icons/actions/waiting.gif";
}

function showQuickSearch(){
	  if(!$("modalWindow_mask")){
		  window.location.href=contextPath+"/Search";
		  return false;
	  }
	
	  $("modalWindow_mask").style.display="block";
	  $("quickSearch_window").style.display="block";
	  var modalWindow=$("quickSearch_window");
	  $("quickSearch_window_iframe").src=contextPath+"/JSP/homepage-bricks/quickSearch_popup.jsp";
	  $("quickSearch_window_iframe").setStyle({
	    height:(modalWindow.getHeight()-$("quickSearch_window_head").getHeight()-15)+"px",
	    width:modalWindow.getWidth()-2+"px"
	  });
	  return false;
}

function closeQuickSearchWindow(){
	  if(!$("modalWindow_mask")){
		    return false;
		  }
	  $("quickSearch_window").style.display="none";
	  $("modalWindow_mask").style.display="none";
	  $("modalWindow_window_iframe").src=contextPath+"/skins/default/images/icons/actions/waiting.gif";
}
