/**
 * 
 */
package org.pimslims.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

/**
 * @author cm65
 * 
 */
public class TargetFindAllTester extends AbstractTestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TargetFindAllTester.class);
    }

    /**
     * Constructor for TestFindAllTargets.
     * 
     * @param methodName
     */
    public TargetFindAllTester(final String methodName) {
        super(methodName);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // 

    /*
     * public void testFindNone() { long start = System.currentTimeMillis(); try {
     * project.findAllTargets("commonName", "nonesuch"); System.out.println("Find targets with no matches took " + (
     * (System.currentTimeMillis()-start)/1000 ) +" seconds" ); } catch (ApiException e) {
     * fail(e.getLocalizedMessage()); } }
     */

    public void testFindAll() {

        final AbstractModel model = ModelImpl.getModel();

        final ReadableVersion rv = model.getReadableVersion(AbstractModel.SUPERUSER);
        final long start = System.currentTimeMillis();
        try {
            Hibernate.initialize(rv.findAll(Target.class, new HashMap<String, Object>()));
            //for (final Target t : rv.findAll(Target.class, new HashMap<String, Object>())) {
            /*
             * System.out.println("\nTarget:"+t.get_Hook()); Collection <MolComponent>
             * DNA=t.findAll(Target.PROP_NUCLEICACIDS, MolComponent.PROP_MOLTYPE, "DNA"); for(MolComponent
             * mol:DNA) { System.out.println(mol.getMolType()+" ; "+mol.getSeqString()); }
             */
            //System.out.println(t);
            //}
            System.out.println("Find all targets " + ((System.currentTimeMillis() - start) / 1000)
                + " seconds");
            rv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    public void testTargetAliasQuickSearch() throws AccessException, ConstraintException {
        wv = getWV();

        try {
            final MetaClass metaClass = this.wv.getMetaClass(Target.class);
            final Target t = create(Target.class);
            t.setAliasNames(Collections.singleton("alias of " + t.getName()));
            final Searcher s = new Searcher(wv);
            final Map criteria = new java.util.HashMap();
            final Paging paging = new Paging(0, 12);
            final Collection<ModelObject> targets = s.search(metaClass, criteria, "alias of ", paging);
            assertTrue(targets.size() > 0);
            assertEquals(t, targets.iterator().next());
        } finally {
            wv.abort();
        }
    }

    public void testTargetProtienQuickSearch() throws AccessException, ConstraintException {
        wv = getWV();

        try {
            final MetaClass metaClass = this.wv.getMetaClass(Target.class);
            final Target t = create(Target.class);
            final Molecule protein1 = this.create(Molecule.class);
            t.setProtein(protein1);
            final Searcher s = new Searcher(wv);
            final Map criteria = new java.util.HashMap();
            final Paging paging = new Paging(0, 12);
            final Collection<ModelObject> targets = s.search(metaClass, criteria, protein1.getName(), paging);
            assertTrue(targets.size() > 0);
            assertEquals(t, targets.iterator().next());
        } finally {
            wv.abort();
        }

    }
}
