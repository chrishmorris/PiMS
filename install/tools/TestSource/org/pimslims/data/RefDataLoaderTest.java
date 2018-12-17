/**
 * current-pims-web org.pimslims.command RefDataLoaderTest.java
 * 
 * @author bl67
 * @date 4 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.jdom.JDOMException;
import org.pimslims.AllToolsTests;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.test.AbstractTestCase;

/**
 * RefDataLoaderTest
 * 
 */
public class RefDataLoaderTest extends AbstractTestCase {
    /**
     * 
     */
    private static final String PURIFIED_PROTEIN_INIT_CAPS = "Purified Protein";

    /**
     * 
     */
    private static final String PURIFIED_PROTEIN = "Purified protein";

    /**
     * 
     */
    private static final String MICROLITRES = "uL";

    //private static final String UNIQUE = "rdl" + System.currentTimeMillis();

    private static final String ENCTAG = "HHHHHH";

    private static final String RSITE = "EcoRI";

    private static final String UNIQUE = "rdl" + System.currentTimeMillis();

    public void testCreateExtension() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final ComponentCategory category =
                new ComponentCategory(this.wv, "cc" + RefDataLoaderTest.UNIQUE);
            this.wv.flush();
            final String name = "ext" + RefDataLoaderTest.UNIQUE;
            final String dir = "forward";
            ExtensionsUtility.create(this.wv, name, "CATG", category.getName(), dir,
                RefDataLoaderTest.ENCTAG, RefDataLoaderTest.RSITE);
        } finally {
            this.wv.abort();
        }
    }

