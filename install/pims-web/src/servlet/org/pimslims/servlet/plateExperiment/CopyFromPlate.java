package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.presentation.PlateExperimentUpdateBean;
import org.pimslims.presentation.plateExperiment.PlateReader;

public class CopyFromPlate extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.plateExperiment.CopyFromPlate doGet");
        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            RefInputSample refInputSample = null;
            if (null != request.getParameter("refInputSample")) {
                refInputSample =
                    (RefInputSample) this.getRequiredObject(rv, request, response,
                        request.getParameter("refInputSample"));
            }
            if (null == refInputSample) {
                return;
            }
            request.setAttribute("refInputSample", refInputSample);

            final Map values = new HashMap();
            if (null == refInputSample.getAmount()) {
                values.put(refInputSample.getName(), new Float("0"));
            } else {
                values.put(refInputSample.getName(), refInputSample.getAmount());
            }
            values.put(refInputSample.getName() + "Unit", refInputSample.getUnit());
            values.put(refInputSample.getName() + "DisplayUnit", refInputSample.getDisplayUnit());
            final Measurement measurement = Measurement.getMeasurement(values, refInputSample.getName());
            System.out.println("Measurement [" + measurement.getValue() + ":" + measurement.getDisplayValue()
                + "]");
            request.setAttribute("measurement", measurement);

            if (null != request.getParameter("outputGroup")) {
                final PlateReader reader =
                    new PlateReader(rv, (ExperimentGroup) this.getRequiredObject(rv, request, response,
                        request.getParameter("outputGroup")), null);
                final PlateExperimentUpdateBean outputGroupBean =
                    new PlateExperimentUpdateBean(reader.getExperimentGroup(), null);

                request.setAttribute("outputGroupBean", outputGroupBean);
                /*
                if (outputGroupBean.isPlateExperiment()) {
                    request.setAttribute("outputGroupName", outputGroupBean.getExperimentGroup().get_Name());
                    request.setAttribute("outputPlateRows", outputGroupBean.getHolderRows());
                    request.setAttribute("outputPlateCols", outputGroupBean.getHolderColumns());
                    final Collection<Holder> holders =
                        HolderFactory.getHolders(outputGroupBean.getExperimentGroup());
                    for (final Holder plate : holders) {
                        request.setAttribute("outputGroup" + HolderFactory.getHolderPoint(plate) + "Name",
                            plate.getName());
                        request.setAttribute("outputGroup" + HolderFactory.getHolderPoint(plate) + "Hook",
                            plate.get_Hook());
                        request.setAttribute("outputGroup" + HolderFactory.getHolderPoint(plate),
                            outputGroupBean.getPlateExperimentLayout(plate));
                    }
                }
                */

                //request.setAttribute("outputGroupPlateExperimentBeans", outputGroupBean.getExperimentBeans());
                //request.setAttribute("outputGroupProtocolBean", outputGroupBean.getProtocolBean());
            }

            if (null != request.getParameter("inputGroup")) {
                final ExperimentGroup group =
                    (ExperimentGroup) this.getRequiredObject(rv, request, response,
                        request.getParameter("inputGroup"));
                final PlateExperimentUpdateBean inputGroupBean =
                    new PlateExperimentUpdateBean(group, null);

                request.setAttribute("inputGroupBean", inputGroupBean);
                /*
                if (inputGroupBean.isPlateExperiment()) {
                    request.setAttribute("inputGroupName", inputGroupBean.getExperimentGroup().get_Name());
                    request.setAttribute("inputPlateRows", inputGroupBean.getHolderRows());
                    request.setAttribute("inputPlateCols", inputGroupBean.getHolderColumns());
                    final Collection<Holder> holders =
                        HolderFactory.getHolders(inputGroupBean.getExperimentGroup());
                    for (final Holder plate : holders) {
                        request.setAttribute("inputGroup" + HolderFactory.getHolderPoint(plate) + "Name",
                            plate.getName());
                        request.setAttribute("inputGroup" + HolderFactory.getHolderPoint(plate) + "Hook",
                            plate.get_Hook());
                        request.setAttribute("inputGroup" + HolderFactory.getHolderPoint(plate),
                            inputGroupBean.getPlateExperimentLayout(plate));
                    }
                }
                */
                //request.setAttribute("inputGroupPlateExperimentBeans", inputGroupBean.getExperimentBeans());
                //request.setAttribute("inputGroupProtocolBean", inputGroupBean.getProtocolBean());
            }

            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/plateExperiment/copyFromPlate.jsp");
            rd.forward(request, response);

            rv.commit();
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Not yet implemented");
    }

    @Override
    public String getServletInfo() {
        return "For copying samples between wells.";
    }

}
