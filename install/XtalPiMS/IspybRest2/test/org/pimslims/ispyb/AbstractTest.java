package org.pimslims.ispyb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.LoginExceptionException;

public class AbstractTest extends IspybBeanFactory {

    protected static final String CRYSTAL_FORM_PREFIX = "http://pims/pims/CrystalForm/";

    /**
     * PROTEIN_NAME String
     */
    private static final String PROTEIN_NAME = "p" + System.currentTimeMillis();

    /**
     * PROTEIN_UUID String
     */
    private static final String PROTEIN_UUID = "http://pims.structuralbiology.eu:8080/pims/Protein/"
        + PROTEIN_NAME;

    /**
     * CRYSTAL_NAME String
     */
    protected static final String CRYSTAL_NAME = "c" + System.currentTimeMillis();

    /**
     * CRYSTAL_UUID String
     */
    protected static final String CRYSTAL_UUID = CRYSTAL_FORM_PREFIX + CRYSTAL_NAME;

    /**
     * PROJECT_UUID String
     */
    protected static final String PROJECT_UUID = "mx5501-2";

    protected static final String UNIQUE = "test" + System.currentTimeMillis();

    protected Client client = null;

    // if the server is self-signed:
    // protected final String certificatePassword;
    // protected final String certificateFilePath;

    /**
     * @param certificatePassword
     * @param certificateFilePath
     */
    public AbstractTest() {
        super();
    }

    protected boolean login(Client client) throws IOException, BadDataExceptionException,
        LoginExceptionException {
        Properties properties = new Properties();
        InputStream inStream = EndToEndTest.class.getResourceAsStream("/Properties");
        properties.load(inStream);
        boolean loggedIn =
            client.authenticate(properties.getProperty("fedid"), properties.getProperty("password"));
        return loggedIn;
    }

    /**
     * @return
     */
    protected Crystal makeUniqueCrystal(String suffix) {
        return makeCrystal(makeProtein(PROTEIN_NAME, PROTEIN_UUID + suffix), CRYSTAL_NAME + suffix,
            CRYSTAL_UUID);
    }

    @Before
    public void setUp() throws IOException, BadDataExceptionException, LoginExceptionException {
        this.client = new Client();
        login(client);
    }

}
