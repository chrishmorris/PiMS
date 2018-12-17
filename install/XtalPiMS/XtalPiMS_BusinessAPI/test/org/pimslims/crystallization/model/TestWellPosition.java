/**
 * 
 */
package org.pimslims.crystallization.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pimslims.business.crystallization.model.WellPosition;
import junit.framework.TestCase;


/**
 * @author Jon Diprose
 */
public class TestWellPosition extends TestCase {
    
    public static int ROW = 0;
    public static int COL = 1;
    public static int SUB = 2;
    
    public static final String A1 = "A01";
    public static final int[] COORD_A1 = {1, 1, 1};
    
    public static final String A1_1 = "A01.1";
    public static final int[] COORD_A1_1 = {1, 1, 1};
    
    public static final String D8 = "D08";
    public static final int[] COORD_D8 = {4, 8, 1};
    
    public static final String D8_3 = "D08.3";
    public static final int[] COORD_D8_3 = {4, 8, 3};
    
    public static final String AA790_2 = "AA790.2";
    public static final int[] COORD_AA790_2 = {27, 790, 2};
    
    public static final String BGB22790_45 = "BGB22790.45";    
    public static final int[] COORD_BGB22790_45 = {1536, 22790, 45};
    
    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(int, int, int)}.
     */
    public void testWellPositionIntIntInt() {
        WellPosition wk = new WellPosition(COORD_A1_1[ROW], COORD_A1_1[COL], COORD_A1_1[SUB]);
        assertEquals(A1_1, wk.toString());
        
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(int, int, int)}.
     */
    public void testWellPositionIntIntInt2() {
        WellPosition wk = new WellPosition(COORD_D8_3[ROW], COORD_D8_3[COL], COORD_D8_3[SUB]);
        assertEquals(D8_3, wk.toString());
        
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(int, int, int)}.
     */
    public void testWellPositionIntIntIntBig() {
        WellPosition wk = new WellPosition(COORD_AA790_2[ROW], COORD_AA790_2[COL], COORD_AA790_2[SUB]);
        assertEquals(AA790_2, wk.toString());
        
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(int, int, int)}.
     */
    public void testWellPositionIntIntIntBig2() {
        WellPosition wk = new WellPosition(COORD_BGB22790_45[ROW], COORD_BGB22790_45[COL], COORD_BGB22790_45[SUB]);
        assertEquals(BGB22790_45, wk.toString());
        
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(int, int, int)}.
     */
    public void testWellPositionIntIntIntFail() {
        try {
            new WellPosition(-1535, 22789, 27);
            fail();
        }
        catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionString() {
        WellPosition wk = new WellPosition(A1);
        assertEquals(COORD_A1[ROW], wk.getRow());
        assertEquals(COORD_A1[COL], wk.getColumn());
        assertEquals(COORD_A1[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionString2() {
        WellPosition wk = new WellPosition(D8);
        assertEquals(COORD_D8[ROW], wk.getRow());
        assertEquals(COORD_D8[COL], wk.getColumn());
        assertEquals(COORD_D8[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringSub() {
        WellPosition wk = new WellPosition(A1_1);
        assertEquals(COORD_A1_1[ROW], wk.getRow());
        assertEquals(COORD_A1_1[COL], wk.getColumn());
        assertEquals(COORD_A1_1[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringSub2() {
        WellPosition wk = new WellPosition(D8_3);
        assertEquals(COORD_D8_3[ROW], wk.getRow());
        assertEquals(COORD_D8_3[COL], wk.getColumn());
        assertEquals(COORD_D8_3[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringBig() {
        WellPosition wk = new WellPosition(AA790_2);
        assertEquals(COORD_AA790_2[ROW], wk.getRow());
        assertEquals(COORD_AA790_2[COL], wk.getColumn());
        assertEquals(COORD_AA790_2[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringBig2() {
        WellPosition wk = new WellPosition(BGB22790_45);
        assertEquals(COORD_BGB22790_45[ROW], wk.getRow());
        assertEquals(COORD_BGB22790_45[COL], wk.getColumn());
        assertEquals(COORD_BGB22790_45[SUB], wk.getSubPosition());
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringFail() {
        try {
            new WellPosition("B!B22790");
            fail();
        }
        catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringFail2() {
        try {
            new WellPosition("A0");
            fail();
        }
        catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringFail3() {
        try {
            new WellPosition("A01.0");
            fail();
        }
        catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#WellPosition(java.lang.String)}.
     */
    public void testWellPositionStringFail4() {
        try {
            new WellPosition("A01.1.1");
            fail();
        }
        catch (IllegalArgumentException e) {
            // OK
        }
    }
    
    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#equals(Object)}.
     */
    public void testEquals() {
        
        WellPosition[] wp = new WellPosition[5];

        wp[0] = new WellPosition("A1");
        wp[1] = new WellPosition("A01");
        wp[2] = new WellPosition("A0001");
        wp[3] = new WellPosition("A1.1");
        wp[4] = new WellPosition("A01.01");
        
        for (int i = 0; i < wp.length - 1; i++) {
            for (int j = i + 1; j < wp.length; j ++) {
                assertEquals(wp[i], wp[j]);
            }
        }
    
    }
    
    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#hashCode()}
     */
    public void testHashCode() {
        
        WellPosition[] wp = new WellPosition[5];

        wp[0] = new WellPosition("A1");
        wp[1] = new WellPosition("A01");
        wp[2] = new WellPosition("A0001");
        wp[3] = new WellPosition("A1.1");
        wp[4] = new WellPosition("A01.01");
        
        for (int i = 0; i < wp.length - 1; i++) {
            for (int j = i + 1; j < wp.length; j ++) {
                assertEquals(wp[i].hashCode(), wp[j].hashCode());
            }
        }
    
    }
    
    /**
     * Test method for {@link org.pimslims.crystallization.model.WellPosition#compareTo()}
     */
    @SuppressWarnings("unchecked")
    public void testCompareTo() {
        
        List<WellPosition> list = new ArrayList<WellPosition>();

        list.add(new WellPosition("B01.2"));//2
        list.add(new WellPosition("F01.2"));//7
        list.add(new WellPosition("C12.2"));//4
        list.add(new WellPosition("H01.1"));//8
        list.add(new WellPosition("A01.3"));//1
        list.add(new WellPosition("D01.1"));//5
        list.add(new WellPosition("C01.2"));//3
        list.add(new WellPosition("F01.1"));//6
        
        Collections.sort(list);
        
        assertEquals(list.get(0).toString(), "A01.3" );
        assertEquals(list.get(1).toString(), "B01.2" );
        assertEquals(list.get(2).toString(), "C01.2" );
        assertEquals(list.get(3).toString(), "C12.2" );
        assertEquals(list.get(4).toString(), "D01.1" );
        assertEquals(list.get(5).toString(), "F01.1" );
        assertEquals(list.get(6).toString(), "F01.2" );
        assertEquals(list.get(7).toString(), "H01.1" );
    }
    
}
