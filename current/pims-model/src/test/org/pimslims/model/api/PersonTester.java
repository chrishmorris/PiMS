/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;
import java.util.HashMap;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Person;


/**
 * Tests generated class for Person
 */
public class PersonTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test Person
     */
    public static final HashMap<String, Object> ATTRIBUTES =
        new HashMap<String, Object>(org.pimslims.test.POJOFactory.getAttrPerson());

    public PersonTester() {
        super(Person.class.getName(), ModelImpl.getModel(), "testing implementation of Person class",
            ATTRIBUTES);
    }

    /**
     * @see org.pimslims.metamodel.TestAMetaClass#doTestVersion(org.pimslims.dao.WritableVersion)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ModelObject doTestVersion(WritableVersion wv) throws ConstraintException, AccessException {
        System.out.println("PersonTester.doTestVersion method");
        Person person = (Person) super.doTestVersion(wv);
        // test concatenation of Given name and Family name
        assertEquals("testGivenName testFamilyName", person.get_Name());
        person.setMiddleInitials(Collections.EMPTY_LIST);
        person.set_Value(Person.PROP_MIDDLEINITIALS, Collections.EMPTY_LIST);

        person.setFamilyTitle("Bishop");
        person.setFamilyTitle(null);
        // TODO assertNull(person.getFamilyTitle());
        return person;
    }

}
