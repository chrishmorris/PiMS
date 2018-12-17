package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.experiment.ExperimentWizardCreator;
import org.pimslims.utils.experiment.ExperimentWizardFieldsDecoder;
import org.pimslims.utils.sequenator.SeqSetupExperiment;

/**
 * @author Peter Troshin
 * 
 */
@Deprecated
// obsolete
public class ExperimentWizardCreate extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Generic create experiment based on the protocol and details supplied from JSP pages";
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

        if (!Util.isHookValid(protocolHook)) {
            //TODO RETURN // should not be its internal data transfer 
        }
        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        final Protocol protocol = rw.get(protocolHook);
        if (protocol == null) {
            //TODO RETURN // something wrong with reference data ? 
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

    /*
     * Protocol name and jsp to forward to supplied as a part of request
     * @param: pname=protocol name
     * @param: url= url of the view 
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String protName = request.getParameter("pname");
        String url = request.getParameter("url");

        ReadableVersion rv = null;
        try {
            rv = this.getReadableVersion(request, response);
            if (rv == null) {
                return;
            }

            Collection<Protocol> protocols = null;
            if (!Util.isEmpty(protName)) {
                protocols = rv.findAll(Protocol.class, Protocol.PROP_NAME, this.clean(protName));

                if (protocols.size() != 1) {
                    request.setAttribute("message", "Warning! More then one or none protocols with name '"
                        + protName + "' has been found! Please load the protocol first. ");
                    final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
            }
            if (Util.isEmpty(url)) {
                request.setAttribute("message", "Warning! Cannot find a view page here: " + url);
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                return;

            }

            //            NewFermentation.setDefaultCreator(rv, request, PIMSServlet.getCurrentUser(rv, request));

            // Remove all special symbols just in case. 
            url = this.clean(url);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/" + url + ".jsp");

            request.setAttribute("version", rv);
            //request.setAttribute("currentPersonHook", super.getCurrentPersonHook(request, response));
            request.setAttribute("protocolH", protocols.iterator().next().get_Hook());
            request.setAttribute("curDate", SeqSetupExperiment.getCurrentDate());
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

    private String clean(String url) {
        url = url.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", "");
        url = url.replaceAll("\\?", "").replaceAll("&", "").replaceAll("%", "");
        url = url.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
        url = url.replaceAll(";", "").replaceAll(":", "").replaceAll("=", "").trim();
        return url;
    }
}
