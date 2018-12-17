/**
 * current-pims-web org.pimslims.utils.sequenator InstrumentCSVGenerator.java
 * 
 * @author pvt43
 * @date 6 Apr 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.utils.experiment.Utils;

/**
 * InstrumentCSVGenerator
 * 
 */
public class InstrumentCSVGenerator {

    public static enum RobotType {
        w96Source, w16Source, standard96WellPlate
    }

    ExperimentGroup ssExpGroup;

    ExperimentGroup seqOrderPlate;

    RobotType robotType;

    ArrayList<SeqSetupExperiment> sses;

    TreeMap<String, String> primerWell;

    TreeMap<String, String> templateWell;

    TreeMap<String, Integer> primerVolumes;

    TreeMap<String, Integer> templateVolumes;

    Iterator<String> primerSource;

    Iterator<String> templateSource;

    Experiment control;

    final ArrayList<Experiment> seqOrderExps;

    final ArrayList<Experiment> seqSetupExps;

    /**
     * Constructor for InstrumentCSVGenerator
     */
    public InstrumentCSVGenerator(final ExperimentGroup ssExpGroup, final RobotType robotType) {
        this.ssExpGroup = ssExpGroup;
        this.seqOrderPlate = Utils.getSequencingOrderPlate(ssExpGroup);
        this.robotType = robotType;
        assert !ssExpGroup.getExperiments().isEmpty();
        assert ssExpGroup.getExperiments().iterator().next().getProtocol().getName().equals(
            SequencingOrder.sSetupProtocolName);
        this.sses = new ArrayList<SeqSetupExperiment>();
        this.seqOrderExps = new ArrayList<Experiment>();
        this.seqSetupExps = new ArrayList<Experiment>();
        for (final Experiment exp : ssExpGroup.getExperiments()) {
            final Experiment sOrderExp = Utils.getSequencingOrderExperiment(exp);
            //Skip the controls they will be setup in a special positions
            if (Utils.isControl(sOrderExp)) {
                //Extract reaction control to be prepared for a special position
                if (Utils.isReactionControl(sOrderExp)) {
                    this.control = exp;
                }
                continue;
            }
            this.seqOrderExps.add(sOrderExp);
            this.seqSetupExps.add(exp);
            this.sses.add(new SeqSetupExperiment(exp));
        }

        // Arrange experiments by their names 
        // this will also arrange them by their position from A1-H12

        Collections.sort(this.sses, new Comparator<SeqSetupExperiment>() {

            public int compare(final SeqSetupExperiment o1, final SeqSetupExperiment o2) {
                final String e1runNumber =
                    o1.exp.getName().substring(SeqSetupExperiment.EXP_NAME_PREFIX.length());
                final String e2runNumber =
                    o2.exp.getName().substring(SeqSetupExperiment.EXP_NAME_PREFIX.length());
                return Integer.decode(e1runNumber).compareTo(Integer.decode(e2runNumber));
            }
        });
        // Build Template & Primer names -> required volumes maps
        this.buildNameRequiredVolumeMap();
        // Make sure this run contain the number of primers lesser than the number of available primers sources 
        // on the robot  
        switch (robotType) {
            case standard96WellPlate:
                this.primerSource = ExternalFieldsHelper.get96WellPlateSource();
                break;
            case w16Source:
                if (this.primerVolumes.keySet().size() > ExternalFieldsHelper.primerSource1D.length) {
                    throw new IllegalArgumentException(
                        "The order contains too many primers. There is not enough sources to aliquot these primers in. This plate cannot be run on the robot specified!");
                }
                this.primerSource = ExternalFieldsHelper.get1DPrimerSource();
                break;
            case w96Source:
                this.primerSource = ExternalFieldsHelper.get2DPrimerSource();
                break;
            default:
                throw new AssertionError("robotType is not supplied!");
        }
        // Template source is equal for both robots 
        this.templateSource = ExternalFieldsHelper.get96WellPlateSource();

        // Build template && primer names -> Well number maps
        this.buildNameWellMap();
    }

    void buildNameRequiredVolumeMap() {
        this.primerVolumes = new TreeMap<String, Integer>();
        this.templateVolumes = new TreeMap<String, Integer>();

        SequencingOrder.getCount(this.primerVolumes, this.seqOrderExps, SequencingOrder.primerName, true);
        SequencingOrder.getCount(this.templateVolumes, this.seqOrderExps, SequencingOrder.TemplateName, true);
        this.templateVolumes = SequencingOrder.countToVolume(this.templateVolumes, 10, 10);
        this.primerVolumes = SequencingOrder.countToVolume(this.primerVolumes, 4, 10);

    }

    void buildNameWellMap() {
        this.primerWell = new TreeMap<String, String>();
        this.templateWell = new TreeMap<String, String>();

        assert this.primerVolumes.size() < 97;
        for (final String primerName : this.primerVolumes.keySet()) {
            // We know there is a next well, no need to check this. See if statement above 
            final String well = this.primerSource.next();
            if (this.robotType.equals(RobotType.standard96WellPlate)) {
                this.primerWell.put(primerName, Utils.convertWellNumber(well));
            } else {
                this.primerWell.put(primerName, well);
            }
        }

        assert this.templateVolumes.size() < 97;
        for (final String templateName : this.templateVolumes.keySet()) {
            // We know there is a next well, as there cannot be more than 96 templates!  
            final String well = this.templateSource.next();
            this.templateWell.put(templateName, Utils.convertWellNumber(well));
        }

    }

