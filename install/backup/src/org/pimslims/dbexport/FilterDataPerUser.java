/**
 * pims-backup org.pimslims.dbexport FilterDataPerUser.java
 *
 * @author alan
 * @date Aug 18, 2009
 *
 *       Protein Information Management System
 * @version: 2.2
 *
 *           Copyright (c) 2009 alan The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dbexport;

import java.io.File;
import java.io.FileNotFoundException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.accessControl.User;

/**
 * FilterDataPerUser TODO this must also delete the other users
 */
public class FilterDataPerUser {

    private static ModelImpl model;
    private static String targetUser;
    private static String propertyFile;
    private static String force;

    /**
     *
     * Constructor for FilterDataPerUserHQL
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a Properties file that defines the connection to the database.");

            // For development purposes
            propertyFile = "./temp_Properties";
            targetUser = "user1";
            force = "no";

            return; // uncomment this line for Release

        } else {
            propertyFile = args[0];
            targetUser = args[1];
            if (args.length < 3) {
                force = "no";
                }else{
                if ( args[2] != null ) {
                    force = args[2];
                    if ( ! "no".equals(force) ) {
                        force = "yes";
                    }
                }else{
                    force = "no";
                }
            }
        }

        // Load the properties file
        try {
            FilterDataPerUser.initModel(propertyFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.exit(1);
        }

        // Get a writable connection to the DB
        final WritableVersion version =
            FilterDataPerUser.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        try {
        // Print target user name
        System.out.println("Target user name: " + targetUser);

        //Get object myUser for a given string username
        User myUser = version.findFirst(User.class, User.PROP_NAME, targetUser);
        if (null == myUser) {
            throw new IllegalStateException("User not found: " + targetUser);
        }

        Session session = version.getSession();
/*
        if ( ! "no".equals(force)) {
            System.out.println("Forcing deletion by removing HolderLocation");
            String hqlQueryDeleteHolderLocation = "delete from HolderLocation";
            session.createQuery(hqlQueryDeleteHolderLocation).setCacheable(false).executeUpdate();
        } */

        String hqlQueryDelete =
            "delete from LabBookEntry labbookentry where " +
            "labbookentry.access is not null and labbookentry.access not in ("+
            "select distinct permission.accessObject from Permission as permission " +
            "LEFT JOIN permission.userGroup.memberUsers as tuser " +
            "where tuser.name = :targetUser and permission.opType = 'read')";

        Query queryDelete = session.createQuery(hqlQueryDelete).setCacheable(false).setString("targetUser", targetUser);

        int count = queryDelete.executeUpdate();
        session.getTransaction().commit();
        System.out.println(count + " Rows deleted");
        session.close();

        } finally {
            if (!version.isCompleted()) {version.abort();}
        }
    }

    /**
     * @param string
     * @throws FileNotFoundException
     */
    private static void initModel(final String propertyFileName) throws FileNotFoundException {
        //start from propertyFile if provided
        System.out.println("loading DB connection info from: " + propertyFileName);
        final File properties = new java.io.File(propertyFileName);
        if (!properties.exists()) {
            System.out.println("file does NOT exist:" + properties.getAbsolutePath());
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        model = (ModelImpl) org.pimslims.dao.ModelImpl.getModel(properties);
        //System.out.println(model.getProperty("db.url"));
    }

}
