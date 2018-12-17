/**
 * current-pims-web org.pimslims.command.leeds.fermentation Tube.java
 * 
 * @author pvt43
 * @date 21 May 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 pvt43   * 
    * 
  
  */
package org.pimslims.presentation.fermentation;

/**
 * Tube
 * 
 */
public class Tube {

    /*
     * From access db:
     */

    // Membrane prep number
    String name;

    //Prep type
    Enum PrepType;

    public static enum PrepType {
        Inner, Outer, Mixed
    }

    // No.Aliquots
    int numberOfAliquot;

    //Aliquot Volume (ml)
    double aliquotVolume;

    // Concentration (mg/ml)
    double concentration;

    double tubeMass;

    //?
    String destination;

    String notes;

    float outerMembRemains;

    float innerMembRemains;

    float mixedMembRemains;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String tube = "Name: " + this.name + "\n";

        //Prep type
        tube += "PrepType: " + this.PrepType + "\n";
        // No.Aliquots
        tube += "N Aliquotes: " + this.numberOfAliquot + "\n";

        //Aliquot Volume (ml)
        tube += "A volume: " + this.aliquotVolume + "\n";

        // Concentration (mg/ml)
        tube += "Conc: " + this.concentration + "\n";

        tube += "Tube Mass: " + this.tubeMass + "\n";

        //?
        tube += "Destination: " + this.destination + "\n";

        tube += "Notes: " + this.notes + "\n";

        tube += "Outer Memb remains:" + this.outerMembRemains + "\n";

        tube += "inner Memb remains:" + this.innerMembRemains + "\n";

        tube += "mixed Memb remains:" + this.mixedMembRemains + "\n";

        return tube;

    }

    public double getTotalProtein() {
        return this.aliquotVolume * this.concentration * this.numberOfAliquot;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof Tube && obj != null) {
            return this.name.equals(((Tube) obj).name);
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
