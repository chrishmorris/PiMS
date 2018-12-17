package org.pimslims.servlet.sequencing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.ExperimentCopier;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.NoteManager;
import org.pimslims.utils.sequenator.SOrdersManager;
import org.pimslims.utils.sequenator.SequencingInputDataParser;
import org.pimslims.utils.sequenator.SequencingOrder;

/**
 * @author Petr Troshin
 * 
 *         Copy Experiments marked for Re-Processing from Sequencing Order
 * 
 * 
 * @baseURL /update/CopySOrder/SO_32
 * @baseURL
 */
public class CopySOrder extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Experiments marked for Re-Processing from Sequencing Order. An Experiment to be copied needs to be Failed and marked for Reprocessing.";
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

        final String pathInfo = request.getPathInfo();
        String soid = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            soid = pathInfo.substring(1); // e.g.
            // Search/org.pimslims.model.experiment.Experiment
        }

        final PrintWriter writer = response.getWriter();
        if (Util.isEmpty(soid)) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Order ID to copy is not found!");
            return;
        }
        final String ignoreMarkedForReprocessionTag = request.getParameter("ignoreReProcessedTag");
        String newOrderId = "";
        WritableVersion rw = null;
        try {

            rw = this.getWritableVersion(request, response);

            final Collection<Experiment> exps = Utils.getExperimentsFromOrder(soid, rw);
            assert !exps.isEmpty();

            if (Util.isEmpty(ignoreMarkedForReprocessionTag)) {
                // Make sure there is something to copy first
                if (!Utils.hasValidExpForCopying(exps)) {
                    PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                    writer
                        .print("No experiments has status failed and marked for ReProcessing thus nothing to copy!");
                    this.commit(request, response, rw);
                    return;
                }
            } else {
                if (!Utils.hasFailedExperiments(exps)) {
                    PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                    writer.print("No experiments has status failed thus nothing to copy!");
                    this.commit(request, response, rw);
                    return;
                }
            }
            // Now can get an id as there is something to copy 
            newOrderId = Utils.getOrderId(rw);

            for (final Experiment exp : exps) {
                if (!exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.Failed.toString())) {
                    continue;
                }
                if (Util.isEmpty(ignoreMarkedForReprocessionTag)) {
                    final NoteManager nm = new NoteManager(exp);
                    if (!nm.isMarkedforReprocessing()) {
                        continue;
                    }
                }
                final Experiment duplicate = ExperimentCopier.duplicate(exp, rw);
                // Reset Status as for a new order
                duplicate.setStatus(SOrdersManager.ExpStatus.To_be_run.toString());
                // Remove ExperimentGroups
                duplicate.remove(Experiment.PROP_EXPERIMENTGROUP, duplicate.getExperimentGroup());
                for (final Annotation a : duplicate.getAnnotations()) {
                    duplicate.remove(LabBookEntry.PROP_ATTACHMENTS, a);
                }
                // Have to create a new Sample for output 
                this.setSampleForOutput(duplicate, rw);
                // Define new dates
                final Calendar c = Calendar.getInstance();
                duplicate.setStartDate(c);
                c.add(Calendar.DAY_OF_MONTH, 1);
                duplicate.setEndDate(c);
                // Set a new Order Id
                final Parameter p = Utils.getParameter(duplicate, SequencingInputDataParser.orderParamName);
                assert p != null;
                p.setValue(newOrderId);
            }

            this.commit(request, response, rw);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
        // This parameter has to be in to make id editable
        request.setAttribute("isNew", true);
        final RequestDispatcher rd = request.getRequestDispatcher("/read/ViewSequencingOrder/" + newOrderId);
        rd.forward(request, response);

    }

    private void setSampleForOutput(final Experiment duplicate, final WritableVersion rw)
        throws ConstraintException {
        // Define new Sample for output for this experiment
        assert duplicate.getOutputSamples().size() == 1;
        final OutputSample outSample = duplicate.getOutputSamples().iterator().next();
        final String tName = Utils.getParameterValue(duplicate, SequencingOrder.TemplateName);
        assert !Util.isEmpty(tName);
        final Sample duplSample = new Sample(rw, tName + "_" + System.currentTimeMillis());
        // This is defined by the Sequencing Order protocol
        final SampleCategory sc =
            rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Nucleic acid");
        assert sc != null;
        duplSample.setSampleCategories(Collections.singleton(sc));
        outSample.setSample(duplSample);
    }
}
