package org.pimslims.ispyb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

import uk.ac.diamond.ispyb.client.IspybServiceStub.CollectPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Container;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DeliveryAgent;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Dewar;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ExperimentType_type3;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ProcessingLevel_type1;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Protein;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ScreenPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Temperature_type3;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Type_type5;

public class PimsBeanFactory extends IspybBeanFactory {

    private final String beamtimeAllocation;

    private final String crystalUUIDPrefix;

    private final String proteinUUIDprefix;

    private final String diffractionUUIDprefix;

    public PimsBeanFactory(String beamtimeAllocation, String uuidPrefix) {
        super();
        this.beamtimeAllocation = beamtimeAllocation;
        this.crystalUUIDPrefix = uuidPrefix + Sample.class.getName() + ":";
        this.proteinUUIDprefix = uuidPrefix + ResearchObjective.class.getName() + ":";
        this.diffractionUUIDprefix = uuidPrefix + Experiment.class.getName() + ":";
    }

    public CrystalShipping makeShipping(ExperimentGroup group) {
        return super.makeShipping(group.getName(), makeDewars(group), this.beamtimeAllocation,
            makeDeliveryAgent(group));
    }

    DeliveryAgent makeDeliveryAgent(ExperimentGroup group) {
        DeliveryAgent ret = new DeliveryAgent();

        ret.setAgentName("Delivery Agent");
        ret.setAgentCode("");

        ret.setShippingDate(group.getStartDate().getTime());
        ret.setDeliveryDate(group.getStartDate().getTime());
        return ret;
    }

    Dewar[] makeDewars(ExperimentGroup group) {

        // find all the dewars
        Map<String, Map<String, Map<String, Sample>>> dewars = new HashMap();
        Collection<Sample> samples = getSamples(group);
        for (Iterator iterator = samples.iterator(); iterator.hasNext();) {
            Sample sample = (Sample) iterator.next();
            this.addSample(sample, dewars);
        }

        // now make beans for them
        Dewar[] ret = new Dewar[dewars.size()];
        int i = 0;
        for (Iterator iterator = dewars.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry dewarEntry = (Map.Entry) iterator.next();
            Dewar dewar = new Dewar();
            dewar.setBarCode((String) dewarEntry.getKey());
            ret[i++] = dewar;
            Map<String, Map<String, Sample>> pucks = (Map<String, Map<String, Sample>>) dewarEntry.getValue();
            for (Iterator iterator2 = pucks.entrySet().iterator(); iterator2.hasNext();) {
                Map.Entry puckEntry = (Map.Entry) iterator2.next();
                Container puck = new Container();
                puck.setType(Type_type5.Puck);
                puck.setCode((String) puckEntry.getKey());
                dewar.addContainer(puck);
                Map<String, Sample> loops = (Map<String, Sample>) puckEntry.getValue();
                for (Iterator iterator3 = loops.entrySet().iterator(); iterator3.hasNext();) {
                    Map.Entry<String, Sample> loopEntry = (Map.Entry<String, Sample>) iterator3.next();
                    Sample sample = loopEntry.getValue();
                    uk.ac.diamond.ispyb.client.IspybServiceStub.Holder loop =
                        super.makeLoop(makeCrystal(sample), getLoopPosition(sample),
                            loopEntry.getKey());
                    puck.addHolder(loop);
                }
            }
        }
        return ret;
    }

    private String getLoopPosition(Sample sample) {
        Holder loop = sample.getHolder();
        return loop.getRowPosition() + "_" + loop.getColPosition();
    }

