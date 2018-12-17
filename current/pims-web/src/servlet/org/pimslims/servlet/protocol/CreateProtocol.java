package org.pimslims.servlet.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.Create;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.xmlio.ProtocolXmlConvertor;
import org.xml.sax.SAXException;

/**
 * @author cm65 //
 */
public class CreateProtocol extends Create {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Not ready to save, prompt for next needed input
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            // ask about experiment type
            this.experimentTypePrompt(version, request, response);

            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            // used first form on page, selected a experiment type
            super.doPost(request, response);
            return;
        }

        // used second form on page, uploading an xml file   
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final Collection<FileItem> files = ListFiles.getFileItems(request);
            InputStream in = null;
            String hook = null;
            for (final Iterator iterator = files.iterator(); iterator.hasNext();) {
                final FileItem file = (FileItem) iterator.next();

                if (file.isFormField()) {
                    if (file.getFieldName().equals("_OWNER")) {
                        final String owner = file.getString();
                        version.setDefaultOwner(owner);
                    }
                }

                if (file.getFieldName().startsWith("_")) {
                    continue; // special parameter, e.g. _anchor
                }
                in = file.getInputStream();

                //TODO file.delete();
            }
            final Protocol protocol = ProtocolXmlConvertor.convertXmlToProtocol(version, in);
            hook = protocol.get_Hook();

            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final JAXBException e) {
            throw new ServletException(e);
        } catch (final SAXException e) {
            throw new ServletException("Sorry, that is not a valid PiMS protocol file", e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * First stage: prompt for experiment type
     * 
     * @param version
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private void experimentTypePrompt(final ReadableVersion version, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, ServletException {

        final Collection<ModelObject> types =
            PIMSServlet.getAll(version, org.pimslims.model.reference.ExperimentType.class);
        request.setAttribute("experimentTypes", ModelObjectShortBean.getModelObjectShortBeans(types));
        request.setAttribute("instrumentTypes", ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet
            .getAll(version, org.pimslims.model.reference.InstrumentType.class)));

        request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

        final MetaClass metaClass = this.getModel().getMetaClass(Protocol.class.getName());
        PIMSServlet.dispatchCustomJsp(request, response, metaClass.getMetaClassName(), "create",
            this.getServletContext());
    }

    @Override
    protected String getMetaClassName(final HttpServletRequest request) {
        return Protocol.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "custom create for a protocol";
    }

}
