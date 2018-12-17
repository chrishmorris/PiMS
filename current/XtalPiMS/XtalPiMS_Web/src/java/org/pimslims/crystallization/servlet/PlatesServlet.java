/*
 * PlatesServlet.java Created on 16 October 2007, 13:49
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class PlatesServlet extends XtalPIMSServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1853279443728896392L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final CommonRequestParams crp = CommonRequestParams.parseRequest(request);

        final String groupName = request.getParameter(PlateExperimentView.PROP_GROUP);
        final String constructId = request.getParameter(PlateExperimentView.PROP_CONSTRUCT_ID);
        final String constructName = request.getParameter(PlateExperimentView.PROP_CONSTRUCT_NAME);
        final String status = request.getParameter(PlateExperimentView.PROP_STATUS);
        String barcode = request.getParameter(ScoreView.PROP_BARCODE);
        if (null != barcode) {
            barcode = barcode.trim();
        }
        final String description = request.getParameter(PlateExperimentView.PROP_DESCRIPTION);
        final String createDateStart = request.getParameter("createDateStart");
        final String createDateEnd = request.getParameter("createDateEnd");
        final String owner = request.getParameter(PlateExperimentView.PROP_OWNER);
        final String runBy = request.getParameter(PlateExperimentView.PROP_RUN_BY);
        final String numberOfCrystals = request.getParameter(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS);
        final String imager = request.getParameter(PlateExperimentView.PROP_IMAGER);
        final String temperature = request.getParameter(PlateExperimentView.PROP_TEMPERATURE);
        final String sampleId = request.getParameter(PlateExperimentView.PROP_SAMPLE_ID);
        final String sampleName = request.getParameter(PlateExperimentView.PROP_SAMPLE_NAME);
        final String lastImageDateStart = request.getParameter("lastImageDateStart");
        final String lastImageDateEnd = request.getParameter("lastImageDateEnd");
        final String plateType = request.getParameter(PlateExperimentView.PROP_PLATE_TYPE);
        final String screen = request.getParameter(PlateExperimentView.PROP_SCREEN);

        DataStorage dataStorage = null;
        try {
            dataStorage = openResources(request);

            final PlateExperimentService plateExperimentService = dataStorage.getPlateExperimentService();

            final BusinessCriteria criteria = new BusinessCriteria(plateExperimentService);

            if ((groupName != null) && (!groupName.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_GROUP, groupName, true, true));
                crp.put(PlateExperimentView.PROP_GROUP, groupName);
            }
            if ((plateType != null) && (!plateType.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_PLATE_TYPE, plateType, true,
                    true));
                crp.put(PlateExperimentView.PROP_PLATE_TYPE, plateType);
            }
            if ((screen != null) && (!screen.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_SCREEN, screen, true, true));
                crp.put(PlateExperimentView.PROP_SCREEN, screen);
            }
            if ((runBy != null) && (!runBy.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_RUN_BY, runBy, true, true));
                crp.put(PlateExperimentView.PROP_RUN_BY, runBy);
            }
            if ((constructId != null) && (!constructId.equals("0"))) {
                criteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_CONSTRUCT_ID,
                    Long.parseLong(constructId), true));
                crp.put(PlateExperimentView.PROP_CONSTRUCT_ID, constructId);
            }
            if ((status != null) && (!status.equals("")) && (!status.equals("all"))) {
                criteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_STATUS, status, true));
                crp.put(PlateExperimentView.PROP_STATUS, status);
            } else {
                crp.put(PlateExperimentView.PROP_STATUS, "all");
            }
            if ((barcode != null) && (!barcode.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_BARCODE, barcode, true, true));
                crp.put(PlateExperimentView.PROP_BARCODE, barcode);
            }
            if ((description != null) && (!description.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_DESCRIPTION, description, true,
                    true));
                crp.put(PlateExperimentView.PROP_DESCRIPTION, description);
            }
            if ((owner != null) && (!owner.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_OWNER, owner, true, true));
                crp.put(PlateExperimentView.PROP_OWNER, owner);
            }
            if ((constructName != null) && (!constructName.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_CONSTRUCT_NAME, constructName,
                    true, true));
                crp.put(PlateExperimentView.PROP_CONSTRUCT_NAME, constructName);
            }
            if ((sampleName != null) && (!sampleName.equals(""))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_SAMPLE_NAME, sampleName, true,
                    true));
                crp.put(PlateExperimentView.PROP_SAMPLE_NAME, sampleName);
            }
            if ((imager != null) && (!imager.equals("")) && (!imager.equals("all"))) {
                criteria.add(BusinessExpression.Like(PlateExperimentView.PROP_IMAGER, imager, true, true));
                crp.put(PlateExperimentView.PROP_IMAGER, imager);
            } else {
                crp.put(PlateExperimentView.PROP_IMAGER, "all");
            }
            if ((numberOfCrystals != null) && (!numberOfCrystals.equals(""))) {
                Integer numCrystals = 0;
                try {
                    numCrystals = Integer.parseInt(numberOfCrystals);

                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                }
                if (numCrystals > 0) {
                    criteria.add(BusinessExpression.GreaterThan(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS,
                        numCrystals, true));
                    crp.put(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS,
                        numberOfCrystals);
                }
            }
            if ((temperature != null) && (!temperature.equals("")) && (!temperature.equals("all"))) {
                try {
                    final Float temp = Float.parseFloat(temperature);
                    criteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_TEMPERATURE, temp, true));
                    crp.put(PlateExperimentView.PROP_TEMPERATURE, temperature);
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                }
            } else {
                crp.put(PlateExperimentView.PROP_TEMPERATURE, "all");
            }
            if ((sampleId != null) && (!sampleId.equals(""))) {
                criteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_SAMPLE_ID, sampleId, true));
                crp.put(PlateExperimentView.PROP_SAMPLE_ID, sampleId);
            }

            // ////////////////////////////////////////////////////////
            // Date range processing
            // ////////////////////////////////////////////////////////

            final DateHandler dh = new DateHandler();
            Calendar calStart = null;
            Calendar calEnd = null;
            try {
                calStart = dh.parseDate(createDateStart);
                crp.put("createDateStart", createDateStart);
            } catch (final ParseException e) {
                // Indicative of a dodgy date - just swallow it
            }
            try {
                calEnd = dh.parseDate(createDateEnd);
                crp.put("createDateEnd", createDateEnd);
            } catch (final ParseException e) {
                // Indicative of a dodgy date - just swallow it
            }
            dh.addCriterion(criteria, PlateExperimentView.PROP_CREATE_DATE, calStart, calEnd);

            calStart = null;
            calEnd = null;
            try {
                calStart = dh.parseDate(lastImageDateStart);
                crp.put("lastImageDateStart", lastImageDateStart);
            } catch (final ParseException e) {
                // Indicative of a dodgy date - just swallow it
            }
            try {
                calEnd = dh.parseDate(lastImageDateEnd);
                crp.put("lastImageDateEnd", lastImageDateEnd);
            } catch (final ParseException e) {
                // Indicative of a dodgy date - just swallow it
            }
            dh.addCriterion(criteria, PlateExperimentView.PROP_LAST_IMAGE_DATE, calStart, calEnd);

            // ////////////////////////////////////////////////////////
            // Run the query
            // ////////////////////////////////////////////////////////

            final JSONObject object = new JSONObject();
            if (request.getParameter("count") != null) {

                final Integer count = plateExperimentService.findViewCount(criteria);
                object.put("count", count);

            } else {
                criteria.setMaxResults(crp.getMaxResults());
                criteria.setFirstResult(crp.getFirstResult());
                if (crp.isAscending()) {
                    criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
                } else {
                    criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
                }

                final Collection<PlateExperimentView> plates = plateExperimentService.findViews(criteria);

                final JSONArray array = new JSONArray();
                if (plates != null) {

                    for (final PlateExperimentView plate : plates) {
                        plate
                            .setLink("<a href=\"./ViewPlate.jsp?barcode="
                                + plate.getBarcode()
                                + "\"><img class=\"icon\" border=\"0\" src=\"./skins/default/images/icons/actions/view.gif\" title=\"View plate "
                                + plate.getBarcode() + "\"  ></a>" + "</a>");
                        array.add(plate.toJSON());
                    }
                }

                object.put("records", array);
                object.put("recordsReturned", plates.size());

                // JMD Add recent barcode to response - requires corresponding
                // change in xdatatable.js's doBeforeCallBack function
                if (null != request.getSession().getAttribute("recentBarcode")) {
                    object.put("recentBarcode", request.getSession().getAttribute("recentBarcode"));
                }
            }

            this.setContentTypeJson(response);
            this.setNoCache(response);
            response.getWriter().print(object);
        } catch (final BusinessException ex) {
            throw new ServletException(ex);
        } finally {
            // Only store for the non-count request!
            if (null == request.getParameter("count")) {
                crp.storeSessionBookmark(request.getSession(), "platesDataTable");
            }
            closeResources(dataStorage);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
    // the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Gets all the information about a persons plates";
    }
    // </editor-fold>
}
