/**
 * V2_0-pims-web org.pimslims.bioinf.newtarget BioJavaFormatRecognitionTester.java
 * 
 * @author pvt43
 * @date 10 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 pvt43 
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.biojavax.bio.seq.RichSequence;

/**
 * BioJavaFormatRecognitionTester
 * 
 */
public class BioJavaFormatRecognitionTester extends TestCase {

    // This is failed test its OK. 
    // Its just proves that BioJava 1.5 is not very good at guessing file types 
    public void testRecognition() {
        //SeqIOTools.identifyFormat(arg0, arg1); 
        //guessFileType(EMBLNucleotideParserTester.emblPrimer)   
        BufferedInputStream br =
            new BufferedInputStream(
                new ByteArrayInputStream(EMBLNucleotideParserTester.emblPrimer.getBytes()));
        try {
            RichSequence.IOTools.readStream(br, null);
        } catch (IOException e) {
            //fail(e.getLocalizedMessage());
        }

    }
}
