package org.pimslims.servlet.plateExperiment;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.lab.PlateCsv;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.servlet.AbstractCsvServlet;

public class PlateExperimentCsv extends AbstractCsvServlet {

    /**
     * Show a report on a experiment group, in CSV format
     * 
     * @baseURL http://localhost:8080/.../read/PlateExperimentCsv/org.pimslims.model.experiment.ExperimentGroup
     *          :63105/expGroup.csv
     * 
     * @author Bill
     * 
     */
    public static class PlateExperimentData extends PlateCsv {

        PlateReader plateReader;

        private final boolean isPlateExperiment;

        public PlateExperimentData(final ExperimentGroup group) throws AccessException {
            super(HolderFactory.getProtocolFromExpGroup(group));

            // get experiment Beans
            this.plateReader = new PlateReader(group.get_Version(), group, null);
            final List<WellExperimentBean> experimentBeans = this.plateReader.getExperiments();
            this.iterator = experimentBeans.iterator();
            this.isPlateExperiment = HolderFactory.isPlateExperiment(group);
        }

        /**
         * PlateExperimentData.isPlateExperiment
         * 
         * @see org.pimslims.lab.PlateCsv#isPlateExperiment()
         */
        @Override
        protected boolean isPlateExperiment() {
            return this.isPlateExperiment;
        }

        public String[] next() {
            final WellExperimentBean expBean = (WellExperimentBean) this.iterator.next();
            final List<String> columnsValues = new LinkedList<String>();

            if (this.isPlateExperiment()) {
                // plateId (holder name)
                columnsValues.add(expBean.getPlate());
                // well position
                columnsValues.add(expBean.getRow() + expBean.getColumn());
            } else {
                columnsValues.add(expBean.getGroupName());
                columnsValues.add(this.experimentNumber(expBean.getName()));
            }

            // Target and Construct
            columnsValues.add(expBean.getTargetName());
            columnsValues.add(expBean.getConstructName());
            // parameters value
            for (final String paraName : this.parameterNames) {
                columnsValues.add(expBean.getParameters().get(paraName));
            }
            for (final String isName : this.inputSampleNames) {
                if (expBean.getInputSampleNames().containsKey(isName)) {
                    columnsValues.add(expBean.getInputSampleNames().get(isName));
                    if (this.inputSampleUnits.containsKey(isName)) {
                        columnsValues.add(expBean.getInputSampleVolumes().get(isName).toString());
                    }
                } else {
                    columnsValues.add("");
                    if (this.inputSampleUnits.containsKey(isName)) {
                        columnsValues.add("");
                    }
                }
            }
            final String[] arrayValues = new String[columnsValues.size()];
            columnsValues.toArray(arrayValues);

            return arrayValues;
        }
    }

    @Override
    protected AbstractCsvData getCsvData(final ReadableVersion version, final String expGrouphook,
        final Map<String, String> parms) throws ServletException, AccessException {
        final ExperimentGroup group = version.get(expGrouphook);
        if (null == group) {
            throw new ServletException("No such experiment group: " + expGrouphook);
        }
        return new PlateExperimentData(group);
    }

}
