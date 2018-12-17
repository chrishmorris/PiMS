/**
 * pims-web org.pimslims.bioinf.local AlignmentParameters.java
 * 
 * @author cm65
 * @date 2 Jun 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 */
package org.pimslims.bioinf.local;

import java.util.Collection;

import org.pimslims.bioinf.util.MatrixLoader;

public class AlignmentParameters {

    private/**
            * the name of the matrix, e.g. BLOSUM62
            */
    String matrix;

    //TODO both these parameters become "short" not "float" in later biojava
    final short openingPenalty;

    final short extend;

    // was public float open;

    AlignmentParameters(final String matrixName, final short openingPenalty, final short extend) {

        this.setMatrix(matrixName);
        this.openingPenalty = openingPenalty;
        this.extend = extend;
    }

    public void setMatrix(final String matrixName) {
        this.matrix = matrixName;
    }

/*
    public void setOpeningPenalty(final float openingPenalty) {
        this.openingPenalty = openingPenalty;
    }

    public void setExtend(final float extend) {
        this.extend = extend;
    } */

    public static Collection<String> getMatrixList() {
        return MatrixLoader.list();
    }

    /**
     * @return Returns the matrix.
     */
    String getMatrix() {
        return this.matrix;
    }

}
