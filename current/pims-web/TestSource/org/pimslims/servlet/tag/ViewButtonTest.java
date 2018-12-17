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
import org.pimslims.presentation.mock.MockPageContext;

/**
 * ViewButtonTest
 * 
 */
@Deprecated
// tests obsolete code
public class ViewButtonTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ViewButtonTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.servlet.tag.ViewButton#doStartTag()}.
     */
    public final void testDoStartTag() throws Exception {
        final WritableVersion version =
            this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ViewButton viewButton = new ViewButton();
            final MockPageContext mockPageContext = new MockPageContext();
            viewButton.setPageContext(mockPageContext);
            final ModelObject modelObject = new Organisation(version, "test" + System.currentTimeMillis());
            viewButton.setModelObject(modelObject);
            viewButton.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertTrue(output.contains(modelObject.get_Name()));
            Assert.assertTrue(output, output.contains("pims/View/" + modelObject.get_Hook()));
            Assert.assertTrue(output, output.contains("title=\"View all information on Organisation test"));
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testEscaped() throws Exception {
        final WritableVersion version =
            this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ViewButton viewButton = new ViewButton();
            final MockPageContext mockPageContext = new MockPageContext();
            viewButton.setPageContext(mockPageContext);
            final ModelObject modelObject = new Organisation(version, "test\"" + System.currentTimeMillis());
            viewButton.setModelObject(modelObject);
            viewButton.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertTrue(output,
                output.contains("title=\"View all information on Organisation test&quot;"));
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
