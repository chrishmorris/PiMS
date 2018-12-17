package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.bioinf.DbRefBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.servlet.PIMSServlet;

/**
 * Servlet to show details of a particular Target.
 * 
 * @author Johan van Niekerk
 */
public class SpotTarget extends PIMSServlet {

    /**
     * Code to satisfy Serializable Interface
     */
    private static final long serialVersionUID = -3156149674123157900L;

    /**
     * @return Servlet descriptor string
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "SPoT Target page";
    }

    /**
     * Show the requested Target
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
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

            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no target specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final String hook = pathInfo.substring(1);
            final Target t = version.get(hook);
            if (null == t) {
                this.writeErrorHead(request, response, "Target not found: " + hook,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Get a SPOTTarget
            final TargetBean tb = new TargetBean(t);

            // Calculate GC % and put in the request
            final Float seqGC = SpotTarget.calcGCContent(t, tb);
            request.setAttribute("gcCont", seqGC);

            request.setAttribute("taxonomyLink", DbRefBean.getTaxonomyLink(t));
            request.setAttribute("results", DbRefBean.getDBRefs(t));
            /* final ResearchObjective expb = TargetUtility.getLastTargetExpBlueprint(t);
            if (expb != null) {
                request.setAttribute("expBlueprintHook", expb.get_Hook());
            } */

            // Put our other results into the request
            final Collection<ConstructResultBean> milestones = SpotTarget.getMilestones(version, t);
            request.setAttribute("milestones", milestones);
            request.setAttribute("targetBean", tb);
            final List<ExperimentBean> targetExpBeans = new ArrayList();
            final Collection<PlateBean> crystalTrialExps = new LinkedList<PlateBean>();
            request.setAttribute("experimentMetaClass",
                this.getModel().getMetaClass(Experiment.class.getName()));
            SpotTarget.setExperimentBeans(version, t, targetExpBeans, crystalTrialExps);
            request.setAttribute("experimentBeans", targetExpBeans);
            request.setAttribute("crystalTrialExps", crystalTrialExps);
            request.setAttribute("mayUpdate", new Boolean(t.get_MayUpdate()));
            //request.setAttribute("leedsConstructs", AbstractSavedPlasmid.getLeedsConstruct(t));
            request.setAttribute("targetGroups", SpotTarget.targetGroups(t));
            request.setAttribute("mayUpdate", t.get_MayUpdate());
            request.setAttribute("owner", t.get_Owner());
            request.setAttribute("files", t.get_Files());

            request.setAttribute("writers", PIMSServlet.findWriters(t.getAccess()));

            if (null != request.getParameter("createDNA")) {
                request.setAttribute("createDNA", request.getParameter("createDNA"));
            }

