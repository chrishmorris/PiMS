/**
 * V2_3-pims-web org.pimslims.presentation NoteBeanTest.java
 * 
 * @author cm65
 * @date 3 Sep 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Note;
import org.pimslims.model.target.ResearchObjective;

/**
 * NoteBeanTest
 * 
 */
public class NoteBeanTest extends TestCase {

    private static final String UNIQUE = "nb" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for NoteBeanTest
     * 
     * @param name
     */
    public NoteBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void test() throws ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ResearchObjective entry =
                new ResearchObjective(version, "name" + System.currentTimeMillis(), "eb" + NoteBeanTest.UNIQUE);
            final Note note = new Note(version, entry);
            note.setDetails("details" + NoteBeanTest.UNIQUE);
            final ModelObjectBean modelObjectBean = new ModelObjectBean(entry);
            Assert.assertEquals(1, modelObjectBean.getNotes().size());
            final NoteBean bean = modelObjectBean.getNotes().iterator().next();
            Assert.assertEquals(note.get_Hook(), bean.getHook());
            Assert.assertEquals(note.getDetails(), bean.getDetails());
            Assert.assertNull(bean.getPerson());
            Assert.assertEquals(0, bean.getSeeAlso().size());
            //TODO Assert.assertNotNull(bean.getDate());
        } finally {
            version.abort(); // not testing persistence here
        }
    }
}
