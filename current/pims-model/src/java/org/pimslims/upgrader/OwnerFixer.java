/**
 * pims-model-3.2 org.pimslims.upgrader OwnerFixer.java
 * 
 * @author bl67
 * @date 13 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Collection;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;

/**
 * OwnerFixer for YSBL xtalpims loader
 * 
 */
@Deprecated
// obsolete
public abstract class OwnerFixer {

    public static void main(final String[] args) throws AbortedException, ConstraintException {
        if (args.length < 2) {
            System.out.println("provide arguments in order: ");
            System.out.println(" 1) old userName");
            System.out.println(" 2) new userName");
            return;
        }
        //AbstractLoader.silent = false;
        final WritableVersion wv = ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
        try {
            fixOwner(args[0].toLowerCase(), args[1].toLowerCase(), wv);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * OwnerFixer.fixOwner
     * 
     * @param string
     * @param string2
     * @param wv
     * @throws ConstraintException
     */
    private static void fixOwner(final String oldUserName, final String newUserName, final WritableVersion wv)
        throws ConstraintException {
        System.out.println("Getting user info...");
        final User oldUser = wv.findFirst(User.class, User.PROP_NAME, oldUserName);

        final User newUser = wv.findFirst(User.class, User.PROP_NAME, newUserName);

        if (oldUser == null) {
            throw new RuntimeException("User " + oldUserName + " can not be found!");
        }
        if (newUser == null) {
            throw new RuntimeException("User " + newUserName + " can not be found!");
        }
        if (newUser.getUserGroups().size() < 1) {
            throw new RuntimeException("User " + newUserName + " has no usergroup defined!");
        }
        LabNotebook owner = null;
        for (final UserGroup newUserGroup : newUser.getUserGroups()) {
            for (final Permission permisstion : newUserGroup.getPermissions()) {
                if (permisstion.getOpType().equalsIgnoreCase("create")) {
                    owner = permisstion.getLabNotebook();
                }
            }
        }
        if (owner == null) {
            throw new RuntimeException(newUserName + "'s usergroup has no 'create' permission defined!");
        }
        System.out.println("Fixing owner and creater for labBookEntries created by " + oldUserName);
        System.out.println("set creator to " + newUser);
        System.out.println("set access  to " + owner);
        final Collection<LabBookEntry> labBookEntries =
            wv.findAll(LabBookEntry.class, LabBookEntry.PROP_CREATOR, oldUser);
        for (final LabBookEntry labBookEntry : labBookEntries) {
            // no, can no longer do this labBookEntry.setCreator(newUser);
            labBookEntry.setAccess(owner);
        }

    }

}
