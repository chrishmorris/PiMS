/**
 * V2_2-pims-web org.pimslims.servlet FileUploadProgress.java
 * 
 * @author pvt43
 * @date 26 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.text.NumberFormat;

import org.apache.commons.fileupload.ProgressListener;

/**
 * FileUploadProgress
 * 
 */
@Deprecated
// suspected obsolete
public class FileUploadProgress implements ProgressListener {

    private long bytesTransferred = -1;

    private long fileSize = -10;

    public String getProgress() {
        final double per = (double) this.bytesTransferred / (double) this.fileSize;
        final NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        return format.format(per * 100);
    }

    public void update(final long pBytesRead, final long pContentLength, final int pItems) {

        if (this.bytesTransferred == pContentLength / 102400) {
            return;
        }
        this.bytesTransferred = pBytesRead / 102400;
        if (this.fileSize == -10) {
            this.fileSize = pContentLength / 102400;
        }

        System.out.println("We are currently reading item " + pItems);
        if (pContentLength == -1) {
            System.out.println("So far, " + this.bytesTransferred + " hundred kilobytes have been read.");
        } else {
            System.out.println("So far, " + this.bytesTransferred + " hundred of " + pContentLength / 102400
                + " hundred kilobytes have been read.");

        }
    }
}
