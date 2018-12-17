<%--
  Brick name: barcodeSearch
  Rows: 1
  Columns: 1
  
  Let the user enter a barcode of a plate, to go to that record.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <h3>Search holders by barcode</h3>
  <div class="brickcontent">
  <form action="${pageContext.request.contextPath}/Barcode"  method="get">
	<![if !IE]><br/><![endif]>
	<input name="barcode" type="text" id="barcode" style="width: 10em; margin-right: 3px;" maxlength="25" />
	<input  type="submit" value="Search" onClick="dontWarn()" />

	<input name="classname" type="hidden" id="classname" value="org.pimslims.model.holder.Holder" />

  </form>
  </div>
