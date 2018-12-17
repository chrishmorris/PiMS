/**
 *
 */
package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;

/**
 * @author Petr Troshin
 * 
 *         Date March 2006
 * 
 *         The class is to create users, user groups, lab notebooks, and permission within the PIMS and to
 *         generate the list of users which will go to tomcat-users file to enable created users to be
 *         authorized.
 * 
 */
@Deprecated
// no longer supported
public class AccessRights implements PIMSUsers, PIMSClassesDescription {
    /*
     * Model (database connection)
     */
    private static AbstractModel model;

    /*
     * The list of possible operation types defined in PIMS
     */
    private static final List<String> opTypes = new ArrayList<String>();
    static {
        AccessRights.opTypes.add("read");
        AccessRights.opTypes.add("create");
        AccessRights.opTypes.add("update");
        AccessRights.opTypes.add("delete");
    }

    /*
     * The list of UserGroups
     */
    static ArrayList allGroups;

    /*
     * The class is to hold the information about user to generate tomcat-user files and give everyone read
     * access to the data of the others. It was proved that collection is not suitable because the PIMS
     * ModelObjects do not support equals and therefore not useful within the collection
     */
    static class UserGroups {
        /*
         * User loing name
         */
        String login;

        /*
         * User password
         */
        String password;

        /*
         * Defines whether user has writable access or not
         */
        private boolean readOnly;

        /*
         * The instance of Implementation.User
         */
        User user;

        /*
         * The instance of Implementation.UserGroup
         */
        UserGroup userGroup;

        UserGroups(final User user, final UserGroup userGroup) {
            this.user = user;
            this.userGroup = userGroup;
        }

        /*
         * The method is used to set the information relevant to tomcat-users file generation
         */
        void setLogin(final String login, final String password, final boolean readOnly) {
            this.login = login;
            this.password = password;
            this.readOnly = readOnly;
        }

        /*
         * Test whether the user is already a member of the group @param usergroup Implemetation.UserGroup
         * @return true if the user is a member false otherwise
         */
        boolean userIsMemberOf(final UserGroup usergroup) {
            final ArrayList leadg = (ArrayList) this.user.getUserGroups();
            for (final Iterator iter = leadg.iterator(); iter.hasNext();) {
                final UserGroup userLeadgroup = (UserGroup) iter.next();
                if (usergroup.getName().equals(userLeadgroup.getName())) {
                    return true;
                }
            }
            return false;
        }

        /*
         * Generate information per user to go into tomcat user file @return String user for tomcat-user file
         */
        String getTomcatUser() {
            assert this.login != null && this.password != null : "Login and/or password for the user has not been set!";
            return "<user username=\"" + this.login + "\" password=\"" + this.password + "\" roles=\""
                + (this.readOnly ? "pims-user" : "pims-view") + "\"/>\n";
        }

        /*
         * Generate an information per user to go to tomcat-user file
         */
        String getTomcatRole() {
            assert this.login != null : "Login has not been set !";
            return "<role rolename=\"" + this.login + "\"/>\n";
        }

    } // UserGroups class end

    /**
     * Create an instance of AccessRights
     */
    public AccessRights() {
        AccessRights.model = org.pimslims.dao.ModelImpl.getModel();
        AccessRights.allGroups = new ArrayList<ModelObject>();
    }

    static String usage() {
        return "Usage: " + "\n" + "AccessRights inputFile outputFile -r" + "\n" + "Where: " + "\n"
            + "-r - give read access for all data to everyone" + "\n"
            + "inputFile - users xml file definition" + "\n"
            + "outFile - output file for writing users definition for tomcat-users " + "\n"
            + "-r flag and outFile is optional";
    }

    /**
     * @param -r - give read access for all data to everyone
     * @param inputFile - users xml file definition
     * @param outFile - output file for writing users definition for tomcat-users -r flag and outFile is
     *            optional
     * @param String[] args
     */

