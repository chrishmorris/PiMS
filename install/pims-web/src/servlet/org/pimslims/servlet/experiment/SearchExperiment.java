/**
 * 
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.create.ValueConverter;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.report.ExperimentReport;
import org.pimslims.report.Filtered;
import org.pimslims.report.ThroughputReport;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.SearchFilter;
import org.pimslims.servlet.report.T2CReport;

// import org.pimslims.search.Conditions;

/**
 * @author cm65
 * 
 */
public class SearchExperiment extends PIMSServlet {

    /**
     * _LAB_NOTEBOOKS String
     */
    private static final String _LAB_NOTEBOOKS = "_labNotebooks";

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show custom list of experiments";
    }

    /*
     *
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // show all requested parameters
        final Map<String, Filtered> parameterFilter = new LinkedHashMap();
        String[] words = request.getParameterValues(T2CReport.KEYWORD);
        if (null == words) {
            words = new String[] {};
        }
        final HashSet<String> selectedKeywords = new HashSet<String>(Arrays.asList(words));
        for (int i = 0; i < words.length; i++) {
            parameterFilter.put(words[i], Filtered.IN);
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }

        final Map params = request.getParameterMap();

        final MetaClass experimentMetaClass = this.getModel().getMetaClass(Experiment.class.getName());
        this.writeHead(request, response, "Search Single Experiments");
        try {

            // prepare chart  response
            //TODO call doGetCriteria
            final SearchFilter criteria = QuickSearch.getCriteria(version, experimentMetaClass, params);
            final SearchFilter criteria1 = new SearchFilter(criteria);
            SearchExperiment.updateCriteria(criteria1);
            Calendar start = Calendar.getInstance();
            if (null == request.getParameter("_START")) {
                start.add(Calendar.YEAR, -1);
            } else {
                start = ValueConverter.doGetCalendar(request.getParameter("_START"));
            }
            request.setAttribute("start", start);
            Calendar end = null;
            if (null != request.getParameter("_END")) {
                end = Calendar.getInstance();
                end.setTimeInMillis(Long.parseLong(request.getParameter("_END")));
            }

            //TODO duplicate code with QuickSearch
            final List<String> notebookList = new ArrayList();
            Collection<LabNotebook> allbooks = version.getReadableLabNotebooks();
            if (version.isAdmin()) {
                allbooks = version.getAll(LabNotebook.class, new Paging(0, 200));
            }
            for (final Iterator iterator = allbooks.iterator(); iterator.hasNext();) {
                final LabNotebook book = (LabNotebook) iterator.next();
                notebookList.add(book.getName());
            }
            Collections.sort(notebookList);
            request.setAttribute("labNotebooks", notebookList);

            final ExperimentReport report =
                new ExperimentReport(version, criteria1.getMap(), request.getParameter("search_all"), start,
                    end);
            final String[] bookNames = request.getParameterValues(LabBookEntry.PROP_ACCESS);
            Collection<LabNotebook> selectedBooks = null;
            if (null != bookNames) {
                selectedBooks = new HashSet();
                for (int i = 0; i < bookNames.length; i++) {
                    final LabNotebook book =
                        version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, bookNames[i]);
                    selectedBooks.add(book);
                }
                report.setLabNotebooks(selectedBooks);
            }
            request.setAttribute("resultSize", report.count());

            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), experimentMetaClass);
            QuickSearch.setPagingAttributes(request, paging);
            final Collection<ExperimentBean> beans = new ArrayList<ExperimentBean>();
            for (final ModelObject mObj : report.getResults(paging)) {
                final Experiment exp = (Experiment) mObj;
                final ExperimentBean bean = new ExperimentBean(exp);
                beans.add(bean);
                T2CReport.processExperimentBean(parameterFilter, selectedKeywords, new HashSet(), bean);
            }
            request.setAttribute("allKeywords", parameterFilter.keySet());

            //paging.addOrderBy(Experiment.PROP_STARTDATE, Order.DESC);
            if ("KPI".equals(request.getParameter("_presentation"))) {
                request.setAttribute("chart", report.getAttrition(request.getRequestURI()));
            } else if ("throughput".equals(request.getParameter("_presentation"))) {
                request.setAttribute("throughput",
                    new ThroughputReport(parameterFilter, report.getResults(paging)));
            } else /* list */{
                //final long start = System.currentTimeMillis();
                //  System.out.println("SearchExperiment found " + ret.size() + " SINGLE experiments using "
                //      + (System.currentTimeMillis() - start) + "ms");
                request.setAttribute("beans", beans);
            }

            if ("yes".equals(request.getParameter("_BARCHART_ONLY"))) {
                final RequestDispatcher rd = request.getRequestDispatcher("/JSP/search/barChart.jsp");
                rd.include(request, response);
                version.commit();
                return;
            }

            QuickSearch.prepareSearchForm(request, version, experimentMetaClass, criteria);

            // now prepare search form
            if (!version.isAdmin()) {
                final Map<ModelObjectShortBean, String> books = new HashMap(); // bean => selected
                final Collection<LabNotebook> readableLabNotebooks = version.getReadableLabNotebooks();
                if (null == selectedBooks) {
                    selectedBooks = readableLabNotebooks;
                }
                for (final Iterator iterator = readableLabNotebooks.iterator(); iterator.hasNext();) {
                    final LabNotebook book = (LabNotebook) iterator.next();
                    books.put(new ModelObjectShortBean(book), selectedBooks.contains(book)
                        ? " selected=\"selected\" " : "");
                }
                request.setAttribute(SearchExperiment._LAB_NOTEBOOKS, books);
            }
            ExperimentType type = null;
            final String[] types = request.getParameterValues("experimentType");
            if (null != types && 1 == types.length && !"".equals(types[0])) {
                type = version.get(types[0]);
                request.setAttribute("experimentType", type);
            }
            // set protocols TODO from workflow TODO from request
            if (type == null) {
                //TODO get protocols from MRU
            } else {
                final Collection<Protocol> protocols = type.getProtocols();
                request.setAttribute("protocols", ModelObjectShortBean.getModelObjectShortBeans(protocols));
            }

            final String[] protocols = request.getParameterValues("protocol");
            if (null != protocols && 1 == protocols.length && !"".equals(protocols[0])) {
                request.setAttribute("protocol", new ModelObjectShortBean(version.get(protocols[0])));
            }

            request.setAttribute("experimentTypes", ModelObjectShortBean
                .getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));

            request.setAttribute("totalRecords", SearchExperiment.getTotalSingleExperimentCount(version));
            request.setAttribute("isAdmin", PIMSServlet.isAdmin(request));
            //request.setAttribute("experimentType", type);
            //TODO also count use of tracked consumables http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-3572

            request.setAttribute("experimentMetaClass", experimentMetaClass);
            request.setAttribute("searchMetaClass", experimentMetaClass);
            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/search/org.pimslims.model.experiment.Experiment.jsp");
            rd.include(request, response);
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

        // was PIMSServlet.writeFoot(writer, request);

    }

    static void updateCriteria(final SearchFilter criteria) {
        criteria.remove("SUBMIT");
        criteria.remove("search_all");
        //criteria for single experiment
        final Map<String, Object> expGroupAndSampleCriteria = SearchExperiment.onlySingleExperiments();
        //only plate exp's expgroup and sample holder are not null: ((!null)&(!null))
        //!((!null)&&(!null))=null||null
        if (criteria.isEmpty()) {
            criteria.putAll(expGroupAndSampleCriteria);
        } else {
            criteria.put("OR", expGroupAndSampleCriteria);
        }
    }

    /**
     * SearchExperiment.onlySingleExperiments
     * 
     * @return
     */
    static Map<String, Object> onlySingleExperiments() {
        final Map<String, Object> sampleCriteria = new HashMap<String, Object>();
        sampleCriteria.put(Sample.PROP_HOLDER, Conditions.isNull());
        final Map<String, Object> osCriteria = new HashMap<String, Object>();
        osCriteria.put(OutputSample.PROP_SAMPLE, sampleCriteria);
        final Map<String, Object> expGroupAndSampleCriteria = new HashMap<String, Object>();
        expGroupAndSampleCriteria.put(Experiment.PROP_OUTPUTSAMPLES, osCriteria);
        expGroupAndSampleCriteria.put(Experiment.PROP_EXPERIMENTGROUP, Conditions.isNull());
        return expGroupAndSampleCriteria;
    }

    static Integer getTotalSingleExperimentCount(final ReadableVersion version) {
        final Map<String, Object> expGroupAndSampleCriteria = SearchExperiment.onlySingleExperiments();
        //only plate exp's expgroup and sample holder are not null: ((!null)&(!null))
        //!((!null)&&(!null))=null||null
        final Map criteria = new HashMap<String, Object>();
        criteria.put("OR", expGroupAndSampleCriteria);

        return version.count(Experiment.class, criteria);
    }

    private static boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

    private static Collection<ExperimentBean> getExperimentBeans(final List<Experiment> results) {
        //final long start = System.currentTimeMillis();
        final Collection<ExperimentBean> ret = new ArrayList<ExperimentBean>();
        for (final ModelObject mObj : results) {
            final Experiment exp = (Experiment) mObj;

            ret.add(new ExperimentBean(exp));
        }
        //  System.out.println("SearchExperiment found " + ret.size() + " SINGLE experiments using "
        //      + (System.currentTimeMillis() - start) + "ms");

        return ret;
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
