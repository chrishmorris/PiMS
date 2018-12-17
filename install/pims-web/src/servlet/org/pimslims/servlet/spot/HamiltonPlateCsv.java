/*
 * Created on 08-Mar-2007 09:01:28 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2007 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.servlet.spot;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.servlet.AbstractCsvServlet;

@Deprecated
// this instrument never worked
public class HamiltonPlateCsv extends AbstractCsvServlet {

    /**
     * Show a work list for the Hamilton robot
     * 
     * @see org.pimslims.servlet.AbstractCsvServlet#getCsvData(org.pimslims.dao.ReadableVersion,
     *      java.lang.String)
     */

    public static class HamiltonPlateData implements AbstractCsvData {

        private final Iterator<Sample> iterator;

        private final String[] attributeNames = { "PiMSJobID", "WellID", "ConstructName", "ConstructNumber" };

        public HamiltonPlateData(final Holder holder) {
            this.iterator = holder.getSamples().iterator();
        }

        public String[] getHeaders() {
            return this.attributeNames;
        }

        public String[] next() {
            final String[] results = new String[this.attributeNames.length];
            final Sample sample = this.iterator.next();
            results[0] = sample.getHolder().getDbId().toString();
            results[1] = HolderFactory.getPositionInHolder(sample).toString();
            if (null != sample.getOutputSample()) {
                if (null != sample.getOutputSample().getExperiment()) {
                    final Experiment experiment = sample.getOutputSample().getExperiment();
                    if ((null != experiment.getProject())
                        && (null != experiment.getExperimentGroup())) {
                        final ResearchObjective expBlueprint =
                            sample.getOutputSample().getExperiment().getResearchObjective();
                        final Set<ResearchObjectiveElement> blueprintComponents =
                            expBlueprint.getResearchObjectiveElements();
                        if (null != blueprintComponents) {
                            if (1 == blueprintComponents.size()) {
                                results[2] = expBlueprint.getCommonName().toString();
                                results[3] = expBlueprint.getDbId().toString();
                            }
                        }
                    }
                }
            }
            return results;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

    }

    @Override
    protected AbstractCsvData getCsvData(final ReadableVersion version, final String hook,
        final Map<String, String> parms) throws ServletException {
        final Holder holder = version.get(hook);
        return new HamiltonPlateData(holder);
    }

}
