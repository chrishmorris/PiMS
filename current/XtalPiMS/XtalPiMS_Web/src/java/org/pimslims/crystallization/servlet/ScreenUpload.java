/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.jdom.JDOMException;
import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.refdata.ScreenUtility;
import org.pimslims.crystallization.tools.ScreenCsvLoader;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.servlet.ListFiles;

/**
 * 
 * @author Bill Lin
 * @version
 */
@WebServlet("/update/ScreenUpload")
public class ScreenUpload extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7643062815567251311L;

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processPostRequest(request, response);
    }

    private void processPostRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        DataStorage dataStorage = null;
        try {

            dataStorage = openResources(request);
            // get info from file items
            final java.util.Collection<FileItem> items = ListFiles.getFileItems(request);

            String screenName = "";
            java.io.InputStream uploadedStream = null;
            for (final FileItem item : items) {

                if (item.isFormField()) {
                    continue;
                }
                uploadedStream = item.getInputStream();
                String name = item.getName();
                if (name.endsWith(".csv")) {
                    screenName = name.substring(0, name.length() - ".csv".length());
                    ScreenCsvLoader loader =
                        ScreenCsvLoader.getLoader(dataStorage, uploadedStream, screenName);
                    Screen screen = loader.load();
                    screen.setName(screenName);
                    dataStorage.getScreenService().create(screen);
                } else if (name.endsWith(".xml")) {

                    final java.io.Reader reader = new java.io.InputStreamReader(uploadedStream);
                    WritableVersion wv = ((DataStorageImpl) dataStorage).getWritableVersion();
                    final ScreenUtility screenUtil = new ScreenUtility(wv);
                    RefHolder screen = screenUtil.loadFile(reader);
                    assert (2 == screen.getHolderCategories().size()) : "Holder categories should include Screen";
                    screenName = screen.getName();
                } else {
                    throw new ServletException("Sorry, PiMS can only load xml and csv files, not file: "
                        + name);
                }
                break;
            }

            dataStorage.commit();
            this.redirect(response, request.getContextPath() + "/update/EditScreen/" + screenName);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final JDOMException e) {
            throw new ServletException(e);
        } catch (final BusinessException e) {
            throw new ServletException(e);
        } finally {
            this.closeResources(dataStorage);
        }
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "screen upload";
    }

    // </editor-fold>

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        throw new ServletException("Get is not supported by: " + this.getClass().getCanonicalName());
    }
}
