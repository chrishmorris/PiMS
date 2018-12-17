/**
 * tools org.pimslims.command AdministratorPassword.java
 * 
 * @author cm65
 * @date 7 Dec 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pimslims.access.Access;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;

/**
 * AdministratorPassword This is a command line tool used during installation of PiMS. It is used for
 * installations that keep passwords in the PiMS database, not for LDAP and not for legacy tomcat-users.xml
 * installations.
 */
public class AdministratorPassword {

    /**
     * ADMINISTRATOR String
     */
    private static final String ADMINISTRATOR = Access.ADMINISTRATOR;

    /**
     * MyConsole Wrapper for java.io.Console that provides a substitute within Eclipse
     */
    private static class MyConsole {

        private final Console console;

        /**
         * Constructor for MyConsole
         * 
         * @param console
         */
        public MyConsole(Console console) {
            this.console = console;
        }

        /**
         * MyConsole.readPassword
         * 
         * @param string
         * @return
         * @throws IOException
         */
        public String readPassword(String string) throws IOException {
            if (null != this.console) {
                return new String(this.console.readPassword(string));
            }
            this.format(string);
            return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        }

        /**
         * MyConsole.format
         * 
         * @param string
         */
        public void format(String string) {
            if (null != this.console) {
                this.console.format(string);
                return;
            }
            System.out.println(String.format(string));
        }

    }

    private final WritableVersion version;

    /**
     * Constructor for AdministratorPassword
     * 
     * @param version
     */
    public AdministratorPassword(WritableVersion version) {
        this.version = version;
    }

    public static void main(String[] args) {
        MyConsole c = new MyConsole(System.console());
        WritableVersion version = ModelImpl.getModel().getWritableVersion(Access.ADMINISTRATOR);
        try {
            boolean noMatch;

            do {
                String newPassword1 = c.readPassword("Enter password for '" + ADMINISTRATOR + "': ");
                String newPassword2 = c.readPassword("Please enterpassword again: ");
                noMatch = !newPassword1.equals(newPassword2);
                if (noMatch) {
                    c.format("Passwords don't match. Try again.%n");
                } else {
                    new AdministratorPassword(version).save(ADMINISTRATOR, newPassword1);
                    c.format("OK: Password for '" + ADMINISTRATOR + "' changed.%n");
                    version.commit();
                }
            } while (noMatch);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (AbortedException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ConstraintException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (AccessException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

    }

    /**
     * AdministratorPassword.save
     * 
     * @param newPassword1
     * @param newPassword12
     * @throws AccessException
     * @throws ConstraintException
     */
    void save(String username, String password) throws ConstraintException, AccessException {
        User user = version.findFirst(User.class, User.PROP_NAME, username);
        if (null == user) {
            user = new User(this.version, username);
        }
        user.setDigestedPassword(password);
    }

}
