/**
 * pims-web org.pimslims.xmlio XmlTransformer.java
 * 
 * @author pajanne
 * @date Jan 15, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.bind.JAXBException;

import org.jdom.JDOMException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.ProtocolUtility;
import org.pimslims.exception.ModelException;
import org.pimslims.model.protocol.Protocol;

/**
 * XmlTransformer
 * 
 */
public class XmlTransformer {

    /**
     * XmlTransformer.main
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java XmlTransformer oldFileName newFileName");
            return;
        }
        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            // read old protocol xml file
            final java.io.Reader reader = new java.io.FileReader(args[0]);
            final Protocol protocol = new ProtocolUtility(wv).loadFile(reader);
            System.out.println("processed: " + args[0]);
            // write new protocol xml file
            final PrintWriter output = new PrintWriter(new FileWriter(args[1]));
            output.write(XmlConvertor.convertBeanToXml(new ProtocolXmlBean(protocol),
                ProtocolXmlConvertor.PROTOCOL_SCHEMA));
            output.close();
            System.out.println("processed: " + args[1]);
            reader.close();
        } catch (final JDOMException e) { // indicates a well-formedness error
            System.out.println(args[0] + " is not well-formed.");
            System.out.println("Could not process: " + args[0]);
            throw new RuntimeException(e);
        } catch (final IOException e) {
            System.out.println("Could not process: " + args[0]);
            throw new RuntimeException(e);
        } catch (final ModelException e) {
            System.out.println("Could not process: " + args[0]);
            throw new RuntimeException(e);
        } catch (final AssertionError e) {
            System.out.println("Could not process: " + args[0]);
            throw e;
        } catch (final JAXBException e) {
            System.out.println("Problem while processing: " + args[1]);
            throw new RuntimeException(e);
        } finally {
            wv.abort(); // do not need to persist
        }
        System.out.println("XmlTransformer has finished");

    }

}
