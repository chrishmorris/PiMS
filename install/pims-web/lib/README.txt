Put jar files in here for external components you use
which do not need to be in the delivered .war file.
This is on the local classpath for your project.
Components that are needed at run time should be in
   WebContent/WEB-INF/lib/
instead.

Not all of these jar files are needed on the classpath in eclipse.
Some are for build.xml only.

Following up on http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-58 indicates that there are several jars in pims-web/lib that are unnecessary. I have categorised the jars as follows:

1. Required on eclipse classpath, required by web application but provided by tomcat (in common/lib):

  servlet-api.jar       
  jsp-api.jar           

2. Required on eclipse classpath, required by web application but duplicated in pims-web/WebContent/WEB-INF/lib:

  axis.jar              => org.pimslims.servlet.recordfetch.DBFetch
  jaxrpc.jar            => org.pimslims.servlet.recordfetch.RequestType

3. Required on eclipse classpath but not required by web application:

  csvutils.jar          => many classes in org.pimslims.command, providing they are not called from the web-app
  httpunit.jar          => test classes
  jericho-html-1.4.jar  => org.pimslims.command.targets.LeedsHTMLTargetsParser, providing they are not called from the web-app
  junit.jar             => test classes

4. Required during war and compileTests build:

  jasper-compiler.jar   => required to build the jsps for the war
  jasper-runtime.jar    => required to build the jsps for the war
  commons-el.jar        => required to build the jsps for the war

5. Probably required by HttpUnit runtime (see Apendix A - HttpUnit dependencies)

  js.jar                => testing javascript - see http://httpunit.sourceforge.net/doc/manual/index.html and http://httpunit.sourceforge.net/doc/faq.html#norhino
  Tidy.jar              => see http://httpunit.sourceforge.net/doc/manual/index.html

6. Not required on eclipse classpath for building but may be runtime required and duplicated in pims-web/WebContent/WEB-INF/lib:

  activation.jar        => required by axis to handle errors thrown by EBI web services
  commons-discovery.jar => required by axis runtime
  saaj.jar              => required by axis runtime


Appendix A - HttpUnit dependencies [ http://httpunit.sourceforge.net/doc/manual/index.html ]:

  Runtime:
  ========
  
     Jar             Have       Description
  nekohtml.jar        N   - HTML parsing - Very tolerant of sloppy HTML - Requires xerces-j 2.2 or higher - http://www.apache.org/~andyc/neko/doc/html/index.html
  tidy.jar            Y   - HTML parsing - Very picky - Works with any jaxp-compliant parser - http://lempinen.net/sami/jtidy/
  xmlParserAPIs.jar   N   - not required JDK >= 1.5.0? - aka xml-apis.jar - the generic parser APIs supported by xerces-j - http://xml.apache.org
  xercesImpl.jar      N   - not required JDK >= 1.5.0? - the xerces-j 2.2 implementation - http://xml.apache.org
  js.jar              Y   - javascript support - http://www.mozilla.org/rhino
  servlet.jar         Y   - aka servlet-api.jar - required for ServletUnit - unit testing of servlets - http://java.sun.com
  junit.jar           Y   - running the unit tests - http://www.junit.org

  Buildtime:
  ==========
  
     Jar             Have       Description
  mail.jar            N   - Testing the file upload capabilities - not used to run - HttpUnit itself http://java.sun.com/products/javamail/
  activation.jar      Y   - Testing the file upload capabilities - not used to run - HttpUnit itself http://java.sun.com/products/javabeans/glasgow/jaf.html


My recommendations are:

A. The jars in category 1 should remain in pims-web/lib and on the eclipse classpath as the web app should use the container-provided versions

B. The jars in category 2 should be removed from the eclipse classpath, deleted from pims-web/lib and their pims-web/WebContent/WEB-INF/lib equivalents added to the eclipse classpath instead

C. The jars in categories 3 and 4 should remain in pims-web/lib and on the eclipse classpath

D. The jars in category 5 should remain in pims-web/lib and on the eclipse classpath as they may be required during the execution of a test case

E. The jars in category 6 should be removed from the eclipse classpath, deleted from pims-web/lib and their pims-web/WebContent/WEB-INF/lib equivalents added to the eclipse classpath instead

Jon