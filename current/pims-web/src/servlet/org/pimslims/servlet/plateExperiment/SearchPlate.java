/**
 * 
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.presentation.plateExperiment.PlateCriteria;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.experiment.CreateExperiment;

/**
 * @author cm65
 * 
 */
public class SearchPlate extends PIMSServlet {

    public static final String EXPERIMENTTYPENAME = "experimentType";

    public static final String HOLDERTYPENAME = "holderType";

    public static final String GROUP_NAME = "name";

    private static final String ONLY_GROUPS = "_only_experiment_groups";

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show custom list of plate experiments";
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

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }

        //final PrintWriter writer = response.getWriter();

        final MetaClass groupMetaClass = this.getModel().getMetaClass(ExperimentGroup.class.getName());
        try {

            final PlateCriteria criteria = this.getPlateCriteria(request);
            final List<PlateBean> plateBeans = PlateExperimentDAO.getPlateBeanList(version, criteria);
            final int resultsize = PlateExperimentDAO.countPlateBeanList(version, criteria);

            ExperimentType experimentType = null;
            final String selectExpTypeHook = request.getParameter(SearchPlate.EXPERIMENTTYPENAME);
            if (selectExpTypeHook != null && selectExpTypeHook.length() > 0) {
                experimentType = version.get(selectExpTypeHook);
            }
            HolderType holderType = null;
            final String selectHolderTypeHook = request.getParameter(SearchPlate.HOLDERTYPENAME);
            if (selectHolderTypeHook != null && selectHolderTypeHook.length() > 0) {
                holderType = version.get(selectHolderTypeHook);
            }

            if (this.isValid(request.getParameter(SearchPlate.EXPERIMENTTYPENAME))) {
                request.setAttribute(SearchPlate.EXPERIMENTTYPENAME, experimentType);
            }

            if (this.isValid(request.getParameter(SearchPlate.HOLDERTYPENAME))) {
                request.setAttribute(SearchPlate.HOLDERTYPENAME, holderType);
            }

            if (this.isValid(request.getParameter(SearchPlate.GROUP_NAME))) {
                request.setAttribute(SearchPlate.GROUP_NAME, request.getParameter("name"));
            }

            request.setAttribute("experimentTypes", ModelObjectShortBean
                .getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));

            request.setAttribute("holderTypes", ModelObjectShortBean.getModelObjectShortBeans(version
                .findAll(HolderType.class, Collections.EMPTY_MAP)));

            request.setAttribute("totalRecords", PlateExperimentDAO.getTotalPlateExperimentCount(version));
            request.setAttribute("resultSize", resultsize);
            request.setAttribute("isAdmin", PIMSServlet.isAdmin(request));
            request.setAttribute("plates", plateBeans);
            request.setAttribute("pagesize", criteria.getLimit());
            request.setAttribute("groupMetaClass", groupMetaClass);
            request.setAttribute("searchMetaClass", groupMetaClass);
            response.setStatus(HttpServletResponse.SC_OK);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/plateExperiment/SearchPlate.jsp");
            rd.forward(request, response);
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final ServletException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    private PlateCriteria getPlateCriteria(final HttpServletRequest request) {
        final PlateCriteria criteria = new PlateCriteria();
        criteria.setExpTypeName(request.getParameter(SearchPlate.EXPERIMENTTYPENAME));
        if ("true".equals(request.getParameter(SearchPlate.ONLY_GROUPS))) {
            criteria.setOnlyGroups(true);
        } else {
            criteria.setHoldTypeName(request.getParameter(SearchPlate.HOLDERTYPENAME));
        }
        if (null != request.getParameter(QuickSearch.SEARCH_ALL)) {
            criteria.setPlateName(request.getParameter(QuickSearch.SEARCH_ALL));
        } else {
            criteria.setPlateName(request.getParameter(SearchPlate.GROUP_NAME));
        }

        //page preparation
        int pageSize = 20;
        int pageStart = 0;

        pageStart = QuickSearch.getPageStartForDisplayTag(request.getParameterMap(), pageSize);
        if (this.isValid(request.getParameter("pagesize"))) {
            pageSize = Integer.parseInt(request.getParameter("pagesize"));
        }

        criteria.setLimit(pageSize);
        criteria.setStart(pageStart);
        return criteria;
    }

    private boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
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
        throw new ServletException("cannot post");
    }

}
