package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.targets.MPSITargetName;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.Organism;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.TargetBeanWriter;
import org.pimslims.servlet.PIMSServlet;

/**
 * Servlet to handle the creation of a new SPOT Target
 * 
 * @author Johan van Niekerk
 */
public class SpotNewTarget extends PIMSServlet {

    /**
     * Code to satisy Serializable Interface
     */
    private static final long serialVersionUID = -5851882064372940058L;

    protected static Boolean nonProtTarget = false;

    /**
     * @return Servlet descriptor string
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "SPoT new Target page";
    }

    /**
     * Show the New Target form
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);

        try {
            if (version == null) {
                return; // error message has already been sent
            }
            // always use a transaction in a try/catch block

            final Collection<Organism> natSrcs =
                PIMSServlet.getAll(version, org.pimslims.model.reference.Organism.class);

            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            SpotNewTarget.setPeople(request, version);
            request.setAttribute("organisms", ModelObjectBean.getModelObjectBeans(natSrcs));
            request.setAttribute("suggestedName",
                MPSITargetName.getTargetCommonName(version, PIMSServlet.getUsername(request)));
            request.setAttribute("dnaTypes", SpotNewTarget.dnaTypes);
            version.commit();

            //300108 modified to work with promoterTarget
            //RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/spot/SpotNewTarget.jsp");
            final RequestDispatcher dispatcher = request.getRequestDispatcher(this.getJspPath());
            dispatcher.forward(request, response);

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
     * @return String path to the calling JSP
     */
    protected String getJspPath() {
        return "/JSP/spot/SpotNewTarget.jsp";
        //return "/JSP/dnaTarget/NewTarget.jsp";
    }

    /**
     * Create beans representing the scientists
     * 
     * @param request
     * @param version
     * @throws ServletException
     */
    public static void setPeople(final HttpServletRequest request, final ReadableVersion version)
        throws ServletException {
        request.setAttribute("people", Collections.EMPTY_LIST);
        final Person person = PIMSServlet.getPersonfromUser(PIMSServlet.getCurrentUser(version, request));
        if (null != person) {
            final ModelObjectBean bean = new ModelObjectBean(person);
            request.setAttribute("currentPerson", bean);
            request.setAttribute("people", Collections.singleton(bean));
        }
    }

    /**
     * Post causes a Target to be created
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Get a WritableVersion
        final WritableVersion version = this.getWritableVersion(request, response);
        String hook = null;
        try {
            final TargetBean tb = new TargetBean();
            final String organismHook = request.getParameter("organismId");
            if (null != organismHook) {
                final ModelObject organism = version.get(organismHook);
                tb.setOrganism(BeanFactory.newBean(organism));
            }
            tb.setPersonHook(request.getParameter("personHook"));

            if (null != request.getParameter("accessId")) {
                final ModelObject access = version.get(request.getParameter("accessId"));
                if (null != access) {
                    version.setDefaultOwner((LabNotebook) access);
                }
            }

            //new field for DNA target
            if (null != request.getParameter("dnaName")) {
                tb.setDnaName(request.getParameter("dnaName"));
                SpotNewTarget.nonProtTarget = true;
            }

            //new field for Natural Source target
            if (null != request.getParameter("sourceName")) {
                tb.setSourceName(request.getParameter("sourceName"));
                SpotNewTarget.nonProtTarget = true;
            }

            //natural source targets do not have a dna sequence
            if (null != request.getParameter("dnaSeq")) {
                tb.setDnaSeq(request.getParameter("dnaSeq"));
            }
            this.setProteinProperties(request, tb);
            // was tb.setGi_number(request.getParameter("giNumber"));
            tb.setComments(request.getParameter("comments"));
            tb.setFunc_desc(request.getParameter("funcDesc"));
            tb.setTarget_id(request.getParameter("targetId"));

            final ModelObject modelObject = TargetBeanWriter.createNewTarget(version, tb);
            hook = modelObject.get_Hook();
            version.commit();
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // now show the new target
        PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);
    }

    /**
     * @param request
     * @param tb
     */
    protected void setProteinProperties(final HttpServletRequest request, final TargetBean tb) {
        tb.setProtSeq(request.getParameter("protSeq"));
        tb.setProtein_name(request.getParameter("proteinName"));
        //TODO if protein sequence is null, translate DNA sequence
        if ("".equals(tb.getProtSeq()) && !"".equals(tb.getDnaSeq())) {
            final String protSeq = ThreeLetterProteinSeq.translate(tb.getDnaSeq());
            tb.setProtSeq(protSeq);
        }
    }

    // Mapping for DNA Target types
    //TODO move to better location
    public static final java.util.Map<String, String> dnaTypes = new HashMap<String, String>();
    static {
        SpotNewTarget.dnaTypes.put("Intron", "Intron");
        SpotNewTarget.dnaTypes.put("5'-UTR", "5'-UTR");
        SpotNewTarget.dnaTypes.put("CRM", "CRM");
        SpotNewTarget.dnaTypes.put("Enhancer", "Enhancer");
        SpotNewTarget.dnaTypes.put("Promoter", "Promoter");
        SpotNewTarget.dnaTypes.put("genomic DNA", "genomic DNA");
        SpotNewTarget.dnaTypes.put("non-coding region", "non-coding region");
        SpotNewTarget.dnaTypes.put("DNA", "DNA");
        SpotNewTarget.dnaTypes.put("Vector", "Vector");

    }

}
