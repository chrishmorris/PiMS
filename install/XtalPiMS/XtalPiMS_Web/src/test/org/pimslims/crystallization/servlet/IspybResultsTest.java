/**
 * xtalPiMS_Web org.pimslims.crystallization.servlet IspybResultsTest.java
 * 
 * @author cm65
 * @date 13 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import javassist.NotFoundException;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.properties.PropertyGetter;

/**
 * IspybResultsTest
 * 
 */
public class IspybResultsTest extends TestCase {

    private final AbstractModel model;

    /**
     * Constructor for IspybResultsTest
     * 
     * @param name
     */
    public IspybResultsTest(String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
        PropertyGetter.setWorkingDirectory("web/WEB-INF/");
    }

    public void testClassLoader() throws MalformedURLException, ClassNotFoundException,
        InstantiationException, IllegalAccessException {
        ClassLoader loader = IspybResults.getClassLoader(this.getClass().getClassLoader());
        loader.loadClass("org.apache.axis2.description.AxisService");
        loader.loadClass("org.apache.axiom.om.OMNode");
        loader.loadClass("org.pimslims.ispyb.Client");
        Class clientClass = loader.loadClass("org.pimslims.ispyb.Client");
        clientClass.newInstance();
        loader.loadClass("uk.ac.diamond.ispyb.client.IspybServiceStub$BeamlineExportedInformation");
        // note that this fails: classLoader.loadClass("uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation");
    }

    /**
     * Test method for
     * {@link org.pimslims.crystallization.servlet.IspybResults#getShipment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.pimslims.dao.WritableVersion)}
     * .
     * 
     * @throws IOException
     * @throws NotFoundException
     * @throws LoginExceptionException
     * @throws BadDataExceptionException
     * @throws AxisFault
     */
    public void testGetShipment() throws IOException {

        Properties properties = new Properties();
        File file = new File("conf/Properties");
        InputStream inStream = new FileInputStream(file);
        System.out.println(file.getAbsolutePath());
        assertNotNull(inStream);
        properties.load(inStream);
        String userid = properties.getProperty("fedid");
        assertNotNull(userid);
        String password = properties.getProperty("password");

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            IspybResults.getShipment(userid, password, "test1307700021250", "mx5501-2", version);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
