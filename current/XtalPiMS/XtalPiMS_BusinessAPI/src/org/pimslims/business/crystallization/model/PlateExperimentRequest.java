/**
 * PlateExperimentRequest.java
 * 
 * Created on 2 Oct 2007
 * 
 * Author seroul
 */
package org.pimslims.business.crystallization.model;

import java.util.Calendar;

import org.pimslims.business.Link;
import org.pimslims.business.core.model.Sample;

/**
 * <p>
 * Details about a plate experiment request
 * </p>
 * <p>
 * A request concern a sample (if same sample at different concentration create another
 * PlateExperimentRequest).
 * </p>
 * <p>
 * The sample have an owner, a contact, a desire run date, informations about screens, safety...
 * </p>
 * <p>
 * Some of this request details can be modified or deleted by the owner (or the admin).
 * </p>
 * 
 * @author seroul
 */
@Deprecated
// seems to be unused
public class PlateExperimentRequest {
    /**
     * A unique identifier
     */
    private long id = -1l;

    /**
     * The Sample concerning by this request.
     */
    private Sample sample = null;

    /**
     * The list of Screen concerning by this request. TODO FIX ME!!!
     */
    private Screen screen = null;

    /**
     * The Date of the request
     */
    private Calendar requestDate = null;

    /**
     * The desired Date for the run
     */
    private Calendar runDate = null;

    /**
     * The run protocol - should refer to a standard PIMS protocol
     */
    private String protocol = "";

    /**
     * Link through to the core PIMS Protocol that is requested for this plate experiment
     */
    private Link protocolLink = null;

    /**
     * Comments from user
     */
    private String comments = "";

    /**
     * Creates a new instance of a PlateExperimentRequest
     */
    public PlateExperimentRequest() {

    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the samples
     */
    public Sample getSample() {
        return sample;
    }

    /**
     * @param sample the samples to set
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }

    /**
     * @return the requestDate
     */
    public Calendar getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(Calendar requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @return the runDate
     */
    public Calendar getRunDate() {
        return runDate;
    }

    /**
     * @param runDate the runDate to set
     */
    public void setRunDate(Calendar runDate) {
        this.runDate = runDate;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Link getProtocolLink() {
        return protocolLink;
    }

    public void setProtocolLink(Link protocolLink) {
        this.protocolLink = protocolLink;
    }
}
