/**
 * V2_0-datamodel org.pimslims.hibernate MetaUtilsTest.java
 * 
 * @author cm65
 * @date 14 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.dao;

import junit.framework.TestCase;

import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaUtils;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;

/**
 * MetaUtilsTest
 * 
 */
public class MetaUtilsTest extends TestCase {

    private final AbstractModel model = ModelImpl.getModel();

    /**
     * @param name
     */
    public MetaUtilsTest(String name) {
        super(name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#isSuperAttribute(org.pimslims.metamodel.MetaAttribute)}.
     */
    public void testIsSuperAttribute() {
        MetaAttribute attribute =
            this.model.getMetaClass(Target.class.getName()).getAttribute(Target.PROP_NAME);
        assertFalse(MetaUtils.isSuperAttribute(attribute));
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaUtils#getDBType(java.lang.String)}.
     */
    public void testGetDBType() {
        assertEquals("INT8", MetaUtils.getDBType("bigint"));
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getSubClassTableName(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetSupperClassTableName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        String table = MetaUtils.getSupperClassTableName(metaClass);
        assertEquals("SAM_AbstractSample".toUpperCase(), table);
    }

    public void testGetSupperClassTableNamePublic() {
        MetaClass metaClass = model.getMetaClass(Database.class.getName());
        String table = MetaUtils.getSupperClassTableName(metaClass);
        assertEquals("REF_PUBLICENTRY".toUpperCase(), table);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getSubClassId(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetKeyName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        String key = MetaUtils.getKeyName(metaClass);
        assertEquals("AbstractSampleId", key);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getSubClassId(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetKeyNamePublic() {
        MetaClass metaClass = model.getMetaClass(Database.class.getName());
        String key = MetaUtils.getKeyName(metaClass);
        assertEquals("PublicEntryId", key);
    }

    //publicentryid

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getSubClassFkName(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetSubClassFkName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        String key = MetaUtils.getSubClassFkName(metaClass);
        assertEquals("sam_sample_abstsa".toUpperCase(), key.toUpperCase());
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getSuperclassName(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetSuperclassName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        String name = MetaUtils.getSuperclassName(metaClass);
        assertEquals(AbstractSample.class.getSimpleName(), name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getThisRoleTableName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetThisRoleTableName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        MetaRole role = metaClass.getMetaRole(AbstractSample.PROP_SAMPLECATEGORIES);
        String name = MetaUtils.getThisRoleTableName(role);
        assertEquals("REF_SampleCategory".toUpperCase(), name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getThisRoleFkName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetThisRoleFkName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        MetaRole role = metaClass.getMetaRole(AbstractSample.PROP_SAMPLECATEGORIES);
        String name = MetaUtils.getThisRoleFkName(role);
        assertEquals("SAM_AbstSa_sampCa", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getOtherRoleTableName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetOtherRoleTableName() {
        MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        MetaRole role = metaClass.getMetaRole(AbstractSample.PROP_SAMPLECATEGORIES);
        String name = MetaUtils.getOtherRoleTableName(role);
        assertEquals("SAM_AbstractSample".toUpperCase(), name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getOtherRoleFkName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetOtherRoleFkName() {
        MetaClass metaClass = model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(org.pimslims.model.target.Target.PROP_TARGETGROUPS);
        String name = MetaUtils.getOtherRoleFkName("TARG_Project", role);
        assertEquals("TARG_TargGr_targets", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getThisRoleColName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetThisRoleColName() {
        MetaClass metaClass = model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(org.pimslims.model.target.Target.PROP_PROJECTS);
        String name = MetaUtils.getThisRoleColName(role);
        assertEquals("PROJECTID", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getOtherRoleColName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetOtherRoleColName() {
        MetaClass metaClass = model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(org.pimslims.model.target.Target.PROP_PROJECTS);
        String name = MetaUtils.getOtherRoleColName(role);
        assertEquals("TARGETID", name);
    }

    public void testColNamesM2M() {
        MetaRole role = model.getMetaClass(UserGroup.class.getName()).getMetaRole(UserGroup.PROP_MEMBERUSERS);
        assertEquals("USERGROUPID", MetaUtils.getOtherRoleColName(role));
        assertEquals("MEMBERUSERID", MetaUtils.getThisRoleColName(role));

        role = model.getMetaClass(Substance.class.getName()).getMetaRole(Substance.PROP_CATEGORIES);
        assertEquals("COMPONENTID", MetaUtils.getOtherRoleColName(role));
        assertEquals("CATEGORYID", MetaUtils.getThisRoleColName(role));

    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaUtils#getSingleName(java.lang.String)}.
     */
    public void testGetSingleName() {
        //MetaClass metaClass = model.getMetaClass(Sample.class.getName());
        String name = MetaUtils.getSingleName(AbstractSample.PROP_SAMPLECATEGORIES);
        assertEquals("sampleCategory", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getMToMTableName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetMToMTableName() {
        MetaClass metaClass = model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(org.pimslims.model.target.Target.PROP_PROJECTS);
        assertFalse(MetaUtils.isOneToManyRole(role));
        String name = MetaUtils.getMToMTableName(role);
        assertEquals("targ_target2projects".toLowerCase(), name.toLowerCase());
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getRoleTableName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetRoleTableName() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_PROTEIN);
        String name = MetaUtils.getRoleTableName(role);
        assertEquals("MOLE_Molecule".toUpperCase(), name);
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaUtils#getRoleFkName(org.pimslims.metamodel.MetaRole)}
     * .
     */
    public void testGetRoleFkName() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_PROTEIN);
        String name = MetaUtils.getRoleFkName(role);
        assertEquals("TARG_Target_protein", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getRoleColName(org.pimslims.metamodel.MetaRole)}.
     */
    public void testGetRoleColName() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_PROTEIN);
        String name = MetaUtils.getRoleColName(role);
        assertEquals("proteinId".toUpperCase(), name);
    }

    /**
     * /** Test method for
     * {@link org.pimslims.metamodel.MetaUtils#qualifiedNameofRole(org.pimslims.metamodel.MetaRole)}.
     */
    public void testQualifiedNameofRole() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_PROTEIN);
        String name = MetaUtils.qualifiedNameofRole(role);
        assertEquals("org.pimslims.model.target.Target.protein", name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#isManyToOne_or_OneToOneRole(org.pimslims.metamodel.MetaRole)}.
     */
    public void testIsManyToOne_or_OneToOneRole() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_PROTEIN);
        assertTrue(MetaUtils.isManyToOne_or_OneToOneRole(role));
        assertFalse(MetaUtils.isOneToManyRole(role));
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#isOneToManyRole(org.pimslims.metamodel.MetaRole)}.
     */
    public void testIsOneToManyRole() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaRole role = metaClass.getMetaRole(Target.PROP_RESEARCHOBJECTIVEELEMENTS);
        assertFalse(MetaUtils.isManyToOne_or_OneToOneRole(role));
        assertTrue(MetaUtils.isOneToManyRole(role));
    }

    /**
     * Test method for
     * {@link org.pimslims.hibernate.CCPNMetaUtils#isIgnoredRole(org.pimslims.metamodel.MetaRole)}.
     * 
     * could public void testIsIgnoredRole() { fail("Not yet implemented"); }
     */

    /**
     * Test method for {@link org.pimslims.metamodel.MetaUtils#getColumnName(java.lang.String)}.
     */
    public void testGetColumnName() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        MetaAttribute ma = metaClass.getAttribute(LabBookEntry.PROP_DETAILS);
        String name = MetaUtils.getColumnName(ma);
        assertEquals("details".toUpperCase(), name);
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getMultiAttributeTableName(org.pimslims.metamodel.MetaClass, java.lang.String)}
     * .
     */
    public void testGetMultiAttributeTableName() {
        MetaClass metaClass = model.getMetaClass(Organisation.class.getName());
        String name = MetaUtils.getMultiAttributeTableName(metaClass, Organisation.PROP_ADDRESSES);
        assertEquals("PEOP_Orga_addresses", name);
    }

    public void testGetMultiAttributeTableName2() {
        MetaClass metaClass = model.getMetaClass(PublicEntry.class.getName());
        String name = MetaUtils.getMultiAttributeTableName(metaClass, PublicEntry.PROP_SAME_AS);
        assertEquals("REF_PublEn_sameAs", name);
    }

    /**
     * Test method for {@link org.pimslims.hibernate.CCPNMetaUtils#getSqlType(java.lang.Class, int)}.
     * 
     * could public void testGetSqlType()
     */

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#columnTypeCheck(org.pimslims.metamodel.MetaAttribute, java.lang.String)}
     * . TODO public void testColumnTypeCheck() { fail("Not yet implemented"); } *>
     * 
     * /** Test method for {@link org.pimslims.metamodel.MetaUtils#isMultiAttribute(java.lang.Class)}.
     */
    public void testIsMultiAttribute() {
        assertTrue(MetaUtils.isMultiAttribute(String[].class));
    }

    /**
     * Test method for
     * {@link org.pimslims.metamodel.MetaUtils#getPackageShortName(org.pimslims.metamodel.MetaClass)}.
     */
    public void testGetPackageShortName() {
        MetaClass metaClass = this.model.getMetaClass(Target.class.getName());
        String name = MetaUtils.getPackageShortName(metaClass);
        assertEquals("TARG", name);
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaUtils#getStandardName(java.lang.String)}.
     */
    public void testGetStandardRoleName() {
        assertEquals("conformings", MetaUtils.getStandardName("HbConformings"));
    }

}
