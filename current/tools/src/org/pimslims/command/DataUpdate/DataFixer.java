/**
 * this class is collection of function which will try to fix the data in DB org.pimslims.upgrader
 * DataFixer.java
 * 
 * @date 21-Dec-2006 10:32:13
 * 
 * @author Bill
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           version
 * 
 * 
 * @see
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.sample.Sample;

public abstract class DataFixer {
    static long maxNumber = 0;

    static String targetToIgnore = "org.pimslims.model.target.Target:60149";

    /**
     * @param name
     * @return private static boolean isNumber(final String name) { try { final long currentNumber =
     *         Long.parseLong(name); if (DataFixer.maxNumber < currentNumber) { DataFixer.maxNumber =
     *         currentNumber; } } catch (final NumberFormatException e) { return false;
     * 
     *         } return true; }
     */
/*
    private static void resetSequence(final WritableVersion wv) throws SQLException {
        final DatabaseUpdater dbUpdater = new DatabaseUpdater(wv.getSession().connection());
        dbUpdater.setSequenceStart("generic_target", DataFixer.maxNumber + 1);

    } */

    public static void DeleteDuplicatedExpParameters(final WritableVersion wv) throws AccessException,
        ConstraintException {

        final Map<String, Object> critias = new HashMap<String, Object>();
        critias.put(Parameter.PROP_VALUE, null);
        critias.put(Parameter.PROP_PARAMTYPE, null);
        final Collection<Parameter> expParameters = wv.findAll(Parameter.class, critias);
        for (final Parameter parameter : expParameters) {
            parameter.delete();

        }

    }

    public static void replaceInvalidChar(final WritableVersion wv) throws ConstraintException {
        final Collection<Sample> samples = wv.getAll(Sample.class, 0, Integer.MAX_VALUE);
        for (final Sample sample : samples) {
            String unit = sample.getAmountUnit();
            if (unit != null) {
                unit = unit.replace("Âµg", "µg");
                sample.setAmountUnit(unit);
            }
        }

    }

}
