/**
 * 
 */
package org.pimslims.business.crystallization.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.business.exception.BusinessException;


/**
 * <p>Class to allow one to describe the volume at a specific
 * WellPosition for a set of WellPosition. Its main intended
 * use is to describe the volume to be transfered from
 * DeepWellPlate to TrialPlate in a plate filling operation
 * and the volume of reservoir to be transfered as mother
 * liquor in a plate setup operation. It also serves as a base
 * class for the description of sample volumes transfered in a
 * plate setup operation.</p>
 * 
 * @author Jon Diprose
 */
public class VolumeMap {
    
    /**
     * <p>ZERO_VOLUME = new Double(0d).</p>
     */
    public static final Double ZERO_VOLUME = new Double(0d);
    
    /**
     * The Map that underpins this VolumeMap
     */
    private final Map<WellPosition, Double> volumes = new HashMap<WellPosition, Double>();
    
    /**
     * <p>The volume unit for all the volumes in this VolumeMap.</p>
     */
    private final String unit;
    
    /**
     * <p>Assess whether the specified VolumeMaps share the same unit.</p>
     * 
     * @param lv
     * @param v
     * @return
     * @throws BusinessException if units are not common
     */
    public static String getCommonUnit(List<? extends VolumeMap> lv, VolumeMap v) throws BusinessException {
        
        // All volumeMaps must have same unit
        boolean gotUnit = false;
        String commonUnit = null;
        if (null != v) {
            gotUnit = true;
            commonUnit = v.getUnit();
        }
        if (null != lv) {
            for (VolumeMap volumeMap: lv) {
                if ((false == gotUnit) && (null != volumeMap)) {
                    gotUnit = true;
                    commonUnit = volumeMap.getUnit();
                }
                else if ((gotUnit == true) && (null != volumeMap)) {
                    if (
                            ((null == commonUnit) && (null != volumeMap.getUnit())) ||
                            ((null == commonUnit) && (null != volumeMap.getUnit())) ||
                            (! commonUnit.equals(volumeMap.getUnit()))
                    ) {
                        throw new BusinessException("units not common");
                    }
                }
            }
        }
        
        return commonUnit;
        
   }
    
    /**
     * <p>Construct a VolumeMap whose volumes all have the specified
     * unit.</p>
     * 
     * @param unit - the unit that applies to all the volumes in this VolumeMap
     */
    public VolumeMap(String unit) {
        if (null == unit) {
            throw new IllegalArgumentException("unit must not be null");
        }
        this.unit = unit;
    }
    
    /**
     * @return The unit for all the volumes in this VolumeMap
     */
    public String getUnit() {
        return this.unit;
    }
    
    /**
     * <p>Get the volume at the specified WellPosition. If the
     * volume at this WellPosition has not been defined then
     * {@link #ZERO_VOLUME} will be returned.</p>
     * 
     * @param wellPosition - the WellPosition for which to return the volume
     * @return The volume at the specified WellPosition or ZERO_VOLUME
     */
    public Double getVolume(WellPosition wellPosition) {
        if (this.volumes.containsKey(wellPosition)) {
            return this.volumes.get(wellPosition);
        }
        return ZERO_VOLUME;
    }
    
    /**
     * <p>Set the volume at the specified WellPosition. The volume must
     * be >= {@link #ZERO_VOLUME} or an IllegalArgumentException will result. Setting
     * volume to {@link #ZERO_VOLUME} is equivalent to calling
     * {@link #removeVolume(WellPosition)}.</p>
     * 
     * @param wellPosition - the WellPosition for which to set the volume
     * @param volume - the volume at the specified WellPosition
     */
    public void setVolume(WellPosition wellPosition, Double volume) {
        
        // Compare volume to ZERO_VOLUME
        int cmp = ZERO_VOLUME.compareTo(volume);
        
        // ZERO_VOLUME is bigger than volume
        if (cmp > 0) {
            throw new IllegalArgumentException("volume must be >= 0d");
        }
        
        // ZERO_VOLUME is smaller than volume
        else if (cmp < 0) {
            this.volumes.put(wellPosition, volume);
        }
        
        // ZERO_VOLUME equals volume
        else {
            removeVolume(wellPosition);
        }
        
    }
    
    /**
     * <p>Remove the specified WellPosition from the VolumeMap.
     * Subsequent calls to {@link #getVolume(WellPosition)} for
     * this WellPosition will return {@link #ZERO_VOLUME}.</p>
     * 
     * @param wellPosition - the WellPosition to remove from this VolumeMap
     */
    public void removeVolume(WellPosition wellPosition) {
        this.volumes.remove(wellPosition);
    }
    
    /**
     * <p>Return the Set of WellPosition that have had their volumes
     * explicitly set.</p>
     * 
     * @return The Set of set WellPosition
     */
    public Set<WellPosition> getSetWellPositions() {
        return this.volumes.keySet();
    }
    
}
