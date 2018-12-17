/**
 * V4_0-web org.pimslims.lab ProtocolCsv.java
 * 
 * @author cm65
 * @date 13 Apr 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderType;

/**
 * ProtocolCsv
 * 
 */
public class ProtocolCsv extends PlateCsv {

    /**
     * Position
     * 
     */
    private static class Position implements Comparable {

        public final String plate;

        public final String well;

        Position(final String plate, final String well) {
            super();
            this.plate = plate;
            this.well = well;
        }

        /**
         * Comparable.compareTo
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(final Object other) {
            return this.well.compareTo(((Position) other).well);
        }

    }

    private final String groupName;

    private final boolean isPlateExperiment;

    /**
     * Constructor for ProtocolCsv for a plate experiment
     * 
     * @param protocol
     */
    public ProtocolCsv(final Protocol protocol, final String groupName, final HolderType type,
        final Map<String, String> holderNames) {
        super(protocol);
        this.isPlateExperiment = true;
        this.groupName = groupName;
        final List<Position> positions = new ArrayList();
        if (holderNames.isEmpty()) {
            final List<String> wells = HolderFactory.getPositions(type);
            for (final Iterator iterator = wells.iterator(); iterator.hasNext();) {
                final String well = (String) iterator.next();
                positions.add(new Position(this.groupName, well));
            }
        } else {
            for (final Iterator it = holderNames.entrySet().iterator(); it.hasNext();) {
                final Map.Entry<String, String> e = (Map.Entry) it.next();
                final String platePosition = e.getValue();
                final List<String> wells = HolderFactory.getPositions(type, platePosition);
                for (final Iterator iterator = wells.iterator(); iterator.hasNext();) {
                    final String well = (String) iterator.next();
                    positions.add(new Position(e.getKey(), well));
                }
            }
            Collections.sort(positions);
        }
        this.iterator = positions.iterator();
    }

    /**
     * Constructor for ProtocolCsv for an experiment group
     * 
     * @param protocol
     */
    public ProtocolCsv(final Protocol protocol, final String groupName, final int numExperiments) {
        super(protocol);
        this.groupName = groupName;
        this.isPlateExperiment = false;
        final List<Position> positions = new ArrayList();
        for (int i = 1; i <= numExperiments; i++) {
            positions.add(new Position(this.groupName, Integer.toString(i)));
        }
        this.iterator = positions.iterator();
    }

    /**
     * PlateCsv.isPlateExperiment
     * 
     * @see org.pimslims.lab.PlateCsv#isPlateExperiment()
     */
    @Override
    protected boolean isPlateExperiment() {
        return this.isPlateExperiment;
    }

    public String[] next() {
        final List<String> columnsValues = new LinkedList<String>();

        final Position p = (Position) this.iterator.next();
        columnsValues.add(p.plate);
        columnsValues.add(p.well);

        // Target and Construct
        columnsValues.add("");
        columnsValues.add("");
        // parameters value
        final Iterator<String> defaults = this.parameterDefaults.iterator();
        for (final String paraName : this.parameterNames) {
            columnsValues.add(defaults.next());
        }
        for (final String isName : this.inputSampleNames) {
            columnsValues.add("");
            if (this.inputSampleUnits.containsKey(isName)) {
                columnsValues.add("");
            }
        }
        final String[] arrayValues = new String[columnsValues.size()];
        columnsValues.toArray(arrayValues);
        return arrayValues;
    }

    /**
     * ProtocolCsv.getExperimentGroupCsv
     * 
     */
    public static AbstractCsvData getExperimentGroupCsv(final Protocol protocol, final String groupName,
        final int numExperiments) {
        return new ProtocolCsv(protocol, groupName, numExperiments);
    }

    /**
     * ProtocolCsv.getPlateCsv
     * 
     * @param protocol
     * @param string
     * @param type
     * @param string2
     * @param string3
     * @param string4
     * @param string5
     * @return
     */
    public static AbstractCsvData getPlateCsv(final Protocol protocol, final String groupName,
        final HolderType type, final Map<String, String> holderNames) {
        return new ProtocolCsv(protocol, groupName, type, holderNames);
    }

}
