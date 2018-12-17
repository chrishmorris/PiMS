package org.pimslims.presentation.concurrent;

import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.experiment.ExperimentCreator;
import org.pimslims.presentation.experiment.ExperimentCreator.ExperimentCreationForm;
import org.pimslims.test.AbstractTestCase;

public class ConcurrentExpCreateTest extends AbstractTestCase {

    public ConcurrentExpCreateTest(final String arg0) {
        super(arg0);
    }

    public void testMultiThreadSessionSave() throws AbortedException, InterruptedException {

        String protocolHook = null;
        String typeHook = null;
        final WritableVersion version0 = AbstractTestCase.model.getWritableVersion("demo");
        try {
            ExperimentType type = version0.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "PCR");
            if (type == null) {
                type = new ExperimentType(version0, "test1" + System.currentTimeMillis());
                // System.out.println("experiment type created!");
            }
            typeHook = type.get_Hook();

            Protocol protocol = version0.findFirst(Protocol.class, Protocol.PROP_NAME, "PiMS PCR");
            if (protocol == null) {
                protocol = new Protocol(version0, "test1" + System.currentTimeMillis(), type);
                // System.out.println("protocol created!");
            }
            protocolHook = protocol.get_Hook();
            version0.commit();
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (!version0.isCompleted()) {
                version0.abort();
            }

        }

        final int numberOfThread = 4;
        final Collection<ExperimentCreaterThread> threads = new HashSet<ExperimentCreaterThread>();
        for (int i = 0; i < numberOfThread; i++) {
            final ExperimentCreaterThread expCreaterThread =
                new ExperimentCreaterThread(protocolHook, typeHook);
            threads.add(expCreaterThread);
            expCreaterThread.start();

        }
        for (final ExperimentCreaterThread expCreaterThread : threads) {
            expCreaterThread.join();// Wait for thread to end.
        }
        // System.out.println( "All Threads Stopped" );

    }
}

class ExperimentCreaterThread extends Thread {

    protected String protocolHook;

    protected String expTypeHook;

    public ExperimentCreaterThread(final String protocolHook, final String expTypeHook) {
        this.expTypeHook = expTypeHook;
        this.protocolHook = protocolHook;
    }

    @Override
    public void run() {
        System.out.println("A thread started!");
        final AbstractModel model = ModelImpl.getModel();
        final ExperimentCreationForm form = new ExperimentCreationForm();

        final WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            form.setExperimentTypeHook(this.expTypeHook);
            form.setProtocolHook(this.protocolHook);
            final ExperimentCreator creator = new ExperimentCreator(form, version);

            final Experiment exp = creator.save();
            version.save(exp.getOutputSamples().iterator().next());
            // System.out.println("Experiment created:
            // "+exp.getName()+"<"+exp.get_Hook()+">");

            version.commit();

        } catch (final AccessException e) {
            // System.out.println("Failed to create experiment!");
            e.printStackTrace();
        } catch (final ConstraintException e) {
            System.out.println("Failed to create experiment!");
            e.printStackTrace();
        } catch (final AbortedException e) {
            System.out.println("Failed to create experiment!");
            e.printStackTrace();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }

        }
    }
}
