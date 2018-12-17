package org.pimslims.servlet.fermentation;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.leeds.SavedPlasmid;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.experiment.ExperimentWizardCreator;
import org.pimslims.utils.experiment.ExperimentWizardFieldsDecoder;
import org.pimslims.utils.experiment.Utils;

/**
 * @author Peter Troshin
 * 
 */

@Deprecated
// Leeds code is obsolete
public class NewFermentation extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Record a new Leeds Frementation Culture experiment Leeds";
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

        // Default
        final String protocolHook = request.getParameter("protocol");

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        final Protocol protocol = rw.get(protocolHook);
        if (protocol == null) {
            throw new ServletException("No such protocol: " + protocolHook);
        }
        Experiment experiment = null;
        final ExperimentWizardFieldsDecoder ed = new ExperimentWizardFieldsDecoder(request.getParameterMap());

        try {
            final ExperimentWizardCreator eWriter = new ExperimentWizardCreator(rw, protocol, ed);
            experiment = eWriter.getExperiment();

            //NewFermentation.setDefaultCreator(rv, request, PIMSServlet.getCurrentUser(rv, request));

            //request.setAttribute("record", record);

            rw.commit();
        } catch (final AccessException e) {
            e.printStackTrace();
            throw new ServletException(e.getLocalizedMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e.getLocalizedMessage());
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new ServletException(e.getLocalizedMessage());
        } finally {
            if (rw != null) {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
            }
        }
        request.setAttribute("_tab", "expDetails");
        this.redirect(response, request.getContextPath() + "/View/" + experiment.get_Hook());

    }

    /**
     * @param request
     * @param response
     * @param experiment
     * @throws ServletException
     * @throws IOException private void doPostNotFile(final HttpServletRequest request, final
     *             HttpServletResponse response, Experiment experiment) throws ServletException, IOException {
     *             // Create construct final String constType = request.getParameter("type"); Map<String,
     *             String> resource =
     *             LeedsFormServletUtils.resourceToMap(AbstractConstructBean.getEntryCloneRb());
     * 
     *             AbstractConstructBean leedsPl = null; final WritableVersion rw =
     *             this.getWritableVersion(request, response); if (rw == null) { return; } try {
     * 
     *             if (constType != null && constType.equalsIgnoreCase("DeepFrozenCulture")) { // Override
     *             defaults if this is deep frozen culture resource =
     *             LeedsFormServletUtils.resourceToMap(AbstractConstructBean.getDeepFrozenCultureRb());
     *             leedsPl = (DeepFrozenCultureBean) LeedsFormServletUtils.updateProperties(
     *             DeepFrozenCultureBean.class, request, resource); } else if (constType != null &&
     *             constType.equalsIgnoreCase("Expression")) { leedsPl = (LeedsConstructBean)
     *             LeedsFormServletUtils.updateProperties(LeedsConstructBean.class, request, resource);
     *             request.setAttribute("record", "Expression"); request.setAttribute("expression", true); }
     *             else { leedsPl = (LeedsConstructBean)
     *             LeedsFormServletUtils.updateProperties(LeedsConstructBean.class, request, resource);
     *             request.setAttribute("record", "Entry Clone"); }
     * 
     *             experiment = leedsPl.record(rw); //
     *             rw.getDefaultOwner(getModel().getMetaClass(Experiment.class.getName()), // null) // create
     *             record under default dataowner
     * 
     *             final String hook = experiment.get_Hook(); rw.commit(); this.redirect(response,
     *             request.getContextPath() + "/Construct/" + hook); } catch (final AccessException e) {
     *             e.printStackTrace(); throw new ServletException(e); } catch (final ClassNotFoundException
     *             e) { e.printStackTrace(); throw new ServletException(e); } catch (final SecurityException
     *             e) { e.printStackTrace(); throw new ServletException(e); } catch (final
     *             IllegalArgumentException e) { e.printStackTrace(); throw new ServletException(e); } catch
     *             (final InstantiationException e) { e.printStackTrace(); throw new ServletException(e); }
     *             catch (final IllegalAccessException e) { e.printStackTrace(); throw new
     *             ServletException(e); } catch (final InvocationTargetException e) { e.printStackTrace();
     *             throw new ServletException(e); } catch (final ConstraintException e) { e.printStackTrace();
     *             if (e instanceof CustomConstraintException) { System.out.println("AttrName" +
     *             ((CustomConstraintException) e).attributeName); System.out.println("AttrValue" +
     *             ((CustomConstraintException) e).value); System.out.println("MName" +
     *             ((CustomConstraintException) e).metaClassName); }
     *             request.setAttribute("javax.servlet.error.exception", e);
     *             request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response); }
     *             catch (final ConstraintViolationException e) { e.printStackTrace();
     *             request.setAttribute("javax.servlet.error.exception", e);
     *             request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response); }
     *             catch (final AbortedException e) { e.printStackTrace(); throw new ServletException(e); }
     *             finally { if (rw != null) { if (!rw.isCompleted()) { rw.abort(); } } } return; }
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String resourceSrc = "/JSP/fermentation/Culture.jsp";

        ReadableVersion rv = null;
        try {
            rv = this.getReadableVersion(request, response);
            if (rv == null) {
                return;
            }

            final Protocol protocol =
                rv.findFirst(Protocol.class, Protocol.PROP_NAME, "Generic fermenter culture");
            if (protocol == null) {
                request.setAttribute("message",
                    "The 'Generic fermenter culture' protocol is not found, please load it first!");
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                return;
            }
            //            NewFermentation.setDefaultCreator(rv, request, PIMSServlet.getCurrentUser(rv, request));

            final RequestDispatcher rd = request.getRequestDispatcher(resourceSrc);

            request.setAttribute("version", rv);
            //request.setAttribute("currentPersonHook", super.getCurrentPersonHook(request, response));
            request.setAttribute("deepFrozenList",
                SavedPlasmid.getPlasmids(SavedPlasmid.getDeepFrozenCultures(rv)));
            request.setAttribute("entryCloneList",
                SavedPlasmid.getPlasmids(SavedPlasmid.getEntryCloneExperiments(rv)));
            request.setAttribute("expressionList",
                SavedPlasmid.getPlasmids(SavedPlasmid.getExpressionConstructExperiments(rv)));
            request.setAttribute("antibiotics", Utils.getAntibiotics(rv));
            request.setAttribute("sample", Utils.getAntibiotics(rv).iterator().next());
            request.setAttribute("cultureTypes", Utils.getParameterDefinition(protocol, "Culture Type"));
            request.setAttribute("protocol", protocol);
            request.setAttribute("protocolH", protocol.get_Hook());
            request.setAttribute("curDate", org.pimslims.utils.sequenator.SeqSetupExperiment.getCurrentDate());
            rd.forward(request, response);

            rv.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (rv != null) {
                if (!rv.isCompleted()) {
                    rv.abort();
                }
            }
        }
    } // get end 

}
