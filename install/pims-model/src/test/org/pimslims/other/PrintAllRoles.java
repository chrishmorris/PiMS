/**
 * org.pimslims.metamodel testPrintAllRoles.java
 * 
 * @date 05-Nov-2006 07:52:09
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 * Copyright (c) 2006
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.other;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;

import junit.framework.TestCase;

/**
 * testPrintAllRoles
 * 
 */
public class PrintAllRoles extends TestCase {
    /**
     * The model being tested
     */
    protected final AbstractModel model = ModelImpl.getModel();

    public void XtestPrint1or2wayRole() {
        Collection<MetaRole> onewayMetaRoles = new HashSet<MetaRole>();
        Collection<MetaRole> twowayMetaRoles = new HashSet<MetaRole>();
        java.util.Collection<String> metaClassNames = model.getClassNames();
        for (String mClassName : metaClassNames) {
            MetaClass mc = model.getMetaClass(mClassName);
            for (MetaRole mr : ((MetaClassImpl) mc).getDeclaredMetaRoles().values()) {
                if (!mr.isOneWay())
                    twowayMetaRoles.add(mr);
                else
                    onewayMetaRoles.add(mr);
            }
        }
        System.out.println("One way roles:");
        doPrintRoles(onewayMetaRoles);
        System.out.println("\nTwo way roles:");
        doPrintRoles(twowayMetaRoles);
    }

    public void testPrintRoleCatagry() {
        Map<String, Set<MetaRole>> roleCatagries = new HashMap<String, Set<MetaRole>>();
        java.util.Collection<String> metaClassNames = model.getClassNames();
        for (String mClassName : metaClassNames) {
            MetaClass mc = model.getMetaClass(mClassName);
            for (MetaRole mr : ((MetaClassImpl) mc).getDeclaredMetaRoles().values()) {
                String roleCatagry = getRoleCatagry(mr);
                if (!roleCatagries.keySet().contains(roleCatagry))
                    roleCatagries.put(roleCatagry, new HashSet<MetaRole>());
                roleCatagries.get(roleCatagry).add(mr);
            }
        }

        for (String roleCatagry : roleCatagries.keySet()) {
            System.out.println(roleCatagry + " -size=" + roleCatagries.get(roleCatagry).size());
        }

        for (String roleCatagry : roleCatagries.keySet()) {
            System.out.println(roleCatagry + " -size=" + roleCatagries.get(roleCatagry).size());
            for (MetaRole mr : roleCatagries.get(roleCatagry)) {
                System.out.println(mr.getMetaClass().getName() + " : " + mr.getRoleName());
            }
        }

        // make sure no new cases
        for (String roleCatagry : roleCatagries.keySet()) {
            String[] rcs = roleCatagry.split("-");
            // no 1way role is parent or child role
            assertFalse(rcs[0].equalsIgnoreCase("1way") && !rcs[4].equalsIgnoreCase("Normal"));
            // no 1way role is 1 to many/1
            assertFalse(rcs[0].equalsIgnoreCase("1way") && !rcs[2].equalsIgnoreCase("(M"));
            // no 2way role is parent role and NotRequired
            assertFalse(rcs[0].equalsIgnoreCase("2way") && rcs[1].equalsIgnoreCase("NotRequired")
                && rcs[1].equalsIgnoreCase("Parent"));
            // no 2way role is child role and Required
            assertFalse(rcs[0].equalsIgnoreCase("2way") && rcs[1].equalsIgnoreCase("Required")
                && rcs[1].equalsIgnoreCase("child"));
        }

    }

    /**
     * @param mr
     * @return
     */
    private String getRoleCatagry(MetaRole mr) {
        String rc = "";
        String linkString;
        if (mr.isOneWay()) {
            rc += "1Way-";
            linkString = "->";
        } else {
            rc += "2Way-";
            linkString = "<->";
        }
        if (mr.getLow() > 0)
            rc += "Required-";
        else
            rc += "NotRequired-";

        if (mr.isOneWay())
            rc += "(M";
        else {
            int high = mr.getOtherRole().getHigh();
            if (high == -1)
                rc += "(M";
            else if (high == 1)
                rc += "(1";
            else
                // rc+="("+high+"";
                rc += "(M";
        }
        rc += linkString;
        // second part
        int high = mr.getHigh();
        if (high == -1)
            rc += "M)-";
        else if (high == 1)
            rc += "1)-";
        else
            // rc+=high+")-";
            rc += "M)-";

        if (((MetaRoleImpl) mr).isParentRole()) {
            rc += "Parent";
        } else if (((MetaRoleImpl) mr).isChildRole())
            rc += "Child";
        else
            rc += "Normal";

        return rc;
    }

    /**
     * @param metaRoles
     */
    private void doPrintRoles(Collection<MetaRole> metaRoles) {
        String seperater = ";";
        System.out.println("MetaClass" + seperater + "RoleName" + seperater + "OtherMetaClass" + seperater
            + "1Or2Way" + seperater + "isRequired" + seperater + "high" + seperater + "isParentRole"
            + seperater + "isChangeable" + seperater + "isAbstract" + seperater + "isDerived" + seperater
            + "isHidden" + seperater + "isOrdered");
        for (MetaRole mr : metaRoles) {

            String linkString;
            String oneOrTwoWay;
            if (mr.isOneWay()) {
                linkString = "->";
                oneOrTwoWay = "1Way";
            } else// twoway
            {
                linkString = "<->";
                oneOrTwoWay = "2Way";
            }

            String isRequired;
            if (mr.getLow() > 0)
                isRequired = "Yes";
            else
                isRequired = "No";

            String highOrLow;
            // first part
            if (mr.isOneWay())
                highOrLow = "?";
            else {
                int high = mr.getOtherRole().getHigh();
                if (high == -1)
                    highOrLow = "M";
                else if (high == 1)
                    highOrLow = "1";
                else
                    highOrLow = high + "";
            }
            highOrLow += linkString;
            // second part
            int high = mr.getHigh();
            if (high == -1)
                highOrLow += "M";
            else if (high == 1)
                highOrLow += "1";
            else
                highOrLow += high;

            String parentRole;
            if (((MetaRoleImpl) mr).isParentRole()) {
                parentRole = "Parent";
            } else if (((MetaRoleImpl) mr).isChildRole())
                parentRole = "Child";
            else
                parentRole = "Normal";

            String isAbstract;
            if (((MetaRoleImpl) mr).isAbstract()) {
                isAbstract = "Yes";
            } else
                isAbstract = "No";

            String isChangeable;
            if (((MetaRoleImpl) mr).isChangeable()) {
                isChangeable = "Yes";
            } else
                isChangeable = "No";

            String isDerived;
            if (((MetaRoleImpl) mr).isDerived()) {
                isDerived = "Yes";
            } else
                isDerived = "No";

            String isHidden;
            if (((MetaRoleImpl) mr).isHidden()) {
                isHidden = "Yes";
            } else
                isHidden = "No";

            String isOrdered;
            if (((MetaRoleImpl) mr).isOrdered()) {
                isOrdered = "Yes";
            } else
                isOrdered = "No";
            System.out.println(mr.getMetaClass().getShortName() + seperater + mr.getRoleName() + seperater
                + mr.getOtherMetaClass().getShortName() + seperater + oneOrTwoWay + seperater + isRequired
                + seperater + highOrLow + seperater + parentRole + seperater + isAbstract + seperater
                + isChangeable + seperater + isDerived + seperater + isHidden + seperater + isOrdered);
        }

    }

}
