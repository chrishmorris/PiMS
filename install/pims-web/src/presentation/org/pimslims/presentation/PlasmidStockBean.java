package org.pimslims.presentation;

import java.util.Set;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;

/**
 * pims-web PlasmidStockBean.java
 * 
 * @author susy
 * @date 2 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */

/**
 * PlasmidStockBean for details used for recording Plasmid stocks
 * 
 */
public class PlasmidStockBean extends AbstractStockBean {

    private Float concentration;

    private String strainPrepdFrom;

    public PlasmidStockBean(final Experiment experiment) {
        super(experiment);
        // 
    }

    public Float getConcentration() {
        return this.concentration;
    }

    private void setConcentration(final Float concentration) {
        this.concentration = concentration;
    }

    public String getStrainPrepdFrom() {
        return this.strainPrepdFrom;
    }

    private void setStrainPrepdFrom(final String strainPrepdFrom) {
        this.strainPrepdFrom = strainPrepdFrom;
    }

    public static PlasmidStockBean getPlasmidStockBean(final Experiment experiment) {
        final PlasmidStockBean psBean = new PlasmidStockBean(experiment);
        AbstractStockBean.experimentDetails(experiment, psBean);
        final Set<Parameter> params = experiment.getParameters();
        for (final Parameter param : params) {
            if (param.get_Name().startsWith("Concentration")) {
                if (!"".equals(param.getValue())) {
                    psBean.setConcentration(Float.valueOf(param.getValue()).floatValue());
                } else {
                    final String amountString = "0.0";
                    psBean.setConcentration(Float.valueOf(amountString).floatValue());
                }
            } else if (param.get_Name().startsWith("Strain")) {
                psBean.setStrainPrepdFrom(param.getValue());
            }
        }
        //The initials should be derived from the Scientist (Experiment creator)
        final String initials = "";
        //initials = StockUtility.makeInitials(experiment);
        psBean.setInitials(initials);

        //Use experiment details for Description if SPOT Construct details has no details value
        StockUtility.stockDescription(experiment, psBean);

        StockUtility.processOutputSample(experiment, psBean);
        StockUtility.setVectorDetails(experiment, psBean);
        return psBean;
    }
}
