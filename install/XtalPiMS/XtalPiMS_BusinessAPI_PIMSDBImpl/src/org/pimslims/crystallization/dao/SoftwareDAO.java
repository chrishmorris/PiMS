package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.logging.Logger;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Software;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.Software}
 * </p>
 * 
 * @author Bill Lin
 */
public class SoftwareDAO {
    private static final Logger log = Logger.getLogger(SoftwareDAO.class);

    public static Software getpimsSoftware(final ReadableVersion version,
        final org.pimslims.business.crystallization.model.Software softwareAnnotator) {
        if (softwareAnnotator == null) {
            return null;
        }

        Software pSoftware = version.get(Software.class, softwareAnnotator.getId());
        if (pSoftware == null) {
            final Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put(Software.PROP_NAME, softwareAnnotator.getName());
            pSoftware = version.findFirst(Software.class, pMap);
        }

        return pSoftware;
    }
}
