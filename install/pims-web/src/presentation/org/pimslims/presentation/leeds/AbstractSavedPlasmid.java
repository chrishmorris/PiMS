/**
 * 
 */
package org.pimslims.presentation.leeds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Creator;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.servlet.utils.ValueFormatter;
import org.pimslims.util.File;

/**
 * @author Petr Troshin
 * 
 */
@Deprecated
// Leeds functionality is not longer supported
public class AbstractSavedPlasmid implements FormFieldsNames, Comparable<AbstractSavedPlasmid> {

    Experiment experiment;

    public AbstractSavedPlasmid(final Experiment experiment) {
        this.experiment = experiment;
    }

    public String getType() {
        return this.experiment.getExperimentType().getName();
    }

    /**
     * @return the Holder for sample 1
     */

    public String getBox1() {
        final Sample sample = this.getOutSample(1);
        if (sample == null) {
            return "";
        }
        final Holder holder = sample.getHolder();
        return holder.get_Hook();
    }

    public String getBox1Barcode() {
        final Sample sample = this.getOutSample(1);
        if (sample == null) {
            return "";
        }
        final Holder box = sample.getHolder();
        if (box != null) {
            return box.getDetails();
        }
        return null;
    }

    public String getPosition1Barcode() {
        final Sample sample = this.getOutSample(1);
        if (sample == null) {
            return "";
        }

        return sample.getBatchNum();
    }

    public void setPosition1Barcode(final String barcode) throws ConstraintException {
        final Sample sample = this.getOutSample(1);
        if (sample != null && !Util.isEmpty(barcode)) {
            sample.setBatchNum(barcode);
        }
    }

    public void setBox1Barcode(final String barcode) throws ConstraintException {
        final Sample sample = this.getOutSample(1);
        if (sample == null) {
            return;
        }

        final Holder box = sample.getHolder();
        if (box != null && !Util.isEmpty(barcode)) {
            box.setDetails(barcode.trim());
        }
    }

    /**
     * @return the Holder for sample 2
     */

    public String getBox2() {
        final Sample sample = this.getOutSample(2);
        if (sample == null) {
            return "";
        }
        final Holder holder = sample.getHolder();
        return holder.get_Hook();
    }

    /**
     * @return the Clone saver name
     */

    public String getComments() {
        return this.experiment.getDetails();
    }

    /**
     * @return the designedBy
     */

    public String getDesignedBy() {
        final User creator = this.experiment.getOperator();
        if (creator == null) {
            return null;
        }
        return creator.get_Hook();
    }

    public String getDate() {
        return AbstractSavedPlasmid.getDate(this.experiment.getStartDate());
    }

    public Calendar getDateasTime() {
        return this.experiment.getStartDate();
    }

    public String getHook() {
        return this.experiment.get_Hook();
    }

