package org.pimslims.presentation.sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

/**
 * Factory class to populate the fields of a Presentation layer TargetBean
 * 
 */
public class SampleBeanWriter {

    public static Sample createNewSample(final WritableVersion version, final SampleBean sb,
        final Collection<SampleCategory> sampleCategories, final RefSample refSample) throws AccessException,
        ConstraintException {

        final HashMap<String, Object> sampleAttributes = new HashMap<String, Object>();

        sampleAttributes.put(Sample.PROP_AMOUNTDISPLAYUNIT, sb.getAmountDisplayUnit());
        sampleAttributes.put(Sample.PROP_AMOUNTUNIT, sb.getAmountUnit());
        sampleAttributes.put(Sample.PROP_BATCHNUM, sb.getBatchNum());
        sampleAttributes.put(Sample.PROP_COLPOSITION, sb.getColPosition());
        sampleAttributes.put(Sample.PROP_CURRENTAMOUNT, sb.getCurrentAmount());
        sampleAttributes.put(Sample.PROP_CURRENTAMOUNTFLAG, sb.getCurrentAmountFlag());
        sampleAttributes.put(LabBookEntry.PROP_DETAILS, sb.getDetails());

        LabNotebook access = null;
        if (null != sb.getAccess()) {
            access = (LabNotebook) version.get(sb.getAccess().getHook());
            sampleAttributes.put(LabBookEntry.PROP_ACCESS, access);
        }
        // sampleAttributes.put(Sample.PROP_DROPANNOTATIONS, "");
/*
        List<HazardPhrase> hazards = new ArrayList();
        Object[] hazardPhases = sb.getHazardPhases();
        for (int i = 0; i < hazardPhases.length; i++) {
            hazards.add((HazardPhrase) version.get((String) hazardPhases[i]));
        }
        sampleAttributes.put(AbstractSample.PROP_HAZARDPHRASES, hazards);
*/

        if (null != sb.getHolderHook()) {
            final Holder holder = (Holder) version.get(sb.getHolderHook());
            sampleAttributes.put(Sample.PROP_HOLDER, holder);
        }
/*
            List<org.pimslims.model.crystallization.Image> images = new ArrayList();
            Object[] myImages = sb.getImages();
            for (int i = 0; i < myImages.length; i++) {
                images.add((org.pimslims.model.crystallization.Image) version.get((String) myImages[i]));
            }
            sampleAttributes.put(Sample.PROP_IMAGES, images);

            List<org.pimslims.model.experiment.InputSample> inputSamples = new ArrayList();
            Object[] myInputs = sb.getInputSamples();
            for (int i = 0; i < myInputs.length; i++) {
                inputSamples.add((org.pimslims.model.experiment.InputSample) version.get((String) myInputs[i]));
            }
            sampleAttributes.put(Sample.PROP_INPUTSAMPLES, inputSamples);
*/
        sampleAttributes.put(Sample.PROP_INITIALAMOUNT, sb.getInitialAmount());
        sampleAttributes.put(AbstractSample.PROP_IONICSTRENGTH, sb.getIonicStrength());
        sampleAttributes.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        sampleAttributes.put(AbstractSample.PROP_ISHAZARD, sb.getIsHazard());

        sampleAttributes.put(AbstractSample.PROP_NAME, sb.getName());
        sampleAttributes.put(AbstractSample.PROP_PH, sb.getpH());
        sampleAttributes.put(Sample.PROP_ROWPOSITION, sb.getRowPosition());
        // sampleAttributes.put(Sample.PROP_SAMPLECOMPONENTS, "");
        sampleAttributes.put(Sample.PROP_SUBPOSITION, sb.getSubPosition());
        final Sample sample = version.create(Sample.class, sampleAttributes);

        for (final SampleCategory sampleCategory : sampleCategories) {
            sample.addSampleCategory(sampleCategory);
        }

        sample.setRefSample(refSample);

        if (null != sb.getAssignTo()) {
            final User assignTo = (User) version.get(sb.getAssignTo());
            sample.setAssignTo(assignTo);
        }

        return sample;
    }

    public static ModelObject createSample(final WritableVersion version, final String name,
        final RefSample refSample, final LabNotebook accessObject) throws AccessException,
        ConstraintException {

        ModelObject modelObject;

        final HashMap<String, Object> sampleAttributes = new HashMap<String, Object>();
        sampleAttributes.put(LabBookEntry.PROP_DETAILS, refSample.getDetails());
        sampleAttributes.put(AbstractSample.PROP_HAZARDPHRASES, refSample.getHazardPhrases());

        sampleAttributes.put(AbstractSample.PROP_IONICSTRENGTH, refSample.getIonicStrength());
        sampleAttributes.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        sampleAttributes.put(AbstractSample.PROP_ISHAZARD, refSample.getIsHazard());
        sampleAttributes.put(AbstractSample.PROP_NAME, name);
        sampleAttributes.put(AbstractSample.PROP_PH, refSample.getPh());
        if (accessObject != null) {
            sampleAttributes.put(LabBookEntry.PROP_ACCESS, accessObject);
        }
        sampleAttributes.put(Sample.PROP_REFSAMPLE, refSample);
        sampleAttributes.put(AbstractSample.PROP_SAMPLECATEGORIES, refSample.getSampleCategories());

        modelObject = version.create(Sample.class, sampleAttributes);
        final Sample sample = (Sample) modelObject;

        final Collection<SampleComponent> refSampleComponents = refSample.getSampleComponents();
        for (final SampleComponent component : refSampleComponents) {
            final Map<String, Object> scAttributes = new HashMap<String, Object>();
            scAttributes.put(SampleComponent.PROP_CONCENTRATION, component.getConcentration());
            scAttributes.put(SampleComponent.PROP_CONCENTRATIONUNIT, component.getConcentrationUnit());
            scAttributes.put(SampleComponent.PROP_CONCDISPLAYUNIT, component.getConcDisplayUnit());
            scAttributes.put(LabBookEntry.PROP_DETAILS, component.getDetails());
            scAttributes.put(SampleComponent.PROP_REFCOMPONENT, component.getRefComponent());
            scAttributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            new SampleComponent((WritableVersion) sample.get_Version(), scAttributes);
        }

        return modelObject;
    }
}
