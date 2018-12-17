<%-- @Author Petr Troshin aka pvt43 --%>
<!--
 * PIMS-110
 * check that the search form has an entry in either search_all or search_by_attribute field
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>


This is obsolete, use 2 different forms so the error cannot happen 
<script type="text/javascript">
function validSearch(form) {

  var searchAll = new Boolean(false);
  var searchAttribute = new Boolean(false);

  if (form.search_all.value != "")
    searchAll=true;

  for (i=0; i<form.elements.length; i++) {
    /* alert("searchAttribute ["+i+","+form.elements[i].type+","+form.elements[i].name+","+form.elements[i].value+"]"); */
    if (form.elements[i].type != "hidden" &&
        form.elements[i].name != "search_all" &&
        form.elements[i].name != "ACTION" &&
        form.elements[i].name != "SUBMIT" &&
        form.elements[i].value != ""&&form.elements[i].value != "[none]") {
           searchAttribute = true;
           /* alert("setting searchAttribute ["+myForm.elements[i].name+","+myForm.elements[i].value+"]"); */
    }
  }
  /* alert("searchAll ["+searchAll+"] searchAttribute ["+searchAttribute+"]"); */

  if (searchAll == true && searchAttribute == true) {
    alert("Please choose either to search All OR search by attribute");
    return false;
  }
  return true;
}
</script>
<%-- This is obsolete, use 2 different forms so the error cannot happen --%>
