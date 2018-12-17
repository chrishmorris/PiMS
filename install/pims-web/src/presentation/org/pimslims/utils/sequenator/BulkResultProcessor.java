/**
 * current-pims-web org.pimslims.utils.sequenator BulkResultProcessor.java
 * 
 * @author pvt43
 * @date 11 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;

/**
 * BulkResultProcessor
 * 
 */
@Deprecated
public class BulkResultProcessor {

    final AbstractModel model;

    final ReadableVersion rv;

    /**
     * Constructor for BulkResultProcessor
     */
    public BulkResultProcessor() {
        this.model = ModelImpl.getModel();
        // TODO change access level ?
        this.rv = this.model.getReadableVersion(Access.ADMINISTRATOR);
    }

    public Set<String> getResultFileNameList() {

        final Set<ResultsProcessor.NamesHolder> fileNames = new HashSet<ResultsProcessor.NamesHolder>();

        final List<ExperimentGroup> requestedSorders = SOrdersManager.getSSetupDoneSOrders(this.rv);

        for (final ExperimentGroup eg : requestedSorders) {
            for (final Experiment exp : eg.getExperiments()) {
                fileNames.add(new ResultsProcessor.NamesHolder(exp));
            }
        }

        return ResultsProcessor.getExpectedFileNames(fileNames);
    }

}
