package org.pimslims.embl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.biojava.bio.BioException;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;

/**
 * Parse all the provided files, to check that we understand the format
 * 
 * @author cm65
 * 
 */
public class Loader {

    public static void main(final String[] args) {
        if (0 == args.length) {
            System.out.println("Usage: filename filename ....");
            return;
        }
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model
                .getWritableVersion(Access.ADMINISTRATOR);
        HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(
                version);
        try {
            for (int i = 0; i < args.length; i++) {
                File file = new File(args[i].trim());
                try {
                    InputStream in = new FileInputStream(file);
                    Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                            .parse(in);
                    for (Iterator iterator = beans.iterator(); iterator
                            .hasNext();) {
                        HamburgResearchObjectiveBean bean = (HamburgResearchObjectiveBean) iterator
                                .next();
                        writer.save(bean);
                    }
                    version.flush();
                    System.out.println("OK: " + file.getAbsolutePath());
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: "
                            + file.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Error while processing: "
                            + file.getAbsolutePath());
                    System.err.println(e.getMessage());
                } catch (AssertionError e) {
                    System.err.println("Error processing: "
                            + file.getAbsolutePath());
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    System.err.println("Error processing: "
                            + file.getAbsolutePath());
                    Throwable cause = e;
                    while (null != cause.getCause()) {
                        cause = cause.getCause();
                    }
                    cause.printStackTrace();
                } catch (ConstraintException e) {
                    System.err.println("Error processing: "
                        + file.getAbsolutePath());
                    throw e; // transaction cannot be committed
                } catch (BioException e) {
                    System.err.println("Error processing: "
                            + file.getAbsolutePath());
                    Throwable cause = e;
                    while (null != cause.getCause()) {
                        cause = cause.getCause();
                    }
                    cause.printStackTrace();
                }
            }
            version.commit();
            System.out.println("Loader completed");
        } catch (ConstraintException e) {
            System.err.println("Loader Failed");
            e.printStackTrace();
        } catch (AbortedException e) {
            System.err.println("Loader Failed");
            e.printStackTrace();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
}
