/**
 * xtalPiMS_Web org.pimslims.crystallization.servlet.upload XmlUpload.java
 * 
 * @author cm65
 * @date 4 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.servlet.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.XtalPIMSServlet;
import org.pimslims.servlet.ListFiles;

/*
 * XmlUpload
 */
public abstract class XmlUpload extends XtalPIMSServlet {

    /**
     * Constructor for XmlUpload 
     */
    public XmlUpload() {
        super();
    }

    protected abstract String makePath(String name);

    protected abstract String save(DataStorage dataStorage, java.io.InputStream uploadedStream) throws BusinessException;
    

    protected void processPostRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        DataStorage dataStorage = null;
        String name = null;
        try {
            dataStorage =  openResources(request);
            //get info from file items
            final java.util.Collection<FileItem> items = ListFiles.getFileItems(request);

            java.io.InputStream uploadedStream = null;
            for (final FileItem item : items) {
                if (item.isFormField()) {
                    continue;
                }
                uploadedStream = item.getInputStream();
                name = save(dataStorage, uploadedStream);
                break;
            }
            dataStorage.commit();

           
            String path = makePath(name);
            this.redirect(response, request.getContextPath() + path);
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final BusinessException e) {
            throw new ServletException(e);
        } finally {
            closeResources(dataStorage);
        }
    }


    /**
     * OrderUpload.doGet
     * @see org.pimslims.crystallization.servlet.XtalPIMSServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException("Get method not supported by: "+this.getClass().getName());
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        processPostRequest(request, response);
    }

}