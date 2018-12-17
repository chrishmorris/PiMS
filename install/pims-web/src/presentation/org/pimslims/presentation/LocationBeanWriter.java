/**
 * pims-web org.pimslims.presentation LocationBeanWriter.java
 * 
 * @author Marc Savitsky
 * @date 10 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;

/**
 * LocationBeanWriter
 * 
 */

import java.util.HashMap;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.location.Location;
import org.pimslims.presentation.sample.LocationBean;

@Deprecated
// Location is obsolete
public class LocationBeanWriter {

    public static Location createNewLocation(final WritableVersion version, final LocationBean lb)
        throws ConstraintException, AccessException {

        final LabNotebook access = (LabNotebook) version.get(lb.getAccess().getHook());

        final HashMap<String, Object> locationAttributes = new HashMap<String, Object>();
        locationAttributes.put(LabBookEntry.PROP_ACCESS, access);
        locationAttributes.put(Location.PROP_NAME, lb.getName());
        locationAttributes.put(Location.PROP_LOCATIONTYPE, lb.getType());
        locationAttributes.put(Location.PROP_TEMPERATURE, lb.getTemperature());
        locationAttributes.put(Location.PROP_TEMPDISPLAYUNIT, lb.getTemperatureDisplayUnit());
        locationAttributes.put(Location.PROP_PRESSURE, lb.getPressure());
        locationAttributes.put(Location.PROP_PRESSUREDISPLAYUNIT, lb.getPressureDisplayUnit());
        if (null != lb.getParentLocation()) {
            locationAttributes.put(Location.PROP_LOCATION, lb.getParentLocation());
        }
        final Location modelObject = version.create(Location.class, locationAttributes);

        // now show the new location
        return modelObject;

    }

}
