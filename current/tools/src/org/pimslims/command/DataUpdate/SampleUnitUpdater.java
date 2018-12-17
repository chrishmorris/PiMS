/**
 * current-pims-web
 * 
 * @author Petr Troshin
 * @date July 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.HashSet;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * PersonFixer
 * 
 * Remove persons created as citations authors to de-clutter database
 * 
 */
public class SampleUnitUpdater implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        Boolean allCorrect = true;

        //fix big kg to µg
        Collection<Sample> samples = wv.findAll(Sample.class, Sample.PROP_AMOUNTUNIT, "kg");
        for (final Sample sample : samples) {

            if (sample.getCurrentAmount() == null) {
                System.out.println("Sample.getCurrentAmount returned NULL for sample " + sample.get_Hook());
                continue;
            }
            if (sample.getCurrentAmount() > 3) {
                allCorrect = false;
                sample.setAmountDisplayUnit("µg");
                sample.setCurrentAmount(sample.getCurrentAmount() / 1000000000f);
                System.out.println(sample + "'s amount is updated to " + sample.getCurrentAmount());
            }
        }

        //fix amount unit from µg to kg
        samples = wv.findAll(Sample.class, Sample.PROP_AMOUNTUNIT, "µg");
        for (final Sample sample : samples) {
            allCorrect = false;
            sample.setAmountUnit("kg");
            sample.setAmountDisplayUnit("µg");
            if (sample.getCurrentAmount() == null) {
                System.out.println("sample.currentAmount retured null for " + sample.get_Hook());
                continue;
            }
            sample.setCurrentAmount(sample.getCurrentAmount() / 1000000000f);
            System.out.println(sample + "'s AmountUnit is updated from µg to kg and amount is updated to "
                + sample.getCurrentAmount());

        }

        //fix  primer SampleComponents
        //find samplecategory
        final Collection<SampleCategory> scs = new HashSet<SampleCategory>();
        scs.addAll(wv.findAll(SampleCategory.class, SampleCategory.PROP_NAME, ConstructBeanWriter.FPRIMER));
        scs.addAll(wv.findAll(SampleCategory.class, SampleCategory.PROP_NAME, ConstructBeanWriter.RPRIMER));
        for (final SampleCategory sc : scs) {
            for (final AbstractSample as : sc.getAbstractSamples()) {
                for (final SampleComponent component : as.getSampleComponents()) //find primer related SampleComponent
                {
                    if (component.getConcentration() != null && component.getConcentration() > 0) {
                        if (component.getConcentrationUnit().equals("µM")) {
                            allCorrect = false;
                            component.setConcentrationUnit("M");
                            component.setConcDisplayUnit("µM");
                            component.setConcentration(component.getConcentration() / 1000000000f);
                            System.out.println(component
                                + "'s Concentration is updated from µM to M and amount is updated to "
                                + component.getConcentration());
                        } else if (component.getConcentrationUnit().equals("M")
                            && component.getConcDisplayUnit().equals("M")) {
                            allCorrect = false;
                            component.setConcentrationUnit("M");
                            component.setConcDisplayUnit("µM");
                            component.setConcentration(component.getConcentration() / 1000000000f);
                            System.out.println(component
                                + "'s Concentration is updated from µM to M and amount is updated to "
                                + component.getConcentration());
                        }
                    }
                }
            }
        }
        return allCorrect;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Correct (1)amount and amountUnit of Samples (2)concentration and concentration displayunit of primer SampleComponents ";
    }

}
