package org.pimslims.servlet.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Substance;
import org.pimslims.presentation.servlet.utils.RecordsFilter;

public class RecordsFilterTester extends TestCase {

    /**
     * Values for creating a test sample
     */
    public static HashMap<MetaAttribute, String> criteria = new HashMap<MetaAttribute, String>();

    public static Collection<ModelObject> results = new ArrayList<ModelObject>();

    AbstractModel model;

    ReadableVersion rv;

    @Override
    public void setUp() {
        this.model = ModelImpl.getModel();
        this.rv = this.model.getReadableVersion(AbstractModel.SUPERUSER);
    }

    @Override
    public void tearDown() {
        this.model = null;
        this.rv.abort();
    }

    public void testFilter() {
        final HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(Substance.PROP_NAME, "TES");
        final Collection results = this.rv.findAll(org.pimslims.model.molecule.Molecule.class, param);
        // System.out.println("res size " + results.size());
        final MetaRole role =
            (this.model.getMetaClass((org.pimslims.model.molecule.Molecule.class.getName()))
                .getMetaRole(Substance.PROP_CATEGORIES));

        final MetaAttribute metaAttr =
            (this.model.getMetaClass((org.pimslims.model.reference.ComponentCategory.class.getName()))
                .getAttribute(org.pimslims.model.reference.ComponentCategory.PROP_NAME));

        RecordsFilterTester.criteria.put(metaAttr, "Buffering agent");
        //final Collection<ModelObject> filteredResults =
        RecordsFilter.filter(results, RecordsFilterTester.criteria, role);
        // System.out.println("Filtered res size " + filteredResults.size());
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(RecordsFilterTester.class);
    }

}
