/**
 * pims-web org.pimslims.presentation StockUtility.java
 * 
 * @author Susy Griffiths
 * @date 2 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 Susy Griffiths The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.vector.FeatureBean;
import org.pimslims.presentation.vector.VectorBean;

/**
 * StockUtility methods for Plasmid and cell stocks
 * 
 */
public class StockUtility {

    /**
     * StockUtility.getResistances
     * 
     * @param psBean presentation/PlasmidStockBean
     * @param refS sample.RefSample
     */
    public static String getResistances(final RefSample refS) {
        final VectorBean vb = VectorBean.getVectorBean(refS);
        List<FeatureBean> resBeans;
        String abRes = "";
        if (null == vb) {
            resBeans = Collections.EMPTY_LIST;
        } else {
            resBeans = vb.getResistances();
            //         String abRes = "";
            for (final FeatureBean fb : resBeans) {
                final String abr = StockUtility.antibioticAbbr.get(fb.getName());
                if (null != abr) {
                    abRes = abRes + abr + " ";
                } else {
                    abRes = abRes + fb.getName() + " ";
                }
            }
            abRes = abRes.trim();
            abRes = abRes.replaceAll(" ", ",");
            //psBean.setAntibioticRes(abRes);
        }
        return abRes;
    }

    //Mapping for abbreviated Antibiotic resistances
    static Map<String, String> antibioticAbbr = new HashMap<String, String>();
    static {
        StockUtility.antibioticAbbr.put("Kannamycin", "Kan"); //misspelled
        StockUtility.antibioticAbbr.put("Kanamycin", "Kan");
        StockUtility.antibioticAbbr.put("Ampicillin", "Amp");
        StockUtility.antibioticAbbr.put("Chloramphenicol", "Chl");
        StockUtility.antibioticAbbr.put("Gentamycin", "Gent");
        StockUtility.antibioticAbbr.put("Zeocin", "Zeo");
        StockUtility.antibioticAbbr.put("Hygromycin", "Hyg");
        StockUtility.antibioticAbbr.put("Hygromycin B", "Hyg");
        StockUtility.antibioticAbbr.put("Neomycin", "Neo");
        StockUtility.antibioticAbbr.put("Carbenicillin", "CB");
        StockUtility.antibioticAbbr.put("D-cycloserine", "DCS");
        StockUtility.antibioticAbbr.put("Kasugamycin", "Ksg");
        StockUtility.antibioticAbbr.put("Nalidixic acid", "Nal");
        StockUtility.antibioticAbbr.put("Rifampicin", "RIF");
        StockUtility.antibioticAbbr.put("Spectinomycin", "Spc");
        StockUtility.antibioticAbbr.put("Streptomicin", "Str");
        StockUtility.antibioticAbbr.put("Tetracycline", "Tet");
        StockUtility.antibioticAbbr.put("Amphotericin B", "AmB");
        StockUtility.antibioticAbbr.put("Erythromycin", "Ery");
        StockUtility.antibioticAbbr.put("G418", "G418");
        StockUtility.antibioticAbbr.put("Vancomycin", "Van");

    }

    /*
     * StockUtility.makeInitials
     */
    private static String makeInitials(final Experiment experiment) {
        String initials = "";
        if (null != experiment.getCreator()) {
            if (null != experiment.getCreator().getPerson()) {
                final Person thePerson = experiment.getCreator().getPerson();
                if (null != thePerson.getGivenName() && !"".equals(thePerson.getGivenName())) {
                    initials = thePerson.getGivenName().substring(0, 1);
                }
                if (null != thePerson.getMiddleInitials() && !"".equals(thePerson.getMiddleInitials())) {
                    final List<String> midInitials = thePerson.getMiddleInitials();
                    for (final String midinit : midInitials) {
                        initials = initials + midinit;

                    }
                }
                initials = initials.concat(thePerson.getFamilyName().substring(0, 1));
                initials = initials.toUpperCase();
            }
        } else {
            initials = experiment.get_Owner();
        }

        return initials;

    }

    /**
     * AbstractStockBean.setVector
     * 
     * @param experiment
     * @param bean
     */
    protected static void setVectorDetails(final Experiment experiment, final AbstractStockBean bean) {
        final List<InputSample> iss = experiment.getInputSamples();
        for (final InputSample is : iss) {
            if (null != is.getSample()) {
                final Sample inSample = is.getSample();
                final Set<SampleCategory> inSamplecats = inSample.getSampleCategories();
                for (final SampleCategory inscat : inSamplecats) {
                    if (inscat.get_Name().equalsIgnoreCase("Vector")) {
                        final RefSample refS = inSample.getRefSample();
                        String vectorName = refS.get_Name();
                        vectorName = vectorName.replaceAll("Vector", "").trim();
                        bean.setVector(vectorName);
                        bean.setPimsVectorHook(inSample.getRefSample().get_Hook());
                        //051109 Make a vector bean from the RefSample to get the Antibiotic resistances
                        final String vectorRes = StockUtility.getResistances(refS);
                        bean.setAntibioticRes(vectorRes);
                        //TODO may need to be modified for Cell stock to include Host cell resistances
                    }
                }
            }
        }
    }

    /**
     * PlasmidStockBean.stockDescription
     * 
     * @param experiment
     * @param stockBean
     */
    @Deprecated
    static void stockDescription(final Experiment experiment, final AbstractStockBean stockBean) {
        if (null != experiment.getDetails()) {
            stockBean.setDescription(experiment.getDetails());
        }
        if (null != experiment.getProject()) {
            //final ResearchObjective ro = experiment.getProject();
            if (null != experiment.getResearchObjective().getFunctionDescription()) {
                final String roDesc = experiment.getResearchObjective().getFunctionDescription();
                if (!"".equals(roDesc)) {
                    stockBean.setDescription(roDesc);
                }
            }
        }
    }

    /**
     * PlasmidStockBean.processOutputSample
     * 
     * @param experiment
     * @param stockBean
     */
    static void processOutputSample(final Experiment experiment, final AbstractStockBean stockBean) {
        final Set<OutputSample> oss = experiment.getOutputSamples();
        if (oss.size() > 0) {
            for (final OutputSample os : oss) {
                if (null != os.getSample()) {
                    final Sample sample = os.getSample();
                    final Set<SampleCategory> samplecats = sample.getSampleCategories();
                    for (final SampleCategory scat : samplecats) {
                        if (scat.get_Name().equalsIgnoreCase("Plasmid")
                            || scat.get_Name().equalsIgnoreCase("Cell")) {
                            stockBean.setPimsSampleHook(sample.get_Hook());
                            if (null != sample.getCurrentAmount()) {
                                stockBean.setVolume(sample.getCurrentAmount() * 1000000);
                            }
                        }
                    }
                    stockBean.setStockName(sample.getName());
                    //231009 get Holder of Sample for 'Rack'
                    if (null != sample.getHolder()) {
                        final Holder holder = sample.getHolder();
                        stockBean.setPimsHolderHook(holder.get_Hook());
                        stockBean.setRack(holder.get_Name());
                    }
                    final String posInHolder = HolderFactory.getPositionInHolder(sample);
                    if (null != posInHolder) {
                        stockBean.setRackPosition(posInHolder);
                    }
                }
            }
        }
    }

}
