/*
 * Created on 1 November 2007 for starting uop automated blast searches
 * @author: Peter Troshin
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.pimslims.bioinf.FloodGate;
import org.pimslims.dao.AbstractModel;
import org.pimslims.lab.Util;
import org.pimslims.presentation.bioinf.BlastMultiple;

/**
 * TODO Make periodic start from the end of previuos run. TODO Add static runs counter Servlet context
 * listener. Start automated blast search using values defined in a Properties.
 * 
 */
public class AutomatedBlastListener implements javax.servlet.ServletContextListener {

    AbstractModel amodel = null;

    static FloodGate floodGate = null;

    public AutomatedBlastListener() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        event.getServletContext().log("Initialising: " + this.getClass().getName());
        try {
            Class.forName("javax.naming.Context");
        } catch (final ClassNotFoundException e) {
            e.printStackTrace(); // no way to report errors
        }

        // open the database connection
        final ServletContext cx = event.getServletContext();
        final HashMap<String, String> propMap = new HashMap<String, String>();
        InputStream in_stream = null;
        try {
            final Properties props = new Properties();
            in_stream = event.getServletContext().getResourceAsStream("/WEB-INF/conf/Properties");
            assert null != in_stream : "Resource not found: /WEB-INF/conf/Properties";
            //TODO must report to user if not found
            props.load(in_stream);
            final Enumeration propKeys = props.keys();
            if (!props.elements().hasMoreElements()) {
                event.getServletContext().log("No properties in Properties");
                throw new Throwable("Fail to initialize parameters from Properties");
            }
            while (propKeys.hasMoreElements()) {
                final String name = propKeys.nextElement().toString();
                final String value = props.getProperty(name);
                if (name == null || value == null) {
                    continue;
                }
                propMap.put(name, value);
                event.getServletContext().log(
                    "Parameter '" + name + "' = '" + value + "' was initialized successfully");
            }
        } catch (final IOException ioe) {
            event.getServletContext().log("FAIL to initialize parameters from Properties", ioe);
            // CHECKSTYLE:OFF
        } catch (final Throwable te) {
            // CHECKSTYLE:ON
            te.printStackTrace();
            event.getServletContext().log("FAIL to initialize parameters from Properties", te);
        } finally {
            if (in_stream != null) {
                try {
                    in_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
        }

        try {

            if (!this.isEnabled(propMap)) {
                cx.log("Automated blast searches disabled");
                return;
            }
            if (!this.searchPDB(propMap) && !this.searchTargetDB(propMap)) {
                cx.log("No database or incorrect database are specified for blast searches ");
                this.getOnFailureMessage();
                return;
            }
            if (this.getPeriodicity(propMap) == 0) {
                cx.log("No automated searches setup, pereodicity is incorrect ");
                this.getOnFailureMessage();
                return;
            }
            if (this.getStartAt(propMap) == null) {
                cx.log("StartAt date is either is not set or incorrect and will not be used ");
            }
            final String contactEmail = propMap.get("contactEmail");
            if (Util.isEmpty(contactEmail) && !contactEmail.contains("@") && !contactEmail.contains(".")) {
                cx.log("No or incorrect email address for contacts has been provided: " + contactEmail);
                this.getOnFailureMessage();
                return;
            }

            // amodel = ModelImpl.getModel(properties);
            // We may want to get separate connection for this task in the future
            this.amodel = (AbstractModel) event.getServletContext().getAttribute("model");

            cx.log("PIMS automated Blast context initialized");
            // CHECKSTYLE:OFF
        } catch (final Throwable ex2) {
            // CHECKSTYLE:ON
            /*
             * It is very unusual to catch Throwable. However, Tomcat give very poor error information
             * if this method throws an exception, so it seems to be necessary here.
             */
            cx.log("PIMS automated Blast is not initialized from properties file!\nProperties file was:"
                + "/WEB-INF/conf/Properties", ex2);
        }
        if (propMap.get("http.proxyHost") != null && propMap.get("http.proxyPort") != null) {
            System.setProperty("http.proxyHost", propMap.get("http.proxyHost"));
            System.setProperty("http.proxyPort", propMap.get("http.proxyPort"));
        }

        final FloodGate.AllDone callback = new FloodGate.AllDone() {
            public void onAllDone() {
                cx.log("Periodic BLAST Search has finished successfully at " + new Date());
            }

            public void onFailed(final Throwable e) {
                cx.log("Periodic BLAST Search has FAILED at " + new Date());
                e.printStackTrace();
            }
        };
        // Just to make it final 

        AutomatedBlastListener.floodGate = new FloodGate(15, callback);

        if (this.searchTargetDB(propMap)) {
            final Thread periodicBlastTargetDB =
                new PeriodicBlast(cx, propMap, "TargetDB", this.getStartAt(propMap));
            periodicBlastTargetDB.setDaemon(true);
            //TODO note this will not work in Tomcat7
            periodicBlastTargetDB.setName("periodicBlastTargetDB" + cx.getRealPath("/"));
            periodicBlastTargetDB.start();
        }

        if (this.searchPDB(propMap)) {
            final Thread periodicBlastPDB = new PeriodicBlast(cx, propMap, "PDB", this.getStartAt(propMap));
            periodicBlastPDB.setDaemon(true);
            periodicBlastPDB.setName("periodicBlastPDB" + cx.getRealPath("/"));
            periodicBlastPDB.start();
        }

        cx.log("PIMS automated Blast initialised correctly");
    }

    boolean isEnabled(final HashMap<String, String> prop) {
        final String isEnabled = prop.get("enablePeriodicBlastSearches");
        return this.getAnswer(isEnabled);
    }

    boolean searchTargetDB(final HashMap<String, String> prop) {
        final String targetDB = prop.get("BlastAgainstTargetDB");
        return this.getAnswer(targetDB);
    }

    boolean searchPDB(final HashMap<String, String> prop) {
        final String PDB = prop.get("BlastAgainstPDB");
        return this.getAnswer(PDB);
    }

    Date getStartAt(final HashMap<String, String> prop) {
        final String startAt = prop.get("startAt");
        final SimpleDateFormat sdf = new SimpleDateFormat("H-m/dd/MM/yyyy");
        Date start = null;
        try {
            start = sdf.parse(startAt);
        } catch (final ParseException e) {
            System.out.println("Cannot parse startAt date " + startAt);
            System.out.println("Periodic BLAST search will not use startAt date");
            return start;
        }
        final Date now = new Date();
        if (start.before(now) || start.after(new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7))) {
            System.out
                .println("StartAt date cannot be more than a week ahead and cannot be in the past. It will not be used ");
            start = null;
        }
        return start;
    }

