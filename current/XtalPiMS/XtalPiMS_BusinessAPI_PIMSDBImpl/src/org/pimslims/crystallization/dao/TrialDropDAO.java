package org.pimslims.crystallization.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.pimslims.logging.Logger;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.core.model.TrialDrop}
 * </p>
 * 
 * @author Bill Lin
 */
public class TrialDropDAO {
    /**
     * TODO compare with SampleServiceImpl.PROTEIN This seems to be the right one. Input Sample name
     */
    public static final String PURIFIED_PROTEIN = "Purified protein";

    //static String TrialDropInputSampleCatoregoryName = PURIFIED_PROTEIN;

    private static final Logger log = Logger.getLogger(TrialDropDAO.class);

    public static Sample getpimsSample(final ReadableVersion version, final TrialDrop drop) {
        if (drop == null) {
            return null;
        }
        Sample pSample = null;
        if (drop.getId() != null) {
            pSample = version.get(drop.getId());
        }
        if (pSample == null) {
            pSample = version.findFirst(Sample.class, Sample.PROP_NAME, drop.getName());
        }

        return pSample;
    }

    public static void updateSample(final Sample pSample, final TrialDrop trialDrop)
        throws ConstraintException {
        if (trialDrop.getSamples() == null || trialDrop.getSamples().isEmpty()) {
            return;
        }
        final SampleQuantity xSample = trialDrop.getSamples().iterator().next();
        pSample.setCurrentAmount(new Float(xSample.getQuantity()));
        pSample.setAmountUnit(xSample.getUnit());
        pSample.setName(xSample.getSample().getName());
        pSample.setPh(new Float(xSample.getSample().getPH()));
        pSample.setDetails(xSample.getSample().getDescription());
        pSample.setBatchNum(xSample.getSample().getBatchReference());

    }

    static Collection<SampleCategory> getTrialDropSampleCategory(final WritableVersion wv) {
        SampleCategory sc = null;
        if (TrialPlateDAO.trialDropSampleCategoryID != null) {
            sc = wv.get(TrialPlateDAO.trialDropSampleCategoryID);
        } else {
            sc = wv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "TrialDrop");
            assert sc != null : "Can not find reference SampleCategory: TrialDrop ";
            TrialPlateDAO.trialDropSampleCategoryID = sc.get_Hook();
        }
        final Collection<SampleCategory> scs = new HashSet<SampleCategory>();
        scs.add(sc);
        return scs;
    }
}
