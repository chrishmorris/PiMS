/**
 * current-pims-web org.pimslims.command.leeds LocationManager.java
 * 
 * @author pvt43
 * @date 1 May 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 pvt43 *
 * 
 * 
 */
package org.pimslims.presentation.leeds;

import java.util.LinkedList;

/**
 * LocationManager
 * 
 * @author Petr Troshin aka pvt43
 */
public class LocationManager {

    LinkedList<Location> locations;

    class Location {
        String position;

        String positionBarcode;

        String boxBarcode;

        String box;

        String tower;

        String name;
    }
}
