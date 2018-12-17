/*
 * Created on 07-Mar-2007 15:49:03 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2007 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.lab;

import java.util.Iterator;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

@Deprecated
// not used
public class RobotWorkListFactory {

    public static String exportWorkList(final Holder holder) {
        Set<ResearchObjectiveElement> blueprintComponents = null;
        ResearchObjective expBlueprint = null;

        final Set<Sample> samples = holder.getSamples();
        final Iterator i = samples.iterator();

        String outputStr = "PiMSJobID, WellID, ConstructName, ConstructNumber\n";

        while (i.hasNext()) {
            final Sample sample = (Sample) i.next();
            if (null != sample.getOutputSample()) {
                if (null != sample.getOutputSample().getExperiment()) {
                    final Experiment experiment = sample.getOutputSample().getExperiment();
                    if ((null != experiment.getProject())
                        && (null != experiment.getExperimentGroup())) {
                        expBlueprint = sample.getOutputSample().getExperiment().getResearchObjective();
                        blueprintComponents = expBlueprint.getResearchObjectiveElements();
                        if (null != blueprintComponents) {
                            if (1 == blueprintComponents.size()) {
                                outputStr =
                                    outputStr + holder.getDbId() + ", "
                                        + HolderFactory.getPositionInHolder(sample) + ", "
                                        + expBlueprint.getCommonName() + ", " + expBlueprint.getDbId() + "\n";
                            }
                        }
                    }
                }
            }
        }
        return outputStr;
    }

    public static void main(final String[] args) {

        final AbstractModel model = ModelImpl.getModel();
        final ReadableVersion rv = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        final Holder testHolder = rv.findFirst(Holder.class, "dbId", "58086");
        System.out.println("Holder dbid: " + testHolder.getDbId());

        System.out.println(RobotWorkListFactory.exportWorkList(testHolder));

        System.out.println("RobotWorkListFactory test finished");
    }

}
