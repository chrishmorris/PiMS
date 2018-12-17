package org.pimslims.servlet.spot;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.primer.CodonBean;
import org.pimslims.lab.primer.SDMPrimerBean;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.target.ExpressionObjective;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.construct.ConstructAnnotator;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * 
 * 
 */
public class CreateMutatedObjective extends PIMSServlet {

    private static final int MAX_NAME_LENGTH = 80;

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     * @return Description of Servlet
     */
    @Override
    public String getServletInfo() {
        return "Create a mutated construct";
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     * @param request a request from a URL
     * @param response a response from a URL
     * @throws ServletException if no target found
     * @throws IOException if it cannot write to the user's browser
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

        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 2 > pathInfo.length()) {
            throw new ServletException("No researchObjective specified");
        }
        final String hook = pathInfo.substring(1);

        // always use a transaction in a try/catch block
        try {
            final ResearchObjective r = version.get(hook);
            if (null == r) {
                this.writeErrorHead(request, response, "ResearchObjective not found",
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final ConstructBean cb = ConstructBeanReader.readConstruct(r);
            cb.setName(CreateMutatedObjective.makeName(version, cb.getName() + "M", r.getClass()));
            cb.setSDMConstruct(true);

            SpotNewTarget.setPeople(request, version);
            request.setAttribute("constructBean", cb);

            request.setAttribute("dnaAndProtSeq2", ThreeLetterProteinSeq.dnaAndProtArray(cb.getWildDnaSeq()));
            request.setAttribute("dnaSeq", cb.getDnaSeq());
            request.setAttribute("expressionOrganisms", CodonBean.getExpressionOrganisms());
            request.setAttribute("expressionOrganism", CodonBean.DEFAULT_EXPRESSION_ORGANISM);

            final RequestDispatcher dispatcher = request.getRequestDispatcher(this.getJspPath());
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException ex) {
            throw new ServletException(ex);
        } catch (final ConstraintException ex) {
            throw new ServletException(ex);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * @return String path to the calling JSP
     */
    protected String getJspPath() {
        return "/JSP/sdm/CreateMutatedObjective.jsp";
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     * @param request a request from a URL
     * @param response a response from a URL
     * @throws ServletException if no target found
     * @throws IOException if it cannot write to the user's browser
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("CreateMutatedObjectiveWizard request parameter [" + e.getKey() + ":"
                + s.toString() + "]");
        }

        final WritableVersion version = this.getWritableVersion(request, response);

        if (version == null) {
            return; // error message has already been sent
        }

        try {
            final ConstructBean cb = this.setConstructBean(version, request);

            final Map<String, String> parameterMap = PIMSServlet.getParameterMap(request);

            final String desiredTm = parameterMap.get("desired_tm");
            float tm = CreateMutatedObjective.DEFTM;
            if (null != desiredTm) {
                tm = Float.parseFloat(desiredTm);
            }
            cb.setDesiredTm(tm);

            final String wizardStep = request.getParameter("wizard_step");

            if (wizardStep.equals("Save")) {
                final ResearchObjective eb =
                    (ResearchObjective) CreateMutatedObjective.save(parameterMap, version, cb);
                version.commit();
                PIMSServlet.redirectPost(response, "../View/" + eb.get_Hook());
            }

            final List<SDMPrimerBean> sPrimerBeans = new ArrayList<SDMPrimerBean>();
            final List<SDMPrimerBean> aPrimerBeans = new ArrayList<SDMPrimerBean>();
            final String nextPage = this.nextStep(parameterMap, cb, sPrimerBeans, aPrimerBeans);

            // not saved, need to show another form
            SpotNewTarget.setPeople(request, version);
            request.setAttribute("targetStartCodon", cb.getTargetProtStart());
            request.setAttribute("constructBean", cb);
            request.setAttribute("sPrimerBeans", sPrimerBeans);
            request.setAttribute("aPrimerBeans", aPrimerBeans);
            request.setAttribute("dnaAndProtSeq2", ThreeLetterProteinSeq.dnaAndProtArray(cb.getDnaSeq()));
            request.setAttribute("dnaSeq", cb.getDnaSeq());

            //request.setAttribute("codonList", codonList);
            request.setAttribute("codonTable", CodonBean.getTranslationTable());

            // provide list of preferred codons
            String expressionOrganism = CodonBean.DEFAULT_EXPRESSION_ORGANISM;
            if (null != request.getParameter("expressionOrganism")) {
                expressionOrganism = request.getParameter("expressionOrganism");
            }
            request.setAttribute("preferredCodonTable", CodonBean.getPreferredCodonTable(expressionOrganism));
            request.setAttribute("expressionOrganism", expressionOrganism);

            response.setStatus(HttpServletResponse.SC_OK);

            //System.out.println("CreateMutatedObjectiveWizard next page [" + nextPage + "]");
            final RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
            dispatcher.forward(request, response);
            return;
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ClassNotFoundException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final SQLException e) {
            throw new ServletException(e);
        } catch (final NumberFormatException e1) {
            this.writeErrorHead(request, response, "bad number: " + e1.getMessage(),
                HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    final static float DEFTM = 78.0f;

    protected String nextStep(final Map<String, String> parms, final ConstructBean cb,
        final List<SDMPrimerBean> sPrimerBeans, final List<SDMPrimerBean> aPrimerBeans)
        throws ServletException, IOException, ConstraintException, AccessException, AbortedException,
        ClassNotFoundException, SQLException {

        final String NONSTANDARD = "Non-standard letters in DNA Sequence";
        final String wizardStep = parms.get("wizard_step");

        String nextPage = null;
        if (wizardStep.equals("1")) {

            nextPage = "/JSP/sdm/NewSDMConstructWizardStep2.jsp";

        } else if (wizardStep.equals("2")) {

            nextPage = "/JSP/sdm/NewSDMConstructWizardStep3.jsp";

            final float desTm = cb.getDesiredTm().floatValue();
            sPrimerBeans.addAll(SDMPrimerBean.makePrimerBeanList(desTm, "S", cb.getDnaSeq(),
                cb.getWildDnaSeq()));

            // Need the reverse complement sequence to calculate rev primer
            aPrimerBeans.addAll(SDMPrimerBean.makePrimerBeanList(desTm, "A", cb.getDnaSeq(),
                cb.getWildDnaSeq()));

        } else {
            throw new ServletException("Unexpected design type or wizard step");
        }
        return nextPage;

    }

    /**
     * @param request a request from a URL
     * @return org.pimslims.presentation.construct.ConstructBean -presenation layer construct bean
     */
    private ConstructBean setConstructBean(final ReadableVersion version, final HttpServletRequest request) {

        //TODO if its a polycistronic construct, there will be several targets
        //System.out.println("CreateMutatedObjectiveWizard setConstructBean");
        final String hook = request.getParameter("pims_researchobjective_hook");

        ResearchObjective objective = null;
        if (null != hook) {
            objective = (ResearchObjective) version.get(hook);
        }
        final ConstructBean cb = ConstructBeanReader.readConstruct(objective);
        cb.setSDMConstruct(true);
        cb.setDnaSeq(request.getParameter("construct_dna_seq"));

        // The following seem to be ignored:
        cb.setProtSeq(request.getParameter("construct_prot_seq"));

        cb.setExpressedProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
        cb.setFinalProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));

        // construct id - set again if this is the first step
        cb.setConstructId(request.getParameter("construct_id"));

        cb.setDescription(request.getParameter("description"));
        cb.setComments(request.getParameter("comments"));
        cb.setUserHook(request.getParameter("personHook"));

        return cb;
    }

    /**
     * PiMS Readable version
     * 
     * @param parms Map of input names & values from a URL
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @throws ServletException
     * @throws ClassNotFoundException
     * @throws ConstraintException
     * @throws AccessException
     * @throws SQLException
     * @throws AbortedException
     */
    static ExpressionObjective save(final Map<String, String> parms, final WritableVersion version,
        final ConstructBean cb) throws ConstraintException, AccessException {

        cb.setFwdPrimer(parms.get("sense_primer"));
        cb.setRevPrimer(parms.get("antisense_primer"));

        if (null != cb.getFinalProt()) {
            ConstructAnnotator.annotateFinalProt(cb);
        }

        //cb.setPcrProductSeq(ConstructAnnotator.getPCRProductSequence(cb));
        cb.setPcrProductSeq(cb.getDnaSeq());

        cb.setDescription(parms.get("description"));
        cb.setComments(parms.get("comments"));
        cb.setUserHook(parms.get("personHook"));

        cb.setDateOfEntry(new java.util.Date());

        for (final Iterator iterator = cb.getPrimers().iterator(); iterator.hasNext();) {
            final PrimerBean bean = (PrimerBean) iterator.next();
            final String direction = bean.isForward() ? "S" : "A";
            final String name = version.getUniqueName(Primer.class, cb.getName() + direction);
            bean.setName(name);
            PrimerBean.annotatePrimer(bean);
        }

        return ConstructBeanWriter.createNewConstruct(version, cb);
    }

    private static String makeName(final ReadableVersion version, final String pname, final Class clazz) {

        return pname; //TODO version.getUniqueName(clazz, pname);
    }

}
