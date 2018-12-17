/**
 * pims-web org.pimslims.presentation.vector SampleComponentBean.java
 * 
 * @author pajanne
 * @date Jun 3, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import org.pimslims.lab.Measurement;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.ModelObjectBean;

/**
 * SampleComponentBean
 * 
 */
public class SampleComponentBean extends ModelObjectBean {

    private VectorBean vector;

    public SampleComponentBean(final SampleComponent component) {
        super(component);
        if (component.getRefComponent() instanceof Construct) {
            this.vector = new VectorBean((Construct) component.getRefComponent());
        }
        if (null != component.getConcentration()) {
            this.values.put("concentration", Measurement.getMeasurement(SampleComponent.PROP_CONCENTRATION,
                component, SampleComponent.PROP_CONCENTRATIONUNIT, SampleComponent.PROP_CONCDISPLAYUNIT));
        }
    }

    /**
     * @return Returns the vector.
     */
    public VectorBean getVector() {
        return this.vector;
    }

    /**
     * @param vector The vector to set.
     */
    public void setVector(final VectorBean vector) {
        this.vector = vector;
    }

}
