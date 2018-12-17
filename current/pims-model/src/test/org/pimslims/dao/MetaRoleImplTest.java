/**
 * org.pimslims.hibernate MetaRoleImplTest.java
 * 
 * @date 22 May 2007 15:04:22
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
package org.pimslims.dao;

import junit.framework.TestCase;

import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.SampleComponent;

/**
 * MetaRoleImplTest
 * 
 */
public class MetaRoleImplTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public MetaRoleImplTest(String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getOtherMetaClass()}.
     */
    public void testGetOtherMetaClass() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertEquals(model.getMetaClass(Experiment.class.getName()), r.getOtherMetaClass());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getLow()}.
     */
    public void testGetLow() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertEquals(1, r.getLow());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getHigh()}.
     */
    public void testGetHigh() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertEquals(1, r.getHigh());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getOtherRole()}.
     */
    public void testGetOtherRole() {

        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);

        MetaClass e = model.getMetaClass(Experiment.class.getName());
        MetaRole er = e.getMetaRole(Experiment.PROP_PARAMETERS);
        assertEquals(er, r.getOtherRole());

    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#isChangeable()}.
     */
    public void testIsChangeable() {

        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertFalse(r.isChangeable());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getOwnMetaClass()}.
     */
    public void testGetOwnMetaClass() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRole r = m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertEquals(m, r.getOwnMetaClass());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#isChildRole()}.
     */
    public void testIsChildRole() {
        MetaClass m = model.getMetaClass(Experiment.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Experiment.PROP_PARAMETERS);
        assertTrue(r.isChildRole());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#isParentRole()}.
     */
    public void testIsParentRole() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRoleImpl r = (MetaRoleImpl) m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertTrue(r.isParentRole());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#getMetaClass()}.
     */
    public void testGetMetaClass() {

        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRole r = m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertEquals(m, r.getMetaClass());
    }

    /**
     * Test method for {@link org.pimslims.metamodel.MetaRoleImpl#isRequired()}.
     */
    public void testIsRequired() {
        MetaClass m = model.getMetaClass(Parameter.class.getName());
        MetaRole r = m.getMetaRole(Parameter.PROP_EXPERIMENT);
        assertTrue(r.isRequired());
    }

    public void testIsOneWay() {
        MetaClass m = model.getMetaClass(SampleComponent.class.getName());
        MetaRole r = m.getMetaRole(SampleComponent.PROP_REFCOMPONENT);
        assertTrue(r.isOneWay());
        MetaClass a = model.getMetaClass(Substance.class.getName());
        assertEquals(a, r.getOtherMetaClass());
    }

    public final void testAccessAlias() {
        MetaClass m = model.getMetaClass(SampleComponent.class.getName());
        MetaRole r = m.getMetaRole(LabBookEntry.PROP_ACCESS);
        assertNotNull(r);
        assertEquals("Lab Notebook", r.getAlias());
    }

}
