/** 
 * xtalPiMS org.pimslims.crystallization.tools OptimisationScreenGenerator.java
 * @author Jon Diprose
 * @date 2 May 2008
 *
 *       Protein Information Management System
 * @version: 2.2
 *
 * Copyright (c) 2008 Jon Diprose
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.tools;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.ScreenType;
import org.pimslims.business.crystallization.model.VolumeMap;
import org.pimslims.business.crystallization.model.WellPosition;

/**
 * <p>Generate an optimisation screen by mixing specified
 * amounts of Conditions. The amounts are described in
 * VolumeMap isntances and added to the mixture by calling
 * {@link #addConditionVolumeMap(Condition, VolumeMap)}.</p>
 * 
 * <p>Note that, if necessary, you should include a Condition
 * with zero ComponentQuantities (and associated VolumeMap) to
 * represent dilution with water.</p>
 */
public class OptimisationScreenGenerator {
    
    /**
     * <p>Descriptions of the volume of each of the various
     * conditions mixed to create each well of the screen.</p>
     */
    private Map<Condition, VolumeMap> conditionMaps = new HashMap<Condition, VolumeMap>();
    
    /**
     * <p>The common unit of the VolumeMaps in conditionMaps
     */
    private String unit = null;
    
    /**
     * <p>Add a VolumeMap for a specific Condition.</p>
     * 
     * @param condition - the Condition to which volumeMap refers
     * @param volumeMap - the volumes of condition used
     */
    public void addConditionVolumeMap(Condition condition, VolumeMap volumeMap) {
        
        if (null == condition) {
            throw new IllegalArgumentException("condition must be defined");
        }
        
        if (null == volumeMap) {
            throw new IllegalArgumentException("volumeMap must be defined");
        }
        
        if (null == unit) {
            unit = volumeMap.getUnit();
        }
        else if (! unit.equals(volumeMap.getUnit())) {
            throw new IllegalArgumentException("volumeMap has wrong unit (" + volumeMap.getUnit() + "). First volumeMap had unit " + unit);
        }
        
        // Copy volumeMap
        VolumeMap vm = new VolumeMap(unit);
        for (WellPosition wp: volumeMap.getSetWellPositions()) {
            vm.setVolume(wp, volumeMap.getVolume(wp));
        }
        
        // Sum existing
        if (conditionMaps.containsKey(condition)) {
            VolumeMap old = conditionMaps.get(condition);
            for (WellPosition wp: volumeMap.getSetWellPositions()) {
                double d1 = old.getVolume(wp).doubleValue();
                double d2 = vm.getVolume(wp).doubleValue();
                vm.setVolume(wp, new Double(d1 + d2));
            }
        }
        
        // Store
        conditionMaps.put(condition, vm);
        
    }
    
    /**
     * <p>Reset this OptimisationScreenGenerator to its initial state.</p>
     */
    public void reset() {
        unit = null;
        conditionMaps.clear();
    }
    
    /**
     * <p>Generate the screen that results from mixing the
     * &lt;Condition, VolumeMap&gt; that have been added to
     * this OptimisationScreenGenerator.</p>
     * 
     * <p>Note that the name of the screen is used to build
     * the name of the conditions within the screen.</p>
     * 
     * @param name - the name of the generated screen
     * @return The generated Screen
     */
    public Screen generate(String name) {
        
        if (null == name) {
            throw new IllegalArgumentException("name must be defined");
        }
        
        if (null == unit) {
            throw new IllegalStateException("no <Condition, VolumeMap>s added");
        }
        
        // Establish total volume at each WellPosition
        VolumeMap totalVolumeMap = new VolumeMap(unit);
        for (Map.Entry<Condition, VolumeMap> entry: conditionMaps.entrySet()) {
            VolumeMap vm = entry.getValue();
            for (WellPosition wp: vm.getSetWellPositions()) {
                // I think VolumeMap#getVolume(WellPosition) can't return null
                // TODO Check/enforce
                double v = vm.getVolume(wp).doubleValue();
                double tv = totalVolumeMap.getVolume(wp).doubleValue();
                totalVolumeMap.setVolume(wp, new Double(v + tv));
            }
        }
        
        // Generate condition positions
        Map<WellPosition, Condition> conditionPositions = new HashMap<WellPosition, Condition>();
        for (Map.Entry<Condition, VolumeMap> entry: conditionMaps.entrySet()) {
            VolumeMap vm = entry.getValue();
            for (WellPosition wp: vm.getSetWellPositions()) {
                
                // Get the condition at this wellPosition
                Condition newC = conditionPositions.get(wp);
                if (null == newC) {
                    newC = new Condition();
                    newC.setLocalName(name + ":" + wp.toStringNoSubPosition());
                    conditionPositions.put(wp, newC);
                }
                
                // Calculate the dilution factor
                double df = vm.getVolume(wp).doubleValue() / totalVolumeMap.getVolume(wp).doubleValue();
                
                // Add the diluted components to this condition
                for (ComponentQuantity cq: entry.getKey().getComponents()) {
                    
                    ComponentQuantity newCq = new ComponentQuantity();
                    newCq.setComponent(cq.getComponent());
                    newCq.setComponentType(cq.getComponentType());
                    newCq.setQuantity(df * cq.getQuantity());
                    newCq.setUnits(cq.getUnits());
                    
                    // Add newCq to newC, adjusting existing quantity if possible
                    addComponentToCondition(newC, newCq);
                    
                }
                
            }
            
        }
        
        // Create screen
        Screen screen = new Screen();
        screen.setName(name);
        screen.setConditionPositions(conditionPositions);
        screen.setScreenType(ScreenType.Optimisation);
        
        // Return screen
        return screen;
        
    }
    
    /**
     * <p>Add the specified ComponentQuantity to the specified Condition. If the
     * Condition already has a ComponentQuantity with the same Component and
     * the units for the two quantities match, adjust the quantity on the existing
     * ComponentQuantity rather than adding the new ComponentQuantity.</p>
     * 
     * @param condition - the Condition to which componentQuantity should be added
     * @param componentQuantity - the ComponentQuantity to add to condition
     */
    protected void addComponentToCondition(Condition condition, ComponentQuantity componentQuantity) {
        
        for (ComponentQuantity cq: condition.getComponents()) {
            if ((cq.getComponent().getChemicalName().endsWith(componentQuantity.getComponent().getChemicalName())) &&
                (cq.getUnits().equals(componentQuantity.getUnits()))) {
                cq.setQuantity(cq.getQuantity() + componentQuantity.getQuantity());
                return;
            }
        }
        condition.addComponent(componentQuantity);
        
    }

}
