package org.pimslims.servlet.plateExperiment;

import java.util.Map;

import javax.servlet.ServletException;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.lab.ProtocolCsv;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderType;
import org.pimslims.servlet.AbstractCsvServlet;

/**
 * CreateExperimentGroupCsv
 * 
 * Make a CSV file suitable for creating an experiment group or plate experiment
 * 
 */
public class CreateExperimentGroupCsv extends AbstractCsvServlet {

    @Override
    protected AbstractCsvData getCsvData(final ReadableVersion version, final String protocolHook,
        final Map<String, String> parms) throws ServletException, AccessException {
        final Protocol protocol = version.get(protocolHook);
        if (null == protocol) {
            throw new ServletException("No such protocol: " + protocolHook);
        }
        int numExperiments = 1;
        if (null != parms.get("numExperiments")) {
            numExperiments = Integer.parseInt(parms.get("numExperiments"));
            return ProtocolCsv.getExperimentGroupCsv(protocol, parms.get("groupName"), numExperiments);
        }
        if (null != parms.get("holderType")) {
            final HolderType type = (HolderType) version.get(parms.get("holderType"));
            final Map<String, String> holders = CreatePlate.getHolderNames(parms, null, type);
            return ProtocolCsv.getPlateCsv(protocol, parms.get("groupName"), type, holders);
        }
        throw new ServletException("Either number of experiments or holder type must be supplied");
    }

}
