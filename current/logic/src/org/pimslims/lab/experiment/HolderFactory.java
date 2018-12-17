/*
 * Created on 13-May-2005 @author: Chris Morris
 */
package org.pimslims.lab.experiment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.FlushMode;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.PersonUtility;
import org.pimslims.lab.SupplierFactory;
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.PinType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.persistence.PimsQuery;

/**
 * 
 * Factory methods to help work with plates. (Eventually methods like these will be generated automatically)
 * 
 * Most methods create an instance of a class. The required attributes and associations are parameters of the
 * methods. The optional ones can be provided in a map. If none are needed, pass
 * java.util.Collections.EMPTY_MAP.
 * 
 * These methods use strings to represent the position in the plate. These are e.g. A03, C10.
 * 
 * When these are handled as integer row and column numbers, they are indexed from zero. Column "1" has index
 * number 0.
 * 
 * @see SupplierFactory
 * 
 * @version 0.1
 */
public class HolderFactory {

    //TODO Sample.ROWS
    public static final String[] ROWS = new String[] { "A", "B", "C", "D", // 24 well
        "E", "F", "G", "H", // 96 well
        "I", "J", "K", "L", "M", "N", "O", "P", // 384 well plate
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", // 1536 well plate
    };

    public static final String[] COLUMNS = new String[] { "01", "02", "03", "04", "05", "06", "07", "08",
        "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"// enough for 384 well plate

    };

    public static String POSITION_NW = "NW";

    public static String POSITION_NE = "NE";

    public static String POSITION_SW = "SW";

    public static String POSITION_SE = "SE";

    private static Map<String, String> PLATE_POSITION_MAPPING = new HashMap<String, String>();
    static {
        HolderFactory.PLATE_POSITION_MAPPING.put(HolderFactory.POSITION_NW, "northWestPlate");
        HolderFactory.PLATE_POSITION_MAPPING.put(HolderFactory.POSITION_NE, "northEastPlate");
        HolderFactory.PLATE_POSITION_MAPPING.put(HolderFactory.POSITION_SW, "southWestPlate");
        HolderFactory.PLATE_POSITION_MAPPING.put(HolderFactory.POSITION_SE, "southEastPlate");
    }

    public static final String[][] POSITIONS96BY_ROW = new String[8][12];

