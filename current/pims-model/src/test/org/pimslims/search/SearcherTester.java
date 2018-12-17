package org.pimslims.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtilTester;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class SearcherTester extends AbstractTestCase {

    /**
     * SYNOMYM String
     */
    private static final String SYNONYM = "syn" + UNIQUE;

    Searcher searcher;

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rv = getRV();
        searcher = new Searcher(rv);
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (!rv.isCompleted()) {
            rv.abort();
        }
        model.disconnect();
        searcher = null;
        super.tearDown();
    }

    //TODO test with Molecule, there may be an issue in Oracle
    public void testCriteriaSearchWithAnyValue() throws AccessException, ConstraintException {
        wv = getWV();
        final Searcher searcher = new Searcher(wv);
        final MetaClass metaClass = wv.getMetaClass(Sample.class);
        final Map<String, Object> criteria = new HashMap<String, Object>();
        final Map<String, Object> scCriteria = new HashMap<String, Object>();
        final String sampleDetails = "sampleDetails";
        try {
            //preparing testing data
            final Sample sample = create(Sample.class);
            sample.setDetails(sampleDetails);
            final SampleCategory sc = create(SampleCategory.class);
            sample.addSampleCategory(sc);
            final Sample sample2 = create(Sample.class);
            sample2.setDetails(sampleDetails);
            //do search 
            scCriteria.put(SampleCategory.PROP_NAME, sc.getName());
            criteria.put(AbstractSample.PROP_SAMPLECATEGORIES, scCriteria);
            final Collection<ModelObject> results = searcher.search(metaClass, criteria, sampleDetails);
            //verify results
            assertEquals(1, results.size());
            assertEquals(sample, results.iterator().next());
            assertEquals(1, searcher.count(metaClass, criteria, sampleDetails));
            wv.abort();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-1687 when search Sample components, primer will be
     * duplicated in result
     * 
     * @throws AbortedException
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testSearchPrimer() throws AbortedException, ConstraintException, AccessException {
        Primer primer = null;
        wv = getWV();
        try {
            primer = create(Primer.class);

            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        final MetaClass m = model.getMetaClass(Substance.class.getName());
        final ArrayList<ModelObject> result = searcher.searchAll(m, primer.getName());
        assertEquals(1, result.size());
        assertEquals(primer.getName(), result.iterator().next().get_Name());

    }

    public void testCountRecords() throws AbortedException, ConstraintException, AccessException {
        wv = getWV();
        try {
            create(Sample.class);
            create(Sample.class);
            create(Sample.class);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        final MetaClass m = model.getMetaClass(Sample.class.getName());
        final long start = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
        final long startTime = System.currentTimeMillis();
        final long count = searcher.countRecords(m);
        assertTrue(count >= 3);
        final long end = HibernateUtilTester.getStatistics(model).getCloseStatementCount();
        final long endTime = System.currentTimeMillis();
        System.out.println("count " + count + " samples using: " + (end - start) + " statements and "
            + (endTime - startTime) / 1000.0 + "'s");
        assertTrue((end - start) < 2);
    }

    public void testTargetQuery() {
        final Map criteria = new HashMap();
        criteria.put(Target.PROP_NAME, "a");
        ArrayList results = null;
        final MetaClass m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        final long start = System.currentTimeMillis();
        results = searcher.search(criteria, m);
        final long finish = System.currentTimeMillis();
        log("Finish Searching target by criteria " + criteria.toString() + " at " + new Date(finish));
        log("Running time " + (finish - start) / 1000.0 + " s" + "; size=" + results.size());
    }

    public void testTargetSearchAllBySearcher() {
        ArrayList<ModelObject> results = null;
        final MetaClass m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        String targetName1 = null;
        String targetName2 = null;
        // create some targets for testing
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            final Target target = create(Target.class, Target.PROP_NAME, "a nBmec" + UNIQUE);
            final Target target2 = create(Target.class, Target.PROP_NAME, "abc" + new Date());
            targetName1 = target.get_Name();
            targetName2 = target2.get_Name();
            assertNotNull(target);
            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        final long start = System.currentTimeMillis();
        results = this.searcher.searchAll(m, "Bmec" + UNIQUE);
        assertEquals(1, results.size());
        assertTrue(HaveName(results, targetName1));
        assertFalse(HaveName(results, targetName2));
        final long finish = System.currentTimeMillis();
        log("Finish Searching " + m.getName() + ":" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

        assertEquals(1, searcher.search(m, Collections.EMPTY_MAP, "Bmec" + UNIQUE, new Paging(0, 2)).size());

    }

    /**
     * @param results
     * @param targetName1
     * @return
     */
    private boolean HaveName(final ArrayList<ModelObject> results, final String Name) {
        for (final ModelObject modelObject : results) {
            if (modelObject.get_Name().equals(Name)) {
                return true;
            }
        }
        return false;
    }

    public void testTargetSearchWith2Criteria() {
        ArrayList<ModelObject> results = null;
        final MetaClass m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        final String targetNameForSearch = "a nBmec" + System.currentTimeMillis();
        final String targetDetailsForSearch = "test details search";
        // create some targets for testing
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            // create 2 target with same details
            Target target = create(Target.class, Target.PROP_NAME, targetNameForSearch);
            target.setDetails(targetDetailsForSearch);
            target = create(Target.class);
            target.setDetails(targetDetailsForSearch);
            assertNotNull(target);
            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        final long start = System.currentTimeMillis();
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(Target.PROP_NAME, targetNameForSearch);
        criteria.put(LabBookEntry.PROP_DETAILS, targetDetailsForSearch);
        results = this.searcher.search(criteria, m);
        assertTrue(results.size() == 1);
        final long finish = System.currentTimeMillis();
        log("Finish Searching " + m.getName() + ":" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

    }

    public void testTargetSearchableFields() throws AccessException, ConstraintException {
        Class<Target> testClass = Target.class;
        final MetaClass m = model.getMetaClass(testClass.getName());

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            create(testClass);
            final Map<String, MetaAttribute> searchableFields = wv.getSearchableFields(m);
            assertNotNull(searchableFields);
            assertTrue(searchableFields.size() > 0);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    // There was an error caused by Citation's multivalue attributes keywords
    public void testCitationSearchAll() {
        Collection<ModelObject> results = null;
        final MetaClass m = model.getMetaClass(org.pimslims.model.core.Citation.class.getName());

        final long start = System.currentTimeMillis();
        results = this.searcher.searchAll(m, "a");
        assertNotNull(results);
        final long finish = System.currentTimeMillis();
        log("Finish Searching " + m.getName() + ":" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

    }

    public void _testTargetSearchAllWithAlias() throws AccessException, ConstraintException, AbortedException {
        Collection<ModelObject> results = null;
        final MetaClass m = model.getMetaClass(Target.class.getName());
        final String alias1 = "alias1" + System.currentTimeMillis();
        final String alias2 = "alias2" + System.currentTimeMillis();
        wv = getWV();
        try {
            create(Target.class, Target.PROP_NAME, "a nBmec" + new Date());
            final List<String> aliases = new LinkedList<String>();
            aliases.add(alias1);
            aliases.add(alias2);
            //target.setAliases(aliases);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        results = this.searcher.searchAll(m, alias1);
        assertEquals(1, results.size());

        //TODO delete it now
    }

    public void testAbstractComponentSearchAll() throws ConstraintException, AbortedException,
        AccessException {
        wv = getWV();
        String hook = null;
        final String searchValue = "NaCl" + System.currentTimeMillis();
        try {
            final Molecule mc = create(Molecule.class);
            mc.setEmpiricalFormula(searchValue);
            hook = mc.get_Hook();
            wv.commit();
        } finally {
            wv.close();
        }

        final Searcher searcher = new Searcher(rv);
        // check searchable fields
        final HashMap<String, MetaAttribute> searchableFields =
            searcher.getSearchableFields(rv.getMetaClass(Substance.class));
        assertTrue(searchableFields.keySet().contains(Molecule.PROP_EMPIRICALFORMULA));
        // check search result
        final ArrayList<ModelObject> results =
            searcher.searchAll(rv.getMetaClass(Substance.class), searchValue);

        assertTrue(results.contains(rv.get(hook)));
        //TODO delete it now
    }

    public void testMoleculeSynonymnSearch() throws AccessException, ConstraintException {
        wv = getWV();

        try {
            final MetaClass metaClass = this.wv.getMetaClass(Molecule.class);
            final Molecule molecule = create(Molecule.class);
            List<String> synonyms = Collections.singletonList(SYNONYM);
            molecule.setSynonyms(synonyms);
            wv.flush();

            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Substance.PROP_SYNONYMS, Conditions.listContains(SYNONYM));
            Searcher s = new Searcher(wv);
            Collection<ModelObject> results = s.search(criteria, metaClass);
            assertEquals(1, results.size());
            assertEquals(molecule, results.iterator().next());

            final Paging paging = new Paging(0, 12);
            final Collection<ModelObject> found = s.searchAll(metaClass, UNIQUE, paging);
            assertEquals(1, found.size());
            assertEquals(molecule, found.iterator().next());
        } finally {
            wv.abort();
        }
    }

    // the search is slower than search all, why?
    public void testCompareSearchWithSearchAll() {
        Collection<ModelObject> results = null;
        MetaClass m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());

        long start = System.currentTimeMillis();
        results = this.searcher.searchAll(m, "a");
        assertNotNull(results);
        long finish = System.currentTimeMillis();
        log("Finish Searching(SearchAll) Target" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

        results = null;
        m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        final Map<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put(Target.PROP_NAME, "a");
        start = System.currentTimeMillis();
        results = this.searcher.search(attributeMap, m);
        assertNotNull(results);
        finish = System.currentTimeMillis();
        log("Finish Searching(Search) Target.COMMONNAME:" + " cost " + (finish - start) / 1000.0 + " s"
            + "; size=" + results.size());

        results = null;
        m = model.getMetaClass(org.pimslims.model.target.Target.class.getName());

        start = System.currentTimeMillis();
        results = this.searcher.searchAll(m, "a");
        assertNotNull(results);
        finish = System.currentTimeMillis();
        log("Finish Searching(SearchAll) Target" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

    }

    // There was an error caused by BlueprintComponen boolean attributes
    // keywords
    public void testBlueprintComponenSearchAll() {
        Collection<ModelObject> results = null;
        final MetaClass m =
            model.getMetaClass(org.pimslims.model.target.ResearchObjectiveElement.class.getName());

        final long start = System.currentTimeMillis();
        results = this.searcher.searchAll(m, "a");
        assertNotNull(results);
        final long finish = System.currentTimeMillis();
        log("Finish Searching " + m.getName() + ":" + " cost " + (finish - start) / 1000.0 + " s" + "; size="
            + results.size());

    }

    // test findfirst with boolean value
    public void testFindFirstWithBooleanValue() {
        wv = super.getWV();
        try {
            final Holder holder = POJOFactory.createHolder(wv);
            Sample sample = POJOFactory.createSample(wv);
            sample.setIsActive(true);
            holder.addSample(sample);
            verifySample(holder);

            sample = POJOFactory.createSample(wv);
            holder.addSample(sample);
            sample.setIsActive(false);
            verifySample(holder);

            sample = POJOFactory.createSample(wv);
            sample.setIsActive(null);
            holder.addSample(sample);
            verifySample(holder);

        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testSearchExperiment() throws ConstraintException {
        final MetaClass metaClass = model.getMetaClass(Experiment.class.getName());
        wv = super.getWV();
        try {
            final ExperimentType type = new ExperimentType(wv, "test" + System.currentTimeMillis());
            final Searcher s = new Searcher(wv);
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
            final Collection<ModelObject> results = s.search(criteria, metaClass);
            assertNotNull(results);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    private void verifySample(final Holder holder) {
        final Sample sampleFromHolder =
            holder.findFirst(Holder.PROP_SAMPLES, AbstractSample.PROP_ISACTIVE, true);
        verifySample(sampleFromHolder);
        final Sample sampleFound = wv.findFirst(Sample.class, AbstractSample.PROP_ISACTIVE, true);
        verifySample(sampleFound);
    }

    private void verifySample(final Sample sampleFound) {
        assertNotNull(sampleFound);
        assertNotNull(sampleFound.getIsActive());
        assertTrue(sampleFound.getIsActive());
    }
}
