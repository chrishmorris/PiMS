package org.pimslims.servlet.target;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.bioinf.targets.TargetFromFasta;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65 //
 */
public class CreateTargetsForGroup extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        throw new ServletException("Get is not supported by: " + this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            // used first form on page, selected a experiment type
            super.doPost(request, response);
            return;
        }

        // used second form on page, uploading an xml file   
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            boolean isProtein = false; // default - nothing returned if the checkbox is off
            final Collection<FileItem> files = ListFiles.getFileItems(request);
            InputStream in = new ByteArrayInputStream("".getBytes("UTF8"));
            final TargetGroup group = version.get(request.getParameter("targetGroup"));
            for (final Iterator iterator = files.iterator(); iterator.hasNext();) {
                final FileItem item = (FileItem) iterator.next();
                if (item.isFormField()) {
                    if (item.getFieldName().startsWith("_")) {
                        continue; // special parameter, e.g. _anchor
                    }
                    if (item.getFieldName().equals("isProtein")) {
                        isProtein = true;
                    }
                    continue;
                }
                in = item.getInputStream();
            }
            final Collection<Target> targets =
                new TargetFromFasta(version, group.get_Owner()).save(in, isProtein);
            // TODO group.add(TargetGroup.PROP_TARGETS, targets);
            for (final Iterator iterator = targets.iterator(); iterator.hasNext();) {
                final Target target = (Target) iterator.next();
                group.addTarget(target);
            }
            //TODO item.delete();
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response);
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
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
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Add targets to group";
    }

}
