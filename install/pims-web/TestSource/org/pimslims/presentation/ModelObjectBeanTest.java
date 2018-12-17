package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.mru.MRUController;

public class ModelObjectBeanTest extends TestCase {

    private static final String UNIQUE = "mob" + System.currentTimeMillis();

    private final AbstractModel model;

    public ModelObjectBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    private static final String MENU = "properties:[ { property:\'Name\', val:contextMenuName} ],\r\n"
        + "actions:[ {text:\'View Organisation\', icon:\'actions/view.gif\', url:";

    private static final String DELETE =
        "{text:\'Delete\', icon:\'actions/delete.gif\',\r\n onclick:\'contextmenu_delete(contextMenuName,\\\'";

    public final void testCreateModelObjectBean() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ModelObject object = new Organisation(version, "test" + System.currentTimeMillis());
            final ModelObjectShortBean bean = new ModelObjectBean(object);
            Assert.assertEquals("Organisation", bean.getClassDisplayName());
            Assert.assertEquals(Organisation.class.getName(), bean.getClassName());
            Assert.assertEquals(object.get_Hook(), bean.getHook());
            Assert.assertEquals(object.get_Name(), bean.getName());

            Assert.assertEquals("Organisation: " + object.get_Name(), bean.toString());
            Assert.assertTrue(bean.getMayDelete());
            Assert.assertTrue(bean.getMayUpdate());
            Assert.assertEquals(ModelObjectBeanTest.MENU,
                bean.getMenu().substring(0, ModelObjectBeanTest.MENU.length()));
            Assert.assertTrue(bean.getMenu(),
                bean.getMenu().contains("url:'/View/" + object.get_Hook() + "' } "));
            Assert.assertTrue(bean.getMenu().contains(ModelObjectBeanTest.DELETE));
            Assert.assertTrue(bean.getMenu(),
                bean.getMenu().contains("url:'/Delete/" + object.get_Hook() + "' }"));
            Assert.assertTrue(bean.getMenu().endsWith("]"));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testGetModelObjectBeans() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Collection<ModelObject> objects = new HashSet();
            objects.add(new Organisation(version, "testOne" + System.currentTimeMillis()));
            objects.add(new Organisation(version, "testTwo" + System.currentTimeMillis()));
            objects.add(null);
            final List<ModelObjectBean> beans = ModelObjectBean.getModelObjectBeans(objects);

            Assert.assertEquals(2, beans.size());
            final ModelObjectShortBean bean = beans.iterator().next();
            Assert.assertEquals("Organisation", bean.getClassDisplayName());
            Assert.assertEquals(Organisation.class.getName(), bean.getClassName());
            Assert.assertTrue(bean.getName().startsWith("test"));

            Assert.assertEquals("Organisation: " + bean.getName(), bean.toString());
            Assert.assertTrue(bean.getMayDelete());
            Assert.assertTrue(bean.getMayUpdate());
            Assert.assertEquals(ModelObjectBeanTest.MENU,
                bean.getMenu().substring(0, ModelObjectBeanTest.MENU.length()));
            Assert.assertTrue(bean.getMenu(), bean.getMenu()
                .contains("url:'/View/" + bean.getHook() + "' } "));
            Assert.assertTrue(bean.getMenu().contains(ModelObjectBeanTest.DELETE));
            Assert.assertTrue(bean.getMenu(),
                bean.getMenu().contains("url:'/Delete/" + bean.getHook() + "' }"));
            Assert.assertTrue(bean.getMenu().endsWith("]"));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testAttribute() {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        final String objectName = "test" + System.currentTimeMillis();
        try {
            final Organisation object = new Organisation(version, objectName);
            object.setCity("Gotham");
            bean = new ModelObjectBean(object);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
        final Map<String, Object> values = bean.getValues();
        Assert.assertTrue(values.containsKey(Organisation.PROP_CITY));
        Assert.assertEquals("Gotham", values.get(Organisation.PROP_CITY));
        Assert.assertFalse(bean.getElements().isEmpty());
        Assert.assertEquals(objectName, values.get(Organisation.PROP_NAME));
    }

    public final void testDate() {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Database dbName = new Database(version, ModelObjectBeanTest.UNIQUE);
            final LabBookEntry entry = new Organisation(version, ModelObjectBeanTest.UNIQUE);
            final ExternalDbLink object = new ExternalDbLink(version, dbName, entry);
            bean = new ModelObjectBean(object);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
        final Map<String, Object> values = bean.getValues();
        Assert.assertTrue(values.containsKey(Attachment.PROP_DATE));
        final Calendar date = (Calendar) values.get(Attachment.PROP_DATE);
        Assert.assertNotNull(date);
    }

    public final void testPage() throws ConstraintException {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        try {
            final LabBookEntry entry = new Organisation(version, ModelObjectBeanTest.UNIQUE);
            entry.setPageNumber(ModelObjectBeanTest.UNIQUE);
            bean = new ModelObjectBean(entry);
            Assert.assertEquals(entry.getPageNumber(), bean.values.get(LabBookEntry.PROP_PAGE_NUMBER));
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testMultiAttribute() throws ConstraintException {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation object = new Organisation(version, "test" + System.currentTimeMillis());
            final List<String> addresses = new ArrayList<String>();
            addresses.add("Room B1");
            addresses.add("Daresbury Laboratory");
            object.setAddresses(addresses);
            bean = new ModelObjectBean(object);
        } finally {
            version.abort(); // not testing persistence
        }
        final Map<String, Object> values = bean.getValues();
        Assert.assertTrue(values.containsKey(Organisation.PROP_ADDRESSES));
        final Object value = values.get(Organisation.PROP_ADDRESSES);
        Assert.assertTrue(value.getClass().getName(), value instanceof String);
    }

    public final void test1Role() {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            bean = new ModelObjectBean(protocol);
            final ModelObjectShortBean et =
                (ModelObjectShortBean) bean.getValues().get(Protocol.PROP_EXPERIMENTTYPE);
            Assert.assertNotNull(Protocol.PROP_EXPERIMENTTYPE, et);
            Assert.assertEquals(type.get_Hook(), et.getHook());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testMru() {
        ModelObjectBean bean = null;
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType otherType =
                new ExperimentType(version, "other" + System.currentTimeMillis());
            MRUController.addObject(org.pimslims.access.Access.ADMINISTRATOR, otherType);
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            bean = new ModelObjectBean(protocol);
            final Map<String, String> types = bean.getMru().get(Protocol.PROP_EXPERIMENTTYPE);
            Assert.assertNotNull(types);
            Assert.assertTrue(types.containsKey(otherType.get_Hook()));
            Assert.assertEquals(otherType.get_Name(), types.get(otherType.get_Hook()));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
