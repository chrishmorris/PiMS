package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.pimslims.lab.primer.YorkPrimerBean;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.SequenceUtility;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.presentation.construct.Extensions;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * 
 * @baseUrl http://localhost:8080/current/spot/SpotNewConstructWizard?commonName=CAG38863&wizard_step=1
 * 
 *          Now used only for DNA targets
 * 
 */
public class SpotNewConstructWizard extends
/* common servlet that provides utility methods */PIMSServlet {

    /*
     * (non-Javadoc)
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     * @return Description of Servlet
     */
    @Override
    public String getServletInfo() {
        return "Create a construct";
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

        throw new ServletException("Now use servlet CreateExpressionObjective");

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
        /*
        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.spot.SpotNewConstructWizard parameter [" + e.getKey()
                + ":" + s.toString() + "]");
        }
        */
        final WritableVersion version = this.getWritableVersion(request, response);

        if (version == null) {
            return; // error message has already been sent
        }
        try {
            final ConstructBean cb = this.setConstructBean(version, request);
            final String wizardStep = request.getParameter("wizard_step");
            final Map<String, String> parms = PIMSServlet.getParameterMap(request);
            if (wizardStep.equals("Save")) {

                //if (null == cb.getDnaTarget() || "".equals(cb.getDnaTarget())) {
                //cb.setDnaSeq(request.getParameter("dnaSeq"));
                // now read using target hook cb.setTargetProtSeq(parms.get("target_prot_seq"));
                //}

                final ResearchObjective eb = (ResearchObjective) this.save(parms, version, cb);
                //New Extension code 030708 added back 150909
                this.createNewExtension(version, cb, request.getParameter("new_fExtName"),
                    request.getParameter("new_rExtName"), request.getParameter("new_fExtTag"),
                    request.getParameter("new_rExtTag"), request.getParameter("new_fExtEnz"),
                    request.getParameter("new_rExtEnz"));

                version.commit();
                PIMSServlet.redirectPost(response, "../View/" + eb.get_Hook());
            } else {
                final List<YorkPrimerBean> primerBeans = new ArrayList<YorkPrimerBean>();
                final List<YorkPrimerBean> rprimerBeans = new ArrayList<YorkPrimerBean>();
                final String nextPage = this.nextStep(parms, cb, primerBeans, rprimerBeans);
                // not saved, need to show another form
                SpotNewTarget.setPeople(request, version);
                request.setAttribute("constructBean", cb);
                request.setAttribute("primerBeans", primerBeans);
                request.setAttribute("rprimerBeans", rprimerBeans);

                // PIMS-2701 200909
                request.setAttribute("f_extensions", Extensions.makeExtensionBeans(version, "Forward"));
                request.setAttribute("r_extensions", Extensions.makeExtensionBeans(version, "Reverse"));

                //Oct 08 for redesign
                request.setAttribute("radioStopMap", SpotNewConstructWizard.radioStopMap);

                request.setAttribute("fixNonstandardStartCodon", ""); //was for non-standard start codon
                final RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
                response.setStatus(HttpServletResponse.SC_OK);
                dispatcher.forward(request, response);
                version.abort();
            }
            return;
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
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

    final static float DEFTM = 60.0f;

    private static final String NONSTANDARD = "Non-standard letters in DNA Sequence";

    /**
     * @param parms Map of input names & values from a URL
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @param primerBeans List of forward org.pimslims.primer.YorkPrimerBeans
     * @param rprimerBeans List of reverse org.pimslims.primer.YorkPrimerBeans
     * @return String representing the name of the next JSP
     * @throws ServletException if Non-standard letters in DNA Sequence
     * @throws IOException if it cannot write to the user's browser
     * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
     * @throws IllegalAlphabetException if BioJavav alphabet not specified
     * @throws ConstraintException if datamodel constraint violated
     * @throws AccessException
     * @throws AbortedException
     * @throws ClassNotFoundException if Unknown class referenced in method
     * @throws SQLException
     */
    protected String nextStep(final Map<String, String> parms, final ConstructBean cb,
        final List<YorkPrimerBean> primerBeans, final List<YorkPrimerBean> rprimerBeans)
        throws ServletException, IOException, ConstraintException, AccessException {

        final String wizardStep = parms.get("wizard_step");

        String nextPage = null;
        if (wizardStep.equals("1")) {
            throw new ServletException("Use CreateExpressionObjective");

        } else if (wizardStep.equals("1dna")) {
            throw new ServletException("Use CreateExpressionObjective");

        } else if (wizardStep.equals("2dna")) {
            nextPage = "/JSP/dnaTarget/DNAConstructWizardStep3.jsp";
            this.processSelectedPrimers(parms, cb);

        } else

        if (wizardStep.equals("2c")) {
            nextPage = "/JSP/spot/SpotNewConstructWizardStep3c.jsp"; //TODO construct/DesignOverlaps
            cb.setDnaSeq(parms.get("dnaSeq"));
            //TODO the lines below don't look right, what about mutation by primer? What about tags?
            cb.setFinalProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
            cb.setExpressedProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
            this.processSelectedPrimers(parms, cb);
        } else {
            throw new ServletException("Bad wizardStep: " + wizardStep);
        }
        return nextPage;

    }

    private void processSelectedPrimers(final Map<String, String> parms, final ConstructBean cb)
        throws ServletException {

        SpotNewConstructWizard.doProcessSelectedPrimers(cb, parms.get("forward_primer"),
            parms.get("reverse_primer"));

        ConstructBean.annotate(cb);
    }

    /**
     * SpotNewConstructWizard.f
     * 
     * @param parms
     * @param cb private void processxtension(final Map<String, String> parms, final ConstructBean cb) {
     *            //TODO no, set extension
     *            cb.setReverseTag(YorkPrimerUtility.processTag(parms.get("reverse_extension")));
     *            cb.setForwardTag(YorkPrimerUtility.processTag(parms.get("forward_extension"))); // Process
     *            tag to get sequence // 140508 Susy code to process user-entered tag final String newFExSeq =
     *            parms.get("new_fExtSeq"); final String newRExSeq = parms.get("new_rExtSeq");
     * 
     *            if ("".equals(newFExSeq) || null == newFExSeq) { } else { cb.setForwardTag(newFExSeq);
     *            //String fexMessage = Extensions.makeNewExtension(version, newFExName, newFExSeq,
     *            "Forward"); } if ("".equals(newRExSeq) || null == newRExSeq) { } else { //TODO no, set
     *            extension cb.setReverseTag(newRExSeq); } // now done in javascript if
     *            ("on".equals(parms.get("check1"))) { final String ftag = cb.getForwardTag() + "ATG";
     *            cb.setForwardTag(ftag); } }
     */

    static/**
           * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
           * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
           * @throws IllegalAlphabetException if BioJavav alphabet not specified
           */
    void doProcessSelectedPrimers(final ConstructBean cb, final String fwdPrimer, final String revPrimer) {
        cb.setFwdOverlapLen(new Integer(fwdPrimer.length()));
        cb.setFwdPrimer(fwdPrimer);

        /* SymbolList symL = DNATools.createDNA(cb.getDnaSeq());
        symL = DNATools.reverseComplement(symL); */
        cb.setRevOverlapLen(new Integer(revPrimer.length()));
        cb.setRevPrimer(revPrimer);
    }

    /**
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @param primerBeans
     * @param rprimerBeans
     * @param dP
     * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
     * @throws IllegalAlphabetException if BioJavav alphabet not specified
     * 
     *             private void makeFandRPrimerBeanLists(final ConstructBean cb, final List<YorkPrimerBean>
     *             primerBeans, final List<YorkPrimerBean> rprimerBeans, final DesignPrimers dP) throws
     *             IllegalSymbolException, IllegalAlphabetException { final float desTm =
     *             cb.getDesiredTm().floatValue(); final String fType = "F";
     *             primerBeans.addAll(this.makePrimerBeanList(cb, dP, desTm, fType)); // Need the reverse
     *             complement sequence to calculate rev primer final String rType = "R";
     *             rprimerBeans.addAll(this.makePrimerBeanList(cb, dP, desTm, rType)); }
     */

    /**
     * @param request a request from a URL
     * @return org.pimslims.presentation.construct.ConstructBean -presenation layer construct bean
     */
    private ConstructBean setConstructBean(final ReadableVersion version, final HttpServletRequest request)
        throws ServletException {
        //TODO if its a polycistronic construct, there will be several targets

        final String targetHook = request.getParameter("pims_target_hook");
        TargetBean tb = new TargetBean();
        if (null != targetHook) {
            tb = new TargetBean((Target) version.get(targetHook));
        }

        //For synthetic gene, need different DNA sequence
        final String sampleHook = request.getParameter("sampleHook");
        if (null != sampleHook && "" != sampleHook) {
            final Sample sgSample = version.get(sampleHook);
            final SyntheticGeneBean sgb = new SyntheticGeneBean(sgSample);
            request.setAttribute("syntheticGeneBean", sgb);
            final String dnaSeq = sgb.getDnaSeq().toUpperCase();
            tb.setDnaSeq(dnaSeq);
        }
        request.setAttribute("sampleHook", sampleHook);

        final ConstructBean cb =
            new ConstructBean(tb, new PrimerBean(PrimerBean.FORWARD), new PrimerBean(PrimerBean.REVERSE));
        final LabNotebook project = version.get(request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID));
        if (project != null) {
            cb.setAccess(new ModelObjectBean(project));
        }
        cb.setDnaTarget(request.getParameter("dna_target"));
        //DNA Sequence formatted for JSPs
        final List<String> chunks = SequenceUtility.chunkSeq(cb.getTargetDnaSeq());
        request.setAttribute("chunks", chunks);
        //Oct 08 after re-design
        //Susy 23-62010 for version 4.1 -displaying target protein sequence NOT translated sequence

        if (null == cb.getDnaTarget() || "".equals(cb.getDnaTarget())) {
            this.setSeqStartAndEnd(request, cb);
            cb.setTargetProtSeq(ThreeLetterProteinSeq.translate(tb.getDnaSeq()));
            final List<String> transSeqChunks =
                SequenceUtility.chunkSeq(ThreeLetterProteinSeq.translate(cb.getTargetDnaSeq()));
            //final List<String> transSeqChunks = SequenceUtility.chunkSeq(cb.getTargetProtSeq());
            request.setAttribute("transSeqChunks", transSeqChunks);
        } else {
            this.setDNASeqStartAndEnd(request, cb);
        }

        // was cb.setPrimersProvided(new Boolean(request.getParameter("primersProvided")));

        // construct id - set again if this is the first step
        cb.setConstructId(request.getParameter("construct_id"));

        /* Primer design values
        final String fwdOverlap = request.getParameter("fwd_overlap_len");
        if (null != fwdOverlap && !"".equals(fwdOverlap)) {
            cb.setFwdOverlapLen(Integer.parseInt(fwdOverlap));
            // TODO no, must setFwdOverlap, after calculating it from
            // targetDnaSeq
        }
        final String revOverlap = request.getParameter("rev_overlap_len");
        if (null != revOverlap && !"".equals(revOverlap)) {
            cb.setRevOverlapLen(Integer.parseInt(revOverlap));
            // TODO no, must setRevOverlap, after calculating it from
            // targetDnaSeq
        } */

        final String tm = request.getParameter("desired_tm");
        if (null != tm && !"".equals(tm)) {
            cb.setDesiredTm(Float.parseFloat(tm));
        }
        /* was
        if (null != request.getParameter("fchoice")) {
        cb.setFwdPrimerChoice(Integer.parseInt(request.getParameter("fchoice")));
        }
        if (null != request.getParameter("rchoice")) {
        cb.setRevPrimerChoice(Integer.parseInt(request.getParameter("rchoice")));
        } 
        */

        final String forward = request.getParameter("forward_primer");
        cb.setFwdPrimer(forward);
        cb.setFwdOverlapLen(forward.length());
        final String reverse = request.getParameter("reverse_primer");
        cb.setRevPrimer(reverse);
        cb.setRevOverlapLen(reverse.length());
        //LATER these two will be set again if step 3c
        if (null != request.getParameter("forward_extension")) {
            cb.setForwardExtension(request.getParameter("forward_extension"));
        }
        if (null != request.getParameter("reverse_extension")) {
            cb.setReverseExtension(request.getParameter("reverse_extension"));
        }

        cb.setForwardTag(request.getParameter("expressed_prot_n"));
        final String final_n = request.getParameter("final_prot_n");
        cb.setFinalProtN(final_n == null ? "" : final_n);

        //140508 Susy Processing new extensions
        if (null != request.getParameter("new_fExtSeq")) {
            cb.setForwardExtension(request.getParameter("new_fExtSeq"));
        }
        if (null != request.getParameter("new_rExtSeq")) {
            cb.setReverseExtension(request.getParameter("new_rExtSeq"));
        }

        if (null == cb.getDnaTarget() || "".equals(cb.getDnaTarget())) {
            //22-11-07 Code to process Start codon ATG addition to tag, if checkbox is checked
            if ("on".equals(request.getParameter("check1"))) {
                final String ftag = request.getParameter("forward_extension") + "ATG";
                cb.setForwardExtension(ftag);
                //220708 Also have to add 'M' to N-term of protein
                final String expM = request.getParameter("expressed_prot_n") + "M";
                cb.setForwardTag(expM);
                final String finM = request.getParameter("final_prot_n") + "M";
                cb.setFinalProtN(finM);
            }

            //211107 Code to process addition of stop codon to tag based on selected radio button
            if (null != request.getParameter("add_stop")) {
                final String stop = request.getParameter("add_stop");
                final String rtag = request.getParameter("reverse_extension") + stop.trim();
                cb.setReverseExtension(rtag);
            }

            // expressed and final protein, some are set again if step 2b or 2c
            cb.setExpressedProt(request.getParameter("expressed_prot"));
            cb.setFinalProt(request.getParameter("final_prot")); //TODO is this used?
            cb.setReverseTag(request.getParameter("expressed_prot_c"));
            final String final_c = request.getParameter("final_prot_c");
            cb.setFinalProtC(final_c == null ? "" : final_c);

            //not set anywhere
            cb.setDescription(request.getParameter("description"));
            cb.setComments(request.getParameter("comments"));
            cb.setUserHook(request.getParameter("personHook"));

        }

        return cb;
    }

    /**
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
    private ModelObject save(final Map<String, String> parms, final WritableVersion version,
        final ConstructBean cb) throws ConstraintException, AccessException {
        cb.setFwdPrimer(parms.get("forward_primer"));
        cb.setRevPrimer(parms.get("reverse_primer"));
        if (null != parms.get("forward_extension")) {
            cb.setForwardExtension(parms.get("forward_extension"));
        }
        //added by Susy 040909
        final String newFExSeq = parms.get("new_fExtSeq");
        //if ("".equals(newFExSeq) || null == newFExSeq) {
        if ((!"".equals(newFExSeq) || null != newFExSeq)
            && "New Extension".equals(parms.get("forward_extension"))) {
            //cb.setForwardTag(YorkPrimerUtility.processTag(parms.get("forward_tag")));
            cb.setForwardExtension(newFExSeq);
        }
        //added Susy PIMS-3046
        if ("on".equals(parms.get("check1"))) {
            final String ftag = parms.get("forward_extension") + "ATG";
            cb.setForwardExtension(ftag);
        }

        if (null != parms.get("reverse_extension")) {
            cb.setReverseExtension(parms.get("reverse_extension"));
        }
        //added by Susy 080909
        final String newRExSeq = parms.get("new_rExtSeq");
        if ((!"".equals(newRExSeq) || null != newRExSeq)
            && "New Extension".equals(parms.get("reverse_extension"))) {
            cb.setReverseExtension(newRExSeq);
        }

        if (null != parms.get("add_stop")) {
            final String stop = parms.get("add_stop");
            final String rtag = cb.getReverseExtension() + stop.trim();
            cb.setReverseExtension(rtag);
        }

        //For synthetic gene construct
        if (null != parms.get("sampleHook") && "" != parms.get("sampleHook")) {
            final String shook = parms.get("sampleHook");
            cb.setSgSampleHook(shook);
            String sampleName;
            final ModelObject sample = version.get(shook);
            sampleName = sample.get_Name();
            cb.setSgSampleName(sampleName);
        }

        // calculate protein sequences and their properties
        ConstructBean.annotate(cb);

        cb.setDescription(parms.get("description"));
        cb.setComments(parms.get("comments"));
        cb.setUserHook(parms.get("personHook"));

        // ConstructAnnotator.annotate(cb);
        cb.setDateOfEntry(new java.util.Date());

        return ConstructBeanWriter.createNewConstruct(version, cb);
    }

    /**
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @param dP an instance of org.pimslims.lab.DesignPrimers
     * @param desTm String value for primer design Tm
     * @param ptype String to define primer type- forward or reverse
     * @return List of org.pimslims.lab.YorkPrimerBean
     * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
     * @throws IllegalAlphabetException if BioJavav alphabet not specified
     * 
     *             private List<YorkPrimerBean> makePrimerBeanList(final ConstructBean cb, final DesignPrimers
     *             dP, final float desTm, final String ptype) throws IllegalSymbolException,
     *             IllegalAlphabetException { String seq = ""; if ("R".equals(ptype)) { SymbolList symL =
     *             DNATools.createDNA(cb.getDnaSeq()); symL = DNATools.reverseComplement(symL); seq =
     *             symL.seqString(); } else { seq = cb.getDnaSeq(); } assert null != seq;
     * 
     *             final List<String> primers = dP.makePrimers(new String[] { seq }, desTm); return
     *             YorkPrimerUtility.makeYPBs(primers, ptype); }
     */

    private static final java.util.Map<String, String> radioStopMap = new LinkedHashMap<String, String>();
    static {
        SpotNewConstructWizard.radioStopMap.put("TTA", "TAA (Adds TTA to 5'-end)");
        SpotNewConstructWizard.radioStopMap.put("TCA", "TGA (Adds TCA to 5'-end)");
        SpotNewConstructWizard.radioStopMap.put("CTA", "TAG (Adds CTA to 5'-end)");
        SpotNewConstructWizard.radioStopMap.put("", "none");
    }

    /**
     * @param request a request from a URL
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     */
    private void setDNASeqStartAndEnd(final HttpServletRequest request, final ConstructBean cb) {
        String startDT = "";
        String endDT = "";

        if (null != request.getParameter("target_dna_start")) {
            startDT = request.getParameter("target_dna_start").trim();
            final int startInt = Integer.parseInt(startDT);
            cb.setTargetDnaStart(new Integer(startInt));
        }
        if (null != request.getParameter("target_dna_end")) {
            endDT = request.getParameter("target_dna_end").trim();
            final int endInt = Integer.parseInt(endDT);
            cb.setTargetDnaEnd(new Integer(endInt));
        }
        final String targetDnaSeq = request.getParameter("target_dna_seq");
        cb.setDnaSeq(targetDnaSeq.substring((cb.getTargetDnaStart()) - 1, cb.getTargetDnaEnd()));
        final List<String> constructChunks = new DnaSequence(cb.getDnaSeq()).chunkSeq();
        request.setAttribute("constructChunks", constructChunks);

    }

    /**
     * @param version
     * @param cb This doesn't seem so appropriate, now that extensions are reference data private void
     *            createNewExtension(final WritableVersion version, final ConstructBean cb, final String
     *            newFExName, final String newRExName) throws ConstraintException, AccessException { if (null
     *            != newFExName && !"".equals(newFExName)) { final ModelObject newFEx =
     *            Extensions.makeNewExtension(version, newFExName, cb.getForwardTag(), "Forward", "", "");
     *            System.out.println("New Forward extension recorded in PiMS " + newFEx.get_Name()); } if
     *            (null != newRExName && !"".equals(newRExName)) { final ModelObject newREx =
     *            Extensions.makeNewExtension(version, newRExName, cb.getReverseTag(), "Reverse", "", "");
     *            System.out.println("New Reverse extension recorded in PiMS " + newREx.get_Name()); } }
     */

    /**
     * @param version
     * @param cb
     */
    private void createNewExtension(final WritableVersion version, final ConstructBean cb,
        final String newFExName, final String newRExName, final String newFExTag, final String newRExTag,
        final String newFExEnz, final String newRExEnz) throws ConstraintException, AccessException {
        if (null != newFExName && !"".equals(newFExName)) {
            final ModelObject newFEx =
                Extensions.makeNewExtension(version, cb.getAccess(), newFExName, cb.getForwardExtension(),
                    "Forward", newFExTag, newFExEnz);
            System.out.println("New Forward extension recorded in PiMS " + newFEx.get_Name());
        }
        if (null != newRExName && !"".equals(newRExName)) {
            final ModelObject newREx =
                Extensions.makeNewExtension(version, cb.getAccess(), newRExName, cb.getReverseExtension(),
                    "Reverse", newRExTag, newRExEnz);
            System.out.println("New Reverse extension recorded in PiMS " + newREx.get_Name());
        }
    }

    /**
     * @param request a request from a URL
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     */
    private void setSeqStartAndEnd(final HttpServletRequest request, final ConstructBean cb)
        throws ServletException {
        final int THREE = 3;
        cb.setTargetProtStart(new Integer(request.getParameter("target_prot_start")));
        cb.setTargetProtEnd(new Integer(request.getParameter("target_prot_end")));
        assert null != cb.getTargetDnaSeq();
        cb.setDnaSeq(cb.getTargetDnaSeq().substring(((cb.getTargetProtStart()) * THREE) - THREE,
            cb.getTargetProtEnd() * THREE));

        //06-02-09 processing non-standard start codons
        if ("on".equals(request.getParameter("fixNonstandardStartCodon"))
            || "on".equals(request.getParameter("fixNonstandardStartCodon"))) {
            cb.setDnaSeq("A" + cb.getDnaSeq().substring(1));
        }
        cb.setProtSeq(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
        //Oct 08 after re-design
        final List<String> protConDNAChunks = SequenceUtility.chunkSeq(cb.getDnaSeq());
        request.setAttribute("protConDNAChunks", protConDNAChunks);
        final List<String> protConChunks = SequenceUtility.chunkSeq(cb.getProtSeq());
        request.setAttribute("protConChunks", protConChunks);

    }

}
