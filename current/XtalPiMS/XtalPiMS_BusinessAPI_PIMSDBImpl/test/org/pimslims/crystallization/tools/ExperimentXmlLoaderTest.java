/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.PlateExperimentServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

/**
 * 
 * @author Chris Morris
 */
public class ExperimentXmlLoaderTest extends PlateExperimentServiceTest {

    private static final String DESCRIPTION = "Crystallization experiment example";

    private static final String UNIQUE = "pxl" + System.currentTimeMillis();

    /**
     * sample name
     */
    private static final String PROTEIN = UNIQUE + "p";

    private static final String xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
            + "<tns:experiment xmlns:tns=\"http://www.helsinki.fi/~vpkestil/schemas/experimentSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.helsinki.fi/~vpkestil/schemas/experimentSchema ../XML_Schemas/experimentSchema.xsd \">\r\n"
            + "  <tns:barcode>"
            + BARCODE
            + "</tns:barcode>\r\n"
            + "  <tns:name>My experiment</tns:name>\r\n"
            + "  <tns:description>"
            + DESCRIPTION
            + "</tns:description>\r\n"
            + "  <tns:screen>\r\n"
            + "    <tns:name>Helsinki1</tns:name>\r\n"
            + "  </tns:screen>\r\n"
            + "  <tns:plate columnsize=\"1\" rowsize=\"2\">\r\n"
            + "    <tns:dropsite column=\"1\" row=\"A\">\r\n"
            + "        <tns:drop location=\"1\">\r\n"
            + "            <tns:sample>\r\n"
            + "                <tns:name>"
            + PROTEIN
            + "</tns:name>\r\n"
            + "                <tns:description>This is suprisingly a protein A</tns:description>\r\n"
            + "            </tns:sample>\r\n"
            + "            <tns:ratios sample=\"1\" screen=\"1\" additive=\"0.1\" />\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical1</tns:name>\r\n"
            + "                <tns:concentration>1</tns:concentration>\r\n"
            + "                <tns:unit>M</tns:unit>\r\n"
            + "            </tns:additive>\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical2</tns:name>\r\n"
            + "                <tns:concentration>10</tns:concentration>\r\n"
            + "                <tns:unit>Percent v/v</tns:unit>\r\n"
            + "            </tns:additive>      \r\n"
            + "        </tns:drop>\r\n"
            + "        <tns:drop location=\"2\">\r\n"
            + "            <tns:sample>\r\n"
            + "                <tns:name>Percipitant</tns:name>\r\n"
            + "                <tns:description>This is suprisingly a percipitant</tns:description>\r\n"
            + "            </tns:sample>\r\n"
            + "            <tns:ratios sample=\"1\" screen=\"2\" additive=\"0.2\" />\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical1</tns:name>\r\n"
            + "                <tns:concentration>1</tns:concentration>\r\n"
            + "                <tns:unit>M</tns:unit>\r\n"
            + "            </tns:additive>\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical2</tns:name>\r\n"
            + "                <tns:concentration>10</tns:concentration>\r\n"
            + "                <tns:unit>Percent v/v</tns:unit>\r\n"
            + "            </tns:additive>      \r\n"
            + "        </tns:drop>     \r\n"
            + "    </tns:dropsite>\r\n"
            + "    <tns:dropsite column=\"1\" row=\"B\">\r\n"
            + "        <tns:drop location=\"1\">\r\n"
            + "            <tns:sample>\r\n"
            + "                <tns:name>"
            + PROTEIN
            + "</tns:name>\r\n"
            + "                <tns:description>This is suprisingly a protein A</tns:description>\r\n"
            + "            </tns:sample>\r\n"
            + "            <tns:ratios sample=\"1\" screen=\"1\" additive=\"0.1\" />\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical1</tns:name>\r\n"
            + "                <tns:concentration>1</tns:concentration>\r\n"
            + "                <tns:unit>M</tns:unit>\r\n"
            + "            </tns:additive>\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical2</tns:name>\r\n"
            + "                <tns:concentration>10</tns:concentration>\r\n"
            + "                <tns:unit>Percent v/v</tns:unit>\r\n"
            + "            </tns:additive>      \r\n"
            + "        </tns:drop>\r\n"
            + "        <tns:drop location=\"2\">\r\n"
            + "            <tns:sample>\r\n"
            + "                <tns:name>Percipitant</tns:name>\r\n"
            + "                <tns:description>This is suprisingly a percipitant</tns:description>\r\n"
            + "            </tns:sample>\r\n"
            + "            <tns:ratios sample=\"1\" screen=\"2\" additive=\"0.2\" />\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical1</tns:name>\r\n"
            + "                <tns:concentration>1</tns:concentration>\r\n"
            + "                <tns:unit>M</tns:unit>\r\n"
            + "            </tns:additive>\r\n"
            + "            <tns:additive>\r\n"
            + "                <tns:name>Chemical2</tns:name>\r\n"
            + "                <tns:concentration>10</tns:concentration>\r\n"
            + "                <tns:unit>Percent v/v</tns:unit>\r\n"
            + "            </tns:additive>      \r\n"
            + "        </tns:drop>     \r\n"
            + "    </tns:dropsite>    \r\n" + "  </tns:plate>\r\n" + "</tns:experiment>";

    public ExperimentXmlLoaderTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(ExperimentXmlLoaderTest.class);

        return suite;
    }

    public void TODOtestSavePlateDetails() throws Exception {
        this.dataStorage.openResources("administrator");
        final InputStream input = new ByteArrayInputStream(xml.getBytes("ISO-8859-1"));
        try {
            save();
            final PlateExperimentService service = this.dataStorage.getPlateExperimentService();

            //search with BARCODE
            final BusinessCriteria criteria = new BusinessCriteria(service);
            final BusinessCriterion criterion =
                BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, BARCODE, true);
            criteria.add(criterion);
            Collection<PlateExperimentView> result = service.findViews(criteria);
            assertNotNull(result);
            assertEquals(1, result.size());
            PlateExperimentView pv = result.iterator().next();
            assertEquals(BARCODE, pv.getBarcode());
            assertEquals(DESCRIPTION, pv.getDescription());

            //TODO test create date and destroy date
            //TODO check plate type, temperature, operator

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void TODOtestSaveDropDetails() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            TrialPlate plate = save();

            final TrialService trialService = dataStorage.getTrialService();

            TrialDrop drop = trialService.findTrialDrop(BARCODE, new WellPosition("B01.1"));
            assertEquals("B01.1", drop.getWellPosition().toString());
            assertEquals(1, drop.getSamples().size());
            assertEquals(PROTEIN, drop.getSamples().iterator().next().getSample().getName());
            assertNotNull(drop.getPlate());
            assertEquals(plate.getBarcode(), drop.getPlate().getBarcode());
            //TODO test ratios and additives
            //TODO check ownership

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    private TrialPlate save() throws BusinessException, UnsupportedEncodingException {
        final InputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        final TrialService trialService = dataStorage.getTrialService();
        OrderXmlLoaderTest.save(this.dataStorage);
        dataStorage.flush();
        new ExperimentXmlLoader(input, this.dataStorage).save();
        this.dataStorage.flush();

        TrialPlate plate = trialService.findTrialPlate(BARCODE);
        return plate;
    }

}
