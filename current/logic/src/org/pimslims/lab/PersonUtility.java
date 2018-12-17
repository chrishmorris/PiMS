package org.pimslims.lab;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Person;
import org.pimslims.model.people.PersonInGroup;

/**
 * @author Bill
 * 
 */
@Deprecated
// Obsolete
public class PersonUtility {
    /**
     * @param rv
     * @return all person who is a user in PIMS
     */
    @Deprecated
    // should show "Users", not "Persons"
    public static Collection<Person> getUserPersons(final ReadableVersion rv) {
        Collection<Person> persons = new HashSet<Person>();
        final Collection<User> users = rv.getAll(User.class);
        for (final User user : users) {
            final Person p = user.getPerson();
            if (p != null) {
                persons.add(p);
            }
        }
        persons = rv.sortByName(persons);
        return persons;

    }

    /**
     * @param rv
     * @return all person who isn't a user in PIMS
     * @Deprecated // there should be none nowadays public static Collection<Person> getOtherPersons(final
     *             ReadableVersion rv) { final Collection<Person> userPersons =
     *             PersonUtility.getUserPersons(rv); // all users final Collection<Person> otherPersons =
     *             rv.getAll(Person.class);
     * 
     *             for (final Person p : userPersons) { // TODO assert otherPersons.contains(p) : "Person not
     *             found: // "+p.getFamilyName(); otherPersons.remove(p);
     * 
     *             } return otherPersons;
     * 
     *             }
     */

    /**
     * get hook and name of a collection of model Object
     * 
     * @param modelObjects
     * @return
     */
    public static <T extends ModelObject> Map<String, String> getHookAndName(final Collection<T> modelObjects) {
        final Map<String, String> hooks = new LinkedHashMap<String, String>();
        for (final ModelObject mo : modelObjects) {
            hooks.put(mo.get_Hook(), mo.get_Name());
        }
        return hooks;
    }

    /**
     * @return the Person representing the current user, or null if this User has no associated Person
     */
    @Deprecated
    // will identify user and person
    public static Person getCurrentUserAsPerson(final ReadableVersion version) {
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("name", version.getUsername());
        final Collection<User> users = version.findAll(org.pimslims.model.accessControl.User.class, criteria);
        if (0 == users.size()) {
            return null; // must be the administrator, and no user record
            // created
        }
        final ModelObject user = users.iterator().next();
        final Collection<ModelObject> people = user.get(User.PROP_PERSON);
        if (0 == people.size()) {
            return null;
        }
        final ModelObject person = people.iterator().next();
        return (Person) person;
    }

    public static class SurnameComparator implements Comparator {
        public int compare(final Object o1, final Object o2) {
            int comparison = PersonUtility.comporatorHelper(o1, o2);
            if (comparison == -2) {
                final Person p1 = (Person) o1;
                final Person p2 = (Person) o2;
                comparison = p1.getFamilyName().compareTo(p2.getFamilyName());
            }
            return comparison;
        }
    }

    static int comporatorHelper(final Object o1, final Object o2) {
        if (o1 == null || o2 == null) {
            return -1;
        }
        if (o1 == o2) {
            return 0;
        }

        if (!(o1 instanceof Person) && !(o2 instanceof Person)) {
            throw new ClassCastException("This comparator can only be used with Person class!");
        }
        return -2;
    }

    public static class GivenNameComparator implements Comparator {
        public int compare(final Object o1, final Object o2) {
            int comparison = PersonUtility.comporatorHelper(o1, o2);
            if (comparison == -2) {
                final Person p1 = (Person) o1;
                final Person p2 = (Person) o2;
                comparison = p1.getGivenName().compareTo(p2.getGivenName());
            }
            return comparison;
        }
    }

    public static String getFullName(final Person person) {

        final String name = person.getGivenName();
        final String surname = person.getFamilyName();
        final List<String> ini = person.getMiddleInitials();
        String fullname = "";
        if (!Util.isEmpty(name)) {
            fullname = name;
        }
        if (ini != null && !ini.isEmpty()) {
            for (final String in : ini) {
                fullname += " " + in;
            }
        }
        if (!Util.isEmpty(surname)) {
            fullname += " " + surname;
        }
        return fullname;
    }

    /**
     * @param person
     * @return
     */
    public static Collection<Group> getGroups(final Person person) {
        final Set<PersonInGroup> pigs = person.getPersonInGroups();
        final Collection<Group> ret = new HashSet(pigs.size());
        for (final Iterator iterator = pigs.iterator(); iterator.hasNext();) {
            final PersonInGroup pig = (PersonInGroup) iterator.next();
            ret.add(pig.getGroup());
        }
        return ret;
    }

    /*
    * name = false -> return surname name = true -> return name
    */
    private static String getSurnameOrName(String designedBy, final boolean name) {
        if (designedBy == null) {
            return null;
        }
        designedBy = designedBy.trim();
        final int spInd = designedBy.indexOf(' ');
        String pname = null;
        if (spInd > 0) {
            if (name) {
                pname = designedBy.substring(0, spInd).trim();
            } else {
                pname = designedBy.substring(spInd + 1).trim();
            }
        } else {
            if (name) {
                pname = designedBy;
            }
        }
        return pname;
    }

    /*
     * @return null if cannot tell familyname
     */
    public static String getPersonFamilyName(final String designedBy) {
        return PersonUtility.getSurnameOrName(designedBy, false);
    }

    public static String getPersonName(final String designedBy) {
        return PersonUtility.getSurnameOrName(designedBy, true);
    }
}
