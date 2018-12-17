/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import org.pimslims.business.BaseService;

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
public interface StatisticsService extends BaseService {

    /**
     * Gets the count of the number of images stored in the database.
     * @return the count of images.
     */
    int getImageCount();

    /**
     * Gets the number of active users in the system.
     * @return the number of active users.
     */
    int getUserCount();

    /**
     * Gets the number of trial plates that have been set up.
     * @return the number of trial plate.
     */
    int getPlateCount();
}
