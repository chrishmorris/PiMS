package org.pimslims.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.JAXBException;

import org.jdom.JDOMException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.xml.sax.SAXException;

public class RefDataLoader {
    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     * @throws FileNotFoundException
     * @throws FileNotFoundException
     */
    private static AbstractModel model;

    public static void main(final String[] args) throws FileNotFoundException, AbortedException,
        ConstraintException, ClassNotFoundException {
        if (args.length < 2) {
            System.out.println("provide arguments in order: ");
            System.out
                .println(" 1) propertieFile: the file defined the DB connection info same as the one used in upgrader!");
            System.out.println(" 2) RefDirectory: a directory which contrains all ref-data!");
            return;
        }
        //AbstractLoader.silent = false;
        RefDataLoader.initModel(args[0]);
        final String filename = args[1];
        final WritableVersion wv =
            RefDataLoader.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            RefDataLoader.loadRefdata3_3(wv, filename);
            wv.commit();
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
        } catch (final ModelException ex) {
            System.out.println("Unable to add details from file: " + filename);
            ex.printStackTrace();
        } catch (final SQLException e) {
            System.out.println("Unable to add details from file: " + filename);
            e.printStackTrace();
        } catch (final JDOMException e) {
            System.out.println(filename + " is not well-formed.");
            e.printStackTrace();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * @param string
     * @throws FileNotFoundException
     */
    private static void initModel(final String propertyFileName) throws FileNotFoundException {

        //start from propertyFile if provided
        System.out.println("loading DB connection info from: " + propertyFileName);
        final File properties = new java.io.File(propertyFileName);
        if (!properties.exists()) {
            System.out.println("file does NOT exist:" + propertyFileName);
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        RefDataLoader.model = org.pimslims.dao.ModelImpl.getModel(properties);

    }

    /**
     * checking directory of ref-dtata is correct
     * 
     * @param string
     */
    private static void checkRefDirectory(final String refdataDirName) {
        System.out.println("loading ref-data from: " + refdataDirName);
        final File refdataDir = new java.io.File(refdataDirName);
        if (!refdataDir.exists()) {
            System.out.println("Directory does NOT exist:" + refdataDirName);
            throw new RuntimeException("Directory does NOT exist:" + refdataDirName);
        } else if (refdataDir.isFile()) {
            System.out.println("please give a directory NOT a file:" + refdataDirName);
            throw new RuntimeException("please give a directory NOT a file:" + refdataDirName);
        }

    }

    @Deprecated
    // now done with build.xml
    static void loadRefdata3_3(final WritableVersion wv, final String refdataDirName) throws AccessException,
        ConstraintException, SQLException, JDOMException, IOException, ClassNotFoundException, JAXBException,
        SAXException {

        final String dirName = refdataDirName + "//";
        RefDataLoader.checkRefDirectory(refdataDirName);
        String filename = null;

        // load new ref-data file next

        System.out.println("\nStart loading new ref-data into DB...");
        System.out.println("-Loading new Suppliers");
        filename = dirName + "Suppliers.csv";
        SupplierUtility.loadFile(wv, filename);

        System.out.println("-Loading new vectors");
        final String vectorDirName = dirName + "/xml/vectors/";
        org.pimslims.xmlio.VectorXmlLoader.loadDir(vectorDirName, dirName + "/xml", wv);

        System.out.println("-Loading new sample categories");
        filename = dirName + "SampleCats.csv";
        SampleCatsUtility.loadFile(wv, filename);

        System.out.println("-Update Chemicals");
        filename = dirName + "Chemicals.csv";

        System.out.println("-Loading new holder types");
        filename = dirName + "PlateTypes.csv";
        HolderTypeUtility.loadFile(wv, filename);

        System.out.println("-Loading new Protocols");
        final Collection<String> protocolFileNames = new LinkedList<String>();
        protocolFileNames.add("/protocols/SPOT_Construct_SDM_Primer_Design.xml");
        protocolFileNames.add("/protocols/Leeds_Expression_Construct.xml");
        protocolFileNames.add("/protocols/Leeds_EntryClone_Construct.xml");
        protocolFileNames.add("/Default-protocols/PiMS_Plasmid_stock.xml");
        protocolFileNames.add("/Default-protocols/PiMS_Cell_stock.xml");
        protocolFileNames.add("/Default-protocols/PiMS_Synthetic_gene.xml");
        protocolFileNames.add("protocols/SPOT_Primerless_Construct_Design.xml");
        protocolFileNames.add("protocols/Leeds_EntryClone_Construct.xml");
        protocolFileNames.add("protocols/Leeds_Expression_Construct.xml");
        for (final String protocolFileName : protocolFileNames) {
            filename = dirName + protocolFileName;
            //System.out.println(protocolFileName);
            try {
                final java.io.Reader reader2 = new java.io.FileReader(filename);
                new ProtocolUtility(wv).loadFile(reader2);
            } catch (final IOException e) {
                System.out.println(filename + " is ignored as can not be found/loaded!");
            }
        }
        wv.flush();

        System.out.println("\nFinish ref-data loading successfully!!!");

    }

    static void loadRefdata3_2(final WritableVersion wv, final String refdataDirName) throws AccessException,
        ConstraintException, SQLException, JDOMException, IOException, ClassNotFoundException, JAXBException,
        SAXException {

        final String dirName = refdataDirName + "//";
        RefDataLoader.checkRefDirectory(refdataDirName);
        String filename = null;

        // load new ref-data file next

        System.out.println("\nStart loading new ref-data into DB...");
        System.out.println("-Loading new Suppliers");
        filename = dirName + "Suppliers.csv";
        SupplierUtility.loadFile(wv, filename);

        System.out.println("-Loading new ExperimentTypes");
        filename = dirName + "ExperimentTypes.csv";
        final java.io.Reader reader = new java.io.FileReader(filename);
        ExperimentTypesUtility.loadFile(wv, reader);

        System.out.println("-Loading new NaturalSources");
        filename = dirName + "NaturalSources.csv";
        NaturalSourceUtility.loadFile(wv, new java.io.File(filename));

        System.out.println("-Loading new vectors");
        final String vectorDirName = dirName + "/xml/vectors/";
        org.pimslims.xmlio.VectorXmlLoader.loadDir(vectorDirName, dirName + "/xml", wv);

        System.out.println("-Loading new Extensions");
        filename = dirName + "/test/TestExtensionsPlus290309.csv";
        ExtensionsUtility.loadFile(wv, filename);

        System.out.println("-Update Chemicals");
        filename = dirName + "Chemicals.csv";
        // improve performance
        ((WritableVersionImpl) wv).setFlushMode(org.pimslims.dao.FlushMode.batchMode());
        ChemicalUtility.loadFile(wv, filename);

        wv.flush();

        System.out.println("\nFinish ref-data loading successfully!!!");

    }

    static void loadRefdata3_1(final WritableVersion wv, final String refdataDirName) throws AccessException,
        ConstraintException, SQLException, JDOMException, IOException, ClassNotFoundException {

        final String dirName = refdataDirName + "//";
        RefDataLoader.checkRefDirectory(refdataDirName);
        String filename = null;

        // load new ref-data file next

        System.out.println("\nStart loading new ref-data into DB...");

        System.out.println("-Loading new Protocols");
        final Collection<String> protocolFileNames = new LinkedList<String>();
        protocolFileNames.add("/protocols/PIMS_Plate_Layout_Forward_Primers.xml");
        protocolFileNames.add("/protocols/PIMS_Plate_Layout_Reverse_Primers.xml");
        protocolFileNames.add("/protocols/Order_Primer_Plate.xml");

        for (final String protocolFileName : protocolFileNames) {
            filename = dirName + protocolFileName;
            //System.out.println(protocolFileName);
            try {
                final java.io.Reader reader2 = new java.io.FileReader(filename);
                new ProtocolUtility(wv).loadFile(reader2);
            } catch (final IOException e) {
                System.out.println(filename + " is ignored as can not be found/loaded!");
            }
        }

        System.out.println("\nFinish ref-data loading successfully!!!");

    }

    static void loadRefdata200710(final WritableVersion wv, final String refdataDirName)
        throws AccessException, ConstraintException, SQLException, JDOMException, IOException,
        ClassNotFoundException {

        final String dirName = refdataDirName + "//";
        RefDataLoader.checkRefDirectory(refdataDirName);
        String filename = null;

        // load new ref-data file next
        System.out.println("\nStart loading new ref-data into DB...");

        System.out.println("-Loading new Suppliers");
        filename = dirName + "Suppliers.csv";
        SupplierUtility.loadFile(wv, filename);

        System.out.println("-Loading new ExperimentTypes");
        filename = dirName + "ExperimentTypes.csv";
        final java.io.Reader reader = new java.io.FileReader(filename);
        ExperimentTypesUtility.loadFile(wv, reader);

        System.out.println("-Createing new status/experimentType links");
        filename = dirName + "StatusToExperimentTypes.csv";
        StatusToExperimentTypesUtility.loadFile(wv, filename);

        System.out.println("-Loading new sample categories");
        filename = dirName + "SampleCats.csv";
        SampleCatsUtility.loadFile(wv, filename);

        System.out.println("-Loading new Protocols");
        final Collection<String> protocolFileNames = new LinkedList<String>();
        protocolFileNames.add("/protocols/PIMS_PCR.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_PlateCulture.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_VectorPrep.xml");
        protocolFileNames.add("/protocols/PIMS_TrialExpression.xml");
        protocolFileNames.add("/OPPF/protocols/OPPF_ScaleUp_Purification.xml");
        protocolFileNames.add("/OPPF/protocols/OPPF_ScaleUp_Concentration.xml");
        protocolFileNames.add("/OPPF/protocols/OPPF_Mass_Spec.xml");
        protocolFileNames.add("/OPPF/protocols/OPPF_Verification.xml");

        protocolFileNames.add("/YSBL/protocols/YSBL_Purification1.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_Purification2.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_Purification3.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_StandardPCR.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_Standard_PCR.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_CloneVerification.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_LIC_Cloning.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_LIC_polymerase.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_LIC_VectorPrep.xml");
        protocolFileNames.add("/YSBL/protocols/HiTel_TrialExpression.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_CloneDigest.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_CloneVerification.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_LIC_Cloning.xml");
        protocolFileNames.add("/YSBL/protocols/YSBL_TrialExpression.xml");

        protocolFileNames.add("/protocols/SSPF_Purification1.xml");
        protocolFileNames.add("/protocols/SPOT_Construct_Primer_Design.xml");
        protocolFileNames.add("/SSPF/protocols/PIMS_Hamilton_PCR.xml");

        for (final String protocolFileName : protocolFileNames) {
            filename = dirName + protocolFileName;
            //System.out.println(protocolFileName);
            try {
                new ProtocolUtility(wv).loadFile(new java.io.FileReader(filename));
            } catch (final IOException e) {
                System.out.println(filename + " is ignored as can not be found/loaded!");
            }
        }

        System.out.println("-Loading new Extensions");
        filename = dirName + "Extensions.csv";
        ExtensionsUtility.loadFile(wv, filename);

        System.out.println("\nFinish ref-data loading successfully!!!");

    }

/*    static void loadRefdataFromDir(WritableVersion wv, String refdataDirName) throws AccessException,
        ConstraintException, JDOMException, IOException, SQLException {

        String dirName = refdataDirName + "//";
        checkRefDirectory(refdataDirName);
        String filename = null;

        // load merge file first
        System.out.println("\nStart merging new ref-data with existing data...");
        System.out.println("-Mergeing ExperimentTypes");
        filename = dirName + "MergedExperimentTypes.csv";
        ExperimentTypesUtility.loadMergeFile(wv, filename);

        System.out.println("-Mergeing NaturalSources");
        filename = dirName + "MergedNaturalSources.csv";
        NaturalSourceUtility.loadMergeFile(wv, filename);

        System.out.println("-Mergeing Scoreboard");
        filename = dirName + "MergedScoreboard.csv";
        ScoreboardUtility.loadMergeFile(wv, filename);

        System.out.println("-Mergeing Status");
        filename = dirName + "MergedStatus.csv";
        StatusToScoreboardUtility.loadMergeFile(wv, filename);

        System.out.println("\nFinish merging successfully!!");

        // load new ref-data file next
        System.out.println("\nStart loading new ref-data into DB...");

        System.out.println("-Loading new ExperimentTypes");
        filename = dirName + "ExperimentTypes.csv";
        ExperimentTypesUtility.loadFile(wv, filename);

        System.out.println("-Loading new status & scoreboards");
        filename = dirName + "StatusToScoreboard.csv";
        StatusToScoreboardUtility.loadFile(wv, filename);

        System.out.println("-Createing new sstatus/experimentType links");
        filename = dirName + "StatusToExperimentTypes.csv";
        StatusToExperimentTypesUtility.loadFile(wv, filename);

        System.out.println("-Loading new NaturalSources");
        filename = dirName + "NaturalSources.csv";
        NaturalSourceUtility.loadFile(wv, filename);

        System.out.println("-Loading new target projects");
        filename = dirName + "Projects.csv";
        ProjectUtility.loadFile(wv, filename);

        System.out.println("-Loading new sample categories");
        filename = dirName + "SampleCats.csv";
        SampleCatsUtility.loadFile(wv, filename);

        System.out.println("-Loading new component categories");
        filename = dirName + "ComponentCats.csv";
        ComponentCatsUtility.loadFile(wv, filename);

        System.out.println("-Loading new Suppliers");
        filename = dirName + "Suppliers.csv";
        SupplierUtility.loadFile(wv, filename);

        System.out.println("-Loading new Reagents");
        filename = dirName + "Reagents.csv";
        ReagentUtility.loadFile(wv, filename);

        System.out.println("-Loading new HazardPhrases");
        filename = dirName + "HazardPhrases.xml";
        HazardPhraseUtility.loadFile(wv, filename);

        System.out.println("-Loading new DbNames");
        filename = dirName + "DbNames.csv";
        DbNameUtility.loadFile(wv, filename);

        System.out.println("-Loading new Chemicals");
        filename = dirName + "Chemicals.csv";
        ChemicalUtility.loadFile(wv, filename);

        System.out.println("-Loading new holder categories");
        filename = dirName + "HolderCats.csv";
        HolderCatsUtility.loadFile(wv, filename);

        System.out.println("-Loading new holder types");
        filename = dirName + "PlateTypes.csv";
        HolderTypeUtility.loadFile(wv, filename);

        System.out.println("-Loading new Protocols");
        Collection<String> protocolFileNames = new LinkedList<String>();
        protocolFileNames.add("/protocols/PIMS_Culture.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_annealing.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_PlateCulture.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_polymerase_reaction.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_Transformation.xml");
        protocolFileNames.add("/protocols/PIMS_LIC_VectorPrep.xml");
        protocolFileNames.add("/protocols/PIMS_miniprep.xml");
        protocolFileNames.add("/protocols/PIMS_PCR.xml");
        protocolFileNames.add("/protocols/PIMS_PlasmidPrep.xml");
        protocolFileNames.add("/protocols/PIMS_PlateCulture.xml");
        protocolFileNames.add("/protocols/PIMS_Transformation.xml");
        protocolFileNames.add("/protocols/PIMS_TrialExpression.xml");
        protocolFileNames.add("/protocols/PIMS_Unspecified.xml");
        protocolFileNames.add("/protocols/SPOT_Construct_Bioinformatics.xml");
        protocolFileNames.add("/protocols/SPOT_Construct_Primer_Design.xml");
        for (String protocolFileName : protocolFileNames) {
            filename = dirName + protocolFileName;
            System.out.println(protocolFileName);
            ProtocolUtility.loadFile(wv, filename);
        }

        System.out.println("\nFinish ref-data loading successfully!!!");

    }*/
}
