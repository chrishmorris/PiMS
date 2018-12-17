/**
 * org.pimslims.generatedApi.pojo ChemElementTester.java
 * 
 * @date 22-Sep-2006 10:48:00
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.dao;

import java.util.Collection;
import java.util.HashSet;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.IDGenerator;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;

public class IDGeneratorTester extends org.pimslims.test.AbstractTestCase {

    public void testGetNextID() {
        Long start = null;
        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            start = IDGenerator.getNextID(wv);
            //id should be continue
            for (Long i = start + 1; i <= start + 500; i++)
                assertEquals(i, IDGenerator.getNextID(wv));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        //id should be continue even version aborted
        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            for (Long i = start + 501; i <= start + 1001; i++)
                assertEquals(i, IDGenerator.getNextID(wv));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testGetNoCommit() throws AccessException, ConstraintException {
        Long start = null;
        String sampleName = "testCommit" + System.currentTimeMillis();
        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            create(Sample.class, AbstractSample.PROP_NAME, sampleName);
            start = IDGenerator.getNextID(wv);
            //id should be continue
            for (Long i = start + 1; i <= start + 500; i++)
                IDGenerator.getNextID(wv);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        //previous sample should not committed
        //should not found a sample in that name
        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            Sample sample = wv.findFirst(Sample.class, AbstractSample.PROP_NAME, sampleName);
            assertNull(sample);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            create(Sample.class, AbstractSample.PROP_NAME, sampleName);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        //should not found a sample in that name
        try {
            wv = model.getWritableVersion(AbstractModel.SUPERUSER);
            Sample sample = wv.findFirst(Sample.class, AbstractSample.PROP_NAME, sampleName);
            assertNull(sample);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void _testConcurrentGet() throws InterruptedException {
        int numberOfThread = 10;
        Collection<IDThread> threads = new HashSet<IDThread>();
        for (int i = 0; i < numberOfThread; i++) {
            IDThread expCreaterThread = new IDThread(i);
            threads.add(expCreaterThread);
            expCreaterThread.start();

        }
        for (IDThread expCreaterThread : threads) {
            expCreaterThread.join();// Wait for thread to end.
        }
    }

}

class IDThread extends Thread {

    protected int threadNumber;

    protected String expTypeHook;

    public IDThread(int i) {
        threadNumber = i;
    }

    @Override
    public void run() {
        System.out.println("started thread " + threadNumber);
        AbstractModel model = ModelImpl.getModel();

        WritableVersion version = model.getWritableVersion("administrator");
        try {
            for (int i = 0; i < 10; i++) {
                Long id = IDGenerator.getNextID(version);
                System.out.println("Thread" + threadNumber + " got ID=" + id);
                sleep(1000);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted())
                version.abort();

        }
    }
}
