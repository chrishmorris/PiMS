/**
 * 
 */
package org.pimslims.command.leeds;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Util;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;

/**
 * @author Petr Troshin
 * 
 */
@Deprecated
// Location is obsolete
public class TestHolderLocation extends TestCase {

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion wv = null;

    Person person = null;

    Location location = null;

    Location newLocation = null;

    Holder holder = null;

    Holder newholder = null;

    Sample sample = null;

    HolderLocation holderLocation = null;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TestHolderLocation.class);
    }

    /**
     * Constructor for TestPlasmid.
     * 
     * @param methodName
     */
    public TestHolderLocation(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws ParseException, AccessException, ConstraintException,
        ClassNotFoundException, SQLException {

        this.wv = this.model.getTestVersion();
        Assert.assertNotNull(this.wv);

        HashMap prop = new HashMap();
        prop.put(Person.PROP_FAMILYNAME, "test person");
        this.person =
            (Person) Util.getOrCreate(this.wv, org.pimslims.model.people.Person.class.getName(), null, prop);

        prop = new HashMap();
        prop.put(Location.PROP_NAME, "test location");
        this.location =
            (Location) Util.getOrCreate(this.wv, org.pimslims.model.location.Location.class.getName(), null,
                prop);

        prop = new HashMap();
        prop.put(Location.PROP_NAME, "new test location");
        this.newLocation =
            (Location) Util.getOrCreate(this.wv, org.pimslims.model.location.Location.class.getName(), null,
                prop);

        prop = new HashMap();
        prop.put(HolderCategory.PROP_NAME, "box");
        final HolderCategory holderCat =
            (HolderCategory) Util.getOrCreate(this.wv,
                org.pimslims.model.reference.HolderCategory.class.getName(), null, prop);

        prop = new HashMap();
        prop.put(AbstractHolder.PROP_NAME, "box25");
        prop.put(AbstractHolder.PROP_HOLDERCATEGORIES, Util.makeList(holderCat));
        this.holder =
            (Holder) Util.getOrCreate(this.wv, org.pimslims.model.holder.Holder.class.getName(), null, prop);

        prop = new HashMap();
        prop.put(AbstractSample.PROP_NAME, "test sample");
        prop.put(Sample.PROP_HOLDER, this.holder);
        this.sample =
            (Sample) Util.getOrCreate(this.wv, org.pimslims.model.sample.Sample.class.getName(), null, prop);

        prop = new HashMap();
        prop.put(AbstractHolder.PROP_NAME, "new box25");
        prop.put(AbstractHolder.PROP_HOLDERCATEGORIES, Util.makeList(holderCat));
        this.newholder =
            (Holder) Util.getOrCreate(this.wv, org.pimslims.model.holder.Holder.class.getName(), null, prop);

        prop = new HashMap();
        prop.put(HolderLocation.PROP_LOCATION, this.location);
        prop.put(HolderLocation.PROP_HOLDER, this.holder);
        prop.put(HolderLocation.PROP_STARTDATE, Calendar.getInstance());
        this.holderLocation =
            (HolderLocation) Util.getOrCreate(this.wv,
                org.pimslims.model.holder.HolderLocation.class.getName(), null, prop);
    }

    public void testUpdateLocation() throws AccessException, ConstraintException, ClassNotFoundException,
        SQLException {

        Assert.assertEquals("Location wrong", this.location.getName(), "test location");
        Assert.assertEquals("Holder wrong", this.holder.getName(), "box25");
        Assert.assertEquals("HolderLocation connected to wrong location", this.holderLocation.getLocation()
            .get_Hook(), this.location.get_Hook());
        Assert.assertEquals("HolderLocation connected to wrong location", this.holderLocation.getLocation()
            .getName(), "test location");
        // Cannot use set method here because need to preserve other holders
        // which may be associated with this location
        ContainerUtility.move(this.holder, this.newLocation);
        // Not there should be new location there, should it?
        Assert.assertEquals("Location wrong", "new test location",
            ContainerUtility.getCurrentLocation(this.holder).getName());

    }

    public void testUpdateHolder() throws AccessException, ConstraintException, ClassNotFoundException,
        SQLException {

        Assert.assertEquals("Holder wrong", this.holder.getName(), "box25");
        Assert.assertEquals("Holder set failed ", this.sample.getHolder().getName(), this.holder.getName());
        this.sample.setHolder(this.newholder);
        Assert
            .assertEquals("Holder set failed ", this.sample.getHolder().getName(), this.newholder.getName());
        Assert.assertEquals("Holder set failed ", this.sample.getHolder().getName(), "new box25");
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        Assert.assertFalse(this.wv.isCompleted());
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }

    }

}
