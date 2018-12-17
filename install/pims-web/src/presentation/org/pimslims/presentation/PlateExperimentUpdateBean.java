/**
 * pims-web org.pimslims.presentation PlateExperimentUpdateBean.java
 * 
 * @author Marc Savitsky
 * @date 9 Feb 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.plateExperiment.PlateExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentParameterBean;
import org.pimslims.presentation.protocol.ProtocolBean;

/**
 * PlateExperimentUpdateBean Represents a group of experiments, possibly in one or more plates 2D. If the
 * plates have subPositions, it represents a "slice" corresponding to a particular subposition.
 */
public class PlateExperimentUpdateBean implements Comparable {

    protected final List<PlateExperimentBean> experimentBeans = new ArrayList<PlateExperimentBean>();

    private ExperimentGroup group = null;

    private ProtocolBean protocolBean = null;

    private final Map<String, String> parameterMap;

    private final Map<String, String> inputsMap;

    //private final HolderType holderType;
    private Holder NW_Plate;

    private Holder NE_Plate;

    private Holder SW_Plate;

    private Holder SE_Plate;

    private int groupScore = 0;

    private int maxSubPositions = 1;

    /**
     * Constructor for PlateExperimentUpdateBean
     * 
     * @param experimentGroup
     * @param subPosition null for all wells, or the subPosition wanted
     * @throws AccessException
     * @throws ServletException
     */
    public PlateExperimentUpdateBean(final ExperimentGroup experimentGroup, final Integer subPosition)
        throws AccessException {

        //System.out.println("-----PlateExperimentUpdateBean - in constructor ---------------------------");  
        final long timings[] = new long[6];
        timings[0] = System.currentTimeMillis();

        this.group = experimentGroup;
        final Set<Experiment> experiments = this.group.getExperiments();
        if (experiments.iterator().hasNext()) {
            final Experiment experiment = experiments.iterator().next();
            if (null != experiment.getProtocol()) {
                this.protocolBean = (ProtocolBean) BeanFactory.newBean(experiment.getProtocol());
            }
        }

        timings[1] = System.currentTimeMillis();

        /* Preload the properties needed in the loop, to avoid 96 selects.
         * You could also usefully get the sample categories, 
         * and some many-to-many associations. 
         * 
         * */
        final String selectHQL =
            "select experiment from Experiment experiment "
                + "left join fetch experiment.access "
                + "left join fetch experiment.experimentType left join fetch experiment.experimentGroup "
                + "left join fetch experiment.researchObjective ro left join fetch ro.researchObjectiveElements "
                + "left join fetch experiment.attachments " + "left join fetch experiment.milestones "
                + "left join fetch experiment.inputSamples inputSample "
                + "   left join fetch inputSample.sample " + "   left join fetch inputSample.refInputSample "
                + "left join fetch experiment.outputSamples outputSample "
                + "  left join fetch outputSample.sample sample left join fetch sample.holder "
                + "  left join fetch outputSample.refOutputSample  "
                + "left join fetch experiment.parameters parm left join parm.parameterDefinition "
                + "where experiment.experimentGroup.id =:groupid";
        final ReadableVersion rv = experimentGroup.get_Version();

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL, Experiment.class);
        query.setLong("groupid", experimentGroup.getDbId());
        query.list();

        timings[2] = System.currentTimeMillis();

        //TODO this is slow, it makes 9 database requests per iteration
        System.out.println("-----start of loop ---------------------------"); //TODO remove
        experiment: for (final Iterator<Experiment> iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = iterator.next();
            if (null != subPosition) {
                final Set<OutputSample> oss = experiment.getOutputSamples();
                oss: for (final Iterator ossIt = oss.iterator(); ossIt.hasNext();) {
                    final OutputSample os = (OutputSample) ossIt.next();
                    if (null == os.getSample() || null == os.getSample().getSubPosition()) {
                        continue oss; // unspecified subposition, so include
                    }
                    if (!subPosition.equals(os.getSample().getSubPosition())) {
                        continue experiment; // other subposition, so ignore
                    }
                }
            }
            final PlateExperimentBean experimentBean = new PlateExperimentBean(experiment);
            this.experimentBeans.add(experimentBean);
            for (final PlateExperimentParameterBean bean : experimentBean.getParameterBeans()) {
                if (bean.getIntValue() > 0) {
                    this.groupScore += bean.getIntValue();
                }
            }
        }

