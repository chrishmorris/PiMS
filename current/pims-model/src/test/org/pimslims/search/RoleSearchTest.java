/**
 * pims-dm org.pimslims.search RoleSearchTest.java
 * 
 * @author cm65
 * @date 10 Sep 2010
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * RoleSearchTest
 * 
 */
public class RoleSearchTest extends TestCase {

    private static final String UNIQUE = "rs" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for RoleSearchTest
     * 
     * @param name
     */
    public RoleSearchTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testFindRoleByRole() throws ConstraintException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective ro = new org.pimslims.model.target.ResearchObjective(wv, UNIQUE, "test");
            final ResearchObjectiveElement element = new ResearchObjectiveElement(wv, "target", "test", ro);

            Assert.assertEquals(1, ro.getResearchObjectiveElements().size());
            final ComponentCategory cat = new ComponentCategory(wv, UNIQUE);
            final ModelObject molecule =
                element.findFirst(ResearchObjectiveElement.PROP_TRIALMOLECULES, Substance.PROP_CATEGORIES,
                    cat);
            Assert.assertNull(molecule);
        } finally {
            wv.abort();
        }
    }

    public void testFindMolecule() {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            Molecule found = wv.findFirst(Molecule.class, Substance.PROP_NAME, "nonesuch");
            Assert.assertNull(found);
        } finally {
            wv.abort();
        }
    }

    /* Reproduce the problem at a lower level
    public void testHibernate() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective ro = new org.pimslims.model.target.ResearchObjective(wv, UNIQUE, "test");
            final ResearchObjectiveElement element = new ResearchObjectiveElement(wv, "target", "test", ro);

            Assert.assertEquals(1, ro.getResearchObjectiveElements().size());
            final ComponentCategory cat = new ComponentCategory(wv, UNIQUE);
            cat.get_Hook();

            final String selectHQL =
                "select distinct tm from " + ResearchObjectiveElement.class.getName()
                    + " as roe inner join roe." + ResearchObjectiveElement.PROP_TRIALMOLECULES + " tm "
                    + "left join tm." + HQLBuilder.getHibernateName(AbstractComponent.PROP_CATEGORIES)
                    + " as  cat" + " where roe.dbId=:" + "A" + " and cat.dbId=:" + "B";
            final org.pimslims.presentation.PimsQuery query =
                element.get_Version().getSession().createQuery(selectHQL).setCacheable(false);
            hqlQuery.setParameter("A", element.getDbId());
            hqlQuery.setParameter("B", cat.getDbId());
            hqlQuery.setFirstResult(0);
            hqlQuery.setMaxResults(1);

            final Collection<ModelObject> res = hqlQuery.list();

            Collection<ModelObject> found = res;
            Assert.assertEquals(0, found.size());
        } finally {
            wv.abort();
        }
    } */

}
