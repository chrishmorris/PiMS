/**
 * 
 */
package org.pimslims.business.crystallization.model;

import org.pimslims.business.core.model.Sample;

/**
 * <p>
 * Class to describe the volume of a Sample in a set of WellCondition. Any WellCondition that have not been
 * explicitly set will return {@link VolumeMap#ZERO_VOLUME}.
 * </p>
 * 
 * 
 * @author Jon Diprose
 */
// In use in OPPF-UK
public class SampleVolumeMap extends VolumeMap {

    /**
     * <p>
     * The sample whose volumes are being described.
     * </p>
     */
    private final Sample sample;

    /**
     * <p>
     * A name for this SampleVolumeMap - will be mapped to InputSample.name.
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Construct a SampleVolumeMap for the specified Sample whose volumes all have the specified unit.
     * </p>
     * 
     * @param sample
     * @param unit - the unit that applies to all the volumes in this VolumeMap
     */
    public SampleVolumeMap(String name, Sample sample, String unit) {
        super(unit);
        if (null == sample) {
            throw new IllegalArgumentException("screen must not be null");
        }
        this.sample = sample;
        this.name = name;
    }

    /**
     * @return the sample whose volumes are described
     */
    public Sample getSample() {
        return sample;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

}
