package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.ArrayList;
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
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.SampleQuantityView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;

/**
 * Servlet implementation class for Servlet: SaltCrystalServlet
 * 
 * A severely cut-down TrialDropServlet that only deals with returning lists of salt crystals.
 * 
 * NB This always connects to the database with readAll set to true. If you modify this class then you MUST
 * ensure that everything returned has been properly sanitized, ie no owner, group, sample info, description
 * etc. Only leave what is relevant to salt crystals.
 */
public class SaltCrystalServlet extends org.pimslims.crystallization.servlet.XtalPIMSServlet {
    static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final CommonRequestParams crp = CommonRequestParams.parseRequest(request);

        final String well = request.getParameter(TrialDropView.PROP_WELL);
        final String localScreenName = request.getParameter(ConditionView.PROP_LOCAL_NAME);
        final String scoreDescription = "Salt Crystals";

        DataStorage dataStorage = null;
        try {
            dataStorage = openResourcesReadAll(request);

            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);

            if ((well != null) && (!well.equals(""))) {
                criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL, well, true));
                crp.put(TrialDropView.PROP_WELL, well);
            }
            if ((localScreenName != null) && (!localScreenName.equals(""))) {
                criteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, localScreenName, true));
                crp.put(ConditionView.PROP_LOCAL_NAME, localScreenName);
            }

            criteria.add(BusinessExpression.Equals("scoreDescription", scoreDescription, true));
            crp.put("scoreDescription", scoreDescription);

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

                        // Remove sensitive information
                        trialDrop.setGroup("");
                        trialDrop.setHumanScores(new ArrayList<ScoreView>());
                        trialDrop.setMicroscopeImages(new ArrayList<ImageView>());
                        trialDrop.setOwner("");
                        trialDrop.setSamples(new ArrayList<SampleQuantityView>());
                        trialDrop.setSoftwareScores(new ArrayList<ScoreView>());
                        trialDrop.setSynchrotron(Boolean.FALSE);
                        for (ImageView iv : trialDrop.getImages()) {
                            iv.setDescription("");
                        }

                        array.add(trialDrop.toJSON());
                    }

                    // JMD recordsReturned set here to avoid potential NPE
                    recordsReturned = trialDrops.size();

                }

                object.put("records", array);

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
                crp.storeSessionBookmark(request.getSession(), "saltXtalsDataTable");
            }
            closeResources(dataStorage);
        }

    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
