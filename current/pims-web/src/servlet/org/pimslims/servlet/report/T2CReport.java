package org.pimslims.servlet.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.DotGraphUtil;
import org.pimslims.graph.GraphGenerationException;
import org.pimslims.graph.GrappaModelLoader;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.graph.implementation.ModelObjectNode;
import org.pimslims.graph.implementation.SampleProvenanceModel;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.ComplexBeanReader;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.pdf.PdfReport;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;
import org.pimslims.utils.ExecRunnerException;
import org.pimslims.utils.ExecRunnerException.BadPathException;

/**
 * Servlet for generating Graph svg data
 * 
 * @author Marc Savitsky
 * @since 16 April 2008
 * 
 */
public class T2CReport extends PIMSServlet {

    public static final String ErrorMessage_NoHook =
        "there is no object in the system that can be accessed by this url";

    private static final String info = "Shows a graph created by GraphViz software ";

    public static final String EXPERIMENTS = "Experiments";

    // context type svg image

    public static final String MIME_IMAGE_SVG = "image/svg+xml";

    public static final String MIME_IMAGE_PDF = "application/pdf";

    public static final String PRESENTATION_HTML = "html";

    public static final String PRESENTATION_PDF = "pdf";

    public static final String PRESENTATION_XML = "xml";

    /**
     * 
     */
    public T2CReport() {
        super();
    }

