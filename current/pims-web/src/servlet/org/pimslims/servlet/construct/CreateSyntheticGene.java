package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.spot.SpotNewTarget;

/**
 * Servlet implementation class for Servlet: CreateSyntheticGene
 * 
 */
public class CreateSyntheticGene extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {

    /* (non-Javadoc)
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create Synthetic gene sample";
    }

    /* (non-Java-doc)
    * @see javax.servlet.http.HttpServlet#HttpServlet()
    */
    public CreateSyntheticGene() {
        super();
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // Get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }

        try {
            final SyntheticGeneBean psgb = new SyntheticGeneBean();
            //This is only created from a link on a Target view
            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                throw new ServletException("No target specified");
            }
            final String hook = pathInfo.substring(1);
            final Target target = version.get(hook);

            //For development get any old target
            //final Target target = version.findFirst(Target.class, Collections.EMPTY_MAP);
            final TargetBean tb = new TargetBean(target);
            psgb.setTargetBean(tb);

            request.setAttribute("psgb", psgb);

            //Need a collection of all the vector input samples to create an mru
            final SampleCategory samCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
            if (null == samCat) {
                this.writeErrorHead(request, response, "No Sample category called Vector in PiMS",
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(AbstractSample.PROP_SAMPLECATEGORIES, samCat);
            final Collection<Sample> vectors = version.findAll(Sample.class, attributes);
            request.setAttribute("vectors", ModelObjectBean.getModelObjectBeans(vectors));

            //Need a collection of people for the Scientist -from SpotNewTarget
            SpotNewTarget.setPeople(request, version);

            //Lab notebook -accessobjects
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/construct/CreateSyntheticGene.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     * Post causes a Synthetic gene sample to be created
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final SyntheticGeneBean sgb = new SyntheticGeneBean();

        // Get a WritableVersion
        final WritableVersion wversion = this.getWritableVersion(request, response);
        if (wversion == null) {
            return; // error message has already been sent
        }

        String hook = null;

        try {
            if (null != request.getParameter("accessId")) {
                final ModelObject access = wversion.get(request.getParameter("accessId"));
                if (null != access) {
                    wversion.setDefaultOwner((LabNotebook) access);
                }
            }

            //Need the Synthetic gene name
            String name = "";
            //final String name = request.getParameter("sgName");
            if (null != request.getParameter("sgName")) {
                name = request.getParameter("sgName");
            }
            sgb.setSgeneName(name);

            //Need the target
            final String targetHook = request.getParameter("pims_target_hook");
            Target target = null;
            TargetBean tb = new TargetBean();
            if (null != targetHook) {
                target = (Target) wversion.get(targetHook);
                tb = new TargetBean(target);
                sgb.setTargetBean(tb);
            }

            //DNA Sequence
            String sgDNASeq = "";
            if (null != request.getParameter("dnaSeq")) {
                sgDNASeq = request.getParameter("dnaSeq").replaceAll(" ", "");
            }
            sgb.setDnaSeq(sgDNASeq);

            //proteinSeq
            String sgProtSeq = "";
            if (null != request.getParameter("protSeq")) {
                sgProtSeq = request.getParameter("protSeq");
            }
            sgb.setProteinSeq(sgProtSeq);

            //The vector input sample
            if (null != request.getParameter("vector")) {
                if ("[none]" != request.getParameter("vector")) {
                    final String vectorHook = request.getParameter("vector");
                    sgb.setVectorHook(vectorHook);
                }
            }

            hook = SyntheticGeneManager.save(wversion, sgb);
            System.out.println("Hook after creating is: " + hook);

            //Process the selected input sample
            final Sample sgSample = wversion.get(hook);
            final Experiment experiment = sgSample.getOutputSample().getExperiment();

            Sample inputVector = null;
            String vecHook = "";
            if (null != sgb.getVectorHook()) {
                vecHook = sgb.getVectorHook();
                inputVector = wversion.get(vecHook);
                final Map<String, Object> criteria = new HashMap<String, Object>();
                criteria.put(InputSample.PROP_NAME, "Vector");
                final InputSample is = experiment.findFirst(Experiment.PROP_INPUTSAMPLES, criteria);
                assert null != is : "No such input sample in protocol: " + name;
                is.setSample(inputVector);
            }
            //Can change parameters here
            this.setGeneParameters(request, experiment);

            wversion.commit();

        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wversion.isCompleted()) {
                wversion.abort();
            }
        }
        // now show the new Synthetic gene
        PIMSServlet.redirectPost(response, request.getContextPath() + "/read/SyntheticGene/" + hook);
    }

    /**
     * CreateSyntheticGene.setGeneParameters
     * 
     * @param request
     * @param experiment
     * @throws ConstraintException
     */
    public void setGeneParameters(final HttpServletRequest request, final Experiment experiment)
        throws ConstraintException {
        String threePrime = "";
        if (null != request.getParameter("threePrimeSite")) {
            threePrime = request.getParameter("threePrimeSite");
        }
        String fivePrime = "";
        if (null != request.getParameter("fivePrimeSite")) {
            fivePrime = request.getParameter("fivePrimeSite");
        }
        String host = "";
        if (null != request.getParameter("expressionHost")) {
            host = request.getParameter("expressionHost");
        }
        String vectorRes = "";
        if (null != request.getParameter("vectorRes")) {
            vectorRes = request.getParameter("vectorRes");
        }

        final Set<Parameter> expParams = experiment.getParameters();
        for (final Parameter expParam : expParams) {
            if (expParam.getParameterDefinition().getName().equals("3'-Restriction site")) {
                expParam.setValue(threePrime);
            }
            if (expParam.getParameterDefinition().getName().equals("5'-Restriction site")) {
                expParam.setValue(fivePrime);
            }
            if (expParam.getParameterDefinition().getName().equals("Vector resistances")) {
                expParam.setValue(vectorRes);
            }
            if (expParam.getParameterDefinition().getName().equals("Optimized for expression in:")) {
                expParam.setValue(host);
            }
        }
    }
}