    public static final List POSITIONS_BY_ROW = new java.util.ArrayList(96);
    static {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 12; j++) {
                HolderFactory.POSITIONS96BY_ROW[i][j] = HolderFactory.ROWS[i] + HolderFactory.COLUMNS[j];
                HolderFactory.POSITIONS_BY_ROW.add(HolderFactory.ROWS[i] + HolderFactory.COLUMNS[j]);
            }
        }
    }

    public static final List POSITIONS_BY_COLUMN_96 = new java.util.ArrayList(96);
    static {
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 8; i++) {
                HolderFactory.POSITIONS_BY_COLUMN_96.add(HolderFactory.ROWS[i] + HolderFactory.COLUMNS[j]);
            }
        }
    }

    /**
     * @param plate
     * @return a list of positions in the plate TODO fix this
     */
    public static List<String> getPositions(final HolderType referencePlate) {
        // was...
        // if (
        // 8==referencePlate.getMaxRow().intValue()
        // && 12==referencePlate.getMaxColumn().intValue()
        // ) {
        // return POSITIONS_LIST_96;
        // }
        // throw new UnsupportedOperationException("not yet implemented");
        final int numRows = referencePlate.getMaxRow().intValue();
        final int numCols = referencePlate.getMaxColumn().intValue();
        final List positions = new ArrayList();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                positions.add(HolderFactory.ROWS[i] + HolderFactory.COLUMNS[j]);
            }

        }
        return Collections.unmodifiableList(positions);
    }

    public static List<String> getPositions(final HolderType referencePlate, final String point) {

        final int numRows = referencePlate.getMaxRow().intValue();
        final int numCols = referencePlate.getMaxColumn().intValue();
        final List positions = new ArrayList();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (HolderFactory.POSITION_NW.equals(point)) {
                    positions.add(HolderFactory.ROWS[i] + HolderFactory.COLUMNS[j]);
                }
                if (HolderFactory.POSITION_NE.equals(point)) {
                    positions.add(HolderFactory.ROWS[i] + HolderFactory.COLUMNS[numCols + j]);
                }
                if (HolderFactory.POSITION_SW.equals(point)) {
                    positions.add(HolderFactory.ROWS[numRows + i] + HolderFactory.COLUMNS[j]);
                }
                if (HolderFactory.POSITION_SE.equals(point)) {
                    positions.add(HolderFactory.ROWS[numRows + i] + HolderFactory.COLUMNS[numCols + j]);
                }
            }

        }
        return Collections.unmodifiableList(positions);
    }

    public static ArrayList get96RowthanColPositions(final HolderType referencePlate) {
        final ArrayList positions = new ArrayList();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 8; j++) {
                positions.add(HolderFactory.ROWS[j] + HolderFactory.COLUMNS[i]);
            }

        }
        return positions;
    }

    /**
     * @param plate
     * @return a list of positions in the plate
     */
    public static List getPositions(final Holder plate) {
        return HolderFactory.getPositions((HolderType) plate.getHolderType());
    }

    public static List getRows(final HolderType referencePlate) {
        final List rows = new ArrayList();
        final int numRows = referencePlate.getMaxRow().intValue();
        for (int i = 0; i < numRows; i++) {
            rows.add(HolderFactory.ROWS[i]);
        }
        return rows;
    }

    public static List getRows(final Holder plate) {
        final AbstractHolderType ht = plate.getHolderType();
        if (null == ht) {
            return Collections.EMPTY_LIST;
        }
        /*
         * Workaround for strange exception from Hibernate: Exception: java.lang.ClassCastException:
         * org.pimslims.model.reference.AbstractHolderType$$EnhancerByCGLIB$$33f3d145 cannot be cast to
         * org.pimslims.model.reference.HolderType
         */
        final HolderType type = ht.get_Version().get(ht.get_Hook());
        return HolderFactory.getRows(type);
    }

    public static List getColumns(final HolderType referencePlate) {
        final List rows = new ArrayList();
        final int numCols = referencePlate.getMaxColumn().intValue();
        for (int i = 0; i < numCols; i++) {
            rows.add(HolderFactory.COLUMNS[i]);
        }
        return rows;
    }

    public static List getColumns(final Holder plate) {
        final AbstractHolderType ht = plate.getHolderType();
        if (null == ht) {
            return Collections.EMPTY_LIST;
        }
        /*
         * Workaround for strange exception from Hibernate: Exception: java.lang.ClassCastException:
         * org.pimslims.model.reference.AbstractHolderType$$EnhancerByCGLIB$$33f3d145 cannot be cast to
         * org.pimslims.model.reference.HolderType
         */
        final HolderType type = ht.get_Version().get(ht.get_Hook());
        return HolderFactory.getColumns(type);
    }

    /**
     * Create a record of a plate.
     * 
     * @param version current transaction
     * @param categories
     * @param code bar code of plate, or name
     * @param refHolder reference information for this plate type
     * @param attributes map field name => value
     * @return a represenation of the new plate
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Holder createHolder(final WritableVersion version, final LabNotebook access,
        final String name, final HolderType type, final Map attributes) throws ConstraintException,
        AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("name", name);
        a.put("holderCategories",
            version.findAll(HolderCategory.class, HolderCategory.PROP_ABSTRACTHOLDERTYPES, type));
        if (access != null) {
            a.put(LabBookEntry.PROP_ACCESS, access);
        }
        final Holder object = version.create(Holder.class, a);

        object.setHolderType(type);
        return object;
    }

    /**
     * Set the position of a sample in a plate
     * 
     * @param plate
     * @param sample
     * @param position
     * @throws ConstraintException
     */
    public static void positionSample(final Holder plate, final Sample sample, final String position)
        throws ConstraintException {
        sample.setRowPosition(HolderFactory.getRow(position) + 1); // the actual data in
        // sample is indexed
        // from 1
        sample.setColPosition(HolderFactory.getColumn(position) + 1);// the actual data in
        // sample is indexed
        // from 1
        sample.setHolder(plate);
    }

    /**
     * @param position, e.g. "C4"
     * @return row number, counting from 0 TODO use method below instead
     */
    public static int getColumn(final String position) {
        final String column = position.substring(1);
        return Integer.parseInt(column) - 1;
    }

    /**
     * @param position, e.g. "C04"
     * @param type represents the holder geometry
     * @return row number, counting from 1
     */
    @Deprecated
    // this test is now done in model
    public static int getColumn(final String position, final HolderType type) {
        final int ret = 1 + HolderFactory.getColumn(position);
        if (null != type) {
            if (ret > type.getMaxColumn()) {
                throw new IllegalArgumentException("Column must be from 1 to: " + type.getMaxColumn());
            }
        }
        return ret;
    }

    /**
     * @param row, counting from 0
     * @return "A".."H" TODO count from 1
     */
    public static String getRow(final int row) {
        if (row < 0) {
            return "";
        }
        return HolderFactory.ROWS[row];
    }

    /**
     * @param column, counting from 0
     * @return "1".."12" TODO count from 1
     */
    public static String getColumn(final int column) {
        if (column < 0) {
            return "";
        }
        return HolderFactory.COLUMNS[column];
    }

    /**
     * @param position, e.g. "C4"
     * @return row number, counting from 0 TODO use method below instead
     */
    public static int getRow(final String position) {
        final String row = position.substring(0, 1);
        final int ret = Arrays.asList(HolderFactory.ROWS).indexOf(row);
        if (-1 == ret) {
            throw new IllegalArgumentException("No such row: " + position);
        }
        return ret;
    }

    /**
     * @param position, e.g. "C4"
     * @param type represents the holder geometry
     * @return column number, counting from 1
     */
    @Deprecated
    // this check is now in model
    public static int getRow(final String position, final HolderType type) {
        final String row = position.substring(0, 1);
        final int ret = 1 + Arrays.asList(HolderFactory.ROWS).indexOf(row);
        if (null != type) {
            if (0 == ret || ret > type.getMaxRow()) {
                throw new IllegalArgumentException("Row must be from 1 to: " + type.getMaxRow());
            }
        }
        return ret;
    }

    public static String getPositionForName(final String position) {
        final String row = position.substring(0, 1);
        final String column = position.substring(1);
        final String s = row + new DecimalFormat("00").format(new Integer(column));
        //System.out.println("HolderFactory.getPositionForName[" + position + "," + s + "]");
        return s;
    }

    /**
     * Temporary fix, until ExperimentGroup supports a type group name => experiment type name
     */
    private static final Map TYPES = new HashMap();

    private static final Comparator<? super Experiment> BY_NAME = new Comparator() {

        @Override
		public int compare(final Object arg0, final Object arg1) {
            final Experiment page0 = (Experiment) arg0;
            final Experiment page1 = (Experiment) arg1;
            return page0.getName().compareTo(page1.getName());
        }
    };

    public static HolderType createHolderType(final WritableVersion wv, final String name,
        final Collection<HolderCategory> categories, final Map attributes) throws AccessException,
        ConstraintException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("name", name);
        a.put(AbstractHolderType.PROP_DEFAULTHOLDERCATEGORIES, categories);
        final ModelObject object = wv.create(HolderType.class, a);
        return (HolderType) object;
    }

    public static HolderCategory createHolderCategory(final WritableVersion wv, final String name,
        final Map<String, Object> attributes) throws AccessException, ConstraintException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("name", name);
        final ModelObject object = wv.create(HolderCategory.class, a);
        return (HolderCategory) object;
    }

    /**
     * @param version
     * @param name
     * @param purpose
     * @param startDate
     * @param endDate
     * @param protocol
     * @param numberOfExperiments
     * @param experimentTypes
     * @param string3
     * @param sampleCategories
     * @return the new group
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExperimentGroup createExperimentGroup(final WritableVersion version,
        final LabNotebook access, final String name, final String purpose,
        final Collection<ExperimentType> types, final Calendar startDate, final Calendar endDate,
        final String details, final Protocol protocol, final int numberOfExperiments) throws AccessException,
        ConstraintException {
        // improve performance
        final boolean oldFlushModel =
            ((WritableVersionImpl) version).getFlushMode().isFlushSessionAfterCreate();
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);
        version.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL); //TODO version.setFlushModeCommit();

        final ExperimentGroup group =
            HolderFactory.doCreateExperimentGroup(version, access, name, purpose, startDate, endDate,
                Collections.EMPTY_MAP);
        group.setDetails(details);
        // LATER object.set("experimentType", experimentType);
        // LATER group.addHolder(holder);

        final Map<String, Object> experimentAttributes =
            HolderFactory.getExperimentAttributes(version, types, details, protocol, Collections.EMPTY_MAP,
                group);

        // Prefix with "0" if less than 10. TODO handle <100, <1000...
        String numberPrefix = "0";
        for (int i = 1; i <= numberOfExperiments; i++) {
            if (i > 9) {
                numberPrefix = "";
            }
            HolderFactory.createExperimentInGroup(version, protocol, group, experimentAttributes,
                numberPrefix + i, numberPrefix + i, null);
        }
        version.flush();
        version.getSession().setFlushMode(org.hibernate.FlushMode.AUTO);
        // back to previous
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(oldFlushModel);
        return group;
    }

    /**
     * Create a record of an experiment group, and of the experiments in it.
     * 
     * TODO may have to add protocol as a parameter of this method
     * 
     * @param version current transaction
     * @param plate a plate that the output samples will appear in
     * @param name name to give to the group The default is the bar code of the plate
     * @param purpose description of the aim of the activity
     * @param details
     * @param outputSampleCategories the sample categories for the resulting samples
     * @param protocol the protocol for the experiments
     * @param attributes map field name => value for the group
     * @return the new reference information
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExperimentGroup createPlateExperiment(final WritableVersion version,
        final LabNotebook access, final Holder plate, final String name, final String purpose,
        final Collection<ExperimentType> types, final Calendar start, final Calendar end,
        final String details, final Protocol protocol, final Map attributes, final Collection<String> wells)
        throws ConstraintException, AccessException {
        final List<String> positions = HolderFactory.getPositions((HolderType) plate.getHolderType());

        return HolderFactory.createPlateExperimentWithPositions(version, access, plate, name, purpose, types,
            start, end, details, protocol, attributes, wells, positions, Collections.EMPTY_MAP);
    }

    public static ExperimentGroup createPlateExperimentWithPositions(final WritableVersion version,
        final LabNotebook access, final Holder plate, String name, final String purpose,
        final Collection<ExperimentType> types, final Calendar start, final Calendar end,
        final String details, final Protocol protocol, final Map attributes, final Collection<String> wells,
        final List<String> positions, final Map<String, ResearchObjective> constructs)
        throws AccessException, ConstraintException {

        final boolean oldFlushModel =
            ((WritableVersionImpl) version).getFlushMode().isFlushSessionAfterCreate(); // improve performance
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);
        final FlushMode hbFlushModle = version.getSession().getFlushMode();
        version.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL); //TODO version.setFlushModeCommit();

        if (null == name) {
            name = plate.getName();
        }
        final ExperimentGroup group =
            HolderFactory.doCreateExperimentGroup(version, access, name, purpose, start, end, attributes);
        // LATER object.set("experimentType", experimentType);
        // LATER group.addHolder(holder);

        final Map<String, Object> experimentAttributes =
            HolderFactory.getExperimentAttributes(version, types, details, protocol, attributes, group);
        for (final String position : positions) {
            //System.out.println(position);
            if (null != wells && !wells.contains(position)) {
                continue;
            }
            //final Collection<Sample> samples =
            HolderFactory.createExperimentInPlate(version, plate, protocol, constructs.get(position), group,
                experimentAttributes, position);
        }
        version.flush();
        version.getSession().setFlushMode(hbFlushModle);
        // back to previous
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(oldFlushModel);
        return group;
    }

    public static void createPlateInGroup(final WritableVersion version, final LabNotebook access,
        final ExperimentGroup group, final Holder holder, String name,
        final Collection<ExperimentType> types, final String details, final Protocol protocol,
        final Map attributes, final Collection<String> wells, final List<String> positions,
        final Map<String, ResearchObjective> constructs) throws AccessException, ConstraintException {

        final boolean oldFlushModel =
            ((WritableVersionImpl) version).getFlushMode().isFlushSessionAfterCreate(); // improve performance
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);
        final FlushMode hbFlushModle = version.getSession().getFlushMode();
        version.getSession().setFlushMode(org.hibernate.FlushMode.MANUAL); //TODO version.setFlushModeCommit();

        if (null == name) {
            name = group.getName();
        }

        final Map<String, Object> experimentAttributes =
            HolderFactory.getExperimentAttributes(version, types, details, protocol, attributes, group);
        for (final String position : positions) {
            //System.out.println(position);
            if (null != wells && !wells.contains(position)) {
                continue;
            }
            //final Collection<Sample> samples =
            HolderFactory.createExperimentInPlate(version, holder, protocol, constructs.get(position), group,
                experimentAttributes, position);
        }
        version.flush();
        version.getSession().setFlushMode(hbFlushModle);
        // back to previous
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(oldFlushModel);
        return;
    }

    public static Collection<Sample> createExperimentInPlate(final WritableVersion version,
        final Holder plate, final Protocol protocol, final ResearchObjective construct,
        final ExperimentGroup group, final Map<String, Object> experimentAttributes, final String position)
        throws AccessException, ConstraintException {
        final Collection<Sample> samples =
            HolderFactory.createExperimentInGroup(version, protocol, group, experimentAttributes, position,
                HolderFactory.getPositionForName(position), construct);
        for (final Iterator iterator = samples.iterator(); iterator.hasNext();) {
            final Sample sample = (Sample) iterator.next();
            sample.setRowPosition(HolderFactory.getRow(position) + 1);// the actual data in sample is indexed from 1
            sample.setColPosition(HolderFactory.getColumn(position) + 1);// the actual data in sample is indexed from 1
            sample.setHolder(plate);
        }
        return samples;
    }

    /**
     * @param version
     * @param name
     * @param purpose
     * @param startDate
     * @param endDate
     * @param attributes
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static ExperimentGroup doCreateExperimentGroup(final WritableVersion version,
        final LabNotebook access, final String name, final String purpose, final Calendar startDate,
        final Calendar endDate, final Map attributes) throws AccessException, ConstraintException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put(ExperimentGroup.PROP_NAME, name);
        a.put(ExperimentGroup.PROP_PURPOSE, purpose);
        a.put(ExperimentGroup.PROP_STARTDATE, startDate);
        a.put(ExperimentGroup.PROP_ENDDATE, endDate);
        if (access != null) {
            a.put(LabBookEntry.PROP_ACCESS, access);
        }
        final ExperimentGroup group = version.create(ExperimentGroup.class, a);
        return group;
    }

    /**
     * @param version
     * @param types
     * @param details
     * @param protocol
     * @param attributes
     * @param group
     * @return
     */
    public static Map<String, Object> getExperimentAttributes(final WritableVersion version,
        final Collection<ExperimentType> types, final String details, final Protocol protocol,
        final Map attributes, final ExperimentGroup group) {
        final Map<String, Object> experimentAttributes = new java.util.HashMap<String, Object>(attributes);
        experimentAttributes.put(Experiment.PROP_STARTDATE, group.getStartDate());
        experimentAttributes.put(Experiment.PROP_ENDDATE, group.getEndDate());
        experimentAttributes.put(Experiment.PROP_STATUS, "To_be_run");
        experimentAttributes.put(Experiment.PROP_EXPERIMENTTYPE, types);
        experimentAttributes.put(LabBookEntry.PROP_DETAILS, details);
        if (null != PersonUtility.getCurrentUserAsPerson(version)) {
            experimentAttributes.put(LabBookEntry.PROP_CREATOR, version.getCurrentUser());
        }
        if (null != protocol) {
            experimentAttributes.put(Experiment.PROP_PROTOCOL, protocol);
        }
        return experimentAttributes;
    }

    /**
     * @param version
     * @param plate
     * @param name
     * @param details
     * @param outputSampleCategories
     * @param protocol
     * @param group
     * @param experimentAttributes
     * @param position
     * @param positionForName
     * @throws AccessException
     * @throws ConstraintException
     */
    public static Collection<Sample> createExperimentInGroup(final WritableVersion version,
        final Protocol protocol, final ExperimentGroup group, final Map<String, Object> experimentAttributes,
        final String position, final String positionForName, final ResearchObjective construct)
        throws AccessException, ConstraintException {

        final Map<String, Object> newExperimentAttributes =
            new java.util.HashMap<String, Object>(experimentAttributes);
        newExperimentAttributes.put(Experiment.PROP_EXPERIMENTGROUP, group);
        newExperimentAttributes.put(Experiment.PROP_NAME, group.getName() + ":" + position);
        final Experiment experiment = version.create(Experiment.class, newExperimentAttributes);
        experiment.setProject(construct);

        final Map<String, Object> newOutputSampleAttributes = new java.util.HashMap<String, Object>();
        final Map<String, Object> newSampleAttributes = new java.util.HashMap<String, Object>();
        newSampleAttributes.put(Sample.PROP_AMOUNTUNIT, "L");
        newSampleAttributes.put(Sample.PROP_CURRENTAMOUNT, 0f);
        newSampleAttributes.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        assert null != protocol;
        ExperimentFactory.createProtocolParametersForExperiment(version, protocol, experiment);
        HolderFactory.createInputSamplesForExperiment(version, experiment, protocol);

        ExperimentUtility.setExpBlueprintSamples(experiment, construct, true);

        final Collection<RefOutputSample> outputs = protocol.getRefOutputSamples();
        final Collection<Sample> ret = new HashSet<Sample>(outputs.size());
        for (final Iterator iterator = outputs.iterator(); iterator.hasNext();) {
            final RefOutputSample ros = (RefOutputSample) iterator.next();
            newOutputSampleAttributes.put(OutputSample.PROP_REFOUTPUTSAMPLE, ros);
            // LATER etc
            // newSampleAttributes.put(OutputSample.PROP_AMOUNTUNIT,
            // ros.getUnit());
            newOutputSampleAttributes.put(OutputSample.PROP_NAME, ros.getName());
            final SampleCategory output = ros.getSampleCategory();
            if (null != output) {
                newSampleAttributes.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(output));
            }
            String name = group.getName() + ":" + positionForName;
            if (1 < outputs.size()) {
                name = group.getName() + ":" + ros.getName() + ":" + positionForName;
            }
            final Sample sample =
                SampleFactory.createSample(version, name, Collections.singleton(output), newSampleAttributes);

            newOutputSampleAttributes.put(OutputSample.PROP_SAMPLE, sample);
            newOutputSampleAttributes.put(OutputSample.PROP_EXPERIMENT, experiment);
            new OutputSample((WritableVersion) experiment.get_Version(), newOutputSampleAttributes);
            ret.add(sample);
        }
        //System.out.println(position + "" + HolderFactory.getPositionInHolder(experiment)); 
        return ret;
    }

    /**
     * @param plate the plate the output samples are in
     * @return
     * 
     */
    public static ExperimentGroup getExperimentGroup(final Holder plate) {
        if (plate == null) {
            return null;
        }
        final Sample sample = (Sample) plate.findFirst(Holder.PROP_SAMPLES);
        if (null == sample) {
            return null;
        }

        if (sample.getOutputSample() == null || sample.getOutputSample().getExperiment() == null) {
            return null;
        }
        final Experiment experiment = sample.getOutputSample().getExperiment();
        return experiment.getExperimentGroup();
    }

    public static ExperimentType getExperimentType(final Holder plate) {
        final ExperimentGroup eg = HolderFactory.getExperimentGroup(plate);
        if (eg == null) {
            return null;
        }
        final Experiment exp = (Experiment) eg.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
        if (exp != null) {
            return exp.getExperimentType();
        }
        // else
        return null;
    }

    @Deprecated
    // there may be more than one plate for the group
    public static Holder getPlate(final ExperimentGroup group) {
        //  final long start = System.currentTimeMillis();
        Holder holder = null;
        final Collection<Experiment> exps = group.getExperiments();
        if (exps != null && exps.size() > 0) {
            holder = HolderFactory.getPlate(exps.iterator().next());
        }
        //  System.out.println("HolderFactory.getPate() using " + (System.currentTimeMillis() - start) + "ms");
        return holder;
    }

    public static Holder getPlate(final Experiment exp) {
        final Set<OutputSample> oss = exp.getOutputSamples();
        for (final Iterator iter = oss.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            if (null == os.getSample()) {
                continue;
            }
            return os.getSample().getHolder();
        }
        return null; // can't tell
    }

    public static Collection<Holder> getHoldersByExperimentType(final ExperimentType et) {
        final Set<Holder> holders = new HashSet<Holder>();
        for (final Experiment exp : et.getExperiments()) {
            final Holder holder = HolderFactory.getPlate(exp);
            if (holder != null) {
                holders.add(holder);
            }
        }
        return holders;
    }

    /**
     * @param experiment
     * @return it's position in the plate, if any
     */
    public static String getPositionInHolder(final Sample sample) {
        if (null == sample) {
            return null;
        }
        if (null == sample.getRowPosition() || null == sample.getColPosition()) {
            return null;
        }
        return HolderFactory.ROWS[HolderFactory.getSampleRow(sample)]
            + HolderFactory.COLUMNS[HolderFactory.getSampleColumn(sample)];
    }

    /**
     * @param experiment
     * @return it's row in the plate, numbered from zero, or -1 if not known
     * 
     */
    public static int getRow(final Experiment experiment) {
        final Collection<OutputSample> oss = experiment.getOutputSamples();
        return HolderFactory.getRow(oss);
    }

    public static int getRow(final Collection<OutputSample> oss) {
        if (oss.isEmpty()) {
            return -1;
        }
        for (final Iterator iter = oss.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            if (null == os.getSample()) {
                continue;
            }
            return HolderFactory.getSampleRow(os.getSample());
        }
        return -1; // can't tell
    }

    /**
     * @param sample
     * @return it's row in the plate, numbered from zero, or -1 if not known
     */
    public static int getSampleRow(final Sample sample) {
        if (null == sample.getRowPosition()) {
            return -1;
        }
        return sample.getRowPosition() - 1;// the actual data in sample is
        // indexed from 1
    }

    /**
     * @param sample
     * @return it's row in the plate, numbered from zero, or -1 if not known
     */
    public static int getSampleColumn(final Sample sample) {
        if (null == sample.getColPosition()) {
            return -1;
        }
        return sample.getColPosition() - 1;// the actual data in sample is
        // indexed from 1
    }

    /**
     * @param experiment
     * @return it's column in the plate, numbered from zero, or -1 if not known
     * 
     */
    public static int getColumn(final Experiment experiment) {
        final Collection<OutputSample> oss = experiment.getOutputSamples();
        return HolderFactory.getColumn(oss);
    }

    public static int getColumn(final Collection<OutputSample> oss) {
        if (oss.isEmpty()) {
            return -1;
        }
        for (final Iterator iter = oss.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            if (null == os.getSample()) {
                continue;
            }
            return HolderFactory.getSampleColumn(os.getSample());
        }
        return -1; // can't tell
    }

    /**
     * @param experiment
     * @return it's position in the plate, if any
     * 
     */
    public static String getPositionInHolder(final Experiment experiment) {
        final Collection<OutputSample> oss = experiment.getOutputSamples();
        assert 0 < oss.size() : "No output for: " + experiment.getName();
        for (final Iterator iter = oss.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            assert null != os.getSample();
            if (null == os.getSample()) {
                continue;
            }
            return HolderFactory.getPositionInHolder(os.getSample());
        }
        return null; // can't tell
    }

    public static Experiment getExperimentByPosition(final ExperimentGroup group, final int row,
        final int column) {
        final Collection experiments = group.getExperiments();
        assert 0 < experiments.size() : "No experiments for group";
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            if (HolderFactory.getColumn(experiment) == column && HolderFactory.getRow(experiment) == row) {
                return experiment;
            }

        }
        return null; // not found
    }

    /**
     * 
     * HolderFactory.getExperimentByName
     * 
     * @param group
     * @param position
     * @return
     */
    public static Experiment getExperimentByName(final ExperimentGroup group, final String name) {
        //TODO convert position A1 =>A01
        final Collection experiments = group.getExperiments();
        assert 0 < experiments.size() : "No experiments for group";
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            if (experiment.getName().equals(name)) {
                return experiment;
            }
        }
        return null; // not found
    }

    /**
     * HolderFactory.positionEquals
     * 
     * @param position a well position, e.g B03 or B3
     * @param outputPosition another
     * @return true if they match
     */
    public static boolean positionEquals(final String well0, final String well1) {
        if (well0.charAt(0) != well1.charAt(0)) {
            return false;
        }
        final int col0 = HolderFactory.getColumn(well0);
        final int col1 = HolderFactory.getColumn(well1);
        return col0 == col1;
    }

    /**
     * @param plate
     * @param row counting from 0
     * @param column coutning from 0
     * @return
     */
    public static Sample getSampleByPosition(final Holder plate, final int row, final int column) {

        final int rowNumber = new Integer(row) + 1;// the actual data in sample is
        // indexed from 1
        final int colNumber = new Integer(column) + 1;// the actual data in sample is
        // indexed from 1
        final Collection<Sample> samples = plate.getSamples();
        Sample sampleFound = null;
        for (final Sample sample : samples) {
            if (sample.getRowPosition().equals(rowNumber) && sample.getColPosition().equals(colNumber)) {
                sampleFound = sample;
                break;
            }
        }

        return sampleFound;
    }

    @Deprecated
    public static Sample getSampleByPosition(final Holder plate, final String position) {
        return HolderFactory.getSampleByPosition(plate, HolderFactory.getRow(position),
            HolderFactory.getColumn(position));
    }

    /**
     * Add an input sample to all members of an experiment group.
     * 
     * @param output the plate the experiment will be performed in
     * @param group the experiment group
     * @param input the input sample
     * @param amount volume in L to pipette LATER
     * @throws ConstraintException if the input sample is exhausted this method is not currently used
     * @param inputSampleName
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void pipetteToAllWells(final WritableVersion wv, final Holder output,
        final ExperimentGroup group, final Sample input, final String inputSampleName, final float amount)
        throws ConstraintException, AccessException {
        final Collection positions = HolderFactory.getPositions(output);
        for (final Iterator iter = positions.iterator(); iter.hasNext();) {
            final String position = (String) iter.next();
            HolderFactory.pipetteToWell(wv, position, group, input, inputSampleName, amount);
        }
    }

    /**
     * Add input samples to all members of an experiment group.
     * 
     * @param wv the current transaction
     * @param input the plate to pipette from
     * @param group the experiment group to pipette to
     * @param inutSampleName the name of the input sample being added
     * @param amount in litres
     * @throws ConstraintException
     * @throws AccessException
     */
    public static void multiLinePipette(final WritableVersion wv, final Holder input,
        final ExperimentGroup group, final String inputSampleName, final Float amount)
        throws ConstraintException, AccessException {

        System.out.println("HolderFactory.multiLinePipette [" + input.getName() + "," + group.getName() + ","
            + inputSampleName + "]");
        final Collection samples = input.getSamples();
        for (final Iterator iter = samples.iterator(); iter.hasNext();) {
            final Sample inputSample = (Sample) iter.next();
            HolderFactory.pipetteToWell(wv, HolderFactory.getPositionInHolder(inputSample), group,
                inputSample, inputSampleName, amount);
        }
    }

    public static void multiLinePipette(final WritableVersion wv, final ExperimentGroup fromGroup,
        final ExperimentGroup toGroup, final String inputSampleName, final Float amount)
        throws ConstraintException, AccessException {

        final List<Experiment> fromExperiments = new ArrayList(fromGroup.getExperiments());
        Collections.sort(fromExperiments, HolderFactory.BY_NAME);
        final List<Experiment> toExperiments = new ArrayList(toGroup.getExperiments());
        Collections.sort(toExperiments, HolderFactory.BY_NAME);
        assert fromExperiments.size() == toExperiments.size();
        final Iterator fromIterator = fromExperiments.iterator();
        for (final Iterator toIterator = toExperiments.iterator(); toIterator.hasNext();) {
            final Experiment toExperiment = (Experiment) toIterator.next();
            final Experiment fromExperiment = (Experiment) fromIterator.next();
            final Set<OutputSample> oss = fromExperiment.getOutputSamples();
            for (final Iterator iterator = oss.iterator(); iterator.hasNext();) {
                final OutputSample outputSample = (OutputSample) iterator.next();
                if (null != outputSample.getSample()) {
                    HolderFactory.setInputSample(outputSample.getSample(), inputSampleName, amount,
                        toExperiment);
                }
            }
        }

        /* for (final Holder holder : HolderFactory.getHolders(fromGroup)) {
             final Collection samples = holder.getSamples();
             for (final Iterator iter = samples.iterator(); iter.hasNext();) {
                 final Sample fromSample = (Sample) iter.next();
                 //                HolderFactory.pipetteToWell(wv, HolderFactory.getPositionInHolder(sample), group, sample,
                 //                    inputSampleName, amount);

                 final Experiment toExperiment =
                     HolderFactory.findExperimentForPosition(HolderFactory.getPositionInHolder(fromSample), toGroup);
                 HolderFactory.setInputSample(fromSample, inputSampleName, amount, toExperiment);
             }
         }*/
    }

    /**
     * Add an input sample to a member of an experiment group. The experiment concerned will be created if
     * necessary.
     * 
     * @param holder the plate to pipette into
     * @param position the well to pipette into
     * @param group the experiment group
     * @param input the sample to pipette
     * @param inputSampleName
     * @param amount volume in L to pipette
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void pipetteToWell(final WritableVersion wv, final String position,
        final ExperimentGroup group, final Sample input, final String inputSampleName, final Float amount)
        throws ConstraintException, AccessException {

        //System.out.println("HolderFactory.pipetteToWell [" + position + "," + input.getName() + ","
        //*+ input.getRefSample().getName()*/+ "," + group.getName() + "," + inputSampleName + "]");
        //System.out.println("HolderFactory.pipetteToWell [" + position + "," + input + "," + group + ","
        //    + inputSampleName + "," + amount + "]");

        assert null != input;

        final Experiment experiment = HolderFactory.findExperimentForPosition(position, group);

        HolderFactory.setInputSample(input, inputSampleName, amount, experiment);
    }

    private static void setInputSample(final Sample input, final String inputSampleName, final Float amount,
        final Experiment experiment) throws ConstraintException {
		/*
		 * if (null == experiment.getProject()) { // set target from previous
		 * experiment, if any
		 * experiment.setProject(ExperimentFactory.getTargetOrConstruct(input));
		 * }
		 */

        InputSample is = null;
        for (final InputSample inputSample : experiment.getInputSamples()) {
            if (inputSample.getName().equals(inputSampleName)) {
                is = inputSample;
                break;
            }
        }
        assert null != is : "No such input sample in experiment: " + inputSampleName;

        is.setSample(input);
        if (null != amount) {
            is.setAmount(amount);
        }
		ExperimentFactory.propagateExperimentProperties(input, experiment);
    }

	private static Experiment findExperimentForPosition(final String position,
			final ExperimentGroup group) {
        Experiment experiment;
        Sample output = null;
        for (final Holder holder : HolderFactory.getPlates(group)) {
            for (final Sample sample : holder.getSamples()) {
                if (HolderFactory.getSampleRow(sample) == HolderFactory.getRow(position)
                    && HolderFactory.getSampleColumn(sample) == HolderFactory.getColumn(position)) {
                    output = sample;
                    break;
                }
            }
        }

        if (null == output) {
            throw new IllegalStateException("No experiment for position: " + position);
        }

        experiment = output.getOutputSample().getExperiment();
        return experiment;
    }

    /**
     * Create a new record of an experiment, in a given experiment group
     * 
     * @param version current transaction
     * @param name name to give to it
     * @param purpose description of the aim of the activity
     * @param attributes map field name => value
     * @return the new reference information
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Experiment newExperiment(final WritableVersion version, final ExperimentGroup group,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("startDate", group.getStartDate());
        a.put("endDate", group.getEndDate());
        a.put("experimentType", HolderFactory.TYPES.get(group.getName()));
        final ModelObject object = version.create(Experiment.class, a);
        final Experiment experiment = (Experiment) object;
        experiment.setExperimentGroup(group);
        return experiment;
    }

    public static Protocol getProtocol(final Holder holder) {
        final Sample sample = (Sample) holder.findFirst(Holder.PROP_SAMPLES);
        if (null == sample) {
            return null;
        }
        //assert null != sample.getOutputSample() : "Sample is not experiment output";
        if (null == sample.getOutputSample()) {
            return null;
        }
        final Experiment experiment = sample.getOutputSample().getExperiment();
        return experiment.getProtocol();
    }

    private static final Map ACTIVE = new HashMap<String, Object>();
    static {
        HolderFactory.ACTIVE.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
    }

    /**
     * When a plate experiment is performed, then the output samples are left in the plate. These may then be
     * pipetted for use in another experiment. This method finds suitable plates to pipette from, given the
     * sample category for the input concerned.
     * 
     * @param category the sample specification
     * @return a collection of holders containing samples of this type
     */
    @Deprecated
    // too slow
    public static Collection<Holder> getHolders(final SampleCategory category) {
        final Collection<Holder> ret = new ArrayList<Holder>();
        final Collection<Holder> allHolders = category.get_Version().getAll(Holder.class);
        for (final Iterator iter = allHolders.iterator(); iter.hasNext();) {
            final Holder holder = (Holder) iter.next();
            final Sample sample = holder.findFirst(Holder.PROP_SAMPLES, AbstractSample.PROP_ISACTIVE, true);
            if (sample == null) {
                continue;
            }
            if (sample.getSampleCategories().contains(category)) {
                ret.add(holder);
            }
            assert null != sample.getIsActive() && sample.getIsActive().booleanValue();

            /*
             * // but see PIMS-592 //TODO restore findFirst, this code will be slow Collection<Sample>
             * samples = holder.getSamples(); samples: for (Iterator iterator = samples.iterator();
             * iterator.hasNext();) { sample = (Sample) iterator.next(); if (null!=sample.getIsActive() &&
             * sample.getIsActive().booleanValue() && refSample==sample.getRefSample()) { ret.add(holder);
             * break samples; }
             */
        }

        return ret;
    }

    public static Protocol getProtocolFromExpGroup(final ExperimentGroup group) {
        if (group == null || group.getExperiments() == null || group.getExperiments().size() == 0) {
            return null;
        }
        // else
        return group.getExperiments().iterator().next().getProtocol();
    }

    public static final String NONE = "[none]";

    public static void createInputSamplesForExperiment(final WritableVersion wv, final Experiment experiment,
        final Protocol protocol) throws ConstraintException, AccessException {

        // find all the existing input samples
        final Collection<InputSample> inputSamples = experiment.getInputSamples();
        final Collection<String> names = new HashSet(inputSamples.size());
        for (final Iterator iter = inputSamples.iterator(); iter.hasNext();) {
            final InputSample is = (InputSample) iter.next();
            names.add(is.getName());
        }

        final Collection<RefInputSample> InputSampleDefCol = protocol.getRefInputSamples();
        for (final RefInputSample ris : InputSampleDefCol) {
			if (null != ris.getRecipe()) {
				continue; // don't make input samples for reagents
			}
            final String name = ris.getName();
            if (names.contains(name)) {
                continue;
            } // don't make duplicate
            final Float amount = ris.getAmount();
            final String unit = ris.getUnit();

            final InputSample made = new InputSample(wv, experiment);
            made.setName(ris.getName());
            made.setRefInputSample(ris);
            made.setRefInputSample(ris);
            made.setAmountUnit(unit);
            made.setAmountDisplayUnit(ris.getDisplayUnit());
            made.setAmount(amount);

        }

    }// EndOf createInputSamplesForExperiment

    public static boolean isPlate(final AbstractHolderType holderType) {

        if (holderType instanceof PinType) {
            return false;
        }
        final HolderType type = (HolderType) holderType;
        if (null == type) {
            return false;
        }
        final String name = type.getName().toLowerCase();
        if (name.contains("box") || name.contains("matrix")) {
            return false;
        }
        if (name.contains("plate")) {
            return true;
        }
        if (null == type.getMaxColumn() || null == type.getMaxRow()) {
            return false;
        }
        if (0 != type.getMaxColumn() % 6) {
            return false;
        }
        if (0 != type.getMaxRow() % 4) {
            return false;
        }

        return true;
    }

    /**
     * 
     * HolderFactory.getHolderPoint
     * 
     * @param holder
     * @return
     */
    public static String getHolderPoint(final Holder holder) {

        final HolderType type = (HolderType) holder.getHolderType();
        final Sample sample = holder.getSamples().iterator().next();

        if (null == sample) {
            return "unknownPlate";
        }

        return HolderFactory.PLATE_POSITION_MAPPING.get(HolderFactory.getHolderPosition(type,
            sample.getRowPosition(), sample.getColPosition()));
    }

    public static String getHolderPosition(final HolderType type, final int row, final int col) {

        if (row > type.getMaxRow()) {
            if (col > type.getMaxColumn()) {
                return HolderFactory.POSITION_SE;
            } else {
                return HolderFactory.POSITION_SW;
            }
        } else {
            if (col > type.getMaxColumn()) {
                return HolderFactory.POSITION_NE;
            } else {
                return HolderFactory.POSITION_NW;
            }
        }

    }

    /**
     * PlateExperimentDAO.getHolders
     * 
     * @param eg
     * @return
     */
    public static Collection<Holder> getPlates(final ExperimentGroup eg) {
        //TODO add join to HolderType to check that these are plates
        // use outer join, to check that ALL samples are in a plate
        final String selectHQL =
        // JMD Performance improvement
        //" select distinct os.sample.holder from  OutputSample as os"
        //    + " where os.experiment.experimentGroup=:group";
            "from Holder h where h.id in (select distinct os.sample.holder.id from OutputSample as os"
                + " where os.experiment.experimentGroup.id=:egid)";

        final ReadableVersion rv = eg.get_Version();
        final PimsQuery query = PimsQuery.getQuery(rv, selectHQL);
        // JMD Change to go with HQL change above
        //query.setEntity("group", eg);
        query.setLong("egid", eg.getDbId());

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Collection results = query.list();
        final Collection<Holder> ret = new ArrayList(results.size());
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final Holder holder = (Holder) iterator.next();
            if (HolderFactory.isPlate(holder.getHolderType())) {
                ret.add(holder);
            }
        }
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
        return ret;
    }

    /**
     * HolderFactory.isPlateExperiment
     * 
     * @param group
     * @return
     */
    public static boolean isPlateExperiment(final ExperimentGroup group) {
        // note that the user already has legitimate access to the group
        //TODO add join to HolderType to check that these are plates
        // use outer join, to check that ALL samples are in a plate
        final String selectHQL =
            " select os.sample from  OutputSample as os" + " where os.experiment.experimentGroup=:group";

        final ReadableVersion rv = group.get_Version();
        final PimsQuery query = PimsQuery.getQuery(rv, selectHQL);

        query.setEntity("group", group);

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Collection results = query.list();
        if (results.isEmpty()) {
            return false;
        }
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final Sample sample = (Sample) iterator.next();
            final Holder holder = sample.getHolder();
            if (null == holder) {
                return false;
            }
            if (!HolderFactory.isPlate(holder.getHolderType())) {
                return false;
            }
        }
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
        return true;
    }

}
