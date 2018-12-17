<%--
  Brick name: sequencingOrders
  Rows: 1
  Columns: 1
  
  Display sequencing orders, and link to create a new one

--%>
  <h3 style="margin-bottom:0.25em">Sequencing orders</h3>
  <div class="brickcontent" >
  <form action="${pageContext.request.contextPath}/read/SearchSequencingOrders"  method="get" style="margin:0.25em;">
		<input title="Order ID:" name="soid" type="text" id="soid" style="width: 9em" maxlength="25" />
		<input  type="submit" value="Search" onclick="dontWarn()" />
  </form>
  &nbsp;&nbsp;<a href="${pageContext.request.contextPath}/update/CreateSequencingOrder">Put a New Order</a>
  </div>

