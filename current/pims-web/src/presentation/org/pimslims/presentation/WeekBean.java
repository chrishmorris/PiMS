/**
 * pims-web org.pimslims.presentation WeekBean.java
 * 
 * @author bl67
 * @date 7 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;

/**
 * WeekBean
 * 
 */
public class WeekBean {
    //the day beans of this week
    private final Map<String, DayBean> dayBeans;

    private final Calendar monday;

    /**
     * Constructor for WeekBean it will create a weekBean which contains daybeans which in same week of 'day'
     */
    public WeekBean(final Calendar day, final ReadableVersion version, final String userName) {
        super();
        this.monday = this.getMonDay((Calendar) day.clone());
        this.dayBeans = this.getDayBeans((Calendar) this.monday.clone(), version, userName);
    }

    /**
     * WeekBean.getFirstDay
     * 
     * @param day
     * @return
     */
    private Calendar getMonDay(final Calendar day) {
        while (day.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            day.add(Calendar.DATE, -1);
        }
        return day;
    }

    public Map<String, DayBean> getDayBeans() {
        return this.dayBeans;
    }

    public DayBean getDayBean(final String day_of_week) {
        if (this.dayBeans.keySet().contains(day_of_week.toLowerCase())) {
            return this.dayBeans.get(day_of_week.toLowerCase());

        }
        return null;
    }

    /**
     * WeekBean.getDayBeans
     * 
     * @param day
     * @return
     */
    private Map<String, DayBean> getDayBeans(final Calendar day, final ReadableVersion version,
        final String userName) {
        final User user = version.findFirst(User.class, User.PROP_NAME, userName);
        final Calendar next7Day = (Calendar) day.clone();
        next7Day.add(Calendar.DATE, 7);

        final Collection<LabBookEntry> labBookEntries =
            LabBookEntryDAO.findLabBookEntries(day, next7Day, version, user);

        final Map<String, DayBean> beans = new HashMap<String, DayBean>();

        beans.put("monday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("tuesday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("wednesday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("thursday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("friday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("saturday", DayBean.resort(labBookEntries, day));
        day.add(Calendar.DATE, 1);
        beans.put("sunday", DayBean.resort(labBookEntries, day));

        return beans;
    }

    /**
     * WeekBean.getMonday
     * 
     * @return
     */
    public Calendar getMonday() {
        return this.monday;
    }
}
