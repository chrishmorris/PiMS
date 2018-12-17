package org.pimslims.servlet.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.pimslims.mhtml.MhtmlFilter;
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
import org.pimslims.presentation.experiment.OutputSampleBean;
import org.pimslims.presentation.experiment.ParameterBean;
import org.pimslims.presentation.pdf.PdfReport;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.report.Filtered;
import org.pimslims.servlet.BookmarkBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.protocol.SearchProtocol;
import org.pimslims.servlet.standard.ViewSample;
import org.pimslims.util.File;
import org.pimslims.utils.ExecRunnerException;
import org.pimslims.utils.ExecRunnerException.BadPathException;

/**
 * Servlet for generating sample provenance report This generates a PDF version or an HTML version.
 * MhtmlFilter transforms the HTML version into MHTML for MS Word.
 * 
 * @author Marc Savitsky
 * @since 16 April 2008
 * 
 */
public class T2CReport extends PIMSServlet {

    /**
     * parameter name for hiding a record
     */
    public static final String EXCLUDED = "_excluded";

    public static final String PRODUCT = "_join:outputSamples.sampleCategory.name";

    /**
     * ACCEPT String
     */
    public static final String ACCEPT = "presentationtype";

    public static final String ErrorMessage_NoHook =
        "there is no object in the system that can be accessed by this url";

    private static final String info = "Shows a graph created by GraphViz software ";

    public static final String EXPERIMENTS = "Experiments";

    public static final String PRESENTATION_HTML = "html"; //TODO "text/html"

    public static final String PRESENTATION_PDF = "pdf"; //TODO "application/pdf"

    public static final String PRESENTATION_XML = "xml"; //TODO application/xml

    public static final String KEYWORD = "_show:parameter.name";

    public static final String METHOD = "Method";

    public static final Map<String, Filtered> DEFAULT_FILTER_IN = new HashMap();
    static {
        T2CReport.DEFAULT_FILTER_IN.put(T2CReport.METHOD, Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Insert", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Translated sequence", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Expressed protein", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Final protein", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Reagents", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Product", Filtered.IN);
        T2CReport.DEFAULT_FILTER_IN.put("Comments", Filtered.IN);
    }

    public static final Map<String, Filtered> DEFAULT_FILTER_OUT = new HashMap();
    static {
        T2CReport.DEFAULT_FILTER_OUT.put(T2CReport.METHOD, Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Insert", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Translated sequence", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Expressed protein", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Final protein", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Reagents", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Product", Filtered.OUT);
        T2CReport.DEFAULT_FILTER_OUT.put("Comments", Filtered.OUT);
    }

    //TODO offer RDF, using Krish's code

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

        Set<String> excludedHooks = Collections.EMPTY_SET; // default is reject none
        final String[] excluded = request.getParameterValues(T2CReport.EXCLUDED);
        if (null != excluded) {
            excludedHooks = new HashSet<String>(Arrays.asList(excluded));
        }
        Set<String> products = null; // default is accept all
        final String[] categories = request.getParameterValues(T2CReport.PRODUCT);
        if (null != categories) {
            products = new HashSet<String>(Arrays.asList(categories));
        }

        final Map<String, Filtered> keywordFilter = new LinkedHashMap();
        Set<String> selectedKeywords = null; // default is accept all
        final String[] words = request.getParameterValues(T2CReport.KEYWORD);
        if (null == words) {
            // no filter from user, default is display all
            keywordFilter.putAll(T2CReport.DEFAULT_FILTER_IN);
        } else {
            // the user has set a filter, so default will be do not display
            keywordFilter.putAll(T2CReport.DEFAULT_FILTER_OUT);
            selectedKeywords = new HashSet<String>(Arrays.asList(words));
            for (final Iterator iterator = selectedKeywords.iterator(); iterator.hasNext();) {
                final String string = (String) iterator.next();
                keywordFilter.put(string, Filtered.IN);
            }
        }

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // get a full path to dot.exe from servlet context
        String dot_path = org.pimslims.properties.PropertyGetter.getStringProperty("dot_path", "dot");
        if (dot_path == null || dot_path.length() < 1) {
            dot_path = "dot";
        }

