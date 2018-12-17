package org.pimslims.other;

import junit.framework.TestCase;

public class Test extends TestCase {

    private static final int LENGTH = 3;

    /**
     * invariant: 0..next are in use, next..LENGTH are free
     */
    private final Object[] resources;

    private int next = 0;

    /**
     * @param resources
     */
    public Test() {
        resources = new Object[LENGTH];
        for (int i = 0; i < resources.length; i++) {
            resources[i] = new Integer(i);
        }
    }

    public int available() {
        return LENGTH - next;
    }

    public synchronized Object acquire() throws InterruptedException {
        while (next == LENGTH) {
            wait();
        }
        Object ret = resources[next];
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        next = next + 1;
        notifyAll();
        return ret;
    }

    public synchronized void release(Object resource) throws InterruptedException {
        while (next == 0) {
            wait();
        }
        resources[--next] = resource;
        notifyAll();
    }

    public void test() {
        try {
            Object resource1 = this.acquire();
            assertNotNull(resource1);
            release(resource1);
            assertEquals(LENGTH, available());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    Object resource1 = null;

    Object resource2 = null;

    public void testSynchronized() {
        new Thread(runnable).start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        try {
            resource2 = acquire();
            assertNotNull(resource1);
            assertFalse("collision", resource1.equals(resource2));
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

    Throwable runException = null;

    private Runnable runnable = new Runnable() {
        public void run() {
            try {
                resource1 = Test.this.acquire();
            } catch (RuntimeException e) {
                runException = e;
            } catch (InterruptedException e) {
                runException = e;
            }
        }
    };

    public void testTooMany() {
        try {
            Object[] myResources = new Object[LENGTH];
            for (int i = 0; i < resources.length; i++) {
                myResources[i] = acquire();
            }
            new Thread(runnable).start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
            for (int i = 0; i < resources.length; i++) {
                release(myResources[i]);
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                fail(e.getMessage());
            }
            assertNotNull(resource1);
            if (null != runException) {
                fail(runException.getMessage());
            }
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

    public void testFinally() {
        boolean isTrue = false;
        try {
            System.out.println("started");
            if (isTrue != true)
                return;
        } catch (final RuntimeException e1) {
            // should not happen in read
            throw new RuntimeException(e1);
        } finally {
            System.out.println("finished");
        }

    }

}
