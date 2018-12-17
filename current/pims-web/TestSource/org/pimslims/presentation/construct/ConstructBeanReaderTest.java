package org.pimslims.presentation.construct;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.experiment.ExperimentCreator;
import org.pimslims.presentation.experiment.ExperimentCreator.ExperimentCreationForm;

public class ConstructBeanReaderTest extends TestCase {

    private static final String UNIQUE = "cbr" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    private final List<Experiment> experimentList = new ArrayList<Experiment>();

    public ConstructBeanReaderTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public static List<Experiment> createWorkflow(final WritableVersion wv, final Experiment experiment)
        throws AccessException, ConstraintException {

        //System.out.println("createWorkflow [" + experiment.get_Name() + "]");
        Assert.assertNotNull(experiment);
        Assert.assertNotNull(experiment.getProject());

        final Protocol protocol = experiment.getProtocol();
        Assert.assertNotNull(protocol);

        final Collection<OutputSample> outputSamples = experiment.getOutputSamples();
        final List<Experiment> ret = new ArrayList();
        for (final OutputSample outputSample : outputSamples) {
            final RefOutputSample refOutputSample = outputSample.getRefOutputSample();
            final Sample sample = outputSample.getSample();

            final List<RefInputSample> refInputSamplesForNextExpt = new ArrayList<RefInputSample>();
            for (final RefInputSample refInputSample : refOutputSample.getSampleCategory()
                .getRefInputSamples()) {
                if (null != refInputSample.getProtocol().getIsForUse()
                    && !refInputSample.getProtocol().getIsForUse()) {
                    continue;
                }
                refInputSamplesForNextExpt.add(refInputSample);
            }

            for (final RefInputSample refInputSample : refInputSamplesForNextExpt) {

                final ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
                form.set_Input(refInputSample.getName() + ":" + sample.get_Hook());
                final Protocol myProtocol = refInputSample.getProtocol();
                form.setProtocolHook(myProtocol.get_Hook());
                form.setExperimentTypeHook(myProtocol.getExperimentType().get_Hook());
                form.setExperimentBlueprintHook(((ModelObject) experiment.getProject()).get_Hook());

                final ExperimentCreator presenter = new ExperimentCreator(form, wv);
                final Experiment myExperiment = presenter.save();
                ExperimentUtility.setExpBlueprintSamples(myExperiment,
                    (ResearchObjective) experiment.getProject());
                ret.add(myExperiment);
                /* this condition was never met, because of the line above
                if (!contains(myExperiment, ret)) {
                    if (experiment.getExperimentType() != myExperiment.getExperimentType()) {
                        doCreateWorkflow(wv, myExperiment);
                    }
                }       */
            }
        }
        return ret;
    }

/*
    private static boolean contains(final Experiment experiment, List<Experiment> list) {
        for (final Experiment exp : list) {
            if (exp.getProtocol() == experiment.getProtocol()) {
                return true;
            }
        }
        return false;
    } */

