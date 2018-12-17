/**
 * 
 */
package org.pimslims.servlet.holder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.sample.HolderContents;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ViewHolderContents extends PIMSServlet {

    private static final String HOLDER_0D_FORWARD_URL = "/JSP/holder/HolderContents0d.jsp";

    private static final String HOLDER_1D_FORWARD_URL = "/JSP/holder/HolderContents1d.jsp";

    private static final String HOLDER_2D_FORWARD_URL = "/JSP/holder/HolderContents2d.jsp";

    private static final String HOLDER_3D_FORWARD_URL = "/JSP/holder/HolderContents3d.jsp";

    /**
     * 
     */
    public ViewHolderContents() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of an holder";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Holder holder = (Holder) version.get(pathInfo); // e.g.
            if (null == holder) {
                this.writeErrorHead(request, response, "Holder not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("head", new Boolean(true));
            final ModelObjectBean bean = BeanFactory.newBean(holder);
            request.setAttribute("bean", bean);
            request.setAttribute("ObjName", bean.getClassDisplayName());
            request.setAttribute("mayUpdate",
                new Boolean(true && bean.getMayUpdate()));

            final HolderType holderType = (HolderType) holder.getHolderType();
            if (null != holderType) {
                request.setAttribute("holderType", BeanFactory.newBean(holderType));
            }

            final int dimension = HolderContents.getDimensions(holder);
            System.out.println("ViewHolder.getForward dimension [" + dimension + "]");
            String forward;

            switch (dimension) {

                case 0:
                    forward = ViewHolderContents.HOLDER_0D_FORWARD_URL;
                    request.setAttribute("contentArray", this.get1DContent(holder, holderType));
                    break;

                case 1:
                    forward = ViewHolderContents.HOLDER_1D_FORWARD_URL;
                    request.setAttribute("contentArray", this.get1DContent(holder, holderType));
                    break;

                case 2:
                    forward = ViewHolderContents.HOLDER_2D_FORWARD_URL;
                    request.setAttribute("contentArray", this.get2DContent(holder, holderType));
                    break;

                case 3:
                    forward = ViewHolderContents.HOLDER_3D_FORWARD_URL;
                    request.setAttribute("contentArray", this.get3DContent(holder, holderType));
                    break;

                default:
                    forward = ViewHolderContents.HOLDER_1D_FORWARD_URL;
                    break;
            }

            response.setStatus(HttpServletResponse.SC_OK);

            System.out.println("ViewHolder.doGet forward [" + forward + "]");
            final RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
            dispatcher.forward(request, response);
            version.commit();

        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }

    }

    private ModelObjectShortBean[] get1DContent(final Holder holder, final HolderType holderType) {

        Integer rows = null;
        if (null != holderType) {
            rows = holderType.getMaxRow();
        }
        final List<AbstractHolder> subHolders = new ArrayList(holder.getSubHolders());
        Collections.sort(subHolders);
        Collections.reverse(subHolders);
        final List<Sample> samples = new ArrayList(holder.getSamples());
        Collections.sort(samples);
        Collections.reverse(samples);
        if (null == rows) {
            // uknown size, so don't use row indices
            rows = subHolders.size() + samples.size();
            final ModelObjectShortBean[] content = new ModelObjectShortBean[rows];
            int row = 0;
            for (final AbstractHolder subHolder : subHolders) {
                content[row++] = new ModelObjectShortBean(subHolder);
            }
            for (final Sample sample : samples) {
                content[row++] = new ModelObjectShortBean(sample);
            }
            return content;
        }
        final ModelObjectShortBean[] content = new ModelObjectShortBean[rows];

        for (final AbstractHolder subHolder : subHolders) {
            content[subHolder.getRowPosition() - 1] = BeanFactory.newBean(subHolder);
        }

        for (final Sample sample : samples) {
            content[sample.getRowPosition() - 1] = BeanFactory.newBean(sample);
        }

        return content;
    }

    private ModelObjectShortBean[][] get2DContent(final Holder holder, final HolderType holderType) {

        final ModelObjectShortBean[][] content =
            new ModelObjectShortBean[holderType.getMaxRow()][holderType.getMaxColumn()];

        for (final AbstractHolder subHolder : holder.getSubHolders()) {
            content[subHolder.getRowPosition() - 1][subHolder.getColPosition() - 1] =
                BeanFactory.newBean(subHolder);
        }

        for (final Sample sample : holder.getSamples()) {
            // note that there are some plate experiments done in 4*24 well plates
            // the samples are numbered as if in one 96 well plate
            final int row = (sample.getRowPosition() - 1) % holderType.getMaxRow();
            final int column = (sample.getColPosition() - 1) % holderType.getMaxColumn();
            content[row][column] = new ModelObjectShortBean(sample);
        }

        return content;
    }

    private ModelObjectShortBean[][][] get3DContent(final Holder holder, final HolderType holderType) {

        final ModelObjectShortBean[][][] content =
            new ModelObjectShortBean[holderType.getMaxRow()][holderType.getMaxColumn()][holderType
                .getMaxSubPosition()];

        for (final AbstractHolder subHolder : holder.getSubHolders()) {
            content[subHolder.getRowPosition() - 1][subHolder.getColPosition() - 1][subHolder
                .getSubPosition() - 1] = new ModelObjectShortBean(subHolder);
        }

        for (final Sample sample : holder.getSamples()) {
            content[sample.getRowPosition() - 1][sample.getColPosition() - 1][sample.getSubPosition() - 1] =
                new ModelObjectShortBean(sample);
        }

        return content;
    }
}
