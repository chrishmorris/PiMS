package org.pimslims.presentation;

import java.util.List;
import java.util.Set;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * pims-web CellStockBean.java
 * 
 * @author susy
 * @date 2 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */

/**
 * CellStockBean for details used for recording Cell stocks
 * 
 */
public class CellStockBean extends AbstractStockBean {

    private String strain;

    private String hostAbRes;

    private String plasmid;

    private String pimsPlasmidHook;

    public CellStockBean(final Experiment experiment) {
        super(experiment);
    }

    public String getStrain() {
        return this.strain;
    }

    private void setStrain(final String strain) {
        this.strain = strain;
    }

    public String getHostAbRes() {
        return this.hostAbRes;
    }

    private void setHostAbRes(final String hostAbRes) {
        this.hostAbRes = hostAbRes;
    }

    public String getPlasmid() {
        return this.plasmid;
    }

    private void setPlasmid(final String plasmid) {
        this.plasmid = plasmid;
    }

    public String getPimsPlasmidHook() {
        return this.pimsPlasmidHook;
    }

    private void setPimsPlasmidHook(final String pimsPlasmidHook) {
        this.pimsPlasmidHook = pimsPlasmidHook;
    }

    public static CellStockBean getCellStockBean(final Experiment experiment) {
        final CellStockBean csBean = new CellStockBean(experiment);
        AbstractStockBean.experimentDetails(experiment, csBean);
        StockUtility.setVectorDetails(experiment, csBean);

        final List<InputSample> iss = experiment.getInputSamples();
        for (final InputSample is : iss) {
            if (null != is.getSample()) {
                final Sample inSample = is.getSample();
                final Set<SampleCategory> inSamplecats = inSample.getSampleCategories();
                for (final SampleCategory inscat : inSamplecats) {
                    if (inscat.get_Name().equalsIgnoreCase("Plasmid")) {
                        csBean.setPimsPlasmidHook(inSample.get_Hook());
                        csBean.setPlasmid(inSample.getName());
                    }
                }
            }
        }

        final Set<Parameter> params = experiment.getParameters();
        for (final Parameter param : params) {
            if (param.get_Name().startsWith("Strain")) {
                csBean.setStrain(param.getValue());
            } else if (param.get_Name().startsWith("Antibiotic")) {
                //TODO Host antibiotic resistance from Cell InputSample = Host
                String abRes = "";
                final String abbrAbRes = StockUtility.antibioticAbbr.get(param.getValue());

                if ((!"".equals(csBean.getAntibioticRes())) && (null != csBean.getAntibioticRes())) {
                    abRes = csBean.getAntibioticRes();
                    if (!"".equals(param.getValue())) {
                        if (null != abbrAbRes) {
                            abRes = abRes + ", " + abbrAbRes;
                        } else {
                            abRes = abRes + ", " + param.getValue();
                        }
                    }
                } else {
                    if (null != abbrAbRes) {
                        abRes = abbrAbRes;
                    } else {
                        abRes = param.getValue();
                    }
                }
                csBean.setHostAbRes(abRes);

            }
        }

        //The initials should be derived from the Scientist (Experiment creator)
        final String initials = "";
        //initials = StockUtility.makeInitials(experiment);
        csBean.setInitials(initials);

        //Use experiment details for Description if SPOT Construct details has no details value
        StockUtility.stockDescription(experiment, csBean);

        StockUtility.processOutputSample(experiment, csBean);
        return csBean;
    }
}
