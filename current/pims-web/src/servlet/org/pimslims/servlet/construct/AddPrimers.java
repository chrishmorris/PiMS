package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.primer.PimsStandardPrimerDesigner;
import org.pimslims.lab.primer.YorkPrimerBean;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.SequenceUtility;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.presentation.construct.Extensions;
import org.pimslims.servlet.PIMSServlet;

public class AddPrimers extends PIMSServlet {

    /*
     * (non-Javadoc)
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */

    /**
     * DESIRED_TM String
     */
    static final String DESIRED_TM = "desired_tm";

    public final static float DEFAULT_TM = 60.0f;

    /**
     * @see javax.servlet.Servlet#getServletInfo()
     * @return Description of Servlet
     */
    @Override
    public String getServletInfo() {
        return "Add primers to a virtual construct";
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

        final String fwdPrimer = request.getParameter("forward_primer");
        final String revPrimer = request.getParameter("reverse_primer");
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Hook not supplied");
            return;
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            final String hook = pathInfo.substring(1);
            final ResearchObjectiveElement roe =
                (ResearchObjectiveElement) this.getRequiredObject(version, request, response, hook);
            final ConstructBean cb = this.setConstructBean(version, roe, request);
            if (null == fwdPrimer || null == revPrimer) {

                final String desiredTm = request.getParameter(AddPrimers.DESIRED_TM);
                float tm = AddPrimers.DEFAULT_TM;
                if (null != desiredTm) {
                    tm = Float.parseFloat(desiredTm);
                }
                cb.setDesiredTm(tm);
                final List<YorkPrimerBean> primerBeans = new ArrayList<YorkPrimerBean>();
                final List<YorkPrimerBean> rprimerBeans = new ArrayList<YorkPrimerBean>();
                request.setAttribute("primerBeans", primerBeans);
                request.setAttribute("rprimerBeans", rprimerBeans);

                boolean fixNonstandardStartCodon = false;
                if ("on".equals(request.getParameter("fixNonstandardStartCodon"))) {
                    final String newStart = "A";
                    cb.setDnaSeq(newStart + cb.getDnaSeq().substring(1));
                    fixNonstandardStartCodon = true;
                }
                PimsStandardPrimerDesigner.makeFandRPrimerBeanLists(primerBeans, rprimerBeans,
                    cb.getDesiredTm(), cb.getDnaSeq());

                request.setAttribute("constructBean", cb);
                request.setAttribute("fixNonstandardStartCodon", fixNonstandardStartCodon);
                request.getRequestDispatcher("/JSP/construct/DesignOverlaps.jsp").forward(request, response);
            } else {
                // design extensions
                cb.setFwdOverlapLen(new Integer(fwdPrimer.length()));
                cb.setFwdPrimer(fwdPrimer);
                cb.setRevOverlapLen(new Integer(revPrimer.length()));
                cb.setRevPrimer(revPrimer);
                ConstructBean.annotate(cb);
                /* the lines below don't look right, what about mutation by primer? What about tags?
                cb.setFinalProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
                cb.setExpressedProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq())); */

                // was SpotNewTarget.setPeople(request, version);
                request.setAttribute("constructBean", cb);

                // PIMS-2701 200909
                request.setAttribute("f_extensions", Extensions.makeExtensionBeans(version, "Forward"));
                request.setAttribute("r_extensions", Extensions.makeExtensionBeans(version, "Reverse"));

                //Oct 08 for redesign
                request.setAttribute("radioStopMap", AddPrimers.radioStopMap);

                response.setStatus(HttpServletResponse.SC_OK);
                request.setAttribute("constructBean", cb);
                version.commit();
                request.getRequestDispatcher("/JSP/construct/DesignExtensions.jsp")
                    .forward(request, response);
            }
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

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
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Hook not supplied");
            return;
        }
        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        try {
            final String hook = pathInfo.substring(1);
            final ResearchObjectiveElement roe =
                (ResearchObjectiveElement) this.getRequiredObject(version, request, response, hook);
            final ConstructBean cb = this.setConstructBean(version, roe, request);
            this.setPostParameters(request, cb);
            //final String wizardStep = request.getParameter("wizard_step");
            final Map<String, String> parms = PIMSServlet.getParameterMap(request);
            // was if (wizardStep.equals("Save")) {

            this.prepareCB(parms, version, cb);
            cb.suggestPrimerNames();
            assert null != cb.getName();
            // calculate protein sequences and their properties
            if (null != roe.getTarget() && !PIMSTarget.isDNATarget(roe.getTarget())) {
                ConstructBean.annotate(cb);
                ConstructBeanWriter.addSequences(version, cb, roe);
            }
            ConstructBeanWriter.createPrimerDesignExperiment(version, cb, roe.getResearchObjective());
            //New Extension code 030708 added back 150909
            this.createNewExtension(version, cb, request.getParameter("new_fExtName"),
                request.getParameter("new_rExtName"), request.getParameter("new_fExtTag"),
                request.getParameter("new_rExtTag"), request.getParameter("new_fExtEnz"),
                request.getParameter("new_rExtEnz"));

            final String roHook = roe.getResearchObjective().get_Hook();
            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + roHook);
            return;
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final NumberFormatException e1) {
            e1.printStackTrace(); //TODO remove
            this.writeErrorHead(request, response, "bad number: " + e1.getMessage(),
                HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    final static float DEFTM = 60.0f;

    private ConstructBean setConstructBean(final ReadableVersion version, final ResearchObjectiveElement roe,
        final HttpServletRequest request) throws ServletException, IOException {
        //TODO if its a polycistronic construct, there will be several ROEs in the virtual construct

        final TargetBean tb = new TargetBean(roe.getTarget()); //TODO NPE

        /* never used
        //For synthetic gene, need different DNA sequence
        final String sampleHook = request.getParameter("sampleHook");
        if (null != sampleHook && "" != sampleHook) {
            final Sample sgSample = version.get(sampleHook);
            final SyntheticGeneBean sgb = new SyntheticGeneBean(sgSample);
            request.setAttribute("syntheticGeneBean", sgb);
            final String dnaSeq = sgb.getDnaSeq().toUpperCase();
            tb.setDnaSeq(dnaSeq);
        }
        request.setAttribute("sampleHook", sampleHook); */

        final ConstructBean cb = ConstructBeanReader.readConstruct(roe.getResearchObjective());
        assert null != cb.getTargetDnaSeq();
        /* was     new ConstructBean(tb, new PrimerBean(PrimerBean.FORWARD), new PrimerBean(PrimerBean.REVERSE));
        cb.setName(roe.getExpressionObjective().getName());
        
        final LabNotebook notebook = roe.getAccess();
        if (notebook != null) {
            cb.setAccess(new ModelObjectBean(notebook));
        } */
        cb.setDnaTarget(request.getParameter("dna_target")); // no protein sequence in target
        //DNA Sequence formatted for JSPs
        final List<String> chunks = SequenceUtility.chunkSeq(cb.getTargetDnaSeq());
        request.setAttribute("chunks", chunks);
        //Oct 08 after re-design
        //Susy 23-62010 for version 4.1 -displaying target protein sequence NOT translated sequence

        if (null == cb.getDnaTarget() || "".equals(cb.getDnaTarget())) {
            final int THREE = 3;
            cb.setTargetProtStart(roe.getApproxBeginSeqId());
            cb.setTargetProtEnd(roe.getApproxEndSeqId());
            // was cb.setTargetDnaSeq(roe.getTarget().getSeqString());
            cb.setDnaSeq(cb.getTargetDnaSeq().substring(((cb.getTargetProtStart()) * THREE) - THREE,
                cb.getTargetProtEnd() * THREE));

            //06-02-09 processing non-standard start codons
            if ("on".equals(request.getParameter("fixNonstandardStartCodon"))) {
                cb.setDnaSeq("A" + cb.getDnaSeq().substring(1));
            }
            cb.setProtSeq(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
            //Oct 08 after re-design
            final List<String> protConDNAChunks = SequenceUtility.chunkSeq(cb.getDnaSeq());
            request.setAttribute("protConDNAChunks", protConDNAChunks);
            final List<String> protConChunks = SequenceUtility.chunkSeq(cb.getProtSeq());
            request.setAttribute("protConChunks", protConChunks);
            cb.setTargetProtSeq(ThreeLetterProteinSeq.translate(tb.getDnaSeq()));
            final List<String> transSeqChunks =
                SequenceUtility.chunkSeq(ThreeLetterProteinSeq.translate(cb.getTargetDnaSeq()));
            //final List<String> transSeqChunks = SequenceUtility.chunkSeq(cb.getTargetProtSeq());
            request.setAttribute("transSeqChunks", transSeqChunks);
        } else {
            cb.setTargetDnaStart(roe.getApproxBeginSeqId());
            cb.setTargetDnaEnd(roe.getApproxEndSeqId());
            /* final String startDT = "";
             final String endDT = "";
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
             final String targetDnaSeq = request.getParameter("target_dna_seq"); */
            cb.setDnaSeq(cb.getTargetDnaSeq().substring((cb.getTargetDnaStart()) - 1, cb.getTargetDnaEnd()));
            final List<String> constructChunks = new DnaSequence(cb.getDnaSeq()).chunkSeq();
            request.setAttribute("constructChunks", constructChunks);
        }

        // was cb.setPrimersProvided(new Boolean(request.getParameter("primersProvided")));

        final String tm = request.getParameter(AddPrimers.DESIRED_TM);
        if (null != tm && !"".equals(tm)) {
            cb.setDesiredTm(Float.parseFloat(tm));
        }

        final String forward = request.getParameter("forward_primer");
        cb.setFwdPrimer(forward);
        cb.setFwdOverlapLen(null == forward ? 0 : forward.length());
        final String reverse = request.getParameter("reverse_primer");
        cb.setRevPrimer(reverse);
        cb.setRevOverlapLen(null == reverse ? 0 : reverse.length());

        return cb;
    }

    /**
     * AddPrimers.setPostParameters
     * 
     * @param request
     * @param cb
     */
    private void setPostParameters(final HttpServletRequest request, final ConstructBean cb) {
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
            cb.setFinalProt(request.getParameter("final_prot")); //TODO probably none of this used
            cb.setReverseTag(request.getParameter("expressed_prot_c"));
            final String final_c = request.getParameter("final_prot_c");
            cb.setFinalProtC(final_c == null ? "" : final_c);

        }
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
    private void prepareCB(final Map<String, String> parms, final WritableVersion version,
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

        cb.setUserHook(parms.get("personHook")); // TODO remove

        // ConstructAnnotator.annotate(cb);
        cb.setDateOfEntry(new java.util.Date());

    }

    private static final java.util.Map<String, String> radioStopMap = new LinkedHashMap<String, String>();
    static {
        AddPrimers.radioStopMap.put("TTA", "TAA (Adds TTA to 5'-end)");
        AddPrimers.radioStopMap.put("TCA", "TGA (Adds TCA to 5'-end)");
        AddPrimers.radioStopMap.put("CTA", "TAG (Adds CTA to 5'-end)");
        AddPrimers.radioStopMap.put("", "none");
    }

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

}
