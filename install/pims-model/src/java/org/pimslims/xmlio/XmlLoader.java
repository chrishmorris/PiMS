/**
 * pims-web org.pimslims.xmlio XmlLoader.java
 * 
 * @author pajanne
 * @date Feb 9, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.xml.sax.SAXException;

/**
 * XmlLoader
 * 
 */
public class XmlLoader {

    /**
     * XmlLoader.main
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java XmlLoader xmlFileName1 xmlFileName2 ...");
            return;
        }
        final AbstractModel model = ModelImpl.getModel();
        for (int i = 0; i < args.length; i++) {
            final WritableVersion version =
                model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                // read protocol xml file
                final InputStream in = new FileInputStream(args[i]);
                ProtocolXmlConvertor.convertXmlToProtocol(version, in);
                System.out.println("processed: " + args[i]);
                version.commit();
                in.close();
            } catch (final IOException e) {
                System.out.println("Could not process: " + args[i]);
                throw new RuntimeException(e);
            } catch (final ModelException e) {
                System.out.println("Could not process: " + args[i]);
                throw new RuntimeException(e);
            } catch (final AssertionError e) {
                System.out.println("Could not process: " + args[i]);
                throw new RuntimeException(e);
            } catch (final SAXException e) {
                System.out.println("Problem while processing: " + args[i]);
                throw new RuntimeException(e);
            } catch (final JAXBException e) {
                System.out.println("Problem while processing: " + args[i]);
                throw new RuntimeException(e);
            } finally {
                if (!version.isCompleted()) {
                    version.abort(); // do not need to persist
                }
            }
        }
        System.out.println("XmlLoader has finished");

    }
}
