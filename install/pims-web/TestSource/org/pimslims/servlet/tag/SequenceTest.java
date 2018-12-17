/**
 * pims-web org.pimslims.tag ViewButtonTest.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.mock.MockPageContext;

/**
 * ViewButtonTest
 * 
 */
public class SequenceTest extends TestCase {

    /**
     * @param methodName
     */
    public SequenceTest(final String methodName) {
        super(methodName);
    }

    /**
     */
    public final void testDoStartTag() throws Exception {
        final Sequence tag = new Sequence();
        final MockPageContext mockPageContext = new MockPageContext();
        tag.setPageContext(mockPageContext);
        tag.setSequence("ATG");
        tag.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertEquals("ATG", output);
        // LATER more tests
    }

    /**
     */
    public final void testEscape() throws Exception {
        final Sequence tag = new Sequence();
        final MockPageContext mockPageContext = new MockPageContext();
        tag.setPageContext(mockPageContext);
        tag.setSequence("\n<ATG");
        tag.doStartTag();
        final String output = mockPageContext.getOutput();
        Assert.assertEquals("ATG", output);
        // LATER more tests
    }

}
