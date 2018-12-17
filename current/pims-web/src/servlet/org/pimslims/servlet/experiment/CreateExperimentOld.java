package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.DefaultExperimentName;
import org.pimslims.presentation.experiment.ExperimentCreator;
import org.pimslims.presentation.experiment.ExperimentCreator.ExperimentCreationForm;
import org.pimslims.presentation.experiment.ExperimentNameFactory;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.plateExperiment.CreateExperimentGroup;

/**
 * @baseURL /Create/org.pimslims.model.experiment.Experiment
 * @author cm65
 * 
 */
@Deprecated
// obsolete
public class CreateExperimentOld extends PIMSServlet {

    /**
     * EXPERIMENT_TYPE_HOOK String
     */
    private static final String EXPERIMENT_TYPE_HOOK = "experimentType";

    /**
     * PROJECT_HOOK String
     */
    private static final String PROJECT_HOOK = Experiment.PROP_PROJECT;

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Not ready to save, prompt for next needed input

        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            if (null != request.getParameter("protocolHook")) {
                throw new ServletException("Can only save on submit");
            } else if (null != request.getParameter(CreateExperimentOld.EXPERIMENT_TYPE_HOOK)) {
                // already got experiment type, ask about protocol
                this.protocolPrompt(version, request, response);
            } else if (null != request.getParameter("experimentProtocolHook")) {
                // already got experiment type, ask about protocol
                this.experimentNamePrompt(version, request, response);
            } else {
                // ask about experiment type
                this.experimentTypePrompt(version, request, response);
            }
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
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

        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final String username = PIMSServlet.getUsername(request);

            final ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
            form.set_Input(request.getParameter("_Input"));
            form.setProtocolHook(request.getParameter("protocolHook"));
            final Protocol protocol = version.get(request.getParameter("protocolHook"));
            assert protocol != null : "protocol should not be null";
            form.setExperimentTypeHook(protocol.getExperimentType().get_Hook());
            form.setExperimentBlueprintHook(request.getParameter(CreateExperimentOld.PROJECT_HOOK));
            form.setExperimentName(request.getParameter("experimentName"));

            if (null != request.getParameter("projectHook")) {
                form.setLabNotebookHook(request.getParameter("projectHook"));
            } else if (null != request.getParameter("_Input")) {
                final Sample input = version.get(request.getParameter("_Input"));
                if (null != input) {
                    form.setLabNotebookHook(input.getAccess().get_Hook());
                }
            }

            final String hook = this.save(version, form, request, response);

            org.pimslims.presentation.mru.MRUController.addObject(username, protocol);

            version.commit();
            this.redirect(response, request.getContextPath() + "/View/" + hook + "#parameters");
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * First stage: prompt for experiment type
     * 
     * @param version
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private void experimentTypePrompt(final ReadableVersion version, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("results",
            ModelObjectShortBean.getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));

        /* MRU stuff, but it doesn't work */
        final String user = super.getUsername(request);

        //final List recentProtocols = MRUController.getMRUs(user, "org.pimslims.model.protocol.Protocol");
        final List<Protocol> recentProtocols = new LinkedList<Protocol>();
        //get protocol from MRU and select active ones
        for (final ModelObjectShortBean mruProtocol : MRUController.getMRUs(user,
            "org.pimslims.model.protocol.Protocol")) {
            final Protocol protocol = version.get(mruProtocol.getHook());
            if (protocol.getIsForUse() != null && protocol.getIsForUse()) {
                recentProtocols.add(protocol);
            }
        }

        request.setAttribute("recentProtocols", recentProtocols);

