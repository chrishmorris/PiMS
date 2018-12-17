/**
 * 
 */
package org.pimslims.presentation.servlet.utils;

import java.io.IOException;

/**
 * A common interface for finding the installation settings for the implementation.
 * 
 * @author cm65
 * 
 */
@Deprecated
// obsolete
public class ErrorMessagesProperties {

    private static java.io.InputStream getInputStream() {
        final java.io.InputStream in = ErrorMessagesProperties.class.getResourceAsStream("Properties");
        if (null == in) {
            throw new RuntimeException(
                "Properties file not found: classes/org/pimslims/servlet/utils/Properties");
        }
        return in;
    }

    private static java.util.Properties properties = null;

    private static synchronized java.util.Properties getProperties() {
        if (null == ErrorMessagesProperties.properties) {
            ErrorMessagesProperties.properties = new java.util.Properties();
            final java.io.InputStream propertiesInputStream = ErrorMessagesProperties.getInputStream();
            try {
                ErrorMessagesProperties.properties.load(propertiesInputStream);
                propertiesInputStream.close();
                // throw new RuntimeException("properties not found");
            } catch (final IOException ex1) {
                throw new RuntimeException(ex1);
            }
        }
        return ErrorMessagesProperties.properties;
    }

    @Deprecated
    // obsolete
    public static String getProperty(final String name) {
        final String property = ErrorMessagesProperties.getProperties().getProperty(name);
        assert null != property : "property not found: " + name;
        return property;
    }

}
