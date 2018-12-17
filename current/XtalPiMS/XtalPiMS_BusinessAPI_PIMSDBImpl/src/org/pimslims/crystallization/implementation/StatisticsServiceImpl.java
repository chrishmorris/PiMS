/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.service.StatisticsService;

/**
 * This Service class provides functions for the headline page, so providing
 * the numbers that go into presentations, etc.
 * I am sure that we could come up with some particularly complex things to
 * count, but for the moment, the simple ones are defined.
 *
 * NOTE: Some of these should be calculated outside the web app (by a cron job
 * or the like) simply because they take too long to count properly...
 * For postgres, there are some cheats that will give you the approximate
 * row count (if you keep it reguarly vacuumed!)
 * @author ian
 */
public class StatisticsServiceImpl extends BaseServiceImpl implements StatisticsService {

    /**
     * constructor.
     * @param dataStorage the datastorage handler
     */
    public StatisticsServiceImpl(final DataStorage dataStorage) {
        super(dataStorage);
    }

    /**
     * Gets the count of the number of images stored in the database.
     * @return the count of images.
     */
    public int getImageCount() {
        return 0;
    }

    /**
     * Gets the number of active users in the system.
     * @return the number of active users.
     */
    public int getUserCount() {
        return 0;
    }

    /**
     * Gets the number of trial plates that have been set up.
     * @return the number of trial plate.
     */
    public int getPlateCount() {
        return 0;
    }


}