    @Deprecated
    // obsolete
    public static void main(final String[] args) {
        // Call this to create access rights for Leeds
        String inputFile = null;
        String outFile = null;
        String read = null;
        if (args.length == 1) {
            inputFile = args[0];
            System.out.println("Will use user.xml file " + inputFile);
        } else if (args.length == 2) {
            inputFile = args[0];
            outFile = args[1];
            System.out.println("Will use user.xml file " + inputFile);
            System.out.println("Will generate a tomcat users file at " + outFile);
        } else if (args.length == 3) {
            inputFile = args[0];
            outFile = args[1];
            read = args[2];
            System.out.println("Will use user.xml file " + inputFile);
            System.out.println("Will generate a tomcat users file at " + outFile);
        } else {
            AccessRights.usage();
            System.exit(0);
        }

        BufferedReader breader = null;
        BufferedWriter bwriter = null;
        boolean readForAll = false;
        try {
            breader = new BufferedReader(new FileReader(inputFile));
            if (outFile != null) {
                bwriter = new BufferedWriter(new FileWriter(outFile));
            }
            if (read != null) {
                if (read.equals("-r")) {
                    readForAll = true;
                    System.out.println("Will give a read access to everyone");
                } else {
                    System.err.println("The wrong parameter!");
                    AccessRights.usage();
                    System.exit(1);
                }
            }

        } catch (final FileNotFoundException e) {
            System.err.println("Could not find input file");
            System.exit(1);
        } catch (final IOException e) {
            System.err.println("Could not write to output file");
            System.exit(1);
        }

        final AccessRights acc = new AccessRights();
        acc.setup(AccessRights.model, breader, bwriter, readForAll);

    }

    /**
     * Start parsing user definition xml file
     * 
     * @param Abstract model PIMS database connection
     * @param BufferedReader inputFile users xml input file
     * @param BufferedWriter outFile optional tomcat-users output file
     * @param boolean readForAll whether to give read access to all data for all users
     */

