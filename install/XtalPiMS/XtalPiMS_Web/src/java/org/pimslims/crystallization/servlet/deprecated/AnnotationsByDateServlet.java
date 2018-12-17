/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet.deprecated;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author ian
 * @deprecated
 */
public class AnnotationsByDateServlet extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 8341109014000181408L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //String user = request.getParameter("user");
//            boolean userOnly = true;
//            if (user.equals("all")) {
//                userOnly = false;
//            }
            HumanScoreService scoreService = this.getDataStorage().getHumanScoreService();
            BusinessCriteria criteria = new BusinessCriteria(scoreService);
            criteria.setMaxResults(4);
            criteria.setFirstResult(0);
            criteria.addOrder(BusinessOrder.DESC("dttimestamp"));
            
            Collection<ScoreView> scores = scoreService.findViews(criteria);

            //BeanComparator comparator = new BeanComparator("date", new ReverseComparator(new ComparableComparator()));
            //Collections.sort(scores, comparator);

            JSONArray array = new JSONArray();
            Iterator<ScoreView> it = scores.iterator();
            while (it.hasNext()) {
                ScoreView score = it.next();
                JSONObject scobj = new JSONObject();
                scobj.put("Barcode", score.getBarcode());
                scobj.put("Well", score.getWell());
                scobj.put("Date", score.getDate().getTimeInMillis());
                scobj.put("Description", score.getDescription());
                scobj.put("Color", ColorUtil.convertColorToHex(score.getColour()));

                scobj.put("Name", score.getName());
                scobj.put("Version", "-");
                scobj.put("Type", "Human");

                array.add(scobj);
            }
            JSONObject object = new JSONObject();
            object.put("annotations", array);
            out.print(object);
        } catch (BusinessException ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
