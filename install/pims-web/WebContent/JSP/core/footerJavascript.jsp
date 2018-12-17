<!-- footerJavascript.jsp -->
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%-- scripts for responding to events --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/validation5.0.0.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/spot.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/tooltip.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/protocol.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/ajax4.4.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/modalWindow4.4.0.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/lib/chosen.proto.min.js" ></script>

<form  id="dummy_deleteform" method="post" style="display:none"
    action="${pageContext.request.contextPath}/update/DeleteAndBackToReferer">
    <input type="hidden" name="hook" value="" />
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/update/DeleteAndBackToReferer')}" />
</form>
<form  id="dummy_removeform" method="post" style="display:none" 
    action="${pageContext.request.contextPath}/update/AjaxRemove/" >
    <input type="hidden" name="from" value="" />
    <input type="hidden" name="role" value="" />
    <input type="hidden" name="removed" value="" />
</form>
<form action="#" id="dummy_copyform" method="post" style="display:none">&nbsp;</form>
<script type="text/javascript" >
var ajaxDeleteCSRF = '${utils:csrfToken(pageContext.request,'/Delete')}';
<%-- could var ajaxUpdateCSRF = '${utils:csrfToken(pageContext.request,'/update/AjaxUpdate')}'; --%>
function contextmenu_delete(name,hook){
  if(confirm('Are you sure you want to delete '+name+'?')){ 
    $("dummy_deleteform")['hook'].value = hook;
    $("dummy_deleteform").submit();
  }
}

function copy_object(hook){
  if(confirm('Are you sure you want to create a copy of this record?')){ 
    $("dummy_copyform").action=contextPath+"/update/Copy/"+hook;
    $("dummy_copyform").submit();
  }
}
// see pimsWidget:select
Prototype.Selector.select('.chosen-select').forEach(
    function(element) {new Chosen(element,  {inherit_select_classes: true, width: "98%"});}
);

//TODO  new Chosen($('ms'), {inherit_select_classes: true});
<%-- was $$("img.qrcode").each(function(qr){
-	qr.src=qr.src.replace("%5b%5bCURRENTPAGE%5d%5d",encodeURIComponent(document.location));
-}); --%>
</script>
  