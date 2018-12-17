This directory contains all the blank stylesheets you need to create for a
specific layout. To use it:

* Create a copy of it under WebContent/css/layouts 
* Rename the copy to BODYCLASS
* Rename all the files within to BODYCLASS_*.css 
* Delete the redundant readme.txt in the copy

where BODYCLASS is the value of bodyCSSClass passed into Header.jspf from your
JSP page.

The bulk of your CSS will go in the file BODYCLASS_layout_all.css - this defines 
style for all supported browsers.

Where one browser does things differently, you can override the styles defined in
BODYCLASS_layout_all.css by defining another rule in the appropriate stylesheet:

* To affect ALL versions of IE, use BODYCLASS_layout_IE.css
* To affect IE6 ONLY, use BODYCLASS_layout_IE6.css
* To affect IE7 ONLY, use BODYCLASS_layout_IE7.css
* To affect ALL non-IE browsers, use BODYCLASS_layout_nonIE.css

For print styles, similar logic applies.

Note that media attributes and conditional comments in Header.jspf take care of 
deciding which stylesheets are included and used, at the browser. You don't need
to think about that stuff, just define your rules in the right one of these
stylesheets.

The stylesheet names are prefixed with the class name. This incurs a one-time cost
of having to rename them all when copying this example, but it is repaid when 
debugging future style issues - Firebug displays only the filename when examining
CSS properties, and having the body class in the filename gives a quick way of
confirming that the correct layout-specific stylesheets are being included.