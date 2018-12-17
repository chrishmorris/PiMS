/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet.upload;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.tools.ImageXmlLoader;

/**
 * 
 * @author Bill Lin
 * @version
 */
@Deprecated
// does not work, due to change from Imager to Location
public class InspectionUpload extends XmlUpload {

    /**
     * 
     */
    private static final long serialVersionUID = 7643062815567251311L;

    @Override
    protected String save(DataStorage dataStorage, java.io.InputStream uploadedStream)
        throws BusinessException {
        return new ImageXmlLoader(uploadedStream, dataStorage).save();
    }

    @Override
    protected String makePath(String name) {
        String path = "/Inspection?name=" + name;
        return path;
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "screen upload";
    }
    // </editor-fold>
}
