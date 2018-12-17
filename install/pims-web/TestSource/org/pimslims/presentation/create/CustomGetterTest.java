/**
 * V2_3-pims-web org.pimslims.presentation.servlet.utils CustomGetterTest.java
 * 
 * @author cm65
 * @date 7 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.Database;
import org.pimslims.presentation.AttributeToHTML;

/**
 * CustomGetterTest
 * 
 */
public class CustomGetterTest extends TestCase {

    /**
     * ROLE_IS_FULL String
     */
    private static final String ROLE_IS_FULL = "<!-- !parameters.canAssociateMore(this.role) -->";

    private static final String UNIQUE = "cg" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for CustomGetterTest
     * 
     * @param name
     */
    public CustomGetterTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testGetRolesSubset() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final MetaClass mainMetaClass = this.model.getMetaClass(ExternalDbLink.class.getName());
            final Map<String, String[]> parms = new HashMap();
            final Database modelObject = new Database(version, CustomGetterTest.UNIQUE);
            parms.put(ExternalDbLink.PROP_DATABASE, new String[] { modelObject.get_Hook() });
            final User author = new User(version, CustomGetterTest.UNIQUE);
            parms.put(Attachment.PROP_AUTHOR, new String[] { author.get_Hook() });
            final RoleHooksHolder hooks = new RoleHooksHolder(mainMetaClass);
            hooks.parse(parms);

            final Collection<MetaRole> roles = mainMetaClass.getMetaRoles().values();
            final ArrayList required = CustomGetter.getRolesSubset(true, roles, hooks);
            Assert.assertEquals(2, required.size());
            final ArrayList optional = CustomGetter.getRolesSubset(false, roles, hooks);
            Assert.assertEquals(1, optional.size());
        } finally {
            version.abort();
        }
    }

    public void testGetRolesSubsetNoParms() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final MetaClass mainMetaClass = this.model.getMetaClass(ExternalDbLink.class.getName());
            final Map<String, String[]> parms = new HashMap();
            final RoleHooksHolder hooks = new RoleHooksHolder(mainMetaClass);
            hooks.parse(parms);

            final Collection<MetaRole> roles = mainMetaClass.getMetaRoles().values();
            final ArrayList required = CustomGetter.getRolesSubset(true, roles, hooks);
            Assert.assertEquals(2, required.size());
            final ArrayList optional = CustomGetter.getRolesSubset(false, roles, hooks);
            Assert.assertEquals(0, optional.size());
        } finally {
            version.abort();
        }
    }

    public void testAttribute() {
        final MetaClass mainMetaClass = this.model.getMetaClass(ExternalDbLink.class.getName());
        final MetaClass pmeta = mainMetaClass;
        final Map<String, String[]> parms = new HashMap();
        parms.put(ExternalDbLink.PROP_URL, new String[] { CustomGetterTest.UNIQUE });
        final AttributeValueMap attributeValues = new AttributeValueMap(mainMetaClass, parms);
        final RoleHooksHolder hooks = new RoleHooksHolder(mainMetaClass);
        hooks.parse(parms);

        final Map errorMessages = new HashMap();
        final Collection<MetaAttribute> attributes = new HashSet<MetaAttribute>();
        attributes.add(mainMetaClass.getAttribute(ExternalDbLink.PROP_URL));
        final CustomGetter cg =
            new CustomGetter(null, pmeta, errorMessages, attributes, attributeValues, null, mainMetaClass,
                hooks, "nonesuch");
        final List<AttributeToHTML> attrs = cg.getSortedAtributes(false);
        Assert.assertEquals(1, attrs.size());
        final AttributeToHTML attr = attrs.iterator().next();
        Assert.assertTrue(attr.getHtml().contains(ExternalDbLink.PROP_URL));
        Assert.assertTrue(attr.getHtml().contains(CustomGetterTest.UNIQUE));
        Assert.assertFalse(attr.getHtml(), attr.getHtml().contains("readonly"));
        // note that there are now no unchangeable attributes in the model
        cg.getMissed(new HashMap());
    }

    public void testSeqString() {
        final MetaClass mainMetaClass = this.model.getMetaClass(Primer.class.getName());
        final MetaClass pmeta = mainMetaClass;
        final Map<String, String[]> parms = new HashMap();
        parms.put(Molecule.PROP_SEQUENCE, new String[] { CustomGetterTest.UNIQUE });
        final AttributeValueMap attributeValues = new AttributeValueMap(mainMetaClass, parms);
        final RoleHooksHolder hooks = new RoleHooksHolder(mainMetaClass);
        hooks.parse(parms);

        final Map errorMessages = new HashMap();
        final Collection<MetaAttribute> attributes = new HashSet<MetaAttribute>();
        attributes.add(mainMetaClass.getAttribute(Molecule.PROP_SEQUENCE));
        final CustomGetter cg =
            new CustomGetter(null, pmeta, errorMessages, attributes, attributeValues, null, mainMetaClass,
                hooks, "nonesuch");
        final List<AttributeToHTML> attrs = cg.getSortedAtributes(false);
        Assert.assertEquals(1, attrs.size());
        final AttributeToHTML attr = attrs.iterator().next();
        Assert.assertTrue(attr.getHtml().contains(Molecule.PROP_SEQUENCE));
        Assert.assertTrue(attr.getHtml().contains(CustomGetterTest.UNIQUE));
    }

    public void testGetOneOptRole() throws ConstraintException {
        final MetaClass mainMetaClass = this.model.getMetaClass(ExternalDbLink.class.getName());
        final MetaClass pmeta = mainMetaClass;

        final Map errorMessages = new HashMap();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final User modelObject = new User(version, CustomGetterTest.UNIQUE);
            final Map<String, String[]> parms = new HashMap();
            parms.put(Attachment.PROP_AUTHOR, new String[] { modelObject.get_Hook() });
            final AttributeValueMap attributeValues = new AttributeValueMap(mainMetaClass, parms);
            final RoleHooksHolder hooks = new RoleHooksHolder(mainMetaClass);
            hooks.parse(parms);

            final CustomGetter cg =
                new CustomGetter(version, pmeta, errorMessages, Collections.EMPTY_SET, attributeValues, pmeta
                    .getMetaRoles().values(), mainMetaClass, hooks, "nonesuch");
            Assert.assertTrue(errorMessages.containsKey("notProvided"));
            Assert.assertNotNull(cg.getOptRoleshtml());
            Assert.assertEquals(1, cg.getOptRoleshtml().size());
            final AttributeToHTML opt = cg.getOptRoleshtml().get(0);
            Assert.assertEquals(CustomGetterTest.ROLE_IS_FULL, opt.getHtml());
            Assert.assertTrue(opt.getHtml(), opt.getRoleAssociations().contains(modelObject.get_Name()));
            Assert.assertTrue(opt.getHtml(), opt.getRoleAssociations().contains(modelObject.get_Hook()));
        } finally {
            version.abort();
        }

    }

}
