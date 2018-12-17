/**
 * V4_3-web org.pimslims.presentation.create RoleHooksHolderTest.java
 * 
 * @author cm65
 * @date 9 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.create;

import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.presentation.create.RoleHooksHolder.RoleHooks;

/**
 * RoleHooksHolderTest
 * 
 */
public class RoleHooksHolderTest extends TestCase {

    private static final String UNIQUE = "rhh" + System.currentTimeMillis();

    /**
     * HOOK String
     */
    private static final String HOOK = RoleHooksHolderTest.UNIQUE + ":x";

    private final AbstractModel model;

    private final MetaClass metaClass;

    public RoleHooksHolderTest(final String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
        this.metaClass = this.model.getMetaClass(ReagentCatalogueEntry.class.getName());
    }

    public void testAdd() {
        final RoleHooksHolder rhh =
            new RoleHooksHolder(org.pimslims.model.sample.ReagentCatalogueEntry.class.getName());
        rhh.addHooks(org.pimslims.model.sample.ReagentCatalogueEntry.PROP_REAGENT,
            new String[] { RoleHooksHolderTest.UNIQUE });
        final RoleHooks rh = rhh.getRoleHook(ReagentCatalogueEntry.PROP_REAGENT);
        Assert.assertEquals("RoleName: refSample\n" + "Object hook: " + RoleHooksHolderTest.UNIQUE + "\n",
            rh.toString());
        final String[] fullHooks = rhh.getFullHooks(ReagentCatalogueEntry.PROP_REAGENT);
        final MetaRole role = this.metaClass.getMetaRole(ReagentCatalogueEntry.PROP_REAGENT);
        Assert.assertFalse(rhh.canAssociateMore(role));
        Assert.assertTrue(rhh.containsHookForRole(ReagentCatalogueEntry.PROP_REAGENT));
        Assert.assertFalse(rhh.isRequiredRolesProvided());
        Assert.assertTrue(rhh.isRequiredRoleProvided(role));
        final String[] missing = rhh.getEmptyMandatoryRoles();
        Assert.assertEquals(1, missing.length);
        Assert.assertEquals(ReagentCatalogueEntry.PROP_SUPPLIER, missing[0]);

        // TODO this does not look right:
        Assert.assertEquals(1, fullHooks.length);
        Assert.assertEquals(org.pimslims.model.sample.RefSample.class.getName() + ":"
            + RoleHooksHolderTest.UNIQUE, fullHooks[0]);
    }

    public void testRemove() {
        final RoleHooksHolder rhh =
            new RoleHooksHolder(org.pimslims.model.sample.ReagentCatalogueEntry.class.getName());
        // note that remove only works if the add was with an abbreviated hook
        rhh.addHooks(org.pimslims.model.sample.ReagentCatalogueEntry.PROP_REAGENT, new String[] { "x" });
        final HashMap map = new HashMap();
        map.put(org.pimslims.model.sample.ReagentCatalogueEntry.PROP_REAGENT,
            new String[] { RoleHooksHolderTest.HOOK });
        rhh.removeHooks(map);
        final RoleHooks rh = rhh.getRoleHook(ReagentCatalogueEntry.PROP_REAGENT);

        final MetaRole role = this.metaClass.getMetaRole(ReagentCatalogueEntry.PROP_REAGENT);
        Assert.assertTrue(rhh.canAssociateMore(role));
        Assert.assertFalse(rhh.containsHookForRole(ReagentCatalogueEntry.PROP_REAGENT));
        Assert.assertFalse(rhh.isRequiredRoleProvided(role));
        final String[] missing = rhh.getEmptyMandatoryRoles();
        Assert.assertEquals(2, missing.length);
    }

    public void testParseAbstractRole() {
        final RoleHooksHolder rhh =
            new RoleHooksHolder(org.pimslims.model.sample.ReagentCatalogueEntry.class.getName());
        rhh.parse("1*" + LabBookEntry.PROP_ATTACHMENTS + "=" + RoleHooksHolderTest.HOOK);
        final RoleHooks rh = rhh.getRoleHook(LabBookEntry.PROP_ATTACHMENTS);
        Assert.assertEquals("RoleName: " + LabBookEntry.PROP_ATTACHMENTS + "\n" + "Object hook: "
            + RoleHooksHolderTest.UNIQUE + ":x\n", rh.toString());
    }
}
