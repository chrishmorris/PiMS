/**
 * current-datamodel org.pimslims.upgrader Version34Test.java
 * 
 * @author cm65
 * @date 22 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.upgrader;

import java.util.Calendar;
import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ContainerType;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.InstrumentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ExpressionObjective;
import org.pimslims.model.target.ResearchObjective;

/**
 * Version34Test
 * 
 * A collection of test cases to verify the changes from DM 33 to DM 34
 * 
 */
@SuppressWarnings("deprecation")
public class Version42Test extends TestCase {

    private static final String UNIQUE = "v42_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version42Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testContainer() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {

            // containers
            Container loop = new Holder(version, UNIQUE + "loop", null);
            ContainerType loopType = new HolderType(version, UNIQUE + "loop");
            loop.setContainerType(loopType);
            assertEquals(UNIQUE + "loop", loopType.getName());
            Container room = new Location(version, UNIQUE + "room");
            assertEquals("Location", room.getContainerType().getName());

            // things that can be contained
            Containable sample = new Sample(version, UNIQUE);
            sample.setContainer(loop);
            assertEquals(loop, sample.getContainer());
            loop.setContainer(room);
            assertEquals(room, loop.getContainer());
            Container lab = new Location(version, UNIQUE + "lab");
            room.setContainer(lab);
            assertEquals(lab, room.getContainer());
            version.flush();

            // contents
            assertEquals(1, loop.getContained().size());
            assertEquals(sample, loop.getContained().iterator().next());
            assertEquals(1, room.getContained().size());
            assertEquals(loop, room.getContained().iterator().next());
            assertEquals(1, lab.getContained().size());
            assertEquals(room, lab.getContained().iterator().next());

            // container dimensions
            assertEquals(0, loop.getDimension());
            CrystalType type = new CrystalType(version, CrystalType.NO_RESERVOIR, UNIQUE);
            type.setMaxColumn(12);
            type.setMaxRow(8);
            type.setMaxSubPosition(3);
            assertEquals(3, type.getDimension());

