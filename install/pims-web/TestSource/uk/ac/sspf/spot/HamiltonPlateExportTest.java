package uk.ac.sspf.spot;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;

@Deprecated
// this instrument never worked
public class HamiltonPlateExportTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(HamiltonPlateExportTest.class);
    }

    private AbstractModel model;

    public HamiltonPlateExportTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testHamiltonPlateList() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            // System.out.println("Here is a list of all holders containing
            // experiments based on the PIMS_Hamilton_PCR protocol");

            final Collection holders = version.getAll(Holder.class);

            final Iterator hI = holders.iterator();
            while (hI.hasNext()) {
                final Holder h = (Holder) hI.next();
                final Collection samples = h.getSamples();
                final Iterator ssI = samples.iterator();
                // System.out.println("Size of samples
                // collection:"+samples.size());
                Boolean isPlateForExport = false;
                while (ssI.hasNext()) {
                    final Sample s = (Sample) ssI.next();
                    final OutputSample os = s.getOutputSample();

                    if (null != os && null != os.getExperiment() && null != os.getExperiment().getProtocol()
                        && os.getExperiment().getProtocol().getName().equals("PiMS Hamilton PCR")

                    ) {
                        isPlateForExport = true;
                    }
                }
                if (isPlateForExport == true) {
                    System.out.println("Holder ID: " + h.get_Hook());
                }
            }
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
