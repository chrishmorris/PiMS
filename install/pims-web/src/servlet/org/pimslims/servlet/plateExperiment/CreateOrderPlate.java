/**
 * V3_1-pims-web org.pimslims.servlet.plateExperiment CreateOrderPlate.java
 * 
 * @author cm65
 * @date 3 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.FlushMode;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.servlet.PIMSServlet;

/**
 * CreateOrderPlate
 * 
 */
public class CreateOrderPlate extends PIMSServlet {

    public static final Comparator<LabBookEntry> CREATION_DATE_ORDER = new Comparator() {

        public int compare(final Object arg0, final Object arg1) {
            final LabBookEntry page0 = (LabBookEntry) arg0;
            final LabBookEntry page1 = (LabBookEntry) arg1;
            return page0.getDbId().compareTo(page1.getDbId());
        }
    };

    /**
     * CreateOrderPlate.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Makes a plate experiment with the construct layout";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("This servlet does not support GET requests");
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.plateExperiment.CreateOrderPlate.doPost()");

        final Map<String, String[]> parms = request.getParameterMap();
        final List<String> constructs = new ArrayList(parms.size());
        final String protocol = request.getParameter("protocol"); // may be null

        for (final Iterator iterator = parms.keySet().iterator(); iterator.hasNext();) {
            final String key = (String) iterator.next();
            System.out.println("org.pimslims.servlet.plateExperiment.CreateOrderPlate.doPost [" + key + ":"
                + parms.get(key)[0] + "]");

            if (PIMSServlet.LAB_NOTEBOOK_ID.equals(key)) {
                continue;
            }
            if (1 != (parms.get(key)).length) {
                System.out
                    .println("org.pimslims.servlet.plateExperiment.CreateOrderPlate.doPost [Invalid Parameter]");
                this.writeErrorHead(request, response, "Expected one value for: " + key,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if ("on".equals(parms.get(key)[0])) {
                // it is the hook of a construct, and it is checked
                if (!key.equals("checkAll")) {
                    constructs.add(key);
                }
                continue;
            }
            this.writeErrorHead(request, response, "Unexpected parameter: " + key + ":" + parms.get(key),
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        System.out.println("org.pimslims.servlet.plateExperiment.CreateOrderPlate.doPost(A)");
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            // to improve performance
            version.getSession().setFlushMode(FlushMode.COMMIT); //TODO version.setFlushModeCommit();

            final LabNotebook access =
                (LabNotebook) version.get(request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID));
            if (null == access) {
                throw new AssertionError("The project is not valid");
            }

            final ExperimentGroup group =
                CreateOrderPlate.createOrderPlate(version, access, protocol, constructs);
            version.commit();
            System.out.println("org.pimslims.servlet.plateExperiment.CreateOrderPlate.doPost["
                + request.getContextPath() + "/OrderPlate/" + group.get_Hook() + "]");
            PIMSServlet.redirectPost(response, request.getContextPath() + "/OrderPlate/" + group.get_Hook());
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * CreateOrderPlate.createOrderPlate
     * 
     * @param version
     * @param protocol
     * @param constructHooks
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExperimentGroup createOrderPlate(final WritableVersion version, final LabNotebook access,
        final String protocolHook, final List<String> constructHooks) throws AccessException,
        ConstraintException {

        Protocol protocol = null;
        if (null == protocolHook) {
            protocol = CreateOrderPlate.getOrderProtocol(version);
        } else {
            protocol = version.get(protocolHook);
        }
        assert null != protocol : "Could not find Order protocol";
        final Calendar now = Calendar.getInstance();
        final String name = "Order" + System.currentTimeMillis(); //TODO make a better one
        final Holder holder = new Holder(version, name, null);
        holder.setAccess(access);
        final AbstractHolderType holderType =
            version.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 well rd-bottom");
        holder.setHolderType(holderType);
        final List<String> wells = HolderFactory.POSITIONS_BY_COLUMN_96.subList(0, constructHooks.size());

        // make the plate layout
        final List<ResearchObjective> constructs = new ArrayList(constructHooks.size());
        for (final Iterator iterator = constructHooks.iterator(); iterator.hasNext();) {
            final String hook = (String) iterator.next();
            final ResearchObjective construct = version.get(hook);
            constructs.add(construct);
        }
        Collections.sort(constructs, CreateOrderPlate.CREATION_DATE_ORDER);
        final Map<String, ResearchObjective> constructMap =
            new HashMap<String, ResearchObjective>(constructHooks.size());
        final Iterator<String> wellIterator = wells.iterator();
        for (final Iterator constructsIt = constructs.iterator(); constructsIt.hasNext();) {
            final ResearchObjective construct = (ResearchObjective) constructsIt.next();
            constructMap.put(wellIterator.next(), construct);
        }
        final ExperimentGroup group =
            HolderFactory.createPlateExperimentWithPositions(version, access, holder, name, "Primer Order",
                Collections.singleton(protocol.getExperimentType()), now, now, "", protocol,
                Collections.EMPTY_MAP, wells, wells, constructMap);

        return group;
    }

    /**
     * CreateOrderPlate.getOrderProtocol
     * 
     * @param version
     * @return
     */
    private static Protocol getOrderProtocol(final WritableVersion version) {
        final Protocol ret =
            version.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.PIMS_ORDER);
        return ret;
    }

}
