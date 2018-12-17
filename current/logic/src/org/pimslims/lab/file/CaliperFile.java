/**
 * pims-web org.pimslims.lab.file CaliperResultFile.java
 * 
 * @author Marc Savitsky
 * @date 12 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.JDOMException;
import org.pimslims.csv.CsvParser;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.properties.PropertyGetter;

/**
 * CaliperResultFile
 * 
 */
public abstract class CaliperFile implements IFileType {

    public static final String CALIPER_PROTOCOL = "Caliper Characterisation";

    private static final Collection<String> types = new HashSet<String>();
    static {
        CaliperFile.types.add("LM");
        CaliperFile.types.add("UM");
    }

    protected String inputString;

    protected String fileName;

    /**
     * PCR_PRODUCT String
     */
    public static final String PCR_PRODUCT = "PCR Product";

    protected CaliperFile() {
        // Empty constructor
    }

    public static boolean isCaliperFile(final String fileName) {
        final Matcher m = CaliperFile.FILE_CALIPER.matcher(fileName);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    static final Pattern FILE_CALIPER =
        Pattern
            .compile("^(Caliper_([a-zA-Z]+)_(.+)_(\\d{4}+-\\d{2}+-\\d{2}+)_(\\d{2}+-\\d{2}+-\\d{2}+))_PeakTable.csv$");

    /**
     * IFile.process
     * 
     * @see org.pimslims.lab.file.IFile#process(org.pimslims.util.File, org.pimslims.metamodel.ModelObject)
     */
    public final void processAsAttachment(final ModelObject object) {

        System.out.println("org.pimslims.lab.file.CaliperResultFile.processAsAttachment");
        if (object instanceof ExperimentGroup) {
            final ExperimentGroup group = (ExperimentGroup) object;

            try {
                for (final Experiment experiment : group.getExperiments()) {
                    this.setExperimentResult(experiment, this.inputString);
                }

            } catch (final IOException e) {
                throw new RuntimeException(e);
            } catch (final ConstraintException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public final ModelObject process(final WritableVersion version) throws IFileException,
        ConstraintException, UnsupportedEncodingException, IOException, AccessException {

        ExperimentGroup group = null;
        // First find the previous plate for holderType, access etc
        final ExperimentGroup fromGroup = CaliperFile.findFromExperimentGroup(version, this.getBarcode());

        version.setDefaultOwner(fromGroup.getAccess());
        // was version.setDefaultCreator(fromGroup.getCreator());

        final String plateName = this.getName();
        // Does this experimentGroup already exist?
        if (CaliperFile.plateAlreadyExist(version, plateName)) {
            throw new IFileException("plate already exists [" + plateName + "]");
        }

        // Find protocol
        final Protocol protocol = CaliperFile.findCaliperCharacterisationProtocol(version);
        final RefInputSample refInputSample = protocol.getRefInputSamples().iterator().next();

        HolderType holderType = null;
        final Collection<Holder> holders = HolderFactory.getPlates(fromGroup);
        if (holders.iterator().hasNext()) {
            final Holder holder = holders.iterator().next();
            holderType = (HolderType) holder.getHolderType();
        }

        final Collection<ExperimentType> experimentTypes =
            Collections.singleton(protocol.getExperimentType());

        // get basic details
        final LabNotebook access = fromGroup.getAccess();
        final Calendar startDate = Calendar.getInstance();
        final Calendar endDate = Calendar.getInstance();
        final String details = "";

        // Create experimentGroup
        final Collection<String> wells =
            CaliperFile.getWellsFromSpreadSheet(new InputStreamReader(CaliperFile
                .parseStringToIS(this.inputString)));

        if (fromGroup.getExperiments().size() != wells.size()) {
            throw new IFileException("number of experiment in from group ["
                + fromGroup.getExperiments().size() + "] doesn't match wells in spreadsheet [" + wells.size()
                + "]");
        }

        group =
            HolderFactory.doCreateExperimentGroup(version, access, plateName, "plate experiment", startDate,
                endDate, Collections.EMPTY_MAP);

        final Holder holder =
            HolderFactory.createHolder(version, access, plateName, holderType, Collections.EMPTY_MAP);

        HolderFactory.createPlateInGroup(version, access, group, holder, null, experimentTypes, details,
            protocol, Collections.EMPTY_MAP, wells, HolderFactory.getPositions(holderType),
            Collections.EMPTY_MAP);

        //final ExperimentGroupWriter gw = new ExperimentGroupWriter(wv, group);
        //gw.setValuesFromSpreadSheet(new InputStreamReader(inputStream));

        HolderFactory.multiLinePipette(version, fromGroup, group, refInputSample.getName(), null);

        version.setDefaultOwner(((LabBookEntry) group).getAccess());
        version.createFile(CaliperFile.parseStringToIS(this.inputString), plateName, group);

        this.processAsAttachment(group);
        return group;
    }

    /**
     * 
     * CaliperResultFile.setExperimentResult
     * 
     * @param experiment
     * @param inputString
     * @return
     * @throws ConstraintException
     * @throws IOException
     */

    private Experiment setExperimentResult(final Experiment experiment, final String inputString)
        throws ConstraintException, IOException {

        //Get Well from experiment
        final String well = HolderFactory.getPositionInHolder(experiment);
        //Get result parameter from experiment
        final Parameter score = this.getResultParameter(experiment.getParameters());
        if (null == score) {
            return null;
        }

        //Get result size [BP] for well from spreadsheet
        final int resultSize = CaliperFile.getResultforWell(well, CaliperFile.parseStringToIS(inputString));

        //get PCR product size from experiment
        final int pcrSize = this.getPCRProductSize(experiment);

        //set result
        score.setValue(CaliperFile.getResult(well, resultSize, pcrSize, score.getParameterDefinition()
            .getPossibleValues().toArray()));

        return experiment;
    }

    /**
     * 
     * CaliperResultFile.getResultParameter
     * 
     * @param parameters
     * @return
     */
    protected final Parameter getResultParameter(final Collection<Parameter> parameters) {
        for (final Parameter parameter : parameters) {
            if (parameter.getName().startsWith("__")) {
                return parameter;
            }
        }
        return null;
    }

    /**
     * 
     * CaliperResultFile.getResultforWell
     * 
     * @param well
     * @param input
     * @return
     * @throws IOException
     */
    protected static int getResultforWell(final String well, final InputStream inputStream)
        throws IOException {

        System.out.println("CaliperFile.getResultforWell [" + well + "]");
        final CsvParser parser = new CsvParser(new InputStreamReader(inputStream));

        float result = 0;
        float bestConc = 0;

        //in case well is A01
        final String col = well.substring(0, 1);
        final String row = well.substring(1);
        final String myWell = col + new Integer(row).intValue();

        while (parser.getLine() != null) {
            final String sizeString = parser.getValueByLabel("Size [BP]");
            if (null != sizeString && !(0 < sizeString.length())) {
                //System.out.println("CaliperResultFile.getResultforWell ignore [" + sizeString + "]");
                continue;
            }
            final float size = new Float(parser.getValueByLabel("Size [BP]")).floatValue();
            if (size < 100) { // ignore peaks of less than 100 base pairs
                continue;
            }
            final String sampleName = parser.getValueByLabel("Sample Name");
            final float conc = new Float(parser.getValueByLabel("Conc. (ng/ul)")).floatValue();
            //final String purity = parser.getValueByLabel("% Purity");
            final String type = parser.getValueByLabel("Type");

            if (myWell.equals(sampleName) && !CaliperFile.types.contains(type)) {
                final float thisConc = conc;
                if (thisConc > bestConc) {
                    bestConc = thisConc;
                    result = size;
                }
            }
        }

        return new Float(result).intValue();
    }

    /**
     * 
     * CaliperResultFile.getPCRProductSize Overridden in CaliperVerificationFile, but used by other subclasses
     * 
     * @param experiment
     * @return
     */
    protected int getPCRProductSize(final Experiment experiment) {

        System.out.println("CaliperFile.getPCRProductSize [" + experiment.get_Name() + "]");
        final ResearchObjective researchObjective = (ResearchObjective) experiment.getProject();
        final ResearchObjectiveElement element = CaliperFile.getResearchObjectiveElement(researchObjective);

        if (null != element) {
            for (final Molecule bc : element.getTrialMolComponents()) {
                for (final ComponentCategory category : bc.getCategories()) {
                    if (category.getName().equals(CaliperFile.PCR_PRODUCT)) {
                        return bc.getSequence().length();
                    }
                }
            }
        }

        return 0;
    }

    /**
     * 
     * CaliperResultFile.getResult
     * 
     * @param resultSize
     * @param pcrSize
     * @param values
     * @return
     */
    protected static String getResult(final String well, final int resultSize, final int pcrSize,
        final Object[] values) {

        final StringBuffer sb = new StringBuffer((String) values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append("," + (String) values[i]);
        }
        System.out.println("CaliperFile.getResult [" + sb.toString() + ":" + well + ":" + resultSize + ":"
            + pcrSize + ":" + "]");

        String result = (String) values[1];

        final int resultMaybe = PropertyGetter.getIntProperty("Caliper.Result.Maybe", 10);
        final int resultYes = PropertyGetter.getIntProperty("Caliper.Result.Yes", 20);

        if (values.length == 4) {
            double d = pcrSize * Math.log10(pcrSize) / resultMaybe;
            System.out.println("CaliperFile.getResult [" + (String) values[2] + ":" + pcrSize + "+-" + d
                + "]");
            if (resultSize > (pcrSize - d) && resultSize < (pcrSize + d)) {
                result = (String) values[2];
            }
            d = pcrSize * Math.log10(pcrSize) / resultYes;
            System.out.println("CaliperFile.getResult [" + (String) values[3] + ":" + pcrSize + "+-" + d
                + "]");
            if (resultSize > (pcrSize - d) && resultSize < (pcrSize + d)) {
                result = (String) values[3];
            }
        }
        return result;
    }

    /**
     * 
     * CaliperResultFile.getWellsFromSpreadSheet
     * 
     * @param reader
     * @return
     * @throws IOException
     * @throws ConstraintException
     */
    public static Collection<String> getWellsFromSpreadSheet(final Reader reader) throws IOException,
        ConstraintException {

        System.out.println("CaliperResultFile.getWellsFromSpreadSheet");
        final CsvParser parser = new CsvParser(reader);
        final List<String> labels = new ArrayList<String>(Arrays.asList(parser.getLabels()));
        final Collection<String> wells = new HashSet<String>();

        // process standard column headers
        final String wellName = "Sample Name";
        if (labels.contains(wellName)) {
            labels.remove(wellName);
        } else {
            throw new IllegalArgumentException("CSV file must contain 'Sample Name' column");
            // LATER accept "row" and "column"
        }

        // now iterate through the file
        while (parser.getLine() != null) {
            final String well = parser.getValueByLabel(wellName);
            if (well.equals("Ladder1") || well.equals("Ladder2") || well.equals("Ladder3")
                || well.equals("Ladder4")) {
                continue;
            }
            wells.add(HolderFactory.getPositionForName(well));
        }

        return wells;
    }

    /**
     * 
     * CaliperResultFile.getBarcode
     * 
     * @param name
     * @return
     */
    public final String getBarcode() {
        return CaliperFile.getBarcode(this.fileName);
    }

    protected static String getBarcode(final String name) {
        //System.out.println("getBarcode [" + name + "]");
        final Matcher m = CaliperFile.FILE_CALIPER.matcher(name);
        if (m.matches()) {
            return m.group(3);
        }
        return null;
    }

    /**
     * 
     * CaliperResultFile.getName
     * 
     * @param name
     * @return
     */
    public final String getName() {
        final Matcher m = CaliperFile.FILE_CALIPER.matcher(this.fileName);
        if (m.matches()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * 
     * CaliperResultFile.findFromExperimentGroup
     * 
     * @param wv
     * @param name
     * @return
     * @throws Exception
     */
    public static ExperimentGroup findFromExperimentGroup(final ReadableVersion wv, final String barcode)
        throws IFileException {

        ExperimentGroup group = null;

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(ExperimentGroup.PROP_NAME, barcode);
        group = wv.findFirst(ExperimentGroup.class, m);

        if (null == group) {
            throw new IFileException("CaliperImport.process from group not found [" + barcode + "]");
        }

        return group;
    }

    /**
     * 
     * ConstructBeanReader.getResearchObjectiveElement
     * 
     * @param expBlueprint
     * @return
     */
    public static ResearchObjectiveElement getResearchObjectiveElement(final ResearchObjective expBlueprint) {
        if (null == expBlueprint) {
            return null;
        }
        final Collection<ResearchObjectiveElement> c = expBlueprint.getResearchObjectiveElements();
        assert 1 == c.size();
        return c.iterator().next();
    }

    public static boolean plateAlreadyExist(final WritableVersion version, final String plateName) {
        if (version.findFirst(Holder.class, AbstractHolder.PROP_NAME, plateName) != null) {
            return true;
        }
        return false;
    }

    public static java.io.InputStream parseStringToIS(final String string)
        throws UnsupportedEncodingException {

        if (string == null) {
            return null;
        }
        return new java.io.ByteArrayInputStream(string.trim().getBytes("UTF-8"));
    }

    /**
     * 
     * CaliperResultFile.findCaliperCharacterisationProtocol
     * 
     * @param wv
     * @return
     */
    public static Protocol findCaliperCharacterisationProtocol(final WritableVersion wv)
        throws IFileException {

        Protocol protocol = null;

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(Protocol.PROP_NAME, CaliperFile.CALIPER_PROTOCOL);
        protocol = wv.findFirst(Protocol.class, m);

        if (null == protocol) {
            throw new IFileException("Caliper Characterisation protocol not found");
        }

        final Collection<RefOutputSample> oss = protocol.getRefOutputSamples();
        if (1 != oss.size()) {
            throw new AssertionError(
                "The protocol for a plate experiment must have exactly one output sample");
        }

        final SampleCategory output = oss.iterator().next().getSampleCategory();
        if (null == output) {
            throw new AssertionError(
                "The protocol's output sample for a plate experiment must have a Sample Category");
        }

        final Collection<RefInputSample> iss = protocol.getRefInputSamples();
        if (1 != iss.size()) {
            throw new AssertionError("The protocol for a plate experiment must have exactly one input sample");
        }
        assert null != protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME,
            "__SCORE");

        return protocol;
    }

    /**
     * IFileType.getTypeName
     * 
     * @see org.pimslims.lab.file.IFileType#getTypeName()
     */
    public abstract String getTypeName();

    public abstract boolean isOfThisType(ReadableVersion version, String fileName) throws IFileException;

    public abstract IFileType getInstance(final String name, final InputStream inputStream)
        throws JDOMException, IOException;

}
