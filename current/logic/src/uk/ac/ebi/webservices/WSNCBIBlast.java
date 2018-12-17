/**
 * WSNCBIBlast.java
 * 
 * This file was originally auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT)
 * WSDL2Java emitter. Then it was edited by Chris, when the EBI web services changed.
 */

package uk.ac.ebi.webservices;

public interface WSNCBIBlast {

    /**
     * Submit an NCBI BLAST analysis job (see
     * http://www.ebi.ac.uk/Tools/webservices/services/ncbiblast#runncbiblast_params_content).
     */
    public java.lang.String runNCBIBlast(InputParams params, String sequence);

    /**
     * Get the status of a submitted job (see
     * http://www.ebi.ac.uk/Tools/webservices/services/ncbiblast#checkstatus_jobid).
     */
    public java.lang.String checkStatus(java.lang.String jobid);

    /**
     * Get the results of a job (see
     * http://www.ebi.ac.uk/Tools/webservices/services/ncbiblast#poll_jobid_type).
     */
    public String poll(java.lang.String jobid, java.lang.String type);

}
