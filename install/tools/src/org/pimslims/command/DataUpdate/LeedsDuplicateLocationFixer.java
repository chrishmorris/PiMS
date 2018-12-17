/**
 * current-pims-web
 * 
 * @author Petr Troshin
 * @date July 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 pvt43
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.location.Location;

/**
 * DuplicateLocationFixer Merge mistakenly duplicated locations.
 * 
 * Note: specific to MPSI DB on July 2008
 */
public class LeedsDuplicateLocationFixer implements IDataFixer {

    String[] freezer1 =
        { "Freezer MPSI 1", "Freezer MPSI 1", "MPSI Freezer 1", ":MPSI Freezer 1", "Frezzer MPSI 1",
            "Freezer MPSi 1" };

    String[] chestfreezer =
        { "Chest freezer, -70oC", "-70 Chest Freezer 7.111", "-70 Chest Freezer Level 5",
            "70 Chest Freezer 7.111", "MPSI Freezer 7.111", "-70 CHest Freezer 7.111" };

    String[] uprightfreezer =
        { "Upright freezer, -70oC", "-70 Upright Freezer Level 5", "-70 Upright Freezer Leve 5",
            "-70 Upright Freezer", "-70 Upsright Freezer Level 5", "-70 Upright Freezer Level5",
            "-70 Upright Freezer Level 2", "-70 Upright Freezer 7.111" };

    String[] mpsifreezer = { "MPSILfreezer", "MPSI", "MPSIL freezer" };

    String[] labBoxUpright = { "SAB LAB SPARE,-70 upright freezer", "SAB LAB box, -70 upright..." };

    String[] labBoxChest = { "SAB LAB box,-70 chest freezer", "SAB LAB box, -70 chest..." };

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        Boolean allCorrect = true;

        final Collection<Location> locs = wv.getAll(Location.class, 0, Integer.MAX_VALUE);
        for (final Location loc : locs) {
            loc.getContents();

            System.out.println("Location " + loc.get_Hook() + " name " + loc.getName() + " is merged with ");
            // TODO complete! 
            allCorrect = false;
        }

        return allCorrect;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Merge duplicated locations with content preserved";
    }

}
