<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>

<head>
  <title>Writing PiMS HTML</title>
</head>

<body bgcolor="white">
  <h1>Writing PiMS HTML</h1>

    <p>For those of you who are working on web pages, or on anything that
    outputs HTML:</p>
    
    <p>All PiMS pages are supposed to validate as "XHTML 1.0 Transitional", a form of
    XML. Version 1.5 of Firefox is significantly better at validating XML
    than previous versions were, and significantly grumpier about pages that
    don't validate - so we're discovering problems that have not surfaced
    before. Some of us have already seen the dreaded "Malformed document"
    page - but wait! There's more...</p>
    
    
    <h3>We MUST ensure that we close all tags</h3>
    <p>Generally, we're quite good about closing things like divs and tables -
    it's the empty tags like input and br that we have trouble with:</p>
    <pre>
    No: &lt;br&gt;
    Yes: &lt;br /&gt;
    </pre>
    <p>In some cases - specifically script tags - the self-closing tag format
    above will cause problems with IE; use the traditional 
    <pre>
    &lt;script&gt;&lt;/script&gt;
    </pre>
    when including external javascript files, instead of 
    <pre>
    &lt;script /&gt;
    </pre>
    
    <h3>Don't forget that tags must be properly nested</h3>
    <pre>
    No: &lt;div&gt;&lt;span&gt;Some text here&lt;/div&gt;&lt;/span&gt;
    Yes: &lt;div&gt;&lt;span&gt;Some text here&lt;/span&gt;&lt;/div&gt;
    </pre>
    <p>- yes, the above is obvious, but it's easy to lose track in a complex
    page.</p>
    
    <h3>We MUST enclose attribute values in quotes</h3>
    <pre>
    No: &lt;input type=text name=foo /&gt;
    Yes: &lt;input type="text" name="foo"/&gt;
    </pre>
    
    <h3>We MUST observe rules about which tags can be nested inside others</h3>
    <p>For example, form inputs - even hidden ones - can't sit directly under a
    &lt;table&gt; tag. If they're in the table at all, they must be in a table
    cell &lt;td&gt; - even though they don't appear on the page.</p>
    
    <p>Characters such as &amp;, &lt; and &gt; MUST be escaped into HTML entities</p>
    <pre>
    No: Fish &amp; chips
    Yes: Fish &amp;amp; chips
    
    No: Home > Experiments
    Yes: Home &amp;lt; Experiments
    </pre>
    <p>Note that this includes ampersands in URL query strings! If you are
    linking to a URL with a query string, you need to be aware of this.</p>
    
    <h3>Tag and attribute names MUST be in lowercase</h3>
    <pre>
    No: &lt;FORM ACTION="foo"&gt;
    No: &lt;form ACTION="foo"&gt;
    No: &lt;FORM action="foo"&gt;
    No: &lt;Form Action="foo"&gt;
    Yes: &lt;form action="foo"&gt;
    </pre>
    
    <h3>Deprecated tags MUST NOT be used</h3>
    <pre>
    No: &lt;b&gt;
    No: &lt;font&gt;
    No: &lt;center&gt;
    Yes: Use CSS!!!!
    </pre>
    
    <p>It's a little extra effort, but it's well worth it. Not only does it
    mean that your page will actually display, not only does it mean that
    your page will behave as you expect, but also valid XHTML means that the
    CSS styles are much more likely to be applied the same way between IE
    and Firefox. Anyone who's spent any time working with CSS on both
    browsers knows what a bonus that is...</p>
    
    <p>Please make sure that you are running the latest version of Firefox -
    version 1.5.0.3 - and not a 1.0.x release. These earlier versions are
    not supported.</p>
    
    <h3>Firefox extensions</h3>
    <p>You may well find the <a href="https://addons.mozilla.org/firefox/249/">"HTML Validator" Firefox extension</a>
    helpful; it's by no means
    perfect, and it usually won't complain about unescaped characters or
    upper-case tag names, but it's very good as a quick check; it helps me
    catch my silliest mistakes along with quite a few bizarre ones. I
    strongly recommend that you install and use this extension. It will tell
    you at a glance how many HTML errors and warnings there are in your
    page; you should aim to get the number of warnings as low as possible -
    there should be zero errors. (But take heart - I routinely see over a
    hundred errors on BBC News pages.) Details on the errors and warnings
    are shown when you view the page source.</p>
    
    <p>While you're installing extensions, here are some more that I find
    useful (in descending order of usefulness):</p>
    
    <ul>
      <li><a href="https://addons.mozilla.org/firefox/35/">IE View</a> - Adds "View this page
      in IE" to your right-click menu. Much easier than finding the IE button
      and cut/pasting the URL!</li>
    
      <li><a href="https://addons.mozilla.org/firefox/1237/">QuickJava</a> - Lets you enable
      and disable Java and Javascript with a single click. The status of both
      is shown in the bottom of the browser window - no more "Did I disable
      that at some point?"</li>
    
      <li><a href="https://addons.mozilla.org/firefox/539/">MeasureIt</a> - A draggable ruler
      that lets you measure height and width of items on the page. I tend to
      use it less for measuring than for seeing if things are lined up
      perfectly.</li>
    
      <li><a href="https://addons.mozilla.org/addon.php?id=271">ColorZilla</a> - A colour
      picker, for when you can't be bothered to rummage through the source to
      find out what that colour is...</li>
    
      <li><a href="https://addons.mozilla.org/firefox/433/">FlashBlock</a> - Replaces Flash
      elements with a button you can click to play them. Won't help you with
      PiMS, but it will stop the Crazy Frog from blasting out of your speakers
      without warning when you're reading an ad-supported page about XHTML.
      (Some Flash 8 ads are getting through, though.)</li>
    
    </ul>


</body>

</html>