    boolean getAnswer(final String answer) {
        if (answer != null && answer.trim().equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }

    /**
     * return hours in milliseconds Incorrect value indictated by 0;
     * 
     * @param ip
     * @return
     */
    long getPeriodicity(final HashMap<String, String> prop) {
        final String periodicity = prop.get("periodicity");
        int hours = 0;
        try {
            hours = Integer.parseInt(periodicity);
        } catch (final NumberFormatException e) {
            System.out.println("Incorrect value of periodicity for automatedBlast search: " + periodicity
                + " no search was setup");
            return 0;
        }
        if (hours < 5 || hours > 4320) {
            System.out.println("Incorrect value of periodicity for automatedBlast search: " + periodicity
                + " the value must be wholenumber and in range 5-4320");
            return 0;
        }
        return hours * 3600 * 1000;
    }

    void getOnFailureMessage() {
        System.out.println("NO AUTOMATED BLAST SEARCHES HAS BEEN SETUP");
    }

    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(final ServletContextEvent event) {
        this.amodel = null;
        AutomatedBlastListener.floodGate = null;
    }

    //Better implementation should allow to monitor status of a job
    class PeriodicBlast extends Thread {

        ServletContext cx = null;

        HashMap<String, String> propMap = null;

        String database;

        Date startAt;

        Date lastRunTime;

        PeriodicBlast(final ServletContext cx, final HashMap<String, String> propMap, final String database,
            final Date startAt) {
            this.cx = cx;
            this.propMap = propMap;
            this.database = database;
            this.startAt = startAt;
        }

        boolean isRunning() {
            return AutomatedBlastListener.floodGate.isRunning();
        }

        /*
         * Return number of minuties till the next run
         */
        Date getLastTimeRun() {
            return this.lastRunTime;
        }

        long getTimeFromLastRun() {
            if (this.lastRunTime != null) {
                return ((new Date()).getTime() - this.lastRunTime.getTime()) / (1000 * 60);
            }
            return -1;
        }

        // Return interval till next run in minuties
        int getRunInterval() {
            return new Long(AutomatedBlastListener.this.getPeriodicity(this.propMap) / (1000 * 60))
                .intValue();
        }

        /*
        * Return number of minuties till the next run
        */
        Date getNextTimeRun() {
            if (this.lastRunTime != null) {
                return new Date(this.lastRunTime.getTime()
                    + AutomatedBlastListener.this.getPeriodicity(this.propMap));
            }
            return this.startAt;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (this.startAt != null && this.startAt.after(new Date())) {
                        Thread.sleep(this.getStartAtSleepInterval());
                    }
                    this.setLastRunTime(new java.util.Date());
                    this.cx.log("Running automatic BLAST search against TargetDB. Starting at "
                        + (this.lastRunTime.toString()));

                    new BlastMultiple(AutomatedBlastListener.floodGate, AutomatedBlastListener.this.amodel,
                        this.database, this.propMap.get("contactEmail"));

                    Thread.sleep(AutomatedBlastListener.this.getPeriodicity(this.propMap));
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        long getStartAtSleepInterval() {
            return (this.startAt.getTime() - new Date().getTime());
        }

        /**
         * @param date
         */
        private void setLastRunTime(final Date date) {
            this.lastRunTime = date;
        }
    }

}