        String hook = null;
        final String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 1) {
            //TODO make filter and show search            
        } else {
            hook = pathInfo.substring(1);
        }

        // get request parameter 'presentationType'
        String presentationType = request.getParameter(T2CReport.ACCEPT);
        if (presentationType == null || presentationType.length() == 0) {
            presentationType = T2CReport.PRESENTATION_HTML;
        }

        ReadableVersion version = null;
        String location = "";
        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            final List<ConstructBean> constructs = new ArrayList<ConstructBean>();
            final List<Experiment> myexperiments = new ArrayList<Experiment>();
            final List<TargetBean> targets = new ArrayList<TargetBean>();
            final Collection<Target> targetList = new ArrayList<Target>();
            final List<ComplexBean> complexes = new ArrayList<ComplexBean>();
            final Set<User> authors = new HashSet<User>();

            final List<Experiment> experiments = new ArrayList<Experiment>();
            final Map<String, Collection<File>> images = new HashMap();

            Map<String, Filtered> filterMap = null;
            if (null == hook) {
                response.setStatus(HttpServletResponse.SC_OK);
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/report/T2CReport.jsp");
                dispatcher.forward(request, response);
                return;
            }

            final Sample sample = version.get(hook);

            if (sample == null) { // http 404 error
                response.sendError(404, "Not found: " + hook);
                return;
            }
            // create the model for the graph
            final SampleProvenanceModel model = new SampleProvenanceModel(sample);
            final IGraph graphModel = model.createGraphModel(sample); //TODO no, model.complete(sample)

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
                @Override
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

            location = this.getLocation(sample);
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

            final byte[] graph =
                this.generateGraph(sample, URL_CONTEXT, dot_path, DotGraphUtil.FORMAT_PNG, graphModel);
            request.setAttribute("sample", sampleBean);
            String role = "";
            if (null != sample.getOutputSample()) {
                if (null != sample.getOutputSample().getRefOutputSample()) {
                    role = sample.getOutputSample().getRefOutputSample().getName();
                }
            }
            request.setAttribute("role", role);
            request.setAttribute("sampleCategories",
                ModelObjectShortBean.getBeans(sample.getSampleCategories()));
            MhtmlFilter.addReportPart(request, graph, "image/png", "/read/Dot/" + sample.get_Hook()
                + "?format=png");

            filterMap = Filtered.getExperimentFilterMap(experiments, excludedHooks, products);
            if (T2CReport.PRESENTATION_PDF.equals(presentationType)) {

                PdfReport document = null;
                document =
                    new PdfReport(response.getOutputStream(), "PiMS Sample Provenance Report    "
                        + new Date().toString(), citation.toString());
                response.setContentType("application/pdf");

                document.addImage(graph);

                for (final ComplexBean bean : complexes) {
                    document.showComplex(bean);
                }
                for (final TargetBean bean : targets) {
                    document.showTarget(bean);
                }
                for (final ConstructBean bean : constructs) {
                    document.showConstruct(bean, filterMap);
                }
                final List<ExperimentBean> beans = new ArrayList(experiments.size());
                for (final Experiment experiment : experiments) {
                    final ExperimentBean bean = new ExperimentBean(experiment);
                    beans.add(bean);
                    T2CReport.processExperimentBean(keywordFilter, selectedKeywords,
                        new HashSet(experiments.size()), bean);
                    if (!filterMap.get(experiment.get_Hook()).getFilteredOut()) {
                        document.showExperiment(bean, experiment, keywordFilter);
                    }
                }
                document.showSample(sampleBean, sample, location);
                response.setStatus(HttpServletResponse.SC_OK);
                document.close();
                return;
            }

            if (T2CReport.PRESENTATION_HTML.equals(presentationType)) {

                ViewSample.setAmounts(request, sample);

                request.setAttribute("location", location);
                BookmarkBean.setBookmark("/read/Provenance?" + request.getQueryString(), request, version);

                request.setAttribute("expblueprints", constructs);
                final Set<String> actualProducts = new HashSet(experiments.size());

                final List<ExperimentBean> beans = new ArrayList(experiments.size());
                for (final Experiment experiment : experiments) {
                    final ExperimentBean bean = new ExperimentBean(experiment);
                    beans.add(bean);
                }
                for (final Iterator iterator1 = beans.iterator(); iterator1.hasNext();) {
                    final ExperimentBean bean = (ExperimentBean) iterator1.next();
                    T2CReport.processExperimentBean(keywordFilter, selectedKeywords, actualProducts, bean);
                }
                request.setAttribute("experiments", beans);

                // prepare filter
                SearchProtocol.exportSampleCategoryList(request, version);
                request.setAttribute("filterMap", filterMap);
                request.setAttribute("keywords", keywordFilter);
                final List<String> allKeywords = new ArrayList(keywordFilter.keySet());
                Collections.sort(allKeywords);
                request.setAttribute("allKeywords", allKeywords);
                if (null == selectedKeywords) {
                    // by default all are selected
                    request.setAttribute(T2CReport.KEYWORD, allKeywords.toArray(new String[] {}));
                } else {
                    request.setAttribute(T2CReport.KEYWORD, selectedKeywords.toArray(new String[] {}));
                }

                request.setAttribute("images", images);
                request.setAttribute("targets", targets);
                request.setAttribute("complexes", complexes);
                request.setAttribute("authors", authors);
                request.setAttribute("requesturl", request.getRequestURL());

                // pass the attached images to MhtmlFilter
                final Collection<org.pimslims.util.File> imageFiles = new HashSet();
                for (final Iterator iterator = images.values().iterator(); iterator.hasNext();) {
                    final Collection<File> files = (Collection<File>) iterator.next();
                    for (final Iterator iterator2 = files.iterator(); iterator2.hasNext();) {
                        final File file = (File) iterator2.next();
                        imageFiles.add(file);
                    }
                }
                request.setAttribute("imageFiles", imageFiles);
                response.setStatus(HttpServletResponse.SC_OK);
                if (null == products) {
                    // not filtered yet, set up filter with outputs actually found
                    request.setAttribute("selectedProducts", actualProducts.toArray(new String[] {}));
                } else {
                    request.setAttribute("selectedProducts", products.toArray(new String[] {}));
                }
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/report/T2CReport.jsp");
                dispatcher.forward(request, response);

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

    /**
     * T2CReport.getLocation
     * 
     * @param sample
     * @return
     */
    private String getLocation(final Sample sample) {
        final List<ModelObjectShortBean> beans = SampleLocationBean.getCurrentLocationTrail(sample);
        if (beans.isEmpty()) {
            return "";
        }
        String ret = "";
        for (final Iterator iterator = beans.iterator(); iterator.hasNext();) {
            final ModelObjectShortBean bean = (ModelObjectShortBean) iterator.next();
            ret += ": " + bean.getName();
        }
        return ret.substring(2);
    }

    /**
     * T2CReport.processExperimentBean
     * 
     * @param keywords
     * @param selectedKeywords
     * @param actualProducts
     * @param bean
     */
    public static void processExperimentBean(final Map<String, Filtered> keywords,
        final Set<String> selectedKeywords, final Set<String> actualProducts, final ExperimentBean bean) {
        final Collection<OutputSampleBean> osbs = bean.getOutputSampleBeans();
        for (final Iterator iterator = osbs.iterator(); iterator.hasNext();) {
            final OutputSampleBean osb = (OutputSampleBean) iterator.next();
            if (null != osb.getRefSampleName()) {
                actualProducts.add(osb.getRefSampleName());
            }
        }
        final List<ParameterBean> parameters = bean.getParameters();
        for (final Iterator iterator = parameters.iterator(); iterator.hasNext();) {
            final ParameterBean parameterBean = (ParameterBean) iterator.next();
            if (keywords.containsKey(parameterBean.getName())) {
                // that's OK
            } else {
                if (null == selectedKeywords) {
                    keywords.put(parameterBean.getName(), Filtered.IN);
                } else {
                    keywords.put(parameterBean.getName(), Filtered.OUT);
                }
            }
        }
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
     * 
     *         private boolean canSpineExport(final Collection<ComplexBean> complexes, final
     *         Collection<TargetBean> targets) {
     * 
     *         final Collection<ComplexBean> myComplexes = this.spineComplexes(complexes, targets);
     * 
     *         if (myComplexes.isEmpty()) { return false; }
     * 
     *         return true; }
     */

    /**
     * return a collection of complexbeans that match all targets
     * 
     * @param complexes
     * @param targets
     * @return
     * 
     *         private Collection<ComplexBean> spineComplexes(final Collection<ComplexBean> complexes, final
     *         Collection<TargetBean> targets) {
     * 
     *         final Collection<ComplexBean> myComplexes = new HashSet<ComplexBean>();
     *         myComplexes.addAll(complexes);
     * 
     *         // if there is more than 1 complex, // make sure that all of the targets are in all of the
     *         complexes if (complexes.size() > 1) { for (final ComplexBean complex : complexes) { for (final
     *         TargetBean target : targets) { if (!complex.containsTarget(target)) {
     *         myComplexes.remove(complex); break; } } } }
     * 
     *         return myComplexes; }
     */

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
