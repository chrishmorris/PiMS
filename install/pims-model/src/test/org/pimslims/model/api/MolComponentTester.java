/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.HashMap;

import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.molecule.Molecule;

/**
 * Tests generated class for molecular components
 */
public class MolComponentTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating one
     */
    public static final HashMap ATTRIBUTES = new HashMap();
    static {
        ATTRIBUTES.put("name", "test component");
        ATTRIBUTES.put(Molecule.PROP_MOLTYPE, "other");
    }

    /**
     * 
     */
    public MolComponentTester() {
        super(org.pimslims.model.molecule.Molecule.class.getName(), ModelImpl.getModel(),
            "testing implementation of molecular components", ATTRIBUTES);
    }

    /**
     * Check that the metadata lists all subclasses
     */
    public void testSubclass() {
        MetaClass ac = model.getMetaClass(org.pimslims.model.molecule.Substance.class.getName());
        java.util.Collection subclasses = ac.getSubtypes();
        assertTrue("is subclass", subclasses.contains(this.metaClass));
        assertTrue("is subclass, not previously loaded",
            subclasses.contains(model.getMetaClass(org.pimslims.model.molecule.Molecule.class.getName())));
    }

    /**
     * TODO test the support for a Boolean attribute
     * 
     * public void testIsSoluble() {
     * 
     * assertNotNull("attribute exists", metaClass.getAttribute("isSoluble")); try { WritableVersion wv =
     * model.getWritableVersion(AbstractModel.SUPERUSER); // create ModelObject testObject =
     * wv.create(javaClass, ATTRIBUTES); String hook = testObject.get_Hook(); assertNotNull("created",
     * wv.get(hook) );
     * 
     * testObject.set_Value("isSoluble", Boolean.TRUE); assertTrue("was set",
     * testObject.get_Value("isSoluble")==Boolean.TRUE);
     * 
     * 
     * wv.delete(testObject); wv.abort(); // not testing persistence here } catch (final ModelException ex) {
     * ex.printStackTrace(); fail(ex.getMessage()); } catch (final RuntimeException ex) { Throwable cause =
     * ex.getCause(); cause.printStackTrace(); fail(cause.getMessage()); } }
     */

    /**
     * Check that a subtype relationship is reported correctly
     */
    public void testSubtype() {
        MetaClass ac = model.getMetaClass(org.pimslims.model.molecule.Substance.class.getName());
        assertTrue("number of subtypes", ac.getSubtypes().size() > 0);
        assertTrue("subtype", ac.getSubtypes().contains(metaClass));
    }

    public void testGetMetaAttributes() {
        MetaClass ma = model.getMetaClass(org.pimslims.model.molecule.Molecule.class.getName());
        for (String attriName : ma.getAttributes().keySet()) { // System.out.println(attriName);
            assertNotNull(ma.getAttribute(attriName));
        }
        assertNotNull(ma.getAttribute("molecularMass"));
        // System.out.println(ma.getAttribute("molecularMass").getName());
    }
}
