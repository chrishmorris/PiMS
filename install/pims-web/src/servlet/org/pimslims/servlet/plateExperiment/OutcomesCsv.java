package org.pimslims.servlet.plateExperiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.lab.experiment.Cohort;
import org.pimslims.lab.experiment.WellBean;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.servlet.AbstractCsvServlet;

public class OutcomesCsv extends AbstractCsvServlet {

    /**
     * Show a report on a experiment group, in CSV format
     * 
     * @baseURL http://localhost:8080/.../read/OutcomesCsv/org.pimslims.model.experiment.ExperimentGroup
     *          :63105/outcomes.csv
     * 
     * @author Bill
     * 
     */
    public static class OutcomesData implements AbstractCsvData {

        private final Iterator<WellBean> iterator;

        private final List<String> preHeaders = new LinkedList<String>();
        {
            this.preHeaders.add("Plate");
            this.preHeaders.add("Well");
            this.preHeaders.add("Construct");
            // could this.preHeaders.add("Target");
        }

        private final List<List<String>> parameterNames = new ArrayList();

        private final Cohort cohort;

        public OutcomesData(final Cohort cohort) throws AccessException {
            // get exp prameter names
            for (int i = 0; i < cohort.getNumberOfStages(); i++) {
                final List<String> names = new ArrayList();
                final Protocol protocol = cohort.getProtocol(i);
                if (protocol != null) {
                    names.add(protocol.getName());
                    final Collection<ParameterDefinition> pds = protocol.getParameterDefinitions();
                    for (final ParameterDefinition pd : pds) {
                        names.add(pd.getName());
                    }
                }
                this.parameterNames.add(names);
            }
            this.iterator = cohort.getWells().iterator();
            this.cohort = cohort;
        }

        public String[] getHeaders() {
            final List<String> headers = new LinkedList<String>();
            headers.addAll(this.preHeaders);
            for (final Iterator iterator = this.parameterNames.iterator(); iterator.hasNext();) {
                final List<String> names = (List<String>) iterator.next();
                headers.addAll(names);
            }
            final String[] arrayHeaders = new String[headers.size()];
            headers.toArray(arrayHeaders);
            return arrayHeaders;
        }

        public String[] next() {
            final WellBean well = this.iterator.next();
            final List<String> columnValues = new LinkedList<String>();

            columnValues.add(well.getHolderName());
            columnValues.add(well.getWell());
            String construct = "";
            Experiment experiment = this.cohort.getExperiment(well, 0);
            if (null != experiment.getProject()) {
                construct = experiment.getProject().getName();
            }
            columnValues.add(construct);

            // find the experiments
            int stage = 0;
            for (final Iterator iterator = this.parameterNames.iterator(); iterator.hasNext();) {
                final List<String> names = (List<String>) iterator.next();
                experiment = this.cohort.getExperiment(well, stage);
                final Iterator iterator2 = names.iterator();
                iterator2.next(); // column header is protocol name
                if (null == experiment) {
                    columnValues.add("");
                } else {
                    columnValues.add(experiment.getStatus());
                }
                // remaining columns are parameter names
                for (; iterator2.hasNext();) {
                    final String name = (String) iterator2.next();
                    Parameter parm = null;
                    if (null != experiment) { // did this one make it to this stage?
                        parm = experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, name);
                    }
                    String value = "";
                    if (null != parm) {
                        value = parm.getValue();
                        if ("TRUE".equalsIgnoreCase(value)) {
                            value = "yes";
                        } else if ("FALSE".equalsIgnoreCase(value)) {
                            value = "no";
                        }
                    }
                    columnValues.add(value);
                }
                stage = stage + 1;
            }
            final String[] ret = new String[columnValues.size()];
            columnValues.toArray(ret);
            return ret;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

    }

    @Override
    protected AbstractCsvData getCsvData(final ReadableVersion version, final String expGrouphook, Map<String, String> parms)
        throws ServletException, AccessException {
        final ExperimentGroup group = version.get(expGrouphook);
        if (null == group) {
            throw new ServletException("No such experiment group: " + expGrouphook);
        }
        return new OutcomesData(new Cohort(group.getExperiments()));
    }

}
