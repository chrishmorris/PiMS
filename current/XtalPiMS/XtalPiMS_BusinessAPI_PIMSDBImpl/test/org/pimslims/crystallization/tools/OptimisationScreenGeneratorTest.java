package org.pimslims.crystallization.tools;

import junit.framework.TestCase;

import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.VolumeMap;
import org.pimslims.business.crystallization.model.WellPosition;

public class OptimisationScreenGeneratorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testAddConditionVolumeMap() {
        OptimisationScreenGenerator osg = new OptimisationScreenGenerator();
        osg.addConditionVolumeMap(new Condition(), new VolumeMap("L"));
        osg.generate("test");
    }

    public final void testReset() {
        OptimisationScreenGenerator osg = new OptimisationScreenGenerator();
        osg.addConditionVolumeMap(new Condition(), new VolumeMap("L"));
        osg.reset();
        try {
            osg.generate("test");
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException e) {
            // OK
        }
    }

    public final void testGenerate() {
        
        Condition hCl = createCondition("1.0M HCl", "Hydrochloric Acid", 1D, "M");
        Condition naOH = createCondition("1.0M NaOH", "Sodium Hydroxide", 1D, "M");
        Condition nH3SO4 = createCondition("2.0M NH3SO4", "Ammonium Sulphate", 2D, "M");
        Condition water = new Condition();
        water.setLocalName("MilliQ Water");
        
        double pHshiftDF = 12D/1212D; // 12 ul into a total of 1212 ul
        double condDF = 1200D/1212D; // 1200 ul into a total of 1212 ul
        
        VolumeMap vmHCl = new VolumeMap("L");
        vmHCl.setVolume(new WellPosition(1, 1), new Double(pHshiftDF * 0.000100D));
        vmHCl.setVolume(new WellPosition(1, 2), new Double(pHshiftDF * 0.000085D));
        vmHCl.setVolume(new WellPosition(1, 3), new Double(pHshiftDF * 0.000070D));
        vmHCl.setVolume(new WellPosition(1, 4), new Double(pHshiftDF * 0.000055D));
        
        VolumeMap vmNaOH = new VolumeMap("L");
        vmNaOH.setVolume(new WellPosition(1, 9), new Double(pHshiftDF * 0.000100D));
        vmNaOH.setVolume(new WellPosition(1, 10), new Double(pHshiftDF * 0.000085D));
        vmNaOH.setVolume(new WellPosition(1, 11), new Double(pHshiftDF * 0.000070D));
        vmNaOH.setVolume(new WellPosition(1, 12), new Double(pHshiftDF * 0.000055D));
        
        VolumeMap vmNH3SO4 = new VolumeMap("L");
        vmNH3SO4.setVolume(new WellPosition(1, 1), new Double(condDF * 0.000100D));
        vmNH3SO4.setVolume(new WellPosition(1, 2), new Double(condDF * 0.000085D));
        vmNH3SO4.setVolume(new WellPosition(1, 3), new Double(condDF * 0.000070D));
        vmNH3SO4.setVolume(new WellPosition(1, 4), new Double(condDF * 0.000055D));
        vmNH3SO4.setVolume(new WellPosition(1, 5), new Double(0.000100D));
        vmNH3SO4.setVolume(new WellPosition(1, 6), new Double(0.000085D));
        vmNH3SO4.setVolume(new WellPosition(1, 7), new Double(0.000070D));
        vmNH3SO4.setVolume(new WellPosition(1, 8), new Double(0.000055D));
        vmNH3SO4.setVolume(new WellPosition(1, 9), new Double(condDF * 0.000100D));
        vmNH3SO4.setVolume(new WellPosition(1, 10), new Double(condDF * 0.000085D));
        vmNH3SO4.setVolume(new WellPosition(1, 11), new Double(condDF * 0.000070D));
        vmNH3SO4.setVolume(new WellPosition(1, 12), new Double(condDF * 0.000055D));
        
        VolumeMap vmWater = new VolumeMap("L");
        vmWater.setVolume(new WellPosition(1, 2), new Double(0.000015D));
        vmWater.setVolume(new WellPosition(1, 3), new Double(0.000030D));
        vmWater.setVolume(new WellPosition(1, 4), new Double(0.000045D));
        vmWater.setVolume(new WellPosition(1, 6), new Double(0.000015D));
        vmWater.setVolume(new WellPosition(1, 7), new Double(0.000030D));
        vmWater.setVolume(new WellPosition(1, 8), new Double(0.000045D));
        vmWater.setVolume(new WellPosition(1, 10), new Double(0.000015D));
        vmWater.setVolume(new WellPosition(1, 11), new Double(0.000030D));
        vmWater.setVolume(new WellPosition(1, 12), new Double(0.000045D));
        
        OptimisationScreenGenerator osg = new OptimisationScreenGenerator();
        osg.addConditionVolumeMap(hCl, vmHCl);
        osg.addConditionVolumeMap(naOH, vmNaOH);
        osg.addConditionVolumeMap(nH3SO4, vmNH3SO4);
        osg.addConditionVolumeMap(water, vmWater);
        
        Screen screen = osg.generate("test");
        dumpScreen(screen);
        
        osg.reset();
        osg.addConditionVolumeMap(hCl, vmHCl);
        osg.addConditionVolumeMap(naOH, vmNaOH);
        osg.addConditionVolumeMap(screen.getCondition(new WellPosition(1,10)), vmNH3SO4);
        osg.addConditionVolumeMap(water, vmWater);
        
        screen = osg.generate("test");
        dumpScreen(screen);
        
    }
    
    private Condition createCondition(String conditionName, String componentName, double conc, String unit) {
        
        Component component = new Component();
        component.setChemicalName(componentName);
        
        ComponentQuantity cq = new ComponentQuantity();
        cq.setComponent(component);
        cq.setQuantity(conc);
        cq.setUnits(unit);
        
        Condition condition = new Condition();
        condition.setLocalName(conditionName);
        condition.addComponent(cq);
        
        return condition;
        
    }
    
    private void dumpScreen(Screen screen) {
        
        for (int col = 1; col <=12; col++) {
            WellPosition wp = new WellPosition(1, col);
            Condition c = screen.getCondition(wp);
            System.out.println(wp + ": " + c.getLocalName());
            for (ComponentQuantity cq: c.getComponents()) {
                System.out.println("    " + cq.getQuantity() + cq.getUnits() + " " + cq.getComponent().getChemicalName());
            }
        }
        
    }
    
}
