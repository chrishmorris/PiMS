/*
 * Created on 03-Feb-2005 @author: Chris Morris
 */
package org.pimslims.data;

import java.util.Collections;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.reference.SampleCategory;

/**
 * Command line utility for adding reference data from CSV files for reagents Data now is:
 * "name","details","role/category","experiment types","catNum","supplier","dpURL"
 * 
 * LATER still not using "details","role/category","experiment types"
 * 
 * LATER aslo need information about sample components, including molecules and hazards
 * 
 */
public class ReagentUtility extends AbstractLoader {

    private ReagentUtility() {
        // no constructor, just static methods
    }

    /**
     * @param filename an Excel CSV file with reagent details
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {
        final java.io.Reader reader = new java.io.FileReader(filename);

        final CsvParser lcsvp = new CsvParser(reader);

        while (lcsvp.getLine() != null) {
            final String name = lcsvp.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }
            //get SampleCategory (should be loaded by SampleCatsUtility)
            final String sampleCatName = lcsvp.getValueByLabel("category");
            final SampleCategory sampleCategory =
                wv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, sampleCatName);
            if (sampleCategory == null) {
                throw new RuntimeException("Can not find sampleCategory: " + sampleCatName);
            }

            //refSample's full attributes
            final Map<String, Object> refSampleFullAttrMap = new java.util.HashMap<String, Object>();
            refSampleFullAttrMap.put(AbstractSample.PROP_NAME, name);
            refSampleFullAttrMap.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections
                .singleton(sampleCategory));
            //refSample's key attribute
            final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
            keyAttrMap.put(AbstractSample.PROP_NAME, name);

            final RefSample refSample =
                (RefSample) AbstractLoader.UpdateOrCreate(wv, RefSample.class, keyAttrMap, null,
                    refSampleFullAttrMap);

            if (sampleCatName.length() > 0) {
                assert refSample.getSampleCategories().size() > 0 : "sampleCategory is not created/added for refSample";
                assert refSample.getSampleCategories().iterator().next().getName().equals(sampleCatName);
            }
            //get supplier (should be loaded by SupplierUtility)
            final String supplierName = lcsvp.getValueByLabel("supplier");
            if ("".equals(supplierName.trim())) {
                // no supplier
                continue;
            }
            final Organisation supplier =
                wv.findFirst(Organisation.class, Organisation.PROP_NAME, supplierName);
            if (supplier == null) {
                throw new RuntimeException("Can not find supplier: " + supplierName);
            }

            if (null != supplier && null != refSample) {
                String catNum = lcsvp.getValueByLabel("catNum");
                if ("".equals(catNum.trim())) {
                    catNum = "Unknown"; //catNum has a not-null constraint
                }

                //RefSampleSource's full attributes
                final Map<String, Object> refSampleSourceFullAttrMap =
                    new java.util.HashMap<String, Object>();
                refSampleSourceFullAttrMap.put(ReagentCatalogueEntry.PROP_REFSAMPLE, refSample);
                refSampleSourceFullAttrMap.put(ReagentCatalogueEntry.PROP_SUPPLIER, supplier);
                refSampleSourceFullAttrMap.put(ReagentCatalogueEntry.PROP_CATALOGNUM, catNum);
                refSampleSourceFullAttrMap.put(ReagentCatalogueEntry.PROP_DATAPAGEURL, lcsvp
                    .getValueByLabel("dpURL"));

                //RefSampleSource's key attribute
                final Map<String, Object> refSampleSourcekeyAttrMap = new java.util.HashMap<String, Object>();
                refSampleSourcekeyAttrMap.put(ReagentCatalogueEntry.PROP_REFSAMPLE, refSample);
                refSampleSourcekeyAttrMap.put(ReagentCatalogueEntry.PROP_SUPPLIER, supplier);

                AbstractLoader.UpdateOrCreate(wv, ReagentCatalogueEntry.class, refSampleSourcekeyAttrMap, null,
                    refSampleSourceFullAttrMap);
            }
        }
    }

    /**
     * @param args a list of file names to load
     */
    public static void main(final String[] args) {

        if (0 == args.length) {
            AbstractLoader.print("Usage: ReagentUtility filename filename ...");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                ReagentUtility.loadFile(wv, filename);
                wv.commit();
                AbstractLoader.print("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                AbstractLoader.print("Unable to read from file: " + filename);
                ex.printStackTrace();
            } catch (final ModelException ex) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                ex.printStackTrace();
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        AbstractLoader.print("Reagent utility has finished");
    }

}
