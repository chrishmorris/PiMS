/**
 * pims-web org.pimslims.command.newtarget BioDBReferencesTester.java
 * 
 * @author Peter Troshin aka pvt43
 * @date 30 Nov 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 pvt43 
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.StringReader;

import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.pimslims.bioinf.newtarget.BioDBReferences;

import junit.framework.TestCase;

/**
 * BioDBReferencesTester
 * 
 */
public class BioDBReferencesTester extends TestCase {

    RichSequence bio = null;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        BufferedReader br =
            new BufferedReader(new StringReader(BioJavaGenBankProteinParserTester.genBankProtein));
        SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
        RichSequenceIterator seqI =
            RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                RichObjectFactory.getDefaultNamespace());
        bio = seqI.nextRichSequence();

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        bio = null;
    }

    public void testBioXReferences() {
        FeatureHolder cds = bio.filter(new FeatureFilter.ByType("CDS"));
        RichFeature rf = (RichFeature) cds.features().next();
        BioDBReferences bioxfers = new BioDBReferences(rf.getRankedCrossRefs());
        //System.out.println(bioxfers);
        assertNotNull(bioxfers);
        assertEquals("2777494", bioxfers.getAccession("GeneID"));
    }
}
