package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;

/**
 * Servlet implementation class for Servlet: TrialDropServlet
 * 
 */
public class TrialDropServlet extends org.pimslims.crystallization.servlet.XtalPIMSServlet {
    static final long serialVersionUID = 1L;

    private static final String[] parms = new String[] {
        ScoreView.PROP_BARCODE,
        TrialDropView.PROP_WELL,
        TrialDropView.PROP_SYNCHROTRON,
        ConditionView.PROP_LOCAL_NAME,
        ConditionView.PROP_LOCAL_NUMBER,
        ConditionView.PROP_MANUFACTURER_NAME,
        ConditionView.PROP_MANUFACTURER_SCREEN_NAME,
        ConditionView.PROP_MANUFACTURER_CODE,
        ConditionView.PROP_MANUFACTURER_CAT_CODE,
        ConditionView.PROP_VOLATILE_CONDITION,
        //"min" + ConditionView.PROP_FINAL_PH,
        //"max" + ConditionView.PROP_FINAL_PH,
        ConditionView.PROP_SALT_CONDITION,
        ComponentView.PROP_NAME,
        ComponentView.PROP_TYPE,
        ComponentView.PROP_VOLATILE_COMPONENT,
        ComponentView.PROP_CAS_NUMBER,
        // "min" + ComponentView.PROP_PH,
        //"max" + ComponentView.PROP_PH,
        "sampleName",
        "sampleId",
        "sampleDescription",
        //"samplePH",
        //SampleView.PROP_MOLECULAR_WEIGHT,
        //SampleView.PROP_NUM_SUB_UNITS,
        SampleView.PROP_BATCH_REFERENCE,
        SampleView.PROP_ORIGIN,
        SampleView.PROP_TYPE,
        SampleView.PROP_CELLULAR_LOCATION,
        //SampleView.PROP_CONCENTRATION,

        //"minCreateDate",
        //"maxCreateDate",
        SampleView.PROP_GI_NUMBER, SampleView.PROP_CONSTRUCT_NAME, SampleView.PROP_CONSTRUCT_ID,
        SampleView.PROP_TARGET_NAME, SampleView.PROP_TARGET_ID, SampleView.PROP_OWNER, SampleView.PROP_GROUP,
        //ImageView.PROP_DATE,
        ImageView.PROP_INSPECTION_NAME, ImageView.PROP_INSTRUMENT, ImageView.PROP_TEMPERATURE,
        //"imageType",
        //"scoreDate",
        "scoreDescription",
    //"scoreColour",
    //"annotator",
    //"version",
    //"scoreType",     
        };

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final CommonRequestParams crp = CommonRequestParams.parseRequest(request);
        String barcode = request.getParameter(ScoreView.PROP_BARCODE);
        if (null != barcode) {
            barcode = barcode.trim();
        }
        final String well = request.getParameter(TrialDropView.PROP_WELL);

        //final String minFinalpHStr = request.getParameter("min" + ConditionView.PROP_FINAL_PH);
        //final String maxFinalpHStr = request.getParameter("max" + ConditionView.PROP_FINAL_PH);
        // final String minPHStr = request.getParameter("min" + ComponentView.PROP_PH);
        //final String maxPHStr = request.getParameter("max" + ComponentView.PROP_PH);
        //final String samplePHStr = request.getParameter("samplePH");
        //final String molecularWeight = request.getParameter(SampleView.PROP_MOLECULAR_WEIGHT);
        //final String numSubUnitsStr = request.getParameter(SampleView.PROP_NUM_SUB_UNITS);
        //final String concentrationStr = request.getParameter(SampleView.PROP_CONCENTRATION);

        //final String minCreateDate = request.getParameter("minCreateDate");
        //final String maxCreateDate = request.getParameter("maxCreateDate");
        //final String imageDate = request.getParameter(ImageView.PROP_DATE);
        //final String imageType = request.getParameter("imageType");
        //final String scoreDate = request.getParameter("scoreDate");
        //final String colour = request.getParameter("scoreColour");
        //final String annotator = request.getParameter("annotator");
        //final String softwareVersion = request.getParameter("version");
        //final String scoreType = request.getParameter("scoreType");

