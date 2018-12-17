package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PlateExperimentUpdateBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.mru.MRU;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.presentation.protocol.ProtocolBean;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.View;

/**
 * Plate
 * 
 * 
 */
public class EditPlate extends PIMSServlet {

    /**
     * 
     */
    private static final String PLATE_FORWARD_URL = "/JSP/plateExperiment/EditPlate.jsp";

    private static final String GROUP_FORWARD_URL = "/JSP/plateExperiment/EditExperimentGroup.jsp";

    private String forwardURL;

    public EditPlate() {
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

        System.out.println("-------------EditPlate: entered doGet() ---------------------------");

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

            final long timings[] = new long[10];
            timings[0] = System.currentTimeMillis();

            final ModelObject object = this.getRequiredObject(version, request, response, pathInfo);
            if (null == object) {
                return; // 404 page already shown
            }

            System.out.println("-------------Point A ---------------------------");

            // for scoreplate view
            if (null != request.getParameter("parameterDefinition")) {
                final ModelObject pDef =
                    this.getRequiredObject(version, request, response,
                        request.getParameter("parameterDefinition"));
                request.setAttribute("parameterDefinition", pDef);
            }

            System.out.println("-------------Point B ---------------------------");

            Holder holder;
            ExperimentGroup group;
            Protocol protocol = null;
            if (object instanceof Holder) {
                // obsolete
                holder = (Holder) object;
                group = HolderFactory.getExperimentGroup((Holder) object);
                protocol = HolderFactory.getProtocol((Holder) object);
            } else {
                group = (ExperimentGroup) object;
                holder = HolderFactory.getPlate(group);
                protocol = group.getExperiments().iterator().next().getProtocol();
            }

            System.out.println("-------------Point C ---------------------------");

            if (null == protocol) {
                //final String className = object.get_MetaClass().getMetaClassName();
                version.abort();
                //View.showExpertView(request, response, className);
                View.viewHolder(request, response);
                return;
            }

            timings[1] = System.currentTimeMillis();
            System.out.println("-------------Timing point 1 ---------------------------");

            Integer subPosition = null;
            if (null == request.getParameter("subPosition")) {
                if (null != holder && null != holder.getHolderType()
                    && null != ((HolderType) holder.getHolderType()).getMaxSubPosition()) {
                    subPosition = 1;
                }
            } else {
                subPosition = Integer.parseInt(request.getParameter("subPosition"));
            }

            timings[2] = System.currentTimeMillis();
            System.out.println("-------------Timing point 2 ---------------------------");

            final PlateExperimentUpdateBean updateBean = new PlateExperimentUpdateBean(group, subPosition);
            assert 0 < updateBean.getExperimentBeans().size();
            request.setAttribute("updateBean", updateBean);
            request.setAttribute("subPosition", subPosition);

            /*
            if (reader.isPlateExperiment()) {
                final Collection<Holder> holders = HolderFactory.getHolders(group);
                for (final Holder plate : holders) {
                    request.setAttribute(HolderFactory.getHolderPoint(plate) + "Name", plate.getName());
                    request.setAttribute(HolderFactory.getHolderPoint(plate) + "Hook", plate.get_Hook());
                    request.setAttribute(HolderFactory.getHolderPoint(plate),
                        updateBean.getPlateExperimentLayout(plate));
                }
            }
            */

            timings[3] = System.currentTimeMillis();

            //milestone
            final ProtocolBean protocolBean = updateBean.getProtocolBean();
            final ExperimentType type = version.get(protocolBean.getExperimentType().getHook());
            final String name = EditPlate.getMilestoneName(type);
            if (ServletUtil.validString(name)) {
                request.setAttribute("milestoneName", name);
            }

            timings[4] = System.currentTimeMillis();

            // MRU
            final String userName = PIMSServlet.getUsername(request);
            final List<MRU> sampleMRUs =
                new ArrayList<MRU>(MRUController.getMRUs(userName, Sample.class.getName()));

            final Map<String, Collection<MRU>> recentSamples = new HashMap<String, Collection<MRU>>();
            for (final RefInputSampleBean refInputSampleBean : protocolBean.getInputSamples()) {

                final Collection<MRU> mruSampleBeans = new HashSet<MRU>();
                for (final MRU mru : sampleMRUs) {
                    final Sample sample = (Sample) version.get(mru.getHook());
                    if (null != sample) {
                        // should have been removed from MRU when deleted, but let's be careful
                        for (final SampleCategory category : sample.getSampleCategories()) {
                            if (category.getDbId() == refInputSampleBean.getSampleCategory().getDbId()) {
                                mruSampleBeans.add(mru);
                            }
                        }
                    }
                }
                recentSamples.put(refInputSampleBean.getHook(), mruSampleBeans);
            }
            request.setAttribute("recentSamples", recentSamples);

            final List<MRU> targetMRUs =
                new ArrayList<MRU>(MRUController.getMRUs(userName, ResearchObjective.class.getName()));
            request.setAttribute("recentTargets", targetMRUs);

            timings[5] = System.currentTimeMillis();

            // Old code

            request.setAttribute("group", group); //TODO still one use of this in tab_nextExperiment.jsp
            request.setAttribute("groupBean", BeanFactory.newBean(group));

            if (HolderFactory.isPlateExperiment(group)) {
                //request.setAttribute("holder", holder);  
                request.setAttribute("holderBean", new ModelObjectShortBean(holder));
                request.setAttribute("holderTypeBean", BeanFactory.newBean(holder.getHolderType()));
                request.setAttribute("currentLocation", SampleLocationBean.getCurrentLocation(holder));
                request.setAttribute("currentLocationTrail",
                    SampleLocationBean.getCurrentLocationTrail(holder));
            }

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
            final List<RefOutputSample> refOutputSamples =
                new ArrayList<RefOutputSample>(protocol.getRefOutputSamples());
            final List<ParameterDefinition> parameterDefinitions =
                new ArrayList<ParameterDefinition>(protocol.getParameterDefinitions());
            Collections.sort(parameterDefinitions, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            Collections.sort(refInputSamples, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            Collections.sort(refOutputSamples, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
            request.setAttribute("parameterDefinitions", parameterDefinitions);
            request.setAttribute("refInputSamples", refInputSamples);
            request.setAttribute("refOutputSamples", refOutputSamples);

            /* request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version)); */
            request.setAttribute("mayUpdate", group.get_MayUpdate());

            timings[6] = System.currentTimeMillis();

            final PlateReader reader = new PlateReader(version, group, subPosition);
            request.setAttribute("annotations", reader.getAnnotations());
            request.setAttribute("samples", reader.getSamples());
            request.setAttribute("details", reader.getDetails());
            request.setAttribute("startDate", reader.getStartDate());
            request.setAttribute("endDate", reader.getEndDate());
            request.setAttribute("isActive", reader.getIsActive());

            // find possible next experiments
            final RefOutputSample out = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES);
            if (null == out) {
                request.setAttribute("refInputBeans", Collections.EMPTY_LIST);
            } else {
                final Collection<RefInputSampleBean> refInputBeans = EditPlate.getRefInputBeans(out);
                request.setAttribute("refInputBeans", refInputBeans);
            }

            timings[7] = System.currentTimeMillis();
            /*System.out.println("EditPlate performance: total=" + (timings[6] - timings[0]) + "ms");
            for (int t = 0; t < 7; t++) {
                System.out.println("EditPlate performance: (" + (t + 1) + "-" + t + ")="
                    + (timings[t + 1] - timings[t]) + "ms");
            } */

            request.setAttribute("protocolBean", protocolBean);
            if (HolderFactory.isPlateExperiment(group)) {
                this.forwardURL = EditPlate.PLATE_FORWARD_URL;
            } else {
                this.forwardURL = EditPlate.GROUP_FORWARD_URL;
            }

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

    //TODO do this in HQL
    //LATER do not filter if it is an experiment group
    static Collection<RefInputSampleBean> getRefInputBeans(final RefOutputSample out) {
        final Collection<RefInputSampleBean> refInputBeans = PlateExperimentDAO.getRefInputBeans(out);
        final Collection<RefInputSampleBean> ret = new ArrayList(refInputBeans.size());
        for (final Iterator iterator = refInputBeans.iterator(); iterator.hasNext();) {
            final RefInputSampleBean refInputSampleBean = (RefInputSampleBean) iterator.next();
            if (null != refInputSampleBean.getProtocol()) {
                final Protocol p =
                    (Protocol) out.get_Version().get(refInputSampleBean.getProtocol().getHook());
                if (null == p) {
                    continue;
                }
                if (null == p.getIsForUse() || p.getIsForUse() && 1 == p.getRefOutputSamples().size()) {
                    ret.add(refInputSampleBean);
                }
            }
        }
        return ret;
    }

    private static String getMilestoneName(final ExperimentType type) {

        if (null == type) {
            return null;
        }
        final Set<WorkflowItem> workflows = type.getWorkflowItems();
        if (workflows.size() != 1) {
            return null;
        }
        final WorkflowItem workflowItem = workflows.iterator().next();
        return workflowItem.getStatus().getName();
    }

}
