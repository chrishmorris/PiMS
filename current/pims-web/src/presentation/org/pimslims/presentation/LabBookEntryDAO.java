/**
 * pims-web org.pimslims.presentation LabBookEntryDAO.java
 * 
 * @author Marc Savitsky
 * @date 8 Sep 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.search.Conditions;

/**
 * LabBookEntryDAO
 * 
 */
public class LabBookEntryDAO {

    public static Collection<LabBookEntry> getLabBookEntryList(final ReadableVersion version,
        final Map criteria) {
        assert criteria != null;
/*
        for (final Iterator it = criteria.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            //final Object value = e.getValue();
            //final StringBuffer s = new StringBuffer();
            //System.out.println("LabBookEntryDAO.getLabBookEntryList criteria [" + e.getKey() + ":"
            //    + value.getClass().getSimpleName() + "," + value.toString() + "]");
        } */

        final Collection<LabBookEntry> labBookEntries = version.findAll(LabBookEntry.class, criteria);

        return labBookEntries;
    }

    public static Collection<LabBookEntry> findLabBookEntries(final Calendar day, final Calendar nextDay,
        final ReadableVersion version, final User user) {
        final Map<String, Object> criteria = new HashMap<String, Object>();
    
        criteria.put(LabBookEntry.PROP_CREATIONDATE, Conditions.between(day, nextDay));
        if (null != user) {
            criteria.put(LabBookEntry.PROP_CREATOR, user);
        }
        final Collection<LabBookEntry> labBookEntries =
            getLabBookEntryList(version, criteria);
    
        criteria.clear();
        criteria.put(LabBookEntry.PROP_LASTEDITEDDATE, Conditions.between(day, nextDay));
        if (null != user) {
            criteria.put(LabBookEntry.PROP_LASTEDITOR, user);
        }
        labBookEntries.addAll(getLabBookEntryList(version, criteria));
        return labBookEntries;
    }

}
