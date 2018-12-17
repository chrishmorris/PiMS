package org.pimslims.bioinf;

import java.util.ArrayList;
import java.util.List;

/**
 * Schedule web service requests to avoid flooding the EBI server. Example of use:
 * 
 * FloodGate.AllDone callback = new FloodGate.AllDone() { public void onAllDone() { System.out.println("flood
 * gate example all done"); } public void onFailed(Exception e) { e.printStackTrace(); } };
 * 
 * final FloodGate floodGate = new FloodGate(15, callback); for (int i=0; i<30; i++) { Runnable job = new
 * Runnable() { public void run() { delay(); // your code here, this is just an example } };
 * floodGate.addJob(job); } floodGate.close();
 * 
 * @author cm65
 * 
 */
public class FloodGate {

    private static class Job implements Runnable {

        private final FloodGate floodGate;

        /**
         * the user's job
         */
        private final Runnable runnable;

        public Job(final FloodGate floodGate, Runnable runnable) {
            super();
            this.floodGate = floodGate;
            this.runnable = runnable;
        }

        public final void run() {
            try {
                runnable.run();
                this.floodGate.oneDone();
                // CHECKSTYLE:OFF
            } catch (Exception e) {
                // unusually, catching all exceptions is the right thing to do
                Throwable cause = e;
                while (null != cause.getCause()) {
                    cause = cause.getCause();
                }
                this.floodGate.oneFailed(cause);
            }
            // CHECKSTYLE:ON
        }
    }

    public interface AllDone {
        void onAllDone();

        void onFailed(Throwable cause);
    }

    private final AllDone callback;

    private int running = 0;

    private final int maxSimultaneousJobs;

    private final List<Job> toRun = new ArrayList<Job>();

    public FloodGate(int maxSimultaneousJobs, final AllDone callback) {
        super();
        this.maxSimultaneousJobs = maxSimultaneousJobs;
        this.callback = callback;
    }

    /**
     * Adds another task to the queue
     * 
     * @param job task to schedule
     */
    public synchronized void addJob(Runnable runnable) {
        Job job = new Job(this, runnable);
        if (this.running < this.maxSimultaneousJobs) {
            this.running = this.running + 1;
            new Thread(job).start();
        } else {
            this.toRun.add(job);
        }
    }

    /**
     * Indicate that no more jobs will be added
     */
    public synchronized boolean close() {
        return false;
    }

    /**
     * Returns after all jobs are done, or it is interrupted, e.g. because the application is closing down.
     * 
     * Useful for testing, may not be needed by live code.
     * 
     * @throws InterruptedException
     */
    public synchronized void waitUntilAllDone() throws InterruptedException {
        while (0 < this.running) {
            this.wait();
        }
    }

    synchronized boolean oneDone() {
        if (0 < this.toRun.size()) {
            Job next = this.toRun.remove(0);
            new Thread(next).start();
            return false;
        }
        this.running = this.running - 1;
        if (0 == this.running) {
            this.callback.onAllDone();
            this.notifyAll();
            return true;
        }
        return false;
    }

    synchronized void oneFailed(Throwable cause) {
        this.callback.onFailed(cause);
        oneDone();
    }

    /**
     * @return the number of active jobs
     */
    int getRunning() {
        return running;
    }

    /*
     * Indicate whether the task has been completed or not 
     */
    public boolean isRunning() {
        if (getRunning() > 0) {
            return true;
        }
        return false;
    }
}
