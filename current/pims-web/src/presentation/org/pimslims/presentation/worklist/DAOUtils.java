package org.pimslims.presentation.worklist;

import java.util.LinkedList;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.sample.Sample;

public class DAOUtils {
    static final String endDateTag = "endDate";

    /**
     * Convert a list of id to a list of modelobject, when a modelobject is not accessable(eg:deleted, access
     * rejected) it will be ignored.
     * 
     * @param rv
     * @param idList
     * @return
     */

    public static java.util.List<Sample> getSamplesFromIDList(final ReadableVersion rv,
        final List<Long> idList) {
        final List<Sample> modelObjects = new LinkedList<Sample>();

        for (final Long id : idList) {
            final Sample modelObject = rv.get(Sample.class, id);
            if (modelObject != null) {
                modelObjects.add(modelObject);
            }
        }
        return modelObjects;
    }

}
