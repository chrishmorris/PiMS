/**
 * current-pims-web uk.ac.sspf.spot.pims ConstructReport.java
 * 
 * @author cm65
 * @date 9 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 * 
 */
package uk.ac.sspf.spot.pims;

import org.pimslims.dao.ReadableVersion;

import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * ConstructReport
 * 
 */
@Deprecated
public abstract class ConstructReport {

    /**
     * Our ReadableVersion, so we can talk to the db
     */
    private ReadableVersion rv;

    /**
     * The filter bean we should apply
     */
    protected ConstructFilterBean filter;

    /**
     * 
     */
    public ConstructReport() {
        super();
    }

    /**
     * @return Returns the filter.
     */
    public ConstructFilterBean getFilter() {
        return this.filter;
    }

    /**
     * Spefiy the filter. A null input will become a new empty filter
     * 
     * @param filter The filter to set.
     */
    public void setFilter(final ConstructFilterBean filter) {
        if (filter == null) {
            this.filter = new ConstructFilterBean();
        } else {
            this.filter = filter;
        }
    }

    /**
     * @return Returns the rv.
     */
    public ReadableVersion getReadableVersion() {
        return this.rv;
    }

    /**
     * @param rv The rv to set.
     * @throws IllegalArgumentException if null is passed in
     */
    protected void setReadableVersion(final ReadableVersion readableVersion) {
        if (readableVersion == null) {
            throw new IllegalArgumentException("readableVersion must not be null");
        }
        this.rv = readableVersion;
    }

}
