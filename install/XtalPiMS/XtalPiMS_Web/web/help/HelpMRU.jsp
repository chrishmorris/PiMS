<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp"><jsp:param name="extraStylesheets" value="helppage" /> 
    <jsp:param name="HeaderName" value='History help' />
</jsp:include>

<div class="help">
</div> <!--end div help-->

<h3 id="navigation">History</h3>
<div class="helpcontents">
<p>Your <em>History</em> is the more recently used items by users.
The most recently used one is always on the top.</p>
<p>Sometimes PiMS will have to clear your history, for example when it is recovering from an error.</p>
<h4 id="navigation">Last 10 history items in menu bar</h4>
	<img class="imageNoFloat"
		src="../images/helpScreenshots/mru-menu-bar.png" />

<h4 id="navigation">Last 10 history items in frontpage</h4>
	<img class="imageNoFloat"
		src="../images/helpScreenshots/mru-brick.png" />
		
<h4 id="navigation">Full history list</h4>
	<div class="textNoFloat">
	For the full history, you can:
	<ul>
		<li>click the 'History' menu 
		<li>click the link,'more...', when you have more than 10 items in your history
	</ul>
	<img class="imageNoFloat"
		src="../images/helpScreenshots/mru-full-list.png" alt="Sample list result" />
	</div>
	
<div class="toplink"><a href="#">Back to top</a></div>


<jsp:include page="/JSP/core/Footer.jsp" />
