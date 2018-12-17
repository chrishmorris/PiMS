/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.Component;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.RefSample;

/**
 * <p>Data Access Object for {@link org.pimslims.business.crystallization.model.Component}.</p>
 * 
 * <p>Note that Component.pH is only mapped in the context of a ComponentQuantity.</p>
 * 
 * <p>The following are as yet unmapped:</p>
 * <ul>
 * <li>TODO Component.chemicalImage</li>
 * <li>TODO Component.safetyInformation</li>
 * </ul>
 * 
 * @author Jon Diprose
 */
public class ComponentDAO {
    
    /**
     * <p>Populate a Component from the specified Molecule.</p>
     * 
     * @param molComponent
     * @return
     */
    public Component populateFromMolecule(Molecule molComponent) {
        
        Component c = new Component();
        
        c.setCasNumber(molComponent.getCasNum());
        c.setChemicalName(molComponent.getName());
        c.setId(molComponent.getDbId());
        c.setVolatileComponent((null == molComponent.getIsVolatile()) ? false : molComponent.getIsVolatile());
        
        // c.setPH(pH) is set in ComponentQuantityDAO
        
        // TODO c.setChemicalImage(chemicalImage);
        // TODO c.setSafetyInformation(safetyInformation);
        
        return c;
        
    }

    /**
     * <p>Find or create a Molecule in the specified WritableVersion
     * to represent the specified Component.</p>
     * 
     * @param component - the Component to represent
     * @param wv - the WritableVersion in which to create the representation
     * @return The Molecule that represents component
     * @throws ConstraintException - if something goes wrong
     */
    protected Molecule findOrCreateMolecule(Component component, WritableVersion wv) throws ConstraintException {
        
        Molecule mc = findMolecule(component, wv);
        if (null == mc) {
            mc = createMolecule(component, wv);
        }
        return mc;
        
    }
    
    /**
     * <p>Find the Molecule that represents the specified Component
     * in the specified ReadableVersion.</p>
     * 
     * @param component - the Component to represent
     * @param rv - the ReadableVersion in which to find the representation
     * @return The Molecule that represents component
     */
    protected Molecule findMolecule(Component component, ReadableVersion rv) {
        
        if (component.getId() > 0) {
            ModelObject mo = rv.get(component.getId());
            if (mo instanceof Molecule) {
                return (Molecule) mo;
            }
            return null;
        }
        return rv.findFirst(Molecule.class, RefSample.PROP_NAME, component.getChemicalName());
        
    }
    
    /**
     * <p>Create a Molecule in the specified WritableVersion
     * to represent the specified Component.</p>
     * 
     * @param component - the Component to represent
     * @param wv - the WritableVersion in which to create the representation
     * @return The Molecule that represents component
     * @throws ConstraintException - if something goes wrong
     */
    protected Molecule createMolecule(Component component, WritableVersion wv) throws ConstraintException {
        
        Map<String, Object> attr = new HashMap<String, Object>();
        
        attr.put(Molecule.PROP_CASNUM, component.getCasNumber());
        attr.put(Molecule.PROP_NAME, component.getChemicalName());
        attr.put(Molecule.PROP_MOLTYPE, "other");
        attr.put(Molecule.PROP_ISVOLATILE, component.isVolatileComponent());
        
        // component.getPH() is persisted by ComponentQuantityDAO
        
        // TODO component.getChemicalImage();
        // TODO component.getSafetyInformation();
        
        // Create Molecule
        Molecule mc = new Molecule(wv, attr);
        
        // Apply Molecule's id
        component.setId(mc.getDbId());
        
        // Return Molecule
        return mc;
        
    }
    
}
