/**
 * 
 */
package org.pimslims.presentation.bioinf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.bioinf.FloodGate;
import org.pimslims.bioinf.NCBIBlast;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.target.Target;
import org.pimslims.properties.PropertyGetter;

import uk.ac.ebi.webservices.InputParams;

/**
 * @author Susy Griffiths YSBL 23-07-07 Class to set up multiple calls to WSNCBIBlast web service Need to get
 *         all target id's and protein sequences Then make Blast beans and use them to create a Map for
 *         DbRef/DbName + BlastRef
 */
public class BlastMultiple {

    private static final String EMAIL = "pims-defects@dl.ac.uk";

    //comment these two lines out to run on command line
    //private static AbstractModel model = ModelImpl.getModel();
    //private static ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);

    /*
     * Method to create a List of all Targets
     */
    public static List allTargets(final ReadableVersion rv) {

        // *****Find all targets and make a map
        final List allTargets = (List) rv.getAll(Target.class);
        System.out.println("We have " + allTargets.size());
        if (0 != allTargets.size()) {
            assert allTargets.size() > 0;
            // process20(allTargets);
        } else {
            System.out.println("No Targets found!");
        }
        return allTargets;
    }

    /*
     * Method to process a sublist of Targets Loop through list of <=20 and
     * create a call to WSNCBIBlast for each
     */
    public static void blastSublist(final List subList) {
        int targCounter = 0;
        for (final Iterator j = subList.iterator(); j.hasNext();) {
            final Target target = (Target) j.next();
            targCounter += 1;
            final String targSeq = target.getSeqString();
            //final String id = target.get_Hook();
            final String target_protName = target.get_Name();
            System.out.println("Processing Target no: " + targCounter + ": " + target_protName);

            // create the inputs and parameters for WSNCBIBlast
            final String seq = /* ">" + target_protName + "\r\n" +*/targSeq;

            // make the parameters
            final InputParams params = BlastMultiple.makeParams("PDB", BlastMultiple.EMAIL);

            //String results = null;

            try {
                final NCBIBlast service = new NCBIBlast();
                // WUBlast service = new WUBlast();

                service.getBlastXmlAsync(params, seq);
                // System.out.println("......The xml is ....");
                // System.out.println(results);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Method to create call to Blast asynchronous for a single target
     */
    public static String blastTargetAsynch(final Target target, final String dbToUse, final String eMail)
        throws IOException, InterruptedException {
        String results = null;
        if (!PIMSTarget.isDNATarget(target)) {
            final String targSeq = target.getSeqString();
            //final String id = target.get_Hook();
            final String target_protName = target.get_Name();
            System.out.println("Processing Target: " + target_protName);

            //create the inputs and parameters for WSNCBIBlast
            //String seq = "";
            if (!"".equals(targSeq) || targSeq != null) {
                //seq = ">" + target_protName + "\r\n" + targSeq;
                //input.setContent(seq);

                //make the parameters
                final InputParams params = BlastMultiple.makeParams(dbToUse, eMail);

                //String results = null;
                //011107 try to prevent call if no sequence -works in test case!
                if (!"".equals(targSeq)) {
                    final NCBIBlast service = new NCBIBlast();
                    //WUBlast service = new WUBlast();

                    results = service.getBlastXmlAsync(params, targSeq);
                    if (results == null) {
                        System.out.println("Blast failed for Target: " + target_protName);
                    } else if ("".equals(results) || "PENDING".equals(results)) {
                        System.out.println("No Hits for Target: " + target_protName + " " + results);
                    } else {
                        System.out.println("Sucessful Blast for Target: " + target_protName);
                    }
                    //System.out.println("......The xml is ....");
                    //System.out.println(results);
                }
            }
        }
        return results;

    }

    /**
     * @param string
     * @throws FileNotFoundException
     */
    private static AbstractModel initModel(final String propertyFileName) throws FileNotFoundException {

        //start from propertyFile if provided
        final File properties = new java.io.File(propertyFileName);
        if (!properties.exists()) {
            System.out.println("file does NOT exist:" + propertyFileName);
        } else if (!properties.isFile()) {
            System.out.println("please give a file NOT a directory: " + propertyFileName);
        }
        System.out.println("loading DB connection info from: " + propertyFileName);

        return org.pimslims.dao.ModelImpl.getModel(properties);

    }

    public static void main(final String[] args) throws FileNotFoundException {
        //Uncomment the following 7 lines to run on the command line
        //*
        if (args.length < 1) {
            System.out.println(" Please provide properties file which defines the DB connection info!");
            return;
        }

        final AbstractModel model = BlastMultiple.initModel(args[0]);
        final ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        PropertyGetter.setProxySetting();
        //*/
        try {
            final String dbToUse = args[1];
            //for testing provide String
            //final String dbToUse = "TargetDB";
            final String email = args[2]; // should validate e.mail
            final FloodGate.AllDone callback = new FloodGate.AllDone() {
                public void onAllDone() {
                    System.out.println("flood gate example all done");
                }

                public void onFailed(final Throwable e) {
                    e.printStackTrace();
                }
            };

            final FloodGate floodGate = new FloodGate(BlastMultipleUtility.MAX, callback);
            final List<Target> targets = BlastMultiple.allTargets(rv);
            //For testing, use a small number of targets
            //targets = targets.subList(1, 4);

            for (int i = 0; i < targets.size(); i++) {
                // can't pass model object to another thread, just hook
                final String targetHook = targets.get(i).get_Hook();

                final Runnable job = new Runnable() {
                    public void run() {
                        // we need a transaction private to this thread
                        final WritableVersion wv =
                            model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
                        try {
                            final Target target = wv.get(targetHook);
                            BlastMultiple.runAndProcessBlast(target, dbToUse, wv, email);
                            wv.commit();
                        } catch (final AccessException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } catch (final ConstraintException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } catch (final AbortedException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } catch (final ClassNotFoundException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } catch (final IOException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } catch (final InterruptedException e) {
                            throw new RuntimeException("Failure for: " + targetHook, e);
                        } finally {
                            if (!wv.isCompleted()) {
                                wv.abort();
                            }
                        }
                    }
                };

                floodGate.addJob(job);
            }
            floodGate.close();
        } finally {
            rv.abort();
        }
    }

    /*
     * Method to create the Params for WSNCBIBlast To return 1 hit and 1
     * alignment
     */
    public static InputParams makeParams(final String dbToSearch, final String eMail) {
        final InputParams params = new InputParams();
        //program to run, could also use blastn, tblastn, tblastx
        params.setProgram("blastp");
        if (dbToSearch.contains("PDB")) {
            params.setDatabase("pdb");
        } else if (dbToSearch.contains("TargetDB")) {
            params.setDatabase(InputParams.TARGETDB);
        }
        //params.setEmail("pims-defects@dl.ac.uk");
        //221007 provide email string
        params.setEmail(eMail);
        //low complexity filter off, default is "seg"
        params.setFilter("none");
        //to set E value to 1.0 default is 10
        params.setExp("1.0"); // currently ignored
        //to limit number of alignments retrieved default is 50
        params.setNumal(InputParams.MIN_HITS);
        //to limit number of hits retrieved default is 50
        params.setScores(InputParams.MIN_HITS);
        //to set the submissions asynchronous
        params.setAsync(new Boolean(true));
        return params;
    }

    /*
     * Method to create a DbRef from output from Blast
     * Also update existing Tophits so that only 2 maximum per Target for PDB and targetDB
     */
    /**
     * @param target
     * @param dbToUse
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws ServiceUnavailableException
     * @throws InterruptedException
     * @throws AbortedException
     */
    public static void runAndProcessBlast(final Target target, final String dbToUse,
        final WritableVersion wv, final String eMail) throws AccessException, ConstraintException,
        ClassNotFoundException, IOException, InterruptedException, AbortedException {
        final String blastOutput = BlastMultiple.blastTargetAsynch(target, dbToUse, eMail);
        if (null != blastOutput) {
            final Map bDbRefPrms = BlastMultipleUtility.makeBlastDbRefMap(blastOutput);
            final Map dbrefPrms = BlastMultipleUtility.createDbRefParams(wv, bDbRefPrms);
            final Map noDetails = BlastMultipleUtility.createDbRefParams(wv, bDbRefPrms);
            //System.out.println("TESTING 240510 dbrefPrms in BM l.303: " + dbrefPrms.toString());
            //final String dbrDetails = (String) dbrefPrms.get("details");
            //System.out.println("TESTING 240510 dbrefPrms details in BM l.304 is: " + dbrDetails);
            //081007 testing updater code
            //final DbRef bdbr = (DbRef) BlastMultiple.makeBlastDbRef(wv,  dbrefPrms, target );
            //assert (!(null==bdbr));
            //dbrefPrms.put(Attachment.PROP_DETAILS, dbrDetails);
            dbrefPrms.put(Attachment.PROP_PARENTENTRY, target);
            noDetails.put(Attachment.PROP_PARENTENTRY, target);
            //System.out.println("TESTING 250510 BM l.311:Map for updatingBlastRefs is: " + dbrefPrms.toString());

            //???Try LabBookEntry.PROP_DETAILS
            //dbrefPrms.put(Attachment.PROP_DETAILS, dbrefPrms.get("details"));
            noDetails.remove("details");//removed because the 'date' will be different
            noDetails.remove("url"); //removed because this has changed
            //System.out.println("TESTING 250510 BM l.315:Map for getting matching DbRefs is: " + noDetails.toString());

            final List matchingDbRefs = BlastMultipleUtility.matchingDbRefs(wv, noDetails);
            //final List matchingDbRefs = BlastMultipleUtility.matchingDbRefs(wv, dbrefPrms);
            BlastMultipleUtility.updateBlastRefs(wv, matchingDbRefs, dbrefPrms);
        }
        //wv.commit();
        //if (!wv.isCompleted()) {
        //    wv.abort();
        //}
    }

    /**
     * To run from listener
     */
    public BlastMultiple(final FloodGate floodGate, final AbstractModel model, final String dbName,
        final String email) {
        final ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        //*/
        try {
            final String dbToUse = dbName;
            //for testing provide String
            //final String dbToUse = "TargetDB";

            final List<Target> targets = BlastMultiple.allTargets(rv);
            //For testing, use a small number of targets
            //targets = targets.subList(50, 100);
            for (int i = 0; i < targets.size(); i++) {
                // can't pass model object to another thread, just hook
                final String targetHook = targets.get(i).get_Hook();

                final Runnable job = new Runnable() {
                    public void run() {
                        // we need a transaction private to this thread
                        final WritableVersion wv =
                            model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
                        try {
                            final Target target = wv.get(targetHook);
                            //TODO enable DNATarget Blast  
                            if (target.getSeqString().length() > 0 && !PIMSTarget.isDNATarget(target)) {
                                BlastMultiple.runAndProcessBlast(target, dbToUse, wv, email);
                                wv.commit();
                            } else {
                                System.out
                                    .println("+++++Target has no protein sequence or is a DNA Target+++++ "
                                        + target.get_Name());
                            }
                        } catch (final AccessException e) {
                            throw new RuntimeException(e);
                        } catch (final ConstraintException e) {
                            throw new RuntimeException(e);
                        } catch (final AbortedException e) {
                            throw new RuntimeException(e);
                        } catch (final ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (!wv.isCompleted()) {
                                wv.abort();
                            }
                        }
                    }
                };
                floodGate.addJob(job);
            }
            floodGate.close();
        } finally {
            rv.abort();
        }
    }

}
