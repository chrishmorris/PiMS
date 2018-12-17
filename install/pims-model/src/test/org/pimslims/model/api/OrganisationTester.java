/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Date;
import java.util.HashMap;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Organisation;

/**
 * Tests generated class for Organisation
 */
public class OrganisationTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test Organisation
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>();
    static {
        ATTRIBUTES.put(Organisation.PROP_NAME, "org" + System.currentTimeMillis());
        ATTRIBUTES.put(Organisation.PROP_ADDRESSES,
            java.util.Arrays.asList(new String[] { "10 Downing Street", "Gotham City" }));
        ATTRIBUTES.put(Organisation.PROP_ORGANISATIONTYPE,
            "testOrganisationType" + System.currentTimeMillis());

    }

    /**
     * 
     */
    public OrganisationTester() {
        super(org.pimslims.model.people.Organisation.class.getName(), ModelImpl.getModel(),
            "testing implementation of Organisation class", ATTRIBUTES);
    }

    public void testNotRequired() {
        MetaAttribute email =
            metaClass.getAttribute(org.pimslims.model.people.Organisation.PROP_EMAILADDRESS);
        assertFalse(email.isRequired());
    }

    public void testSetList() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            // create
            ModelObject testObject = wv.create(javaClass, ATTRIBUTES);
            String hook = testObject.get_Hook();
            assertNotNull("created", wv.get(hook));

            MetaAttribute addresses = metaClass.getAttribute("addresses");
            addresses.set(testObject, new String[] { "10 Downing Street", "Westminster" });
            assertNotNull("set array", addresses.get(testObject));
            java.util.List list = java.util.Arrays.asList(new String[] { "The Bat Cave", "Gotham City" });
            addresses.set(testObject, list);
            assertNotNull("set list", addresses.get(testObject));

            // TODO test search for match on multi-attribute

        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getClass().getName() + ": " + ex.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /*
     * LATER public void testEscape() { Pattern.compile("\\\\\\\\"); attributes.put("name", "it's a
     * \'problem\\"); testVersion(); }
     */

    public static String getTestAttribute() {

        return Organisation.PROP_ORGANISATIONTYPE;
    }

    /**
     * @see org.pimslims.metamodel.AbstractTestModelObject#getNewValue()
     */

    public static Object getNewValue() {
        return "newName" + new Date().getTime();
    }
}
