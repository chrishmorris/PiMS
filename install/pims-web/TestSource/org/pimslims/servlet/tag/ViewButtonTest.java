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

import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.people.Organisation;
import org.pimslims.presentation.mock.MockPageContext;

/**
 * ViewButtonTest
 * 
 */
public class ViewButtonTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ViewButtonTest(String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.servlet.tag.ViewButton#doStartTag()}.
     */
    public final void testDoStartTag() throws Exception {
        WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ViewButton viewButton = new ViewButton();
            MockPageContext mockPageContext = new MockPageContext();
            viewButton.setPageContext(mockPageContext);
            ModelObject modelObject = new Organisation(version, "test" + System.currentTimeMillis());
            viewButton.setModelObject(modelObject);
            viewButton.doStartTag();
            String output = mockPageContext.getOutput();
            assertTrue(output.contains(modelObject.get_Name()));
            assertTrue(output, output.contains("pims/View/" + modelObject.get_Hook()));
            assertTrue(output, output.contains("title=\"View all information on Organisation test"));
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testEscaped() throws Exception {
        WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ViewButton viewButton = new ViewButton();
            MockPageContext mockPageContext = new MockPageContext();
            viewButton.setPageContext(mockPageContext);
            ModelObject modelObject = new Organisation(version, "test\"" + System.currentTimeMillis());
            viewButton.setModelObject(modelObject);
            viewButton.doStartTag();
            String output = mockPageContext.getOutput();
            assertTrue(output, output.contains("title=\"View all information on Organisation test&quot;"));
            System.out.println(output); //TODO remove
            // LATER more tests
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
