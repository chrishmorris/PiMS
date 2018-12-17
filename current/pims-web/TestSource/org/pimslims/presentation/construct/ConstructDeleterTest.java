/**
 * V3_1-pims-web org.pimslims.presentation.construct ConstructDeleterTest.java
 * 
 * @author cm65
 * @date 19 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;

/**
 * ConstructDeleterTest
 * 
 */
public class ConstructDeleterTest extends TestCase {

    private final AbstractModel model;

    public ConstructDeleterTest(final String method) {
        super(method);

        this.model = ModelImpl.getModel();
    }

    public void testGetConstructPartsRO() throws AbortedException, ConstraintException, AccessException,
        ServletException, ClassNotFoundException, SQLException {
        String hook = null;
        String proteinHook = null;
        String userHook = null;
        WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final LabNotebook owner = new LabNotebook(wv, "owner" + System.currentTimeMillis());
            wv.setDefaultOwner(owner.getName());

            final Molecule protein = new Molecule(wv, "protein", "o" + System.currentTimeMillis());
            proteinHook = protein.get_Hook();

            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME + "p", protein);
            final User user = new User(wv, ConstructBeanWriterTest.SCIENTIST);
            userHook = user.get_Hook();

            final ConstructBean cb = this.createBean(target, user);

            final ResearchObjective eb = ConstructBeanWriter.createNewConstruct(wv, cb);
            hook = eb.get_Hook();
            Assert.assertEquals(owner, eb.getAccess());
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ResearchObjective eb = wv.get(hook);
            final LabNotebook owner = eb.getAccess();

            final List<ModelObject> toDelete = ConstructBeanWriter.getConstructParts(eb);
            this.doTest(proteinHook, userHook, wv, owner, toDelete);
            wv.commit();

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testGetConstructPartsExp() throws AbortedException, ConstraintException, AccessException,
        ServletException, ClassNotFoundException, SQLException {
        String hook = null;
        String proteinHook = null;
        String userHook = null;
        WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final LabNotebook owner = new LabNotebook(wv, "owner" + System.currentTimeMillis());
            wv.setDefaultOwner(owner.getName());

            final Molecule protein = new Molecule(wv, "protein", "o" + System.currentTimeMillis());
            proteinHook = protein.get_Hook();

            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME + "pe", protein);
            final User user = new User(wv, ConstructBeanWriterTest.SCIENTIST);
            userHook = user.get_Hook();

            final ConstructBean cb = this.createBean(target, user);

            final ResearchObjective eb = ConstructBeanWriter.createNewConstruct(wv, cb);
            final Experiment exp = ExperimentUtility.getPrimerDesignExperiment(eb);
            hook = exp.get_Hook();
            Assert.assertEquals(owner, exp.getAccess());
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Experiment exp = wv.get(hook);
            final LabNotebook owner = exp.getAccess();

            final List<ModelObject> toDelete = ConstructBeanWriter.getConstructParts(exp);
            this.doTest(proteinHook, userHook, wv, owner, toDelete);
            wv.commit();

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    private ConstructBean createBean(final Target target, final User user) {
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence(ConstructBeanWriterTest.REV_PRIMER);
        reverse.setLengthOnGeneString(Integer.toString(ConstructBeanWriterTest.REV_OVERLAP.length()));
        reverse.setTag(ConstructBeanWriterTest.REVERSE_TAG);
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructBeanWriterTest.FWD_PRIMER);
        forward.setLengthOnGeneString(Integer.toString(ConstructBeanWriterTest.FWD_OVERLAP.length()));
        forward.setTag(ConstructBeanWriterTest.FORWARD_TAG);
        final ConstructBean cb = new ConstructBean(new TargetBean(target), forward, reverse);
        //cb.setTargetHook(target.get_Hook());
        cb.setConstructId(ConstructBeanWriterTest.CONSTRUCT_ID);
        //cb.setTargetName("SSO1439" + System.currentTimeMillis());
        cb.setDnaSeq(ConstructBeanWriterTest.DNA_SEQUENCE);
        cb.setProtSeq("KKK");
        cb.setDescription(ConstructBeanWriterTest.DESCRIPTION);
        cb.setComments(ConstructBeanWriterTest.COMMENTS);
        cb.setExpressedProt(ConstructBeanWriterTest.EXPRESSED_PROTEIN);
        cb.setFinalProt(ConstructBeanWriterTest.FINAL_PROTEIN);
        cb.setTargetProtStart(1);
        cb.setTargetProtEnd(244);
        cb.setUserHook(user.get_Hook());
        cb.setDateOfEntry(new Date());
        cb.setPcrProductSeq(ConstructBeanWriterTest.PCR_PRODUCT_SEQ);
        return cb;
    }

    private void doTest(final String proteinHook, final String userHook, final WritableVersion wv,
        final LabNotebook owner, final List<ModelObject> toDelete) throws AccessException,
        ConstraintException {
        Assert.assertEquals(12, toDelete.size());

        // now delete
        for (final Iterator iterator = toDelete.iterator(); iterator.hasNext();) {
            final ModelObject modelObject = (ModelObject) iterator.next();
            modelObject.delete();
        }

        // now test all is done
        wv.flush();
        final Collection<LabBookEntry> pages = owner.getLabBookEntries();
        Assert.assertEquals(2, pages.size()); // protein, target
        /* for (final Iterator iterator = pages.iterator(); iterator.hasNext();) {
            final LabBookEntry labBookEntry = (LabBookEntry) iterator.next();
            System.out.println(labBookEntry.get_Hook());
        } */

        // tidy up
        final Molecule protein = wv.get(proteinHook);
        wv.delete(protein.getProteinForTargets());
        protein.delete();
        final User user = wv.get(userHook);
        user.delete();
        owner.delete();
    }
}
