/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.ComponentQuantity}.
 * </p>
 * 
 * @author Jon Diprose
 */
public class ComponentQuantityDAO {

    /**
     * <p>
     * Populate a ComponentQuantity from the specified SampleComponent.
     * </p>
     * 
     * @param sampleComponent
     * @return
     */
    public ComponentQuantity populateFromSampleComponent(final SampleComponent sampleComponent) {

        final ComponentQuantity cq = new ComponentQuantity();
        cq.setUnits(sampleComponent.getConcentrationUnit());
        cq.setQuantity(sampleComponent.getConcentration());
        cq.setComponentType(sampleComponent.getDetails());

        final ComponentDAO componentDAO = new ComponentDAO();
        final Component component =
            componentDAO.populateFromMolecule((Molecule) sampleComponent.getRefComponent());
        component.setPH((null == sampleComponent.getPh()) ? 0 : sampleComponent.getPh());
        cq.setComponent(component);

        return cq;

    }

    /**
     * <p>
     * Create a SampleComponent attached to the specified RefSample in the specified WritableVersion to
     * represent the specified ComponentQuantity.
     * </p>
     * 
     * @param cq - The ComponentQuantity to represent
     * @param wv - The WritableVersion in which to create the SampleComponent
     * @param refSample - The RefSample to which to attach the SampleComponent
     * @return The SampleComponent that represents cq
     * @throws ConstraintException - if something goes wrong
     */
    protected SampleComponent createSampleComponent(final ComponentQuantity cq, final WritableVersion wv,
        final RefSample refSample) throws ConstraintException {

        final ComponentDAO componentDAO = new ComponentDAO();
        final Molecule mc = componentDAO.findOrCreateMolecule(cq.getComponent(), wv);

        final Map<String, Object> scattr = new HashMap<String, Object>();
        scattr.put(SampleComponent.PROP_ABSTRACTSAMPLE, refSample);
        scattr.put(SampleComponent.PROP_CONCENTRATION, new Float(cq.getQuantity()));
        scattr.put(SampleComponent.PROP_CONCENTRATIONUNIT, cq.getUnits());
        if (cq.getDisplayUnit() != null && cq.getDisplayUnit().trim().length() > 0) {
            scattr.put(SampleComponent.PROP_CONCDISPLAYUNIT, cq.getDisplayUnit());
        } else {
            scattr.put(SampleComponent.PROP_CONCDISPLAYUNIT, cq.getUnits());
        }
        scattr.put(SampleComponent.PROP_DETAILS, cq.getComponentType());
        scattr.put(SampleComponent.PROP_PH, (0 == cq.getComponent().getPH()) ? (Float) null : cq
            .getComponent().getPH());
        scattr.put(SampleComponent.PROP_REFCOMPONENT, mc);

        final SampleComponent sc = new SampleComponent(wv, scattr);
        wv.flush();
        return sc;

    }

}
