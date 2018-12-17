/**
 * current-pims-web org.pimslims.command.leeds.fermentation FermentationFormBean.java
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

import java.util.ArrayList;
import java.util.Date;

import org.pimslims.lab.Util;

/**
 * FermentationFormBean
 * 
 */
public class FermentationBatch {

    /*
    "Ordered?"
     "Prep no."
        "Protein type"
        "Batch"
        "Box No."
        "Inners remaining"
        "Outers remaining"
        "Mixed remaining"
        "Notes"
        "Tested for expression?"

    */
    /*
    For each table row create 1 experiment.
    Exp.Type = Large scale fermentation
    Exp.Protocol= Large scale fermentation
    Start date = Date
    End date = Date
    Create one output sample – Cells.
    Scientist = made by
    Target = Protein (bat search for Target.name, Target.alias, Target.Protein.name)
    Batch number
    */

    /**
     * From access:
     */
    //Batch
    String batch;

    //Operator
    String scientist;

    //Culture date
    Date start;

    //?
    Date end;

    //Strain details
    //Protein
    String comments;

    // Total cell mass (Mass)
    float mass;

    ArrayList<Tube> tubes;

    @Override
    public String toString() {
        //Batch
        String sbatch = "Batch " + this.batch + "\n";

        sbatch += "scientist " + this.scientist + "\n";

        sbatch += "start " + this.start + "\n";

        sbatch += "end " + this.end + "\n";

        sbatch += "comments " + this.comments + "\n";

        sbatch += "mass " + this.mass + "\n";

        sbatch += "tubes: " + "\n";
        if (this.tubes != null) {
            for (final Tube t : this.tubes) {
                sbatch += t.toString() + "\n";
            }
        }

        return sbatch;
    }

    public void addTube(final Tube tube) {
        if (this.tubes == null) {
            this.tubes = new ArrayList<Tube>();
        }
        this.tubes.add(tube);
    }

    public Tube getTube(final String name) {
        if (this.tubes != null && !Util.isEmpty(name)) {
            for (final Tube t : this.tubes) {
                final String prepNum = t.name;
                if (prepNum != null && prepNum.equals(name)) {
                    return t;
                }
            }
        }
        return null;
    }
}
