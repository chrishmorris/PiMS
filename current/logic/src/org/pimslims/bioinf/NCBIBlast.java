package org.pimslims.bioinf;

/*
 * To use this, you may have to set an HTTP proxy, by e.g.: -Dhttp.proxyPort=8080
 * -Dhttp.proxyHost=wwwcache.dl.ac.uk on the "java" command.
 */
import java.io.IOException;

import uk.ac.ebi.webservices.InputParams;
import uk.ac.ebi.webservices.WSNCBIBlast;

public class NCBIBlast {

    /**
     * TOOL_XML String
     */
    static final String TOOL_XML = "toolxml";

    /**
     * TOOL_OUTPUT String
     */
    private static final String TOOL_OUTPUT = "tooloutput";

    private static final long DELAY = 30;

    private final WSNCBIBlast ncbiblast;

    /**
     * @throws IOException
     * @throws ServiceUnavailableException
     * 
     */
    public NCBIBlast() throws IOException {
        this.ncbiblast = new NcbiBlastImpl(); //was service.getWSNCBIBlast();
    }

    /*
     * Method to get results of Blast for sequence string
     */
    //public String getBlastRecord(uk.ac.ebi.www.InputParams params, uk.ac.ebi.www.Data[] data)
    @Deprecated
    // only tested, not used
    public String getBlastRecord(final uk.ac.ebi.webservices.InputParams params, final String sequence) {
        final String jobid = this.ncbiblast.runNCBIBlast(params, sequence);
        return this.ncbiblast.poll(jobid, NCBIBlast.TOOL_OUTPUT);
    }

    //public String getBlastResults(final uk.ac.ebi.www.InputParams params, final uk.ac.ebi.www.Data[] data)

    @Deprecated
    // only tested, not used
    public String getBlastResults(final uk.ac.ebi.webservices.InputParams params, final String sequence) {
        final String jobid = this.ncbiblast.runNCBIBlast(params, sequence);
        return this.ncbiblast.poll(jobid, NCBIBlast.TOOL_OUTPUT);
    }

    /*
     * Method to get a list of possible output formats to set
     */
    /*
     * public WSFile[] getBlastResults(InputParams params, Data[] data) throws RemoteException { String jobid =
     * ncbiblast.runNCBIBlast(params, data); WSFile[] results = ncbiblast.getResults(jobid); for (int i=0;i<results.length;i++){
     * WSFile file = results[i]; System.out.println("Result " + file.getType()+" is available, with file
     * extension " + file.getExt()); } //byte[] resultbytes = blast.poll(jobid,"tooloutput"); return results; } /*
     * Method to return blast results for xml file
     */

    String getBlastXml(final uk.ac.ebi.webservices.InputParams params, final String sequence) {
        final String jobid = this.ncbiblast.runNCBIBlast(params, sequence);
        return this.ncbiblast.poll(jobid, NCBIBlast.TOOL_XML);
    }

    /*
     * Method to return blast results for xml file
     */
    //public String getBlastXmlAsync(final uk.ac.ebi.www.InputParams params, final uk.ac.ebi.www.Data[] data)
    public String getBlastXmlAsync(final uk.ac.ebi.webservices.InputParams params, final String sequence)
        throws InterruptedException {
        final String seq = sequence;
        if (seq == null || "".equals(seq)) {
            System.out.println("*+*+*+ cannot run Blast Target has no sequence +*+*+*");
            final String noResult = "";
            return noResult;
        }
        final String jobid = this.ncbiblast.runNCBIBlast(params, seq);
        String result = "PENDING";
        String status = this.ncbiblast.checkStatus(jobid);
        // TODO what about pending?
        while (status.equals(NcbiBlastImpl.RUNNING)) {

            System.out.println("*****The job " + jobid + "is running******");
            Thread.sleep(NCBIBlast.DELAY);
            status = this.ncbiblast.checkStatus(jobid); // check for the job status
        }
        if (status.equals("DONE") // obsolete
            || status.equals(NcbiBlastImpl.FINISHED)) {
            System.out.println("****Job " + jobid + " is done******");
            result = this.ncbiblast.poll(jobid, NCBIBlast.TOOL_XML);
            if ("".equals(result)) {
                System.out.println("Blast ".equals(jobid));
            }
            return result;
        }
        final String problem = "Error with the job " + jobid + result;
        System.out.println("+++++" + problem);
        return problem;
    }

    //TODO get this under test
    public static String getBlastResult(final String target_protSeq, final String dbToSearch)
        throws IOException {
        String result;
        // Set up the parameters for WSNCBIBlast service call
        final InputParams params = new InputParams();
        if (dbToSearch.contains("PDB")) {
            params.setDatabase("pdb");
            params.setProgram("blastp");
        } else if (dbToSearch.contains("TargetDB")) {
            params.setDatabase(InputParams.TARGETDB);
            params.setProgram("blastp");
        } else if (dbToSearch.contains("EMBL")) {
            params.setDatabase(InputParams.EMBL);
            params.setProgram("blastn");
        }
        params.setEmail("pims-defects@dl.ac.uk"); //TODO take this from pims.xml
        //TODO params.setFilter(NOFILTER); 
        // params.setExp(1.0f); //to set E value to 1.0 default is 10
        //TODO params.setNumal("200"); // to limit number of alignments retrieved
        // default is 50
        //TODO params.setScores("200"); // to limit number of hits retrieved default is
        // 50

        //input.setType("sequence");
        final String seq = /* ">target_protName\r\n" + */target_protSeq;

        final NCBIBlast service = new NCBIBlast();
        // WUBlast service = new WUBlast();
        result = service.getBlastXml(params, seq);
        return result;
    }
}
