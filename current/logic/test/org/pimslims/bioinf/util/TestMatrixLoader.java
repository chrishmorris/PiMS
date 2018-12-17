/**
 * current-pims-web org.pimslims.bioinf.util TestMatrixLoader.java
 * 
 * @author pvt43
 * @date 2 Jun 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 pvt43   * 
    * 
 * . 
  */
package org.pimslims.bioinf.util;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.FiniteAlphabet;

/**
 * TestMatrixLoader
 * 
 */
public class TestMatrixLoader extends TestCase {
    public void testList() {
        Assert.assertNotNull(MatrixLoader.list());
        Assert.assertFalse(MatrixLoader.list().isEmpty());
        Assert.assertEquals(71, MatrixLoader.list().size());
    }

	public void testLoad() throws IOException {
        Assert.assertNotNull(MatrixLoader.load("PAM100"));
    }

	public void testcanLoadAll() throws IOException {
        for (final String s : MatrixLoader.list()) {
			Assert.assertNotNull(MatrixLoader.load(s));
        }
    }

    public void testBLOSUM62() throws IOException, BioException {

		final String file = MatrixLoader.load("BLOSUM62");

        //final FiniteAlphabet alphabet = DNATools.getDNA(); // also fails with ProteinTools.getTAlphabet();
        final FiniteAlphabet alphabet = (FiniteAlphabet) AlphabetManager.alphabetForName("PROTEIN-TERM");
/*
 * Other options
 * INTEGER
    RNA
    DNA
    STRUCTURE
    PROTEIN
    NUCLEOTIDE
    PROTEIN-TERM
    DOUBLE
 */
		final SubstitutionMatrix matrix = new SubstitutionMatrix(alphabet,
				file, "BLOSUM62");
        Assert.assertNotNull(matrix);
    }
}
