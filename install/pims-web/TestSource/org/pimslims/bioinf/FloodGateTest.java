package org.pimslims.bioinf;

import java.util.Random;

import junit.framework.TestCase;

/**
 * Test cases for a class that schedules web service requests to avoid flooding the EBI server
 * 
 * @author cm65
 * 
 */
public class FloodGateTest extends TestCase {

    public FloodGateTest(String methodname) {
        super(methodname);
    }

    public static class TestNoJobsCallback implements FloodGate.AllDone {

        public TestNoJobsCallback() {
            super();
        }

        public void setFloodGate(FloodGate floodGate) {
            // no action needed
        }

        public void onAllDone() {
            // no action needed
        }

        public void onFailed(Throwable e) {
            e.printStackTrace();
        }

    }

    public void testNoJobs() throws InterruptedException {
        TestNoJobsCallback callBack = new TestNoJobsCallback();
        FloodGate floodGate = new FloodGate(15, callBack);
        callBack.setFloodGate(floodGate);
        floodGate.close();
        assertEquals(0, floodGate.getRunning());
        floodGate.waitUntilAllDone();
    }

    public static class OneJobCallback implements FloodGate.AllDone {

        private boolean done = false;

        public void setDone(boolean done) {
            this.done = done;
        }

        public void onAllDone() {
            assert this.done;
        }

        public void onFailed(Throwable e) {
            e.printStackTrace();
        }

    }

    public static class TestOneJob implements Runnable {

        private final OneJobCallback callback;

        public TestOneJob(OneJobCallback callback) {
            this.callback = callback;
        }

        public void run() {
            // show it is done
            this.callback.setDone(true);
        }

    }

    public void testOneJob() throws InterruptedException {
        OneJobCallback callback = new OneJobCallback();
        FloodGate floodGate = new FloodGate(15, callback);
        TestOneJob job = new TestOneJob(callback);
        floodGate.addJob(job);
        floodGate.close();
        floodGate.waitUntilAllDone();
        assertEquals(0, floodGate.getRunning());
    }

    /**
     * For testing, checks that the jobs finished within a specified time interval
     * 
     * @author cm65
     * 
     */
    public static class IntervalCallback implements FloodGate.AllDone {

        private final long start;

        private final long end;

        public IntervalCallback(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public void onAllDone() {
            long now = System.currentTimeMillis();
            assert now >= this.start;
            assert now < this.end;
        }

        public void onFailed(Throwable e) {
            System.out.println("failed");
            throw new RuntimeException(e);
        }

    }

    public static class DelayJob implements Runnable {

        /**
         * milliseconds to wait
         */
        private final long delay;

        public DelayJob(long delay) {
            this.delay = delay;
        }

        public void run() {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                // no action needed;
            }
            // System.out.println("done it: "+this.getClass().getName());
        }

    }

    public void testTogether() throws InterruptedException {
        final long start = System.currentTimeMillis();
        final FloodGate floodGate = new FloodGate(2, new IntervalCallback(start + 1000L, start + 2000L));
        floodGate.addJob(new DelayJob(1000L));
        floodGate.addJob(new DelayJob(1000L));
        assertEquals(2, floodGate.getRunning());
        floodGate.close();
        floodGate.waitUntilAllDone();
        assertEquals(0, floodGate.getRunning());
    }

    public void testOneAtATime() throws InterruptedException {
        final long start = System.currentTimeMillis();
        final FloodGate floodGate = new FloodGate(1, new IntervalCallback(start + 2000L, start + 2100L));
        floodGate.addJob(new DelayJob(1000L));
        floodGate.addJob(new DelayJob(1000L));
        assertEquals(1, floodGate.getRunning());
        floodGate.close();
        floodGate.waitUntilAllDone();
        assertEquals(0, floodGate.getRunning());
    }

    /**
     * Pause for up to a second. Used in the example code instead of doing real work
     */
    static void delay() {
        float random = new Random().nextFloat(); // 0.0..1.0
        long delay = (long) (1000f * random); // 0..1000
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // no action needed
        }
    }

    public void testExample() {
        long start = System.currentTimeMillis();
        FloodGate.AllDone callback = new FloodGate.AllDone() {
            public void onAllDone() {
                System.out.println("flood gate example all done");
            }

            public void onFailed(Throwable e) {
                e.printStackTrace();
            }
        };

        final FloodGate floodGate = new FloodGate(15, callback);
        for (int i = 0; i < 30; i++) {
            Runnable job = new Runnable() {
                public void run() {
                    delay(); // your code here, this is just an example
                }
            };
            floodGate.addJob(job);
        }
        assertEquals(15, floodGate.getRunning());
        floodGate.close();
        try {
            floodGate.waitUntilAllDone();
            assertEquals(0, floodGate.getRunning());
            assertTrue(System.currentTimeMillis() < start + 2000);
        } catch (InterruptedException e1) {
            // interruption, e.g. application is closing down;
            // pending jobs will not be run
        }
    }

}