        System.out.println("----- end of loop ---------------------------"); //TODO remove
        timings[3] = System.currentTimeMillis();

        //TODO probably not used
        this.parameterMap = PlateExperimentUpdateBean.getParametersForJSON(experiments);
        this.inputsMap = PlateExperimentUpdateBean.getInputsForJSON(experiments);

        timings[4] = System.currentTimeMillis();

        final Collection<Holder> holders = HolderFactory.getPlates(this.getExperimentGroup());
        for (final Holder plate : holders) {
            // there are none, unless this is a plate experiment
            final HolderType type = (HolderType) plate.getHolderType();
            if (null != type.getMaxSubPosition()) {
                this.maxSubPositions = type.getMaxSubPosition();
            }
            final Sample sample = plate.getSamples().iterator().next();
            if (HolderFactory.POSITION_NW.equals(HolderFactory.getHolderPosition(type,
                sample.getRowPosition(), sample.getColPosition()))) {
                this.NW_Plate = plate;
            }
            if (HolderFactory.POSITION_NE.equals(HolderFactory.getHolderPosition(type,
                sample.getRowPosition(), sample.getColPosition()))) {
                this.NE_Plate = plate;
            }
            if (HolderFactory.POSITION_SW.equals(HolderFactory.getHolderPosition(type,
                sample.getRowPosition(), sample.getColPosition()))) {
                this.SW_Plate = plate;
            }
            if (HolderFactory.POSITION_SE.equals(HolderFactory.getHolderPosition(type,
                sample.getRowPosition(), sample.getColPosition()))) {
                this.SE_Plate = plate;
            }
        }

