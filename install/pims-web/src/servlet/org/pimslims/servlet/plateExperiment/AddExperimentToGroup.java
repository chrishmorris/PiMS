/**
 * V3_1-pims-web org.pimslims.servlet.plateExperiment AddExperimentToGroup.java
 * 
 * @author cm65
 * @date 27 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.servlet.PIMSServlet;

/**
 * AddExperimentToGroup
 * 
 */
public class AddExperimentToGroup extends PIMSServlet {

    /**
     * AddExperimentToGroup.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create a new experiment in an experiment group or plate experiment";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("This URL does not support GET");
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        PIMSServlet.validatePost(request);

        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {

            final ExperimentGroup group =
                (ExperimentGroup) this.getRequiredObject(version, request, response, request
                    .getParameter("group"));
            if (null == group) {
                return;
            }

            version.setDefaultOwner(group.getAccess());

            final Holder plate = HolderFactory.getPlate(group);
            final Protocol protocol = HolderFactory.getProtocolFromExpGroup(group);

            final Collection<ExperimentType> types = Collections.singleton(protocol.getExperimentType());
            final Map<String, Object> attributes =
                HolderFactory.getExperimentAttributes(version, types, "", protocol, Collections.EMPTY_MAP,
                    group);

            final String experimentNumber = this.nextExperiment(group);
            if (null == plate) {
                HolderFactory.createExperimentInGroup(version, protocol, group, attributes, experimentNumber,
                    experimentNumber, null);
            } else {
                final String position = request.getParameter("position");
                final ResearchObjective construct = version.get(request.getParameter("construct"));
                HolderFactory.createExperimentInPlate(version, plate, protocol, construct, group, attributes,
                    position);
            }
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    private String nextExperiment(final ExperimentGroup group) {

        int greatest = 0;
        for (final Experiment experiment : group.getExperiments()) {
            final String name = experiment.getName();
            final String number = name.substring(name.lastIndexOf(':') + 1);
            if (null == number) {
                continue;
            }
            final int thisNumber = Integer.parseInt(number);
            if (thisNumber > greatest) {
                greatest = thisNumber;
            }
        }

        return new DecimalFormat("00").format(greatest + 1);
    }

}
