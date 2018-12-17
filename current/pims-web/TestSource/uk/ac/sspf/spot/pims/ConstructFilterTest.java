/**
 * current-pims-web uk.ac.sspf.spot.pims ConstructFilterTest.java
 * 
 * @author cm65
 * @date 15 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package uk.ac.sspf.spot.pims;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

import uk.ac.sspf.spot.beans.ConstructFastaBean;
import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * ConstructFilterTest
 * 
 */
@Deprecated
// we are seeking to retire all the code this tests
public class ConstructFilterTest extends TestCase {

    private final AbstractModel model;

    /**
     * @param methodName
     */
    public ConstructFilterTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testProteinNameFilter() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final Target target2 =
                ConstructFastaTest.createTarget(version, "pnfTwo", "Failed", ConstructFastaTest.DATE);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ConstructFasta cf = new ConstructFasta(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setProteinName(target.getProtein().getName());
            cf.setFilter(filter);
            final Collection<ConstructFastaBean> progress = cf.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructFastaBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.SEQ_WWW, bean.getSequence());
            Assert.assertEquals(">" + ConstructFastaTest.CONSTRUCT_NAME + " - ", bean.getDescription());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testOrganismNameFilter() throws AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Target target = ConstructFastaTest.createTarget(version);
            final Organism species = new Organism(version, "species" + System.currentTimeMillis());
            target.setSpecies(species);
            final Target target2 =
                ConstructFastaTest.createTarget(version, "Two", "Failed", ConstructFastaTest.DATE);
            final Set<Target> targets = new HashSet<Target>();
            targets.add(target);
            targets.add(target2);

            final ConstructFasta cf = new ConstructFasta(version, null);
            final ConstructFilterBean filter = new ConstructFilterBean();
            filter.setOrganism(species.getName());
            cf.setFilter(filter);
            final Collection<ConstructFastaBean> progress = cf.getProgressList(targets, null);
            Assert.assertEquals(1, progress.size());
            final ConstructFastaBean bean = progress.iterator().next();
            Assert.assertEquals(ConstructFastaTest.SEQ_WWW, bean.getSequence());
            Assert.assertEquals(">" + ConstructFastaTest.CONSTRUCT_NAME + " - " + species.getName(), bean
                .getDescription());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
