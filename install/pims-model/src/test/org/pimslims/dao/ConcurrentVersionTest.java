package org.pimslims.dao;

import java.util.Collection;
import java.util.HashSet;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.test.AbstractTestCase;

public class ConcurrentVersionTest extends AbstractTestCase {
    static int numberofThread = 100;

    public ConcurrentVersionTest(String arg0) {
        super(arg0);
    }

    public void testMultiThreadVersion() throws InterruptedException {

        Collection<VersionThread> threads = new HashSet<VersionThread>();
        for (int i = 0; i < numberofThread; i++) {
            VersionThread thread = new VersionThread(model);
            threads.add(thread);

        }
        for (VersionThread thread : threads) {
            thread.start();// start threads
        }

        for (VersionThread thread : threads) {
            thread.join();// Wait for thread to end.
        }
        for (VersionThread thread : threads) {
            if (!thread.isSuccess)
                fail();
        }
        // System.out.println( "All Threads Stopped" );

    }
}

class VersionThread extends Thread {

    private final AbstractModel model;

    public boolean isSuccess = true;

    /**
     * @param model
     */
    public VersionThread(AbstractModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        // System.out.println("A thread started!");
        AbstractModel model = ModelImpl.getModel();

        ReadableVersion version = model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            for (int i = 0; i < 100; i++) {
                ReadableVersionImpl.getCurrentVersions();
                ReadableVersionImpl.getReadableVersionNumber();
                ReadableVersionImpl.getVersion(null);
            }
        } catch (java.util.ConcurrentModificationException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            if (!version.isCompleted())
                version.abort();

        }
    }
}
