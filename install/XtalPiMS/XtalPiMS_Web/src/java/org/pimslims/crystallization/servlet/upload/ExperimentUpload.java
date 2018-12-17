/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.jdom.JDOMException;
import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.refdata.ScreenUtility;
import org.pimslims.crystallization.tools.ExperimentXmlLoader;
import org.pimslims.crystallization.tools.OrderXmlLoader;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
public class ExperimentUpload extends XmlUpload {
   
    
    @Override
    protected String save(DataStorage dataStorage, java.io.InputStream uploadedStream) throws BusinessException {
        return new ExperimentXmlLoader(uploadedStream, dataStorage ).save();
    }

    @Override
    protected String makePath(String name) {
        String path = "/ViewPlate.jsp?barcode=" + name;
        return path;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7643062815567251311L;

       /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "upload experiment details";
    }
 
}