    /**
     * creates graph image as svg and puts it into servlet output stream usage: call this servlet from html:
     * <embed src="Graph" name="chart" width="800" height="300" type="image/svg+xml"
     * pluginspage="http://www.adobe.com/svg/viewer/install/"/>
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("T2CReport doGet");
        final ServletContext scontext = this.getServletContext();
        final HttpSession session = request.getSession();

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // get a full path to dot.exe from servlet context
        String dot_path = org.pimslims.properties.PropertyGetter.getStringProperty("dot_path", "dot");
        if (dot_path == null || dot_path.length() < 1) {
            dot_path = "dot";
        }

        final String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 1) {
            this.writeErrorHead(request, response, "No record specified", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // get request parameter 'presentationType'
        String presentationType = request.getParameter("presentationtype");
        if (presentationType == null || presentationType.length() == 0) {
            presentationType = T2CReport.PRESENTATION_HTML;
        }

        ReadableVersion version = null;
        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            final ModelObject object = version.get(pathInfo.substring(1));

            if (object == null) { // http 404 error
                response.sendError(404, "Not found: " + pathInfo.substring(1));
                return;
            }

            // create the model for the graph
            final SampleProvenanceModel model = new SampleProvenanceModel(object);
            final IGraph graphModel = model.createGraphModel(object); //TODO no, model.complete(sample)

            final List<ConstructBean> constructs = new ArrayList<ConstructBean>();
            final List<Experiment> myexperiments = new ArrayList<Experiment>();
            final List<TargetBean> targets = new ArrayList<TargetBean>();
            final Collection<Target> targetList = new ArrayList<Target>();
            final List<ComplexBean> complexes = new ArrayList<ComplexBean>();
            final Set<User> authors = new HashSet<User>();

            final List<Experiment> experiments = new ArrayList<Experiment>();
            final Map<String, Collection<File>> images = new HashMap();
            for (final INode node : graphModel.getNodes()) {
                final ModelObject modelObject = version.get(((ModelObjectNode) node).getHook());

                if (modelObject instanceof Experiment) {

                    final Experiment experiment = (Experiment) modelObject;
                    experiments.add(experiment);
                    images.put(experiment.get_Hook(), PdfReport.getImages(experiment));
                    if (null != experiment.getCreator()) {
                        authors.add(experiment.getCreator());
                    }
                    myexperiments.add(experiment);

                } else if (modelObject instanceof ResearchObjective) {

                    final ResearchObjective expBlueprint = (ResearchObjective) modelObject;
                    for (final ResearchObjectiveElement blueprintComponent : expBlueprint
                        .getResearchObjectiveElements()) {
                        if (null != blueprintComponent.getCreator()) {
                            authors.add(blueprintComponent.getCreator());
                        }
                        if (blueprintComponent.getComponentType().equals("complex")) {
                            final ComplexBean complex = ComplexBeanReader.readComplexBean(expBlueprint);
                            complexes.add(complex);
                            break;
                        } else {
                            final ConstructBean bean = ConstructBeanReader.readConstruct(expBlueprint);
                            constructs.add(bean);
                            break;
                        }
                    }

                } else if (modelObject instanceof Target) {

                    final Target target = (Target) modelObject;
                    targetList.add(target);
                    targets.add(new TargetBean(target));
                    if (null != target.getCreator()) {
                        authors.add(target.getCreator());
                    }
                }
            }

            Collections.sort(experiments, new Comparator<Experiment>() {
                public int compare(final Experiment a, final Experiment b) {
                    return a.getDbId().compareTo(b.getDbId());
                }
            });

            final StringBuffer citation = new StringBuffer();
            for (final User author : authors) {
                if (null == author.getPerson()) {
                    citation.append(author.getName());
                } else {
                    citation.append(author.getPerson().getName());
                }
                citation.append(",");
            }

            if (citation.length() > 0) {
                citation.deleteCharAt(citation.length() - 1);
                citation.append("; ");
            }

            citation.append("PiMS: Provenance of: ");

            final Sample sample = (Sample) object;
            final SampleBean sampleBean = new SampleBean(sample);
            citation.append(sample.getName());
            citation.append("; viewed ");
            citation.append(new SimpleDateFormat().format(new Date()));
            citation.append("; <URL: ");
            citation.append(request.getRequestURL());
            citation.append(">");

            // get url context to generate url for graph nodes
            final StringBuffer sb = new StringBuffer(request.getRequestURL());
            final String URL_CONTEXT =
                sb.substring(0, sb.indexOf(request.getRequestURI())) + request.getContextPath() + "/View/";

            if (T2CReport.PRESENTATION_HTML.equals(presentationType)) {

                this.printHtmlReport(request, response, session, dot_path, object, graphModel, constructs,
                    targets, complexes, authors, experiments, images, sample, sampleBean, URL_CONTEXT);

            } else if (T2CReport.PRESENTATION_PDF.equals(presentationType)) {

                PdfReport document = null;
                document =
                    new PdfReport(response.getOutputStream(), "PiMS Sample Provenance Report    "
                        + new Date().toString(), citation.toString());
                response.setContentType("application/pdf");

                final byte[] graph =
                    this.generateGraph(object, URL_CONTEXT, dot_path, DotGraphUtil.FORMAT_PNG, graphModel);
                document.addImage(graph);

                for (final ComplexBean bean : complexes) {
                    document.showComplex(bean);
                }
                for (final TargetBean bean : targets) {
                    document.showTarget(bean);
                }
                for (final ConstructBean bean : constructs) {
                    document.showExpBlueprint(bean);
                }
                for (final Experiment experiment : experiments) {
                    document.showExperiment(new ExperimentBean(experiment), experiment);
                }
                document.showSample(sampleBean, sample);
                response.setStatus(HttpServletResponse.SC_OK);
                document.close();

            } /*else if (T2CReport.PRESENTATION_XML.equals(presentationType)) {

                final Spine2Export tse = new Spine2Export();

                //tse.process(targetList);
                for (final ComplexBean bean : this.spineComplexes(complexes, targets)) {
                    tse.addComplex(bean);
                }
                for (final TargetBean bean : targets) {
                    tse.addTarget(bean);
                }
                for (final ConstructBean bean : constructs) {
                    tse.addVirtualTarget(bean);
                }
                for (final Experiment bean : myexperiments) {
                    tse.addExperiment(bean);
                }
                tse.process();

                final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                final PrintWriter writer = response.getWriter();
                outputter.output(tse, writer);
              } */

