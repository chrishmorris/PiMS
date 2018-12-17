package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FlushMode;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.experiment.ExperimentGroupWriter;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.spreadsheet.SpreadsheetError;

/**
 * Servlet implementation class for Servlet: CreatePlate
 * 
 */
public class UpdatePlateFromSpreadsheet extends PIMSServlet {
/*
    private static final Map<String, Object> NINETY_SIX = new HashMap<String, Object>();
    static {
        UpdatePlateFromSpreadsheet.NINETY_SIX.put(HolderType.PROP_MAXCOLUMN, new Integer(12));
        UpdatePlateFromSpreadsheet.NINETY_SIX.put(HolderType.PROP_MAXROW, new Integer(8));
    } */

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
        throw new ServletException(this.getClass().getName() + " does not support the GET method");
    }

    /*
     * (non-Java-doc)
     *   ss
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }
        final Map<String, String> parms = new HashMap<String, String>();

        final Map<String, String> uploadedFiles = SpreadsheetGetter.getSpreadsheet(request, parms);
        final Map.Entry<String, String> entry = uploadedFiles.entrySet().iterator().next();
        //final String inputName = entry.getKey();
        final String inputString = entry.getValue();

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }
        try {
            // to improve performance
            version.getSession().setFlushMode(FlushMode.COMMIT); //TODO version.setFlushModeCommit();
            // create the holder
            final ExperimentGroup group =
                (ExperimentGroup) this.getRequiredObject(version, request, response, pathInfo);
            if (null == group) {
                return;
            }
            final ExperimentGroupWriter gw = new ExperimentGroupWriter(version, group);
            gw.setValuesFromSpreadSheetForAmend(new InputStreamReader(CaliperFile
                .parseStringToIS(inputString)));

            final Collection<SpreadsheetError> errors = gw.getErrors();
            if (!errors.isEmpty()) {
                System.out.println("/JSP/plateExperiment/ErrorPlate.jsp");
                request.setAttribute("bean", BeanFactory.newBean(group));
                request.setAttribute("spreadsheetErrors", errors);
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/plateExperiment/ErrorPlate.jsp");
                dispatcher.forward(request, response);

            } else {
                System.out.println(request.getContextPath() + "/View/" + group.get_Hook());
                version.commit();
                PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + group.get_Hook());
            }

        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final IllegalArgumentException e) {
            throw new ServletException("Unable to interpret CSV file", e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
