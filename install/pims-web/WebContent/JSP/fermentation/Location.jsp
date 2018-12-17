<%--
Fermantation culture tab page
@author Petr Troshin aka pvt43
July 2008
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>


  <tr>
      <td>Location obsolete<span class="required">*</span></td>
      <td>
		<select name="loc1" id="loc1">
				<pims:list version="${version}" attributes="name" metaClassName="org.pimslims.model.location.Location" selected="${construct.location1}" />       
  	   	<option value="">Add new...</option>
      </select>
      </td>
      
      <td>Tower</td>
      <td>
			<select name="loc1" id="loc1">
				<pims:list version="${version}" attributes="name" metaClassName="org.pimslims.model.location.Location" selected="${construct.location1}" />       
  	   	<option value="">Add new...</option>
      </select>
      </td>
      
      <td>Box</td>
      <td>
      <select name="box" id="box">
	  		<pims:list version="${version}" attributes="name" metaClassName="org.pimslims.model.holder.Holder" selected="${construct.box1}"  />  
  			<option value="">Add new...</option>
      </select>
      TODO add
      </td>
    </tr>
    

<!-- OLD -->
