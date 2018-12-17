/**
 * pims-web org.pimslims.presentation PrimerBeanReader.java
 * 
 * @author Marc Savitsky
 * @date 4 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.primer.PrimerProxy;
import org.pimslims.lab.sample.SampleUtility;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

public class PrimerBeanReader {

    public static PrimerBean readPrimer(final ReadableVersion version, final String name) {

        final Sample sample = PrimerBeanReader.getSample(version, name);
        return PrimerBeanReader.readPrimer(sample);
    }

    public static PrimerBean readPrimer(final Sample sample) {

        if (sample == null) {
            return null;
        }
        final PrimerBean bean = new PrimerBean();

        // get sequence properties
        Primer primer = null;
        final Set<SampleComponent> sampleComps = sample.getSampleComponents();
        for (final Iterator iterator = sampleComps.iterator(); iterator.hasNext();) {
            final SampleComponent scomp = (SampleComponent) iterator.next();
            final Substance molcomponent = scomp.getRefComponent();
            if (molcomponent instanceof Primer) {
                primer = (Primer) molcomponent;
                bean.setPrimerHook(primer.get_Hook());
                if (null != scomp.getConcentration()) {
                    bean.setConcentration(scomp.getConcentration().toString());
                    //System.out.println("Sample Concentration [" + scomp.get_Name() + ":" + scomp.getConcentration()
                    //    + ":" + scomp.getConcDisplayUnit() + ":" + scomp.getConcentrationUnit() + "]");
                    final Measurement m =
                        Measurement.getMeasurement(SampleComponent.PROP_CONCENTRATION, scomp,
                            SampleComponent.PROP_CONCENTRATIONUNIT, SampleComponent.PROP_CONCDISPLAYUNIT);
                    bean.setConcentration(new Float(m.getDisplayValue()).toString());
                }
            }

        }

        //System.out.println("PrimerBean readPrimer [" + primer + "]");
        if (null == primer) {
            return null;
        }

        //set sequence properties
        final PrimerProxy primerProxcy = new PrimerProxy(primer);
        bean.setDirection(primerProxcy.getDirection());
        final DnaSequence sequence = new DnaSequence(primer.getSeqString());
        bean.setSequence(sequence.getSequence());
        bean.setTmfullfloat(primerProxcy.getTmFullFloat());
        bean.setMolecularMass(Float.toString(sequence.getMass()));
        //was bean.setGcfull(primerProxcy.getGcfull());

        bean.setGcgene(primer.getGcGene());
        final Integer overlapLen = primer.getLengthOnGene();
        if (overlapLen != null) {
            bean.setLengthOnGeneString(overlapLen.toString());
        }

        bean.setRestrictionsite(primer.getRestrictionSite());
        if (primer.getMeltingTemperatureGene() != null) {
            bean.setTmGene(primer.getMeltingTemperatureGene().toString());
        }
        if (primer.getMeltingTemperatureSeller() != null) {
            bean.setTmseller(primer.getMeltingTemperatureSeller().toString());
        }
        bean.setOD(primer.getOpticalDensity());
        bean.setParticularity(primer.getParticularity());

        // set sample properties
        bean.setSample(sample); //TODO for performance, but can lead to trouble
        bean.setName(sample.getName());
        bean.setPositionBarcode(sample.getBatchNum());
        bean.setPosition(PrimerBeanReader.getPosition(sample));

        if (null != SampleUtility.getOrganisation(sample)) {
            bean.setSeller(SampleUtility.getOrganisation(sample).get_Hook());
        }

        if (null != sample.getCurrentAmount()) {
            //bean.setAmount(sample.getCurrentAmount().toString());
            //System.out.println("Sample Amount [" + sample.get_Name() + ":" + sample.getCurrentAmount() + ":"
            //    + sample.getAmountDisplayUnit() + ":" + sample.getAmountUnit() + "]");
            final Measurement m =
                Measurement.getMeasurement(Sample.PROP_CURRENTAMOUNT, sample, Sample.PROP_AMOUNTUNIT,
                    Sample.PROP_AMOUNTDISPLAYUNIT);
            //System.out.println("Sample Amount [" + m.getDisplayValue() + "]");
            bean.setAmount(new Float(m.getDisplayValue()).toString());
        }

        // get experiment properties
        if (null != sample.getOutputSample()) {
            final Experiment experiment = sample.getOutputSample().getExperiment();
            final String osname = sample.getOutputSample().getName(); // "Forward Primer" or "Reverse Primer"
            final String direction = osname.substring(0, "Forward".length());
            final Parameter tag =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, direction + " Tag");
            if (null != tag) {
                bean.setTag(tag.getValue());
            }
        }

        final Holder holder = sample.getHolder();
        if (null != holder) {
            bean.setBox(holder.get_Hook());
            bean.setBoxName(holder.getName());
            if (null != holder.getSubPosition()) {
                bean.setBoxBarcode(Integer.toString(holder.getSubPosition()));
            }
            final Location location = ContainerUtility.getCurrentLocation(holder);
            if (null != location) {
                bean.setLocation(location.get_Hook());
                bean.setLocationName(location.getName());
            }
        }

        return bean;
    }

    private static Sample getSample(final ReadableVersion version, final String name) {

        final Map<String, Object> sampleProp = new HashMap<String, Object>();
        sampleProp.put(AbstractSample.PROP_NAME, name);
        final Sample sample = version.findFirst(Sample.class, sampleProp);
        return sample;
    }

    public static Experiment getCloningDesignExperiment(final ReadableVersion version, final String name) {
        final Sample sample = PrimerBeanReader.getSample(version, name);
        for (final InputSample inSam : sample.getInputSamples()) {
            if (inSam.getExperiment().getExperimentType().getName()
                .equals(FormFieldsNames.leedscloningDesign)) {
                return inSam.getExperiment();
            }
        }
        return null;
    }

    private static final Map<String, String> OTHER_LETTER = new HashMap();
    static {
        PrimerBeanReader.OTHER_LETTER.put("r", "f");
        PrimerBeanReader.OTHER_LETTER.put("R", "F");
        PrimerBeanReader.OTHER_LETTER.put("f", "r");
        PrimerBeanReader.OTHER_LETTER.put("F", "R");
    }

    /**
     * @param name e.g. sso1468.cR, MPSIL0033F1
     * @return the name of the other primer in the pair, or null
     */
    public static String getOtherPrimerName(final String name) {
        final int lastr = name.toUpperCase().lastIndexOf("R");
        final int lastf = name.toUpperCase().lastIndexOf("F");
        final int last = Math.max(lastr, lastf);
        if (-1 == last) {
            return null;
        }
        final String letter = name.substring(last, last + 1);
        final String otherLetter = PrimerBeanReader.OTHER_LETTER.get(letter);
        return name.substring(0, last) + otherLetter + name.substring(last + 1);
    }

    public static String getPosition(final Sample sample) {
        if (sample == null || sample.getColPosition() == null || sample.getRowPosition() == null) {
            return null;
        }

        final String colPos = HolderFactory.getColumn(sample.getColPosition() - 1);
        final String rowPos = HolderFactory.getRow(sample.getRowPosition() - 1);
        return rowPos + colPos;
    }
}