    protected Sample getInputSample(final String name) {
        final InputSample is =
            this.experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME, name);
        if (null == is) {
            return null;
        }
        return is.getSample();
    }

    /*
     * 1- return forward primer sample 2- return reverse primer sample 3- return vector sample
    
    private Sample getInSample(final int sampleNum) {
        final Collection<InputSample> samples = this.experiment.getInputSamples();
        if (samples.size() > 3) {
            throw new RuntimeException("There should be less than 3 samples! Wrong type of experiment?");
        }
        // InputSample[] inSamples = samples.toArray(new InputSample[2]);
        // No order here!! Have to find out now which one is the first and the
        // second sample
        // First sample ends with 1, second with (2)
        Sample fprimerSample = null;
        Sample rprimerSample = null;
        Sample derivedPlasmidSample = null;
        for (final InputSample inSample : samples) {
            final Sample sample = inSample.getSample();
            if (null == sample) {
                continue;
            }
            final Collection<SampleCategory> scategories = sample.getSampleCategories();

            assert scategories != null : "Input sample categories wrong. Cannot proceed";
            for (final SampleCategory scat : scategories) {
                if (scat.getName().equalsIgnoreCase(FormFieldsNames.FPRIMER)) {
                    fprimerSample = sample;
                }
                if (scat.getName().equalsIgnoreCase(FormFieldsNames.RPRIMER)) {
                    rprimerSample = sample;
                }
                if (scat.getName().equalsIgnoreCase(FormFieldsNames.VECTOR)) {
                    derivedPlasmidSample = sample;
                }
                // for legacy data
                if (null == derivedPlasmidSample && scat.getName().equalsIgnoreCase("Plasmid")) {
                    derivedPlasmidSample = sample;
                }
            }
        }
        switch (sampleNum) {
            case 1:
                return fprimerSample;
            case 2:
                return rprimerSample;
            case 3:
                return derivedPlasmidSample;
            default:
                // cannot find the sample
                return null;
        }
    } */

    public Collection<Sample> getStrainList() {
        final ReadableVersion rv = this.experiment.get_Version();
        // COULD: this should be sample category

        final Collection<AbstractSample> sampleList = new HashSet<AbstractSample>();
        SampleCategory category =
            rv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, FormFieldsNames.strain);
        if (category != null) {
            sampleList.addAll(category.getAbstractSamples());
        }

        category =
            rv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, FormFieldsNames.competentCells);
        if (category != null) {
            sampleList.addAll(category.getAbstractSamples());
        }

        final Collection<Sample> ret = new HashSet<Sample>();
        for (final Iterator iterator = sampleList.iterator(); iterator.hasNext();) {
            final AbstractSample sample = (AbstractSample) iterator.next();
            if (sample instanceof Sample) {
                ret.add((Sample) sample);
            }
        }
        return ret;
    }

    // called from JSP
    public Collection<SavedPlasmid> getEntryClones() {
        final ReadableVersion rv = this.experiment.get_Version();
        final Collection<Experiment> exps = SavedPlasmid.getEntryCloneExperiments(rv);
        return SavedPlasmid.getPlasmids(exps);
    }

    // called from JSP
    public Collection<SavedPlasmid> getExpressionConstructs() {
        final ReadableVersion rv = this.experiment.get_Version();
        final Collection<Experiment> exps = SavedPlasmid.getExpressionConstructExperiments(rv);
        return SavedPlasmid.getPlasmids(exps);
    }

    // First sample is used to link with Deep frozen cultures
    public Sample getLinkingSample() {
        return this.getOutSample(1);
    }

    /**
     * @return the location1
     */

    public String getLocation1() {
        final Sample sample = this.getOutSample(1);
        if (sample == null) {
            return "";
        }
        final Holder sampleHolder = sample.getHolder();
        if (sampleHolder == null) {
            return "";
        }
        final Location location = ContainerUtility.getCurrentLocation(sampleHolder);
        if (location == null) {
            return "";
        }
        return location.get_Hook();
    }

    /**
     * @return the location2
     */

    public String getLocation2() {
        final Sample sample = this.getOutSample(2);
        if (sample == null) {
            return null;
        }
        final Holder sampleHolder = sample.getHolder();
        if (sampleHolder == null) {
            return null;
        }
        final Location location = ContainerUtility.getCurrentLocation(sampleHolder);
        assert location != null : "Current location is null";
        return location.get_Hook();

    }

    public String getName() {
        return this.experiment.getName();
    }

    Sample getOutSample(final int sampleNum) {
        final Set<OutputSample> samples = this.experiment.getOutputSamples();
        if (samples.size() < 1) {
            throw new RuntimeException("There should be two samples! Wrong type of experiment?");
        }
        final OutputSample[] outSamples = samples.toArray(new OutputSample[samples.size()]);
        // No order here!! Have to find out now which one is the first and the
        // second sample
        // First sample ends with 1, second with (2)
        Sample sample1 = null;
        Sample sample2 = null;
        Sample sampleCloneSaver = null;
        for (final OutputSample outSample : outSamples) {
            final Sample sample = outSample.getSample();
            assert sample != null;
            if (sample.getName().endsWith("(1)")) {
                sample1 = sample;
            }
            if (sample.getName().endsWith("(2)")) {
                sample2 = sample;
            }
            if (sample.getName().endsWith("(3)")) {
                sampleCloneSaver = sample;
            }
        }
        switch (sampleNum) {
            case 1:
                return sample1;
            case 2:
                return sample2;
            case 3:
                return sampleCloneSaver;
            default:
                // cannot find the sample
                return null;
        }
    }

    Parameter getParameter(final String paramCategory) {
        final Set<Parameter> paramaters = this.experiment.getParameters();
        for (final Parameter par : paramaters) {
            final String mname = par.getName();
            if (mname != null && mname.equals(paramCategory)) {
                return par;
            }
        }
        return null;
    }

    public String getPosition1() {
        return PrimerBeanReader.getPosition(this.getOutSample(1));
    }

    public String getPosition2() {
        return PrimerBeanReader.getPosition(this.getOutSample(2));
    }

    public File getSequence() {
        return this.getSpecificFiles("Sequence");
    }

    File getSpecificFiles(final String description) {
        final Collection<File> files = this.experiment.get_Files();
        for (final File file : files) {
            if (description.equals(file.getLegend())) {
                return file;
            }
        }
        return null;
    }

    /**
     * Only the first target will be returned by this method! Not suitable for complexes!
     * 
     * @return
     */
    public Target getTarget() {
        final ResearchObjective eblue = (ResearchObjective) this.experiment.getProject();
        if (eblue == null) {
            return null;
        }
        return eblue.getResearchObjectiveElements().iterator().next().getTarget();
    }

    public ReadableVersion getVersion() {
        return this.experiment.get_Version();
    }

    public File getVNTIMap() {
        return this.getSpecificFiles("VNTIMap");
    }

    public void setBox1(final String box1) throws ConstraintException {
        assert Util.isHookValid(box1) : "Hook " + box1 + " for box 1 seems to be invalid";
        final Sample sample = this.getOutSample(1);
        final Holder newHolder = sample.get_Version().get(box1);
        sample.setHolder(newHolder);
    }

    public void setBox2(final String box2) throws ConstraintException, AccessException {

        final Sample sample = this.getOutSample(2);
        Holder newHolder = null;
        final WritableVersion rw = (WritableVersion) sample.get_Version();
        if (Util.isHookValid(box2)) {
            newHolder = rw.get(box2);
        } else {
            newHolder = Creator.recordHolder(rw, FormFieldsNames.boxHolderType, box2, null);
        }
        sample.setHolder(newHolder);
    }

    /**
     * @param comments the comments to set
     * @throws ConstraintException
     */

    public void setComments(final String comments) throws ConstraintException {
        this.experiment.setDetails(comments);
    }

    /**
     * @param date the date to set
     * @throws ConstraintException
     */
    public void setDate(final Date date) throws ConstraintException {
        this.experiment.setStartDate(PrimerForm.getCalendar(date));
    }

    /**
     * @param designedBy the designedBy to set
     * @throws ConstraintException
     * @throws AccessException NOTE!! - this should not be used because it is unlikely to have desired effect.
     *             This may change the author for the number of experiments!
     */

    public void setDesignedBy(final String userHook) throws ConstraintException {
        final User designer = this.experiment.get_Version().get(userHook);
        assert designer != null : "Cannot change author of the experiment bacause user hook " + userHook
            + " is not correct or no user found";
        this.experiment.setOperator(designer);
    }

    void setLocation(final int locationNum, final String locationHook) throws ConstraintException,
        AccessException {
        final ReadableVersion version = this.experiment.get_Version();
        final Sample sample = this.getOutSample(locationNum);
        final Holder sampleHolder = sample.getHolder();
        Location newLoc = null;
        final WritableVersion rw = (WritableVersion) sample.get_Version();
        if (Util.isHookValid(locationHook)) {
            newLoc = version.get(locationHook);
        } else {
            newLoc = Creator.recordLocation(rw, locationHook);
            ContainerUtility.move(sampleHolder, newLoc);
        }

        final Collection hlocs = sampleHolder.getHolderLocations();
        // TODO Create HolderLocation if there was not one
        assert hlocs != null : "No HolderLocation was created, cannot update location " + locationHook;

        ContainerUtility.move(sampleHolder, newLoc);
/*
        System.out.println("Location updated " + newLoc.getName());

        for (HolderLocation l : sample.getHolder().getHolderLocations()) {
            System.out.println("locations " + l.getLocation().getName());
        }*/

    }

    public void setLocation1(final String location1) throws ConstraintException, AccessException {
        this.setLocation(1, location1);
    }

    public void setLocation2(final String location2) throws ConstraintException, AccessException {
        this.setLocation(2, location2);
    }

    /**
     * @param name the name to set
     */

    public void setName(final String name) throws ConstraintException {
        this.experiment.setName(name);
        LeedsConstructBean.setTarget((WritableVersion) this.experiment.get_Version(), this.experiment, name);
    }

    void setPosition(final int locationNum, String position) throws ConstraintException {
        final Sample sample = this.getOutSample(locationNum);
        if (sample == null) {
            return;
        }
        if (Util.isEmpty(position)) {
            final Integer unknown = null;
            sample.setColPosition(unknown);
            sample.setRowPosition(unknown);
        } else {
            position = position.toUpperCase();
            final int cpos = HolderFactory.getColumn(position);
            final int rpos = HolderFactory.getRow(position);

            sample.setColPosition(cpos);
            sample.setRowPosition(rpos);
        }
    }

    public void setPosition1(final String position1) throws ConstraintException {
        this.setPosition(1, position1);
    }

    public void setPosition2(final String position2) throws ConstraintException {
        this.setPosition(2, position2);
    }

    /*
     * Cannot change: Box Primers Derived from
     */
    public void updatePlasmid(final AbstractConstructBean leedsPlasmid) throws ConstraintException,
        AccessException {
        Sample sample = this.getOutSample(1);
        Holder sampleHolder = sample.getHolder();
        if (sampleHolder == null || !sampleHolder.get_Hook().equals(leedsPlasmid.box1)) {
            this.setBox1(leedsPlasmid.box1);
        }
        if (!Util.isEmpty(leedsPlasmid.position1Barcode)) {
            sample.setBatchNum(leedsPlasmid.position1Barcode);
        }
        Location location = ContainerUtility.getCurrentLocation(sampleHolder);
        if (sampleHolder == null || location == null || !location.get_Hook().equals(leedsPlasmid.location1)) {
            this.setLocation1(leedsPlasmid.location1);
        }
        if (!this.getPosition1().equals(leedsPlasmid.position1)) {
            this.setPosition1(leedsPlasmid.position1);
        }

        if (!Util.isEmpty(leedsPlasmid.location2) && !Util.isEmpty(leedsPlasmid.box2)
            && !Util.isEmpty(leedsPlasmid.position2)) {

            sample = this.getOutSample(2);
            // Sample 2 which is connected to the Location 2 may not have been
            // created
            final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
            if (sample == null) {
                sample =
                    DeepFrozenCultureBean.recordSample(rw, FormFieldsNames.RECOMBINANT_PLASMID,
                        this.getName() + " (2)");
                //final ModelObject outputSample2 =
                Creator.recordOutputSamples(rw, sample, this.experiment, "sample2");
            }

            sampleHolder = sample.getHolder();
            if (sampleHolder == null || !sampleHolder.get_Hook().equals(leedsPlasmid.box2)) {
                this.setBox2(leedsPlasmid.box2);
            }
            // Need to get hoder again as it may be created in the if clause
            // above
            sampleHolder = sample.getHolder();
            location = ContainerUtility.getCurrentLocation(sampleHolder);
            if (location == null || !location.get_Hook().equals(leedsPlasmid.location2)) {
                this.setLocation2(leedsPlasmid.location2);
            }

            if (Util.isEmpty(this.getPosition2()) || !this.getPosition2().equals(leedsPlasmid.position2)) {
                this.setPosition2(leedsPlasmid.position2);
            }
        }

        if (leedsPlasmid.comments != null && !this.getComments().equals(leedsPlasmid.comments)) {
            this.setComments(leedsPlasmid.comments);
        }
        if (!this.getDate().equals(leedsPlasmid.date)) {
            this.setDate(leedsPlasmid.date);
        }
        final User designer = this.experiment.getOperator();
        if (designer == null || !designer.get_Hook().equals(leedsPlasmid.designedBy)) {
            this.setDesignedBy(leedsPlasmid.designedBy);
        }
        if (!this.getName().equals(leedsPlasmid.name)) {
            this.setName(leedsPlasmid.name);
        }

    }

    public static Collection<AbstractSavedPlasmid> getLeedsConstruct(final Target target) {
        final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
        final List<AbstractSavedPlasmid> leedsPlasmids = new ArrayList<AbstractSavedPlasmid>();
        for (final ResearchObjectiveElement bc : bcs) {
            final ResearchObjective expBlue = bc.getResearchObjective();
            if (expBlue == null) {
                continue;
            }
            final Set<Experiment> exps = expBlue.getExperiments();
            if (exps == null || exps.isEmpty()) {
                continue;
            }
            final Collection<AbstractSavedPlasmid> oneBluePrintPlasmids =
                AbstractSavedPlasmid.getLeedsPlasmids(exps);
            if (oneBluePrintPlasmids == null) {
                continue;
            }
            leedsPlasmids.addAll(oneBluePrintPlasmids);
        }
        Collections.sort(leedsPlasmids);
        return leedsPlasmids;
    }

    public static Collection<AbstractSavedPlasmid> getLeedsPlasmids(final Set<Experiment> experiments) {
        final List<AbstractSavedPlasmid> plasmids = new ArrayList<AbstractSavedPlasmid>(experiments.size());
        for (final Experiment exp : experiments) {
            final ExperimentType exptype = exp.getExperimentType();
            if (exptype == null) {
                continue;
            }
            if (SavedPlasmid.isValidEntryClone(exp) || SavedPlasmid.isValidExpressionConstruct(exp)) {
                plasmids.add(new AbstractSavedPlasmid(exp));
            }

        }
        return plasmids;
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final AbstractSavedPlasmid o) {
        return o.getType().compareTo(this.getType());
    }

    /**
     * Write an input field for dates
     * 
     * @param date the current value
     * @web function
     */
    public static String getDate(Calendar date) {
        if (date == null) {
            date = Calendar.getInstance();
        }
        return ValueFormatter.formatDate(date);
    }

}