            final ModelObject ec =
                version.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.ENTRY_CLONE_PROTOCOL);
            if (null != ec) {
                request.setAttribute("entryCloneProtocolHook", ec.get_Hook());
            }

            final List<ModelObjectBean> dbNames = new ArrayList<ModelObjectBean>();
            dbNames.addAll(ModelObjectBean.getModelObjectBeans(PIMSServlet.getAll(version,
                org.pimslims.model.reference.Database.class)));

            Collections.sort(dbNames, new Comparator<ModelObjectBean>() {
                public int compare(final ModelObjectBean c1, final ModelObjectBean c2) {
                    if (c1.getName().equals("unspecified")) {
                        return -1;
                    }
                    if (c2.getName().equals("unspecified")) {
                        return 1;
                    }
                    return c1.getName().compareTo(c2.getName());
                }
            });

            request.setAttribute("dbnames", dbNames);

            //Susy 260111 Synthetic gene samples for Target
            final Collection<SyntheticGeneBean> sgbs = SyntheticGeneManager.makeSGBeansForTarget(version, t);
            request.setAttribute("sgbs", sgbs);

            // Dispatch to the JSP
            if (PIMSTarget.isDNATarget(t)) {
                request.setAttribute("dnaTypes", SpotNewTarget.dnaTypes);

                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/dnaTarget/DNATarget.jsp");
                dispatcher.forward(request, response);
            } else {
                if (tb != null && tb.getProtSeq() != null) {
                    //PIMS-876 DNA sequence and protein sequence should match
                    final Boolean compareSeqs =
                        ThreeLetterProteinSeq.compareSeqs(tb.getProtSeq(), tb.getDnaSeq());
                    request.setAttribute("compareSeqs", compareSeqs);
                    final ProteinSequence pseq = new ProteinSequence(tb.getProtSeq());
                    final float protPI = pseq.getPI();
                    request.setAttribute("protPI", protPI);
                    final float protEX = pseq.getExtinctionCoefficient();
                    request.setAttribute("protEX", protEX);
                    final float protMass = pseq.getMass();
                    request.setAttribute("protMass", protMass);
                    final float abs01pc = pseq.getAbsPt1percent();
                    request.setAttribute("abs01pc", abs01pc);
                }

                response.setStatus(HttpServletResponse.SC_OK);
                final RequestDispatcher dispatcher = request.getRequestDispatcher(this.getJspPath());
                dispatcher.forward(request, response);
            }

        } catch (final IllegalArgumentException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /* 
     * If this still isn't fast enough, then do the join from Target to ResearchObjective in the HQL
     * */
    static void setExperimentBeans(final ReadableVersion version, final Target target,
        final List<ExperimentBean> experimentBeans, final Collection<PlateBean> crystalTrialExps) {
        //org.apache.log4j.Logger.getLogger("org.hibernate.SQL").debug("---started---");
        // get BluprintComponents
        final Set<ResearchObjectiveElement> blueprintComps = target.getResearchObjectiveElements();

        for (final Iterator i = blueprintComps.iterator(); i.hasNext();) {
            final ResearchObjectiveElement blueprintComponent = (ResearchObjectiveElement) i.next();

            final ResearchObjective expBlueprint = blueprintComponent.getResearchObjective();
            SpotTarget.setExperimentBeans(experimentBeans, crystalTrialExps, expBlueprint);
        }

        java.util.Collections.sort(experimentBeans);
        //org.apache.log4j.Logger.getLogger("org.hibernate.SQL").debug("---done---");
    }

    static void setExperimentBeans(final List<ExperimentBean> experimentBeans,
        final Collection<PlateBean> crystalTrialExps, final ResearchObjective expBlueprint) {
        final Collection<ExperimentGroup> expGroups =
            PlateExperimentDAO.getCrystallizationPlates(expBlueprint);
        //add to crystalTrialExps
        for (final ExperimentGroup expGroup : expGroups) {
            crystalTrialExps.add(new PlateBean(expGroup));
        }
        final Collection<Experiment> expLst =
            ConstructBeanReader.getNonCrystallizationExperiments(expBlueprint);

        for (final Experiment exp : expLst) {

            final ExperimentBean esb = new ExperimentBean(exp);
            experimentBeans.add(esb);

        }
    }

    static final Collection<String> CrystalTrialExpTypeNames = new HashSet<String>();
    {
        SpotTarget.CrystalTrialExpTypeNames.add("Trials");
    }

    /**
     * SpotTarget.getLinkedRecords
     * 
     * @param t
     * @return
     */
    public static List<ModelObjectShortBean> getLinkedRecords(final Target target) {
        final List<ModelObjectShortBean> ret = new ArrayList(4);

        final Set<Molecule> nas = target.getNucleicAcids();
        for (final Iterator iterator = nas.iterator(); iterator.hasNext();) {
            final Molecule molecule = (Molecule) iterator.next();
            ret.add(new ModelObjectShortBean(molecule));
        }

        final Set<ResearchObjective> ros = new HashSet();
        final Set<ResearchObjectiveElement> roes = target.getResearchObjectiveElements();
        for (final Iterator iterator = roes.iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            final ResearchObjective ro = element.getResearchObjective();
            if (!ros.contains(ro)) {
                if (ro != null) {
                    ret.addAll(ModelObjectShortBean.getBeansInOriginalOrder(ConstructBeanWriter
                        .getConstructParts(ro)));
                }
            }
        }

        ret.add(new ModelObjectShortBean(target));
        ret.add(new ModelObjectShortBean(target.getProtein())); // delete this last
        return ret;
    }

    /**
     * @return String path to the calling JSP
     */
    protected String getJspPath() {
        return "/JSP/spot/SpotTarget.jsp";
        //return "/JSP/dnaTarget/NewTarget.jsp";
    }

    public static Collection<ModelObjectShortBean> targetGroups(final Target target) {
        return ModelObjectShortBean.getBeans(target.getTargetGroups());
    }

    public static Collection<ConstructResultBean> getMilestones(final ReadableVersion version, final Target t) {
        final List<ConstructResultBean> milestones = new ArrayList<ConstructResultBean>();
        for (final Iterator iterator = t.getResearchObjectiveElements().iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            final ResearchObjective ro = element.getResearchObjective();
            if (ro != null) {
                if (!ConstructUtility.isSpotConstruct(ro)) {
                    SpotTarget.findMilestonesFromConstructDesignExperiments(ro, milestones);
                    continue;
                }
                //Susy for synthetic gene
                Boolean synthGene = false;
                String sgSampleHook = "";
                if (ro.getSimilarityDetails().contains("Synthetic gene ::")) {
                    synthGene = true;
                    String[] sgBits = new String[3];
                    sgBits = ro.getSimilarityDetails().split("::");
                    sgSampleHook = sgBits[2].trim();
                }
                // Get thelatest status
                // TODO: Fix this so that it is indeed the latest milestone and not
                // just the first milestone
                Milestone latestBlueprintStatus = null;

                if ((ro.getMilestones()).size() != 0) {
                    latestBlueprintStatus = ro.getMilestones().iterator().next();
                }

                // If we have a status
                if (latestBlueprintStatus != null) {

                    // Convert to a SPOTConstructMilestone
                    final ConstructResultBean latestScm =
                        new ConstructResultBean(version, latestBlueprintStatus);
                    latestScm.setSyntheticGeneMilestone(synthGene);
                    latestScm.setSgeneHook(sgSampleHook);

                    // Store the SPOTConstructMilestone
                    milestones.add(latestScm);

                }

                /*
                 * Else we used to make one up Assume its missing a selected milestone?
                 

                else {

                    // Get a new SPOTConstructMilestone
                    final ConstructResultBean scm = new ConstructResultBean();

                    // Set values for the fields needed by SpotTarget.jsp
                    scm.setConstructId(ro.getName());
                    scm.setConstructDescription(ro.getFunctionDescription());
                    scm.setConstructHook(ro.get_Hook());
                    scm.setMilestoneName(null);

                    scm.setDateOfExperiment(null);
                    scm.setSyntheticGeneMilestone(synthGene);
                    scm.setSgeneHook(sgSampleHook);

                    // Store the SPOTConstructMilestone
                    milestones.add(scm);
                } */
            }
        }
        // sort target status into date order (most recent first)
        Collections.sort(milestones, SpotTarget.TARGET_COMPARATOR);

        return milestones;
    }

    /**
     * SpotTarget.findMilestonesFromConstructDesignExperiments
     * 
     * @param ro
     * @param milestones
     */
    private static void findMilestonesFromConstructDesignExperiments(final ResearchObjective ro,
        final List<ConstructResultBean> milestones) {
        final ReadableVersion version = ro.get_Version();
        final ExperimentType design =
            version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Construct Design");
        final Collection<Experiment> experiments =
            ro.findAll(ResearchObjective.PROP_EXPERIMENTS, Experiment.PROP_EXPERIMENTTYPE, design);
        for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            final ConstructResultBean bean = new ConstructResultBean();
            bean.setConstructDescription(experiment.getDetails());
            bean.setConstructHook(experiment.get_Hook());
            bean.setConstructId(experiment.getName());
            bean.setMilestoneName("construct");
            bean.setDateOfExperiment(experiment.getEndDate());
            milestones.add(bean);
        }
    }

    public static final Comparator TARGET_COMPARATOR = new Comparator() {
        public int compare(final Object obj1, final Object obj2) {

            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            final ConstructResultBean scm1 = (ConstructResultBean) obj1;
            final ConstructResultBean scm2 = (ConstructResultBean) obj2;

            final Calendar d1 = scm1.getDateOfExperiment();
            final Calendar d2 = scm2.getDateOfExperiment();

            if (d1 == null) {
                if (d2 == null) {
                    return EQUAL;
                }
                return AFTER;
            }

            if (d2 == null) {
                return BEFORE;
            }

            return d2.compareTo(d1);
        }
    };

    /**
     * @param t Target
     * @param tb TargetBean
     * @return
     */
    public static Float calcGCContent(final Target t, final TargetBean tb) {
        try {
            DnaSequence seq = null;
            Float gc;
            if (tb != null && tb.getDnaSeq() != null) {
                seq = new DnaSequence(tb.getDnaSeq());
            } else if (PIMSTarget.isDNATarget(t)) {
                seq = new DnaSequence(tb.getProtSeq());
            } else {
                seq = new DnaSequence("");
            }
            gc = seq.getGCContent();
            return gc;
        } catch (final IllegalArgumentException ex) {
            // bad sequence
            return 0f;
        }
    }

}