        timings[5] = System.currentTimeMillis();
        System.out
            .println("PlateUpdateExperimentBean performance: total=" + (timings[5] - timings[0]) + "ms");
        for (int t = 0; t < 5; t++) {
            System.out.println("PlateUpdateExperimentBean performance: (" + (t + 1) + "-" + t + ")="
                + (timings[t + 1] - timings[t]) + "ms");
        }
        System.out.println("-----PlateExperimentUpdateBean - done constructor ---------------------------"); //TODO remove

    }

    public int compareTo(final Object obj) {

        if (!(obj instanceof PlateExperimentUpdateBean)) {
            throw new ClassCastException("obj1 is not an PlateExperimentUpdateBean! ");
        }

        final Integer score1 = ((PlateExperimentUpdateBean) obj).groupScore;
        final Integer score0 = this.groupScore;

        return score1.compareTo(score0);
    }

    public PlateExperimentBean[][] getPlateExperimentLayout(final Holder holder) {

        final HolderType type = (HolderType) holder.getHolderType();

        final PlateExperimentBean[][] beanPlate =
            new PlateExperimentBean[type.getMaxRow()][type.getMaxColumn()];

        for (final PlateExperimentBean bean : this.experimentBeans) {

            if (null != bean.getHolder() && bean.getHolder().getHook().equals(holder.get_Hook())) {
                int row = bean.getRow();
                while (row >= type.getMaxRow()) {
                    row = row - type.getMaxRow();
                }
                int column = bean.getColumn();
                while (column >= type.getMaxColumn()) {
                    column = column - type.getMaxColumn();
                }

                beanPlate[row][column] = bean;
            }
        }
        return beanPlate;
    }

    private Holder getFirstPlate() {
        final Collection<Holder> holders = HolderFactory.getPlates(this.group);
        if (holders.iterator().hasNext()) {
            return holders.iterator().next();
        }
        return null;
    }

    public String[] getHolderRows() {
        final Holder holder = this.getFirstPlate();
        final int numRows = HolderFactory.getRows(holder).size();
        final String[] plateRows = new String[numRows];
        System.arraycopy(HolderFactory.ROWS, 0, plateRows, 0, numRows);
        return plateRows;
    }

    public String[] getHolderColumns() {
        final Holder holder = this.getFirstPlate();
        if (null == holder) {
            return new String[] {};
        }
        final int numColumns = HolderFactory.getColumns(holder).size();
        final String[] plateColumns = new String[numColumns];
        System.arraycopy(HolderFactory.COLUMNS, 0, plateColumns, 0, numColumns);
        return plateColumns;
    }

    public ModelObjectBean getNorthWestPlate() {
        return BeanFactory.newBean(this.NW_Plate);
    }

    public PlateExperimentBean[][] getNorthWestPlateLayout() {
        return this.getPlateExperimentLayout(this.NW_Plate);
    }

    public ModelObjectBean getNorthEastPlate() {
        return BeanFactory.newBean(this.NE_Plate);
    }

    public PlateExperimentBean[][] getNorthEastPlateLayout() {
        return this.getPlateExperimentLayout(this.NE_Plate);
    }

    public ModelObjectBean getSouthWestPlate() {
        return BeanFactory.newBean(this.SW_Plate);
    }

    public PlateExperimentBean[][] getSouthWestPlateLayout() {
        return this.getPlateExperimentLayout(this.SW_Plate);
    }

    public ModelObjectBean getSouthEastPlate() {
        return BeanFactory.newBean(this.SE_Plate);
    }

    public PlateExperimentBean[][] getSouthEastPlateLayout() {
        return this.getPlateExperimentLayout(this.SE_Plate);
    }

    public String getName() {
        return this.getExperimentGroup().getName();
    }

    public Collection<PlateExperimentBean> getExperimentBeans() {
        Collections.sort(this.experimentBeans);
        return this.experimentBeans;
    }

    private ExperimentGroup getExperimentGroup() {
        return this.group;
    }

    public ProtocolBean getProtocolBean() {
        return this.protocolBean;
    }

    //TODO is this used?
    public Map<String, String> getParameterMapObsolete() {
        return this.parameterMap;
    }

    //TODO is this used?
    public Map<String, String> getInputMapObsolete() {
        return this.inputsMap;
    }

    private static Map<String, String> getParametersForJSON(final Collection<Experiment> experiments) {

        final Map<String, String> parameterMap = new HashMap<String, String>();

        if (!experiments.isEmpty()) {
            final Experiment experiment = experiments.iterator().next();
            final Collection<Parameter> parameters = experiment.getParameters();
            //System.out.println("parameters [" + experiment.getName() + ":" + parameters.size() + "]");
            for (final Parameter parameter : parameters) {
                String value = parameter.getValue();
                if (null == parameter.getValue()) {
                    value = "";
                }
                parameterMap.put(parameter.getName(), value);
            }
        }

        for (final Experiment experiment : experiments) {
            final Collection<Parameter> parameters = experiment.getParameters();
            for (final Parameter parameter : parameters) {
                final String value = parameterMap.get(parameter.getName());
                if (null == parameter.getValue()) {
                    if ("".equals(value) || null == value) {
                        continue;
                    }
                } else if (parameter.getValue().equals(value)) {
                    continue;
                }
                parameterMap.remove(parameter.getName());
                parameterMap.put(parameter.getName(), "undefined");
            }

        }
        return parameterMap;
    }

    private static Map<String, String> getInputsForJSON(final Collection<Experiment> experiments) {

        final Map<String, String> inputMap = new HashMap<String, String>();

        if (!experiments.isEmpty()) {
            final Experiment experiment = experiments.iterator().next();
            final Collection<InputSample> inputSamples = experiment.getInputSamples();
            for (final InputSample inputSample : inputSamples) {
                final Sample sample = inputSample.getSample();
                if (null != sample) {
                    inputMap.put(inputSample.getName(), sample.getName());
                } else {
                    inputMap.put(inputSample.getName(), "");
                }
            }
        }

        for (final Experiment experiment : experiments) {
            //System.out.println("experiment [" + experiment.get_Name() + "]");
            final Collection<InputSample> inputSamples = experiment.getInputSamples();
            for (final InputSample inputSample : inputSamples) {
                final String value = inputMap.get(inputSample.getName());
                final Sample sample = inputSample.getSample();
                String newValue = "";
                if (null != sample) {
                    newValue = sample.getName();
                }
                //System.out.println("inputSample [" + inputSample.getName() + ":" + value + ":" + newValue
                //    + "]");
                if (null != value && value.equals(newValue)) {
                    continue;
                }
                inputMap.remove(inputSample.getName());
                inputMap.put(inputSample.getName(), "undefined");
            }

        }
        return inputMap;
    }

    /**
     * PlateExperimentUpdateBean.getGroupScore
     * 
     * @return
     */
    public int getGroupScore() {
        return this.groupScore;
    }

    /**
     * PlateExperimentUpdateBean.getMaxSubPositions
     * 
     * @return
     */
    public int getMaxSubPositions() {
        return this.maxSubPositions;
    }

}
