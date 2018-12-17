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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaUtils;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.ConferenceCitation;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;

/**
 * Version34Test
 * 
 * A collection of test cases to verify the changes from DM 33 to DM 34
 * 
 */
public class Version34Test extends TestCase {

    private static final String UNIQUE = "v34_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    // string length 80 for testing
    private static final String LONG =
        "12345678901234567890123456789012345678901234567890123456789012345678901234567890";

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version34Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    // needs to test old constructor
    public void testWhyChosenOptional() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            target.setWhyChosen(null);
            version.flush(); // ensure all DB constraints are excercised
            new Target(version, UNIQUE + "two", protein);
        } finally {
            version.abort();
        }
    }

    /**
     * Check that timestamps have been replaced by Calendar instances. This doesn't check much at run time -
     * but if the change hadn't been made, it wouldn't compile.
     * 
     * @throws ConstraintException
     */
    public void testCalendar() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            LabBookEntry page = new Organisation(version, UNIQUE);
            map.put(Citation.PROP_AUTHORS, UNIQUE);
            map.put(Citation.PROP_YEAR, 1970);
            map.put(Attachment.PROP_PARENTENTRY, page);
            ConferenceCitation citation = new ConferenceCitation(version, map);
            Calendar time = citation.getStartDate();
            assertNull(time);
            version.flush(); // ensure all DB constraints are excercised
        } finally {
            version.abort();
        }
    }

    public void testExperimentStatus() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            assertEquals("", experiment.getStatus());
            experiment.setStatus("Unknown");
            assertEquals("Unknown", experiment.getStatus());
            version.flush(); // ensure all DB constraints are excercised
        } finally {
            version.abort();
        }
    }

    public void testUseByDate() throws ConstraintException {
        MetaClass mc = model.getMetaClass(Sample.class.getName());
        MetaAttribute attribute = mc.getAttribute(Sample.PROP_USEBYDATE);
        assertNotNull(attribute);
        assertNotNull(MetaUtils.getColumnName(attribute));
        WritableVersion version = this.model.getTestVersion();
        try {
            Sample sample = new Sample(version, UNIQUE);
            sample.setUseByDate(NOW);
            assertEquals(NOW, sample.getUseByDate());
            version.flush(); // ensure all DB constraints are excercised
        } finally {
            version.abort();
        }
    }

    // needs to test the old constructor
    public void testImageCreator() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            new Person(version, UNIQUE);
            Image image = new Image(version, "path", UNIQUE);
            // was image.setCreator(null);
            version.flush(); // ensure all DB constraints are excercised
            new Image(version, "path", UNIQUE + "two");
        } finally {
            version.abort();
        }
    }
}
