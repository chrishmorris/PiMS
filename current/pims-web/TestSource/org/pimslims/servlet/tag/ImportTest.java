/**
 * pims-web org.pimslims.tag ViewButtonTest.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockPageContext;

public class ImportTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ImportTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testClassName() throws Exception {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Import tag = new Import();
            final MockPageContext mockPageContext = new MockPageContext();
            tag.setPageContext(mockPageContext);
            tag.setClassName(Sample.class.getName());
            tag.doStartTag();
            final Map<String, Object> constants =
                (Map<String, Object>) mockPageContext.getAttribute("Sample");
            Assert.assertEquals(Sample.class, constants.get("class"));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testProperty() throws Exception {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Import tag = new Import();
            final MockPageContext mockPageContext = new MockPageContext();
            tag.setPageContext(mockPageContext);
            tag.setClassName(Sample.class.getName());
            tag.doStartTag();
            final Map<String, Object> constants =
                (Map<String, Object>) mockPageContext.getAttribute("Sample");
            Assert.assertEquals(Sample.PROP_ASSIGNTO, constants.get("PROP_ASSIGNTO"));
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
