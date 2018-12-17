package org.pimslims.command;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jdom.JDOMException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.ProtocolUtility;
import org.pimslims.exception.ModelException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;

public class ProtocolUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

/* TODO This test fails, if the protocol has been used. Is that a real problem, or a bad test?
    public void testLoad() throws JDOMException {

        final String filename = "./data/protocols/Leeds_Order_PrimerPlate.xml";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Protocol protocolOrder =
                wv.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.PIMS_ORDER);
            if (protocolOrder != null) {
                protocolOrder.getParameterDefinitions().iterator().next().delete();
            }
            final java.io.Reader reader = new java.io.FileReader(filename);
            new ProtocolUtility(wv).loadFile(reader);

            Assert.assertTrue(protocolOrder.getParameterDefinitions().size() == 10);
            // wv.commit();
            System.out.println("Loaded details from file: " + filename);
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } catch (final ModelException ex) {
            System.out.println("Unable to add details from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        System.out.println("Protocol utility has finished");
    } */

    public void testLoadPossibleValue() throws JDOMException {

        final String filename = "./data/Glasgow/protocols/GLASGOW_Membrane_Preparation.xml";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final java.io.Reader reader = new java.io.FileReader(filename);
            new ProtocolUtility(wv).loadFile(reader);
            final Protocol protocol =
                wv.findFirst(Protocol.class, Protocol.PROP_NAME, "GLASGOW Membrane Preparation");
            final ParameterDefinition pd =
                protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME,
                    "a. Method of cell disruption");
            Assert.assertEquals(4, pd.getPossibleValues().size());
            // wv.commit();
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } catch (final ModelException ex) {
            System.out.println("Unable to add details from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

}
