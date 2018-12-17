package org.pimslims.lab;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Person;
import org.pimslims.model.people.PersonInGroup;
import org.pimslims.test.AbstractTestCase;

@SuppressWarnings("deprecation")
// Person is obsolete
public class PersonUtilityTest extends AbstractTestCase {
    public PersonUtilityTest() {
        super("test person utility");

    }

    public void testGetNoGroups() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final PersonInGroup pig = this.create(PersonInGroup.class);
            final Person person = pig.getPerson();
            final Collection<Group> groups = PersonUtility.getGroups(person);
            Assert.assertNotNull(groups);
            Assert.assertEquals(1, groups.size());
            Assert.assertTrue(groups.contains(pig.getGroup()));
        } finally {
            this.wv.abort();
        }
    }

    public void testGetPersons() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {

            final Person p1 = this.create(Person.class);
            final Person p2 = this.create(Person.class);
            // all user person check
            final Collection<Person> allPerson = this.wv.getAll(Person.class, 0, Integer.MAX_VALUE);
            final User user = this.create(User.class);
            user.setPerson(p1);
            this.wv.flush();
            // getUserPersons
            Collection<Person> persons;
            persons = PersonUtility.getUserPersons(this.wv);
            Assert.assertTrue(persons.contains(p1));
            Assert.assertFalse(persons.contains(p2));
            for (final Person p : persons) {
                if (!allPerson.contains(p)) {
                    Assert.assertTrue(allPerson.contains(p));
                }
            }

        } finally {
            this.wv.abort();
        }
    }

}