    public void testReadResearchObjective() throws ConstraintException, AccessException {

        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective ro = ConstructBeanWriterTest.createConstructBean(wv);
            final ConstructBean cb = ConstructBeanReader.readConstruct(ro);
            Assert.assertNotNull("Failed to read ConstructBean", cb);
            Assert.assertEquals(ro.get_Hook(), cb.getHook());
            ConstructBeanWriterTest.checkBeanProperties(cb);
            //cb.getImageFiles();
            //cb.getAttachmentFiles();
            cb.getNotes();

            final Experiment exp = ExperimentUtility.getPrimerDesignExperiment(ro);
            Assert.assertNotNull(exp);
            Assert.assertTrue(cb.getMayDelete());
            Assert.assertEquals("two primers", 3, exp.getOutputSamples().size());
            Assert.assertEquals(new Integer(2), cb.getTargetProtStart());
            Assert.assertEquals(new Integer(243), cb.getTargetProtEnd());

        } finally {
            wv.abort();
        }
    }

    public void testReadOnlyResearchObjective() throws ConstraintException, AccessException {

        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective eb = new ResearchObjective(wv, ConstructBeanReaderTest.UNIQUE, "test");
            new ResearchObjectiveElement(wv, "test", "test", eb);
            final ConstructBeanForList cb = ConstructBeanReader.readConstruct(eb);
            Assert.assertNotNull("Failed to read ConstructBean", cb);
            Assert.assertEquals(eb.get_Hook(), cb.getHook());
            Assert.assertTrue(cb.getMayDelete());
        } finally {
            wv.abort();
        }
    }

    public void testReadForwardPrimer() throws ConstraintException, AccessException {

        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective eb = ConstructBeanWriterTest.createConstructBean(wv);
            final Experiment exp = ExperimentUtility.getPrimerDesignExperiment(eb);
            Assert.assertNotNull(exp);
            Assert.assertEquals("two primers", 3, exp.getOutputSamples().size());

            final OutputSample fos =
                exp.findFirst(Experiment.PROP_OUTPUTSAMPLES, OutputSample.PROP_NAME, "Forward Primer");
            Assert.assertNotNull(fos);
            final PrimerBean f = PrimerBeanReader.readPrimer(fos.getSample());
            Assert.assertEquals(ConstructBeanWriterTest.FWD_PRIMER, f.getSequence());
            Assert.assertNotNull(f.getHook());

            //TODO Assert.assertEquals(ConstructBeanWriterTest.FORWARD_TAG, f.getTag());
            Assert.assertEquals(ConstructBeanWriterTest.FORWARD_EXTENSION, f.getExtension());
            //TODO Assert.assertNotNull(cb.getFwdOverlapGc());
            //TODO Assert.assertNotNull(cb.getFwdOverlapTm());
            Assert.assertEquals(ConstructBeanWriterTest.FWD_OVERLAP.length(), f.getOverlapLength());
            Assert.assertEquals(ConstructBeanWriterTest.FWD_OVERLAP, f.getOverlap());
            Assert.assertNotNull(f.getPrimerHook());
            final ModelObject primer = wv.get(f.getPrimerHook());
            Assert.assertTrue(primer instanceof Primer);

            final OutputSample ros =
                exp.findFirst(Experiment.PROP_OUTPUTSAMPLES, OutputSample.PROP_NAME, "Reverse Primer");
            final PrimerBean r = PrimerBeanReader.readPrimer(ros.getSample());
            Assert.assertEquals(ConstructBeanWriterTest.REV_PRIMER, r.getSequence());
            Assert.assertNotNull(r.getHook());
            Assert.assertEquals(ConstructBeanWriterTest.REVERSE_TAG, r.getTag());
            Assert.assertEquals(ConstructBeanWriterTest.REV_OVERLAP, r.getOverlap());
            //TODO Assert.assertEquals(50.0f, r.getRevOverlapGc());
            //TODO Assert.assertNotNull(cb.getRevOverlapTm());
            Assert.assertEquals(ConstructBeanWriterTest.REV_OVERLAP.length(), r.getOverlapLength());
        } finally {
            wv.abort();
        }
    }

    public void testReadPrimerDesignExperiment() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective eb = ConstructBeanWriterTest.createConstructBean(wv);
            final Experiment exp = ExperimentUtility.getPrimerDesignExperiment(eb);
            Assert.assertNotNull(exp);
            Assert.assertEquals(eb, exp.getProject());
            final ConstructBean cb = ConstructBeanReader.readConstruct(exp);
            Assert.assertNotNull("Failed to read ConstructBean", cb);
            Assert.assertEquals(eb.get_Hook(), cb.getHook());
            ConstructBeanWriterTest.checkBeanProperties(cb);
            /* was Assert.assertNotNull(cb.getRevPrimerHook());
            ModelObject primer = wv.get(cb.getRevPrimerHook());
            Assert.assertTrue(primer instanceof Primer); */
            final List<PrimerBean> primers = cb.getPrimers();
            Assert.assertEquals(2, primers.size());
            final PrimerBean bean = primers.iterator().next();
            Assert.assertNotNull(bean.getHook());
            final ModelObject primer = wv.get(bean.getHook());
            Assert.assertTrue(primer.getClass().getName(), primer instanceof Sample);
            Assert.assertEquals("TTGATCAAGCCT", bean.getOverlap());
            Assert.assertEquals(bean.getOverlap(), bean.getOverlap());
        } finally {
            wv.abort();
        }
    }

    public void testReadMinimalExperiment() throws ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, ConstructBeanReaderTest.UNIQUE);
            final Experiment exp =
                new Experiment(version, ConstructBeanReaderTest.UNIQUE, ConstructBeanReaderTest.NOW,
                    ConstructBeanReaderTest.NOW, type);
            final ModelObjectShortBean cb = ConstructBeanReader.readConstruct(exp);
            Assert.assertNotNull("Failed to read ConstructBean", cb);
        } finally {
            version.abort();
        }
    }

    public void testBadSequence() {
        Assert.assertEquals(0f, ConstructBeanReader.getGCContent("not DNA"));
    }

    public void xtestConstructProgress() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ResearchObjective eb = ConstructBeanWriterTest.createConstructBean(wv);
            final Experiment exp = ExperimentUtility.getPrimerDesignExperiment(eb);
            this.experimentList.addAll(ConstructBeanReaderTest.createWorkflow(wv, exp));

            /*System.out.println("Construct progress");
            final Collection<List> progressList = ConstructBeanReader.getConstructProgress(eb);
            for (final List<ExperimentBean> progressExperiments : progressList) {
                System.out.println("Construct progress line");
                for (final ExperimentBean experiment : progressExperiments) {
                    System.out.println("Experiment [" + experiment.getName() + "]");
                }
            } */

        } finally {
            wv.abort();
        }
    }
}
