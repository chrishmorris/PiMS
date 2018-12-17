<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
  <jsp:param name="HeaderName" value='Help on something' />
  <jsp:param name="extraStylesheets" value="helppage" /> 
  </jsp:include>

<c:catch var="error">

<%-- Set up variables for page title. --%>
<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/functions/Help.jsp">Guide to using PiMS</a>
</c:set>
<c:set var="icon" value="default.png" />
<c:set var="title" value="Something Help"/>

<%-- the page title --%>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" icon="${icon}" />

<div class="help">
  <pimsWidget:box initialState="fixed" title="Overview">
    <div class="help">
      Overview
    </div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Contents">
    <div class="leftcolumn">
      <ul>
        <li><a href="#head1">First heading</a>
          <ul>
            <li><a href="#subhead1">Sub-heading 1</a>
             <ul>
              <li><a href="#subsubhead">Sub sub-heading</a></li>
             </ul></li>
            <li><a href="#subhead2">Sub-heading 2</a></li>
          </ul></li>
        </ul>
    </div>
    <div class="rightcolumn">
      <ul>
        <li><a href="#head2">Second heading</a></li>
      </ul>
    </div>
    <div class="shim"></div>
  </pimsWidget:box>
  <pimsWidget:box initialState="fixed" title="Related Help">
    <ul>
      <li><a href="">A link</a> to another help page</li>
    </ul>
  </pimsWidget:box>
</div>

<%-- the page contents --%>
<div class="helpcontents">
  <!-- ================================================================================ -->
  <h3 id="head1">First heading</h3>
  Text here<br />
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/anImage.png" alt="an image" />
  
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ................................................................................ -->
  <h4 id="subhead1">Sub-heading 1</h4>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/anotherImage.png" alt="Another image" />
  <br /><br />
  <ul>
    <li>list item</li>
    <li>another list item.</li>
  </ul>
  <!-- ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''' -->
   <h5 id="subsubhead">Sub sub-heading</h5>
  
  <div class="toplink"><a href="#">Back to top</a></div>
  
  <!-- ................................................................................ -->
  <h4 id="subhead2">Sub-heading 2</h4>
  Some text. 
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/yetAnotherImage.png" alt="yet another image" />
  <br /><br />
  <ul>
    <li>another list</li>
  </ul>
  <img class="imageNoFloat" src="${pageContext.request.contextPath}/images/helpScreenshots/andAnother.png" alt="and another image" />
  <br /><br />
  <ul>
    <li>Last list.
      <br />
      &nbsp; <em>-some italicised text</em>
      <br /><br />
    </li>
    <li><strong>bold</strong>
      <br /><br />
    </li>
    <li>finally.</li>
  </ul>
  <div class="toplink"><a href="#">Back to top</a></div>

  <!-- ================================================================================ -->
  <h3 id="head2">Template second heading</h3>
  <div class="toplink"><a href="#">Back to top</a></div>

</div>

<%-- page footer --%>
</c:catch>
<c:if test="${error != null}">"/>
  <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