/*
    //TODO change this - old extensions are instances of Molecule, but must be Extensions.
    public void testCreateDuplicateExtension() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final ComponentCategory category =
                new ComponentCategory(this.wv, "cc" + RefDataLoaderTest.UNIQUE);
            this.wv.flush();
            final String name = "ext" + RefDataLoaderTest.UNIQUE;
            final String dir = "forward";
            final Extension extension = new Extension(this.wv, "DNA", name);
            ExtensionsUtility.create(this.wv, name, "CATG", category.getName(), dir,
                RefDataLoaderTest.ENCTAG, RefDataLoaderTest.RSITE);
            Assert.assertEquals("CATG", extension.getSeqString());
        } finally {
            this.wv.abort();
        }
    }
*/
    //this is the test for refdata-modifs 20071003 and 20071004
    public void xtestLoaderFor200710() throws AccessException, ConstraintException, IOException,
        SQLException, JDOMException, ClassNotFoundException {
        this.wv = this.getWV();
        try {
            RefDataLoader.loadRefdata200710(this.wv, AllToolsTests.TestingDataPath);
            //new Organisation added
            Assert.assertNotNull("new Organisation added", this.wv.findFirst(Organisation.class,
                Organisation.PROP_NAME, "Operon Biotechnologies GmbH"));
            Assert.assertNotNull("new Organisation added", this.wv.findFirst(Organisation.class,
                Organisation.PROP_NAME, "MWG Biotech"));

            //replace exp type: Protein production with Protein production summary
            Assert.assertNull("Protein production should be removed", this.wv.findFirst(ExperimentType.class,
                ExperimentType.PROP_NAME, "Protein production"));
            final ExperimentType et =
                this.wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME,
                    "Protein production summary");
            Assert.assertNotNull("Protein production summary should be added", et);
            Assert.assertTrue(et.getWorkflowItems().size() > 0);
            Assert.assertEquals("Purified", et.getWorkflowItems().iterator().next().getStatus().getName());

            //update SampleCats
            final SampleCategory sc =
                this.wv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                    RefDataLoaderTest.PURIFIED_PROTEIN);
            Assert.assertNotNull(sc);
            Assert.assertEquals("Purified protein, ready for crystallogenesis or further analysis", sc
                .getDetails());

            //Protocols
            /*
            Changed:
                sampleCatName from "Target -contaiing sample" to "Purified protein"
                /protocols/SSPF_Purification1.xml
                /YSBL/protocols/YSBL_Purification1.xml
                /YSBL/protocols/YSBL_Purification2.xml
                /YSBL/protocols/YSBL_Purification3.xml
                /OPPF/protocols/OPPF_Mass_Spec.xml
                /OPPF/protocols/OPPF_ScaleUp_Purification.xml
                /OPPF/protocols/OPPF_ScaleUp_Concentration.xml
            */
            this.verifyProtocolOutputSample(this.wv, "SSPF Purification 1",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, null, "L",
                RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolOutputSample(this.wv, "YSBL affinity Purification",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, null, "L",
                RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolOutputSample(this.wv, "YSBL non-affinity Purification",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, null, "L",
                RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolOutputSample(this.wv, "YSBL His-tag Purification",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, null, "L",
                RefDataLoaderTest.MICROLITRES);

            this.verifyProtocolOutputSample(this.wv, "OPPF Mass Spectrometry", "Mass Spec Output",
                RefDataLoaderTest.PURIFIED_PROTEIN, null, "L", "mL");
            /* protocol has changed this.verifyProtocolInputSample(this.wv, "OPPF Mass Spectrometry", "Concentrated Protein",
                RefDataLoaderTest.PURIFIED_PROTEIN, 0.00008f, "L", RefDataLoaderTest.MICROLITRES); */

            /*this.verifyProtocolOutputSample(this.wv, "OPPF ScaleUp Purification",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, 0.00008f,
                "L", RefDataLoaderTest.MICROLITRES); 

            this.verifyProtocolOutputSample(this.wv, "OPPF ScaleUp Concentration", "Concentrated Protein",
                RefDataLoaderTest.PURIFIED_PROTEIN, 0.00008f, "L", RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolInputSample(this.wv, "OPPF ScaleUp Concentration",
                RefDataLoaderTest.PURIFIED_PROTEIN_INIT_CAPS, RefDataLoaderTest.PURIFIED_PROTEIN, null, "L",
                RefDataLoaderTest.MICROLITRES); */

            /*
             *Changed:
            sampleCatName and RefSample attributes from Primer to "Forward Primer" or "Reverse Primer"
            /protocols/PIMS_PCR.xml
            /YSBL/protocols/HiTel_Standard_PCR.xml
            /YSBL/protocols/YSBL_StandardPCR.xml
            Old:
            <inputSample sampleCatNamingSys="default" sampleCatName="Primer" amount="0.000001" unit="L" displayUnit="uL" refSample="Primer" name="Forward primer" />
            <inputSample sampleCatNamingSys="default" sampleCatName="Primer" amount="0.000001" unit="L" displayUnit="uL" refSample="Primer" name="Reverse primer" />
            New:
            <inputSample sampleCatNamingSys="default" sampleCatName="Forward Primer" amount="0.000001" unit="L" displayUnit="uL" refSample="Forward Primer" name="Forward primer" />
            <inputSample sampleCatNamingSys="default" sampleCatName="Reverse Primer" amount="0.000001" unit="L" displayUnit="uL" refSample="Reverse Primer" name="Reverse primer" />

             */
            this.verifyProtocolInputSample(this.wv, "PiMS PCR", "Forward Primer", "Forward Primer",
                0.000001f, "L", RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolInputSample(this.wv, "PiMS PCR", "Reverse Primer", "Reverse Primer",
                0.000001f, "L", RefDataLoaderTest.MICROLITRES);
            final Protocol protocol = this.wv.findFirst(Protocol.class, Protocol.PROP_NAME, "PiMS PCR");
            //make true did not add new one
            Assert.assertEquals(7, protocol.getRefInputSamples().size());
            Assert.assertEquals(5, protocol.getParameterDefinitions().size());

            this.verifyProtocolInputSample(this.wv, "HiTel Standard PCR", "Forward primer", "Forward Primer",
                0.000005f, "L", RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolInputSample(this.wv, "HiTel Standard PCR", "Reverse primer", "Reverse Primer",
                0.000005f, "L", RefDataLoaderTest.MICROLITRES);

            this.verifyProtocolInputSample(this.wv, "YSBL Standard PCR", "Forward primer", "Forward Primer",
                0.000001f, "L", RefDataLoaderTest.MICROLITRES);
            this.verifyProtocolInputSample(this.wv, "YSBL Standard PCR", "Reverse primer", "Reverse Primer",
                0.000001f, "L", RefDataLoaderTest.MICROLITRES);
            /*Protocols
             * Changed: added alphabetical index to ParameterDefinition name attributes
             * /YSBL/protocols/HiTel_CloneVerification.xml
             * /YSBL/protocols/HiTel_LIC_Cloning.xml
             * /YSBL/protocols/HiTel_LIC_polymerase.xml
             * /YSBL/protocols/HiTel_LIC_VectorPrep.xml
             *
             */
            this.verifyProtocolParameterDefinition(this.wv, "HiTel Clone verification", "A. Polymerase",
                "Polymerase used: Pfu/KOD", "String", "Pfu");
            this.verifyProtocolParameterDefinition(this.wv, "HiTel LIC cloning",
                "A. Vector: LIC+, LIC-, Baculo", "Type of vector used: LIC +/- cleavable C-term", "String",
                "LIC+");
            this.verifyProtocolParameterDefinition(this.wv, "HiTel LIC-polymerase reaction",
                "A. Insert concentration pmol/ul", "", "Float", "");
            this.verifyProtocolParameterDefinition(this.wv, "HiTel LIC vector preparation",
                "A. Plasmid concentration ug/ml", "Plasmid concentration ug/ml", "Float", "");
            this.verifyProtocolParameterDefinition(this.wv, "PiMS Trial expression", "Q. Gel details", "",
                "String", "Markers in lane 1, etc.");

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    private void verifyProtocolParameterDefinition(final WritableVersion wv, final String protocolName,
        final String pdName, final String lable, final String type, final String defaultValue) {
        final Protocol protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        Assert.assertNotNull(protocol);
        final List<ParameterDefinition> pds = protocol.getParameterDefinitions();
        ParameterDefinition pdFound = null;
        for (final ParameterDefinition pd : pds) {
            if (pd.getName().equals(pdName)) {
                pdFound = pd;
                break;
            }
        }
        Assert.assertNotNull(pdFound);
        //verify pdFound
        Assert.assertEquals(lable, pdFound.getLabel());
        Assert.assertEquals(type, pdFound.getParamType());
        Assert.assertEquals(defaultValue, pdFound.getDefaultValue());
    }

    /**
     * 
     * @param wv
     * @param protocolName
     * @param outputSampleName
     * @param sampleCategoryName
     * @param amount
     * @param unit
     * @param displayUnit
     */

    private void verifyProtocolOutputSample(final WritableVersion wv, final String protocolName,
        final String outputSampleName, final String sampleCategoryName, final Float amount,
        final String unit, final String displayUnit) {
        final Protocol protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        Assert.assertNotNull("Can not find protocol: " + protocolName, protocol);
        Assert.assertEquals(1, protocol.getRefOutputSamples().size());
        final RefOutputSample os = protocol.getRefOutputSamples().iterator().next();
        //verify outputSampleName
        Assert.assertEquals("protocol " + protocolName + "'s outputSample should use Name "
            + outputSampleName, outputSampleName, os.getName());
        //verify sampleCategoryName
        Assert.assertEquals("protocol " + protocolName + "'s outputSample should use sampleCategory "
            + sampleCategoryName, sampleCategoryName, os.getSampleCategory().getName());
        //verify amount
        Assert.assertEquals("protocol " + protocolName + "'s outputSample should use amount " + amount,
            amount, os.getAmount());
        //verify unit
        Assert.assertEquals("protocol " + protocolName + "'s outputSample should use unit " + unit, unit, os
            .getUnit());
        //verify displayUnit
        Assert.assertEquals("protocol " + protocolName + "'s outputSample should use displayUnit "
            + displayUnit, displayUnit, os.getDisplayUnit());

    }

    /**
     * 
     * @param wv
     * @param protocolName
     * @param inputSampleName
     * @param sampleCategoryName
     * @param amount
     * @param unit
     * @param displayUnit
     */
    private void verifyProtocolInputSample(final WritableVersion wv, final String protocolName,
        final String inputSampleName, final String sampleCategoryName, final Float amount, final String unit,
        final String displayUnit) {
        final Protocol protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        Assert.assertNotNull("Can not find protocol: " + protocolName, protocol);
        final RefInputSample is =
            protocol.findFirst(Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME, inputSampleName);
        //verify RefInputSample existed
        Assert.assertNotNull("Can not find RefInputSample " + inputSampleName + " for protocol"
            + protocolName, is);
        //verify inputSampleName
        Assert.assertEquals("protocol " + protocolName + "'s inputSample should use Name " + inputSampleName,
            inputSampleName, is.getName());
        //verify sampleCategoryName
        Assert.assertEquals("protocol " + protocolName + "'s inputSample should use sampleCategory "
            + sampleCategoryName, sampleCategoryName, is.getSampleCategory().getName());
        //verify amount
        Assert.assertEquals("protocol " + protocolName + "'s inputSample should use amount " + amount,
            amount, is.getAmount());
        //verify unit
        Assert.assertEquals("protocol " + protocolName + "'s inputSample should use unit " + unit, unit, is
            .getUnit());
        //verify displayUnit
        Assert.assertEquals("protocol " + protocolName + "'s inputSample should use displayUnit "
            + displayUnit, displayUnit, is.getDisplayUnit());

    }
}
