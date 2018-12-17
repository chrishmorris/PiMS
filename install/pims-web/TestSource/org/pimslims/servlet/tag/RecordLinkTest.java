/**
 * pims-web org.pimslims.tag ViewButtonTest.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Organisation;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.mock.MockPageContext;

/**
 * ViewButtonTest
 * 
 */
@Deprecated
// obsolete
public class RecordLinkTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public RecordLinkTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     */
    public final void testDoStartTag() throws Exception {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RecordLink tag = new RecordLink();
            final MockPageContext mockPageContext = new MockPageContext();
            tag.setPageContext(mockPageContext);
            final ModelObject modelObject = new Organisation(version, "test" + System.currentTimeMillis());
            tag.setBean(new ModelObjectShortBean(modelObject));
            tag.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertTrue(output.contains(modelObject.get_Name() + "</a>"));
            Assert.assertTrue(output, output.contains("pims/View/" + modelObject.get_Hook()));
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     */
    public final void testEscape() throws Exception {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RecordLink tag = new RecordLink();
            final MockPageContext mockPageContext = new MockPageContext();
            tag.setPageContext(mockPageContext);
            final ModelObject modelObject = new Organisation(version, "<test>" + System.currentTimeMillis());
            tag.setBean(new ModelObjectShortBean(modelObject));
            tag.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertFalse(output, output.contains(modelObject.get_Name()));
            Assert.assertTrue(output, output.contains("pims/View/" + modelObject.get_Hook()));
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
