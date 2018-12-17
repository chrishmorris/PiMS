package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.plateExperiment.DefaultPlateName;
import org.pimslims.presentation.plateExperiment.PlateNameFactory;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.util.File;
import org.pimslims.utils.orders.operon.PrimerOrderFormImpl;

public class OrderPlate extends Plate {

    /**
     * 
     */
    private static final String FORWARD_URL = "/JSP/plateExperiment/OrderPlate.jsp";

    public OrderPlate() {
        super();
        this.setForwardURL(OrderPlate.FORWARD_URL);
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a plate, for primer ordering";
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        /*
        System.out.println("org.pimslims.servlet.plateExperiment.OrderPlate.doPost");
        for (final Iterator iter = request.getParameterMap().entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String key = (String) entry.getKey();
            final String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                System.out.println("Parameter [" + key + "," + values[i] + "]");
            }
        }
        */

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }

        final String hook = request.getParameter("inputPlate");
        if (null == hook) {
            throw new ServletException("No Primer Order to process");
        }

        try {
            final ModelObject object = version.get(hook);
            Holder holder;
            if (object instanceof Holder) {
                holder = (Holder) object;
            } else {
                assert object instanceof ExperimentGroup;
                holder = HolderFactory.getPlate((ExperimentGroup) object);
            }

            //create the primer order
            final PrimerOrderFormImpl of = new PrimerOrderFormImpl();
            of.loadFromOrderExperiment(version, hook);
            final File file = of.saveOrderForm(version.get(hook), version);

            //create the primer plates
            OrderPlate.createPrimerPlates(version, holder);

            final String requestString =
                new String(request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath() + "/ViewFile/" + file.getHook()
                    + "/" + file.getName());
            System.out.println("org.pimslims.servlet.order.OrderPrimers request [" + requestString + "]");
            response.sendRedirect(requestString);
            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final AccessException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final ConstraintException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            if (version != null) {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }
        }
    }

    /**
     * OrderPlate.getOrder
     * 
     * @see org.pimslims.servlet.plateExperiment.Plate#getOrder()
     */
    @Override
    protected Comparator getOrder() {
        return new WellExperimentBean.RowOrder();
    }

    /**
     * 
     * OrderPlate.createPlate
     * 
     * @param version
     * @param holder
     * @throws ConstraintException
     * @throws AccessException
     */
    public static void createPrimerPlates(final WritableVersion version, final Holder holder)
        throws AccessException, ConstraintException {

        Protocol protocol;

        protocol = OrderPlate.getForwardPrimerPlateProtocol(version);
        assert null != protocol : "Protocol not found for Forward Primer plate";
        OrderPlate.createPlate(version, protocol, holder);

        protocol = OrderPlate.getReversePrimerPlateProtocol(version);
        assert null != protocol : "Protocol not found for Reverse Primer plate";
        OrderPlate.createPlate(version, protocol, holder);
    }

    /**
     * // OrderPlate.createPlate
     * 
     * @param protocol
     * @param hook
     */
    static ExperimentGroup createPlate(final WritableVersion version, final Protocol protocol,
        final Holder holder) throws AccessException, ConstraintException {

        final PlateNameFactory pnf = PropertyGetter.getInstance("Plate.Name.Factory", DefaultPlateName.class);
        final String plateName = pnf.suggestPlateName(version, holder, protocol);

        final Holder plate =
            HolderFactory.createHolder(version, null, plateName, (HolderType) holder.getHolderType(),
                java.util.Collections.EMPTY_MAP);

        if (null == protocol) {
            throw new AssertionError("The protocol is not valid");
        }
        final Collection<ExperimentType> experimentTypes =
            Collections.singleton(protocol.getExperimentType());

        final Collection<RefOutputSample> oss = protocol.getRefOutputSamples();
        if (1 != oss.size()) {
            throw new AssertionError(
                "The protocol for a plate experiment must have exactly one output sample");
        }

        final Collection<RefInputSample> iss = protocol.getRefInputSamples();
        if (1 != iss.size()) {
            throw new AssertionError("The protocol for a plate experiment must have exactly one input sample");
        }
        final RefInputSample refInputSample = iss.iterator().next();

        final SampleCategory output = oss.iterator().next().getSampleCategory();
        if (null == output) {
            throw new AssertionError(
                "The protocol's output sample for a plate experiment must have a Sample Category");
        }

        // create the experiment group
        final Collection<String> wells = CreatePlate.getWellsFromInputPlate(holder);

        final ExperimentGroup group =
            HolderFactory.createPlateExperiment(version, null, plate, plateName, "plate experiment",
                experimentTypes, Calendar.getInstance(), Calendar.getInstance(), null, protocol,
                Collections.EMPTY_MAP, wells);

        HolderFactory.multiLinePipette(version, holder, group, refInputSample.getName(), null);
        return group;
    }

    private static Protocol getForwardPrimerPlateProtocol(final WritableVersion version) {
        final Protocol ret =
            version.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.PIMS_FORWARD_PRIMERS);
        return ret;
    }

    private static Protocol getReversePrimerPlateProtocol(final WritableVersion version) {
        final Protocol ret =
            version.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.PIMS_REVERSE_PRIMERS);
        return ret;
    }
}
