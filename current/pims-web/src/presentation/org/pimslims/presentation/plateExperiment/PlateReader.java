package org.pimslims.presentation.plateExperiment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.bioinf.local.SequenceGetter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.WellExperimentBean;

/**
 * PlateReader Makes beans to represent the details of a plate experiment. It can represent all rows and
 * columns. If the plate has multiple subpositions, only one is represented.
 */
public class PlateReader {

    private final Experiment firstExperiment;

    private final ExperimentGroup group;

    /**
     * subPosition Integer The subPosition of interest
     */
    private final Integer subPosition;

    /**
     * The number of subPositions in the plate
     */
    private final Integer maxPositions;

    public PlateReader(final ReadableVersion version, final Holder holder, final Integer subPosition) {
        this(version, HolderFactory.getExperimentGroup(holder), subPosition);
    }

    public PlateReader(final ReadableVersion version, final ExperimentGroup group, final Integer subPosition) {
        // could this.version = version;
        this.group = group;
        this.firstExperiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS);
        this.subPosition = subPosition;
        final HolderType type = PlateReader.getPlateHolderType(group);
        if (null == type || null == type.getMaxSubPosition()) {
            this.maxPositions = 1;
        } else {
            this.maxPositions = type.getMaxSubPosition();
        }
    }

    /**
     * PlateReader.getExperiments
     * 
     * @param order
     * @return
     * @throws AccessException
     */
    public List<WellExperimentBean> getExperiments(final Comparator order) throws AccessException {
        final List<WellExperimentBean> beans = new ArrayList<WellExperimentBean>(96);

        assert null != this.group : "No experiment group found";
        final Collection<Experiment> experiments = this.group.getExperiments();
        final Map<Experiment, ResearchObjective> ros =
            PlateExperimentDAO.getExperimentsAndResearchObjectives(this.group);

        //Protocol protocol = null;
        //if (null != this.firstExperiment) {
        //    protocol = this.firstExperiment.getProtocol();
        //}

        final Map<Experiment, Collection<Molecule>> trialMolecules =
            PlateExperimentDAO.getTrialMolecules(this.group);
        final Map<Experiment, Collection<Parameter>> parameters =
            PlateExperimentDAO.getAssociates(this.group, Parameter.class);
        final Map<Experiment, Collection<OutputSample>> oss =
            PlateExperimentDAO.getAssociates(this.group, OutputSample.class);
        final Map<Experiment, Collection<InputSample>> iss =
            PlateExperimentDAO.getAssociates(this.group, InputSample.class);
        for (final Experiment experiment : experiments) {
            final Collection<OutputSample> outputSamples = oss.get(experiment);
            final Sample outputSample = PlateReader.getOutputSample(outputSamples);
            if (null != this.subPosition && null != outputSample
                && !this.subPosition.equals(outputSample.getSubPosition())) {
                continue;
            }
            Holder myHolder = null;
            if (null != outputSample) {
                myHolder = outputSample.getHolder();
            }
            final ResearchObjective ro = ros.get(experiment);

            final WellExperimentBean bean =
                new WellExperimentBean(experiment, parameters.get(experiment), iss.get(experiment),
                    outputSamples, myHolder != null ? myHolder.getName() : null, myHolder != null
                        ? myHolder.get_Hook() : null, this.group != null ? this.group.getName() : null,
                    this.group != null ? this.group.get_Hook() : null, HolderFactory.getRow(outputSamples),
                    HolderFactory.getColumn(outputSamples), Collections.EMPTY_MAP, ro);

            if (null != trialMolecules.get(experiment)) {
                final int pcrprod = SequenceGetter.getPCRProductLength(trialMolecules.get(experiment));
                if (pcrprod != -1) {
                    bean.setPcrProductSize(pcrprod);
                }
                final float proteinMW = SequenceGetter.getProteinMW(trialMolecules.get(experiment));
                if (proteinMW != -1) {
                    bean.setProteinMW(proteinMW);
                }
            }
            beans.add(bean);

        }
        Collections.sort(beans, order);
        return beans;
    }

    public List<WellExperimentBean> getExperiments() throws AccessException {
        return this.getExperiments(new WellExperimentBean.ColumnOrder());
    }

    public Experiment getExperimentByPosition(final int row, final int column) {

        final Collection<Experiment> experiments = this.group.getExperiments();
        for (final Experiment experiment : experiments) {
            if (row == HolderFactory.getRow(experiment) && column == HolderFactory.getColumn(experiment)) {
                return experiment;
            }
        }
        return null;
    }

    public Experiment getExperimentByResearchObjective(final String construct) {

        if (null != construct) {
            final Collection<Experiment> experiments = this.group.getExperiments();
            for (final Experiment experiment : experiments) {
                final Project researchObjective = experiment.getProject();
                if (null != researchObjective && construct.equals(researchObjective.getName())) {
                    return experiment;
                }
            }
        }
        return null;
    }

    public List<Sample> getSamples() {
        final List<Sample> samples = new ArrayList<Sample>();
        final ExperimentGroup group = this.group;
        assert null != group : "No experiment group found";
        final Collection<Experiment> experiments = group.getExperiments();

        for (final Experiment experiment : experiments) {
            // don't filter by subPosition - this is used to make a select list for new inputs
            final Collection<InputSample> iss = experiment.getInputSamples();
            for (final InputSample is : iss) {
                if (null != is.getSample()) {
                    samples.add(is.getSample());
                }
            }
        }
        return samples;
    }

    public List<Annotation> getAnnotations() {
        final List<Annotation> annotations = new ArrayList<Annotation>();
        final ExperimentGroup group = this.group;
        assert null != group : "No experiment group found";

        for (final Annotation annotation : group.getAnnotations()) {
            annotations.add(annotation);
        }

        return annotations;
    }

    public ExperimentGroup getExperimentGroup() {
        return this.group;
    }

    public String getDetails() {
        return this.firstExperiment.getDetails();
    }

    public Calendar getStartDate() {
        return this.firstExperiment.getStartDate();
    }

    public Calendar getEndDate() {
        return this.firstExperiment.getEndDate();
    }

    public Boolean getIsActive() {
        final Collection<OutputSample> outputSamples = this.firstExperiment.getOutputSamples();
        if (!outputSamples.isEmpty()) {
            final OutputSample outputSample = outputSamples.iterator().next();
            final Sample sample = outputSample.getSample();
            if (null != sample) {
                return sample.getIsActive();
            }
        }
        return false;
    }

    /**
     * PlateReader.getPlatelayout
     * 
     * @param group
     * @return
     */
    public static int[][] getPlatelayout(final ExperimentGroup group) {

        final HolderType holderType = PlateReader.getPlateHolderType(group);
        if (null == holderType) {
            return null;
        }
        final Collection<Experiment> experiments = group.getExperiments();
        final int[][] layout = new int[holderType.getMaxRow() * 2][holderType.getMaxColumn() * 2];
        for (final Experiment experiment : experiments) {
            if (experiment.getOutputSamples().iterator().hasNext()) {
                final OutputSample outputSample = experiment.getOutputSamples().iterator().next();
                final Sample sample = outputSample.getSample();
                if (null != sample) {
                    layout[sample.getRowPosition() - 1][sample.getColPosition() - 1] = 1;
                }
            }
        }

        return layout;
    }

    /**
     * PlateReader.getPlateHolderType
     * 
     * @param group
     * @return
     */
    public static HolderType getPlateHolderType(final ExperimentGroup group) {

        final Collection<Experiment> experiments = group.getExperiments();
        if (experiments.isEmpty()) {
            return null;
        }

        HolderType holderType = null;
        if (experiments.iterator().hasNext()) {
            final Experiment experiment = experiments.iterator().next();
            if (experiment.getOutputSamples().iterator().hasNext()) {
                final OutputSample outputSample = experiment.getOutputSamples().iterator().next();
                final Sample sample = outputSample.getSample();
                if (null != sample) {
                    final Holder holder = sample.getHolder();
                    if (null != holder) {
                        holderType = (HolderType) holder.getHolderType();
                    }
                }
            }
        }

        return holderType;
    }

    /**
     * PlateReader.getPlateMinRow
     * 
     * @param plateLayout
     * @return
     */
    public static int getPlateMinRow(final int[][] plateLayout) {
        for (int i = 0; i < plateLayout.length; i++) {
            for (int j = 0; j < plateLayout[i].length; j++) {
                if (plateLayout[i][j] == 1) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * PlateReader.getPlateMaxRow
     * 
     * @param plateLayout
     * @return
     */
    public static int getPlateMaxRow(final int[][] plateLayout) {
        int max = 0;
        for (int i = 0; i < plateLayout.length; i++) {
            for (int j = 0; j < plateLayout[i].length; j++) {
                if (plateLayout[i][j] == 1) {
                    max = i;
                }
            }
        }
        return max;
    }

    /**
     * PlateReader.getPlateMinCol
     * 
     * @param plateLayout
     * @return
     */
    public static int getPlateMinCol(final int[][] plateLayout) {
        int min = 100;
        for (int i = 0; i < plateLayout.length; i++) {
            for (int j = 0; j < plateLayout[i].length; j++) {
                if (plateLayout[i][j] == 1) {
                    if (j < min) {
                        min = j;
                    }
                }
            }
        }
        return min;
    }

    /**
     * PlateReader.getPlateMaxCol
     * 
     * @param plateLayout
     * @return
     */
    public static int getPlateMaxCol(final int[][] plateLayout) {
        int max = 0;
        for (int i = 0; i < plateLayout.length; i++) {
            for (int j = 0; j < plateLayout[i].length; j++) {
                if (plateLayout[i][j] == 1) {
                    if (j > max) {
                        max = j;
                    }
                }
            }
        }
        return max;
    }

    /**
     * PlateReader.getSubPositions
     * 
     * @return
     */
    public Integer getMaxSubPosition() {
        if (null == this.maxPositions) {
            return 1;
        }
        return this.maxPositions;
    }

    private static Sample getOutputSample(final Collection<OutputSample> oss) {
        if (oss.isEmpty()) {
            return null;
        }
        for (final Iterator iter = oss.iterator(); iter.hasNext();) {
            final OutputSample os = (OutputSample) iter.next();
            if (null == os.getSample()) {
                continue;
            }
            return os.getSample();
        }
        return null; // can't tell
    }
}