    /**
     * @param sample
     * @param dewars Map dewar bar code => puck bar code => loop bar code => sample
     */
    void addSample(Sample sample, Map<String, Map<String, Map<String, Sample>>> dewars) {
        org.pimslims.model.holder.Container dewar = null;
        String dewarName = "";
        String puckName = "";
        org.pimslims.model.holder.Container loop = sample.getContainer();
        org.pimslims.model.holder.Container puck = loop.getContainer();
        if (null != puck) {
            puckName = puck.getName();
            dewar = puck.getContainer();
            if (null != dewar) {
                dewarName = dewar.getName();
            }
        }
        Map<String, Map<String, Sample>> pucks = dewars.get(dewarName);
        if (null == pucks) {
            pucks = new HashMap<String, Map<String, Sample>>();
            dewars.put(dewarName, pucks);
        }
        Map<String, Sample> loops = pucks.get(puckName);
        if (null == loops) {
            loops = new HashMap<String, Sample>();
            pucks.put(puckName, loops);
        }
        String loopName = loop.getName();
        loops.put(loopName, sample);
        System.out.println(dewarName + ":" + puckName + ":" + loopName);
    }

    /* (non-Javadoc)
     * @see org.pimslims.ispyb.IspybBeanFactory#makeCrystal(uk.ac.diamond.client.IspybServiceStub.Protein, java.lang.String, java.lang.String)
     */
    protected Crystal makeCrystal(Sample sample) {
        Project ro = sample.getInputSamples().iterator().next().getExperiment().getProject();
        return super
            .makeCrystal(makeProtein(ro), sample.getName(), this.crystalUUIDPrefix + sample.getDbId());
    }

    /* (non-Javadoc)
     * @see org.pimslims.ispyb.IspybBeanFactory#makeCrystalDetails(java.util.Collection, java.lang.String)
     */
    protected CrystalDetails makeCrystalDetails(ExperimentGroup group) {
        Collection<Sample> samples = getSamples(group);
        Collection<Crystal> crystals = new ArrayList(samples.size());
        for (Iterator iterator = samples.iterator(); iterator.hasNext();) {
            Sample sample = (Sample) iterator.next();
            crystals.add(this.makeCrystal(sample));
        }
        return super.makeCrystalDetails(crystals, this.beamtimeAllocation);
    }

    private Collection<Sample> getSamples(ExperimentGroup group) {
        Map<String, Map<String, Map<String, Sample>>> dewars = new HashMap();
        Set<Experiment> experiments = group.getExperiments();
        Collection<Sample> ret = new ArrayList(experiments.size());
        for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            Experiment experiment = (Experiment) iterator.next();
            Collection<InputSample> oss = experiment.getInputSamples();
            for (Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                InputSample os = (InputSample) iterator2.next();
                Sample sample = os.getSample();
                if (null != sample) {
                    ret.add(sample);
                }
            }
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see org.pimslims.ispyb.IspybBeanFactory#makeProtein(java.lang.String, java.lang.String)
     */
    protected Protein makeProtein(Project ro) {
        if (null == ro) {
            return null;
        }
        return super.makeProtein(ro.getName(), this.proteinUUIDprefix + ((LabBookEntry) ro).getDbId());
    }

    DiffractionPlan makeDiffractionPlan(Experiment experiment) {
        DiffractionPlan ret = new DiffractionPlan();
        ret.setProjectUUID(this.beamtimeAllocation);
        Sample sample = ((InputSample) experiment.findFirst(Experiment.PROP_INPUTSAMPLES)).getSample();

        CollectPlan collectionPlan = new CollectPlan();
        //TODO need symbolic names in the beans, then to add these as experiment parameters
        collectionPlan.setExperimentType(ExperimentType_type3.value1);
        collectionPlan.setTemperature(Temperature_type3.value1);
        collectionPlan.setProcessingLevel(ProcessingLevel_type1.value1);
        collectionPlan.setAnomalousScattererElement(getParameterValue(experiment, "Anomalous Scatterer"));

        ScreenPlan screen = null;
        return super.makeDiffractionPlan(this.beamtimeAllocation,
            this.diffractionUUIDprefix + experiment.getDbId(), this.crystalUUIDPrefix + sample.getDbId(),
            collectionPlan, getDefaultSweep(2.0d), screen);
    }

    private String getParameterValue(Experiment experiment, String name) {
		// TODO Speed this up
        Parameter parm = experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, name);
        return parm.getValue();
    }

}
