package org.pimslims.servlet.orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.presentation.PimsQuery;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;
import org.pimslims.utils.orders.operon.PrimerOrderFormImpl;

public class OrderPrimers extends PIMSServlet {
    @Override
    public String getServletInfo() {
        return "Primer Plate Order";
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

        // If the the file was sent

        System.out.println("org.pimslims.servlet.order.PrimerOrderForm.doGet");
        if (!this.checkStarted(request, response)) {
            return;
        }
        final ReadableVersion rv = super.getReadableVersion(request, response);
        try {
            request.setAttribute("holdersOfOrder", OrderPrimers.getPrimerOrderList(rv));
            rv.commit();
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/oppf/PrimerOrderForm.jsp");
            dispatcher.forward(request, response);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
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

        // System.out.println("org.pimslims.servlet.order.OrderPrimers.doPost");
        // for (Iterator iter = request.getParameterMap().entrySet().iterator();
        // iter.hasNext();) {
        // Map.Entry entry = (Map.Entry)iter.next();
        // String key = (String)entry.getKey();
        // String[] values = (String[])entry.getValue();
        // for (int i = 0; i < values.length; i++)
        // System.out.println("Parameter ["+key+","+values[i]+"]");
        // }

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }

        final String hook = request.getParameter("primerform");

        try {

            if (null == hook) {
                throw new ServletException("No Primer Order to process");
            }

            final PrimerOrderFormImpl of = new PrimerOrderFormImpl();

            of.loadFromOrderExperiment(version, hook);

            final File file = of.saveOrderForm(version.get(hook), version);
            final String requestString =
                new String(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath() + "/ViewFile/" + file.getHook()
                    + "/" + file.getName());
            System.out.println("org.pimslims.servlet.order.OrderPrimers request [" + requestString + "]");
            response.sendRedirect(requestString);
            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final AccessException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final ConstraintException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            if (version != null) {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }
        }
    }

    /**
     * get all holders which exp type is 'Order'
     * 
     * @param rv
     * @return LinkedHashMap<hook,name> which is ordered
     * @throws ConstraintException
     * @throws AccessException
     */
    static LinkedHashMap<String, String> getPrimerOrderList(final ReadableVersion rv) {

        /*
         * What we want to end up with is a LinkedHashMap<String, String> of all the ExperimentGroup's
         * containing experiments of type 'Order' where the key is the ExperimentGroup's hook and the
         * value is the ExperimentGroup's name, sorted by name. Oh, I see, sorted by hook would give
         * order of creation (providing a sequential id generator is in use). I still think name is
         * easier for users.
         * 
         * TODO ExperimentType could be more specific!
         * 
         * Is name unique? If so, why not have name as the key and hook as the value? Ah well, stick with what we've got.
         * 
         * So, HQL is:
         */
        final List<String> selectCriteria = new ArrayList<String>();
        selectCriteria.add("exp.experimentType.name = 'Order'");
        //TODO needs access control
        final String selectWhereHQL =
            JpqlQuery.getWhereHQL(rv, "exp.experimentGroup", selectCriteria, ExperimentGroup.class);
        final String selectHQL =
            " select distinct exp.experimentGroup.id, exp.experimentGroup.name from "
                + Experiment.class.getName() + "  exp " + selectWhereHQL
                + " order by exp.experimentGroup.name desc";
        // TODO where status is to be run or in process

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL);

        //final long start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final List results = query.list();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");

        final LinkedHashMap<String, String> orderExperimentGroups = new LinkedHashMap<String, String>();
        for (final Object record : results) {
            // Record is an Object[] of form [ (Long) id, (String) name ]
            // DB constraints mean neither can be null
            final Object[] rec = (Object[]) record;
            // TODO For maintainability, is there some method in model to which I can give a class
            // derived from ModelObject and an id and get the hook back?
            // Ie enforce the same behaviour as org.pimslims.metamodel.AbstractModelObject.get_Hook()
            orderExperimentGroups.put(ExperimentGroup.class.getName() + ":" + rec[0].toString(),
                (String) rec[1]);
        }

        return orderExperimentGroups;

    }
}
