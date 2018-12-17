/**
 * current-pims-web org.pimslims.servlet.tag ListTest.java
 * 
 * @author cm65
 * @date 26 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.presentation.mock.MockPageContext;

/**
 * ListTest
 * 
 */
@Deprecated
// list tag is obsolete
public class ListTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param name
     */
    public ListTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testNone() throws Exception {
        final MockPageContext mockPageContext = new MockPageContext();
        final List tag = new List();
        tag.setPageContext(mockPageContext);
        tag.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertEquals("<option value=\"\" >none recorded</option>", output);
    }

    public final void testOne() throws Exception {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final MockPageContext mockPageContext = new MockPageContext();
            final List tag = new List();
            tag.setPageContext(mockPageContext);
            tag.setMetaClassName(Organisation.class.getName());
            tag.setAttributes(Organisation.PROP_NAME);
            final ModelObject modelObject = new Organisation(version, "test" + System.currentTimeMillis());
            tag.setObjectList(Collections.singleton(modelObject));
            tag.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertEquals("<option value=\"" + modelObject.get_Hook() + "\">" + modelObject.get_Name()
                + "</option>\n", output);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testEscape() throws Exception {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final MockPageContext mockPageContext = new MockPageContext();
            final List tag = new List();
            tag.setPageContext(mockPageContext);
            tag.setMetaClassName(Organisation.class.getName());
            tag.setAttributes(Organisation.PROP_NAME);
            final ModelObject modelObject = new Organisation(version, "aa<bb" + System.currentTimeMillis());
            tag.setObjectList(Collections.singleton(modelObject));
            tag.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertFalse(output.contains("aa<bb"));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    @SuppressWarnings("deprecation")
    public final void testNullPointer() throws Exception {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final MockPageContext mockPageContext = new MockPageContext();
            final List tag = new List();
            tag.setPageContext(mockPageContext);
            tag.setMetaClassName(Person.class.getName());
            tag.setAttributes("name");
            final ModelObject modelObject = new Person(version, "aa<bb" + System.currentTimeMillis());
            tag.setObjectList(Collections.singleton(modelObject));
            tag.doStartTag();
            final String output = mockPageContext.getOutput();
            Assert.assertFalse(output.contains("aa<bb"));
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
