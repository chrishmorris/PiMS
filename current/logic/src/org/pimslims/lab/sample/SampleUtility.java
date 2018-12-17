package org.pimslims.lab.sample;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.Util;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;

public class SampleUtility {

    public static String getSampleCategory(final Sample sample) {
        if (sample == null) {
            return null;
        }
        final Collection<SampleCategory> scats = sample.getSampleCategories();
        assert scats != null && scats.size() == 1 : "Warning: More then one or none sample categories in the sample, returning first!";
        for (final SampleCategory sc : sample.getSampleCategories()) {
            return sc.getName();
        }
        return null;
    }

    public static void linkSampleWithSupplier(final WritableVersion rw, final Sample sample,
        final Organisation organisation) throws AccessException, ConstraintException {

        //System.out.println("linkSampleWithSupplier [" + sample.get_Name() + ":" + organisation.get_Name()
        //    + "]");
        final RefSample rsample = sample.getRefSample();
        // Assume that the RefSample with the Organisation hook as a name is
        // associated with appropriate organisation

        //String name = organisation.get_Hook();
        final String name = organisation.get_Name() + " Primers";
        if (rsample == null || !rsample.getName().equals(name)) {
            final Map<String, Object> rsampleprop = new HashMap<String, Object>();
            rsampleprop.put(AbstractSample.PROP_NAME, name);
            RefSample refSample = rw.findFirst(org.pimslims.model.sample.RefSample.class, rsampleprop);
            if (refSample != null) {
                sample.setRefSample(refSample);
            } else {
                // Create new RefSample
                final SampleCategory sampleCat = Creator.recordSampleCategory(rw, name);
				//System.out.println("recordRefSample [" + sampleCat.get_Hook() + "]");
				final RefSample refSample1 = SampleFactory.getRefSample(rw, name, Collections.singleton(sampleCat));
				refSample = refSample1;
				final Map<String, Object> rsProp = Util.getNewMap();
				rsProp.put(ReagentCatalogueEntry.PROP_REAGENT, refSample);
				rsProp.put(ReagentCatalogueEntry.PROP_CATALOGNUM, "999");
				rsProp.put(ReagentCatalogueEntry.PROP_SUPPLIER, organisation);
				ReagentCatalogueEntry rfsource1;
				
				rfsource1 = (ReagentCatalogueEntry) Util.create(rw, ReagentCatalogueEntry.class, rsProp);
                final ReagentCatalogueEntry rfsource =
                    rfsource1;
                final Set<ReagentCatalogueEntry> rfs = new HashSet<ReagentCatalogueEntry>(2);
                rfs.add(rfsource);
                refSample.setRefSampleSources(rfs);
                sample.setRefSample(refSample);
                //System.out.println("REFSAMPLESET");
            }
        }

    }

    public static Organisation getOrganisation(final Sample sample) {
        final ReagentCatalogueEntry rfsource = SampleUtility.getRefSampleSource(sample);
        if (rfsource != null) {
            return rfsource.getSupplier();
        }
        return null;
    }

    public static ReagentCatalogueEntry getRefSampleSource(final Sample sample) {
        final RefSample rsample = sample.getRefSample();
        if (rsample == null) {
            return null; // assert rsample != null :"Please define RefSample
        }
        // for the Sample first";
        final Collection<ReagentCatalogueEntry> rfsources = rsample.getRefSampleSources();
        if (rfsources != null && !rfsources.isEmpty()) {
            return rfsources.iterator().next();
        }
        return null;
    }

    // get all expType and its name of sample related exp
    public static Map<ExperimentType, String> getRelatedExperimentType(final Sample sample) {
        final Map<ExperimentType, String> expTypes = new HashMap<ExperimentType, String>();
        if (sample.getInputSamples() != null) {
            for (final InputSample inputSample : sample.getInputSamples()) {
                if (inputSample.getExperiment() != null) {
                    final ExperimentType et = inputSample.getExperiment().getExperimentType();
                    expTypes.put(et, et.getName());
                }
            }
        }
        if (sample.getOutputSample() != null) {
            final OutputSample outputSample = sample.getOutputSample();
            if (outputSample.getExperiment() != null) {
                final ExperimentType et = outputSample.getExperiment().getExperimentType();
                expTypes.put(et, et.getName());
            }

        }

        return expTypes;

    }

    public static Sample copySample(final Sample sample) throws ConstraintException {
        final Map<String, Object> prop = sample.get_Values();
        prop.put(AbstractSample.PROP_NAME, sample.get_Version().getUniqueName(Sample.class, sample.getName()));
        return new Sample((WritableVersion) sample.get_Version(), prop);
    }
}