        /* // prepare the input fields
        final Map<String, String> control = new java.util.HashMap<String, String>();
        int counter = 0;
        for (final ModelObject result : CreateExperiment.activeExperimentTypes(version)) {
            final String selected = (counter == 0 ? "checked=\"checked\"" : "");
            counter++;
            control.put(result.get_Hook(), "<input  title=\"choose?\" type=\"radio\" " + selected
                + " name=\"experimentTypeHook\" value=\"" + result.get_Hook() + "\" />");
        }
        //request.setAttribute("control", control); */

        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/experiment/ChooseExperimentType.jsp");
        rd.forward(request, response);
    }

    /**
     * Second stage: prompt for protocol
     * 
     * @param version
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws AccessException
     * @throws ServletException
     */
    private void protocolPrompt(final ReadableVersion version, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, AccessException, ServletException {
        final MetaClass protocol =
            version.getModel().getMetaClass(org.pimslims.model.protocol.Protocol.class.getName());
        final ModelObject experimentType =
            version.get(request.getParameter(CreateExperimentOld.EXPERIMENT_TYPE_HOOK));
        assert null != experimentType : "No such experiment type: "
            + request.getParameter(CreateExperimentOld.EXPERIMENT_TYPE_HOOK);
        request.setAttribute("modelObject", BeanFactory.newBean(experimentType));

        final Collection results = CreateExperimentGroup.getProtocols(version, request, experimentType);

        // for Ajax List
        if (null != request.getParameter("isAJAX")
            && "true".equals(request.getParameter("isAJAX").toString())) {
            response.setContentType("text/xml");
            final XMLOutputter xo = new XMLOutputter();
            xo.output(CreateExperimentGroup.makeAjaxListXML(results), response.getWriter());
            return;
        }
        // could add unspecified protocol
        // get the protocol pre-selected
        final String protocolHook = request.getParameter("protocol");
        // prepare the input fields
        final Map<String, String> control = new java.util.HashMap<String, String>();
        int counter = 0;
        for (final Iterator iter = results.iterator(); iter.hasNext();) {
            final ModelObject result = (ModelObject) iter.next();
            String selected = "";
            if (protocolHook == null && counter == 0) {
                selected = "checked=\"checked\"";
            } else if (protocolHook != null && protocolHook.equals(result.get_Hook())) {
                selected = "checked=\"checked\"";
            }
            counter++;
            control.put(result.get_Hook(), "<input  title=\"choose?\" type=\"radio\" " + selected
                + " name=\"protocolHook\" value=\"" + result.get_Hook() + "\" />");
        }
        request.setAttribute("control", control);
        request.setAttribute("listMetaClass", protocol);
        request.setAttribute("controlHeader", "Choose?");
        request.setAttribute("results", ModelObjectShortBean.getModelObjectShortBeans(results));

        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/experiment/ChooseExperimentType.jsp");
        rd.forward(request, response);
    }

    /**
     * 
     * CreateExperiment.experimentNamePrompt
     * 
     * @param version
     * @param request
     * @param response
     * @throws IOException
     * @throws AccessException
     * @throws ServletException
     */
    private void experimentNamePrompt(final ReadableVersion version, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, AccessException, ServletException {

        //final MetaClass protocol =             version.getModel().getMetaClass(org.pimslims.model.protocol.Protocol.class.getName());
        final Protocol experimentProtocol = version.get(request.getParameter("experimentProtocolHook"));
        assert null != experimentProtocol : "No such protocol: "
            + request.getParameter("experimentProtocolHook");

        final ModelObject experimentType = experimentProtocol.getExperimentType();
        assert null != experimentType;

        request.setAttribute("modelObject", BeanFactory.newBean(experimentProtocol));
        final ExperimentNameFactory enf =
            PropertyGetter.getInstance("Experiment.Name.Factory", DefaultExperimentName.class);
        //experiment.setName(enf.suggestExperimentName(this.wv, experiment, inputSampleName));

        final Collection<LabNotebook> accessObjects = version.getCurrentProjects();

        // for Ajax List
        if (null != request.getParameter("isAJAX")
            && "true".equals(request.getParameter("isAJAX").toString())) {
            response.setContentType("text/xml");
            final XMLOutputter xo = new XMLOutputter();
            String experimentName;
            Project project = null;
            if (null != request.getParameter(CreateExperimentOld.PROJECT_HOOK)) {
                project =
                    (ResearchObjective) version.get(request.getParameter(CreateExperimentOld.PROJECT_HOOK));
            }
            if (null == project) {
                experimentName = enf.suggestExperimentName(version, experimentProtocol);
            } else {
                experimentName = enf.suggestExperimentName(version, experimentProtocol, project);
            }
            //xo.output(this.makeAjaxStringXML(experimentName), response.getWriter());

            final Element rootElement = new Element("string");
            rootElement.addContent(CreateExperimentOld.makeAjaxStringXML("experiment", experimentName));

            for (final ModelObject object : accessObjects) {
                rootElement.addContent(CreateExperimentOld.makeAjaxStringXML("project", object.get_Name(),
                    object.get_Hook()));
            }

            xo.output(new Document(rootElement), response.getWriter());
            return;
        }
    }

    public static Element makeAjaxStringXML(final String tag, final String result) {
        final Element elem = new Element(tag);
        elem.setAttribute("name", result);
        return elem;
    }

    public static Element makeAjaxStringXML(final String tag, final String name, final String hook) {
        final Element elem = new Element(tag);
        elem.setAttribute("name", name);
        elem.setAttribute("hook", hook);
        return elem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "custom create for an experiment";
    }

    /**
     * Last stage: save the experiment
     * 
     * @param version
     * @param form
     * @param request
     * @param response
     * @return the hook of the new experiment
     * @throws ServletException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     */
    private String save(final WritableVersion version, final ExperimentCreator.ExperimentCreationForm form,
        final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
        IOException, AccessException, ConstraintException {
        final ExperimentCreator presenter = new ExperimentCreator(form, version);
        final Experiment experiment = presenter.save();
        ExperimentUtility.setExpBlueprintSamples(experiment, (ResearchObjective) experiment.getProject());
        // no longer ExperimentTree.reloadExperimentTree(request, version);
        return experiment.get_Hook();
    }

    public static Collection<Protocol> activeProtocols(final Collection<Protocol> protocols) {
        final Collection<Protocol> collection = new HashSet();
        for (final Protocol p : protocols) {
            if (null == p.getIsForUse() || p.getIsForUse()) {
                collection.add(p);
            }
        }
        return collection;
    }

}