            version.flush(); // check these values can be saved
        } finally {
            version.abort();
        }
    }

    //PIMS-2810 and PIMS-3625
    public void testPermission() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            LabNotebook book = new LabNotebook(version, UNIQUE);
            UserGroup group = new UserGroup(version, UNIQUE);
            Permission p = new Permission(version, PIMSAccess.READ, book, group);
            assertEquals(book, p.getLabNotebook());
            book.delete();
            version.flush();
        } finally {
            version.abort();
        }
    }

    //PIMS-3166 add "supervised by" association
    public void testUser() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            User student = new User(version, UNIQUE);
            assertEquals(null, student.getSupervisor());
            User supervisor = new User(version, "prof" + UNIQUE);
            student.setSupervisor(supervisor);
            version.flush(); // ensure all DB constraints are exercised

            //MetaClass mc = model.getMetaClass(User.class.getName());
            Collection supervisors = student.get(User.PROP_SUPERVISOR);
            assertNotNull(supervisors);

            Collection students = supervisor.getSupervisees();
            assertEquals(1, students.size());
            supervisor.removeSupervisee(student);
            assertTrue(supervisor.getSupervisees().isEmpty());
            assertNull(student.getSupervisor());
            supervisor.addSupervisee(student);
            assertEquals(supervisor, student.getSupervisor());

        } finally {
            version.abort();
        }
    }

    public void testResearchObjective() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            org.pimslims.model.experiment.Project project = new ResearchObjective(version, UNIQUE, UNIQUE);
            assertTrue(ExpressionObjective.class.isInstance(project));
            assertTrue(org.pimslims.model.experiment.Project.class.isInstance(project));
            project.setClientCostCode(UNIQUE);
            Collection<ExpressionObjective> eos = project.getExpressionObjectives();
            assertEquals(1, eos.size());
            assertTrue(eos.contains((ExpressionObjective) project));
            assertEquals(project, eos.iterator().next().getProject());

            version.flush(); // ensure all DB constraints are exercised
            assertEquals(UNIQUE, project.getClientCostCode());
            assertEquals(UNIQUE,
                ((ResearchObjective) project).get_Value(ResearchObjective.PROP_CLIENTCOSTCODE));
            assertTrue(((ExpressionObjective) project).getExpressionObjectiveElements().isEmpty());

        } finally {
            version.abort();
        }
    }

    public void testExperiment() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment mo = new Experiment(version, UNIQUE, NOW, NOW, type);
            assertNull(mo.getProject());
            mo.setInvoiceNumber(UNIQUE);
            version.flush(); // ensure all DB constraints are exercised
            assertEquals(UNIQUE, mo.getInvoiceNumber());
            assertEquals(UNIQUE, mo.get_Value(Experiment.PROP_INVOICENUMBER));
        } finally {
            version.abort();
        }
    }

    public void testSample() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Sample mo = new Sample(version, UNIQUE);
            mo.setInvoiceNumber(UNIQUE);
            version.flush(); // ensure all DB constraints are exercised
            assertEquals(UNIQUE, mo.getInvoiceNumber());
            assertEquals(UNIQUE, mo.get_Value(Sample.PROP_INVOICENUMBER));
        } finally {
            version.abort();
        }
    }

    // uniqueness constraint added for names of 
    // ExperimentGroup, Instrument, Method, Software, Location, CyParameterDefinition
    public void testUniqueness() {
        WritableVersion version = this.model.getTestVersion();
        try {
            new Instrument(version, UNIQUE);
            new Instrument(version, UNIQUE);
            version.flush(); // ensure all DB constraints are exercised
            fail("Created with same name");
        } catch (ConstraintException e) {
            // that's fine
        } finally {
            version.abort();
        }
    }

    public void testCrystalType() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            CrystalType type = new CrystalType(version, CrystalType.NO_RESERVOIR, UNIQUE);
            type.setMaxSubPosition(4);
            assertEquals(4, type.getSubpositions().length);
            /*  for (int i : type.getSubpositions()) {
                 // we want to be able to make a loop like this!
             } */
            type.setHasReservoir(true);
            assertEquals(3, type.getSubpositions().length);
        } finally {
            version.abort();
        }
    }

    public void testHolder() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Holder holder = new Holder(version, UNIQUE, null);
            final Location location = new Location(version, "loc" + UNIQUE);
            holder.setContainer(location);
            assertEquals(location, holder.getContainer());
            holder.setContainer(null);
            Assert.assertEquals(null, holder.getContainer());

            final Holder parent = new Holder(version, "p" + UNIQUE, null);
            holder.setContainer(parent);
            assertEquals(parent, holder.getContainer());
            holder.setContainer(null);
            Assert.assertEquals(null, holder.getContainer());

            holder.setParentHolder(parent);
            holder.setContainer(location);
            assertNull(holder.getParentHolder());

        } finally {
            version.abort();
        }
    }

    // better support for instruments and reagents
    public void testProtocol() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType experimentType = new ExperimentType(version, UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE, experimentType);
            InstrumentType type = new InstrumentType(version, UNIQUE);
            protocol.setInstrumentType(type);
            version.flush(); // ensure all DB constraints are exercised
            assertEquals(type, protocol.getInstrumentType());
            assertEquals(type, protocol.get_Value(Protocol.PROP_INSTRUMENTTYPE));

            SampleCategory category = new SampleCategory(version, UNIQUE);
            RefSample recipe = new RefSample(version, UNIQUE);
            recipe.addSampleCategory(category);
            RefInputSample ris = new RefInputSample(version, category, protocol);
            ris.setRecipe(recipe);
            version.flush();
            assertEquals(recipe, ris.getRecipe());
            assertEquals(recipe, ris.get_Value(RefInputSample.PROP_RECIPE));
        } finally {
            version.abort();
        }
    }

    /* no, dont need a replacement for ImageType, that is under access control
    public void testImageType() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            org.pimslims.model.crystallization.WellImageType type =
                new org.pimslims.model.crystallization.WellImageType(version, UNIQUE);
            LabBookEntry entry = type;
            Instrument instrument = new Instrument(version, UNIQUE);
            instrument.setDefaultWellImageType(type);
            Image image = new Image(version, UNIQUE, UNIQUE);
            image.setWellImageType(type);
            version.flush(); // ensure all DB constraints are exercised
            assertEquals(entry, version.findFirst(org.pimslims.model.crystallization.WellImageType.class,
                org.pimslims.model.crystallization.WellImageType.PROP_NAME, UNIQUE));
            assertEquals(type, instrument.getDefaultWellImageType());
            assertEquals(type, image.getWellImageType());
        } finally {
            version.abort();
        }
    } */

    // PIMS-3618 Instrument needs "file path" attribute, containing the URL where the output files are kept.
    public void testInstrument() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Instrument mo = new Instrument(version, UNIQUE);
            mo.setFilePath("http://test");
            version.flush(); // ensure all DB constraints are exercised
            assertEquals("http://test", mo.getFilePath());

        } finally {
            version.abort();
        }
    }
}
