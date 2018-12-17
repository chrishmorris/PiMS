/**
 * 
 */
package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.vector.VectorBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ViewRefSample extends PIMSServlet {

    private static final Map<String, Object> ACTIVE = new HashMap(1);
    static {
        ViewRefSample.ACTIVE.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
    }

    /**
     * 
     */
    public ViewRefSample() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a recipe";
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

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            MetaClass metaClass = null; // type to show, if any
            final RefSample refSample = (RefSample) version.get(pathInfo); // e.g.
            // Example/org.pimslims.model.experiment.Experiment:42355
            if (null == refSample) {
                this.writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("refsample", BeanFactory.newBean(refSample));
            final Set<SampleCategory> categories = refSample.getSampleCategories();
            String category = "";
            if (1 == categories.size()) {
                category = categories.iterator().next().getName();
            }
            final Set<SampleComponent> components = refSample.getSampleComponents();
            request.setAttribute("components", ModelObjectBean.getModelObjectBeans(components));
            request.setAttribute("categories", ModelObjectShortBean.getModelObjectShortBeans(categories));
            final Collection<Sample> stocks = ViewRefSample.getActiveSample(version, refSample);

            request.setAttribute("stocks", ModelObjectShortBean.getModelObjectShortBeans(stocks));

            request.setAttribute("sampleSources", refSample.getRefSampleSources());
            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(refSample));
            metaClass = refSample.get_MetaClass();
            request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));
            request.setAttribute("mayUpdate", ViewRefSample.getMayUpdate(refSample));
            request.setAttribute("owner", refSample.get_Owner());
            //request.setAttribute("userPersons", PersonUtility.getHookAndName(PersonUtility
            //    .getUserPersons(version)));

            final Collection<Person> people =
                this.getAllSorted(version, org.pimslims.model.people.Person.class);
            //request.setAttribute("userPersons", ModelObjectBean.getModelObjectBeans(people));

            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            if ("Vector".equals(category)) {
                final VectorBean vectorBean = VectorBean.getVectorBean(refSample);
                request.setAttribute("vector", vectorBean);
            }

            PIMSServlet.dispatchCustomJsp(request, response, category, "recipe", this.getServletContext());
            version.commit();
        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }

    }

    private static Collection<Sample> getActiveSample(final ReadableVersion version, final RefSample refSample) {
        final Map<String, Object> sampleCriterials = new HashMap<String, Object>();
        sampleCriterials.put(Sample.PROP_REFSAMPLE, refSample);
        sampleCriterials.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        final Collection<Sample> stocks = version.findAll(Sample.class, sampleCriterials);
        return stocks;
    }

    private Collection getAllSorted(final ReadableVersion version, final Class javaClass)
        throws ServletException {

        final List list = new ArrayList();
        list.addAll(PIMSServlet.getAll(version, javaClass));
        Collections.sort(list, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return list;
    }

    /**
     * @param refSample
     * @return
     */
    static boolean getMayUpdate(final RefSample refSample) {
        final Collection<Sample> samples = ViewRefSample.getActiveSample(refSample.get_Version(), refSample);
        if (null != samples && samples.size() > 0) {
            return false;
        }
        return refSample.get_MayUpdate();
    }
}
