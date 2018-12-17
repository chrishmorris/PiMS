package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.View;

/**
 * Plate
 * 
 * TODO obsolete, Ed has started developing the new plate experiment implementation
 * 
 */
@Deprecated
public class Plate extends PIMSServlet {

    /**
     * 
     */
    private static final String FORWARD_URL = "/JSP/plateExperiment/Plate.jsp";

    private String forwardURL = Plate.FORWARD_URL;

    public Plate() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a plate";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final ModelObject object = this.getRequiredObject(version, request, response, pathInfo);
            if (null == object) {
                return; // 404 page already shown
            }

            // for scoreplate view
            if (null != request.getParameter("parameterDefinition")) {
                final ModelObject pDef =
                    this.getRequiredObject(version, request, response,
                        request.getParameter("parameterDefinition"));
                request.setAttribute("parameterDefinition", pDef);
            }

            Holder holder;
            ExperimentGroup group;
            Protocol protocol = null;
            if (object instanceof Holder) {
                holder = (Holder) object;
                group = HolderFactory.getExperimentGroup(holder);
                protocol = HolderFactory.getProtocol(holder);
            } else {
                group = (ExperimentGroup) object;
                holder = HolderFactory.getPlate(group);
                protocol = group.getExperiments().iterator().next().getProtocol();
            }

            if (null == protocol || group == null) {
                final String className = object.get_MetaClass().getMetaClassName();
                version.abort();
                View.showExpertView(request, response, className);
                return;
            }

            final PlateReader reader = new PlateReader(version, group, null);

            final HttpSession session = request.getSession();
            if (null != session.getAttribute("_tab")) {
                request.setAttribute("_tab", session.getAttribute("_tab"));
            }

            // TODO This block shouldn't be hard-coded
            final List<String> plates0 = new ArrayList<String>();
            if (null == holder) {
                plates0.add("");
            } else {
                plates0.add(holder.get_Name());
            }
            final List<List<String>> plateIDs = new ArrayList<List<String>>();
            plateIDs.add(plates0);
            request.setAttribute("plateIDs", plateIDs);

            // List<String> owners = reader.getOwners();
            request.setAttribute("group", group);
            request.setAttribute("holder", holder);

            request.setAttribute("currentLocation", SampleLocationBean.getCurrentLocation(holder));
            request.setAttribute("currentLocationTrail", SampleLocationBean.getCurrentLocationTrail(holder));

            /* for order view
            if (holder != null) {
                if (OrderUtility.isPlateOrder(holder)) {
                    request.setAttribute("holderOrder", holder);
                }
            } */

            int numRows = 8;
            int numCols = 12;
            if (null != holder) {
                numRows = HolderFactory.getRows(holder).size();
                numCols = HolderFactory.getColumns(holder).size();
            }
            final String[] rows = new String[numRows];
            final String[] cols = new String[numCols];
            System.arraycopy(HolderFactory.ROWS, 0, rows, 0, numRows);
            System.arraycopy(HolderFactory.COLUMNS, 0, cols, 0, numCols);

            // TODO This block shouldn't be hard-coded
            request.setAttribute("layout", "1x" + numCols + "x" + numRows); // 1x12x8
            request.setAttribute("rows", rows);
            request.setAttribute("cols", cols);

            final List<RefInputSample> refInputSamples =
                new ArrayList<RefInputSample>(protocol.getRefInputSamples());
            final List<ParameterDefinition> parameterDefinitions =
                new ArrayList<ParameterDefinition>(protocol.getParameterDefinitions());
            Collections.sort(parameterDefinitions, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            Collections.sort(refInputSamples, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            request.setAttribute("parameterDefinitions", parameterDefinitions);
            request.setAttribute("refInputSamples", refInputSamples);

            request.setAttribute("protocol", protocol);
            request.setAttribute("experiments", reader.getExperiments(this.getOrder()));// slow
            request.setAttribute("annotations", reader.getAnnotations());
            request.setAttribute("samples", reader.getSamples());
            request.setAttribute("mayUpdate", group.get_MayUpdate());
            // TODO for header <jsp:param name="mayUpdate" value='${mayUpdate}'
            // />
            request.setAttribute("details", reader.getDetails());
            /*final SimpleDateFormat df = Utils.getDateFormat();
            request.setAttribute("startDate", ValueFormatter.formatDate(reader.getStartDate()));
            request.setAttribute("endDate", ValueFormatter.formatDate(reader.getEndDate())); */

            // find possible next experiments
            final RefOutputSample out = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES);
            //request.setAttribute("refInputSamplesForNextExpt", this.getRefInputSamplesForNextExpt(out));
            request.setAttribute("refInputBeans", PlateExperimentDAO.getRefInputBeans(out));

            final RequestDispatcher dispatcher = request.getRequestDispatcher(this.forwardURL);
            dispatcher.forward(request, response);
            version.commit();
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

    protected Comparator getOrder() {
        return new WellExperimentBean.ColumnOrder();
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

        throw new UnsupportedOperationException(this.getClass().getName() + " does not accept POST");

    }

    /**
     * @return Returns the forwardURL.
     */
    private String getForwardURL() {
        return this.forwardURL;
    }

    /**
     * @param forwardURL The forwardURL to set.
     */
    protected void setForwardURL(final String forwardURL) {
        this.forwardURL = forwardURL;
    }

}