        String subPosition = request.getParameter("subPosition");
        if (subPosition == null || subPosition.length() == 0) {
            subPosition = "1";
        }
        DataStorage dataStorage = null;
        try {
            dataStorage = openResources(request);

            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            if ((barcode != null) && (!barcode.equals(""))) {
                criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, barcode, true));
                crp.put(TrialDropView.PROP_BARCODE, barcode);
            }
            if ((well != null) && (!well.equals(""))) {
                criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL, well, true));
                crp.put(TrialDropView.PROP_WELL, well);
                if (well.contains(".")) {
                    final WellPosition position = new WellPosition(well);
                    subPosition = "" + position.getSubPosition();
                }
            }
            //TODO make a loop here

            addParameterToCriteria(request, crp, criteria, TrialDropView.PROP_SYNCHROTRON);

            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_LOCAL_NAME);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_LOCAL_NUMBER);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_MANUFACTURER_NAME);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_MANUFACTURER_SCREEN_NAME);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_MANUFACTURER_CODE);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_MANUFACTURER_CAT_CODE);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_VOLATILE_CONDITION);
            addParameterToCriteria(request, crp, criteria, ConditionView.PROP_SALT_CONDITION);

            addParameterToCriteria(request, crp, criteria, ComponentView.PROP_NAME);
            addParameterToCriteria(request, crp, criteria, ComponentView.PROP_TYPE);
            addParameterToCriteria(request, crp, criteria, ComponentView.PROP_VOLATILE_COMPONENT);
            addParameterToCriteria(request, crp, criteria, ComponentView.PROP_CAS_NUMBER);

            addParameterToCriteria(request, crp, criteria, "sampleName");
            addParameterToCriteria(request, crp, criteria, "sampleId");
            addParameterToCriteria(request, crp, criteria, "sampleDescription");

            addParameterToCriteria(request, crp, criteria, SampleView.PROP_BATCH_REFERENCE);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_ORIGIN);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_TYPE);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_CELLULAR_LOCATION);

            addParameterToCriteria(request, crp, criteria, SampleView.PROP_GI_NUMBER);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_CONSTRUCT_NAME);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_CONSTRUCT_ID);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_TARGET_NAME);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_TARGET_ID);

            addParameterToCriteria(request, crp, criteria, ImageView.PROP_INSPECTION_NAME);
            addParameterToCriteria(request, crp, criteria, ImageView.PROP_TYPE);
            addParameterToCriteria(request, crp, criteria, ImageView.PROP_INSTRUMENT);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_OWNER);
            addParameterToCriteria(request, crp, criteria, SampleView.PROP_GROUP);
            addParameterToCriteria(request, crp, criteria, "scoreDescription");

            /*
             * Do something with these ranges...
             */
            /*
             * minFinalpHStr = request.getParameter("min" +
             * ConditionView.PROP_FINAL_PH); minPHStr =
             * request.getParameter(ComponentView.PROP_PH); samplePHStr =
             * request.getParameter("samplePH"); molecularWeight =
             * request.getParameter(SampleView.PROP_MOLECULAR_WEIGHT);
             * numSubUnitsStr =
             * request.getParameter(SampleView.PROP_NUM_SUB_UNITS);
             * concentrationStr =
             * request.getParameter(SampleView.PROP_CONCENTRATION);;
             * minCreateDate = request.getParameter("minCreateDate");
             * maxCreateDate = request.getParameter("maxCreateDate"); imageDate
             * = request.getParameter(ImageView.PROP_DATE);
             */

            final JSONObject object = new JSONObject();
            if (request.getParameter("count") != null) {
                final int screenCount = trialService.findViewCount(criteria);

                object.put("count", screenCount);
            } else {
                criteria.setMaxResults(crp.getMaxResults());
                criteria.setFirstResult(crp.getFirstResult());
                if (crp.isAscending()) {
                    criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
                } else {
                    criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
                }

                /*
                 * trialService.setCompositeImageURLStub(ImageURL
                 * .getUrl_compositeimages()); trialService
                 * .setSliceImageURLStub(ImageURL.getUrl_sliceimages());
                 * trialService.setZoomedImageURLStub(ImageURL
                 * .getUrl_zoomedimages());
                 */

                final Collection<TrialDropView> trialDrops = trialService.findViews(criteria);
                // JMD - Default zero records returned
                int recordsReturned = 0;

                final JSONArray array = new JSONArray();
                if (trialDrops != null) {

                    final Iterator<TrialDropView> it = trialDrops.iterator();
                    while (it.hasNext()) {
                        final TrialDropView trialDrop = it.next();
                        if (subPosition.equalsIgnoreCase("-1")
                            || trialDrop.getWell().endsWith("." + subPosition)) {
                            array.add(trialDrop.toJSON());
                            // System.out.println("Found trialDrop at
                            // "+trialDrop.getWell()+" with
                            // "+trialDrop.getImages().size()+" images");
                        } else {
                            // System.out.println("Found trialDrop,not match
                            // subPosition("+subPosition+"), at
                            // "+trialDrop.getWell()+" with
                            // "+trialDrop.getImages().size()+" images");
                        }
                    }

                    // JMD recordsReturned set here to avoid potential NPE
                    recordsReturned = trialDrops.size();

                }

                object.put("records", array);
                System.out.println("records" + array.toArray().length);

                /*
                 * JMD - given that this is outside the if (trialDrops != null)
                 * block above, this is a potential NPE
                 * 
                 * object.put("recordsReturned", trialDrops.size());
                 */
                object.put("recordsReturned", recordsReturned);

            }

            this.setContentTypeJson(response);
            this.setNoCache(response);
            response.getWriter().print(object);
        } catch (final BusinessException ex) {
            throw new ServletException(ex);
        }

        finally {
            // Only store for the non-count request!
            if (null == request.getParameter("count")) {
                crp.storeSessionBookmark(request.getSession(), "trialDropsDataTable");
            }
            closeResources(dataStorage);
        }

    }

    private void addParameterToCriteria(final HttpServletRequest request, final CommonRequestParams crp,
        final BusinessCriteria criteria, String parm) throws BusinessException {
        final String value = request.getParameter(parm);
        if ((value != null) && (!value.equals(""))) {
            criteria.add(BusinessExpression.Equals(parm, value, true));
            crp.put(parm, value);
        }
    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the
    // code.">
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
}
