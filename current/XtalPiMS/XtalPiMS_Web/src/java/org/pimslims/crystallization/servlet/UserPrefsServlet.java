/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.pimslims.crystallization.util.UserPreferences;
import org.pimslims.crystallization.util.UserPreferencesSessionImpl;

/**
 * 
 * @author Jon Diprose
 */
@Deprecated
// obsolete
public class UserPrefsServlet extends XtalPIMSServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4700702435212810652L;

    /**
     * Get the UserPrefences instance
     * 
     * @param request
     * @return
     */
    protected UserPreferences getPrefs(HttpServletRequest request) {

        UserPreferences prefs = new UserPreferencesSessionImpl();

        // Required to initialize
        prefs.reset(request);

        return prefs;

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {

        UserPreferences prefs = getPrefs(request);

        JSONObject json = new JSONObject();
        Enumeration<String> en = request.getParameterNames();
        if (en.hasMoreElements()) {
            while (en.hasMoreElements()) {
                String param = en.nextElement();
                json.put(param, prefs.getPreference(param));
            }
        } else {
            json.putAll(prefs.getPreferences());
        }

        this.setContentTypeJson(response);
        this.setNoCache(response);
        response.getWriter().print(json);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * Could Limit length and/or use only allowed keys/values to mitigate against misuse?
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {

        UserPreferences prefs = getPrefs(request);

        for (Map.Entry<String, String[]> entry : ((Map<String, String[]>) request.getParameterMap())
            .entrySet()) {
            String[] values = (String[]) entry.getValue();
            // if (0 < values.length && (0 < values[0].length())) {
            if (0 < values.length) {
                prefs.setPreference((String) entry.getKey(), values[0]);
            }
        }

        // Save new preferences
        prefs.save(request);

    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "UserPreference Servlet";
    }

}
