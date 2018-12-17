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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.properties.PropertyGetter;
import org.xml.sax.SAXException;

/**
 * XmlLoader
 * 
 */
public class VectorXmlLoader {

    /**
     * XmlLoader.main
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java VectorXmlLoader dataDir");
            return;
        }
        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        // read files from input directory
        try {
            //VectorXmlLoader.loadDir(args[0], "WebContent/WEB-INF/", version);
            VectorXmlLoader.loadDir(args[0], "data/xml", version);
            version.commit();
            System.out.println("VectorXmlLoader has finished");
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    public static void loadDir(final String dirName, final String workingDir, final WritableVersion version)
        throws FileNotFoundException, JAXBException, SAXException, IOException, ConstraintException,
        AccessException {
        String[] filenames;
        final File f = new File(dirName);
        filenames = f.list();
        for (int i = 0; i < filenames.length; i++) {
            if (!filenames[i].equalsIgnoreCase(".svn")) {
                PropertyGetter.setWorkingDirectory(workingDir);
                System.out.println(filenames[i]);
                // read vector xml file
                InputStream in;
                VectorXmlBean vectorXmlBean;
                in = new FileInputStream(dirName + filenames[i]);
                vectorXmlBean = VectorXmlConvertor.convertXmlToVectorBean(in);
                VectorXmlBean.save(version, vectorXmlBean);
                in.close();

                System.out.println(vectorXmlBean.getName());
                System.out.println("processed: " + filenames[i]);

            }
        }
    }
}