    @Deprecated
    // obsolete
    public void setup(final AbstractModel model, final BufferedReader inputFile,
        final BufferedWriter outFile, final boolean readForAll) {
        assert null != inputFile;
        final SAXBuilder builder = new SAXBuilder();
        builder.setValidation(true);
        builder.setIgnoringElementContentWhitespace(true);
        WritableVersion rw = null;
        try {
            final Document doc = builder.build(inputFile);
            final Element cont = doc.getRootElement();
            // Obtain a writable version for each person, than commit
            rw = model.getWritableVersion(AbstractModel.SUPERUSER);
            final List organisations = cont.getChildren("Organisation");
            for (final Iterator iter = organisations.iterator(); iter.hasNext();) {
                final Element el = (Element) iter.next();
                final String orgName = el.getAttributeValue("name");
                final Element access = el.getChild("access");
                final List users = el.getChildren("users");
                final ArrayList allowedopTypes = this.parseAccess(access);
                this.parse(rw, orgName, users, allowedopTypes);
            }
            // Record tomcat users config file
            if (outFile != null) {
                this.writeTomcatConfig(outFile);
            }
            // Give every user read access to the rest of the data
            if (readForAll) {
                AccessRights.giveReadForAll();
            }
            rw.commit();

        } catch (final JDOMException jex) {
            Throwable cause = jex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause);
        } catch (final IOException ioe) {
            Throwable cause = ioe;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
                inputFile.close();
                outFile.close();
            } catch (final IOException e) {
                Throwable cause = e;
                while (null != cause.getCause()) {
                    cause = cause.getCause();
                }
                throw new RuntimeException(cause);
            }
        }

    }

    /**
     * 
     * @param ArrayList access the list of an access rights for a particular user according to the access
     *            section of xml user definition file
     * @return ArrayList of Strings which represents allowed access action for the user
     * @throws DataConversionException
     */
    ArrayList parseAccess(final Element access) throws DataConversionException {
        final ArrayList<String> allowedopTypes = new ArrayList<String>();
        final List rights = access.getAttributes();
        for (final Iterator iterator = rights.iterator(); iterator.hasNext();) {
            final Attribute opType = (Attribute) iterator.next();
            if (opType.getBooleanValue()) {
                allowedopTypes.add(opType.getName());
            }
        }
        return allowedopTypes;
    }

    /**
     * Parse Organisation section in the users xml file
     * 
     * @param rw WritableVersion
     * @param orgName String organisation name
     * @param allusers List of the jdom.Elements representing each user
     * @param allowedopTypes List of the access actions common for the organisation (allowed for each user in
     *            the group)
     * @throws DataConversionException
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    @Deprecated
    // obsolete
    void parse(final WritableVersion rw, final String orgName, final List allusers,
        final ArrayList allowedopTypes) throws DataConversionException, AccessException, ConstraintException,
        ClassNotFoundException {

        for (final Iterator iter = allusers.iterator(); iter.hasNext();) {
            final Element elem = (Element) iter.next();
            final List users = elem.getChildren("user");
            this.parseUsers(rw, orgName, users, allowedopTypes);
        }
    }

    /**
     * 
     * @param rw WritableVersion
     * @param orgName String organisation name
     * @param users List of the jdom.Elements representing all users within organisation
     * @param access List of the access action common for this organisation
     * @throws DataConversionException
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     * @throws IOException
     */

    @Deprecated
    // obsolete
    void parseUsers(final WritableVersion rw, final String orgName, final List users, List access)
        throws DataConversionException, AccessException, ConstraintException, ClassNotFoundException {

        // Default data owner
        this.createAccess(rw, Access.REFERENCE);
        // Data owner
        final ModelObject accessObj = this.createAccess(rw, orgName);
        // Groups for groups for one dataowner for each opType (read, create,
        // etc)
        final HashMap groups = this.createGroups(rw, orgName);
        // Permission
        this.createAllAccessRights(rw, accessObj, groups);
        final List groupAccess = access;
        for (final Iterator iterator = users.iterator(); iterator.hasNext();) {
            final Element user = (Element) iterator.next();
            final Element person = user.getChild("person");
            final String login = user.getChild("loginName").getValue();
            final String passwd = user.getChild("password").getValue();

            final Element userAccess = user.getChild("access");

            if (userAccess != null) {
                access = this.parseAccess(userAccess);
            } else {
                access = groupAccess; // Common access level for a group
            }

            // Person
            final ModelObject personObj = this.createPerson(rw, orgName, person);
            // User
            this.createUser(rw, access, login, personObj, groups);

            // Generate tomcatConfig adds
            boolean waccess = false;
            if (access.contains("create") || access.contains("update") || access.contains("delete")) {
                waccess = true;
            }
            ((UserGroups) AccessRights.allGroups.get(AccessRights.allGroups.size() - 1)).setLogin(login,
                passwd, waccess);
        }

    }

    /**
     * Generate tomcat-users configuration file for tomcat
     */
    void writeTomcatConfig(final BufferedWriter writer) throws IOException {
        for (final Iterator iter = AccessRights.allGroups.iterator(); iter.hasNext();) {
            final UserGroups userGroup = (UserGroups) iter.next();
            writer.write(userGroup.getTomcatRole());
        }
        for (final Iterator iter = AccessRights.allGroups.iterator(); iter.hasNext();) {
            final UserGroups userGroup = (UserGroups) iter.next();
            writer.write(userGroup.getTomcatUser());
        }
    }

    /**
     * Give all users readable access to all data if called
     * 
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    static void giveReadForAll() throws AccessException, ConstraintException, ClassNotFoundException {

        final ArrayList allgroups = new ArrayList(AccessRights.allGroups);
        for (final Iterator iter = AccessRights.allGroups.iterator(); iter.hasNext();) {
            final UserGroups userGroup = (UserGroups) iter.next();
            // allgroups.put(userGroup.userGroupName, userGroup.userGroup);
            for (final Iterator iterator = allgroups.iterator(); iterator.hasNext();) {
                final UserGroups curusgroup = (UserGroups) iterator.next();
                if (!userGroup.userIsMemberOf(curusgroup.userGroup)) {
                    userGroup.user.addUserGroup(curusgroup.userGroup);
                }
            }
        }
    }

    /**
     * Create all possible access rights within PIMS for a given organisation
     * 
     * @param rw WritableVersion
     * @param access List
     * @param groups - Map accessType (e.g. read) -> Implementation.UserGroup instance
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    void createAllAccessRights(final WritableVersion rw, final ModelObject access, final HashMap groups)
        throws AccessException, ConstraintException, ClassNotFoundException {

        for (final Iterator iter = groups.keySet().iterator(); iter.hasNext();) {
            final String opType = (String) iter.next();
            final HashMap<String, Object> permissionPrm = new HashMap<String, Object>();
            permissionPrm.put(PermissionAttr.opType, opType);
            permissionPrm.put(PermissionRoles.accessObject, access);
            permissionPrm.put(PermissionRoles.userGroup, groups.get(opType));
            Util.getOrCreate(rw, Access.REFERENCE, Permission.class.getName(), permissionPrm);
        }
    }

    /**
     * Create Implementation.User based on information recorded on the user in users.xml
     * 
     * @param rw WritableVersion
     * @param accessRights - List of the access rights defined for this specific user
     * @param login String login name
     * @param person - instance of Person
     * @param groups - List all groups within the organisation
     * @return ModelObject Implementation.User
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    ModelObject createUser(final WritableVersion rw, final List accessRights, final String login,
        final ModelObject person, final HashMap groups) throws AccessException, ConstraintException,
        ClassNotFoundException {

        final HashMap<String, Object> userPrm = new HashMap<String, Object>();
        userPrm.put(UserAttr.name, login);
        userPrm.put(UserRoles.person, person);
        final ArrayList<Object> groupForUser = new ArrayList<Object>();
        for (final Iterator iter = accessRights.iterator(); iter.hasNext();) {
            final String opType = (String) iter.next();
            groupForUser.add(groups.get(opType));
        }
        userPrm.put(UserRoles.ledGroups, groupForUser);
        final ModelObject user = Util.getOrCreate(rw, Access.REFERENCE, User.class.getName(), userPrm);
        final UserGroup userGroup = (UserGroup) ((ModelObject) groups.get("read"));

        AccessRights.allGroups.add(new UserGroups((User) user, userGroup));

        return user;
    }

    /**
     * Create an Implemenation.AccessObject
     * 
     * @param rw - WritableVersion
     * @param organisation String the name of organisation
     * @return ModelObject AccessObject
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    @Deprecated
    // obsolete
    ModelObject createAccess(final WritableVersion rw, final String organisation) throws AccessException,
        ConstraintException, ClassNotFoundException {

        final HashMap<String, String> accessPrm = new HashMap<String, String>();
        accessPrm.put(LabNotebook.PROP_NAME, organisation); // "Leeds data"
        // accessPrm.put(AccessObjectAttr.description, dataownerdesc); //"Global
        // Leeds data"
        final ModelObject access =
            Util.getOrCreate(rw, Access.REFERENCE, LabNotebook.class.getName(), accessPrm);
        return access;
    }

    /**
     * 
     * @param rw WritableVersion
     * @param organisation String organisation name
     * @return HashMap accessAction -> Implementation.UserGroup instance e.g. read -> Leeds_read (where Leeds
     *         = organisation name)
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    HashMap createGroups(final WritableVersion rw, final String organisation) throws AccessException,
        ConstraintException, ClassNotFoundException {
        final HashMap<String, ModelObject> groups = new HashMap<String, ModelObject>();
        for (final Iterator iter = AccessRights.opTypes.iterator(); iter.hasNext();) {
            final String opType = (String) iter.next();
            final HashMap<String, String> groupPrm = new HashMap<String, String>();
            groupPrm.put(UserGroupAttr.name, organisation + "_" + opType); // "Leeds"
            final ModelObject group =
                Util.getOrCreate(rw, Access.REFERENCE, UserGroup.class.getName(), groupPrm);
            groups.put(opType, group);
        }
        return groups;
    }

    /**
     * 
     * @param rw WritableVersion
     * @param dataowner String - dataowner = organisation name
     * @param person jdom.Element person
     * @return ModelObject Person
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    ModelObject createPerson(final WritableVersion rw, final String dataowner, final Element person)
        throws AccessException, ConstraintException, ClassNotFoundException {
        final String name = person.getChild("name").getValue();
        final String surname = person.getChild("surname").getValue();
        final HashMap<String, String> pattr = new HashMap<String, String>();
        pattr.put(PersonAttr.familyName, surname);
        pattr.put(PersonAttr.givenName, name);
        final Element title = person.getChild("title");
        if (title != null) {
            pattr.put(PersonAttr.title, title.getValue());
        }
        return Util.getOrCreate(rw, dataowner, Person.class.getName(), pattr);
    }

} // class end
