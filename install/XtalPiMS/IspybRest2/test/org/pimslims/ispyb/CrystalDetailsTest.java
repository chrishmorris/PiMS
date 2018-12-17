package org.pimslims.ispyb;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.junit.Test;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;

/**
 * @author cm65 There is a basic test of the crystalDetails web service in EndToEndTest This class adds tests
 *         for more special requirements
 */
public class CrystalDetailsTest extends AbstractTest {

    public CrystalDetailsTest() {
        super();
    }

    @Test
    public void testOldProteinNewCrystal() throws AxisFault, BadDataExceptionException, NotFoundException,
        MalformedURIException {

        Crystal crystal1 = makeUniqueCrystal("o1");
        CrystalDetails details = makeCrystalDetails(Collections.singleton(crystal1), PROJECT_UUID);
        validate(details);
        String response = this.client.crystalDetails(details);
        assertEquals("OK", response);

        // now send another crystal
        Crystal crystal2 = makeCrystal(crystal1.getProtein(), CRYSTAL_NAME + "o2", CRYSTAL_UUID + "o2");
        details = makeCrystalDetails(Collections.singleton(crystal2), PROJECT_UUID);
        response = this.client.crystalDetails(details);
        assertEquals("OK", response);

    }

    @Test
    public void testTwoCrystalsOneProtein() throws AxisFault, BadDataExceptionException, NotFoundException,
        MalformedURIException {
        Collection<Crystal> crystals = new ArrayList(2);
        Crystal crystal1 = makeUniqueCrystal("b1");
        crystals.add(crystal1);
        Crystal crystal2 = makeCrystal(crystal1.getProtein(), CRYSTAL_NAME + "b2", CRYSTAL_UUID + "b2");
        crystals.add(crystal2);
        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
        validate(details);
        String response = this.client.crystalDetails(details);
        assertEquals("OK", response);
    }

    @Test
    public void testIdempotent() throws AxisFault, BadDataExceptionException, NotFoundException,
        MalformedURIException {
        CrystalDetails details =
            makeCrystalDetails(Collections.singleton(makeUniqueCrystal("bi")), PROJECT_UUID);
        validate(details);
        String response = this.client.crystalDetails(details);
        assertEquals("OK", response);

        // now send the same details again
        // for example, the same crystal might shipped more than once
        // note that it is not easy for the client to find out if Ispyb has a record of the crystal
        response = this.client.crystalDetails(details);
        assertEquals("OK", response);

    }

}
