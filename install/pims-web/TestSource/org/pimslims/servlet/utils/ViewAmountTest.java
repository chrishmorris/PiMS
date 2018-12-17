package org.pimslims.servlet.utils;

import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ServletUtil;

public class ViewAmountTest extends TestCase {

    /**
     * Values for creating a test sample
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>();
    static {
        ViewAmountTest.ATTRIBUTES.put("name", "testSample" + System.currentTimeMillis());
        ViewAmountTest.ATTRIBUTES.put(Sample.PROP_AMOUNTDISPLAYUNIT, "mL");
        ViewAmountTest.ATTRIBUTES.put(Sample.PROP_AMOUNTUNIT, "L");
        ViewAmountTest.ATTRIBUTES.put(Sample.PROP_CURRENTAMOUNT, new Float(0.1f));
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ViewAmountTest.class);
    }

    public ViewAmountTest(final String methodName) {
        super(methodName);
    }

    public void testGetAmountDisplayUnitAttributeName() {
        Assert.assertEquals(Sample.PROP_AMOUNTDISPLAYUNIT,
            ServletUtil.getDisplayUnitAttributeName(Sample.class.getName(), Sample.PROP_CURRENTAMOUNT));
    }

}
