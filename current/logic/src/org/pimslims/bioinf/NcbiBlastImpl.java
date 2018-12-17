/**
 * pims-web org.pimslims.bioinf NcbiBlastImpl.java
 * 
 * @author cm65
 * @date 23 Sep 2010
 * 
 *       Protein Information Management System
 * @version: 4.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ac.ebi.webservices.InputParams;
import uk.ac.ebi.webservices.WSNCBIBlast;

/**
 * NcbiBlastImpl
 * 
 * @see http://www.ebi.ac.uk/Tools/webservices/services/sss/ncbi_blast_rest
 * @see http://www.ebi.ac.uk/Tools/sss/ncbiblast/
 * 
 */
public class NcbiBlastImpl implements WSNCBIBlast {

    NcbiBlastImpl() {
        super();
    }

    public static final String RUNNING = "RUNNING";

    public static final String FINISHED = "FINISHED";

    public static final String NOT_FOUND = "NOT_FOUND";

    public static final String ERROR = "ERROR";

    /**
     * NcbiBlastImpl.checkStatus
     * 
     * @return one of the statuses above
     * @see uk.ac.ebi.webservices.WSNCBIBlast#checkStatus(java.lang.String)
     */
    public String checkStatus(final String jobid) {
        try {
            return NcbiBlastImpl.get("http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/status/" + jobid);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * NcbiBlastImpl.getResult
     * 
     * @return one of the statuses above
     * @see uk.ac.ebi.webservices.WSNCBIBlast#checkStatus(java.lang.String)
     */
    public String getResult(final String jobid, final String type) {
        String t = "/out";
        if (NCBIBlast.TOOL_XML.equals(type)) {
            t = "/xml";
        }
        try {
            return NcbiBlastImpl
                .get("http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/result/" + jobid + t);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * NcbiBlastImpl.get
     * 
     * @param string
     * @return
     */
    public static String get(final String uri) throws IOException {
        try {
            final URL url = new URL(uri);
            final InputStream is = url.openStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            final StringBuffer ret = new StringBuffer();
            while (true) {
                final String line = reader.readLine();
                if (null == line) {
                    break;
                }
                ret.append(line + "\n");
            }
            is.close();
            System.out.println("Got: " + url.toString());
            return ret.toString().trim();
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * NcbiBlastImpl.poll TODO set time limit
     * 
     * @see uk.ac.ebi.webservices.WSNCBIBlast#poll(java.lang.String, java.lang.String)
     */
    public String poll(final String jobid, final String type) {
        long delay = 3000L; // the EBI rarely replies before 3s
        while (true) {
            try {
                Thread.sleep(delay);
            } catch (final InterruptedException e) {
                break;
            }
            final String status = this.checkStatus(jobid);
            if (NcbiBlastImpl.RUNNING.equals(status)) {
                delay = delay + delay / 2; // don't spam the EBI
                continue;
            }
            if (NcbiBlastImpl.FINISHED.equals(status)) {
                break;
            }
            throw new RuntimeException("Blast failed for job: " + jobid + " status: " + status);
        }
        return this.getResult(jobid, type);
    }

    /**
     * NcbiBlastImpl.runNCBIBlast
     * 
     * @return job id
     * @see uk.ac.ebi.webservices.WSNCBIBlast#runNCBIBlast(uk.ac.ebi.webservices.InputParams,
     *      uk.ac.ebi.webservices.Data[])
     */
    public String runNCBIBlast(final InputParams params, final String sequence) {
        final Map<String, String> parms = new HashMap();
        parms.put("email", params.getEmail());
        parms.put("program", params.getProgram());
        parms.put("matrix", params.getMatrix());
        parms.put("alignments", params.getNumal());
        parms.put("scores", params.getScores());

        //TODO parms.put("exp", params.getExp());

        if (null != params.getDropoff()) {
            parms.put("dropoff", params.getDropoff().toString());
        }
        Float ratio = 1f;
        if (null != params.getMatch() && null != params.getMismatch()) {
            ratio = -(float) params.getMatch() / (float) params.getMismatch();
            parms.put("match_scores", ratio.toString());
        }
        if (null != params.getOpengap()) {
            parms.put("gapopen", params.getOpengap().toString());
        }
        if (null != params.getExtendgap()) {
            parms.put("gapext", params.getExtendgap().toString());
        }
        //TODO parms.put("filter", params.getFilter());
        parms.put("gapalign", params.getGapalign()); // "true" or "false"
        if (null != params.getAlign()) {
            parms.put("align", params.getAlign().toString());
        }
        parms.put("stype", params.getSequenceType());
        parms.put("database", params.getDatabase());

        parms.put("sequence", sequence);
        //parms.put("seqrange",  ""+start+"-"+ end);

        return this.post("http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/run/", parms);
    }

    /**
     * NcbiBlastImpl.post
     * 
     * @param string
     * @param parms
     * @return
     */
    private String post(final String url, final Map<String, String> parms) {
        String ret = "";
        try {
            StringBuffer data = new StringBuffer();
            for (final Iterator iterator = parms.keySet().iterator(); iterator.hasNext();) {
                final String name = (String) iterator.next();
                if (null != parms.get(name)) {
                    data.append(URLEncoder.encode(name, "UTF-8") + "="
                        + URLEncoder.encode(parms.get(name), "UTF-8") + "&");
                }
            }
            if (0 < data.length()) {
                // trim final &
                data = data.deleteCharAt(data.length() - 1);
            }

            // Send data 
            System.out.println("Post to: " + url + "\n" + data);
            final URLConnection conn = new URL(url).openConnection();
            conn.setDoOutput(true);
            final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            ret = "";
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                ret = ret + line + "\n";
            }

            wr.close();
            rd.close();
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            if (e.getMessage().startsWith("Server returned HTTP response code: 400")) {
                throw new RuntimeException("Bad request", e);
            } else {
                throw new RuntimeException(e);
            }
        }
        return ret.trim();
    }
}
