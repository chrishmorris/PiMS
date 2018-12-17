package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FlushMode;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.ExperimentGroupWriter;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.experiment.CreateExperimentOld;

/**
 * Servlet implementation class for Servlet: CreatePlate
 * 
 */
public class CreateExperimentGroup extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "record a group pof experiments";
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            final String typeHook = request.getParameter("experimentType");
            ModelObject experimentType = null;
            if (null != typeHook) {
                experimentType = rv.get(typeHook);
                request.setAttribute("experimentType", BeanFactory.newBean(experimentType));
            }

            // for Ajax List
            //TODO give this request a better URL, and stop using XML
            //TODO also pass out of use protocols
            if (null != request.getParameter("isAJAX")
                && "true".equals(request.getParameter("isAJAX").toString())) {
                final Collection results = CreateExperimentGroup.getProtocols(rv, request, experimentType);
                response.setContentType("text/xml");
                final XMLOutputter xo = new XMLOutputter();
                xo.output(CreateExperimentGroup.makeAjaxListXML(results), response.getWriter());
                return;
            }

            final List<ModelObjectBean> experimentTypes =
                ModelObjectBean.getModelObjectBeans((CreateExperimentGroup
                    .activeExperimentTypes(CreateExperimentGroup.activeProtocolsForGroups(PIMSServlet.getAll(
                        rv, org.pimslims.model.protocol.Protocol.class)))));
            Collections.sort(experimentTypes);
            request.setAttribute("experimentTypes", experimentTypes);

            // protocol supplied parameter for protocol
            if (null != request.getParameter("protocol")) {
                final Protocol protocol = rv.get(request.getParameter("protocol"));
                request
                    .setAttribute("experimentType", new ModelObjectShortBean(protocol.getExperimentType()));
                request.setAttribute("protocol", protocol);
            }

            if (null != request.getParameter("refInputSample")) {
                // it's a follow on 
                request.setAttribute("refInputSample", rv.get(request.getParameter("refInputSample")));
            }
            if (null != request.getParameter("inputGroup")) { //TODO shouldn't this be inputGroup?
                // it's a follow on 
                request.setAttribute("inputGroup", rv.get(request.getParameter("inputGroup")));
            }

            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(rv));

            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/plateExperiment/CreateExperimentGroup.jsp");
            rd.forward(request, response);
            rv.commit();
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("org.pimslims.servlet.plateExperiment.CreateExperimentGroup");
        final Map<String, String> parms = PIMSServlet.getParameterMap(request);

        String inputString = null;
        if (!"text/html".equals(request.getContentType())) {

            final Map<String, String> uploadedFiles = SpreadsheetGetter.getSpreadsheet(request, parms);
            if (!uploadedFiles.isEmpty()) {
                final Map.Entry<String, String> entry = uploadedFiles.entrySet().iterator().next();
                inputString = entry.getValue();
            }
        }

        final String name = parms.get("groupName");
        int numberOfExperiments = 0;
        final String stringNumExps = parms.get("numExperiments");
        try {
            numberOfExperiments = Integer.parseInt(stringNumExps);
        } catch (final NumberFormatException e1) {
            this.writeErrorHead(request, response, "Bad number of experiments: " + stringNumExps,
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (parms.containsKey("GetSpreadsheet")) {
            final String url =
                request.getContextPath() + "/read/CreateExperimentGroupCsv/" + parms.get("protocol") + "/"
                    + name + ".csv?groupName=" + name + "&numExperiments=" + numberOfExperiments;
            PIMSServlet.redirectPost(response, url);
            return;
        }
        if (!this.checkStarted(request, response)) {
            return;
        }
        // TODO no, use ValueFormatter
        final Calendar startDate = java.util.Calendar.getInstance();
        final Calendar endDate = java.util.Calendar.getInstance();
        final String start = parms.get("startDate");
        final String end = parms.get("endDate");
        try {
            if (null != start && !"".equals(start)) {
                final long time = Long.parseLong(start);
                startDate.setTimeInMillis(time);
            }
            if (null != end && !"".equals(end)) {
                final long time = Long.parseLong(end);
                endDate.setTimeInMillis(time);
            }

        } catch (final NumberFormatException e) {
            throw new ServletException(e);
        }

        //String plateName = parms.get("plateName");
        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }
        // to improve performance
        version.getSession().setFlushMode(FlushMode.COMMIT); //TODO version.setFlushModeCommit();

        try {
            final Protocol protocol = (Protocol) version.get(parms.get("protocol"));
            if (null == protocol) {
                throw new AssertionError("No such protocol: " + parms.get("protocol"));
            }
            final Collection<ExperimentType> experimentTypes =
                Collections.singleton(protocol.getExperimentType());

            final LabNotebook access = (LabNotebook) version.get(parms.get(PIMSServlet.LAB_NOTEBOOK_ID));
            if (null == access) {
                throw new AssertionError("No such Lab Notebook: " + parms.get(PIMSServlet.LAB_NOTEBOOK_ID));
            }

            final String details = parms.get("details");
            final String risHook = parms.get("refInputSample");
            final String inputGroupHook = parms.get("inputGroup");
            final ExperimentGroup group =
                CreateExperimentGroup.createGroup(inputString, startDate, endDate, numberOfExperiments,
                    version, protocol, experimentTypes, access, name, details, risHook, inputGroupHook);

            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + group.get_Hook());

        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static Document makeAjaxListXML(final Collection<ModelObject> results) {
        final Element rootElement = new Element("list");
        for (final ModelObject object : results) {
            rootElement.addContent(CreateExperimentOld.makeAjaxStringXML("object", object.get_Name(),
                object.get_Hook()));
        }
        final Document xml = new Document(rootElement);
        return xml;
    }

    /**
     * CreateExperimentOld.getProtocols
     * 
     * @param version
     * @param request
     * @param experimentType
     * @return
     */
    public static Collection getProtocols(final ReadableVersion version, final HttpServletRequest request,
        final ModelObject experimentType) {
        final Collection results;
        if (null != request.getParameter("isPLATE")) {
            results =
                CreatePlate.activeProtocolsForPlates(version.findAll(Protocol.class,
                    Protocol.PROP_EXPERIMENTTYPE, experimentType));

        } else if (null != request.getParameter("isGROUP")) {
            results =
                CreateExperimentGroup.activeProtocolsForGroups(version.findAll(Protocol.class,
                    Protocol.PROP_EXPERIMENTTYPE, experimentType));

        } else {
            results =
                CreateExperimentOld.activeProtocols(version.findAll(Protocol.class,
                    Protocol.PROP_EXPERIMENTTYPE, experimentType));
        }
        return results;
    }

    public static ExperimentGroup createGroup(final String csvAsString, final Calendar startDate,
        final Calendar endDate, final int numberOfExperiments, final WritableVersion version,
        final Protocol protocol, final Collection<ExperimentType> experimentTypes, final LabNotebook access,
        final String name, final String details, final String risHook, final String inputGroupHook)
        throws AccessException, ConstraintException, UnsupportedEncodingException, IOException {
        final ExperimentGroup group =
            HolderFactory.createExperimentGroup(version, access, name, "experiment group", experimentTypes,
                startDate, endDate, details, protocol, numberOfExperiments);

        if (null != csvAsString) {
            final ExperimentGroupWriter gw = new ExperimentGroupWriter(version, group);
            final InputStreamReader reader = new InputStreamReader(CaliperFile.parseStringToIS(csvAsString));
            gw.setValuesFromSpreadSheetForCreate(reader);
        }

        // copy samples
        if (null != risHook && null != inputGroupHook) {
            final RefInputSample refInputSample = (RefInputSample) version.get(risHook);
            final ExperimentGroup inputGroup = (ExperimentGroup) version.get(inputGroupHook);
            HolderFactory.multiLinePipette(version, inputGroup, group, refInputSample.getName(), null);
        }
        return group;
    }

    /**
     * 
     * CreateExperimentGroup.activeExperimentTypes
     * 
     * @param protocols
     * @return
     * @throws ServletException
     */
    public static Collection<ModelObject> activeExperimentTypes(final Collection<Protocol> protocols)
        throws ServletException {

        final Set<ModelObject> experimentTypes = new TreeSet<ModelObject>();
        for (final ModelObject object : protocols) {
            final Protocol protocol = (Protocol) object;
            experimentTypes.add(protocol.getExperimentType());
        }
        return experimentTypes;
    }

    //TODO do this in HQL, that will be quicker
    public static Collection<Protocol> activeProtocolsForGroups(final Collection<Protocol> protocols) {
        final Collection<Protocol> collection = new HashSet();
        for (final Protocol p : protocols) {
            if (null == p.getIsForUse() || p.getIsForUse() /*&& 1 == p.getRefOutputSamples().size()*/) {
                collection.add(p);
            }
        }
        return collection;
    }
}