    /*
     Mix and water destination Destination -> from seq prep sheet
     PRIMER Source -> 1-16 / A1-L8
     PRIMER Destination -> from seq prep sheet
     Template Source -> A1-H12 - i tell the user there to aliquot the templates
     Template Destination -> from seq prep sheet
     Reaction control (A1) -> A1
     */
    public String generateFixVolumeCSV() {

        final String[] csv = new String[] { "", "", "", "", "" };

        for (final SeqSetupExperiment sse : this.sses) {
            csv[0] += sse.wellNum + ",";
            csv[1] += this.primerWell.get(sse.primerName) + ",";
            csv[2] += sse.wellNum + ",";
            csv[3] += this.templateWell.get(sse.templateName) + ",";
            csv[4] += sse.wellNum + ",";
        }

        return this.toCSVString(csv);
    }

    /*
        Mix Volume -> 5,5,5,5
        Mix Destination -> from seq prep sheet
        Water Volume -> 6,6,6,6
        Water Destination -> from seq prep sheet
        PRIMER Source -> 1-16 / A1-L8
        PRIMER Destination -> from seq prep sheet
        Template Source -> A1-H12
        Template Destination -> from seq prep sheet 
        Reaction control (A1) -> A1
     */
    public String generateCSV() {
        final String[] csv = new String[] { "", "", "", "", "", "", "", "" };

        for (final SeqSetupExperiment sse : this.sses) {
            csv[0] += sse.getNormalizedPremixVol() + ",";
            csv[1] += sse.wellNum + ",";
            csv[2] += sse.getNormalizedWaterVol() + ",";
            csv[3] += sse.wellNum + ",";
            csv[4] += this.primerWell.get(sse.primerName) + ",";
            csv[5] += sse.wellNum + ",";
            // This is generated as a separate file in generateTemplateVolumeFile() method
            //csv[6] += sse.getNormalizedTemplateVol() + ",";
            csv[6] += this.templateWell.get(sse.templateName) + ",";
            csv[7] += sse.wellNum + ",";
        }

        return this.toCSVString(csv);
    }

    /* A1 -> Template Volume 
     * B1 -> Template Volume 
     * C1 -> Template Volume 
     * ... 
     * Separate file for with template volumes only needed for variable volume run
     */
    public String generateTemplateVolumeFile() {
        String tVolumeFile = "";
        for (final SeqSetupExperiment sse : this.sses) {
            tVolumeFile += sse.getNormalizedTemplateVol() + "\r\n"; // windows line end as clients are predominantly windows
        }
        return tVolumeFile;
    }

    /*
     * This method assumes that there are undeleted "," in the end of each string
     */
    private String toCSVString(final String[] csv) {
        String result = "";
        for (int i = 0; i < csv.length; i++) {
            result += csv[i].substring(0, csv[i].length() - 1) + "\r\n"; // windows line end as clients are predominantly windows
        }
        if (this.control != null) {
            result += new SeqSetupExperiment(this.control).wellNum;
        }
        return result;

    }

    static class ExternalFieldsHelper {
        // Primer source for Robot 1
        static int[] primerSource1D = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

        // This is the source for Robot2
        static String[][] primerSource2D =
            new String[][] { { "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8" },
                { "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8" },
                { "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8" },
                { "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8" },
                { "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8" },
                { "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8" },
                { "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8" },
                { "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8" },
                { "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8" },
                { "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8" },
                { "K1", "K2", "K3", "K4", "K5", "K6", "K7", "K8" },
                { "L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8" } };

        static String waterSource = "B1";

        // This is set elsewhere outside the PIMS
        // static String controlTemplate = "A";

        static String controlPrimer = "X";

        static Iterator<String> get96WellPlateSource() {
            return ExternalFieldsHelper.flatten2DSource(HolderFactory.POSITIONS96BY_ROW);
        }

        static Iterator<String> get1DPrimerSource() {
            final ArrayList<String> values = new ArrayList<String>();
            for (final int s : ExternalFieldsHelper.primerSource1D) {
                values.add(new Integer(s).toString());
            }
            return values.iterator();
        }

        static Iterator<String> get2DPrimerSource() {
            return ExternalFieldsHelper.flatten2DSource(ExternalFieldsHelper.primerSource2D);
        }

        private static Iterator<String> flatten2DSource(final String[][] source) {
            assert source != null;
            final ArrayList<String> flattenSource = new ArrayList<String>();
            for (int i = 0; i < source.length; i++) {
                final List<String> flattenSourceRow = Arrays.asList(source[i]);
                flattenSource.addAll(flattenSourceRow);
            }
            return flattenSource.iterator();
        }
    }

    /**
     * @return Returns the primerWell.
     */
    public final Map<String, String> getPrimerWell() {
        return this.primerWell;
    }

    /**
     * @return Returns the templateWell.
     */
    public final Map<String, String> getTemplateWell() {
        return this.templateWell;
    }

    /**
     * @return Returns the primerVolumes.
     */
    public final Set<Map.Entry<String, Integer>> getPrimerVolumes() {
        return this.primerVolumes.entrySet();
    }

    /**
     * @return Returns the templateVolumes.
     */
    public final Set<Map.Entry<String, Integer>> getTemplateVolumes() {
        return this.templateVolumes.entrySet();
    }

}
