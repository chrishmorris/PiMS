/**
 * org.pimslims.hibernate MetaClassImplTest.java
 * 
 * @date 15-May-2007 19:51:25
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.metamodel;

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.model.core.Citation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;

/**
 * MetaClassImplTest
 * 
 */
public class MetaClassImplTester extends TestCase {

    private final AbstractModel model;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MetaClassImplTester.class);
    }

    /**
     * Constructor for MetaClassImplTest.
     * 
     * @param arg0
     */
    public MetaClassImplTester(String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testGetJavaClass() {
        org.pimslims.metamodel.MetaClass refSampleMC = model.getMetaClass(RefSample.class.getName());
        assertEquals(RefSample.class, refSampleMC.getJavaClass());
    }

    public void testInheritedRole() {

        org.pimslims.metamodel.MetaClass refSampleMC = model.getMetaClass(RefSample.class.getName());
        verifyRefSampleRoles(refSampleMC);
        ((MetaClassImpl) refSampleMC).getParentRole();
        verifyRefSampleRoles(refSampleMC);
    }

    private void verifyRefSampleRoles(org.pimslims.metamodel.MetaClass refSampleMC) {
        Collection<String> roleNames = refSampleMC.getMetaRoles().keySet();
        assertTrue(roleNames.contains(AbstractSample.PROP_HAZARDPHRASES));
        for (String roleName : roleNames) {
            // System.out.println(roleName);
            assertNotNull(refSampleMC.getMetaRole(roleName));

        }
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getParentClass()'
     */
    public final void testGetParentClass() {
        MetaClassImpl m = (MetaClassImpl) model.getMetaClass(Parameter.class.getName());
        Class parent = m.getParentClass();
        assertEquals(Experiment.class, parent);
    }

    /*
     */
    public final void testGetParentRole() {
        MetaClassImpl m = (MetaClassImpl) model.getMetaClass(Parameter.class.getName());
        MetaRole parent = m.getParentRole();
        assertEquals("experiment", parent.getRoleName());

    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getAllAttributes()'
     */
    public final void testGetAllAttributes() {
        MetaClassImpl citation = (MetaClassImpl) model.getMetaClass(Citation.class.getName());
        model.getMetaClass(AbstractSample.class.getName());
        Map<String, MetaAttribute> all = citation.getAllAttributes();
        final Map<String, MetaAttribute> attributes = citation.getAttributes();
        assertTrue(all.keySet().containsAll(attributes.keySet()));
        assertTrue(all.containsKey("dbId"));
        assertFalse(attributes.containsKey("dbId"));
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getMetaRoles()'
     */
    public final void testGetMetaRoles() {

        MetaClassImpl citation = (MetaClassImpl) model.getMetaClass(Citation.class.getName());
        Map<String, MetaRole> all = citation.getMetaRoles();
        assertFalse(all.containsKey("project"));
        //assertTrue(all.containsKey("software"));
        //assertTrue(all.containsKey("access"));
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getAllMetaRoles()'
     */
    public final void testGetAllMetaRoles() {

        MetaClassImpl citation = (MetaClassImpl) model.getMetaClass(Citation.class.getName());
        Map<String, MetaRole> part = citation.getDeclaredMetaRoles();
        Map<String, MetaRole> all = citation.getMetaRoles();

        //assertTrue(all.containsKey("software"));
        assertFalse(part.containsKey("access"));
        assertTrue(all.keySet().containsAll(part.keySet()));

    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getSubtypes()'
     */
    public final void testGetSubtypes() {
        MetaClass sample = model.getMetaClass(Sample.class.getName());
        MetaClass abstractSample = model.getMetaClass(AbstractSample.class.getName());
        assertTrue(abstractSample.getSubtypes().contains(sample));
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getSupertype()'
     */
    public final void testGetSupertype() {
        MetaClass sample = model.getMetaClass(Sample.class.getName());
        MetaClass abstractSample = model.getMetaClass(AbstractSample.class.getName());
        assertEquals(abstractSample, sample.getSupertype());
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getShortName()'
     */
    public final void testGetShortName() {
        MetaClassImpl m = (MetaClassImpl) model.getMetaClass(Parameter.class.getName());
        assertEquals("Parameter", m.getShortName());
    }

    /*
     * Test method for 'org.pimslims.hibernate.CCPNMetaClass.getPackageShortName()'
     */
    public final void testGetPackageShortName() {
        MetaClassImpl m = (MetaClassImpl) model.getMetaClass(Parameter.class.getName());
        assertEquals("EXPE", m.getPackageShortName());

    }

}
