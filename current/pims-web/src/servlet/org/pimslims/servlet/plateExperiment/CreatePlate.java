package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.HolderTypeBean;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PlateExperimentUpdateBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.experiment.ExperimentGroupWriter;
import org.pimslims.presentation.plateExperiment.DefaultPlateName;
import org.pimslims.presentation.plateExperiment.PlateNameFactory;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.experiment.CreateExperimentOld;

/**
 * Servlet implementation class for Servlet: CreatePlate
 * 
 */
public class CreatePlate extends PIMSServlet {

    //TODO what about 384?
    private static final Map<String, Object> NINETY_SIX = new HashMap<String, Object>();
    static {
        CreatePlate.NINETY_SIX.put(HolderType.PROP_MAXCOLUMN, new Integer(12));
        CreatePlate.NINETY_SIX.put(HolderType.PROP_MAXROW, new Integer(8));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Set and get the values to/from each well in the plate";
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.plateExperiment.CreatePlate");
        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }

        try {
            if (null != request.getParameter("experimentProtocolHook")) {
                // already got experiment type and protocol, suggest a group name
                this.groupNamePrompt(rv, request, response);

            } else {
                final List<ModelObjectBean> experimentTypes =
                    ModelObjectBean.getModelObjectBeans(CreateExperimentGroup
                        .activeExperimentTypes(CreatePlate.activeProtocolsForPlates(PIMSServlet.getAll(rv,
                            org.pimslims.model.protocol.Protocol.class))));
                Collections.sort(experimentTypes);
                request.setAttribute("experimentTypes", experimentTypes);

                final Collection<HolderType> types = rv.findAll(HolderType.class, Collections.EMPTY_MAP);
                request.setAttribute("holderTypes", CreatePlate.holderTypesForPlates(types));

                request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(rv));

                // protocol supplied parameter for experimentType
                if (null != request.getParameter("experimentType")) {
                    final ExperimentType experimentType = rv.get(request.getParameter("experimentType"));
                    request.setAttribute("experimentType", experimentType);
                }

                // protocol supplied parameter for protocol
                if (null != request.getParameter("protocol")) {
                    final Protocol protocol = rv.get(request.getParameter("protocol"));
                    request.setAttribute("experimentType", protocol.getExperimentType());
                    request.setAttribute("protocol", protocol);
                }

                // Next Experiment (refInputSample, inputPlate and inputGroup supplied)
                if (null != request.getParameter("refInputSample")) {
                    final RefInputSample refInputSample = rv.get(request.getParameter("refInputSample"));
                    request.setAttribute("refInputSample", refInputSample);
                    request.setAttribute("experimentType", refInputSample.getProtocol().getExperimentType());
                    request.setAttribute("protocol", refInputSample.getProtocol());
                }

                if (null != request.getParameter("inputPlate")) {
                    request.setAttribute("inputPlate", rv.get(request.getParameter("inputPlate")));
                }

                if (null != request.getParameter("inputGroup")) {
                    final ExperimentGroup group = rv.get(request.getParameter("inputGroup"));
                    if (null != group) {
                        request.setAttribute("inputGroup", group);
                        request.setAttribute("experimentCount", group.getExperiments().size());

                        final PlateReader reader = new PlateReader(rv, group, null);
                        final PlateExperimentUpdateBean updateBean =
                            new PlateExperimentUpdateBean(reader.getExperimentGroup(), null);

                        final Collection<Holder> holders = HolderFactory.getPlates(group);
                        for (final Holder plate : holders) {
                            // there are none, unless this is a plate experiment
                            request.setAttribute(HolderFactory.getHolderPoint(plate) + "Name",
                                plate.getName());
                            request.setAttribute(HolderFactory.getHolderPoint(plate) + "Hook",
                                plate.get_Hook());
                            request.setAttribute(HolderFactory.getHolderPoint(plate),
                                updateBean.getPlateExperimentLayout(plate));
                        }

                        request.setAttribute("inputPlateHoldertype",
                            new HolderTypeBean(PlateReader.getPlateHolderType(group)));
                        final int[][] plateLayout = PlateReader.getPlatelayout(group);
                        request.setAttribute("inputPlateLayout", plateLayout);
                        request.setAttribute("inputPlateMinRow", PlateReader.getPlateMinRow(plateLayout));
                        request.setAttribute("inputPlateMaxRow", PlateReader.getPlateMaxRow(plateLayout));
                        request.setAttribute("inputPlateMinCol", PlateReader.getPlateMinCol(plateLayout));
                        request.setAttribute("inputPlateMaxCol", PlateReader.getPlateMaxCol(plateLayout));
                    }
                }

                final RequestDispatcher rd =
                    request.getRequestDispatcher("/JSP/plateExperiment/CreatePlate.jsp");
                rd.forward(request, response);
            }
            rv.commit();

        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /**
     * 
     * CreatePlate.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        final PrintWriter writer = response.getWriter();
        final Map<String, String> parms = new HashMap<String, String>();

        String inputName = null;
        String inputString = null;
        final Map<String, String> uploadedFiles = SpreadsheetGetter.getSpreadsheet(request, parms);
        if (!uploadedFiles.isEmpty()) {
            final Map.Entry<String, String> entry = uploadedFiles.entrySet().iterator().next();
            inputName = entry.getKey();
            inputString = entry.getValue();
        }
        //TODO simpler to submit this request to the right place to start with
        final String groupName = parms.get("groupName");
        if (parms.containsKey("GetSpreadsheet")) {
            final String url =
                request.getContextPath() + "/read/CreateExperimentGroupCsv/" + parms.get("protocol") + "/"
                    + groupName + ".csv?groupName=" + groupName + "&holderType=" + parms.get("holderType")
                    + "&plateId_NW=" + parms.get("plateId_NW") + "&plateId_NE=" + parms.get("plateId_NE")
                    + "&plateId_SW=" + parms.get("plateId_SW") + "&plateId_SE=" + parms.get("plateId_SE");
            PIMSServlet.redirectPost(response, url);
            return;
        }

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }
        // to improve performance
        version.setFlushModeCommit();

        try {
            final Protocol protocol = (Protocol) version.get(parms.get("protocol"));
            if (null == protocol) {
                throw new AssertionError("The protocol is not valid");
            }

            if (parms.containsKey("checkSpreadsheet")) {
                System.out.println("org.pimslims.servlet.plateexperiment.CreatePlate checkSpreadsheet");
                final PrintWriter out = response.getWriter();
                out.println(this.checkSpreadsheet(inputString, protocol));
                version.commit();
                return;
            }

            final HolderType holderType = version.get(parms.get("holderType"));
            assert null != holderType : "No such holder type: " + request.getParameter("holderType");
            // Set<HolderCategory> categories =
            // holderType.getHolderCategories();
            if (CaliperFile.plateAlreadyExist(version, groupName)) {
                throw new ConstraintException(null, "Plate's Name", groupName, "Can not use " + groupName
                    + " as plate's name because it is already in used. please choose another name!");
            }

            final LabNotebook access = (LabNotebook) version.get(parms.get(PIMSServlet.LAB_NOTEBOOK_ID));
            if (null == access) {
                throw new AssertionError("Please specify a lab notebook");
            }

            final Collection<ExperimentType> experimentTypes =
                Collections.singleton(protocol.getExperimentType());

            final Collection<RefOutputSample> oss = protocol.getRefOutputSamples();
            if (1 != oss.size()) {
                throw new AssertionError(
                    "The protocol for a plate experiment must have exactly one output sample");
            }

            final SampleCategory output = oss.iterator().next().getSampleCategory();
            if (null == output) {
                throw new AssertionError(
                    "The protocol's output sample for a plate experiment must have a Sample Category");
            }

            // get basic details
            //TODO no, use ValueFormatter
            final Calendar startDate = Calendar.getInstance();
            final Calendar endDate = Calendar.getInstance();
            final String start = parms.get("startDate");
            final String end = parms.get("endDate");
            try {
                if (null != start && !"".equals(start)) {
                    startDate.setTimeInMillis(Long.parseLong(start));
                }

                if (null != end && !"".equals(end)) {
                    endDate.setTimeInMillis(Long.parseLong(end));
                }

            } catch (final NumberFormatException e) {
                throw new ServletException(e);
            }

            Collection<String> wells = null;

            if (null != inputString) {
                wells =
                    SpreadsheetGetter.getWellsFromSpreadSheet(new InputStreamReader(CaliperFile
                        .parseStringToIS(inputString)));
            }

            if (null != request.getParameter("inputGroup")) {
                wells =
                    CreatePlate.getWellsFromInputGroup((ExperimentGroup) version.get(request
                        .getParameter("inputGroup")));
            }

            // create the experiment group
            final ExperimentGroup group =
                HolderFactory.doCreateExperimentGroup(version, access, groupName, "plate experiment",
                    startDate, endDate, Collections.EMPTY_MAP);

            final Map<String, String> holders = //CreatePlate.getHolderNames(parms, inputString, holderType);
                CreatePlate.getHolderNames(parms, group);

            if (holders.isEmpty()) {

                final Holder holder =
                    HolderFactory.createHolder(version, access, groupName, holderType,
                        java.util.Collections.EMPTY_MAP);

                HolderFactory.createPlateInGroup(version, access, group, holder, null, experimentTypes,
                    request.getParameter("details"), protocol, Collections.EMPTY_MAP, wells,
                    HolderFactory.getPositions(holderType), Collections.EMPTY_MAP);
            }

            CreatePlate.createPlates(parms, version, holderType, access, protocol, experimentTypes, wells,
                holders, group);

            if (null != inputString) {
                final ExperimentGroupWriter gw = new ExperimentGroupWriter(version, group);
                gw.setValuesFromSpreadSheetForCreate(new InputStreamReader(CaliperFile
                    .parseStringToIS(inputString)));
            }

            if (null != request.getParameter("refInputSample") && null != request.getParameter("inputGroup")) {
                final RefInputSample refInputSample = version.get(request.getParameter("refInputSample"));
                final ExperimentGroup fromGroup = version.get(request.getParameter("inputGroup"));
                HolderFactory.multiLinePipette(version, fromGroup, group, refInputSample.getName(), null);
            }

            if (null != inputString) {
                final java.io.InputStream uploadedStream = CaliperFile.parseStringToIS(inputString);
                version.setDefaultOwner(((LabBookEntry) group).getAccess());
                version.createFile(uploadedStream, inputName, group);
            }

            version.commit();
            PIMSServlet.redirectPost(response, "View/" + group.get_Hook());

        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final IllegalArgumentException e) {
            this.writeErrorHead(request, response, "Unable to interpret CSV file",
                HttpServletResponse.SC_BAD_REQUEST);
            writer.write("<p class=error>" + e.getMessage() + "</p>");
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * 
     * CreatePlate.getHolderNames
     * 
     * @param parms
     * @param inputString
     * @param holderType
     * @return
     */
    static Map<String, String> getHolderNames(final Map<String, String> parms, final String inputString,
        final HolderType holderType) {
        final Map<String, String> holders = new HashMap<String, String>();

        if (null != inputString) {
            try {
                holders.putAll(SpreadsheetGetter.getHoldersFromSpreadSheet(
                    new InputStreamReader(CaliperFile.parseStringToIS(inputString)), holderType));
            } catch (final ConstraintException e) {
                throw new RuntimeException(e);
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (ServletUtil.validString(parms.get("plateId_NW"))) {
            holders.put(parms.get("plateId_NW"), "NW");
        }
        if (ServletUtil.validString(parms.get("plateId_NE"))) {
            holders.put(parms.get("plateId_NE"), "NE");
        }
        if (ServletUtil.validString(parms.get("plateId_SW"))) {
            holders.put(parms.get("plateId_SW"), "SW");
        }
        if (ServletUtil.validString(parms.get("plateId_SE"))) {
            holders.put(parms.get("plateId_SE"), "SE");
        }
        return holders;
    }

    static Map<String, String> getHolderNames(final Map<String, String> parms, final ExperimentGroup group) {
        final Map<String, String> holders = new HashMap<String, String>();

        if (ServletUtil.validString(parms.get("plateId_NW"))) {
            holders.put(parms.get("plateId_NW"), "NW");
        }
        if (ServletUtil.validString(parms.get("plateId_NE"))) {
            holders.put(parms.get("plateId_NE"), "NE");
        }
        if (ServletUtil.validString(parms.get("plateId_SW"))) {
            holders.put(parms.get("plateId_SW"), "SW");
        }
        if (ServletUtil.validString(parms.get("plateId_SE"))) {
            holders.put(parms.get("plateId_SE"), "SE");
        }

        if (holders.isEmpty()) {
            holders.put(group.getName(), "NW");
        }
        return holders;
    }

    /**
     * 
     * CreatePlate.createPlates
     * 
     * @param parms
     * @param version
     * @param holderType
     * @param access
     * @param protocol
     * @param experimentTypes
     * @param wells
     * @param holders
     * @param group
     * @throws ConstraintException
     * @throws AccessException
     */
    public static void createPlates(final Map<String, String> parms, final WritableVersion version,
        final HolderType holderType, final LabNotebook access, final Protocol protocol,
        final Collection<ExperimentType> experimentTypes, final Collection<String> wells,
        final Map<String, String> holders, final ExperimentGroup group) throws ConstraintException,
        AccessException {
        for (final Iterator it = holders.entrySet().iterator(); it.hasNext();) {
            final Map.Entry<String, String> e = (Map.Entry) it.next();
            final String plateName = e.getKey();
            final String platePosition = e.getValue();

            //System.out.println("CreatePlate [" + plateName + ":" + platePosition + "]");
            final Holder holder =
                HolderFactory.createHolder(version, access, plateName, holderType,
                    java.util.Collections.EMPTY_MAP);

            final List<String> positions = HolderFactory.getPositions(holderType, platePosition);

            HolderFactory.createPlateInGroup(version, access, group, holder, null, experimentTypes,
                parms.get("details"), protocol, Collections.EMPTY_MAP, wells, positions,
                Collections.EMPTY_MAP);
        }
    }

    /**
     * 
     * CreatePlate.getWellsFromInputPlate
     * 
     * @param inputPlate
     * @return
     */
    public static List<String> getWellsFromInputPlate(final Holder inputPlate) {

        final ArrayList<String> wells = new ArrayList<String>();
        for (final Sample inputSample : inputPlate.getSamples()) {
            wells.add(HolderFactory.getPositionInHolder(inputSample));
        }

        return wells;
    }

    /**
     * 
     * CreatePlate.getWellsFromInputGroup
     * 
     * @param inputGroup
     * @return
     */
    public static List<String> getWellsFromInputGroup(final ExperimentGroup inputGroup) {

        final ArrayList<String> wells = new ArrayList<String>();
        for (final Holder holder : HolderFactory.getPlates(inputGroup)) {
            for (final Sample inputSample : holder.getSamples()) {
                wells.add(HolderFactory.getPositionInHolder(inputSample));
            }
        }

        return wells;
    }

    /**
     * 
     * CreatePlate.activeProtocolsForPlates
     * 
     * @param protocols
     * @return
     */
    //TODO do this in HQL, that will be quicker
    public static Collection<Protocol> activeProtocolsForPlates(final Collection<Protocol> protocols) {
        final Collection<Protocol> collection = new HashSet();
        for (final Protocol p : protocols) {
            if (null == p.getIsForUse() || p.getIsForUse() && 1 == p.getRefOutputSamples().size()) {
                collection.add(p);
            }
        }
        return collection;
    }

    /**
     * 
     * CreatePlate.holderTypesForPlates
     * 
     * @param holderTypes
     * @return
     */
    public static Collection<HolderTypeBean> holderTypesForPlates(final Collection<HolderType> holderTypes) {
        final List<HolderTypeBean> collection = new ArrayList();
        for (final HolderType h : holderTypes) {
            if (h.getDefaultHolderCategories().size() == 1
                && "Box".equals(h.getDefaultHolderCategories().iterator().next().getName())) {
                continue;
            }
            if (null == h.getMaxRow() || null == h.getMaxColumn()) {
                continue;
            }
            collection.add(new HolderTypeBean(h));
        }
        Collections.sort(collection);
        return collection;
    }

    /**
     * 
     * CreatePlate.groupNamePrompt
     * 
     * @param version
     * @param request
     * @param response
     * @throws IOException
     * @throws AccessException
     * @throws ServletException
     */
    private void groupNamePrompt(final ReadableVersion version, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, AccessException, ServletException {

        final Protocol experimentProtocol = version.get(request.getParameter("experimentProtocolHook"));
        assert null != experimentProtocol : "No such protocol: "
            + request.getParameter("experimentProtocolHook");

        final ModelObject experimentType = experimentProtocol.getExperimentType();
        assert null != experimentType;

        final PlateNameFactory pnf = PropertyGetter.getInstance("Plate.Name.Factory", DefaultPlateName.class);

        // for Ajax List
        if (null != request.getParameter("isAJAX")
            && "true".equals(request.getParameter("isAJAX").toString())) {
            response.setContentType("text/xml");
            final XMLOutputter xo = new XMLOutputter();
            final String experimentName = pnf.suggestPlateName(version, null, experimentProtocol);

            final Element rootElement = new Element("string");
            rootElement.addContent(CreateExperimentOld.makeAjaxStringXML("experiment", experimentName));

            xo.output(new Document(rootElement), response.getWriter());
            return;
        }
    }

    /**
     * 
     * CreatePlate.checkSpreadsheet
     * 
     * @param inputString
     * @param protocol
     * @return
     * @throws ConstraintException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private String checkSpreadsheet(final String inputString, final Protocol protocol)
        throws ConstraintException, UnsupportedEncodingException, IOException {

        final Collection<String> differences = new ArrayList<String>();
        final Collection<String> labels = this.getSpreadsheetLabels(inputString);

        final Collection<String> definitions = this.getDefinitionNames(protocol);
        final Collection<String> refInputs = this.getRefInputNames(protocol);

        for (final String label : labels) {
            if (definitions.contains(label)) {
                continue;
            }
            if (refInputs.contains(label)) {
                continue;
            }
            differences.add(label);
        }

        if (differences.isEmpty()) {
            return "";
        }

        final StringBuffer sb = new StringBuffer();
        sb.append("Labels from spreadsheet: ");
        final String labelString = labels.toString();
        sb.append(labelString.substring(1, labelString.length() - 1));
        sb.append("\n\n");

        sb.append("Parameters from protocol: ");
        final String definitionString = definitions.toString();
        sb.append(definitionString.substring(1, definitionString.length() - 1));
        sb.append("\n");

        sb.append("Inputs from protocol: ");
        final String refInputString = refInputs.toString();
        sb.append(refInputString.substring(1, refInputString.length() - 1));

        return sb.toString();
    }

    /**
     * 
     * CreatePlate.getDefinitionNames
     * 
     * @param protocol
     * @return
     */
    private Collection<String> getDefinitionNames(final Protocol protocol) {
        final Collection<String> definitionNames = new ArrayList<String>();
        for (final ParameterDefinition parameter : protocol.getParameterDefinitions()) {
            definitionNames.add(parameter.getName());
        }
        return definitionNames;
    }

    /**
     * 
     * CreatePlate.getRefInputNames
     * 
     * @param protocol
     * @return
     */
    private Collection<String> getRefInputNames(final Protocol protocol) {
        final Collection<String> refInputNames = new ArrayList<String>();
        for (final RefInputSample refInput : protocol.getRefInputSamples()) {
            if (!this.isAmountLabel(refInput.getName())) {
                refInputNames.add(refInput.getName());
            }
        }
        return refInputNames;
    }

    /**
     * 
     * CreatePlate.getSpreadsheetLabels
     * 
     * @param inputString
     * @return
     * @throws ConstraintException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private Collection<String> getSpreadsheetLabels(final String inputString) throws ConstraintException,
        UnsupportedEncodingException, IOException {

        final Collection<String> spreadsheetLabels = new ArrayList<String>();
        final Collection<String> labels =
            SpreadsheetGetter.getlabelsFromSpreadSheet(new InputStreamReader(CaliperFile
                .parseStringToIS(inputString)));

        for (final String label : labels) {
            if ("PlateId".equalsIgnoreCase(label)) {
                continue;
            }
            if ("Well".equalsIgnoreCase(label)) {
                continue;
            }
            if ("Target".equalsIgnoreCase(label)) {
                continue;
            }
            if ("Construct".equalsIgnoreCase(label)) {
                continue;
            }
            if (!this.isAmountLabel(label)) {
                spreadsheetLabels.add(label);
            }
        }
        return spreadsheetLabels;
    }

    /**
     * 
     * CreatePlate.isAmountLabel
     * 
     * @param name
     * @return
     */
    private boolean isAmountLabel(final String name) {
        if (name.endsWith("uL")) {
            return true;
        }
        return false;
    }
}
