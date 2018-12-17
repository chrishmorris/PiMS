/**
 * 
 * @author Marc Savitsky
 * @date 17 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.lab.primer.CodonBean;
import org.pimslims.test.AbstractTestCase;

/**
 */
public class CodonBeanTest extends AbstractTestCase {

    private static final Collection<CodonBean> TABLE = CodonBean.getTranslationTable();

    /**
     * 
     * @param name
     */
    public CodonBeanTest(final String name) {
        super(name);
    }

    /**
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void printTranslationTable() {

        for (final CodonBean bean : CodonBeanTest.TABLE) {
            System.out.println("bean [" + bean.toString() + "]");
        }
    }

    public void test() {
        Assert.assertEquals(4 * 4 * 4, CodonBeanTest.TABLE.size());
        final Set<String> codons = new HashSet(64);
        for (final CodonBean bean : CodonBeanTest.TABLE) {
            final String triplet = bean.getTriplet();
            Assert.assertFalse("Duplicated entry for: " + triplet, codons.contains(triplet));
            codons.add(triplet);
        }
    }

    public void testLysine() {
        int count = 0;
        for (final CodonBean bean : CodonBeanTest.TABLE) {
            if (bean.getAminoAcid().equals("Lys")) {
                count++;
                Assert.assertTrue("AAA".equals(bean.getTriplet()) || "AAG".equals(bean.getTriplet()));
            }
        }
        Assert.assertEquals(2, count);
    }

    public void testStop() {
        int count = 0;
        for (final CodonBean bean : CodonBeanTest.TABLE) {
            if (bean.getAminoAcid().equals("Ter")) {
                count++;
                Assert.assertTrue("Bad stop codon: " + bean.getTriplet(), "TAG".equals(bean.getTriplet())
                    || "TAA".equals(bean.getTriplet()) || "TGA".equals(bean.getTriplet()));
            }
        }
        Assert.assertEquals(3, count);
    }
}
