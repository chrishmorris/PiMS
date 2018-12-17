package org.pimslims.command.leeds.primerOrder;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private final String BUNDLE_NAME;

    private final ResourceBundle RESOURCE_BUNDLE;

    public Messages(final String resourceName) {
        this.BUNDLE_NAME = "org.pimslims.command.leeds.primerOrder." + resourceName;
        this.RESOURCE_BUNDLE = ResourceBundle.getBundle(this.BUNDLE_NAME);
    }

    public String getString(final String key) {

        try {
            return this.RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
