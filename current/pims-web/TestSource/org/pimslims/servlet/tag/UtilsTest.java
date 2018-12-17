/**
 * pims-web org.pimslims.tag ViewButtonTest.java
 * 
 * @author cm65
 * @date 3 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.tag;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;

/**
 * ViewButtonTest
 * 
 */
public class UtilsTest extends TestCase {

    /**
     * @param methodName
     */
    public UtilsTest(final String methodName) {
        super(methodName);
    }

    public void testCsrfToken() {
        final HttpServletRequest request = new MockHttpServletRequest("post", Collections.EMPTY_MAP);
        final java.lang.String token = Utils.csrfToken(request, "/Delete");
        Assert.assertNotNull(token);
    }

    public final void testBelowMaxLength() throws Exception {
        final String result = Utils.truncate("ABCDEFGHIJABCDEFGHIJ", 50);
        Assert.assertTrue(result, result.equals("ABCDEFGHIJABCDEFGHIJ"));
    }

    public final void testAtMaxLength() throws Exception {
        final String result = Utils.truncate("ABCDEFGHIJABCDEFGHIJ", 20);
        Assert.assertTrue(result, result.equals("ABCDEFGHIJABCDEFGHIJ"));
    }

    public final void testOverMaxLength() throws Exception {
        final String result = Utils.truncate("ABCDEFGHIJABCDEFGHIJA", 20);
        Assert.assertTrue(result, result.equals("ABCDEFGHIJABCDEFG..."));
    }

    public final void testEmptyString() throws Exception {
        final String result = Utils.truncate("", 20);
        Assert.assertTrue(result, result.equals(""));
    }

    public final void testShortTruncate() throws Exception {
        final String result = Utils.truncate("ABCDEFGHIJABCDEFGHIJ", 3);
        Assert.assertTrue(result, result.equals("..."));
    }

    public final void testEscapeJS() {
        Assert.assertEquals("aa", ModelObjectShortBean.escapeJS("aa"));
        Assert.assertEquals("b\\'b", ModelObjectShortBean.escapeJS("b'b"));
        Assert.assertEquals("b\\\\b", ModelObjectShortBean.escapeJS("b\\b"));
        // if the javascript is in an onclick attribute, then must replace quotes
        // html does not allow escaping with a back slash
        Assert.assertEquals("b&quot;b", ModelObjectShortBean.escapeJS("b\"b"));
    }

}
