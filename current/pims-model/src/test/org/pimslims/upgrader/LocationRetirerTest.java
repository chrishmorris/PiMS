/**
 * pims-model org.pimslims.upgrader LocationRetirerTest.java
 * 
 * @author cm65
 * @date 30 Jan 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Calendar;
import java.util.Set;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.Note;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.reference.Database;

/**
 * LocationRetirerTest
 * 
 */
@SuppressWarnings("deprecation")
public class LocationRetirerTest extends TestCase {

    private static final String UNIQUE = "lr" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for LocationRetirerTest
     * 
     * @param name
     */
    public LocationRetirerTest(String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.upgrader.LocationRetirer#LocationRetirer(org.pimslims.dao.WritableVersion)}.
     * 
     * @throws ConstraintException
     */
    public void testLocationRetirer() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            new LocationRetirer(version);
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.upgrader.LocationRetirer#retire(org.pimslims.model.location.Location)}.
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testRetire() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            location.setDetails("d" + UNIQUE);
            location.setPageNumber("p-" + UNIQUE);
            Holder holder = new LocationRetirer(version).retire(location);
            assertEquals(location.getName(), holder.getName());
            assertEquals(location.getDetails(), holder.getDetails());
            assertEquals(location.getPageNumber(), holder.getPageNumber());
        } finally {
            version.abort();
        }
    }

    public void testAnnotation() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            new Annotation(version, location).setLegend(UNIQUE + "legend");
            Holder holder = new LocationRetirer(version).retire(location);
            Set<Annotation> annotations = holder.getAnnotations();
            assertEquals(1, annotations.size());
            Annotation annotation = annotations.iterator().next();
            assertEquals(UNIQUE + "legend", annotation.getLegend());
        } finally {
            version.abort();
        }
    }

    public void testNote() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            new Note(version, location).setDetails(UNIQUE + "details");
            Holder holder = new LocationRetirer(version).retire(location);
            Set<Attachment> attachments = holder.getAttachments();
            assertEquals(1, attachments.size());
            Attachment attachment = attachments.iterator().next();
            assertEquals(UNIQUE + "details", attachment.getDetails());
        } finally {
            version.abort();
        }
    }

    public void testLink() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            Database dbName = new Database(version, UNIQUE);
            new ExternalDbLink(version, dbName, location);
            Holder holder = new LocationRetirer(version).retire(location);
            Set<Attachment> attachments = holder.getAttachments();
            assertEquals(1, attachments.size());
            ExternalDbLink attachment = (ExternalDbLink) attachments.iterator().next();
            assertEquals(dbName, attachment.getDatabaseName());
        } finally {
            version.abort();
        }
    }

    public void testTempAndPressure() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            location.setDetails("d" + UNIQUE);
            location.setPressureDisplayUnit("bar");
            location.setPressure(1.0f);
            location.setTempDisplayUnit("C");
            location.setTemperature(21.0f);
            Holder holder = new LocationRetirer(version).retire(location);
            assertEquals(location.getDetails() + " Temperature: " + location.getTempWithDisplayUnit()
                + " Pressure: " + location.getPressureWithDisplayUnit(), holder.getDetails());
        } finally {
            version.abort();
        }
    }

    public void testType() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            location.setLocationType("Room");
            Holder holder = new LocationRetirer(version).retire(location);
            assertEquals(location.getDetails() + " locationType: " + location.getLocationType(),
                holder.getDetails());
        } finally {
            version.abort();
        }
    }

    public void testDoesNotRetireContained() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location container = new Location(version, UNIQUE + "container");
            Location location = new Location(version, UNIQUE);
            location.setContainer(container);
            Holder holder = new LocationRetirer(version).retire(location);
            assertNull(holder);
        } finally {
            version.abort();
        }
    }

    public void testRetiresContainer() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location container = new Location(version, UNIQUE + "container");
            Location location = new Location(version, UNIQUE);
            location.setContainer(container);
            Holder holder = new LocationRetirer(version).retire(container);
            assertNotNull(holder);
            assertEquals(1, holder.getContained().size());
            Containable containedHolder = holder.getContained().iterator().next();
            assertEquals(location.get_Name(), containedHolder.getName());
        } finally {
            version.abort();
        }
    }

    public void testContainedHolders() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Holder contained = new Holder(version, UNIQUE + "h", null);
            Location location = new Location(version, UNIQUE);
            new HolderLocation(version, NOW, location, contained);
            new LocationRetirer(version).retire(location);
            AbstractHolder parent = contained.getParentHolder();
            assertNotNull(parent);
            assertEquals(location.getName(), parent.getName());
        } finally {
            version.abort();
        }
    }

    public void testCycle() throws AccessException, ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location container = new Location(version, UNIQUE + "container");
            Location location = new Location(version, UNIQUE);
            location.setContainer(container);
            container.setContainer(location);
            new LocationRetirer(version).retire(location);
            // that's OK, avoided an infinite loop
        } finally {
            version.abort();
        }
    }

    //TODO test lab notebook, 

    public void testRetireAll() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Location location = new Location(version, UNIQUE);
            location.setDetails("d" + UNIQUE);
            location.setPageNumber("p-" + UNIQUE);
            new LocationRetirer(version).retireAll();
            assertNotNull(version.findFirst(Holder.class, AbstractHolder.PROP_NAME, location.getName()));
        } finally {
            version.abort();
        }
    }
}
