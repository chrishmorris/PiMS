/**
 * current-pims-web org.pimslims.data StandardSequencingPrimersUtility
 * 
 * @author Petr Troshin
 * @date Apr-2009
 * 
 * 
 */
package org.pimslims.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.lab.Util;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanWriter;

/**
 * StandardSequencingPrimersUtility Command line utility for loading RefData from .csv file for Standard
 * Primers used in sequencing the format: name, sequence
 * 
 */
public class StandardSequencingPrimersUtility extends AbstractLoader {
    /**
     * @param filename Name of file with a list of Extensions
     * @param wv org.pimslims.WritableVersion
     * @throws java.io.IOException IO exception
     * @throws AccessException org.pimslims.exception.AccessException
     * @throws ConstraintException org.pimslims.exception.ConstraintException
     * @throws ClassNotFoundException
     */
    public static void loadFile(final WritableVersion rw, final String filename, final boolean delete,
        final boolean addnew) throws java.io.IOException, AccessException, ConstraintException,
        ClassNotFoundException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);

        final List<String> labels = Arrays.asList(lcsvp.getLabels());
        if (!labels.contains("name")) {
            throw new RuntimeException(
                "StandardSequencingPrimersUtility can not handle data file which does not have a column for 'name'!");
        }

        while (lcsvp.getLine() != null) {

            //**** name of the Extension AbstractComponent.PROP_NAME

            final String name = lcsvp.getValueByLabel("name");
            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }
            final String sequence = lcsvp.getValueByLabel("seqString").toUpperCase().trim();

            final Sample sample = rw.findFirst(Sample.class, AbstractSample.PROP_NAME, name);

            if (sample != null) {
                if (addnew) {
                    continue;
                }
                if (delete) {
                    sample.delete();
                    System.out.println("Deleting Sample " + sample);
                } else {
                    throw new RuntimeException(
                        "Sample with name "
                            + name
                            + " already exist! Please either delete it first! (you can force deleting but calling the program with deleteRecorded parameter) or specify the addnew flag!");
                }
            }

            final Molecule mol = rw.findFirst(Molecule.class, Substance.PROP_NAME, name);

            if (mol != null) {
                if (addnew) {
                    continue;
                }
                if (delete) {
                    mol.delete();
                    System.out.println("Deleting Molecule " + mol);
                } else {
                    throw new RuntimeException(
                        "Molecule with name "
                            + name
                            + " already exist! Please delete it first! Or force deleting but calling the program with deleteRecorded parameter! ");
                }
            }

            final Primer prim = rw.findFirst(Primer.class, Substance.PROP_NAME, name);

            if (prim != null) {
                if (addnew) {
                    continue;
                }
                if (delete) {
                    prim.delete();
                    System.out.println("Deleting Primer " + prim);
                } else {
                    throw new RuntimeException(
                        "Primer with name "
                            + name
                            + " already exist! Please delete it first! Or force deleting but calling the program with deleteRecorded parameter! ");
                }
            }

            assert !Util.isEmpty(name) && !Util.isEmpty(sequence);

            final PrimerBean pb = new PrimerBean(name, sequence, true, ""); // no tag

            final Sample primer = PrimerBeanWriter.writePrimer(rw, pb, null);

            final SampleCategory sc =
                rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Standard Sequencing Primer");

            if (sc == null) {
                throw new RuntimeException(
                    "Sample Category: 'Standard Sequencing Primer' is not found! Please run loadSampleCategories first!");
            }
            // Override sample category as appropriate
            primer.setSampleCategories(Collections.singleton(sc));

        }
    }

    /**
     * @param args arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out
                .println("Usage: StandardSequencingPrimersUtility filename.csv [deleteRecorded|addnew]");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        final String filename = args[0];
        String action = null;
        if (args.length == 2) {
            action = args[1];
        }
        final boolean delete =
            !Util.isEmpty(action) && action.equalsIgnoreCase("deleteRecorded") ? true : false;

        final boolean add = !Util.isEmpty(action) && action.equalsIgnoreCase("addnew") ? true : false;

        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            StandardSequencingPrimersUtility.loadFile(wv, filename, delete, add);
            wv.commit();
            System.out.println("Loaded details from file: " + filename);
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
        } catch (final ModelException ex) {
            AbstractLoader.print("Unable to add details from file: " + filename);
            ex.printStackTrace();
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        System.out.println("StandardSequencingPrimersUtility utility has finished");

    }
}
