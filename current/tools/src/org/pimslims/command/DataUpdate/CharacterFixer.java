/**
 * current-pims-web org.pimslims.command.DataUpdate CharacterFixer.java
 * 
 * @author bl67
 * @date 20 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

/**
 * CharacterFixer
 * 
 */
public class CharacterFixer implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {
        int totoalInvalidChar = 0;
        //search in sample's amound unit
        final Collection<Sample> samples = wv.getAll(Sample.class, 0, Integer.MAX_VALUE);
        for (final Sample sample : samples) {
            String unit = sample.getAmountUnit();
            if (unit != null && unit.contains("Âµ")) {
                unit = unit.replace("Âµ", "µ");
                sample.setAmountUnit(unit);
                totoalInvalidChar++;
            }
        }
        //search in samplecomponent's concentrationunit
        final Collection<SampleComponent> scs = wv.getAll(SampleComponent.class, 0, Integer.MAX_VALUE);
        for (final SampleComponent sc : scs) {
            String unit = sc.getConcentrationUnit();
            if (unit != null && unit.contains("Âµ")) {
                unit = unit.replace("Âµ", "µ");
                sc.setConcentrationUnit(unit);
                totoalInvalidChar++;
            }
        }
        if (totoalInvalidChar != 0) {
            System.out.println(totoalInvalidChar + " invalid Âµ are replaced by µ");
        }
        return totoalInvalidChar == 0;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {

        return "fix strange character like 'Âµ' in sample or sample component";
    }

}
