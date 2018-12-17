/**
 * current-pims-web org.pimslims.command.DataUpdate DataFixerList.java
 * 
 * @author bl67
 * @date 17 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * DataFixerList
 * 
 */
public class DataFixerList {
    protected static final List<IDataFixer> getDataFixers() {

        final List<IDataFixer> dataFixers = new LinkedList<IDataFixer>();
        dataFixers.add(new OrganismsFixer());
        return dataFixers;

    }

    private static AbstractModel model;

    public static void main(final String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            DataFixerList.model = ModelImpl.getModel();
        } else {
            DataFixerList.initModel(args[0]);
        }

        int currentFixer = 1;
        final int totoalFixer = DataFixerList.getDataFixers().size();
        for (final IDataFixer fixer : DataFixerList.getDataFixers()) {
            System.out.println("\n---------Fixer " + currentFixer + " of " + totoalFixer
                + " -----------------------------------");
            currentFixer++;
            System.out.println(fixer.getClass().getSimpleName() + ": " + fixer.getDescription() + "\n");
            final WritableVersion wv =
                DataFixerList.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                final Boolean result = fixer.fixData(wv);
                if (result != null && result) {
                    System.out.println(fixer.getClass().getSimpleName() + " did not find any problem!");
                }
                wv.commit();
            } catch (final ConstraintException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());
            } catch (final AccessException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());
            } catch (final AbortedException e) {
                e.printStackTrace();
                System.out.println(fixer.getClass().getSimpleName() + " failed due to "
                    + e.getLocalizedMessage());
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                    System.out.println("Transaction(s) aborted!");
                }
            }
            System.out.println(fixer.getClass().getSimpleName() + " has finished!");
        }
        System.out.println("-------------All Fixers have been finished!---------------------------");
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
            System.out.println("file does NOT exist:" + propertyFileName);
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        DataFixerList.model = org.pimslims.dao.ModelImpl.getModel(properties);

    }
}