            version.commit();

        } catch (final BadPathException de) {
            this.showBadPathPage(request, response, dot_path);
        } catch (final GraphGenerationException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (version != null && !version.isCompleted()) {
                version.abort();
            }
        }

    }// End of doGet

    private void printHtmlReport(final HttpServletRequest request, final HttpServletResponse response,
        final HttpSession session, final String dot_path, final ModelObject object, final IGraph graphModel,
        final List<ConstructBean> constructs, final List<TargetBean> targets,
        final List<ComplexBean> complexes, final Set<User> authors, final List<Experiment> experiments,
        final Map<String, Collection<File>> images, final Sample sample, final SampleBean sampleBean,
        final String URL_CONTEXT) throws IOException, GraphGenerationException, BadPathException,
        ServletException {
        // create svg graph and write it to outputStream
        /*final SVG svg =
            new SVG(new String(this.generateGraph(object, URL_CONTEXT, dot_path, DotGraphUtil.FORMAT_SVG,
                graphModel))); 

        //session.setAttribute("svg", svg); */

        final String objectName = object.getClass().getSimpleName();
        /*if (objectName.contains("ExpBlueprint")) {
            objectName = objectName.replaceAll("ExpBlueprint", "Construct/Target");
        } */

        request.setAttribute("sample", sampleBean);

        String role = "";
        if (null != sample.getOutputSample()) {
            if (null != sample.getOutputSample().getRefOutputSample()) {
                role = sample.getOutputSample().getRefOutputSample().getName();
            }
        }
        request.setAttribute("role", role);
        request.setAttribute("sampleCategories", ModelObjectShortBean.getBeans(sample.getSampleCategories()));
        request.setAttribute("expblueprints", constructs);
        final List<ExperimentBean> beans = new ArrayList(experiments.size());
        for (final Experiment experiment : experiments) {
            beans.add(new ExperimentBean(experiment));
        }
        request.setAttribute("experiments", beans);
        request.setAttribute("images", images);
        request.setAttribute("targets", targets);
        request.setAttribute("complexes", complexes);
        request.setAttribute("authors", authors);
        request.setAttribute("requesturl", request.getRequestURL());
        response.setStatus(HttpServletResponse.SC_OK);

        final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/report/T2CReport.jsp");
        dispatcher.forward(request, response);
    }

    private void showBadPathPage(final HttpServletRequest request, final HttpServletResponse response,
        final String dotPath) throws IOException {
        this.writeErrorHead(request, response, "Installation problem", HttpServletResponse.SC_BAD_REQUEST);
        final PrintWriter writer = response.getWriter();
        writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
        writer.print("<p>PIMS is unable to show your graph. Please show this page to your adminstrator.</p>");
        writer.print("<p>The GraphViz executable dot was not found at: " + dotPath + "</p>");
        writer.print("</div><br/>");
        writer.print("<a href=\"../Installation\">View installation details</a>");
    }

    /*
     * creates graph model for ModelObject
     */
    private byte[] generateGraph(final ModelObject object, final String url_context, final String dotPath,
        final String format, final IGraph graphModel) throws IOException, GraphGenerationException,
        ExecRunnerException.BadPathException {

        final String data = new GrappaModelLoader(graphModel).produceData();

        // execute dot.exe to generate graph
        final byte[] results = new DotGraphUtil().runDot(dotPath, format, data);

        return results;
    }// End of generateGraph

    /**
     * is this workflow suitable for export to spine?
     * 
     * @param complexes
     * @param targets
     * @return
     */
    private boolean canSpineExport(final Collection<ComplexBean> complexes,
        final Collection<TargetBean> targets) {

        final Collection<ComplexBean> myComplexes = this.spineComplexes(complexes, targets);

        if (myComplexes.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * return a collection of complexbeans that match all targets
     * 
     * @param complexes
     * @param targets
     * @return
     */
    private Collection<ComplexBean> spineComplexes(final Collection<ComplexBean> complexes,
        final Collection<TargetBean> targets) {

        final Collection<ComplexBean> myComplexes = new HashSet<ComplexBean>();
        myComplexes.addAll(complexes);

        // if there is more than 1 complex,
        // make sure that all of the targets are in all of the complexes
        if (complexes.size() > 1) {
            for (final ComplexBean complex : complexes) {
                for (final TargetBean target : targets) {
                    if (!complex.containsTarget(target)) {
                        myComplexes.remove(complex);
                        break;
                    }
                }
            }
        }

        return myComplexes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return T2CReport.info;
    }

}
