package org.pimslims.persistence;

import java.io.InputStream;

import org.hibernate.util.DTDEntityResolver;
import org.xml.sax.InputSource;

/**
 * Extends Hibernate's default resolver with lookup of entities on classpath.
 * <p>
 * For example, the following can be resolved:
 * 
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;!DOCTYPE hibernate-mapping SYSTEM &quot;http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd&quot;
 * [
 * &lt;!ENTITY usertypes SYSTEM &quot;/org/hibernate/ce/auction/persistence/UserTypes.hbm.xml&quot;&gt;
 * ]&gt;
 * 
 * &lt;hibernate-mapping&gt;
 * 
 * &amp;usertypes
 * ...
 * </pre>
 * 
 * The file will be looked up in the classpath. Don't forget the leading slash! Relative location is not
 * supported.
 * 
 * @author Bill Lin
 */
@Deprecated
// unused
public class ImportFromClasspathEntityResolver extends DTDEntityResolver {

    // private static final Log log =
    // LogFactory.getLog(ImportFromClasspathEntityResolver.class);

    /**
     * 
     */
    private static final long serialVersionUID = 8075245065693229133L;

    // This is the prefix of SYSTEM entity identifiers which are not URLs
    private static final String PREFIX = "file://";

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        // log.debug("Trying to resolve system id: " + systemId);
        if (systemId != null && systemId.startsWith(PREFIX)) {
            // Remove the initial slash and look it up
            String resource = systemId.substring(PREFIX.length());
            // log.debug("Looking up entity on classpath: " + resource);

            InputStream stream = getClass().getResourceAsStream(resource);
            if (stream == null) {
                stream = getClass().getClassLoader().getResourceAsStream(resource);
            }
            if (stream == null) {
                stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            }
            if (stream == null) {
                // log.error("Couldn't find entity in classpath: " + resource);
            } else {
                // log.debug("Found entity on classpath: " + resource);
                InputSource source = new InputSource(stream);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                return source;
            }
        }
        return super.resolveEntity(publicId, systemId);
    }

}
